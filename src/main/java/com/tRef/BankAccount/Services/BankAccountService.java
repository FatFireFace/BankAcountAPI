package com.tRef.BankAccount.Services;

import com.tRef.BankAccount.Entities.BankAccount;
import com.tRef.BankAccount.Exceptions.InsufficientFundsException;
import com.tRef.BankAccount.Exceptions.InvalidAccountException;
import com.tRef.BankAccount.Repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@EnableAsync
@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Async
    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        for (BankAccount account : accounts) {
            synchronized (account) {
                LocalDateTime now = LocalDateTime.now();
                long minutesElapsed = ChronoUnit.MINUTES.between(account.getLastInterestAddedAt(), now);

                if (minutesElapsed >= 1) {
                    BigDecimal interestRate = new BigDecimal("0.05");
                    BigDecimal initialBalance = account.getInitialDeposit();
                    BigDecimal maxBalance = initialBalance.multiply(new BigDecimal("2.07"));

                    BigDecimal newBalance = account.getBalance();

                    for (long i = 0; i < minutesElapsed; i++) {
                        newBalance = newBalance.add(newBalance.multiply(interestRate));
                        if (newBalance.compareTo(maxBalance) > 0) {
                            newBalance = maxBalance;
                            break;
                        }
                    }

                    account.setBalance(newBalance);
                    account.setLastInterestAddedAt(now);
                    bankAccountRepository.save(account);
                }
            }
        }
    }

    @Transactional
    public void transferMoney(UUID fromAccountId, UUID toAccountId, BigDecimal amount) throws InsufficientFundsException, InvalidAccountException {
        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidAccountException("Cannot transfer money to the same account");
        }

        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new InvalidAccountException("Source account not found"));
        BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new InvalidAccountException("Destination account not found"));

        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds in the source account");
                }

                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                bankAccountRepository.save(fromAccount);
                bankAccountRepository.save(toAccount);
            }
        }
    }
}
