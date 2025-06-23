package com.assignment.paymentservice.controller;

import com.assignment.paymentservice.dto.BankAccountRequest;
import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.mapper.BankAccountResponseMapper;
import com.assignment.paymentservice.service.BankAccountService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);
    BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("accounts")
    public ResponseEntity<?> createBankAccount(@RequestBody BankAccountRequest request) {
        logger.info("Received request to create bank account. Account number : {}",request.getAccountNumber());
        bankAccountService.createBankAccount(request.getAccountNumber().toString(),request.getBalance());
        logger.info("Bank account created successfully. Account number : {}",request.getAccountNumber());
        return ResponseEntity.ok().build();
    }

    @GetMapping("accounts/{accountNumber}")
    public ResponseEntity<?> getBankAccount(@PathVariable String accountNumber) {
        logger.info("Received request to retrieve bank account. Account number : {}",accountNumber);
        BankAccount bankAccount = bankAccountService.getAccount(accountNumber);
        logger.info("Account details retrieved for account number : {}",accountNumber);
        return ResponseEntity.ok(BankAccountResponseMapper.toResponse(bankAccount));
    }


}
