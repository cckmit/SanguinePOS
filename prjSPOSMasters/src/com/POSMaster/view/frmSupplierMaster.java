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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmSupplierMaster extends javax.swing.JFrame
{

    private ResultSet countSet, countSet1;
    private String selectQuery, insertQuery;
    private String updateQuery, strCode, code, sql;
    private String gpCode = "CC00000";
    boolean flag;
    private List<String> arrMobileNoList = new ArrayList<String>();
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize CostCenterMaster
     */
    public frmSupplierMaster()
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
            txtSupplierCode.requestFocus();

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
    private void funSetSupplierData(Object[] data) throws Exception
    {
        sql = "select a.strSupplierCode,a.strSupplierName"
                + ",a.strAddress1,a.strAddress2,a.intMobileNumber,a.strContactPerson,a.strEmailId,a.strGSTNo "
                + " from tblSuppliermaster  a "
                + " where a.strSupplierCode='" + clsGlobalVarClass.gSearchedItem + "'";
        //System.out.println(sql);
        ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsCostCenter.next();
        txtSupplierCode.setText(rsCostCenter.getString(1));//code
        txtSupplierName.setText(rsCostCenter.getString(2));//name
        btnNew.setMnemonic('u');
        txtAddress1.setText(rsCostCenter.getString(3));
        txtAddress2.setText(rsCostCenter.getString(4));
        txtContactPerson.setText(rsCostCenter.getString(6));
        txtMobileNo.setText(rsCostCenter.getString(5));
        txtEmailId.setText(rsCostCenter.getString(7));
        txtGSTNo.setText(rsCostCenter.getString(8));
        rsCostCenter.close();

        txtSupplierCode.requestFocus();

    }

    /**
     * This method is used to save cost center
     */
    private void funSaveSupplier()
    {
        try
        {
            selectQuery = "select count(*) from tblsuppliermaster";
            countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            countSet1.next();
            int cn = countSet1.getInt(1);
            countSet1.close();
            if (cn > 0)
            {
                selectQuery = "select max(strSupplierCode) from tblsuppliermaster";
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
                    gpCode = "S00000" + intCode;
                }
                else if (intCode < 100)
                {
                    gpCode = "S0000" + intCode;
                }
                else if (intCode < 1000)
                {
                    gpCode = "S000" + intCode;
                }
                else if (intCode < 10000)
                {
                    gpCode = "S00" + intCode;
                }
                else if (intCode < 100000)
                {
                    gpCode = "S0" + intCode;
                }
                else if (intCode < 1000000)
                {
                    gpCode = "G" + intCode;
                }              
            }
            else
            {
                code = "0";                
                gpCode = "S000001";

            }
            clsUtility obj = new clsUtility();
            String itemItem = txtSupplierName.getText().trim();
            String code = "";
            if (clsGlobalVarClass.funCheckItemName("tblsuppliermaster", "strSupplierName", "strSupplierCode", itemItem, code, "save", ""))
            {
                new frmOkPopUp(this, "This Supplier Name is Already Exist", "Error", 0).setVisible(true);
                txtSupplierName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtSupplierName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Supplier Name", "Error", 0).setVisible(true);
                txtSupplierCode.requestFocus();
            }
            else if (!obj.funCheckLength(txtSupplierName.getText(), 30))
            {
                new frmOkPopUp(this, "Supplier Name length must be less than 30", "Error", 0).setVisible(true);
                txtSupplierName.requestFocus();
            }
//            else if (!clsGlobalVarClass.validateEmpty(txtEmailId.getText()))
//            {
//                new frmOkPopUp(this, "Please Enter Email Address", "Error", 0).setVisible(true);
//                return;
//            }
//            else if (!clsGlobalVarClass.validateEmail(txtEmailId.getText().trim()))
//            {
//                new frmOkPopUp(this, "Please Enter valid Email Address", "Error", 0).setVisible(true);
//                txtEmailId.requestFocus();
//                return;
//            }
            else
            {

                String address1 = txtAddress1.getText().toString().replaceAll("\\\\", "#");
                String addess2 = txtAddress2.getText().trim().replaceAll("\\\\", "#");

                txtSupplierCode.setText(gpCode);
                insertQuery = "insert into tblsuppliermaster "
                        + "(strSupplierCode,strSupplierName,strAddress1,strAddress2,intMobileNumber,strContactPerson,strEmailId,strGSTNo"
                        + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag )"
                        + "values('" + txtSupplierCode.getText() + "','" + txtSupplierName.getText() + "'"
                        + ",'" + address1 + "','" + addess2 + "','" + txtMobileNo.getText() + "','" + txtContactPerson.getText() + "','" + txtEmailId.getText() + "','" + txtGSTNo.getText() + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";

                int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);

                if (exc > 0)
                {
                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Supplier Master' ";
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
     * This method is used to update cost center
     */
    private void funUpdateSupplier()
    {
        try
        {
            clsUtility obj = new clsUtility();
            String itemItem = txtSupplierName.getText().trim();
            String supplierCode = txtSupplierCode.getText().trim();

            if (clsGlobalVarClass.funCheckItemName("tblsuppliermaster", "strSupplierName", "strSupplierCode", itemItem, supplierCode, "update", ""))
            {
                new frmOkPopUp(this, "This Supplier Name is Already Exist", "Error", 0).setVisible(true);
                txtSupplierName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtSupplierName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Supplier Name", "Error", 0).setVisible(true);
                txtSupplierCode.requestFocus();
            }
            else if (!obj.funCheckLength(txtSupplierName.getText(), 30))
            {
                new frmOkPopUp(this, "Supplier Name length must be less than 30", "Error", 0).setVisible(true);
                txtSupplierName.requestFocus();
            }
//            else if (!clsGlobalVarClass.validateEmpty(txtEmailId.getText()))
//            {
//                new frmOkPopUp(this, "Please Enter Email Address", "Error", 0).setVisible(true);
//                return;
//            }
//            else if (!clsGlobalVarClass.validateEmail(txtEmailId.getText().trim()))
//            {
//                new frmOkPopUp(this, "Please Enter valid Email Address", "Error", 0).setVisible(true);
//                txtEmailId.requestFocus();
//                return;
//            }            
            else
            {
                String address1 = txtAddress1.getText().toString().replaceAll("\\\\", "#");
                String address2 = txtAddress2.getText().trim().replaceAll("\\\\", "#");
                String mobileNo = txtMobileNo.getText();
                String contactPerson = txtContactPerson.getText();
                String emailId = txtEmailId.getText();
                String gstNo = txtGSTNo.getText();

                updateQuery = "UPDATE tblsuppliermaster "
                        + "SET strSupplierName = '" + txtSupplierName.getText() + "'"
                        + ",strAddress1='" + address1 + "',strAddress2='" + address2 + "'"
                        + ",intMobileNumber='" + mobileNo + "',strContactPerson = '" + contactPerson + "',strEmailId = '" + emailId + "',strGSTNo = '" + gstNo + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N'"
                        + " WHERE strSupplierCode ='" + txtSupplierCode.getText() + "'";

                int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);

                clsGlobalVarClass.dbMysql.execute("delete from tblprintersetup where strcostcentercode='" + txtSupplierCode.getText() + "'");

                if (exc > 0)
                {
                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Supplier Master' ";
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

    private void funSelectSupplier()
    {
        try
        {
            //Search for cost center to update
            //btnNew.setText("UPDATE");
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("SupplierMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetSupplierData(data);
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
     * This method is used to reset all fields
     */
    private void funResetField()
    {

        txtSupplierCode.requestFocus();
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');
        flag = false;
        txtSupplierCode.setText("");
        txtSupplierName.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtContactPerson.setText("");
        txtEmailId.setText("");
        txtMobileNo.setText("");
        txtGSTNo.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblfromName = new javax.swing.JLabel();
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
        lblCostCode = new javax.swing.JLabel();
        txtSupplierCode = new javax.swing.JTextField();
        txtSupplierName = new javax.swing.JTextField();
        lblGroupName2 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        txtAddress1 = new javax.swing.JTextField();
        txtAddress2 = new javax.swing.JTextField();
        lblLabelOnKOT = new javax.swing.JLabel();
        lblLabelOnKOT1 = new javax.swing.JLabel();
        lblLabelOnKOT2 = new javax.swing.JLabel();
        txtMobileNo = new javax.swing.JTextField();
        lblLabelOnKOT3 = new javax.swing.JLabel();
        lblLabelOnKOT4 = new javax.swing.JLabel();
        lblLabelOnKOT5 = new javax.swing.JLabel();
        txtContactPerson = new javax.swing.JTextField();
        txtEmailId = new javax.swing.JTextField();
        txtGSTNo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        lblfromName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfromName.setForeground(new java.awt.Color(255, 255, 255));
        lblfromName.setText("Supplier ");
        panelHeader.add(lblfromName);
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
        lblUserCode.setPreferredSize(new java.awt.Dimension(71, 30));
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
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 559));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Supplier Master");

        lblCostCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCostCode.setText("Supplier Code       :");

        txtSupplierCode.setEditable(false);
        txtSupplierCode.setBackground(new java.awt.Color(204, 204, 204));
        txtSupplierCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSupplierCodeMouseClicked(evt);
            }
        });
        txtSupplierCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierCodeKeyPressed(evt);
            }
        });

        txtSupplierName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSupplierNameMouseClicked(evt);
            }
        });
        txtSupplierName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierNameActionPerformed(evt);
            }
        });
        txtSupplierName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierNameKeyPressed(evt);
            }
        });

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Supplier Name      :");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Cost Center Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Cost Center Master");
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

        txtAddress1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAddress1MouseClicked(evt);
            }
        });
        txtAddress1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddress1ActionPerformed(evt);
            }
        });
        txtAddress1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddress1KeyPressed(evt);
            }
        });

        txtAddress2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAddress2MouseClicked(evt);
            }
        });
        txtAddress2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddress2KeyPressed(evt);
            }
        });

        lblLabelOnKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT.setText("Address1            :");

        lblLabelOnKOT1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT1.setText("Address2            :");

        lblLabelOnKOT2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT2.setText("Mobile No          :");

        txtMobileNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMobileNoMouseClicked(evt);
            }
        });
        txtMobileNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMobileNoActionPerformed(evt);
            }
        });
        txtMobileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMobileNoKeyPressed(evt);
            }
        });

        lblLabelOnKOT3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT3.setText("Contact Person            :");

        lblLabelOnKOT4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT4.setText("Email Id            :");

        lblLabelOnKOT5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT5.setText("GST No            :");

        txtContactPerson.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtContactPersonMouseClicked(evt);
            }
        });
        txtContactPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactPersonActionPerformed(evt);
            }
        });
        txtContactPerson.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContactPersonKeyPressed(evt);
            }
        });

        txtEmailId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEmailIdMouseClicked(evt);
            }
        });
        txtEmailId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailIdActionPerformed(evt);
            }
        });
        txtEmailId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailIdKeyPressed(evt);
            }
        });

        txtGSTNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtGSTNoMouseClicked(evt);
            }
        });
        txtGSTNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGSTNoActionPerformed(evt);
            }
        });
        txtGSTNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTNoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLabelOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(txtSupplierName, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblLabelOnKOT2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(txtAddress1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblLabelOnKOT4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEmailId))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblLabelOnKOT1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtAddress2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))))
                        .addGap(41, 41, 41))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSupplierCode, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblLabelOnKOT5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblLabelOnKOT3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(385, 385, 385))))
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtAddress1, txtAddress2, txtSupplierName});

        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLabelOnKOT2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLabelOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLabelOnKOT1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLabelOnKOT3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLabelOnKOT4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLabelOnKOT5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 0);
        panelLayout.add(panelBody, gridBagConstraints);

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSupplierCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupplierCodeMouseClicked
        // TODO add your handling code here:
        funSelectSupplier();

    }//GEN-LAST:event_txtSupplierCodeMouseClicked

    private void txtSupplierNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupplierNameMouseClicked
        // TODO add your handling code here:
        if (txtSupplierName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Supplier Name").setVisible(true);
            txtSupplierName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtSupplierName.getText(), "1", "Enter Supplier Name").setVisible(true);
            txtSupplierName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtSupplierNameMouseClicked

    private void txtSupplierNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierNameKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSupplierNameKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
            //    int totalTableRows = tblItemTable.getRowCount();

            dispose();
            clsGlobalVarClass.hmActiveForms.remove("Supplier Master");
        }
        catch (Exception e)
        {
        }

    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funSaveSupplier();
        }
        else
        {
            //Update existing cost center
            funUpdateSupplier();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void txtAddress1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAddress1MouseClicked
        try
        {
            if (txtAddress1.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Address.").setVisible(true);
                txtAddress1.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtAddress1.getText(), "1", "Please Enter Address.").setVisible(true);
                txtAddress1.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtAddress1MouseClicked

    private void txtAddress1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddress1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddress1KeyPressed

    private void txtSupplierCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectSupplier();
        }
        if (evt.getKeyCode() == 10)
        {
            txtSupplierName.requestFocus();
        }

    }//GEN-LAST:event_txtSupplierCodeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                //Add new cost center
                funSaveSupplier();
            }
            else
            {
                //Update existing cost center
                funUpdateSupplier();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
//        if (txtMobileNo.getText().toString().contains(",") )
//        {
//            String []arrMobileList=txtMobileNo.getText().toString().split(",");
//               for (int cnt = 0; cnt < arrMobileList.length; cnt++)
//                {
//                    if (!arrMobileList[cnt].matches("\\d{10}") )
//                    {
//                         new frmOkPopUp(this, "Please Enter Valid Mobile Number.", "Error", 0).setVisible(true);
//                         return;
//                    }
//                    else
//                    {
//                        if(cnt>0)
//                        {
//                            try
//                            {
//                                sql = "select count(strSupplierCode),strSupplierName from tblsuppliermaster where intMobileNumber like '%" + arrMobileList[cnt] + "%'";
//                                ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                                rsCustomer.next();
//                                int found = rsCustomer.getInt(1);
//                                String customerName= rsCustomer.getString(2);
//                                rsCustomer.close();
//
//                                if (found > 0)
//                                {
//                                   new frmOkPopUp(this, "Mobile No already exists for another Supplier ", "Error", 0).setVisible(true);
//                                   return;
//                                }
//                                else
//                                {
//                                    arrMobileNoList.add(arrMobileList[cnt]);
//                                }
//                            }
//                            catch (Exception e)
//                            {
//                                objUtility.funWriteErrorLog(e);
//                                e.printStackTrace();
//                            }
//                        } 
//                        else
//                        {
//                             arrMobileNoList.add(arrMobileList[cnt]);
//                        }   
//                            
//                    }        
//                }
//        }
//        else
//        {
//           if (!txtMobileNo.getText().matches("\\d{10}") )
//            {
//                new frmOkPopUp(this, "Please Enter Valid Mobile Number.", "Error", 0).setVisible(true);
//                return;
//            }
//        } 

        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funSaveSupplier();
        }
        else
        {
            //Update existing cost center
            funUpdateSupplier();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Supplier Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtAddress2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAddress2MouseClicked
        try
        {
            if (txtAddress2.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Address2.").setVisible(true);
                txtAddress2.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtAddress2.getText(), "1", "Please Enter Address2.").setVisible(true);
                txtAddress2.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtAddress2MouseClicked

    private void txtAddress2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddress2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddress2KeyPressed

    private void txtAddress1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddress1ActionPerformed

    }//GEN-LAST:event_txtAddress1ActionPerformed

    private void txtSupplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierNameActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSupplierNameActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Supplier Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Supplier Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtMobileNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMobileNoMouseClicked
        try
        {
            if (txtMobileNo.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Mobile Number.").setVisible(true);
                txtMobileNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtMobileNo.getText(), "1", "Please Enter Mobile Number.").setVisible(true);
                txtMobileNo.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtMobileNoMouseClicked

    private void txtMobileNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMobileNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileNoActionPerformed

    private void txtMobileNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileNoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileNoKeyPressed

    private void txtContactPersonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtContactPersonMouseClicked
        try
        {
            if (txtContactPerson.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Contact Person Name.").setVisible(true);
                txtContactPerson.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtContactPerson.getText(), "1", "Please Enter Contact Person Name.").setVisible(true);
                txtContactPerson.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtContactPersonMouseClicked

    private void txtContactPersonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactPersonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactPersonActionPerformed

    private void txtContactPersonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactPersonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactPersonKeyPressed

    private void txtEmailIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailIdMouseClicked
        try
        {
            if (txtEmailId.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Email Id.").setVisible(true);
                txtEmailId.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtEmailId.getText(), "1", "Please Enter Email Id.").setVisible(true);
                txtEmailId.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtEmailIdMouseClicked

    private void txtEmailIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdActionPerformed

    private void txtEmailIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailIdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdKeyPressed

    private void txtGSTNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGSTNoMouseClicked
        try
        {
            if (txtGSTNo.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter GST No.").setVisible(true);
                txtGSTNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtGSTNo.getText(), "1", "Please Enter GST No.").setVisible(true);
                txtGSTNo.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtGSTNoMouseClicked

    private void txtGSTNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGSTNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTNoActionPerformed

    private void txtGSTNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTNoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTNoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCostCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblLabelOnKOT;
    private javax.swing.JLabel lblLabelOnKOT1;
    private javax.swing.JLabel lblLabelOnKOT2;
    private javax.swing.JLabel lblLabelOnKOT3;
    private javax.swing.JLabel lblLabelOnKOT4;
    private javax.swing.JLabel lblLabelOnKOT5;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblfromName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtAddress1;
    private javax.swing.JTextField txtAddress2;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtEmailId;
    private javax.swing.JTextField txtGSTNo;
    private javax.swing.JTextField txtMobileNo;
    private javax.swing.JTextField txtSupplierCode;
    private javax.swing.JTextField txtSupplierName;
    // End of variables declaration//GEN-END:variables

}
