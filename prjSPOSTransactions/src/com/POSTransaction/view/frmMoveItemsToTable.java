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
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmOkPopUp;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import javax.swing.table.DefaultTableModel;

public class frmMoveItemsToTable extends javax.swing.JFrame
{

    public Vector vAllTableNo, vAllTableName, vBussyTableNo, vBussyTableName;
    public String sql, toTableNo, toTableName, fromKOTNo, fromTableNo;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    private JButton[] btnTableArray;
    private clsUtility objUtility;
    private HashMap<String, String> mapGetPOSCode;
    private HashMap<String, String> mapGetBussyTableNo;

    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();
    private final DecimalFormat gDecimalFormatForQty = new DecimalFormat("0.0");

    public frmMoveItemsToTable()
    {
	initComponents();
	try
	{

	    objUtility = new clsUtility();
	    vAllTableNo = new Vector();
	    vAllTableName = new Vector();
	    vBussyTableNo = new Vector();
	    vBussyTableName = new Vector();

	    mapGetPOSCode = new HashMap<String, String>();
	    mapGetBussyTableNo = new HashMap<String, String>();

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

	    funFillPOSCombo();

	    if (clsGlobalVarClass.gMoveKOTToOtherPOS)
	    {
		funFillAllTableVector("All", "");
	    }
	    else
	    {
		funFillAllTableVector(clsGlobalVarClass.gPOSCode, "");
	    }

	    funFillBussyTableCombo();

	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("Super"))
	    {
		cmbPOS.setEnabled(true);
		cmbBusyTables.setEnabled(true);
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

    private void funFillBussyTableCombo() throws Exception
    {
	//cmbTable.addItem("Select Table");

	cmbBusyTables.removeAllItems();
	mapGetBussyTableNo.clear();

	cmbBusyTables.addItem("All");
	mapGetBussyTableNo.put("All", "All");
	sql = "select distinct a.strTableNo,b.strTableName,b.strStatus "
		+ "from tblitemrtemp a,tbltablemaster b "
		+ "where a.strTableNo=b.strTableNo "
		+ "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strNCKotYN='N' "
		+ "order by b.intSequence ";
	ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsTable.next())
	{
	    cmbBusyTables.addItem(rsTable.getString(2));
	    mapGetBussyTableNo.put(rsTable.getString(2), rsTable.getString(1));
	}
	rsTable.close();
    }

    private void funFillPOSCombo() throws Exception
    {

	cmbPOS.addItem("All");
	mapGetPOSCode.put("All", "All");
	sql = "select strPOSCode,strPOSName from tblposmaster ";
	ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsPOS.next())
	{
	    cmbPOS.addItem(rsPOS.getString(2));
	    mapGetPOSCode.put(rsPOS.getString(2), rsPOS.getString(1));

	    if (clsGlobalVarClass.gPOSCode.equals(rsPOS.getString(1)))
	    {
		cmbPOS.setSelectedItem(rsPOS.getString(2));
	    }
	}
	rsPOS.close();
	funPOSComboSelected(cmbPOS.getSelectedItem().toString());

    }

