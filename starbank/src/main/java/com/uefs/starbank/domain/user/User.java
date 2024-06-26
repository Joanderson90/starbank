package com.uefs.starbank.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty(message = "{field.name.mandatory}")
    private String name;

    @NotEmpty(message = "{field.cpf.mandatory}")
    @CPF(message = "{field.cpf.invalid}")
    private String cpf;

    @JsonProperty("date_birth")
    private LocalDate dateBirth;
}
