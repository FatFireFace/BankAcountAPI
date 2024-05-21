package com.tRef.BankAccount.Controllers;

import com.tRef.BankAccount.Entities.User;
import com.tRef.BankAccount.Exceptions.*;
import com.tRef.BankAccount.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        return null;
    }

    @PutMapping("/{userId}/phones")
    public ResponseEntity<?> addPhone(@PathVariable UUID userId, @RequestParam String phone) {
        try {
            userService.addPhone(userId, phone);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (PhoneNumberAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Phone number already exists");
        }
    }


    @DeleteMapping("/{userId}/phones")
    public ResponseEntity<?> removePhone(@PathVariable UUID userId, @RequestParam String phone) {
        try {
            userService.removePhone(userId, phone);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (LastPhoneNumberDeletionException e) {
            return ResponseEntity.badRequest().body("Cannot delete last phone number");
        }
    }

    @PutMapping("/{userId}/emails")
    public ResponseEntity<?> addEmail(@PathVariable UUID userId, @RequestParam String email) {
        try {
            userService.addEmail(userId, email);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
    }

    @DeleteMapping("/{userId}/emails")
    public ResponseEntity<?> removeEmail(@PathVariable UUID userId, @RequestParam String email) {
        try {
            userService.removeEmail(userId, email);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (LastEmailDeletionException e) {
            return ResponseEntity.badRequest().body("Cannot delete last email");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) LocalDate birthDate,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String name) {
        List<User> users = userService.searchUsers(birthDate, phone, name);
        return ResponseEntity.ok(users);
    }

}
