package ru.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class DeferredExecutorImplTest {
    static Runnable r1;
    static Runnable r2;
    static Runnable r3;

//    @BeforeAll
//    static void init(){
//
//    }


    @Test
    void concurrentExecutorTest() throws InterruptedException {
        int initDelay = 2000;

        Consumer<Long> consumer = l -> System.out.println(System.currentTimeMillis() - l + "  consumer 1");
        Consumer<Long> consumer2 = l -> System.out.println(System.currentTimeMillis() - l + "  consumer 2");
        Consumer<Long> consumer3 = l -> System.out.println(System.currentTimeMillis() - l + "  consumer 3");

        List<Consumer<Long>> consumers = new ArrayList<Consumer<Long>>() {{
            add(consumer);
            add(consumer2);
            add(consumer3);
        }};

        DeferredExecutor<Long> deferredExecutor = new DeferredExecutorImpl<>(consumer, initDelay);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(100);
                    deferredExecutor.accept(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            int consumersSize = consumers.size();
            int currentConsumerIndex = 0;
            for (int i = 1; i < 20; i++) {
                try {
                    Thread.sleep(500);
                    deferredExecutor.setDelay(i * 100);
                    currentConsumerIndex = (currentConsumerIndex + 1) % consumersSize;
                    deferredExecutor.setConsumer(consumers.get(currentConsumerIndex));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

    }
}