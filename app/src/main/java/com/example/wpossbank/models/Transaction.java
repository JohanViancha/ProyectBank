package com.example.wpossbank.models;

import java.util.Date;

public class Transaction {

    private String type;
    private double amount;
    private String date;
    private String identification;



    public Transaction(String type, double amount, String date, String identification) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.identification = identification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
