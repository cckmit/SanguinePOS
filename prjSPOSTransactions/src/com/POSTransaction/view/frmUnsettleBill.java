/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;

public class frmUnsettleBill extends javax.swing.JFrame
{

    private String sql;
    private String[] arrReasonCode;
    String poscode, dtBillDate;
    private clsUtility objUtility;
    public java.util.Vector vSettledBills, vReasonNames;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    private String selectedBillNo = "", selctedReasonCode = "";
    private Map<String, String> mapReasons;

    public frmUnsettleBill()
    {
        objUtility = new clsUtility();
        initComponents();
        poscode = clsGlobalVarClass.gPOSCode;
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

        vReasonNames = new Vector();
        vSettledBills = new Vector();
        btnPreviousReason.setEnabled(false);
        btnPreviousBillNo.setEnabled(false);
        cntNavigate = 0;
        cntNavigate1 = 0;

        lblSelectedBillNo.setText("");
        lblSelectedReason.setText("");

        mapReasons = new HashMap<String, String>();

        funLoadSettledBills("");
        funLoadReasons("");
    }

    private void funUnsettleBill()
    {
        try
        {
            String selectedBillNo = lblSelectedBillNo.getText().trim();

            selectedBillNo = selectedBillNo.replaceAll("<html>", "");
            selectedBillNo = selectedBillNo.replaceAll("<h5>", "");
            selectedBillNo = selectedBillNo.replaceAll("</h5>", "");
            selectedBillNo = selectedBillNo.replaceAll("</html>", "");

            if (selectedBillNo.length() == 0)
            {
                new frmOkPopUp(this, "Please Select Bill", "Warning", 1).setVisible(true);
                return;
            }

            String reasonName = lblSelectedReason.getText();
            reasonName = reasonName.replaceAll("<html>", "");
            reasonName = reasonName.replaceAll("<h5>", "");
            reasonName = reasonName.replaceAll("</h5>", "");
            reasonName = reasonName.replaceAll("</html>", "");
            String reasonCode = mapReasons.get(reasonName);

            if (reasonName.trim().length() == 0)
            {
                new frmOkPopUp(this, "Please Select Reason", "Warning", 1).setVisible(true);
                return;
            }
            sql = "select dteBillDate,dblGrandTotal,strClientCode,strTableNo,strWaiterNo,strAreaCode"
                    + ",strPosCode,strOperationType,strSettelmentMode,intShiftCode "
                    + " from tblbillhd where strBillNo='" + selectedBillNo + "'";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rs.next())
            {
                sql = "insert into tblvoidbillhd(strPosCode,strReasonCode,strReasonName,strBillNo,"
                        + "dblActualAmount,dteBillDate,strTransType,dteModifyVoidBill,strTableNo,strWaiterNo,"
                        + "intShiftCode,strUserCreated,strUserEdited,strClientCode)"
                        + " values('" + clsGlobalVarClass.gPOSCode + "','" + reasonCode + "','" + reasonName + "'"
                        + ",'" + selectedBillNo + "','" + rs.getString(2) + "','" + rs.getString(1) + "'"
                        + ",'USBill','" + clsGlobalVarClass.getCurrentDateTime() + "','" + rs.getString(4) + "','" + rs.getString(5) + "'"
                        + ",'" + rs.getString(10) + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gClientCode + "')";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "select strBillNo from tblbillsettlementdtl a,tblsettelmenthd b "
                        + " where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='" + selectedBillNo + "' "
                        + " and b.strSettelmentType='Debit Card' ";
                ResultSet rsDebitCardBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsDebitCardBill.next())
                {
                    sql = "select strCardNo,dblTransactionAmt,strPOSCode,dteBillDate "
                            + " from tbldebitcardbilldetails where strBillNo='" + selectedBillNo + "' ";
                    ResultSet rsDebitCardBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsDebitCardBillDtl.next())
                    {
                        objUtility.funDebitCardTransaction(selectedBillNo, rsDebitCardBillDtl.getString(1), rsDebitCardBillDtl.getDouble(2), "Unsettle");
                        objUtility.funUpdateDebitCardBalance(rsDebitCardBillDtl.getString(1), rsDebitCardBillDtl.getDouble(2), "Unsettle");
                    }
                    rsDebitCardBillDtl.close();
                }
                rsDebitCardBill.close();

