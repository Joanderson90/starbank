package com.uefs.starbank.service.bank;

import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.rest.dto.account.BalanceRequestDTO;
import com.uefs.starbank.rest.dto.account.BalanceResponseDTO;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.TransferDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;

public interface BankI {

    void deposit(DepositDTO deposit);

    void deposit(DepositDTO deposit, Integer bankCode);

    Double withdrawal(WithdrawalDTO withdrawalDTO);

    BalanceResponseDTO getBalance(BalanceRequestDTO balanceRequestDTO);

    Double transfer(TransferDTO transferDTO);

    Transaction getTransaction(Integer id);

    void handleToken() throws InterruptedException;

    Bank getThisBank();


}



