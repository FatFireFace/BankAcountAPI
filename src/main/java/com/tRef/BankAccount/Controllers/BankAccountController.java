package com.tRef.BankAccount.Controllers;

import com.tRef.BankAccount.Entities.BankAccount;
import com.tRef.BankAccount.Entities.User;
import com.tRef.BankAccount.Exceptions.InsufficientFundsException;
import com.tRef.BankAccount.Exceptions.InvalidAccountException;
import com.tRef.BankAccount.Repositories.UserRepository;
import com.tRef.BankAccount.Services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID toAccountId,
            @RequestParam BigDecimal amount) {

        try {
            UUID fromAccountId = getAuthenticatedUserAccountId(userDetails);
            bankAccountService.transferMoney(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (InsufficientFundsException | InvalidAccountException | AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private UUID getAuthenticatedUserAccountId(UserDetails userDetails) throws InvalidAccountException {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidAccountException("User not found"));
        BankAccount bankAccount = user.getBankAccount();
        if (bankAccount == null) {
            throw new InvalidAccountException("User has no associated bank account");
        }
        return bankAccount.getId();
    }
}


