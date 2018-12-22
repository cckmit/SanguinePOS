package com.POSTransaction.view;



import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSPrinting.Text.DayEnd.clsDayEndTextReport;
import com.POSPrinting.clsKOTGeneration;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmReprintDocs extends javax.swing.JFrame
{

    private StringBuilder sql;
    private ResultSet resultSet;
    private String[] arrPOSName = null;
    private int size;
    private HashMap<String, String> mapPOSCodeAndNames;
    private String operationType;
    private String kotFor;
    private String selectedPOSName;
    private String selectedPOSCode;
    private DefaultTableModel dtmDefaultModel;
    private DefaultTableModel dtmForDinaKOT;
    private DefaultTableModel dtmForDirectBiller;
    private DefaultTableModel dtmForBill;
    private String selectedKOT = "";
    private String selectedBill = "";
    private int nextCnt = 0, prevCnt = 0;
    clsUtility objUtility;
    clsUtility2 objUtility2;
    private Vector vReasonCodeForReprint;
    private Vector vReasonNameForReprint;
    private String reprintRemarks = "";
    private String selectedReasonCode = "";
    private String viewORprint = "view";
    private String dayEndDate;

    public frmReprintDocs()
    {
	objUtility = new clsUtility();
	objUtility2 = new clsUtility2();
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
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    sql = new StringBuilder();
	    mapPOSCodeAndNames = new HashMap<String, String>();
	    arrPOSName = funGetPOSName();
	    size = arrPOSName.length;

	    btnPrevious.setEnabled(false);
	    btnNext.setEnabled(false);

	    if (size > 4)
	    {
		btnNext.setEnabled(true);
	    }
	    else
	    {
		btnNext.setEnabled(false);
	    }
	    funSetUnVisiblePOSButton(false);
	    if (size > 4)
	    {
		funSetPOSNames(1, 4);
		funSetVisiblePOSButton(1, 4, true);
	    }
	    else
	    {
		funSetPOSNames(1, size);
		funSetVisiblePOSButton(1, size, true);
	    }
	    funLoadReprintResons();

//            nextCnt=0;
//            prevCnt=size;
	    panelForDayEndDate.setVisible(false);
	    dtmDefaultModel = new DefaultTableModel();
	    dtmForDinaKOT = new DefaultTableModel();
	    dtmForDirectBiller = new DefaultTableModel();
	    dtmForBill = new DefaultTableModel();

	    funSetPOS1Selected();
	    funBillMouseClicked();

	    if (clsGlobalVarClass.gNoOfDaysReportsView != 0)
	    {
		try
		{

		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		    final Date userDateRange = dateFormat.parse(clsGlobalVarClass.gPOSOnlyDateForTransaction);
		    int days = userDateRange.getDate() - clsGlobalVarClass.gNoOfDaysReportsView;
		    userDateRange.setDate(days);

		    dteForDayEnd.setMinSelectableDate(userDateRange);

		    dteForDayEnd.addPropertyChangeListener(new PropertyChangeListener()
		    {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
			    if ("date".equals(e.getPropertyName()))
			    {
				Date dateChooserValue = (Date) e.getNewValue();

				if (clsGlobalVarClass.gNoOfDaysReportsView != 0 && dateChooserValue.before(userDateRange))
				{
				    try
				    {
					java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
					dteForDayEnd.setDate(date);
				    }
				    catch (Exception ex)
				    {
					ex.printStackTrace();
				    }
				}
			    }
			}
		    });
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funNextButtonPressed()
    {
	nextCnt++;
	int startIndex = (nextCnt * 4) + 1;
	int remainder = size - (nextCnt * 4);
	int endIndex;
	if (remainder >= 4)
	{
	    endIndex = (startIndex - 1) + 4;
	}
	else
	{
	    endIndex = (startIndex - 1) + remainder;
	}
	funSetPOSNames(startIndex, endIndex);
	if (remainder <= 4)
	{
	    btnNext.setEnabled(false);
	}
	prevCnt--;
	btnPrevious.setEnabled(true);

    }

    private void funPreviousButtonPressed()
    {
	prevCnt++;

	int prevC = Math.abs(prevCnt);
	int startIndex = (prevC * 4) + 1;
	int remainder = size - (prevC * 4);
	int endIndex;
	if (remainder >= 4)
	{
	    endIndex = (startIndex - 1) + 4;
	}
	else
	{
	    endIndex = (startIndex - 1) + remainder;
	}
	funSetPOSNames(startIndex, endIndex);
	if (remainder >= size)
	{
	    btnPrevious.setEnabled(false);
	}
	nextCnt--;
	btnNext.setEnabled(true);
    }

    private void funSetPOSNames(int startIndex, int endIndex)
    {
	btnPOS1.setEnabled(false);
	btnPOS2.setEnabled(false);
	btnPOS3.setEnabled(false);
	btnPOS4.setEnabled(false);

	btnPOS1.setVisible(false);
	btnPOS2.setVisible(false);
	btnPOS3.setVisible(false);
	btnPOS4.setVisible(false);

	JButton[] arrPOSButtons =
	{
	    btnPOS1, btnPOS2, btnPOS3, btnPOS4
	};

	int cnt = 0;
	for (int i = startIndex; i <= endIndex; i++)
	{
	    String posName = "";
	    posName = "<html>" + arrPOSName[i - 1].replaceAll(" ", "<br>") + "</html>";
	    arrPOSButtons[cnt].setText(posName);
	    arrPOSButtons[cnt].setEnabled(true);
	    arrPOSButtons[cnt].setVisible(true);
	    cnt++;
	}

	/*
         * for(int i=startIndex;i<=endIndex;i++) { String posName=""; switch(i)
         * { case 0: posName="<html>"+arrPOSName[i].replaceAll(" ",
         * "<br>")+"</html>"; btnPOS1.setText(posName);
         * btnPOS1.setEnabled(true); break;
         *
         * case 1: posName="<html>"+arrPOSName[i].replaceAll(" ",
         * "<br>")+"</html>"; btnPOS2.setText(posName);
         * btnPOS2.setEnabled(true); break;
         *
         * case 2: posName="<html>"+arrPOSName[i].replaceAll(" ",
         * "<br>")+"</html>"; btnPOS3.setText(posName);
         * btnPOS3.setEnabled(true); break;
         *
         * case 3: posName="<html>"+arrPOSName[i].replaceAll(" ",
         * "<br>")+"</html>"; btnPOS4.setText(posName);
         * btnPOS4.setEnabled(true); break; } }
	 */
    }

    /*
     * private void funTextFilePrintingBill(String billno, String billDate) {
     *
     * try { clsTextFileGeneratorForPrinting obj = new
     * clsTextFileGeneratorForPrinting(); if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 1")) {
     * obj.funGenerateTextFileBillPrinting(billno, "reprint", "sales report",
     * "sale",billDate,""); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 2")) {
     * obj.funGenerateTextFileBillPrintingForFormat2(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 3")) {
     * obj.funGenerateTextFileBillPrintingForFormat3(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 4")) {
     * obj.funGenerateTextFileBillPrintingForFormat4(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 5")) {
     * obj.funGenerateTextFileBillPrintingForFormat5(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 6")) {
     * obj.funGenerateTextFileBillPrintingForFormat6(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 7")) {
     * obj.funGenerateTextFileBillPrintingForFormat7(billno, "reprint", "sales
     * report", "sale",billDate); } } catch (Exception e) { e.printStackTrace();
     * } }
     */
    private String[] funGetPOSName()
    {
	String[] posName = null;
	try
	{
	    sql.setLength(0);
	    sql.append("select strPOSCode,strPOSName from tblposmaster");
	    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());

	    resultSet.last();
	    int size = resultSet.getRow();
	    resultSet.beforeFirst();

	    posName = new String[size];
	    for (int i = 0; resultSet.next(); i++)
	    {
		posName[i] = resultSet.getString("strPOSName");
		mapPOSCodeAndNames.put(resultSet.getString("strPOSName"), resultSet.getString("strPOSCode"));
		System.out.println("" + posName[i]);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return posName;
    }

    private void funSetVisiblePOSButton(int startIndex, int endIndex, boolean flag)
    {
	for (int i = startIndex; i <= endIndex; i++)
	{
	    switch (i)
	    {
		case 1:
		    btnPOS1.setVisible(flag);
		    btnPOS1.setEnabled(flag);
		    break;
		case 2:
		    btnPOS2.setVisible(flag);
		    btnPOS2.setEnabled(flag);
		    break;
		case 3:
		    btnPOS3.setVisible(flag);
		    btnPOS3.setEnabled(flag);
		    break;
		case 4:
		    btnPOS4.setVisible(flag);
		    btnPOS4.setEnabled(flag);
		    break;
	    }
	}
    }

    private void funSetUnVisiblePOSButton(boolean flag)
    {
	btnPOS1.setVisible(flag);
	btnPOS2.setVisible(flag);
	btnPOS3.setVisible(flag);
	btnPOS4.setVisible(flag);

	btnPOS1.setEnabled(flag);
	btnPOS2.setEnabled(flag);
	btnPOS3.setEnabled(flag);
	btnPOS4.setEnabled(flag);
    }

    private void funSetDinaSelected()
    {
	//selected
	btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	kotFor = "Dina";
	//unselected
	btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
    }

    private void funSetDirectBillerSelected()
    {
	//selected
	btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	kotFor = "DirectBiller";
	//unselected
	btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
    }

    private void funSetKOTOperationSelected()
    {
	//selected
	btnKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	operationType = "KOT";
	//unselected
	btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
    }

    private void funSetBillOperationSelected()
    {
	//selected
	btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	operationType = "Bill";
	//unselected
	btnKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
    }

    private void funSetDayEndOperationSelected()
    {
	//selected
	btnDayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	operationType = "DayEnd";
	//unselected
	btnKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
    }

    private void funSetPOSName(JButton btnPOSSelected)
    {
	String posName = btnPOSSelected.getText();
	posName = posName.replace("<html>", "");
	posName = posName.replace("</html>", "");
	posName = posName.replaceAll("<br>", " ");
	selectedPOSName = posName;
    }

    private void funExecute()
    {
	txtSearch.setVisible(true);
	lblSearch.setVisible(true);

	if (selectedPOSName == null || selectedPOSName.isEmpty())
	{
	    JOptionPane.showMessageDialog(null, "Please Select POS.");
	    return;
	}

	String posCode = funGetSelectedPOSCode();
	clsGlobalVarClass.gPosCodeForReprintDocs = posCode;

	if (operationType == null || operationType.isEmpty())
	{
	    JOptionPane.showMessageDialog(null, "Please Select Operation.");
	    return;
	}
	if (operationType.equalsIgnoreCase("KOT"))
	{
	    if (kotFor == null || kotFor.isEmpty())
	    {
		JOptionPane.showMessageDialog(null, "Please Select Dina OR Direct Biller.");
		return;
	    }
	    scrollPane.setVisible(true);
	    if (kotFor.equalsIgnoreCase("Dina"))
	    {
		funGenerateTableForDinaKOT();
	    }
	    else if (kotFor.equalsIgnoreCase("DirectBiller"))
	    {
		funGenerateTableForDirectBiller();
	    }
	}
	else if (operationType.equalsIgnoreCase("Bill"))
	{
	    scrollPane.setVisible(true);
	    funGenerateTableForBill();
	}
	else if (operationType.equalsIgnoreCase("DayEnd"))
	{
	    txtSearch.setVisible(false);
	    lblSearch.setVisible(false);

	    scrollPane.setVisible(false);
	    lblNoOfReprintLabel.setVisible(false);
	    lblNoOfReprints.setVisible(false);
	    panelForDayEndDate.setLocation(scrollPane.getLocation());
	    panelForDayEndDate.setVisible(true);
	}
    }

    private String funGetSelectedPOSCode()
    {
	selectedPOSCode = mapPOSCodeAndNames.get(selectedPOSName);
	return selectedPOSCode;
    }

    private void funGenerateTableForDinaKOT()
    {
	try
	{
	    dtmForDinaKOT = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"KOT NO", "Time", "Waiter", "Table", "PAX No", "User Created", "Amount"
		    }
	    )
	    {
		Class[] types = new Class[]
		{
		    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
		};
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false
		};

		public Class getColumnClass(int columnIndex)
		{
		    return types[columnIndex];
		}

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };
	    dtmForDinaKOT.setRowCount(0);
	    tblShowData.setRowHeight(25);

	    String sql = "select a.strKOTNo as KOTNo,TIME_FORMAT(time(a.dteDateCreated),'%h:%i') as Time \n"
		    + ",IFNULL(c.strWShortName,'NA') as WaiterName,b.strTableName as TableName\n"
		    + ",a.intPaxNo as PaxNo,a.strUserEdited as UserCreated ,a.dblAmount as Amount\n"
		    + "from tblitemrtemp a left outer join tbltablemaster b on a.strTableNo=b.strTableNo \n"
		    + "left outer join tblwaitermaster c  on a.strWaiterNo=c.strWaiterNo\n"
		    + "where a.strPOSCode='" + clsGlobalVarClass.gPosCodeForReprintDocs + "' "
		    + "and (a.strKOTNo like '%" + txtSearch.getText().trim() + "%' "
		    + "or c.strWShortName like '%" + txtSearch.getText().trim() + "%' "
		    + "or  b.strTableName like '%" + txtSearch.getText().trim() + "%' ) "
		    + "group by a.strKOTNo,a.strTableNo \n"
		    + "order by a.strKOTNo desc";
	    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (resultSet.next())
	    {
		Object row[] =
		{
		    resultSet.getString("KOTNo"), resultSet.getString("Time"), resultSet.getString("WaiterName"), resultSet.getString("TableName"), resultSet.getString("PaxNo"), resultSet.getString("UserCreated"), resultSet.getString("Amount")
		};
		dtmForDinaKOT.addRow(row);
	    }
	    tblShowData.setModel(dtmForDinaKOT);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblShowData.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funGenerateTableForDirectBiller()
    {
	try
	{
	    dtmForDirectBiller = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {
		    },
		    new String[]
		    {
			"Bill NO", "POS", "Time", "Amount"
		    }
	    )
	    {
		Class[] types = new Class[]
		{
		    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
		};
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false
		};

		public Class getColumnClass(int columnIndex)
		{
		    return types[columnIndex];
		}

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };
	    dtmForDirectBiller.setRowCount(0);
	    tblShowData.setRowHeight(25);

	    String sql = "select a.strbillno as BillNo,TIME_FORMAT(time(a.dteBillDate),'%h:%i') as Time ,b.strPOSName as POS"
		    + ",a.dblGrandTotal as TotalAmount "
		    + " from tblbillhd a ,tblposmaster b "
		    + " where a.strPOSCode='" + clsGlobalVarClass.gPosCodeForReprintDocs + "' "
		    + " and a.strPOSCode=b.strPOSCode "
		    + " and a.strTableNo='' Or a.strTableNo='TB0000'  "
		    + " and a.strbillno like '%" + txtSearch.getText().trim() + "%'  "
		    + " order by a.strbillno DESC";
	    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (resultSet.next())
	    {
		Object row[] =
		{
		    resultSet.getString("BillNo"), resultSet.getString("POS"), resultSet.getString("Time"), resultSet.getString("TotalAmount")
		};
		dtmForDirectBiller.addRow(row);
	    }
	    tblShowData.setModel(dtmForDirectBiller);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblShowData.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funResetTableModel()
    {
	dtmDefaultModel.setRowCount(0);
	dtmForDinaKOT.setRowCount(0);
	dtmForDirectBiller.setRowCount(0);
	dtmForBill.setRowCount(0);
    }

    private void funGenerateTableForBill()
    {
	try
	{
	    dtmForBill = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"Bill NO", "Table Name", "Time", "Amount"
		    }
	    )
	    {
		Class[] types = new Class[]
		{
		    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
		};
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false
		};

		public Class getColumnClass(int columnIndex)
		{
		    return types[columnIndex];
		}

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };
	    dtmForBill.setRowCount(0);
	    tblShowData.setRowHeight(25);

	    String sql = "select a.strbillno as BillNo,ifnull(b.strTableName,'ND') as TableName,TIME_FORMAT(time(a.dteBillDate),'%h:%i') as Time"
		    + ",a.strPOSCode as POSCode,a.dblGrandTotal as TotalAmount "
		    + "from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + "where a.strPOSCode='" + clsGlobalVarClass.gPosCodeForReprintDocs + "' "
		    + " and (a.strbillno like '%" + txtSearch.getText().trim() + "%'  "
		    + " or  b.strTableName like '%" + txtSearch.getText().trim() + "%' ) "
		    + "order by a.strbillno DESC";
	    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (resultSet.next())
	    {
		Object row[] =
		{
		    resultSet.getString("BillNo"), resultSet.getString("TableName"), resultSet.getString("Time"), resultSet.getString("TotalAmount")
		};
		dtmForBill.addRow(row);
	    }
	    tblShowData.setModel(dtmForBill);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblShowData.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funResetSelection()
    {
	btnKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));

	operationType = "";
	kotFor = "";
    }

    private void funViewButtonPressed()
    {
	try
	{
	    viewORprint = "view";

	    if (operationType == null || operationType.length() == 0)
	    {
		new frmOkPopUp(this, "Please Select The Operation", "Warning", 1).setVisible(true);
		return;
	    }
	    if (operationType.equalsIgnoreCase("KOT"))
	    {
		if (kotFor == null || kotFor.length() == 0)
		{
		    new frmOkPopUp(this, "Please Select The Operation", "Warning", 1).setVisible(true);
		    return;
		}
	    }

	    if (operationType.equalsIgnoreCase("KOT"))
	    {
		if (kotFor.equalsIgnoreCase("Dina"))
		{
		    if (selectedKOT.length() == 0)
		    {
			new frmOkPopUp(this, "Please Select KOT No.", "Warning", 1).setVisible(true);
			return;
		    }
		    else
		    {
			try
			{
			    String sql = "select strTableNo from tblitemrtemp "
				    + "where strKOTNo='" + selectedKOT + "' "
				    + "group by strKOTNo ;";
			    ResultSet rsTableNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    rsTableNo.next();
			    String TableNo = rsTableNo.getString(1);
			    rsTableNo.close();

			    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
			    objKOTGeneration.funKOTGeneration(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "N");

//                            if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
//                            {
////                                clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
////                                ob.funRemotePrintUsingTextFile(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "N");
//                                
//                                clsKOTGeneration objKOTGeneration=new clsKOTGeneration();
//                                objKOTGeneration.funKOTGeneration(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "N");
//                            }
//                            else
//                            {
//                                clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//                                ob.funRemotePrintUsingTextFile(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "N");
//                                                                
//                            }
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			}
		    }
		    selectedKOT = "";
		}
		else if (kotFor.equalsIgnoreCase("DirectBiller"))
		{
		    if (selectedBill.length() == 0)
		    {
			new frmOkPopUp(this, "Please Select BillNo.", "Warning", 1).setVisible(true);
			return;
		    }
		    else
		    {
			if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
			{
			    // funTextFilePrintingBill(selectedBill.trim(),clsGlobalVarClass.getPOSDateForTransaction());                          
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
			else
			{
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
		    }
		    selectedBill = "";
		}

	    }
	    else if (operationType.equalsIgnoreCase("Bill"))
	    {
		if (selectedBill.length() == 0)
		{
		    new frmOkPopUp(this, "Please Select BillNo.", "Warning", 1).setVisible(true);
		    return;
		}
		else
		{
		    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
		    {
			funTextFilePreviewBill(selectedBill);
		    }
		    else
		    {
			funTextFilePreviewBill(selectedBill);
		    }
		}
		selectedBill = "";
	    }
	    else if (operationType.equalsIgnoreCase("DayEnd"))
	    {
		clsGlobalVarClass.gDayEndReportForm = "ReprintDayEndReport";
		try
		{
		    String dayEndDate = (dteForDayEnd.getDate().getYear() + 1900) + "-" + (dteForDayEnd.getDate().getMonth() + 1)
			    + "-" + (dteForDayEnd.getDate().getDate());

//                    clsTextFileGenerationForPrinting2 obj = new clsTextFileGenerationForPrinting2();
//                    obj.funGenerateTextDayEndReport(selectedPOSCode, dayEndDate, "reprint", clsGlobalVarClass.gShiftNo, "N");
		    clsDayEndTextReport objDayEndTextReport = new clsDayEndTextReport();
		    objDayEndTextReport.funGenerateTextDayEndReport(selectedPOSCode, dayEndDate, "reprint", clsGlobalVarClass.gShiftNo, "N");

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funShowNoOfReprints(String docNo, String transactionType)
    {
	try
	{
	    if (docNo.length() > 0 && transactionType.length() > 0)
	    {
		String sqlReprintCount = "select count(strDocNo) from tblaudit "
			+ " where strTransactionName='" + transactionType + "' and strDocNo='" + docNo + "'";
		ResultSet rsReprintCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlReprintCount);
		if (rsReprintCount.next())
		{
		    lblNoOfReprintLabel.setVisible(true);
		    lblNoOfReprints.setVisible(true);
		    lblNoOfReprintLabel.setText(docNo);
		    lblNoOfReprints.setText(String.valueOf(rsReprintCount.getInt(1)));
		}
		rsReprintCount.close();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funTableMouseClick(java.awt.event.MouseEvent evt)
    {
	String docNo = "", transactionType = "";
	int selectedRow = tblShowData.getSelectedRow();
	if (operationType.equalsIgnoreCase("KOT"))
	{
	    if (kotFor.equalsIgnoreCase("Dina"))
	    {
		selectedKOT = (String) tblShowData.getValueAt(selectedRow, 0);
		docNo = selectedKOT;
		transactionType = "KOT";
	    }
	    else if (kotFor.equalsIgnoreCase("DirectBiller"))
	    {
		selectedBill = (String) tblShowData.getValueAt(selectedRow, 0);
		docNo = selectedBill;
		transactionType = "Bill";
	    }
	}
	else if (operationType.equalsIgnoreCase("Bill"))
	{
	    selectedBill = (String) tblShowData.getValueAt(selectedRow, 0);
	    docNo = selectedBill;
	    transactionType = "Bill";
	}
	if (evt.getClickCount() == 2)
	{
	    if (operationType.equalsIgnoreCase("KOT"))
	    {
		if (kotFor.equalsIgnoreCase("Dina"))
		{
		    selectedKOT = (String) tblShowData.getValueAt(selectedRow, 0);
		}
		else if (kotFor.equalsIgnoreCase("DirectBiller"))
		{
		    selectedBill = (String) tblShowData.getValueAt(selectedRow, 0);
		}
	    }
	    else if (operationType.equalsIgnoreCase("Bill"))
	    {
		selectedBill = (String) tblShowData.getValueAt(selectedRow, 0);
	    }
	    funViewButtonPressed();
	}
	funShowNoOfReprints(docNo, transactionType);
    }

    private void funPrintButtonClick()
    {
	try
	{
	    viewORprint = "print";
	    if (operationType == null || operationType.length() == 0)
	    {
		new frmOkPopUp(this, "Please Select The Operation", "Warning", 1).setVisible(true);
		return;
	    }
	    if (operationType.equalsIgnoreCase("KOT"))
	    {
		if (kotFor == null || kotFor.length() == 0)
		{
		    new frmOkPopUp(this, "Please Select The Operation", "Warning", 1).setVisible(true);
		    return;
		}
	    }

	    if (operationType.equalsIgnoreCase("KOT"))
	    {
		if (kotFor.equalsIgnoreCase("Dina"))
		{
		    if (selectedKOT.length() == 0)
		    {
			new frmOkPopUp(this, "Please Select KOT No.", "Warning", 1).setVisible(true);
			return;
		    }
		    else
		    {
			try
			{
			    if (funSelectionOfRemark())
			    {
				return;
			    }

			    String sql = "select strTableNo from tblitemrtemp "
				    + "where strKOTNo='" + selectedKOT + "' "
				    + "group by strKOTNo ;";
			    ResultSet rsTableNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    rsTableNo.next();
			    String TableNo = rsTableNo.getString(1);
			    rsTableNo.close();

			    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
			    objKOTGeneration.funKOTGeneration(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "Y");

//                            if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
//                            {
//                                clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//                                ob.funRemotePrintUsingTextFile(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "Y");
//                            }
//                            else
//                            {
//                                clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//                                ob.funRemotePrintUsingTextFile(TableNo, selectedKOT.trim(), "", "Reprint", "Dina", "Y");
//                            }
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			}
		    }
		}
		else if (kotFor.equalsIgnoreCase("DirectBiller"))
		{
		    if (selectedBill.length() == 0)
		    {
			new frmOkPopUp(this, "Please Select BillNo.", "Warning", 1).setVisible(true);
			return;
		    }
		    else
		    {
			if (funSelectionOfRemark())
			{
			    return;
			}

			if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
			{
			    //funTextFilePrintingBill(selectedBill.trim(),clsGlobalVarClass.getPOSDateForTransaction());
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
			else
			{
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
		    }
		}

	    }
	    if (operationType.equalsIgnoreCase("Bill"))
	    {
		if (selectedBill.length() == 0)
		{
		    new frmOkPopUp(this, "Please Select BillNo.", "Warning", 1).setVisible(true);
		    return;
		}
		else
		{
		    try
		    {

			if (funSelectionOfRemark())
			{
			    return;
			}

			if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
			{
			    // funTextFilePrintingBill(selectedBill.trim(),clsGlobalVarClass.getPOSDateForTransaction());
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
			else
			{
			    objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
			}
		    }
		    catch (Exception e)
		    {
			e.printStackTrace();
		    }
		}
	    }
	    if (operationType.equalsIgnoreCase("DayEnd"))
	    {
		clsGlobalVarClass.gDayEndReportForm = "ReprintDayEndReport";
		try
		{
		    Date dt1 = dteForDayEnd.getDate();
		    int d = dt1.getDate();
		    int m = dt1.getMonth() + 1;
		    int y = dt1.getYear() + 1900;
		    dayEndDate = y + "-" + m + "-" + d;

		    if (funSelectionOfRemark())
		    {
			return;
		    }

		    clsDayEndTextReport objDayEndTextReport = new clsDayEndTextReport();
		    objDayEndTextReport.funGenerateTextDayEndReport(selectedPOSCode, dayEndDate, "reprint", clsGlobalVarClass.gShiftNo, "Y");

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }

	    if (clsGlobalVarClass.gClientCode.equals("240.001"))
	    {
		objUtility2.funSaveReprintAudit("Reprint", operationType, "", "Reprint from pending bills", selectedKOT, selectedBill, dayEndDate);
	    }
	    else
	    {
		objUtility2.funSaveReprintAudit("Reprint", operationType, selectedReasonCode, reprintRemarks, selectedKOT, selectedBill, dayEndDate);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean funSelectionOfRemark()
    {

	boolean isCanceled = false;
	try
	{
	    if (clsGlobalVarClass.gPrintRemarkAndReasonForReprint)
	    {
		if (clsGlobalVarClass.gTouchScreenMode)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Reprint Remark.").setVisible(true);
		    reprintRemarks = clsGlobalVarClass.gKeyboardValue;
		}
		else
		{
		    reprintRemarks = JOptionPane.showInputDialog(null, "Enter Reprint Remarks");
		}
		if (vReasonCodeForReprint.size() == 0)
		{
		    JOptionPane.showMessageDialog(this, "Please Create Reason For Reprint.");
		    isCanceled = true;
		}
		else
		{
		    Object[] arrObjReasonCode = vReasonCodeForReprint.toArray();
		    Object[] arrObjReasonName = vReasonNameForReprint.toArray();
		    String selectedReason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
		    if (null == selectedReason)
		    {
			JOptionPane.showMessageDialog(this, "Please Select Reason");
			isCanceled = true;
		    }
		    else
		    {
			for (int cntReason = 0; cntReason < vReasonCodeForReprint.size(); cntReason++)
			{
			    if (vReasonNameForReprint.elementAt(cntReason).toString().equals(selectedReason))
			    {
				selectedReasonCode = vReasonCodeForReprint.elementAt(cntReason).toString();
				break;
			    }
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return isCanceled;
	}

    }

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
        panelLayout = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBodyRoot = new javax.swing.JPanel();
        btnPOS1 = new javax.swing.JButton();
        btnPOS3 = new javax.swing.JButton();
        btnPOS2 = new javax.swing.JButton();
        btnPOS4 = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnKOT = new javax.swing.JButton();
        btnBill = new javax.swing.JButton();
        btnDayEnd = new javax.swing.JButton();
        btnDina = new javax.swing.JButton();
        btnDirectBiller = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tblShowData = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        panelForDayEndDate = new javax.swing.JPanel();
        dteForDayEnd = new com.toedter.calendar.JCalendar();
        lblNoOfReprintLabel = new javax.swing.JLabel();
        lblNoOfReprints = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS -");
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
        lblformName.setText(" - Re-Print Doc");
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

        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBodyRoot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBodyRoot.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBodyRoot.setOpaque(false);

        btnPOS1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPOS1.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPOS1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnPOS1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPOS1MouseClicked(evt);
            }
        });
        btnPOS1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPOS1ActionPerformed(evt);
            }
        });

        btnPOS3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPOS3.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPOS3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnPOS3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPOS3MouseClicked(evt);
            }
        });
        btnPOS3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPOS3ActionPerformed(evt);
            }
        });

        btnPOS2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPOS2.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPOS2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnPOS2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPOS2MouseClicked(evt);
            }
        });
        btnPOS2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPOS2ActionPerformed(evt);
            }
        });

        btnPOS4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPOS4.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPOS4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnPOS4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPOS4MouseClicked(evt);
            }
        });
        btnPOS4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPOS4ActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnPrevious.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevious.setText("<<");
        btnPrevious.setEnabled(false);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrevious.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPreviousMouseClicked(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNext.setText(">>");
        btnNext.setEnabled(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });

        btnKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnKOT.setText("KOT");
        btnKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnKOT.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnKOTMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnKOTMouseEntered(evt);
            }
        });

        btnBill.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnBill.setForeground(new java.awt.Color(255, 255, 255));
        btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnBill.setText("Bill");
        btnBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnBill.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillMouseClicked(evt);
            }
        });

        btnDayEnd.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDayEnd.setForeground(new java.awt.Color(255, 255, 255));
        btnDayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnDayEnd.setText("Day End");
        btnDayEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDayEnd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnDayEnd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDayEndMouseClicked(evt);
            }
        });

        btnDina.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDina.setForeground(new java.awt.Color(255, 255, 255));
        btnDina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnDina.setText("Dina");
        btnDina.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDina.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnDina.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDinaMouseClicked(evt);
            }
        });

        btnDirectBiller.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDirectBiller.setForeground(new java.awt.Color(255, 255, 255));
        btnDirectBiller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnDirectBiller.setText("<html>Direct<br>Biller</html>");
        btnDirectBiller.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDirectBiller.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png"))); // NOI18N
        btnDirectBiller.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDirectBillerMouseClicked(evt);
            }
        });

        scrollPane.setOpaque(false);

        dtmDefaultModel=new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
            },
            new String []
            {
                "", "", "", ""
            }
        );
        tblShowData.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        tblShowData.setModel(dtmDefaultModel);
        tblShowData.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblShowDataMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblShowData);

        btnView.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnView.setForeground(new java.awt.Color(255, 255, 255));
        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnView.setText("View");
        btnView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnView.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnView.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnViewActionPerformed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrintMouseClicked(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });

        panelForDayEndDate.setOpaque(false);
        panelForDayEndDate.setPreferredSize(new java.awt.Dimension(452, 402));

        javax.swing.GroupLayout panelForDayEndDateLayout = new javax.swing.GroupLayout(panelForDayEndDate);
        panelForDayEndDate.setLayout(panelForDayEndDateLayout);
        panelForDayEndDateLayout.setHorizontalGroup(
            panelForDayEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelForDayEndDateLayout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addComponent(dteForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        panelForDayEndDateLayout.setVerticalGroup(
            panelForDayEndDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelForDayEndDateLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(dteForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        lblNoOfReprintLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblNoOfReprints.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNoOfReprints.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        txtSearch.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchKeyReleased(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");

        javax.swing.GroupLayout panelBodyRootLayout = new javax.swing.GroupLayout(panelBodyRoot);
        panelBodyRoot.setLayout(panelBodyRootLayout);
        panelBodyRootLayout.setHorizontalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelBodyRootLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyRootLayout.createSequentialGroup()
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelBodyRootLayout.createSequentialGroup()
                                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelBodyRootLayout.createSequentialGroup()
                                    .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnPOS3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnPOS1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNoOfReprintLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnPOS2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnPOS4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNoOfReprints, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelBodyRootLayout.createSequentialGroup()
                                .addComponent(btnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyRootLayout.createSequentialGroup()
                                .addComponent(btnDina, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyRootLayout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSearch)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                        .addGap(140, 463, Short.MAX_VALUE)
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))))
            .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                    .addContainerGap(310, Short.MAX_VALUE)
                    .addComponent(panelForDayEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        panelBodyRootLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBill, btnDayEnd, btnDina, btnDirectBiller, btnKOT});

        panelBodyRootLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnPOS1, btnPOS2, btnPOS3, btnPOS4});

        panelBodyRootLayout.setVerticalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyRootLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoOfReprintLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNoOfReprints, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyRootLayout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyRootLayout.createSequentialGroup()
                        .addComponent(lblSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyRootLayout.createSequentialGroup()
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPOS1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPOS2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPOS3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPOS4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDina, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                    .addContainerGap(44, Short.MAX_VALUE)
                    .addComponent(panelForDayEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(88, Short.MAX_VALUE)))
        );

        panelBodyRootLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBill, btnDayEnd, btnKOT});

        panelLayout.add(panelBodyRoot, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPOS1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPOS1MouseClicked
	funSetPOS1Selected();
    }//GEN-LAST:event_btnPOS1MouseClicked

    private void btnPOS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS1ActionPerformed

    }//GEN-LAST:event_btnPOS1ActionPerformed

    private void btnPOS3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPOS3MouseClicked
	//selected
	btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	funSetPOSName(btnPOS3);
	//unselected
	btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));

	funResetTableModel();
	funResetSelection();
    }//GEN-LAST:event_btnPOS3MouseClicked

    private void btnPOS3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS3ActionPerformed

    }//GEN-LAST:event_btnPOS3ActionPerformed

    private void btnPOS2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPOS2MouseClicked
	//selected
	btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	funSetPOSName(btnPOS2);
	//unselected
	btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));

	funResetTableModel();
	funResetSelection();
    }//GEN-LAST:event_btnPOS2MouseClicked

    private void btnPOS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS2ActionPerformed

    }//GEN-LAST:event_btnPOS2ActionPerformed

    private void btnPOS4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPOS4MouseClicked
	//selected
	btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	funSetPOSName(btnPOS4);
	//unselected
	btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));

	funResetTableModel();
	funResetSelection();
    }//GEN-LAST:event_btnPOS4MouseClicked

    private void btnPOS4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS4ActionPerformed

    }//GEN-LAST:event_btnPOS4ActionPerformed

    private void btnPreviousMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPreviousMouseClicked
	if (prevCnt != 0)
	{
	    funPreviousButtonPressed();
	}

    }//GEN-LAST:event_btnPreviousMouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
	funNextButtonPressed();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnKOTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKOTMouseClicked

	funSetKOTOperationSelected();
	funSetDinaSelected();
	funExecute();
    }//GEN-LAST:event_btnKOTMouseClicked

    private void btnBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillMouseClicked

	funBillMouseClicked();
    }//GEN-LAST:event_btnBillMouseClicked

    private void btnDayEndMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDayEndMouseClicked
	funSetDayEndOperationSelected();
	funExecute();
    }//GEN-LAST:event_btnDayEndMouseClicked

    private void btnDinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDinaMouseClicked
	funSetKOTOperationSelected();
	funSetDinaSelected();
	funExecute();
    }//GEN-LAST:event_btnDinaMouseClicked

    private void btnDirectBillerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDirectBillerMouseClicked
	funSetKOTOperationSelected();
	funSetDirectBillerSelected();
	funExecute();
    }//GEN-LAST:event_btnDirectBillerMouseClicked

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
	funPrintButtonClick();
    }//GEN-LAST:event_btnPrintMouseClicked


    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Reprint");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnKOTMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnKOTMouseEntered
    {//GEN-HEADEREND:event_btnKOTMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnKOTMouseEntered

    private void tblShowDataMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblShowDataMouseClicked
    {//GEN-HEADEREND:event_tblShowDataMouseClicked
	funTableMouseClick(evt);
    }//GEN-LAST:event_tblShowDataMouseClicked

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewActionPerformed
    {//GEN-HEADEREND:event_btnViewActionPerformed
	// TODO add your handling code here:
	funViewButtonPressed();
    }//GEN-LAST:event_btnViewActionPerformed

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
	clsGlobalVarClass.hmActiveForms.remove("Reprint");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Reprint");
    }//GEN-LAST:event_formWindowClosing

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSearchMouseClicked
    {//GEN-HEADEREND:event_txtSearchMouseClicked
	// TODO add your handling code here:
	frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search...");
	keyboard.setVisible(true);
	keyboard.setAlwaysOnTop(true);
	keyboard.setAutoRequestFocus(true);
	txtSearch.setText(clsGlobalVarClass.gKeyboardValue);

	funFillDataWithSearchData(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyReleased
    {//GEN-HEADEREND:event_txtSearchKeyReleased

	funFillDataWithSearchData(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void funTextFilePreviewBill(String billNo)
    {
	try
	{
	    // funTextFilePrintingBill(selectedBill.trim(),clsGlobalVarClass.getPOSDateForTransaction()); 
	    //objUtility.funPrintBill(selectedBill.trim(), clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, selectedPOSCode, viewORprint);
	    objUtility.funPrintBill(selectedBill.trim(), "Reprint Document", clsGlobalVarClass.getOnlyPOSDateForTransaction(), selectedPOSCode, "view");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBill;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDayEnd;
    private javax.swing.JButton btnDina;
    private javax.swing.JButton btnDirectBiller;
    private javax.swing.JButton btnKOT;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPOS1;
    private javax.swing.JButton btnPOS2;
    private javax.swing.JButton btnPOS3;
    private javax.swing.JButton btnPOS4;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnView;
    private com.toedter.calendar.JCalendar dteForDayEnd;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNoOfReprintLabel;
    private javax.swing.JLabel lblNoOfReprints;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBodyRoot;
    private javax.swing.JPanel panelForDayEndDate;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblShowData;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private void funLoadReprintResons()
    {
	try
	{
	    vReasonCodeForReprint = new Vector();
	    vReasonNameForReprint = new Vector();
	    String sql_Reason = "select strReasonCode,strReasonName from tblreasonmaster where strReprint='Y'";
	    ResultSet rsDiscReason = clsGlobalVarClass.dbMysql.executeResultSet(sql_Reason);
	    while (rsDiscReason.next())
	    {
		vReasonCodeForReprint.add(rsDiscReason.getString(1));
		vReasonNameForReprint.add(rsDiscReason.getString(2));
	    }
	    rsDiscReason.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-9", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funSetPOS1Selected()
    {
	//selected
	btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection1.png")));
	funSetPOSName(btnPOS1);
	//unselected
	btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));
	btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png")));

	funResetTableModel();
	funResetSelection();
    }

    private void funBillMouseClicked()
    {
	funSetBillOperationSelected();
	funExecute();
    }

    private void funFillDataWithSearchData(String searchText)
    {
	funExecute();
    }

}
