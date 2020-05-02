package com.fxz.demo.filter;

import com.alibaba.dubbo.rpc.*;
import org.springframework.core.annotation.Order;

@Order(Integer.MIN_VALUE)
public class StaticsFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        System.out.println("invocation-" + invocation.toString());

        return invoker.invoke(invocation);
    }
}
