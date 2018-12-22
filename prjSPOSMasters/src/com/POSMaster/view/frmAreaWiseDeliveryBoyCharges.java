/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;

public class frmAreaWiseDeliveryBoyCharges extends javax.swing.JFrame {

    private String[] deliveryBoyCodeArray, deliveryBoyNameArray;
    private String selectQuery, insertQuery, deliveryBoyCode, custAreaCode;
    private String userCode, updateQuery, deliveryBoyName, sql;
    boolean flag;
    clsUtility objUtility = new clsUtility();
    
    public frmAreaWiseDeliveryBoyCharges() {
        initComponents();
        try {
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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

            userCode = clsGlobalVarClass.gUserCode;
            lblUserCode.setText(userCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funFillFormData();
            funSetShortCutKeys();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys() {
        btnClose.setMnemonic('c');
        btnSave.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    
    
    private void funResetField() {
        
        btnSave.setText("SAVE");
        btnSave.setMnemonic('s');
        flag = false;
        txtCustAreaCode.setText("");
        txtValue.setText("");
        custAreaCode = "";
    }
    
    
    private void funOperations() {
        try {
            if (btnSave.getText().equalsIgnoreCase("SAVE")) {

                clsUtility obj =new clsUtility();
                if (!clsGlobalVarClass.validateEmpty(txtCustAreaCode.getText())) {
                    new frmOkPopUp(this, "Please Select Cust Area ", "Error", 0).setVisible(true);
                    return;
                } else if (!obj.funCheckLength(txtValue.getText(), 30)) {
                    new frmOkPopUp(this, "Please Enter Value", "Error", 0).setVisible(true);
                    //   txtCustAreaName.requestFocus();
                    return;
                }
                deliveryBoyName = cmbDeliveryBoyName.getSelectedItem().toString();
                for (int i = 0; i < deliveryBoyCodeArray.length; i++) {
                    if (deliveryBoyName.equals(deliveryBoyNameArray[i])) {
                        deliveryBoyCode = deliveryBoyCodeArray[i];
                        break;
                    }
                }
                String sql = "select strCustAreaCode,strDeliveryBoyCode from tblareawisedelboywisecharges "
                    + " where strCustAreaCode='" + custAreaCode + "'"
                    + "and strDeliveryBoyCode='" + deliveryBoyCode + "';";
                ResultSet rsSubgroupName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsSubgroupName.next()) {
                    new frmOkPopUp(this, "Area Name Already Exist", "Successfull", 3).setVisible(true);
                    txtCustAreaCode.requestFocus();
                    return;
                } else {

                    insertQuery = "insert into tblareawisedelboywisecharges (strCustAreaCode,strDeliveryBoyCode,dblValue,"
                            + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag)"
                            + "values('" + custAreaCode + "','" + deliveryBoyCode + "','" + txtValue.getText() + "','"
                            + "" + userCode + "','" + userCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"
                            + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";
                    //System.out.println(insertQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
                    if (exc > 0) {
                        sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='AreaWiseDC' and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                }
            } else {

                if (!clsGlobalVarClass.validateEmpty(txtCustAreaCode.getText())) {
                    new frmOkPopUp(this, "Please Enter Cust Area Name", "Error", 0).setVisible(true);
                }
                deliveryBoyName = cmbDeliveryBoyName.getSelectedItem().toString();
                for (int i = 0; i < deliveryBoyCodeArray.length; i++) {
                    if (deliveryBoyName.equals(deliveryBoyNameArray[i])) {
                        deliveryBoyCode = deliveryBoyCodeArray[i];
                        break;
                    }
                }
                
                sql = "select strCustAreaCode,strDeliveryBoyCode from tblareawisedelboywisecharges "
                        + " where strCustAreaCode='" + custAreaCode + "' and strDeliveryBoyCode='" + deliveryBoyCode + "'";
                
                ResultSet rsSubUpadte = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsSubUpadte.next()) {
                    new frmOkPopUp(this, " Cust Area Code Already Exist", "Error", 0).setVisible(true);
                    txtCustAreaCode.requestFocus();
                    rsSubUpadte.close();
                } else {
                    updateQuery = "UPDATE tblareawisedelboywisecharges SET strDeliveryBoyCode = '" + deliveryBoyCode
                            + "',dblValue = '" + txtValue.getText() + "',strUserEdited='" + userCode + "',dteDateEdited='"
                            + clsGlobalVarClass.getCurrentDateTime() + "' WHERE strCustAreaCode ='" + custAreaCode + "'";
                    //System.out.println(updateQuery);
                    int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                    if (exc > 0) {
                        
                        sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='AreaWiseDC' and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                }
            }
            txtCustAreaCode.requestFocus();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry")) {
                new frmOkPopUp(this, "Area Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }
    
    
    
    /**
     * This method is used to set sub name
     *
     * @param text
     */
    private void funFillFormData() throws Exception
    {
        cmbDeliveryBoyName.removeAllItems();
        selectQuery = "select count(*) from tbldeliverypersonmaster";
        ResultSet rsDelBoy = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
        rsDelBoy.next();
        int rowCount = rsDelBoy.getInt(1);
        deliveryBoyCodeArray = new String[rowCount];
        deliveryBoyNameArray = new String[rowCount];
        selectQuery = "select strDPCode,strDPName from tbldeliverypersonmaster";
        rsDelBoy = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
        int i = 0;
        while (rsDelBoy.next()) {
            deliveryBoyNameArray[i] = rsDelBoy.getString(2);
            deliveryBoyCodeArray[i] = rsDelBoy.getString(1);
            cmbDeliveryBoyName.addItem(rsDelBoy.getString(2));
            i++;
        }
        rsDelBoy.close();
    }

    private void funSetData(Object[] data) throws Exception
    {
        custAreaCode = data[0].toString();
        sql = "select * from tblareawisedelboywisecharges where strCustAreaCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsCustAreaData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsCustAreaData.next())
        {
            txtValue.setText(rsCustAreaData.getString(3));
        }
        rsCustAreaData.close();

    //search Area Name Set
        sql = "select strBuildingName from tblbuildingmaster where strBuildingCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsCustAreaNameData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if(rsCustAreaNameData.next())
        {
            txtCustAreaCode.setText(rsCustAreaNameData.getString(1));
        }
        rsCustAreaNameData.close();

        selectQuery = "select strDPName from tbldeliverypersonmaster where strDPCode='" + data[2].toString() + "'";
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
        if(rs.next())
        {
            cmbDeliveryBoyName.setSelectedItem(rs.getString(1));
        }
        rs.close();

        DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();
        cmbModel.removeAllElements();
        cmbModel.addElement(rs.getString(1));
        for (int i = 0; i < deliveryBoyCodeArray.length; i++) {
            if (!data[2].toString().equals(String.valueOf(deliveryBoyCodeArray[i]))) {
                cmbModel.addElement(deliveryBoyNameArray[i]);
            }
        }
        cmbDeliveryBoyName.setModel(cmbModel);
        cmbDeliveryBoyName.requestFocus();
    }
    
    
    private void funSetBuildingData(Object[] data) throws Exception {
        funResetField();
        flag = true;
        custAreaCode = data[0].toString();
        txtCustAreaCode.setText(data[1].toString());
        cmbDeliveryBoyName.requestFocus();

    }

    private void funSearchBuildingMaster() {
        try {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("BuildingMaster");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked) {
                // btnSave.setText("UPDATE");//UpdateD
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetBuildingData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        } catch (Exception e) {
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

        pnlheader = new javax.swing.JPanel();
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
        pnlLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        pnlAreaWiseDeliveryBoyCharges = new javax.swing.JPanel();
        lblareaWiseDeliveryCharges = new javax.swing.JLabel();
        lblDeliveryBoyName = new javax.swing.JLabel();
        lblValue = new javax.swing.JLabel();
        txtCustAreaCode = new javax.swing.JTextField();
        txtValue = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblCustAreaCode = new javax.swing.JLabel();
        cmbDeliveryBoyName = new javax.swing.JComboBox();
        btnSelect = new javax.swing.JButton();

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

        pnlheader.setBackground(new java.awt.Color(69, 164, 238));
        pnlheader.setLayout(new javax.swing.BoxLayout(pnlheader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        pnlheader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        pnlheader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Area Wise Delivery Boy Charges");
        pnlheader.add(lblformName);
        pnlheader.add(filler4);
        pnlheader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        pnlheader.add(lblPosName);
        pnlheader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        pnlheader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        pnlheader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        pnlheader.add(lblHOSign);

        getContentPane().add(pnlheader, java.awt.BorderLayout.PAGE_START);

        pnlLayout.setLayout(new java.awt.GridBagLayout());

        pnlAreaWiseDeliveryBoyCharges.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        pnlAreaWiseDeliveryBoyCharges.setMinimumSize(new java.awt.Dimension(800, 570));
        pnlAreaWiseDeliveryBoyCharges.setOpaque(false);

        lblareaWiseDeliveryCharges.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblareaWiseDeliveryCharges.setText("Area Wise Delivery Boy Charges");

        lblDeliveryBoyName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryBoyName.setText("Delivery Boy Name         :");

        lblValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblValue.setText("Value                           :");

        txtCustAreaCode.setEditable(false);
        txtCustAreaCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustAreaCodeMouseClicked(evt);
            }
        });
        txtCustAreaCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustAreaCodeKeyPressed(evt);
            }
        });

        txtValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValueActionPerformed(evt);
            }
        });
        txtValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValueKeyPressed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
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

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblCustAreaCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAreaCode.setText("Customer Area Name           :");

        cmbDeliveryBoyName.setBackground(new java.awt.Color(51, 102, 255));
        cmbDeliveryBoyName.setForeground(new java.awt.Color(255, 255, 255));
        cmbDeliveryBoyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDeliveryBoyNameKeyPressed(evt);
            }
        });

        btnSelect.setBackground(new java.awt.Color(51, 102, 255));
        btnSelect.setText("...");
        btnSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelectMouseClicked(evt);
            }
        });
        btnSelect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSelectKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlAreaWiseDeliveryBoyChargesLayout = new javax.swing.GroupLayout(pnlAreaWiseDeliveryBoyCharges);
        pnlAreaWiseDeliveryBoyCharges.setLayout(pnlAreaWiseDeliveryBoyChargesLayout);
        pnlAreaWiseDeliveryBoyChargesLayout.setHorizontalGroup(
            pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAreaWiseDeliveryBoyChargesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createSequentialGroup()
                        .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCustAreaCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDeliveryBoyName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblValue, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDeliveryBoyName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCustAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelect))
                    .addComponent(lblareaWiseDeliveryCharges))
                .addContainerGap(136, Short.MAX_VALUE))
        );
        pnlAreaWiseDeliveryBoyChargesLayout.setVerticalGroup(
            pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblareaWiseDeliveryCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108)
                .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeliveryBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDeliveryBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                .addGroup(pnlAreaWiseDeliveryBoyChargesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        pnlLayout.add(pnlAreaWiseDeliveryBoyCharges, new java.awt.GridBagConstraints());

        getContentPane().add(pnlLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("AreaWiseDBoyCharges");
    }//GEN-LAST:event_btnCloseMouseClicked

    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("AreaWiseDBoyCharges");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        try {
            funResetField();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnResetMouseClicked

    
    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        funOperations();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void txtCustAreaCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustAreaCodeMouseClicked
        // TODO add your handling code here:

        try {

            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("AreaWiseDeliveryBoyCharges");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked) {
                btnSave.setText("UPDATE");
                btnSave.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtCustAreaCodeMouseClicked

    private void txtValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValueActionPerformed
    
    private void btnSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectMouseClicked
        // TODO add your handling code here:
        funSearchBuildingMaster();

    }//GEN-LAST:event_btnSelectMouseClicked

    private void txtCustAreaCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustAreaCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/') {

            funSearchBuildingMaster();
        }
        if(evt.getKeyCode()==10)
        {
            btnSelect.requestFocus();
        }
    }//GEN-LAST:event_txtCustAreaCodeKeyPressed

    private void cmbDeliveryBoyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDeliveryBoyNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            txtValue.requestFocus();
        }
    }//GEN-LAST:event_cmbDeliveryBoyNameKeyPressed

    private void txtValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValueKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            btnSave.requestFocus();
        }
    }//GEN-LAST:event_txtValueKeyPressed

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            funOperations();
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        funOperations();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSelectKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSelectKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/') {
            funSearchBuildingMaster();
        }
        if(evt.getKeyCode()==10)
        {
            cmbDeliveryBoyName.requestFocus();
        }
    }//GEN-LAST:event_btnSelectKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
          clsGlobalVarClass.hmActiveForms.remove("AreaWiseDBoyCharges");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
          clsGlobalVarClass.hmActiveForms.remove("AreaWiseDBoyCharges");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelect;
    private javax.swing.JComboBox cmbDeliveryBoyName;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCustAreaCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryBoyName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblValue;
    private javax.swing.JLabel lblareaWiseDeliveryCharges;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel pnlAreaWiseDeliveryBoyCharges;
    private javax.swing.JPanel pnlLayout;
    private javax.swing.JPanel pnlheader;
    private javax.swing.JTextField txtCustAreaCode;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
