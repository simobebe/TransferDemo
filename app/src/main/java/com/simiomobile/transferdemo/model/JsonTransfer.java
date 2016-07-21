package com.simiomobile.transferdemo.model;

/**
 * Created by Aor__Feyverly on 3/7/2559.
 */

public class JsonTransfer {
    private String fromAcountId;
    private String toAccountId;
    private String amount;
    private String fee;
    private boolean status;
    public JsonTransfer(){

    }

    public String getFromAcountId() {
        return fromAcountId;
    }

    public void setFromAcountId(String fromAcountId) {
        this.fromAcountId = fromAcountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
