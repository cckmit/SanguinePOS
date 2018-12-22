/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmVoidStock extends javax.swing.JFrame {

    ResultSet rs,rs1, recordset;
    String insertQuery;
    private String StockCode;
    private String StockDate;
    private String userCreated;
    String Tax, SubTotal;
    BigDecimal totalAmt;
    String voidStockDate,dte;
    private String ReasonName;
    String[] reason;
    private String selectQuery;
    private String reasoncode;
    
    String userCode = clsGlobalVarClass.gUserCode;
    String sqlQuery="select strAuditing from tbluserdtl where strUserCode='"+userCode+"' and strFormName='VoidStock'";
    
    public frmVoidStock()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        lblTaxValue.setText("0.00");
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        
        try
        {
          cmbTransactonType.addItem("Stock In"); 
          cmbTransactonType.addItem("Stock Out"); 
          cmbTransactonType.addItem("PS Posting"); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funFillGridStkInStkOut(String sql)
    {
        try
        {
            double subTotalAmt=0.00;
            double TotalAmt=0.00;
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column) 
                {
                    //all cells false
                    return false;
                }
            };
            dm.getDataVector().removeAllElements();
            dm.addColumn("Description");
            dm.addColumn("Qty");
            dm.addColumn("Amount");
            
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String ItemName = rs.getString(1);
                String dblQuantity = rs.getString(2);
                String dblAmount = rs.getString(3);
                subTotalAmt=subTotalAmt+ Double.parseDouble(dblAmount);
                Object[] row={ItemName,dblQuantity,dblAmount};
                dm.addRow(row);
            }
            selectQuery="select count(*) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
            rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            rs.next();
            int Count=rs.getInt(1);
            if(Count>0)
            {
                selectQuery="select sum(dblTaxAmt) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                rs.next();
                Tax=rs.getString(1);
                lblTaxValue.setText(Tax);
                TotalAmt=Double.parseDouble(Tax)+subTotalAmt;
            }   
            else
            {
                TotalAmt=subTotalAmt;
            }
            lblSubTotalValue.setText(Double.toString(subTotalAmt));
            
            lblTotalAmt.setText(Double.toString(TotalAmt));
            tblItemTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.setShowHorizontalLines(true);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(210);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void fillGridPSP(String sql)
    {
        try
        {
            double TotalcompStk=0.00;
            double TotalPhyStk=0.00;
            double TotalVariance=0.00;
            double TotalVarAmt=0.00;
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column) 
                {
                    //all cells false
                    return false;
                }
            };
                dm.getDataVector().removeAllElements();            
                dm.addColumn("Description");
                dm.addColumn("Comp Stk");
                dm.addColumn("Phy Stk");
                dm.addColumn("Variance");
                dm.addColumn("Var Amt");
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rs.next())
                {
                    String ItemName = rs.getString(1);
                    String CompStk = rs.getString(2);
                    String PhyStk = rs.getString(3);
                    String Variance = rs.getString(4);
                    String VarAmt = rs.getString(5);
                    TotalcompStk=TotalcompStk+ Double.parseDouble(CompStk);
                    TotalPhyStk=TotalPhyStk+Double.parseDouble(PhyStk);
                    TotalVariance=TotalVariance+Double.parseDouble(Variance);
                    TotalVarAmt=TotalVarAmt+Double.parseDouble(VarAmt);
                    Object[] row={ItemName,CompStk,PhyStk,Variance,VarAmt};
                    dm.addRow(row);
                }
                selectQuery="select count(*) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                rs.next();
                int Count=rs.getInt(1);
                if(Count>0)
                {
                    selectQuery="select sum(dblTaxAmt) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                    rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    rs.next();
                    Tax=rs.getString(1);
                    lblTaxValue.setText(Tax);
                }        
