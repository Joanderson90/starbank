package com.uefs.starbank.domain.enums;

import lombok.Getter;

@Getter
public enum BankOperation {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal"),
    TRANSFER("transfer"),
    TRANSACTION("transaction");


    private final String operationValue;

    BankOperation(String operation) {
        this.operationValue = operation;
    }
}
