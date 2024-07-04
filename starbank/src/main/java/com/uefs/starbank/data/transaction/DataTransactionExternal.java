package com.uefs.starbank.data.transaction;

import com.uefs.starbank.domain.bank.TransactionExternal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTransactionExternal {

    @Getter
    private static final List<TransactionExternal> transactionList = new ArrayList<>();

    public static TransactionExternal addTransaction(TransactionExternal transaction) {
        transactionList.add(transaction);

        return transaction;
    }


    public static Optional<TransactionExternal> findById(Integer id) {
        return transactionList
                .stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();

    }


}
