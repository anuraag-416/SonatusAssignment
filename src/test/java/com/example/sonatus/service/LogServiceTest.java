package com.example.sonatus.service;

import com.example.sonatus.*;
import com.example.sonatus.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogServiceTest {

    private LogService logService;

    @BeforeEach
    void setup() {
        logService = new LogService();
    }

    @Test
    public void testAddLogAndRetrieveLogs() {
        // Create and add a log entry
        Instant now = Instant.parse("2025-03-17T10:15:00Z");
        LogEntry entry = new LogEntry("auth-service", now, "User login successful");
        logService.addLog(entry);

        // Retrieve logs in the valid time range
        List<LogEntry> logs = logService.getLogs("auth-service", now.minusSeconds(60), now.plusSeconds(60));

        // Assertions
        assertFalse(logs.isEmpty());
        assertEquals(1, logs.size());
        assertEquals("User login successful", logs.get(0).getMessage());
    }

    @Test
    public void testGetLogs_EmptyLogs() {
        // Try retrieving logs for a non-existent service
        List<LogEntry> logs = logService.getLogs("unknown-service", Instant.now().minusSeconds(3600), Instant.now());
        assertTrue(logs.isEmpty());
    }

    @Test
    public void testRemoveOldLogs() {
        // Add a log that is older than 1 hour
        Instant oldLogTime = Instant.now().minusSeconds(4000);
        logService.addLog(new LogEntry("auth-service", oldLogTime, "Old log entry"));

        // Add a recent log
        Instant recentLogTime = Instant.now();
        logService.addLog(new LogEntry("auth-service", recentLogTime, "Recent log entry"));

        // Remove old logs
        logService.removeOldLogs();

        // Check remaining logs
        List<LogEntry> logs = logService.getLogs("auth-service", Instant.now().minusSeconds(3600), Instant.now().plusSeconds(60));
        assertEquals(1, logs.size());
        assertEquals("Recent log entry", logs.get(0).getMessage());
    }
}
