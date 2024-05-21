package com.tRef.BankAccount.Entities;

import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Validated
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String patronymic;
    @Column
    private String email;
    @Column
    private String phone;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private final BankAccount bankAccount;

    public User() {
        this.bankAccount = null;  // Документация JPA просит создавать пустой конструктор для сущностей
    }


    public User(String username, String password, BigDecimal sum, String phone, String email){
        this.username = username;
        this.password = password;
        this.bankAccount = new BankAccount(sum);
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    //

    public UUID getId() {
        return id;
    }
}
