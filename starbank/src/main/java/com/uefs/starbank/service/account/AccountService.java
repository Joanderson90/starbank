package com.uefs.starbank.service.account;

import com.uefs.starbank.data.account.DataAccount;
import com.uefs.starbank.domain.bank.Account;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.rest.dto.account.AccountDTO;
import com.uefs.starbank.service.bank.BankI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements AccountI {

    @Autowired
    BankI bankService;


    @Override
    public Account create(AccountDTO account) {

        int amountAccount = DataAccount.getAccountList().size();

        Bank bankInfo = bankService.getThisBank();

        Account newAccount = Account
                .builder()
                .id(amountAccount + 1)
                .number((amountAccount + 1) * 1000 * bankInfo.getCode())
                .digit(bankInfo.getCode())
                .password(account.getPassword())
                .userList(account.getUserList())
                .balance(0.0)
                .bank(bankInfo)
                .build();

        return DataAccount.addAccount(newAccount);
    }

    @Override
    public List<Account> list() {
        return DataAccount.getAccountList();
    }
}
