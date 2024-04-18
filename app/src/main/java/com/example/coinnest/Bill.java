package com.example.coinnest;

import java.util.Date;

public class Bill {
    private String Billname;
    private double amount;
    private String dueDate;
    private String category;
    private boolean markedForDeletion;
    // Default constructor required for Gson deserialization
    public Bill() {
    }

    // Constructor
    public Bill(String Billname, double amount, String dueDate, String category) {
        this.Billname = Billname;
        this.amount = amount;
        this.dueDate = dueDate;
        this.category = category;
    }

    // Getter for name
    public String getBillName() {
        return Billname;
    }

    // Setter for name
    public void setBillName(String Billname) {
        this.Billname = Billname;
    }

    // Getter for amount
    public Double getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    // Getter for due date
    public String getDueDate() {
        return dueDate;
    }

    // Setter for due date
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    // Getter for category
    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }
}
