package com.fxz.demo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
        Entry entry = null;
        try {
            entry = SphU.entry("web-ctl-test");
            return messageProccess.process("process_test");
        } catch (BlockException e) {
            return e.getMessage();
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

    }

    @GetMapping("/sentinel")
    public String testSentinel() {
        Entry entry = null;
        try {
            entry = SphU.entry("testSentinel");
            return "ok";
        } catch (BlockException e) {
            return e.getMessage();
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
