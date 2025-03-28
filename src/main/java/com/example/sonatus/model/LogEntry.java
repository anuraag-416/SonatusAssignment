package com.example.sonatus.model;

import java.time.Instant;

public class LogEntry {
    private final String serviceName;
    private final Instant timestamp;
    private final String message;

    public LogEntry(String serviceName, Instant timestamp, String message) {
        this.serviceName = serviceName;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}

