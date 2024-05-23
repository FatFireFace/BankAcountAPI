package com.tRef.BankAccount.Specifications;

import com.tRef.BankAccount.Entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {
    public static Specification<User> hasBirthDateAfter(LocalDate birthDate) {
        return (root, query, builder) -> birthDate == null ? null : builder.greaterThan(root.get("birthDate"), birthDate);
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, builder) -> phone == null ? null : builder.isMember(phone, root.get("phones"));
    }

    public static Specification<User> hasNameLike(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(root.get("name"), name + "%");
    }
}

