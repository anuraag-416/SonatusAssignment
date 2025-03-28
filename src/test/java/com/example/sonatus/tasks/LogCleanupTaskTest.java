package com.example.sonatus.tasks;

import com.example.sonatus.model.LogEntry;
import com.example.sonatus.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.mockito.Mockito.*;

public class LogCleanupTaskTest {

    private LogService logService;
    private LogCleanupTask logCleanupTask;

    @BeforeEach
    void setup() {
        logService = mock(LogService.class);
        logCleanupTask = new LogCleanupTask(logService);
    }

    @Test
    public void testCleanupOldLogs() {
        // Simulate cleanup task execution
        logCleanupTask.cleanupOldLogs();

        // Verify that removeOldLogs() was called once
        verify(logService, times(1)).removeOldLogs();
    }

    @Test
    public void testNoOldLogsToCleanup() {
        // Simulate no logs to clean
        doNothing().when(logService).removeOldLogs();

        // Trigger cleanup
        logCleanupTask.cleanupOldLogs();

        // Verify that removeOldLogs() was called
        verify(logService, times(1)).removeOldLogs();
    }
}
