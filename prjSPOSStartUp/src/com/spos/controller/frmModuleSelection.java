/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spos.controller;



import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmTools;
import com.POSLicence.controller.clsClientDetails;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class frmModuleSelection extends javax.swing.JFrame
{   
    Vector vModuleName = new Vector();

    
    
    public frmModuleSelection()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        Date dt = new Date();
        String currentDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
        lblDate.setText(currentDate);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);

        lblMasterModule.setVisible(false);
        lblTransactionModule.setVisible(false);
        lblReportModule.setVisible(false);
        funInitModules();

//        lblMasterModule.requestFocus();
//        lblMasterModule.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    private void funInitModules()
    {
        JLabel[] arrBtnModules =
        {
            lblMasterModule, lblTransactionModule, lblReportModule
        };
        vModuleName.clear();
        try
        {
            if (clsGlobalVarClass.gTheme.equalsIgnoreCase("Default"))
            {
                arrBtnModules[0].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgMasters.png")));
                arrBtnModules[1].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions.png")));
                arrBtnModules[2].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports.png")));
            }
            else
            {
                arrBtnModules[0].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgMasters1.png")));
                arrBtnModules[1].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions1.png")));
                arrBtnModules[2].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports1.png")));
            }

            int cnt = 0;
            if (clsGlobalVarClass.gUserCode.equals("SANGUINE"))
            {
                //arrBtnModules[0].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgMasters.png")));
                arrBtnModules[0].setVisible(true);
                vModuleName.add("Masters");

                // arrBtnModules[1].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions.png")));
                arrBtnModules[1].setVisible(true);
                vModuleName.add("Transactions");

                // arrBtnModules[2].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports.png")));
                arrBtnModules[2].setVisible(true);
                vModuleName.add("Reports");
            }
            else
            {
                String sql = "";
                if (clsGlobalVarClass.gSuperUser == true)
                {
                    sql = "select DISTINCT(b.strModuleType) "
                            + " from tblsuperuserdtl a,tblforms b "
                            + " where a.strFormName=b.strModuleName "
                            + " and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' "
                            + " order by b.strModuleType ";
                }
                else
                {
                    sql = "select DISTINCT(b.strModuleType) "
                            + " from tbluserdtl a,tblforms b "
                            + " where a.strFormName=b.strModuleName "
                            + " and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' "
                            + " order by b.strModuleType ";
                }
                ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

                //Map mapModules=new TreeMap<String,String>();
                Map<Integer, String> mapModules = new TreeMap<Integer, String>();

                while (rs.next())
                {
                    if (rs.getString(1).equals("M"))
                    {
                        mapModules.put(1, "M");
                    }
                    else if (rs.getString(1).equals("T"))
                    {
                        mapModules.put(2, "T");
                    }
                    else if (rs.getString(1).equals("R"))
                    {
                        mapModules.put(3, "R");
                    }
                    /*
                     if(rs.getString(1).equals("M"))
                     {
                     arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgMasters.png")));
                     arrBtnModules[cnt].setVisible(true);
                     vModuleName.add("Masters");
                     }
                     else if(rs.getString(1).equals("T"))
                     {
                     arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions.png")));
                     arrBtnModules[cnt].setVisible(true);
                     vModuleName.add("Transactions");
                     }
                     else if(rs.getString(1).equals("R"))
                     {
                     arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports.png")));
                     arrBtnModules[cnt].setVisible(true);
                     vModuleName.add("Reports");
                     }
                     cnt++;*/
                }
                rs.close();

                for (Map.Entry<Integer, String> entry : mapModules.entrySet())
                {
                    if (entry.getValue().equals("M"))
                    {
                        //arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgMasters.png")));
                        arrBtnModules[0].setVisible(true);
                        vModuleName.add("Masters");
                    }
                    else if (entry.getValue().equals("T"))
                    {
                        // arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions.png")));
                        arrBtnModules[1].setVisible(true);
                        vModuleName.add("Transactions");
                    }
                    else if (entry.getValue().equals("R"))
                    {
                        //arrBtnModules[cnt].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports.png")));
                        arrBtnModules[2].setVisible(true);
                        vModuleName.add("Reports");
                    }
                    cnt++;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funModuleButtonClicked(String moduleName)
    {
        try
        {
            clsGlobalVarClass.gSelectedModule = moduleName;

            if (null != clsGlobalVarClass.gChangeModule && clsGlobalVarClass.gChangeModule.equals("Y"))
            {
                new frmMainMenu().setVisible(true);
            }
            else
            {
                if (clsGlobalVarClass.gUserPOSCode.trim().length() == 0)
                {
                    new frmMainMenu().setVisible(true);
                    clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsGlobalVarClass.gClientCode);
                    clsClientDetails.hmClientDtl.clear();
                    clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
                }
                else if (!clsGlobalVarClass.gUserPOSCode.equals("All POS"))
                {
                    clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsGlobalVarClass.gClientCode);
                    clsClientDetails.hmClientDtl.clear();
                    clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
                    new frmPOSSelection().setVisible(true);
                }
                else            // for ALL POS
                {
                    dispose();
                    clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsGlobalVarClass.gClientCode);
                    clsClientDetails.hmClientDtl.clear();
                    clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
                    new frmPOSSelection().setVisible(true);
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            this.dispose();
            new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
            new frmTools("startup").setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblLogOut = new javax.swing.JLabel();
        panelLayout = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        lblMasterModule = new javax.swing.JLabel();
        lblTransactionModule = new javax.swing.JLabel();
        lblReportModule = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" POS Selection");
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);
        panelHeader.add(filler6);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(250, 30));
        panelHeader.add(lblPosName);

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

        lblLogOut.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLogOut.setForeground(new java.awt.Color(0, 255, 255));
        lblLogOut.setText("LOG OUT");
        lblLogOut.setMaximumSize(new java.awt.Dimension(34, 30));
        lblLogOut.setMinimumSize(new java.awt.Dimension(34, 30));
        lblLogOut.setPreferredSize(new java.awt.Dimension(180, 30));
        lblLogOut.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblLogOutMouseClicked(evt);
            }
        });
        panelHeader.add(lblLogOut);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblMasterModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/Masters.png"))); // NOI18N
        lblMasterModule.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblMasterModuleMouseClicked(evt);
            }
        });
        lblMasterModule.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                lblMasterModuleKeyPressed(evt);
            }
        });

        lblTransactionModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgTransactions.png"))); // NOI18N
        lblTransactionModule.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTransactionModuleMouseClicked(evt);
            }
        });
        lblTransactionModule.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                lblTransactionModuleKeyPressed(evt);
            }
        });

        lblReportModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgReports.png"))); // NOI18N
        lblReportModule.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblReportModuleMouseClicked(evt);
            }
        });
        lblReportModule.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                lblReportModuleKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMasterModule, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(lblTransactionModule, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(lblReportModule, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMasterModule, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTransactionModule, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReportModule, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(191, Short.MAX_VALUE))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOutMouseClicked
        // TODO add your handling code here:
        frmOkCancelPopUp objOKCancel = new frmOkCancelPopUp(this, "Do you want to log out??");
        objOKCancel.setVisible(true);
        if (objOKCancel.getResult() == 1)
        {
            dispose();
            new frmLogin().setVisible(true);
        }
    }//GEN-LAST:event_lblLogOutMouseClicked

    private void lblMasterModuleKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_lblMasterModuleKeyPressed
    {//GEN-HEADEREND:event_lblMasterModuleKeyPressed
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            case 37:
                lblMasterModule.setBorder(null);
                lblTransactionModule.setBorder(null);
                lblReportModule.requestFocus();
                lblReportModule.setBorder(new BevelBorder(BevelBorder.LOWERED));
                break;

            case 39:
                lblMasterModule.setBorder(null);
                lblReportModule.setBorder(null);
                lblTransactionModule.requestFocus();
                lblTransactionModule.setBorder(new BevelBorder(BevelBorder.LOWERED));
                break;

            case 10:
                dispose();
                //funModuleButtonClicked(vModuleName.elementAt(0).toString());
                funModuleButtonClicked("Masters");
                break;
        }
    }//GEN-LAST:event_lblMasterModuleKeyPressed

    private void lblMasterModuleMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblMasterModuleMouseClicked
    {//GEN-HEADEREND:event_lblMasterModuleMouseClicked
        // TODO add your handling code here:
        dispose();
        funModuleButtonClicked("Masters");
    }//GEN-LAST:event_lblMasterModuleMouseClicked

    private void lblTransactionModuleKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_lblTransactionModuleKeyPressed
    {//GEN-HEADEREND:event_lblTransactionModuleKeyPressed
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            case 37:
                lblTransactionModule.setBorder(null);
                lblReportModule.setBorder(null);
                lblMasterModule.requestFocus();
                lblMasterModule.setBorder(new BevelBorder(BevelBorder.LOWERED));

                break;
            case 39:
                lblMasterModule.setBorder(null);
                lblTransactionModule.setBorder(null);
                lblReportModule.requestFocus();
                lblReportModule.setBorder(new BevelBorder(BevelBorder.LOWERED));

                break;
            case 10:
                dispose();
                funModuleButtonClicked("Transactions");
                break;
        }
    }//GEN-LAST:event_lblTransactionModuleKeyPressed

    private void lblTransactionModuleMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTransactionModuleMouseClicked
    {//GEN-HEADEREND:event_lblTransactionModuleMouseClicked
        dispose();
        funModuleButtonClicked("Transactions");
    }//GEN-LAST:event_lblTransactionModuleMouseClicked

    private void lblReportModuleKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_lblReportModuleKeyPressed
    {//GEN-HEADEREND:event_lblReportModuleKeyPressed
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            case 37:

                lblMasterModule.setBorder(null);
                lblReportModule.setBorder(null);
                lblTransactionModule.requestFocus();
                lblTransactionModule.setBorder(new BevelBorder(BevelBorder.LOWERED));

                break;
            case 39:

                lblTransactionModule.setBorder(null);
                lblReportModule.setBorder(null);
                lblMasterModule.requestFocus();
                lblMasterModule.setBorder(new BevelBorder(BevelBorder.LOWERED));

                break;
            case 10:
                dispose();
                funModuleButtonClicked("Reports");
                break;
        }
    }//GEN-LAST:event_lblReportModuleKeyPressed

    private void lblReportModuleMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblReportModuleMouseClicked
    {//GEN-HEADEREND:event_lblReportModuleMouseClicked
        dispose();
        funModuleButtonClicked("Reports");
    }//GEN-LAST:event_lblReportModuleMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblLogOut;
    private javax.swing.JLabel lblMasterModule;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReportModule;
    private javax.swing.JLabel lblTransactionModule;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    // End of variables declaration//GEN-END:variables
}
