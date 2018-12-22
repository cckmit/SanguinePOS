package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSendMail;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSPrinting.Text.DayEnd.clsDayEndTextReport;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmShiftEndProcessConsolidate extends javax.swing.JFrame
{

    private DefaultTableModel dm, dm1, dmSettlementTotal, dmSettlementTable;
    private Object[] gridRecords, totalRecords, discountRecords, settGridRecords, totalSettRecords;
    private double totalWithdrawl, totalPayments, totalTransOuts, totalFloat, totalTransIn;
    private double sales, cashIn, cashOut, advCash, totalSales, totalDiscount;
    private String posDate, sql;
    private int noOfDiscountedBills;
    private double dblApproxSaleAmount = 0.00;
    private int shiftNo;
    private Map<String, String> hmPOS = new HashMap<String, String>();
    clsUtility objUtility;
    private clsUtility2 objUtility2;

    public frmShiftEndProcessConsolidate()
    {
        initComponents();
        try
        {
            objUtility = new clsUtility();
            objUtility2 = new clsUtility2();

            lblShiftNo.setText("Shift No - " + shiftNo);
            gridRecords = new Object[11];
            totalRecords = new Object[11];
            discountRecords = new Object[4];
            settGridRecords = new Object[4];
            totalSettRecords = new Object[4];
            lblPOSName1.setText(clsGlobalVarClass.gPOSName);
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
                    + "where strDayEnd='N' ";
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
            dmSettlementTable.addColumn("No Of Bills");
            dmSettlementTotal = new DefaultTableModel();
            dmSettlementTotal.addColumn("");
            dmSettlementTotal.addColumn("");
            dmSettlementTotal.addColumn("");

            funFillCurrencyGrid();
            funFillSettlementWiseSalesGrid();
            fun_FillTable_SaleInProgress();
            fun_FillTable_UnsettleBills();
            lblApproximateTotal.setText(String.valueOf(dblApproxSaleAmount));

            funFillPOS();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillPOS() throws Exception
    {
        hmPOS.clear();
        sql = "select strPOSCode,strPOSName from tblposmaster ";
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rs.next())
        {
            hmPOS.put(rs.getString(1), rs.getString(2));
        }
        rs.close();
    }

    public void funFillCurrencyGrid()
    {
        try
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
                    + " and date(a.dteBillDate ) ='" + posDate + "' "
                    + " and c.strSettelmentType='Cash' and a.intShiftCode=" + shiftNo + " GROUP BY c.strSettelmentDesc";
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
                    + "Where date(dteBillDate ) ='" + posDate + "' "
                    + "and dblDiscountAmt > 0.00 and intShiftCode=" + shiftNo + " ";
            ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTotalDiscountBills.next())
            {
                noOfDiscountedBills = rsTotalDiscountBills.getInt(1);
            }

            int totalBillNo = 0;
            sql = "select count(strBillNo) from tblbillhd where date(dteBillDate ) ='" + posDate + "' and intShiftCode='" + shiftNo + "' ";
            ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            if (rsTotalBills.next())
            {
                totalBillNo = rsTotalBills.getInt(1);
            }
            totalRecords[0] = "Total Sales";
            totalRecords[1] = totalSales;
            totalRecords[8] = totalBillNo;

            dm1.addRow(totalRecords);
            dm1.addRow(discountRecords);
            sql = "select count(dblAdvDeposite) from tbladvancereceipthd "
                    + " where dtReceiptDate='" + posDate + "' and intShiftCode=" + shiftNo;
            ResultSet rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsTotalAdvance.next();
            int count = rsTotalAdvance.getInt(1);
            if (count > 0)
            {
                //sql="select sum(dblAdvDeposite) from tbladvancereceipthd where dtReceiptDate='"+posDate+"'";
                sql = "select sum(b.dblAdvDepositesettleAmt) from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
                        + " where date(a.dtReceiptDate)='" + posDate + "' and intShiftCode=" + shiftNo + " "
                        + "and c.strSettelmentCode=b.strSettlementCode "
                        + " and a.strReceiptNo=b.strReceiptNo and c.strSettelmentType='Cash'";
                System.out.println(sql);
                rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsTotalAdvance.next();
                advCash = Double.parseDouble(rsTotalAdvance.getString(1));
                tblDayEnd.setValueAt(advCash, 0, 4);
            }

            sql = "select strTransType,sum(dblAmount),strCurrencyType from tblcashmanagement "
                    + "where dteTransDate='" + posDate + "' and intShiftCode=" + shiftNo
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
                    + "and date(dteBillDate ) ='" + posDate + "'";
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
            tblDayEnd.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funFillSettlementWiseSalesGrid()
    {
        try
        {
            totalDiscount = 0;
            totalSales = 0;
            sql = "SELECT c.strSettelmentDesc,sum(b.dblSettlementAmt),sum(a.dblDiscountAmt) FROM tblbillhd a, tblbillsettlementdtl b"
                    + ", tblsettelmenthd c Where a.strBillNo = b.strBillNo and b.strSettlementCode = c.strSettelmentCode "
                    + " and date(a.dteBillDate ) ='" + posDate + "' "
                    + " and intShiftCode=" + shiftNo
                    + " GROUP BY c.strSettelmentDesc,a.strPosCode";
            //System.out.println(sql);
            ResultSet rsSettlementSale = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsSettlementSale.next())
            {
                settGridRecords[0] = rsSettlementSale.getString(1);
                settGridRecords[1] = rsSettlementSale.getString(2);

                totalDiscount = totalDiscount + (Double.parseDouble(rsSettlementSale.getString(3).toString()));

                totalSales = totalSales + (Double.parseDouble(rsSettlementSale.getString(2).toString()));

                dmSettlementTable.addRow(settGridRecords);
            }
            rsSettlementSale.close();
            noOfDiscountedBills = 0;
            sql = "SELECT count(strBillNo),sum(dblDiscountAmt) FROM tblbillhd "
                    + "Where date(dteBillDate ) ='" + posDate + "'  "
                    + "and dblDiscountAmt > 0.00 ";
            ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTotalDiscountBills.next())
            {
                noOfDiscountedBills = rsTotalDiscountBills.getInt(1);
            }
            //System.out.println("Discounts="+totalDiscount+"\tTotal Bills="+noOfDiscountedBills);
            int totalBillNo = 0;
            sql = "select count(strBillNo) from tblbillhd where date(dteBillDate ) ='" + posDate + "' ";
            ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            if (rsTotalBills.next())
            {
                totalBillNo = rsTotalBills.getInt(1);
            }
            totalSettRecords[0] = "Total Sales";
            totalSettRecords[1] = totalSales;
            totalSettRecords[2] = totalBillNo;

            discountRecords[0] = "Total Discount";
            discountRecords[1] = totalDiscount;
            discountRecords[2] = noOfDiscountedBills;
            dmSettlementTotal.addRow(totalSettRecords);
            dmSettlementTotal.addRow(discountRecords);
            //tblSettlementWiseSalesTotal

            tblSettlementWiseSales.setModel(dmSettlementTable);
            tblSettlementWiseSalesTotal.setModel(dmSettlementTotal);
            tblSettlementWiseSales.setRowHeight(25);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSettlementWiseSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
            if (tblSettlementWiseSales.getRowCount() > 0)
            {
                tblSettlementWiseSales.setValueAt(totalBillNo, 0, 2);
            }
            dblApproxSaleAmount += totalSales;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funUpdateDayEndTable()
    {
        try
        {
            funShiftStartProcess();
            lblShiftNo.setText(String.valueOf(shiftNo));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dispose();
    }

    public void funShiftStartProcess()
    {
        try
        {

            sql = "select strPOSCode from tblposmaster where strOperationalYN='Y' ";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                sql = "update tbldayendprocess set strShiftEnd='N' "
                        + "where strPOSCode='" + rsPOS.getString(1) + "' and strDayEnd='N' and strShiftEnd=''";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            rsPOS.close();

            if (shiftNo == 0)
            {
                shiftNo++;
            }

            sql = "select strPOSCode from tblposmaster where strOperationalYN='Y' ";
            rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                sql = "update tbldayendprocess set intShiftCode= " + shiftNo + " "
                        + "where strPOSCode='" + rsPOS.getString(1) + "' and strShiftEnd='N' and strDayEnd='N'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            rsPOS.close();

            clsGlobalVarClass.gShiftEnd = "N";
            clsGlobalVarClass.gDayEnd = "N";
            clsGlobalVarClass.gShiftNo = shiftNo;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funShiftEnd(String posCode)
    {

        try
        {

            if (clsGlobalVarClass.gEnableShiftYN)//for shift wise
            {
                sql = "delete from tblitemrtemp where strTableNo='null'";
                clsGlobalVarClass.dbMysql.execute(sql);

                clsGlobalVarClass.gDayEndReportForm = "DayEndReport";

                String sqlShift = "select date(max(dtePOSDate)),intShiftCode"
                        + " from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N'"
                        + " and (strShiftEnd='' or strShiftEnd='N')";
                ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlShift);
                if (rsShiftNo.next())
                {
                    shiftNo = rsShiftNo.getInt(2);
                }

                sql = "update tbltablemaster set strStatus='Normal' "
                        + " where strPOSCode='" + posCode + "' ";

                //                sql = "update tbldayendprocess set strShiftEnd='Y'"
                //                        + " where strPOSCode='" + posCode + "' and strDayEnd='N'";
                //                clsGlobalVarClass.dbMysql.execute(sql);
                objUtility.funGetNextShiftNoForShiftEnd(posCode, shiftNo);
            }
            else
            {

                sql = "delete from tblitemrtemp where strTableNo='null'";
                clsGlobalVarClass.dbMysql.execute(sql);

                clsGlobalVarClass.gDayEndReportForm = "DayEndReport";

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

                sql = "update tbltablemaster set strStatus='Normal' "
                        + " where strPOSCode='" + posCode + "'";

                sql = "update tbldayendprocess set strShiftEnd='Y'"
                        + " where strPOSCode='" + posCode + "' and strDayEnd='N'";
                clsGlobalVarClass.dbMysql.execute(sql);
                objUtility.funGetNextShiftNo(posCode, shiftNo);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funShiftEndButtonClicked()
    {
        try
        {
            if (clsGlobalVarClass.gEnableShiftYN)//for shift wise
            {
                boolean flgDayEnd = false;
                sql = "select strPosCode from tbldayendprocess  "
                        + " where intShiftCode>0 and strShiftEnd='N' "
                        + " and date(dtePOSDate) = '" + posDate + "' "
                        + " ORDER by strPosCode ";
                ResultSet rsPos = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsPos.next())
                {
                    funShiftEnd(rsPos.getString(1));
                }
                rsPos.close();
                sql = "select  sum(dblTotalSale),sum(dblTotalDiscount),sum(dblPayments) "
                        + " from tbldayendprocess where date(dtePOSDate)='" + posDate + "' "
                        + " and strDayEnd='Y'";
                ResultSet rsTotData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsTotData.next())
                {
                    String filePath = System.getProperty("user.dir");
                    filePath = filePath + "/Temp/Temp_DayEndReport.txt";

//                    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
//                    {
//                        clsTextFileGenerationForPrinting2 obj = new clsTextFileGenerationForPrinting2();
//                        obj.funGenerateTextDayEndReport("All", posDate, "", clsGlobalVarClass.gShiftNo, "Y");
//                    }
                    clsDayEndTextReport objDayEndTextReport = new clsDayEndTextReport();
                    objDayEndTextReport.funGenerateTextDayEndReport("All", posDate, "", clsGlobalVarClass.gShiftNo, "Y");

                    new clsSendMail().funSendMail(rsTotData.getDouble(1), rsTotData.getDouble(2), rsTotData.getDouble(3), filePath,clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo,clsGlobalVarClass.gClientCode);
                }
                rsTotData.close();
            }
            else
            {
                boolean flgDayEnd = false;
                sql = "select strPosCode from tbldayendprocess  "
                        + " where intShiftCode>0 and strShiftEnd='N' "
                        + " and date(dtePOSDate) = '" + posDate + "' "
                        + " ORDER by strPosCode ";
                ResultSet rsPos = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsPos.next())
                {
                    funShiftEnd(rsPos.getString(1));
                }
                rsPos.close();
                sql = "select  sum(dblTotalSale),sum(dblTotalDiscount),sum(dblPayments) "
                        + " from tbldayendprocess where date(dtePOSDate)='" + posDate + "' "
                        + " and strDayEnd='Y'";
                ResultSet rsTotData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsTotData.next())
                {
                    String filePath = System.getProperty("user.dir");
                    filePath = filePath + "/Temp/Temp_DayEndReport.txt";

                    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
                    {
//                        clsTextFileGenerationForPrinting2 obj = new clsTextFileGenerationForPrinting2();
//                        obj.funGenerateTextDayEndReport("All", posDate, "", clsGlobalVarClass.gShiftNo, "Y");

                        clsDayEndTextReport objDayEndTextReport = new clsDayEndTextReport();
                        objDayEndTextReport.funGenerateTextDayEndReport("All", posDate, "", clsGlobalVarClass.gShiftNo, "Y");
                    }
                    new clsSendMail().funSendMail(rsTotData.getDouble(1), rsTotData.getDouble(2), rsTotData.getDouble(3), filePath,clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo,clsGlobalVarClass.gClientCode);
                }
                rsTotData.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            public void windowDeactivated(java.awt.event.WindowEvent evt)
            {
                formWindowDeactivated(evt);
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
        lblformName.setText("- Day End Consolidate");
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
        lblUserCode.setName(""); // NOI18N
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

        tblDayEnd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
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

        lblPaxNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPaxNo.setText("Total Pax   :");

        lblTotalPax.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalPax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPax.setText("0");
        lblTotalPax.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        tblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        tblTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null}
            },
            new String []
            {
                "Title 1"
            }
        ));
        tblTotal.setRowHeight(25);
        scrShiftEndTable2.setViewportView(tblTotal);

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
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

        btnShiftStart.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
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

        btnShiftEnd.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
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

        tblSettlementWiseSalesTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        tblSettlementWiseSalesTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null}
            },
            new String []
            {
                "Title 1"
            }
        ));
        tblSettlementWiseSalesTotal.setRowHeight(25);
        jScrollPane4.setViewportView(tblSettlementWiseSalesTotal);

        tblSettlementWiseSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
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

        jTableSelesUnderProgress.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
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

        lblUnsettleBills.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUnsettleBills.setText("Unsettle Bills");

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

        lblSalesUnderProgress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSalesUnderProgress.setText("Sales Under Progress");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotal.setText("Total   :");

        lblApproximateTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        panelBody.setBackground(new java.awt.Color(254, 254, 254));

        lblShiftNo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblShiftNo.setForeground(new java.awt.Color(0, 141, 255));
        lblShiftNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShiftNo.setText("Shift No - ");

        lblPOSName1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblPOSName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblShiftEnd.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
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
                .addGap(251, 251, 251)
                .addComponent(lblShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(263, 263, 263))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblShiftEnd)
                    .addComponent(lblShiftNo)
                    .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(310, 310, 310)
                        .addComponent(lblUnsettleBills, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(480, 480, 480)
                        .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(630, 630, 630)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(scrShiftEndTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(500, 500, 500)
                        .addComponent(lblPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(610, 610, 610)
                        .addComponent(lblTotalPax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(lblApproximateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(scrSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(lblSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(scrShiftEndTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(310, 310, 310)
                        .addComponent(scrUnsettledBills, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(290, 290, 290)
                        .addComponent(scrSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(lblSalesUnderProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(424, 424, 424)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(270, 270, 270)
                                .addComponent(lblUnsettleBills, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(scrShiftEndTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(417, 417, 417)
                                .addComponent(lblApproximateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrShiftEndTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(290, 290, 290)
                                .addComponent(scrUnsettledBills, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(420, 420, 420)
                                .addComponent(lblTotalPax, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(420, 420, 420)
                                .addComponent(lblPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26))
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

        if (btnShiftEnd.isEnabled())
        {
            try
            {
                String filePath = "";
                if (clsGlobalVarClass.gEnableShiftYN)//for shift wise
                {
                    sql = "select strposcode from tblposmaster where strOperationalYN='Y' ";
                    ResultSet rsPos = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    while (rsPos.next())
                    {
                        if (objUtility.funCheckPendingBills(rsPos.getString("strposcode")))
                        {
                            JOptionPane.showMessageDialog(this, "Please Settle The Pending Bills.");
                            return;
                        }
                        else if (objUtility.funCheckTableBusy(rsPos.getString("strposcode")))
                        {
                            JOptionPane.showMessageDialog(this, "Sorry Tables Are Busy Now.");
                            return;
                        }
                    }

                    int option = JOptionPane.showConfirmDialog(this, "Do you want To Consolidate Shift End?");

                    if (option == 0)
                    {
                        filePath = clsGlobalVarClass.funBackupDatabase();
                        funShiftEndButtonClicked();
                    }

                }
                else
                {
                    sql = "select strposcode from tblposmaster where strOperationalYN='Y' ";
                    ResultSet rsPos = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    while (rsPos.next())
                    {
                        if (objUtility.funCheckPendingBills(rsPos.getString("strposcode")))
                        {
                            JOptionPane.showMessageDialog(this, "Please Settle The Pending Bills.");
                            return;
                        }
                        else if (objUtility.funCheckTableBusy(rsPos.getString("strposcode")))
                        {
                            JOptionPane.showMessageDialog(this, "Sorry Tables Are Busy Now.");
                            return;
                        }
                    }

                    int option = JOptionPane.showConfirmDialog(this, "Do you want to End Day?");

                    if (option == 0)
                    {
                        if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
                        {
                            filePath = clsGlobalVarClass.funBackupDatabase();
                        }
                        funShiftEndButtonClicked();
                    }

                }
                final String backupFilePathMail = filePath;

                if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
                {
                    funSendDBBackupAndErrorLogFileToSanguineAuditiing(backupFilePathMail);
                }

                objUtility = null;
                int option = JOptionPane.showConfirmDialog(this, "Do You Want To Start Day ?");
                if (option == 0)
                {
                    funUpdateDayEndTable();
                    btnShiftEnd.setEnabled(true);
                    btnShiftStart.setEnabled(false);
                }
                System.exit(0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }//GEN-LAST:event_btnShiftEndMouseClicked

    private void tblSettlementWiseSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSettlementWiseSalesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementWiseSalesMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowDeactivated
    {//GEN-HEADEREND:event_formWindowDeactivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnShiftEnd;
    private javax.swing.JButton btnShiftStart;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableSelesUnderProgress;
    private javax.swing.JLabel lblApproximateTotal;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName1;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSalesUnderProgress;
    private javax.swing.JLabel lblShiftEnd;
    private javax.swing.JLabel lblShiftNo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalPax;
    private javax.swing.JLabel lblUnsettleBills;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrSalesUnderProgress;
    private javax.swing.JScrollPane scrShiftEndTable1;
    private javax.swing.JScrollPane scrShiftEndTable2;
    private javax.swing.JScrollPane scrUnsettledBills;
    private javax.swing.JTable tblDayEnd;
    private javax.swing.JTable tblSettlementWiseSales;
    private javax.swing.JTable tblSettlementWiseSalesTotal;
    private javax.swing.JTable tblTotal;
    private javax.swing.JTable tblUnsettleBills;
    // End of variables declaration//GEN-END:variables

    /*
     * Ritesh 09 Sept 2014
     */
    /*
     *
     */
    private void fun_FillTable_SaleInProgress()
    {
        try
        {
            double dblSaleInProgressAmount = 0.00;
            DefaultTableModel dm_SalesUnderProgress = (DefaultTableModel) jTableSelesUnderProgress.getModel();
            dm_SalesUnderProgress.setRowCount(0);

            String sql_FillTable = "select b.strTableName,sum(a.dblAmount) "
                    + " from tblitemrtemp a,tbltablemaster b "
                    + " where a.strTableNo=b.strTableNo and a.strNCKotYN='N' "
                    + " group by a.strTableNo";
            ResultSet rs_filltable = clsGlobalVarClass.dbMysql.executeResultSet(sql_FillTable);
            while (rs_filltable.next())
            {
                dblSaleInProgressAmount += rs_filltable.getDouble(2);
                Object[] ob =
                {
                    rs_filltable.getString(1), rs_filltable.getString(2)
                };
                dm_SalesUnderProgress.addRow(ob);
            }
            Object[] BlankRow =
            {
                "", ""
            };
            dm_SalesUnderProgress.addRow(BlankRow);
            Object[] totalRow =
            {
                "Total", dblSaleInProgressAmount
            };
            dm_SalesUnderProgress.addRow(totalRow);
            rs_filltable.close();
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            jTableSelesUnderProgress.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            jTableSelesUnderProgress.setModel(dm_SalesUnderProgress);
            dblApproxSaleAmount += dblSaleInProgressAmount;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /*
     * Ritesh 09 Sept 2014
     */

    private void fun_FillTable_UnsettleBills()
    {
        try
        {
            double unSetteledBillAmount = 0.00;
            DefaultTableModel dm_UnsettledBills = (DefaultTableModel) tblUnsettleBills.getModel();
            dm_UnsettledBills.setRowCount(0);
            String sql_UnsettledBillsDina = "select a.strBillNo,c.strTableName,a.dblGrandTotal "
                    + "from tblbillhd a,tbltablemaster c "
                    + "where  date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
                    + "and a.strTableNo=c.strTableNo and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) ";

            ResultSet rs_UnsettledBills = clsGlobalVarClass.dbMysql.executeResultSet(sql_UnsettledBillsDina);
            while (rs_UnsettledBills.next())
            {
                unSetteledBillAmount += rs_UnsettledBills.getDouble(3);
                Object[] ob =
                {
                    rs_UnsettledBills.getString(1), rs_UnsettledBills.getString(2), rs_UnsettledBills.getString(3)
                };
                dm_UnsettledBills.addRow(ob);
            }
            rs_UnsettledBills.close();
            String sql_UnsettledBillDirectBiller = "select a.strBillNo,a.dblGrandTotal "
                    + "from tblbillhd a "
                    + "where a.strTableNo='' and  date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
                    + "and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) ";
            ResultSet rs_UnsettledBills_DirectBiller = clsGlobalVarClass.dbMysql.executeResultSet(sql_UnsettledBillDirectBiller);
            while (rs_UnsettledBills_DirectBiller.next())
            {
                unSetteledBillAmount += rs_UnsettledBills_DirectBiller.getDouble(2);
                Object[] ob =
                {
                    rs_UnsettledBills_DirectBiller.getString(1), "Direct Biller", rs_UnsettledBills_DirectBiller.getString(2)
                };
                dm_UnsettledBills.addRow(ob);
            }
            rs_UnsettledBills_DirectBiller.close();
            Object[] blankRow =
            {
                "", " ", ""
            };
            dm_UnsettledBills.addRow(blankRow);
            Object[] TotalRow =
            {
                "Total", " ", unSetteledBillAmount
            };
            dm_UnsettledBills.addRow(TotalRow);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tblUnsettleBills.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblUnsettleBills.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tblUnsettleBills.setModel(dm_UnsettledBills);
            dblApproxSaleAmount += unSetteledBillAmount;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
}
