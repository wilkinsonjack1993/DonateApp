package com.donate.charity.donate;

import org.joda.time.LocalDateTime;

/**
 * Created by Jack on 19/12/2017.
 */

public class TransactionRecord {
    private String charityName;
    private String currency;
    private double amount;
    private LocalDateTime dateTime;

    TransactionRecord(String charityName, String currency, double amount, LocalDateTime dateTime) {
        this.charityName = charityName;
        this.currency = currency;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
