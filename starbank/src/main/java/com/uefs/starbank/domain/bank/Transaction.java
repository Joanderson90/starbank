package com.uefs.starbank.domain.bank;

import com.uefs.starbank.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Integer id;

    private TransactionStatus transactionStatus;
}
