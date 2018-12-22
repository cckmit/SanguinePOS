/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsStockInDtl;
import com.POSGlobal.controller.clsStockInHd;
import com.POSGlobal.controller.clsStockOutDtl;
import com.POSGlobal.controller.clsStockOutHd;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class frmStkIn extends javax.swing.JFrame
{

    private Map<String, clsStockInDtl> hmStockInDtl = new HashMap<String, clsStockInDtl>();
    private Map<String, clsStockOutDtl> hmStockOutDtl = new HashMap<String, clsStockOutDtl>();
    private String textValue, itemCode;
    private Map hmReasons;
    private boolean numFlag;
    private clsUtility objUtility;
    private String selectedFileName;
    private Map<String, String> hmItemMaster;
    private String itemSelection;
    private List<clsTaxCalculationDtls> arrListTaxCal;
    private double dynamicTaxAmt = 0, globalGrandTotal = 0;

    public frmStkIn()
    {

    }

    public frmStkIn(String op)
    {
	initComponents();

	selectedFileName = "";
	itemSelection = "";
	objUtility = new clsUtility();
	if ((!op.equals("StkIn")))
	{
	    lblformName.setText("- Stock Out");
	    lblSupplierName.setVisible(false);
	    txtSuppCode.setVisible(false);
	    txtSupplierName.setVisible(false);
	    lblAgainst.setVisible(false);
	    txtPOCode.setVisible(false);
	    btnFill.setVisible(false);
	}
	funSetShortCutKeys();
	txtExtCode.requestFocus();
	Timer timer = new Timer(500, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		Date date1 = new Date();
		String new_str = String.format("%tr", date1);
		String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
		lblDate.setText(dateAndTime);
	    }
	});
	timer.setRepeats(true);
	timer.setCoalesce(true);
	timer.setInitialDelay(0);
	timer.start();

	try
	{
	    numFlag = false;
	    textValue = "";
	    hmReasons = new HashMap<String, String>();
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    lblStkDate.setText(bdte);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date purchaseDate = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dtePurchaseBillDate.setDate(purchaseDate);

	    if (op.equals("StkIn"))
	    {

		btnModifyStk.setText("MODIFY STOCKIN");
		lblStkNo.setText("Stk In No.");
		String sql = "select strReasonCode,strReasonName,strTransferType from tblreasonmaster where strStkIn='Y'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rs.next())
		{
		    hmReasons.put(rs.getString(2), rs.getString(1));
		}
		rs.close();
		funFillReasonCombo();

	    }
	    else
	    {
		// lblProductName is a lable  
		btnModifyStk.setText("MODIFY STOCKOUT");
		lblStkNo.setText("Stk Out No.");
		String sql = "select strReasonCode,strReasonName,strTransferType from tblreasonmaster where strStkOut='Y'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rs.next())
		{
		    hmReasons.put(rs.getString(2), rs.getString(1));
		}
		rs.close();
		funFillReasonCombo();
	    }

	    btnPLU.setVisible(false);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(180);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(70);
	    tblItemTable.setShowHorizontalLines(true);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);

	    hmItemMaster = new HashMap<String, String>();
	    ResultSet rsItem = clsGlobalVarClass.dbMysql.executeResultSet("select strItemCode,strItemName from tblitemmaster where strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
	    while (rsItem.next())
	    {
		hmItemMaster.put(rsItem.getString(2).trim(), rsItem.getString(1));
	    }
	    rsItem.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Function to go to home after clicking on home button
    private void funHomeButtonClicked()
    {
	int cntRow = tblItemTable.getRowCount();
	if (cntRow > 0)
	{
	    frmOkCancelPopUp okOb = new frmOkCancelPopUp(this, "Do you want to end transaction");
	    okOb.setVisible(true);
	    int res = okOb.getResult();
	    if (res == 1)
	    {
		funTruncateTable();
		dispose();
		clsGlobalVarClass.hmActiveForms.remove("Stock In");
		clsGlobalVarClass.hmActiveForms.remove("Stock Out");
	    }
	}
	else
	{
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("Stock In");
	    clsGlobalVarClass.hmActiveForms.remove("Stock Out");
	}
    }

    //Function to Set searched Item realated data to textfield
    private void funSetData(Object[] data)
    {
	try
	{
	    itemSelection = "from help";
	    String sql = "";
	    if (clsGlobalVarClass.gStockInOption.equalsIgnoreCase("ItemWise"))
	    {
		sql = "select strItemCode,strItemName,dblPurchaseRate,strExternalCode "
			+ "from tblitemmaster where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    }
	    else
	    {
		sql = "select strMenuCode,strMenuName,0.00,'' "
			+ "from tblmenuhd where strMenuCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    }
	    ResultSet rsItemData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsItemData.next();
	    itemCode = rsItemData.getString(1);
	    txtItemName.setText(rsItemData.getString(2));
	    txtPurchaseRate.setText(rsItemData.getString(3));
	    txtQty.setText("1");

	    if (!rsItemData.getString(4).equals("0.00"))
	    {
		txtExtCode.setText(rsItemData.getString(4));
	    }
	    numFlag = false;

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Function to Fill Reason Combobox
    private void funFillReasonCombo()
    {
	Set setReasons = hmReasons.keySet();
	Iterator itrReasons = setReasons.iterator();
	cmbReason.removeAllItems();
	while (itrReasons.hasNext())
	{
	    cmbReason.addItem(itrReasons.next());
	}

    }

    //Function to generate stockIn code
    public String funGenerateStockInCode()
    {
	long lastNo = 0;
	String sql = "", stockInCode = "";

	try
	{
	    //obj_List_clsStockInTemp.clear();  ///Ask
	    sql = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockInNo'";
	    ResultSet rsStockinno = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsStockinno.next())
	    {
		lastNo = rsStockinno.getLong(2);
		lastNo = lastNo + 1;
		rsStockinno.close();
		stockInCode = "SI" + String.format("%07d", lastNo);
		if (stockInCode.trim().length() <= 7)
		{
		    new frmOkPopUp(null, "StockInCode Not Generated  Please Try Again", "Error", 1).setVisible(true);
		    funClearFields();

		}
		System.out.println(stockInCode);
		sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='stockinNo'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return stockInCode;
    }

// Function to Save StockIn
    private void funSaveStkIn()
    {
	try
	{
	    double totalFixedTaxAmt = 0;
	    for (int cn = 0; cn < tblTaxCal.getRowCount(); cn++)
	    {
		if (!tblTaxCal.getValueAt(cn, 1).toString().trim().isEmpty())
		{
		    totalFixedTaxAmt += Double.parseDouble(tblTaxCal.getValueAt(cn, 1).toString());
		}
	    }
	    if (Double.parseDouble(lblTotalFixedTax.getText()) != totalFixedTaxAmt)
	    {
		JOptionPane.showMessageDialog(this, "Please click on UPDATE TOTAL on Calculate Tax Tab.");
		return;
	    }

	    int ex = 0;
	    java.util.Date objDate = new java.util.Date();
	    objDate = dtePurchaseBillDate.getDate();
	    if (tblItemTable.getRowCount() == 0)
	    {
		new frmOkPopUp(null, "Please Select Item", "Warning", 1).setVisible(true);
		return;
	    }

	    if (txtPurchaseBillNo.getText().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Purchase Bill NO");
		return;
	    }

	    if (objDate == null)
	    {
		new frmOkPopUp(null, "Invalid Date", "", 2).setVisible(true);
		dtePurchaseBillDate.requestFocusInWindow();
	    }
	    if (txtSuppCode.getText().trim().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Select Supplier");
		return;
	    }
	    else
	    {
		if (cmbReason.getItemCount() == 0)
		{
		    JOptionPane.showMessageDialog(null, "Please create stock in reason!!!");
		    return;
		}
		String favoritereason = cmbReason.getSelectedItem().toString();
		String reasonCode = hmReasons.get(favoritereason).toString();
		String purchaseBillDate = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			+ " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

		if (!(lblStkInCode.getText().isEmpty()))
		{
		    if (favoritereason != null)
		    {
			String stockInCode = lblStkInCode.getText().toString();
			System.out.println("Stock in code=" + stockInCode);

			clsStockInHd objStockInHd = new clsStockInHd();
			objStockInHd.setStrStkInCode(stockInCode);
			objStockInHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
			objStockInHd.setDteStkInDate(clsGlobalVarClass.gPOSDateForTransaction);
			objStockInHd.setStrReasonCode(reasonCode);
			objStockInHd.setStrPurchaseBillNo(txtPurchaseBillNo.getText());
			objStockInHd.setDtePurchaseBillDate(purchaseBillDate);
			objStockInHd.setIntShiftCode(0);
			objStockInHd.setStrUserCreated(lblUserCode.getText());
			objStockInHd.setStrUserEdited(lblUserCode.getText());
			objStockInHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
			objStockInHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
			objStockInHd.setStrClientCode(clsGlobalVarClass.gClientCode);
			objStockInHd.setStrInvoiceCode(txtPurchaseBillNo.getText());
			objStockInHd.setDblTaxAmt(Double.parseDouble(lblTotalTax.getText()));
			objStockInHd.setDblExtraAmt(0.00);
			objStockInHd.setDblGrandTotal(Double.parseDouble(txtTotal.getText()));
			objStockInHd.setStrSupplierCode(txtSuppCode.getText());

			ex = funInsertStockInDataTable(hmStockInDtl, objStockInHd);
			if (ex > 0)
			{
			    lblStkInCode.setText(stockInCode);
			}

			int res = JOptionPane.showConfirmDialog(null, "Do you want to update purchase rate in item master with excel sheet ?");
			if (res == 0)
			{
			    for (Map.Entry<String, clsStockInDtl> entry : hmStockInDtl.entrySet())
			    {
				String sqlUpdate = "update tblitemmaster set dblPurchaseRate = " + entry.getValue().getDblPurchaseRate() + " "
					+ " where strItemCode='" + entry.getKey() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
				clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			    }
			}

			JOptionPane.showMessageDialog(this, "Stock In No : " + stockInCode, "", 1);
			funStockInOutReport("StockIn", stockInCode, lblStkDate.getText());
		    }
		}
		else
		{
		    if (favoritereason != null)
		    {
			String stockInCode = funGenerateStockInCode();
			System.out.println("Stock in code=" + stockInCode);

			clsStockInHd objStockInHd = new clsStockInHd();
			objStockInHd.setStrStkInCode(stockInCode);
			objStockInHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
			objStockInHd.setDteStkInDate(clsGlobalVarClass.gPOSDateForTransaction);
			objStockInHd.setStrReasonCode(reasonCode);
			objStockInHd.setStrPurchaseBillNo(txtPurchaseBillNo.getText());
			objStockInHd.setDtePurchaseBillDate(purchaseBillDate);
			objStockInHd.setIntShiftCode(0);
			objStockInHd.setStrUserCreated(lblUserCode.getText());
			objStockInHd.setStrUserEdited(lblUserCode.getText());
			objStockInHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
			objStockInHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
			objStockInHd.setStrClientCode(clsGlobalVarClass.gClientCode);
			objStockInHd.setStrInvoiceCode(txtPurchaseBillNo.getText());
			objStockInHd.setDblTaxAmt(Double.parseDouble(lblTotalTax.getText()));
			objStockInHd.setDblExtraAmt(0.00);
			objStockInHd.setDblGrandTotal(Double.parseDouble(txtTotal.getText()));
			objStockInHd.setStrSupplierCode(txtSuppCode.getText());

			ex = funInsertStockInDataTable(hmStockInDtl, objStockInHd);
			if (ex > 0)
			{
			    lblStkInCode.setText(stockInCode);
			}

			int res = JOptionPane.showConfirmDialog(null, "Do you want to update purchase rate in item master with excel sheet ?");
			if (res == 0)
			{
			    for (Map.Entry<String, clsStockInDtl> entry : hmStockInDtl.entrySet())
			    {
				String sqlUpdate = "update tblitemmaster set dblPurchaseRate = " + entry.getValue().getDblPurchaseRate() + " "
					+ " where strItemCode='" + entry.getKey() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
				clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			    }
			}

			JOptionPane.showMessageDialog(this, "Stock In No : " + stockInCode, "", 1);
			funStockInOutReport("StockIn", stockInCode, lblStkDate.getText());
		    }
		}
		if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		{
		    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Inventory", "Stock");
		}
		funResetFields();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Function to save Stockout  
    private void funSaveStkOut()
    {
	try
	{
	    int ex = 0;
	    java.util.Date objDate = new java.util.Date();
	    objDate = dtePurchaseBillDate.getDate();

	    if (tblItemTable.getRowCount() == 0)
	    {
		new frmOkPopUp(null, "Please Select Item", "Warning", 1).setVisible(true);
		return;
	    }

	    if (txtPurchaseBillNo.getText().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Purchase Bill NO");
		return;
	    }

	    if (objDate == null)
	    {
		new frmOkPopUp(null, "Invalid Date", "", 2).setVisible(true);
		dtePurchaseBillDate.requestFocusInWindow();
	    }
	    else
	    {
		if (cmbReason.getItemCount() == 0)
		{
		    JOptionPane.showMessageDialog(null, "Please create stock out reason!!!");
		    return;
		}
		String purchaseBillDate = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			+ " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();
		String favoritereason = cmbReason.getSelectedItem().toString();
		String reasonCode = hmReasons.get(favoritereason).toString();
		if (!(lblStkInCode.getText().isEmpty()))
		{
		    if (favoritereason != null)
		    {
			String stockOutCode = lblStkInCode.getText().toString();
			clsStockOutHd objStockOutHd = new clsStockOutHd();
			objStockOutHd.setStrStkOutCode(stockOutCode);
			objStockOutHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
			objStockOutHd.setDteStkOutDate(clsGlobalVarClass.gPOSDateForTransaction);
			objStockOutHd.setStrReasonCode(reasonCode);
			objStockOutHd.setStrPurchaseBillNo(txtPurchaseBillNo.getText());
			objStockOutHd.setDtePurchaseBillDate(purchaseBillDate);
			objStockOutHd.setIntShiftCode(0);
			objStockOutHd.setStrUserCreated(lblUserCode.getText());
			objStockOutHd.setStrUserEdited(lblUserCode.getText());
			objStockOutHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
			objStockOutHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
			objStockOutHd.setStrClientCode(clsGlobalVarClass.gClientCode);
			objStockOutHd.setDblTaxAmt(Double.parseDouble(lblTotalTax.getText()));
			objStockOutHd.setDblExtraAmt(0.00);
			objStockOutHd.setDblGrandTotal(Double.parseDouble(txtTotal.getText()));
			objStockOutHd.setStrSupplierCode(txtSuppCode.getText());

			ex = funInsertStockOutDtlTable(hmStockOutDtl, objStockOutHd);
			if (ex > 0)
			{
			    lblStkInCode.setText(stockOutCode);
			    JOptionPane.showMessageDialog(this, "Stock Out No : " + stockOutCode, "", 1);
			    funStockInOutReport("StockOut", stockOutCode, lblStkDate.getText());
			}
		    }
		}
		else
		{
		    if (favoritereason != null)
		    {
			String stockOutCode = funGenerateStockOutCode();
			clsStockOutHd objStockOutHd = new clsStockOutHd();
			objStockOutHd.setStrStkOutCode(stockOutCode);
			objStockOutHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
			objStockOutHd.setDteStkOutDate(clsGlobalVarClass.gPOSDateForTransaction);
			objStockOutHd.setStrReasonCode(reasonCode);
			objStockOutHd.setStrPurchaseBillNo(txtPurchaseBillNo.getText());
			objStockOutHd.setDtePurchaseBillDate(purchaseBillDate);
			objStockOutHd.setIntShiftCode(0);
			objStockOutHd.setStrUserCreated(lblUserCode.getText());
			objStockOutHd.setStrUserEdited(lblUserCode.getText());
			objStockOutHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
			objStockOutHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
			objStockOutHd.setStrClientCode(clsGlobalVarClass.gClientCode);
			objStockOutHd.setDblTaxAmt(Double.parseDouble(lblTotalTax.getText()));
			objStockOutHd.setDblExtraAmt(0.00);
			objStockOutHd.setDblGrandTotal(Double.parseDouble(txtTotal.getText()));
			objStockOutHd.setStrSupplierCode(txtSuppCode.getText());

			ex = funInsertStockOutDtlTable(hmStockOutDtl, objStockOutHd);
			if (ex > 0)
			{
			    lblStkInCode.setText(stockOutCode);
			    JOptionPane.showMessageDialog(this, "Stock Out No : " + stockOutCode, "", 1);
			    funStockInOutReport("StockOut", stockOutCode, lblStkDate.getText());
			}
		    }
		}
	    }
	    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
	    {
		clsGlobalVarClass.funInvokeHOWebserviceForTrans("Inventory", "Stock");
	    }
	    funResetFields();
	    funTruncateTable();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funTruncateTable()
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute("truncate table tblstockintemp");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Function to generate stockOut code
    public String funGenerateStockOutCode()
    {
	long lastNo1 = 0;
	String sql = "", stockOutCode = "", sql1;
	try
	{
	    sql1 = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockOutNo'";
	    ResultSet rsStockOutno = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
	    if (rsStockOutno.next())
	    {
		lastNo1 = rsStockOutno.getLong(2);
		lastNo1 = lastNo1 + 1;
		stockOutCode = "SO" + String.format("%07d", lastNo1);
		sql = "update tblinternal set dblLastNo='" + lastNo1 + "' where strTransactionType='stockOutNo'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return stockOutCode;
    }

    private void funResetFields()
    {
	try
	{

	    DefaultTableModel dm = new DefaultTableModel();
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Tax");
	    dm.addColumn("Rate");
	    dm.addColumn("Amount");
	    tblItemTable.setModel(dm);
	    // funFillReasonCombo();

	    btnModifyStk.setEnabled(true);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(180);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(70);
	    tblItemTable.setShowHorizontalLines(true);
	    txtTotal.setText("");
	    lblTotalTax.setText("");
	    lblTotalQty.setText("");
	    lblStkInCode.setText("");
	    txtPurchaseRate.setText("0.00");
	    txtQty.setText("0.00");
	    txtItemName.setText("");
	    txtPurchaseBillNo.setText("");
	    txtPOCode.setText("");
	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date purchaseDate = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dtePurchaseBillDate.setDate(purchaseDate);
	    hmStockInDtl.clear();
	    hmStockOutDtl.clear();
	    selectedFileName = "";
	    txtSuppCode.setText("");
	    txtSupplierName.setText("");
	    dynamicTaxAmt = 0;
	    globalGrandTotal = 0;

	    DefaultTableModel dmTaxCal = (DefaultTableModel) tblTaxCal.getModel();
	    dmTaxCal.setRowCount(0);
	    tblTaxCal.setModel(dmTaxCal);
	    lblTotalFixedTax.setText("0.00");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funConvertString(String ItemName)
    {
	if (ItemName.contains("<html>"))
	{
	    String tempitemName = ItemName;
	    StringBuilder sb1 = new StringBuilder(tempitemName);
	    sb1 = sb1.delete(0, 6);
	    int seq = sb1.lastIndexOf("<br>");
	    String split = sb1.substring(0, seq);
	    int end = sb1.lastIndexOf("</html>");
	    String last = sb1.substring(seq + 4, end);
	    ItemName = split + " " + last;
	}
	return ItemName;
    }

    private void funTableRowClicked()
    {
	try
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    String itemName = tblItemTable.getValueAt(selectedRow, 1).toString();
	    itemCode = tblItemTable.getValueAt(selectedRow, 0).toString();
	    txtItemName.setText(itemName);
	    txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow, 3).toString());
	    txtQty.setText(tblItemTable.getValueAt(selectedRow, 2).toString());
	    itemSelection = "from grid";
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funItemCodeTextFieldClicked()
    {
	if (clsGlobalVarClass.gStockInOption.equalsIgnoreCase("ItemWise"))
	{
	    objUtility.funCallForSearchForm("RawMenuItem");
	}
	else
	{
	    objUtility.funCallForSearchForm("Menu");
	}
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetData(data);
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    //Function for calculator
    private void funPressKeyboardButton(String buttonText)
    {
	try
	{

	    textValue = textValue + buttonText;

	    if (!numFlag)
	    {
		txtQty.setText(textValue);
	    }
	    else
	    {
		txtPurchaseRate.setText(textValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Function to modify stock data on modify button
    private void funModifyStockButtonClicked()
    {
	try
	{
	    if (lblStkNo.getText().equals("Stk In No."))
	    {
		objUtility.funCallForSearchForm("StockIn");
		new frmSearchFormDialog(this, true).setVisible(true);
		if (clsGlobalVarClass.gSearchItemClicked)
		{
		    Object[] stockInData = clsGlobalVarClass.gArrListSearchData.toArray();
		    funSetDataStockIn(stockInData);
		    clsGlobalVarClass.gSearchItemClicked = false;
		}
	    }
	    else
	    {
		objUtility.funCallForSearchForm("StockOut");
		new frmSearchFormDialog(this, true).setVisible(true);
		if (clsGlobalVarClass.gSearchItemClicked)
		{
		    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		    funSetDataStockOut(data);
		    clsGlobalVarClass.gSearchItemClicked = false;
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetDataStockIn(Object[] stockInData)
    {
	funAddModifyStockInToList(stockInData[0].toString());

    }

    private void funSetDataStockOut(Object[] stockInData)
    {
	funAddModifyStockOutToList(stockInData[0].toString());

    }

    //Fetch stockin data from stockindtl and stockinhd table and fill map set it to tblItemTable
    private void funAddModifyStockInToList(String StockIncode)
    {
	try
	{
	    hmStockInDtl.clear();
	    lblStkInCode.setText(StockIncode);

	    String sql = "select b.strReasonName from tblstkinhd a,tblreasonmaster b where a.strStkInCode='" + StockIncode + "'"
		    + " and a.strReasonCode=b.strReasonCode";

	    ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsReason.next();
	    cmbReason.setSelectedItem(rsReason.getString(1));
	    rsReason.close();
	    sql = " SELECT a.strSupplierCode,b.strSupplierName "
		    + " FROM tblstkinhd a,tblsuppliermaster b"
		    + " WHERE a.strStkInCode='" + StockIncode + "' AND a.strSupplierCode=b.strSupplierCode";
	    ResultSet rsSupplier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsSupplier.next();
	    txtSuppCode.setText(rsSupplier.getString(1));
	    txtSupplierName.setText(rsSupplier.getString(2));
	    rsSupplier.close();
	    String purchaseBillNo = "";
	    Date purchaseBillDate = null;
	    if (clsGlobalVarClass.gStockInOption.equalsIgnoreCase("ItemWise"))
	    {
		sql = "select a.strStkInCode,c.strItemName,a.strItemCode,a.dblQuantity,a.dblPurchaseRate,a.dblAmount,"
			+ "b.strPurchaseBillNo,date(b.dtePurchaseBillDate) from tblstkindtl a,tblstkinhd b,tblitemmaster c  "
			+ "where a.strStkInCode='" + StockIncode + "' and a.strStkInCode=b.strStkInCode and a.strItemCode=c.strItemCode";
	    }
	    else
	    {
		sql = "select a.strStkInCode,c.strMenuName,a.strItemCode"
			+ ",a.dblQuantity,a.dblPurchaseRate,a.dblAmount,b.strPurchaseBillNo"
			+ ",date(b.dtePurchaseBillDate) "
			+ "from tblstkindtl a,tblstkinhd b,tblmenuhd c  "
			+ "where a.strStkInCode='" + StockIncode + "' and a.strStkInCode=b.strStkInCode "
			+ "and a.strItemCode=c.strMenuCode";
	    }
	    ResultSet rsStockIn = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsStockIn.next())
	    {
		itemCode = rsStockIn.getString(3);
		clsStockInDtl objStockInDtl = new clsStockInDtl();
		if (null != hmStockInDtl.get(itemCode))
		{
		    objStockInDtl = hmStockInDtl.get(itemCode);
		    objStockInDtl.setDblAmount(objStockInDtl.getDblAmount() + Double.parseDouble(rsStockIn.getString(5)) * Double.parseDouble(rsStockIn.getString(4)));
		    objStockInDtl.setDblQuantity(objStockInDtl.getDblQuantity() + Double.parseDouble(rsStockIn.getString(4)));
		    objStockInDtl.setDblPurchaseRate(objStockInDtl.getDblPurchaseRate() + Double.parseDouble(rsStockIn.getString(5)));
		}
		else
		{
		    objStockInDtl.setStrItemCode(itemCode);
		    objStockInDtl.setStrItemName(rsStockIn.getString(2));
		    objStockInDtl.setStrStkInCode(rsStockIn.getString(1));
		    objStockInDtl.setDblAmount(Double.parseDouble(rsStockIn.getString(6)));
		    objStockInDtl.setDblQuantity(Double.parseDouble(rsStockIn.getString(4)));
		    objStockInDtl.setDblPurchaseRate(Double.parseDouble(rsStockIn.getString(5)));
		    objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objStockInDtl.setStrDataPostFlag("N");
		}

		hmStockInDtl.put(itemCode, objStockInDtl);
		purchaseBillNo = rsStockIn.getString(7);
		purchaseBillDate = rsStockIn.getDate(8);
		itemCode = "";
	    }

	    txtPurchaseBillNo.setText(purchaseBillNo);
	    dtePurchaseBillDate.setDate(purchaseBillDate);
	    funRefreshStockInOutGridTable();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAddModifyStockOutToList(String stkOutcode)
    {
	try
	{
	    String purchaseBillNo = "";
	    Date purchaseBillDate = null;
	    hmStockOutDtl.clear();
	    lblStkInCode.setText(stkOutcode);

	    String sql = "select b.strReasonName from tblstkouthd a,tblreasonmaster b where a.strStkOutCode='" + stkOutcode + "' "
		    + "and a.strReasonCode=b.strReasonCode";
	    ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsReason.next();
	    cmbReason.setSelectedItem(rsReason.getString(1));
	    rsReason.close();

	    sql = "select a.strStkOutCode,c.strItemName,a.strItemCode,a.dblQuantity,a.dblPurchaseRate,a.dblAmount,b.strPurchaseBillNo,b.dtePurchaseBillDate "
		    + "from tblstkoutdtl a,tblstkouthd b ,tblitemmaster c where a.strStkOutCode='" + stkOutcode + "' and a.strStkOutCode=b.strStkOutCode and a.strItemCode=c.strItemCode";

	    ResultSet rsStockOut = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsStockOut.next())
	    {
		itemCode = rsStockOut.getString(3);
		clsStockOutDtl objStockOutDtl = new clsStockOutDtl();
		if (null != hmStockOutDtl.get(itemCode))
		{
		    objStockOutDtl = hmStockOutDtl.get(itemCode);
		    objStockOutDtl.setDblAmount(objStockOutDtl.getDblAmount() + Double.parseDouble(rsStockOut.getString(5)) * Double.parseDouble(rsStockOut.getString(4)));
		    objStockOutDtl.setDblQuantity(objStockOutDtl.getDblQuantity() + Double.parseDouble(rsStockOut.getString(4)));
		    objStockOutDtl.setDblPurchaseRate(objStockOutDtl.getDblPurchaseRate() + Double.parseDouble(rsStockOut.getString(5)));
		}
		else
		{
		    objStockOutDtl.setStrItemCode(itemCode);
		    objStockOutDtl.setStrItemName(rsStockOut.getString(2));
		    objStockOutDtl.setStrStkOutCode(rsStockOut.getString(1));
		    objStockOutDtl.setDblAmount(Double.parseDouble(rsStockOut.getString(6)));
		    objStockOutDtl.setDblQuantity(Double.parseDouble(rsStockOut.getString(4)));
		    objStockOutDtl.setDblPurchaseRate(Double.parseDouble(rsStockOut.getString(5)));
		    objStockOutDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objStockOutDtl.setStrDataPostFlag("N");
		}

		hmStockOutDtl.put(itemCode, objStockOutDtl);
		purchaseBillNo = rsStockOut.getString(7);
		purchaseBillDate = rsStockOut.getDate(8);
		itemCode = "";
	    }
	    txtPurchaseBillNo.setText(purchaseBillNo);
	    dtePurchaseBillDate.setDate(purchaseBillDate);
	    funRefreshStockInOutGridTable();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    //Insert stockin data to stockindtl and stockinhd table
    public int funInsertStockInDataTable(Map<String, clsStockInDtl> hmStockInDtl, clsStockInHd objStockInHd) throws Exception
    {
	String sql = "delete from tblstkinhd where strStkInCode='" + objStockInHd.getStrStkInCode() + "'";
	clsGlobalVarClass.dbMysql.execute(sql);

	sql = "insert into tblstkinhd (strStkInCode,strPOSCode,dteStkInDate,strReasonCode,strPurchaseBillNo"
		+ ",dtePurchaseBillDate,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
		+ ",strInvoiceCode,dblTaxAmt,dblExtraAmt,dblGrandTotal,strSupplierCode)"
		+ " values('" + objStockInHd.getStrStkInCode() + "','" + objStockInHd.getStrPOSCode() + "','" + objStockInHd.getDteStkInDate() + "','" + objStockInHd.getStrReasonCode()
		+ "','" + objStockInHd.getStrPurchaseBillNo() + "','" + objStockInHd.getDtePurchaseBillDate()
		+ "','" + objStockInHd.getStrUserCreated() + "','" + objStockInHd.getStrUserEdited() + "','" + objStockInHd.getDteDateCreated()
		+ "','" + objStockInHd.getDteDateEdited() + "','" + objStockInHd.getStrClientCode() + "'"
		+ ",'" + objStockInHd.getStrInvoiceCode() + "'," + objStockInHd.getDblTaxAmt() + "," + objStockInHd.getDblExtraAmt() + ""
		+ "," + objStockInHd.getDblGrandTotal() + ",'" + objStockInHd.getStrSupplierCode() + "')";
	clsGlobalVarClass.dbMysql.execute(sql);

	int rows = 0;
	sql = "delete from tblstkindtl where strStkInCode='" + objStockInHd.getStrStkInCode() + "'";
	clsGlobalVarClass.dbMysql.execute(sql);

	boolean flgSql = false;
	StringBuilder sb = new StringBuilder();
	sb.append(" insert into tblstkindtl (strStkInCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount,strClientCode,strDataPostFlag)"
		+ " values ");
	for (clsStockInDtl objStkinDtl : hmStockInDtl.values())
	{
	    flgSql = true;
	    sb.append("('" + objStockInHd.getStrStkInCode() + "','" + objStkinDtl.getStrItemCode() + "','" + objStkinDtl.getDblQuantity() + "','" + objStkinDtl.getDblPurchaseRate() + "','"
		    + objStkinDtl.getDblAmount() + "','" + objStkinDtl.getStrClientCode() + "','" + objStkinDtl.getStrDataPostFlag() + "'),");
	}

	int index = 0;
	if (flgSql)
	{
	    index = sb.lastIndexOf(",");
	    sb = sb.delete(index, sb.length());
	    rows = clsGlobalVarClass.dbMysql.execute(sb.toString());
	}

	if (null != arrListTaxCal)
	{
	    sql = "delete from tblstkintaxdtl where strStkInCode='" + objStockInHd.getStrStkInCode() + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    flgSql = false;
	    sb.setLength(0);
	    sb.append(" insert into tblstkintaxdtl (strStkInCode,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode,strDataPostFlag)"
		    + " values ");
	    for (clsTaxCalculationDtls objTaxCalDtl : arrListTaxCal)
	    {
		if (objTaxCalDtl.getTaxAmount() > 0)
		{
		    flgSql = true;
		    sb.append("('" + objStockInHd.getStrStkInCode() + "','" + objTaxCalDtl.getTaxCode() + "','" + objTaxCalDtl.getTaxableAmount() + "'"
			    + ",'" + objTaxCalDtl.getTaxAmount() + "','" + clsGlobalVarClass.gClientCode + "','N'),");
		}
	    }

	    for (int cn = 0; cn < tblTaxCal.getRowCount(); cn++)
	    {
		flgSql = true;
		sb.append("('" + objStockInHd.getStrStkInCode() + "','" + tblTaxCal.getValueAt(cn, 2).toString() + "'"
			+ "," + Double.parseDouble(txtTotal.getText()) + "," + Double.parseDouble(tblTaxCal.getValueAt(cn, 1).toString()) + ""
			+ ",'" + clsGlobalVarClass.gClientCode + "','N'),");
	    }

	    if (flgSql)
	    {
		index = sb.lastIndexOf(",");
		sb = sb.delete(index, sb.length());
		rows = clsGlobalVarClass.dbMysql.execute(sb.toString());
	    }
	}

	String sqlUpdatePO = "update tblpurchaseorderhd set strClosePO='Y' "
		+ " where strPOCode='" + objStockInHd.getStrPurchaseBillNo() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlUpdatePO);

	return rows;
    }

    //Function ti insert stockout data to stockoutdtl and stockouthd table
    public int funInsertStockOutDtlTable(Map<String, clsStockOutDtl> hmStockOutDtl, clsStockOutHd objStockOutHd) throws Exception
    {

	String sql = "delete from tblstkouthd where strStkOutCode='" + objStockOutHd.getStrStkOutCode() + "'";
	clsGlobalVarClass.dbMysql.execute(sql);

	sql = "insert into tblstkouthd (strStkOutCode,strPOSCode,dteStkOutDate"
		+ ",strReasonCode,strPurchaseBillNo,dtePurchaseBillDate,strUserCreated,strUserEdited"
		+ ",dteDateCreated,dteDateEdited,strClientCode,dblTaxAmt,dblExtraAmt,dblGrandTotal,strSupplierCode)"
		+ " values('" + objStockOutHd.getStrStkOutCode() + "','" + objStockOutHd.getStrPOSCode() + "','" + objStockOutHd.getDteStkOutDate() + "'"
		+ ",'" + objStockOutHd.getStrReasonCode() + "','" + objStockOutHd.getStrPurchaseBillNo() + "','" + objStockOutHd.getDtePurchaseBillDate() + "'"
		+ ",'" + objStockOutHd.getStrUserCreated() + "','" + objStockOutHd.getStrUserEdited() + "','" + objStockOutHd.getDteDateCreated() + "'"
		+ ",'" + objStockOutHd.getDteDateEdited() + "','" + objStockOutHd.getStrClientCode() + "'"
		+ "," + objStockOutHd.getDblTaxAmt() + "," + objStockOutHd.getDblExtraAmt() + ""
		+ "," + objStockOutHd.getDblGrandTotal() + ",'" + objStockOutHd.getStrSupplierCode() + "')";
	clsGlobalVarClass.dbMysql.execute(sql);

	boolean flgSql = false;
	int rows = 0;
	sql = "delete from tblstkoutdtl where strStkOutCode='" + objStockOutHd.getStrStkOutCode() + "'";
	clsGlobalVarClass.dbMysql.execute(sql);
	String iQuery = " insert into tblstkoutdtl (strStkOutCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount,strClientCode,strDataPostFlag)"
		+ " values ";
	for (clsStockOutDtl objStkoutDtl : hmStockOutDtl.values())
	{
	    flgSql = true;
	    iQuery += "('" + objStockOutHd.getStrStkOutCode() + "','" + objStkoutDtl.getStrItemCode() + "','" + objStkoutDtl.getDblQuantity() + "','" + objStkoutDtl.getDblPurchaseRate() + "','"
		    + objStkoutDtl.getDblAmount() + "','" + objStkoutDtl.getStrClientCode() + "','" + objStkoutDtl.getStrDataPostFlag() + "'),";
	}
	StringBuilder sb = new StringBuilder(iQuery);
	int index = sb.lastIndexOf(",");

	if (flgSql)
	{
	    iQuery = sb.delete(index, sb.length()).toString();
	    rows = clsGlobalVarClass.dbMysql.execute(iQuery);
	}

	flgSql = false;

	if (null != arrListTaxCal)
	{
	    sb.setLength(0);
	    sb.append(" insert into tblstkouttaxdtl (strStkOutCode,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode,strDataPostFlag)"
		    + " values ");
	    for (clsTaxCalculationDtls objTaxCalDtl : arrListTaxCal)
	    {
		if (objTaxCalDtl.getTaxAmount() > 0)
		{
		    flgSql = true;
		    sb.append("('" + objStockOutHd.getStrStkOutCode() + "','" + objTaxCalDtl.getTaxCode() + "','" + objTaxCalDtl.getTaxableAmount() + "'"
			    + ",'" + objTaxCalDtl.getTaxAmount() + "','" + clsGlobalVarClass.gClientCode + "','N'),");
		}
	    }

	    for (int cn = 0; cn < tblTaxCal.getRowCount(); cn++)
	    {
		flgSql = true;
		sb.append("('" + objStockOutHd.getStrStkOutCode() + "','" + tblTaxCal.getValueAt(cn, 2).toString() + "'"
			+ "," + Double.parseDouble(txtTotal.getText()) + "," + Double.parseDouble(tblTaxCal.getValueAt(cn, 1).toString()) + ""
			+ ",'" + clsGlobalVarClass.gClientCode + "','N'),");
	    }
	    if (flgSql)
	    {
		index = sb.lastIndexOf(",");
		sb = sb.delete(index, sb.length());
		rows = clsGlobalVarClass.dbMysql.execute(sb.toString());
	    }
	}

	return rows;
    }

    //Function to scroll item list in upward direction from table
    private void funUpButtonClicked()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int rowcount = tblItemTable.getRowCount();
	    selectedRow--;
	    if (selectedRow < 0)
	    {
		selectedRow = rowcount - 1;

		String itemName = tblItemTable.getValueAt(selectedRow, 1).toString();
		itemCode = tblItemTable.getValueAt(selectedRow, 0).toString();
		txtItemName.setText(itemName);
		txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow, 3).toString());
		txtQty.setText(tblItemTable.getValueAt(selectedRow, 2).toString());
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		//insertFlag=true;
	    }
	    else if (selectedRow == rowcount)
	    {
		selectedRow = 0;

		String itemName = tblItemTable.getValueAt(selectedRow, 1).toString();
		itemCode = tblItemTable.getValueAt(selectedRow, 0).toString();
		txtItemName.setText(itemName);
		txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow, 3).toString());
		txtQty.setText(tblItemTable.getValueAt(selectedRow, 2).toString());
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		//insertFlag=true;
	    }
	    else
	    {

		String itemName = tblItemTable.getValueAt(selectedRow, 1).toString();
		itemCode = tblItemTable.getValueAt(selectedRow, 0).toString();
		txtItemName.setText(itemName);
		txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow, 3).toString());
		txtQty.setText(tblItemTable.getValueAt(selectedRow, 2).toString());
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		//insertFlag=true;
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    //editMode=false;
	}
    }

    //Function to scroll item list in downward direction from table
    private void funDownButtonClicked()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int rowCount = tblItemTable.getRowCount();
	    if (selectedRow < rowCount - 1)
	    {

		tblItemTable.changeSelection(selectedRow + 1, 0, false, false);
		String itemName = tblItemTable.getValueAt(selectedRow + 1, 1).toString();
		itemCode = tblItemTable.getValueAt(selectedRow + 1, 0).toString();
		txtItemName.setText(itemName);
		txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow + 1, 3).toString());
		txtQty.setText(tblItemTable.getValueAt(selectedRow + 1, 2).toString());

		// insertFlag=true;
	    }
	    else if (selectedRow == rowCount - 1)
	    {
		selectedRow = 0;

		tblItemTable.changeSelection(selectedRow, 0, false, false);
		String itemName = tblItemTable.getValueAt(selectedRow, 1).toString();
		itemCode = tblItemTable.getValueAt(selectedRow, 0).toString();
		txtItemName.setText(itemName);
		txtPurchaseRate.setText(tblItemTable.getValueAt(selectedRow, 3).toString());
		txtQty.setText(tblItemTable.getValueAt(selectedRow, 2).toString());
		//insertFlag=true;
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    //editMode=false;
	}
    }

    /**
     * StockIn or StockOut Text Report
     *
     * @param type
     * @throws Exception
     */
    public void funStockInOutReport(String type, String stockInOutCode, String stockInOutDate) throws Exception
    {
	int count = 0;
	String posName = "";
	double grandTotal = 0.00, quantityTotal = 0.00, taxTotal = 0.00;
	new clsUtility().funCreateTempFolder();

	if (type.equals("StockIn"))
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp/Temp_StockInReport.txt");
	    PrintWriter pw = new PrintWriter(file);
	    funPrintBlankLines(clsGlobalVarClass.gClientName, pw);
	    funPrintBlankLines(clsGlobalVarClass.gClientAddress1, pw);
	    if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress2, pw);
	    }
	    if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress3, pw);
	    }
	    pw.println();
	    funPrintBlankLines("StockIn Slip", pw);
	    String sqlPos = " select b.strPosName from tblstkinhd a,tblposmaster b "
		    + " where a.strStkInCode='" + stockInOutCode + "' and a.strPOSCode=b.strPosCode ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlPos);
	    if (rs.next())
	    {
		posName = rs.getString(1);
	    }
	    pw.println();
	    pw.println();
	    pw.println("POS :" + posName);
	    String[] date = stockInOutDate.split("-");
	    String stockInDate = date[2] + "-" + date[1] + "-" + date[0];
	    pw.println("StockIn Date:" + stockInDate);
	    pw.println("StockIn No:" + stockInOutCode);
	    pw.println("---------------------------------------");

	    pw.println();
	    pw.println("Item Name");
	    pw.println("       Rate         Qty        Amount");
	    pw.println("---------------------------------------");

	    StringBuilder sqlStockInData = new StringBuilder();
	    sqlStockInData.append(" select a.strStkInCode,c.strItemName,a.strItemCode,a.dblQuantity,"
		    + " a.dblPurchaseRate,a.dblAmount,b.strPurchaseBillNo,date(b.dtePurchaseBillDate),d.strPosName"
		    + ",b.dblTaxAmt,b.dblGrandTotal "
		    + " from tblstkindtl a,tblstkinhd b,tblitemmaster c,tblposmaster d  "
		    + " where a.strStkInCode='" + stockInOutCode + "' and a.strStkInCode=b.strStkInCode "
		    + " and a.strItemCode=c.strItemCode and b.strPOSCode=d.strPosCode ");

	    ResultSet rsStockInData = clsGlobalVarClass.dbMysql.executeResultSet(sqlStockInData.toString());
	    while (rsStockInData.next())
	    {
		count++;
		pw.println(rsStockInData.getString(2));
		funPrintTextWithAlignment("right", rsStockInData.getString(5), 12, pw); // Rate
		funPrintTextWithAlignment("right", rsStockInData.getString(4), 12, pw); // Qty
		funPrintTextWithAlignment("right", rsStockInData.getString(6), 13, pw); // Amt
		pw.println();
		pw.println();
		quantityTotal += Double.parseDouble(rsStockInData.getString(4));
		grandTotal = rsStockInData.getDouble(11);
		taxTotal = rsStockInData.getDouble(10);
	    }
	    rsStockInData.close();

	    pw.println();
	    pw.println("---------------------------------------");
	    pw.print("TAX TOTAL");
	    funPrintTextWithAlignment("right", String.valueOf(Math.rint(taxTotal)), 27, pw);
	    pw.println();
	    pw.print("GRAND TOTAL");
	    funPrintTextWithAlignment("right", String.valueOf(Math.rint(quantityTotal)), 13, pw);
	    funPrintTextWithAlignment("right", String.valueOf(Math.rint(grandTotal)), 11, pw);
	    pw.println();
	    pw.println("---------------------------------------");
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

	    if (count > 0)
	    {
		funShowTextFile(file, "StockIn Report");
	    }
	}
	else
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp/Temp_StockOutReport.txt");
	    PrintWriter pw = new PrintWriter(file);
	    funPrintBlankLines(clsGlobalVarClass.gClientName, pw);
	    funPrintBlankLines(clsGlobalVarClass.gClientAddress1, pw);
	    if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress2, pw);
	    }
	    if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress3, pw);
	    }
	    pw.println();
	    funPrintBlankLines("StockOut Slip", pw);
	    String sqlPos = " select b.strPosName from tblstkouthd a,tblposmaster b "
		    + " where a.strStkOutCode='" + stockInOutCode + "' and a.strPOSCode=b.strPosCode ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlPos);
	    if (rs.next())
	    {
		posName = rs.getString(1);
	    }
	    pw.println();
	    pw.println();
	    pw.println("POS :" + posName);
	    String[] date = stockInOutDate.split("-");
	    String stockOutDate = date[2] + "-" + date[1] + "-" + date[0];
	    pw.println("StockOut Date:" + stockOutDate);
	    pw.println("StockOut No:" + stockInOutCode);
	    pw.println("---------------------------------------");

	    pw.println();
	    pw.println("Item Name");
	    pw.println("       Rate         Qty        Amount");
	    pw.println("---------------------------------------");

	    StringBuilder sqlStockOutData = new StringBuilder();
	    sqlStockOutData.append(" select a.strStkOutCode,c.strItemName,a.strItemCode,a.dblQuantity, "
		    + " a.dblPurchaseRate,a.dblAmount,b.strPurchaseBillNo,date(b.dtePurchaseBillDate),d.strPosName "
		    + " from tblstkoutdtl a,tblstkouthd b,tblitemmaster c,tblposmaster d "
		    + " where a.strStkOutCode='" + stockInOutCode + "' and a.strStkOutCode=b.strStkOutCode "
		    + " and a.strItemCode=c.strItemCode and b.strPOSCode=d.strPosCode ");

	    ResultSet rsStockOutData = clsGlobalVarClass.dbMysql.executeResultSet(sqlStockOutData.toString());
	    while (rsStockOutData.next())
	    {
		count++;
		pw.println(rsStockOutData.getString(2));
		funPrintTextWithAlignment("right", rsStockOutData.getString(5), 12, pw); // Rate
		funPrintTextWithAlignment("right", rsStockOutData.getString(4), 12, pw); // Qty
		funPrintTextWithAlignment("right", rsStockOutData.getString(6), 13, pw); // Amt
		pw.println();
		pw.println();
		quantityTotal += Double.parseDouble(rsStockOutData.getString(4));
		grandTotal += Double.parseDouble(rsStockOutData.getString(6));
	    }
	    rsStockOutData.close();

	    pw.println();
	    pw.println("---------------------------------------");
	    pw.print("GRAND TOTAL");
	    funPrintTextWithAlignment("right", String.valueOf(Math.rint(quantityTotal)), 13, pw);
	    funPrintTextWithAlignment("right", String.valueOf(Math.rint(grandTotal)), 11, pw);
	    pw.println();
	    pw.println("---------------------------------------");
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

	    if (count > 0)
	    {
		funShowTextFile(file, "StockOut Report");
	    }
	}
    }

    /**
     * Print blank Lines
     *
     * @param textToPrint
     * @param pw
     * @return
     */
    private int funPrintBlankLines(String textToPrint, PrintWriter pw)
    {
	pw.println();
	int len = 40 - textToPrint.length();
	len = len / 2;
	for (int cnt = 0; cnt < len; cnt++)
	{
	    pw.print(" ");
	}
	pw.print(textToPrint);
	return len;
    }

    /**
     * Print Text With Alignment
     *
     * @param align
     * @param textToPrint
     * @param totalLength
     * @param pw
     * @return
     */
    private int funPrintTextWithAlignment(String align, String textToPrint, int totalLength, PrintWriter pw)
    {
	int len = totalLength - textToPrint.length();
	for (int cnt = 0; cnt < len; cnt++)
	{
	    pw.print(" ");
	}

	DecimalFormat decFormat = new DecimalFormat("######.00");
	pw.print(decFormat.format(Double.parseDouble(textToPrint)));
	return 1;
    }

    /**
     * Show text file Method
     *
     * @param file
     * @param reportName
     */
    private void funShowTextFile(File file, String reportName)
    {
	try
	{
	    String data = "";
	    FileReader fread = new FileReader(file);
	    BufferedReader brText = new BufferedReader(fread);

	    String line = "";
	    while ((line = brText.readLine()) != null)
	    {
		data = data + line + "\n";
	    }
	    new com.POSGlobal.view.frmShowTextFile(data, reportName, file, "").setVisible(true);
	    fread.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetExternalCode(String code)
    {
	try
	{
	    String[] itemData = new String[7];
	    txtExtCode.setText(code);
	    String sql = "select strItemCode,strItemName,dblPurchaseRate from tblitemmaster where strExternalCode='" + code + "'";
	    ResultSet rsExt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsExt.next())
	    {
		itemData[0] = rsExt.getString(1);
		itemData[1] = rsExt.getString(2);
		itemData[2] = "";
		itemData[3] = "";
		itemData[4] = "";
		itemData[5] = rsExt.getString(3);
		itemData[6] = code;
		funSetData(itemData);
		txtQty.setText("1");
		txtQty.requestFocus();
		txtQty.selectAll();
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Invalid External Code");
		String exCode = txtExtCode.getText();
		StringBuilder sb = new StringBuilder(exCode);
		txtExtCode.setText(sb.substring(0, 2).toString());
		txtExtCode.requestFocus();
	    }
	    rsExt.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funOpenPOSearch()
    {
	try
	{
	    objUtility.funCallForSearchForm("POForStockIn");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		txtPOCode.setText(data[0].toString());
		txtPurchaseBillNo.setText(data[0].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillStockInWithPO() throws Exception
    {
	hmStockInDtl.clear();
	String POCode = txtPOCode.getText();
	String sql = "select a.strItemCode,a.dblOrderQty,a.dblPurchaseRate,a.dblAmount,b.strItemName "
		+ " from tblpurchaseorderdtl a,tblitemmaster b "
		+ " where a.strItemCode=b.strItemCode and a.strPOCode='" + POCode + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	ResultSet rsPODtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsPODtl.next())
	{
	    clsStockInDtl objStockInDtl = new clsStockInDtl();

	    objStockInDtl.setStrItemCode(rsPODtl.getString(1));
	    objStockInDtl.setStrItemName(rsPODtl.getString(5));
	    objStockInDtl.setStrStkInCode("");
	    objStockInDtl.setDblAmount(rsPODtl.getDouble(4));
	    objStockInDtl.setDblQuantity(rsPODtl.getDouble(2));
	    objStockInDtl.setDblPurchaseRate(rsPODtl.getDouble(3));
	    objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
	    objStockInDtl.setStrDataPostFlag("N");
	    hmStockInDtl.put(rsPODtl.getString(1), objStockInDtl);
	}

	funRefreshStockInOutGridTable();
    }

    private void funImportExportBtnClicked() throws Exception
    {
	if (btnImportExport.getText().equalsIgnoreCase("Export"))
	{
	    //funExportStockInExcelSheet();
	    funExportData();
	}
	else
	{
	    //funImportStockInData(selectedFileName);
	    if (btnImportExport.getText().equals("Browse"))
	    {
		funBrowseFile();
	    }
	    else if (lblStkNo.getText().equals("Stk In No."))
	    {
		funReadImportedExcelSheetStkIN(selectedFileName);
	    }
	    else
	    {
		funReadImportedExcelSheetStkOut();
	    }
	}
    }

    private void funBrowseFile()
    {
	String filePath = System.getProperty("user.dir");
	String fileName = filePath;
	File file = new File(fileName);

	if (file.exists())
	{
	    String fName = "";
	    // fileName = filePath + "/Temp/StockIn_Excel.xlsx";
	    fileName = filePath + "/Temp/";
	    file = new File(fileName);
	    if (file.exists())
	    {
		JFileChooser jfc = new JFileChooser(fileName);
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
		    File tempFile = jfc.getSelectedFile();
		    String excelFilePath = tempFile.getAbsolutePath();
		    fName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
		    System.out.println(fName);
		    selectedFileName = excelFilePath.substring(excelFilePath.lastIndexOf("/") + 1, excelFilePath.length());

		    if (!selectedFileName.isEmpty())
		    {
			btnImportExport.setToolTipText(selectedFileName);
		    }
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Export File for Stock In...");
		return;
	    }
	}
    }

    private void funImportStockInData(String fileName) throws Exception
    {
	hmStockInDtl.clear();

	FileInputStream excelFile = new FileInputStream(new File(fileName));
	Workbook workbook = new XSSFWorkbook(excelFile);
	Sheet datatypeSheet = workbook.getSheetAt(0);
	Iterator<Row> iterator = datatypeSheet.iterator();

	String billNo = "";
	while (iterator.hasNext())
	{

	    Row currentRow = iterator.next();

	    if (currentRow.getRowNum() == 0)
	    {
		if (null == currentRow.getCell(1) && currentRow.getCell(1).toString().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Enter bill no in excel sheet!!!");
		    break;
		}
		else
		{
		    billNo = currentRow.getCell(1).toString();
		    txtPurchaseBillNo.setText(billNo);
		}
	    }
	    else if (currentRow.getRowNum() > 1)
	    {
		String itemName = "";
		double qty = 0, purRate = 0;

		if (null == currentRow.getCell(0) && currentRow.getCell(0).toString().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Item Name not entered please check excel sheet at " + currentRow.getRowNum());
		    break;
		}
		else if (null == currentRow.getCell(1) && currentRow.getCell(1).toString().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Quantity not entered please check excel sheet at " + currentRow.getRowNum());
		    break;
		}
		else if (null == currentRow.getCell(2) && currentRow.getCell(2).toString().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Purchase Rate not entered please check excel sheet at " + currentRow.getRowNum());
		    break;
		}
		itemName = currentRow.getCell(0).getStringCellValue().trim();
		qty = currentRow.getCell(1).getNumericCellValue();
		purRate = currentRow.getCell(2).getNumericCellValue();

		if (hmItemMaster.containsKey(itemName))
		{
		    String itCode = hmItemMaster.get(itemName);
		    clsStockInDtl objStockInDtl = new clsStockInDtl();

		    objStockInDtl.setStrItemCode(itCode);
		    objStockInDtl.setStrItemName(itemName);
		    objStockInDtl.setStrStkInCode("");
		    objStockInDtl.setDblAmount(qty * purRate);
		    objStockInDtl.setDblQuantity(qty);
		    objStockInDtl.setDblPurchaseRate(purRate);
		    objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objStockInDtl.setStrDataPostFlag("N");
		    hmStockInDtl.put(itCode, objStockInDtl);
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Invalid item in excel sheet " + itemName);
		    break;
		}
	    }
	}

	funRefreshStockInOutGridTable();

	int res = JOptionPane.showConfirmDialog(null, "Do you want to update purchase rate in item master with excel sheet ?");
	if (res == 0)
	{
	    for (Map.Entry<String, clsStockInDtl> entry : hmStockInDtl.entrySet())
	    {
		String sqlUpdate = "update tblitemmaster set dblPurchaseRate = " + entry.getValue().getDblPurchaseRate() + " "
			+ " where strItemCode='" + entry.getKey() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		clsGlobalVarClass.dbMysql.execute(sqlUpdate);
	    }
	}

	funSaveStkIn();
    }

    private void funExportStockInExcelSheet() throws Exception
    {
	objUtility.funCreateTempFolder();
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + "/Temp/StockIn_Excel.xlsx");

	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("StockIn");

	Row row1 = sheet.createRow(0);
	Cell cellBillNoLbl = row1.createCell(0);
	cellBillNoLbl.setCellValue("BillNo");

	Row row2 = sheet.createRow(1);
	Cell cellItemName = row2.createCell(0);
	cellItemName.setCellValue("Item Name");

	Cell cellQty = row2.createCell(1);
	cellQty.setCellValue("Quantity");

	Cell cellPurchaseRate = row2.createCell(2);
	cellPurchaseRate.setCellValue("Purchase Rate");

	FileOutputStream outputStream = new FileOutputStream(file);
	workbook.write(outputStream);
	JOptionPane.showMessageDialog(null, "File Exported Successfully!!!");
    }

    private void funExportData()
    {
	try
	{
	    HSSFWorkbook hwb = new HSSFWorkbook();
	    HSSFSheet sheet = hwb.createSheet("new sheet");
	    HSSFRow rowhead = sheet.createRow((short) 1);

	    CellStyle style = hwb.createCellStyle();
	    Font font = hwb.createFont();
	    font.setFontName("Arial");
	    style.setFillForegroundColor(HSSFColor.BLUE.index);
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    font.setColor(HSSFColor.WHITE.index);
	    style.setFont(font);

	    rowhead.createCell((short) 0).setCellValue("Item Code");
	    rowhead.getCell(0).setCellStyle(style);
	    rowhead.createCell((short) 1).setCellValue("Item Name");
	    rowhead.getCell(1).setCellStyle(style);
	    rowhead.createCell((short) 2).setCellValue("Purchase Rate");
	    rowhead.getCell(2).setCellStyle(style);
	    rowhead.createCell((short) 3).setCellValue("Stock Balance");
	    rowhead.getCell(3).setCellStyle(style);
	    rowhead.createCell((short) 4).setCellValue("StockIn Qty");
	    rowhead.getCell(4).setCellStyle(style);

	    String sql = "select a.strItemCode,a.strItemName,b.dblPurchaseRate,a.intBalance, b.strRawMaterial"
		    + " from tblitemcurrentstk  a,tblitemmaster b "
		    + " where a.strItemCode=b.strItemCode and b.strRawMaterial='Y' "
		    + " order by a.strItemName ";       //a.strSubgroupName,
	    System.out.println("sql=" + sql);
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    int i = 3;

	    while (rs.next())
	    {
		HSSFRow row = sheet.createRow((short) i);
		row.createCell((short) 0).setCellValue(rs.getString(1));
		row.createCell((short) 1).setCellValue(rs.getString(2));
		row.createCell((short) 2).setCellValue(rs.getString(3));
		row.createCell((short) 3).setCellValue(rs.getString(4));
		row.createCell((short) 4).setCellValue("");
		i++;
	    }
	    rs.close();

	    objUtility.funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp/StockIn_Excel.xls");
	    FileOutputStream fileOut = new FileOutputStream(file);
	    hwb.write(fileOut);
	    fileOut.close();
	    Desktop dt = Desktop.getDesktop();
	    dt.open(file);

	    //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/CustomerData.xls");
	    rs.close();
	}
	catch (FileNotFoundException ex)
	{
	    JOptionPane.showMessageDialog(this, "File is already opened please close ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funReadImportedExcelSheetStkIN(String filePathTemp)
    {
	String itemName = "";
	double purRate = 0, stkInQty = 0;
	hmStockInDtl.clear();
	boolean flgStockInData = false;
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File filestk = new File(selectedFileName);
	    if (filestk.isFile())
	    {
		FileInputStream file = new FileInputStream(filestk);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet worksheet = workbook.getSheetAt(0);
		int i = 3;
		while (i <= worksheet.getLastRowNum())
		{
		    HSSFRow row = worksheet.getRow(i++);
		    //Sets the Read data to the model class
		    HSSFCell cell = row.getCell(3);

		    if (row.getCell(4) != null)
		    {
			if (!row.getCell(4).toString().isEmpty())
			{
			    flgStockInData = true;
			    String itCode = row.getCell(0).getStringCellValue();
			    itemName = row.getCell(1).getStringCellValue().trim();
			    purRate = Double.valueOf(row.getCell(2).getNumericCellValue()); //row.getCell(2).getNumericCellValue();
			    stkInQty = row.getCell(4).getNumericCellValue(); //row.getCell(4).getNumericCellValue();

			    if (hmItemMaster.containsKey(itemName))
			    {
				itCode = hmItemMaster.get(itemName);
			    }

			    clsStockInDtl objStockInDtl = new clsStockInDtl();

			    objStockInDtl.setStrItemCode(itCode);
			    objStockInDtl.setStrItemName(itemName);
			    objStockInDtl.setStrStkInCode("");
			    objStockInDtl.setDblAmount(stkInQty * purRate);
			    objStockInDtl.setDblQuantity(stkInQty);
			    objStockInDtl.setDblPurchaseRate(purRate);
			    objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			    objStockInDtl.setStrDataPostFlag("N");
			    hmStockInDtl.put(itCode, objStockInDtl);

			}
		    }
		}

		if (!flgStockInData)
		{
		    JOptionPane.showMessageDialog(null, "Please update stock quantity in excel sheet for atleast one item!!! ");
		}

		funRefreshStockInOutGridTable();
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Please Select File To Import...");
		return;
	    }
	}
	catch (Exception e)
	{
	    if (e.getMessage().trim().equals("Cannot get a numeric value from a text cell"))
	    {
		JOptionPane.showMessageDialog(this, "Invalid data entered in excel for " + itemName);
	    }
	    e.printStackTrace();
	}
    }

    public void funReadImportedExcelSheetStkOut()
    {
	String itemName = "";
	double purRate = 0, stkInQty = 0;
	hmStockOutDtl.clear();
	boolean flgStockInData = false;
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File filestk = new File(filePath + "/Temp/StockIn_Excel.xls");
	    if (filestk.isFile())
	    {
		FileInputStream file = new FileInputStream(filestk);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet worksheet = workbook.getSheetAt(0);
		int i = 3;
		while (i <= worksheet.getLastRowNum())
		{
		    HSSFRow row = worksheet.getRow(i++);
		    //Sets the Read data to the model class
		    HSSFCell cell = row.getCell(3);

		    if (row.getCell(4) != null)
		    {
			if (!row.getCell(4).toString().isEmpty())
			{
			    flgStockInData = true;
			    itemName = row.getCell(1).getStringCellValue().trim();
			    purRate = Double.valueOf(row.getCell(2).getStringCellValue()); //row.getCell(2).getNumericCellValue();
			    stkInQty = row.getCell(4).getNumericCellValue(); //row.getCell(4).getNumericCellValue();

			    if (hmItemMaster.containsKey(itemName))
			    {
				String itCode = hmItemMaster.get(itemName);
				clsStockOutDtl objStocOutDtl = new clsStockOutDtl();

				objStocOutDtl.setStrItemCode(itCode);
				objStocOutDtl.setStrItemName(itemName);
				objStocOutDtl.setStrStkOutCode("");
				objStocOutDtl.setDblAmount(stkInQty * purRate);
				objStocOutDtl.setDblQuantity(stkInQty);
				objStocOutDtl.setDblPurchaseRate(purRate);
				objStocOutDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				objStocOutDtl.setStrDataPostFlag("N");
				hmStockOutDtl.put(itCode, objStocOutDtl);
			    }
			    else
			    {
				JOptionPane.showMessageDialog(null, "Invalid item in excel sheet " + itemName);
				break;
			    }
			}
		    }
		}

		if (!flgStockInData)
		{
		    JOptionPane.showMessageDialog(null, "Please update stock quantity in excel sheet for atleast one item!!! ");
		}

		funRefreshStockInOutGridTable();
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Please Select File To Import...");
		return;
	    }
	}
	catch (Exception e)
	{
	    if (e.getMessage().equals("Cannot get a numeric value from a text cell"))
	    {
		JOptionPane.showMessageDialog(this, "Invalid data entered in excel for " + itemName);
	    }
	    e.printStackTrace();
	}

    }

    private void funOpenSupplierMasterSearch()
    {
	try
	{
	    objUtility.funCallForSearchForm("SupplierMaster");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		txtSuppCode.setText(data[0].toString());
		txtSupplierName.setText(data[1].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCalculateTax() throws Exception
    {
	DefaultTableModel dmTaxCal = (DefaultTableModel) tblTaxCal.getModel();
	dmTaxCal.setRowCount(0);
	String dtPOSDate = clsGlobalVarClass.gPOSOnlyDateForTransaction;
	double fixedTaxAmount = 0;
	StringBuilder sqlTaxCal = new StringBuilder();
	sqlTaxCal.setLength(0);
	sqlTaxCal.append("select strTaxCode,strTaxDesc,dblAmount from tbltaxhd where strTaxType='Fixed Amount' and strTaxOnSP='Purchase'"
		+ " and date(dteValidFrom) <='" + dtPOSDate + "' and date(dteValidTo)>='" + dtPOSDate + "' ");

	ResultSet rsTaxCal = dbMysql.executeResultSet(sqlTaxCal.toString());
	while (rsTaxCal.next())
	{
	    Object[] row =
	    {
		rsTaxCal.getString(2), rsTaxCal.getDouble(3), rsTaxCal.getString(1)
	    };
	    dmTaxCal.addRow(row);
	    fixedTaxAmount += rsTaxCal.getDouble(3);
	}
	rsTaxCal.close();

	lblTotalFixedTax.setText(String.valueOf(fixedTaxAmount));
	double grandTotal = globalGrandTotal + dynamicTaxAmt + (Double.parseDouble(lblTotalFixedTax.getText()));
	lblTotalTax.setText(String.valueOf(dynamicTaxAmt + Double.parseDouble(lblTotalFixedTax.getText())));
	txtTotal.setText(String.valueOf(Math.rint(grandTotal)));

	tblTaxCal.setRowHeight(25);
	tblTaxCal.setModel(dmTaxCal);

	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.LEFT);
	tblTaxCal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
    }

    private void funUpdateFixedTaxTotal()
    {
	try
	{

	    double totalFixedTaxAmt = 0;
	    for (int cn = 0; cn < tblTaxCal.getRowCount(); cn++)
	    {
		if (!tblTaxCal.getValueAt(cn, 1).toString().trim().isEmpty())
		{
		    totalFixedTaxAmt += Double.parseDouble(tblTaxCal.getValueAt(cn, 1).toString());
		}
	    }

	    lblTotalFixedTax.setText(String.valueOf(totalFixedTaxAmt));
	    double grandTotal = globalGrandTotal + dynamicTaxAmt + (Double.parseDouble(lblTotalFixedTax.getText()));
	    lblTotalTax.setText(String.valueOf(dynamicTaxAmt + Double.parseDouble(lblTotalFixedTax.getText())));
	    txtTotal.setText(String.valueOf(Math.rint(grandTotal)));

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

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
        panelMainForm = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        tabDataEntry = new javax.swing.JTabbedPane();
        panelFormBody = new javax.swing.JPanel();
        panelOperationalButtons = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnModifyStk = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnDelItem = new javax.swing.JButton();
        btnPopulateItem = new javax.swing.JButton();
        panelItemDtlGrid = new javax.swing.JPanel();
        scrItemGrid = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblPaxNo = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        lblTotalTax = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        lblTotalQty = new javax.swing.JLabel();
        lblQty = new javax.swing.JLabel();
        panelMenuItem = new javax.swing.JPanel();
        lblStkInCode = new javax.swing.JLabel();
        lblStkDate = new javax.swing.JLabel();
        lblStkNo = new javax.swing.JLabel();
        cmbReason = new javax.swing.JComboBox();
        txtItemName = new javax.swing.JTextField();
        lblRateQty = new javax.swing.JLabel();
        txtPurchaseRate = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnMenuItem = new javax.swing.JButton();
        btnPLU = new javax.swing.JButton();
        txtPurchaseBillNo = new javax.swing.JTextField();
        lblItemName = new javax.swing.JLabel();
        lblReason = new javax.swing.JLabel();
        lblBillNo = new javax.swing.JLabel();
        lblRateQty1 = new javax.swing.JLabel();
        dtePurchaseBillDate = new com.toedter.calendar.JDateChooser();
        lblExternalCode = new javax.swing.JLabel();
        txtExtCode = new javax.swing.JTextField();
        lblAgainst = new javax.swing.JLabel();
        txtPOCode = new javax.swing.JTextField();
        btnFill = new javax.swing.JButton();
        btnImportExport = new javax.swing.JButton();
        cmbOperationType = new javax.swing.JComboBox();
        txtSuppCode = new javax.swing.JTextField();
        txtSupplierName = new javax.swing.JTextField();
        lblSupplierName = new javax.swing.JLabel();
        panelTax = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTaxCal = new javax.swing.JTable();
        lblTotalFixedTax = new javax.swing.JLabel();
        lblTotalFixTax = new javax.swing.JLabel();
        btnCalTax = new javax.swing.JButton();
        btnResetTaxGrid = new javax.swing.JButton();
        btnCalTax1 = new javax.swing.JButton();

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
        lblformName.setText("- Stock In");
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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelOperationalButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelOperationalButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelOperationalButtons.setOpaque(false);

        btnDone.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDone.setForeground(new java.awt.Color(255, 255, 255));
        btnDone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDone.setText("DONE");
        btnDone.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDone.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDoneActionPerformed(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setText("HOME");
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeActionPerformed(evt);
            }
        });

        btnModifyStk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnModifyStk.setForeground(new java.awt.Color(255, 255, 255));
        btnModifyStk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnModifyStk.setText("MODIFY STOCKIN");
        btnModifyStk.setBorder(null);
        btnModifyStk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModifyStk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnModifyStk.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnModifyStkMouseClicked(evt);
            }
        });
        btnModifyStk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModifyStkActionPerformed(evt);
            }
        });

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnUp.setText("UP");
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnUp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUpMouseClicked(evt);
            }
        });
        btnUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDown.setText("DOWN");
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownMouseClicked(evt);
            }
        });

        btnDelItem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDelItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDelItem.setText("DELETE");
        btnDelItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDelItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelItemMouseClicked(evt);
            }
        });

        btnPopulateItem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPopulateItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPopulateItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopulateItem.setText("POPULATE");
        btnPopulateItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopulateItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPopulateItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopulateItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperationalButtonsLayout = new javax.swing.GroupLayout(panelOperationalButtons);
        panelOperationalButtons.setLayout(panelOperationalButtonsLayout);
        panelOperationalButtonsLayout.setHorizontalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPopulateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnModifyStk, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDone, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelOperationalButtonsLayout.setVerticalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationalButtonsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                        .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnModifyStk, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(1, 1, 1))
                    .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPopulateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );

        panelItemDtlGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtlGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtlGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtlGrid.setOpaque(false);
        panelItemDtlGrid.setPreferredSize(new java.awt.Dimension(260, 600));
        panelItemDtlGrid.setLayout(null);

        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ItemCode", "Description", "Qty", "Rate", "Amount", "Title 6"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblItemTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblItemTable.setRowHeight(30);
        tblItemTable.setShowVerticalLines(false);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrItemGrid.setViewportView(tblItemTable);

        panelItemDtlGrid.add(scrItemGrid);
        scrItemGrid.setBounds(0, 0, 400, 450);
        panelItemDtlGrid.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");
        panelItemDtlGrid.add(lblTotal);
        lblTotal.setBounds(260, 460, 60, 30);

        lblTax.setText("Total Tax");
        panelItemDtlGrid.add(lblTax);
        lblTax.setBounds(120, 460, 50, 30);

        lblTotalTax.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTax.setText("0.00");
        panelItemDtlGrid.add(lblTotalTax);
        lblTotalTax.setBounds(180, 460, 60, 30);

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(txtTotal);
        txtTotal.setBounds(310, 460, 80, 30);

        lblTotalQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        panelItemDtlGrid.add(lblTotalQty);
        lblTotalQty.setBounds(60, 460, 50, 30);

        lblQty.setText("Total Qty");
        panelItemDtlGrid.add(lblQty);
        lblQty.setBounds(10, 460, 50, 30);

        panelMenuItem.setBackground(new java.awt.Color(255, 255, 255));
        panelMenuItem.setEnabled(false);
        panelMenuItem.setOpaque(false);
        panelMenuItem.setLayout(null);

        lblStkInCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        panelMenuItem.add(lblStkInCode);
        lblStkInCode.setBounds(100, 10, 140, 30);
        panelMenuItem.add(lblStkDate);
        lblStkDate.setBounds(250, 10, 138, 30);

        lblStkNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStkNo.setText("Stk In No.");
        panelMenuItem.add(lblStkNo);
        lblStkNo.setBounds(20, 10, 80, 30);

        panelMenuItem.add(cmbReason);
        cmbReason.setBounds(100, 140, 300, 30);

        txtItemName.setEditable(false);
        txtItemName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemNameMouseClicked(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemNameKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtItemName);
        txtItemName.setBounds(100, 220, 290, 30);

        lblRateQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRateQty.setText("Quantity");
        panelMenuItem.add(lblRateQty);
        lblRateQty.setBounds(240, 260, 50, 28);

        txtPurchaseRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPurchaseRate.setText("0.00");
        txtPurchaseRate.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtPurchaseRateFocusGained(evt);
            }
        });
        txtPurchaseRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPurchaseRateMouseClicked(evt);
            }
        });
        txtPurchaseRate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPurchaseRateActionPerformed(evt);
            }
        });
        txtPurchaseRate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPurchaseRateKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtPurchaseRate);
        txtPurchaseRate.setBounds(120, 300, 90, 28);

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setText("1");
        txtQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtQtyMouseClicked(evt);
            }
        });
        txtQty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQtyActionPerformed(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtQtyKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtQty);
        txtQty.setBounds(300, 260, 90, 28);

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });
        panelMenuItem.add(btnOK);
        btnOK.setBounds(50, 460, 90, 42);

        btnMenuItem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnMenuItem.setForeground(new java.awt.Color(255, 255, 255));
        btnMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenuItem.setText("MENU ITEM");
        btnMenuItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenuItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenuItemActionPerformed(evt);
            }
        });
        panelMenuItem.add(btnMenuItem);
        btnMenuItem.setBounds(160, 460, 120, 42);

        btnPLU.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPLU.setForeground(new java.awt.Color(255, 255, 255));
        btnPLU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPLU.setText("PLU");
        btnPLU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPLU.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPLU.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPLUActionPerformed(evt);
            }
        });
        panelMenuItem.add(btnPLU);
        btnPLU.setBounds(300, 460, 90, 40);

        txtPurchaseBillNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPurchaseBillNoMouseClicked(evt);
            }
        });
        txtPurchaseBillNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPurchaseBillNoKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtPurchaseBillNo);
        txtPurchaseBillNo.setBounds(100, 180, 130, 30);

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemName.setText("Item Name");
        panelMenuItem.add(lblItemName);
        lblItemName.setBounds(21, 220, 70, 30);

        lblReason.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReason.setText("Reason");
        panelMenuItem.add(lblReason);
        lblReason.setBounds(20, 140, 60, 30);

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillNo.setText("Bill No");
        panelMenuItem.add(lblBillNo);
        lblBillNo.setBounds(20, 180, 60, 30);

        lblRateQty1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRateQty1.setText("Purchase Rate");
        panelMenuItem.add(lblRateQty1);
        lblRateQty1.setBounds(20, 300, 90, 28);

        dtePurchaseBillDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                dtePurchaseBillDateMouseClicked(evt);
            }
        });
        dtePurchaseBillDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dtePurchaseBillDateKeyPressed(evt);
            }
        });
        panelMenuItem.add(dtePurchaseBillDate);
        dtePurchaseBillDate.setBounds(250, 180, 140, 30);

        lblExternalCode.setText("External Code");
        panelMenuItem.add(lblExternalCode);
        lblExternalCode.setBounds(20, 260, 80, 30);

        txtExtCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExtCodeMouseClicked(evt);
            }
        });
        txtExtCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExtCodeKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtExtCode);
        txtExtCode.setBounds(110, 260, 120, 30);

        lblAgainst.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAgainst.setText("PO Code    ");
        panelMenuItem.add(lblAgainst);
        lblAgainst.setBounds(20, 100, 80, 30);

        txtPOCode.setEditable(false);
        txtPOCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPOCodeMouseClicked(evt);
            }
        });
        panelMenuItem.add(txtPOCode);
        txtPOCode.setBounds(100, 100, 140, 30);

        btnFill.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFill.setForeground(new java.awt.Color(255, 255, 255));
        btnFill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnFill.setText("FILL");
        btnFill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFillActionPerformed(evt);
            }
        });
        panelMenuItem.add(btnFill);
        btnFill.setBounds(250, 100, 70, 30);

        btnImportExport.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnImportExport.setForeground(new java.awt.Color(255, 255, 255));
        btnImportExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnImportExport.setText("EXPORT");
        btnImportExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnImportExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImportExportActionPerformed(evt);
            }
        });
        panelMenuItem.add(btnImportExport);
        btnImportExport.setBounds(310, 300, 80, 30);

        cmbOperationType.setBackground(new java.awt.Color(51, 102, 255));
        cmbOperationType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbOperationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Export", "Browse", "Import" }));
        cmbOperationType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbOperationTypeMouseClicked(evt);
            }
        });
        cmbOperationType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperationTypeActionPerformed(evt);
            }
        });
        panelMenuItem.add(cmbOperationType);
        cmbOperationType.setBounds(220, 300, 80, 30);

        txtSuppCode.setEditable(false);
        txtSuppCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSuppCodeMouseClicked(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSuppCodeKeyPressed(evt);
            }
        });
        panelMenuItem.add(txtSuppCode);
        txtSuppCode.setBounds(100, 50, 100, 30);

        txtSupplierName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSupplierNameMouseClicked(evt);
            }
        });
        txtSupplierName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtSupplierNameActionPerformed(evt);
            }
        });
        panelMenuItem.add(txtSupplierName);
        txtSupplierName.setBounds(200, 50, 200, 30);

        lblSupplierName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSupplierName.setText("Supplier  :");
        panelMenuItem.add(lblSupplierName);
        lblSupplierName.setBounds(20, 50, 70, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelMenuItem, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelMenuItem, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        tabDataEntry.addTab("Stock In", panelFormBody);

        tblTaxCal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Tax Name", "Tax Amount", "Tax Code"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTaxCal.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblTaxCalKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblTaxCal);
        if (tblTaxCal.getColumnModel().getColumnCount() > 0)
        {
            tblTaxCal.getColumnModel().getColumn(2).setMinWidth(1);
            tblTaxCal.getColumnModel().getColumn(2).setPreferredWidth(1);
            tblTaxCal.getColumnModel().getColumn(2).setMaxWidth(1);
        }

        lblTotalFixedTax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotalFixedTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFixedTax.setText("0.00");

        lblTotalFixTax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotalFixTax.setText("Tax Total");

        btnCalTax.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnCalTax.setForeground(new java.awt.Color(255, 255, 255));
        btnCalTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCalTax.setText("CALCULATE TAX");
        btnCalTax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalTax.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCalTax.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalTaxActionPerformed(evt);
            }
        });

        btnResetTaxGrid.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnResetTaxGrid.setForeground(new java.awt.Color(255, 255, 255));
        btnResetTaxGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnResetTaxGrid.setText("RESET");
        btnResetTaxGrid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetTaxGrid.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnResetTaxGrid.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetTaxGridActionPerformed(evt);
            }
        });

        btnCalTax1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnCalTax1.setForeground(new java.awt.Color(255, 255, 255));
        btnCalTax1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCalTax1.setText("UPDATE TOTAL");
        btnCalTax1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalTax1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCalTax1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalTax1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTaxLayout = new javax.swing.GroupLayout(panelTax);
        panelTax.setLayout(panelTaxLayout);
        panelTaxLayout.setHorizontalGroup(
            panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaxLayout.createSequentialGroup()
                .addGroup(panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTaxLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotalFixTax, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211)
                        .addComponent(lblTotalFixedTax, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTaxLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelTaxLayout.createSequentialGroup()
                                .addComponent(btnCalTax, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnResetTaxGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCalTax1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        panelTaxLayout.setVerticalGroup(
            panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalTax1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetTaxGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalTax, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalFixTax, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFixedTax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(171, Short.MAX_VALUE))
        );

        tabDataEntry.addTab("Calculate tax", panelTax);

        panelMainForm.add(tabDataEntry, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
	// TODO add your handling code here:
	if (lblStkNo.getText().equals("Stk In No."))
	{
	    funSaveStkIn();
	}
	else
	{
	    funSaveStkOut();
	}

    }//GEN-LAST:event_btnDoneActionPerformed

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
	// TODO add your handling code here:     
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnModifyStkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModifyStkMouseClicked
	// TODO add your handling code here:
	funModifyStockButtonClicked();
    }//GEN-LAST:event_btnModifyStkMouseClicked

    private void btnModifyStkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyStkActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnModifyStkActionPerformed

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
	// TODO add your handling code here:     
    }//GEN-LAST:event_btnUpMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
	// TODO add your handling code here:
	funUpButtonClicked();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
	// TODO add your handling code here:
	funDownButtonClicked();
    }//GEN-LAST:event_btnDownMouseClicked

    private void btnDelItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelItemMouseClicked
	// TODO add your handling code here:
	funDeleteStockInOutItem();
    }//GEN-LAST:event_btnDelItemMouseClicked

    private void btnPopulateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopulateItemActionPerformed
	// TODO add your handling code here:        
    }//GEN-LAST:event_btnPopulateItemActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
	funTableRowClicked();
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtItemNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemNameMouseClicked
	funItemCodeTextFieldClicked();
    }//GEN-LAST:event_txtItemNameMouseClicked

    private void txtPurchaseRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPurchaseRateMouseClicked

	frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Purchase Rate");
	num.setVisible(true);
	if (null != clsGlobalVarClass.gNumerickeyboardValue)
	{
	    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
	    {
		txtPurchaseRate.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}

	if (!numFlag)
	{
	    textValue = "";
	}
	numFlag = true;
    }//GEN-LAST:event_txtPurchaseRateMouseClicked

    private void txtPurchaseRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPurchaseRateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtPurchaseRate.getText().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "purchase rate cannot be blank");
	    }
	    else
	    {
		funFillStockInOutGridTable();
		funClearFields();
	    }
	}
    }//GEN-LAST:event_txtPurchaseRateKeyPressed

    private void txtQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtQtyMouseClicked

	frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Quntiry");
	num.setVisible(true);
	if (null != clsGlobalVarClass.gNumerickeyboardValue)
	{
	    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
	    {
		txtQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}

	// TODO add your handling code here:
	if (numFlag)
	{
	    textValue = "";
	}
	numFlag = false;
    }//GEN-LAST:event_txtQtyMouseClicked

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtQty.getText().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Quantity cannot be blank");
	    }
	}
    }//GEN-LAST:event_txtQtyKeyPressed

    private void btnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuItemActionPerformed
	// TODO add your handling code here:
	//new frmMenuItemMaster().setVisible(true);
    }//GEN-LAST:event_btnMenuItemActionPerformed

    private void btnPLUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPLUActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnPLUActionPerformed

    private void txtPurchaseBillNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPurchaseBillNoMouseClicked

	if (txtPurchaseBillNo.getText().length() == 0)
	{
	    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Purchase Bill No").setVisible(true);
	    txtPurchaseBillNo.setText(clsGlobalVarClass.gKeyboardValue);
	}
	else
	{
	    new frmAlfaNumericKeyBoard(this, true, txtPurchaseBillNo.getText(), "1", "Enter Purchase Bill No").setVisible(true);
	    txtPurchaseBillNo.setText(clsGlobalVarClass.gKeyboardValue);
	}

    }//GEN-LAST:event_txtPurchaseBillNoMouseClicked

    private void txtExtCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExtCodeMouseClicked
	// TODO add your handling code here:
	if (txtExtCode.getText().length() == 0)
	{
	    new frmAlfaNumericKeyBoard(this, true, "1", "Enter External Code of Item").setVisible(true);
	    txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
	}
	else
	{
	    new frmAlfaNumericKeyBoard(this, true, txtExtCode.getText(), "1", "Enter External Code of Item").setVisible(true);
	    txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
	}
    }//GEN-LAST:event_txtExtCodeMouseClicked

    private void txtExtCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtCodeKeyPressed
	// TODO add your handling code here:

	try
	{
	    System.out.println("Key= " + evt.getKeyCode());
	    if (evt.getKeyCode() == 10)
	    {
		funSetExternalCode(txtExtCode.getText());
	    }
	    else if (evt.getKeyCode() == 47)
	    {
		funItemCodeTextFieldClicked();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }//GEN-LAST:event_txtExtCodeKeyPressed

    private void txtPurchaseBillNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPurchaseBillNoKeyPressed
	// TODO add your handling code here:        
	if (evt.getKeyCode() == 10)
	{
	    dtePurchaseBillDate.requestFocusInWindow();
	}
    }//GEN-LAST:event_txtPurchaseBillNoKeyPressed

    private void dtePurchaseBillDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dtePurchaseBillDateMouseClicked
	// TODO add your handling code here:

    }//GEN-LAST:event_dtePurchaseBillDateMouseClicked

    private void dtePurchaseBillDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtePurchaseBillDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == KeyEvent.VK_ENTER)
	{
	    txtItemName.requestFocus();
	}
    }//GEN-LAST:event_dtePurchaseBillDateKeyPressed

    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funItemCodeTextFieldClicked();
	}
	if (evt.getKeyCode() == 10)
	{
	    txtQty.requestFocus();
	    txtQty.selectAll();
	    //evt.setKeyCode(666);
	}

    }//GEN-LAST:event_txtItemNameKeyPressed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
	// TODO add your handling code here:
	funHomeButtonClicked();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void txtPurchaseRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseRateFocusGained
	// TODO add your handling code here:

    }//GEN-LAST:event_txtPurchaseRateFocusGained

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
	// TODO add your handling code here:
	funFillStockInOutGridTable();
    }//GEN-LAST:event_btnOKActionPerformed

    private void txtPurchaseRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchaseRateActionPerformed


    }//GEN-LAST:event_txtPurchaseRateActionPerformed

    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
	// TODO add your handling code here:

	txtPurchaseRate.requestFocus();
	txtPurchaseRate.selectAll();
    }//GEN-LAST:event_txtQtyActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Stock In");
	clsGlobalVarClass.hmActiveForms.remove("Stock Out");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Stock In");
	clsGlobalVarClass.hmActiveForms.remove("Stock Out");
    }//GEN-LAST:event_formWindowClosing

    private void btnFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFillActionPerformed
	// TODO add your handling code here:
	try
	{
	    funFillStockInWithPO();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }//GEN-LAST:event_btnFillActionPerformed

    private void txtPOCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPOCodeMouseClicked
	// TODO add your handling code here:
	funOpenPOSearch();
    }//GEN-LAST:event_txtPOCodeMouseClicked

    private void btnImportExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportExportActionPerformed
	// TODO add your handling code here:
	try
	{
	    funImportExportBtnClicked();
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }//GEN-LAST:event_btnImportExportActionPerformed

    private void cmbOperationTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbOperationTypeMouseClicked
	// TODO add your handling code here:

    }//GEN-LAST:event_cmbOperationTypeMouseClicked

    private void cmbOperationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationTypeActionPerformed
	// TODO add your handling code here:
	if (cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("Export"))
	{
	    btnImportExport.setText("Export");
	}
	else if (cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("Browse"))
	{
	    btnImportExport.setText("Browse");
	}
	else
	{
	    btnImportExport.setText("Import");
	}
    }//GEN-LAST:event_cmbOperationTypeActionPerformed

    private void btnCalTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalTaxActionPerformed
	// TODO add your handling code here:
	try
	{
	    funCalculateTax();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnCalTaxActionPerformed

    private void btnResetTaxGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTaxGridActionPerformed
	funTaxCalculateResetButtonClicked();
    }//GEN-LAST:event_btnResetTaxGridActionPerformed

    private void txtSuppCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSuppCodeMouseClicked
	// TODO add your handling code here:
	funOpenSupplierMasterSearch();
    }//GEN-LAST:event_txtSuppCodeMouseClicked

    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funOpenSupplierMasterSearch();
	}
    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void txtSupplierNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupplierNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierNameMouseClicked

    private void txtSupplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierNameActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierNameActionPerformed

    private void tblTaxCalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTaxCalKeyPressed
	// TODO add your handling code here:


    }//GEN-LAST:event_tblTaxCalKeyPressed

    private void btnCalTax1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalTax1ActionPerformed
	// TODO add your handling code here:
	funUpdateFixedTaxTotal();
    }//GEN-LAST:event_btnCalTax1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalTax;
    private javax.swing.JButton btnCalTax1;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnFill;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnImportExport;
    private javax.swing.JButton btnMenuItem;
    private javax.swing.JButton btnModifyStk;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPLU;
    private javax.swing.JButton btnPopulateItem;
    private javax.swing.JButton btnResetTaxGrid;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox cmbOperationType;
    private javax.swing.JComboBox cmbReason;
    private com.toedter.calendar.JDateChooser dtePurchaseBillDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAgainst;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblExternalCode;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblRateQty;
    private javax.swing.JLabel lblRateQty1;
    private javax.swing.JLabel lblReason;
    private javax.swing.JLabel lblStkDate;
    private javax.swing.JLabel lblStkInCode;
    private javax.swing.JLabel lblStkNo;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalFixTax;
    private javax.swing.JLabel lblTotalFixedTax;
    private javax.swing.JLabel lblTotalQty;
    private javax.swing.JLabel lblTotalTax;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtlGrid;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelMenuItem;
    private javax.swing.JPanel panelOperationalButtons;
    private javax.swing.JPanel panelTax;
    private javax.swing.JScrollPane scrItemGrid;
    private javax.swing.JTabbedPane tabDataEntry;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblTaxCal;
    private javax.swing.JTextField txtExtCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPOCode;
    private javax.swing.JTextField txtPurchaseBillNo;
    private javax.swing.JTextField txtPurchaseRate;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    private void funClearFields()
    {
	String exCode = txtExtCode.getText();
	if (exCode.length() > 2)
	{
	    StringBuilder sb = new StringBuilder(exCode);
	    txtExtCode.setText(sb.substring(0, 2).toString());
	}
	txtItemName.setText("");
	txtQty.setText("0.00");
	txtPurchaseRate.setText("0.00");
	txtExtCode.requestFocus();
	txtPOCode.setText("");
    }

    //Function to fill hashmap for stockIn and stockOut GridTable
    private void funFillStockInOutGridTable()
    {
	try
	{
	    if (txtItemName.getText().isEmpty())
	    {
		JOptionPane.showConfirmDialog(null, "Please Select Item");
		return;
	    }

	    if (Double.parseDouble(txtQty.getText()) == 0)
	    {
		JOptionPane.showConfirmDialog(null, "Please Enter Quantity");
		return;
	    }

	    if (lblStkNo.getText().equals("Stk In No."))
	    {
		clsStockInDtl objStockInDtl = new clsStockInDtl();
		if (null != hmStockInDtl.get(itemCode))
		{
		    objStockInDtl = hmStockInDtl.get(itemCode);
		    if (itemSelection.equalsIgnoreCase("from grid"))
		    {
			objStockInDtl.setDblQuantity(Double.parseDouble(txtQty.getText()));
			objStockInDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * objStockInDtl.getDblQuantity());
			objStockInDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    }
		    else
		    {
			objStockInDtl.setDblQuantity(objStockInDtl.getDblQuantity() + Double.parseDouble(txtQty.getText()));
			objStockInDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * objStockInDtl.getDblQuantity());
			objStockInDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    }
		}
		else
		{
		    objStockInDtl.setStrItemCode(itemCode);
		    objStockInDtl.setStrItemName(txtItemName.getText());
		    objStockInDtl.setStrStkInCode("");
		    objStockInDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * Double.parseDouble(txtQty.getText()));
		    objStockInDtl.setDblQuantity(Double.parseDouble(txtQty.getText()));
		    objStockInDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objStockInDtl.setStrDataPostFlag("N");
		}
		hmStockInDtl.put(itemCode, objStockInDtl);
	    }
	    else
	    {
		clsStockOutDtl objStockOutDtl = new clsStockOutDtl();

		if (null != hmStockOutDtl.get(itemCode))
		{
		    if (itemSelection.equalsIgnoreCase("from grid"))
		    {
			objStockOutDtl.setDblQuantity(Double.parseDouble(txtQty.getText()));
			objStockOutDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * objStockOutDtl.getDblQuantity());
			objStockOutDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    }
		    else
		    {
			objStockOutDtl.setDblQuantity(objStockOutDtl.getDblQuantity() + Double.parseDouble(txtQty.getText()));
			objStockOutDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * objStockOutDtl.getDblQuantity());
			objStockOutDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    }
		}
		else
		{
		    objStockOutDtl.setStrItemCode(itemCode);
		    objStockOutDtl.setStrItemName(txtItemName.getText());
		    objStockOutDtl.setStrStkOutCode("");
		    objStockOutDtl.setDblAmount(Double.parseDouble(txtPurchaseRate.getText()) * Double.parseDouble(txtQty.getText()));
		    objStockOutDtl.setDblQuantity(Double.parseDouble(txtQty.getText()));
		    objStockOutDtl.setDblPurchaseRate(Double.parseDouble(txtPurchaseRate.getText()));
		    objStockOutDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objStockOutDtl.setStrDataPostFlag("N");
		}
		hmStockOutDtl.put(itemCode, objStockOutDtl);
	    }
	    itemCode = "";
	    funRefreshStockInOutGridTable();
	    textValue = "";
	    itemSelection = "";
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

