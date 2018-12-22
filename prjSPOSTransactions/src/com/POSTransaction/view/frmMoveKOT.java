/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmMoveKOT extends javax.swing.JFrame
{

    public Vector vTableNo, vTableName, vOpenKOTNo, vOpenTableNo, vOpenTableName;
    public String sql, toTableNo, toTableName, fromKOTNo, fromTableNo, fromTableName;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    private JButton[] btnTableArray;
    private clsUtility objUtility;
    private Map<String, String> mapBusyTablesNameWithCodes;

    public frmMoveKOT()
    {
	initComponents();
	try
	{

	    objUtility = new clsUtility();
	    vTableNo = new Vector();
	    vTableName = new Vector();
	    vOpenTableNo = new Vector();
	    vOpenTableName = new Vector();
	    vOpenKOTNo = new Vector();
	    btnPrevious.setEnabled(false);
	    btnPrevious1.setEnabled(false);
	    cntNavigate = 0;
	    cntNavigate1 = 0;
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    Date date1 = new Date();
	    String new_str = String.format("%tr", date1);
	    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
	    lblDate.setText(dateAndTime);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    mapBusyTablesNameWithCodes = new HashMap();

	    if (clsGlobalVarClass.gMoveKOTToOtherPOS)
	    {
		funFillTableVector("All", "");
	    }
	    else
	    {
		funFillTableVector(clsGlobalVarClass.gPOSCode, "");
	    }
	    funFillOpenKOTVector("All");
	    funFillBusyTables();
	    funFillPOSCombo();

	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("Super"))
	    {
		cmbPOS.setEnabled(true);
		txtSearchBusyTables.setEnabled(true);
	    }
	    else
	    {
		cmbPOS.setEnabled(false);
		//cmbTable.setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillBusyTables() throws Exception
    {

	mapBusyTablesNameWithCodes.put("All", "All");
	sql = "select strTableNo,strTableName from tbltablemaster "
		+ "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and strOperational='Y' "
		+ "order by intSequence ";
	ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsTable.next())
	{
	    mapBusyTablesNameWithCodes.put(rsTable.getString(2), rsTable.getString(1));
	}
	rsTable.close();

    }

    private void funFillPOSCombo() throws Exception
    {
	cmbPOS.addItem("Select POS");
	cmbPOS.addItem("All                                                                All");
	sql = "select strPOSCode,strPOSName from tblposmaster ";
	ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsPOS.next())
	{
	    cmbPOS.addItem(rsPOS.getString(2) + "                                                                " + rsPOS.getString(1));
	    if (clsGlobalVarClass.gPOSCode.equals(rsPOS.getString(1)))
	    {
		cmbPOS.setSelectedItem(rsPOS.getString(2) + "                                                                " + rsPOS.getString(1));
	    }
	}
	rsPOS.close();
	funPOSComboSelected(cmbPOS.getSelectedItem().toString());
	funFillOpenKOTVector("All");
    }

    private void funFillTableVector(String posCode, String searchAllTables)
    {
	try
	{
	    cntNavigate = 0;
	    cntNavigate1 = 0;
	    btnPrevious1.setEnabled(false);
	    btnNext1.setEnabled(true);

	    vTableNo.removeAllElements();
	    vTableName.removeAllElements();

	    sql = "select strTableNo,strTableName from tbltablemaster "
		    + " where strOperational='Y' ";
	    if (!posCode.equals("All"))
	    {
		sql += " and strPOSCode='" + posCode + "' ";
		if (searchAllTables.trim().length() > 0)
		{
		    sql += " and strTableName like '" + searchAllTables + "%' ";
		}
	    }
	    else
	    {
		if (searchAllTables.trim().length() > 0)
		{
		    sql += " and strTableName like '" + searchAllTables + "%' ";
		}
	    }
	    sql += " order by intSequence ;";
	    ResultSet rsTblNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTblNo.next())
	    {
		vTableNo.add(rsTblNo.getString(1));
		vTableName.add(rsTblNo.getString(2));
	    }
	    funLoadTables(0, vTableNo.size());
	    if (vTableNo.size() < 16)
	    {
		btnNext1.setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillOpenKOTVector(String tableNo)
    {
	try
	{
	    btnPrevious.setEnabled(false);
	    btnNext.setEnabled(true);
	    vOpenKOTNo.removeAllElements();
	    vOpenTableNo.removeAllElements();

	    String POSCode = null;
	    if (cmbPOS.getSelectedIndex() > 0)
	    {
		String POSName = cmbPOS.getSelectedItem().toString();
		StringBuilder sb = new StringBuilder(POSName);
		POSCode = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
		sb = null;
	    }

	    sql = "select distinct(strKOTNo),strTableNo from tblitemrtemp ";
	    if (!tableNo.equals("All"))
	    {
		if (null == POSCode || POSCode.equalsIgnoreCase("All"))
		{
		    sql += " where strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strNCKOTYN='N' ";
		}
		else
		{
		    sql += " where strTableNo='" + tableNo + "' and strPOSCode='" + POSCode + "' and strNCKOTYN='N'  ";
		}
	    }
	    else
	    {
		if (null == POSCode || POSCode.equalsIgnoreCase("All"))
		{
		    sql += " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strNCKOTYN='N' ";
		}
		else
		{
		    sql += " where strPOSCode='" + POSCode + "' and strNCKOTYN='N' ";
		}
	    }
	    System.out.println(sql);
	    ResultSet rsKOTNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsKOTNo.next())
	    {
		vOpenKOTNo.add(rsKOTNo.getString(1));
		vOpenTableNo.add(rsKOTNo.getString(2));
	    }
	    funLoadOpenKOTs(0, vOpenKOTNo.size());

	    if (vOpenKOTNo.size() <= 16)
	    {
		btnNext.setEnabled(false);
	    }

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

	    btnTableArray = new JButton[]
	    {
		btnTableNo1, btnTableNo2, btnTableNo3, btnTableNo4, btnTableNo5, btnTableNo6, btnTableNo7, btnTableNo8, btnTableNo9, btnTableNo10, btnTableNo11, btnTableNo12, btnTableNo13, btnTableNo14, btnTableNo15, btnTableNo16
	    };
	    for (int k = 0; k < btnTableArray.length; k++)
	    {
		btnTableArray[k].setForeground(Color.black);
		btnTableArray[k].setText("");
	    }
	    for (int i = startIndex; i < totalSize; i++)
	    {
		if (i == vTableNo.size())
		{
		    break;
		}
		String tblName = vTableName.elementAt(i).toString();
		sql = "select strTableNo,strStatus from tbltablemaster where strTableNo='" + vTableNo.elementAt(i).toString() + "'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rs.next();
		String status = rs.getString(2);
		int pax = 0;
		if (cntIndex < 16)
		{
		    if (status.equals("Occupied"))
		    {
			sql = "select intPaxNo from tblitemrtemp where strTableNo='" + vTableNo.elementAt(i).toString() + "' ";
			ResultSet tblRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (tblRs.next())
			{
			    pax = tblRs.getInt(1);
			}
		    }
		    if (tblName.contains(" "))
		    {
			StringBuilder sb = new StringBuilder(tblName);
			int len = sb.length();
			int seq = sb.lastIndexOf(" ");
			String split = sb.substring(0, seq);
			String last = sb.substring(seq + 1, len);

			if (status.equals("Occupied"))
			{
			    btnTableArray[cntIndex].setForeground(Color.white);
			    btnTableArray[cntIndex].setBackground(Color.red);
			}
			else if (status.equals("Billed"))
			{
			    btnTableArray[cntIndex].setForeground(Color.black);
			    btnTableArray[cntIndex].setBackground(Color.lightGray);
			}
			else if (status.equals("Normal"))
			{
			    btnTableArray[cntIndex].setForeground(Color.black);
			    btnTableArray[cntIndex].setBackground(Color.lightGray);
			}
			btnTableArray[cntIndex].setText("<html>" + split + "<br>" + last + "<br>" + pax + "</html>");
		    }
		    else
		    {
			if (status.equals("Occupied"))
			{
			    btnTableArray[cntIndex].setForeground(Color.white);
			    btnTableArray[cntIndex].setBackground(Color.red);
			}
			else if (status.equals("Billed"))
			{
			    btnTableArray[cntIndex].setForeground(Color.black);
			    btnTableArray[cntIndex].setBackground(Color.lightGray);
			}
			else if (status.equals("Normal"))
			{
			    btnTableArray[cntIndex].setForeground(Color.black);
			    btnTableArray[cntIndex].setBackground(Color.lightGray);
			}
			btnTableArray[cntIndex].setText("<html>" + tblName + "<br>" + pax + "</html>");
		    }
		    btnTableArray[cntIndex].setEnabled(true);
		    cntIndex++;
		}
	    }
	    for (int j = cntIndex; j < 16; j++)
	    {
		btnTableArray[j].setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadOpenKOTs(int startIndex, int totalSize)
    {
	try
	{
	    int cntIndex = 0;
	    JButton[] btnTableArray =
	    {
		btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
	    };
	    for (int k = 0; k < btnTableArray.length; k++)
	    {
		btnTableArray[k].setForeground(Color.black);
		btnTableArray[k].setBackground(Color.LIGHT_GRAY);
		btnTableArray[k].setText("");
	    }
	    for (int i = startIndex; i < totalSize; i++)
	    {
		if (i == vOpenKOTNo.size())
		{
		    break;
		}
		String kotNo = vOpenKOTNo.elementAt(i).toString();

		if (cntIndex < 16)
		{
		    btnTableArray[cntIndex].setText(kotNo);
		    btnTableArray[cntIndex].setEnabled(true);
		    cntIndex++;
		}
	    }
	    for (int j = cntIndex; j < 16; j++)
	    {
		btnTableArray[j].setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSelectTable(String tableName, int index)
    {
	try
	{
	    if (tableName.trim().length() > 0)
	    {
		System.out.println(cntNavigate1);
		int currentIndex = (16 * cntNavigate1) + index;
		System.out.println(currentIndex);
		lblToTableName.setText(vTableName.elementAt(currentIndex).toString());
		toTableNo = vTableNo.elementAt(currentIndex).toString();
		toTableName = vTableName.elementAt(currentIndex).toString();
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSelectOpenTable(String kotNo, int index)
    {
	try
	{
	    if (kotNo.trim().length() > 0)
	    {
		int kotIndex = vOpenKOTNo.indexOf(kotNo);
		fromKOTNo = kotNo;
		fromTableNo = vOpenTableNo.elementAt(kotIndex).toString();

		sql = "select strTableName from tbltablemaster where strTableNo='" + fromTableNo + "'";
		ResultSet rsTableNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsTableNo.next())
		{
		    fromTableName = rsTableNo.getString(1);
		    lblOpenTableName.setText(rsTableNo.getString(1));
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean validateTables()
    {
	boolean flgValidate = true;
	try
	{
	    if (toTableName == null || toTableName.trim().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Select table from All Tables");
		flgValidate = false;
	    }
	    else if (fromKOTNo == null || fromKOTNo.trim().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Select table from Open Tables");
		flgValidate = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return flgValidate;
    }

    private String funGetReasonCode()
    {
	String reasonCode = "", reasonName = "";
	try
	{
	    int reasoncount = 0, i = 0;
	    sql = "select count(strReasonName) from tblreasonmaster where strMoveKOT='Y'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		reasoncount = rs.getInt(1);
	    }
	    if (reasoncount > 0)
	    {
		String[] arrReason = new String[reasoncount];
		sql = "select strReasonName from tblreasonmaster where strMoveKOT='Y'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		i = 0;
		while (rs.next())
		{
		    arrReason[i] = rs.getString(1);
		    i++;
		}
		reasonName = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.PLAIN_MESSAGE, null, arrReason, arrReason[0]);

	    }
	    else
	    {
		new frmOkPopUp(this, "Please Create Reason", "Warning", 1).setVisible(true);
	    }

	    if (null != reasonName && !reasonName.trim().isEmpty())
	    {
		sql = "select strReasonCode from tblreasonmaster where strReasonName='" + reasonName + "'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rs.next();
		reasonCode = rs.getString("strReasonCode");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return reasonCode;
	}
    }

    private void funMoveKOT()
    {
	try
	{
	    String reasonCode = funGetReasonCode();

	    if (reasonCode == null || reasonCode.trim().isEmpty())
	    {
		JOptionPane.showMessageDialog(null, "Please create reason for move kot!!!");
		return;
	    }

	    String createdDate = "";
	    List<clsMakeKotItemDtl> listOfItemsForFromTable = new ArrayList<clsMakeKotItemDtl>();

	    String fromTableName = "";

	    sql = "select strStatus,intPaxNo,strTableName "
		    + " from tbltablemaster "
		    + " where strTableNo='" + fromTableNo + "'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    int pax = 0;
	    if (rs.next())
	    {
		String status = rs.getString(1);
		fromTableName = rs.getString(3);
		pax = rs.getInt(2);
		sql = "update tbltablemaster set strStatus='" + status + "',intPaxNo=" + pax + " "
			+ " where strTableNo='" + toTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    rs.close();

	    String remarks = "Shifted from " + fromTableName + " To " + toTableName + ".";

	    sql = "select a.strItemCode,a.strItemName,sum(a.dblItemQuantity),sum(a.dblAmount),dteDateCreated,a.strWaiterNo "
		    + "from tblitemrtemp a "
		    + "where strKOTNo='" + fromKOTNo + "' "
		    + "group by a.strItemCode ";
	    ResultSet rsFromTableItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    String itemCode = "", itemName = "", waiterNo = "";
	    String strType = "MVKot", voidedDate = funGetVodidedDate();
	    double quantity = 0.0, amount = 0.0;
	    while (rsFromTableItems.next())
	    {
		clsMakeKotItemDtl objKotItemDtl = new clsMakeKotItemDtl();
		objKotItemDtl.setItemCode(rsFromTableItems.getString(1));
		itemCode = rsFromTableItems.getString(1);
		objKotItemDtl.setItemName(rsFromTableItems.getString(2));
		itemName = rsFromTableItems.getString(2);
		objKotItemDtl.setQty(rsFromTableItems.getDouble(3));
		quantity = rsFromTableItems.getDouble(3);
		objKotItemDtl.setAmt(rsFromTableItems.getDouble(4));
		amount = rsFromTableItems.getDouble(4);
		createdDate = rsFromTableItems.getString(5);
		waiterNo = rsFromTableItems.getString(6);

		listOfItemsForFromTable.add(objKotItemDtl);

		String insertQuery = "insert into tblvoidkot(strTableNo,strPOSCode,strItemCode, "
			+ " strItemName,dblItemQuantity,dblAmount,strWaiterNo,strKOTNo,intPaxNo,strType,strReasonCode, "
			+ " strUserCreated,dteDateCreated,dteVoidedDate,strClientCode,strRemark,strVoidBillType ) "
			+ " values ";

		insertQuery += "('" + toTableNo + "','" + clsGlobalVarClass.gPOSCode + "','" + itemCode + "','" + itemName + "',"
			+ "'" + quantity + "','" + amount + "','" + waiterNo + "','" + fromKOTNo + "','" + pax + "','" + strType + "'"
			+ ",'" + reasonCode + "','" + clsGlobalVarClass.gUserCode + "','" + createdDate + "','" + voidedDate + "'"
			+ ",'" + clsGlobalVarClass.gClientCode + "','" + remarks + "','Move KOT') ";
		clsGlobalVarClass.dbMysql.execute(insertQuery);
	    }
	    rsFromTableItems.close();

	    sql = "update tblitemrtemp set strTableNo='" + toTableNo + "' "
		    + "where strKOTNo='" + fromKOTNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tblkottaxdtl set strTableNo='" + toTableNo + "' "
		    + "where strKOTNo='" + fromKOTNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "select strPOSCode from tbltablemaster "
		    + " where strTableNo='" + toTableNo + "'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		String posCode = rs.getString(1);
		sql = "update tblitemrtemp set strPOSCode='" + posCode + "' "
			+ " where strKOTNo='" + fromKOTNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    rs.close();

	    sql = "select strKOTNo,dteDateCreated from tblitemrtemp where strTableNo='" + fromTableNo + "' and strNCKotYN='N' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    if (!rs.next())
	    {
		sql = "update tbltablemaster set strStatus='Normal',intPaxNo=0 "
			+ "where strTableNo='" + fromTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	    //insert into itemrtempbck tabl
	    objUtility.funInsertIntoTblItemRTempBck(fromTableNo);
	    objUtility.funInsertIntoTblItemRTempBck(toTableNo);

	    JOptionPane.showMessageDialog(this, fromKOTNo + " Shifted to " + toTableName);

	    //send message to all cost centers 
	   if(clsGlobalVarClass.gPrintMoveTableMoveKOT)
	   {
	       funSendMessageToCostCenters(listOfItemsForFromTable);
	   }

	    funFillTableVector("All", "");
	    funFillOpenKOTVector("All");
	    funClearFields();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funClearFields()
    {
	toTableName = "";
	lblToTableName.setText("");
	lblOpenTableName.setText("");
	txtSearchBusyTables.setText("All");
	cmbPOS.setSelectedIndex(0);
    }

    private void funSetDefaultColorAll(int btnIndex)
    {
	try
	{
	    JButton[] btnTableArray =
	    {
		btnTableNo1, btnTableNo2, btnTableNo3, btnTableNo4, btnTableNo5, btnTableNo6, btnTableNo7, btnTableNo8, btnTableNo9, btnTableNo10, btnTableNo11, btnTableNo12, btnTableNo13, btnTableNo14, btnTableNo15, btnTableNo16
	    };
	    Color btnColor = btnTableArray[btnIndex].getBackground();
//            if (btnColor != Color.black)
//            {
	    btnTableArray[btnIndex].setBackground(Color.red);
//                for (int cnt = 0; cnt < btnTableArray.length; cnt++)
//                {
//                    if (cnt != btnIndex)
//                    {
//                        btnTableArray[cnt].setBackground(btnColor);
//                    }
//                }
//            }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetDefaultColorOpen(int btnIndex)
    {
	try
	{
	    JButton[] btnTableArray =
	    {
		btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
	    };
	    Color btnColor = btnTableArray[btnIndex].getBackground();
	    if (btnColor != Color.black)
	    {
		btnTableArray[btnIndex].setBackground(Color.black);
		for (int cnt = 0; cnt < btnTableArray.length; cnt++)
		{
		    if (cnt != btnIndex)
		    {
			btnTableArray[cnt].setBackground(btnColor);
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funPreviousBtnClick()
    {
	try
	{
	    cntNavigate1--;
	    btnNext1.setEnabled(true);
	    if (cntNavigate1 == 0)
	    {
		btnPrevious1.setEnabled(false);
		funLoadTables(0, vTableNo.size());
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

    private void funTableComboSelected(String tableName)
    {
	StringBuilder sb = new StringBuilder(tableName);
	String tableNo = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
	sb = null;
	funFillOpenKOTVector(tableNo);
    }

    private void funPOSComboSelected(String POSName)
    {
	StringBuilder sb = new StringBuilder(POSName);
	String POSCode = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
	sb = null;
	funFillTableVector(POSCode, "");
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
        panelMainForm = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelAllTables = new javax.swing.JPanel();
        btnTableNo2 = new javax.swing.JButton();
        btnTableNo1 = new javax.swing.JButton();
        btnTableNo3 = new javax.swing.JButton();
        btnTableNo4 = new javax.swing.JButton();
        btnTableNo5 = new javax.swing.JButton();
        btnTableNo6 = new javax.swing.JButton();
        btnTableNo7 = new javax.swing.JButton();
        btnTableNo8 = new javax.swing.JButton();
        btnTableNo9 = new javax.swing.JButton();
        btnTableNo10 = new javax.swing.JButton();
        btnTableNo11 = new javax.swing.JButton();
        btnTableNo12 = new javax.swing.JButton();
        btnTableNo13 = new javax.swing.JButton();
        btnTableNo14 = new javax.swing.JButton();
        btnTableNo15 = new javax.swing.JButton();
        btnTableNo16 = new javax.swing.JButton();
        panelOpenTable = new javax.swing.JPanel();
        btnOpenTable2 = new javax.swing.JButton();
        btnOpenTable1 = new javax.swing.JButton();
        btnOpenTable3 = new javax.swing.JButton();
        btnOpenTable4 = new javax.swing.JButton();
        btnOpenTable5 = new javax.swing.JButton();
        btnOpenTable6 = new javax.swing.JButton();
        btnOpenTable7 = new javax.swing.JButton();
        btnOpenTable8 = new javax.swing.JButton();
        btnOpenTable9 = new javax.swing.JButton();
        btnOpenTable10 = new javax.swing.JButton();
        btnOpenTable11 = new javax.swing.JButton();
        btnOpenTable12 = new javax.swing.JButton();
        btnOpenTable13 = new javax.swing.JButton();
        btnOpenTable14 = new javax.swing.JButton();
        btnOpenTable15 = new javax.swing.JButton();
        btnOpenTable16 = new javax.swing.JButton();
        lblOpenTable = new javax.swing.JLabel();
        lblOpenTable1 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious1 = new javax.swing.JButton();
        btnNext1 = new javax.swing.JButton();
        lblOpenTableName = new javax.swing.JLabel();
        lblToTableName = new javax.swing.JLabel();
        btnShowKOT = new javax.swing.JButton();
        cmbPOS = new javax.swing.JComboBox();
        txtSearchAllTables = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        txtSearchBusyTables = new javax.swing.JTextField();
        lblSearch1 = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS ");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Move KOT");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelAllTables.setBackground(new java.awt.Color(255, 255, 255));
        panelAllTables.setEnabled(false);
        panelAllTables.setOpaque(false);

        btnTableNo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo2MouseClicked(evt);
            }
        });

        btnTableNo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo1MouseClicked(evt);
            }
        });

        btnTableNo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo3MouseClicked(evt);
            }
        });

        btnTableNo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo4MouseClicked(evt);
            }
        });

        btnTableNo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo5MouseClicked(evt);
            }
        });

        btnTableNo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo6MouseClicked(evt);
            }
        });

        btnTableNo7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo7MouseClicked(evt);
            }
        });

        btnTableNo8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo8MouseClicked(evt);
            }
        });

        btnTableNo9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo9MouseClicked(evt);
            }
        });

        btnTableNo10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo10MouseClicked(evt);
            }
        });

        btnTableNo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo11MouseClicked(evt);
            }
        });

        btnTableNo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo12MouseClicked(evt);
            }
        });

        btnTableNo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo13MouseClicked(evt);
            }
        });

        btnTableNo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo14MouseClicked(evt);
            }
        });

        btnTableNo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo15MouseClicked(evt);
            }
        });

        btnTableNo16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelAllTablesLayout = new javax.swing.GroupLayout(panelAllTables);
        panelAllTables.setLayout(panelAllTablesLayout);
        panelAllTablesLayout.setHorizontalGroup(
            panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addComponent(btnTableNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelAllTablesLayout.setVerticalGroup(
            panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTableNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTableNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAllTablesLayout.createSequentialGroup()
                            .addComponent(btnTableNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnTableNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);
        panelOpenTable.setOpaque(false);

        btnOpenTable2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable2MouseClicked(evt);
            }
        });

        btnOpenTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable1MouseClicked(evt);
            }
        });

        btnOpenTable3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable3MouseClicked(evt);
            }
        });

        btnOpenTable4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable4MouseClicked(evt);
            }
        });

        btnOpenTable5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable5MouseClicked(evt);
            }
        });

        btnOpenTable6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable6MouseClicked(evt);
            }
        });

        btnOpenTable7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable7MouseClicked(evt);
            }
        });

        btnOpenTable8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable8MouseClicked(evt);
            }
        });

        btnOpenTable9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable9MouseClicked(evt);
            }
        });

        btnOpenTable10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable10MouseClicked(evt);
            }
        });

        btnOpenTable11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable11MouseClicked(evt);
            }
        });

        btnOpenTable12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable12MouseClicked(evt);
            }
        });

        btnOpenTable13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable13MouseClicked(evt);
            }
        });

        btnOpenTable14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable14MouseClicked(evt);
            }
        });

        btnOpenTable15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable15MouseClicked(evt);
            }
        });

        btnOpenTable16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnOpenTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelOpenTableLayout = new javax.swing.GroupLayout(panelOpenTable);
        panelOpenTable.setLayout(panelOpenTableLayout);
        panelOpenTableLayout.setHorizontalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelOpenTableLayout.setVerticalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelOpenTableLayout.createSequentialGroup()
                            .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblOpenTable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblOpenTable.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable.setText("OPEN KOTs");

        lblOpenTable1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblOpenTable1.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable1.setText("ALL TABLES");

        btnSave.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious.setText("<<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext.setText(">>>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrevious1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnPrevious1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious1.setText("<<<");
        btnPrevious1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevious1ActionPerformed(evt);
            }
        });

        btnNext1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext1.setText(">>>");
        btnNext1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext1ActionPerformed(evt);
            }
        });

        btnShowKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnShowKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnShowKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnShowKOT.setText("VIEW KOT");
        btnShowKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnShowKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowKOTActionPerformed(evt);
            }
        });

        cmbPOS.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbPOS.setToolTipText("Select POS");
        cmbPOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOSActionPerformed(evt);
            }
        });

        txtSearchAllTables.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtSearchAllTables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchAllTablesMouseClicked(evt);
            }
        });
        txtSearchAllTables.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchAllTablesKeyReleased(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");

        txtSearchBusyTables.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtSearchBusyTables.setText("All");
        txtSearchBusyTables.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBusyTablesKeyReleased(evt);
            }
        });

        lblSearch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch1.setToolTipText("Search Menu");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormBodyLayout.createSequentialGroup()
                            .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(56, 56, 56)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormBodyLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(120, 120, 120)
                            .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblToTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106)
                        .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblOpenTable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOpenTable1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch)))
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblOpenTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(txtSearchBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTableNo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo2MouseClicked

	funSetDefaultColorAll(1);
	funSelectTable(btnTableNo2.getText(), 1);
    }//GEN-LAST:event_btnTableNo2MouseClicked

    private void btnTableNo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo1MouseClicked

	System.out.println(btnTableNo1.getBackground());
	funSetDefaultColorAll(0);
	funSelectTable(btnTableNo1.getText(), 0);
    }//GEN-LAST:event_btnTableNo1MouseClicked

    private void btnTableNo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo3MouseClicked
	funSetDefaultColorAll(2);
	funSelectTable(btnTableNo3.getText(), 2);
    }//GEN-LAST:event_btnTableNo3MouseClicked

    private void btnTableNo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo4MouseClicked

	funSetDefaultColorAll(3);
	funSelectTable(btnTableNo4.getText(), 3);
    }//GEN-LAST:event_btnTableNo4MouseClicked

    private void btnTableNo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo5MouseClicked

	funSetDefaultColorAll(4);
	funSelectTable(btnTableNo5.getText(), 4);
    }//GEN-LAST:event_btnTableNo5MouseClicked

    private void btnTableNo6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo6MouseClicked

	funSetDefaultColorAll(5);
	funSelectTable(btnTableNo6.getText(), 5);
    }//GEN-LAST:event_btnTableNo6MouseClicked

    private void btnTableNo7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo7MouseClicked

	funSetDefaultColorAll(6);
	funSelectTable(btnTableNo7.getText(), 6);
    }//GEN-LAST:event_btnTableNo7MouseClicked

    private void btnTableNo8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo8MouseClicked

	funSetDefaultColorAll(7);
	funSelectTable(btnTableNo8.getText(), 7);
    }//GEN-LAST:event_btnTableNo8MouseClicked

    private void btnTableNo9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo9MouseClicked

	funSetDefaultColorAll(8);
	funSelectTable(btnTableNo9.getText(), 8);
    }//GEN-LAST:event_btnTableNo9MouseClicked

    private void btnTableNo10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo10MouseClicked

	funSetDefaultColorAll(9);
	funSelectTable(btnTableNo10.getText(), 9);
    }//GEN-LAST:event_btnTableNo10MouseClicked

    private void btnTableNo11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo11MouseClicked

	funSetDefaultColorAll(10);
	funSelectTable(btnTableNo11.getText(), 10);
    }//GEN-LAST:event_btnTableNo11MouseClicked

    private void btnTableNo12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo12MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(11);
	funSelectTable(btnTableNo12.getText(), 11);
    }//GEN-LAST:event_btnTableNo12MouseClicked

    private void btnTableNo13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo13MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(12);
	funSelectTable(btnTableNo13.getText(), 12);
    }//GEN-LAST:event_btnTableNo13MouseClicked

    private void btnTableNo14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo14MouseClicked

	funSetDefaultColorAll(13);
	funSelectTable(btnTableNo14.getText(), 13);
    }//GEN-LAST:event_btnTableNo14MouseClicked

    private void btnTableNo15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo15MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(14);
	funSelectTable(btnTableNo15.getText(), 14);
    }//GEN-LAST:event_btnTableNo15MouseClicked

    private void btnTableNo16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo16MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(15);
	funSelectTable(btnTableNo16.getText(), 15);
    }//GEN-LAST:event_btnTableNo16MouseClicked

    private void btnOpenTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable2MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(1);

	funSelectOpenTable(btnOpenTable2.getText(), 1);
    }//GEN-LAST:event_btnOpenTable2MouseClicked

    private void btnOpenTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable1MouseClicked
	// TODO add your handling code here:
	if (btnOpenTable1.isEnabled())
	{
	    funSetDefaultColorOpen(0);
	    funSelectOpenTable(btnOpenTable1.getText(), 0);
	}
    }//GEN-LAST:event_btnOpenTable1MouseClicked

    private void btnOpenTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable3MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(2);
	funSelectOpenTable(btnOpenTable3.getText(), 2);
    }//GEN-LAST:event_btnOpenTable3MouseClicked

    private void btnOpenTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable4MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(3);
	funSelectOpenTable(btnOpenTable4.getText(), 3);
    }//GEN-LAST:event_btnOpenTable4MouseClicked

    private void btnOpenTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable5MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(4);
	funSelectOpenTable(btnOpenTable5.getText(), 4);
    }//GEN-LAST:event_btnOpenTable5MouseClicked

    private void btnOpenTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable6MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(5);
	funSelectOpenTable(btnOpenTable6.getText(), 5);
    }//GEN-LAST:event_btnOpenTable6MouseClicked

    private void btnOpenTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable7MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(6);
	funSelectOpenTable(btnOpenTable7.getText(), 6);
    }//GEN-LAST:event_btnOpenTable7MouseClicked

    private void btnOpenTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable8MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(7);
	funSelectOpenTable(btnOpenTable8.getText(), 7);
    }//GEN-LAST:event_btnOpenTable8MouseClicked

    private void btnOpenTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable9MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(8);
	funSelectOpenTable(btnOpenTable9.getText(), 8);
    }//GEN-LAST:event_btnOpenTable9MouseClicked

    private void btnOpenTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable10MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(9);
	funSelectOpenTable(btnOpenTable10.getText(), 9);
    }//GEN-LAST:event_btnOpenTable10MouseClicked

    private void btnOpenTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable11MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(10);
	funSelectOpenTable(btnOpenTable11.getText(), 10);
    }//GEN-LAST:event_btnOpenTable11MouseClicked

    private void btnOpenTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable12MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(11);
	funSelectOpenTable(btnOpenTable12.getText(), 11);
    }//GEN-LAST:event_btnOpenTable12MouseClicked

    private void btnOpenTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable13MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(12);
	funSelectOpenTable(btnOpenTable13.getText(), 12);
    }//GEN-LAST:event_btnOpenTable13MouseClicked

    private void btnOpenTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable14MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(13);
	funSelectOpenTable(btnOpenTable14.getText(), 13);
    }//GEN-LAST:event_btnOpenTable14MouseClicked

    private void btnOpenTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable15MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(14);
	funSelectOpenTable(btnOpenTable15.getText(), 14);
    }//GEN-LAST:event_btnOpenTable15MouseClicked

    private void btnOpenTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable16MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorOpen(15);
	funSelectOpenTable(btnOpenTable16.getText(), 15);
    }//GEN-LAST:event_btnOpenTable16MouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
	// TODO add your handling code here:
	try
	{
	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(toTableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table, table No = " + toTableName);
	    }
	    else if (validateTables())
	    {
		funMoveKOT();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Move KOT");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
	// TODO add your handling code here:
	try
	{
	    cntNavigate--;
	    btnNext.setEnabled(true);
	    if (cntNavigate == 0)
	    {
		btnPrevious.setEnabled(false);
		funLoadOpenKOTs(0, vOpenTableNo.size());
	    }
	    else
	    {
		int tableSize = cntNavigate * 16;
		int resMod = vOpenTableNo.size() % tableSize;
		int resDiv = vOpenTableNo.size() / tableSize;
		int totalSize = tableSize + 16;
		//System.out.println("Size="+vOpenTableNo.size()+"\tMod="+resMod+"\tdiv="+resDiv+"\tsss="+tableSize);
		funLoadOpenKOTs(tableSize, totalSize);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
	// TODO add your handling code here:
	try
	{
	    cntNavigate++;
	    int tableSize = cntNavigate * 16;
	    int resMod = vOpenTableNo.size() % tableSize;
	    int resDiv = vOpenTableNo.size() / tableSize;
	    int totalSize = tableSize + 16;
	    funLoadOpenKOTs(tableSize, totalSize);
	    btnPrevious.setEnabled(true);
	    if (resDiv == cntNavigate)
	    {
		btnNext.setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevious1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevious1ActionPerformed
	// TODO add your handling code here:
	funPreviousBtnClick();
    }//GEN-LAST:event_btnPrevious1ActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext1ActionPerformed
	// TODO add your handling code here:
	try
	{
	    cntNavigate1++;
	    int tableSize = cntNavigate1 * 16;
	    int resMod = vTableNo.size() % tableSize;
	    int resDiv = vTableNo.size() / 16;
	    int totalSize = tableSize + 16;
	    tblStartIndex = tableSize;
	    tblEndIndex = totalSize;
	    funLoadTables(tableSize, totalSize);
	    btnPrevious1.setEnabled(true);
	    if (resDiv == cntNavigate1)
	    {
		btnNext1.setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnShowKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowKOTActionPerformed
	boolean isButtonClick = false;
	JButton[] btnTableArray =
	{
	    btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
	};
	for (int cnt = 0; cnt < btnTableArray.length; cnt++)
	{
	    Color btnColor = btnTableArray[cnt].getBackground();
	    if (btnColor == Color.black)
	    {
		isButtonClick = true;
		break;
	    }
	}
	System.out.println("isButtonClicked=" + isButtonClick);
	if (!isButtonClick)
	{
	    JOptionPane.showMessageDialog(null, "Please Select KOT");
	    return;
	}
	else
	{
	    funShowKOT();
	}
    }//GEN-LAST:event_btnShowKOTActionPerformed

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOSActionPerformed
	// TODO add your handling code here:
	if (cmbPOS.getSelectedIndex() > 0)
	{
	    funPOSComboSelected(cmbPOS.getSelectedItem().toString());
	    funFillOpenKOTVector("All");
	}
    }//GEN-LAST:event_cmbPOSActionPerformed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

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

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Move KOT");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Move KOT");
    }//GEN-LAST:event_formWindowClosing

    private void txtSearchAllTablesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSearchAllTablesMouseClicked
    {//GEN-HEADEREND:event_txtSearchAllTablesMouseClicked
	// TODO add your handling code here:
	frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search All Tables");
	keyboard.setVisible(true);
	keyboard.setAlwaysOnTop(true);
	keyboard.setAutoRequestFocus(true);
	txtSearchAllTables.setText(clsGlobalVarClass.gKeyboardValue);

	funSearchAllTables(txtSearchAllTables.getText().trim());
    }//GEN-LAST:event_txtSearchAllTablesMouseClicked

    private void txtSearchAllTablesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchAllTablesKeyReleased
    {//GEN-HEADEREND:event_txtSearchAllTablesKeyReleased

	funSearchAllTables(txtSearchAllTables.getText().trim());
    }//GEN-LAST:event_txtSearchAllTablesKeyReleased

    private void txtSearchBusyTablesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchBusyTablesKeyReleased
    {//GEN-HEADEREND:event_txtSearchBusyTablesKeyReleased
	funSearchBusyTableClicked();
    }//GEN-LAST:event_txtSearchBusyTablesKeyReleased

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
	    java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveKOT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmMoveKOT().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnOpenTable1;
    private javax.swing.JButton btnOpenTable10;
    private javax.swing.JButton btnOpenTable11;
    private javax.swing.JButton btnOpenTable12;
    private javax.swing.JButton btnOpenTable13;
    private javax.swing.JButton btnOpenTable14;
    private javax.swing.JButton btnOpenTable15;
    private javax.swing.JButton btnOpenTable16;
    private javax.swing.JButton btnOpenTable2;
    private javax.swing.JButton btnOpenTable3;
    private javax.swing.JButton btnOpenTable4;
    private javax.swing.JButton btnOpenTable5;
    private javax.swing.JButton btnOpenTable6;
    private javax.swing.JButton btnOpenTable7;
    private javax.swing.JButton btnOpenTable8;
    private javax.swing.JButton btnOpenTable9;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrevious1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShowKOT;
    private javax.swing.JButton btnTableNo1;
    private javax.swing.JButton btnTableNo10;
    private javax.swing.JButton btnTableNo11;
    private javax.swing.JButton btnTableNo12;
    private javax.swing.JButton btnTableNo13;
    private javax.swing.JButton btnTableNo14;
    private javax.swing.JButton btnTableNo15;
    private javax.swing.JButton btnTableNo16;
    private javax.swing.JButton btnTableNo2;
    private javax.swing.JButton btnTableNo3;
    private javax.swing.JButton btnTableNo4;
    private javax.swing.JButton btnTableNo5;
    private javax.swing.JButton btnTableNo6;
    private javax.swing.JButton btnTableNo7;
    private javax.swing.JButton btnTableNo8;
    private javax.swing.JButton btnTableNo9;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTable;
    private javax.swing.JLabel lblOpenTable1;
    private javax.swing.JLabel lblOpenTableName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearch1;
    private javax.swing.JLabel lblToTableName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAllTables;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    private javax.swing.JTextField txtSearchAllTables;
    private javax.swing.JTextField txtSearchBusyTables;
    // End of variables declaration//GEN-END:variables

    private void funShowKOT()
    {
	try
	{
//            clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//            ob.funShowKOTUsingTextFile(fromTableNo, fromKOTNo, "", "Dina", "noReprint");

	    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
	    objKOTGeneration.funKOTGeneration(fromTableNo, fromKOTNo, "", "Reprint", "Dina", "N");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSearchAllTables(String searchAllTables)
    {
	if (clsGlobalVarClass.gMoveKOTToOtherPOS)
	{
	    funFillTableVector("All", searchAllTables);
	}
	else
	{
	    funFillTableVector(clsGlobalVarClass.gPOSCode, searchAllTables);
	}
    }

    private void funSendMessageToCostCenters(List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    String filename = (filePath + "/Temp/MoveKOT.txt");
	    File file = new File(filename);

	    funCreateTestTextFile(file, fromKOTNo, toTableName, listOfItemsForFromTable);

	    String sqlCostCenters = "select b.strCostCenterCode ,c.strCostCenterName,c.strPrinterPort,c.strSecondaryPrinterPort,c.strPrintOnBothPrinters "
		    + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
		    + "where a.strKOTNo='" + fromKOTNo + "' "
		    + "and a.strItemCode=b.strItemCode "
		    + "and a.strPOSCode=b.strPosCode "
		    + "and b.strCostCenterCode=c.strCostCenterCode "
		    + "group by c.strCostCenterCode; ";
	    ResultSet rsCostCenters = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenters);
	    while (rsCostCenters.next())
	    {

		String primaryPrinterName = rsCostCenters.getString(3);
		String secondaryPrinterName = rsCostCenters.getString(4);
		String printOnBothPrinters = rsCostCenters.getString(5);

		//printing
		new clsUtility2().funPrintToPrinter(primaryPrinterName, secondaryPrinterName, "MoveKOT", printOnBothPrinters, false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCreateTempFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File PrintText = new File(filePath + "/Temp");
	    if (!PrintText.exists())
	    {
		PrintText.mkdirs();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCreateTestTextFile(File file, String fromKOTNo, String toTableName, List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
	BufferedWriter fileWriter = null;
	try
	{

	    DecimalFormat decimalFormat = new DecimalFormat("0.##");
	    //File file=new File(filename);
	    fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

	    String fileHeader = "---KOT SHIFTED MESSAGE---------";
	    String dottedLine = "----------------------------------";
	    String newLine = "\n";
	    String blankLine = "                                   ";

	    fileWriter.write(fileHeader);
	    fileWriter.newLine();
	    fileWriter.write(dottedLine);
	    fileWriter.newLine();
	    fileWriter.write("User Name : " + clsGlobalVarClass.gUserName);
	    fileWriter.newLine();
	    fileWriter.write("POS Name : " + clsGlobalVarClass.gPOSName);
	    fileWriter.newLine();
	    //message
	    fileWriter.newLine();
	    fileWriter.write("KOT No. " + fromKOTNo + " From Table No. ");
	    fileWriter.write(fromTableName + " Shifted To Table ");
	    fileWriter.newLine();
	    fileWriter.write("No. " + toTableName + ".");
	    fileWriter.newLine();
	    fileWriter.write(dottedLine);

	    if (clsGlobalVarClass.gPrintItemsOnMoveKOTMoveTable)
	    {
		Iterator<clsMakeKotItemDtl> it = listOfItemsForFromTable.iterator();
		while (it.hasNext())
		{
		    clsMakeKotItemDtl objKotItemDtl = it.next();
		    fileWriter.newLine();
		    fileWriter.write(decimalFormat.format(objKotItemDtl.getQty()) + "  " + objKotItemDtl.getItemName());
		}
		fileWriter.newLine();
		fileWriter.write(dottedLine);
	    }

	    fileWriter.newLine();
	    for (int cntLines = 0; cntLines < Integer.parseInt(clsGlobalVarClass.gNoOfLinesInKOTPrint); cntLines++)
	    {
		fileWriter.newLine();
	    }
	    if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		fileWriter.write("V");//Linux
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    fileWriter.write("V");
		}
		else
		{
		    fileWriter.write("m");//windows
		}
	    }
	}
	catch (FileNotFoundException ex)
	{
	    ex.printStackTrace();
	}
	catch (UnsupportedEncodingException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
	finally
	{
	    try
	    {
		fileWriter.close();
	    }
	    catch (IOException ex)
	    {
		ex.printStackTrace();
	    }
	}

    }

    /*private void funUpdatePreviousKOTDetails() throws Exception
    {
        int cnt = 0;
        String strType = "MVKot", voidedDate = funGetVodidedDate();;
        String tableNo = mapBusyTableName.get(cmbBusyTable.getSelectedItem().toString());
        if (hmSelectedItemList.size() > 0)
        {
            String insertQuery = "insert into tblvoidkot(strTableNo,strPOSCode,strItemCode,strItemName,dblItemQuantity, "
                    + " dblAmount,strWaiterNo,strKOTNo,intPaxNo,strType,strReasonCode, "
                    + " strUserCreated,dteDateCreated,dteVoidedDate,strClientCode,strRemark ) "
                    + " values ";
            for (Map.Entry<String, Map<String, List<String>>> entryKOTMap : hmSelectedItemList.entrySet())
            {
                String itemCode = "", itemName = "", itemQty = "", itemAmt = "", waiterNo = "", createdDate = "";
                Map<String, List<String>> mapSelectedItem = entryKOTMap.getValue();
                for (Map.Entry<String, List<String>> entryItemMap : mapSelectedItem.entrySet())
                {
                    List<String> listOfParam = entryItemMap.getValue();
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");
                        itemCode = param[3];
                        createdDate = param[5];

                        String selectQuery = "select strItemName,dblItemQuantity,dblAmount,strUserCreated,dteDateCreated,strItemCode "
                                + " ,strPOSCode,strTableNo,strWaiterNo,strSerialNo,dblRedeemAmt,strCustomerCode "
                                + " from tblitemrtemp "
                                + " where strKOTNo='" + entryKOTMap.getKey() + "' and strItemCode='" + entryItemMap.getKey() + "' ";
                        ResultSet rsQuery = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                        while (rsQuery.next())
                        {
                            if (Double.valueOf(param[1]) < rsQuery.getDouble(2))
                            {
                                double qty = rsQuery.getDouble(2) - Double.valueOf(param[1]);
                                double rate = rsQuery.getDouble(3) / rsQuery.getDouble(2);
                                double amt = qty * rate;
                                String updateQuery = "update tblitemrtemp set dblItemQuantity='" + qty + "' , dblAmount='" + amt + "' where strKOTNo='" + entryKOTMap.getKey() + "' and left(strItemCode,7)='" + entryItemMap.getKey() + "' ";
                                clsGlobalVarClass.dbMysql.execute(updateQuery);
                            }
                            else
                            {
                                String deleteQuery = " delete from tblitemrtemp "
                                        + " where strKOTNo='" + entryKOTMap.getKey() + "' and left(strItemCode,7)='" + entryItemMap.getKey() + "' ";
                                clsGlobalVarClass.dbMysql.execute(deleteQuery);
                            }

                        }
                        rsQuery.close();

                        if (cnt == 0)
                        {
                            insertQuery += " ('" + tableNo + "','" + clsGlobalVarClass.gPOSCode + "','" + param[3] + "',"
                                    + "'" + param[0] + "','" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "',"
                                    + "'" + param[4] + "','" + entryKOTMap.getKey() + "','0','" + strType + "','R02',"
                                    + "'" + clsGlobalVarClass.gUserCode + "','" + createdDate + "'," + "'" + voidedDate + "'"
                                    + ",'" + clsGlobalVarClass.gClientCode + "','moved kot') ";
                        }
                        else
                        {
                            insertQuery += ",('" + tableNo + "','" + clsGlobalVarClass.gPOSCode + "','" + param[3] + "',"
                                    + "'" + param[0] + "','" + Double.valueOf(param[1]) + "','" + Double.valueOf(param[2]) + "',"
                                    + "'" + param[4] + "','" + entryKOTMap.getKey() + "','0','" + strType + "','R02',"
                                    + "'" + clsGlobalVarClass.gUserCode + "','" + createdDate + "'," + "'" + voidedDate + "'"
                                    + ",'" + clsGlobalVarClass.gClientCode + "','moved kot') ";
                        }
                        cnt++;
                    }
                }
            }

            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }
        }

        String sql = " select count(strTableNo) from tblitemrtemp "
                + " where strTableNo='" + tableNo + "' "
                + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
        System.out.println(sql);
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        int count = 0;
        if (rs.next())
        {
            count = rs.getInt(1);
        }
        rs.close();
        if (count == 0)
        {
            sql = "update tbltablemaster set strStatus='Normal' where strTableNo='" + tableNo + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
        }

    }*/
    private String funGetVodidedDate()
    {
	String voidDate = null;
	try
	{
	    java.util.Date dt = new java.util.Date();
	    String time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    voidDate = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
	    voidDate += " " + time;

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return voidDate;
	}
    }

    private void funSearchBusyTableClicked()
    {
	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search busy tables...");
	    keyboard.setVisible(true);
	    keyboard.setAlwaysOnTop(true);
	    keyboard.setAutoRequestFocus(true);
	    txtSearchBusyTables.setText(clsGlobalVarClass.gKeyboardValue);

	    String searchTableName = txtSearchBusyTables.getText().trim();
	    String searchTableCode = "All";
	    if (mapBusyTablesNameWithCodes.containsKey(searchTableName))
	    {
		searchTableCode = mapBusyTablesNameWithCodes.get(searchTableName);
	    }

	    funFillOpenKOTVector(searchTableCode);
	}
	else
	{
	    String searchTableName = txtSearchBusyTables.getText().trim();
	    String searchTableCode = "All";
	    if (mapBusyTablesNameWithCodes.containsKey(searchTableName))
	    {
		searchTableCode = mapBusyTablesNameWithCodes.get(searchTableName);
	    }

	    funFillOpenKOTVector(searchTableCode);
	}
    }
}
