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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmOrderMaster extends javax.swing.JFrame
{

    private String sql;
    boolean flag;
    private HashMap<String, String> mapPOSCode;
    private HashMap<String, String> mapPOSName;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This default constructor is used to initialized area master
     *
     */
    public frmOrderMaster()
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
            java.util.Date dt = new java.util.Date();
            int day = dt.getDate();
            int month = dt.getMonth() + 1;
            int year = dt.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            txtOrderDesc.requestFocus();
            funLoadPOSCombo();
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
     * This method is used to set data to pos combobox
     *
     * @param data of type object
     * @return
     */
    private void funSetData(Object[] data)
    {
        try
        {
            sql = "select strOrderCode,strOrderDesc,tmeUpToTime,strPOSCode from tblordermaster where strOrderCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsOrderInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsOrderInfo.next())
            {
                txtOrderCode.setText(rsOrderInfo.getString(1));
                txtOrderDesc.setText(rsOrderInfo.getString(2));
                String[] upToTime = rsOrderInfo.getString(3).split(" ");
                String[] time = upToTime[0].split(":");
                cmbUpToHour.setSelectedItem(time[0]);
                cmbUpToMinute.setSelectedItem(time[1]);
                cmbUpToAMPM.setSelectedItem(upToTime[1]);
                
                cmbPOS.setSelectedItem(mapPOSCode.get(rsOrderInfo.getString(4)));
                
            }
            rsOrderInfo.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate order codes
     *
     * @return String orderCode
     */
    private String funGenerateOrderCode()
    {
        String orderCode = "";
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='Order'";
            ResultSet rsOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsOrderCode.next();
            int orderCodeCnt = rsOrderCode.getInt(1);
            rsOrderCode.close();
            if (orderCodeCnt > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='Order'";
                rsOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsOrderCode.next();
                long code = rsOrderCode.getLong(1);
                code = code + 1;
                orderCode = "OR" + String.format("%06d", code);
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='Order'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                orderCode = "OR000001";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('Order'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return orderCode;
    }

    /**
     * This method is used to save area
     *
     * @return
     */
    private void funSaveArea()
    {
        try
        {
            String name = txtOrderDesc.getText().trim();
            String code = txtOrderCode.getText().trim();
            String upToTime = cmbUpToHour.getSelectedItem().toString() + ":" + cmbUpToMinute.getSelectedItem().toString() + " " + cmbUpToAMPM.getSelectedItem().toString();;

            String posName = cmbPOS.getSelectedItem().toString();
            String posCode = mapPOSName.get(posName);

            if (clsGlobalVarClass.funCheckItemName("tblordermaster", "strOrderDesc", "strOrderCode", name, code, "save", ""))
            {
                new frmOkPopUp(null, "Order Name is Already Exist", "Error", 0).setVisible(true);
                txtOrderDesc.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtOrderDesc.getText()))
            {
                new frmOkPopUp(null, "Please Enter Order Name", "Error", 0).setVisible(true);
            }
            else
            {
                txtOrderCode.setText(funGenerateOrderCode());

                sql = "insert into tblordermaster (strOrderCode,strOrderDesc,tmeUpToTime,strUserCreated,strUserEdited"
                        + ",dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strPOSCode)"
                        + "values('" + txtOrderCode.getText() + "','" + txtOrderDesc.getText() + "','" + upToTime + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + posCode + "')";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Order' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(null, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetFields();
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
     * This method is used to update area
     *
     * @return
     */
    private void funUpdateArea()
    {
        try
        {

            String upToTime = cmbUpToHour.getSelectedItem().toString() + ":" + cmbUpToMinute.getSelectedItem().toString() + " " + cmbUpToAMPM.getSelectedItem().toString();;

            String code = txtOrderCode.getText().trim();
            String name = txtOrderDesc.getText().trim();

            String posName = cmbPOS.getSelectedItem().toString();
            String posCode = mapPOSName.get(posName);

            if (clsGlobalVarClass.funCheckItemName("tblordermaster", "strOrderDesc", "strOrderCode", name, code, "update", ""))
            {
                new frmOkPopUp(null, "Order Name is Already Exist", "Error", 0).setVisible(true);
                txtOrderDesc.requestFocus();
            }
            else
            {
                sql = "UPDATE tblordermaster SET strOrderDesc = '" + txtOrderDesc.getText() + "',tmeUpToTime = '" + upToTime + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',"
                        + "dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strPOSCode='" + posCode + "' "
                        + " WHERE strOrderCode ='" + txtOrderCode.getText() + "'";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Order' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(null, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetFields();
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
     * This method is used to reset all fields
     *
     * @return
     */
    private void funResetFields()
    {
        try
        {
            btnNew.setText("SAVE");
            flag = false;
            txtOrderCode.setText("");
            txtOrderDesc.setText("");
            cmbUpToHour.setSelectedIndex(0);
            cmbUpToMinute.setSelectedIndex(0);
            cmbUpToAMPM.setSelectedIndex(0);
            txtOrderDesc.requestFocus();
            cmbPOS.setSelectedIndex(0);
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
        lblOrderCode = new javax.swing.JLabel();
        lblGroupName2 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        txtOrderCode = new javax.swing.JTextField();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        txtOrderDesc = new javax.swing.JTextField();
        lblOrderFromTime1 = new javax.swing.JLabel();
        cmbUpToHour = new javax.swing.JComboBox();
        cmbUpToMinute = new javax.swing.JComboBox();
        cmbUpToAMPM = new javax.swing.JComboBox();
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
        lblProductName.setText("SPOS- ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Order Master");
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

        lblOrderCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderCode.setText("Order Code   :");

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Order Desc   :");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Area Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Area Master");
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

        txtOrderCode.setEnabled(false);
        txtOrderCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtOrderCodeMouseClicked(evt);
            }
        });
        txtOrderCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderCodeActionPerformed(evt);
            }
        });
        txtOrderCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderCodeKeyPressed(evt);
            }
        });

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Order Master");

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

        txtOrderDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtOrderDescMouseClicked(evt);
            }
        });
        txtOrderDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderDescKeyPressed(evt);
            }
        });

        lblOrderFromTime1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderFromTime1.setText("Up To Time :");

        cmbUpToHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "00" }));
        cmbUpToHour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUpToHourActionPerformed(evt);
            }
        });
        cmbUpToHour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbUpToHourKeyPressed(evt);
            }
        });

        cmbUpToMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbUpToMinute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUpToMinuteActionPerformed(evt);
            }
        });
        cmbUpToMinute.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbUpToMinuteKeyPressed(evt);
            }
        });

        cmbUpToAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbUpToAMPM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbUpToAMPMKeyPressed(evt);
            }
        });

        lblPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOS.setText("POS            :");

        cmbPOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOSActionPerformed(evt);
            }
        });
        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOSKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(434, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(287, 287, 287)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblOrderCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtOrderCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblGroupName2, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                    .addComponent(lblPOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtOrderDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblOrderFromTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(cmbUpToHour, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbUpToMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbUpToAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrderCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOrderCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOrderDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbUpToAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbUpToMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbUpToHour, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrderFromTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Order Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveArea();
        }
        else
        {
            funUpdateArea();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void funSearchOrderCode()
    {
        flag = true;
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("OrderMaster");
        new frmSearchFormDialog(null, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");//UpdateD
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetData(data);
        }
    }
    private void txtOrderCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOrderCodeMouseClicked
        // TODO add your handling code here:
        funSearchOrderCode();

    }//GEN-LAST:event_txtOrderCodeMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void txtOrderCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderCodeKeyPressed
        // TODO add your handling code here:
       //Open help on key '?' or key '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSearchOrderCode();
        }
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            txtOrderDesc.requestFocus();
        }
    }//GEN-LAST:event_txtOrderCodeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveArea();
            }
            else
            {
                funUpdateArea();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveArea();
        }
        else
        {
            funUpdateArea();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Order Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtOrderDescMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOrderDescMouseClicked
        // TODO add your handling code here:
        if (txtOrderDesc.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(null, true, "1", "Enter Order Name").setVisible(true);
            txtOrderDesc.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(null, true, txtOrderDesc.getText(), "1", "Enter Order Name").setVisible(true);
            txtOrderDesc.setText(clsGlobalVarClass.gKeyboardValue);
        }

    }//GEN-LAST:event_txtOrderDescMouseClicked

    private void txtOrderDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderDescKeyPressed
        // TODO add your handling code here:
        //Open help on key '?' or key '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSearchOrderCode();
        }
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            cmbPOS.requestFocus();
        }
    }//GEN-LAST:event_txtOrderDescKeyPressed

    private void txtOrderCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderCodeActionPerformed

    private void cmbUpToHourKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbUpToHourKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbUpToMinute.requestFocus();
        }
    }//GEN-LAST:event_cmbUpToHourKeyPressed

    private void cmbUpToMinuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbUpToMinuteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbUpToAMPM.requestFocus();
        }
    }//GEN-LAST:event_cmbUpToMinuteKeyPressed

    private void cmbUpToAMPMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbUpToAMPMKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
           btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbUpToAMPMKeyPressed

    private void cmbUpToHourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUpToHourActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUpToHourActionPerformed

    private void cmbUpToMinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUpToMinuteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUpToMinuteActionPerformed

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPOSActionPerformed
    {//GEN-HEADEREND:event_cmbPOSActionPerformed

    }//GEN-LAST:event_cmbPOSActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Order Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Order Master");
    }//GEN-LAST:event_formWindowClosing

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOSKeyPressed
        // TODO add your handling code here:
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            cmbUpToHour.requestFocus();
        }
    }//GEN-LAST:event_cmbPOSKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.JComboBox cmbUpToAMPM;
    private javax.swing.JComboBox cmbUpToHour;
    private javax.swing.JComboBox cmbUpToMinute;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOrderCode;
    private javax.swing.JLabel lblOrderFromTime1;
    private javax.swing.JLabel lblPOS;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtOrderCode;
    private javax.swing.JTextField txtOrderDesc;
    // End of variables declaration//GEN-END:variables

    private void funLoadPOSCombo()
    {
        try
        {
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
            mapPOSCode = new HashMap<String, String>();
            mapPOSName = new HashMap<String, String>();
            cmbPOS.addItem("All");
            mapPOSCode.put("All", "All");
            mapPOSName.put("All", "All");
            while (rsPOS.next())
            {
                cmbPOS.addItem(rsPOS.getString(2));
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
}
