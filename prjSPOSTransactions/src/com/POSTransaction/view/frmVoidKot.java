/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmUserAuthenticationPopUp;
import com.POSPrinting.clsVoidKOTGenerator;
import com.POSTransaction.controller.clsVoidItemDetails;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmVoidKot extends javax.swing.JFrame
{

    ResultSet rs, rs1;
    String sql;
    String[] arrReason;
    String KotNo, resonCode = "", manualKOTNo, deleteMode, selectedTableNo;
    //java.util.Vector delItemName, delItemCode, delItemQuantity, delItemAmount;
    private double voidedAmount, singleItemAmount, selectedVoidQty;
    private HashMap<String, String> mapTableCombo;
    private HashMap<String, String> mapVoidedItemCode;
    private Map<String, clsVoidItemDetails> hmVoidItemDetails;
    private String KOT_TableNo, tableName_kot;
    private String voidKOTRemark = "";
    private clsUtility objUtility;
    private clsUtility2 objUtility2 = new clsUtility2();
    private String POSCode = "", POSDate = "", areaCode = "", tableNo = "";
    private String transactionUserCode = null;

    private String buttonClicked;
    private StringBuilder sqlBuilder;
    String sqlQuery = null;

    String userCode = clsGlobalVarClass.gUserCode;
    private boolean isAuditing = false;
    private final DecimalFormat decimalFormat;

    public frmVoidKot()
    {
        objUtility = new clsUtility();
        initComponents();
        cmbTables.requestFocus();
        sqlBuilder = new StringBuilder();
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        Date date1 = new Date();
        String new_str = String.format("%tr", date1);
        String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
        lblDate.setText(dateAndTime);
        mapVoidedItemCode = new HashMap<String, String>();
        hmVoidItemDetails = new HashMap<String, clsVoidItemDetails>();
        deleteMode = "KOT";
        decimalFormat = new DecimalFormat("#.##");

        String tbluserdtl = "tbluserdtl";
        if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
        {
            tbluserdtl = "tblsuperuserdtl";
        }
        else
        {
            tbluserdtl = "tbluserdtl";
        }
        sqlQuery = "select strAuditing from " + tbluserdtl + " where strUserCode='" + userCode + "' and strFormName='VoidKot'";
        funFillTableCombo();
        funFillHelpGrid("");
    }

    private void funFillHelpGrid(String searchTable)
    {
        try
        {
            sql = "select a.strKOTNo,a.strTableNo,b.strTableName,c.strWShortName"
                    + ",a.strPrintYN,a.strTakeAwayYesNo,a.strUserCreated,a.intPaxNo"
                    + ",sum(a.dblAmount),a.strManualKOTNo "
                    + "from tblitemrtemp a left outer join tbltablemaster b "
                    + "on a.strTableNo=b.strTableNo  left outer join  tblwaitermaster c "
                    + "on a.strWaiterNo=c.strWaiterNo  "
                    + "where a.strTableNo=b.strTableNo and a.strNCKotYN='N' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
            if (!cmbTables.getSelectedItem().toString().equals("All Tables"))
            {
                String tbNo = mapTableCombo.get(cmbTables.getSelectedItem().toString());
                sql += " and a.strTableNo='" + tbNo + "'";
            }

            if (searchTable.trim().length() > 0)
            {
                sql += " and b.strTableName like '" + searchTable + "%' ";
            }

            sql += " group by a.strKOTNo"
                    + " order by b.intSequence,a.strKOTNo";
            DefaultTableModel dm = (DefaultTableModel) tblVoidKotList.getModel();
            dm.setRowCount(0);

            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String KotNo = rs.getString("a.strKOTNo");
                String strTableName = rs.getString("b.strTableName");
                String waiterName = rs.getString("c.strWShortName");
                String takeAway = rs.getString("a.strTakeAwayYesNo");
                String user = rs.getString("a.strUserCreated");
                String pax = rs.getString("a.intPaxNo");
                double dblAmount = rs.getDouble("sum(a.dblAmount)");
                manualKOTNo = rs.getString("a.strManualKOTNo");
                Object[] row =
                {
                    KotNo, strTableName, waiterName, user, dblAmount, manualKOTNo
                };
                dm.addRow(row);
            }
            rs.close();
            tblVoidKotList.setModel(dm);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funVoidFullVoidButtonClicked()
    {
        deleteMode = "KOT";
        if (tblVoidKotList.getSelectedRow() < 0)
        {
            JOptionPane.showMessageDialog(this, "Please select kot to void");
            return;
        }
        funVoidKot();
    }

    private void funDeleteButtonClicked()
    {

        try
        {
            DefaultTableModel dtm = (DefaultTableModel) tblItemTable.getModel();
            if (tblItemTable.getModel().getRowCount() > 0)
            {
                selectedVoidQty = 0;
                int row = tblItemTable.getSelectedRow();
                System.out.println("Selected Row= " + row);
                if (row > -1)
                {
                    String itemName = tblItemTable.getValueAt(row, 0).toString();
                    String itemCode = tblItemTable.getValueAt(row, 3).toString();

                    if (Double.parseDouble(tblItemTable.getValueAt(row, 1).toString()) > 1)
                    {
                        new frmNumericKeyboard(this, true, "", "Double", "Enter quantity to void").setVisible(true);
                        if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > Double.parseDouble(tblItemTable.getValueAt(row, 1).toString()))
                        {
                            JOptionPane.showMessageDialog(this, "Please select valid quantity");
                            return;
                        }
                        else
                        {
                            if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) == 0)
                            {
                                return;
                            }

                            selectedVoidQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
                            double temAmt = Double.parseDouble(tblItemTable.getValueAt(row, 2).toString());
                            double temQty = Double.parseDouble(tblItemTable.getValueAt(row, 1).toString());
                            double temSingleItemAmt = temAmt / temQty;

                            if ((temQty - selectedVoidQty) == 0)
                            {
                                dtm.removeRow(row);
                            }
                            else
                            {
                                tblItemTable.setValueAt((temQty - selectedVoidQty), row, 1);
                                tblItemTable.setValueAt((temAmt - (temSingleItemAmt * selectedVoidQty)), row, 2);
                            }

                            clsVoidItemDetails objVoidItemDetails = new clsVoidItemDetails();
                            objVoidItemDetails.setItemCode(itemCode);
                            objVoidItemDetails.setItemName(itemName);
                            objVoidItemDetails.setItemVoidQty(selectedVoidQty);
                            objVoidItemDetails.setItemVoidAmt(temSingleItemAmt * selectedVoidQty);
                            hmVoidItemDetails.put(itemCode, objVoidItemDetails);
                            mapVoidedItemCode.put(itemCode, String.valueOf(selectedVoidQty));
                        }
                    }
                    else
                    {
                        double amt = Double.parseDouble(tblItemTable.getValueAt(row, 2).toString());
                        clsVoidItemDetails objVoidItemDetails = new clsVoidItemDetails();
                        objVoidItemDetails.setItemCode(itemCode);
                        objVoidItemDetails.setItemName(itemName);
                        objVoidItemDetails.setItemVoidQty(1);
                        objVoidItemDetails.setItemVoidAmt(amt * 1);
                        hmVoidItemDetails.put(itemCode, objVoidItemDetails);

                        mapVoidedItemCode.put(itemCode, "1");
                        dtm.removeRow(row);
                    }

                    for (int cn = row; cn < tblItemTable.getRowCount(); cn++)//to remove modifiers
                    {
                        String modiName = tblItemTable.getValueAt(row, 0).toString();
                        String modiItemCode = tblItemTable.getValueAt(row, 3).toString();
                        if (modiItemCode.substring(0, 7).equals(itemCode) && modiName.startsWith("-->"))
                        {
                            dtm.removeRow(row);
                        }
                        else
                        {
                            break;
                        }
                    }

                    List<clsItemDtlForTax> arrListItemDtl = new ArrayList<clsItemDtlForTax>();
                    double subTotal = 0;
                    for (int cnt = 0; cnt < tblItemTable.getRowCount(); cnt++)
                    {
                        clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                        objItemDtlForTax.setItemCode(tblItemTable.getValueAt(cnt, 3).toString());
                        objItemDtlForTax.setItemName(tblItemTable.getValueAt(cnt, 0).toString());
                        objItemDtlForTax.setAmount(Double.parseDouble(tblItemTable.getValueAt(cnt, 2).toString()));
                        objItemDtlForTax.setDiscAmt(0);
                        objItemDtlForTax.setDiscPer(0);
                        subTotal += Double.parseDouble(tblItemTable.getValueAt(cnt, 2).toString());
                        arrListItemDtl.add(objItemDtlForTax);
                    }

                    double taxAmt = 0;
                    List<clsTaxCalculationDtls> arrListTaxDtl = objUtility.funCalculateTax(arrListItemDtl, POSCode, POSDate, areaCode, "DineIn", subTotal, 0, "Void KOT", "S01", "Sales");
                    for (clsTaxCalculationDtls objTaxDtl : arrListTaxDtl)
                    {
                        taxAmt += objTaxDtl.getTaxAmount();
                    }
                    arrListTaxDtl = null;
                    arrListItemDtl = null;

                    lblTaxValue.setText(String.valueOf(Math.rint(taxAmt)));
                    lblSubTotalValue.setText(Double.toString(subTotal));
                    lblTotalAmt.setText(String.valueOf(Math.rint(subTotal + taxAmt)));
                    tblItemTable.updateUI();
                    tblItemTable.changeSelection(-1, 0, false, false);
                }
                else
                {
                    new frmOkPopUp(this, "Please select Item first", "Error", 1).setVisible(true);
                }
            }
            else
            {
                new frmOkPopUp(this, "Please select Item first", "Error", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private String funGetVodidedDate()
    {
        String voidDate = null;
        try
        {
            java.util.Date dt = new java.util.Date();
            String time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            String bdte = clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);
            voidDate = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
            voidDate += " " + time;

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return voidDate;
        }
    }

    private void funGetReasonCode()
    {
        String favoritereason = null;
        try
        {
            int reasoncount = 0, i = 0;
            sql = "select count(strReasonName) from tblreasonmaster where strKot='Y'";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                reasoncount = rs.getInt(1);
            }
            if (reasoncount > 0)
            {
                arrReason = new String[reasoncount];
                sql = "select strReasonName from tblreasonmaster where strKot='Y'";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                i = 0;
                while (rs.next())
                {
                    arrReason[i] = rs.getString(1);
                    i++;
                }
                favoritereason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.PLAIN_MESSAGE, null, arrReason, arrReason[0]);

            }
            else
            {
                new frmOkPopUp(this, "Please Create Reason", "Warning", 1).setVisible(true);
            }

            if (favoritereason != null)
            {
                sql = "select strReasonCode from tblreasonmaster where strReasonName='" + favoritereason + "'";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                resonCode = rs.getString("strReasonCode");
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funVoidKot()
    {
        String voidBillType = "Full KOT Void";
        try
        {
            if (this.transactionUserCode != null && !this.transactionUserCode.isEmpty())
            {
                userCode = this.transactionUserCode;
            }
            isAuditing = false;
            rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            if (rs1.next())
            {
                if (Boolean.parseBoolean(rs1.getString(1)))
                {
                    isAuditing = true;
                }
            }
            else
            {
                isAuditing = true;
            }

            String voidedDate = funGetVodidedDate();
            String reasonCode = "NoAuditing";
            if (isAuditing)
            {
                funGetReasonCode();
                reasonCode = resonCode;
            }

            if (isAuditing)
            {
                if (!clsGlobalVarClass.gTouchScreenMode)
                {
                    voidKOTRemark = JOptionPane.showInputDialog(null, "Enter Remarks");
                }
                else
                {
                    new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Remark.").setVisible(true);
                    voidKOTRemark = clsGlobalVarClass.gKeyboardValue;
                }
            }

            int exc = 0;
            if (!"".equals(reasonCode))
            {
                String strType = "VKot";
                sql = "select strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo,"
                        + "strKOTNo,intPaxNo,strUserCreated,dteDateCreated "
			+ "from tblitemrtemp "
                        + "where strKOTNo='" + lblKOTNo.getText() + "' and strNCKotYN='N' ";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

                voidedAmount = 0;
                while (rs.next())
                {
                    sql = "insert into tbltempvoidkot(strItemName,strItemCode,dblItemQuantity,strTableNo,strWaiterNo,strKOTNo) "
                            + "values('" + rs.getString("strItemName") + "','" + rs.getString("strItemCode") + "','"
                            + rs.getString("dblItemQuantity") + "','" + rs.getString("strTableNo") + "','" + rs.getString("strWaiterNo") + "'"
                            + ",'" + rs.getString("strKOTNo") + "')";
                    //System.out.println(sql);
                    exc = clsGlobalVarClass.dbMysql.execute(sql);

                    sql = "update tblvoidkot  set strVoidBillType='Full KOT Void' "
                            + "where strKOTNo='" + lblKOTNo.getText() + "'";
                    exc = clsGlobalVarClass.dbMysql.execute(sql);

                    sql = "insert into tblvoidkot(strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity,"
                            + "dblAmount,strWaiterNo,strKOTNo,intPaxNo,strType,strReasonCode,strUserCreated,"
                            + "dteDateCreated,dteVoidedDate,strClientCode,strManualKOTNo,strRemark,strVoidBillType,strAreaCode) "
                            + "values('" + rs.getString("strTableNo") + "','" + rs.getString("strPOSCode") + "'"
                            + ",'" + rs.getString("strItemCode") + "','" + rs.getString("strItemName") + "'"
                            + "," + "'" + rs.getString("dblItemQuantity") + "','" + rs.getString("dblAmount") + "'"
                            + ",'" + rs.getString("strWaiterNo") + "','" + rs.getString("strKOTNo") + "'"
                            + "," + "'" + rs.getString("intPaxNo") + "','" + strType + "','" + reasonCode + "'"
                            + ",'" + userCode + "','" + rs.getString("dteDateCreated") + "'"
                            + "," + "'" + voidedDate + "','" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "'"
			    + ",'" + objUtility.funCheckSpecialCharacters(voidKOTRemark) + "'"
			    + ",'" + voidBillType + "','"+areaCode+"' ) ";

                    voidedAmount = voidedAmount + rs.getDouble("dblAmount");
                    if (isAuditing)
                    {
                        exc = clsGlobalVarClass.dbMysql.execute(sql);
                    }

                    sql = "Delete from tblnonchargablekot where strKOTNo='" + lblKOTNo.getText() + "' ";
                    clsGlobalVarClass.dbMysql.execute(sql);

                }

                //send void kot sms
                sql = "select a.strSendSMSYN,a.longMobileNo "
                        + "from tblsmssetup a "
                        + "where  (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All' )  "
                        + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                        + "and a.strTransactionName='VoidKOT' "
                        + "and a.strSendSMSYN='Y'; ";
                ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsSendSMS.next())
                {
                    String mobileNo = rsSendSMS.getString(2);//mobileNo

                    funSendVoidKOTSMS(lblKOTNo.getText(), mobileNo);

                }
                rsSendSMS.close();

                funResetField();
                funRemotePrint();
                funFillHelpGrid("");

            }

            if (clsGlobalVarClass.gConnectionActive.equals("Y"))
            {
                if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
                {
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Audit", "Void");
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funResetField()
    {
        tblItemTable.setModel(new DefaultTableModel());
        lblKOTNo.setText("");
        lblUserCreated.setText("");
        lblSubTotalValue.setText("");
        lblTaxValue.setText("");
        lblTotalAmt.setText("");
        panelHeader.setVisible(true);
    }

    private void funRemotePrint()
    {
        try
        {

	    if(clsGlobalVarClass.gAreaWisePricing.equals("Y"))
	    {
		sql = "select a.strItemName,c.strCostCenterCode,c.strPrinterPort,a.strItemCode,c.intPrimaryPrinterNoOfCopies "
                    + "from tblvoidkot a,tblmenuitempricingdtl b,tblcostcentermaster c "
                    + "where left(a.strItemCode,7)=b.strItemCode "
		    + "and b.strCostCenterCode=c.strCostCenterCode "
		    + "and a.strAreaCode=b.strAreaCode "
                    + "and a.strKOTNo='" + KotNo + "' "
		    + "and a.strPrintKOT='N' "
                    + "and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                    + "group by c.strCostCenterCode";
	    }else{
		sql = "select a.strItemName,c.strCostCenterCode,c.strPrinterPort,a.strItemCode,c.intPrimaryPrinterNoOfCopies "
                    + "from tblvoidkot a,tblmenuitempricingdtl b,tblcostcentermaster c "
                    + "where left(a.strItemCode,7)=b.strItemCode "
		    + "and b.strCostCenterCode=c.strCostCenterCode "
                    + "and a.strKOTNo='" + KotNo + "' "
		    + "and a.strPrintKOT='N' "
                    + "and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                    + "group by c.strCostCenterCode";
	    }
            ResultSet rsPrint = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                clsVoidKOTGenerator objVoidKOTGenerator = new clsVoidKOTGenerator();
                while (rsPrint.next())
                {
                    String costCenterCode = rsPrint.getString(2);
		    int costCenterWiseCopies = Integer.parseInt(rsPrint.getString(5));
                    objVoidKOTGenerator.funGenerateVoidKOT(tableName_kot, KotNo, "VoidKOT", costCenterCode, mapVoidedItemCode,costCenterWiseCopies,"");
                }
                rsPrint.close();
                sql = "Update tblvoidkot set strPrintKOT='Y' where strKOTNo='" + KotNo + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                clsVoidKOTGenerator objVoidKOTGenerator = new clsVoidKOTGenerator();
                while (rsPrint.next())
                {
                    String costCenterCode = rsPrint.getString(2);
		    int costCenterWiseCopies = Integer.parseInt(rsPrint.getString(5));
                    objVoidKOTGenerator.funGenerateVoidKOT(tableName_kot, KotNo, "VoidKOT", costCenterCode, mapVoidedItemCode,1,"");
		    if(costCenterWiseCopies>1){
			for(int k=0;k<costCenterWiseCopies-1;k++){
			    objVoidKOTGenerator.funGenerateVoidKOT(tableName_kot, KotNo, "VoidKOT", costCenterCode, mapVoidedItemCode,costCenterWiseCopies-1,"Reprint");
			}
		    }
                }
                rsPrint.close();
                sql = "Update tblvoidkot set strPrintKOT='Y' where strKOTNo='" + KotNo + "' ";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            funDeleteTempItem();
            mapVoidedItemCode.clear();
            if (deleteMode.equals("KOT"))
            {
                funDeleteKOTFromTemp();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDeleteKOTFromTemp()
    {
        try
        {
            sql = "delete from tblitemrtemp where strKOTNo='" + KotNo + "' and strTableNo='" + KOT_TableNo + "' and strNCKotYN='N' ";
            clsGlobalVarClass.dbMysql.execute(sql);

            String sql_tableBusy = "select count(*) from tblitemrtemp where strTableNo='" + KOT_TableNo + "' and strNCKotYN='N' ";
            ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sql_tableBusy);
            rsCount.next();
            int cnt = rsCount.getInt(1);

            if (cnt == 0)
            {
                String sql_status = "update tbltablemaster set strStatus='Normal',intPaxNo=0 "
                        + "where strTableNo='" + KOT_TableNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql_status);
            }
            sql = "delete from tblkottaxdtl where strKOTNo='" + KotNo + "' and strTableNo='" + KOT_TableNo + "';";
            clsGlobalVarClass.dbMysql.execute(sql);

            //insert into itemrtempbck table
            new clsUtility().funInsertIntoTblItemRTempBck(KOT_TableNo, KotNo);

            resonCode = "";
            KOT_TableNo = "";
            rsCount.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillTableCombo()
    {
        try
        {
            cmbTables.removeAllItems();
            mapTableCombo = new HashMap<String, String>();
            cmbTables.addItem("All Tables");
            String sqlFillCombo = "select b.strTableNo,a.strTableName "
                    + "from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo "
                    + "and  (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR a.strPOSCode='All') and strNCKotYN='N'  "
                    + "group by b.strTableNo "
                    + "order by a.intSequence";
            ResultSet rsFillCombo = clsGlobalVarClass.dbMysql.executeResultSet(sqlFillCombo);
            while (rsFillCombo.next())
            {
                mapTableCombo.put(rsFillCombo.getString(2), rsFillCombo.getString(1));
                cmbTables.addItem(rsFillCombo.getString(2));
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDeleteTempItem()
    {
        try
        {
            clsGlobalVarClass.dbMysql.execute("delete from tbltempvoidkot ");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGetTableNo(String KOTNo)
    {
        String tableNo = "";
        try
        {
            String sqlTableNo = "select strTableNo from tblitemrtemp where strKOTNo='" + KOTNo + "' and strNCKotYN='N' group by strTableNo;";
            ResultSet rsTableNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableNo);
            rsTableNo.next();
            tableNo = rsTableNo.getString(1);
            rsTableNo.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return tableNo;
        }
    }

    private String funGetTableName(String KOTNo)
    {
        String tableNameKOT = "";
        try
        {
            String sqlTableNo = "select b.strTableName from tblitemrtemp a,tbltablemaster b "
                    + " where a.strKOTNo='" + KOTNo + "' and a.strTableNo=b.strTableNo and strNCKotYN='N' "
                    + " group by a.strTableNo;";
            ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableNo);
            if (rsTableName.next())
            {
                tableNameKOT = rsTableName.getString(1);
            }
            rsTableName.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return tableNameKOT;
        }
    }

    private String funGetWaiterName(String KOTNo)
    {
        String waiterFullName = "";
        try
        {
            String sqlTableNo = "select b.strWShortName,b.strWFullName "
                    + "from tblitemrtemp a,tblwaitermaster b  "
                    + "where a.strKOTNo='" + KOTNo + "'  "
                    + "and a.strWaiterNo=b.strWaiterNo  "
                    + "and strNCKotYN='N'  "
                    + "group by a.strTableNo; ";
            ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableNo);
            if (rsTableName.next())
            {
                waiterFullName = rsTableName.getString(2);
            }
            rsTableName.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return waiterFullName;
        }
    }

    private void funFillItemGrid()
    {
        try
        {

            double totalQtyCount = 0;
            int row = tblVoidKotList.getSelectedRow();
            KotNo = tblVoidKotList.getValueAt(row, 0).toString();
            lblKOTNo.setText(KotNo);
            sql = "select count(*) "
                    + "from tblitemrtemp "
                    + "where strKOTNo='" + KotNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strNCKotYN='N' ";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cnt = rs.getInt(1);
            if (cnt > 0)
            {
                sql = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated,strItemCode"
                        + " ,strPOSCode,strTableNo "
                        + " from tblitemrtemp "
                        + " where strKOTNo='" + KotNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strNCKotYN='N' ";
                double subTotalAmt = 0.00;
                DefaultTableModel dm = new DefaultTableModel()
                {
                    @Override
                    public boolean isCellEditable(int row, int column)
                    {
                        //all cells false
                        return false;
                    }
                };
                dm.getDataVector().removeAllElements();
                dm.addColumn("Description");
                dm.addColumn("Qty");
                dm.addColumn("Amount");
                dm.addColumn("ItemCode");

                List<clsItemDtlForTax> arrListItemDtl = new ArrayList<clsItemDtlForTax>();
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rs.next())
                {
                    String itemName = rs.getString("strItemName");
                    String dblQuantity = rs.getString("dblItemQuantity");

                    if (!itemName.contains("-->"))
                    {
                        double itemQty = Double.parseDouble(dblQuantity);
                        if (itemQty > 1.0)
                        {
                            totalQtyCount = totalQtyCount + itemQty;
                        }
                        else
                        {
                            totalQtyCount = totalQtyCount + 1.0;
                        }
                    }

                    double dblAmount = rs.getDouble("dblAmount");
                    String itemCode = rs.getString("strItemCode");
                    subTotalAmt = subTotalAmt + dblAmount;
                    lblUserCreated.setText(rs.getString("strUserCreated"));
                    POSDate = rs.getString("dteDateCreated");
                    POSCode = rs.getString(7);
                    tableNo = rs.getString(8);
                    Object[] fillrow =
                    {
                        itemName, dblQuantity, dblAmount, itemCode
                    };
                    dm.addRow(fillrow);

                    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                    objItemDtlForTax.setItemCode(itemCode);
                    objItemDtlForTax.setItemName(itemName);
                    objItemDtlForTax.setAmount(dblAmount);
                    objItemDtlForTax.setDiscAmt(0);
                    objItemDtlForTax.setDiscPer(0);
                    arrListItemDtl.add(objItemDtlForTax);
                }

                sql = "select strAreaCode from tbltablemaster where strTableNo='" + tableNo + "';";
                ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsAreaCode.next())
                {
                    areaCode = rsAreaCode.getString(1);
                }
                rsAreaCode.close();

                double taxAmt = 0;
                List<clsTaxCalculationDtls> arrListTaxDtl = objUtility.funCalculateTax(arrListItemDtl, POSCode, POSDate, areaCode, "DineIn", subTotalAmt, 0, "Void KOT", "S01", "Sales");
                for (clsTaxCalculationDtls objTaxDtl : arrListTaxDtl)
                {
                    taxAmt += objTaxDtl.getTaxAmount();
                }
                arrListTaxDtl = null;

                lblTaxValue.setText(String.valueOf(Math.rint(taxAmt)));
                lblSubTotalValue.setText(Double.toString(subTotalAmt));
                lblTotalAmt.setText(String.valueOf(Math.rint(subTotalAmt + taxAmt)));
                tblItemTable.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tblItemTable.setShowHorizontalLines(true);
                tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(230);
                tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
                tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);

                hmVoidItemDetails.clear();

                if (totalQtyCount <= 1)
                {
                    btnUp.setEnabled(false);
                    btnDown.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnDone.setEnabled(false);
                }
                else
                {
                    btnUp.setEnabled(true);
                    btnDown.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnDone.setEnabled(true);
                }

            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDeleteItem()
    {
        String voidBillType = "Item Void";
        try
        {

            if (this.transactionUserCode != null && !this.transactionUserCode.isEmpty())
            {
                userCode = this.transactionUserCode;
            }

            if (hmVoidItemDetails.size() == 0)
            {
                new frmOkPopUp(this, "Please select Item first", "Error", 1).setVisible(true);
            }
            else
            {
                isAuditing = false;
                rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
                if (rs1.next())
                {
                    if (Boolean.parseBoolean(rs1.getString(1)))
                    {
                        isAuditing = true;
                    }
                }
                else
                {
                    isAuditing = true;
                }

                double voidedQty = 0;
                String voidedDate = funGetVodidedDate();

                if (isAuditing)
                {
                    if (!clsGlobalVarClass.gTouchScreenMode)
                    {
                        voidKOTRemark = JOptionPane.showInputDialog(null, "Enter Remarks");
                    }
                    else
                    {
                        new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Remark.").setVisible(true);
                        voidKOTRemark = clsGlobalVarClass.gKeyboardValue;
                    }
                }

                String reasonCode = "NoAuditing";
                if (isAuditing)
                {
                    funGetReasonCode();
                    reasonCode = resonCode;
                }

                if (!reasonCode.equals(""))
                {
                    for (Map.Entry<String, clsVoidItemDetails> entry : hmVoidItemDetails.entrySet())
                    {
                        double qtyAfterDelete = 0;
                        String strType = "VKot";
                        sql = "select strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity,"
                                + "dblAmount,strWaiterNo,strKOTNo,intPaxNo,strUserCreated,dteDateCreated "
                                + "from tblitemrtemp where strItemCode='" + entry.getKey() + "' "
                                + "and strKOTNo='" + lblKOTNo.getText() + "' and strTableNo='" + selectedTableNo + "' and strNCKotYN='N' ";

                        rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rs.next())
                        {
                            qtyAfterDelete = Double.parseDouble(rs.getString(5)) - entry.getValue().getItemVoidQty();
                            singleItemAmount = Double.parseDouble(rs.getString("dblAmount")) / Double.parseDouble(rs.getString("dblItemQuantity"));
                            if (qtyAfterDelete > 0)
                            {
                                voidedQty = entry.getValue().getItemVoidQty();
                                voidedAmount = singleItemAmount;
                            }
                            else
                            {
                                voidedQty = entry.getValue().getItemVoidQty();
                                voidedAmount = entry.getValue().getItemVoidAmt();
                            }

			    
			    
                            sql = "insert into tblvoidkot(strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity,"
                                    + "dblAmount,strWaiterNo,strKOTNo,intPaxNo,strType,strReasonCode,"
                                    + "strUserCreated,dteDateCreated,dteVoidedDate,strClientCode,strRemark,strVoidBillType,strAreaCode) "
                                    + "values('" + rs.getString("strTableNo") + "','" + rs.getString("strPOSCode") + "'"
                                    + ",'" + rs.getString("strItemCode") + "','" + rs.getString("strItemName") + "',"
                                    + "'" + voidedQty + "','" + voidedAmount + "','" + rs.getString("strWaiterNo") + "'"
                                    + ",'" + rs.getString("strKOTNo") + "'," + "'" + rs.getString("intPaxNo") + "'"
                                    + ",'" + strType + "','" + reasonCode + "','" + userCode + "'"
                                    + ",'" + rs.getString("dteDateCreated") + "'," + "'" + voidedDate + "'" + ""
                                    + ",'" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + objUtility.funCheckSpecialCharacters(voidKOTRemark) + "'"
				    + ",'" + voidBillType + "','"+areaCode+"' ) ";

                            int exc = 0;

                            if (isAuditing)
                            {
                                exc = clsGlobalVarClass.dbMysql.execute(sql);
                            }

                            sql = "Update tblnonchargablekot set dblQuantity='" + qtyAfterDelete + "' where strKOTNo='" + rs.getString("strKOTNo") + "' and strItemCode='" + rs.getString("strItemCode") + "' ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                            if (qtyAfterDelete == 0)
                            {
                                sql = "Delete from tblnonchargablekot where strKOTNo='" + rs.getString("strKOTNo") + "' and strItemCode='" + rs.getString("strItemCode") + "' ";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }
                            if (qtyAfterDelete > 0)
                            {
                                sql = "insert into tbltempvoidkot(strItemName,strItemCode,dblItemQuantity,strTableNo,strWaiterNo,strKOTNo) "
                                        + "values('" + entry.getValue().getItemName() + "','"
                                        + entry.getValue().getItemCode() + "','" + entry.getValue().getItemVoidQty() + "','" + rs.getString("strTableNo") + "','" + rs.getString("strWaiterNo") + "','" + rs.getString("strKOTNo") + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);

                                sql = "update tblitemrtemp set dblItemQuantity=" + qtyAfterDelete + ", "
                                        + " dblAmount=" + (singleItemAmount * qtyAfterDelete) + ", dblTaxAmt='" + lblTaxValue.getText() + "' "
                                        + " where strItemCode='" + entry.getValue().getItemCode() + "'"
                                        + " and strKOTNo='" + lblKOTNo.getText() + "'  and strNCKotYN='N' ";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }
                            else
                            {
                                sql = "insert into tbltempvoidkot(strItemName,strItemCode,dblItemQuantity,strTableNo,strWaiterNo,strKOTNo) "
                                        + "values('" + rs.getString("strItemName") + "','" + rs.getString("strItemCode") + "','"
                                        + rs.getString("dblItemQuantity") + "','" + rs.getString("strTableNo") + "','" + rs.getString("strWaiterNo") + "','" + rs.getString("strKOTNo") + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);

                                sql = "delete from tblitemrtemp where left(strItemCode,7)='" + entry.getValue().getItemCode() + "' "
                                        + " and strKOTNo='" + lblKOTNo.getText() + "' and strTableNo='" + selectedTableNo + "' and strNCKotYN='N' ";
                                clsGlobalVarClass.dbMysql.execute(sql);

                                sql = "select count(*) from tblitemrtemp where strTableNo='" + selectedTableNo + "'  and strNCKotYN='N' ";
                                ResultSet rsDel = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                                rsDel.next();
                                if (rsDel.getInt(1) == 0)
                                {
                                    sql = "update tbltablemaster set strStatus='Normal',intPaxNo=0 "
                                            + "where strTableNo='" + selectedTableNo + "'";
                                    clsGlobalVarClass.dbMysql.execute(sql);
                                }
                                rsDel.close();
                            }

                        }
                        rs.close();
                    }

                    //insert into itemrtempbck table 
                    new clsUtility().funInsertIntoTblItemRTempBck(selectedTableNo);

                    sql = "delete from tblkottaxdtl "
                            + "where strKOTNo='" + lblKOTNo.getText() + "' and strTableNo='" + selectedTableNo + "' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    sql = "insert into tblkottaxdtl values('" + selectedTableNo + "','" + lblKOTNo.getText() + "','" + lblSubTotalValue.getText() + "','" + lblTaxValue.getText() + "') ";
                    clsGlobalVarClass.dbMysql.execute(sql);

                    hmVoidItemDetails.clear();

                    funRemotePrint();
                    funFillHelpGrid("");

                    //send void kot sms
                    sql = "select a.strSendSMSYN,a.longMobileNo "
                            + "from tblsmssetup a "
                            + "where  (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All' )  "
                            + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                            + "and a.strTransactionName='VoidKOT' "
                            + "and a.strSendSMSYN='Y'; ";
                    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsSendSMS.next())
                    {
                        String mobileNo = rsSendSMS.getString(2);//mobileNo

                        funSendVoidKOTSMS(lblKOTNo.getText(), mobileNo);

                    }
                    rsSendSMS.close();

                    funResetField();
                }

                resonCode = "";
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
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
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelKOTDtl = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        scrKOTDtl = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        lblPaxNo = new javax.swing.JLabel();
        btnUp = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblKOT = new javax.swing.JLabel();
        lblKOTNo = new javax.swing.JLabel();
        lblSubTotalTitle = new javax.swing.JLabel();
        lblSubTotalValue = new javax.swing.JLabel();
        lblTaxTitle = new javax.swing.JLabel();
        lblTaxValue = new javax.swing.JLabel();
        lblUserCreated = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnFullVoidKOT = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnDone = new javax.swing.JButton();
        panelKOTList = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        scrKOTGrid = new javax.swing.JScrollPane();
        tblVoidKotList = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        cmbTables = new javax.swing.JComboBox();
        txtSearchAllTables = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();

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

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Void KOT");
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

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelKOTDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelKOTDtl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelKOTDtl.setForeground(new java.awt.Color(254, 184, 80));
        panelKOTDtl.setPreferredSize(new java.awt.Dimension(260, 600));
        panelKOTDtl.setLayout(null);

        tblItemTable.setBackground(new java.awt.Color(51, 102, 255));
        tblItemTable.setForeground(new java.awt.Color(255, 255, 255));
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount", "ItemCode"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
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
        scrKOTDtl.setViewportView(tblItemTable);

        panelKOTDtl.add(scrKOTDtl);
        scrKOTDtl.setBounds(0, 50, 360, 350);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");
        panelKOTDtl.add(lblTotal);
        lblTotal.setBounds(180, 470, 60, 30);
        panelKOTDtl.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPDark.png"))); // NOI18N
        btnUp.setToolTipText("Up");
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPLite.png"))); // NOI18N
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
        panelKOTDtl.add(btnUp);
        btnUp.setBounds(0, 510, 70, 40);

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDeleteMouseClicked(evt);
            }
        });
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });
        panelKOTDtl.add(btnDelete);
        btnDelete.setBounds(0, 410, 40, 40);

        lblKOT.setText("Kot No. :");
        panelKOTDtl.add(lblKOT);
        lblKOT.setBounds(0, 10, 60, 30);
        panelKOTDtl.add(lblKOTNo);
        lblKOTNo.setBounds(60, 10, 80, 30);

        lblSubTotalTitle.setText("Sub Total ");
        panelKOTDtl.add(lblSubTotalTitle);
        lblSubTotalTitle.setBounds(180, 410, 70, 20);

        lblSubTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelKOTDtl.add(lblSubTotalValue);
        lblSubTotalValue.setBounds(260, 410, 90, 20);

        lblTaxTitle.setText("Tax");
        panelKOTDtl.add(lblTaxTitle);
        lblTaxTitle.setBounds(180, 440, 60, 20);

        lblTaxValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelKOTDtl.add(lblTaxValue);
        lblTaxValue.setBounds(260, 440, 90, 20);
        panelKOTDtl.add(lblUserCreated);
        lblUserCreated.setBounds(210, 10, 120, 30);

        lblTotalAmt.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalAmt.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelKOTDtl.add(lblTotalAmt);
        lblTotalAmt.setBounds(260, 470, 90, 30);

        lblUser.setText("User :");
        panelKOTDtl.add(lblUser);
        lblUser.setBounds(160, 10, 50, 30);

        btnFullVoidKOT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFullVoidKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnFullVoidKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnFullVoidKOT.setText("FULL VOID ");
        btnFullVoidKOT.setToolTipText("Full Void");
        btnFullVoidKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFullVoidKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnFullVoidKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFullVoidKOTActionPerformed(evt);
            }
        });
        panelKOTDtl.add(btnFullVoidKOT);
        btnFullVoidKOT.setBounds(250, 510, 100, 40);

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownDark.png"))); // NOI18N
        btnDown.setToolTipText("Down");
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownLite.png"))); // NOI18N
        btnDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownMouseClicked(evt);
            }
        });
        panelKOTDtl.add(btnDown);
        btnDown.setBounds(80, 510, 70, 40);

        btnDone.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDone.setForeground(new java.awt.Color(255, 255, 255));
        btnDone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDone.setText("DONE");
        btnDone.setToolTipText("Save");
        btnDone.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDone.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDone.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDoneMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnDoneMouseEntered(evt);
            }
        });
        panelKOTDtl.add(btnDone);
        btnDone.setBounds(50, 410, 100, 40);

        panelKOTList.setBackground(new java.awt.Color(255, 255, 255));

        tblVoidKotList.setBackground(new java.awt.Color(254, 254, 254));
        tblVoidKotList.setForeground(new java.awt.Color(1, 1, 1));
        tblVoidKotList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "KOT", "Table", "Waiter", "User", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblVoidKotList.setRowHeight(25);
        tblVoidKotList.setSelectionBackground(new java.awt.Color(0, 120, 255));
        tblVoidKotList.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblVoidKotList.getTableHeader().setReorderingAllowed(false);
        tblVoidKotList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblVoidKotListMouseClicked(evt);
            }
        });
        scrKOTGrid.setViewportView(tblVoidKotList);
        if (tblVoidKotList.getColumnModel().getColumnCount() > 0)
        {
            tblVoidKotList.getColumnModel().getColumn(0).setMinWidth(100);
            tblVoidKotList.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        btnClose.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });

        cmbTables.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTablesActionPerformed(evt);
            }
        });

        txtSearchAllTables.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchAllTablesMouseClicked(evt);
            }
        });
        txtSearchAllTables.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchAllTablesKeyReleased(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");

        javax.swing.GroupLayout panelKOTListLayout = new javax.swing.GroupLayout(panelKOTList);
        panelKOTList.setLayout(panelKOTListLayout);
        panelKOTListLayout.setHorizontalGroup(
            panelKOTListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTListLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelKOTListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrKOTGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTListLayout.createSequentialGroup()
                        .addComponent(cmbTables, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))))
        );
        panelKOTListLayout.setVerticalGroup(
            panelKOTListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKOTListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelKOTListLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelKOTListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cmbTables, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrKOTGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelKOTDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelKOTList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelKOTList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(panelKOTDtl, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
        // TODO add your handling code here:
        deleteMode = "Item";
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
        // TODO add your handling code here:

        if (btnUp.isEnabled())
        {
            if (tblItemTable.getModel().getRowCount() > 0)
            {
                int r = tblItemTable.getSelectedRow();
                tblItemTable.changeSelection(r - 1, 0, false, false);
            }
            else
            {
                new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
            }
        }

    }//GEN-LAST:event_btnUpMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // TODO add your handling code here:
        if (btnDelete.isEnabled())
        {
            funDeleteButtonClicked();
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnFullVoidKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFullVoidKOTActionPerformed

        buttonClicked = "FullVoid";
        String formName = "VoidKot";
        try
        {
            if(false)// (objUtility.funCheckTableStatusFromItemRTemp(selectedTableNo))
            {
                JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
            }
            else
            {
                if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
                {
                    funVoidFullVoidButtonClicked();
                }
                else
                {

                    boolean isTrue = objUtility2.funHasTLA(formName, clsGlobalVarClass.gUserCode);
                    if (isTrue)
                    {
                        String isValidUser = "Invalid User";
                        frmUserAuthenticationPopUp okCancelPopUp = new frmUserAuthenticationPopUp(null, "User Authentication!!!", formName);
                        okCancelPopUp.setVisible(true);
                        String enterUserCode = okCancelPopUp.getUserName();
                        String enterPassword = okCancelPopUp.getPassword();
                        int res2 = okCancelPopUp.getResult();

                        if (res2 == 1) // pressing OK button
                        {
                            isValidUser = objUtility2.funIsValidUser(enterUserCode, enterPassword);
                        }

                        if (isValidUser.equalsIgnoreCase("Valid User"))
                        {
                            boolean isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
                            if (isUserGranted)
                            {
                                funSetTransactionUserDtl(enterUserCode);

                                funVoidFullVoidButtonClicked();

                                funResetTransactionUserDtl();
                            }
                            else
                            {
                                new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
                            }
                        }
                        else
                        {
                            new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
                        }
                    }
                    else
                    {
                        funVoidFullVoidButtonClicked();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnFullVoidKOTActionPerformed

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
        // TODO add your handling code here:
        if (btnDown.isEnabled())
        {

            if (tblItemTable.getModel().getRowCount() > 0)
            {
                int r = tblItemTable.getSelectedRow();
                int rowcount = tblItemTable.getRowCount();
                if (r < rowcount)
                {
                    tblItemTable.changeSelection(r + 1, 0, false, false);
                }
                else if (r == rowcount)
                {
                    r = 0;
                    tblItemTable.changeSelection(r, 0, false, false);
                }
            }
            else
            {
                new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
            }
        }
    }//GEN-LAST:event_btnDownMouseClicked

    private void btnDoneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDoneMouseClicked

        if (btnDone.isEnabled())
        {

            buttonClicked = "ItemVoid";
            String formName = "VoidKot";
            try
            {
                if(false)// (objUtility.funCheckTableStatusFromItemRTemp(selectedTableNo))
                {
                    JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
                }
                else
                {
                    if (hmVoidItemDetails.size() == 0)
                    {
                        new frmOkPopUp(this, "Please select Item first", "Error", 1).setVisible(true);
                    }
                    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
                    {
                        funDeleteItem();
                    }
                    else
                    {
                        boolean isTrue = objUtility2.funHasTLA(formName, clsGlobalVarClass.gUserCode);
                        if (isTrue)
                        {
                            String isValidUser = "Invalid User";
                            frmUserAuthenticationPopUp okCancelPopUp = new frmUserAuthenticationPopUp(null, "User Authentication!!!", formName);
                            okCancelPopUp.setVisible(true);
                            String enterUserCode = okCancelPopUp.getUserName();
                            String enterPassword = okCancelPopUp.getPassword();
                            int res2 = okCancelPopUp.getResult();

                            if (res2 == 1) // pressing OK button
                            {
                                isValidUser = objUtility2.funIsValidUser(enterUserCode, enterPassword);
                            }

                            if (isValidUser.equalsIgnoreCase("Valid User"))
                            {
                                boolean isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
                                if (isUserGranted)
                                {
                                    funSetTransactionUserDtl(enterUserCode);

                                    funDeleteItem();

                                    funResetTransactionUserDtl();
                                }
                                else
                                {
                                    new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
                                }
                            }
                            else
                            {
                                new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
                            }
                        }
                        else
                        {
                            funDeleteItem();
                        }

                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDoneMouseClicked

    private void btnDoneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDoneMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDoneMouseEntered

    private void tblVoidKotListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVoidKotListMouseClicked
        // TODO add your handling code here:
        try
        {
            String selectedTableName = tblVoidKotList.getValueAt(tblVoidKotList.getSelectedRow(), 1).toString();
            String KOTNo = tblVoidKotList.getValueAt(tblVoidKotList.getSelectedRow(), 0).toString();
            selectedTableNo = mapTableCombo.get(selectedTableName);
            KOT_TableNo = funGetTableNo(KOTNo);//ritesh
            tableName_kot = funGetTableName(KOTNo);
            funFillItemGrid();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblVoidKotListMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("VoidKot");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void cmbTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTablesActionPerformed
        // TODO add your handling code here:
        funFillHelpGrid("");
    }//GEN-LAST:event_cmbTablesActionPerformed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("VoidKot");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("VoidKot");
    }//GEN-LAST:event_formWindowClosing

    private void txtSearchAllTablesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSearchAllTablesMouseClicked
    {//GEN-HEADEREND:event_txtSearchAllTablesMouseClicked
        // TODO add your handling code here:
        frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search Tables");
        keyboard.setVisible(true);
        keyboard.setAlwaysOnTop(true);
        keyboard.setAutoRequestFocus(true);
        txtSearchAllTables.setText(clsGlobalVarClass.gKeyboardValue);

        funFillHelpGrid(txtSearchAllTables.getText().trim());
    }//GEN-LAST:event_txtSearchAllTablesMouseClicked

    private void txtSearchAllTablesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchAllTablesKeyReleased
    {//GEN-HEADEREND:event_txtSearchAllTablesKeyReleased

        funFillHelpGrid(txtSearchAllTables.getText().trim());
    }//GEN-LAST:event_txtSearchAllTablesKeyReleased

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnFullVoidKOT;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox cmbTables;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblKOT;
    public javax.swing.JLabel lblKOTNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSubTotalTitle;
    private javax.swing.JLabel lblSubTotalValue;
    private javax.swing.JLabel lblTaxTitle;
    private javax.swing.JLabel lblTaxValue;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserCreated;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelKOTDtl;
    private javax.swing.JPanel panelKOTList;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrKOTDtl;
    private javax.swing.JScrollPane scrKOTGrid;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblVoidKotList;
    private javax.swing.JTextField txtSearchAllTables;
    // End of variables declaration//GEN-END:variables

    private void funSetTransactionUserDtl(String userCode)
    {
        this.transactionUserCode = userCode;

    }

    private void funResetTransactionUserDtl()
    {
        this.transactionUserCode = null;

    }

    private void funCloseAuthenticationDialog()
    {
        Window[] windows = Window.getWindows();
        for (Window window : windows)
        {
            if (window instanceof JDialog)
            {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1 && dialog.getContentPane().getComponent(0) instanceof JOptionPane)
                {
                    dialog.dispose();
                }
            }
        }
    }

    private void funSendVoidKOTSMS(String kotNo, String mobileNo) throws Exception
    {
        StringBuilder mainSMSBuilder = new StringBuilder();

        String sql = "select a.strKOTNo,sum(a.dblAmount),ifnull(b.strTableName,''),ifnull(c.strWFullName,'') "
                + ",TIME_FORMAT(time(a.dteVoidedDate),'%h:%i')voidTime,d.strReasonName,a.strRemark "
                + "from tblvoidkot a "
                + "left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
                + "left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo "
                + "left outer join tblreasonmaster d on a.strReasonCode=d.strReasonCode "
                + "where a.strKOTNo='" + kotNo + "' "
                + "group by a.strKOTNo ";
        ResultSet rsVoidKOT = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsVoidKOT.next())
        {
            mainSMSBuilder.append("VoidKOT");
            mainSMSBuilder.append(" ,KOT:" + kotNo);
            mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
            mainSMSBuilder.append(" ,User:" + userCode);
            mainSMSBuilder.append(" ,Table:" + rsVoidKOT.getString(3));
            mainSMSBuilder.append(" ,Waiter:" + rsVoidKOT.getString(4));
            mainSMSBuilder.append(" ,Time:" + rsVoidKOT.getString(5));
            mainSMSBuilder.append(" ,Voided Amount:" + decimalFormat.format(Math.rint(rsVoidKOT.getDouble(2))));
            mainSMSBuilder.append(" ,Reason:" + rsVoidKOT.getString(6));
            mainSMSBuilder.append(" ,Remarks:" + rsVoidKOT.getString(7));
        }
        rs.close();

        ArrayList<String> mobileNoList = new ArrayList<>();
        String mobNos[] = mobileNo.split(",");
        for (String mn : mobNos)
        {
            mobileNoList.add(mn);
        }
        clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
        objSMSSender.start();
    }

}
