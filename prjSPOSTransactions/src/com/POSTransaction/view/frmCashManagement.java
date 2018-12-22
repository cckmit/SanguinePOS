package com.POSTransaction.view;

import com.POSGlobal.controller.clsCashManagement;
import com.POSGlobal.controller.clsCashManagementDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmCashManagement extends javax.swing.JFrame
{
    private String sql,transDate;
    java.util.Vector vReasonCode, vReasonName, vSelectedForm, vSettlementModes;

    public frmCashManagement()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        try
        {
            vReasonCode = new java.util.Vector();
            vReasonName = new java.util.Vector();
            vSettlementModes = new java.util.Vector();
            
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            
            sql = "select strReasonCode,strReasonName from tblreasonmaster where strCashMgmt='Y'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                vReasonCode.add(rs.getString(1));
                vReasonName.add(rs.getString(2));
                cmbReason.addItem(rs.getString(2));
            }
            rs.close();

            sql = "select strSettelmentDesc from tblsettelmenthd where strSettelmentType='Cash'";
            ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsSettlement.next())
            {
                vSettlementModes.add(rsSettlement.getString(1));
                cmbSettlement.addItem(rsSettlement.getString(1));
            }
            funSetShortCutKeys();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public void tickTock()
    {
        lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }


    
    private boolean funCheckUserEntryForRolling() throws Exception
    {
        boolean flgResult=false;
        String sql="select strTransID from tblcashmanagement "
            + " where strUserCreated='"+clsGlobalVarClass.gUserCode+"' and strAgainst='Rolling' "
            + "and date(dteTransDate)='"+clsGlobalVarClass.getOnlyPOSDateForTransaction()+"'";
        //System.out.println(sql);
        ResultSet rsCashMgmt=clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsCashMgmt.next())
        {
            flgResult=true;
        }
        rsCashMgmt.close();
        
        return flgResult;
    }
    
    

    private void funSaveCashManagement()
    {
        if (Double.parseDouble(txtAmount.getText()) > 0)
        {
            try
            {
                double rollingAmt=0;
                String against="Direct";
                String transId = new clsUtility().funGenerateNextCode();
                String transType = cmbTransType.getSelectedItem().toString();
                String rollingTransType = cmbTransType.getSelectedItem().toString();
                txtTransId.setText(transId);
                double balanceAmt=0;
                double amount=Double.parseDouble(txtAmount.getText());
                double rollingAmount=0;
                transDate=new clsUtility().funGetPOSDateForTransaction();
                
                if(transType.equalsIgnoreCase("Rolling"))
                {
                    if(funCheckUserEntryForRolling())
                    {
                        JOptionPane.showMessageDialog(null, clsGlobalVarClass.gUserCode+" has already entered rolling amount");
                        return;
                    }
                    
                    clsCashManagement objCashMgmt=new clsCashManagement();
                    Map<String,clsCashManagementDtl> hmCashMgmtDtl=objCashMgmt.funGetCashManagement(transDate.split(" ")[0], transDate.split(" ")[0],clsGlobalVarClass.gPOSCode);
                    balanceAmt=objCashMgmt.funGetBalanceUserWise(transDate.split(" ")[0], transDate.split(" ")[0], hmCashMgmtDtl, clsGlobalVarClass.gUserCode);
                    
                    amount=((balanceAmt-Double.parseDouble(txtAmount.getText())));
                    transType="Withdrawal";
                    against="Rolling";
                    rollingAmt=Double.parseDouble(txtAmount.getText());
                    rollingAmount=Double.parseDouble(txtAmount.getText());
                }
                
                
                sql = "insert into tblcashmanagement(strTransID,strTransType,dteTransDate,strReasonCode,strPOSCode"
                    + ",dblAmount,strRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strCurrencyType"
                    + ",intShiftCode,strAgainst,dblRollingAmt,strClientCode,strdataPostFlag) "
                    + "values ('" + transId + "','" + transType + "','" + transDate + "','" + vReasonCode.elementAt(cmbReason.getSelectedIndex()).toString() + "'"
                    + ",'" + clsGlobalVarClass.gPOSCode + "','" + amount + "','" + txtRemarks.getText() + "'"
                    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + cmbSettlement.getSelectedItem().toString() + "'"
                    + ",'" + clsGlobalVarClass.gShiftNo + "','"+against+"','"+rollingAmt+"','"+clsGlobalVarClass.gClientCode+"'"
                    + ",'N')";
                System.out.println(sql);
                clsGlobalVarClass.dbMysql.execute(sql);
                JOptionPane.showMessageDialog(this, "Data Inserted Successfully");

                int num = JOptionPane.showConfirmDialog(this, "Would you like to Print", "Print", JOptionPane.YES_NO_OPTION);
                if (num == 0)
                {
                    //String printerName,String transId,String transType,double amount,String reason,String remarks,String transDate
                    funGenerateTextFileForCashManagement(clsGlobalVarClass.gBillPrintPrinterPort,transId,transType,amount,cmbReason.getSelectedItem().toString(),txtRemarks.getText(),clsGlobalVarClass.gPOSDateForTransaction,rollingAmt);
                }
                funResetFields();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            new frmOkPopUp(null, "Please Enter Amount", "Warnning", 1).setVisible(true);
            txtAmount.requestFocus();
        }
    }

    
    private void funUpdateCashManagement()
    {
        if (Double.parseDouble(txtAmount.getText()) > 0)
        {
            try
            {
                double rollingAmt=0;
                String against="Direct";
                String reason = vReasonCode.elementAt(cmbReason.getSelectedIndex()).toString();
                String remarks = txtRemarks.getText();
                String transType=cmbTransType.getSelectedItem().toString();
                double balanceAmt=0;
                double amount=Double.parseDouble(txtAmount.getText());
                if(transType.equalsIgnoreCase("Rolling"))
                {
                    clsCashManagement objCashMgmt=new clsCashManagement();
                    Map<String,clsCashManagementDtl> hmCashMgmtDtl=objCashMgmt.funGetCashManagement(clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0], clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0],clsGlobalVarClass.gPOSCode);
                    balanceAmt=objCashMgmt.funGetBalanceUserWise(clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0], clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0], hmCashMgmtDtl, clsGlobalVarClass.gUserCode);
                    amount=(balanceAmt-Double.parseDouble(txtAmount.getText()));
                    transType="Withdrawal";
                    against="Rolling";
                    rollingAmt=Double.parseDouble(txtAmount.getText());
                }
                
                sql = "update tblcashmanagement set strTransType='" + transType + "',dteTransDate='" + clsGlobalVarClass.gPOSDateForTransaction+ "'"
                    + " ,strReasonCode='" + reason + "',strPOSCode='" + clsGlobalVarClass.gPOSCode + "',dblAmount='" + amount+ "'"
                    + " ,strRemarks='" + remarks + "',strCurrencyType='" + cmbSettlement.getSelectedItem().toString()+ "'"
                    + " ,intShiftCode=" + clsGlobalVarClass.gShiftNo + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                    + " ,dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strAgainst='"+against+"'"
                    + " ,dblRollingAmt='"+rollingAmt+"' "
                    + " where strTransId='" + txtTransId.getText() + "'";
                System.out.println(sql);
                clsGlobalVarClass.dbMysql.execute(sql);
                JOptionPane.showMessageDialog(this, "Data Updated Successfully");
                funResetFields();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            new frmOkPopUp(null, "Please Enter Amount", "Warnning", 1).setVisible(true);
            txtAmount.requestFocus();
        }
    }

    
    private void funResetFields()
    {
        btnSave.setText("SAVE");
        txtAmount.setText("0.00");
        txtRemarks.setText("");
        txtTransId.setText("");
        cmbTransType.setSelectedItem("Rolling");
        cmbTransType.requestFocus();
    }

    
    private void funSetCashManagementData(Object[] data)
    {
        try
        {
            sql = "select * from tblcashmanagement where strTransID='" + data[0].toString() + "'";
            ResultSet rsCashMngData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsCashMngData.next())
            {
                txtTransId.setText(rsCashMngData.getString(1));
                cmbTransType.setSelectedItem(rsCashMngData.getString(2).toString());
                
                txtAmount.setText(rsCashMngData.getString(6));
                cmbReason.setSelectedItem(rsCashMngData.getString(4).toString());
                cmbSettlement.setSelectedItem(rsCashMngData.getString(12).toString());
                txtRemarks.setText(rsCashMngData.getString(7));
            }
            rsCashMngData.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void funSetShortCutKeys()
    {
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');
        btnClose.setMnemonic('c');
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
        };  ;
        panelFormBody = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
        lblTransId = new javax.swing.JLabel();
        lblTransType = new javax.swing.JLabel();
        txtTransId = new javax.swing.JTextField();
        cmbTransType = new javax.swing.JComboBox();
        lblAmt = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        cmbReason = new javax.swing.JComboBox();
        lblReason = new javax.swing.JLabel();
        lblRemarks = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmbSettlement = new javax.swing.JComboBox();
        lblSettlementMode = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Cash Management");
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

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        btnSave.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
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

        btnReset.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
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

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
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

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Cash Management");

        lblTransId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransId.setText("Transaction ID");

        lblTransType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransType.setText("Transaction Type");

        txtTransId.setEnabled(false);
        txtTransId.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTransIdMouseClicked(evt);
            }
        });

        cmbTransType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rolling", "Float", "Transfer In", "Refund", "Withdrawal", "Payments", "Transfer Out" }));
        cmbTransType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbTransTypeKeyPressed(evt);
            }
        });

        lblAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAmt.setText("Amount");

        txtAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAmount.setText("0.00");
        txtAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAmountMouseClicked(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAmountKeyPressed(evt);
            }
        });

        cmbReason.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbReasonKeyPressed(evt);
            }
        });

        lblReason.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReason.setText("Reason");

        lblRemarks.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRemarks.setText("Remarks");

        txtRemarks.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRemarksMouseClicked(evt);
            }
        });
        txtRemarks.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtRemarksKeyPressed(evt);
            }
        });

        cmbSettlement.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSettlementKeyPressed(evt);
            }
        });

        lblSettlementMode.setText("Currency");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(274, 274, 274)
                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(40, 40, 40)
                                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(40, 40, 40)
                                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblRemarks, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblReason, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTransType, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbTransType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbReason, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(65, 65, 65)
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblSettlementMode, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                            .addComponent(lblAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                            .addComponent(cmbSettlement, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(156, 156, 156)))))
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTransType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblReason, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSettlementMode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReason, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(160, 160, 160)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Cash Management");
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtTransIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTransIdMouseClicked

        /*
        try
        {
            objUtility.funCallForSearchForm("CashManagement");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnSave.setText("UPDATE");//UpdateD
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetCashManagementData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
                */
    }//GEN-LAST:event_txtTransIdMouseClicked

    private void txtAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAmountMouseClicked
        // TODO add your handling code here:
        if (txtAmount.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "","Double", "Enter Amount").setVisible(true);
            txtAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtAmount.getText(), "Double", "Enter Amount").setVisible(true);
            txtAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtAmountMouseClicked

    private void txtRemarksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRemarksMouseClicked
        // TODO add your handling code here:
        if (txtRemarks.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Remark").setVisible(true);
            txtRemarks.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtRemarks.getText(), "1", "Enter Remark").setVisible(true);
            txtRemarks.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtRemarksMouseClicked

    private void cmbTransTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTransTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtAmount.requestFocus();
            txtAmount.selectAll();
        }
    }//GEN-LAST:event_cmbTransTypeKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbReason.requestFocus();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void cmbSettlementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSettlementKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtRemarks.requestFocus();
        }
    }//GEN-LAST:event_cmbSettlementKeyPressed

    private void txtRemarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnSave.getText().equals("SAVE"))
            {
                funSaveCashManagement();
            }
            else
            {
                funUpdateCashManagement();
            }
        }
    }//GEN-LAST:event_txtRemarksKeyPressed

    private void cmbReasonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbReasonKeyPressed
        
    }//GEN-LAST:event_cmbReasonKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (btnSave.getText().equals("SAVE"))
        {
            funSaveCashManagement();
        }
        else
        {
            funUpdateCashManagement();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Cash Management");
    }//GEN-LAST:event_formWindowClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbReason;
    private javax.swing.JComboBox cmbSettlement;
    private javax.swing.JComboBox cmbTransType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAmt;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReason;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblSettlementMode;
    private javax.swing.JLabel lblTransId;
    private javax.swing.JLabel lblTransType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtTransId;
    // End of variables declaration//GEN-END:variables

    
    
    private void funGenerateTextFileForCashManagement(String printerName,String transId,String transType,double amount,String reason,String remarks,String transDate,double rollingAmt)
    {
        clsUtility objUtility=new clsUtility();
        try
        {
            transDate=objUtility.funGetDateInFormat(transDate.split(" ")[0], "dd-MM-yyyy");
            funCreateTempFolder();
            String filePath = System.getProperty("user.dir");
            File fileCashManagementSlip = new File(filePath + "/Temp/CashManagementSlip.txt");
            PrintWriter pw = new PrintWriter(fileCashManagementSlip);
            pw.println();
            pw.println(objUtility.funPrintTextWithAlignment("Cash Management Slip", 40, "Center"));
            pw.println(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName, 40, "Center"));
            pw.println(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSName, 40, "Center"));
            pw.println(objUtility.funPrintTextWithAlignment(transDate, 40, "Center"));
            pw.println();
            pw.println("------------------------------------------------");
            pw.print(objUtility.funPrintTextWithAlignment("Trans ID ", 10, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(transId, 30, "Left"));
            pw.println();
            pw.print(objUtility.funPrintTextWithAlignment("Trans Type ", 12, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(transType, 30, "Left"));
            pw.println();
            pw.print(objUtility.funPrintTextWithAlignment("Amount ", 10, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(amount), 30, "Left"));
            pw.println();
            pw.print(objUtility.funPrintTextWithAlignment("Reason ", 10, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(reason, 30, "Left"));
            pw.println();
            pw.print(objUtility.funPrintTextWithAlignment("Remarks ", 10, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(remarks, 30, "Left"));
            pw.println();
            pw.print(objUtility.funPrintTextWithAlignment("User ", 10, "Left"));
            pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gUserCode, 30, "Left"));
            
            if(rollingAmt>0)
            {
                pw.println();
                pw.println("---------------------------");
                pw.print(objUtility.funPrintTextWithAlignment("Trans ID ", 10, "Left"));
                pw.print(objUtility.funPrintTextWithAlignment(transId, 30, "Left"));
                pw.println();
                pw.print(objUtility.funPrintTextWithAlignment("Trans Type ", 12, "Left"));
                pw.print(objUtility.funPrintTextWithAlignment("Rolling", 30, "Left"));
                pw.println();
                pw.print(objUtility.funPrintTextWithAlignment("Amount ", 10, "Left"));
                pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(rollingAmt), 30, "Left"));
                pw.println();
            }
            
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
            
            funPrintToPrinter(printerName, "CashMgmt",fileCashManagementSlip.getAbsolutePath());
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
            File fileCashMgmt = new File(filePath + "/Temp");
            if (!fileCashMgmt.exists())
            {
                fileCashMgmt.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void funPrintToPrinter(String primaryPrinterName, String type, String fileName)
    {
        try
        {
            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                funPrintCashWindow(primaryPrinterName,fileName);
            }
            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funPrintCashWindow(String primaryPrinterName,String fileName)
    {
        try
        {
            int printerIndex = 0;
            String printerStatus = "Not Found";
            System.out.println("Primary Name=" + primaryPrinterName);

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            primaryPrinterName = primaryPrinterName.replaceAll("#", "\\\\");

            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++)
            {
                System.out.println("Service=" + printService[i].getName() + "\tPrim P=" + primaryPrinterName);
                String printerServiceName = printService[i].getName();
                if (primaryPrinterName.equalsIgnoreCase(printerServiceName))
                {
                    System.out.println("Printer=" + primaryPrinterName);
                    printerIndex = i;
                    printerStatus = "Found";
                    break;
                }
            }

            if (printerStatus.equals("Found"))
            {
                DocPrintJob job = printService[printerIndex].createPrintJob();
                FileInputStream fis = new FileInputStream(fileName);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
                String printerInfo = "";

                PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
                for (Attribute a : att.toArray())
                {
                    String attributeName;
                    String attributeValue;
                    attributeName = a.getName();
                    attributeValue = att.get(a.getClass()).toString();
                    if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
                    {
                        clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
                        printerInfo = primaryPrinterName + "!" + attributeValue;
                        //System.out.println(attributeName + " : " + attributeValue);
                    }
                }
                if (clsGlobalVarClass.gShowBill)
                {
                    funShowTextFile(new File(fileName), "", printerInfo);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public void funShowTextFile(File file, String formName, String printerInfo)
    {
        try
        {
            String data = "";
            FileReader fread = new FileReader(file);
            //BufferedReader KOTIn = new BufferedReader(fread);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = "";
            while ((line = KOTIn.readLine()) != null)
            {
                data = data + line + "\n";
            }
            String fileName = file.getName();
            String name = "";
            if (formName.trim().length() > 0)
            {
                name = formName;
            }

            new com.POSGlobal.view.frmShowTextFile(data, name, file, printerInfo).setVisible(true);
            fread.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funPrintOnSecPrinter(String secPrinterName, String fileName) throws Exception
    {
        String printerStatus = "Not Found";
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        int printerIndex = 0;
        for (int i = 0; i < printService.length; i++)
        {
            System.out.println("Service=" + printService[i].getName() + "\tSec P=" + secPrinterName);
            String printerServiceName = printService[i].getName();

            if (secPrinterName.equalsIgnoreCase(printerServiceName))
            {
                System.out.println("Sec Printer=" + secPrinterName);
                printerIndex = i;
                printerStatus = "Found";
                break;
            }
        }
        if (printerStatus.equals("Found"))
        {
            String printerInfo = "";
            DocPrintJob job = printService[printerIndex].createPrintJob();
            FileInputStream fis = new FileInputStream(fileName);
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
            job.addPrintJobListener(new MyPrintJobListener());
            job.print(doc, pras);

            PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
            for (Attribute a : att.toArray())
            {
                String attributeName;
                String attributeValue;
                attributeName = a.getName();
                attributeValue = att.get(a.getClass()).toString();
                if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
                {
                    clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
                    printerInfo = secPrinterName + "!" + attributeValue;
                }
                System.out.println(attributeName + " : " + attributeValue);
            }
            if (clsGlobalVarClass.gShowBill)
            {
                funShowTextFile(new File(fileName), "", printerInfo);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, secPrinterName + " Printer Not Found");
        }
    }

    class MyPrintJobListener implements PrintJobListener
    {

        public void printDataTransferCompleted(PrintJobEvent pje)
        {
            System.out.println("printDataTransferCompleted");
        }

        public void printJobCanceled(PrintJobEvent pje)
        {
            System.out.println("The print job was cancelled");
        }

        public void printJobCompleted(PrintJobEvent pje)
        {
            System.out.println("The print job was completed");
        }

        public void printJobFailed(PrintJobEvent pje)
        {
            System.out.println("The print job has failed");
        }

        public void printJobNoMoreEvents(PrintJobEvent pje)
        {
        }

        public void printJobRequiresAttention(PrintJobEvent pje)
        {
        }
    }

}
