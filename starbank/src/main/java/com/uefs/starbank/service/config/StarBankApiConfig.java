package com.uefs.starbank.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StarBankApiConfig {

    @Value("${com.uefs.starbank.bank-code}")
    private String bankCode;

}
