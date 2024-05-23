package com.tRef.BankAccount.Services;

import com.tRef.BankAccount.Entities.BankAccount;
import com.tRef.BankAccount.Exceptions.InsufficientFundsException;
import com.tRef.BankAccount.Repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@EnableAsync
@Service
public class BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Async
    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void updateBalances() {
        logger.debug("Starting scheduled task to update balances");
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
                logger.debug("Updated balance for account {}: {}", account.getId(), account.getBalance());
            }
        }
    }

    @Transactional
    public void transferMoney(UUID fromAccountId, UUID toAccountId, BigDecimal amount) throws InsufficientFundsException, AccountNotFoundException {
        logger.debug("Transferring {} from account {} to account {}", amount, fromAccountId, toAccountId);
        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId).orElseThrow(() -> {
            logger.error("Account with ID {} not found", fromAccountId);
            return new AccountNotFoundException("Source account not found");
        });
        BankAccount toAccount = bankAccountRepository.findById(toAccountId).orElseThrow(() -> {
            logger.error("Account with ID {} not found", toAccountId);
            return new AccountNotFoundException("Destination account not found");
        });

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            logger.warn("Insufficient funds in account {}", fromAccountId);
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        logger.debug("Transfer successful: {} transferred from account {} to account {}", amount, fromAccountId, toAccountId);
    }
}

