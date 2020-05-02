package com.fxz.demo.filter;

import com.alibaba.dubbo.rpc.*;
import org.springframework.core.annotation.Order;

@Order(Integer.MAX_VALUE)
public class LogFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("log filter->" + invocation);
        return invoker.invoke(invocation);
    }
}
