/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBackupDatabase;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSendMail;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class frmShiftEndWithoutDetails extends javax.swing.JFrame
{

    private String sql, shiftDate, posDate;
    private double totalSales, totalDiscount, totalPayments;
    private int shiftNo;
    clsUtility objUtility;
    private clsUtility2 objUtility2;
    private static int NOOFREPORTS = 0;
    private JCheckBox chkBoxSelectAll;
    public frmShiftEndWithoutDetails()
    {
        initComponents();

        try
        {
            objUtility = new clsUtility();
            objUtility2 = new clsUtility2();

            lblShiftNo.setText("Shift No - " + clsGlobalVarClass.gShiftNo);
            lblPOSName1.setText(clsGlobalVarClass.gPOSName);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            String bdte = clsGlobalVarClass.gPOSStartDate;
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);
            String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            posDate = date1;

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
            sql = "select dtePOSDate,intShiftCode from tbldayendprocess "
                    + "where strDayEnd='N' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and (strShiftEnd='' or strShiftEnd='N') ";
            ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsShiftNo.next())
            {
                shiftDate = rsShiftNo.getString(1).substring(0, rsShiftNo.getString(1).indexOf(" "));
                lblShiftEnd.setText(clsGlobalVarClass.funConvertDateToSimpleFormat(rsShiftNo.getString(1)));
                lblShiftNo.setText("Shift No - " + rsShiftNo.getString(2));
                shiftNo = Integer.parseInt(rsShiftNo.getString(2));
            }
            rsShiftNo.close();

            java.util.Date dt1 = new java.util.Date();
            int day = dt1.getDate();
            int month = dt1.getMonth() + 1;
            int year = dt1.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            sql = "select date(max(dtePOSDate)) from tbldayendprocess where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
            ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsDayEnd.next();
            String sDate = rsDayEnd.getString(1);
            lblShiftEnd.setText(sDate);
            rsDayEnd.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    public void funUpdateDayEndTable()
    {
        try
        {
            System.out.println("Shift=" + clsGlobalVarClass.gShifts + "\tShift no=" + clsGlobalVarClass.gShiftNo);
            /*
             * if(clsGlobalVarClass.gShiftNo==0 &&
             * clsGlobalVarClass.gShifts==false) { sql="update tbldayendprocess
             * set intShiftCode=1,strShiftEnd='N' where strDayEnd='N'" + " and
             * strPOSCode='"+clsGlobalVarClass.gPOSCode+"'"; int
             * exc=clsGlobalVarClass.dbMysql.execute(sql);
             * clsGlobalVarClass.gShiftEnd="N"; clsGlobalVarClass.gDayEnd="N";
             * clsGlobalVarClass.gShiftNo=0;
             * lblShiftNo.setText(String.valueOf(clsGlobalVarClass.gShiftNo)); }
             * else if(clsGlobalVarClass.gShiftNo!=0 &&
             * clsGlobalVarClass.gShifts==true) { sql="update tbldayendprocess
             * set intShiftCode=1,strShiftEnd='N' where strDayEnd='N'" + " and
             * strPOSCode='"+clsGlobalVarClass.gPOSCode+"'"; int
             * exc=clsGlobalVarClass.dbMysql.execute(sql);
             * clsGlobalVarClass.gShiftEnd="N"; clsGlobalVarClass.gDayEnd="N";
             * clsGlobalVarClass.gShiftNo=1;
             * lblShiftNo.setText(String.valueOf(clsGlobalVarClass.gShiftNo)); }
             * else { funShiftStartProcess();
             * lblShiftNo.setText(String.valueOf(clsGlobalVarClass.gShiftNo)); }
             */

            funShiftStartProcess();
            lblShiftNo.setText(String.valueOf(clsGlobalVarClass.gShiftNo));

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        dispose();
    }

    public void funGetNextShiftNo(String posCode)
    {
        int shiftCount = 0;
        try
        {
            if (clsGlobalVarClass.gShifts)
            {
                sql = "select count(intShiftCode) from tblshiftmaster where strPOSCode='" + posCode + "'";
                ResultSet rsShiftNoCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsShiftNoCount.next();
                shiftCount = rsShiftNoCount.getInt(1);
                rsShiftNoCount.close();
                if (shiftCount > 0)
                {
                    if (clsGlobalVarClass.gShiftNo == shiftCount)
                    {
                        //clsGlobalVarClass.gShiftNo=1;
                        funShiftEndProcess("DayEnd", posCode);
                    }
                    else
                    {
                        clsGlobalVarClass.gShiftNo++;
                        funShiftEndProcess("ShiftEnd", posCode);
                    }
                }
            }
            else
            {
                funShiftEndProcess("DayEnd", posCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funShiftStartProcess() throws Exception
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

    private boolean funInsertQBillData(String posCode)
    {
        boolean flgResult = false;

        try
        {
            String sqlAdvRecDtl = "delete from tblqadvancereceiptdtl "
                    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
                    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
            sqlAdvRecDtl = "insert into tblqadvancereceiptdtl "
                    + "(select * from tbladvancereceiptdtl "
                    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
                    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
            sqlAdvRecDtl = "delete from tbladvancereceiptdtl "
                    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
                    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
            System.out.println("Adv Rec Dtl");

            String sqlAdvRecHd = "delete from tblqadvancereceipthd where strReceiptNo in "
                    + " (select strReceiptNo from tbladvancereceipthd "
                    + " where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            //System.out.println(sqlAdvRecHd);
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);
            sqlAdvRecHd = "insert into tblqadvancereceipthd "
                    + "(select * from tbladvancereceipthd "
                    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            //System.out.println(sqlAdvRecHd);
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);

            sqlAdvRecHd = "delete from tbladvancereceipthd where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            //System.out.println(sqlAdvRecHd);
            clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);
            System.out.println("Adv Rec Hd");

            String sqlAdvBookDtl = "delete from tblqadvbookbilldtl where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
            sqlAdvBookDtl = "insert into tblqadvbookbilldtl "
                    + " (select * from tbladvbookbilldtl where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
            sqlAdvBookDtl = "delete from tbladvbookbilldtl where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
            System.out.println("Adv Dtl");

            String sqlAdvBookModDtl = "delete from tblqadvordermodifierdtl where strAdvOrderNo in "
                    + " (select strAdvOrderNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
            sqlAdvBookModDtl = "insert into tblqadvordermodifierdtl "
                    + " (select * from tbladvordermodifierdtl where strAdvOrderNo in "
                    + " (select strAdvOrderNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
            sqlAdvBookModDtl = "delete from tbladvordermodifierdtl where strAdvOrderNo in "
                    + " (select strAdvOrderNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
            System.out.println("Adv Mod Dtl");

            String sqlAdvBookHd = "delete from tblqadvbookbillhd where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
            sqlAdvBookHd = "insert into tblqadvbookbillhd "
                    + " (select * from tbladvbookbillhd where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
            sqlAdvBookHd = "delete from tbladvbookbillhd where strAdvBookingNo in "
                    + " (select strAdvBookingNo from tblbillhd "
                    + " where strPOSCode='" + posCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
            System.out.println("Adv Hd");

            String qSqlBillDtl = "delete from tblqbilldtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
            qSqlBillDtl = "insert into tblqbilldtl (select * from tblbilldtl "
                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
            clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
            qSqlBillDtl = "delete from tblbilldtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
            System.out.println("Bill Dtl");

            String qSqlBillSettDtl = "delete from tblqbillsettlementdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
            qSqlBillSettDtl = "insert into tblqbillsettlementdtl (select * from tblbillsettlementdtl "
                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
            clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
            qSqlBillSettDtl = "delete from tblbillsettlementdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
            System.out.println("Bill Sett Dtl");

            String qSqlBillModDtl = "delete from tblqbillmodifierdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
            qSqlBillModDtl = "insert into tblqbillmodifierdtl (select * from tblbillmodifierdtl "
                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
            clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
            qSqlBillModDtl = "delete from tblbillmodifierdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
            System.out.println("Bill Mod Dtl");

            String qSqlBillTaxDtl = "delete from tblqbilltaxdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
            qSqlBillTaxDtl = "insert into tblqbilltaxdtl (select * from tblbilltaxdtl "
                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
            clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
            qSqlBillTaxDtl = "delete from tblbilltaxdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
            System.out.println("Bill Tax Dtl");

            String qSqlBillHd = "delete from tblqbillhd where strPOSCode='" + posCode + "'"
                    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
            clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
            qSqlBillHd = "insert into tblqbillhd (select * from tblbillhd "
                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' and strPOSCode='" + posCode + "') ";
            clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
            qSqlBillHd = "delete from tblbillhd where strPOSCode='" + posCode + "'";
            clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
            System.out.println("Bill HD");

            flgResult = true;

        }
        catch (Exception e)
        {
            flgResult = false;
            JOptionPane.showMessageDialog(null, "Qfile Data Posting failed!!!");
            e.printStackTrace();
        }
        finally
        {
            return flgResult;
        }
    }

    private void funBackupDatabase() throws Exception
    {
        clsBackupDatabase objDBBackup = new clsBackupDatabase();
        objDBBackup.funTakeBackUpDB();
    }

    private int funPostBillDataToCMS(String posCode) throws Exception
    {
        int res = 0;
        double roundOff = 0, creditAmt = 0, debitAmt = 0;
        try
        {
            JSONObject jObj = new JSONObject();
            JSONArray arrObj = new JSONArray();

            String sql_SubGroupWise = "select a.strPOSCode,ifnull(d.strSubGroupCode,'NA'),ifnull(d.strSubGroupName,'NA')"
                    + ",sum(b.dblAmount),date(a.dteBillDate) "
                    + "from tblbillhd a left outer join tblbilldtl b on a.strBillNo=b.strBillNo "
                    + "left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
                    + "left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
                    + "where a.strPOSCode='" + posCode + "' "
                    + "group by d.strSubGroupCode,d.strSubGroupName";

            ResultSet rsSubGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_SubGroupWise);
            while (rsSubGroupWise.next())
            {
                JSONObject objSubGroupWise = new JSONObject();
                objSubGroupWise.put("RVCode", rsSubGroupWise.getString(1) + "-" + rsSubGroupWise.getString(2));
                objSubGroupWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSubGroupWise.getString(3));
                objSubGroupWise.put("CRAmt", rsSubGroupWise.getDouble(4));
                objSubGroupWise.put("DRAmt", 0);
                objSubGroupWise.put("ClientCode", clsGlobalVarClass.gClientCode);
                objSubGroupWise.put("BillDate", rsSubGroupWise.getString(5));
                objSubGroupWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                objSubGroupWise.put("POSCode", posCode);
                objSubGroupWise.put("BillDateTo", rsSubGroupWise.getString(5));
                arrObj.add(objSubGroupWise);
            }
            rsSubGroupWise.close();

            String sql_TaxWise = "select a.strPOSCode,c.strTaxCode,c.strTaxDesc,sum(b.dblTaxAmount),date(a.dteBillDate) "
                    + "from tblbillhd a left outer join tblbilltaxdtl b on a.strBillNo=b.strBillNo "
                    + "left outer join tbltaxhd c on b.strTaxCode=c.strTaxCode "
                    + "where a.strPOSCode='" + posCode + "' "
                    + "group by c.strTaxCode";
            ResultSet rsTaxWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxWise);
            while (rsTaxWise.next())
            {
                JSONObject objTaxWise = new JSONObject();
                objTaxWise.put("RVCode", rsTaxWise.getString(1) + "-" + rsTaxWise.getString(2));
                objTaxWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsTaxWise.getString(3));
                objTaxWise.put("CRAmt", rsTaxWise.getDouble(4));
                objTaxWise.put("DRAmt", 0);
                objTaxWise.put("ClientCode", clsGlobalVarClass.gClientCode);
                objTaxWise.put("BillDate", rsTaxWise.getString(5));
                objTaxWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                objTaxWise.put("POSCode", posCode);
                objTaxWise.put("BillDateTo", rsTaxWise.getString(5));
                arrObj.add(objTaxWise);
            }
            rsTaxWise.close();

            String sql_Discount = "select strPOSCode,sum(dblDiscountAmt),date(dteBillDate) "
                    + "from tblbillhd "
                    + "where strPOSCode='" + posCode + "' "
                    + "group by strPOSCode";
            ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sql_Discount);
            while (rsDiscount.next())
            {
                JSONObject objDiscount = new JSONObject();
                objDiscount.put("RVCode", rsDiscount.getString(1) + "-Discount");
                objDiscount.put("RVName", "Discount");
                objDiscount.put("CRAmt", 0);
                objDiscount.put("DRAmt", rsDiscount.getDouble(2));
                objDiscount.put("ClientCode", clsGlobalVarClass.gClientCode);
                objDiscount.put("BillDate", rsDiscount.getString(3));
                objDiscount.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                objDiscount.put("POSCode", posCode);
                objDiscount.put("BillDateTo", rsDiscount.getString(3));
                arrObj.add(objDiscount);
            }
            rsDiscount.close();

            /*
             * String sql_RoundOff="SELECT strPOSCode,sum((dbltaxamt +
             * dblsubtotal) - dblgrandtotal)" + ",date(dteBillDate) " + "from
             * tblbillhd where strPOSCode= '"+clsGlobalVarClass.gPOSCode+"'";
             * ResultSet
             * rsRoundOff=clsGlobalVarClass.dbMysql.executeResultSet(sql_RoundOff);
             * while(rsRoundOff.next()) { JSONObject objRoundOff=new
             * JSONObject();
             * objRoundOff.put("RVCode",rsRoundOff.getString(1)+"-Roff");
             * objRoundOff.put("RVName",clsGlobalVarClass.gPOSName+"-Roff");
             * objRoundOff.put("CRAmt",0);
             * objRoundOff.put("DRAmt",rsRoundOff.getDouble(2));
             * objRoundOff.put("ClientCode",clsGlobalVarClass.gClientCode);
             * objRoundOff.put("BillDate",rsRoundOff.getString(3));
             * objRoundOff.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
             * objRoundOff.put("POSCode",clsGlobalVarClass.gPOSCode);
             * objRoundOff.put("BillDateTo",rsRoundOff.getString(3));
             * arrObj.add(objRoundOff); } rsRoundOff.close();
             */
            String sql_Settlement = "select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                    + " ,ifnull(c.strSettelmentDesc,''),ifnull(sum(b.dblSettlementAmt),0),date(a.dteBillDate) "
                    + " from tblbillhd a left outer join tblbillsettlementdtl b on a.strBillNo=b.strBillNo "
                    + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                    + " where c.strSettelmentType='Member' and a.strPOSCode='" + posCode + "' "
                    + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while (rsSettlement.next())
            {
                JSONObject objSettlementWise = new JSONObject();
                objSettlementWise.put("RVCode", rsSettlement.getString(1) + "-" + rsSettlement.getString(2));
                objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSettlement.getString(3));
                objSettlementWise.put("CRAmt", 0);
                objSettlementWise.put("DRAmt", rsSettlement.getDouble(4));
                objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate", rsSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode", posCode);
                objSettlementWise.put("BillDateTo", rsSettlement.getString(5));
                arrObj.add(objSettlementWise);
            }
            rsSettlement.close();

            sql_Settlement = "select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                    + " ,ifnull(c.strSettelmentDesc,''),ifnull(sum(b.dblSettlementAmt),0),date(a.dteBillDate) "
                    + " from tblbillhd a left outer join tblbillsettlementdtl b on a.strBillNo=b.strBillNo "
                    + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                    + " where c.strSettelmentType='Cash' and a.strPOSCode='" + posCode + "' "
                    + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while (rsSettlement.next())
            {
                JSONObject objSettlementWise = new JSONObject();
                objSettlementWise.put("RVCode", rsSettlement.getString(1) + "-" + rsSettlement.getString(2));
                objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSettlement.getString(3));
                objSettlementWise.put("CRAmt", 0);
                objSettlementWise.put("DRAmt", rsSettlement.getDouble(4));
                objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate", rsSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode", posCode);
                objSettlementWise.put("BillDateTo", rsSettlement.getString(5));
                arrObj.add(objSettlementWise);
            }
            rsSettlement.close();

            JSONObject objRoundOff = new JSONObject();
            objRoundOff.put("RVCode", clsGlobalVarClass.gPOSCode + "-Roff");
            objRoundOff.put("RVName", clsGlobalVarClass.gPOSName + "-Roff");
            roundOff = debitAmt - creditAmt;
            if (roundOff < 0)
            {
                roundOff = roundOff * (-1);
                objRoundOff.put("DRAmt", roundOff);
                objRoundOff.put("CRAmt", 0);
            }
            else
            {
                objRoundOff.put("DRAmt", 0);
                objRoundOff.put("CRAmt", roundOff);
            }
            objRoundOff.put("ClientCode", clsGlobalVarClass.gClientCode);
            objRoundOff.put("BillDate", posDate);
            objRoundOff.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
            objRoundOff.put("POSCode", posCode);
            objRoundOff.put("BillDateTo", posDate);
            arrObj.add(objRoundOff);

            jObj.put("BillInfo", arrObj);
            System.out.println(jObj);

            String cmsURL = clsGlobalVarClass.gCMSWebServiceURL + "/funPostRVDataToCMS";
            System.out.println(cmsURL);
            URL url = new URL(cmsURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jObj.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output = "", op = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null)
            {
                op += output;
            }
            System.out.println(op);
            conn.disconnect();
            if (op.equals("false"))
            {
                res = 0;
            }
            else
            {
                JSONObject jObjCL = new JSONObject();
                JSONArray arrObjCL = new JSONArray();
                /*
                 * String sql_MemberCL="select
                 * strCustomerCode,'',strBillNo,date(dteBillDate),dblGrandTotal
                 * " + "from tblbillhd " + "where
                 * strPOSCode='"+clsGlobalVarClass.gPOSCode+"' " + "and
                 * strSettelmentMode='Member'";
                 */
                String sql_MemberCL = "select a.strCustomerCode,d.strCustomerName,a.strBillNo,date(a.dteBillDate),b.dblSettlementAmt "
                        + "from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c,tblcustomermaster d "
                        + "where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                        + "and a.strCustomerCode=d.strCustomerCode "
                        + "and a.strPOSCode='" + posCode + "' "
                        + "and c.strSettelmentType='Member'";
                System.out.println(sql_MemberCL);
                ResultSet rsMemeberCL = clsGlobalVarClass.dbMysql.executeResultSet(sql_MemberCL);
                while (rsMemeberCL.next())
                {
                    JSONObject objMemeberCL = new JSONObject();
                    objMemeberCL.put("DebtorCode", rsMemeberCL.getString(1));
                    objMemeberCL.put("DebtorName", rsMemeberCL.getString(2));
                    objMemeberCL.put("BillNo", rsMemeberCL.getString(3));
                    objMemeberCL.put("BillDate", rsMemeberCL.getString(4));
                    objMemeberCL.put("BillAmt", rsMemeberCL.getDouble(5));
                    objMemeberCL.put("ClientCode", clsGlobalVarClass.gClientCode);
                    objMemeberCL.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
                    objMemeberCL.put("POSCode", posCode);
                    objMemeberCL.put("POSName", clsGlobalVarClass.gPOSName);
                    objMemeberCL.put("BillDateTo", rsMemeberCL.getString(4));
                    arrObjCL.add(objMemeberCL);
                }
                rsMemeberCL.close();

                jObjCL.put("MemberCLInfo", arrObjCL);
                System.out.println(jObjCL);
                String cmsURLCL = clsGlobalVarClass.gCMSWebServiceURL + "/funPostCLDataToCMS";
                System.out.println(cmsURLCL);
                URL urlCL = new URL(cmsURLCL);
                HttpURLConnection connCL = (HttpURLConnection) urlCL.openConnection();
                connCL.setDoOutput(true);
                connCL.setRequestMethod("POST");
                connCL.setRequestProperty("Content-Type", "application/json");
                OutputStream osCL = connCL.getOutputStream();
                osCL.write(jObjCL.toString().getBytes());
                osCL.flush();

                if (connCL.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + connCL.getResponseCode());
                }
                BufferedReader brCL = new BufferedReader(new InputStreamReader((connCL.getInputStream())));
                String output1 = "", op1 = "";
                System.out.println("Output from Server .... \n");
                while ((output1 = brCL.readLine()) != null)
                {
                    op1 += output1;
                }
                connCL.disconnect();
                System.out.println(op1);
                if (op1.equals("false"))
                {
                    res = 0;
                }
                else
                {
                    res = 1;
                }
            }
        }
        catch (Exception e)
        {
            res = 0;
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Check CMS Web Service URL and Internet Connection!!!");
        }
        finally
        {
            return res;
        }
    }

    public void funShiftEndProcess(String status, String posCode)
    {
        String newStartDate = "";
        int shiftEnd = 0;
        try
        {
            if (clsGlobalVarClass.gCMSIntegrationYN)
            {
                if (funPostBillDataToCMS(posCode) == 0)
                {
                    return;
                }
            }

            sql = "select count(*) from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N'";
            ResultSet rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsDayEndRecord.next();
            if (rsDayEndRecord.getInt(1) > 0)
            {
                rsDayEndRecord.close();
                sql = "select date(max(dtePOSDate)) from tbldayendprocess "
                        + "where strPOSCode='" + posCode + "'";
                rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsDayEndRecord.next();
                Date startDate = rsDayEndRecord.getDate(1);
                shiftDate = rsDayEndRecord.getString(1);
                if (status.equals("DayEnd"))
                {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(startDate);
                    cal.add(Calendar.DATE, 1);
                    newStartDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
                    rsDayEndRecord.close();
                }
                else
                {
                    newStartDate = shiftDate;
                }

                String dayEnd = "N";
                int shift = 0;
                //clsGlobalVarClass.dbMysql.funStartTransaction();
                if (status.equals("DayEnd"))
                {
                    sql = "update tbldayendprocess set strDayEnd='Y',strShiftEnd='Y' "
                            + "where strPOSCode='" + posCode + "' and strDayEnd='N'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    dayEnd = "Y";
                }
                else
                {
                    sql = "update tbldayendprocess set strDayEnd='N',strShiftEnd='Y' "
                            + "where strPOSCode='" + posCode + "' and strDayEnd='N'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    shift = clsGlobalVarClass.gShiftNo;
                }
                sql = "insert into tbldayendprocess(strPOSCode,dtePOSDate,strDayEnd,intShiftCode,strShiftEnd"
                        + ",strUserCreated,dteDateCreated) "
                        + "values('" + posCode + "','" + newStartDate + "','N'," + shift
                        + ",'','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
                clsGlobalVarClass.dbMysql.execute(sql);
                clsGlobalVarClass.gShiftEnd = "";
                clsGlobalVarClass.gDayEnd = "N";
                clsGlobalVarClass.setStartDate(newStartDate);
                clsGlobalVarClass.funSetPOSDate();
                System.out.println("Shift = " + clsGlobalVarClass.gShifts);

                if (status.equals("ShiftEnd"))
                {
                    shiftEnd = clsGlobalVarClass.gShiftNo - 1;
                }
                else
                {
                    shiftEnd = clsGlobalVarClass.gShiftNo;
                }
                objUtility.funCalculateDayEndCash(shiftDate, shiftEnd, posCode);
                objUtility.funUpdateDayEndFields(shiftDate, shiftEnd, dayEnd, posCode);
                lblShiftEnd.setText(clsGlobalVarClass.funConvertDateToSimpleFormat(newStartDate));

                if (funInsertQBillData(posCode))
                {
                    //clsGlobalVarClass.dbMysql.funCommitTransaction();
                }
                else
                {
                    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
                }
                if (clsGlobalVarClass.gConnectionActive.equals("Y"))
                {
                    if (clsGlobalVarClass.gDataSendFrequency.equals("After Day End"))
                    {
                        clsGlobalVarClass.funInvokeHOWebserviceForTrans("All", "Day End");
                        clsGlobalVarClass.funPostCustomerDataToHOPOS();
                        clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
                    }
                }
                if (clsGlobalVarClass.gPostSalesDataToMMS)
                {
                    clsGlobalVarClass.funPostItemSalesData(posCode, posDate, posDate);
                }
                clsGlobalVarClass.funPostDayEndData(newStartDate, shift);
                funDayEndflash();
            }
        }
        catch (SQLException e)
        {
            //clsGlobalVarClass.dbMysql.funRollbackTransaction();
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            //clsGlobalVarClass.dbMysql.funRollbackTransaction();
            ex.printStackTrace();
        }

        /*
         * if(clsGlobalVarClass.funSetMailServerProperties()) { try {
         * if(clsGlobalVarClass.funCreateEmailMessage(totalSales,totalDiscount,totalPayments))
         * { clsGlobalVarClass.funSendEmail(); } } catch (MessagingException ex)
         * { ex.printStackTrace(); } }
         */
        try
        {
            String filePath = System.getProperty("user.dir");
            filePath = filePath + "/Temp/Temp_DayEndReport.txt";
            new clsSendMail().funSendMail(totalSales, totalDiscount, totalPayments, filePath,clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo,clsGlobalVarClass.gClientCode);
            funBackupDatabase();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }

    private void funDayEndflash()
    {
        try
        {
            if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                //clsTextFileGeneratorForPrinting obj = new clsTextFileGeneratorForPrinting();
                //obj.funGenerateTextDayEndReport(poscode,billDate,"");
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
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  
        ;
        panelFormBody = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        lblShiftNo = new javax.swing.JLabel();
        lblShiftEnd = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnShiftStart = new javax.swing.JButton();
        btnShiftEnd = new javax.swing.JButton();
        lblPOSName1 = new javax.swing.JLabel();

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
        lblformName.setText("- Day End Process");
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

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelBody.setBackground(new java.awt.Color(254, 254, 254));

        lblShiftNo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblShiftNo.setForeground(new java.awt.Color(0, 141, 255));
        lblShiftNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShiftNo.setText("Shift No - ");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblShiftEnd)
                    .addComponent(lblShiftNo))
                .addGap(0, 22, Short.MAX_VALUE))
        );

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

        lblPOSName1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblPOSName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120)
                        .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShiftEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShiftStart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 343, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        // TODO add your handling code here:
        try
        {
            if (clsGlobalVarClass.gEnableShiftYN)//for shift wise
            {
                String sqlShift = "select date(max(dtePOSDate)),intShiftCode "
                        + " from tbldayendprocess "
                        + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N' "
                        + " and (strShiftEnd='' or strShiftEnd='N') ";
                ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlShift);
                if (rsShiftNo.next())
                {
                    shiftNo = rsShiftNo.getInt(2);
                }
                rsShiftNo.close();

                sql = "delete from tblitemrtemp where strTableNo='null' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                clsGlobalVarClass.gDayEndReportForm = "DayEndReport";
                if (objUtility.funCheckPendingBills(clsGlobalVarClass.gPOSCode))
                {
                    JOptionPane.showMessageDialog(this, "Please settle pending bills");
                }
                else if (objUtility.funCheckTableBusy(clsGlobalVarClass.gPOSCode))
                {
                    JOptionPane.showMessageDialog(this, "Sorry Tables are Busy Now");
                }
                else
                {
                    if (btnShiftEnd.isEnabled())
                    {
                        int option = JOptionPane.showConfirmDialog(this, "Do You Want To End Shift No." + shiftNo + " ?");
                        if (option == 0)
                        {
                            String backupFilePath = "";
                            if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
                            {
                                backupFilePath = clsGlobalVarClass.funBackupDatabase();
                            }
                            final String backupFilePathMail = backupFilePath;

                            sql = "update tbltablemaster set strStatus='Normal' "
                                    + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
                            clsGlobalVarClass.dbMysql.execute(sql);

                            if (clsGlobalVarClass.gGenrateMI)
                            {
                                frmGenrateMallInterfaceText objGenrateMallInterfaceText = new frmGenrateMallInterfaceText();
                                objGenrateMallInterfaceText.funWriteToFile(posDate, posDate, "Current", "Y");
                            }
                            objUtility.funGetNextShiftNoForShiftEnd(clsGlobalVarClass.gPOSCode, shiftNo);
                            btnShiftEnd.setEnabled(false);

                            JOptionPane.showMessageDialog(null, "Shift End Done Successfully!!!");

                            final String filePath = System.getProperty("user.dir") + "/Temp/Temp_DayEndReport.txt";

                            //send mail sales amount after shift end
                            new clsSendMail().funSendMail(totalSales, totalDiscount, totalPayments, filePath,clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo,clsGlobalVarClass.gClientCode);

                            if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
                            {
                                funSendDBBackupAndErrorLogFileToSanguineAuditiing(backupFilePathMail);
                            }

                            option = JOptionPane.showConfirmDialog(this, "Do You Want To Start Day ?");
                            if (option == 0)
                            {
                                funUpdateDayEndTable();
                                btnShiftEnd.setEnabled(true);
                                btnShiftStart.setEnabled(false);
                            }
                            //System.exit(0);
                        }
                    }
                }
            }
            else
            {
                String sqlShift = "select date(max(dtePOSDate)),intShiftCode"
                        + " from tbldayendprocess "
                        + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N'"
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
                rsShiftNo.close();

                sql = "delete from tblitemrtemp where strTableNo='null' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                clsGlobalVarClass.gDayEndReportForm = "DayEndReport";
                if (objUtility.funCheckPendingBills(clsGlobalVarClass.gPOSCode))
                {
                    JOptionPane.showMessageDialog(this, "Please settle pending bills");
                }
                else if (objUtility.funCheckTableBusy(clsGlobalVarClass.gPOSCode))
                {
                    JOptionPane.showMessageDialog(this, "Sorry Tables are Busy Now");
                }
                else
                {
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
                                    + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
                            clsGlobalVarClass.dbMysql.execute(sql);

                            sql = "update tbldayendprocess set strShiftEnd='Y' "
                                    + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N'";
                            clsGlobalVarClass.dbMysql.execute(sql);
                            if (clsGlobalVarClass.gGenrateMI)
                            {
                                frmGenrateMallInterfaceText objGenrateMallInterfaceText = new frmGenrateMallInterfaceText();
                                objGenrateMallInterfaceText.funWriteToFile(posDate, posDate, "Current", "Y");
                            }
                            objUtility.funGetNextShiftNo(clsGlobalVarClass.gPOSCode, shiftNo);
                            btnShiftEnd.setEnabled(false);

                            final String filePath = System.getProperty("user.dir") + "/Temp/Temp_DayEndReport.txt";
			  
			   
			    // mail dayend reports on blank day end 
			    option = JOptionPane.showConfirmDialog(this, "Do You Want To Email Reports?");
			    if (option == 0)
			    {
				funSendDayEndReports(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo);
			    }
			    else
			    {
				//delete old reports
				frmShiftEndProcess objShiftEnd=new frmShiftEndProcess();
				objShiftEnd.funCreateReportFolder();
			    }
			    
			    
                           

                            new Thread()
                            {

                                @Override
                                public void run()
                                {

                                    try
                                    {
                                         //send mail sales amount after shift end
					 new clsSendMail().funSendMail(totalSales, totalDiscount, totalPayments, filePath,clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo,clsGlobalVarClass.gClientCode);

                                        if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
                                        {
                                            funSendDBBackupAndErrorLogFileToSanguineAuditiing(backupFilePathMail);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                }

                            }.start();

			     JOptionPane.showMessageDialog(null, "Day End Done Successfully!!!");
			     
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
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnShiftEndMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Day End");
    }//GEN-LAST:event_formWindowClosing

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

    private void tblDayEndReportsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblDayEndReportsMouseClicked
    {//GEN-HEADEREND:event_tblDayEndReportsMouseClicked
        funSetReportCount();
    }//GEN-LAST:event_tblDayEndReportsMouseClicked

    private void btnDayEndReports1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDayEndReports1MouseClicked
    {//GEN-HEADEREND:event_btnDayEndReports1MouseClicked
        funSendEmailClicked(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName, posDate, clsGlobalVarClass.gShiftNo);
    }//GEN-LAST:event_btnDayEndReports1MouseClicked


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
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHOSign3;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName3;
    private javax.swing.JLabel lblNoOfReports;
    private javax.swing.JLabel lblPOSName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPosName2;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblProductName3;
    private javax.swing.JLabel lblShiftEnd;
    private javax.swing.JLabel lblShiftNo;
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
    private javax.swing.JTable tblDayEndReports;
    // End of variables declaration//GEN-END:variables

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
    
    private void funSendDayEndReports(String posCode, String posName, String posDate, int shiftNo)
    {
	boolean isSendDefault = false;

	try
	{
	    frmShiftEndProcess objShiftEnd=new frmShiftEndProcess();
	    objShiftEnd.funCreateReportFolder();

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
	    header.setLayout(new FlowLayout(FlowLayout.RIGHT));

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
		//funSendEmailClicked(posCode, posName, posDate, shiftNo);
	    }
	    else
	    {
		//dialogDayEndReports.setVisible(true);
	    }
	    dialogDayEndReports.setVisible(true);
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
	    frmShiftEndProcess objShiftEnd=new frmShiftEndProcess();
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

		    objShiftEnd.funGenerateReport(tblDayEndReports.getValueAt(r, 0).toString(), posCode, posName, posDate, shift);
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

}
