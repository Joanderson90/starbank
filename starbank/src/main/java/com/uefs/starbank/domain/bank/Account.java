package com.uefs.starbank.domain.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uefs.starbank.domain.enums.Bank;
import com.uefs.starbank.domain.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Account {

    private Integer id;

    @JsonProperty("account_number")
    private Integer number;

    private Integer digit;

    private String password;

    private List<User> userList;

    private Double balance;

    private Bank bank;
}
