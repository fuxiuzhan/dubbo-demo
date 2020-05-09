package com.fxz.demo.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorSignal {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(4);
        testExec(atomicInteger);
    }

    static void testExec(AtomicInteger atomicInteger) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        /**
         * 如果使用countDownLatch，则需要使用新线程启动如下过程，让所有线程在同一重点，
         * 最终标识整个过程完结
         */
        for (int i = 0; i < 4; i++) {
            threadPoolExecutor.execute(new Thread() {
                @Override
                public void run() {
                    if (true) {
                        // throw new RuntimeException("11");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    atomicInteger.decrementAndGet();

                }
            });

            /**
             * 题记：线程池使用信号量机制同步要特别注意，不能使用会阻塞的同步方法
             * 原因如下：1 由于线程池的特殊性，线程池会预先创建和复用线程，当线程多于核心线程数
             * 队列又不满的情况下线程会排队，如果使用阻塞的同步机制，可能会导致
             * 当前线程挂起，不能正常退出释放线程，排队的线程得不到运行的机会，最后撑爆线程池，建议使用
             * 原子类型的数字自减或自增
             * 2 犯的低级错误 将自旋代码块放错地方，放到循环内执行了，导致计数出问题，原理与1一致。
             * 线程池，信号量，同步机制使用要慎重
             */
        }
        while (atomicInteger.get() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("final->" + atomicInteger.get());
        threadPoolExecutor.shutdown();
    }
}
