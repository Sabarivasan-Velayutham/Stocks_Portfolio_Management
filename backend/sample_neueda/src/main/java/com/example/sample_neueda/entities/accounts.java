package com.example.sample_neueda.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String number;
    private String type;
    private double balance;
    private double interest;
    private LocalDate lastInterestApplied;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<transaction> transactions;
    public accounts() {
    }

    public accounts(String name, String number, String type, double balance, double interest, LocalDate lastInterestApplied) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.balance = balance;
        this.interest = interest;
        this.lastInterestApplied = lastInterestApplied;
    }

    @Override
    public String toString() {
        return "accounts{" +
                "lastInterestApplied=" + lastInterestApplied +
                ", interest=" + interest +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public LocalDate getLastInterestApplied() {
        return lastInterestApplied;
    }

    public void setLastInterestApplied(LocalDate lastInterestApplied) {
        this.lastInterestApplied = lastInterestApplied;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