    private void funFillAllTableVector(String posCode, String searchAllTables)
    {
	try
	{
	    cntNavigate = 0;
	    cntNavigate1 = 0;
	    btnPrevious1.setEnabled(false);
	    btnNext1.setEnabled(true);

	    vAllTableNo.removeAllElements();
	    vAllTableName.removeAllElements();

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
		vAllTableNo.add(rsTblNo.getString(1));
		vAllTableName.add(rsTblNo.getString(2));
	    }
	    funLoadTables(0, vAllTableNo.size());
	    if (vAllTableNo.size() < 16)
	    {
		btnNext1.setEnabled(false);
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
		if (i == vAllTableNo.size())
		{
		    break;
		}
		String tblName = vAllTableName.elementAt(i).toString();
		sql = "select strTableNo,strStatus from tbltablemaster where strTableNo='" + vAllTableNo.elementAt(i).toString() + "'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rs.next();
		String status = rs.getString(2);
		int pax = 0;
		if (cntIndex < 16)
		{
		    if (status.equals("Occupied"))
		    {
			sql = "select intPaxNo from tblitemrtemp where strTableNo='" + vAllTableNo.elementAt(i).toString() + "' ";
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

    private void funSelectTable(String tableName, int index)
    {
	try
	{
	    if (tableName.trim().length() > 0)
	    {
		System.out.println(cntNavigate1);
		int currentIndex = (16 * cntNavigate1) + index;
		System.out.println(currentIndex);

		toTableNo = vAllTableNo.elementAt(currentIndex).toString();
		toTableName = vAllTableName.elementAt(currentIndex).toString();
		lblToTableName.setText(toTableName);
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
	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();

	    int rowCount = defaultTableModel.getRowCount();

	    if (rowCount <= 0)
	    {
		JOptionPane.showMessageDialog(this, "Select items");
		flgValidate = false;

		return flgValidate;
	    }

	    boolean isItemSelected = false;
	    for (int r = 0; r < rowCount; r++)
	    {
		boolean b = Boolean.parseBoolean(defaultTableModel.getValueAt(r, 5).toString());
		if (b)
		{
		    isItemSelected = true;
		    break;
		}
	    }

	    if (!isItemSelected)
	    {
		JOptionPane.showMessageDialog(this, "Select items");
		flgValidate = false;

		return flgValidate;
	    }

	    if (toTableName == null || toTableName.trim().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Select table for shift.");
		flgValidate = false;

		return flgValidate;
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

    private void funMoveItemsToTable()
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

	    List<clsMakeKotItemDtl> listOfItemsForFromTable = new ArrayList<clsMakeKotItemDtl>();
	    List<clsMakeKotItemDtl> listOfItemsForToTable = new ArrayList<clsMakeKotItemDtl>();

	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	    int rowCount = defaultTableModel.getRowCount();

	    String oldKOTNo = "";

	    String sqlOldKOTNo = "select a.strKOTNo "
		    + "from tblitemrtemp a  "
		    + "where strTableNo='" + fromTableNo + "'  "
		    + "and strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and strNcKotYN='N' "
		    + "limit 1 ";
	    ResultSet rsKOT = clsGlobalVarClass.dbMysql.executeResultSet(sqlOldKOTNo);
	    if (rsKOT.next())
	    {
		oldKOTNo = rsKOT.getString(1);
	    }
	    rsKOT.close();

	    String newKOTNo = funGenerateKOTNo();

	    for (int r = 0; r < rowCount; r++)
	    {

		String itemName = defaultTableModel.getValueAt(r, 0).toString();
		double itemRate = Double.parseDouble(defaultTableModel.getValueAt(r, 1).toString());
		double itemQty = Double.parseDouble(defaultTableModel.getValueAt(r, 2).toString());
		double moveQty = Double.parseDouble(defaultTableModel.getValueAt(r, 3).toString());

		boolean isItemSelected = Boolean.parseBoolean(defaultTableModel.getValueAt(r, 5).toString());
		String itemCode = defaultTableModel.getValueAt(r, 6).toString();
		String waiterNo = defaultTableModel.getValueAt(r, 7).toString();
		double firedQty = Double.parseDouble(defaultTableModel.getValueAt(r, 8).toString());

		if (isItemSelected)
		{
		    itemQty = itemQty - moveQty;
		}

		//for original items
		double itemAmt = itemRate * itemQty;
		clsMakeKotItemDtl objItemForFromTable = new clsMakeKotItemDtl(String.valueOf(r), oldKOTNo, fromTableNo, waiterNo, itemName, itemCode, itemQty, itemAmt, pax, "Y", "N", false, "", "", "", "N", itemRate);
		objItemForFromTable.setDblFiredQty(firedQty);

		listOfItemsForFromTable.add(objItemForFromTable);

		//for move items 
		if (isItemSelected)
		{
		    double moveItemAmt = itemRate * moveQty;
		    clsMakeKotItemDtl objItemForToTable = new clsMakeKotItemDtl(String.valueOf(r), newKOTNo, toTableNo, waiterNo, itemName, itemCode, moveQty, moveItemAmt, pax, "Y", "N", false, "", "", "", "N", itemRate);
		    objItemForToTable.setDblFiredQty(firedQty);

		    listOfItemsForToTable.add(objItemForToTable);
		}

	    }

	    funUpdateDataInTempTable("Update", listOfItemsForFromTable, fromTableNo);
	    funInsertDataInTempTable("Insert", listOfItemsForToTable);

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

	    JOptionPane.showMessageDialog(this, "Items Shifted to " + toTableName);

	    //send message to all cost centers 
	    funSendMessageToCostCenters(listOfItemsForToTable);

	    funFillAllTableVector("All", "");

	    funClearFields();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funClearFields() throws Exception
    {
	toTableName = "";
	toTableNo = "";
	lblToTableName.setText("");
	lblFromTableName.setText("");

	cmbBusyTables.setSelectedIndex(0);
	cmbPOS.setSelectedIndex(0);

	DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	defaultTableModel.setRowCount(0);

	funFillBussyTableCombo();
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

    private void funPreviousBtnClick()
    {
	try
	{
	    cntNavigate1--;
	    btnNext1.setEnabled(true);
	    if (cntNavigate1 == 0)
	    {
		btnPrevious1.setEnabled(false);
		funLoadTables(0, vAllTableNo.size());
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

    private void funTableComboSelected(String bussyTableName)
    {
	String bussyTableNo = mapGetBussyTableNo.get(bussyTableName);

	funFillItemsForSelectedTable(bussyTableNo, bussyTableName);
    }

    private void funPOSComboSelected(String POSName)
    {
	String posCode = mapGetPOSCode.get(POSName);
	funFillAllTableVector(posCode, "");
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
        panelBusyTableItems = new javax.swing.JPanel();
        scrollBussyTableItems = new javax.swing.JScrollPane();
        tblBussyTableItems = new javax.swing.JTable();
        lblOpenTable = new javax.swing.JLabel();
        lblOpenTable1 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnPrevious1 = new javax.swing.JButton();
        btnNext1 = new javax.swing.JButton();
        btnShowKOT = new javax.swing.JButton();
        cmbBusyTables = new javax.swing.JComboBox();
        cmbPOS = new javax.swing.JComboBox();
        txtSearchAllTables = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        lblFromTableName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblToTableName = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS ");
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
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Move Items To Table");
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

        btnTableNo2.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo2MouseClicked(evt);
            }
        });

        btnTableNo1.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo1MouseClicked(evt);
            }
        });

        btnTableNo3.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo3MouseClicked(evt);
            }
        });

        btnTableNo4.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo4MouseClicked(evt);
            }
        });

        btnTableNo5.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo5MouseClicked(evt);
            }
        });

        btnTableNo6.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo6MouseClicked(evt);
            }
        });

        btnTableNo7.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo7MouseClicked(evt);
            }
        });

        btnTableNo8.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo8MouseClicked(evt);
            }
        });

        btnTableNo9.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo9MouseClicked(evt);
            }
        });

        btnTableNo10.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo10MouseClicked(evt);
            }
        });

        btnTableNo11.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo11MouseClicked(evt);
            }
        });

        btnTableNo12.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo12MouseClicked(evt);
            }
        });

        btnTableNo13.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo13MouseClicked(evt);
            }
        });

        btnTableNo14.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo14MouseClicked(evt);
            }
        });

        btnTableNo15.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo15MouseClicked(evt);
            }
        });

        btnTableNo16.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnTableNo16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
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

        panelBusyTableItems.setBackground(new java.awt.Color(255, 255, 255));
        panelBusyTableItems.setEnabled(false);
        panelBusyTableItems.setOpaque(false);

        tblBussyTableItems.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        tblBussyTableItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Rate", "Qty", "Move Qty", "Amount", "Select", "", "", ""
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblBussyTableItems.setPreferredSize(new java.awt.Dimension(400, 420));
        tblBussyTableItems.setRowHeight(35);
        tblBussyTableItems.getTableHeader().setReorderingAllowed(false);
        tblBussyTableItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblBussyTableItemsMouseClicked(evt);
            }
        });
        scrollBussyTableItems.setViewportView(tblBussyTableItems);
        if (tblBussyTableItems.getColumnModel().getColumnCount() > 0)
        {
            tblBussyTableItems.getColumnModel().getColumn(0).setPreferredWidth(350);
            tblBussyTableItems.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblBussyTableItems.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblBussyTableItems.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblBussyTableItems.getColumnModel().getColumn(7).setPreferredWidth(0);
            tblBussyTableItems.getColumnModel().getColumn(8).setMinWidth(0);
            tblBussyTableItems.getColumnModel().getColumn(8).setPreferredWidth(0);
            tblBussyTableItems.getColumnModel().getColumn(8).setMaxWidth(0);
        }

        javax.swing.GroupLayout panelBusyTableItemsLayout = new javax.swing.GroupLayout(panelBusyTableItems);
        panelBusyTableItems.setLayout(panelBusyTableItemsLayout);
        panelBusyTableItemsLayout.setHorizontalGroup(
            panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
            .addGroup(panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollBussyTableItems, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
        );
        panelBusyTableItemsLayout.setVerticalGroup(
            panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
            .addGroup(panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBusyTableItemsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollBussyTableItems, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)))
        );

        lblOpenTable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblOpenTable.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable.setText("BUSY TABLES");

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
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnPrevious1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnPrevious1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious1.setText("<<<");
        btnPrevious1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevious1ActionPerformed(evt);
            }
        });

        btnNext1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext1.setText(">>>");
        btnNext1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNext1ActionPerformed(evt);
            }
        });

        btnShowKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnShowKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnShowKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnShowKOT.setText("VIEW KOT");
        btnShowKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnShowKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShowKOTActionPerformed(evt);
            }
        });

        cmbBusyTables.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbBusyTables.setToolTipText("Select Table");
        cmbBusyTables.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBusyTablesActionPerformed(evt);
            }
        });

        cmbPOS.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbPOS.setToolTipText("Select POS");
        cmbPOS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPOSActionPerformed(evt);
            }
        });

        txtSearchAllTables.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtSearchAllTables.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchAllTablesMouseClicked(evt);
            }
        });
        txtSearchAllTables.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchAllTablesKeyReleased(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");

        lblFromTableName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setText("==>");

        lblToTableName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblFromTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblToTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(450, 450, 450)
                        .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(214, 214, 214)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelBusyTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(cmbBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblOpenTable)
                        .addGap(12, 12, 12)
                        .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                            .addComponent(cmbBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelBusyTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFromTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblToTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
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
		funMoveItemsToTable();
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
	clsGlobalVarClass.hmActiveForms.remove("Move Items To Table");
    }//GEN-LAST:event_btnCloseActionPerformed

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
	    int resMod = vAllTableNo.size() % tableSize;
	    int resDiv = vAllTableNo.size() / 16;
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

	funShowKOT();

    }//GEN-LAST:event_btnShowKOTActionPerformed

    private void cmbBusyTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBusyTablesActionPerformed
	// TODO add your handling code here:
	if (cmbBusyTables.getSelectedIndex() > 0)
	{
	    funTableComboSelected(cmbBusyTables.getSelectedItem().toString());
	}
    }//GEN-LAST:event_cmbBusyTablesActionPerformed

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOSActionPerformed
	// TODO add your handling code here:
	if (cmbPOS.getSelectedIndex() > 0)
	{
	    funPOSComboSelected(cmbPOS.getSelectedItem().toString());

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
	clsGlobalVarClass.hmActiveForms.remove("Move Items To Table");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Move Items To Table");
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

    private void tblBussyTableItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblBussyTableItemsMouseClicked
    {//GEN-HEADEREND:event_tblBussyTableItemsMouseClicked
	funBussyTableMouseClicked(evt);
    }//GEN-LAST:event_tblBussyTableItemsMouseClicked

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
	    java.util.logging.Logger.getLogger(frmMoveItemsToTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveItemsToTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveItemsToTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmMoveItemsToTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmMoveItemsToTable().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext1;
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
    private javax.swing.JComboBox cmbBusyTables;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromTableName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTable;
    private javax.swing.JLabel lblOpenTable1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblToTableName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAllTables;
    private javax.swing.JPanel panelBusyTableItems;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrollBussyTableItems;
    private javax.swing.JTable tblBussyTableItems;
    private javax.swing.JTextField txtSearchAllTables;
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
	    funFillAllTableVector("All", searchAllTables);
	}
	else
	{
	    funFillAllTableVector(clsGlobalVarClass.gPOSCode, searchAllTables);
	}
    }

    private void funSendMessageToCostCenters(List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    String filename = (filePath + "/Temp/Move Items To Table.txt");
	    File file = new File(filename);

	    funCreateTestTextFile(file, lblFromTableName.getText(), toTableName, listOfItemsForFromTable);

	    String sqlCostCenters = "select b.strCostCenterCode ,c.strCostCenterName,c.strPrinterPort,c.strSecondaryPrinterPort,c.strPrintOnBothPrinters "
		    + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
		    + "where a.strTableNo='" + toTableNo + "' "
		    + "and a.strItemCode=b.strItemCode "
		    + "and a.strPOSCode=b.strPosCode "
		    + "and b.strCostCenterCode=c.strCostCenterCode "
		    + "and a.strNCKotYN='N' "
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

    private void funCreateTestTextFile(File file, String fromTableName, String toTableName, List<clsMakeKotItemDtl> listOfItemsForFromTable)
    {
	BufferedWriter fileWriter = null;
	try
	{

	    DecimalFormat decimalFormat = new DecimalFormat("0.##");
	    //File file=new File(filename);
	    fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

	    String fileHeader = "---ITEMS SHIFTED MESSAGE---------";
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
	    fileWriter.write("Items From Table No. ");
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

    private void funFillItemsForSelectedTable(String bussyTableNo, String bussyTableName)
    {
	try
	{

	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	    defaultTableModel.setRowCount(0);

	    String sqlTableItemDtl = " select strItemCode,strItemName,dblRate,sum(dblItemQuantity),sum(dblAmount),sum(a.dblTaxAmt),a.strWaiterNo,dblFiredQty "
		    + "from tblitemrtemp a  "
		    + "where strTableNo='" + bussyTableNo + "'  "
		    + "and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All')  "
		    + "and strNcKotYN='N' "
		    + "group by a.strItemCode "
		    + "order by strKOTNo desc ,strSerialNo ";
	    ResultSet rsBussyTableItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableItemDtl);
	    while (rsBussyTableItems.next())
	    {
		Object[] row =
		{
		    rsBussyTableItems.getString(2), gDecimalFormat.format(rsBussyTableItems.getDouble(3)), gDecimalFormatForQty.format(rsBussyTableItems.getDouble(4)), gDecimalFormatForQty.format(rsBussyTableItems.getDouble(4)), gDecimalFormat.format(rsBussyTableItems.getDouble(5)), false, rsBussyTableItems.getString(1), rsBussyTableItems.getString(7), rsBussyTableItems.getString(8)
		};

		defaultTableModel.addRow(row);
	    }
	    rsBussyTableItems.close();

	    fromTableNo = bussyTableNo;
	    String fromTableName = bussyTableName;
	    lblFromTableName.setText(fromTableName);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funBussyTableMouseClicked(MouseEvent evt)
    {
	try
	{
	    int columnNo = tblBussyTableItems.getSelectedColumn();
	    int rowNo = tblBussyTableItems.getSelectedRow();

	    if (columnNo == 3)//move qty
	    {
		double originalQty = Double.parseDouble(tblBussyTableItems.getValueAt(rowNo, 2).toString());
		if (originalQty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Move Qty");
		    num.setVisible(true);
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			{
			    double enterQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);

			    if (enterQty > 0)
			    {
				if (enterQty > originalQty)
				{
				    JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
				    return;
				}
				else
				{
				    tblBussyTableItems.setValueAt(gDecimalFormatForQty.format(enterQty), rowNo, 3);
				    clsGlobalVarClass.gNumerickeyboardValue = null;
				}
			    }
			    else
			    {
				JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
				return;
			    }
			}
			else
			{
			    JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
			    return;
			}
		    }
		    else
		    {
			JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
			return;
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGenerateKOTNo()
    {
	String kotNo = "";
	try
	{
	    long code = 0;
	    sql = "select dblLastNo from tblinternal where strTransactionType='KOTNo'";
	    ResultSet rsKOT = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsKOT.next())
	    {
		code = rsKOT.getLong(1);
		code = code + 1;
		kotNo = "KT" + String.format("%07d", code);
		clsGlobalVarClass.gUpdatekot = true;
		clsGlobalVarClass.gKOTCode = code;
	    }
	    else
	    {
		kotNo = "KT0000001";
		clsGlobalVarClass.gUpdatekot = false;
	    }
	    rsKOT.close();
	    sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='KOTNo'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return kotNo;
    }

    private void funInsertDataInTempTable(String type, List<clsMakeKotItemDtl> listOfMakeKOTItemDtl)
    {
	//private synchronized void fun_Insert_Data_tblitemrtemp() {
	try
	{
	    String homeDelivery = "No", customerName = "", customerCode = "";

	    String takeAway = "No";

	    String counterCode = "NA";
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		counterCode = clsGlobalVarClass.gCounterCode;
	    }

	    String tableNo = "";
	    String KOTNo = "";
	    double KOTAmt = 0, taxAmt = 0;
	    String delBoyCode = "";

	    String insertQuery = "insert into tblitemrtemp (strSerialNo,strTableNo,strCardNo,dblRedeemAmt,strPosCode,strItemCode"
		    + ",strHomeDelivery,strCustomerCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo"
		    + ",strKOTNo,intPaxNo,strPrintYN,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strTakeAwayYesNo,strNCKotYN,strCustomerName,strCounterCode"
		    + ",dblRate,dblTaxAmt,strDelBoyCode,dblFiredQty ) values ";
	    for (clsMakeKotItemDtl listItemDtl : listOfMakeKOTItemDtl)
	    {

		tableNo = listItemDtl.getTableNo();
		KOTNo = listItemDtl.getKOTNo();

		insertQuery += "('" + listItemDtl.getSequenceNo() + "','" + listItemDtl.getTableNo() + "'"
			+ ",'','0.00','" + clsGlobalVarClass.gPOSCode + "'"
			+ ",'" + listItemDtl.getItemCode() + "','" + homeDelivery + "','" + customerCode + "'"
			+ ",'" + listItemDtl.getItemName() + "','" + listItemDtl.getQty() + "','" + listItemDtl.getAmt() + "'"
			+ ",'" + listItemDtl.getWaiterNo() + "','" + listItemDtl.getKOTNo() + "'"
			+ ",'" + listItemDtl.getPaxNo() + "','" + listItemDtl.getPrintYN() + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			+ ",'" + takeAway + "','N','" + customerName + "','" + counterCode + "'"
			+ ",'" + listItemDtl.getItemRate() + "','0.00','" + delBoyCode + "','"+listItemDtl.getDblFiredQty()+"'),";
		KOTAmt += listItemDtl.getAmt();
	    }
	    StringBuilder sb = new StringBuilder(insertQuery);
	    int index = sb.lastIndexOf(",");
	    insertQuery = sb.delete(index, sb.length()).toString();
	    //System.out.println(insertQuery);
	    clsGlobalVarClass.dbMysql.execute(insertQuery);

	    String sql = "insert into tblkottaxdtl "
		    + "values ('" + tableNo + "','" + KOTNo + "'," + KOTAmt + "," + taxAmt + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(tableNo);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, "Error In Print KOT-" + e.getMessage());
	}
    }

    private void funUpdateDataInTempTable(String type, List<clsMakeKotItemDtl> listOfMakeKOTItemDtl, String fromTableNo)
    {

	try
	{

	    for (clsMakeKotItemDtl itemDtl : listOfMakeKOTItemDtl)
	    {

		String itemCode = itemDtl.getItemCode();
		double itemQty = itemDtl.getQty();

		if (itemQty <= 0)
		{
		    String deleteQuery = "delete from tblitemrtemp  "
			    + "where strItemCode='" + itemCode + "' "
			    + "and strTableNo='" + fromTableNo + "' "
			    + "and strNCKotYN='N' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		}
		else
		{
		    String deleteQuery = "delete from tblitemrtemp  "
			    + "where strItemCode='" + itemCode + "' "
			    + "and strTableNo='" + fromTableNo + "' "
			    + "and strNCKotYN='N' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		    String insertQuery = "insert into tblitemrtemp (strSerialNo,strTableNo,strCardNo,dblRedeemAmt,strPosCode,strItemCode"
			    + ",strHomeDelivery,strCustomerCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo"
			    + ",strKOTNo,intPaxNo,strPrintYN,strUserCreated,strUserEdited,dteDateCreated"
			    + ",dteDateEdited,strTakeAwayYesNo,strNCKotYN,strCustomerName,strCounterCode"
			    + ",dblRate,dblTaxAmt,strDelBoyCode,dblFiredQty ) values ";

		    insertQuery += "('" + itemDtl.getSequenceNo() + "','" + fromTableNo + "'"
			    + ",'','0.00','" + clsGlobalVarClass.gPOSCode + "'"
			    + ",'" + itemDtl.getItemCode() + "','NO','' "
			    + ",'" + itemDtl.getItemName() + "','" + itemQty + "','" + itemDtl.getAmt() + "'"
			    + ",'" + itemDtl.getWaiterNo() + "','" + itemDtl.getKOTNo() + "'"
			    + ",'" + itemDtl.getPaxNo() + "','" + itemDtl.getPrintYN() + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'','N','','' "
			    + ",'" + itemDtl.getItemRate() + "','0.00','','"+itemDtl.getDblFiredQty()+"')";

		    clsGlobalVarClass.dbMysql.execute(insertQuery);
		}
	    }

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(fromTableNo);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, "Error In Print KOT-" + e.getMessage());
	}
    }
}
