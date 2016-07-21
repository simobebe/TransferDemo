package com.simiomobile.transferdemo.model;

/**
 * Created by Aor__Feyverly on 3/7/2559.
 */

public class JsonAccount {
    private String accountNumber;
    private String imageAccountURL;
    private double balanceAccount;

    public String getImageAccountURL() {
        return imageAccountURL;
    }

    public void setImageAccountURL(String imageAccountURL) {
        this.imageAccountURL = imageAccountURL;
    }


    public double getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(double balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
