/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsTextFieldOnlyNumber;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.POSGlobal.controller.clsUtility;

public class frmCustAreaMaster extends javax.swing.JFrame
{
    private String buldingCode;
    private String strCode, sql;
    DefaultTableModel dmDeliverycharges;
    private HashMap<String, String> mapCustomerType;
    private String zoneCode = "", dBoyPayOut = "0.00", helperPayOut = "0.00";
    clsUtility objUtility = new clsUtility();
    
    public frmCustAreaMaster()
    {
        initComponents();
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
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
        clsGlobalVarClass.gFormNameOnKeyBoard = "Bulid Code Or Name";
        txtBuildingCode.requestFocus();
        txtHomeDeliCharge.setDocument(new clsTextFieldOnlyNumber(20, 3).new JNumberFieldFilter());
        txtHomeDeliCharge.setText("0.00");
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        dmDeliverycharges = (DefaultTableModel) tblDeliveryCharge.getModel();
        mapCustomerType = new HashMap<String, String>();

        String sql = "select a.strCustTypeCode ,a.strCustType \n"
            + "from tblcustomertypemaster a order by a.strCustTypeCode";
        try
        {
            ResultSet rsDelBoyCateories = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsDelBoyCateories.next())
            {
                mapCustomerType.put(rsDelBoyCateories.getString(2), rsDelBoyCateories.getString(1));
            }
            rsDelBoyCateories.close();
        }
        catch (Exception ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }

