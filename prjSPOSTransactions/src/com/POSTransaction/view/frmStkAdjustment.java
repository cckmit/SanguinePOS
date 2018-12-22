/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmStkAdjustment extends javax.swing.JFrame
{

    private java.util.Vector vItemName, vItemCode;
    private String sql, day, date, time, dteCreated, billDate;
    private double variance, sellTotal, stkInTotal;
    private String selectQuery, insertQuery, voucherNo, code, strCode, updateQuery, StockInCode, posCode;
    java.util.Vector modVector, itemVector, vPurchaseRate, vSaleRate;
    ResultSet recordSet;
    DecimalFormat formt;
    private boolean flag;
    clsUtility objUtility;

    public frmStkAdjustment()
    {
        initComponents();
        objUtility = new clsUtility();
        scrTax.setVisible(false);
        btnGSIE.setEnabled(false);
        formt = new DecimalFormat("####0.00");
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

    }

    private void funSetData(Object[] data)
    {
        try
        {
            lblBillNo.setText("");
            lblStkInCode.setText("");
            java.util.Date objDate = new java.util.Date();
            dteCreated = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
                    + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

            String bdte = clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);
            lblStkDate.setText(bdte);
            String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
            String time = bDate.getHours() + ":" + bDate.getMinutes() + ":" + bDate.getSeconds();
            billDate = date1 + " " + time;

            flag = true;
            txtPSPCode.setText(data[0].toString());
            posCode = data[1].toString();
            vItemCode = new java.util.Vector();
            vItemName = new java.util.Vector();
            itemVector = new java.util.Vector();
            vPurchaseRate = new java.util.Vector();
            vSaleRate = new java.util.Vector();
            btnGSIE.setEnabled(false);
            sql = "select strBillNo,strStkInCode from tblpsphd "
                    + "where strPSPCode='" + data[0].toString() + "' and strPOSCode='" + posCode + "'";
            ResultSet pspRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            pspRs.next();
            String bNo = pspRs.getString(1);
            String stkNo = pspRs.getString(2);

            if (bNo.length() > 0)
            {
                lblBillNo.setText(bNo);
            }
            if (stkNo.length() > 0)
            {
                lblStkInCode.setText(stkNo);
            }

            if (bNo.length() > 0)
            {
                funFillTable(1);
                flag = false;
            }
            else if (stkNo.length() > 0)
            {
                funFillTable(2);
                lblStkInCode.setText(stkNo);
                flag = false;
            }
            else
            {
                funFillTable(2);
            }
            if (!flag)
            {
                btnGSE.setEnabled(false);
                btnGSIE.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillTable(int id)
    {
        try
        {
            sellTotal = 0;
            stkInTotal = 0;
            vItemCode.removeAllElements();
            vItemName.removeAllElements();
            vPurchaseRate.removeAllElements();
            vSaleRate.removeAllElements();
            DefaultTableModel dm = new DefaultTableModel();
            dm.addColumn("Item Code");
            dm.addColumn("Item Name");
            dm.addColumn("Variance");

            if (id == 1)
            {
                cmbTransactionType.setSelectedItem("Sale");
                btnGSE.setEnabled(true);
                btnGSIE.setEnabled(false);
            }
            else
            {
                cmbTransactionType.setSelectedItem("StkIn");
                btnGSE.setEnabled(false);
                btnGSIE.setEnabled(true);
            }

            if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
            {
                dm.addColumn("Sale Rate");
                dm.addColumn("Sale Amount");
            }
            else
            {
                dm.addColumn("Purchase Rate");
                dm.addColumn("Purchase Amount");
            }

            Object[] ob = new Object[5];
            sql = "select * from tblpspdtl where strPSPCode='" + txtPSPCode.getText() + "'";
            System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String itCode = rs.getString(2);

                sql = "SELECT a.strItemCode,a.strItemName,a.strStockInEnable,a.dblPurchaseRate,a.strRawMaterial,a.dblSalePrice "
                        + "FROM tblitemmaster a "
                        + "WHERE a.strItemCode='" + itCode + "' ";
                ResultSet rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rs1.next())
                {
                    vItemCode.add(itCode);
                    double purchaseRate = Double.parseDouble(rs1.getString(4));
                    double saleRate = Double.parseDouble(rs1.getString(6));
                    vPurchaseRate.add(purchaseRate);
                    vSaleRate.add(saleRate);
                    String itName = rs1.getString(2);
                    vItemName.add(itName);
                    variance = Double.parseDouble(rs.getString(5));

                    if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
                    {
                        if (variance < 0)
                        {
                            ob[0] = itCode;
                            ob[1] = itName;
                            ob[2] = variance * (-1);
                            ob[3] = saleRate;
                            double purTotal = (variance * saleRate) * (-1);
                            ob[4] = formt.format(purTotal);
                            dm.addRow(ob);
                            sellTotal = sellTotal + purTotal;
                        }
                    }
                    else
                    {
                        if (variance >= 0)
                        {
                            ob[0] = itCode;
                            ob[1] = itName;
                            ob[2] = variance;
                            ob[3] = purchaseRate;
                            double purTotal = variance * purchaseRate;
                            ob[4] = formt.format(purTotal);
                            dm.addRow(ob);
                            stkInTotal = stkInTotal + purTotal;
                        }
                    }
                }
            }

            tblStockTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblStockTable.setShowHorizontalLines(true);
            tblStockTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblStockTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(0).setPreferredWidth(135);
            tblStockTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblStockTable.getColumnModel().getColumn(2).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(4).setPreferredWidth(115);
            lblTotal.setText(formt.format(sellTotal));

            if (!flag)
            {
                btnGSE.setEnabled(false);
                btnGSIE.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funResetFields()
    {
        try
        {
            DefaultTableModel dm = new DefaultTableModel();
            dm.addColumn("Item Code");
            dm.addColumn("Item Name");
            dm.addColumn("Variance");

            if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
            {
                dm.addColumn("Sale Rate");
                dm.addColumn("Sale Amount");
            }
            else
            {
                dm.addColumn("Purchase Rate");
                dm.addColumn("Purchase Amount");
            }

            tblStockTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblStockTable.setShowHorizontalLines(true);
            tblStockTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblStockTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(0).setPreferredWidth(135);
            tblStockTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblStockTable.getColumnModel().getColumn(2).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(4).setPreferredWidth(115);
            lblTotal.setText("");
            int rCnt = tblStockTable.getRowCount();
            //fillTaxTable(rCnt,"Sales",String.valueOf(sellTotal));
            btnGSE.setEnabled(false);
            btnGSIE.setEnabled(false);
            lblBillNo.setText("");
            lblStkInCode.setText("");
            lblTotal.setText("");
            txtPSPCode.setText("");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGenerateBill()
    {
        try
        {
            Long code = null;
            selectQuery = "select count(strBillNo) from  tblstorelastbill where strPosCode='" + posCode + "'";//generate Bill No.
            recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            recordSet.next();
            int count = recordSet.getInt(1);
            recordSet.close();
            if (count > 0)
            {
                selectQuery = "select strBillNo from tblstorelastbill where strPosCode='" + posCode + "'";
                System.out.println(selectQuery);
                recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                recordSet.next();
                code = recordSet.getLong(1);
                System.out.println("code" + code);
                code = code + 1;
                //String formatted = String.format("%07d", number);
                voucherNo = posCode + String.format("%05d", code);
                updateQuery = "update tblstorelastbill set strBillNo='" + code + "' where strPosCode='" + posCode + "'";
                clsGlobalVarClass.dbMysql.execute(updateQuery);
                //System.out.println("Bill No="+voucherNo+"POS Code="+posCode);
            }
            else
            {
                voucherNo = posCode + "00001";
                insertQuery = "insert into tblstorelastbill values('" + posCode + "','1')";
                clsGlobalVarClass.dbMysql.execute(insertQuery);
                //System.out.println("Bill No="+voucherNo+"POS Code="+posCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return voucherNo;
    }

    private String funGenerateStockInCode()
    {
        try
        {
            selectQuery = "select count(*) from tblstkinhd";
            ResultSet rrs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            rrs.next();
            int cn = rrs.getInt(1);

            if (cn > 0)
            {
                selectQuery = "select max(strStkInCode) from tblstkinhd";
                rrs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                rrs.next();
                code = rrs.getString(1);
                int ind = code.lastIndexOf("0");
                if ((ind + 1) == code.length())
                {
                    strCode = code.substring(ind - 1, code.length());
                }
                else
                {
                    strCode = code.substring(ind + 1, code.length());
                }
                System.out.println(strCode);
                int intCode = Integer.parseInt(strCode);
                intCode++;
                if (intCode < 10)
                {
                    StockInCode = "SI0000" + intCode;
                }
                else if (intCode < 100)
                {
                    StockInCode = "SI000" + intCode;
                }
                else if (intCode < 1000)
                {
                    StockInCode = "SI00" + intCode;
                }
                else if (intCode < 10000)
                {
                    StockInCode = "SI0" + intCode;
                }
                else if (intCode < 100000)
                {
                    StockInCode = "SI" + intCode;
                }
            }
            else
            {
                StockInCode = "SI00001";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return StockInCode;
    }

    private void funStkInEntry()
    {
        try
        {
            java.util.Date objDate = new java.util.Date();

            dteCreated = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
                    + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();;

            String stkInCode = funGenerateStockInCode();
            sql = "insert into tblstkinhd (strStkInCode,strPOSCode,dteStkInDate,strReasonCode,strUserCreated,"
                    + "strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
                    + " values('" + stkInCode + "','" + clsGlobalVarClass.gPOSCode + "','" + billDate
                    + "','R99','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + dteCreated + "','"
                    + dteCreated + "','"+clsGlobalVarClass.gClientCode+"')";
            clsGlobalVarClass.dbMysql.execute(sql);

            for (int i = 0; i < tblStockTable.getRowCount(); i++)
            {
                sql = "insert into tblstkindtl (strStkInCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount,strClientCode)"
                        + " values('" + stkInCode + "','" + vItemCode.elementAt(i).toString() + "','" + variance + "','"
                        + formt.format(vPurchaseRate.elementAt(i)) + "','" + formt.format(stkInTotal) + "','"+clsGlobalVarClass.gClientCode+"')";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            sql = "update tblpsphd set strStkInCode='" + stkInCode + "' where strPSPCode='" + txtPSPCode.getText() + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
            lblStkInCode.setText(stkInCode);
            JOptionPane.showMessageDialog(this, "Stock In Entry Generated Successfully");
            funResetFields();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funSaleEntry()
    {
        try
        {
            java.util.Date objDate = new java.util.Date();
            dteCreated = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
                    + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();;
            String billNo = funGenerateBill();
            sql = "update tblpsphd set strBillNo='" + billNo + "' where strPSPCode='" + txtPSPCode.getText() + "'";

            String settMode = "Cash";
            String settCode = "S01";
            String cardName = "";

            int exc = clsGlobalVarClass.dbMysql.execute(sql);
            sql = "insert into tblbillhd(strBillNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
                    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strUserCreated,strUserEdited,dteDateCreated,"
                    + "dteDateEdited,strClientCode) values('" + billNo + "','" + billDate + "','" + clsGlobalVarClass.gPOSCode
                    + "','" + settMode + "','0.00','0.00','0.00','" + sellTotal + "','" + sellTotal + "','" + clsGlobalVarClass.gUserCode
                    + "','" + clsGlobalVarClass.gUserCode + "','" + dteCreated + "','" + dteCreated + "','" + clsGlobalVarClass.gClientCode + "')";
            exc = clsGlobalVarClass.dbMysql.execute(sql);

            sql = "insert into tblbillsettlementdtl (strBillNo,strSettlementCode,dblSettlementAmt,dblPaidAmt,strCardName,"
                    + "strClientCode) values('" + billNo + "','" + settCode + "','" + sellTotal + "','" + sellTotal + "','" + cardName + "','"
                    + clsGlobalVarClass.gClientCode + "')";
            exc = clsGlobalVarClass.dbMysql.execute(sql);

            for (int i = 0; i < tblStockTable.getRowCount(); i++)
            {
                sql = "insert into tblbilldtl(strItemCode,strItemName,strBillNo,dblQuantity,dblAmount,dblTaxAmount,"
                        + "dteBillDate,strClientCode) values('" + vItemCode.elementAt(i).toString() + "','" + vItemName.elementAt(i).toString()
                        + "','" + billNo + "','" + tblStockTable.getValueAt(i, 2) + "','" + tblStockTable.getValueAt(i, 4) + "','0.00"
                        + "','" + billDate + "','" + clsGlobalVarClass.gClientCode + "')";
                exc = clsGlobalVarClass.dbMysql.execute(sql);
            }
            lblBillNo.setText(billNo);
            JOptionPane.showMessageDialog(this, "Sale Entry Generated Successfully");
            funResetFields();
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
        cmbTransactionType = new javax.swing.JComboBox();
        lblFormName = new javax.swing.JLabel();
        scrItemDtl = new javax.swing.JScrollPane();
        tblStockTable = new javax.swing.JTable();
        btnGSE = new javax.swing.JButton();
        btnGSIE = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        scrTax = new javax.swing.JScrollPane();
        tblTaxTable = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        lblBill = new javax.swing.JLabel();
        lblBillNo = new javax.swing.JLabel();
        lblStkIn = new javax.swing.JLabel();
        lblStkInCode = new javax.swing.JLabel();
        lblStkDate = new javax.swing.JLabel();
        lblPhyStkCode = new javax.swing.JLabel();
        txtPSPCode = new javax.swing.JTextField();

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
        lblformName.setText("- Stock Adujstment");
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

        cmbTransactionType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbTransactionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sale", "Stock In", "Stock Out" }));
        cmbTransactionType.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbTransactionTypeItemStateChanged(evt);
            }
        });
        cmbTransactionType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbTransactionTypeMouseClicked(evt);
            }
        });

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Stock Adjustment");

        tblStockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null}
            },
            new String []
            {
                "Item Code", "Item Name", "Variance", "Sale Rate", "Sale Amount"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblStockTable.setRowHeight(25);
        scrItemDtl.setViewportView(tblStockTable);

        btnGSE.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnGSE.setForeground(new java.awt.Color(255, 255, 255));
        btnGSE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGSE.setText("<html>Generate <br>Sale Entry</html>");
        btnGSE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGSE.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGSE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGSEActionPerformed(evt);
            }
        });

        btnGSIE.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnGSIE.setForeground(new java.awt.Color(255, 255, 255));
        btnGSIE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGSIE.setText("<html>Generate <br>Stock In Entry</html>");
        btnGSIE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGSIE.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGSIE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGSIEActionPerformed(evt);
            }
        });

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblTotalAmt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalAmt.setText("Total");

        tblTaxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Tax Code", "Taxable Amt", "Tax Amt"
            }
        ));
        scrTax.setViewportView(tblTaxTable);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        lblBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBill.setText("Bill No");

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        lblStkIn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStkIn.setText("Stock In Code");

        lblStkInCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblStkDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStkDate.setText("Generate Entry");

        lblPhyStkCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPhyStkCode.setText("Phy Stk Posting No");

        txtPSPCode.setEnabled(false);
        txtPSPCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPSPCodeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(scrItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnGSE, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btnGSIE, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(lblTotalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrTax, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblPhyStkCode, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtPSPCode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(140, 140, 140)
                                .addComponent(lblStkDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(cmbTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblBill, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(lblStkIn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblStkInCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStkDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhyStkCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPSPCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBill, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStkIn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStkInCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(scrItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrTax, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGSE, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGSIE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTransactionTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTransactionTypeItemStateChanged
        // TODO add your handling code here:
        try
        {

            sellTotal = 0;
            stkInTotal = 0;
            vItemCode.removeAllElements();
            vItemName.removeAllElements();
            vPurchaseRate.removeAllElements();
            vSaleRate.removeAllElements();
            DefaultTableModel dm = new DefaultTableModel();
            dm.addColumn("Item Code");
            dm.addColumn("Item Name");
            dm.addColumn("Variance");

            if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
            {
                dm.addColumn("Sale Rate");
                dm.addColumn("Sale Amount");
            }
            else
            {
                dm.addColumn("Purchase Rate");
                dm.addColumn("Purchase Amount");
            }

            Object[] ob = new Object[5];
            sql = "select * from tblpspdtl where strPSPCode='" + txtPSPCode.getText() + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String itCode = rs.getString(2);
                sql = "SELECT a.strItemCode,a.strItemName,a.strStockInEnable,a.dblPurchaseRate,a.strRawMaterial,a.dblSalePrice "
                        + "FROM tblitemmaster a "
                        + "WHERE a.strItemCode='" + itCode + "' ";
                ResultSet rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rs1.next())
                {
                    vItemCode.add(itCode);
                    double purchaseRate = Double.parseDouble(rs1.getString(4));
                    double saleRate = Double.parseDouble(rs1.getString(6));
                    vPurchaseRate.add(purchaseRate);
                    vSaleRate.add(saleRate);
                    String itName = rs1.getString(2);
                    vItemName.add(itName);
                    variance = Double.parseDouble(rs.getString(5));

                    if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
                    {
                        if (variance < 0)
                        {
                            ob[0] = itCode;
                            ob[1] = itName;
                            ob[2] = variance * (-1);
                            ob[3] = saleRate;
                            double purTotal = (variance * saleRate) * (-1);
                            ob[4] = formt.format(purTotal);
                            dm.addRow(ob);
                            sellTotal = sellTotal + purTotal;
                        }
                    }
                    else
                    {
                        if (variance >= 0)
                        {
                            ob[0] = itCode;
                            ob[1] = itName;
                            ob[2] = variance;
                            ob[3] = purchaseRate;
                            double purTotal = variance * purchaseRate;
                            ob[4] = formt.format(purTotal);
                            dm.addRow(ob);
                            stkInTotal = stkInTotal + purTotal;
                        }
                    }
                }
            }

            tblStockTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblStockTable.setShowHorizontalLines(true);
            tblStockTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblStockTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tblStockTable.getColumnModel().getColumn(0).setPreferredWidth(135);
            tblStockTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblStockTable.getColumnModel().getColumn(2).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblStockTable.getColumnModel().getColumn(4).setPreferredWidth(115);
            if (cmbTransactionType.getSelectedItem().toString().equals("Sale"))
            {
                lblTotal.setText(formt.format(sellTotal));
                btnGSIE.setEnabled(false);
                btnGSE.setEnabled(true);
            }
            else
            {
                lblTotal.setText(formt.format(stkInTotal));
                btnGSIE.setEnabled(true);
                btnGSE.setEnabled(false);
            }
            if (!flag)
            {
                btnGSE.setEnabled(false);
                btnGSIE.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbTransactionTypeItemStateChanged

    private void cmbTransactionTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbTransactionTypeMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbTransactionTypeMouseClicked

    private void btnGSEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGSEActionPerformed
        // TODO add your handling code here:
        funSaleEntry();
    }//GEN-LAST:event_btnGSEActionPerformed

    private void btnGSIEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGSIEActionPerformed
        // TODO add your handling code here:
        funStkInEntry();
    }//GEN-LAST:event_btnGSIEActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Stock Adujstment");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtPSPCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPSPCodeMouseClicked
        // TODO add your handling code here:
        try
        {
            objUtility.funCallForSearchForm("PSPCode");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPSPCodeMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Stock Adujstment");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Stock Adujstment");
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
            java.util.logging.Logger.getLogger(frmStkAdjustment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmStkAdjustment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmStkAdjustment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmStkAdjustment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmStkAdjustment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGSE;
    private javax.swing.JButton btnGSIE;
    private javax.swing.JComboBox cmbTransactionType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBill;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPhyStkCode;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblStkDate;
    private javax.swing.JLabel lblStkIn;
    private javax.swing.JLabel lblStkInCode;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrItemDtl;
    private javax.swing.JScrollPane scrTax;
    private javax.swing.JTable tblStockTable;
    private javax.swing.JTable tblTaxTable;
    private javax.swing.JTextField txtPSPCode;
    // End of variables declaration//GEN-END:variables
}
