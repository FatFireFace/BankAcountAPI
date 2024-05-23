package com.tRef.BankAccount.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private BigDecimal balance;

    @Column
    @NotNull
    private final BigDecimal initialDeposit;

    @Column
    private final BigDecimal maxBalance;

    @Column
    private LocalDateTime lastInterestAddedAt;

    @Column
    private final LocalDateTime createdAt;

    @OneToOne(mappedBy = "account")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    //конструкторы
    public BankAccount(){
        this.initialDeposit = null;
        createdAt = null;
        maxBalance = null;
    }
    public BankAccount(BigDecimal initialDeposit){
        this.initialDeposit = initialDeposit;
        this.createdAt = LocalDateTime.now();
        this.maxBalance = initialDeposit.multiply(new BigDecimal("2.07"));
    }


    //геттеры-сеттеры
    public BigDecimal getInitialDeposit(){
        return initialDeposit;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastInterestAddedAt() {
        return lastInterestAddedAt;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setLastInterestAddedAt(LocalDateTime lastInterestAddedAt) {
        this.lastInterestAddedAt = lastInterestAddedAt;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getMaxBalance(){
        return maxBalance;
    }
}