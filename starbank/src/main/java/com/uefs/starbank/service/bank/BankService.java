package com.uefs.starbank.service.bank;

import com.uefs.starbank.data.account.DataAccount;
import com.uefs.starbank.data.token.DataToken;
import com.uefs.starbank.data.transaction.DataTransaction;
import com.uefs.starbank.data.transaction.DataTransactionExternal;
import com.uefs.starbank.domain.bank.AccountSimpleDTO;
import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.domain.bank.TransactionExternal;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.domain.enums.BankOperation;
import com.uefs.starbank.domain.enums.TransactionStatus;
import com.uefs.starbank.mutex.AccountMutex;
import com.uefs.starbank.rest.client.bank.WebClientBankI;
import com.uefs.starbank.rest.dto.account.BalanceRequestDTO;
import com.uefs.starbank.rest.dto.account.BalanceResponseDTO;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.TransferDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;
import com.uefs.starbank.service.config.StarBankApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BankService implements BankI {

    @Autowired
    WebClientBankI webClientBank;

    @Autowired
    StarBankApiConfig starBankApiConfig;


    @Override
    public void deposit(DepositDTO deposit) {

        DataAccount.findByNumber(deposit.getAccountNumber())
                .ifPresentOrElse((account -> {

                            while (!DataToken.hasToken()) {
                                log.info("Waiting token for do deposit...");
                            }

                            while (AccountMutex.hasAccessToCriticalSection(account.getNumber())) ;

                            AccountMutex.addAccessToCriticalSection(account.getNumber());

                            account.setBalance(account.getBalance() + deposit.getAmount());

                            AccountMutex.removeAccessToCriticalSection(account.getNumber());
                        }),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Account not found.");
                        });
    }

    @Override
    public void deposit(DepositDTO deposit, Integer bankCode) {
        webClientBank.deposit(deposit, bankCode);
    }


    @Override
    public Double withdrawal(WithdrawalDTO withdrawalDTO) {

        DataAccount.findByNumberAndDigitAndPassword(withdrawalDTO.getAccountNumber(),
                        withdrawalDTO.getDigit(),
                        withdrawalDTO.getPassword())
                .ifPresentOrElse(account -> {

                            while (!DataToken.hasToken()) {
                                log.info("Waiting token for do withdrawal...");
                            }

                            while (AccountMutex.hasAccessToCriticalSection(account.getNumber())) ;

                            AccountMutex.addAccessToCriticalSection(account.getNumber());

                            double finalBalance = account.getBalance() - withdrawalDTO.getAmount();

                            if (finalBalance < 0.0) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Amount not valid.");
                            }

                            account.setBalance(finalBalance);

                            handleExternalBankRequest(withdrawalDTO, BankOperation.WITHDRAWAL);

                            AccountMutex.removeAccessToCriticalSection(account.getNumber());
                        },

                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Account not found.");
                        });

        return withdrawalDTO.getAmount();
    }

    private static void handleExternalBankRequest(WithdrawalDTO withdrawalDTO, BankOperation bankOperation) {

        if (withdrawalDTO.getBankCodeRequestWithdrawal() != null && Bank.containsCode(withdrawalDTO.getBankCodeRequestWithdrawal())) {

            AccountSimpleDTO accountSimple = new AccountSimpleDTO(withdrawalDTO.getAccountNumber(), withdrawalDTO.getAmount());

            DataTransactionExternal.findById(withdrawalDTO.getIdTransaction())
                    .ifPresentOrElse(transactionExternal -> transactionExternal.getAccountList().add(accountSimple),
                            () -> {
                                List<AccountSimpleDTO> accountSimpleDTOList = new ArrayList<>();
                                accountSimpleDTOList.add(accountSimple);

                                TransactionExternal transactionExternal = TransactionExternal
                                        .builder()
                                        .id(withdrawalDTO.getIdTransaction())
                                        .bankOperation(bankOperation)
                                        .accountList(accountSimpleDTOList)
                                        .externalBankCode(withdrawalDTO.getBankCodeRequestWithdrawal())
                                        .build();

                                DataTransactionExternal.addTransaction(transactionExternal);
                            });

        }
    }

    @Override
    public BalanceResponseDTO getBalance(BalanceRequestDTO balanceRequestDTO) {

        BalanceResponseDTO balanceResponseDTO = new BalanceResponseDTO();

        DataAccount.findByNumber(balanceRequestDTO.getAccountNumber())
                .ifPresentOrElse(account -> {
                            balanceResponseDTO.setAccountNumber(account.getNumber());
                            balanceResponseDTO.setBalance(account.getBalance());
                        },
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Account not found.");
                        });

        return balanceResponseDTO;
    }

    @Override
    public Double transfer(TransferDTO transferDTO) {

        validTransferAccountOriginRoot(transferDTO);

        Map<WithdrawalDTO, Double> accountWithdrawalMap = new HashMap<>();

        Double amountForTransfer;

        Transaction transaction = DataTransaction.addTransaction();


        try {
            transferDTO.getWithdrawalOriginList().forEach(withdrawalOrigin -> {

                if (withdrawalOrigin.getDigit().equals(getThisBank().getCode())) {
                    accountWithdrawalMap.put(withdrawalOrigin, withdrawal(withdrawalOrigin));

                } else {
                    withdrawalOrigin.setBankCodeRequestWithdrawal(getThisBank().getCode());
                    withdrawalOrigin.setIdTransaction(transaction.getId());

                    accountWithdrawalMap.put(withdrawalOrigin, webClientBank.withdrawal(withdrawalOrigin, withdrawalOrigin.getDigit()));
                }
            });

            amountForTransfer = accountWithdrawalMap.values().stream().reduce(0.0, Double::sum);

            doDeposit(transferDTO.getAccountNumber(), amountForTransfer, transferDTO.getDigit());

        } catch (Exception e) {
            log.error(e.getMessage());

            undoTransfer(accountWithdrawalMap);

            DataTransaction.updateStatus(transaction.getId(), TransactionStatus.FINISHED);

            throw new RuntimeException(e);

        }

        DataTransaction.updateStatus(transaction.getId(), TransactionStatus.FINISHED);


        return amountForTransfer;

    }


    private void doDeposit(Integer accountNumber, Double amount, Integer accountDigit) {
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setAccountNumber(accountNumber);
        depositDTO.setAmount(amount);

        if (accountDigit.equals(getThisBank().getCode())) {
            deposit(depositDTO);
        } else {
            webClientBank.deposit(depositDTO, accountDigit);
        }
    }


    private void validTransferAccountOriginRoot(TransferDTO transferDTO) {

        boolean accountOriginThisBankPresent = transferDTO.getWithdrawalOriginList()
                .stream()
                .anyMatch(withdrawal ->
                        DataAccount.findByNumberAndDigitAndPassword(withdrawal.getAccountNumber(),
                                        getThisBank().getCode(),
                                        withdrawal.getPassword())
                                .isPresent()
                );

        if (!accountOriginThisBankPresent) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Account origin root not found.");
        }


    }

    private void undoTransfer(Map<WithdrawalDTO, Double> accountWithdrawalMap) {
        accountWithdrawalMap.forEach((withdrawalDTO, amount) -> doDeposit(withdrawalDTO.getAccountNumber(), amount, withdrawalDTO.getDigit()));
    }

    @Override
    public Transaction getTransaction(Integer id) {
        return DataTransaction
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transaction not found."));

    }

    @Override
    public void handleToken() throws InterruptedException {

        try {

            Bank thisBank = this.getThisBank();

            log.info("({}) Got the token!", thisBank.getName());

            DataToken.setToken(true);

            TimeUnit.SECONDS.sleep(3);

            Bank nextBankHope = getNextBankHope(thisBank);

            log.info("Passing token to bank: {}. ", nextBankHope.getName());

            DataToken.setToken(false);

            webClientBank.sendToken(nextBankHope.getCode());

            log.info("Token away!");


        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();

            throw new InterruptedException();
        }


    }

    private Bank getNextBankHope(Bank thisBank) {
        return switch (thisBank.getCode()) {
            case 1 -> Bank.STAR_BANK_COPY_1;
            case 2 -> Bank.STAR_BANK_COPY_2;
            default -> Bank.STAR_BANK;
        };

    }

    @Override
    public Bank getThisBank() {
        return Arrays
                .stream(Bank.values()).filter(bank -> bank.getCode().equals(Integer.parseInt(starBankApiConfig.getBankCode())))
                .findFirst()
                .get();
    }


}
