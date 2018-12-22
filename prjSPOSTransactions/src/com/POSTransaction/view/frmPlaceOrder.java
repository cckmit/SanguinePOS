
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.gSanguineWebServiceURL;
import com.POSGlobal.controller.clsPlaceOrderDtl;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSynchronizePOSDataToHO;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSPrinting.Utility.clsPrintingUtility;
import com.POSTransaction.controller.clsOrderDtl;
import com.POSTransaction.controller.clsSpecialOrderItemCharDtl;
import com.POSTransaction.controller.clsSpecialOrderItemDtl;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class frmPlaceOrder extends javax.swing.JFrame 
{
    clsUtility objUtility;
    String dtefullFillment = null,orderDate="";
    private Map hmOrder;
    private int hiddenColumnCount;
    Map<String,List<clsOrderDtl>> hmSubGroupOrderDtl;
    Map<String,String> hmItemDtl;
    private long orderUpToTime;
    Map<String,String> hmClosedItems,hmSubGroup;
    
    public frmPlaceOrder(){
    
        initComponents();
        hiddenColumnCount=0;
        objUtility=new clsUtility();
        hmSubGroupOrderDtl=new HashMap<String,List<clsOrderDtl>>();
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        lblItemCode.setVisible(false);
        lblGeneralOrderDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblAdvanceOrderDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblUrgentOrderDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        hmSubGroup=new LinkedHashMap<String,String>();
        
        try
        {
            String date=clsGlobalVarClass.gPOSDateToDisplay;
            Date dt=new Date(Integer.parseInt(date.split("-")[2])-1900,Integer.parseInt(date.split("-")[1])-1,Integer.parseInt(date.split("-")[0]));
            clsGlobalVarClass.funCalculateStock(dt, dt, "All", "Both", "Stock");

            funLoadHmOrder();
            funFillNormalOrderGrid();
            funFillAdvanceOrderGridItems();
            funFillUrgentOrderGridItems();
            funFillGroupComboBox();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    private int funFillNormalOrderGrid() throws Exception
    {
        hmClosedItems=new HashMap<String,String>();
        String sql="select strItemCode from tblmenuitempricingdtl "
            + " where date(dteToDate) < '"+clsGlobalVarClass.gPOSDateForTransaction+"' ";
        ResultSet rsClosedItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsClosedItems.next())
        {
            hmClosedItems.put(rsClosedItems.getString(1),rsClosedItems.getString(1));
        }
        rsClosedItems.close();
        
        DefaultTableModel dm=(DefaultTableModel)tblPlaceOrderItems.getModel();
        String []postDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        hmItemDtl=new HashMap<String,String>();
        int count=1;
        
        String sqlOrder="select c.strExternalCode,c.strItemName,b.strProductCode,sum(b.dblStockQty) "
            + " ,sum(b.dblQty),a.strOrderCode,a.strSOCode,date(a.dteOrderDate),f.strGroupCode,f.strSubGroupCode "
            + " ,b.strItemCode,c.intDeliveryDays "
            + " from tblplaceorderhd a,tblplaceorderdtl b,tblitemmaster c,tblitemcurrentstk e,tblsubgrouphd f "
            + " where a.strOrderCode=b.strOrderCode and b.strItemCode=c.strItemCode and c.strItemCode=e.strItemCode  "
            + " and c.strSubGroupCode=f.strSubGroupCode and a.strOrderType='Normal' and a.strCloseSO='N' "
            + " and a.strSOCode='' and a.strOrderTypeCode='"+hmOrder.get(cmbOrderType.getSelectedItem().toString()) +"' "
            + " and date(a.dteSODate)='"+postDate[0]+"' "
            + " group by b.strItemCode "
            + " order by a.dteOrderDate,f.strSubGroupName ";
        //System.out.println("sqlOrder"+sqlOrder);
        ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sqlOrder);
        while(rsItems.next())
        {
            hmItemDtl.put(rsItems.getString(11),rsItems.getString(11));
            Object[] arrObjRows={count,rsItems.getString(1),rsItems.getString(2),rsItems.getString(4),rsItems.getString(5),rsItems.getString(8),rsItems.getString(6),rsItems.getString(7),rsItems.getString(3),rsItems.getString(9),rsItems.getString(10),rsItems.getString(11),rsItems.getString(12)};
            dm.addRow(arrObjRows);
            count++;
        }
        rsItems.close();
        tblPlaceOrderItems.setModel(dm);
        tblPlaceOrderItems.setRowHeight(15);
        
        return 1;
    }
    
    
    
    private int funFillNormalOrderGridForLastOrder() throws Exception
    {
        DefaultTableModel dm=(DefaultTableModel)tblPlaceOrderItems.getModel();
        dm.setRowCount(0);
        String []postDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        hmItemDtl=new HashMap<String,String>();
        int count=1;
        String date=postDate[0];
        Date dt=new Date(Integer.parseInt(date.split("-")[0])-1900,Integer.parseInt(date.split("-")[1])-1,Integer.parseInt(date.split("-")[2]));
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DATE, -7);
        String lastOrderDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
        
        String sqlOrder="select c.strExternalCode,c.strItemName,b.strProductCode,sum(b.dblStockQty), "
            + " sum(b.dblQty),a.strOrderCode,a.strSOCode,date(a.dteOrderDate),f.strGroupCode,f.strSubGroupCode"
            + " ,b.strItemCode,c.intDeliveryDays,c.strNoDeliveryDays "
            + " from tblplaceorderhd a,tblplaceorderdtl b,tblitemmaster c,tblitemcurrentstk e,tblsubgrouphd f  "
            + " where a.strOrderCode=b.strOrderCode and b.strItemCode=c.strItemCode and c.strItemCode=e.strItemCode  "
            + " and c.strSubGroupCode=f.strSubGroupCode and a.strOrderType='Normal' and date(a.dteOrderDate)='"+lastOrderDate+"' "
            + " and a.strOrderTypeCode='"+hmOrder.get(cmbOrderType.getSelectedItem().toString()) +"' "
            + " group by b.strItemCode "
            + " order by a.dteOrderDate,f.strSubGroupName ";
        //System.out.println("sqlOrder"+sqlOrder);
        ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sqlOrder);
        while(rsItems.next())
        {
            String orderDelDate=funGetOrderDelDateForNormalOrder(rsItems.getInt(12),rsItems.getString(13));
            hmItemDtl.put(rsItems.getString(11),rsItems.getString(11));
            Object[] arrObjRows={count,rsItems.getString(1),rsItems.getString(2),rsItems.getString(4),rsItems.getString(5),orderDelDate,rsItems.getString(6),rsItems.getString(7),rsItems.getString(3),rsItems.getString(9),rsItems.getString(10),rsItems.getString(11),rsItems.getString(12)};
            dm.addRow(arrObjRows);
            count++;
        }
        rsItems.close();
        tblPlaceOrderItems.setModel(dm);
        tblPlaceOrderItems.setRowHeight(15);
        
        return 1;
    }
    
    
    
    private void funFillGroupComboBox() throws Exception
    {
        cmbGroup.removeAllItems();
        String sql="select strGroupCode,strGroupName,strOperationalYN "
            + " from tblgrouphd "
            + " where strOperationalYN='Y' and strGroupCOde in (select strGroupCode from tblsubgrouphd) "
            + " order by strGroupName ";
        ResultSet rsGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsGroup.next())
        {
            cmbGroup.addItem(rsGroup.getString(2)+"                                                                      "+rsGroup.getString(1)); 
        }
        rsGroup.close();
        
        hmSubGroup.clear();
        sql="select strSubGroupCode,strSubGroupName "
            + " from tblsubgrouphd order by strSubGroupName ";
        ResultSet rsSubGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsSubGroup.next())
        {
            hmSubGroup.put(rsSubGroup.getString(1),rsSubGroup.getString(2));
        }
        rsSubGroup.close();
    }
    
    
    
    private void funFillSubGroupComboBox(String groupCode) throws Exception
    {
        cmbSubGroup.removeAllItems();
        String sql="select strSubGroupCode,strSubGroupName from tblsubgrouphd "
            + " where strGroupCode='"+groupCode+"' order by strSubGroupName";
        ResultSet rsSubGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsSubGroup.next())
        {
            sql="select strItemCode from tblitemmaster where strSubGroupCode='"+rsSubGroup.getString(1)+"' ";
            ResultSet rsSubGroupCodeCount=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsSubGroupCodeCount.next())
            {
                cmbSubGroup.addItem(rsSubGroup.getString(2)+"                                                                       "+rsSubGroup.getString(1));
            }
            rsSubGroupCodeCount.close();
        }
        rsSubGroup.close();
        if(cmbSubGroup.getItemCount()>0)
        {
            funCalculateTotalQty();
        }
    }
    
    
    
    private void funCreateTempFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/Temp");
            if (!file.exists())
            {
                file.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void funGenerateLinkupTextfile(List<String>arrUnLinkedItemDtl)
    {   
        clsUtility objUtility=new clsUtility();
        try
        {
            funCreateTempFolder();
            String filePath=System.getProperty("user.dir");
            filePath+="/Temp/Temp_ItemUnLinkedItems.txt";
            File textFile=new File(filePath);
            PrintWriter pw=new PrintWriter(textFile);
            pw.println(objUtility.funPrintTextWithAlignment(" UnLinked Items ",40,"Center"));
            pw.println(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName,40,"Center"));
            pw.println(" ");
            pw.println("________________________________________");
	    pw.print(objUtility.funPrintTextWithAlignment("ItemName",25,"Left"));
            pw.println(" ");
            pw.println("________________________________________");
            pw.println(" ");
            
            if(arrUnLinkedItemDtl.size()>0)
            {
                for(int cnt=0;cnt<arrUnLinkedItemDtl.size();cnt++)
                {
                    String items=arrUnLinkedItemDtl.get(cnt);
                    pw.print(objUtility.funPrintTextWithAlignment(""+items+" ",25,"Left"));
                    pw.println(" ");
                }
            }

            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println("m");
            
            pw.flush();
            pw.close();
          
            clsPrintingUtility objPrintingUtility=new clsPrintingUtility();
            if (clsGlobalVarClass.gShowBill) 
            {
                objPrintingUtility.funShowTextFile(textFile,"","");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    private String funGetOrderDelDateForNormalOrder(int requiredDays,String noDeliverDays)
    {
        String orderDeliveryDate="";
        
        String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        String[] arrSpDate=posDate[0].split("-");
        Date dtNextDate=new Date(Integer.parseInt(arrSpDate[0])-1900,Integer.parseInt(arrSpDate[1])-1,Integer.parseInt(arrSpDate[2]));
        String calDate=funGetDate(dtNextDate, requiredDays);
        String[] sp=calDate.split("-");
        dtNextDate=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));

        //int dayCount=Integer.parseInt(cmbDeliveryDays.getSelectedItem().toString());
        int dayCount=0;
        String dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
        String[] arrSpNoDelDays=noDeliverDays.split(",");
        List<String> arrListDays=new ArrayList<String>();

        for(int cnt=0;cnt<arrSpNoDelDays.length;cnt++)
        {
            arrListDays.add(arrSpNoDelDays[cnt]);
        }

        for(int cnt=0;cnt<arrListDays.size();cnt++)
        {
            if(arrListDays.contains(dayOfWeek))
            {
                String tempDate=funGetDate(dtNextDate, 1);
                String[] sp1=tempDate.split("-");
                dtNextDate=new Date(Integer.parseInt(sp1[0])-1900,Integer.parseInt(sp1[1])-1,Integer.parseInt(sp1[2]));
                dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
                dayCount++;
            }
        }

        requiredDays+=dayCount;
        Date dt=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DATE, dayCount);
        if((cal.getTime().getMonth()+1)<=9)
        {
            if((cal.getTime().getDate())<=9)
            {
                orderDeliveryDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
            }
            else
            {
                orderDeliveryDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
            }
        }
        else
        {
            if((cal.getTime().getDate())<=9)
            {
                orderDeliveryDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
            }
            else
            {
                orderDeliveryDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
            }
        }
        
        return orderDeliveryDate;
    }
    
    
    
    private void funFillPOSWSItems()
    {
        List<String> arrListUnlinkedItems=new ArrayList<String>();
        String socode1="",orderCode="";
        DefaultTableModel dm=(DefaultTableModel)tblPlaceOrderItems.getModel();
        int count=tblPlaceOrderItems.getRowCount();
       
        boolean flagFound=false;
        //dm.setRowCount(0);
        try
        {
            String groupCode=cmbGroup.getSelectedItem().toString();
            groupCode=groupCode.substring(groupCode.length()-8, groupCode.length());
            String subGroupCode=cmbSubGroup.getSelectedItem().toString();
            subGroupCode=subGroupCode.substring(subGroupCode.length()-9, subGroupCode.length());
            
            if(arrListUnlinkedItems.size()>0)
            {
                funGenerateLinkupTextfile(arrListUnlinkedItems);
            }
                   
        //Query for Normal Order
            String sql=" select e.strExternalCode,a.strItemName ,ifnull(b.strWSProductCode,''),a.intBalance,e.intDeliveryDays,"
                + " e.strNoDeliveryDays,f.strGroupCode,f.strSubGroupCode,a.strItemCode "
                + " from tblitemcurrentstk a left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode "
                + " left outer join tblitemorderingdtl c on b.strItemCode=c.strItemCode "
                + " left outer join tblordermaster d on c.strOrderCode=d.strOrderCode "
                + " left outer join tblitemmaster e on a.strItemCode=e.strItemCode "
                + " left outer join tblsubgrouphd f on e.strSubGroupCode=f.strSubGroupCode "
                + " where d.strOrderCode='"+hmOrder.get(cmbOrderType.getSelectedItem().toString()) +"' "
                + " and e.strSubGroupCode='"+subGroupCode+"' "
                + " group by a.strItemName order by a.strItemName ";
            //System.out.println("NormalOrderQuery:" +sql);
            ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                       
            if(hmSubGroupOrderDtl.size()>0)
            {
                for(Map.Entry<String,List<clsOrderDtl>> entry:hmSubGroupOrderDtl.entrySet())
                {
                   if(entry.getKey().equals(subGroupCode))
                    {
                        flagFound=true;
                    }
                }
            }
            
            if(flagFound)
            {
                if(tblPlaceOrderItems.getRowCount()>0)
                {
                    DefaultTableModel dmPlaceNormalOrder = (DefaultTableModel) tblPlaceOrderItems.getModel();
                    for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
                    {
                        if(!tblPlaceOrderItems.getValueAt(cnt, 10).toString().equals(subGroupCode))
                        {
                            if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())==0)
                            {
                                dmPlaceNormalOrder.removeRow(cnt);
                                cnt--;
                            }
                        }
                    } 
                }
                 
                while(rsItems.next())
                {
                    if(!hmClosedItems.containsKey(rsItems.getString(9)))
                    {
                        if(!hmItemDtl.containsKey(rsItems.getString(9)))
                        {
                            boolean flgItemSelect=true;
                            if(rsItems.getString(3).isEmpty())
                            {
                                arrListUnlinkedItems.add(rsItems.getString(2));
                            }
                            else
                            {                        
                                int requiredDays=rsItems.getInt(5);
                                String noDeliverDays=rsItems.getString(6);
                                orderDate=funGetOrderDelDateForNormalOrder(requiredDays,noDeliverDays);
                                
                                /*String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                                String[] arrSpDate=posDate[0].split("-");
                                Date dtNextDate=new Date(Integer.parseInt(arrSpDate[0])-1900,Integer.parseInt(arrSpDate[1])-1,Integer.parseInt(arrSpDate[2]));
                                String calDate=funGetDate(dtNextDate, requiredDays);
                                String[] sp=calDate.split("-");
                                dtNextDate=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));

                                //int dayCount=Integer.parseInt(cmbDeliveryDays.getSelectedItem().toString());
                                int dayCount=0;
                                String dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
                                String[] arrSpNoDelDays=noDeliverDays.split(",");
                                List<String> arrListDays=new ArrayList<String>();

                                for(int cnt=0;cnt<arrSpNoDelDays.length;cnt++)
                                {
                                    arrListDays.add(arrSpNoDelDays[cnt]);
                                }

                                for(int cnt=0;cnt<arrListDays.size();cnt++)
                                {
                                    if(arrListDays.contains(dayOfWeek))
                                    {
                                        String tempDate=funGetDate(dtNextDate, 1);
                                        String[] sp1=tempDate.split("-");
                                        dtNextDate=new Date(Integer.parseInt(sp1[0])-1900,Integer.parseInt(sp1[1])-1,Integer.parseInt(sp1[2]));
                                        dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
                                        dayCount++;
                                    }
                                }

                                requiredDays+=dayCount;
                                Date dt=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));
                                GregorianCalendar cal = new GregorianCalendar();
                                cal.setTime(dt);
                                cal.add(Calendar.DATE, dayCount);
                                if((cal.getTime().getMonth()+1)<=9)
                                {
                                    if((cal.getTime().getDate())<=9)
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
                                    }
                                    else
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
                                    }
                                }
                                else
                                {
                                    if((cal.getTime().getDate())<=9)
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
                                    }
                                    else
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
                                    }
                                }
                                */
                                
                                double stock=rsItems.getDouble(4);
                                for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
                                {
                                    if(tblPlaceOrderItems.getValueAt(cnt,11).toString().equals(rsItems.getString(9)))
                                    {
                                        flgItemSelect=false;
                                    }
                                }
                                if(flgItemSelect)
                                {
                                    count++;
                                    Object[] arrObjRows={count,rsItems.getString(1),rsItems.getString(2),stock,0,orderDate,orderCode,socode1,rsItems.getString(3),rsItems.getString(7),rsItems.getString(8),rsItems.getString(9),rsItems.getString(5)};
                                    dm.addRow(arrObjRows);
                                }
                            }
                        }
                    }
                }
                rsItems.close();
            }
           
            if(!flagFound)
            {
                if(tblPlaceOrderItems.getRowCount()>0)
                {
                    DefaultTableModel dmPlaceNormalOrder = (DefaultTableModel) tblPlaceOrderItems.getModel();
                    for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
                    {
                        if(!tblPlaceOrderItems.getValueAt(cnt, 10).toString().equals(subGroupCode))
                        {
                            if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())==0)
                            {
                                dmPlaceNormalOrder.removeRow(cnt);
                                cnt--;
                            }
                        }
                    }
                }
                count=count-1;
                
                while(rsItems.next())
                {
                    if(!hmClosedItems.containsKey(rsItems.getString(9)))
                    {
                        String iCode=rsItems.getString(9);
                        if(!hmItemDtl.containsKey(iCode))
                        {
                            count++;
                            if(rsItems.getString(3).isEmpty())
                            {
                                arrListUnlinkedItems.add(rsItems.getString(2));
                            }
                            else
                            {
                                int requiredDays=rsItems.getInt(5);
                                String noDeliverDays=rsItems.getString(6);
                                String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                                String[] arrSpDate=posDate[0].split("-");
                                Date dtNextDate=new Date(Integer.parseInt(arrSpDate[0])-1900,Integer.parseInt(arrSpDate[1])-1,Integer.parseInt(arrSpDate[2]));
                                String calDate=funGetDate(dtNextDate, requiredDays);
                                String[] sp=calDate.split("-");
                                dtNextDate=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));

                                //int dayCount=Integer.parseInt(cmbDeliveryDays.getSelectedItem().toString());
                                int dayCount=0;
                                String dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
                                String[] arrSpNoDelDays=noDeliverDays.split(",");
                                List<String> arrListDays=new ArrayList<String>();

                                for(int cnt=0;cnt<arrSpNoDelDays.length;cnt++)
                                {
                                    arrListDays.add(arrSpNoDelDays[cnt]);
                                }

                                for(int cnt=0;cnt<arrListDays.size();cnt++)
                                {
                                    if(arrListDays.contains(dayOfWeek))
                                    {
                                        String tempDate=funGetDate(dtNextDate, 1);
                                        String[] sp1=tempDate.split("-");
                                        dtNextDate=new Date(Integer.parseInt(sp1[0])-1900,Integer.parseInt(sp1[1])-1,Integer.parseInt(sp1[2]));
                                        dayOfWeek=funGetDayOfWeek(dtNextDate.getDay());
                                        dayCount++;
                                    }
                                }

                                requiredDays+=dayCount;
                                Date dt=new Date(Integer.parseInt(sp[0])-1900,Integer.parseInt(sp[1])-1,Integer.parseInt(sp[2]));
                                GregorianCalendar cal = new GregorianCalendar();
                                cal.setTime(dt);
                                cal.add(Calendar.DATE, dayCount);
                                if((cal.getTime().getMonth()+1)<=9)
                                {
                                    if((cal.getTime().getDate())<=9)
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
                                    }
                                    else
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
                                    }
                                }
                                else
                                {
                                    if((cal.getTime().getDate())<=9)
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate());
                                    }
                                    else
                                    {
                                        orderDate=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
                                    }
                                }
                                double stock=rsItems.getDouble(4);
                                Object[] arrObjRows={count,rsItems.getString(1),rsItems.getString(2),stock,0,orderDate,orderCode,socode1,rsItems.getString(3),rsItems.getString(7),rsItems.getString(8),rsItems.getString(9),rsItems.getString(5)};
                                dm.addRow(arrObjRows);
                            }
                        }
                    }
                }
                rsItems.close();
            }
           
                        
            for(int n=0;n<tblPlaceOrderItems.getRowCount();n++)   
            {
                tblPlaceOrderItems.setValueAt((n+1), n, 0);
                   
                String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                String[] spOrderDate=posDate[0].split("-");
                Date dtFulfillmentDate=new Date(Integer.parseInt(spOrderDate[0])-1900,Integer.parseInt(spOrderDate[1])-1,Integer.parseInt(spOrderDate[2]));
                int dayCount=Integer.parseInt(cmbDeliveryDays.getSelectedItem().toString());
                dayCount+=Integer.parseInt(tblPlaceOrderItems.getValueAt(n, 12).toString());
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dtFulfillmentDate);
                cal.add(Calendar.DATE, dayCount);
                if((cal.getTime().getMonth()+1)<=9)
                {
                    if((cal.getTime().getDate())<=9)
                    {
                        tblPlaceOrderItems.setValueAt((cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate()),n,5);
                    }
                    else
                    {
                        tblPlaceOrderItems.setValueAt((cal.getTime().getYear()+1900)+"-"+"0"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate()),n,5);
                    }
                }
                else
                {
                    if((cal.getTime().getDate())<=9)
                    {
                        tblPlaceOrderItems.setValueAt((cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+"0"+(cal.getTime().getDate()),n,5);
                    }
                    else
                    {
                        tblPlaceOrderItems.setValueAt((cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate()),n,5);
                    }
                }
            }
            
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblPlaceOrderItems.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblPlaceOrderItems.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblPlaceOrderItems.setRowHeight(15);
            double totalNormalOrderQty=0;
            String rowSubGroupCode="",rowGroupCode="";
            double totalSubGropQty=0,totalGroupQty=0;
            
            for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
            {
                if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
                {
                    double qty=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    totalNormalOrderQty+=qty;
                    rowGroupCode=tblPlaceOrderItems.getValueAt(cnt, 9).toString();
                    rowSubGroupCode=tblPlaceOrderItems.getValueAt(cnt, 10).toString();
                    if(subGroupCode.equals(rowSubGroupCode))
                    {
                        totalSubGropQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                    if(groupCode.equals(rowGroupCode))
                    {
                        totalGroupQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                }
            }
            lblTotalQty.setText(String.valueOf(totalNormalOrderQty));
            lblTotalSubGroupQty.setText(String.valueOf(totalSubGropQty));
            lblTotalGroupQty.setText(String.valueOf(totalGroupQty));
            List<clsOrderDtl> arrListOrderDtl=new ArrayList<clsOrderDtl>();
            
            for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
            {
                clsOrderDtl objOrderDtl=null;
                rowSubGroupCode=tblPlaceOrderItems.getValueAt(cnt, 10).toString();
                if(subGroupCode.equals(rowSubGroupCode))
                {
                    objOrderDtl=new clsOrderDtl();
                    objOrderDtl.setItemCode(tblPlaceOrderItems.getValueAt(cnt, 10).toString());
                    objOrderDtl.setQty(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString()));
                }

                if(hmSubGroupOrderDtl.containsKey(subGroupCode))
                {
                    arrListOrderDtl=hmSubGroupOrderDtl.get(subGroupCode);
                    if(objOrderDtl!=null)
                    {
                        arrListOrderDtl.add(objOrderDtl);
                    }
                }
                else
                {
                    if(objOrderDtl!=null)
                    {
                        arrListOrderDtl=new ArrayList<clsOrderDtl>();
                        arrListOrderDtl.add(objOrderDtl);
                    }
                }
                if(arrListOrderDtl.size()>0)
                {
                    hmSubGroupOrderDtl.put(subGroupCode, arrListOrderDtl);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    private void funFillPOSWSItems(String itemCode)
    {
        DefaultTableModel dm=(DefaultTableModel)tblPlaceOrderItems.getModel();
        try
        {
            String sql="select a.strItemCode ,a.strItemName ,ifnull(b.strWSProductCode,''), a.strSubGroupName"
                + ", a.strGroupName,a.intBalance "
                + " from tblitemcurrentstk a left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode "
                + " where a.strItemCode='"+itemCode+"' "
                + " order by a.strItemName ";
            ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsItems.next())
            {
                if(rsItems.getString(3).isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "This POS Item Is Not Linked with MMS Product!!!");
                }
                else
                {
                    double stock=0;
                    stock=rsItems.getDouble(6);
                    Object[] arrObjRows={rsItems.getString(5),rsItems.getString(4),rsItems.getString(2),stock,0,rsItems.getString(1),rsItems.getString(3)};
                    dm.addRow(arrObjRows);
                }
            }
            rsItems.close();
            lblItemCode.setText("");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
   //Fill Advance Order Items to table tblPlaceAdvanceOrderItems 
    private void funFillAdvanceOrderGridItems()
    {        
        List<String> arrListUnlinkedItems=new ArrayList<String>();
        int cnt=0,count=0,totalColumnSize=0;
        String SOCode1="",orderCode="";
        DefaultTableModel dm=(DefaultTableModel)tblPlaceAdvanceOrderItems.getModel();
        dm.setRowCount(0);
        dm.addColumn("Sr.No");
        dm.addColumn("ItemCode");
        dm.addColumn("ItemName");
        dm.addColumn("Order Qty");
        dm.addColumn("Delivery Date");
      
        try
        {
            int charCount=dm.getColumnCount();
            Map<String,Integer> hmCharValues=new HashMap<String,Integer>();
            String sqlCharName="select strCharName from tblcharactersticsmaster ";
            ResultSet rsCharCount=clsGlobalVarClass.dbMysql.executeResultSet(sqlCharName);
            while(rsCharCount.next())
            {
                count++;
                dm.addColumn(rsCharCount.getString(1).toUpperCase());
                hmCharValues.put(rsCharCount.getString(1).toUpperCase(),charCount);
                charCount++;
            }
            rsCharCount.close();
            hiddenColumnCount=count;

            dm.addColumn("Customer Name");
            dm.addColumn("Phone No");
            dm.addColumn("Order Code");
            dm.addColumn("SOCode");
            dm.addColumn("AdvOrderNo");
            dm.addColumn("WSProdCode");
            dm.addColumn("Weight");
            dm.addColumn("Stock Qty");
                       
            double totalAdvOrderQty=0;
            String sql=" select b.strItemCode ,b.strItemName,f.intBalance,date(a.dteOrderFor ),"
                + " a.strAdvBookingNo,ifnull(g.strWSProductCode,''),b.dblWeight,b.dblQuantity,h.strCustomerName,h.longMobileNo  "
                + " from tbladvbookbillhd a left outer join tbladvbookbilldtl b on a.strAdvBookingNo=b.strAdvBookingNo "
                + " left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
                + " left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
                + " left outer join tblgrouphd e on d.strGroupCode=e.strGroupCode "
                + " left outer join tblitemcurrentstk f on b.strItemCode=f.strItemCode "
                + " left outer join tblitemmasterlinkupdtl g on b.strItemCode=g.strItemCode "
                + " left outer join tblcustomermaster h on b.strCustomerCode=h.strCustomerCode "
                + " where a.strAdvBookingNo NOT IN (select strAdvOrderNo from tblplaceorderadvorderdtl "
                + " where strOrderType='Advance') "
                + " and a.strUrgentOrder='N' "
                + " order by a.dteOrderFor  ";
            System.out.println("AdvOrderQuery:" +sql);
            ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsItems.next())
            {
                cnt++;
                if(rsItems.getString(6).isEmpty())
                {
                    arrListUnlinkedItems.add(rsItems.getString(2));
                }
                else
                {
                    double stock=0;
                    double orderQty=rsItems.getDouble(8);
                    stock=rsItems.getDouble(3);
                    totalAdvOrderQty+=orderQty;
                    Object[] arrObjRows=
                    {
                        cnt,rsItems.getString(1),rsItems.getString(2),orderQty,rsItems.getString(4)
                    };
                    ArrayList<Object> arrCharList = new ArrayList<Object>(Arrays.asList(arrObjRows));
                    for(int cn=0;cn<hiddenColumnCount;cn++)
                    {
                        arrCharList.add("");
                    }
                   
                    String sqlChar=" select b.strCharName,a.strCharValues "
                        + " from tbladvbookbillchardtl a,tblcharactersticsmaster b "
                        + " where a.strCharCode=b.strCharCode and a.strAdvBookingNo='"+rsItems.getString(5)+"' "
                        + " and a.strItemCode='"+rsItems.getString(1)+"' ";
                    ResultSet rsChar=clsGlobalVarClass.dbMysql.executeResultSet(sqlChar);
                    while(rsChar.next())
                    {
                        if(hmCharValues.containsKey(rsChar.getString(1).toUpperCase()))
                        {
                            arrCharList.set(hmCharValues.get(rsChar.getString(1).toUpperCase()), rsChar.getString(2));
                        }
                    }
                    rsChar.close();
                   
                    arrCharList.add(rsItems.getString(9));
                    arrCharList.add(rsItems.getString(10));
                    arrCharList.add(orderCode);
                    arrCharList.add(SOCode1);
                    arrCharList.add(rsItems.getString(5));
                    arrCharList.add(rsItems.getString(6));
                    arrCharList.add(rsItems.getString(7));
                    arrCharList.add(stock);
                    totalColumnSize=arrCharList.size();
                    dm.addRow(arrCharList.toArray());
                }
            }
            rsItems.close();
            lblTotalAdvOrderQty.setText(String.valueOf(totalAdvOrderQty));
            if(arrListUnlinkedItems.size()>0)
            {
                funGenerateLinkupTextfile(arrListUnlinkedItems);
            }
             
            //tblPlaceAdvanceOrderItems.setModel(dm);
            tblPlaceAdvanceOrderItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(4).setPreferredWidth(120);
            if(count>0)
            {
                for(int i=0;i<count;i++)
                {
                    tblPlaceAdvanceOrderItems.getColumnModel().getColumn(i).setPreferredWidth(70);
                }
            }
            hiddenColumnCount=4+count;
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(hiddenColumnCount+1).setPreferredWidth(120);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(hiddenColumnCount+2).setPreferredWidth(100);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            funHideAdvanceOrderColumn(hiddenColumnCount+3,totalColumnSize);
            tblPlaceAdvanceOrderItems.setRowHeight(20);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 
    }
    
    
    //Fill Urgent Order Items to table tblPlaceUrgentOrderItems 
    private void funFillUrgentOrderGridItems()
    {
        List<String> arrListUnlinkedItems=new ArrayList<String>();
        int cnt=0,count=0,totalColumnSize=0;
        String SOCode1="",orderCode="";
        DefaultTableModel dm=(DefaultTableModel)tblPlaceUrgentOrderItems.getModel();
        dm.setRowCount(0);
        dm.addColumn("Sr.No");
        dm.addColumn("ItemCode");
        dm.addColumn("ItemName");
        dm.addColumn("Order Qty");
        dm.addColumn("Delivery Date");
      
        try
        {
            int charCount=dm.getColumnCount();
            Map<String,Integer> hmCharValues=new HashMap<String,Integer>();
            String sqlCharName="select strCharName from tblcharactersticsmaster";
            ResultSet rsCharCount=clsGlobalVarClass.dbMysql.executeResultSet(sqlCharName);
            while(rsCharCount.next())
            {
                count++;
                dm.addColumn(rsCharCount.getString(1).toUpperCase());
                hmCharValues.put(rsCharCount.getString(1).toUpperCase(),charCount);
                charCount++;
            }
            rsCharCount.close();
            hiddenColumnCount=count;

            dm.addColumn("Customer Name");
            dm.addColumn("Phone No");
            dm.addColumn("Order Code");
            dm.addColumn("SOCode");
            dm.addColumn("AdvOrderNo");
            dm.addColumn("WSProdCode");
            dm.addColumn("Weight");
            dm.addColumn("Stock Qty");
                       
            double totalUrgentOrderQty=0;
            String sql=" select b.strItemCode ,b.strItemName,f.intBalance,date(a.dteOrderFor ),"
                + " a.strAdvBookingNo,ifnull(g.strWSProductCode,''),b.dblWeight,b.dblQuantity,h.strCustomerName,h.longMobileNo  "
                + " from tbladvbookbillhd a left outer join tbladvbookbilldtl b on a.strAdvBookingNo=b.strAdvBookingNo "
                + " left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
                + " left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
                + " left outer join tblgrouphd e on d.strGroupCode=e.strGroupCode "
                + " left outer join tblitemcurrentstk f on b.strItemCode=f.strItemCode "
                + " left outer join tblitemmasterlinkupdtl g on b.strItemCode=g.strItemCode "
                + " left outer join tblcustomermaster h on b.strCustomerCode=h.strCustomerCode "
                + " where a.strAdvBookingNo NOT IN (select strAdvOrderNo from tblplaceorderadvorderdtl "
                + " where strOrderType='Urgent') "
                + " and a.strUrgentOrder='Y' "
                + " order by a.dteOrderFor  ";
            //System.out.println("AdvOrderQuery:" +sql);
            ResultSet rsItems=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsItems.next())
            {
                cnt++;
                if(rsItems.getString(6).isEmpty())
                {
                    arrListUnlinkedItems.add(rsItems.getString(2));
                }
                else
                {
                    double stock=0;
                    double orderQty=rsItems.getDouble(8);
                    stock=rsItems.getDouble(3);
                    totalUrgentOrderQty+=orderQty;
                    Object[] arrObjRows=
                    {
                        cnt,rsItems.getString(1),rsItems.getString(2),orderQty,rsItems.getString(4)
                    };
                    ArrayList<Object> arrCharList = new ArrayList<Object>(Arrays.asList(arrObjRows));
                    for(int cn=0;cn<hiddenColumnCount;cn++)
                    {
                        arrCharList.add("");
                    }
                   
                    String sqlChar=" select b.strCharName,a.strCharValues "
                        + " from tbladvbookbillchardtl a,tblcharactersticsmaster b "
                        + " where a.strCharCode=b.strCharCode and a.strAdvBookingNo='"+rsItems.getString(5)+"' ";
                    ResultSet rsChar=clsGlobalVarClass.dbMysql.executeResultSet(sqlChar);
                    while(rsChar.next())
                    {
                        if(hmCharValues.containsKey(rsChar.getString(1).toUpperCase()))
                        {
                            arrCharList.set(hmCharValues.get(rsChar.getString(1).toUpperCase()), rsChar.getString(2));
                        }
                    }
                    rsChar.close();
                   
                    arrCharList.add(rsItems.getString(9));
                    arrCharList.add(rsItems.getString(10));
                    arrCharList.add(orderCode);
                    arrCharList.add(SOCode1);
                    arrCharList.add(rsItems.getString(5));
                    arrCharList.add(rsItems.getString(6));
                    arrCharList.add(rsItems.getString(7));
                    arrCharList.add(stock);
                    totalColumnSize=arrCharList.size();
                    dm.addRow(arrCharList.toArray());
                }
            }
            rsItems.close();
            lblTotalUrgentOrderQty.setText(String.valueOf(totalUrgentOrderQty));
            if(arrListUnlinkedItems.size()>0)
            {
                funGenerateLinkupTextfile(arrListUnlinkedItems);
            }
             
            //tblPlaceAdvanceOrderItems.setModel(dm);
            tblPlaceUrgentOrderItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(4).setPreferredWidth(120);
            if(count>0)
            {
                for(int i=0;i<count;i++)
                {
                    tblPlaceUrgentOrderItems.getColumnModel().getColumn(i).setPreferredWidth(70);
                }
            }
            hiddenColumnCount=4+count;
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(hiddenColumnCount+1).setPreferredWidth(120);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(hiddenColumnCount+2).setPreferredWidth(100);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            funHideUrgentOrderColumn(hiddenColumnCount+3,totalColumnSize);
            tblPlaceUrgentOrderItems.setRowHeight(20);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 
    }
    
    

    
//Function to go to home after clicking on home button
    private void funHomeButtonClicked()
    {
        clsGlobalVarClass.hmActiveForms.remove("Place Order");
        dispose();
    }
    
    private void funResetFields()
    {
        DefaultTableModel dm=(DefaultTableModel)tblPlaceOrderItems.getModel();
        dm.setRowCount(0);
        tblPlaceOrderItems.setModel(dm);
        lblTotalQty.setText("0");
        lblTotalSubGroupQty.setText("0");
        lblTotalGroupQty.setText("0");
        hmSubGroupOrderDtl.clear();
    }
    
    private void funResetAdvanceOrderFields()
    {
        DefaultTableModel dm=(DefaultTableModel)tblPlaceAdvanceOrderItems.getModel();
        dm.setRowCount(0);
        tblPlaceAdvanceOrderItems.setModel(dm);
        lblTotalAdvOrderQty.setText("0");
    }
    
    private void funResetUrgentOrderFields()
    {
        DefaultTableModel dm=(DefaultTableModel)tblPlaceUrgentOrderItems.getModel();
        dm.setRowCount(0);
        tblPlaceUrgentOrderItems.setModel(dm);
        lblTotalUrgentOrderQty.setText("0");
    }
    
    
    private void funHideAdvanceOrderColumn(int columnCount,int totalColumnSize)
    {
        int i=columnCount;
        for(i=columnCount;i<totalColumnSize;i++)
        {
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(i).setMinWidth(0);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(i).setMaxWidth(0);
            tblPlaceAdvanceOrderItems.getColumnModel().getColumn(i).setWidth(0);
        }
    }
    
    private void funHideUrgentOrderColumn(int columnCount,int totalColumnSize)
    {
        int i=columnCount;
        for(i=columnCount;i<totalColumnSize;i++)
        {
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(i).setMinWidth(0);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(i).setMaxWidth(0);
            tblPlaceUrgentOrderItems.getColumnModel().getColumn(i).setWidth(0);
        }
    }
    
   
    
    private String funGenerateOrderCode()throws Exception
    {
        long lastNo = 0;
        String orderCode="";
        /*String sql = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='PlaceOrder'";
        ResultSet rsOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsOrderCode.next()) {
            lastNo = rsOrderCode.getLong(2);
            lastNo = lastNo + 1;
            orderCode = "OC" + String.format("%07d", lastNo);
            sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='PlaceOrder'";
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        rsOrderCode.close();*/
        
        String sql="select right(max(strOrderCode),7) from tblplaceorderhd where strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
        ResultSet rsOrderCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsOrderCode.next())
        {
            lastNo=rsOrderCode.getLong(1);
        }
        rsOrderCode.close();
        
        lastNo = lastNo + 1;
        orderCode = "OC" + String.format("%07d", lastNo);
        sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='PlaceOrder'";
        clsGlobalVarClass.dbMysql.execute(sql);
        
        return orderCode;
    }
    
    
//Place Normal Order
    private void funPlaceOrder() throws Exception
    {
        String locCode="";
        int orderCount=0,count=1;
        Map<String,String> hmOrderCode=null;
        String sql="select strWSLocationCode from tblposmaster where strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
        ResultSet rsLocCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsLocCode.next())
        {
            locCode=rsLocCode.getString(1);
        }
        rsLocCode.close();
        if(!locCode.isEmpty())
        {
            hmOrderCode=funSaveOrder();
            String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
            String SODate=posDate[0];
          
          //Fill JSONObject feom hashmap
            JSONObject jObjSO=new JSONObject();
            JSONArray jArrSalesCharData=new JSONArray();
            String key="SO";
          
            if(hmOrderCode.size()>0)
            {
                for(Map.Entry<String,String> entry:hmOrderCode.entrySet())
                {
                    JSONObject jObj=new JSONObject();
                    JSONArray jArrSalesData=new JSONArray();
                    String fulFillmentDateKey=entry.getKey();
                    String orderCode=entry.getValue();
                    String sqlOrder="select b.strItemCode,b.dblStockQty,b.dblQty, "
                        + " b.strProductCode,a.dteOrderDate,a.dteSODate,a.strOrderCode "
                        + " from tblplaceorderhd a,tblplaceorderdtl b "
                        + " where a.strOrderCode=b.strOrderCode and a.strOrderCode='"+orderCode+"' ";
                    ResultSet rsOrder=clsGlobalVarClass.dbMysql.executeResultSet(sqlOrder);
                    while(rsOrder.next())
                    {
                       JSONObject jObjItemDtl=new JSONObject();
                       jObjItemDtl.put("ItemCode", rsOrder.getString(1));
                       jObjItemDtl.put("ProductCode",rsOrder.getString(4));
                       jObjItemDtl.put("StockQty",rsOrder.getString(2));
                       jObjItemDtl.put("OrderQty",rsOrder.getString(3));
                       jObjItemDtl.put("Weight", "0");
                       jArrSalesData.add(jObjItemDtl);
                    }
                    rsOrder.close();
                    
                    if(jArrSalesData.size()>0)
                    {
                        jObj.put("OrderData", jArrSalesData);
                        jObj.put("OrderCharData", jArrSalesCharData);
                        jObj.put("fullFillmentDate", fulFillmentDateKey);
                        jObj.put("OrderType","Normal Order");
                        jObj.put("SODate", SODate);
                        jObj.put("SOCode", "");
                        jObj.put("OrderCode", orderCode);
                        jObj.put("LocCode", locCode);
                        jObj.put("ClientCode", clsGlobalVarClass.gClientCode);
                        jObj.put("WSClientCode", clsGlobalVarClass.gWSClientCode);
                        orderCount++;
                        key="SO"+orderCount;
                        jObjSO.put(key, jObj);
                    }
                }
            }
            
        //Call webstock web service to save place order dtls to webstock table & pos order table
            for(int cn=1;cn<=jObjSO.size();cn++)
            {
                key="SO"+cn;
                JSONObject jObjSalesOrder=(JSONObject)jObjSO.get(key);
                String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funPlaceOrder";
                System.out.println(hoURL);
                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(jObjSalesOrder.toString().getBytes());
                os.flush();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String SOCode="",deliveryDate="",orderCode="";
                String output = "", op = "Updated successfully: ";
                while ((output = br.readLine()) != null)
                {
                    String []res=output.split("#");
                    SOCode=res[0];
                    deliveryDate=res[1];
                    op +=res[0];
                }
                System.out.println(op);
                orderCode=jObjSalesOrder.get("OrderCode").toString();
                sql="update tblplaceorderhd set strSOCOde='"+SOCode+"' "
                    + " where strOrderCode='"+orderCode+"' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl=new LinkedHashMap<String,List<clsPlaceOrderDtl>>();
                List<clsPlaceOrderDtl> listItemDtl=null;
                String sqlOrder="select c.strItemName,d.strSubGroupName,b.dblQty, "
                    + " b.strProductCode,a.dteOrderDate,a.dteSODate,a.strOrderCode "
                    + " from tblplaceorderhd a,tblplaceorderdtl b,tblitemmaster c,tblsubgrouphd d "
                    + " where a.strOrderCode=b.strOrderCode and b.strItemCode=c.strItemCode "
                    + " and c.strSubGroupCode=d.strSubGroupCode and a.strOrderCode='"+orderCode+"' "
                    + " order by d.strSubGroupName ";
                ResultSet rsItemDtl=clsGlobalVarClass.dbMysql.executeResultSet(sqlOrder);
                while(rsItemDtl.next())
                {
                    clsPlaceOrderDtl objItemDtl=new clsPlaceOrderDtl();
                    if(hmPlaceOrderDtl.containsKey(rsItemDtl.getString(2)))
                    {
                        listItemDtl=hmPlaceOrderDtl.get(rsItemDtl.getString(2));
                        objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                        objItemDtl.setItemName(rsItemDtl.getString(1));
                        objItemDtl.setSaleQty(rsItemDtl.getString(3));
                    }
                    else
                    {
                        listItemDtl=new ArrayList<clsPlaceOrderDtl>();
                        objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                        objItemDtl.setItemName(rsItemDtl.getString(1));
                        objItemDtl.setSaleQty(rsItemDtl.getString(3));
                    }
                    listItemDtl.add(objItemDtl);
                    hmPlaceOrderDtl.put(rsItemDtl.getString(2),listItemDtl);
                }
                rsItemDtl.close();

                JOptionPane.showMessageDialog(this, "Sales Order Code : "+SOCode);
                String[] dt=SODate.split("-");
                String dtSODate=dt[2]+"-"+dt[1]+"-"+dt[0];
                String[] dt1=deliveryDate.split("-");
                funGeneratePlaceOrderTextReport(dtSODate, SODate, hmPlaceOrderDtl, SOCode);
                //funGeneratePlaceOrderJasperReport(dtSODate,SODate,hmPlaceOrderDtl,SOCode,"Normal Order Details");
                funResetFields();
            }

            if(count>0)
            {
                if(count==jObjSO.size())
                {
                    JOptionPane.showMessageDialog(this, "Order placed Successfully");
                    btnPlaceNormalOrder.setEnabled(true);
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("PlaceOrder","");
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Location Not Found!!!!!!!!!");
            btnPlaceNormalOrder.setEnabled(true);
        }
    }
    
     
    
    
//Save Normal Order     
    private Map<String,String> funSaveOrder() throws Exception
    {
        String locCode="";
        Map<String,String> hmOrderCode=null;
        Map<String,List<clsOrderDtl>> hmOrderDtl=null;
        String sql="select strWSLocationCode from tblposmaster where strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
        ResultSet rsLocCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsLocCode.next())
        {
            locCode=rsLocCode.getString(1);
        }
        rsLocCode.close();
        if(!locCode.isEmpty())
        {
            String fulFtDate="",existOrderCode="";
            hmOrderDtl=new HashMap<String,List<clsOrderDtl>>();
            hmOrderCode=new HashMap<String,String>();
            List<clsOrderDtl> arrListOrderDtl=new ArrayList<clsOrderDtl>();
            
            for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
            {
                existOrderCode=tblPlaceOrderItems.getValueAt(cnt, 6).toString();
                clsOrderDtl objOrderDtl=null;
                if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
                {
                    fulFtDate=tblPlaceOrderItems.getValueAt(cnt, 5).toString();
                    objOrderDtl=new clsOrderDtl();
                    objOrderDtl.setItemCode(tblPlaceOrderItems.getValueAt(cnt, 11).toString());
                    objOrderDtl.setProductCode(tblPlaceOrderItems.getValueAt(cnt, 8).toString());
                    objOrderDtl.setOrderTypeCode(hmOrder.get(cmbOrderType.getSelectedItem().toString()).toString());
                    objOrderDtl.setQty(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString()));
                    objOrderDtl.setStockQty(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 3).toString()));
                    objOrderDtl.setWeight(0);
                    objOrderDtl.setOrderCode(existOrderCode);
                }
                
                if(hmOrderDtl.containsKey(fulFtDate))
                {
                    arrListOrderDtl=hmOrderDtl.get(fulFtDate);
                    if(objOrderDtl!=null)
                    {
                        arrListOrderDtl.add(objOrderDtl);
                    }
                }
                else
                {
                    if(objOrderDtl!=null)
                    {
                        arrListOrderDtl=new ArrayList<clsOrderDtl>();
                        arrListOrderDtl.add(objOrderDtl);
                    }
                }
                if(arrListOrderDtl.size()>0)
                {
                    hmOrderDtl.put(fulFtDate, arrListOrderDtl);
                }
            }
            int cnt=0;
            List<clsOrderDtl> listOrderDtl = new ArrayList<clsOrderDtl>();
            for(Map.Entry<String,List<clsOrderDtl>> entry:hmOrderDtl.entrySet())
            {
                String orderCode="";
                listOrderDtl=entry.getValue();
                for(clsOrderDtl objOrderDtl:entry.getValue())
                {
                    if(objOrderDtl.getOrderCode().equals(""))
                    {
                        orderCode=funGenerateOrderCode();
                        break;
                    }
                    else
                    {
                        orderCode=objOrderDtl.getOrderCode();
                        sql="delete from tblplaceorderhd where strOrderCode='"+orderCode+"'";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        sql="delete from tblplaceorderdtl where strOrderCode='"+orderCode+"'";
                        clsGlobalVarClass.dbMysql.execute(sql);
                    }    
                }
              
                String sqlInsertPlaceOrderHd="insert into tblplaceorderhd (strOrderCode,strSOCode,dteSODate"
                    + ",dteOrderDate,strUserCreated,dteDateCreated,strClientCode,strCloseSO,strDCCode,strOrderTypeCode"
                    + ",strOrderType ) "
                    + "values"
                    + "('"+orderCode+"','','"+clsGlobalVarClass.gPOSDateForTransaction+"','"+entry.getKey()+"','"+clsGlobalVarClass.gUserCode+"'"
                    + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','N',''"
                    + ",'"+hmOrder.get(cmbOrderType.getSelectedItem().toString())+"','Normal')";
                //System.out.println("sqlInsertPlaceOrderHd:"+sqlInsertPlaceOrderHd);
                clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderHd);
                for(clsOrderDtl objOrderDtl:listOrderDtl)
                {
                    String sqlInsertPlaceOrderDtl="insert into tblplaceorderdtl "
                        + "(strOrderCode,strProductCode,strItemCode,dblQty,dblStockQty,strClientCode) "
                        + "values ('"+orderCode+"','"+objOrderDtl.getProductCode()+"','"+objOrderDtl.getItemCode()+"'"
                        + ",'"+objOrderDtl.getQty()+"','"+objOrderDtl.getStockQty()+"','"+clsGlobalVarClass.gClientCode+"')";
                    //System.out.println("sqlInsertPlaceOrderDtl:"+sqlInsertPlaceOrderDtl);
                    clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderDtl);
                }
                cnt++;
                hmOrderCode.put(entry.getKey(), orderCode);
            }
            if(cnt>0)
            {
               if(cnt==hmOrderDtl.size()) 
               {
                 JOptionPane.showMessageDialog(this, "Order saved Successfully");
                 //funResetFields();
                 btnSaveNormalOrder.setEnabled(true);
               }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Location Not Found!!!!!!!!!");
            btnSaveNormalOrder.setEnabled(true);
        }
        
        return hmOrderCode;
    }
    
    
    
    public File funGeneratePlaceOrderTextReport(String placedOrderDate,String SODate,Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl,String SOCode)
    {
        File textFile=null;
        try
        {
            clsUtility objUtil = (clsUtility)objUtility.clone();
            objUtil.funCreateTempFolder();
            String filPath = System.getProperty("user.dir");
            textFile = new File(filPath + "/Temp/NormalOrderDetails.txt");
            PrintWriter pw = new PrintWriter(textFile);
            
            double finalTotalQty=0;
            pw.println();
            pw.println(objUtil.funPrintTextWithAlignment("Order List", 40, "Center"));
            pw.println(objUtil.funPrintTextWithAlignment("POS Name    :"+clsGlobalVarClass.gPOSName, 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("Order Type  :"+cmbOrderType.getSelectedItem().toString(), 30, "Left"));
            pw.println(objUtil.funPrintTextWithAlignment("Order Date  :"+lblGeneralOrderDate.getText(), 30, "Left"));
            
            if(!SODate.isEmpty())
            {
                pw.println(objUtil.funPrintTextWithAlignment("SO Date  :"+SODate, 30, "Left"));
                pw.println(objUtil.funPrintTextWithAlignment("SO Code :"+SOCode, 30, "Left"));
            }
            
            pw.println("----------------------------------------");
            pw.println(objUtil.funPrintTextWithAlignment("SubGroup Name  ",40,"Left"));
            pw.println("----------------------------------------");
            pw.print(objUtil.funPrintTextWithAlignment("ItemName",32,"Left"));
            pw.print(objUtil.funPrintTextWithAlignment("Quantity",8,"Left"));
            pw.println();
            pw.println("----------------------------------------");
            pw.println();
            for (Map.Entry<String, List<clsPlaceOrderDtl>> entry : hmPlaceOrderDtl.entrySet())
            {
                double totalOrderQty=0;
                List<clsPlaceOrderDtl> listOfPlaceOrderDtl = entry.getValue();
                pw.println(objUtil.funPrintTextWithAlignment(entry.getKey(),40,"Left"));
                pw.println("----------------------------------------");
                for(int j=0;j<listOfPlaceOrderDtl.size();j++)
                {
                    clsPlaceOrderDtl objOrder=listOfPlaceOrderDtl.get(j);
                    pw.print(objUtil.funPrintTextWithAlignment(objOrder.getItemName(),32,"Left"));
                    pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(objOrder.getSaleQty()),8,"Right"));
                    pw.println();
                    totalOrderQty+=Double.parseDouble(objOrder.getSaleQty());
                }
                pw.println("----------------------------------------");
                pw.print(objUtil.funPrintTextWithAlignment(entry.getKey(),32,"Left"));
                pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(totalOrderQty),8,"Right"));
                pw.println();
                pw.println("----------------------------------------");
                pw.println();
                pw.println();
                finalTotalQty+=totalOrderQty;
            }

            pw.println("----------------------------------------");
            pw.print(objUtil.funPrintTextWithAlignment("Total ",32,"Left"));
            pw.print(objUtil.funPrintTextWithAlignment(String.valueOf(finalTotalQty),8,"Right"));
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
            
            funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort,textFile.getAbsolutePath());
            funShowTextFile(textFile, "Placed Order List Report");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return textFile;
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
    
    
    private void funGeneratePlaceOrderJasperReport(String placedOrderDate,String SODate,Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl,String SOCode,String Title)
    {
        try
        {
            String reportName = "com/POSTransaction/view/rptPlaceOrderReport.jasper";
            HashMap hm = new HashMap();
            String imagePath = System.getProperty("user.dir");
            imagePath = imagePath + "\\ReportImage";
            System.out.println("imagePath=" + imagePath);
            hm.put("posName", clsGlobalVarClass.gPOSName);
            hm.put("orderDate", placedOrderDate);
            hm.put("SODate",SODate);
            hm.put("SOCode",SOCode);
            hm.put("userName", clsGlobalVarClass.gUserName);
            hm.put("imagePath", imagePath);
            hm.put("clientName", clsGlobalVarClass.gClientName);
            hm.put("Title", Title);
            String line="__________";

            List<clsPlaceOrderDtl> listOfLeftSidePlaceOrderDtl=new ArrayList<clsPlaceOrderDtl>();
            List<clsPlaceOrderDtl> listOfRightSidePlaceOrderDtl=new ArrayList<clsPlaceOrderDtl>();
            List<clsPlaceOrderDtl> orderList = new ArrayList<clsPlaceOrderDtl>(); 
            int c=0;
            Font font = new Font("Courier", Font.BOLD,14);
            for (Map.Entry<String, List<clsPlaceOrderDtl>> entry : hmPlaceOrderDtl.entrySet())
            {
                orderList = entry.getValue();
                if(c%2==0)
                {
                    double totalQty=0;
                    clsPlaceOrderDtl objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName((char)27+entry.getKey());
                    objOrder.setItemName(entry.getKey());
                    objOrder.setSaleQty("");
                    listOfLeftSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName("");
                    objOrder.setItemName(line+line);
                    objOrder.setSaleQty(" ");
                    listOfLeftSidePlaceOrderDtl.add(objOrder);
                    for(int i=0;i<orderList.size();i++)
                    {
                        objOrder=orderList.get(i);
                        totalQty=totalQty+Double.valueOf(objOrder.getSaleQty());
                        listOfLeftSidePlaceOrderDtl.add(objOrder);
                    }
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName((char)27+entry.getKey());
                    objOrder.setItemName("");
                    objOrder.setSaleQty(line);
                    listOfLeftSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName(entry.getKey());
                    objOrder.setItemName("");
                    objOrder.setSaleQty(String.valueOf(totalQty));
                    listOfLeftSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName("");
                    objOrder.setItemName("");
                    objOrder.setSaleQty("");
                    listOfLeftSidePlaceOrderDtl.add(objOrder);
                } 
                else
                {
                    double totalQty=0;
                    clsPlaceOrderDtl objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName(entry.getKey());
                    objOrder.setItemName(entry.getKey());
                    objOrder.setSaleQty("");
                    listOfRightSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName("");
                    objOrder.setItemName(line+line);
                    objOrder.setSaleQty(" ");
                    listOfRightSidePlaceOrderDtl.add(objOrder);
                    for(int i=0;i<orderList.size();i++)
                    {
                        objOrder=orderList.get(i);
                        totalQty=totalQty+Double.valueOf(objOrder.getSaleQty());
                        listOfRightSidePlaceOrderDtl.add(objOrder);
                    }

                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName(entry.getKey());
                    objOrder.setItemName("");
                    objOrder.setSaleQty(line);
                    listOfRightSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName(entry.getKey());
                    objOrder.setItemName("");
                    objOrder.setSaleQty(String.valueOf(totalQty));
                    listOfRightSidePlaceOrderDtl.add(objOrder);
                    objOrder=new clsPlaceOrderDtl();
                    objOrder.setSubGroupName("");
                    objOrder.setItemName("");
                    objOrder.setSaleQty("");
                    listOfRightSidePlaceOrderDtl.add(objOrder);
                }
                c++;
            }

            hm.put("LeftSideList",listOfLeftSidePlaceOrderDtl );
            hm.put("RightSideList",listOfRightSidePlaceOrderDtl );

            InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
            JasperPrint print = JasperFillManager.fillReport(is, hm, clsGlobalVarClass.conJasper);
            JRViewer viewer = new JRViewer(print);
            JFrame jf = new JFrame();
            jf.getContentPane().add(viewer);
            jf.validate();
            jf.setVisible(true);
            jf.setSize(new Dimension(850, 750));
            jf.setLocationRelativeTo(this);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
                    
    }
    
    
    
    private void funOpenMenuItemSearch()
    {
        objUtility.funCallForSearchForm("MenuItemForPlaceOrder");
        new frmSearchFormDialog(this,true).setVisible(true);
        if(clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data=clsGlobalVarClass.gArrListSearchData.toArray();
            lblItemCode.setText(data[0].toString());
            clsGlobalVarClass.gSearchItemClicked=false;
        }
    }
    
    
    private void funCalculateTotalQty()
    {
        String groupCode=cmbGroup.getSelectedItem().toString();
        groupCode=groupCode.substring(groupCode.length()-8, groupCode.length());
        String subGroupCode=cmbSubGroup.getSelectedItem().toString();
        subGroupCode=subGroupCode.substring(subGroupCode.length()-9, subGroupCode.length());
        
        String rowGroupCode="",rowSubGroupCode="";
        double totalSubGropQty=0,totalGroupQty=0;
        
        if(tblPlaceOrderItems.getSelectedColumn()==4)
        {
            double totalQty=0;
            for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
            {
                if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
                {
                    double qty=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    totalQty+=qty;
                    rowGroupCode=tblPlaceOrderItems.getValueAt(cnt, 9).toString();
                    rowSubGroupCode=tblPlaceOrderItems.getValueAt(cnt, 10).toString();
                    if(subGroupCode.equals(rowSubGroupCode))
                    {
                        totalSubGropQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                 
                    if(groupCode.equals(rowGroupCode))
                    {
                        totalGroupQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                }
            }
            lblTotalQty.setText(String.valueOf(totalQty));
            lblTotalSubGroupQty.setText(String.valueOf(totalSubGropQty));
            lblTotalGroupQty.setText(String.valueOf(totalGroupQty));
        }
        else
        {
            double totalQty=0;
            for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
            {
                if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
                {
                    double qty=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    totalQty+=qty;
                    rowGroupCode=tblPlaceOrderItems.getValueAt(cnt, 9).toString();
                    rowSubGroupCode=tblPlaceOrderItems.getValueAt(cnt, 10).toString();
                    if(subGroupCode.equals(rowSubGroupCode))
                    {
                        totalSubGropQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString()); 
                    }
                 
                    if(groupCode.equals(rowGroupCode))
                    {
                        totalGroupQty+=Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                }
            }
            lblTotalQty.setText(String.valueOf(totalQty));
            lblTotalSubGroupQty.setText(String.valueOf(totalSubGropQty));
            lblTotalGroupQty.setText(String.valueOf(totalGroupQty));
        }
    }
        
    
    
//Place Advance Order    
    private void funAdvancePlaceOrder() throws Exception
    {
        int count=0;
        String deliveryDate="";
        String [] SODate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        String locCode="";
        String sql="select strWSLocationCode from tblposmaster where strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
        ResultSet rsLocCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsLocCode.next())
        {
            locCode=rsLocCode.getString(1);
        }
        rsLocCode.close();
        
        if(!locCode.isEmpty())
        {
            Map<String,List<String>> hmAdvOrderDtl=new HashMap<String,List<String>>();
            Map<String,List<clsSpecialOrderItemDtl>> hmSpecialOrderItemDtl=new HashMap<String,List<clsSpecialOrderItemDtl>>(); 
            Map<String,List<clsSpecialOrderItemCharDtl>> hmSpecialOrderItemCharDtl=new HashMap<String,List<clsSpecialOrderItemCharDtl>>(); 
            Map<String,List<String>> hmAdvanceOrderSOCodeDtl=new HashMap<String,List<String>>();
            Map<String,String> hmOrderCode=new HashMap<String,String>(); 
            for(int cn=0;cn<tblPlaceAdvanceOrderItems.getRowCount();cn++)
            {
                String key=tblPlaceAdvanceOrderItems.getValueAt(cn, 4).toString();
                if(hmAdvOrderDtl.containsKey(key))
                {
                    List<String> arrListAdvOrcerNo=hmAdvOrderDtl.get(key);
                    arrListAdvOrcerNo.add(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                    hmAdvOrderDtl.put(key,arrListAdvOrcerNo);
                }
                else
                {
                    List<String> arrListAdvOrcerNo=new ArrayList<String>();
                    arrListAdvOrcerNo.add(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                    hmAdvOrderDtl.put(key,arrListAdvOrcerNo);
                }
                
                List<clsSpecialOrderItemDtl> listSpecailItemDtl=null;
                if(hmSpecialOrderItemDtl.containsKey(key))
                {
                    listSpecailItemDtl=hmSpecialOrderItemDtl.get(key);
                }
                else
                {
                    listSpecailItemDtl=new ArrayList<clsSpecialOrderItemDtl>();
                }
                clsSpecialOrderItemDtl objSpecialItemDtl=new clsSpecialOrderItemDtl();
                objSpecialItemDtl.setItemCode(tblPlaceAdvanceOrderItems.getValueAt(cn, 1).toString());
                objSpecialItemDtl.setProductCode(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+6).toString());
                objSpecialItemDtl.setOrderQty(Double.parseDouble(tblPlaceAdvanceOrderItems.getValueAt(cn, 3).toString()));
                objSpecialItemDtl.setStockQty(Double.parseDouble(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+8).toString()));
                objSpecialItemDtl.setWeight(Double.parseDouble(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+7).toString()));
                objSpecialItemDtl.setAdvOrderNo(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                
                listSpecailItemDtl.add(objSpecialItemDtl);
                hmSpecialOrderItemDtl.put(key,listSpecailItemDtl);
                
                List<clsSpecialOrderItemCharDtl> listSpecailItemCharDtl=null;
                if(hmSpecialOrderItemCharDtl.containsKey(key))
                {
                    listSpecailItemCharDtl=hmSpecialOrderItemCharDtl.get(key);
                }
                else
                {
                    listSpecailItemCharDtl=new ArrayList<clsSpecialOrderItemCharDtl>();
                }
                
                sql=" select a.strCharCode,c.strWSCharCode,a.strCharValues,a.strAdvBookingNo "
                    + " from tbladvbookbillchardtl a,tbladvbookbillhd b,tblcharactersticsmaster c "
                    + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strCharCode=c.strCharCode "
                    + " and a.strAdvBookingNo='"+tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+5).toString()+"' "
                    + " and a.strItemCode= '"+tblPlaceAdvanceOrderItems.getValueAt(cn, 1).toString()+"' ";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rs.next())
                {
                    clsSpecialOrderItemCharDtl objSpecialItemCharDtl=new clsSpecialOrderItemCharDtl();
                    objSpecialItemCharDtl.setItemCode(tblPlaceAdvanceOrderItems.getValueAt(cn, 1).toString());
                    objSpecialItemCharDtl.setProductCode(tblPlaceAdvanceOrderItems.getValueAt(cn, hiddenColumnCount+6).toString());
                    objSpecialItemCharDtl.setCharacterstics(rs.getString(2)+":"+rs.getString(3));
                    objSpecialItemCharDtl.setAdvOrderNo(rs.getString(4));
                    listSpecailItemCharDtl.add(objSpecialItemCharDtl);
                }
                rs.close();
                hmSpecialOrderItemCharDtl.put(key,listSpecailItemCharDtl);
            }
            
            int cntSO=0;
            JSONObject jObjSO=new JSONObject();
            
            for(Map.Entry<String,List<clsSpecialOrderItemDtl>> entry : hmSpecialOrderItemDtl.entrySet() )
            {
                JSONObject jObj=new JSONObject();
                String fulFillmentDate=entry.getKey();
                cntSO++;
                JSONArray jArrSalesData=new JSONArray();
                JSONArray jArrSalesCharData=new JSONArray();
                List<clsSpecialOrderItemDtl> listSpecialItemDtl=entry.getValue();
                for(clsSpecialOrderItemDtl obj:listSpecialItemDtl)
                {
                    JSONObject jObjItemDtl=new JSONObject();
                    jObjItemDtl.put("ItemCode", obj.getItemCode());
                    jObjItemDtl.put("ProductCode", obj.getProductCode());
                    jObjItemDtl.put("StockQty", obj.getStockQty());
                    jObjItemDtl.put("OrderQty", obj.getOrderQty());
                    jObjItemDtl.put("Weight", obj.getWeight());
                    jObjItemDtl.put("AdvOrderNo", obj.getAdvOrderNo());
                    jArrSalesData.add(jObjItemDtl);
                }
                
                if(hmSpecialOrderItemCharDtl.containsKey(entry.getKey()))
                {
                    List<clsSpecialOrderItemCharDtl> listSpecialItemCharDtl=hmSpecialOrderItemCharDtl.get(entry.getKey());
                    for(clsSpecialOrderItemCharDtl obj:listSpecialItemCharDtl)
                    {
                        JSONObject jObjItemCharDtl=new JSONObject();
                        jObjItemCharDtl.put("Charcterstics", obj.getCharacterstics());
                        jObjItemCharDtl.put("ProductCode", obj.getProductCode());
                        jObjItemCharDtl.put("AdvOrderNo", obj.getAdvOrderNo());
                        jArrSalesCharData.add(jObjItemCharDtl);
                    }
                }
                
                jObj.put("OrderData", jArrSalesData);
                jObj.put("OrderCharData", jArrSalesCharData);
                jObj.put("fullFillmentDate", fulFillmentDate);
                jObj.put("OrderType","Advance Order");
                jObj.put("SODate", SODate[0]);
                jObj.put("LocCode", locCode);
                jObj.put("SOCode", "");
                jObj.put("OrderCode", "");
                jObj.put("ClientCode", clsGlobalVarClass.gClientCode);
                jObj.put("WSClientCode", clsGlobalVarClass.gWSClientCode);
                String key="SO"+cntSO;
                jObjSO.put(key, jObj);
            }
            
            for(int cn=1;cn<=jObjSO.size();cn++)
            {
                String key="SO"+cn;
                JSONObject jObjSalesOrder=(JSONObject)jObjSO.get(key);

                String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funPlaceOrder";
                System.out.println(hoURL);
                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(jObjSalesOrder.toString().getBytes());
                os.flush();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String SOCode="";
                String output = "", op = "Updated successfully: ";
                while ((output = br.readLine()) != null)
                {
                    String []res=output.split("#");
                    SOCode=res[0];
                    deliveryDate=res[1];
                    op +=res[0];
                }
                System.out.println(op);

                if(!SOCode.isEmpty())
                {
                    String orderCode="";
                    orderCode=funGenerateOrderCode();
                    
                    List<clsPlaceOrderDtl> listItemDtl=null;
                    List<String> listCharValue=null;
                    String sqlInsertPlaceOrderDtl="insert into tblplaceorderdtl "
                        + "(strOrderCode,strProductCode,strItemCode,dblQty,dblStockQty,strClientCode,strAdvOrderNo) "
                        + "values ";
                    JSONArray mJsonArray=(JSONArray)jObjSalesOrder.get("OrderData");
                    JSONObject mJsonObject = new JSONObject();
                    Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl=new LinkedHashMap<String,List<clsPlaceOrderDtl>>();
                    for (int i = 0; i < mJsonArray.size(); i++)
                    {
                        mJsonObject =(JSONObject) mJsonArray.get(i);
                        sql="select b.strSubGroupCode,b.strSubGroupName,a.strItemName from tblitemmaster a,tblsubgrouphd b "
                            + " where a.strItemCode='"+mJsonObject.get("ItemCode").toString()+"' "
                            + " and a.strSubGroupCode=b.strSubGroupCode";
                        ResultSet rsItemDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if(rsItemDtl.next())
                        {
                            clsPlaceOrderDtl objItemDtl=new clsPlaceOrderDtl();
                            if(hmPlaceOrderDtl.containsKey(rsItemDtl.getString(2)))
                            {
                                listItemDtl=hmPlaceOrderDtl.get(rsItemDtl.getString(2));
                                objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                                objItemDtl.setItemName(rsItemDtl.getString(3));
                                objItemDtl.setSaleQty(mJsonObject.get("OrderQty").toString());
                            }
                            else
                            {
                                listItemDtl=new ArrayList<clsPlaceOrderDtl>();
                                objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                                objItemDtl.setItemName(rsItemDtl.getString(3));
                                objItemDtl.setSaleQty(mJsonObject.get("OrderQty").toString());
                            }
                            listItemDtl.add(objItemDtl);
                            hmPlaceOrderDtl.put(rsItemDtl.getString(2),listItemDtl);
                        }
                        rsItemDtl.close();
                        
                        if(hmAdvanceOrderSOCodeDtl.containsKey(mJsonObject.get("AdvOrderNo").toString()))
                        {
                            listCharValue=hmAdvanceOrderSOCodeDtl.get(mJsonObject.get("AdvOrderNo").toString());
                        }
                        else
                        {
                            listCharValue=new ArrayList<String>();
                        }
                        String val=SOCode+"#"+mJsonObject.get("ItemCode").toString();
                        listCharValue.add(val);
                        hmAdvanceOrderSOCodeDtl.put(mJsonObject.get("AdvOrderNo").toString(),listCharValue);
                        
                        if(i==0)
                        {
                            sqlInsertPlaceOrderDtl+="('"+orderCode+"','"+mJsonObject.get("ProductCode").toString()+"'"
                                + ",'"+mJsonObject.get("ItemCode").toString()+"','"+mJsonObject.get("OrderQty")+"'"
                                + ",'"+mJsonObject.get("StockQty")+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+mJsonObject.get("AdvOrderNo")+"')";
                        }
                        else
                        {
                            sqlInsertPlaceOrderDtl+=",('"+orderCode+"','"+mJsonObject.get("ProductCode").toString()+"'"
                                + ",'"+mJsonObject.get("ItemCode").toString()+"','"+mJsonObject.get("OrderQty")+"'"
                                + ",'"+mJsonObject.get("StockQty")+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+mJsonObject.get("AdvOrderNo")+"')";
                        }
                    }
                    clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderDtl);
                                        
                    String orderDate=jObjSalesOrder.get("fullFillmentDate").toString();
                    if(hmAdvOrderDtl.containsKey(orderDate))
                    {
                        List<String> arrListAdvOrderNo=hmAdvOrderDtl.get(deliveryDate);
                        for(String advOrderNo:arrListAdvOrderNo)
                        {
                            sql="insert into tblplaceorderadvorderdtl (strAdvOrderNo,dteOrderDate,strClientCode,strDataPostFlag"
                                + ",strOrderType) "
                                + " values('"+advOrderNo+"','"+orderDate+"','"+clsGlobalVarClass.gClientCode+"','N'"
                                + ",'Advance') ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }
                    }
                    
                    String sqlInsertPlaceOrderHd="insert into tblplaceorderhd "
                        + "(strOrderCode,strSOCode,dteSODate,dteOrderDate,strUserCreated,dteDateCreated,strClientCode "
                        + ",strCloseSO,strDCCode,strOrderType) values "
                        + "('"+orderCode+"','"+SOCode+"','"+SODate[0]+"','"+orderDate+"','"+clsGlobalVarClass.gUserCode+"'"
                        + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','N','','Advance')";
                    clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderHd);
                    
                    String[] dt=SODate[0].split("-");
                    String dtSODate=dt[2]+"-"+dt[1]+"-"+dt[0];
                    String[] dt1=deliveryDate.split("-");
                    String dtDelivery=dt1[2]+"-"+dt1[1]+"-"+dt1[0];
                    hmOrderCode.put(orderCode,SOCode);
                    
                    //funGeneratePlaceOrderJasperReport(dtSODate,dtDelivery,hmPlaceOrderDtl,SOCode,"Advance Order Details");
                    funGeneratePlaceOrderTextReport(dtSODate,dtDelivery,hmPlaceOrderDtl,SOCode);
                    funSaveAdvanceOrderImageDtl(hmAdvanceOrderSOCodeDtl);
                    //funUpdateOrderCodeToMMS(hmOrderCode);
                    JOptionPane.showMessageDialog(null, SOCode);
                    count++;
                    funResetAdvanceOrderFields();
                }
            }
            if(count>0)
            {
                if(count==jObjSO.size())
                {
                    JOptionPane.showMessageDialog(this, "Order placed Successfully");
                    btnPlaceAdvOrder.setEnabled(true);
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("PlaceOrder","");
                }
            } 
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Location Not Found!!!!!!!!!");
            btnPlaceAdvOrder.setEnabled(true);
        }
    }
    
    
    
