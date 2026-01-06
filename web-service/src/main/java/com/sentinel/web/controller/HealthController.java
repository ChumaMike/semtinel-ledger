package com.sentinel.web.controller;

import com.sentinel.common.dto.AlertDTO;
import com.sentinel.web.service.SentinelWatchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "http://localhost:5173") // 🌟 Allows React to talk to this service
public class HealthController {

    @Autowired
    private SentinelWatchdog watchdog;

    @GetMapping("/alerts")
    public List<AlertDTO> getSystemAlerts() {
        return watchdog.getAlerts();
    }
}