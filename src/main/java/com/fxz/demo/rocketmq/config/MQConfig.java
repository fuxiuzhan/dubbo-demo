package com.fxz.demo.rocketmq.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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

    @Value("${mq.default.txgroup:txGroup}")
    private String txGroup;
    @Value("${mq.default.txtopic:tcTopic}")
    private String txTopic;

    private static AtomicLong counter = new AtomicLong(0);

    @ConditionalOnMissingBean(TransactionMQProducer.class)
    @Bean("transactionMQProducer")
    public TransactionMQProducer getTransactionMQProducer() {
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer();
        transactionMQProducer.setNamesrvAddr(nameServer);
        transactionMQProducer.setProducerGroup("tx-" + providerGroup);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("threadpool-prifix-" + counter.getAndIncrement());
                return thread;
            }
        });
        transactionMQProducer.setExecutorService(threadPoolExecutor);
        return transactionMQProducer;
    }

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
