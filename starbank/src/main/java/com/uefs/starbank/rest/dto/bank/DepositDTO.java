package com.uefs.starbank.rest.dto.bank;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {

    @NotNull(message = "{field.account-number.mandatory}")
    @JsonProperty("account_number")
    Integer accountNumber;

    @NotNull(message = "{field.amount.mandatory}")
    @Min(value = 1, message = "{field.amount-invalid.mandatory}")
    Double amount;
}