//                lblSubTotalValue.setText(Double.toString(subTotalAmt));
//                TotalAmt=Double.parseDouble(Tax)+subTotalAmt;
//                lblTotalAmt.setText(Double.toString(TotalAmt));
                tblItemTable.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tblItemTable.setShowHorizontalLines(true);
                tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(110);
                tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(73);
                tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(73);
                tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(63);
                tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(63);
        }
        catch(Exception e)
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
        panelStockItemDtl = new javax.swing.JPanel();
        scrItemDtl = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        lblPaxNo = new javax.swing.JLabel();
        btnUp = new javax.swing.JButton();
        btnUp1 = new javax.swing.JButton();
        lblDateTime = new javax.swing.JLabel();
        lblStockDateTime = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        lblStockCode = new javax.swing.JLabel();
        lblSubTotalTitle = new javax.swing.JLabel();
        lblSubTotalValue = new javax.swing.JLabel();
        lblTaxTitle = new javax.swing.JLabel();
        lblTaxValue = new javax.swing.JLabel();
        lblUserCreated = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnVoidBill = new javax.swing.JButton();
        panelStockList = new javax.swing.JPanel();
        scrStockList = new javax.swing.JScrollPane();
        tblStkVoidList = new javax.swing.JTable();
        lblTransType = new javax.swing.JLabel();
        cmbTransactonType = new javax.swing.JComboBox();
        btnClose = new javax.swing.JButton();

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
        panelHeader.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Void Stock");
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

        panelStockItemDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelStockItemDtl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelStockItemDtl.setForeground(new java.awt.Color(254, 184, 80));
        panelStockItemDtl.setOpaque(false);
        panelStockItemDtl.setPreferredSize(new java.awt.Dimension(260, 600));
        panelStockItemDtl.setLayout(null);

        tblItemTable.setBackground(new java.awt.Color(51, 102, 255));
        tblItemTable.setForeground(new java.awt.Color(255, 255, 255));
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblItemTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblItemTable.setRowHeight(30);
        tblItemTable.setShowVerticalLines(false);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrItemDtl.setViewportView(tblItemTable);

        panelStockItemDtl.add(scrItemDtl);
        scrItemDtl.setBounds(0, 60, 360, 340);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");
        panelStockItemDtl.add(lblTotal);
        lblTotal.setBounds(190, 470, 60, 30);
        panelStockItemDtl.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPDark.png"))); // NOI18N
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPLite.png"))); // NOI18N
        btnUp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUpMouseClicked(evt);
            }
        });
        btnUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpActionPerformed(evt);
            }
        });
        panelStockItemDtl.add(btnUp);
        btnUp.setBounds(10, 500, 90, 40);

        btnUp1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp1.setForeground(new java.awt.Color(255, 255, 255));
        btnUp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownDark.png"))); // NOI18N
        btnUp1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownLite.png"))); // NOI18N
        btnUp1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUp1MouseClicked(evt);
            }
        });
        panelStockItemDtl.add(btnUp1);
        btnUp1.setBounds(120, 500, 90, 40);
        panelStockItemDtl.add(lblDateTime);
        lblDateTime.setBounds(100, 30, 160, 30);

        lblStockDateTime.setText("Date & Time");
        panelStockItemDtl.add(lblStockDateTime);
        lblStockDateTime.setBounds(10, 30, 90, 30);

        lblStock.setText("Stock In Code");
        panelStockItemDtl.add(lblStock);
        lblStock.setBounds(10, 0, 90, 30);
        panelStockItemDtl.add(lblStockCode);
        lblStockCode.setBounds(100, 0, 80, 30);

        lblSubTotalTitle.setText("Sub Total ");
        panelStockItemDtl.add(lblSubTotalTitle);
        lblSubTotalTitle.setBounds(190, 410, 70, 20);

        lblSubTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelStockItemDtl.add(lblSubTotalValue);
        lblSubTotalValue.setBounds(270, 410, 90, 20);

        lblTaxTitle.setText("Tax");
        panelStockItemDtl.add(lblTaxTitle);
        lblTaxTitle.setBounds(190, 440, 60, 20);

        lblTaxValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelStockItemDtl.add(lblTaxValue);
        lblTaxValue.setBounds(270, 440, 90, 20);
        panelStockItemDtl.add(lblUserCreated);
        lblUserCreated.setBounds(260, 0, 80, 30);

        lblTotalAmt.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalAmt.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelStockItemDtl.add(lblTotalAmt);
        lblTotalAmt.setBounds(270, 470, 90, 30);

        lblUser.setText("User Created");
        panelStockItemDtl.add(lblUser);
        lblUser.setBounds(180, 0, 80, 30);

        btnVoidBill.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVoidBill.setForeground(new java.awt.Color(255, 255, 255));
        btnVoidBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnVoidBill.setText("VOID");
        btnVoidBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVoidBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnVoidBill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnVoidBillActionPerformed(evt);
            }
        });
        panelStockItemDtl.add(btnVoidBill);
        btnVoidBill.setBounds(270, 500, 90, 40);

        panelStockList.setBackground(new java.awt.Color(255, 255, 255));
        panelStockList.setOpaque(false);

        tblStkVoidList.setBackground(new java.awt.Color(254, 254, 254));
        tblStkVoidList.setForeground(new java.awt.Color(1, 1, 1));
        tblStkVoidList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "", "", "", ""
            }
        ));
        tblStkVoidList.setRowHeight(25);
        tblStkVoidList.setSelectionBackground(new java.awt.Color(0, 120, 255));
        tblStkVoidList.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblStkVoidList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblStkVoidListMouseClicked(evt);
            }
        });
        scrStockList.setViewportView(tblStkVoidList);

        lblTransType.setText("Transaction Type");

        cmbTransactonType.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTransactonType.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbTransactonTypeItemStateChanged(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelStockListLayout = new javax.swing.GroupLayout(panelStockList);
        panelStockList.setLayout(panelStockListLayout);
        panelStockListLayout.setHorizontalGroup(
            panelStockListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStockListLayout.createSequentialGroup()
                .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbTransactonType, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scrStockList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panelStockListLayout.setVerticalGroup(
            panelStockListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStockListLayout.createSequentialGroup()
                .addGroup(panelStockListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTransactonType, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrStockList, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelStockItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelStockList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelStockList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(panelStockItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked

    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
        // TODO add your handling code here:
        if (tblItemTable.getModel().getRowCount() > 0)
        {
            int r = tblItemTable.getSelectedRow();
            tblItemTable.changeSelection(r - 1, 0, false, false);
        }
        else
        {
            new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
        }
    }//GEN-LAST:event_btnUpMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnUp1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUp1MouseClicked
        // TODO add your handling code here:
        if (tblItemTable.getModel().getRowCount() > 0)
        {
            int r = tblItemTable.getSelectedRow();
            int rowcount = tblItemTable.getRowCount();
            if (r < rowcount)
            {
                tblItemTable.changeSelection(r + 1, 0, false, false);
            }
            else if (r == rowcount)
            {
                r = 0;
                tblItemTable.changeSelection(r, 0, false, false);
            }
        }
        else
        {
            new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
        }
    }//GEN-LAST:event_btnUp1MouseClicked

    private void btnVoidBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoidBillActionPerformed
        // TODO add your handling code here:
        try
        {
            String favoritereason=null;
            String voidResaonCode=null,transType=null;
            if(tblItemTable.getRowCount()>0)
            {
                if(StockCode.contains("SI"))
                {
                    favoritereason=getStockReasonStock("strVoidStkIn");
                    transType="VSI";
                    if (tblItemTable.getModel().getRowCount() > 0)
                    {
                        if(favoritereason!=null)
                        {
                            selectQuery="select strReasonCode from tblreasonmaster "
                            + "where strReasonName='"+favoritereason+"'";
                            rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                            while(rs.next())
                            {
                                reasoncode=rs.getString(1);
                            }
                            voidResaonCode=reasoncode;
                            int ch = JOptionPane.showConfirmDialog(this, "Do you want to Void Stock ?", "Void Stock", JOptionPane.YES_NO_OPTION);
                            if (ch == JOptionPane.YES_OPTION)
                            {
                                funVoidStockIn(voidResaonCode,transType);
                            }
                        }
                    }

                }
                if(StockCode.contains("SO"))
                {
                    favoritereason=getStockReasonStock("strVoidStkOut");
                    transType="VSO";
                    if (tblItemTable.getModel().getRowCount() > 0)
                    {
                        if(favoritereason!=null)
                        {
                            selectQuery="select strReasonCode from tblreasonmaster "
                            + "where strReasonName='"+favoritereason+"'";
                            rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                            while(rs.next())
                            {
                                reasoncode=rs.getString(1);
                            }
                            voidResaonCode=reasoncode;
                            int ch = JOptionPane.showConfirmDialog(this, "Do you want to Void Stock ?", "Void Stock", JOptionPane.YES_NO_OPTION);
                            if (ch == JOptionPane.YES_OPTION)
                            {
                                funVoidStockOut(voidResaonCode,transType);
                            }
                        }
                    }
                }
                if(StockCode.contains("PS"))
                {
                    favoritereason=getStockReasonStock("strPSP");
                    transType="VPS";
                    if(favoritereason!=null)
                    {
                        selectQuery="select strReasonCode from tblreasonmaster "
                        + "where strReasonName='"+favoritereason+"'";
                        rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                        while(rs.next())
                        {
                            reasoncode=rs.getString(1);
                        }
                        voidResaonCode=reasoncode;
                        int ch = JOptionPane.showConfirmDialog(this, "Do you want to Void Stock ?", "Void Stock", JOptionPane.YES_NO_OPTION);
                        if (ch == JOptionPane.YES_OPTION)
                        {
                            funVoidPSPStock(voidResaonCode,transType);
                        }
                    }
                }
            }
            else
            {
                new frmOkPopUp(this, "Please select Item","Warning", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnVoidBillActionPerformed

    private void tblStkVoidListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStkVoidListMouseClicked
        // TODO add your handling code here:
        try
        {
            int selectedRow = tblStkVoidList.getSelectedRow();
            StockCode = tblStkVoidList.getModel().getValueAt(selectedRow, 0).toString();

            if(StockCode.contains("SI"))
            {
                selectQuery="select b.strItemName,a.dblQuantity,a.dblAmount from tblstkindtl a,tblitemmaster b where strStkInCode='" + StockCode + "' and a.strItemCode=b.strItemCode";
                lblStock.setText("Stock In Code");
                lblStockCode.setText(StockCode);
                selectedRow = tblStkVoidList.getSelectedRow();
                lblUserCreated.setText(tblStkVoidList.getModel().getValueAt(selectedRow, 3).toString());
                lblDateTime.setText(tblStkVoidList.getModel().getValueAt(selectedRow, 1).toString());
                funFillGridStkInStkOut(selectQuery);

            }
            if(StockCode.contains("SO"))
            {
                selectQuery="select b.strItemName,a.dblQuantity,a.dblAmount from tblstkoutdtl a,tblitemmaster b where strStkOutCode='" + StockCode + "' and a.strItemCode=b.strItemCode";
                lblStock.setText("Stock Out Code");
                lblStockCode.setText(StockCode);
                selectedRow = tblStkVoidList.getSelectedRow();
                lblUserCreated.setText(tblStkVoidList.getModel().getValueAt(selectedRow, 3).toString());
                lblDateTime.setText(tblStkVoidList.getModel().getValueAt(selectedRow, 1).toString());
                funFillGridStkInStkOut(selectQuery);
            }
            if(StockCode.contains("PS"))
            {
                selectQuery="select b.strItemName,a.dblCompStk,a.dblPhyStk,a.dblVariance,a.dblVairanceAmt from tblpspdtl a,tblitemmaster b where strPSPCode='" + StockCode + "' and a.strItemCode=b.strItemCode";
                lblStock.setText("PSP Code");
                lblStockCode.setText(StockCode);
                fillGridPSP(selectQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblStkVoidListMouseClicked

    private void cmbTransactonTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTransactonTypeItemStateChanged
        if(cmbTransactonType.getSelectedItem().equals("Stock In"))
        {
            funFillStockInTable();
        }
        if(cmbTransactonType.getSelectedItem().equals("Stock Out"))
        {
            funFillStockOutTable();
        }
        if(cmbTransactonType.getSelectedItem().equals("PS Posting"))
        {
            funFillStockPSPTable();
        }
    }//GEN-LAST:event_cmbTransactonTypeItemStateChanged

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("VoidStock");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("VoidStock");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("VoidStock");
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
            java.util.logging.Logger.getLogger(frmVoidStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVoidStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVoidStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVoidStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVoidStock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnUp;
    private javax.swing.JButton btnUp1;
    private javax.swing.JButton btnVoidBill;
    private javax.swing.JComboBox cmbTransactonType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblStock;
    public javax.swing.JLabel lblStockCode;
    private javax.swing.JLabel lblStockDateTime;
    private javax.swing.JLabel lblSubTotalTitle;
    private javax.swing.JLabel lblSubTotalValue;
    private javax.swing.JLabel lblTaxTitle;
    private javax.swing.JLabel lblTaxValue;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblTransType;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserCreated;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelStockItemDtl;
    private javax.swing.JPanel panelStockList;
    private javax.swing.JScrollPane scrItemDtl;
    private javax.swing.JScrollPane scrStockList;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblStkVoidList;
    // End of variables declaration//GEN-END:variables

    private void funResetField() {
        tblItemTable.setModel(new DefaultTableModel());
        lblStockCode.setText("");
        lblUserCreated.setText("");
        lblDateTime.setText("");
        lblSubTotalValue.setText("");
        lblTaxValue.setText("");
        lblTotalAmt.setText("");
        panelHeader.setVisible(true);
    }

    private void funFillStockInTable() {
        try {
            DefaultTableModel dm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };

            dm.addColumn("Stock In Code");
            dm.addColumn("Stock In  Date");
            dm.addColumn("Reason Name");
            dm.addColumn("User Created");
            selectQuery="select a.strStkInCode, Date(a.dteStkInDate),a.strUserCreated,b.strReasonName "
                + "from tblstkinhd a, tblreasonmaster b "
                + "where a.strPosCode='"+clsGlobalVarClass.gPOSCode+"'"
                + " and a.strReasonCode=b.strReasonCode and b.strStkIn='Y' "
                + "ORDER BY strStkInCode ASC";            
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);

            while (rs.next()) 
            {
                StockCode = rs.getString(1);
                StockDate = rs.getString(2);
                userCreated = rs.getString(3);
                ReasonName=rs.getString(4);
                Object[] rows = {StockCode, StockDate, ReasonName,userCreated};
                dm.addRow(rows);
                
            }
                
                tblStkVoidList.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tblStkVoidList.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
                tblStkVoidList.getColumnModel().getColumn(0).setPreferredWidth(80);
                tblStkVoidList.getColumnModel().getColumn(1).setPreferredWidth(110);
                tblStkVoidList.getColumnModel().getColumn(2).setPreferredWidth(80);
                tblStkVoidList.getColumnModel().getColumn(3).setPreferredWidth(70);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void funFillStockOutTable()
    {
        try {
            DefaultTableModel dm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            dm.getDataVector().removeAllElements();
            dm.addColumn("Stock Out Code");
            dm.addColumn("Stock Out  Date");
            dm.addColumn("Reason Name");
            dm.addColumn("User Created");
            selectQuery="select a.strStkOutCode, Date(a.dteStkOutDate),a.strUserCreated,b.strReasonName "
                + "from tblstkouthd a, tblreasonmaster b "
                + "where a.strPosCode='"+clsGlobalVarClass.gPOSCode+"' and a.strReasonCode=b.strReasonCode "
                + "ORDER BY strStkOutCode ASC";
            System.out.println(selectQuery);
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);

            while (rs.next()) 
            {
                StockCode = rs.getString(1);
                StockDate = rs.getString(2);
                userCreated = rs.getString(3);
                ReasonName=rs.getString(4);
                Object[] rows = {StockCode, StockDate, ReasonName,userCreated};
                dm.addRow(rows);                
            }
            
            tblStkVoidList.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblStkVoidList.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblStkVoidList.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblStkVoidList.getColumnModel().getColumn(1).setPreferredWidth(110);
            tblStkVoidList.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblStkVoidList.getColumnModel().getColumn(3).setPreferredWidth(70);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
    }

    private void funFillStockPSPTable() 
    {
        try {
            DefaultTableModel dm = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
            };
            dm.getDataVector().removeAllElements();
            dm.addColumn("PSP Code");
            dm.addColumn("Stock In Code");
            dm.addColumn("Stock Out Code");
            dm.addColumn("Stock In Amt");
            dm.addColumn("Sale Amt");
            selectQuery="select a.strPSPCode,a.strStkInCode,a.strStkOutCode,a.strBillNo,a.dblStkInAmt,a.dblSaleAmt "
                    + "from tblpsphd a where a.strPosCode='"+clsGlobalVarClass.gPOSCode+"' ORDER BY strPSPCode ASC";
            System.out.println(selectQuery);
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);

            while (rs.next()) 
            {
                StockCode = rs.getString(1);
                String StockInCode=rs.getString(2);
                String StockOutCode=rs.getString(3);        
                String BillNo=rs.getString(4);
                String StkAmt=rs.getString(5);
                String SalesAmt=rs.getString(6);
                Object[] rows = {StockCode, StockInCode, StockOutCode,BillNo,StkAmt,SalesAmt};
                dm.addRow(rows);                
            }
            tblStkVoidList.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblStkVoidList.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblStkVoidList.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblStkVoidList.getColumnModel().getColumn(1).setPreferredWidth(110);
            tblStkVoidList.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblStkVoidList.getColumnModel().getColumn(3).setPreferredWidth(70);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    private String getStockReasonStock(String fieldName)
    {
        String favoritereason=null;
        try
        {
                int reasoncount=0; 
                int i=0;
                selectQuery="select count(strReasonName) from tblreasonmaster where "+fieldName+"='Y'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                while(rs.next())
                {
                    reasoncount=rs.getInt(1);
                }
                
                if(reasoncount>0)
                {
                reason=new String[reasoncount];
                selectQuery="select strReasonName from tblreasonmaster where "+fieldName+"='Y'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                i=0;
                while(rs.next())
                {
                    reason[i]=rs.getString(1);
                    i++;
                }
                    favoritereason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?","Reason",JOptionPane.INFORMATION_MESSAGE,null,reason,reason[0]);
                }
                else
                {
                    new frmOkPopUp(this, "Please create Reason", "Warning", 1).setVisible(true);
                }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        return favoritereason;
        }
    }
    
    private void funVoidStockIn(String voidResaonCode,String transType)
    {
        try
        {
             
            int exce=0,del=0;
            voidStockDate=funGetVodidedDate();
            StockCode =lblStockCode.getText();
            selectQuery="select strStkInCode,strPOSCode,dteStkInDate,strReasonCode,strPurchaseBillNo,dtePurchaseBillDate,strUserCreated from tblstkinhd where strStkInCode='"+StockCode+"'";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            rs.next();
            insertQuery="insert into tblvoidstockhd(strStockCode,strPOSCode,dteStkDate,strReasonCode,"
                + "strPurchaseBillNo,dtePurchaseBillDate,dteVoidedDate,strTransType,strVoidReasonCode,strUserCreated)"
                + " values('"+rs.getString("strStkInCode")+"','"+rs.getString("strPOSCode")+"','"+rs.getString("dteStkInDate")+"',"
                + "'"+rs.getString("strReasonCode")+"','"+rs.getString("strPurchaseBillNo")+"','"+rs.getString("dtePurchaseBillDate")+"','"+voidStockDate+"','"+transType+"','"+voidResaonCode+"','"+rs.getString("strUserCreated")+"')";
            rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            int exc=0;
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            exc=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
            
            if(exc>0)
            {
                selectQuery="select strItemCode,dblQuantity,dblPurchaseRate,dblAmount from tblstkindtl where strStkInCode='"+StockCode+"'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                while (rs.next())
                {
                    String ItemCode = rs.getString("strItemCode");
                    String Qty = rs.getString("dblQuantity");
                    String dblPurchaseRate=rs.getString("dblPurchaseRate");
                    String amount = rs.getString("dblAmount");
                    insertQuery="insert into tblvoidstockdtl(strStockCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount)"
                        + " values('"+StockCode+"','"+ItemCode+"','"+Qty+"','"+dblPurchaseRate+"','"+amount+"')";
                   rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            exce=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
                }
                if(exce>0)
                {
                    selectQuery="select Count(*) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                    rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    rs.next();
                    int rowcnt=rs.getInt(1);
                    if(rowcnt>0)
                    {
                        selectQuery="select strTransactionId,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                        rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                        while(rs.next())
                        {
                            insertQuery="insert into tblvoidstocktaxdtl(strTransactionId,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode)"
                                + " values('"+rs.getString("strTransactionId") +"','"+rs.getString("strTaxCode") +"','"+rs.getString("dblTaxableAmt") +"','"+rs.getString("dblTaxAmt") +"','"+rs.getString("strClientCode") +"')";
                    rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            del=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
                        }
                    }
                    else
                    {
                        del=1;
                    }
                }
                if(del>0)
                {
                    clsGlobalVarClass.dbMysql.execute("Delete from tblstkinhd where strStkInCode='" + StockCode + "'");
                    clsGlobalVarClass.dbMysql.execute("Delete from tblstkindtl where strStkInCode='" + StockCode + "'");
                    funResetField();
                    funFillStockInTable();
                }                       
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
                   
        }
        
    }
    private void funVoidStockOut(String voidResaonCode,String transType)
    {
        try
        {             
            int exce=0,del=0;
            voidStockDate=funGetVodidedDate();
            StockCode =lblStockCode.getText();
            selectQuery="select strStkOutCode,strPOSCode,dteStkOutDate,strReasonCode,strPurchaseBillNo,dtePurchaseBillDate,strUserCreated from tblstkouthd where strStkOutCode='"+StockCode+"'";
            System.out.println("stock out"+selectQuery);
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            rs.next();
            insertQuery="insert into tblvoidstockhd(strStockCode,strPOSCode,dteStkDate,strReasonCode,"
                + "strPurchaseBillNo,dtePurchaseBillDate,dteVoidedDate,strTransType,strVoidReasonCode,strUserCreated)"
                + " values('"+rs.getString("strStkOutCode")+"','"+rs.getString("strPOSCode")+"','"+rs.getString("dteStkOutDate")+"',"
                + "'"+rs.getString("strReasonCode")+"','"+rs.getString("strPurchaseBillNo")+"','"+rs.getString("dtePurchaseBillDate")+"','"+voidStockDate+"','"+transType+"','"+voidResaonCode+"','"+rs.getString("strUserCreated")+"')";
            
            rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            int exc=0;
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            exc=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
            if(exc>0)
            {
                    selectQuery="select strItemCode,dblQuantity,dblPurchaseRate,dblAmount from tblstkoutdtl where strStkOutCode='"+StockCode+"'";
                    rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    while (rs.next())
                    {
                       String ItemCode = rs.getString("strItemCode");               
                       String Qty = rs.getString("dblQuantity");
                       String dblPurchaseRate=rs.getString("dblPurchaseRate");
                       String amount = rs.getString("dblAmount");
                       insertQuery="insert into tblvoidstockdtl(strStockCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount)"
                               + " values('"+StockCode+"','"+ItemCode+"','"+Qty+"','"+dblPurchaseRate+"','"+amount+"')";
                       rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
          
                        if(rs1.next())
                        {
                            if(Boolean.parseBoolean(rs1.getString(1)))
                                exce=clsGlobalVarClass.dbMysql.execute(insertQuery);
                        }
                    }
                    if(exce>0)
                    {
                        selectQuery="select Count(*) from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                        rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                        rs.next();
                        int rowcnt=rs.getInt(1);
                        if(rowcnt>0)
                        {
                            selectQuery="select strTransactionId,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode from tblstocktaxdtl where strTransactionId='"+StockCode+"'";
                            rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                            while(rs.next())
                            {
                                insertQuery="insert into tblvoidstocktaxdtl(strTransactionId,strTaxCode,dblTaxableAmt,dblTaxAmt,strClientCode)"
                                + " values('"+rs.getString("strTransactionId") +"','"+rs.getString("strTaxCode") +"','"+rs.getString("dblTaxableAmt") +"','"+rs.getString("dblTaxAmt") +"','"+rs.getString("strClientCode") +"')";
                               rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
           
                                 if(rs1.next())
                                    {
                                         if(Boolean.parseBoolean(rs1.getString(1)))
                                        del=clsGlobalVarClass.dbMysql.execute(insertQuery);
                                    }
                            }
                        }
                        else
                        {
                          del=1;  
                        }
                    }
                    if(del>0)
                    {
                       clsGlobalVarClass.dbMysql.execute("Delete from tblstkouthd where strStkOutCode='" + StockCode + "'");
                       clsGlobalVarClass.dbMysql.execute("Delete from tblstkoutdtl where strStkOutCode='" + StockCode + "'");
                       funResetField();
                       funFillStockOutTable();
                    }
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
                   
        }
        
    }
    private void funVoidPSPStock(String voidResaonCode,String transType)
    {
        try
        {             
            int exce=0,del=0;
            StockCode =lblStockCode.getText();
            voidStockDate=funGetVodidedDate();
            selectQuery="select strPSPCode,strPOSCode,strStkInCode,strStkOutCode,strBillNo,dblStkInAmt,dblSaleAmt,strUserCreated from tblpsphd where strPSPCode='"+StockCode+"'";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            rs.next();
            insertQuery="insert into tblvoidstockhd(strStockCode,strPOSCode,strStockInCode,strStockOutCode,strBillNo,"
                    + "dblStkInAmt,dblSaleAmt,dteVoidedDate,strTransType,strVoidReasonCode,strUserCreated)"
                    + " values('"+rs.getString("strPSPCode")+"','"+rs.getString("strPOSCode")+"','"+rs.getString("strStkInCode")+"',"
                    + "'"+rs.getString("strStkOutCode")+"','"+rs.getString("strBillNo")+"','"+rs.getString("dblStkInAmt")+"','"+rs.getString("dblSaleAmt")+"',"
                    + "'"+voidStockDate+"','"+transType+"','"+voidResaonCode+"','"+rs.getString("strUserCreated")+"')";
            System.out.println(insertQuery);
            rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            int exc=0;
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            exc=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
            if(exc>0)
            {
                    selectQuery="select strItemCode,dblPhyStk,dblCompStk,dblVariance,dblVairanceAmt from tblpspdtl where strPSPCode='"+StockCode+"'";
                    rs=clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                    while (rs.next())
                    {
                       String ItemCode = rs.getString("strItemCode");               
                       String dblPhyStk = rs.getString("dblPhyStk");
                       String dblCompStk=rs.getString("dblCompStk");
                       String dblVariance = rs.getString("dblVariance");
                       String dblVairanceAmt = rs.getString("dblVairanceAmt");
                       insertQuery="insert into tblvoidstockdtl(strStockCode,strItemCode,dblPhyStk,dblCompStk,dblVariance,dblVairanceAmt)"
                               + " values('"+StockCode+"','"+ItemCode+"','"+dblPhyStk+"','"+dblCompStk+"','"+dblVariance+"','"+dblVairanceAmt+"')";
                       rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
            
                    if(rs1.next())
                    {
                        if(Boolean.parseBoolean(rs1.getString(1)))
                            exce=clsGlobalVarClass.dbMysql.execute(insertQuery);
                    }
                     }
                    if(exce>0)
                    {
                        
                       clsGlobalVarClass.dbMysql.execute("Delete from tblpsphd where strPSPCode='" + StockCode + "'");
                       clsGlobalVarClass.dbMysql.execute("Delete from tblpspdtl where strPSPCode='" + StockCode + "'");
                       funResetField();
                       funFillStockPSPTable();
                    }
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
                   
        }
        
    }
    private String funGetVodidedDate()
    {
       String voidDate=null;
       try
       {
            java.util.Date dt = new java.util.Date();
           
            int day = dt.getDate();
            int month = dt.getMonth() + 1;
            int year = dt.getYear() + 1900;
            int h=dt.getHours();
            int m=dt.getMinutes();
            int s=dt.getSeconds();
            String time=h+":"+m+":"+s;
            String date=year + "-" + month + "-" + day;
            voidDate=date+" "+time;       
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
       finally
       {
            return voidDate;
       }
        
    }
}

    
    
