package com.chumabanking.sentinel.service;

import com.chumabanking.sentinel.repository.AccountRepository;
import com.chumabanking.sentinel.repository.GoalRepository;
import com.chumabanking.sentinel.repository.TransactionRepository;
import com.sentinel.common.model.Account;
import com.sentinel.common.model.Goal;
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

@ExtendWith(MockitoExtension.class) // 1. Setup Mockito
class GoalServiceTest {

    @Mock private GoalRepository goalRepository;        // Fake DB
    @Mock private AccountRepository accountRepository;  // Fake DB
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks private GoalService goalService;       // The Service we are testing

    @Test
    void shouldCreateGoalSuccessfully() {
        // Arrange
        Long userId = 1L;
        String name = "Test Car";
        BigDecimal target = new BigDecimal("10000");

        when(goalRepository.save(any(Goal.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Goal created = goalService.createGoal(userId, name, target, "Desc", "HIGH", null);

        // Assert
        assertEquals("IN_PROGRESS", created.getStatus());
        assertEquals(BigDecimal.ZERO, created.getCurrentAmount());
        assertEquals("HIGH", created.getPriority());
    }

    @Test
    void shouldContributeAndCompleteGoal() {
        // Arrange
        // 1. Create a Fake Goal (Target: 1000, Current: 0)
        Goal goal = new Goal();
        goal.setGoalId(1L);
        goal.setTargetAmount(new BigDecimal("1000"));
        goal.setCurrentAmount(BigDecimal.ZERO);
        goal.setStatus("IN_PROGRESS");

        // 2. Create a Fake Account (Balance: 5000)
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(new BigDecimal("5000"));

        // 3. Teach Mocks to return our fake data
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(goalRepository.save(any(Goal.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act: Contribute 1000 (Full Amount)
        Goal updatedGoal = goalService.contributeToGoal(1L, "12345", new BigDecimal("1000"));

        // Assert
        assertEquals("ACHIEVED", updatedGoal.getStatus()); // Should be done!
        assertEquals(new BigDecimal("1000"), updatedGoal.getCurrentAmount()); // Should be full
        assertEquals(new BigDecimal("4000"), account.getBalance()); // Account should drop by 1000
    }

    @Test
    void shouldFailIfInsufficientFunds() {
        // Arrange
        Goal goal = new Goal();
        goal.setGoalId(1L);

        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(new BigDecimal("10")); // Only R10 in bank

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Act & Assert
        // We expect an error when we try to move R100
        assertThrows(RuntimeException.class, () -> {
            goalService.contributeToGoal(1L, "12345", new BigDecimal("100"));
        });
    }
}