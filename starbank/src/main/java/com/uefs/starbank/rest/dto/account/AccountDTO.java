package com.uefs.starbank.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uefs.starbank.domain.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {

    @NotEmpty(message = "{field.password.mandatory}")
    private String password;

    @NotEmpty(message = "{field.user-list.mandatory}")
    @Valid
    @JsonProperty("user_list")
    private List<User> userList;

}
