/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsTDHOnItemDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSTransaction.controller.clsCalculateBillDiscount;
import static com.POSTransaction.view.frmDirectBiller.Itemcode;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmBillItems extends javax.swing.JFrame
{

    
    
    private frmBillSettlement obj;
    private Map<String, clsBillItemDtl> hmSelectedItems;
    private DefaultTableModel dmBillItems;
    private clsUtility objUtility;
    private List<clsBillItemDtl> listBillItems;
    private clsCalculateBillDiscount objBillSettlementUtility;

    public frmBillItems(Object ob, List<clsBillItemDtl> listBillItemsTemp)
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
            //String qty=html+objBillItemDtl.getQuantity()+"</font></html>";
            String qty = String.valueOf(objBillItemDtl.getQuantity());
            String amt = html + objBillItemDtl.getAmount() + "</font></html>";
            String rate = html + objBillItemDtl.getRate() + "</font></html>";
            Object[] ob =
            {
                itemName, qty, amt, itemCode, rate
            };
            dmBillItems.addRow(ob);
        }
        lblItems.setText("");
        
         DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
         rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
         
        tblBillItems.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblBillItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        tblBillItems.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblBillItems.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblBillItems.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblBillItems.getColumnModel().getColumn(3).setPreferredWidth(75);
        tblBillItems.getColumnModel().getColumn(4).setPreferredWidth(10);

        this.revalidate();
        this.repaint();
    }

    private void funTableRowClicked(String clickType)
    {
        StringBuilder sbItems = new StringBuilder();
        try
        {
            if (tblBillItems.getRowCount() > 0)
            {
                int row = tblBillItems.getSelectedRow();

                String itemName = funRemoveHTMLTags(tblBillItems.getValueAt(row, 0).toString());
                //String itemQty=funRemoveHTMLTags(tblBillItems.getValueAt(row,1).toString());
                String itemQty = tblBillItems.getValueAt(row, 1).toString();
                String itemAmt = funRemoveHTMLTags(tblBillItems.getValueAt(row, 2).toString());
                String itemCode = funRemoveHTMLTags(tblBillItems.getValueAt(row, 3).toString());
                String itemRate = funRemoveHTMLTags(tblBillItems.getValueAt(row, 4).toString());

                if (hmSelectedItems.containsKey(itemCode))
                {
                    hmSelectedItems.remove(itemCode);
                    String html = "<html><font color=black>";
                    itemName = html + itemName + "</font></html>";
                    //itemQty=html+itemQty+"</font></html>";
                    itemAmt = html + itemAmt + "</font></html>";
                    itemCode = html + itemCode + "</font></html>";
                    itemRate = html + itemRate + "</font></html>";

                    tblBillItems.setValueAt(itemName, row, 0);
                    tblBillItems.setValueAt(itemQty, row, 1);
                    tblBillItems.setValueAt(itemAmt, row, 2);
                    tblBillItems.setValueAt(itemCode, row, 3);
                    tblBillItems.setValueAt(itemRate, row, 4);
                }
                else
                {
                    clsBillItemDtl objBillItemDtl = new clsBillItemDtl();
                    objBillItemDtl.setItemName(itemName);
                    objBillItemDtl.setItemCode(itemCode);
                    objBillItemDtl.setQuantity(Double.parseDouble(itemQty));
                    objBillItemDtl.setAmount(Double.parseDouble(itemAmt));
                    objBillItemDtl.setRate(Double.parseDouble(itemRate));
                    hmSelectedItems.put(itemCode, objBillItemDtl);

                    String html = "<html><font color=red  >";// Added two spaces after red to match length of black
                    itemName = html + itemName + "</font></html>";
                    //itemQty=html+itemQty+"</font></html>";
                    itemAmt = html + itemAmt + "</font></html>";
                    itemCode = html + itemCode + "</font></html>";
                    itemRate = html + itemRate + "</font></html>";

                    tblBillItems.setValueAt(itemName, row, 0);
                    tblBillItems.setValueAt(itemQty, row, 1);
                    tblBillItems.setValueAt(itemAmt, row, 2);
                    tblBillItems.setValueAt(itemCode, row, 3);
                    tblBillItems.setValueAt(itemRate, row, 4);
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
            lblItems.setText(sbItems.toString());

            funItemTablePressed(clickType);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        repaint();
        revalidate();
    }

    private void funItemTablePressed(String clickType)
    {
        try
        {
            if (tblBillItems.getRowCount() > 0)
            {
                clsGlobalVarClass.gNumerickeyboardValue = null;

                int columnNo = tblBillItems.getSelectedColumn();
                int rowNo = tblBillItems.getSelectedRow();

                if (clickType.equals("Mouse"))
                {
                    if (columnNo == 1)
                    {
                        double dblQty = Double.parseDouble(tblBillItems.getValueAt(rowNo, 1).toString());
                        if (dblQty > 1)
                        {
                            frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
                            num.setVisible(true);
                            if (null != clsGlobalVarClass.gNumerickeyboardValue)
                            {
                                if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0 && Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue)<=dblQty)
                                {
                                    tblBillItems.setValueAt(Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue), rowNo, 1);
                                }
                            }
                        }
                    }
                }
                else
                {
                    double dblQty = Double.parseDouble(tblBillItems.getValueAt(rowNo, 1).toString());
                    if (dblQty > 1)
                    {
                        frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
                        num.setVisible(true);

                        if (null != clsGlobalVarClass.gNumerickeyboardValue)
                        {
                            if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0 && Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue)<=dblQty)
                            {
                                tblBillItems.setValueAt(Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue), rowNo, 1);
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
    }

    private String funRemoveHTMLTags(String text)
    {
        //<html><font color=red  >KT0033750</font></html>
        StringBuilder sb = new StringBuilder(text);
        String tmpStr = sb.substring(24, sb.length());
        sb = new StringBuilder(tmpStr);
        //System.out.println("SS=  "+sb.substring(0,tmpStr.indexOf("<")).toString());
        return sb.substring(0, tmpStr.indexOf("<")).toString().trim();
    }

    private void funOKButtonClicked()
    {
        for (int cnt = 0; cnt < tblBillItems.getRowCount(); cnt++)
        {
            String itemCode = funRemoveHTMLTags(tblBillItems.getValueAt(cnt, 3).toString());
            double amt = Double.parseDouble(funRemoveHTMLTags(tblBillItems.getValueAt(cnt, 2).toString()));
            double rate = Double.parseDouble(funRemoveHTMLTags(tblBillItems.getValueAt(cnt, 4).toString()));
            if (hmSelectedItems.containsKey(itemCode))
            {
                double qty = Double.parseDouble(tblBillItems.getValueAt(cnt, 1).toString());
                if((qty*rate)>amt)
                {
                    JOptionPane.showMessageDialog(null, "Please select valid quantity!!!");
                    return;
                }
                hmSelectedItems.get(itemCode).setQuantity(qty);
            }
        }

        dispose();
        objBillSettlementUtility.funSetComplimentaryItems(hmSelectedItems);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLayout = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        lblPosName = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        panelBody = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        btnClose = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillItems = new javax.swing.JTable();
        lblItems = new javax.swing.JLabel();

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(520, 470));

        javax.swing.GroupLayout panelLayoutLayout = new javax.swing.GroupLayout(panelLayout);
        panelLayout.setLayout(panelLayoutLayout);
        panelLayoutLayout.setHorizontalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayoutLayout.setVerticalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        panelHeader.setPreferredSize(new java.awt.Dimension(580, 30));
        panelHeader.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelHeaderMouseDragged(evt);
            }
        });

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("Make Items Complimentary");

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblPosNameMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addComponent(lblProductName)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblModuleName)
                    .addComponent(lblformName))
                .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblformName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblProductName))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblModuleName))
                    .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setAutoscrolls(true);
        panelBody.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelBody.setMinimumSize(new java.awt.Dimension(520, 470));
        panelBody.setName(""); // NOI18N
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(630, 484));

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        jScrollPane1.setAutoscrolls(true);

        tblBillItems.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        tblBillItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Qty", "Amount", "Item Code", "Rate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            tblBillItems.getColumnModel().getColumn(0).setMinWidth(400);
            tblBillItems.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblBillItems.getColumnModel().getColumn(0).setMaxWidth(400);
            tblBillItems.getColumnModel().getColumn(3).setMinWidth(0);
            tblBillItems.getColumnModel().getColumn(3).setPreferredWidth(0);
            tblBillItems.getColumnModel().getColumn(3).setMaxWidth(0);
            tblBillItems.getColumnModel().getColumn(4).setMinWidth(0);
            tblBillItems.getColumnModel().getColumn(4).setPreferredWidth(0);
            tblBillItems.getColumnModel().getColumn(4).setMaxWidth(0);
        }

        lblItems.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        lblItems.setMinimumSize(new java.awt.Dimension(430, 0));
        lblItems.setPreferredSize(new java.awt.Dimension(550, 0));

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblItems, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jScrollPane1)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(4, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosing

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        funOKButtonClicked();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_btnOKMouseClicked

    private void lblPosNameMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPosNameMouseDragged
        // TODO add your handling code here:
//        int x=evt.getXOnScreen();
//        int y=evt.getYOnScreen();
//        this.setLocation(x, y);
    }//GEN-LAST:event_lblPosNameMouseDragged

    private void panelHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelHeaderMouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x, y);
    }//GEN-LAST:event_panelHeaderMouseDragged

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void tblBillItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillItemsMouseClicked
        funTableRowClicked("Mouse");
    }//GEN-LAST:event_tblBillItemsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOK;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblItems;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblBillItems;
    // End of variables declaration//GEN-END:variables

//    private void funResetFields()
//    {
//         try {
//             java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
//             dteFromDate.setDate(date);
//             dteToDate.setDate(date);
//         } catch (ParseException ex) {
//             Logger.getLogger(frmCustomerHistory.class.getName()).log(Level.SEVERE, null, ex);
//         }
//        
//    }
}
