package com.assignment.paymentservice.repository;

import com.assignment.paymentservice.entity.BankAccount;
import com.assignment.paymentservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByKey (String Key);

}
