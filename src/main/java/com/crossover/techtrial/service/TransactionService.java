package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long transactionId);

    List<Transaction> getAll();

    void delete(Long id);
}
