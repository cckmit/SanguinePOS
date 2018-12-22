/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.apache.xmlbeans.impl.jam.JComment;

public class frmCustomerHistory extends javax.swing.JFrame {

 

  
        private List<clsDirectBillerItemDtl> obj_List_ItemDtl;
        private List<clsMakeKotItemDtl> obj_List_KOT_ItemDtl;
        private DefaultTableModel dm;
        private Object[] records;
        frmDirectBiller obDirectBiller;
        frmMakeKOT obMakeKot;
        Map<String,String> itemMap=new HashMap<String, String>();
        
        private String strFormName;
        
        frmCustomerHistory(frmDirectBiller objDirectBiller) 
        {
             initComponents();
             obDirectBiller=objDirectBiller;
             obj_List_ItemDtl = obDirectBiller.getObj_List_ItemDtl();
             dm =(DefaultTableModel) tblCustHist.getModel();
             lblformName.setText(clsGlobalVarClass.gCustomerName);
            strFormName="frmDirectBiller";
             
      //   lblformName.setText(clsGlobalVarClass.gCustomerName+"         "+ clsGlobalVarClass.gCustMBNo);
            funSetDate();
            funGetCustomerHistory();
          
        }
         frmCustomerHistory(frmMakeKOT objMakeKot) 
        {
             initComponents();
             obMakeKot=objMakeKot;
             obj_List_KOT_ItemDtl = obMakeKot.getObj_List_KOT_ItemDtl();
             strFormName="frmMakeKOT";
             dm =(DefaultTableModel) tblCustHist.getModel();
             lblformName.setText(clsGlobalVarClass.gCustomerName);

      //   lblformName.setText(clsGlobalVarClass.gCustomerName+"         "+ clsGlobalVarClass.gCustMBNo);
            funSetDate();
            funGetCustomerHistory();
          
        }
        
