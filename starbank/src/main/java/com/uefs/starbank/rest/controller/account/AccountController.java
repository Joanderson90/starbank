package com.uefs.starbank.rest.controller.account;

import com.uefs.starbank.domain.bank.Account;
import com.uefs.starbank.rest.dto.account.AccountDTO;
import com.uefs.starbank.service.account.AccountI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountI accountService;

    @PostMapping("create")
    public Account create(@RequestBody @Valid AccountDTO account) {
        return accountService.create(account);
    }

    @GetMapping("list")
    public List<Account> list() {
        return accountService.list();
    }

}
