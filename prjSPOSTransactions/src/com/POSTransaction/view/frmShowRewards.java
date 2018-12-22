/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsRewards;
import com.POSGlobal.controller.clsUtility;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmShowRewards extends javax.swing.JFrame
{

    private frmDirectBiller objDirectBiller;
    private frmMakeKOT objMakeKOT;

    private clsUtility objUtility;
    private List<clsRewards> listOfReward;
    private String customerCode;
    private String mobileNo;
    private String customerName;
    private String formName;

    public frmShowRewards(frmDirectBiller objDirectBiller, String customerCode, String customerName, String mobileNo, List<clsRewards> listOfReward)
    {
        initComponents();
        try
        {
            this.objDirectBiller =  objDirectBiller;
            this.formName="DirectBiller";
            this.objUtility = new clsUtility();
            this.listOfReward = listOfReward;
            this.customerCode = customerCode;
            this.customerName = customerName;
            this.mobileNo = mobileNo;

            lblMobileNo.setText(mobileNo);
            lblCustomerName.setText(customerName);

            funFillGridWithItems(listOfReward);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
     public frmShowRewards(frmMakeKOT objMakeKOT, String customerCode, String customerName, String mobileNo, List<clsRewards> listOfReward)
    {
        initComponents();
        try
        {
            this.objMakeKOT =  objMakeKOT;
            this.formName="MakeKOT";
            this.objUtility = new clsUtility();
            this.listOfReward = listOfReward;
            this.customerCode = customerCode;
            this.customerName = customerName;
            this.mobileNo = mobileNo;

            lblMobileNo.setText(mobileNo);
            lblCustomerName.setText(customerName);

            funFillGridWithItems(listOfReward);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    public void funFillGridWithItems(List<clsRewards> listOfReward) throws Exception
    {

        DefaultTableModel dtm = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //all cells false
                return false;
            }
        };
        dtm.setRowCount(0);
        dtm.addColumn("Reward Id");
        dtm.addColumn("Name");
        dtm.addColumn("Category");
        dtm.addColumn("Reward Points");
        dtm.addColumn("Points");
        dtm.addColumn("POS Item Code");
        dtm.addColumn("Type");
        dtm.addColumn("Is Item Off");

        for (clsRewards objRewards : listOfReward)
        {

            Object[] ob =
            {
                objRewards.getStrRewardId(), objRewards.getStrRewardName(), objRewards.getStrRewardCategory(), objRewards.getStrRewardPoints(),objRewards.getStrPoints(), objRewards.getStrRewardPOSItemCode(), objRewards.getStrRewardType(),objRewards.isItemOff()
            };
            dtm.addRow(ob);
        }        

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tblRewards.setModel(dtm);
        tblRewards.setRowHeight(30);

        tblRewards.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//        tblBillItems.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
//        tblBillItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblRewards.getColumnModel().getColumn(0).setPreferredWidth(80);//id
        tblRewards.getColumnModel().getColumn(1).setPreferredWidth(200);//name
        tblRewards.getColumnModel().getColumn(2).setPreferredWidth(100);//category
        tblRewards.getColumnModel().getColumn(3).setPreferredWidth(80);//reward points
        tblRewards.getColumnModel().getColumn(4).setPreferredWidth(75);//points
        tblRewards.getColumnModel().getColumn(5).setPreferredWidth(100);//item code
        tblRewards.getColumnModel().getColumn(6).setPreferredWidth(150);//reward type
        tblRewards.getColumnModel().getColumn(7).setPreferredWidth(75);//is item off

    }

    private void funOKButtonClicked()
    {
        if (tblRewards.getRowCount() > 0)
        {
            int selectedRow = tblRewards.getSelectedRow();

            if (selectedRow >= 0)
            {
                clsRewards objRewards = new clsRewards();
                objRewards.setStrRewardId(tblRewards.getValueAt(selectedRow, 0).toString());//id
                objRewards.setStrRewardName(tblRewards.getValueAt(selectedRow, 1).toString());//name
                objRewards.setStrRewardCategory(tblRewards.getValueAt(selectedRow, 2).toString());//category
                objRewards.setStrRewardPoints(tblRewards.getValueAt(selectedRow, 3).toString());//reward points
                objRewards.setStrPoints(tblRewards.getValueAt(selectedRow, 4).toString());//points
                objRewards.setStrRewardPOSItemCode(tblRewards.getValueAt(selectedRow, 5).toString());//pos item code
                objRewards.setStrRewardType(tblRewards.getValueAt(selectedRow, 6).toString());//type
                objRewards.setItemOff(Boolean.parseBoolean(tblRewards.getValueAt(selectedRow, 7).toString()));//Is item off

                if(formName.equalsIgnoreCase("DirectBiller"))
                {
                    objDirectBiller.setCustomerRewards(objRewards);
                }   
                if(formName.equalsIgnoreCase("MakeKOT"))
                {
                    objMakeKOT.setCustomerRewards(objRewards);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Please Select Reward", "Reward Selection", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please Select Reward", "Reward Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        dispose();
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
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblMobileNo = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
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
        tblRewards = new javax.swing.JTable();
        lblRewardIdLabel = new javax.swing.JLabel();
        lblRewardIdValue = new javax.swing.JLabel();
        lblRewardNameLabel = new javax.swing.JLabel();
        lblRewardNameValue = new javax.swing.JLabel();

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
        panelHeader.setPreferredSize(new java.awt.Dimension(580, 30));
        panelHeader.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                panelHeaderMouseDragged(evt);
            }
        });

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("Customer Rewards : ");

        lblMobileNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMobileNo.setForeground(new java.awt.Color(255, 255, 255));

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCustomerName.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addComponent(lblProductName)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblModuleName)
                    .addComponent(lblformName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblformName, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblProductName))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblModuleName))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(filler6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(filler5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblMobileNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnOK.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOKMouseClicked(evt);
            }
        });
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOKActionPerformed(evt);
            }
        });

        jScrollPane1.setAutoscrolls(true);

        tblRewards.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        tblRewards.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "REWARD ID", "NAME", "CATEGORY", "POINTS", "POS ITEM CODE", "REWARD TYPE"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
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
        tblRewards.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRewards.setRowHeight(30);
        tblRewards.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblRewards.getTableHeader().setReorderingAllowed(false);
        tblRewards.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblRewardsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRewards);
        tblRewards.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblRewards.getColumnModel().getColumnCount() > 0)
        {
            tblRewards.getColumnModel().getColumn(0).setMinWidth(70);
            tblRewards.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblRewards.getColumnModel().getColumn(0).setMaxWidth(70);
            tblRewards.getColumnModel().getColumn(1).setMinWidth(200);
            tblRewards.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblRewards.getColumnModel().getColumn(1).setMaxWidth(200);
            tblRewards.getColumnModel().getColumn(2).setMinWidth(100);
            tblRewards.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblRewards.getColumnModel().getColumn(2).setMaxWidth(100);
            tblRewards.getColumnModel().getColumn(3).setMinWidth(70);
            tblRewards.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblRewards.getColumnModel().getColumn(3).setMaxWidth(70);
            tblRewards.getColumnModel().getColumn(4).setMinWidth(100);
            tblRewards.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblRewards.getColumnModel().getColumn(4).setMaxWidth(100);
            tblRewards.getColumnModel().getColumn(5).setMinWidth(100);
            tblRewards.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblRewards.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        lblRewardIdLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        lblRewardIdLabel.setText("Reward Id:");

        lblRewardIdValue.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N

        lblRewardNameLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        lblRewardNameLabel.setText("Reward Name:");

        lblRewardNameValue.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblRewardIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRewardIdValue, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblRewardNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRewardNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRewardIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRewardIdValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRewardNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRewardNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(4, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
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

    private void tblRewardsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRewardsMouseClicked

        if(tblRewards.getRowCount()>0)
        {
            funTableRowClicked();
        }
    }//GEN-LAST:event_tblRewardsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOK;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblMobileNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRewardIdLabel;
    private javax.swing.JLabel lblRewardIdValue;
    private javax.swing.JLabel lblRewardNameLabel;
    private javax.swing.JLabel lblRewardNameValue;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblRewards;
    // End of variables declaration//GEN-END:variables

    private void funTableRowClicked()
    {
        int selectedRow=tblRewards.getSelectedRow();
        
        lblRewardIdValue.setText(tblRewards.getValueAt(selectedRow, 0).toString());//id
        lblRewardNameValue.setText(tblRewards.getValueAt(selectedRow, 1).toString());//name
    }
}
