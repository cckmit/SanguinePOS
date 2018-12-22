/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumberKeyPad;
import java.math.BigDecimal;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class panelModifier extends javax.swing.JPanel
{

    frmMakeKOT objMakeKOT;
    frmDirectBiller objDirectBiller;
    frmAdvanceOrder objAdvOrder;
    Object obj;
    ResultSet rs;
    private String sql, itemCode;
    int count;

    public panelModifier(Object obj)
    {
        try
        {
            initComponents();
            this.obj = obj;
            if (obj.getClass().getName().equals("com.POSTransaction.view.frmMakeKOT"))
            {
                objMakeKOT = (frmMakeKOT) obj;
            }
            else if (obj.getClass().getName().equals("com.POSTransaction.view.frmAdvanceOrder"))
            {
                objAdvOrder = (frmAdvanceOrder) obj;
            }
            else
            {
                objDirectBiller = (frmDirectBiller) obj;
            }
            txtFreeFlowName.setVisible(false);
            txtFreeFlowRate.setVisible(false);
            btnOK.setVisible(false);
            lblfreeFlow.setVisible(false);
            lblfreeflowrate.setVisible(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funFillTable(String itemCode)
    {
        try
        {
            this.itemCode = itemCode;
            String modifiercode = null;
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };

            dm.addColumn("Modifier Name");
            dm.addColumn("Modifier Desc");
            sql = "select strModifierCode from tblitemmodofier where strItemCode='" + itemCode + "' "
                + "or strItemCode='All' "
                + " group by strModifierCode";

            ResultSet rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs1.next())
            {
                modifiercode = rs1.getString(1);
                sql = "select strModifierName,strModifierDesc from  tblmodifiermaster "
                        + "where strModifierCode='" + modifiercode + "'";

                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rs.next())
                {
                    Object[] rows =
                    {
                        rs.getString(1), rs.getString(2)
                    };
                    dm.addRow(rows);
                }
                rs.close();
                tblBillDetails.setModel(dm);
                tblBillDetails.getColumnModel().getColumn(0).setPreferredWidth(70);
                tblBillDetails.getColumnModel().getColumn(1).setPreferredWidth(143);
            }
            rs1.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funTableBillDtlClicked()
    {
        try
        {
            int i = tblBillDetails.getSelectedRow();
            count++;
            String modifierName = tblBillDetails.getModel().getValueAt(i, 0).toString();
            if (obj.getClass().getName().equals("com.POSTransaction.view.frmMakeKOT"))
            {
                objMakeKOT = (frmMakeKOT) obj;
                if (objMakeKOT.flag_isTDHModifier_Item)
                {
                    objMakeKOT.funGetModifierRateForTDHOnModifier(modifierName, itemCode);
                }
                else
                {
                    objMakeKOT.funGetModifierRate(modifierName);
                    this.setVisible(false);
                    objMakeKOT.funShowPanel();
                }
            }
            else if (obj.getClass().getName().equals("com.POSTransaction.view.frmAdvanceOrder"))
            {
                objAdvOrder = (frmAdvanceOrder) obj;
                objAdvOrder.getModifierRate(modifierName);
                setVisible(false);
                objAdvOrder.funShowItemPanel();

            }
            else
            {
                objDirectBiller = (frmDirectBiller) obj;
                objDirectBiller.funGetModifierRate(modifierName, itemCode);
                if (objDirectBiller.flag_isTDHModifier_Item)
                {
                    this.setVisible(true);
                    frmDirectBiller.flagTDHItem = true;
                }
                else
                {
                    frmDirectBiller.flagTDHItem = false;
                    this.setVisible(false);
                    objDirectBiller.funShowPanel();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCloseButtonClicked()
    {
        try
        {
            setVisible(false);
            if (obj.getClass().getName().equals("com.POSTransaction.view.frmMakeKOT"))
            {
                objMakeKOT = (frmMakeKOT) obj;
                objMakeKOT.funShowPanel();
            }
            else if (obj.getClass().getName().equals("com.POSTransaction.view.frmAdvanceOrder"))
            {
                objAdvOrder = (frmAdvanceOrder) obj;
                objAdvOrder.funShowItemPanel();
            }
            else
            {
                objDirectBiller = (frmDirectBiller) obj;
                objDirectBiller.funShowPanel();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFreeFlowModButtonClicked()
    {
        lblfreeFlow.setVisible(true);
        lblfreeflowrate.setVisible(true);
        txtFreeFlowName.setVisible(true);
        txtFreeFlowRate.setVisible(true);
        txtFreeFlowRate.setText("0");
        btnOK.setVisible(true);
        jScrollPane1.setVisible(false);
    }

    private void funOKButtonClicked()
    {
        if (obj.getClass().getName().equals("com.POSTransaction.view.frmAdvanceOrder"))
        {
            objAdvOrder.funInsertFreeFlowModifier(txtFreeFlowName.getText(), Double.parseDouble(txtFreeFlowRate.getText()));
            setVisible(false);
            objAdvOrder.funShowItemPanel();
            funResetfreeflow();
        }
        else if (obj.getClass().getName().equals("com.POSTransaction.view.frmMakeKOT"))
        {
            objMakeKOT = (frmMakeKOT) obj;
            objMakeKOT.funInsertFreeFlowModifier(txtFreeFlowName.getText(), new BigDecimal(txtFreeFlowRate.getText()));
            setVisible(false);
            objMakeKOT.funShowItemPanel();
            funResetfreeflow();
        }
        else
        {
            objDirectBiller.funInsertFreeFlowModifier(txtFreeFlowName.getText(), new BigDecimal(txtFreeFlowRate.getText()));
            setVisible(false);
            objDirectBiller.funShowItemPanel();
            funResetfreeflow();
        }
    }

    private void funPredefinedModifierButtonClicked()
    {
        lblfreeFlow.setVisible(false);
        lblfreeflowrate.setVisible(false);
        txtFreeFlowName.setVisible(false);
        txtFreeFlowRate.setVisible(false);
        btnOK.setVisible(false);
        jScrollPane1.setVisible(true);
    }

    public void getItemCode(String textValue)
    {
        count = 0;
        funFillTable(textValue);
    }

    
    /*
    void setItemName(String text) {
        txtFreeFlowName.setText(text);
    }

    void setfreeFlowModifieRate(String toString) {
        txtFreeFlowRate.setText(toString);
    }*/
    
    
    private void funResetfreeflow()
    {
        lblfreeFlow.setVisible(false);
        lblfreeflowrate.setVisible(false);
        txtFreeFlowName.setVisible(false);
        txtFreeFlowRate.setVisible(false);
        txtFreeFlowRate.setText("0");
        btnOK.setVisible(false);
        jScrollPane1.setVisible(true);
        txtFreeFlowName.setText("");
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

        txtFreeFlowName = new javax.swing.JTextField();
        txtFreeFlowRate = new javax.swing.JTextField();
        lblfreeflowrate = new javax.swing.JLabel();
        lblfreeFlow = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCloseModPanel = new javax.swing.JButton();
        btnFreeFlowModi = new javax.swing.JButton();
        btnPredefinedModifier = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillDetails = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 235, 174)));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setInheritsPopupMenu(true);
        setPreferredSize(new java.awt.Dimension(340, 340));
        setLayout(null);

        txtFreeFlowName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFreeFlowNameMouseClicked(evt);
            }
        });
        add(txtFreeFlowName);
        txtFreeFlowName.setBounds(80, 50, 260, 40);

        txtFreeFlowRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFreeFlowRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFreeFlowRateMouseClicked(evt);
            }
        });
        txtFreeFlowRate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtFreeFlowRateActionPerformed(evt);
            }
        });
        add(txtFreeFlowRate);
        txtFreeFlowRate.setBounds(80, 110, 90, 40);

        lblfreeflowrate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblfreeflowrate.setText("Rate");
        add(lblfreeflowrate);
        lblfreeflowrate.setBounds(10, 114, 60, 30);

        lblfreeFlow.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblfreeFlow.setText("Enter Text");
        add(lblfreeFlow);
        lblfreeFlow.setBounds(0, 54, 80, 30);

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setText("Ok");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOKMouseClicked(evt);
            }
        });
        add(btnOK);
        btnOK.setBounds(50, 190, 240, 40);

        btnCloseModPanel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCloseModPanel.setForeground(new java.awt.Color(255, 255, 255));
        btnCloseModPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCloseModPanel.setText("Close");
        btnCloseModPanel.setToolTipText("Close Modifier Panel");
        btnCloseModPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseModPanel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCloseModPanel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCloseModPanel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseModPanelActionPerformed(evt);
            }
        });
        add(btnCloseModPanel);
        btnCloseModPanel.setBounds(250, 0, 90, 40);

        btnFreeFlowModi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnFreeFlowModi.setForeground(new java.awt.Color(255, 255, 255));
        btnFreeFlowModi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnFreeFlowModi.setText("<html>Free Flow<br>Modifier</html>");
        btnFreeFlowModi.setToolTipText("Free Flow Modifier");
        btnFreeFlowModi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFreeFlowModi.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnFreeFlowModi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnFreeFlowModiMouseClicked(evt);
            }
        });
        btnFreeFlowModi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFreeFlowModiActionPerformed(evt);
            }
        });
        add(btnFreeFlowModi);
        btnFreeFlowModi.setBounds(0, 0, 90, 40);

        btnPredefinedModifier.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPredefinedModifier.setForeground(new java.awt.Color(255, 255, 255));
        btnPredefinedModifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPredefinedModifier.setText("<html>PreDefine<br>Modifier</html>");
        btnPredefinedModifier.setToolTipText("Predefined Modifier");
        btnPredefinedModifier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPredefinedModifier.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnPredefinedModifier.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPredefinedModifierMouseClicked(evt);
            }
        });
        add(btnPredefinedModifier);
        btnPredefinedModifier.setBounds(120, 0, 90, 40);

        tblBillDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String []
            {
                "Modifier Name", "Modifier Desc"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblBillDetails.setRowHeight(25);
        tblBillDetails.getTableHeader().setReorderingAllowed(false);
        tblBillDetails.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblBillDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBillDetails);
        if (tblBillDetails.getColumnModel().getColumnCount() > 0)
        {
            tblBillDetails.getColumnModel().getColumn(0).setResizable(false);
            tblBillDetails.getColumnModel().getColumn(1).setResizable(false);
        }

        add(jScrollPane1);
        jScrollPane1.setBounds(0, 40, 340, 370);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseModPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseModPanelActionPerformed
        // TODO add your handling code here:
        funCloseButtonClicked();
    }//GEN-LAST:event_btnCloseModPanelActionPerformed

    private void tblBillDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailsMouseClicked

        funTableBillDtlClicked();
    }//GEN-LAST:event_tblBillDetailsMouseClicked

    private void btnFreeFlowModiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFreeFlowModiMouseClicked
        // TODO add your handling code here:        
        funFreeFlowModButtonClicked();
    }//GEN-LAST:event_btnFreeFlowModiMouseClicked

    private void btnFreeFlowModiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreeFlowModiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFreeFlowModiActionPerformed

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        // TODO add your handling code here:
        funOKButtonClicked();
    }//GEN-LAST:event_btnOKMouseClicked

    private void txtFreeFlowRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFreeFlowRateMouseClicked
        // TODO add your handling code here:        
        clsGlobalVarClass.gNumerickeyboardValue="";
        frmNumberKeyPad objNumKeyPad=new frmNumberKeyPad(objAdvOrder, true, "ffRate", this);
        objNumKeyPad.setVisible(true);
        if(clsGlobalVarClass.gRateEntered=true)
        {
            txtFreeFlowRate.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            txtFreeFlowRate.setText("0.00");
        }
    }//GEN-LAST:event_txtFreeFlowRateMouseClicked

    private void txtFreeFlowNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFreeFlowNameMouseClicked
        // TODO add your handling code here:
        new frmAlfaNumericKeyBoard(null, true, "1", "Enter Name ").setVisible(true);
        txtFreeFlowName.setText(clsGlobalVarClass.gKeyboardValue);
    }//GEN-LAST:event_txtFreeFlowNameMouseClicked

    private void txtFreeFlowRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFreeFlowRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFreeFlowRateActionPerformed

    private void btnPredefinedModifierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPredefinedModifierMouseClicked
        // TODO add your handling code here:
        funPredefinedModifierButtonClicked();
    }//GEN-LAST:event_btnPredefinedModifierMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseModPanel;
    private javax.swing.JButton btnFreeFlowModi;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPredefinedModifier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblfreeFlow;
    private javax.swing.JLabel lblfreeflowrate;
    private javax.swing.JTable tblBillDetails;
    private javax.swing.JTextField txtFreeFlowName;
    private javax.swing.JTextField txtFreeFlowRate;
    // End of variables declaration//GEN-END:variables

}
