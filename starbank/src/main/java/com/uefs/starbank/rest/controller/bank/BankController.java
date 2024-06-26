package com.uefs.starbank.rest.controller.bank;

import com.uefs.starbank.domain.bank.Transaction;
import com.uefs.starbank.rest.dto.account.BalanceRequestDTO;
import com.uefs.starbank.rest.dto.account.BalanceResponseDTO;
import com.uefs.starbank.rest.dto.bank.DepositDTO;
import com.uefs.starbank.rest.dto.bank.TransferDTO;
import com.uefs.starbank.rest.dto.bank.WithdrawalDTO;
import com.uefs.starbank.service.bank.BankI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    BankI bankService;


    @PostMapping("/deposit")
    public void deposit(@RequestBody @Valid DepositDTO depositDTO) {
        bankService.deposit(depositDTO);
    }

    @PostMapping("/depositAnotherBank")
    public void depositAnotherBank(@RequestBody @Valid DepositDTO depositDTO) {
        bankService.deposit(depositDTO, 2);
    }

    @PostMapping("/withdrawal")
    public Double withdrawal(@RequestBody @Valid WithdrawalDTO withdrawalDTO) {
        return bankService.withdrawal(withdrawalDTO);
    }

    @PostMapping("/transfer")
    public Double transfer(@RequestBody @Valid TransferDTO transferDTO) {
        return bankService.transfer(transferDTO);
    }

    @GetMapping("/balance")
    public BalanceResponseDTO balance(@RequestBody @Valid BalanceRequestDTO balanceRequestDTO) {
        return bankService.getBalance(balanceRequestDTO);
    }

    @GetMapping("/transaction/{id}")
    public Transaction getTransaction(@PathVariable Integer id) {
        return bankService.getTransaction(id);
    }
}
