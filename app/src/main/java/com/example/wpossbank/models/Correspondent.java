package com.example.wpossbank.models;

public class Correspondent {

    private int id;
    private String email;
    private String password;
    private double balance;

    public Correspondent(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Correspondent(int id, String email, String password, double balance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double saldo) {
        this.balance = saldo;
    }
}
