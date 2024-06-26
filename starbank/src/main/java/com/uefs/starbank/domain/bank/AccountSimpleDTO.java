package com.uefs.starbank.domain.bank;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSimpleDTO {

    private Integer accountNumber;

    private Double amount;
}
