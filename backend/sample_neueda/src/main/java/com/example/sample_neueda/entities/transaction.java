package com.example.sample_neueda.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "BUY" or "SELL"
    private int quantity;
    private double price;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private stocks stock;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private accounts account;

    // Constructors
    public transaction() {}

    public accounts getAccount() {
        return account;
    }

    public void setAccount(accounts account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", timestamp=" + timestamp +
                ", stock=" + stock +
                ", account=" + account +
                '}';
    }

    public transaction(String type, int quantity, double price, LocalDateTime timestamp, stocks stock, accounts account) {
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.stock = stock;
        this.account = account;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public stocks getStock() {
        return stock;
    }

    public void setStock(stocks stock) {
        this.stock = stock;
    }
}
