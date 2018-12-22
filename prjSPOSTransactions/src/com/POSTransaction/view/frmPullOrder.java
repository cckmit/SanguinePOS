/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.gLastModifiedDate;
import static com.POSGlobal.controller.clsGlobalVarClass.gPropertyCode;
import static com.POSGlobal.controller.clsGlobalVarClass.gSanguineWebServiceURL;
import com.POSGlobal.controller.clsStockInDtl;
import com.POSGlobal.controller.clsStockInHd;
import com.POSGlobal.controller.clsUtility;
import com.POSTransaction.controller.clsSalesReturn;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class frmPullOrder extends javax.swing.JFrame 
{
    clsUtility objUtility;
    
    public frmPullOrder(){
    
        initComponents();
        objUtility=new clsUtility();
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        try
        {
             funGetInvoiceDetails();  
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
        
    }

    
    
//Function to go to home after clicking on home button
    private void funHomeButtonClicked()
    {
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Pull Order");
    }

    
    
    private void funResetFields()
    {
        DefaultTableModel dm=(DefaultTableModel)tblOrderItems.getModel();
        dm.setRowCount(0);
        tblOrderItems.setModel(dm);
        
    }
    
    
    private void funPullOrder(String invoiceCode) throws Exception
    {
        String fetchMasterURL = gSanguineWebServiceURL + "/MMSIntegration/funPullOrder"
            + "?InvoiceCode="+invoiceCode+"&ClientCode=" + clsGlobalVarClass.gClientCode;
        URL url = new URL(fetchMasterURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        String output = "", jsonString = "";
        while ((output = br.readLine()) != null)
        {
            jsonString += output;
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jObj = (JSONObject) obj;

        DefaultTableModel dm=(DefaultTableModel) tblOrderItems.getModel();
        dm.setRowCount(0);
        
        Iterator<Object> it = jObj.keySet().iterator();
        if (it.hasNext())
        {
            JSONArray mJsonArray = null;
            mJsonArray = (JSONArray) jObj.get("OrderRec");
            for(int cnt=0;cnt<mJsonArray.size();cnt++)
            {
                JSONObject rows=(JSONObject)mJsonArray.get(cnt);
                String itemName="",itemCode="";
                String sql="select a.strItemName,a.strItemCode from tblitemmaster a,tblitemmasterlinkupdtl b "
                    + " where a.strItemCode=b.strItemCode and b.strWSProductCode='"+rows.get("ProductCode")+"'";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if(rs.next())
                {
                    itemName=rs.getString(1);
                    itemCode=rs.getString(2);
                }
                rs.close();
                Object[] arrObjRows={itemName,rows.get("Qty"),rows.get("Qty"),"",itemCode,rows.get("ProductCode")};
                dm.addRow(arrObjRows);
            }
            tblOrderItems.setModel(dm);
        }
    }

    
    private void funGenerateStockInEntry(String invoiceCode,String soCode) throws Exception
    {
        String reasonCode="";
        boolean flgReason=false;
        String sqlStockInReasonCode="select strReasonCode from tblreasonmaster "
            + " where strStkIn='Y' ";
        ResultSet rsStockInReasonCode=clsGlobalVarClass.dbMysql.executeResultSet(sqlStockInReasonCode);
        if(rsStockInReasonCode.next())
        {
            flgReason=true;
            reasonCode=rsStockInReasonCode.getString(1);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please Create Reason for Stock In!!!");
        }
        rsStockInReasonCode.close();
        
        if(flgReason)
        {
            Map<String,clsSalesReturn> hmSalesReturnData=new HashMap<String,clsSalesReturn>();
            frmStkIn objStkIn=new frmStkIn();
            String stockInCode=objStkIn.funGenerateStockInCode();
            clsStockInHd objStockInHd =new clsStockInHd();
            objStockInHd.setStrStkInCode(stockInCode);
            objStockInHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
            objStockInHd.setDteStkInDate(clsGlobalVarClass.gPOSDateForTransaction);
            objStockInHd.setStrReasonCode(reasonCode);
            objStockInHd.setStrPurchaseBillNo(invoiceCode);
            objStockInHd.setDtePurchaseBillDate(clsGlobalVarClass.gPOSDateForTransaction);
            objStockInHd.setIntShiftCode(0);
            objStockInHd.setStrUserCreated(lblUserCode.getText());
            objStockInHd.setStrUserEdited(lblUserCode.getText());
            objStockInHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
            objStockInHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
            objStockInHd.setStrClientCode(clsGlobalVarClass.gClientCode);
            objStockInHd.setStrInvoiceCode(invoiceCode);

            Map<String,clsStockInDtl> hmStockInDtl= new HashMap<String,clsStockInDtl>();
            for(int cnt=0;cnt<tblOrderItems.getRowCount();cnt++)
            {
                clsStockInDtl objStockInDtl =new clsStockInDtl();
                String itemCode=tblOrderItems.getValueAt(cnt, 4).toString();
                String productCode=tblOrderItems.getValueAt(cnt, 5).toString();
                double actualQty=Double.parseDouble(tblOrderItems.getValueAt(cnt, 1).toString());
                double receivedQty=Double.parseDouble(tblOrderItems.getValueAt(cnt, 2).toString());
                String remarks=tblOrderItems.getValueAt(cnt, 3).toString();
                objStockInDtl.setStrItemCode(itemCode);
                objStockInDtl.setStrItemName(tblOrderItems.getValueAt(cnt, 0).toString());
                objStockInDtl.setStrStkInCode(stockInCode);
                objStockInDtl.setDblAmount(0);
                objStockInDtl.setDblQuantity(receivedQty);
                objStockInDtl.setDblPurchaseRate(0);
                objStockInDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
                objStockInDtl.setStrDataPostFlag("N");
                hmStockInDtl.put(itemCode,objStockInDtl);
                double variance=actualQty-receivedQty;
                if(variance>0)
                {
                    clsSalesReturn objSalesReturn = new clsSalesReturn();
                    objSalesReturn.setProductCode(productCode);
                    objSalesReturn.setRemarks(remarks);
                    objSalesReturn.setReturnQty(variance);
                    hmSalesReturnData.put(productCode,objSalesReturn);
                }
            }
            objStkIn.funInsertStockInDataTable(hmStockInDtl, objStockInHd);

            /*
            String sql="update tblplaceorderhd set strCloseSO='Y', strDCCode='"+invoiceCode+"' "
                + " where strSOCode='"+soCode+"'";
            int afftectedRows=clsGlobalVarClass.dbMysql.execute(sql);
            if(afftectedRows<1)
            {
                int i=0;
                String orderCode=funGenerateOrderCode();
                String sqlInsertPlaceOrderDtl="insert into tblplaceorderdtl "
                    + "(strOrderCode,strProductCode,strItemCode,dblQty,dblStockQty,strClientCode,strAdvOrderNo) "
                    + "values ";
                for(int cnt=0;cnt<tblOrderItems.getRowCount();cnt++)
                {
                    String itemCode=tblOrderItems.getValueAt(cnt, 4).toString();
                    String productCode=tblOrderItems.getValueAt(cnt, 5).toString();
                    double actualQty=Double.parseDouble(tblOrderItems.getValueAt(cnt, 1).toString());
                    double receivedQty=Double.parseDouble(tblOrderItems.getValueAt(cnt, 2).toString());
                    String remarks=tblOrderItems.getValueAt(cnt, 3).toString();
                    if(i==0)
                    {
                        sqlInsertPlaceOrderDtl+="('"+orderCode+"','"+productCode+"','"+itemCode+"','"+receivedQty+"'"
                            + ",'0','"+clsGlobalVarClass.gClientCode+"','')";
                    }
                    else
                    {
                        sqlInsertPlaceOrderDtl+=",('"+orderCode+"','"+productCode+"','"+itemCode+"','"+receivedQty+"'"
                        + ",'0','"+clsGlobalVarClass.gClientCode+"','')";
                    }
                    i++;
                }

                if(i>0)
                {
                     clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderDtl);
                }
                String sqlInsertPlaceOrderHd="insert into tblplaceorderhd "
                    + "(strOrderCode,strSOCode,dteSODate,dteOrderDate,strUserCreated,dteDateCreated,strClientCode "
                    + ",strCloseSO,strDCCode,strOrderType) values "
                    + "('"+orderCode+"','"+soCode+"','"+clsGlobalVarClass.gPOSDateForTransaction+"','"+clsGlobalVarClass.gPOSDateForTransaction+"','"+clsGlobalVarClass.gUserCode+"'"
                    + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','Y','"+invoiceCode+"','Normal-Direct')";
                clsGlobalVarClass.dbMysql.execute(sqlInsertPlaceOrderHd); 
            }*/

            if(hmSalesReturnData.size()>0)
            {
                funSendSalesReturnData(hmSalesReturnData,invoiceCode);
            }
            JOptionPane.showMessageDialog(null, "Stock In Code : "+stockInCode);
            funGetInvoiceDetails();
            funResetFields();
        }
    }
    
    
    private int funSendSalesReturnData(Map<String,clsSalesReturn> hmSalesReturnData,String invoiceCode) throws Exception
    {
        int ret=0;
        String locCode="";
        String sql="select strWSLocationCode from tblposmaster where strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
        ResultSet rsLocCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsLocCode.next())
        {
            locCode=rsLocCode.getString(1);
        }
        rsLocCode.close();
        
        JSONObject jObj=new JSONObject();
        JSONArray jArrSalesData=new JSONArray();
        for (Map.Entry<String,clsSalesReturn> entry : hmSalesReturnData.entrySet()) {
		
            JSONObject jObjItemDtl=new JSONObject();
            jObjItemDtl.put("ProductCode", entry.getValue().getProductCode());
            jObjItemDtl.put("ReturnQty", entry.getValue().getReturnQty());
            jObjItemDtl.put("Remarks", entry.getValue().getRemarks());
            jArrSalesData.add(jObjItemDtl);
        }
        
        jObj.put("SaleReturnData", jArrSalesData);
        jObj.put("SaleReturnDate", clsGlobalVarClass.gPOSDateForTransaction);
        jObj.put("LocCode", locCode);
        jObj.put("InvoiceCode", invoiceCode);
        jObj.put("ClientCode", clsGlobalVarClass.gClientCode);
        
        String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funInsertSalesReturn";
        URL url = new URL(hoURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        OutputStream os = conn.getOutputStream();
        os.write(jObj.toString().getBytes());
        os.flush();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
        {
            throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String SRCode="";
        String output = "", op = "Updated successfully: ";
        while ((output = br.readLine()) != null)
        {
            SRCode=output;
            op +=output;
        }
        return ret;
    }
    
    
    private void funGetInvoiceDetails() throws Exception
    {
        String []posdate=clsGlobalVarClass.gPOSDateForTransaction.split(" ");
        Map<String,String> hmInvCode=new HashMap<String,String>();
        String sql="select strInvoiceCode from tblstkinhd a "
            + " where date(a.dteStkInDate)='"+posdate[0]+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
        ResultSet rsInvCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rsInvCode.next())
        {
            hmInvCode.put(rsInvCode.getString(1),rsInvCode.getString(1));
        }
        rsInvCode.close();
        
        String fetchMasterURL = gSanguineWebServiceURL + "/MMSIntegration/funGetInvoiceData"
            + "?InvoiceDate="+posdate[0]+"&ClientCode=" + clsGlobalVarClass.gClientCode+"&WSClientCode=" + clsGlobalVarClass.gWSClientCode;
        URL url = new URL(fetchMasterURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        String output = "", jsonString = "";
        while ((output = br.readLine()) != null)
        {
            jsonString += output;
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        JSONObject jObj = (JSONObject) obj;

        DefaultTableModel dm=(DefaultTableModel) tblInvoiceDtls.getModel();
        dm.setRowCount(0);
        tblInvoiceDtls.setRowHeight(20);
        
        Iterator<Object> it = jObj.keySet().iterator();
        if (it.hasNext())
        {
            JSONArray mJsonArray = null;
            mJsonArray = (JSONArray) jObj.get("InvoiceDtl");
            for(int cnt=0;cnt<mJsonArray.size();cnt++)
            {
                JSONObject rows=(JSONObject)mJsonArray.get(cnt);
                if(!hmInvCode.containsKey(rows.get("InvoiveCode")))
                {
                    Object[] arrObjRows={rows.get("InvoiveCode"),rows.get("InvoiceDate"),rows.get("SOCode"),rows.get("CustomerName")};
                    dm.addRow(arrObjRows);
                }
            }
            tblInvoiceDtls.setModel(dm);
        }
    }
    
    
    private void funGetSelectedInvoice(java.awt.event.MouseEvent evt)
    {
        int row = tblInvoiceDtls.getSelectedRow();
        String InvoiceCode= tblInvoiceDtls.getValueAt(row, 0).toString();
        try
        {
            if (evt.getClickCount() == 1)
            {
                funPullOrder(InvoiceCode);
            }
            else if(evt.getClickCount() == 2)
            {
                funPullOrder(InvoiceCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
    }
    
    private String funGenerateOrderCode()throws Exception
    {
        long lastNo = 0;
        String orderCode="";
        String sql = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='PlaceOrder'";
        ResultSet rsOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsOrderCode.next()) {
            lastNo = rsOrderCode.getLong(2);
            lastNo = lastNo + 1;
            rsOrderCode.close();
            orderCode = "OC" + String.format("%07d", lastNo);
            sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='PlaceOrder'";
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        rsOrderCode.close();
        return orderCode;
    }
     
    
    private void funGenerateStockInButtonPressed()
    {
        try
        {
            int row =tblInvoiceDtls.getSelectedRow();
            if(row>-1)
            {
                String InvoiceCode=tblInvoiceDtls.getValueAt(row, 0).toString();
                funGenerateStockInEntry(InvoiceCode,tblInvoiceDtls.getValueAt(row, 2).toString());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please Select Invoice from Grid!!!");
            }
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
        panelMainForm = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelOperationalButtons = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnGenerateStockIn = new javax.swing.JButton();
        panelItemDtlGrid = new javax.swing.JPanel();
        lblPaxNo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrderItems = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInvoiceDtls = new javax.swing.JTable();

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

        panelOperationalButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelOperationalButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelOperationalButtons.setOpaque(false);

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setText("CLOSE");
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnGenerateStockIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerateStockIn.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateStockIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGenerateStockIn.setText("<html>GENERATE<br> STOCK IN</html>");
        btnGenerateStockIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGenerateStockIn.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGenerateStockIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateStockInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperationalButtonsLayout = new javax.swing.GroupLayout(panelOperationalButtons);
        panelOperationalButtons.setLayout(panelOperationalButtonsLayout);
        panelOperationalButtonsLayout.setHorizontalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(btnGenerateStockIn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(362, Short.MAX_VALUE))
        );
        panelOperationalButtonsLayout.setVerticalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnGenerateStockIn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        panelItemDtlGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtlGrid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtlGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtlGrid.setOpaque(false);
        panelItemDtlGrid.setPreferredSize(new java.awt.Dimension(260, 600));

        tblOrderItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Actual Qty", "Received Qty", "Remarks", "Item Code", "WS Prod Code"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOrderItems.setRowHeight(30);
        jScrollPane1.setViewportView(tblOrderItems);
        if (tblOrderItems.getColumnModel().getColumnCount() > 0) {
            tblOrderItems.getColumnModel().getColumn(1).setMinWidth(70);
            tblOrderItems.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblOrderItems.getColumnModel().getColumn(1).setMaxWidth(70);
            tblOrderItems.getColumnModel().getColumn(2).setMinWidth(70);
            tblOrderItems.getColumnModel().getColumn(2).setPreferredWidth(70);
            tblOrderItems.getColumnModel().getColumn(2).setMaxWidth(70);
            tblOrderItems.getColumnModel().getColumn(4).setMinWidth(5);
            tblOrderItems.getColumnModel().getColumn(4).setMaxWidth(5);
            tblOrderItems.getColumnModel().getColumn(5).setMinWidth(5);
            tblOrderItems.getColumnModel().getColumn(5).setPreferredWidth(5);
            tblOrderItems.getColumnModel().getColumn(5).setMaxWidth(5);
        }

        tblInvoiceDtls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoive Code", "Invoice Date", "SO Code", "Customer Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoiceDtls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceDtlsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblInvoiceDtls);

        javax.swing.GroupLayout panelItemDtlGridLayout = new javax.swing.GroupLayout(panelItemDtlGrid);
        panelItemDtlGrid.setLayout(panelItemDtlGridLayout);
        panelItemDtlGridLayout.setHorizontalGroup(
            panelItemDtlGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemDtlGridLayout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(lblPaxNo))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        panelItemDtlGridLayout.setVerticalGroup(
            panelItemDtlGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemDtlGridLayout.createSequentialGroup()
                .addGroup(panelItemDtlGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelItemDtlGridLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblPaxNo)))
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addComponent(panelItemDtlGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        funHomeButtonClicked();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnGenerateStockInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateStockInActionPerformed
        // TODO add your handling code here:
        funGenerateStockInButtonPressed();
    }//GEN-LAST:event_btnGenerateStockInActionPerformed

    private void tblInvoiceDtlsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceDtlsMouseClicked
        // TODO add your handling code here:
        funGetSelectedInvoice(evt);
    }//GEN-LAST:event_tblInvoiceDtlsMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Pull Order");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Pull Order");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerateStockIn;
    private javax.swing.JButton btnHome;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtlGrid;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOperationalButtons;
    private javax.swing.JTable tblInvoiceDtls;
    private javax.swing.JTable tblOrderItems;
    // End of variables declaration//GEN-END:variables

}
    
    
