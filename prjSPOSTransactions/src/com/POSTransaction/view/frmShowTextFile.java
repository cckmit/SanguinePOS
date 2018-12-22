/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import java.io.File;
import java.io.FileInputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

public class frmShowTextFile extends javax.swing.JFrame {

    private String transType,formName;
    private File fName;
    /**
     * 
     * @param Data
     * @param formName take form name blank for all reports except Day End
     */
    public frmShowTextFile(String Data,String formName,File file)
    {
        initComponents();
        setLocationRelativeTo(null);
        this.formName=formName;
        fName=file;
        transType=formName;
        txtAreaData.setText(Data);
        btnPrint.setVisible(false);
        //System.out.println("File="+file.getName());
        if(formName.equalsIgnoreCase("sales report") || formName.equalsIgnoreCase("Text Sales Report") || formName.equalsIgnoreCase("Production Order Report"))
        {
            btnPrint.setVisible(true);
        }
    }
    
    private void fun_PrintToPrinter(String printerName, String type) {
        try 
        {
            String reportname = "";
            String fileName = "";
            fileName = "Temp/Temp_Bill.txt";
            reportname = "bill";
            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) 
            {
                printBillWindows(reportname);
            }
            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) 
            {
                Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fun_PrintReportToPrinter(String printerName, String type) {
        try 
        {
            String filePath = System.getProperty("user.dir");
            String fileName = "/Temp";
            fileName+="/"+fName.getName();
            System.out.println("Print= "+fileName);
            fileName=filePath+fileName;
            System.out.println("Print= "+fileName);
            //Sales report Temp_Bill.txt
            
            //Item Wise Temp_ItemWiseReport.txt
            
            /*if (type.equalsIgnoreCase("BillWise")) 
            {
                fileName="/Temp/Temp_Bill.txt";
            }
            else if (type.equalsIgnoreCase("DayEnd")) 
            {
                fileName="/Temp/Temp_DayEndReport.txt";
            }
            else if(type.equalsIgnoreCase("ItemWise"))
            {
                fileName="/Temp/Temp_ItemWiseReport.txt";
            }
            else if(type.equalsIgnoreCase("Sales Report"))
            {
                fileName="/Temp/Temp_Bill.txt";
            }*/
            
            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) 
            {
                //printBillWindows(type);
                
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                //PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
                //DocPrintJob job = defaultService.createPrintJob();
                
                int printerIndex=0;
                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                for (int i = 0; i < printService.length; i++) {

                    if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName())) {
                        printerIndex = i;
                        break;
                    }
                }
                DocPrintJob job = printService[printerIndex].createPrintJob();
                
                FileInputStream fis = new FileInputStream(fileName);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
            }
            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")) 
            {
                Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * printBillWindows() method print to Default Printer. No Parameter required
     */
    private void printBillWindows(String type) {
        try {
            String filePath = System.getProperty("user.dir");
            String filename = "";
            
            if (type.equalsIgnoreCase("bill")) {
                filename = (filePath + "/Temp/Temp_Bill.txt");
            } else if (type.equalsIgnoreCase("dayend")) {
                filename = (filePath + "/Temp/Temp_DayEndReport.txt");
            }
            else if(type.equalsIgnoreCase("Text Sales Report"))
            {
                filename = (filePath + "/Temp/Temp_ItemWiseReport.txt");
            }
            else if(type.equalsIgnoreCase("Sales Report"))
            {
                filename = (filePath + "/Temp/Temp_Bill.txt");
            }
            
            
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            //PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //DocPrintJob job = defaultService.createPrintJob();            
            
            int printerIndex=0;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++) {

                if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName())) {
                    printerIndex = i;
                    break;
                }
            }
            DocPrintJob job = printService[printerIndex].createPrintJob();
            FileInputStream fis = new FileInputStream(filename);
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);

        } catch (Exception e) {
            //e.printStackTrace();
            if(clsGlobalVarClass.gShowPrinterErrorMsg)
            {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 02", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
public frmShowTextFile(){
    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelMainForm = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        scrData = new javax.swing.JScrollPane();
        txtAreaData = new javax.swing.JTextArea();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        panelMainForm.setBackground(new java.awt.Color(216, 238, 254));
        panelMainForm.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 3, true));

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        txtAreaData.setColumns(20);
        txtAreaData.setFont(new java.awt.Font("Lucida Console", 0, 13)); // NOI18N
        txtAreaData.setRows(5);
        scrData.setViewportView(txtAreaData);

        javax.swing.GroupLayout panelMainFormLayout = new javax.swing.GroupLayout(panelMainForm);
        panelMainForm.setLayout(panelMainFormLayout);
        panelMainFormLayout.setHorizontalGroup(
            panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainFormLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrData, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelMainFormLayout.setVerticalGroup(
            panelMainFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainFormLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrData, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelMainFormLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMainForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMainForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
    if("DayEnd".equalsIgnoreCase(transType)){        
        dispose();
    }else{
       dispose();
    }
        
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        if(formName.equalsIgnoreCase("sales report") || formName.equalsIgnoreCase("Text Sales Report") || formName.equalsIgnoreCase("Production Order Report"))
        {
            fun_PrintReportToPrinter("POS", formName);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

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
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmShowTextFile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrData;
    private javax.swing.JTextArea txtAreaData;
    // End of variables declaration//GEN-END:variables
}
