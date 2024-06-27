package com.uefs.starbank.data.transaction;

import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.domain.enums.TransactionStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTransaction {

    @Getter
    private static final List<Transaction> transactionList = new ArrayList<>();

    public static Transaction addTransaction(Transaction transaction) {

        transactionList.add(transaction);

        return transaction;
    }

    public static Transaction addTransaction() {

        Transaction newTransaction = new Transaction(transactionList.size() + 1, TransactionStatus.RUNNING);

        transactionList.add(newTransaction);

        return newTransaction;
    }

    public static Optional<Transaction> findById(Integer id) {
        return transactionList
                .stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();

    }

    public static void updateStatus(Integer id, TransactionStatus transactionStatus) {
        findById(id).ifPresentOrElse(transaction -> transaction.setTransactionStatus(transactionStatus),
                () -> {
                    throw new RuntimeException(String.format("Transaction with id %s not found.", id));
                });
    }

}
