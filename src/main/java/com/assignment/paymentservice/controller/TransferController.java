package com.assignment.paymentservice.controller;

import com.assignment.paymentservice.dto.TransferRequest;
import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.service.BankAccountService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    BankAccountService bankAccountService;

    public TransferController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("transactions")
    public ResponseEntity<?> transferAmount(@RequestHeader("Idempotency-Key") String key,
                                            @RequestBody TransferRequest request) {
        logger.info("Received transfer request with key : {}",key);
        bankAccountService.transferAmount(key, request.getSourceAccount().toString(),
                request.getDestinationAccount().toString(), request.getAmount());
        logger.info("Transfer is successful for key : {}",key);
        return ResponseEntity.ok().build();
    }

}