                sql = "select b.strSettelmentType from tblbillsettlementdtl a,tblsettelmenthd b "
                        + " where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='" + selectedBillNo + "' ";
                ResultSet rsSettlementMode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsSettlementMode.next())
                {
                    if (rsSettlementMode.getString(1).trim().equals("Complementary"))
                    {
                        funMoveComplimentaryBillToBillDtl(selectedBillNo, rs.getString(7), rs.getString(6), rs.getString(8));
                    }
                }
                rsSettlementMode.close();

                sql = "delete from tblbillsettlementdtl where strBillNo='" + selectedBillNo + "'";
                int unsettleExc = clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbillhd set strSettelmentMode='',strDataPostFlag='N' where strBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbilldtl set strDataPostFlag='N' where strBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbillmodifierdtl set strDataPostFlag='N' where strBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbilltaxdtl set strDataPostFlag='N' where strBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbillseriesbilldtl set strDataPostFlag='N' where strHdBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "update tblbilldiscdtl set strDataPostFlag='N' where strBillNo='" + selectedBillNo + "'";
                clsGlobalVarClass.dbMysql.execute(sql);

                if (unsettleExc > 0)
                {
                    new frmOkPopUp(this, "Unsettle Successfully", "Success", 1).setVisible(true);

                    //send unsettleed bill MSG                   
                    sql = "select a.strSendSMSYN,a.longMobileNo "
                            + "from tblsmssetup a "
                            + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                            + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
                            + "and a.strTransactionName='UnsettleBill' "
                            + "and a.strSendSMSYN='Y'; ";
                    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsSendSMS.next())
                    {
                        String mobileNo = rsSendSMS.getString(2);//mobileNo

                        funSendUnSettleBillSMS(selectedBillNo, mobileNo);

                    }
                    rsSendSMS.close();

                    funResetAll();
                }
            }
        }
        catch (Exception e)
        {
            //clsGlobalVarClass.dbMysql.funRollbackTransaction();
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private int funMoveComplimentaryBillToBillDtl(String billNo, String POSCode, String billAreaCode, String operationTypeForTax) throws Exception
    {
        String sqlDelete = "delete from tblbilldtl where strBillNo='" + billNo + "' and dtBillDate='"+clsGlobalVarClass.gPOSOnlyDateForTransaction+"' ";
        clsGlobalVarClass.dbMysql.execute(sqlDelete);

        String sqlInsertBillComDtl = "insert into tblbilldtl "
                + " select a.strItemCode,a.strItemName,a.strBillNo,a.strAdvBookingNo,a.dblRate \n"
                + ",a.dblQuantity,a.dblAmount,a.dblTaxAmount,a.dteBillDate,a.strKOTNo\n"
                + ",a.strClientCode,a.strCustomerCode,a.tmeOrderProcessing,a.strDataPostFlag,a.strMMSDataPostFlag\n"
                + ",a.strManualKOTNo,a.tdhYN,a.strPromoCode,a.strCounterCode,a.strWaiterNo\n"
                + ",a.dblDiscountAmt,a.dblDiscountPer,a.strSequenceNo,a.dtBillDate,a.tmeOrderPickup\n"
                + "from tblbillcomplementrydtl a  "
                + "where a.strBillNo='" + billNo + "' "
                + "and a.dtBillDate='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
        clsGlobalVarClass.dbMysql.execute(sqlInsertBillComDtl);

        String sql = " select strItemCode,strItemName,dblAmount,dblDiscountAmt "
                + "from tblbilldtl where strBillNo='" + billNo + "' and dtBillDate='"+clsGlobalVarClass.gPOSOnlyDateForTransaction+"' ";
        double subTotal = 0.0;
        Double disTotal = 0.0;

        List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rs.next())
        {
            clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
            objItemDtl.setItemCode(rs.getString(1));
            objItemDtl.setItemName(rs.getString(2));
            objItemDtl.setAmount(rs.getDouble(3));
            objItemDtl.setDiscAmt(rs.getDouble(4));
            subTotal += rs.getDouble(3);
            disTotal += rs.getDouble(4);
            arrListItemDtls.add(objItemDtl);
        }
        double disper = 0.00;
        if (subTotal > 0)
        {
            disper = (disTotal / subTotal) * 100;
        }
        clsUtility obj = new clsUtility();
        List<clsTaxCalculationDtls> arrListTaxCal = obj.funCalculateTax(arrListItemDtls, POSCode, "", billAreaCode, operationTypeForTax, 0, 0, "Tax Regen", "S01","Sales");
        sqlDelete = "delete from tblbilltaxdtl where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
        double taxAmt = 0.0;
        clsGlobalVarClass.dbMysql.execute(sqlDelete);
        for (clsTaxCalculationDtls objTaxCalDtl : arrListTaxCal)
        {
            String sqlInsertTaxDtl = "insert into tblbilltaxdtl "
                    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode) "
                    + "values('" + billNo + "','" + objTaxCalDtl.getTaxCode() + "'"
                    + "," + objTaxCalDtl.getTaxableAmount() + "," + objTaxCalDtl.getTaxAmount() + ""
                    + ",'" + clsGlobalVarClass.gClientCode + "')";
            taxAmt += objTaxCalDtl.getTaxAmount();
            clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
        }
        double grandTotal = ((subTotal + taxAmt) - disTotal);
        sql = "update tblbillhd set dblDiscountAmt='" + disTotal + "',dblDiscountPer='" + disper + "',"
                + "dblTaxAmt='" + taxAmt + "',dblSubTotal='" + subTotal + "',dblGrandTotal='" + grandTotal + "' "
                + "where strBillNo='" + billNo + "' "
                + "and dtBillDate='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
        clsGlobalVarClass.dbMysql.execute(sql);

        sqlDelete = "delete from tblbillcomplementrydtl where strBillNo='" + billNo + "' and dtBillDate='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
        clsGlobalVarClass.dbMysql.execute(sqlDelete);

        return 0;
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
        jLabel1 = new javax.swing.JLabel();
        txtSearchBillNo = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        panelBills = new javax.swing.JPanel();
        btnBillNo2 = new javax.swing.JButton();
        btnBillNo1 = new javax.swing.JButton();
        btnBillNo3 = new javax.swing.JButton();
        btnBillNo4 = new javax.swing.JButton();
        btnBillNo5 = new javax.swing.JButton();
        btnBillNo6 = new javax.swing.JButton();
        btnBillNo7 = new javax.swing.JButton();
        btnBillNo8 = new javax.swing.JButton();
        btnBillNo9 = new javax.swing.JButton();
        btnBillNo10 = new javax.swing.JButton();
        btnBillNo11 = new javax.swing.JButton();
        btnBillNo12 = new javax.swing.JButton();
        btnBillNo13 = new javax.swing.JButton();
        btnBillNo14 = new javax.swing.JButton();
        btnBillNo15 = new javax.swing.JButton();
        btnBillNo16 = new javax.swing.JButton();
        btnPreviousBillNo = new javax.swing.JButton();
        btnNextBillNo = new javax.swing.JButton();
        panelReasons = new javax.swing.JPanel();
        btnReasonNo2 = new javax.swing.JButton();
        btnReasonNo1 = new javax.swing.JButton();
        btnReasonNo3 = new javax.swing.JButton();
        btnReasonNo4 = new javax.swing.JButton();
        btnReasonNo5 = new javax.swing.JButton();
        btnReasonNo6 = new javax.swing.JButton();
        btnReasonNo7 = new javax.swing.JButton();
        btnReasonNo8 = new javax.swing.JButton();
        btnReasonNo9 = new javax.swing.JButton();
        btnReasonNo10 = new javax.swing.JButton();
        btnReasonNo11 = new javax.swing.JButton();
        btnReasonNo12 = new javax.swing.JButton();
        btnReasonNo13 = new javax.swing.JButton();
        btnReasonNo14 = new javax.swing.JButton();
        btnReasonNo15 = new javax.swing.JButton();
        btnReasonNo16 = new javax.swing.JButton();
        btnPreviousReason = new javax.swing.JButton();
        btnNextReason = new javax.swing.JButton();
        lblSelectedReason = new javax.swing.JLabel();
        lblSelectedBillNo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

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
        lblformName.setText("- Unsettle Bill");
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
        lblFormName.setText("Unsettle Bill");

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
        btnUnsettle.setText("UNSETTLE");
        btnUnsettle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUnsettle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnUnsettle.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUnsettleMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Bill No. :");

        txtSearchBillNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchBillNoMouseClicked(evt);
            }
        });
        txtSearchBillNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchBillNoKeyReleased(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");

        panelBills.setBackground(new java.awt.Color(255, 255, 255));
        panelBills.setEnabled(false);
        panelBills.setOpaque(false);

        btnBillNo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo2MouseClicked(evt);
            }
        });

        btnBillNo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo1MouseClicked(evt);
            }
        });

        btnBillNo3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo3MouseClicked(evt);
            }
        });

        btnBillNo4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo4MouseClicked(evt);
            }
        });

        btnBillNo5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo5MouseClicked(evt);
            }
        });

        btnBillNo6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo6MouseClicked(evt);
            }
        });

        btnBillNo7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo7MouseClicked(evt);
            }
        });

        btnBillNo8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo8MouseClicked(evt);
            }
        });

        btnBillNo9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo9MouseClicked(evt);
            }
        });

        btnBillNo10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo10MouseClicked(evt);
            }
        });

        btnBillNo11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo11MouseClicked(evt);
            }
        });

        btnBillNo12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo12MouseClicked(evt);
            }
        });

        btnBillNo13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo13MouseClicked(evt);
            }
        });

        btnBillNo14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo14MouseClicked(evt);
            }
        });

        btnBillNo15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo15MouseClicked(evt);
            }
        });

        btnBillNo16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBillNo16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNo16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNo16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelBillsLayout = new javax.swing.GroupLayout(panelBills);
        panelBills.setLayout(panelBillsLayout);
        panelBillsLayout.setHorizontalGroup(
            panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillsLayout.createSequentialGroup()
                .addComponent(btnBillNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBillNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBillNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBillNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelBillsLayout.createSequentialGroup()
                .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBillsLayout.createSequentialGroup()
                        .addComponent(btnBillNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillsLayout.createSequentialGroup()
                        .addComponent(btnBillNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillsLayout.createSequentialGroup()
                        .addComponent(btnBillNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelBillsLayout.setVerticalGroup(
            panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillsLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBillNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBillsLayout.createSequentialGroup()
                            .addComponent(btnBillNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnBillNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBillNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBillNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        btnPreviousBillNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPreviousBillNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPreviousBillNo.setText("<<<");
        btnPreviousBillNo.setToolTipText("Previous Bill No");
        btnPreviousBillNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreviousBillNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPreviousBillNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousBillNoActionPerformed(evt);
            }
        });

        btnNextBillNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextBillNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextBillNo.setText(">>>");
        btnNextBillNo.setToolTipText("Next Bill No");
        btnNextBillNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextBillNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextBillNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextBillNoActionPerformed(evt);
            }
        });

        panelReasons.setBackground(new java.awt.Color(255, 255, 255));
        panelReasons.setEnabled(false);
        panelReasons.setOpaque(false);

        btnReasonNo2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo2MouseClicked(evt);
            }
        });

        btnReasonNo1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo1MouseClicked(evt);
            }
        });

        btnReasonNo3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo3MouseClicked(evt);
            }
        });

        btnReasonNo4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo4.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo4MouseClicked(evt);
            }
        });

        btnReasonNo5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo5MouseClicked(evt);
            }
        });

        btnReasonNo6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo6MouseClicked(evt);
            }
        });

        btnReasonNo7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo7MouseClicked(evt);
            }
        });

        btnReasonNo8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo8.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo8MouseClicked(evt);
            }
        });

        btnReasonNo9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo9MouseClicked(evt);
            }
        });

        btnReasonNo10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo10.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo10MouseClicked(evt);
            }
        });

        btnReasonNo11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo11.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo11MouseClicked(evt);
            }
        });

        btnReasonNo12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo12.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo12MouseClicked(evt);
            }
        });

        btnReasonNo13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo13.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo13MouseClicked(evt);
            }
        });

        btnReasonNo14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo14.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo14MouseClicked(evt);
            }
        });

        btnReasonNo15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo15.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo15MouseClicked(evt);
            }
        });

        btnReasonNo16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReasonNo16.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnReasonNo16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReasonNo16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnReasonNo16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelReasonsLayout = new javax.swing.GroupLayout(panelReasons);
        panelReasons.setLayout(panelReasonsLayout);
        panelReasonsLayout.setHorizontalGroup(
            panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReasonsLayout.createSequentialGroup()
                .addComponent(btnReasonNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReasonNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReasonNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReasonNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelReasonsLayout.createSequentialGroup()
                .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReasonsLayout.createSequentialGroup()
                        .addComponent(btnReasonNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReasonsLayout.createSequentialGroup()
                        .addComponent(btnReasonNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReasonsLayout.createSequentialGroup()
                        .addComponent(btnReasonNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReasonNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelReasonsLayout.setVerticalGroup(
            panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReasonsLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnReasonNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReasonNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelReasonsLayout.createSequentialGroup()
                            .addComponent(btnReasonNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnReasonNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReasonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnReasonNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReasonNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        btnPreviousReason.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPreviousReason.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPreviousReason.setText("<<<");
        btnPreviousReason.setToolTipText("Previous Open KOTs");
        btnPreviousReason.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreviousReason.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPreviousReason.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousReasonActionPerformed(evt);
            }
        });

        btnNextReason.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextReason.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextReason.setText(">>>");
        btnNextReason.setToolTipText("Next Open KOTs");
        btnNextReason.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextReason.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextReason.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextReasonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 255));
        jLabel2.setText("Reasons ");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(lblSearch))))
                    .addComponent(panelBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(btnPreviousBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(lblSelectedBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnNextBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelFormBodyLayout.createSequentialGroup()
                                    .addComponent(btnUnsettle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelFormBodyLayout.createSequentialGroup()
                                    .addComponent(btnPreviousReason, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblSelectedReason, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(btnNextReason, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(panelReasons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(325, 325, 325)
                .addComponent(lblFormName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSearch))
                        .addGap(10, 10, 10)
                        .addComponent(panelBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPreviousBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNextBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSelectedBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelReasons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPreviousReason, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNextReason, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSelectedReason, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUnsettle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Unsettle Bill");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnUnsettleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnsettleMouseClicked
        // TODO add your handling code here:
        funUnsettleBill();
    }//GEN-LAST:event_btnUnsettleMouseClicked

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblProductNameMouseClicked

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

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Unsettle Bill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Unsettle Bill");
    }//GEN-LAST:event_formWindowClosing

    private void txtSearchBillNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSearchBillNoMouseClicked
    {//GEN-HEADEREND:event_txtSearchBillNoMouseClicked
        // TODO add your handling code here:
        frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search Bill");
        keyboard.setVisible(true);
        keyboard.setAlwaysOnTop(true);
        keyboard.setAutoRequestFocus(true);
        txtSearchBillNo.setText(clsGlobalVarClass.gKeyboardValue);

        funLoadSettledBills(txtSearchBillNo.getText().trim());
    }//GEN-LAST:event_txtSearchBillNoMouseClicked

    private void txtSearchBillNoKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchBillNoKeyReleased
    {//GEN-HEADEREND:event_txtSearchBillNoKeyReleased

        funLoadSettledBills(txtSearchBillNo.getText().trim());
    }//GEN-LAST:event_txtSearchBillNoKeyReleased

    private void btnBillNo2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo2MouseClicked
    {//GEN-HEADEREND:event_btnBillNo2MouseClicked
        funSetSelctedBillNo(1);
    }//GEN-LAST:event_btnBillNo2MouseClicked

    private void btnBillNo1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo1MouseClicked
    {//GEN-HEADEREND:event_btnBillNo1MouseClicked
        funSetSelctedBillNo(0);
    }//GEN-LAST:event_btnBillNo1MouseClicked

    private void btnBillNo3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo3MouseClicked
    {//GEN-HEADEREND:event_btnBillNo3MouseClicked
        funSetSelctedBillNo(2);
    }//GEN-LAST:event_btnBillNo3MouseClicked

    private void btnBillNo4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo4MouseClicked
    {//GEN-HEADEREND:event_btnBillNo4MouseClicked
        funSetSelctedBillNo(3);
    }//GEN-LAST:event_btnBillNo4MouseClicked

    private void btnBillNo5MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo5MouseClicked
    {//GEN-HEADEREND:event_btnBillNo5MouseClicked
        funSetSelctedBillNo(4);
    }//GEN-LAST:event_btnBillNo5MouseClicked

    private void btnBillNo6MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo6MouseClicked
    {//GEN-HEADEREND:event_btnBillNo6MouseClicked
        funSetSelctedBillNo(5);
    }//GEN-LAST:event_btnBillNo6MouseClicked

    private void btnBillNo7MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo7MouseClicked
    {//GEN-HEADEREND:event_btnBillNo7MouseClicked
        funSetSelctedBillNo(6);
    }//GEN-LAST:event_btnBillNo7MouseClicked

    private void btnBillNo8MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo8MouseClicked
    {//GEN-HEADEREND:event_btnBillNo8MouseClicked
        funSetSelctedBillNo(7);
    }//GEN-LAST:event_btnBillNo8MouseClicked

    private void btnBillNo9MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo9MouseClicked
    {//GEN-HEADEREND:event_btnBillNo9MouseClicked
        funSetSelctedBillNo(8);
    }//GEN-LAST:event_btnBillNo9MouseClicked

    private void btnBillNo10MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo10MouseClicked
    {//GEN-HEADEREND:event_btnBillNo10MouseClicked
        funSetSelctedBillNo(9);
    }//GEN-LAST:event_btnBillNo10MouseClicked

    private void btnBillNo11MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo11MouseClicked
    {//GEN-HEADEREND:event_btnBillNo11MouseClicked
        funSetSelctedBillNo(10);
    }//GEN-LAST:event_btnBillNo11MouseClicked

    private void btnBillNo12MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo12MouseClicked
    {//GEN-HEADEREND:event_btnBillNo12MouseClicked
        funSetSelctedBillNo(11);
    }//GEN-LAST:event_btnBillNo12MouseClicked

    private void btnBillNo13MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo13MouseClicked
    {//GEN-HEADEREND:event_btnBillNo13MouseClicked
        funSetSelctedBillNo(12);
    }//GEN-LAST:event_btnBillNo13MouseClicked

    private void btnBillNo14MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo14MouseClicked
    {//GEN-HEADEREND:event_btnBillNo14MouseClicked
        funSetSelctedBillNo(13);
    }//GEN-LAST:event_btnBillNo14MouseClicked

    private void btnBillNo15MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo15MouseClicked
    {//GEN-HEADEREND:event_btnBillNo15MouseClicked
        funSetSelctedBillNo(14);
    }//GEN-LAST:event_btnBillNo15MouseClicked

    private void btnBillNo16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnBillNo16MouseClicked
    {//GEN-HEADEREND:event_btnBillNo16MouseClicked
        funSetSelctedBillNo(15);
    }//GEN-LAST:event_btnBillNo16MouseClicked

    private void btnPreviousBillNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPreviousBillNoActionPerformed
    {//GEN-HEADEREND:event_btnPreviousBillNoActionPerformed
        // TODO add your handling code here:
        funPreviousBillButtonClicked();
    }//GEN-LAST:event_btnPreviousBillNoActionPerformed

    private void btnNextBillNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextBillNoActionPerformed
    {//GEN-HEADEREND:event_btnNextBillNoActionPerformed
        // TODO add your handling code here:
        funNextBillNoButtonClicked();
    }//GEN-LAST:event_btnNextBillNoActionPerformed

    private void btnReasonNo2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo2MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo2MouseClicked
        funSetSelctedReasonNo(1);
    }//GEN-LAST:event_btnReasonNo2MouseClicked

    private void btnReasonNo1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo1MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo1MouseClicked
        funSetSelctedReasonNo(0);
    }//GEN-LAST:event_btnReasonNo1MouseClicked

    private void btnReasonNo3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo3MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo3MouseClicked
        funSetSelctedReasonNo(2);
    }//GEN-LAST:event_btnReasonNo3MouseClicked

    private void btnReasonNo4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo4MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo4MouseClicked
        funSetSelctedReasonNo(3);
    }//GEN-LAST:event_btnReasonNo4MouseClicked

    private void btnReasonNo5MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo5MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo5MouseClicked
        funSetSelctedReasonNo(4);
    }//GEN-LAST:event_btnReasonNo5MouseClicked

    private void btnReasonNo6MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo6MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo6MouseClicked
        funSetSelctedReasonNo(5);
    }//GEN-LAST:event_btnReasonNo6MouseClicked

    private void btnReasonNo7MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo7MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo7MouseClicked
        funSetSelctedReasonNo(6);
    }//GEN-LAST:event_btnReasonNo7MouseClicked

    private void btnReasonNo8MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo8MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo8MouseClicked
        funSetSelctedReasonNo(7);
    }//GEN-LAST:event_btnReasonNo8MouseClicked

    private void btnReasonNo9MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo9MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo9MouseClicked
        funSetSelctedReasonNo(8);
    }//GEN-LAST:event_btnReasonNo9MouseClicked

    private void btnReasonNo10MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo10MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo10MouseClicked
        funSetSelctedReasonNo(9);
    }//GEN-LAST:event_btnReasonNo10MouseClicked

    private void btnReasonNo11MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo11MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo11MouseClicked
        funSetSelctedReasonNo(10);
    }//GEN-LAST:event_btnReasonNo11MouseClicked

    private void btnReasonNo12MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo12MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo12MouseClicked
        funSetSelctedReasonNo(11);
    }//GEN-LAST:event_btnReasonNo12MouseClicked

    private void btnReasonNo13MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo13MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo13MouseClicked
        funSetSelctedReasonNo(12);
    }//GEN-LAST:event_btnReasonNo13MouseClicked

    private void btnReasonNo14MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo14MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo14MouseClicked
        funSetSelctedReasonNo(13);
    }//GEN-LAST:event_btnReasonNo14MouseClicked

    private void btnReasonNo15MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo15MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo15MouseClicked
        funSetSelctedReasonNo(14);
    }//GEN-LAST:event_btnReasonNo15MouseClicked

    private void btnReasonNo16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnReasonNo16MouseClicked
    {//GEN-HEADEREND:event_btnReasonNo16MouseClicked

        funSetSelctedReasonNo(15);
    }//GEN-LAST:event_btnReasonNo16MouseClicked

    private void btnPreviousReasonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPreviousReasonActionPerformed
    {//GEN-HEADEREND:event_btnPreviousReasonActionPerformed
        // TODO add your handling code here:
        funPreviousReasonButtonClicked();
    }//GEN-LAST:event_btnPreviousReasonActionPerformed

    private void btnNextReasonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextReasonActionPerformed
    {//GEN-HEADEREND:event_btnNextReasonActionPerformed
        // TODO add your handling code here:
        funNextReasonButtonClicked();
    }//GEN-LAST:event_btnNextReasonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBillNo1;
    private javax.swing.JButton btnBillNo10;
    private javax.swing.JButton btnBillNo11;
    private javax.swing.JButton btnBillNo12;
    private javax.swing.JButton btnBillNo13;
    private javax.swing.JButton btnBillNo14;
    private javax.swing.JButton btnBillNo15;
    private javax.swing.JButton btnBillNo16;
    private javax.swing.JButton btnBillNo2;
    private javax.swing.JButton btnBillNo3;
    private javax.swing.JButton btnBillNo4;
    private javax.swing.JButton btnBillNo5;
    private javax.swing.JButton btnBillNo6;
    private javax.swing.JButton btnBillNo7;
    private javax.swing.JButton btnBillNo8;
    private javax.swing.JButton btnBillNo9;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNextBillNo;
    private javax.swing.JButton btnNextReason;
    private javax.swing.JButton btnPreviousBillNo;
    private javax.swing.JButton btnPreviousReason;
    private javax.swing.JButton btnReasonNo1;
    private javax.swing.JButton btnReasonNo10;
    private javax.swing.JButton btnReasonNo11;
    private javax.swing.JButton btnReasonNo12;
    private javax.swing.JButton btnReasonNo13;
    private javax.swing.JButton btnReasonNo14;
    private javax.swing.JButton btnReasonNo15;
    private javax.swing.JButton btnReasonNo16;
    private javax.swing.JButton btnReasonNo2;
    private javax.swing.JButton btnReasonNo3;
    private javax.swing.JButton btnReasonNo4;
    private javax.swing.JButton btnReasonNo5;
    private javax.swing.JButton btnReasonNo6;
    private javax.swing.JButton btnReasonNo7;
    private javax.swing.JButton btnReasonNo8;
    private javax.swing.JButton btnReasonNo9;
    private javax.swing.JButton btnUnsettle;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSelectedBillNo;
    private javax.swing.JLabel lblSelectedReason;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBills;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelReasons;
    private javax.swing.JTextField txtSearchBillNo;
    // End of variables declaration//GEN-END:variables

    private void funLoadSettledBills(String searchBillNo)
    {
        try
        {
            String sql = "select ifnull(d.strTableName,'ND') as Table_Name, a.strBillNo as Bill_No "
                    + " ,a.dblGrandTotal as Total_Amount,c.strSettelmentDesc as Settle_Mode, a.strUserCreated as User "
                    + " , a.strRemarks as Remarks "
                    + " from tblbillhd a inner join tblbillsettlementdtl b on a.strbillno=b.strbillno "
                    + " inner join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                    + " left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
                    + " where date(a.dteBillDate)='" + objUtility.funGetOnlyPOSDateForTransaction() + "' "
                    + " and c.strSettelmentType!='Complementary' "
                    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
//            if (!clsGlobalVarClass.gSuperUser)
//            {
//                sql += " and a.strUserCreated='" + clsGlobalVarClass.gUserCode + "' ";
//            }
            if (searchBillNo.trim().length() > 0)
            {
                sql += " and a.strBillNo like '" + searchBillNo + "%' ";
            }
            sql += " group by a.strbillno order by a.strbillno DESC";

            ResultSet rsBillNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            vSettledBills.clear();
            while (rsBillNo.next())
            {
                vSettledBills.add(rsBillNo.getString(2));
            }
            rsBillNo.close();
            funLoadBillNo(0, vSettledBills.size());

            if (vSettledBills.size() <= 16)
            {
                btnNextBillNo.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadBillNo(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;
            JButton[] btnBillsArray =
            {
                btnBillNo1, btnBillNo2, btnBillNo3, btnBillNo4, btnBillNo5, btnBillNo6, btnBillNo7, btnBillNo8, btnBillNo9, btnBillNo10, btnBillNo11, btnBillNo12, btnBillNo13, btnBillNo14, btnBillNo15, btnBillNo16
            };
            for (int k = 0; k < btnBillsArray.length; k++)
            {
                btnBillsArray[k].setForeground(Color.black);
                btnBillsArray[k].setBackground(Color.LIGHT_GRAY);
                btnBillsArray[k].setText("");
            }
            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == vSettledBills.size())
                {
                    break;
                }
                String billNo = vSettledBills.elementAt(i).toString();

                if (cntIndex < 16)
                {
                    btnBillsArray[cntIndex].setText("<html><h5>" + billNo + "</h5></html>");
                    btnBillsArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            for (int j = cntIndex; j < 16; j++)
            {
                btnBillsArray[j].setEnabled(false);
            }

            for (int i = 0; i < btnBillsArray.length; i++)
            {
                if (btnBillsArray[i].getText().equals(selectedBillNo))
                {
                    btnBillsArray[i].setBackground(Color.BLUE);
                }
                else
                {
                    btnBillsArray[i].setBackground(Color.LIGHT_GRAY);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funPreviousReasonButtonClicked()
    {
        try
        {
            cntNavigate--;
            btnNextReason.setEnabled(true);
            if (cntNavigate == 0)
            {
                btnPreviousReason.setEnabled(false);
                funLoadReasons(0, vReasonNames.size());
            }
            else
            {
                int tableSize = cntNavigate * 16;
                int totalSize = tableSize + 16;
                funLoadReasons(tableSize, totalSize);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funNextReasonButtonClicked()
    {
        try
        {
            cntNavigate++;
            int tableSize = cntNavigate * 16;
            int resDiv = vReasonNames.size() / tableSize;
            int totalSize = tableSize + 16;
            funLoadReasons(tableSize, totalSize);
            btnPreviousReason.setEnabled(true);
            if (resDiv == cntNavigate)
            {
                btnNextReason.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetSelctedBillNo(int selectedButtonIndex)
    {

        JButton[] btnBillsArray =
        {
            btnBillNo1, btnBillNo2, btnBillNo3, btnBillNo4, btnBillNo5, btnBillNo6, btnBillNo7, btnBillNo8, btnBillNo9, btnBillNo10, btnBillNo11, btnBillNo12, btnBillNo13, btnBillNo14, btnBillNo15, btnBillNo16
        };

        lblSelectedBillNo.setText(btnBillsArray[selectedButtonIndex].getText());

        for (int i = 0; i < btnBillsArray.length; i++)
        {
            if (btnBillsArray[i].getText().equals(lblSelectedBillNo.getText()))
            {
                btnBillsArray[i].setBackground(Color.BLUE);
            }
            else
            {
                btnBillsArray[i].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void funPreviousBillButtonClicked()
    {
        try
        {
            cntNavigate1--;
            btnNextBillNo.setEnabled(true);
            if (cntNavigate1 == 0)
            {
                btnPreviousBillNo.setEnabled(false);
                funLoadBillNo(0, vSettledBills.size());
            }
            else
            {
                int tableSize = cntNavigate1 * 16;
                int totalSize = tableSize + 16;
                funLoadBillNo(tableSize, totalSize);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funNextBillNoButtonClicked()
    {
        try
        {
            cntNavigate1++;
            int tableSize = cntNavigate1 * 16;
            int resDiv = vSettledBills.size() / 16;
            int totalSize = tableSize + 16;
            tblStartIndex = tableSize;
            tblEndIndex = totalSize;
            funLoadBillNo(tableSize, totalSize);
            btnPreviousBillNo.setEnabled(true);
            if (resDiv == cntNavigate1)
            {
                btnNextBillNo.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetSelctedReasonNo(int selectedButtonIndex)
    {
        JButton[] btnReasonsArray =
        {
            btnReasonNo1, btnReasonNo2, btnReasonNo3, btnReasonNo4, btnReasonNo5, btnReasonNo6, btnReasonNo7, btnReasonNo8, btnReasonNo9, btnReasonNo10, btnReasonNo11, btnReasonNo12, btnReasonNo13, btnReasonNo14, btnReasonNo15, btnReasonNo16
        };

        selctedReasonCode = mapReasons.get(btnReasonsArray[selectedButtonIndex].getText());
        lblSelectedReason.setText(btnReasonsArray[selectedButtonIndex].getText());

        for (int i = 0; i < btnReasonsArray.length; i++)
        {
            if (btnReasonsArray[i].getText().equals(lblSelectedReason.getText()))
            {
                btnReasonsArray[i].setBackground(Color.BLUE);
            }
            else
            {
                btnReasonsArray[i].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void funLoadReasons(String string)
    {
        try
        {

            btnPreviousReason.setEnabled(false);
            btnNextReason.setEnabled(true);
            vReasonNames.removeAllElements();

            mapReasons.clear();
            sql = "select strReasonCode,strReasonName from tblreasonmaster where strUnsettleBill='Y' ";
            ResultSet rsKOTNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsKOTNo.next())
            {
                mapReasons.put(rsKOTNo.getString(2), rsKOTNo.getString(1));

                vReasonNames.add(rsKOTNo.getString(2));
            }
            rsKOTNo.close();
            funLoadReasons(0, vReasonNames.size());

            if (vReasonNames.size() <= 16)
            {
                btnNextReason.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadReasons(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;
            JButton[] btnReasonsArray =
            {
                btnReasonNo1, btnReasonNo2, btnReasonNo3, btnReasonNo4, btnReasonNo5, btnReasonNo6, btnReasonNo7, btnReasonNo8, btnReasonNo9, btnReasonNo10, btnReasonNo11, btnReasonNo12, btnReasonNo13, btnReasonNo14, btnReasonNo15, btnReasonNo16
            };
            for (int k = 0; k < btnReasonsArray.length; k++)
            {
                btnReasonsArray[k].setForeground(Color.black);
                btnReasonsArray[k].setBackground(Color.LIGHT_GRAY);
                btnReasonsArray[k].setText("");
            }
            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == vReasonNames.size())
                {
                    break;
                }
                String kotNo = vReasonNames.elementAt(i).toString();

                if (cntIndex < 16)
                {
                    btnReasonsArray[cntIndex].setText("<html><h5>" + kotNo + "</h5></html>");
                    btnReasonsArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            for (int j = cntIndex; j < 16; j++)
            {
                btnReasonsArray[j].setEnabled(false);
            }

            for (int i = 0; i < btnReasonsArray.length; i++)
            {
                if (btnReasonsArray[i].getText().equals(selctedReasonCode))
                {
                    btnReasonsArray[i].setBackground(Color.BLUE);
                }
                else
                {
                    btnReasonsArray[i].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funResetAll()
    {
        vReasonNames = new Vector();
        vSettledBills = new Vector();
        btnPreviousReason.setEnabled(false);
        btnPreviousBillNo.setEnabled(false);
        cntNavigate = 0;
        cntNavigate1 = 0;

        lblSelectedBillNo.setText("");
        lblSelectedReason.setText("");

        selectedBillNo = "";
        selctedReasonCode = "";

        mapReasons = new HashMap<String, String>();

        funLoadSettledBills("");
        funLoadReasons("");
    }

    private void funSendUnSettleBillSMS(String billNo, String mobileNo)
    {

        try
        {
            clsUtility2 objUtility2 = new clsUtility2();
            StringBuilder mainSMSBuilder = new StringBuilder();

            mainSMSBuilder.append("UnsettleBill");
            mainSMSBuilder.append(",Bill No:" + billNo);
            mainSMSBuilder.append(",POS:" + clsGlobalVarClass.gPOSName);
            mainSMSBuilder.append(",User:" + clsGlobalVarClass.gUserCode);

            String sql = "select a.strBillNo,a.strReasonName,a.strUserEdited,a.strRemark  "
                    + "from tblvoidbillhd a  "
                    + "where a.strTransType='USBill'  "
                    + "and strBillNo='" + billNo + "' ";
            ResultSet rsModBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsModBill.next())
            {
                mainSMSBuilder.append(",Reason:" + rsModBill.getString(2));
                mainSMSBuilder.append(",Remarks:" + rsModBill.getString(4));
            }
            rsModBill.close();

            ArrayList<String> mobileNoList = new ArrayList<>();
            mobileNoList.add(mobileNo);
            clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
            objSMSSender.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
