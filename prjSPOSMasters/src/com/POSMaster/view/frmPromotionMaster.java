/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmPromotionMaster extends javax.swing.JFrame
{
    private String sql;
    private String promoItemCode = "", promoItemCodeGet = "";
    private clsUtility obj;
    private Map<String, String> hmPOS,hmArea;
    private JLabel lblModuleName1;
    clsUtility objUtility = new clsUtility();
    
    public frmPromotionMaster()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        try
        {
            java.util.Date posDate = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName1 = new JLabel();
            dteFromDate.setDate(posDate);
            lblModuleName1.setText(clsGlobalVarClass.gSelectedModule);
            dteToDate.setDate(clsGlobalVarClass.funGetCalenderToDate(1));
            txtPromoName.requestFocus();
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            obj = new clsUtility();
            funSetShortCutKeys();
            hmPOS = new HashMap<String, String>();
            hmArea = new HashMap<String, String>();
            funFillPOSCombo();
            tblPromoDayTime.setRowHeight(30);
        }
        catch (Exception ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

// Fill POS Combo    
    private void funFillPOSCombo() throws Exception
    {
        hmPOS.clear();
        cmbPOS.removeAllItems();
        hmPOS.put("All", "All");
        cmbPOS.addItem("All");
        sql = "select strPOSCode,strPOSName from tblposmaster";
        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsPOS.next())
        {
            hmPOS.put(rsPOS.getString(2), rsPOS.getString(1));
            cmbPOS.addItem(rsPOS.getString(2));
        }
        rsPOS.close();
        
        funFillAreaCombo(hmPOS.get(cmbPOS.getSelectedItem()));
    }

// Fill Area Combo    
    private void funFillAreaCombo(String POSCode) throws Exception
    {
        hmArea.clear();
        cmbArea.removeAllItems();
        sql = "select strAreaCode,strAreaName from tblareamaster where (strPOSCode='"+POSCode+"' or strPOSCode='All') order by strAreaName";
        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsPOS.next())
        {
            hmArea.put(rsPOS.getString(2), rsPOS.getString(1));
            cmbArea.addItem(rsPOS.getString(2));
        }
        rsPOS.close();
        cmbArea.setSelectedItem("All");
    }
    
    
    /**
     * This method is used to get help
     */
    private void funHelp()
    {
        try
        {
            if (cmbBuyPromotionOn.getSelectedItem().equals("Item"))
            {
                obj.funCallForSearchForm("MenuItem");
                new frmSearchFormDialog(null, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    funSetItem(data);
                }
            }
            else if (cmbBuyPromotionOn.getSelectedItem().equals("MenuHead"))
            {
                obj.funCallForSearchForm("Menu");
                new frmSearchFormDialog(null, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    funSetMenuCode(data);
                }
            }
            else if (cmbBuyPromotionOn.getSelectedItem().equals("BillAmount"))
            {
                txtBuyItemName.setText("");
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to help table
     */
    private void funHelpTable()
    {
        try
        {
            if (cmbGetPromotionOn.getSelectedItem().equals("Item"))
            {
                obj.funCallForSearchForm("MenuItem");
                new frmSearchFormDialog(null, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    funSetItemTable(data);
                }
            }
            else if (cmbGetPromotionOn.getSelectedItem().equals("MenuHead"))
            {
                obj.funCallForSearchForm("Menu");
                new frmSearchFormDialog(null, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    funSetMenuCodeTable(data);
                }
            }
            else if (cmbGetPromotionOn.getSelectedItem().equals("BillAmount"))
            {
                txtGetItemName.setText("");
            }
            else if(cmbGetPromotionOn.getSelectedItem().equals("PromoGroup"))
            {
                obj.funCallForSearchForm("PromoGroupMaster");
                new frmSearchFormDialog(this, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    funSetPromoGroupMasterData(data);
                    clsGlobalVarClass.gSearchItemClicked = false;
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    
    private void funSetPromoGroupMasterData(Object[] data) throws Exception
    {
        promoItemCodeGet = data[0].toString();
        txtGetItemName.setText(data[1].toString());
        funFillGetMenuItemGrid(data[0].toString(),"PromoGroupWise");
        
    }
    

    /**
     * This method is used to set item table
     *
     * @param data
     * @throws Exception
     */
    private void funSetItemTable(Object[] data) throws Exception
    {
        sql = "select strItemCode,strItemName from tblitemmaster "
                + "where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            promoItemCodeGet = rsItemInfo.getString(1);
            txtGetItemName.setText(rsItemInfo.getString(2));
        }
        rsItemInfo.close();
    }

    /**
     * This method is used to set menu code table
     *
     * @param data
     * @throws Exception
     */
    private void funSetMenuCodeTable(Object[] data) throws Exception
    {
        sql = "select strMenuCode,strMenuName from tblmenuhd "
                + "where strMenuCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            promoItemCodeGet = rsItemInfo.getString(1);
            txtGetItemName.setText(rsItemInfo.getString(2));
            funFillGetMenuItemGrid(rsItemInfo.getString(1),"MenuHeadWise");
        }
        rsItemInfo.close();
    }

    /**
     * This method is used to set item
     *
     * @param data
     * @throws Exception
     */
    private void funSetItem(Object[] data) throws Exception
    {
        sql = "select strItemCode,strItemName from tblitemmaster "
                + "where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            promoItemCode = rsItemInfo.getString(1);
            txtBuyItemName.setText(rsItemInfo.getString(2));
        }
        rsItemInfo.close();
    }

    private int funFillBuyMenuItemGrid(String menuCode) throws Exception
    {
        DefaultTableModel dmMenuItems = (DefaultTableModel) tblBuyMenuItems.getModel();
        dmMenuItems.setRowCount(0);
        sql = "select distinct strItemCode,strItemName "
                + "from tblmenuitempricingdtl where strMenuCode='" + menuCode + "' ";
        ResultSet rsMenuItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsMenuItems.next())
        {
            sql = "select distinct strPriceSunday from tblmenuitempricingdtl where strItemCode='" + rsMenuItems.getString(1) + "';";
            ResultSet rsPrice = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsPrice.next())
            {
                Object[] arrObjMenuItems =
                {
                    rsMenuItems.getString(2), rsPrice.getString(1), true, rsMenuItems.getString(1)
                };
                dmMenuItems.addRow(arrObjMenuItems);
            }
            rsPrice.close();
        }
        rsMenuItems.close();
        tblBuyMenuItems.setModel(dmMenuItems);

        return 1;
    }

    private int funFillGetMenuItemGrid(String code,String promoType) throws Exception
    {
        DefaultTableModel dmMenuItems = (DefaultTableModel) tblGetMenuItems.getModel();
        dmMenuItems.setRowCount(0);
        
        if(promoType.equals("MenuHeadWise"))
        {
            sql = "select distinct strItemCode,strItemName "
                    + "from tblmenuitempricingdtl where strMenuCode='" + code + "' ";
            ResultSet rsMenuItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsMenuItems.next())
            {
                sql = "select distinct strPriceSunday from tblmenuitempricingdtl where strItemCode='" + rsMenuItems.getString(1) + "';";
                ResultSet rsPrice = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsPrice.next())
                {
                    Object[] arrObjMenuItems =
                    {
                        rsMenuItems.getString(2), rsPrice.getString(1), true, rsMenuItems.getString(1)
                    };
                    dmMenuItems.addRow(arrObjMenuItems);
                }
                rsPrice.close();
            }
            rsMenuItems.close();
            tblGetMenuItems.setModel(dmMenuItems);
        }
        else if(promoType.equals("PromoGroupWise"))
        {
            sql = "select a.strItemCode,a.strItemName "
                + " from tblpromogroupdtl z,tblitemmaster a "
                + " where z.strItemCode=a.strItemCode and z.strPromoGroupCode='"+code+"' "
                + " and z.strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
            ResultSet rsPGItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPGItems.next())
            {
                sql = "select distinct strPriceSunday from tblmenuitempricingdtl where strItemCode='" + rsPGItems.getString(1) + "';";
                ResultSet rsPrice = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsPrice.next())
                {
                    Object[] arrObjMenuItems =
                    {
                        rsPGItems.getString(2), rsPrice.getString(1), true, rsPGItems.getString(1)
                    };
                    dmMenuItems.addRow(arrObjMenuItems);
                }
                rsPrice.close();
            }
            rsPGItems.close();
            tblGetMenuItems.setModel(dmMenuItems);
        }
        return 1;
    }

    private void funFillPromoDayTimeTable()
    {
        if (cmbFromHour.getSelectedItem() == "HH")
        {
            JOptionPane.showMessageDialog(this, "Please Enter vaild Hour in From Time");
            return;
        }
        if (cmbFromMinute.getSelectedItem() == "MM")
        {
            JOptionPane.showMessageDialog(this, "Please Enter vaild Minute in From Time");
            return;
        }
        if (cmbToHour.getSelectedItem() == "HH")
        {
            JOptionPane.showMessageDialog(this, "Please Enter vaild Hour in To Time");
            return;
        }
        if (cmbToMinute.getSelectedItem() == "MM")
        {
            JOptionPane.showMessageDialog(this, "Please Enter vaild Minute in TO Time");
            return;
        }
        if (cmbFromAMPM.getSelectedItem().toString().equalsIgnoreCase(cmbToAMPM.getSelectedItem().toString()))
        {
            if (cmbFromHour.getSelectedItem().toString().equals("12") && cmbFromAMPM.getSelectedItem().toString().equals("AM"))
            {
                JOptionPane.showMessageDialog(this, "Please Select 00 instead of 12 in case of AM");
                return;
            }
            if (cmbToHour.getSelectedItem().toString().equals("12") && cmbToAMPM.getSelectedItem().toString().equals("AM"))
            {
                JOptionPane.showMessageDialog(this, "Please Select 00 instead of 12 in case of AM");
                return;
            }

            if (cmbFromHour.getSelectedItem().toString().equals("00") && cmbFromAMPM.getSelectedItem().toString().equals("PM"))
            {
                JOptionPane.showMessageDialog(this, "Please Select 12 instead of 00 in case of PM");
                return;
            }
            if (cmbToHour.getSelectedItem().toString().equals("00") && cmbToAMPM.getSelectedItem().toString().equals("PM"))
            {
                JOptionPane.showMessageDialog(this, "Please Select 12 instead of 00 in case of PM");
                return;
            }

            Date dt = new Date();
            String currentDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            String strFromTime = cmbFromHour.getSelectedItem().toString() + ":" + cmbFromMinute.getSelectedItem().toString() + ":" + cmbFromAMPM.getSelectedItem().toString();
            String strToTime = cmbToHour.getSelectedItem().toString() + ":" + cmbToMinute.getSelectedItem().toString() + ":" + cmbToAMPM.getSelectedItem().toString();

            //String fromTime = currentDate + " " + funConvertTime(strFromTime);
            //String toTime = currentDate + " " + funConvertTime(strToTime);
            
            String fromTime = currentDate + " " + strFromTime;
            String toTime = currentDate + " " + strToTime;
            clsUtility objUtility = new clsUtility();
            long diff1 = objUtility.funCompareTime(fromTime, toTime);
            if (diff1 <= 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter vaild TO Time");
                return;
            }
        }

        String fromTime = cmbFromHour.getSelectedItem() + ":" + cmbFromMinute.getSelectedItem() + ":" + cmbFromAMPM.getSelectedItem();
        String toTime = cmbToHour.getSelectedItem() + ":" + cmbToMinute.getSelectedItem() + ":" + cmbToAMPM.getSelectedItem();
        String day = cmbDays.getSelectedItem().toString();

        if (day.equalsIgnoreCase("All"))
        {
            DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
            //dmPromoDayTime.setRowCount(0);

            String[] days =
            {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            };
            for (int i = 0; i < days.length; i++)
            {
                day = days[i];
                boolean flgStatus = true;
                for (int cnt = 0; cnt < tblPromoDayTime.getRowCount(); cnt++)
                {
                    if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 1).toString().equals(fromTime) && tblPromoDayTime.getValueAt(cnt, 2).toString().equals(toTime))
                    {
                        flgStatus = false;
                        JOptionPane.showMessageDialog(null, "Day with from time and to time is already present in grid!!!");
                        break;
                    }
                    else if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 1).toString().equals(fromTime))
                    {
                        flgStatus = false;
                        JOptionPane.showMessageDialog(null, "Day with from time is already present in grid!!!");
                        break;
                    }
                    else if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 2).toString().equals(toTime))
                    {
                        flgStatus = false;
                        JOptionPane.showMessageDialog(null, "Day with from time is already present in grid!!!");
                        break;
                    }
                    else
                    {
                        flgStatus = true;
                    }
                }

                if (flgStatus)
                {
                    Object[] arrObj =
                    {
                        day, fromTime, toTime, false
                    };
                    dmPromoDayTime.addRow(arrObj);
                    tblPromoDayTime.setModel(dmPromoDayTime);
                }
            }
        }
        else
        {
            boolean flgStatus = true;
            for (int cnt = 0; cnt < tblPromoDayTime.getRowCount(); cnt++)
            {
                if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 1).toString().equals(fromTime) && tblPromoDayTime.getValueAt(cnt, 2).toString().equals(toTime))
                {
                    flgStatus = false;
                    JOptionPane.showMessageDialog(null, "Day with from time and to time is already present in grid!!!");
                    break;
                }
                else if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 1).toString().equals(fromTime))
                {
                    flgStatus = false;
                    JOptionPane.showMessageDialog(null, "Day with from time is already present in grid!!!");
                    break;
                }
                else if (tblPromoDayTime.getValueAt(cnt, 0).toString().equals(day) && tblPromoDayTime.getValueAt(cnt, 2).toString().equals(toTime))
                {
                    flgStatus = false;
                    JOptionPane.showMessageDialog(null, "Day with from time is already present in grid!!!");
                    break;
                }
                else
                {
                    flgStatus = true;
                }
            }

            if (flgStatus)
            {
                DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
                Object[] arrObj =
                {
                    day, fromTime, toTime, false
                };
                dmPromoDayTime.addRow(arrObj);
                tblPromoDayTime.setModel(dmPromoDayTime);
            }
        }
    }

    private String funConvertTime(String time)
    {
        String[] arrTime = time.split(":");
        String convertedTime = "";
        int hr = Integer.parseInt(arrTime[0]);
        String min = arrTime[1].split(" ")[0];
        String ampm = arrTime[1].split(" ")[1];

        if (hr == 12)
        {
            if (ampm.equals("AM"))
            {
                hr += 12;
            }
        }
        else
        {
            if (ampm.equals("PM"))
            {
                hr += 12;
            }
        }
        String hours = String.valueOf(hr);
        if (hr < 10)
        {
            hours = "0" + hours;
        }
        convertedTime = hours + ":" + min + ":00";
        return convertedTime;
    }

    private void funRemoveRowFromPromoDayTimeTable()
    {
        DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
        for (int cnt = 0; cnt < tblPromoDayTime.getRowCount(); cnt++)
        {
            if (Boolean.parseBoolean(tblPromoDayTime.getValueAt(cnt, 3).toString()))
            {
                dmPromoDayTime.removeRow(cnt);
            }
        }
        tblPromoDayTime.setModel(dmPromoDayTime);
    }

    private void funRsetPromoDayTimeGrid()
    {
        int res = JOptionPane.showConfirmDialog(null, "Do you want to reset table?");
        if (res == 0)
        {
            DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
            dmPromoDayTime.setRowCount(0);
            tblPromoDayTime.setModel(dmPromoDayTime);
        }
    }

    /**
     * This method is used to set menu code
     *
     * @param data
     * @throws Exception
     */
    private void funSetMenuCode(Object[] data) throws Exception
    {
        sql = "select strMenuCode,strMenuName from tblmenuhd "
                + "where strMenuCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            promoItemCode = rsItemInfo.getString(1);
            funFillBuyMenuItemGrid(rsItemInfo.getString(1));
            txtBuyItemName.setText(rsItemInfo.getString(2));
        }
        rsItemInfo.close();
    }

    
    private void funCheckPromoGroupType()
    {
        if(cmbPGType.getSelectedItem().toString().equalsIgnoreCase("Limited"))
        {
            txtGetQty.setEnabled(true);
            txtGetQty.setText("");
        }
        else
        {
            txtGetQty.setEnabled(false);
            txtGetQty.setText("0");
        }
    }
    

    /**
     * This method is used to help for promotion code
     */
    private void funHelpForPromoCode()
    {
        try
        {
            obj.funCallForSearchForm("PromoCode");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetPromoData(data[0].toString());
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * set promotion data
     *
     * @param promoCode
     * @throws Exception
     */
    private void funSetPromoData(String promoCode) throws Exception
    {
        String buyItemCode = "";
        String sql = "";
        String sqlDtl1 = "select strPromotionOn from tblpromotionmaster "
                + "where strPromoCode='" + promoCode + "'";
        ResultSet rsTemp1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl1);
        if (rsTemp1.next())
        {
            if (rsTemp1.getString(1).equals("MenuHead"))
            {
                sql = "select a.*,b.strMenuName,ifnull(c.strPosName,'All') "
                        + " from tblpromotionmaster a left outer join tblmenuhd b on a.strPromoItemCode=b.strMenuCode "
                        + " left outer join tblposmaster c on a.strPOSCode=c.strPosCode "
                        + " where a.strPromoCode='" + promoCode + "'";
            }
            else if (rsTemp1.getString(1).equals("PromoGroup"))
            {
                sql = "select a.*,b.strItemName,ifnull(c.strPosName,'All') "
                        + " from tblpromotionmaster a left outer join tblitemmaster b on a.strPromoItemCode=b.strItemCode "
                        + " left outer join tblposmaster c on a.strPOSCode=c.strPosCode "
                        + " where a.strPromoCode='" + promoCode + "'";
            }
            else
            {
                sql = "select a.*,b.strItemName,ifnull(c.strPosName,'All') "
                        + " from tblpromotionmaster a left outer join tblitemmaster b on a.strPromoItemCode=b.strItemCode "
                        + " left outer join tblposmaster c on a.strPOSCode=c.strPosCode "
                        + " where a.strPromoCode='" + promoCode + "'";
            }
        }
        rsTemp1.close();

        ResultSet rsPromotion = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsPromotion.next())
        {
            txtPromoCode.setText(rsPromotion.getString(1));
            txtPromoName.setText(rsPromotion.getString(2));
            cmbBuyPromotionOn.setSelectedItem(rsPromotion.getString(3));
            cmbOperator.setSelectedItem(rsPromotion.getString(5));
            txtBuyQty.setText(rsPromotion.getString(6));

            String[] spFromDate = (rsPromotion.getString(7).split(" ")[0].split("-"));
            String fromDate = spFromDate[2] + "-" + spFromDate[1] + "-" + spFromDate[0];
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
            dteFromDate.setDate(date);
            String[] spToDate = (rsPromotion.getString(8).split(" ")[0].split("-"));
            String toDate = spToDate[2] + "-" + spToDate[1] + "-" + spToDate[0];
            date = new SimpleDateFormat("dd-MM-yyyy").parse(toDate);
            dteToDate.setDate(date);

            cmbType.setSelectedItem(rsPromotion.getString(12));
            txtPromotionNote.setText(rsPromotion.getString(13));
            buyItemCode = rsPromotion.getString(4);
            promoItemCode = rsPromotion.getString(4);
            if (!rsPromotion.getString(20).equals("All"))
            {
                ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSName from tblposmaster where strPOSCode='" + rsPromotion.getString(20) + "'");
                if (rsPOS.next())
                {
                    cmbPOS.setSelectedItem(rsPOS.getString(1));
                }
                rsPOS.close();
            }
            else
            {
                cmbPOS.setSelectedItem(rsPromotion.getString(20));
            }
            promoItemCodeGet = rsPromotion.getString(21);
            ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet("select strAreaName from tblareamaster where strAreaCode='" + rsPromotion.getString(23) + "'");
            if (rsArea.next())
            {
                cmbArea.setSelectedItem(rsArea.getString(1));
            }
            rsArea.close();
            
            cmbPGType.setSelectedItem(rsPromotion.getString(24));
            txtKOTTimeBound.setText(rsPromotion.getString(25));
            
            txtBuyItemName.setText(rsPromotion.getString(26));
            
            rsPromotion.close();
            if (cmbBuyPromotionOn.getSelectedItem().toString().equals("MenuHead"))
            {
                funFillBuyMenuItemGridForUpdate(buyItemCode, txtPromoCode.getText().trim());
            }

            sql = "select strGetPromoOn,strGetItemCode from tblpromotionmaster where strPromoCode='" + promoCode + "'";
            ResultSet rsPromoDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsPromoDtl.next())
            {
                cmbGetPromotionOn.setSelectedItem(rsPromoDtl.getString(1));
                if (rsPromoDtl.getString(1).equals("MenuHead"))
                {
                    sql = "select strMenuName from tblmenuhd where strMenuCode='" + rsPromoDtl.getString(2) + "'";
                    ResultSet rsMenu = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsMenu.next())
                    {
                        txtGetItemName.setText(rsMenu.getString(1));
                    }
                    rsMenu.close();
                    funFillGetMenuItemGridForUpdate(rsPromoDtl.getString(2), txtPromoCode.getText().trim(),"MenuHeadWise");
                }
                else if (rsPromoDtl.getString(1).equals("PromoGroup"))
                {
                    sql = "select strPromoGroupName from tblpromogroupmaster where strPromoGroupCode='" + rsPromoDtl.getString(2) + "'";
                    ResultSet rsPromoGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsPromoGroup.next())
                    {
                        txtGetItemName.setText(rsPromoGroup.getString(1));
                    }
                    rsPromoGroup.close();
                    funFillGetMenuItemGridForUpdate(rsPromoDtl.getString(2), txtPromoCode.getText().trim(),"PromoGroupWise");
                }
                else
                {
                    sql = "select strItemName from tblitemmaster where strItemCode='" + rsPromoDtl.getString(2) + "'";
                    ResultSet rsItem = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsItem.next())
                    {
                        txtGetItemName.setText(rsItem.getString(1));
                    }
                    rsItem.close();
                    promoItemCodeGet = rsPromoDtl.getString(2);
                }

                sql = "select dblGetQty,strDiscountType,dblDiscount "
                        + "from tblpromotiondtl where strPromoCode='" + promoCode + "'";
                ResultSet rsGetItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsGetItemDtl.next())
                {
                    txtGetQty.setText(rsGetItemDtl.getString(1));
                    cmbDiscount.setSelectedItem(rsGetItemDtl.getString(2));
                    txtDiscount.setText(rsGetItemDtl.getString(3));
                }
                rsGetItemDtl.close();
            }
            funFillPromoDayTimeGrid(promoCode);

            rsPromoDtl.close();

            btnNew.setText("UPDATE");
            btnNew.setMnemonic('u');
        }
    }

    private void funFillPromoDayTimeGrid(String promoCode) throws Exception
    {
        DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
        dmPromoDayTime.setRowCount(0);
        sql = "select * from tblpromotiondaytimedtl where strPromoCode='" + promoCode + "'";
        ResultSet rsPromoDayTime = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsPromoDayTime.next())
        {
            String fromTime=rsPromoDayTime.getString(3).trim().replaceAll(" ", ":");
            String toTime=rsPromoDayTime.getString(4).trim().replaceAll(" ", ":");
            Object[] arrObj =
            {
                rsPromoDayTime.getString(2), fromTime, toTime, false
            };
            dmPromoDayTime.addRow(arrObj);
        }
        rsPromoDayTime.close();

        tblPromoDayTime.setModel(dmPromoDayTime);
    }

    private int funFillBuyMenuItemGridForUpdate(String menuCode, String promoCode) throws Exception
    {
        funFillBuyMenuItemGrid(menuCode);

        for (int cnt = 0; cnt < tblBuyMenuItems.getRowCount(); cnt++)
        {
            tblBuyMenuItems.setValueAt(false, cnt, 2);
        }

        sql = "select strBuyPromoItemCode from tblbuypromotiondtl where strPromoCode='" + promoCode + "'";
        ResultSet rsMenu = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsMenu.next())
        {
            for (int cnt = 0; cnt < tblBuyMenuItems.getRowCount(); cnt++)
            {
                if (rsMenu.getString(1).equals(tblBuyMenuItems.getValueAt(cnt, 3).toString()))
                {
                    tblBuyMenuItems.setValueAt(true, cnt, 2);
                }
            }
        }
        rsMenu.close();

        return 1;
    }

    private int funFillGetMenuItemGridForUpdate(String code, String promoCode,String promoType) throws Exception
    {
        funFillGetMenuItemGrid(code,promoType);

        for (int cnt = 0; cnt < tblGetMenuItems.getRowCount(); cnt++)
        {
            tblGetMenuItems.setValueAt(false, cnt, 2);
        }

        sql = "select strPromoItemCode from tblpromotiondtl where strPromoCode='" + promoCode + "'";
        ResultSet rsMenuItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsMenuItems.next())
        {
            for (int cnt = 0; cnt < tblGetMenuItems.getRowCount(); cnt++)
            {
                if (rsMenuItems.getString(1).equals(tblGetMenuItems.getValueAt(cnt, 3).toString()))
                {
                    tblGetMenuItems.setValueAt(true, cnt, 2);
                }
            }
        }
        rsMenuItems.close();
        return 1;
    }

    /**
     * This method is used to get promotion code
     *
     * @return long promotion code
     * @throws Exception
     */
    private long funGenPromoCode() throws Exception
    {
        long lastNo = 0;
        String sql = "select dblLastNo from tblinternal where strTransactionType='PromotionCode'";
        ResultSet rsPromo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsPromo.next())
        {
            lastNo = rsPromo.getLong(1);
            long updateCounter = lastNo + 1;
            String updateSql = "update tblinternal set dblLastNo=" + updateCounter + " "
                    + "where strTransactionType='PromotionCode'";
            clsGlobalVarClass.dbMysql.execute(updateSql);
        }
        else
        {
            String insertSql = "insert into tblinternal(strTransactionType,dblLastNo) values('PromotionCode',1)";
            lastNo = 1;
            clsGlobalVarClass.dbMysql.execute(insertSql);
        }
        rsPromo.close();
        return lastNo + 1;
    }

    /**
     * This method is used to save
     */
    private void funSave()
    {
        try
        {
            Date dtFromDate = dteFromDate.getDate();
            String dateFrom = (dtFromDate.getYear() + 1900) + "-" + (dtFromDate.getMonth() + 1) + "-" + (dtFromDate.getDate());
            Date dtToDate = dteToDate.getDate();
            String dateTo = (dtToDate.getYear() + 1900) + "-" + (dtToDate.getMonth() + 1) + "-" + (dtToDate.getDate());

            if (txtPromoName.getText().trim().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Promotion Name");
                return;
            }

            long lastNo = funGenPromoCode();
            String promoCode = "PM" + String.format("%03d", lastNo);
            txtPromoCode.setText(promoCode);

            String days = "";
            String fromTime = "";
            String toTime = "";

            String buyPromotionOn = cmbBuyPromotionOn.getSelectedItem().toString();
            String getPromotionOn = cmbGetPromotionOn.getSelectedItem().toString();

            String areaCode=hmArea.get(cmbArea.getSelectedItem().toString());
            
            String promoGroupType=cmbPGType.getSelectedItem().toString();
            String kotTimeBound=txtKOTTimeBound.getText();
            
            String sql = "Insert into tblpromotionmaster "
                    + "(strPromoCode,strPromoName,strPromotionOn,strPromoItemCode,strOperator,dblBuyQty,dteFromDate"
                    + ",dteToDate,tmeFromTime,tmeToTime,strDays,strType,strPromoNote,strUserCreated,strUserEdited"
                    + ",dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strPOSCode,strGetItemCode,strGetPromoOn"
                    + ",strAreaCode,strPromoGroupType,longKOTTimeBound) "
                    + "VALUES('" + txtPromoCode.getText() + "','" + txtPromoName.getText() + "','" + buyPromotionOn + "'"
                    + ",'" + promoItemCode + "','" + cmbOperator.getSelectedItem().toString() + "'," + txtBuyQty.getText() + ""
                    + ",'" + dateFrom + "','" + dateTo + "','" + fromTime + "','" + toTime + "','" + days + "','" + cmbType.getSelectedItem().toString() + "'"
                    + ",'" + txtPromotionNote.getText() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','N','" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "'"
                    + ",'" + promoItemCodeGet + "','" + getPromotionOn + "','"+areaCode+"','"+promoGroupType+"','"+kotTimeBound+"')";
            System.out.println(sql);
            clsGlobalVarClass.dbMysql.execute(sql);
            funInsertBuyPromoItems();
            funInsertGetPromoItems();
            funInsertPromoDayTimeDtl();

            sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + " where strTableName='Promotion' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            new frmOkPopUp(null, "Entry added Successfully", "Successful", 4).setVisible(true);
            funResetField();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private int funInsertBuyPromoItems() throws Exception
    {
        if (cmbBuyPromotionOn.getSelectedItem().toString().trim().equals("Item"))
        {
            String delete = "delete from tblbuypromotiondtl where strPromoCode='" + txtPromoCode.getText() + "' ";
            clsGlobalVarClass.dbMysql.execute(delete);

            String sqlinsert = "Insert into tblbuypromotiondtl "
                    + "(strPromoCode,strBuyPromoItemCode,dblBuyItemQty,strOperator,strClientCode,strDataPostFlag) "
                    + "VALUES('" + txtPromoCode.getText() + "','" + promoItemCode + "','" + txtBuyQty.getText() + "'"
                    + ",'" + cmbOperator.getSelectedItem().toString().trim() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','N')";
            //System.out.println(sqlinsert);
            clsGlobalVarClass.dbMysql.execute(sqlinsert);
        }
        else
        {
            String delete = "delete from tblbuypromotiondtl where strPromoCode='" + txtPromoCode.getText() + "' ";
            clsGlobalVarClass.dbMysql.execute(delete);

            for (int cnt = 0; cnt < tblBuyMenuItems.getRowCount(); cnt++)
            {
                boolean flgSelect = Boolean.parseBoolean(tblBuyMenuItems.getValueAt(cnt, 2).toString());
                if (flgSelect)
                {
                    String sqlinsert = "Insert into tblbuypromotiondtl "
                            + "(strPromoCode,strBuyPromoItemCode,dblBuyItemQty,strOperator,strClientCode,strDataPostFlag) "
                            + "VALUES('" + txtPromoCode.getText() + "','" + tblBuyMenuItems.getValueAt(cnt, 3).toString() + "'"
                            + ",'" + txtBuyQty.getText() + "','" + cmbOperator.getSelectedItem().toString().trim() + "'"
                            + ",'" + clsGlobalVarClass.gClientCode + "','N')";
                    //System.out.println(sqlinsert);
                    clsGlobalVarClass.dbMysql.execute(sqlinsert);
                }
            }
        }

        return 1;
    }

    private int funInsertGetPromoItems() throws Exception
    {
        if (cmbGetPromotionOn.getSelectedItem().toString().trim().equals("Item"))
        {
            String delete = "delete from tblpromotiondtl where strPromoCode='" + txtPromoCode.getText() + "' ";
            clsGlobalVarClass.dbMysql.execute(delete);

            String sqlinsert = "Insert into tblpromotiondtl "
                    + "(strPromoCode,strPromotionOn,strPromoItemCode,dblGetQty,strDiscountType,dblDiscount"
                    + ",strClientCode) "
                    + "VALUES('" + txtPromoCode.getText() + "','" + cmbGetPromotionOn.getSelectedItem().toString() + "'"
                    + ",'" + promoItemCodeGet + "','" + txtGetQty.getText() + "','" + cmbDiscount.getSelectedItem().toString() + "'"
                    + ",'" + txtDiscount.getText() + "','" + clsGlobalVarClass.gClientCode + "')";
            //System.out.println(sqlinsert);
            clsGlobalVarClass.dbMysql.execute(sqlinsert);
        }
        else
        {
            String delete = "delete from tblpromotiondtl where strPromoCode='" + txtPromoCode.getText() + "' ";
            clsGlobalVarClass.dbMysql.execute(delete);

            for (int cnt = 0; cnt < tblGetMenuItems.getRowCount(); cnt++)
            {
                boolean flgSelect = Boolean.parseBoolean(tblGetMenuItems.getValueAt(cnt, 2).toString());
                if (flgSelect)
                {
                    String sqlinsert = "Insert into tblpromotiondtl "
                            + "(strPromoCode,strPromotionOn,strPromoItemCode,dblGetQty,strDiscountType,dblDiscount"
                            + ",strClientCode) "
                            + "VALUES('" + txtPromoCode.getText() + "','" + cmbGetPromotionOn.getSelectedItem().toString() + "'"
                            + ",'" + tblGetMenuItems.getValueAt(cnt, 3).toString() + "','" + txtGetQty.getText() + "'"
                            + ",'" + cmbDiscount.getSelectedItem().toString() + "','" + txtDiscount.getText() + "'"
                            + ",'" + clsGlobalVarClass.gClientCode + "')";
                    //System.out.println(sqlinsert);
                    clsGlobalVarClass.dbMysql.execute(sqlinsert);
                }
            }
        }

        return 1;
    }

    private int funInsertPromoDayTimeDtl() throws Exception
    {
        String delete = "delete from tblpromotiondaytimedtl where strPromoCode='" + txtPromoCode.getText() + "' ";
        clsGlobalVarClass.dbMysql.execute(delete);

        for (int cnt = 0; cnt < tblPromoDayTime.getRowCount(); cnt++)
        {
            String sqlinsert = "Insert into tblpromotiondaytimedtl "
                    + "(strPromoCode,strDay,tmeFromTime,tmeToTime,strClientCode) "
                    + "VALUES('" + txtPromoCode.getText() + "','" + tblPromoDayTime.getValueAt(cnt, 0).toString() + "'"
                    + ",'" + tblPromoDayTime.getValueAt(cnt, 1).toString() + "'"
                    + ",'" + tblPromoDayTime.getValueAt(cnt, 2).toString() + "','" + clsGlobalVarClass.gClientCode + "')";
            //System.out.println(sqlinsert);
            clsGlobalVarClass.dbMysql.execute(sqlinsert);
        }

        return 1;
    }

    /**
     * This method is used to update
     */
    private void funUpdate()
    {
        try
        {
            Date dtFromDate = dteFromDate.getDate();
            String dateFrom = (dtFromDate.getYear() + 1900) + "-" + (dtFromDate.getMonth() + 1) + "-" + (dtFromDate.getDate());
            Date dtToDate = dteToDate.getDate();
            String dateTo = (dtToDate.getYear() + 1900) + "-" + (dtToDate.getMonth() + 1) + "-" + (dtToDate.getDate());

            if (txtPromoName.getText().trim().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Promotion Name");
                return;
            }

            String days = "";
            String fromTime = "";
            String toTime = "";

            String buyPromotionOn = cmbBuyPromotionOn.getSelectedItem().toString();
            String getPromotionOn = cmbGetPromotionOn.getSelectedItem().toString();

            String areaCode=hmArea.get(cmbArea.getSelectedItem().toString());
            
            String promoGroupType=cmbPGType.getSelectedItem().toString();
            String kotTimeBound=txtKOTTimeBound.getText();
            
            String sql = "update tblpromotionmaster set "
                    + "strPromoName='" + txtPromoName.getText().trim() + "',strPromotionOn='" + buyPromotionOn.trim() + "'"
                    + ",strPromoItemCode='" + promoItemCode.trim() + "',strOperator='" + cmbOperator.getSelectedItem().toString().trim() + "'"
                    + ",dblBuyQty=" + txtBuyQty.getText() + ",dteFromDate='" + dateFrom + "',dteToDate='" + dateTo + "'"
                    + ",tmeFromTime='" + fromTime + "',tmeToTime='" + toTime + "',strDays='" + days + "'"
                    + ",strPromoNote='" + txtPromotionNote.getText().trim() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N'"
                    + ",strPOSCode='" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "',strAreaCode='"+areaCode+"'"
                    + ",strPromoGroupType='"+promoGroupType+"',longKOTTimeBound='"+kotTimeBound+"' "
                    + "where strPromoCode='" + txtPromoCode.getText().trim() + "'";
            System.out.println(sql);
            clsGlobalVarClass.dbMysql.execute(sql);

            funInsertBuyPromoItems();
            funInsertGetPromoItems();
            funInsertPromoDayTimeDtl();

            sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + " where strTableName='Promotion' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            new frmOkPopUp(null, "Record Updated Successfully", "Successful", 4).setVisible(true);
            funResetField();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private boolean funCheckDuplicateBuyPromoItem() throws Exception
    {
        boolean flgResult = false;

        String query = " select strPromoCode "
                + " from tblpromotionmaster "
                + " where strPromoItemCode='" + promoItemCode + "' "
                + " and strPromoCode!='" + txtPromoCode.getText() + "' "
                + " and (strPOSCode='" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "' or strPOSCode='All') "
                + " and strAreaCode='"+hmArea.get(cmbArea.getSelectedItem().toString())+"' ";
        ResultSet rsDupPromo = clsGlobalVarClass.dbMysql.executeResultSet(query);
        if (rsDupPromo.next())
        {
            flgResult = true;
        }
        else
        {
            /*
             query=" select strPromoCode "
             + " from tblbuypromotiondtl "
             + " where strBuyPromoItemCode='"+promoItemCode+"' and strPromoCode!='"+txtPromoCode.getText()+"' " ;*/

            query = "select a.strPromoCode "
                    + " from tblpromotionmaster a,tblbuypromotiondtl b "
                    + " where a.strPromoCode=b.strPromoCode and b.strBuyPromoItemCode='" + promoItemCode + "' "
                    + " and a.strPromoCode!='" + txtPromoCode.getText() + "' "
                    + " and (a.strPOSCode='" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "' or a.strPOSCode='All')";

            ResultSet rsDupPromo1 = clsGlobalVarClass.dbMysql.executeResultSet(query);
            if (rsDupPromo1.next())
            {
                flgResult = true;
            }
            rsDupPromo1.close();
        }
        return flgResult;
    }

    private boolean funCheckDuplicateGetPromoItem() throws Exception
    {
        boolean flgResult = false;
        String query = " select strPromoCode "
                + " from tblpromotionmaster "
                + " where strGetItemCode='" + promoItemCodeGet + "' and strPromoCode!='" + txtPromoCode.getText() + "' "
                + " and (strPOSCode='" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "' or strPOSCode='All')";
        ResultSet rsDupPromo = clsGlobalVarClass.dbMysql.executeResultSet(query);
        if (rsDupPromo.next())
        {
            flgResult = true;
        }
        else
        {
            /*
             query=" select strPromoCode "
             + " from tblpromotiondtl "
             + "where strPromoItemCode='"+promoItemCodeGet+"' and strPromoCode!='"+txtPromoCode.getText()+"' " ;*/

            query = "select a.strPromoCode "
                    + " from tblpromotionmaster a,tblpromotiondtl b "
                    + " where a.strPromoCode=b.strPromoCode and b.strPromoItemCode='" + promoItemCodeGet + "' "
                    + " and a.strPromoCode!='" + txtPromoCode.getText() + "' "
                    + " and (a.strPOSCode='" + hmPOS.get(cmbPOS.getSelectedItem().toString()) + "' or a.strPOSCode='All')";

            ResultSet rsDupPromo1 = clsGlobalVarClass.dbMysql.executeResultSet(query);
            if (rsDupPromo1.next())
            {
                flgResult = true;
            }
            rsDupPromo1.close();
        }
        return flgResult;
    }

    public void funSaveAndUpdateOpearation()
    {
        try
        {

            if (txtPromoName.getText().trim().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Promotion Name");
                return;
            }

            if (!obj.funCheckDouble(txtBuyQty.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild input buy value");
                txtBuyQty.requestFocus();
                return;
            }

            if (Double.parseDouble(txtDiscount.getText().trim()) > 0)
            {
                if (!obj.funCheckDouble(txtDiscount.getText()))
                {
                    JOptionPane.showMessageDialog(this, "Invaild input discount value");
                    txtDiscount.requestFocus();
                    return;
                }
                txtGetQty.setText("0.00");
            }
            else
            {
//                if (funCheckDuplicateGetPromoItem())
//                {
//                    JOptionPane.showMessageDialog(this, "Promotion is already defined On Get Item");
//                    return;
//                }

                if (cmbGetPromotionOn.getSelectedItem().equals("Item") && txtGetItemName.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Please select Get Item.");
                    return;
                }
                if (cmbGetPromotionOn.getSelectedItem().equals("MenuHead") && txtGetItemName.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Please select Get MenuHead");
                    return;
                }
                
                if(cmbGetPromotionOn.getSelectedItem().toString().equals("PromoGroup"))
                {
                    if(cmbPGType.getSelectedItem().toString().equalsIgnoreCase("Limited"))
                    {
                        if (!obj.funCheckDouble(txtGetQty.getText()))
                        {
                            JOptionPane.showMessageDialog(this, "Invaild input get quantity.");
                            txtGetQty.requestFocus();
                            return;
                        }
                    }
                    else
                    {
                        if (!obj.funCheckDouble(txtKOTTimeBound.getText()))
                        {
                            JOptionPane.showMessageDialog(this, "Invaild input KOT Time Bound.");
                            txtGetQty.requestFocus();
                            return;
                        }
                    }
                }
                else
                {
                    if (!obj.funCheckDouble(txtGetQty.getText()))
                    {
                        JOptionPane.showMessageDialog(this, "Invaild input get quantity.");
                        txtGetQty.requestFocus();
                        return;
                    }
                }
            }

            if (cmbBuyPromotionOn.getSelectedItem().equals("Item") && txtBuyItemName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please select Item.");
                return;
            }
            if (cmbBuyPromotionOn.getSelectedItem().equals("MenuHead") && txtBuyItemName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please select MenuHead");
                return;
            }

            if (dteToDate.getDate().before(dteFromDate.getDate()))
            {
                new frmOkPopUp(this, "Invalid To Date", "Error", 1).setVisible(true);
                return;
            }

            if (tblPromoDayTime.getRowCount() == 0)
            {
                new frmOkPopUp(this, "Please Enter Day and Time for Promotion. ", "Error", 1).setVisible(true);
                return;
            }

            if (btnNew.getText().equals("SAVE"))
            {

                if (funCheckDuplicateBuyPromoItem())
                {
                    JOptionPane.showMessageDialog(this, "Promotion is already defined On Buy Item");
                    return;
                }

                funSave();
            }
            else
            {
                funUpdate();
            }
            
            cmbArea.setSelectedItem("All");
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
        txtPromoCode.setText("");
        txtPromoName.setText("");
        txtPromotionNote.setText("");
        txtBuyItemName.setText("");
        cmbBuyPromotionOn.setSelectedIndex(0);
        cmbFromHour.setSelectedIndex(0);
        cmbFromMinute.setSelectedIndex(0);
        cmbFromAMPM.setSelectedIndex(0);
        cmbToHour.setSelectedIndex(0);
        cmbToMinute.setSelectedIndex(0);
        cmbToAMPM.setSelectedIndex(0);
        /*
         chkSun.setSelected(false);
         chkMon.setSelected(false);
         chkTue.setSelected(false);
         chkWed.setSelected(false);
         chkThus.setSelected(false);
         chkFri.setSelected(false);
         chkSat.setSelected(false);
         */

        cmbType.setSelectedIndex(0);
        cmbOperator.setSelectedIndex(0);
        txtBuyQty.setText("");
        cmbGetPromotionOn.setSelectedIndex(0);
        txtGetItemName.setText("");
        txtGetQty.setText("");
        cmbDiscount.setSelectedIndex(0);
        txtDiscount.setText("0.00");
        txtPromotionNote.setText("");
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');
        DefaultTableModel dmBuyItems = (DefaultTableModel) tblBuyMenuItems.getModel();
        dmBuyItems.setRowCount(0);

        DefaultTableModel dmGetItems = (DefaultTableModel) tblGetMenuItems.getModel();
        dmGetItems.setRowCount(0);

        DefaultTableModel dmPromoDayTime = (DefaultTableModel) tblPromoDayTime.getModel();
        dmPromoDayTime.setRowCount(0);
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
        panelLayout = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        lblPromoCode = new javax.swing.JLabel();
        txtPromoCode = new javax.swing.JTextField();
        lblPromoName = new javax.swing.JLabel();
        txtPromoName = new javax.swing.JTextField();
        lblPOSName = new javax.swing.JLabel();
        cmbPOS = new javax.swing.JComboBox();
        lblFromDate = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        tblToDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        Tab = new javax.swing.JTabbedPane();
        panelBuyTab = new javax.swing.JPanel();
        cmbType = new javax.swing.JComboBox();
        cmbOperator = new javax.swing.JComboBox();
        txtBuyQty = new javax.swing.JTextField();
        lblFrom = new javax.swing.JLabel();
        lbltype = new javax.swing.JLabel();
        lblIs = new javax.swing.JLabel();
        txtBuyItemName = new javax.swing.JTextField();
        cmbBuyPromotionOn = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBuyMenuItems = new javax.swing.JTable();
        panelGetTab = new javax.swing.JPanel();
        cmbGetPromotionOn = new javax.swing.JComboBox();
        lblQty = new javax.swing.JLabel();
        txtGetQty = new javax.swing.JTextField();
        lblDiscount = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        txtGetItemName = new javax.swing.JTextField();
        cmbDiscount = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGetMenuItems = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblFromTime = new javax.swing.JLabel();
        cmbFromHour = new javax.swing.JComboBox();
        cmbFromMinute = new javax.swing.JComboBox();
        cmbFromAMPM = new javax.swing.JComboBox();
        lblToTime = new javax.swing.JLabel();
        cmbToHour = new javax.swing.JComboBox();
        cmbToMinute = new javax.swing.JComboBox();
        cmbToAMPM = new javax.swing.JComboBox();
        cmbDays = new javax.swing.JComboBox();
        btnAdd = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPromoDayTime = new javax.swing.JTable();
        btnRemove = new javax.swing.JButton();
        btnResetGrid = new javax.swing.JButton();
        lblPromotionNote = new javax.swing.JLabel();
        txtPromotionNote = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblAreaName = new javax.swing.JLabel();
        cmbArea = new javax.swing.JComboBox();
        lblPGType = new javax.swing.JLabel();
        cmbPGType = new javax.swing.JComboBox();
        txtKOTTimeBound = new javax.swing.JTextField();
        lblKOTTimeBound = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setName("promotion"); // NOI18N
        setUndecorated(true);
        setResizable(false);
        setState(NORMAL);
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
        lblProductName.setText("SPOS- ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Promotion Master");
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

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblPromoCode.setText("Promotion Code");

        txtPromoCode.setEnabled(false);
        txtPromoCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPromoCodeMouseClicked(evt);
            }
        });
        txtPromoCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPromoCodeKeyPressed(evt);
            }
        });

        lblPromoName.setText("Promotion Name");

        txtPromoName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPromoNameMouseClicked(evt);
            }
        });
        txtPromoName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPromoNameKeyPressed(evt);
            }
        });

        lblPOSName.setText("POS");

        cmbPOS.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbPOSItemStateChanged(evt);
            }
        });

        lblFromDate.setText("From Date");

        dteFromDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteFromDateKeyPressed(evt);
            }
        });

        tblToDate.setText("To Date");

        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        panelBuyTab.setOpaque(false);

        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Quantity", "value" }));
        cmbType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbTypeKeyPressed(evt);
            }
        });

        cmbOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", "<", ">" }));
        cmbOperator.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperatorActionPerformed(evt);
            }
        });

        txtBuyQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBuyQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBuyQtyMouseClicked(evt);
            }
        });
        txtBuyQty.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBuyQtyKeyPressed(evt);
            }
        });

        lblFrom.setText("Value");

        lbltype.setText("Type");

        lblIs.setText("Is");

        txtBuyItemName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBuyItemNameMouseClicked(evt);
            }
        });
        txtBuyItemName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBuyItemNameKeyPressed(evt);
            }
        });

        cmbBuyPromotionOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item", "MenuHead" }));
        cmbBuyPromotionOn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBuyPromotionOnActionPerformed(evt);
            }
        });
        cmbBuyPromotionOn.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbBuyPromotionOnKeyPressed(evt);
            }
        });

        tblBuyMenuItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name", "Rate", "Select", "ItemCode"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, true, false
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
        jScrollPane1.setViewportView(tblBuyMenuItems);
        if (tblBuyMenuItems.getColumnModel().getColumnCount() > 0)
        {
            tblBuyMenuItems.getColumnModel().getColumn(0).setMinWidth(300);
            tblBuyMenuItems.getColumnModel().getColumn(0).setPreferredWidth(300);
            tblBuyMenuItems.getColumnModel().getColumn(0).setMaxWidth(300);
            tblBuyMenuItems.getColumnModel().getColumn(1).setMinWidth(100);
            tblBuyMenuItems.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblBuyMenuItems.getColumnModel().getColumn(1).setMaxWidth(100);
            tblBuyMenuItems.getColumnModel().getColumn(2).setMinWidth(60);
            tblBuyMenuItems.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblBuyMenuItems.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        javax.swing.GroupLayout panelBuyTabLayout = new javax.swing.GroupLayout(panelBuyTab);
        panelBuyTab.setLayout(panelBuyTabLayout);
        panelBuyTabLayout.setHorizontalGroup(
            panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuyTabLayout.createSequentialGroup()
                .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIs, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltype, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBuyPromotionOn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBuyTabLayout.createSequentialGroup()
                        .addComponent(cmbOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuyQty, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtBuyItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBuyTabLayout.setVerticalGroup(
            panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuyTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBuyPromotionOn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuyItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbType)
                    .addComponent(lbltype, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbOperator)
                    .addGroup(panelBuyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuyQty, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblIs, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 166, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBuyTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        Tab.addTab("Buy", panelBuyTab);

        panelGetTab.setOpaque(false);

        cmbGetPromotionOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item", "MenuHead", "PromoGroup" }));
        cmbGetPromotionOn.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbGetPromotionOnKeyPressed(evt);
            }
        });

        lblQty.setText("Quantity");

        txtGetQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGetQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGetQtyMouseClicked(evt);
            }
        });
        txtGetQty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtGetQtyActionPerformed(evt);
            }
        });
        txtGetQty.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGetQtyKeyPressed(evt);
            }
        });

        lblDiscount.setText("Discount");

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscount.setText("0.00");
        txtDiscount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountMouseClicked(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountKeyPressed(evt);
            }
        });

        txtGetItemName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGetItemNameMouseClicked(evt);
            }
        });
        txtGetItemName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGetItemNameKeyPressed(evt);
            }
        });

        cmbDiscount.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Value", "Percent" }));
        cmbDiscount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbDiscountKeyPressed(evt);
            }
        });

        tblGetMenuItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name", "Rate", "Select", "ItemCode"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, true, false
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
        jScrollPane2.setViewportView(tblGetMenuItems);
        if (tblGetMenuItems.getColumnModel().getColumnCount() > 0)
        {
            tblGetMenuItems.getColumnModel().getColumn(0).setMinWidth(300);
            tblGetMenuItems.getColumnModel().getColumn(0).setPreferredWidth(300);
            tblGetMenuItems.getColumnModel().getColumn(0).setMaxWidth(300);
            tblGetMenuItems.getColumnModel().getColumn(1).setMinWidth(100);
            tblGetMenuItems.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblGetMenuItems.getColumnModel().getColumn(1).setMaxWidth(100);
            tblGetMenuItems.getColumnModel().getColumn(2).setMinWidth(60);
            tblGetMenuItems.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblGetMenuItems.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        javax.swing.GroupLayout panelGetTabLayout = new javax.swing.GroupLayout(panelGetTab);
        panelGetTab.setLayout(panelGetTabLayout);
        panelGetTabLayout.setHorizontalGroup(
            panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGetTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelGetTabLayout.createSequentialGroup()
                        .addComponent(cmbGetPromotionOn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGetItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGetTabLayout.createSequentialGroup()
                        .addComponent(lblQty)
                        .addGap(18, 18, 18)
                        .addComponent(txtGetQty, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGetTabLayout.createSequentialGroup()
                        .addComponent(lblDiscount)
                        .addGap(18, 18, 18)
                        .addComponent(cmbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDiscount)))
                .addGap(51, 51, 51)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );
        panelGetTabLayout.setVerticalGroup(
            panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGetTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGetItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbGetPromotionOn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGetQty, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(172, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        Tab.addTab("Get", panelGetTab);

        jPanel1.setOpaque(false);

        lblFromTime.setText("From Time");

        cmbFromHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbFromHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromHourKeyPressed(evt);
            }
        });

        cmbFromMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbFromMinute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromMinuteKeyPressed(evt);
            }
        });

        cmbFromAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbFromAMPM.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFromAMPMActionPerformed(evt);
            }
        });
        cmbFromAMPM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromAMPMKeyPressed(evt);
            }
        });

        lblToTime.setText("To Time");

        cmbToHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbToHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToHourKeyPressed(evt);
            }
        });

        cmbToMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbToMinute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToMinuteKeyPressed(evt);
            }
        });

        cmbToAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbToAMPM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToAMPMKeyPressed(evt);
            }
        });

        cmbDays.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" }));
        cmbDays.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbDaysKeyPressed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAddKeyPressed(evt);
            }
        });

        tblPromoDayTime.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Day", "From Time", "To Time", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, true, true
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
        jScrollPane3.setViewportView(tblPromoDayTime);
        if (tblPromoDayTime.getColumnModel().getColumnCount() > 0)
        {
            tblPromoDayTime.getColumnModel().getColumn(3).setMinWidth(50);
            tblPromoDayTime.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblPromoDayTime.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });

        btnResetGrid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnResetGrid.setForeground(new java.awt.Color(255, 255, 255));
        btnResetGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetGrid.setText("RESET");
        btnResetGrid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetGrid.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetGridActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnResetGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(cmbDays, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblFromTime, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbFromHour, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbFromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbFromAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(24, 24, 24)
                            .addComponent(lblToTime, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbToHour, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(cmbToMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(cmbToAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbToMinute, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbToHour, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbFromHour, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbFromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbFromAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFromTime, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDays, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cmbToAMPM)
                    .addComponent(lblToTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
        );

        Tab.addTab("Day & Time", jPanel1);

        lblPromotionNote.setText("Promotion Note");

        txtPromotionNote.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPromotionNoteActionPerformed(evt);
            }
        });
        txtPromotionNote.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPromotionNoteKeyPressed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Promotion");
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
        btnCancel.setToolTipText("Close Promotion Master");
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

        lblAreaName.setText("Area");

        lblPGType.setText("Type");

        cmbPGType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Limited", "UnlImited" }));
        cmbPGType.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbPGTypeItemStateChanged(evt);
            }
        });
        cmbPGType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPGTypeKeyPressed(evt);
            }
        });

        txtKOTTimeBound.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtKOTTimeBound.setText("0");

        lblKOTTimeBound.setText("KOT Time Bound");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tab)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblPromotionNote, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPromotionNote))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(14, 14, 14)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbPOS, 0, 198, Short.MAX_VALUE)
                                            .addComponent(cmbArea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblPromoCode)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPromoCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblPromoName)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPromoName, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblPGType, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                            .addComponent(cmbPGType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addComponent(tblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addComponent(lblKOTTimeBound, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtKOTTimeBound, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPromoCode)
                    .addComponent(txtPromoCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPromoName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPromoName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblKOTTimeBound, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblPGType, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbPGType, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtKOTTimeBound, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Tab, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPromotionNote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPromotionNote, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPromoCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPromoCodeMouseClicked
    {//GEN-HEADEREND:event_txtPromoCodeMouseClicked
        setAlwaysOnTop(false);
        funHelpForPromoCode();
    }//GEN-LAST:event_txtPromoCodeMouseClicked

    private void txtPromoCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPromoCodeKeyPressed
    {//GEN-HEADEREND:event_txtPromoCodeKeyPressed
        // TODO add your handling code here:
        //open item help on click of '?' or '/' key
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            setAlwaysOnTop(false);
            funHelpForPromoCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtPromoName.requestFocus();
        }
    }//GEN-LAST:event_txtPromoCodeKeyPressed

    private void txtPromoNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPromoNameMouseClicked
    {//GEN-HEADEREND:event_txtPromoNameMouseClicked
        try
        {
            if (txtPromoName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Name.").setVisible(true);
                txtPromoName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPromoName.getText(), "1", "Please Enter Name.").setVisible(true);
                txtPromoName.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPromoNameMouseClicked

    private void txtPromoNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPromoNameKeyPressed
    {//GEN-HEADEREND:event_txtPromoNameKeyPressed
        // TODO add your handling code here:
        //open item help on click of '?' or '/' key
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            setAlwaysOnTop(false);
            funHelpForPromoCode();
        }
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            cmbBuyPromotionOn.requestFocus();
            //txtBuyItemName.requestFocus();
        }
    }//GEN-LAST:event_txtPromoNameKeyPressed

    private void cmbBuyPromotionOnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbBuyPromotionOnActionPerformed
    {//GEN-HEADEREND:event_cmbBuyPromotionOnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBuyPromotionOnActionPerformed

    private void cmbBuyPromotionOnKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbBuyPromotionOnKeyPressed
    {//GEN-HEADEREND:event_cmbBuyPromotionOnKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            txtBuyItemName.requestFocus();
        }

    }//GEN-LAST:event_cmbBuyPromotionOnKeyPressed

    private void txtBuyItemNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBuyItemNameMouseClicked
    {//GEN-HEADEREND:event_txtBuyItemNameMouseClicked
        setAlwaysOnTop(false);
        funHelp();
    }//GEN-LAST:event_txtBuyItemNameMouseClicked

    private void txtBuyItemNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtBuyItemNameKeyPressed
    {//GEN-HEADEREND:event_txtBuyItemNameKeyPressed
        // TODO add your handling code here:
        //Open help on '?' and '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            setAlwaysOnTop(false);
            funHelp();
        }
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            cmbType.requestFocus();
            // txtBuyQty.requestFocus();
        }
    }//GEN-LAST:event_txtBuyItemNameKeyPressed

    private void dteFromDateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteFromDateKeyPressed
    {//GEN-HEADEREND:event_dteFromDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteToDate.requestFocus();
        }
    }//GEN-LAST:event_dteFromDateKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteToDateKeyPressed
    {//GEN-HEADEREND:event_dteToDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbFromHour.requestFocus();
        }
    }//GEN-LAST:event_dteToDateKeyPressed

    private void cmbFromHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromHourKeyPressed
    {//GEN-HEADEREND:event_cmbFromHourKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbFromMinute.requestFocus();
        }
    }//GEN-LAST:event_cmbFromHourKeyPressed

    private void cmbFromMinuteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromMinuteKeyPressed
    {//GEN-HEADEREND:event_cmbFromMinuteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbFromAMPM.requestFocus();
        }
    }//GEN-LAST:event_cmbFromMinuteKeyPressed

    private void cmbFromAMPMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromAMPMKeyPressed
    {//GEN-HEADEREND:event_cmbFromAMPMKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbToHour.requestFocus();
        }
    }//GEN-LAST:event_cmbFromAMPMKeyPressed

    private void cmbToHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToHourKeyPressed
    {//GEN-HEADEREND:event_cmbToHourKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbToMinute.requestFocus();
        }
    }//GEN-LAST:event_cmbToHourKeyPressed

    private void cmbToMinuteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToMinuteKeyPressed
    {//GEN-HEADEREND:event_cmbToMinuteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbToAMPM.requestFocus();
        }
    }//GEN-LAST:event_cmbToMinuteKeyPressed

    private void cmbToAMPMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToAMPMKeyPressed
    {//GEN-HEADEREND:event_cmbToAMPMKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            btnAdd.requestFocus();
        }
    }//GEN-LAST:event_cmbToAMPMKeyPressed

    private void cmbOperatorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbOperatorActionPerformed
    {//GEN-HEADEREND:event_cmbOperatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbOperatorActionPerformed

    private void txtGetQtyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtGetQtyActionPerformed
    {//GEN-HEADEREND:event_txtGetQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGetQtyActionPerformed

    private void txtGetItemNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGetItemNameMouseClicked
    {//GEN-HEADEREND:event_txtGetItemNameMouseClicked
        setAlwaysOnTop(false);
        funHelpTable();
    }//GEN-LAST:event_txtGetItemNameMouseClicked

    private void txtPromotionNoteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPromotionNoteActionPerformed
    {//GEN-HEADEREND:event_txtPromotionNoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPromotionNoteActionPerformed

    private void txtPromotionNoteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPromotionNoteKeyPressed
    {//GEN-HEADEREND:event_txtPromotionNoteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtPromotionNoteKeyPressed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNewMouseClicked
    {//GEN-HEADEREND:event_btnNewMouseClicked

        if (btnNew.isEnabled())
        {
            btnNew.setEnabled(false);
            funSaveAndUpdateOpearation();
            btnNew.setEnabled(true);
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewActionPerformed
    {//GEN-HEADEREND:event_btnNewActionPerformed
        // TODO add your handling code here:
        //  funSaveAndUpdateOpearation();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnNewKeyPressed
    {//GEN-HEADEREND:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveAndUpdateOpearation();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnResetMouseClicked
    {//GEN-HEADEREND:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnCancelMouseClicked
    {//GEN-HEADEREND:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Promotion Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Promotion Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbFromAMPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFromAMPMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFromAMPMActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        funFillPromoDayTimeTable();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        funRemoveRowFromPromoDayTimeTable();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnResetGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetGridActionPerformed
        // TODO add your handling code here:
        funRsetPromoDayTimeGrid();
    }//GEN-LAST:event_btnResetGridActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Promotion Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Promotion Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtBuyQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuyQtyKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            Tab.setSelectedIndex(1); //open get tab
            cmbGetPromotionOn.requestFocus();
        }
    }//GEN-LAST:event_txtBuyQtyKeyPressed

    private void cmbGetPromotionOnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGetPromotionOnKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            txtGetItemName.requestFocus();
        }
    }//GEN-LAST:event_cmbGetPromotionOnKeyPressed

    private void txtGetItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetItemNameKeyPressed
        // TODO add your handling code here:
        //Open help on '?' and '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            setAlwaysOnTop(false);
            funHelp();
        }
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            txtGetQty.requestFocus();
        }
    }//GEN-LAST:event_txtGetItemNameKeyPressed

    private void cmbTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTypeKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            txtBuyQty.requestFocus();
        }
    }//GEN-LAST:event_cmbTypeKeyPressed

    private void txtGetQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGetQtyKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            cmbDiscount.requestFocus();
        }

    }//GEN-LAST:event_txtGetQtyKeyPressed

    private void cmbDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDiscountKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            // Tab.setSelectedIndex(1); //open get tab
            txtDiscount.requestFocus();
        }
    }//GEN-LAST:event_cmbDiscountKeyPressed

    private void txtDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            Tab.setSelectedIndex(2); //open day & time tab
            cmbDays.requestFocus();
        }
    }//GEN-LAST:event_txtDiscountKeyPressed

    private void cmbDaysKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDaysKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            cmbFromHour.requestFocus();
        }
    }//GEN-LAST:event_cmbDaysKeyPressed

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
        //focus to another field on click of enter key
        if (evt.getKeyCode() == 10)
        {
            funFillPromoDayTimeTable();
        }
    }//GEN-LAST:event_btnAddKeyPressed

    private void txtBuyQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuyQtyMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtBuyQty.getText().length() == 0)
            {
                frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Please Enter Buy qty");
                num.setVisible(true);
                if (null != clsGlobalVarClass.gNumerickeyboardValue)
                {
                    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                    {
                        txtBuyQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
                    }
                }
            }
            else
            {
                frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Please Enter Buy qty");
                num.setVisible(true);
                if (null != clsGlobalVarClass.gNumerickeyboardValue)
                {
                    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                    {
                        txtBuyQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
                    }
                }
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtBuyQtyMouseClicked

    private void txtGetQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGetQtyMouseClicked
        // TODO add your handling code here:

        try
        {
            if (txtGetQty.getText().length() == 0)
            {
                frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Please Enter Get qty");
                num.setVisible(true);
                if (null != clsGlobalVarClass.gNumerickeyboardValue)
                {
                    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                    {
                        txtGetQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
                    }
                }
            }
            else
            {
                frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Please Enter Get qty");
                num.setVisible(true);
                if (null != clsGlobalVarClass.gNumerickeyboardValue)
                {
                    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                    {
                        txtGetQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
                    }
                }
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtGetQtyMouseClicked

    private void txtDiscountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtDiscount.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, txtDiscount.getText(), "Double", "Please Enter Discount").setVisible(true);
                txtDiscount.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this, true, txtDiscount.getText(), "Double", "Please Enter Discount").setVisible(true);
                txtDiscount.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtDiscountMouseClicked

    private void cmbPOSItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPOSItemStateChanged
        // TODO add your handling code here:
        try
        {
            funFillAreaCombo(hmPOS.get(cmbPOS.getSelectedItem().toString()));
        }catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbPOSItemStateChanged

    private void cmbPGTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPGTypeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPGTypeKeyPressed

    private void cmbPGTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPGTypeItemStateChanged
        // TODO add your handling code here:
        funCheckPromoGroupType();
    }//GEN-LAST:event_cmbPGTypeItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetGrid;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbBuyPromotionOn;
    private javax.swing.JComboBox cmbDays;
    private javax.swing.JComboBox cmbDiscount;
    private javax.swing.JComboBox cmbFromAMPM;
    private javax.swing.JComboBox cmbFromHour;
    private javax.swing.JComboBox cmbFromMinute;
    private javax.swing.JComboBox cmbGetPromotionOn;
    private javax.swing.JComboBox cmbOperator;
    private javax.swing.JComboBox cmbPGType;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.JComboBox cmbToAMPM;
    private javax.swing.JComboBox cmbToHour;
    private javax.swing.JComboBox cmbToMinute;
    private javax.swing.JComboBox cmbType;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAreaName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblFrom;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblFromTime;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblIs;
    private javax.swing.JLabel lblKOTTimeBound;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPGType;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblPromoCode;
    private javax.swing.JLabel lblPromoName;
    private javax.swing.JLabel lblPromotionNote;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblToTime;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lbltype;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelBuyTab;
    private javax.swing.JPanel panelGetTab;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblBuyMenuItems;
    private javax.swing.JTable tblGetMenuItems;
    private javax.swing.JTable tblPromoDayTime;
    private javax.swing.JLabel tblToDate;
    private javax.swing.JTextField txtBuyItemName;
    private javax.swing.JTextField txtBuyQty;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGetItemName;
    private javax.swing.JTextField txtGetQty;
    private javax.swing.JTextField txtKOTTimeBound;
    private javax.swing.JTextField txtPromoCode;
    private javax.swing.JTextField txtPromoName;
    private javax.swing.JTextField txtPromotionNote;
    // End of variables declaration//GEN-END:variables

}
