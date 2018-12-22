/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.controller;

/**
 *
 * @author PRASHANT
 */
public class clsVoidItemDetails {
    
    private String itemCode;
    
    private String itemName;
    
    private double itemVoidQty;
    
    private double itemVoidAmt;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemVoidQty() {
        return itemVoidQty;
    }

    public void setItemVoidQty(double itemVoidQty) {
        this.itemVoidQty = itemVoidQty;
    }

    public double getItemVoidAmt() {
        return itemVoidAmt;
    }

    public void setItemVoidAmt(double itemVoidAmt) {
        this.itemVoidAmt = itemVoidAmt;
    }
    
    
    
}
