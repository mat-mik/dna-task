package com.example.dnatask.user;

import com.example.dnatask.common.Event;

import java.time.Instant;

public record UserCreated(long userId, String name, Instant when) implements Event {

    @Override
    public Instant getWhen() {
        return when;
    }

    @Override
    public long getAggregateId() {
        return userId;
    }


}
