package com.helloncu.houseinformationsystem.utils;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;
import java.util.LinkedList;

/**
 * Created by liangzhan on 18-3-13.
 * mqtt协议消息总线
 */
public class MQTTMessageHandle {
    String user = env("ACTIVEMQ_USER", "admin");
    String password = env("ACTIVEMQ_PASSWORD", "password");
    String host = env("ACTIVEMQ_HOST", "192.168.1.104");
    int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));

    /**
     * 这是发布主题的方法
     *
     * @param destination 主题名称
     * @param message     主题内容
     */
    public void sendMessage(String destination, String message) {
        MQTT mqtt = new MQTT();
        FutureConnection connection = null;
        try {
            mqtt.setHost(host, port);
            mqtt.setUserName(user);
            mqtt.setPassword(password);
            connection = mqtt.futureConnection();
            connection.connect().await();
            final LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();
            UTF8Buffer topic = new UTF8Buffer(destination);
            Buffer msg = new UTF8Buffer(message);
            //retain设为true的话，mqtt会对消息进行保留，并每隔一段时间重发
            queue.add(connection.publish(topic, msg, QoS.EXACTLY_ONCE, true));
            System.out.println("mqtt:send to topic:" + topic + "," + "message:" + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.disconnect().await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return CallbackConnection 返回连接回调
     */
    public CallbackConnection getCallbackConnection() {
        CallbackConnection connection = null;
        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(host, port);
            mqtt.setUserName(user);
            mqtt.setPassword(password);
            connection = mqtt.callbackConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 这是订阅信息的方法（由于是在子线程循环，故没有返回值）
     * 仅作为例子参考
     *
     * @param destination 订阅主题
     */
    public void receiveMessage(final String destination) {
        try {
            final CallbackConnection connection = getCallbackConnection();
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

                //收到推送执行的方法
                @Override
                public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                    String body = msg.utf8().toString();
                    System.out.println("mqtt:receive from topic:" + topic + "," + "message:" + body);
                    //通知用户
                    ack.run();
                }
            });
            //连接回调
            connection.connect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                    System.out.println("receive connection connect onSuccess");
                    //连接成功后设置订阅的主题destination和模式
                    Topic[] topics = {new Topic(destination, QoS.AT_MOST_ONCE)};
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
                    Listener.class.wait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }
}
