/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsPurchaseOrderDtl;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmPurchaseOrder extends javax.swing.JFrame
{

    clsUtility objUtility = new clsUtility();
    private Map<String, clsPurchaseOrderDtl> hmPurchaseOrderItemDtl = new HashMap<String, clsPurchaseOrderDtl>();
    private String subGroupName, groupName, dtPOSDate;
    private List<clsTaxCalculationDtls> arrListTaxCal;
    private boolean isOldPO=false;
    /**
     * This method is used to initialize frmPromotionGroupMaster
     */
    public frmPurchaseOrder()
    {
        initComponents();
        try
        {
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);

            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            String bdte = clsGlobalVarClass.gPOSStartDate;
            Date bDate = dFormat.parse(bdte);
            dtPOSDate = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();

            funSetShortCutKeys();
            subGroupName = "";
            groupName = "";
            dtePODate.setDate(objUtility.funGetDateToSetCalenderDate());
            dteDeliveryDate.setDate(objUtility.funGetDateToSetCalenderDate());
            funCalculateItemStock();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    private int funCalculateItemStock() throws Exception
    {
        String sql = "select date(dteStartDate) from tblsetup";
        ResultSet rsStartDate = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsStartDate.next())
        {
            String date = rsStartDate.getString(1);
            Date dt = new Date(Integer.parseInt(date.split("-")[0]) - 1900, Integer.parseInt(date.split("-")[1]) - 1, Integer.parseInt(date.split("-")[2]));
            clsGlobalVarClass.funCalculateStock(dt, new Date(), "All", "Both", "Stock");
        }
        rsStartDate.close();
        return 1;
    }

    private void funSetMenuItemData(String itemCode) throws Exception
    {
        String sql = "select a.strItemCode,a.strItemName,b.strSubGroupName,c.strGroupName,a.dblPurchaseRate"
                + " ,d.intBalance,a.strExternalCode "
                + " from tblitemmaster a,tblsubgrouphd b,tblgrouphd c,tblitemcurrentstk d "
                + " where a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode "
                + " and a.strItemCode=d.strItemCode and a.strItemCode='" + itemCode + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            txtItemCode.setText(rsItemInfo.getString(1));
            lblItemName.setText(rsItemInfo.getString(2));
            subGroupName = rsItemInfo.getString(3);
            groupName = rsItemInfo.getString(4);
            txtOrderQty.setText("1.00");
            txtOrderQty.selectAll();
            txtPurRate.setText(rsItemInfo.getString(5));
            lblStkQty.setText(rsItemInfo.getString(6));
            txtExternalCode.setText(rsItemInfo.getString(7));
            txtOrderQty.requestFocus();
        }
        rsItemInfo.close();
    }

    private void funAddRow() throws Exception
    {
        if (hmPurchaseOrderItemDtl.containsKey(txtItemCode.getText().trim()))
        {
            clsPurchaseOrderDtl objPurchaseOrderDtl = hmPurchaseOrderItemDtl.get(txtItemCode.getText().trim());
            objPurchaseOrderDtl.setOrderQty(objPurchaseOrderDtl.getOrderQty() + Double.parseDouble(txtOrderQty.getText()));
            double amt = objPurchaseOrderDtl.getPurchaseRate() * objPurchaseOrderDtl.getOrderQty();
            objPurchaseOrderDtl.setAmount(amt);
            hmPurchaseOrderItemDtl.put(txtItemCode.getText().trim(), objPurchaseOrderDtl);
        }
        else
        {
            clsPurchaseOrderDtl objPurchaseOrderDtl = new clsPurchaseOrderDtl();
            objPurchaseOrderDtl.setItemCode(txtItemCode.getText().trim());
            objPurchaseOrderDtl.setItemName(lblItemName.getText().trim());
            objPurchaseOrderDtl.setOrderQty(Double.parseDouble(txtOrderQty.getText()));
            objPurchaseOrderDtl.setPurchaseRate(Double.parseDouble(txtPurRate.getText()));
            double amt = objPurchaseOrderDtl.getPurchaseRate() * objPurchaseOrderDtl.getOrderQty();
            objPurchaseOrderDtl.setAmount(amt);
            objPurchaseOrderDtl.setSubGroupName(subGroupName.trim());
            objPurchaseOrderDtl.setGroupName(groupName.trim());
            hmPurchaseOrderItemDtl.put(txtItemCode.getText().trim(), objPurchaseOrderDtl);
        }
        isOldPO=false;
        funRefreshMenuItemGrid();
        
        txtItemCode.setText("");
        lblItemName.setText("");
        txtOrderQty.setText("0.00");
        txtPurRate.setText("0.00");
        lblStkQty.setText("");
        txtExternalCode.setText("");
    }

    private void funRemoveRow() throws Exception
    {
        for (int i = 0; i < tblItemDetails.getRowCount(); i++)
        {
            String itemCode = "";
            boolean flgSelect = Boolean.parseBoolean(tblItemDetails.getValueAt(i, 6).toString());
            if (flgSelect)
            {
                itemCode = tblItemDetails.getValueAt(i, 7).toString().trim();
                if (hmPurchaseOrderItemDtl.containsKey(itemCode))
                {
                    hmPurchaseOrderItemDtl.remove(itemCode);
                }
            }
        }
        funRefreshMenuItemGrid();
    }

    private void funRefreshMenuItemGrid() throws Exception
    {
        DefaultTableModel dmMenuItems = (DefaultTableModel) tblItemDetails.getModel();
        dmMenuItems.setRowCount(0);

        double subTotal = 0, taxAmt = 0, grandTotal = 0;

        List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

        for (Map.Entry<String, clsPurchaseOrderDtl> entry : hmPurchaseOrderItemDtl.entrySet())
        {
            Object[] arrObj =
            {
                entry.getValue().getItemName(), entry.getValue().getOrderQty(), entry.getValue().getPurchaseRate(),
                 entry.getValue().getAmount(), entry.getValue().getSubGroupName(), entry.getValue().getGroupName(),
                 false, entry.getValue().getItemCode()
            };
            subTotal += entry.getValue().getAmount();
            dmMenuItems.addRow(arrObj);

            clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
            objItemDtl.setItemCode(entry.getValue().getItemCode());
            objItemDtl.setItemName(entry.getValue().getItemName());
            objItemDtl.setAmount(entry.getValue().getAmount());
            objItemDtl.setDiscAmt(0);
            arrListItemDtls.add(objItemDtl);
        }

        arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, "", "", subTotal, 0.00, "", "", "Purchase");

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

        grandTotal = subTotal + taxAmt;
        tblItemDetails.setModel(dmMenuItems);
        if(!isOldPO){
            txtTaxAmt.setText(String.valueOf(taxAmt));
        }
        txtSubTotal.setText(String.valueOf(subTotal));
        txtGrandTotal.setText(String.valueOf(grandTotal));
        
        funResetDetailFields();
        
    }

    private void funOpenPurchaseOrderSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("PurchseOrder");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetPurchseOrderData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetPurchseOrderData(Object[] data)
    {
        try
        {
            hmPurchaseOrderItemDtl.clear();
            isOldPO=true;
            DefaultTableModel dm = (DefaultTableModel) tblItemDetails.getModel();
            dm.setRowCount(0);

            String POCode = data[0].toString();
            String sql = "select a.strPOCode,a.dtePODate,a.strSupplierCode,b.strSupplierName,a.dteDeliveryDate,a.dblTaxAmt "
                    + " from tblpurchaseorderhd a,tblsuppliermaster b "
                    + " where a.strSupplierCode=b.strSupplierCode and a.strPOCode='" + POCode + "' "
                    + " and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                txtPOCode.setText(rs.getString(1));

                String[] spPODate = (rs.getString(2).split(" ")[0].split("-"));
                String PODate = spPODate[2] + "-" + spPODate[1] + "-" + spPODate[0];
                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(PODate);
                dtePODate.setDate(date);

                String[] spDelDate = (rs.getString(5).split(" ")[0].split("-"));
                String delDate = spDelDate[2] + "-" + spDelDate[1] + "-" + spDelDate[0];
                java.util.Date dtDelDate = new SimpleDateFormat("dd-MM-yyyy").parse(delDate);
                dteDeliveryDate.setDate(dtDelDate);

                txtSuppCode.setText(rs.getString(3));
                txtSupplierName.setText(rs.getString(4));
                txtTaxAmt.setText(String.valueOf(rs.getDouble(6)));
            }
            rs.close();

            sql = "select a.strItemCode,a.strItemName,z.dblOrderQty,z.dblPurchaseRate,b.strSubGroupName,c.strGroupName "
                    + " from tblpurchaseorderdtl z,tblitemmaster a,tblsubgrouphd b,tblgrouphd c"
                    + " where z.strItemCode=a.strItemCode and a.strSubGroupCode=b.strSubGroupCode "
                    + " and b.strGroupCode=c.strGroupCode and z.strPOCode='" + POCode + "' "
                    + " and z.strClientCode='" + clsGlobalVarClass.gClientCode + "' ";

            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                clsPurchaseOrderDtl objPurchaseOrderDtl = new clsPurchaseOrderDtl();
                objPurchaseOrderDtl.setItemCode(rs.getString(1));
                objPurchaseOrderDtl.setItemName(rs.getString(2));
                objPurchaseOrderDtl.setOrderQty(rs.getDouble(3));
                objPurchaseOrderDtl.setPurchaseRate(rs.getDouble(4));
                double amt = objPurchaseOrderDtl.getPurchaseRate() * objPurchaseOrderDtl.getOrderQty();
                objPurchaseOrderDtl.setAmount(amt);
                objPurchaseOrderDtl.setSubGroupName(rs.getString(5));
                objPurchaseOrderDtl.setGroupName(rs.getString(6));
                hmPurchaseOrderItemDtl.put(rs.getString(1), objPurchaseOrderDtl);
            }
            rs.close();

            funRefreshMenuItemGrid();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGeneratePOCode() throws Exception
    {
        String poCode = "";

        String sql = "select count(dblLastNo) from tblinternal where strTransactionType='PurchaseOrder'";
        ResultSet rsPurchaseOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsPurchaseOrder.next();
        int cnt = rsPurchaseOrder.getInt(1);
        rsPurchaseOrder.close();
        if (cnt > 0)
        {
            sql = "select dblLastNo from tblinternal where strTransactionType='PurchaseOrder'";
            rsPurchaseOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsPurchaseOrder.next();
            long code = rsPurchaseOrder.getLong(1);
            code = code + 1;
            poCode = "PO" + String.format("%07d", code);
            sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='PurchaseOrder'";
            clsGlobalVarClass.dbMysql.execute(sql);
            rsPurchaseOrder.close();
        }
        else
        {
            poCode = "PO00000001";
            sql = "insert into tblinternal values('PurchaseOrder'," + 1 + ")";
            clsGlobalVarClass.dbMysql.execute(sql);
        }

        return poCode;
    }

    /**
     * This method is used to save promotion group master
     */
    private void funSavePurchaseOrder() throws Exception
    {
        if (!clsGlobalVarClass.validateEmpty(txtSuppCode.getText().trim()))
        {
            new frmOkPopUp(this, "Please Select Supplier", "Error", 0).setVisible(true);
            txtSuppCode.requestFocus();
        }
        else
        {
            if (txtPOCode.getText().trim().isEmpty())
            {
                String POCode = funGeneratePOCode();
                txtPOCode.setText(POCode);
            }

            if (funInsertPurchaseOrderDtlTable() && funInsertPurchaseOrderTaxDtlTable())
            {
                String closePO = "N";
                if (chkClosePO.isSelected())
                {
                    closePO = "Y";
                }
                String dtPODate = (dtePODate.getDate().getYear() + 1900) + "-" + (dtePODate.getDate().getMonth() + 1) + "-" + (dtePODate.getDate().getDate());
                String dtDelDate = (dteDeliveryDate.getDate().getYear() + 1900) + "-" + (dteDeliveryDate.getDate().getMonth() + 1) + "-" + (dteDeliveryDate.getDate().getDate());

                String sql = "delete from tblpurchaseorderhd where strPOCode='" + txtPOCode.getText() + "' "
                        + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "insert into tblpurchaseorderhd (strPOCode,dtePODate,strSupplierCode"
                        + ",dblSubTotal,dblTaxAmt,dblExtraAmt,dblGrandTotal,strUserCreated,strUserEdited"
                        + ",dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,dteDeliveryDate,strClosePO) "
                        + "values('" + txtPOCode.getText() + "','" + dtPODate + "','" + txtSuppCode.getText() + "'"
                        + ",'" + txtSubTotal.getText() + "','" + txtTaxAmt.getText() + "','0.00','" + txtGrandTotal.getText() + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "','N','" + dtDelDate + "','" + closePO + "')";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                JOptionPane.showMessageDialog(null, "Data entered successfully " + txtPOCode.getText());

                funGeneratePlaceOrderTextReport();
                funResetFields();
            }
        }
    }

    private boolean funInsertPurchaseOrderDtlTable() throws Exception
    {
        String sql = "delete from tblpurchaseorderdtl where strPOCode='" + txtPOCode.getText() + "' "
                + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
        clsGlobalVarClass.dbMysql.execute(sql);

        for (Map.Entry<String, clsPurchaseOrderDtl> entry : hmPurchaseOrderItemDtl.entrySet())
        {
            sql = "insert into tblpurchaseorderdtl (strPOCode,strItemCode,dblOrderQty,dblPurchaseRate"
                    + ",dblAmount,strRemarks,strClientCode,strDataPostFlag) "
                    + "values('" + txtPOCode.getText() + "','" + entry.getValue().getItemCode() + "'"
                    + ",'" + entry.getValue().getOrderQty() + "','" + entry.getValue().getPurchaseRate() + "'"
                    + ",'" + entry.getValue().getAmount() + "','','" + clsGlobalVarClass.gClientCode + "','N')";
            clsGlobalVarClass.dbMysql.execute(sql);
        }

        return true;
    }

    private boolean funInsertPurchaseOrderTaxDtlTable() throws Exception
    {
        String sql = "delete from tblpurchaseordertaxdtl where strPOCode='" + txtPOCode.getText() + "' "
                + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
        clsGlobalVarClass.dbMysql.execute(sql);

        for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
        {
            sql = "insert into tblpurchaseordertaxdtl (strPOCode,strTaxCode,dblTaxableAmount,dblTaxAmount"
                    + ",strClientCode,strDataPostFlag) "
                    + "values('" + txtPOCode.getText() + "','" + objTaxCalculationDtls.getTaxCode() + "'"
                    + ",'" + objTaxCalculationDtls.getTaxableAmount() + "','" + objTaxCalculationDtls.getTaxAmount() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','N')";
            clsGlobalVarClass.dbMysql.execute(sql);
        }

        return true;
    }

    private boolean funCheckDuplicateName(String promoGroupName, String promoGroupCode)
    {
        boolean flgResult = true;
        try
        {
            String sql = "select strPromoGroupName from tblpromogroupmaster "
                    + " where strPromoGroupName='" + promoGroupName + "' and strPromoGroupCode!='" + promoGroupCode + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                flgResult = false;
            }
            rs.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return flgResult;
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
                txtExternalCode.requestFocus();
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset detail fields
     */
    private void funResetFields()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('u');
            txtSuppCode.requestFocus();
            txtSuppCode.setText("");
            txtPOCode.setText("");
            txtItemCode.setText("");
            lblItemName.setText("");
            txtOrderQty.setText("1.00");
            txtPurRate.setText("0.00");
            txtSubTotal.setText("0.00");
            txtTaxAmt.setText("0.00");
            txtGrandTotal.setText("0.00");
            txtOrderQty.setText("0.00");
            txtSupplierName.setText("");
            lblStkQty.setText("0.00");
            subGroupName = "";
            groupName = "";
            chkClosePO.setSelected(false);
            DefaultTableModel dm = (DefaultTableModel) tblItemDetails.getModel();
            dm.setRowCount(0);
            dtePODate.setDate(objUtility.funGetDateToSetCalenderDate());
            dteDeliveryDate.setDate(objUtility.funGetDateToSetCalenderDate());

            hmPurchaseOrderItemDtl.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    /**
     * This method is used to reset fields
     */
    private void funResetDetailFields()
    {
        try
        {
            txtItemCode.setText("");
            lblItemName.setText("");
            txtOrderQty.setText("0.00");
            txtPurRate.setText("0.00");
            lblStkQty.setText("0.00");
            subGroupName = "";
            groupName = "";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    private void funValidateAndSave()
    {
        try
        {
            funSavePurchaseOrder();
            funResetFields();
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
            objUtility.funCallForSearchForm("PurchseOrder");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                txtPOCode.setText(data[0].toString());
                funSetPurchseOrderData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funOpenMenuItemSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("MenuItemForPO");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetMenuItemData(data[0].toString());
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funExternalCodeEntered()
    {
        try
        {
            String externalCode = txtExternalCode.getText().trim();
            String sql = "select strItemCode from tblitemmaster "
                    + " where strExternalCode='" + externalCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                funSetMenuItemData(rs.getString(1));
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Invalid External Code!!!");
                txtExternalCode.requestFocus();
            }
            rs.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int funGeneratePlaceOrderTextReport()
    {
        File textFile = null;
        try
        {
            String dtPODate = (dtePODate.getDate().getDate()) + "-" + (dtePODate.getDate().getMonth() + 1) + "-" + (dtePODate.getDate().getYear() + 1900);
            String dtDelDate = (dteDeliveryDate.getDate().getDate()) + "-" + (dteDeliveryDate.getDate().getMonth() + 1) + "-" + (dteDeliveryDate.getDate().getYear() + 1900);

            clsUtility objUtil = (clsUtility) objUtility.clone();
            objUtil.funCreateTempFolder();
            String filPath = System.getProperty("user.dir");
            textFile = new File(filPath + "/Temp/PurchaseOrderDetails.txt");
            PrintWriter pw = new PrintWriter(textFile);

            double finalTotalQty = 0, totalAmt = 0,taxAmt=0;
            pw.println();
            pw.println(objUtil.funPrintTextWithAlignment("Purchase Order", 40, "Center"));
            pw.println(objUtil.funPrintTextWithAlignment("POS Name    :" + clsGlobalVarClass.gPOSName, 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("PO Code  :" + txtPOCode.getText(), 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("PO Date  :" + dtPODate, 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("Delivery Date  :" + dtDelDate, 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("Supplier :" + txtSupplierName.getText().trim(), 40, "Left"));

            pw.println("----------------------------------------");
            pw.print(objUtil.funPrintTextWithAlignment("ItemName", 24, "Left"));
            pw.print(objUtil.funPrintTextWithAlignment("Qty", 8, "Right"));
            pw.print(objUtil.funPrintTextWithAlignment("Amt", 8, "Right"));
            pw.println();
            pw.println("----------------------------------------");
            pw.println();
            for (Map.Entry<String, clsPurchaseOrderDtl> entry : hmPurchaseOrderItemDtl.entrySet())
            {
                pw.print(objUtil.funPrintTextWithAlignment(entry.getValue().getItemName(), 24, "Left"));
                pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(entry.getValue().getOrderQty()), 8, "Right"));
                pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(entry.getValue().getAmount()), 8, "Right"));
                pw.println();
                finalTotalQty += entry.getValue().getOrderQty();
                totalAmt += entry.getValue().getAmount();
            }
            taxAmt=Double.parseDouble(txtTaxAmt.getText());
            totalAmt+=taxAmt;
            pw.println("----------------------------------------");
            pw.print(objUtil.funPrintTextWithAlignment("Tax ", 24, "Left"));
            pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(""), 8, "Right"));
            pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(taxAmt), 8, "Right"));
            pw.println();
            
            pw.print(objUtil.funPrintTextWithAlignment("Total ", 24, "Left"));
            pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(finalTotalQty), 8, "Right"));
            pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(totalAmt), 8, "Right"));
            pw.println();
            pw.println("----------------------------------------");

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

            funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, textFile.getAbsolutePath());
            funShowTextFile(textFile, "Placed Order List Report");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    private void funPrintReportToPrinter(String printerName, String fileName)
    {
        try
        {

            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                int printerIndex = 0;
                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                for (int i = 0; i < printService.length; i++)
                {

                    if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName()))
                    {
                        printerIndex = i;
                        break;
                    }
                }
                DocPrintJob job = printService[printerIndex].createPrintJob();
                FileInputStream fis = new FileInputStream(fileName);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
            }
            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            BufferedReader KOTIn = new BufferedReader(fread);

            String line = "";
            while ((line = KOTIn.readLine()) != null)
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        lblProductName1 = new javax.swing.JLabel();
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
        };  ;
        panelbody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        lblPOCode = new javax.swing.JLabel();
        scrollPaneItems = new javax.swing.JScrollPane();
        tblItemDetails = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblItemName = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        btnResetItemDtlGrid = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblSupplierName = new javax.swing.JLabel();
        txtSupplierName = new javax.swing.JTextField();
        lblPODate = new javax.swing.JLabel();
        dtePODate = new com.toedter.calendar.JDateChooser();
        txtPOCode = new javax.swing.JTextField();
        txtOrderQty = new javax.swing.JTextField();
        lblOrderQty = new javax.swing.JLabel();
        lblPurchaseRate = new javax.swing.JLabel();
        txtPurRate = new javax.swing.JTextField();
        lblItemCode = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtTaxAmt = new javax.swing.JTextField();
        lblTaxAmt = new javax.swing.JLabel();
        lblGrandTotal = new javax.swing.JLabel();
        txtGrandTotal = new javax.swing.JTextField();
        lblStockQty = new javax.swing.JLabel();
        lblStkQty = new javax.swing.JLabel();
        lblExtCode = new javax.swing.JLabel();
        txtExternalCode = new javax.swing.JTextField();
        lblDeliveryDate = new javax.swing.JLabel();
        dteDeliveryDate = new com.toedter.calendar.JDateChooser();
        chkClosePO = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName1.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName1.setText("SPOS - ");
        panelHeader.add(lblProductName1);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("Purchase Order");
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
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelbody.setBackground(new java.awt.Color(255, 255, 255));
        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setOpaque(false);
        panelbody.setPreferredSize(new java.awt.Dimension(800, 570));
        panelbody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Purchase Order");
        panelbody.add(lblFormName, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 180, 30));

        txtSuppCode.setEditable(false);
        txtSuppCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSuppCodeMouseClicked(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });
        panelbody.add(txtSuppCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 90, 30));

        lblPOCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOCode.setText("PO Code :");
        panelbody.add(lblPOCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 30));

        scrollPaneItems.setBackground(new java.awt.Color(255, 255, 255));

        tblItemDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Qty", "Rate", "Amt", "Sub Group Name", "Group Name", "Select", "Item Code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemDetails.setRowHeight(25);
        tblItemDetails.setRowMargin(2);
        tblItemDetails.getTableHeader().setReorderingAllowed(false);
        tblItemDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemDetailsMouseClicked(evt);
            }
        });
        scrollPaneItems.setViewportView(tblItemDetails);
        if (tblItemDetails.getColumnModel().getColumnCount() > 0) {
            tblItemDetails.getColumnModel().getColumn(0).setMinWidth(200);
            tblItemDetails.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblItemDetails.getColumnModel().getColumn(0).setMaxWidth(200);
            tblItemDetails.getColumnModel().getColumn(1).setMinWidth(50);
            tblItemDetails.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblItemDetails.getColumnModel().getColumn(1).setMaxWidth(50);
            tblItemDetails.getColumnModel().getColumn(2).setMinWidth(60);
            tblItemDetails.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblItemDetails.getColumnModel().getColumn(2).setMaxWidth(60);
            tblItemDetails.getColumnModel().getColumn(3).setMinWidth(80);
            tblItemDetails.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblItemDetails.getColumnModel().getColumn(3).setMaxWidth(80);
            tblItemDetails.getColumnModel().getColumn(4).setMinWidth(180);
            tblItemDetails.getColumnModel().getColumn(4).setPreferredWidth(180);
            tblItemDetails.getColumnModel().getColumn(4).setMaxWidth(180);
            tblItemDetails.getColumnModel().getColumn(5).setMinWidth(180);
            tblItemDetails.getColumnModel().getColumn(5).setPreferredWidth(180);
            tblItemDetails.getColumnModel().getColumn(5).setMaxWidth(180);
            tblItemDetails.getColumnModel().getColumn(6).setMinWidth(45);
            tblItemDetails.getColumnModel().getColumn(6).setPreferredWidth(45);
            tblItemDetails.getColumnModel().getColumn(6).setMaxWidth(45);
            tblItemDetails.getColumnModel().getColumn(7).setMinWidth(10);
            tblItemDetails.getColumnModel().getColumn(7).setPreferredWidth(10);
            tblItemDetails.getColumnModel().getColumn(7).setMaxWidth(10);
        }

        panelbody.add(scrollPaneItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 800, 320));

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Modifier Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNewKeyPressed(evt);
            }
        });
        panelbody.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 90, 40));

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        panelbody.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 520, 90, 40));

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Modifier Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        panelbody.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 520, 90, 40));

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panelbody.add(lblItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 270, 30));

        txtItemCode.setEditable(false);
        txtItemCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtItemCodeMouseClicked(evt);
            }
        });
        txtItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemCodeActionPerformed(evt);
            }
        });
        panelbody.add(txtItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 90, 30));

        btnResetItemDtlGrid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnResetItemDtlGrid.setForeground(new java.awt.Color(255, 255, 255));
        btnResetItemDtlGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnResetItemDtlGrid.setText("RESET");
        btnResetItemDtlGrid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetItemDtlGrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetItemDtlGridMouseClicked(evt);
            }
        });
        btnResetItemDtlGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetItemDtlGridActionPerformed(evt);
            }
        });
        panelbody.add(btnResetItemDtlGrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 70, 30));

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        panelbody.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 60, 30));

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        panelbody.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 80, 30));

        lblSupplierName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSupplierName.setText("Supplier  :");
        panelbody.add(lblSupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 60, 30));

        txtSupplierName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSupplierNameMouseClicked(evt);
            }
        });
        txtSupplierName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierNameActionPerformed(evt);
            }
        });
        panelbody.add(txtSupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 240, 30));

        lblPODate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPODate.setText("PO Date :");
        panelbody.add(lblPODate, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 40, 60, 30));

        dtePODate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtePODateKeyPressed(evt);
            }
        });
        panelbody.add(dtePODate, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 130, 30));

        txtPOCode.setEditable(false);
        txtPOCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPOCodeMouseClicked(evt);
            }
        });
        txtPOCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOCodeKeyPressed(evt);
            }
        });
        panelbody.add(txtPOCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 100, 30));

        txtOrderQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOrderQty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtOrderQtyMouseClicked(evt);
            }
        });
        txtOrderQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderQtyKeyPressed(evt);
            }
        });
        panelbody.add(txtOrderQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 60, 30));

        lblOrderQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderQty.setText("Order Qty :");
        panelbody.add(lblOrderQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 70, 30));

        lblPurchaseRate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPurchaseRate.setText("Purchase Rate :");
        panelbody.add(lblPurchaseRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 90, 30));

        txtPurRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPurRate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPurRateMouseClicked(evt);
            }
        });
        txtPurRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPurRateKeyPressed(evt);
            }
        });
        panelbody.add(txtPurRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 100, 30));

        lblItemCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemCode.setText("Item :");
        panelbody.add(lblItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 40, 30));

        lblSubTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubTotal.setText("Sub Total");
        panelbody.add(lblSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 490, 70, 20));

        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setText("0.00");
        panelbody.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 490, 90, 30));

        txtTaxAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTaxAmt.setText("0.00");
        panelbody.add(txtTaxAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 90, 30));

        lblTaxAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxAmt.setText("Tax");
        panelbody.add(lblTaxAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 490, 70, 20));

        lblGrandTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGrandTotal.setText("Grand Total");
        panelbody.add(lblGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 490, 70, 30));

        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGrandTotal.setText("0.00");
        panelbody.add(txtGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 490, 110, 30));

        lblStockQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockQty.setText("Stock Qty");
        panelbody.add(lblStockQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 60, 30));

        lblStkQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelbody.add(lblStkQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 80, 30));

        lblExtCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExtCode.setText("Ext Code :");
        panelbody.add(lblExtCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, 60, 30));

        txtExternalCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExternalCodeKeyPressed(evt);
            }
        });
        panelbody.add(txtExternalCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 120, 30));

        lblDeliveryDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryDate.setText("Del Dt :");
        panelbody.add(lblDeliveryDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 80, 50, 30));

        dteDeliveryDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteDeliveryDateKeyPressed(evt);
            }
        });
        panelbody.add(dteDeliveryDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 130, 30));

        chkClosePO.setText("  CLOSE PO");
        panelbody.add(chkClosePO, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 80, -1));

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void txtSuppCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSuppCodeMouseClicked
        // TODO add your handling code here:
        funOpenSupplierMasterSearch();
    }//GEN-LAST:event_txtSuppCodeMouseClicked


    private void tblItemDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemDetailsMouseClicked

    }//GEN-LAST:event_tblItemDetailsMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Item Modifier");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenSupplierMasterSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtOrderQty.requestFocus();
        }
    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funValidateAndSave();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Purchase Order");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Purchase Order");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Purchase Order");
    }//GEN-LAST:event_formWindowClosing

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == 10)
        {
            funValidateAndSave();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void txtItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemCodeActionPerformed
        // TODO add your handling code here:
        funOpenMenuItemSearch();
    }//GEN-LAST:event_txtItemCodeActionPerformed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        try
        {
            funAddRow();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        // TODO add your handling code here:
        if (tblItemDetails.getSelectedRow() > -1)
        {
            try
            {
                funRemoveRow();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select item from grid to delete");
        }
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
        // TODO add your handling code here:
        funOpenMenuItemSearch();
    }//GEN-LAST:event_txtItemCodeMouseClicked

    private void txtSupplierNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupplierNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierNameMouseClicked

    private void txtSupplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierNameActionPerformed

    private void dtePODateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtePODateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtePODateKeyPressed

    private void txtPOCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPOCodeMouseClicked
        // TODO add your handling code here:
        funOpenPOSearch();
    }//GEN-LAST:event_txtPOCodeMouseClicked

    private void txtPOCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPOCodeKeyPressed

    private void txtOrderQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOrderQtyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderQtyMouseClicked

    private void txtOrderQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderQtyKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderQtyKeyPressed

    private void txtPurRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPurRateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurRateMouseClicked

    private void txtPurRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPurRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurRateKeyPressed

    private void txtExternalCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExternalCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funExternalCodeEntered();
        }
    }//GEN-LAST:event_txtExternalCodeKeyPressed

    private void btnResetItemDtlGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetItemDtlGridActionPerformed
        funResetDetailFields();
    }//GEN-LAST:event_btnResetItemDtlGridActionPerformed

    private void btnResetItemDtlGridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetItemDtlGridMouseClicked
        funResetDetailFields();
    }//GEN-LAST:event_btnResetItemDtlGridMouseClicked

    private void dteDeliveryDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteDeliveryDateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dteDeliveryDateKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetItemDtlGrid;
    private javax.swing.JCheckBox chkClosePO;
    private com.toedter.calendar.JDateChooser dteDeliveryDate;
    private com.toedter.calendar.JDateChooser dtePODate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryDate;
    private javax.swing.JLabel lblExtCode;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGrandTotal;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemCode;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOrderQty;
    private javax.swing.JLabel lblPOCode;
    private javax.swing.JLabel lblPODate;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName1;
    private javax.swing.JLabel lblPurchaseRate;
    private javax.swing.JLabel lblStkQty;
    private javax.swing.JLabel lblStockQty;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JLabel lblTaxAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JScrollPane scrollPaneItems;
    private javax.swing.JTable tblItemDetails;
    private javax.swing.JTextField txtExternalCode;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtOrderQty;
    private javax.swing.JTextField txtPOCode;
    private javax.swing.JTextField txtPurRate;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtTaxAmt;
    // End of variables declaration//GEN-END:variables
}
