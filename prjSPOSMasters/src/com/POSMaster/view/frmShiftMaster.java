/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author sss11
 */
public class frmShiftMaster extends javax.swing.JFrame
{

    private String sql, shiftDate, billDateTimeType, shiftStartTime, shiftEndTime;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmShioftmaster
     */
    public frmShiftMaster()
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

            funFillPOSCombo();
            funSetShortCutKeys();
            txtShiftNo.requestFocus();
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
     * This method is used to reset fields
     */
    public void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtShiftNo.setText("");
            txtShiftStartTime.setText("");
            txtShiftEndTime.setText("");
            cmbAMPM1.setSelectedItem("am");
            txtShiftNo.requestFocus();
            cmbAMPM2.setSelectedItem("am");
            cmbBillDateTimeType.setSelectedItem("POSDate");
            txtShiftNo.requestFocus();
            cmbPOS.setSelectedIndex(0);
            cmbPOS.setEnabled(true);
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
    public void setData(Object[] data)
    {
        try
        {
            sql ="select a.*,b.strPosName from tblshiftmaster a ,tblposmaster b "
                +"where a.strPOSCode=b.strPosCode "
                +"and a.intShiftCode='" + data[0].toString() + "' and a.strPOSCode='"+data[1].toString() +"' ";
            ResultSet rsShiftInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsShiftInfo.next();
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
            txtShiftNo.setText(rsShiftInfo.getString(1));
            txtShiftStartTime.setText(rsShiftInfo.getString(3).substring(0, rsShiftInfo.getString(3).indexOf(" ")));
            txtShiftEndTime.setText(rsShiftInfo.getString(4).substring(0, rsShiftInfo.getString(4).indexOf(" ")));
            cmbAMPM1.setSelectedItem(rsShiftInfo.getString(3).substring(rsShiftInfo.getString(3).indexOf(" ") + 1, rsShiftInfo.getString(3).length()));
            cmbAMPM2.setSelectedItem(rsShiftInfo.getString(4).substring(rsShiftInfo.getString(4).indexOf(" ") + 1, rsShiftInfo.getString(4).length()));
            cmbBillDateTimeType.setSelectedItem(rsShiftInfo.getString(5));
            cmbPOS.setSelectedItem(rsShiftInfo.getString(10) + "                                  " + rsShiftInfo.getString(2));
            
            cmbPOS.setEnabled(false);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate next shift no
     *
     * @param date
     * @return int
     */
    public int funGenerateNextShiftNo(String date)
    {
        int shiftNo = 0;
        try
        {
            String posCode=cmbPOS.getSelectedItem().toString().split("                                  ")[1].trim();
            sql = "select max(intShiftCode) from tblshiftmaster where strPOSCode='"+posCode+"' ";
            ResultSet rsShiftNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsShiftNo.next())
            {
                shiftNo = rsShiftNo.getInt(1);
                shiftNo++;
            }
            else
            {
                shiftNo = 1;
            }
            rsShiftNo.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return shiftNo;
        }
    }

    /**
     * This method is used to save shift no
     */
    public void funSaveShift()
    {
        try
        {
            shiftStartTime = txtShiftStartTime.getText() + " " + cmbAMPM1.getSelectedItem().toString();
            shiftEndTime = txtShiftEndTime.getText() + " " + cmbAMPM2.getSelectedItem().toString();
            int shift = funGenerateNextShiftNo(shiftDate);
            txtShiftNo.setText(String.valueOf(shift));
            
            String posCode=cmbPOS.getSelectedItem().toString().split("                                  ")[1].trim();
            
            
            sql = "insert into tblshiftmaster (intShiftCode,strPOSCode,tmeShiftStart,"
                    + "tmeShiftEnd,strBillDateTimeType," + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
                    + " values(" + shift + ",'" + posCode+ "','" + shiftStartTime
                    + "','" + shiftEndTime + "','" + cmbBillDateTimeType.getSelectedItem().toString()
                    + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                    + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'"+clsGlobalVarClass.gClientCode+"')";
            //System.out.println(sql);
            int insert = clsGlobalVarClass.dbMysql.execute(sql);
            if (insert > 0)
            {
                clsGlobalVarClass.gShifts = true;
                JOptionPane.showMessageDialog(this, "Shift Created Successfully");
                funResetField();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update shift
     */
    public void funUpdateShift()
    {
        try
        {
            shiftStartTime = txtShiftStartTime.getText() + " " + cmbAMPM1.getSelectedItem().toString();
            shiftEndTime = txtShiftEndTime.getText() + " " + cmbAMPM2.getSelectedItem().toString();
            
             String posCode=cmbPOS.getSelectedItem().toString().split("                                  ")[1].trim();

            sql = "update tblshiftmaster set tmeShiftStart='" + shiftStartTime + "'"
                    + ",tmeShiftEnd='" + shiftEndTime + "',strBillDateTimeType='" + cmbBillDateTimeType.getSelectedItem().toString() + "',"
                    + "strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                    + "where intShiftCode=" + txtShiftNo.getText()+" "
                    + "and strPOSCode='"+posCode+"' ";
            System.out.println("SqlUpadate==" + sql);
            int update = clsGlobalVarClass.dbMysql.execute(sql);
            if (update > 0)
            {
                JOptionPane.showMessageDialog(this, "Shift Updated Successfully");
                funResetField();
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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  
        ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblShiftNo = new javax.swing.JLabel();
        txtShiftNo = new javax.swing.JTextField();
        lblSettelmentType = new javax.swing.JLabel();
        txtShiftStartTime = new javax.swing.JTextField();
        cmbAMPM1 = new javax.swing.JComboBox();
        lblSettelmentType1 = new javax.swing.JLabel();
        txtShiftEndTime = new javax.swing.JTextField();
        cmbAMPM2 = new javax.swing.JComboBox();
        cmbBillDateTimeType = new javax.swing.JComboBox();
        lblApplicable = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblPOS = new javax.swing.JLabel();
        cmbPOS = new javax.swing.JComboBox();

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
        lblProductName.setText("SPOS -  ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Shift Master");
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

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Shift Master");

        lblShiftNo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lblShiftNo.setText("Shift No               :");

        txtShiftNo.setEditable(false);
        txtShiftNo.setBackground(new java.awt.Color(204, 204, 204));
        txtShiftNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShiftNoMouseClicked(evt);
            }
        });
        txtShiftNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShiftNoActionPerformed(evt);
            }
        });
        txtShiftNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtShiftNoKeyPressed(evt);
            }
        });

        lblSettelmentType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSettelmentType.setText("Shift Start Time      :");

        txtShiftStartTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShiftStartTimeMouseClicked(evt);
            }
        });
        txtShiftStartTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtShiftStartTimeKeyPressed(evt);
            }
        });

        cmbAMPM1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "am", "pm" }));

        lblSettelmentType1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSettelmentType1.setText("Shift End Time     :");

        txtShiftEndTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShiftEndTimeMouseClicked(evt);
            }
        });
        txtShiftEndTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtShiftEndTimeKeyPressed(evt);
            }
        });

        cmbAMPM2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "am", "pm" }));

        cmbBillDateTimeType.setBackground(new java.awt.Color(51, 102, 255));
        cmbBillDateTimeType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbBillDateTimeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "POSDate", "SystemDate" }));
        cmbBillDateTimeType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbBillDateTimeTypeKeyPressed(evt);
            }
        });

        lblApplicable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApplicable.setText("Bill Date Time         :");

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Shift");
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
        btnCancel.setToolTipText("Close Shift Master");
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

        lblPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOS.setText("POS                      :");

        cmbPOS.setBackground(new java.awt.Color(51, 102, 255));
        cmbPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOSKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(443, 443, 443)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 126, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtShiftStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cmbAMPM1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(lblSettelmentType1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtShiftEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cmbAMPM2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(cmbBillDateTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblShiftNo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPOS))
                        .addGap(29, 29, 29)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtShiftNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(93, 93, 93))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblShiftNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtShiftNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtShiftStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAMPM1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSettelmentType1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtShiftEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAMPM2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBillDateTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void funSelectShiftNo()
    {
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("Shift Master");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");
            btnNew.setMnemonic('u');
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            setData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }
    private void txtShiftNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShiftNoActionPerformed
        funResetField();
        funSelectShiftNo();
    }//GEN-LAST:event_txtShiftNoActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
        {
            funSaveShift();
        }
        else
        {
            funUpdateShift();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField(); //clear all fields of the form
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Shift Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtShiftNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShiftNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectShiftNo();
        }
        if (evt.getKeyCode() == 10)
        {
            txtShiftStartTime.requestFocus();
        }
    }//GEN-LAST:event_txtShiftNoKeyPressed

    private void txtShiftStartTimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShiftStartTimeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtShiftEndTime.requestFocus();
        }
    }//GEN-LAST:event_txtShiftStartTimeKeyPressed

    private void txtShiftEndTimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShiftEndTimeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbBillDateTimeType.requestFocus();
        }
    }//GEN-LAST:event_txtShiftEndTimeKeyPressed

    private void cmbBillDateTimeTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbBillDateTimeTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbBillDateTimeTypeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
            {
                funSaveShift();
            }
            else
            {
                funUpdateShift();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
        {
            funSaveShift();
        }
        else
        {
            funUpdateShift();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Shift Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtShiftNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShiftNoMouseClicked
        // TODO add your handling code here:
        funSelectShiftNo();
    }//GEN-LAST:event_txtShiftNoMouseClicked

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbPOSKeyPressed
    {//GEN-HEADEREND:event_cmbPOSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPOSKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Shift Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Shift Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtShiftStartTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShiftStartTimeMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtShiftStartTime.getText().length() == 0)
            {
               new frmNumericKeyboard(this,true,txtShiftStartTime.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
               txtShiftStartTime.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this,true,txtShiftStartTime.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
                txtShiftStartTime.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_txtShiftStartTimeMouseClicked

    private void txtShiftEndTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShiftEndTimeMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtShiftEndTime.getText().length() == 0)
            {
               new frmNumericKeyboard(this,true,txtShiftEndTime.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
               txtShiftEndTime.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this,true,txtShiftEndTime.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
                txtShiftEndTime.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtShiftEndTimeMouseClicked

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
            java.util.logging.Logger.getLogger(frmShiftMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmShiftMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmShiftMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmShiftMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmShiftMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbAMPM1;
    private javax.swing.JComboBox cmbAMPM2;
    private javax.swing.JComboBox cmbBillDateTimeType;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblApplicable;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOS;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSettelmentType;
    private javax.swing.JLabel lblSettelmentType1;
    private javax.swing.JLabel lblShiftNo;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtShiftEndTime;
    private javax.swing.JTextField txtShiftNo;
    private javax.swing.JTextField txtShiftStartTime;
    // End of variables declaration//GEN-END:variables

    private void funFillPOSCombo()
    {
        try
        {
            String sqlPOS = "select a.strPosCode,a.strPosName from tblposmaster a order by a.strPosCode";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);

            cmbPOS.removeAllItems();
            while (rsPOS.next())
            {
                cmbPOS.addItem(rsPOS.getString(2) + "                                  " + rsPOS.getString(1));
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
}
