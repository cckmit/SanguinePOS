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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmAdvanceOrderTypeMaster extends javax.swing.JFrame
{
    clsUtility objUtility = new clsUtility();
    
    public frmAdvanceOrderTypeMaster()
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
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();
            txtAdvOrderTypeNo.requestFocus();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to search advance order time
     *
     * @return
     */
    private void funCallAdvOrderTypeSearch()
    {
        try
        {
            clsUtility obj = new clsUtility();
            clsGlobalVarClass.gSearchItemClicked=false;
            if(clsGlobalVarClass.gArrListSearchData!=null)
            {
                clsGlobalVarClass.gArrListSearchData.clear();
            }
            obj.funCallForSearchForm("AdvOrderTypeMaster");            
            new frmSearchFormDialog(this, true).setVisible(true);            
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");//UpdateD
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                txtAdvOrderTypeNo.setText(data[0].toString());
                txtAdvOrderTypeName.setText(data[1].toString());
                cmbOperational.setSelectedItem(data[2].toString());
                clsGlobalVarClass.gSearchItemClicked = false;
                txtAdvOrderTypeName.requestFocus();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save master
     *
     * @throws Exception
     * @return
     */
    private void funSaveMaster() throws Exception
    {
        try
        {
            if (txtAdvOrderTypeName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Advance Order Type Name", "Error", 0).setVisible(true);
                txtAdvOrderTypeName.requestFocus();
                return;
            }
            long lastNo = funGenerateAdvOrderTypeCode();
            String advOrderTypeCode = "AT" + String.format("%03d", lastNo);
            String insertSql = "insert into tbladvanceordertypemaster (strAdvOrderTypeCode,strAdvOrderTypeName"
                    + ",strOperational,strPOSCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
                    + ",strClientCode,strDataPostFlag)"
                    + " values('" + advOrderTypeCode + "','" + txtAdvOrderTypeName.getText().trim() + "','" + cmbOperational.getSelectedItem().toString() + "'"
                    + ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','N')";
            clsGlobalVarClass.dbMysql.execute(insertSql);

            String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + " where strTableName='AdvanceOrderType'";
            clsGlobalVarClass.dbMysql.execute(sql);

            JOptionPane.showMessageDialog(this, "Data Saved Successfully.");
            funResetField();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update master
     *
     * @throws Exception
     * @return
     */
    private void funUpdateMaster() throws Exception
    {
        try
        {
            if (txtAdvOrderTypeName.getText().trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Enter Advance Order Type Name", "Error", 0).setVisible(true);
                txtAdvOrderTypeName.requestFocus();
                return;
            }
            String advOrderTypeCode = txtAdvOrderTypeNo.getText().trim();
            String updateSql = "update tbladvanceordertypemaster set strAdvOrderTypeName='" + txtAdvOrderTypeName.getText().trim() + "'"
                    + ",strOperational='" + cmbOperational.getSelectedItem().toString() + "',strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
                    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",strClientCode='" + clsGlobalVarClass.gClientCode + "',strDataPostFlag='N' "
                    + "where strAdvOrderTypeCode='" + advOrderTypeCode + "'";
            clsGlobalVarClass.dbMysql.execute(updateSql);

            String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + " where strTableName='AdvanceOrderType' ";
            clsGlobalVarClass.dbMysql.execute(sql);

            JOptionPane.showMessageDialog(this, "Data Updated Successfully.");
            funResetField();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate advance order type code
     *
     * @throws Exception
     * @return long lastNo
     */
    private long funGenerateAdvOrderTypeCode() throws Exception
    {
        long lastNo = 0;

        String sqlInternal = "select dblLastNo from tblinternal "
                + "where strTransactionType='AdvOrderType'";
        ResultSet rsInternal = clsGlobalVarClass.dbMysql.executeResultSet(sqlInternal);
        if (rsInternal.next())
        {
            lastNo = rsInternal.getLong(1);
            lastNo++;
        }
        rsInternal.close();
        String sqlInternalUpdate = "update tblinternal set dblLastNo=" + lastNo + " "
                + "where strTransactionType='AdvOrderType'";
        clsGlobalVarClass.dbMysql.execute(sqlInternalUpdate);

        return lastNo;
    }

    /**
     * This method is used to reset all fields
     *
     * @return
     */
    private void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtAdvOrderTypeNo.setText("");
            txtAdvOrderTypeName.setText("");
            txtAdvOrderTypeNo.requestFocus();
            cmbOperational.setSelectedItem("Yes");
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

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
        panelLayout = new JPanel() {       public void paintComponent(Graphics g) {         Image img = Toolkit.getDefaultToolkit().getImage(         getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));         g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);         }       };  ;
        panelBody = new javax.swing.JPanel();
        panelContent = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblAdvOrderTypeNo = new javax.swing.JLabel();
        txtAdvOrderTypeNo = new javax.swing.JTextField();
        txtAdvOrderTypeName = new javax.swing.JTextField();
        lblAdvOrderTypeName = new javax.swing.JLabel();
        lblOperational = new javax.swing.JLabel();
        cmbOperational = new javax.swing.JComboBox();
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
        lblformName.setText("-Advance Order Master");
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
        lblUserCode.setName(""); // NOI18N
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

        panelContent.setBackground(new java.awt.Color(255, 255, 255));
        panelContent.setOpaque(false);
        panelContent.setLayout(null);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Advance Order Master");
        panelContent.add(lblFormName);
        lblFormName.setBounds(250, 70, 310, 30);

        lblAdvOrderTypeNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAdvOrderTypeNo.setText("Adv Order Type No       :");
        panelContent.add(lblAdvOrderTypeNo);
        lblAdvOrderTypeNo.setBounds(240, 170, 150, 30);

        txtAdvOrderTypeNo.setEditable(false);
        txtAdvOrderTypeNo.setBackground(new java.awt.Color(204, 204, 204));
        txtAdvOrderTypeNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAdvOrderTypeNoMouseClicked(evt);
            }
        });
        txtAdvOrderTypeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdvOrderTypeNoActionPerformed(evt);
            }
        });
        txtAdvOrderTypeNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdvOrderTypeNoKeyPressed(evt);
            }
        });
        panelContent.add(txtAdvOrderTypeNo);
        txtAdvOrderTypeNo.setBounds(390, 170, 140, 30);

        txtAdvOrderTypeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdvOrderTypeNameFocusLost(evt);
            }
        });
        txtAdvOrderTypeName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAdvOrderTypeNameMouseClicked(evt);
            }
        });
        txtAdvOrderTypeName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdvOrderTypeNameKeyPressed(evt);
            }
        });
        panelContent.add(txtAdvOrderTypeName);
        txtAdvOrderTypeName.setBounds(390, 230, 210, 30);

        lblAdvOrderTypeName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAdvOrderTypeName.setText("Adv Order Type Name   :");
        panelContent.add(lblAdvOrderTypeName);
        lblAdvOrderTypeName.setBounds(240, 230, 150, 30);

        lblOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperational.setText("Operational                  :");
        panelContent.add(lblOperational);
        lblOperational.setBounds(240, 290, 150, 30);

        cmbOperational.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        cmbOperational.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbOperationalKeyPressed(evt);
            }
        });
        panelContent.add(cmbOperational);
        cmbOperational.setBounds(390, 290, 210, 30);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Advance Order  Type Master");
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
        panelContent.add(btnNew);
        btnNew.setBounds(450, 490, 90, 40);

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
        panelContent.add(btnReset);
        btnReset.setBounds(570, 490, 90, 40);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Advance Order Type Master");
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
        panelContent.add(btnCancel);
        btnCancel.setBounds(690, 490, 90, 40);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelContent, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(panelContent, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAdvOrderTypeNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNoMouseClicked
        // TODO add your handling code here:
        funCallAdvOrderTypeSearch();
    }//GEN-LAST:event_txtAdvOrderTypeNoMouseClicked

    private void txtAdvOrderTypeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdvOrderTypeNameFocusLost

    private void txtAdvOrderTypeNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNameMouseClicked
        // TODO add your handling code here:
        if (txtAdvOrderTypeName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Advance Order Type Name").setVisible(true);
            txtAdvOrderTypeName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtAdvOrderTypeName.getText(), "1", "Enter Advance Order Type Name").setVisible(true);
            txtAdvOrderTypeName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtAdvOrderTypeNameMouseClicked

    private void txtAdvOrderTypeNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbOperational.requestFocus();
        }
    }//GEN-LAST:event_txtAdvOrderTypeNameKeyPressed

    private void cmbOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalKeyPressed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveMaster();
            }
            else
            {
                funUpdateMaster();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Advance Order Type Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtAdvOrderTypeNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNoKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funCallAdvOrderTypeSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtAdvOrderTypeName.requestFocus();
        }
    }//GEN-LAST:event_txtAdvOrderTypeNoKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            try
            {
                if (btnNew.getText().equalsIgnoreCase("SAVE"))
                {
                    funSaveMaster();
                }
                else
                {
                    funUpdateMaster();
                }
            }
            catch (Exception e)
            {
                objUtility.funWriteErrorLog(e);
                e.printStackTrace();

            }


    }//GEN-LAST:event_btnNewKeyPressed
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveMaster();
            }
            else
            {
                funUpdateMaster();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Advance Order Type Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtAdvOrderTypeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdvOrderTypeNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdvOrderTypeNoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Advance Order Type Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Advance Order Type Master");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbOperational;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAdvOrderTypeName;
    private javax.swing.JLabel lblAdvOrderTypeNo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperational;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtAdvOrderTypeName;
    private javax.swing.JTextField txtAdvOrderTypeNo;
    // End of variables declaration//GEN-END:variables

}
