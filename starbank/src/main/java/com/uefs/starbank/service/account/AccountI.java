package com.uefs.starbank.service.account;

import com.uefs.starbank.domain.bank.Account;
import com.uefs.starbank.rest.dto.account.AccountDTO;

import java.util.List;

public interface AccountI {

    Account create(AccountDTO account);

    List<Account> list();
}
