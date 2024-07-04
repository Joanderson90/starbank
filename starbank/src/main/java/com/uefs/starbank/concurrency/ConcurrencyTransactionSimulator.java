package com.uefs.starbank.concurrency;

import com.uefs.starbank.data.account.DataAccount;
import com.uefs.starbank.data.token.DataToken;
import com.uefs.starbank.domain.bank.Account;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.domain.enums.BankOperation;
import com.uefs.starbank.domain.user.User;
import com.uefs.starbank.service.bank.BankI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcurrencyTransactionSimulator implements CommandLineRunner {

    @Autowired
    private BankI bankService;


    @Override
    public void run(String... args) throws Exception {
//        simulateConcurrency();
    }

    public void simulateConcurrency() {

        Bank bankInfo = bankService.getThisBank();

        List<User> userList = new ArrayList<>();
        userList.add(new User("Alice", "88377653982", LocalDate.now()));
        userList.add(new User("Teddy", "98185878471", LocalDate.now()));

        Account newAccount = Account
                .builder()
                .id(1)
                .number(1000 * bankInfo.getCode())
                .digit(bankInfo.getCode())
                .password("123")
                .userList(userList)
                .balance(1000.0)
                .bank(bankInfo)
                .build();

        DataAccount.addAccount(newAccount);

        DataToken.setToken(true);

        Thread thread1 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread2 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread3 = new ConcurrencyAccount(newAccount, bankService, BankOperation.WITHDRAWAL);

        Thread thread4 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread5 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread6 = new ConcurrencyAccount(newAccount, bankService, BankOperation.WITHDRAWAL);

        Thread thread7 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread8 = new ConcurrencyAccount(newAccount, bankService, BankOperation.DEPOSIT);
        Thread thread9 = new ConcurrencyAccount(newAccount, bankService, BankOperation.WITHDRAWAL);


        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();


    }
}