//Function to refresh and fill stockIn and stockOut GridTable
    private void funRefreshStockInOutGridTable()
    {
	try
	{
	    DefaultTableModel dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.addColumn("ItemCode");
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Purchase Rate");
	    dm.addColumn("Amount");
	    double qtyTotal = 0.00;
	    double subTotal = 0, taxAmt = 0, grandTotal = 0;

	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    if (lblStkNo.getText().equals("Stk In No."))
	    {
		for (Map.Entry<String, clsStockInDtl> entry : hmStockInDtl.entrySet())
		{
		    Object[] row =
		    {
			entry.getValue().getStrItemCode(), entry.getValue().getStrItemName(), entry.getValue().getDblQuantity(), entry.getValue().getDblPurchaseRate(), entry.getValue().getDblAmount()
		    };
		    dm.addRow(row);
		    qtyTotal += entry.getValue().getDblQuantity();
		    subTotal += entry.getValue().getDblAmount();

		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(entry.getValue().getStrItemCode());
		    objItemDtl.setItemName(entry.getValue().getStrItemName());
		    objItemDtl.setAmount(entry.getValue().getDblAmount());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);
		}
	    }
	    else
	    {
		for (Map.Entry<String, clsStockOutDtl> entry : hmStockOutDtl.entrySet())
		{
		    Object[] row =
		    {
			entry.getValue().getStrItemCode(), entry.getValue().getStrItemName(), entry.getValue().getDblQuantity(), entry.getValue().getDblPurchaseRate(), entry.getValue().getDblAmount()
		    };
		    dm.addRow(row);
		    qtyTotal += entry.getValue().getDblQuantity();
		    subTotal += entry.getValue().getDblAmount();

		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(entry.getValue().getStrItemCode());
		    objItemDtl.setItemName(entry.getValue().getStrItemName());
		    objItemDtl.setAmount(entry.getValue().getDblAmount());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);
		}
	    }

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSOnlyDateForTransaction, "", "", subTotal, 0.00, "", "", "Purchase");
	    for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
	    {
		if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		{
		    taxAmt = taxAmt + objTaxDtl.getTaxAmount();
		    Object[] taxTotalRow =
		    {
			objTaxDtl.getTaxName(), "", Math.rint(objTaxDtl.getTaxAmount())
		    };
		}
	    }

	    double fixedTaxAmount = 0;
	    if (!lblStkInCode.getText().trim().isEmpty())
	    {
		DefaultTableModel dmTaxCal = (DefaultTableModel) tblTaxCal.getModel();
		dmTaxCal.setRowCount(0);

		String sql = "select a.strStkInCode,a.strTaxCode,b.strTaxDesc,a.dblTaxableAmt,a.dblTaxAmt "
			+ " from tblstkintaxdtl a,tbltaxhd b "
			+ " where a.strTaxCode=b.strTaxCode and b.strTaxOnSP='Purchase' and b.strTaxType='Fixed Amount' "
			+ "and a.strStkInCode='" + lblStkInCode.getText().trim() + "'";

		if (!lblStkNo.getText().equals("Stk In No."))
		{
		    sql = "select a.strStkInCode,a.strTaxCode,b.strTaxDesc,a.dblTaxableAmt,a.dblTaxAmt "
			    + " from tblstkouttaxdtl a,tbltaxhd b "
			    + " where a.strTaxCode=b.strTaxCode and b.strTaxOnSP='Purchase' and b.strTaxType='Fixed Amount' "
			    + "and a.strStkInCode='" + lblStkInCode.getText().trim() + "'";
		}

		ResultSet rsFixedTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsFixedTax.next())
		{
		    Object[] row =
		    {
			rsFixedTax.getString(3), rsFixedTax.getDouble(5), rsFixedTax.getString(2)
		    };
		    dmTaxCal.addRow(row);
		    fixedTaxAmount += rsFixedTax.getDouble(5);
		}
		rsFixedTax.close();

		tblTaxCal.setRowHeight(25);
		tblTaxCal.setModel(dmTaxCal);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.LEFT);
		tblTaxCal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    }

	    lblTotalFixedTax.setText(String.valueOf(fixedTaxAmount));
	    globalGrandTotal = subTotal;
	    grandTotal = subTotal + taxAmt + (Double.parseDouble(lblTotalFixedTax.getText()));
	    lblTotalQty.setText(String.valueOf(qtyTotal));
	    lblTotalTax.setText(String.valueOf(taxAmt + Double.parseDouble(lblTotalFixedTax.getText())));
	    txtTotal.setText(String.valueOf(Math.rint(grandTotal)));
	    dynamicTaxAmt = taxAmt;

	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(180);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(70);
	    txtExtCode.requestFocus();
	    funResetDetailsField();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funResetDetailsField()
    {
	itemCode = "";
	txtItemName.setText("");
	txtQty.setText("0");
	txtPurchaseRate.setText("0");
	txtExtCode.setText("");
    }

    private void funSetShortCutKeys()
    {
	btnDone.setMnemonic('d');
	btnHome.setMnemonic('h');
	btnUp.setMnemonic('u');
	btnDown.setMnemonic('n');
	btnDelItem.setMnemonic('l');
    }

    //Function to delete item from grids of table   
    private void funDeleteStockInOutItem()
    {
	try
	{
	    if (tblItemTable.getModel().getRowCount() > 0)
	    {
		int r = tblItemTable.getSelectedRow();
		if (r == -1)
		{
		    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
		}
		else
		{
		    int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to delete item?", "Item Delete", JOptionPane.YES_NO_OPTION);
		    if (ch == JOptionPane.YES_OPTION)
		    {
			if (lblStkNo.getText().equals("Stk In No."))
			{
			    for (Map.Entry<String, clsStockInDtl> entry : hmStockInDtl.entrySet())
			    {
				if (entry.getValue().getStrItemName().equalsIgnoreCase(tblItemTable.getValueAt(r, 1).toString()))
				{
				    hmStockInDtl.remove(entry.getKey());
				    break;
				}
			    }
			}
			else
			{
			    for (Map.Entry<String, clsStockOutDtl> entry : hmStockOutDtl.entrySet())
			    {
				if (entry.getValue().getStrItemName().equalsIgnoreCase(tblItemTable.getValueAt(r, 1).toString()))
				{
				    hmStockOutDtl.remove(entry.getKey());
				    break;
				}
			    }
			}
		    }
		    funRefreshStockInOutGridTable();
		    funClearFields();
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funTaxCalculateResetButtonClicked()
    {
	DefaultTableModel objDefaultTableModel = (DefaultTableModel) tblTaxCal.getModel();
	objDefaultTableModel.setRowCount(0);
	lblTotalFixedTax.setText("0.00");
    }
}
