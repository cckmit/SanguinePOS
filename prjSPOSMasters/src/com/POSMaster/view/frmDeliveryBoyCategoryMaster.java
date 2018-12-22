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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author sss11
 */
public class frmDeliveryBoyCategoryMaster extends javax.swing.JFrame {

    private String sql;
    private String custTypeCodeStatus;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmCustomerTypeMaster
     */
    public frmDeliveryBoyCategoryMaster() {
        initComponents();
        try {
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys() {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField() {
        try {
            btnNew.setText("SAVE");
            txtDelBoyCatCode.setText("");
            txtDelBoyCatName.setText("");
            txtDelBoyCatCode.requestFocus();
               btnNew.setMnemonic('s');
//            txtDiscPer.setText("0.00");

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetData(Object[] data) {
        try {
            sql = "select * from tbldeliveryboycategorymaster where strDelBoyCategoryCode='" + data[0].toString() + "'";
            ResultSet rsCustTypeData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCustTypeData.next()) {
                txtDelBoyCatCode.setText(rsCustTypeData.getString(1));
                txtDelBoyCatName.setText(rsCustTypeData.getString(2));
                //txtDiscPer.setText(rsCustTypeData.getString(3));
            }
            rsCustTypeData.close();
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate customer type code
     *
     * @return long customer type code
     */
    private long funGenDelBoyCatCode() {
        long lastNo = 1;
        try {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='DelBoyCategory'";
            ResultSet rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsDelBoyCatCode.next();
            int cntDelBoyCategory = rsDelBoyCatCode.getInt(1);
            rsDelBoyCatCode.close();
            if (cntDelBoyCategory > 0) {
                sql = "select dblLastNo from tblinternal where strTransactionType='DelBoyCategory'";
                rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsDelBoyCatCode.next();
                long code = rsDelBoyCatCode.getLong(1);
                code = code + 1;
                lastNo = code;
                rsDelBoyCatCode.close();
            } else {
                lastNo = 1;
            }
            String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
                    + "where strTransactionType='DelBoyCategory'";
            clsGlobalVarClass.dbMysql.execute(updateSql);
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return lastNo;
    }

    /**
     * This method is used to save customer type
     */
    private void funSaveDeliveryBoyCategory() {
        try {
            if (!clsGlobalVarClass.validateEmpty(txtDelBoyCatName.getText())) {
                new frmOkPopUp(this, "Please Enter Delivery Boy Category Name", "Error", 0).setVisible(true);
                txtDelBoyCatName.requestFocus();
            } else {
                long lastNo = funGenDelBoyCatCode();
                String delBoyCategoryCode = "DC" + String.format("%03d", lastNo);
                txtDelBoyCatCode.setText(delBoyCategoryCode);

                if (funCheckDuplicateDelBoyCategory()) {
                    new frmOkPopUp(this, "Delivery boy category is already present", "Error", 3).setVisible(true);
                    return;
                }

                sql = "INSERT INTO tbldeliveryboycategorymaster (strDelBoyCategoryCode,strDelBoyCategoryName,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag) "
                        + "VALUES ('" + txtDelBoyCatCode.getText() + "', '" + txtDelBoyCatName.getText() + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "','N')";

                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0) {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='DelBoyCat' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                }
            }
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update customer type
     */
    private void funUpdateDeliveryBoyCategory() {
        try {
            if (!clsGlobalVarClass.validateEmpty(txtDelBoyCatName.getText())) {
                new frmOkPopUp(this, "Please Enter Delivery Boy Category Name", "Error", 0).setVisible(true);
                txtDelBoyCatName.requestFocus();
            } else {
                sql = "UPDATE tbldeliveryboycategorymaster "
                        + "SET strDelBoyCategoryName='" + txtDelBoyCatName.getText() + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N' WHERE strDelBoyCategoryCode ='" + txtDelBoyCatCode.getText() + "'";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0) {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='DelBoyCat' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                }
            }
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to check duplicate customer type
     *
     * @return boolean
     */
    private boolean funCheckDuplicateDelBoyCategory() {
        boolean flgDelBoyCategory = false;
        try {
            sql = "select count(strDelBoyCategoryCode) from tbldeliveryboycategorymaster where strDelBoyCategoryName='" + txtDelBoyCatName.getText() + "'";
            //System.out.println(sql);
            ResultSet rsDupCustType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsDupCustType.next()) {
                if (rsDupCustType.getInt(1) > 0) {
                    flgDelBoyCategory = true;
                }
            }
            rsDupCustType.close();
        } catch (Exception e) {
            flgDelBoyCategory = true;
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        } finally {
            return flgDelBoyCategory;
        }
    }

    /**
     * This method is used to select menu head code
     */
    private void funLoadDeliveryBoyCategoryCode() {
        try {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("DeliveryBoyCategoryMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked) {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
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
        panelLayout = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBodyRoot = new javax.swing.JPanel();
        panelBodt = new javax.swing.JPanel();
        lblDelBoyCatCode = new javax.swing.JLabel();
        lblDelBoyCatName = new javax.swing.JLabel();
        txtDelBoyCatName = new javax.swing.JTextField();
        txtDelBoyCatCode = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Delivery Boy Category");
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

        panelBodyRoot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBodyRoot.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBodyRoot.setOpaque(false);

        panelBodt.setBackground(new java.awt.Color(255, 255, 255));
        panelBodt.setOpaque(false);
        panelBodt.setPreferredSize(new java.awt.Dimension(610, 600));
        panelBodt.setLayout(null);

        lblDelBoyCatCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDelBoyCatCode.setText("Delivery Boy Category Code    :");
        panelBodt.add(lblDelBoyCatCode);
        lblDelBoyCatCode.setBounds(160, 160, 180, 30);

        lblDelBoyCatName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDelBoyCatName.setText("Delivery Boy Category Name    :");
        panelBodt.add(lblDelBoyCatName);
        lblDelBoyCatName.setBounds(160, 220, 180, 30);

        txtDelBoyCatName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDelBoyCatNameMouseClicked(evt);
            }
        });
        txtDelBoyCatName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDelBoyCatNameKeyPressed(evt);
            }
        });
        panelBodt.add(txtDelBoyCatName);
        txtDelBoyCatName.setBounds(340, 220, 230, 30);

        txtDelBoyCatCode.setEditable(false);
        txtDelBoyCatCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDelBoyCatCodeMouseClicked(evt);
            }
        });
        txtDelBoyCatCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDelBoyCatCodeKeyPressed(evt);
            }
        });
        panelBodt.add(txtDelBoyCatCode);
        txtDelBoyCatCode.setBounds(340, 160, 80, 30);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Customer Type Master");
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
        panelBodt.add(btnNew);
        btnNew.setBounds(420, 430, 90, 40);

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
        panelBodt.add(btnReset);
        btnReset.setBounds(530, 430, 90, 40);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(24, 19, 19));
        lblFormName.setText("Delivery Boy Category Master");
        panelBodt.add(lblFormName);
        lblFormName.setBounds(260, 50, 330, 30);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Customer Type Master");
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
        panelBodt.add(btnCancel);
        btnCancel.setBounds(640, 430, 90, 40);

        javax.swing.GroupLayout panelBodyRootLayout = new javax.swing.GroupLayout(panelBodyRoot);
        panelBodyRoot.setLayout(panelBodyRootLayout);
        panelBodyRootLayout.setHorizontalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBodt, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBodyRootLayout.setVerticalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBodt, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelLayout.add(panelBodyRoot, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDelBoyCatNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDelBoyCatNameMouseClicked
        // TODO add your handling code here:
        if (!clsGlobalVarClass.gClientCode.equals("009.001")) {
            if (txtDelBoyCatName.getText().length() == 0) {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Type").setVisible(true);
                txtDelBoyCatName.setText(clsGlobalVarClass.gKeyboardValue);
            } else {
                new frmAlfaNumericKeyBoard(this, true, txtDelBoyCatName.getText(), "1", "Enter Customer Type").setVisible(true);
                txtDelBoyCatName.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtDelBoyCatNameMouseClicked

    private void txtDelBoyCatNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDelBoyCatNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            if (funCheckDuplicateDelBoyCategory()) {
                new frmOkPopUp(this, "Customer Type is already present", "Error", 3).setVisible(true);
                return;
            }
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtDelBoyCatNameKeyPressed

    private void txtDelBoyCatCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDelBoyCatCodeMouseClicked
        // TODO add your handling code here:
        funLoadDeliveryBoyCategoryCode();
    }//GEN-LAST:event_txtDelBoyCatCodeMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) {
            funSaveDeliveryBoyCategory(); //Save new Menu Head
        } else {
            funUpdateDeliveryBoyCategory();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) {
            funSaveDeliveryBoyCategory(); //Save new Menu Head
        } else {
            funUpdateDeliveryBoyCategory();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            if (btnNew.getText().equalsIgnoreCase("SAVE")) {
                funSaveDeliveryBoyCategory(); //Save new Menu Head
            } else {
                funUpdateDeliveryBoyCategory();  //Update Existing Menu Head
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Delivery Boy Category Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtDelBoyCatCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDelBoyCatCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/') {
            funLoadDeliveryBoyCategoryCode();
        }
        if(evt.getKeyCode()==10)
        {
            txtDelBoyCatName.requestFocus();
        }
    }//GEN-LAST:event_txtDelBoyCatCodeKeyPressed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Delivery Boy Category Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("Delivery Boy Category Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("Delivery Boy Category Master");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelBoyCatCode;
    private javax.swing.JLabel lblDelBoyCatName;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBodt;
    private javax.swing.JPanel panelBodyRoot;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtDelBoyCatCode;
    private javax.swing.JTextField txtDelBoyCatName;
    // End of variables declaration//GEN-END:variables
}
