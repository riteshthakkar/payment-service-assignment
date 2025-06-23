package com.assignment.paymentservice.controller;

import com.assignment.paymentservice.dto.BankAccountRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;

    private BankAccountRequest bankAccountRequest;

    private BankAccount bankAccount;

    @BeforeEach
    void setup() {
        bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setAccountNumber(1111L);
        bankAccountRequest.setBalance("100.00");

        bankAccount = new BankAccount();
        bankAccount.setAccountNumber("1111");
        bankAccount.setBalance("100.00");
    }

    @Test
    void testCreateBankAccount_success() {
        doNothing().when(bankAccountService).createBankAccount(anyString(),anyString());
        bankAccountController.createBankAccount(bankAccountRequest);
        verify(bankAccountService).createBankAccount(bankAccountRequest.getAccountNumber().toString(),bankAccountRequest.getBalance());
    }

    @Test
    void testCreateBankAccount_exception() {
        doThrow(new AccountAlreadyExistException("Account already Exist")).when(bankAccountService).createBankAccount(anyString(),anyString());
        AccountAlreadyExistException ex = assertThrows(AccountAlreadyExistException.class,() ->
                bankAccountController.createBankAccount(bankAccountRequest));
        assertEquals("Account already Exist",ex.getMessage());
    }

    @Test
    void testGetBankAccount_success() {
        String accountNumber = "1111";
        when(bankAccountService.getAccount(anyString())).thenReturn(bankAccount);
        ResponseEntity<BankAccount> response  = (ResponseEntity<BankAccount>) bankAccountController.getBankAccount(accountNumber);
        assertNotNull(response.getBody());
        assertEquals("1111",response.getBody().getAccountNumber());
        assertEquals("100.00",response.getBody().getBalance());
    }

    @Test
    void testGetBankAccount_exception() {
        String accountNumber = "1111";
        when(bankAccountService.getAccount(anyString())).thenThrow(AccountNotFoundException.class);
        assertThrows(AccountNotFoundException.class,() ->
                bankAccountController.getBankAccount(accountNumber));
    }
}
