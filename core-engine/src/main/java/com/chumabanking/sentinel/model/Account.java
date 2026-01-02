package com.chumabanking.sentinel.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data // This is Lombok—it creates your Getters and Setters automatically!
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long userId;

    // Use BigDecimal for money to avoid rounding errors!
    private BigDecimal balance;

    private String currency;
}