package com.altafrica.repository;

import com.altafrica.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByReference(String reference);
}

