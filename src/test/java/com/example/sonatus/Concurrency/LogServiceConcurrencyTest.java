


package com.example.sonatus.Concurrency;
import com.example.sonatus.model.LogEntry;
import com.example.sonatus.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogServiceConcurrencyTest {

    private LogService logService;

    @BeforeEach
    void setup() {
        logService = new LogService();
    }

    @Test
    void testConcurrentLogInsertions() throws InterruptedException, ExecutionException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // Insert logs concurrently
        Callable<Void> task = () -> {
            Instant now = Instant.now();
            logService.addLog(new LogEntry("auth-service", now, "Log message"));
            return null;
        };

        // Submit 100 concurrent tasks
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Check if all logs were inserted successfully
        assertEquals(threadCount, logService.getLogs("auth-service", Instant.now().minusSeconds(3600), Instant.now()).size());
    }
}
