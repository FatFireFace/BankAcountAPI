package com.tRef.BankAccount.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Validated
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank(message = "Не может быть пустым.")
    @Column(name = "username", unique = true)
    private String username;
    @NotBlank(message = "Не может быть пустым.")
    @Column(name = "password")
    private String password;
    @NotBlank(message = "Не может быть пустым.")
    @Column
    private final String name;
    @NotBlank(message = "Не может быть пустым.")
    @Column
    private final String surname;
    @NotBlank(message = "Не может быть пустым.")
    @Column
    private final String patronymic;
    @NotBlank(message = "Не может быть пустым.")
    @Column
    private final LocalDate birthDate;
    @NotEmpty(message = "Должен быть привязан хотя бы один адрес")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_emails", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email", unique = true)
    @Email(message = "Введите корректный адрес электронной почты")
    private Set<String> emails = new HashSet<>();
    @NotEmpty(message = "Введите как минимум один номер")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_phones", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone", unique = true)
    private Set<String> phones = new HashSet<>();
    @NotBlank(message = "Не может быть пустым.")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private final BankAccount bankAccount;

    public User() {
        this.bankAccount = null;
        this.patronymic = null;
        this.name = null;
        this.surname = null;
        birthDate = null;// Документация JPA просит создавать пустой конструктор для сущностей
    }

    public User(String username, String password, String name, String surname, String patronymic, BigDecimal initialDeposit, String phone, String email, LocalDate birthday) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.bankAccount = new BankAccount(initialDeposit);
        this.phones.add(phone);
        this.emails.add(email);
        this.birthDate = birthday;
    }


    public void addEmail(String email) {
        this.emails.add(email);
    }

    public void removeEmail(String email) {
        if (this.emails.size() > 1) {
            this.emails.remove(email);
        } else {
            throw new IllegalStateException("Пользователь должен иметь хотя бы один email.");
        }
    }

    public void addPhone(String phone) {
        this.phones.add(phone);
    }

    public void removePhone(String phone) {
        if (this.phones.size() > 1) {
            this.phones.remove(phone);
        } else {
            throw new IllegalStateException("Пользователь должен иметь хотя бы один привязанный номер.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName(){
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic(){
        return this.patronymic;
    }
    public UUID getId() {
        return id;
    }

    public Set<String> getEmails(){
        return emails;
    }
    public Set<String> getPhones() {
        return phones;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }


}
