/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmGiftVoucherIssue extends javax.swing.JFrame
{

    private String sql, custCode, gvCode, gvAmount, customerMobileNo, giftVoucherValue, smsText;
    private Connection conRMS;
    private Statement st;
    private double custGFTotalAmount, custTotalBillAmount;
    private clsUtility objUtility;

    public frmGiftVoucherIssue()
    {
        initComponents();
        objUtility = new clsUtility();
        this.setLocationRelativeTo(null);
        try
        {
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            //txtCustomerName.requestFocus();
            funOpenRMSDBCon();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funOpenRMSDBCon()
    {
        try
        {
            String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conRMS = DriverManager.getConnection(rmsConURL);
            st = conRMS.createStatement();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funSetData(Object[] data)
    {
        txtCustomerName.setText(data[1].toString());
        try
        {
            custTotalBillAmount = 0;
            custCode = data[0].toString();
            customerMobileNo = data[2].toString();
            sql = "select strCustCode,SUM(dblGrandTotal) from tblsalehd where strCustCode='" + data[0].toString() + "' "
                    + "group by strCustCode";
            System.out.println(sql);
            ResultSet rsTotalSale = st.executeQuery(sql);
            if (rsTotalSale.next())
            {
                custTotalBillAmount = Double.parseDouble(rsTotalSale.getString(2));
            }
            rsTotalSale.close();
            DefaultTableModel dmGiftVoucher = new DefaultTableModel();
            DefaultTableModel dmGiftVoucherTotal = new DefaultTableModel();
            dmGiftVoucher.addColumn("GiftVoucher No");
            dmGiftVoucher.addColumn("GiftVoucher Amount");
            custGFTotalAmount = 0;
            //dmGiftVoucher.addColumn("Total");
            sql = "select strcustomercode,strgiftvoucherno,sum(dblbillamount) from tblgiftvoucherissue "
                    + "where strCustomerCode='" + data[0].toString() + "' group by strcustomercode,strgiftvoucherno";
            System.out.println(sql);
            ResultSet rsGFIssue = st.executeQuery(sql);
            while (rsGFIssue.next())
            {
                Object[] ob = new Object[3];
                ob[0] = rsGFIssue.getString(2);
                ob[1] = rsGFIssue.getString(3);
                custGFTotalAmount = custGFTotalAmount + (Double.parseDouble(ob[1].toString()));
                dmGiftVoucher.addRow(ob);
            }
            rsGFIssue.close();

            dmGiftVoucherTotal.addColumn("");
            dmGiftVoucherTotal.addColumn("");
            Object[] ob1 = new Object[2];
            ob1[0] = "Total";
            ob1[1] = custGFTotalAmount;
            dmGiftVoucherTotal.addRow(ob1);

            tblGiftVoucher.setModel(dmGiftVoucher);
            tblGFTotal.setModel(dmGiftVoucherTotal);
            tblGiftVoucher.setSize(400, 400);
            tblGiftVoucher.setRowHeight(25);
            tblGFTotal.setRowHeight(25);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblGiftVoucher.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblGiftVoucher.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblGiftVoucher.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblGFTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblGFTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblGFTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblGFTotal.getColumnModel().getColumn(1).setPreferredWidth(200);
            txtTotalBillAmount.setText(String.valueOf(custTotalBillAmount - custGFTotalAmount));
            txtGiftVoucherNo.requestFocus();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
    }

    public void funResetField()
    {
        try
        {
            DefaultTableModel dm = (DefaultTableModel) tblGiftVoucher.getModel();
            int cnt = 0;
            int rowCount = tblGiftVoucher.getRowCount();
            while (cnt < rowCount)
            {
                dm.removeRow(0);
                cnt++;
            }
            tblGiftVoucher.setModel(dm);
            btnSave.setText("SAVE");
            txtCustomerName.setText("");
            txtGiftVoucherNo.setText("");

            DefaultTableModel dm1 = (DefaultTableModel) tblGFTotal.getModel();
            cnt = 0;
            rowCount = tblGFTotal.getRowCount();
            while (cnt < rowCount)
            {
                dm1.removeRow(0);
                cnt++;
            }
            txtGiftVoucherAmount.setText("");
            txtTotalBillAmount.setText("0.00");
            txtCustomerName.requestFocus();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funIssueGiftVoucher()
    {
        try
        {
            double billAmt = Double.parseDouble(txtTotalBillAmount.getText());
            double giftVAmt = Double.parseDouble(txtGiftVoucherAmount.getText());
            if (giftVAmt > billAmt)
            {
                new frmOkPopUp(this, "<html>The bill amount is not<br> enough for Gift Voucher</html>", "Successfull", 3).setVisible(true);
                return;
            }

            if (funCheckDupGFNo(txtGiftVoucherNo.getText().trim()))
            {
                if (funValidateGVNo(txtGiftVoucherNo.getText().trim()))
                {
                    conRMS.setAutoCommit(false);
                    sql = "INSERT INTO tblGiftVoucherIssue(strGiftVoucherCode,strGiftVoucherNo,strCustomerCode,"
                            + "dblBillAmount,dblGiftVoucherAmount,strUserCreated,strUserEdited,dteDateCreated,"
                            + "dteDateEdited) "
                            + "VALUES ('" + gvCode + "','" + txtGiftVoucherNo.getText().trim() + "','" + custCode + "',"
                            + txtGiftVoucherAmount.getText().trim() + "," + gvAmount + ",'" + clsGlobalVarClass.gUserCode + "'," + "'"
                            + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"
                            + clsGlobalVarClass.getCurrentDateTime() + "')";
                    System.out.println(sql);
                    int insert = st.executeUpdate(sql);
                    clsGlobalVarClass.dbMysql.execute(sql);
                    if (insert > 0)
                    {
                        String date = clsGlobalVarClass.getCurrentDateTime();
                        StringBuilder sb = new StringBuilder(date);
                        date = sb.delete(sb.indexOf(" "), sb.length()).toString();
                        smsText = "Dear Sanskar Shopper Congratulations!!! you have just won Rs." + giftVoucherValue
                                + " Sanskar Gift Voucher. Your Gift Voucher No is " + txtGiftVoucherNo.getText().trim()
                                + ". Thank You Visit Again.";
                        ArrayList<String> mobileNoList = new ArrayList<>();
                        mobileNoList.add(customerMobileNo);
                        clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsText);
                        objSMSSender.start();

                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                    }
                    conRMS.commit();
                }
            }

        }
        catch (Exception e)
        {
            try
            {
                conRMS.rollback();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void funSelectCustomer()
    {
        funResetField();
        objUtility.funCallForSearchForm("CustomerMaster");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }

    private boolean funCheckDupGFNo(String gfNo)
    {
        boolean flgGFNo = true;
        try
        {
            funOpenRMSDBCon();
            sql = "select count(strGiftVoucherNo) from tblgiftvoucherissue where strGiftVoucherNo='" + gfNo + "'";
            ResultSet rsGFNo = st.executeQuery(sql);
            if (rsGFNo.next())
            {
                if (rsGFNo.getInt(1) > 0)
                {
                    flgGFNo = false;
                    new frmOkPopUp(this, "Gift Voucher no is already used", "Error", 0).setVisible(true);
                }
            }
            rsGFNo.close();

        }
        catch (Exception e)
        {
            flgGFNo = false;
            e.printStackTrace();
        }
        finally
        {
            return flgGFNo;
        }
    }

    public boolean funValidateGVNo(String giftVNo)
    {
        boolean flgGFNo = true;
        try
        {
            StringBuilder sb = new StringBuilder(giftVNo);
            String seriesCode = sb.substring(0, 3);
            String gfNo = sb.substring(3, giftVNo.length());
            if (txtGiftVoucherNo.getText().trim().length() == 0)
            {
                new frmOkPopUp(null, "Invalid Gift Voucher", "Warning", 1).setVisible(true);
                txtGiftVoucherNo.requestFocus();
                flgGFNo = false;
            }
            else if (!clsGlobalVarClass.validateIntegers(gfNo))
            {
                new frmOkPopUp(null, "Enter numbers only", "Warning", 1).setVisible(true);
                txtGiftVoucherNo.requestFocus();
                flgGFNo = false;
            }
            else if (seriesCode.equalsIgnoreCase("SBA") || seriesCode.equalsIgnoreCase("SBB") || seriesCode.equalsIgnoreCase("SBC"))
            {
                flgGFNo = true;
                sql = "select strGiftVoucherCode,intGiftVoucherStartNo,intGiftVoucherEndNo,strGiftVoucherValueType"
                        + ",dblGiftVoucherValue,dteValidFrom,dteValidTo "
                        + "from tblgiftvouchermaster where strGiftVoucherSeries='" + seriesCode + "'";
                System.out.println(sql);
                ResultSet rsGiftVoucherdtl = st.executeQuery(sql);
                if (rsGiftVoucherdtl.next())
                {
                    gvCode = rsGiftVoucherdtl.getString(1);
                    gvAmount = rsGiftVoucherdtl.getString(5);

                    int giftVoucherSeriesStartNo = rsGiftVoucherdtl.getInt(2);
                    int giftVoucherSeriesEndNo = rsGiftVoucherdtl.getInt(3);
                    String giftVoucherValueType = rsGiftVoucherdtl.getString(4);
                    giftVoucherValue = rsGiftVoucherdtl.getString(5);
                    String validFrom = rsGiftVoucherdtl.getString(6);
                    String validTo = rsGiftVoucherdtl.getString(7);
                    int giftVoucherNo = Integer.parseInt(gfNo);

                    if (giftVoucherNo >= giftVoucherSeriesStartNo && giftVoucherNo <= giftVoucherSeriesEndNo)
                    {
                        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dtPOSDate = dFormat.parse(clsGlobalVarClass.getCurrentDateTime());
                        long posTime = dtPOSDate.getTime();

                        dFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dtGiftVoucherValidTo = dFormat.parse(validTo);
                        long gfValidToTime = dtGiftVoucherValidTo.getTime();

                        dFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dtGiftVoucherValidFrom = dFormat.parse(validFrom);
                        long gfValidFromTime = dtGiftVoucherValidFrom.getTime();

                        if ((gfValidToTime - posTime) >= 0 && (posTime - gfValidFromTime) >= 0)
                        {
                            flgGFNo = true;
                            if (seriesCode.equalsIgnoreCase("SBA"))
                            {
                                txtGiftVoucherAmount.setText("20000");
                            }
                            else if (seriesCode.equalsIgnoreCase("SBB"))
                            {
                                txtGiftVoucherAmount.setText("15000");
                            }
                            else if (seriesCode.equalsIgnoreCase("SBC"))
                            {
                                txtGiftVoucherAmount.setText("10000");
                            }
                            rsGiftVoucherdtl.close();
                        }
                        else
                        {
                            flgGFNo = false;
                            new frmOkPopUp(null, "This Gift Voucher is Expired.", "Warning", 1).setVisible(true);
                        }
                    }
                    else
                    {
                        flgGFNo = false;
                        new frmOkPopUp(null, "Invalid Gift Voucher No.", "Warning", 1).setVisible(true);
                    }
                }
            }
            else
            {
                flgGFNo = false;
                new frmOkPopUp(this, "Invalid Gift Voucher", "Error", 0).setVisible(true);
            }
        }
        catch (Exception e)
        {
            flgGFNo = false;
            e.printStackTrace();
        }
        finally
        {
            return flgGFNo;
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
        panelFormBody1 = new javax.swing.JPanel();
        lblCustomerName = new javax.swing.JLabel();
        lblGiftVoucherNo = new javax.swing.JLabel();
        txtGiftVoucherNo = new javax.swing.JTextField();
        txtCustomerName = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblHeader = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        lblTotalBillAmount = new javax.swing.JLabel();
        txtTotalBillAmount = new javax.swing.JTextField();
        scrGiftVoucher = new javax.swing.JScrollPane();
        tblGiftVoucher = new javax.swing.JTable();
        scrGFTotal = new javax.swing.JScrollPane();
        tblGFTotal = new javax.swing.JTable();
        txtGiftVoucherAmount = new javax.swing.JTextField();

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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Gift Voucher Issue");
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

        panelFormBody1.setBackground(new java.awt.Color(255, 255, 255));
        panelFormBody1.setOpaque(false);
        panelFormBody1.setPreferredSize(new java.awt.Dimension(610, 600));
        panelFormBody1.setLayout(null);

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Customer Name   :");
        panelFormBody1.add(lblCustomerName);
        lblCustomerName.setBounds(90, 120, 120, 30);

        lblGiftVoucherNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGiftVoucherNo.setText("Gift Voucher No   :");
        panelFormBody1.add(lblGiftVoucherNo);
        lblGiftVoucherNo.setBounds(90, 390, 120, 30);

        txtGiftVoucherNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGiftVoucherNoMouseClicked(evt);
            }
        });
        txtGiftVoucherNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGiftVoucherNoKeyPressed(evt);
            }
        });
        panelFormBody1.add(txtGiftVoucherNo);
        txtGiftVoucherNo.setBounds(240, 390, 170, 30);

        txtCustomerName.setEditable(false);
        txtCustomerName.setEnabled(false);
        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
            }
        });
        txtCustomerName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCustomerNameKeyPressed(evt);
            }
        });
        panelFormBody1.add(txtCustomerName);
        txtCustomerName.setBounds(240, 120, 410, 30);

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnSave);
        btnSave.setBounds(430, 510, 90, 40);

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnReset);
        btnReset.setBounds(560, 510, 90, 40);

        lblHeader.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(24, 19, 19));
        lblHeader.setText("Gift Voucher Issue");
        panelFormBody1.add(lblHeader);
        lblHeader.setBounds(280, 30, 230, 30);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnCancel);
        btnCancel.setBounds(690, 510, 90, 40);

        lblTotalBillAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotalBillAmount.setText("Total Bill Amount  :");
        panelFormBody1.add(lblTotalBillAmount);
        lblTotalBillAmount.setBounds(90, 330, 120, 30);

        txtTotalBillAmount.setEditable(false);
        txtTotalBillAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalBillAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTotalBillAmountMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtTotalBillAmount);
        txtTotalBillAmount.setBounds(240, 330, 170, 30);

        tblGiftVoucher.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String []
            {
                "GiftVoucher No", "GiftVoucher Amount", "Total"
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
        scrGiftVoucher.setViewportView(tblGiftVoucher);

        panelFormBody1.add(scrGiftVoucher);
        scrGiftVoucher.setBounds(240, 170, 410, 90);

        tblGFTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrGFTotal.setViewportView(tblGFTotal);

        panelFormBody1.add(scrGFTotal);
        scrGFTotal.setBounds(240, 262, 410, 40);

        txtGiftVoucherAmount.setEditable(false);
        txtGiftVoucherAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGiftVoucherAmountMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtGiftVoucherAmount);
        txtGiftVoucherAmount.setBounds(460, 390, 170, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelFormBody1, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelFormBody1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtGiftVoucherNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGiftVoucherNoMouseClicked
        // TODO add your handling code here:
        if (!clsGlobalVarClass.gClientCode.equals("009.001"))
        {
            if (txtGiftVoucherNo.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Menu Head Name").setVisible(true);
                txtGiftVoucherNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtGiftVoucherNo.getText(), "1", "Enter Menu Head Name").setVisible(true);
                txtGiftVoucherNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtGiftVoucherNoMouseClicked

    private void txtGiftVoucherNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGiftVoucherNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (funCheckDupGFNo(txtGiftVoucherNo.getText().trim()))
            {
                funValidateGVNo(txtGiftVoucherNo.getText().trim());
            }
        }
    }//GEN-LAST:event_txtGiftVoucherNoKeyPressed

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerNameMouseClicked
        // TODO add your handling code here:
        funSelectCustomer();
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtCustomerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funSelectCustomer();
        }
    }//GEN-LAST:event_txtCustomerNameKeyPressed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        funIssueGiftVoucher();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
            objUtility = null;
            conRMS.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("GiftVoucherIssue");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtTotalBillAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTotalBillAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBillAmountMouseClicked

    private void txtGiftVoucherAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGiftVoucherAmountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiftVoucherAmountMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("GiftVoucherIssue");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("GiftVoucherIssue");
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
            java.util.logging.Logger.getLogger(frmGiftVoucherIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmGiftVoucherIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmGiftVoucherIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmGiftVoucherIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmGiftVoucherIssue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblGiftVoucherNo;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTotalBillAmount;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelFormBody1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrGFTotal;
    private javax.swing.JScrollPane scrGiftVoucher;
    private javax.swing.JTable tblGFTotal;
    private javax.swing.JTable tblGiftVoucher;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtGiftVoucherAmount;
    private javax.swing.JTextField txtGiftVoucherNo;
    private javax.swing.JTextField txtTotalBillAmount;
    // End of variables declaration//GEN-END:variables
}
