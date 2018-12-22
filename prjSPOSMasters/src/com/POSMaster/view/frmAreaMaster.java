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
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author sss11
 */
public class frmAreaMaster extends javax.swing.JFrame
{

    private String sql;
    boolean flag;
    private String AreaCode;
    private Map<String, String> hmPOS;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This default constructor is used to initialized area master
     *
     */
    public frmAreaMaster()
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
            txtAreaName.requestFocus();
            funFillPOSCombo();
            funSetShortCutKeys();

            funSetMACAddress("");

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
     * This method is used to fill pos name combobox
     *
     * @throws Exception
     * @return
     */
    private void funFillPOSCombo() throws Exception
    {
        hmPOS = new HashMap<String, String>();
        cmbPOS.removeAllItems();

        hmPOS.put("All", "All");
        cmbPOS.addItem("All");
        String sqlPOS = "select strPOSCode,strPOSName from tblposmaster";
        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
        while (rsPOS.next())
        {
            hmPOS.put(rsPOS.getString(2), rsPOS.getString(1));
            cmbPOS.addItem(rsPOS.getString(2));
        }
        rsPOS.close();
    }

    /**
     * This method is used to set data to pos combobox
     *
     * @param data of type object
     * @return
     */
    private void setData(Object[] data)
    {
        try
        {
            sql = "select strAreaCode,strAreaName,strPOSCode,strMACAddress from tblareamaster where strAreaCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAreaInfo.next())
            {
                txtAreaCode.setText(rsAreaInfo.getString(1));
                txtAreaName.setText(rsAreaInfo.getString(2));
                for (Map.Entry<String, String> entry : hmPOS.entrySet())
                {
                    if (entry.getValue().equals(rsAreaInfo.getString(3)))
                    {
                        cmbPOS.setSelectedItem(entry.getKey());
                        break;
                    }
                }                                
            }
	    funSetMACAddress(rsAreaInfo.getString(4));
            rsAreaInfo.close();
            
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate area codes
     *
     * @return String areaCode
     */
    private String funGenerateAreaCode()
    {
        String areaCode = "";
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='Area'";
            ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsAreaCode.next();
            int areaCodeCnt = rsAreaCode.getInt(1);
            rsAreaCode.close();
            if (areaCodeCnt > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='Area'";
                rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsAreaCode.next();
                long code = rsAreaCode.getLong(1);
                code = code + 1;
                areaCode = "A" + String.format("%03d", code);
                AreaCode = areaCode;
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='Area'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                areaCode = "A001";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('Area'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return areaCode;
    }

    /**
     * This method is used to copy area wise item price
     *
     * @param areaCode
     * @throws Exception
     * @return int
     */
    private int funCopyItemPriceToArea(String areaCode) throws Exception
    {
        sql = "INSERT INTO tblmenuitempricingdtl (strItemCode, strItemName, strPosCode, strMenuCode,strPopular,\n"
                + "strPriceMonday,strPriceTuesday,strPriceWednesday,strPriceThursday,strPriceFriday,strPriceSaturday,strPriceSunday,\n"
                + " dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,tmeTimeTo,strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,\n"
                + " strUserEdited,dteDateCreated,dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing)\n"
                + "SELECT strItemCode, strItemName,strPosCode, strMenuCode,strPopular, strPriceMonday,strPriceTuesday,strPriceWednesday,\n"
                + "strPriceThursday,strPriceFriday,strPriceSaturday,strPriceSunday, dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,\n"
                + "tmeTimeTo,strAMPMTo,strCostCenterCode,strTextColor,strUserCreated, strUserEdited,dteDateCreated,dteDateEdited,\n"
                + "'" + AreaCode + "',strSubMenuHeadCode,strHourlyPricing FROM tblmenuitempricingdtl ";
        return clsGlobalVarClass.dbMysql.execute(sql);
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
            String posCode = hmPOS.get(cmbPOS.getSelectedItem().toString().trim());
            String name = txtAreaName.getText().trim();
            String code = txtAreaCode.getText().trim();

            if (clsGlobalVarClass.funCheckItemName("tblareamaster", "strAreaName", "strAreaCode", name, code, "save", ""))
            {
                new frmOkPopUp(null, "Area Name is Already Exist", "Error", 0).setVisible(true);
                txtAreaName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtAreaName.getText()))
            {
                new frmOkPopUp(null, "Please Enter Table Name", "Error", 0).setVisible(true);
            }
            else
            {
                txtAreaCode.setText(funGenerateAreaCode());

                sql = "insert into tblareamaster (strAreaCode,strAreaName,strUserCreated,strUserEdited"
                        + ",dteDateCreated,dteDateEdited,strClientCode,strPOSCode,strMACAddress)"
                        + "values('" + txtAreaCode.getText() + "','" + txtAreaName.getText() + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','" + posCode + "'"
                        + ",'"+txtMACAddress.getText().trim()+"')";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='Area' ";
                clsGlobalVarClass.dbMysql.execute(sql);

                if (exc > 0)
                {
                    //funCopyItemPriceToArea(txtAreaCode.getText());
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
            String posCode = hmPOS.get(cmbPOS.getSelectedItem().toString().trim());
            String name = txtAreaName.getText().trim();
            String code = txtAreaCode.getText().trim();

            if (clsGlobalVarClass.funCheckItemName("tblareamaster", "strAreaName", "strAreaCode", name, code, "update", ""))
            {
                new frmOkPopUp(null, "Area Name is Already Exist", "Error", 0).setVisible(true);
                txtAreaName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtAreaName.getText()))
            {
                new frmOkPopUp(null, "Please Enter Table Name", "Error", 0).setVisible(true);
            }
            else
            {
                sql = "UPDATE tblareamaster SET strAreaName = '" + txtAreaName.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',"
                        + "dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strPOSCode='" + posCode + "'"
                        + ",strMACAddress='"+txtMACAddress.getText().trim()+"' "
			+ ",strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                        + " WHERE strAreaCode ='" + txtAreaCode.getText() + "'";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Area' ";
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
     * This method is used to validate duplicate tables
     *
     * @throws Exception
     * @return boolean
     */
    private boolean funValidateDuplicateTable() throws Exception
    {
        boolean flgDupArea = true;
        sql = "select count(*) from tblareamaster where strAreaName='" + txtAreaName.getText() + "'";
        ResultSet rsDupArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsDupArea.next();
        if (rsDupArea.getInt(1) > 0)
        {
            flgDupArea = false;
        }
        return flgDupArea;
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
            txtAreaCode.setText("");
            txtAreaName.setText("");
            txtAreaName.requestFocus();
            funSetMACAddress("");
            funFillPOSCombo();

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
        };  
        ;
        panelBody = new javax.swing.JPanel();
        lblAreaCode = new javax.swing.JLabel();
        lblGroupName2 = new javax.swing.JLabel();
        txtAreaName = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        txtAreaCode = new javax.swing.JTextField();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        lblGroupName3 = new javax.swing.JLabel();
        cmbPOS = new javax.swing.JComboBox();
        lblMACAddress = new javax.swing.JLabel();
        txtMACAddress = new javax.swing.JTextField();

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
        lblProductName.setText("SPOS- ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Area Master");
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

        lblAreaCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAreaCode.setText("Area Code     :");

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Area Name    :");

        txtAreaName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAreaNameMouseClicked(evt);
            }
        });
        txtAreaName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAreaNameKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Area Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Area Master");
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

        txtAreaCode.setEnabled(false);
        txtAreaCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAreaCodeMouseClicked(evt);
            }
        });
        txtAreaCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAreaCodeKeyPressed(evt);
            }
        });

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Area Master");

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

        lblGroupName3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName3.setText("POS Name    :");

        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPOSKeyPressed(evt);
            }
        });

        lblMACAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMACAddress.setText("MAC Address :");

        txtMACAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMACAddressMouseClicked(evt);
            }
        });
        txtMACAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMACAddressKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblGroupName3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMACAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMACAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(lblAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(313, 313, 313))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAreaCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGroupName3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMACAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMACAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
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

    private void txtAreaNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtAreaName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter Table Name").setVisible(true);
                txtAreaName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtAreaName.getText(), "1", "Enter Table Name").setVisible(true);
                txtAreaName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtAreaNameMouseClicked

    private void txtAreaNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaNameKeyPressed
        // TODO add your handling code here:
        try
        {
            if (evt.getKeyCode() == 10)
            {
                if (txtAreaName.getText().length() > 8)
                {
                    JOptionPane.showMessageDialog(null, "Table Name should not be more than 8 characters");
                }
                else if (funValidateDuplicateTable())
                {
                    funSaveArea();
                }
                cmbPOS.requestFocus();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtAreaNameKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Area Master");
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
    public void SearchAreaCode()
    {
        flag = true;
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("AreaMaster");
        new frmSearchFormDialog(null, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");//UpdateD
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            setData(data);
        }
    }
    private void txtAreaCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaCodeMouseClicked
        // TODO add your handling code here:
        SearchAreaCode();

    }//GEN-LAST:event_txtAreaCodeMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void txtAreaCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            SearchAreaCode();
        }
    }//GEN-LAST:event_txtAreaCodeKeyPressed

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOSKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbPOSKeyPressed

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
        clsGlobalVarClass.hmActiveForms.remove("Area Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Area Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Area Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtMACAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtMACAddressMouseClicked
    {//GEN-HEADEREND:event_txtMACAddressMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMACAddressMouseClicked

    private void txtMACAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtMACAddressKeyPressed
    {//GEN-HEADEREND:event_txtMACAddressKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMACAddressKeyPressed

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
            java.util.logging.Logger.getLogger(frmAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmAreaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmAreaMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAreaCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblGroupName3;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMACAddress;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtAreaCode;
    private javax.swing.JTextField txtAreaName;
    private javax.swing.JTextField txtMACAddress;
    // End of variables declaration//GEN-END:variables

    private void funSetMACAddress(String strMacAdrr)
    {
        String physicalAddress="";
        try
        {
            clsUtility objUtility = new clsUtility();
            String hostName = objUtility.funGetHostName();
            physicalAddress = objUtility.funGetCurrentMACAddress();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
	    if(strMacAdrr.isEmpty()){
		 txtMACAddress.setText(physicalAddress);
	    }else{
		String strMacArr[]=strMacAdrr.split(",");
		for(String stroldArr:strMacArr){
		    if(stroldArr.equals(physicalAddress)){
			txtMACAddress.setText(strMacAdrr);
			break;
		    }else{
			txtMACAddress.setText(physicalAddress+","+strMacAdrr);
		    }
		     
		}
		 
	    }
           
        }
    }
}
