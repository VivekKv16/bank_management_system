package com.packBank;

import java.sql.Timestamp;

public class Transaction {
    private long id;
    private String type;
    private double amount;
    private long accountNumber;
    private Timestamp timestamp;

    public Transaction(long id, String type, double amount, long accountNumber, Timestamp timestamp) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public long getAccountNumber() { return accountNumber; }
    public Timestamp getTimestamp() { return timestamp; }
}
