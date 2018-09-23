package com.intelligencencu.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.intelligencencu.activitys.MqttNotificationActivity;
import com.intelligencencu.utils.MQTTMessageHandle;
import com.intelligencencu.NotificationUtils;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * 这是用于接收Mqtt推送信息的类
 */
public class MqttReceiveService extends Service {
    private int notificationID = 0;
    //收到推送执行的方法
    private String temp = "";
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences("house_information_system", MODE_PRIVATE);
        temp = mSharedPreferences.getString("mqtt_temp", "");
        Log.d("MqttReceiveService", "getSharedPreferences temp:" + temp);

        new Thread() {
            @Override
            public void run() {
                super.run();
                goReceiveMQTT();
            }
        }.start();
        Log.d("MqttReceiveService", "MqttReceiveService start");
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();
                    String topic = data.getString("topic");
                    String message = data.getString("message");
                    Log.d("MqttReceviceService", "topic:" + topic + "," + "message:" + message);
                    //新建一个通知，把消息通知出去
                    showNotification(topic, message);
                    break;
            }
        }
    };

    /**
     * 显示弹窗的方法
     *
     * @param topic   订阅的话题
     * @param message 话题内容
     */
    private void showNotification(String topic, String message) {
        //点击通知后开启的画面activity
        if (notificationID == Integer.MAX_VALUE)
            notificationID = 0;
        Intent intent = new Intent(MqttReceiveService.this, MqttNotificationActivity.class);
        intent.putExtra("topic", topic);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getActivity(MqttReceiveService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationUtils notificationUtils = new NotificationUtils(MqttReceiveService.this);
        notificationUtils.sendNotification(topic, message, notificationID, pendingIntent);
        notificationID++;
        Log.d("MqttReceiveService", "showNotification()");
    }

    /**
     * 执行接收消息的方法
     */
    private void goReceiveMQTT() {
        MQTTMessageHandle mqttMessageHandle = new MQTTMessageHandle();

        final CallbackConnection connection = mqttMessageHandle.getCallbackConnection();
        //连接监听
        connection.listener(new Listener() {
            @Override
            public void onConnected() {
                System.out.println("receive connection listener onConnected");
            }

            @Override
            public void onDisconnected() {
                System.out.println("receive connection listener onDisconnected");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("receive connection listener onFailure");
                throwable.printStackTrace();
                System.exit(-2);
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                System.out.println("mqtt:receive from topic:" + topic + "," + "message:" + body);
                if (!body.equals(temp)) {
                    //这里把消息通过Handle通知UI显示
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("topic", topic.toString());
                    bundle.putString("message", body);
                    message.setData(bundle);
                    message.what = 0;
                    handler.sendMessage(message);
                    temp = body;
                    //保存mqtt的消息
                    mSharedPreferences.edit().putString("mqtt_temp", temp).apply();
                }
                ack.run();
            }
        });
        //连接回调
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                System.out.println("receive connection connect onSuccess");
                //连接成功后设置订阅的主题destination和模式
                Topic[] topics = {new Topic("stationInformation", QoS.EXACTLY_ONCE)};
                //subscribe
                connection.subscribe(topics, new Callback<byte[]>() {
                    public void onSuccess(byte[] qoses) {
                        System.out.println("receive connection subscribe onSuccess");
                    }

                    public void onFailure(Throwable value) {
                        System.out.println("connection subscribe onFailure");
                        value.printStackTrace();
                        System.exit(-2);
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("connection connect onFailure");
                value.printStackTrace();
                System.exit(-2);
            }
        });
        // Wait forever..
        synchronized (Listener.class) {
            while (true) {
                try {
                    Listener.class.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在服务销毁的时候保存mqtt的消息
        SharedPreferences.Editor mqtt_temp = mSharedPreferences.edit();
        mqtt_temp.putString("mqtt_temp", temp);
        mqtt_temp.apply();
        Log.d("MqttReceiveService", "onDestroy");
    }
}
