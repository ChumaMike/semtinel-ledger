package com.chumabanking.sentinel.controller;

import com.chumabanking.sentinel.model.Account;
import com.chumabanking.sentinel.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chumabanking.sentinel.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId,
                           @RequestParam Long toId,
                           @RequestParam BigDecimal amount) {
        accountService.transferMoney(fromId, toId, amount);
        return "Transfer successful! Sent R" + amount + " to Account ID: " + toId;
    }
}