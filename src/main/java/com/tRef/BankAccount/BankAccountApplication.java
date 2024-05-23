package com.tRef.BankAccount;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Bank Account API", version = "1.0", description = "Bank Account Management API"))
public class BankAccountApplication{
	public static void main(String[] args) {
		SpringApplication.run(BankAccountApplication.class, args);
	}
}
