/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class frmJioMoneyRefund extends javax.swing.JFrame 
{
    clsUtility objUtility;
    private String[] arrReasonCode;
    private String sql="";    
    public frmJioMoneyRefund(){
    
        initComponents();
        objUtility=new clsUtility();
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        fillComboBox();
       
        
    }
    
    public void funRefundAmount()
    {
        clsUtility obj =new clsUtility();
        obj.funStartSocketBat();
        try
        {
            String RequestType="1002";
            String imei="111111111111111";
            String userName="9820001759";
            String issueRefund="Y";
            String Amount= txtRefundAmount.getText();
            String rrno= txtRefundRefNo.getText();
            String requestData = "requestType=" + RequestType +
            "&mid=" + clsGlobalVarClass.gJioMoneyMID +
            "&amount=" + Amount +
            "&tid=" + clsGlobalVarClass.gJioMoneyTID +
            "&rrno=" + rrno +
            "&imei=" + imei +
            "&issueRefund=" + issueRefund +
            "&userName=" + userName +
            "&version=V2" + 
            "&dealerId=" + 
            "&dealerSubId=";
                                  
            System.out.println("RequestData : "+requestData);
            String Response="";
            Response =  obj.funMakeTransaction(requestData, RequestType, clsGlobalVarClass.gJioMoneyMID, clsGlobalVarClass.gJioMoneyTID, txtRefundAmount.getText(),"PRE_PROD","localhost","5150");
            System.out.println(Response);

            String strRes = Response.trim();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
            JSONArray lang= (JSONArray) jsonObject.get("result");
            JSONParser jsonParser1 = new JSONParser();
            JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
            String responseCode= (String) jsonObject1.get("responseCode");

            if(null != responseCode)
            {
                if(responseCode.equals("0000"))
                {
                    JOptionPane.showMessageDialog(this, "JioMoney Refund Successful.");
                    funUnsettleBill();
                }
                else
                {
                    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
                }
            } 
        
        }catch (Exception e){
            System.out.println("Exception:" + e);
        }

    }
    
    
    private void funSearchBillNo()
    {
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("SettleBill");
        new frmSearchFormDialog(null, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetData(data);
        }
    }
    
    private void funSetData(Object[] data)
    {
        try
        {
            txtBillNo.setText(String.valueOf(data[0]));
            txtRefundRefNo.setText(String.valueOf(data[1]));
            txtRefundAmount.setText(String.valueOf(data[2]));
        
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

     private void fillComboBox()
    {
        try
        {   
            sql="select count(*) from tblreasonmaster where strUnsettleBill='Y'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cnt=rs.getInt(1);
            rs.close();
            int cntReasonCode=0;
            if(cnt>0)
            {
                arrReasonCode=new String[cnt];
                sql="select strReasonCode,strReasonName from tblreasonmaster where strUnsettleBill='Y'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rs.next())
                {
                    arrReasonCode[cntReasonCode]=rs.getString(1);
                    cmbReason.addItem(rs.getString(2));
                    cntReasonCode++;
                }
                rs.close();
            }
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private void funUnsettleBill()
    {
        try
        {
            if(cmbReason.getItemCount()!=0)
            {
                int index=cmbReason.getSelectedIndex();
                sql="select dteBillDate,dblGrandTotal,strClientCode,strTableNo,strWaiterNo,strAreaCode"
                    + ",strPosCode,strOperationType,strSettelmentMode,intShiftCode "
                    + " from tblbillhd where strBillNo='"+txtBillNo.getText().trim()+"'";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if(rs.next())
                {
                    sql="insert into tblvoidbillhd(strPosCode,strReasonCode,strReasonName,strBillNo,"
                        + "dblActualAmount,dteBillDate,strTransType,dteModifyVoidBill,strTableNo,strWaiterNo,"
                        + "intShiftCode,strUserCreated,strUserEdited,strClientCode)"
                        + " values('"+clsGlobalVarClass.gPOSCode+"','"+arrReasonCode[index]+"','"+cmbReason.getSelectedItem().toString()
                        +"','"+txtBillNo.getText().trim()+"','"+rs.getString(2)+"','"+rs.getString(1)
                        +"','USBill','"+clsGlobalVarClass.getCurrentDateTime()+"','"+rs.getString(4)+"','"+rs.getString(5)
                        +"','"+rs.getString(10)+"','"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.gUserCode
                        +"','"+clsGlobalVarClass.gClientCode+"')";
                    clsGlobalVarClass.dbMysql.execute(sql);
                        
                    sql="select strBillNo from tblbillsettlementdtl a,tblsettelmenthd b "
                        +" where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='"+txtBillNo.getText()+"' "
                        +" and b.strSettelmentType='Debit Card' ";
                    ResultSet rsDebitCardBill=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rsDebitCardBill.next())
                    {
                        sql="select strCardNo,dblTransactionAmt,strPOSCode,dteBillDate "
                            + " from tbldebitcardbilldetails where strBillNo='"+txtBillNo.getText().trim()+"' ";
                        ResultSet rsDebitCardBillDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if(rsDebitCardBillDtl.next())
                        {
                            objUtility.funDebitCardTransaction(txtBillNo.getText().trim(), rsDebitCardBillDtl.getString(1), rsDebitCardBillDtl.getDouble(2), "Unsettle");
                            objUtility.funUpdateDebitCardBalance(rsDebitCardBillDtl.getString(1), rsDebitCardBillDtl.getDouble(2), "Unsettle");
                        }
                        rsDebitCardBillDtl.close();
                    }
                    rsDebitCardBill.close();
                    
                    sql="select b.strSettelmentType from tblbillsettlementdtl a,tblsettelmenthd b "
                        +" where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='"+txtBillNo.getText()+"' ";
                    ResultSet rsSettlementMode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rsSettlementMode.next())
                    {
                        if(rsSettlementMode.getString(1).trim().equals("Complementary"))
                        {
                            funMoveComplimentaryBillToBillDtl(txtBillNo.getText().trim(),rs.getString(7),rs.getString(6),rs.getString(8));
                        }
                    }
                    rsSettlementMode.close();
                    
                    sql="delete from tblbillsettlementdtl where strBillNo='"+txtBillNo.getText()+"'";
                    int unsettleExc=clsGlobalVarClass.dbMysql.execute(sql);
                            
                    sql="update tblbillhd set strDataPostFlag='N' where strBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                            
                    sql="update tblbilldtl set strDataPostFlag='N' where strBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                            
                    sql="update tblbillmodifierdtl set strDataPostFlag='N' where strBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                            
                    sql="update tblbilltaxdtl set strDataPostFlag='N' where strBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    sql="update tblbillseriesbilldtl set strDataPostFlag='N' where strHdBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    sql="update tblbilldiscdtl set strDataPostFlag='N' where strBillNo='"+txtBillNo.getText().trim()+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    if(unsettleExc>0)
                    {
                        new frmOkPopUp(this,"Unsettle Successfully", "Success", 1).setVisible(true);
                        funResetField();
                    }
                }
            }
            else
            {
                new frmOkPopUp(this, "Please Create Reason First", "Warning", 1).setVisible(true);
            }
        }
        catch(Exception e)
        {
            //clsGlobalVarClass.dbMysql.funRollbackTransaction();
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private int funMoveComplimentaryBillToBillDtl(String billNo,String POSCode
        ,String billAreaCode, String operationTypeForTax) throws Exception
    {
        String sqlDelete = "delete from tblbilldtl where strBillNo='" + billNo + "'";
        clsGlobalVarClass.dbMysql.execute(sqlDelete);
        
        String sqlInsertBillComDtl = "insert into tblbilldtl "
                + " select * from tblbillcomplementrydtl where strBillNo='"+billNo+"' ";
        clsGlobalVarClass.dbMysql.execute(sqlInsertBillComDtl);
        
        String sql=" select strItemCode,strItemName,dblAmount,dblDiscountAmt "
                + "from tblbilldtl where strBillNo='"+billNo+"' ";
        double subTotal=0.0;
        Double disTotal=0.0;
    
        List<clsItemDtlForTax> arrListItemDtls=new ArrayList<clsItemDtlForTax>();
        ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while(rs.next())
        {
            clsItemDtlForTax objItemDtl=new clsItemDtlForTax();
            objItemDtl.setItemCode(rs.getString(1));
            objItemDtl.setItemName(rs.getString(2));
            objItemDtl.setAmount(rs.getDouble(3));
            objItemDtl.setDiscAmt(rs.getDouble(4));
            subTotal+=rs.getDouble(3);
            disTotal+=rs.getDouble(4);
            arrListItemDtls.add(objItemDtl);
        }
        double disper=0.00;
        if(subTotal>0)
        {
            disper=(disTotal/subTotal)*100;
        }
        clsUtility obj=new clsUtility();
        List<clsTaxCalculationDtls> arrListTaxCal = obj.funCalculateTax(arrListItemDtls,POSCode,"",billAreaCode,operationTypeForTax,0,0,"Tax Regen","S01","Sales");
        sqlDelete = "delete from tblbilltaxdtl where strBillNo='" +billNo + "'";
        double taxAmt=0.0;
        clsGlobalVarClass.dbMysql.execute(sqlDelete);
        for(clsTaxCalculationDtls objTaxCalDtl : arrListTaxCal)
        {            
            String sqlInsertTaxDtl = "insert into tblbilltaxdtl "
                + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode) "
                + "values('" + billNo + "','" + objTaxCalDtl.getTaxCode()+ "'"
                + "," + objTaxCalDtl.getTaxableAmount() + "," + objTaxCalDtl.getTaxAmount() + ""
                + ",'" + clsGlobalVarClass.gClientCode + "')";
            taxAmt+=objTaxCalDtl.getTaxAmount();
            clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
        }
        double grandTotal=((subTotal+taxAmt)-disTotal);
        sql="update tblbillhd set dblDiscountAmt='"+disTotal+"',dblDiscountPer='"+disper+"',"
            + "dblTaxAmt='"+taxAmt+"',dblSubTotal='"+subTotal+"',dblGrandTotal='"+grandTotal+"' "
            + "where strBillNo='"+txtBillNo.getText().trim()+"'";
        clsGlobalVarClass.dbMysql.execute(sql);
        
        sqlDelete = "delete from tblbillcomplementrydtl where strBillNo='" + billNo + "'";
        clsGlobalVarClass.dbMysql.execute(sqlDelete);
        
        return 0;
    }
    
    private void funResetField()
    {
        txtBillNo.setText("");
        txtRefundAmount.setText("0.00");
        txtRefundRefNo.setText("");
        
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
        panelLayout = new javax.swing.JPanel();
        lblModuleName1 = new javax.swing.JLabel();
        panelRefund = new javax.swing.JPanel();
        txtRefundRefNo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtRefundAmount = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        btnRefund = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtBillNo = new javax.swing.JTextField();
        lblReasonName = new javax.swing.JLabel();
        cmbReason = new javax.swing.JComboBox();

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
        lblformName.setText("-JioMoney Refund");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelLayout.setForeground(new java.awt.Color(254, 184, 80));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(260, 600));

        lblModuleName1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblModuleName1.setForeground(new java.awt.Color(14, 7, 7));
        lblModuleName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModuleName1.setText("JioMoney Refund");

        panelRefund.setOpaque(false);

        txtRefundRefNo.setEditable(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Ref No   :");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Amount :");

        txtRefundAmount.setEditable(false);
        txtRefundAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRefundAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnRefund.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRefund.setForeground(new java.awt.Color(255, 255, 255));
        btnRefund.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnRefund.setText("Refund");
        btnRefund.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefund.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnRefund.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefundMouseClicked(evt);
            }
        });
        btnRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Bill No   :");

        txtBillNo.setEditable(false);
        txtBillNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBillNoMouseClicked(evt);
            }
        });
        txtBillNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillNoActionPerformed(evt);
            }
        });

        lblReasonName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReasonName.setText("Select Reason");

        javax.swing.GroupLayout panelRefundLayout = new javax.swing.GroupLayout(panelRefund);
        panelRefund.setLayout(panelRefundLayout);
        panelRefundLayout.setHorizontalGroup(
            panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRefundLayout.createSequentialGroup()
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRefundLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(btnRefund, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRefundLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                            .addComponent(lblReasonName))
                        .addGap(33, 33, 33)
                        .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBillNo, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(txtRefundAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRefundRefNo)
                            .addComponent(cmbReason, javax.swing.GroupLayout.Alignment.TRAILING, 0, 217, Short.MAX_VALUE))))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        panelRefundLayout.setVerticalGroup(
            panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRefundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtRefundRefNo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbReason, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblReasonName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRefundLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtRefundAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(panelRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefund, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelLayoutLayout = new javax.swing.GroupLayout(panelLayout);
        panelLayout.setLayout(panelLayoutLayout);
        panelLayoutLayout.setHorizontalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayoutLayout.createSequentialGroup()
                .addGap(295, 295, 295)
                .addComponent(lblModuleName1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
            .addGroup(panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLayoutLayout.createSequentialGroup()
                    .addGap(171, 171, 171)
                    .addComponent(panelRefund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(171, Short.MAX_VALUE)))
        );
        panelLayoutLayout.setVerticalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayoutLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lblModuleName1)
                .addContainerGap(453, Short.MAX_VALUE))
            .addGroup(panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLayoutLayout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addComponent(panelRefund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(138, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelLayout, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("JioMoney Refund");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("JioMoney Refund");
    }//GEN-LAST:event_formWindowClosing

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("JioMoney Refund");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnRefundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefundMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("JioMoney Refund");
    }//GEN-LAST:event_btnRefundMouseClicked

    private void btnRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundActionPerformed
        // TODO add your handling code here:
        funRefundAmount();
    }//GEN-LAST:event_btnRefundActionPerformed

    private void txtBillNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillNoActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtBillNoActionPerformed

    private void txtBillNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillNoMouseClicked
        // TODO add your handling code here:
         funSearchBillNo();
    }//GEN-LAST:event_txtBillNoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRefund;
    private javax.swing.JComboBox cmbReason;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReasonName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelRefund;
    private javax.swing.JTextField txtBillNo;
    private javax.swing.JTextField txtRefundAmount;
    private javax.swing.JTextField txtRefundRefNo;
    // End of variables declaration//GEN-END:variables

}
    
    
