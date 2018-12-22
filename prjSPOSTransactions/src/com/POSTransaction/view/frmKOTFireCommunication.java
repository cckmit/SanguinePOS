package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class frmKOTFireCommunication extends javax.swing.JDialog
{

    private String tableNo;
    private ArrayList<String> listKOTOnTable;
    private ArrayList<clsMakeKotItemDtl> listKOTItemsOnTable;
    private final DecimalFormat gDecimalFormatForQty = new DecimalFormat("0.0");

    public frmKOTFireCommunication(java.awt.Frame parent, boolean modal, String tableNo)
    {

	super(parent, modal);
	initComponents();

	this.setLocationRelativeTo(null);
	this.tableNo = tableNo;

	funCustomiseBillSeriesTableColumnHeader();

	funFillOldKOTItems(tableNo);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblPosName = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        brnHomeAddress = new javax.swing.JButton();
        btnHomeAddress = new javax.swing.JButton();
        scrollBussyTableItems = new javax.swing.JScrollPane();
        tblKOTFireItems = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 600));
        setMinimumSize(new java.awt.Dimension(700, 600));
        setResizable(false);
        getContentPane().setLayout(null);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setPreferredSize(new java.awt.Dimension(800, 30));

        lblModuleName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("SPOS - KOT FIRE COMMUNICATION");

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(lblUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(311, 311, 311)
                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblModuleName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblModuleName, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panelHeader);
        panelHeader.setBounds(0, 0, 820, 30);

        brnHomeAddress.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        brnHomeAddress.setForeground(new java.awt.Color(255, 255, 255));
        brnHomeAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        brnHomeAddress.setMnemonic('k');
        brnHomeAddress.setText("FIRE");
        brnHomeAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        brnHomeAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        brnHomeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                brnHomeAddressMouseClicked(evt);
            }
        });
        getContentPane().add(brnHomeAddress);
        brnHomeAddress.setBounds(480, 530, 100, 40);

        btnHomeAddress.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnHomeAddress.setForeground(new java.awt.Color(255, 255, 255));
        btnHomeAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnHomeAddress.setMnemonic('h');
        btnHomeAddress.setText("CANCEL");
        btnHomeAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHomeAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnHomeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeAddressMouseClicked(evt);
            }
        });
        btnHomeAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeAddressActionPerformed(evt);
            }
        });
        btnHomeAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHomeAddressKeyPressed(evt);
            }
        });
        getContentPane().add(btnHomeAddress);
        btnHomeAddress.setBounds(590, 530, 100, 40);

        tblKOTFireItems.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblKOTFireItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "<html>Printed<br>Qty</html>", "<html>Pending<br>Qty</html>", "<html>Fire<br>Qty</html>", "Select", ""
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false
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
        tblKOTFireItems.setFillsViewportHeight(true);
        tblKOTFireItems.setRowHeight(35);
        tblKOTFireItems.getTableHeader().setReorderingAllowed(false);
        tblKOTFireItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblKOTFireItemsMouseClicked(evt);
            }
        });
        scrollBussyTableItems.setViewportView(tblKOTFireItems);
        if (tblKOTFireItems.getColumnModel().getColumnCount() > 0)
        {
            tblKOTFireItems.getColumnModel().getColumn(0).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(0).setPreferredWidth(300);
            tblKOTFireItems.getColumnModel().getColumn(1).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(1).setPreferredWidth(5);
            tblKOTFireItems.getColumnModel().getColumn(2).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(2).setPreferredWidth(15);
            tblKOTFireItems.getColumnModel().getColumn(3).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(3).setPreferredWidth(25);
            tblKOTFireItems.getColumnModel().getColumn(4).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(4).setPreferredWidth(5);
            tblKOTFireItems.getColumnModel().getColumn(5).setResizable(false);
            tblKOTFireItems.getColumnModel().getColumn(5).setPreferredWidth(5);
            tblKOTFireItems.getColumnModel().getColumn(6).setMinWidth(0);
            tblKOTFireItems.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblKOTFireItems.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        getContentPane().add(scrollBussyTableItems);
        scrollBussyTableItems.setBounds(0, 32, 700, 490);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHomeAddressActionPerformed
    {//GEN-HEADEREND:event_btnHomeAddressActionPerformed
	dispose();
    }//GEN-LAST:event_btnHomeAddressActionPerformed

    private void btnHomeAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHomeAddressKeyPressed
    {//GEN-HEADEREND:event_btnHomeAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnHomeAddressKeyPressed

    private void brnHomeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_brnHomeAddressMouseClicked
    {//GEN-HEADEREND:event_brnHomeAddressMouseClicked
	funFireButtonClicked();

    }//GEN-LAST:event_brnHomeAddressMouseClicked

    private void btnHomeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnHomeAddressMouseClicked
    {//GEN-HEADEREND:event_btnHomeAddressMouseClicked

    }//GEN-LAST:event_btnHomeAddressMouseClicked

    private void tblKOTFireItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblKOTFireItemsMouseClicked
    {//GEN-HEADEREND:event_tblKOTFireItemsMouseClicked
	funTableMouseClicked(evt);
    }//GEN-LAST:event_tblKOTFireItemsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnHomeAddress;
    private javax.swing.JButton btnHomeAddress;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JScrollPane scrollBussyTableItems;
    private javax.swing.JTable tblKOTFireItems;
    // End of variables declaration//GEN-END:variables

    private void funTableMouseClicked(MouseEvent evt)
    {
	try
	{
	    int columnNo = tblKOTFireItems.getSelectedColumn();
	    int rowNo = tblKOTFireItems.getSelectedRow();

	    if (columnNo == 4 && tblKOTFireItems.getValueAt(rowNo, 6) != null && tblKOTFireItems.getValueAt(rowNo, 6).toString().trim().length() > 0)//fire qty && itemCode
	    {
		double originalQty = Double.parseDouble(tblKOTFireItems.getValueAt(rowNo, 1).toString());
		double firedQty = Double.parseDouble(tblKOTFireItems.getValueAt(rowNo, 2).toString());
		double pendingQty = Double.parseDouble(tblKOTFireItems.getValueAt(rowNo, 3).toString());
		double fireQty = Double.parseDouble(tblKOTFireItems.getValueAt(rowNo, 4).toString());
		String itemCode = tblKOTFireItems.getValueAt(rowNo, 6).toString();

		if (pendingQty > 0)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(null, true, "Fire Qty");
		    num.setVisible(true);
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			{
			    double enterQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);

			    if (enterQty > 0)
			    {
				if (enterQty > pendingQty)
				{
				    JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
				    return;
				}
				else
				{
				    tblKOTFireItems.setValueAt(gDecimalFormatForQty.format(enterQty), rowNo, 4);
				    tblKOTFireItems.setValueAt(true, rowNo, 5);
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

    private void funCustomiseBillSeriesTableColumnHeader()
    {
	JTableHeader header = tblKOTFireItems.getTableHeader();
	header.setPreferredSize(new Dimension(header.getWidth(), 40));

	DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tblKOTFireItems.getTableHeader().getDefaultRenderer();
	renderer.setHorizontalAlignment(JLabel.LEFT);
	renderer.setVerticalTextPosition(JLabel.TOP);
	renderer.setVerticalAlignment(JLabel.TOP);
    }

    private void funFillOldKOTItems(String tableNo)
    {
	try
	{
	    boolean flagIsKOTPresent = false;
	    listKOTOnTable = new ArrayList<>();
	    listKOTItemsOnTable = new ArrayList<>();

	    String sqlKOTDtl = "select strKOTNo "
		    + " from tblitemrtemp "
		    + " where (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All') "
		    + " and strTableNo='" + tableNo + "' "
		    + " and strPrintYN='Y' "
		    + " and strNCKotYN='N' "
		    //+ " and (dblItemQuantity-dblFiredQty)>0 "
		    + " group by strKOTNo  "
		    + " order by strKOTNo DESC";

	    ResultSet rsKOTDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlKOTDtl);
	    while (rsKOTDtl.next())
	    {
		listKOTOnTable.add(rsKOTDtl.getString(1));
		flagIsKOTPresent = true;
	    }
	    rsKOTDtl.close();
	    if (flagIsKOTPresent)
	    {
		String sqlTableItemDtl = "select strKOTNo,strTableNo,strWaiterNo"
			+ " ,strItemName,strItemCode,dblItemQuantity,dblAmount"
			+ " ,intPaxNo,strPrintYN,tdhComboItemYN,strSerialNo,strNcKotYN,dblRate"
			+ " ,dblFiredQty,dblPrintQty "
			+ " from tblitemrtemp where strTableNo='" + tableNo + "' "
			+ " and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All') "
			+ " and strNcKotYN='N' "
			//+ " and (dblItemQuantity-dblFiredQty)>0 "
			+ " order by strKOTNo desc ,strSerialNo";
		ResultSet rsTableItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableItemDtl);
		while (rsTableItemDtl.next())
		{
		    clsMakeKotItemDtl obKOTItemDtl = new clsMakeKotItemDtl(rsTableItemDtl.getString(11), rsTableItemDtl.getString(1), rsTableItemDtl.getString(2), rsTableItemDtl.getString(3), rsTableItemDtl.getString(4), rsTableItemDtl.getString(5), rsTableItemDtl.getDouble(6), rsTableItemDtl.getDouble(7), rsTableItemDtl.getInt(8), rsTableItemDtl.getString(9), rsTableItemDtl.getString(10), false, "", "", "", "N", rsTableItemDtl.getDouble(13));
		    obKOTItemDtl.setDblFireQty(rsTableItemDtl.getDouble(14));
		    obKOTItemDtl.setDblPrintQty(rsTableItemDtl.getDouble(15));

		    if (rsTableItemDtl.getDouble(7) >= 0)
		    {
			listKOTItemsOnTable.add(obKOTItemDtl);
		    }
		}
		rsTableItemDtl.close();

		funFillKOTWiseTable();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillKOTWiseTable()
    {
	try
	{
	    DefaultTableModel dm = (DefaultTableModel) tblKOTFireItems.getModel();
	    dm.setRowCount(0);
	    tblKOTFireItems.getColumnModel().getColumn(6).setPreferredWidth(0);

	    //==Old KOT Items===//
	    if (null != listKOTOnTable && listKOTOnTable.size() > 0)
	    {

		//Collections.sort(list_KOT_On_Table);
		for (String oldKOTNos : listKOTOnTable)
		{
		    String kotTime = funGetKOTTimeForOldKOT(oldKOTNos);
		    String tempOldKOT = "<html><font color=black><b>" + oldKOTNos + "</font></html>";
		    Object obKotTitle[] =
		    {
			tempOldKOT, "", "", "", "", false, ""
		    };
		    dm.addRow(obKotTitle);
		    for (clsMakeKotItemDtl obList : listKOTItemsOnTable)
		    {

			String temp_kot = obList.getKOTNo();
			if (oldKOTNos.equalsIgnoreCase(temp_kot))
			{
			    String itemName = obList.getItemName();
			    double qty = obList.getQty();
			    double printedQty = obList.getDblFireQty();
			    double pendingQty = (obList.getQty() - obList.getDblFireQty());
			    double fireQty = 0;
			    String itemCode = obList.getItemCode();

			    Object ob_OldKOT_Item[] =
			    {
				itemName, qty, printedQty, pendingQty, fireQty, false, itemCode
			    };
			    dm.addRow(ob_OldKOT_Item);
			}
		    }
		}

		tblKOTFireItems.setModel(dm);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblKOTFireItems.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblKOTFireItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblKOTFireItems.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblKOTFireItems.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGetKOTTimeForOldKOT(String kotNo)
    {
	String kotTime = "";
	try
	{
	    String sqlKot = "select DATE_FORMAT(dteDateCreated,'%H:%i') from tblitemrtemp where strKOTNo='" + kotNo + "' limit 1";
	    ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
	    if (rsKOTTime.next())
	    {
		kotTime = rsKOTTime.getString(1);
	    }
	    rsKOTTime.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return kotTime;
	}
    }

    private void funFireButtonClicked()
    {
	try
	{

	    boolean isKOTSelected = false;
	    for (int row = 0; row < tblKOTFireItems.getRowCount(); row++)
	    {
		if (tblKOTFireItems.getValueAt(row, 0).toString().startsWith("<html><font color=black><b>KT"))
		{
		    isKOTSelected = Boolean.parseBoolean(tblKOTFireItems.getValueAt(row, 5).toString());
		    break;
		}
	    }

	    if (isKOTSelected)
	    {
		//code call for make bill and Make Kot form
		frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want fire KOT?");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{
		    funAddItemsToFire();
		}
	    }
	    else
	    {
		funAddItemsToFire();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAddItemsToFire()
    {
	try
	{

	    List<clsMakeKotItemDtl> listOfFireItems = new ArrayList<>();
	    String kotNo = "";
	    boolean isKOTSelected = false;
	    for (int row = 0; row < tblKOTFireItems.getRowCount(); row++)
	    {
		if (tblKOTFireItems.getValueAt(row, 0).toString().startsWith("<html><font color=black><b>KT"))
		{
		    kotNo = tblKOTFireItems.getValueAt(row, 0).toString().split("<b>")[1].split("</font>")[0];
		    isKOTSelected = Boolean.parseBoolean(tblKOTFireItems.getValueAt(row, 5).toString());

		    continue;
		}

		if (isKOTSelected && tblKOTFireItems.getValueAt(row, 3) != null && Double.parseDouble(tblKOTFireItems.getValueAt(row, 3).toString()) > 0)
		{
		    String itemCode = tblKOTFireItems.getValueAt(row, 6).toString();
		    String itemName = tblKOTFireItems.getValueAt(row, 0).toString();
		    double qty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 1).toString());
		    double firedQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 2).toString());
		    double pendingQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 3).toString());
		    double fireQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 4).toString());

		    clsMakeKotItemDtl objFireKOTItemDtl = new clsMakeKotItemDtl();
		    objFireKOTItemDtl.setKOTNo(kotNo);
		    objFireKOTItemDtl.setItemCode(itemCode);
		    objFireKOTItemDtl.setItemName(itemName);
		    objFireKOTItemDtl.setQty(qty);
		    objFireKOTItemDtl.setDblFiredQty(firedQty);
		    objFireKOTItemDtl.setDblPendingQty(pendingQty);
		    objFireKOTItemDtl.setDblFireQty(pendingQty);
		    objFireKOTItemDtl.setDblPrintQty(pendingQty);

		    listOfFireItems.add(objFireKOTItemDtl);
		}
		else
		{
		    if (Double.parseDouble(tblKOTFireItems.getValueAt(row, 4).toString()) > 0 && Boolean.parseBoolean(tblKOTFireItems.getValueAt(row, 5).toString()))
		    {
			String itemCode = tblKOTFireItems.getValueAt(row, 6).toString();
			String itemName = tblKOTFireItems.getValueAt(row, 0).toString();
			double qty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 1).toString());
			double firedQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 2).toString());
			double pendingQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 3).toString());
			double fireQty = Double.parseDouble(tblKOTFireItems.getValueAt(row, 4).toString());

			clsMakeKotItemDtl objFireKOTItemDtl = new clsMakeKotItemDtl();
			objFireKOTItemDtl.setKOTNo(kotNo);
			objFireKOTItemDtl.setItemCode(itemCode);
			objFireKOTItemDtl.setItemName(itemName);
			objFireKOTItemDtl.setQty(qty);
			objFireKOTItemDtl.setDblFiredQty(firedQty);
			objFireKOTItemDtl.setDblPendingQty(pendingQty);
			objFireKOTItemDtl.setDblFireQty(fireQty);
			objFireKOTItemDtl.setDblPrintQty(fireQty);

			listOfFireItems.add(objFireKOTItemDtl);

			int nextRow = row + 1;

			for (int m = nextRow; m < tblKOTFireItems.getRowCount(); m++)
			{
			    if (tblKOTFireItems.getValueAt(m, 0).toString().startsWith("-->"))
			    {
				tblKOTFireItems.setValueAt(true, m, 5);
				tblKOTFireItems.setValueAt(tblKOTFireItems.getValueAt(m, 3), m, 4);
			    }
			    else
			    {
				break;
			    }
			}		
		    }
		    else
		    {
			continue;
		    }
		}

	    }

	    if (listOfFireItems.size() > 0)
	    {
		dispose();
		
		int updatedPrintQty = clsGlobalVarClass.dbMysql.execute("update tblitemrtemp  "
			+ "set dblPrintQty=0 "
			+ "where strTableNo='" + tableNo + "' ");

		Set<String> setOfKOTs = new HashSet<String>();
		for (clsMakeKotItemDtl objFireKOTItemDtl : listOfFireItems)
		{
		    String sqlUpdate = "update tblitemrtemp  "
			    + "set dblFiredQty=dblFiredQty+" + objFireKOTItemDtl.getDblFireQty() + " "
			    + ",dblPrintQty='" + objFireKOTItemDtl.getDblFireQty() + "' "
			    + "where strTableNo='" + tableNo + "' "
			    + "and strKOTNo='" + objFireKOTItemDtl.getKOTNo() + "' "
			    + "and strItemCode='" + objFireKOTItemDtl.getItemCode() + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    setOfKOTs.add(objFireKOTItemDtl.getKOTNo());
		}

		Iterator<String> it = setOfKOTs.iterator();
		while (it.hasNext())
		{
		    String KOTNO = it.next();

		    if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
		    {
			clsKOTGeneration objGeneration = new clsKOTGeneration();
			objGeneration.funKOTGeneration(tableNo, KOTNO, "", "", "Dina", "Y");
		    }
		    else
		    {
			clsKOTGeneration objGeneration = new clsKOTGeneration();
			objGeneration.funKOTGeneration(tableNo, KOTNO, "", "", "Dina", "Y");
		    }
		}	
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Please enter the fire qty.");
		return;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
