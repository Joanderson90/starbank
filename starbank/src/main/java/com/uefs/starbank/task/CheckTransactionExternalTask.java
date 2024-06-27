package com.uefs.starbank.task;

import com.uefs.starbank.data.transaction.DataTransactionExternal;
import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.domain.bank.TransactionExternal;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.domain.enums.BankOperation;
import com.uefs.starbank.domain.enums.TransactionStatus;
import com.uefs.starbank.rest.client.bank.WebClientBank;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.service.bank.BankI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class CheckTransactionExternalTask {

    @Autowired
    WebClientBank webClientBank;

    @Autowired
    BankI bankService;

    private static final long FIXED_DELAY = 10 * 1000;

    @Scheduled(fixedDelay = FIXED_DELAY)
    public void scheduleFixedDelayTask() {

        List<TransactionExternal> transactionToRemoveList = new ArrayList<>();

        DataTransactionExternal.getTransactionList().forEach(transactionExternal -> {
            log.info("Checking external transaction with id: {} from bank: {}.",
                    transactionExternal.getId(),
                    getBankName(transactionExternal));

            try {
                Transaction transaction = webClientBank.getTransaction(transactionExternal.getId(), transactionExternal.getExternalBankCode());

                if (transaction != null && (transaction.getTransactionStatus().equals(TransactionStatus.FINISHED))) {
                    log.info("Transaction with id: {} from bank: {} finished with success!",
                            transactionExternal.getId(),
                            getBankName(transactionExternal));

                    transactionToRemoveList.add(DataTransactionExternal.findById(transactionExternal.getId()).get());
                }

            } catch (Exception e) {
                log.error("There was an error in the transaction with id: {} from bank: {}.",
                        transactionExternal.getId(),
                        getBankName(transactionExternal));

                transactionToRemoveList.add(DataTransactionExternal.findById(transactionExternal.getId()).get());

                undoTransaction(transactionExternal);

            }

        });

        DataTransactionExternal.getTransactionList().removeAll(transactionToRemoveList);
    }

    private void undoTransaction(TransactionExternal transactionExternal) {

        if (transactionExternal.getBankOperation().equals(BankOperation.WITHDRAWAL)) {

            transactionExternal.getAccountList().forEach(accountSimpleDTO -> {

                log.info("Redoing transaction from bank: {} of: {} for account: {}.",
                        getBankName(transactionExternal),
                        BankOperation.WITHDRAWAL,
                        accountSimpleDTO.getAccountNumber());

                DepositDTO depositDTO = new DepositDTO();
                depositDTO.setAccountNumber(accountSimpleDTO.getAccountNumber());
                depositDTO.setAmount(accountSimpleDTO.getAmount());

                bankService.deposit(depositDTO);
            });
        }
    }

    private static String getBankName(TransactionExternal transactionExternal) {
        return Arrays.stream(Bank.values())
                .filter(bank -> bank.getCode().equals(transactionExternal.getExternalBankCode()))
                .findFirst()
                .get()
                .getName();
    }
}
