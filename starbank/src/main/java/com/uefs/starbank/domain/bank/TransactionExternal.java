package com.uefs.starbank.domain.bank;

import com.uefs.starbank.domain.enums.BankOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionExternal {

    private Integer id;

    private BankOperation bankOperation;

    private List<AccountSimpleDTO> accountList;

    private Integer externalBankCode;

}
