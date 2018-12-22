package com.POSTransaction.controller;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.view.frmMakeKOT;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class clsSaveAndPrintKOT implements Runnable
{

    private final List<clsMakeKotItemDtl> objListKOTItemDtl;
    private boolean isItemInserted;
    private String tableNo;
    private String KOTNO;
    private frmMakeKOT objMakeKOT;
    private String nCKOT_YN;
    private String resonCode, cmsMemberCode, cmsMemberName;
    private int pax = 0;
    private String debitCardNo;
    private double cardBalance = 0;
    private double taxAmt = 0;
    clsUtility objUtility = new clsUtility();
    private List<String> arrListHomeDelDetails;
    private String homeDeliveryKOT;

    public clsSaveAndPrintKOT(List<clsMakeKotItemDtl> objListKOTItemDtl, String tableNo, String KOTNO, frmMakeKOT obj, String nCKOT_YN, String reasonCode, String cmsMemberCode, String cmsMemberName, int pax, String debitCardNo, double cardBalance, double taxAmt, List<String> arrListHomeDelDetails, String homeDeliveryKOT)
    {
	this.isItemInserted = false;
	this.tableNo = tableNo;
	this.KOTNO = KOTNO;
	this.objListKOTItemDtl = objListKOTItemDtl;
	objMakeKOT = obj;
	this.nCKOT_YN = nCKOT_YN;
	this.resonCode = reasonCode;
	this.cmsMemberCode = cmsMemberCode;
	this.cmsMemberName = cmsMemberName;
	this.pax = pax;
	this.debitCardNo = debitCardNo;
	this.cardBalance = cardBalance;
	this.taxAmt = taxAmt;
	this.arrListHomeDelDetails = arrListHomeDelDetails;
	this.homeDeliveryKOT = homeDeliveryKOT;
    }

    @Override
    public void run()
    {

	if (!isItemInserted)
	{
	    funInsertDataInTempTable();
	}
	if (isItemInserted && "Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
	{
	    funUpdateKOT(tableNo, KOTNO);
	}
	else if (isItemInserted && "Jasper".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
	{
	    funUpdateKOT(tableNo, KOTNO);
	}

	
	if (clsGlobalVarClass.gFireCommunication)
	{
	    //don't fire and print the KOT
	}
	else
	{
	    if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
	    {
		clsKOTGeneration objGeneration = new clsKOTGeneration();
		objGeneration.funKOTGeneration(tableNo, KOTNO, "", "", "Dina", "Y");
	    }
	    else
	    {
		clsKOTGeneration objGeneration = new clsKOTGeneration();
		objGeneration.funKOTGeneration(tableNo, KOTNO, "", "", "Dina", "Y");
	    }
	}
    }

    private void funInsertDataInTempTable()
    {

	try
	{
	    String homeDelivery = "No", customerName = "", customerCode = clsGlobalVarClass.gCustomerCode, rewardId = "";

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
	    {
		if (objMakeKOT.getCustomerRewards() != null && objMakeKOT.getCustomerRewards().getStrRewardId() != null)
		{
		    rewardId = objMakeKOT.getCustomerRewards().getStrRewardId();
		}
	    }

	    
	    if (clsGlobalVarClass.gFireCommunication)
	    {
		
	    }
	    String kotToBillNote = "";
	    if (objMakeKOT.getKOTToBillNote() != null)
	    {
		kotToBillNote = objMakeKOT.getKOTToBillNote().trim();
	    }

	    if (homeDeliveryKOT.equals("Y"))
	    {
		homeDelivery = "No";
		if (arrListHomeDelDetails.get(3).toString().equals("HomeDelivery"))
		{
		    homeDelivery = "Yes";
		}
		customerName = "";
	    }
	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		customerCode = cmsMemberCode;
		customerName = cmsMemberName;
	    }

	    String takeAway = "No";
	    if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
	    {
		takeAway = "Yes";
	    }

	    String counterCode = "NA";
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		counterCode = clsGlobalVarClass.gCounterCode;
	    }

	    String tableNo = "";
	    String KOTNo = "";
	    double KOTAmt = 0;
	    String delBoyCode = "";
	    if (homeDelivery == "Yes")
	    {
		delBoyCode = arrListHomeDelDetails.get(4);
	    }
	    String insertQuery = "insert into tblitemrtemp (strSerialNo,strTableNo,strCardNo,dblRedeemAmt,strPosCode,strItemCode"
		    + ",strHomeDelivery,strCustomerCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo"
		    + ",strKOTNo,intPaxNo,strPrintYN,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strTakeAwayYesNo,strNCKotYN,strCustomerName,strCounterCode"
		    + ",dblRate,dblTaxAmt,strDelBoyCode,strCRMRewardId,dblFiredQty,dblPrintQty,strBillNote) "
		    + " values ";
	    for (clsMakeKotItemDtl listItemDtl : objListKOTItemDtl)
	    {

		tableNo = listItemDtl.getTableNo();
		KOTNo = listItemDtl.getKOTNo();
		
		double firedQty=0.00,printQty=0.00;

		insertQuery += "('" + listItemDtl.getSequenceNo() + "','" + listItemDtl.getTableNo() + "'"
			+ ",'" + debitCardNo + "','" + cardBalance + "','" + clsGlobalVarClass.gPOSCode + "'"
			+ ",'" + listItemDtl.getItemCode() + "','" + homeDelivery + "','" + customerCode + "'"
			+ ",'" + listItemDtl.getItemName() + "','" + listItemDtl.getQty() + "','" + listItemDtl.getAmt() + "'"
			+ ",'" + listItemDtl.getWaiterNo() + "','" + listItemDtl.getKOTNo() + "'"
			+ ",'" + listItemDtl.getPaxNo() + "','" + listItemDtl.getPrintYN() + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			+ ",'" + takeAway + "','" + nCKOT_YN + "','" + customerName + "','" + counterCode + "'"
			+ ",'" + listItemDtl.getItemRate() + "'," + taxAmt + ",'" + delBoyCode + "','" + rewardId + "'"
			+ ",'" + firedQty + "','" + printQty + "','" + kotToBillNote + "'),";
		KOTAmt += listItemDtl.getAmt();
	    }
	    StringBuilder sb = new StringBuilder(insertQuery);
	    int index = sb.lastIndexOf(",");
	    insertQuery = sb.delete(index, sb.length()).toString();
	    //System.out.println(insertQuery);
	    clsGlobalVarClass.dbMysql.execute(insertQuery);

	    String sql = "insert into tblkottaxdtl "
		    + "values ('" + tableNo + "','" + KOTNo + "'," + KOTAmt + "," + taxAmt + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    isItemInserted = true;
	    customerCode = "";
	    customerName = "";
	    cmsMemberCode = "";
	    cmsMemberName = "";

	    if ("Y".equals(nCKOT_YN))
	    {
		String sqlNCKOT = "insert into tblnonchargablekot (strTableNo,strItemCode,dblQuantity,dblRate,strKOTNo,"
			+ "strEligibleForVoid,strClientCode,strReasonCode,strRemark,dteNCKOTDate,strUserCreated,strUserEdited"
			+ ",strPOSCode,strItemName,strBillNote ) "
			+ " values ";
		for (clsMakeKotItemDtl listItemDtl : objListKOTItemDtl)
		{
		    double rate = listItemDtl.getAmt() / listItemDtl.getQty();
		    sqlNCKOT += "('" + listItemDtl.getTableNo() + "','" + listItemDtl.getItemCode() + "',"
			    + "'" + listItemDtl.getQty() + "','" + rate + "','" + listItemDtl.getKOTNo() + "',"
			    + "'Y','" + clsGlobalVarClass.gClientCode + "','" + resonCode + "'"
			    + ",'" + objUtility.funCheckSpecialCharacters(clsGlobalVarClass.gKeyboardValue) + "','" + objUtility.funGetPOSDateForTransaction() + "',"
			    + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSCode + "','"+listItemDtl.getItemName()+"','"+kotToBillNote+"'), ";
		}
		StringBuilder sb1 = new StringBuilder(sqlNCKOT);
		int index1 = sb1.lastIndexOf(",");
		sqlNCKOT = sb1.delete(index1, sb1.length()).toString();
		clsGlobalVarClass.dbMysql.execute(sqlNCKOT);

		sql = "delete from tblkottaxdtl where strTableNo='" + tableNo + "' and strKOTNo='" + KOTNo + "' ";
		clsGlobalVarClass.dbMysql.execute(sql);

		//send NC KOT MSG
		sql = "select a.strSendSMSYN,a.longMobileNo "
			+ "from tblsmssetup a "
			+ "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			+ "and a.strTransactionName='NCKOT' "
			+ "and a.strSendSMSYN='Y'; ";
		ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsSendSMS.next())
		{
		    String mobileNo = rsSendSMS.getString(2);//mobileNo

		    funSendNCKOTSMS(KOTNo, mobileNo);

		}
		rsSendSMS.close();
	    }

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(tableNo);	    	    
	    objMakeKOT.setKOTToBillNote();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, "Error In Print KOT-" + e.getMessage());
	}
    }

    private void funUpdateKOT(String tempTableNO, String KOTNo)
    {
	//private synchronized void funUpdateKOT(String tempTableNO, String KOTNo) {
	try
	{

	    String customerCode = clsGlobalVarClass.gCustomerCode;
	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		customerCode = cmsMemberCode;
	    }
	    String sql_update = "update tblitemrtemp set strPrintYN='Y',dteDateCreated='" + objUtility.funGetPOSDateForTransaction() + "' "
		    + "where strKOTNo='" + KOTNo + "' and strTableNo='" + tableNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql_update);
	    if (homeDeliveryKOT.equals("Y"))
	    {
		String sql = "update tblitemrtemp set strHomeDelivery='Yes',strCustomerCode='" + customerCode + "' "
			+ "where strTableNo='" + tempTableNO + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    else
	    {
		String sql = "update tblitemrtemp set strHomeDelivery='No',strCustomerCode='" + customerCode + "' "
			+ "where strTableNo='" + tempTableNO + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    String sql = "update tbldebitcardtabletemp set strPrintYN='Y' where strTableNo='" + tempTableNO + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    // Update table status to normal if kot is NCKOT and status of table is normal    
	    String status = "";
	    if ("Y".equals(nCKOT_YN))
	    {
		status = "Normal";
		sql = "update tbltablemaster set strStatus='Normal' "
			+ " where strTableNo='" + tempTableNO + "' and strStatus='Normal' ";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    else
	    {
		status = "Occupied";
		sql = "update tbltablemaster set strStatus='Occupied' where strTableNo='" + tempTableNO + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	    sql = "update tbltablemaster set intPaxNo='" + pax + "' where strTableNo='" + tempTableNO + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    objMakeKOT.funResetFields();
	    if (objMakeKOT != null)
	    {
		objMakeKOT = null;
	    }

	    //Update Table Status to Inresto POS
	    if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
	    {
		String sqlTableName = "select strTableName from tbltablemaster where strTableNo='" + tableNo + "'";
		ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableName);
		if (rsTableName.next())
		{
		    objUtility.funUpdateTableStatusToInrestoApp(tableNo, rsTableName.getString(1).trim(), status);
		}
		rsTableName.close();
	    }

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(tableNo, KOTNo);
	}
	catch (Exception e)
	{
	    new clsUtility().funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    System.gc();
	}
    }

    private void funSendNCKOTSMS(String kotNo, String mobileNo)
    {

	try
	{
	    clsUtility2 objUtility2 = new clsUtility2();
	    StringBuilder mainSMSBuilder = new StringBuilder();
	    DecimalFormat decimalFormat = new DecimalFormat("#.##");

	    mainSMSBuilder.append("NCKOT");
	    mainSMSBuilder.append(" ,KOT_No:" + kotNo);
	    mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
	    mainSMSBuilder.append(" ,User:" + clsGlobalVarClass.gUserCode);

	    String sql = "select a.strKOTNo,c.strTableName,sum(a.dblQuantity*a.dblRate) "
		    + ",TIME_FORMAT(time(a.dteNCKOTDate),'%h:%i')ncTime,b.strReasonName,a.strRemark   "
		    + "from tblnonchargablekot a "
		    + "left outer join tblreasonmaster b on a.strReasonCode=b.strReasonCode  "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo "
		    + "where a.strKOTNo='" + kotNo + "'  "
		    + "group by a.strKOTNo";
	    ResultSet rsModBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsModBill.next())
	    {
		mainSMSBuilder.append(" ,Time:" + rsModBill.getString(4));
		mainSMSBuilder.append(" ,Amount:" + decimalFormat.format(Math.rint(rsModBill.getDouble(3))));
		mainSMSBuilder.append(" ,Reason:" + rsModBill.getString(5));
		mainSMSBuilder.append(" ,Remarks:" + rsModBill.getString(6));
	    }
	    rsModBill.close();

	    ArrayList<String> mobileNoList = new ArrayList<>();
	    String mobNos[] = mobileNo.split(",");
	    for (String mn : mobNos)
	    {
		mobileNoList.add(mn);
	    }
	    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
	    objSMSSender.start();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	}
    }
}
