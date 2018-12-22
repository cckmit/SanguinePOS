
package com.POSTransaction.controller;

public class clsSpecialOrderItemDtl {
    
    private String itemCode;
    
    private String productCode;
    
    private double stockQty;
    
    private double orderQty;
    
    private double weight;
    
    private String advOrderNo;
    
    private String SOCode;
    
    private String placeOrderCode;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public double getStockQty() {
        return stockQty;
    }

    public void setStockQty(double stockQty) {
        this.stockQty = stockQty;
    }

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAdvOrderNo() {
        return advOrderNo;
    }

    public void setAdvOrderNo(String advOrderNo) {
        this.advOrderNo = advOrderNo;
    }

    public String getSOCode() {
        return SOCode;
    }

    public void setSOCode(String SOCode) {
        this.SOCode = SOCode;
    }

    public String getPlaceOrderCode() {
        return placeOrderCode;
    }

    public void setPlaceOrderCode(String placeOrderCode) {
        this.placeOrderCode = placeOrderCode;
    }
}
