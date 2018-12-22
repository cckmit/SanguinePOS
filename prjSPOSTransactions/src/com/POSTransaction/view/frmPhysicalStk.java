/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPSPDtl;
import com.POSGlobal.controller.clsPSPHd;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsStockInDtl;
import com.POSGlobal.controller.clsStockInHd;
import com.POSGlobal.controller.clsStockOutDtl;
import com.POSGlobal.controller.clsStockOutHd;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javafx.scene.control.Cell;
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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class frmPhysicalStk extends javax.swing.JFrame 
{
    private String textValue2,itemCode;
    private boolean numFlag;
    private Map<String,clsPSPDtl> hmPSPDtl=null;
    private Map<String,clsStockInDtl> hmStockInDtl= new HashMap<String,clsStockInDtl>();
    private Map<String,clsStockOutDtl> hmStockOutDtl= new HashMap<String,clsStockOutDtl>();
    clsStockOutHd objStockOutHd = new clsStockOutHd();
    clsStockInHd objStockInHd = new clsStockInHd();
    private clsUtility objUtility;
    private frmStkIn objFrmStkIn;
    private String selectedFileName;
    private String postingRemarks="";
    private String selectedReasonCode="";
    
    public frmPhysicalStk()
    {
        initComponents();
        try 
        {
            objUtility=new clsUtility();
            objFrmStkIn=new frmStkIn();
            //btnPopulateItem.setVisible(false);
           
            txtExtCode.requestFocus();
            hmPSPDtl= new HashMap<String,clsPSPDtl>();
            String bdte=clsGlobalVarClass.gPOSStartDate;
            lblStkDate.setText(bdte);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            textValue2="";
            selectedFileName="";
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            scrTax.setVisible(false);
            
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(140);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(65);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblItemTable.getColumnModel().getColumn(5).setPreferredWidth(60);
            tblItemTable.setShowHorizontalLines(true);
            
            funCalculateItemStock();
            funSetShortCutKeys();
           
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    private void funSetShortCutKeys()
    {
        btnHome.setMnemonic('h');
        btnDone.setMnemonic('d');
        btnModifyStk.setMnemonic('m');
        btnUp.setMnemonic('u');
        btnDown.setMnemonic('w');
        btnDelItem.setMnemonic('l');
        btnPopulateItem.setMnemonic('p');
    }
    
    
    private void funSetItemDetails(Object[] data)
    {
        try
        {
            String sql="select * from tblitemmaster where strItemCode='"+data[0].toString()+"'";
            ResultSet rsItemData=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsItemData.next())
            {
                itemCode=rsItemData.getString(1);
                txtItemCode.setText(rsItemData.getString(2));
                txtExtCode.setText(rsItemData.getString(9));
                txtQty.setText("1.00");
                txtQty.selectAll();
            }
            rsItemData.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    } 
    
    private void funSetExternalCode1(String itCode)
    {
        try
        {             
            String sql="select strExternalCode from tblitemmaster where strItemCode='"+itCode+"'";
            ResultSet rsExtCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsExtCode.next())
            {
                txtExtCode.setText(rsExtCode.getString(1));
            }
            else
            {
                txtExtCode.setText("");
            }
            rsExtCode.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     
    private void funSetExternalCode(String code)
    {
        try
        {
            String[] itemData=new String[7];
            txtExtCode.setText(code);
            String sql="select strItemCode,strItemName,dblPurchaseRate from tblitemmaster where strExternalCode='"+code+"'";
            ResultSet rsExt=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsExt.next())
            {
                itemData[0]=rsExt.getString(1);
                itemData[1]=rsExt.getString(2);
                itemData[2]="";
                itemData[3]="";
                itemData[4]="";
                itemData[5]=rsExt.getString(3);
                itemData[6]=code;
                funSetItemDetails(itemData);
                txtQty.setText("1");
                txtQty.requestFocus();
                txtQty.selectAll();
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Invalid External Code");
                String exCode=txtExtCode.getText();
                StringBuilder sb=new StringBuilder(exCode);
                txtExtCode.setText(sb.substring(0,2).toString());
                txtExtCode.requestFocus();
            }
            rsExt.close();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String convertString(String ItemName)
    {
        if (ItemName.contains("<html>"))
        {
            String tempitemName = ItemName;
            StringBuilder sb1 = new StringBuilder(tempitemName);
            sb1 = sb1.delete(0, 6);
            int seq = sb1.lastIndexOf("<br>");
            String split = sb1.substring(0, seq);
            int end = sb1.lastIndexOf("</html>");
            String last = sb1.substring(seq + 4, end);
            ItemName = split + " " + last;
        }
        return ItemName;
    }
    
    private String funGenerateStockInCode() {
        
        long lastNo = 0; 
        String stockInCode="";
        try {
          
            String sql = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockInNo'";
            ResultSet rsStockinno = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsStockinno.next()) {
                lastNo = rsStockinno.getLong(2);
                lastNo = lastNo + 1;
                rsStockinno.close();
                stockInCode = "SI" + String.format("%07d", lastNo);
                  
                System.out.println(stockInCode);
                sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='stockinNo'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            rsStockinno.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            return stockInCode;
        }
    }

    
    private String funGeneratePhysicalStockCode() 
    {
        String pspCode="";
        long lastNo = 0;
        try 
        {
            String sql = "select strTransactionType,dblLastNo from tblinternal "
                + "where strTransactionType='Physicalstock'";
            ResultSet rsStockinno = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsStockinno.next()) {
                lastNo = rsStockinno.getLong(2);
                lastNo = lastNo + 1;
                pspCode = "PS" + String.format("%07d", lastNo);
                sql = "update tblinternal set dblLastNo='" + lastNo + "' "
                    + "where strTransactionType='Physicalstock'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pspCode;
    }
    
    private void funSavePhysicalStk()
    {
        clsPSPHd objPSPHd=new clsPSPHd();
        String pspCode="";
        int ex=0;
    
        try
        {
            if (tblItemTable.getModel().getRowCount() > 0)
            {
                funSelectionOfRemarkAndReason();
                if(selectedReasonCode.isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Select reasons for physical stock....");
                    return;
                }

                StringBuilder sbPhyStkItems=new StringBuilder();
                sbPhyStkItems.setLength(0);
                Set<String> setPhyStkItems = hmPSPDtl.keySet();
                for(String item : setPhyStkItems)
                {
                    if(sbPhyStkItems.length()==0)
                    {
                        sbPhyStkItems.append("'"+item+"'");
                    }
                    else
                    {
                        sbPhyStkItems.append(",'"+item+"'");
                    }
                }

                String sql="select * from tblitemcurrentstk where strItemCode not in ("+sbPhyStkItems+") and intBalance>0 ";
                System.out.println(sql);
                ResultSet rsPhysTkItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsPhysTkItems.next())
                {
                    double compQty=rsPhysTkItems.getDouble(10);
                    double phyQty=rsPhysTkItems.getDouble(10);
                    clsPSPDtl objPSPDtl = new clsPSPDtl();

                    objPSPDtl.setStrItemCode(rsPhysTkItems.getString(3));
                    objPSPDtl.setStrItemName(rsPhysTkItems.getString(4));
                    objPSPDtl.setStrPSPCode("");
                    objPSPDtl.setDblCompStk(compQty);
                    objPSPDtl.setDblPhyStk(phyQty);
                    double dblVarQty=(compQty)-(phyQty);
                    objPSPDtl.setDblVariance(dblVarQty);
                    objPSPDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                    objPSPDtl.setStrDataPostFlag("N");
                    objPSPDtl.setDblVairanceAmt(0);
                    hmPSPDtl.put(rsPhysTkItems.getString(3),objPSPDtl);
                }
                rsPhysTkItems.close();


                java.util.Date objDate=new java.util.Date();
                String date=(objDate.getYear()+1900)+"-"+(objDate.getMonth()+1)+"-"+objDate.getDate()
                    +" "+objDate.getHours()+":"+objDate.getMinutes()+":"+objDate.getSeconds();

                if(lblPhysicalStkNo.getText().isEmpty())
                {
                    pspCode=funGeneratePhysicalStockCode();

                    objPSPHd.setStrPSPCode(pspCode);
                    objPSPHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
                    objPSPHd.setStrStkInCode("");
                    objPSPHd.setStrStkOutCode("");
                    objPSPHd.setStrBillNo("");
                    objPSPHd.setDblStkInAmt(Double.parseDouble(lblStkInTotal.getText()));
                    objPSPHd.setDblSaleAmt(Double.parseDouble(lblSaleTotal.getText()));
                    objPSPHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
                    objPSPHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
                    objPSPHd.setDteDateCreated(date);
                    objPSPHd.setDteDateEdited(date);
                    objPSPHd.setStrClientCode(clsGlobalVarClass.gClientCode);
                    objPSPHd.setStrDataPostFlag("N");
                    objPSPHd.setStrReasonCode(selectedReasonCode);
                    objPSPHd.setStrRemark(postingRemarks);


                    ex=funInsertPhysicalStockToTable(objPSPHd,hmPSPDtl);
                    if(ex>0)
                    {
                        lblPhysicalStkNo.setText(pspCode);
                        JOptionPane.showMessageDialog(this,"Physaical Stk No : "+pspCode);
                        int choice = JOptionPane.showConfirmDialog(this, "Do you want to Print ?", "", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) 
			{
			    funPhysicalStockReport(pspCode);
			}
                        if(hmStockOutDtl.size()>0)
                        {
                            objFrmStkIn.funInsertStockOutDtlTable(hmStockOutDtl, objStockOutHd);
                            objFrmStkIn.funStockInOutReport("StockOut",objStockOutHd.getStrStkOutCode(),lblStkDate.getText());
                        }
                        if(hmStockInDtl.size()>0)
                        {
                            objFrmStkIn.funInsertStockInDataTable(hmStockInDtl, objStockInHd);
                            objFrmStkIn.funStockInOutReport("StockIn", objStockInHd.getStrStkInCode(),lblStkDate.getText());
                        }
                    }
                }
                else
                {
                    pspCode=lblPhysicalStkNo.getText();
                    objPSPHd.setStrPSPCode(pspCode);
                    objPSPHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
                    objPSPHd.setStrStkInCode("");
                    objPSPHd.setStrStkOutCode("");
                    objPSPHd.setStrBillNo("");
                    objPSPHd.setDblStkInAmt(Double.parseDouble(lblStkInTotal.getText()));
                    objPSPHd.setDblSaleAmt(Double.parseDouble(lblSaleTotal.getText()));
                    objPSPHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
                    objPSPHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
                    objPSPHd.setDteDateCreated(date);
                    objPSPHd.setDteDateEdited(date);
                    objPSPHd.setStrClientCode(clsGlobalVarClass.gClientCode);
                    objPSPHd.setStrDataPostFlag("N");
                    objPSPHd.setStrReasonCode(selectedReasonCode);
                    objPSPHd.setStrRemark(postingRemarks);


                    ex=funInsertPhysicalStockToTable(objPSPHd,hmPSPDtl);
                    if(ex>0)
                    {
                        lblPhysicalStkNo.setText(pspCode);
                        JOptionPane.showMessageDialog(this,"Physaical Stk No : "+pspCode);
                        int choice = JOptionPane.showConfirmDialog(this, "Do you want to Print ?", "", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) 
			{
			    funPhysicalStockReport(pspCode);
			}
                        if(hmStockOutDtl.size()>0)
                        {
                            objFrmStkIn.funInsertStockOutDtlTable(hmStockOutDtl, objStockOutHd);
                            objFrmStkIn.funStockInOutReport("StockOut",objStockOutHd.getStrStkOutCode(),lblStkDate.getText());
                        }
                        if(hmStockInDtl.size()>0)
                        {
                            objFrmStkIn.funInsertStockInDataTable(hmStockInDtl, objStockInHd);
                            objFrmStkIn.funStockInOutReport("StockIn", objStockInHd.getStrStkInCode(),lblStkDate.getText());
                        }
                    }
                }

                if (clsGlobalVarClass.gConnectionActive.equals("Y")) 
                {
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Inventory","Stock");
                }
                funResetFields();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Please Enter item");
                return;
            }    
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private String funGenerateStockOutCode()
    {
        String stockOutCode="";
        long lastNo1=0;
        String sql1;
        try
        {
            sql1 = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockOutNo'";
            ResultSet rsStockOutno = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
            if (rsStockOutno.next()) 
            {
                lastNo1 = rsStockOutno.getLong(2);
                lastNo1 = lastNo1 + 1;
                stockOutCode = "SO" + String.format("%07d", lastNo1);
                String sql = "update tblinternal set dblLastNo='" + lastNo1 + "' where strTransactionType='stockOutNo'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            return stockOutCode;
        }
    }
    
    
    private int funCalculateItemStock() throws Exception
    {
        String sql="select date(dteStartDate) from tblsetup";
        ResultSet rsStartDate=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsStartDate.next())
        {
            String date=rsStartDate.getString(1);
            Date dt=new Date(Integer.parseInt(date.split("-")[0])-1900,Integer.parseInt(date.split("-")[1])-1,Integer.parseInt(date.split("-")[2]));
            clsGlobalVarClass.funCalculateStock(dt, new Date(), "All", "Both", "Stock");
        }
        rsStartDate.close();
        return 1;
    }
  
   
    
    
    //Insert physical stock data to tblpspdtl and tblpsphd table
    
    public int funInsertPhysicalStockToTable(clsPSPHd objPSPHd,Map<String,clsPSPDtl> hmPSPDtl) throws Exception
    {
        int rows=0;
        String stockInCode="";
        String stockOutCode="";
        String sql="";
        String sqlInsertPSPDtl="";
      
        sqlInsertPSPDtl= " insert into tblpspdtl (strPSPCode,strItemCode,dblPhyStk,dblCompStk,dblVariance,"
            + " dblVairanceAmt,strClientCode,strDataPostFlag) "
            + " values ";
            
        for(clsPSPDtl objPSPDtl: hmPSPDtl.values())
        {
            Double variance=objPSPDtl.getDblVariance();
            itemCode=objPSPDtl.getStrItemCode();
//            if(objPSPDtl.getDblPhyStk()!=0)
//            {
                double purchaseRate=0;
                if(clsGlobalVarClass.gEffectOnPSP)
                {
                    sql="select dblPurchaseRate from tblitemmaster where strItemCode='"+itemCode+"'";
                    System.out.println(sql);
                    ResultSet rsPurchaseRate=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rsPurchaseRate.next())
                    {
                        purchaseRate=rsPurchaseRate.getDouble(1);
                    }
                    rsPurchaseRate.close();
                    
                    if(variance>0)
                    { 
                        if(stockInCode.isEmpty())
                        {
                              stockInCode=funGenerateStockInCode();
                        }
                        objStockInHd.setStrStkInCode(stockInCode);
                        objStockInHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
                        objStockInHd.setDteStkInDate(clsGlobalVarClass.gPOSDateForTransaction);
                        objStockInHd.setStrReasonCode("R01");
                        objStockInHd.setStrPurchaseBillNo("");
                        objStockInHd.setDtePurchaseBillDate(clsGlobalVarClass.gPOSDateForTransaction);
                        objStockInHd.setIntShiftCode(0);
                        objStockInHd.setStrUserCreated(lblUserCode.getText());
                        objStockInHd.setStrUserEdited(lblUserCode.getText());
                        objStockInHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
                        objStockInHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
                        objStockInHd.setStrClientCode(clsGlobalVarClass.gClientCode);

                        clsStockInDtl objStockInDtl = new clsStockInDtl();
                        if(null!=hmStockInDtl.get(itemCode))
                        {
                            objStockInDtl=hmStockInDtl.get(itemCode);
                            objStockInDtl.setDblAmount(objStockInDtl.getDblAmount()+(purchaseRate * variance));
                            objStockInDtl.setDblQuantity(objStockInDtl.getDblQuantity()+variance);
                        }
                        else
                        {
                            objStockInDtl.setStrItemCode(itemCode);
                            objStockInDtl.setStrItemName(txtItemCode.getText());
                            objStockInDtl.setStrStkInCode(stockInCode);
                            objStockInDtl.setDblAmount(purchaseRate * variance);
                            objStockInDtl.setDblQuantity(variance);
                            objStockInDtl.setDblPurchaseRate(purchaseRate);
                            objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                            objStockInDtl.setStrDataPostFlag("N");
                        }
                        hmStockInDtl.put(itemCode,objStockInDtl);
                    }
                    if(variance<0)
                    {
                        double qty=(-1)*variance;

                        if(stockOutCode.isEmpty())
                        {
                            stockOutCode=funGenerateStockOutCode();
                        }
                        objStockOutHd.setStrStkOutCode(stockOutCode);
                        objStockOutHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
                        objStockOutHd.setDteStkOutDate(clsGlobalVarClass.gPOSDateForTransaction);
                        objStockOutHd.setStrReasonCode("R01");
                        objStockOutHd.setStrPurchaseBillNo("");
                        objStockOutHd.setDtePurchaseBillDate(clsGlobalVarClass.gPOSDateForTransaction);
                        objStockOutHd.setIntShiftCode(0);
                        objStockOutHd.setStrUserCreated(lblUserCode.getText());
                        objStockOutHd.setStrUserEdited(lblUserCode.getText());
                        objStockOutHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
                        objStockOutHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
                        objStockOutHd.setStrClientCode(clsGlobalVarClass.gClientCode);

                        clsStockOutDtl objStockOutDtl = new clsStockOutDtl();
                        if(null!=hmStockOutDtl.get(itemCode))
                        {
                           objStockOutDtl=hmStockOutDtl.get(itemCode);
                           objStockOutDtl.setDblAmount(objStockOutDtl.getDblAmount()+(purchaseRate * qty));
                           objStockOutDtl.setDblQuantity(objStockOutDtl.getDblQuantity()+qty);
                        }
                        else
                        {
                            objStockOutDtl.setStrItemCode(itemCode);
                            objStockOutDtl.setStrItemName(txtItemCode.getText());
                            objStockOutDtl.setStrStkOutCode(stockOutCode);
                            objStockOutDtl.setDblAmount(purchaseRate * qty);
                            objStockOutDtl.setDblQuantity(qty);
                            objStockOutDtl.setDblPurchaseRate(purchaseRate);
                            objStockOutDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                            objStockOutDtl.setStrDataPostFlag("N");
                        }
                        hmStockOutDtl.put(itemCode,objStockOutDtl);
                    }
                }
                itemCode="";
//            }
            sqlInsertPSPDtl+=" ('"+objPSPHd.getStrPSPCode()+"','"+objPSPDtl.getStrItemCode()+"','"+objPSPDtl.getDblPhyStk()+"',"
                + " '"+objPSPDtl.getDblCompStk()+"','"+objPSPDtl.getDblVariance()+"','"+objPSPDtl.getDblVairanceAmt()+"',"
                + " '"+objPSPDtl.getStrClientCode()+"','"+objPSPDtl.getStrDataPostFlag()+"'),";
        }

        sql="delete from tblpspdtl where strPSPCode='"+objPSPHd.getStrPSPCode()+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
        clsGlobalVarClass.dbMysql.execute(sql);

        StringBuilder sb = new StringBuilder(sqlInsertPSPDtl);
        int index = sb.lastIndexOf(",");
        sqlInsertPSPDtl = sb.delete(index, sb.length()).toString();
        System.out.println("sqlInsertPSPDtl="+sqlInsertPSPDtl);
        rows=clsGlobalVarClass.dbMysql.execute(sqlInsertPSPDtl);

        sql="delete from tblpsphd where strPSPCode='"+objPSPHd.getStrPSPCode()+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"'; ";
        clsGlobalVarClass.dbMysql.execute(sql);

        sql="insert into tblpsphd (strPSPCode,strPOSCode,strStkInCode,strStkOutCode,strBillNo,"
            + "dblStkInAmt,dblSaleAmt,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
            + ",strClientCode,strDataPostFlag,strReasonCode,strRemarks)"
            + " values('"+objPSPHd.getStrPSPCode()+"','"+objPSPHd.getStrPOSCode()+"','"+stockInCode+"','"+stockOutCode+"'"
            + ",'"+objPSPHd.getStrBillNo()+"','"+objPSPHd.getDblStkInAmt()+"','"+objPSPHd.getDblSaleAmt()+"','"+objPSPHd.getStrUserCreated()+"'"
            + ",'"+objPSPHd.getStrUserEdited()+"','"+objPSPHd.getDteDateCreated()+"','"+objPSPHd.getDteDateEdited()+"','"+objPSPHd.getStrClientCode()+"'"
            + ",'"+objPSPHd.getStrDataPostFlag()+"','"+objPSPHd.getStrReasonCode()+"','"+objPSPHd.getStrRemark()+"')";
        clsGlobalVarClass.dbMysql.execute(sql);
        
        return rows;
    }
    
    
    private void funResetFields() 
    {
        try 
        {
            numFlag=false;
            lblPhysicalStkNo.setText("");
            lblSaleTotal.setText("0");
            hmStockInDtl.clear();
            hmStockOutDtl.clear();
            hmPSPDtl.clear();
            selectedReasonCode="";
            postingRemarks="";
            DefaultTableModel dm = new DefaultTableModel();
            dm.addColumn("ItemCode");
            dm.addColumn("Description");
            dm.addColumn("Comp Stk");
            dm.addColumn("Phy Stk");
            dm.addColumn("Variance");
            dm.addColumn("Var Amt");
            tblItemTable.setModel(dm);
            lblSaleTotal.setText("");            
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.setShowHorizontalLines(true);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(140);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(65);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblItemTable.getColumnModel().getColumn(5).setPreferredWidth(60);
            lblStkInTotal.setText("");
            lblSaleTotal.setText("");
            selectedFileName="";
            btnImportExport.setText("Export");
            cmbOperationType.setSelectedItem("Export");
            lblFileName.setText("");
            lblExportedFileName.setText("");
            lblFileName.setVisible(false);
            lblExportedFileName.setVisible(false);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void funFillGridWithNonEnteredItems() throws Exception
    {
        String sql="select strItemCode,strItemName,intBalance,0,0,0 from tblitemcurrentstk where intBalance!=0 order by strItemName";
        ResultSet rsNonEnteredItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsNonEnteredItems.next())
        {
            itemCode= rsNonEnteredItems.getString(1);
            double comStk=Double.parseDouble(rsNonEnteredItems.getString(3).toString());
            double phyStk=Double.parseDouble(rsNonEnteredItems.getString(4).toString());
            double variance=phyStk-comStk;
            double purchaseRate=Double.parseDouble(rsNonEnteredItems.getString(6).toString());
            double varianceAmt=purchaseRate*variance;
            if(!hmPSPDtl.containsKey(itemCode))
            {
                clsPSPDtl objPSPDtl = new clsPSPDtl();
                objPSPDtl.setStrItemCode(itemCode);
                objPSPDtl.setStrItemName(rsNonEnteredItems.getString(2));
                objPSPDtl.setStrPSPCode("");
                objPSPDtl.setDblCompStk(comStk);
                objPSPDtl.setDblPhyStk(phyStk);
                objPSPDtl.setDblVariance(variance);
                objPSPDtl.setDblVairanceAmt(varianceAmt);
                objPSPDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                objPSPDtl.setStrDataPostFlag("N");
                hmPSPDtl.put(itemCode,objPSPDtl);
            }
            itemCode="";
        }  
        rsNonEnteredItems.close();
        funFillTable();
    }
    
    
    private void funFillTable() throws Exception
    {
        double purchaseRate=0;
        double saleTotal=0;
        double stkOutTotal=0;
        double saleRate=0;
        String[] dayPrice = {"strPriceSunday", "strPriceMonday", "strPriceTuesday", "strPriceWednesday", "strPriceThursday", "strPriceFriday", "strPriceSaturday"};
        int dayNo = new Date().getDay();
        String transDay = dayPrice[dayNo];

        DecimalFormat formt = new DecimalFormat("####0.00");
	
	 DefaultTableModel dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
       
        dm.addColumn("ItemCode");
        dm.addColumn("Description");
        dm.addColumn("Comp Stk");
        dm.addColumn("Phy Stk");
        dm.addColumn("Variance");
        dm.addColumn("Var Amt");

        for (Map.Entry<String, clsPSPDtl> entry : hmPSPDtl.entrySet()) 
        { 
            clsPSPDtl objPSPDtl=new clsPSPDtl();
            String sql="select dblPurchaseRate from tblitemmaster where strItemCode='"+entry.getKey()+"'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                purchaseRate=rs.getDouble(1);
            }
            rs.close();

            sql="select " + transDay + " from tblmenuitempricingdtl "
                + "where strItemCode='" +entry.getKey()+ "' and strPosCode='"+clsGlobalVarClass.gPOSCode+"'";
            System.out.println(sql);
            rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                saleRate=rs.getDouble(1);
            }
            else
            {
                saleRate=0;
            }

            double dblComStk=entry.getValue().getDblCompStk();
            double dblPhyStk=entry.getValue().getDblPhyStk();
            double dblVar=entry.getValue().getDblVariance();

            if(dblVar>0)
            {
                purchaseRate=purchaseRate*dblVar;
                stkOutTotal=stkOutTotal+purchaseRate;
                Object[] ob={entry.getKey(),entry.getValue().getStrItemName(),dblComStk,dblPhyStk,formt.format(dblVar),formt.format(purchaseRate)};
                dm.addRow(ob);
                objPSPDtl.setDblVairanceAmt(purchaseRate);
            }
            else if(dblVar<0)
            {
                saleRate=saleRate*(-dblVar);
                saleTotal=saleTotal+saleRate;
                Object[] ob={entry.getKey(),entry.getValue().getStrItemName(),dblComStk,dblPhyStk,formt.format(dblVar),formt.format(saleRate)};
                dm.addRow(ob);
                objPSPDtl.setDblVairanceAmt(saleRate);
            }
            else if(dblVar==0)
            {
                dblVar=0;
                saleRate=saleRate*dblVar;
                saleTotal=saleTotal+saleRate;
                Object[] ob={entry.getKey(),entry.getValue().getStrItemName(),dblComStk,dblPhyStk,formt.format(dblVar),formt.format(saleRate)};
                dm.addRow(ob);
                objPSPDtl.setDblVairanceAmt(saleRate);
            }
        }

        lblStkInTotal.setText(formt.format(stkOutTotal));
        lblSaleTotal.setText(formt.format(saleTotal));
        tblItemTable.setModel(dm);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblItemTable.setShowHorizontalLines(true);
        tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblItemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tblItemTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblItemTable.getColumnModel().getColumn(5).setPreferredWidth(60);
    }
    
    
    private void funEnterItemToGrid()
    {
        try
        {
            if(txtItemCode.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Please Select item");
                return;
            }
            else
            {
                textValue2="";
                funAddItemToPhysicalStock();
                funClearFields();
                txtExtCode.requestFocus();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void funAddItemToPhysicalStock() throws Exception
    {
        
        double compQty=0;
        String sql="select intBalance from tblitemcurrentstk where strItemCode='"+itemCode+"' ";
        ResultSet rsStockCal=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsStockCal.next())
        {
            compQty=rsStockCal.getDouble(1);
        }
        rsStockCal.close();
        
        clsPSPDtl objPSPDtl = new clsPSPDtl();
        if(null!=hmPSPDtl.get(itemCode))
        {
            objPSPDtl=hmPSPDtl.get(itemCode);
            double compStk=objPSPDtl.getDblCompStk()+compQty;
            double phyStk=objPSPDtl.getDblPhyStk()+Double.parseDouble(txtQty.getText());
            objPSPDtl.setDblCompStk(compStk);
            objPSPDtl.setDblPhyStk(phyStk);
            double variance=(phyStk-compStk);
            objPSPDtl.setDblVariance(variance);
        }
        else
        {
            objPSPDtl.setStrItemCode(itemCode);
            objPSPDtl.setStrItemName(txtItemCode.getText());
            objPSPDtl.setStrPSPCode("");
            objPSPDtl.setDblCompStk(compQty);
            objPSPDtl.setDblPhyStk(Double.parseDouble(txtQty.getText()));
            double variance=0.00;
//          if(compQty<0)
//            {
//               variance=(compQty+Double.parseDouble(txtQty.getText())); 
//            }
//            else 
//            {
//                variance=(Double.parseDouble(txtQty.getText())-(compQty));
//            }
            variance=(Double.parseDouble(txtQty.getText())-(compQty));
            objPSPDtl.setDblVariance(variance);
            objPSPDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
            objPSPDtl.setStrDataPostFlag("N");
            objPSPDtl.setDblVairanceAmt(0);
        }
        hmPSPDtl.put(itemCode,objPSPDtl);
        itemCode="";
        funFillTable();
    }
    
    
    private void funClearFields()
    {
        txtExtCode.setText("");
        txtItemCode.setText("");
        txtQty.setText("1.00");
        txtQty.selectAll();
        numFlag=false;
    }
    

    
    
    
    /**
     * StockIn or StockOut Text Report
     *
     * @param type
     * @throws Exception
     */
    public void funPhysicalStockReport(String physicalStockCode) throws Exception
    {
        int count = 0;
        String posName="";
        int cnt = 0;
        double grandCompStkTotal = 0.00, quantityTotal = 0.00,grandPhyStkTotal=0.00;
        funCreateTempFolder();
           
        String filePath = System.getProperty("user.dir");
        File file = new File(filePath + "/Temp/Temp_PhysicalStockReport.txt");
        PrintWriter pw = new PrintWriter(file);
        funPrintBlankLines(clsGlobalVarClass.gClientName, pw);
        funPrintBlankLines(clsGlobalVarClass.gClientAddress1, pw);
        if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
        {
            funPrintBlankLines(clsGlobalVarClass.gClientAddress2, pw);
        }
        if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
        {
            funPrintBlankLines(clsGlobalVarClass.gClientAddress3, pw);
        }
        pw.println();
        funPrintBlankLines("Physical Stock Slip", pw);

        String sqlPos = " select b.strPosName from tblpsphd a ,tblposmaster b "
            + " where a.strPSPCode='"+physicalStockCode+"' and a.strPOSCode=b.strPosCode ";

        ResultSet rs= clsGlobalVarClass.dbMysql.executeResultSet(sqlPos);
        if (rs.next())
        {
            posName=rs.getString(1);
        }
        pw.println();
        pw.println();
        pw.println("POS :" + posName);
        String []date=lblStkDate.getText().split("-");
        String phyStkDate=date[2]+"-"+date[1]+"-"+date[0];
        pw.println("Date:" +phyStkDate);
        pw.println("Physical Stock No:" +physicalStockCode);

        pw.println("---------------------------------------");

        pw.println();
        pw.println("Item Name");
        pw.println("    CompStock       PhyStock      Variance");
        pw.println("---------------------------------------");

        StringBuilder sqlPhysicalStockData = new StringBuilder();

        sqlPhysicalStockData.append( " select a.strPSPCode,b.strItemName,a.strItemCode,a.dblCompStk,a.dblPhyStk,"
            + " a.dblVariance,a.dblVairanceAmt "
            + " from  tblPSPdtl a, tblItemMaster b "
            + " where a.strPSPCode='"+physicalStockCode+"' and a.strItemCode=b.strItemCode ");

        ResultSet rsPhysicalStockData = clsGlobalVarClass.dbMysql.executeResultSet(sqlPhysicalStockData.toString());
        while (rsPhysicalStockData.next())
        {
            count++;

            pw.println(rsPhysicalStockData.getString(2));
            funPrintTextWithAlignment("right", rsPhysicalStockData.getString(4), 12, pw); // CompStk
            funPrintTextWithAlignment("right", rsPhysicalStockData.getString(5), 12, pw); // PhyStk
            funPrintTextWithAlignment("right", rsPhysicalStockData.getString(6), 13, pw); // Variance
            pw.println();
            pw.println();

            grandCompStkTotal += Double.parseDouble(rsPhysicalStockData.getString(4));
            grandPhyStkTotal += Double.parseDouble(rsPhysicalStockData.getString(5));
            quantityTotal += Double.parseDouble(rsPhysicalStockData.getString(6));
        }
        rsPhysicalStockData.close();
        pw.println();
        pw.println("---------------------------------------");
        pw.println("GRAND TOTAL");
        pw.println();
     
        funPrintTextWithAlignment("right", String.valueOf(Math.rint(grandCompStkTotal)), 12, pw);
        funPrintTextWithAlignment("right", String.valueOf(Math.rint(grandPhyStkTotal)), 12, pw);
        funPrintTextWithAlignment("right", String.valueOf(Math.rint(quantityTotal)), 12, pw);  
        
       
        pw.println();
        pw.println("---------------------------------------");
        pw.println();
        pw.println();
        pw.println();
        pw.println();
        pw.println();
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
        
        funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort,file.getAbsolutePath());
        if (count > 0)
        {
            funShowTextFile(file, "Physical Stock Report");
        }
    }
    
    
    
    private void funPrintReportToPrinter(String printerName, String fileName) {
        try {
            
            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) {
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                int printerIndex = 0;
                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                for (int i = 0; i < printService.length; i++) {

                    if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName())) {
                        printerIndex = i;
                        break;
                    }
                }
                DocPrintJob job = printService[printerIndex].createPrintJob();
                FileInputStream fis = new FileInputStream(fileName);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
            } else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) {
                Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * create Temp Folder for text report
     */
    private void funCreateTempFolder()
    {
        String filePath = System.getProperty("user.dir");
        File TextKOT = new File(filePath + "/Temp");
        if (!TextKOT.exists())
        {
            TextKOT.mkdirs();
        }
    }
    
    
     /**
     * Print blank Lines
     *
     * @param textToPrint
     * @param pw
     * @return
     */
    private int funPrintBlankLines(String textToPrint, PrintWriter pw)
    {
        pw.println();
        int len = 40 - textToPrint.length();
        len = len / 2;
        for (int cnt = 0; cnt < len; cnt++)
        {
            pw.print(" ");
        }
        pw.print(textToPrint);
        return len;
    }
    
     
        
          /**
     * Print Text With Alignment
     *
     * @param align
     * @param textToPrint
     * @param totalLength
     * @param pw
     * @return
     */
    private int funPrintTextWithAlignment(String align, String textToPrint, int totalLength, PrintWriter pw)
    {
        int len = totalLength - textToPrint.length();
        for (int cnt = 0; cnt < len; cnt++)
        {
            pw.print(" ");
        }

        DecimalFormat decFormat = new DecimalFormat("######.00");
        pw.print(decFormat.format(Double.parseDouble(textToPrint)));
        return 1;
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
            BufferedReader brText = new BufferedReader(fread);

            String line = "";
            while ((line = brText.readLine()) != null)
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
    
    
    
    private void funPopulateButtonClicked() throws Exception
    {
        int result=JOptionPane.showConfirmDialog(this,"Do you want to populate items?");
        if(result==0)
        {
            /*
            String sql="delete from tblitemcurrentstk";
            clsGlobalVarClass.dbMysql.execute(sql);
            sql="insert into tblitemcurrentstk (strItemCode,strItemName) select strItemCode,strItemName from tblitemmaster";
            clsGlobalVarClass.dbMysql.execute(sql);
            StringBuilder sb=new StringBuilder(clsGlobalVarClass.getCurrentDateTime());
            clsGlobalVarClass.proCalculateStock(sb.substring(0,sb.indexOf(" ")).toString(),sb.substring(0,sb.indexOf(" ")).toString(),clsGlobalVarClass.gPOSCode);
            */
            funFillGridWithNonEnteredItems();
        }
    }
    
    
    private void funOpenItemSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("MenuItem");
            new frmSearchFormDialog(this,true).setVisible(true);
            if(clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data=clsGlobalVarClass.gArrListSearchData.toArray();
                funSetItemDetails(data);
                clsGlobalVarClass.gSearchItemClicked=false;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void funUpButtonClicked()
    {
        if (tblItemTable.getModel().getRowCount() > 0)
        {
            int r = tblItemTable.getSelectedRow();
            int rowcount = tblItemTable.getRowCount();
            if (r == -1)
            {
                r = 0;
                String temItemName=tblItemTable.getValueAt(r, 1).toString();
                String temComStk=tblItemTable.getValueAt(r, 2).toString();
                String temPhyStk=tblItemTable.getValueAt(r, 3).toString();
                String temVariance=tblItemTable.getValueAt(r, 4).toString();
                String temVarianceAmt=tblItemTable.getValueAt(r, 5).toString();
                txtItemCode.setText(temItemName);
                //txtPurchaseRate.setText(vPurchaseRate.elementAt(r).toString());
                txtQty.setText(temPhyStk);
                itemCode=tblItemTable.getValueAt(r, 0).toString();
                tblItemTable.changeSelection(r, 0, false, false);

                //System.out.println("UP Row value3="+tblItemTable.getValueAt(r, 0)+"\tItCode="+itemCode);
            }
            else if (r == rowcount)
            {
                r = 0;
                String temItemName=tblItemTable.getValueAt(r, 1).toString();
                String temComStk=tblItemTable.getValueAt(r, 2).toString();
                String temPhyStk=tblItemTable.getValueAt(r, 3).toString();
                String temVariance=tblItemTable.getValueAt(r, 4).toString();
                String temVarianceAmt=tblItemTable.getValueAt(r, 5).toString();
                txtItemCode.setText(temItemName);
               
                txtQty.setText(temPhyStk);
                itemCode=tblItemTable.getValueAt(r, 0).toString();
                tblItemTable.changeSelection(r, 0, false, false);
                //System.out.println("UP Row value3="+tblItemTable.getValueAt(r, 0)+"\tItCode="+itemCode);
            }
            else if (r < rowcount)
            {
                String temItemName=tblItemTable.getValueAt(r-1, 1).toString();
                String temComStk=tblItemTable.getValueAt(r-1, 2).toString();
                String temPhyStk=tblItemTable.getValueAt(r-1, 3).toString();
                String temVariance=tblItemTable.getValueAt(r-1, 4).toString();
                String temVarianceAmt=tblItemTable.getValueAt(r-1, 5).toString();
                txtItemCode.setText(temItemName);
                //txtPurchaseRate.setText(vPurchaseRate.elementAt(r-1).toString());
                txtQty.setText(temPhyStk);
                itemCode=tblItemTable.getValueAt(r-1, 0).toString();
                tblItemTable.changeSelection(r-1, 0, false, false);
                //System.out.println("UP Row value3="+tblItemTable.getValueAt(r-1, 0)+"\tItCode="+itemCode);
            }
        }
        else
        {
            new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
         
        }
    }
    
    
    private void funDownButtonClicked()
    {
        if (tblItemTable.getModel().getRowCount() > 0)
        {
            int r = tblItemTable.getSelectedRow();
            int rowcount = tblItemTable.getRowCount();
            if (r < rowcount)
            {
                tblItemTable.changeSelection(r + 1, 0, false, false);
                String temItemName=tblItemTable.getValueAt(r+1, 1).toString();
                String temQty1=tblItemTable.getValueAt(r+1, 2).toString();
                String temPurchaseRate=tblItemTable.getValueAt(r+1, 3).toString();
                String temSmount=tblItemTable.getValueAt(r+1, 4).toString();
                txtItemCode.setText(temItemName);
                //txtPurchaseRate.setText(temPurchaseRate);
                txtQty.setText(temQty1);
                itemCode=tblItemTable.getValueAt(r, 0).toString();
                //System.out.println("Row value="+tblItemTable.getValueAt(r+1, 0)+"\tItCode="+itemCode);
            }
            else if (r == rowcount)
            {
                r = 0;
                tblItemTable.changeSelection(r, 0, false, false);
                String temItemName=tblItemTable.getValueAt(r, 1).toString();
                String temQty1=tblItemTable.getValueAt(r, 2).toString();
                String temPurchaseRate=tblItemTable.getValueAt(r, 3).toString();
                String temSmount=tblItemTable.getValueAt(r, 4).toString();
                txtItemCode.setText(temItemName);
                //txtPurchaseRate.setText(temPurchaseRate);
                txtQty.setText(temQty1);
                itemCode=tblItemTable.getValueAt(r, 0).toString();
            }
        }
        else
        {
            new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
        }
    }
    
    
    private void funDeleteButtonClicked()
    {
        try
        {
            int r = tblItemTable.getSelectedRow();
            if (r == -1)
            {
                new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
            }
            else
            {
                int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to delete item?", "Item Delete", JOptionPane.YES_NO_OPTION);
                if (ch == JOptionPane.YES_OPTION)
                {
                    for (Map.Entry<String, clsPSPDtl> entry : hmPSPDtl.entrySet()) 
                    {
                        if(entry.getValue().getStrItemName().equalsIgnoreCase(tblItemTable.getValueAt(r, 1).toString()))
                        {
                            hmPSPDtl.remove(entry.getKey());
                            break;
                        }
                    }
                }
                funFillTable();
                funClearFields();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void funHomeButtonClicked()
    {
        boolean closeConn=false;
        try
        {
            int r=tblItemTable.getRowCount();
            if(r>0)
            {
                frmOkCancelPopUp okOb = new frmOkCancelPopUp(this, "Do you want to end transaction");
                okOb.setVisible(true);
                int res = okOb.getResult();
                if (res == 1)
                {
                    clsGlobalVarClass.hmActiveForms.remove("Physical Stock Posting");
                    closeConn=true;
                    dispose();
                }
            }
            else
            {
                clsGlobalVarClass.hmActiveForms.remove("Physical Stock Posting");
                dispose();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            objUtility=null;
            try
            {
                if(closeConn)
                {
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    
          
    private void funExportData()
    {
        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            HSSFRow rowhead = sheet.createRow((short) 1);
            
            CellStyle style = hwb.createCellStyle();
            Font font = hwb.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);

           
            
            rowhead.createCell((short) 0).setCellValue("Item Code");
            rowhead.getCell(0).setCellStyle(style);
            rowhead.createCell((short) 1).setCellValue("SubGroup Name");
            rowhead.getCell(1).setCellStyle(style);
            rowhead.createCell((short) 2).setCellValue("Item Name");
            rowhead.getCell(2).setCellStyle(style);
            rowhead.createCell((short) 3).setCellValue("Computer Stock");
            rowhead.getCell(3).setCellStyle(style);
            rowhead.createCell((short) 4).setCellValue("Physical Stock");
            rowhead.getCell(4).setCellStyle(style);
            
       
            
            String sql = "select a.strItemCode,a.strSubgroupName,a.strItemName,a.intBalance as CompStk "
                    + " from tblitemcurrentstk  a,tblitemmaster b "
                    + " where a.strItemCode=b.strItemCode "
                    + " order by a.strSubgroupName,a.strItemName ";
            System.out.println("sql="+sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            int i = 3;

            while (rs.next())
            {
                HSSFRow row = sheet.createRow((short) i);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                row.createCell((short) 4).setCellValue("");
                i++;
            }

            String filePath = System.getProperty("user.dir");
            
            File file = new File(filePath + "/PhysicalStock.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            hwb.write(fileOut);
            fileOut.close();
            Desktop dt = Desktop.getDesktop();
            dt.open(file);
         
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/CustomerData.xls");
            
            rs.close();
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(this, "File is already opened please close ");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void funBrowseFile()
    {
        try 
        {
            String filePath = System.getProperty("user.dir");
            String fileName=filePath;
            File file = new File(fileName);
           
            if(file.exists())
            {
                String fName="";
                fileName=filePath + "/PhysicalStock.xls";
                file = new File(fileName);
                if(file.exists())
                {
                    JFileChooser jfc = new JFileChooser(fileName);
                    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
                    {
                        File tempFile = jfc.getSelectedFile();
                        String excelFilePath = tempFile.getAbsolutePath();
                        fName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                        System.out.println(fName);
                        selectedFileName = excelFilePath.substring(excelFilePath.lastIndexOf("/") + 1, excelFilePath.length());

                        if(!selectedFileName.isEmpty())
                        {
                            lblFileName.setVisible(true);
                            lblFileName.setText("File Name :");
                            lblExportedFileName.setVisible(true);
                            lblExportedFileName.setText(fName);
                        }

                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Export File for Physical Stock...");
                    return;
                }
                
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
    
    
    public void funReadImportedExcelSheet(String filePath)
    {
       String itemCode="",itemName="";
       double compQty=0,phyQty=0;
       hmPSPDtl= new HashMap<String,clsPSPDtl>();
       try
	{
            if(!selectedFileName.isEmpty())
            {
               FileInputStream file = new FileInputStream(filePath);
               HSSFWorkbook workbook = new HSSFWorkbook(file);
               HSSFSheet worksheet = workbook.getSheetAt(0);
               int i = 3;
               while (i <= worksheet.getLastRowNum()) 
                {
                    clsPSPDtl objPSPDtl = new clsPSPDtl();
                    HSSFRow row = worksheet.getRow(i++);
                    //Sets the Read data to the model class
                    HSSFCell cell = row.getCell(3);

                    if(row.getCell(4)!=null)
                    {
                      if(!row.getCell(4).toString().isEmpty())
                      {
                            itemCode=row.getCell(0).getStringCellValue();
                            itemName=row.getCell(2).getStringCellValue();
                            compQty=Double.valueOf(row.getCell(3).getNumericCellValue());
                            phyQty=row.getCell(4).getNumericCellValue();
                            objPSPDtl.setStrItemCode(itemCode);
                            objPSPDtl.setStrItemName(itemName);
                            objPSPDtl.setStrPSPCode("");
                            objPSPDtl.setDblCompStk(compQty);
                            objPSPDtl.setDblPhyStk(phyQty);
                            double variance=0.00;
//                            if(compQty<0)
//                            {
//                               variance=(compQty+phyQty); 
//                            }
//                            else 
//                            {
//                                variance=(phyQty-compQty);
//                            }
                            variance=(phyQty-compQty);
                            
                            objPSPDtl.setDblVariance(variance);
                            objPSPDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                            objPSPDtl.setStrDataPostFlag("N");
                            objPSPDtl.setDblVairanceAmt(0);
                            hmPSPDtl.put(itemCode,objPSPDtl);
                        }  
                     }
                }
                
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Please Select File To Import...");
                return;
            }
              
        } 
	catch (Exception e) 
        {
           e.printStackTrace();
        }	
     
    }
    
    
   private void funSelectionOfRemarkAndReason()
   {
       
       try
       {
           if (clsGlobalVarClass.gTouchScreenMode)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Reprint Remark.").setVisible(true);
                postingRemarks = clsGlobalVarClass.gKeyboardValue;
            }
            else
            {
                postingRemarks = JOptionPane.showInputDialog(null, "Enter Reprint Remarks");
            }
             
           selectedReasonCode= funLoadAndSelectResons();
           
           System.out.println("postingRemarks :"+postingRemarks );
           System.out.println("selectedReasonCode :"+selectedReasonCode );     
            
        }
       catch(Exception e)
       {
          e.printStackTrace();
       }
       
   }
   
   
   
    private String funLoadAndSelectResons()
    {
        String reasonCode = "";
        try
        {
            int reasoncount=0;
            String sql = "select count(strReasonName) from tblreasonmaster where strPSP='Y' ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            int i = 0;
            while (rs.next())
            {
                reasoncount = rs.getInt(1);
            }
            rs.close();
            
            if (reasoncount > 0)
            {
                Object[] reason = new String[reasoncount];
                sql = "select strReasonName from tblreasonmaster where strPSP='Y' ";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                i = 0;
                while (rs.next())
                {
                    reason[i] = rs.getString(1);
                    i++;
                }
                rs.close();
                
                String selectedReasonDesc = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, reason, reason[0]);
                if (selectedReasonDesc != null)
                {
                    sql = "select strReasonCode from tblreasonmaster where strReasonName='" + selectedReasonDesc + "' and strPSP='Y'";
                    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    while (rs.next())
                    {
                        reasonCode = rs.getString(1);
                    }
                }    
            }  
            
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-9", JOptionPane.ERROR_MESSAGE);
            
        }
        return reasonCode;
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
        };  
        ;
        panelFormBody = new javax.swing.JPanel();
        panelSelectItem = new javax.swing.JPanel();
        lblPhysicalStkNo = new javax.swing.JLabel();
        lblStkDate = new javax.swing.JLabel();
        lblStkNo = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        lblExportedFileName = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        scrTax = new javax.swing.JScrollPane();
        tblTaxTable = new javax.swing.JTable();
        btnMenuItem = new javax.swing.JButton();
        lblItemName = new javax.swing.JLabel();
        panelNumericKeyPad = new javax.swing.JPanel();
        btnCal7 = new javax.swing.JButton();
        btnCal8 = new javax.swing.JButton();
        btnCalClear = new javax.swing.JButton();
        btnCal9 = new javax.swing.JButton();
        btnCal4 = new javax.swing.JButton();
        btnCal5 = new javax.swing.JButton();
        btnCal6 = new javax.swing.JButton();
        btnCal0 = new javax.swing.JButton();
        btnCal1 = new javax.swing.JButton();
        btnCal2 = new javax.swing.JButton();
        btnCal3 = new javax.swing.JButton();
        btnCal00 = new javax.swing.JButton();
        btnCalDot = new javax.swing.JButton();
        btnCalBackSpace = new javax.swing.JButton();
        lblExternalCode = new javax.swing.JLabel();
        txtExtCode = new javax.swing.JTextField();
        btnImportExport = new javax.swing.JButton();
        lblRateQty1 = new javax.swing.JLabel();
        lblFileName = new javax.swing.JLabel();
        cmbOperationType = new javax.swing.JComboBox();
        panelItemDtlGrid = new javax.swing.JPanel();
        scrItemDtl = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblPaxNo = new javax.swing.JLabel();
        lblVariance = new javax.swing.JLabel();
        lblStkInTotal = new javax.swing.JLabel();
        lblSaleTotal = new javax.swing.JLabel();
        panelOperationalButtons = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnDelItem = new javax.swing.JButton();
        btnPopulateItem = new javax.swing.JButton();
        btnModifyStk = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

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
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -  ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Physical Stock");
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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelSelectItem.setBackground(new java.awt.Color(255, 255, 255));
        panelSelectItem.setEnabled(false);
        panelSelectItem.setOpaque(false);
        panelSelectItem.setLayout(null);

        lblPhysicalStkNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        panelSelectItem.add(lblPhysicalStkNo);
        lblPhysicalStkNo.setBounds(110, 10, 131, 30);
        panelSelectItem.add(lblStkDate);
        lblStkDate.setBounds(260, 20, 138, 30);

        lblStkNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStkNo.setText("Physical Stk No.");
        panelSelectItem.add(lblStkNo);
        lblStkNo.setBounds(10, 10, 100, 30);

        txtItemCode.setEnabled(false);
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
        panelSelectItem.add(txtItemCode);
        txtItemCode.setBounds(100, 50, 280, 30);

        lblExportedFileName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panelSelectItem.add(lblExportedFileName);
        lblExportedFileName.setBounds(100, 200, 140, 28);

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setText("1");
        txtQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtQtyMouseClicked(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtQtyKeyPressed(evt);
            }
        });
        panelSelectItem.add(txtQty);
        txtQty.setBounds(100, 160, 90, 28);

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnOK.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOKMouseClicked(evt);
            }
        });
        panelSelectItem.add(btnOK);
        btnOK.setBounds(10, 440, 70, 42);

        tblTaxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Tax Code", "Taxable Amt", "Tax Amt"
            }
        ));
        scrTax.setViewportView(tblTaxTable);

        panelSelectItem.add(scrTax);
        scrTax.setBounds(310, 360, 65, 43);

        btnMenuItem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnMenuItem.setForeground(new java.awt.Color(255, 255, 255));
        btnMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenuItem.setText("MENU ITEM");
        btnMenuItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenuItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenuItemActionPerformed(evt);
            }
        });
        panelSelectItem.add(btnMenuItem);
        btnMenuItem.setBounds(90, 440, 100, 42);

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemName.setText("Item Name");
        panelSelectItem.add(lblItemName);
        lblItemName.setBounds(10, 50, 61, 30);

        panelNumericKeyPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericKeyPad.setOpaque(false);

        btnCal7.setBackground(new java.awt.Color(102, 153, 255));
        btnCal7.setText("7");
        btnCal7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal7MouseClicked(evt);
            }
        });

        btnCal8.setBackground(new java.awt.Color(102, 153, 255));
        btnCal8.setText("8");
        btnCal8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal8MouseClicked(evt);
            }
        });

        btnCalClear.setBackground(new java.awt.Color(102, 153, 255));
        btnCalClear.setText("C");
        btnCalClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalClearMouseClicked(evt);
            }
        });

        btnCal9.setBackground(new java.awt.Color(102, 153, 255));
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
            }
        });

        btnCal4.setBackground(new java.awt.Color(102, 153, 255));
        btnCal4.setText("4");
        btnCal4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal4MouseClicked(evt);
            }
        });

        btnCal5.setBackground(new java.awt.Color(102, 153, 255));
        btnCal5.setText("5");
        btnCal5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal5MouseClicked(evt);
            }
        });

        btnCal6.setBackground(new java.awt.Color(102, 153, 255));
        btnCal6.setText("6");
        btnCal6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal6MouseClicked(evt);
            }
        });

        btnCal0.setBackground(new java.awt.Color(102, 153, 255));
        btnCal0.setText("0");
        btnCal0.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal0MouseClicked(evt);
            }
        });

        btnCal1.setBackground(new java.awt.Color(102, 153, 255));
        btnCal1.setText("1");
        btnCal1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal1MouseClicked(evt);
            }
        });

        btnCal2.setBackground(new java.awt.Color(102, 153, 255));
        btnCal2.setText("2");
        btnCal2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal2MouseClicked(evt);
            }
        });
        btnCal2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCal2ActionPerformed(evt);
            }
        });

        btnCal3.setBackground(new java.awt.Color(102, 153, 255));
        btnCal3.setText("3");
        btnCal3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal3MouseClicked(evt);
            }
        });
        btnCal3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCal3ActionPerformed(evt);
            }
        });

        btnCal00.setBackground(new java.awt.Color(102, 153, 255));
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
            }
        });

        btnCalDot.setBackground(new java.awt.Color(102, 153, 255));
        btnCalDot.setText(".");
        btnCalDot.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalDotMouseClicked(evt);
            }
        });

        btnCalBackSpace.setBackground(new java.awt.Color(102, 153, 255));
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalBackSpaceMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelNumericKeyPadLayout = new javax.swing.GroupLayout(panelNumericKeyPad);
        panelNumericKeyPad.setLayout(panelNumericKeyPadLayout);
        panelNumericKeyPadLayout.setHorizontalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCalClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                                .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCalBackSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 2, Short.MAX_VALUE))
        );
        panelNumericKeyPadLayout.setVerticalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelSelectItem.add(panelNumericKeyPad);
        panelNumericKeyPad.setBounds(10, 240, 240, 190);

        lblExternalCode.setText("External Code");
        panelSelectItem.add(lblExternalCode);
        lblExternalCode.setBounds(10, 110, 80, 30);

        txtExtCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExtCodeMouseClicked(evt);
            }
        });
        txtExtCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExtCodeKeyPressed(evt);
            }
        });
        panelSelectItem.add(txtExtCode);
        txtExtCode.setBounds(100, 110, 160, 30);

        btnImportExport.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnImportExport.setForeground(new java.awt.Color(255, 255, 255));
        btnImportExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnImportExport.setText("Export");
        btnImportExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnImportExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImportExportActionPerformed(evt);
            }
        });
        panelSelectItem.add(btnImportExport);
        btnImportExport.setBounds(310, 440, 80, 40);

        lblRateQty1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRateQty1.setText("Quantity");
        panelSelectItem.add(lblRateQty1);
        lblRateQty1.setBounds(10, 160, 50, 28);

        lblFileName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panelSelectItem.add(lblFileName);
        lblFileName.setBounds(10, 200, 70, 28);

        cmbOperationType.setBackground(new java.awt.Color(51, 102, 255));
        cmbOperationType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbOperationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Export", "Browse", "Import" }));
        cmbOperationType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbOperationTypeMouseClicked(evt);
            }
        });
        cmbOperationType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperationTypeActionPerformed(evt);
            }
        });
        panelSelectItem.add(cmbOperationType);
        cmbOperationType.setBounds(200, 441, 100, 40);

        panelItemDtlGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtlGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtlGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtlGrid.setOpaque(false);
        panelItemDtlGrid.setPreferredSize(new java.awt.Dimension(260, 600));
        panelItemDtlGrid.setLayout(null);

        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ItemCode", "Description", "Comp Stk", "Phy Stk", "Variance", "Variance Amt"
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
        scrItemDtl.setViewportView(tblItemTable);

        panelItemDtlGrid.add(scrItemDtl);
        scrItemDtl.setBounds(0, 0, 400, 450);
        panelItemDtlGrid.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        lblVariance.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblVariance.setText("<html>Variance<br>Value</html> ");
        panelItemDtlGrid.add(lblVariance);
        lblVariance.setBounds(10, 450, 70, 50);
        panelItemDtlGrid.add(lblStkInTotal);
        lblStkInTotal.setBounds(170, 460, 70, 30);

        lblSaleTotal.setBackground(new java.awt.Color(255, 255, 204));
        panelItemDtlGrid.add(lblSaleTotal);
        lblSaleTotal.setBounds(300, 460, 80, 30);

        panelOperationalButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelOperationalButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelOperationalButtons.setOpaque(false);

        btnDone.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDone.setForeground(new java.awt.Color(255, 255, 255));
        btnDone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDone.setText("DONE");
        btnDone.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDone.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDoneActionPerformed(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setText("HOME");
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeActionPerformed(evt);
            }
        });

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnUp.setText("UP");
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
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

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDown.setText("DOWN");
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownMouseClicked(evt);
            }
        });
        btnDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDownActionPerformed(evt);
            }
        });

        btnDelItem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDelItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDelItem.setText("Delete");
        btnDelItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDelItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelItemMouseClicked(evt);
            }
        });
        btnDelItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelItemActionPerformed(evt);
            }
        });

        btnPopulateItem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPopulateItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPopulateItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopulateItem.setText("POPULATE");
        btnPopulateItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopulateItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPopulateItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopulateItemActionPerformed(evt);
            }
        });

        btnModifyStk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnModifyStk.setForeground(new java.awt.Color(255, 255, 255));
        btnModifyStk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnModifyStk.setText("MODIFY PHY STOCK");
        btnModifyStk.setBorder(null);
        btnModifyStk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModifyStk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnModifyStk.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnModifyStkMouseClicked(evt);
            }
        });
        btnModifyStk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModifyStkActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperationalButtonsLayout = new javax.swing.GroupLayout(panelOperationalButtons);
        panelOperationalButtons.setLayout(panelOperationalButtonsLayout);
        panelOperationalButtonsLayout.setHorizontalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModifyStk, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPopulateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 103, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDone, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelOperationalButtonsLayout.setVerticalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationalButtonsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationalButtonsLayout.createSequentialGroup()
                            .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnDone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnPopulateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(1, 1, 1))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnModifyStk, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelSelectItem, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelSelectItem, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
        // TODO add your handling code here:
        funOpenItemSearch();
    }//GEN-LAST:event_txtItemCodeMouseClicked

    private void txtQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtQtyMouseClicked
        // TODO add your handling code here:
        if(numFlag)
        {
            textValue2="";
        }
        numFlag=false;
    }//GEN-LAST:event_txtQtyMouseClicked

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed
        // TODO add your handling code here:
        /*if(evt.getKeyCode()==10)
        {
            if(txtQty.getText().length()==0)
            {
                JOptionPane.showMessageDialog(this,"Quantity cannot be blank");
            }
            else
            {
                textValue2="";
                stkPhyQty=new Double(txtQty.getText());
                funCalculateItemStock(txtItemCode.getText());
                funFillTable();
                clearFields();
                txtExtCode.requestFocus();
            }
        }*/
        if(evt.getKeyCode()==10)
        {
            funEnterItemToGrid();
        }
    }//GEN-LAST:event_txtQtyKeyPressed

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        // TODO add your handling code here:
        funEnterItemToGrid();
    }//GEN-LAST:event_btnOKMouseClicked

    private void btnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuItemActionPerformed
        // TODO add your handling code here:
        //new frmMenuItemMaster().setVisible(true);
    }//GEN-LAST:event_btnMenuItemActionPerformed

    private void btnCal7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal7MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal7.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal7MouseClicked

    private void btnCal8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal8MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal8.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal8MouseClicked

    private void btnCalClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalClearMouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2="";
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnCal9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal9MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal9.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal9MouseClicked

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal4.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal5.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal6.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal0.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal1.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal2.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal2ActionPerformed

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal3.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal3ActionPerformed

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCal00.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCalDotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalDotMouseClicked
        // TODO add your handling code here:
        try
        {
            textValue2=textValue2+btnCalDot.getText();
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCalDotMouseClicked

    private void btnCalBackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalBackSpaceMouseClicked
        // TODO add your handling code here:
        try
        {
            if(textValue2.length()>2)
            {
                StringBuilder sb=new StringBuilder(textValue2);
                sb.delete(textValue2.length()-1, textValue2.length());
                textValue2=sb.toString();
            }
            if(!numFlag)
            {
                txtQty.setText(textValue2);
            }
            else
            {
                //txtPurchaseRate.setText(textValue2);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCalBackSpaceMouseClicked

    private void txtExtCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExtCodeMouseClicked
        // TODO add your handling code here:
        
        if(txtExtCode.getText().length()==0)
        {
            new frmAlfaNumericKeyBoard(this,true,"1","Enter External Code of Item").setVisible(true);
            txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this,true,txtExtCode.getText(),"1","Enter External Code of Item").setVisible(true);
            txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtExtCodeMouseClicked

    private void txtExtCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtCodeKeyPressed
        // TODO add your handling code here:
        try
        {
            System.out.println("Key= "+evt.getKeyCode());
            if(evt.getKeyCode()==10)
            {
                /*
                String exCode=txtExtCode.getText();
                if(exCode.length()==3)
                {
                    StringBuilder sb=new StringBuilder(exCode);
                    sb=sb.insert(2,"0");
                    exCode=sb.insert(3,"0").toString();
                }
                if(exCode.length()==4)
                {
                    StringBuilder sb=new StringBuilder(exCode);
                    exCode=sb.insert(2,"0").toString();
                }
                txtExtCode.setText(exCode);*/
                funSetExternalCode(txtExtCode.getText());
            }
            else if(evt.getKeyCode()==47)
            {
                funOpenItemSearch();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtExtCodeKeyPressed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
        try
        {
            int rowNo=tblItemTable.getSelectedRow();
            String temItemName=tblItemTable.getValueAt(rowNo, 1).toString();
            String temPhyStk=tblItemTable.getValueAt(rowNo, 3).toString();
            txtItemCode.setText(temItemName);
            txtQty.setText(temPhyStk);
            itemCode=tblItemTable.getValueAt(rowNo, 0).toString();
            funSetExternalCode1(itemCode);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpMouseClicked

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
        // TODO add your handling code here:        
    }//GEN-LAST:event_btnDownMouseClicked

    private void btnDelItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelItemMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnDelItemMouseClicked

    private void btnPopulateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopulateItemActionPerformed
        // TODO add your handling code here:
        try
        {
            funPopulateButtonClicked();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPopulateItemActionPerformed

    private void btnModifyStkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyStkActionPerformed
        // TODO add your handling code here:
        funModifyPhyStockButtonClicked();
    }//GEN-LAST:event_btnModifyStkActionPerformed

    private void btnModifyStkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModifyStkMouseClicked
        // TODO add your handling code here:        
    }//GEN-LAST:event_btnModifyStkMouseClicked

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        // TODO add your handling code here:
        funSavePhysicalStk();
    }//GEN-LAST:event_btnDoneActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
        funUpButtonClicked();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        // TODO add your handling code here:
        funDownButtonClicked();
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnDelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelItemActionPerformed
        // TODO add your handling code here:
        funDeleteButtonClicked();
    }//GEN-LAST:event_btnDelItemActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        funHomeButtonClicked();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Physical Stock Posting");
    }//GEN-LAST:event_formWindowClosed

    private void btnImportExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportExportActionPerformed
        // TODO add your handling code here:
        if(btnImportExport.getText().equals("Export"))
        {
            funExportData();
        }
        else if(btnImportExport.getText().equals("Browse"))
        {
            funBrowseFile();
        }
        else
        {
            funReadImportedExcelSheet(selectedFileName);
            try
            {
                funFillTable();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
        
    }//GEN-LAST:event_btnImportExportActionPerformed

    private void cmbOperationTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbOperationTypeMouseClicked
        // TODO add your handling code here:
     
    }//GEN-LAST:event_cmbOperationTypeMouseClicked

    private void cmbOperationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationTypeActionPerformed
        // TODO add your handling code here:
        if (cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("Export"))
        {
            btnImportExport.setText("Export"); 
        }
        else if(cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("Browse"))
        {
            btnImportExport.setText("Browse"); 
        }
        else
        {
             btnImportExport.setText("Import");
        }
    }//GEN-LAST:event_cmbOperationTypeActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCal0;
    private javax.swing.JButton btnCal00;
    private javax.swing.JButton btnCal1;
    private javax.swing.JButton btnCal2;
    private javax.swing.JButton btnCal3;
    private javax.swing.JButton btnCal4;
    private javax.swing.JButton btnCal5;
    private javax.swing.JButton btnCal6;
    private javax.swing.JButton btnCal7;
    private javax.swing.JButton btnCal8;
    private javax.swing.JButton btnCal9;
    private javax.swing.JButton btnCalBackSpace;
    private javax.swing.JButton btnCalClear;
    private javax.swing.JButton btnCalDot;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnImportExport;
    private javax.swing.JButton btnMenuItem;
    private javax.swing.JButton btnModifyStk;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPopulateItem;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox cmbOperationType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblExportedFileName;
    private javax.swing.JLabel lblExternalCode;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPhysicalStkNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRateQty1;
    private javax.swing.JLabel lblSaleTotal;
    private javax.swing.JLabel lblStkDate;
    private javax.swing.JLabel lblStkInTotal;
    private javax.swing.JLabel lblStkNo;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblVariance;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtlGrid;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelNumericKeyPad;
    private javax.swing.JPanel panelOperationalButtons;
    private javax.swing.JPanel panelSelectItem;
    private javax.swing.JScrollPane scrItemDtl;
    private javax.swing.JScrollPane scrTax;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblTaxTable;
    private javax.swing.JTextField txtExtCode;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtQty;
    // End of variables declaration//GEN-END:variables

