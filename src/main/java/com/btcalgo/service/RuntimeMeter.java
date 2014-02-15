package com.btcalgo.service;

import com.btcalgo.reactor.NameableConsumer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class RuntimeMeter implements NameableConsumer<Event<Void>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String TITLE_BASE = "TRIAL license. Time to exit: ";
    private static final String MINUTES = " minutes";
    private static final String MINUTE = " minute";
    private AtomicInteger mins = new AtomicInteger(330 - 300 + 1);
    private StringProperty title = new SimpleStringProperty(TITLE_BASE + mins.get() + MINUTES);

    private ScheduledFuture scheduledFuture;

    @Override
    public String getId() {
        return "meter";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        if (scheduledFuture!= null && scheduledFuture.isCancelled()) {
            return;
        }

        updateTitle(mins.decrementAndGet());
        if (mins.get() <= 0){
            log.info("Exit application on timer");
            Platform.exit();
        }
    }

    public void stopMeter() {
        scheduledFuture.cancel(false);
    }

    private void updateTitle(final int minsLeft) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setTitle(TITLE_BASE + minsLeft + (minsLeft == 1 ? MINUTE : MINUTES));
            }
        });
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
