/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import com.POSGlobal.controller.clsUtility;

public class frmLoyaltyPointMaster extends javax.swing.JFrame
{
    private String sql;
    DefaultTableModel dmMenuHead;
    DefaultTableModel dmPOS;
    DefaultTableModel dmGroupSubGroup;
    DefaultTableModel dmCustType;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmLoyaltiPoints
     */
    public frmLoyaltyPointMaster()
    {
        try
        {
            initComponents();
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            dteToDate.setDate(clsGlobalVarClass.funGetCalenderToDate(1));
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);

            dmMenuHead = (DefaultTableModel) tblMenuHead.getModel();
            dmPOS = (DefaultTableModel) tblPOS.getModel();
            dmGroupSubGroup = (DefaultTableModel) tblGroupSubGroup.getModel();
            dmCustType = (DefaultTableModel) tblCustType.getModel();

            funLoadMenuHead();
            funLoadPOS();
            funLoadGroupSubGroup();
            funLoadCustomerType();
            funSetShortCutKeys();
        }
        catch (ParseException ex)
        {
            Logger.getLogger(frmLoyaltyPointMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to load menu heads
     */
    private void funLoadMenuHead()
    {
        try
        {
            String sql = "select strMenuCode,strMenuName from tblmenuhd";
            ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsMenuHead.next())
            {
                Object row[] =
                {
                    rsMenuHead.getString(1), rsMenuHead.getString(2), false
                };
                dmMenuHead.addRow(row);
            }
            tblMenuHead.setModel(dmMenuHead);
            rsMenuHead.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to pos
     */
    private void funLoadPOS()
    {
        try
        {
            String sql = "select strPosCode,strPosName from tblposmaster";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                Object row[] =
                {
                    rsPOS.getString(1), rsPOS.getString(2), false
                };
                dmPOS.addRow(row);
            }
            tblPOS.setModel(dmPOS);
            rsPOS.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load subgroup
     */
    private void funLoadGroupSubGroup()
    {
        try
        {
            String sql = "select strSubGroupCode,strSubGroupName from tblsubgrouphd ";
            ResultSet rsGrpSubGrp = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsGrpSubGrp.next())
            {
                Object row[] =
                {
                    rsGrpSubGrp.getString(1), rsGrpSubGrp.getString(2), false
                };
                dmGroupSubGroup.addRow(row);
            }
            tblGroupSubGroup.setModel(dmGroupSubGroup);
            rsGrpSubGrp.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load customer type
     */
    private void funLoadCustomerType()
    {
        try
        {
            String sql = "select strCustTypeCode,strCustType from tblcustomertypemaster";
            ResultSet rsCustType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsCustType.next())
            {
                Object row[] =
                {
                    rsCustType.getString(1), rsCustType.getString(2), false
                };
                dmCustType.addRow(row);
            }
            tblCustType.setModel(dmCustType);
            rsCustType.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get loyalty code
     *
     * @return
     */
    private long funGetLoyaltyCode()
    {
        long loyaltyCode = 1;
        try
        {
            String sql = "select dblLastNo from tblinternal where strTransactionType='LoyaltyCode'";
            ResultSet rsLoyaltyCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsLoyaltyCode.next())
            {
                loyaltyCode = rsLoyaltyCode.getLong(1);
                long updateCounter = loyaltyCode + 1;
                String updateSql = "update tblinternal set dblLastNo=" + updateCounter + " "
                    + "where strTransactionType='LoyaltyCode'";
                clsGlobalVarClass.dbMysql.execute(updateSql);
            }
            else
            {
                String insertSql = "insert into tblinternal(strTransactionType,dblLastNo) values('LoyaltyCode',1)";
                loyaltyCode = 1;
                clsGlobalVarClass.dbMysql.execute(insertSql);
            }
            rsLoyaltyCode.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return loyaltyCode + 1;
    }

    /**
     * This method is used to update loyalty code
     *
     * @param LoyaltyCode
     * @return int
     */
    private int funUpdateLoyaltyCode(long LoyaltyCode)
    {
        int res = 0;
        try
        {
            String updateSql = "update tblinternal set dblLastNo=" + LoyaltyCode + " "
                + "where strTransactionType='LoyaltyCode'";
            res = clsGlobalVarClass.dbMysql.execute(updateSql);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * This method is used to save loyalty code
     */
    private void funSaveLoyaltyCode()
    {
        try
        {
            long lastNo = funGetLoyaltyCode();
            Date dtFromDate = dteFromDate.getDate();
            String dateFrom = (dtFromDate.getYear() + 1900) + "-" + (dtFromDate.getMonth() + 1) + "-" + (dtFromDate.getDate());
            Date dtToDate = dteToDate.getDate();
            String dateTo = (dtToDate.getYear() + 1900) + "-" + (dtToDate.getMonth() + 1) + "-" + (dtToDate.getDate());
            String loyaltyCode = "LC" + String.format("%03d", lastNo);
            String insertSql = "insert into tblloyaltypoints (strLoyaltyCode,dblAmount,dblLoyaltyPoints"
                + ",dblLoyaltyPoints1,dblLoyaltyValue,strUserCreated,strUserEdited,dteDateCreated"
                + ",dteDateEdited,strClientCode,strDataPostFlag,dteFromDate,dteToDate)"
                + "VALUES('" + loyaltyCode + "','" + txtAmount.getText() + "','" + txtPoints.getText() + "','" + txtPoints1.getText() + "'"
                + ",'" + txtValue.getText() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                + ",'" + clsGlobalVarClass.gClientCode + "','N','" + dateFrom + "','" + dateTo + "')";

            clsGlobalVarClass.dbMysql.execute(insertSql);
            funSavePOS(loyaltyCode);
            funSaveMenuHead(loyaltyCode);
            funSaveGroupSubGroup(loyaltyCode);
            funSaveCustomerType(loyaltyCode);
            funUpdateLoyaltyCode(lastNo);
            new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
            funResetField();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save pos
     *
     * @param loyaltyCode
     */
    private void funSavePOS(String loyaltyCode) throws Exception
    {
        String insertSql="";
        boolean select = false, flgCheck = false;
        funGetLoyaltyCode();        
        String deleteSql = "delete from tblloyaltypointposdtl where strLoyaltyCode='" + loyaltyCode + "' ";
        clsGlobalVarClass.dbMysql.execute(deleteSql);
        insertSql = "insert into tblloyaltypointposdtl (strLoyaltyCode,strPOSCode,strClientCode,strDataPostFlag) values";
        for (int i = 0; i < tblPOS.getRowCount(); i++)
        {
            select = Boolean.parseBoolean(tblPOS.getValueAt(i, 2).toString());
            if (select)
            {
                flgCheck = true;
                insertSql += "('" + loyaltyCode + "','" + tblPOS.getValueAt(i, 0).toString() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','N'),";
            }
        }
        if (flgCheck)
        {
            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            clsGlobalVarClass.dbMysql.execute(insertSql);
        }
    }

    /**
     * This method is used to save menu head
     *
     * @param loyaltyCode
     */
    private void funSaveMenuHead(String loyaltyCode) throws Exception
    {
        String insertSql;
        boolean select = false, flgCheck = false;
        String deleteSql = "delete from tblloyaltypointmenuhddtl where strLoyaltyCode='" + loyaltyCode + "'";
        clsGlobalVarClass.dbMysql.execute(deleteSql);
        insertSql = "insert into tblloyaltypointmenuhddtl (strLoyaltyCode,strMenuCode,strClientCode,strDataPostFlag) values";
        for (int i = 0; i < tblMenuHead.getRowCount(); i++)
        {
            select = Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString());
            if (select)
            {
                flgCheck = true;
                insertSql += "('" + loyaltyCode + "','" + tblMenuHead.getValueAt(i, 0).toString() + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "','N'),";
            }
        }
        if (flgCheck)
        {
            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            clsGlobalVarClass.dbMysql.execute(insertSql);
        }

    }

    /**
     * This method is used to sane subgroup
     *
     * @param loyaltyCode
     */
    private void funSaveGroupSubGroup(String loyaltyCode) throws Exception
    {
        String insertSql="";
        boolean select = false, flgCheck = false;
        String deleteSql = "delete from tblloyaltypointsubgroupdtl where strLoyaltyCode='" + loyaltyCode + "'";
        clsGlobalVarClass.dbMysql.execute(deleteSql);
        insertSql = "insert into tblloyaltypointsubgroupdtl (strLoyaltyCode,strCode,strClientCode,strDataPostFlag) values";
        for (int i = 0; i < tblGroupSubGroup.getRowCount(); i++)
        {
            select = Boolean.parseBoolean(tblGroupSubGroup.getValueAt(i, 2).toString());
            if (select)
            {
                flgCheck = true;
                insertSql += "('" + loyaltyCode + "','" + tblGroupSubGroup.getValueAt(i, 0).toString() + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "','N'),";
            }
        }
        if (flgCheck)
        {
            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            clsGlobalVarClass.dbMysql.execute(insertSql);
        }
    }

    /**
     * This method is used to save customer type
     *
     * @param loyaltyCode
     */
    private void funSaveCustomerType(String loyaltyCode) throws Exception
    {
        String insertSql;
        boolean select = false, flgCheck = false;
        String deleteSql = "delete from tblloyaltypointcustomerdtl where strLoyaltyCode='" + loyaltyCode + "'";
        clsGlobalVarClass.dbMysql.execute(deleteSql);
        insertSql = "insert into tblloyaltypointcustomerdtl (strLoyaltyCode,strCustomerTypeCode,strClientCode,strDataPostFlag) values";
        for (int i = 0; i < tblCustType.getRowCount(); i++)
        {
            select = Boolean.parseBoolean(tblCustType.getValueAt(i, 2).toString());
            if (select)
            {
                flgCheck = true;
                insertSql += "('" + loyaltyCode + "','" + tblCustType.getValueAt(i, 0).toString() + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "','N'),";
            }
        }
        if (flgCheck)
        {
            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            clsGlobalVarClass.dbMysql.execute(insertSql);
        }
    }


    /**
     * This method is used to update LP
     */
    private void funUpdateButton()
    {
        try
        {
            String loyaltyCode = txtLoyaltyCode.getText().toString();
            Date dtFromDate = dteFromDate.getDate();
            String dateFrom = (dtFromDate.getYear() + 1900) + "-" + (dtFromDate.getMonth() + 1) + "-" + (dtFromDate.getDate());
            Date dtToDate = dteToDate.getDate();
            String dateTo = (dtToDate.getYear() + 1900) + "-" + (dtToDate.getMonth() + 1) + "-" + (dtToDate.getDate());
            String updateQuery = "update tblloyaltypoints SET dblAmount='" + txtAmount.getText() + "',"
                + "dblLoyaltyPoints='" + txtPoints.getText() + "',dblLoyaltyPoints1='" + txtPoints1.getText() + "'"
                + ",dblLoyaltyValue='" + txtValue.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',dteFromDate='" + dateFrom + "',dteToDate='" + dateTo + "'"
                + "where strLoyaltyCode='" + loyaltyCode + "' ";

            int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
            if (exc > 0)
            {
                funSavePOS(loyaltyCode);
                funSaveMenuHead(loyaltyCode);
                funSaveGroupSubGroup(loyaltyCode);
                funSaveCustomerType(loyaltyCode);
                new frmOkPopUp(null, "Updated Successfully", "Successful", 4).setVisible(true);
                funResetField();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
        btnSave.setMnemonic('s');
        txtLoyaltyCode.setText("");
        txtAmount.setText("");
        txtPoints1.setText("");
        txtPoints.setText("");
        txtValue.setText("");
        btnSave.setText("Save");
        txtLoyaltyCode.requestFocus();
        dmCustType.setRowCount(0);
        dmGroupSubGroup.setRowCount(0);
        dmMenuHead.setRowCount(0);
        dmPOS.setRowCount(0);

        funLoadMenuHead();
        funLoadPOS();
        funLoadGroupSubGroup();
        funLoadCustomerType();
    }

    /**
     * This method is used to check select customer type
     */
    private void funCheckSelectCustType()
    {
        for (int i = 0; i < tblCustType.getRowCount(); i++)
        {
            if (Boolean.parseBoolean(tblCustType.getValueAt(i, 2).toString()) == true)
            {
                break;
            }
        }
    }

    /**
     * This method is used to check selected subgroup
     */
    private void funCheckSelectGroupSubGroup()
    {
        for (int i = 0; i < tblGroupSubGroup.getRowCount(); i++)
        {
            if (Boolean.parseBoolean(tblGroupSubGroup.getValueAt(i, 2).toString()) == true)
            {
                break;
            }
        }
    }

    /**
     * This method is used to check selected pos
     */
    private void funCheckSelectPOS()
    {

        for (int i = 0; i < tblPOS.getRowCount(); i++)
        {
            if (Boolean.parseBoolean(tblPOS.getValueAt(i, 2).toString()) == true)
            {
                break;
            }
        }
    }

    /**
     * This method is used to check selected menu head
     */
    private void funCheckSelectMenuHead()
    {
        for (int i = 0; i < tblMenuHead.getRowCount(); i++)
        {
            if (Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString()) == true)
            {
                break;
            }
        }
    }

    /**
     * This method is used to check selected loyalty code
     */
    private void funSelectLoyaltyCode()
    {
        clsUtility obj=new clsUtility();
        obj.funCallForSearchForm("LoyaltyMaster");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnSave.setText("UPDATE");
            btnSave.setMnemonic('u');
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetLoyaltyData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }

    /**
     * This method is used to set loyalty data
     *
     * @param data
     */
    private void funSetLoyaltyData(Object[] data)
    {
        try
        {
            txtLoyaltyCode.setText(data[0].toString());
            txtAmount.setText(data[1].toString());
            txtPoints.setText(data[2].toString());
            txtPoints1.setText(data[3].toString());
            txtValue.setText(data[4].toString());
            funFillDtlPOSTable(data[0].toString());
            funFillDtlMenuHeadTable(data[0].toString());
            funFillDtlGrpSubGrpTable(data[0].toString());
            funFillDtlCustTypeTable(data[0].toString());
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
            sql = "select dteFromDate,dteToDate from tblloyaltypoints "
                + "where strLoyaltyCode='" + data[0].toString() + "'";
            ResultSet rsTemp = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTemp.next())
            {
                Date dt = dtFormat.parse(rsTemp.getString(1));
                dteFromDate.setDate(dt);
                dt = dtFormat.parse(rsTemp.getString(2));
                dteToDate.setDate(dt);
            }
        }
        catch (Exception ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }
    }

    /**
     * This method is used to fill pos table
     *
     * @param loayaltyCode
     */
    private void funFillDtlPOSTable(String loayaltyCode) throws Exception
    {
        boolean select = false;
        Vector vPOS = new Vector();
        String sqlDtl = "select a.strPOSCode from tblposmaster a ,tblloyaltypointposdtl b "
            + "where a.strposCode=b.strposCode and b.strLoyaltyCode='" + loayaltyCode + "'";

        ResultSet rsFillPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
        while (rsFillPOS.next())
        {
            vPOS.add(rsFillPOS.getString(1));
        }
        rsFillPOS.close();
        DefaultTableModel dmPOS = (DefaultTableModel) tblPOS.getModel();
        dmPOS.setRowCount(0);
        String sqlPOS = "select strposCode,strposName from tblposmaster";

        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
        while (rsPOS.next())
        {
            select = false;
            for (int i = 0; i < vPOS.size(); i++)
            {
                if (rsPOS.getString(1).equals(vPOS.elementAt(i).toString()))
                {
                    select = true;
                    break;
                }
            }
            Object row[] =
            {
                rsPOS.getString(1), rsPOS.getString(2), select
            };
            dmPOS.addRow(row);
        }
        rsPOS.close();
        tblPOS.setModel(dmPOS);
    }

    /**
     * This method is used to fill menu head table
     *
     * @param loayaltyCode
     */
    private void funFillDtlMenuHeadTable(String loayaltyCode) throws Exception
    {
        boolean select = false;
        Vector vMenuHead = new Vector();
        String sqlDtl = "select a.strMenuCode from tblmenuhd a ,tblloyaltypointmenuhddtl b "
                + "where a.strMenuCode=b.strMenuCode and b.strLoyaltyCode='" + loayaltyCode + "'";

        ResultSet rsFillMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
        while (rsFillMenuHead.next())
        {
            vMenuHead.add(rsFillMenuHead.getString(1));
        }
        rsFillMenuHead.close();
        DefaultTableModel dmMenuHead = (DefaultTableModel) tblMenuHead.getModel();
        dmMenuHead.setRowCount(0);
        String sqlMenuHead = "select strMenuCode,strMenuName from tblmenuhd";
        ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sqlMenuHead);
        while (rsMenuHead.next())
        {
            select = false;
            for (int i = 0; i < vMenuHead.size(); i++)
            {
                if (rsMenuHead.getString(1).equals(vMenuHead.elementAt(i).toString()))
                {
                    select = true;
                    break;
                }
            }
            Object row[] =
            {
                rsMenuHead.getString(1), rsMenuHead.getString(2), select
            };
            dmMenuHead.addRow(row);
        }
        rsMenuHead.close();
        tblMenuHead.setModel(dmMenuHead);
    }

    /**
     * This method is used to fill sub group table
     *
     * @param loayaltyCode
     */
    private void funFillDtlGrpSubGrpTable(String loayaltyCode) throws Exception
    {
        boolean select = false;
        Vector vSubGroup = new Vector();
        String sqlDtl = "select a.strSubGroupCode from tblsubgrouphd a ,tblloyaltypointsubgroupdtl b "
                + "where a.strSubGroupCode=b.strCode and b.strLoyaltyCode='" + loayaltyCode + "'";

        ResultSet rsFillSubGroup = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
        while (rsFillSubGroup.next())
        {
            vSubGroup.add(rsFillSubGroup.getString(1));
        }
        rsFillSubGroup.close();
        DefaultTableModel dmSubGroup = (DefaultTableModel) tblGroupSubGroup.getModel();
        dmSubGroup.setRowCount(0);
        String sqlSubGroup = "select strSubGroupCode,strSubGroupName from tblsubgrouphd";
        ResultSet rsSubGroup = clsGlobalVarClass.dbMysql.executeResultSet(sqlSubGroup);
        while (rsSubGroup.next())
        {
            select = false;
            for (int i = 0; i < vSubGroup.size(); i++)
            {
                if (rsSubGroup.getString(1).equals(vSubGroup.elementAt(i).toString()))
                {
                    select = true;
                    break;
                }
            }
            Object row[] =
            {
                rsSubGroup.getString(1), rsSubGroup.getString(2), select
            };
            dmSubGroup.addRow(row);
        }
        rsSubGroup.close();
        tblGroupSubGroup.setModel(dmSubGroup);
    }

    /**
     * This method is used to fill customer type table
     *
     * @param loayaltyCode
     */
    private void funFillDtlCustTypeTable(String loayaltyCode) throws Exception
    {
        boolean select = false;
        Vector vCustType = new Vector();
        String sqlDtl = "select a.strCustTypeCode from tblcustomertypemaster a ,tblloyaltypointcustomerdtl b "
                + "where a.strCustTypeCode=b.strCustomerTypeCode and b.strLoyaltyCode='" + loayaltyCode + "'";
        ResultSet rsFillCustType = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
        while (rsFillCustType.next())
        {
            vCustType.add(rsFillCustType.getString(1));
        }
        rsFillCustType.close();
        DefaultTableModel dmCustType = (DefaultTableModel) tblCustType.getModel();
        dmCustType.setRowCount(0);
        String sqlCustType = "select strCustTypeCode,strCustType from tblcustomertypemaster";
        ResultSet rsCustType = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustType);
        while (rsCustType.next())
        {
            select = false;
            for (int i = 0; i < vCustType.size(); i++)
            {
                if (rsCustType.getString(1).equals(vCustType.elementAt(i).toString()))
                {
                    select = true;
                    break;
                }
            }
            Object row[] =
            {
                rsCustType.getString(1), rsCustType.getString(2), select
            };
            dmCustType.addRow(row);
        }
        rsCustType.close();
        tblCustType.setModel(dmCustType);
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

        paneHeader = new javax.swing.JPanel();
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
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblLoyaltyCode = new javax.swing.JLabel();
        txtLoyaltyCode = new javax.swing.JTextField();
        txtAmount = new javax.swing.JTextField();
        lblAmount = new javax.swing.JLabel();
        lblPoint1 = new javax.swing.JLabel();
        txtPoints1 = new javax.swing.JTextField();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblFromDate = new javax.swing.JLabel();
        txtPoints = new javax.swing.JTextField();
        txtValue = new javax.swing.JTextField();
        dteToDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        lblValue = new javax.swing.JLabel();
        lblPoints = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        tabMenuHead = new javax.swing.JScrollPane();
        tblMenuHead = new javax.swing.JTable();
        tabGroupSubGroup = new javax.swing.JScrollPane();
        tblGroupSubGroup = new javax.swing.JTable();
        tabCustType = new javax.swing.JScrollPane();
        tblCustType = new javax.swing.JTable();
        tabPOS = new javax.swing.JScrollPane();
        tblPOS = new javax.swing.JTable();

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

        paneHeader.setBackground(new java.awt.Color(69, 164, 238));
        paneHeader.setLayout(new javax.swing.BoxLayout(paneHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        paneHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        paneHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Loyalty Point");
        paneHeader.add(lblformName);
        paneHeader.add(filler4);
        paneHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        paneHeader.add(lblPosName);
        paneHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        paneHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        paneHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        paneHeader.add(lblHOSign);

        getContentPane().add(paneHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Loyalty Points Setup");

        lblLoyaltyCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLoyaltyCode.setText("Loyalty Code");

        txtLoyaltyCode.setEditable(false);
        txtLoyaltyCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtLoyaltyCodeMouseClicked(evt);
            }
        });
        txtLoyaltyCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtLoyaltyCodeKeyPressed(evt);
            }
        });

        txtAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAmountMouseClicked(evt);
            }
        });
        txtAmount.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAmountKeyPressed(evt);
            }
        });

        lblAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAmount.setText("Amount");

        lblPoint1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPoint1.setText("Points");

        txtPoints1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPoints1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPoints1MouseClicked(evt);
            }
        });
        txtPoints1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPoints1KeyPressed(evt);
            }
        });

        dteFromDate.setPreferredSize(new java.awt.Dimension(100, 25));
        dteFromDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteFromDateKeyPressed(evt);
            }
        });

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date ");

        txtPoints.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPoints.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPointsMouseClicked(evt);
            }
        });
        txtPoints.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPointsActionPerformed(evt);
            }
        });
        txtPoints.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPointsKeyPressed(evt);
            }
        });

        txtValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValue.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtValueMouseClicked(evt);
            }
        });
        txtValue.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtValueActionPerformed(evt);
            }
        });
        txtValue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtValueKeyPressed(evt);
            }
        });

        dteToDate.setPreferredSize(new java.awt.Dimension(100, 25));
        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date ");

        lblValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblValue.setText("Value");

        lblPoints.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPoints.setText("Points");

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setToolTipText("Save Loyalti Point");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSaveKeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("Reset");
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

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setToolTipText("Close Loyalti Point");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
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

        tblMenuHead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Menu Head Code", "Menu Head Name", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tblMenuHead.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblMenuHeadMouseClicked(evt);
            }
        });
        tabMenuHead.setViewportView(tblMenuHead);

        tabPane.addTab("Menu Head", tabMenuHead);

        tblGroupSubGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "GroupSubGroup Code", "GroupSubGroup Name", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tblGroupSubGroup.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblGroupSubGroupMouseClicked(evt);
            }
        });
        tabGroupSubGroup.setViewportView(tblGroupSubGroup);

        tabPane.addTab("GroupSubGroup", tabGroupSubGroup);

        tblCustType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Customer Code", "Customer Type", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tblCustType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCustTypeMouseClicked(evt);
            }
        });
        tabCustType.setViewportView(tblCustType);

        tabPane.addTab("Customer Type", tabCustType);

        tblPOS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS Code", "POS Name", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tblPOS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPOSMouseClicked(evt);
            }
        });
        tabPOS.setViewportView(tblPOS);

        tabPane.addTab("POS", tabPOS);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)))
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFromDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPoint1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPoints1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(lblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblLoyaltyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtLoyaltyCode))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(167, 167, 167)
                        .addComponent(lblPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(240, 240, 240)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoyaltyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLoyaltyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPoint1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPoints1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblToDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblPOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOSMouseClicked
        funCheckSelectPOS();
    }//GEN-LAST:event_tblPOSMouseClicked

    private void tblCustTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustTypeMouseClicked
        funCheckSelectCustType();
    }//GEN-LAST:event_tblCustTypeMouseClicked

    private void tblGroupSubGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupSubGroupMouseClicked
        funCheckSelectGroupSubGroup();
    }//GEN-LAST:event_tblGroupSubGroupMouseClicked

    private void tblMenuHeadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuHeadMouseClicked
        funCheckSelectMenuHead();
    }//GEN-LAST:event_tblMenuHeadMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("LoyaltyPoints");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked
    
    
    
    private void funSaveAndUpdateOperations()
    {
        clsUtility obj=new clsUtility();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dteFromDate.getDate();
        Date date2 = dteToDate.getDate();
        long diff = date2.getTime() - date1.getTime();
        if (diff < 0)
        {
            new frmOkPopUp(this, "Invalid To Date", "Error", 1).setVisible(true);
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("Save"))
        {
            if (!obj.funCheckDouble(txtValue.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Value");
                txtValue.requestFocus();
                return;
            }
            if (!obj.funCheckDouble(txtPoints.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Loyalty Point");
                txtPoints.requestFocus();
                return;
            }
            if (!obj.funCheckDouble(txtPoints1.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Amount");
                txtPoints1.requestFocus();
                return;
            }
            funSaveLoyaltyCode();
            txtValue.setText("");
            txtPoints1.setText("");
            txtPoints.setText("");
        }
        else
        {
            if (!obj.funCheckDouble(txtValue.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Value");
                txtValue.requestFocus();
                return;
            }
            if (!obj.funCheckDouble(txtPoints.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Loyalty Point");
                txtPoints.requestFocus();
                return;
            }
            if (!obj.funCheckDouble(txtPoints1.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Amount");
                txtPoints1.requestFocus();
                return;
            }
            funUpdateButton();
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        funSaveAndUpdateOperations();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValueActionPerformed

    private void txtValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtValueMouseClicked
        // TODO add your handling code here:
        if (txtValue.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Value").setVisible(true);
            txtValue.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtValue.getText(),"Double", "Enter Value").setVisible(true);
            txtValue.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtValueMouseClicked

    private void txtPointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPointsActionPerformed
        // TODO add your handling code here:
        if (txtPoints.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Points").setVisible(true);
            txtPoints.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtPoints.getText(),"Double", "Enter Points" ).setVisible(true);
            txtPoints.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPointsActionPerformed

    private void txtPoints1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPoints1MouseClicked
        // TODO add your handling code here:
        if (txtPoints1.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Points").setVisible(true);
            txtPoints1.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtPoints1.getText(),"Double", "Enter Points").setVisible(true);
            txtPoints1.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPoints1MouseClicked

    private void txtAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAmountMouseClicked
        //        
        try
        {
            if (txtAmount.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, "","Double", "Please Enter Amount.").setVisible(true);
                txtAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this, true, txtAmount.getText(), "Double", "Please Enter Amount.").setVisible(true);
                txtAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }//GEN-LAST:event_txtAmountMouseClicked

    private void txtLoyaltyCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLoyaltyCodeMouseClicked
        // TODO add your handling code here:
        
        funSelectLoyaltyCode();
    }//GEN-LAST:event_txtLoyaltyCodeMouseClicked

    private void txtLoyaltyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLoyaltyCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectLoyaltyCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtAmount.requestFocus();
        }
    }//GEN-LAST:event_txtLoyaltyCodeKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtPoints.requestFocus();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtPointsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPointsKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtPoints1.requestFocus();
        }
    }//GEN-LAST:event_txtPointsKeyPressed

    private void txtPoints1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPoints1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtValue.requestFocus();
        }
    }//GEN-LAST:event_txtPoints1KeyPressed

    private void txtValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValueKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteFromDate.requestFocus();
        }
    }//GEN-LAST:event_txtValueKeyPressed

    private void dteFromDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteFromDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteToDate.requestFocus();
        }
    }//GEN-LAST:event_dteFromDateKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteToDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSave.requestFocus();
        }

    }//GEN-LAST:event_dteToDateKeyPressed

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveAndUpdateOperations();
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("LoyaltyPoints");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtAmountActionPerformed
    {//GEN-HEADEREND:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtPointsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPointsMouseClicked
    {//GEN-HEADEREND:event_txtPointsMouseClicked
        if (txtPoints.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "","Double", "Please Enter Points.").setVisible(true);
            txtPoints.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtPoints.getText(), "Double", "Please Enter Points.").setVisible(true);
            txtPoints.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPointsMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("LoyaltyPoints");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("LoyaltyPoints");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblLoyaltyCode;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPoint1;
    private javax.swing.JLabel lblPoints;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblValue;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel paneHeader;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane tabCustType;
    private javax.swing.JScrollPane tabGroupSubGroup;
    private javax.swing.JScrollPane tabMenuHead;
    private javax.swing.JScrollPane tabPOS;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblCustType;
    private javax.swing.JTable tblGroupSubGroup;
    private javax.swing.JTable tblMenuHead;
    private javax.swing.JTable tblPOS;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtLoyaltyCode;
    private javax.swing.JTextField txtPoints;
    private javax.swing.JTextField txtPoints1;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
