/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSReport.controller;

/**
 *
 * @author ajjim
 */
public class clsGroupSubGroupItemBean
{

    private String strItemName;

    private String strSubGroupName;

    private String strGroupName;

    private String strGroupCode;

    private double dblQuantity;

    private double dblAmount;

    private double dblSubTotal;

    private double dblNetTotal;

    private double dblDisAmt;

    private String strItemCode;

    private String strPOSName;
    
    private double dblTaxAmt;

    public double getDblSubTotal()
    {
        return dblSubTotal;
    }

    public void setDblSubTotal(double dblSubTotal)
    {
        this.dblSubTotal = dblSubTotal;
    }

    public double getDblDisAmt()
    {
        return dblDisAmt;
    }

    public void setDblDisAmt(double dblDisAmt)
    {
        this.dblDisAmt = dblDisAmt;
    }

    public clsGroupSubGroupItemBean()
    {
    }

    public String getStrPOSName()
    {
        return strPOSName;
    }

    public void setStrPOSName(String strPOSName)
    {
        this.strPOSName = strPOSName;
    }

    public String getStrItemName()
    {
        return strItemName;
    }

    public void setStrItemName(String strItemName)
    {
        this.strItemName = strItemName;
    }

    public String getStrSubGroupName()
    {
        return strSubGroupName;
    }

    public void setStrSubGroupName(String strSubGroupName)
    {
        this.strSubGroupName = strSubGroupName;
    }

    public String getStrGroupName()
    {
        return strGroupName;
    }

    public void setStrGroupName(String strGroupName)
    {
        this.strGroupName = strGroupName;
    }

    public double getDblQuantity()
    {
        return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity)
    {
        this.dblQuantity = dblQuantity;
    }

    public double getDblAmount()
    {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount)
    {
        this.dblAmount = dblAmount;
    }

    public String getStrItemCode()
    {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode)
    {
        this.strItemCode = strItemCode;
    }

    public String getStrGroupCode()
    {
        return strGroupCode;
    }

    public void setStrGroupCode(String strGroupCode)
    {
        this.strGroupCode = strGroupCode;
    }

    public double getDblNetTotal()
    {
        return dblNetTotal;
    }

    public void setDblNetTotal(double dblNetTotal)
    {
        this.dblNetTotal = dblNetTotal;
    }
    
    public double getDblTaxAmt()
    {
	return dblTaxAmt;
    }

    public void setDblTaxAmt(double dblTaxAmt)
    {
	this.dblTaxAmt = dblTaxAmt;
    }

}
