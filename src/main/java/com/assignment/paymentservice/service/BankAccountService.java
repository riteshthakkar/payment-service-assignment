package com.assignment.paymentservice.service;

import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.entity.Transaction;
import com.assignment.paymentservice.exception.AccountAlreadyExistException;
import com.assignment.paymentservice.exception.AccountNotFoundException;
import com.assignment.paymentservice.exception.DuplicatePaymentTransferException;
import com.assignment.paymentservice.exception.InsufficientBalanceException;
import com.assignment.paymentservice.repository.BankAccountRepository;
import com.assignment.paymentservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    public BankAccount getAccount(String accountNumber) {
        logger.info("Getting bank account details. Account number : {}",accountNumber);
        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccount.isPresent()){
            logger.info("Account number details retrieved. Account number : {}",accountNumber);
            return bankAccount.get();
        } else {
            logger.error("Account number does not exist. Account number : {}",accountNumber);
            throw new AccountNotFoundException("Account number provided does not exist.");
        }
    }

    public void createBankAccount(String accountNumber, String initialBalance) {
        logger.info("Creating bank account. Account number : {}",accountNumber);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(accountNumber);
        bankAccount.setBalance(initialBalance);
        if (bankAccountRepository.findByAccountNumber(accountNumber).isPresent()){
            logger.error("Account is already present. Account number : {}",accountNumber);
            throw new AccountAlreadyExistException("Account number already present");
        }
        bankAccountRepository.save(bankAccount);
        logger.info("Bank account created successfully. Account number : {}",accountNumber);
    }

    @Transactional
    public void transferAmount(String key, String fromAccount, String toAccount, String amount) {
        logger.info("Transfer request processing start. Key : {}",key);
        BigDecimal transferAmount = new BigDecimal(amount);

        Optional<Transaction> existing = transactionRepository.findByKey(key);
        if (existing.isPresent()) {
            logger.error("Duplicate transfer request. Key : {}",key);
            throw new DuplicatePaymentTransferException("Duplicate transaction with Key " + key);
        }

        //Lock sender account
        BankAccount sender = bankAccountRepository.findByAccountNumberForUpdate(fromAccount)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found " + fromAccount));
        logger.info("Sender account found. Account number : {}",fromAccount);

        //Lock receiver account
        BankAccount receiver = bankAccountRepository.findByAccountNumberForUpdate(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found " + toAccount));
        logger.info("Receiver account found. Account number : {}",toAccount);

        BigDecimal senderBalance = new BigDecimal(sender.getBalance());
        BigDecimal receiverBalance = new BigDecimal(receiver.getBalance());

        if(senderBalance.compareTo(transferAmount) < 0){
            logger.error("Insufficient funds in sender account. Sender account number : {}, Key : {}",fromAccount,key);
            throw new InsufficientBalanceException("Insufficient balance in sender account " + fromAccount);
        }
        //update balances
        sender.setBalance(senderBalance.subtract(transferAmount).toString());
        receiver.setBalance(receiverBalance.add(transferAmount).toString());

        bankAccountRepository.save(sender);
        logger.info("Account balance updated in sender account.");
        bankAccountRepository.save(receiver);
        logger.info("Account balance updated in receiver account.");

        //insert transaction record
        Transaction transaction = new Transaction();
        transaction.setKey(key);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
        logger.info("Transfer completed successfully. Key:{}", key);
    }
}
