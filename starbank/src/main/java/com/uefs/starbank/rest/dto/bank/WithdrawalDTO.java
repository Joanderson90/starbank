package com.uefs.starbank.rest.dto.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawalDTO {

    @NotNull(message = "{field.account-number.mandatory}")
    @JsonProperty("account_number")
    Integer accountNumber;

    @NotNull(message = "{field.account-digit.mandatory}")
    Integer digit;

    @NotNull(message = "{field.amount.mandatory}")
    @Min(value = 1, message = "{field.amount-invalid.mandatory}")
    Double amount;

    @NotEmpty(message = "{field.password.mandatory}")
    private String password;

    Integer bankCodeRequestWithdrawal;

    Integer idTransaction;
}
