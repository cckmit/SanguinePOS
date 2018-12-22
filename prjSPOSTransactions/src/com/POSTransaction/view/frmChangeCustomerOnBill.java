/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmChangeCustomerOnBill extends javax.swing.JFrame
{
    private String cmsMemCode;
    private String cmsMemName;
    private String strMemberCode;
    private DefaultTableModel dm;
    clsUtility objUtility;

    public frmChangeCustomerOnBill()
    {
        try
        {
            initComponents();
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String new_str = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
            
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            
            objUtility=new clsUtility();
            if (!clsGlobalVarClass.gCMSIntegrationYN)
            {
                lblMemberCode.setText("Customer Code");
                lblMemberName.setText("Customer Name");
            }
            //funFillGridForDirectBiller();
            funSearchBillNo("");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillGridForDirectBiller()
    {
        try
        {
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
            dm.addColumn("Bill No.");
            dm.addColumn("Bill Date");
            dm.addColumn("Settle Mode");
            dm.addColumn("Amount");
            dm.addColumn("Table");

            String sql = "select a.strBillNo,a.dteBillDate,a.strSettelmentMode,a.dblTaxAmt,a.dblSubTotal,a.dblGrandTotal"
                  + ",a.strUserCreated,b.strTableName "
                  + " from tblbillhd a left outer join tbltablemaster b "
                  + " on a.strTableNo=b.strTableNo "
                  + " where date(a.dteBillDate)='" + objUtility.funGetOnlyPOSDateForTransaction() + "' "
                  + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                  + " and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) "
                  + " and a.strBillNo NOT LIKE '%-%' "
                  + " order by a.strTableNo ";            
            ResultSet rsBillInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsBillInfo.next())
            {
                String settleMode = rsBillInfo.getString(3);
                if ("null".equalsIgnoreCase(rsBillInfo.getString(3)))
                {
                    settleMode = "";
                }
                Object[] rows =
                {
                    rsBillInfo.getString(1), rsBillInfo.getString(2), settleMode, rsBillInfo.getString(6), rsBillInfo.getString(8)
                };
                dm.addRow(rows);
            }
            tableVoidBill.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tableVoidBill.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tableVoidBill.getColumnModel().getColumn(0).setPreferredWidth(70);
            tableVoidBill.getColumnModel().getColumn(1).setPreferredWidth(90);
            tableVoidBill.getColumnModel().getColumn(2).setPreferredWidth(70);
            tableVoidBill.getColumnModel().getColumn(3).setPreferredWidth(50);
            tableVoidBill.getColumnModel().getColumn(4).setPreferredWidth(60);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSelectBill()
    {
        int selectedBill = tableVoidBill.getSelectedRow();
        String billNo = tableVoidBill.getModel().getValueAt(selectedBill, 0).toString();
        lblVoucherNo.setText(billNo);
        funFillItemGrid(billNo);
    }

    private void funFillItemGrid(String billNo)
    {
        try
        {
            dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            dm.addColumn("Description");
            dm.addColumn("Qty");
            dm.addColumn("Amount");
            dm.addColumn("ModCode");
            dm.addColumn("KOT No");
            lblVoucherNo.setText(billNo);
            List<ArrayList<Object>> arrListItemDtls = new ArrayList<ArrayList<Object>>();

            String sql="select strItemName,strBillNo,dblQuantity,dblAmount,dteBillDate,strItemCode,strKOTNo "
                + " from tblbilldtl where strBillNo='" + billNo + "' ;";
            ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsBillDtl.next())
            {
                ArrayList<Object> arrListItemRow = new ArrayList<Object>();
                String itemCode = rsBillDtl.getString(6);
                Object[] rows =
                {
                    rsBillDtl.getString(1), rsBillDtl.getString(3), rsBillDtl.getString(4), rsBillDtl.getString(6), rsBillDtl.getString(7)
                };
                dm.addRow(rows);
                arrListItemRow.add(rsBillDtl.getString(6));
                arrListItemRow.add(rsBillDtl.getString(4));
                arrListItemDtls.add(arrListItemRow);
                sql = "select strModifierName,dblQuantity,dblAmount,strItemCode,strModifierCode from tblbillmodifierdtl "
                      + "where strItemCode='" + itemCode + "' and strBillNo='" + billNo + "' ;";
                ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsModifier.next())
                {
                    String modItemCode = rsModifier.getString(5) + rsModifier.getString(4);
                    Object[] modifier =
                    {
                        rsModifier.getString("strModifierName"), rsModifier.getString("dblQuantity"), rsModifier.getString("dblAmount"), modItemCode, ""
                    };
                    dm.addRow(modifier);
                }
                rsModifier.close();
            }
            rsBillDtl.close();
            
            double discountAmt = 0;
            double discountPer = 0;
            double subTotal = 0;
            double grandTotal = 0;
            double taxAmt = 0;
            String userCreated="";
            
            tblItemTable.setModel(dm);
            if(!clsGlobalVarClass.gShowItemDetailsGrid)
            {
               dm.setRowCount(0);
            }
             
            String sqlBillHd = "select a.dblTaxAmt,a.dblSubTotal,a.dblGrandTotal,a.strUserCreated ,a.dblDiscountAmt"
                + ",a.dblDiscountPer ,ifnull(b.strCustomerCode,'ND') as strCustomerCode,ifnull(b.strCustomerName,'ND') as strCustomerName "
                + "from tblbillhd a left outer join  tblcustomermaster b on  a.strCustomerCode=b.strCustomerCode "
                + " where a.strBillNo='" + lblVoucherNo.getText() + "'";
            rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillHd);
            while (rsBillDtl.next())
            {
                taxAmt = rsBillDtl.getDouble(2);
                subTotal = rsBillDtl.getDouble(2);
                grandTotal = rsBillDtl.getDouble(3);
                userCreated = rsBillDtl.getString(4);
                discountAmt = Double.parseDouble(rsBillDtl.getString(5));
                discountPer = Double.parseDouble(rsBillDtl.getString(6));

                lblMemberCodeValue.setText(rsBillDtl.getString("strCustomerCode"));
                lblMemberNameValue.setText(rsBillDtl.getString("strCustomerName"));
                strMemberCode = rsBillDtl.getString("strCustomerCode");
            }
            lblUserCreated.setText(userCreated);
            lblSubTotalValue.setText(String.valueOf(subTotal));
            lblTaxValue.setText(String.valueOf(taxAmt));
            lblTotalAmt.setText(String.valueOf(grandTotal));
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.setShowHorizontalLines(true);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(210);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void funSearchBillNo(String searchText)
    {
        try
        {
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };

            dm.addColumn("Bill No.");
            dm.addColumn("Time");
            dm.addColumn("Table Name");
            
            String sql = "select a.strBillNo,TIME_FORMAT(time(a.dteBillDate),'%h:%i') as dteBillDate,ifnull(b.strTableName,''),a.strPOSCode "
                + " from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
                + " where a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' AND(  a.strBillNo Like'%" + searchText + "%' or a.dteBillDate like '%" + searchText + "%' "
                + " or b.strTableName like '%" + searchText + "%') "
                + " and date(dteBillDate)='" + objUtility.funGetOnlyPOSDateForTransaction() + "' "
                + " order by a.strTableNo ";
            ResultSet rsBillInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsBillInfo.next())
            {
                Object[] rows =
                {
                    rsBillInfo.getString(1), rsBillInfo.getString(2), rsBillInfo.getString(3)
                };
                dm.addRow(rows);
                tableVoidBill.setModel(dm);
            }
            rsBillInfo.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    private void funPrintBill() throws Exception
    {
        if (tableVoidBill.getSelectedRow() > -1)
        {
            int row = tableVoidBill.getSelectedRow();
            String selectedBill = tableVoidBill.getValueAt(row, 0).toString();
//            String billDate = tableVoidBill.getValueAt(row, 1).toString();
             String billDate = objUtility.funGetOnlyPOSDateForTransaction();
            billDate=billDate.split("-")[0]+"-"+billDate.split("-")[1]+"-"+billDate.split("-")[2];
            
            String POSCode="";
            String sql="select strPOSCode from tblbillhd where strBillNo='"+selectedBill+"' ";
            ResultSet rsPOSCode=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rsPOSCode.next())
            {
                POSCode=rsPOSCode.getString(1);
            }
            rsPOSCode.close();
            
            
            try 
            {
                if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
                {
                   // funTextFilePrintingBill(selectedBill,billDate);
                    objUtility.funPrintBill(selectedBill, billDate,false,POSCode,"print");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please Select Bill.");
            return;
        }
    }
    
    
    
    private void funNewCustomerButtonPressed()
    {        
        new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
        if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
        {
            if (clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{8}") || clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{9}") || clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{10}"))
            {
                clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
                funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please Enter Valid Mobile No.");
                return;
            }
        }
    }
    
    
    
    private void funSetCustMobileNo(String mbNo)
    {
        try
        {            
            if (mbNo.trim().length() == 0)
            {
                objUtility.funCallForSearchForm("CustomerMaster");
                new frmSearchFormDialog(this, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    lblMemberCodeValue.setText(data[0].toString());
                    lblMemberNameValue.setText(data[1].toString());
                }
            }
            else
            {
                String sql = "select count(strCustomerCode) from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
                ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCustomer.next();
                int found = rsCustomer.getInt(1);
                rsCustomer.close();

                if (found > 0)
                {
                    if (clsGlobalVarClass.gCustAddressSelectionForBill)
                    {
                        sql = "select strCustomerCode,strCustomerName,longMobileNo,strBuildingName,"
                            + "strStreetName,strLandMark,strOfficeBuildingName,strOfficeStreetName,strOfficeLandmark"
                            + " from tblcustomermaster where longMobileNo='" + mbNo + "'";
                        rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        while (rsCustomer.next())
                        {                            
                            lblMemberCodeValue.setText(rsCustomer.getString(1));
                            lblMemberNameValue.setText(rsCustomer.getString(2));
                        }
                        rsCustomer.close();
                        objUtility.funCallForSearchForm("CustomerAddress");
                        new frmSearchFormDialog(this, true).setVisible(true);
                        if (clsGlobalVarClass.gSearchItemClicked)
                        {
                            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                            lblMemberCodeValue.setText(data[0].toString());
                            lblMemberNameValue.setText(data[1].toString());
                        }
                    }
                    else
                    {
                        String sql_CustInfo = "select strCustomerCode,strCustomerName,strBuldingCode "
                            + "from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
                        ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
                        if (rsCust.next())
                        {
                            lblMemberCodeValue.setText(rsCust.getString(1));
                            lblMemberNameValue.setText("<html>" + rsCust.getString(2) + "</html>");
                        }
                    }
                }
                else
                {
                    clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(mbNo);
                    new frmCustomerMaster().setVisible(true);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
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
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblformName = new javax.swing.JLabel();
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
        panelShowBills = new javax.swing.JPanel();
        scrBills = new javax.swing.JScrollPane();
        tableVoidBill = new javax.swing.JTable();
        lblBillNo = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnChangeCustomer = new javax.swing.JButton();
        OrderPanel = new javax.swing.JPanel();
        scrItemDtlGrid = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        lblPaxNo = new javax.swing.JLabel();
        lblMemberNameValue = new javax.swing.JLabel();
        lblMemberName = new javax.swing.JLabel();
        lblBillNo1 = new javax.swing.JLabel();
        lblVoucherNo = new javax.swing.JLabel();
        lblSubTotalTitle = new javax.swing.JLabel();
        lblSubTotalValue = new javax.swing.JLabel();
        lblTaxTitle = new javax.swing.JLabel();
        lblTaxValue = new javax.swing.JLabel();
        lblUserCreated = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        btnVoidBill = new javax.swing.JButton();
        btnItemVoid = new javax.swing.JButton();
        lblBillDateTime1 = new javax.swing.JLabel();
        lblMemberCode = new javax.swing.JLabel();
        lblDateTime1 = new javax.swing.JLabel();
        lblMemberCodeValue = new javax.swing.JLabel();

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
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("Change Customer On Bill");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblformName);

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

        panelShowBills.setBackground(new java.awt.Color(255, 255, 255));
        panelShowBills.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelShowBills.setOpaque(false);

        tableVoidBill.setBackground(new java.awt.Color(254, 254, 254));
        tableVoidBill.setForeground(new java.awt.Color(1, 1, 1));
        tableVoidBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No", "Bill Date"
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
        tableVoidBill.setRowHeight(25);
        tableVoidBill.setSelectionBackground(new java.awt.Color(0, 120, 255));
        tableVoidBill.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tableVoidBill.getTableHeader().setReorderingAllowed(false);
        tableVoidBill.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tableVoidBillMouseClicked(evt);
            }
        });
        scrBills.setViewportView(tableVoidBill);

        lblBillNo.setText("Bill No.");

        txtSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSearchKeyPressed(evt);
            }
        });

        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSearch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSearchMouseClicked(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnChangeCustomer.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnChangeCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnChangeCustomer.setText("<html>Change Customer</html>");
        btnChangeCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeCustomer.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnChangeCustomer.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnChangeCustomerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelShowBillsLayout = new javax.swing.GroupLayout(panelShowBills);
        panelShowBills.setLayout(panelShowBillsLayout);
        panelShowBillsLayout.setHorizontalGroup(
            panelShowBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShowBillsLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnChangeCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(scrBills)
        );
        panelShowBillsLayout.setVerticalGroup(
            panelShowBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShowBillsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelShowBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelShowBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnChangeCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrBills, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        OrderPanel.setBackground(new java.awt.Color(255, 255, 255));
        OrderPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        OrderPanel.setForeground(new java.awt.Color(254, 184, 80));
        OrderPanel.setOpaque(false);
        OrderPanel.setPreferredSize(new java.awt.Dimension(260, 600));
        OrderPanel.setLayout(null);

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
        tblItemTable.getTableHeader().setReorderingAllowed(false);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrItemDtlGrid.setViewportView(tblItemTable);

        OrderPanel.add(scrItemDtlGrid);
        scrItemDtlGrid.setBounds(0, 130, 340, 300);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");
        OrderPanel.add(lblTotal);
        lblTotal.setBounds(160, 470, 60, 20);
        OrderPanel.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);
        OrderPanel.add(lblMemberNameValue);
        lblMemberNameValue.setBounds(120, 100, 220, 30);

        lblMemberName.setText("Member Name:");
        OrderPanel.add(lblMemberName);
        lblMemberName.setBounds(10, 100, 120, 30);

        lblBillNo1.setText("Bill No.");
        OrderPanel.add(lblBillNo1);
        lblBillNo1.setBounds(10, 10, 50, 30);
        OrderPanel.add(lblVoucherNo);
        lblVoucherNo.setBounds(70, 10, 70, 30);

        lblSubTotalTitle.setText("Sub Total ");
        OrderPanel.add(lblSubTotalTitle);
        lblSubTotalTitle.setBounds(160, 430, 80, 20);

        lblSubTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblSubTotalValue);
        lblSubTotalValue.setBounds(230, 430, 100, 20);

        lblTaxTitle.setText("Tax");
        OrderPanel.add(lblTaxTitle);
        lblTaxTitle.setBounds(160, 450, 80, 20);

        lblTaxValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblTaxValue);
        lblTaxValue.setBounds(230, 450, 100, 20);
        OrderPanel.add(lblUserCreated);
        lblUserCreated.setBounds(250, 10, 90, 30);

        lblTotalAmt.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalAmt.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblTotalAmt);
        lblTotalAmt.setBounds(230, 470, 100, 20);

        lblUserName.setText("User Created");
        OrderPanel.add(lblUserName);
        lblUserName.setBounds(150, 10, 90, 30);

        btnVoidBill.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVoidBill.setForeground(new java.awt.Color(255, 255, 255));
        btnVoidBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnVoidBill.setText("Print");
        btnVoidBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVoidBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnVoidBill.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnVoidBillActionPerformed(evt);
            }
        });
        OrderPanel.add(btnVoidBill);
        btnVoidBill.setBounds(120, 500, 100, 40);

        btnItemVoid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnItemVoid.setForeground(new java.awt.Color(255, 255, 255));
        btnItemVoid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnItemVoid.setText("Save");
        btnItemVoid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemVoid.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnItemVoid.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnItemVoidActionPerformed(evt);
            }
        });
        OrderPanel.add(btnItemVoid);
        btnItemVoid.setBounds(10, 500, 100, 40);

        lblBillDateTime1.setText("Date & Time");
        OrderPanel.add(lblBillDateTime1);
        lblBillDateTime1.setBounds(10, 40, 90, 30);

        lblMemberCode.setText("Member Code: ");
        OrderPanel.add(lblMemberCode);
        lblMemberCode.setBounds(10, 70, 120, 30);
        OrderPanel.add(lblDateTime1);
        lblDateTime1.setBounds(100, 40, 200, 30);
        OrderPanel.add(lblMemberCodeValue);
        lblMemberCodeValue.setBounds(120, 70, 220, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addComponent(OrderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelShowBills, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OrderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelShowBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableVoidBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableVoidBillMouseClicked
        // TODO add your handling code here:
        funSelectBill();
    }//GEN-LAST:event_tableVoidBillMouseClicked

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        // TODO add your handling code here:
        frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Enter Bill No.");
        keyboard.setVisible(true);
        keyboard.setAlwaysOnTop(true);
        keyboard.setAutoRequestFocus(true);
        txtSearch.setText(clsGlobalVarClass.gKeyboardValue);
        funSearchBillNo(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        // TODO add your handling code here:
        funSearchBillNo(txtSearch.getText().trim());
    }//GEN-LAST:event_btnSearchMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("ChangeCustomerOnBill");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
        
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyPressed
    {//GEN-HEADEREND:event_txtSearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSearchBillNo(txtSearch.getText().trim());
        }
    }//GEN-LAST:event_txtSearchKeyPressed

    private void btnVoidBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnVoidBillActionPerformed
    {//GEN-HEADEREND:event_btnVoidBillActionPerformed
        try
        {
            funPrintBill();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnVoidBillActionPerformed

      /*private void funTextFilePrintingBill(String billNo,String billDate){
        
        try 
        {
            clsTextFileGeneratorForPrinting obj = new clsTextFileGeneratorForPrinting();
            if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 1"))
            {
                obj.funGenerateTextFileBillPrinting(billNo, "reprint","","sale",billDate,"");
            }
            else if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 2"))
            {
                obj.funGenerateTextFileBillPrintingForFormat2(billNo, "reprint","","sale",billDate);
            }
            else if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 3"))
            {
                obj.funGenerateTextFileBillPrintingForFormat3(billNo, "reprint","","sale",billDate);
            }
            else if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 4"))
            {
                obj.funGenerateTextFileBillPrintingForFormat4(billNo, "reprint","","sale",billDate);
            }
            else if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 5"))
            {
                obj.funGenerateTextFileBillPrintingForFormat5(billNo, "","","sale",billDate);
            }
            else if(clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 6"))
            {
                obj.funGenerateTextFileBillPrintingForFormat6(billNo, "","","sale",billDate);
            }
         } catch (Exception e) {
              e.printStackTrace();
         }
        
     } */
    
    
    
    private void btnItemVoidActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnItemVoidActionPerformed
    {//GEN-HEADEREND:event_btnItemVoidActionPerformed
        if (tableVoidBill.getSelectedRow() >=0)
        {
            if(clsGlobalVarClass.gCMSIntegrationYN)
            {
                funUpdateMemberDetails();
            }
            else
            {
                funUpdateCustomerDetails();
            }
        }
    }//GEN-LAST:event_btnItemVoidActionPerformed

    private void btnChangeCustomerMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnChangeCustomerMouseClicked
    {//GEN-HEADEREND:event_btnChangeCustomerMouseClicked
        try
        {
            if (tableVoidBill.getSelectedRow() > -1)
            {
                if (clsGlobalVarClass.gCMSIntegrationYN)
                {
                    funGetCMSMemberCode();
                }
                else
                {
                    funNewCustomerButtonPressed();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Please Select Bill.");
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnChangeCustomerMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("ChangeCustomerOnBill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("ChangeCustomerOnBill");
    }//GEN-LAST:event_formWindowClosing

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void funGetCMSMemberCode() throws Exception
    {
        new frmAlfaNumericKeyBoard(null, true, "1", "Enter Member Code").setVisible(true);
        String strCustomerCode = clsGlobalVarClass.gKeyboardValue;
        if (clsGlobalVarClass.gKeyboardValue.trim().length() > 0)
        {
            clsUtility objUtility=new clsUtility();
            String memberInfo = objUtility.funCheckMemeberBalance(strCustomerCode);
            if (memberInfo.contains("#"))
            {
                if (memberInfo.split("#")[4].trim().equals("Y"))
                {
                    JOptionPane.showMessageDialog(this, "Member is blocked");
                    return;
                }
                else
                {
                    cmsMemCode = memberInfo.split("#")[0];
                    cmsMemName = memberInfo.split("#")[1];
                    funSetMemberDetail(cmsMemCode, cmsMemName);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Member Not Found!!!");
            }
        }
    }

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
            java.util.logging.Logger.getLogger(frmChangeCustomerOnBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmChangeCustomerOnBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmChangeCustomerOnBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmChangeCustomerOnBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmChangeCustomerOnBill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JButton btnChangeCustomer;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnItemVoid;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnVoidBill;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBillDateTime1;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblBillNo1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateTime1;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMemberCode;
    public static javax.swing.JLabel lblMemberCodeValue;
    private javax.swing.JLabel lblMemberName;
    public static javax.swing.JLabel lblMemberNameValue;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSubTotalTitle;
    private javax.swing.JLabel lblSubTotalValue;
    private javax.swing.JLabel lblTaxTitle;
    private javax.swing.JLabel lblTaxValue;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserCreated;
    private javax.swing.JLabel lblUserName;
    public javax.swing.JLabel lblVoucherNo;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelShowBills;
    private javax.swing.JScrollPane scrBills;
    private javax.swing.JScrollPane scrItemDtlGrid;
    private javax.swing.JTable tableVoidBill;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private void funSetMemberDetail(String cmsMemCode, String cmsMemName)
    {
        lblMemberCodeValue.setText(cmsMemCode);
        lblMemberNameValue.setText(cmsMemName);

        strMemberCode=cmsMemCode;
    }


    private void funUpdateMemberDetails()
    {
        try
        {
            int selectedBillNo = tableVoidBill.getSelectedRow();
            String billNo = tableVoidBill.getModel().getValueAt(selectedBillNo, 0).toString();
            if(strMemberCode.equalsIgnoreCase("ND"))
            {
                strMemberCode="";
            }
            String sql = "update tblbillhd set strCustomerCode='" + strMemberCode + "' \n"
                + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strBillNo='" + billNo + "' ";
            //System.out.println("update sql="+sql);
            int i = clsGlobalVarClass.dbMysql.execute(sql);
            
            if (i >= 0)
            {
                new frmOkPopUp(null, "Customer Updated Successfully.", "Message", 1).setVisible(true);
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to Print Bill ?", "", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) 
                {
                    funPrintBill();
                }
                funResetFields();
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    
    private void funUpdateCustomerDetails()
    {
        try
        {
            int selectedBillNo = tableVoidBill.getSelectedRow();
            String billNo = tableVoidBill.getModel().getValueAt(selectedBillNo, 0).toString();
            
            String sql = "update tblbillhd set strCustomerCode='" + lblMemberCodeValue.getText() + "' \n"
                + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strBillNo='" + billNo + "' ";
            //System.out.println("update sql="+sql);
            int i = clsGlobalVarClass.dbMysql.execute(sql);
            
            sql = "update tblhomedelivery set strCustomerCode='" + lblMemberCodeValue.getText() + "' \n"
                + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strBillNo='" + billNo + "' ";
            //System.out.println("update sql="+sql);
            clsGlobalVarClass.dbMysql.execute(sql);           
            if (i >= 0)
            {
                new frmOkPopUp(null, "Customer Updated Successfully.", "Message", 1).setVisible(true);
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to Print Bill ?", "", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) 
                {
                    funPrintBill();
                }
                funResetFields();
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    

    private void funResetFields()
    {
        strMemberCode = "";
        lblMemberCodeValue.setText("");
        lblMemberNameValue.setText("");
        dm.setRowCount(0);
        lblVoucherNo.setText("");
        lblUserCreated.setText("");
        lblDateTime1.setText("");
        lblSubTotalValue.setText("");
        lblTaxValue.setText("");
        lblTotalAmt.setText("");

    }
}
