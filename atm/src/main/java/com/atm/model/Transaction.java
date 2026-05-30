package com.atm.model;

import com.atm.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final boolean success;

    public Transaction(String accountNumber, TransactionType type,
                       BigDecimal amount, boolean success) {
        this.transactionId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.success = success;
    }
}
