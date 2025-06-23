package com.assignment.paymentservice.mapper;

import com.assignment.paymentservice.dto.BankAccountResponse;
import com.assignment.paymentservice.entity.BankAccount;

public class BankAccountResponseMapper {

    public static BankAccountResponse toResponse(BankAccount account) {
        if (account == null) return null;

        return new BankAccountResponse(
                Long.valueOf(account.getAccountNumber()),
                account.getBalance()
        );
    }
}
