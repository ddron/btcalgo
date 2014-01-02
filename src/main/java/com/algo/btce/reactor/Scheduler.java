package com.algo.btce.reactor;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import reactor.core.Reactor;
import reactor.event.Event;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class Scheduler {

    private Reactor reactor;
    private TaskScheduler taskScheduler;

    public Scheduler(Reactor reactor, TaskScheduler taskScheduler) {
        this.reactor = reactor;
        this.taskScheduler = taskScheduler;
    }

    public <T> ScheduledFuture schedule(Object key, T event, Trigger trigger) {
        return taskScheduler.schedule(createRunnable(key, event), trigger);
    }

    public <T> ScheduledFuture schedule(Object key, T event, Date startTime) {
        return taskScheduler.schedule(createRunnable(key, event), startTime);
    }

    public <T> ScheduledFuture scheduleAtFixedRate(Object key, T event, Date startTime, long period) {
        return taskScheduler.scheduleAtFixedRate(createRunnable(key, event), startTime, period);
    }

    public <T> ScheduledFuture scheduleAtFixedRate(Object key, T event, long period) {
        return taskScheduler.scheduleAtFixedRate(createRunnable(key, event), period);
    }

    public <T> ScheduledFuture scheduleWithFixedDelay(Object key, T event, Date startTime, long delay) {
        return taskScheduler.scheduleWithFixedDelay(createRunnable(key, event), startTime, delay);
    }

    public <T> ScheduledFuture scheduleWithFixedDelay(Object key, T event, long delay) {
        return taskScheduler.scheduleWithFixedDelay(createRunnable(key, event), delay);
    }

    private <T> Runnable createRunnable(final Object key, final T event) {
        return new Runnable() {
            @Override
            public void run() {
                reactor.notify(key, Event.wrap(event));
            }
        };
    }
}
