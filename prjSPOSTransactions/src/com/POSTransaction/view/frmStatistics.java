package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmStatistics extends javax.swing.JFrame
{

    private String sql, dtPOSDate;
    private HashMap hmPOS;
    private int cntNext, cntPrev;
    private double totalSalesAmt, totalSalesQty;
    private JButton arrBtnPOS[];
    private ArrayList<String> listOfPOSNames;

    public frmStatistics()
    {
        initComponents();
        try
        {
            cntNext = 0;
            cntPrev = 0;
            totalSalesAmt = 0;
            totalSalesQty = 0;
            dtPOSDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0].trim();
            System.out.println("Date=" + dtPOSDate);
            arrBtnPOS = new JButton[]
            {
                btnPOS1, btnPOS2, btnPOS3, btnPOS4
            };
            funFillPOSButtons();
            funFillSalesAchieved("All", "All");
            funFillSalesInProgress("All", "All");
            funCalculateAvgSalesInfo("All");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillPOSButtons() throws Exception
    {
        hmPOS = new HashMap<String, String>();
        listOfPOSNames = new ArrayList<String>();

        if (clsGlobalVarClass.gShowOnlyLoginPOSReports)
        {
            hmPOS.put(clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSName);

            hmPOS.put(clsGlobalVarClass.gPOSName, clsGlobalVarClass.gPOSCode);
            listOfPOSNames.add(clsGlobalVarClass.gPOSName);

        }
        else
        {
            hmPOS.put("All", "All");
            listOfPOSNames.add("All");
            sql = "select strPOSCode,strPOSName from tblposmaster order by strPOSCode ";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                hmPOS.put(rsPOS.getString(2), rsPOS.getString(1));
                listOfPOSNames.add(rsPOS.getString(2));
            }
            rsPOS.close();
        }

        int start = 0;
        int end = 4;
        if (hmPOS.size() <= 4)
        {
            end = hmPOS.size();
        }
        else
        {
            btnNext.setEnabled(true);
        }

        funSetPOSButtonNames(start, end);
    }

    private void funSetPOSButtonNames(int start, int end)
    {
        Object[] arrPOS = hmPOS.entrySet().toArray();

        funSetEnablePOSButtons(false);
        for (int cnt = 0; cnt < end; cnt++)
        {
//            String pos = arrPOS[(cntNext*4)+cnt].toString().split("=>")[0];
//            String posName = pos.split("=")[0];

            String posName = listOfPOSNames.get((cntNext * 4) + cnt);
            arrBtnPOS[cnt].setEnabled(true);
            arrBtnPOS[cnt].setText(posName);
        }
    }

    private void funFillSalesAchieved(String posCode, String posName) throws Exception
    {
        lblPOSName.setText(posName);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tblSalesAchieved.setRowHeight(16);
        DefaultTableModel dmSalesAchieved = (DefaultTableModel) tblSalesAchieved.getModel();
        dmSalesAchieved.setRowCount(0);

        String sql = "select c.strRevenueHead "
                + "from tblitemmaster c "
                + "group by c.strRevenueHead "
                + "order by c.strRevenueHead ";
        ResultSet rsRevenueHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsRevenueHead.next())
        {
            Object[] row =
            {
                rsRevenueHead.getString(1), "0", "0"
            };
            dmSalesAchieved.addRow(row);
        }
        Object[] arrOb11 =
        {
            "<html><font size=5 color=red>Totals</font></html>", "0", "0"
        };
        dmSalesAchieved.addRow(arrOb11);

        tblSalesAchieved.setModel(dmSalesAchieved);
        tblSalesAchieved.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblSalesAchieved.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        double totalQty = 0, totalAmt = 0;

        String amountField = "sum(b.dblamount)";
        if (!chkWithDiscount.isSelected())
        {
            amountField = "sum(b.dblamount)-sum(b.dblDiscountAmt) ";
        }

        for (int cnt = 0; cnt < tblSalesAchieved.getRowCount(); cnt++)
        {
            sql = "select c.strRevenueHead,sum(b.dblquantity)," + amountField + " "
                    + " from tblbillhd a,tblbilldtl b,tblitemmaster c "
                    + " where a.strBillNo=b.strBillNo and b.stritemcode=c.strItemCode ";
            if (!posCode.equals("All"))
            {
                sql += " and a.strPosCode='" + posCode + "' ";
            }
            sql += " group by c.strRevenueHead "
                    + " order by c.strRevenueHead;";
            //System.out.println(sql);
            ResultSet rsRevenue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsRevenue.next())
            {
                if (rsRevenue.getString(1).trim().equalsIgnoreCase(tblSalesAchieved.getValueAt(cnt, 0).toString().trim()))
                {
                    totalQty += rsRevenue.getDouble(2);
                    totalAmt += rsRevenue.getDouble(3);
                    tblSalesAchieved.setValueAt(rsRevenue.getDouble(2), cnt, 1);
                    tblSalesAchieved.setValueAt(rsRevenue.getDouble(3), cnt, 2);
                }
            }
            rsRevenue.close();
        }
        totalSalesAmt += totalAmt;
        totalSalesQty += totalQty;
        lblTotalAmt.setText(Math.rint(totalSalesAmt) + "");
        lblTotalQty.setText(Math.rint(totalSalesQty) + "");
        for (int r = 0; r < tblSalesAchieved.getRowCount(); r++)
        {
            if (tblSalesAchieved.getValueAt(r, 0).toString().equalsIgnoreCase("<html><font size=5 color=red>Totals</font></html>"))
            {
                tblSalesAchieved.setValueAt("<html><font size=5 color=red>" + totalQty + "</font></html>", r, 1);
                tblSalesAchieved.setValueAt("<html><font size=5 color=red>" + totalAmt + "</font></html>", r, 2);
                break;
            }
        }
    }

    private void funFillSalesInProgress(String posCode, String posName) throws Exception
    {
        lblPOSName.setText(posName);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblSalesInProgress.setRowHeight(16);
        DefaultTableModel dmSalesInProgress = (DefaultTableModel) tblSalesInProgress.getModel();
        dmSalesInProgress.setRowCount(0);
//        

        String sql = "select c.strRevenueHead "
                + "from tblitemmaster c "
                + "group by c.strRevenueHead "
                + "order by c.strRevenueHead ";
        ResultSet rsRevenueHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsRevenueHead.next())
        {
            Object[] row =
            {
                rsRevenueHead.getString(1), "0", "0"
            };
            dmSalesInProgress.addRow(row);
        }
        Object[] arrOb11 =
        {
            "<html><font size=5 color=red>Totals</font></html>", "0", "0"
        };
        dmSalesInProgress.addRow(arrOb11);

        tblSalesInProgress.setModel(dmSalesInProgress);
        tblSalesInProgress.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblSalesInProgress.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        double totalQty = 0, totalAmt = 0;

        for (int cnt = 0; cnt < tblSalesInProgress.getRowCount(); cnt++)
        {
            sql = "select b.strRevenueHead,sum(a.dblItemQuantity),sum(a.dblAmount) "
                    + " from tblitemrtemp a,tblitemmaster b "
                    + " where a.stritemcode=b.strItemCode and a.strNCKotYN='N' ";
            if (!posCode.equals("All"))
            {
                sql += " and a.strPosCode='" + posCode + "' ";
            }
            sql += " group by b.strRevenueHead "
                    + " order by b.strRevenueHead;";

            ResultSet rsRevenue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsRevenue.next())
            {
                if (rsRevenue.getString(1).trim().equalsIgnoreCase(tblSalesInProgress.getValueAt(cnt, 0).toString().trim()))
                {
                    totalQty += rsRevenue.getDouble(2);
                    totalAmt += rsRevenue.getDouble(3);
                    tblSalesInProgress.setValueAt(rsRevenue.getDouble(2), cnt, 1);
                    tblSalesInProgress.setValueAt(rsRevenue.getDouble(3), cnt, 2);
                }
            }
            rsRevenue.close();
        }
        totalSalesAmt += totalAmt;
        totalSalesQty += totalQty;
        lblTotalAmt.setText(Math.rint(totalSalesAmt) + "");
        lblTotalQty.setText(Math.rint(totalSalesQty) + "");

        for (int r = 0; r < tblSalesInProgress.getRowCount(); r++)
        {
            if (tblSalesInProgress.getValueAt(r, 0).toString().equalsIgnoreCase("<html><font size=5 color=red>Totals</font></html>"))
            {
                tblSalesInProgress.setValueAt("<html><font size=5 color=red>" + totalQty + "</font></html>", r, 1);
                tblSalesInProgress.setValueAt("<html><font size=5 color=red>" + totalAmt + "</font></html>", r, 2);
                break;
            }
        }
    }

    private void funCalculateAvgSalesInfo(String posCode) throws Exception
    {
        int coversTurned = 0, tablesTurned = 0, coversServed = 0, busyTables = 0;

        sql = "select count(distinct(strTableNo)) from tblbillhd ";
        if (!posCode.equals("All"))
        {
            sql += " where strPOSCode='" + posCode + "'";
        }
        ResultSet rsTurned = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsTurned.next())
        {
            tablesTurned = rsTurned.getInt(1);
            txtTablesTurned.setText(tablesTurned + "");
        }
        rsTurned.close();

        sql = "select count(distinct(strTableNo)) from tblitemrtemp where strNCKotYN='N' ";
        if (!posCode.equals("All"))
        {
            sql += " and  strPOSCode='" + posCode + "'";
        }
        ResultSet rsBusy = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsBusy.next())
        {
            busyTables = rsBusy.getInt(1);
            txtBusyTables.setText(busyTables + "");
        }
        rsBusy.close();

        // Covers/PAX
        sql = "select count(intPaxNo) from tblbillhd ";
        if (!posCode.equals("All"))
        {
            sql += " where strPOSCode='" + posCode + "'";
        }
        ResultSet rsCovers = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsCovers.next())
        {
            coversTurned = rsCovers.getInt(1);
            txtCoversTurned.setText(coversTurned + "");
        }
        rsCovers.close();

        sql = "select count(intPaxNo) from tblitemrtemp where strNCKotYN='N' ";
        if (!posCode.equals("All"))
        {
            sql += " and strPOSCode='" + posCode + "'";
        }
        rsCovers = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsCovers.next())
        {
            coversServed = rsCovers.getInt(1);
            txtCoversServed.setText(coversServed + "");
        }
        rsCovers.close();

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableModel dmAvgCovers = (DefaultTableModel) tblAvgCovers.getModel();
        dmAvgCovers.setRowCount(0);
        DefaultTableModel dmAvgCheck = (DefaultTableModel) tblAvgCheck.getModel();
        dmAvgCheck.setRowCount(0);

        sql = "select c.strItemType,sum(b.dblamount) "
                + " from tblbillhd a,tblbilldtl b,tblitemmaster c "
                + " where a.strBillNo=b.strBillNo and b.stritemcode=c.strItemCode ";
        //     + " and date(a.dteBillDate)='"+dtPOSDate+"' ";
        if (!posCode.equals("All"))
        {
            sql += " and a.strPosCode='" + posCode + "' ";
        }
        sql += " group by c.strItemType "
                + " order by c.strItemType;";

        double totalAvgCheckAchieved = 0, totalAvgCoversAchieved = 0;
        ResultSet rsAvgSalesAchieved = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsAvgSalesAchieved.next())
        {
            totalAvgCoversAchieved += Math.rint(rsAvgSalesAchieved.getDouble(2) / coversTurned);
            totalAvgCheckAchieved += Math.rint(rsAvgSalesAchieved.getDouble(2) / tablesTurned);

            Object[] arrObjAvgCovers =
            {
                rsAvgSalesAchieved.getString(1), Math.rint(rsAvgSalesAchieved.getDouble(2) / coversTurned)
            };
            dmAvgCovers.addRow(arrObjAvgCovers);

            Object[] arrObjAvgCheck =
            {
                rsAvgSalesAchieved.getString(1), Math.rint(rsAvgSalesAchieved.getDouble(2) / tablesTurned)
            };
            dmAvgCheck.addRow(arrObjAvgCheck);
        }
        rsAvgSalesAchieved.close();

        String tempAvgTotalCoversAch = "<html><font size=4 color=red>" + totalAvgCoversAchieved + "</font></html>";
        String tempAvgTotalCheckAch = "<html><font size=4 color=red>" + totalAvgCheckAchieved + "</font></html>";

        Object[] arrObj1 =
        {
            "<html><font size=4 color=red>Total</font></html>", tempAvgTotalCoversAch
        };
        Object[] arrObj2 =
        {
            "<html><font size=4 color=red>Total</font></html>", tempAvgTotalCheckAch
        };

        Object[] arrTemp =
        {
            "", "", ""
        };

        dmAvgCovers.addRow(arrTemp);
        dmAvgCheck.addRow(arrTemp);
        dmAvgCovers.addRow(arrObj1);
        dmAvgCheck.addRow(arrObj2);

        tblAvgCheck.setModel(dmAvgCheck);
        tblAvgCovers.setModel(dmAvgCovers);
        tblAvgCheck.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblAvgCovers.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        DefaultTableModel dmAvgCovers1 = (DefaultTableModel) tblAvgCovers1.getModel();
        dmAvgCovers1.setRowCount(0);
        DefaultTableModel dmAvgCheck1 = (DefaultTableModel) tblAvgCheck1.getModel();
        dmAvgCheck1.setRowCount(0);

        sql = "select c.strItemType,sum(a.dblamount) "
                + " from tblitemrtemp a,tblitemmaster c "
                + " where a.stritemcode=c.strItemCode ";
        //+ " where a.stritemcode=c.strItemCode and strNCKotYN='N' ";
        //    + " and date(a.dteDateCreated)='"+dtPOSDate+"' ";
        if (!posCode.equals("All"))
        {
            sql += " and a.strPosCode='" + posCode + "' ";
        }
        sql += " group by c.strItemType order by c.strItemType;";

        double totalAvgCoverInProgress = 0, totalAvgBusyTablesInProgress = 0;
        ResultSet rsAvgSalesInProgress = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsAvgSalesInProgress.next())
        {
            totalAvgCoverInProgress += Math.rint(rsAvgSalesInProgress.getDouble(2) / coversServed);
            Object[] arrObjAvgCovers1 =
            {
                rsAvgSalesInProgress.getString(1), Math.rint(rsAvgSalesInProgress.getDouble(2) / coversServed)
            };
            dmAvgCovers1.addRow(arrObjAvgCovers1);

            totalAvgBusyTablesInProgress += Math.rint(rsAvgSalesInProgress.getDouble(2) / busyTables);
            Object[] arrObjAvgCheck1 =
            {
                rsAvgSalesInProgress.getString(1), Math.rint(rsAvgSalesInProgress.getDouble(2) / busyTables)
            };
            dmAvgCheck1.addRow(arrObjAvgCheck1);
        }
        rsAvgSalesInProgress.close();

        String tempAvgTotalCoversInProgress = "<html><font size=4 color=red>" + totalAvgCoverInProgress + "</font></html>";
        String tempAvgTotalBusyTablesInProgress = "<html><font size=4 color=red>" + totalAvgBusyTablesInProgress + "</font></html>";

        Object[] arrObj3 =
        {
            "<html><font size=4 color=red>Total</font></html>", tempAvgTotalCoversInProgress
        };
        Object[] arrObj4 =
        {
            "<html><font size=4 color=red>Total</font></html>", tempAvgTotalBusyTablesInProgress
        };

        dmAvgCovers1.addRow(arrTemp);
        dmAvgCheck1.addRow(arrTemp);
        dmAvgCovers1.addRow(arrObj3);
        dmAvgCheck1.addRow(arrObj4);

        tblAvgCheck1.setModel(dmAvgCheck1);
        tblAvgCovers1.setModel(dmAvgCovers1);
        tblAvgCheck1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblAvgCovers1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
    }

    private void funPOSButtonClicked(String posName, int index)
    {
        try
        {
            /*
             JButton[] arrBtnPOS={btnPOS1,btnPOS2,btnPOS3,btnPOS4};
             arrBtnPOS[index].setBackground(Color.red);
            
             for(int cnt=0;cnt<arrBtnPOS.length;cnt++)
             {
             if(cnt!=index)
             {
             arrBtnPOS[cnt].setBackground(Color.black);
             }
             }*/
            totalSalesAmt = 0;
            totalSalesQty = 0;
            String posCode = hmPOS.get(posName).toString();
            funFillSalesAchieved(posCode, posName);
            funFillSalesInProgress(posCode, posName);
            funCalculateAvgSalesInfo(posCode);
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
        panelMainForm = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSalesAchieved = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSalesInProgress = new javax.swing.JTable();
        panelPOS = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        btnPOS1 = new javax.swing.JButton();
        btnPOS2 = new javax.swing.JButton();
        btnPOS3 = new javax.swing.JButton();
        btnPOS4 = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblSalesInProgress = new javax.swing.JLabel();
        lblSalesAchieved = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAvgCovers = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblAvgCheck = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblAvgCovers1 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblAvgCheck1 = new javax.swing.JTable();
        txtTablesTurned = new javax.swing.JTextField();
        txtCoversTurned = new javax.swing.JTextField();
        lblCoversTurned = new javax.swing.JLabel();
        lblTablesTurned = new javax.swing.JLabel();
        txtBusyTables = new javax.swing.JTextField();
        lblCoversServed = new javax.swing.JLabel();
        txtCoversServed = new javax.swing.JTextField();
        lblBusyTables = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        lblTotalQty = new javax.swing.JLabel();
        lblPOSName = new javax.swing.JLabel();
        chkWithDiscount = new javax.swing.JCheckBox();

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
        lblformName.setText("STATISTICS");
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
        lblUserCode.setName(""); // NOI18N
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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(254, 254, 254));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tblSalesAchieved.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblSalesAchieved.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Revenue Head", "Quantity", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSalesAchieved);
        if (tblSalesAchieved.getColumnModel().getColumnCount() > 0) {
            tblSalesAchieved.getColumnModel().getColumn(0).setPreferredWidth(250);
        }

        tblSalesInProgress.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblSalesInProgress.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Revenue Head", "Quantity", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblSalesInProgress);
        if (tblSalesInProgress.getColumnModel().getColumnCount() > 0) {
            tblSalesInProgress.getColumnModel().getColumn(0).setPreferredWidth(250);
        }

        panelPOS.setBackground(new java.awt.Color(255, 255, 255));

        btnPrevious.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnPrevious.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPrevious.setText("<<");
        btnPrevious.setEnabled(false);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnPOS1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnPOS1.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPOS1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPOS1ActionPerformed(evt);
            }
        });

        btnPOS2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnPOS2.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPOS2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPOS2ActionPerformed(evt);
            }
        });

        btnPOS3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnPOS3.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPOS3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPOS3ActionPerformed(evt);
            }
        });

        btnPOS4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnPOS4.setForeground(new java.awt.Color(255, 255, 255));
        btnPOS4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPOS4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPOS4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPOS4ActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnNext.setText(">>");
        btnNext.setEnabled(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelPOSLayout = new javax.swing.GroupLayout(panelPOS);
        panelPOS.setLayout(panelPOSLayout);
        panelPOSLayout.setHorizontalGroup(
            panelPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPOSLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPOS1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPOS2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPOS3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPOS4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelPOSLayout.setVerticalGroup(
            panelPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
            .addComponent(btnPOS1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnPOS2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnPOS3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnPOS4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        lblSalesInProgress.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        lblSalesInProgress.setText("Sales In Progress");

        lblSalesAchieved.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        lblSalesAchieved.setText("Sales Achieved");

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        jLabel1.setText("Other Information");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tblAvgCovers.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblAvgCovers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Avg Covers", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblAvgCovers);

        tblAvgCheck.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblAvgCheck.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Avg Check", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblAvgCheck);

        tblAvgCovers1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblAvgCovers1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Avg Covers", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblAvgCovers1);

        tblAvgCheck1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        tblAvgCheck1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Avg Check", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblAvgCheck1);

        txtTablesTurned.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        txtTablesTurned.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTablesTurned.setText("0");
        txtTablesTurned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTablesTurnedActionPerformed(evt);
            }
        });

        txtCoversTurned.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        txtCoversTurned.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblCoversTurned.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCoversTurned.setText("Covers Turned");

        lblTablesTurned.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblTablesTurned.setText("Tables Turned");

        txtBusyTables.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        txtBusyTables.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBusyTables.setMinimumSize(new java.awt.Dimension(6, 21));

        lblCoversServed.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCoversServed.setText("Covers Served");

        txtCoversServed.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        txtCoversServed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCoversServed.setMinimumSize(new java.awt.Dimension(6, 21));

        lblBusyTables.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblBusyTables.setText("Busy Tables");

        lblTotalAmt.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        lblTotalAmt.setForeground(new java.awt.Color(51, 51, 255));
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblTotalQty.setFont(new java.awt.Font("Trebuchet MS", 2, 18)); // NOI18N
        lblTotalQty.setForeground(new java.awt.Color(51, 51, 255));
        lblTotalQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblPOSName.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        lblPOSName.setForeground(new java.awt.Color(51, 51, 255));

        chkWithDiscount.setText("With Discount");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblSalesAchieved, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalQty, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSalesInProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCoversTurned, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblTablesTurned, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtCoversTurned)
                                            .addComponent(txtTablesTurned, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                                .addComponent(lblBusyTables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                                .addComponent(lblCoversServed, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                                                .addComponent(txtCoversServed, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkWithDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkWithDiscount))
                        .addComponent(lblTotalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTotalQty, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSalesAchieved)
                        .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSalesInProgress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCoversTurned, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCoversTurned, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTablesTurned, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTablesTurned, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCoversServed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCoversServed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(lblBusyTables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnPOS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS1ActionPerformed
        // TODO add your handling code here:
        funPOSButtonClicked(btnPOS1.getText().trim(), 0);

    }//GEN-LAST:event_btnPOS1ActionPerformed

    private void btnPOS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS2ActionPerformed
        // TODO add your handling code here:
        funPOSButtonClicked(btnPOS2.getText().trim(), 1);
    }//GEN-LAST:event_btnPOS2ActionPerformed

    private void btnPOS3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS3ActionPerformed
        // TODO add your handling code here:
        funPOSButtonClicked(btnPOS3.getText().trim(), 2);
    }//GEN-LAST:event_btnPOS3ActionPerformed

    private void btnPOS4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOS4ActionPerformed
        // TODO add your handling code here:
        funPOSButtonClicked(btnPOS4.getText().trim(), 3);
    }//GEN-LAST:event_btnPOS4ActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        if (btnPrevious.isEnabled())
        {
            funPreviousButtonClicked();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void txtTablesTurnedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTablesTurnedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTablesTurnedActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        clsGlobalVarClass.hmActiveForms.remove("Statistics");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnNextMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNextMouseClicked
    {//GEN-HEADEREND:event_btnNextMouseClicked
        if (btnNext.isEnabled())
        {
            funNextButtonClicked();
        }
    }//GEN-LAST:event_btnNextMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Statistics");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Statistics");
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPOS1;
    private javax.swing.JButton btnPOS2;
    private javax.swing.JButton btnPOS3;
    private javax.swing.JButton btnPOS4;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JCheckBox chkWithDiscount;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblBusyTables;
    private javax.swing.JLabel lblCoversServed;
    private javax.swing.JLabel lblCoversTurned;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSalesAchieved;
    private javax.swing.JLabel lblSalesInProgress;
    private javax.swing.JLabel lblTablesTurned;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblTotalQty;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelPOS;
    private javax.swing.JTable tblAvgCheck;
    private javax.swing.JTable tblAvgCheck1;
    private javax.swing.JTable tblAvgCovers;
    private javax.swing.JTable tblAvgCovers1;
    private javax.swing.JTable tblSalesAchieved;
    private javax.swing.JTable tblSalesInProgress;
    private javax.swing.JTextField txtBusyTables;
    private javax.swing.JTextField txtCoversServed;
    private javax.swing.JTextField txtCoversTurned;
    private javax.swing.JTextField txtTablesTurned;
    // End of variables declaration//GEN-END:variables

    private void funNextButtonClicked()
    {
        cntNext++;
        btnPrevious.setEnabled(true);

        int start = 0;
        int end = hmPOS.size() - (cntNext * 4);

        if (((cntNext * 4) + 4) >= hmPOS.size())
        {
            btnNext.setEnabled(false);
        }
        if (end > 4)
        {
            end = 4;
        }

        funSetPOSButtonNames(start, end);
    }

    private void funSetEnablePOSButtons(boolean flag)
    {
        for (int cnt = 0; cnt < 4; cnt++)
        {
            arrBtnPOS[cnt].setText("");
            arrBtnPOS[cnt].setEnabled(flag);
        }
    }

    private void funPreviousButtonClicked()
    {
        cntNext--;
        btnNext.setEnabled(true);

        int end = (hmPOS.size() - (cntNext * 4)) - 1;
        int start = 0;

        if (cntNext == 0)
        {
            btnPrevious.setEnabled(false);
        }

        if (end > 4)
        {
            end = 4;
            start = 0;
        }
        else
        {
            start = 0;
            end = end;
        }
        funSetPOSButtonNames(start, end);
    }

}
