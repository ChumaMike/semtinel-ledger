package com.sentinel.common.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Data
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private Long userId;

    private String name;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private String status; // "IN_PROGRESS", "COMPLETED"

    // 🌟 NEW FIELDS
    private String description;    // "Why I want this"
    private String priority;       // "HIGH", "MEDIUM", "LOW"
    private LocalDate deadline;    // When do you want to finish?

    @Column(columnDefinition = "TEXT")
    private String notes;          // "Plan: Save R500 every month..."

    private LocalDate createdDate;
}