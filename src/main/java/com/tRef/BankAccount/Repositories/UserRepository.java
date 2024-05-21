package com.tRef.BankAccount.Repositories;

import com.tRef.BankAccount.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{

    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
