package com.assignment.paymentservice.controller;

import com.assignment.paymentservice.dto.BankAccountRequest;
import com.assignment.paymentservice.dto.TransferRequest;
import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.exception.AccountAlreadyExistException;
import com.assignment.paymentservice.exception.AccountNotFoundException;
import com.assignment.paymentservice.exception.InsufficientBalanceException;
import com.assignment.paymentservice.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransferController transferController;

    private TransferRequest transferRequest;

    @BeforeEach
    void setup() {
        transferRequest = new TransferRequest();
        transferRequest.setSourceAccount(1111L);
        transferRequest.setDestinationAccount(2222L);
        transferRequest.setAmount("100.00");
    }

    @Test
    void testTransferAmount_success() {
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        doNothing().when(bankAccountService).transferAmount(eq(key),eq(fromAccount),eq(toAccount),eq(amount));
        transferController.transferAmount(key,transferRequest);
        verify(bankAccountService).transferAmount(eq(key),eq(fromAccount),eq(toAccount),eq(amount));
    }

    @Test
    void testTransferAmount_SenderAccountNotFound() {
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        doThrow(new AccountNotFoundException("Sender account not found")).when(bankAccountService)
                .transferAmount(eq(key),eq(fromAccount),eq(toAccount),eq(amount));
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class,() ->
                transferController.transferAmount(key,transferRequest));
        assertEquals("Sender account not found",ex.getMessage());
    }

    @Test
    void testTransferAmount_InsufficientFund() {
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(bankAccountService)
                .transferAmount(eq(key),eq(fromAccount),eq(toAccount),eq(amount));
        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class,() ->
                transferController.transferAmount(key,transferRequest));
        assertEquals("Insufficient balance",ex.getMessage());
    }
}
