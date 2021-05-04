package com.example.wpossbank.models;

public class Corresponsal {

    private int id;
    private String email;
    private String password;
    private double saldo;

    public Corresponsal(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Corresponsal(int id, String email, String password, double saldo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.saldo = saldo;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
