package com.example.sonatus.service;

import com.example.sonatus.model.LogEntry;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class LogService {

    // In-memory storage:
    // Key = serviceName, Value = NavigableMap<timestamp, message>
    private final ConcurrentMap<String, NavigableMap<Instant, String>> logsMap = new ConcurrentHashMap<>();

    /**
     * Store a new log entry in a thread-safe manner.
     */
    public void addLog(LogEntry entry) {
        // Get or create the map for this service
        NavigableMap<Instant, String> serviceLogs =
                logsMap.computeIfAbsent(entry.getServiceName(), k -> new ConcurrentSkipListMap<>());
        // Put the log in the map (key=timestamp, value=message)
        serviceLogs.put(entry.getTimestamp(), entry.getMessage());
    }

    /**
     * Retrieve logs for a given service name between [start, end].
     */
    public List<LogEntry> getLogs(String serviceName, Instant start, Instant end) {
        NavigableMap<Instant, String> serviceLogs = logsMap.get(serviceName);
        if (serviceLogs == null) {
            return List.of(); // no logs for service
        }
        // subMap gives us logs in [start, end], inclusive
        return serviceLogs.subMap(start, true, end, true)
                .entrySet()
                .stream()
                .map(e -> new LogEntry(serviceName, e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Remove logs older than 1 hour from now.
     */
    public void removeOldLogs() {
        Instant cutoff = Instant.now().minus(Duration.ofHours(1));
        for (NavigableMap<Instant, String> map : logsMap.values()) {
            // Tail of map from cutoff onward. The older entries are all below cutoff.
            // We can headMap them and remove them.
            NavigableMap<Instant, String> toRemove = map.headMap(cutoff, false);
            // We can remove them in a safe way:
            toRemove.clear(); // Clears all older than cutoff
        }
    }
}
