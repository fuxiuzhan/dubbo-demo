package com.fxz.demo.rocketmq.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MQConfig {
    @Value("${mq.default.group:fxzGroup}")
    private String defaultGroup;
    @Value("${mq.default.nameserver:192.168.1.201:9876}")
    private String nameServer;
    @Value("${mq.default.topic:fxz}")
    private String topic;
    @Value("${mq.default.tag:*}")
    private String tag;
    @Value("${mq.default.providergroup:providerGroup}")
    private String providerGroup;
    @Value("${mq.default.consumergroup:consumerGroup}")
    private String consumerGroup;


    @ConditionalOnMissingBean(DefaultMQProducer.class)
    @Bean("defaultMQProducer")
    public DefaultMQProducer getProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(providerGroup, true);
        producer.setNamesrvAddr(nameServer);
        return producer;
    }

    @ConditionalOnMissingBean(DefaultMQPushConsumer.class)
    @Bean("defaultMQPushConsumer")
    public DefaultMQPushConsumer getConsumer() throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup, true);
        defaultMQPushConsumer.setNamesrvAddr(nameServer);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.subscribe(topic, tag);
        return defaultMQPushConsumer;
    }
}
