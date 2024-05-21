package com.tRef.BankAccount.Services;

import com.tRef.BankAccount.Entities.BankAccount;
import com.tRef.BankAccount.Repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@EnableAsync
@Service
public class BankAccountService{

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Async
    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        for (BankAccount account : accounts) {
            LocalDateTime now = LocalDateTime.now();
            long minutesElapsed = ChronoUnit.MINUTES.between(account.getLastInterestAddedAt(), now);

            if (minutesElapsed >= 1) {
                BigDecimal interestRate = new BigDecimal("0.05");
                BigDecimal maxBalance = account.getMaxBalance();

                for (long i = 0; i < minutesElapsed; i++) {
                    BigDecimal newBalance = account.getBalance().add(account.getBalance().multiply(interestRate));
                    if (newBalance.compareTo(maxBalance) > 0) {
                        newBalance = maxBalance;
                        break;
                    }
                    account.setBalance(newBalance);
                }
                account.setLastInterestAddedAt(now);
                bankAccountRepository.save(account);
            }
        }
    }
}
