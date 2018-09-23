package com.intelligencencu.services;

import com.intelligencencu.services.MqttReceiveService;

public class MqttBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //开机后启动服务
        startService(new Intent(context,MqttReceiveService.class));
    }

}