package com.example.sonatus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
public class LogEntryDTO {
    private String serviceName;
    private String timestamp;
    private String message;
}