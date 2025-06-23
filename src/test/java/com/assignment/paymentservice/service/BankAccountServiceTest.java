package com.assignment.paymentservice.service;

import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.entity.Transaction;
import com.assignment.paymentservice.exception.AccountAlreadyExistException;
import com.assignment.paymentservice.exception.AccountNotFoundException;
import com.assignment.paymentservice.exception.DuplicatePaymentTransferException;
import com.assignment.paymentservice.exception.InsufficientBalanceException;
import com.assignment.paymentservice.repository.BankAccountRepository;
import com.assignment.paymentservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    private BankAccount testAccount;
    private BankAccount testAccount1;

    @BeforeEach
    void setup() {
        testAccount = new BankAccount();
        testAccount.setId(1L);
        testAccount.setAccountNumber("1111");
        testAccount.setBalance("1000");

        testAccount1 = new BankAccount();
        testAccount1.setId(2L);
        testAccount1.setAccountNumber("2222");
        testAccount1.setBalance("2000");

    }

    @Test
    void testGetAccountDetailsTest(){
        String accountNumber = "1111";
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(testAccount));
        BankAccount result = bankAccountService.getAccount(accountNumber);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    void testGetAccountDetailsTest_AccountNotFound(){
        String accountNumber = "111";
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,() -> bankAccountService.getAccount(accountNumber));
    }

    @Test
    void testCreateAccountTest(){
        String accountNumber = "1111";
        String initialBalance = "1000";
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(testAccount);
        bankAccountService.createBankAccount(accountNumber,initialBalance);
        verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test
    void testCreateAccountTest_AccountAlreadyExist(){
        String accountNumber = "1111";
        String initialBalance = "1000";
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(testAccount));
        assertThrows(AccountAlreadyExistException.class,() -> bankAccountService.createBankAccount(accountNumber,initialBalance));
    }

    @Test
    void testTransferAmount(){
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        when(transactionRepository.findByKey(key)).thenReturn(Optional.empty());
        when(bankAccountRepository.findByAccountNumberForUpdate(fromAccount)).thenReturn(Optional.of(testAccount));
        when(bankAccountRepository.findByAccountNumberForUpdate(toAccount)).thenReturn(Optional.of(testAccount1));

        bankAccountService.transferAmount(key,fromAccount,toAccount,amount);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testTransferAmount_DuplicatePayment(){
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        when(transactionRepository.findByKey(key)).thenReturn(Optional.of(Mockito.mock(Transaction.class)));

        assertThrows(DuplicatePaymentTransferException.class,() ->
                bankAccountService.transferAmount(key,fromAccount,toAccount,amount));
    }

    @Test
    void testTransferAmount_SenderAccountNotFound(){
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        when(transactionRepository.findByKey(key)).thenReturn(Optional.empty());
        when(bankAccountRepository.findByAccountNumberForUpdate(fromAccount)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,() ->
                bankAccountService.transferAmount(key,fromAccount,toAccount,amount));
    }

    @Test
    void testTransferAmount_ReceiverAccountNotFound(){
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "100.00";
        when(transactionRepository.findByKey(key)).thenReturn(Optional.empty());
        when(bankAccountRepository.findByAccountNumberForUpdate(fromAccount)).thenReturn(Optional.of(testAccount));
        when(bankAccountRepository.findByAccountNumberForUpdate(toAccount)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,() ->
                bankAccountService.transferAmount(key,fromAccount,toAccount,amount));
    }

    @Test
    void testTransferAmount_InsufficientFund(){
        String key = "unique-key";
        String fromAccount = "1111";
        String toAccount = "2222";
        String amount = "5000.00";
        when(transactionRepository.findByKey(key)).thenReturn(Optional.empty());
        when(bankAccountRepository.findByAccountNumberForUpdate(fromAccount)).thenReturn(Optional.of(testAccount));
        when(bankAccountRepository.findByAccountNumberForUpdate(toAccount)).thenReturn(Optional.of(testAccount1));

        assertThrows(InsufficientBalanceException.class,() ->
                bankAccountService.transferAmount(key,fromAccount,toAccount,amount));
    }

}
