package com.fxz.demo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.springframework.core.annotation.Order;

@Order(Integer.MIN_VALUE)
@Activate(group = {"provider", "consumer"})
public class StaticsFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        System.out.println("invocation-" + invocation.toString());

        return invoker.invoke(invocation);
    }
}