        Object[] strValues = mapCustomerType.keySet().toArray();
        cmboBoxCustomerType.setModel(new DefaultComboBoxModel(strValues));
        funSetShortCutKeys();
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is to add delivery charge row
     *
     * @return
     */
    private void funDeliveryChargeAddRow()
    {
        try
        {
            Object[] column = new Object[5];
            column[0] = txtFromBillAmount.getText();
            column[1] = txtToBillAmount.getText();
            column[2] = txtDeliverycharges.getText();
            column[3] = cmboBoxCustomerType.getSelectedItem().toString();
            column[4] = false;

            dmDeliverycharges.addRow(column);
            tblDeliveryCharge.setModel(dmDeliverycharges);
            tblDeliveryCharge.setRowHeight(30);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblDeliveryCharge.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
            tblDeliveryCharge.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblDeliveryCharge.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblDeliveryCharge.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

            tblDeliveryCharge.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblDeliveryCharge.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblDeliveryCharge.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblDeliveryCharge.getColumnModel().getColumn(3).setPreferredWidth(100);

            tblDeliveryCharge.setSize(700, 900);

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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };
        panelBody = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        panelTabAreaMaster = new javax.swing.JPanel();
        lblAreaName = new javax.swing.JLabel();
        txtBuildingCode = new javax.swing.JTextField();
        txtBuildingName = new javax.swing.JTextField();
        txtBuldingAddress = new javax.swing.JTextField();
        lblAddress = new javax.swing.JLabel();
        lblAreaCode = new javax.swing.JLabel();
        lblFormName = new javax.swing.JLabel();
        lblHomeDeliCharge = new javax.swing.JLabel();
        txtHomeDeliCharge = new javax.swing.JTextField();
        lblDeliveryBoyPayOut = new javax.swing.JLabel();
        txtZoneCode = new javax.swing.JTextField();
        lblCustAreaType1 = new javax.swing.JLabel();
        txtHelperPayOut = new javax.swing.JTextField();
        lblHelperPayOut = new javax.swing.JLabel();
        txtDeliveryBoyPayOut = new javax.swing.JTextField();
        panelTabDeliveryCharges = new javax.swing.JPanel();
        txtDeliverycharges = new javax.swing.JTextField();
        txtToBillAmount = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane();
        tblDeliveryCharge = new javax.swing.JTable();
        btnReset1 = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnResetAll = new javax.swing.JButton();
        txtFromBillAmount = new javax.swing.JTextField();
        lblKM = new javax.swing.JLabel();
        btnRemove = new javax.swing.JButton();
        lblAmount = new javax.swing.JLabel();
        cmboBoxCustomerType = new javax.swing.JComboBox();
        lblKM1 = new javax.swing.JLabel();
        lblCustType = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

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
        lblformName.setText("- Customer Area Master");
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

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        panelTabAreaMaster.setBackground(new java.awt.Color(255, 255, 255));
        panelTabAreaMaster.setOpaque(false);

        lblAreaName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAreaName.setText("Area Name                       :");

        txtBuildingCode.setEditable(false);
        txtBuildingCode.setBackground(new java.awt.Color(204, 204, 204));
        txtBuildingCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBuildingCodeMouseClicked(evt);
            }
        });
        txtBuildingCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuildingCodeKeyPressed(evt);
            }
        });

        txtBuildingName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBuildingNameMouseClicked(evt);
            }
        });
        txtBuildingName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuildingNameKeyPressed(evt);
            }
        });

        txtBuldingAddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBuldingAddressMouseClicked(evt);
            }
        });
        txtBuldingAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuldingAddressKeyPressed(evt);
            }
        });

        lblAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAddress.setText("Address                           :");

        lblAreaCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAreaCode.setText("Area Code                        :");

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Customer Area Master");

        lblHomeDeliCharge.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomeDeliCharge.setText("Home Delivery Charges      :");

        txtHomeDeliCharge.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHomeDeliCharge.setText("0.00");
        txtHomeDeliCharge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHomeDeliChargeMouseClicked(evt);
            }
        });
        txtHomeDeliCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHomeDeliChargeActionPerformed(evt);
            }
        });
        txtHomeDeliCharge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHomeDeliChargeKeyPressed(evt);
            }
        });

        lblDeliveryBoyPayOut.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryBoyPayOut.setText("Delivery Boy Pay Out         :");

        txtZoneCode.setEditable(false);
        txtZoneCode.setBackground(new java.awt.Color(204, 204, 204));
        txtZoneCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtZoneCodeMouseClicked(evt);
            }
        });
        txtZoneCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZoneCodeActionPerformed(evt);
            }
        });
        txtZoneCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtZoneCodeKeyPressed(evt);
            }
        });

        lblCustAreaType1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAreaType1.setText("Zone                               :");

        txtHelperPayOut.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHelperPayOut.setText("0.00");
        txtHelperPayOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHelperPayOutMouseClicked(evt);
            }
        });
        txtHelperPayOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHelperPayOutActionPerformed(evt);
            }
        });
        txtHelperPayOut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHelperPayOutKeyPressed(evt);
            }
        });

        lblHelperPayOut.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHelperPayOut.setText("Helper Pay Out                 :");

        txtDeliveryBoyPayOut.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDeliveryBoyPayOut.setText("0.00");
        txtDeliveryBoyPayOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDeliveryBoyPayOutMouseClicked(evt);
            }
        });
        txtDeliveryBoyPayOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeliveryBoyPayOutActionPerformed(evt);
            }
        });
        txtDeliveryBoyPayOut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeliveryBoyPayOutKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelTabAreaMasterLayout = new javax.swing.GroupLayout(panelTabAreaMaster);
        panelTabAreaMaster.setLayout(panelTabAreaMasterLayout);
        panelTabAreaMasterLayout.setHorizontalGroup(
            panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                        .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHomeDeliCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lblCustAreaType1)
                                .addComponent(lblAreaName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDeliveryBoyPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuldingAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtZoneCode, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBuildingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHomeDeliCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDeliveryBoyPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                        .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                                .addGap(203, 203, 203)
                                .addComponent(txtBuildingName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                                .addComponent(lblHelperPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(txtHelperPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTabAreaMasterLayout.setVerticalGroup(
            panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabAreaMasterLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAreaCode)
                    .addComponent(txtBuildingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuildingName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAreaName))
                .addGap(18, 18, 18)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuldingAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress))
                .addGap(18, 18, 18)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHomeDeliCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomeDeliCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtZoneCode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustAreaType1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeliveryBoyPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDeliveryBoyPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTabAreaMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHelperPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHelperPayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        tabPane.addTab("Area Master", panelTabAreaMaster);

        panelTabDeliveryCharges.setBackground(new java.awt.Color(255, 255, 255));
        panelTabDeliveryCharges.setOpaque(false);
        panelTabDeliveryCharges.setPreferredSize(new java.awt.Dimension(610, 600));
        panelTabDeliveryCharges.setLayout(null);

        txtDeliverycharges.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDeliverycharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDeliverychargesMouseClicked(evt);
            }
        });
        txtDeliverycharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeliverychargesActionPerformed(evt);
            }
        });
        txtDeliverycharges.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeliverychargesKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(txtDeliverycharges);
        txtDeliverycharges.setBounds(130, 70, 140, 30);

        txtToBillAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtToBillAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtToBillAmountMouseClicked(evt);
            }
        });
        txtToBillAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtToBillAmountKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(txtToBillAmount);
        txtToBillAmount.setBounds(290, 20, 140, 30);

        tblDeliveryCharge.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Bill Amt", "To Bill Amt", "Delivery charges", "Customer Type", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDeliveryCharge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDeliveryChargeMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblDeliveryCharge);

        panelTabDeliveryCharges.add(scrollPane);
        scrollPane.setBounds(0, 170, 790, 280);

        btnReset1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset1.setText("Reset");
        btnReset1.setToolTipText("Reset All Fields");
        btnReset1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset1ActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnReset1);
        btnReset1.setBounds(410, 120, 90, 40);

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnClose);
        btnClose.setBounds(480, 510, 90, 30);

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Delivery Charges");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnAdd);
        btnAdd.setBounds(120, 120, 90, 40);

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnSave);
        btnSave.setBounds(110, 510, 90, 30);

        btnResetAll.setBackground(new java.awt.Color(255, 255, 255));
        btnResetAll.setForeground(new java.awt.Color(255, 255, 255));
        btnResetAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetAll.setText("Reset");
        btnResetAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetAll.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnResetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetAllActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnResetAll);
        btnResetAll.setBounds(300, 510, 90, 30);

        txtFromBillAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFromBillAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFromBillAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFromBillAmountMouseClicked(evt);
            }
        });
        txtFromBillAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromBillAmountActionPerformed(evt);
            }
        });
        txtFromBillAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFromBillAmountKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(txtFromBillAmount);
        txtFromBillAmount.setBounds(130, 20, 140, 30);

        lblKM.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKM.setText("Delivery Charges  :");
        panelTabDeliveryCharges.add(lblKM);
        lblKM.setBounds(10, 70, 120, 30);

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setToolTipText(" Remove Delivery Charges");
        btnRemove.setEnabled(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnRemove);
        btnRemove.setBounds(270, 120, 100, 40);

        lblAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAmount.setText("Amount             :");
        panelTabDeliveryCharges.add(lblAmount);
        lblAmount.setBounds(10, 24, 110, 20);

        cmboBoxCustomerType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--select--" }));
        cmboBoxCustomerType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmboBoxCustomerTypeKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(cmboBoxCustomerType);
        cmboBoxCustomerType.setBounds(400, 70, 140, 30);

        lblKM1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKM1.setText("Delivery Charges  :");
        panelTabDeliveryCharges.add(lblKM1);
        lblKM1.setBounds(10, 70, 120, 30);

        lblCustType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustType.setText("Customer Type  : ");
        panelTabDeliveryCharges.add(lblCustType);
        lblCustType.setBounds(290, 70, 100, 30);

        tabPane.addTab("Delivery Charges", panelTabDeliveryCharges);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Customer Area  Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNewKeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET All");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close  Customer Area  Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
 private void funSelectBuilding()
    {
        try
        {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("BuildingMaster");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");//UpdateD
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                setBuildingData(data);
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
     * This method is used to build name
     *
     * @return
     */
    private void funBuildingName()
    {
        if (txtBuildingName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(null, true, "1", "Enter Building Name").setVisible(true);
            txtBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(null, true, txtBuildingName.getText(), "1", "Enter Building Name").setVisible(true);
            txtBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }

    /**
     * This method is used to build address
     *
     */
    public void funBuildingAddress()
    {
        if (txtBuldingAddress.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(null, true, "1", "Enter Building Area").setVisible(true);
            txtBuldingAddress.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(null, true, txtBuldingAddress.getText(), "1", "Enter Building Area").setVisible(true);
            txtBuldingAddress.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }

    /**
     * This method is used to build home delivery charge
     *
     * @return
     */
    public void funBuildingHomeDeliCharge()
    {
        if (txtHomeDeliCharge.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true,"","Double","Enter Home Delivery Charge").setVisible(true);
            txtHomeDeliCharge.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtHomeDeliCharge.getText(),"Double", "Enter Home Delivery Charge").setVisible(true);
            txtHomeDeliCharge.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }

    public void funDeliveryBoyPayOutCharges()
    {
        if (txtDeliveryBoyPayOut.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Delivery Boy Pay Out Charge").setVisible(true);
            txtDeliveryBoyPayOut.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtDeliveryBoyPayOut.getText(),"Double", "Enter Delivery Boy Pay Out Charge").setVisible(true);
            txtDeliveryBoyPayOut.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }

    public void funHelperPayOutCharges()
    {
        if (txtHelperPayOut.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double" ,"Enter Helper Pay Charge Charge").setVisible(true);
            txtHelperPayOut.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtHelperPayOut.getText(),"Double" ,"Enter Helper Pay Charge Charge").setVisible(true);
            txtHelperPayOut.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }

    /**
     * This method is used to save master
     *
     */
    private void funSaveButton()
    {
        try
        {
            ResultSet countSet = null, countSet1 = null;
            String selectQuery = "", insertQuery = "";
            String code = "";
            long lastNo = 1;

            selectQuery = "select count(*) from tblbuildingmaster";
            countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            countSet1.next();
            int cn = countSet1.getInt(1);
            countSet1.close();
            if (cn > 0)
            {
                selectQuery = "select max(strBuildingCode) from tblbuildingmaster";
                countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet.next();
                code = countSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 1).toString();
                for (int i = 0; i < ss.length(); i++)
                {
                    if (ss.charAt(i) != '0')
                    {
                        strCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                lastNo = Long.parseLong(strCode);
                lastNo++;
                buldingCode = "B" + String.format("%07d", lastNo);
            }
            else
            {
                sql = "select longCustSeries from tblsetup";
                ResultSet rsCustSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsCustSeries.next())
                {
                    lastNo = Long.parseLong(rsCustSeries.getString(1));
                }
                rsCustSeries.close();
                buldingCode = "B" + String.format("%07d", lastNo);
            }

            if (!clsGlobalVarClass.validateEmpty(txtBuildingName.getText()))
            {
                new frmOkPopUp(null, "Please enter Building Name", "Error", 0).setVisible(true);
                txtBuildingName.setText("");
                txtBuldingAddress.setText("");
                txtBuildingName.requestFocus();
            }
            else
            {
                String hdCharges = "0.00";

                if (txtHomeDeliCharge.getText().trim().length() == 0)
                {
                    hdCharges = "0.00";
                }
                else
                {
                    hdCharges = txtHomeDeliCharge.getText();
                }
                if (txtDeliveryBoyPayOut.getText().trim().length() == 0)
                {
                    dBoyPayOut = "0.00";
                }
                else
                {
                    dBoyPayOut = txtDeliveryBoyPayOut.getText();
                }
                if (txtHelperPayOut.getText().trim().length() == 0)
                {
                    helperPayOut = "0.00";
                }
                else
                {
                    helperPayOut = txtHelperPayOut.getText();
                }
                txtBuildingCode.setText(buldingCode);
                insertQuery = "insert into tblbuildingmaster (strBuildingCode,strBuildingName"
                        + ",strAddress,strUserCreated,strUserEdited,dteDateCreated"
                        + ",dteDateEdited,dblHomeDeliCharge,strClientCode,dblDeliveryBoyPayOut"
                        + ",dblHelperPayOut,strZoneCode) "
                        + "values('" + txtBuildingCode.getText() + "','"
                        + txtBuildingName.getText() + "','" + txtBuldingAddress.getText() + "','"
                        + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                        + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + hdCharges + "','" + clsGlobalVarClass.gClientCode + "','" + dBoyPayOut + "'"
                        + ",'" + helperPayOut + "','" +zoneCode+ "' )";
                //System.out.println(insertQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
                if (tblDeliveryCharge.getRowCount() > 0)
                {
                    funDCSave();
                }
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Building' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(null, "Entry added Successfully", "Successful", 4).setVisible(true);
                    funResetField();
                    clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry"))
            {
                new frmOkPopUp(null, "Building Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }

    }

    /**
     * This method is used to update master
     *
     * @return
     */
    private void funUpdateButton()
    {
        try
        {
            String updateQuery = "";
            if (!clsGlobalVarClass.validateEmpty(txtBuildingName.getText()))
            {
                new frmOkPopUp(null, "Please Building  Name", "Error", 0).setVisible(true);
                txtBuildingName.setText("");
                txtBuldingAddress.setText("");
                txtBuildingName.requestFocus();
            }
            else
            {
                String hdCharges = "0.00";
                if (txtHomeDeliCharge.getText().trim().length() == 0)
                {
                    hdCharges = "0.00";
                }
                else
                {
                    hdCharges = txtHomeDeliCharge.getText();
                }
                if (txtDeliveryBoyPayOut.getText().trim().length() == 0)
                {
                    dBoyPayOut = "0.00";
                }
                else
                {
                    dBoyPayOut = txtDeliveryBoyPayOut.getText();
                }
                if (txtHelperPayOut.getText().trim().length() == 0)
                {
                    helperPayOut = "0.00";
                }
                else
                {
                    helperPayOut = txtHelperPayOut.getText();
                }
                updateQuery = "UPDATE tblbuildingmaster SET strBuildingName = '" + txtBuildingName.getText()
                        + "',strAddress='" + txtBuldingAddress.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode
                        + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime()
                        + "',dblHomeDeliCharge='" + hdCharges 
                        + "',dblDeliveryBoyPayOut='" + dBoyPayOut 
                        + "',dblHelperPayOut='" + helperPayOut + "' "
                        + ",strZoneCode='"+zoneCode+"' "
                        + "WHERE strBuildingCode ='" + txtBuildingCode.getText() + "'";
                // System.out.println(updateQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);

                updateQuery = "delete from tblareawisedc where strBuildingCode='" + txtBuildingCode.getText() + "' ";
                clsGlobalVarClass.dbMysql.execute(updateQuery);
                if (tblDeliveryCharge.getRowCount() > 0)
                {
                    funDCSave();
                }
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Building' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(null, "Updated Successfully", "Successful", 4).setVisible(true);
                    funResetField();
                    clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    /**
     * This method is used to save delivery charges
     *
     * @return int
     */
    private int funDCSave() throws Exception
    {
        sql = "Insert into tblareawisedc (strBuildingCode,dblBillAmount,dblBillAmount1,dblDeliveryCharges"
                + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strCustTypeCode) "
                + "VALUES";
        for (int cnt = 0; cnt < tblDeliveryCharge.getRowCount(); cnt++)
        {
            sql += "('" + txtBuildingCode.getText() + "'," + tblDeliveryCharge.getValueAt(cnt, 0).toString() + ""
                    + "," + tblDeliveryCharge.getValueAt(cnt, 1) + "," + tblDeliveryCharge.getValueAt(cnt, 2) + ""
                    + ",'" + clsGlobalVarClass.gUserCode + "'" + ",'" + clsGlobalVarClass.gUserCode + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "'" + ",'" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','" + mapCustomerType.get(tblDeliveryCharge.getValueAt(cnt, 3)) + "'),";
        }
        StringBuilder sb = new StringBuilder(sql);
        int ind = sb.lastIndexOf(",");
        sql = sb.delete(ind, sb.length()).toString();
        clsGlobalVarClass.dbMysql.execute(sql);

        return 1;
    }

    /**
     * This method is used to reset DC
     *
     */
    private void funResetDC()
    {
        txtFromBillAmount.setText("");
        txtToBillAmount.setText("");
        txtDeliverycharges.setText("");
        cmboBoxCustomerType.setSelectedIndex(0);
    }

    /**
     * This method is used to check bill amount
     *
     * @return boolean
     */
    private boolean funCheckBillAmount()
    {
        Double DeliveryChargeBillAmount = Double.parseDouble(txtFromBillAmount.getText());
        Double BetweenBillAmount1 = Double.parseDouble(txtToBillAmount.getText());

        if (BetweenBillAmount1 > DeliveryChargeBillAmount)
        {
            JOptionPane.showMessageDialog(this, "Invaild from  and TO Bill Amount");
            txtFromBillAmount.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * This method is used to check table amount
     *
     * @param tblFromBillAmt
     * @param tblToBillAmt
     * @return boolean
     */
    private boolean funCheckTableAmount(double tblFromBillAmt, double tblToBillAmt)
    {        
        boolean flgResult = true;
        for (int i = 0; i < tblDeliveryCharge.getRowCount(); i++)
        {
            String fromBillAmt = tblDeliveryCharge.getValueAt(i, 0).toString();
            tblFromBillAmt = Double.parseDouble(fromBillAmt);
            String toBillAmt = tblDeliveryCharge.getValueAt(i, 1).toString();
            tblToBillAmt = Double.parseDouble(toBillAmt);
            String tableCustType=tblDeliveryCharge.getValueAt(i, 3).toString();
            
            if (!(tblToBillAmt < Double.parseDouble(txtFromBillAmount.getText()) && tblToBillAmt < Double.parseDouble(txtToBillAmount.getText()))
                && cmboBoxCustomerType.getSelectedItem().toString().equals(tableCustType) )
            {
                flgResult = false;
                JOptionPane.showMessageDialog(this, "The Amount is Already Exist");
                break;
            }
        }
        return flgResult;
    }

    private void txtBuildingCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuildingCodeMouseClicked
        // TODO add your handling code here:
        funSelectBuilding();
    }//GEN-LAST:event_txtBuildingCodeMouseClicked

    private void txtBuildingNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuildingNameMouseClicked
        // TODO add your handling code here:
        funBuildingName();
    }//GEN-LAST:event_txtBuildingNameMouseClicked

    private void txtBuildingNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuildingNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtBuldingAddress.requestFocus();
        }
    }//GEN-LAST:event_txtBuildingNameKeyPressed

    private void txtBuldingAddressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuldingAddressMouseClicked
        // TODO add your handling code here:
        funBuildingAddress();
    }//GEN-LAST:event_txtBuldingAddressMouseClicked

    private void txtBuldingAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuldingAddressKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtHomeDeliCharge.requestFocus();
        }
    }//GEN-LAST:event_txtBuldingAddressKeyPressed

    private void txtHomeDeliChargeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHomeDeliChargeMouseClicked
        // TODO add your handling code here:
        funBuildingHomeDeliCharge();
    }//GEN-LAST:event_txtHomeDeliChargeMouseClicked

    private void txtHomeDeliChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHomeDeliChargeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHomeDeliChargeActionPerformed

    private void txtHomeDeliChargeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHomeDeliChargeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtZoneCode.requestFocus();
        }
    }//GEN-LAST:event_txtHomeDeliChargeKeyPressed

    private void txtDeliverychargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDeliverychargesMouseClicked
        // TODO add your handling code here:
        if (txtDeliverycharges.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Delivery Charges").setVisible(true);
            txtDeliverycharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtDeliverycharges.getText(),"Double", "Enter Delivery Charges").setVisible(true);
            txtDeliverycharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtDeliverychargesMouseClicked

    private void txtDeliverychargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeliverychargesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeliverychargesActionPerformed

    private void txtDeliverychargesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeliverychargesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            clsUtility obj=new clsUtility();
            if (!obj.funCheckDouble(txtFromBillAmount.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Delivery Charges Bill Amount1");
                return;
            }
            if (!obj.funCheckDouble(txtToBillAmount.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Delivery Charges Bill Amount2");
                return;
            }
            if (!obj.funCheckDouble(txtDeliverycharges.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Delivery charges");
                return;
            }
            if (Double.parseDouble(txtFromBillAmount.getText().trim()) < 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in BillAmount1");
                txtFromBillAmount.requestFocus();
                return;
            }
            if (Double.parseDouble(txtToBillAmount.getText().trim()) < 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in BillAmount2");
                txtToBillAmount.requestFocus();
                return;
            }
            if (Double.parseDouble(txtDeliverycharges.getText().trim()) < 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in Delivery Charges");
                txtDeliverycharges.requestFocus();
                return;
            }
            if (Double.parseDouble(txtFromBillAmount.getText()) > Double.parseDouble(txtToBillAmount.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild from and To Bill Amount");
                txtFromBillAmount.requestFocus();
                return;
            }
            if (!funCheckTableAmount(Double.parseDouble(txtFromBillAmount.getText()), Double.parseDouble(txtToBillAmount.getText())))
            {
                return;
            }
            funDeliveryChargeAddRow();

            txtFromBillAmount.setText("");
            txtToBillAmount.setText("");
            txtDeliverycharges.setText("");
            txtFromBillAmount.requestFocus();
            return;
        }
    }//GEN-LAST:event_txtDeliverychargesKeyPressed

    private void txtToBillAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtToBillAmountMouseClicked
        // TODO add your handling code here:
        if (txtToBillAmount.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Bill Amount").setVisible(true);
            txtToBillAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtToBillAmount.getText(),"Double", "Enter Bill Amount").setVisible(true);
            txtToBillAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtToBillAmountMouseClicked

    private void txtToBillAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtToBillAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtDeliverycharges.requestFocus();
        }

    }//GEN-LAST:event_txtToBillAmountKeyPressed

    private void tblDeliveryChargeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDeliveryChargeMouseClicked
        funCheckSelection();
    }//GEN-LAST:event_tblDeliveryChargeMouseClicked

    private void btnReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset1ActionPerformed
        funResetDC();
    }//GEN-LAST:event_btnReset1ActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
    private void funAddDeliveryCharges()
    {
        clsUtility obj=new clsUtility();
        if (!obj.funCheckDouble(txtFromBillAmount.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Input Delivery Charges Bill Amount1");
            return;
        }
        if (!obj.funCheckDouble(txtToBillAmount.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Input Delivery Charges Bill Amount2");
            return;
        }
        if (!obj.funCheckDouble(txtDeliverycharges.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Input Delivery charges");
            return;
        }
        if (Double.parseDouble(txtFromBillAmount.getText().trim()) < 0)
        {
            JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in BillAmount1");
            txtFromBillAmount.requestFocus();
            return;
        }
        if (Double.parseDouble(txtToBillAmount.getText().trim()) < 0)
        {
            JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in BillAmount2");
            txtToBillAmount.requestFocus();
            return;
        }
        if (Double.parseDouble(txtDeliverycharges.getText().trim()) < 0)
        {
            JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in Delivery Charges");
            txtDeliverycharges.requestFocus();
            return;
        }
        if (Double.parseDouble(txtFromBillAmount.getText()) > Double.parseDouble(txtToBillAmount.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild from and To Bill Amount");
            txtFromBillAmount.requestFocus();
            return;
        }
        if (!funCheckTableAmount(Double.parseDouble(txtFromBillAmount.getText()), Double.parseDouble(txtToBillAmount.getText())))
        {
            return;
        }
        funDeliveryChargeAddRow();

        txtFromBillAmount.setText("");
        txtToBillAmount.setText("");
        txtDeliverycharges.setText("");
        txtFromBillAmount.requestFocus();
        return;
    }

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        funAddDeliveryCharges();
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funAddDeliveryCharges();
        }
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnResetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetAllActionPerformed

    private void txtFromBillAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFromBillAmountMouseClicked
        // TODO add your handling code here:
        if (txtFromBillAmount.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double" ,"Enter Bill Amount").setVisible(true);
            txtFromBillAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtFromBillAmount.getText(),"Double" ,"Enter Bill Amount").setVisible(true);
            txtFromBillAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtFromBillAmountMouseClicked

    private void txtFromBillAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromBillAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromBillAmountActionPerformed

    private void txtFromBillAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFromBillAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtToBillAmount.requestFocus();
        }
    }//GEN-LAST:event_txtFromBillAmountKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        funRemoveRow();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveButton();
        }
        else
        {
            funUpdateButton();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Customer Area Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtZoneCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZoneCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZoneCodeActionPerformed
    public void funSelectZoneAreaCode()
    {
        try
        {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("ZoneMaster");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                // btnSave.setText("UPDATE");//UpdateD
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                zoneCode = data[0].toString();
                txtZoneCode.setText(data[1].toString());
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    private void txtZoneCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtZoneCodeMouseClicked

        funSelectZoneAreaCode();
    }//GEN-LAST:event_txtZoneCodeMouseClicked

    private void txtHelperPayOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHelperPayOutMouseClicked
        // TODO add your handling code here:
        funHelperPayOutCharges();
    }//GEN-LAST:event_txtHelperPayOutMouseClicked

    private void txtHelperPayOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHelperPayOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHelperPayOutActionPerformed

    private void txtHelperPayOutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHelperPayOutKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }

    }//GEN-LAST:event_txtHelperPayOutKeyPressed

    private void txtDeliveryBoyPayOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDeliveryBoyPayOutMouseClicked
        // TODO add your handling code here:
        funDeliveryBoyPayOutCharges();
    }//GEN-LAST:event_txtDeliveryBoyPayOutMouseClicked

    private void txtDeliveryBoyPayOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeliveryBoyPayOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeliveryBoyPayOutActionPerformed

    private void txtDeliveryBoyPayOutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeliveryBoyPayOutKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtHelperPayOut.requestFocus();
        }
    }//GEN-LAST:event_txtDeliveryBoyPayOutKeyPressed

    private void txtBuildingCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuildingCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectBuilding();
        }
        if (evt.getKeyCode() == 10)
        {
            txtBuildingName.requestFocus();
        }
    }//GEN-LAST:event_txtBuildingCodeKeyPressed

    private void txtZoneCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtZoneCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '/' || evt.getKeyChar() == '?')
        {
            funSelectZoneAreaCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtDeliveryBoyPayOut.requestFocus();
        }

    }//GEN-LAST:event_txtZoneCodeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveButton();
            }
            else
            {
                funUpdateButton();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void cmboBoxCustomerTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmboBoxCustomerTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmboBoxCustomerTypeKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
       
        if(txtBuldingAddress.getText().length()==0)
        {
            new frmOkPopUp(null, "Please Enter Building Address", "Error", 0).setVisible(true);
            return ;
        }
        
        
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveButton();
        }
        else
        {
            funUpdateButton();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Customer Area Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Customer Area Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Customer Area Master");
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(frmCustAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmCustAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmCustAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmCustAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmCustAreaMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnResetAll;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmboBoxCustomerType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblAreaCode;
    private javax.swing.JLabel lblAreaName;
    private javax.swing.JLabel lblCustAreaType1;
    private javax.swing.JLabel lblCustType;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryBoyPayOut;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHelperPayOut;
    private javax.swing.JLabel lblHomeDeliCharge;
    private javax.swing.JLabel lblKM;
    private javax.swing.JLabel lblKM1;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelTabAreaMaster;
    private javax.swing.JPanel panelTabDeliveryCharges;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblDeliveryCharge;
    private javax.swing.JTextField txtBuildingCode;
    private javax.swing.JTextField txtBuildingName;
    private javax.swing.JTextField txtBuldingAddress;
    private javax.swing.JTextField txtDeliveryBoyPayOut;
    private javax.swing.JTextField txtDeliverycharges;
    private javax.swing.JTextField txtFromBillAmount;
    private javax.swing.JTextField txtHelperPayOut;
    private javax.swing.JTextField txtHomeDeliCharge;
    private javax.swing.JTextField txtToBillAmount;
    private javax.swing.JTextField txtZoneCode;
    // End of variables declaration//GEN-END:variables
    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {
        txtBuildingName.requestFocus();
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');

        txtBuildingCode.setText("");
        txtBuildingName.setText("");
        txtBuldingAddress.setText("");
        txtHomeDeliCharge.setText("");
        txtFromBillAmount.setText("");
        txtToBillAmount.setText("");
        txtDeliverycharges.setText("0.00");
        txtDeliveryBoyPayOut.setText("");
        txtHelperPayOut.setText("");
        txtZoneCode.setText("");
        zoneCode = "";
        dmDeliverycharges.setRowCount(0);
        btnRemove.setEnabled(false);
        txtBuildingCode.requestFocus();
    }

    /**
     * This method is used to set building name
     *
     * @param text
     */
    void setbulName(String text)
    {
        txtBuildingName.setText(text);
    }

    /**
     * This method is used to set area name
     *
     * @param text
     */
    void setAreaName(String text)
    {
        txtBuldingAddress.setText(text);
    }

    /**
     * This method is used to set building data
     *
     * @param data
     * @throws Exception
     */
    private void setBuildingData(Object[] data) throws Exception
    {
        sql = "select a.strBuildingCode,a.strBuildingName,a.strAddress"
                + " ,a.dblHomeDeliCharge,a.dblDeliveryBoyPayOut,a.dblHelperPayOut"
                + ",ifnull(c.strZoneName,''),ifnull(c.strZoneCode,'') "
                + " from tblbuildingmaster a left outer join tblareawisedc b "
                + " on a.strBuildingCode=b.strBuildingCode "
                + " left outer join tblzonemaster c on a.strZoneCode=c.strZoneCode "
                + " where a.strBuildingCode='" + clsGlobalVarClass.gSearchedItem + "'";
        System.out.println(sql);
        ResultSet rsBuildingInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsBuildingInfo.next())
        {
            txtBuildingCode.setText(rsBuildingInfo.getString(1));
            txtBuildingName.setText(rsBuildingInfo.getString(2));
            txtBuldingAddress.setText(rsBuildingInfo.getString(3));
            txtHomeDeliCharge.setText(rsBuildingInfo.getString(4));
            txtDeliveryBoyPayOut.setText(rsBuildingInfo.getString(5));
            txtHelperPayOut.setText(rsBuildingInfo.getString(6));
            txtZoneCode.setText(rsBuildingInfo.getString(7));
            zoneCode = rsBuildingInfo.getString(8);
            sql = "select b.dblBillAmount,b.dblBillAmount1,b.dblDeliveryCharges"
                    + ",c.strCustType \n"
                    + "from tblbuildingmaster a ,tblareawisedc b,tblcustomertypemaster c  "
                    + "where a.strBuildingCode='" + clsGlobalVarClass.gSearchedItem + "' "
                    + "and b.strBuildingCode='" + clsGlobalVarClass.gSearchedItem + "' "
                    + "and b.strCustTypeCode=c.strCustTypeCode";
            System.out.println(sql);
            ResultSet rsDeliveryCharge = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmDeliverycharges.setRowCount(0);

            while (rsDeliveryCharge.next())
            {
                Object row[] =
                {
                    rsDeliveryCharge.getString(1), rsDeliveryCharge.getString(2), rsDeliveryCharge.getString(3), rsDeliveryCharge.getString(4), false
                };
                dmDeliverycharges.addRow(row);
            }

            rsDeliveryCharge.close();
            tblDeliveryCharge.setModel(dmDeliverycharges);
            tblDeliveryCharge.setRowHeight(30);
        }
        rsBuildingInfo.close();
        txtBuildingCode.requestFocus();
    }

    /**
     * This method is used to check selection
     */
    private void funCheckSelection()
    {
        int rowNo = tblDeliveryCharge.getSelectedRow();
        String rowValue = tblDeliveryCharge.getValueAt(rowNo, 4).toString();
        if (Boolean.parseBoolean(rowValue))
        {
            btnRemove.setEnabled(true);
        }

        boolean flgSelect = false;
        for (int i = 0; i < tblDeliveryCharge.getRowCount(); i++)
        {
            String rowValue1 = tblDeliveryCharge.getValueAt(i, 4).toString();
            if (Boolean.parseBoolean(rowValue1))
            {
                flgSelect = true;
                break;
            }
        }
        if (!flgSelect)
        {
            btnRemove.setEnabled(false);
        }
    }

    /**
     * This method is used to remove row
     */
    private void funRemoveRow()
    {
        int rowNo = tblDeliveryCharge.getRowCount();
        java.util.Vector vIndexToDelete = new java.util.Vector();
        for (int i = 0; i < rowNo; i++)
        {
            boolean select = Boolean.parseBoolean(tblDeliveryCharge.getValueAt(i, 4).toString());
            if (select)
            {
                vIndexToDelete.add(i);
                //dmDeliverycharges.removeRow(i);
            }
        }
        int cnt = 0;
        while (cnt < tblDeliveryCharge.getRowCount())
        {
            boolean select = Boolean.parseBoolean(tblDeliveryCharge.getValueAt(cnt, 4).toString());
            if (select)
            {
                dmDeliverycharges.removeRow(cnt);
            }
            else
            {
                cnt++;
            }
        }
    }

}
