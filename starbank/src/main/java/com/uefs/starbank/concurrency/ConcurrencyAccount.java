package com.uefs.starbank.concurrency;

import com.uefs.starbank.domain.bank.Account;
import com.uefs.starbank.domain.enums.BankOperation;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;
import com.uefs.starbank.service.bank.BankI;

public class ConcurrencyAccount extends Thread {

    private final Account account;

    private final BankI bankService;

    private final BankOperation bankOperation;


    public ConcurrencyAccount(Account account, BankI bankService, BankOperation bankOperation) {
        this.account = account;
        this.bankService = bankService;
        this.bankOperation = bankOperation;
    }

    @Override
    public void run() {

        switch (this.bankOperation) {
            case DEPOSIT:
                doDeposit();
                break;
            case WITHDRAWAL:
                doWithdrawal();
                break;
            default:
                break;
        }

    }

    private void doWithdrawal() {
        bankService.withdrawal(WithdrawalDTO.builder()
                .accountNumber(account.getNumber())
                .digit(account.getDigit())
                .password(account.getPassword())
                .amount(1000.0)
                .build());
    }

    private void doDeposit() {
        bankService.deposit(new DepositDTO(account.getNumber(), 1000.0));
    }
}
