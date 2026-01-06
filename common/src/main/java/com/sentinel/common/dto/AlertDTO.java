package com.sentinel.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDTO {
    private String serviceName;   // e.g., "SENTINEL-AI"
    private String alertLevel;    // e.g., "CRITICAL", "WARNING"
    private String message;       // e.g., "Connection Timeout"
    private long timestamp;
}