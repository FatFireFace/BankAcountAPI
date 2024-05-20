package com.tRef.BankAccount.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Entity
@Validated
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private final BankAccount bankAccount;


    public User(String username, String password, long sum, String phoneNumber, String email){
        this.username = username;
        this.password = password;
        this.bankAccount = new BankAccount(sum);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setPatronymic(String patronymic){
        this.patronymic = patronymic;
    }

    public String getPatronymic(){
        return this.patronymic;
    }
}
