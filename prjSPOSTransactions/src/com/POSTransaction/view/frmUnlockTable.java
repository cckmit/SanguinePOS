/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmUnlockTable extends javax.swing.JFrame 
{

    public java.util.Vector vOpenTableNo, vOpenTableName;
    public String sql;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    HashMap<Integer, String>mapSelectedTable=null;

    public frmUnlockTable()
    {
        initComponents();
        try
        {
     
            vOpenTableNo = new Vector();
            vOpenTableName = new Vector();
            mapSelectedTable=new HashMap<>();
            btnPrevious.setEnabled(false);
            cntNavigate = 0;
            cntNavigate1 = 0;
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            Date date1 = new Date();
            String new_str = String.format("%tr", date1);
            String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
            lblDate.setText(dateAndTime);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            funFillTableVector("", "");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillTableVector(String searchOpenTableText, String searchAllTableText)
    {
        try
        {
            cntNavigate = 0;
            cntNavigate1 = 0;

            btnPrevious.setEnabled(false);
            btnNext.setEnabled(true);
            vOpenTableNo.removeAllElements();
            vOpenTableName.removeAllElements();
          
            ResultSet rsTblNo = null;
           sql = "select a.strTableNo,b.strTableName from tblitemrtemp a,tbltablemaster b "
                + " where a.strTableNo=b.strTableNo and a.strTableStatus='BillingInProgress' ";
            if (!clsGlobalVarClass.gMoveTableToOtherPOS)
            {
                sql += " and (b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPOSCode='All') ";

            }
             sql += " group by a.strTableNo order by b.intSequence ";
            rsTblNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTblNo.next())
            {
                vOpenTableNo.add(rsTblNo.getString(1));
                vOpenTableName.add(rsTblNo.getString(2));
            }
            funLoadOpenTables(0, vOpenTableNo.size());

            
            if (vOpenTableNo.size() <= 32)
            {
                btnNext.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private void funLoadOpenTables(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;

            JButton[] btnTableArray =
            {
                btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16,
                btnOpenTable17, btnOpenTable18, btnOpenTable19, btnOpenTable20, btnOpenTable21, btnOpenTable22, btnOpenTable23, btnOpenTable24, btnOpenTable25, btnOpenTable26, btnOpenTable27, btnOpenTable28, btnOpenTable29, btnOpenTable30, btnOpenTable31, btnOpenTable32
            };
            for (int k = 0; k < btnTableArray.length; k++)
            {
                btnTableArray[k].setForeground(Color.black);
                btnTableArray[k].setBackground(Color.lightGray);
                btnTableArray[k].setText("");
            }
            System.out.println(vOpenTableName.size());
            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == vOpenTableName.size())
                {
                    break;
                }
                String tblName = vOpenTableName.elementAt(i).toString();
                if (cntIndex < 32)
                {
                    btnTableArray[cntIndex].setText("<html>" + tblName + "<br>" + "</html>");
                    
                    btnTableArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            //System.out.println("Open Table Index="+cntIndex);
            for (int j = cntIndex; j < 32; j++)
            {
                btnTableArray[j].setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private void funSelectOpenTable(String tableName, int index)
    {
        try
        {
            if (tableName.trim().length() > 0)
            {
                index = (cntNavigate * 32) + index;
                lblOpenTableName.setText(vOpenTableName.elementAt(index).toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean funValidateTables()
    {
        boolean flgValidate = true;
        try
        {
            if (mapSelectedTable.size() == 0)
            {
                JOptionPane.showMessageDialog(this, "Select tables to unlock!!!");
                flgValidate = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return flgValidate;
    }

  

    private void funClearFields()
    {
        mapSelectedTable.clear();
        lblAllTableName.setText("");
        lblOpenTableName.setText("");
    }

   

    private void funSetDefaultColorOpen(String tableName, int btnIndex)
    {
        try
        {
            if (tableName.trim().length() > 0)
            {
                JButton[] btnTableArray =
                {
                    btnOpenTable1, btnOpenTable2, btnOpenTable3, btnOpenTable4, btnOpenTable5, btnOpenTable6, btnOpenTable7, btnOpenTable8, btnOpenTable9, btnOpenTable10, btnOpenTable11, btnOpenTable12, btnOpenTable13, btnOpenTable14, btnOpenTable15, btnOpenTable16,
                    btnOpenTable17, btnOpenTable18, btnOpenTable19, btnOpenTable20, btnOpenTable21, btnOpenTable22, btnOpenTable23, btnOpenTable24, btnOpenTable25, btnOpenTable26, btnOpenTable27, btnOpenTable28, btnOpenTable29, btnOpenTable30, btnOpenTable31, btnOpenTable32
                    
                };
                String tblNo= vOpenTableNo.elementAt(btnIndex).toString();
                Color btnColor = btnTableArray[btnIndex].getBackground();
                if(btnColor != Color.RED)
                  {
                        btnTableArray[btnIndex].setBackground(Color.RED);
                        mapSelectedTable.put(btnIndex,tblNo);
                  } 
                  else
                  {
                        btnTableArray[btnIndex].setBackground(Color.lightGray);
                        mapSelectedTable.remove(btnIndex,tblNo);
                  } 
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    private void funSaveButtonClicked()
    {
        clsUtility objUtility=new clsUtility();
        if (funValidateTables())
        {
            funUnlockTable();
        }
    }
    
    
     private void funUnlockTable()
    {
        clsUtility objUtility = new clsUtility();
        try
        {
            if(mapSelectedTable.size()>0)
               for (Map.Entry<Integer, String> entry : mapSelectedTable.entrySet()) 
               {
                    sql = "update tblitemrtemp set strTableStatus='Normal' where strTableNo in ('"+entry.getValue()+"');";
                    clsGlobalVarClass.dbMysql.execute(sql);
                }
        
                JOptionPane.showMessageDialog(this,"Unlocked table successfully!! ");
                funFillTableVector("", "");
                funClearFields();

        }
        catch (Exception e)
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
    private void initComponents() {

        lblformName = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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
        panelOpenTable = new javax.swing.JPanel();
        btnOpenTable2 = new javax.swing.JButton();
        btnOpenTable1 = new javax.swing.JButton();
        btnOpenTable3 = new javax.swing.JButton();
        btnOpenTable4 = new javax.swing.JButton();
        btnOpenTable5 = new javax.swing.JButton();
        btnOpenTable6 = new javax.swing.JButton();
        btnOpenTable7 = new javax.swing.JButton();
        btnOpenTable8 = new javax.swing.JButton();
        btnOpenTable9 = new javax.swing.JButton();
        btnOpenTable10 = new javax.swing.JButton();
        btnOpenTable11 = new javax.swing.JButton();
        btnOpenTable12 = new javax.swing.JButton();
        btnOpenTable13 = new javax.swing.JButton();
        btnOpenTable14 = new javax.swing.JButton();
        btnOpenTable15 = new javax.swing.JButton();
        btnOpenTable16 = new javax.swing.JButton();
        btnOpenTable17 = new javax.swing.JButton();
        btnOpenTable18 = new javax.swing.JButton();
        btnOpenTable19 = new javax.swing.JButton();
        btnOpenTable20 = new javax.swing.JButton();
        btnOpenTable21 = new javax.swing.JButton();
        btnOpenTable22 = new javax.swing.JButton();
        btnOpenTable23 = new javax.swing.JButton();
        btnOpenTable24 = new javax.swing.JButton();
        btnOpenTable25 = new javax.swing.JButton();
        btnOpenTable26 = new javax.swing.JButton();
        btnOpenTable27 = new javax.swing.JButton();
        btnOpenTable28 = new javax.swing.JButton();
        btnOpenTable29 = new javax.swing.JButton();
        btnOpenTable30 = new javax.swing.JButton();
        btnOpenTable31 = new javax.swing.JButton();
        btnOpenTable32 = new javax.swing.JButton();
        lblOpenTableName = new javax.swing.JLabel();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblAllTableName = new javax.swing.JLabel();
        btnUnlockTable = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblformName.setBackground(new java.awt.Color(69, 164, 238));
        lblformName.setLayout(new javax.swing.BoxLayout(lblformName, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        lblformName.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.add(lblModuleName);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" - Move Table");
        lblformName.add(jLabel2);
        lblformName.add(filler4);
        lblformName.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblformName.add(lblPosName);
        lblformName.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblformName.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblformName.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblformName.add(lblHOSign);

        getContentPane().add(lblformName, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);

        btnOpenTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable2MouseClicked(evt);
            }
        });

        btnOpenTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable1MouseClicked(evt);
            }
        });

        btnOpenTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable3MouseClicked(evt);
            }
        });

        btnOpenTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable4MouseClicked(evt);
            }
        });

        btnOpenTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable5MouseClicked(evt);
            }
        });

        btnOpenTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable6MouseClicked(evt);
            }
        });

        btnOpenTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable7MouseClicked(evt);
            }
        });

        btnOpenTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable8MouseClicked(evt);
            }
        });

        btnOpenTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable9MouseClicked(evt);
            }
        });

        btnOpenTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable10MouseClicked(evt);
            }
        });

        btnOpenTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable11MouseClicked(evt);
            }
        });

        btnOpenTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable12MouseClicked(evt);
            }
        });
        btnOpenTable12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenTable12ActionPerformed(evt);
            }
        });

        btnOpenTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable13MouseClicked(evt);
            }
        });

        btnOpenTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable14MouseClicked(evt);
            }
        });

        btnOpenTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable15MouseClicked(evt);
            }
        });

        btnOpenTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable16MouseClicked(evt);
            }
        });

        btnOpenTable17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable17MouseClicked(evt);
            }
        });

        btnOpenTable18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable18MouseClicked(evt);
            }
        });

        btnOpenTable19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable19MouseClicked(evt);
            }
        });

        btnOpenTable20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable20MouseClicked(evt);
            }
        });

        btnOpenTable21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable21MouseClicked(evt);
            }
        });

        btnOpenTable22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable22MouseClicked(evt);
            }
        });

        btnOpenTable23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable23MouseClicked(evt);
            }
        });

        btnOpenTable24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable24MouseClicked(evt);
            }
        });

        btnOpenTable25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable25MouseClicked(evt);
            }
        });

        btnOpenTable26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable26MouseClicked(evt);
            }
        });

        btnOpenTable27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable27MouseClicked(evt);
            }
        });

        btnOpenTable28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable28MouseClicked(evt);
            }
        });

        btnOpenTable29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable29MouseClicked(evt);
            }
        });

        btnOpenTable30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable30MouseClicked(evt);
            }
        });

        btnOpenTable31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable31MouseClicked(evt);
            }
        });

        btnOpenTable32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable32MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelOpenTableLayout = new javax.swing.GroupLayout(panelOpenTable);
        panelOpenTable.setLayout(panelOpenTableLayout);
        panelOpenTableLayout.setHorizontalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable25, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable26, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable27, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable28, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable29, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable30, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable31, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable32, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable18, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable20, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable22, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable24, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelOpenTableLayout.createSequentialGroup()
                                .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelOpenTableLayout.createSequentialGroup()
                                .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelOpenTableLayout.createSequentialGroup()
                                .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelOpenTableLayout.createSequentialGroup()
                                .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelOpenTableLayout.setVerticalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panelOpenTableLayout.createSequentialGroup()
                            .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOpenTable17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable18, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable20, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable22, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable24, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOpenTable25, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable26, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable27, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable28, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable29, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable30, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable31, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenTable32, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious.setText("<<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext.setText(">>>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnUnlockTable.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUnlockTable.setForeground(new java.awt.Color(255, 255, 255));
        btnUnlockTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnUnlockTable.setText("Unlock");
        btnUnlockTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUnlockTable.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnUnlockTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnlockTableActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Unlock Tables");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addComponent(btnUnlockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(314, 314, 314)
                        .addComponent(lblAllTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(322, 322, 322))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAllTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUnlockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable2MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable2.getText(), 1);
        funSelectOpenTable(btnOpenTable2.getText(), 1);
    }//GEN-LAST:event_btnOpenTable2MouseClicked

    private void btnOpenTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable1MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable1.getText(), 0);
        funSelectOpenTable(btnOpenTable1.getText(), 0);
    }//GEN-LAST:event_btnOpenTable1MouseClicked

    private void btnOpenTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable3MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable3.getText(), 2);
        funSelectOpenTable(btnOpenTable3.getText(), 2);
    }//GEN-LAST:event_btnOpenTable3MouseClicked

    private void btnOpenTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable4MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable4.getText(), 3);
        funSelectOpenTable(btnOpenTable4.getText(), 3);
    }//GEN-LAST:event_btnOpenTable4MouseClicked

    private void btnOpenTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable5MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable5.getText(), 4);
        funSelectOpenTable(btnOpenTable5.getText(), 4);
    }//GEN-LAST:event_btnOpenTable5MouseClicked

    private void btnOpenTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable6MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable6.getText(), 5);
        funSelectOpenTable(btnOpenTable6.getText(), 5);
    }//GEN-LAST:event_btnOpenTable6MouseClicked

    private void btnOpenTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable7MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable7.getText(), 6);
        funSelectOpenTable(btnOpenTable7.getText(), 6);
    }//GEN-LAST:event_btnOpenTable7MouseClicked

    private void btnOpenTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable8MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable8.getText(), 7);
        funSelectOpenTable(btnOpenTable8.getText(), 7);
    }//GEN-LAST:event_btnOpenTable8MouseClicked

    private void btnOpenTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable9MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable9.getText(), 8);
        funSelectOpenTable(btnOpenTable9.getText(), 8);
    }//GEN-LAST:event_btnOpenTable9MouseClicked

    private void btnOpenTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable10MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable10.getText(), 9);
        funSelectOpenTable(btnOpenTable10.getText(), 9);
    }//GEN-LAST:event_btnOpenTable10MouseClicked

    private void btnOpenTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable11MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(), 10);
        funSelectOpenTable(btnOpenTable11.getText(), 10);
    }//GEN-LAST:event_btnOpenTable11MouseClicked

    private void btnOpenTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable12MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable12.getText(), 11);
        funSelectOpenTable(btnOpenTable12.getText(), 11);
    }//GEN-LAST:event_btnOpenTable12MouseClicked

    private void btnOpenTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable13MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable13.getText(), 12);
        funSelectOpenTable(btnOpenTable13.getText(), 12);
    }//GEN-LAST:event_btnOpenTable13MouseClicked

    private void btnOpenTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable14MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable14.getText(), 13);
        funSelectOpenTable(btnOpenTable14.getText(), 13);
    }//GEN-LAST:event_btnOpenTable14MouseClicked

    private void btnOpenTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable15MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable15.getText(), 14);
        funSelectOpenTable(btnOpenTable15.getText(), 14);
    }//GEN-LAST:event_btnOpenTable15MouseClicked

    private void btnOpenTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable16MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable16.getText(), 15);
        funSelectOpenTable(btnOpenTable16.getText(), 15);
    }//GEN-LAST:event_btnOpenTable16MouseClicked

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate--;
            if (cntNavigate == 0)
            {
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(true);
                funLoadOpenTables(0, vOpenTableNo.size());
            }
            else
            {
                int tableSize = cntNavigate * 32;
                int resMod = vOpenTableNo.size() % tableSize;
                int resDiv = vOpenTableNo.size() / tableSize;
                int totalSize = tableSize + 32;
                //System.out.println("Size="+vOpenTableNo.size()+"\tMod="+resMod+"\tdiv="+resDiv+"\tsss="+tableSize);
                funLoadOpenTables(tableSize, totalSize);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate++;
            int tableSize = cntNavigate * 32;
            int resMod = vOpenTableNo.size() % tableSize;
            int resDiv = vOpenTableNo.size() / tableSize;
            int totalSize = tableSize + 32;
            funLoadOpenTables(tableSize, totalSize);
            btnPrevious.setEnabled(true);
            if (totalSize >= vOpenTableNo.size())
            {
                btnNext.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnUnlockTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnlockTableActionPerformed
        // TODO add your handling code here:
        funSaveButtonClicked();
    }//GEN-LAST:event_btnUnlockTableActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Unlock Table");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Unlock Table");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Unlock Table");
    }//GEN-LAST:event_formWindowClosing

    private void btnOpenTable17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable17MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable17.getText(), 16);
        funSelectOpenTable(btnOpenTable17.getText(), 16);
    }//GEN-LAST:event_btnOpenTable17MouseClicked

    private void btnOpenTable18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable18MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable18.getText(), 17);
        funSelectOpenTable(btnOpenTable18.getText(), 17);
    }//GEN-LAST:event_btnOpenTable18MouseClicked

    private void btnOpenTable19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable19MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable19.getText(), 18);
        funSelectOpenTable(btnOpenTable19.getText(), 18);
    }//GEN-LAST:event_btnOpenTable19MouseClicked

    private void btnOpenTable20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable20MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable20.getText(), 19);
        funSelectOpenTable(btnOpenTable20.getText(), 19);
    }//GEN-LAST:event_btnOpenTable20MouseClicked

    private void btnOpenTable21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable21MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable21.getText(), 20);
        funSelectOpenTable(btnOpenTable21.getText(), 20);
    }//GEN-LAST:event_btnOpenTable21MouseClicked

    private void btnOpenTable22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable22MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable22.getText(), 21);
        funSelectOpenTable(btnOpenTable22.getText(), 21);
    }//GEN-LAST:event_btnOpenTable22MouseClicked

    private void btnOpenTable23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable23MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable23.getText(), 22);
        funSelectOpenTable(btnOpenTable23.getText(), 22);
    }//GEN-LAST:event_btnOpenTable23MouseClicked

    private void btnOpenTable24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable24MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable24.getText(), 23);
        funSelectOpenTable(btnOpenTable24.getText(), 23);
    }//GEN-LAST:event_btnOpenTable24MouseClicked

    private void btnOpenTable25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable25MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable25.getText(), 24);
        funSelectOpenTable(btnOpenTable25.getText(), 24);
    }//GEN-LAST:event_btnOpenTable25MouseClicked

    private void btnOpenTable26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable26MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable26.getText(), 25);
        funSelectOpenTable(btnOpenTable26.getText(), 25);
    }//GEN-LAST:event_btnOpenTable26MouseClicked

    private void btnOpenTable27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable27MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable27.getText(), 26);
        funSelectOpenTable(btnOpenTable27.getText(), 26);
    }//GEN-LAST:event_btnOpenTable27MouseClicked

    private void btnOpenTable28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable28MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable28.getText(), 27);
        funSelectOpenTable(btnOpenTable28.getText(), 27);
    }//GEN-LAST:event_btnOpenTable28MouseClicked

    private void btnOpenTable29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable29MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable29.getText(), 28);
        funSelectOpenTable(btnOpenTable29.getText(), 28);
    }//GEN-LAST:event_btnOpenTable29MouseClicked

    private void btnOpenTable30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable30MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable30.getText(), 29);
        funSelectOpenTable(btnOpenTable30.getText(), 29);
    }//GEN-LAST:event_btnOpenTable30MouseClicked

    private void btnOpenTable31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable31MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable31.getText(), 30);
        funSelectOpenTable(btnOpenTable31.getText(), 30);
    }//GEN-LAST:event_btnOpenTable31MouseClicked

    private void btnOpenTable32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable32MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable32.getText(), 31);
        funSelectOpenTable(btnOpenTable32.getText(), 31);
    }//GEN-LAST:event_btnOpenTable32MouseClicked

    private void btnOpenTable12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenTable12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOpenTable12ActionPerformed

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
            java.util.logging.Logger.getLogger(frmUnlockTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmUnlockTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmUnlockTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmUnlockTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmUnlockTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnOpenTable1;
    private javax.swing.JButton btnOpenTable10;
    private javax.swing.JButton btnOpenTable11;
    private javax.swing.JButton btnOpenTable12;
    private javax.swing.JButton btnOpenTable13;
    private javax.swing.JButton btnOpenTable14;
    private javax.swing.JButton btnOpenTable15;
    private javax.swing.JButton btnOpenTable16;
    private javax.swing.JButton btnOpenTable17;
    private javax.swing.JButton btnOpenTable18;
    private javax.swing.JButton btnOpenTable19;
    private javax.swing.JButton btnOpenTable2;
    private javax.swing.JButton btnOpenTable20;
    private javax.swing.JButton btnOpenTable21;
    private javax.swing.JButton btnOpenTable22;
    private javax.swing.JButton btnOpenTable23;
    private javax.swing.JButton btnOpenTable24;
    private javax.swing.JButton btnOpenTable25;
    private javax.swing.JButton btnOpenTable26;
    private javax.swing.JButton btnOpenTable27;
    private javax.swing.JButton btnOpenTable28;
    private javax.swing.JButton btnOpenTable29;
    private javax.swing.JButton btnOpenTable3;
    private javax.swing.JButton btnOpenTable30;
    private javax.swing.JButton btnOpenTable31;
    private javax.swing.JButton btnOpenTable32;
    private javax.swing.JButton btnOpenTable4;
    private javax.swing.JButton btnOpenTable5;
    private javax.swing.JButton btnOpenTable6;
    private javax.swing.JButton btnOpenTable7;
    private javax.swing.JButton btnOpenTable8;
    private javax.swing.JButton btnOpenTable9;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUnlockTable;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAllTableName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTableName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    // End of variables declaration//GEN-END:variables

}
