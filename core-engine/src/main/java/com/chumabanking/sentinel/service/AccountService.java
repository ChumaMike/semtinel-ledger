package com.chumabanking.sentinel.service;

import com.chumabanking.sentinel.model.Account;
import com.chumabanking.sentinel.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        // 1. Python Sentinel Check (The "Gatekeeper")
        String sentinelUrl = "http://localhost:8000/v1/scrutinize";
        Map<String, Object> request = Map.of(
                "from_id", fromId,
                "to_id", toId,
                "amount", amount
        );

        // Call Python and get the result
        Map<String, String> response = restTemplate.postForObject(sentinelUrl, request, Map.class);

        if (response != null && "DENY".equals(response.get("decision"))) {
            throw new RuntimeException("SENTINEL REJECTED: " + response.get("reason"));
        }

        // 2. Original Accounting Logic (Only runs if Python says ALLOW)
        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}