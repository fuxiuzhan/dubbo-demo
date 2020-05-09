package com.fxz.demo.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//@Component
public class SentinelTest implements ApplicationRunner {
    private static final String recouceName = "test-resource";

    public static void testSentinel(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        initFlow(recouceName);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.execute(new Thread() {
                @Override
                public void run() {
                    setName("Thread-" + finalI);
                    for (int i = 0; i < 100; i++) {

                        Entry entry = null;
                        try {
                            entry = SphU.entry(recouceName);
                            System.out.println("Thread->" + getName() + " entry->" + recouceName);
                        } catch (BlockException e) {
                            System.out.println("Thread->" + getName() + " blocked->" + recouceName);
                        } finally {
                            if (entry != null) {
                                entry.exit();
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static void initFlow(String resourceName) {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule(resourceName);
        // set limit qps to 10
        rule.setCount(10);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        testSentinel(args.getSourceArgs());
    }
}
