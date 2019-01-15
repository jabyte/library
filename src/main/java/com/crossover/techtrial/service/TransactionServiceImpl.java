package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactionList = new ArrayList<>();

        transactionRepository.findAll().forEach(transactionList::add);

        return transactionList;
    }

    @Override
    public void delete(Long id) {
        this.transactionRepository.deleteById(id);
    }
}
