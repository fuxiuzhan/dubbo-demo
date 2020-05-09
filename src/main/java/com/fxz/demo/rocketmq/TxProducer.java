package com.fxz.demo.rocketmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author fxz
 */
//@Component
public class TxProducer implements ApplicationRunner {
    @Autowired
    TransactionMQProducer transactionMQProducer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread() {
            @Override
            public void run() {
                transactionMQProducer.setTransactionListener(new TransactionListener() {
                    @Override
                    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                        if (message.getTransactionId().hashCode() % 2 == 0) {
                            System.out.println("message commit->" + JSON.toJSONString(message));
                            return LocalTransactionState.UNKNOW;
                        } else {
                            System.out.println("message unkonw->" + JSON.toJSONString(message));
                            return LocalTransactionState.UNKNOW;
                        }
                    }

                    @Override
                    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                        System.out.println("message check commit->" + JSON.toJSONString(messageExt));
                        return LocalTransactionState.COMMIT_MESSAGE;
                    }
                });
                try {
                    transactionMQProducer.start();
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 100; i++) {
                    Message message = new Message("txFxz", "tag-fxz", ("this is " + i + " message").getBytes());
                    try {
                        System.out.println(transactionMQProducer.sendMessageInTransaction(message,null));
                        Thread.sleep(200);
                    } catch (MQClientException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               // transactionMQProducer.shutdown();
            }
        }.start();
    }
}
