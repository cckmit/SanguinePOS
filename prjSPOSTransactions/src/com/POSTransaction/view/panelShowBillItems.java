package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSTransaction.controller.clsCalculateBillDiscount;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class panelShowBillItems extends javax.swing.JPanel
{

    frmBillSettlement obj;
    DefaultTableModel dm;
    StringBuilder sb = new StringBuilder();
    private Map<String, clsBillItemDtl> hmSelectedItems;
    DefaultTableModel dmBillItems;
    clsUtility objUtility;
    List<clsBillItemDtl> listBillItems;
    private clsCalculateBillDiscount objBillSettlementUtility;

    public panelShowBillItems(Object ob, List<clsBillItemDtl> listBillItemsTemp)
    {

	initComponents();
	try
	{
	    obj = (frmBillSettlement) ob;
	    objBillSettlementUtility = new clsCalculateBillDiscount(obj);

	    objUtility = new clsUtility();
	    listBillItems = listBillItemsTemp;
	    funFillGridWithItems(listBillItems);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funFillGridWithItems(List<clsBillItemDtl> listBillItems) throws Exception
    {
	hmSelectedItems = new HashMap<String, clsBillItemDtl>();
	dmBillItems = (DefaultTableModel) tblBillItems.getModel();
	dmBillItems.setRowCount(0);
	String html = "<html><font color=black>";

	for (clsBillItemDtl objBillItemDtl : listBillItems)
	{
	    String itemName = html + objBillItemDtl.getItemName() + "</font></html>";
	    String itemCode = html + objBillItemDtl.getItemCode() + "</font></html>";
	    String qty = html + objBillItemDtl.getQuantity() + "</font></html>";
	    String amt = html + objBillItemDtl.getAmount() + "</font></html>";
	    Object[] ob =
	    {
		itemName, qty, amt, itemCode
	    };
	    dmBillItems.addRow(ob);
	}
	lblKOTs.setText("");

	tblBillItems.getColumnModel().getColumn(0).setPreferredWidth(200);
	tblBillItems.getColumnModel().getColumn(1).setPreferredWidth(100);
	tblBillItems.getColumnModel().getColumn(2).setPreferredWidth(100);
	tblBillItems.getColumnModel().getColumn(3).setPreferredWidth(75);

	this.revalidate();
	this.repaint();
    }

    private void funTableRowClicked()
    {
	StringBuilder sbItems = new StringBuilder();
	try
	{
	    if (tblBillItems.getRowCount() > 0)
	    {
		int row = tblBillItems.getSelectedRow();

		String itemName = funRemoveHTMLTags(tblBillItems.getValueAt(row, 0).toString());
		String itemQty = funRemoveHTMLTags(tblBillItems.getValueAt(row, 1).toString());
		String itemAmt = funRemoveHTMLTags(tblBillItems.getValueAt(row, 2).toString());
		String itemCode = funRemoveHTMLTags(tblBillItems.getValueAt(row, 3).toString());

		if (hmSelectedItems.containsKey(itemCode))
		{
		    hmSelectedItems.remove(itemCode);
		    String html = "<html><font color=black>";
		    itemName = html + itemName + "</font></html>";
		    itemQty = html + itemQty + "</font></html>";
		    itemAmt = html + itemAmt + "</font></html>";
		    itemCode = html + itemCode + "</font></html>";
		    tblBillItems.setValueAt(itemName, row, 0);
		    tblBillItems.setValueAt(itemQty, row, 1);
		    tblBillItems.setValueAt(itemAmt, row, 2);
		    tblBillItems.setValueAt(itemCode, row, 3);
		}
		else
		{
		    clsBillItemDtl objBillItemDtl = new clsBillItemDtl();
		    objBillItemDtl.setItemName(itemName);
		    objBillItemDtl.setItemCode(itemCode);
		    objBillItemDtl.setQuantity(Double.parseDouble(itemQty));
		    objBillItemDtl.setAmount(Double.parseDouble(itemAmt));
		    hmSelectedItems.put(itemCode, objBillItemDtl);
		    String html = "<html><font color=red  >";// Added two spaces after red to match length of black
		    itemName = html + itemName + "</font></html>";
		    itemQty = html + itemQty + "</font></html>";
		    itemAmt = html + itemAmt + "</font></html>";
		    itemCode = html + itemCode + "</font></html>";
		    tblBillItems.setValueAt(itemName, row, 0);
		    tblBillItems.setValueAt(itemQty, row, 1);
		    tblBillItems.setValueAt(itemAmt, row, 2);
		    tblBillItems.setValueAt(itemCode, row, 3);
		}
	    }
	    sbItems.setLength(0);

	    int cnt = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : hmSelectedItems.entrySet())
	    {
		if (cnt == 0)
		{
		    sbItems.append(entry.getValue().getItemName());
		}
		else
		{
		    sbItems.append(", ");
		    sbItems.append(entry.getValue().getItemName());
		}
		cnt++;
	    }
	    lblKOTs.setText(sbItems.toString());

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	repaint();
	revalidate();
    }

    private String funRemoveHTMLTags(String text)
    {
	//<html><font color=red  >KT0033750</font></html>
	StringBuilder sb = new StringBuilder(text);
	return sb.substring(24, 33).toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillItems = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnMakeBill = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lblKOTs = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(490, 603));
        setLayout(null);

        jScrollPane1.setAutoscrolls(true);

        tblBillItems.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        tblBillItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Qty", "Amount", "Item Code"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBillItems.setRowHeight(25);
        tblBillItems.getTableHeader().setReorderingAllowed(false);
        tblBillItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillItemsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBillItems);
        if (tblBillItems.getColumnModel().getColumnCount() > 0) {
            tblBillItems.getColumnModel().getColumn(0).setResizable(false);
            tblBillItems.getColumnModel().getColumn(1).setResizable(false);
            tblBillItems.getColumnModel().getColumn(2).setResizable(false);
            tblBillItems.getColumnModel().getColumn(3).setResizable(false);
        }

        add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 490, 430);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("BACK");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        add(btnClose);
        btnClose.setBounds(308, 503, 111, 41);

        btnRefresh.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.setToolTipText("Refresh Form");
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        add(btnRefresh);
        btnRefresh.setBounds(159, 503, 111, 41);

        btnMakeBill.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnMakeBill.setForeground(new java.awt.Color(255, 255, 255));
        btnMakeBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMakeBill.setText("Make Bill");
        btnMakeBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMakeBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMakeBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMakeBillMouseClicked(evt);
            }
        });
        add(btnMakeBill);
        btnMakeBill.setBounds(10, 503, 111, 41);

        jScrollPane2.setViewportView(lblKOTs);

        add(jScrollPane2);
        jScrollPane2.setBounds(0, 433, 490, 64);
    }// </editor-fold>//GEN-END:initComponents

    private void tblBillItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillItemsMouseClicked
	funTableRowClicked();
    }//GEN-LAST:event_tblBillItemsMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	// TODO add your handling code here:
	obj.funCloseForm();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
	// TODO add your handling code here:
	try
	{
	    funFillGridWithItems(listBillItems);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnMakeBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMakeBillMouseClicked
	// TODO add your handling code here:
	if (hmSelectedItems.size() > 0)
	{
	    this.setVisible(false);
	    objBillSettlementUtility.funSetComplimentaryItems(hmSelectedItems);
	    obj.showPanel();
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Please Select KOT");
	}
    }//GEN-LAST:event_btnMakeBillMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnMakeBill;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblKOTs;
    private javax.swing.JTable tblBillItems;
    // End of variables declaration//GEN-END:variables
}
