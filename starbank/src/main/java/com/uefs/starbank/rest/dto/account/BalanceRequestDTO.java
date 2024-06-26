package com.uefs.starbank.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BalanceRequestDTO {

    @NotNull(message = "{field.account-number.mandatory}")
    @JsonProperty("account_number")
    Integer accountNumber;


}
