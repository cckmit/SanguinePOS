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
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmMoveKOTItemToTable extends javax.swing.JFrame
{

    public Vector vOpenKOTNo, vOpenTableNo;
    public int cntNavigate;
    private JButton[] btnTableArray;
    private clsUtility objUtility;
    private HashMap<String, String> mapBusyTableNo, mapTableNo, mapBusyTableName, mapTableName;
    private Map<String, Map<String, List<String>>> hmSelectedItemList;
    private Map<String, Map<String, List<String>>> hmExistingKOTItemList;
    private int kotItemSequenceNO = 0;
    private String strSerialNo;

    public frmMoveKOTItemToTable()
    {
        initComponents();
        try
        {
            hmSelectedItemList = new HashMap<String, Map<String, List<String>>>();

            objUtility = new clsUtility();
            vOpenTableNo = new Vector();
            vOpenKOTNo = new Vector();
            cntNavigate = 0;
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            Date date1 = new Date();
            String new_str = String.format("%tr", date1);
            String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
            lblDate.setText(dateAndTime);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funFillTableCombo();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillTableCombo() throws Exception
    {
        cmbBusyTable.removeAllItems();
        //For Busy Table
        ResultSet rsBusyTable = clsGlobalVarClass.dbMysql.executeResultSet("select strTableNo,strTableName from tbltablemaster "
                + " where strStatus='Occupied' "
                + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                + " order by intSequence ");
        mapBusyTableNo = new HashMap<String, String>();
        mapBusyTableName = new HashMap<String, String>();
        cmbBusyTable.addItem("Select Table");

        while (rsBusyTable.next())
        {
            cmbBusyTable.addItem(rsBusyTable.getString(2));
            mapBusyTableNo.put(rsBusyTable.getString(1), rsBusyTable.getString(2));
            mapBusyTableName.put(rsBusyTable.getString(2), rsBusyTable.getString(1));
        }
        rsBusyTable.close();

        cmbTable.removeAllItems();
        //For All table list

        ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet("select strTableNo,strTableName from tbltablemaster "
                + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                + " and strOperational='Y' "
                + " order by intSequence ");
        mapTableNo = new HashMap<String, String>();
        mapTableName = new HashMap<String, String>();
        cmbTable.addItem("Select Table");
        while (rsTable.next())
        {
            cmbTable.addItem(rsTable.getString(2));
            mapTableNo.put(rsTable.getString(1), rsTable.getString(2));
            mapTableName.put(rsTable.getString(2), rsTable.getString(1));
        }
        rsTable.close();
    }

    private void funFillOpenKOTVector(String tableNo)
    {
        try
        {
            vOpenKOTNo.removeAllElements();
            vOpenTableNo.removeAllElements();

            String sql = "select distinct(strKOTNo),strTableNo from tblitemrtemp "
                    + " where strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";

            System.out.println(sql);
            ResultSet rsKOTNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsKOTNo.next())
            {
                vOpenKOTNo.add(rsKOTNo.getString(1));
                vOpenTableNo.add(rsKOTNo.getString(2));
            }
            funLoadOpenKOTs(0, vOpenKOTNo.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadOpenKOTs(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;
            JButton[] btnTableArray =
            {
                btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
            };
            for (int k = 0; k < btnTableArray.length; k++)
            {
                btnTableArray[k].setForeground(Color.black);
                btnTableArray[k].setBackground(Color.LIGHT_GRAY);
                btnTableArray[k].setText("");
            }
            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == vOpenKOTNo.size())
                {
                    break;
                }
                String kotNo = vOpenKOTNo.elementAt(i).toString();

                if (cntIndex < 16)
                {
                    btnTableArray[cntIndex].setText(kotNo);
                    btnTableArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            for (int j = cntIndex; j < 16; j++)
            {
                btnTableArray[j].setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean funValidateTables()
    {
        boolean flgValidate = true;
        try
        {
            if (tableNewKOTList.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please add items to save new KOT ");
                flgValidate = false;
            }
            if (cmbTable.getSelectedItem().equals("Select Table"))
            {
                JOptionPane.showMessageDialog(this, "Please select table to save new KOT");
                flgValidate = false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return flgValidate;
    }

    private void funSetDefaultColorOpen(int btnIndex)
    {
        try
        {
            JButton[] btnTableArray =
            {
                btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
            };
            Color btnColor = btnTableArray[btnIndex].getBackground();
            if (btnColor != Color.black)
            {
                btnTableArray[btnIndex].setBackground(Color.black);
                for (int cnt = 0; cnt < btnTableArray.length; cnt++)
                {
                    if (cnt != btnIndex)
                    {
                        btnTableArray[cnt].setBackground(btnColor);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSelectOpenTable(String kotNo, int index)
    {
        try
        {
            if (kotNo.trim().length() > 0)
            {
                funFillItemGrid(kotNo);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillItemGrid(String KotNo)
    {
        try
        {
            Map<String, List<String>> mapSelectedItemList = new HashMap<String, List<String>>();
            double subTotalAmt = 0.00;
            DefaultTableModel dm = (DefaultTableModel) tableKOTList.getModel();
            dm.setRowCount(0);
            String tableNo = mapBusyTableName.get(cmbBusyTable.getSelectedItem().toString());
            String sql = "select count(*) from tblitemrtemp "
                    + "where strKOTNo='" + KotNo + "' ";
            if (!tableNo.equals("All"))
            {
                sql += " and strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
            }
            else
            {
                sql += " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";

            }
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cnt = rs.getInt(1);
            if (cnt > 0)
            {
                sql = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated,strItemCode"
                        + " ,strPOSCode,strTableNo,strWaiterNo,strSerialNo,dblRedeemAmt,strCustomerCode "
                        + " from tblitemrtemp "
                        + " where strKOTNo='" + KotNo + "' ";
                if (!tableNo.equals("All"))
                {
                    sql += " and strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
                }
                else
                {
                    sql += " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";

                }
                sql += " order by strSerialNo";

                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rs.next())
                {
                    List<String> arrListOfItem = new ArrayList<String>();
                    boolean flgSelect = false;
                    String itemName = rs.getString("strItemName");
                    String dblQuantity = rs.getString("dblItemQuantity");
                    String dblAmount = rs.getString("dblAmount");
                    String itemCode = rs.getString("strItemCode");
                    String waiterNo = rs.getString("strWaiterNo");
                    String createdDate = rs.getString("dteDateCreated");
                    String serialNo = rs.getString("strSerialNo");
                    String dblRedeemAmt = rs.getString("dblRedeemAmt");
                    String customerCode = rs.getString("strCustomerCode");

                    if (hmSelectedItemList.containsKey(KotNo))
                    {
                        mapSelectedItemList = hmSelectedItemList.get(KotNo);
                        if (mapSelectedItemList.containsKey(itemCode))
                        {
                            arrListOfItem = mapSelectedItemList.get(itemCode);

                        }
                    }
                    if (arrListOfItem.size() > 0)
                    {
                        if (arrListOfItem.contains(itemName + "#" + dblQuantity + "#" + dblAmount + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + serialNo + "#" + dblRedeemAmt + "#" + customerCode));
                        {
                            flgSelect = true;
                        }
                    }
                    else
                    {
                        flgSelect = false;
                    }

                    Object[] fillrow =
                    {
                        itemName, dblQuantity, dblAmount, flgSelect, itemCode, waiterNo, KotNo, createdDate, serialNo, dblRedeemAmt, customerCode
                    };
                    dm.addRow(fillrow);
                }

                tableKOTList.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tableKOTList.setShowHorizontalLines(true);
                tableKOTList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tableKOTList.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
                tableKOTList.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
                tableKOTList.getColumnModel().getColumn(0).setPreferredWidth(260);
                tableKOTList.getColumnModel().getColumn(1).setPreferredWidth(50);
                tableKOTList.getColumnModel().getColumn(2).setPreferredWidth(83);

            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*  
     private void funAddSelectedItemToNewKOTList()throws Exception
     {
     List<List<String>> arrMainListToFillGrid=null;
     Map<String, List<List<String>>> mapMainItemList=new HashMap<String, List<List<String>>>();
       
     if(hmExistingKOTItemList.size()>0)
     {
     for (Map.Entry<String, Map<String, List<String>>> entry : hmExistingKOTItemList.entrySet())
     {
     String itemCode="",itemName="",itemQty="",itemAmt="",waiterNo="";
     Map<String, List<String>> mapSelectedItem=entry.getValue();
     for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
     {
     List<String> listOfParam = entry1.getValue();
     for(int i=0;i<listOfParam.size();i++)
     {
     String[] param=listOfParam.get(i).split("#");
     itemCode=param[3];
     itemName=param[0];
     itemQty=param[1];
     itemAmt=param[2];
     waiterNo=param[4];
     }
     if(mapMainItemList.containsKey(entry1.getKey()))
     {
     arrMainListToFillGrid=mapMainItemList.get(entry1.getKey());
     for(int c=0;c<arrMainListToFillGrid.size();c++)
     {
     listOfParam=arrMainListToFillGrid.get(c);
     for(int count=0;count<listOfParam.size();count++)
     {
     String[] param=listOfParam.get(count).split("#");
     if(itemCode.equals(param[3]))
     {
     double qty=Double.valueOf(param[1]);
     double amt=Double.valueOf(param[2]);
     arrMainListToFillGrid.remove(c);
     listOfParam=new ArrayList<String>();
     listOfParam.add(itemName+"#"+String.valueOf(qty+Double.valueOf(itemQty))+"#"+String.valueOf(amt+Double.valueOf(itemAmt))+"#"+itemCode+"#"+waiterNo); 
     break;
     }
                               
     }
     }
                        
     }
     else
     {
     arrMainListToFillGrid=new ArrayList<List<String>>();
     }
     arrMainListToFillGrid.add(listOfParam);
     mapMainItemList.put(entry1.getKey(), arrMainListToFillGrid);
     }
                 
     }
     }
       
          
     for (Map.Entry<String, Map<String, List<String>>> entry : mapSelectedItemList1.entrySet())
     {
     String itemCode="",itemName="",itemQty="",itemAmt="",waiterNo="";
     Map<String, List<String>> mapSelectedItem=entry.getValue();
     for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
     {
     List<String> listOfParam = entry1.getValue();
     for(int i=0;i<listOfParam.size();i++)
     {
     String[] param=listOfParam.get(i).split("#");
     itemCode=param[3];
     itemName=param[0];
     itemQty=param[1];
     itemAmt=param[2];
     waiterNo=param[4];
     }
     if(mapMainItemList.containsKey(entry1.getKey()))
     {
     arrMainListToFillGrid=mapMainItemList.get(entry1.getKey());
     for(int c=0;c<arrMainListToFillGrid.size();c++)
     {
     listOfParam=arrMainListToFillGrid.get(c);
     for(int count=0;count<listOfParam.size();count++)
     {
     String[] param=listOfParam.get(count).split("#");
     if(itemCode.equals(param[3]))
     {
     double qty=Double.valueOf(param[1]);
     double amt=Double.valueOf(param[2]);
     arrMainListToFillGrid.remove(c);
     listOfParam=new ArrayList<String>();
     listOfParam.add(itemName+"#"+String.valueOf(qty+Double.valueOf(itemQty))+"#"+String.valueOf(amt+Double.valueOf(itemAmt))+"#"+itemCode+"#"+waiterNo); 
     break;
     }
                               
     }
     }
                        
     }
     else
     {
     arrMainListToFillGrid=new ArrayList<List<String>>();
     }
     arrMainListToFillGrid.add(listOfParam);
     mapMainItemList.put(entry1.getKey(), arrMainListToFillGrid);
     }
     }
        
        
        
     if(mapMainItemList.size()>0)
     {
     funFillGrid(mapMainItemList);
     System.out.println(mapMainItemList);
     }
     }
     */
    private void funFillGrid() throws Exception
    // private void funFillGrid(Map<String, List<List<String>>> mapMainItemList)throws Exception
    {
        DefaultTableModel dmNewKotDtl = (DefaultTableModel) tableNewKOTList.getModel();
        dmNewKotDtl.setRowCount(0);

        if (hmExistingKOTItemList!=null && hmExistingKOTItemList.size() > 0)
        {
            for (Map.Entry<String, Map<String, List<String>>> entry : hmExistingKOTItemList.entrySet())
            {
                String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "";
                Map<String, List<String>> mapSelectedItem = entry.getValue();
                for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
                {
                    List<String> listOfParam = entry1.getValue();
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");
                        itemCode = param[3];
                        itemName = param[0];
                        itemQty = param[1];
                        itemAmt = param[2];
                        waiterNo = param[4];
                        createdDate = param[5];
                        Object[] row =
                        {
                            itemName, itemQty, itemAmt, itemCode, waiterNo, createdDate
                        };
                        dmNewKotDtl.addRow(row);
                    }
                }
            }
        }

        if (hmSelectedItemList.size() > 0)
        {
            for (Map.Entry<String, Map<String, List<String>>> entry : hmSelectedItemList.entrySet())
            {
                String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "";
                Map<String, List<String>> mapSelectedItem = entry.getValue();
                for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
                {
                    List<String> listOfParam = entry1.getValue();
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");
                        itemCode = "<html><font color=blue>" + param[3] + "</font></html>";
                        itemName = "<html><font color=blue>" + param[0] + "</font></html>";
                        itemQty = "<html><font color=blue>" + param[1] + "</font></html>";
                        itemAmt = "<html><font color=blue>" + param[2] + "</font></html>";
                        waiterNo = "<html><font color=blue>" + param[4] + "</font></html>";
                        createdDate = "<html><font color=blue>" + param[5] + "</font></html>";

                        Object[] row =
                        {
                            itemName, itemQty, itemAmt, itemCode, waiterNo, createdDate
                        };
                        dmNewKotDtl.addRow(row);
                    }
                }
            }
        }

        tableNewKOTList.setModel(dmNewKotDtl);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableNewKOTList.setShowHorizontalLines(true);
        tableNewKOTList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableNewKOTList.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tableNewKOTList.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tableNewKOTList.getColumnModel().getColumn(0).setPreferredWidth(230);
        tableNewKOTList.getColumnModel().getColumn(1).setPreferredWidth(40);
        tableNewKOTList.getColumnModel().getColumn(2).setPreferredWidth(60);

    }

    private String funGenerateKOTNo()
    {
        String kotNo = "";
        try
        {
            long code = 0;
            String sql = "select dblLastNo from tblinternal where strTransactionType='KOTNo'";
            ResultSet rsKOT = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsKOT.next())
            {
                code = rsKOT.getLong(1);
                code = code + 1;
                kotNo = "KT" + String.format("%07d", code);
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
            }
            else
            {
                kotNo = "KT0000001";
                clsGlobalVarClass.gUpdatekot = false;
            }
            rsKOT.close();
            sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='KOTNo'";
            clsGlobalVarClass.dbMysql.execute(sql);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return kotNo;
    }

    private void funSaveKOT(double taxAmt)
    {
        try
        {
            String kotNo = funGenerateKOTNo();
            String tableNo = mapTableName.get(cmbTable.getSelectedItem().toString());
            String KOTNo = "";
            double KOTAmt = 0;
            int cnt = 0;

            List<clsMakeKotItemDtl> listOfItemsForFromTable = new ArrayList<clsMakeKotItemDtl>();

            String insertQuery = "insert into tblitemrtemp(strSerialNo,strTableNo,strCardNo,dblRedeemAmt,strPosCode,strItemCode"
                    + ",strHomeDelivery,strCustomerCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo"
                    + ",strKOTNo,intPaxNo,strPrintYN,strUserCreated,strUserEdited,dteDateCreated"
                    + ",dteDateEdited,strTakeAwayYesNo,strNCKotYN,strCustomerName,strCounterCode"
                    + ",dblRate,dblTaxAmt) values ";

            for (Map.Entry<String, Map<String, List<String>>> entryKOTMap : hmSelectedItemList.entrySet())
            {
                String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "", serialNo = "", redeemAmt = "", customerCode = "";
                double rate = 0;
                Map<String, List<String>> mapSelectedItem = entryKOTMap.getValue();
                for (Map.Entry<String, List<String>> entryItemMap : mapSelectedItem.entrySet())
                {
                    List<String> listOfParam = entryItemMap.getValue();
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");

                        itemName = param[0];
                        itemQty = param[1];
                        itemAmt = param[2];
                        itemCode = param[3];
                        waiterNo = param[4];
                        createdDate = param[5];
                        rate = (Double.parseDouble(itemAmt) / Double.parseDouble(itemQty));

                        clsMakeKotItemDtl objKotItemDtl = new clsMakeKotItemDtl();
                        objKotItemDtl.setItemCode(itemCode);
                        objKotItemDtl.setItemName(itemName);
                        objKotItemDtl.setQty(Double.parseDouble(itemQty));
                        objKotItemDtl.setAmt(Double.parseDouble(itemAmt));

                        listOfItemsForFromTable.add(objKotItemDtl);

                        KOTAmt += Double.valueOf(param[2]);

                        if (cnt == 0)
                        {
                            insertQuery += "('" + param[6] + "','" + tableNo + "'"
                                    + ",'','0','" + clsGlobalVarClass.gPOSCode + "','" + itemCode + "',"
                                    + "'','','" + param[0] + "'"
                                    + ",'" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "'"
                                    + ",'" + param[4] + "','" + kotNo + "'"
                                    + ",'1','Y'"
                                    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                                    + ",'" + createdDate + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
                                    + ",'','N','',''," + rate + "," + taxAmt + ")";

                        }
                        else
                        {
                            insertQuery += ",('" + param[6] + "','" + tableNo + "'"
                                    + ",'','0','" + clsGlobalVarClass.gPOSCode + "','" + itemCode + "',"
                                    + "'','','" + param[0] + "'"
                                    + ",'" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "'"
                                    + ",'" + param[4] + "','" + kotNo + "'"
                                    + ",'1','Y'"
                                    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                                    + ",'" + createdDate + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
                                    + ",'','N','',''," + rate + "," + taxAmt + ")";
                        }
                        cnt++;
                    }
                }
            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }

            /* StringBuilder sb = new StringBuilder(insertQuery);
             int index = sb.lastIndexOf(",");
             insertQuery = sb.delete(index, sb.length()).toString();
             System.out.println(insertQuery);
             clsGlobalVarClass.dbMysql.execute(insertQuery);
             */
            if (taxAmt > 0)
            {
                String sql = "insert into tblkottaxdtl "
                        + "values ('" + tableNo + "','" + KOTNo + "'," + KOTAmt + "," + taxAmt + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            funUpdateKOT(tableNo, KOTNo);
            //update previous kot details
            funUpdatePreviousKOTDetails();

            String busyTableNo = mapBusyTableName.get(cmbBusyTable.getSelectedItem().toString());
            String shiftedTableNo = mapTableName.get(cmbTable.getSelectedItem().toString());
            //insert into itemrtempbck table
            objUtility.funInsertIntoTblItemRTempBck(busyTableNo);
            objUtility.funInsertIntoTblItemRTempBck(shiftedTableNo);

            JOptionPane.showMessageDialog(this, "KOT Generated on Table " + cmbTable.getSelectedItem().toString());
            funPrintKOT(KOTNo, tableNo);
            if (listOfItemsForFromTable.size() > 0)
            {
                //send message to all cost centers 
                funSendMessageToCostCenters(listOfItemsForFromTable);
            }
            funResetFields();
            btnSave.setEnabled(true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private String getStrSerialNo()
    {
        strSerialNo = String.valueOf(kotItemSequenceNO);
        return strSerialNo;
    }

    private void funUpdateKOT(String tempTableNO, String KOTNo)
    {
        try
        {
            String sql = "update tbltablemaster set strStatus='Occupied' where strTableNo='" + tempTableNO + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        catch (Exception e)
        {
            new clsUtility().funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            System.gc();
        }
    }

    private void funResetFields() throws Exception
    {
        DefaultTableModel dm = (DefaultTableModel) tableKOTList.getModel();
        dm.setRowCount(0);
        DefaultTableModel dm1 = (DefaultTableModel) tableNewKOTList.getModel();
        dm1.setRowCount(0);
        tableKOTList.setModel(dm);
        tableNewKOTList.setModel(dm1);
        hmSelectedItemList.clear();
        hmExistingKOTItemList.clear();
        funFillOpenKOTVector("All");
        funFillTableCombo();
        cmbBusyTable.setSelectedItem("Select Table");
        cmbTable.setSelectedItem("Select Table");
    }

    private void funTableKOTListClicked() throws Exception
    {
        List<String> arrListOfItem = null;
        Map<String, List<String>> mapSelectedItemList = null;
        for (int i = 0; i < tableKOTList.getRowCount(); i++)
        {
            String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", kotNo = "", createdDate = "", isModifier = "N", modItemCode = "", serialNo = "", redeemAmt = "", customerCode = "";
            itemName = tableKOTList.getValueAt(i, 0).toString();
            itemQty = tableKOTList.getValueAt(i, 1).toString();
            itemAmt = tableKOTList.getValueAt(i, 2).toString();
            itemCode = tableKOTList.getValueAt(i, 4).toString();
            waiterNo = tableKOTList.getValueAt(i, 5).toString();
            kotNo = tableKOTList.getValueAt(i, 6).toString();
            createdDate = tableKOTList.getValueAt(i, 7).toString();
            serialNo = tableKOTList.getValueAt(i, 8).toString();
            redeemAmt = tableKOTList.getValueAt(i, 9).toString();
            customerCode = tableKOTList.getValueAt(i, 10).toString();

            if (itemCode.contains("M"))
            {
                isModifier = "Y";
                modItemCode = itemCode.substring(0, 7);
                System.out.println("modItemCode:-" + modItemCode);
            }
            if (isModifier.equals("N"))
            {
                Boolean flgSelect = Boolean.parseBoolean(tableKOTList.getValueAt(i, 3).toString());
                if (flgSelect)
                {
                    if (hmSelectedItemList.containsKey(kotNo))
                    {
                        mapSelectedItemList = hmSelectedItemList.get(kotNo);
                        if (mapSelectedItemList.containsKey(itemCode))
                        {
                            arrListOfItem = mapSelectedItemList.get(itemCode);
                        }
                        else
                        {
                            arrListOfItem = new ArrayList<String>();
                            if (Double.parseDouble(itemQty) > 1)
                            {
                                new frmNumericKeyboard(this, true, "", "Double", "Enter quantity to move").setVisible(true);
                                if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > Double.parseDouble(itemQty))
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
                                    String selectedVoidQty = clsGlobalVarClass.gNumerickeyboardValue;
                                    double temSingleItemAmt = Double.parseDouble(itemAmt) / Double.parseDouble(itemQty);
                                    double selectedItemAmt = Double.parseDouble(selectedVoidQty) * temSingleItemAmt;
                                    arrListOfItem.add(itemName + "#" + selectedVoidQty + "#" + String.valueOf(selectedItemAmt) + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + getStrSerialNo() + "#" + redeemAmt + "#" + customerCode);
                                    kotItemSequenceNO++;
                                }
                            }
                            else
                            {
                                arrListOfItem.add(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + getStrSerialNo() + "#" + redeemAmt + "#" + customerCode);
                                kotItemSequenceNO++;
                            }
                        }
                    }
                    else
                    {
                        mapSelectedItemList = new HashMap<String, List<String>>();
                        arrListOfItem = new ArrayList<String>();
                        if (Double.parseDouble(itemQty) > 1)
                        {
                            new frmNumericKeyboard(this, true, "", "Double", "Enter quantity to move").setVisible(true);
                            if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > Double.parseDouble(itemQty))
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
                                String selectedVoidQty = clsGlobalVarClass.gNumerickeyboardValue;
                                double temSingleItemAmt = Double.parseDouble(itemAmt) / Double.parseDouble(itemQty);
                                double selectedItemAmt = Double.parseDouble(selectedVoidQty) * temSingleItemAmt;
                                arrListOfItem.add(itemName + "#" + selectedVoidQty + "#" + String.valueOf(selectedItemAmt) + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + getStrSerialNo() + "#" + redeemAmt + "#" + customerCode);
                                kotItemSequenceNO++;
                            }
                        }
                        else
                        {
                            arrListOfItem.add(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + getStrSerialNo() + "#" + redeemAmt + "#" + customerCode);
                            kotItemSequenceNO++;
                        }
                        // arrListOfItem.add(itemName+"#"+itemQty+"#"+itemAmt+"#"+itemCode+"#"+waiterNo+"#"+createdDate+"#"+getStrSerialNo()+"#"+redeemAmt+"#"+customerCode); 
                        // kotItemSequenceNO++;
                    }
                    mapSelectedItemList.put(itemCode, arrListOfItem);
                    hmSelectedItemList.put(kotNo, mapSelectedItemList);
                }
                else
                {
                    if (hmSelectedItemList.size() > 0)
                    {
                        mapSelectedItemList = new HashMap<String, List<String>>();
                        arrListOfItem = new ArrayList<String>();
                        if (hmSelectedItemList.containsKey(kotNo))
                        {
                            mapSelectedItemList = hmSelectedItemList.get(kotNo);
                            if (mapSelectedItemList.containsKey(itemCode))
                            {
                                arrListOfItem = mapSelectedItemList.get(itemCode);
                            }
                        }
                        if (arrListOfItem.size() > 0)
                        {
                            arrListOfItem.remove(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + serialNo + "#" + redeemAmt + "#" + customerCode);
                            mapSelectedItemList.remove(itemCode, arrListOfItem);
                            hmSelectedItemList.remove(kotNo, mapSelectedItemList);
                        }
                    }
                }
            }
            else
            {
                if (hmSelectedItemList.containsKey(kotNo))
                {
                    boolean flgFound = false;
                    boolean flgMove = false;
                    mapSelectedItemList = hmSelectedItemList.get(kotNo);
                    if (mapSelectedItemList.containsKey(modItemCode))
                    {
                        kotItemSequenceNO = kotItemSequenceNO - 1;
                        serialNo = getStrSerialNo() + ".00";
                        arrListOfItem = mapSelectedItemList.get(modItemCode);
                        for (int cnt = 0; cnt < arrListOfItem.size(); cnt++)
                        {
                            String[] param = arrListOfItem.get(cnt).split("#");
                            if (param[3].equals(modItemCode))
                            {
                                String selectQuery = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated,strItemCode "
                                        + " ,strPOSCode,strTableNo,strWaiterNo,strSerialNo,dblRedeemAmt,strCustomerCode "
                                        + " from tblitemrtemp "
                                        + " where strKOTNo='" + kotNo + "' and strItemCode='" + modItemCode + "' ";
                                ResultSet rsQuery = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                                while (rsQuery.next())
                                {
                                    double qty = rsQuery.getDouble(2) - Double.valueOf(param[1]);
                                    if (Double.valueOf(param[1]) < rsQuery.getDouble(2) && qty != '1')
                                    {
                                        flgMove = false;
                                    }
                                    else
                                    {
                                        flgMove = true;
                                    }
                                }
                                rsQuery.close();
                            }
                        }
                        if (arrListOfItem.size() > 1)
                        {
                            for (int cnt = 0; cnt < arrListOfItem.size(); cnt++)
                            {
                                String[] param = arrListOfItem.get(cnt).split("#");
                                if (param[0].equals(itemName) && param[3].equals(itemCode))
                                {
                                    flgFound = true;
                                }
                                if (flgFound)
                                {
                                    arrListOfItem.remove(cnt);
                                }
                            }
                        }

                        if (flgMove)
                        {
                            arrListOfItem.add(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate + "#" + serialNo + "#" + redeemAmt + "#" + customerCode);
                            tableKOTList.setValueAt(Boolean.TRUE, i, 3);
                            kotItemSequenceNO++;
                            mapSelectedItemList.put(modItemCode, arrListOfItem);
                        }

                    }
                    hmSelectedItemList.put(kotNo, mapSelectedItemList);
                }
            }

        }
    }

    private void funUpdatePreviousKOTDetails() throws Exception
    {
        int cnt = 0;
        String strType = "MVKot", voidedDate = funGetVodidedDate();;
        String tableNo = mapBusyTableName.get(cmbBusyTable.getSelectedItem().toString());
        if (hmSelectedItemList.size() > 0)
        {
            String insertQuery = "insert into tblvoidkot(strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity, "
                    + " dblAmount,strWaiterNo,strKOTNo,intPaxNo,strType,strReasonCode, "
                    + " strUserCreated,dteDateCreated,dteVoidedDate,strClientCode,strRemark ) "
                    + " values ";
            for (Map.Entry<String, Map<String, List<String>>> entryKOTMap : hmSelectedItemList.entrySet())
            {
                String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "";
                Map<String, List<String>> mapSelectedItem = entryKOTMap.getValue();
                for (Map.Entry<String, List<String>> entryItemMap : mapSelectedItem.entrySet())
                {
                    List<String> listOfParam = entryItemMap.getValue();
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");
                        itemCode = param[3];
                        createdDate = param[5];

                        String selectQuery = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated,strItemCode "
                                + " ,strPOSCode,strTableNo,strWaiterNo,strSerialNo,dblRedeemAmt,strCustomerCode "
                                + " from tblitemrtemp "
                                + " where strKOTNo='" + entryKOTMap.getKey() + "' and strItemCode='" + entryItemMap.getKey() + "' ";
                        ResultSet rsQuery = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                        while (rsQuery.next())
                        {
                            if (Double.valueOf(param[1]) < rsQuery.getDouble(2))
                            {
                                double qty = rsQuery.getDouble(2) - Double.valueOf(param[1]);
                                double rate = rsQuery.getDouble(3) / rsQuery.getDouble(2);
                                double amt = qty * rate;
                                String updateQuery = "update tblitemrtemp set dblItemQuantity='" + qty + "' , dblAmount='" + amt + "' where strKOTNo='" + entryKOTMap.getKey() + "' and strItemCode='" + entryItemMap.getKey() + "' ";
                                clsGlobalVarClass.dbMysql.execute(updateQuery);
                            }
                            else
                            {
                                String deleteQuery = " delete from tblitemrtemp "
                                        + " where strKOTNo='" + entryKOTMap.getKey() + "' and left(strItemCode,7)='" + entryItemMap.getKey() + "' ";
                                clsGlobalVarClass.dbMysql.execute(deleteQuery);
                            }

                        }
                        rsQuery.close();

                        if (cnt == 0)
                        {
                            insertQuery += " ('" + tableNo + "','" + clsGlobalVarClass.gPOSCode + "','" + param[3] + "',"
                                    + "'" + param[0] + "','" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "',"
                                    + "'" + param[4] + "','" + entryKOTMap.getKey() + "','0','" + strType + "','R02',"
                                    + "'" + clsGlobalVarClass.gUserCode + "','" + createdDate + "'," + "'" + voidedDate + "'"
                                    + ",'" + clsGlobalVarClass.gClientCode + "','moved kot') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tableNo + "','" + clsGlobalVarClass.gPOSCode + "','" + param[3] + "',"
                                    + "'" + param[0] + "','" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "',"
                                    + "'" + param[4] + "','" + entryKOTMap.getKey() + "','0','" + strType + "','R02',"
                                    + "'" + clsGlobalVarClass.gUserCode + "','" + createdDate + "'," + "'" + voidedDate + "'"
                                    + ",'" + clsGlobalVarClass.gClientCode + "','moved kot') ";
                        }
                        cnt++;
                    }
                }
            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }
        }

        String sql = " select count(strTableNo) from tblitemrtemp "
                + " where strTableNo='" + tableNo + "' "
                + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
        System.out.println(sql);
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        int count = 0;
        if (rs.next())
        {
            count = rs.getInt(1);
        }
        rs.close();
        if (count == 0)
        {
            sql = "update tbltablemaster set strStatus='Normal' where strTableNo='" + tableNo + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
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

    private void funPrintKOT(String KOTNo, String tableNo) throws Exception
    {
        clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
        objKOTGeneration.funKOTGeneration(tableNo, KOTNo, "", "", "Dine", "Y");

//        if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
//        {
//            clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//            PreparedStatement pst = null;
//            String areaCodeForAll = "";
//            String sql_AreaCode = "select strAreaCode from tblareamaster where strAreaName='All';";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_AreaCode);
//            ResultSet rsAreaCode = pst.executeQuery();
//            if (rsAreaCode.next())
//            {
//                areaCodeForAll = rsAreaCode.getString(1);
//            }
//            rsAreaCode.close();
//            String sql = "select a.strItemName,a.strNCKotYN,d.strCostCenterCode,d.strPrimaryPrinterPort,d.strSecondaryPrinterPort,d.strCostCenterName "
//                    + " from tblitemrtemp a left outer join tblmenuitempricingdtl c on a.strItemCode = c.strItemCode "
//                    + " left outer join tblprintersetup d on c.strCostCenterCode=d.strCostCenterCode "
//                    + " where a.strKOTNo=? and a.strTableNo=? and (c.strPosCode=? or c.strPosCode='All') "
//                    + " and (c.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo=? ) "
//                    + " OR c.strAreaCode =?) "
//                    + " group by d.strCostCenterCode";
//            //System.out.println(sql);
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql);
//            pst.setString(1, KOTNo);
//            pst.setString(2, tableNo);
//            pst.setString(3, clsGlobalVarClass.gPOSCode);
//            pst.setString(4, tableNo);
//            pst.setString(5, areaCodeForAll);
//            ResultSet rsPrint = pst.executeQuery();
//            while (rsPrint.next())
//            {
//                ob.funGenerateTextFileForMoveKOTItemsToTable(tableNo, rsPrint.getString(3), areaCodeForAll, KOTNo, "", rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), "", rsPrint.getString(2), cmbBusyTable.getSelectedItem().toString());
//
//            }
//            rsPrint.close();
//            pst.close();
//        }
    }

    private void funFillExistingKOTDetails() throws Exception
    {
        List<String> arrListOfItem = null;
        Map<String, List<String>> mapSelectedItemList = null;
        String tableNo = mapTableName.get(cmbTable.getSelectedItem().toString());

        String sql = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated, "
                + " strItemCode ,strPOSCode,strTableNo,strWaiterNo ,strKOTNo  "
                + " from tblitemrtemp  ";
        if (!tableNo.equals("All"))
        {
            sql += " where strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
        }
        else
        {
            sql += " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
        }
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rs.next())
        {
            String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", kotNo = "", createdDate = "";
            itemName = rs.getString(1);
            itemQty = rs.getString(2);
            itemAmt = rs.getString(3);
            itemCode = rs.getString(6);
            waiterNo = rs.getString(9);
            kotNo = rs.getString(10);
            createdDate = rs.getString(5);

            if (hmExistingKOTItemList.containsKey(kotNo))
            {
                mapSelectedItemList = hmExistingKOTItemList.get(kotNo);
                if (mapSelectedItemList.containsKey(itemCode))
                {
                    arrListOfItem = mapSelectedItemList.get(itemCode);
                }
                else
                {
                    arrListOfItem = new ArrayList<String>();
                    arrListOfItem.add(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate);
                }
            }
            else
            {
                mapSelectedItemList = new HashMap<String, List<String>>();
                arrListOfItem = new ArrayList<String>();
                arrListOfItem.add(itemName + "#" + itemQty + "#" + itemAmt + "#" + itemCode + "#" + waiterNo + "#" + createdDate);
            }
            mapSelectedItemList.put(itemCode, arrListOfItem);
            hmExistingKOTItemList.put(kotNo, mapSelectedItemList);
        }
        rs.close();
        funPreviousKOTList();
    }

    private void funPreviousKOTList() throws Exception
    {
        List<List<String>> arrMainListToFillGrid = null;
        Map<String, List<List<String>>> mapMainItemList = new HashMap<String, List<List<String>>>();
        for (Map.Entry<String, Map<String, List<String>>> entry : hmExistingKOTItemList.entrySet())
        {
            String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "";
            Map<String, List<String>> mapSelectedItem = entry.getValue();
            for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
            {
                List<String> listOfParam = entry1.getValue();
                for (int i = 0; i < listOfParam.size(); i++)
                {
                    String[] param = listOfParam.get(i).split("#");
                    itemCode = param[3];
                    itemName = param[0];
                    itemQty = param[1];
                    itemAmt = param[2];
                    waiterNo = param[4];
                    createdDate = param[5];
                }
                if (mapMainItemList.containsKey(entry1.getKey()))
                {
                    arrMainListToFillGrid = mapMainItemList.get(entry1.getKey());
                    for (int c = 0; c < arrMainListToFillGrid.size(); c++)
                    {
                        listOfParam = arrMainListToFillGrid.get(c);
                        for (int count = 0; count < listOfParam.size(); count++)
                        {
                            String[] param = listOfParam.get(count).split("#");
                            if (itemCode.equals(param[3]))
                            {
                                double qty = Double.valueOf(param[1]);
                                double amt = Double.valueOf(param[2]);
                                arrMainListToFillGrid.remove(c);
                                listOfParam = new ArrayList<String>();
                                listOfParam.add(itemName + "#" + String.valueOf(qty + Double.valueOf(itemQty)) + "#" + String.valueOf(amt + Double.valueOf(itemAmt)) + "#" + itemCode + "#" + waiterNo + "#" + createdDate);
                                break;
                            }

                        }
                    }

                }
                else
                {
                    arrMainListToFillGrid = new ArrayList<List<String>>();
                }
                arrMainListToFillGrid.add(listOfParam);
                mapMainItemList.put(entry1.getKey(), arrMainListToFillGrid);
            }

        }
        if (mapMainItemList.size() > 0)
        {
            // funFillGrid(mapMainItemList);
            funFillGrid();
            System.out.println(mapMainItemList);
        }
    }

    private void funMoveItemToTable() throws Exception
    {
        if (funValidateTables())
        {
            double taxAmt = 0, subTotalAmt = 0;
            List<clsItemDtlForTax> arrListItemDtl = new ArrayList<clsItemDtlForTax>();

            if (hmSelectedItemList.size() > 0)
            {
                for (Map.Entry<String, Map<String, List<String>>> entry : hmSelectedItemList.entrySet())
                {
                    Map<String, List<String>> mapSelectedItem = entry.getValue();
                    for (Map.Entry<String, List<String>> entry1 : mapSelectedItem.entrySet())
                    {
                        List<String> listOfParam = entry1.getValue();
                        for (int i = 0; i < listOfParam.size(); i++)
                        {
                            String[] param = listOfParam.get(i).split("#");
                            subTotalAmt = subTotalAmt + Double.parseDouble(param[2]);

                            clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                            objItemDtlForTax.setItemCode(param[3]);
                            objItemDtlForTax.setItemName(param[0]);
                            objItemDtlForTax.setAmount(Double.valueOf(param[2]));
                            objItemDtlForTax.setDiscAmt(0);
                            objItemDtlForTax.setDiscPer(0);
                            arrListItemDtl.add(objItemDtlForTax);
                        }
                    }
                }
            }

            if (arrListItemDtl.size() > 0)
            {
                String areaCode = "";
                String sql = "select strAreaCode from tbltablemaster where strTableNo='" + mapTableName.get(cmbTable.getSelectedItem().toString()) + "' ";
                ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsAreaCode.next())
                {
                    areaCode = rsAreaCode.getString(1);
                }
                rsAreaCode.close();
                List<clsTaxCalculationDtls> arrListTaxDtl = objUtility.funCalculateTax(arrListItemDtl, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSDateForTransaction, areaCode, "DineIn", subTotalAmt, 0, "Make KOT", "S01","Sales");
                for (clsTaxCalculationDtls objTaxDtl : arrListTaxDtl)
                {
                    taxAmt += objTaxDtl.getTaxAmount();
                }
                arrListTaxDtl = null;
            }

            if (hmSelectedItemList.size() > 0)
            {
                btnSave.setEnabled(false);
                funSaveKOT(taxAmt);
            }
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
        panelMainForm = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelOpenTable = new javax.swing.JPanel();
        btnOpenTable2 = new javax.swing.JButton();
        btnOpenTable1 = new javax.swing.JButton();
        btnOpenTable3 = new javax.swing.JButton();
        btnOpenTable4 = new javax.swing.JButton();
        btnOpenTable5 = new javax.swing.JButton();
        btnOpenTable6 = new javax.swing.JButton();
        btnOpenTable7 = new javax.swing.JButton();
        btnOpenTable8 = new javax.swing.JButton();
        btnOpenTable9 = new javax.swing.JButton();
        btnOpenTable10 = new javax.swing.JButton();
        btnOpenTable11 = new javax.swing.JButton();
        btnOpenTable12 = new javax.swing.JButton();
        btnOpenTable13 = new javax.swing.JButton();
        btnOpenTable14 = new javax.swing.JButton();
        btnOpenTable15 = new javax.swing.JButton();
        btnOpenTable16 = new javax.swing.JButton();
        lblOpenTable = new javax.swing.JLabel();
        lblOpenTable1 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        cmbBusyTable = new javax.swing.JComboBox();
        cmbTable = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableKOTList = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableNewKOTList = new javax.swing.JTable();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

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
        lblProductName.setText("SPOS ");
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
        lblformName.setText("- Move KOT Items To Table");
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
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);
        panelFormBody.setPreferredSize(new java.awt.Dimension(815, 500));

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);
        panelOpenTable.setOpaque(false);

        btnOpenTable2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable2.setAlignmentY(0.1F);
        btnOpenTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable2MouseClicked(evt);
            }
        });

        btnOpenTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable1.setAlignmentY(0.1F);
        btnOpenTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable1MouseClicked(evt);
            }
        });

        btnOpenTable3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable3.setAlignmentY(0.1F);
        btnOpenTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable3MouseClicked(evt);
            }
        });

        btnOpenTable4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable4.setAlignmentY(0.1F);
        btnOpenTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable4MouseClicked(evt);
            }
        });

        btnOpenTable5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable5.setAlignmentY(0.1F);
        btnOpenTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable5MouseClicked(evt);
            }
        });

        btnOpenTable6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable6.setAlignmentY(0.3F);
        btnOpenTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable6MouseClicked(evt);
            }
        });

        btnOpenTable7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable7.setAlignmentY(0.3F);
        btnOpenTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable7MouseClicked(evt);
            }
        });

        btnOpenTable8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable8MouseClicked(evt);
            }
        });

        btnOpenTable9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable9MouseClicked(evt);
            }
        });

        btnOpenTable10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable10MouseClicked(evt);
            }
        });

        btnOpenTable11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable11MouseClicked(evt);
            }
        });

        btnOpenTable12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable12MouseClicked(evt);
            }
        });

        btnOpenTable13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable13MouseClicked(evt);
            }
        });

        btnOpenTable14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable14MouseClicked(evt);
            }
        });

        btnOpenTable15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable15MouseClicked(evt);
            }
        });

        btnOpenTable16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelOpenTableLayout = new javax.swing.GroupLayout(panelOpenTable);
        panelOpenTable.setLayout(panelOpenTableLayout);
        panelOpenTableLayout.setHorizontalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOpenTableLayout.createSequentialGroup()
                            .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOpenTableLayout.createSequentialGroup()
                            .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOpenTableLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelOpenTableLayout.setVerticalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(87, 87, 87))
        );

        lblOpenTable.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOpenTable.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable.setText("OPEN KOTs");

        lblOpenTable1.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOpenTable1.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable1.setText("ALL TABLES");

        btnSave.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        cmbBusyTable.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbBusyTable.setToolTipText("Select Table");
        cmbBusyTable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBusyTableActionPerformed(evt);
            }
        });

        cmbTable.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbTable.setToolTipText("Select POS");
        cmbTable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTableActionPerformed(evt);
            }
        });

        tableKOTList.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tableKOTList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount", "Select", "ItemCode", "WaiterNo", "KOTNo", "createdDate", "serialNo", "RedeemAmt", "customerCode"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, false, false, false, false, false, false, false
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
        tableKOTList.setPreferredSize(new java.awt.Dimension(400, 200));
        tableKOTList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tableKOTListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableKOTList);
        if (tableKOTList.getColumnModel().getColumnCount() > 0)
        {
            tableKOTList.getColumnModel().getColumn(4).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(4).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(4).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(5).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(5).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(5).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(6).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(6).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(6).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(7).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(7).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(7).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(8).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(8).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(8).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(9).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(9).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(9).setMaxWidth(0);
            tableKOTList.getColumnModel().getColumn(10).setMinWidth(0);
            tableKOTList.getColumnModel().getColumn(10).setPreferredWidth(0);
            tableKOTList.getColumnModel().getColumn(10).setMaxWidth(0);
        }

        tableNewKOTList.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tableNewKOTList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount", "ItemCode", "WaiterNo", "createdDate"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tableNewKOTList.setPreferredSize(new java.awt.Dimension(320, 350));
        jScrollPane2.setViewportView(tableNewKOTList);
        if (tableNewKOTList.getColumnModel().getColumnCount() > 0)
        {
            tableNewKOTList.getColumnModel().getColumn(3).setMinWidth(0);
            tableNewKOTList.getColumnModel().getColumn(3).setPreferredWidth(0);
            tableNewKOTList.getColumnModel().getColumn(3).setMaxWidth(0);
            tableNewKOTList.getColumnModel().getColumn(4).setMinWidth(0);
            tableNewKOTList.getColumnModel().getColumn(4).setPreferredWidth(0);
            tableNewKOTList.getColumnModel().getColumn(4).setMaxWidth(0);
            tableNewKOTList.getColumnModel().getColumn(5).setMinWidth(0);
            tableNewKOTList.getColumnModel().getColumn(5).setPreferredWidth(0);
            tableNewKOTList.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        btnPrevious.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious.setText("<<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext.setText(">>>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });

        btnOk.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnOk.setForeground(new java.awt.Color(255, 255, 255));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(cmbBusyTable, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(192, 192, 192))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)))
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelOpenTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(34, 34, 34)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(cmbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                                .addComponent(lblOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addGap(37, 37, 37)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbBusyTable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(lblOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormBodyLayout.createSequentialGroup()
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(86, 86, 86))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable16MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(15);
        funSelectOpenTable(btnOpenTable16.getText(), 15);

    }//GEN-LAST:event_btnOpenTable16MouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try
        {
            String tblNo = mapTableName.get(cmbTable.getSelectedItem().toString());
            if(false)//if (objUtility.funCheckTableStatusFromItemRTemp(tableNo))
            {
                JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
            }
            else
            {
                funMoveItemToTable();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Move KOT Items");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void cmbBusyTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBusyTableActionPerformed
        // TODO add your handling code here:
        if (cmbBusyTable.getSelectedIndex() > 0)
        {
            funFillOpenKOTVector(mapBusyTableName.get(cmbBusyTable.getSelectedItem().toString()));
        }
    }//GEN-LAST:event_cmbBusyTableActionPerformed

    private void cmbTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTableActionPerformed
        // TODO add your handling code here:
        try
        {
            if (!cmbBusyTable.getSelectedItem().toString().equals("Select Table"))
            {
                if (cmbTable.getSelectedItem().toString().equals(cmbBusyTable.getSelectedItem().toString()))
                {
                    JOptionPane.showMessageDialog(this, "Please select another table to move KOT item !!!");
                    return;
                }
                hmExistingKOTItemList = new HashMap<String, Map<String, List<String>>>();
                funFillExistingKOTDetails();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_cmbTableActionPerformed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

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

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Move KOT Items");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Move KOT Items");
    }//GEN-LAST:event_formWindowClosing

    private void btnOpenTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable15MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(14);
        funSelectOpenTable(btnOpenTable15.getText(), 14);

    }//GEN-LAST:event_btnOpenTable15MouseClicked

    private void btnOpenTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable14MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(13);
        funSelectOpenTable(btnOpenTable14.getText(), 13);

    }//GEN-LAST:event_btnOpenTable14MouseClicked

    private void btnOpenTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable13MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(12);
        funSelectOpenTable(btnOpenTable13.getText(), 12);

    }//GEN-LAST:event_btnOpenTable13MouseClicked

    private void btnOpenTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable12MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(11);
        funSelectOpenTable(btnOpenTable12.getText(), 11);

    }//GEN-LAST:event_btnOpenTable12MouseClicked

    private void btnOpenTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable11MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(10);
        funSelectOpenTable(btnOpenTable11.getText(), 10);

    }//GEN-LAST:event_btnOpenTable11MouseClicked

    private void btnOpenTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable10MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(9);
        funSelectOpenTable(btnOpenTable10.getText(), 9);

    }//GEN-LAST:event_btnOpenTable10MouseClicked

    private void btnOpenTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable9MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(8);
        funSelectOpenTable(btnOpenTable9.getText(), 8);

    }//GEN-LAST:event_btnOpenTable9MouseClicked

    private void btnOpenTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable8MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(7);
        funSelectOpenTable(btnOpenTable8.getText(), 7);

    }//GEN-LAST:event_btnOpenTable8MouseClicked

    private void btnOpenTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable7MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(6);
        funSelectOpenTable(btnOpenTable7.getText(), 6);

    }//GEN-LAST:event_btnOpenTable7MouseClicked

    private void btnOpenTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable6MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(5);
        funSelectOpenTable(btnOpenTable6.getText(), 5);

    }//GEN-LAST:event_btnOpenTable6MouseClicked

    private void btnOpenTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable5MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(4);
        funSelectOpenTable(btnOpenTable5.getText(), 4);

    }//GEN-LAST:event_btnOpenTable5MouseClicked

    private void btnOpenTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable4MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(3);
        funSelectOpenTable(btnOpenTable4.getText(), 3);

    }//GEN-LAST:event_btnOpenTable4MouseClicked

    private void btnOpenTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable3MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(2);
        funSelectOpenTable(btnOpenTable3.getText(), 2);

    }//GEN-LAST:event_btnOpenTable3MouseClicked

    private void btnOpenTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable1MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(0);
        funSelectOpenTable(btnOpenTable1.getText(), 0);
    }//GEN-LAST:event_btnOpenTable1MouseClicked

    private void btnOpenTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable2MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(1);
        funSelectOpenTable(btnOpenTable2.getText(), 1);


    }//GEN-LAST:event_btnOpenTable2MouseClicked

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate--;
            btnNext.setEnabled(true);
            if (cntNavigate == 0)
            {
                btnPrevious.setEnabled(false);
                funLoadOpenKOTs(0, vOpenTableNo.size());
            }
            else
            {
                int tableSize = cntNavigate * 16;
                int resMod = vOpenTableNo.size() % tableSize;
                int resDiv = vOpenTableNo.size() / tableSize;
                int totalSize = tableSize + 16;
                //System.out.println("Size="+vOpenTableNo.size()+"\tMod="+resMod+"\tdiv="+resDiv+"\tsss="+tableSize);
                funLoadOpenKOTs(tableSize, totalSize);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate++;
            int tableSize = cntNavigate * 16;
            int resMod = vOpenTableNo.size() % tableSize;
            int resDiv = vOpenTableNo.size() / tableSize;
            int totalSize = tableSize + 16;
            funLoadOpenKOTs(tableSize, totalSize);
            btnPrevious.setEnabled(true);
            if (resDiv == cntNavigate)
            {
                btnNext.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        try
        {
            if (cmbTable.getSelectedItem().equals("Select Table"))
            {
                JOptionPane.showMessageDialog(this, "Please select new table to move KOT item !!!");
                return;
            }

            //funAddSelectedItemToNewKOTList();
            funFillGrid();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnOkActionPerformed

    private void tableKOTListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableKOTListMouseClicked
        // TODO add your handling code here:
        try
        {
            funTableKOTListClicked();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_tableKOTListMouseClicked

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
            java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmMoveKOT().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnOpenTable1;
    private javax.swing.JButton btnOpenTable10;
    private javax.swing.JButton btnOpenTable11;
    private javax.swing.JButton btnOpenTable12;
    private javax.swing.JButton btnOpenTable13;
    private javax.swing.JButton btnOpenTable14;
    private javax.swing.JButton btnOpenTable15;
    private javax.swing.JButton btnOpenTable16;
    private javax.swing.JButton btnOpenTable2;
    private javax.swing.JButton btnOpenTable3;
    private javax.swing.JButton btnOpenTable4;
    private javax.swing.JButton btnOpenTable5;
    private javax.swing.JButton btnOpenTable6;
    private javax.swing.JButton btnOpenTable7;
    private javax.swing.JButton btnOpenTable8;
    private javax.swing.JButton btnOpenTable9;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbBusyTable;
    private javax.swing.JComboBox cmbTable;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTable;
    private javax.swing.JLabel lblOpenTable1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    private javax.swing.JTable tableKOTList;
    private javax.swing.JTable tableNewKOTList;
    // End of variables declaration//GEN-END:variables

    private void funSendMessageToCostCenters(List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
        try
        {
            funCreateTempFolder();
            String filePath = System.getProperty("user.dir");
            String filename = (filePath + "/Temp/MoveKOTItems.txt");
            File file = new File(filename);

            String shiftedTableName = cmbTable.getSelectedItem().toString();
            String shiftedTableNo = mapTableName.get(shiftedTableName);

            funCreateTestTextFile(file, shiftedTableName, listOfItemsForFromTable);

            String sqlCostCenters = "select b.strCostCenterCode ,c.strCostCenterName,c.strPrinterPort,c.strSecondaryPrinterPort,c.strPrintOnBothPrinters "
                    + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
                    + "where a.strTableNo='" + shiftedTableNo + "' "
                    + "and a.strItemCode=b.strItemCode "
                    + "and a.strPOSCode=b.strPosCode "
                    + "and b.strCostCenterCode=c.strCostCenterCode "
                    + "group by c.strCostCenterCode; ";
            ResultSet rsCostCenters = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenters);
            while (rsCostCenters.next())
            {

                String primaryPrinterName = rsCostCenters.getString(3);
                String secondaryPrinterName = rsCostCenters.getString(4);
                String printOnBothPrinters = rsCostCenters.getString(5);

                //printing
                new clsUtility2().funPrintToPrinter(primaryPrinterName, secondaryPrinterName, "MoveKOTItems", printOnBothPrinters, false);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCreateTempFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File PrintText = new File(filePath + "/Temp");
            if (!PrintText.exists())
            {
                PrintText.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCreateTestTextFile(File file, String toTableName, List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
        BufferedWriter fileWriter = null;
        try
        {

            DecimalFormat decimalFormat = new DecimalFormat("0.##");
            //File file=new File(filename);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

            String fileHeader = "---ITEMS SHIFTED MESSAGE-------";
            String dottedLine = "----------------------------------";
            String newLine = "\n";
            String blankLine = "                                   ";

            fileWriter.write(fileHeader);
            fileWriter.newLine();
            fileWriter.write(dottedLine);
            fileWriter.newLine();
            fileWriter.write("User Name : " + clsGlobalVarClass.gUserName);
            fileWriter.newLine();
            fileWriter.write("POS Name : " + clsGlobalVarClass.gPOSName);
            fileWriter.newLine();
            //message

            fileWriter.newLine();
            fileWriter.write("Items Shifted From Table No. ");
	    fileWriter.write(cmbBusyTable.getSelectedItem().toString()+" To Table No. " + toTableName + ".");
            fileWriter.newLine();
            fileWriter.write(dottedLine);

            if (clsGlobalVarClass.gPrintItemsOnMoveKOTMoveTable)
            {
                Iterator<clsMakeKotItemDtl> it = listOfItemsForFromTable.iterator();
                while (it.hasNext())
                {
                    clsMakeKotItemDtl objKotItemDtl = it.next();
                    fileWriter.newLine();
                    fileWriter.write(decimalFormat.format(objKotItemDtl.getQty()) + "  " + objKotItemDtl.getItemName());
                }
                fileWriter.newLine();
                fileWriter.write(dottedLine);
            }

            fileWriter.newLine();
            for (int cntLines = 0; cntLines < Integer.parseInt(clsGlobalVarClass.gNoOfLinesInKOTPrint); cntLines++)
            {
                fileWriter.newLine();
            }
            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                fileWriter.write("V");//Linux
            }
            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
                {
                    fileWriter.write("V");
                }
                else
                {
                    fileWriter.write("m");//windows
                }
            }
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
}
