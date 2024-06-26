package com.uefs.starbank.data.transaction;

import com.uefs.starbank.domain.bank.TransactionExternal;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public static void removeById(Integer id) {

        findById(id).ifPresentOrElse(transaction -> getTransactionList().remove(transaction),
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("Transaction with id %s not found.", id));
                });

    }
}
