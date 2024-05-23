package com.tRef.BankAccount.Controllers;

import com.tRef.BankAccount.Entities.User;
import com.tRef.BankAccount.Exceptions.*;
import com.tRef.BankAccount.Services.CustomUserDetailsService;
import com.tRef.BankAccount.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        try {
            userService.registerUser(user);
            logger.info("User {} created successfully", user.getUsername());
            return ResponseEntity.ok("User created successfully");
        } catch (UserRegistrationException e) {
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/phones")
    public ResponseEntity<?> addPhone(@PathVariable UUID userId, @RequestParam String phone) {
        try {
            userService.addPhone(userId, phone);
            logger.info("Phone {} added for user with ID {}", phone, userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("Error adding phone: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (PhoneNumberAlreadyExistsException e) {
            logger.error("Error adding phone: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Номер уже зарегестрирован");
        }
    }

    @DeleteMapping("/{userId}/phones")
    public ResponseEntity<?> removePhone(@PathVariable UUID userId, @RequestParam String phone) {
        try {
            userService.removePhone(userId, phone);
            logger.info("Phone {} removed for user with ID {}", phone, userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("Error removing phone: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (LastPhoneNumberDeletionException e) {
            logger.error("Error removing phone: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Нельзя удалить последний номер");
        }
    }

    @PutMapping("/{userId}/emails")
    public ResponseEntity<?> addEmail(@PathVariable UUID userId, @RequestParam String email) {
        try {
            userService.addEmail(userId, email);
            logger.info("Email {} added for user with ID {}", email, userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("Error adding email: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (EmailAlreadyExistsException e) {
            logger.error("Error adding email: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Адрес уже зарегестрирован");
        }
    }

    @DeleteMapping("/{userId}/emails")
    public ResponseEntity<?> removeEmail(@PathVariable UUID userId, @RequestParam String email) {
        try {
            userService.removeEmail(userId, email);
            logger.info("Email {} removed for user with ID {}", email, userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("Error removing email: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (LastEmailDeletionException e) {
            logger.error("Error removing email: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Нельзя удалить последний номер");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) LocalDate birthDate,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String name) {
        logger.debug("Searching users with birthDate: {}, phone: {}, name: {}", birthDate, phone, name);
        List<User> users = userService.searchUsers(birthDate, phone, name);
        return ResponseEntity.ok(users);
    }
}
