/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsLinkupDtl;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;



public class frmSubGroupMaster extends javax.swing.JFrame
{

    private String sql;
    private Map<String, String> hmGroupMaster = new HashMap<String, String>();
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmSubGroupMaster
     */
    public frmSubGroupMaster()
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
                    String new_str = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
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
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            funFillGroupCombo();
            funFillFactoryCombo();
            txtSubGroupCode.requestFocus();
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
     * This method is used to fill sub group code
     */
    private void funFillGroupCombo() throws Exception
    {
        cmbGroupCode.removeAllItems();
        sql = "select strGroupCode,strGroupName from tblgrouphd";
        ResultSet rsGroupMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsGroupMaster.next())
        {
            hmGroupMaster.put(rsGroupMaster.getString(2), rsGroupMaster.getString(1));
            cmbGroupCode.addItem(rsGroupMaster.getString(2));
        }
        rsGroupMaster.close();
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
            sql = "select * from tblsubgrouphd where strSubGroupCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsSubGroupData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsSubGroupData.next())
            {
                txtSubGroupCode.setText(rsSubGroupData.getString(1));
                txtSubGroupName.setText(rsSubGroupData.getString(2));
                txtincentives.setText(rsSubGroupData.getString(10));
                txtAccountCode.setText(rsSubGroupData.getString(11));
                txtWSLocationCode.setText(rsSubGroupData.getString(12));
                sql = "select strGroupName from tblgrouphd where strGroupCode='" + data[2].toString() + "'";
                ResultSet rsGroupMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsGroupMaster.next())
                {
                    cmbGroupCode.setSelectedItem(rsGroupMaster.getString(1));
                }
                rsGroupMaster.close();
                funSetFactoryLinckedUp(clsGlobalVarClass.gSearchedItem);
            }
            rsSubGroupData.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set GL Code to GL Code field
     *
     * @param data
     */
    private void funSetAccountCode(Object[] data)
    {
        String accCode = (String) data[0];
        txtAccountCode.setText(accCode);
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');
        txtSubGroupCode.setText("");
        txtSubGroupName.setText("");
        txtincentives.setText("");
        txtSubGroupName.requestFocus();
        txtAccountCode.setText("");
        txtWSLocationCode.setText("");
        cmbFactoryLinckedUp.setSelectedItem("");

    }

    private void funSaveUpdateSubGroupRecord()
    {
        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveSubGroupRecord();
            }
            else
            {
                funUpdateSubGroupRecord();
            }
            txtSubGroupName.requestFocus();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry"))
            {
                new frmOkPopUp(this, "Sub Group Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }

    private void funSaveSubGroupRecord() throws Exception
    {
        String subGroupCode = "";
        sql = "select count(*) from tblsubgrouphd";
        ResultSet rsSubGroupMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsSubGroupMaster.next();
        int cntSGRecords = rsSubGroupMaster.getInt(1);
        rsSubGroupMaster.close();

        if (cntSGRecords > 0)
        {
            String sgCode = "";
            sql = "select max(strSubGroupCode) from tblsubgrouphd";
            rsSubGroupMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsSubGroupMaster.next();
            StringBuilder sb = new StringBuilder(rsSubGroupMaster.getString(1));
            String ss = sb.delete(0, 2).toString();
            for (int i = 0; i < ss.length(); i++)
            {
                if (ss.charAt(i) != '0')
                {
                    sgCode = ss.substring(i, ss.length());
                    break;
                }
            }
            int intCode = Integer.parseInt(sgCode);
            intCode++;
            if (intCode < 10)
            {
                subGroupCode = "SG000000" + intCode;
            }
            else if (intCode < 100)
            {
                subGroupCode = "SG00000" + intCode;
            }
            else if (intCode < 1000)
            {
                subGroupCode = "SG0000" + intCode;
            }
            else if (intCode < 10000)
            {
                subGroupCode = "SG000" + intCode;
            }
            else if (intCode < 100000)
            {
                subGroupCode = "SG00" + intCode;
            }
            else if (intCode < 1000000)
            {
                subGroupCode = "SG0" + intCode;
            }
        }
        else
        {
            subGroupCode = "SG0000001";
        }

        if (!clsGlobalVarClass.validateEmpty(txtSubGroupName.getText()))
        {
            new frmOkPopUp(this, "Please Enter Sub Group Name", "Error", 0).setVisible(true);
            return;
        }
        else if (!objUtility.funCheckLength(txtSubGroupName.getText(), 30))
        {
            new frmOkPopUp(this, "SubGroup Name length must be less than 30", "Error", 0).setVisible(true);
            txtSubGroupName.requestFocus();
            return;
        }
        String sql = "select strSubGroupName from tblsubgrouphd where strSubGroupName='" + txtSubGroupName.getText().trim() + "';";
        ResultSet rsSubgroupName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsSubgroupName.next())
        {
            new frmOkPopUp(this, "Sub Group Name Already Exist", "Successfull", 3).setVisible(true);
            txtSubGroupName.requestFocus();
            return;
        }
        else
        {
            String groupCode = hmGroupMaster.get(cmbGroupCode.getSelectedItem().toString());
            double incentive = 0.00;
            if (txtincentives.getText().equals(""))
            {
                incentive = 0.00;
            }
            else
            {
                incentive = Double.parseDouble(txtincentives.getText());
            }

            txtSubGroupCode.setText(subGroupCode);
            String factoryCode = funGetFactoryCode();
            sql = "insert into tblsubgrouphd (strSubGroupCode,strSubGroupName,strGroupCode,strIncentives,strUserCreated"
                    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strAccountCode,strFactoryCode)"
                    + "values('" + txtSubGroupCode.getText() + "','" + txtSubGroupName.getText() + "','" + groupCode + "'"
                    + ",'" + incentive + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','" + txtAccountCode.getText() + "','"+factoryCode+"' )";
            //System.out.println(insertQuery);
            int exc = clsGlobalVarClass.dbMysql.execute(sql);
            if (exc > 0)
            {
                sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                    + " where strTableName='SubGroup' ";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                funResetField();
            }
        }        
    }

    private void funUpdateSubGroupRecord() throws Exception
    {
        clsUtility objUtility = new clsUtility();
        if (!clsGlobalVarClass.validateEmpty(txtSubGroupName.getText()))
        {
            new frmOkPopUp(this, "Please Enter Sub Group Name", "Error", 0).setVisible(true);
        }
        else if (!objUtility.funCheckLength(txtSubGroupName.getText(), 30))
        {
            new frmOkPopUp(this, "SubGroup Name length must be less than 30", "Error", 0).setVisible(true);
            txtSubGroupName.requestFocus();
        }
        sql = "select strSubGroupName from tblsubgrouphd where strsubgroupname='" + txtSubGroupName.getText().trim() + "' and strsubgroupcode!='" + txtSubGroupCode.getText() + "'";

        System.out.println("sql" + sql);
        ResultSet rsSubUpadte = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsSubUpadte.next())
        {
            new frmOkPopUp(this, "Sub Group Name Already Exist", "Error", 0).setVisible(true);
            txtSubGroupName.requestFocus();
            rsSubUpadte.close();
        }
        else
        {

            String groupCode = hmGroupMaster.get(cmbGroupCode.getSelectedItem().toString());
            String factoryCode = funGetFactoryCode();
            
            sql = "UPDATE tblsubgrouphd SET strSubGroupName = '" + txtSubGroupName.getText() + "'"
                    + " ,strGroupCode = '" + groupCode + "',strIncentives = '" + txtincentives.getText() + "'"
                    + " ,strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + " ,strAccountCode='" + txtAccountCode.getText() + "',strFactoryCode='"+factoryCode+"' "
                    + " WHERE strSubGroupCode ='" + txtSubGroupCode.getText() + "'";
            //System.out.println(updateQuery);
            int exc = clsGlobalVarClass.dbMysql.execute(sql);
            if (exc > 0)
            {
                sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                    + " where strTableName='SubGroup' ";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                funResetField();
            }
        }
    }

    private void funSelectSubGroupCode()
    {
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("SubGroup");
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

    private void funAccountCodeTextFieldClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP=new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsAccountDtl> accountInfo = objLinkSangERP.funGetAccountDtl("GL Code", clsGlobalVarClass.gClientCode);
            new frmSearchFormDialog(this, true, accountInfo).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                String accCode = data[0].toString();
                System.out.println(accCode);
                funSetAccountCode(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            objLinkSangERP = null;
        }
    }

    //help for fetching property code
    private void funPropertyCodeTextFieldClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP=new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listLocation = objLinkSangERP.funGetLocationDtls(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Location Code");
            listColumns.add("Location Name");
            new frmSearchFormDialog(this, true, listLocation, "MMS Locations", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                txtWSLocationCode.setText(data[0].toString());
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            objLinkSangERP = null;
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
        tabSubGroupMaster = new javax.swing.JTabbedPane();
        panelbody = new javax.swing.JPanel();
        lbltitle = new javax.swing.JLabel();
        txtSubGroupCode = new javax.swing.JTextField();
        lblSubGroupCode = new javax.swing.JLabel();
        lblSubGroupName = new javax.swing.JLabel();
        txtSubGroupName = new javax.swing.JTextField();
        lblGroupCode = new javax.swing.JLabel();
        cmbGroupCode = new javax.swing.JComboBox();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblincentives = new javax.swing.JLabel();
        txtincentives = new javax.swing.JTextField();
        paneLinkup = new javax.swing.JPanel();
        lblAccountCode = new javax.swing.JLabel();
        txtAccountCode = new javax.swing.JTextField();
        lblWSLocationCode = new javax.swing.JLabel();
        txtWSLocationCode = new javax.swing.JTextField();
        lblFactoryLinckedUp = new javax.swing.JLabel();
        cmbFactoryLinckedUp = new javax.swing.JComboBox();

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
        lblformName.setText("- SubGroup Master");
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

        panelbody.setBackground(new java.awt.Color(255, 255, 255));
        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelbody.setOpaque(false);

        lbltitle.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lbltitle.setText("Sub Group Master");

        txtSubGroupCode.setEditable(false);
        txtSubGroupCode.setBackground(new java.awt.Color(204, 204, 204));
        txtSubGroupCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSubGroupCodeMouseClicked(evt);
            }
        });
        txtSubGroupCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubGroupCodeActionPerformed(evt);
            }
        });
        txtSubGroupCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubGroupCodeKeyPressed(evt);
            }
        });

        lblSubGroupCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubGroupCode.setText("Sub Group Code      :");

        lblSubGroupName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubGroupName.setText("Sub Group Name     :");

        txtSubGroupName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSubGroupNameMouseClicked(evt);
            }
        });
        txtSubGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubGroupNameActionPerformed(evt);
            }
        });
        txtSubGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubGroupNameKeyPressed(evt);
            }
        });

        lblGroupCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupCode.setText("Group Code            :");

        cmbGroupCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbGroupCode.setForeground(new java.awt.Color(255, 255, 255));
        cmbGroupCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbGroupCodeKeyPressed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Sub Group");
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
        btnCancel.setToolTipText("Close Sub Group Master");
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

        lblincentives.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblincentives.setText("Incentives              :");

        txtincentives.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtincentives.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtincentivesMouseClicked(evt);
            }
        });
        txtincentives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtincentivesActionPerformed(evt);
            }
        });
        txtincentives.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtincentivesKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(292, 292, 292)
                                .addComponent(lbltitle, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGap(222, 222, 222)
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblSubGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtSubGroupName))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblincentives, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblGroupCode, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtincentives)
                                            .addComponent(cmbGroupCode, 0, 184, Short.MAX_VALUE))))))
                        .addGap(0, 236, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lbltitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblincentives, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtincentives, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        tabSubGroupMaster.addTab("General", panelbody);

        paneLinkup.setBackground(new java.awt.Color(255, 255, 255));
        paneLinkup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        paneLinkup.setMinimumSize(new java.awt.Dimension(800, 570));
        paneLinkup.setOpaque(false);

        lblAccountCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAccountCode.setText("Account Code                 :");

        txtAccountCode.setEditable(false);
        txtAccountCode.setBackground(new java.awt.Color(204, 204, 204));
        txtAccountCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAccountCodeMouseClicked(evt);
            }
        });
        txtAccountCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountCodeActionPerformed(evt);
            }
        });
        txtAccountCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAccountCodeKeyPressed(evt);
            }
        });

        lblWSLocationCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWSLocationCode.setText("Web Stock Location Code :");

        txtWSLocationCode.setEditable(false);
        txtWSLocationCode.setBackground(new java.awt.Color(204, 204, 204));
        txtWSLocationCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWSLocationCodeMouseClicked(evt);
            }
        });
        txtWSLocationCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWSLocationCodeActionPerformed(evt);
            }
        });
        txtWSLocationCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWSLocationCodeKeyPressed(evt);
            }
        });

        lblFactoryLinckedUp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFactoryLinckedUp.setText("Factory                           :");

        cmbFactoryLinckedUp.setBackground(new java.awt.Color(51, 102, 255));
        cmbFactoryLinckedUp.setForeground(new java.awt.Color(255, 255, 255));
        cmbFactoryLinckedUp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbFactoryLinckedUpKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout paneLinkupLayout = new javax.swing.GroupLayout(paneLinkup);
        paneLinkup.setLayout(paneLinkupLayout);
        paneLinkupLayout.setHorizontalGroup(
            paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneLinkupLayout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addGroup(paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblWSLocationCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAccountCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFactoryLinckedUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtWSLocationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFactoryLinckedUp, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(227, Short.MAX_VALUE))
        );
        paneLinkupLayout.setVerticalGroup(
            paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneLinkupLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWSLocationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWSLocationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbFactoryLinckedUp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFactoryLinckedUp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(325, Short.MAX_VALUE))
        );

        tabSubGroupMaster.addTab("Linkup", paneLinkup);

        panelLayout.add(tabSubGroupMaster, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSubGroupCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSubGroupCodeMouseClicked
        funSelectSubGroupCode();
    }//GEN-LAST:event_txtSubGroupCodeMouseClicked

    private void txtSubGroupCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubGroupCodeActionPerformed

    }//GEN-LAST:event_txtSubGroupCodeActionPerformed

    private void txtSubGroupNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSubGroupNameMouseClicked
        if (txtSubGroupName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Sub Group Name").setVisible(true);
            txtSubGroupName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtSubGroupName.getText(), "1", "Enter Sub Group Name").setVisible(true);
            txtSubGroupName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtSubGroupNameMouseClicked

    private void txtSubGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubGroupNameActionPerformed

    }//GEN-LAST:event_txtSubGroupNameActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        funSaveUpdateSubGroupRecord();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("SubGroup");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtincentivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtincentivesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtincentivesActionPerformed

    private void txtSubGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubGroupCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectSubGroupCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtSubGroupName.requestFocus();
        }
    }//GEN-LAST:event_txtSubGroupCodeKeyPressed

    private void txtSubGroupNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubGroupNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbGroupCode.requestFocus();
        }
    }//GEN-LAST:event_txtSubGroupNameKeyPressed

    private void cmbGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGroupCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtincentives.requestFocus();
        }
    }//GEN-LAST:event_cmbGroupCodeKeyPressed

    private void txtincentivesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtincentivesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtincentivesKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveUpdateSubGroupRecord();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funSaveUpdateSubGroupRecord();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("SubGroup");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtAccountCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAccountCodeMouseClicked
        // TODO add your handling code here:
        funAccountCodeTextFieldClicked();
    }//GEN-LAST:event_txtAccountCodeMouseClicked

    private void txtAccountCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeActionPerformed

    private void txtAccountCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("SubGroup");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("SubGroup");
    }//GEN-LAST:event_formWindowClosing

    private void txtWSLocationCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWSLocationCodeMouseClicked
        // TODO add your handling code here:
        funPropertyCodeTextFieldClicked();
    }//GEN-LAST:event_txtWSLocationCodeMouseClicked

    private void txtWSLocationCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWSLocationCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWSLocationCodeActionPerformed

    private void txtWSLocationCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWSLocationCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWSLocationCodeKeyPressed

    private void cmbFactoryLinckedUpKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFactoryLinckedUpKeyPressed
    {//GEN-HEADEREND:event_cmbFactoryLinckedUpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFactoryLinckedUpKeyPressed

    private void txtincentivesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtincentivesMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtincentives.getText().length() == 0)
            {
               new frmNumericKeyboard(this,true,txtincentives.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
               txtincentives.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this,true,txtincentives.getText(),"Double" , "Please Enter Sales Rate").setVisible(true);
                txtincentives.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtincentivesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbFactoryLinckedUp;
    private javax.swing.JComboBox cmbGroupCode;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAccountCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFactoryLinckedUp;
    private javax.swing.JLabel lblGroupCode;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSubGroupCode;
    private javax.swing.JLabel lblSubGroupName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWSLocationCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblincentives;
    private javax.swing.JLabel lbltitle;
    private javax.swing.JPanel paneLinkup;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JTabbedPane tabSubGroupMaster;
    private javax.swing.JTextField txtAccountCode;
    private javax.swing.JTextField txtSubGroupCode;
    private javax.swing.JTextField txtSubGroupName;
    private javax.swing.JTextField txtWSLocationCode;
    private javax.swing.JTextField txtincentives;
    // End of variables declaration//GEN-END:variables

    private void funFillFactoryCombo()
    {
        try
        {
            String sqlBuilder = "select a.strFactoryCode,a.strFactoryName "
                    + "from tblfactorymaster a ";
            ResultSet rsFactoris = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder);
            cmbFactoryLinckedUp.removeAllItems();
            cmbFactoryLinckedUp.addItem("");
            while (rsFactoris.next())
            {
                cmbFactoryLinckedUp.addItem(rsFactoris.getString(2));
            }
            rsFactoris.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetFactoryLinckedUp(String subGroupCode)
    {
        try
        {
            String sqlBuilder = "select a.strFactoryCode,b.strFactoryName "
                    + "from  tblsubgrouphd a,tblfactorymaster b "
                    + "where a.strFactoryCode=b.strFactoryCode "
                    + "and strSubGroupCode='"+subGroupCode+"' ";
            ResultSet rsFactoris = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder);
            if (rsFactoris.next())
            {
                cmbFactoryLinckedUp.setSelectedItem(rsFactoris.getString(2));
            }
            else
            {
                cmbFactoryLinckedUp.setSelectedItem("");
            }
            rsFactoris.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private String funGetFactoryCode()
    {
        String factoryCode="";
        try
        {
            String sqlBuilder = "select a.strFactoryCode,a.strFactoryName "
                    + "from tblfactorymaster a "
                    + "where strFactoryName='"+cmbFactoryLinckedUp.getSelectedItem().toString()+"' ";
            ResultSet rsFactoris = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder);            
            if(rsFactoris.next())
            {
                factoryCode=rsFactoris.getString(1);
            }
            rsFactoris.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return factoryCode;
        }
    }
}