private void funSetDataPhyStk(Object[] data) {
   
    funAddModifyPhyStockToList(data[0].toString());
        
    }

private void funAddModifyPhyStockToList(String phyStkcode)
    {
        try 
          {
            
            hmPSPDtl.clear();
            lblPhysicalStkNo.setText(phyStkcode);
            String sql="select a.strPSPCode,b.strItemName,a.strItemCode,a.dblCompStk,a.dblPhyStk,a.dblVariance,a.dblVairanceAmt,a.strClientCode,a.strDataPostFlag " 
               + "from  tblPSPdtl a, tblItemMaster b " 
               +"where a.strPSPCode='"+phyStkcode+"' and a.strItemCode=b.strItemCode";
          
            ResultSet rs =clsGlobalVarClass.dbMysql.executeResultSet(sql);
            String temPurBillNo="";
            while (rs.next())
            {
               itemCode= rs.getString(3);
               clsPSPDtl objPSPDtl = new clsPSPDtl();
               if(null!=hmPSPDtl.get(itemCode))
                {
                    objPSPDtl=hmPSPDtl.get(itemCode);
                    objPSPDtl.setDblCompStk(objPSPDtl.getDblCompStk()+rs.getDouble(4));
                    objPSPDtl.setDblPhyStk(objPSPDtl.getDblPhyStk()+rs.getDouble(5));
                    
                }
                else
                {
                  
                  objPSPDtl.setStrItemCode(itemCode);
                  objPSPDtl.setStrItemName(rs.getString(2));
                  objPSPDtl.setStrPSPCode(rs.getString(1));
                  objPSPDtl.setDblCompStk(rs.getDouble(4));
                  objPSPDtl.setDblPhyStk(rs.getDouble(5));
                  objPSPDtl.setDblVariance(rs.getDouble(6));
                  objPSPDtl.setDblVairanceAmt(rs.getDouble(7));
                  objPSPDtl.setStrClientCode(rs.getString(8));
                  objPSPDtl.setStrDataPostFlag(rs.getString(9));
                  
                   
                }
                 hmPSPDtl.put(itemCode,objPSPDtl);
                 itemCode="";
                 
            }            
            funFillTable();
            funClearFields();
           // funFillTableModify();
           
        } catch (Exception e) {
            e.printStackTrace();
           
        }
    }



