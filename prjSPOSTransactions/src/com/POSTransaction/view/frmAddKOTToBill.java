package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmAddKOTToBill extends javax.swing.JFrame
{

    public java.util.Vector vOpenKOTNo, vUnsettledBills, vOpenTableNo;
    public String sql, clsOpenKOTNo, clsOpenTableNo;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    public List<String> listSelectedKOTs = new ArrayList<String>();
    private String clsBillNo = null;
    public frmMakeKOT objMakeKOT;
    public frmMakeBill objMakeBill;
    public boolean flgMergeKOTToBill;

    public frmAddKOTToBill()
    {
	initComponents();

	try
	{
	    vOpenKOTNo = new Vector();
	    vOpenTableNo = new Vector();
	    vUnsettledBills = new Vector();
	    btnPreviousKOTNo.setEnabled(false);
	    btnPreviousBillNo.setEnabled(false);
	    cntNavigate = 0;
	    cntNavigate1 = 0;
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    Date date1 = new Date();
	    String new_str = String.format("%tr", date1);
	    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
	    lblDate.setText(dateAndTime);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    funFillTableNames();
	    funFillOpenKOT();
	    funFillUnsettledBills("");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public frmAddKOTToBill(String transactionName,frmMakeKOT objMakeKOT)
    {
	this.objMakeKOT=objMakeKOT;
    }
    public frmAddKOTToBill(String transactionName,frmMakeBill objMakeBill)
    {
	this.objMakeBill=objMakeBill;
    }

    private void funFillUnsettledBills(String searchBillNo)
    {
	try
	{

	    btnPreviousBillNo.setEnabled(false);
	    btnNextBillNo.setEnabled(true);
	    vUnsettledBills.removeAllElements();
	    sql = "select strBillNo from tblbillhd"
		    + " where strBillNo not in (select strBillNo from tblbillsettlementdtl) and strTableNo<>'' "
		    + " and strOperationType='DineIn' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";

	    if (searchBillNo.trim().length() > 0)
	    {
		sql += " and strBillNo like '" + searchBillNo + "%' ";
	    }
	    /*sql = "select strBillNo from tblbillhd"
             + " where strBillNo not in (select strBillNo from tblbillsettlementdtl) and strTableNo<>'' ";*/

	    ResultSet rsBillNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillNo.next())
	    {
		vUnsettledBills.add(rsBillNo.getString(1));
	    }
	    rsBillNo.close();
	    funLoadBillNo(0, vUnsettledBills.size());

	    if (vUnsettledBills.size() <= 16)
	    {
		btnNextBillNo.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadBillNo(int startIndex, int totalSize)
    {
	try
	{
	    int cntIndex = 0;
	    JButton[] btnTableArray =
	    {
		btnTableNo1, btnTableNo2, btnTableNo3, btnTableNo4, btnTableNo5, btnTableNo6, btnTableNo7, btnTableNo8, btnTableNo9, btnTableNo10, btnTableNo11, btnTableNo12, btnTableNo13, btnTableNo14, btnTableNo15, btnTableNo16
	    };
	    for (int k = 0; k < btnTableArray.length; k++)
	    {
		btnTableArray[k].setForeground(Color.black);
		btnTableArray[k].setBackground(Color.LIGHT_GRAY);
		btnTableArray[k].setText("");
	    }
	    for (int i = startIndex; i < totalSize; i++)
	    {
		if (i == vUnsettledBills.size())
		{
		    break;
		}
		String billNo = vUnsettledBills.elementAt(i).toString();

		if (cntIndex < 16)
		{
		    btnTableArray[cntIndex].setText(billNo);
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

    private void funFillOpenKOT() throws Exception
    {
	btnPreviousKOTNo.setEnabled(false);
	btnNextKOTNo.setEnabled(true);
	vOpenKOTNo.removeAllElements();
	sql = "select distinct(a.strKOTNo),a.strTableNo,b.strTableName "
		+ "from tblitemrtemp a,tbltablemaster b "
		+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strNCKOTYN='N'  "
		+ "and a.strTableNo=b.strTableNo ";
	if (!cmbTableNames.getSelectedItem().toString().equalsIgnoreCase("All"))
	{
	    sql += "and b.strTableName = '" + cmbTableNames.getSelectedItem().toString() + "' ";
	}

	sql += "order by a.strKOTNo ";

	ResultSet rsKOTNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsKOTNo.next())
	{
	    vOpenKOTNo.add(rsKOTNo.getString(1));
	    vOpenTableNo.add(rsKOTNo.getString(2));
	}
	rsKOTNo.close();
	funLoadOpenKOTs(0, vOpenKOTNo.size());

	if (vOpenKOTNo.size() <= 16)
	{
	    btnNextKOTNo.setEnabled(false);
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

	    funSetDefaultColorOpen();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funPreviousBillButtonClicked()
    {
	try
	{
	    cntNavigate1--;
	    btnNextBillNo.setEnabled(true);
	    if (cntNavigate1 == 0)
	    {
		btnPreviousBillNo.setEnabled(false);
		funLoadBillNo(0, vUnsettledBills.size());
	    }
	    else
	    {
		int tableSize = cntNavigate1 * 16;
		int totalSize = tableSize + 16;
		funLoadBillNo(tableSize, totalSize);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funNextBillNoButtonClicked()
    {
	try
	{
	    cntNavigate1++;
	    int tableSize = cntNavigate1 * 16;
	    int resDiv = vUnsettledBills.size() / 16;
	    int totalSize = tableSize + 16;
	    tblStartIndex = tableSize;
	    tblEndIndex = totalSize;
	    funLoadBillNo(tableSize, totalSize);
	    btnPreviousBillNo.setEnabled(true);
	    if (resDiv == cntNavigate1)
	    {
		btnNextBillNo.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetDefaultColorOpen()
    {
	try
	{
	    JButton[] btnTableArray =
	    {
		btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16
	    };

	    for (JButton btnTableArray1 : btnTableArray)
	    {
		String kotn = btnTableArray1.getText().trim();
		if (listSelectedKOTs.contains(kotn))
		{
		    btnTableArray1.setBackground(Color.BLUE);
		}
		else
		{
		    btnTableArray1.setBackground(Color.LIGHT_GRAY);
		}
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
	    if (listSelectedKOTs.contains(kotNo))
	    {
		listSelectedKOTs.remove(kotNo);
	    }
	    else
	    {
		listSelectedKOTs.add(kotNo);
	    }
	    funSetDefaultColorOpen();
	    funSetSelectedKOTNoToLabel();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
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
	    if (btnColor != Color.black)
	    {
		btnTableArray[btnIndex].setBackground(Color.BLACK);
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

    private void funSelectTable(String tableName, int index)
    {
	try
	{
	    if (tableName.trim().length() > 0)
	    {
		int currentIndex = (16 * cntNavigate1) + index;
		clsBillNo = vUnsettledBills.elementAt(currentIndex).toString();
		lblBillNo.setText(clsBillNo);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAddKOTToBill(String billNo, String kotNo)
    {
	try
	{
	    String tableNo = "";
	    String areaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    String sqlArea = "select date(a.dteBillDate),b.strAreaCode,a.strTableNo "
		    + "from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + "where a.strBillNo='" + billNo + "'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
	    if (rsAreaCode.next())
	    {
		areaCode = rsAreaCode.getString(2);
		tableNo = rsAreaCode.getString(3);
	    }
	    rsAreaCode.close();

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

	    ResultSet rsTableNos = clsGlobalVarClass.dbMysql.executeResultSet("select strTableNo from tblitemrtemp "
		    + "where strKOTNo In " + kots + " "
		    + "group by  strKOTNo ");
	    while (rsTableNos.next())
	    {
		if (rsTableNos.isFirst())
		{
		    tableNo = "(" + "'" + rsTableNos.getString(1) + "'";
		}
		else
		{
		    tableNo = tableNo + "," + "'" + rsTableNos.getString(1) + "'";
		}
	    }
	    rsTableNos.close();

	    tableNo = tableNo + ")";
	    clsGlobalVarClass.gTransactionType = "AddKOTToBill";
	    new frmBillSettlement(this, billNo, areaCode, kotNo, tableNo).setVisible(true);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * @return the listSelectedKOTs
     */
    public List<String> getList_Selected_KOTs()
    {
	return listSelectedKOTs;
    }

   

    private void funSetSelectedKOTNoToLabel()
    {
	String strKOT = "";
	boolean first = true;
	for (String KOTno : listSelectedKOTs)
	{
	    if (first)
	    {
		strKOT += KOTno;
		first = false;
	    }
	    else
	    {
		strKOT += "," + KOTno;
	    }
	}
	lblKOTNoS.setText("");
	lblKOTNoS.setText(strKOT);
    }

    private void funSaveButtonPressed()
    {
	if (listSelectedKOTs.isEmpty())
	{
	    JOptionPane.showMessageDialog(this, "Please Select KOT");
	    return;
	}
	if (null == clsBillNo || lblBillNo.getText().trim().length() == 0)
	{
	    JOptionPane.showMessageDialog(this, "Please Select Bill");
	    return;
	}
	funAddKOTToBill(clsBillNo, clsOpenKOTNo);
    }

    private void funPreviousKOTNoButtonClicked()
    {
	try
	{
	    cntNavigate--;
	    btnNextKOTNo.setEnabled(true);
	    if (cntNavigate == 0)
	    {
		btnPreviousKOTNo.setEnabled(false);
		funLoadOpenKOTs(0, vOpenKOTNo.size());
	    }
	    else
	    {
		int tableSize = cntNavigate * 16;
		int totalSize = tableSize + 16;
		funLoadOpenKOTs(tableSize, totalSize);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funNextKOTNoButtonClicked()
    {
	try
	{
	    cntNavigate++;
	    int tableSize = cntNavigate * 16;
	    int resDiv = vOpenKOTNo.size() / tableSize;
	    int totalSize = tableSize + 16;
	    funLoadOpenKOTs(tableSize, totalSize);
	    btnPreviousKOTNo.setEnabled(true);
	    if (resDiv == cntNavigate)
	    {
		btnNextKOTNo.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
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
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        lblOpenTable = new javax.swing.JLabel();
        lblOpenTable1 = new javax.swing.JLabel();
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
        lblKOTNoS = new javax.swing.JLabel();
        btnPreviousKOTNo = new javax.swing.JButton();
        btnNextKOTNo = new javax.swing.JButton();
        btnPreviousBillNo = new javax.swing.JButton();
        lblBillNo = new javax.swing.JLabel();
        btnNextBillNo = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        cmbTableNames = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        txtSearchBillNo = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS -   ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" -Add KOT To Bill");
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

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);
        panelFormBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblOpenTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblOpenTable.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable.setText("OPEN KOT");
        panelFormBody.add(lblOpenTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 2, 121, 20));

        lblOpenTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblOpenTable1.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable1.setText("BILLS");
        panelFormBody.add(lblOpenTable1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 70, 20));

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);
        panelOpenTable.setOpaque(false);

        btnOpenTable2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable2MouseClicked(evt);
            }
        });

        btnOpenTable1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable1MouseClicked(evt);
            }
        });

        btnOpenTable3.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable3MouseClicked(evt);
            }
        });

        btnOpenTable4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable4MouseClicked(evt);
            }
        });

        btnOpenTable5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable5MouseClicked(evt);
            }
        });

        btnOpenTable6.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable6MouseClicked(evt);
            }
        });

        btnOpenTable7.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable7MouseClicked(evt);
            }
        });

        btnOpenTable8.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable8MouseClicked(evt);
            }
        });

        btnOpenTable9.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable9MouseClicked(evt);
            }
        });

        btnOpenTable10.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable10MouseClicked(evt);
            }
        });

        btnOpenTable11.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable11MouseClicked(evt);
            }
        });

        btnOpenTable12.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable12MouseClicked(evt);
            }
        });

        btnOpenTable13.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable13MouseClicked(evt);
            }
        });

        btnOpenTable14.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable14MouseClicked(evt);
            }
        });

        btnOpenTable15.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOpenTable15MouseClicked(evt);
            }
        });

        btnOpenTable16.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnOpenTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
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

        panelFormBody.add(panelOpenTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 69, -1, -1));

        panelAllTables.setBackground(new java.awt.Color(255, 255, 255));
        panelAllTables.setEnabled(false);
        panelAllTables.setOpaque(false);

        btnTableNo2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo2MouseClicked(evt);
            }
        });

        btnTableNo1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo1MouseClicked(evt);
            }
        });

        btnTableNo3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo3MouseClicked(evt);
            }
        });

        btnTableNo4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo4MouseClicked(evt);
            }
        });

        btnTableNo5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo5MouseClicked(evt);
            }
        });

        btnTableNo6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo6MouseClicked(evt);
            }
        });

        btnTableNo7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo7MouseClicked(evt);
            }
        });

        btnTableNo8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo8MouseClicked(evt);
            }
        });

        btnTableNo9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo9MouseClicked(evt);
            }
        });

        btnTableNo10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo10MouseClicked(evt);
            }
        });

        btnTableNo11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo11MouseClicked(evt);
            }
        });

        btnTableNo12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo12MouseClicked(evt);
            }
        });

        btnTableNo13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo13MouseClicked(evt);
            }
        });

        btnTableNo14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo14MouseClicked(evt);
            }
        });

        btnTableNo15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTableNo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTableNo15MouseClicked(evt);
            }
        });

        btnTableNo16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
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

        panelFormBody.add(panelAllTables, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, -1));
        panelFormBody.add(lblKOTNoS, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, 176, 40));

        btnPreviousKOTNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPreviousKOTNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPreviousKOTNo.setText("<<<");
        btnPreviousKOTNo.setToolTipText("Previous Open KOTs");
        btnPreviousKOTNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreviousKOTNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPreviousKOTNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousKOTNoActionPerformed(evt);
            }
        });
        panelFormBody.add(btnPreviousKOTNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 60, 40));

        btnNextKOTNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextKOTNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextKOTNo.setText(">>>");
        btnNextKOTNo.setToolTipText("Next Open KOTs");
        btnNextKOTNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextKOTNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextKOTNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextKOTNoActionPerformed(evt);
            }
        });
        panelFormBody.add(btnNextKOTNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 470, 60, 40));

        btnPreviousBillNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPreviousBillNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPreviousBillNo.setText("<<<");
        btnPreviousBillNo.setToolTipText("Previous Bill No");
        btnPreviousBillNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreviousBillNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPreviousBillNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousBillNoActionPerformed(evt);
            }
        });
        panelFormBody.add(btnPreviousBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 470, 60, 40));
        panelFormBody.add(lblBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 470, 162, 40));

        btnNextBillNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextBillNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextBillNo.setText(">>>");
        btnNextBillNo.setToolTipText("Next Bill No");
        btnNextBillNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextBillNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextBillNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextBillNoActionPerformed(evt);
            }
        });
        panelFormBody.add(btnNextBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 470, 60, 40));

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setToolTipText("Add KOT to Bill");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        panelFormBody.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(516, 525, 114, 40));

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Close Form");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });
        panelFormBody.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(648, 525, 114, 41));

        cmbTableNames.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTableNamesActionPerformed(evt);
            }
        });
        panelFormBody.add(cmbTableNames, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 29, 179, 36));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Bill No. :");
        panelFormBody.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 50, 30));

        txtSearchBillNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchBillNoMouseClicked(evt);
            }
        });
        txtSearchBillNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchBillNoKeyReleased(evt);
            }
        });
        panelFormBody.add(txtSearchBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 145, 30));

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");
        panelFormBody.add(lblSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Table Name :");
        panelFormBody.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 28, -1, 36));

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable2MouseClicked
	funSelectOpenTable(btnOpenTable2.getText(), 1);
    }//GEN-LAST:event_btnOpenTable2MouseClicked

    private void btnOpenTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable1MouseClicked
	funSelectOpenTable(btnOpenTable1.getText().trim(), 0);
    }//GEN-LAST:event_btnOpenTable1MouseClicked

    private void btnOpenTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable3MouseClicked
	funSelectOpenTable(btnOpenTable3.getText(), 2);
    }//GEN-LAST:event_btnOpenTable3MouseClicked

    private void btnOpenTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable4MouseClicked
	funSelectOpenTable(btnOpenTable4.getText(), 3);
    }//GEN-LAST:event_btnOpenTable4MouseClicked

    private void btnOpenTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable5MouseClicked
	funSelectOpenTable(btnOpenTable5.getText(), 4);
    }//GEN-LAST:event_btnOpenTable5MouseClicked

    private void btnOpenTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable6MouseClicked
	funSelectOpenTable(btnOpenTable6.getText(), 5);
    }//GEN-LAST:event_btnOpenTable6MouseClicked

    private void btnOpenTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable7MouseClicked
	funSelectOpenTable(btnOpenTable7.getText(), 6);
    }//GEN-LAST:event_btnOpenTable7MouseClicked

    private void btnOpenTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable8MouseClicked
	funSelectOpenTable(btnOpenTable8.getText(), 7);
    }//GEN-LAST:event_btnOpenTable8MouseClicked

    private void btnOpenTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable9MouseClicked
	funSelectOpenTable(btnOpenTable9.getText(), 8);
    }//GEN-LAST:event_btnOpenTable9MouseClicked

    private void btnOpenTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable10MouseClicked
	funSelectOpenTable(btnOpenTable10.getText(), 9);
    }//GEN-LAST:event_btnOpenTable10MouseClicked

    private void btnOpenTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable11MouseClicked
	funSelectOpenTable(btnOpenTable11.getText(), 10);
    }//GEN-LAST:event_btnOpenTable11MouseClicked

    private void btnOpenTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable12MouseClicked
	funSelectOpenTable(btnOpenTable12.getText(), 11);
    }//GEN-LAST:event_btnOpenTable12MouseClicked

    private void btnOpenTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable13MouseClicked
	funSelectOpenTable(btnOpenTable13.getText(), 12);
    }//GEN-LAST:event_btnOpenTable13MouseClicked

    private void btnOpenTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable14MouseClicked
	funSelectOpenTable(btnOpenTable14.getText(), 13);
    }//GEN-LAST:event_btnOpenTable14MouseClicked

    private void btnOpenTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable15MouseClicked
	funSelectOpenTable(btnOpenTable15.getText(), 14);
    }//GEN-LAST:event_btnOpenTable15MouseClicked

    private void btnOpenTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable16MouseClicked
	// TODO add your handling code here:
	funSelectOpenTable(btnOpenTable16.getText(), 15);
    }//GEN-LAST:event_btnOpenTable16MouseClicked

    private void btnTableNo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo2MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(1);
	funSelectTable(btnTableNo2.getText(), 1);
    }//GEN-LAST:event_btnTableNo2MouseClicked

    private void btnTableNo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo1MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(0);
	funSelectTable(btnTableNo1.getText(), 0);
    }//GEN-LAST:event_btnTableNo1MouseClicked

    private void btnTableNo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo3MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(2);
	funSelectTable(btnTableNo3.getText(), 2);
    }//GEN-LAST:event_btnTableNo3MouseClicked

    private void btnTableNo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo4MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(3);
	funSelectTable(btnTableNo4.getText(), 3);
    }//GEN-LAST:event_btnTableNo4MouseClicked

    private void btnTableNo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo5MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(4);
	funSelectTable(btnTableNo5.getText(), 4);
    }//GEN-LAST:event_btnTableNo5MouseClicked

    private void btnTableNo6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo6MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(5);
	funSelectTable(btnTableNo6.getText(), 5);
    }//GEN-LAST:event_btnTableNo6MouseClicked

    private void btnTableNo7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo7MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(6);
	funSelectTable(btnTableNo7.getText(), 6);
    }//GEN-LAST:event_btnTableNo7MouseClicked

    private void btnTableNo8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo8MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(7);
	funSelectTable(btnTableNo8.getText(), 7);
    }//GEN-LAST:event_btnTableNo8MouseClicked

    private void btnTableNo9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo9MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(8);
	funSelectTable(btnTableNo9.getText(), 8);
    }//GEN-LAST:event_btnTableNo9MouseClicked

    private void btnTableNo10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo10MouseClicked
	// TODO add your handling code here:
	funSetDefaultColorAll(9);
	funSelectTable(btnTableNo10.getText(), 9);
    }//GEN-LAST:event_btnTableNo10MouseClicked

    private void btnTableNo11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo11MouseClicked
	// TODO add your handling code here:
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
	// TODO add your handling code here:
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

    private void btnPreviousKOTNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousKOTNoActionPerformed
	// TODO add your handling code here:
	funPreviousKOTNoButtonClicked();
    }//GEN-LAST:event_btnPreviousKOTNoActionPerformed

    private void btnNextKOTNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextKOTNoActionPerformed
	// TODO add your handling code here:
	funNextKOTNoButtonClicked();
    }//GEN-LAST:event_btnNextKOTNoActionPerformed

    private void btnPreviousBillNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousBillNoActionPerformed
	// TODO add your handling code here:        
	funPreviousBillButtonClicked();
    }//GEN-LAST:event_btnPreviousBillNoActionPerformed

    private void btnNextBillNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextBillNoActionPerformed
	// TODO add your handling code here:
	funNextBillNoButtonClicked();
    }//GEN-LAST:event_btnNextBillNoActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
	// TODO add your handling code here: 
	funSaveButtonPressed();
	// funAddKOTToBill(clsBillNo,clsOpenKOTNo);
	dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Add KOT To Bill");
	System.gc();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Add KOT To Bill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Add KOT To Bill");
    }//GEN-LAST:event_formWindowClosing

    private void cmbTableNamesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTableNamesActionPerformed
    {//GEN-HEADEREND:event_cmbTableNamesActionPerformed
	funOpenKOTsSearchKeyPressed();
    }//GEN-LAST:event_cmbTableNamesActionPerformed

    private void txtSearchBillNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSearchBillNoMouseClicked
    {//GEN-HEADEREND:event_txtSearchBillNoMouseClicked
	// TODO add your handling code here:
	frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search All Tables");
	keyboard.setVisible(true);
	keyboard.setAlwaysOnTop(true);
	keyboard.setAutoRequestFocus(true);
	txtSearchBillNo.setText(clsGlobalVarClass.gKeyboardValue);

	funFillUnsettledBills(txtSearchBillNo.getText().trim());
    }//GEN-LAST:event_txtSearchBillNoMouseClicked

    private void txtSearchBillNoKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchBillNoKeyReleased
    {//GEN-HEADEREND:event_txtSearchBillNoKeyReleased

	funFillUnsettledBills(txtSearchBillNo.getText().trim());
    }//GEN-LAST:event_txtSearchBillNoKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNextBillNo;
    private javax.swing.JButton btnNextKOTNo;
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
    private javax.swing.JButton btnPreviousBillNo;
    private javax.swing.JButton btnPreviousKOTNo;
    private javax.swing.JButton btnSave;
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
    private javax.swing.JComboBox cmbTableNames;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblKOTNoS;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTable;
    private javax.swing.JLabel lblOpenTable1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAllTables;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    private javax.swing.JTextField txtSearchBillNo;
    // End of variables declaration//GEN-END:variables
   private void funOpenKOTsSearchKeyPressed()
    {
	try
	{
	    funFillOpenKOT();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    Logger.getLogger(frmAddKOTToBill.class.getName()).log(Level.SEVERE, null, e);
	}
    }

    private void funFillTableNames()
    {
	try
	{
	    String sqlQuery = "select distinct(b.strTableName) "
		    + "from tblitemrtemp a,tbltablemaster b "
		    + "where a.strTableNo=b.strTableNo "
		    + "and (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  or a.strPOSCode='All')";
	    ResultSet rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    cmbTableNames.removeAllItems();
	    cmbTableNames.addItem("All");
	    while (rsTables.next())
	    {
		cmbTableNames.addItem(rsTables.getString(1));
	    }
	    rsTables.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public List<String> getListSelectedKOTs()
    {
	return listSelectedKOTs;
    }

    public void setListSelectedKOTs(List<String> listSelectedKOTs)
    {
	this.listSelectedKOTs = listSelectedKOTs;
    }

    public frmMakeKOT getObjMakeKOT()
    {
	return this.objMakeKOT;
    }
    
    
}
