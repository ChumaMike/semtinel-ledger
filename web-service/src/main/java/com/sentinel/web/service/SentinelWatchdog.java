package com.sentinel.web.service;

import com.sentinel.common.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SentinelWatchdog {

    @Autowired
    private RestTemplate restTemplate;

    // Thread-safe list to hold current system alerts
    private final List<AlertDTO> activeAlerts = new CopyOnWriteArrayList<>();

    // Runs every 10 seconds (10000ms)
    @Scheduled(fixedRate = 10000)
    public void monitorSystemHealth() {
        checkSentinelAI();
    }

    private void checkSentinelAI() {
        String aiUrl = "http://sentinel-ai:8000/docs";
        try {
            restTemplate.getForEntity(aiUrl, String.class);
            // If it succeeds, clear any "SENTINEL-AI" alerts
            activeAlerts.removeIf(a -> a.getServiceName().equals("SENTINEL-AI"));
        } catch (Exception e) {
            // If it fails, add an alert if one doesn't already exist
            boolean alreadyAlerted = activeAlerts.stream()
                    .anyMatch(a -> a.getServiceName().equals("SENTINEL-AI"));

            if (!alreadyAlerted) {
                activeAlerts.add(new AlertDTO(
                        "SENTINEL-AI",
                        "CRITICAL",
                        "Security Layer Unreachable",
                        System.currentTimeMillis()
                ));
                System.err.println("🚨 WATCHDOG: Sentinel AI is DOWN!");
            }
        }
    }

    public List<AlertDTO> getAlerts() {
        return activeAlerts;
    }
}