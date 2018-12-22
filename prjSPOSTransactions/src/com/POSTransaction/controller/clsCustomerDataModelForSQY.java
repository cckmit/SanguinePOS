/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.controller;

/**
 *
 * @author sss
 */
public class clsCustomerDataModelForSQY 
{    
    private String transactionId,consumer,outlet_uuid,created_at,customerCode;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    private int status;
    private long custMobileNo;
    
    public String getTransactionId() {
        return transactionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    

    public String getConsumer() {
        return consumer;
    }

    public String getOutlet_uuid() {
        return outlet_uuid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public double getRedeemed_amt() {
        return redeemed_amt;
    }
    private double redeemed_amt;
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public void setOutlet_uuid(String outlet_uuid) {
        this.outlet_uuid = outlet_uuid;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setRedeemed_amt(double redeemed_amt) {
        this.redeemed_amt = redeemed_amt;
    }

    public long getCustMobileNo() {
        return custMobileNo;
    }

    public void setCustMobileNo(long custMobileNo) {
        this.custMobileNo = custMobileNo;
    }
    
    
}
