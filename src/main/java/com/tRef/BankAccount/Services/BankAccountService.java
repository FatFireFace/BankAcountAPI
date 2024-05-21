package com.tRef.BankAccount.Services;

import com.tRef.BankAccount.Repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BankAccountService{

    @Autowired
    private BankAccountRepository bankAccountRepository;
}
