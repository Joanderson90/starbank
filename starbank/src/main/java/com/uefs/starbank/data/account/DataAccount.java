package com.uefs.starbank.data.account;

import com.uefs.starbank.domain.bank.Account;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataAccount {

    @Getter
    private static final List<Account> accountList = new ArrayList<>();

    public static Account addAccount(Account account) {
        accountList.add(account);

        return account;
    }

    public static Optional<Account> findByNumber(Integer number) {

        return getAccountList()
                .stream()
                .filter(account -> account.getNumber().equals(number))
                .findFirst();

    }

    public static Optional<Account> findByNumberAndDigitAndPassword(Integer number, Integer digit, String password) {

        return getAccountList()
                .stream()
                .filter(account -> account.getNumber().equals(number) &&
                        account.getDigit().equals(digit) &&
                        account.getPassword().equals(password))
                .findFirst();

    }


}
