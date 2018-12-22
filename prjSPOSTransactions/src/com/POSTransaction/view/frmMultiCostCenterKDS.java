package com.POSTransaction.view;

import com.POSGlobal.controller.clsCustomTableCellRenderer;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class frmMultiCostCenterKDS extends javax.swing.JFrame
{

    private DefaultTableModel dmKDS;
    private Map mapCostCenterCode;
    private Map hmCostCenter;
    private int cntNavigate;
    //  public int cntNavigate,cntNavigate1,tblStartIndex, tblEndIndex;
    Timer timer;
    private clsUtility objUtility;

    public frmMultiCostCenterKDS()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Windows Classic".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }//CDE/Motif
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        objUtility = new clsUtility();
        initComponents();
        this.setLocationRelativeTo(null);

        try
        {
            // Function cost center ,master 
            btnPrevious.setEnabled(false);
            cntNavigate = 0;

            funLoadHmCostCenter();

            timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String new_str = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
                    lblDate.setText(dateAndTime);
                    try
                    {
                        funLoadCostCenter();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            //funLoadCostCenter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadCostCenter() throws Exception
    {
        dmKDS = new DefaultTableModel();

        Set setCostCenter = mapCostCenterCode.keySet();
        Iterator itrCostCenter = setCostCenter.iterator();
        while (itrCostCenter.hasNext())
        {
            dmKDS.addColumn(itrCostCenter.next());
        }

        tblKDS.setModel(dmKDS);

        funAddItemToKDSGrid();
    }

    private void funAddItemToKDSGrid() throws Exception
    {
        int row = 0;
        int columnCount = tblKDS.getColumnCount();
        //tblKDS.setForeground(Color.red);

        for (int cnt = 0; cnt < tblKDS.getColumnCount(); cnt++)
        {
            int rowCount = tblKDS.getRowCount();
            row = 0;
            String column = tblKDS.getColumnName(cnt).toString();
            String costCenterCode = mapCostCenterCode.get(column).toString();
            /* String sql="select a.strItemName,c.strCostCenterName,a.dblItemQuantity,a.strKOTNo,left(time(a.dteDateCreated) ,5),a.strTableNo,a.strWaiterNo "
             + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
             + "where left(a.strItemCode,7)=b.strItemCode and b.strCostCenterCode=c.strCostCenterCode "
             + "and b.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' and b.strCostCenterCode='"+costCenterCode+"' "
             + "group by a.strItemCode,a.strKOTNo "
             + "order by a.strKOTNo desc";*/
            String sql = "select a.strItemName,c.strCostCenterName,a.dblItemQuantity,a.strKOTNo,left(time(a.dteDateCreated) ,5),d.strTableName ,e.strWFullName "
                    + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c ,tbltablemaster d ,tblwaitermaster e "
                    + "where left(a.strItemCode,7)=b.strItemCode "
                    + "and b.strCostCenterCode=c.strCostCenterCode and a.strPOSCode=b.strPOSCode "
                    + "and b.strCostCenterCode='" + costCenterCode + "' "
                    + "and d.strTableNo=a.strTableNo "
                    + "and e.strWaiterNo=a.strWaiterNo "
                    + "group by a.strItemCode,a.strKOTNo "
                    + "order by a.strKOTNo desc;";

            System.out.println(sql);
            String kot = "";
            ResultSet rsTemp = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTemp.next())
            {
                String dbCol = rsTemp.getString(2);
                String kotNO = rsTemp.getString(4);
                String kotTime = rsTemp.getString(5);
                String tableName = rsTemp.getString(6);
                String waiterName = rsTemp.getString(7);
                if (dbCol.equals(column))
                {
                    String itemName = rsTemp.getString(1);
                    String qty = rsTemp.getString(3);

                    int columnWidth = tblKDS.getColumnModel().getColumn(cnt).getMinWidth();
                    int len = funPrintBlankLines(itemName, qty, 30);
                    for (int l = 0; l < len; l++)
                    {
                        //itemName+=" ";
                    }
                    itemName = qty + "     " + itemName;
                    if (itemName.contains("-->"))
                    {
                        itemName = "     " + itemName;
                    }
                    itemName = "     " + itemName;
                    String kotName = tableName + "                                     " + waiterName;
                    kotName = "<html><font color=green> " + tableName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + waiterName + "</font></html>";
                    if (row < rowCount)
                    {
                        if (!kot.equals(kotNO))
                        {
                            kot = kotNO;
                            String kotWithTime = kot + "                        " + kotTime;

                            tblKDS.setValueAt(kotWithTime, row, cnt);
                            row++;

                            TableCellRenderer renderer = new clsCustomTableCellRenderer();
                            renderer.getTableCellRendererComponent(tblKDS, kotWithTime, false, false, row, cnt);
                            tblKDS.getColumnModel().getColumn(cnt).setCellRenderer(renderer);
                            tblKDS.setValueAt(kotName, row, cnt);
                            row++;
                        }
                        tblKDS.setValueAt(itemName, row, cnt);
                    }
                    else
                    {
                        if (!kot.equals(kotNO))
                        {
                            kot = kotNO;
                            String kotWithTime = kot + "                        " + kotTime;

                            Object[] arrObjRowKOT = new Object[columnCount];
                            Object[] arrObjRowKOT1 = new Object[columnCount];
                            for (int i = 0; i < columnCount; i++)
                            {
                                arrObjRowKOT[i] = "";
                                arrObjRowKOT1[i] = "";

                            }

                            arrObjRowKOT[cnt] = kotWithTime;
                            dmKDS.addRow(arrObjRowKOT);
                            arrObjRowKOT1[cnt] = kotName;
                            dmKDS.addRow(arrObjRowKOT1);

                            TableCellRenderer renderer = new clsCustomTableCellRenderer();
                            renderer.getTableCellRendererComponent(tblKDS, kotWithTime, false, false, row, cnt);
                            tblKDS.getColumnModel().getColumn(cnt).setCellRenderer(renderer);

                        }

                        Object[] arrObjRow = new Object[columnCount];
                        for (int i = 0; i < columnCount; i++)
                        {
                            arrObjRow[i] = "";
                        }
                        arrObjRow[cnt] = itemName;
                        dmKDS.addRow(arrObjRow);
                    }
                    row++;
                }
            }
        }
    }

    private void funLoadHmCostCenter() throws Exception
    {
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(true);
        mapCostCenterCode = new HashMap<String, String>();
        hmCostCenter = new HashMap<String, String>();

        String sql = "select strCostCenterCode,strCostCenterName "
                + "from tblcostcentermaster "
                + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
        ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsCostCenter.next())
        {
            hmCostCenter.put(rsCostCenter.getString(2), rsCostCenter.getString(1));
        }
        rsCostCenter.close();

        int cnt = 0;
        sql = "select c.strCostCenterCode,c.strCostCenterName "
                + "from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
                + "where a.strItemCode=b.strItemCode and b.strCostCenterCode=c.strCostCenterCode "
                + "and a.strPOSCode=b.strPosCode "
                + "and c.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                + "group by c.strCostCenterCode,c.strCostCenterName "
                + "order by c.strCostCenterName ";
        rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsCostCenter.next())
        {
            mapCostCenterCode.put(rsCostCenter.getString(2), rsCostCenter.getString(1));
            cnt++;
        }
        rsCostCenter.close();

        funButtonArraylist(0, 4);

    }

    private void funChngeButtonSelected(JButton button)
    {
        if (button.getForeground() == Color.WHITE)
        {
            button.setForeground(java.awt.Color.BLACK);
            mapCostCenterCode.remove(button.getText());
        }

        else
        {
            button.setForeground(java.awt.Color.WHITE);
            mapCostCenterCode.put(button.getText(), hmCostCenter.get(button.getText()));
        }

    }

    private void funButtonArraylist(int start, int end)
    {

        JButton button[] =
        {
            btnCostCenter1, btnCostCenter2, btnCostCenter3, btnCostCenter4
        };
        Set set = hmCostCenter.keySet();
        Object[] arrObjCostCenter = set.toArray();
        int cnt = 0;
        for (int i = start; i < end; i++)
        {
           // System.out.println(arrObjCostCenter[i]);

            if (i == hmCostCenter.size())
            {

                break;
            }
            if (cnt < 4)
            {
                button[cnt].setText("" + arrObjCostCenter[i]);
                button[cnt].setEnabled(true);
                button[cnt].setVisible(true);
                cnt++;
            }

        }

        for (int j = cnt; j < 4; j++)
        {
            button[j].setEnabled(false);
            button[j].setVisible(false);
        }

    }

    private int funPrintBlankLines(String textToPrint, String textToPrint1, int columnWidth)
    {
        int len = columnWidth - (textToPrint.length() + textToPrint1.length());
        len = len / 2;
        return len;
    }

    private void funUpdateCell()
    {
        try
        {
            int selectedRow = tblKDS.getSelectedRow();
            int selectedColumn = tblKDS.getSelectedColumn();
            //System.out.println(tblKDS.getValueAt(selectedRow,selectedColumn));

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

        jFrame1 = new javax.swing.JFrame();
        panelHeader1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lblDate1 = new javax.swing.JLabel();
        lblPosName1 = new javax.swing.JLabel();
        lblUserCode1 = new javax.swing.JLabel();
        lblModuleName1 = new javax.swing.JLabel();
        lblformName1 = new javax.swing.JLabel();
        panelMain1 = new javax.swing.JPanel();
        lblFormName1 = new javax.swing.JLabel();
        scrKDS1 = new javax.swing.JScrollPane();
        tblKDS1 = new javax.swing.JTable();
        panelCostCenter1 = new javax.swing.JPanel();
        btnCostCenter5 = new javax.swing.JButton();
        btnCostCenter7 = new javax.swing.JButton();
        btnCostCenter8 = new javax.swing.JButton();
        btnCostCenter9 = new javax.swing.JButton();
        btnPrevious1 = new javax.swing.JButton();
        btnNext1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnCostCenter10 = new javax.swing.JButton();
        lblBackground1 = new javax.swing.JLabel();
        panelHeader = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblPosName = new javax.swing.JLabel();
        lblUserCode = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        panelMain = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        scrKDS = new javax.swing.JScrollPane();
        tblKDS = new javax.swing.JTable();
        panelCostCenter = new javax.swing.JPanel();
        btnCostCenter1 = new javax.swing.JButton();
        btnCostCenter4 = new javax.swing.JButton();
        btnCostCenter3 = new javax.swing.JButton();
        btnCostCenter2 = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnCostCenter6 = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jFrame1.setMinimumSize(new java.awt.Dimension(800, 600));
        jFrame1.setUndecorated(true);
        jFrame1.setResizable(false);
        jFrame1.getContentPane().setLayout(null);

        panelHeader1.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader1.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader1.setPreferredSize(new java.awt.Dimension(800, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("SPOS -");

        lblDate1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate1.setForeground(new java.awt.Color(255, 255, 255));

        lblPosName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName1.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUserCode1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode1.setForeground(new java.awt.Color(255, 255, 255));

        lblModuleName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName1.setForeground(new java.awt.Color(255, 255, 255));

        lblformName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName1.setForeground(new java.awt.Color(255, 255, 255));
        lblformName1.setText("- Multi Cost Center KDS");

        javax.swing.GroupLayout panelHeader1Layout = new javax.swing.GroupLayout(panelHeader1);
        panelHeader1.setLayout(panelHeader1Layout);
        panelHeader1Layout.setHorizontalGroup(
            panelHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeader1Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblModuleName1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblformName1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPosName1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUserCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );
        panelHeader1Layout.setVerticalGroup(
            panelHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeader1Layout.createSequentialGroup()
                .addGroup(panelHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPosName1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblModuleName1)
                        .addComponent(lblformName1))
                    .addComponent(lblUserCode1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jFrame1.getContentPane().add(panelHeader1);
        panelHeader1.setBounds(0, 0, 799, 30);

        panelMain1.setBackground(new java.awt.Color(216, 238, 254));

        lblFormName1.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblFormName1.setForeground(new java.awt.Color(51, 102, 255));
        lblFormName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tblKDS1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder())));
        tblKDS1.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        tblKDS1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {}
            },
            new String []
            {

            }
        ));
        tblKDS1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblKDS1.setRowHeight(30);
        tblKDS1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblKDS1MouseClicked(evt);
            }
        });
        scrKDS1.setViewportView(tblKDS1);

        btnCostCenter5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter5.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter5.setText("Cost 1");
        btnCostCenter5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter5ActionPerformed(evt);
            }
        });

        btnCostCenter7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter7.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter7.setText("Cost 4");
        btnCostCenter7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter7ActionPerformed(evt);
            }
        });

        btnCostCenter8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter8.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter8.setText("Cost 3");
        btnCostCenter8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCostCenter8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter8ActionPerformed(evt);
            }
        });

        btnCostCenter9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter9.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter9.setText("Cost 2");
        btnCostCenter9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter9ActionPerformed(evt);
            }
        });

        btnPrevious1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrevious1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevious1.setText("<<");
        btnPrevious1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevious1ActionPerformed(evt);
            }
        });

        btnNext1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNext1.setText(">>");
        btnNext1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNext1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCostCenter1Layout = new javax.swing.GroupLayout(panelCostCenter1);
        panelCostCenter1.setLayout(panelCostCenter1Layout);
        panelCostCenter1Layout.setHorizontalGroup(
            panelCostCenter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCostCenter1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter9, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(btnCostCenter8, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(341, Short.MAX_VALUE))
        );
        panelCostCenter1Layout.setVerticalGroup(
            panelCostCenter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCostCenter1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCostCenter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCostCenter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btnCostCenter9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCostCenter8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnCostCenter7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnCostCenter5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        btnCostCenter10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter10.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter10.setText("Close");
        btnCostCenter10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter10.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMain1Layout = new javax.swing.GroupLayout(panelMain1);
        panelMain1.setLayout(panelMain1Layout);
        panelMain1Layout.setHorizontalGroup(
            panelMain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMain1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMain1Layout.createSequentialGroup()
                        .addComponent(panelCostCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFormName1))
                    .addGroup(panelMain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMain1Layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCostCenter10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrKDS1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMain1Layout.setVerticalGroup(
            panelMain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMain1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(lblFormName1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelMain1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(panelCostCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrKDS1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelMain1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMain1Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(panelMain1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCostCenter10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jFrame1.getContentPane().add(panelMain1);
        panelMain1.setBounds(-9, 0, 1140, 580);

        lblBackground1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"))); // NOI18N
        jFrame1.getContentPane().add(lblBackground1);
        lblBackground1.setBounds(0, 0, 1600, 1400);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
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
        getContentPane().setLayout(null);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.setPreferredSize(new java.awt.Dimension(800, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("SPOS -");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jLabel12MouseClicked(evt);
            }
        });

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Multi Cost Center KDS");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblModuleName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblformName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPosName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblModuleName)
                        .addComponent(lblformName))
                    .addComponent(lblUserCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelHeader);
        panelHeader.setBounds(0, 0, 799, 30);

        panelMain.setBackground(new java.awt.Color(216, 238, 254));

        lblFormName.setFont(new java.awt.Font("Trebuchet MS", 0, 30)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(51, 102, 255));
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tblKDS.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder())));
        tblKDS.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        tblKDS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {}
            },
            new String []
            {

            }
        ));
        tblKDS.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblKDS.setRowHeight(30);
        tblKDS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblKDSMouseClicked(evt);
            }
        });
        scrKDS.setViewportView(tblKDS);

        btnCostCenter1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter1.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter1.setText("Cost 1");
        btnCostCenter1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter1ActionPerformed(evt);
            }
        });

        btnCostCenter4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter4.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter4.setText("Cost 4");
        btnCostCenter4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter4ActionPerformed(evt);
            }
        });

        btnCostCenter3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter3.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter3.setText("Cost 3");
        btnCostCenter3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCostCenter3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter3ActionPerformed(evt);
            }
        });

        btnCostCenter2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter2.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter2.setText("Cost 2");
        btnCostCenter2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter2ActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevious.setText("<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNext.setText(">>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCostCenterLayout = new javax.swing.GroupLayout(panelCostCenter);
        panelCostCenter.setLayout(panelCostCenterLayout);
        panelCostCenterLayout.setHorizontalGroup(
            panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCostCenterLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(btnCostCenter3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCostCenter4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(341, Short.MAX_VALUE))
        );
        panelCostCenterLayout.setVerticalGroup(
            panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCostCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCostCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btnCostCenter2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCostCenter3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnCostCenter4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnCostCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCostCenter6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCostCenter6.setForeground(new java.awt.Color(255, 255, 255));
        btnCostCenter6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCostCenter6.setText("Close");
        btnCostCenter6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCostCenter6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCostCenter6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCostCenter6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(panelCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFormName))
                    .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCostCenter6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(scrKDS, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(lblFormName))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(panelCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrKDS, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCostCenter6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelMain);
        panelMain.setBounds(-9, 0, 1140, 580);

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"))); // NOI18N
        getContentPane().add(lblBackground);
        lblBackground.setBounds(0, 0, 1600, 1400);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblKDSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKDSMouseClicked
        funUpdateCell();
    }//GEN-LAST:event_tblKDSMouseClicked

    private void btnCostCenter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostCenter1ActionPerformed
        // TODO add your handling code here:
        funChngeButtonSelected(btnCostCenter1);
    }//GEN-LAST:event_btnCostCenter1ActionPerformed

    private void btnCostCenter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostCenter2ActionPerformed
        // TODO add your handling code here:
        funChngeButtonSelected(btnCostCenter2);
    }//GEN-LAST:event_btnCostCenter2ActionPerformed

    private void btnCostCenter3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostCenter3ActionPerformed
        // TODO add your handling code here:
        funChngeButtonSelected(btnCostCenter3);
    }//GEN-LAST:event_btnCostCenter3ActionPerformed

    private void btnCostCenter4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostCenter4ActionPerformed
        // TODO add your handling code here:

        funChngeButtonSelected(btnCostCenter4);
    }//GEN-LAST:event_btnCostCenter4ActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:

        try
        {
            cntNavigate--;
            btnNext.setEnabled(true);
            if (cntNavigate == 0)
            {
                btnPrevious.setEnabled(false);
                funButtonArraylist(0, hmCostCenter.size());
            }
            else
            {
                int buttonSize = cntNavigate * 4;
                int resMod = hmCostCenter.size() % buttonSize;
                int resDiv = hmCostCenter.size() / buttonSize;
                int totalSize = buttonSize + 4;
                funButtonArraylist(buttonSize, totalSize);
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
            btnPrevious.setEnabled(true);
            cntNavigate++;

            int buttonSize = cntNavigate * 4;
            //int resMod=hmCostCenterCode.size()%buttonSize;
            int resDiv = hmCostCenter.size() / buttonSize;
            int totalSize = buttonSize + 4;
            funButtonArraylist(buttonSize, totalSize);

            if (resDiv == cntNavigate)
            {
                btnNext.setEnabled(false);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnNextActionPerformed

    private void btnCostCenter6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCostCenter6ActionPerformed

        timer.stop();
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("MultiCostCenterKDS");
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equalsIgnoreCase(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }//CDE/Motif
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmMultiCostCenterKDS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCostCenter6ActionPerformed

    private void tblKDS1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblKDS1MouseClicked
    {//GEN-HEADEREND:event_tblKDS1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblKDS1MouseClicked

    private void btnCostCenter5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCostCenter5ActionPerformed
    {//GEN-HEADEREND:event_btnCostCenter5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCostCenter5ActionPerformed

    private void btnCostCenter7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCostCenter7ActionPerformed
    {//GEN-HEADEREND:event_btnCostCenter7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCostCenter7ActionPerformed

    private void btnCostCenter8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCostCenter8ActionPerformed
    {//GEN-HEADEREND:event_btnCostCenter8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCostCenter8ActionPerformed

    private void btnCostCenter9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCostCenter9ActionPerformed
    {//GEN-HEADEREND:event_btnCostCenter9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCostCenter9ActionPerformed

    private void btnPrevious1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrevious1ActionPerformed
    {//GEN-HEADEREND:event_btnPrevious1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrevious1ActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNext1ActionPerformed
    {//GEN-HEADEREND:event_btnNext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnCostCenter10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCostCenter10ActionPerformed
    {//GEN-HEADEREND:event_btnCostCenter10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCostCenter10ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabel12MouseClicked
    {//GEN-HEADEREND:event_jLabel12MouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblDateMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("MultiCostCenterKDS");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("MultiCostCenterKDS");
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCostCenter1;
    private javax.swing.JButton btnCostCenter10;
    private javax.swing.JButton btnCostCenter2;
    private javax.swing.JButton btnCostCenter3;
    private javax.swing.JButton btnCostCenter4;
    private javax.swing.JButton btnCostCenter5;
    private javax.swing.JButton btnCostCenter6;
    private javax.swing.JButton btnCostCenter7;
    private javax.swing.JButton btnCostCenter8;
    private javax.swing.JButton btnCostCenter9;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrevious1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblBackground1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFormName1;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPosName1;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserCode1;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblformName1;
    private javax.swing.JPanel panelCostCenter;
    private javax.swing.JPanel panelCostCenter1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelHeader1;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelMain1;
    private javax.swing.JScrollPane scrKDS;
    private javax.swing.JScrollPane scrKDS1;
    private javax.swing.JTable tblKDS;
    private javax.swing.JTable tblKDS1;
    // End of variables declaration//GEN-END:variables

}
