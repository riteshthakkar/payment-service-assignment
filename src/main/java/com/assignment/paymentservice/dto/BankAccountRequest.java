package com.assignment.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccountRequest {
    @JsonProperty("account_id")
    private Long accountNumber;

    @JsonProperty("initial_balance")
    private String balance;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

