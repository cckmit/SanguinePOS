/*
    @Author Vinayak Padalkar

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
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmDiscountMaster extends javax.swing.JFrame
{

    private String selectQuery;
    private String strCode, code, sql;
    boolean flag;
    private clsUtility objUtility;
    private HashMap<String, String> mapPOSCode;
    private HashMap<String, String> mapPOSName;

    /**
     * This method is used to initialize CostCenterMaster
     */
    public frmDiscountMaster()
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
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
	    txtDiscountCode.requestFocus();
	    dteFromDate.setDate(objUtility.funGetDateToSetCalenderDate());
	    dteToDate.setDate(objUtility.funGetDateToSetCalenderDate());
	    funLoadPOSCombo();

	    funSetShortCutKeys();
	    funDiscOnComboClicked();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funSetShortCutKeys()
    {
	btnCancel.setMnemonic('c');
	btnNew.setMnemonic('s');
	btnReset.setMnemonic('r');

    }

    private void funLoadPOSCombo()
    {
	try
	{
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
	    mapPOSCode = new HashMap<String, String>();
	    mapPOSName = new HashMap<String, String>();
	    cmbPosCode.addItem("All");
	    mapPOSCode.put("All", "All");
	    mapPOSName.put("All", "All");
	    while (rsPOS.next())
	    {
		cmbPosCode.addItem(rsPOS.getString(2));
		mapPOSCode.put(rsPOS.getString(1), rsPOS.getString(2));
		mapPOSName.put(rsPOS.getString(2), rsPOS.getString(1));
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetDiscountData(Object[] data) throws Exception
    {
	sql = "select * from tbldischd a "
		+ " where  a.strDiscCode='" + data[0].toString() + "'";
	ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsDisc.next())
	{
	    txtDiscountCode.setText(rsDisc.getString(1));//code
	    txtDiscountName.setText(rsDisc.getString(2));

	    btnNew.setText("UPDATE");//updated
	    btnNew.setMnemonic('u');

	    cmbPosCode.setSelectedItem(mapPOSCode.get(rsDisc.getString(3)));

	    String discOn = rsDisc.getString(5);
	    cmbDiscountOn.setSelectedItem(discOn);
	    cmbDiscountOn.setEnabled(false);
	    if (discOn.equalsIgnoreCase("All"))
	    {
		txtDiscountOnCode.setText("All");
		lblDiscOnValue.setText("All");
	    }
	    else
	    {
		txtDiscountOnCode.setText("");
		lblDiscOnValue.setText("");
	    }

	    txtDiscountValue.setText("0.00");
	    dteFromDate.setDate(rsDisc.getDate(6));
	    dteToDate.setDate(rsDisc.getDate(7));

	    //operation type  for discount on
	    String dineIn = rsDisc.getString(13);
	    if (dineIn.equalsIgnoreCase("Y"))
	    {
		chkDineIn.setSelected(true);
	    }
	    else
	    {
		chkDineIn.setSelected(false);
	    }

	    String homeDelivery = rsDisc.getString(14);
	    if (homeDelivery.equalsIgnoreCase("Y"))
	    {
		chkHomeDelivery.setSelected(true);
	    }
	    else
	    {
		chkHomeDelivery.setSelected(false);
	    }

	    String takeAway = rsDisc.getString(15);
	    if (takeAway.equalsIgnoreCase("Y"))
	    {
		chkTakeAway.setSelected(true);
	    }
	    else
	    {
		chkTakeAway.setSelected(false);
	    }

	    rsDisc.close();
	    txtDiscountCode.requestFocus();
	}
	rsDisc.close();

	DefaultTableModel dtm = (DefaultTableModel) tblDiscDtl.getModel();
	dtm.setRowCount(0);

	sql = "select * from tbldiscdtl a "
		+ " where  a.strDiscCode='" + data[0].toString() + "'";
	rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsDisc.next())
	{
	    Object row[] =
	    {
		rsDisc.getString(2), rsDisc.getString(3), rsDisc.getString(4), rsDisc.getString(5)
	    };

	    dtm.addRow(row);
	}
	rsDisc.close();
    }

    /**
     * This method is used to save cost center
     */
    private void funSelectDiscountCode()
    {
	try
	{
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("DiscountMaster");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		btnNew.setText("UPDATE");
		btnNew.setMnemonic('u');
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetDiscountData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funSelectDiscountOn()
    {
	try
	{
	    clsUtility obj = new clsUtility();
	    if (cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("Item"))
	    {
		obj.funCallForSearchForm("MenuItem");
	    }
	    else if (cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("Group"))
	    {
		obj.funCallForSearchForm("Group");
	    }
	    else if (cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("SubGroup"))
	    {
		obj.funCallForSearchForm("SubGroup");
	    }
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		txtDiscountOnCode.setText(data[0].toString());
		lblDiscOnValue.setText(data[1].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	    else
	    {
		txtDiscountOnCode.setText("");
		lblDiscOnValue.setText("");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funSaveDiscount()
    {
	try
	{
	    String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());

	    Date dteFromDat = dteFromDate.getDate();
	    int d1 = dteFromDat.getDate();
	    int m1 = dteFromDat.getMonth() + 1;
	    int y1 = dteFromDat.getYear() + 1900;
	    //int validDateSum1 = d1 + m1 + y1;
	    String strFromDate = y1 + "-" + m1 + "-" + d1;

	    Date dteToDat = dteToDate.getDate();
	    int d = dteToDat.getDate();
	    int m = dteToDat.getMonth() + 1;
	    int y = dteToDat.getYear() + 1900;
	    //int validDateSum = d + m + y;
	    String strToDate = y + "-" + m + "-" + d;

	    String discountCode = funGetDiscountCode();
	    String discName = txtDiscountName.getText().trim();
	    String discOn = cmbDiscountOn.getSelectedItem().toString();

	    //operation type  for discount on
	    String dineIn = "N";
	    if (chkDineIn.isSelected())
	    {
		dineIn = "Y";
	    }

	    String homeDelivery = "N";
	    if (chkHomeDelivery.isSelected())
	    {
		homeDelivery = "Y";
	    }

	    String takeAway = "N";
	    if (chkTakeAway.isSelected())
	    {
		takeAway = "Y";
	    }

	    String insertSql = "INSERT INTO tbldischd (strDiscCode, strDiscName, strPOSCode, strClientCode,"
		    + " strDiscOn,dteFromDate,dteToDate"
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag "
		    + ",strDineIn,strHomeDelivery,strTakeAway) "
		    + " VALUES ('" + discountCode + "','" + discName + "','" + posCode + "', '" + clsGlobalVarClass.gClientCode + "'"
		    + ",'" + discOn + "','" + strFromDate + "', '" + strToDate + "'"
		    + ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "' ,'" + clsGlobalVarClass.gPOSDateForTransaction + "'"
		    + ",'N' "
		    + ",'" + dineIn + "','" + homeDelivery + "','" + takeAway + "')";
	    clsGlobalVarClass.dbMysql.execute(insertSql);

	    StringBuffer sqlDtl = new StringBuffer("INSERT INTO tbldiscdtl (strDiscCode, strDiscOnCode, strDiscOnName, strDiscountType"
		    + ",dblDiscountValue,strClientCode "
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag) "
		    + " VALUES ");
	    boolean insert = false;
	    for (int row = 0; row < tblDiscDtl.getRowCount(); row++)
	    {
		insert = true;
		String discOnCode = tblDiscDtl.getValueAt(row, 0).toString();
		String discOnName = tblDiscDtl.getValueAt(row, 1).toString();
		String discountType = tblDiscDtl.getValueAt(row, 2).toString();
		String discountValue = tblDiscDtl.getValueAt(row, 3).toString();

		if (row == 0)
		{
		    sqlDtl.append("('" + discountCode + "','" + discOnCode + "','" + discOnName + "','" + discountType + "','" + discountValue + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "' "
			    + ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "' ,'" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'N')");
		}
		else
		{
		    sqlDtl.append(",('" + discountCode + "','" + discOnCode + "','" + discOnName + "','" + discountType + "','" + discountValue + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "' "
			    + ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "' ,'" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'N')");
		}
	    }
	    if (insert)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlDtl.toString());
		String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ " where strTableName='DiscountMaster' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);

		funResetField();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funUpdateDiscount()
    {

	try
	{
	    String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());

	    Date dteFromDat = dteFromDate.getDate();
	    int d1 = dteFromDat.getDate();
	    int m1 = dteFromDat.getMonth() + 1;
	    int y1 = dteFromDat.getYear() + 1900;
	    //int validDateSum1 = d1 + m1 + y1;
	    String strFromDate = y1 + "-" + m1 + "-" + d1;

	    Date dteToDat = dteToDate.getDate();
	    int d = dteToDat.getDate();
	    int m = dteToDat.getMonth() + 1;
	    int y = dteToDat.getYear() + 1900;
	    //int validDateSum = d + m + y;
	    String strToDate = y + "-" + m + "-" + d;

	    String discountCode = txtDiscountCode.getText();
	    String discName = txtDiscountName.getText().trim();
	    String discOn = cmbDiscountOn.getSelectedItem().toString();

	    //operation type  for discount on
	    String dineIn = "N";
	    if (chkDineIn.isSelected())
	    {
		dineIn = "Y";
	    }

	    String homeDelivery = "N";
	    if (chkHomeDelivery.isSelected())
	    {
		homeDelivery = "Y";
	    }

	    String takeAway = "N";
	    if (chkTakeAway.isSelected())
	    {
		takeAway = "Y";
	    }

	    String updateQuery = "Update tbldischd "
		    + "SET strDiscName=? ,strPOSCode=? "
		    + ",dteFromDate=?,dteToDate=? "
		    + ",strUserEdited=?,dteDateEdited=? "
		    + ",strDineIn=?,strHomeDelivery=?,strTakeAway=?"
		    + "WHERE strDiscCode =? ";
	    PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(updateQuery);
	    pre.setString(1, discName);
	    pre.setString(2, posCode);
	    pre.setString(3, strFromDate);
	    pre.setString(4, strToDate);
	    pre.setString(5, clsGlobalVarClass.gUserCode);
	    pre.setString(6, clsGlobalVarClass.gPOSDateForTransaction);
	    pre.setString(7, dineIn);
	    pre.setString(8, homeDelivery);
	    pre.setString(9, takeAway);

	    pre.setString(10, discountCode);
	    int exc = pre.executeUpdate();
	    pre.close();

	    String deleteSql = "delete from tbldiscdtl "
		    + "where strDiscCode='" + discountCode + "' ";
	    clsGlobalVarClass.dbMysql.execute(deleteSql.toString());

	    StringBuffer sqlDtl = new StringBuffer("INSERT INTO tbldiscdtl (strDiscCode, strDiscOnCode, strDiscOnName, strDiscountType"
		    + ",dblDiscountValue,strClientCode "
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag) "
		    + " VALUES ");
	    boolean insert = false;
	    for (int row = 0; row < tblDiscDtl.getRowCount(); row++)
	    {
		insert = true;
		String discOnCode = tblDiscDtl.getValueAt(row, 0).toString();
		String discOnName = tblDiscDtl.getValueAt(row, 1).toString();
		String discountType = tblDiscDtl.getValueAt(row, 2).toString();
		String discountValue = tblDiscDtl.getValueAt(row, 3).toString();

		if (row == 0)
		{
		    sqlDtl.append("('" + discountCode + "','" + discOnCode + "','" + discOnName + "','" + discountType + "','" + discountValue + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "' "
			    + ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "' ,'" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'N')");
		}
		else
		{
		    sqlDtl.append(",('" + discountCode + "','" + discOnCode + "','" + discOnName + "','" + discountType + "','" + discountValue + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "' "
			    + ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "' ,'" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'N')");
		}
	    }
	    if (insert)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlDtl.toString());

		String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ " where strTableName='DiscountMaster' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);

		funResetField();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGetDiscountCode()
    {

	String discountCode = "";

	try
	{

	    selectQuery = "select count(*) from tbldischd ";
	    ResultSet countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    countSet1.next();
	    int cn = countSet1.getInt(1);
	    countSet1.close();
	    if (cn > 0)
	    {
		selectQuery = "select max(strDiscCode) from tbldischd ";
		ResultSet countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
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
		    discountCode = "D00000" + intCode;
		}
		else if (intCode < 100)
		{
		    discountCode = "D0000" + intCode;
		}
		else if (intCode < 1000)
		{
		    discountCode = "D000" + intCode;
		}
		else if (intCode < 10000)
		{
		    discountCode = "D00" + intCode;
		}
		else if (intCode < 100000)
		{
		    discountCode = "D0" + intCode;
		}
		else if (intCode < 1000000)
		{
		    discountCode = "D" + intCode;
		}

	    }
	    else
	    {
		code = "0";
		discountCode = "D000001";
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return discountCode;
    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {

	txtDiscountCode.requestFocus();
	btnNew.setText("SAVE");
	btnNew.setMnemonic('s');
	flag = false;
	txtDiscountCode.setText("");
	txtDiscountName.setText("");
	txtDiscountOnCode.setText("");
	txtDiscountValue.setText("0.00");
	cmbDiscountType.setSelectedIndex(0);
	cmbDiscountOn.setSelectedIndex(0);
	cmbPosCode.setSelectedIndex(0);
	cmbDiscountOn.setEnabled(true);

	chkDineIn.setSelected(true);
	chkHomeDelivery.setSelected(false);
	chkTakeAway.setSelected(false);

	DefaultTableModel dtm = (DefaultTableModel) tblDiscDtl.getModel();
	dtm.setRowCount(0);

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
        lblDiscountCode = new javax.swing.JLabel();
        txtDiscountCode = new javax.swing.JTextField();
        lblDiscountType = new javax.swing.JLabel();
        cmbDiscountOn = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        lbDiscountPer = new javax.swing.JLabel();
        lblCounterUserCode1 = new javax.swing.JLabel();
        cmbDiscountType = new javax.swing.JComboBox();
        cmbPosCode = new javax.swing.JComboBox();
        txtDiscountValue = new javax.swing.JTextField();
        txtDiscountOnCode = new javax.swing.JTextField();
        lbDiscountPer1 = new javax.swing.JLabel();
        lbDiscountPer2 = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        dteToDate = new com.toedter.calendar.JDateChooser();
        txtDiscountName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDiscDtl = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblDiscOnValue = new javax.swing.JLabel();
        lblDiscOnOperationType = new javax.swing.JLabel();
        chkHomeDelivery = new javax.swing.JCheckBox();
        chkDineIn = new javax.swing.JCheckBox();
        chkTakeAway = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        lblfromName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfromName.setForeground(new java.awt.Color(255, 255, 255));
        lblfromName.setText("-Discount Master");
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

        lblDiscountCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscountCode.setText("Discount Code        :");

        txtDiscountCode.setEditable(false);
        txtDiscountCode.setBackground(new java.awt.Color(204, 204, 204));
        txtDiscountCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountCodeMouseClicked(evt);
            }
        });
        txtDiscountCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountCodeKeyPressed(evt);
            }
        });

        lblDiscountType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscountType.setText("Discount On           :");

        cmbDiscountOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Item", "Group", "SubGroup" }));
        cmbDiscountOn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDiscountOnActionPerformed(evt);
            }
        });
        cmbDiscountOn.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbDiscountOnKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Cost Center Master");
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
        btnNew.setToolTipText("Save Cost Center Master");
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

        lbDiscountPer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbDiscountPer.setText("Discount Type        :");

        lblCounterUserCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterUserCode1.setText("POS                      : ");

        cmbDiscountType.setBackground(new java.awt.Color(51, 102, 255));
        cmbDiscountType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbDiscountType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Percentage", "Amount" }));
        cmbDiscountType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbDiscountTypeKeyPressed(evt);
            }
        });

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });

        txtDiscountValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountValue.setText("0.00");
        txtDiscountValue.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountValueMouseClicked(evt);
            }
        });
        txtDiscountValue.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtDiscountValueActionPerformed(evt);
            }
        });
        txtDiscountValue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountValueKeyPressed(evt);
            }
        });

        txtDiscountOnCode.setEditable(false);
        txtDiscountOnCode.setBackground(new java.awt.Color(204, 204, 204));
        txtDiscountOnCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountOnCodeMouseClicked(evt);
            }
        });
        txtDiscountOnCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountOnCodeKeyPressed(evt);
            }
        });

        lbDiscountPer1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbDiscountPer1.setText("From Date              :");

        lbDiscountPer2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbDiscountPer2.setText("To Date     :");

        dteFromDate.setToolTipText("Select From Date");
        dteFromDate.setPreferredSize(new java.awt.Dimension(119, 35));

        dteToDate.setToolTipText("Select To Date");
        dteToDate.setPreferredSize(new java.awt.Dimension(119, 35));

        txtDiscountName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtDiscountNameMouseEntered(evt);
            }
        });
        txtDiscountName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtDiscountNameActionPerformed(evt);
            }
        });
        txtDiscountName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountNameKeyPressed(evt);
            }
        });

        tblDiscDtl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDiscDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Code", "Name", "Discount Type", "Value"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblDiscDtl.setRowHeight(25);
        tblDiscDtl.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblDiscDtl.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblDiscDtl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblDiscDtl);
        if (tblDiscDtl.getColumnModel().getColumnCount() > 0)
        {
            tblDiscDtl.getColumnModel().getColumn(0).setMinWidth(0);
            tblDiscDtl.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblDiscDtl.getColumnModel().getColumn(0).setMaxWidth(0);
            tblDiscDtl.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblDiscDtl.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblDiscDtl.getColumnModel().getColumn(3).setPreferredWidth(50);
        }

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Save Cost Center Master");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAddKeyPressed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setToolTipText("Save Cost Center Master");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });
        btnRemove.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnRemoveKeyPressed(evt);
            }
        });

        lblDiscOnOperationType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscOnOperationType.setText("Discount On :");

        chkHomeDelivery.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkHomeDelivery.setText("Home Delivery");
        chkHomeDelivery.setOpaque(false);

        chkDineIn.setSelected(true);
        chkDineIn.setText("Dinning In");
        chkDineIn.setOpaque(false);

        chkTakeAway.setText("Take Away");
        chkTakeAway.setOpaque(false);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(432, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lbDiscountPer, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbDiscountType, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(txtDiscountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addComponent(lblDiscountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtDiscountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtDiscountName))
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addComponent(lblCounterUserCode1)
                            .addGap(25, 25, 25)
                            .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblDiscOnOperationType, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(chkDineIn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(chkHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(chkTakeAway, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(lblDiscountType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbDiscountOn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(lbDiscountPer1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(28, 28, 28)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtDiscountOnCode)
                                .addComponent(lbDiscountPer2, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(lblDiscOnValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDiscountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDiscountName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDiscountCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCounterUserCode1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                            .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(lblDiscOnOperationType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkDineIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkTakeAway, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDiscountPer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDiscountPer2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDiscountType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtDiscountOnCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbDiscountOn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDiscOnValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDiscountType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDiscountPer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(0, 164, Short.MAX_VALUE)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Discount Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Discount Master");
    }//GEN-LAST:event_formWindowClosing

    private void cmbDiscountTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDiscountTypeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbDiscountTypeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (btnNew.getText().equalsIgnoreCase("SAVE"))
	    {
		//Add new cost center
		funSaveDiscount();
	    }
	    else
	    {
		//Update existing cost center
		funUpdateDiscount();
	    }
	}
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
	// TODO add your handling code here:
	funSaveUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
	// TODO add your handling code here:
	if (btnNew.getText().equalsIgnoreCase("SAVE"))
	{
	    //Add new cost center
	    funSaveDiscount();
	}
	else
	{
	    //Update existing cost center
	    funUpdateDiscount();
	}
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Discount Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	try
	{
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("Discount Master");
	}
	catch (Exception e)
	{
	}
    }//GEN-LAST:event_btnCancelMouseClicked

    private void cmbDiscountOnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDiscountOnKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    // cmbSecondaryPrinters.requestFocus();
	}
    }//GEN-LAST:event_cmbDiscountOnKeyPressed

    private void cmbDiscountOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDiscountOnActionPerformed

	funDiscOnComboClicked();
    }//GEN-LAST:event_cmbDiscountOnActionPerformed

    private void txtDiscountCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funSelectDiscountCode();
	}
	if (evt.getKeyCode() == 10)
	{

	}
    }//GEN-LAST:event_txtDiscountCodeKeyPressed

    private void txtDiscountCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountCodeMouseClicked
	// TODO add your handling code here:
	funSelectDiscountCode();
    }//GEN-LAST:event_txtDiscountCodeMouseClicked

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void txtDiscountValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountValueMouseClicked
	try
	{

	    if (txtDiscountValue.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Discount Value").setVisible(true);
		txtDiscountValue.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtDiscountValue.getText(), "Double", "Enter Discount Value").setVisible(true);
		txtDiscountValue.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtDiscountValueMouseClicked

    private void txtDiscountValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountValueActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountValueActionPerformed

    private void txtDiscountValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountValueKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountValueKeyPressed

    private void txtDiscountOnCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountOnCodeMouseClicked
	if (!cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("All"))
	{
	    funSelectDiscountOn();
	}

    }//GEN-LAST:event_txtDiscountOnCodeMouseClicked

    private void txtDiscountOnCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountOnCodeKeyPressed
	if (!cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("All"))
	{
	    funSelectDiscountOn();
	}
    }//GEN-LAST:event_txtDiscountOnCodeKeyPressed

    private void txtDiscountNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDiscountNameMouseClicked
    {//GEN-HEADEREND:event_txtDiscountNameMouseClicked
	try
	{
	    if (txtDiscountName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Discount Description").setVisible(true);
		txtDiscountName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtDiscountName.getText(), "1", "Enter Discount Description").setVisible(true);
		txtDiscountName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtDiscountNameMouseClicked

    private void txtDiscountNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtDiscountNameActionPerformed
    {//GEN-HEADEREND:event_txtDiscountNameActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountNameActionPerformed

    private void txtDiscountNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtDiscountNameKeyPressed
    {//GEN-HEADEREND:event_txtDiscountNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountNameKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAddMouseClicked
    {//GEN-HEADEREND:event_btnAddMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
	funAddButtonClicked();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAddKeyPressed
    {//GEN-HEADEREND:event_btnAddKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnRemoveMouseClicked
    {//GEN-HEADEREND:event_btnRemoveMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
	funRemoveButtonClicked();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnRemoveKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnRemoveKeyPressed
    {//GEN-HEADEREND:event_btnRemoveKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveKeyPressed

    private void txtDiscountNameMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDiscountNameMouseEntered
    {//GEN-HEADEREND:event_txtDiscountNameMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountNameMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkDineIn;
    private javax.swing.JCheckBox chkHomeDelivery;
    private javax.swing.JCheckBox chkTakeAway;
    private javax.swing.JComboBox cmbDiscountOn;
    private javax.swing.JComboBox cmbDiscountType;
    private javax.swing.JComboBox cmbPosCode;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDiscountPer;
    private javax.swing.JLabel lbDiscountPer1;
    private javax.swing.JLabel lbDiscountPer2;
    private javax.swing.JLabel lblCounterUserCode1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDiscOnOperationType;
    private javax.swing.JLabel lblDiscOnValue;
    private javax.swing.JLabel lblDiscountCode;
    private javax.swing.JLabel lblDiscountType;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblfromName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblDiscDtl;
    private javax.swing.JTextField txtDiscountCode;
    private javax.swing.JTextField txtDiscountName;
    private javax.swing.JTextField txtDiscountOnCode;
    private javax.swing.JTextField txtDiscountValue;
    // End of variables declaration//GEN-END:variables

    private void funSaveUpdate()
    {
	String discName = txtDiscountName.getText().trim();
	Date dt1 = dteFromDate.getDate();
	Date dt2 = dteToDate.getDate();

	if (discName.isEmpty())
	{
	    new frmOkPopUp(this, "Please Enter Discount Name", "Error", 1).setVisible(true);
	    return;
	}

	if ((dt2.getTime() - dt1.getTime()) < 0)
	{
	    new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
	    return;
	}

	if (tblDiscDtl.getRowCount() <= 0)
	{
	    new frmOkPopUp(this, "Please Enter Discount Detail", "Error", 1).setVisible(true);
	    return;
	}

	if (btnNew.getText().equalsIgnoreCase("SAVE"))
	{
	    funSaveDiscount();
	}
	else
	{
	    funUpdateDiscount();
	}

    }

    private void funAddButtonClicked()
    {

	String discountOnCode = txtDiscountOnCode.getText();
	String discountOnName = lblDiscOnValue.getText();
	String discTye = cmbDiscountType.getSelectedItem().toString();
	String discOn = cmbDiscountOn.getSelectedItem().toString();

	if (!objUtility.funCheckLength(txtDiscountValue.getText(), 6))
	{
	    new frmOkPopUp(this, "Value length must be less than 7", "Error", 0).setVisible(true);
	    txtDiscountValue.requestFocus();
	    return;
	}
	if (discountOnCode.isEmpty())
	{
	    new frmOkPopUp(this, "Please Select " + discOn, "Error", 0).setVisible(true);
	    return;
	}
	double dblDiscValue = Double.parseDouble(txtDiscountValue.getText().trim());
	if (dblDiscValue <= 0)
	{
	    new frmOkPopUp(this, "Invalid Discount Value", "Error", 0).setVisible(true);
	    return;
	}
	boolean isExists = false;
	for (int i = 0; i < tblDiscDtl.getRowCount(); i++)
	{
	    if (discountOnCode.equalsIgnoreCase(tblDiscDtl.getValueAt(i, 0).toString()))
	    {
		isExists = true;
		break;
	    }
	}
	if (isExists)
	{
	    new frmOkPopUp(this, "Duplicate Discount.", "Error", 0).setVisible(true);
	    return;
	}

	boolean isValid = true;
	for (int i = 0; i < tblDiscDtl.getRowCount(); i++)
	{
	    if (tblDiscDtl.getValueAt(i, 0).toString().equalsIgnoreCase("All"))
	    {
		isValid = false;
		break;
	    }
	}
	if (!isValid)
	{
	    new frmOkPopUp(this, "Invalid Discount Details.", "Error", 0).setVisible(true);
	    return;
	}

	if (tblDiscDtl.getRowCount() > 0 && discOn.equalsIgnoreCase("All"))
	{
	    new frmOkPopUp(this, "Invalid Discount Details.", "Error", 0).setVisible(true);
	    return;
	}

	Object row[] =
	{
	    discountOnCode, discountOnName, discTye, dblDiscValue
	};
	DefaultTableModel dtm = (DefaultTableModel) tblDiscDtl.getModel();
	dtm.addRow(row);

	txtDiscountValue.setText("0.00");
	cmbDiscountType.setSelectedIndex(0);
	if (!discOn.equalsIgnoreCase("All"))
	{
	    txtDiscountOnCode.setText("");
	    lblDiscOnValue.setText("");
	}

	cmbDiscountOn.setEnabled(false);

    }

    private void funRemoveButtonClicked()
    {
	int selectedRow = tblDiscDtl.getSelectedRow();
	if (selectedRow < 0)
	{
	    new frmOkPopUp(this, "Please Select Discount Details.", "Error", 0).setVisible(true);
	    return;
	}
	DefaultTableModel dtm = (DefaultTableModel) tblDiscDtl.getModel();
	dtm.removeRow(selectedRow);

	if (dtm.getRowCount() == 0)
	{
	    cmbDiscountOn.setEnabled(true);
	}
    }

    private void funDiscOnComboClicked()
    {
	if (cmbDiscountOn.getSelectedItem().toString().equalsIgnoreCase("All"))
	{
	    txtDiscountOnCode.setEnabled(false);
	    txtDiscountOnCode.setText("All");
	    lblDiscOnValue.setText("All");
	}
	else
	{
	    txtDiscountOnCode.setEnabled(true);
	    txtDiscountOnCode.setText("");
	    lblDiscOnValue.setText("");
	}
    }

}
