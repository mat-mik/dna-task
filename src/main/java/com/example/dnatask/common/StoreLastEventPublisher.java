package com.example.dnatask.common;

public class StoreLastEventPublisher implements EventPublisher {

    private Event lastEvent;

    @Override
    public void publish(Event event) {
        this.lastEvent = event;
    }

    public Event getLastEvent() {
        return lastEvent;
    }
}
