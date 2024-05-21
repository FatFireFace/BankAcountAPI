package com.tRef.BankAccount.Repositories;

import com.tRef.BankAccount.Entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID>{

}