/*private void funFillTableModify()
    {
        try
        {
            List_itemName.clear();
            List_itemCode.clear();
            List_CompStk.clear();
            List_PhyStkQty.clear();
            List_Varience.clear();
            List_VarienceAmt.clear();
            List_SaleRate.clear();
            String itemCode = null;
            double saleRate=0;
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            double varianceAmt=0,qtyVariance=0;
            dm.addColumn("Description");
            dm.addColumn("CompStk");
            dm.addColumn("PhyStk");
            dm.addColumn("Variance");
            dm.addColumn("Variance Atm");
            double temp_PhyStk=0.00;
            
            for(frmPhysicalStk.clsPhyStockTemp objPhyStk: obj_List_clsPhyStockTemp){                
               String itemName=objPhyStk.getStrItemName();
               List_itemName.add(objPhyStk.getStrItemName());
               List_itemCode.add(objPhyStk.getStrItemCode());
               
               List_CompStk.add(objPhyStk.getdblCompStk());
               List_PhyStkQty.add(objPhyStk.getdblPhyStk());
               List_Varience.add(objPhyStk.getdblVariance());
               qtyVariance+=objPhyStk.getdblVariance();
               List_VarienceAmt.add(objPhyStk.getdblVarianceAmt());
               varianceAmt+=objPhyStk.getdblVarianceAmt();
               if(qtyVariance<0)
               {
                   qtyVariance=qtyVariance*-1;
                   varianceAmt=varianceAmt*-1;
               }
               if(qtyVariance!=0)
               {
               saleRate=varianceAmt/qtyVariance;
               }
               List_SaleRate.add(saleRate);
               
               Object[] row={objPhyStk.getStrItemName(),objPhyStk.getdblCompStk(),objPhyStk.getdblPhyStk(),objPhyStk.getdblVariance(),objPhyStk.getdblVarianceAmt()};
               dm.addRow(row);
            }
            //lblTotalQty.setText(String.valueOf(qtyTotal));
            tblItemTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(65);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(65);
            tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
*/

    private void funModifyPhyStockButtonClicked()
    {
        try
        {
            objUtility.funCallForSearchForm("PhysicalStock");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetDataPhyStk(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
    
    

