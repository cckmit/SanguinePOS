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
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.POSGlobal.controller.clsUtility;

public class frmDeliveryPersonMaster extends javax.swing.JFrame
{

    private ResultSet countSet;
    private String selectQuery, insertQuery;
    private String updateQuery;
    boolean flag;
    private ResultSet countSet1;
    private String code, sql;
    private String strCode;
    private String deliveryBoyCode;
    String DevlPersonCode, custAreaCodeForDelCharges;
    DefaultTableModel dmDeliverycharges;
    private HashMap<String, String> hm_deliveryBpayOut = null;
    private HashMap<String, String> hm_HelperpayOut = null;
    clsUtility obj=new clsUtility();

    /**
     * This method is used to initialize frmDeliveryPersonMaster
     */
    public frmDeliveryPersonMaster()
    {

        /*
         dmDeliverycharges = new javax.swing.table.DefaultTableModel(
         new Object[][]{},
         new String[]{
         "Area Code", "Area Name", "Pay Out", "Select"
         }
         ) {
         Class[] types = new Class[]{
         java.lang.String.class,java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
         };
         boolean[] canEdit = new boolean[]{
         false, false, true
         };

         public Class getColumnClass(int columnIndex) {
         return types[columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int columnIndex) {
         return canEdit[columnIndex];
         }
         };
        
         tblDelBoyIncentives = new JTable();
         tblDelBoyIncentives.setModel(dmDeliverycharges);
         */
        initComponents();
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

        dmDeliverycharges = (DefaultTableModel) tblDelBoyIncentives.getModel();
        txtDevlCode.requestFocus();
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        funSetShortCutKeys();

    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to select delivery person code
     */
    private void funSelectDeliveryPersonCode()
    {
        try
        {
            //new frmSearchForm(this,"frmDeliveryPersonMaster").setVisible(true);
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("DeliveryPersonMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnSave.setText("UPDATE");
                btnSave.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetFormData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
                txtDelvName.requestFocus();
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get delivery boy name
     */
    private void funDeliveryBoyName()
    {
        try
        {
            if (txtDelvName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Name ").setVisible(true);
                txtDelvName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtDelvName.getText(), "1", "Enter Name").setVisible(true);
                txtDelvName.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    /**
     * This method is used to save delivery person
     */
    private void funSaveDeliveryPerson()
    {
        try
        {
            char operational = 'N';
            if (cmbOperational.getSelectedIndex() == 0)
            {
                operational = 'Y';
            }
            selectQuery = "select count(*) from tbldeliverypersonmaster";
            countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            countSet1.next();
            int cn = countSet1.getInt(1);
            countSet1.close();
            if (cn > 0)
            {
                selectQuery = "select max(strDPCode) from tbldeliverypersonmaster";
                countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet.next();
                code = countSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 2).toString();
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
                //System.out.println(intCode+"\t"+strCode);
                if (intCode < 10)
                {
                    deliveryBoyCode = "DB00000" + intCode;
                }
                else if (intCode < 100)
                {
                    deliveryBoyCode = "DB0000" + intCode;
                }
                else if (intCode < 1000)
                {
                    deliveryBoyCode = "DB000" + intCode;
                }
                else if (intCode < 10000)
                {
                    deliveryBoyCode = "DB00" + intCode;
                }
                else if (intCode < 100000)
                {
                    deliveryBoyCode = "DB0" + intCode;
                }
                else if (intCode < 1000000)
                {
                    deliveryBoyCode = "DB" + intCode;
                }
            }
            else
            {
                deliveryBoyCode = "DB000001";
            }

            int exc = 0;

            if (!clsGlobalVarClass.validateEmpty(txtDelvName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Delivery Boy Name", "Error", 0).setVisible(true);
                txtDelvName.setText("");
                txtDelvName.requestFocus();
            }
            else if (!obj.funCheckLength(txtDelvName.getText(), 30))
            {
                new frmOkPopUp(this, "Delivery Boy Name length must be less than 30", "Error", 0).setVisible(true);
                txtDelvName.requestFocus();
            }
            else
            {
                insertQuery = "insert into tbldeliverypersonmaster "
                    + "(strDPCode,strDPName,strUserCreated,strUserEdited,dteDateCreated"
                    + ",dteDateEdited,strOperational,strClientCode)"
                    + " values('" + deliveryBoyCode + "','" + txtDelvName.getText() + "'"
                    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",'" + operational + "','" + clsGlobalVarClass.gClientCode + "')";
                exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
                funSaveDelBoyCharges();
                if (exc > 0)
                {
                    txtDevlCode.setText(deliveryBoyCode);
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='DeliveryBoy' ";
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
     * This method is used to update delivery person
     */
    private void funUpdateDeliveryPerson()
    {
        try
        {
            String operational = "N";
            if (cmbOperational.getSelectedIndex() == 0)
            {
                operational = "Y";
            }
            if (!clsGlobalVarClass.validateEmpty(txtDelvName.getText()))
            {
                new frmOkPopUp(null, "Please Enter Delivery Boy Name", "Error", 0).setVisible(true);
                txtDelvName.setText("");
                txtDelvName.requestFocus();
            }
            else if (!obj.funCheckLength(txtDelvName.getText(), 30))
            {
                new frmOkPopUp(this, "Delivery Boy Name length must be less than 30", "Error", 0).setVisible(true);
                txtDelvName.requestFocus();
            }
            else
            {
                updateQuery = "UPDATE tbldeliverypersonmaster SET strDPName = '" + txtDelvName.getText()
                    + "',strOperational='" + operational + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strClientCode='" + clsGlobalVarClass.gClientCode + "'"
                    + ",strDataPostFlag='N' "
                    + "WHERE strDPCode='" + txtDevlCode.getText() + "'";
                int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                String deleteDelBoyCharges = "delete from tblareawisedelboywisecharges where strDeliveryBoyCode='" + txtDevlCode.getText() + "'";
                int i = clsGlobalVarClass.dbMysql.execute(deleteDelBoyCharges);
                funUpdateDelBoyCharges();
                if (exc > 0)
                {
                    String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='DeliveryBoy' ";
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
     * This method is used to save or update
     */
    private void funSaveAndUpdate()
    {
        if (btnSave.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveDeliveryPerson();
        }
        else
        {
            funUpdateDeliveryPerson();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetFormData(Object[] data)
    {
        try
        {
            txtDevlCode.setText(data[0].toString());//by ritesh
            txtDelvName.setText(data[1].toString());
            String operational = data[2].toString();
            if ("YES".equalsIgnoreCase(operational))
            {
                cmbOperational.setSelectedIndex(0);
            }
            else
            {
                cmbOperational.setSelectedIndex(1);
            }
            funLoadDelBoyIncentiveTable();
            txtDelvName.requestFocus();
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
        btnSave.setText("SAVE");
        btnSave.setMnemonic('s');
        txtDelvName.setText("");
        txtDevlCode.setText("");
        cmbOperational.setSelectedIndex(0);
        txtDevlCode.requestFocus();
        dmDeliverycharges.setRowCount(0);
        btnRemove.setEnabled(false);
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
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        tabedPaneDelBoy = new javax.swing.JTabbedPane();
        tabDelBoy = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblDeliveryBoyCode = new javax.swing.JLabel();
        txtDevlCode = new javax.swing.JTextField();
        lblDeliveryBoyName = new javax.swing.JLabel();
        txtDelvName = new javax.swing.JTextField();
        lblOperation = new javax.swing.JLabel();
        cmbOperational = new javax.swing.JComboBox();
        panelTabDeliveryCharges = new javax.swing.JPanel();
        txtIncentives = new javax.swing.JTextField();
        btnReset1 = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnSave1 = new javax.swing.JButton();
        btnResetAll = new javax.swing.JButton();
        txtCustAreaName = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        lblBuildingName = new javax.swing.JLabel();
        lblIncentives = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDelBoyIncentives = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnDBPayOut = new javax.swing.JButton();
        btnHelperPayOut = new javax.swing.JButton();

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
        panelHeader.setForeground(new java.awt.Color(255, 255, 255));
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
        lblformName.setText("- Delivery Boy Master");
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
        panelLayout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLayout.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 570));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setToolTipText("Save Delivery Person Master");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSaveKeyPressed(evt);
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
        btnCancel.setToolTipText("Close Delivery Person Master");
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

        tabDelBoy.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Delivery Boy Master");

        lblDeliveryBoyCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryBoyCode.setText("Delivery Boy Code :");

        txtDevlCode.setEditable(false);
        txtDevlCode.setBackground(new java.awt.Color(204, 204, 204));
        txtDevlCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDevlCode.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtDevlCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDevlCodeMouseClicked(evt);
            }
        });
        txtDevlCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDevlCodeKeyPressed(evt);
            }
        });

        lblDeliveryBoyName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryBoyName.setText("Delivery Boy Name :");

        txtDelvName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDelvName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDelvNameMouseClicked(evt);
            }
        });
        txtDelvName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDelvNameKeyPressed(evt);
            }
        });

        lblOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperation.setText("Operational          :");

        cmbOperational.setBackground(new java.awt.Color(51, 102, 255));
        cmbOperational.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbOperational.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "YES", "NO" }));
        cmbOperational.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbOperationalKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout tabDelBoyLayout = new javax.swing.GroupLayout(tabDelBoy);
        tabDelBoy.setLayout(tabDelBoyLayout);
        tabDelBoyLayout.setHorizontalGroup(
            tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDelBoyLayout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabDelBoyLayout.createSequentialGroup()
                        .addComponent(lblDeliveryBoyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDevlCode, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabDelBoyLayout.createSequentialGroup()
                        .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDeliveryBoyName)
                            .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDelvName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(161, 161, 161))
        );
        tabDelBoyLayout.setVerticalGroup(
            tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabDelBoyLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeliveryBoyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDevlCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDeliveryBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDelvName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(tabDelBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOperational, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(171, Short.MAX_VALUE))
        );

