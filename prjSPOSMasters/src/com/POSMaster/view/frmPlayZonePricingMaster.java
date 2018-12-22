/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsTextFieldOnlyNumber;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmMultiPOSSelection;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmPlayZonePricingMaster extends javax.swing.JFrame
{

    int intTimeStamp=0;
    boolean flagCopyPaste=false,flagCopyPasteGuest=false;
    String strSelectedItemCode;
    HashMap<String, String> hmCostCenterName;
    HashMap<String, String> hmCostCenterCode;
    HashMap<String, String> hmMenuHeadName;
    HashMap<String, String> hmMenuHeadCode;
    HashMap<String, String> hmPOSCode;
    private HashMap<String, String> hmAreaName;
    private HashMap<String, String> hmAreaCode;
    private Set<String> selectedPOSCodeSet;
    clsUtility objUtility = new clsUtility();   
    StringBuilder sbSql=new StringBuilder();
    String validFromDate="", validToDate="" ;
     
    /**
     * This method is used to initialize frmPlayZonePricingMaster
     */
    public frmPlayZonePricingMaster()
    {
        initComponents();
        try
        {
            hmCostCenterName=new HashMap<>();
            hmMenuHeadName=new HashMap<>();
            hmCostCenterCode=new HashMap<>();
            hmMenuHeadCode=new HashMap<>();
            hmPOSCode=new HashMap<>();
            hmAreaName = new HashMap<String, String>();
            hmAreaCode = new HashMap<String, String>();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            
            selectedPOSCodeSet = new HashSet<String>();
            java.util.Date dt = new java.util.Date();
            int day = dt.getDate();
            int month = dt.getMonth() + 1;
            int year = dt.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            funFillCombo();
            funSetShortCutKeys();
            date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            dteToDate.setDate(date);

            tblMember.getColumn("From Dur").setCellRenderer(
                new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, 
                                                               Object value, 
                                                               boolean isSelected, 
                                                               boolean hasFocus, 
                                                               int row, 
                                                               int column) {
                    setText(value.toString());
                    //setBackground();
                    return this;
                }
            });
            tblMember.getColumn("To Dur").setCellRenderer(
                new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, 
                                                               Object value, 
                                                               boolean isSelected, 
                                                               boolean hasFocus, 
                                                               int row, 
                                                               int column) {
                    setText(value.toString());
                    //setBackground();
                    return this;
                }
            });
            
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

     /**
     * get Selected Pos Code
     *
     * @return
     * @throws Exception
     */
    private String funGetSelectedPosCode() throws Exception
    {
        String pos = null;

        String posCode = cmbPosCode.getSelectedItem().toString();
        if (posCode.equalsIgnoreCase("Multiple"))
        {
            pos = posCode;
        }
        else
        {
            StringBuilder sb = new StringBuilder(posCode);
            int len = posCode.length();
            int lastInd = sb.lastIndexOf(" ");
            pos = sb.substring(lastInd + 1, len).toString();
        }

        return pos;
    }
    
    private void funSelectItemCode()
    {
        try
        {
            objUtility.funCallForSearchForm("MenuItemNoRaw");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetItemData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
     private void funSelectPlayZonePricingCode()
    {
        try
        {
            objUtility.funCallForSearchForm("PlayZonePricingMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");//UpdateD
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetPricingData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
     private void funFillCombo(){
         try{
             
                cmbTimeSlot.addItem("30");
                cmbTimeSlot.addItem("45");
                cmbTimeSlot.addItem("60");
                
                cmbPosCode.addItem("All");
                cmbPosCode.addItem("Multiple");
                sbSql.setLength(0);
                sbSql.append("select strPosName,strPosCode from tblposmaster where strOperationalYN='Y' ");
                ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
                while (rs.next())
                {
                    cmbPosCode.addItem(rs.getString(1) + " " + rs.getString(2));
                    hmPOSCode.put(rs.getString(2), rs.getString(1));
                }
                rs.close();
             
                sbSql.setLength(0);
                sbSql.append("select strCostCenterCode,strCostCenterName from tblcostcentermaster where strClientCode='"+clsGlobalVarClass.gClientCode+"';  ");
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
                while (rs.next())
                {
                   cmbCostCenter.addItem(rs.getString(2));
                   hmCostCenterName.put(rs.getString(2).trim(), rs.getString(1));
                   hmCostCenterCode.put(rs.getString(1).trim(), rs.getString(2));
                }
                rs.close();
                sbSql.setLength(0);
                sbSql.append("select strMenuCode,strMenuName from tblmenuhd where strClientCode='"+clsGlobalVarClass.gClientCode+"';  ");
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
                while (rs.next())
                {
                   cmbMenuHead.addItem(rs.getString(2));
                   hmMenuHeadName.put(rs.getString(2).trim(), rs.getString(1));
                   hmMenuHeadCode.put(rs.getString(1).trim(), rs.getString(2));
                }
                rs.close();
                
                cmbArea.removeAllItems();
                hmAreaName = new HashMap<String, String>();
                sbSql.setLength(0);
                sbSql.append("select * from tblareamaster where strClientCode ='"+clsGlobalVarClass.gClientCode+"' ");
                ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
                while(rsArea.next())
                {
                    hmAreaName.put(rsArea.getString(2), rsArea.getString(1));
                    hmAreaCode.put(rsArea.getString(1), rsArea.getString(2));
                    cmbArea.addItem(rsArea.getString(2));
                }
                rsArea.close();
                cmbArea.setSelectedItem("All");
         }catch(Exception e ){
             e.printStackTrace();
         }
     }
     
    
    private void funSetItemData(Object[] data){
        
     try{   
            sbSql.setLength(0);
            sbSql.append("select strItemCode,strItemName,strRevenueHead,strDiscountApply from tblitemmaster where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'");
            ResultSet rsItemData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
            if (rsItemData.next())
            {
                txtItemCode.setText(rsItemData.getString(2));
                strSelectedItemCode=rsItemData.getString(1);
                String disApply = rsItemData.getString(4).toString();
                if (disApply.equalsIgnoreCase("Y"))
                {
                    //chkDiscount.setSelected(true);
                }
                else
                {
                   // chkDiscount.setSelected(false);
                }
                cmbMenuHead.setSelectedItem(rsItemData.getString(3));

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void funSetPricingData(Object[] data){
        
        try{
            txtMemberRate.setText("0");
            txtGuestRate.setText("0");
            sbSql.setLength(0);
            sbSql.append("select a.strPosCode ,b.strItemName , a.intTimeStamp ,a.strMenuCode,a.strCostCenterCode,"
                    + "a.dteFromDate,a.dteToDate,a.intGracePeriod,a.strItemCode,a.strAreaCode from  tblplayzonepricinghd a left outer join tblitemmaster b on "
                    + " a.strItemCode=b.strItemCode where a.strPlayZonePricingCode='"+clsGlobalVarClass.gSearchedItem+"' and a.strClientCode='"+clsGlobalVarClass.gClientCode+"' ;");
            ResultSet rsData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
            if (rsData.next())
            {
                txtItemCode.setText(rsData.getString(2));
                cmbTimeSlot.setSelectedItem(rsData.getString(3));
                cmbMenuHead.setSelectedItem(hmMenuHeadCode.get(rsData.getString(4).trim()));
                cmbCostCenter.setSelectedItem(hmCostCenterCode.get(rsData.getString(5).trim()));
                txtPlayZonePricingCode.setText(clsGlobalVarClass.gSearchedItem);
                dteFromDate.setDate(rsData.getDate(6));
                dteToDate.setDate(rsData.getDate(7));
                txtGracePeriod.setText(String.valueOf(rsData.getInt(8)));
                cmbPosCode.setSelectedItem(hmPOSCode.get(rsData.getString(1))+" "+rsData.getString(1));
                strSelectedItemCode=rsData.getString(9);
                cmbArea.setSelectedItem(hmAreaCode.get(rsData.getString(10)));
            }
            rsData.close();
            sbSql.setLength(0);
            sbSql.append("select a.strPlayZonePricingCode,TIME_FORMAT(a.dteFromTime,'%H:%i'),TIME_FORMAT(a.dteToTime,'%H:%i'),"
                    + "a.dblMemberPriceSunday,a.dblMemberPriceMonday,a.dblMemberPriceTuesday,a.dblMemberPriceWednesday,"
                    + "a.dblMemberPriceThursday,a.dblMemberPriceFriday,a.dblMemberPriceSaturday,a.dblGuestPriceSunday,"
                    + "a.dblGuestPriceMonday,a.dblGuestPriceTuesday,a.dblGuestPriceWednesday,a.dblGuestPriceThursday,"
                    + "a.dblGuestPriceFriday,a.dblGuestPriceSaturday  from tblplayzonepricingdtl a where a.strPlayZonePricingCode ='"+clsGlobalVarClass.gSearchedItem+"'"); 
            rsData=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
            
            DefaultTableModel dmGuest = (DefaultTableModel) tblGuest.getModel();
            dmGuest.setRowCount(0);
            DefaultTableModel dmMember = (DefaultTableModel) tblMember.getModel();
            dmMember.setRowCount(0);
            while(rsData.next())
            {
                Object[] obGuest=
                    {
                      rsData.getDouble(11),rsData.getDouble(12),rsData.getDouble(13),
                      rsData.getDouble(14),rsData.getDouble(15),rsData.getDouble(16),
                      rsData.getDouble(17)
                    };
                     dmGuest.addRow(obGuest);
                     
                Object[] obMember=
                    {
                      rsData.getString(2),rsData.getString(3),rsData.getDouble(4),
                      rsData.getDouble(5),rsData.getDouble(6),rsData.getDouble(7),
                      rsData.getDouble(8),rsData.getDouble(9), rsData.getDouble(10)
                    };
                     dmMember.addRow(obMember);     
                     
           }
         tblGuest.setModel(dmGuest);
         tblMember.setModel(dmMember);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    //Apply Button on click
    private void funApplyValues(){
        
        if (!funCheckInt(txtGracePeriod.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Grace Period");
            return;
        }
        if (!objUtility.funCheckDouble(txtMemberRate.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Member Rate");
            return;
        }
        if (!objUtility.funCheckDouble(txtGuestRate.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Guest Rate");
            return;
        }
        if (Integer.parseInt(txtGracePeriod.getText())>60)
        {
            JOptionPane.showMessageDialog(this, "Invalid Grace Period");
            return;
        }
        if(txtItemCode.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Select Item");
            return;
        }
        
        java.util.Date objDate = dteFromDate.getDate();
        validFromDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                         + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

         objDate = dteToDate.getDate();
         validToDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

         Date dt1 = dteFromDate.getDate();
         Date dt2 = dteToDate.getDate();
         if ((dt2.getTime() - dt1.getTime()) < 0)
         {
             new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
             return;
         }
      
    }
    private void funCopyMember(){
        intTimeStamp=Integer.parseInt(cmbTimeSlot.getSelectedItem().toString());
        if (!objUtility.funCheckDouble(txtMemberRate.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Member Rate");
            return;
        }
         funFillMemberTable();
    }
    private void funCopyGuest(){
        intTimeStamp=Integer.parseInt(cmbTimeSlot.getSelectedItem().toString());
        if (!objUtility.funCheckDouble(txtGuestRate.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Guest Rate");
            return;
        }
          funFillGuestTable();
    }
     public boolean funCheckInt(String text)
    {
        boolean flg = false;
        try
        {
            int num = Integer.parseInt(text);
            flg = true;
        }
        catch (Exception e)
        {
            flg = false;
        }
        finally
        {
            return flg;
        }
    }
    private void funFillMemberTable(){
        try{
                DefaultTableModel dm1 = (DefaultTableModel) tblMember.getModel();
                dm1.setRowCount(0);
                int noOfRows=0;
                if(Integer.parseInt(cmbTimeSlot.getSelectedItem().toString())==30){
                    noOfRows=48;
                }
                else if(Integer.parseInt(cmbTimeSlot.getSelectedItem().toString())==45){
                    noOfRows=32;
                }else{
                    noOfRows=24;
                }
                
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date dateFrom =df.parse("00:01"); 
                Date dateTo=new Date();
                if(intTimeStamp==30){
                     dateTo =df.parse("00:30");
                }else if(intTimeStamp==45){
                    dateTo =df.parse("00:45");
                }else {
                    dateTo =df.parse("01:00");
                }
                
                Calendar calFrom = Calendar.getInstance();
                calFrom.setTime(dateFrom);
                
                Calendar calTo = Calendar.getInstance();
                calTo.setTime(dateTo);
                
                double dblMemberRate=Double.parseDouble(txtMemberRate.getText());
                
                for(int i=0;i<noOfRows;i++){
                    Object[] ob=
                    {
                        df.format(calFrom.getTime()),df.format(calTo.getTime()),dblMemberRate,dblMemberRate,dblMemberRate,
                        dblMemberRate,dblMemberRate,dblMemberRate,dblMemberRate
                    };
                     calTo.add(Calendar.MINUTE, intTimeStamp);
                     calFrom.add(Calendar.MINUTE, intTimeStamp);
                     dm1.addRow(ob);
                }
                tblMember.setModel(dm1);              
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void funFillGuestTable(){
        try{
                DefaultTableModel dm1 = (DefaultTableModel) tblGuest.getModel();
                dm1.setRowCount(0);
                int noOfRows=0;
                if(Integer.parseInt(cmbTimeSlot.getSelectedItem().toString())==30){
                    noOfRows=48;
                }
                else if(Integer.parseInt(cmbTimeSlot.getSelectedItem().toString())==45){
                    noOfRows=32;
                }
                else{
                    noOfRows=24;
                }
                double dblGuestRate=Double.parseDouble(txtGuestRate.getText());
                for(int i=0;i<noOfRows;i++){
                    Object[] ob=
                    {
                        dblGuestRate,dblGuestRate,dblGuestRate,dblGuestRate,
                        dblGuestRate,dblGuestRate,dblGuestRate
                    };
                     dm1.addRow(ob);
                }
                tblGuest.setModel(dm1);
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void funSavePlayZonePricingMaster(){
        try{
            
            if(cmbPosCode.getSelectedItem().toString().equalsIgnoreCase("All")){
                selectedPOSCodeSet=hmPOSCode.keySet();
            }
            if(selectedPOSCodeSet.size()>0){
                long lastNo=0;  
                StringBuilder sbsql=new StringBuilder();
                String playZoneCode="";
                for(String pos:selectedPOSCodeSet)
                {
                    if(funCheckIsItemAlreadyPriced(pos)==1){
                        new frmOkPopUp(this, "This Item is already priced, Please Expire old price", "Error ", 3).setVisible(true);
                        return;
                    }
                    lastNo=funGetPlayZoneCode();
                    playZoneCode = "PZ" + String.format("%07d", lastNo);
                    sbsql.setLength(0);
                    sbsql.append(" INSERT INTO tblplayzonepricinghd (strPlayZonePricingCode,strPosCode,strItemCode,strMenuCode,strCostCenterCode,strClientCode,intTimeStamp,intGracePeriod,dteFromDate,dteToDate,strUserCreated,strAreaCode ) "
                        + "VALUES ('" + playZoneCode + "', '" + pos + "', '" + strSelectedItemCode+ "'"
                        + ", '" + hmMenuHeadName.get(cmbMenuHead.getSelectedItem().toString().trim()) + "', '" + hmCostCenterName.get(cmbCostCenter.getSelectedItem().toString().trim()) + "'"
                        + ", '" + clsGlobalVarClass.gClientCode + "', " + cmbTimeSlot.getSelectedItem().toString() + "," + txtGracePeriod.getText()+ ",'" + validFromDate + "', '" + validToDate + "',"
                        + " '" + clsGlobalVarClass.gUserCode + "','"+hmAreaName.get(cmbArea.getSelectedItem().toString().trim()) +"')");

                    clsGlobalVarClass.dbMysql.execute(sbsql.toString());
                    
                    for(int rc=0;rc<tblMember.getRowCount();rc++){
                        sbsql.setLength(0);
                        sbsql.append(" INSERT INTO tblplayzonepricingdtl (strPlayZonePricingCode,dteFromTime,dteToTime,dblMemberPriceSunday,dblMemberPriceMonday,dblMemberPriceTuesday,dblMemberPriceWednesday,dblMemberPriceThursday,dblMemberPriceFriday,dblMemberPriceSaturday,"
                                + " dblGuestPriceSunday,dblGuestPriceMonday,dblGuestPriceTuesday,dblGuestPriceWednesday,dblGuestPriceThursday,dblGuestPriceFriday,dblGuestPriceSaturday,strClientCode ) "
                                + "VALUES ('" + playZoneCode + "', '" + tblMember.getValueAt(rc, 0).toString() + "', '" + tblMember.getValueAt(rc, 1).toString()+ "' , " + tblMember.getValueAt(rc, 2).toString()+ " "
                                + ", " +tblMember.getValueAt(rc, 3).toString()  + ", " +tblMember.getValueAt(rc, 4).toString()+ " ,"+tblMember.getValueAt(rc, 5).toString()+","+tblMember.getValueAt(rc, 6).toString()+" "
                                + ", " +tblMember.getValueAt(rc, 7).toString()+ ", " + tblMember.getValueAt(rc, 8).toString() + " ,"+tblGuest.getValueAt(rc, 0).toString()+","+tblGuest.getValueAt(rc, 1).toString()+","+tblGuest.getValueAt(rc, 2).toString()+""
                                + ", "+tblGuest.getValueAt(rc, 3).toString()+","+tblGuest.getValueAt(rc, 4).toString()+","+tblGuest.getValueAt(rc, 5).toString()+","+tblGuest.getValueAt(rc, 6).toString()+",'"+clsGlobalVarClass.gClientCode+"' )");
                    
                        clsGlobalVarClass.dbMysql.execute(sbsql.toString());
                    }
                }
                 new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                 funResetFields();
            }

            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void funUpdatePlayZonePricingMaster(){
        try{
            java.util.Date objDate = dteFromDate.getDate();
            validFromDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                         + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

            objDate = dteToDate.getDate();
            validToDate = ((objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate())
                        + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();
     
            Date dt1 = dteFromDate.getDate();
            Date dt2 = dteToDate.getDate();
            if ((dt2.getTime() - dt1.getTime()) < 0)
            {
                new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
                return;
            }
         
            if(cmbPosCode.getSelectedItem().toString().equalsIgnoreCase("All")){
                selectedPOSCodeSet=hmPOSCode.keySet();
            }
            if(selectedPOSCodeSet.size()>0){
                StringBuilder sbsql=new StringBuilder();
                for(String pos:selectedPOSCodeSet)
                {
                    sbsql.setLength(0);
                    sbsql.append("update tblplayzonepricinghd set strPosCode= '" + pos + "',strItemCode='" + strSelectedItemCode+ "',strMenuCode='" + hmMenuHeadName.get(cmbMenuHead.getSelectedItem().toString().trim()) + "',"
                            + "strCostCenterCode='" + hmCostCenterName.get(cmbCostCenter.getSelectedItem().toString().trim()) + "',intTimeStamp=" + cmbTimeSlot.getSelectedItem().toString() + ","
                            + "intGracePeriod=" + txtGracePeriod.getText()+ ",dteFromDate='" + validFromDate + "',dteToDate='" + validToDate + "',"
                            + "strAreaCode='"+hmAreaName.get(cmbArea.getSelectedItem().toString().trim()) +"' where strPlayZonePricingCode='"+txtPlayZonePricingCode.getText()+"'");

                    int exc = clsGlobalVarClass.dbMysql.execute(sbsql.toString());
                    
                    for(int rc=0;rc<tblMember.getRowCount();rc++){
                        sbsql.setLength(0);
                        sbsql.append(" update tblplayzonepricingdtl set "
                                + "dblMemberPriceSunday=" + tblMember.getValueAt(rc, 2).toString()+ ",dblMemberPriceMonday=" + tblMember.getValueAt(rc, 3).toString()+ ",dblMemberPriceTuesday=" + tblMember.getValueAt(rc, 4).toString()+ ","
                                + "dblMemberPriceWednesday=" + tblMember.getValueAt(rc, 5).toString()+ ",dblMemberPriceThursday=" + tblMember.getValueAt(rc, 6).toString()+ ",dblMemberPriceFriday=" + tblMember.getValueAt(rc, 7).toString()+ ","
                                + "dblMemberPriceSaturday=" + tblMember.getValueAt(rc, 8).toString()+ ","
                                + " dblGuestPriceSunday="+tblGuest.getValueAt(rc, 0).toString()+",dblGuestPriceMonday="+tblGuest.getValueAt(rc, 1).toString()+",dblGuestPriceTuesday="+tblGuest.getValueAt(rc, 2).toString()+",dblGuestPriceWednesday="+tblGuest.getValueAt(rc, 3).toString()+","
                                + "dblGuestPriceThursday="+tblGuest.getValueAt(rc, 4).toString()+",dblGuestPriceFriday="+tblGuest.getValueAt(rc, 5).toString()+",dblGuestPriceSaturday="+tblGuest.getValueAt(rc, 6).toString()+""
                                + " where strPlayZonePricingCode='"+txtPlayZonePricingCode.getText()+"' "
                                + " and dteFromTime= '"+ tblMember.getValueAt(rc, 0).toString()+"' and dteToTime='"+ tblMember.getValueAt(rc, 1).toString()+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"' LIMIT 1;");
                                
                        exc =  clsGlobalVarClass.dbMysql.execute(sbsql.toString());
                    }
                }
                 new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                 funResetFields();
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private int funCheckIsItemAlreadyPriced(String posCode) throws SQLException{
        int count=0;
        ResultSet rsItem=null;
        try{
            
            String sql="select count(*) ,a.strPlayZonePricingCode\n" +
                       " from tblplayzonepricinghd  a where a.strItemCode='"+strSelectedItemCode+"' "
                     + " and a.strPosCode='"+posCode+"' and date(a.dteToDate) >='"+validFromDate+"';";
            rsItem = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsItem.next()){
                count=rsItem.getInt(1);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            return count;
        }
        finally{
            rsItem.close();
        }
        return count;
    }
    
    
    private long funGetPlayZoneCode(){
        long lastNo=1;
        try
        {
            String sql = "select count(dblLastNo) from tblinternal where strTransactionType='PlayZonePricing'";
            ResultSet rsCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCode.next();
            int cntPlayZone = rsCode.getInt(1);
            rsCode.close();
            if (cntPlayZone > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='PlayZonePricing'";
                rsCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCode.next();
                long code = rsCode.getLong(1);
                code = code + 1;
                lastNo = code;
                rsCode.close();
            }
            else
            {
                lastNo = 1;
            }
            String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
                    + "where strTransactionType='PlayZonePricing'";
            clsGlobalVarClass.dbMysql.execute(updateSql);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
        
        return lastNo;
    }
    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

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
        lblMeniItemName = new javax.swing.JLabel();
        scrollPaneMenuHeads = new javax.swing.JScrollPane();
        tblMember = new javax.swing.JTable();
        scrollPaneItems = new javax.swing.JScrollPane();
        tblGuest = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        dteToDate = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        lblMeniItemName2 = new javax.swing.JLabel();
        cmbMenuHead = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbCostCenter = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnGuest = new javax.swing.JButton();
        cmbTimeSlot = new javax.swing.JComboBox();
        txtPlayZonePricingCode = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtGracePeriod = new javax.swing.JTextField();
        lbGracePeriod = new javax.swing.JLabel();
        txtMemberRate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtGuestRate = new javax.swing.JTextField();
        btnMember = new javax.swing.JButton();
        cmbArea = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
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

        lblProductName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName1.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName1.setText("SPOS - ");
        panelHeader.add(lblProductName1);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Play Zone Pricing Master");
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

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Play Zone Pricing Master");

        lblMeniItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMeniItemName.setText("Menu Item     :");

        tblMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "From Dur", "To Dur", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblMember.setRowHeight(25);
        tblMember.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblMemberMouseClicked(evt);
            }
        });
        tblMember.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblMemberKeyPressed(evt);
            }
        });
        scrollPaneMenuHeads.setViewportView(tblMember);

        scrollPaneItems.setBackground(new java.awt.Color(255, 255, 255));

        tblGuest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
            }
        ));
        tblGuest.setRowHeight(25);
        tblGuest.setRowMargin(2);
        tblGuest.getTableHeader().setReorderingAllowed(false);
        tblGuest.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblGuestMouseClicked(evt);
            }
        });
        tblGuest.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblGuestKeyPressed(evt);
            }
        });
        scrollPaneItems.setViewportView(tblGuest);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Modifier Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Modifier Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Menu Head  :");

        txtItemCode.setEditable(false);
        txtItemCode.setBackground(new java.awt.Color(204, 204, 204));
        txtItemCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemCodeMouseClicked(evt);
            }
        });
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemCodeKeyPressed(evt);
            }
        });

        dteFromDate.setBackground(new java.awt.Color(216, 216, 216));
        dteFromDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteFromDateKeyPressed(evt);
            }
        });

        dteToDate.setBackground(new java.awt.Color(216, 216, 216));
        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("From Date          :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("To Date      :");

        cmbPosCode.setToolTipText("Select POS");
        cmbPosCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPosCodeActionPerformed(evt);
            }
        });

        lblMeniItemName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMeniItemName2.setText("Time Slot       :");

        cmbMenuHead.setToolTipText("Select POS");
        cmbMenuHead.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMenuHeadActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("POS  :");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Cost Center        :");

        cmbCostCenter.setToolTipText("Select POS");
        cmbCostCenter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCostCenterActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Member");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Additional Guest");

        btnGuest.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGuest.setForeground(new java.awt.Color(255, 255, 255));
        btnGuest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnGuest.setText("Copy");
        btnGuest.setToolTipText("Apply Rate");
        btnGuest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuest.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnGuest.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnGuestMouseClicked(evt);
            }
        });
        btnGuest.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGuestActionPerformed(evt);
            }
        });
        btnGuest.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnGuestKeyPressed(evt);
            }
        });

        cmbTimeSlot.setToolTipText("time slot");
        cmbTimeSlot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTimeSlotActionPerformed(evt);
            }
        });

        txtPlayZonePricingCode.setEditable(false);
        txtPlayZonePricingCode.setBackground(new java.awt.Color(204, 204, 204));
        txtPlayZonePricingCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPlayZonePricingCodeMouseClicked(evt);
            }
        });
        txtPlayZonePricingCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPlayZonePricingCodeKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Play Zone Pricing :");

        txtGracePeriod.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGracePeriod.setText("0");
        txtGracePeriod.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGracePeriodMouseClicked(evt);
            }
        });
        txtGracePeriod.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGracePeriodKeyPressed(evt);
            }
        });

        lbGracePeriod.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbGracePeriod.setText("Grace Period   :");

        txtMemberRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMemberRate.setText("0");
        txtMemberRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMemberRateMouseClicked(evt);
            }
        });
        txtMemberRate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMemberRateKeyPressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Set Member Rate:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Guest  Rate    :");

        txtGuestRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGuestRate.setText("0");
        txtGuestRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGuestRateMouseClicked(evt);
            }
        });
        txtGuestRate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGuestRateKeyPressed(evt);
            }
        });

        btnMember.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMember.setForeground(new java.awt.Color(255, 255, 255));
        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnMember.setText("Copy");
        btnMember.setToolTipText("Apply Rate");
        btnMember.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMember.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnMember.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMemberMouseClicked(evt);
            }
        });
        btnMember.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnMemberKeyPressed(evt);
            }
        });

        cmbArea.setToolTipText("Select POS");
        cmbArea.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAreaActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Area           :");

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addComponent(scrollPaneMenuHeads, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(169, 169, 169)
                                .addComponent(btnGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10))
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(scrollPaneItems, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(73, Short.MAX_VALUE))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                                .addComponent(btnMember, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                    .addComponent(txtMemberRate, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelbodyLayout.createSequentialGroup()
                                    .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cmbMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelbodyLayout.createSequentialGroup()
                                    .addComponent(txtPlayZonePricingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMeniItemName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addComponent(lblMeniItemName)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lbGracePeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTimeSlot, 0, 119, Short.MAX_VALUE)
                            .addComponent(txtItemCode)
                            .addComponent(txtGracePeriod)
                            .addComponent(txtGuestRate))
                        .addGap(144, 144, 144))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(lblFormName)
                .addGap(12, 12, 12)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPlayZonePricingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(cmbMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMeniItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTimeSlot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMeniItemName2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGracePeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbGracePeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGuestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(btnGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtMemberRate)
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11)
                                .addComponent(jLabel9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnMember, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollPaneItems, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(scrollPaneMenuHeads, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("PlayZone Pricing Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("PlayZone Pricing Master");
    }//GEN-LAST:event_formWindowClosing

    private void cmbCostCenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCostCenterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCostCenterActionPerformed

    private void cmbMenuHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMenuHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMenuHeadActionPerformed

    private void cmbPosCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPosCodeActionPerformed

        try
        {
            String posCode = funGetSelectedPosCode();
            if (posCode.equalsIgnoreCase("Multiple"))
            {
                frmMultiPOSSelection objMultiPOSSelection = new frmMultiPOSSelection(this);
                selectedPOSCodeSet = objMultiPOSSelection.funGetSelectedPOSCode();
            }
            else
            {
                selectedPOSCodeSet.clear();
                selectedPOSCodeSet.add(posCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbPosCodeActionPerformed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteToDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_dteToDateKeyPressed

    private void dteFromDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteFromDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteToDate.requestFocus();
        }
    }//GEN-LAST:event_dteFromDateKeyPressed

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
        // TODO add your handling code here:
        funSelectItemCode();
       
    }//GEN-LAST:event_txtItemCodeMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("PlayZone Pricing Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("PlayZone Pricing Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if(!(tblGuest.getModel().getRowCount()>=24||tblMember.getModel().getRowCount()>=24)){
            JOptionPane.showMessageDialog(this, "first fill pricing table ");
            return;
        }
        if(!(tblGuest.getModel().getRowCount()==tblMember.getModel().getRowCount())){
            JOptionPane.showMessageDialog(this, "Table row count not match");
            return;
        }
        funApplyValues();
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSavePlayZonePricingMaster();
            }
            else
            {
                funUpdatePlayZonePricingMaster();
            }
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
    
    }//GEN-LAST:event_btnNewMouseClicked

    private void tblGuestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuestMouseClicked
       int selectedRow=tblGuest.getSelectedRow();
       tblMember.setRowSelectionInterval(selectedRow, selectedRow);
       
    }//GEN-LAST:event_tblGuestMouseClicked

    private void tblMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberMouseClicked
       int selectedRow=tblMember.getSelectedRow();
       tblGuest.setRowSelectionInterval(selectedRow, selectedRow);
       
       
    }//GEN-LAST:event_tblMemberMouseClicked

    private void cmbTimeSlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTimeSlotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTimeSlotActionPerformed

    private void txtPlayZonePricingCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPlayZonePricingCodeMouseClicked
        // TODO add your handling code here:
        funSelectPlayZonePricingCode();
    }//GEN-LAST:event_txtPlayZonePricingCodeMouseClicked

    private void txtPlayZonePricingCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlayZonePricingCodeKeyPressed
        // TODO add your handling code here:
      //  funSelectPlayZonePricingCode();
    }//GEN-LAST:event_txtPlayZonePricingCodeKeyPressed

    private void txtGracePeriodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGracePeriodMouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtGracePeriodMouseClicked

    private void txtGracePeriodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGracePeriodKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnGuest.requestFocus();
        }
    }//GEN-LAST:event_txtGracePeriodKeyPressed

    private void tblMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblMemberKeyPressed
        // TODO add your handling code here: tblMember.getSelectionModel()
       
        if(tblMember.getSelectedColumn()<tblMember.getColumnCount()-1)
        {
           if(evt.getKeyCode()==KeyEvent.VK_TAB)
                {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult=0;
                    if(!flagCopyPaste){
                        dialogResult = JOptionPane.showConfirmDialog(null, "Copy Paste data ??", "Copy", dialogButton);
                    }    
                    if(dialogResult == 0) {
                        flagCopyPaste=true;
                        String cellValue=String.valueOf(tblMember.getValueAt(tblMember.getSelectedRow(), tblMember.getSelectedColumn()));
                        if(!cellValue.contains(":"))
                            tblMember.setValueAt(cellValue,tblMember.getSelectedRow(), tblMember.getSelectedColumn()+1);
                        
                    } else {
                        return;
                    } 
                } 
        }
        else{
            flagCopyPaste=false;
        }

    }//GEN-LAST:event_tblMemberKeyPressed

    private void tblGuestKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGuestKeyPressed
        // TODO add your handling code here:
        
        if(tblGuest.getSelectedColumn()<tblGuest.getColumnCount()-1)
        {
           if(evt.getKeyCode()==KeyEvent.VK_TAB)
                {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult=0;
                    if(!flagCopyPasteGuest){
                        dialogResult = JOptionPane.showConfirmDialog(null, "Copy Paste data ??", "Copy", dialogButton);
                    }    
                    if(dialogResult == 0) {
                        flagCopyPasteGuest=true;
                        String cellValue=String.valueOf(tblGuest.getValueAt(tblGuest.getSelectedRow(), tblGuest.getSelectedColumn()));
                        tblGuest.setValueAt(cellValue,tblGuest.getSelectedRow(), tblGuest.getSelectedColumn()+1);
                    } else {
                        return;
                    } 
                } 
        }
        else{
            flagCopyPasteGuest=false;
        }
    }//GEN-LAST:event_tblGuestKeyPressed

    private void txtMemberRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMemberRateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberRateMouseClicked

    private void txtMemberRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMemberRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberRateKeyPressed

    private void txtGuestRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGuestRateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGuestRateMouseClicked

    private void txtGuestRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGuestRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGuestRateKeyPressed

    private void btnGuestKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuestKeyPressed
        // TODO add your handling code here:
        //funApplyValues();
    }//GEN-LAST:event_btnGuestKeyPressed

    private void btnGuestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuestMouseClicked
        // TODO add your handling code here:
        // funApplyValues();
    }//GEN-LAST:event_btnGuestMouseClicked

    private void btnMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMemberMouseClicked
        // TODO add your handling code here:
        funCopyMember();
    }//GEN-LAST:event_btnMemberMouseClicked

    private void btnMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnMemberKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMemberKeyPressed

    private void btnGuestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuestActionPerformed
        // TODO add your handling code here:
        funCopyGuest();
    }//GEN-LAST:event_btnGuestActionPerformed

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAreaActionPerformed

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('u');
            txtItemCode.setText("");
            txtPlayZonePricingCode.setText("");
            cmbPosCode.setSelectedItem("");
            cmbMenuHead.setSelectedItem("");
            cmbCostCenter.setSelectedItem("");
            cmbTimeSlot.setSelectedItem("");
            cmbArea.setSelectedItem("All");
            txtGracePeriod.setText("0");
            txtMemberRate.setText("0");
            txtGuestRate.setText("0");
            DefaultTableModel dm = (DefaultTableModel) tblGuest.getModel();
            dm.setRowCount(0);
            dm = (DefaultTableModel) tblMember.getModel();
            dm.setRowCount(0);
           
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnGuest;
    private javax.swing.JButton btnMember;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbCostCenter;
    private javax.swing.JComboBox cmbMenuHead;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbTimeSlot;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbGracePeriod;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMeniItemName;
    private javax.swing.JLabel lblMeniItemName2;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName1;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JScrollPane scrollPaneItems;
    private javax.swing.JScrollPane scrollPaneMenuHeads;
    private javax.swing.JTable tblGuest;
    private javax.swing.JTable tblMember;
    private javax.swing.JTextField txtGracePeriod;
    private javax.swing.JTextField txtGuestRate;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtMemberRate;
    private javax.swing.JTextField txtPlayZonePricingCode;
    // End of variables declaration//GEN-END:variables
}
