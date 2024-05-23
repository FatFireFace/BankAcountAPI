package com.tRef.BankAccount.Controllers;

import com.tRef.BankAccount.Exceptions.InsufficientFundsException;
import com.tRef.BankAccount.Exceptions.InvalidAccountException;
import com.tRef.BankAccount.Services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID toAccountId,
            @RequestParam BigDecimal amount) {

        try {
            UUID fromAccountId = getAuthenticatedUserAccountId(userDetails);
            bankAccountService.transferMoney(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (InsufficientFundsException | InvalidAccountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private UUID getAuthenticatedUserAccountId(UserDetails userDetails) {

        return UUID.randomUUID();
    }
}
