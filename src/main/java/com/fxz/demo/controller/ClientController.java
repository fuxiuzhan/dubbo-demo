package com.fxz.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fxz.demo.service.MessageProccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ClientController {
    @Reference(version = "1.0.0")
    MessageProccess messageProccess;

    @RequestMapping("/process/{str}")
    public String process(@PathVariable("str") String str) {
        return messageProccess.process(str);
    }

    @GetMapping("/test")
    public String test() {
        return messageProccess.process("process_test");
    }
}
