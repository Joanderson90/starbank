package com.uefs.starbank.rest.config.webclient;

import com.uefs.starbank.service.bank.BankI;
import com.uefs.starbank.service.config.StarBankApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {

    @Autowired
    BankI bankService;

    @Autowired
    private StarBankApiConfig starBankApiConfig;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(bankService.getThisBank().getCompleteUrl(starBankApiConfig)).build();
    }
}
