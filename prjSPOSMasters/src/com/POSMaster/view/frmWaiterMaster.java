/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsFixedSizeText;
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
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;

public class frmWaiterMaster extends javax.swing.JFrame
{

    private String code, strCode;
    boolean flag;
    clsUtility obj = new clsUtility();

    /**
     * This method is used to initialize frmWaiterMaster
     */
    public frmWaiterMaster()
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
            txtWShortName.setDocument(new clsFixedSizeText(10));
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtWaiterNo.requestFocus();

            funFillPOSCombo();
            funSetShortCutKeys();
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
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
    private void funSetWaiterData(Object[] data)
    {
        try
        {
            String sql = "select * from tblwaitermaster where strWaiterNo='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsWaiterInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsWaiterInfo.next())
            {
                txtWaiterNo.setText(rsWaiterInfo.getString(1));
                txtWShortName.setText(rsWaiterInfo.getString(2));
                txtWFullName.setText(rsWaiterInfo.getString(3));
                if (rsWaiterInfo.getString(5).equals("Y"))
                {
                    chkOperational.setSelected(true);
                }
                txtDebitCardString.setText(rsWaiterInfo.getString(6));
                if(rsWaiterInfo.getString(13).equalsIgnoreCase("All"))
                {
                    cmbPOS.setSelectedItem("All"+"                                  "+"All");
                }
                else
                {
                    sql = "select strPOSName,strPOSCode from tblposmaster where strPOSCode='"+rsWaiterInfo.getString(13)+"' ";
                    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    rsPOS.next();
                    cmbPOS.setSelectedItem(rsPOS.getString(1) + "                                  " + rsPOS.getString(2));
                }                                
            }
            rsWaiterInfo.close();

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select waiter
     */
    private void funSelectWaiter()
    {
        try
        {
            flag = true;
            //new frmSearchForm(this,"frmWaiterMaster").setVisible(true);
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("WaiterMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");//UpdateD
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetWaiterData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate waiter code
     *
     * @return
     */
    private String funGenerateWaiterCode()
    {
        String strWaiterCode = "";
        try
        {
            String sql = "select count(*) from tblwaitermaster";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cn = rs.getInt(1);
            rs.close();
            if (cn > 0)
            {
                sql = "select max(strWaiterNo) from tblwaitermaster";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                code = rs.getString(1);
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

                if (intCode < 100)
                {
                    strWaiterCode = "W" + String.format("%03d", intCode);
                }
                else
                {
                    strWaiterCode = "W" + intCode;
                }
            }
            else
            {
                strWaiterCode = "W001";
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return strWaiterCode;
    }

    /**
     * This method is used to save waiter
     */
    private void funSaveWaiter()
    {
        try
        {
            String waiterName = txtWShortName.getText().trim();
            String code = "";
            if (clsGlobalVarClass.funCheckItemName("tblwaitermaster", "strWShortName", "strWaiterNo", waiterName, code, "save", ""))
            {
                new frmOkPopUp(this, "This  Waiter Name is Already Exist", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtWShortName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Waiters Short Name", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();
                txtWShortName.setText("");
            }
            else if (!obj.funCheckLength(txtWShortName.getText(), 10))
            {
                new frmOkPopUp(this, "Waiter Short Name length must be less than 10", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();

            }
            else if (!clsGlobalVarClass.validateEmpty(txtWFullName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Waiters Full Name", "Error", 0).setVisible(true);
                txtWFullName.requestFocus();
                txtWFullName.setText("");
            }
            else if (!obj.funCheckLength(txtWFullName.getText(), 30))
            {
                new frmOkPopUp(this, "Waiter Full Name length must be less than 30", "Error", 0).setVisible(true);
                txtWFullName.requestFocus();

            }
            else
            {
                txtWaiterNo.setText(funGenerateWaiterCode());
                String operational = "N";
                if (chkOperational.isSelected())
                {
                    operational = "Y";
                }
                String posCode=cmbPOS.getSelectedItem().toString().split("                                  ")[1].trim();
                
                
                String sql = "insert into tblwaitermaster "
                        + "(strWaiterNo,strWShortName,strWFullName,strStatus"
                        + ",strOperational,strDebitCardString,strUserCreated"
                        + ",strUserEdited,dteDateCreated,dteDateEdited,strPOSCode,strClientCode) "
                        + "values('" + txtWaiterNo.getText() + "','" + txtWShortName.getText()
                        + "','" + txtWFullName.getText() + "','Normal','" + operational + "'"
                        + ",'" + new clsUtility().funGetSingleTrackData(txtDebitCardString.getText().trim()) + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"+posCode+"','"+clsGlobalVarClass.gClientCode+"')";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Waiter' and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Entry added Successfully", "Successful", 3).setVisible(true);
                    funResetField();
                }
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update waiter
     */
    private void funUpdateWaiter()
    {
        try
        {
            String waiterName = txtWShortName.getText().trim();
            String code = txtWaiterNo.getText().trim();
            if (clsGlobalVarClass.funCheckItemName("tblwaitermaster", "strWShortName", "strWaiterNo", waiterName, code, "update", ""))
            {
                new frmOkPopUp(this, "This  Waiter Name is Already Exist", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtWShortName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Waiters Short Name", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();
                txtWShortName.setText("");
            }
            else if (!obj.funCheckLength(txtWShortName.getText(), 10))
            {
                new frmOkPopUp(this, "Waiter Short Name length must be less than 10", "Error", 0).setVisible(true);
                txtWShortName.requestFocus();

            }
            else if (!clsGlobalVarClass.validateEmpty(txtWFullName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Waiters Full Name", "Error", 0).setVisible(true);
                txtWFullName.requestFocus();
                txtWFullName.setText("");
            }
            else if (!obj.funCheckLength(txtWFullName.getText(), 30))
            {
                new frmOkPopUp(this, "Waiter Full Name length must be less than 30", "Error", 0).setVisible(true);
                txtWFullName.requestFocus();

            }
            else
            {
                String operational = "N";
                if (chkOperational.isSelected())
                {
                    operational = "Y";
                }
                String posCode=cmbPOS.getSelectedItem().toString().split("                                  ")[1].trim();
                
                String sql = "UPDATE tblwaitermaster "
                        + "SET strWShortName = '" + txtWShortName.getText() + "',strWFullName='" + txtWFullName.getText() + "'"
                        + ",strOperational='" + operational + "',strDebitCardString='" + new clsUtility().funGetSingleTrackData(txtDebitCardString.getText().trim()) + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N',strPOSCode='"+posCode+"' "
                        + "WHERE strWaiterNo ='" + txtWaiterNo.getText() + "'";
                //System.out.println(updateQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='Waiter' ";//and strClientCode='"+clsGlobalVarClass.gClientCode+"'
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successful", 3).setVisible(true);
                    funResetField();
                }
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            flag = false;
            txtWaiterNo.setText("");
            txtWFullName.setText("");
            txtWShortName.setText("");
            txtWShortName.requestFocus();
            chkOperational.setSelected(false);
            txtDebitCardString.setText("");
            cmbPOS.setSelectedIndex(0);

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
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
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        txtWaiterNo = new javax.swing.JTextField();
        txtWShortName = new javax.swing.JTextField();
        lblWShortName = new javax.swing.JLabel();
        lblWFullName = new javax.swing.JLabel();
        txtWFullName = new javax.swing.JTextField();
        lblWaiterNo = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        chkOperational = new javax.swing.JCheckBox();
        lblOperational = new javax.swing.JLabel();
        lblSwipeCard = new javax.swing.JLabel();
        txtDebitCardString = new javax.swing.JPasswordField();
        cmbPOS = new javax.swing.JComboBox();
        lblPOS = new javax.swing.JLabel();

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
        lblformName.setText("- Waiter Master");
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
        panelLayout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLayout.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Waiter Master");

        txtWaiterNo.setEditable(false);
        txtWaiterNo.setBackground(new java.awt.Color(204, 204, 204));
        txtWaiterNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWaiterNoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtWaiterNoMouseEntered(evt);
            }
        });
        txtWaiterNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWaiterNoKeyPressed(evt);
            }
        });

        txtWShortName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWShortNameMouseClicked(evt);
            }
        });
        txtWShortName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWShortNameKeyPressed(evt);
            }
        });

        lblWShortName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWShortName.setText("Short Name     :");

        lblWFullName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWFullName.setText("Full Name        :");

        txtWFullName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWFullNameMouseClicked(evt);
            }
        });
        txtWFullName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWFullNameKeyPressed(evt);
            }
        });

        lblWaiterNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWaiterNo.setText("Waiter No        :");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Waiter Master");
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
        btnNew.setToolTipText("Save Waiter");
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

        chkOperational.setText("jCheckBox1");
        chkOperational.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkOperationalKeyPressed(evt);
            }
        });

        lblOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperational.setText("Operational      :");

        lblSwipeCard.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSwipeCard.setText("Swipe Card      :");

        txtDebitCardString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDebitCardStringKeyPressed(evt);
            }
        });

        cmbPOS.setBackground(new java.awt.Color(51, 102, 255));
        cmbPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOSKeyPressed(evt);
            }
        });

        lblPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOS.setText("POS               :");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(0, 247, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblOperational, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblWFullName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblWaiterNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblWShortName, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtWaiterNo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtWShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtWFullName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(chkOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(153, 153, 153))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDebitCardString, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWaiterNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWaiterNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkOperational)
                    .addComponent(lblOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDebitCardString, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
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

    private void txtWaiterNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWaiterNoMouseClicked
        funResetField();
        funSelectWaiter();
    }//GEN-LAST:event_txtWaiterNoMouseClicked

    private void txtWShortNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWShortNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtWShortName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Waiters Short Name").setVisible(true);
                txtWShortName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtWShortName.getText(), "1", "Enter Waiters Short Name").setVisible(true);
                txtWShortName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtWShortNameMouseClicked

    private void txtWShortNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWShortNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtWFullName.requestFocus();
        }
    }//GEN-LAST:event_txtWShortNameKeyPressed

    private void txtWFullNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWFullNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtWFullName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Waiters Full Name").setVisible(true);
                txtWFullName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtWFullName.getText(), "1", "Enter Waiters Full Name").setVisible(true);
                txtWFullName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtWFullNameMouseClicked

    private void txtWFullNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWFullNameKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == 10)
        {
            chkOperational.requestFocus();
        }
    }//GEN-LAST:event_txtWFullNameKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
            dispose();
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
         clsGlobalVarClass.hmActiveForms.remove("Waiter Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        try
        {
            funResetField();

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveWaiter();
            }
            else
            {
                funUpdateWaiter();
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void txtWaiterNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWaiterNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectWaiter();
        }
        if (evt.getKeyCode() == 10)
        {
            txtWShortName.requestFocus();
        }
    }//GEN-LAST:event_txtWaiterNoKeyPressed

    private void txtWaiterNoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWaiterNoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWaiterNoMouseEntered

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveWaiter();
            }
            else
            {
                funUpdateWaiter();
            }
        }

    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveWaiter();
        }
        else
        {
            funUpdateWaiter();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Waiter Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void chkOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkOperationalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtDebitCardString.requestFocus();
        }
    }//GEN-LAST:event_chkOperationalKeyPressed

    private void txtDebitCardStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDebitCardStringKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtDebitCardStringKeyPressed

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbPOSKeyPressed
    {//GEN-HEADEREND:event_cmbPOSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPOSKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Waiter Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Waiter Master");
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(frmWaiterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmWaiterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmWaiterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmWaiterMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmWaiterMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkOperational;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperational;
    private javax.swing.JLabel lblPOS;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSwipeCard;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWFullName;
    private javax.swing.JLabel lblWShortName;
    private javax.swing.JLabel lblWaiterNo;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPasswordField txtDebitCardString;
    private javax.swing.JTextField txtWFullName;
    private javax.swing.JTextField txtWShortName;
    private javax.swing.JTextField txtWaiterNo;
    // End of variables declaration//GEN-END:variables
    private void funFillPOSCombo()
    {
        try
        {
            String sqlPOS = "select a.strPosCode,a.strPosName from tblposmaster a order by a.strPosCode";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);

            cmbPOS.removeAllItems();
            cmbPOS.addItem("All"+"                                  " +"All");
            while (rsPOS.next())
            {
                cmbPOS.addItem(rsPOS.getString(2) + "                                  " + rsPOS.getString(1));
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

}
