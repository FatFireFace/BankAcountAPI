package com.tRef.BankAccount.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    @NotNull
    private BigDecimal balance;

    @Column
    @NotNull
    private BigDecimal initialDeposit;

    @OneToOne(mappedBy = "account")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public BankAccount(BigDecimal initialDeposit){
        this.initialDeposit = initialDeposit;
    }


}