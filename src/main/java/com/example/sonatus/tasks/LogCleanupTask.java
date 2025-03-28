package com.example.sonatus.tasks;

import com.example.sonatus.service.LogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogCleanupTask {

    private final LogService logService;

    public LogCleanupTask(LogService logService) {
        this.logService = logService;
    }

    /**
     * Runs every minute (example schedule) to remove logs older than 1 hour.
     */
    @Scheduled(fixedRate = 60000) // 60,000 ms = 1 minute
    public void cleanupOldLogs() {
        logService.removeOldLogs();
    }
}
