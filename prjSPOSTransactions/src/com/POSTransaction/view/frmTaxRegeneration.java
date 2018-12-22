package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class frmTaxRegeneration extends javax.swing.JFrame
{

    private clsUtility objUtility;

    public frmTaxRegeneration()
    {
        initComponents();
        try
        {
            objUtility = new clsUtility();
            java.util.Date posDate = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(posDate);
            dteToDate.setDate(posDate);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            cmbPosCode.addItem("All");
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            String sql_POS = "select strPosName,strPosCode from tblposmaster";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql_POS);
            while (rsPOS.next())
            {
                cmbPosCode.addItem(rsPOS.getString(1) + "                                         !" + rsPOS.getString(2));
            }
            rsPOS.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int funRegenerateTax(String posCode, String fromDate, String toDate)
    {
        try
        {
            String sql = "";

            String sql_Bills = "select a.strBillNo,ifnull(b.strAreaCode,''),a.strOperationType,ifnull(d.strSettelmentType,'Cash') "
                    + " ,a.dblSubTotal,a.dblDiscountPer,a.strSettelmentMode,a.dblGrandTotal,c.dblSettlementAmt,date(a.dteBillDate)"
                    + ",d.strSettelmentCode,a.dteBillDate,a.dblDiscountAmt "
                    + " from tblqbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
                    + " left outer join tblqbillsettlementdtl c on a.strBillNo=c.strBillNo and DATE(a.dteBillDate)=DATE(c.dteBillDate) "
                    + " left outer join tblsettelmenthd d on c.strSettlementCode=d.strSettelmentCode "
                    + " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
                    + " and a.strPOSCode='" + posCode + "' ";
                   // + " and a.strBillNo='FP0111040' ";
            ResultSet rsBills = clsGlobalVarClass.dbMysql.executeResultSet(sql_Bills);
            while (rsBills.next())
            {
                String billNo = rsBills.getString(1);
                String area = rsBills.getString(2);
                if (area.trim().length() == 0)
                {
                    area = clsGlobalVarClass.gDineInAreaForDirectBiller;
                }
                String operationType = rsBills.getString(3);
                double subTotal = rsBills.getDouble(5);
                double discPer = rsBills.getDouble(6);
                String filterDate = rsBills.getString(10);
                String settlementCode = rsBills.getString(11);
                String billDate = rsBills.getString(12);
		
		double dblBillDiscAmt  = rsBills.getDouble(13);

//                String sqlDiscAmt = "select sum(dblDiscAmt) from tblqbilldiscdtl   "
//                        + " where strBillNo='" + billNo + "' "
//                        + " and date(dteBillDate)='" + filterDate + "' ";
//                ResultSet rsBillDiscAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlDiscAmt);
//                
//                if (rsBillDiscAmt.next())
//                {
//                    dblBillDiscAmt = rsBillDiscAmt.getDouble(1);
//                }
//		rsBillDiscAmt.close();

                //ArrayList<ArrayList<Object>> list=funCalculateTax(billNo,area,rsBills.getString(3),rsBills.getString(4),rsBills.getString(5),rsBills.getString(6),posCode);
                //System.out.println(list);
                List<clsItemDtlForTax> arrListItemDtlForTax = new ArrayList<clsItemDtlForTax>();
                sql = "select strItemCode,strItemName,dblAmount,dblDiscountAmt,dblDiscountPer,strKOTNo "
                        + "from tblqbilldtl "
                        + "where strBillNo='" + billNo + "' "
                        + "and date(dteBillDate)='" + filterDate + "' ";
                ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsBillDtl.next())
                {
                    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                    objItemDtlForTax.setItemCode(rsBillDtl.getString(1));
                    objItemDtlForTax.setItemName(rsBillDtl.getString(2));
                    objItemDtlForTax.setAmount(rsBillDtl.getDouble(3));
                    objItemDtlForTax.setDiscAmt(rsBillDtl.getDouble(4));
                    objItemDtlForTax.setDiscPer(rsBillDtl.getDouble(5));

                    arrListItemDtlForTax.add(objItemDtlForTax);
                }
                rsBillDtl.close();

                //modifiers
                sql = "select strItemCode,strModifierName,dblAmount,dblDiscAmt,dblDiscPer "
                        + "from tblqbillmodifierdtl "
                        + "where strBillNo='" + billNo + "' "
                        + "and date(dteBillDate)='" + filterDate + "' ";
                ResultSet rsBillModiDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsBillModiDtl.next())
                {
                    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                    objItemDtlForTax.setItemCode(rsBillModiDtl.getString(1));
                    objItemDtlForTax.setItemName(rsBillModiDtl.getString(2));
                    objItemDtlForTax.setAmount(rsBillModiDtl.getDouble(3));
                    objItemDtlForTax.setDiscAmt(rsBillModiDtl.getDouble(4));
                    objItemDtlForTax.setDiscPer(rsBillModiDtl.getDouble(5));

                    arrListItemDtlForTax.add(objItemDtlForTax);
                }
                rsBillModiDtl.close();

                String settlementType = rsBills.getString(4);
                if (operationType.equalsIgnoreCase("DirectBiller"))
                {
                    operationType = "DineIn";
                }
                List<clsTaxCalculationDtls> arrListTaxDtl = new clsUtility().funCalculateTax(arrListItemDtlForTax, posCode, filterDate, area, operationType, subTotal, dblBillDiscAmt, "Tax Regen", settlementCode, "Sales");
                System.out.println(arrListTaxDtl.size() + "\t" + operationType + "\t" + area);
                String sql_BillTaxDtl = "insert into tblqbilltaxdtl "
                        + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,strDataPostFlag,dteBillDate ) "
                        + " values ";
                String billTaxDtlData = "";
                boolean flgData = false;

                for (clsTaxCalculationDtls objTaxCalDtl : arrListTaxDtl)
                {
                    billTaxDtlData += "('" + billNo + "','" + objTaxCalDtl.getTaxCode() + "','" + objTaxCalDtl.getTaxableAmount() + "'"
                            + ",'" + objTaxCalDtl.getTaxAmount() + "','" + clsGlobalVarClass.gClientCode + "','N','" + billDate + "'),";
                    flgData = true;
                }

                if (flgData)
                {
                    System.out.println(billNo);
                    if (billNo.equals("P0321452"))
                    {
                        System.out.println(billNo);
                    }

                    sql_BillTaxDtl += " " + billTaxDtlData;
                    sql_BillTaxDtl = sql_BillTaxDtl.substring(0, (sql_BillTaxDtl.length() - 1));
                    //System.out.println(sql_BillTaxDtl);

                    sql = "delete from tblqbilltaxdtl "
                            + "where strBillNo='" + billNo + "' "
                            + "and date(dteBillDate)='" + filterDate + "' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    clsGlobalVarClass.dbMysql.execute(sql_BillTaxDtl);

                    try
                    {
                        if (rsBills.getString(4).equals("Complementary"))
                        {
                            String sqlUpdate = "update tblqbilltaxdtl set dblTaxableAmount=0.00,dblTaxAmount=0.00 "
                                    + "where strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            clsGlobalVarClass.dbMysql.execute(sqlUpdate);

                            sqlUpdate = "update tblqbillhd set dblTaxAmt=0.00,dblSubTotal=0.00"
                                    + ",dblDiscountAmt=0.00,dblDiscountPer=0.00,dblGrandTotal=0.00 "
                                    + "where strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            clsGlobalVarClass.dbMysql.execute(sqlUpdate);

                            sqlUpdate = "update tblqbilldtl set dblAmount=0.00,dblDiscountAmt=0.00,dblDiscountPer=0.00 "
                                    + "where strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            clsGlobalVarClass.dbMysql.execute(sqlUpdate);

                            sqlUpdate = "update tblqbillmodifierdtl set dblAmount=0.00 where strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			    
			    sqlUpdate = "update tblqbilldiscdtl set dblDiscAmt=0.00,dblDiscPer=0.00,dblDiscOnAmt=0.00 where strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            clsGlobalVarClass.dbMysql.execute(sqlUpdate);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        String sql_UpdateBillHdSubTotal = "update tblqbillhd "
                                + " set dblSubTotal="
                                + "(select sum(c.dblAmount) "
                                + "from  "
                                + "(select a.strItemCode,a.strItemName,a.strBillNo,a.dblAmount,a.dblDiscountAmt,a.dblDiscountPer "
                                + "from tblqbilldtl a where a.strBillNo='" + billNo + "' and date(a.dteBillDate)='" + filterDate + "'   "
                                + "union all "
                                + "select b.strItemCode,b.strModifierName,b.strBillNo,b.dblAmount,b.dblDiscAmt,b.dblDiscPer  "
                                + "from tblqbillmodifierdtl b where b.strBillNo='" + billNo + "' and date(b.dteBillDate)='" + filterDate + "'  ) "
                                + "c  "
                                + " group by c.strBillNo) "
                                + " where strBillNo='" + billNo + "' "
                                + " and date(dteBillDate)='" + filterDate + "' ";
                        clsGlobalVarClass.dbMysql.execute(sql_UpdateBillHdSubTotal);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        String sql_UpdateBillHdTaxAmt = "update tblqbillhd "
                                + " set dblTaxAmt=(select ifnull(sum(a.dblTaxAmount),0) from tblqbilltaxdtl a where strBillNo='" + billNo + "'  and date(dteBillDate)='" + filterDate + "'  group by strBillNo) "
                                + " where strBillNo='" + billNo + "'"
                                + "  and date(dteBillDate)='" + filterDate + "'  ";
                        clsGlobalVarClass.dbMysql.execute(sql_UpdateBillHdTaxAmt);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        sql = "update tblqbillhd set dblDiscountAmt="
                                + "(select sum(c.dblDiscountAmt) "
                                + "from  "
                                + "(select a.strItemCode,a.strItemName,a.strBillNo,a.dblAmount,a.dblDiscountAmt,a.dblDiscountPer "
                                + "from tblqbilldtl a where a.strBillNo='" + billNo + "' and date(a.dteBillDate)='" + filterDate + "'   "
                                + "union all "
                                + "select b.strItemCode,b.strModifierName,b.strBillNo,b.dblAmount,b.dblDiscAmt,b.dblDiscPer  "
                                + "from tblqbillmodifierdtl b where b.strBillNo='" + billNo + "' and date(b.dteBillDate)='" + filterDate + "'  ) "
                                + "c  "
                                + " group by c.strBillNo) "
                                + " where strBillNo='" + billNo + "' "
                                + "  and date(dteBillDate)='" + filterDate + "'  ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        if (rsBills.getDouble(5) > 0)
                        {
                            sql = "update tblqbillhd "
                                    + " set dblDiscountPer=(dblDiscountAmt/dblSubTotal)*100 "
                                    + " where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
                                    + " and strPOSCode='" + posCode + "' "
                                    + " and strBillNo='" + billNo + "' "
                                    + " and date(dteBillDate)='" + filterDate + "'  ";
                            System.out.println(clsGlobalVarClass.dbMysql.execute(sql));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    String sql_UpdateBillHdGrandTotal = "update tblqbillhd set dblGrandTotal=round((dblSubTotal-dblDiscountAmt)+dblTaxAmt,0) "
                            + " where strBillNo='" + billNo + "' "
                            + "  and date(dteBillDate)='" + filterDate + "'  ";
                    clsGlobalVarClass.dbMysql.execute(sql_UpdateBillHdGrandTotal);

                    /**
                     * delete double entries of same settlement with No
                     * multisettle
                     */
                    sql = "select count(b.strSettlementCode) "
                            + "from tblqbillhd a,tblqbillsettlementdtl b "
                            + "where a.strBillNo=b.strBillNo "
                            + "and date(a.dteBillDate)=date(b.dteBillDate) "
                            + "and a.strBillNo='" + billNo + "' "
                            + "and date(a.dteBillDate)='" + filterDate + "' "
                            + "and a.strSettelmentMode<> 'MultiSettle' "
                            + "group by a.strBillNo,b.strSettlementCode,a.strClientCode "
                            + "having count(b.strSettlementCode)>1 ";
                    ResultSet rsDoubleEntriesOfSettlemts = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsDoubleEntriesOfSettlemts.next())
                    {
                        clsGlobalVarClass.dbMysql.execute("DELETE FROM tblqbillsettlementdtl WHERE strBillNo='" + billNo + "' AND date(dteBillDate)='" + filterDate + "' LIMIT 1");
                    }
                    rsDoubleEntriesOfSettlemts.close();

                    String sql_BillSettlementAmt = "update tblqbillsettlementdtl c "
                            + " join (select a.dblGrandTotal as GrandTotal,a.strBillNo as BillNo,a.dteBillDate "
                            + " from tblqbillhd a,tblqbillsettlementdtl b "
                            + " where a.strbillno=b.strbillNo  and date(a.dteBillDate)=date(b.dteBillDate) and a.strBillNo='" + billNo + "' and date(a.dteBillDate)='" + filterDate + "' "
                            + " and (b.dblSettlementAmt-a.dblGrandTotal) not between -0.01 and 0.01 "
                            + " and a.strSettelmentMode<> 'MultiSettle') d "
                            + " on c.strbillno=d.BillNo and date(c.dteBillDate)=date(d.dteBillDate) "
                            + " set c.dblSettlementAmt = d.GrandTotal "
                            + "where c.strBillNo='" + billNo + "' "
                            + "and date(c.dteBillDate)='" + filterDate + "'";
                    clsGlobalVarClass.dbMysql.execute(sql_BillSettlementAmt);

                    String sql_UpdateBillSettlement = "update tblqbillsettlementdtl set dblPaidAmt=round(dblPaidAmt,0),dblActualAmt=round(dblActualAmt,0) "
                            + " where strBillNo='" + billNo + "' "
                            + "  and date(dteBillDate)='" + filterDate + "'  ";
                    clsGlobalVarClass.dbMysql.execute(sql_UpdateBillHdGrandTotal);

                    String settlementMode = rsBills.getString(7);
                    double grandTotal = rsBills.getDouble(8);

                    /**
                     * to update billhd grandTotal to billSettlementTotal
                     */
                    sql = "select dblGrandTotal from tblqbillhd "
                            + " where strBillNo='" + billNo + "' "
                            + " and date(dteBillDate)='" + filterDate + "' ";
                    ResultSet rsTempBillHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsTempBillHd.next())
                    {
                        grandTotal = rsTempBillHd.getDouble(1);
                    }
                    rsTempBillHd.close();

                    sql = "select a.strSettlementCode,sum(a.dblSettlementAmt) "
                            + "from tblqbillsettlementdtl a "
                            + "where a.strBillNo='" + billNo + "' "
                            + "and date(a.dteBillDate)='" + filterDate + "' ";
                    ResultSet rsBillHdGT = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsBillHdGT.next())
                    {
                        double billSettleAmt = rsBillHdGT.getDouble(2);//total settlement amount
                        double billHDnBillSettleAmtDiff = grandTotal - billSettleAmt;

                        if (billHDnBillSettleAmtDiff > 0)
                        {
                            sql = "update tblqbillsettlementdtl  "
                                    + "SET dblSettlementAmt = dblSettlementAmt+" + billHDnBillSettleAmtDiff + " "
                                    + "where strBillNo='" + billNo + "'  "
                                    + "AND DATE(dteBillDate)='" + filterDate + "'  "
                                    + "and strSettlementCode='" + settlementCode + "' ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }
                    }
                    rsBillHdGT.close();

                    if (settlementMode.equals("MultiSettle"))
                    {
                        if (settlementType.equals("Cash"))
                        {
                            sql = "select dblGrandTotal from tblqbillhd "
                                    + " where strBillNo='" + billNo + "' "
                                    + " and date(dteBillDate)='" + filterDate + "' ";
                            rsTempBillHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            if (rsTempBillHd.next())
                            {
                                grandTotal = rsTempBillHd.getDouble(1);
                            }
                            rsTempBillHd.close();

                            String settleCode = "";
                            double settlementAmt = 0;
                            sql = "select a.dblSettlementAmt,a.strSettlementCode "
                                    + "from tblqbillsettlementdtl a,tblsettelmenthd b "
                                    + "where a.strSettlementCode=b.strSettelmentCode "
                                    + "and date(a.dteBillDate)='" + filterDate + "'  "
                                    + "and b.strSettelmentType!='Cash' "
                                    + "and a.strBillNo='" + billNo + "' ";
                            ResultSet rsSettle = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            while (rsSettle.next())
                            {
                                settlementAmt += rsSettle.getDouble(1);
                                settleCode = "'" + rsSettle.getString(2) + "',";
                            }
                            rsSettle.close();

                            if (settleCode.contains(","))
                            {
                                settleCode = settleCode.substring(0, settleCode.length() - 1);
                            }
                            if (settleCode.length() > 0)
                            {
                                double settleAmtForCash = grandTotal - settlementAmt;
                                System.out.println(settleAmtForCash);
                                System.out.println(grandTotal);
                                System.out.println(settlementAmt);
                                sql = "update tblqbillsettlementdtl "
                                        + "set dblSettlementAmt=" + settleAmtForCash
                                        + " where strSettlementCode not in(" + settleCode + ") "
                                        + " and strBillNo='" + billNo + "' "
                                        + " and date(dteBillDate)='" + filterDate + "' ";
                                System.out.println(sql);
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }

                        }

                        if (settlementType.equals("Complementary"))
                        {
                            sql = "select a.dblSettlementAmt,a.strSettlementCode "
                                    + "from tblqbillsettlementdtl a,tblsettelmenthd b "
                                    + "where a.strSettlementCode=b.strSettelmentCode "
                                    + "and b.strSettelmentType!='Complementary' "
                                    + "and a.strBillNo='" + billNo + "' "
                                    + "and date(dteBillDate)='" + filterDate + "' ";
                            ResultSet rsSettle = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            while (rsSettle.next())
                            {
                                sql = "delete from tblqbillsettlementdtl "
                                        + "where strBillNo='" + billNo + "' "
                                        + "and strSettlementCode='" + rsSettle.getString(2) + "' "
                                        + "and date(dteBillDate)='" + filterDate + "' ";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }
                            rsSettle.close();
                        }
                    }

                    String sqlUpdateBillSeriesGT = "update tblbillseriesbilldtl a\n"
                            + "join tblqbillhd b\n"
                            + "on  a.strHdBillNo=b.strBillNo\n"
                            + "and date(a.dteBillDate)=date(b.dteBillDate)\n"
                            + "set a.dblGrandTotal=b.dblGrandTotal\n"
                            + "where a.strHdBillNo=b.strBillNo\n"
                            + "and date(a.dteBillDate)=date(b.dteBillDate)\n"
                            + "and a.strHdBillNo='" + billNo + "'\n"
                            + "and date(a.dteBillDate)='" + filterDate + "' ";
                    clsGlobalVarClass.dbMysql.execute(sqlUpdateBillSeriesGT);

                    /*
                     * String multiSettleBill="update tblqbillhd a,
                     * tblqbillsettlementdtl b " + "set a.dblgrandtotal =
                     * sum(b.dblSettlementAmt) " + "where
                     * a.strBillNo=b.strBillNo and
                     * a.strSettelmentMode='MultiSettle' " + "group by
                     * b.strBillNo "; System.out.println(multiSettleBill);
                     * clsGlobalVarClass.dbMysql.execute(multiSettleBill);
                     */
                }

                sql = "update tblqbillhd set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbilldtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbillsettlementdtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbillmodifierdtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbilltaxdtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbillcomplementrydtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbilldiscdtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblqbillpromotiondtl set strDataPostFlag='N' where strBillNo='" + billNo + "' and date(dteBillDate)='" + filterDate + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                objUtility.funUpdateBillDtlWithTaxValues(billNo, "QFile", filterDate);
            }

            sql = "update tblqbillhd set dblsubtotal=0,dblTaxAmt=0,dblGrandTotal=0,strDataPostFlag='N' "
                    + " where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
                    + " and strPOSCode='" + posCode + "' "
                    + " and strBillNo Not In (select strBillNo from tblqbilldtl) ";
            System.out.println(sql);
            System.out.println(clsGlobalVarClass.dbMysql.execute(sql));

            /*
             * sql="update tblqbillhd " + " set
             * dblDiscountAmt=(dblSubTotal*dblDiscountPer)/100 " + " where
             * date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' " + "
             * and strPOSCode='"+posCode+"' ";
             * System.out.println(clsGlobalVarClass.dbMysql.execute(sql));
             */
            sql = "update tblqbilldtl a join "
                    + " ( select b.dblDiscountAmt as DisAmt,b.dblDiscountPer as DiscPer,b.strBillNo as BillNo ,"
                    + " b.strPOSCode as POSCode, b.dteBillDate as BillDate from tblqbillhd b "
                    + " where date(b.dteBillDate) between '" + fromDate + "' and '" + toDate + "'  "
                    + ") c "
                    + " on a.strbillno=c.BillNo  and date(a.dteBillDate)=date(c.dteBillDate) "
                    + " set a.dblDiscountAmt=(a.dblAmount*c.DiscPer)/100,strDataPostFlag='N' "
                    + " where date(c.BillDate) between '" + fromDate + "' and '" + toDate + "' "
                    + " and c.POSCode='" + posCode + "' ";
            //System.out.println(sql);
            //System.out.println(clsGlobalVarClass.dbMysql.execute(sql));

            objUtility.funCalculateDayEndCashForQFile(fromDate, 1);
            objUtility.funUpdateDayEndFieldsForQFile(fromDate, 1, "Y");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 1;
    }

    private ArrayList<ArrayList<Object>> funCalculateTax(String billNo, String areaCode, String opTypeFromDB, String settleMode, String subTotal, String discPer, String posCode)
    {
        ArrayList<ArrayList<Object>> list = null;
        try
        {
            list = funCheckDateRangeForTax(billNo, areaCode, opTypeFromDB, settleMode, subTotal, discPer, posCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<ArrayList<Object>> funCheckDateRangeForTax(String billNo, String areaCode, String opTypeFromDB, String settleMode, String subTotal, String discPer, String posCode) throws Exception
    {
        String taxCode = "", taxName = "", taxOnSP = "", taxType = "", taxOnGD = "", taxCal = "", taxIndicator = "";
        String itemType = "", opType = "", area = "", taxOnTax = "No", taxOnTaxCode = "";
        double taxPercent = 0.00, taxAmount = 0.00, taxableAmount = 0.00, taxCalAmt = 0.00;
        ArrayList<Object> listTax = new ArrayList<Object>();
        ArrayList<ArrayList<Object>> arrListTaxCal = new ArrayList<ArrayList<Object>>();
        clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp;");// Empty Tax Temp Table

        String sql_ChkTaxDate = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent"
                + ",a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType"
                + ",a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
                + "from tbltaxhd a,tbltaxposdtl b "
                + "where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + posCode + "' "
                + "and a.strTaxOnSP='Sales' "
                + "order by a.strTaxOnTax,a.strTaxDesc";

        ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_ChkTaxDate);
        while (rsTax.next())
        {
            taxCode = rsTax.getString(1);
            taxName = rsTax.getString(2);
            taxOnSP = rsTax.getString(3);
            taxType = rsTax.getString(4);
            taxOnGD = rsTax.getString(7);
            taxCal = rsTax.getString(8);
            taxIndicator = rsTax.getString(9);
            taxOnTax = rsTax.getString(13);
            taxOnTaxCode = rsTax.getString(14);
            taxPercent = Double.parseDouble(rsTax.getString(5));
            taxAmount = Double.parseDouble(rsTax.getString(6));
            taxableAmount = 0.00;
            taxCalAmt = 0.00;

            String sql_TaxOn = "select strAreaCode,strOperationType,strItemType "
                    + "from tbltaxhd where strTaxCode='" + taxCode + "'";
            ResultSet rsTaxOn = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxOn);
            if (rsTaxOn.next())
            {
                area = rsTaxOn.getString(1);
                opType = rsTaxOn.getString(2);
                itemType = rsTaxOn.getString(3);
            }
            if (funCheckAreaCode(taxCode, area, areaCode))
            {
                if (funCheckOperationType(taxCode, opType, opTypeFromDB))
                {
                    if (funFindSettlementForTax(taxCode, settleMode))
                    {
                        listTax = new ArrayList<Object>();
                        if (taxIndicator.trim().length() > 0) // For Indicator Based Tax
                        {
                            double taxIndicatorTotal = funGetTaxIndicatorTotal(taxIndicator, billNo);
                            if (taxIndicatorTotal > 0)
                            {
                                if (taxOnGD.equals("Gross"))
                                {
                                    taxableAmount = taxIndicatorTotal;
                                }
                                else
                                {
                                    taxableAmount = (taxIndicatorTotal) - (taxIndicatorTotal * (taxPercent / 100));
                                }
                                if (taxCal.equals("Forward")) // Forward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * (taxPercent / 100);
                                }
                                else // Backward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * 100 / (100 + taxPercent);
                                    taxCalAmt = taxableAmount - taxCalAmt;
                                }
                                listTax.add(taxCode);
                                listTax.add(taxName);
                                listTax.add(taxableAmount);
                                listTax.add(taxCalAmt);
                                listTax.add(taxCal);
                                arrListTaxCal.add(listTax);
                                //funInsertTaxTemp(taxCode,taxName,taxableAmount,taxCalAmt,taxCal);
                            }
                        }
                        else // For Blank Indicator
                        {
                            if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
                            {
                                taxableAmount = funGetTaxableAmountForTaxOnTax(taxOnTaxCode, arrListTaxCal);
                                if (taxCal.equals("Forward")) // Forward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * (taxPercent / 100);
                                }
                                else // Backward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * 100 / (100 + taxPercent);
                                }
                                listTax.add(taxCode);
                                listTax.add(taxName);
                                listTax.add(taxableAmount);
                                listTax.add(taxCalAmt);
                                listTax.add(taxCal);
                                arrListTaxCal.add(listTax);
                            }
                            else
                            {
                                if (taxOnGD.equals("Gross"))
                                {
                                    taxableAmount = Double.parseDouble(subTotal);
                                }
                                else
                                {
                                    taxableAmount = (Double.parseDouble(subTotal)) - (Double.parseDouble(subTotal) * (Double.parseDouble(discPer) / 100));
                                }

                                if (taxCal.equals("Forward")) // Forward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * (taxPercent / 100);
                                }
                                else // Backward Tax Calculation
                                {
                                    taxCalAmt = taxableAmount * 100 / (100 + taxPercent);
                                }
                                listTax.add(taxCode);
                                listTax.add(taxName);
                                listTax.add(taxableAmount);
                                listTax.add(taxCalAmt);
                                listTax.add(taxCal);
                                arrListTaxCal.add(listTax);
                                //funInsertTaxTemp(taxCode,taxName,taxableAmount,taxCalAmt,taxCal);
                            }
                        }
                    }
                }
            }
        }

        return arrListTaxCal;
    }

    private boolean funCheckAreaCode(String taxCode, String area, String areaCode)
    {
        boolean flgTaxOn = false;
        String[] spAreaCode = area.split(",");
        for (int cnt = 0; cnt < spAreaCode.length; cnt++)
        {
            if (spAreaCode[cnt].equals(areaCode))
            {
                flgTaxOn = true;
                break;
            }
        }

        return flgTaxOn;
    }

    private boolean funCheckOperationType(String taxCode, String opType, String operationTypeForTax)
    {
        boolean flgTaxOn = false;
        String[] spOpType = opType.split(",");
        for (int cnt = 0; cnt < spOpType.length; cnt++)
        {
            if (spOpType[cnt].equals("HomeDelivery") && operationTypeForTax.equalsIgnoreCase("HomeDelivery"))
            {
                flgTaxOn = true;
                break;
            }
            if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("Dine In"))
            {
                flgTaxOn = true;
                break;
            }
            if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("Direct Biller"))
            {
                flgTaxOn = true;
                break;
            }
            if (spOpType[cnt].equals("TakeAway") && operationTypeForTax.equalsIgnoreCase("TakeAway"))
            {
                flgTaxOn = true;
                break;
            }
        }
        return flgTaxOn;
    }

    private double funGetTaxIndicatorTotal(String indicator, String billNo) throws Exception
    {
        String sql_Query = "";
        double indicatorAmount = 0.00;

        sql_Query = "select a.strItemCode,b.strTaxIndicator,sum(a.dblAmount) "
                + "from tblqbilldtl a,tblitemmaster b "
                + "where a.strItemCode=b.strItemCode and a.strBillNo='" + billNo + "' and b.strTaxIndicator='" + indicator + "' "
                + " group by b.strTaxIndicator";
        //System.out.println(sql_Query);
        ResultSet rsTaxIndicator = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
        if (rsTaxIndicator.next())
        {
            indicatorAmount += Double.parseDouble(rsTaxIndicator.getString(3));
        }
        rsTaxIndicator.close();
        return indicatorAmount;
    }

    private double funGetItemTypeTotal(String itemType, String billNo) throws Exception
    {
        String sql_Query = "";
        double itemTypeAmount = 0.00;
        sql_Query = "select a.strItemCode,b.strTaxIndicator,sum(a.dblAmount) "
                + " from tblqbilldtl a,tblitemmaster b "
                + " where left(a.strItemCode,7)=b.strItemCode and b.strItemType='" + itemType + "' "
                + " and a.strBillNo='" + billNo + "' "
                + " group by b.strItemType";
        //System.out.println(sql_Query);
        ResultSet raItemType = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
        if (raItemType.next())
        {
            itemTypeAmount += Double.parseDouble(raItemType.getString(3));
        }
        raItemType.close();
        return itemTypeAmount;
    }

    private boolean funFindSettlementForTax(String taxCode, String settlementMode) throws Exception
    {
        boolean flgTaxSettlement = false;
        String sqlSettlementTax = "select a.strSettlementCode,a.strSettlementName "
                + " from tblsettlementtax a,tblsettelmenthd b "
                + " where a.strSettlementCode=b.strSettelmentCode and a.strTaxCode='" + taxCode + "' "
                + " and a.strApplicable='true' and b.strSettelmentType='" + settlementMode + "';";
        ResultSet rsTaxSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sqlSettlementTax);
        if (rsTaxSettlement.next())
        {
            flgTaxSettlement = true;
        }
        rsTaxSettlement.close();
        return flgTaxSettlement;
    }

    private double funGetTaxableAmountForTaxOnTax(String taxOnTaxCode, ArrayList<ArrayList<Object>> arrListTaxCal) throws Exception
    {
        double taxableAmt = 0;
        String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
        for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
        {
            for (int t = 0; t < spTaxOnTaxCode.length; t++)
            {
                ArrayList arrListTax = arrListTaxCal.get(cnt);
                if (arrListTax.get(0).toString().equals(spTaxOnTaxCode[t]))
                {
                    taxableAmt += Double.parseDouble(arrListTax.get(2).toString()) + Double.parseDouble(arrListTax.get(3).toString());
                }
            }
        }
        return taxableAmt;
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
        lblUserCode = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
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
        lblFormName = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        cmbPosCode = new javax.swing.JComboBox();
        lblPOSName = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnGenerateTax = new javax.swing.JButton();
        disReportName = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        lblFromDate = new javax.swing.JLabel();

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
        lblformName.setText("- Tax Regeneration");
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

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelHeader.add(lblUserCode);
        panelHeader.add(filler6);

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

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.setForeground(new java.awt.Color(255, 255, 255));

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS Name       :");

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBack.setText("CLOSE");
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBack.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBackMouseClicked(evt);
            }
        });
        btnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBackActionPerformed(evt);
            }
        });

        btnGenerateTax.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerateTax.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGenerateTax.setText("<html>Re-Generate<br>Taxes</html>");
        btnGenerateTax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGenerateTax.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGenerateTax.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnGenerateTaxMouseClicked(evt);
            }
        });
        btnGenerateTax.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGenerateTaxActionPerformed(evt);
            }
        });

        disReportName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        disReportName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disReportName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date          :");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date       :");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(109, 109, 109)
                                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(87, 87, 87)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(110, 110, 110)
                                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(disReportName, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(btnGenerateTax, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 16, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addComponent(disReportName, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFormName)
                .addGap(51, 51, 51)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(234, 234, 234)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnGenerateTax, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(137, 137, 137))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Tax Regeneration");
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnGenerateTaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerateTaxMouseClicked

        String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + dteFromDate.getDate().getDate();
        String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + dteToDate.getDate().getDate();

        if (cmbPosCode.getSelectedItem().toString().equalsIgnoreCase("All"))
        {
            for (int i = 1; i < cmbPosCode.getItemCount(); i++)
            {
                funRegenerateTax(cmbPosCode.getItemAt(i).toString().split("!")[1], fromDate, toDate);
            }
        }
        else
        {
            String posCode = cmbPosCode.getSelectedItem().toString().split("!")[1];
            funRegenerateTax(posCode, fromDate, toDate);
        }
        if (clsGlobalVarClass.gConnectionActive.equals("Y"))
        {
            try
            {
                clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                new clsUtility().funWriteErrorLog(e);
            }
        }

        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Tax Regeneration");
    }//GEN-LAST:event_btnGenerateTaxMouseClicked

    private void btnGenerateTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateTaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenerateTaxActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Tax Regeneration");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Tax Regeneration");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnGenerateTax;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JLabel disReportName;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    // End of variables declaration//GEN-END:variables
}
