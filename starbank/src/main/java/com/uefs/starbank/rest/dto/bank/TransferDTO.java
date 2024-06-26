package com.uefs.starbank.rest.dto.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TransferDTO {

    @NotNull(message = "{field.account-number.mandatory}")
    @JsonProperty("account_number")
    Integer accountNumber;

    @NotNull(message = "{field.account-digit.mandatory}")
    Integer digit;

    @NotEmpty(message = "{field.withdrawal-list.mandatory}")
    @Valid
    List<WithdrawalDTO> withdrawalOriginList;
}
