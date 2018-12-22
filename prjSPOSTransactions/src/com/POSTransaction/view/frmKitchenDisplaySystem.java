/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmKitchenDisplaySystem extends javax.swing.JFrame {

    private String sql;
    java.util.Vector itemCode;
    clsUtility objUtility;
    
    public frmKitchenDisplaySystem() 
    {
        initComponents();
        objUtility=new clsUtility();
        itemCode=new java.util.Vector();
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        funFillTable();
        Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    tickTock();
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
    }
    
    public void tickTock()
    {
        lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }
    
    public void funFillTable()
    {
        try
        {
            //System.out.println(objUtility.funGetOnlyPOSDateForTransaction());
            DefaultTableModel dm = (DefaultTableModel) tblItemTable.getModel();
            dm.getDataVector().removeAllElements();
            itemCode.removeAllElements();
            int i=0;
            int SumQty=0;
            
            sql="select a.strItemCode,strBillNo,a.strItemName,dblQuantity,time(dteBillDate) OrderTime,"
                    + "RIGHT(TIMEDIFF(time(now()),time(dteBillDate)),5) Difference,"
                    + "(intProcTimeMin-RIGHT(TIMEDIFF(time(now()),time(dteBillDate)),5)) as Delay from "
                    + "tblbilldtl a,tblitemmaster b where a.strItemCode = b.strItemCode and date(dteBillDate) "
                    + "between '"+objUtility.funGetOnlyPOSDateForTransaction()+"'  and '"+objUtility.funGetOnlyPOSDateForTransaction()+"'  and tmeOrderProcessing = '00:00:00' "
                    + "order by dteBillDate asc ";           
            //System.out.println(sql);
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rs.next())
            {
                 i++;
                 sql="select count(*) from tblhomedelivery where strBillNo='"+rs.getString(2)+"'";
                 ResultSet rs1=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                 rs1.next();
                 int flag=rs1.getInt(1);
                 if(flag==1)
                 {
                     itemCode.add(rs.getString(1));                     
                     Object[] rows = {i, rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),"HD",false};
                     dm.addRow(rows);
                 }
                 else
                 {
                    itemCode.add(rs.getString(1));                      
                    Object[] rows  = {i, rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),"Dining",false};
                    dm.addRow(rows);
                 }
                 SumQty=SumQty+rs.getInt(4);
            }
            for(int row=0;row<tblItemTable.getRowCount();row++)
            {
                int delay=Integer.parseInt(tblItemTable.getValueAt(row, 6).toString());
                Component comp = null;
                if(delay<1)
                {                      
                }
                else
                {                    
                }
            }
            
            lblSumOfQty.setText(Integer.toString(SumQty));
            lblItemCount.setText(Integer.toString(i));
            tblItemTable.setModel(dm);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(45);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(75);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblItemTable.getColumnModel().getColumn(5).setPreferredWidth(70);
            tblItemTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            tblItemTable.getColumnModel().getColumn(7).setPreferredWidth(70);
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
     public void selection()
    {
        int row=tblItemTable.getSelectedRow();
        int col=5;
        int match=Integer.parseInt(tblItemTable.getValueAt(row, col).toString());
        if(match==0)
        {
            tblItemTable.setValueAt("1",row, 5);
        }
        else if(match==1)
        {
            tblItemTable.setValueAt("0",row, 5);
        }
    }
    
    public void changecolor()
    {
        for(int row=0;row<tblItemTable.getRowCount();row++)
        {
             int match=Integer.parseInt(tblItemTable.getValueAt(row, 5).toString());
             if(match==1)
             {
                 tblItemTable.setGridColor(Color.red);
             }
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
        panelFormBody1 = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        btnorderProcess = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        scrKDS = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblItemCount = new javax.swing.JLabel();
        lblSumOfQty = new javax.swing.JLabel();

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
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- KDS");
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

        panelFormBody1.setOpaque(false);
        panelFormBody1.setLayout(null);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Kitchen Display System");
        lblFormName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelFormBody1.add(lblFormName);
        lblFormName.setBounds(260, 0, 270, 31);

        btnorderProcess.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnorderProcess.setForeground(new java.awt.Color(255, 255, 255));
        btnorderProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnorderProcess.setText("Order Processed");
        btnorderProcess.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnorderProcess.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnorderProcess.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnorderProcessMouseClicked(evt);
            }
        });
        btnorderProcess.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnorderProcessActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnorderProcess);
        btnorderProcess.setBounds(90, 490, 280, 50);

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExitActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnExit);
        btnExit.setBounds(480, 490, 280, 50);

        scrKDS.setBackground(new java.awt.Color(255, 255, 255));

        tblItemTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Sr No.", "Bill No.", "Item Name", "Qty", "Order Time", "Differrence", "Delay", "Ord For", "Processed"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        tblItemTable.setRowHeight(40);
        tblItemTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblItemTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrKDS.setViewportView(tblItemTable);

        panelFormBody1.add(scrKDS);
        scrKDS.setBounds(0, 30, 780, 450);

        lblItemCount.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        lblItemCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panelFormBody1.add(lblItemCount);
        lblItemCount.setBounds(10, 490, 70, 50);

        lblSumOfQty.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        lblSumOfQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSumOfQty.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelFormBody1.add(lblSumOfQty);
        lblSumOfQty.setBounds(400, 490, 70, 50);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFormBody1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFormBody1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnorderProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnorderProcessMouseClicked
        // TODO add your handling code here:
        try
        {
            SimpleDateFormat hour,minute,second,am ;
            java.util.Date dt=new java.util.Date();
            Date dNow = new Date( );
            hour = new SimpleDateFormat ("hh");
            minute=new SimpleDateFormat("mm");
            second=new SimpleDateFormat("ss");
            am=new SimpleDateFormat("a");

            for(int row=0;row<tblItemTable.getRowCount();row++)
            {
                boolean applicable=Boolean.parseBoolean(tblItemTable.getValueAt(row, 8).toString());
                if(applicable==true)
                {
                    String BillNo=tblItemTable.getValueAt(row,1).toString();
                    String ItemCode=itemCode.elementAt(row).toString();
                    sql="update tblbilldtl set tmeOrderProcessing='"+hour.format(dNow)+minute.format(dNow)+second.format(dNow)
                    +"' where strBillNo='"+BillNo+"' and strItemCode='"+ItemCode+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                }
            }
            funFillTable();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnorderProcessMouseClicked

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Kitchen System");
        //System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
        // TODO add your handling code here:
        try
        {
            for(int k=0;k<tblItemTable.getRowCount();k++)
            {
                boolean applicable=Boolean.parseBoolean(tblItemTable.getValueAt(k, 5).toString());
                if(applicable==true)
                {
                    break;
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnorderProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnorderProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnorderProcessActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Kitchen System");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Kitchen System");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmKitchenDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmKitchenDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmKitchenDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmKitchenDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmKitchenDisplaySystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnorderProcess;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSumOfQty;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelFormBody1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrKDS;
    private javax.swing.JTable tblItemTable;
    // End of variables declaration//GEN-END:variables
}
