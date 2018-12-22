/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSMaster.controller;

/**
 *
 * @author prashant
 */
public class clsPromotionGroupMaster {
    
    private String promoGroupCode;
    
    private String itemCode;
    
    private String itemName;
    
    private boolean itemSelected;
    
    private String subGroupName;
    
    private String groupName;
    
    private double itemRate;

    public String getPromoGroupCode() {
        return promoGroupCode;
    }

    public void setPromoGroupCode(String promoGroupCode) {
        this.promoGroupCode = promoGroupCode;
    }

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

    public boolean isItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        this.itemSelected = itemSelected;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public double getItemRate() {
        return itemRate;
    }

    public void setItemRate(double itemRate) {
        this.itemRate = itemRate;
    }
    
    
}
