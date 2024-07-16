package com.uefs.starbank.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StarBankApiConfig {

    @Value("${com.uefs.starbank.bank-code}")
    private String bankCode;

    @Value("${com.uefs.starbank.ip-address-bank-1}")
    private String ipAddressBank1;

    @Value("${com.uefs.starbank.ip-address-bank-2}")
    private String ipAddressBank2;

    @Value("${com.uefs.starbank.ip-address-bank-3}")
    private String ipAddressBank3;


}
