package com.liaohao.mqtt;

import com.liaohao.mqtt.service.IMqttSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class MqttApplicationTests {

    @Autowired
    private IMqttSender mqttSender;
    @Test
    void contextLoads() {
        mqttSender.SendTomqtt("test",1,"hello mqtt");
    }

}
