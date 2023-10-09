package com.challenge.backend.utils;

import java.time.Duration;
import java.time.Instant;

public class TimeTravelUtils {

    private final String name;
    private Instant start;
    private Instant end;

    public TimeTravelUtils(String name) {
        setPointA();
        this.name = name;
    }

    public void setPointA() {
        this.start = Instant.now();
    }

    public void setPointB() {
        this.end = Instant.now();
    }

    public String getDuration() {
        setPointB();
        return Duration.between(this.start, this.end).toMillis() + " millis";
    }
}
