/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
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
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmCustomerTypeMaster extends javax.swing.JFrame
{
    clsUtility objUtility = new clsUtility();
    private String sql;

    public frmCustomerTypeMaster()
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
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtCustTypeCode.requestFocus();
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
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtCustTypeCode.setText("");
            txtCustType.setText("");
            txtCustType.requestFocus();
            txtDiscPer.setText("0.00");
            txtCustTypeCode.requestFocus();
            cmbPlayZoneCustType.setSelectedIndex(0);
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
            sql = "select * from tblcustomertypemaster where strCustTypeCode='" + data[0].toString() + "'";
            ResultSet rsCustTypeData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCustTypeData.next())
            {
                txtCustTypeCode.setText(rsCustTypeData.getString(1));
                txtCustType.setText(rsCustTypeData.getString(2));
                txtDiscPer.setText(rsCustTypeData.getString(3));
                cmbPlayZoneCustType.setSelectedItem(rsCustTypeData.getString(10));
            }
            rsCustTypeData.close();
            txtCustType.requestFocus();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    
     /**
     * This method is used to get customer type
     *
     * @return string
     */
    private long funGenCustTypeCode()
    {
        
        long lastNo = 1;
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='custtype'";
            ResultSet rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCustTypeCode.next();
            int cntType = rsCustTypeCode.getInt(1);
            rsCustTypeCode.close();
            if (cntType > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='custtype'";
                rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCustTypeCode.next();
                long code = rsCustTypeCode.getLong(1);
                code = code + 1;
                lastNo = code;
               
                String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
                    + "where strTransactionType='custtype'";
                clsGlobalVarClass.dbMysql.execute(updateSql);
                 rsCustTypeCode.close();
            }
            else
            {
                lastNo = 1;
                sql = "insert into tblinternal values('custtype'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
          
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
    private void funSaveCustomerType()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtCustType.getText()))
            {
                new frmOkPopUp(this, "Please Enter Customer Type", "Error", 0).setVisible(true);
                txtCustType.requestFocus();
            }
            else
            {
                long lastNo = funGenCustTypeCode();
                String custTypeCode = "CT" + String.format("%03d", lastNo);
                txtCustTypeCode.setText(custTypeCode);

                if (funCheckDuplicateCustomerType())
                {
                    new frmOkPopUp(this, "Customer Type is already present", "Error", 3).setVisible(true);
                    return;
                }

                sql = "insert into tblcustomertypemaster "
                      + "(strCustTypeCode,strCustType,dblDiscPer,strUserCreated,strUserEdited,dteDateCreated"
                      + ",dteDateEdited,strClientCode,strDataPostFlag,strPlayZoneCustType) "
                      + "values('" + custTypeCode + "','" + txtCustType.getText() + "'," + txtDiscPer.getText().trim() + ",'"
                      + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                      + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                      + ",'" + clsGlobalVarClass.gClientCode + "','N','"+cmbPlayZoneCustType.getSelectedItem().toString()+"')";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='CustomerType' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
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
    private void funUpdateCustomerType()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtCustType.getText()))
            {
                new frmOkPopUp(this, "Please Enter Customer Type", "Error", 0).setVisible(true);
                txtCustType.requestFocus();
            }
            else
            {
                sql = "UPDATE tblcustomertypemaster "
                      + "SET strCustType='" + txtCustType.getText() + "',dblDiscPer=" + txtDiscPer.getText().trim() + ""
                      + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                      + ",strDataPostFlag='N',strPlayZoneCustType='"+cmbPlayZoneCustType.getSelectedItem().toString()+"'  "
                      + "WHERE strCustTypeCode ='" + txtCustTypeCode.getText() + "'";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='CustomerType' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
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
    private boolean funCheckDuplicateCustomerType()
    {
        boolean flgDupCustType = false;
        try
        {
            sql = "select count(strCustType) from tblcustomertypemaster where strCustType='" + txtCustType.getText() + "'";
            //System.out.println(sql);
            ResultSet rsDupCustType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsDupCustType.next())
            {
                if (rsDupCustType.getInt(1) > 0)
                {
                    flgDupCustType = true;
                }
            }
            rsDupCustType.close();
        }
        catch (Exception e)
        {
            flgDupCustType = true;
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return flgDupCustType;
        }
    }

    /**
     * This method is used to select menu head code
     */
    private void funSelectCustTypeCode()
    {
        try
        {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("CustTypeMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');

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
        lblCustTypeCode = new javax.swing.JLabel();
        lblCustType = new javax.swing.JLabel();
        txtCustType = new javax.swing.JTextField();
        txtCustTypeCode = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        lblCustType1 = new javax.swing.JLabel();
        txtDiscPer = new javax.swing.JTextField();
        cmbPlayZoneCustType = new javax.swing.JComboBox();
        lblPlayZoneCustType = new javax.swing.JLabel();

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
        lblformName.setText("- Customer Type Master");
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

        lblCustTypeCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustTypeCode.setText("Customer type Code   :");
        panelBodt.add(lblCustTypeCode);
        lblCustTypeCode.setBounds(250, 190, 140, 30);

        lblCustType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustType.setText("Customer Type          :");
        panelBodt.add(lblCustType);
        lblCustType.setBounds(250, 250, 140, 30);

        txtCustType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustTypeMouseClicked(evt);
            }
        });
        txtCustType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustTypeKeyPressed(evt);
            }
        });
        panelBodt.add(txtCustType);
        txtCustType.setBounds(440, 250, 230, 30);

        txtCustTypeCode.setEditable(false);
        txtCustTypeCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustTypeCodeMouseClicked(evt);
            }
        });
        txtCustTypeCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustTypeCodeKeyPressed(evt);
            }
        });
        panelBodt.add(txtCustTypeCode);
        txtCustTypeCode.setBounds(440, 190, 120, 30);

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
        btnNew.setBounds(440, 500, 90, 40);

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
        btnReset.setBounds(550, 500, 90, 40);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(24, 19, 19));
        lblFormName.setText("Customer Type Master");
        panelBodt.add(lblFormName);
        lblFormName.setBounds(260, 50, 250, 30);

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
        btnCancel.setBounds(660, 500, 90, 40);

        lblCustType1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustType1.setText("Discount %               :");
        panelBodt.add(lblCustType1);
        lblCustType1.setBounds(250, 310, 140, 30);

        txtDiscPer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscPer.setText("0.00");
        txtDiscPer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDiscPerMouseClicked(evt);
            }
        });
        txtDiscPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscPerKeyPressed(evt);
            }
        });
        panelBodt.add(txtDiscPer);
        txtDiscPer.setBounds(440, 310, 120, 30);

        cmbPlayZoneCustType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPlayZoneCustType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Member", "Guest" }));
        panelBodt.add(cmbPlayZoneCustType);
        cmbPlayZoneCustType.setBounds(440, 370, 240, 40);

        lblPlayZoneCustType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPlayZoneCustType.setText("Play Zone Customer Type    :");
        panelBodt.add(lblPlayZoneCustType);
        lblPlayZoneCustType.setBounds(250, 370, 190, 40);

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

    private void txtCustTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustTypeMouseClicked
        // TODO add your handling code here:
        if (!clsGlobalVarClass.gClientCode.equals("009.001"))
        {
            if (txtCustType.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Type").setVisible(true);
                txtCustType.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtCustType.getText(), "1", "Enter Customer Type").setVisible(true);
                txtCustType.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtCustTypeMouseClicked

    private void txtCustTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (funCheckDuplicateCustomerType())
            {
                new frmOkPopUp(this, "Customer Type is already present", "Error", 3).setVisible(true);
                return;
            }
            txtDiscPer.requestFocus();
        }
    }//GEN-LAST:event_txtCustTypeKeyPressed

    private void txtCustTypeCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustTypeCodeMouseClicked
        // TODO add your handling code here:
        funSelectCustTypeCode();
    }//GEN-LAST:event_txtCustTypeCodeMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveCustomerType(); //Save new Menu Head
        }
        else
        {
            funUpdateCustomerType();  //Update Existing Menu Head
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
//        // TODO add your handling code here:
//        if (btnNew.getText().equalsIgnoreCase("SAVE"))
//        {
//            funSaveCustomerType(); //Save 
//        }
//        else
//        {
//            funUpdateCustomerType();  //Update 
//        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveCustomerType(); //Save 
            }
            else
            {
                funUpdateCustomerType();  //Update 
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
        clsGlobalVarClass.hmActiveForms.remove("CustomerTypeMaster");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtDiscPerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscPerMouseClicked
        try
        {
            if (txtDiscPer.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, "","Double", "Please Enter Discount.").setVisible(true);
                txtDiscPer.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this, true, txtDiscPer.getText(), "Double", "Please Enter Discount.").setVisible(true);
                txtDiscPer.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtDiscPerMouseClicked

    private void txtCustTypeCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustTypeCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectCustTypeCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtCustType.requestFocus();
        }
    }//GEN-LAST:event_txtCustTypeCodeKeyPressed

    private void txtDiscPerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscPerKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtDiscPerKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("CustomerTypeMaster");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("CustomerTypeMaster");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("CustomerTypeMaster");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbPlayZoneCustType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCustType;
    private javax.swing.JLabel lblCustType1;
    private javax.swing.JLabel lblCustTypeCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPlayZoneCustType;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBodt;
    private javax.swing.JPanel panelBodyRoot;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtCustType;
    private javax.swing.JTextField txtCustTypeCode;
    private javax.swing.JTextField txtDiscPer;
    // End of variables declaration//GEN-END:variables
}