    public void funSetDate()
    {
       Date date=null,fromdate=null;
         try {
             date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, -3);
                fromdate = cal.getTime();
             
         } catch (ParseException ex) {
             Logger.getLogger(frmCustomerHistory.class.getName()).log(Level.SEVERE, null, ex);
         }
             dteFromDate.setDate(fromdate);
             dteToDate.setDate(date);
    }
    
    public void funGetCustomerHistory()
    {
        
         String fromDate = funGetCalenderDate(dteFromDate.getDate());
         String toDate = funGetCalenderDate(dteToDate.getDate());
         dm.setRowCount(0);
          StringBuilder sbSqlLiveBill=new StringBuilder();
          StringBuilder sbSqlQFileBill=new StringBuilder();
          StringBuilder sbSql=new StringBuilder();
          String strBillNo="";
           records = new Object[4];
         try{
               sbSqlLiveBill.setLength(0);
               sbSqlQFileBill.setLength(0);
               sbSql.setLength(0);
               double totalBillAmt=0;
                sbSqlLiveBill.append("select a.strBillNo,date(a.dteBillDate),Time(a.dteBillDate),a.dblGrandTotal,b.strItemName,"
                        + "b.dblQuantity,b.dblAmount,b.strItemCode\n from tblbillhd a,tblbilldtl b "
                        + "where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode \n" +                          
                            "and a.strCustomerCode='"+clsGlobalVarClass.gCustomerCode+"' \n" +
                            "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' ORDER BY date(a.dteBillDate) DESC");
                
                sbSqlQFileBill.append("select a.strBillNo,date(a.dteBillDate),Time(a.dteBillDate),a.dblGrandTotal,b.strItemName,"
                        + "b.dblQuantity,b.dblAmount,b.strItemCode\n from tblqbillhd a,tblqbilldtl b "
                        + "where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode \n" +                           
                            "and a.strCustomerCode='"+clsGlobalVarClass.gCustomerCode+"' \n" +
                            "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' ORDER BY date(a.dteBillDate) DESC");
               
                ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveBill.toString());
                Date d1;
                 String Qty="";
                 
                ResultSet rs=null;
                String BillNo="";
                String ModQty="1";
                String[]  strTime=new String[3];
                //clsDirectBillerItemDtl obItemDtl = new clsDirectBillerItemDtl();
                     while(rsCustomer.next())
                    {    
                       // totalBillAmt  =+ rsCustomer.getDouble(7);
                        //If bill is repeat 
                         if(!strBillNo.equals(rsCustomer.getString(1)))
                      {  
                          String[] dateArr =new String[3];
                          dateArr=rsCustomer.getString(2).split("-");//for date
                          
                           d1 = new SimpleDateFormat("dd-MM-yyyy").parse(rsCustomer.getString(2));
                           BillNo= "<html><font color=blue>" + rsCustomer.getString(1)
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                +  dateArr[2]+"-"+dateArr[1]+"-"+dateArr[0].substring(2, 4) + "</font></html>";
                            
                           strTime=rsCustomer.getString(3).split(":");
                           
                         records[0]=BillNo;
                         records[1]=strTime[0]+":"+strTime[1];
                         records[2]=rsCustomer.getString(4);
                         records[3]=null;
                         //tblCustHist.getModel().removeTableModelListener();
                        
                         //for Adding 1st Item in row
                         dm.addRow(records);
                         
                         itemMap.put(rsCustomer.getString(5).toString(), rsCustomer.getString(8).toString());
                            Qty=rsCustomer.getString(6).split("\\.")[0];
                            
                           records[0]=rsCustomer.getString(5);
                           records[1]=Qty;
                           records[2]=rsCustomer.getString(7);
                           records[3]=false;
                           dm.addRow(records);
                           
                      String sql="select a.strModifierName,a.dblRate,a.dblQuantity,a.dblAmount,a.strItemCode"
                                 + " from tblbillmodifierdtl a where a.strBillNo='"+rsCustomer.getString(1)+"' and a.strItemCode like '"+rsCustomer.getString(8)+"%'";
                                ResultSet rset=clsGlobalVarClass.dbMysql.executeResultSet(sql);  //and a.strItemCode like 'I000918%';
                                while(rset.next())
                                {
                                       itemMap.put(rset.getString(1).toString(), rset.getString(5).toString());
                                       ModQty=rset.getString(3).split("\\.")[0];
                                       records[0]=rset.getString(1);
                                       records[1]=ModQty;
                                       records[2]=rset.getString(4);
                                       records[3]=false;
                                       dm.addRow(records);
                                }
                       
                      }
                      else{
                             //For Adding Items in Same Bill
                            itemMap.put(rsCustomer.getString(5).toString(), rsCustomer.getString(8).toString());
                            Qty=rsCustomer.getString(6).split("\\.")[0];
                            
                           records[0]=rsCustomer.getString(5);
                           records[1]=Qty;
                           records[2]=rsCustomer.getString(7);
                           records[3]=false;
                           dm.addRow(records);
                           
                             String sql="select a.strModifierName,a.dblRate,a.dblQuantity,a.dblAmount,a.strItemCode"
                                 + " from tblbillmodifierdtl a where a.strBillNo='"+rsCustomer.getString(1)+"' and a.strItemCode like '"+rsCustomer.getString(8)+"%'";
                              ResultSet rset=clsGlobalVarClass.dbMysql.executeResultSet(sql);  //and a.strItemCode like 'I000918%';
                                while(rset.next())
                                {
                                       itemMap.put(rset.getString(1).toString(), rset.getString(5).toString());
                                       ModQty=rset.getString(3).split("\\.")[0];
                                       records[0]=rset.getString(1);
                                       records[1]=ModQty;
                                       records[2]=rset.getString(4);
                                       records[3]=false;
                                       dm.addRow(records);
                                }
                       }
                        strBillNo=rsCustomer.getString(1);
                    }
                    rsCustomer.close();
                    rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFileBill.toString());
                     while(rsCustomer.next())
                    {    
                       // totalBillAmt  =+ rsCustomer.getDouble(7);
                         if(!strBillNo.equals(rsCustomer.getString(1)))
                      {  
                           String[] dateArr =new String[3];
                          dateArr=rsCustomer.getString(2).split("-");//for date
                          
                           d1 = new SimpleDateFormat("dd-MM-yyyy").parse(rsCustomer.getString(2));
                            BillNo= "<html><font color=blue>" + rsCustomer.getString(1)
                                +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + dateArr[2]+"-"+dateArr[1]+"-"+dateArr[0].substring(2, 4) + "</font></html>";
                                
                                 strTime=rsCustomer.getString(3).split(":");   
                                 records[0]=BillNo;
                                 records[1]=strTime[0]+":"+strTime[1];
                                 records[2]=rsCustomer.getString(4);
                                 records[3]=null;
                                 dm.addRow(records);
                                
                                   itemMap.put(rsCustomer.getString(5).toString(), rsCustomer.getString(8).toString());
                                   Qty=rsCustomer.getString(6).split("\\.")[0];
                                   records[0]=rsCustomer.getString(5);
                                   records[1]=Qty;
                                   records[2]=rsCustomer.getString(7);
                                   records[3]=false;
                                   dm.addRow(records);
                                    String sql="select a.strModifierName,a.dblRate,a.dblQuantity,a.dblAmount,a.strItemCode"
                                        + " from tblqbillmodifierdtl a where a.strBillNo='"+rsCustomer.getString(1)+"' and a.strItemCode like '"+rsCustomer.getString(8)+"%'";
                                       ResultSet rset=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                                       while(rset.next())
                                       {
                                            itemMap.put(rset.getString(1).toString(), rset.getString(5).toString());
                                             ModQty=rset.getString(3).split("\\.")[0];
                                              records[0]=rset.getString(1);
                                              records[1]=ModQty;
                                              records[2]=rset.getString(4);
                                              records[3]=false;
                                              dm.addRow(records);
                                       }
                      
                      }
                      else{
                                itemMap.put(rsCustomer.getString(5).toString(), rsCustomer.getString(8).toString());
                                Qty=rsCustomer.getString(6).split("\\.")[0];
                                records[0]=rsCustomer.getString(5);
                                records[1]=Qty;
                                records[2]=rsCustomer.getString(7);
                                records[3]=false;
                                dm.addRow(records);
                           
                            String sql="select a.strModifierName,a.dblRate,a.dblQuantity,a.dblAmount,a.strItemCode"
                                 + " from tblqbillmodifierdtl a where a.strBillNo='"+rsCustomer.getString(1)+"' and a.strItemCode like '"+rsCustomer.getString(8)+"%'";
                                ResultSet rset=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                                while(rset.next())
                                {
                                       itemMap.put(rset.getString(1).toString(), rset.getString(5).toString());
                                       ModQty=rset.getString(3).split("\\.")[0];
                                       records[0]=rset.getString(1);
                                       records[1]=ModQty;
                                       records[2]=rset.getString(4);
                                       records[3]=false;
                                       dm.addRow(records);
                                }
                       }
                        strBillNo=rsCustomer.getString(1);
                    }
                    rsCustomer.close();
                   
                    
                    
                    
                    tblCustHist.setModel(dm);
            
                    
                    
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblCustHist.setShowHorizontalLines(true);
            tblCustHist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblCustHist.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblCustHist.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblCustHist.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblCustHist.getColumnModel().getColumn(0).setPreferredWidth(312);
            tblCustHist.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblCustHist.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblCustHist.getColumnModel().getColumn(3).setPreferredWidth(55);
            
            funSetUpCheckBoxColumn(tblCustHist.getColumnModel().getColumn(3));
            
           
         //   funBillRow();
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    private void funSetUpCheckBoxColumn(TableColumn column) {
        
                DefaultTableCellRenderer renderer;
            renderer = new DefaultTableCellRenderer() {
                
                // This is called by the table when a cell needs rendering:
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus,int row, int column)
                {
                    
                  
                        Object col1Value = table.getValueAt(row, 3);
                    if (col1Value == null || col1Value.toString().length()==0) 
                    {
                        JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        comp.setText(null);
                        return comp;
                       
                    }
                    
                    return table.getDefaultRenderer(table.getColumnClass(column)).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                }
                
              
            };
                
    // set our new custom renderer into the column
    column.setCellRenderer(renderer);
 }
 
      public void funGetSelectedItemForMakeKOT()    
      {
        try{
            double dblprice=0;
            String itemName="",itemCode="";
            double qty1=0,amt=0,rate=0;
            boolean check;
            boolean flag = false;
            int pax=1;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
        
            for(int i=0;i<tblCustHist.getRowCount();i++)
            {
                if(tblCustHist.getValueAt(i, 1).toString().length()>2)
                 {
                    tblCustHist.setGridColor(Color.green);
                 } 
                else 
                {
                check=(boolean) tblCustHist.getValueAt(i, 3);

                if(check==true)
                {
                    itemName=tblCustHist.getValueAt(i, 0).toString();
                    itemCode=itemMap.get(itemName);
                    qty1=Double.parseDouble(tblCustHist.getValueAt(i, 1).toString());
                    amt=Double.parseDouble(tblCustHist.getValueAt(i, 2).toString());
                    rate=amt/qty1;
                    clsMakeKotItemDtl obMakeKotItemDtl;
                  
                    flag = false;
                    if (obj_List_KOT_ItemDtl.size() > 0 )
                    {    
                          for (clsMakeKotItemDtl listItemDtl : obj_List_KOT_ItemDtl)
                          {
                                String tempItemCode = listItemDtl.getItemCode();
                                if (tempItemCode.equalsIgnoreCase(itemCode) && listItemDtl.isIsModifier() == false && "N".equalsIgnoreCase(listItemDtl.getTdhComboItemYN()))
                                {
                                    double prevQty = listItemDtl.getQty();
                                    double finalQty = prevQty + qty1;

                                    if (!clsGlobalVarClass.gNegBilling)
                                    {
                                        if (!clsGlobalVarClass.funCheckNegativeStock(tempItemCode, finalQty))
                                        {
                                            return;
                                        }
                                    }
                                    listItemDtl.setAmt(rate * finalQty);
                                    listItemDtl.setQty(prevQty + qty1);
                                    flag = true;
                                    
                                }
                                if (flag)
                                {
                                    break;
                                }
                          }
                          

                    }
                    if (!flag)
                    {
                        if (!clsGlobalVarClass.gNegBilling)
                        {
                            if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, qty1))
                            {
                                return;
                            }
                        }
                        if(itemName.startsWith("-->"))
                        {
                            double srNo=Double.parseDouble(obMakeKot.getStrSerialNo())+0.01-1;
                            clsMakeKotItemDtl ob = new clsMakeKotItemDtl(String.valueOf(decimalFormat.format(srNo)), obMakeKot.KOTNo, obMakeKot.globalTableNo, obMakeKot.globalWaiterNo, itemName, itemCode, qty1, (qty1 * rate), pax, "N", "N", true, "", "", "", "N", amt);
                            obj_List_KOT_ItemDtl.add(ob);
                        }
                        else
                        {
                            clsMakeKotItemDtl ob = new clsMakeKotItemDtl(obMakeKot.getStrSerialNo(), obMakeKot.KOTNo, obMakeKot.globalTableNo, obMakeKot.globalWaiterNo, itemName, itemCode, qty1, (qty1 * rate), pax, "N", "N", false, "", "", "", "N", amt);
                            obMakeKot.kotItemSequenceNO++;
                            obj_List_KOT_ItemDtl.add(ob);
                        }
                    }
                    obMakeKot.flagOpenItem = false;
                    obMakeKot.funRefreshItemTable();

                  }
                        
               }
            }  
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
      }
        
      public void funGetSelectedItemForDirectBiller()
     {
         int serNo=obj_List_ItemDtl.size()+1;
         double dblprice=0;
         String itemName="",itemCode="";
         double dblQty=0,amt=0,rate=0;
         boolean check;
          boolean flag = false;
           int modifierSeqNo = 0;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            for(int i=0;i<tblCustHist.getRowCount();i++)
            {
                if(tblCustHist.getValueAt(i, 1).toString().length()>2)
                 {
                    tblCustHist.setGridColor(Color.green);
                 } 
                else 
                {
                    check=(boolean) tblCustHist.getValueAt(i, 3);
                    
                    if(check==true)
                    {
                        itemName=tblCustHist.getValueAt(i, 0).toString();
                        itemCode=itemMap.get(itemName);
                        dblQty=Double.parseDouble(tblCustHist.getValueAt(i, 1).toString());
                        amt=Double.parseDouble(tblCustHist.getValueAt(i, 2).toString());
                        rate=amt/dblQty;
                        clsDirectBillerItemDtl obDirectBill;
                       //  clsDirectBillerItemDtl ob=new clsDirectBillerItemDtl() ;
                         serNo++;
                         flag = false;
                        if (obj_List_ItemDtl.size() > 0 )
                        {       
                            for (clsDirectBillerItemDtl list_cls_ItemRow : obj_List_ItemDtl)
                            {
                                String temp_itemCode = list_cls_ItemRow.getItemCode();
                                if (temp_itemCode.equalsIgnoreCase(itemCode) && list_cls_ItemRow.isIsModifier() == false && "N".equalsIgnoreCase(list_cls_ItemRow.getTdhComboItemYN()))
                                {
                                    double temp_qty = list_cls_ItemRow.getQty();
                                    double final_qty = temp_qty + 1;
                                    double amount = Math.rint(rate * final_qty);
                                    list_cls_ItemRow.setRate(rate);
                                    list_cls_ItemRow.setAmt(amount);
                                    list_cls_ItemRow.setQty(final_qty);

                                    flag = true;
                                }
                                 if (flag){
                                        break;
                                    }
                            }
                        }
                        if (!flag)
                        {
                            if(itemName.startsWith("-->"))
                            {
                                double srNo=Double.parseDouble(obDirectBiller.getSeqNo());
                                srNo=(srNo==0) ? 1:srNo;
                                srNo=srNo+0.01-1;
                                clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(itemName, itemMap.get(itemName), dblQty, amt, true, "", "N", "", rate, "", String.valueOf(decimalFormat.format(srNo)),0);
                                obj_List_ItemDtl.add(ob);
                            }
                            else
                            {
                                clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(itemName, itemMap.get(itemName), dblQty, amt, false, "", "N", "", rate, "", obDirectBiller.getSeqNo(),0);
                                obj_List_ItemDtl.add(ob);
                            }
                        }
                              obDirectBiller.funRefreshItemTable();
                    }
                        
               }
            }
     }
        
    public void funSelectedRow()
    {
     // int rowNo = tblCustHist.getSelectedRow();
       
         if(!(tblCustHist.getValueAt(tblCustHist.getSelectedRow(), 3)== null))
                      {
                         boolean check=(boolean) tblCustHist.getValueAt(tblCustHist.getSelectedRow(), 3);
                         if(check)
                         {
                             tblCustHist.setValueAt(!check,tblCustHist.getSelectedRow() , 3);
                         }
                         else{
                             tblCustHist.setValueAt(!check,tblCustHist.getSelectedRow() , 3);
                         }

                      }
//     tblCustHist.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//        public void valueChanged(ListSelectionEvent event) {
//            // do some actions here, for example
//            // print first column value from selected row
//            System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
//        }
//    });
    
 }
      
    //Get date into yyyy-mm-dd format
     private String funGetCalenderDate(Date objDate)
       {
            return (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getDate());
       }
     
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLayout = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        lblPosName = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        panelBody = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        dteFromDate = new com.toedter.calendar.JDateChooser();
        dteToDate = new com.toedter.calendar.JDateChooser();
        btnExecute = new javax.swing.JButton();
        pnlSalesData = new javax.swing.JScrollPane();
        tblCustHist = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(520, 470));

        javax.swing.GroupLayout panelLayoutLayout = new javax.swing.GroupLayout(panelLayout);
        panelLayout.setLayout(panelLayoutLayout);
        panelLayoutLayout.setHorizontalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayoutLayout.setVerticalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(2147483600, 2147483647));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(625, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setPreferredSize(new java.awt.Dimension(580, 30));
        panelHeader.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelHeaderMouseDragged(evt);
            }
        });

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Customer History");

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblPosNameMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addComponent(lblProductName)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblModuleName)
                    .addComponent(lblformName))
                .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(136, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(lblProductName))
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblModuleName))
                    .addComponent(lblformName)))
            .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setAutoscrolls(true);
        panelBody.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelBody.setMinimumSize(new java.awt.Dimension(520, 470));
        panelBody.setName(""); // NOI18N
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(630, 484));

        dteFromDate.setToolTipText("Select From Date");
        dteFromDate.setPreferredSize(new java.awt.Dimension(119, 35));
        dteFromDate.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                dteFromDateHierarchyChanged(evt);
            }
        });
        dteFromDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dteFromDatePropertyChange(evt);
            }
        });

        dteToDate.setToolTipText("Select From Date");
        dteToDate.setPreferredSize(new java.awt.Dimension(119, 35));
        dteToDate.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                dteToDateHierarchyChanged(evt);
            }
        });
        dteToDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dteToDatePropertyChange(evt);
            }
        });

        btnExecute.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExecute.setForeground(new java.awt.Color(255, 255, 255));
        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnExecute.setText("EXECUTE");
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnExecute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExecuteMouseClicked(evt);
            }
        });
        btnExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecuteActionPerformed(evt);
            }
        });

        tblCustHist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "Qty", "Amount", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblCustHist.setFillsViewportHeight(true);
        tblCustHist.setRowHeight(25);
        tblCustHist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustHistMouseClicked(evt);
            }
        });
        pnlSalesData.setViewportView(tblCustHist);
        if (tblCustHist.getColumnModel().getColumnCount() > 0) {
            tblCustHist.getColumnModel().getColumn(3).setResizable(false);
        }

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlSalesData, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addComponent(pnlSalesData, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         
    }//GEN-LAST:event_formWindowClosing

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
       
        if(strFormName.equals("frmMakeKOT"))
        {
            funGetSelectedItemForMakeKOT();
            obMakeKot.setEnabled(true);
            dispose();
        }
        else{
             funGetSelectedItemForDirectBiller();
            obDirectBiller.setEnabled(true);
            dispose();
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        // TODO add your handling code here:
         if(strFormName.equals("frmMakeKOT"))
        {
            funGetSelectedItemForMakeKOT();
            obMakeKot.setEnabled(true);
            dispose();
        }
        else{
             funGetSelectedItemForDirectBiller();
            obDirectBiller.setEnabled(true);
            dispose();
        }
    }//GEN-LAST:event_btnOKMouseClicked

    private void tblCustHistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustHistMouseClicked
            
            funSelectedRow();
    }//GEN-LAST:event_tblCustHistMouseClicked

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
        // TODO add your handling code here:
        funGetCustomerHistory();
        //dispose();
    }//GEN-LAST:event_btnExecuteActionPerformed

    private void btnExecuteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExecuteMouseClicked
        // TODO add your handling code here:
        funGetCustomerHistory();
    }//GEN-LAST:event_btnExecuteMouseClicked

    private void dteToDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dteToDatePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dteToDatePropertyChange

    private void dteToDateHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_dteToDateHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_dteToDateHierarchyChanged

    private void dteFromDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dteFromDatePropertyChange

    }//GEN-LAST:event_dteFromDatePropertyChange

    private void dteFromDateHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_dteFromDateHierarchyChanged

    }//GEN-LAST:event_dteFromDateHierarchyChanged

    private void lblPosNameMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPosNameMouseDragged
        // TODO add your handling code here:
//        int x=evt.getXOnScreen();
//        int y=evt.getYOnScreen();
//        this.setLocation(x, y);
    }//GEN-LAST:event_lblPosNameMouseDragged

    private void panelHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelHeaderMouseDragged
        // TODO add your handling code here:
         int x=evt.getXOnScreen();
        int y=evt.getYOnScreen();
        this.setLocation(x, y);
    }//GEN-LAST:event_panelHeaderMouseDragged
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnOK;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane pnlSalesData;
    private javax.swing.JTable tblCustHist;
    // End of variables declaration//GEN-END:variables



//    private void funResetFields()
//    {
//         try {
//             java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
//             dteFromDate.setDate(date);
//             dteToDate.setDate(date);
//         } catch (ParseException ex) {
//             Logger.getLogger(frmCustomerHistory.class.getName()).log(Level.SEVERE, null, ex);
//         }
//        
//    }

}