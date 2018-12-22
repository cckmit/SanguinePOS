/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spos.controller;

/**
 *
 * @author Prashant
 */
public class clsPOSMaster
{

    private String POSCode;
    private String POSName;
    private String POSType;    
    private String debitCardYN;
    private String propertyCode;
    private String counterWiseBilling;
    private String delayedSettlement;
    private String billPrinterPort;
    private String advOrderPrinterPort;
    private String vatNo;
    private String serviceTaxNo;
    private String printVatNo;
    private String printServiceTaxNo;

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getServiceTaxNo() {
        return serviceTaxNo;
    }

    public void setServiceTaxNo(String serviceTaxNo) {
        this.serviceTaxNo = serviceTaxNo;
    }

    public String getPrintServiceTaxNo() {
        return printServiceTaxNo;
    }

    public void setPrintServiceTaxNo(String printServiceTaxNo) {
        this.printServiceTaxNo = printServiceTaxNo;
    }

    public String getPrintVatNo() {
        return printVatNo;
    }

    public void setPrintVatNo(String printVatNo) {
        this.printVatNo = printVatNo;
    }
 

    public String getPOSCode()
    {
        return POSCode;
    }

    public void setPOSCode(String POSCode)
    {
        this.POSCode = POSCode;
    }

    public String getPOSName()
    {
        return POSName;
    }

    public void setPOSName(String POSName)
    {
        this.POSName = POSName;
    }

    public String getPOSType()
    {
        return POSType;
    }

    public void setPOSType(String POSType)
    {
        this.POSType = POSType;
    }

    public String getDebitCardYN()
    {
        return debitCardYN;
    }

    public void setDebitCardYN(String debitCardYN)
    {
        this.debitCardYN = debitCardYN;
    }

    public String getPropertyCode()
    {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode)
    {
        this.propertyCode = propertyCode;
    }

    public String getCounterWiseBilling()
    {
        return counterWiseBilling;
    }

    public void setCounterWiseBilling(String counterWiseBilling)
    {
        this.counterWiseBilling = counterWiseBilling;
    }

    public String getDelayedSettlement()
    {
        return delayedSettlement;
    }

    public void setDelayedSettlement(String delayedSettlement)
    {
        this.delayedSettlement = delayedSettlement;
    }

    public String getBillPrinterPort()
    {
        return billPrinterPort;
    }

    public void setBillPrinterPort(String billPrinterPort)
    {
        this.billPrinterPort = billPrinterPort;
    }

    public String getAdvOrderPrinterPort()
    {
        return advOrderPrinterPort;
    }

    public void setAdvOrderPrinterPort(String advOrderPrinterPort)
    {
        this.advOrderPrinterPort = advOrderPrinterPort;
    }

}
