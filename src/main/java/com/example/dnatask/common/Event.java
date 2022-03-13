package com.example.dnatask.common;

import java.time.Instant;

public interface Event {

    Instant getWhen();

    long getAggregateId();
}
