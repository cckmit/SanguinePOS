/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class frmCocktailWorldInterface extends javax.swing.JFrame {

    private String sql;
    
    
    public frmCocktailWorldInterface() {
        
        initComponents();
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        try
        {
            funFillPOSCodeCombo();
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            //dteToDate.setDate(date);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funFillPOSCodeCombo() throws Exception
    {
        cmbPosCode.addItem("All                                               All");
        sql = "select strPosName,strPosCode from tblposmaster";
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rs.next())
        {
            cmbPosCode.addItem(rs.getString(1)+"                                               "+rs.getString(2));
        }
        rs.close();
    }

    private void funResetField()
    {
        
    }
    
    
    private String funGetCalenderDate(Date objDate)
    {
        return (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getDate());
    }
    
    
    /**
     * create Temp Folder for text report
     */
    private void funCreateTempFolder()
    {
        String filePath = System.getProperty("user.dir");
        File file = new File(filePath + "/Temp");
        if (!file.exists())
        {
            file.mkdirs();
        }
    }
    
    
    private void funGenerateSaleDataFile() throws Exception
    {
        String selectedPOSCode=cmbPosCode.getSelectedItem().toString();
        String POSCode=selectedPOSCode.split("                                               ")[1];
        
        String fromDate=funGetCalenderDate(dteFromDate.getDate());
        //String toDate=funGetCalenderDate(dteToDate.getDate());
        
        String[] arrFileName=fromDate.split("-");
        String fileName="CW"+arrFileName[2]+arrFileName[1]+arrFileName[0].substring(1,3);
        funCreateTempFolder();
        String filePath = System.getProperty("user.dir");
        File file = new File(filePath + "/Temp/"+fileName+".txt");
        PrintWriter pw = new PrintWriter(file);
        
        StringBuilder sbSql=new StringBuilder();
        
        Map<Integer,List<String>> hmSalesData=new HashMap<Integer,List<String>>();
        
        sbSql.setLength(0);
        sbSql.append("select a.strPOSCode,a.strBillNo,b.strItemCode,sum(b.dblQuantity),b.dblRate,a.strSettelmentMode "
            + " from tblbillhd a,tblbilldtl b,tblitemmaster c "
            + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) = '"+fromDate+"' and b.strItemCode=c.strItemCode ");
        if(!POSCode.equals("All"))
        {
            sbSql.append(" and a.strPOSCode='"+POSCode+"' ");
        }
        sbSql.append(" and c.strRevenueHead='Liquor' ");
        sbSql.append(" group by b.strItemCode order by a.strBillNo");
        
        ResultSet rsSaleData=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
        while(rsSaleData.next())
        {
            String text=rsSaleData.getString(1)+""+rsSaleData.getString(2)+" "+rsSaleData.getString(3)+" "+rsSaleData.getString(4)+" "+rsSaleData.getString(5);
            pw.println(text);
        }
        rsSaleData.close();
        
        sbSql.setLength(0);
        sbSql.append("select a.strPOSCode,a.strBillNo,b.strItemCode,sum(b.dblQuantity),b.dblRate,a.strSettelmentMode "
            + " from tblqbillhd a,tblqbilldtl b,tblitemmaster c "
            + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) = '"+fromDate+"' and b.strItemCode=c.strItemCode ");
        if(!POSCode.equals("All"))
        {
            sbSql.append(" and a.strPOSCode='"+POSCode+"' ");
        }
        sbSql.append(" and c.strRevenueHead='Liquor' ");
        sbSql.append(" group by b.strItemCode order by a.strBillNo");
        
        rsSaleData=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
        while(rsSaleData.next())
        {
            String text=rsSaleData.getString(1)+""+rsSaleData.getString(2)+" "+rsSaleData.getString(3)+" "+rsSaleData.getString(4)+" "+rsSaleData.getString(5);
            pw.println(text);
        }
        rsSaleData.close();
        
        pw.flush();
        pw.close();
        
        Desktop dt = Desktop.getDesktop();
        dt.open(file);
        
    }
    
    
    
    private void funGenerateMenuItemCodeFile() throws Exception
    {
        String fileName="CWMENU";
        funCreateTempFolder();
        String filePath = System.getProperty("user.dir");
        File file = new File(filePath + "/Temp/"+fileName+".txt");
        PrintWriter pw = new PrintWriter(file);
        
        StringBuilder sbSql=new StringBuilder();
        
        sbSql.setLength(0);
        sbSql.append("select strItemCode,strItemName from tblitemmaster where strRevenueHead='Liquor' ");
                
        ResultSet rsMenuItemCodeData=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
        while(rsMenuItemCodeData.next())
        {
            String text=rsMenuItemCodeData.getString(1)+" "+rsMenuItemCodeData.getString(2);
            pw.println(text);
        }
        rsMenuItemCodeData.close();
                
        pw.flush();
        pw.close();
        
        Desktop dt = Desktop.getDesktop();
        dt.open(file);
        
    }
    
    
    
    
    private void funInvokeReport()
    {
        try
        {
            if(cmbReportType.getSelectedItem().toString().equals("Sale Data File"))
            {
                funGenerateSaleDataFile();
            }
            else
            {
                funGenerateMenuItemCodeFile();
            }
        }catch(Exception e)
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
        lblFormName = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnUnsettle = new javax.swing.JButton();
        lblPOSName = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblFromDate = new javax.swing.JLabel();
        lblReportType = new javax.swing.JLabel();
        cmbReportType = new javax.swing.JComboBox();

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
        lblformName.setText("Cocktail World Interface");
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
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setMinimumSize(new java.awt.Dimension(800, 570));
        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);
        panelFormBody.setPreferredSize(new java.awt.Dimension(800, 570));

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Cocktail World Interface");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });

        btnUnsettle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUnsettle.setForeground(new java.awt.Color(255, 255, 255));
        btnUnsettle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnUnsettle.setText("SUBMIT");
        btnUnsettle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUnsettle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnUnsettle.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUnsettleMouseClicked(evt);
            }
        });

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS Name                :");

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.setForeground(new java.awt.Color(255, 255, 255));
        cmbPosCode.setToolTipText("Select POS");

        dteFromDate.setToolTipText("Select From Date");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("Date                        :");

        lblReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReportType.setText("Report Type             :");

        cmbReportType.setBackground(new java.awt.Color(51, 102, 255));
        cmbReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbReportType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sale Data File", "Menu Item Code File" }));
        cmbReportType.setToolTipText("Select POS");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUnsettle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(lblFormName))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblFromDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPOSName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbPosCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(234, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUnsettle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("CW Interface");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnUnsettleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnsettleMouseClicked
        // TODO add your handling code here:
        funInvokeReport();
    }//GEN-LAST:event_btnUnsettleMouseClicked

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
        // TODO add your handling code here:        
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
        // TODO add your handling code here:        
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("CW Interface");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("CW Interface");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnUnsettle;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbReportType;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReportType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    // End of variables declaration//GEN-END:variables
}
