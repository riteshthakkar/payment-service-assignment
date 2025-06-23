package com.assignment.paymentservice.repository;

import com.assignment.paymentservice.entity.BankAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT b from bank_account b WHERE b.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumber (@Param("accountNumber") String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b from bank_account b WHERE b.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberForUpdate (@Param("accountNumber") String accountNumber);
}
