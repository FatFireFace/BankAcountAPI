package com.tRef.BankAccount.Services;

import com.tRef.BankAccount.Entities.User;
import com.tRef.BankAccount.Exceptions.*;
import com.tRef.BankAccount.Repositories.UserRepository;
import com.tRef.BankAccount.Specifications.UserSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(User user) throws UserRegistrationException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserRegistrationException("Username already exists");
        }

        for (String phone : user.getPhones()) {
            if (userRepository.existsByPhone(phone)) {
                throw new UserRegistrationException("Phone number already exists");
            }
        }

        for (String email : user.getEmails()) {
            if (userRepository.existsByEmail(email)) {
                throw new UserRegistrationException("Email already exists");
            }
        }

        // Кодирование пароля перед сохранением пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public void addPhone(UUID userId, String phone) throws UserNotFoundException, PhoneNumberAlreadyExistsException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getPhones().contains(phone)) {
            throw new PhoneNumberAlreadyExistsException();
        }
        user.addPhone(phone);
        userRepository.save(user);
    }

    public void removePhone(UUID userId, String phone) throws UserNotFoundException, LastPhoneNumberDeletionException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getPhones().size() <= 1) {
            throw new LastPhoneNumberDeletionException();
        }
        user.removePhone(phone);
        userRepository.save(user);
    }

    public void addEmail(UUID userId, String email) throws UserNotFoundException, EmailAlreadyExistsException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getEmails().contains(email)) {
            throw new EmailAlreadyExistsException();
        }
        user.addEmail(email);
        userRepository.save(user);
    }

    public void removeEmail(UUID userId, String email) throws UserNotFoundException, LastEmailDeletionException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getEmails().size() <= 1) {
            throw new LastEmailDeletionException();
        }
        user.removeEmail(email);
        userRepository.save(user);
    }

    public List<User> searchUsers(LocalDate birthDate, String phone, String name) {
        Specification<User> spec = Specification.where(UserSpecification.hasBirthDateAfter(birthDate))
                .and(UserSpecification.hasPhone(phone))
                .and(UserSpecification.hasNameLike(name));
        return userRepository.findAll(spec);
    }
}
