package com.fxz.demo.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TxConsumer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread() {
            @Override
            public void run() {
                DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("tx-consumerGroup");
                defaultMQPushConsumer.setNamesrvAddr("192.168.1.201:9876");
                try {
                    Thread.sleep(10 * 1000);
                    defaultMQPushConsumer.subscribe("txFxz", "*");
                    defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                            if (list != null) {
                                for (MessageExt messageExt : list) {
                                    System.out.println("txMessage->" + messageExt);
                                }
                            }
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    });
                    defaultMQPushConsumer.start();
                } catch (MQClientException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
