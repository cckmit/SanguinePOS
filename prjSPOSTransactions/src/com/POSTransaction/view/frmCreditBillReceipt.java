/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsCreditBillReceipt;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

public class frmCreditBillReceipt extends javax.swing.JFrame
{

    private clsUtility objUtility;
    private SimpleDateFormat ddMMyyyyDateFormat;
    private SimpleDateFormat yyyyMMddDateFormat;
    private String selectedCustomerCode;
    private String selectedCustomerName;
    private int settlementNavigate;
    private String posDate, amountBox, sql, textValue1, settleCode, settleName, settleType, strValue;
    private java.util.Vector vSettlementDesc, vSettlementCode, vSettlementType, vCurrencyType;
    private JButton[] settlementArray = new JButton[4];
    private boolean enter, settleMode, flgModifyAdvOrder;

    public frmCreditBillReceipt()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        try
        {

            objUtility = new clsUtility();

            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            ddMMyyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // dteBillDate.setDate(objUtility.funGetDateToSetCalenderDate());
            settlementNavigate = 0;
            settlementArray[0] = btnSettle1;
            settlementArray[1] = btnSettle2;
            settlementArray[2] = btnSettle3;
            settlementArray[3] = btnSettle4;
            int cntSettlement = 0;
            for (cntSettlement = 0; cntSettlement < settlementArray.length; cntSettlement++)
            {
                settlementArray[cntSettlement].setVisible(false);
            }
            vSettlementType = new java.util.Vector();
            vSettlementDesc = new java.util.Vector();
            vSettlementCode = new java.util.Vector();
            vCurrencyType = new java.util.Vector();
            cntSettlement = 0;
            sql = "Select Count(*) from tblsettelmenthd where strApplicable='Yes' and strCreditReceiptYN='Y'";
            ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsSettlement.next();
            int settlementCount = rsSettlement.getInt(1);
            if (settlementCount > 3)
            {
                settlementCount = 4;
            }
            for (cntSettlement = 0; cntSettlement < settlementCount; cntSettlement++)
            {
                settlementArray[cntSettlement].setVisible(true);
            }
            cntSettlement = 0;
            sql = "select strSettelmentCode,strSettelmentDesc,strSettelmentType,dblConvertionRatio"
                    + " from tblsettelmenthd where strApplicable='Yes' and strCreditReceiptYN='Y'";
            rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsSettlement.next())
            {
                vSettlementCode.add(rsSettlement.getString(1));
                vSettlementDesc.add(rsSettlement.getString(2));
                vSettlementType.add(rsSettlement.getString(3));
                vCurrencyType.add(rsSettlement.getString(4));
                cntSettlement++;
            }
            rsSettlement.close();
            if (vSettlementCode.size() <= 4)
            {
                btnNextSettlementMode.setEnabled(false);
            }
            funFillSettlementButtons(0, 4);
            btnPrevSettlementMode.setEnabled(false);
            settlementNavigate = 0;

            procSettlementBtnClick(btnSettle1.getText());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
        panelFormBody1 = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblReceiptNo = new javax.swing.JLabel();
        txtReceiptNo = new javax.swing.JTextField();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        panelNumericKeyPad = new javax.swing.JPanel();
        btnCal7 = new javax.swing.JButton();
        btnCal8 = new javax.swing.JButton();
        btnCal9 = new javax.swing.JButton();
        btnCalClear = new javax.swing.JButton();
        btnCal4 = new javax.swing.JButton();
        btnCal5 = new javax.swing.JButton();
        btnCal6 = new javax.swing.JButton();
        btnCal0 = new javax.swing.JButton();
        btnCal00 = new javax.swing.JButton();
        btnCal3 = new javax.swing.JButton();
        btnCalBackSpace = new javax.swing.JButton();
        btnCalDot = new javax.swing.JButton();
        btnCal1 = new javax.swing.JButton();
        btnCal2 = new javax.swing.JButton();
        btnCalEnter = new javax.swing.JButton();
        txtBillNo = new javax.swing.JTextField();
        lblBillNo = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblBillDate = new javax.swing.JLabel();
        lblBillAmount = new javax.swing.JLabel();
        txtCreditAmount = new javax.swing.JTextField();
        lblEnterAmount = new javax.swing.JLabel();
        txtReceiptAmt = new javax.swing.JTextField();
        txtPaidAmount = new javax.swing.JTextField();
        lblPaidAmount = new javax.swing.JLabel();
        btnSaveUpdate = new javax.swing.JButton();
        lblBalanceAmount = new javax.swing.JLabel();
        txtBalanceAmount = new javax.swing.JTextField();
        lblPayMode = new javax.swing.JLabel();
        btnPrevSettlementMode = new javax.swing.JButton();
        btnSettle1 = new javax.swing.JButton();
        btnSettle2 = new javax.swing.JButton();
        btnSettle3 = new javax.swing.JButton();
        btnSettle4 = new javax.swing.JButton();
        btnNextSettlementMode = new javax.swing.JButton();
        lblBillDateValue = new javax.swing.JLabel();
        panelPayModeHeader = new javax.swing.JPanel();
        lblPayModeHeader = new javax.swing.JLabel();
        lblPaymentMode = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        lblRemarks = new javax.swing.JLabel();
        lblEnterAmount1 = new javax.swing.JLabel();
        txtRefundAmt = new javax.swing.JTextField();

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
        lblProductName.setText("SPOS-");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Credit Bill Receipt");
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

