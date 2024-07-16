package com.uefs.starbank.domain.enums;

import com.uefs.starbank.service.config.StarBankApiConfig;
import lombok.Getter;

@Getter
public enum Bank {
    STAR_BANK(1, "Star Bank", "http://", 8080),
    STAR_BANK_COPY_1(2, "Star Bank Copy 1", "http://", 8081),
    STAR_BANK_COPY_2(3, "Star Bank Copy 2", "http://", 8082);


    private final Integer code;

    private final String name;

    private final String url;

    private final Integer port;

    Bank(int code, String name, String url, int port) {
        this.code = code;
        this.name = name;
        this.url = url;
        this.port = port;
    }


    public String getCompleteUrl(StarBankApiConfig starBankApiConfig) {

        String ipAddressBank = switch (this.code) {
            case 1 -> starBankApiConfig.getIpAddressBank1();
            case 2 -> starBankApiConfig.getIpAddressBank2();
            case 3 -> starBankApiConfig.getIpAddressBank3();
            default -> "localhost";
        };


        return this.getUrl() + ipAddressBank + ":" + this.getPort() + "/api/bank/";
    }

    public static boolean containsCode(Integer code) {

        for (Bank bank : Bank.values()) {
            if (bank.code.equals(code)) {
                return true;
            }
        }

        return false;
    }


}
