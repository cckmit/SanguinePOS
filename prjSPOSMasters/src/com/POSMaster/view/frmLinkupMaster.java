/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;



import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsLinkupDtl;
import com.POSGlobal.controller.clsPOSLinkupDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSGlobal.controller.clsDebtorDtl;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.util.Collections;
import java.util.Comparator;

public class frmLinkupMaster extends javax.swing.JFrame
{

    private HashMap<String, String> mapPOSCode, mapPOSName;
    private StringBuilder sb = new StringBuilder();
    private ResultSet rs;
    DefaultTableModel dmItemLinkup, dmPOSLinkup, dmTaxLinkup, dmSubGroupLinkup, dmSettlementLinkup,dmCostCenterLinkup,dmCustomerLinkup;
    clsUtility objUtility = new clsUtility();
    
    public frmLinkupMaster()
    {
        initComponents();
        try
        {
            clsGlobalVarClass.gSearchItem = "";
            clsGlobalVarClass.gSearchFormName = "";
            dmItemLinkup = (DefaultTableModel) tblItemLinkupDtl.getModel();
            dmPOSLinkup = (DefaultTableModel) tblPOSLinkupDtl.getModel();
            dmTaxLinkup = (DefaultTableModel) tblTaxLinkupDtl.getModel();
            dmSubGroupLinkup = (DefaultTableModel) tblSubGroupLinkupDtl.getModel();
            dmSettlementLinkup = (DefaultTableModel) tblSettlementLinkupDtl.getModel();
            dmCostCenterLinkup = (DefaultTableModel) tblCostCenterLinkupDtl.getModel();
	    dmCustomerLinkup = (DefaultTableModel) tblCustomerLinkupDtl.getModel();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();
            funLoadItemLinkupTable();
            funLoadPOSLocationLinkupTable();
            funLoadTaxAccountLinkupTable();
            funLoadSubGroupAccountLinkupTable();
            funLoadSettlementAccountCodeLinkupTable();
            funLoadCostCenterLocationLinkupTable();
	    funLoadCustomerLinkupTable();
            funLoadPOSCombo();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    private void funSetShortCutKeys()
    {
        btnExit.setMnemonic('c');
        btnReset.setMnemonic('r');
    }

    private void funLoadPOSCombo() throws Exception
    {
        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
        mapPOSCode = new HashMap<String, String>();
        mapPOSName = new HashMap<String, String>();
        jComboBox1.addItem("All");
        mapPOSCode.put("All", "All");
        mapPOSName.put("All", "All");
        while (rsPOS.next())
        {
            jComboBox1.addItem(rsPOS.getString(2));
            mapPOSCode.put(rsPOS.getString(1), rsPOS.getString(2));
            mapPOSName.put(rsPOS.getString(2), rsPOS.getString(1));
        }

    }

    //Fill Item Linkup with WS Product Code table   
    private void funLoadItemLinkupTable()
    {

        try
        {
            String sql="";
            if(clsGlobalVarClass.gPOSWiseItemToMMSProductLinkUpYN)
            {
                 sql = " SELECT a.strItemCode,a.strItemName,a.strPosCode,if(a.strPosCode='All','All',c.strPosName),ifnull(b.strWSProductName,''),ifnull(b.strWSProductCode,'')"
                    + ",ifnull(b.strExciseBrandCode,''),ifnull(b.strExciseBrandName,'')    "
                    + "FROM tblmenuitempricingdtl a  "
                    + "left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode and (a.strPosCode=b.strPOSCode or a.strPosCode='All' ) "
                    + ",tblposmaster c  "
                    + "where (a.strPosCode=c.strPosCode  or a.strPosCode='All' ) "
                    + "group by a.strPosCode,a.strItemCode  "
                    + "order by b.strWSProductName asc,a.strItemName asc ";
            }
            else
            {
                sql = "select a.strItemCode,a.strItemName,'All','All',ifnull(b.strWSProductName,''),ifnull(b.strWSProductCode,''),ifnull(b.strExciseBrandCode,''),ifnull(b.strExciseBrandName,'') "
                        + "from tblitemmaster a "
                        + "left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode "
                        + "where a.strRawMaterial='N' "
                        + "GROUP BY a.strItemCode "
                        + "ORDER BY b.strWSProductName ASC,a.strItemName ASC; ";
            }

            
            //System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmItemLinkup.setRowCount(0);
            while (rs.next())
            {
                boolean flgFound = true;
                final Object[] column = new Object[7];
                column[0] = rs.getString(1);
                column[1] = rs.getString(2);
                column[2] = rs.getString(4);
                column[3] = rs.getString(6);
                column[4] = rs.getString(5);
                column[5] = rs.getString(7);
                column[6] = rs.getString(8);
                dmItemLinkup.addRow(column);

            }
            rs.close();
            tblItemLinkupDtl.setModel(dmItemLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblItemLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblItemLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblItemLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblItemLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
            tblItemLinkupDtl.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);

            tblItemLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblItemLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblItemLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblItemLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblItemLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(245);

            tblItemLinkupDtl.setSize(700, 850);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //help for fetching product code
    private String funProductCodeTextFieldClicked()
    {
        String prodDtl = "";
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listProducts = objLinkSangERP.funGetProductDtl(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Product Code");
            listColumns.add("Product Name");
            new frmSearchFormDialog(this, true, listProducts, "MMS Products", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                prodDtl = data[0].toString() + "#" + data[1].toString();
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
        return prodDtl;
    }

    //Fill Item Linkup with WS Product Code table   
    private void funLoadItemLinkupTable1()
    {

        try
        {
            final int rowNo = tblItemLinkupDtl.getSelectedRow();
            String prodDtl = funProductCodeTextFieldClicked();
            String[] data = prodDtl.split("#");
            tblItemLinkupDtl.setValueAt(data[0], rowNo, 3);
            tblItemLinkupDtl.setValueAt(data[1], rowNo, 4);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funItemPOSLinkupRowClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listProducts = objLinkSangERP.funGetProductDtl(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Product Code");
            listColumns.add("Product Name");
            new frmSearchFormDialog(this, true, listProducts, "MMS Products", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblItemLinkupDtl.getSelectedRow();
                tblItemLinkupDtl.setValueAt(data[0], rowNo, 3);
                tblItemLinkupDtl.setValueAt(data[1], rowNo, 4);
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

    //Save linking WS product Code with itemmaster in tblitemorderingdtl table
    private void funSaveDataToItemLinkupTable() throws Exception
    {
        int cnt = 0;

        String insertQuery = "";
        if (tblItemLinkupDtl.getRowCount() > 0)
        {
            insertQuery = "insert into tblitemmasterlinkupdtl (strItemCode,strWSProductCode,strWSProductName,strPOSCode,strExciseBrandCode,strExciseBrandName,strClientCode,strDataPostFlag) values ";
            for (int row = 0; row < tblItemLinkupDtl.getRowCount(); row++)
            {
//                if (tblItemLinkupDtl.getValueAt(row, 3).equals(""))
//                {
//
//                }
//                else
//                {
                    if (tblItemLinkupDtl.getValueAt(row, 2).equals("All"))
                    {
                        if (mapPOSCode.size() > 0)
                        {
                            for (Map.Entry<String, String> entry : mapPOSCode.entrySet())
                            {
                                if (!entry.getKey().equals("All"))
                                {
                                    if (cnt == 0)
                                    {
                                        insertQuery += "('" + tblItemLinkupDtl.getValueAt(row, 0) + "','" + tblItemLinkupDtl.getValueAt(row, 3) + "','" + tblItemLinkupDtl.getValueAt(row, 4) + "','" + entry.getKey() + "','"+tblItemLinkupDtl.getValueAt(row, 5).toString()+"','"+tblItemLinkupDtl.getValueAt(row, 6).toString()+"', '" + clsGlobalVarClass.gClientCode + "', 'N') ";
                                    }
                                    else
                                    {
                                        insertQuery += ",('" + tblItemLinkupDtl.getValueAt(row, 0) + "','" + tblItemLinkupDtl.getValueAt(row, 3) + "','" + tblItemLinkupDtl.getValueAt(row, 4) + "','" + entry.getKey() + "','"+tblItemLinkupDtl.getValueAt(row, 5).toString()+"','"+tblItemLinkupDtl.getValueAt(row, 6).toString()+"', '" + clsGlobalVarClass.gClientCode + "', 'N')";
                                    }
                                    cnt++;
                                }

                            }
                        }
                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblItemLinkupDtl.getValueAt(row, 0) + "','" + tblItemLinkupDtl.getValueAt(row, 3) + "','" + tblItemLinkupDtl.getValueAt(row, 4) + "','" + mapPOSName.get(tblItemLinkupDtl.getValueAt(row, 2)) + "','"+tblItemLinkupDtl.getValueAt(row, 5).toString()+"','"+tblItemLinkupDtl.getValueAt(row, 6).toString()+"', '" + clsGlobalVarClass.gClientCode + "', 'N') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblItemLinkupDtl.getValueAt(row, 0) + "','" + tblItemLinkupDtl.getValueAt(row, 3) + "','" + tblItemLinkupDtl.getValueAt(row, 4) + "','" + mapPOSName.get(tblItemLinkupDtl.getValueAt(row, 2)) + "','"+tblItemLinkupDtl.getValueAt(row, 5).toString()+"','"+tblItemLinkupDtl.getValueAt(row, 6).toString()+"', '" + clsGlobalVarClass.gClientCode + "', 'N')";
                        }
                        cnt++;
                    }

//                }
            }

        }
        if (cnt > 0)
        {
            System.out.println("cnt=" + cnt);
            System.out.println("insertQuery=" + insertQuery);
            String deleteQuery = " truncate table tblitemmasterlinkupdtl  ";
            clsGlobalVarClass.dbMysql.execute(deleteQuery);
            clsGlobalVarClass.dbMysql.execute(insertQuery);
        }
    }

    //Fill POS Linkup with WS Location Code table   
  /*  private void funLoadPOSLocationLinkupTable()
     {
        
     try
     {
            
     String sql = " select strPosCode,strPosName,strPosType,strDebitCardTransactionYN"
     + " ,strPropertyPOSCode,strCounterWiseBilling,strDelayedSettlementForDB"
     + " ,strBillPrinterPort ,strAdvReceiptPrinterPort,strOperationalYN"
     + ",strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo,strRoundOff,strTip,strDiscount"
     + ",strWSLocationCode,strExciseLicenceCode,strEnableShift "
     + " from tblposmaster order by strPosName ";
     //System.out.println(sql);
     ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
     dmPOSLinkup.setRowCount(0);
     while (rs.next())
     {
     boolean flgFound=true;
     final Object[] column = new Object[8];
     column[0] = rs.getString(1);
     column[1] = rs.getString(2);
     column[2] = rs.getString(18);
     column[3] = rs.getString(17);
     column[4] = rs.getString(16);
     column[5] = rs.getString(15);
     column[6] = false; 
     dmPOSLinkup.addRow(column);
                
                
     }
     rs.close();
     tblPOSLinkupDtl.setModel(dmPOSLinkup);

     DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
     leftRenderer.setHorizontalAlignment(JLabel.LEFT);
     tblPOSLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
     tblPOSLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
     tblPOSLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
     tblPOSLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
     tblPOSLinkupDtl.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
     tblPOSLinkupDtl.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
           

            
     tblPOSLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(100);
     tblPOSLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
     tblPOSLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(150);
     tblPOSLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(100);
     tblPOSLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(100);
     tblPOSLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(100);

     tblPOSLinkupDtl.setSize(700, 850);

     }
     catch (Exception e)
     {

     e.printStackTrace();
     }
     }   
     */
    private void funLoadPOSLocationLinkupTable()
    {
        Map<String, clsPOSLinkupDtl> mapPOSLinkupDtl = new HashMap<String, clsPOSLinkupDtl>();
        List<clsPOSLinkupDtl> listPosLinkup = new ArrayList<clsPOSLinkupDtl>();
        clsPOSLinkupDtl obj = null;
        try
        {
            dmPOSLinkup.setRowCount(0);
            String sqlDis = " select a.strPosCode,a.strPosName,a.strWSLocationCode,a.strWSLocationName,a.strDiscount,ifnull(b.strWBAccountName,'') "
                    + " from tblposmaster a left outer join tblaccountmaster b on a.strDiscount=b.strWBAccountCode "
                    + " order by a.strPosCode   ";
            //System.out.println(sql);
            ResultSet rsDis = clsGlobalVarClass.dbMysql.executeResultSet(sqlDis);

            while (rsDis.next())
            {

                obj = new clsPOSLinkupDtl();
                obj.setStrPOSCode(rsDis.getString(1));
                obj.setStrPOSName(rsDis.getString(2));
                obj.setStrWSLocationCode(rsDis.getString(3));
                obj.setStrWSLocationName(rsDis.getString(4));
                obj.setStrDisAccountCode(rsDis.getString(5));
                obj.setStrDisAcccountName(rsDis.getString(6));
                mapPOSLinkupDtl.put(rsDis.getString(1), obj);

            }
            rsDis.close();

            String sqlTip = " select a.strPosCode,a.strPosName,a.strWSLocationCode,a.strWSLocationName,a.strTip,ifnull(b.strWBAccountName,'') "
                    + "from tblposmaster a left outer join tblaccountmaster b on a.strTip=b.strWBAccountCode "
                    + "order by a.strPosCode   ";
            //System.out.println(sql);
            ResultSet rsTip = clsGlobalVarClass.dbMysql.executeResultSet(sqlTip);

            while (rsTip.next())
            {

                if (mapPOSLinkupDtl.size() > 0)
                {
                    obj = mapPOSLinkupDtl.get(rsTip.getString(1));
                    obj.setStrTipAccountCode(rsTip.getString(5));
                    obj.setStrTipAccountName(rsTip.getString(6));

                }
                mapPOSLinkupDtl.put(rsTip.getString(1), obj);
            }
            rsTip.close();

            String sqlRoundOff = " select a.strPosCode,a.strPosName,a.strWSLocationCode,a.strWSLocationName,a.strRoundOff,ifnull(b.strWBAccountName,'') "
                    + " from tblposmaster a left outer join tblaccountmaster b on a.strRoundOff=b.strWBAccountCode "
                    // + " order by b.strWBAccountCode desc,a.strPosName asc  ";
                    + " order by a.strPosCode  ";
            //System.out.println(sql);
            ResultSet rsRoundOff = clsGlobalVarClass.dbMysql.executeResultSet(sqlRoundOff);

            while (rsRoundOff.next())
            {

                if (mapPOSLinkupDtl.size() > 0)
                {
                    obj = mapPOSLinkupDtl.get(rsRoundOff.getString(1));
                    obj.setStrRoundoffAccountCode(rsRoundOff.getString(5));
                    obj.setStrRoundoffAccountName(rsRoundOff.getString(6));

                }
                mapPOSLinkupDtl.put(rsRoundOff.getString(1), obj);
            }
            rsRoundOff.close();

            String sqlExcise = "select a.strPosCode,a.strPosName,a.strExciseLicenceCode,a.strExciseLicenceName from tblposmaster a  ";
            //System.out.println(sql);
            ResultSet rsExcise = clsGlobalVarClass.dbMysql.executeResultSet(sqlExcise);

            while (rsExcise.next())
            {

                if (mapPOSLinkupDtl.size() > 0)
                {
                    obj = mapPOSLinkupDtl.get(rsExcise.getString(1));
                    obj.setStrExciseLicenceCode(rsExcise.getString(3));
                    obj.setStrExciseLicenceName(rsExcise.getString(4));

                }
                mapPOSLinkupDtl.put(rsExcise.getString(1), obj);
            }
            rsExcise.close();

            if (mapPOSLinkupDtl.size() > 0)
            {
                for (Map.Entry<String, clsPOSLinkupDtl> entry : mapPOSLinkupDtl.entrySet())
                {
                    obj = entry.getValue();
                    listPosLinkup.add(obj);
                }
            }

            Comparator<clsPOSLinkupDtl> posCodeComparator = new Comparator<clsPOSLinkupDtl>()
            {

                @Override
                public int compare(clsPOSLinkupDtl o1, clsPOSLinkupDtl o2)
                {
                    return o1.getStrPOSCode().compareToIgnoreCase(o2.getStrPOSCode());
                }
            };
            Collections.sort(listPosLinkup, posCodeComparator);
            if (listPosLinkup.size() > 0)
            {
                for (int cnt = 0; cnt < listPosLinkup.size(); cnt++)
                {
                    obj = listPosLinkup.get(cnt);
                    final Object[] column = new Object[12];
                    column[0] = obj.getStrPOSCode();
                    column[1] = obj.getStrPOSName();
                    column[2] = obj.getStrWSLocationCode();
                    column[3] = obj.getStrWSLocationName();
                    column[4] = obj.getStrDisAccountCode();
                    column[5] = obj.getStrDisAcccountName();
                    column[6] = obj.getStrTipAccountCode();
                    column[7] = obj.getStrTipAccountName();
                    column[8] = obj.getStrRoundoffAccountCode();
                    column[9] = obj.getStrRoundoffAccountName();
                    column[10] = obj.getStrExciseLicenceCode();
                    column[11] = obj.getStrExciseLicenceName();
                    dmPOSLinkup.addRow(column);
                }

            }

            tblPOSLinkupDtl.setModel(dmPOSLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblPOSLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(6).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(7).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(8).setCellRenderer(leftRenderer);
            tblPOSLinkupDtl.getColumnModel().getColumn(9).setCellRenderer(leftRenderer);

            tblPOSLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(70); //posCode
            tblPOSLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250); //posName
            tblPOSLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(90); //locCode
            tblPOSLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(200); //locName
            tblPOSLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(90); //disAccCode
            tblPOSLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(250); //disAccName
            tblPOSLinkupDtl.getColumnModel().getColumn(6).setPreferredWidth(90); //tipAccCode
            tblPOSLinkupDtl.getColumnModel().getColumn(7).setPreferredWidth(250); //tipAccName
            tblPOSLinkupDtl.getColumnModel().getColumn(8).setPreferredWidth(90); //roundoffAccCode
            tblPOSLinkupDtl.getColumnModel().getColumn(9).setPreferredWidth(250); //roundoffAccName

            tblPOSLinkupDtl.setSize(700, 1650);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Fetch Web Stock Location Code
    private void funtblPOSLocationLinkupRowClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listLocation = objLinkSangERP.funGetLocationDtls(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Location Code");
            listColumns.add("Location Name");
            new frmSearchFormDialog(this, true, listLocation, "MMS Locations", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblPOSLinkupDtl.getSelectedRow();
                tblPOSLinkupDtl.setValueAt(data[0], rowNo, 2);
                tblPOSLinkupDtl.setValueAt(data[1], rowNo, 3);
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
    
    
     //Fetch Web Stock Location Code
    private void funtblCostCenterLocationLinkupRowClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listLocation = objLinkSangERP.funGetLocationDtls(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Location Code");
            listColumns.add("Location Name");
            new frmSearchFormDialog(this, true, listLocation, "MMS Locations", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblCostCenterLinkupDtl.getSelectedRow();
                tblCostCenterLinkupDtl.setValueAt(data[0], rowNo, 2);
                tblCostCenterLinkupDtl.setValueAt(data[1], rowNo, 3);
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

    //Save linking WS location Code with posmaster tblposmaster
    private void funUpdatePOSMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";

        String insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
        if (tblPOSLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblPOSLinkupDtl.getRowCount(); row++)
            {
                if ((!tblPOSLinkupDtl.getValueAt(row, 4).toString().isEmpty()) && (!tblPOSLinkupDtl.getValueAt(row, 4).toString().equals("NA")) && (!tblPOSLinkupDtl.getValueAt(row, 5).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblPOSLinkupDtl.getValueAt(row, 4) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblPOSLinkupDtl.getValueAt(row, 4) + "','" + tblPOSLinkupDtl.getValueAt(row, 5) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblPOSLinkupDtl.getValueAt(row, 4) + "','" + tblPOSLinkupDtl.getValueAt(row, 5) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        cnt++;
                    }
                }
                else if ((!tblPOSLinkupDtl.getValueAt(row, 6).toString().isEmpty()) && (!tblPOSLinkupDtl.getValueAt(row, 6).toString().equals("NA")) && (!tblPOSLinkupDtl.getValueAt(row, 7).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblPOSLinkupDtl.getValueAt(row, 6) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblPOSLinkupDtl.getValueAt(row, 6) + "','" + tblPOSLinkupDtl.getValueAt(row, 7) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblPOSLinkupDtl.getValueAt(row, 6) + "','" + tblPOSLinkupDtl.getValueAt(row, 7) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        cnt++;
                    }
                }

                else if ((!tblPOSLinkupDtl.getValueAt(row, 8).toString().isEmpty()) && (!tblPOSLinkupDtl.getValueAt(row, 8).toString().equals("NA")) && (!tblPOSLinkupDtl.getValueAt(row, 9).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblPOSLinkupDtl.getValueAt(row, 8) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblPOSLinkupDtl.getValueAt(row, 8) + "','" + tblPOSLinkupDtl.getValueAt(row, 9) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblPOSLinkupDtl.getValueAt(row, 8) + "','" + tblPOSLinkupDtl.getValueAt(row, 9) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        cnt++;
                    }
                }

                sqlUpdate = " update tblposmaster set strWSLocationCode='" + tblPOSLinkupDtl.getValueAt(row, 2) + "',strWSLocationName='" + tblPOSLinkupDtl.getValueAt(row, 3) + "', "
                        + " strDiscount='" + tblPOSLinkupDtl.getValueAt(row, 4) + "',strTip='" + tblPOSLinkupDtl.getValueAt(row, 6) + "'"
                        + ",strRoundOff='" + tblPOSLinkupDtl.getValueAt(row, 8) + "',strExciseLicenceCode='"+tblPOSLinkupDtl.getValueAt(row, 10).toString()+"',strExciseLicenceName='"+tblPOSLinkupDtl.getValueAt(row, 11).toString()+"' "
                        + " where strPosCode='" + tblPOSLinkupDtl.getValueAt(row, 0) + "' ";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);

            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }

        }
    }

    //Fill Tax Linkup with WB Account Code table   
    private void funLoadTaxAccountLinkupTable()
    {

        try
        {

            String sql = " select a.strTaxCode as Tax_Code,a.strTaxDesc as Tax_Desc,a.strAccountCode as Account_Code ,ifnull(b.strWBAccountName,'') "
                    + " from tbltaxhd a left outer join tblaccountmaster b on a.strAccountCode=b.strWBAccountCode "
                    + " order by strTaxDesc ";
            //System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmTaxLinkup.setRowCount(0);
            while (rs.next())
            {
                final Object[] column = new Object[7];
                column[0] = rs.getString(1);
                column[1] = rs.getString(2);
                column[2] = rs.getString(3);
                column[3] = rs.getString(4);
                column[4] = false;
                dmTaxLinkup.addRow(column);

            }
            rs.close();
            tblTaxLinkupDtl.setModel(dmTaxLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblTaxLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblTaxLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblTaxLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblTaxLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            tblTaxLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblTaxLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblTaxLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblTaxLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);

            tblTaxLinkupDtl.setSize(700, 850);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Save linking WB Account Code with taxmaster tblTaxMaster
    private void funUpdateTaxMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";

        String insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
        if (tblTaxLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblTaxLinkupDtl.getRowCount(); row++)
            {
                if ((!tblTaxLinkupDtl.getValueAt(row, 2).toString().isEmpty()) && (!tblTaxLinkupDtl.getValueAt(row, 2).toString().equals("NA")) && (!tblTaxLinkupDtl.getValueAt(row, 3).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblTaxLinkupDtl.getValueAt(row, 2) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblTaxLinkupDtl.getValueAt(row, 2) + "','" + tblTaxLinkupDtl.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblTaxLinkupDtl.getValueAt(row, 2) + "','" + tblTaxLinkupDtl.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        cnt++;
                    }
                }

                sqlUpdate = "update tbltaxhd set strAccountCode='" + tblTaxLinkupDtl.getValueAt(row, 2) + "' where strTaxCode='" + tblTaxLinkupDtl.getValueAt(row, 0) + "'";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);
            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }

        }

    }

    //Fill SubGroup Linkup with WB Account Code table   
    private void funLoadSubGroupAccountLinkupTable()
    {

        try
        {

            String sql = " select a.strSubGroupCode as SubGroup_Code,a.strSubGroupName as SubGroup_Name,a.strAccountCode as Account_Code,ifnull(b.strWBAccountName,'') "
		    + " ,ifnull(c.strWSSubGroupCode,''),ifnull(c.strWSSubGroupName,'')"
                    + " from tblsubgrouphd a left outer join tblaccountmaster b on a.strAccountCode=b.strWBAccountCode "
		    + " left outer join tblsubgroupmasterlinkupdtl c on a.strSubGroupCode=c.strSubGrooupCode"
                    + " order by strSubGroupName ";

            //System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmSubGroupLinkup.setRowCount(0);
            while (rs.next())
            {
                final Object[] column = new Object[7];
                column[0] = rs.getString(1);
                column[1] = rs.getString(2);
                column[2] = rs.getString(3);
                column[3] = rs.getString(4);
                column[4] = rs.getString(5);
		column[5] = rs.getString(6);
                dmSubGroupLinkup.addRow(column);

            }
            rs.close();
            tblSubGroupLinkupDtl.setModel(dmSubGroupLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
	    tblSubGroupLinkupDtl.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
	    tblSubGroupLinkupDtl.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);

            tblSubGroupLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);
	    tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(100);
	    tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(200);

            tblSubGroupLinkupDtl.setSize(700, 850);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    //Save linking WB Account Code with subgroup in tblsubgrouphd
    private void funUpdateSubGroupMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";
        String insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
        if (tblSubGroupLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblSubGroupLinkupDtl.getRowCount(); row++)
            {
                if ((!tblSubGroupLinkupDtl.getValueAt(row, 2).toString().isEmpty()) && (!tblSubGroupLinkupDtl.getValueAt(row, 2).toString().equals("NA")) && (!tblSubGroupLinkupDtl.getValueAt(row, 3).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblSubGroupLinkupDtl.getValueAt(row, 2) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
//                        if (cnt == 0)
//                        {
//                            insertQuery += "('" + tblSubGroupLinkupDtl.getValueAt(row, 2) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "') ";
//                        }
//                        else
//                        {
                            insertQuery += "('" + tblSubGroupLinkupDtl.getValueAt(row, 2) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "') ";
//                        }
                        clsGlobalVarClass.dbMysql.execute(insertQuery);
//                        cnt++;
                    }
                }

                sqlUpdate = "update tblsubgrouphd set strAccountCode='" + tblSubGroupLinkupDtl.getValueAt(row, 2) + "' where strSubGroupCode='" + tblSubGroupLinkupDtl.getValueAt(row, 0) + "'";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);
            }

//            if (cnt > 0)
//            {
//                clsGlobalVarClass.dbMysql.execute(insertQuery);
//            }

        }

    }

    //Fill Settlement master Linkup with WB Account Code table   
    private void funLoadSettlementAccountCodeLinkupTable()
    {

        try
        {

            String sql = " select a.strSettelmentCode as Settlement_Code,a.strSettelmentDesc as Settlement_Desc,a.strSettelmentType as Settlement_Type,a.strAccountCode as Account_Code,ifnull(b.strWBAccountName,'') "
                    + " from tblsettelmenthd a left outer join tblaccountmaster b on a.strAccountCode=b.strWBAccountCode "
                    + " order by strSettelmentDesc ";
            //System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmSettlementLinkup.setRowCount(0);
            while (rs.next())
            {
                final Object[] column = new Object[7];
                column[0] = rs.getString(1);
                column[1] = rs.getString(2);
                column[2] = rs.getString(3);
                column[3] = rs.getString(4);
                column[4] = rs.getString(5);
                column[6] = false;
                dmSettlementLinkup.addRow(column);

            }
            rs.close();
            tblSettlementLinkupDtl.setModel(dmSettlementLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblSettlementLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblSettlementLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblSettlementLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblSettlementLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
            tblSettlementLinkupDtl.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);

            tblSettlementLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblSettlementLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(180);
            tblSettlementLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(130);
            tblSettlementLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblSettlementLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(180);

            tblSettlementLinkupDtl.setSize(700, 850);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Save linking WB Account Code with settlement in tblsettelmenthd
    private void funUpdateSettlementMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";

        String insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
        if (tblSettlementLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblSettlementLinkupDtl.getRowCount(); row++)
            {
                if ((!tblSettlementLinkupDtl.getValueAt(row, 3).toString().isEmpty()) && (!tblSettlementLinkupDtl.getValueAt(row, 3).toString().equals("NA")) && (!tblSettlementLinkupDtl.getValueAt(row, 4).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblSettlementLinkupDtl.getValueAt(row, 3) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblSettlementLinkupDtl.getValueAt(row, 3) + "','" + tblSettlementLinkupDtl.getValueAt(row, 4) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblSettlementLinkupDtl.getValueAt(row, 3) + "','" + tblSettlementLinkupDtl.getValueAt(row, 4) + "','" + clsGlobalVarClass.gClientCode + "') ";
                        }
                        cnt++;
                    }
                }

                sqlUpdate = "update tblsettelmenthd set strAccountCode='" + tblSettlementLinkupDtl.getValueAt(row, 3) + "' where strSettelmentCode='" + tblSettlementLinkupDtl.getValueAt(row, 0) + "'";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);
            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }

        }

    }
    
    
      //Save linking WS location Code with posmaster tblposmaster
    private void funUpdateCostCenterMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";

        String insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
        if (tblCostCenterLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblCostCenterLinkupDtl.getRowCount(); row++)
            {
                sqlUpdate = " update tblcostcentermaster set strWSLocationCode='" + tblCostCenterLinkupDtl.getValueAt(row, 2) + "',strWSLocationName='" + tblCostCenterLinkupDtl.getValueAt(row, 3) + "'  "
                       + " where strCostCenterCode='" + tblCostCenterLinkupDtl.getValueAt(row, 0) + "' and strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);
            }

        }
    }
    
    //Save linking WB Account Code with subgroup in tblsubgrouphd
    private void funUpdateCustomerMasterTable() throws Exception
    {
        int cnt = 0;
        String sqlUpdate = "";
        String insertQuery ="";
        if (tblCustomerLinkupDtl.getRowCount() > 0)
        {
            for (int row = 0; row < tblCustomerLinkupDtl.getRowCount(); row++)
            {
                if ((!tblCustomerLinkupDtl.getValueAt(row, 2).toString().isEmpty()) && (!tblCustomerLinkupDtl.getValueAt(row, 2).toString().equals("NA")) && (!tblCustomerLinkupDtl.getValueAt(row, 3).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblCustomerLinkupDtl.getValueAt(row, 2) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
			 insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
                        insertQuery += "('" + tblCustomerLinkupDtl.getValueAt(row, 2) + "','" + tblCustomerLinkupDtl.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "') ";                      
                        clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
                }
		else
		{
		    
		}
		if ((!tblCustomerLinkupDtl.getValueAt(row, 4).toString().isEmpty()) && (!tblCustomerLinkupDtl.getValueAt(row, 4).toString().equals("NA")) && (!tblCustomerLinkupDtl.getValueAt(row, 5).toString().isEmpty()))
                {
                    String selectQuery = "select * from tblaccountmaster a where a.strWBAccountCode='" + tblCustomerLinkupDtl.getValueAt(row, 4) + "' and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    if (rs.next())
                    {

                    }
                    else
                    {
			 insertQuery = "insert into tblaccountmaster (strWBAccountCode,strWBAccountName,strClientCode) values ";
                        insertQuery += "('" + tblCustomerLinkupDtl.getValueAt(row, 4) + "','" + tblCustomerLinkupDtl.getValueAt(row, 5) + "','" + clsGlobalVarClass.gClientCode + "') ";                      
                        clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
                }
		else
		{
		    
		}

                sqlUpdate = "update tblcustomermaster set strAccountCode='" + tblCustomerLinkupDtl.getValueAt(row, 2) + "',strDebtorCode='"+tblCustomerLinkupDtl.getValueAt(row, 4) +"' where strCustomerCode='" + tblCustomerLinkupDtl.getValueAt(row, 0) + "'";
                clsGlobalVarClass.dbMysql.execute(sqlUpdate);
            }
        }
    }
    
    
     private void funLoadCostCenterLocationLinkupTable()
    {
        Map<String, clsPOSLinkupDtl> mapCostCenterLinkupDtl = new HashMap<String, clsPOSLinkupDtl>();
        List<clsPOSLinkupDtl> listCostCenterLinkup = new ArrayList<clsPOSLinkupDtl>();
        clsPOSLinkupDtl obj = null;
        try
        {
            dmCostCenterLinkup.setRowCount(0);
            String sqlDis = " select a.strCostCenterCode,a.strCostCenterName,a.strWSLocationCode,a.strWSLocationName"
                    + " from tblcostcentermaster a where a.strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                    + " order by a.strCostCenterCode   ";
            //System.out.println(sql);
            ResultSet rsDis = clsGlobalVarClass.dbMysql.executeResultSet(sqlDis);

            while (rsDis.next())
            {

                obj = new clsPOSLinkupDtl();
                obj.setStrCostCenterCode(rsDis.getString(1));
                obj.setStrCostCenterName(rsDis.getString(2));
                obj.setStrWSLocationCode(rsDis.getString(3));
                obj.setStrWSLocationName(rsDis.getString(4));
               
                mapCostCenterLinkupDtl.put(rsDis.getString(1), obj);

            }
            rsDis.close();
         if (mapCostCenterLinkupDtl.size() > 0)
            {
                for (Map.Entry<String, clsPOSLinkupDtl> entry : mapCostCenterLinkupDtl.entrySet())
                {
                    obj = entry.getValue();
                    listCostCenterLinkup.add(obj);
                }
            }

            Comparator<clsPOSLinkupDtl> CostCenterCodeComparator = new Comparator<clsPOSLinkupDtl>()
            {

                @Override
                public int compare(clsPOSLinkupDtl o1, clsPOSLinkupDtl o2)
                {
                    return o1.getStrCostCenterCode().compareToIgnoreCase(o2.getStrCostCenterCode());
                }
            };
            Collections.sort(listCostCenterLinkup, CostCenterCodeComparator);
            if (listCostCenterLinkup.size() > 0)
            {
                for (int cnt = 0; cnt < listCostCenterLinkup.size(); cnt++)
                {
                    obj = listCostCenterLinkup.get(cnt);
                    final Object[] column = new Object[12];
                    column[0] = obj.getStrCostCenterCode();
                    column[1] = obj.getStrCostCenterName();
                    column[2] = obj.getStrWSLocationCode();
                    column[3] = obj.getStrWSLocationName();
                  
                    dmCostCenterLinkup.addRow(column);
                }

            }

            tblCostCenterLinkupDtl.setModel(dmCostCenterLinkup);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
            

            tblCostCenterLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(70); //CostCenterCode
            tblCostCenterLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250); //CostCenterName
            tblCostCenterLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(90); //locCode
            tblCostCenterLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(200); //locName
           

            tblCostCenterLinkupDtl.setSize(700, 1650);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    

    //Fetch Web Book Account Code for All master
    private void funAccountLinkupHelp(JTable selectedTable, int columnNo)
    {
       clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsAccountDtl> accountInfo = objLinkSangERP.funGetAccountDtl("GL Code", clsGlobalVarClass.gClientCode);
            new frmSearchFormDialog(this, true, accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = selectedTable.getSelectedRow();
                selectedTable.setValueAt(data[0], rowNo, columnNo);
                selectedTable.setValueAt(data[1], rowNo, columnNo + 1);
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
    
    private void funAccountLinkupAllHelp(JTable selectedTable, int columnNo)
    {
       clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsAccountDtl> accountInfo = objLinkSangERP.funGetAccountDtl("All", clsGlobalVarClass.gClientCode);
            new frmSearchFormDialog(this, true, accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = selectedTable.getSelectedRow();
                selectedTable.setValueAt(data[0], rowNo, columnNo);
                selectedTable.setValueAt(data[1], rowNo, columnNo + 1);
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

    /**
     * This method is used to save data
     */
    private void funSaveLinkup()
    {
        try
        {
            int cnt = 0;
            int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to Update All Linkup?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION)
            {
                funSaveDataToItemLinkupTable();
                funUpdatePOSMasterTable();
                funUpdateTaxMasterTable();
                funUpdateSubGroupMasterTable();
                funUpdateSettlementMasterTable();
                funUpdateCostCenterMasterTable();
		funUpdateCustomerMasterTable();
                funSaveDataToSubGroupLinkupTable();
		new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                funResetFields();
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

        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblProductName = new javax.swing.JLabel();
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
        tabLinkupMaster = new javax.swing.JTabbedPane();
        panelItem = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        separator1 = new javax.swing.JSeparator();
        srollPane = new javax.swing.JScrollPane();
        tblItemLinkupDtl = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        panelPOS = new javax.swing.JPanel();
        separator2 = new javax.swing.JSeparator();
        srollPane1 = new javax.swing.JScrollPane();
        tblPOSLinkupDtl = new javax.swing.JTable();
        panelTax = new javax.swing.JPanel();
        separator3 = new javax.swing.JSeparator();
        srollPane2 = new javax.swing.JScrollPane();
        tblTaxLinkupDtl = new javax.swing.JTable();
        panelSubGroup = new javax.swing.JPanel();
        separator4 = new javax.swing.JSeparator();
        srollPane3 = new javax.swing.JScrollPane();
        tblSubGroupLinkupDtl = new javax.swing.JTable();
        panelSettlement = new javax.swing.JPanel();
        separator5 = new javax.swing.JSeparator();
        srollPane4 = new javax.swing.JScrollPane();
        tblSettlementLinkupDtl = new javax.swing.JTable();
        panelCostCenter = new javax.swing.JPanel();
        separator6 = new javax.swing.JSeparator();
        srollPane5 = new javax.swing.JScrollPane();
        tblCostCenterLinkupDtl = new javax.swing.JTable();
        panelCustomer = new javax.swing.JPanel();
        separator7 = new javax.swing.JSeparator();
        srollPane6 = new javax.swing.JScrollPane();
        tblCustomerLinkupDtl = new javax.swing.JTable();

        jTextField1.setText("jTextField1");

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

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Linkup Master");
        lblformName.setMaximumSize(new java.awt.Dimension(170, 17));
        lblformName.setMinimumSize(new java.awt.Dimension(170, 17));
        lblformName.setPreferredSize(new java.awt.Dimension(170, 17));
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

        panelItem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelItem.setMinimumSize(new java.awt.Dimension(800, 570));
        panelItem.setOpaque(false);
        panelItem.setPreferredSize(new java.awt.Dimension(800, 570));

        btnExit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExit.setForeground(new java.awt.Color(251, 246, 246));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.setToolTipText("Close Menu Item Pricing");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExitMouseClicked(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExitActionPerformed(evt);
            }
        });

        tblItemLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Code", "Item Name", "POS Name", "WS Prd Code", "WebStock Product Name", "Excise Brand Code", "Excise Brand Name"
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
        tblItemLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblItemLinkupDtl.setRowHeight(25);
        tblItemLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblItemLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblItemLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblItemLinkupDtlFocusLost(evt);
            }
        });
        tblItemLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemLinkupDtlMouseClicked(evt);
            }
        });
        tblItemLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblItemLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblItemLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblItemLinkupDtlKeyPressed(evt);
            }
        });
        srollPane.setViewportView(tblItemLinkupDtl);
        if (tblItemLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblItemLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblItemLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblItemLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblItemLinkupDtl.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jComboBox1));
            tblItemLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblItemLinkupDtl.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jTextField1));
            tblItemLinkupDtl.getColumnModel().getColumn(4).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(245);
            tblItemLinkupDtl.getColumnModel().getColumn(5).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(120);
            tblItemLinkupDtl.getColumnModel().getColumn(6).setResizable(false);
            tblItemLinkupDtl.getColumnModel().getColumn(6).setPreferredWidth(250);
        }

        btnReset.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnReset.setForeground(new java.awt.Color(250, 243, 243));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnSave.setForeground(new java.awt.Color(251, 246, 246));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setToolTipText("Close Menu Item Pricing");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                btnSaveVetoableChange(evt);
            }
        });

        javax.swing.GroupLayout panelItemLayout = new javax.swing.GroupLayout(panelItem);
        panelItem.setLayout(panelItemLayout);
        panelItemLayout.setHorizontalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addComponent(srollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
            .addGroup(panelItemLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelItemLayout.setVerticalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addGroup(panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelItemLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelItemLayout.createSequentialGroup()
                        .addComponent(srollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        tabLinkupMaster.addTab("Item", panelItem);

        panelPOS.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelPOS.setMinimumSize(new java.awt.Dimension(800, 570));
        panelPOS.setOpaque(false);

        tblPOSLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS Code", "POS Name", "WS Loc Code", "WS Location Name", "Dis Acc Code", "Dis Acc Name", "Tip Acc Code", "Tip Acc Name", "Round Off Acc Code", "Round Off Acc Name", "Excise License Code", "Excise License Name"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPOSLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPOSLinkupDtl.setRowHeight(25);
        tblPOSLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblPOSLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblPOSLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblPOSLinkupDtlFocusLost(evt);
            }
        });
        tblPOSLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPOSLinkupDtlMouseClicked(evt);
            }
        });
        tblPOSLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblPOSLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblPOSLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblPOSLinkupDtlKeyPressed(evt);
            }
        });
        srollPane1.setViewportView(tblPOSLinkupDtl);
        if (tblPOSLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblPOSLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(130);
            tblPOSLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(130);
            tblPOSLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(4).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblPOSLinkupDtl.getColumnModel().getColumn(4).setHeaderValue("Dis Acc Code");
            tblPOSLinkupDtl.getColumnModel().getColumn(5).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(5).setHeaderValue("Dis Acc Name");
            tblPOSLinkupDtl.getColumnModel().getColumn(6).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(6).setPreferredWidth(90);
            tblPOSLinkupDtl.getColumnModel().getColumn(6).setHeaderValue("Tip Acc Code");
            tblPOSLinkupDtl.getColumnModel().getColumn(7).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(7).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(7).setHeaderValue("Tip Acc Name");
            tblPOSLinkupDtl.getColumnModel().getColumn(8).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(8).setPreferredWidth(120);
            tblPOSLinkupDtl.getColumnModel().getColumn(8).setHeaderValue("Round Off Acc Code");
            tblPOSLinkupDtl.getColumnModel().getColumn(9).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(9).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(9).setHeaderValue("Round Off Acc Name");
            tblPOSLinkupDtl.getColumnModel().getColumn(10).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(10).setPreferredWidth(120);
            tblPOSLinkupDtl.getColumnModel().getColumn(10).setHeaderValue("Excise License Code");
            tblPOSLinkupDtl.getColumnModel().getColumn(11).setResizable(false);
            tblPOSLinkupDtl.getColumnModel().getColumn(11).setPreferredWidth(250);
            tblPOSLinkupDtl.getColumnModel().getColumn(11).setHeaderValue("Excise License Name");
        }

        javax.swing.GroupLayout panelPOSLayout = new javax.swing.GroupLayout(panelPOS);
        panelPOS.setLayout(panelPOSLayout);
        panelPOSLayout.setHorizontalGroup(
            panelPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPOSLayout.createSequentialGroup()
                .addComponent(srollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator2, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        panelPOSLayout.setVerticalGroup(
            panelPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPOSLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(498, Short.MAX_VALUE))
            .addGroup(panelPOSLayout.createSequentialGroup()
                .addComponent(srollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("POS", panelPOS);

        panelTax.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelTax.setMinimumSize(new java.awt.Dimension(800, 570));
        panelTax.setOpaque(false);

        tblTaxLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Tax Code", "Tax Name", "WebBook Account Code", "WebBook Account Name"
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
        tblTaxLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTaxLinkupDtl.setRowHeight(25);
        tblTaxLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblTaxLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblTaxLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblTaxLinkupDtlFocusLost(evt);
            }
        });
        tblTaxLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTaxLinkupDtlMouseClicked(evt);
            }
        });
        tblTaxLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblTaxLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblTaxLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblTaxLinkupDtlKeyPressed(evt);
            }
        });
        srollPane2.setViewportView(tblTaxLinkupDtl);
        if (tblTaxLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblTaxLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblTaxLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblTaxLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblTaxLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblTaxLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblTaxLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblTaxLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblTaxLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);
        }

        javax.swing.GroupLayout panelTaxLayout = new javax.swing.GroupLayout(panelTax);
        panelTax.setLayout(panelTaxLayout);
        panelTaxLayout.setHorizontalGroup(
            panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaxLayout.createSequentialGroup()
                .addComponent(srollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator3, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        panelTaxLayout.setVerticalGroup(
            panelTaxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaxLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(498, Short.MAX_VALUE))
            .addGroup(panelTaxLayout.createSequentialGroup()
                .addComponent(srollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("Tax", panelTax);

        panelSubGroup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelSubGroup.setMinimumSize(new java.awt.Dimension(800, 570));
        panelSubGroup.setOpaque(false);

        tblSubGroupLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "SubGroup Code", "SubGroup Name", "WebBook Account Code", "WebBook Account Name", "WS SubGroup Code", "WS SubGroup Name"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblSubGroupLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSubGroupLinkupDtl.setRowHeight(25);
        tblSubGroupLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblSubGroupLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblSubGroupLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblSubGroupLinkupDtlFocusLost(evt);
            }
        });
        tblSubGroupLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblSubGroupLinkupDtlMouseClicked(evt);
            }
        });
        tblSubGroupLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblSubGroupLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblSubGroupLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblSubGroupLinkupDtlKeyPressed(evt);
            }
        });
        srollPane3.setViewportView(tblSubGroupLinkupDtl);
        if (tblSubGroupLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblSubGroupLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(4).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(5).setResizable(false);
            tblSubGroupLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        javax.swing.GroupLayout panelSubGroupLayout = new javax.swing.GroupLayout(panelSubGroup);
        panelSubGroup.setLayout(panelSubGroupLayout);
        panelSubGroupLayout.setHorizontalGroup(
            panelSubGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSubGroupLayout.createSequentialGroup()
                .addComponent(srollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator4, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        panelSubGroupLayout.setVerticalGroup(
            panelSubGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSubGroupLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(498, Short.MAX_VALUE))
            .addGroup(panelSubGroupLayout.createSequentialGroup()
                .addComponent(srollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("SubGroup", panelSubGroup);

        panelSettlement.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelSettlement.setMinimumSize(new java.awt.Dimension(800, 570));
        panelSettlement.setOpaque(false);

        tblSettlementLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Settlement Code", "Settlement Name", "Settlement", "WebBook Account Code", "WebBook Account Name"
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
        tblSettlementLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSettlementLinkupDtl.setRowHeight(25);
        tblSettlementLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblSettlementLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblSettlementLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblSettlementLinkupDtlFocusLost(evt);
            }
        });
        tblSettlementLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblSettlementLinkupDtlMouseClicked(evt);
            }
        });
        tblSettlementLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblSettlementLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblSettlementLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblSettlementLinkupDtlKeyPressed(evt);
            }
        });
        srollPane4.setViewportView(tblSettlementLinkupDtl);
        if (tblSettlementLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblSettlementLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblSettlementLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblSettlementLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblSettlementLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblSettlementLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblSettlementLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblSettlementLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblSettlementLinkupDtl.getColumnModel().getColumn(4).setResizable(false);
            tblSettlementLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(250);
        }

        javax.swing.GroupLayout panelSettlementLayout = new javax.swing.GroupLayout(panelSettlement);
        panelSettlement.setLayout(panelSettlementLayout);
        panelSettlementLayout.setHorizontalGroup(
            panelSettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettlementLayout.createSequentialGroup()
                .addComponent(srollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator5, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        panelSettlementLayout.setVerticalGroup(
            panelSettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettlementLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(498, Short.MAX_VALUE))
            .addGroup(panelSettlementLayout.createSequentialGroup()
                .addComponent(srollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("Settlement", panelSettlement);

        panelCostCenter.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelCostCenter.setMinimumSize(new java.awt.Dimension(800, 570));
        panelCostCenter.setOpaque(false);

        tblCostCenterLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "CostCenter Code", "CostCenter Name", "WS Loc Code", "WS Location Name"
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
        tblCostCenterLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCostCenterLinkupDtl.setRowHeight(25);
        tblCostCenterLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblCostCenterLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblCostCenterLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblCostCenterLinkupDtlFocusLost(evt);
            }
        });
        tblCostCenterLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCostCenterLinkupDtlMouseClicked(evt);
            }
        });
        tblCostCenterLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblCostCenterLinkupDtlInputMethodTextChanged(evt);
            }
        });
        tblCostCenterLinkupDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblCostCenterLinkupDtlKeyPressed(evt);
            }
        });
        srollPane5.setViewportView(tblCostCenterLinkupDtl);
        if (tblCostCenterLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblCostCenterLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(130);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(130);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblCostCenterLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);
        }

        javax.swing.GroupLayout panelCostCenterLayout = new javax.swing.GroupLayout(panelCostCenter);
        panelCostCenter.setLayout(panelCostCenterLayout);
        panelCostCenterLayout.setHorizontalGroup(
            panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCostCenterLayout.createSequentialGroup()
                .addComponent(srollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator6, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        panelCostCenterLayout.setVerticalGroup(
            panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCostCenterLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(498, Short.MAX_VALUE))
            .addGroup(panelCostCenterLayout.createSequentialGroup()
                .addComponent(srollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("Cost Center", panelCostCenter);

        tblCustomerLinkupDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Customer Code", "Customer Name", "Account Code", "Account Name", "Debtor Code", "Debtor Full Name"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblCustomerLinkupDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCustomerLinkupDtl.setRowHeight(25);
        tblCustomerLinkupDtl.getTableHeader().setReorderingAllowed(false);
        tblCustomerLinkupDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblCustomerLinkupDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblCustomerLinkupDtlFocusLost(evt);
            }
        });
        tblCustomerLinkupDtl.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCustomerLinkupDtlMouseClicked(evt);
            }
        });
        tblCustomerLinkupDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblCustomerLinkupDtlInputMethodTextChanged(evt);
            }
        });
        srollPane6.setViewportView(tblCustomerLinkupDtl);
        if (tblCustomerLinkupDtl.getColumnModel().getColumnCount() > 0)
        {
            tblCustomerLinkupDtl.getColumnModel().getColumn(0).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCustomerLinkupDtl.getColumnModel().getColumn(1).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblCustomerLinkupDtl.getColumnModel().getColumn(2).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblCustomerLinkupDtl.getColumnModel().getColumn(3).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblCustomerLinkupDtl.getColumnModel().getColumn(4).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(4).setPreferredWidth(150);
            tblCustomerLinkupDtl.getColumnModel().getColumn(5).setResizable(false);
            tblCustomerLinkupDtl.getColumnModel().getColumn(5).setPreferredWidth(250);
        }

        javax.swing.GroupLayout panelCustomerLayout = new javax.swing.GroupLayout(panelCustomer);
        panelCustomer.setLayout(panelCustomerLayout);
        panelCustomerLayout.setHorizontalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addComponent(srollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator7, javax.swing.GroupLayout.DEFAULT_SIZE, 3, Short.MAX_VALUE))
        );
        panelCustomerLayout.setVerticalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(separator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(502, Short.MAX_VALUE))
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addComponent(srollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabLinkupMaster.addTab("Customer", panelCustomer);

        panelLayout.add(tabLinkupMaster, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Linkup Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Linkup Master");
    }//GEN-LAST:event_formWindowClosing

    private void tblItemLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemLinkupDtlKeyPressed
        // TODO add your handling code here:
        // System.out.println("key code="+evt.getKeyCode()+"\tChar="+evt.getKeyChar()+"\textendes key code"+evt.getExtendedKeyCode());

    }//GEN-LAST:event_tblItemLinkupDtlKeyPressed

    private void tblItemLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblItemLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblItemLinkupDtlInputMethodTextChanged

    private void tblItemLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblItemLinkupDtlFocusLost

    }//GEN-LAST:event_tblItemLinkupDtlFocusLost

    private void tblItemLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblItemLinkupDtlFocusGained


    }//GEN-LAST:event_tblItemLinkupDtlFocusGained

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        clsGlobalVarClass.hmActiveForms.remove("Linkup Master");
        funExitClick();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExitMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        funResetClick();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSaveVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_btnSaveVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveVetoableChange

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        funSaveLinkup();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
            // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveMouseClicked

    private void tblPOSLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPOSLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPOSLinkupDtlFocusGained

    private void tblPOSLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPOSLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPOSLinkupDtlFocusLost

    private void tblPOSLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPOSLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPOSLinkupDtlInputMethodTextChanged

    private void tblPOSLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPOSLinkupDtlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPOSLinkupDtlKeyPressed

    private void tblTaxLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblTaxLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTaxLinkupDtlFocusGained

    private void tblTaxLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblTaxLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTaxLinkupDtlFocusLost

    private void tblTaxLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblTaxLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTaxLinkupDtlInputMethodTextChanged

    private void tblTaxLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTaxLinkupDtlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTaxLinkupDtlKeyPressed

    private void tblSubGroupLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSubGroupLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSubGroupLinkupDtlFocusGained

    private void tblSubGroupLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSubGroupLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSubGroupLinkupDtlFocusLost

    private void tblSubGroupLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblSubGroupLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSubGroupLinkupDtlInputMethodTextChanged

    private void tblSubGroupLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSubGroupLinkupDtlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSubGroupLinkupDtlKeyPressed

    private void tblSettlementLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSettlementLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementLinkupDtlFocusGained

    private void tblSettlementLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSettlementLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementLinkupDtlFocusLost

    private void tblSettlementLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblSettlementLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementLinkupDtlInputMethodTextChanged

    private void tblSettlementLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSettlementLinkupDtlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSettlementLinkupDtlKeyPressed

    private void tblItemLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemLinkupDtlMouseClicked
        // TODO add your handling code here:
        try
        {

            int col = tblPOSLinkupDtl.columnAtPoint(evt.getPoint());
            String name = tblPOSLinkupDtl.getColumnName(col);
            if (col == 3 || col == 4)
            {
                if (evt.getClickCount() == 2)
                {
                    funItemPOSLinkupRowClicked();
                }
            }

            if (col == 5 || col == 6)
            {
                if (evt.getClickCount() == 2)
                {
                    funPOSItemToExciseBrandLinkup();
                }
            }

            // funLoadItemLinkupTable1();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }//GEN-LAST:event_tblItemLinkupDtlMouseClicked

    private void tblPOSLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOSLinkupDtlMouseClicked
        // TODO add your handling code here:
        try
        {
            int col = tblPOSLinkupDtl.columnAtPoint(evt.getPoint());
            String name = tblPOSLinkupDtl.getColumnName(col);
            if (col == 2)
            {
                if (evt.getClickCount() == 2)
                {
                    funtblPOSLocationLinkupRowClicked();
                }

            }
            else if (col == 4)
            {
                if (evt.getClickCount() == 2)
                {
                    funAccountLinkupHelp(tblPOSLinkupDtl, col);
                }
            }
            else if (col == 6)
            {
                if (evt.getClickCount() == 2)
                {
                    funAccountLinkupHelp(tblPOSLinkupDtl, col);
                }
            }
            else if (col == 8)
            {
                if (evt.getClickCount() == 2)
                {
                    funAccountLinkupHelp(tblPOSLinkupDtl, col);
                }
            }
             else if (col == 10 || col==11)
            {
                if (evt.getClickCount() == 2)
                {
                    funPOSToExciseLicenseLinkup();
                }
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }//GEN-LAST:event_tblPOSLinkupDtlMouseClicked

    private void tblTaxLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTaxLinkupDtlMouseClicked
        // TODO add your handling code here:
        try
        {
            /*int col = tblTaxLinkupDtl.columnAtPoint(evt.getPoint()); 
             String name = tblTaxLinkupDtl.getColumnName(col);
             if(col==2)
             {
             if (evt.getClickCount() == 2)
             {
             funAccountLinkupHelp(tblTaxLinkupDtl,col); 
             }
             }
             */
            if (evt.getClickCount() == 2)
            {
                funAccountLinkupHelp(tblTaxLinkupDtl, 2);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblTaxLinkupDtlMouseClicked

    private void tblSubGroupLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubGroupLinkupDtlMouseClicked
        // TODO add your handling code here:
        try
        {
            /* int col = tblSubGroupLinkupDtl.columnAtPoint(evt.getPoint()); 
             String name = tblSubGroupLinkupDtl.getColumnName(col);
             if(col==2)
             {
             if (evt.getClickCount() == 2)
             {
             funAccountLinkupHelp(tblSubGroupLinkupDtl,col); 
             }
             }
             */
	    int col = tblSubGroupLinkupDtl.columnAtPoint(evt.getPoint());
            String name = tblSubGroupLinkupDtl.getColumnName(col);
            if (col == 4 || col == 5)
            {
                if (evt.getClickCount() == 2)
                {
                    funSubGroupPOSLinkupRowClicked();
                }
            }
            if (col == 2 || col == 3)
            {
            if (evt.getClickCount() == 2)
            {
                funAccountLinkupHelp(tblSubGroupLinkupDtl, 2);
            }
	    }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblSubGroupLinkupDtlMouseClicked

    private void tblSettlementLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSettlementLinkupDtlMouseClicked
        // TODO add your handling code here:
        try
        {
            /*int col = tblSettlementLinkupDtl.columnAtPoint(evt.getPoint()); 
             String name = tblSettlementLinkupDtl.getColumnName(col);
             if(col==2)
             {
             if (evt.getClickCount() == 2)
             {
             funAccountLinkupHelp(tblSettlementLinkupDtl,col); 
             }
             }
             */

            if (evt.getClickCount() == 2)
            {
                funAccountLinkupAllHelp(tblSettlementLinkupDtl, 3);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }//GEN-LAST:event_tblSettlementLinkupDtlMouseClicked

    private void tblCostCenterLinkupDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblCostCenterLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCostCenterLinkupDtlFocusGained

    private void tblCostCenterLinkupDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblCostCenterLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCostCenterLinkupDtlFocusLost

    private void tblCostCenterLinkupDtlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCostCenterLinkupDtlMouseClicked
        
         try
        {
            int col = tblCostCenterLinkupDtl.columnAtPoint(evt.getPoint());
            String name = tblCostCenterLinkupDtl.getColumnName(col);
            if (col == 2)
            {
                if (evt.getClickCount() == 2)
                {
                    funtblCostCenterLocationLinkupRowClicked();
                }

            }
           
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_tblCostCenterLinkupDtlMouseClicked

    private void tblCostCenterLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblCostCenterLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCostCenterLinkupDtlInputMethodTextChanged

    private void tblCostCenterLinkupDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCostCenterLinkupDtlKeyPressed
    }//GEN-LAST:event_tblCostCenterLinkupDtlKeyPressed

    private void tblCustomerLinkupDtlMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblCustomerLinkupDtlMouseClicked
    {//GEN-HEADEREND:event_tblCustomerLinkupDtlMouseClicked
	try
        {

            int col = tblCustomerLinkupDtl.columnAtPoint(evt.getPoint());
            String name = tblCustomerLinkupDtl.getColumnName(col);
            if(col == 2 || col == 3)
	    {
		funAccountLinkupHelp(tblCustomerLinkupDtl,2);
	    }
	    else if (col == 4 || col == 5)
            {
		funCustomerLinkupHelp();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblCustomerLinkupDtlMouseClicked

    private void tblCustomerLinkupDtlFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_tblCustomerLinkupDtlFocusGained
    {//GEN-HEADEREND:event_tblCustomerLinkupDtlFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCustomerLinkupDtlFocusGained

    private void tblCustomerLinkupDtlFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_tblCustomerLinkupDtlFocusLost
    {//GEN-HEADEREND:event_tblCustomerLinkupDtlFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCustomerLinkupDtlFocusLost

    private void tblCustomerLinkupDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_tblCustomerLinkupDtlInputMethodTextChanged
    {//GEN-HEADEREND:event_tblCustomerLinkupDtlInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblCustomerLinkupDtlInputMethodTextChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelCostCenter;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItem;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelPOS;
    private javax.swing.JPanel panelSettlement;
    private javax.swing.JPanel panelSubGroup;
    private javax.swing.JPanel panelTax;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private javax.swing.JSeparator separator3;
    private javax.swing.JSeparator separator4;
    private javax.swing.JSeparator separator5;
    private javax.swing.JSeparator separator6;
    private javax.swing.JSeparator separator7;
    private javax.swing.JScrollPane srollPane;
    private javax.swing.JScrollPane srollPane1;
    private javax.swing.JScrollPane srollPane2;
    private javax.swing.JScrollPane srollPane3;
    private javax.swing.JScrollPane srollPane4;
    private javax.swing.JScrollPane srollPane5;
    private javax.swing.JScrollPane srollPane6;
    private javax.swing.JTabbedPane tabLinkupMaster;
    private javax.swing.JTable tblCostCenterLinkupDtl;
    private javax.swing.JTable tblCustomerLinkupDtl;
    private javax.swing.JTable tblItemLinkupDtl;
    private javax.swing.JTable tblPOSLinkupDtl;
    private javax.swing.JTable tblSettlementLinkupDtl;
    private javax.swing.JTable tblSubGroupLinkupDtl;
    private javax.swing.JTable tblTaxLinkupDtl;
    // End of variables declaration//GEN-END:variables

    /**
     * This method is used to exit from form
     */
    private void funExitClick()
    {
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
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        dispose();
    }

    private void funResetFields()
    {
        DefaultTableModel dmItem = (DefaultTableModel) tblItemLinkupDtl.getModel();
        dmItem.setRowCount(0);
        DefaultTableModel dmPOS = (DefaultTableModel) tblPOSLinkupDtl.getModel();
        dmPOS.setRowCount(0);
        DefaultTableModel dmTax = (DefaultTableModel) tblTaxLinkupDtl.getModel();
        dmTax.setRowCount(0);
        DefaultTableModel dmSubGroup = (DefaultTableModel) tblSubGroupLinkupDtl.getModel();
        dmSubGroup.setRowCount(0);
        DefaultTableModel dmSettlement = (DefaultTableModel) tblSettlementLinkupDtl.getModel();
        dmSettlement.setRowCount(0);
	DefaultTableModel dmCustomerLinkup = (DefaultTableModel) tblCustomerLinkupDtl.getModel();
        dmSettlement.setRowCount(0);
	sb.setLength(0);
    }

    /**
     * This method is used to reset
     */
    private void funResetClick()
    {
        funResetFields();
    }

    private void funPOSItemToExciseBrandLinkup()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listProducts = objLinkSangERP.funGetBrandDtls(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Brand Code");
            listColumns.add("Brand Name");
            new frmSearchFormDialog(this, true, listProducts, "Excise Products", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblItemLinkupDtl.getSelectedRow();
                tblItemLinkupDtl.setValueAt(data[0], rowNo, 5);
                tblItemLinkupDtl.setValueAt(data[1], rowNo, 6);
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

    private void funPOSToExciseLicenseLinkup()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listExciseLicenseDtl = objLinkSangERP.funGetLicenceDtls(clsGlobalVarClass.gWSClientCode);
            new frmSearchFormDialog(this,listExciseLicenseDtl,true).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblPOSLinkupDtl.getSelectedRow();
                tblPOSLinkupDtl.setValueAt(data[0], rowNo, 10);
                tblPOSLinkupDtl.setValueAt(data[1], rowNo,11);
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
    
    private void funCustomerLinkupHelp()
    {
       clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsDebtorDtl> debtorInfo = objLinkSangERP.funGetDebtorDtl(clsGlobalVarClass.gClientCode);
            new frmSearchFormDialog(debtorInfo, true,this).setVisible(true);

            if(clsGlobalVarClass.gSearchItemClicked)  
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblCustomerLinkupDtl.getSelectedRow();
                tblCustomerLinkupDtl.setValueAt(data[0], rowNo, 4);
                tblCustomerLinkupDtl.setValueAt(data[1], rowNo, 5);
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
    
    private void funLoadCustomerLinkupTable()
    {
	
        try
        {

            String sql = " select a.strCustomerCode as Customer_Code,a.strCustomerName as Customer_Name,a.strAccountCode as Account_Code,"
		    + "a.strDebtorCode as Debtor_Code from tblcustomermaster a left outer join tblaccountmaster b on "
		    + "a.strAccountCode=b.strWBAccountCode";
	   
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmCustomerLinkup.setRowCount(0);
            while (rs.next())
            {
                final Object[] column = new Object[7];
                column[0] = rs.getString(1);
                column[1] = rs.getString(2);
		column[2] = rs.getString(3);
		column[4] = rs.getString(4);
		//String sqlDebt = "select strWBAccountCode , strWBAccountName from tblaccountmaster where strWBAccountCode='"+ rs.getString(4) +"' ";
                String sqlDebt="select a.strWBAccountName as Debtor_Name,b.strWBAccountName as Account_Name from ( select  a.strWBAccountCode,a.strWBAccountName from tblaccountmaster a where a.strWBAccountCode='"+ rs.getString(4) +"' ) a, "
			+ " (select  b.strWBAccountCode,b.strWBAccountName from tblaccountmaster b where b.strWBAccountCode='"+ rs.getString(3) +"' ) b";
		
		ResultSet rsDebt = clsGlobalVarClass.dbMysql.executeResultSet(sqlDebt);
		while (rsDebt.next())
		{
		    column[5] = rsDebt.getString(1);
		    column[3] = rsDebt.getString(2);
		}
		rsDebt.close();
		dmCustomerLinkup.addRow(column);
            }
            rs.close();
            tblCustomerLinkupDtl.setModel(dmCustomerLinkup);
	   
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblCustomerLinkupDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblCustomerLinkupDtl.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            
            tblCustomerLinkupDtl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCustomerLinkupDtl.getColumnModel().getColumn(1).setPreferredWidth(280);
            
            tblCustomerLinkupDtl.setSize(700, 850);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
     private void funSubGroupPOSLinkupRowClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listProducts = objLinkSangERP.funGetSubGroupDtl(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("SubGroup Code");
            listColumns.add("SubGroup Name");
            new frmSearchFormDialog(this, true, listProducts, "MMS SubGroup", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                final int rowNo = tblSubGroupLinkupDtl.getSelectedRow();
                tblSubGroupLinkupDtl.setValueAt(data[0], rowNo, 4);
                tblSubGroupLinkupDtl.setValueAt(data[1], rowNo, 5);
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
     
      //Save linking WS product Code with itemmaster in tblitemorderingdtl table
    private void funSaveDataToSubGroupLinkupTable() throws Exception
    {
        int cnt = 0;

        String insertQuery = "";
        if (tblSubGroupLinkupDtl.getRowCount() > 0)
        {
            insertQuery = "insert into tblsubgroupmasterlinkupdtl (strSubGrooupCode,strWSSubGroupCode,strWSSubGroupName,strClientCode,strDataPostFlag) values ";
            for (int row = 0; row < tblSubGroupLinkupDtl.getRowCount(); row++)
            {
                    
                        if (cnt == 0)
                        {
                            insertQuery += "('" + tblSubGroupLinkupDtl.getValueAt(row, 0) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 4) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 5) + "', '" + clsGlobalVarClass.gClientCode + "', 'N') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tblSubGroupLinkupDtl.getValueAt(row, 0) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 4) + "','" + tblSubGroupLinkupDtl.getValueAt(row, 5) + "', '" + clsGlobalVarClass.gClientCode + "', 'N')";
                        }
                        cnt++;

            }

        }
        if (cnt > 0)
        {
            System.out.println("cnt=" + cnt);
            System.out.println("insertQuery=" + insertQuery);
            String deleteQuery = " truncate table tblsubgroupmasterlinkupdtl  ";
            clsGlobalVarClass.dbMysql.execute(deleteQuery);
            clsGlobalVarClass.dbMysql.execute(insertQuery);
        }
    }
    
}
