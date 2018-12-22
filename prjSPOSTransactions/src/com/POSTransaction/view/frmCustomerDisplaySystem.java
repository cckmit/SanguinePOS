/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmCustomerDisplaySystem extends javax.swing.JFrame
{

    private String sql;   
    private final DefaultTableModel dtmCustomerWiseBill;
    private final Timer refreshTimer;

    public frmCustomerDisplaySystem()
    {
        initComponents();
        
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        
        tblCustomerWiseBillTable.setRowHeight(150);
        dtmCustomerWiseBill = (DefaultTableModel) tblCustomerWiseBillTable.getModel();
        
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
        
        
        
        refreshTimer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Refresh Bill");
                funFillTable();                
            }
        });
        refreshTimer.setRepeats(true);
        refreshTimer.setCoalesce(true);
        refreshTimer.setInitialDelay(0);
        refreshTimer.start();
    }

    public void tickTock()
    {
        lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));       
    }

    public void funFillTable()
    {
        try
        {
            java.util.Date dt1 = new java.util.Date();
            int day = dt1.getDate();
            int month = dt1.getMonth() + 1;
            int year = dt1.getYear() + 1900;
            String chkDate = year + "-" + month + "-" + day;
                      
            dtmCustomerWiseBill.setRowCount(0);
        
            sql = "SELECT a.strBillNo AS BillNo,a.dblGrandTotal AS GrandTotal, IFNULL(b.strCustomerName,'') AS CustomerName\n" +
                    "FROM tblbillhd a " +
                    "LEFT OUTER " +
                    "JOIN tblcustomermaster b ON a.strCustomerCode=b.strCustomerCode "+
                    "WHERE a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' "+
                    "ORDER BY a.strPOSCode,BillNo DESC";

            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
               Object object[]={rs.getString("BillNo"),rs.getString("CustomerName"),rs.getString("GrandTotal")};
               dtmCustomerWiseBill.addRow(object);
            }
           
            tblCustomerWiseBillTable.setModel(dtmCustomerWiseBill);
            tblCustomerWiseBillTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
           
            DefaultTableCellRenderer leftCellRenderer=new DefaultTableCellRenderer();
            leftCellRenderer.setHorizontalAlignment(JLabel.LEFT);
            DefaultTableCellRenderer rightCellRenderer=new DefaultTableCellRenderer();
            rightCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
            
            tblCustomerWiseBillTable.getColumnModel().getColumn(0).setCellRenderer(leftCellRenderer);
            tblCustomerWiseBillTable.getColumnModel().getColumn(1).setCellRenderer(leftCellRenderer);
            //tblCustomerWiseBillTable.getColumnModel().getColumn(2).setCellRenderer(rightCellRenderer);
            
            tblCustomerWiseBillTable.getColumnModel().getColumn(0).setPreferredWidth(600);
            tblCustomerWiseBillTable.getColumnModel().getColumn(1).setPreferredWidth(655);
            //tblCustomerWiseBillTable.getColumnModel().getColumn(2).setPreferredWidth(155);

        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        lblFormName = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        scrKDS = new javax.swing.JScrollPane();
        tblCustomerWiseBillTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        lblformName.setText("- CDS");
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

        lblFormName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Customer Display System");
        lblFormName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExitMouseClicked(evt);
            }
        });

        scrKDS.setBackground(new java.awt.Color(255, 255, 255));

        tblCustomerWiseBillTable.setFont(new java.awt.Font("Tahoma", 1, 50)); // NOI18N
        tblCustomerWiseBillTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No.", "Customer Name"
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
        tblCustomerWiseBillTable.setRowHeight(40);
        tblCustomerWiseBillTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblCustomerWiseBillTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblCustomerWiseBillTable.getTableHeader().setReorderingAllowed(false);
        scrKDS.setViewportView(tblCustomerWiseBillTable);
        if (tblCustomerWiseBillTable.getColumnModel().getColumnCount() > 0)
        {
            tblCustomerWiseBillTable.getColumnModel().getColumn(0).setResizable(false);
            tblCustomerWiseBillTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblCustomerWiseBillTable.getColumnModel().getColumn(1).setResizable(false);
            tblCustomerWiseBillTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        }

        javax.swing.GroupLayout panelMainFormLayout = new javax.swing.GroupLayout(panelMainForm);
        panelMainForm.setLayout(panelMainFormLayout);
        panelMainFormLayout.setHorizontalGroup(
            panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainFormLayout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scrKDS)
        );
        panelMainFormLayout.setVerticalGroup(
            panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainFormLayout.createSequentialGroup()
                .addGroup(panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrKDS, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
        );

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnExitMouseClicked
    {//GEN-HEADEREND:event_btnExitMouseClicked
        dispose();
        refreshTimer.stop();
        clsGlobalVarClass.hmActiveForms.remove("CustomerDisplaySystem");
    }//GEN-LAST:event_btnExitMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("CustomerDisplaySystem");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("CustomerDisplaySystem");
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(frmCustomerDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerDisplaySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new frmCustomerDisplaySystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrKDS;
    private javax.swing.JTable tblCustomerWiseBillTable;
    // End of variables declaration//GEN-END:variables
}
