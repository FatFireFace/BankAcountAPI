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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public void registerUser(User user) throws UserRegistrationException {
        logger.debug("Registering user: {}", user);
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Username {} already exists", user.getUsername());
            throw new UserRegistrationException("Username already exists");
        }

        for (String phone : user.getPhones()) {
            if (userRepository.existsByPhone(phone)) {
                logger.warn("Phone number {} already exists", phone);
                throw new UserRegistrationException("Phone number already exists");
            }
        }

        for (String email : user.getEmails()) {
            if (userRepository.existsByEmail(email)) {
                logger.warn("Email {} already exists", email);
                throw new UserRegistrationException("Email already exists");
            }
        }

        userRepository.save(user);
    }

    public void addPhone(UUID userId, String phone) throws UserNotFoundException, PhoneNumberAlreadyExistsException {
        logger.debug("Adding phone {} for user with ID {}", phone, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User with ID {} not found", userId);
            return new UserNotFoundException("User not found");
        });
        if (user.getPhones().contains(phone)) {
            logger.warn("Phone number {} already exists for user {}", phone, userId);
            throw new PhoneNumberAlreadyExistsException();
        }
        user.addPhone(phone);
        userRepository.save(user);
    }

    public void removePhone(UUID userId, String phone) throws UserNotFoundException, LastPhoneNumberDeletionException {
        logger.debug("Removing phone {} for user with ID {}", phone, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User with ID {} not found", userId);
            return new UserNotFoundException("User not found");
        });
        if (user.getPhones().size() <= 1) {
            logger.warn("Cannot delete the last phone number for user {}", userId);
            throw new LastPhoneNumberDeletionException();
        }
        user.removePhone(phone);
        userRepository.save(user);
    }

    public void addEmail(UUID userId, String email) throws UserNotFoundException, EmailAlreadyExistsException {
        logger.debug("Adding email {} for user with ID {}", email, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User with ID {} not found", userId);
            return new UserNotFoundException("User not found");
        });
        if (user.getEmails().contains(email)) {
            logger.warn("Email {} already exists for user {}", email, userId);
            throw new EmailAlreadyExistsException();
        }
        user.addEmail(email);
        userRepository.save(user);
    }

    public void removeEmail(UUID userId, String email) throws UserNotFoundException, LastEmailDeletionException {
        logger.debug("Removing email {} for user with ID {}", email, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User with ID {} not found", userId);
            return new UserNotFoundException("User not found");
        });
        if (user.getEmails().size() <= 1) {
            logger.warn("Cannot delete the last email for user {}", userId);
            throw new LastEmailDeletionException();
        }
        user.removeEmail(email);
        userRepository.save(user);
    }

    public List<User> searchUsers(LocalDate birthDate, String phone, String name) {
        logger.debug("Searching users with birthDate: {}, phone: {}, name: {}", birthDate, phone, name);
        Specification<User> spec = Specification.where(UserSpecification.hasBirthDateAfter(birthDate))
                .and(UserSpecification.hasPhone(phone))
                .and(UserSpecification.hasNameLike(name));
        return userRepository.findAll(spec);
    }
}
