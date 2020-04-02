package com.liaohao.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author hao
 * @Date 2020/4/1 10:54
 * @Version 1.0
 * @Description TODO
 **/
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String url="tcp://localhost:1883";

    private String clientId;

    private String topic;

    private int timeout = 30;

    private String username;

    private String password;


}