        tabedPaneDelBoy.addTab("Delivery Boy", tabDelBoy);

        panelTabDeliveryCharges.setBackground(new java.awt.Color(255, 255, 255));
        panelTabDeliveryCharges.setOpaque(false);
        panelTabDeliveryCharges.setPreferredSize(new java.awt.Dimension(610, 600));
        panelTabDeliveryCharges.setLayout(null);

        txtIncentives.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIncentives.setText("0.00");
        txtIncentives.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtIncentivesMouseClicked(evt);
            }
        });
        txtIncentives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIncentivesActionPerformed(evt);
            }
        });
        txtIncentives.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIncentivesKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(txtIncentives);
        txtIncentives.setBounds(150, 70, 110, 30);

        btnReset1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset1.setText("Reset");
        btnReset1.setToolTipText("Reset All Fields");
        btnReset1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset1ActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnReset1);
        btnReset1.setBounds(400, 170, 90, 40);

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnClose);
        btnClose.setBounds(480, 510, 90, 30);

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Delivery Charges");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnAdd);
        btnAdd.setBounds(150, 170, 90, 40);

        btnSave1.setBackground(new java.awt.Color(255, 255, 255));
        btnSave1.setForeground(new java.awt.Color(255, 255, 255));
        btnSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave1.setText("Save");
        btnSave1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnSave1);
        btnSave1.setBounds(110, 510, 90, 30);

        btnResetAll.setBackground(new java.awt.Color(255, 255, 255));
        btnResetAll.setForeground(new java.awt.Color(255, 255, 255));
        btnResetAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetAll.setText("Reset");
        btnResetAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetAll.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnResetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetAllActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnResetAll);
        btnResetAll.setBounds(300, 510, 90, 30);

        txtCustAreaName.setEditable(false);
        txtCustAreaName.setBackground(new java.awt.Color(204, 204, 204));
        txtCustAreaName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCustAreaName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustAreaNameMouseClicked(evt);
            }
        });
        txtCustAreaName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustAreaNameActionPerformed(evt);
            }
        });
        txtCustAreaName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustAreaNameKeyPressed(evt);
            }
        });
        panelTabDeliveryCharges.add(txtCustAreaName);
        txtCustAreaName.setBounds(150, 20, 380, 30);

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setToolTipText(" Remove Delivery Charges");
        btnRemove.setEnabled(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnRemove);
        btnRemove.setBounds(270, 170, 100, 40);

        lblBuildingName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuildingName.setText("Area Code               :");
        panelTabDeliveryCharges.add(lblBuildingName);
        lblBuildingName.setBounds(10, 20, 130, 30);

        lblIncentives.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIncentives.setText("Pay Out                  :");
        panelTabDeliveryCharges.add(lblIncentives);
        lblIncentives.setBounds(10, 70, 130, 30);

        tblDelBoyIncentives.setRowHeight(30);
        tblDelBoyIncentives.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cust Area Code", "Cust Area Name", "Incentives", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDelBoyIncentives.setColumnSelectionAllowed(true);
        tblDelBoyIncentives.getTableHeader().setReorderingAllowed(false);
        tblDelBoyIncentives.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDelBoyIncentivesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDelBoyIncentives);
        tblDelBoyIncentives.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        panelTabDeliveryCharges.add(jScrollPane1);
        jScrollPane1.setBounds(0, 230, 520, 240);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Default Pay Outs For  :");
        panelTabDeliveryCharges.add(jLabel1);
        jLabel1.setBounds(10, 120, 140, 20);

        btnDBPayOut.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDBPayOut.setForeground(new java.awt.Color(255, 255, 255));
        btnDBPayOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgModBtn.png"))); // NOI18N
        btnDBPayOut.setText("Delivery Boy");
        btnDBPayOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDBPayOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDBPayOutMouseClicked(evt);
            }
        });
        btnDBPayOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDBPayOutActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnDBPayOut);
        btnDBPayOut.setBounds(150, 110, 110, 40);

        btnHelperPayOut.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHelperPayOut.setForeground(new java.awt.Color(255, 255, 255));
        btnHelperPayOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnHelperPayOut.setText("Helper");
        btnHelperPayOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHelperPayOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHelperPayOutMouseClicked(evt);
            }
        });
        btnHelperPayOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelperPayOutActionPerformed(evt);
            }
        });
        panelTabDeliveryCharges.add(btnHelperPayOut);
        btnHelperPayOut.setBounds(290, 110, 100, 40);

        tabedPaneDelBoy.addTab("Delivery Charges", panelTabDeliveryCharges);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(460, 460, 460)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabedPaneDelBoy, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(tabedPaneDelBoy, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDevlCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDevlCodeMouseClicked
        // TODO add your handling code here:
        funSelectDeliveryPersonCode();
    }//GEN-LAST:event_txtDevlCodeMouseClicked

    private void txtDelvNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDelvNameMouseClicked
        // TODO add your handling code here:
        funDeliveryBoyName();
    }//GEN-LAST:event_txtDelvNameMouseClicked

    private void txtDelvNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDelvNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbOperational.requestFocus();
        }
    }//GEN-LAST:event_txtDelvNameKeyPressed

    private void cmbOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSave.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalKeyPressed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        funSaveAndUpdate();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSaveAndUpdate();
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Home Delivery Person");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtIncentivesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIncentivesMouseClicked
        // TODO add your handling code here:
        if (txtIncentives.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double" ,"Enter Delivery Charges").setVisible(true);
            txtIncentives.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,txtIncentives.getText(),"Double" , "Enter Delivery Charges").setVisible(true);
            txtIncentives.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtIncentivesMouseClicked

    private void txtIncentivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIncentivesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIncentivesActionPerformed

    private void txtIncentivesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIncentivesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (!obj.funCheckDouble(txtCustAreaName.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Delivery Charges Bill Amount1");
                return;
            }

            if (!obj.funCheckDouble(txtIncentives.getText()))
            {
                JOptionPane.showMessageDialog(this, "Invaild Input Delivery charges");
                return;
            }
            if (Double.parseDouble(txtCustAreaName.getText().trim()) < 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in BillAmount1");
                txtCustAreaName.requestFocus();
                return;
            }

            if (Double.parseDouble(txtIncentives.getText().trim()) < 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Amount Greater than 0 in Delivery Charges");
                txtIncentives.requestFocus();
                return;
            }

            funDeliveryChargeAddRow("Single");

            txtCustAreaName.setText("");
            txtIncentives.setText("");
            txtCustAreaName.requestFocus();
            return;
        }
    }//GEN-LAST:event_txtIncentivesKeyPressed

    private void btnReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset1ActionPerformed
        funResetDC();
    }//GEN-LAST:event_btnReset1ActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked

        if (txtCustAreaName.getText().isEmpty() || txtCustAreaName.getText().length() < 1)
        {
            JOptionPane.showMessageDialog(this, "Please Select Building Name");
            return;
        }

        if (!obj.funCheckDouble(txtIncentives.getText()))
        {
            JOptionPane.showMessageDialog(this, "Invaild Input Incentives");
            return;
        }

        funDeliveryChargeAddRow("Single");

        txtCustAreaName.setText("");
        txtIncentives.setText("0.00");
        txtCustAreaName.requestFocus();
        btnDBPayOut.setEnabled(true);
        btnHelperPayOut.setEnabled(true);
        return;
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSave1ActionPerformed

    private void btnResetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetAllActionPerformed

    private void txtCustAreaNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustAreaNameMouseClicked

        funSelectBuildingName();
    }//GEN-LAST:event_txtCustAreaNameMouseClicked

    private void txtCustAreaNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustAreaNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustAreaNameActionPerformed

    private void txtCustAreaNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustAreaNameKeyPressed
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectBuildingName();
        }
    }//GEN-LAST:event_txtCustAreaNameKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        funRemoveRow();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void tblDelBoyIncentivesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDelBoyIncentivesMouseClicked

        funCheckSelection();
    }//GEN-LAST:event_tblDelBoyIncentivesMouseClicked

    private void btnHelperPayOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelperPayOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHelperPayOutActionPerformed

    private void btnDBPayOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDBPayOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDBPayOutActionPerformed

    private void btnDBPayOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDBPayOutMouseClicked
        // TODO add your handling code here:

        hm_deliveryBpayOut = new HashMap<String, String>();
        try
        {
            sql = "select strBuildingCode,strBuildingName,dblDeliveryBoyPayOut "
                  + " from tblbuildingmaster "
                  + " where dblDeliveryBoyPayOut > 0;";
            ResultSet rsDbPayOut = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsDbPayOut.next())
            {
                hm_deliveryBpayOut.put(rsDbPayOut.getString(1), rsDbPayOut.getString(2) + "!" + rsDbPayOut.getString(3));
            }
            rsDbPayOut.close();
            funDeliveryChargeAddRow("Del Boy");

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDBPayOutMouseClicked

    private void btnHelperPayOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelperPayOutMouseClicked
        // TODO add your handling code here:
        hm_HelperpayOut = new HashMap<String, String>();
        try
        {
            sql = "select strBuildingCode,strBuildingName,dblHelperPayOut "
                  + " from tblbuildingmaster "
                  + " where dblHelperPayOut >0;";
            ResultSet rsHPayOut = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsHPayOut.next())
            {
                hm_HelperpayOut.put(rsHPayOut.getString(1), rsHPayOut.getString(2) + "!" + rsHPayOut.getString(3));
            }
            rsHPayOut.close();
            funDeliveryChargeAddRow("Helper");
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHelperPayOutMouseClicked

    private void txtDevlCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDevlCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectDeliveryPersonCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtDelvName.requestFocus();
        }
    }//GEN-LAST:event_txtDevlCodeKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        funSaveAndUpdate();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Home Delivery Person");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
           clsGlobalVarClass.hmActiveForms.remove("Home Delivery Person");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
           clsGlobalVarClass.hmActiveForms.remove("Home Delivery Person");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDBPayOut;
    private javax.swing.JButton btnHelperPayOut;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnResetAll;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSave1;
    private javax.swing.JComboBox cmbOperational;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuildingName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryBoyCode;
    private javax.swing.JLabel lblDeliveryBoyName;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblIncentives;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperation;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelTabDeliveryCharges;
    private javax.swing.JPanel tabDelBoy;
    private javax.swing.JTabbedPane tabedPaneDelBoy;
    private javax.swing.JTable tblDelBoyIncentives;
    private javax.swing.JTextField txtCustAreaName;
    private javax.swing.JTextField txtDelvName;
    private javax.swing.JTextField txtDevlCode;
    private javax.swing.JTextField txtIncentives;
    // End of variables declaration//GEN-END:variables

    private void funSelectBuildingName()
    {

        try
        {
           clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("BuildingMaster");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                custAreaCodeForDelCharges = data[0].toString();
                txtCustAreaName.setText(data[1].toString());
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDeliveryChargeAddRow(String recordType)
    {
        try
        {
            if (recordType.equalsIgnoreCase("Del Boy"))
            {
                Iterator itdeliveryBpayOut = hm_deliveryBpayOut.keySet().iterator();
                Object[] column = new Object[hm_deliveryBpayOut.size()];
                dmDeliverycharges.setRowCount(0);
                while (itdeliveryBpayOut.hasNext())
                {
                    String key = itdeliveryBpayOut.next().toString();
                    Object row[] = new Object[]
                    {
                        key, hm_deliveryBpayOut.get(key).split("!")[0], hm_deliveryBpayOut.get(key).split("!")[1]
                    };
                    dmDeliverycharges.addRow(row);
                }
                tblDelBoyIncentives.setModel(dmDeliverycharges);
            }
            else if (recordType.equalsIgnoreCase("Helper"))
            {
                Iterator ithelperBpayOut = hm_HelperpayOut.keySet().iterator();
                Object[] column = new Object[hm_HelperpayOut.size()];
                dmDeliverycharges.setRowCount(0);
                while (ithelperBpayOut.hasNext())
                {
                    String key = ithelperBpayOut.next().toString();
                    Object row[] = new Object[]
                    {
                        key, hm_HelperpayOut.get(key).split("!")[0], hm_HelperpayOut.get(key).split("!")[1]
                    };
                    dmDeliverycharges.addRow(row);
                }
                tblDelBoyIncentives.setModel(dmDeliverycharges);
            }
            else
            {
                Object row[] = new Object[]
                {
                    custAreaCodeForDelCharges, txtCustAreaName.getText().trim(), txtIncentives.getText().trim()
                };
                dmDeliverycharges.addRow(row);
            }

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblDelBoyIncentives.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblDelBoyIncentives.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            tblDelBoyIncentives.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblDelBoyIncentives.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            tblDelBoyIncentives.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDelBoyIncentives.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblDelBoyIncentives.getColumnModel().getColumn(2).setPreferredWidth(30);
            tblDelBoyIncentives.getColumnModel().getColumn(3).setPreferredWidth(30);

            tblDelBoyIncentives.setSize(700, 900);

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funCheckSelection()
    {

        /*
         for (int i = 0; i < tblDelBoyIncentives.getRowCount(); i++) 
         {
         if (tblDelBoyIncentives.getValueAt(i, 3).equals(true)) {
         btnRemove.setEnabled(true);
         break;
         } else {
         btnRemove.setEnabled(false);
         }
         }*/
        int rowNo = tblDelBoyIncentives.getSelectedRow();
        String rowValue = tblDelBoyIncentives.getValueAt(rowNo, 3).toString();
        if (Boolean.parseBoolean(rowValue))
        {
            btnRemove.setEnabled(true);
        }

        boolean flgSelect = false;
        for (int i = 0; i < tblDelBoyIncentives.getRowCount(); i++)
        {
            String rowValue1 = tblDelBoyIncentives.getValueAt(i, 3).toString();
            if (Boolean.parseBoolean(rowValue1))
            {
                flgSelect = true;
                break;
            }
        }
        if (!flgSelect)
        {
            btnRemove.setEnabled(false);
        }
    }

    private void funResetDC()
    {

        txtCustAreaName.setText("");
        txtIncentives.setText("0.00");
    }

    private void funRemoveRow()
    {

        int rowNo = tblDelBoyIncentives.getRowCount();
        java.util.Vector vIndexToDelete = new java.util.Vector();
        for (int i = 0; i < rowNo; i++)
        {
            boolean select = Boolean.parseBoolean(tblDelBoyIncentives.getValueAt(i, 3).toString());
            if (select)
            {
                vIndexToDelete.add(i);
                //dmDeliverycharges.removeRow(i);
            }
        }
        int cnt = 0;
        while (cnt < tblDelBoyIncentives.getRowCount())
        {
            boolean select = Boolean.parseBoolean(tblDelBoyIncentives.getValueAt(cnt, 3).toString());
            if (select)
            {
                if (tblDelBoyIncentives.getRowCount() > 0)
                {
                    dmDeliverycharges.removeRow(cnt);
                }
            }
            else
            {
                cnt++;
            }
        }
    }

    
    private void funSaveDelBoyCharges()
    {
        try
        {
            if (tblDelBoyIncentives.getRowCount() > 0)
            {
                String delChargeSql = "INSERT INTO tblareawisedelboywisecharges (strCustAreaCode, strDeliveryBoyCode, dblValue, strUserCreated, strUserEdited, dteDateCreated, dteDateEdited, strClientCode) VALUES ";
                for (int row = 0; row < tblDelBoyIncentives.getRowCount(); row++)
                {
                    if (row == 0)
                    {
                        delChargeSql += "('" + tblDelBoyIncentives.getValueAt(row, 0) + "', '" + deliveryBoyCode + "','" + tblDelBoyIncentives.getValueAt(row, 2) + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "')";
                    }
                    else
                    {
                        delChargeSql += ",('" + tblDelBoyIncentives.getValueAt(row, 0) + "', '" + deliveryBoyCode + "','" + tblDelBoyIncentives.getValueAt(row, 2) + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "')";
                    }
                }
                clsGlobalVarClass.dbMysql.execute(delChargeSql);
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    
    
    private void funUpdateDelBoyCharges()
    {
        try
        {
            if (tblDelBoyIncentives.getRowCount() > 0)
            {
                String delChargeSql = "INSERT INTO tblareawisedelboywisecharges (strCustAreaCode, strDeliveryBoyCode, dblValue, strUserCreated, strUserEdited, dteDateCreated, dteDateEdited, strClientCode) VALUES ";
                for (int row = 0; row < tblDelBoyIncentives.getRowCount(); row++)
                {
                    if (row == 0)
                    {
                        delChargeSql += "('" + tblDelBoyIncentives.getValueAt(row, 0) + "', '" + txtDevlCode.getText() + "','" + tblDelBoyIncentives.getValueAt(row, 2) + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "')";
                    }
                    else
                    {
                        delChargeSql += ",('" + tblDelBoyIncentives.getValueAt(row, 0) + "', '" + txtDevlCode.getText() + "','" + tblDelBoyIncentives.getValueAt(row, 2) + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "')";
                    }
                }
                clsGlobalVarClass.dbMysql.execute(delChargeSql);
            }
        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    
    
    private void funLoadDelBoyIncentiveTable()
    {
        try
        {
            String sql = "select b.strBuildingCode,b.strBuildingName ,a.dblValue "
                + "from tblareawisedelboywisecharges a left outer join tblbuildingmaster b on a.strCustAreaCode=b.strBuildingCode "
                + "where a.strDeliveryBoyCode='" + txtDevlCode.getText() + "' ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            dmDeliverycharges.setRowCount(0);
            while (rs.next())
            {
                Object[] column = new Object[4];
                column[0] = rs.getString("strBuildingCode");
                column[1] = rs.getString("strBuildingName");
                column[2] = rs.getString("dblValue");
                column[3] = false;
                dmDeliverycharges.addRow(column);
            }
            rs.close();
            tblDelBoyIncentives.setModel(dmDeliverycharges);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblDelBoyIncentives.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblDelBoyIncentives.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblDelBoyIncentives.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

            tblDelBoyIncentives.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblDelBoyIncentives.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblDelBoyIncentives.getColumnModel().getColumn(2).setPreferredWidth(100);

            tblDelBoyIncentives.setSize(700, 900);

        }
        catch (Exception e)
        {
            obj.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
}
