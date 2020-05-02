package com.fxz.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fxz.demo.service.MessageProccess;

@Service(version = "1.0.0")
public class MessageProcessImpl implements MessageProccess {
    @Override
    public String process(String context) {
        return "process-" + context;
    }
}
