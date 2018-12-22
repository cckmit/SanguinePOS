/* To change this license header, choose License Headers in Project Properties.
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
public class frmZoneMaster extends javax.swing.JFrame
{

    private String sql;
    clsUtility objUtility = new clsUtility();

    public frmZoneMaster()
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
            txtZoneCode.requestFocus();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();
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
        btnSaveUpdate.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {
        try
        {
            btnSaveUpdate.setText("SAVE");
            btnSaveUpdate.setMnemonic('s');
            txtZoneCode.setText("");
            txtZoneName.setText("");
            txtZoneCode.requestFocus();
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
            sql = "select * from tblzonemaster where strZoneCode='" + data[0].toString() + "'";
            ResultSet rsCustTypeData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCustTypeData.next())
            {
                txtZoneCode.setText(rsCustTypeData.getString(1));
                txtZoneName.setText(rsCustTypeData.getString(2));
                //txtDiscPer.setText(rsCustTypeData.getString(3));
            }
            rsCustTypeData.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate customer type code
     *
     * @return long customer type code
     */
    private long funGenZoneCode()
    {
        long lastNo = 1;
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='Zone'";
            ResultSet rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsDelBoyCatCode.next();
            int cntDelBoyCategory = rsDelBoyCatCode.getInt(1);
            rsDelBoyCatCode.close();
            if (cntDelBoyCategory > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='Zone'";
                rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsDelBoyCatCode.next();
                long code = rsDelBoyCatCode.getLong(1);
                code = code + 1;
                lastNo = code;
                rsDelBoyCatCode.close();
            }
            else
            {
                lastNo = 1;
            }
            String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
                    + "where strTransactionType='Zone'";
            clsGlobalVarClass.dbMysql.execute(updateSql);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return lastNo;
    }

    /**
     * This method is used to save customer type
     */
    private void funSaveZoneMaster()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtZoneName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Zone Name", "Error", 0).setVisible(true);
                txtZoneName.requestFocus();
            }
            else
            {
                long lastNo = funGenZoneCode();
                String delBoyCategoryCode = "CA" + String.format("%03d", lastNo);
                txtZoneCode.setText(delBoyCategoryCode);

                if (funCheckDuplicateZone())
                {
                    new frmOkPopUp(this, "Zone is already present", "Error", 3).setVisible(true);
                    return;
                }

                sql = "INSERT INTO tblzonemaster (strZoneCode,strZoneName,strUserCreated,strUserEdited"
                        + ",dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag) "
                        + "VALUES ('" + txtZoneCode.getText() + "', '" + txtZoneName.getText() + "', '" + clsGlobalVarClass.gUserCode + "'"
                        + ", '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ", '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "','N')";

                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
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
     * This method is used to update customer type
     */
    private void funUpdateZoneMaster()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtZoneName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Zone Name", "Error", 0).setVisible(true);
                txtZoneName.requestFocus();
            }
            else
            {
                sql = "UPDATE tblzonemaster "
                        + "SET strZoneName='" + txtZoneName.getText() + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N' WHERE strZoneCode ='" + txtZoneCode.getText() + "'";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
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
     * This method is used to check duplicate customer type
     *
     * @return boolean
     */
    private boolean funCheckDuplicateZone()
    {
        boolean flgZone = false;
        try
        {
            sql = "select count(strZoneCode) from tblzonemaster where strZoneName='" + txtZoneName.getText() + "'";
            //System.out.println(sql);
            ResultSet rsDupZone = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsDupZone.next())
            {
                if (rsDupZone.getInt(1) > 0)
                {
                    flgZone = true;
                }
            }
            rsDupZone.close();
        }
        catch (Exception e)
        {
            flgZone = true;
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return flgZone;
        }
    }

    /**
     * This method is used to select menu head code
     */
    private void funLoadZoneCode()
    {
        try
        {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("ZoneMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnSaveUpdate.setText("UPDATE");
                btnSaveUpdate.setMnemonic('u');
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
        lblCustAreaTypeCode = new javax.swing.JLabel();
        lblCustAreaTypeName = new javax.swing.JLabel();
        txtZoneName = new javax.swing.JTextField();
        txtZoneCode = new javax.swing.JTextField();
        btnSaveUpdate = new javax.swing.JButton();
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
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" - Zone Master");
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

        lblCustAreaTypeCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAreaTypeCode.setText("Zone Code          :");
        panelBodt.add(lblCustAreaTypeCode);
        lblCustAreaTypeCode.setBounds(260, 220, 140, 30);

        lblCustAreaTypeName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAreaTypeName.setText("Zone Name         :");
        panelBodt.add(lblCustAreaTypeName);
        lblCustAreaTypeName.setBounds(260, 280, 140, 30);

        txtZoneName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtZoneNameMouseClicked(evt);
            }
        });
        txtZoneName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtZoneNameKeyPressed(evt);
            }
        });
        panelBodt.add(txtZoneName);
        txtZoneName.setBounds(410, 280, 230, 30);

        txtZoneCode.setEditable(false);
        txtZoneCode.setBackground(new java.awt.Color(204, 204, 204));
        txtZoneCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtZoneCodeMouseClicked(evt);
            }
        });
        txtZoneCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtZoneCodeKeyPressed(evt);
            }
        });
        panelBodt.add(txtZoneCode);
        txtZoneCode.setBounds(410, 220, 100, 30);

        btnSaveUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSaveUpdate.setText("SAVE");
        btnSaveUpdate.setToolTipText("Save Customer Type Master");
        btnSaveUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSaveUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveUpdateMouseClicked(evt);
            }
        });
        btnSaveUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveUpdateActionPerformed(evt);
            }
        });
        btnSaveUpdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSaveUpdateKeyPressed(evt);
            }
        });
        panelBodt.add(btnSaveUpdate);
        btnSaveUpdate.setBounds(440, 510, 90, 40);

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
        btnReset.setBounds(550, 510, 90, 40);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(24, 19, 19));
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Zone Master");
        panelBodt.add(lblFormName);
        lblFormName.setBounds(250, 50, 330, 30);

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
        btnCancel.setBounds(660, 510, 90, 40);

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

    private void txtZoneNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtZoneNameMouseClicked
        // TODO add your handling code here:
        if (!clsGlobalVarClass.gClientCode.equals("009.001"))
        {
            if (txtZoneName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Type").setVisible(true);
                txtZoneName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtZoneName.getText(), "1", "Enter Customer Type").setVisible(true);
                txtZoneName.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtZoneNameMouseClicked

    private void txtZoneNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtZoneNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (funCheckDuplicateZone())
            {
                new frmOkPopUp(this, "Zone is already present", "Error", 3).setVisible(true);
                return;
            }
            btnSaveUpdate.requestFocus();
        }
    }//GEN-LAST:event_txtZoneNameKeyPressed

    private void txtZoneCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtZoneCodeMouseClicked
        // TODO add your handling code here:
        funLoadZoneCode();
    }//GEN-LAST:event_txtZoneCodeMouseClicked

    private void btnSaveUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveUpdateMouseClicked
        // TODO add your handling code here:
        if (btnSaveUpdate.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveZoneMaster(); //Save new Menu Head
        }
        else
        {
            funUpdateZoneMaster();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnSaveUpdateMouseClicked

    private void btnSaveUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveUpdateActionPerformed
        // TODO add your handling code here:
        if (btnSaveUpdate.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveZoneMaster(); //Save new Menu Head
        }
        else
        {
            funUpdateZoneMaster();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnSaveUpdateActionPerformed

    private void btnSaveUpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveUpdateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnSaveUpdate.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveZoneMaster(); //Save new Menu Head
            }
            else
            {
                funUpdateZoneMaster();  //Update Existing Menu Head
            }
        }
    }//GEN-LAST:event_btnSaveUpdateKeyPressed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Zone Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtZoneCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtZoneCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funLoadZoneCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtZoneName.requestFocus();
        }
    }//GEN-LAST:event_txtZoneCodeKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Zone Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Zone Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Zone Master");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSaveUpdate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCustAreaTypeCode;
    private javax.swing.JLabel lblCustAreaTypeName;
    private javax.swing.JLabel lblDate;
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
    private javax.swing.JTextField txtZoneCode;
    private javax.swing.JTextField txtZoneName;
    // End of variables declaration//GEN-END:variables
}
