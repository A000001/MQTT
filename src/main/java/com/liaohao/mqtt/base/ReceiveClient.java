package com.liaohao.mqtt.base;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @Author hao
 * @Date 2020/3/31 15:12
 * @Version 1.0
 * @Description 消息接收客户端
 **/
public class ReceiveClient {

    public static final String host = "tcp://localhost:1883";

    public static final String topicName = "test";

    public static final String clientid = "client";

    private MqttConnectOptions options;

    private MqttClient client;

    private MqttTopic topic;

//    private String userName = "hao";
//
//    private String password = "123456";

    private MqttMessage mqttMessage;

    private ScheduledExecutorService schedule;

    private void start() throws MqttException {
        client = new MqttClient(host, clientid, new MemoryPersistence());
        options = new MqttConnectOptions();
        options.setCleanSession(true);
//        options.setUserName(userName);
//        options.setPassword(password.toCharArray());
        client.setCallback(new CustomCallback());
        client.getTopic(topicName);
        options.setWill(topicName,"close".getBytes(),2, true);
        client.connect(options);
        client.subscribe(new String[]{"test"},new int[]{1});
    }

    public static void main(String[] args) throws MqttException {
        ReceiveClient clientMQTT = new ReceiveClient();
        clientMQTT.start();
    }
}
