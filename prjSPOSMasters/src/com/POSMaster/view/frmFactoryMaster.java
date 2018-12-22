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
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;

public class frmFactoryMaster extends javax.swing.JFrame
{
    private String userCode;
    private StringBuilder sqlBuilder=null;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmGroupMaster
     */
    public frmFactoryMaster()
    {
        initComponents();
        funSetShortCutKeys();
        try
        {
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    java.util.Date date1 = new java.util.Date();
                    String newstr = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            userCode = clsGlobalVarClass.gUserCode;
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            
            sqlBuilder = new StringBuilder();

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
     * This method is used to set data
     *
     * @param data
     */
    private void funSetData(Object[] data)
    {
        try
        {
            sqlBuilder.setLength(0);
            sqlBuilder.append("select a.strFactoryCode,a.strFactoryName "
                    + "from tblfactorymaster a where strFactoryCode='" + clsGlobalVarClass.gSearchedItem + "' ");
            ResultSet rsFactory = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
            if (rsFactory.next())
            {
                txtFactoryCode.setText(rsFactory.getString(1));
                txtFactoryName.setText(rsFactory.getString(2));                
            }
            rsFactory.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSaveUpdate()
    {
        clsUtility obj = new clsUtility();
        String propertCode = clsGlobalVarClass.gClientCode.substring(4);        
        try
        {
            String factoryCode = txtFactoryCode.getText().trim();
            String factoryName = txtFactoryName.getText().trim();

            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                if (!clsGlobalVarClass.validateEmpty(factoryName))
                {
                    new frmOkPopUp(this, "Please Enter Factory Name", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }
                else if (clsGlobalVarClass.funCheckItemName("tblfactorymaster", "strFactoryName", "strFactoryCode", factoryName, "", "save", ""))
                {
                    new frmOkPopUp(this, "This Factory Name is Already Exist", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }
                else if (!obj.funCheckLength(factoryName, 30))
                {
                    new frmOkPopUp(this, "Factory Name length must be less than 30", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }
                else
                {
                    sqlBuilder.setLength(0);
                    sqlBuilder.append("select ifnull(max(a.strFactoryCode),0) strMaxFactoryCode "
                            + "from tblfactorymaster a");
                    ResultSet rsFactoryCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
                    if (rsFactoryCode.next())
                    {
                        String strMaxFactoryCode = rsFactoryCode.getString(1);
                        if (strMaxFactoryCode.equals("0"))
                        {
                            factoryCode = "F" + String.format("%06d", (Integer.parseInt(rsFactoryCode.getString(1)) + 1));
                        }
                        else
                        {
                            strMaxFactoryCode = strMaxFactoryCode.substring(1);
                            int intMaxFactoryCode = Integer.parseInt(strMaxFactoryCode);
                            intMaxFactoryCode++;
                            factoryCode = "F" + String.format("%06d", intMaxFactoryCode);
                        }
                    }
                    rsFactoryCode.close();
                    System.out.println("MaxFactoryCode=" + factoryCode);

                    sqlBuilder.setLength(0);
                    sqlBuilder.append("insert into tblfactorymaster (strFactoryCode,strFactoryName,strUserCreated,"
                            + "strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag)"
                            + "values('" +factoryCode+ "','" +factoryName+ "','" + userCode + "','" + userCode + "'"
                            + ",'"+clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",'" +clsGlobalVarClass.gClientCode+ "','N') ");
                    //System.out.println(insertQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
                    if (exc > 0)
                    {
                        sqlBuilder.setLength(0);
                        sqlBuilder.append("update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='Factory' ");
                        clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
                        new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                }
            }
            else
            {
                if (!clsGlobalVarClass.validateEmpty(factoryName))
                {
                    new frmOkPopUp(this, "Please Enter Factory Name", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }
                else if (clsGlobalVarClass.funCheckItemName("tblfactorymaster", "strFactoryName", "strFactoryCode", factoryName, factoryCode, "update", ""))
                {
                    new frmOkPopUp(this, "This Factory Name is Already Exist", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }
                else if (!obj.funCheckLength(factoryName, 30))
                {
                    new frmOkPopUp(this, "Factory Name length must be less than 30", "Error", 0).setVisible(true);
                    txtFactoryName.requestFocus();
                }              
                else
                {
                    sqlBuilder.setLength(0);
                    sqlBuilder.append("UPDATE tblfactorymaster "
                            + "SET strFactoryName = '" +factoryName+ "' "
                            + ",strUserEdited='"+userCode+"' "
                            + ",dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime() + "' "                            
                            + " WHERE strFactoryCode ='" +factoryCode+"' ");
                    //System.out.println(updateQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
                    if (exc > 0)
                    {
                        sqlBuilder.setLength(0);
                        sqlBuilder.append("update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='Factory' ");
                        clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
                        new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
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
                new frmOkPopUp(this, "Factory Name Is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {

        btnNew.setText("SAVE");        
        txtFactoryCode.setText("");
        txtFactoryName.setText("");
        txtFactoryCode.requestFocus();        
        btnNew.setMnemonic('s');
    }

    private void funSelectFactoryCode()
    {
        try
        {

            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("FactoryMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");//UpdateD
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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblFactoryCode = new javax.swing.JLabel();
        txtFactoryCode = new javax.swing.JTextField();
        lblFactoryName = new javax.swing.JLabel();
        txtFactoryName = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

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
        lblformName.setText("-Factory Master");
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

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Factory Master");

        lblFactoryCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFactoryCode.setText("Factory Code    :");

        txtFactoryCode.setEditable(false);
        txtFactoryCode.setBackground(new java.awt.Color(204, 204, 204));
        txtFactoryCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFactoryCodeMouseClicked(evt);
            }
        });
        txtFactoryCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtFactoryCodeKeyPressed(evt);
            }
        });

        lblFactoryName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFactoryName.setText("Factory Name    :");

        txtFactoryName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFactoryNameMouseClicked(evt);
            }
        });
        txtFactoryName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtFactoryNameKeyPressed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Group Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Group Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFactoryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtFactoryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFactoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFactoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFactoryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFactoryCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFactoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFactoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void txtFactoryCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFactoryCodeMouseClicked

        funSelectFactoryCode();
    }//GEN-LAST:event_txtFactoryCodeMouseClicked

    private void txtFactoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFactoryNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtFactoryName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Group Name").setVisible(true);
                txtFactoryName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtFactoryName.getText(), "1", "Enter Group Name").setVisible(true);
                txtFactoryName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtFactoryNameMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        funSaveUpdate();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        try
        {
            funResetField();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Factory Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtFactoryCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFactoryCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectFactoryCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtFactoryName.requestFocus();
        }
    }//GEN-LAST:event_txtFactoryCodeKeyPressed

    private void txtFactoryNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFactoryNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtFactoryNameKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveUpdate();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funSaveUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Factory Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Factory Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Factory Master");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFactoryCode;
    private javax.swing.JLabel lblFactoryName;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtFactoryCode;
    private javax.swing.JTextField txtFactoryName;
    // End of variables declaration//GEN-END:variables
}
