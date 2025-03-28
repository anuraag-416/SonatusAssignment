package com.example.sonatus.controller;

import com.example.sonatus.dto.LogEntryDTO;
import com.example.sonatus.model.LogEntry;
import com.example.sonatus.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * POST /logs
     * Ingest a new log entry.
     */
    @PostMapping
    public ResponseEntity<String> addLog(@RequestBody LogEntryDTO dto) {
        try {
            // Parse timestamp from DTO

            Instant logTime = Instant.parse(dto.getTimestamp());
            // Create a new LogEntry object
            LogEntry entry = new LogEntry(dto.getServiceName(), logTime, dto.getMessage());
            // Add log to the service
            logService.addLog(entry);
            // Return success response
            return ResponseEntity.ok("Log added successfully.");

        } catch (DateTimeParseException e) {
            // Handle invalid timestamp format
            return ResponseEntity.badRequest().body("Invalid timestamp format. Please use ISO-8601 (e.g. 2025-03-17T10:15:00Z).");

        } catch (Exception e) {
            // Return an internal server error response with error and log details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e + ". Log details: " + dto);
        }
    }


    /**
     * GET /logs?service=serviceName&start=2025-03-17T10:00:00Z&end=2025-03-17T10:30:00Z
     * Query logs for a given service in the specified time range.
     */
    @GetMapping
    public List<LogEntryDTO> getLogs(
            @RequestParam String service,
            @RequestParam String start,
            @RequestParam String end
    ) {
        // Parse times
        Instant startTime = Instant.parse(start);
        Instant endTime = Instant.parse(end);

        List<LogEntry> results = logService.getLogs(service, startTime, endTime);

        // Convert to DTO for response
        return results.stream()
                .map(e -> new LogEntryDTO(e.getServiceName(), e.getTimestamp().toString(), e.getMessage()))
                .toList();
    }
}
