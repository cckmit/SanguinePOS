/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmMakeBill extends javax.swing.JFrame
{

    private ResultSet rs;
    private String sql, tableNo;
    private String tableName;
    private int cntNavigate1;
    private clsCustomerDataModelForSQY objData;
    private String clsAreaCode, clsAreaCodeForAll;
    private String clsAreaName;
    private Map<String, String> hmTable;
    private Map<String, Integer> hmTableSeq;
    private clsUtility objUtility = new clsUtility();
    private clsUtility2 objUtility2 = new clsUtility2();
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public frmMakeBill()
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
	    btnPrevItem.setEnabled(true);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);

	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);

	    hmTable = new HashMap<String, String>();
	    hmTableSeq = new HashMap<String, Integer>();

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

	    String sqlUpdateTablStatus = "update tbltablemaster a "
		    + "join tblitemrtemp b on a.strTableNo=b.strTableNo "
		    + "set a.strStatus='Occupied' "
		    + "where a.strStatus='Normal' "
		    + "and b.strNCKOTYN='N' ";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdateTablStatus);

	    String sqlArea = "select strAreaCode from tblareamaster "
		    + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
		    + " and strAreaName='All' "
		    + " order by strAreaCode";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
	    if (rsArea.next())
	    {
		clsAreaCodeForAll = rsArea.getString(1);
		funSetArea(rsArea.getString(1));
	    }
	    rsArea.close();
	    funInitTables();
	    btnPrevItem.setEnabled(false);
	    cntNavigate1 = 1;
	    funPreviousTableClick();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void tickTock()
    {
	lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    private void funSetArea(String selAreaCode)
    {
	try
	{
	    cntNavigate1 = 0;
	    clsAreaCode = selAreaCode;
	    sql = "select strAreaName from tblareamaster where strAreaCode='" + clsAreaCode + "'";
	    ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsAreaInfo.next())
	    {
		clsAreaName = rsAreaInfo.getString(1);
	    }
	    rsAreaInfo.close();
	    lblAreaName4.setText(clsAreaName);
	    hmTable.clear();
	    hmTableSeq.clear();

	    if (clsAreaName.equalsIgnoreCase("All"))
	    {
		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strStatus='Occupied' and strOperational='Y' "
			+ " order by intSequence ";
		/*
                 sql = "select strTableNo,strTableName,intSequence "
                 + " from tbltablemaster "
                 + " where (strPOSCode='"+posCode+"' "
                 + " or strPOSCode='All') "
                 + " and strOperational='Y' and strStatus='Occupied' "
                 + " order by intSequence ";
		 */
	    }
	    else
	    {

		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ " where strAreaCode='" + clsAreaCode + "' "
			+ " and (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strStatus='Occupied' and strOperational='Y' "
			+ " order by intSequence ";
		/*
                 sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                 + " where strAreaCode='" + clsAreaCode + "' "
                 + " and (strPOSCode='"+posCode+"' "
                 + " or strPOSCode='All') "
                 + " and strOperational='Y' and strStatus='Occupied' "
                 + " order by intSequence ";
		 */
	    }
	    System.out.println(sql);
	    ResultSet rsTableCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTableCode.next())
	    {
		hmTable.put(rsTableCode.getString(2), rsTableCode.getString(1));
		hmTableSeq.put(rsTableCode.getString(1) + "!" + rsTableCode.getString(2), rsTableCode.getInt(3));
	    }
	    rsTableCode.close();
	    funLoadTables(0, hmTableSeq.size());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInitTables()
    {
	try
	{
	    hmTable.clear();
	    hmTableSeq.clear();
	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (clsGlobalVarClass.gTreatMemberAsTable)
		{
		    sql = "select strTableNo,strTableName from tbltablemaster "
			    + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			    + " and strOperational='Y' "
			    + " AND strStatus='Occupied' "
			    + " order by strTableName";
		}
		else
		{
		    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			    + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			    + " and strOperational='Y' "
			    + " AND strStatus='Occupied' "
			    + " order by intSequence";
		}
	    }
	    else
	    {
		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strOperational='Y' "
			+ " AND strStatus='Occupied' "
			+ " order by intSequence";
	    }
	    ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTableInfo.next())
	    {
		hmTable.put(rsTableInfo.getString(2).toUpperCase(), rsTableInfo.getString(1));
		hmTableSeq.put(rsTableInfo.getString(1) + "!" + rsTableInfo.getString(2), rsTableInfo.getInt(3));
	    }

	    rsTableInfo.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadTables(int startIndex, int totalSize)
    {
	try
	{
	    int cntIndex = 0;
	    if (startIndex == 0)
	    {
		btnPrevItem.setEnabled(false);
	    }
	    JButton[] btnTableArray =
	    {
		btnTable1, btnTable2, btnTable3, btnTable4, btnTable5, btnTable6, btnTable7, btnTable8, btnTable9, btnTable10, btnTable11, btnTable12, btnTable13, btnTable14, btnTable15, btnTable16
	    };
	    for (int cntTable = 0; cntTable < btnTableArray.length; cntTable++)
	    {
		btnTableArray[cntTable].setForeground(Color.black);
		btnTableArray[cntTable].setText("");
		btnTableArray[cntTable].setIcon(null);
	    }

	    hmTableSeq = clsGlobalVarClass.funSortMapOnValues(hmTableSeq);
	    //Object[] arrObjTables = hmTable.entrySet().toArray();
	    Object[] arrObjTables = hmTableSeq.entrySet().toArray();

	    for (int cntTable = startIndex; cntTable < totalSize; cntTable++)
	    {
		if (cntTable == totalSize)
		{
		    break;
		}
		String tblInfo = arrObjTables[cntTable].toString().split("=")[0];
		String tblName = tblInfo.split("!")[1];

		sql = "select strTableNo,strStatus,intPaxNo "
			+ " from tbltablemaster "
			+ " where strTableNo='" + tblInfo.split("!")[0] + "' "
			+ " and strOperational='Y' "
			+ " order by intSequence";
		//System.out.println(arrObjTables[cntTable].toString().split("=")[1]);
		ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsTableInfo.next();
		String status = rsTableInfo.getString(2);
		int pax = rsTableInfo.getInt(3);
		if (cntIndex < 16)
		{
		    if (status.equals("Occupied"))
		    {
			btnTableArray[cntIndex].setBackground(Color.red);
			btnTableArray[cntIndex].setForeground(Color.white);
		    }
		    else if (status.equals("Billed"))
		    {
			btnTableArray[cntIndex].setBackground(Color.blue);
			btnTableArray[cntIndex].setForeground(Color.white);
		    }
		    else if (status.equals("Normal"))
		    {
			btnTableArray[cntIndex].setBackground(Color.lightGray);
			btnTableArray[cntIndex].setForeground(Color.black);
		    }
		    btnTableArray[cntIndex].setText("<html>" + tblName + "<br>" + pax + "</html>");
		    btnTableArray[cntIndex].setEnabled(true);
		    cntIndex++;
		}
		rsTableInfo.close();
	    }

	    for (int cntTable1 = cntIndex; cntTable1 < 16; cntTable1++)
	    {
		btnTableArray[cntTable1].setEnabled(false);
	    }
	    if (totalSize > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    else
	    {
		btnNextItem.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funRemoveLast(String text)
    {
	String temSt = text;
	if (temSt.contains("<br>"))
	{
	    StringBuilder sb = new StringBuilder(text);
	    sb = sb.delete(sb.lastIndexOf("<br>"), sb.length());
	    temSt = sb.substring(6, sb.length());
	}
	return temSt;
    }

    private void funFillItemGrid(String tblName)
    {
	try
	{
	    tableName = funRemoveLast(tblName);
	    tableNo = hmTable.get(tableName.toUpperCase());
	    double grandTotal = 0;
	    DefaultTableModel dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };

	    sql = "select count(*) from tblitemrtemp "
		    + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and strTableNo='" + tableNo + "' "
		    + " and strNCKOTYN='N' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rs.next();
	    if (rs.getInt(1) > 0)
	    {
		txtTableNo.setText(tableName);
		int pax = 0;
		String wtNo = "";
		dm.addColumn("Description");
		dm.addColumn("Qty");
		dm.addColumn("Amount");
		sql = "select strItemName,sum(dblItemQuantity),sum(dblAmount),strWaiterNo,intPaxNo "
			+ " from tblitemrtemp "
			+ " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ " and strTableNo='" + tableNo + "' "
			+ " and strNCKOTYN='N' "
			+ " group by strItemCode "
			+ " order by strSerialNo ";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

		while (rs.next())
		{
		    wtNo = rs.getString(4);
		    pax = rs.getInt(5);
		    grandTotal = grandTotal + rs.getDouble(3);
		    Object[] rows =
		    {
			rs.getString(1), rs.getString(2), rs.getString(3)
		    };
		    dm.addRow(rows);
		}
		rs.close();
		if ("null".equalsIgnoreCase(wtNo))
		{
		    lblWaiterName.setText("");
		}
		else
		{
		    rs = clsGlobalVarClass.dbMysql.executeResultSet("select strWShortName from tblwaitermaster where strWaiterNo='" + wtNo + "'");
		    if (rs.next())
		    {
			lblWaiterName.setText(rs.getString(1));
		    }
		    rs.close();
		}

		lblPAX.setText(String.valueOf(pax));
		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    //start code to calculate roundoff amount and round off by amt
		    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
		    grandTotal = mapRoundOff.get("roundOffAmt");
		}
		else
		{
		    grandTotal = Double.parseDouble(gDecimalFormat.format(grandTotal));
		}
		txtTotal.setText(String.valueOf(grandTotal));

		tblItemTable.setModel(dm);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblItemTable.setShowHorizontalLines(true);
		tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(210);
		tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(65);
		tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(95);

		btnMakeBill.requestFocus();
	    }
	    else
	    {
		funResetMakeBillTable();
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funNextTableClick()
    {
	cntNavigate1++;
	int tableSize = cntNavigate1 * 16;
	int resDiv = hmTable.size() / 16;
	int totalSize = tableSize + 16;

	if (hmTable.size() < totalSize)
	{
	    funLoadTables(tableSize, hmTable.size());
	}
	else
	{
	    funLoadTables(tableSize, totalSize);
	}
	btnPrevItem.setEnabled(true);
	if (resDiv == cntNavigate1)
	{
	    btnNextItem.setEnabled(false);
	}
    }

    private void funPreviousTableClick()
    {
	try
	{
	    cntNavigate1--;
	    if (cntNavigate1 == 0)
	    {
		btnPrevItem.setEnabled(false);
		btnNextItem.setEnabled(true);
		funLoadTables(0, hmTable.size());
	    }
	    else
	    {
		int tableSize = cntNavigate1 * 16;
		int totalSize = tableSize + 16;
		funLoadTables(tableSize, totalSize);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funEnableControls()
    {
	btnMakeBill.setEnabled(true);
    }

    public void funResetMakeBillTable()
    {
	try
	{
	    btnMakeBill.setEnabled(true);
	    txtTableNo.setText("");
	    lblWaiterName.setText("");
	    lblPaxNo.setText("");

	    lblWaiterName.setText("");
	    lblCustomerName.setText("");
	    lblPAX.setText("");

	    DefaultTableModel dm = new DefaultTableModel();
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    tblItemTable.setModel(dm);
	    txtTotal.setText("");
	    clsGlobalVarClass.gTakeAway = "No";
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    //tblItemTable.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(),24));
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(165);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	    funSetArea(clsAreaCodeForAll);
	    txtTableNo.requestFocus();
	    clsGlobalVarClass.gDebitCardNo = null;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCallTableSearch()
    {
	objUtility.funCallForSearchForm("MakeBillTableSearch");
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funFillItemGrid(data[1].toString());
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funTableNoTextFieldClicked(java.awt.event.MouseEvent evt)
    {
	if (evt.getClickCount() == 2)
	{
	    funCallTableSearch();
	}
	else
	{
	    txtTableNo.setText("");
	    funLoadTables(0, hmTable.size());
	}
    }

    private void funNewCustomerButtonPressed()
    {
	if (clsGlobalVarClass.gCRMInterface.equals("SQY"))
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
	    {
		clsGlobalVarClass.gFlgPoints = "DiscountPoints";
		new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    }
	}
	else if (clsGlobalVarClass.gCRMInterface.equals("PMAM"))
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
	    {
		clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
		funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
	    }
	}
	else
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
	    {
		if (clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{10}"))
		{
		    clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
		    funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Please Enter Valid Mobile No.");
		    return;
		}
	    }
	}
    }

    private void funSetCustMobileNo(String mbNo)
    {
	String buildingCodeForHD = "";
	try
	{
	    double totalBillAmount = 0.00;
	    if (txtTotal.getText().trim().length() > 0)
	    {
		totalBillAmount = Double.parseDouble(txtTotal.getText());
	    }
	    if (mbNo.trim().length() == 0)
	    {
		objUtility.funCallForSearchForm("CustomerMaster");
		new frmSearchFormDialog(this, true).setVisible(true);
		if (clsGlobalVarClass.gSearchItemClicked)
		{
		    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		    clsGlobalVarClass.gCustomerCode = data[0].toString();
		    buildingCodeForHD = data[4].toString();
		    clsGlobalVarClass.gSearchItemClicked = false;
		    lblCustomerName.setText("<html>" + data[1].toString() + "</html>");
		}
	    }
	    else
	    {
		sql = "select count(strCustomerCode) from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
		ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsCustomer.next();
		int found = rsCustomer.getInt(1);
		rsCustomer.close();

		if (found > 0)
		{
		    if (clsGlobalVarClass.gCustAddressSelectionForBill)
		    {
			sql = "select strCustomerCode,strCustomerName,longMobileNo,strBuildingName,"
				+ "strStreetName,strLandMark,strOfficeBuildingName,strOfficeStreetName,strOfficeLandmark"
				+ " from tblcustomermaster where longMobileNo='" + mbNo + "'";
			rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rsCustomer.next())
			{
			    lblCustomerName.setText(rsCustomer.getString(2));
			}
			rsCustomer.close();
			clsGlobalVarClass.gCustMBNo = mbNo;
			clsGlobalVarClass.gSearchItem = mbNo;
			objUtility.funCallForSearchForm("CustomerAddress");
			new frmSearchFormDialog(this, true).setVisible(true);
			if (clsGlobalVarClass.gSearchItemClicked)
			{
			    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			    clsGlobalVarClass.gCustomerCode = data[0].toString();
			    buildingCodeForHD = "";
			    clsGlobalVarClass.gSearchItemClicked = false;
			    lblCustomerName.setText("<html>" + data[1].toString() + "</html>");
			}
		    }
		    else
		    {
			String sql_CustInfo = "select strCustomerCode,strCustomerName,strBuldingCode "
				+ "from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
			ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
			if (rsCust.next())
			{
			    clsGlobalVarClass.gCustomerCode = rsCust.getString(1);
			    lblCustomerName.setText("<html>" + rsCust.getString(2) + "</html>");
			    clsGlobalVarClass.gCustMBNo = mbNo;
			    buildingCodeForHD = rsCust.getString(3);
			}
		    }
		}
		else
		{
		    clsGlobalVarClass.gNewCustomerForHomeDel = true;
		    clsGlobalVarClass.gTotalBillAmount = totalBillAmount;
		    clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(mbNo);
		    new frmCustomerMaster().setVisible(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funDoneButtonPressed()
    {

	if (tblItemTable.getRowCount() == 0)
	{

	    new frmOkPopUp(this, "Please Select Table", "Warning", 1).setVisible(true);
	    return;
	}
	else
	{
	    btnMakeBill.setEnabled(false);
	    txtTableNo.requestFocus();
	    txtTableNo.selectAll();
	    try{
		if(clsGlobalVarClass.gStrMergeAllKOTSToBill){
		String strStatus="Normal";
		String sql="select count(a.strTableNo) from tblbillhd a where a.strTableNo='"+tableNo+"' " +
			    " and a.strBillNo NOT IN (select b.strBillNo from tblbillsettlementdtl b)  ";
		ResultSet rs= clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if(rs.next()){
		    if(rs.getInt(1)>0){
			strStatus="AlreadyBilled";
		    }
		}
		if(strStatus.equals("AlreadyBilled")){
		    frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want to Merge this Table on Previous Bill ",false);
		    okOb.setVisible(true);
		    int res = okOb.getResult();
		    okOb.dispose();
		    if (res == 1)
		    {
			 if(!funAddAllKOTSOnTableToBill(tableNo)){
			     btnMakeBill.setEnabled(true);
			     JOptionPane.showMessageDialog(this, "Bill Not Found On This Table");
			     return;
			 }

		    }else{
			new frmBillSettlement(this, tableNo).setVisible(true);
		    }    
		}
		else{
			new frmBillSettlement(this, tableNo).setVisible(true);
		    }
		
	    }else{
		new frmBillSettlement(this, tableNo).setVisible(true);
	    }
		
	    }catch(Exception e){
		e.printStackTrace();
	    }
	    
	    

	}
	txtTableNo.setFocusable(true);
    }

    private void funCustomerNoButtonClicked()
    {
	if (tblItemTable.getRowCount() == 0)
	{

	    new frmOkPopUp(this, "Please Select Table", "Warning", 1).setVisible(true);
	    return;
	}

	if (clsGlobalVarClass.gCRMInterface.equals("SQY"))
	{
	    clsGlobalVarClass.gFlgPoints = "DiscountPoints";
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	}
	else
	{
	    clsGlobalVarClass.gNewCustomerForHomeDel = true;
	    funNewCustomerButtonPressed();
	}
    }

    private void funHomeButtonClicked()
    {
	frmOkCancelPopUp okOb = new frmOkCancelPopUp(this, "Do you want to end transaction");
	okOb.setVisible(true);
	int res = okOb.getResult();
	if (res == 1)
	{
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("Make Bill");
	}
    }

    private void funCheckKOTButtonClicked()
    {

	if (tblItemTable.getRowCount() == 0)
	{
	    new frmOkPopUp(this, "Please Select Table", "Warning", 1).setVisible(true);
	    return;
	}

	if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
	{
	    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
	    objKOTGeneration.funCkeckKotTextFile(tableNo, lblWaiterName.getText(), "Y","");
	}
	else
	{
	    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();

	    objKOTGeneration.funCkeckKotForJasper(tableNo,lblWaiterName.getText().trim(), "Y");
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
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelItemGrid = new javax.swing.JPanel();
        scrItemGrid = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        txtTotal = new javax.swing.JTextField();
        lblTotalAmt = new javax.swing.JLabel();
        lblPaxNo = new javax.swing.JLabel();
        lblWaiterName = new javax.swing.JLabel();
        labelWaiterName = new javax.swing.JLabel();
        labelPaxNo = new javax.swing.JLabel();
        lblPAX = new javax.swing.JLabel();
        lblTbl = new javax.swing.JLabel();
        txtTableNo = new javax.swing.JTextField();
        panelTableList = new javax.swing.JPanel();
        btnTable2 = new javax.swing.JButton();
        btnTable1 = new javax.swing.JButton();
        btnTable3 = new javax.swing.JButton();
        btnTable4 = new javax.swing.JButton();
        btnTable5 = new javax.swing.JButton();
        btnTable6 = new javax.swing.JButton();
        btnTable7 = new javax.swing.JButton();
        btnTable8 = new javax.swing.JButton();
        btnTable9 = new javax.swing.JButton();
        btnTable10 = new javax.swing.JButton();
        btnTable11 = new javax.swing.JButton();
        btnTable12 = new javax.swing.JButton();
        btnTable13 = new javax.swing.JButton();
        btnTable14 = new javax.swing.JButton();
        btnTable15 = new javax.swing.JButton();
        btnTable16 = new javax.swing.JButton();
        panelNavigation4 = new javax.swing.JPanel();
        panelArea4 = new javax.swing.JPanel();
        lblAreaName4 = new javax.swing.JLabel();
        btnPrevItem = new javax.swing.JButton();
        btnNextItem = new javax.swing.JButton();
        lblCustomerName = new javax.swing.JLabel();
        btnMakeBill = new javax.swing.JButton();
        btnCustMobileNo = new javax.swing.JButton();
        btnCheckKOT = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        lblCustomerName1 = new javax.swing.JLabel();

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
        lblProductName.setText(" SPOS - ");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("jLabel2");
        lblModuleName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblModuleNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Make Bill");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelItemGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelItemGrid.setForeground(new java.awt.Color(254, 184, 80));
        panelItemGrid.setOpaque(false);
        panelItemGrid.setPreferredSize(new java.awt.Dimension(260, 600));
        panelItemGrid.setLayout(null);

        tblItemTable.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblItemTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblItemTable.setRowHeight(25);
        tblItemTable.setShowVerticalLines(false);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrItemGrid.setViewportView(tblItemTable);
        if (tblItemTable.getColumnModel().getColumnCount() > 0)
        {
            tblItemTable.getColumnModel().getColumn(0).setMinWidth(210);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(210);
            tblItemTable.getColumnModel().getColumn(0).setMaxWidth(210);
            tblItemTable.getColumnModel().getColumn(1).setMinWidth(65);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(65);
            tblItemTable.getColumnModel().getColumn(1).setMaxWidth(65);
            tblItemTable.getColumnModel().getColumn(2).setMinWidth(95);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(95);
            tblItemTable.getColumnModel().getColumn(2).setMaxWidth(95);
        }

        panelItemGrid.add(scrItemGrid);
        scrItemGrid.setBounds(0, 50, 370, 410);

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        panelItemGrid.add(txtTotal);
        txtTotal.setBounds(280, 470, 90, 30);

        lblTotalAmt.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblTotalAmt.setText("TOTAL");
        panelItemGrid.add(lblTotalAmt);
        lblTotalAmt.setBounds(220, 470, 60, 30);
        panelItemGrid.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        lblWaiterName.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        panelItemGrid.add(lblWaiterName);
        lblWaiterName.setBounds(160, 10, 150, 30);

        labelWaiterName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        labelWaiterName.setText("WTR");
        panelItemGrid.add(labelWaiterName);
        labelWaiterName.setBounds(130, 10, 22, 30);

        labelPaxNo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        labelPaxNo.setText("PAX");
        panelItemGrid.add(labelPaxNo);
        labelPaxNo.setBounds(320, 10, 20, 30);

        lblPAX.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        panelItemGrid.add(lblPAX);
        lblPAX.setBounds(350, 10, 20, 30);

        lblTbl.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblTbl.setText("TBL");
        panelItemGrid.add(lblTbl);
        lblTbl.setBounds(0, 10, 20, 30);

        txtTableNo.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        txtTableNo.setForeground(new java.awt.Color(51, 102, 255));
        txtTableNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTableNoMouseClicked(evt);
            }
        });
        txtTableNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTableNoKeyPressed(evt);
            }
        });
        panelItemGrid.add(txtTableNo);
        txtTableNo.setBounds(20, 10, 100, 30);

        panelTableList.setBackground(new java.awt.Color(255, 255, 255));
        panelTableList.setEnabled(false);
        panelTableList.setOpaque(false);

        btnTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable2MouseClicked(evt);
            }
        });
        btnTable2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable2ActionPerformed(evt);
            }
        });

        btnTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable1ActionPerformed(evt);
            }
        });

        btnTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable3MouseClicked(evt);
            }
        });
        btnTable3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable3ActionPerformed(evt);
            }
        });

        btnTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable4MouseClicked(evt);
            }
        });
        btnTable4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable4ActionPerformed(evt);
            }
        });

        btnTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable5MouseClicked(evt);
            }
        });
        btnTable5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable5ActionPerformed(evt);
            }
        });

        btnTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable6MouseClicked(evt);
            }
        });
        btnTable6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable6ActionPerformed(evt);
            }
        });

        btnTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable7MouseClicked(evt);
            }
        });
        btnTable7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable7ActionPerformed(evt);
            }
        });

        btnTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable8MouseClicked(evt);
            }
        });
        btnTable8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable8ActionPerformed(evt);
            }
        });

        btnTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable9MouseClicked(evt);
            }
        });
        btnTable9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable9ActionPerformed(evt);
            }
        });

        btnTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable10MouseClicked(evt);
            }
        });
        btnTable10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable10ActionPerformed(evt);
            }
        });

        btnTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable11MouseClicked(evt);
            }
        });
        btnTable11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable11ActionPerformed(evt);
            }
        });

        btnTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable12MouseClicked(evt);
            }
        });
        btnTable12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable12ActionPerformed(evt);
            }
        });

        btnTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable13MouseClicked(evt);
            }
        });
        btnTable13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable13ActionPerformed(evt);
            }
        });

        btnTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable14MouseClicked(evt);
            }
        });
        btnTable14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable14ActionPerformed(evt);
            }
        });

        btnTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable15MouseClicked(evt);
            }
        });
        btnTable15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable15ActionPerformed(evt);
            }
        });

        btnTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTable16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTable16MouseClicked(evt);
            }
        });
        btnTable16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTable16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTableListLayout = new javax.swing.GroupLayout(panelTableList);
        panelTableList.setLayout(panelTableListLayout);
        panelTableListLayout.setHorizontalGroup(
            panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTableListLayout.createSequentialGroup()
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTableListLayout.createSequentialGroup()
                                .addComponent(btnTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelTableListLayout.createSequentialGroup()
                                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTableListLayout.createSequentialGroup()
                                .addComponent(btnTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(btnTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTableListLayout.createSequentialGroup()
                                .addComponent(btnTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelTableListLayout.createSequentialGroup()
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTableListLayout.createSequentialGroup()
                                .addComponent(btnTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableListLayout.createSequentialGroup()
                                .addComponent(btnTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        panelTableListLayout.setVerticalGroup(
            panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTableListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTableListLayout.createSequentialGroup()
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNavigation4.setBackground(new java.awt.Color(255, 255, 255));
        panelNavigation4.setOpaque(false);

        panelArea4.setBackground(new java.awt.Color(69, 164, 238));
        panelArea4.setForeground(new java.awt.Color(240, 200, 80));
        panelArea4.setName(""); // NOI18N
        panelArea4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                panelAreaMouseClicked(evt);
            }
        });

        lblAreaName4.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblAreaName4.setForeground(new java.awt.Color(255, 255, 255));
        lblAreaName4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAreaName4.setText("All");

        javax.swing.GroupLayout panelArea4Layout = new javax.swing.GroupLayout(panelArea4);
        panelArea4.setLayout(panelArea4Layout);
        panelArea4Layout.setHorizontalGroup(
            panelArea4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAreaName4, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
        );
        panelArea4Layout.setVerticalGroup(
            panelArea4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAreaName4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnPrevItem.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnPrevItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPrevItem.setText("<<<");
        btnPrevItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnPrevItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevItemActionPerformed(evt);
            }
        });

        btnNextItem.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnNextItem.setForeground(new java.awt.Color(255, 255, 255));
        btnNextItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnNextItem.setText(">>>");
        btnNextItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnNextItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNextItemMouseClicked(evt);
            }
        });
        btnNextItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNavigation4Layout = new javax.swing.GroupLayout(panelNavigation4);
        panelNavigation4.setLayout(panelNavigation4Layout);
        panelNavigation4Layout.setHorizontalGroup(
            panelNavigation4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigation4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelArea4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelNavigation4Layout.setVerticalGroup(
            panelNavigation4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigation4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNavigation4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panelArea4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrevItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        lblCustomerName.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N

        btnMakeBill.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnMakeBill.setForeground(new java.awt.Color(255, 255, 255));
        btnMakeBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnMakeBill.setMnemonic('d');
        btnMakeBill.setText("DONE");
        btnMakeBill.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnMakeBill.setBorderPainted(false);
        btnMakeBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMakeBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn2.png"))); // NOI18N
        btnMakeBill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMakeBillActionPerformed(evt);
            }
        });
        btnMakeBill.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnMakeBillKeyPressed(evt);
            }
        });

        btnCustMobileNo.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnCustMobileNo.setForeground(new java.awt.Color(255, 255, 255));
        btnCustMobileNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnCustMobileNo.setMnemonic('c');
        btnCustMobileNo.setText("CUSTOMER");
        btnCustMobileNo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnCustMobileNo.setBorderPainted(false);
        btnCustMobileNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustMobileNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn2.png"))); // NOI18N
        btnCustMobileNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCustMobileNoActionPerformed(evt);
            }
        });

        btnCheckKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnCheckKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnCheckKOT.setMnemonic('k');
        btnCheckKOT.setText("<html>CHECK <br> KOT</html>");
        btnCheckKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCheckKOT.setMaximumSize(new java.awt.Dimension(103, 43));
        btnCheckKOT.setMinimumSize(new java.awt.Dimension(103, 43));
        btnCheckKOT.setPreferredSize(new java.awt.Dimension(103, 43));
        btnCheckKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn2.png"))); // NOI18N
        btnCheckKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCheckKOTActionPerformed(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn1.png"))); // NOI18N
        btnHome.setMnemonic('h');
        btnHome.setText("HOME");
        btnHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnHome.setBorderPainted(false);
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCmnBtn2.png"))); // NOI18N
        btnHome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeActionPerformed(evt);
            }
        });

        lblCustomerName1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName1.setText("Customer :");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelItemGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblCustomerName1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCheckKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCustMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnMakeBill, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(panelNavigation4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panelTableList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelNavigation4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelTableList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnMakeBill)
                                .addComponent(btnCustMobileNo))
                            .addComponent(btnCheckKOT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelItemGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustomerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked

    }//GEN-LAST:event_tblItemTableMouseClicked

    private void txtTableNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTableNoMouseClicked

	funTableNoTextFieldClicked(evt);
    }//GEN-LAST:event_txtTableNoMouseClicked

    private void txtTableNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTableNoKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    txtTableNo.setText(" ");
	    funCallTableSearch();
	}

	if (evt.getKeyCode() == KeyEvent.VK_ENTER)
	{
	    if (hmTable.containsKey(txtTableNo.getText().trim().toUpperCase()))
	    {
		funFillItemGrid(txtTableNo.getText().trim());
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Wrong table name!!!");
		txtTableNo.setText("");
	    }
	}
    }//GEN-LAST:event_txtTableNoKeyPressed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblModuleNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblModuleNameMouseClicked
    {//GEN-HEADEREND:event_lblModuleNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblModuleNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Make Bill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Make Bill");
    }//GEN-LAST:event_formWindowClosing

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHomeActionPerformed
    {//GEN-HEADEREND:event_btnHomeActionPerformed
	funHomeButtonClicked();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnMakeBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMakeBillActionPerformed
    {//GEN-HEADEREND:event_btnMakeBillActionPerformed
	funDoneButtonPressed();
    }//GEN-LAST:event_btnMakeBillActionPerformed

    private void btnMakeBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnMakeBillKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funDoneButtonPressed();
	}
    }//GEN-LAST:event_btnMakeBillKeyPressed

    private void btnCustMobileNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustMobileNoActionPerformed
	// TODO add your handling code here:
	funCustomerNoButtonClicked();
    }//GEN-LAST:event_btnCustMobileNoActionPerformed

    private void btnCheckKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckKOTActionPerformed
	// TODO add your handling code here:
	funCheckKOTButtonClicked();
    }//GEN-LAST:event_btnCheckKOTActionPerformed

    private void btnTable16ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable16ActionPerformed
    {//GEN-HEADEREND:event_btnTable16ActionPerformed

	funFillItemGrid(btnTable16.getText().trim());
    }//GEN-LAST:event_btnTable16ActionPerformed

    private void btnTable16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable16MouseClicked
    {//GEN-HEADEREND:event_btnTable16MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable16MouseClicked

    private void btnTable15ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable15ActionPerformed
    {//GEN-HEADEREND:event_btnTable15ActionPerformed

	funFillItemGrid(btnTable15.getText().trim());
    }//GEN-LAST:event_btnTable15ActionPerformed

    private void btnTable15MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable15MouseClicked
    {//GEN-HEADEREND:event_btnTable15MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable15MouseClicked

    private void btnTable14ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable14ActionPerformed
    {//GEN-HEADEREND:event_btnTable14ActionPerformed

	funFillItemGrid(btnTable14.getText().trim());
    }//GEN-LAST:event_btnTable14ActionPerformed

    private void btnTable14MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable14MouseClicked
    {//GEN-HEADEREND:event_btnTable14MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable14MouseClicked

    private void btnTable13ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable13ActionPerformed
    {//GEN-HEADEREND:event_btnTable13ActionPerformed

	funFillItemGrid(btnTable13.getText().trim());
    }//GEN-LAST:event_btnTable13ActionPerformed

    private void btnTable13MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable13MouseClicked
    {//GEN-HEADEREND:event_btnTable13MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable13MouseClicked

    private void btnTable12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable12ActionPerformed
    {//GEN-HEADEREND:event_btnTable12ActionPerformed

	funFillItemGrid(btnTable12.getText().trim());
    }//GEN-LAST:event_btnTable12ActionPerformed

    private void btnTable12MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable12MouseClicked
    {//GEN-HEADEREND:event_btnTable12MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable12MouseClicked

    private void btnTable11ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable11ActionPerformed
    {//GEN-HEADEREND:event_btnTable11ActionPerformed

	funFillItemGrid(btnTable11.getText().trim());
    }//GEN-LAST:event_btnTable11ActionPerformed

    private void btnTable11MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable11MouseClicked
    {//GEN-HEADEREND:event_btnTable11MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable11MouseClicked

    private void btnTable10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable10ActionPerformed
    {//GEN-HEADEREND:event_btnTable10ActionPerformed

	funFillItemGrid(btnTable10.getText().trim());
    }//GEN-LAST:event_btnTable10ActionPerformed

    private void btnTable10MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable10MouseClicked
    {//GEN-HEADEREND:event_btnTable10MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable10MouseClicked

    private void btnTable9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable9ActionPerformed
    {//GEN-HEADEREND:event_btnTable9ActionPerformed

	funFillItemGrid(btnTable9.getText().trim());
    }//GEN-LAST:event_btnTable9ActionPerformed

    private void btnTable9MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable9MouseClicked
    {//GEN-HEADEREND:event_btnTable9MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable9MouseClicked

    private void btnTable8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable8ActionPerformed
    {//GEN-HEADEREND:event_btnTable8ActionPerformed

	funFillItemGrid(btnTable8.getText().trim());
    }//GEN-LAST:event_btnTable8ActionPerformed

    private void btnTable8MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable8MouseClicked
    {//GEN-HEADEREND:event_btnTable8MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable8MouseClicked

    private void btnTable7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable7ActionPerformed
    {//GEN-HEADEREND:event_btnTable7ActionPerformed

	funFillItemGrid(btnTable7.getText().trim());
    }//GEN-LAST:event_btnTable7ActionPerformed

    private void btnTable7MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable7MouseClicked
    {//GEN-HEADEREND:event_btnTable7MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable7MouseClicked

    private void btnTable6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable6ActionPerformed
    {//GEN-HEADEREND:event_btnTable6ActionPerformed

	funFillItemGrid(btnTable6.getText().trim());
    }//GEN-LAST:event_btnTable6ActionPerformed

    private void btnTable6MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable6MouseClicked
    {//GEN-HEADEREND:event_btnTable6MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable6MouseClicked

    private void btnTable5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable5ActionPerformed
    {//GEN-HEADEREND:event_btnTable5ActionPerformed

	funFillItemGrid(btnTable5.getText().trim());
    }//GEN-LAST:event_btnTable5ActionPerformed

    private void btnTable5MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable5MouseClicked
    {//GEN-HEADEREND:event_btnTable5MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable5MouseClicked

    private void btnTable4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable4ActionPerformed
    {//GEN-HEADEREND:event_btnTable4ActionPerformed

	funFillItemGrid(btnTable4.getText().trim());
    }//GEN-LAST:event_btnTable4ActionPerformed

    private void btnTable4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable4MouseClicked
    {//GEN-HEADEREND:event_btnTable4MouseClicked
	// TODO add your handling code here:itemName=btnIItem1.getText();
    }//GEN-LAST:event_btnTable4MouseClicked

    private void btnTable3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable3ActionPerformed
    {//GEN-HEADEREND:event_btnTable3ActionPerformed

	funFillItemGrid(btnTable3.getText().trim());
    }//GEN-LAST:event_btnTable3ActionPerformed

    private void btnTable3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable3MouseClicked
    {//GEN-HEADEREND:event_btnTable3MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTable3MouseClicked

    private void btnTable1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable1ActionPerformed
    {//GEN-HEADEREND:event_btnTable1ActionPerformed

	funFillItemGrid(btnTable1.getText().trim());
    }//GEN-LAST:event_btnTable1ActionPerformed

    private void btnTable2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTable2ActionPerformed
    {//GEN-HEADEREND:event_btnTable2ActionPerformed

	funFillItemGrid(btnTable2.getText().trim());
    }//GEN-LAST:event_btnTable2ActionPerformed

    private void btnTable2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTable2MouseClicked
    {//GEN-HEADEREND:event_btnTable2MouseClicked

    }//GEN-LAST:event_btnTable2MouseClicked

    private void btnNextItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextItemActionPerformed
    {//GEN-HEADEREND:event_btnNextItemActionPerformed
	// TODO add your handling code here:
	funNextTableClick();
    }//GEN-LAST:event_btnNextItemActionPerformed

    private void btnNextItemMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNextItemMouseClicked
    {//GEN-HEADEREND:event_btnNextItemMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnNextItemMouseClicked

    private void btnPrevItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrevItemActionPerformed
    {//GEN-HEADEREND:event_btnPrevItemActionPerformed
	// TODO add your handling code here:
	funPreviousTableClick();
    }//GEN-LAST:event_btnPrevItemActionPerformed

    private void panelAreaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_panelAreaMouseClicked
    {//GEN-HEADEREND:event_panelAreaMouseClicked

	objUtility.funCallForSearchForm("AreaMasterForMakeKOT");
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetArea(data[0].toString());
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }//GEN-LAST:event_panelAreaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckKOT;
    private javax.swing.JButton btnCustMobileNo;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnMakeBill;
    private javax.swing.JButton btnNextItem;
    private javax.swing.JButton btnPrevItem;
    private javax.swing.JButton btnTable1;
    private javax.swing.JButton btnTable10;
    private javax.swing.JButton btnTable11;
    private javax.swing.JButton btnTable12;
    private javax.swing.JButton btnTable13;
    private javax.swing.JButton btnTable14;
    private javax.swing.JButton btnTable15;
    private javax.swing.JButton btnTable16;
    private javax.swing.JButton btnTable2;
    private javax.swing.JButton btnTable3;
    private javax.swing.JButton btnTable4;
    private javax.swing.JButton btnTable5;
    private javax.swing.JButton btnTable6;
    private javax.swing.JButton btnTable7;
    private javax.swing.JButton btnTable8;
    private javax.swing.JButton btnTable9;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel labelPaxNo;
    private javax.swing.JLabel labelWaiterName;
    private javax.swing.JLabel lblAreaName4;
    public static javax.swing.JLabel lblCustomerName;
    public static javax.swing.JLabel lblCustomerName1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPAX;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTbl;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWaiterName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelArea4;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemGrid;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelNavigation4;
    private javax.swing.JPanel panelTableList;
    private javax.swing.JScrollPane scrItemGrid;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTextField txtTableNo;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the objData
     */
    public clsCustomerDataModelForSQY getObjData()
    {
	return objData;
    }
    
    private boolean funAddAllKOTSOnTableToBill(String tableNoForAddKOTToBill){
	try
	{

	    frmAddKOTToBill objAddKOTToBill = new frmAddKOTToBill("AddItemsToBillForSameTable", this);

	    String sqlLastBill = "select a.strBillNo,a.strPOSCode,a.strSettelmentMode,a.strTableNo,b.strTableName,b.strStatus,b.strAreaCode "
		    + "from tblbillhd a,tbltablemaster b "
		    + "where a.strTableNo=b.strTableNo "
		    + "and a.strTableNo='" + tableNoForAddKOTToBill + "' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + "and a.strSettelmentMode='' "
		    + "order by a.dteBillDate desc "
		    + "limit 1 ";
	    ResultSet rsLastBilledTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastBill);
	    if (rsLastBilledTable.next())
	    {

		String lastBilledBillNo = rsLastBilledTable.getString(1);
		String billNo = lastBilledBillNo;
		String areaCode = clsGlobalVarClass.gAreaCodeForTrans;
		areaCode = rsLastBilledTable.getString(7);
		String tableNo = rsLastBilledTable.getString(4);

		List<String> listSelectedKOTs = new ArrayList<String>();

		String sqlKOTs = "select a.strKOTNo  "
			+ "from tblitemrtemp a "
			+ "where a.strTableNo='" + tableNoForAddKOTToBill + "'  "
			+ "and a.tdhComboItemYN='N'  "
			+ "and a.strNCKotYN='N' "
			+ "group by a.strKOTNo";
		ResultSet rsKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sqlKOTs);
		while (rsKOTs.next())
		{
		    listSelectedKOTs.add(rsKOTs.getString(1));
		}
		rsKOTs.close();

		StringBuilder kots = new StringBuilder("(");
		for (int i = 0; i < listSelectedKOTs.size(); i++)
		{
		    if (i == 0)
		    {
			kots.append("'" + listSelectedKOTs.get(i) + "'");
		    }
		    else
		    {
			kots.append(",'" + listSelectedKOTs.get(i) + "'");
		    }
		}
		kots.append(")");

		objAddKOTToBill.setListSelectedKOTs(listSelectedKOTs);
		objAddKOTToBill.flgMergeKOTToBill=true;
		clsGlobalVarClass.gTransactionType = "AddKOTToBill";
		String kotNo = "";

		new frmBillSettlement(objAddKOTToBill, billNo, areaCode, kotNo, tableNo).setVisible(true);
	    }
	    else{
		return false;
	    }
	    rsLastBilledTable.close();
	   
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	 return true;
    }


}
