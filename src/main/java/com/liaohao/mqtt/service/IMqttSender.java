package com.liaohao.mqtt.service;

import com.liaohao.mqtt.config.MqttConfiguration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * @Author hao
 * @Date 2020/4/1 16:54
 * @Version 1.0
 * @Description TODO
 **/
@Service
@MessagingGateway(defaultRequestChannel = MqttConfiguration.CHANNEL_NAME_OUT)
public interface IMqttSender {

    void SendTomqtt(String payload);

    void SendTomqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    void SendTomqtt(@Header(MqttHeaders.TOPIC) String topic,@Header(MqttHeaders.QOS) int qos, String payload);


}