        panelFormBody1.setOpaque(false);
        panelFormBody1.setPreferredSize(new java.awt.Dimension(780, 600));
        panelFormBody1.setLayout(null);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Credit Bill Receipt");
        panelFormBody1.add(lblFormName);
        lblFormName.setBounds(290, 0, 190, 35);

        lblReceiptNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReceiptNo.setText("Receipt No.");
        panelFormBody1.add(lblReceiptNo);
        lblReceiptNo.setBounds(10, 50, 80, 30);

        txtReceiptNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtReceiptNo.setEnabled(false);
        txtReceiptNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtReceiptNoMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtReceiptNo);
        txtReceiptNo.setBounds(90, 50, 110, 30);

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Customer");
        panelFormBody1.add(lblCustomerName);
        lblCustomerName.setBounds(10, 90, 80, 30);

        txtCustomerName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtCustomerName.setEnabled(false);
        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtCustomerName);
        txtCustomerName.setBounds(90, 90, 350, 30);

        panelNumericKeyPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericKeyPad.setMinimumSize(new java.awt.Dimension(340, 260));
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

        btnCal9.setBackground(new java.awt.Color(102, 153, 255));
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
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

        btnCal00.setBackground(new java.awt.Color(102, 153, 255));
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
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

        btnCalBackSpace.setBackground(new java.awt.Color(102, 153, 255));
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalBackSpaceMouseClicked(evt);
            }
        });
        btnCalBackSpace.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalBackSpaceActionPerformed(evt);
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

        btnCalEnter.setBackground(new java.awt.Color(102, 153, 255));
        btnCalEnter.setText("Enter");
        btnCalEnter.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalEnterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelNumericKeyPadLayout = new javax.swing.GroupLayout(panelNumericKeyPad);
        panelNumericKeyPad.setLayout(panelNumericKeyPadLayout);
        panelNumericKeyPadLayout.setHorizontalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                                .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                                .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        panelNumericKeyPadLayout.setVerticalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        panelFormBody1.add(panelNumericKeyPad);
        panelNumericKeyPad.setBounds(250, 370, 220, 180);

        txtBillNo.setEditable(false);
        txtBillNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtBillNo.setEnabled(false);
        txtBillNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBillNoMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtBillNo);
        txtBillNo.setBounds(90, 130, 110, 30);

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillNo.setText("Bill No.");
        panelFormBody1.add(lblBillNo);
        lblBillNo.setBounds(10, 130, 50, 30);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnCancel);
        btnCancel.setBounds(700, 510, 90, 40);

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnClearMouseClicked(evt);
            }
        });
        btnClear.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearActionPerformed(evt);
            }
        });
        btnClear.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnClearKeyPressed(evt);
            }
        });
        panelFormBody1.add(btnClear);
        btnClear.setBounds(600, 510, 90, 40);

        lblBillDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillDate.setText("Bill Date ");
        panelFormBody1.add(lblBillDate);
        lblBillDate.setBounds(210, 130, 50, 30);

        lblBillAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillAmount.setText("Credit Amount");
        panelFormBody1.add(lblBillAmount);
        lblBillAmount.setBounds(10, 180, 80, 30);

        txtCreditAmount.setEditable(false);
        txtCreditAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtCreditAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCreditAmount.setEnabled(false);
        txtCreditAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCreditAmountMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtCreditAmount);
        txtCreditAmount.setBounds(100, 180, 110, 30);

        lblEnterAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnterAmount.setText("Receipt Amount");
        panelFormBody1.add(lblEnterAmount);
        lblEnterAmount.setBounds(20, 470, 100, 30);

        txtReceiptAmt.setEditable(false);
        txtReceiptAmt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtReceiptAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtReceiptAmt.setText("0");
        txtReceiptAmt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtReceiptAmtMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtReceiptAmt);
        txtReceiptAmt.setBounds(120, 470, 110, 30);

        txtPaidAmount.setEditable(false);
        txtPaidAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtPaidAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaidAmount.setEnabled(false);
        txtPaidAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPaidAmountMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtPaidAmount);
        txtPaidAmount.setBounds(300, 180, 110, 30);

        lblPaidAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPaidAmount.setText("Paid Amount");
        panelFormBody1.add(lblPaidAmount);
        lblPaidAmount.setBounds(220, 180, 80, 30);

        btnSaveUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSaveUpdate.setText("SAVE");
        btnSaveUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSaveUpdate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveUpdateMouseClicked(evt);
            }
        });
        btnSaveUpdate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveUpdateActionPerformed(evt);
            }
        });
        btnSaveUpdate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSaveUpdateKeyPressed(evt);
            }
        });
        panelFormBody1.add(btnSaveUpdate);
        btnSaveUpdate.setBounds(500, 510, 90, 40);

        lblBalanceAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBalanceAmount.setText("Balance Amount");
        panelFormBody1.add(lblBalanceAmount);
        lblBalanceAmount.setBounds(420, 180, 100, 30);

        txtBalanceAmount.setEditable(false);
        txtBalanceAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtBalanceAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBalanceAmount.setEnabled(false);
        txtBalanceAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBalanceAmountMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtBalanceAmount);
        txtBalanceAmount.setBounds(520, 180, 110, 30);

        lblPayMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPayMode.setText("Payment Mode");
        panelFormBody1.add(lblPayMode);
        lblPayMode.setBounds(10, 220, 101, 30);

        btnPrevSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevSettlementMode.setText("<<<");
        btnPrevSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevSettlementModeActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnPrevSettlementMode);
        btnPrevSettlementMode.setBounds(10, 260, 60, 40);

        btnSettle1.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle1.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseEntered(evt);
            }
        });
        panelFormBody1.add(btnSettle1);
        btnSettle1.setBounds(80, 260, 100, 40);

        btnSettle2.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle2.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle2MouseClicked(evt);
            }
        });
        panelFormBody1.add(btnSettle2);
        btnSettle2.setBounds(190, 260, 102, 40);

        btnSettle3.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle3.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle3MouseClicked(evt);
            }
        });
        btnSettle3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettle3ActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnSettle3);
        btnSettle3.setBounds(300, 260, 100, 40);

        btnSettle4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle4.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle4MouseClicked(evt);
            }
        });
        panelFormBody1.add(btnSettle4);
        btnSettle4.setBounds(410, 260, 100, 40);

        btnNextSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnNextSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextSettlementMode.setText(">>>");
        btnNextSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextSettlementModeActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnNextSettlementMode);
        btnNextSettlementMode.setBounds(520, 260, 60, 40);
        panelFormBody1.add(lblBillDateValue);
        lblBillDateValue.setBounds(270, 130, 170, 30);

        panelPayModeHeader.setMinimumSize(new java.awt.Dimension(250, 150));
        panelPayModeHeader.setOpaque(false);

        lblPayModeHeader.setText("Payment Mode");

        lblPaymentMode.setText("Cash");

        javax.swing.GroupLayout panelPayModeHeaderLayout = new javax.swing.GroupLayout(panelPayModeHeader);
        panelPayModeHeader.setLayout(panelPayModeHeaderLayout);
        panelPayModeHeaderLayout.setHorizontalGroup(
            panelPayModeHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPayModeHeaderLayout.createSequentialGroup()
                .addComponent(lblPayModeHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPaymentMode, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
        );
        panelPayModeHeaderLayout.setVerticalGroup(
            panelPayModeHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPayModeHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblPaymentMode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelFormBody1.add(panelPayModeHeader);
        panelPayModeHeader.setBounds(10, 310, 240, 40);

        txtRemark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRemarkMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtRemark);
        txtRemark.setBounds(470, 310, 320, 30);

        lblRemarks.setText("Remarks");
        panelFormBody1.add(lblRemarks);
        lblRemarks.setBounds(400, 310, 70, 30);

        lblEnterAmount1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnterAmount1.setText("Refund Amount");
        panelFormBody1.add(lblEnterAmount1);
        lblEnterAmount1.setBounds(20, 510, 100, 30);

        txtRefundAmt.setEditable(false);
        txtRefundAmt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtRefundAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRefundAmt.setText("0");
        txtRefundAmt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRefundAmtMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtRefundAmt);
        txtRefundAmt.setBounds(120, 510, 110, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFormBody1, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFormBody1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtReceiptNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtReceiptNoMouseClicked

        //funSelectCreditBillReceipt();
    }//GEN-LAST:event_txtReceiptNoMouseClicked

    private void btnCal7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal7MouseClicked

        procNumericValue(btnCal7.getText());
    }//GEN-LAST:event_btnCal7MouseClicked

    private void btnCal8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal8MouseClicked

        procNumericValue(btnCal8.getText());
    }//GEN-LAST:event_btnCal8MouseClicked

    private void btnCal9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal9MouseClicked

        procNumericValue(btnCal9.getText());
    }//GEN-LAST:event_btnCal9MouseClicked

    private void btnCalClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalClearMouseClicked
        // TODO add your handling code here:
        funClearBtnPressed();
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked

        procNumericValue(btnCal4.getText());
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked

        procNumericValue(btnCal5.getText());
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked

        procNumericValue(btnCal6.getText());
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked

        procNumericValue(btnCal0.getText());
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked

        procNumericValue(btnCal00.getText());
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked

        procNumericValue(btnCal3.getText());
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal3ActionPerformed

    private void btnCalBackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalBackSpaceMouseClicked
        // TODO add your handling code here:
        funBackspaceBtnPressed();
    }//GEN-LAST:event_btnCalBackSpaceMouseClicked

    private void btnCalBackSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalBackSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalBackSpaceActionPerformed

    private void btnCalDotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalDotMouseClicked

        procNumericValue(btnCalDot.getText());
    }//GEN-LAST:event_btnCalDotMouseClicked

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked

        procNumericValue(btnCal1.getText());
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked
        // TODO add your handling code here:
        procNumericValue(btnCal2.getText());
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal2ActionPerformed

    private void btnCalEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalEnterMouseClicked
        funEnterButtonClicked();

    }//GEN-LAST:event_btnCalEnterMouseClicked

    private void txtBillNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillNoMouseClicked

        funSelectCreditBills();
    }//GEN-LAST:event_txtBillNoMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        //funCheckAdvanceOrder();
        clsGlobalVarClass.hmActiveForms.remove("Credit Bill Receipt");
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

    }//GEN-LAST:event_btnClearActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Credit Bill Receipt");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Credit Bill Receipt");
    }//GEN-LAST:event_formWindowClosing

    private void btnClearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnClearKeyPressed

    }//GEN-LAST:event_btnClearKeyPressed

    private void txtCreditAmountMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCreditAmountMouseClicked
    {//GEN-HEADEREND:event_txtCreditAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCreditAmountMouseClicked

    private void txtReceiptAmtMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtReceiptAmtMouseClicked
    {//GEN-HEADEREND:event_txtReceiptAmtMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReceiptAmtMouseClicked

    private void txtPaidAmountMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPaidAmountMouseClicked
    {//GEN-HEADEREND:event_txtPaidAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaidAmountMouseClicked

    private void btnSaveUpdateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveUpdateActionPerformed
    {//GEN-HEADEREND:event_btnSaveUpdateActionPerformed

    }//GEN-LAST:event_btnSaveUpdateActionPerformed

    private void btnSaveUpdateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnSaveUpdateKeyPressed
    {//GEN-HEADEREND:event_btnSaveUpdateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveUpdateKeyPressed

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnClearMouseClicked
    {//GEN-HEADEREND:event_btnClearMouseClicked
        funResetFields();
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnSaveUpdateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSaveUpdateMouseClicked
    {//GEN-HEADEREND:event_btnSaveUpdateMouseClicked
        if (btnSaveUpdate.getText().equalsIgnoreCase("save"))
        {
            if (txtBillNo.getText().trim().isEmpty())
            {
                new frmOkPopUp(null, "Please Select Bill", "warning", 1).setVisible(true);
                return;
            }

            if (vSettlementCode.size() == 0 || settleCode.trim().isEmpty() || settleName.trim().isEmpty())
            {
                new frmOkPopUp(null, "Please Select Settlement", "warning", 1).setVisible(true);
                return;
            }

            double creditAmt = Double.parseDouble(txtCreditAmount.getText());
            double paidAmt = Double.parseDouble(txtPaidAmount.getText());
            double balanceAmt = Double.parseDouble(txtBalanceAmount.getText());
            double receiptAmt = Double.parseDouble(txtReceiptAmt.getText());

            if (creditAmt == paidAmt || balanceAmt <= 0)
            {
                new frmOkPopUp(null, "Full Payment Received", "warning", 1).setVisible(true);
                return;
            }

            if (txtReceiptAmt.getText().trim().length() == 0 || Double.parseDouble(txtReceiptAmt.getText().trim()) <= 0)
            {
                new frmOkPopUp(null, "Please Enter Amount", "warning", 1).setVisible(true);
                return;
            }

            funSaveReceipt();
        }
        else
        {
            if (txtReceiptNo.getText().trim().isEmpty())
            {
                new frmOkPopUp(null, "Please Select Receipt", "warning", 1).setVisible(true);
                return;
            }
        }
    }//GEN-LAST:event_btnSaveUpdateMouseClicked

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCustomerNameMouseClicked
    {//GEN-HEADEREND:event_txtCustomerNameMouseClicked
        funCustomerHelpClicked();
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtBalanceAmountMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBalanceAmountMouseClicked
    {//GEN-HEADEREND:event_txtBalanceAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceAmountMouseClicked

    private void btnPrevSettlementModeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrevSettlementModeActionPerformed
    {//GEN-HEADEREND:event_btnPrevSettlementModeActionPerformed
        // TODO add your handling code here:
        funPrevSettlementMode();
    }//GEN-LAST:event_btnPrevSettlementModeActionPerformed

    private void btnSettle1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSettle1MouseClicked
    {//GEN-HEADEREND:event_btnSettle1MouseClicked

        procSettlementBtnClick(btnSettle1.getText());
    }//GEN-LAST:event_btnSettle1MouseClicked

    private void btnSettle1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSettle1MouseEntered
    {//GEN-HEADEREND:event_btnSettle1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettle1MouseEntered

    private void btnSettle2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSettle2MouseClicked
    {//GEN-HEADEREND:event_btnSettle2MouseClicked

        procSettlementBtnClick(btnSettle2.getText());
    }//GEN-LAST:event_btnSettle2MouseClicked

    private void btnSettle3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSettle3MouseClicked
    {//GEN-HEADEREND:event_btnSettle3MouseClicked

        procSettlementBtnClick(btnSettle3.getText());
    }//GEN-LAST:event_btnSettle3MouseClicked

    private void btnSettle3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSettle3ActionPerformed
    {//GEN-HEADEREND:event_btnSettle3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettle3ActionPerformed

    private void btnSettle4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSettle4MouseClicked
    {//GEN-HEADEREND:event_btnSettle4MouseClicked
        // TODO add your handling code here:
        procSettlementBtnClick(btnSettle4.getText());
    }//GEN-LAST:event_btnSettle4MouseClicked

    private void btnNextSettlementModeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextSettlementModeActionPerformed
    {//GEN-HEADEREND:event_btnNextSettlementModeActionPerformed
        // TODO add your handling code here:
        funNextSettlementMode();
    }//GEN-LAST:event_btnNextSettlementModeActionPerformed

    private void txtRemarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtRemarkMouseClicked
    {//GEN-HEADEREND:event_txtRemarkMouseClicked
        if (txtRemark.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Remark").setVisible(true);
            txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtRemark.getText(), "1", "Enter Remark").setVisible(true);
            txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtRemarkMouseClicked

    private void txtRefundAmtMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtRefundAmtMouseClicked
    {//GEN-HEADEREND:event_txtRefundAmtMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRefundAmtMouseClicked


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
    private javax.swing.JButton btnCalEnter;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnNextSettlementMode;
    private javax.swing.JButton btnPrevSettlementMode;
    private javax.swing.JButton btnSaveUpdate;
    private javax.swing.JButton btnSettle1;
    private javax.swing.JButton btnSettle2;
    private javax.swing.JButton btnSettle3;
    private javax.swing.JButton btnSettle4;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBalanceAmount;
    private javax.swing.JLabel lblBillAmount;
    private javax.swing.JLabel lblBillDate;
    private javax.swing.JLabel lblBillDateValue;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEnterAmount;
    private javax.swing.JLabel lblEnterAmount1;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaidAmount;
    private javax.swing.JLabel lblPayMode;
    private javax.swing.JLabel lblPayModeHeader;
    private javax.swing.JLabel lblPaymentMode;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReceiptNo;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelFormBody1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelNumericKeyPad;
    private javax.swing.JPanel panelPayModeHeader;
    private javax.swing.JTextField txtBalanceAmount;
    private javax.swing.JTextField txtBillNo;
    private javax.swing.JTextField txtCreditAmount;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtPaidAmount;
    private javax.swing.JTextField txtReceiptAmt;
    private javax.swing.JTextField txtReceiptNo;
    private javax.swing.JTextField txtRefundAmt;
    private javax.swing.JTextField txtRemark;
    // End of variables declaration//GEN-END:variables

    private void funBackspaceBtnPressed()
    {
        try
        {

            StringBuilder receiptAmtBuilder = new StringBuilder(txtReceiptAmt.getText());
            receiptAmtBuilder.deleteCharAt(txtReceiptAmt.getText().length() - 1);

            txtReceiptAmt.setText(receiptAmtBuilder.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funClearBtnPressed()
    {
        try
        {
            txtReceiptAmt.setText("");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void procNumericValue(String strValue)
    {

        if (txtReceiptAmt.getText().trim().length() > 0 && Double.parseDouble(txtReceiptAmt.getText().trim()) <= 0)
        {
            txtReceiptAmt.setText("");
        }

        String receiptAmtValue = txtReceiptAmt.getText() + strValue;
        txtReceiptAmt.setText(receiptAmtValue);

    }

    private void funSelectCreditBillReceipt()
    {
        try
        {
            objUtility.funCallForSearchForm("CreditBillReceipts");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetReceipData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetBillCreditData(Object[] data)
    {
        try
        {

            String billNo = data[0].toString();
            String billDate = data[1].toString();
            String creditAmt = data[2].toString();

            double paidAmt = 0.00;

            //live
            String sql = "select a.strBillNo, sum(c.dblReceiptAmt) "
                    + "from tblbillhd a,tblbillsettlementdtl b,tblqcreditbillreceipthd c,tblsettelmenthd d "
                    + "where a.strBillNo=b.strBillNo "
                    + "and a.strBillNo=c.strBillNo "
                    + "and b.strSettlementCode=d.strSettelmentCode "
                    + "and d.strSettelmentType='Credit' "
                    + "and a.strBillNo='" + billNo + "' "
                    + "group by c.strBillNo;";
            ResultSet rsReceiptHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsReceiptHd.next())
            {
                paidAmt += rsReceiptHd.getDouble(2);
            }
            rsReceiptHd.close();

            //Q
            sql = "select a.strBillNo, sum(c.dblReceiptAmt) "
                    + "from tblqbillhd a,tblqbillsettlementdtl b,tblqcreditbillreceipthd c,tblsettelmenthd d "
                    + "where a.strBillNo=b.strBillNo "
                    + "and a.strBillNo=c.strBillNo "
                    + "and b.strSettlementCode=d.strSettelmentCode "
                    + "and d.strSettelmentType='Credit' "
                    + "and a.strBillNo='" + billNo + "' "
                    + "group by c.strBillNo;";
            rsReceiptHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsReceiptHd.next())
            {
                paidAmt += rsReceiptHd.getDouble(2);
            }
            rsReceiptHd.close();

            txtBillNo.setText(billNo);
            lblBillDateValue.setText(billDate);
            txtCreditAmount.setText(creditAmt);
            txtPaidAmount.setText(String.valueOf(paidAmt));

            double dblCreditAmt = Double.parseDouble(creditAmt);
            double dblPaidAmt = paidAmt;
            double balanceAmt = dblCreditAmt - dblPaidAmt;

            txtBalanceAmount.setText(String.valueOf(balanceAmt));
            txtReceiptAmt.setText(String.valueOf(balanceAmt));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void funSelectCreditBills()
    {
        try
        {
            if (selectedCustomerCode == null || selectedCustomerCode.trim().isEmpty())
            {
                new frmOkPopUp(null, "Please Select Customer", "warning", 1).setVisible(true);
                return;
            }

//            String filterDate = yyyyMMddDateFormat.format(dteBillDate.getDate());
            String posDate = clsGlobalVarClass.getOnlyPOSDateForTransaction();

//            Date dteFilterDate = yyyyMMddDateFormat.parse(filterDate);
            Date dtePOSDate = ddMMyyyyDateFormat.parse(clsGlobalVarClass.gPOSDateToDisplay);

//            String tableType = "Live";
//            if (dteFilterDate.before(dtePOSDate))
//            {
//                tableType = "QFile";
//            }
//
//            objUtility.funCallForSearchForm("CreditBills", filterDate, tableType,"",selectedCustomerCode);
            List<clsCreditBillReceipt> listOfCreditBillReceipts = new ArrayList<clsCreditBillReceipt>();
            listOfCreditBillReceipts = funGetCreditBillReceipts(listOfCreditBillReceipts);

            List<String> listOfColumns = new ArrayList<>();
            listOfColumns.add("Bill No");
            listOfColumns.add("Bill Date");
            listOfColumns.add("Credit Amount");

            clsGlobalVarClass.gSearchItemClicked = false;
            new frmSearchFormDialog(null, true, listOfCreditBillReceipts, "Credit Bill Receipts", listOfColumns, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetBillCreditData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funResetFields()
    {
        txtReceiptNo.setText("");
        //dteBillDate.setDate(objUtility.funGetDateToSetCalenderDate());
        txtBillNo.setText("");
        txtCustomerName.setText("");
        txtCreditAmount.setText("");
        txtPaidAmount.setText("");
        txtBalanceAmount.setText("");
        txtReceiptAmt.setText("0");
        txtRefundAmt.setText("0");
        lblBillDateValue.setText("");

        selectedCustomerCode = "";
        selectedCustomerName = "";
        settleCode = "";
        settleMode = false;
        txtRemark.setText("");

        procSettlementBtnClick(btnSettle1.getText());

    }

    private void funSetReceipData(Object[] data)
    {

    }

    private void funSaveReceipt()
    {
        try
        {

            String receiptNo = funGenerateReceiptNo();
            String chequeNo = "";
            String bankName = "";
            String remarks = txtRemark.getText().trim();
            String chequeDate = "1990-01-01 00:00:00";
            double creditAmt = Double.parseDouble(txtCreditAmount.getText());
            double paidAmt = Double.parseDouble(txtPaidAmount.getText());
            double balanceAmt = Double.parseDouble(txtBalanceAmount.getText());
            double receiptAmt = Double.parseDouble(txtReceiptAmt.getText());

            if (creditAmt == paidAmt || balanceAmt <= 0)
            {
                new frmOkPopUp(null, "Full Payment Received", "warning", 1).setVisible(true);
                return;
            }

            if (receiptAmt > balanceAmt)
            {
                double refundAmt = receiptAmt - balanceAmt;

                txtRefundAmt.setText(String.valueOf(refundAmt));

                txtReceiptAmt.setText(String.valueOf(balanceAmt));
                receiptAmt = Double.parseDouble(txtReceiptAmt.getText());

            }

            String sql = "insert into tblqcreditbillreceipthd(strReceiptNo,strBillNo,strPOSCode,dteReceiptDate,dblReceiptAmt,intShiftCode"
                    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,dteBillDate"
                    + ",strSettlementCode,strSettlementName,strChequeNo,strBankName,dteChequeDate,strRemarks)"
                    + "values "
                    + "('" + receiptNo + "','" + txtBillNo.getText() + "','" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + receiptAmt + "','" + clsGlobalVarClass.gShiftNo + "',"
                    + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N'"
                    + ",'" + lblBillDateValue.getText() + "','" + settleCode + "','" + settleName + "'"
                    + ",'" + chequeNo + "','" + bankName + "','" + chequeDate + "','" + remarks + "')";

            int x = clsGlobalVarClass.dbMysql.execute(sql);

            if (clsGlobalVarClass.gConnectionActive.equals("Y"))
            {
                clsGlobalVarClass.funInvokeHOWebserviceForTrans("Credit Bill Receipts", "Credit Bill Receipts");
            }

            if (x > 0)
            {
                new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
                funResetFields();
            }
            else
            {
                new frmOkPopUp(this, "Unsuccessfull", "Error", 3).setVisible(true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGenerateReceiptNo()
    {
        String receiptNo = "";
        try
        {
            String sql = "select count(dblLastNo) from tblinternal where strTransactionType='CreditReceipt'";
            ResultSet rsAdvReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsAdvReceipt.next();
            int receiptCnt = rsAdvReceipt.getInt(1);
            rsAdvReceipt.close();
            if (receiptCnt > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='CreditReceipt'";
                rsAdvReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsAdvReceipt.next();
                long code = rsAdvReceipt.getLong(1);
                code = code + 1;
                receiptNo = "CR" + String.format("%05d", code);
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='CreditReceipt'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                receiptNo = "CR00001";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('CreditReceipt'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return receiptNo;
    }

    private void funCustomerHelpClicked()
    {
        clsGlobalVarClass.gSearchItemClicked = false;
        selectedCustomerCode = "";
        selectedCustomerName = "";
        objUtility.funCallForSearchForm("CustomerMaster");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();

            selectedCustomerCode = data[0].toString();
            selectedCustomerName = data[1].toString();
            txtCustomerName.setText(selectedCustomerName);

            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }

    private List<clsCreditBillReceipt> funGetCreditBillReceipts(List<clsCreditBillReceipt> listOfCreditBillReceipts)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        try
        {
            //live
            sqlBuilder.setLength(0);
            sqlBuilder.append("SELECT a.strBillNo,date(a.dteBillDate),a.dteBillDate,a.strClientCode, SUM(b.dblSettlementAmt) "
                    + "FROM tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
                    + "WHERE a.strBillNo=b.strBillNo  "
                    + "AND b.strSettlementCode=c.strSettelmentCode  "
                    + "and date(a.dtebilldate)=date(b.dtebilldate)  "
                    + "and a.strClientCode=b.strClientCode  "
                    + "AND c.strSettelmentType='Credit'  "
                    + "AND a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
                    + "AND a.strCustomerCode='" + selectedCustomerCode + "' "
                    + "GROUP BY a.strBillNo ");
            ResultSet rsCreditBillReceipts = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
            while (rsCreditBillReceipts.next())
            {
                String billNo = rsCreditBillReceipts.getString(1);
                String filterBillDate = rsCreditBillReceipts.getString(2);
                String billDate = rsCreditBillReceipts.getString(3);
                String clientCode = rsCreditBillReceipts.getString(4);
                double creditAmount = rsCreditBillReceipts.getDouble(5);

                //remove full paid bills
                //live
                sqlBuilder.setLength(0);
                sqlBuilder.append("select a.strBillNo,date(a.dteBillDate),a.strPOSCode,a.strClientCode,sum(a.dblReceiptAmt) "
                        + "from tblqcreditbillreceipthd a "
                        + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
                        + "and a.strBillNo='" + billNo + "' "
                        + "and date(a.dteBillDate)='" + filterBillDate + "' "
                        + "and a.strClientCode='" + clientCode + "' ");
                ResultSet rsPaidBills = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());

                if (rsPaidBills.next())
                {
                    double totalReceiptAmt = rsPaidBills.getDouble(5);
                    if (Math.rint(creditAmount) == Math.rint(totalReceiptAmt))
                    {
                        //dont add
                    }
                    else
                    {

                        clsCreditBillReceipt objCreditBillReceipt = new clsCreditBillReceipt();

                        objCreditBillReceipt.setStrBillNo(billNo);
                        objCreditBillReceipt.setDteBillDate(billDate);
                        objCreditBillReceipt.setDblCreditAmount(creditAmount);
                        objCreditBillReceipt.setStrClientCode(clientCode);

                        listOfCreditBillReceipts.add(objCreditBillReceipt);
                    }
                }
                else
                {
                    clsCreditBillReceipt objCreditBillReceipt = new clsCreditBillReceipt();

                    objCreditBillReceipt.setStrBillNo(billNo);
                    objCreditBillReceipt.setDteBillDate(billDate);
                    objCreditBillReceipt.setDblCreditAmount(creditAmount);
                    objCreditBillReceipt.setStrClientCode(clientCode);

                    listOfCreditBillReceipts.add(objCreditBillReceipt);
                }

            }
            rsCreditBillReceipts.close();

            //Qfile
            sqlBuilder.setLength(0);
            sqlBuilder.append("SELECT a.strBillNo,date(a.dteBillDate),a.dteBillDate,a.strClientCode, SUM(b.dblSettlementAmt) "
                    + "FROM tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
                    + "WHERE a.strBillNo=b.strBillNo  "
                    + "AND b.strSettlementCode=c.strSettelmentCode  "
                    + "and date(a.dtebilldate)=date(b.dtebilldate)  "
                    + "and a.strClientCode=b.strClientCode  "
                    + "AND c.strSettelmentType='Credit'  "
                    + "AND a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
                    + "AND a.strCustomerCode='" + selectedCustomerCode + "' "
                    + "GROUP BY a.strBillNo ");
            rsCreditBillReceipts = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
            while (rsCreditBillReceipts.next())
            {
                String billNo = rsCreditBillReceipts.getString(1);
                String filterBillDate = rsCreditBillReceipts.getString(2);
                String billDate = rsCreditBillReceipts.getString(3);
                String clientCode = rsCreditBillReceipts.getString(4);
                double creditAmount = rsCreditBillReceipts.getDouble(5);

                //remove full paid bills
                //live
                sqlBuilder.setLength(0);
                sqlBuilder.append("select a.strBillNo,date(a.dteBillDate),a.strPOSCode,a.strClientCode,sum(a.dblReceiptAmt) "
                        + "from tblqcreditbillreceipthd a "
                        + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
                        + "and a.strBillNo='" + billNo + "' "
                        + "and date(a.dteBillDate)='" + filterBillDate + "' "
                        + "and a.strClientCode='" + clientCode + "' ");
                ResultSet rsPaidBills = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());

                if (rsPaidBills.next())
                {
                    double totalReceiptAmt = rsPaidBills.getDouble(5);
                    if (Math.rint(creditAmount) == Math.rint(totalReceiptAmt))
                    {
                        //dont add
                    }
                    else
                    {

                        clsCreditBillReceipt objCreditBillReceipt = new clsCreditBillReceipt();

                        objCreditBillReceipt.setStrBillNo(billNo);
                        objCreditBillReceipt.setDteBillDate(billDate);
                        objCreditBillReceipt.setDblCreditAmount(creditAmount);
                        objCreditBillReceipt.setStrClientCode(clientCode);

                        listOfCreditBillReceipts.add(objCreditBillReceipt);
                    }
                }
                else
                {
                    clsCreditBillReceipt objCreditBillReceipt = new clsCreditBillReceipt();

                    objCreditBillReceipt.setStrBillNo(billNo);
                    objCreditBillReceipt.setDteBillDate(billDate);
                    objCreditBillReceipt.setDblCreditAmount(creditAmount);
                    objCreditBillReceipt.setStrClientCode(clientCode);

                    listOfCreditBillReceipts.add(objCreditBillReceipt);
                }

            }
            rsCreditBillReceipts.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return listOfCreditBillReceipts;
        }
    }

    private void funPrevSettlementMode()
    {
        try
        {
            settlementNavigate--;
            if (settlementNavigate == 0)
            {
                btnPrevSettlementMode.setEnabled(false);
                btnNextSettlementMode.setEnabled(true);
                funFillSettlementButtons(0, vSettlementCode.size());
            }
            else
            {
                int startIndex = (settlementNavigate * 4);
                int endIndex = startIndex + 4;
                funFillSettlementButtons(startIndex, endIndex);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillSettlementButtons(int startIndex, int endIndex)
    {
        int cntArrayIndex = 0;
        for (int k = 0; k < 4; k++)
        {
            settlementArray[k].setVisible(false);
            settlementArray[k].setText("");
        }
        for (int cntSettlement = startIndex; cntSettlement < endIndex; cntSettlement++)
        {
            if (cntSettlement == vSettlementCode.size())
            {
                break;
            }
            if (cntArrayIndex < 4)
            {
                settlementArray[cntArrayIndex].setText(vSettlementDesc.elementAt(cntSettlement).toString());
                settlementArray[cntArrayIndex].setVisible(true);
                cntArrayIndex++;
            }
        }
    }

    /*
     * Check the Which Settlement Mode Select By User 
     */
    private void procSettlementBtnClick(String strSettleClick)
    {
        try
        {
            lblPaymentMode.setText(strSettleClick);
            panelPayModeHeader.setVisible(true);
            String sql = "select strSettelmentType,strSettelmentDesc,strSettelmentCode from tblsettelmenthd where strSettelmentDesc='" + strSettleClick + "' ";

            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                settleCode = rs.getString("strSettelmentCode");
                settleName = rs.getString("strSettelmentDesc");
                settleType = rs.getString("strSettelmentType");

                switch (rs.getString("strSettelmentType"))
                {

                    case "Cash":
                        amountBox = "PaidAmount";
                        textValue1 = "";
//                    panelAmountDetails.setVisible(true);
//                    panelAmountDetails.setLocation(pointCash);
//                    panelCoupen.setVisible(false);
//                    panelCardDetails.setVisible(false);
//                    paneLChequeDetails.setVisible(false);
                        break;

                    case "Credit Card":
                        amountBox = "PaidAmount";
                        textValue1 = "";
//                    panelCardDetails.setVisible(true);
//                    panelAmountDetails.setVisible(true);
//                    panelAmountDetails.setLocation(pointCash);
//                    panelCoupen.setVisible(false);
//                    paneLChequeDetails.setVisible(false);
                        break;

                    case "Coupon":
                        amountBox = "CouponAmount";
                        textValue1 = "";
//                    panelAmountDetails.setVisible(false);
//                    panelCoupen.setVisible(true);
//                    panelCardDetails.setVisible(false);
//                    paneLChequeDetails.setVisible(false);
//                    panelCoupen.setLocation(pointCash);
                        break;

                    case "Cheque":
                        amountBox = "PaidAmount";
                        textValue1 = "";
//                    panelAmountDetails.setVisible(true);
//                    panelAmountDetails.setLocation(pointCash);
//                    panelCoupen.setVisible(false);
//                    panelCardDetails.setVisible(false);
//                    paneLChequeDetails.setVisible(true);
//                    paneLChequeDetails.setLocation(pointCredit);
                        break;
                }
                settleMode = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funNextSettlementMode()
    {
        try
        {
            settlementNavigate++;
            int startIndex = (settlementNavigate * 4);
            int endIndex = startIndex + 4;
            int disableNext = vSettlementCode.size() / startIndex;
            funFillSettlementButtons(startIndex, endIndex);
            btnPrevSettlementMode.setEnabled(true);
            if (disableNext == settlementNavigate)
            {
                btnNextSettlementMode.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funEnterButtonClicked()
    {

        double creditAmt = Double.parseDouble(txtCreditAmount.getText());
        double paidAmt = Double.parseDouble(txtPaidAmount.getText());
        double balanceAmt = Double.parseDouble(txtBalanceAmount.getText());
        double receiptAmt = Double.parseDouble(txtReceiptAmt.getText());

        if (creditAmt == paidAmt || balanceAmt <= 0)
        {
            new frmOkPopUp(null, "Full Payment Received", "warning", 1).setVisible(true);
            return;
        }
        if (receiptAmt > balanceAmt)
        {
            double refundAmt = receiptAmt - balanceAmt;

            txtRefundAmt.setText(String.valueOf(Math.rint(refundAmt)));

            txtReceiptAmt.setText(String.valueOf(balanceAmt));
            receiptAmt = Double.parseDouble(txtReceiptAmt.getText());

        }
    }

}
