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
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;

public class frmGroupMaster extends javax.swing.JFrame
{

    private ResultSet countSet, countSet1;
    private String selectQuery, insertQuery, sql;
    private String userCode, updateQuery, strCode, code;
    private String gpCode = "G00000";
    boolean flag;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmGroupMaster
     */
    public frmGroupMaster()
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
            sql = "select * from tblgrouphd where strGroupCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsGroup.next())
            {
                txtGroupCode.setText(rsGroup.getString(1));
                txtGroupName.setText(rsGroup.getString(2));
                if (rsGroup.getString(9).equals("Y"))
                {
                    chkOpeartionalYN.setSelected(true);
                }
                else
                {
                    chkOpeartionalYN.setSelected(false);
                }
                
                txtGroupShortName.setText(rsGroup.getString(10));
            }
            rsGroup.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSaveUpdate()
    {
        try
        {
            clsUtility obj = new clsUtility();
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                selectQuery = "select count(*) from tblgrouphd";
                countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet1.next();
                int cn = countSet1.getInt(1);
                countSet1.close();
                if (cn > 0)
                {
                    selectQuery = "select max(strGroupCode) from tblgrouphd";
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
                    int intCode = Integer.parseInt(strCode);
                    intCode++;
                    if (intCode < 10)
                    {
                        gpCode = "G000000" + intCode;
                    }
                    else if (intCode < 100)
                    {
                        gpCode = "G00000" + intCode;
                    }
                    else if (intCode < 1000)
                    {
                        gpCode = "G0000" + intCode;
                    }
                    else if (intCode < 10000)
                    {
                        gpCode = "G000" + intCode;
                    }
                    else if (intCode < 100000)
                    {
                        gpCode = "G00" + intCode;
                    }
                    else if (intCode < 1000000)
                    {
                        gpCode = "G0" + intCode;
                    }

                }
                else
                {
                    gpCode = "G0000001";
                }

                String name = txtGroupName.getText().trim();
                String code = txtGroupCode.getText().trim();
                String operationalYN = "N";
                if (chkOpeartionalYN.isSelected())
                {
                    operationalYN = "Y";
                }

                if (clsGlobalVarClass.funCheckItemName("tblgrouphd", "strGroupName", "strGroupCode", name, code, "save", ""))
                {
                    new frmOkPopUp(this, "This Group Name is Already Exist", "Error", 0).setVisible(true);
                    txtGroupName.requestFocus();
                }
                else if (!clsGlobalVarClass.validateEmpty(txtGroupName.getText()))
                {
                    new frmOkPopUp(this, "Please Enter Group Name", "Error", 0).setVisible(true);
                    txtGroupName.requestFocus();
                }
                else if (!obj.funCheckLength(txtGroupName.getText(), 30))
                {
                    new frmOkPopUp(this, "Group Name length must be less than 30", "Error", 0).setVisible(true);
                    txtGroupName.requestFocus();
                }
                else
                {
                    txtGroupCode.setText(gpCode);
                    insertQuery = "insert into tblgrouphd (strGroupCode,strGroupName,strUserCreated,"
                            + "strUserEdited,dteDateCreated,dteDateEdited,strOperationalYN,strGroupShortName,strClientCode)"
                            + "values('" + txtGroupCode.getText()
                            + "','" + txtGroupName.getText().trim() + "','" + userCode + "','" + userCode + "','"
                            + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",'" + operationalYN + "','"+txtGroupShortName.getText().trim()+"','"+clsGlobalVarClass.gClientCode+"')";
                    //System.out.println(insertQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
                    if (exc > 0)
                    {
                        String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Group' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                }
            }
            else
            {
                String name = txtGroupName.getText().trim();
                String code = txtGroupCode.getText().trim();
                String operationalYN = "N";
                if (chkOpeartionalYN.isSelected())
                {
                    operationalYN = "Y";
                }

                if (clsGlobalVarClass.funCheckItemName("tblgrouphd", "strGroupName", "strGroupCode", name, code, "update", ""))
                {
                    new frmOkPopUp(this, "This Group Name is Already Exist", "Error", 0).setVisible(true);
                    txtGroupName.requestFocus();
                }
                else if (!clsGlobalVarClass.validateEmpty(txtGroupName.getText()))
                {
                    new frmOkPopUp(this, "Please Enter Group Name", "Error", 0).setVisible(true);
                }
                else if (!obj.funCheckLength(txtGroupName.getText(), 30))
                {
                    new frmOkPopUp(this, "Group Name length must be less than 30", "Error", 0).setVisible(true);
                    txtGroupName.requestFocus();
                }
                else
                {
                    updateQuery = "UPDATE tblgrouphd SET strGroupName = '" + txtGroupName.getText().trim()+"',strUserEdited='" + userCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            +",strOperationalYN='" + operationalYN + "' "
                            + ",strGroupShortName='"+txtGroupShortName.getText().trim()+"' "
                            + " WHERE strGroupCode ='" + txtGroupCode.getText() + "'";
                    //System.out.println(updateQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                    if (exc > 0)
                    {
                        String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Group' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
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
                new frmOkPopUp(this, "Group Code is already present", "Error", 1).setVisible(true);
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
        flag = false;
        txtGroupCode.setText("");
        txtGroupName.setText("");
        txtGroupShortName.setText("");
        txtGroupCode.requestFocus();
        chkOpeartionalYN.setSelected(false);
        btnNew.setMnemonic('u');
    }

    private void funSelectGroupCode()
    {
        try
        {

            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Group");
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
        lblGroupCode2 = new javax.swing.JLabel();
        txtGroupCode = new javax.swing.JTextField();
        lblGroupName2 = new javax.swing.JLabel();
        txtGroupName = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblOperational = new javax.swing.JLabel();
        chkOpeartionalYN = new javax.swing.JCheckBox();
        lblGroupShortName = new javax.swing.JLabel();
        txtGroupShortName = new javax.swing.JTextField();

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
        lblformName.setText("-Group Master");
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
        lblFormName.setText("Group Master");

        lblGroupCode2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupCode2.setText("Group Code    :");

        txtGroupCode.setEditable(false);
        txtGroupCode.setBackground(new java.awt.Color(204, 204, 204));
        txtGroupCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGroupCodeMouseClicked(evt);
            }
        });
        txtGroupCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGroupCodeKeyPressed(evt);
            }
        });

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Group Name    :");

        txtGroupName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGroupNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtGroupNameMouseEntered(evt);
            }
        });
        txtGroupName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGroupNameKeyPressed(evt);
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

        lblOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperational.setText("Operational      :");

        chkOpeartionalYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkOpeartionalYN.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkOpeartionalYNActionPerformed(evt);
            }
        });

        lblGroupShortName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupShortName.setText("Short Name     :");

        txtGroupShortName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGroupShortNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtGroupShortNameMouseEntered(evt);
            }
        });
        txtGroupShortName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGroupShortNameKeyPressed(evt);
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
                        .addComponent(lblGroupCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblGroupName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblOperational, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(lblGroupShortName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkOpeartionalYN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGroupShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGroupCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGroupShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGroupShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkOpeartionalYN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
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


    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Group");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Group");
    }//GEN-LAST:event_formWindowClosing

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Group");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnCancelMouseClicked
    {//GEN-HEADEREND:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnResetMouseClicked
    {//GEN-HEADEREND:event_btnResetMouseClicked
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

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnNewKeyPressed
    {//GEN-HEADEREND:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveUpdate();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNewActionPerformed
    {//GEN-HEADEREND:event_btnNewActionPerformed
        // TODO add your handling code here:
        funSaveUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNewMouseClicked
    {//GEN-HEADEREND:event_btnNewMouseClicked
        // TODO add your handling code here:
        funSaveUpdate();
    }//GEN-LAST:event_btnNewMouseClicked

    private void chkOpeartionalYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkOpeartionalYNActionPerformed
    {//GEN-HEADEREND:event_chkOpeartionalYNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkOpeartionalYNActionPerformed

    private void txtGroupNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtGroupNameKeyPressed
    {//GEN-HEADEREND:event_txtGroupNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtGroupNameKeyPressed

    private void txtGroupNameMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGroupNameMouseEntered
    {//GEN-HEADEREND:event_txtGroupNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupNameMouseEntered

    private void txtGroupNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGroupNameMouseClicked
    {//GEN-HEADEREND:event_txtGroupNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtGroupName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Group Name").setVisible(true);
                txtGroupName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtGroupName.getText(), "1", "Enter Group Name").setVisible(true);
                txtGroupName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtGroupNameMouseClicked

    private void txtGroupCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtGroupCodeKeyPressed
    {//GEN-HEADEREND:event_txtGroupCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectGroupCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtGroupName.requestFocus();
        }
    }//GEN-LAST:event_txtGroupCodeKeyPressed

    private void txtGroupCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGroupCodeMouseClicked
    {//GEN-HEADEREND:event_txtGroupCodeMouseClicked

        funSelectGroupCode();
    }//GEN-LAST:event_txtGroupCodeMouseClicked

    private void txtGroupShortNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGroupShortNameMouseClicked
    {//GEN-HEADEREND:event_txtGroupShortNameMouseClicked
        try
        {
            if (txtGroupShortName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Group Short Name").setVisible(true);
                txtGroupShortName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtGroupShortName.getText(), "1", "Enter Group Short Name").setVisible(true);
                txtGroupShortName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtGroupShortNameMouseClicked

    private void txtGroupShortNameMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGroupShortNameMouseEntered
    {//GEN-HEADEREND:event_txtGroupShortNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupShortNameMouseEntered

    private void txtGroupShortNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtGroupShortNameKeyPressed
    {//GEN-HEADEREND:event_txtGroupShortNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupShortNameKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkOpeartionalYN;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGroupCode2;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblGroupShortName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperational;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtGroupCode;
    private javax.swing.JTextField txtGroupName;
    private javax.swing.JTextField txtGroupShortName;
    // End of variables declaration//GEN-END:variables
}
