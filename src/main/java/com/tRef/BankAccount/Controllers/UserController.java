package com.tRef.BankAccount.Controllers;

import com.tRef.BankAccount.Entities.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/user")
@RestController
public class UserController{

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        return null;
    }

}
