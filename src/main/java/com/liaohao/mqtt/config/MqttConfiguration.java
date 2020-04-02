package com.liaohao.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @Author hao
 * @Date 2020/4/1 10:46
 * @Version 1.0
 * @Description TODO
 **/
@Configuration
@IntegrationComponentScan
public class MqttConfiguration {

    public static final  String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    public static final  String CHANNEL_NAME_IN = "mqttInboundChannel";

    private static final byte[] WILL_DATA = "offline".getBytes();

    @Value("${mqtt.url}")
    private String url;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.timeout}")
    private int timeout;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    /**
     * MQTT连接器选项
     * @return
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        // 是否每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名密码
//        options.setUserName(username);
//        options.setPassword(password.toCharArray());
        // 设置连接超时时间（秒）
        options.setConnectionTimeout(timeout);
        // 设置会话心跳时间（s）
        // 服务器会每隔1.5*20秒的时间想客户端发送心跳判断客户端是否在线，但是并没有重连机制
        options.setKeepAliveInterval(20);
        // 设置“遗嘱”消息话题，若客户端和服务端之间的连接意外中断，服务器将发布客户端的“遗嘱”消息
        options.setWill("willtopic", WILL_DATA, 2, false);
        options.setServerURIs(url.split(","));
        return options;
    }

    /**
     * MQTT客户端
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    // =================================入站通配适配器配置=================================
    /**
     * MQTT消息通道（消费者）
     * @return
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息订阅绑定（消费者）
     * @return
     */
    @Bean
    public MessageProducer inbound(){
        // 可以同时订阅多个topic
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,mqttClientFactory(),topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 接收到的消息处理器
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                System.out.printf("====================消息体：%s============", message.getPayload());
            }
        };
    }


    // =================================出站通配适配器配置=================================
    /**
     * MQTT消息通道（生产者）
     * @return
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }

}
