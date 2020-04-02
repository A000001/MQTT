package com.liaohao.mqtt.base;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @Author hao
 * @Date 2020/3/31 11:22
 * @Version 1.0
 * @Description 消息发送客户端
 **/
public class SendClient {

    public static final String host = "tcp://localhost:1883";

    public static final String topicName = "test";

    public static final String clientid = "server";

    private MqttClient client;

    private MqttTopic topic;

//    private String userName = "hao";

//    private String password = "123456";

    private MqttMessage mqttMessage;

    public SendClient() throws MqttException {
        client = new MqttClient(host,clientid,new MemoryPersistence());
        connect();
    }

    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
//        options.setUserName(userName);
//        options.setPassword(password.toCharArray());
        client.setCallback(new CustomCallback());
        try {
            client.connect(options);
            topic = client.getTopic(topicName);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(MqttTopic topic,MqttMessage message) throws MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println(token.isComplete());

    }

    public static void main(String[] args) throws MqttException {
        SendClient serverMQTT = new SendClient();
        serverMQTT.mqttMessage = new MqttMessage();
        serverMQTT.mqttMessage.setQos(1);
        serverMQTT.mqttMessage.setRetained(true);
        while (true) {
            serverMQTT.mqttMessage.setPayload("hello base".getBytes());
            serverMQTT.publish(serverMQTT.topic, serverMQTT.mqttMessage);
            System.out.println(serverMQTT.mqttMessage.isRetained() + "------ratained状态");
        }
    }
}
