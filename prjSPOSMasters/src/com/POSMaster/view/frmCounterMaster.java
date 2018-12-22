/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmCounterMaster extends javax.swing.JFrame {

    DefaultTableModel dmMenuHead;
    private HashMap<String, String> mapPOSCode;
    private HashMap<String, String> mapPOSName;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmCounterMaster
     */
    public frmCounterMaster() {
        initComponents();
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        txtCounterCode.requestFocus();

        dmMenuHead = (DefaultTableModel) tblMenuHead.getModel();
        //funInitMenuHeadTable();
        funLoadMenuHead();
        funFillUserCodeCombo();
        funLoadPOSCombo();
        funSetShortCutKeys();
    }

    private void funSetShortCutKeys() {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    /**
     * This method is used to load menu heads
     */
    private void funLoadMenuHead() {
        try {
            String sql = "select strMenuCode,strMenuName from tblmenuhd";
            ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsMenuHead.next()) {
                Object row[] = {rsMenuHead.getString(1), rsMenuHead.getString(2), false};
                dmMenuHead.addRow(row);
            }
            tblMenuHead.setModel(dmMenuHead);
            rsMenuHead.close();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    
    private void funFillUserCodeCombo()
    {
        try
        {
            cmbUserCode.addItem("");
            String sql="select strUserCode from tbluserhd ";
            ResultSet rsUserCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsUserCode.next())
            {
                cmbUserCode.addItem(rsUserCode.getString(1));
            }
            rsUserCode.close();
            
        }catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * This method is used to reset all fields
     */
    private void funResetAllField() {
        btnNew.setText("SAVE");
        txtCounterCode.setText("");
        txtCounterName.setText("");
        btnNew.setMnemonic('s');
        txtCounterName.requestFocus();
        cmbPosCode.setSelectedIndex(0);
        
        //dmMenuHead.setRowCount(0);
        int rowCount = dmMenuHead.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            tblMenuHead.setValueAt(false, row, 2);
        }
    }

    /**
     * This method is used to reset menu head
     */
    private void funResetMenuHeadField() {
        dmMenuHead.setRowCount(0);
    }

    /**
     * This method is used to check selection
     */
    private void funCheckSelection() {
        for (int i = 0; i < tblMenuHead.getRowCount(); i++) {
            if (Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString()) == true) {
                break;
            }
        }
    }

    /**
     * This method is used to remove row
     */
    private void funRemoveRow() {
        for (int i = 0; i < tblMenuHead.getRowCount(); i++) {
            boolean select = Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString());
            if (select) {
                dmMenuHead.removeRow(i);
            }
        }
    }

    /**
     * This method is used to get counter code
     *
     * @return long counter code
     */
    private String funGetCounterCode() {
        
        String counterCode="";

        try {
            String sql = "select count(dblLastNo) from tblinternal where strTransactionType='Counter'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int areaCodeCnt = rs.getInt(1);
            rs.close();
            if (areaCodeCnt > 0) {
                sql = "select dblLastNo from tblinternal where strTransactionType='Counter'";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                long code = rs.getLong(1);
                code = code + 1;
                counterCode = "CT" + String.format("%02d", code);
                
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='Counter'";
                clsGlobalVarClass.dbMysql.execute(sql);
            } else {
                counterCode = "CT01";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('Counter'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return counterCode;
    }

    /**
     * This method is used to save counter
     */
    private void funSaveCounter() 
    {
        String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
        if (txtCounterName.getText().trim().length() == 0) {
            new frmOkPopUp(this, "Please Enter Counter Name", "Error", 0).setVisible(true);
            txtCounterName.requestFocus();
            return;
        }
        if (tblMenuHead.getRowCount() == 0) {
            new frmOkPopUp(this, "Please Enter Atleast one Menu Head", "Error", 0).setVisible(true);
            txtCounterCode.requestFocus();
            return;
        }
        int rowCount = tblMenuHead.getRowCount();
        boolean validFlag = false;
        for (int i = 0; i < rowCount; i++) {
            if (tblMenuHead.getValueAt(i, 2).equals(true)) {
                validFlag = true;
                break;
            }
        }
        if (!validFlag) {
            new frmOkPopUp(this, "Please select atleast one menu head", "Error", 0).setVisible(true);
            return;
        }
        try {
            String counterCode = funGetCounterCode();
            boolean select = false;
            
            String insertSql = "insert into tblcounterhd (strCounterCode,strCounterName,strPOSCode,strUserCreated"
                    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strOperational,strUserCode)"
                    + " values('" + counterCode + "','" + txtCounterName.getText().trim() + "','" +posCode+ "','" + clsGlobalVarClass.gUserCode + "',"
                    + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','" + cmbOperational.getSelectedItem() + "'"
                    + ",'"+cmbUserCode.getSelectedItem().toString().trim()+"')";
            clsGlobalVarClass.dbMysql.execute(insertSql);
            insertSql = "insert into tblcounterdtl (strCounterCode,strMenuCode,strClientCode) values";
            for (int i = 0; i < tblMenuHead.getRowCount(); i++) {
                select = Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString());
                if (select) {
                    insertSql += "('" + counterCode + "','" + tblMenuHead.getValueAt(i, 0).toString() + "'"
                            + ",'" + clsGlobalVarClass.gClientCode + "'),";
                }
            }

            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            //System.out.println(insertSql);
            clsGlobalVarClass.dbMysql.execute(insertSql); 
            
            String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                + " where strTableName='Counter' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
            funResetAllField();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update counter
     */
    private void funUpadteCounter() 
    {
        String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
        if (txtCounterName.getText().trim().length() == 0) {
            new frmOkPopUp(this, "Please Enter Counter Name", "Error", 0).setVisible(true);
            txtCounterName.requestFocus();
            return;
        }
        if (tblMenuHead.getRowCount() == 0) {
            new frmOkPopUp(this, "Please Enter Atleast one Menu Head", "Error", 0).setVisible(true);
            return;
        }

        try {
            boolean select = false;
            String sqlupdateHd = "update tblcounterhd set strCounterName='" + txtCounterName.getText().trim() + "'  ,"
                + " strUserEdited='" + clsGlobalVarClass.gUserCode + "' ,dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',"
                + " strOperational='" + cmbOperational.getSelectedItem() + "',struserCode='"+cmbUserCode.getSelectedItem().toString().trim()+"',"
                + " strPOSCode='"+posCode+"' "
                + " where strCounterCode='" + txtCounterCode.getText().trim() + "';";
            clsGlobalVarClass.dbMysql.execute(sqlupdateHd);

            String sqlDeleteDtl = "delete from tblcounterdtl where strCounterCode='" + txtCounterCode.getText().trim() + "'";
            clsGlobalVarClass.dbMysql.execute(sqlDeleteDtl);
            String insertSql = "";
            insertSql = "insert into tblcounterdtl (strCounterCode,strMenuCode,strClientCode) values";
            for (int i = 0; i < tblMenuHead.getRowCount(); i++) {
                select = Boolean.parseBoolean(tblMenuHead.getValueAt(i, 2).toString());
                if (select) {
                    insertSql += "('" + txtCounterCode.getText().trim() + "','" + tblMenuHead.getValueAt(i, 0).toString() + "'"
                            + ",'" + clsGlobalVarClass.gClientCode + "'),";
                }
            }

            StringBuilder sb = new StringBuilder(insertSql);
            int index = sb.lastIndexOf(",");
            insertSql = sb.delete(index, sb.length()).toString();
            //System.out.println(insertSql);
            clsGlobalVarClass.dbMysql.execute(insertSql);
            
            String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                + " where strTableName='Counter' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            new frmOkPopUp(this, "Entry updated Successfully", "Successfull", 3).setVisible(true);
            funResetAllField();
        } catch (Exception e) {
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
        panelLayout = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblCounterCode = new javax.swing.JLabel();
        txtCounterCode = new javax.swing.JTextField();
        lblCounterName = new javax.swing.JLabel();
        txtCounterName = new javax.swing.JTextField();
        lblOperation = new javax.swing.JLabel();
        cmbOperational = new javax.swing.JComboBox();
        tblMenuItems = new javax.swing.JScrollPane();
        tblMenuHead = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbUserCode = new javax.swing.JComboBox();
        lblCounterUserCode = new javax.swing.JLabel();
        lblCounterUserCode1 = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        lblformName.setText("-Counter Master");
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
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(24, 19, 19));
        lblTitle.setText("Counter Master");

        lblCounterCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterCode.setText("Counter Code   :");

        txtCounterCode.setEditable(false);
        txtCounterCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCounterCodeMouseClicked(evt);
            }
        });
        txtCounterCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCounterCodeKeyPressed(evt);
            }
        });

        lblCounterName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterName.setText("Counter Name  :");

        txtCounterName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCounterNameMouseClicked(evt);
            }
        });
        txtCounterName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCounterNameKeyPressed(evt);
            }
        });

        lblOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperation.setText("Operational      :");

        cmbOperational.setBackground(new java.awt.Color(51, 102, 255));
        cmbOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbOperational.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        cmbOperational.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbOperationalKeyPressed(evt);
            }
        });

        tblMenuHead.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblMenuHead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Menu Code", "Menu Name", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMenuHead.setRowHeight(22);
        tblMenuHead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenuHeadMouseClicked(evt);
            }
        });
        tblMenuItems.setViewportView(tblMenuHead);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Counter Master");
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
        btnReset.setText("RESET");
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
        btnCancel.setToolTipText("Close Counter Master");
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

        cmbUserCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbUserCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbUserCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbUserCodeKeyPressed(evt);
            }
        });

        lblCounterUserCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterUserCode.setText("User                :");

        lblCounterUserCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterUserCode1.setText("POS                :");

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPosCodeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(299, 299, 299)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tblMenuItems, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblOperation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblCounterCode, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                    .addComponent(lblCounterUserCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCounterCode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(cmbPosCode, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbOperational, javax.swing.GroupLayout.Alignment.LEADING, 0, 145, Short.MAX_VALUE))
                                        .addGap(153, 153, 153)
                                        .addComponent(cmbUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(349, 349, 349)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCounterUserCode, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(lblCounterName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCounterName, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCounterCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCounterCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCounterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCounterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblOperation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbOperational, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCounterUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCounterUserCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(tblMenuItems, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

    private void txtCounterCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCounterCodeMouseClicked
        // TODO add your handling code here:
        funSelectCounterCode();
    }//GEN-LAST:event_txtCounterCodeMouseClicked

    private void txtCounterNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCounterNameMouseClicked
        // TODO add your handling code here:
        if (txtCounterName.getText().length() == 0) {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Name").setVisible(true);
            txtCounterName.setText(clsGlobalVarClass.gKeyboardValue);
        } else {
            new frmAlfaNumericKeyBoard(this, true, txtCounterName.getText(), "1", "Enter Customer Name").setVisible(true);
            txtCounterName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtCounterNameMouseClicked

    private void cmbOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalKeyPressed

    private void tblMenuHeadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuHeadMouseClicked
        // TODO add your handling code here:
        funCheckSelection();
    }//GEN-LAST:event_tblMenuHeadMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        if (btnNew.getText().equalsIgnoreCase("SAVE")) {
            funSaveCounter();
        } else {
            funUpadteCounter();
        }

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetAllField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("CounterMaster");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtCounterCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCounterCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/') {
            funSelectCounterCode();
        }
        if (evt.getKeyCode() == 10) {
            txtCounterName.requestFocus();
        }
    }//GEN-LAST:event_txtCounterCodeKeyPressed

    private void txtCounterNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCounterNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            cmbOperational.requestFocus();
        }
    }//GEN-LAST:event_txtCounterNameKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            if (btnNew.getText().equalsIgnoreCase("SAVE")) {
                funSaveCounter();
            } else {
                funUpadteCounter();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetAllField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("CounterMaster");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("CounterMaster");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("CounterMaster");
    }//GEN-LAST:event_formWindowClosing

    private void cmbUserCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbUserCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUserCodeKeyPressed

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPosCodeKeyPressed
    /**
     * This method is used to select counter code
     */
    private void funSelectCounterCode() {
        clsUtility obj=new clsUtility();
        obj.funCallForSearchForm("Counter");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked) {
            btnNew.setText("UPDATE");
            btnNew.setMnemonic('u');
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            setDataForCounterCode(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
        obj=null;
    }

    /**
     * This method is used to
     *
     * @param data
     */
    private void setDataForCounterCode(Object[] data) {
        txtCounterCode.setText(data[0].toString());
        txtCounterName.setText(data[1].toString());
        cmbOperational.setSelectedItem(data[2].toString());
        cmbUserCode.setSelectedItem(data[3].toString());
        cmbPosCode.setSelectedItem(mapPOSCode.get(data[4].toString()));
        funFillDTlTable(data[0].toString());
    }

    /**
     * This method is used to fill table data
     *
     * @param counterCode
     */
    private void funFillDTlTable(String counterCode) {
        try {
            boolean select = false;
            java.util.Vector vCounterMenu = new java.util.Vector();
            String sqlDtl = "select a.strMenuCode,b.strMenuName from tblcounterdtl a,tblmenuhd b "
                    + "where a.strCounterCode='" + counterCode + "' and "
                    + "a.strMenuCode=b.strMenuCode order by b.strMenuName;";
            ResultSet rsDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
            while (rsDtl.next()) {
                vCounterMenu.add(rsDtl.getString(1));
                //Object row[]={rsDtl.getString(1),rsDtl.getString(2),true};
                //dm.addRow(row);
            }
            rsDtl.close();
            DefaultTableModel dm = (DefaultTableModel) tblMenuHead.getModel();
            dm.setRowCount(0);
            String sqlMenuHead = "select strMenuCode,strMenuName from tblmenuhd order by strMenuName";
            ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sqlMenuHead);
            while (rsMenuHead.next()) {
                select = false;
                for (int i = 0; i < vCounterMenu.size(); i++) {
                    if (rsMenuHead.getString(1).equals(vCounterMenu.elementAt(i).toString())) {
                        select = true;
                        break;
                    }
                }
                Object row[] = {rsMenuHead.getString(1), rsMenuHead.getString(2), select};
                dm.addRow(row);
            }
            rsMenuHead.close();
            tblMenuHead.setModel(dm);
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private void funLoadPOSCombo()
    {
        
        try
        {
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
            mapPOSCode = new HashMap<String, String>();
            mapPOSName = new HashMap<String, String>();
            cmbPosCode.addItem("All");
            mapPOSCode.put("All", "All");
            mapPOSName.put("All", "All");
            while (rsPOS.next())
            {
                cmbPosCode.addItem(rsPOS.getString(2));
                mapPOSCode.put(rsPOS.getString(1), rsPOS.getString(2));
                mapPOSName.put(rsPOS.getString(2), rsPOS.getString(1));
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmCounterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCounterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCounterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCounterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmCounterMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbOperational;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbUserCode;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCounterCode;
    private javax.swing.JLabel lblCounterName;
    private javax.swing.JLabel lblCounterUserCode;
    private javax.swing.JLabel lblCounterUserCode1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperation;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblMenuHead;
    private javax.swing.JScrollPane tblMenuItems;
    private javax.swing.JTextField txtCounterCode;
    private javax.swing.JTextField txtCounterName;
    // End of variables declaration//GEN-END:variables
}
