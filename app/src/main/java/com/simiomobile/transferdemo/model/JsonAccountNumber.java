package com.simiomobile.transferdemo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aor__Feyverly on 3/7/2559.
 */

public class JsonAccountNumber {

    @SerializedName("account_number")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
