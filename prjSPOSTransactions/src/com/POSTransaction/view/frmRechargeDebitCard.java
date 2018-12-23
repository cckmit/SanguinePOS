/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmOkPopUp;
import com.POSPrinting.Utility.clsPrintingUtility;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmRechargeDebitCard extends javax.swing.JFrame
{

    private String sql, allowRecharge, authenticateMemberCard, debitMemberCode;
    private double minRechargeAmt, maxRechargeAmt;
    private Map<String, String> hmRechargeSettlementOptions = new HashMap<String, String>();
    private Map<String, clsRechargeSettlementOptions> hmSettlementOptions = new HashMap<String, clsRechargeSettlementOptions>();
    private String cardString, cardTypeCode;
    private JButton[] settlementArray = new JButton[4];
    private Vector vSettlementDesc;
    private int settlementNavigate;
    private String settlementType, settlementCode;
    private clsUtility objUtility;
    private clsUtility2 objUtility2;

    /**
     * This method is used to initialize frmRechargeDebitCard
     */
    public frmRechargeDebitCard()
    {
	initComponents();
	funSetShortCutKeys();
	try
	{
	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    Date date1 = new Date();
		    String newstr = String.format("%tr", date1);
		    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
		    lblDate.setText(dateAndTime);
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();
	    debitMemberCode = "";
	    cardString = "";
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    txtCardNo.requestFocus();
	    btnMember.setVisible(false);
	    lblMemberCode.setVisible(false);
	    lblMemberName.setVisible(false);
	    cardTypeCode = "";
	    settlementType = "";
	    settlementCode = "";

	    settlementArray[0] = btnSettle1;
	    settlementArray[1] = btnSettle2;
	    settlementArray[2] = btnSettle3;
	    settlementArray[3] = btnSettle4;

	    int cntSettlement = 0;
	    for (cntSettlement = 0; cntSettlement < settlementArray.length; cntSettlement++)
	    {
		settlementArray[cntSettlement].setVisible(false);
	    }
	    vSettlementDesc = new java.util.Vector();
	    btnPrevSettlementMode.setEnabled(false);
	    settlementNavigate = 0;
	    lblBalWithoutSettle.setVisible(false);

	    objUtility = new clsUtility();
	    objUtility2 = new clsUtility2();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private int funFillSettlementArray(String cardTCode) throws Exception
    {
	vSettlementDesc.clear();
	hmRechargeSettlementOptions.clear();
	sql = "select b.strSettelmentDesc,b.strSettelmentType,b.strSettelmentCode "
		+ " from tbldebitcardsettlementdtl a,tblsettelmenthd b "
		+ " where a.strSettlementCode=b.strSettelmentCode and a.strCardTypeCode='" + cardTCode + "';";
	ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsSettlement.next())
	{
	    String settleMent = rsSettlement.getString(2) + "#" + rsSettlement.getString(3); // Type#Code
	    hmRechargeSettlementOptions.put(rsSettlement.getString(1), settleMent);
	    vSettlementDesc.add(rsSettlement.getString(1));
	}
	rsSettlement.close();
	if (vSettlementDesc.size() <= 4)
	{
	    btnNextSettlementMode.setEnabled(false);
	}
	return 1;
    }

    private void funPrevSettlementMode()
    {
	try
	{
	    settlementNavigate--;
	    if (settlementNavigate == 0)
	    {
		btnPrevSettlementMode.setEnabled(false);
		btnNextSettlementMode.setEnabled(true);
		funFillSettlementButtons(0, vSettlementDesc.size());
	    }
	    else
	    {
		int startIndex = (settlementNavigate * 4);
		int endIndex = startIndex + 4;
		int disableNext = vSettlementDesc.size() / startIndex;
		funFillSettlementButtons(startIndex, endIndex);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funNextSettlementMode()
    {
	try
	{
	    settlementNavigate++;
	    int startIndex = (settlementNavigate * 4);
	    int endIndex = startIndex + 4;
	    int disableNext = vSettlementDesc.size() / startIndex;
	    funFillSettlementButtons(startIndex, endIndex);
	    btnPrevSettlementMode.setEnabled(true);
	    if (disableNext == settlementNavigate)
	    {
		btnNextSettlementMode.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /*
     * Check the Which Settlement Mode Select By User
     */
    private void funSettlementBtnClick(String settlementDesc)
    {
	try
	{
	    lblSelectedSettlementMode.setText(settlementDesc);
	    String settleInfo = hmRechargeSettlementOptions.get(settlementDesc);
	    settlementCode = settleInfo.split("#")[1];
	    settlementType = settleInfo.split("#")[0];
	    lblSelectedSettlementMode.setText(settlementDesc);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSetShortCutKeys()
    {
	btnCancel.setMnemonic('c');
	btnNew1.setMnemonic('s');
	btnReset.setMnemonic('r');
    }

    public void funFillSettlementButtons(int startIndex, int endIndex)
    {
	int cntArrayIndex = 0;
	for (int k = 0; k < 4; k++)
	{
	    settlementArray[k].setVisible(false);
	    settlementArray[k].setText("");
	}
	for (int cntSettlement = startIndex; cntSettlement < endIndex; cntSettlement++)
	{
	    if (cntSettlement == vSettlementDesc.size())
	    {
		break;
	    }
	    if (cntArrayIndex < 4)
	    {
		settlementArray[cntArrayIndex].setText(vSettlementDesc.elementAt(cntSettlement).toString());
		settlementArray[cntArrayIndex].setVisible(true);
		cntArrayIndex++;
	    }
	}
    }

    /**
     * This method is used to set debit card data
     *
     * @param cardString
     */
    private void funSetCardData(String cardString)
    {
	try
	{
	    funResetFields();
	    this.cardString = cardString;
	    String sqlCardMaster = "SELECT a.dblRedeemAmt,a.strCardTypeCode,b.strCustomerName,a.strCardNo,c.dblDepositAmt "
		    + "FROM tbldebitcardmaster a "
		    + "left outer join tbldebitcardtype c on a.strCardTypeCode=c.strCardTypeCode "
		    + "LEFT OUTER JOIN tblcustomermaster b ON a.strCustomerCode=b.strCustomerCode "
		    + "WHERE a.strCardString='"+cardString+"' ";
	    ResultSet rsCardMaster = clsGlobalVarClass.dbMysql.executeResultSet(sqlCardMaster);
	    if (rsCardMaster.next())
	    {
		
		//rsCardMaster.getDouble(5) Deposite amt no need to deduct deposite from redeepmtion.
		double debitCardBalance = rsCardMaster.getDouble(1);
		
		
		lblBalWithoutSettle.setText(String.valueOf(debitCardBalance));
		cardTypeCode = rsCardMaster.getString(2);
		lblCustomerName.setText(rsCardMaster.getString(3));
		txtCardNo.setText(rsCardMaster.getString(4));

		// Open KOTs
		debitCardBalance -= objUtility.funGetKOTAmtOnTable(rsCardMaster.getString(4).trim());

		// Open Bills    
		sql = "select sum(dblGrandTotal) "
			+ " from tblbillhd where strCardNo='" + rsCardMaster.getString(4) + "' "
			+ " and strBillNo not in (select strBillNo from tblbillsettlementdtl) "
			+ " group by strBillNo ";
		ResultSet rsOpenBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsOpenBills.next())
		{
		    debitCardBalance -= rsOpenBills.getDouble(1);
		}
		rsOpenBills.close();

		txtCFBalance.setText(String.valueOf(Math.rint(debitCardBalance)));
	    }
	    rsCardMaster.close();

	    String sqlCardType = "select * from tbldebitcardtype where strCardTypeCode='" + cardTypeCode + "'";
	    ResultSet rsCardType = clsGlobalVarClass.dbMysql.executeResultSet(sqlCardType);
	    if (rsCardType.next())
	    {
		txtValidUpTo.setText(rsCardType.getString(15));
		cardTypeCode = rsCardType.getString(1);
		txtCardTypeName.setText(rsCardType.getString(2));
		txtDeposit.setText(rsCardType.getString(21));
		txtMinCharges.setText(rsCardType.getString(22));
		minRechargeAmt = rsCardType.getDouble(19);
		maxRechargeAmt = rsCardType.getDouble(20);

		if ((rsCardType.getString(7).equals("Y")))
		{
		    chkRedeemable.setSelected(true);
		}
		else
		{
		    chkRedeemable.setSelected(false);
		}
		if ((rsCardType.getString(5).equals("Y")))
		{
		    chkComplementary.setSelected(true);
		}
		else
		{
		    chkComplementary.setSelected(false);
		}
		if (rsCardType.getString(12).toString().equalsIgnoreCase("Y"))
		{
		    allowRecharge = "Y";
		}
		else
		{
		    allowRecharge = "N";
		}
		authenticateMemberCard = rsCardType.getString(40).toString();
		if (authenticateMemberCard.equals("Y"))
		{
		    btnMember.setVisible(true);
		    lblMemberCode.setVisible(true);
		    lblMemberName.setVisible(true);
		}

		funFillSettlementArray(cardTypeCode);
		funFillSettlementButtons(0, 4);
	    }
	    rsCardType.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to post DC recharge amount to RMS
     *
     * @param extCode
     * @return boolean
     */
    private boolean funPostDCRechargeAmtToRMS(String extCode)
    {
	boolean flgRecharge = false;
	Connection conRMS = null;
	try
	{
	    Date dt = new Date();
	    String date = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
	    String time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	    String currentDate = date + " " + time;
	    String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    conRMS = DriverManager.getConnection(rmsConURL);
	    conRMS.setAutoCommit(false);
	    Statement st = conRMS.createStatement();
	    String remarks = "Recharge From CRM by " + clsGlobalVarClass.gUserCode;
	    sql = "insert into tblRechargeDebitCard(strRechargeNo,strDebitCardString,dblRechargeAmount"
		    + ",strCustomerCode,dtEntryDate,strRemarks) "
		    + "values('" + lblRechargeNo.getText() + "','" + txtCardNo.getText().trim()
		    + "'," + txtAmount.getText() + ",'" + extCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + remarks + "')";
	    System.out.println(sql);
	    int recharge = st.executeUpdate(sql);
	    if (recharge > 0)
	    {
		sql = "select dblLastNo from tblInternal where strTransactionType='JV'";
		ResultSet rsLastNo = st.executeQuery(sql);
		rsLastNo.next();
		double voucherNo = Double.parseDouble(rsLastNo.getString(1));
		voucherNo++;
		StringBuilder sb = new StringBuilder(voucherNo + "");
		sb = sb.delete(sb.indexOf("."), sb.length());
		long vNo = Integer.parseInt(sb.toString());
		String rechargeAmount = txtAmount.getText().trim();
		sb = new StringBuilder(rechargeAmount + "");
		sb = sb.delete(sb.indexOf("."), sb.length());
		int amount = Integer.parseInt(sb.toString());

		sql = "insert into tblJvHd(intVochNo,dteVochDate,strNarration,dblGrandTotal,strUser"
			+ ",strTransactionType,dteEntryDate,dteModifiedDate,strUserModified,strPointType) "
			+ "values(" + vNo + ",'" + currentDate + "','" + remarks + "','" + amount + "','HFP',"
			+ "'JV','" + currentDate + "','" + currentDate + "','HFP','RC')";
		st.executeUpdate(sql);

		sql = "insert into tblJvDtl(intVochNo,dteVochDate,strAccountCode,strSubCode,dblDrAmount,dblCrAmount) "
			+ "values(" + vNo + ",'" + currentDate + "','600-001-01',''," + amount + ",0)";
		st.executeUpdate(sql);

		sql = "insert into tblJvDtl(intVochNo,dteVochDate,strAccountCode,strSubCode,dblDrAmount,dblCrAmount) "
			+ "values(" + vNo + ",'" + currentDate + "','002-001-01','" + extCode + "',0," + amount + ")";
		st.executeUpdate(sql);

		sql = "update tblInternal set dblLastNo=" + vNo + " where strTransactionType='JV'";
		st.executeUpdate(sql);
		flgRecharge = true;
	    }
	    conRMS.commit();
	}
	catch (Exception e)
	{
	    try
	    {
		conRMS.rollback();
	    }
	    catch (Exception ex)
	    {
	    }
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		conRMS.close();
	    }
	    catch (Exception ex)
	    {
	    }
	    return flgRecharge;
	}
    }

    /**
     * This method is used to recharge debit card
     */
    private void funRechargeDebitCard()
    {
	try
	{
	    if (!clsGlobalVarClass.validateEmpty(txtCardNo.getText()))
	    {
		new frmOkPopUp(this, "Swipe the Card", "", 1).setVisible(true);
		txtCardNo.requestFocus();
	    }
	    else if (allowRecharge.equalsIgnoreCase("N"))
	    {
		new frmOkPopUp(this, "This Card Is Not Rechargable", "Error", 0).setVisible(true);
	    }
	    else if (!clsGlobalVarClass.validateEmpty(txtAmount.getText()))
	    {
		new frmOkPopUp(this, "Please Enter Amount To Recharge", "Error", 0).setVisible(true);
	    }
	    else if (cmbOperation.getSelectedItem().toString().equalsIgnoreCase("Recharge"))
	    {
		double amount = Double.parseDouble(txtAmount.getText());
		if (amount < minRechargeAmt || amount > maxRechargeAmt)
		{
		    JOptionPane.showMessageDialog(this, "Minimum Recharge Amount is:" + minRechargeAmt + " and Maximum Recharge Amount is:" + maxRechargeAmt);
		    return;
		}

		String redeemable = "N", complementary = "N";
		if (chkRedeemable.isSelected() == true)
		{
		    redeemable = "Y";
		}
		if (chkComplementary.isSelected() == true)
		{
		    complementary = "Y";
		}

		String recahrgeNoToInsert = objUtility2.funGetRechargeNo();
		String redeemNoToInsert = objUtility2.funGetRedeemNo();
		String rechargeSlipNoToInsert = objUtility2.funGetRechargeSlipNo();

		
		
                //System.out.println("recahrgeNoToInsert"+recahrgeNoToInsert+"    redeemNoToInsert"+redeemNoToInsert+"    rechergeSlipNoToInsert"+rechargeSlipNoToInsert);
		//double cfBalance = Double.parseDouble(txtCFBalance.getText());
		double cfBalance = Double.parseDouble(lblBalWithoutSettle.getText());
		double redeemableAmount = cfBalance + amount;
		String remarks = "Walkin Recharge By " + clsGlobalVarClass.gUserCode;
		String memberCodeForRecharge = lblMemberCode.getText();

		//clsGlobalVarClass.dbMysql.funStartTransaction();
		sql = "insert into tbldebitcardrecharge (intRechargeNo,intRedeemNo,strCardTypeCode,strCardString,"
			+ "strRedeemable,strComplementary,dblRechargeAmount,strUserCreated,dteDateCreated,"
			+ "strPOSCode,strRemarks,strRechargeType,strClientCode,strMemberCode,strRechargeSlipNo,strCardNo) "
			+ "values ('" + recahrgeNoToInsert + "','" + redeemNoToInsert + "','" + cardTypeCode
			+ "','" + cardString + "','" + redeemable + "','" + complementary + "','" + txtAmount.getText() + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + objUtility.funGetPOSDateForTransaction() + "'"
			+ ",'" + clsGlobalVarClass.gPOSCode + "','" + remarks + "','Cash'"
			+ ",'" + clsGlobalVarClass.gClientCode + "','" + memberCodeForRecharge + "'"
			+ ",'" + rechargeSlipNoToInsert + "','" + txtCardNo.getText() + "')";
		clsGlobalVarClass.dbMysql.execute(sql);

		for (Map.Entry<String, clsRechargeSettlementOptions> entry : hmSettlementOptions.entrySet())
		{
		    sql = "insert into tbldcrechargesettlementdtl "
			    + "(strRechargeNo,strSettlementCode,dblRechargeAmt,strCardNo,strType,strClientCode,strDataPostFlag) "
			    + "values('" + recahrgeNoToInsert + "','" + entry.getValue().getSettlementCode() + "'"
			    + "," + entry.getValue().getSettlementAmt() + ",'" + entry.getValue().getCardNo() + "'"
			    + ",'" + entry.getValue().getSettlementType() + "','" + clsGlobalVarClass.gClientCode + "','N') ";
		    clsGlobalVarClass.dbMysql.execute(sql);

		    if (entry.getValue().getSettlementType().equals("Debit Card"))
		    {
			String sqlUpdate = "update tbldebitcardmaster "
				+ " set dblRedeemAmt=dblRedeemAmt-" + entry.getValue().getSettlementAmt() + " "
				+ " where strCardNo='" + entry.getValue().getCardNo() + "';";
			clsGlobalVarClass.dbMysql.execute(sqlUpdate);

			sqlUpdate = "update tbldebitcardrecharge set strTransferBalance='Y' "
				+ " where intRechargeNo='" + recahrgeNoToInsert + "';";
			clsGlobalVarClass.dbMysql.execute(sqlUpdate);
		    }
		}

		sql = "update tbldebitcardtype set strRedeemableCard='" + redeemable
			+ "',strComplementary='" + complementary + "' where strCardTypeCode='" + cardTypeCode + "'";
		clsGlobalVarClass.dbMysql.execute(sql);

		sql = "update tbldebitcardmaster set dblRedeemAmt='" + redeemableAmount
			+ "',strReachrgeRemark='" + remarks + "',strRefMemberCode='" + memberCodeForRecharge + "'"
			+ " where strCardNo='" + txtCardNo.getText() + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
		double dblTotCfBalance= Double.parseDouble(txtCFBalance.getText().trim());
		if(redeemable.equals("N")){
		    dblTotCfBalance -=Double.parseDouble(txtDeposit.getText().trim());
		}
		//funGenerateRechargeTextfile(txtAmount.getText(), txtCFBalance.getText(), rechargeSlipNoToInsert, memberCodeForRecharge, lblMemberName.getText(), txtCardTypeName.getText(), txtDeposit.getText(), txtCardNo.getText());
		funGenerateRechargeTextfile(txtAmount.getText(), String.valueOf(dblTotCfBalance), rechargeSlipNoToInsert, memberCodeForRecharge, lblMemberName.getText(), txtCardTypeName.getText(), txtDeposit.getText(), txtCardNo.getText());
		lblRechargeNo.setText(recahrgeNoToInsert);
		txtCFBalance.setText(String.valueOf(redeemableAmount));

		// Post Recharge amount from JPOS to RMS
		if (clsGlobalVarClass.gRFIDInterface.equals("Y"))
		{
		    sql = "select b.strExternalCode from tbldebitcardmaster a,tblcustomermaster b "
			    + "where a.strCustomerCode=b.strCustomerCode and a.strCardString='" + cardString + "'";
		    ResultSet rsExtCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    String extCode = "";
		    if (rsExtCode.next())
		    {
			extCode = rsExtCode.getString(1);
		    }
		    if (funPostDCRechargeAmtToRMS(extCode))
		    {
			//clsGlobalVarClass.dbMysql.funCommitTransaction();
			new frmOkPopUp(this, "Recharge Successful", "Successful", 3).setVisible(true);
		    }
		    else
		    {
			//clsGlobalVarClass.dbMysql.funRollbackTransaction();
		    }
		}
		else
		{
		    cardString = "";
		    //clsGlobalVarClass.dbMysql.funCommitTransaction();
		    new frmOkPopUp(this, "Recharge Successful", "Successful", 3).setVisible(true);
		}
		funResetFields();
	    }
	    else
	    {
		double cfBalance = Double.parseDouble(txtCFBalance.getText());
		double amount = Double.parseDouble(txtAmount.getText());

		if (amount > cfBalance)
		{
		    JOptionPane.showMessageDialog(this, "Please enter proper amount:" + amount);
		    return;
		}

		long refundNo = funGetRefundNo();
		long refundSlipNo = funGetRefundSlipNo();
		String refundNoToInsert = "RF" + String.format("%07d", refundNo);
		String refundSlipNoToInsert = "RSL" + String.format("%07d", refundSlipNo);

		double redeemableAmount = cfBalance - amount;
		sql = "update tbldebitcardmaster set dblRedeemAmt='" + redeemableAmount + "' "
			+ "where strCardNo='" + txtCardNo.getText() + "'";
		clsGlobalVarClass.dbMysql.execute(sql);

		sql = "insert into tbldebitcardrefundamt (strRefundNo,strCardTypeCode,strCardString,strCardNo,"
			+ "dblCardBalance,dblRefundAmt,strUserCreated,dteDateCreated,"
			+ "strClientCode,strDataPostFlag,strRefundSlipNo,strPOSCode) "
			+ "values ('" + refundNoToInsert + "','" + cardTypeCode + "','" + cardString + "','" + txtCardNo.getText() + "'"
			+ ",'" + txtCFBalance.getText() + "','" + txtAmount.getText() + "','" + clsGlobalVarClass.gUserCode + "','" + objUtility.funGetPOSDateForTransaction() + "'"
			+ ",'" + clsGlobalVarClass.gClientCode + "','N','" + refundSlipNoToInsert + "','" + clsGlobalVarClass.gPOSCode + "')";
		System.out.println("refundNoToInsert" + refundNoToInsert + " refundSlipNoToInsert" + refundSlipNoToInsert);
		clsGlobalVarClass.dbMysql.execute(sql);

		funGenerateRefundTextfile(txtAmount.getText(), txtCFBalance.getText(), refundSlipNoToInsert, txtCardTypeName.getText(), lblCustomerName.getText(), txtCardNo.getText());
		lblRechargeNo.setText(refundNoToInsert);
		txtCFBalance.setText(String.valueOf(redeemableAmount));
		cardString = "";
		new frmOkPopUp(this, "Refund Successfully", "Successful", 3).setVisible(true);
		funResetFields();
	    }
	}
	catch (Exception e)
	{
	    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
	clsGlobalVarClass.gDebitCardNo = null;
	lblRechargeNo.setText("");
	lblCustomerName.setText("");
	txtCardNo.setText("");
	txtValidUpTo.setText("");
	cardTypeCode = "";
	txtCardTypeName.setText("");
	txtDeposit.setText("");
	txtMinCharges.setText("");
	chkRedeemable.setSelected(false);
	chkComplementary.setSelected(false);
	txtCFBalance.setText("");
	txtAmount.setText("");
	cmbOperation.setSelectedItem("Recharge");
        //DefaultTableModel dmSettlementTable = (DefaultTableModel) tblSettlmentMode.getModel();
	//dmSettlementTable.setRowCount(0);
	lblMemberCode.setText("");
	lblMemberName.setText("");
	btnMember.setVisible(false);
	lblMemberCode.setVisible(false);
	lblMemberName.setVisible(false);
	debitMemberCode = "";
	DefaultTableModel obj = (DefaultTableModel) tblSettlement.getModel();
	obj.setRowCount(0);
	hmSettlementOptions.clear();
    }

   

    /**
     * This method is used to get refund no
     *
     * @return string
     */
    private long funGetRefundNo() throws Exception
    {
	long lastNo = 1;
	sql = "select count(dblLastNo) from tblinternal where strTransactionType='RefundNo'";
	ResultSet rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rsCustTypeCode.next();
	int cntCustType = rsCustTypeCode.getInt(1);
	rsCustTypeCode.close();
	if (cntCustType > 0)
	{
	    sql = "select dblLastNo from tblinternal where strTransactionType='RefundNo'";
	    rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCustTypeCode.next();
	    long code = rsCustTypeCode.getLong(1);
	    code = code + 1;
	    lastNo = code;

	    String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
		    + "where strTransactionType='RefundNo'";
	    clsGlobalVarClass.dbMysql.execute(updateSql);
	    rsCustTypeCode.close();
	}
	else
	{
	    lastNo = 1;
	    sql = "insert into tblinternal values('RefundNo'," + 1 + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	return lastNo;
    }

    /**
     * This method is used to get refund slip no
     *
     * @return string
     */
    private long funGetRefundSlipNo() throws Exception
    {
	long lastNo = 1;
	sql = "select count(dblLastNo) from tblinternal where strTransactionType='RefundSlipNo'";
	ResultSet rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rsCustTypeCode.next();
	int cntCustType = rsCustTypeCode.getInt(1);
	rsCustTypeCode.close();
	if (cntCustType > 0)
	{
	    sql = "select dblLastNo from tblinternal where strTransactionType='RefundSlipNo'";
	    rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCustTypeCode.next();
	    long code = rsCustTypeCode.getLong(1);
	    code = code + 1;
	    lastNo = code;

	    String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
		    + "where strTransactionType='RefundSlipNo'";
	    clsGlobalVarClass.dbMysql.execute(updateSql);
	    rsCustTypeCode.close();
	}
	else
	{
	    lastNo = 1;
	    sql = "insert into tblinternal values('RefundSlipNo'," + 1 + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	return lastNo;
    }

    /**
     * This method is used to change recharge combo
     */
    private void funRechargeComboChanged()
    {
	if (!cmbOperation.getSelectedItem().toString().equalsIgnoreCase("Recharge"))
	{
	    txtAmount.setText("");
	    //funFillPaymentModeTable();
	}
	else
	{
	    txtAmount.setText("");
	    //funFillPaymentModeTable();
	}
    }

    private void funSetMemberCardData(String cardString)
    {
	clsUtility objUtility = new clsUtility();
	try
	{
	    String memberInfo = objUtility.funAuthoriseCMSMemberForRechargeUsingCard(cardString);
	    if (!memberInfo.isEmpty())
	    {
		lblMemberCode.setText(memberInfo.split("#")[0]);
		lblMemberName.setText(memberInfo.split("#")[1]);
		debitMemberCode = memberInfo.split("#")[0];
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    objUtility = null;
	}
    }

    /**
     * This method is used to create temp folder
     *
     */
    private void funCreateTempFolder()
    {
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + "/Temp");
	if (!file.exists())
	{
	    file.mkdirs();
	}
    }

    /**
     * This method is used for create recharge slip
     *
     */
    private void funGenerateRechargeTextfile(String rechargeAmt, String cardBalance, String rechargeSlipNo, String memberRefferenceCode, String memberRefferenceName, String cardType, String depositeAmt, String cardNo)
    {
	double amount = Double.parseDouble(rechargeAmt);
	clsUtility objUtility = new clsUtility();
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    filePath += "/Temp/Temp_Recharge.txt";
	    File textFile = new File(filePath);
	    PrintWriter pw = new PrintWriter(textFile);
	    pw.println(objUtility.funPrintTextWithAlignment("Recharge Slip", 40, "Center"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSName, 40, "Center"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName, 40, "Center"));
	    pw.println(" ");
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.getCurrentDateTime(), 22, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Card Type", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardType, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Card No", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardNo, 18, "Left"));
	    pw.println(" ");

	    if (!memberRefferenceCode.isEmpty())
	    {
		pw.print(objUtility.funPrintTextWithAlignment("RefMemberCode", 16, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(memberRefferenceCode, 22, "Left"));
		pw.println(" ");
	    }

	    if (!memberRefferenceName.isEmpty())
	    {
		pw.print(objUtility.funPrintTextWithAlignment("RefMemberName", 16, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(memberRefferenceName, 22, "Left"));
		pw.println(" ");
	    }

	    pw.print(objUtility.funPrintTextWithAlignment("RechargeSlip No", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(rechargeSlipNo, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Deposite Amt", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(depositeAmt, 18, "Left"));
	    pw.println(" ");

	    if (cardBalance.isEmpty())
	    {
		cardBalance = "0.00";
	    }
	    pw.print(objUtility.funPrintTextWithAlignment("C/F Balance", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardBalance, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Recharge Amt", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(rechargeAmt, 18, "Left"));
	    pw.println(" ");

	    double totalBal = Double.parseDouble(cardBalance) + Double.parseDouble(rechargeAmt);
	    pw.print(objUtility.funPrintTextWithAlignment("Total Balance", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(totalBal), 18, "Left"));
	    pw.println(" ");

	    String amtInWords = new clsUtility().funGetAmtInWords((long) amount);
	    pw.print(objUtility.funPrintTextWithAlignment("RS." + amtInWords + "Only", 16, "Left"));
	    pw.println();
	    pw.println();
	    pw.println();
	    pw.println();
	    pw.println();
	    pw.println();

	    if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		pw.println("V");//Linux
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    pw.println("V");
		}
		else
		{
		    pw.println("m");//windows
		}
	    }

	    pw.flush();
	    pw.close();

	    objUtility.funPrintTextDocumentsToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, filePath);

	    if (clsGlobalVarClass.gShowBill)
	    {
		clsPrintingUtility objPrintingUtility = new clsPrintingUtility();

		objPrintingUtility.funShowTextFile(textFile, "Debit Card Recharge", clsGlobalVarClass.gBillPrintPrinterPort);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used for create refund slip
     *
     *
     */
    private void funGenerateRefundTextfile(String refundAmt, String cardBalance, String refundSlipNo, String cardType, String customerName, String cardNo)
    {
	double amount = Double.parseDouble(refundAmt);
	clsUtility objUtility = new clsUtility();
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    filePath += "/Temp/Temp_Refund.txt";
	    File textFile = new File(filePath);
	    PrintWriter pw = new PrintWriter(textFile);
	    pw.println(objUtility.funPrintTextWithAlignment("Refund Slip", 40, "Center"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSName, 40, "Center"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName, 40, "Center"));
	    pw.println(" ");
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 16, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.getCurrentDateTime().split(" ")[0], 22, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Card Type", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardType, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Card No", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardNo, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("RefundSlip No", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(refundSlipNo, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("C/F Balance", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardBalance, 18, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Refund Amt", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(refundAmt, 18, "Left"));
	    pw.println(" ");

	    double totalBal = Double.parseDouble(cardBalance) - Double.parseDouble(refundAmt);
	    pw.print(objUtility.funPrintTextWithAlignment("Total Balance", 20, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(totalBal), 18, "Left"));
	    pw.println(" ");

	    String amtInWords = new clsUtility().funGetAmtInWords((long) amount);
	    pw.print(objUtility.funPrintTextWithAlignment("RS." + amtInWords + "Only", 16, "Left"));
	    pw.println();
	    pw.println();
	    pw.println();
	    pw.println();
	    pw.println();

	    if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		pw.println("V");//Linux
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    pw.println("V");
		}
		else
		{
		    pw.println("m");//windows
		}
	    }
	    pw.flush();
	    pw.close();
	    for(int billPrintCount=0;billPrintCount<2;billPrintCount++)
	    {
		 objUtility.funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, filePath);
	    }
	    if (clsGlobalVarClass.gShowBill)
	    {
		clsPrintingUtility objPrintingUtility = new clsPrintingUtility();
		objPrintingUtility.funShowTextFile(textFile, "", "");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funKeyBoardNumericValue(String buttonText)
    {
	String textAmt = lblSettleAmt.getText() + buttonText;
	lblSettleAmt.setText(textAmt);
    }

    private void funBackspaceBtnPressed()
    {
	try
	{
	    String textValue = lblSettleAmt.getText();
	    StringBuilder sb = new StringBuilder(textValue);
	    sb.delete(textValue.length() - 1, textValue.length());
	    lblSettleAmt.setText(sb.toString());

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funKeyBoardEnterButtonPressed()
    {
	try
	{
	    if (Double.parseDouble(lblSettleAmt.getText()) < 0 || lblSettleAmt.getText().isEmpty())
	    {
		JOptionPane.showMessageDialog(null, "Please Enter Valid Recharge Amount!!!");
		return;
	    }

	    if (settlementType.equals("Debit Card"))
	    {
		String cardNo = "";
		new frmSwipCardPopUp(this, "frmRechargeDebitCard1").setVisible(true);

		if (null != clsGlobalVarClass.gDebitCardNo && clsGlobalVarClass.gDebitCardNo.length() > 0)
		{
		    sql = "select strCardNo,dblRedeemAmt from tbldebitcardmaster "
			    + "where strCardString='" + clsGlobalVarClass.gDebitCardNo + "' ";
		    ResultSet rsCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsCardNo.next())
		    {
			cardNo = rsCardNo.getString(1);
			if (rsCardNo.getDouble(2) < Double.parseDouble(lblSettleAmt.getText()))
			{
			    JOptionPane.showMessageDialog(null, "Insufficient Balance in Card. Card Balance is " + rsCardNo.getString(2) + " !!!");
			}
			else if (lblSelectedSettlementMode.getText().isEmpty())
			{
			    JOptionPane.showMessageDialog(null, "Please Select Settlement Mode.!!!");
			}
			else
			{
			    clsRechargeSettlementOptions objRechargeSettleOptions = new clsRechargeSettlementOptions();
			    if (hmSettlementOptions.containsKey(settlementType))
			    {
				if (chkTransferBal.isSelected())
				{
				    objRechargeSettleOptions.setSettlementCode(settlementCode);
				    objRechargeSettleOptions.setSettlementType(settlementType);
				    objRechargeSettleOptions.setSettlementDesc(lblSelectedSettlementMode.getText());
				    objRechargeSettleOptions.setCardNo(cardNo);
				    objRechargeSettleOptions.setSettlementAmt(Double.parseDouble(lblSettleAmt.getText()));
				}
				else
				{
				    objRechargeSettleOptions = hmSettlementOptions.get(settlementType);
				    objRechargeSettleOptions.setCardNo(cardNo);
				    objRechargeSettleOptions.setSettlementAmt(objRechargeSettleOptions.getSettlementAmt() + Double.parseDouble(lblSettleAmt.getText()));
				}

			    }
			    else
			    {
				objRechargeSettleOptions.setSettlementCode(settlementCode);
				objRechargeSettleOptions.setSettlementType(settlementType);
				objRechargeSettleOptions.setSettlementDesc(lblSelectedSettlementMode.getText());
				objRechargeSettleOptions.setCardNo(cardNo);
				objRechargeSettleOptions.setSettlementAmt(Double.parseDouble(lblSettleAmt.getText()));
			    }
			    hmSettlementOptions.put(settlementType, objRechargeSettleOptions);
			}
		    }
		    rsCardNo.close();
		}
	    }
	    else
	    {
		if (lblSelectedSettlementMode.getText().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Please Select Settlement Mode.!!!");
		    return;
		}
		clsRechargeSettlementOptions objRechargeSettleOptions = new clsRechargeSettlementOptions();
		if (hmSettlementOptions.containsKey(settlementType))
		{
		    objRechargeSettleOptions = hmSettlementOptions.get(settlementType);
		    objRechargeSettleOptions.setSettlementAmt(objRechargeSettleOptions.getSettlementAmt() + Double.parseDouble(lblSettleAmt.getText()));
		}
		else
		{
		    objRechargeSettleOptions.setSettlementCode(settlementCode);
		    objRechargeSettleOptions.setSettlementType(settlementType);
		    objRechargeSettleOptions.setSettlementDesc(lblSelectedSettlementMode.getText());
		    objRechargeSettleOptions.setCardNo(txtCardNo.getText());
		    objRechargeSettleOptions.setSettlementAmt(Double.parseDouble(lblSettleAmt.getText()));
		}
		hmSettlementOptions.put(settlementType, objRechargeSettleOptions);
	    }
	    funFillSettlementGrid();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillSettlementGrid() throws Exception
    {
	lblSettleAmt.setText("");
	lblSelectedSettlementMode.setText("");
	DefaultTableModel dmSettlementGrid = (DefaultTableModel) tblSettlement.getModel();
	dmSettlementGrid.setRowCount(0);

	double totalSettleAmt = 0;
	for (Map.Entry<String, clsRechargeSettlementOptions> entry : hmSettlementOptions.entrySet())
	{
	    Object[] arrObj =
	    {
		entry.getValue().getSettlementType(), entry.getValue().getSettlementDesc(), entry.getValue().getSettlementAmt()
	    };
	    dmSettlementGrid.addRow(arrObj);
	    totalSettleAmt += entry.getValue().getSettlementAmt();
	}

	txtAmount.setText(String.valueOf(totalSettleAmt));
    }

    private void funRemoveSettlementOptionFromGrid() throws Exception
    {
	int row = tblSettlement.getSelectedRow();
	int col = tblSettlement.getSelectedColumn();
	String settleType = tblSettlement.getValueAt(row, col).toString();
	hmSettlementOptions.remove(settleType);
	funFillSettlementGrid();
    }

    private void funSaveButtonPressed()
    {
	if (hmSettlementOptions.size() > 0)
	{
	    if (authenticateMemberCard.equals("Y"))
	    {
		if (debitMemberCode.isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Select Member!!!");
		}
		else
		{
		    funRechargeDebitCard();
		}
	    }
	    else
	    {
		funRechargeDebitCard();
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Please Enter Amount!!!");
	}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel2 = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  
        ;
        panelBody = new javax.swing.JPanel();
        btnNew1 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jlblRechargeNo = new javax.swing.JLabel();
        lblRechargeNo = new javax.swing.JLabel();
        lblCardNo1 = new javax.swing.JLabel();
        txtCardNo = new javax.swing.JTextField();
        btnSwipeCard = new javax.swing.JButton();
        jlblRedeemNo = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        lblCardTypeName = new javax.swing.JLabel();
        txtCardTypeName = new javax.swing.JTextField();
        lblValidUpTo = new javax.swing.JLabel();
        txtValidUpTo = new javax.swing.JTextField();
        lblMinCharges = new javax.swing.JLabel();
        txtMinCharges = new javax.swing.JTextField();
        lblDeposite = new javax.swing.JLabel();
        txtDeposit = new javax.swing.JTextField();
        btnMember = new javax.swing.JButton();
        lblMemberCode = new javax.swing.JLabel();
        chkRedeemable = new javax.swing.JCheckBox();
        chkComplementary = new javax.swing.JCheckBox();
        lblCFBalance = new javax.swing.JLabel();
        txtCFBalance = new javax.swing.JTextField();
        lblOperation = new javax.swing.JLabel();
        cmbOperation = new javax.swing.JComboBox();
        lblAmount = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnPrevSettlementMode = new javax.swing.JButton();
        btnSettle1 = new javax.swing.JButton();
        btnSettle2 = new javax.swing.JButton();
        btnSettle3 = new javax.swing.JButton();
        btnSettle4 = new javax.swing.JButton();
        btnNextSettlementMode = new javax.swing.JButton();
        btnCal7 = new javax.swing.JButton();
        btnCal8 = new javax.swing.JButton();
        btnCal9 = new javax.swing.JButton();
        btnCalClear = new javax.swing.JButton();
        btnCal0 = new javax.swing.JButton();
        btnCal6 = new javax.swing.JButton();
        btnCal5 = new javax.swing.JButton();
        btnCal4 = new javax.swing.JButton();
        btnCal1 = new javax.swing.JButton();
        btnCal2 = new javax.swing.JButton();
        btnCal3 = new javax.swing.JButton();
        btnCal00 = new javax.swing.JButton();
        btnCalEnter = new javax.swing.JButton();
        btnCalBackSpace = new javax.swing.JButton();
        btnCalDot = new javax.swing.JButton();
        lblSelectedSettlementMode = new javax.swing.JLabel();
        scrSettlementDtl = new javax.swing.JScrollPane();
        tblSettlement = new javax.swing.JTable();
        lblMemberName = new javax.swing.JLabel();
        lblSettleAmt = new javax.swing.JLabel();
        btnRemoveSettlement = new javax.swing.JButton();
        lblBalWithoutSettle = new javax.swing.JLabel();
        chkTransferBal = new javax.swing.JCheckBox();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Recharge Debit Card");
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);
        panelBody.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                panelBodyKeyPressed(evt);
            }
        });

        btnNew1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew1.setForeground(new java.awt.Color(255, 255, 255));
        btnNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnNew1.setText("SAVE");
        btnNew1.setToolTipText("Save Debit Card Detail");
        btnNew1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNew1MouseClicked(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Recharge Debit Card");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        jlblRechargeNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jlblRechargeNo.setText("Recharge No        :");

        lblRechargeNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblCardNo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardNo1.setText("Card No.              :");

        txtCardNo.setEditable(false);
        txtCardNo.setBackground(new java.awt.Color(255, 255, 255));
        txtCardNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCardNo.setFocusable(false);
        txtCardNo.setPreferredSize(new java.awt.Dimension(59, 22));
        txtCardNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardNoMouseClicked(evt);
            }
        });
        txtCardNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCardNoKeyPressed(evt);
            }
        });

        btnSwipeCard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSwipeCard.setForeground(new java.awt.Color(255, 255, 255));
        btnSwipeCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSwipeCard.setText("Swipe");
        btnSwipeCard.setToolTipText("Swipe The Card");
        btnSwipeCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSwipeCard.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSwipeCard.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSwipeCardMouseClicked(evt);
            }
        });
        btnSwipeCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSwipeCardKeyPressed(evt);
            }
        });

        jlblRedeemNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jlblRedeemNo.setText("Card Holder Name :");

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblCardTypeName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardTypeName.setText("Card Type Name   :");

        txtCardTypeName.setEditable(false);
        txtCardTypeName.setBackground(new java.awt.Color(204, 204, 204));
        txtCardTypeName.setFocusable(false);
        txtCardTypeName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCardTypeNameKeyPressed(evt);
            }
        });

        lblValidUpTo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblValidUpTo.setText("Valid Up To          :");

        txtValidUpTo.setEditable(false);
        txtValidUpTo.setBackground(new java.awt.Color(204, 204, 204));
        txtValidUpTo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtValidUpTo.setFocusable(false);
        txtValidUpTo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtValidUpToKeyPressed(evt);
            }
        });

        lblMinCharges.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinCharges.setText("Min. Charges        :");

        txtMinCharges.setEditable(false);
        txtMinCharges.setBackground(new java.awt.Color(204, 204, 204));
        txtMinCharges.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMinCharges.setFocusable(false);
        txtMinCharges.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMinChargesKeyPressed(evt);
            }
        });

        lblDeposite.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeposite.setText("Deposit                :");

        txtDeposit.setEditable(false);
        txtDeposit.setBackground(new java.awt.Color(204, 204, 204));
        txtDeposit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDeposit.setFocusable(false);
        txtDeposit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDepositKeyPressed(evt);
            }
        });

        btnMember.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMember.setForeground(new java.awt.Color(255, 255, 255));
        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMember.setText("Member");
        btnMember.setToolTipText("Save Debit Card Detail");
        btnMember.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMember.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnMember.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMemberMouseClicked(evt);
            }
        });
        btnMember.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMemberActionPerformed(evt);
            }
        });
        btnMember.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnMemberKeyPressed(evt);
            }
        });

        lblMemberCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        chkRedeemable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkRedeemable.setText("Redeemable");
        chkRedeemable.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkRedeemableKeyPressed(evt);
            }
        });

        chkComplementary.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkComplementary.setText("Complementary");
        chkComplementary.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkComplementaryKeyPressed(evt);
            }
        });

        lblCFBalance.setBackground(new java.awt.Color(255, 255, 255));
        lblCFBalance.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCFBalance.setText("C/F Balance         : ");

        txtCFBalance.setEditable(false);
        txtCFBalance.setBackground(new java.awt.Color(204, 204, 204));
        txtCFBalance.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCFBalance.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCFBalanceKeyPressed(evt);
            }
        });

        lblOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperation.setText("Operation            :");

        cmbOperation.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Recharge", "Refund" }));
        cmbOperation.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperationActionPerformed(evt);
            }
        });
        cmbOperation.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbOperationKeyPressed(evt);
            }
        });

        lblAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAmount.setText("Amount          :");

        txtAmount.setEditable(false);
        txtAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAmount.setText("0.00");
        txtAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAmountMouseClicked(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAmountKeyPressed(evt);
            }
        });

        btnPrevSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevSettlementMode.setText("<<<");
        btnPrevSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevSettlementModeActionPerformed(evt);
            }
        });

        btnSettle1.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle1.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseEntered(evt);
            }
        });

        btnSettle2.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle2.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle2MouseClicked(evt);
            }
        });

        btnSettle3.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle3.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle3MouseClicked(evt);
            }
        });
        btnSettle3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettle3ActionPerformed(evt);
            }
        });

        btnSettle4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle4.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle4MouseClicked(evt);
            }
        });

        btnNextSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnNextSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextSettlementMode.setText(">>>");
        btnNextSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextSettlementModeActionPerformed(evt);
            }
        });

        btnCal7.setBackground(new java.awt.Color(102, 153, 255));
        btnCal7.setText("7");
        btnCal7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal7MouseClicked(evt);
            }
        });

        btnCal8.setBackground(new java.awt.Color(102, 153, 255));
        btnCal8.setText("8");
        btnCal8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal8MouseClicked(evt);
            }
        });

        btnCal9.setBackground(new java.awt.Color(102, 153, 255));
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
            }
        });

        btnCalClear.setBackground(new java.awt.Color(102, 153, 255));
        btnCalClear.setText("C");
        btnCalClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalClearMouseClicked(evt);
            }
        });

        btnCal0.setBackground(new java.awt.Color(102, 153, 255));
        btnCal0.setText("0");
        btnCal0.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal0MouseClicked(evt);
            }
        });

        btnCal6.setBackground(new java.awt.Color(102, 153, 255));
        btnCal6.setText("6");
        btnCal6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal6MouseClicked(evt);
            }
        });

        btnCal5.setBackground(new java.awt.Color(102, 153, 255));
        btnCal5.setText("5");
        btnCal5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal5MouseClicked(evt);
            }
        });

        btnCal4.setBackground(new java.awt.Color(102, 153, 255));
        btnCal4.setText("4");
        btnCal4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal4MouseClicked(evt);
            }
        });

        btnCal1.setBackground(new java.awt.Color(102, 153, 255));
        btnCal1.setText("1");
        btnCal1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal1MouseClicked(evt);
            }
        });

        btnCal2.setBackground(new java.awt.Color(102, 153, 255));
        btnCal2.setText("2");
        btnCal2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal2MouseClicked(evt);
            }
        });

        btnCal3.setBackground(new java.awt.Color(102, 153, 255));
        btnCal3.setText("3");
        btnCal3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal3MouseClicked(evt);
            }
        });

        btnCal00.setBackground(new java.awt.Color(102, 153, 255));
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
            }
        });

        btnCalEnter.setBackground(new java.awt.Color(102, 153, 255));
        btnCalEnter.setText("Enter");
        btnCalEnter.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalEnterMouseClicked(evt);
            }
        });

        btnCalBackSpace.setBackground(new java.awt.Color(102, 153, 255));
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalBackSpaceMouseClicked(evt);
            }
        });

        btnCalDot.setBackground(new java.awt.Color(102, 153, 255));
        btnCalDot.setText(".");
        btnCalDot.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalDotMouseClicked(evt);
            }
        });

        lblSelectedSettlementMode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        tblSettlement.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSettlement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "SettlementType", "SettlementName", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblSettlement.setRowHeight(25);
        tblSettlement.getTableHeader().setReorderingAllowed(false);
        scrSettlementDtl.setViewportView(tblSettlement);
        if (tblSettlement.getColumnModel().getColumnCount() > 0)
        {
            tblSettlement.getColumnModel().getColumn(0).setResizable(false);
            tblSettlement.getColumnModel().getColumn(1).setResizable(false);
            tblSettlement.getColumnModel().getColumn(2).setResizable(false);
        }

        lblMemberName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblSettleAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        btnRemoveSettlement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemoveSettlement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnRemoveSettlement.setText("REMOVE");
        btnRemoveSettlement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveSettlement.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveSettlementActionPerformed(evt);
            }
        });

        chkTransferBal.setText("Transfer Balance");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblCardTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCardTypeName))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jlblRechargeNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblCardNo1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblRechargeNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(26, 26, 26)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(jlblRedeemNo, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addComponent(lblCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(panelBodyLayout.createSequentialGroup()
                                                    .addComponent(lblMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(txtMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(panelBodyLayout.createSequentialGroup()
                                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblDeposite, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(chkRedeemable))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(chkComplementary, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(lblCFBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCFBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 79, Short.MAX_VALUE))))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblValidUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtValidUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                                .addComponent(btnMember, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblMemberCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                                .addGap(125, 125, 125)
                                                .addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addComponent(lblMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblBalWithoutSettle, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(lblSelectedSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(lblSettleAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addGap(79, 79, 79)
                                                .addComponent(lblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(scrSettlementDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(panelBodyLayout.createSequentialGroup()
                                                    .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(panelBodyLayout.createSequentialGroup()
                                                    .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(btnPrevSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(btnSettle1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(btnSettle2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(8, 8, 8)
                                    .addComponent(btnSettle3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(btnSettle4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(btnNextSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(chkTransferBal, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnRemoveSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblRechargeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRechargeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblRedeemNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCardNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDeposite, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCardTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkRedeemable)
                            .addComponent(chkComplementary, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblValidUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValidUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblCFBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCFBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnMember, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMemberCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblMemberName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblBalWithoutSettle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(chkTransferBal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPrevSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSettle1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSettle2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSettle3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSettle4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNextSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSelectedSettlementMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSettleAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(scrSettlementDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCardNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardNoMouseClicked

    }//GEN-LAST:event_txtCardNoMouseClicked

    private void btnSwipeCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSwipeCardMouseClicked
	// TODO add your handling code here:
	funResetFields();
	new frmSwipCardPopUp(this, "frmRechargeDebitCard1").setVisible(true);
	txtCardNo.setText(clsGlobalVarClass.gDebitCardNo);
	txtCardNo.requestFocus();
	funSetCardData(clsGlobalVarClass.gDebitCardNo);
    }//GEN-LAST:event_btnSwipeCardMouseClicked

    private void txtAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAmountMouseClicked

    }//GEN-LAST:event_txtAmountMouseClicked

    private void cmbOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationActionPerformed
	// TODO add your handling code here:
	funRechargeComboChanged();
    }//GEN-LAST:event_cmbOperationActionPerformed

    private void btnNew1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNew1MouseClicked
	// TODO add your handling code here:
	funSaveButtonPressed();
    }//GEN-LAST:event_btnNew1MouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	objUtility = null;
	dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtCFBalanceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCFBalanceKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtAmount.requestFocus();
	}
    }//GEN-LAST:event_txtCFBalanceKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbOperation.requestFocus();
	}
    }//GEN-LAST:event_txtAmountKeyPressed

    private void cmbOperationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew1.requestFocus();
	}
    }//GEN-LAST:event_cmbOperationKeyPressed

    private void panelBodyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelBodyKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_panelBodyKeyPressed

    private void txtCardNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardNoKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnSwipeCard.requestFocus();
	}
    }//GEN-LAST:event_txtCardNoKeyPressed

    private void btnSwipeCardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSwipeCardKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtValidUpTo.requestFocus();
	}
    }//GEN-LAST:event_btnSwipeCardKeyPressed

    private void txtValidUpToKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValidUpToKeyPressed

    }//GEN-LAST:event_txtValidUpToKeyPressed

    private void txtCardTypeNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardTypeNameKeyPressed
	// TODO add your handling code here:        
    }//GEN-LAST:event_txtCardTypeNameKeyPressed

    private void txtDepositKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDepositKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtMinCharges.requestFocus();
	}
    }//GEN-LAST:event_txtDepositKeyPressed

    private void txtMinChargesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinChargesKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkRedeemable.requestFocus();
	}
    }//GEN-LAST:event_txtMinChargesKeyPressed

    private void chkRedeemableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkRedeemableKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkComplementary.requestFocus();
	}
    }//GEN-LAST:event_chkRedeemableKeyPressed

    private void chkComplementaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkComplementaryKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew1.requestFocus();
	}
    }//GEN-LAST:event_chkComplementaryKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	clsGlobalVarClass.gDebitCardNo = null;
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("RechargeDebitCard");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMemberMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnMemberMouseClicked

    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
	// funResetFields();
	new frmSwipCardPopUp(this, "frmRechargeDebitCard1").setVisible(true);
	funSetMemberCardData(clsGlobalVarClass.gDebitCardNo);

    }//GEN-LAST:event_btnMemberActionPerformed

    private void btnMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnMemberKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnMemberKeyPressed

    private void btnPrevSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevSettlementModeActionPerformed
	// TODO add your handling code here:
	funPrevSettlementMode();
    }//GEN-LAST:event_btnPrevSettlementModeActionPerformed

    private void btnSettle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle1MouseClicked
	funSettlementBtnClick(btnSettle1.getText());
    }//GEN-LAST:event_btnSettle1MouseClicked

    private void btnSettle1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle1MouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnSettle1MouseEntered

    private void btnSettle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle2MouseClicked
	funSettlementBtnClick(btnSettle2.getText());
    }//GEN-LAST:event_btnSettle2MouseClicked

    private void btnSettle3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle3MouseClicked
	funSettlementBtnClick(btnSettle3.getText());
    }//GEN-LAST:event_btnSettle3MouseClicked

    private void btnSettle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettle3ActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnSettle3ActionPerformed

    private void btnSettle4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle4MouseClicked
	funSettlementBtnClick(btnSettle4.getText());
    }//GEN-LAST:event_btnSettle4MouseClicked

    private void btnNextSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextSettlementModeActionPerformed
	funNextSettlementMode();
    }//GEN-LAST:event_btnNextSettlementModeActionPerformed

    private void btnCal7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal7MouseClicked
	funKeyBoardNumericValue(btnCal7.getText());
    }//GEN-LAST:event_btnCal7MouseClicked

    private void btnCal8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal8MouseClicked
	funKeyBoardNumericValue(btnCal8.getText());
    }//GEN-LAST:event_btnCal8MouseClicked

    private void btnCal9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal9MouseClicked
	funKeyBoardNumericValue(btnCal9.getText());
    }//GEN-LAST:event_btnCal9MouseClicked

    private void btnCalClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalClearMouseClicked
	lblSettleAmt.setText("");
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked
	funKeyBoardNumericValue(btnCal0.getText());
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked
	funKeyBoardNumericValue(btnCal6.getText());
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked
	funKeyBoardNumericValue(btnCal5.getText());
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked
	funKeyBoardNumericValue(btnCal4.getText());
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked
	funKeyBoardNumericValue(btnCal1.getText());
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked
	funKeyBoardNumericValue(btnCal2.getText());
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked
	funKeyBoardNumericValue(btnCal3.getText());
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked
	funKeyBoardNumericValue(btnCal00.getText());
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCalEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalEnterMouseClicked
	funKeyBoardEnterButtonPressed();
    }//GEN-LAST:event_btnCalEnterMouseClicked

    private void btnCalBackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalBackSpaceMouseClicked
	funBackspaceBtnPressed();
    }//GEN-LAST:event_btnCalBackSpaceMouseClicked

    private void btnCalDotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalDotMouseClicked
	funKeyBoardNumericValue(btnCalDot.getText());
    }//GEN-LAST:event_btnCalDotMouseClicked

    private void btnRemoveSettlementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveSettlementActionPerformed
	// TODO add your handling code here:
	try
	{
	    if (tblSettlement.getSelectedRow() < 0)
	    {
		JOptionPane.showMessageDialog(null, "Please Select Settlement Mode to Remove!!!");
		return;
	    }
	    funRemoveSettlementOptionFromGrid();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnRemoveSettlementActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("RechargeDebitCard");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("RechargeDebitCard");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCal0;
    private javax.swing.JButton btnCal00;
    private javax.swing.JButton btnCal1;
    private javax.swing.JButton btnCal2;
    private javax.swing.JButton btnCal3;
    private javax.swing.JButton btnCal4;
    private javax.swing.JButton btnCal5;
    private javax.swing.JButton btnCal6;
    private javax.swing.JButton btnCal7;
    private javax.swing.JButton btnCal8;
    private javax.swing.JButton btnCal9;
    private javax.swing.JButton btnCalBackSpace;
    private javax.swing.JButton btnCalClear;
    private javax.swing.JButton btnCalDot;
    private javax.swing.JButton btnCalEnter;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnMember;
    private javax.swing.JButton btnNew1;
    private javax.swing.JButton btnNextSettlementMode;
    private javax.swing.JButton btnPrevSettlementMode;
    private javax.swing.JButton btnRemoveSettlement;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSettle1;
    private javax.swing.JButton btnSettle2;
    private javax.swing.JButton btnSettle3;
    private javax.swing.JButton btnSettle4;
    private javax.swing.JButton btnSwipeCard;
    private javax.swing.JCheckBox chkComplementary;
    private javax.swing.JCheckBox chkRedeemable;
    private javax.swing.JCheckBox chkTransferBal;
    private javax.swing.JComboBox cmbOperation;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlblRechargeNo;
    private javax.swing.JLabel jlblRedeemNo;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblBalWithoutSettle;
    private javax.swing.JLabel lblCFBalance;
    private javax.swing.JLabel lblCardNo1;
    private javax.swing.JLabel lblCardTypeName;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeposite;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMemberCode;
    private javax.swing.JLabel lblMemberName;
    private javax.swing.JLabel lblMinCharges;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperation;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRechargeNo;
    private javax.swing.JLabel lblSelectedSettlementMode;
    private javax.swing.JLabel lblSettleAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblValidUpTo;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane scrSettlementDtl;
    private javax.swing.JTable tblSettlement;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtCFBalance;
    private javax.swing.JTextField txtCardNo;
    private javax.swing.JTextField txtCardTypeName;
    private javax.swing.JTextField txtDeposit;
    private javax.swing.JTextField txtMinCharges;
    private javax.swing.JTextField txtValidUpTo;
    // End of variables declaration//GEN-END:variables

}

class clsRechargeSettlementOptions
{

    private String settlementCode;

    private String settlementType;

    private String settlementDesc;

    private String cardNo;

    private double settlementAmt;

    public String getSettlementCode()
    {
	return settlementCode;
    }

    public void setSettlementCode(String settlementCode)
    {
	this.settlementCode = settlementCode;
    }

    public String getSettlementType()
    {
	return settlementType;
    }

    public void setSettlementType(String settlementType)
    {
	this.settlementType = settlementType;
    }

    public String getSettlementDesc()
    {
	return settlementDesc;
    }

    public void setSettlementDesc(String settlementDesc)
    {
	this.settlementDesc = settlementDesc;
    }

    public String getCardNo()
    {
	return cardNo;
    }

    public void setCardNo(String cardNo)
    {
	this.cardNo = cardNo;
    }

    public double getSettlementAmt()
    {
	return settlementAmt;
    }

    public void setSettlementAmt(double settlementAmt)
    {
	this.settlementAmt = settlementAmt;
    }
}
