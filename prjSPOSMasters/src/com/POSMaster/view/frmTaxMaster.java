/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmTaxMaster extends javax.swing.JFrame
{

    private boolean amtFlag, perFlag = true;
    private String taxCode, sql, taxOnTaxCode;
    ResultSet countSet1, countSet;
    private String strCode, code, validFromDate, validToDate, taxRounded, taxOnTax;
    private String[] posCodes, settCode;
    private int i, rowCount;
    private int sc1;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmTaxMaster
     */
    public frmTaxMaster()
    {
        initComponents();
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

            rowCount = 0;
            clsGlobalVarClass.gUserCode = clsGlobalVarClass.gUserCode;
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            lblPosName.setText(clsGlobalVarClass.gPOSName);
            txtTaxCode.requestFocus();

            DefaultTableModel dm = (DefaultTableModel) tblTaxSettlementMode.getModel();
            sql = "select count(strSettelmentCode) from tblsettelmenthd";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int setCnt = rs.getInt(1);
            settCode = new String[setCnt];
            sql = "select strSettelmentCode,strSettelmentDesc from tblsettelmenthd";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            i = 0;
            while (rs.next())
            {
                settCode[i] = rs.getString(1);
                Object[] ob =
                {
                    rs.getString(1), rs.getString(2), false
                };
                dm.addRow(ob);
                rowCount++;
                i++;
            }
            tblTaxSettlementMode.setModel(dm);
            rs.close();

            //function to fill all groups
            funFillTaxOnGroupTable();

            java.util.Date dt = new java.util.Date();
            int day = dt.getDate();
            int month = dt.getMonth() + 1;
            int year = dt.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            dteValidFrom.setDate(date);
            year += 100;
            dte = day + "-" + month + "-" + year;
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            dteValidTo.setDate(date);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            sql = "select count(strPosCode) from tblposmaster";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cnt = rs.getInt(1);
            rs.close();
            posCodes = new String[cnt];
            i = 0;
            DefaultTableModel dmTable = (DefaultTableModel) tblPOSCode.getModel();
            sql = "select strPosCode,strPosName from tblposmaster";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                posCodes[i] = rs.getString(1);
                Object[] ob =
                {
                    rs.getString(2), false
                };
                dmTable.addRow(ob);
                i++;
            }
            tblPOSCode.setModel(dmTable);

            DefaultTableModel dmArea = (DefaultTableModel) tblAreaMaster.getModel();
            sql = "select strAreaCode,strAreaName from tblareamaster";
            ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsArea.next())
            {
                Object[] arrArea =
                {
                    rsArea.getString(1), rsArea.getString(2), false
                };
                dmArea.addRow(arrArea);
            }
            tblAreaMaster.setModel(dmArea);
            rsArea.close();

            if (cmbTaxIndicator.getSelectedItem().toString().trim().length() > 0)
            {
                cmbTaxOnItemType.setEnabled(true);
            }
            else
            {
                cmbTaxOnItemType.setEnabled(false);
            }

            DefaultTableModel dmTaxOnTax = (DefaultTableModel) tblTaxOnTax.getModel();
            //sql = "select strTaxCode,strTaxDesc from tbltaxhd where strTaxOnTax='No'";
            sql = "select strTaxCode,strTaxDesc from tbltaxhd";
            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTax.next())
            {
                Object[] arrTax =
                {
                    rsTax.getString(1), rsTax.getString(2), false
                };
                dmTaxOnTax.addRow(arrTax);
            }
            tblTaxOnTax.setModel(dmTaxOnTax);
            rsTax.close();
            funSetShortCutKeys();
            txtTaxCode.requestFocus();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetTaxMasterData(Object[] data)
    {
        try
        {
            txtTaxCode.setText(data[0].toString());
            txtTaxDesc.setText(data[1].toString());
            cmbTaxOnSP.setSelectedItem(data[2].toString());
            cmbTaxType.setSelectedItem(data[3].toString());
            txtPercent.setText(data[4].toString());
            txtTaxAmount.setText(data[5].toString());
            cmbTaxGD.setSelectedItem(data[8].toString());
            cmbTaxCal.setSelectedItem(data[9].toString());
            cmbTaxIndicator.setSelectedItem(data[10].toString());

            if (data[11].toString().equals("true"))
            {
                chkTaxRounded.setEnabled(true);
            }
            if (data[12].toString().equals("Yes"))
            {
                chkTaxOnTax.setSelected(true);

            }
            else
            {
                chkTaxOnTax.setSelected(false);
            }
            StringBuilder sb = new StringBuilder(data[6].toString());
            int ind = sb.indexOf(" ");
            String dt = sb.substring(0, ind);
            String[] dts = new String[3];
            StringTokenizer stk = new StringTokenizer(dt, "-");
            int j = 0;
            while (stk.hasMoreTokens())
            {
                dts[j] = stk.nextToken();
                j++;
            }
            String date1 = dts[2] + "-" + dts[1] + "-" + dts[0];
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
            dteValidFrom.setDate(date);

            sb = new StringBuilder(data[7].toString());
            ind = sb.indexOf(" ");
            dt = sb.substring(0, ind);
            dts = new String[3];
            stk = new StringTokenizer(dt, "-");
            j = 0;
            while (stk.hasMoreTokens())
            {
                dts[j] = stk.nextToken();
                j++;
            }
            String date2 = dts[2] + "-" + dts[1] + "-" + dts[0];
            date = new SimpleDateFormat("dd-MM-yyyy").parse(date2);
            dteValidTo.setDate(date);

            sql = "select strOperationType,strItemType,strAccountCode,strTaxShortName,strBillNote "
                    + " from tbltaxhd where strTaxCode='" + txtTaxCode.getText() + "'";
            clsGlobalVarClass.dbMysql.open("mysql");
            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTax.next())
            {
                String[] spOperation = rsTax.getString(1).split(",");
                for (int cnt = 0; cnt < spOperation.length; cnt++)
                {
                    if (spOperation[cnt].equals("HomeDelivery"))
                    {
                        chkHomeDelivery.setSelected(true);
                    }
                    else if (spOperation[cnt].equals("DineIn"))
                    {
                        chkDineIn.setSelected(true);
                    }
                    else if (spOperation[cnt].equals("TakeAway"))
                    {
                        chkTakeAway.setSelected(true);
                    }
                }
                cmbTaxOnItemType.setSelectedItem(rsTax.getString(2));
                txtAccountCode.setText(rsTax.getString(3));
                txtTaxShortName.setText(rsTax.getString(4));
                txtBillNote.setText(rsTax.getString(5));
            }
            rsTax.close();

            funFillSettlementTable();
            funFillTaxOnGroupTable(txtTaxCode.getText());
            funFillPosTable();
            funFillAreaMasterTable();
            funFillTaxOnTaxTable();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to fill area master
     */
    private void funFillAreaMasterTable()
    {
        try
        {
            sql = "select strAreaCode from tbltaxhd where strTaxCode='" + txtTaxCode.getText() + "'";
            clsGlobalVarClass.dbMysql.open("mysql");
            ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsArea.next())
            {
                String[] spArea = rsArea.getString(1).split(",");
                for (int cnt = 0; cnt < spArea.length; cnt++)
                {
                    for (int cnt1 = 0; cnt1 < tblAreaMaster.getRowCount(); cnt1++)
                    {
                        if (tblAreaMaster.getValueAt(cnt1, 0).toString().equals(spArea[cnt]))
                        {
                            tblAreaMaster.setValueAt(true, cnt1, 2);
                        }
                    }
                }
            }
            rsArea.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to fill pos table
     */
    private void funFillPosTable()
    {
        try
        {
            sql = "select * from tbltaxposdtl where strTaxCode='" + txtTaxCode.getText() + "'";
            clsGlobalVarClass.dbMysql.open("mysql");
            DefaultTableModel dm = (DefaultTableModel) tblPOSCode.getModel();
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                for (int k = 0; k < tblPOSCode.getRowCount(); k++)
                {
                    if (rs.getString(2).equals(posCodes[k]))
                    {
                        tblPOSCode.setValueAt(true, k, 1);
                    }
                }
            }
            tblPOSCode.setModel(dm);
            rs.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to fill settlement table
     */
    private void funFillSettlementTable()
    {
        try
        {
            sql = "select * from tblsettlementtax where strTaxCode='" + txtTaxCode.getText() + "'";
            clsGlobalVarClass.dbMysql.open("mysql");
            DefaultTableModel dm = (DefaultTableModel) tblTaxSettlementMode.getModel();
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                for (int k = 0; k < tblTaxSettlementMode.getRowCount(); k++)
                {
                    if (rs.getString(4).equals("true") && rs.getString(2).equals(tblTaxSettlementMode.getValueAt(k, 0)))
                    {
                        tblTaxSettlementMode.setValueAt(true, k, 2);
                    }
                }
            }
            tblTaxSettlementMode.setModel(dm);
            rs.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to fill tax on tax table
     */
    private void funFillTaxOnTaxTable()
    {
        try
        {
            sql = "select strTaxOnTaxCode from tbltaxhd where strTaxCode='" + txtTaxCode.getText() + "'";
            clsGlobalVarClass.dbMysql.open("mysql");
            DefaultTableModel dmTax = (DefaultTableModel) tblTaxOnTax.getModel();
            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTax.next())
            {
                String[] spTax = rsTax.getString(1).split(",");
                for (int cnt = 0; cnt < spTax.length; cnt++)
                {
                    for (int cnt1 = 0; cnt1 < tblTaxOnTax.getRowCount(); cnt1++)
                    {
                        if (tblTaxOnTax.getValueAt(cnt1, 0).toString().equals(spTax[cnt]))
                        {
                            tblTaxOnTax.setValueAt(true, cnt1, 2);
                        }
                    }
                }
            }
            rsTax.close();

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
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtTaxCode.setText("");
            txtTaxDesc.setText("");
            txtTaxAmount.setText("0.00");
            txtPercent.setText("0.00");
            txtAccountCode.setText("");
            txtTaxShortName.setText("");
            chkTaxRounded.setEnabled(false);
            cmbTaxCal.setSelectedItem("Backword");
            cmbTaxGD.setSelectedItem("Gross");
            chkTaxOnTax.setSelected(false);
            cmbTaxType.setSelectedItem("Percent");
            cmbTaxOnSP.setSelectedItem("Sale");
            cmbTaxIndicator.setSelectedItem(" ");
            chkDineIn.setSelected(false);
            chkHomeDelivery.setSelected(false);
            chkTakeAway.setSelected(false);
            cmbTaxOnItemType.setSelectedIndex(0);
            txtBillNote.setText("");
            for (int i = 0; i < rowCount; i++)
            {
                tblTaxSettlementMode.setValueAt(false, i, 2);
            }

            for (int i = 0; i < tblPOSCode.getRowCount(); i++)
            {
                tblPOSCode.setValueAt(false, i, 1);
            }

            for (int i = 0; i < tblAreaMaster.getRowCount(); i++)
            {
                tblAreaMaster.setValueAt(false, i, 2);
            }

            for (int i = 0; i < tblTaxOnTax.getRowCount(); i++)
            {
                tblTaxOnTax.setValueAt(false, i, 2);
            }

            funFillTaxOnGroupTable();
            
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funTaxMasterOperations()
    {
        try
        {
            clsUtility obj = new clsUtility();
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                //btnNew.setText("Save");//Update
                sql = "select count(*) from tbltaxhd";
                countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                countSet1.next();
                int cn = countSet1.getInt(1);
                countSet1.close();
                if (cn > 0)
                {
                    sql = "select max(strTaxCode) from tbltaxhd";
                    countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    countSet.next();
                    code = countSet.getString(1);
                    StringBuilder sb = new StringBuilder(code);
                    String ss = sb.delete(0, 1).toString();
                    for (int i = 0; i < ss.length(); i++)
                    {
                        if (ss.charAt(i) != '0')
                        {
                            strCode = ss.substring(i, ss.length());
                            break;
                        }
                    }
                    int intCode = Integer.parseInt(strCode);
                    intCode++;
                    if (intCode < 10)
                    {
                        taxCode = "T0" + intCode;
                    }
                    else
                    {
                        taxCode = "T" + intCode;
                    }
                }
                else
                {
                    taxCode = "T01";
                }
                java.util.Date objDate = dteValidFrom.getDate();
                validFromDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

                objDate = dteValidTo.getDate();
                validToDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

                Date dt1 = dteValidFrom.getDate();
                Date dt2 = dteValidTo.getDate();
                if ((dt2.getTime() - dt1.getTime()) < 0)
                {
                    new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
                }
                else
                {
                    if (txtTaxDesc.getText().length() == 0)
                    {
                        new frmOkPopUp(this, "Please enter tax description", "Error", 0).setVisible(true);
                        txtTaxDesc.requestFocus();
                    }
                    else if (!obj.funCheckLength(txtTaxDesc.getText(), 30))
                    {
                        new frmOkPopUp(this, "Tax Description length must be less than 30", "Error", 0).setVisible(true);
                        txtTaxDesc.requestFocus();
                    }
                    else
                    {
                        if (chkTaxRounded.isEnabled())
                        {
                            taxRounded = "true";
                        }
                        else
                        {
                            taxRounded = "false";
                        }
                        if (chkTaxOnTax.isSelected())
                        {
                            taxOnTaxCode = "";
                            taxOnTax = "Yes";
                            for (int cnt = 0; cnt < tblTaxOnTax.getRowCount(); cnt++)
                            {
                                if (Boolean.parseBoolean(tblTaxOnTax.getValueAt(cnt, 2).toString()))
                                {
                                    taxOnTaxCode += "," + tblTaxOnTax.getValueAt(cnt, 0);
                                }
                            }
                            StringBuilder sb1 = new StringBuilder(taxOnTaxCode);
                            taxOnTaxCode = sb1.delete(0, 1).toString();
                        }
                        else
                        {
                            taxOnTax = "No";
                        }
                        boolean posFlag = false;
                        for (int k = 0; k < tblPOSCode.getRowCount(); k++)
                        {
                            boolean select = Boolean.parseBoolean(tblPOSCode.getValueAt(k, 1).toString());
                            if (select == true)
                            {
                                posFlag = true;
                                break;
                            }
                        }
                        boolean setFlag = false;
                        for (int k = 0; k < tblTaxSettlementMode.getRowCount(); k++)
                        {
                            boolean applicable = Boolean.parseBoolean(tblTaxSettlementMode.getValueAt(k, 2).toString());
                            if (applicable == true)
                            {
                                setFlag = true;
                                break;
                            }
                        }
                        if (posFlag == false)
                        {
                            new frmOkPopUp(this, "Please select atleast POS Code", "Error", 1).setVisible(true);
                            return;
                        }
                        if (setFlag == false)
                        {
                            new frmOkPopUp(this, "Please select atleast one Settlement code", "Error", 1).setVisible(true);
                            return;
                        }
                        if (txtTaxShortName.getText().length() > 20)
                        {
                            new frmOkPopUp(this, "Please Enter min 20 characters only", "Error", 1).setVisible(true);
                            return;
                        }
                        txtTaxCode.setText(taxCode);
                        String areaCode = "", operationType = "", itemType = "";
                        for (int cnt = 0; cnt < tblAreaMaster.getRowCount(); cnt++)
                        {
                            if (Boolean.parseBoolean(tblAreaMaster.getValueAt(cnt, 2).toString()))
                            {
                                areaCode += "," + tblAreaMaster.getValueAt(cnt, 0).toString();
                            }
                        }
                        if (chkDineIn.isSelected())
                        {
                            operationType += ",DineIn";
                        }
                        if (chkHomeDelivery.isSelected())
                        {
                            operationType += ",HomeDelivery";
                        }
                        if (chkTakeAway.isSelected())
                        {
                            operationType += ",TakeAway";
                        }
                        itemType = cmbTaxOnItemType.getSelectedItem().toString();

                        StringBuilder sb = new StringBuilder(areaCode);
                        areaCode = sb.delete(0, 1).toString();

                        sb = new StringBuilder(operationType);
                        operationType = sb.delete(0, 1).toString();

                        sql = "insert into tbltaxhd (strTaxCode,strTaxDesc,strTaxOnSP,strTaxType,dblPercent,"
                                + "dblAmount,dteValidFrom,dteValidTo,strTaxOnGD,strTaxCalculation,strTaxIndicator,"
                                + "strTaxRounded,strTaxOnTax,strTaxOnTaxCode,strUserCreated,strUserEdited,"
                                + "dteDateCreated,dteDateEdited,strAreaCode,strOperationType,strItemType,strClientCode"
                                + ",strAccountCode,strTaxShortName,strBillNote)"
                                + " values('" + txtTaxCode.getText() + "','" + txtTaxDesc.getText()
                                + "','" + cmbTaxOnSP.getSelectedItem().toString() + "','" + cmbTaxType.getSelectedItem().toString()
                                + "','" + txtPercent.getText() + "','" + txtTaxAmount.getText() + "','" + validFromDate + "','"
                                + validToDate + "','" + cmbTaxGD.getSelectedItem().toString()
                                + "','" + cmbTaxCal.getSelectedItem().toString() + "','"
                                + cmbTaxIndicator.getSelectedItem().toString() + "','" + taxRounded + "','"
                                + taxOnTax + "','" + taxOnTaxCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                                + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                                + ",'" + areaCode + "','" + operationType + "','" + itemType + "','" + clsGlobalVarClass.gClientCode + "'"
                                + ",'" + txtAccountCode.getText() + "','" + txtTaxShortName.getText().trim() + "','"+txtBillNote.getText().trim()+"')";
                        System.out.println(sql);
                        int exc = clsGlobalVarClass.dbMysql.execute(sql);

                        if (exc > 0)
                        {
                            int posRowNo = tblPOSCode.getRowCount();
                            //System.out.println(posRowNo);
                            for (int k = 0; k < posRowNo; k++)
                            {
                                boolean select = Boolean.parseBoolean(tblPOSCode.getValueAt(k, 1).toString());
                                String psName = tblPOSCode.getValueAt(k, 0).toString();
                                if (select == true)
                                {
                                    sql = "insert into tbltaxposdtl values('" + txtTaxCode.getText() + "','"
                                            + posCodes[k] + "','" + txtTaxDesc.getText() + "','"+clsGlobalVarClass.gClientCode+"')";
                                    clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            }
                            int rNo = tblTaxSettlementMode.getRowCount();
                            for (int k = 0; k < rNo; k++)
                            {
                                boolean applicable = Boolean.parseBoolean(tblTaxSettlementMode.getValueAt(k, 2).toString());
                                if (applicable == true)
                                {
                                    String settlementCode = tblTaxSettlementMode.getValueAt(k, 0).toString();
                                    String settlementType = tblTaxSettlementMode.getValueAt(k, 1).toString();
                                    sql = "insert into tblsettlementtax values('" + txtTaxCode.getText() + "','"
                                            + settlementCode + "','" + settlementType + "','" + applicable + "','"
                                            + validFromDate + "','" + validToDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                                            + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"+clsGlobalVarClass.gClientCode+"')";
                                    int sc1 = clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            }

                            funSaveTaxOnGroupData(txtTaxCode.getText());

                            sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                    + " where strTableName='Tax' ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                            new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                            funResetField();
                        }
                    }
                }
            }
            else
            {
                java.util.Date objDate = dteValidFrom.getDate();
                validFromDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

                objDate = dteValidTo.getDate();
                validToDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

                System.out.println("valid from date=" + dteValidFrom.toString() + "    valid to date=" + dteValidTo);

                if (chkTaxRounded.isEnabled())
                {
                    taxRounded = "true";
                }
                else
                {
                    taxRounded = "false";
                }
                if (chkTaxOnTax.isSelected())
                {
                    taxOnTaxCode = "";
                    taxOnTax = "Yes";
                    for (int cnt = 0; cnt < tblTaxOnTax.getRowCount(); cnt++)
                    {
                        if (Boolean.parseBoolean(tblTaxOnTax.getValueAt(cnt, 2).toString()))
                        {
                            taxOnTaxCode += "," + tblTaxOnTax.getValueAt(cnt, 0);
                        }
                    }
                    StringBuilder sb1 = new StringBuilder(taxOnTaxCode);
                    taxOnTaxCode = sb1.delete(0, 1).toString();
                }
                else
                {
                    taxOnTax = "No";
                }

                if (txtTaxShortName.getText().length() > 20)
                {
                    new frmOkPopUp(this, "Please Enter min 20 characters only", "Error", 1).setVisible(true);
                    return;
                }

                Date dt1 = dteValidFrom.getDate();
                Date dt2 = dteValidTo.getDate();
                if ((dt2.getTime() - dt1.getTime()) < 0)
                {
                    new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
                }
                else if (!obj.funCheckLength(txtTaxDesc.getText(), 30))
                {
                    new frmOkPopUp(this, "Tax Description length must be less than 30", "Error", 0).setVisible(true);
                    txtTaxDesc.requestFocus();
                }
                else
                {
                    String areaCode = "", operationType = "", itemType = "";
                    for (int cnt = 0; cnt < tblAreaMaster.getRowCount(); cnt++)
                    {
                        if (Boolean.parseBoolean(tblAreaMaster.getValueAt(cnt, 2).toString()))
                        {
                            areaCode += "," + tblAreaMaster.getValueAt(cnt, 0).toString();
                        }
                    }
                    if (chkDineIn.isSelected())
                    {
                        operationType += ",DineIn";
                    }
                    if (chkHomeDelivery.isSelected())
                    {
                        operationType += ",HomeDelivery";
                    }
                    if (chkTakeAway.isSelected())
                    {
                        operationType += ",TakeAway";
                    }
                    itemType = cmbTaxOnItemType.getSelectedItem().toString();

                    StringBuilder sb = new StringBuilder(areaCode);
                    areaCode = sb.delete(0, 1).toString();

                    sb = new StringBuilder(operationType);
                    operationType = sb.delete(0, 1).toString();

                    sql = "UPDATE tbltaxhd SET strTaxDesc = '" + txtTaxDesc.getText() + "',strTaxOnSP='"
                            + cmbTaxOnSP.getSelectedItem().toString() + "',strTaxType='" + cmbTaxType.getSelectedItem().toString()
                            + "',dblPercent='" + txtPercent.getText() + "',dblAmount='" + txtTaxAmount.getText() + "',dteValidFrom='"
                            + validFromDate + "',dteValidTo='" + validToDate + "',strTaxOnGD='" + cmbTaxGD.getSelectedItem().toString()
                            + "',strTaxCalculation='" + cmbTaxCal.getSelectedItem().toString() + "',strTaxIndicator='"
                            + cmbTaxIndicator.getSelectedItem().toString() + "',strTaxRounded='" + taxRounded
                            + "',strTaxOnTax='" + taxOnTax + "',strTaxOnTaxCode='" + taxOnTaxCode + "',strUserEdited='"
                            + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",strAreaCode='" + areaCode + "',strOperationType='" + operationType + "'"
                            + ",strItemType='" + itemType + "',strDataPostFlag='N',strAccountCode='" + txtAccountCode.getText() + "'"
                            + ",strTaxShortName='" + txtTaxShortName.getText().trim() + "' "
                            + ",strBillNote='"+txtBillNote.getText().trim()+"'"
                            + " WHERE strTaxCode ='" + txtTaxCode.getText() + "'";
                    //System.out.println(sql);
                    int exc = clsGlobalVarClass.dbMysql.execute(sql);
                    if (exc > 0)
                    {
                        int rNo = tblTaxSettlementMode.getRowCount();
                        for (int k = 0; k < rNo; k++)
                        {
                            String settlementCode = tblTaxSettlementMode.getValueAt(k, 0).toString();
                            String settlementName = tblTaxSettlementMode.getValueAt(k, 1).toString();
                            sql = "select count(*) from tblsettlementtax where strSettlementCode='" + settlementCode + "' and strTaxCode='" + txtTaxCode.getText() + "'";
                            ResultSet rrs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            rrs.next();
                            if (rrs.getInt(1) == 0)
                            {
                                boolean applicable = Boolean.parseBoolean(tblTaxSettlementMode.getValueAt(k, 2).toString());
                                settlementCode = tblTaxSettlementMode.getValueAt(k, 0).toString();
                                //String settlementType = tblTaxSettlementMode.getValueAt(k, 1).toString();
                                sql = "insert into tblsettlementtax values('" + txtTaxCode.getText()
                                        + "','" + settlementCode + "','" + settlementName + "','" + applicable + "','"
                                        + validFromDate + "','" + validToDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                                        + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"+clsGlobalVarClass.gClientCode+"')";
                                sc1 = clsGlobalVarClass.dbMysql.execute(sql);
                            }
                            else
                            {
                                //System.out.println("Up New");
                                boolean applicable = Boolean.parseBoolean(tblTaxSettlementMode.getValueAt(k, 2).toString());
                                sql = "update tblsettlementtax "
                                        + "set strApplicable='" + applicable + "' "
                                        + ",strSettlementName='" + settlementName + "'"
                                        + ",dteFrom='" + validFromDate + "'"
                                        + ",dteTo='" + validToDate
                                        + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                        + ",strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                                        + "where strSettlementCode='" + settlementCode + "' "
                                        + "and strTaxCode='" + txtTaxCode.getText() + "' ";
                                //System.out.println(sql);
                                sc1 = clsGlobalVarClass.dbMysql.execute(sql);
                            }
                        }

                        rNo = tblPOSCode.getRowCount();
                        for (int k = 0; k < rNo; k++)
                        {
                            String psname = tblPOSCode.getValueAt(k, 0).toString();
                            sql = "select count(*) from tbltaxposdtl where strPOSCode='" + posCodes[k] + "' and strTaxCode='" + txtTaxCode.getText() + "'";
                            ResultSet rrs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            rrs.next();
                            if (rrs.getInt(1) == 0)
                            {
                                boolean applicable = Boolean.parseBoolean(tblPOSCode.getValueAt(k, 1).toString());
                                if (applicable == true)
                                {
                                    sql = "insert into tbltaxposdtl values('" + txtTaxCode.getText() + "','" + posCodes[k] + "','" + txtTaxDesc.getText() + "','"+clsGlobalVarClass.gClientCode+"')";
                                    sc1 = clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            }
                            else
                            {
                                //System.out.println("Up New");
                                boolean applicable = Boolean.parseBoolean(tblPOSCode.getValueAt(k, 1).toString());
                                if (applicable == false)
                                {
                                    sql = "delete from tbltaxposdtl where strTaxCode='" + txtTaxCode.getText() + "' and strPOSCode='" + posCodes[k] + "'";
                                    sc1 = clsGlobalVarClass.dbMysql.execute(sql);
                                }

                            }
                        }
                        
                        
                        //update taxgeoup detail
                         funSaveTaxOnGroupData(txtTaxCode.getText());
                        
                        
                    }
                    if (sc1 > 0)
                    {

                        sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Tax' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        //clsGlobalVarClass.funInvokeHOWebserviceForMasters();
                        new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry"))
            {
                new frmOkPopUp(this, "Tax Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }

    /**
     * This method is used to set GL Code to GL Code field
     *
     * @param data
     */
    private void funSetAccountCode(Object[] data)
    {
        String accCode = (String) data[0];
        txtAccountCode.setText(accCode);

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
        }; ;
        panelBody = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        panelTabDtl1 = new javax.swing.JPanel();
        txtTaxCode = new javax.swing.JTextField();
        lblTaxCode = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbTaxType = new javax.swing.JComboBox();
        lblTaxType = new javax.swing.JLabel();
        lblTaxAmount = new javax.swing.JLabel();
        txtTaxAmount = new javax.swing.JTextField();
        cmbTaxOnSP = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtPercent = new javax.swing.JTextField();
        txtTaxDesc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPOSCode = new javax.swing.JTable();
        lblTaxShortName = new javax.swing.JLabel();
        txtTaxShortName = new javax.swing.JTextField();
        lblValidFrom1 = new javax.swing.JLabel();
        dteValidFrom = new com.toedter.calendar.JDateChooser();
        lblValidTo1 = new javax.swing.JLabel();
        dteValidTo = new com.toedter.calendar.JDateChooser();
        lblBillNote = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtBillNote = new javax.swing.JTextArea();
        panelTabDtl2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cmbTaxGD = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cmbTaxCal = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTaxSettlementMode = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbTaxIndicator = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        chkTaxRounded = new javax.swing.JCheckBox();
        chkTaxOnTax = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTaxOnGroupApplicable = new javax.swing.JTable();
        panelTabDtl3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblAreaMaster = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        chkHomeDelivery = new javax.swing.JCheckBox();
        chkTakeAway = new javax.swing.JCheckBox();
        chkDineIn = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbTaxOnItemType = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTaxOnTax = new javax.swing.JTable();
        panelLinkupTab = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblAccountCode = new javax.swing.JLabel();
        txtAccountCode = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Tax Master");
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

        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(254, 254, 254));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save TAX ");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
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

        tabPane.setBackground(new java.awt.Color(255, 255, 255));
        tabPane.setMinimumSize(new java.awt.Dimension(676, 576));

        panelTabDtl1.setBackground(new java.awt.Color(255, 255, 255));
        panelTabDtl1.setOpaque(false);
        panelTabDtl1.setLayout(null);

        txtTaxCode.setEditable(false);
        txtTaxCode.setBackground(new java.awt.Color(204, 204, 204));
        txtTaxCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxCodeMouseClicked(evt);
            }
        });
        txtTaxCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxCodeKeyPressed(evt);
            }
        });
        panelTabDtl1.add(txtTaxCode);
        txtTaxCode.setBounds(140, 10, 120, 30);

        lblTaxCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxCode.setText("Tax Code             :");
        panelTabDtl1.add(lblTaxCode);
        lblTaxCode.setBounds(10, 10, 140, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Tax On S/P          :");
        panelTabDtl1.add(jLabel1);
        jLabel1.setBounds(10, 160, 160, 30);

        cmbTaxType.setBackground(new java.awt.Color(216, 216, 216));
        cmbTaxType.setForeground(new java.awt.Color(255, 255, 255));
        cmbTaxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Percent", "Fixed Amount" }));
        cmbTaxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTaxTypeActionPerformed(evt);
            }
        });
        cmbTaxType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTaxTypeKeyPressed(evt);
            }
        });
        panelTabDtl1.add(cmbTaxType);
        cmbTaxType.setBounds(500, 10, 126, 30);

        lblTaxType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxType.setText("Tax Type        :");
        panelTabDtl1.add(lblTaxType);
        lblTaxType.setBounds(400, 10, 100, 30);

        lblTaxAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxAmount.setText("Amount          :");
        panelTabDtl1.add(lblTaxAmount);
        lblTaxAmount.setBounds(400, 110, 100, 30);

        txtTaxAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTaxAmount.setText("0.00");
        txtTaxAmount.setEnabled(false);
        txtTaxAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxAmountMouseClicked(evt);
            }
        });
        txtTaxAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxAmountKeyPressed(evt);
            }
        });
        panelTabDtl1.add(txtTaxAmount);
        txtTaxAmount.setBounds(500, 110, 130, 30);

        cmbTaxOnSP.setBackground(new java.awt.Color(216, 216, 216));
        cmbTaxOnSP.setForeground(new java.awt.Color(255, 255, 255));
        cmbTaxOnSP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sales", "Purchase" }));
        cmbTaxOnSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTaxOnSPKeyPressed(evt);
            }
        });
        panelTabDtl1.add(cmbTaxOnSP);
        cmbTaxOnSP.setBounds(140, 160, 126, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Percent          :");
        panelTabDtl1.add(jLabel2);
        jLabel2.setBounds(400, 160, 90, 30);

        txtPercent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPercent.setText("0.00");
        txtPercent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPercentMouseClicked(evt);
            }
        });
        txtPercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPercentKeyPressed(evt);
            }
        });
        panelTabDtl1.add(txtPercent);
        txtPercent.setBounds(500, 160, 130, 30);

        txtTaxDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxDescMouseClicked(evt);
            }
        });
        txtTaxDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxDescKeyPressed(evt);
            }
        });
        panelTabDtl1.add(txtTaxDesc);
        txtTaxDesc.setBounds(140, 60, 240, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Tax Description    :");
        panelTabDtl1.add(jLabel3);
        jLabel3.setBounds(10, 60, 120, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("POS Code            :");
        panelTabDtl1.add(jLabel4);
        jLabel4.setBounds(10, 200, 120, 40);

        tblPOSCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblPOSCode.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "POS Name", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPOSCode.setRowHeight(25);
        tblPOSCode.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblPOSCode.setSelectionForeground(new java.awt.Color(254, 254, 254));
        jScrollPane1.setViewportView(tblPOSCode);

        panelTabDtl1.add(jScrollPane1);
        jScrollPane1.setBounds(140, 200, 460, 130);

        lblTaxShortName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxShortName.setText("Tax Short Name   :");
        panelTabDtl1.add(lblTaxShortName);
        lblTaxShortName.setBounds(10, 110, 120, 30);

        txtTaxShortName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxShortNameMouseClicked(evt);
            }
        });
        txtTaxShortName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxShortNameKeyPressed(evt);
            }
        });
        panelTabDtl1.add(txtTaxShortName);
        txtTaxShortName.setBounds(140, 110, 240, 30);

        lblValidFrom1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblValidFrom1.setText("Valid From         :");
        panelTabDtl1.add(lblValidFrom1);
        lblValidFrom1.setBounds(10, 340, 130, 30);

        dteValidFrom.setBackground(new java.awt.Color(216, 216, 216));
        dteValidFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteValidFromKeyPressed(evt);
            }
        });
        panelTabDtl1.add(dteValidFrom);
        dteValidFrom.setBounds(140, 340, 139, 30);

        lblValidTo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblValidTo1.setText("Valid To              :");
        panelTabDtl1.add(lblValidTo1);
        lblValidTo1.setBounds(330, 340, 130, 30);

        dteValidTo.setBackground(new java.awt.Color(216, 216, 216));
        dteValidTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteValidToKeyPressed(evt);
            }
        });
        panelTabDtl1.add(dteValidTo);
        dteValidTo.setBounds(460, 340, 139, 30);

        lblBillNote.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillNote.setText("Bill Note            :");
        panelTabDtl1.add(lblBillNote);
        lblBillNote.setBounds(10, 380, 120, 30);

        txtBillNote.setColumns(20);
        txtBillNote.setRows(5);
        jScrollPane6.setViewportView(txtBillNote);

        panelTabDtl1.add(jScrollPane6);
        jScrollPane6.setBounds(140, 380, 320, 40);

        tabPane.addTab("Tax Details1", panelTabDtl1);

        panelTabDtl2.setOpaque(false);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setForeground(new java.awt.Color(254, 254, 254));
        jPanel6.setMinimumSize(new java.awt.Dimension(671, 548));
        jPanel6.setOpaque(false);
        jPanel6.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Tax on G/D         :");
        jPanel6.add(jLabel12);
        jLabel12.setBounds(10, 50, 140, 30);

        cmbTaxGD.setBackground(new java.awt.Color(216, 216, 216));
        cmbTaxGD.setForeground(new java.awt.Color(255, 255, 255));
        cmbTaxGD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gross", "Discount" }));
        cmbTaxGD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTaxGDKeyPressed(evt);
            }
        });
        jPanel6.add(cmbTaxGD);
        cmbTaxGD.setBounds(140, 50, 130, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Tax Calculation    :");
        jLabel13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel13KeyPressed(evt);
            }
        });
        jPanel6.add(jLabel13);
        jLabel13.setBounds(330, 50, 170, 30);

        cmbTaxCal.setBackground(new java.awt.Color(216, 216, 216));
        cmbTaxCal.setForeground(new java.awt.Color(255, 255, 255));
        cmbTaxCal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Backward", "Forward" }));
        cmbTaxCal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTaxCalKeyPressed(evt);
            }
        });
        jPanel6.add(cmbTaxCal);
        cmbTaxCal.setBounds(470, 50, 140, 30);

        tblTaxSettlementMode.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Set Code", "Settlement Name", "Applicable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTaxSettlementMode.setRowHeight(25);
        tblTaxSettlementMode.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblTaxSettlementMode.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblTaxSettlementMode.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblTaxSettlementMode);
        if (tblTaxSettlementMode.getColumnModel().getColumnCount() > 0) {
            tblTaxSettlementMode.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblTaxSettlementMode.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblTaxSettlementMode.getColumnModel().getColumn(2).setPreferredWidth(30);
        }

        jPanel6.add(jScrollPane2);
        jScrollPane2.setBounds(10, 230, 340, 190);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Tax Applicable On");
        jPanel6.add(jLabel14);
        jLabel14.setBounds(10, 200, 100, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Tax On Tax         :");
        jLabel15.setPreferredSize(new java.awt.Dimension(94, 27));
        jPanel6.add(jLabel15);
        jLabel15.setBounds(10, 150, 120, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Tax Indicator        :");
        jPanel6.add(jLabel16);
        jLabel16.setBounds(330, 110, 150, 30);

        cmbTaxIndicator.setBackground(new java.awt.Color(216, 216, 216));
        cmbTaxIndicator.setForeground(new java.awt.Color(255, 255, 255));
        cmbTaxIndicator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        cmbTaxIndicator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTaxIndicatorActionPerformed(evt);
            }
        });
        cmbTaxIndicator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTaxIndicatorKeyPressed(evt);
            }
        });
        jPanel6.add(cmbTaxIndicator);
        cmbTaxIndicator.setBounds(470, 110, 140, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Tax Rounded     :");
        jPanel6.add(jLabel17);
        jLabel17.setBounds(10, 100, 140, 27);

        chkTaxRounded.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkTaxRoundedKeyPressed(evt);
            }
        });
        jPanel6.add(chkTaxRounded);
        chkTaxRounded.setBounds(140, 100, 30, 30);

        chkTaxOnTax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkTaxOnTaxMouseClicked(evt);
            }
        });
        chkTaxOnTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTaxOnTaxActionPerformed(evt);
            }
        });
        chkTaxOnTax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkTaxOnTaxKeyPressed(evt);
            }
        });
        jPanel6.add(chkTaxOnTax);
        chkTaxOnTax.setBounds(140, 150, 30, 30);

        tblTaxOnGroupApplicable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Group Code", "Group Name", "Applicable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTaxOnGroupApplicable.setRowHeight(25);
        tblTaxOnGroupApplicable.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblTaxOnGroupApplicable.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblTaxOnGroupApplicable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblTaxOnGroupApplicable);
        if (tblTaxOnGroupApplicable.getColumnModel().getColumnCount() > 0) {
            tblTaxOnGroupApplicable.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblTaxOnGroupApplicable.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblTaxOnGroupApplicable.getColumnModel().getColumn(2).setPreferredWidth(30);
        }

        jPanel6.add(jScrollPane3);
        jScrollPane3.setBounds(360, 230, 360, 190);

        javax.swing.GroupLayout panelTabDtl2Layout = new javax.swing.GroupLayout(panelTabDtl2);
        panelTabDtl2.setLayout(panelTabDtl2Layout);
        panelTabDtl2Layout.setHorizontalGroup(
            panelTabDtl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        panelTabDtl2Layout.setVerticalGroup(
            panelTabDtl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 424, Short.MAX_VALUE)
        );

        tabPane.addTab("Tax Details2", panelTabDtl2);

        panelTabDtl3.setOpaque(false);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setForeground(new java.awt.Color(254, 254, 254));
        jPanel7.setMinimumSize(new java.awt.Dimension(671, 548));
        jPanel7.setOpaque(false);
        jPanel7.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Tax On Tax");
        jPanel7.add(jLabel6);
        jLabel6.setBounds(10, 20, 80, 20);

        tblAreaMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblAreaMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Area Code", "Area Name", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAreaMaster.setRowHeight(25);
        tblAreaMaster.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblAreaMaster.setSelectionForeground(new java.awt.Color(254, 254, 254));
        jScrollPane4.setViewportView(tblAreaMaster);

        jPanel7.add(jScrollPane4);
        jScrollPane4.setBounds(10, 260, 450, 130);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Select Area :");
        jPanel7.add(jLabel7);
        jLabel7.setBounds(10, 220, 90, 30);

        chkHomeDelivery.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkHomeDelivery.setText("Home Delivery");
        chkHomeDelivery.setOpaque(false);
        jPanel7.add(chkHomeDelivery);
        chkHomeDelivery.setBounds(90, 180, 110, 23);

        chkTakeAway.setText("Take Away");
        chkTakeAway.setOpaque(false);
        jPanel7.add(chkTakeAway);
        chkTakeAway.setBounds(330, 180, 80, 23);

        chkDineIn.setText("Dinning In");
        chkDineIn.setOpaque(false);
        jPanel7.add(chkDineIn);
        chkDineIn.setBounds(220, 180, 90, 23);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Tax On :");
        jPanel7.add(jLabel8);
        jLabel8.setBounds(20, 180, 70, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Tax On :");
        jPanel7.add(jLabel10);
        jLabel10.setBounds(430, 24, 60, 30);

        cmbTaxOnItemType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTaxOnItemType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Both", "Food", "Liquor" }));
        jPanel7.add(cmbTaxOnItemType);
        cmbTaxOnItemType.setBounds(490, 20, 120, 40);

        tblTaxOnTax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblTaxOnTax.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tax Code", "Tax Name", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTaxOnTax.setRowHeight(25);
        tblTaxOnTax.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblTaxOnTax.setSelectionForeground(new java.awt.Color(254, 254, 254));
        jScrollPane5.setViewportView(tblTaxOnTax);

        jPanel7.add(jScrollPane5);
        jScrollPane5.setBounds(10, 50, 420, 120);

        javax.swing.GroupLayout panelTabDtl3Layout = new javax.swing.GroupLayout(panelTabDtl3);
        panelTabDtl3.setLayout(panelTabDtl3Layout);
        panelTabDtl3Layout.setHorizontalGroup(
            panelTabDtl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        panelTabDtl3Layout.setVerticalGroup(
            panelTabDtl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabDtl3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabPane.addTab("Tax Details 3", panelTabDtl3);

        panelLinkupTab.setOpaque(false);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setForeground(new java.awt.Color(254, 254, 254));
        jPanel8.setMinimumSize(new java.awt.Dimension(671, 548));
        jPanel8.setOpaque(false);
        jPanel8.setLayout(null);

        lblAccountCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAccountCode.setText("Account Code     :");
        jPanel8.add(lblAccountCode);
        lblAccountCode.setBounds(210, 60, 130, 30);

        txtAccountCode.setEditable(false);
        txtAccountCode.setBackground(new java.awt.Color(204, 204, 204));
        txtAccountCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAccountCodeMouseClicked(evt);
            }
        });
        txtAccountCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountCodeActionPerformed(evt);
            }
        });
        txtAccountCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAccountCodeKeyPressed(evt);
            }
        });
        jPanel8.add(txtAccountCode);
        txtAccountCode.setBounds(360, 60, 140, 30);

        javax.swing.GroupLayout panelLinkupTabLayout = new javax.swing.GroupLayout(panelLinkupTab);
        panelLinkupTab.setLayout(panelLinkupTabLayout);
        panelLinkupTabLayout.setHorizontalGroup(
            panelLinkupTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        panelLinkupTabLayout.setVerticalGroup(
            panelLinkupTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLinkupTabLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        tabPane.addTab("Linkup", panelLinkupTab);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(254, 254, 254));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Close TAX Master");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(254, 254, 254));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
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

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Tax Master");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addGap(314, 314, 314)
                            .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(194, 194, 194))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 452, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        funTaxMasterOperations();
    }//GEN-LAST:event_btnNewMouseClicked

    private void cmbTaxIndicatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTaxIndicatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTaxIndicatorActionPerformed

    private void chkTaxOnTaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkTaxOnTaxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_chkTaxOnTaxMouseClicked

    private void chkTaxOnTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTaxOnTaxActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_chkTaxOnTaxActionPerformed

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Tax Master");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Tax Master");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void txtTaxDescMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxDescMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtTaxDesc.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Tax Description").setVisible(true);
                txtTaxDesc.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtTaxDesc.getText(), "1", "Enter Tax Description").setVisible(true);
                txtTaxDesc.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtTaxDescMouseClicked

    private void txtPercentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPercentMouseClicked
        // TODO add your handling code here:
        try
        {
            if (perFlag == true)
            {
                if (txtPercent.getText().length() == 0)
                {
                    new frmNumericKeyboard(this, true, "", "Double", "Enter Tax Percent").setVisible(true);
                    txtPercent.setText(clsGlobalVarClass.gNumerickeyboardValue);
                }
                else
                {
                    new frmNumericKeyboard(this, true, txtPercent.getText(), "Double", "Enter Tax Percent").setVisible(true);
                    txtPercent.setText(clsGlobalVarClass.gNumerickeyboardValue);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPercentMouseClicked

    private void txtTaxAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxAmountMouseClicked
        // TODO add your handling code here:
        try
        {
            if (amtFlag == true)
            {
                if (txtTaxAmount.getText().length() == 0)
                {
                    new frmNumericKeyboard(this, true, "", "Double", "Enter Tax Amount").setVisible(true);
                    txtTaxAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
                }
                else
                {
                    new frmNumericKeyboard(this, true, txtTaxAmount.getText(), "Double", "Enter Tax Amount").setVisible(true);
                    txtTaxAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtTaxAmountMouseClicked

    private void cmbTaxTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTaxTypeActionPerformed
        // TODO add your handling code here:
        if (!cmbTaxType.getSelectedItem().toString().equals("Percent"))
        {
            txtPercent.setText("0.00");
            txtTaxAmount.setEnabled(true);
            txtPercent.setEnabled(false);
            amtFlag = true;
            perFlag = false;
        }
        else
        {
            txtTaxAmount.setText("0.00");
            txtPercent.setEnabled(true);
            txtTaxAmount.setEnabled(false);
            amtFlag = false;
            perFlag = true;
        }
    }//GEN-LAST:event_cmbTaxTypeActionPerformed
    private void funSelectTaxCode()
    {
        try
        {
            funResetField();
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Tax");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetTaxMasterData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funAccountCodeTextfieldClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsAccountDtl> accountInfo = objLinkSangERP.funGetAccountDtl("GL Code", clsGlobalVarClass.gClientCode);
            new frmSearchFormDialog(this, true, accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                String accCode = data[0].toString();
                funSetAccountCode(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            objLinkSangERP = null;
        }
    }
    private void txtTaxCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxCodeMouseClicked
        // TODO add your handling code here:
        funSelectTaxCode();
    }//GEN-LAST:event_txtTaxCodeMouseClicked

    private void txtTaxCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectTaxCode();
        }
        if (evt.getKeyCode() == 10)
        {
            cmbTaxType.requestFocus();
        }
    }//GEN-LAST:event_txtTaxCodeKeyPressed

    private void cmbTaxTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtTaxDesc.requestFocus();
        }
    }//GEN-LAST:event_cmbTaxTypeKeyPressed

    private void txtTaxAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxAmountKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTaxAmountKeyPressed

    private void txtTaxDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxDescKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbTaxOnSP.requestFocus();
        }

    }//GEN-LAST:event_txtTaxDescKeyPressed

    private void cmbTaxOnSPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxOnSPKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtPercent.requestFocus();
        }

    }//GEN-LAST:event_cmbTaxOnSPKeyPressed

    private void txtPercentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPercentKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtPercentKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funTaxMasterOperations();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void cmbTaxGDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxGDKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbTaxCal.requestFocus();
        }
    }//GEN-LAST:event_cmbTaxGDKeyPressed

    private void jLabel13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel13KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            chkTaxRounded.requestFocus();
        }
    }//GEN-LAST:event_jLabel13KeyPressed

    private void chkTaxRoundedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkTaxRoundedKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbTaxIndicator.requestFocus();
        }
    }//GEN-LAST:event_chkTaxRoundedKeyPressed

    private void cmbTaxIndicatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxIndicatorKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            chkTaxOnTax.requestFocus();
        }
    }//GEN-LAST:event_cmbTaxIndicatorKeyPressed

    private void chkTaxOnTaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkTaxOnTaxKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteValidFrom.requestFocus();
        }
    }//GEN-LAST:event_chkTaxOnTaxKeyPressed

    private void dteValidFromKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteValidFromKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteValidTo.requestFocus();
        }
    }//GEN-LAST:event_dteValidFromKeyPressed

    private void dteValidToKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteValidToKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_dteValidToKeyPressed

    private void cmbTaxCalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxCalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbTaxIndicator.requestFocus();
        }
    }//GEN-LAST:event_cmbTaxCalKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funTaxMasterOperations();
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtAccountCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAccountCodeMouseClicked
        // TODO add your handling code here:
        funAccountCodeTextfieldClicked();

    }//GEN-LAST:event_txtAccountCodeMouseClicked

    private void txtAccountCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeActionPerformed

    private void txtAccountCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeKeyPressed

    private void txtTaxShortNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxShortNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxShortNameMouseClicked

    private void txtTaxShortNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxShortNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxShortNameKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Tax Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Tax Master");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmTaxMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmTaxMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmTaxMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmTaxMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmTaxMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkDineIn;
    private javax.swing.JCheckBox chkHomeDelivery;
    private javax.swing.JCheckBox chkTakeAway;
    private javax.swing.JCheckBox chkTaxOnTax;
    private javax.swing.JCheckBox chkTaxRounded;
    private javax.swing.JComboBox cmbTaxCal;
    private javax.swing.JComboBox cmbTaxGD;
    private javax.swing.JComboBox cmbTaxIndicator;
    private javax.swing.JComboBox cmbTaxOnItemType;
    private javax.swing.JComboBox cmbTaxOnSP;
    private javax.swing.JComboBox cmbTaxType;
    private com.toedter.calendar.JDateChooser dteValidFrom;
    private com.toedter.calendar.JDateChooser dteValidTo;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblAccountCode;
    private javax.swing.JLabel lblBillNote;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTaxAmount;
    private javax.swing.JLabel lblTaxCode;
    private javax.swing.JLabel lblTaxShortName;
    private javax.swing.JLabel lblTaxType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblValidFrom1;
    private javax.swing.JLabel lblValidTo1;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelLinkupTab;
    private javax.swing.JPanel panelTabDtl1;
    private javax.swing.JPanel panelTabDtl2;
    private javax.swing.JPanel panelTabDtl3;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblAreaMaster;
    private javax.swing.JTable tblPOSCode;
    private javax.swing.JTable tblTaxOnGroupApplicable;
    private javax.swing.JTable tblTaxOnTax;
    private javax.swing.JTable tblTaxSettlementMode;
    private javax.swing.JTextField txtAccountCode;
    private javax.swing.JTextArea txtBillNote;
    private javax.swing.JTextField txtPercent;
    private javax.swing.JTextField txtTaxAmount;
    private javax.swing.JTextField txtTaxCode;
    private javax.swing.JTextField txtTaxDesc;
    private javax.swing.JTextField txtTaxShortName;
    // End of variables declaration//GEN-END:variables

    private void funFillTaxOnGroupTable()
    {
        try
        {
            DefaultTableModel dmForTaxOnGroup = (DefaultTableModel) tblTaxOnGroupApplicable.getModel();
            
            dmForTaxOnGroup.setRowCount(0);
            

            String sql = "select a.strGroupCode,a.strGroupName "
                    + "from tblgrouphd a "
                    + "where a.strOperationalYN='Y' "
                    + "order by a.strGroupCode,a.strGroupName;";
            ResultSet rsGroups = clsGlobalVarClass.dbMysql.executeResultSet(sql);                        
            while (rsGroups.next())
            {
                Object[] row =
                {
                    rsGroups.getString(1), rsGroups.getString(2), false
                };

                dmForTaxOnGroup.addRow(row);
            }
            rsGroups.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillTaxOnGroupTable(String taxCode)
    {
        try
        {
            DefaultTableModel dmForTaxOnGroup = (DefaultTableModel) tblTaxOnGroupApplicable.getModel();

            String sql = "select a.strTaxCode,a.strGroupCode,a.strGroupName,a.strApplicable "
                    + "from tbltaxongroup a "
                    + "where a.strTaxCode='" + taxCode + "' ";

            ResultSet rsGroups = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsGroups.next())
            {
                for (int row = 0; row < tblTaxOnGroupApplicable.getRowCount(); row++)
                {
                    if (rsGroups.getString(4).equalsIgnoreCase("true") && rsGroups.getString(2).equals(tblTaxOnGroupApplicable.getValueAt(row, 0).toString()))
                    {
                        tblTaxOnGroupApplicable.setValueAt(true, row, 2);
                    }
                }
            }
            rsGroups.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSaveTaxOnGroupData(String taxCode)
    {
        try
        {
            DefaultTableModel dmForTaxOnGroup = (DefaultTableModel) tblTaxOnGroupApplicable.getModel();

            String sql = "insert into tbltaxongroup values ";

            for (int row = 0; row < tblTaxOnGroupApplicable.getRowCount(); row++)
            {
                String groupCode = tblTaxOnGroupApplicable.getValueAt(row, 0).toString();
                String groupName = tblTaxOnGroupApplicable.getValueAt(row, 1).toString();
                String applicable = tblTaxOnGroupApplicable.getValueAt(row, 2).toString();

                if (row == 0)
                {
                    sql += "('" + taxCode + "','" + groupCode + "','" + groupName + "','" + applicable + "'"
                            + ",'" + validFromDate + "','" + validToDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                            + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                }
                else
                {
                    sql += ",('" + taxCode + "','" + groupCode + "','" + groupName + "','" + applicable + "'"
                            + ",'" + validFromDate + "','" + validToDate + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                            + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                }
            }
            if(tblTaxOnGroupApplicable.getRowCount()>0)
            {
                clsGlobalVarClass.dbMysql.execute("delete from tbltaxongroup where strTaxCode='"+taxCode+"' ");
                int sc1 = clsGlobalVarClass.dbMysql.execute(sql);
            }           
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
}
