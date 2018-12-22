/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.License.controller;

/**
 *
 * @author Sanguine
 */
public class clsClientDetails
{

    private String strClientCode;
    private String strClientName;    
    private String strOutletName;
    private String dteInstallationDate;
    private String dteExpiryDate;
    private String strPOSVersion;//Enterprise/Lite
    private String intMAXSPOSTerminal;//No. of POS Machines
    private String intMAXAPOSTerminals;//No. of APOS Devices
    private clsSMSPackDtl objSMSPackDtl;//client SMS pack details
    private String strComments;

    private String strContactPerson;
    private String strContactNo;
    private String strEmailId;
    private String strBillRegeneration;
    private String strModuleNames;
    private String strClientPassword;
    

    public clsClientDetails(String id, String Client_Name,String outletName, String installDate, String expiryDate, String posVersion, String intMAXTerminal, clsSMSPackDtl smsPackDtl, String intMAXAPOSTerminals, String comments,
	    String strContactPerson, String strContactNo, String strEmailId, String billRegeneration,String clientPassword)
    {

	this.strClientCode = id;
	this.strClientName = Client_Name;
	this.dteInstallationDate = installDate;
	this.dteExpiryDate = expiryDate;
	this.strPOSVersion = posVersion;
	this.intMAXSPOSTerminal = intMAXTerminal;
	this.intMAXAPOSTerminals = intMAXAPOSTerminals;
	this.objSMSPackDtl = smsPackDtl;
	this.strComments = comments;

	this.strContactPerson = strContactPerson;
	this.strContactNo = strContactNo;
	this.strEmailId = strEmailId;
	this.strBillRegeneration = billRegeneration;
	this.strOutletName=outletName;
	this.strClientPassword=clientPassword;

    }

    public String getStrClientCode()
    {
	return strClientCode;
    }

    public void setStrClientCode(String strClientCode)
    {
	this.strClientCode = strClientCode;
    }

    public String getStrClientName()
    {
	return strClientName;
    }

    public void setStrClientName(String strClientName)
    {
	this.strClientName = strClientName;
    }

    public String getDteInstallationDate()
    {
	return dteInstallationDate;
    }

    public void setDteInstallationDate(String dteInstallationDate)
    {
	this.dteInstallationDate = dteInstallationDate;
    }

    public String getDteExpiryDate()
    {
	return dteExpiryDate;
    }

    public void setDteExpiryDate(String dteExpiryDate)
    {
	this.dteExpiryDate = dteExpiryDate;
    }

    public String getStrPOSVersion()
    {
	return strPOSVersion;
    }

    public void setStrPOSVersion(String strPOSVersion)
    {
	this.strPOSVersion = strPOSVersion;
    }

    public String getIntMAXSPOSTerminal()
    {
	return intMAXSPOSTerminal;
    }

    public void setIntMAXSPOSTerminal(String intMAXSPOSTerminal)
    {
	this.intMAXSPOSTerminal = intMAXSPOSTerminal;
    }

    public String getIntMAXAPOSTerminals()
    {
	return intMAXAPOSTerminals;
    }

    public void setIntMAXAPOSTerminals(String intMAXAPOSTerminals)
    {
	this.intMAXAPOSTerminals = intMAXAPOSTerminals;
    }

    public clsSMSPackDtl getObjSMSPackDtl()
    {
	return objSMSPackDtl;
    }

    public void setObjSMSPackDtl(clsSMSPackDtl objSMSPackDtl)
    {
	this.objSMSPackDtl = objSMSPackDtl;
    }

    public String getStrComments()
    {
	return strComments;
    }

    public void setStrComments(String strComments)
    {
	this.strComments = strComments;
    }

    public String getStrContactPerson()
    {
	return strContactPerson;
    }

    public void setStrContactPerson(String strContactPerson)
    {
	this.strContactPerson = strContactPerson;
    }

    public String getStrContactNo()
    {
	return strContactNo;
    }

    public void setStrContactNo(String strContactNo)
    {
	this.strContactNo = strContactNo;
    }

    public String getStrEmailId()
    {
	return strEmailId;
    }

    public void setStrEmailId(String strEmailId)
    {
	this.strEmailId = strEmailId;
    }

    public String getStrBillRegeneration()
    {
	return strBillRegeneration;
    }

    public void setStrBillRegeneration(String strBillRegeneration)
    {
	this.strBillRegeneration = strBillRegeneration;
    }

    public String getStrOutletName()
    {
	return strOutletName;
    }

    public void setStrOutletName(String strOutletName)
    {
	this.strOutletName = strOutletName;
    }

    public String getStrModuleNames()
    {
	return strModuleNames;
    }

    public void setStrModuleNames(String strModuleNames)
    {
	this.strModuleNames = strModuleNames;
    }

    public String getStrClientPassword()
    {
	return strClientPassword;
    }

    public void setStrClientPassword(String strClientPassword)
    {
	this.strClientPassword = strClientPassword;
    }

  

}
