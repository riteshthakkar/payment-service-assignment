package com.assignment.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
    @JsonProperty("source_account_id")
    private Long sourceAccount;
    @JsonProperty("destination_account_id")
    private Long destinationAccount;
    private String amount;

    public Long getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Long getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Long destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

