package com.fxz.demo.config;

import com.alibaba.dubbo.config.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DubboConfig {
    @Bean
    public ConsumerConfig getConsulmerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setCheck(false);
        consumerConfig.setTimeout(2000);
       // consumerConfig.setFilter("myfilter,logfilter");
        return consumerConfig;
    }
}
