package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemWiseConsumption;
import com.POSGlobal.controller.clsOperatorDtl;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSalesFlashReport;
import com.POSGlobal.controller.clsSendMail;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSReport.controller.clsAuditorsReport;
import com.POSReport.controller.clsBillWiseSalesReport;
import com.POSReport.controller.clsComplimentaryBillReport;
import com.POSReport.controller.clsCounterWiseReport;
import com.POSReport.controller.clsCustomerWiseSales;
import com.POSReport.controller.clsDailyCollectionReport;
import com.POSReport.controller.clsDailySalesReport;
import com.POSReport.controller.clsDeliveryboyIncentivesReport;
import com.POSReport.controller.clsDiscountWiseReport;
import com.POSReport.controller.clsGroupSubGroupWiseReport;
import com.POSReport.controller.clsGroupWiseReport;
import com.POSReport.controller.clsGuestCreditReport;
import com.POSReport.controller.clsItemWiseConsumptionReport;
import com.POSReport.controller.clsItemWiseSalesReport;
import com.POSReport.controller.clsMenuHeadWiseSalesReport;
import com.POSReport.controller.clsNonChargableKOTReport;
import com.POSReport.controller.clsOperatorWiseReport;
import com.POSReport.controller.clsOrderAnalysisReport;
import com.POSReport.controller.clsRevenueHeadWiseReport;
import com.POSReport.controller.clsSettlementWiseReport;
import com.POSReport.controller.clsSubGroupWiseReport;
import com.POSReport.controller.clsSubGroupWiseSummaryReport;
import com.POSReport.controller.clsTaxBreakupSummaryReport;
import com.POSReport.controller.clsTaxWiseSalesReport;
import com.POSReport.controller.clsUnusedCardBalanceReport;
import com.POSReport.controller.clsVoidKOTReport;
import com.POSReport.controller.clsWaiterWiseIncentiveSalesReport;
import com.POSReport.controller.clsWaiterWiseItemReport;
import com.POSReport.controller.clsWaiterWiseItemWiseIncentivesSummaryReport;
import com.POSReport.view.frmSettlementWiseGroupWiseBreakup;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class frmShiftEndProcess extends javax.swing.JFrame
{

    private DefaultTableModel dm, dm1, dmSettlementTotal, dmSettlementTable;
    private Object[] gridRecords, totalRecords, discountRecords, settGridRecords, totalSettRecords;
    private double totalWithdrawl, totalPayments, totalTransOuts, totalFloat, totalTransIn;
    private double sales, cashIn, cashOut, advCash, totalSales, totalDiscount;
    private String posDate, sql;
    private int noOfDiscountedBills;
    private double dblApproxSaleAmount = 0.00;
    private int shiftNo;
    private clsUtility objUtility;
    private static int NOOFREPORTS = 0;
    private JCheckBox chkBoxSelectAll;
    private clsUtility2 objUtility2;

    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public frmShiftEndProcess()
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
	    objUtility2 = new clsUtility2();
	    lblShiftNo.setText("Shift No - " + clsGlobalVarClass.gShiftNo);
	    gridRecords = new Object[11];
	    totalRecords = new Object[11];
	    discountRecords = new Object[4];
	    settGridRecords = new Object[4];
	    totalSettRecords = new Object[4];
	    lblPOSName1.setText(clsGlobalVarClass.gPOSName);
	    lblPosName2.setText(clsGlobalVarClass.gPOSName);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();

	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    posDate = date1;
	    //System.out.println("Shift End value="+clsGlobalVarClass.gShiftEnd);
	    //System.out.println("Day End value="+clsGlobalVarClass.gDayEnd);
	    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
	    {
		btnShiftStart.setEnabled(true);
		btnShiftEnd.setEnabled(false);
	    }
	    else if (clsGlobalVarClass.gShiftEnd.equals("N") && clsGlobalVarClass.gDayEnd.equals("N"))
	    {
		btnShiftStart.setEnabled(false);
		btnShiftEnd.setEnabled(true);
	    }

	    //btnShiftStart.setEnabled(true);
	    //btnShiftEnd.setEnabled(true);
	    sql = "select dtePOSDate,intShiftCode from tbldayendprocess "
		    + "where strDayEnd='N' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and (strShiftEnd='' or strShiftEnd='N')   ";
	    ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsShiftNo.next())
	    {
		lblShiftEnd.setText(clsGlobalVarClass.funConvertDateToSimpleFormat(rsShiftNo.getString(1)));
		lblShiftNo.setText("Shift No - " + rsShiftNo.getString(2));
		shiftNo = Integer.parseInt(rsShiftNo.getString(2));
	    }
	    rsShiftNo.close();

	    sql = "select date(max(dtePOSDate)) from tbldayendprocess where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	    ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsDayEnd.next();
	    String sDate = rsDayEnd.getString(1);
	    lblShiftEnd.setText(sDate);

	    sales = 0;
	    cashIn = 0;
	    cashOut = 0;
	    totalSales = 0;
	    totalWithdrawl = 0;
	    totalTransIn = 0;
	    totalTransOuts = 0;
	    totalPayments = 0;
	    totalFloat = 0;
	    dm = new DefaultTableModel();
	    dmSettlementTable = new DefaultTableModel();
	    dmSettlementTotal = new DefaultTableModel();
	    dm.addColumn("Sett Mode");
	    dm.addColumn("Cash(Sales)");
	    dm.addColumn("Float");
	    dm.addColumn("TransIn");
	    dm.addColumn("Advance");
	    dm.addColumn("TotalRec");
	    dm.addColumn("Payments");
	    dm.addColumn("TransOuts");
	    dm.addColumn("Withdrawls");
	    dm.addColumn("TotalPay");
	    dm.addColumn("CashInHand");
	    dm1 = new DefaultTableModel();
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");

	    dmSettlementTable = new DefaultTableModel();
	    dmSettlementTable.addColumn("Settlement Mode");
	    dmSettlementTable.addColumn("Amount");
	    dmSettlementTable.addColumn("Discount");
	    dmSettlementTotal = new DefaultTableModel();
	    dmSettlementTotal.addColumn("Description");
	    dmSettlementTotal.addColumn("Amount");
	    dmSettlementTotal.addColumn("Bills");

	    funFillCurrencyGrid();
	    funFillSettlementWiseSalesGrid();
	    funFillTableSaleInProgress();
	    funFillTableUnsettleBills();
	    lblApproximateTotal.setText(gDecimalFormat.format(dblApproxSaleAmount));

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillCurrencyGrid() throws Exception
    {
	sql = "select strSettelmentDesc from tblsettelmenthd where strSettelmentType='Cash'";
	//System.out.println(sql);
	ResultSet rsSettlementInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsSettlementInfo.next())
	{
	    gridRecords[0] = rsSettlementInfo.getString(1);
	    gridRecords[1] = "0.00";
	    gridRecords[2] = "0.00";
	    gridRecords[3] = "0.00";
	    gridRecords[4] = "0.00";
	    gridRecords[5] = "0.00";
	    gridRecords[6] = "0.00";
	    gridRecords[7] = "0.00";
	    gridRecords[8] = "0.00";
	    gridRecords[9] = "0.00";
	    gridRecords[10] = "0";
	    dm.addRow(gridRecords);
	}
	tblDayEnd.setModel(dm);
	sql = "SELECT c.strSettelmentDesc,sum(b.dblSettlementAmt),sum(a.dblDiscountAmt),c.strSettelmentType "
		+ "FROM tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ "Where a.strBillNo = b.strBillNo and b.strSettlementCode = c.strSettelmentCode "
		+ " and date(a.dteBillDate ) ='" + posDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
		+ " and c.strSettelmentType='Cash' and a.intShiftCode=" + shiftNo + " GROUP BY c.strSettelmentDesc,a.strPosCode";
	//System.out.println(sql);
	ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsSettlement.next())
	{
	    if (rsSettlement.getString(1).equals("Cash"))
	    {
		sales = sales + (Double.parseDouble(rsSettlement.getString(2).toString()));
	    }
	    totalDiscount = totalDiscount + (Double.parseDouble(rsSettlement.getString(3).toString()));

	    totalSales = totalSales + (Double.parseDouble(rsSettlement.getString(2).toString()));

	    for (int cntDayEndTable = 0; cntDayEndTable < tblDayEnd.getRowCount(); cntDayEndTable++)
	    {
		if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsSettlement.getString(1)))
		{
		    tblDayEnd.setValueAt(rsSettlement.getString(2), cntDayEndTable, 1);
		}
	    }
	}

	noOfDiscountedBills = 0;
	sql = "SELECT count(strBillNo),sum(dblDiscountAmt) FROM tblbillhd "
		+ "Where date(dteBillDate ) ='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and dblDiscountAmt > 0.00 and intShiftCode=" + shiftNo
		+ " GROUP BY strPosCode";
	ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsTotalDiscountBills.next())
	{
	    noOfDiscountedBills = rsTotalDiscountBills.getInt(1);
	}

	int totalBillNo = 0;
	sql = "select count(strBillNo) from tblbillhd where date(dteBillDate ) ='" + posDate + "' and "
		+ "strPOSCode='" + clsGlobalVarClass.gPOSCode + " and intShiftCode=" + shiftNo
		+ "' GROUP BY strPosCode";
	ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	if (rsTotalBills.next())
	{
	    totalBillNo = rsTotalBills.getInt(1);
	}
	totalRecords[0] = "Totals";
	totalRecords[1] = totalSales;
	totalRecords[8] = totalBillNo;

	dm1.addRow(totalRecords);
	dm1.addRow(discountRecords);
	sql = "select count(dblAdvDeposite) from tbladvancereceipthd "
		+ "where dtReceiptDate='" + posDate + "' and intShiftCode=" + shiftNo;
	ResultSet rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rsTotalAdvance.next();
	int count = rsTotalAdvance.getInt(1);
	if (count > 0)
	{
	    //sql="select sum(dblAdvDeposite) from tbladvancereceipthd where dtReceiptDate='"+posDate+"'";
	    sql = "select sum(b.dblAdvDepositesettleAmt) from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
		    + "where date(a.dtReceiptDate)='" + posDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode
		    + "' and intShiftCode=" + shiftNo + " and c.strSettelmentCode=b.strSettlementCode "
		    + "and a.strReceiptNo=b.strReceiptNo and c.strSettelmentType='Cash'";
	    // System.out.println(sql);
	    rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsTotalAdvance.next();
	    advCash = Double.parseDouble(rsTotalAdvance.getString(1));
	    tblDayEnd.setValueAt(advCash, 0, 4);
	}

	sql = "select strTransType,sum(dblAmount),strCurrencyType from tblcashmanagement "
		+ "where dteTransDate='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and intShiftCode=" + shiftNo
		+ " group by strTransType,strCurrencyType";
	//System.out.println(sql);
	ResultSet rsTransaction = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsTransaction.next())
	{
	    for (int cntDayEndTable = 0; cntDayEndTable < tblDayEnd.getRowCount(); cntDayEndTable++)
	    {
		if (rsTransaction.getString(1).equals("Float"))
		{
		    if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsTransaction.getString(3)))
		    {
			totalFloat += Double.parseDouble(rsTransaction.getString(2));
			tblDayEnd.setValueAt(rsTransaction.getString(2), cntDayEndTable, 2);
			cashIn = cashIn + (Double.parseDouble(rsTransaction.getString(2).toString()));
		    }
		}
		else if (rsTransaction.getString(1).equals("Transfer In"))
		{
		    if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsTransaction.getString(3)))
		    {
			totalTransIn += Double.parseDouble(rsTransaction.getString(2));
			tblDayEnd.setValueAt(rsTransaction.getString(2), cntDayEndTable, 3);
			cashIn = cashIn + (Double.parseDouble(rsTransaction.getString(2).toString()));
		    }
		}
		else if (rsTransaction.getString(1).equals("Payments"))
		{
		    if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsTransaction.getString(3)))
		    {
			totalPayments += Double.parseDouble(rsTransaction.getString(2));
			tblDayEnd.setValueAt(rsTransaction.getString(2), cntDayEndTable, 6);
			cashOut = cashOut + (Double.parseDouble(rsTransaction.getString(2).toString()));
		    }
		}
		else if (rsTransaction.getString(1).equals("Transfer Out"))
		{
		    if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsTransaction.getString(3)))
		    {
			totalTransOuts += Double.parseDouble(rsTransaction.getString(2));
			tblDayEnd.setValueAt(rsTransaction.getString(2), cntDayEndTable, 7);
			cashOut = cashOut + (Double.parseDouble(rsTransaction.getString(2).toString()));
		    }
		}
		else if (rsTransaction.getString(1).equals("Withdrawl"))
		{
		    if (tblDayEnd.getValueAt(cntDayEndTable, 0).toString().equals(rsTransaction.getString(3)))
		    {
			totalWithdrawl += Double.parseDouble(rsTransaction.getString(2));
			tblDayEnd.setValueAt(rsTransaction.getString(2), cntDayEndTable, 8);
			cashOut = cashOut + (Double.parseDouble(rsTransaction.getString(2).toString()));
		    }
		}
	    }
	}

	sql = "select sum(intPaxNo) from tblbillhd where intShiftCode=" + shiftNo + " "
		+ "and date(dteBillDate ) ='" + posDate + "'" + "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	//System.out.println(sql);
	ResultSet rsTotalPax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsTotalPax.next())
	{
	    int totalPax = rsTotalPax.getInt(1);
	    lblTotalPax.setText(String.valueOf(totalPax));
	}
	rsTotalPax.close();

	cashIn = cashIn + advCash + sales;
	tblTotal.setModel(dm1);
	tblTotal.setValueAt(totalFloat, 0, 2);
	tblTotal.setValueAt(totalTransIn, 0, 3);
	tblTotal.setValueAt(advCash, 0, 4);
	tblTotal.setValueAt(cashIn, 0, 5);
	tblTotal.setValueAt(totalPayments, 0, 6);
	tblTotal.setValueAt(totalTransOuts, 0, 7);
	tblTotal.setValueAt(totalWithdrawl, 0, 8);
	tblTotal.setValueAt(cashOut, 0, 9);
	tblDayEnd.setValueAt(cashIn, 0, 5);
	tblDayEnd.setValueAt(cashOut, 0, 9);

	double inHandCash = (cashIn) - cashOut;
	tblDayEnd.setRowHeight(25);
	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	tblDayEnd.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	tblDayEnd.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
	tblDayEnd.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);

	tblDayEnd.getColumnModel().getColumn(0).setPreferredWidth(85);
	tblDayEnd.getColumnModel().getColumn(1).setPreferredWidth(65);
	tblDayEnd.getColumnModel().getColumn(2).setPreferredWidth(50);
	tblDayEnd.getColumnModel().getColumn(3).setPreferredWidth(65);
	tblDayEnd.getColumnModel().getColumn(4).setPreferredWidth(55);
	tblDayEnd.getColumnModel().getColumn(5).setPreferredWidth(60);
	tblDayEnd.getColumnModel().getColumn(6).setPreferredWidth(60);
	tblDayEnd.getColumnModel().getColumn(7).setPreferredWidth(70);
	tblDayEnd.getColumnModel().getColumn(8).setPreferredWidth(65);
	tblDayEnd.getColumnModel().getColumn(9).setPreferredWidth(55);
	tblDayEnd.getColumnModel().getColumn(10).setPreferredWidth(80);

	DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(7).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(8).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(9).setCellRenderer(rightRenderer1);
	tblTotal.getColumnModel().getColumn(10).setCellRenderer(rightRenderer1);

	tblTotal.getColumnModel().getColumn(0).setPreferredWidth(85);
	tblTotal.getColumnModel().getColumn(1).setPreferredWidth(65);
	tblTotal.getColumnModel().getColumn(2).setPreferredWidth(50);
	tblTotal.getColumnModel().getColumn(3).setPreferredWidth(65);
	tblTotal.getColumnModel().getColumn(4).setPreferredWidth(55);
	tblTotal.getColumnModel().getColumn(5).setPreferredWidth(60);
	tblTotal.getColumnModel().getColumn(6).setPreferredWidth(60);
	tblTotal.getColumnModel().getColumn(7).setPreferredWidth(70);
	tblTotal.getColumnModel().getColumn(8).setPreferredWidth(65);
	tblTotal.getColumnModel().getColumn(9).setPreferredWidth(55);

	double totalReceipts = 0.00, totalPayments = 0.00, balance = 0.00;
	for (int cntDayEndTable = 0; cntDayEndTable < tblDayEnd.getRowCount(); cntDayEndTable++)
	{
	    totalReceipts = Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 1).toString())
		    + Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 2).toString())
		    + Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 3).toString())
		    + Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 4).toString());

	    totalPayments = Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 6).toString())
		    + Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 7).toString())
		    + Double.parseDouble(tblDayEnd.getValueAt(cntDayEndTable, 8).toString());
	    balance = totalReceipts - totalPayments;
	    tblDayEnd.setValueAt(balance, cntDayEndTable, 10);
	}
    }

    private void funFillSettlementWiseSalesGrid() throws Exception
    {
	totalDiscount = 0;
	totalSales = 0;
	sql = "SELECT c.strSettelmentDesc,sum(b.dblSettlementAmt),sum(a.dblDiscountAmt) "
		+ "FROM tblbillhd a, tblbillsettlementdtl b"
		+ ", tblsettelmenthd c Where a.strBillNo = b.strBillNo and b.strSettlementCode = c.strSettelmentCode "
		+ " and date(a.dteBillDate ) ='" + posDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
		+ " and intShiftCode=" + shiftNo
		+ " GROUP BY c.strSettelmentDesc,a.strPosCode";
	//System.out.println(sql);
	ResultSet rsSettlementSale = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsSettlementSale.next())
	{
	    settGridRecords[0] = rsSettlementSale.getString(1);
	    settGridRecords[1] = gDecimalFormat.format(rsSettlementSale.getDouble(2));
	    settGridRecords[2] = gDecimalFormat.format(rsSettlementSale.getDouble(3));
	    totalDiscount = totalDiscount + (Double.parseDouble(rsSettlementSale.getString(3).toString()));
	    totalSales = totalSales + (Double.parseDouble(rsSettlementSale.getString(2).toString()));
	    dmSettlementTable.addRow(settGridRecords);
	}
	rsSettlementSale.close();
	noOfDiscountedBills = 0;
	sql = "SELECT count(strBillNo),sum(dblDiscountAmt) FROM tblbillhd "
		+ "Where date(dteBillDate ) ='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and dblDiscountAmt > 0.00 GROUP BY strPosCode";
	ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsTotalDiscountBills.next())
	{
	    noOfDiscountedBills = rsTotalDiscountBills.getInt(1);
	}
	//System.out.println("Discounts="+totalDiscount+"\tTotal Bills="+noOfDiscountedBills);
	int totalBillNo = 0;
	sql = "select count(strBillNo) from tblbillhd where date(dteBillDate ) ='" + posDate + "' and "
		+ "strPOSCode='" + clsGlobalVarClass.gPOSCode + "' GROUP BY strPosCode";
	ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	if (rsTotalBills.next())
	{
	    totalBillNo = rsTotalBills.getInt(1);
	}
	totalSettRecords[0] = "Total Sales";
	totalSettRecords[1] = gDecimalFormat.format(totalSales);
	totalSettRecords[2] = totalBillNo;

	discountRecords[0] = "Discount";
	discountRecords[1] = gDecimalFormat.format(totalDiscount);
	discountRecords[2] = noOfDiscountedBills;
	dmSettlementTotal.addRow(totalSettRecords);
	dmSettlementTotal.addRow(discountRecords);
	//tblSettlementWiseSalesTotal

	tblSettlementWiseSales.setModel(dmSettlementTable);
	tblSettlementWiseSalesTotal.setModel(dmSettlementTotal);
	tblSettlementWiseSales.setRowHeight(25);

	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	tblSettlementWiseSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	tblSettlementWiseSales.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	tblSettlementWiseSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	tblSettlementWiseSales.getColumnModel().getColumn(0).setPreferredWidth(212);
	tblSettlementWiseSales.getColumnModel().getColumn(1).setPreferredWidth(100);
	tblSettlementWiseSales.getColumnModel().getColumn(2).setPreferredWidth(100);
	DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	tblSettlementWiseSalesTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
	tblSettlementWiseSalesTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
	tblSettlementWiseSalesTotal.getColumnModel().getColumn(0).setPreferredWidth(212);
	tblSettlementWiseSalesTotal.getColumnModel().getColumn(1).setPreferredWidth(100);

	dblApproxSaleAmount += totalSales;
    }

    private void funUpdateDayEndTable()
    {
	try
	{
	   if(!clsGlobalVarClass.gReceiverEmailIds.trim().isEmpty())
	   {
	       funCheckYesterdaysReport();
	   }

	    System.out.println("Shift=" + clsGlobalVarClass.gShifts + "\tShift no=" + shiftNo);
	    funShiftStartProcess();
	    lblShiftNo.setText(String.valueOf(shiftNo));

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	dispose();
    }

    public int funShiftStartProcess() throws Exception
    {
	if (clsGlobalVarClass.gShifts)
	{
	    sql = "update tbldayendprocess set strShiftEnd='N' "
		    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N' and strShiftEnd=''";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    String sql = "select count(intShiftCode) from tblshiftmaster where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	    ResultSet rsShiftNoCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsShiftNoCount.next();
	    int shiftCount = rsShiftNoCount.getInt(1);
	    rsShiftNoCount.close();
	    if (shiftCount > 0)
	    {
		if (shiftNo == shiftCount)
		{
		    shiftNo = 1;
		}
	    }

	    clsGlobalVarClass.gShiftEnd = "N";
	    clsGlobalVarClass.gDayEnd = "N";
	    clsGlobalVarClass.gShiftNo = shiftNo;
	}
	else
	{
	    sql = "update tbldayendprocess set strShiftEnd='N' "
		    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N' and strShiftEnd=''";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    if (shiftNo == 0)
	    {
		shiftNo++;
	    }
	    sql = "update tbldayendprocess set intShiftCode= " + shiftNo + " "
		    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strShiftEnd='N' and strDayEnd='N'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    clsGlobalVarClass.gShiftEnd = "N";
	    clsGlobalVarClass.gDayEnd = "N";
	    clsGlobalVarClass.gShiftNo = shiftNo;
	}

	return 1;
    }

    //for shift disable
    private void funShiftEnd(String posCode)
    {
	try
	{
	    sql = "delete from tblitemrtemp where strTableNo='null'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    clsGlobalVarClass.gDayEndReportForm = "DayEndReport";

	    if (objUtility.funCheckPendingBills(posCode))
	    {
		JOptionPane.showMessageDialog(this, "Please settle pending bills");
		return;
	    }
	    else if (objUtility.funCheckTableBusy(posCode))
	    {
		JOptionPane.showMessageDialog(this, "Sorry Tables are Busy Now");
		return;
	    }
	    else if (objUtility2.isCheckedInMembers(posCode))
	    {
		JOptionPane.showMessageDialog(this, "Please check out the playzone members.");
		return;
	    }
	    else
	    {
		String sqlShift = "select date(max(dtePOSDate)),intShiftCode"
			+ " from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N'"
			+ " and (strShiftEnd='' or strShiftEnd='N')";
		ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlShift);
		if (rsShiftNo.next())
		{
		    shiftNo = rsShiftNo.getInt(2);
		}
		else
		{
		    shiftNo++;
		}

		if (btnShiftEnd.isEnabled())
		{
		    int option = JOptionPane.showConfirmDialog(this, "Do you want to End Day?");
		    if (option == 0)
		    {
			String backupFilePath = "";
			if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
			{
			    backupFilePath = clsGlobalVarClass.funBackupDatabase();
			}

			final String backupFilePathMail = backupFilePath;

			sql = "update tbltablemaster set strStatus='Normal' "
				+ " where strPOSCode='" + posCode + "' ";
			clsGlobalVarClass.dbMysql.execute(sql);

			sql = "truncate tblkottaxdtl ";
			clsGlobalVarClass.dbMysql.execute(sql);

			sql = "update tbldayendprocess set strShiftEnd='Y'"
				+ " where strPOSCode='" + posCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);
			if (clsGlobalVarClass.gGenrateMI)
			{
			    frmGenrateMallInterfaceText objGenrateMallInterfaceText = new frmGenrateMallInterfaceText();
			    objGenrateMallInterfaceText.funWriteToFile(posDate, posDate, "Current", "Y");
			}
			objUtility.funGetNextShiftNo(posCode, shiftNo);
			btnShiftEnd.setEnabled(false);

			// new clsManagersReport().funGenerateManagersReport(posDate, posDate, clsGlobalVarClass.gPOSCode);                        
			final String filePath = System.getProperty("user.dir") + "/Temp/Temp_DayEndReport.txt";

			funSendDayEndReports(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo);

			//send mail sales amount after shift end
			new clsSendMail().funSendMail(totalSales, totalDiscount, totalPayments, filePath, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo, clsGlobalVarClass.gClientCode);

			if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
			{
			    funSendDBBackupAndErrorLogFileToSanguineAuditiing(backupFilePathMail);
			}

			objUtility = null;

			option = JOptionPane.showConfirmDialog(this, "Do You Want To Start Day ?");
			if (option == 0)
			{
			    funUpdateDayEndTable();
			    btnShiftEnd.setEnabled(true);
			    btnShiftStart.setEnabled(false);
			}
			System.exit(0);
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillTableSaleInProgress() throws Exception
    {
	String sqlUpdateTablStatus = "update tbltablemaster a "
		+ "join tblitemrtemp b on a.strTableNo=b.strTableNo "
		+ "set a.strStatus='Occupied' "
		+ "where a.strStatus='Normal' "
		+ "and b.strNCKOTYN='N' ";
	clsGlobalVarClass.dbMysql.execute(sqlUpdateTablStatus);

	double dblSaleInProgressAmount = 0.00;
	DefaultTableModel dmSalesUnderProgress = (DefaultTableModel) jTableSelesUnderProgress.getModel();
	dmSalesUnderProgress.setRowCount(0);

	String sql_FillTable = "select b.strTableName,sum(a.dblAmount) "
		+ " from tblitemrtemp a,tbltablemaster b "
		+ " where a.strTableNo=b.strTableNo and a.strNCKotYN='N' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ " group by a.strTableNo";
	ResultSet rs_filltable = clsGlobalVarClass.dbMysql.executeResultSet(sql_FillTable);
	while (rs_filltable.next())
	{
	    dblSaleInProgressAmount += rs_filltable.getDouble(2);
	    Object[] ob =
	    {
		rs_filltable.getString(1), rs_filltable.getString(2)
	    };
	    dmSalesUnderProgress.addRow(ob);
	}
	Object[] BlankRow =
	{
	    "", ""
	};
	dmSalesUnderProgress.addRow(BlankRow);
	Object[] totalRow =
	{
	    "Total", dblSaleInProgressAmount
	};
	dmSalesUnderProgress.addRow(totalRow);
	rs_filltable.close();
	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	jTableSelesUnderProgress.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	jTableSelesUnderProgress.setModel(dmSalesUnderProgress);
	dblApproxSaleAmount += dblSaleInProgressAmount;
    }

    private void funFillTableUnsettleBills() throws Exception
    {
	double unSetteledBillAmount = 0.00;
	DefaultTableModel dmUnsettledBills = (DefaultTableModel) tblUnsettleBills.getModel();
	dmUnsettledBills.setRowCount(0);
	String sqlUnsettledBillsDina = "select a.strBillNo,c.strTableName,a.dblGrandTotal "
		+ " from tblbillhd a,tbltablemaster c "
		+ " where  date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		+ " and a.strTableNo=c.strTableNo and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) "
		+ " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	ResultSet rsUnsettledBills = clsGlobalVarClass.dbMysql.executeResultSet(sqlUnsettledBillsDina);
	while (rsUnsettledBills.next())
	{
	    unSetteledBillAmount += rsUnsettledBills.getDouble(3);
	    Object[] ob =
	    {
		rsUnsettledBills.getString(1), rsUnsettledBills.getString(2), gDecimalFormat.format(rsUnsettledBills.getDouble(3))
	    };
	    dmUnsettledBills.addRow(ob);
	}
	rsUnsettledBills.close();
	String sqlUnsettledBillDirectBiller = "select a.strBillNo,a.dblGrandTotal "
		+ " from tblbillhd a "
		+ " where a.strTableNo='' and  date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		+ " and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) "
		+ " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	ResultSet rsUnsettledBillsDirectBiller = clsGlobalVarClass.dbMysql.executeResultSet(sqlUnsettledBillDirectBiller);
	while (rsUnsettledBillsDirectBiller.next())
	{
	    unSetteledBillAmount += rsUnsettledBillsDirectBiller.getDouble(2);
	    Object[] ob =
	    {
		rsUnsettledBillsDirectBiller.getString(1), "Direct Biller", gDecimalFormat.format(rsUnsettledBillsDirectBiller.getDouble(2))
	    };
	    dmUnsettledBills.addRow(ob);
	}
	rsUnsettledBillsDirectBiller.close();
	Object[] blankRow =
	{
	    "", " ", ""
	};
	dmUnsettledBills.addRow(blankRow);
	Object[] TotalRow =
	{
	    "Total", " ", unSetteledBillAmount
	};
	dmUnsettledBills.addRow(TotalRow);

	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	tblUnsettleBills.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	tblUnsettleBills.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	tblUnsettleBills.setModel(dmUnsettledBills);
	dblApproxSaleAmount += unSetteledBillAmount;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        dialogDayEndReports = new javax.swing.JDialog();
        panelHeader2 = new javax.swing.JPanel();
        lblProductName3 = new javax.swing.JLabel();
        lblModuleName3 = new javax.swing.JLabel();
        lblformName2 = new javax.swing.JLabel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName2 = new javax.swing.JLabel();
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode2 = new javax.swing.JLabel();
        lblDate2 = new javax.swing.JLabel();
        lblHOSign3 = new javax.swing.JLabel();
        panelDayEndSetup2 = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };
        ;
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDayEndReports = new javax.swing.JTable();
        lblNoOfReports = new javax.swing.JLabel();
        btnDayEndReports1 = new javax.swing.JButton();
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
        panelMainForm = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        scrShiftEndTable1 = new javax.swing.JScrollPane();
        tblDayEnd = new javax.swing.JTable();
        lblPaxNo = new javax.swing.JLabel();
        lblTotalPax = new javax.swing.JLabel();
        scrShiftEndTable2 = new javax.swing.JScrollPane();
        tblTotal = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnShiftStart = new javax.swing.JButton();
        btnShiftEnd = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSettlementWiseSalesTotal = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSettlementWiseSales = new javax.swing.JTable();
        scrSalesUnderProgress = new javax.swing.JScrollPane();
        jTableSelesUnderProgress = new javax.swing.JTable();
        lblUnsettleBills = new javax.swing.JLabel();
        scrUnsettledBills = new javax.swing.JScrollPane();
        tblUnsettleBills = new javax.swing.JTable();
        lblSalesUnderProgress = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblApproximateTotal = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        lblShiftNo = new javax.swing.JLabel();
        lblPOSName1 = new javax.swing.JLabel();
        lblShiftEnd = new javax.swing.JLabel();

        dialogDayEndReports.setBounds(new java.awt.Rectangle(200, 200, 700, 585));
        dialogDayEndReports.setModal(true);
        dialogDayEndReports.setResizable(false);

        panelHeader2.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader2.setLayout(new javax.swing.BoxLayout(panelHeader2, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName3.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName3.setText("SPOS -");
        lblProductName3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductName3MouseClicked(evt);
            }
        });
        panelHeader2.add(lblProductName3);

        lblModuleName3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName3.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader2.add(lblModuleName3);

        lblformName2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName2.setForeground(new java.awt.Color(255, 255, 255));
        lblformName2.setText("- Day End Process");
        lblformName2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformName2MouseClicked(evt);
            }
        });
        panelHeader2.add(lblformName2);
        panelHeader2.add(filler13);
        panelHeader2.add(filler14);

        lblPosName2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName2.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName2.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName2.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName2.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosName2MouseClicked(evt);
            }
        });
        panelHeader2.add(lblPosName2);
        panelHeader2.add(filler15);

        lblUserCode2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode2.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode2.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode2.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode2.setName(""); // NOI18N
        lblUserCode2.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCode2MouseClicked(evt);
            }
        });
        panelHeader2.add(lblUserCode2);

        lblDate2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate2.setForeground(new java.awt.Color(255, 255, 255));
        lblDate2.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate2.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate2.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDate2MouseClicked(evt);
            }
        });
        panelHeader2.add(lblDate2);

        lblHOSign3.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign3.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign3.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSign3MouseClicked(evt);
            }
        });
        panelHeader2.add(lblHOSign3);

        panelDayEndSetup2.setOpaque(false);

        tblDayEndReports.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDayEndReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String []
            {
                "REPORT NAME", "SEND EMAIL"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true
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
        tblDayEndReports.getTableHeader().setReorderingAllowed(false);
        tblDayEndReports.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblDayEndReportsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDayEndReports);
        if (tblDayEndReports.getColumnModel().getColumnCount() > 0)
        {
            tblDayEndReports.getColumnModel().getColumn(0).setResizable(false);
            tblDayEndReports.getColumnModel().getColumn(0).setPreferredWidth(570);
            tblDayEndReports.getColumnModel().getColumn(1).setResizable(false);
        }

        lblNoOfReports.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNoOfReports.setForeground(new java.awt.Color(51, 51, 255));
        lblNoOfReports.setText("No Of Reports   :");

        btnDayEndReports1.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        btnDayEndReports1.setForeground(new java.awt.Color(254, 254, 254));
        btnDayEndReports1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDayEndReports1.setText("Send Email");
        btnDayEndReports1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDayEndReports1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDayEndReports1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDayEndReports1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelDayEndSetup2Layout = new javax.swing.GroupLayout(panelDayEndSetup2);
        panelDayEndSetup2.setLayout(panelDayEndSetup2Layout);
        panelDayEndSetup2Layout.setHorizontalGroup(
            panelDayEndSetup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDayEndSetup2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoOfReports, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDayEndReports1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelDayEndSetup2Layout.setVerticalGroup(
            panelDayEndSetup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDayEndSetup2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDayEndSetup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDayEndReports1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoOfReports, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout dialogDayEndReportsLayout = new javax.swing.GroupLayout(dialogDayEndReports.getContentPane());
        dialogDayEndReports.getContentPane().setLayout(dialogDayEndReportsLayout);
        dialogDayEndReportsLayout.setHorizontalGroup(
            dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
            .addGroup(dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelHeader2, javax.swing.GroupLayout.PREFERRED_SIZE, 703, Short.MAX_VALUE))
            .addGroup(dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelDayEndSetup2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialogDayEndReportsLayout.setVerticalGroup(
            dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 585, Short.MAX_VALUE)
            .addGroup(dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(dialogDayEndReportsLayout.createSequentialGroup()
                    .addComponent(panelHeader2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 555, Short.MAX_VALUE)))
            .addGroup(dialogDayEndReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogDayEndReportsLayout.createSequentialGroup()
                    .addGap(0, 29, Short.MAX_VALUE)
                    .addComponent(panelDayEndSetup2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(1024, 768));
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
        panelHeader.setMinimumSize(new java.awt.Dimension(1024, 30));
        panelHeader.setPreferredSize(new java.awt.Dimension(1024, 30));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Day End Process");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setName(""); // NOI18N
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setMaximumSize(new java.awt.Dimension(1024, 768));
        panelMainForm.setMinimumSize(new java.awt.Dimension(1024, 768));
        panelMainForm.setOpaque(false);
        panelMainForm.setPreferredSize(new java.awt.Dimension(1024, 768));
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setAutoscrolls(true);
        panelFormBody.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelFormBody.setMaximumSize(new java.awt.Dimension(1024, 768));
        panelFormBody.setMinimumSize(new java.awt.Dimension(1024, 768));
        panelFormBody.setOpaque(false);
        panelFormBody.setPreferredSize(new java.awt.Dimension(1024, 768));

        tblDayEnd.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblDayEnd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Settl Mode", "Cash(Sales)", "Float", "Trans In", "Advance", "Total Rec", "Payments", "Trans Outs", "Withdrawls", "Total Pay", "Balance"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblDayEnd.setSelectionBackground(new java.awt.Color(0, 153, 255));
        tblDayEnd.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblDayEnd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblDayEndMouseClicked(evt);
            }
        });
        scrShiftEndTable1.setViewportView(tblDayEnd);

        lblPaxNo.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblPaxNo.setText("Total Pax   :");

        lblTotalPax.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblTotalPax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPax.setText("0");
        lblTotalPax.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        tblTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        tblTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Title 1"
            }
        ));
        tblTotal.setRowHeight(25);
        scrShiftEndTable2.setViewportView(tblTotal);

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnClose.setForeground(new java.awt.Color(254, 254, 254));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnShiftStart.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnShiftStart.setForeground(new java.awt.Color(255, 255, 255));
        btnShiftStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnShiftStart.setText("START");
        btnShiftStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShiftStart.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnShiftStart.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShiftStartActionPerformed(evt);
            }
        });

        btnShiftEnd.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnShiftEnd.setForeground(new java.awt.Color(254, 254, 254));
        btnShiftEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnShiftEnd.setText("END");
        btnShiftEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShiftEnd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnShiftEnd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnShiftEndMouseClicked(evt);
            }
        });

        tblSettlementWiseSalesTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        tblSettlementWiseSalesTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Amount", "No. Of Bills"
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
        tblSettlementWiseSalesTotal.setRowHeight(25);
        jScrollPane4.setViewportView(tblSettlementWiseSalesTotal);

        tblSettlementWiseSales.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblSettlementWiseSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Settlement Mode", "Amount", "No Of Bills"
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
        tblSettlementWiseSales.setSelectionBackground(new java.awt.Color(0, 153, 255));
        tblSettlementWiseSales.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblSettlementWiseSales.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblSettlementWiseSalesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblSettlementWiseSales);

        jTableSelesUnderProgress.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jTableSelesUnderProgress.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Table Name", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTableSelesUnderProgress.getTableHeader().setReorderingAllowed(false);
        scrSalesUnderProgress.setViewportView(jTableSelesUnderProgress);

        lblUnsettleBills.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblUnsettleBills.setText("Unsettled Bills");

        tblUnsettleBills.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblUnsettleBills.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No", "Table Name", "Bill Amount"
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
        tblUnsettleBills.getTableHeader().setReorderingAllowed(false);
        scrUnsettledBills.setViewportView(tblUnsettleBills);

        lblSalesUnderProgress.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblSalesUnderProgress.setText("Sales Under Progress");

        lblTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblTotal.setText("Total   :");

        lblApproximateTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N

        panelBody.setBackground(new java.awt.Color(254, 254, 254));

        lblShiftNo.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        lblShiftNo.setForeground(new java.awt.Color(0, 141, 255));
        lblShiftNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShiftNo.setText("Shift No - ");

        lblPOSName1.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        lblPOSName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblShiftEnd.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        lblShiftEnd.setForeground(new java.awt.Color(0, 141, 255));
        lblShiftEnd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShiftEnd.setText("Day End Process");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblShiftNo, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(263, 263, 263))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblShiftNo)
                        .addComponent(lblShiftEnd)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBody, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelFormBodyLayout.createSequentialGroup()
                                    .addGap(150, 150, 150)
                                    .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelFormBodyLayout.createSequentialGroup()
                                    .addGap(300, 300, 300)
                                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrShiftEndTable1)
                            .addComponent(scrShiftEndTable2)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(scrUnsettledBills, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblUnsettleBills, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addComponent(lblSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(90, 390, Short.MAX_VALUE))
                                    .addComponent(scrSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(460, 460, 460)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(lblApproximateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(460, 460, 460)
                        .addComponent(lblPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalPax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrShiftEndTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrShiftEndTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnsettleBills, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrUnsettledBills, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(scrSalesUnderProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTotalPax, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblApproximateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(42, 42, 42)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(66, 66, 66))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDayEndMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDayEndMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_tblDayEndMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	// TODO add your handling code here:
	objUtility = null;
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnShiftStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShiftStartActionPerformed
	// TODO add your handling code here:
	funUpdateDayEndTable();
	btnShiftEnd.setEnabled(true);
	btnShiftStart.setEnabled(false);
    }//GEN-LAST:event_btnShiftStartActionPerformed

    private void btnShiftEndMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShiftEndMouseClicked
	try
	{
	    if (clsGlobalVarClass.gEnableShiftYN)
	    {
		String sql = "select count(intShiftCode) from tblshiftmaster where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
		ResultSet rsShiftNoCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsShiftNoCount.next();
		int shiftCount = rsShiftNoCount.getInt(1);
		rsShiftNoCount.close();
		if (shiftCount > 0)
		{
		    if (shiftNo == shiftCount)
		    {
			funShiftEnd(clsGlobalVarClass.gPOSCode);//for shift disable
		    }
		    else//for shift enable
		    {
			funDoShiftEnd(clsGlobalVarClass.gPOSCode);
		    }
		}
		else
		{
		    funShiftEnd(clsGlobalVarClass.gPOSCode);//for shift disable
		}
	    }
	    else
	    {
		funShiftEnd(clsGlobalVarClass.gPOSCode);//for shift disable
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }//GEN-LAST:event_btnShiftEndMouseClicked

    private void tblSettlementWiseSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSettlementWiseSalesMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementWiseSalesMouseClicked

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void lblProductName3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductName3MouseClicked
    {//GEN-HEADEREND:event_lblProductName3MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblProductName3MouseClicked

    private void lblformName2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformName2MouseClicked
    {//GEN-HEADEREND:event_lblformName2MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblformName2MouseClicked

    private void lblPosName2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosName2MouseClicked
    {//GEN-HEADEREND:event_lblPosName2MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblPosName2MouseClicked

    private void lblUserCode2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCode2MouseClicked
    {//GEN-HEADEREND:event_lblUserCode2MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblUserCode2MouseClicked

    private void lblDate2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDate2MouseClicked
    {//GEN-HEADEREND:event_lblDate2MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblDate2MouseClicked

    private void lblHOSign3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSign3MouseClicked
    {//GEN-HEADEREND:event_lblHOSign3MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblHOSign3MouseClicked

    private void btnDayEndReports1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDayEndReports1MouseClicked
    {//GEN-HEADEREND:event_btnDayEndReports1MouseClicked
	funSendEmailClicked(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo);

    }//GEN-LAST:event_btnDayEndReports1MouseClicked

    private void tblDayEndReportsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblDayEndReportsMouseClicked
    {//GEN-HEADEREND:event_tblDayEndReportsMouseClicked
	funSetReportCount();
    }//GEN-LAST:event_tblDayEndReportsMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosing

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDayEndReports1;
    private javax.swing.JButton btnShiftEnd;
    private javax.swing.JButton btnShiftStart;
    private javax.swing.JDialog dialogDayEndReports;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableSelesUnderProgress;
    private javax.swing.JLabel lblApproximateTotal;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHOSign3;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName3;
    private javax.swing.JLabel lblNoOfReports;
    private javax.swing.JLabel lblPOSName1;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPosName2;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblProductName3;
    private javax.swing.JLabel lblSalesUnderProgress;
    private javax.swing.JLabel lblShiftEnd;
    private javax.swing.JLabel lblShiftNo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalPax;
    private javax.swing.JLabel lblUnsettleBills;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserCode2;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblformName2;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelDayEndSetup2;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelHeader2;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrSalesUnderProgress;
    private javax.swing.JScrollPane scrShiftEndTable1;
    private javax.swing.JScrollPane scrShiftEndTable2;
    private javax.swing.JScrollPane scrUnsettledBills;
    private javax.swing.JTable tblDayEnd;
    private javax.swing.JTable tblDayEndReports;
    private javax.swing.JTable tblSettlementWiseSales;
    private javax.swing.JTable tblSettlementWiseSalesTotal;
    private javax.swing.JTable tblTotal;
    private javax.swing.JTable tblUnsettleBills;
    // End of variables declaration//GEN-END:variables

    //for shift enable
    private void funDoShiftEnd(String gPOSCode)
    {
	try
	{
	    sql = "delete from tblitemrtemp where strTableNo='null'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    clsGlobalVarClass.gDayEndReportForm = "DayEndReport";

	    boolean doCheckPendingOperations = true;
	    if (clsGlobalVarClass.gEnableShiftYN && clsGlobalVarClass.gLockDataOnShiftYN)
	    {
		//if shift is enable and  shift live data to q is enable(gLockDataOnShiftYN==true)
		// check pending bills and kots
		doCheckPendingOperations = true;
	    }
	    else if (clsGlobalVarClass.gEnableShiftYN && !clsGlobalVarClass.gLockDataOnShiftYN)
	    {
		//if shift is enable and don't shift live data to q is enablegLockDataOnShiftYN==false)
		//don't check pending bills and kots
		doCheckPendingOperations = false;
	    }

	    if (doCheckPendingOperations && objUtility.funCheckPendingBills(gPOSCode))
	    {
		JOptionPane.showMessageDialog(this, "Please settle pending bills");
		return;
	    }
	    else if (doCheckPendingOperations && objUtility.funCheckTableBusy(gPOSCode))
	    {
		JOptionPane.showMessageDialog(this, "Sorry Tables are Busy Now");
		return;
	    }
	    else if (objUtility2.isCheckedInMembers(gPOSCode))
	    {
		JOptionPane.showMessageDialog(this, "Please check out the playzone members.");
		return;
	    }
	    else
	    {
		String sqlShift = "select date(max(dtePOSDate)),intShiftCode"
			+ " from tbldayendprocess where strPOSCode='" + gPOSCode + "' and strDayEnd='N'"
			+ " and (strShiftEnd='' or strShiftEnd='N')";
		ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlShift);
		if (rsShiftNo.next())
		{
		    shiftNo = rsShiftNo.getInt(2);
		}

		if (btnShiftEnd.isEnabled())
		{
		    int option = JOptionPane.showConfirmDialog(this, "Do You Want To End Shift No." + shiftNo + " ?");
		    if (option == 0)
		    {

			//database backup
			String backupFilePath = "";
			if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
			{
			    backupFilePath = clsGlobalVarClass.funBackupDatabase();
			}
			final String backupFilePathMail = backupFilePath;

			if (doCheckPendingOperations)
			{
			    sql = "update tbltablemaster set strStatus='Normal' "
				    + " where strPOSCode='" + gPOSCode + "' ";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}

			sql = "update tbldayendprocess set strShiftEnd='Y'"
				+ " where strPOSCode='" + gPOSCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);

			//generate MI
			if (clsGlobalVarClass.gGenrateMI)
			{
			    frmGenrateMallInterfaceText objGenrateMallInterfaceText = new frmGenrateMallInterfaceText();
			    objGenrateMallInterfaceText.funWriteToFile(posDate, posDate, "Current", "Y");
			}

			//shift end function
			objUtility.funGetNextShiftNoForShiftEnd(gPOSCode, shiftNo);

			btnShiftEnd.setEnabled(false);
			final String filePath = System.getProperty("user.dir") + "/Temp/Temp_DayEndReport.txt";

			option = JOptionPane.showConfirmDialog(this, "Do You Want To Email Reports?");
			if (option == 0)
			{
			    funSendDayEndReports(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo);
			}
			else
			{
			    //delete old reports
			    funCreateReportFolder();
			}

			//send mail sales amount after shift end
			new clsSendMail().funSendMail(totalSales, totalDiscount, totalPayments, filePath, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo, clsGlobalVarClass.gClientCode);

			if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
			{
			    funSendDBBackupAndErrorLogFileToSanguineAuditiing(backupFilePathMail);
			}

			objUtility = null;
			clsGlobalVarClass.gShiftNo = (shiftNo + 1);

			option = JOptionPane.showConfirmDialog(this, "Do You Want To Start Day ?");
			if (option == 0)
			{
			    funUpdateDayEndTable();
			    btnShiftEnd.setEnabled(true);
			    btnShiftStart.setEnabled(false);
			}
			System.exit(0);
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSendDayEndReports(String posCode, String posName, String posDate, int shiftNo)
    {
	boolean isSendDefault = false;

	try
	{
	    funCreateReportFolder();

	    String sqlReports = "select b.strModuleName,b.strFormName "
		    + "from (select a.strModuleName,a.strFormName  "
		    + "from tblforms a  "
		    + "where a.strModuleType='R' "
		    + "union  "
		    + "select 'Customer Wise Sales'strModuleName,'Customer Wise Sales' strFormName "
		    + ")b "
		    + "order by strModuleName ";
	    ResultSet rsReports = clsGlobalVarClass.dbMysql.executeResultSet(sqlReports);
	    DefaultTableModel dmDayEndReports = (DefaultTableModel) tblDayEndReports.getModel();
	    dmDayEndReports.setRowCount(0);
	    while (rsReports.next())
	    {
		Object[] row =
		{
		    rsReports.getString(1).toUpperCase(), false
		};
		dmDayEndReports.addRow(row);
	    }
	    rsReports.close();

	    //fill old reports
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet("select  strPOSCode,strReportName,date(dtePOSDate) "
		    + "from tbldayendreports "
		    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
	    while (rs.next())
	    {
		String reportName = rs.getString(2);

		for (int i = 0; i < tblDayEndReports.getRowCount(); i++)
		{
		    if (tblDayEndReports.getValueAt(i, 0) != null && tblDayEndReports.getValueAt(i, 0).toString().equalsIgnoreCase(reportName))
		    {
			tblDayEndReports.setValueAt(Boolean.parseBoolean("true"), i, 1);
			isSendDefault = true;
		    }
		}

	    }
	    rs.close();

	    chkBoxSelectAll = new JCheckBox("Select All");
	    chkBoxSelectAll.setSelected(false);
	    TableColumnModel columnModel = tblDayEndReports.getColumnModel();
	    JTableHeader header = tblDayEndReports.getTableHeader();
	    header.add(chkBoxSelectAll);
	    header.setLayout(new FlowLayout(FlowLayout.CENTER));

	    tblDayEndReports.setRowHeight(30);

	    chkBoxSelectAll.addActionListener(new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    if (chkBoxSelectAll.isSelected())
		    {
			for (int i = 0; i < tblDayEndReports.getRowCount(); i++)
			{
			    tblDayEndReports.setValueAt(Boolean.parseBoolean("true"), i, 1);
			}
		    }
		    else
		    {
			for (int i = 0; i < tblDayEndReports.getRowCount(); i++)
			{
			    tblDayEndReports.setValueAt(Boolean.parseBoolean("false"), i, 1);
			}
		    }

		    funSetReportCount();

		}
	    });
	    if (isSendDefault)
	    {
		funSendEmailClicked(posCode, posName, posDate, shiftNo);
	    }
	    else
	    {
		dialogDayEndReports.setVisible(true);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

//        funCreateZipFolder();
//        String filePath = System.getProperty("user.dir");
//        String zipFile = filePath + "/Reports.zip";
//
//        try
//        {
//            new clsSendMail().funSendMail(clsGlobalVarClass.gReceiverEmailIds, zipFile);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    public void funCreateExcelSheet(List<String> parameterList, List<String> headerList, Map<Integer, List<String>> map, List<String> totalList, String fileName)
    {
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + "\\Reports\\" + fileName + ".xls");
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
	    WritableFont cellFont = new WritableFont(WritableFont.COURIER, 14);
	    cellFont.setBoldStyle(WritableFont.BOLD);
	    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
	    WritableFont headerCellFont = new WritableFont(WritableFont.TIMES, 10);
	    headerCellFont.setBoldStyle(WritableFont.BOLD);
	    WritableCellFormat headerCell = new WritableCellFormat(headerCellFont);

	    for (int j = 0; j <= parameterList.size(); j++)
	    {
		Label l0 = new Label(2, 0, parameterList.get(0), cellFormat);
		Label l1 = new Label(0, 2, parameterList.get(1), headerCell);
		Label l2 = new Label(1, 2, parameterList.get(2), headerCell);
		Label l3 = new Label(2, 2, parameterList.get(3), headerCell);
		Label l4 = new Label(0, 3, parameterList.get(4), headerCell);
		Label l5 = new Label(1, 3, parameterList.get(5), headerCell);

		sheet1.addCell(l0);
		sheet1.addCell(l1);
		sheet1.addCell(l2);
		sheet1.addCell(l3);
		sheet1.addCell(l4);
		sheet1.addCell(l5);
	    }

	    for (int j = 0; j < headerList.size(); j++)
	    {
		Label lblHeader = new Label(j, 5, headerList.get(j), headerCell);
		sheet1.addCell(lblHeader);
	    }

	    int i = 7;
	    for (Map.Entry<Integer, List<String>> entry : map.entrySet())
	    {
		Label lbl0 = new Label(0, i, entry.getKey().toString());
		List<String> nameList = map.get(entry.getKey());
		for (int j = 0; j < nameList.size(); j++)
		{
		    int colIndex = j + 1;
		    Label lblData = new Label(colIndex, i, nameList.get(j));
		    sheet1.addCell(lblData);
		    sheet1.setColumnView(i, 15);
		}
		sheet1.addCell(lbl0);
		i++;
	    }

	    for (int j = 0; j < totalList.size(); j++)
	    {
		String[] l0 = new String[10];
		for (int c = 0; c < totalList.size(); c++)
		{
		    l0 = totalList.get(c).split("#");
		    int pos = Integer.parseInt(l0[1]);
		    Label lable0 = new Label(pos, i + 1, l0[0], headerCell);
		    sheet1.addCell(lable0);
		}
		Label labelTotal = new Label(0, i + 1, "TOTAL:", headerCell);
		sheet1.addCell(labelTotal);
	    }
	    workbook1.write();
	    workbook1.close();

//            Desktop dt = Desktop.getDesktop();
//            dt.open(file);
	}
	catch (Exception ex)
	{
	    JOptionPane.showMessageDialog(null, ex.getMessage());
	    ex.printStackTrace();
	}
    }

    public void funCreateReportFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Reports");

	    System.out.println("reports path=" + file.toPath());
	    if (file.exists())
	    {
		// Get all files in the folder
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++)
		{
		    // Delete each file in the folder
		    files[i].delete();
		}
		// Delete the folder
		// file.delete();
	    }
	    else
	    {
		file.mkdir();
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCreateZipFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    String zipFile = filePath + "/Reports.zip";
	    File dir = new File(filePath + "/Reports");

	    //create byte buffer
	    byte[] buffer = new byte[1024];

	    //create object of FileOutputStream
	    FileOutputStream fout = new FileOutputStream(zipFile);
	    //create object of ZipOutputStream from FileOutputStream
	    ZipOutputStream zout = new ZipOutputStream(fout);
	    //check to see if this directory exists
	    if (!dir.isDirectory())
	    {
		System.out.println(dir.getName() + " is not a directory");
	    }
	    else
	    {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++)
		{
		    //create object of FileInputStream for source file
		    FileInputStream fin = new FileInputStream(files[i]);
		    zout.putNextEntry(new ZipEntry(files[i].getName()));
		    /*
                     * After creating entry in the zip file, actually write the
                     * file.
		     */
		    int length;
		    while ((length = fin.read(buffer)) > 0)
		    {
			zout.write(buffer, 0, length);
		    }
		    zout.closeEntry();
		    //close the InputStream
		    fin.close();
		}
	    }
	    //close the ZipOutputStream
	    zout.close();
	    System.out.println("Zip file has been created!");
	}
	catch (Exception ioe)
	{
	    System.out.println("IOException :" + ioe);
	}
    }

    private void funGenerateBillWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsBillWiseSalesReport obj = new clsBillWiseSalesReport();
	obj.funGenerateBillWiseReport("Excel Report", hm, "Yes");
    }

    ///////////////////////////// line no 2204
    private void funGenerateItemWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);
	hm.put("printComplimentaryYN", "Yes");

	clsItemWiseSalesReport obj = new clsItemWiseSalesReport();
	obj.funGenerateItemWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateGroupWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);
	hm.put("rptType", "All");
	hm.put("subGroup", "All");

	clsGroupWiseReport obj = new clsGroupWiseReport();
	obj.funGroupWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateSubGroupWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);
	hm.put("rptType", "Detail");

	clsSubGroupWiseReport obj = new clsSubGroupWiseReport();
	obj.funSubGroupWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateMenuHeadWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsMenuHeadWiseSalesReport obj = new clsMenuHeadWiseSalesReport();
	obj.funGenerateMenuHeadWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateComplimentaryWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Group Wise");
	hm.put("reasonCode", "All");
	hm.put("reasonName", "All");
	clsComplimentaryBillReport obj = new clsComplimentaryBillReport();
	obj.funGenerateComplimentaryBillReport("Excel Report", hm, "Yes");
    }

    private void funGenerateGuestCreditExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsGuestCreditReport obj = new clsGuestCreditReport();
	obj.funGuestCreditReport("Excel Report", hm, "Yes");
    }

    private void funGenerateCounterWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Menu Wise");
	clsCounterWiseReport obj = new clsCounterWiseReport();
	obj.funCounterWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateDiscountExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Detail");
	clsDiscountWiseReport obj = new clsDiscountWiseReport();
	obj.funGenerateDsicountWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateGroupSubGroupWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("groupCode", "All");
	hm.put("subGroupCode", "All");
	hm.put("rptType", "Detail");
	
	clsGroupSubGroupWiseReport obj = new clsGroupSubGroupWiseReport();
	obj.funGenerateGroupSubGroupWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateNCKOTExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("reasonCode", "All");
	clsNonChargableKOTReport obj = new clsNonChargableKOTReport();
	obj.funNonChargableKOTReport("Excel Report", hm, "Yes");
    }

    private void funGenerateOperatorWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("userCode", "All");
	hm.put("userName", "All");
	hm.put("settleCode", "All");
	clsOperatorWiseReport obj = new clsOperatorWiseReport();
	obj.funOperatorWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateOrderAnalysisExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("userCode", "All");
	hm.put("userName", "All");
	hm.put("settleCode", "All");
	clsOrderAnalysisReport obj = new clsOrderAnalysisReport();
	obj.funOrderAnalysisReport("Excel Report", hm, "Yes");
    }

    private void funGenerateSettlementWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsSettlementWiseReport obj = new clsSettlementWiseReport();
	obj.funSettlementWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateTaxExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsTaxWiseSalesReport obj = new clsTaxWiseSalesReport();
	obj.funTaxWiseSalesReport("Excel Report", hm, "Yes");
    }

    private void funGenerateVoidBillExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Detail");
	clsTaxWiseSalesReport obj = new clsTaxWiseSalesReport();
	obj.funTaxWiseSalesReport("Excel Report", hm, "Yes");
    }

    private void funGenerateTaxBreakUpExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsTaxBreakupSummaryReport obj = new clsTaxBreakupSummaryReport();
	obj.funTaxBreakupSummaryReport("Excel Report", hm, "Yes");
    }

    private void funGenerateWaiterWiseItemExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("waiterCode", "All");
	clsWaiterWiseItemReport obj = new clsWaiterWiseItemReport();
	obj.funWaiterWiseItemReport("Excel Report", hm, "Yes");
    }

    private void funGenerateAuditorExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsAuditorsReport obj = new clsAuditorsReport();
	obj.funAuditorsReport("Excel Report", hm, "Yes");
    }

    private void funGenerateWaiterWiseIncentivesExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Detail");
	clsWaiterWiseIncentiveSalesReport obj = new clsWaiterWiseIncentiveSalesReport();
	obj.funGenerateWaiterWiseIncentivesWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateDeliveryBoyIncentivesExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("rptType", "Detail");
	hm.put("DPName", "All");
	hm.put("DPCode", "All");
	clsDeliveryboyIncentivesReport obj = new clsDeliveryboyIncentivesReport();
	obj.funDeliveryboyIncentivesReport("Excel Report", hm, "Yes");
    }

    private void funGenerateDailyCollectionExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsDailyCollectionReport obj = new clsDailyCollectionReport();
	obj.funDailyCollectionReport("Excel Report", hm, "Yes");
    }

    private void funGenerateDailySalesExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsDailySalesReport obj = new clsDailySalesReport();
	obj.funDailyCollectionReport("Excel Report", hm, "Yes");
    }

    private void funGenerateVoidKOTExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsVoidKOTReport obj = new clsVoidKOTReport();
	obj.funVoidKOTReport("Excel Report", hm, "Yes", "All");
    }

    private void funGenerateSubGroupWiseSummaryExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsSubGroupWiseSummaryReport obj = new clsSubGroupWiseSummaryReport();
	obj.funSubGroupWiseSummaryReport("Excel Report", hm, "Yes");
    }

    private void funGenerateUnUsedCardBalanceExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	clsUnusedCardBalanceReport obj = new clsUnusedCardBalanceReport();
	obj.funUnusedCardBalanceReport("Excel Report", hm, "Yes");
    }

    private void funGenerateRevenueHeadWiseExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("revenueHead", "All");
	hm.put("rptType", "Detail");
	clsRevenueHeadWiseReport obj = new clsRevenueHeadWiseReport();
	obj.funRevenueHeadWiseReport("Excel Report", hm, "Yes");
    }

    private void funGenerateItemConsumptionExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("GroupCode", "All");
	hm.put("GroupName", "All");
	hm.put("PrintZeroAmountModi", "Yes");
	hm.put("costCenterCode", "All");
	hm.put("costCenterName", "All");

	clsItemWiseConsumptionReport obj = new clsItemWiseConsumptionReport();
	obj.funItemWiseConsumptionReport("Excel Report", hm, "Yes");

    }

    private void funGenerateWaiterWiseItemWiseIncentivesExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("groupCode", "All");
	hm.put("subGroupCode", "All");

	clsWaiterWiseItemWiseIncentivesSummaryReport objWaiterWiseItemWiseIncentivesSummaryReport = new clsWaiterWiseItemWiseIncentivesSummaryReport();
	objWaiterWiseItemWiseIncentivesSummaryReport.funWaiterWiseItemWiseIncentivesSummaryReport("Excel Report", hm, "Yes", "Detail");
    }

    private void funCheckBoxClicked(JCheckBox chkBox)
    {
	if (chkBox.isSelected())
	{
	    NOOFREPORTS = NOOFREPORTS + 1;
	}
	else
	{
	    NOOFREPORTS = NOOFREPORTS - 1;
	}
	lblNoOfReports.setText("");
	lblNoOfReports.setText("No Of Reports   :" + NOOFREPORTS);
    }

    private void funSendEmailClicked(String posCode, String posName, String posDate, int shift)
    {
	try
	{
	    String fromDate = posDate;
	    String toDate = posDate;

	    StringBuilder sqlBuilder = new StringBuilder();
	    clsUtility objUtility = new clsUtility();

	    sqlBuilder.setLength(0);
	    sqlBuilder.append("insert into tbldayendreports "
		    + "(strPOSCode,strClientCode,strReportName,dtePOSDate,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag) "
		    + "values ");

	    int count = 0;
	    for (int r = 0; r < tblDayEndReports.getRowCount(); r++)
	    {
		if (Boolean.parseBoolean(tblDayEndReports.getValueAt(r, 1).toString()))
		{
		    if (count == 0)
		    {
			sqlBuilder.append("('" + posCode + "','" + clsGlobalVarClass.gClientCode + "','" + tblDayEndReports.getValueAt(r, 0).toString() + "'"
				+ ",'" + posDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','N')");
			count++;
		    }
		    else
		    {
			sqlBuilder.append(",('" + posCode + "','" + clsGlobalVarClass.gClientCode + "','" + tblDayEndReports.getValueAt(r, 0).toString() + "'"
				+ ",'" + posDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','N')");
			count++;
		    }

		    funGenerateReport(tblDayEndReports.getValueAt(r, 0).toString(), posCode, posName, posDate, shift);
		}
	    }
	    //clear old reports
	    clsGlobalVarClass.dbMysql.execute("delete from tbldayendreports "
		    + "where strPOSCode='" + posCode + "' "
		    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
	    //insert dy end reports             
	    clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());

	    //System.out.println("reportsSql->"+sqlBuilder);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	dialogDayEndReports.setVisible(false);
    }

    public void funGenerateReport(String reportName, String posCode, String posName, String posDate, int shift)
    {
	try
	{
	    String fromDate = posDate;
	    String toDate = posDate;

	    if (reportName.equalsIgnoreCase("Bill Wise Report".toUpperCase()))
	    {
		funGenerateBillWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Item Wise Report".toUpperCase()))
	    {
		funGenerateItemWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Group Wise Report".toUpperCase()))
	    {
		funGenerateGroupWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("SubGroupWise Report".toUpperCase()))
	    {
		funGenerateSubGroupWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("OperatorWise Report".toUpperCase()))
	    {
		funGenerateOperatorWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("SettlementWise Report".toUpperCase()))
	    {
		funGenerateSettlementWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Void Bill Report".toUpperCase()))
	    {
		funGenerateVoidBillExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Cost Centre Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Tax Wise Report".toUpperCase()))
	    {
		funGenerateTaxExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Cash Mgmt Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Audit Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Advance Order Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Stock In Out Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Day End Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("AvgItemPerBill".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("AvgPerCover".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("AvgTicketValue".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("DebitCardFlashReports".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Promotion Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Discount Report".toUpperCase()))
	    {
		funGenerateDiscountExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Group-SubGroup Wise Report".toUpperCase()))
	    {
		funGenerateGroupSubGroupWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Loyalty Point Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Complimentary Settlement Report".toUpperCase()))
	    {
		funGenerateComplimentaryWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Counter Wise Sales Report".toUpperCase()))
	    {
		funGenerateCounterWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Non Chargable KOT Report".toUpperCase()))
	    {
		funGenerateNCKOTExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Order Analysis Report".toUpperCase()))
	    {
		funGenerateOrderAnalysisExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Auditor Report".toUpperCase()))
	    {
		funGenerateAuditorExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Tax Breakup Summary Report".toUpperCase()))
	    {
		funGenerateTaxBreakUpExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Menu Head Wise".toUpperCase()))
	    {
		funGenerateMenuHeadWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("WaiterWiseItemReport".toUpperCase()))
	    {
		funGenerateWaiterWiseItemExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("WaiterWiseIncentivesReport".toUpperCase()))
	    {
		funGenerateWaiterWiseIncentivesExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("DeliveryboyIncentive".toUpperCase()))
	    {
		funGenerateDeliveryBoyIncentivesExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Sales Summary Flash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("POS Wise Sales".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Daily Collection Report".toUpperCase()))
	    {
		funGenerateDailyCollectionExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Daily Sales Report".toUpperCase()))
	    {
		funGenerateDailySalesExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Void KOT Report".toUpperCase()))
	    {
		funGenerateVoidKOTExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Guest Credit Report".toUpperCase()))
	    {
		funGenerateGuestCreditExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("SubGroupWiseSummaryReport".toUpperCase()))
	    {
		funGenerateSubGroupWiseSummaryExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("UnusedCardBalanceReport".toUpperCase()))
	    {
		funGenerateUnUsedCardBalanceExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("DayWiseSalesSummaryFlash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("BillWiseSettlementSalesSummaryFlash".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Revenue Head Wise Item Sales".toUpperCase()))
	    {
		funGenerateRevenueHeadWiseExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Managers Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Item Wise Consumption".toUpperCase()))
	    {
		funGenerateItemConsumptionExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Table Wise Pax Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("Posting Report".toUpperCase()))
	    {

	    }
	    else if (reportName.equalsIgnoreCase("WAITER WISE ITEM WISE INCENTIVES REPORT".toUpperCase()))
	    {
		funGenerateWaiterWiseItemWiseIncentivesExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Customer Wise Sales".toUpperCase()))
	    {
		funGenerateCustomerWiseSalesExcelReport(posCode, posName, fromDate, toDate);
	    }
	    else if (reportName.equalsIgnoreCase("Settlement Wise Group Wise Breakup".toUpperCase()))
	    {
		funGenerateSettlementWiseGroupWiseBreakupExcelReport(posCode, posName, fromDate, toDate);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetReportCount()
    {
	int noOfReports = 0;
	for (int i = 0; i < tblDayEndReports.getRowCount(); i++)
	{
	    if (Boolean.parseBoolean(tblDayEndReports.getValueAt(i, 1).toString()))
	    {
		noOfReports = noOfReports + 1;
	    }
	}
	NOOFREPORTS = noOfReports;
	lblNoOfReports.setText("");
	lblNoOfReports.setText("No Of Reports   :" + NOOFREPORTS);
	System.out.println("counter=" + NOOFREPORTS);
    }

    private void funSendDBBackupAndErrorLogFileToSanguineAuditiing(String dbBackupFilePath)
    {
	Date dt = new Date();
	String dateTime = "", date = "";
	dateTime = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900) + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	date = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);

	String filePath = System.getProperty("user.dir");
	File logFile = new File(filePath + "/ErrorLogs/err " + date + ".txt");

	dbBackupFilePath = System.getProperty("user.dir") + "\\DBBackup\\" + dbBackupFilePath + ".sql";
	//String filePath = System.getProperty("user.dir")+"/DBBackup/1.sql";
	File dbBackupFile = new File(dbBackupFilePath);

	objUtility2.funSendDBBackupAndErrorLogFileOnDayEnd(logFile, dbBackupFile);
    }

    private void funGenerateCustomerWiseSalesExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = funGetCommonHashMapForExcelReport(posCode, posName, fromDate, toDate);

	hm.put("groupCode", "All");
	hm.put("subGroupCode", "All");

	clsCustomerWiseSales objCustomerWiseSales = new clsCustomerWiseSales();
	objCustomerWiseSales.funGenerateCustomerWiseSalesExcelReport("Excel Report", hm, "Yes", "Detail");
    }

    private void funGenerateSettlementWiseGroupWiseBreakupExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	frmSettlementWiseGroupWiseBreakup objSettlementWiseGroupWiseBreakup = new frmSettlementWiseGroupWiseBreakup();
	objSettlementWiseGroupWiseBreakup.funGenerateExcelReport(posCode,fromDate,toDate);
    }
	    
    private void funCheckYesterdaysReport()
    {
	try
	{
	    sql = "select a.strPOSCode,date(dtePOSDate)dtePOSDate,intShiftCode,strShiftEnd,strDayEnd,if(dteDayEndReportsDateTime is null,'N','Y')dteDayEndReportsDateTime "
		    + ",b.strPosName "
		    + "from tbldayendprocess a,tblposmaster b "
		    + "where a.strPOSCode=b.strPosCode "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
		    + "and strDayEnd='Y'  "
		    + "and strShiftEnd='Y' "
		    + "order by a.dtePOSDate desc "
		    + "limit 1 ";
	    ResultSet rsPendingDayEndReports = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsPendingDayEndReports.next())
	    {
		String posCode = rsPendingDayEndReports.getString(1);
		String posDate = rsPendingDayEndReports.getString(2);
		int shiftNo = rsPendingDayEndReports.getInt(3);
		String isPendingDayEndReportsSend = rsPendingDayEndReports.getString(6);
		String posName = rsPendingDayEndReports.getString(7);

		if (isPendingDayEndReportsSend.equalsIgnoreCase("N"))
		{

		    funSendDayEndReports(posCode, posName, posDate, shiftNo);

		    clsSendMail objSendMail = new clsSendMail();
		    objSendMail.funSendMail(posCode, posName, posDate, shiftNo, clsGlobalVarClass.gClientCode);
		}
	    }
	    rsPendingDayEndReports.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    class clsOrderAnalysisColumns
    {

	private String itemName;

	private String itemCode;

	private String KOTNo;

	private double saleQty;

	private double CompQty;

	private double NCQty;

	private double voidQty;

	private double compliQty;

	private double voidKOTQty;

	private double itemSaleRate;

	private double itemPurchaseRate;

	private double totalAmt;

	private double totalCostValue;

	private double totalDiscountAmt;

	private double finalItemQty;

	public String getItemName()
	{
	    return itemName;
	}

	public void setItemName(String itemName)
	{
	    this.itemName = itemName;
	}

	public String getItemCode()
	{
	    return itemCode;
	}

	public void setItemCode(String itemCode)
	{
	    this.itemCode = itemCode;
	}

	public String getKOTNo()
	{
	    return KOTNo;
	}

	public void setKOTNo(String KOTNo)
	{
	    this.KOTNo = KOTNo;
	}

	public double getSaleQty()
	{
	    return saleQty;
	}

	public void setSaleQty(double saleQty)
	{
	    this.saleQty = saleQty;
	}

	public double getNCQty()
	{
	    return NCQty;
	}

	public void setNCQty(double NCQty)
	{
	    this.NCQty = NCQty;
	}

	public double getVoidQty()
	{
	    return voidQty;
	}

	public void setVoidQty(double voidQty)
	{
	    this.voidQty = voidQty;
	}

	public double getVoidKOTQty()
	{
	    return voidKOTQty;
	}

	public void setVoidKOTQty(double voidKOTQty)
	{
	    this.voidKOTQty = voidKOTQty;
	}

	public double getItemSaleRate()
	{
	    return itemSaleRate;
	}

	public void setItemSaleRate(double itemSaleRate)
	{
	    this.itemSaleRate = itemSaleRate;
	}

	public double getItemPurchaseRate()
	{
	    return itemPurchaseRate;
	}

	public void setItemPurchaseRate(double itemPurchaseRate)
	{
	    this.itemPurchaseRate = itemPurchaseRate;
	}

	public double getTotalAmt()
	{
	    return totalAmt;
	}

	public void setTotalAmt(double totalAmt)
	{
	    this.totalAmt = totalAmt;
	}

	public double getTotalCostValue()
	{
	    return totalCostValue;
	}

	public void setTotalCostValue(double totalCostValue)
	{
	    this.totalCostValue = totalCostValue;
	}

	public double getTotalDiscountAmt()
	{
	    return totalDiscountAmt;
	}

	public void setTotalDiscountAmt(double totalDiscountAmt)
	{
	    this.totalDiscountAmt = totalDiscountAmt;
	}

	public double getFinalItemQty()
	{
	    return finalItemQty;
	}

	public void setFinalItemQty(double finalItemQty)
	{
	    this.finalItemQty = finalItemQty;
	}

	public double getCompliQty()
	{
	    return compliQty;
	}

	public void setCompliQty(double compliQty)
	{
	    this.compliQty = compliQty;
	}

	public double getCompQty()
	{
	    return CompQty;
	}

	public void setCompQty(double CompQty)
	{
	    this.CompQty = CompQty;
	}

    }

    private HashMap funGetCommonHashMapForExcelReport(String posCode, String posName, String fromDate, String toDate)
    {
	HashMap hm = new HashMap();

	String[] arrFromDate = fromDate.split("-");
	String[] arrToDate = toDate.split("-");

	String fromDateToDisplay = arrFromDate[2] + "-" + arrFromDate[1] + "-" + arrFromDate[0];
	String toDateToDisplay = arrToDate[2] + "-" + arrToDate[1] + "-" + arrToDate[0];

	String shiftNo = "All", shiftCode = "All";
	if (clsGlobalVarClass.gEnableShiftYN)
	{
	    shiftNo = String.valueOf(clsGlobalVarClass.gShiftNo);
	    shiftCode = String.valueOf(clsGlobalVarClass.gShiftNo);
	}

	String imagePath = System.getProperty("user.dir");
	imagePath = imagePath + File.separator + "ReportImage";
	if (posCode.equalsIgnoreCase("All"))
	{
	    imagePath = imagePath + File.separator + "imgClientImage.jpg";
	}
	else
	{
	    imagePath = imagePath + File.separator + "img" + posCode + ".jpg";
	}

	File imgFile = new File(imagePath);
	if (!imgFile.exists())
	{
	    imagePath = getClass().getResource("/com/POSReport/images/imgSanguineLogo.png").toString();
	}
	System.out.println("imagePath=" + imagePath);

	hm.put("posName", posName);
	hm.put("fromDate", fromDate);
	hm.put("toDate", toDate);
	hm.put("userName", clsGlobalVarClass.gUserName);
	hm.put("posCode", posCode);
	hm.put("userCode", clsGlobalVarClass.gUserCode);
	hm.put("imagePath", imagePath);
	hm.put("clientName", clsGlobalVarClass.gClientName);
	hm.put("fromDateToDisplay", fromDateToDisplay);
	hm.put("toDateToDisplay", toDateToDisplay);
	hm.put("shiftNo", shiftNo);
	hm.put("shiftCode", shiftCode);
	hm.put("currency", "BASE");
	
	

	return hm;
    }
}
