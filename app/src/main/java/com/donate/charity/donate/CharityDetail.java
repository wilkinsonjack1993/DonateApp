package com.donate.charity.donate;

/**
 * Created by Jack on 20/12/2017.
 */

public class CharityDetail {

    private String charityName;
    private String charityNumber;
    private int accountNumber;

    CharityDetail(String charityName, String charityNumber, int accountNumber) {
        this.charityName = charityName;
        this.charityNumber = charityNumber;
        this.accountNumber = accountNumber;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public String getCharityNumber() {
        return charityNumber;
    }

    public void setCharityNumber(String charityNumber) {
        this.charityNumber = charityNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
