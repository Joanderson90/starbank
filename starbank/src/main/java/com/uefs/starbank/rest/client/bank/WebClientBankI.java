package com.uefs.starbank.rest.client.bank;

import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;

public interface WebClientBankI {

    void deposit(DepositDTO depositDTO, Integer bankCode);

    Double withdrawal(WithdrawalDTO withdrawalDTO, Integer bankCode);

    Transaction getTransaction(Integer integer, Integer bankCode);
}
