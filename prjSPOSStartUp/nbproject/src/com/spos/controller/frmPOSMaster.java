/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spos.controller;

import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsTextFileGeneratorForPrinting;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
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
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmPOSMaster extends javax.swing.JFrame {

    private String gpCode;
    private String userCode, code, strCode;
    private String[] posType = {"Direct Biller", "Dina"};
    DefaultTableModel dmTimeTable;
    Map<String,String> hmSettlementDtl = new HashMap<String,String>();
    private String printerType;

    /**
     * This method is used to initialize frmPosMaster
     */
    public frmPOSMaster() {
        initComponents();
        this.setLocationRelativeTo(null);
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        txtPosName.requestFocus();
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        cmbPosType.removeAllItems();
        cmbPosType.addItem(posType[0]);
        cmbPosType.addItem(posType[1]);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblPosName.setText(clsGlobalVarClass.gPOSName);

        dmTimeTable = new DefaultTableModel();
        dmTimeTable.addColumn("From Time");
        dmTimeTable.addColumn("To Time");
        funSetShortCutKeys();
        funFillSettlementDtl();
    }

    private void funSetShortCutKeys() {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to initialize frmPOs Master for pos code
     *
     * @param uCode
     */
    public frmPOSMaster(String uCode) {
        initComponents();
        this.setLocationRelativeTo(null);
        txtPosName.requestFocus();
        userCode = uCode;
        lblUserCode.setText(userCode);
        cmbPosType.removeAllItems();
        cmbPosType.addItem(posType[0]);
        cmbPosType.addItem(posType[1]);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField() {
        try {
            txtPosName.requestFocus();
            btnNew.setText("SAVE");
            txtPosCode.setText("");
            txtPosName.setText("");
            cmbPosType.removeAllItems();
            cmbPosType.addItem(posType[0]);
            cmbPosType.addItem(posType[1]);
            cmbDebitCardTransactionYN.setSelectedIndex(0);
            txtPropertyPOSCode.setText("");
            txtBillPrinterPort.setText("");
            chkCounter.setSelected(false);
            chkDelayedSettlement.setSelected(false);
            chkOpeartionalYN.setSelected(false);
            txtAdvReceiptPrinterPort.setText("");
            chkPrintVatNo.setSelected(false);
            chkServiceTaxNo.setSelected(false);
            txtVatNo1.setText("");
            txtServiceTaxNo.setText("");
            txtRoundOff.setText("");
            txtTip.setText("");
            txtDiscount.setText("");
            
            funReset();
            
            for (int i = 0; i < tblSettlementDtl.getRowCount(); i++) {
                tblSettlementDtl.setValueAt(false, i, 2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void setData(Object[] data) {
        try {
            String sql = "select strPosCode,strPosName,strPosType,strDebitCardTransactionYN"
                + " ,strPropertyPOSCode,strCounterWiseBilling,strDelayedSettlementForDB"
                + " ,strBillPrinterPort ,strAdvReceiptPrinterPort,strOperationalYN"
                + ",strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo,strRoundOff,strTip,strDiscount "
                + " from tblposmaster where strPosCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsPOSInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsPOSInfo.next();
            txtPosCode.setText(rsPOSInfo.getString(1));
            txtPosName.setText(rsPOSInfo.getString(2));
            cmbPosType.setSelectedItem(rsPOSInfo.getString(3));
            cmbDebitCardTransactionYN.setSelectedItem(rsPOSInfo.getString(4));
            txtPropertyPOSCode.setText(rsPOSInfo.getString(5));
            
            String billPrinterName=rsPOSInfo.getString(8).replaceAll("#","\\\\");
            String advOrderPrinterName=rsPOSInfo.getString(9).replaceAll("#","\\\\");
                
            txtBillPrinterPort.setText(billPrinterName);
            txtAdvReceiptPrinterPort.setText(advOrderPrinterName);

            if (rsPOSInfo.getString(6).equals("Yes")) {
                chkCounter.setSelected(true);
            } else {
                chkCounter.setSelected(false);
            }

            if (rsPOSInfo.getString(7).equals("Y")) {
                chkDelayedSettlement.setSelected(true);
            } else {
                chkDelayedSettlement.setSelected(false);
            }
            
            String operationalYN=rsPOSInfo.getString(10);
            if(operationalYN.equalsIgnoreCase("Y"))
            {
                chkOpeartionalYN.setSelected(true);
            }
            else
            {
                chkOpeartionalYN.setSelected(false);
            }
            if(rsPOSInfo.getString(11).equals("Y"))
            {
                chkPrintVatNo.setSelected(true);
            }
            else
            {
                chkPrintVatNo.setSelected(false);
            }
             
            if(rsPOSInfo.getString(12).equals("Y"))
            {
                chkServiceTaxNo.setSelected(true);
            }
            else
            {
                chkServiceTaxNo.setSelected(false);
            }
            
            txtVatNo1.setText(rsPOSInfo.getString(13));
            txtServiceTaxNo.setText(rsPOSInfo.getString(14));
            txtRoundOff.setText(rsPOSInfo.getString(15));
            txtTip.setText(rsPOSInfo.getString(16));
            txtDiscount.setText(rsPOSInfo.getString(17));
            funFillSettlementDtl();
            sql="select * from tblpossettlementdtl where strPOSCode='"+txtPosCode.getText()+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
            ResultSet rsSettlementDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsSettlementDtl.next())
            {
                for(int cnt=0;cnt<tblSettlementDtl.getRowCount();cnt++)
                {
                    if(tblSettlementDtl.getValueAt(cnt,0).toString().equals(rsSettlementDtl.getString(2)))
                    {
                        tblSettlementDtl.setValueAt(true, cnt, 2);
                        break;
                    }
                }
            }
            rsSettlementDtl.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set table data
     */
    private void funSetTableData() {
        try {
            String sqlReOrderTime = "select tmeFromTime,tmeToTime from tblreordertime where strPOSCode='" + txtPosCode.getText().trim() + "' ";
            ResultSet rsReOrderTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlReOrderTime);
            while (rsReOrderTime.next()) {
                Object row[] = {rsReOrderTime.getString(1), rsReOrderTime.getString(2)};
                System.out.println(rsReOrderTime.getString(1) + "\t" + rsReOrderTime.getString(1));
                dmTimeTable.addRow(row);
            }
            tblTimeTable.setModel(dmTimeTable);
            rsReOrderTime.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save reorder
     *
     * @param pCode
     * @throws Exception
     */
    private void funSaveReOrder(String pCode) throws Exception {
        String posCode = pCode;
        Object[] records = new Object[2];
        for (int cnt = 0; cnt < tblTimeTable.getRowCount(); cnt++) {
            String sql = "Insert into tblreordertime (tmeFromTime ,tmeToTime,strPOSCode,strUserCreated,strUserEdited,"
                    + "dteDateCreated,dteDateEdited,strClientCode) "
                    + "VALUES('" + tblTimeTable.getValueAt(cnt, 0).toString() + "','" + tblTimeTable.getValueAt(cnt, 1)
                    + "','" + posCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                    + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        String sql = "select tmeFromTime,tmeToTime from tblreordertime";
        ResultSet rsExecute = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsExecute.next()) {
            records[0] = rsExecute.getString(1);
            records[1] = rsExecute.getString(2);
        }
        rsExecute.close();
        funAllReset();
    }

    /**
     * This method is used to add row
     */
    private void funAddRow() // To add Time into the Table (Jai)
    {
        try {
            if (cmbFromHour.getSelectedItem() == "HH") {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Hour in From Time");
                return;
            }
            if (cmbFromMinute.getSelectedItem() == "MM") {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Minute in From Time");
                return;
            }
            if (cmbToHour.getSelectedItem() == "HH") {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Hour in To Time");
                return;
            }
            if (cmbToMinute.getSelectedItem() == "MM") {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Minute in TO Time");
                return;
            }

            if ((!cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH"))) {
                new frmOkPopUp(this, "Please Select To Time", "Error", 1).setVisible(true);
                return;
            } else if ((cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (!cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH"))) {
                new frmOkPopUp(this, "Please Select From Time", "Error", 1).setVisible(true);
                return;
            }

            Object[] column = new Object[2];
            String fromTime = cmbFromHour.getSelectedItem() + ":" + cmbFromMinute.getSelectedItem() + " " + cmbFromAMPM.getSelectedItem();
            column[0] = fromTime;

            String toTime = cmbToHour.getSelectedItem() + ":" + cmbToMinute.getSelectedItem() + " " + cmbToAMPM.getSelectedItem();
            column[1] = toTime;

            String[] arrFromTimeAMPM = fromTime.split(" ");
            String[] arrFromTime = arrFromTimeAMPM[0].split(":");
            int fromHour = Integer.parseInt(arrFromTime[0]);
            int fromMin = Integer.parseInt(arrFromTime[1]);

            String[] arrToTimeAMPM = toTime.split(" ");
            String[] arrToTime = arrToTimeAMPM[0].split(":");
            int toHour = Integer.parseInt(arrToTime[0]);
            int toMin = Integer.parseInt(arrToTime[1]);

            if (cmbFromAMPM.getSelectedItem() == "PM") {
                fromHour += fromHour + 12;
            }

            if (cmbToAMPM.getSelectedItem() == "PM") {
                toHour += toHour + 12;
            }

            clsUtility objUtility=new clsUtility();
            String currDate = objUtility.funGetCurrentDate();
            String finalFromTime = currDate + " " + fromHour + ":" + fromMin + ":00";
            String finalToTime = currDate + " " + toHour + ":" + toMin + ":00";
            long result = objUtility.funCompareTime(finalFromTime, finalToTime);
            if (result > 0) {
                if (!(funCheckDuplicateRow(fromTime, toTime))) {
                    dmTimeTable.addRow(column);
                    tblTimeTable.setModel(dmTimeTable);
                    tblTimeTable.setRowHeight(30);

                    DefaultTableCellRenderer LeftRenderer = new DefaultTableCellRenderer();
                    LeftRenderer.setHorizontalAlignment(JLabel.LEFT);
                    tblTimeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
                    tblTimeTable.getColumnModel().getColumn(1).setPreferredWidth(200);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Date");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to check duplicate row
     *
     * @param from
     * @param to
     * @return
     */
    private boolean funCheckDuplicateRow(String from, String to) {
        boolean flg = false;
        String fromtime = from;
        String totime = to;
        String tblFrom = "";
        String tblTo = "";

        for (int i = 0; i < tblTimeTable.getRowCount(); i++) {
            tblFrom = tblTimeTable.getValueAt(i, 0).toString();
            tblTo = tblTimeTable.getValueAt(i, 1).toString();
            if ((tblFrom.equalsIgnoreCase(fromtime)) && (tblTo.equalsIgnoreCase(totime))) {

                flg = true;
                break;
            }
        }
        return flg;
    }

    /**
     * This method is used to reset all time combo boxes
     */
    private void funReset() {
        dmTimeTable.setRowCount(0);
        cmbFromHour.setSelectedIndex(0);
        cmbFromMinute.setSelectedIndex(0);
        cmbFromAMPM.setSelectedIndex(0);
        cmbToHour.setSelectedIndex(0);
        cmbToMinute.setSelectedIndex(0);
        cmbToAMPM.setSelectedIndex(0);
    }

    /**
     * This method is used to reset all fields
     */
    private void funAllReset() {
        dmTimeTable = new DefaultTableModel();
        cmbFromHour.setSelectedIndex(0);
        cmbFromMinute.setSelectedIndex(0);
        cmbFromAMPM.setSelectedIndex(0);
        cmbToHour.setSelectedIndex(0);
        cmbToMinute.setSelectedIndex(0);
        cmbToAMPM.setSelectedIndex(0);
        dmTimeTable.setRowCount(0);
        dmTimeTable.setRowCount(0);
        funResetField();
    }

    /**
     * This method is used to select pos
     */
    private void funSelectPOS() {
        clsGlobalVarClass.funCallForSearchForm("Pos");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked) {
            btnNew.setText("UPDATE");
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            setData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
            funSetTableData();
        }
    }

    /**
     * This method is used to generate pos code
     *
     * @return
     */
    private String funGeneratePosCode() {
        try {
            String sql = "select count(*) from tblposmaster";
            ResultSet countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            countSet.next();
            int cn = countSet.getInt(1);
            countSet.close();
            if (cn > 0) {
                sql = "select max(strPosCode) from tblposmaster";
                countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                countSet.next();
                code = countSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 1).toString();
                for (int i = 0; i < ss.length(); i++) {
                    if (ss.charAt(i) != '0') {
                        strCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                int intCode = Integer.parseInt(strCode);
                intCode++;
                if (intCode < 10) {
                    gpCode = "P0" + intCode;
                } else {
                    gpCode = "P" + intCode;
                }
            } else {
                code = "0";
                gpCode = "P01";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gpCode;
    }
    
    
    private void funSaveSettlementDtl(String posCode) throws Exception
    {
        StringBuilder sb=new StringBuilder();
        
        sb.append("delete from tblpossettlementdtl where strPOSCode='"+posCode+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"'");
        clsGlobalVarClass.dbMysql.execute(sb.toString());
        
        for(int cnt=0;cnt<tblSettlementDtl.getRowCount();cnt++)
        {
            boolean applicable = Boolean.parseBoolean(tblSettlementDtl.getValueAt(cnt, 2).toString());
            if(applicable)
            {
                sb.setLength(0);
                sb.append("insert into tblpossettlementdtl (strPOSCode,strSettlementCode,strSettlementDesc,strClientCode,strDataPostFlag) ");
                sb.append("values('"+posCode+"','"+tblSettlementDtl.getValueAt(cnt, 0).toString()+"',");
                sb.append("'"+tblSettlementDtl.getValueAt(cnt, 1).toString()+"','"+clsGlobalVarClass.gClientCode+"','N')");
                //System.out.println(sb.toString());
                clsGlobalVarClass.dbMysql.execute(sb.toString());
            }
        }
        sb=null;
    }
        

    /**
     * This method is used to save pos
     *
     * @return int
     */
    private int funSavePOS() {
        try {
            gpCode = funGeneratePosCode();
            if (!clsGlobalVarClass.validateEmpty(txtPosName.getText())) {
                new frmOkPopUp(this, "Please Enter Pos Name", "Error", 0).setVisible(true);
                txtPosName.setText("");
                txtPosName.requestFocus();
            } else {
                txtPosCode.setText(gpCode);
                String counterWiseBilling = "No";
                if (chkCounter.isSelected()) {
                    counterWiseBilling = "Yes";
                }
                String delayedSettlement = "N";
                if (chkDelayedSettlement.isSelected()) {
                    delayedSettlement = "Y";
                }
                String operationalYN = "N";
                if (chkOpeartionalYN.isSelected())
                {
                    operationalYN = "Y";
                }
                
                String printVatNo="N";
                if(chkPrintVatNo.isSelected())
                {
                    printVatNo="Y";
                }
                String printServiceTaxNo="N";
                if(chkServiceTaxNo.isSelected())
                {
                    printServiceTaxNo="Y";
                } 
                
                
                String billPrinterName=txtBillPrinterPort.getText().trim().replaceAll("\\\\", "#");
                String advOrderPrinterName=txtAdvReceiptPrinterPort.getText().trim().replaceAll("\\\\", "#");
                
                String sql = "insert into tblposmaster(strPosCode,strPosName,strPosType,strDebitCardTransactionYN,"
                   + "strPropertyPOSCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
                    + ",strCounterWiseBilling,strDelayedSettlementForDB,strBillPrinterPort"
                    + ",strAdvReceiptPrinterPort,strOperationalYN,strPrintVatNo"
                    + ",strPrintServiceTaxNo,strVatNo,strServiceTaxNo,strRoundOff,strTip,strDiscount) "
                    + "values('" + txtPosCode.getText() + "','" + txtPosName.getText() + "','"
                    + cmbPosType.getSelectedItem().toString() + "','" + cmbDebitCardTransactionYN.getSelectedItem().toString()
                    + "','" + txtPropertyPOSCode.getText().trim() + "','" + clsGlobalVarClass.gUserCode + "','"
                    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"
                    + clsGlobalVarClass.getCurrentDateTime() + "','" + counterWiseBilling + "'"
                    + ",'" + delayedSettlement + "' ,'" + billPrinterName + "'"
                    + ",'" + advOrderPrinterName + "','" + operationalYN + "','" +printVatNo + "','" +printServiceTaxNo + "','" + txtVatNo1.getText() + "','" + txtServiceTaxNo.getText() + "' , "
                    + "'"+txtRoundOff.getText()+"','"+txtTip.getText()+"','"+txtDiscount.getText()+"')";
                System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                if (exc > 0) {
                    
                    funSaveSettlementDtl(txtPosCode.getText());
                    funSaveReOrder(txtPosCode.getText());
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This method is used to update pos
     */
    private void funUpdatePOS() {
        try {
            if (!clsGlobalVarClass.validateEmpty(txtPosName.getText())) {
                new frmOkPopUp(this, "Please Enter Pos Name", "Error", 0).setVisible(true);
            } else {
                String counterWiseBilling = "No";
                if (chkCounter.isSelected()) {
                    counterWiseBilling = "Yes";
                }
                String delayedSettlement = "N";
                if (chkDelayedSettlement.isSelected()) {
                    delayedSettlement = "Y";
                }
                String operationalYN = "N";
                if (chkOpeartionalYN.isSelected())
                {
                    operationalYN = "Y";
                }
                String printVatNo="N";
                if(chkPrintVatNo.isSelected())
                {
                    printVatNo="Y";
                }
                String printServiceTaxNo="N";
                if(chkServiceTaxNo.isSelected())
                {
                    printServiceTaxNo="Y";
                } 
                
                
                String billPrinterName=txtBillPrinterPort.getText().trim().replaceAll("\\\\", "#");
                String advOrderPrinterName=txtAdvReceiptPrinterPort.getText().trim().replaceAll("\\\\", "#");
                                
                String sql = "UPDATE tblposmaster SET strPosName = '" + txtPosName.getText() + "',strPosType = "
                    + " '" + cmbPosType.getSelectedItem().toString() + "',strDebitCardTransactionYN="
                    + " '" + cmbDebitCardTransactionYN.getSelectedItem().toString() + "',strPropertyPOSCode="
                    + " '" + txtPropertyPOSCode.getText().trim() + "',strUserEdited='" + clsGlobalVarClass.gUserCode
                    + " ',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + " ,strCounterWiseBilling='" + counterWiseBilling + "',strDelayedSettlementForDB='" + delayedSettlement + "' "
                    + " ,strBillPrinterPort='" + billPrinterName + "' "
                    + " ,strAdvReceiptPrinterPort='" + advOrderPrinterName + "' "
                    + ",strOperationalYN='" + operationalYN + "',strPrintVatNo='"+printVatNo+"',strPrintServiceTaxNo='"+printServiceTaxNo+"' "
                    + ",strVatNo='" + txtVatNo1.getText() + "',strServiceTaxNo='" + txtServiceTaxNo.getText() + "',strRoundOff='"+txtRoundOff.getText()+"' ,strTip='"+txtTip.getText()+"',strDiscount='"+txtDiscount.getText()+"'"
                    + " WHERE strPosCode ='" + txtPosCode.getText() + "'";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                funSaveSettlementDtl(txtPosCode.getText());
                funUpdateReOrderTime();

                if (exc > 0) {
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);

                }
                funResetField();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is used to update reorder time
     */
    private void funUpdateReOrderTime() {
        try {
            String deleteReOrder = "delete from tblreordertime where strPOSCode='" + txtPosCode.getText().trim() + "'";
            clsGlobalVarClass.dbMysql.execute(deleteReOrder);

            for (int cnt = 0; cnt < tblTimeTable.getRowCount(); cnt++) {
                System.out.println(tblTimeTable.getValueAt(cnt, 0).toString() + "\t" + tblTimeTable.getValueAt(cnt, 1));

                String sql = "Insert into tblreordertime "
                        + "(tmeFromTime ,tmeToTime,strPOSCode,strUserCreated,strUserEdited,"
                        + "dteDateCreated,dteDateEdited,strClientCode) "
                        + "VALUES('" + tblTimeTable.getValueAt(cnt, 0).toString() + "','" + tblTimeTable.getValueAt(cnt, 1)
                        + "','" + txtPosCode.getText().trim() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                        + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                clsGlobalVarClass.dbMysql.execute(sql);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void funFillSettlementDtl()
    {
        try
        {
            DefaultTableModel dmSettlementDtl = (DefaultTableModel) tblSettlementDtl.getModel();
            dmSettlementDtl.setRowCount(0);
            String sql = "select strSettelmentCode,strSettelmentDesc "
                + "from tblsettelmenthd";
            ResultSet rsSettlementDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsSettlementDtl.next()) 
            {
                hmSettlementDtl.put(rsSettlementDtl.getString(2),rsSettlementDtl.getString(1));
                
                Object[] ob = {rsSettlementDtl.getString(1),rsSettlementDtl.getString(2), false};
                dmSettlementDtl.addRow(ob);
            }
            tblSettlementDtl.setModel(dmSettlementDtl);
            rsSettlementDtl.close();
        }catch(Exception e)
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
        };
        panelbody = new javax.swing.JPanel();
        tabPOSMaster = new javax.swing.JTabbedPane();
        panelPOS = new javax.swing.JPanel();
        lblPosCode = new javax.swing.JLabel();
        lblPosName1 = new javax.swing.JLabel();
        lblPosType = new javax.swing.JLabel();
        txtPosName = new javax.swing.JTextField();
        txtPosCode = new javax.swing.JTextField();
        cmbPosType = new javax.swing.JComboBox();
        lblFormName = new javax.swing.JLabel();
        lblCardTransaction = new javax.swing.JLabel();
        cmbDebitCardTransactionYN = new javax.swing.JComboBox();
        txtPropertyPOSCode = new javax.swing.JTextField();
        lblPosName4 = new javax.swing.JLabel();
        chkCounter = new javax.swing.JCheckBox();
        lblCounterWiseBilling = new javax.swing.JLabel();
        lblDelayedSettlement = new javax.swing.JLabel();
        chkDelayedSettlement = new javax.swing.JCheckBox();
        lblBillPrinterPort = new javax.swing.JLabel();
        txtBillPrinterPort = new javax.swing.JTextField();
        lblDiscount = new javax.swing.JLabel();
        txtAdvReceiptPrinterPort = new javax.swing.JTextField();
        lblOperational = new javax.swing.JLabel();
        chkOpeartionalYN = new javax.swing.JCheckBox();
        btnBillPrinterTest = new javax.swing.JButton();
        btnAdvReceiptPrinterTest = new javax.swing.JButton();
        chkPrintVatNo = new javax.swing.JCheckBox();
        chkServiceTaxNo = new javax.swing.JCheckBox();
        txtServiceTaxNo = new javax.swing.JTextField();
        txtTip = new javax.swing.JTextField();
        txtVatNo1 = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        lblAdvReceiptPrinterPort1 = new javax.swing.JLabel();
        lblRoundOff = new javax.swing.JLabel();
        lblTip = new javax.swing.JLabel();
        txtRoundOff = new javax.swing.JTextField();
        panelSettlementDtl = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSettlementDtl = new javax.swing.JTable();
        panelReOrder = new javax.swing.JPanel();
        lblToTime = new javax.swing.JLabel();
        lblfromTime = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tblTimeTable = new javax.swing.JTable();
        btnReset2 = new javax.swing.JButton();
        btnAllReset1 = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        cmbFromHour = new javax.swing.JComboBox();
        cmbFromMinute = new javax.swing.JComboBox();
        cmbFromAMPM = new javax.swing.JComboBox();
        cmbToAMPM = new javax.swing.JComboBox();
        cmbToHour = new javax.swing.JComboBox();
        cmbToMinute = new javax.swing.JComboBox();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);

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
        lblformName.setText(" -POS Master");
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

        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelbody.setOpaque(false);

        panelPOS.setBackground(new java.awt.Color(255, 255, 255));
        panelPOS.setForeground(new java.awt.Color(236, 179, 7));
        panelPOS.setOpaque(false);
        panelPOS.setPreferredSize(new java.awt.Dimension(800, 560));
        panelPOS.setLayout(null);

        lblPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPosCode.setText("POS Code                          :");
        panelPOS.add(lblPosCode);
        lblPosCode.setBounds(210, 30, 190, 30);

        lblPosName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPosName1.setText("POS Name                         :");
        panelPOS.add(lblPosName1);
        lblPosName1.setBounds(210, 70, 190, 30);

        lblPosType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPosType.setText("Style Of Operation              :");
        panelPOS.add(lblPosType);
        lblPosType.setBounds(210, 110, 190, 30);

        txtPosName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPosNameMouseClicked(evt);
            }
        });
        txtPosName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPosNameKeyPressed(evt);
            }
        });
        panelPOS.add(txtPosName);
        txtPosName.setBounds(400, 70, 190, 30);

        txtPosCode.setBackground(new java.awt.Color(240, 240, 240));
        txtPosCode.setEnabled(false);
        txtPosCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPosCodeMouseClicked(evt);
            }
        });
        txtPosCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPosCodeKeyPressed(evt);
            }
        });
        panelPOS.add(txtPosCode);
        txtPosCode.setBounds(400, 30, 103, 30);

        cmbPosType.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Direct Biller", "Dine In" }));
        cmbPosType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPosTypeKeyPressed(evt);
            }
        });
        panelPOS.add(cmbPosType);
        cmbPosType.setBounds(400, 110, 190, 30);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(13, 9, 9));
        lblFormName.setText("POS Master ");
        panelPOS.add(lblFormName);
        lblFormName.setBounds(330, 0, 157, 30);

        lblCardTransaction.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardTransaction.setText("Debit Card Transaction         :");
        panelPOS.add(lblCardTransaction);
        lblCardTransaction.setBounds(210, 150, 190, 30);

        cmbDebitCardTransactionYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        cmbDebitCardTransactionYN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDebitCardTransactionYNKeyPressed(evt);
            }
        });
        panelPOS.add(cmbDebitCardTransactionYN);
        cmbDebitCardTransactionYN.setBounds(400, 150, 190, 30);

        txtPropertyPOSCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPropertyPOSCodeMouseClicked(evt);
            }
        });
        txtPropertyPOSCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPropertyPOSCodeKeyPressed(evt);
            }
        });
        panelPOS.add(txtPropertyPOSCode);
        txtPropertyPOSCode.setBounds(400, 190, 190, 30);

        lblPosName4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPosName4.setText("Property POS Code             :");
        panelPOS.add(lblPosName4);
        lblPosName4.setBounds(210, 190, 190, 30);

        chkCounter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkCounter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCounterActionPerformed(evt);
            }
        });
        chkCounter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkCounterKeyPressed(evt);
            }
        });
        panelPOS.add(chkCounter);
        chkCounter.setBounds(400, 230, 30, 21);

        lblCounterWiseBilling.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterWiseBilling.setText("Counter Wise Billing             :");
        panelPOS.add(lblCounterWiseBilling);
        lblCounterWiseBilling.setBounds(210, 230, 190, 20);

        lblDelayedSettlement.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDelayedSettlement.setText("Delayed Settlement :");
        panelPOS.add(lblDelayedSettlement);
        lblDelayedSettlement.setBounds(440, 230, 130, 20);

        chkDelayedSettlement.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDelayedSettlement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDelayedSettlementActionPerformed(evt);
            }
        });
        chkDelayedSettlement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkDelayedSettlementKeyPressed(evt);
            }
        });
        panelPOS.add(chkDelayedSettlement);
        chkDelayedSettlement.setBounds(570, 230, 21, 21);

        lblBillPrinterPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillPrinterPort.setText("Bill Printer Name                  :");
        lblBillPrinterPort.setName(""); // NOI18N
        panelPOS.add(lblBillPrinterPort);
        lblBillPrinterPort.setBounds(210, 260, 190, 30);

        txtBillPrinterPort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBillPrinterPortMouseClicked(evt);
            }
        });
        txtBillPrinterPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillPrinterPortActionPerformed(evt);
            }
        });
        txtBillPrinterPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBillPrinterPortKeyPressed(evt);
            }
        });
        panelPOS.add(txtBillPrinterPort);
        txtBillPrinterPort.setBounds(400, 260, 190, 30);

        lblDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscount.setText("Discount:");
        lblDiscount.setName(""); // NOI18N
        panelPOS.add(lblDiscount);
        lblDiscount.setBounds(410, 490, 60, 30);

        txtAdvReceiptPrinterPort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAdvReceiptPrinterPortMouseClicked(evt);
            }
        });
        txtAdvReceiptPrinterPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdvReceiptPrinterPortKeyPressed(evt);
            }
        });
        panelPOS.add(txtAdvReceiptPrinterPort);
        txtAdvReceiptPrinterPort.setBounds(400, 300, 190, 30);

        lblOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperational.setText("Operational                         :");
        panelPOS.add(lblOperational);
        lblOperational.setBounds(210, 340, 170, 20);

        chkOpeartionalYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkOpeartionalYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOpeartionalYNActionPerformed(evt);
            }
        });
        panelPOS.add(chkOpeartionalYN);
        chkOpeartionalYN.setBounds(400, 340, 30, 21);

        btnBillPrinterTest.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBillPrinterTest.setForeground(new java.awt.Color(255, 255, 255));
        btnBillPrinterTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn1.png"))); // NOI18N
        btnBillPrinterTest.setText("TEST");
        btnBillPrinterTest.setToolTipText("Save Cost Center Master");
        btnBillPrinterTest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillPrinterTest.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn2.png"))); // NOI18N
        btnBillPrinterTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBillPrinterTestMouseClicked(evt);
            }
        });
        btnBillPrinterTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillPrinterTestActionPerformed(evt);
            }
        });
        btnBillPrinterTest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBillPrinterTestKeyPressed(evt);
            }
        });
        panelPOS.add(btnBillPrinterTest);
        btnBillPrinterTest.setBounds(600, 260, 70, 30);

        btnAdvReceiptPrinterTest.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdvReceiptPrinterTest.setForeground(new java.awt.Color(255, 255, 255));
        btnAdvReceiptPrinterTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn1.png"))); // NOI18N
        btnAdvReceiptPrinterTest.setText("TEST");
        btnAdvReceiptPrinterTest.setToolTipText("Save Cost Center Master");
        btnAdvReceiptPrinterTest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdvReceiptPrinterTest.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn2.png"))); // NOI18N
        btnAdvReceiptPrinterTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAdvReceiptPrinterTestMouseClicked(evt);
            }
        });
        btnAdvReceiptPrinterTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvReceiptPrinterTestActionPerformed(evt);
            }
        });
        btnAdvReceiptPrinterTest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAdvReceiptPrinterTestKeyPressed(evt);
            }
        });
        panelPOS.add(btnAdvReceiptPrinterTest);
        btnAdvReceiptPrinterTest.setBounds(600, 300, 70, 30);

        chkPrintVatNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintVatNo.setText("Print VAT No.            :");
        chkPrintVatNo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintVatNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintVatNoActionPerformed(evt);
            }
        });
        panelPOS.add(chkPrintVatNo);
        chkPrintVatNo.setBounds(210, 370, 160, 30);

        chkServiceTaxNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkServiceTaxNo.setText("Print Service Tax No.   :");
        chkServiceTaxNo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        panelPOS.add(chkServiceTaxNo);
        chkServiceTaxNo.setBounds(210, 410, 160, 30);

        txtServiceTaxNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtServiceTaxNoMouseClicked(evt);
            }
        });
        txtServiceTaxNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtServiceTaxNoKeyPressed(evt);
            }
        });
        panelPOS.add(txtServiceTaxNo);
        txtServiceTaxNo.setBounds(400, 410, 330, 30);

        txtTip.setBackground(new java.awt.Color(240, 240, 240));
        txtTip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTipMouseClicked(evt);
            }
        });
        txtTip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTipKeyPressed(evt);
            }
        });
        panelPOS.add(txtTip);
        txtTip.setBounds(260, 490, 120, 30);

        txtVatNo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtVatNo1MouseClicked(evt);
            }
        });
        txtVatNo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVatNo1KeyPressed(evt);
            }
        });
        panelPOS.add(txtVatNo1);
        txtVatNo1.setBounds(400, 370, 330, 30);

        txtDiscount.setBackground(new java.awt.Color(240, 240, 240));
        txtDiscount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDiscountMouseClicked(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountKeyPressed(evt);
            }
        });
        panelPOS.add(txtDiscount);
        txtDiscount.setBounds(470, 490, 120, 30);

        lblAdvReceiptPrinterPort1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAdvReceiptPrinterPort1.setText("Adv Receipt Printer Name     :");
        lblAdvReceiptPrinterPort1.setName(""); // NOI18N
        panelPOS.add(lblAdvReceiptPrinterPort1);
        lblAdvReceiptPrinterPort1.setBounds(210, 300, 190, 30);

        lblRoundOff.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRoundOff.setText("Round Off                          :");
        lblRoundOff.setName(""); // NOI18N
        panelPOS.add(lblRoundOff);
        lblRoundOff.setBounds(210, 450, 180, 30);

        lblTip.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTip.setText("Tip:");
        lblTip.setName(""); // NOI18N
        panelPOS.add(lblTip);
        lblTip.setBounds(210, 490, 30, 30);

        txtRoundOff.setBackground(new java.awt.Color(240, 240, 240));
        txtRoundOff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtRoundOffMouseClicked(evt);
            }
        });
        txtRoundOff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRoundOffKeyPressed(evt);
            }
        });
        panelPOS.add(txtRoundOff);
        txtRoundOff.setBounds(400, 450, 330, 30);

        tabPOSMaster.addTab("General", panelPOS);

        tblSettlementDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Settlement Code", "Settlement Name", "Applicable"
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
        tblSettlementDtl.setRowHeight(25);
        tblSettlementDtl.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblSettlementDtl.setSelectionForeground(new java.awt.Color(254, 254, 254));
        jScrollPane2.setViewportView(tblSettlementDtl);

        javax.swing.GroupLayout panelSettlementDtlLayout = new javax.swing.GroupLayout(panelSettlementDtl);
        panelSettlementDtl.setLayout(panelSettlementDtlLayout);
        panelSettlementDtlLayout.setHorizontalGroup(
            panelSettlementDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettlementDtlLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(259, Short.MAX_VALUE))
        );
        panelSettlementDtlLayout.setVerticalGroup(
            panelSettlementDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettlementDtlLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        tabPOSMaster.addTab("Settlement", panelSettlementDtl);

        panelReOrder.setBackground(new java.awt.Color(255, 255, 255));
        panelReOrder.setOpaque(false);
        panelReOrder.setPreferredSize(new java.awt.Dimension(610, 600));
        panelReOrder.setLayout(null);

        lblToTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToTime.setText("To Time");
        panelReOrder.add(lblToTime);
        lblToTime.setBounds(380, 50, 70, 30);

        lblfromTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblfromTime.setText("From Time");
        panelReOrder.add(lblfromTime);
        lblfromTime.setBounds(20, 50, 70, 30);

        btnAdd.setBackground(new java.awt.Color(204, 204, 204));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Reorder Timing");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panelReOrder.add(btnAdd);
        btnAdd.setBounds(110, 100, 90, 30);

        btnClose.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panelReOrder.add(btnClose);
        btnClose.setBounds(410, 510, 90, 40);

        tblTimeTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblTimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Time", "To Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollPane.setViewportView(tblTimeTable);

        panelReOrder.add(scrollPane);
        scrollPane.setBounds(0, 150, 800, 310);

        btnReset2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset2.setForeground(new java.awt.Color(255, 255, 255));
        btnReset2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset2.setText("Reset");
        btnReset2.setToolTipText("Reset All Fields");
        btnReset2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset2ActionPerformed(evt);
            }
        });
        panelReOrder.add(btnReset2);
        btnReset2.setBounds(250, 100, 90, 30);

        btnAllReset1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAllReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnAllReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAllReset1.setText("Reset");
        btnAllReset1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAllReset1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAllReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllReset1ActionPerformed(evt);
            }
        });
        panelReOrder.add(btnAllReset1);
        btnAllReset1.setBounds(240, 510, 90, 40);

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        panelReOrder.add(btnSave);
        btnSave.setBounds(70, 510, 90, 40);

        cmbFromHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        panelReOrder.add(cmbFromHour);
        cmbFromHour.setBounds(110, 50, 60, 30);

        cmbFromMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        panelReOrder.add(cmbFromMinute);
        cmbFromMinute.setBounds(190, 50, 60, 30);

        cmbFromAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        panelReOrder.add(cmbFromAMPM);
        cmbFromAMPM.setBounds(270, 50, 60, 30);

        cmbToAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        panelReOrder.add(cmbToAMPM);
        cmbToAMPM.setBounds(610, 50, 60, 30);

        cmbToHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        panelReOrder.add(cmbToHour);
        cmbToHour.setBounds(450, 50, 60, 30);

        cmbToMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        panelReOrder.add(cmbToMinute);
        cmbToMinute.setBounds(530, 50, 60, 30);

        tabPOSMaster.addTab("ReOrder Time", panelReOrder);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Pos");
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

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Pos Master");
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

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tabPOSMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addComponent(tabPOSMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPosCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPosCodeMouseClicked
        // TODO add your handling code here:
        funSelectPOS();
    }//GEN-LAST:event_txtPosCodeMouseClicked

    private void cmbPosTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            cmbDebitCardTransactionYN.requestFocus();
        }
    }//GEN-LAST:event_cmbPosTypeKeyPressed

    private void cmbDebitCardTransactionYNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDebitCardTransactionYNKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            txtPropertyPOSCode.requestFocus();
        }
    }//GEN-LAST:event_cmbDebitCardTransactionYNKeyPressed

    private void txtPropertyPOSCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPropertyPOSCodeMouseClicked
        try
        {
            if (txtPropertyPOSCode.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please POS Code.").setVisible(true);
                txtPropertyPOSCode.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPropertyPOSCode.getText(), "1", "Please POS Code.").setVisible(true);
                txtPropertyPOSCode.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPropertyPOSCodeMouseClicked

    private void chkCounterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCounterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCounterActionPerformed

    private void chkDelayedSettlementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDelayedSettlementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDelayedSettlementActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        funAddRow();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnReset2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset2ActionPerformed
        funReset();
    }//GEN-LAST:event_btnReset2ActionPerformed

    private void btnAllReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllReset1ActionPerformed
        funAllReset();
    }//GEN-LAST:event_btnAllReset1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) {
            funSavePOS();
        } else {
            funUpdatePOS();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPosCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/') {
            funSelectPOS();
        }
    }//GEN-LAST:event_txtPosCodeKeyPressed

    private void txtPosNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPosNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            cmbPosType.requestFocus();
        }
    }//GEN-LAST:event_txtPosNameKeyPressed

    private void txtPropertyPOSCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPropertyPOSCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkCounter.requestFocus();
        }
    }//GEN-LAST:event_txtPropertyPOSCodeKeyPressed

    private void chkCounterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkCounterKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkDelayedSettlement.requestFocus();
        }
    }//GEN-LAST:event_chkCounterKeyPressed

    private void chkDelayedSettlementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkDelayedSettlementKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            txtBillPrinterPort.requestFocus();
        }
    }//GEN-LAST:event_chkDelayedSettlementKeyPressed

    private void txtBillPrinterPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillPrinterPortKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            txtAdvReceiptPrinterPort.requestFocus();
        }
    }//GEN-LAST:event_txtBillPrinterPortKeyPressed

    private void txtAdvReceiptPrinterPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvReceiptPrinterPortKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtAdvReceiptPrinterPortKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) {
            funSavePOS();
        } else {
            funUpdatePOS();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void chkOpeartionalYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkOpeartionalYNActionPerformed
    {//GEN-HEADEREND:event_chkOpeartionalYNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkOpeartionalYNActionPerformed

    private void txtBillPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBillPrinterPortMouseClicked
    {//GEN-HEADEREND:event_txtBillPrinterPortMouseClicked
        try
        {
            if (txtBillPrinterPort.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Bill Printer Name.").setVisible(true);
                txtBillPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtBillPrinterPort.getText(), "1", "Please Enter Bill Printer Name.").setVisible(true);
                txtBillPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtBillPrinterPortMouseClicked

    private void txtAdvReceiptPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtAdvReceiptPrinterPortMouseClicked
    {//GEN-HEADEREND:event_txtAdvReceiptPrinterPortMouseClicked
        
        try
        {
            if (txtAdvReceiptPrinterPort.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Advance Receipt Printer Name.").setVisible(true);
                txtAdvReceiptPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtAdvReceiptPrinterPort.getText(), "1", "Please Enter Advance Receipt Printer Name.").setVisible(true);
                txtAdvReceiptPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtAdvReceiptPrinterPortMouseClicked

    private void txtBillPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtBillPrinterPortActionPerformed
    {//GEN-HEADEREND:event_txtBillPrinterPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBillPrinterPortActionPerformed

    private void btnBillPrinterTestMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillPrinterTestMouseClicked
    {//GEN-HEADEREND:event_btnBillPrinterTestMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBillPrinterTestMouseClicked

    private void btnBillPrinterTestActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBillPrinterTestActionPerformed
    {//GEN-HEADEREND:event_btnBillPrinterTestActionPerformed
        printerType="BillPrinter";
        funTestPrint(txtBillPrinterPort.getText().trim());
    }//GEN-LAST:event_btnBillPrinterTestActionPerformed
    
    private void btnBillPrinterTestKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnBillPrinterTestKeyPressed
    {//GEN-HEADEREND:event_btnBillPrinterTestKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBillPrinterTestKeyPressed

    private void btnAdvReceiptPrinterTestMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAdvReceiptPrinterTestMouseClicked
    {//GEN-HEADEREND:event_btnAdvReceiptPrinterTestMouseClicked
        printerType="AdvancedReceiptPrinter"; 
        funTestPrint(txtAdvReceiptPrinterPort.getText().trim());
    }//GEN-LAST:event_btnAdvReceiptPrinterTestMouseClicked

    private void btnAdvReceiptPrinterTestActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAdvReceiptPrinterTestActionPerformed
    {//GEN-HEADEREND:event_btnAdvReceiptPrinterTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdvReceiptPrinterTestActionPerformed

    private void btnAdvReceiptPrinterTestKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAdvReceiptPrinterTestKeyPressed
    {//GEN-HEADEREND:event_btnAdvReceiptPrinterTestKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdvReceiptPrinterTestKeyPressed

    private void txtPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPosNameMouseClicked
    {//GEN-HEADEREND:event_txtPosNameMouseClicked
       try
        {
            if (txtPosName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please POS Name.").setVisible(true);
                txtPosName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPosName.getText(), "1", "Please POS Name.").setVisible(true);
                txtPosName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPosNameMouseClicked

    private void chkPrintVatNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintVatNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPrintVatNoActionPerformed

    private void txtServiceTaxNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServiceTaxNoKeyPressed
        // TODO add your handling code here:
        
         if (evt.getKeyCode() == 10) {
            txtServiceTaxNo.requestFocus();
        }
    }//GEN-LAST:event_txtServiceTaxNoKeyPressed

    private void txtServiceTaxNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtServiceTaxNoMouseClicked
        // TODO add your handling code here:
        
        
          try
        {
            if (txtServiceTaxNo.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Service Tax No.").setVisible(true);
                txtServiceTaxNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtServiceTaxNo.getText(), "1", "Please Enter Service Tax No.").setVisible(true);
                txtServiceTaxNo.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtServiceTaxNoMouseClicked

    private void txtVatNo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVatNo1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVatNo1MouseClicked

    private void txtVatNo1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVatNo1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVatNo1KeyPressed

    private void txtDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountKeyPressed

    private void txtDiscountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountMouseClicked
        // TODO add your handling code here:

        clsUtility objUtility=new clsUtility();
        try
        {
            List<clsAccountDtl> accountInfo=objUtility.funGetAccountDtl("GL Code", "060.001");
            new frmSearchFormDialog(this, true,accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                String accCode=data[0].toString();
                System.out.println(accCode);
                funSetDiscount(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            objUtility=null;
        }
    }//GEN-LAST:event_txtDiscountMouseClicked

    private void txtTipKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == 10) {
            txtTip.requestFocus();
        }
    }//GEN-LAST:event_txtTipKeyPressed

    private void txtTipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTipMouseClicked
        // TODO add your handling code here:

        clsUtility objUtility=new clsUtility();
        try
        {
            List<clsAccountDtl> accountInfo=objUtility.funGetAccountDtl("GL Code", "060.001");
            new frmSearchFormDialog(this, true,accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                String accCode=data[0].toString();
                funSetTip(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            objUtility=null;
        }
    }//GEN-LAST:event_txtTipMouseClicked

    private void txtRoundOffKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRoundOffKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoundOffKeyPressed

    private void txtRoundOffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRoundOffMouseClicked
        // TODO add your handling code here:
        clsUtility objUtility=new clsUtility();
        try
        {
            List<clsAccountDtl> accountInfo=objUtility.funGetAccountDtl("GL Code", "060.001");
            new frmSearchFormDialog(this, true,accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                String accCode=data[0].toString();
                funSetRoundOff(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            objUtility=null;
        }
    }//GEN-LAST:event_txtRoundOffMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAdvReceiptPrinterTest;
    private javax.swing.JButton btnAllReset1;
    private javax.swing.JButton btnBillPrinterTest;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset2;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkCounter;
    private javax.swing.JCheckBox chkDelayedSettlement;
    private javax.swing.JCheckBox chkOpeartionalYN;
    private javax.swing.JCheckBox chkPrintVatNo;
    private javax.swing.JCheckBox chkServiceTaxNo;
    private javax.swing.JComboBox cmbDebitCardTransactionYN;
    private javax.swing.JComboBox cmbFromAMPM;
    private javax.swing.JComboBox cmbFromHour;
    private javax.swing.JComboBox cmbFromMinute;
    private javax.swing.JComboBox cmbPosType;
    private javax.swing.JComboBox cmbToAMPM;
    private javax.swing.JComboBox cmbToHour;
    private javax.swing.JComboBox cmbToMinute;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAdvReceiptPrinterPort1;
    private javax.swing.JLabel lblBillPrinterPort;
    private javax.swing.JLabel lblCardTransaction;
    private javax.swing.JLabel lblCounterWiseBilling;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelayedSettlement;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperational;
    private javax.swing.JLabel lblPosCode;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPosName1;
    private javax.swing.JLabel lblPosName4;
    private javax.swing.JLabel lblPosType;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRoundOff;
    private javax.swing.JLabel lblTip;
    private javax.swing.JLabel lblToTime;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblfromTime;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelPOS;
    private javax.swing.JPanel panelReOrder;
    private javax.swing.JPanel panelSettlementDtl;
    private javax.swing.JPanel panelbody;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabPOSMaster;
    private javax.swing.JTable tblSettlementDtl;
    private javax.swing.JTable tblTimeTable;
    private javax.swing.JTextField txtAdvReceiptPrinterPort;
    private javax.swing.JTextField txtBillPrinterPort;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtPosCode;
    private javax.swing.JTextField txtPosName;
    private javax.swing.JTextField txtPropertyPOSCode;
    private javax.swing.JTextField txtRoundOff;
    private javax.swing.JTextField txtServiceTaxNo;
    private javax.swing.JTextField txtTip;
    private javax.swing.JTextField txtVatNo1;
    // End of variables declaration//GEN-END:variables

    private void funTestPrint(String printerName)
    {
        funCreateTempFolder();
        String filePath = System.getProperty("user.dir");
        String filename = (filePath + "/Temp/TestCCPrinter.txt");
        try 
        {
            File file=new File(filename);
            funCreateTestTextFile(file);
            clsTextFileGeneratorForPrinting fileGeneratorForPrinting=new clsTextFileGeneratorForPrinting();
            fileGeneratorForPrinting.funShowTextFile(file, "", "");
            
            int printerIndex = 0;
            String printerStatus="Not Found";
            
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            printerName=printerName.replaceAll("#", "\\\\");
            
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++) 
            {
                String printerServiceName=printService[i].getName();
                if (printerName.equalsIgnoreCase(printerServiceName)) 
                {
                    System.out.println("Printer="+printerName);
                    printerIndex = i;
                    printerStatus="Found";
                    break;
                }
            }
            
            if(printerStatus.equals("Found"))
            {
                DocPrintJob job = printService[printerIndex].createPrintJob();
                FileInputStream fis = new FileInputStream(filename);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
                
                PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
                for (Attribute a : att.toArray()) 
                {
                    String attributeName;
                    String attributeValue;
                    attributeName = a.getName();
                    attributeValue = att.get(a.getClass()).toString();
                    if(attributeName.trim().equalsIgnoreCase("queued-job-count"))
                    {
                        System.out.println(attributeName + " : " + attributeValue);
                    }
                }
            }
            else
            {                
                JOptionPane.showMessageDialog(null,printerName+" Printer Not Found");
            }            
            
        } catch (Exception e) {
            
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);            
        }
    }
    
    
     private void funCreateTempFolder()
    {        
        try
        {
            String filePath = System.getProperty("user.dir");
            File PrintText = new File(filePath + "/Temp");
            if (!PrintText.exists()) {
                PrintText.mkdirs();
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private void funCreateTestTextFile(File file)
    {
        BufferedWriter fileWriter=null;
        try 
        {
            //File file=new File(filename);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
            
            String fileHeader="----------Print Testing------------";
            String dottedLine="-----------------------------------";
            String newLine="\n";
            String blankLine="                                   ";
            
            fileWriter.write(fileHeader);
            fileWriter.newLine();
            fileWriter.write(dottedLine);
            fileWriter.newLine();
            fileWriter.write("User Name : "+clsGlobalVarClass.gUserName);
            fileWriter.newLine();
            fileWriter.write("POS Name : "+clsGlobalVarClass.gPOSName);
            fileWriter.newLine();  
            if(printerType.equalsIgnoreCase("BillPrinter"))
            {
                fileWriter.write("Bill Printer Name : "+txtBillPrinterPort.getText());
                fileWriter.newLine();
            }
            else if(printerType.equalsIgnoreCase("AdvancedReceiptPrinter"))
            {
                fileWriter.write("Advance Receipt Printer Name : "+txtAdvReceiptPrinterPort.getText());
                fileWriter.newLine();
            }
            else
            {
                fileWriter.write("Printer Name : ");
                fileWriter.newLine();
            }
                               
            fileWriter.write(dottedLine);
            
        }
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        } 
        catch (UnsupportedEncodingException ex) 
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
           ex.printStackTrace();
        }
        finally 
        {
            try 
            {
                fileWriter.close();
            } 
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        
    }
    
     private void funSetRoundOff(Object[] data)
     {
         String accCode=(String) data[0];
         System.out.println(accCode);
         txtRoundOff.setText(accCode);
       
     }
     
       private void funSetTip(Object[] data)
     {
         String accCode=(String) data[0];
         System.out.println(accCode);
         txtTip.setText(accCode);
       
     }
       
         private void funSetDiscount(Object[] data)
     {
         String accCode=(String) data[0];
         System.out.println(accCode);
         txtDiscount.setText(accCode);
       
     }
}
