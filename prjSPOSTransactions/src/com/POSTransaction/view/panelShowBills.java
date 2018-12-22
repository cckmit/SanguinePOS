package com.POSTransaction.view;

import com.POSGlobal.controller.clsBenowIntegration;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUPIBean;
import com.POSGlobal.controller.clsUtility;
import com.POSTransaction.controller.clsBillDiscountDtl;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class panelShowBills extends javax.swing.JPanel
{

    private String areaCode;
    frmBillSettlement obj;
    private Vector vTableNo;
    clsUtility objUtility;
    int currentRow = 0;
    Map<String, String> mapTableNameWithCode;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public panelShowBills(Object ob)
    {
	initComponents();
	obj = (frmBillSettlement) ob;
	objUtility = new clsUtility();
	mapTableNameWithCode = new HashMap<String, String>();

	btnLast3UPIs.setVisible(false);
	if (clsGlobalVarClass.gBenowIntegrationYN)
	{
	    btnLast3UPIs.setVisible(true);
	}

	cmbBilledTables.addItemListener(new ItemChangeListener());

	funFillTableCombo();
	funFillUnsettledBills();

	tblBills.requestFocus();
	tblBills.requestFocusInWindow();
	tblBills.addKeyListener(new KeyListener()
	{

	    @Override
	    public void keyTyped(KeyEvent evt)
	    {
		tblBills.requestFocus();
	    }

	    @Override
	    public void keyPressed(KeyEvent evt)
	    {
		if (evt.getKeyCode() == 10) // Check for Enter Key Press
		{
		    funTableRowClicked();
		}

		if (evt.getKeyCode() == 40)
		{
		    funDownArrowPressed();
		}
		if (evt.getKeyCode() == 38)
		{
		    funUpArrowPressed();
		}
	    }

	    @Override
	    public void keyReleased(KeyEvent e)
	    {
		tblBills.requestFocus();
	    }
	});

    }

    public void funFillUnsettledBills()
    {
	try
	{

	    double dblTotalSettleAmt = 0.00;

	    String tableName = cmbBilledTables.getSelectedItem().toString();

	    vTableNo = new Vector();
	    DefaultTableModel dmBills = (DefaultTableModel) tblBills.getModel();
	    dmBills.setRowCount(0);
	    dmBills.setColumnCount(0);
	    tblBills.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

	    if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
	    {
		dmBills.addColumn("Bill No");
		dmBills.addColumn("Time");
		dmBills.addColumn("Table");
		dmBills.addColumn("Waiter");
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    dmBills.addColumn("Member");
		}
		else
		{
		    dmBills.addColumn("Customer");
		}
		dmBills.addColumn("Amount");
	    }
	    else//Delivery Detail Wise
	    {
		dmBills.addColumn("Bill No");
		dmBills.addColumn("Time");
		dmBills.addColumn("Table");
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    dmBills.addColumn("Member");
		}
		else
		{
		    dmBills.addColumn("Customer");
		}
		dmBills.addColumn("Area");
		dmBills.addColumn("Del Boy");
		dmBills.addColumn("Amount");
	    }
	    String sql = "";
	    if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
	    {
		sql = "select a.strBillNo,ifnull(b.strTableNo,''),ifnull(b.strTableName,''),ifnull(c.strWaiterNo,'')\n"
			+ " ,ifnull(c.strWShortName,''),ifnull(d.strCustomerCode,''),ifnull(d.strCustomerName,''),a.dblGrandTotal"
			+ " ,DATE_FORMAT(a.dteBillDate,\"%h:%i\")  \n"
			+ " from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo\n"
			+ " left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo\n"
			+ " left outer join tblcustomermaster d on a.strCustomerCode=d.strCustomerCode\n"
			+ " where  a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			+ " and a.strSettelmentMode='' ";
		if (!tableName.equalsIgnoreCase("All"))
		{
		    String tableCode = mapTableNameWithCode.get(tableName);
		    sql = sql + " and a.strTableNo='" + tableCode + "' ";
		}
		sql = sql + " order by a.dteBillDate desc ";
	    }
	    else//Delivery Detail Wise
	    {
		sql = "SELECT a.strBillNo,IFNULL(d.strCustomerName,''),ifnull(e.strBuildingName,''),ifnull(f.strDPName,'')"
			+ " ,a.dblGrandTotal,ifnull(g.strTableNo,''),ifnull(g.strTableName,''),DATE_FORMAT(a.dteBillDate,\"%h:%i\") "
			+ " FROM tblbillhd a "
			+ " left outer join tblhomedeldtl b on a.strBillNo=b.strBillNo "
			+ " LEFT OUTER JOIN tblcustomermaster d ON a.strCustomerCode=d.strCustomerCode "
			+ " left outer join tblbuildingmaster e on d.strBuldingCode=e.strBuildingCode "
			+ " left outer join tbldeliverypersonmaster  f on  f.strDPCode=b.strDPCode "
			+ " left outer join tbltablemaster g on a.strTableNo=g.strTableNo "
			+ " where  a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			+ " and a.strSettelmentMode='' ";

		if (!tableName.equalsIgnoreCase("All"))
		{
		    String tableCode = mapTableNameWithCode.get(tableName);
		    sql = sql + " and a.strTableNo='" + tableCode + "' ";
		}
		sql = sql + " group by a.strBillNo"
			+ " order by a.dteBillDate desc ";

	    }
	    ResultSet rsPendingBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsPendingBills.next())
	    {
		if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
		{
		    Object[] ob =
		    {
			rsPendingBills.getString(1), rsPendingBills.getString(9), rsPendingBills.getString(3), rsPendingBills.getString(5), rsPendingBills.getString(7), gDecimalFormat.format(rsPendingBills.getDouble(8))

		    };
		    dmBills.addRow(ob);
		    vTableNo.add(rsPendingBills.getString(2));

		    dblTotalSettleAmt += rsPendingBills.getDouble(8);//settleAmt
		}
		else//Delivery Detail Wise
		{
		    Object[] ob =
		    {
			rsPendingBills.getString(1), rsPendingBills.getString(8), rsPendingBills.getString(7), rsPendingBills.getString(2), rsPendingBills.getString(3), rsPendingBills.getString(4), gDecimalFormat.format(rsPendingBills.getDouble(5))
		    };
		    dmBills.addRow(ob);
		    vTableNo.add(rsPendingBills.getString(6));

		    dblTotalSettleAmt += rsPendingBills.getDouble(5);//settleAmt
		}
	    }
	    rsPendingBills.close();

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblBills.getColumnModel().getColumn(tblBills.getColumnCount() - 1).setCellRenderer(rightRenderer);

	    txtTotalSettleAmt.setText(gDecimalFormat.format(dblTotalSettleAmt));
	    this.revalidate();
	    this.repaint();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funTableRowClicked()
    {
	try
	{
	    if (tblBills.getRowCount() > 0)
	    {
		if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
		{
		    int selectedRow = tblBills.getSelectedRow();
		    String billNo = tblBills.getValueAt(selectedRow, 0).toString();
		    String tableNo = tblBills.getValueAt(selectedRow, 2).toString();
		    String billType = "Direct Biller";
		    clsGlobalVarClass.funCheckHomeDelivery(billNo);
		    int row = tblBills.getSelectedRow();
		    if (tableNo.trim().length() == 0)
		    {
			billType = "Direct Biller";
			areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    }
		    else
		    {
			billType = "DineIn";
			String sql = "select strAreaCode from tbltablemaster where strTableNo='" + vTableNo.elementAt(row).toString() + "'";
			ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsArea.next())
			{
			    areaCode = rsArea.getString(1);
			}
			rsArea.close();
		    }
		    obj.setBillData(billNo, billType, areaCode);

		    funLoadOldDiscount(billNo);
		    setVisible(false);
		    obj.showPanel();
		}
		else//Delivery Detail Wise
		{
		    int selectedRow = tblBills.getSelectedRow();
		    String billNo = tblBills.getValueAt(selectedRow, 0).toString();
		    String tableNo = tblBills.getValueAt(selectedRow, 2).toString();
		    String billType = "";
		    clsGlobalVarClass.funCheckHomeDelivery(billNo);
		    int row = tblBills.getSelectedRow();
		    if (tableNo.trim().length() == 0)
		    {
			billType = "Direct Biller";
			areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    }
		    else
		    {
			billType = "DineIn";
			String sql = "select strAreaCode from tbltablemaster where strTableNo='" + vTableNo.elementAt(row).toString() + "'";
			ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsArea.next())
			{
			    areaCode = rsArea.getString(1);
			}
			rsArea.close();
		    }
		    obj.setBillData(billNo, billType, areaCode);

		    funLoadOldDiscount(billNo);
		    setVisible(false);
		    obj.showPanel();
		}

		funFillTableCombo();
		cmbBilledTables.setSelectedItem("All");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funLoadOldDiscount(String billNo)
    {
	try
	{
	    if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill"))
	    {
		String sqlDisc = "select CONCAT(a.strDiscOnType,'!',a.strDiscOnValue) as mapKey,a.strDiscRemarks"
			+ " ,a.strDiscReasonCode,a.dblDiscPer,a.dblDiscAmt,a.dblDiscOnAmt "
			+ " from tblbilldiscdtl a "
			+ " where a.strBillNo='" + billNo + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
		ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlDisc);
		while (resultSet.next())
		{
		    clsBillDiscountDtl billDiscountDtl = new clsBillDiscountDtl(resultSet.getString("strDiscRemarks"), resultSet.getString("strDiscReasonCode"), resultSet.getDouble("dblDiscPer"), resultSet.getDouble("dblDiscAmt"), resultSet.getDouble("dblDiscOnAmt"));
		    obj.mapBillDiscDtl.put(resultSet.getString("mapKey"), billDiscountDtl);
		}
		resultSet.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funDownArrowPressed()
    {

	int rowcount = tblBills.getRowCount();
	if (currentRow < rowcount)
	{
	    tblBills.changeSelection(currentRow++, 0, false, false);
	}
	else if (currentRow == rowcount)
	{
	    currentRow = 0;
	    tblBills.changeSelection(currentRow, 0, false, false);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblBills = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        lblTable = new javax.swing.JLabel();
        cmbBilledTables = new javax.swing.JComboBox();
        txtTotalSettleAmt = new javax.swing.JTextField();
        btnLast3UPIs = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMinimumSize(new java.awt.Dimension(490, 605));
        setPreferredSize(new java.awt.Dimension(490, 605));

        tblBills.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblBills.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        )
    );
    tblBills.setFillsViewportHeight(true);
    tblBills.setRowHeight(25);
    tblBills.getTableHeader().setReorderingAllowed(false);
    tblBills.addMouseListener(new java.awt.event.MouseAdapter()
    {
        public void mouseClicked(java.awt.event.MouseEvent evt)
        {
            tblBillsMouseClicked(evt);
        }
    });
    tblBills.addKeyListener(new java.awt.event.KeyAdapter()
    {
        public void keyPressed(java.awt.event.KeyEvent evt)
        {
            tblBillsKeyPressed(evt);
        }
    });
    jScrollPane1.setViewportView(tblBills);

    btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
    btnClose.setForeground(new java.awt.Color(255, 255, 255));
    btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
    btnClose.setText("BACK");
    btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
    btnClose.addMouseListener(new java.awt.event.MouseAdapter()
    {
        public void mouseClicked(java.awt.event.MouseEvent evt)
        {
            btnCloseMouseClicked(evt);
        }
    });

    lblTable.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
    lblTable.setText("Table");

    cmbBilledTables.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N

    txtTotalSettleAmt.setEditable(false);
    txtTotalSettleAmt.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
    txtTotalSettleAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    txtTotalSettleAmt.setText("0.00");
    txtTotalSettleAmt.setOpaque(false);

    btnLast3UPIs.setFont(new java.awt.Font("Trebuchet MS", 1, 13)); // NOI18N
    btnLast3UPIs.setForeground(new java.awt.Color(255, 255, 255));
    btnLast3UPIs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
    btnLast3UPIs.setText("LAST 15 UPI's");
    btnLast3UPIs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnLast3UPIs.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
    btnLast3UPIs.addMouseListener(new java.awt.event.MouseAdapter()
    {
        public void mouseClicked(java.awt.event.MouseEvent evt)
        {
            btnLast3UPIsMouseClicked(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(lblTable, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cmbBilledTables, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLast3UPIs, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(txtTotalSettleAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(cmbBilledTables)
                .addComponent(lblTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 0, 0)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalSettleAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast3UPIs, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, 0))
    );
    }// </editor-fold>//GEN-END:initComponents

    private void tblBillsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillsMouseClicked
	funTableRowClicked();
    }//GEN-LAST:event_tblBillsMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	// TODO add your handling code here:
	obj.funCloseForm();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void tblBillsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblBillsKeyPressed

//        if (evt.getKeyCode() == 10) // Check for Enter Key Press
//        {
//            funTableRowClicked();
//        }
//
//        if (evt.getKeyCode() == 40)
//        {
//            funDownArrowPressed();
//        }
//        if (evt.getKeyCode() == 38)
//        {
//            funUpArrowPressed();
//        }
    }//GEN-LAST:event_tblBillsKeyPressed

    private void btnLast3UPIsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnLast3UPIsMouseClicked
    {//GEN-HEADEREND:event_btnLast3UPIsMouseClicked
	funShowLast3TransactionButtonClicked();
    }//GEN-LAST:event_btnLast3UPIsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnLast3UPIs;
    private javax.swing.JComboBox cmbBilledTables;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTable;
    private javax.swing.JTable tblBills;
    private javax.swing.JTextField txtTotalSettleAmt;
    // End of variables declaration//GEN-END:variables

    private void funUpArrowPressed()
    {
	int rowcount = tblBills.getRowCount();
	if (currentRow >= 0)
	{
	    tblBills.changeSelection(currentRow--, 0, false, false);
	}
	else if (currentRow == rowcount)
	{
	    currentRow = 0;
	    tblBills.changeSelection(currentRow, 0, false, false);
	}
    }

    public void funFillUnsettledBills(String nextBillNo)
    {
	try
	{

	    double dblTotalSettleAmt = 0.00;
	    String tableName = cmbBilledTables.getSelectedItem().toString();

	    vTableNo = new Vector();
	    DefaultTableModel dmBills = (DefaultTableModel) tblBills.getModel();
	    dmBills.setRowCount(0);
	    dmBills.setColumnCount(0);
	    tblBills.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

	    if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
	    {
		dmBills.addColumn("Bill No");
		dmBills.addColumn("Time");
		dmBills.addColumn("Table");
		dmBills.addColumn("Waiter");
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    dmBills.addColumn("Member");
		}
		else
		{
		    dmBills.addColumn("Customer");
		}
		dmBills.addColumn("Amount");
	    }
	    else//Delivery Detail Wise
	    {
		dmBills.addColumn("Bill No");
		dmBills.addColumn("Time");
		dmBills.addColumn("Table");
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    dmBills.addColumn("Member");
		}
		else
		{
		    dmBills.addColumn("Customer");
		}
		dmBills.addColumn("Area");
		dmBills.addColumn("Del Boy");
		dmBills.addColumn("Amount");
	    }
	    String sql = "";
	    if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
	    {
		sql = "select a.strBillNo,ifnull(b.strTableNo,''),ifnull(b.strTableName,''),ifnull(c.strWaiterNo,'')\n"
			+ " ,ifnull(c.strWShortName,''),ifnull(d.strCustomerCode,''),ifnull(d.strCustomerName,''),a.dblGrandTotal"
			+ " ,DATE_FORMAT(a.dteBillDate,\"%h:%i\")  \n"
			+ " from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo\n"
			+ " left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo\n"
			+ " left outer join tblcustomermaster d on a.strCustomerCode=d.strCustomerCode\n"
			+ " where  a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			+ " and a.strSettelmentMode='' ";
		if (!tableName.equalsIgnoreCase("All"))
		{
		    String tableCode = mapTableNameWithCode.get(tableName);
		    sql = sql + " and a.strTableNo='" + tableCode + "' ";
		}
		sql = sql + " order by a.dteBillDate desc ";
	    }
	    else//Delivery Detail Wise
	    {
		sql = "SELECT a.strBillNo,IFNULL(d.strCustomerName,''),ifnull(e.strBuildingName,''),ifnull(f.strDPName,'')"
			+ " ,a.dblGrandTotal,ifnull(g.strTableNo,''),ifnull(g.strTableName,''),DATE_FORMAT(a.dteBillDate,\"%h:%i\") "
			+ " FROM tblbillhd a "
			+ " left outer join tblhomedeldtl b on a.strBillNo=b.strBillNo "
			+ " LEFT OUTER JOIN tblcustomermaster d ON a.strCustomerCode=d.strCustomerCode "
			+ " left outer join tblbuildingmaster e on d.strBuldingCode=e.strBuildingCode "
			+ " left outer join tbldeliverypersonmaster  f on  f.strDPCode=b.strDPCode "
			+ " left outer join tbltablemaster g on a.strTableNo=g.strTableNo "
			+ " where  a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			+ " and a.strSettelmentMode='' ";
		if (!tableName.equalsIgnoreCase("All"))
		{
		    String tableCode = mapTableNameWithCode.get(tableName);
		    sql = sql + " and a.strTableNo='" + tableCode + "' ";
		}
		sql = sql + " group by a.strBillNo "
			+ " order by a.dteBillDate desc ";
	    }
	    ResultSet rsPendingBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsPendingBills.next())
	    {
		if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
		{
		    Object[] ob =
		    {
			rsPendingBills.getString(1), rsPendingBills.getString(9), rsPendingBills.getString(3), rsPendingBills.getString(5), rsPendingBills.getString(7), gDecimalFormat.format(rsPendingBills.getDouble(8))
		    };
		    dmBills.addRow(ob);
		    vTableNo.add(rsPendingBills.getString(2));

		    dblTotalSettleAmt += rsPendingBills.getDouble(8);//settleAmt
		}
		else//Delivery Detail Wise
		{
		    Object[] ob =
		    {
			rsPendingBills.getString(1), rsPendingBills.getString(8), rsPendingBills.getString(7), rsPendingBills.getString(2), rsPendingBills.getString(3), rsPendingBills.getString(4), gDecimalFormat.format(rsPendingBills.getDouble(5))
		    };
		    dmBills.addRow(ob);
		    vTableNo.add(rsPendingBills.getString(6));

		    dblTotalSettleAmt += rsPendingBills.getDouble(5);//settleAmt
		}
	    }
	    rsPendingBills.close();

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblBills.getColumnModel().getColumn(tblBills.getColumnCount() - 1).setCellRenderer(rightRenderer);

//            if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))// 6 columns
//            {
//                tblBills.getColumnModel().getColumn(0).setPreferredWidth(100);
//                tblBills.getColumnModel().getColumn(1).setPreferredWidth(50);
//                tblBills.getColumnModel().getColumn(2).setPreferredWidth(100);
//                tblBills.getColumnModel().getColumn(3).setPreferredWidth(120);
//                tblBills.getColumnModel().getColumn(4).setPreferredWidth(180);
//                tblBills.getColumnModel().getColumn(5).setPreferredWidth(100);
//                
//            }
//            else//home delivery wise 7 columns
//            {
//                tblBills.getColumnModel().getColumn(0).setPreferredWidth(100);
//                tblBills.getColumnModel().getColumn(1).setPreferredWidth(50);
//                tblBills.getColumnModel().getColumn(2).setPreferredWidth(120);
//                tblBills.getColumnModel().getColumn(3).setPreferredWidth(150);
//                tblBills.getColumnModel().getColumn(4).setPreferredWidth(130);
//                tblBills.getColumnModel().getColumn(5).setPreferredWidth(130);
//                tblBills.getColumnModel().getColumn(6).setPreferredWidth(100);
//            }
	    txtTotalSettleAmt.setText(gDecimalFormat.format(dblTotalSettleAmt));
	    this.revalidate();
	    this.repaint();

	    //auto row select for this bill no for bill series enable
	    funTableRowClicked(nextBillNo);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funTableRowClicked(String nextBillNo)
    {
	try
	{
	    int selectedRow = -1;
	    if (tblBills.getRowCount() > 0)
	    {
		if (clsGlobalVarClass.gShowBillsType.equalsIgnoreCase("Table Detail Wise"))
		{

		    for (int row = 0; row < tblBills.getRowCount(); row++)
		    {
			if (nextBillNo.equalsIgnoreCase(tblBills.getValueAt(row, 0).toString()))
			{
			    selectedRow = row;
			    break;
			}
		    }

		    String billNo = tblBills.getValueAt(selectedRow, 0).toString();
		    String tableNo = tblBills.getValueAt(selectedRow, 2).toString();
		    String billType = "Direct Biller";
		    clsGlobalVarClass.funCheckHomeDelivery(billNo);

		    if (tableNo.trim().length() == 0)
		    {
			billType = "Direct Biller";
			areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    }
		    else
		    {
			billType = "DineIn";
			String sql = "select strAreaCode from tbltablemaster where strTableNo='" + vTableNo.elementAt(selectedRow).toString() + "'";
			ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsArea.next())
			{
			    areaCode = rsArea.getString(1);
			}
			rsArea.close();
		    }
		    obj.setBillData(billNo, billType, areaCode);

		    funLoadOldDiscount(billNo);
		    setVisible(false);
		    obj.showPanel();
		}
		else//Delivery Detail Wise
		{

		    for (int row = 0; row < tblBills.getRowCount(); row++)
		    {
			if (nextBillNo.equalsIgnoreCase(tblBills.getValueAt(row, 0).toString()))
			{
			    selectedRow = row;
			    break;
			}
		    }
		    String billNo = tblBills.getValueAt(selectedRow, 0).toString();
		    String tableNo = tblBills.getValueAt(selectedRow, 2).toString();
		    String billType = "";
		    clsGlobalVarClass.funCheckHomeDelivery(billNo);

		    if (tableNo.trim().length() == 0)
		    {
			billType = "Direct Biller";
			areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    }
		    else
		    {
			billType = "DineIn";
			String sql = "select strAreaCode from tbltablemaster where strTableNo='" + vTableNo.elementAt(selectedRow).toString() + "'";
			ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsArea.next())
			{
			    areaCode = rsArea.getString(1);
			}
			rsArea.close();
		    }
		    obj.setBillData(billNo, billType, areaCode);

		    funLoadOldDiscount(billNo);
		    setVisible(false);
		    obj.showPanel();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funFillTableCombo()
    {
	try
	{
	    cmbBilledTables.removeAllItems();
	    mapTableNameWithCode.clear();

	    String sql = "select a.strTableNo ,b.strTableName "
		    + "from tblbillhd a,tbltablemaster b "
		    + "where a.strTableNo=b.strTableNo "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		    + "and strSettelmentMode=''  "
		    + "group by b.strTableNo "
		    + "order by b.strTableNo ";
	    ResultSet rsBilledTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    cmbBilledTables.addItem("All");
	    mapTableNameWithCode.put("All", "All");
	    while (rsBilledTables.next())
	    {
		cmbBilledTables.addItem(rsBilledTables.getString(2));
		mapTableNameWithCode.put(rsBilledTables.getString(2), rsBilledTables.getString(1));
	    }
	    rsBilledTables.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funShowLast3TransactionButtonClicked()
    {
	try
	{
	    clsBenowIntegration objBenowIntegration = new clsBenowIntegration();
	    List<clsUPIBean> listOfLastUPITransactions = objBenowIntegration.funGetLastUPITransactionLIst();

	    if (listOfLastUPITransactions.size() > 0)
	    {
		frmShowUPITransactions objShowUPITransactions = new frmShowUPITransactions(this,listOfLastUPITransactions);
		objShowUPITransactions.setVisible(true);
		objShowUPITransactions.setLocationRelativeTo(this);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    class ItemChangeListener implements ItemListener
    {

	@Override
	public void itemStateChanged(ItemEvent event)
	{
	    if (event.getStateChange() == ItemEvent.SELECTED)
	    {
		funFillUnsettledBills();
	    }
	}
    }        

}
