package com.uefs.starbank.rest.client.bank;

import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.domain.enums.BankOperation;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class WebClientBank implements WebClientBankI {

    private final WebClient webClient;


    public WebClientBank() {
        HttpClient httpClient = HttpClient.create();

        this.webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }


    @Override
    public void deposit(DepositDTO depositDTO, Integer bankCode) {

        Arrays.stream(Bank.values()).filter(bank -> bank.getCode().equals(bankCode)).findFirst().ifPresent(bank -> {
            try {
                postSynchronously(bank.getCompleteUrl() + BankOperation.DEPOSIT.getOperationValue(), depositDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public Double withdrawal(WithdrawalDTO withdrawalDTO, Integer bankCode) {

        AtomicReference<Double> amountWithdrawn = new AtomicReference<>(0.0);

        Arrays.stream(Bank.values()).filter(bank -> bank.getCode().equals(bankCode)).findFirst().ifPresent(bank -> {
            try {
                amountWithdrawn.set(Double.parseDouble(postSynchronously(bank.getCompleteUrl() + BankOperation.WITHDRAWAL.getOperationValue(), withdrawalDTO)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        return amountWithdrawn.get();
    }

    @Override
    public Transaction getTransaction(Integer id, Integer bankCode) {
        AtomicReference<Transaction> transaction = new AtomicReference<>(new Transaction());

        Arrays.stream(Bank.values()).filter(bank -> bank.getCode().equals(bankCode)).findFirst().ifPresent(bank -> {
            try {
                transaction.set(getTransactionRequestAsync(bank.getCompleteUrl() + BankOperation.TRANSACTION.getOperationValue() + "/" + id));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return transaction.get();

    }

    public String postSynchronously(String url, Object requestBody) throws Exception {

        log.info("Going to hit API - URL {} Body {}", url, requestBody);
        String response = "";

        try {
            response = this.webClient
                    .method(HttpMethod.POST)
                    .uri(url)
                    .accept(MediaType.ALL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(requestBody), Object.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception ex) {
            log.error("Error while calling API ", ex);
            throw new RuntimeException("XYZ service api error: " + ex.getMessage());
        } finally {
            log.info("API Response {}", response);
        }

        return response;
    }

    public Transaction getTransactionRequestAsync(String url) {

        Transaction transactionResponse = new Transaction();

        try {
            transactionResponse = this.webClient
                    .method(HttpMethod.GET)
                    .uri(url)
                    .accept(MediaType.ALL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Transaction.class)
                    .block();

        } catch (Exception ex) {
            log.error("Error while calling API ", ex);
            throw new RuntimeException("XYZ service api error: " + ex.getMessage());
        } finally {
            log.info("API Response {}", transactionResponse);
        }

        return transactionResponse;
    }


}
