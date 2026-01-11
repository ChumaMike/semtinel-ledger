package com.chumabanking.sentinel.service;

import com.chumabanking.sentinel.repository.AccountRepository;
import com.chumabanking.sentinel.repository.TransactionRepository;
import com.sentinel.common.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks private AccountService accountService;

    @Test
    void shouldCreateAccountWithInitialZeroBalance() {
        // Arrange
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Account created = accountService.createAccount(1L, "SAVINGS", "Vacation Fund");

        // Assert
        assertEquals(BigDecimal.ZERO, created.getBalance());
        assertEquals("ACTIVE", created.getStatus());
        assertTrue(created.getAccountNumber().startsWith("200")); // Check our generator logic
    }

    @Test
    void shouldBlockExpenseIfInsufficientFunds() {
        // Arrange
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(new BigDecimal("50.00")); // Broke

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Act & Assert
        // Try to buy a R100 burger with R50 balance
        assertThrows(RuntimeException.class, () -> {
            accountService.logManualExpense("12345", new BigDecimal("100.00"), "Food", "Burger");
        });

        // Verify we NEVER saved the transaction
        verify(transactionRepository, never()).save(any());
    }
}