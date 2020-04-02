package com.liaohao.mqtt.base;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author hao
 * @Date 2020/3/31 15:02
 * @Version 1.0
 * @Description 回调类
 **/
public class CustomCallback implements MqttCallback {
    // 客户端连接断开时会执行该方法，可用于断开重连
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("连接断开");
    }

    // subscribe后接收到的消息会执行到这里面
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
    }

    // 当发送端消息发送成功后会执行该方法
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
