package com.uefs.starbank.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponseDTO {

    @JsonProperty("account_number")
    Integer accountNumber;

    Double balance;
}
