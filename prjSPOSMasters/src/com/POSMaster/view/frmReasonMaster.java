/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;

/**
 *
 * @author sss11
 */
public class frmReasonMaster extends javax.swing.JFrame
{

    private String selectQuery, insertQuery, updateQuery;
    private ResultSet countSet1, countSet;
    private String rsCode, stkIn, stkOut, voidBill, sql, transferEntry, complementary, discount, ncKOT, reprintYN;
    private String transferType, ModifyBill, PSP, voidkot, cashMgmt, voidStkIn, voidStkOut, UnsettleBill;
    private java.util.Vector vPosCode, vPosName;
    private String voidAdvOrder;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmreasonMaster
     */
    public frmReasonMaster()
    {
        initComponents();

        try
        {
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String newstr = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            vPosCode = new Vector();
            vPosName = new Vector();
            txtReasonCode.requestFocus();
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();

            fillPosCodeCombo();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to fill pos code combo
     */
    public void fillPosCodeCombo()
    {
        try
        {
            cmbPosCode.removeAllItems();
            sql = "select strPosCode,strPosName from tblposmaster";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                vPosCode.add(rs.getString(1));
                vPosName.add(rs.getString(2));
                cmbPosCode.addItem(rs.getString(2));
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetData(Object[] data)
    {
        try
        {
            sql = "select * from tblreasonmaster where strReasonCode='" + data[0].toString() + "'";

            ResultSet rsReasonData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsReasonData.next();
            txtReasonCode.setText(rsReasonData.getString(1));
            txtReasonName.setText(rsReasonData.getString(2));
            if (rsReasonData.getString(3).equals("Y"))
            {
                chkStkIn.setSelected(true);
            }
            else
            {
                chkStkIn.setSelected(false);
            }
            if (rsReasonData.getString(4).equals("Y"))
            {
                chkStkOut.setSelected(true);
            }
            else
            {
                chkStkOut.setSelected(false);
            }
            if (rsReasonData.getString(5).equals("Y"))
            {
                chkVoid.setSelected(true);
            }
            else
            {
                chkVoid.setSelected(false);
            }
            if (rsReasonData.getString(6).equals("Y"))
            {
                chkModifyBill.setSelected(true);
            }
            else
            {
                chkModifyBill.setSelected(false);
            }
            if (rsReasonData.getString(9).equals("Y"))
            {
                chkPSP.setSelected(true);
            }
            else
            {
                chkPSP.setSelected(false);
            }
            if (rsReasonData.getString(10).equals("Y"))
            {
                chkVoidKot.setSelected(true);
            }
            else
            {
                chkVoidKot.setSelected(false);
            }
            if (rsReasonData.getString(12).equals("Y"))
            {
                chkVoidStkIn.setSelected(true);
            }
            else
            {
                chkVoidStkIn.setSelected(false);
            }
            if (rsReasonData.getString(13).equals("Y"))
            {
                chkVoidStkOut.setSelected(true);
            }
            else
            {
                chkVoidStkOut.setSelected(false);
            }
            if (rsReasonData.getString(11).equals("Y"))
            {
                chkCashMgmt.setSelected(true);
            }
            else
            {
                chkCashMgmt.setSelected(false);
            }
            if (rsReasonData.getString(14).equals("Y"))
            {
                chkUnsettleBill.setSelected(true);
            }
            else
            {
                chkUnsettleBill.setSelected(false);
            }
            if (rsReasonData.getString(15).equals("Y"))
            {
                chkComplementary.setSelected(true);
            }
            else
            {
                chkComplementary.setSelected(false);
            }

            if (rsReasonData.getString(16).equals("Y"))
            {
                chkDiscount.setSelected(true);
            }
            else
            {
                chkDiscount.setSelected(false);
            }

            if (rsReasonData.getString(23).equals("Y"))
            {
                chkNcKOT.setSelected(true);
            }
            else
            {
                chkNcKOT.setSelected(false);
            }

            if (rsReasonData.getString(24).equals("Y"))
            {
                chkVoidAdvOrder.setSelected(true);
            }
            else
            {
                chkVoidAdvOrder.setSelected(false);
            }

            if (rsReasonData.getString(25).equals("Y"))
            {
                chkReprint.setSelected(true);
            }
            else
            {
                chkReprint.setSelected(false);
            }

            if (rsReasonData.getString(26).equals("Y"))
            {
                chkMoveKOT.setSelected(true);
            }
            else
            {
                chkMoveKOT.setSelected(false);
            }

            cmbTransferType.setSelectedItem(rsReasonData.getString(8).toString());

            if (rsReasonData.getString(27).equals("Y"))
            {
                chkHashTagLoyalty.setSelected(true);
            }
            else
            {
                chkHashTagLoyalty.setSelected(false);
            }

            if (rsReasonData.getString(28).equals("Y"))
            {
                chkOperational.setSelected(true);
            }
            else
            {
                chkOperational.setSelected(false);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set reason code
     *
     * @param text
     */
    public void setReasonCode(String text)
    {
        try
        {
            txtReasonCode.setText(text);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * set reason name
     *
     * @param text
     */
    public void setReasonName(String text)
    {
        try
        {
            txtReasonName.setText(text);
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
    private void funResetFields()
    {
        try
        {

            btnSave.setMnemonic('s');
            txtReasonCode.setText("");
            txtReasonName.setText("");
            txtReasonCode.requestFocus();
            chkStkIn.setSelected(false);
            chkStkOut.setSelected(false);
            chkVoid.setSelected(false);
            chkModifyBill.setSelected(false);
            chkPSP.setSelected(false);
            chkVoidKot.setSelected(false);
            chkCashMgmt.setSelected(false);
            chkVoidStkIn.setSelected(false);
            chkVoidStkOut.setSelected(false);
            chkUnsettleBill.setSelected(false);
            chkComplementary.setSelected(false);
            chkDiscount.setSelected(false);
            chkNcKOT.setSelected(false);
            chkVoidAdvOrder.setSelected(false);
            chkReprint.setSelected(false);
            btnSave.setText("SAVE");
            fillPosCodeCombo();
            cmbPosCode.setEnabled(false);
            chkMoveKOT.setSelected(false);
            
            chkHashTagLoyalty.setSelected(false);
            chkOperational.setSelected(false);

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

        jLabel2 = new javax.swing.JLabel();
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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelbody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblReasonCOde = new javax.swing.JLabel();
        txtReasonCode = new javax.swing.JTextField();
        lblReasonName = new javax.swing.JLabel();
        txtReasonName = new javax.swing.JTextField();
        lblTransaction = new javax.swing.JLabel();
        chkStkIn = new javax.swing.JCheckBox();
        chkStkOut = new javax.swing.JCheckBox();
        chkVoid = new javax.swing.JCheckBox();
        chkModifyBill = new javax.swing.JCheckBox();
        chkPSP = new javax.swing.JCheckBox();
        chkVoidKot = new javax.swing.JCheckBox();
        chkUnsettleBill = new javax.swing.JCheckBox();
        chkCashMgmt = new javax.swing.JCheckBox();
        chkVoidStkOut = new javax.swing.JCheckBox();
        chkComplementary = new javax.swing.JCheckBox();
        chkDiscount = new javax.swing.JCheckBox();
        chkNcKOT = new javax.swing.JCheckBox();
        chkVoidStkIn = new javax.swing.JCheckBox();
        cmbTransferType = new javax.swing.JComboBox();
        lblTransferType = new javax.swing.JLabel();
        lblTransferEntry = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        btnClose = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblClickHere = new javax.swing.JLabel();
        chkVoidAdvOrder = new javax.swing.JCheckBox();
        chkReprint = new javax.swing.JCheckBox();
        chkMoveKOT = new javax.swing.JCheckBox();
        chkHashTagLoyalty = new javax.swing.JCheckBox();
        chkOperational = new javax.swing.JCheckBox();

        jLabel2.setText("jLabel2");

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
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Reason Master");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelbody.setBackground(new java.awt.Color(255, 255, 255));
        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelbody.setName(""); // NOI18N
        panelbody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Reason Master");

        lblReasonCOde.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReasonCOde.setText("Reason Code     :");

        txtReasonCode.setEditable(false);
        txtReasonCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtReasonCodeMouseClicked(evt);
            }
        });
        txtReasonCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtReasonCodeKeyPressed(evt);
            }
        });

        lblReasonName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReasonName.setText("Reason Name    :");

        txtReasonName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtReasonNameMouseClicked(evt);
            }
        });
        txtReasonName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtReasonNameKeyPressed(evt);
            }
        });

        lblTransaction.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransaction.setText("Transaction       :");

        chkStkIn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkStkIn.setText("Stock In");
        chkStkIn.setOpaque(false);

        chkStkOut.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkStkOut.setText("Stock Out");
        chkStkOut.setOpaque(false);
        chkStkOut.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                chkStkOutStateChanged(evt);
            }
        });
        chkStkOut.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkStkOutActionPerformed(evt);
            }
        });

        chkVoid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoid.setText("Void Bill");
        chkVoid.setOpaque(false);

        chkModifyBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkModifyBill.setText("Modify Bill");
        chkModifyBill.setOpaque(false);

        chkPSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPSP.setText("PSP");
        chkPSP.setOpaque(false);

        chkVoidKot.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidKot.setText("VoidKot");
        chkVoidKot.setOpaque(false);

        chkUnsettleBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkUnsettleBill.setText("Unsettle Bill");
        chkUnsettleBill.setOpaque(false);

        chkCashMgmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkCashMgmt.setText("Cash Management");
        chkCashMgmt.setOpaque(false);

        chkVoidStkOut.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidStkOut.setText("Void Stock Out");
        chkVoidStkOut.setOpaque(false);

        chkComplementary.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkComplementary.setText("Complementary");
        chkComplementary.setOpaque(false);

        chkDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDiscount.setText("Discount");
        chkDiscount.setOpaque(false);

        chkNcKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkNcKOT.setText("Non Chargable KOT");
        chkNcKOT.setOpaque(false);

        chkVoidStkIn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidStkIn.setText("Void Stock In");
        chkVoidStkIn.setOpaque(false);

        cmbTransferType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Purchase", "Purchase Return", "Other" }));
        cmbTransferType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTransferTypeActionPerformed(evt);
            }
        });
        cmbTransferType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbTransferTypeKeyPressed(evt);
            }
        });

        lblTransferType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransferType.setText("Transfer Type     :");

        lblTransferEntry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransferEntry.setText("Transfer Entry    :");

        cmbPosCode.setEnabled(false);
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Close Reason Master");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
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

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setToolTipText("Save Reason");
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
        btnSave.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSaveKeyPressed(evt);
            }
        });

        lblClickHere.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblClickHere.setForeground(new java.awt.Color(0, 102, 255));
        lblClickHere.setText("Click here for Select All");
        lblClickHere.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblClickHereMouseClicked(evt);
            }
        });
        lblClickHere.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                lblClickHereKeyPressed(evt);
            }
        });

        chkVoidAdvOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidAdvOrder.setText("Void Advance Order");
        chkVoidAdvOrder.setOpaque(false);
        chkVoidAdvOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkVoidAdvOrderActionPerformed(evt);
            }
        });

        chkReprint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkReprint.setText("Reprint");
        chkReprint.setOpaque(false);
        chkReprint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkReprintActionPerformed(evt);
            }
        });

        chkMoveKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMoveKOT.setText("Move KOT");
        chkMoveKOT.setOpaque(false);
        chkMoveKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkMoveKOTActionPerformed(evt);
            }
        });

        chkHashTagLoyalty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkHashTagLoyalty.setText("Hash Tag Loyalty Interface");
        chkHashTagLoyalty.setOpaque(false);
        chkHashTagLoyalty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkHashTagLoyaltyActionPerformed(evt);
            }
        });

        chkOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkOperational.setText("Operational       :     ");
        chkOperational.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkOperational.setOpaque(false);
        chkOperational.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkOperationalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(311, 311, 311)
                                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addComponent(lblTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(cmbTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addComponent(lblTransferEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addGap(120, 120, 120)
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(panelbodyLayout.createSequentialGroup()
                                                .addComponent(chkVoidStkIn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(chkStkOut)
                                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                                        .addComponent(chkVoidStkOut, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10, 10, 10)
                                                        .addComponent(chkCashMgmt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(panelbodyLayout.createSequentialGroup()
                                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(chkComplementary, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(chkReprint))
                                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(chkDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(chkMoveKOT))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(chkVoid, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(chkNcKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addComponent(lblTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelbodyLayout.createSequentialGroup()
                                                .addComponent(chkStkIn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(150, 150, 150)
                                                .addComponent(chkModifyBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(86, 86, 86)
                                                .addComponent(chkVoidKot, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36)
                                                .addComponent(chkPSP))
                                            .addComponent(lblClickHere, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panelbodyLayout.createSequentialGroup()
                                                .addGap(396, 396, 396)
                                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(chkUnsettleBill, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(chkHashTagLoyalty)
                                                    .addComponent(chkVoidAdvOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblReasonName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblReasonCOde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtReasonCode, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtReasonName, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(chkOperational))))
                        .addGap(0, 53, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReasonCOde, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReasonCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReasonName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReasonName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(lblClickHere, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkStkIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkStkOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkModifyBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkVoidKot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkPSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkVoidStkIn)
                    .addComponent(chkVoidStkOut)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkCashMgmt)
                        .addComponent(chkUnsettleBill)))
                .addGap(13, 13, 13)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkComplementary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkDiscount)
                        .addComponent(chkNcKOT)
                        .addComponent(chkVoidAdvOrder)))
                .addGap(18, 18, 18)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkMoveKOT)
                    .addComponent(chkHashTagLoyalty)
                    .addComponent(chkReprint)
                    .addComponent(chkVoid))
                .addGap(22, 22, 22)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTransferEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtReasonCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtReasonCodeMouseClicked
        // TODO add your handling code here:
        funReasonSelectCode();
    }//GEN-LAST:event_txtReasonCodeMouseClicked

    private void txtReasonNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtReasonNameMouseClicked
        // TODO add your handling code here:
        funReasonName();
    }//GEN-LAST:event_txtReasonNameMouseClicked

    private void chkStkOutStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkStkOutStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chkStkOutStateChanged

    private void chkStkOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStkOutActionPerformed
        // TODO add your handling code here:
        if (chkStkOut.isSelected())
        {
            cmbPosCode.setEnabled(true);
        }
        else
        {
            cmbPosCode.setEnabled(false);
        }
    }//GEN-LAST:event_chkStkOutActionPerformed

    private void cmbTransferTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTransferTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTransferTypeActionPerformed

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Reason Master");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void funoperationsSaveAndUpdate()
    {
        try
        {

            stkIn = "N";
            stkOut = "N";
            voidBill = "N";
            ModifyBill = "N";
            PSP = "N";
            voidkot = "N";
            cashMgmt = "N";
            voidStkIn = "N";
            voidStkOut = "N";
            UnsettleBill = "N";
            complementary = "N";
            discount = "N";
            ncKOT = "N";
            voidAdvOrder = "N";
            reprintYN = "N";

            if (chkStkIn.isSelected() == true)
            {
                stkIn = "Y";
            }
            if (chkStkOut.isSelected() == true)
            {
                stkOut = "Y";
            }
            if (chkVoid.isSelected() == true)
            {
                voidBill = "Y";
            }
            if (chkModifyBill.isSelected() == true)
            {
                ModifyBill = "Y";
            }
            if (chkPSP.isSelected() == true)
            {
                PSP = "Y";
            }
            if (chkVoidKot.isSelected() == true)
            {
                voidkot = "Y";
            }
            if (chkCashMgmt.isSelected() == true)
            {
                cashMgmt = "Y";
            }
            if (chkVoidStkIn.isSelected() == true)
            {
                voidStkIn = "Y";
            }
            if (chkVoidStkOut.isSelected() == true)
            {
                voidStkOut = "Y";
            }
            if (chkUnsettleBill.isSelected() == true)
            {
                UnsettleBill = "Y";
            }
            if (chkComplementary.isSelected() == true)
            {
                complementary = "Y";
            }
            if (chkDiscount.isSelected() == true)
            {
                discount = "Y";
            }
            if (chkNcKOT.isSelected() == true)
            {
                ncKOT = "Y";
            }

            if (chkVoidAdvOrder.isSelected() == true)
            {
                voidAdvOrder = "Y";
            }

            if (chkReprint.isSelected())
            {
                reprintYN = "Y";
            }

            String moveKOT = "N";
            if (chkMoveKOT.isSelected())
            {
                moveKOT = "Y";
            }

            String hashTagLoyaltyYN = "N";
            if (chkHashTagLoyalty.isSelected())
            {
                hashTagLoyaltyYN = "Y";
            }

            String operational = "N";
            if (chkOperational.isSelected())
            {
                operational = "Y";
            }

            //UPdate Date 26/11/2013
            clsUtility obj = new clsUtility();
            if (btnSave.getText().equalsIgnoreCase("SAVE"))
            {
                selectQuery = "select count(*) from tblreasonmaster";
                countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet1.next();
                int cn = countSet1.getInt(1);
                countSet1.close();
                if (cn > 0)
                {
                    //selectQuery = "select max(strReasonCode) from tblreasonmaster";
                    selectQuery = "select max(cast(substring(strReasonCode,2) as UNSIGNED)) from tblreasonmaster";
                    countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    countSet.next();
                    int intCode = countSet.getInt(1);

                    intCode++;
                    if (intCode < 10)
                    {
                        rsCode = "R00" + intCode;
                    }
                    else if (intCode > 9 && intCode < 100)
                    {
                        rsCode = "R0" + intCode;
                    }
                    else
                    {
                        rsCode = "R" + intCode;
                    }
                }
                else
                {
                    rsCode = "R001";
                }

                String name = txtReasonName.getText().trim();
                String code = txtReasonCode.getText().trim();
                if (clsGlobalVarClass.funCheckItemName("tblreasonmaster", "strReasonName", "strReasonCode", name, code, "save", ""))
                {
                    new frmOkPopUp(this, "This Reason Name is Already Exist", "Error", 0).setVisible(true);
                    txtReasonName.requestFocus();
                }
                else if (!clsGlobalVarClass.validateEmpty(txtReasonName.getText()))
                {
                    new frmOkPopUp(this, "Enter Reason Name Please", "Error", 0).setVisible(true);
                    txtReasonName.setText("");
                    txtReasonName.requestFocus();
                }
                else if (!obj.funCheckLength(txtReasonName.getText(), 50))
                {
                    new frmOkPopUp(this, "ReasonName length must be less than 50", "Error", 0).setVisible(true);
                    txtReasonName.requestFocus();
                }
                else
                {
                    transferType = cmbTransferType.getSelectedItem().toString();
                    if (chkStkOut.isSelected())
                    {
                        transferEntry = String.valueOf(vPosCode.elementAt(cmbPosCode.getSelectedIndex()));
                    }
                    else
                    {
                        transferEntry = "none";
                    }
                    txtReasonCode.setText(rsCode);
                    insertQuery = "insert into tblreasonmaster(strReasonCode,strReasonName,strStkIn,strStkOut,"
                            + "strVoidBill,strModifyBill,strTransferEntry,strTransferType,strPSP,strKot,"
                            + "strCashMgmt,strVoidStkIn,strVoidStkOut,strUnsettleBill,strComplementary,strDiscount,"
                            + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strNCKOT,strVoidAdvOrder,strReprint"
                            + ",strMoveKOT,strHashTagLoyalty,strOperational) "
                            + "values('" + txtReasonCode.getText() + "','" + txtReasonName.getText() + "','" + stkIn + "','"
                            + stkOut + "','" + voidBill + "','" + ModifyBill + "','" + transferEntry + "','" + transferType + "','"
                            + PSP + "','" + voidkot + "','" + cashMgmt + "','" + voidStkIn + "','"
                            + voidStkOut + "','" + UnsettleBill + "','" + complementary + "','" + discount + "','"
                            + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                            + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
                            + "'" + ncKOT + "','" + voidAdvOrder + "','" + reprintYN + "','" + moveKOT + "'"
                            + ",'" + hashTagLoyaltyYN + "','" + operational + "')";
                    //System.out.println(insertQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
                    if (exc > 0)
                    {
                        sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Reason' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetFields();
                    }
                }
            }
            else
            {

                String name = txtReasonName.getText().trim();
                String code = txtReasonCode.getText().trim();
                if (clsGlobalVarClass.funCheckItemName("tblreasonmaster", "strReasonName", "strReasonCode", name, code, "update", ""))
                {
                    new frmOkPopUp(this, "This Reason Name is Already Exist", "Error", 0).setVisible(true);
                    txtReasonName.requestFocus();
                }
                else if (txtReasonName.getText().length() == 0)
                {
                    new frmOkPopUp(this, "Enter Reason Name Please", "Error", 0).setVisible(true);
                }
                else if (!obj.funCheckLength(txtReasonName.getText(), 50))
                {
                    new frmOkPopUp(this, "ReasonName length must be less than 50", "Error", 0).setVisible(true);
                    txtReasonName.requestFocus();
                }
                else
                {
                    transferType = cmbTransferType.getSelectedItem().toString();
                    updateQuery = "UPDATE tblreasonmaster SET strReasonName = '" + txtReasonName.getText()
                            + "',strStkIn='" + stkIn + "',strStkOut='" + stkOut + "',strVoidBill='" + voidBill
                            + "',strModifyBill='" + ModifyBill + "',strTransferEntry='" + transferEntry + "',strTransferType='" + transferType
                            + "',strPSP='" + PSP + "',strKot='" + voidkot + "',strCashMgmt='" + cashMgmt
                            + "',strVoidStkIn='" + voidStkIn + "',StrVoidStkOut='" + voidStkOut
                            + "',strUnsettleBill='" + UnsettleBill + "',strComplementary='" + complementary + "',strDiscount='" + discount
                            + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='"
                            + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N' ,strNCKOT='" + ncKOT + "',strVoidAdvOrder='" + voidAdvOrder + "'"
                            + ",strReprint='" + reprintYN + "',strMoveKOT='" + moveKOT + "'"
                            + ",strHashTagLoyalty='" + hashTagLoyaltyYN + "',strOperational='" + operational + "' "
                            + "WHERE strReasonCode ='" + txtReasonCode.getText() + "'";
                    //System.out.println(updateQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                    if (exc > 0)
                    {
                        sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Reason' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                        funResetFields();
                    }
                }
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry"))
            {
                new frmOkPopUp(this, "Reason Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }
    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        funoperationsSaveAndUpdate();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void chkVoidAdvOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVoidAdvOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkVoidAdvOrderActionPerformed

    private void lblClickHereMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClickHereMouseClicked
        // TODO add your handling code here:
        chkStkIn.setSelected(true);
        chkStkOut.setSelected(true);
        chkVoid.setSelected(true);
        chkModifyBill.setSelected(true);
        chkPSP.setSelected(true);
        chkVoidStkIn.setSelected(true);
        chkVoidStkOut.setSelected(true);
        chkCashMgmt.setSelected(true);
        chkUnsettleBill.setSelected(true);
        chkComplementary.setSelected(true);
        chkDiscount.setSelected(true);
        chkNcKOT.setSelected(true);
        chkVoidAdvOrder.setSelected(true);
        chkVoidKot.setSelected(true);
        chkReprint.setSelected(true);
        chkMoveKOT.setSelected(true);
        chkHashTagLoyalty.setSelected(true);

    }//GEN-LAST:event_lblClickHereMouseClicked

    private void txtReasonCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funReasonSelectCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtReasonName.requestFocus();
        }
    }//GEN-LAST:event_txtReasonCodeKeyPressed

    private void txtReasonNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            lblClickHere.requestFocus();
        }

    }//GEN-LAST:event_txtReasonNameKeyPressed

    private void lblClickHereKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblClickHereKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbTransferType.requestFocus();
        }
    }//GEN-LAST:event_lblClickHereKeyPressed

    private void cmbTransferTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTransferTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSave.requestFocus();
        }
    }//GEN-LAST:event_cmbTransferTypeKeyPressed

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funoperationsSaveAndUpdate();
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        funoperationsSaveAndUpdate();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Reason Master");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkReprintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkReprintActionPerformed
    {//GEN-HEADEREND:event_chkReprintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkReprintActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Reason Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Reason Master");
    }//GEN-LAST:event_formWindowClosing

    private void chkMoveKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMoveKOTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkMoveKOTActionPerformed

    private void chkHashTagLoyaltyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkHashTagLoyaltyActionPerformed
    {//GEN-HEADEREND:event_chkHashTagLoyaltyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkHashTagLoyaltyActionPerformed

    private void chkOperationalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkOperationalActionPerformed
    {//GEN-HEADEREND:event_chkOperationalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkOperationalActionPerformed

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
            java.util.logging.Logger.getLogger(frmReasonMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmReasonMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmReasonMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmReasonMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmReasonMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkCashMgmt;
    private javax.swing.JCheckBox chkComplementary;
    private javax.swing.JCheckBox chkDiscount;
    private javax.swing.JCheckBox chkHashTagLoyalty;
    private javax.swing.JCheckBox chkModifyBill;
    private javax.swing.JCheckBox chkMoveKOT;
    private javax.swing.JCheckBox chkNcKOT;
    private javax.swing.JCheckBox chkOperational;
    private javax.swing.JCheckBox chkPSP;
    private javax.swing.JCheckBox chkReprint;
    private javax.swing.JCheckBox chkStkIn;
    private javax.swing.JCheckBox chkStkOut;
    private javax.swing.JCheckBox chkUnsettleBill;
    private javax.swing.JCheckBox chkVoid;
    private javax.swing.JCheckBox chkVoidAdvOrder;
    private javax.swing.JCheckBox chkVoidKot;
    private javax.swing.JCheckBox chkVoidStkIn;
    private javax.swing.JCheckBox chkVoidStkOut;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbTransferType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblClickHere;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReasonCOde;
    private javax.swing.JLabel lblReasonName;
    private javax.swing.JLabel lblTransaction;
    private javax.swing.JLabel lblTransferEntry;
    private javax.swing.JLabel lblTransferType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JTextField txtReasonCode;
    private javax.swing.JTextField txtReasonName;
    // End of variables declaration//GEN-END:variables
    /**
     * This method is used to select reason code
     */
    private void funReasonSelectCode()
    {
        try
        {
            //new frmSearchForm(this,"frmReason").setVisible(true);
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Reason");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnSave.setText("UPDATE");//UpdateD
                btnSave.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select reason name
     */
    private void funReasonName()
    {
        try
        {
            if (txtReasonName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Reason Name").setVisible(true);
                txtReasonName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtReasonName.getText(), "1", "Enter Reason Name").setVisible(true);
                txtReasonName.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

}
