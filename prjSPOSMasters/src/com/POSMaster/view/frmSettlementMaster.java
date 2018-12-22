/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsSettelementOptions;
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
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmSettlementMaster extends javax.swing.JFrame
{

    private String[] settType =
    {
        "Cash", "Credit Card", "Debit Card", "Credit", "Coupon", "Complementary", "Gift Voucher", "Loyality Points", "Member", "Room", "JioMoney", "Online Payment", "Cheque","Benow"
    };
    private String code, strCode, settlementCode, sql, strBilling, strAdvReceipt;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmSettlementMaster
     */
    public frmSettlementMaster()
    {
        initComponents();
        try
        {
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            funSetShortCutKeys();
            txtSettelmentCode.requestFocus();

            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    tickTock();
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            cmbSettelmentType.removeAllItems();
            for (int k = 0; k < settType.length; k++)
            {
                cmbSettelmentType.addItem(settType[k]);
            }
            cmbApplicable.addItem("Yes");
            cmbApplicable.addItem("No");
            chlApplicableForBilling.setSelected(true);
	    chkSelectCustomerOnBillSettlement.setEnabled(false);
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
     * This method is used to set ticktock
     */
    private void tickTock()
    {
        Date date1 = new Date();
        String newstr = String.format("%tr", date1);
        String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
        lblDate.setText(dateAndTime);
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
            txtSettelmentCode.setText("");
            txtSettelmentName.setText("");
            cmbSettelmentType.removeAllItems();
            cmbSettelmentType.addItem(settType[0]);
            cmbSettelmentType.addItem(settType[1]);
            cmbSettelmentType.addItem(settType[2]);
            cmbSettelmentType.addItem(settType[3]);
            cmbSettelmentType.addItem(settType[4]);
            cmbSettelmentType.addItem(settType[5]);
            cmbApplicable.removeAllItems();
            cmbApplicable.addItem("Yes");
            cmbApplicable.addItem("No");
            chlApplicableForBilling.setSelected(true);
            chkApplicableForAdvOrderReceipt.setSelected(false);
            chkBillPrintOnSettlement.setSelected(false);
            txtCurrencyRate.setText("1.00");
            strBilling = "No";
            strAdvReceipt = "No";
            txtSettelmentCode.requestFocus();
            txtAccountCode.setText("");
            cmbComissionType.setSelectedIndex(0);
            txtThirdPartyComission.setText("0.00");
            cmbComissionOn.setSelectedIndex(0);
	    chkSelectCustomerOnBillSettlement.setSelected(false);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set settlement data
     *
     * @param data
     */
    private void funSetSettlementData(Object[] data)
    {
        try
        {
            sql = "select * from tblsettelmenthd where strSettelmentCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsSettlementInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsSettlementInfo.next();
            txtSettelmentCode.setText(rsSettlementInfo.getString(1));
            txtSettelmentName.setText(rsSettlementInfo.getString(2));
            cmbSettelmentType.removeAllItems();
            cmbSettelmentType.addItem(rsSettlementInfo.getString(3).toString());
            txtAccountCode.setText(rsSettlementInfo.getString(14));
            for (int cntSettlement = 0; cntSettlement < settType.length; cntSettlement++)
            {
                if (!rsSettlementInfo.getString(3).equalsIgnoreCase(settType[cntSettlement]))
                {
                    cmbSettelmentType.addItem(settType[cntSettlement]);
                }
            }
            cmbApplicable.removeAllItems();
            cmbApplicable.addItem(rsSettlementInfo.getString(4).toString());
            if (rsSettlementInfo.getString(4).equalsIgnoreCase("Yes"))
            {
                cmbApplicable.addItem("No");
            }
            else
            {
                cmbApplicable.addItem("Yes");
            }
            if (rsSettlementInfo.getString(5).equalsIgnoreCase("Yes"))
            {
                chlApplicableForBilling.setSelected(true);
            }
            else
            {
                chlApplicableForBilling.setSelected(false);
            }
            if (rsSettlementInfo.getString(6).equalsIgnoreCase("Yes"))
            {
                chkApplicableForAdvOrderReceipt.setSelected(true);
            }
            else
            {
                chkApplicableForAdvOrderReceipt.setSelected(false);
            }
            txtCurrencyRate.setText(rsSettlementInfo.getString(7));

            if (rsSettlementInfo.getString(15).equalsIgnoreCase("Y"))
            {
                chkBillPrintOnSettlement.setSelected(true);
            }
            else
            {
                chkBillPrintOnSettlement.setSelected(false);
            }

            if (rsSettlementInfo.getString(16).equalsIgnoreCase("Y"))
            {
                chkApplicableForCreditReceipts.setSelected(true);
            }
            else
            {
                chkApplicableForCreditReceipts.setSelected(false);
            }

            txtThirdPartyComission.setText(rsSettlementInfo.getString(17));
            cmbComissionType.setSelectedItem(rsSettlementInfo.getString(18));
            cmbComissionOn.setSelectedItem(rsSettlementInfo.getString(19));
	    if(cmbSettelmentType.getSelectedItem().toString().equalsIgnoreCase("Credit"))
	    {
		
		if (rsSettlementInfo.getString(20).equalsIgnoreCase("Y"))
		{
		    chkSelectCustomerOnBillSettlement.setSelected(true);
		}
		else
		{
		    chkSelectCustomerOnBillSettlement.setSelected(false);
		}
		chkSelectCustomerOnBillSettlement.setEnabled(true);
	    }
	    else
	    {
		
		if (rsSettlementInfo.getString(20).equalsIgnoreCase("Y"))
		{
		    chkSelectCustomerOnBillSettlement.setSelected(true);
		}
		else
		{
		    chkSelectCustomerOnBillSettlement.setSelected(false);
		}
		chkSelectCustomerOnBillSettlement.setEnabled(false);
	    }	
	    
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save settlement data
     */
    private void funSaveSettlementMode()
    {
        try
        {
            sql = "select count(*) from tblsettelmenthd";
            ResultSet rsSettlementCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsSettlementCode.next();
            int cn = rsSettlementCode.getInt(1);
            rsSettlementCode.close();
            if (cn > 0)
            {
                sql = "select max(strSettelmentCode) from tblsettelmenthd";
                rsSettlementCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsSettlementCode.next();
                code = rsSettlementCode.getString(1);
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
                    settlementCode = "S0" + intCode;
                }
                else
                {
                    settlementCode = "S" + intCode;
                }
            }
            else
            {
                code = "0";
                settlementCode = "S01";
            }
            if (!clsGlobalVarClass.validateEmpty(txtSettelmentName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Settlement Name", "Error", 0).setVisible(true);
                txtSettelmentName.requestFocus();
            }
            else if (clsGlobalVarClass.funCheckItemName("tblsettelmenthd", "strSettelmentDesc", "", txtSettelmentName.getText().trim(), "", "", ""))
            {
                new frmOkPopUp(this, "Settlement Name is Already Present", "Error", 0).setVisible(true);
                return;
            }
            else
            {
                txtSettelmentCode.setText(String.valueOf(settlementCode));
                if (chlApplicableForBilling.isSelected())
                {
                    strBilling = "Yes";
                }
                if (chkApplicableForAdvOrderReceipt.isSelected())
                {
                    strAdvReceipt = "Yes";
                }

                String billPrintOnSettlement = "N";
                if (chkBillPrintOnSettlement.isSelected())
                {
                    billPrintOnSettlement = "Y";
                }

                String applicableForCreditReceipt = "N";
                if (chkApplicableForCreditReceipts.isSelected())
                {
                    applicableForCreditReceipt = "Y";
                }
		
		String customerSelectionOnBillSettlement = "N";
                if (chkSelectCustomerOnBillSettlement.isSelected())
                {
                    customerSelectionOnBillSettlement = "Y";
                }
                String comissionType = cmbComissionType.getSelectedItem().toString();
                double comission = 0.00;
                try
                {
                    comission = Double.parseDouble(txtThirdPartyComission.getText().trim());
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                    new frmOkPopUp(this, "Please Enter Valid Comission", "Error", 0).setVisible(true);
                    return;
                }
                String comissionOn = cmbComissionOn.getSelectedItem().toString();

                sql = "insert into tblsettelmenthd(strSettelmentCode,strSettelmentDesc,strSettelmentType,strApplicable,"
                        + "strBilling,strAdvanceReceipt,dblConvertionRatio,strUserCreated,strUserEdited,dteDateCreated"
                        + ",dteDateEdited,strClientCode,strAccountCode,strBillPrintOnSettlement,strCreditReceiptYN"
                        + ",dblThirdPartyComission,strComissionType,strComissionOn,strCustomerSelectionOnBillSettlement )"
                        + "values('" + txtSettelmentCode.getText() + "','" + txtSettelmentName.getText().trim() + "','" + cmbSettelmentType.getSelectedItem().toString()
                        + "','" + cmbApplicable.getSelectedItem().toString() + "','" + strBilling + "','" + strAdvReceipt + "','"
                        + txtCurrencyRate.getText() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
                        + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "','" + txtAccountCode.getText() + "'"
                        + ",'" + billPrintOnSettlement + "','" + applicableForCreditReceipt + "'"
                        + ",'" + comission + "','" + comissionType + "','" + comissionOn + "','"+customerSelectionOnBillSettlement+"')";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {

                    clsSettelementOptions objSettlmentOptions = new clsSettelementOptions();
                    objSettlmentOptions.funAddSettelementOptions();
                    objSettlmentOptions = null;

                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Settlement' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                    if (clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
                    {

                    }
                    else
                    {
                        //clsGlobalVarClass.funInvokeHOWebserviceForMasters();
                    }
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
     * This method is used to update settlement mode
     */
    private void funUpdateSettlementMode()
    {
        try
        {
            if (chlApplicableForBilling.isSelected())
            {
                strBilling = "Yes";
            }
            if (chkApplicableForAdvOrderReceipt.isSelected())
            {
                strAdvReceipt = "Yes";
            }
            String billPrintOnSettlement = "N";
            if (chkBillPrintOnSettlement.isSelected())
            {
                billPrintOnSettlement = "Y";
            }
            String applicableForCreditReceipt = "N";
            if (chkApplicableForCreditReceipts.isSelected())
            {
                applicableForCreditReceipt = "Y";
            }
	    String customerSelectionOnBillSettlement = "N";
            if (chkSelectCustomerOnBillSettlement.isSelected())
            {
                customerSelectionOnBillSettlement = "Y";
            }
	    
            if (clsGlobalVarClass.funCheckItemName("tblsettelmenthd", "strSettelmentDesc", "strSettelmentCode", txtSettelmentName.getText().trim(), txtSettelmentCode.getText().trim(), "update", ""))
            {
                new frmOkPopUp(this, "Settlement Name is Already Present", "Error", 0).setVisible(true);
                txtSettelmentName.requestFocus();
                return;
            }

            String comissionType = cmbComissionType.getSelectedItem().toString();
            double comission = 0.00;
            try
            {
                comission = Double.parseDouble(txtThirdPartyComission.getText().trim());
            }
            catch (Exception e)
            {
                e.printStackTrace();

                new frmOkPopUp(this, "Please Enter Valid Comission", "Error", 0).setVisible(true);
                return;
            }
            String comissionOn = cmbComissionOn.getSelectedItem().toString();

            sql = "UPDATE tblsettelmenthd SET strSettelmentDesc = '" + txtSettelmentName.getText() + "',strSettelmentType = '"
                    + cmbSettelmentType.getSelectedItem().toString() + "',strApplicable = '" + cmbApplicable.getSelectedItem().toString()
                    + "',strBilling='" + strBilling + "',strAdvanceReceipt='" + strAdvReceipt + "',dblConvertionRatio='" + txtCurrencyRate.getText()
                    + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                    + ",strClientCode='" + clsGlobalVarClass.gClientCode + "',strAccountCode='" + txtAccountCode.getText() + "'"
                    + ",strBillPrintOnSettlement='" + billPrintOnSettlement + "'"
                    + ",strCreditReceiptYN='" + applicableForCreditReceipt + "' "
                    + ",dblThirdPartyComission='" + txtThirdPartyComission.getText().trim() + "',strComissionType='" + comissionType + "',strComissionOn='" + comissionOn + "' "
		    + ",strCustomerSelectionOnBillSettlement='"+customerSelectionOnBillSettlement+"'"
                    + " WHERE strSettelmentCode ='" + txtSettelmentCode.getText() + "'";

            int exc = clsGlobalVarClass.dbMysql.execute(sql);
            if (exc > 0)
            {
                clsSettelementOptions objSettlmentOptions = new clsSettelementOptions();
                objSettlmentOptions.funAddSettelementOptions();
                objSettlmentOptions = null;
                sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='Settlement' ";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
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
        tabSettleMaster = new javax.swing.JTabbedPane();
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblSettelmentCode = new javax.swing.JLabel();
        txtSettelmentCode = new javax.swing.JTextField();
        txtSettelmentName = new javax.swing.JTextField();
        lblSettelmentName = new javax.swing.JLabel();
        lblSettelmentType = new javax.swing.JLabel();
        cmbSettelmentType = new javax.swing.JComboBox();
        chkApplicableForAdvOrderReceipt = new javax.swing.JCheckBox();
        chlApplicableForBilling = new javax.swing.JCheckBox();
        lblApplicableFor = new javax.swing.JLabel();
        lblApplicable = new javax.swing.JLabel();
        cmbApplicable = new javax.swing.JComboBox();
        txtCurrencyRate = new javax.swing.JTextField();
        lblCurrencyRate = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        chkBillPrintOnSettlement = new javax.swing.JCheckBox();
        chkApplicableForCreditReceipts = new javax.swing.JCheckBox();
        lblThirdPartyComission = new javax.swing.JLabel();
        txtThirdPartyComission = new javax.swing.JTextField();
        cmbComissionType = new javax.swing.JComboBox();
        cmbComissionOn = new javax.swing.JComboBox();
        lblApplicable1 = new javax.swing.JLabel();
        chkSelectCustomerOnBillSettlement = new javax.swing.JCheckBox();
        panelLinkup = new javax.swing.JPanel();
        lblAccountCode = new javax.swing.JLabel();
        txtAccountCode = new javax.swing.JTextField();

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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Settlement Master");
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
        panelLayout.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 570));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Settlement Master");

        lblSettelmentCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSettelmentCode.setText("Settlement Code     :");

        txtSettelmentCode.setEditable(false);
        txtSettelmentCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSettelmentCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSettelmentCodeMouseClicked(evt);
            }
        });
        txtSettelmentCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSettelmentCodeKeyPressed(evt);
            }
        });

        txtSettelmentName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSettelmentName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSettelmentNameMouseClicked(evt);
            }
        });
        txtSettelmentName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSettelmentNameKeyPressed(evt);
            }
        });

        lblSettelmentName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSettelmentName.setText("Settlement Name    :");

        lblSettelmentType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSettelmentType.setText("Settlement Type     :");

        cmbSettelmentType.setBackground(new java.awt.Color(51, 102, 255));
        cmbSettelmentType.setMaximumRowCount(15);
        cmbSettelmentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Credit", "CreditCard", "Complementary", "DebitCard", "Cupon", "CRM Points", "Room", "JioMoney", "Online Payment", "Cheque", "Benow" }));
        cmbSettelmentType.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbSettelmentTypeItemStateChanged(evt);
            }
        });
        cmbSettelmentType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSettelmentTypeKeyPressed(evt);
            }
        });

        chkApplicableForAdvOrderReceipt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkApplicableForAdvOrderReceipt.setText("Advance Receipt");
        chkApplicableForAdvOrderReceipt.setOpaque(false);
        chkApplicableForAdvOrderReceipt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkApplicableForAdvOrderReceiptKeyPressed(evt);
            }
        });

        chlApplicableForBilling.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chlApplicableForBilling.setText("Billing");
        chlApplicableForBilling.setOpaque(false);
        chlApplicableForBilling.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chlApplicableForBillingKeyPressed(evt);
            }
        });

        lblApplicableFor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApplicableFor.setText("Applicable For           :");

        lblApplicable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApplicable.setText("Applicable                 :");

        cmbApplicable.setBackground(new java.awt.Color(51, 102, 255));
        cmbApplicable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbApplicable.setForeground(new java.awt.Color(255, 255, 255));
        cmbApplicable.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbApplicableKeyPressed(evt);
            }
        });

        txtCurrencyRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCurrencyRate.setText("1.00");
        txtCurrencyRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCurrencyRateMouseClicked(evt);
            }
        });
        txtCurrencyRate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCurrencyRateKeyPressed(evt);
            }
        });

        lblCurrencyRate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lblCurrencyRate.setText("Convertion Rate        :");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Settlement Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Settlement");
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

        chkBillPrintOnSettlement.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkBillPrintOnSettlement.setText("Bill Print On Settlement");
        chkBillPrintOnSettlement.setOpaque(false);
        chkBillPrintOnSettlement.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkBillPrintOnSettlementKeyPressed(evt);
            }
        });

        chkApplicableForCreditReceipts.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkApplicableForCreditReceipts.setText("Credit Receipt");
        chkApplicableForCreditReceipts.setOpaque(false);
        chkApplicableForCreditReceipts.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkApplicableForCreditReceiptsKeyPressed(evt);
            }
        });

        lblThirdPartyComission.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lblThirdPartyComission.setText("Third Party Comission :");

        txtThirdPartyComission.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtThirdPartyComission.setText("0.00");
        txtThirdPartyComission.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtThirdPartyComissionMouseClicked(evt);
            }
        });
        txtThirdPartyComission.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtThirdPartyComissionActionPerformed(evt);
            }
        });
        txtThirdPartyComission.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtThirdPartyComissionKeyPressed(evt);
            }
        });

        cmbComissionType.setBackground(new java.awt.Color(51, 102, 255));
        cmbComissionType.setMaximumRowCount(15);
        cmbComissionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Per", "Amt" }));
        cmbComissionType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbComissionTypeKeyPressed(evt);
            }
        });

        cmbComissionOn.setBackground(new java.awt.Color(51, 102, 255));
        cmbComissionOn.setMaximumRowCount(15);
        cmbComissionOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Net Amount", "Gross Amount", "No. Of PAX" }));
        cmbComissionOn.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbComissionOnKeyPressed(evt);
            }
        });

        lblApplicable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApplicable1.setText("On");
        lblApplicable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        chkSelectCustomerOnBillSettlement.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSelectCustomerOnBillSettlement.setText("Select Customer On Bill Settlement");
        chkSelectCustomerOnBillSettlement.setOpaque(false);
        chkSelectCustomerOnBillSettlement.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkSelectCustomerOnBillSettlementKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 105, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblSettelmentName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(txtSettelmentName, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblSettelmentCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(txtSettelmentCode, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(cmbSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(48, 48, 48))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblApplicableFor, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(143, 143, 143)
                                .addComponent(chkApplicableForAdvOrderReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chkApplicableForCreditReceipts, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblThirdPartyComission, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtThirdPartyComission, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbComissionType, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblApplicable1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbComissionOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chlApplicableForBilling)
                                    .addComponent(cmbApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCurrencyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCurrencyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(chkBillPrintOnSettlement)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkSelectCustomerOnBillSettlement)))
                .addGap(26, 26, 26))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSettelmentCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSettelmentCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSettelmentName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSettelmentName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSettelmentType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApplicableFor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chlApplicableForBilling, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkApplicableForAdvOrderReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkApplicableForCreditReceipts, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbApplicable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkBillPrintOnSettlement)
                        .addComponent(chkSelectCustomerOnBillSettlement)))
                .addGap(30, 30, 30)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCurrencyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCurrencyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblThirdPartyComission, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbComissionType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThirdPartyComission, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbComissionOn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApplicable1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        tabSettleMaster.addTab("General", panelBody);

        panelLinkup.setBackground(new java.awt.Color(255, 255, 255));
        panelLinkup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLinkup.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLinkup.setOpaque(false);

        lblAccountCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAccountCode.setText("Account Code           :");

        txtAccountCode.setEditable(false);
        txtAccountCode.setBackground(new java.awt.Color(204, 204, 204));
        txtAccountCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAccountCodeMouseClicked(evt);
            }
        });
        txtAccountCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtAccountCodeActionPerformed(evt);
            }
        });
        txtAccountCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAccountCodeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelLinkupLayout = new javax.swing.GroupLayout(panelLinkup);
        panelLinkup.setLayout(panelLinkupLayout);
        panelLinkupLayout.setHorizontalGroup(
            panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLinkupLayout.createSequentialGroup()
                .addContainerGap(247, Short.MAX_VALUE)
                .addComponent(lblAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(txtAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(209, 209, 209))
        );
        panelLinkupLayout.setVerticalGroup(
            panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLinkupLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAccountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(461, Short.MAX_VALUE))
        );

        tabSettleMaster.addTab("Linkup", panelLinkup);

        panelLayout.add(tabSettleMaster, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void SelectSettlementCode()
    {
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("Settlement");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");
            btnNew.setMnemonic('u');
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetSettlementData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
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
        System.out.println(accCode);
        txtAccountCode.setText(accCode);

    }

    //Fetch Account Code 
    private void funAccountCodeTextFieldClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsAccountDtl> accountInfo = objLinkSangERP.funGetAccountDtl("All", clsGlobalVarClass.gClientCode);
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

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Settlement");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField(); //clear all fields of the form
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
        {
            funSaveSettlementMode();
        }
        else
        {
            funUpdateSettlementMode();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
        {
            funSaveSettlementMode();
        }
        else
        {
            funUpdateSettlementMode();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE")) //Code for save new settlement type
            {
                funSaveSettlementMode();
            }
            else
            {
                funUpdateSettlementMode();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Settlement");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtSettelmentCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSettelmentCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            SelectSettlementCode();
        }
        if (evt.getKeyCode() == 10)
        {
            txtSettelmentName.requestFocus();
        }
    }//GEN-LAST:event_txtSettelmentCodeKeyPressed

    private void txtSettelmentCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSettelmentCodeMouseClicked

        SelectSettlementCode();
    }//GEN-LAST:event_txtSettelmentCodeMouseClicked

    private void txtSettelmentNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSettelmentNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbSettelmentType.requestFocus();
        }
    }//GEN-LAST:event_txtSettelmentNameKeyPressed

    private void txtSettelmentNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSettelmentNameMouseClicked
        // TODO add your handling code here:
        if (txtSettelmentName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Settlement Name").setVisible(true);
            txtSettelmentName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtSettelmentName.getText(), "1", "Enter Settlement Name").setVisible(true);
            txtSettelmentName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtSettelmentNameMouseClicked

    private void cmbSettelmentTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSettelmentTypeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            chlApplicableForBilling.requestFocus();
        }
    }//GEN-LAST:event_cmbSettelmentTypeKeyPressed

    private void chkApplicableForAdvOrderReceiptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkApplicableForAdvOrderReceiptKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbApplicable.requestFocus();
        }
    }//GEN-LAST:event_chkApplicableForAdvOrderReceiptKeyPressed

    private void chlApplicableForBillingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chlApplicableForBillingKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            chkApplicableForAdvOrderReceipt.requestFocus();
        }
    }//GEN-LAST:event_chlApplicableForBillingKeyPressed

    private void txtAccountCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeKeyPressed

    private void txtAccountCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountCodeActionPerformed

    private void txtAccountCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAccountCodeMouseClicked
        // TODO add your handling code here:

        funAccountCodeTextFieldClicked();
    }//GEN-LAST:event_txtAccountCodeMouseClicked

    private void cmbApplicableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbApplicableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtCurrencyRate.requestFocus();
        }
    }//GEN-LAST:event_cmbApplicableKeyPressed

    private void txtCurrencyRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCurrencyRateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_txtCurrencyRateKeyPressed

    private void chkBillPrintOnSettlementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkBillPrintOnSettlementKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkBillPrintOnSettlementKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Settlement");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Settlement");
    }//GEN-LAST:event_formWindowClosing

    private void txtCurrencyRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCurrencyRateMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtCurrencyRate.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, txtCurrencyRate.getText(), "Double", "Please Enter Sales Rate").setVisible(true);
                txtCurrencyRate.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this, true, txtCurrencyRate.getText(), "Double", "Please Enter Sales Rate").setVisible(true);
                txtCurrencyRate.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtCurrencyRateMouseClicked

    private void chkApplicableForCreditReceiptsKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_chkApplicableForCreditReceiptsKeyPressed
    {//GEN-HEADEREND:event_chkApplicableForCreditReceiptsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkApplicableForCreditReceiptsKeyPressed

    private void txtThirdPartyComissionMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtThirdPartyComissionMouseClicked
    {//GEN-HEADEREND:event_txtThirdPartyComissionMouseClicked
        try
        {
            if (txtThirdPartyComission.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, txtThirdPartyComission.getText(), "Double", "Please Enter Comission Rate").setVisible(true);
                txtThirdPartyComission.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }
            else
            {
                new frmNumericKeyboard(this, true, txtThirdPartyComission.getText(), "Double", "Please Enter Comission Rate").setVisible(true);
                txtThirdPartyComission.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtThirdPartyComissionMouseClicked

    private void txtThirdPartyComissionKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtThirdPartyComissionKeyPressed
    {//GEN-HEADEREND:event_txtThirdPartyComissionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThirdPartyComissionKeyPressed

    private void txtThirdPartyComissionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtThirdPartyComissionActionPerformed
    {//GEN-HEADEREND:event_txtThirdPartyComissionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThirdPartyComissionActionPerformed

    private void cmbComissionTypeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbComissionTypeKeyPressed
    {//GEN-HEADEREND:event_cmbComissionTypeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbComissionTypeKeyPressed

    private void cmbComissionOnKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbComissionOnKeyPressed
    {//GEN-HEADEREND:event_cmbComissionOnKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbComissionOnKeyPressed

    private void chkSelectCustomerOnBillSettlementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkSelectCustomerOnBillSettlementKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSelectCustomerOnBillSettlementKeyPressed

    private void cmbSettelmentTypeItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbSettelmentTypeItemStateChanged
    {//GEN-HEADEREND:event_cmbSettelmentTypeItemStateChanged
        // TODO add your handling code here:
	if(cmbSettelmentType.getSelectedItem()!=null)
	{    
	if(cmbSettelmentType.getSelectedItem().toString().equalsIgnoreCase("Credit"))
	{
	    chkSelectCustomerOnBillSettlement.setEnabled(true);
	} 
	else
	{
	    chkSelectCustomerOnBillSettlement.setEnabled(false);
	}    
	}
    }//GEN-LAST:event_cmbSettelmentTypeItemStateChanged

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
            java.util.logging.Logger.getLogger(frmSettlementMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmSettlementMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmSettlementMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmSettlementMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmSettlementMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkApplicableForAdvOrderReceipt;
    private javax.swing.JCheckBox chkApplicableForCreditReceipts;
    private javax.swing.JCheckBox chkBillPrintOnSettlement;
    private javax.swing.JCheckBox chkSelectCustomerOnBillSettlement;
    private javax.swing.JCheckBox chlApplicableForBilling;
    private javax.swing.JComboBox cmbApplicable;
    private javax.swing.JComboBox cmbComissionOn;
    private javax.swing.JComboBox cmbComissionType;
    private javax.swing.JComboBox cmbSettelmentType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAccountCode;
    private javax.swing.JLabel lblApplicable;
    private javax.swing.JLabel lblApplicable1;
    private javax.swing.JLabel lblApplicableFor;
    private javax.swing.JLabel lblCurrencyRate;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSettelmentCode;
    private javax.swing.JLabel lblSettelmentName;
    private javax.swing.JLabel lblSettelmentType;
    private javax.swing.JLabel lblThirdPartyComission;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelLinkup;
    private javax.swing.JTabbedPane tabSettleMaster;
    private javax.swing.JTextField txtAccountCode;
    private javax.swing.JTextField txtCurrencyRate;
    private javax.swing.JTextField txtSettelmentCode;
    private javax.swing.JTextField txtSettelmentName;
    private javax.swing.JTextField txtThirdPartyComission;
    // End of variables declaration//GEN-END:variables
}
