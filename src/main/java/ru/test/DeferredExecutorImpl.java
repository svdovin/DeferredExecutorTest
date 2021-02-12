package ru.test;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class DeferredExecutorImpl<T> implements DeferredExecutor<T> {

    private Consumer<T> consumer;
    private AtomicLong delay;
    private Timer timer;
    private List<ConsumerTimerTask> tasksList;

    public DeferredExecutorImpl(Consumer<T> consumer, long delay) {
        timer = new Timer();
        this.delay = new AtomicLong(delay);
        
        
        //svdovin: A thread-safe variant of ArrayList in which all mutative operations (add, set, and so on) 
        //are implemented by making a fresh copy of the underlying array.
        
        tasksList = new CopyOnWriteArrayList<>();
        this.consumer = consumer;
        this.delay.set(delay);
    }

    @Override
    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void setDelay(long delay) {
        this.delay.set(delay);
    }

    @Override
    public void accept(T t) {
        ConsumerTimerTask task = new ConsumerTimerTask(t);
        tasksList.add(task);
        //svdovin: что внутри schedule?
        timer.schedule(task, delay.get());
    }

    private class ConsumerTimerTask extends TimerTask {
        private T acceptedValue;

        public ConsumerTimerTask(T value) {
            acceptedValue = value;
        }

        @Override
        public void run() {
            consumer.accept(acceptedValue);
            tasksList.remove(this);
        }
    }
}
