package com.sentinel.common.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long fromAccountId; // Internal Ref
    private Long toAccountId;   // Internal Ref

    // 🌟 RETAIL BANKING FIELDS
    private String senderAccountNumber;   // e.g. 100100001
    private String receiverAccountNumber; // e.g. 200200001
    private String description;           // e.g. "Monthly Rent"

    private BigDecimal amount;
    private String status; // "SUCCESS", "BLOCKED", "FAILED"

    private LocalDateTime timestamp;

    public Transaction() {}

    // Updated Constructor for Retail Logic
    public Transaction(Long fromId, Long toId, String fromAccNum, String toAccNum, BigDecimal amount, String status, String description) {
        this.fromAccountId = fromId;
        this.toAccountId = toId;
        this.senderAccountNumber = fromAccNum;
        this.receiverAccountNumber = toAccNum;
        this.amount = amount;
        this.status = status;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}