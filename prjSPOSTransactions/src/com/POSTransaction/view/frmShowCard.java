package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSPrinting.Utility.clsPrintingUtility;
import com.POSTransaction.controller.nfc.ReaderThread;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class frmShowCard extends javax.swing.JFrame 
{
    private String  fromDate, toDate;
    DefaultTableModel dm, totalDm;
    private String exportFormName, totdayDate, ExportReportPath,cardNo;
    private clsUtility objUtility;
    private List<List<String>> arrListShowCardDtl;
    private List<String> arrListRecharge;
    private List<String> arrListRedeem;
    private List<String> arrListRefund;
    private List<String> arrListOpenKOT;
    private List<String> arrListUnsettleBill;
    private List<String> arrListTransferAmt;
    private double totalAmt;
    
    
    public frmShowCard() 
    {
        initComponents();        
        funSetLookAndFeel();
        try 
        {
            objUtility=new clsUtility();
            ExportReportPath = clsPosConfigFile.exportReportPath;
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            exportFormName = "Modifed Bill";
            arrListShowCardDtl=new ArrayList<List<String>>();
            totalAmt=0;
            cardNo="";
            funFillComboBox();
            java.util.Date objDate = new java.util.Date();
            String dte = objDate.getDate() + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getYear() + 1900);
            lblDate.setText(dte);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            totdayDate = dte;
            dteFromDate.setDate(date);
            dteToDate.setDate(date);
            dm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            dm.addColumn("Bill No");
            dm.addColumn("Bill Date");
            dm.addColumn("ModifiedDate");
            dm.addColumn("Entry Time");
            dm.addColumn("Modify Time");
            dm.addColumn("Bill Amt");
            dm.addColumn("New Amt");
            dm.addColumn("User Created");
            dm.addColumn("User Edited");

            DefaultTableModel dm1 = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            dm1.getDataVector().removeAllElements();
            dm1.addColumn("");
            dm1.addColumn("");
            dm1.addColumn("");
            dm1.addColumn("");
            dm1.addColumn("");
            tblTotal.updateUI();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * this function is used Filling POS Code,Reason code User Code ComboBoxs
     */
    private void funFillComboBox() throws Exception
    {
        ResultSet objResultSet=null;
        cmbPosCode.addItem("All");
        objResultSet = clsGlobalVarClass.dbMysql.executeResultSet("select strPosName,strPosCode from tblposmaster");
        while (objResultSet.next()) {
            cmbPosCode.addItem(objResultSet.getString(1) + "                                               " + objResultSet.getString(2));
        }
        objResultSet.close();
    }

  /**
     * this Function is used for get Selected POS Code
     */
    public String getSelectedPosCode() {
        String pos = null;
        try {
            String posCode = cmbPosCode.getSelectedItem().toString();
            StringBuilder sb = new StringBuilder(posCode);
            int len = posCode.length();
            int lastInd = sb.lastIndexOf(" ");
            pos = sb.substring(lastInd + 1, len).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return pos;
        }
    }

    /**
     * this Function is used for get From Date
     *
     * @return
     */
    private String funGetSelectedFromDate() 
    {
        fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + dteFromDate.getDate().getDate();
        return fromDate;
    }

    /**
     * this Function is used for get To Date
     *
     * @return
     */
    private String funGetSelectedToDate() 
    {
        toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + dteToDate.getDate().getDate();
        return toDate;
    }


    private void funExecuteButtonPressed()
    {
        fromDate = funGetSelectedFromDate();
        toDate = funGetSelectedToDate();
        String sql="";
        try
        {
            boolean flgValidCard=false;
            clsUtility obj=new clsUtility();
            new frmSwipCardPopUp(this, "frmRegisterDebitCard").setVisible(true);
            if (null != clsGlobalVarClass.gDebitCardNo) {
                if (!clsGlobalVarClass.gEnableNFCInterface && obj.funValidateDebitCardString(clsGlobalVarClass.gDebitCardNo)) {
                        
                    sql="select a.strCardNo,ifnull(b.strCustomerName,'') "
                        + " from tbldebitcardmaster a left outer join tblcustomermaster b "
                        + " on a.strCustomerCode=b.strCustomerCode "
                        + " where a.strCardString='"+clsGlobalVarClass.gDebitCardNo+"';";
                    ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rs.next())
                    {
                        cardNo=rs.getString(1);
                        lblCardNo.setText(rs.getString(1));
                        lblCustomerName.setText(rs.getString(2));
                    }
                    rs.close();
                    flgValidCard=true;
                }
                else if (clsGlobalVarClass.gEnableNFCInterface) {
                        
                    sql="select a.strCardNo,ifnull(b.strCustomerName,'') "
                        + " from tbldebitcardmaster a left outer join tblcustomermaster b "
                        + " on a.strCustomerCode=b.strCustomerCode "
                        + " where a.strCardString='"+clsGlobalVarClass.gDebitCardNo+"';";
                    ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rs.next())
                    {
                        cardNo=rs.getString(1);
                        lblCardNo.setText(rs.getString(1));
                        lblCustomerName.setText(rs.getString(2));
                    }
                    rs.close();
                    flgValidCard=true;
                } 
                else {
                    JOptionPane.showMessageDialog(this, "Invalid Card No");
                }
            }
            
            if(flgValidCard)
            {
                DefaultTableModel dmShowCards = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        //all cells false
                        return false;
                    }
                };
                dmShowCards.getDataVector().removeAllElements();

                DefaultTableModel dmTotals = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        //all cells false
                        return false;
                    }
                };
               
                dmTotals.getDataVector().removeAllElements();
                dmTotals.addColumn("");
                dmTotals.addColumn("");

                tblShowCardDetails.updateUI();
                tblTotal.updateUI();
                dmShowCards.addColumn("POS");
                dmShowCards.addColumn("Trans No");
                dmShowCards.addColumn("Trans Date");
                dmShowCards.addColumn("Trans Time");
                dmShowCards.addColumn("Trans Type");
                dmShowCards.addColumn("User");
                dmShowCards.addColumn("Amount");
                double totalRechargeAmt=0;
                StringBuilder sbSqlRecharge=new StringBuilder();
                StringBuilder sbSqlRedeem=new StringBuilder();
                StringBuilder sbSqlRefund=new StringBuilder();
                StringBuilder sbSqlOpenKot=new StringBuilder();
                StringBuilder sbSqlUnsettleBill=new StringBuilder();
                StringBuilder sbSqlTransferedAmt=new StringBuilder();
                StringBuilder sbSqlRevenueAmt=new StringBuilder();
                                
                
                sbSqlRecharge.setLength(0);
                sbSqlRecharge.append("select b.strPosName,a.intRechargeNo,date(a.dteDateCreated),time(a.dteDateCreated),'Recharge',a.strUserCreated,a.dblRechargeAmount "
                    + " from tbldebitcardrecharge a,tblposmaster b "
                    + " where date(a.dteDateCreated ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " and a.strPOSCode=b.strPosCode and a.strCardString='"+clsGlobalVarClass.gDebitCardNo+"' " );
                
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlRecharge.append("  and b.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                ResultSet rsRechargeDetails=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlRecharge.toString());
                while(rsRechargeDetails.next())
                {
                   arrListRecharge=new ArrayList<String>();
                    Object[] row = {rsRechargeDetails.getString(1), rsRechargeDetails.getString(2)
                        , rsRechargeDetails.getString(3),rsRechargeDetails.getString(4)
                        , rsRechargeDetails.getString(5),rsRechargeDetails.getString(6),rsRechargeDetails.getString(7)};
                    totalRechargeAmt+=rsRechargeDetails.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListRecharge.add(rsRechargeDetails.getString(1));
                    arrListRecharge.add(rsRechargeDetails.getString(2));
                    arrListRecharge.add(rsRechargeDetails.getString(3));
                    arrListRecharge.add(rsRechargeDetails.getString(4));
                    arrListRecharge.add(rsRechargeDetails.getString(5));
                    arrListRecharge.add(rsRechargeDetails.getString(6));
                    arrListRecharge.add(rsRechargeDetails.getString(7));
                }
                rsRechargeDetails.close();
                arrListShowCardDtl.add(arrListRecharge);

                double totalRedeemAmt=0;
                sbSqlRedeem.append("select c.strPosName,a.strBillNo,date(a.dteBillDate),time(a.dteBillDate),'Redeem',e.strUserCreated,a.dblTransactionAmt "
                    + "from tbldebitcardbilldetails a,tbldebitcardmaster b,tblposmaster c,tblbillhd e "
                    + "where date(e.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "and a.strCardNo=b.strCardNo and a.strPOSCode=c.strPosCode "
                    + "and a.strBillNo=e.strBillNo and a.strTransactionType='Settle' "
                    + "and b.strCardNo='"+cardNo+"'  ");
                
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                   sbSqlRedeem.append("  and c.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                ResultSet rsRedeemDetails=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlRedeem.toString());
                while(rsRedeemDetails.next())
                {
                    arrListRedeem=new ArrayList<String>();
                    Object[] row = {rsRedeemDetails.getString(1), rsRedeemDetails.getString(2)
                        , rsRedeemDetails.getString(3),rsRedeemDetails.getString(4)
                        , rsRedeemDetails.getString(5),rsRedeemDetails.getString(6),rsRedeemDetails.getString(7)};
                    totalRedeemAmt+=rsRedeemDetails.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListRedeem.add(rsRedeemDetails.getString(1));
                    arrListRedeem.add(rsRedeemDetails.getString(2));
                    arrListRedeem.add(rsRedeemDetails.getString(3));
                    arrListRedeem.add(rsRedeemDetails.getString(4));
                    arrListRedeem.add(rsRedeemDetails.getString(5));
                    arrListRedeem.add(rsRedeemDetails.getString(6));
                    arrListRedeem.add(rsRedeemDetails.getString(7));
                }
                rsRedeemDetails.close();
                arrListShowCardDtl.add(arrListRedeem);
                
                
                sbSqlRedeem.setLength(0);
                sbSqlRedeem.append("select c.strPosName,a.strBillNo,date(a.dteBillDate),time(a.dteBillDate),'Redeem',e.strUserCreated,a.dblTransactionAmt "
                    + "from tbldebitcardbilldetails a,tbldebitcardmaster b,tblposmaster c,tblqbillsettlementdtl d,tblqbillhd e,tblsettelmenthd f "
                    + "where date(e.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "and a.strCardNo=b.strCardNo and a.strPOSCode=c.strPosCode and a.strBillNo=d.strBillNo and d.strBillNo=e.strBillNo "
                    + "and d.strSettlementCode=f.strSettelmentCode and a.strTransactionType='Settle' "
                    + "and f.strSettelmentType='Debit Card' and b.strCardNo='"+cardNo+"'  ");
                
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                   sbSqlRedeem.append("  and c.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                rsRedeemDetails=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlRedeem.toString());
                while(rsRedeemDetails.next())
                {
                    arrListRedeem=new ArrayList<String>();
                    Object[] row = {rsRedeemDetails.getString(1), rsRedeemDetails.getString(2)
                        , rsRedeemDetails.getString(3),rsRedeemDetails.getString(4)
                        , rsRedeemDetails.getString(5),rsRedeemDetails.getString(6),rsRedeemDetails.getString(7)};
                    totalRedeemAmt+=rsRedeemDetails.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListRedeem.add(rsRedeemDetails.getString(1));
                    arrListRedeem.add(rsRedeemDetails.getString(2));
                    arrListRedeem.add(rsRedeemDetails.getString(3));
                    arrListRedeem.add(rsRedeemDetails.getString(4));
                    arrListRedeem.add(rsRedeemDetails.getString(5));
                    arrListRedeem.add(rsRedeemDetails.getString(6));
                    arrListRedeem.add(rsRedeemDetails.getString(7));
                }
                rsRedeemDetails.close();
                arrListShowCardDtl.add(arrListRedeem);
                
                

                double totalRefundAmt=0;
               // sbSql.setLength(0);
                sbSqlRefund.append("select b.strPosName,a.strRefundNo,date(a.dteDateCreated),time(a.dteDateCreated),'Refund'"
                    + ",a.strUserCreated,a.dblRefundAmt "
                    + " from tbldebitcardrefundamt a,tblposmaster b "
                    + " where date(a.dteDateCreated ) BETWEEN '" + fromDate + "' AND '" + toDate + "'  "
                    + " and a.strPOSCode=b.strPosCode and a.strCardString='"+clsGlobalVarClass.gDebitCardNo+"'  ");
                
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlRefund.append("  and b.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                  
                ResultSet rsRefundDetails=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlRefund.toString());
                while(rsRefundDetails.next())
                {
                    arrListRefund=new ArrayList<String>();
                    Object[] row = {rsRefundDetails.getString(1), rsRefundDetails.getString(2)
                        , rsRefundDetails.getString(3),rsRefundDetails.getString(4)
                        , rsRefundDetails.getString(5),rsRefundDetails.getString(6),rsRefundDetails.getString(7)};
                    totalRefundAmt+=rsRefundDetails.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListRefund.add(rsRefundDetails.getString(1));
                    arrListRefund.add(rsRefundDetails.getString(2));
                    arrListRefund.add(rsRefundDetails.getString(3));
                    arrListRefund.add(rsRefundDetails.getString(4));
                    arrListRefund.add(rsRefundDetails.getString(5));
                    arrListRefund.add(rsRefundDetails.getString(6));
                    arrListRefund.add(rsRefundDetails.getString(7));
                }
                rsRefundDetails.close();
                arrListShowCardDtl.add(arrListRefund);
                
                
                double totalOpenKOTAmt=0;
                // sbSql.setLength(0);
                sbSqlOpenKot.append(" select b.strPosName,a.strKOTNo,date(a.dteDateCreated),time(a.dteDateCreated), "
                    + " 'Open KOT',a.strUserCreated,sum(a.dblAmount),a.dblTaxAmt "
                    + " from tblitemrtemp a,tblposmaster b"
                    + " where date(a.dteDateCreated ) BETWEEN '" + fromDate + "' AND '" + toDate + "'  "
                    + " and a.strPOSCode=b.strPosCode and a.strCardNo='"+cardNo+"' "
                    + " and a.strPrintYN='Y' and a.strNCKotYN='N' ");
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlOpenKot.append("  and b.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                sbSqlOpenKot.append("  group by a.strKOTNo ");
                
                ResultSet rsOpenKOT=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlOpenKot.toString());
                while(rsOpenKOT.next())
                {
                    double openKOTAmt=(rsOpenKOT.getDouble(7)+rsOpenKOT.getDouble(8));
                    arrListOpenKOT=new ArrayList<String>();
                    Object[] row = {"<html><font color=red>"+rsOpenKOT.getString(1)+"</font></html>", 
                        "<html><font color=red>"+rsOpenKOT.getString(2)+"</font></html>",
                        "<html><font color=red>"+rsOpenKOT.getString(3)+"</font></html>",
                        "<html><font color=red>"+rsOpenKOT.getString(4)+"</font></html>",
                        "<html><font color=red>"+rsOpenKOT.getString(5)+"</font></html>",
                        "<html><font color=red>"+rsOpenKOT.getString(6)+"</font></html>",
                        "<html><font color=red>"+Math.rint(openKOTAmt)+"</font></html>"};
                    totalOpenKOTAmt+=openKOTAmt;
                  
                    dmShowCards.addRow(row);
                  
                    arrListOpenKOT.add(rsOpenKOT.getString(1));
                    arrListOpenKOT.add(rsOpenKOT.getString(2));
                    arrListOpenKOT.add(rsOpenKOT.getString(3));
                    arrListOpenKOT.add(rsOpenKOT.getString(4));
                    arrListOpenKOT.add(rsOpenKOT.getString(5));
                    arrListOpenKOT.add(rsOpenKOT.getString(6));
                    arrListOpenKOT.add(rsOpenKOT.getString(7));
                }
                rsOpenKOT.close();
                arrListShowCardDtl.add(arrListOpenKOT);
                
                
                double totalUnsettleAmt=0;
                sbSqlUnsettleBill.append("  select c.strPosName,a.strBillNo,date(a.dteBillDate),time(a.dteBillDate),'Unsettle Bill', "
                    + "  a.strUserCreated ,a.dblGrandTotal from tblbillhd a left outer join tbltablemaster b  "
                    + "  on a.strTableNo=b.strTableNo ,tblposmaster c  "
                    + " where date(a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " and a.strPOSCode=c.strPosCode and a.strCardNo='"+cardNo+"' "
                    + " and a.strBillNo not in (select strBillNo from tblbillsettlementdtl)");
                 
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlUnsettleBill.append("  and c.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                ResultSet rsUnsettleBill=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlUnsettleBill.toString());
                while(rsUnsettleBill.next())
                {
                    arrListUnsettleBill=new ArrayList<String>();
                    Object[] row = {"<html><font color=red>"+rsUnsettleBill.getString(1)+"</font></html>", 
                        "<html><font color=red>"+rsUnsettleBill.getString(2)+"</font></html>",
                        "<html><font color=red>"+rsUnsettleBill.getString(3)+"</font></html>",
                        "<html><font color=red>"+rsUnsettleBill.getString(4)+"</font></html>",
                        "<html><font color=red>"+rsUnsettleBill.getString(5)+"</font></html>",
                        "<html><font color=red>"+rsUnsettleBill.getString(6)+"</font></html>",
                        "<html><font color=red>"+rsUnsettleBill.getString(7)+"</font></html>"
                    };
                    totalUnsettleAmt+=rsUnsettleBill.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListUnsettleBill.add(rsUnsettleBill.getString(1));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(2));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(3));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(4));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(5));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(6));
                    arrListUnsettleBill.add(rsUnsettleBill.getString(7));
                }
                rsUnsettleBill.close();
                arrListShowCardDtl.add(arrListUnsettleBill);
                
                
                
                double totalTransferAmt=0;
                sbSqlTransferedAmt.append("  select c.strPosName,b.strCardNo,date(b.dteDateCreated),time(b.dteDateCreated)"
                    + ",'Balance Transfer',b.strUserCreated,a.dblRechargeAmt "
                    + " from tbldcrechargesettlementdtl a , tbldebitcardrecharge b,tblposmaster c  "
                    + " where date(b.dteDateCreated) BETWEEN '" + fromDate + "' AND '" + toDate + "'  "
                    + " and a.dblRechargeAmt=b.dblRechargeAmount and a.strRechargeNo=b.intRechargeNo "
                    + " and b.strPOSCode=c.strPosCode and a.strCardNo='"+cardNo+"' and a.strType='Debit Card' ");
                 
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlTransferedAmt.append("  and b.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                ResultSet rsTransferAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlTransferedAmt.toString());
                while(rsTransferAmt.next())
                {
                    arrListTransferAmt=new ArrayList<String>();
                    Object[] row = {rsTransferAmt.getString(1), rsTransferAmt.getString(2)
                        , rsTransferAmt.getString(3),rsTransferAmt.getString(4)
                        , rsTransferAmt.getString(5),rsTransferAmt.getString(6),rsTransferAmt.getString(7)};
                    totalTransferAmt+=rsTransferAmt.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListTransferAmt.add(rsTransferAmt.getString(1));
                    arrListTransferAmt.add(rsTransferAmt.getString(2));
                    arrListTransferAmt.add(rsTransferAmt.getString(3));
                    arrListTransferAmt.add(rsTransferAmt.getString(4));
                    arrListTransferAmt.add(rsTransferAmt.getString(5));
                    arrListTransferAmt.add(rsTransferAmt.getString(6));
                    arrListTransferAmt.add(rsTransferAmt.getString(7));
                }
                rsTransferAmt.close();
                arrListShowCardDtl.add(arrListTransferAmt);
                
                
                sbSqlRevenueAmt.setLength(0);
                sbSqlRevenueAmt.append("select b.strPosName,'',date(a.dtePOSDate),time(a.dtePOSDate),'Card Revenue'"
                    + ",a.strUserCreated,a.dblCardAmt "
                    + " from tbldebitcardrevenue a,tblposmaster b "
                    + " where date(a.dtePOSDate) BETWEEN '"+ fromDate + "' AND '" + toDate + "' "
                    + " and a.strPOSCode=b.strPosCode and a.strCardNo='"+cardNo+"' " );
                
                if(!cmbPosCode.getSelectedItem().toString().equals("All"))
                {
                    sbSqlRevenueAmt.append("  and b.strPOSCode='"+getSelectedPosCode()+"' ");
                }
                System.out.println(sbSqlRevenueAmt);
                
                List<String> arrListCardRevenue=new ArrayList<String>();
                double totalCardRevenue=0;
                ResultSet rsCardRevenue=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlRevenueAmt.toString());
                while(rsCardRevenue.next())
                {
                    
                    Object[] row = {rsCardRevenue.getString(1), rsCardRevenue.getString(2)
                        , rsCardRevenue.getString(3),rsCardRevenue.getString(4)
                        , rsCardRevenue.getString(5),rsCardRevenue.getString(6),rsCardRevenue.getString(7)};
                    totalCardRevenue+=rsCardRevenue.getDouble(7);
                    dmShowCards.addRow(row);
                    arrListCardRevenue.add(rsCardRevenue.getString(1));
                    arrListCardRevenue.add(rsCardRevenue.getString(2));
                    arrListCardRevenue.add(rsCardRevenue.getString(3));
                    arrListCardRevenue.add(rsCardRevenue.getString(4));
                    arrListCardRevenue.add(rsCardRevenue.getString(5));
                    arrListCardRevenue.add(rsCardRevenue.getString(6));
                    arrListCardRevenue.add(rsCardRevenue.getString(7));
                }
                rsCardRevenue.close();
                arrListShowCardDtl.add(arrListCardRevenue);
                
                
                totalAmt=totalRechargeAmt-(totalRedeemAmt+totalRefundAmt+totalOpenKOTAmt+totalUnsettleAmt+totalTransferAmt+totalCardRevenue);
                if(totalAmt<0)
                {
                    totalAmt=0;
                    Object[] total = {"Card Balance",Math.rint(totalAmt)};
                    dmTotals.addRow(total);
                }
                else
                {
                    Object[] total = {"Card Balance",Math.rint(totalAmt)};
                    dmTotals.addRow(total);
                }
                
                tblShowCardDetails.setModel(dmShowCards);
                tblTotal.setModel(dmTotals);

                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                
                /*
                int i = 0, j = 0;
                int k = 5;
                String s="";
                for (i = 0; i < tblShowCardDetails.getRowCount() + 1; i++) 
                {
                    s=tblShowCardDetails.getValueAt(i, k).toString();
                   
                }
                if (s.equalsIgnoreCase("Open KOT")) 
                {
                   tblShowCardDetails.set
                }*/
                
                tblShowCardDetails.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
                tblShowCardDetails.getColumnModel().getColumn(0).setPreferredWidth(200);
                tblShowCardDetails.getColumnModel().getColumn(1).setPreferredWidth(120);
                tblShowCardDetails.getColumnModel().getColumn(2).setPreferredWidth(120);
                tblShowCardDetails.getColumnModel().getColumn(3).setPreferredWidth(100);
                tblShowCardDetails.getColumnModel().getColumn(4).setPreferredWidth(100);
                tblShowCardDetails.getColumnModel().getColumn(5).setPreferredWidth(80);
                tblShowCardDetails.getColumnModel().getColumn(6).setPreferredWidth(70);
                
                DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
                rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
               // tblTotal.getColumnModel().getColumn(0).setCellRenderer(rightRenderer1);
                tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
                tblTotal.getColumnModel().getColumn(0).setPreferredWidth(500);
                tblTotal.getColumnModel().getColumn(1).setPreferredWidth(200);
            }
        
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    

    /**
     * Export File 
     * @param table
     * @param file 
     */
    void ExportFile(JTable table, File file) {
        try {
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
            TableModel model = table.getModel();
            sheet1.addCell(new Label(3, 0, "DSS Java Pos Audit Flash Report "));
            //sheet1.addCell(new Label(0,2,"gsdfg "));
            for (int i = 0; i < model.getColumnCount() + 1; i++) {
                Label column = new Label(i, 1, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int i = 0, j = 0;
            int k = 0;
            //System.out.println(model.getRowCount());
            for (i = 1; i < model.getRowCount() + 1; i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1, model.getValueAt(k, j).toString());
                    sheet1.addCell(row);
                }
                k++;
            }
            addLastOfExportReport(workbook1);
            workbook1.write();
            workbook1.close();
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + ExportReportPath + "/" + exportFormName + totdayDate + ".xls");
            //sendMail();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "File is already opened please close ");
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     *
     * Add last Export File 
     * Export Show card Details
     * @param workbook1 
     */

    private void addLastOfExportReport(WritableWorkbook workbook1) {
        try {
            int i = 0, j = 0, LastIndexReport = 0;
            if (exportFormName.equals("Modifed Bill")) {
                LastIndexReport = 4;
            } else if (exportFormName.equals("Voided Bill")) {
                LastIndexReport = 3;
            } else if (exportFormName.equals("Line Void")) {
                LastIndexReport = 3;
            } else if (exportFormName.equals("Void Kot")) {
                LastIndexReport = 3;
            }
            else if (exportFormName.trim().equalsIgnoreCase("Voided Advance Order Bill")) {
                LastIndexReport = 3;
            }

            WritableSheet sheet2 = workbook1.getSheet(0);
            int r = sheet2.getRows();
            System.out.println("Total Row\t" + r);
            System.out.println("tblTotal.getRowCount== "+tblTotal.getRowCount()+"");
            for (i = r; i < tblTotal.getRowCount() + r; i++) {
                for (j = 0; j < tblTotal.getColumnCount(); j++) {
                    System.out.println("j="+j+"\t(LastIndexReport+j)=\t" + (LastIndexReport + j) + "\n" + "i+1\t" + (i + 1) + "\n" + "tblTotal.getValueAt(0, "+j+")\t" + tblTotal.getValueAt(0, j).toString());
                    Label row = new Label(LastIndexReport + j, i + 1, tblTotal.getValueAt(0, j).toString());
                    sheet2.addCell(row);                   
                }
            }
            WritableSheet sheet3 = workbook1.getSheet(0);
            r = sheet3.getRows();
            Formatter fmt = new Formatter();
            Calendar cal = Calendar.getInstance();
            fmt.format("%tr", cal);
            Label row = new Label(1, r + 1, " Created On : " + clsGlobalVarClass.gPOSDateToDisplay + " At : " + fmt + " By : " + clsGlobalVarClass.gUserCode + " ");
            sheet2.addCell(row);
        } catch (IndexOutOfBoundsException | WriteException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * This method is used to create temp folder
     *
     * @return string
     */
    
   private void funCreateTempFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File Text_CardDtl = new File(filePath + "/Temp");
            if (!Text_CardDtl.exists())
            {
                Text_CardDtl.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
  
   
    public void funPrintShowCardDtlTextfile(List<List<String>> arrListShowCardDtlTemp,String custName,double totalAmount)
    {
        clsUtility objUtility=new clsUtility();
        try
        {
            double totalBalance=0;
            funCreateTempFolder();
            String filPath=System.getProperty("user.dir");
            File textFile=new File(filPath + "/Temp/Temp_Card_Dtl.txt");
            PrintWriter pw=new PrintWriter(textFile);
            pw.println(objUtility.funPrintTextWithAlignment("Card Transaction Detail",40,"Center"));
            pw.println(" ");
            pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSName,40,"Center"));
            pw.println(" ");
            pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName,40,"Center"));
            pw.println(" ");
            pw.println(" ");
            pw.print(objUtility.funPrintTextWithAlignment("Date",8,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(":",4,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSDate,28,"Left"));
            pw.println(" ");
            pw.print(objUtility.funPrintTextWithAlignment("Card No",8,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(":",4,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(cardNo,28,"Left"));
            pw.println(" ");

            pw.print(objUtility.funPrintTextWithAlignment("Customer Name",15,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(":",4,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(custName,21,"Left"));
            pw.println(" ");

            pw.println("----------------------------------------");
            pw.print(objUtility.funPrintTextWithAlignment("POS ",8,"Left"));
            pw.println(" ");
            pw.print(objUtility.funPrintTextWithAlignment("",6,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment("Trans No",10,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment("Date",12,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment("Type",5,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment("Amt",7,"RIGHT"));
            pw.println(" ");
            pw.println("----------------------------------------");
            for(int cnt=0;cnt<arrListShowCardDtlTemp.size();cnt++)
            {
                List<String> items=arrListShowCardDtlTemp.get(cnt);
                if(null!=items)
                {
                    String amt=items.get(6);
                    Double rechargeAmt=Double.valueOf(amt);
                    pw.print(objUtility.funPrintTextWithAlignment(items.get(0),8,"Left"));
                    pw.println(" ");
                    pw.print(objUtility.funPrintTextWithAlignment("",6,"Left"));
                    pw.print(objUtility.funPrintTextWithAlignment(items.get(1),10,"Left"));
                    pw.print(objUtility.funPrintTextWithAlignment(items.get(2),12,"Left"));
                    if(items.get(3).equals("Recharge"))
                    {
                        pw.print(objUtility.funPrintTextWithAlignment("RC",5,"Left"));
                    }
                    else if(items.get(3).equals("Redeem"))
                    {
                        pw.print(objUtility.funPrintTextWithAlignment("RD",5,"Left"));
                    }
                    else
                    {
                        pw.print(objUtility.funPrintTextWithAlignment("RF",5,"Left"));
                    }

                    pw.print(objUtility.funPrintTextWithAlignment(""+Math.rint(rechargeAmt),7,"RIGHT"));
                    pw.println(" ");
                }
            }

            pw.println(" ");
            pw.println("----------------------------------------");
            pw.println(" ");
            int row=tblTotal.getSelectedRow();
            pw.print(objUtility.funPrintTextWithAlignment("",27,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment("Total",6,"Left"));
            pw.print(objUtility.funPrintTextWithAlignment(""+totalAmount,7,"RIGHT"));
            pw.flush();
            pw.close();
            clsPrintingUtility objPrintingUtility=new clsPrintingUtility();
            if (clsGlobalVarClass.gShowBill) 
            {
                objPrintingUtility.funShowTextFile(textFile,"","");
            }
            objUtility.funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, filPath);
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

        pnlBackGround = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSReport/images/imgBGJPOS.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        pnlMain = new javax.swing.JPanel();
        pnlsalesGrid = new javax.swing.JScrollPane();
        tblShowCardDetails = new javax.swing.JTable();
        lblposCode = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        lblFromDate = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        pnlGridHeader = new javax.swing.JScrollPane();
        tblTotal = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnSwipeCard = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        lblCustomerName = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        pnlheader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblfromName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(100, 0));
        lblPosName = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(100, 0));
        lblUserCode = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        lblCardNo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(250, 250, 250));
        setExtendedState(MAXIMIZED_BOTH);
        setMaximumSize(new java.awt.Dimension(800, 600));
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

        pnlBackGround.setBackground(new java.awt.Color(250, 250, 250));
        pnlBackGround.setMaximumSize(new java.awt.Dimension(800, 600));
        pnlBackGround.setMinimumSize(new java.awt.Dimension(800, 600));
        pnlBackGround.setOpaque(false);
        pnlBackGround.setPreferredSize(new java.awt.Dimension(800, 600));
        pnlBackGround.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBackground(new java.awt.Color(250, 250, 250));
        pnlMain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        pnlMain.setMaximumSize(new java.awt.Dimension(800, 600));
        pnlMain.setMinimumSize(new java.awt.Dimension(800, 600));
        pnlMain.setOpaque(false);
        pnlMain.setPreferredSize(new java.awt.Dimension(800, 600));

        pnlsalesGrid.setBackground(new java.awt.Color(250, 250, 250));

        tblShowCardDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS", "Transaction No", "Transaction Date", "Transaction Time", "Transaction Type", "User", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblShowCardDetails.setRowHeight(30);
        tblShowCardDetails.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblShowCardDetailsMouseClicked(evt);
            }
        });
        pnlsalesGrid.setViewportView(tblShowCardDetails);

        lblposCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblposCode.setText("POS Name");

        cmbPosCode.setToolTipText("Select  POS");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date");

        dteFromDate.setToolTipText("Select From Date");
        dteFromDate.setPreferredSize(new java.awt.Dimension(119, 35));

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date");

        dteToDate.setToolTipText("Select To Date");
        dteToDate.setPreferredSize(new java.awt.Dimension(119, 35));

        pnlGridHeader.setBackground(new java.awt.Color(250, 250, 250));

        tblTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTotal.setRowHeight(30);
        pnlGridHeader.setViewportView(tblTotal);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Export File");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnSwipeCard.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSwipeCard.setForeground(new java.awt.Color(255, 255, 255));
        btnSwipeCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSwipeCard.setText("SWIPE");
        btnSwipeCard.setToolTipText("Export File");
        btnSwipeCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSwipeCard.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSwipeCardActionPerformed(evt);
            }
        });

        btnExport.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExport.setText("EXPORT");
        btnExport.setToolTipText("Export File");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setToolTipText("Export File");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintActionPerformed(evt);
            }
        });

        pnlheader.setBackground(new java.awt.Color(69, 164, 238));
        pnlheader.setMaximumSize(new java.awt.Dimension(700, 30));
        pnlheader.setMinimumSize(new java.awt.Dimension(700, 30));
        pnlheader.setName(""); // NOI18N
        pnlheader.setPreferredSize(new java.awt.Dimension(700, 30));
        pnlheader.setLayout(new javax.swing.BoxLayout(pnlheader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductNameMouseClicked(evt);
            }
        });
        pnlheader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        pnlheader.add(lblModuleName);

        lblfromName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfromName.setForeground(new java.awt.Color(255, 255, 255));
        lblfromName.setText("-Show Card");
        lblfromName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblfromNameMouseClicked(evt);
            }
        });
        pnlheader.add(lblfromName);
        pnlheader.add(filler4);
        pnlheader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblPosName.setMaximumSize(new java.awt.Dimension(250, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(250, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(250, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        pnlheader.add(lblPosName);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(120, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(120, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(120, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        pnlheader.add(lblDate);
        pnlheader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        pnlheader.add(lblUserCode);

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
        pnlheader.add(lblHOSign);

        lblCardNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlheader, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(86, 86, 86))
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addComponent(lblposCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlGridHeader, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlsalesGrid))
                .addContainerGap())
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addComponent(pnlheader, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblposCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCardNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(pnlsalesGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlGridHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlBackGround.add(pnlMain, new java.awt.GridBagConstraints());

        getContentPane().add(pnlBackGround, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblShowCardDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShowCardDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblShowCardDetailsMouseClicked

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
         objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblfromNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblfromNameMouseClicked
    {//GEN-HEADEREND:event_lblfromNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblfromNameMouseClicked

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

    private void btnSwipeCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwipeCardActionPerformed
        // TODO add your handling code here:
        try
        {
        if(clsGlobalVarClass.gEnableNFCInterface)
        {
            ReaderThread objReader=new ReaderThread();
            Thread objThread=new Thread(objReader);
            objThread.start();
            funExecuteButtonPressed();
            clsGlobalVarClass.gDebitCardNo = null;
            objThread.stop();
            objThread=null;
            System.out.println("Thread is="+objThread.isAlive());
            return;
        }
        else
        {
            funExecuteButtonPressed();
          }
        }
        catch(Exception e)
        {
            e.printStackTrace();;
        }
    
    }//GEN-LAST:event_btnSwipeCardActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("ShowCard");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equalsIgnoreCase(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }//CDE/Motif
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        try {
            
            File theDir = new File(ExportReportPath);
            File file = new File(ExportReportPath + "/" + exportFormName + totdayDate + ".xls");
            // if the directory does not exist, create it
            if (!theDir.exists()) {
                theDir.mkdir();
                ExportFile(tblShowCardDetails, file);
            } else {
                ExportFile(tblShowCardDetails, file);
            }
        } catch (Exception ex) {
            Logger.getLogger(frmShowCard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        funPrintShowCardDtlTextfile(arrListShowCardDtl,lblCustomerName.getText(),totalAmt);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("ShowCard");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("ShowCard");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSwipeCard;
    private javax.swing.JComboBox cmbPosCode;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCardNo;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblfromName;
    private javax.swing.JLabel lblposCode;
    private javax.swing.JPanel pnlBackGround;
    private javax.swing.JScrollPane pnlGridHeader;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlheader;
    private javax.swing.JScrollPane pnlsalesGrid;
    private javax.swing.JTable tblShowCardDetails;
    private javax.swing.JTable tblTotal;
    // End of variables declaration//GEN-END:variables
    private void funSetLookAndFeel()
    {
        try
        {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
            SwingUtilities.updateComponentTreeUI( this );
        }
        catch (UnsupportedLookAndFeelException e)
        {
            // handle exception
        }
        catch (ClassNotFoundException e)
        {
            // handle exception
        }
        catch (InstantiationException e)
        {
            // handle exception
        }
        catch (IllegalAccessException e)
        {
            // handle exception
        }
    }

    private void funResetLookAndFeel()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                System.out.println("lookandfeel"+info.getName());
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI( this );
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmShowCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmShowCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmShowCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmShowCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