//Place Urgent Order     
    private void funPlaceUrgentOrder() throws Exception
    {
        int count=0;
        String deliveryDate="";
        String [] SODate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        String locCode="";
        String sql="select strWSLocationCode from tblposmaster where strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
        ResultSet rsLocCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsLocCode.next())
        {
            locCode=rsLocCode.getString(1);
        }
        rsLocCode.close();
        
        if(!locCode.isEmpty())
        {
            Map<String,List<String>> hmAdvOrderDtl=new HashMap<String,List<String>>();
            Map<String,List<clsSpecialOrderItemDtl>> hmSpecialOrderItemDtl=new HashMap<String,List<clsSpecialOrderItemDtl>>(); 
            Map<String,List<clsSpecialOrderItemCharDtl>> hmSpecialOrderItemCharDtl=new HashMap<String,List<clsSpecialOrderItemCharDtl>>(); 
            Map<String,String> hmOrderCode=new HashMap<String,String>(); 
            for(int cn=0;cn<tblPlaceUrgentOrderItems.getRowCount();cn++)
            {
                String key=tblPlaceUrgentOrderItems.getValueAt(cn, 4).toString();
                if(hmAdvOrderDtl.containsKey(key))
                {
                    List<String> arrListAdvOrcerNo=hmAdvOrderDtl.get(key);
                    arrListAdvOrcerNo.add(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                    hmAdvOrderDtl.put(key,arrListAdvOrcerNo);
                }
                else
                {
                    List<String> arrListAdvOrcerNo=new ArrayList<String>();
                    arrListAdvOrcerNo.add(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                    hmAdvOrderDtl.put(key,arrListAdvOrcerNo);
                }
                
                List<clsSpecialOrderItemDtl> listSpecailItemDtl=null;
                if(hmSpecialOrderItemDtl.containsKey(key))
                {
                    listSpecailItemDtl=hmSpecialOrderItemDtl.get(key);
                }
                else
                {
                    listSpecailItemDtl=new ArrayList<clsSpecialOrderItemDtl>();
                }
                clsSpecialOrderItemDtl objSpecialItemDtl=new clsSpecialOrderItemDtl();
                objSpecialItemDtl.setItemCode(tblPlaceUrgentOrderItems.getValueAt(cn, 1).toString());
                objSpecialItemDtl.setProductCode(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+6).toString());
                objSpecialItemDtl.setOrderQty(Double.parseDouble(tblPlaceUrgentOrderItems.getValueAt(cn, 3).toString()));
                objSpecialItemDtl.setStockQty(Double.parseDouble(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+8).toString()));
                objSpecialItemDtl.setWeight(Double.parseDouble(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+7).toString()));
                objSpecialItemDtl.setAdvOrderNo(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+5).toString());
                listSpecailItemDtl.add(objSpecialItemDtl);
                hmSpecialOrderItemDtl.put(key,listSpecailItemDtl);
                                
                List<clsSpecialOrderItemCharDtl> listSpecailItemCharDtl=null;
                if(hmSpecialOrderItemCharDtl.containsKey(key))
                {
                    listSpecailItemCharDtl=hmSpecialOrderItemCharDtl.get(key);
                }
                else
                {
                    listSpecailItemCharDtl=new ArrayList<clsSpecialOrderItemCharDtl>();
                }
                
                sql=" select a.strCharCode,c.strWSCharCode,a.strCharValues,a.strAdvBookingNo "
                    + " from tbladvbookbillchardtl a,tbladvbookbillhd b,tblcharactersticsmaster c "
                    + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strCharCode=c.strCharCode "
                    + " and a.strAdvBookingNo='"+tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+5).toString()+"' ";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rs.next())
                {
                    clsSpecialOrderItemCharDtl objSpecialItemCharDtl=new clsSpecialOrderItemCharDtl();
                    objSpecialItemCharDtl.setItemCode(tblPlaceUrgentOrderItems.getValueAt(cn, 1).toString());
                    objSpecialItemCharDtl.setProductCode(tblPlaceUrgentOrderItems.getValueAt(cn, hiddenColumnCount+6).toString());
                    objSpecialItemCharDtl.setCharacterstics(rs.getString(2)+":"+rs.getString(3));
                    objSpecialItemCharDtl.setAdvOrderNo(rs.getString(4));
                    listSpecailItemCharDtl.add(objSpecialItemCharDtl);
                }
                rs.close();
                hmSpecialOrderItemCharDtl.put(key,listSpecailItemCharDtl);
            }
            
            int cntSO=0;
            JSONObject jObjSO=new JSONObject();
            for(Map.Entry<String,List<clsSpecialOrderItemDtl>> entry : hmSpecialOrderItemDtl.entrySet() )
            {
                JSONObject jObj=new JSONObject();
                String fulFillmentDate=entry.getKey();
                cntSO++;
                JSONArray jArrSalesData=new JSONArray();
                JSONArray jArrSalesCharData=new JSONArray();
                List<clsSpecialOrderItemDtl> listSpecialItemDtl=entry.getValue();
                for(clsSpecialOrderItemDtl obj:listSpecialItemDtl)
                {
                    JSONObject jObjItemDtl=new JSONObject();
                    jObjItemDtl.put("ItemCode", obj.getItemCode());
                    jObjItemDtl.put("ProductCode", obj.getProductCode());
                    jObjItemDtl.put("StockQty", obj.getStockQty());
                    jObjItemDtl.put("OrderQty", obj.getOrderQty());
                    jObjItemDtl.put("Weight", obj.getWeight());
                    jObjItemDtl.put("AdvOrderNo", obj.getAdvOrderNo());
                    
                    jArrSalesData.add(jObjItemDtl);
                }
                
                if(hmSpecialOrderItemCharDtl.containsKey(entry.getKey()))
                {
                    List<clsSpecialOrderItemCharDtl> listSpecialItemCharDtl=hmSpecialOrderItemCharDtl.get(entry.getKey());
                    for(clsSpecialOrderItemCharDtl obj:listSpecialItemCharDtl)
                    {
                        JSONObject jObjItemCharDtl=new JSONObject();
                        jObjItemCharDtl.put("Charcterstics", obj.getCharacterstics());
                        jObjItemCharDtl.put("ProductCode", obj.getProductCode());
                        jObjItemCharDtl.put("AdvOrderNo", obj.getAdvOrderNo());
                        jArrSalesCharData.add(jObjItemCharDtl);
                    }
                }
                
                jObj.put("OrderData", jArrSalesData);
                jObj.put("OrderCharData", jArrSalesCharData);
                jObj.put("fullFillmentDate", fulFillmentDate);
                jObj.put("OrderType","Urgent Order");
                jObj.put("SODate", SODate[0]);
                jObj.put("LocCode", locCode);
                jObj.put("SOCode", "");
                jObj.put("OrderCode", "");
                jObj.put("ClientCode", clsGlobalVarClass.gClientCode);
                jObj.put("WSClientCode", clsGlobalVarClass.gWSClientCode);
                String key="SO"+cntSO;
                jObjSO.put(key, jObj);
            }
            
            for(int cn=1;cn<=jObjSO.size();cn++)
            {
                String key="SO"+cn;
                JSONObject jObjSalesOrder=(JSONObject)jObjSO.get(key);

                String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funPlaceOrder";
                System.out.println(hoURL);
                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(jObjSalesOrder.toString().getBytes());
                os.flush();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String SOCode="";
                String output = "", op = "Updated successfully: ";
                while ((output = br.readLine()) != null)
                {
                    String []res=output.split("#");
                    SOCode=res[0];
                    deliveryDate=res[1];
                    op +=res[0];
                }
                System.out.println(op);

                if(!SOCode.isEmpty())
                {
                    String orderCode="";
                    orderCode=funGenerateOrderCode();
                    List<clsPlaceOrderDtl> listItemDtl=null;
                    String sqlInsertPlaceOrderDtl="insert into tblplaceorderdtl "
                        + "(strOrderCode,strProductCode,strItemCode,dblQty,dblStockQty,strClientCode,strAdvOrderNo) "
                        + "values ";
                    JSONArray mJsonArray=(JSONArray)jObjSalesOrder.get("OrderData");
                    JSONObject mJsonObject = new JSONObject();
                    Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl=new LinkedHashMap<String,List<clsPlaceOrderDtl>>();
                    for (int i = 0; i < mJsonArray.size(); i++)
                    {
                        mJsonObject =(JSONObject) mJsonArray.get(i);
                        sql="select b.strSubGroupCode,b.strSubGroupName,a.strItemName from tblitemmaster a,tblsubgrouphd b "
                            + " where a.strItemCode='"+mJsonObject.get("ItemCode").toString()+"' "
                            + " and a.strSubGroupCode=b.strSubGroupCode";
                        ResultSet rsItemDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if(rsItemDtl.next())
                        {
                            clsPlaceOrderDtl objItemDtl=new clsPlaceOrderDtl();
                            if(hmPlaceOrderDtl.containsKey(rsItemDtl.getString(2)))
                            {
                                listItemDtl=hmPlaceOrderDtl.get(rsItemDtl.getString(2));
                                objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                                objItemDtl.setItemName(rsItemDtl.getString(3));
                                objItemDtl.setSaleQty(mJsonObject.get("OrderQty").toString());
                            }
                            else
                            {
                                listItemDtl=new ArrayList<clsPlaceOrderDtl>();
                                objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                                objItemDtl.setItemName(rsItemDtl.getString(3));
                                objItemDtl.setSaleQty(mJsonObject.get("OrderQty").toString());
                            }
                            listItemDtl.add(objItemDtl);
                            hmPlaceOrderDtl.put(rsItemDtl.getString(2),listItemDtl);
                        }
                        rsItemDtl.close();
                           
                        if(i==0)
                        {
                            sqlInsertPlaceOrderDtl+="('"+orderCode+"','"+mJsonObject.get("ProductCode").toString()+"'"
                                + ",'"+mJsonObject.get("ItemCode").toString()+"','"+mJsonObject.get("OrderQty")+"'"
                                + ",'"+mJsonObject.get("StockQty")+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+mJsonObject.get("AdvOrderNo")+"')";
                        }
                        else
                        {
                            sqlInsertPlaceOrderDtl+=",('"+orderCode+"','"+mJsonObject.get("ProductCode").toString()+"'"
                                + ",'"+mJsonObject.get("ItemCode").toString()+"','"+mJsonObject.get("OrderQty")+"'"
                                + ",'"+mJsonObject.get("StockQty")+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+mJsonObject.get("AdvOrderNo")+"')";
                        }
                    }
                    clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderDtl);

                    String orderDate=jObjSalesOrder.get("fullFillmentDate").toString();
                    if(hmAdvOrderDtl.containsKey(orderDate))
                    {
                        List<String> arrListAdvOrderNo=hmAdvOrderDtl.get(deliveryDate);
                        for(String advOrderNo:arrListAdvOrderNo)
                        {
                            sql="insert into tblplaceorderadvorderdtl (strAdvOrderNo,dteOrderDate,strClientCode,strDataPostFlag"
                                + ",strOrderType) "
                                + " values('"+advOrderNo+"','"+orderDate+"','"+clsGlobalVarClass.gClientCode+"','N'"
                                + ",'Urgent') ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }
                    }
                    
                    String sqlInsertPlaceOrderHd="insert into tblplaceorderhd "
                        + "(strOrderCode,strSOCode,dteSODate,dteOrderDate,strUserCreated,dteDateCreated,strClientCode "
                        + ",strCloseSO,strDCCode,strOrderType) values "
                        + "('"+orderCode+"','"+SOCode+"','"+SODate[0]+"','"+deliveryDate+"','"+clsGlobalVarClass.gUserCode+"'"
                        + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','N','','Urgent')";
                    clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderHd);
                    
                    String[] dt=SODate[0].split("-");
                    String dtSODate=dt[2]+"-"+dt[1]+"-"+dt[0];
                    String[] dt1=deliveryDate.split("-");
                    String dtDelivery=dt1[2]+"-"+dt1[1]+"-"+dt1[0];
                    hmOrderCode.put(orderCode,SOCode);
                    //funGeneratePlaceOrderJasperReport(dtSODate,dtDelivery,hmPlaceOrderDtl,SOCode,"Advance Order Details");
                    funGeneratePlaceOrderTextReport(dtSODate,dtDelivery,hmPlaceOrderDtl,SOCode);
                    //funUpdateOrderCodeToMMS(hmOrderCode);
                    JOptionPane.showMessageDialog(null, SOCode);
                    count++;
                    funResetUrgentOrderFields();
                }
            }
            if(count>0)
            {
                if(count==jObjSO.size())
                {
                    JOptionPane.showMessageDialog(this, "Order placed Successfully");
                    btnPlaceUrgentOrder.setEnabled(true);
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("PlaceOrder","");
                }
            } 
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Location Not Found!!!!!!!!!");
            btnPlaceUrgentOrder.setEnabled(true);
        }
    }
   
    
    
    private void funSaveAdvanceOrderImageDtl( Map<String,List<String>> hmAdvanceOrderSOCodeDtl) throws Exception
    {
        if(hmAdvanceOrderSOCodeDtl.size()>0)
        {
            JSONObject objJson = new JSONObject();
            JSONArray arrImgObj = new JSONArray();

            for (Map.Entry<String, List<String>> entry : hmAdvanceOrderSOCodeDtl.entrySet())
            {
                String advanceOrderNo=entry.getKey();
                List<String> listOfItems = entry.getValue();
                for(int i=0;i<listOfItems.size();i++)
                {
                    String []data=listOfItems.get(i).split("#");
                    String SOCode=data[0];
                    String itemCode=data[1];
                    String sqlOrder="select a.blobCakeImage,a.strItemCode,b.strAdvBookingNo,c.strWSProductCode "
                        + " from tbladvbookbillimgdtl a,tbladvbookbillhd b,tblitemmasterlinkupdtl c "
                        + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strItemCode=c.strItemCode "
                        + " and a.strAdvBookingNo='"+advanceOrderNo+"' "
                        + " and a.strItemCode= '"+itemCode+"' ";
                    ResultSet rsAdvImg=clsGlobalVarClass.dbMysql.executeResultSet(sqlOrder);
                    while(rsAdvImg.next())
                    {
                        JSONObject objRows = new JSONObject();
                        Blob blob = rsAdvImg.getBlob(1);
                        int blobLength = (int) blob.length();
                        byte[] blobAsBytes = blob.getBytes(1, blobLength);
                        String encoded=Base64.getEncoder().encodeToString(blobAsBytes);
                        byte[] decoded = Base64.getDecoder().decode(blobAsBytes);
                        String filePath = funCreateTempFolderForImage();
                        File file = new File(filePath +"/"+rsAdvImg.getString(4)+".jpg");
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(decoded);
                        fos.close();
                       //ImageIO.write(bufferedImage, "jpg", outputfile);
                        FileInputStream fis=new FileInputStream(file);
                        byte[] bytes = Files.readAllBytes(file .toPath());
                        String encodedImage = Base64.getEncoder().encodeToString(bytes);
                        objRows.put("itemName",rsAdvImg.getString(4));
                        objRows.put("itemImage",encodedImage);
                        objRows.put("soCode",SOCode);
                        objRows.put("advOrderNo",rsAdvImg.getString(3));
                        arrImgObj.add(objRows);
                    }
                    rsAdvImg.close();
                }
            }  
                
            if(arrImgObj.size()>0)
            {
                objJson.put("ImageData", arrImgObj);
                objJson.put("ClientCode", clsGlobalVarClass.gWSClientCode);
                String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funSaveImage";
                System.out.println(hoURL);

                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(objJson.toString().getBytes());
                os.flush();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output = "", op = "";

                while ((output = br.readLine()) != null)
                {
                    op += output;
                }
                System.out.println("res=" + op);
                String []res=op.split("#");
                if(res[0].equals("successfully"))
                {
                    String decodedFile=res[1];
                }
                conn.disconnect();
            }
        }
    }
    
    
    
    private String funCreateTempFolderForImage()
    {
        String fileName="Download Image";
        File theDir = new File(fileName);
        if (!theDir.exists()) 
        {
            boolean result = false;
            try
            {
                theDir.mkdir();
                result = true;
            } 
            catch(SecurityException se)
            {
                se.printStackTrace();
            }
            if(result) 
            {
                System.out.println("DIR created");
            }
        }
        return fileName;
    }
    
    
    
    
    private void funUpdateOrderCodeToMMS( Map<String,String> hmOrderCode) throws Exception
    {
        if(hmOrderCode.size()>0)
        {
            JSONObject objJson = new JSONObject();
            JSONArray arrItemDtl = new JSONArray();

            for (Map.Entry<String, String> entry : hmOrderCode.entrySet())
            {
                JSONObject objRows = new JSONObject();
                objRows.put("orderCode",entry.getKey());
                objRows.put("soCode",entry.getValue());
                arrItemDtl.add(objRows);
            }  
                
            if(arrItemDtl.size()>0)
            {
                objJson.put("OrderDtl", arrItemDtl);
                String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funUpdateOrderCode";
                System.out.println(hoURL);
                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(objJson.toString().getBytes());
                os.flush();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output = "", op = "";
                while ((output = br.readLine()) != null)
                {
                    op += output;
                }
                System.out.println("res=" + op);
                conn.disconnect();
            }
        }
    }
   
   
    
    private void funLoadHmOrder() throws Exception
    {   
        hmOrder=new HashMap<String,String>();
        String sql=" select strOrderCode,strOrderDesc,tmeUpToTime from tblordermaster "
                 + " where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
        ResultSet rsOrder=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsOrder.next())
        {
            String upToTime = funConvertTime(rsOrder.getString(3));
            String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            upToTime = posDate[0] + " " + upToTime;
            Date orderDate = format.parse(upToTime);
            long orderTime = orderDate.getTime();
            orderUpToTime=orderDate.getTime();
            long diffTime = funTimeDiff(orderTime);
            if(diffTime>=0)
            {
                hmOrder.put(rsOrder.getString(2),rsOrder.getString(1));
            }
        }
        rsOrder.close();
        Set setOrder=hmOrder.keySet();
        Iterator itrOrder=setOrder.iterator();
        while(itrOrder.hasNext())
        {            
            cmbOrderType.addItem(itrOrder.next());
        }
    }
    
    
    private long funTimeDiff(long orderTime) throws Exception
    {
        Date currDate = new Date();
        String [] posDate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currTime = currDate.getHours()+":"+currDate.getMinutes()+":"+currDate.getSeconds();
        currTime=posDate[0] + " "+currTime;
        Date cDate = format.parse(currTime);
        long cTime=cDate.getTime();
        long diffTime = orderTime - cTime;
        return diffTime;
    }
    
    
   
    private String funGetDate(Date dt,int reqdDays)
    {
        String date="";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DATE, reqdDays);
        date=(cal.getTime().getYear()+1900)+"-"+(cal.getTime().getMonth()+1)+"-"+(cal.getTime().getDate());
        return date;
    }
    
    
     private String funGetDayOfWeek(int day)
    {
        String dayOfWeek = "";
        switch (day)
        {
            case 0:
                dayOfWeek = "Sunday";
                break;

            case 1:
                dayOfWeek = "Monday";
                break;

            case 2:
                dayOfWeek = "Tuesday";
                break;

            case 3:
                dayOfWeek = "Wednesday";
                break;

            case 4:
                dayOfWeek = "Thursday";
                break;

            case 5:
                dayOfWeek = "Friday";
                break;

            case 6:
                dayOfWeek = "Saturday";
                break;
        }
        return dayOfWeek;
    }
   
   
    private String funCheckDay(String selectedDay,String noDeliverDays)throws Exception
    {
        String strFound="No";
        String[] spDays = noDeliverDays.split(",");
        for (int cnt = 0; cnt < spDays.length; cnt++)
        {
            if((spDays[cnt].equals(selectedDay)) )
            {
                strFound="Yes";
            }
        }
        return strFound;
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

    private void funCheckCurrentAndRequiredDay(int cnt,String nodeliveryDays,String selectedOrderdate)throws Exception
    { 
        selectedOrderdate+=" "+"00"+":"+"00"+":"+"00";
        cnt++;
        String [] nextDate=selectedOrderdate.split(" ");
        String[] arrNextSpDate=nextDate[0].split("-");
        Date dtNextDate1=new Date(Integer.parseInt(arrNextSpDate[0]),Integer.parseInt(arrNextSpDate[1]),Integer.parseInt(arrNextSpDate[2]));
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(dtNextDate1);
        cal1.add(Calendar.DATE, cnt);
        int orderDay1 = cal1.getTime().getDay();
        String selectedOrderDay="";
        if (orderDay1==0)
        {
            selectedOrderDay="Sunday";
        }
        if (orderDay1==1)
        {
            selectedOrderDay="Monday";
        }
        if (orderDay1==2)
        {
            selectedOrderDay="Tuesday";
        }
        if (orderDay1==3)
        {
            selectedOrderDay="Wednesday";
        }
        if (orderDay1==4)
        {
            selectedOrderDay="Thursday";
        }
        if (orderDay1==5)
        {
            selectedOrderDay="Friday";
        }
        if (orderDay1==6)
        {
            selectedOrderDay="Saturday";
        }
      
        String strFoundYN=funCheckDay(selectedOrderDay,nodeliveryDays);
        if(strFoundYN.equals("Yes"))
        {
            funCheckCurrentAndRequiredDay(cnt,nodeliveryDays,selectedOrderdate);
        }
        else
        {
            funSetDate(selectedOrderdate,cnt);
        }
    }
   
   
    private String funSetDate(String selectedDate,int cnt)throws Exception
    {
        String [] nextDate=selectedDate.split(" ");
        String[] arrNextSpDate=nextDate[0].split("-");
        Date dtNextDate1=new Date(Integer.parseInt(arrNextSpDate[0]),Integer.parseInt(arrNextSpDate[1]),Integer.parseInt(arrNextSpDate[2]));
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(dtNextDate1);
        cal1.add(Calendar.DATE, cnt);
        orderDate=cal1.getTime().getYear()+"-"+(cal1.getTime().getMonth())+"-"+(cal1.getTime().getDate());
        String dte = (cal1.getTime().getDate()) + "-" +(cal1.getTime().getMonth())+ "-" + cal1.getTime().getYear();
        orderDate=cal1.getTime().getYear() + "-" +(cal1.getTime().getMonth()) + "-" + (cal1.getTime().getDate());
        if(cal1.getTime().getMonth()<=9)
        {
            orderDate=cal1.getTime().getYear()+"-"+"0"+(cal1.getTime().getMonth())+"-"+(cal1.getTime().getDate());
        }
        if(cal1.getTime().getDate()<=9)
        {
            orderDate=cal1.getTime().getYear()+"-"+"0"+(cal1.getTime().getMonth())+"-"+"0"+(cal1.getTime().getDate());
        }
        return orderDate;
    }
   
    
    private void funPrintOrder() throws Exception
    {
        Map<String,List<clsPlaceOrderDtl>> hmPlaceOrderDtl=new HashMap<String,List<clsPlaceOrderDtl>>();
        List<clsPlaceOrderDtl> listItemDtl=null;
        StringBuilder sbSql=new StringBuilder();
        for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
        {
            if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
            {
                sbSql.setLength(0);
                sbSql.append("select b.strSubGroupCode,b.strSubGroupName,a.strItemName "
                    + " from tblitemmaster a,tblsubgrouphd b "
                    + " where a.strItemCode='"+tblPlaceOrderItems.getValueAt(cnt, 1).toString()+"' "
                    + " and a.strSubGroupCode=b.strSubGroupCode");
                ResultSet rsItemDtl=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
                if(rsItemDtl.next())
                {
                    clsPlaceOrderDtl objItemDtl=new clsPlaceOrderDtl();
                    if(hmPlaceOrderDtl.containsKey(rsItemDtl.getString(2)))
                    {
                        listItemDtl=hmPlaceOrderDtl.get(rsItemDtl.getString(2));
                        objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                        objItemDtl.setItemName(rsItemDtl.getString(3));
                        objItemDtl.setSaleQty(tblPlaceOrderItems.getValueAt(cnt, 4).toString());
                    }
                    else
                    {
                        listItemDtl=new ArrayList<clsPlaceOrderDtl>();
                        objItemDtl.setSubGroupName(rsItemDtl.getString(2));
                        objItemDtl.setItemName(rsItemDtl.getString(3));
                        objItemDtl.setSaleQty(tblPlaceOrderItems.getValueAt(cnt, 4).toString().toString());
                    }
                    listItemDtl.add(objItemDtl);
                    hmPlaceOrderDtl.put(rsItemDtl.getString(2),listItemDtl);
                }
                rsItemDtl.close();
            }
        }
        funGeneratePlaceOrderTextReport(orderDate,"",hmPlaceOrderDtl,"");
    }
    
    
    private boolean funGetWSConnectionStatus()
    {
        boolean flgHOStatus = false;
        try
        {
            String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funInvokeMMSWebService";
            URL url = new URL(hoURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output = "", op = "";
            while ((output = br.readLine()) != null)
            {
                op += output;
            }
            System.out.println("HO Conn=" + op);
            conn.disconnect();
            flgHOStatus = Boolean.parseBoolean(op);
        }
        catch (Exception e)
        {
            flgHOStatus = false;
            e.printStackTrace();
        }
        finally
        {
            return flgHOStatus;
        }
    }
    
    
    
    private void funFillPendingSubGroupList()
    {
        Vector vPendingSubGroup=new Vector();
        for(Map.Entry<String,String> entry:hmSubGroup.entrySet())
        {
            vPendingSubGroup.add(entry.getValue());
        }
        
        for(int cnt=0;cnt<tblPlaceOrderItems.getRowCount();cnt++)
        {
            if(Double.parseDouble(tblPlaceOrderItems.getValueAt(cnt, 4).toString())>0)
            {
                String rowSubGroupCode=tblPlaceOrderItems.getValueAt(cnt, 10).toString().trim();
                if(hmSubGroup.containsKey(rowSubGroupCode))
                {
                    vPendingSubGroup.removeElement(hmSubGroup.get(rowSubGroupCode));
                }
            }
        }
        
        listPendingSubGroupNames.removeAll();
        listPendingSubGroupNames.setListData(vPendingSubGroup);
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
        panelMainForm = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        panelFormBody = new javax.swing.JPanel();
        tabPlaceOrder = new javax.swing.JTabbedPane();
        panelItemDtlGrid = new javax.swing.JPanel();
        lblPaxNo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlaceOrderItems = new javax.swing.JTable();
        lblFullfillment = new javax.swing.JLabel();
        lblItemCode = new javax.swing.JLabel();
        lblGeneralOrderDate = new javax.swing.JLabel();
        lblGroupName = new javax.swing.JLabel();
        cmbOrderType = new javax.swing.JComboBox();
        btnPopulateItems = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnPlaceNormalOrder = new javax.swing.JButton();
        cmbGroup = new javax.swing.JComboBox();
        lblOrderType = new javax.swing.JLabel();
        lblSubGroupName = new javax.swing.JLabel();
        cmbSubGroup = new javax.swing.JComboBox();
        btnPopulate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblTotalQty = new javax.swing.JLabel();
        btnSaveNormalOrder = new javax.swing.JButton();
        lblTotalGroupQty = new javax.swing.JLabel();
        lblTotalSubGroupQty = new javax.swing.JLabel();
        btnCloseNormalOrder = new javax.swing.JButton();
        cmbDeliveryDays = new javax.swing.JComboBox();
        panelAdvanceOrderItemGrid = new javax.swing.JPanel();
        lblPaxNo1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPlaceAdvanceOrderItems = new javax.swing.JTable();
        lbAdvanceOrderFullfillment = new javax.swing.JLabel();
        lblItemCode1 = new javax.swing.JLabel();
        lblAdvanceOrderDate = new javax.swing.JLabel();
        btnPlaceAdvOrder = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblTotalAdvOrderQty = new javax.swing.JLabel();
        paneUrgentOrderItemGrid = new javax.swing.JPanel();
        lblPaxNo2 = new javax.swing.JLabel();
        lbUrgentOrderFullfillment = new javax.swing.JLabel();
        lblItemCode2 = new javax.swing.JLabel();
        lblUrgentOrderDate = new javax.swing.JLabel();
        btnPlaceUrgentOrder = new javax.swing.JButton();
        btnUrgentOrderClose = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblTotalUrgentOrderQty = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPlaceUrgentOrderItems = new javax.swing.JTable();
        panelPendingSubGroups = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listPendingSubGroupNames = new javax.swing.JList();
        lblPendingSubGroupNames = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
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
        lblformName.setText("Place Order");
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

        tabPlaceOrder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabPlaceOrderFocusGained(evt);
            }
        });

        panelItemDtlGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtlGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtlGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtlGrid.setMinimumSize(new java.awt.Dimension(809, 590));
        panelItemDtlGrid.setOpaque(false);
        panelItemDtlGrid.setPreferredSize(new java.awt.Dimension(260, 590));
        panelItemDtlGrid.setLayout(null);
        panelItemDtlGrid.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        tblPlaceOrderItems.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPlaceOrderItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sr.No", "Item Code", "Item Name", "Stock", "Order Qty", "Delivery Date", "OrderCode", "SOCode", "WS Prod Code", "Group Name", "SubGroup Name", "ItemCode1", "DelDate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPlaceOrderItems.setRowHeight(30);
        tblPlaceOrderItems.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblPlaceOrderItemsFocusLost(evt);
            }
        });
        tblPlaceOrderItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlaceOrderItemsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblPlaceOrderItemsMouseExited(evt);
            }
        });
        tblPlaceOrderItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPlaceOrderItemsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblPlaceOrderItems);
        if (tblPlaceOrderItems.getColumnModel().getColumnCount() > 0) {
            tblPlaceOrderItems.getColumnModel().getColumn(0).setMinWidth(40);
            tblPlaceOrderItems.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblPlaceOrderItems.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPlaceOrderItems.getColumnModel().getColumn(1).setMinWidth(80);
            tblPlaceOrderItems.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblPlaceOrderItems.getColumnModel().getColumn(1).setMaxWidth(80);
            tblPlaceOrderItems.getColumnModel().getColumn(3).setMinWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(3).setMaxWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(4).setMinWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(4).setMaxWidth(70);
            tblPlaceOrderItems.getColumnModel().getColumn(5).setMinWidth(100);
            tblPlaceOrderItems.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblPlaceOrderItems.getColumnModel().getColumn(5).setMaxWidth(100);
            tblPlaceOrderItems.getColumnModel().getColumn(6).setMinWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(6).setPreferredWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(6).setMaxWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(7).setMinWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(7).setPreferredWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(7).setMaxWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(8).setMinWidth(5);
            tblPlaceOrderItems.getColumnModel().getColumn(8).setPreferredWidth(5);
            tblPlaceOrderItems.getColumnModel().getColumn(8).setMaxWidth(5);
            tblPlaceOrderItems.getColumnModel().getColumn(9).setMinWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(9).setPreferredWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(9).setMaxWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(10).setMinWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(10).setPreferredWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(10).setMaxWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(11).setMinWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(11).setPreferredWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(11).setMaxWidth(2);
            tblPlaceOrderItems.getColumnModel().getColumn(12).setMinWidth(1);
            tblPlaceOrderItems.getColumnModel().getColumn(12).setPreferredWidth(1);
            tblPlaceOrderItems.getColumnModel().getColumn(12).setMaxWidth(1);
        }

        panelItemDtlGrid.add(jScrollPane1);
        jScrollPane1.setBounds(0, 80, 800, 410);

        lblFullfillment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFullfillment.setText("Order Date :");
        panelItemDtlGrid.add(lblFullfillment);
        lblFullfillment.setBounds(320, 0, 80, 30);
        panelItemDtlGrid.add(lblItemCode);
        lblItemCode.setBounds(380, 40, 30, 20);
        panelItemDtlGrid.add(lblGeneralOrderDate);
        lblGeneralOrderDate.setBounds(400, 0, 140, 30);

        lblGroupName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName.setText("Group   :");
        panelItemDtlGrid.add(lblGroupName);
        lblGroupName.setBounds(10, 40, 60, 30);

        cmbOrderType.setToolTipText("Select POS");
        panelItemDtlGrid.add(cmbOrderType);
        cmbOrderType.setBounds(90, 0, 140, 30);

        btnPopulateItems.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPopulateItems.setForeground(new java.awt.Color(255, 255, 255));
        btnPopulateItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopulateItems.setText("Submit");
        btnPopulateItems.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopulateItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPopulateItemsMouseClicked(evt);
            }
        });
        panelItemDtlGrid.add(btnPopulateItems);
        btnPopulateItems.setBounds(710, 40, 80, 30);

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(btnPrint);
        btnPrint.setBounds(270, 500, 80, 30);

        btnPlaceNormalOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPlaceNormalOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnPlaceNormalOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPlaceNormalOrder.setText("PLACE ORDER");
        btnPlaceNormalOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlaceNormalOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPlaceNormalOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaceNormalOrderActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(btnPlaceNormalOrder);
        btnPlaceNormalOrder.setBounds(10, 500, 120, 30);

        cmbGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGroupActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(cmbGroup);
        cmbGroup.setBounds(70, 40, 160, 30);

        lblOrderType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderType.setText("Order Type :");
        panelItemDtlGrid.add(lblOrderType);
        lblOrderType.setBounds(10, 0, 80, 30);

        lblSubGroupName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubGroupName.setText("Sub Group  :");
        panelItemDtlGrid.add(lblSubGroupName);
        lblSubGroupName.setBounds(320, 40, 80, 30);

        cmbSubGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubGroupActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(cmbSubGroup);
        cmbSubGroup.setBounds(400, 40, 220, 30);

        btnPopulate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPopulate.setForeground(new java.awt.Color(255, 255, 255));
        btnPopulate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopulate.setText("POPULATE");
        btnPopulate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopulate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPopulate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPopulateMouseClicked(evt);
            }
        });
        btnPopulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopulateActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(btnPopulate);
        btnPopulate.setBounds(360, 500, 110, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Totals   :");
        panelItemDtlGrid.add(jLabel1);
        jLabel1.setBounds(580, 500, 60, 30);

        lblTotalQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalQty.setText("0");
        panelItemDtlGrid.add(lblTotalQty);
        lblTotalQty.setBounds(650, 500, 140, 30);

        btnSaveNormalOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveNormalOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveNormalOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSaveNormalOrder.setText("SAVE ORDER");
        btnSaveNormalOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveNormalOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSaveNormalOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveNormalOrderMouseClicked(evt);
            }
        });
        btnSaveNormalOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNormalOrderActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(btnSaveNormalOrder);
        btnSaveNormalOrder.setBounds(140, 500, 120, 30);

        lblTotalGroupQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotalGroupQty.setText(" ");
        panelItemDtlGrid.add(lblTotalGroupQty);
        lblTotalGroupQty.setBounds(240, 40, 60, 30);

        lblTotalSubGroupQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panelItemDtlGrid.add(lblTotalSubGroupQty);
        lblTotalSubGroupQty.setBounds(630, 40, 70, 30);

        btnCloseNormalOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCloseNormalOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnCloseNormalOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCloseNormalOrder.setText("CLOSE");
        btnCloseNormalOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseNormalOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCloseNormalOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseNormalOrderMouseClicked(evt);
            }
        });
        btnCloseNormalOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseNormalOrderActionPerformed(evt);
            }
        });
        panelItemDtlGrid.add(btnCloseNormalOrder);
        btnCloseNormalOrder.setBounds(480, 500, 80, 30);

        cmbDeliveryDays.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbDeliveryDays.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        panelItemDtlGrid.add(cmbDeliveryDays);
        cmbDeliveryDays.setBounds(706, 0, 80, 30);

        tabPlaceOrder.addTab("General", panelItemDtlGrid);

        panelAdvanceOrderItemGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelAdvanceOrderItemGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelAdvanceOrderItemGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelAdvanceOrderItemGrid.setMinimumSize(new java.awt.Dimension(800, 590));
        panelAdvanceOrderItemGrid.setOpaque(false);
        panelAdvanceOrderItemGrid.setPreferredSize(new java.awt.Dimension(260, 590));
        panelAdvanceOrderItemGrid.setRequestFocusEnabled(false);
        panelAdvanceOrderItemGrid.setLayout(null);
        panelAdvanceOrderItemGrid.add(lblPaxNo1);
        lblPaxNo1.setBounds(290, 20, 0, 0);

        tblPlaceAdvanceOrderItems.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPlaceAdvanceOrderItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPlaceAdvanceOrderItems.setRowHeight(30);
        tblPlaceAdvanceOrderItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlaceAdvanceOrderItemsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblPlaceAdvanceOrderItemsMouseExited(evt);
            }
        });
        tblPlaceAdvanceOrderItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPlaceAdvanceOrderItemsKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblPlaceAdvanceOrderItems);

        panelAdvanceOrderItemGrid.add(jScrollPane2);
        jScrollPane2.setBounds(0, 50, 800, 400);

        lbAdvanceOrderFullfillment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbAdvanceOrderFullfillment.setText("Order Date :");
        panelAdvanceOrderItemGrid.add(lbAdvanceOrderFullfillment);
        lbAdvanceOrderFullfillment.setBounds(10, 10, 70, 30);
        panelAdvanceOrderItemGrid.add(lblItemCode1);
        lblItemCode1.setBounds(380, 40, 30, 20);
        panelAdvanceOrderItemGrid.add(lblAdvanceOrderDate);
        lblAdvanceOrderDate.setBounds(90, 10, 140, 30);

        btnPlaceAdvOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPlaceAdvOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnPlaceAdvOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPlaceAdvOrder.setText("PLACE ORDER");
        btnPlaceAdvOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlaceAdvOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPlaceAdvOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaceAdvOrderActionPerformed(evt);
            }
        });
        panelAdvanceOrderItemGrid.add(btnPlaceAdvOrder);
        btnPlaceAdvOrder.setBounds(460, 490, 140, 40);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
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
        panelAdvanceOrderItemGrid.add(btnClose);
        btnClose.setBounds(630, 490, 93, 40);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Total Qty    :");
        panelAdvanceOrderItemGrid.add(jLabel2);
        jLabel2.setBounds(540, 450, 90, 30);

        lblTotalAdvOrderQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalAdvOrderQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelAdvanceOrderItemGrid.add(lblTotalAdvOrderQty);
        lblTotalAdvOrderQty.setBounds(640, 450, 130, 30);

        tabPlaceOrder.addTab("Advance Order", panelAdvanceOrderItemGrid);

        paneUrgentOrderItemGrid.setBackground(new java.awt.Color(255, 255, 255));
        paneUrgentOrderItemGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        paneUrgentOrderItemGrid.setForeground(new java.awt.Color(254, 184, 80));
        paneUrgentOrderItemGrid.setMinimumSize(new java.awt.Dimension(800, 590));
        paneUrgentOrderItemGrid.setOpaque(false);
        paneUrgentOrderItemGrid.setPreferredSize(new java.awt.Dimension(260, 590));
        paneUrgentOrderItemGrid.setRequestFocusEnabled(false);
        paneUrgentOrderItemGrid.setLayout(null);
        paneUrgentOrderItemGrid.add(lblPaxNo2);
        lblPaxNo2.setBounds(290, 20, 0, 0);

        lbUrgentOrderFullfillment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbUrgentOrderFullfillment.setText("Order Date :");
        paneUrgentOrderItemGrid.add(lbUrgentOrderFullfillment);
        lbUrgentOrderFullfillment.setBounds(10, 10, 70, 30);
        paneUrgentOrderItemGrid.add(lblItemCode2);
        lblItemCode2.setBounds(380, 40, 30, 20);
        paneUrgentOrderItemGrid.add(lblUrgentOrderDate);
        lblUrgentOrderDate.setBounds(90, 10, 140, 30);

        btnPlaceUrgentOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPlaceUrgentOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnPlaceUrgentOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPlaceUrgentOrder.setText("PLACE ORDER");
        btnPlaceUrgentOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlaceUrgentOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPlaceUrgentOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaceUrgentOrderActionPerformed(evt);
            }
        });
        paneUrgentOrderItemGrid.add(btnPlaceUrgentOrder);
        btnPlaceUrgentOrder.setBounds(460, 490, 140, 40);

        btnUrgentOrderClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUrgentOrderClose.setForeground(new java.awt.Color(255, 255, 255));
        btnUrgentOrderClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnUrgentOrderClose.setText("CLOSE");
        btnUrgentOrderClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUrgentOrderClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnUrgentOrderClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUrgentOrderCloseMouseClicked(evt);
            }
        });
        btnUrgentOrderClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUrgentOrderCloseActionPerformed(evt);
            }
        });
        paneUrgentOrderItemGrid.add(btnUrgentOrderClose);
        btnUrgentOrderClose.setBounds(630, 490, 93, 40);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Total Qty    :");
        paneUrgentOrderItemGrid.add(jLabel3);
        jLabel3.setBounds(540, 450, 90, 30);

        lblTotalUrgentOrderQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalUrgentOrderQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paneUrgentOrderItemGrid.add(lblTotalUrgentOrderQty);
        lblTotalUrgentOrderQty.setBounds(640, 450, 130, 30);

        tblPlaceUrgentOrderItems.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPlaceUrgentOrderItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPlaceUrgentOrderItems.setRowHeight(30);
        tblPlaceUrgentOrderItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlaceUrgentOrderItemsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblPlaceUrgentOrderItemsMouseExited(evt);
            }
        });
        tblPlaceUrgentOrderItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPlaceUrgentOrderItemsKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblPlaceUrgentOrderItems);

        paneUrgentOrderItemGrid.add(jScrollPane3);
        jScrollPane3.setBounds(0, 50, 800, 400);

        tabPlaceOrder.addTab("Urgent Order", paneUrgentOrderItemGrid);

        panelPendingSubGroups.setBackground(new java.awt.Color(255, 255, 255));

        listPendingSubGroupNames.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(listPendingSubGroupNames);

        lblPendingSubGroupNames.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPendingSubGroupNames.setText("Pending Sub Groups");

        javax.swing.GroupLayout panelPendingSubGroupsLayout = new javax.swing.GroupLayout(panelPendingSubGroups);
        panelPendingSubGroups.setLayout(panelPendingSubGroupsLayout);
        panelPendingSubGroupsLayout.setHorizontalGroup(
            panelPendingSubGroupsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPendingSubGroupsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panelPendingSubGroupsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPendingSubGroupNames, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        panelPendingSubGroupsLayout.setVerticalGroup(
            panelPendingSubGroupsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPendingSubGroupsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPendingSubGroupNames, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabPlaceOrder.addTab("Pending Sub Groups", panelPendingSubGroups);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabPlaceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 805, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tabPlaceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 571, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        funHomeButtonClicked();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnPlaceAdvOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaceAdvOrderActionPerformed
        // TODO add your handling code here:
        try
        {
            if(tblPlaceAdvanceOrderItems.getRowCount()>0)
            {
                int res = JOptionPane.showConfirmDialog(null, "Do you want to proceed to place order?");
                if (res == 0)
                {
                    if(funGetWSConnectionStatus())
                    {
                        btnPlaceAdvOrder.setEnabled(false);
                        funAdvancePlaceOrder();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Could not connect to MMS Server!!!");
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Enter Items to Place Advance Order!!!");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPlaceAdvOrderActionPerformed

    private void tblPlaceAdvanceOrderItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlaceAdvanceOrderItemsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceAdvanceOrderItemsKeyPressed

    private void tblPlaceAdvanceOrderItemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceAdvanceOrderItemsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceAdvanceOrderItemsMouseExited

    private void tblPlaceAdvanceOrderItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceAdvanceOrderItemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceAdvanceOrderItemsMouseClicked

    private void btnPlaceNormalOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaceNormalOrderActionPerformed
        // TODO add your handling code here:
        try
        {
            if(tblPlaceOrderItems.getRowCount()>0)
            {
                int res = JOptionPane.showConfirmDialog(null, "Do you want to proceed to place order?");
                if (res == 0)
                {
                    if(funGetWSConnectionStatus())
                    {
                        btnPlaceNormalOrder.setEnabled(false);
                        funPlaceOrder();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Could not connect to MMS Server!!!");
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Enter Items to Place Order!!!");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPlaceNormalOrderActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
        try
        {
            funPrintOrder();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnPopulateItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPopulateItemsMouseClicked
        // TODO add your handling code here:
        if(hmOrder.size()>0)
        {
            funFillPOSWSItems();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please Check Order Master!!!");
        }
    }//GEN-LAST:event_btnPopulateItemsMouseClicked

    private void tblPlaceOrderItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlaceOrderItemsKeyPressed
        // TODO add your handling code here:
        funCalculateTotalQty();
    }//GEN-LAST:event_tblPlaceOrderItemsKeyPressed

    private void tblPlaceOrderItemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceOrderItemsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceOrderItemsMouseExited

    private void tblPlaceOrderItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceOrderItemsMouseClicked
        // TODO add your handling code here:
        funCalculateTotalQty();
    }//GEN-LAST:event_tblPlaceOrderItemsMouseClicked

    private void cmbGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGroupActionPerformed
        // TODO add your handling code here:        
        try
        {
            String groupCode=cmbGroup.getSelectedItem().toString();
            groupCode=groupCode.substring(groupCode.length()-8, groupCode.length()).trim();
            funFillSubGroupComboBox(groupCode);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbGroupActionPerformed

    private void btnPopulateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPopulateMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnPopulateMouseClicked

    private void btnPopulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopulateActionPerformed
        // TODO add your handling code here:
        try
        {
            funFillNormalOrderGridForLastOrder();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPopulateActionPerformed

    private void tblPlaceOrderItemsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPlaceOrderItemsFocusLost
        // TODO add your handling code here:        
    }//GEN-LAST:event_tblPlaceOrderItemsFocusLost

    private void btnSaveNormalOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNormalOrderActionPerformed
        // TODO add your handling code here:
        try
        {
            long diffTime = funTimeDiff(orderUpToTime);
            if(diffTime>=0)
            {
                btnSaveNormalOrder.setEnabled(false);
                funSaveOrder();
                funResetFields();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Check order Master for up To Time !!!");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveNormalOrderActionPerformed

    private void btnSaveNormalOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveNormalOrderMouseClicked
        // TODO add your handling code here:
        try
        {
            funSaveOrder();
            funFillNormalOrderGrid();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveNormalOrderMouseClicked

    private void btnPlaceUrgentOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaceUrgentOrderActionPerformed
        // TODO add your handling code here:
        try
        {
            if(tblPlaceUrgentOrderItems.getRowCount()>0)
            {
                int res = JOptionPane.showConfirmDialog(null, "Do you want to proceed to place order?");
                if (res == 0)                
                {
                    long diffTime = funTimeDiff(orderUpToTime);
                    if(diffTime>=0)
                    {
                        if(funGetWSConnectionStatus())
                        {
                            btnPlaceUrgentOrder.setEnabled(false);
                            funPlaceUrgentOrder();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Could not connect to MMS Server!!!");
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Check order Master for up To Time !!!");
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Enter Items to Place Urgent OrderEnter Items to Place Advance Order!!!");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPlaceUrgentOrderActionPerformed

    private void btnUrgentOrderCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUrgentOrderCloseMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUrgentOrderCloseMouseClicked

    private void btnUrgentOrderCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUrgentOrderCloseActionPerformed
        // TODO add your handling code here:
        funHomeButtonClicked();
    }//GEN-LAST:event_btnUrgentOrderCloseActionPerformed

    private void tblPlaceUrgentOrderItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceUrgentOrderItemsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceUrgentOrderItemsMouseClicked

    private void tblPlaceUrgentOrderItemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlaceUrgentOrderItemsMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceUrgentOrderItemsMouseExited

    private void tblPlaceUrgentOrderItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlaceUrgentOrderItemsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlaceUrgentOrderItemsKeyPressed

    private void cmbSubGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubGroupActionPerformed
        // TODO add your handling code here:
        if(cmbSubGroup.getItemCount()>0)
        {
            funCalculateTotalQty();
        }
    }//GEN-LAST:event_cmbSubGroupActionPerformed

    private void btnCloseNormalOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseNormalOrderMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseNormalOrderMouseClicked

    private void btnCloseNormalOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseNormalOrderActionPerformed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Place Order");
        dispose();
    }//GEN-LAST:event_btnCloseNormalOrderActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Place Order");
    }//GEN-LAST:event_formWindowClosed

    private void tabPlaceOrderFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabPlaceOrderFocusGained
        // TODO add your handling code here:
        funFillPendingSubGroupList();
    }//GEN-LAST:event_tabPlaceOrderFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCloseNormalOrder;
    private javax.swing.JButton btnPlaceAdvOrder;
    private javax.swing.JButton btnPlaceNormalOrder;
    private javax.swing.JButton btnPlaceUrgentOrder;
    private javax.swing.JButton btnPopulate;
    private javax.swing.JButton btnPopulateItems;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSaveNormalOrder;
    private javax.swing.JButton btnUrgentOrderClose;
    private javax.swing.JComboBox cmbDeliveryDays;
    private javax.swing.JComboBox cmbGroup;
    private javax.swing.JComboBox cmbOrderType;
    private javax.swing.JComboBox cmbSubGroup;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbAdvanceOrderFullfillment;
    private javax.swing.JLabel lbUrgentOrderFullfillment;
    private javax.swing.JLabel lblAdvanceOrderDate;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFullfillment;
    private javax.swing.JLabel lblGeneralOrderDate;
    private javax.swing.JLabel lblGroupName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemCode;
    private javax.swing.JLabel lblItemCode1;
    private javax.swing.JLabel lblItemCode2;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOrderType;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPaxNo1;
    private javax.swing.JLabel lblPaxNo2;
    private javax.swing.JLabel lblPendingSubGroupNames;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSubGroupName;
    private javax.swing.JLabel lblTotalAdvOrderQty;
    private javax.swing.JLabel lblTotalGroupQty;
    private javax.swing.JLabel lblTotalQty;
    private javax.swing.JLabel lblTotalSubGroupQty;
    private javax.swing.JLabel lblTotalUrgentOrderQty;
    private javax.swing.JLabel lblUrgentOrderDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JList listPendingSubGroupNames;
    private javax.swing.JPanel paneUrgentOrderItemGrid;
    private javax.swing.JPanel panelAdvanceOrderItemGrid;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtlGrid;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelPendingSubGroups;
    private javax.swing.JTabbedPane tabPlaceOrder;
    private javax.swing.JTable tblPlaceAdvanceOrderItems;
    private javax.swing.JTable tblPlaceOrderItems;
    private javax.swing.JTable tblPlaceUrgentOrderItems;
    // End of variables declaration//GEN-END:variables

}
    
