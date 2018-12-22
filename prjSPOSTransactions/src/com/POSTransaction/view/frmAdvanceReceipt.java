/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSPrinting.clsAdvOrderReceiptGenerator;
import com.POSTransaction.controller.clsCalculateBillDiscount;
import com.POSTransaction.controller.clsCalculateBillPromotions;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsPromotionItems;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmAdvanceReceipt extends javax.swing.JFrame
{

    private int settlementNavigate;
    private String posDate, amountBox, sql, textValue1, settleCode, settleName, settleType, strValue;
    private java.util.Vector vSettlementDesc, vSettlementCode, vSettlementType, vCurrencyType;
    private boolean enter, settleMode, flgModifyAdvOrder;
    private Point pointCash, pointCredit;
    private JButton[] settlementArray = new JButton[4];
    private Map<String, String> mapSettlement;
    private clsUtility objUtility;
    private DecimalFormat formt = new DecimalFormat("####0.00");
    

    public frmAdvanceReceipt()
    {
	
    }

    public frmAdvanceReceipt(String callingForm, boolean flgModAdvOrder)
    {
        initComponents();
	
	
        this.setLocationRelativeTo(null);
        try
        {
            objUtility = new clsUtility();
            flgModifyAdvOrder = flgModAdvOrder;
            mapSettlement = new HashMap<String, String>();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtPaidAmount.setText("0");
            txtBalance.setText("0.00");
            if (callingForm.equals("StartMenu"))
            {
                btnPrint.setEnabled(false);
            }

            String orderDate = "", areaCode = "A001", operationTypeForTax = "HomeDelivery", settlementCode = "S01";
            double subTotal = 0.00, discountAmt = 0.00;
            double dblTotalTaxAmt = 0.00, dblOrderAmt = 0.00, dblPromotionAmt = 0.00, dblFinalAmt = 0.00, dblAdvTookAmt = 0.00;
            if (callingForm.equals("AdvOrder"))
            {
                //lblOrderAmount.setText(clsGlobalVarClass.getAdvanceAmt());              
                String sql = "select a.strAdvBookingNo,a.strCustomerCode,b.strCustomerName,a.dblHomeDelCharges"
                        + ",a.dblGrandTotal,date(a.dteOrderFor ) "
                        + "from tbladvbookbillhd a,tblcustomermaster b "
                        + "where a.strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "' "
                        + "and a.strCustomerCode=b.strCustomerCode ";
                ResultSet rsCustInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsCustInfo.next())
                {
                    txtAdvOrderNo.setText(rsCustInfo.getString(1));
                    txtCustomerName.setText(rsCustInfo.getString(3));
                    lblOrderAmount.setText(rsCustInfo.getString(5));
                    orderDate = rsCustInfo.getString(6);
                    lblOrderDateDisplay.setText(orderDate);

                    dblOrderAmt = rsCustInfo.getDouble(5);
                }
                rsCustInfo.close();

                //for promotion                
                List<clsDirectBillerItemDtl> listDirectBillerItemDtlForPromotion = new ArrayList<>();

                /**
                 * calculating taxes
                 */
                List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
                sql = "select a.strItemCode,a.strItemName,a.dblQuantity,a.dblAmount "
                        + "from tbladvbookbilldtl a where a.strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'  ";
                ResultSet rsAdvOrdDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsAdvOrdDtl.next())
                {

                    clsDirectBillerItemDtl objBillerItemDtl = new clsDirectBillerItemDtl();
                    objBillerItemDtl.setItemCode(rsAdvOrdDtl.getString(1));
                    objBillerItemDtl.setItemName(rsAdvOrdDtl.getString(2));
                    objBillerItemDtl.setQty(rsAdvOrdDtl.getDouble(3));
                    objBillerItemDtl.setAmt(rsAdvOrdDtl.getDouble(4));

                    listDirectBillerItemDtlForPromotion.add(objBillerItemDtl);
                }
                rsAdvOrdDtl.close();
                //modifiers
                sql = "select a.strItemCode,a.strModifierName,a.dblQuantity,a.dblAmount  "
                        + "from tbladvordermodifierdtl a where a.strAdvOrderNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'   ";
                rsAdvOrdDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsAdvOrdDtl.next())
                {
                    clsDirectBillerItemDtl objBillerItemDtl = new clsDirectBillerItemDtl();
                    objBillerItemDtl.setItemCode(rsAdvOrdDtl.getString(1));
                    objBillerItemDtl.setItemName(rsAdvOrdDtl.getString(2));
                    objBillerItemDtl.setQty(rsAdvOrdDtl.getDouble(3));
                    objBillerItemDtl.setAmt(rsAdvOrdDtl.getDouble(4));

                    listDirectBillerItemDtlForPromotion.add(objBillerItemDtl);
                }
                rsAdvOrdDtl.close();

                frmBillSettlement objBillSettlement = new frmBillSettlement("AdanceReceipt");
                frmDirectBiller objDirectBiller = new frmDirectBiller("AdanceReceipt", listDirectBillerItemDtlForPromotion);
		
                objBillSettlement.setObjDirectBiller(objDirectBiller);
                objBillSettlement.setDtPOSDate(clsGlobalVarClass.gPOSOnlyDateForTransaction);
		
		clsCalculateBillPromotions objCalculateBillPromotions = new clsCalculateBillPromotions(objBillSettlement);

                Map<String, clsPromotionItems> hmPromoItemDtl = null;
                Map<String, clsPromotionItems> hmPromoItem = new HashMap<String, clsPromotionItems>();
                HashMap<String, clsBillItemDtl> mapPromoItemDisc = new HashMap<>();
                Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();

                boolean flgApplyPromoOnBill = false;
                if (clsGlobalVarClass.gActivePromotions)
                {
                    hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("DirectBiller", "", "", new ArrayList());
                    if (null != hmPromoItemDtl)
                    {
                        if (hmPromoItemDtl.size() > 0)
                        {
                            if (clsGlobalVarClass.gPopUpToApplyPromotionsOnBill)
                            {
                                int res = JOptionPane.showConfirmDialog(null, "Do want to Calculate Promotions for this Bill?");
                                if (res == 0)
                                {
                                    flgApplyPromoOnBill = true;
                                }
                            }
                            else
                            {
                                flgApplyPromoOnBill = true;
                            }
                        }
                    }
                }
                objBillSettlement.setObjDirectBiller(null);
                objBillSettlement.setDtPOSDate(null);

                List<clsDirectBillerItemDtl> listDirectBillerItemDtlForTaxes = new ArrayList<>();

                for (clsDirectBillerItemDtl objDirectBillerItemList : listDirectBillerItemDtlForPromotion)
                {
                    String itemCode = objDirectBillerItemList.getItemCode();
                    String item = objDirectBillerItemList.getItemName();
                    double amount = objDirectBillerItemList.getAmt();
                    double qty = objDirectBillerItemList.getQty();
                    double rate = objDirectBillerItemList.getRate();
                    double freeAmount = 0.00, discAmt = 0.00;

                    if (null != hmPromoItemDtl)
                    {
                        if (hmPromoItemDtl.containsKey(itemCode))
                        {
                            if (null != hmPromoItemDtl.get(itemCode))
                            {
                                clsPromotionItems objPromoItemsDtl = hmPromoItemDtl.get(itemCode);
                                if (objPromoItemsDtl.getPromoType().equals("ItemWise"))
                                {
                                    double freeQty = objPromoItemsDtl.getFreeItemQty();
                                    if (freeQty > 0)
                                    {
                                        freeAmount = freeAmount + (rate * freeQty);
                                        amount = amount - freeAmount;
                                        hmPromoItem.put(itemCode, objPromoItemsDtl);
                                        hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
                                        hmPromoItemDtl.remove(itemCode);
                                    }
                                }
                                else if (objPromoItemsDtl.getPromoType().equals("Discount"))
                                {
                                    double discA = 0;
                                    double discP = 0;
                                    if (objPromoItemsDtl.getDiscType().equals("Value"))
                                    {
                                        discA = objPromoItemsDtl.getDiscAmt();
                                        discP = (discA / amount) * 100;
                                        hmPromoItem.put(itemCode, objPromoItemsDtl);
                                        hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
                                        hmPromoItemDtl.remove(itemCode);

                                        clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
                                        objItemPromoDiscount.setItemCode(itemCode);
                                        objItemPromoDiscount.setItemName(item);
                                        objItemPromoDiscount.setDiscountAmount(discA);
                                        objItemPromoDiscount.setDiscountPercentage(discP);
                                        objItemPromoDiscount.setAmount(amount);

                                        mapPromoItemDisc.put(itemCode, objItemPromoDiscount);

                                        dblPromotionAmt += discA;
                                    }
                                    else
                                    {
                                        discP = objPromoItemsDtl.getDiscPer();
                                        discA = (discP / 100) * amount;
                                        hmPromoItem.put(itemCode, objPromoItemsDtl);
                                        hmPromoItemDtl.remove(itemCode);
                                        clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
                                        objItemPromoDiscount.setItemCode(itemCode);
                                        objItemPromoDiscount.setItemName(item);
                                        objItemPromoDiscount.setDiscountAmount(discA);
                                        objItemPromoDiscount.setDiscountPercentage(discP);
                                        objItemPromoDiscount.setAmount(amount);

                                        mapPromoItemDisc.put(itemCode, objItemPromoDiscount);

                                        dblPromotionAmt += discA;
                                    }

                                    discAmt = discA;
                                }
                            }
                        }
                    }

                    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
                    objItemDtlForTax.setItemCode(itemCode);
                    objItemDtlForTax.setItemName(item);
                    objItemDtlForTax.setAmount(amount);
                    objItemDtlForTax.setDiscAmt(discAmt);

                    subTotal += objItemDtlForTax.getAmount();

                    arrListItemDtls.add(objItemDtlForTax);
                }

                lblPromoAmount.setText(String.valueOf(formt.format(dblPromotionAmt)));

                List<clsTaxCalculationDtls> arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, orderDate, areaCode, operationTypeForTax, subTotal, discountAmt, "", settlementCode, "Sales");
                for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
                {
                    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
                    {
                        dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
                    }
                }
                lblTaxAmount.setText(String.valueOf(formt.format(dblTotalTaxAmt)));

                dblFinalAmt = dblOrderAmt - dblPromotionAmt + dblTotalTaxAmt;
                dblFinalAmt = Double.parseDouble(formt.format(dblFinalAmt));
                //dblFinalAmt = Math.rint(dblFinalAmt);
                lblFinalAmount.setText(String.valueOf(dblFinalAmt));
                txtBalance.setText(String.valueOf(dblFinalAmt));

                if (flgModAdvOrder == true)
                {
                    DefaultTableModel dm = (DefaultTableModel) tblSettlement.getModel();
                    dm.getDataVector().removeAllElements();
                    txtAdvOrderNo.setText(clsGlobalVarClass.gAdvOrderNoForBilling);

                    String receiptNo = "";
                    Double tempAdvAmt = 0.00;
                    String data = "select strReceiptNo from tbladvancereceipthd "
                            + "where strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "' ";
                    ResultSet rsReceiptData = clsGlobalVarClass.dbMysql.executeResultSet(data);
                    while (rsReceiptData.next())
                    {
                        receiptNo = rsReceiptData.getString(1);
                    }
                    rsReceiptData.close();
                    txtRecptNo.setText(receiptNo);
                    sql = "select a.strSettlementCode,a.dblAdvDepositesettleAmt,a.strCardNo,a.strExpirydate"
                            + ",a.strChequeNo,a.dteCheque,a.strBankName,a.strRemark,a.dblPaidAmt,b.strSettelmentDesc,a.dteInstallment  "
                            + "from tbladvancereceiptdtl a,tblsettelmenthd b "
                            + "where strReceiptNo='" + txtRecptNo.getText() + "' and a.strSettlementCode=b.strSettelmentCode ";
                    ResultSet rsReceiptDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    while (rsReceiptDtl.next())
                    {
                        mapSettlement.put(rsReceiptDtl.getString(1), rsReceiptDtl.getString(11));
                        tempAdvAmt += Double.parseDouble(rsReceiptDtl.getString(2));
                        settleName = rsReceiptDtl.getString(10);
                        Object[] row =
                        {
                            rsReceiptDtl.getString(1), settleName, rsReceiptDtl.getString(2), rsReceiptDtl.getString(3), rsReceiptDtl.getString(4), rsReceiptDtl.getString(9), rsReceiptDtl.getString(8), "old"
                        };
                        dm.addRow(row);
                        tblSettlement.setModel(dm);
                    }
                    rsReceiptDtl.close();

                    dblAdvTookAmt = tempAdvAmt;
                    lblTotalAmt.setText(String.valueOf(tempAdvAmt));
                    rsReceiptDtl.close();
                    
                    txtBalance.setText(String.valueOf(dblFinalAmt-tempAdvAmt));
                }
            }
            txtAdvOrderNo.setEditable(false);
            txtAdvOrderNo.setEnabled(false);
            String bdte = clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);

            String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
            String time = bDate.getHours() + ":" + bDate.getMinutes() + ":" + bDate.getSeconds();
            posDate = date1 + " " + time;
            panelCardDetails.setVisible(false);
            paneLChequeDetails.setVisible(false);
            panelCoupen.setVisible(false);
            panelAmountDetails.setVisible(false);
            pointCash = panelAmountDetails.getLocation();
            pointCredit = panelCardDetails.getLocation();
            amountBox = "";
            txtAdvanceDeposit.requestFocus();
            txtAdvanceDeposit.setFocusable(true);
            textValue1 = "";

            settlementArray[0] = btnSettle1;
            settlementArray[1] = btnSettle2;
            settlementArray[2] = btnSettle3;
            settlementArray[3] = btnSettle4;
            int cntSettlement = 0;
            for (cntSettlement = 0; cntSettlement < settlementArray.length; cntSettlement++)
            {
                settlementArray[cntSettlement].setVisible(false);
            }
            vSettlementType = new java.util.Vector();
            vSettlementDesc = new java.util.Vector();
            vSettlementCode = new java.util.Vector();
            vCurrencyType = new java.util.Vector();
            cntSettlement = 0;
            sql = "Select Count(*) from tblsettelmenthd where strApplicable='Yes' and strAdvanceReceipt='Yes'";
            ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsSettlement.next();
            int settlementCount = rsSettlement.getInt(1);
            if (settlementCount > 3)
            {
                settlementCount = 4;
            }
            for (cntSettlement = 0; cntSettlement < settlementCount; cntSettlement++)
            {
                settlementArray[cntSettlement].setVisible(true);
            }
            cntSettlement = 0;
            sql = "select strSettelmentCode,strSettelmentDesc,strSettelmentType,dblConvertionRatio"
                    + " from tblsettelmenthd where strApplicable='Yes' and strBilling='Yes'";
            rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsSettlement.next())
            {
                vSettlementCode.add(rsSettlement.getString(1));
                vSettlementDesc.add(rsSettlement.getString(2));
                vSettlementType.add(rsSettlement.getString(3));
                vCurrencyType.add(rsSettlement.getString(4));
                cntSettlement++;
            }
            rsSettlement.close();
            if (vSettlementCode.size() <= 4)
            {
                btnNextSettlementMode.setEnabled(false);
            }
            funFillSettlementButtons(0, 4);
            btnPrevSettlementMode.setEnabled(false);
            settlementNavigate = 0;
            funSetDates();

            btnPrint.setMnemonic('p');
            procSettlementBtnClick(btnSettle1.getText());
            if (Math.rint(dblAdvTookAmt) >= Math.rint(dblFinalAmt))
            {
                panelAmountDetails.setVisible(false);
                enter = true;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetDates()
    {
        try
        {
            java.util.Date dt1 = new java.util.Date();
            int day = dt1.getDate();
            int month = dt1.getMonth() + 1;
            int year = dt1.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            dteCheque.setDate(date);
            dteExpiry.setDate(date);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*
     * Check the Which Settlement Mode Select By User
     */
    private void procSettlementBtnClick(String strSettleClick)
    {
        try
        {
            lblPaymentMode.setText(strSettleClick);
            panelPayModeHeader.setVisible(true);
            sql = "select strSettelmentType,strSettelmentDesc,strSettelmentCode from tblsettelmenthd where strSettelmentDesc='" + strSettleClick + "'";

            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            settleCode = rs.getString("strSettelmentCode");
            settleName = rs.getString("strSettelmentDesc");
            settleType = rs.getString("strSettelmentType");

            switch (rs.getString("strSettelmentType"))
            {

                case "Cash":
                    amountBox = "PaidAmount";
                    textValue1 = "";
                    panelAmountDetails.setVisible(true);
                    panelAmountDetails.setLocation(pointCash);
                    panelCoupen.setVisible(false);
                    panelCardDetails.setVisible(false);
                    paneLChequeDetails.setVisible(false);
                    break;

                case "Credit Card":
                    amountBox = "PaidAmount";
                    textValue1 = "";
                    panelCardDetails.setVisible(true);
                    panelAmountDetails.setVisible(true);
                    panelAmountDetails.setLocation(pointCash);
                    panelCoupen.setVisible(false);
                    paneLChequeDetails.setVisible(false);
                    break;

                case "Coupon":
                    amountBox = "CouponAmount";
                    textValue1 = "";
                    panelAmountDetails.setVisible(false);
                    panelCoupen.setVisible(true);
                    panelCardDetails.setVisible(false);
                    paneLChequeDetails.setVisible(false);
                    panelCoupen.setLocation(pointCash);
                    break;

                case "Cheque":
                    amountBox = "PaidAmount";
                    textValue1 = "";
                    panelAmountDetails.setVisible(true);
                    panelAmountDetails.setLocation(pointCash);
                    panelCoupen.setVisible(false);
                    panelCardDetails.setVisible(false);
                    paneLChequeDetails.setVisible(true);
                    paneLChequeDetails.setLocation(pointCredit);
                    break;

                case "Online Payment":
                    amountBox = "PaidAmount";
                    textValue1 = "";
                    panelAmountDetails.setVisible(true);
                    panelAmountDetails.setLocation(pointCash);
                    panelCoupen.setVisible(false);
                    panelCardDetails.setVisible(false);
                    paneLChequeDetails.setVisible(false);
                    break;
            }
            settleMode = true;
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*
     * To insert the record into Table
     */
    private void funInsertAdvRecTransTables(String advReceiptNo) throws Exception
    {
        int exc = 0;
        //String setleCode=null,settleName=null,amount=null,cardName=null,expriyDate=null,paidAmount=null,remark=null;
        double sumAdvAmount = 0.00;
        int row = 0;
        java.util.Date objDate = new java.util.Date();
        String advReceiptDate = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate();

        sql = "delete from tbladvancereceiptdtl where strReceiptNo='" + txtRecptNo.getText() + "'";
        clsGlobalVarClass.dbMysql.execute(sql);

        double finalAmount = Double.parseDouble(lblFinalAmount.getText().trim());

        for (row = 0; row < tblSettlement.getRowCount(); row++)
        {
            settleName = tblSettlement.getValueAt(row, 1).toString();
            sumAdvAmount += Double.parseDouble(tblSettlement.getValueAt(row, 2).toString());

            double advAmt = Double.parseDouble(tblSettlement.getValueAt(row, 2).toString());
            double paidAmt = Double.parseDouble(tblSettlement.getValueAt(row, 2).toString());
            double advOrderBillAmt = Double.parseDouble(lblOrderAmount.getText().trim());

            if (finalAmount < sumAdvAmount)
            {
                advAmt = finalAmount;
            }
            sql = "insert into tbladvancereceiptdtl(strReceiptNo,strSettlementCode,strCardNo,strExpirydate"
                    + " ,strChequeNo,dteCheque,strBankName,dblAdvDepositesettleAmt,strRemark,dblPaidAmt,strClientCode"
                    + " ,dteInstallment) "
                    + " values('" + advReceiptNo + "','" + tblSettlement.getValueAt(row, 0).toString() + "','" + tblSettlement.getValueAt(row, 3).toString() + "'"
                    + " ,'" + tblSettlement.getValueAt(row, 4).toString() + "','" + txtChequeNo.getText() + "','" + advReceiptDate + "'"
                    + " ,'" + txtBankName.getText() + "','" + advAmt + "','" + paidAmt + "','" + tblSettlement.getValueAt(row, 5).toString() + "'"
                    + ",'" + clsGlobalVarClass.gClientCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
            exc = clsGlobalVarClass.dbMysql.execute(sql);
        }
        if (exc > 0)
        {
            sql = "delete from tbladvancereceipthd "
                    + " where strReceiptNo='" + txtRecptNo.getText() + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
                    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            double advOrderBillAmt = Double.parseDouble(lblOrderAmount.getText().trim());
            if (finalAmount < sumAdvAmount)
            {
                sumAdvAmount = finalAmount;
            }

            if (row > 1)
            {
                String settle = "MultiSettle";
                //set PosDate in dtReceiptDate field
                sql = "insert into tbladvancereceipthd(strReceiptNo,strAdvBookingNo,strPOSCode,strSettelmentMode,dtReceiptDate,dblAdvDeposite,intShiftCode,strUserCreated,dtDateCreated,strUserEdited,dtDateEdited,strClientCode)"
                        + " values('" + advReceiptNo + "','" + txtAdvOrderNo.getText() + "','" + clsGlobalVarClass.gPOSCode + "','" + settle + "','" + posDate + "','" + sumAdvAmount + "','" + clsGlobalVarClass.gShiftNo + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                exc = clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                //set PosDate in dtReceiptDate field
                sql = "insert into tbladvancereceipthd(strReceiptNo,strAdvBookingNo,strPOSCode"
                        + ",strSettelmentMode,dtReceiptDate,dblAdvDeposite,intShiftCode,strUserCreated"
                        + ",strUserEdited,dtDateCreated,dtDateEdited,strClientCode) "
                        + "values('" + advReceiptNo + "','" + txtAdvOrderNo.getText() + "','" + clsGlobalVarClass.gPOSCode + "'"
                        + ",'" + settleName + "','" + posDate + "','" + sumAdvAmount + "','" + clsGlobalVarClass.gShiftNo + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
                exc = clsGlobalVarClass.dbMysql.execute(sql);
            }

            //update advBillbook  settle,menet mode
            String sql = "update tbladvbookbillhd  "
                    + "set strSettelmentMode='" + settleName + "'  "
                    + "where strAdvBookingNo='" + txtAdvOrderNo.getText() + "' ";
            clsGlobalVarClass.dbMysql.execute(sql);

            txtRecptNo.setText(advReceiptNo);
            String sqlAdvOrder = "select a.strAdvBookingNo,date(a.dteOrderFor),b.strCustomerName,a.strDeliveryTime"
                    + ",ifnull(c.strWShortName,'') "
                    + "from tbladvbookbillhd a left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
                    + "left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo "
                    + "where a.strAdvBookingNo='" + txtAdvOrderNo.getText() + "'";
            ResultSet rsAdvOrder = clsGlobalVarClass.dbMysql.executeResultSet(sqlAdvOrder);
            if (rsAdvOrder.next())
            {
                String dateAndTime = rsAdvOrder.getString(2) + " " + rsAdvOrder.getString(4);
                funGenReceiptPrint(txtRecptNo.getText(), rsAdvOrder.getString(1), rsAdvOrder.getString(3), clsGlobalVarClass.gPOSCode, dateAndTime, rsAdvOrder.getString(5));
            }
            rsAdvOrder.close();
            funResetField();

            if (clsGlobalVarClass.gProductionLinkup)
            {
                int res = JOptionPane.showConfirmDialog(null, "Do you want to open place order form?");
                if (res == 0)
                {
                    new frmPlaceOrder().setVisible(true);
                }
            }
            dispose();
            clsGlobalVarClass.hmActiveForms.remove("Advance Booking Receipt");
        }
    }

    private void funBackspaceBtnPressed()
    {
        try
        {
            if (amountBox.equals("PaidAmount"))
            {
                StringBuilder sb = new StringBuilder(textValue1);
                sb.delete(textValue1.length() - 1, textValue1.length());
                textValue1 = sb.toString();
                txtAdvanceDeposit.setText(textValue1);
            }
            else if (amountBox.equals("CouponAmount"))
            {
                StringBuilder sb = new StringBuilder(textValue1);
                sb.delete(textValue1.length() - 1, textValue1.length());
                textValue1 = sb.toString();
                txtCoupenAmt.setText(textValue1);
            }
            else if (amountBox.equals("PaidAmt"))
            {
                StringBuilder sb = new StringBuilder(textValue1);
                sb.delete(textValue1.length() - 1, textValue1.length());
                textValue1 = sb.toString();
                txtPaidAmount.setText(textValue1);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funClearBtnPressed()
    {
        try
        {
            if (amountBox.equals("PaidAmount"))
            {
                textValue1 = "";
                txtAdvanceDeposit.setText(textValue1);
            }
            else if (amountBox.equals("CouponAmount"))
            {
                textValue1 = "";
                txtCoupenAmt.setText(textValue1);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void procNumericValue(String strValue)
    {
        if (amountBox.equals("PaidAmount"))
        {
            textValue1 = textValue1 + strValue;
            txtAdvanceDeposit.setText(textValue1);
        }
        else if (amountBox.equals("CouponAmount"))
        {
            textValue1 = textValue1 + strValue;
            txtCoupenAmt.setText(textValue1);
        }
        else if (amountBox.equals("PaidAmt"))
        {
            textValue1 = textValue1 + strValue;
            txtPaidAmount.setText(textValue1);
        }
    }

    //Reset the field
    private void procClear()
    {
        txtCardName.setText("");
        txtAdvanceDeposit.setText("");
        txtCoupenAmt.setText("");
        panelCoupen.setVisible(false);
        panelAmountDetails.setVisible(false);
        panelCardDetails.setVisible(false);
        panelPayModeHeader.setVisible(false);
        paneLChequeDetails.setVisible(false);
    }

    private void funSaveAdvanceReceipt()
    {
        try
        {
            if (enter == true)
            {
                if (btnPrint.getText().equalsIgnoreCase("PRINT"))
                {
                    String advReceiptNo = "";
                    if (flgModifyAdvOrder)
                    {
                        advReceiptNo = txtRecptNo.getText();
                    }
                    else
                    {
                        advReceiptNo = funGenerateReceiptNo();
                    }
                    funInsertAdvRecTransTables(advReceiptNo);
                    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
                    {
                        if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
                        {
                            clsGlobalVarClass.funInvokeHOWebserviceForTrans("AdvanceOrder", "Bill");
                        }
                    }
                }
            }

            /*
             * if(flgModifyAdvOrder==false) { if(enter==true) {
             * if(btnPrint.getText().equalsIgnoreCase("PRINT")) { String
             * advReceiptNo=funGenerateReceiptNo();
             * funInsertAdvRecTransTables(advReceiptNo); if
             * (clsGlobalVarClass.gConnectionActive.equals("Y")) {
             * if(clsGlobalVarClass.gDataSendFrequency.equals("After Every
             * Bill")) {
             * clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales","Bill");
             * } } } } } else { java.util.Date objDate=new java.util.Date();
             * String
             * advReceiptDate=(objDate.getYear()+1900)+"-"+(objDate.getMonth()+1)+"-"+objDate.getDate();
             * int exc=0; String
             * setleCode=null,settleName=null,Amount=null,cardName=null,expriyDate=null,PaidAmount=null,remark=null;
             * double sumAdvAmount=0.00; int row=0; sql="delete from
             * tbladvancereceiptdtl where
             * strReceiptNo='"+txtRecptNo.getText()+"'";
             * clsGlobalVarClass.dbMysql.execute(sql); exc=1; if(exc>0) {
             * row=tblSettlement.getRowCount(); String installmentDate="";
             * for(row=0;row<tblSettlement.getRowCount();row++) {
             * setleCode=tblSettlement.getValueAt(row, 0).toString();
             * settleName=tblSettlement.getValueAt(row, 1).toString();
             * Amount=tblSettlement.getValueAt(row, 2).toString();
             * sumAdvAmount=sumAdvAmount+Double.parseDouble(Amount);
             * cardName=tblSettlement.getValueAt(row, 3).toString();
             * expriyDate=tblSettlement.getValueAt(row, 4).toString();
             * PaidAmount=tblSettlement.getValueAt(row, 5).toString();
             * remark=tblSettlement.getValueAt(row, 6).toString(); String
             * flag=tblSettlement.getValueAt(row, 7).toString();
             * if(flag.equals("new")) {
             * installmentDate=clsGlobalVarClass.getCurrentDateTime();
             * sql="insert into
             * tbladvancereceiptdtl(strReceiptNo,strSettlementCode,strCardNo," +
             * "strExpirydate,strChequeNo,strBankName,dblAdvDepositesettleAmt,strRemark,dblPaidAmt"
             * + ",dteCheque,strClientCode,dteInstallment)" + "
             * values('"+txtRecptNo.getText()+"','"+setleCode+"','"+cardName+"','"+expriyDate+"'"
             * +
             * ",'"+txtChequeNo.getText()+"','"+txtBankName.getText()+"','"+Amount+"','"+remark+"'"
             * +
             * ",'"+PaidAmount+"','"+advReceiptDate+"','"+clsGlobalVarClass.gClientCode+"','"+installmentDate+"'
             * )"; exc=clsGlobalVarClass.dbMysql.execute(sql); } } if(row>1) {
             * settleName="MultiSettle"; } } if(exc>0) { sql="update
             * tbladvancereceipthd set strSettelmentMode='"+settleName+"'" +
             * ",dtReceiptDate='"+posDate+"',dblAdvDeposite='"+sumAdvAmount+"',strDataPostFlag='N'
             * " + "where strAdvBookingNo='"+txtAdvOrderNo.getText()+"' " + "and
             * strReceiptNo='"+txtRecptNo.getText()+"'";
             * exc=clsGlobalVarClass.dbMysql.execute(sql); } if(exc>0) { String
             * sql_AdvOrder="select
             * a.strAdvBookingNo,date(a.dteOrderFor),b.strCustomerName,a.strDeliveryTime,ifnull(c.strWShortName,'')
             * " +"from tbladvbookbillhd a left outer join tblcustomermaster b
             * on a.strCustomerCode=b.strCustomerCode " +"left outer join
             * tblwaitermaster c on a.strWaiterNo=c.strWaiterNo " +"where
             * a.strAdvBookingNo='"+txtAdvOrderNo.getText()+"'"; ResultSet
             * rsAdvOrder=clsGlobalVarClass.dbMysql.executeResultSet(sql_AdvOrder);
             *
             * if(rsAdvOrder.next()) { String
             * dateAndTime=rsAdvOrder.getString(2)+" "+rsAdvOrder.getString(4);
             * funGenReceiptPrint(txtRecptNo.getText(),rsAdvOrder.getString(1),rsAdvOrder.getString(3),clsGlobalVarClass.gPOSCode,dateAndTime,rsAdvOrder.getString(5));
             * } rsAdvOrder.close(); if
             * (clsGlobalVarClass.gConnectionActive.equals("Y")) {
             * if(clsGlobalVarClass.gDataSendFrequency.equals("After Every
             * Bill")) {
             * clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales","Bill");
             * } } funResetField(); dispose();
             * clsGlobalVarClass.hmActiveForms.remove("Advance Booking
             * Receipt"); }
            }
             */
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funPrintButtonPressed()
    {
        if (tblSettlement.getRowCount() > 0)
        {
            funSaveAdvanceReceipt();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Select Settelement !!!");
            txtAdvanceDeposit.requestFocus();
        }
    }

    private void funEnterButtonPressed()
    {
        try
        {
            String toDate = "";
            double temptotalAmt = 0.00;
            if (settleMode == true)
            {
                DefaultTableModel dm = (DefaultTableModel) tblSettlement.getModel();

                if (settleType.equals("Coupon"))
                {
                    strValue = txtCoupenAmt.getText();
                    if (strValue.equals("") || Double.parseDouble(strValue) == 0)
                    {
                        new frmOkPopUp(this, "Please Enter Amount", "Warning", 1).setVisible(true);
                        return;
                    }
                }
                else if (settleType.equals("Credit Card"))
                {
                    Date dtExpirtDate = dteExpiry.getDate();
                    toDate = (dtExpirtDate.getYear() + 1900) + "-" + (dtExpirtDate.getMonth() + 1);
                    strValue = txtAdvanceDeposit.getText();
                    if (strValue.equals("") || Double.parseDouble(strValue) == 0)
                    {
                        new frmOkPopUp(this, "Please Enter Amount", "Warning", 1).setVisible(true);
                        return;
                    }
                }
                else if (settleType.equals("Cheque"))
                {
                    Date dtChequeDate = dteCheque.getDate();
                    toDate = (dtChequeDate.getYear() + 1900) + "-" + (dtChequeDate.getMonth() + 1) + "-" + dtChequeDate.getDate();
                    strValue = txtAdvanceDeposit.getText();
                    if (strValue.equals("") || Double.parseDouble(strValue) == 0)
                    {
                        new frmOkPopUp(this, "Please Enter Amount", "Warning", 1).setVisible(true);
                        return;
                    }
                }
                else
                {
                    strValue = txtAdvanceDeposit.getText();
                    if (strValue.equals(""))
                    {
                        new frmOkPopUp(this, "Please Enter Amount", "Warning", 1).setVisible(true);
                        return;
                    }
                    if (tblSettlement.getRowCount() > 0)
                    {
                        if (strValue.equals("") || Double.parseDouble(strValue) == 0)
                        {
                            new frmOkPopUp(this, "Please Enter Amount", "Warning", 1).setVisible(true);
                            return;
                        }
                    }
                }
                if (!settleType.equals("Clear"))
                {

                    int rowNo = -1;
                    double newSettleAmt = Double.parseDouble(strValue);
                    for (int row = 0; row < tblSettlement.getRowCount(); row++)
                    {
                        String oldSettle = tblSettlement.getValueAt(row, 0).toString();
                        if (settleCode.equalsIgnoreCase(oldSettle))
                        {
                            newSettleAmt = newSettleAmt + Double.parseDouble(tblSettlement.getValueAt(row, 2).toString());
                            rowNo = row;
                            break;
                        }
                    }
                    if (rowNo > -1)
                    {
                        strValue = String.valueOf(newSettleAmt);

                        tblSettlement.setValueAt(strValue, rowNo, 2);//update settlement amt
                    }
                    else
                    {
                        Object[] row =
                        {
                            settleCode, settleName, strValue, txtCardName.getText(), toDate, txtPaidAmount.getText(), txtRemark.getText(), "new"
                        };
                        dm.addRow(row);
                    }
                    tblSettlement.setModel(dm);
                }
                for (int row = 0; row < tblSettlement.getRowCount(); row++)
                {
                    temptotalAmt = temptotalAmt + Double.parseDouble(tblSettlement.getValueAt(row, 2).toString());
                }
                if (!txtPaidAmount.getText().equals("0"))
                {
                    Double Refund = Double.parseDouble(strValue) - Double.parseDouble(txtPaidAmount.getText());
                    new frmOkPopUp(this, "Refund Amount", Refund.toString(), 1).setVisible(true);
                    txtRefund.setText(Refund.toString());
                }
                lblTotalAmt.setText(String.valueOf(temptotalAmt));

                String sqlTaxex = "select a.strTaxCode,a.strTaxDesc from tbltaxhd a "
                        + "where date(a.dteValidTo)>='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
                ResultSet rsIsTaxex = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsIsTaxex.next())
                {
                    if (temptotalAmt > Double.parseDouble(lblFinalAmount.getText()))
                    {
                        double refAmt = Math.rint(temptotalAmt - Double.parseDouble(lblFinalAmount.getText()));
                        lblRefundAmount.setText(String.valueOf(refAmt));
                    }

                }
                else
                {
                    if (temptotalAmt > Double.parseDouble(clsGlobalVarClass.getAdvanceAmt()))
                    {
                        double refAmt = Math.rint(temptotalAmt - Double.parseDouble(clsGlobalVarClass.getAdvanceAmt()));
                        lblRefundAmount.setText(String.valueOf(refAmt));
                    }
                }

                double dblBalanceAmt = Math.rint(Double.parseDouble(lblFinalAmount.getText())) - Math.rint(temptotalAmt);
                if(dblBalanceAmt<=0)
                {
                    dblBalanceAmt=0;
                }
                txtBalance.setText(String.valueOf(dblBalanceAmt));

                if (temptotalAmt >= Double.parseDouble(lblFinalAmount.getText()))
                {
                    settleType = "Clear";
                    procClear();
//                    enter = true;
                    btnPrint.requestFocus();

                }
                else if (temptotalAmt == 0)
                {
                    settleType = "Clear";
                    procClear();
                    //enter = true;
                    btnPrint.requestFocus();
                }
                else
                {
                    txtAdvanceDeposit.setText("0");
                }
                if (tblSettlement.getRowCount() > 0)
                {
                    enter = true;
                }
            }
            else
            {
                new frmOkPopUp(null, "Please Select Settle Mode", "warning", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private String funGenerateReceiptNo()
    {
        String receiptNo = "";
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='AdvReceipt'";
            ResultSet rsAdvReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsAdvReceipt.next();
            int receiptCnt = rsAdvReceipt.getInt(1);
            rsAdvReceipt.close();
            if (receiptCnt > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='AdvReceipt'";
                rsAdvReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsAdvReceipt.next();
                long code = rsAdvReceipt.getLong(1);
                code = code + 1;
                receiptNo = "AR" + String.format("%05d", code);
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='AdvReceipt'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                receiptNo = "AR00001";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('AdvReceipt'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return receiptNo;
    }

    //Reset the field 
    private void funResetField()
    {
        btnPrint.setText("SAVE");
        txtRecptNo.setText("");
        txtAdvanceDeposit.setText("");
        txtCardName.setText("");
        txtChequeNo.setText("");
        txtBankName.setText("");
        txtCustomerName.setText("");
        clsGlobalVarClass.gCustCodeForAdvOrder = null;
        clsGlobalVarClass.gAdvOrderNoForBilling = null;
        clsGlobalVarClass.setAdvanceAmt(null);
        clsGlobalVarClass.setAdvanceOrderForDate(null);
        clsGlobalVarClass.setAdvOrderNo(null);
        sql = null;
        vSettlementDesc = null;
        vSettlementCode = null;
        vSettlementType = null;
        vCurrencyType = null;
        mapSettlement = null;
    }

    /*
     * to Generate the Receipt
     */
    private void funGenReceiptPrint(String receiptNo, String advOrderNo, String custName, String posCode, String orderDate, String waiterName)
    {

        clsAdvOrderReceiptGenerator objOrderReceiptGenerator = new clsAdvOrderReceiptGenerator();
        objOrderReceiptGenerator.funGenerateAdvReceipt(advOrderNo, receiptNo, posCode, "", custName, orderDate, waiterName, "");

    }

    private void funPrevSettlementMode()
    {
        try
        {
            settlementNavigate--;
            if (settlementNavigate == 0)
            {
                btnPrevSettlementMode.setEnabled(false);
                btnNextSettlementMode.setEnabled(true);
                funFillSettlementButtons(0, vSettlementCode.size());
            }
            else
            {
                int startIndex = (settlementNavigate * 4);
                int endIndex = startIndex + 4;
                funFillSettlementButtons(startIndex, endIndex);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funNextSettlementMode()
    {
        try
        {
            settlementNavigate++;
            int startIndex = (settlementNavigate * 4);
            int endIndex = startIndex + 4;
            int disableNext = vSettlementCode.size() / startIndex;
            funFillSettlementButtons(startIndex, endIndex);
            btnPrevSettlementMode.setEnabled(true);
            if (disableNext == settlementNavigate)
            {
                btnNextSettlementMode.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillSettlementButtons(int startIndex, int endIndex)
    {
        int cntArrayIndex = 0;
        for (int k = 0; k < 4; k++)
        {
            settlementArray[k].setVisible(false);
            settlementArray[k].setText("");
        }
        for (int cntSettlement = startIndex; cntSettlement < endIndex; cntSettlement++)
        {
            if (cntSettlement == vSettlementCode.size())
            {
                break;
            }
            if (cntArrayIndex < 4)
            {
                settlementArray[cntArrayIndex].setText(vSettlementDesc.elementAt(cntSettlement).toString());
                settlementArray[cntArrayIndex].setVisible(true);
                cntArrayIndex++;
            }
        }
    }

    private void funSelectAdvOrderNo()
    {
        try
        {
            objUtility.funCallForSearchForm("AvdBookReceipt");
            new frmSearchFormDialog(null, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetAdvOrderData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetAdvOrderData(Object[] data) throws Exception
    {
        sql = "select a.strReceiptNo,a.strAdvBookingNo,a.dblAdvDeposite,date(b.dteOrderFor),b.dblGrandTotal,c.strCustomerName "
                + " from tbladvancereceipthd a,tbladvbookbillhd b,tblcustomermaster c "
                + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strReceiptNo='" + data[1].toString() + "' "
                + " and b.strCustomerCode=c.strCustomerCode ";
        ResultSet rsAdvOrderData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsAdvOrderData.next())
        {
            txtRecptNo.setText(rsAdvOrderData.getString(1));
            txtAdvOrderNo.setText(rsAdvOrderData.getString(2));
            lblOrderDateDisplay.setText(rsAdvOrderData.getString(4));
            lblOrderAmount.setText(rsAdvOrderData.getString(5));
            txtCustomerName.setText(rsAdvOrderData.getString(6));
            funSetSettlementDtl();
        }
        rsAdvOrderData.close();
    }

    private void funSetSettlementDtl()
    {
        try
        {
            DefaultTableModel dm = (DefaultTableModel) tblSettlement.getModel();
            dm.getDataVector().removeAllElements();
            double tempAdvAmt = 0.00;
            sql = "select a.strSettlementCode,a.dblAdvDepositesettleAmt,a.strCardNo,a.strExpirydate"
                    + ",a.strChequeNo,a.dteCheque,a.strBankName,a.strRemark,a.dblPaidAmt,b.strSettelmentDesc,a.dteInstallment  "
                    + "from tbladvancereceiptdtl a,tblsettelmenthd b "
                    + "where strReceiptNo='" + txtRecptNo.getText() + "' and a.strSettlementCode=b.strSettelmentCode ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                mapSettlement.put(rs.getString(1), rs.getString(11));
                String settlementCode = rs.getString(1);
                String advDepositeAmt = rs.getString(2);
                String cardNo = rs.getString(3);
                String expiryDate = rs.getString(4);
                String remark = rs.getString(8);
                String paidAmt = rs.getString(9);
                tempAdvAmt += Double.parseDouble(advDepositeAmt);
                settleName = rs.getString(10);
                Object[] row =
                {
                    settlementCode, settleName, advDepositeAmt, cardNo, expiryDate, paidAmt, remark, "old"
                };
                dm.addRow(row);
                tblSettlement.setModel(dm);
            }
            rs.close();
            lblTotalAmt.setText(String.valueOf(tempAdvAmt));
            btnPrint.setEnabled(true);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*
     * private int funCheckAdvanceOrder() { try { sql="select strAdvBookingNo
     * from tbladvancereceipthd " + " where
     * strAdvBookingNo='"+txtAdvOrderNo.getText()+"' and
     * strPOSCode='"+clsGlobalVarClass.gPOSCode+"' " + " and
     * strClientCode='"+clsGlobalVarClass.gClientCode+"' "; ResultSet
     * rsAdvOrder=clsGlobalVarClass.dbMysql.executeResultSet(sql);
     * if(!rsAdvOrder.next()) { sql="delete from tbladvbookbillhd where
     * strAdvBookingNo='"++"' "; } rsAdvOrder.close(); }catch(Exception e) {
     * objUtility.funWriteErrorLog(e); e.printStackTrace(); }
    }
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
        };  
        ;
        panelFormBody = new javax.swing.JPanel();
        panelFormBody1 = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblReceiptNo = new javax.swing.JLabel();
        txtRecptNo = new javax.swing.JTextField();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblPayMode = new javax.swing.JLabel();
        btnSettle1 = new javax.swing.JButton();
        btnSettle2 = new javax.swing.JButton();
        btnSettle3 = new javax.swing.JButton();
        panelCardDetails = new javax.swing.JPanel();
        txtCardName = new javax.swing.JTextField();
        lblCardName = new javax.swing.JLabel();
        lblCardExpirtDate = new javax.swing.JLabel();
        dteExpiry = new com.toedter.calendar.JDateChooser();
        panelPayModeHeader = new javax.swing.JPanel();
        lblPayModeHeader = new javax.swing.JLabel();
        lblPaymentMode = new javax.swing.JLabel();
        panelAmountDetails = new javax.swing.JPanel();
        txtAdvanceDeposit = new javax.swing.JTextField();
        lblAdvDeposite = new javax.swing.JLabel();
        lblPaidAmt = new javax.swing.JLabel();
        txtPaidAmount = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtRefund = new javax.swing.JTextField();
        panelNumericKeyPad = new javax.swing.JPanel();
        btnCal7 = new javax.swing.JButton();
        btnCal8 = new javax.swing.JButton();
        btnCal9 = new javax.swing.JButton();
        btnCalClear = new javax.swing.JButton();
        btnCal4 = new javax.swing.JButton();
        btnCal5 = new javax.swing.JButton();
        btnCal6 = new javax.swing.JButton();
        btnCal0 = new javax.swing.JButton();
        btnCal00 = new javax.swing.JButton();
        btnCal3 = new javax.swing.JButton();
        btnCalBackSpace = new javax.swing.JButton();
        btnCalDot = new javax.swing.JButton();
        btnCal1 = new javax.swing.JButton();
        btnCal2 = new javax.swing.JButton();
        btnCalEnter = new javax.swing.JButton();
        paneLChequeDetails = new javax.swing.JPanel();
        lblChequeNo = new javax.swing.JLabel();
        txtChequeNo = new javax.swing.JTextField();
        lblBankName = new javax.swing.JLabel();
        txtBankName = new javax.swing.JTextField();
        lblChequeDate = new javax.swing.JLabel();
        dteCheque = new com.toedter.calendar.JDateChooser();
        txtAdvOrderNo = new javax.swing.JTextField();
        lblAdvOrderNo = new javax.swing.JLabel();
        lblOrderDate = new javax.swing.JLabel();
        lblOrderDateDisplay = new javax.swing.JLabel();
        lblOrderAmt = new javax.swing.JLabel();
        lblOrderAmount = new javax.swing.JLabel();
        scrTax1 = new javax.swing.JScrollPane();
        tblSettlement = new javax.swing.JTable();
        panelCoupen = new javax.swing.JPanel();
        lblAmt = new javax.swing.JLabel();
        txtCoupenAmt = new javax.swing.JTextField();
        lblRemarks = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        btnSettle4 = new javax.swing.JButton();
        lblTotalAmount = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnPrevSettlementMode = new javax.swing.JButton();
        btnNextSettlementMode = new javax.swing.JButton();
        lblRefundAmount = new javax.swing.JLabel();
        lblRefund = new javax.swing.JLabel();
        lblPromoAmt = new javax.swing.JLabel();
        lblPromoAmount = new javax.swing.JLabel();
        lblTaxAmt = new javax.swing.JLabel();
        lblTaxAmount = new javax.swing.JLabel();
        lblFinalAmount = new javax.swing.JLabel();
        lblTaxAmt1 = new javax.swing.JLabel();
        lblTotalAmount1 = new javax.swing.JLabel();
        txtBalance = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS-");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Advance Booking Receipt");
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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelFormBody1.setOpaque(false);
        panelFormBody1.setPreferredSize(new java.awt.Dimension(780, 600));
        panelFormBody1.setLayout(null);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Advance Booking Receipt ");
        panelFormBody1.add(lblFormName);
        lblFormName.setBounds(250, 0, 290, 35);

        lblReceiptNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReceiptNo.setText("Receipt No. :");
        panelFormBody1.add(lblReceiptNo);
        lblReceiptNo.setBounds(6, 46, 80, 30);

        txtRecptNo.setEnabled(false);
        txtRecptNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRecptNoMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtRecptNo);
        txtRecptNo.setBounds(90, 50, 110, 30);

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Name         :");
        panelFormBody1.add(lblCustomerName);
        lblCustomerName.setBounds(6, 91, 80, 24);

        txtCustomerName.setEnabled(false);
        panelFormBody1.add(txtCustomerName);
        txtCustomerName.setBounds(90, 90, 230, 30);

        lblPayMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPayMode.setText("Payment Mode");
        panelFormBody1.add(lblPayMode);
        lblPayMode.setBounds(6, 130, 101, 30);

        btnSettle1.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle1.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseEntered(evt);
            }
        });
        panelFormBody1.add(btnSettle1);
        btnSettle1.setBounds(70, 170, 100, 40);

        btnSettle2.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle2.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle2MouseClicked(evt);
            }
        });
        panelFormBody1.add(btnSettle2);
        btnSettle2.setBounds(180, 170, 102, 40);

        btnSettle3.setBackground(new java.awt.Color(102, 153, 255));
        btnSettle3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle3.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle3MouseClicked(evt);
            }
        });
        btnSettle3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettle3ActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnSettle3);
        btnSettle3.setBounds(290, 170, 100, 40);

        panelCardDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelCardDetails.setOpaque(false);

        txtCardName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardNameMouseClicked(evt);
            }
        });

        lblCardName.setText("Card Name");

        lblCardExpirtDate.setText("Expiry Date");

        javax.swing.GroupLayout panelCardDetailsLayout = new javax.swing.GroupLayout(panelCardDetails);
        panelCardDetails.setLayout(panelCardDetailsLayout);
        panelCardDetailsLayout.setHorizontalGroup(
            panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCardDetailsLayout.createSequentialGroup()
                .addGroup(panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblCardExpirtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(lblCardName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCardName)
                    .addComponent(dteExpiry, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelCardDetailsLayout.setVerticalGroup(
            panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCardDetailsLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCardDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dteExpiry, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(lblCardExpirtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelFormBody1.add(panelCardDetails);
        panelCardDetails.setBounds(20, 340, 240, 70);

        panelPayModeHeader.setMinimumSize(new java.awt.Dimension(250, 150));
        panelPayModeHeader.setOpaque(false);

        lblPayModeHeader.setText("Payment Mode");

        lblPaymentMode.setText("Cash");

        javax.swing.GroupLayout panelPayModeHeaderLayout = new javax.swing.GroupLayout(panelPayModeHeader);
        panelPayModeHeader.setLayout(panelPayModeHeaderLayout);
        panelPayModeHeaderLayout.setHorizontalGroup(
            panelPayModeHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPayModeHeaderLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblPayModeHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPaymentMode, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        panelPayModeHeaderLayout.setVerticalGroup(
            panelPayModeHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPayModeHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblPayModeHeader)
                .addComponent(lblPaymentMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFormBody1.add(panelPayModeHeader);
        panelPayModeHeader.setBounds(20, 220, 240, 20);

        panelAmountDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAdvanceDeposit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAdvanceDeposit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAdvanceDepositMouseClicked(evt);
            }
        });
        txtAdvanceDeposit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtAdvanceDepositActionPerformed(evt);
            }
        });
        txtAdvanceDeposit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAdvanceDepositKeyPressed(evt);
            }
        });

        lblAdvDeposite.setText("Ad. Deposit  ");

        lblPaidAmt.setText("Paid Amount");

        txtPaidAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaidAmount.setMinimumSize(new java.awt.Dimension(99, 25));
        txtPaidAmount.setPreferredSize(new java.awt.Dimension(99, 25));
        txtPaidAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPaidAmountMouseClicked(evt);
            }
        });

        jLabel18.setText("Refund Amount");

        txtRefund.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRefund.setMinimumSize(new java.awt.Dimension(99, 25));
        txtRefund.setPreferredSize(new java.awt.Dimension(99, 25));

        javax.swing.GroupLayout panelAmountDetailsLayout = new javax.swing.GroupLayout(panelAmountDetails);
        panelAmountDetails.setLayout(panelAmountDetailsLayout);
        panelAmountDetailsLayout.setHorizontalGroup(
            panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAmountDetailsLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblPaidAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAdvDeposite, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAdvanceDeposit)
                    .addComponent(txtPaidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(txtRefund, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAmountDetailsLayout.setVerticalGroup(
            panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAmountDetailsLayout.createSequentialGroup()
                .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAdvanceDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdvDeposite, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPaidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPaidAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAmountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRefund, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFormBody1.add(panelAmountDetails);
        panelAmountDetails.setBounds(20, 240, 238, 100);

        panelNumericKeyPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericKeyPad.setMinimumSize(new java.awt.Dimension(340, 260));
        panelNumericKeyPad.setOpaque(false);

        btnCal7.setBackground(new java.awt.Color(102, 153, 255));
        btnCal7.setText("7");
        btnCal7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal7MouseClicked(evt);
            }
        });

        btnCal8.setBackground(new java.awt.Color(102, 153, 255));
        btnCal8.setText("8");
        btnCal8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal8MouseClicked(evt);
            }
        });

        btnCal9.setBackground(new java.awt.Color(102, 153, 255));
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
            }
        });

        btnCalClear.setBackground(new java.awt.Color(102, 153, 255));
        btnCalClear.setText("C");
        btnCalClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalClearMouseClicked(evt);
            }
        });

        btnCal4.setBackground(new java.awt.Color(102, 153, 255));
        btnCal4.setText("4");
        btnCal4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal4MouseClicked(evt);
            }
        });

        btnCal5.setBackground(new java.awt.Color(102, 153, 255));
        btnCal5.setText("5");
        btnCal5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal5MouseClicked(evt);
            }
        });

        btnCal6.setBackground(new java.awt.Color(102, 153, 255));
        btnCal6.setText("6");
        btnCal6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal6MouseClicked(evt);
            }
        });

        btnCal0.setBackground(new java.awt.Color(102, 153, 255));
        btnCal0.setText("0");
        btnCal0.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal0MouseClicked(evt);
            }
        });

        btnCal00.setBackground(new java.awt.Color(102, 153, 255));
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
            }
        });

        btnCal3.setBackground(new java.awt.Color(102, 153, 255));
        btnCal3.setText("3");
        btnCal3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal3MouseClicked(evt);
            }
        });
        btnCal3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCal3ActionPerformed(evt);
            }
        });

        btnCalBackSpace.setBackground(new java.awt.Color(102, 153, 255));
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalBackSpaceMouseClicked(evt);
            }
        });
        btnCalBackSpace.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalBackSpaceActionPerformed(evt);
            }
        });

        btnCalDot.setBackground(new java.awt.Color(102, 153, 255));
        btnCalDot.setText(".");
        btnCalDot.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalDotMouseClicked(evt);
            }
        });

        btnCal1.setBackground(new java.awt.Color(102, 153, 255));
        btnCal1.setText("1");
        btnCal1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal1MouseClicked(evt);
            }
        });

        btnCal2.setBackground(new java.awt.Color(102, 153, 255));
        btnCal2.setText("2");
        btnCal2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal2MouseClicked(evt);
            }
        });
        btnCal2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCal2ActionPerformed(evt);
            }
        });

        btnCalEnter.setBackground(new java.awt.Color(102, 153, 255));
        btnCalEnter.setText("Enter");
        btnCalEnter.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalEnterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelNumericKeyPadLayout = new javax.swing.GroupLayout(panelNumericKeyPad);
        panelNumericKeyPad.setLayout(panelNumericKeyPadLayout);
        panelNumericKeyPadLayout.setHorizontalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                                .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                                .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        panelNumericKeyPadLayout.setVerticalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        panelFormBody1.add(panelNumericKeyPad);
        panelNumericKeyPad.setBounds(270, 390, 220, 180);

        paneLChequeDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        paneLChequeDetails.setOpaque(false);

        lblChequeNo.setText("Cheque No.   ");

        txtChequeNo.setPreferredSize(new java.awt.Dimension(50, 25));
        txtChequeNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtChequeNoMouseClicked(evt);
            }
        });

        lblBankName.setText("Bank Name       ");

        txtBankName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBankNameMouseClicked(evt);
            }
        });

        lblChequeDate.setText("Date");

        dteCheque.setPreferredSize(new java.awt.Dimension(112, 25));

        javax.swing.GroupLayout paneLChequeDetailsLayout = new javax.swing.GroupLayout(paneLChequeDetails);
        paneLChequeDetails.setLayout(paneLChequeDetailsLayout);
        paneLChequeDetailsLayout.setHorizontalGroup(
            paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneLChequeDetailsLayout.createSequentialGroup()
                .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paneLChequeDetailsLayout.createSequentialGroup()
                        .addComponent(lblChequeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dteCheque, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                    .addGroup(paneLChequeDetailsLayout.createSequentialGroup()
                        .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBankName)
                            .addComponent(txtChequeNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        paneLChequeDetailsLayout.setVerticalGroup(
            paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneLChequeDetailsLayout.createSequentialGroup()
                .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneLChequeDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblChequeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFormBody1.add(paneLChequeDetails);
        paneLChequeDetails.setBounds(20, 410, 240, 100);

        txtAdvOrderNo.setEditable(false);
        txtAdvOrderNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAdvOrderNoMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtAdvOrderNo);
        txtAdvOrderNo.setBounds(370, 50, 110, 30);

        lblAdvOrderNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAdvOrderNo.setText("Adv Order No.        :");
        panelFormBody1.add(lblAdvOrderNo);
        lblAdvOrderNo.setBounds(220, 50, 140, 30);

        lblOrderDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderDate.setText("Order Date :");
        panelFormBody1.add(lblOrderDate);
        lblOrderDate.setBounds(330, 90, 80, 30);
        panelFormBody1.add(lblOrderDateDisplay);
        lblOrderDateDisplay.setBounds(400, 90, 140, 30);

        lblOrderAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderAmt.setText("Order Amount :");
        panelFormBody1.add(lblOrderAmt);
        lblOrderAmt.setBounds(570, 50, 100, 30);

        lblOrderAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblOrderAmount.setForeground(new java.awt.Color(153, 0, 0));
        panelFormBody1.add(lblOrderAmount);
        lblOrderAmount.setBounds(680, 50, 110, 30);

        tblSettlement.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSettlement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Code", "Name", "Amount", "Card Number", "Expriy Date", "Paid Amount", "Coupon Remark", "flag"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblSettlement.setRowHeight(25);
        tblSettlement.getTableHeader().setReorderingAllowed(false);
        scrTax1.setViewportView(tblSettlement);
        if (tblSettlement.getColumnModel().getColumnCount() > 0)
        {
            tblSettlement.getColumnModel().getColumn(0).setMinWidth(0);
            tblSettlement.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblSettlement.getColumnModel().getColumn(0).setMaxWidth(0);
            tblSettlement.getColumnModel().getColumn(1).setMinWidth(150);
            tblSettlement.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblSettlement.getColumnModel().getColumn(1).setMaxWidth(150);
            tblSettlement.getColumnModel().getColumn(2).setMinWidth(70);
            tblSettlement.getColumnModel().getColumn(2).setPreferredWidth(70);
            tblSettlement.getColumnModel().getColumn(2).setMaxWidth(70);
            tblSettlement.getColumnModel().getColumn(3).setMinWidth(70);
            tblSettlement.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblSettlement.getColumnModel().getColumn(3).setMaxWidth(70);
            tblSettlement.getColumnModel().getColumn(4).setMinWidth(100);
            tblSettlement.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblSettlement.getColumnModel().getColumn(4).setMaxWidth(100);
            tblSettlement.getColumnModel().getColumn(5).setMinWidth(100);
            tblSettlement.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblSettlement.getColumnModel().getColumn(5).setMaxWidth(100);
            tblSettlement.getColumnModel().getColumn(6).setMinWidth(100);
            tblSettlement.getColumnModel().getColumn(6).setPreferredWidth(100);
            tblSettlement.getColumnModel().getColumn(6).setMaxWidth(100);
            tblSettlement.getColumnModel().getColumn(7).setMinWidth(0);
            tblSettlement.getColumnModel().getColumn(7).setPreferredWidth(0);
            tblSettlement.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        panelFormBody1.add(scrTax1);
        scrTax1.setBounds(270, 240, 520, 130);

        panelCoupen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelCoupen.setOpaque(false);

        lblAmt.setText("Amount");

        txtCoupenAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblRemarks.setText("Remark");

        txtRemark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRemarkMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelCoupenLayout = new javax.swing.GroupLayout(panelCoupen);
        panelCoupen.setLayout(panelCoupenLayout);
        panelCoupenLayout.setHorizontalGroup(
            panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoupenLayout.createSequentialGroup()
                .addGroup(panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAmt, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(lblRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRemark, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addGroup(panelCoupenLayout.createSequentialGroup()
                        .addComponent(txtCoupenAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelCoupenLayout.setVerticalGroup(
            panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoupenLayout.createSequentialGroup()
                .addGroup(panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCoupenAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(panelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        panelFormBody1.add(panelCoupen);
        panelCoupen.setBounds(20, 510, 240, 60);

        btnSettle4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSettle4.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle4MouseClicked(evt);
            }
        });
        panelFormBody1.add(btnSettle4);
        btnSettle4.setBounds(400, 170, 100, 40);

        lblTotalAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalAmount.setText("Total");
        panelFormBody1.add(lblTotalAmount);
        lblTotalAmount.setBounds(630, 380, 60, 30);
        panelFormBody1.add(lblTotalAmt);
        lblTotalAmt.setBounds(700, 380, 80, 30);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnCancel);
        btnCancel.setBounds(690, 560, 90, 40);

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintActionPerformed(evt);
            }
        });
        btnPrint.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintKeyPressed(evt);
            }
        });
        panelFormBody1.add(btnPrint);
        btnPrint.setBounds(560, 560, 90, 40);

        btnPrevSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevSettlementMode.setText("<<<");
        btnPrevSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevSettlementModeActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnPrevSettlementMode);
        btnPrevSettlementMode.setBounds(0, 170, 60, 40);

        btnNextSettlementMode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNextSettlementMode.setForeground(new java.awt.Color(255, 255, 255));
        btnNextSettlementMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextSettlementMode.setText(">>>");
        btnNextSettlementMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextSettlementMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextSettlementMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextSettlementModeActionPerformed(evt);
            }
        });
        panelFormBody1.add(btnNextSettlementMode);
        btnNextSettlementMode.setBounds(510, 170, 60, 40);
        panelFormBody1.add(lblRefundAmount);
        lblRefundAmount.setBounds(700, 480, 80, 30);

        lblRefund.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefund.setText("Refund");
        panelFormBody1.add(lblRefund);
        lblRefund.setBounds(630, 480, 60, 30);

        lblPromoAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoAmt.setText("Promotion Amount :");
        panelFormBody1.add(lblPromoAmt);
        lblPromoAmt.setBounds(550, 90, 120, 30);
        panelFormBody1.add(lblPromoAmount);
        lblPromoAmount.setBounds(680, 90, 110, 30);

        lblTaxAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxAmt.setText("Tax Amount  :");
        panelFormBody1.add(lblTaxAmt);
        lblTaxAmt.setBounds(580, 130, 90, 30);
        panelFormBody1.add(lblTaxAmount);
        lblTaxAmount.setBounds(680, 130, 110, 30);

        lblFinalAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        panelFormBody1.add(lblFinalAmount);
        lblFinalAmount.setBounds(680, 170, 110, 30);

        lblTaxAmt1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxAmt1.setText("Final Amount :");
        panelFormBody1.add(lblTaxAmt1);
        lblTaxAmt1.setBounds(580, 170, 90, 30);

        lblTotalAmount1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalAmount1.setText("Balance");
        panelFormBody1.add(lblTotalAmount1);
        lblTotalAmount1.setBounds(630, 430, 60, 30);
        panelFormBody1.add(txtBalance);
        txtBalance.setBounds(700, 430, 80, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFormBody1, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFormBody1, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtRecptNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRecptNoMouseClicked
        funSelectAdvOrderNo();
    }//GEN-LAST:event_txtRecptNoMouseClicked

    private void btnSettle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle1MouseClicked

        procSettlementBtnClick(btnSettle1.getText());
    }//GEN-LAST:event_btnSettle1MouseClicked

    private void btnSettle1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettle1MouseEntered

    private void btnSettle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle2MouseClicked

        procSettlementBtnClick(btnSettle2.getText());
    }//GEN-LAST:event_btnSettle2MouseClicked

    private void btnSettle3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle3MouseClicked

        procSettlementBtnClick(btnSettle3.getText());
    }//GEN-LAST:event_btnSettle3MouseClicked

    private void btnSettle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettle3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettle3ActionPerformed

    private void txtCardNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardNameMouseClicked
        if (txtCardName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Card Name").setVisible(true);
            txtCardName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtCardName.getText(), "1", "Enter Card Name").setVisible(true);
            txtCardName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtCardNameMouseClicked

    private void txtAdvanceDepositMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAdvanceDepositMouseClicked
        // TODO add your handling code here:
        textValue1 = txtAdvanceDeposit.getText();
        amountBox = "PaidAmount";
    }//GEN-LAST:event_txtAdvanceDepositMouseClicked

    private void txtAdvanceDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdvanceDepositActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdvanceDepositActionPerformed

    private void txtPaidAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPaidAmountMouseClicked
        // TODO add your handling code here:
        textValue1 = "";
        txtPaidAmount.setText("");
        amountBox = "PaidAmt";
    }//GEN-LAST:event_txtPaidAmountMouseClicked

    private void btnCal7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal7MouseClicked

        procNumericValue(btnCal7.getText());
    }//GEN-LAST:event_btnCal7MouseClicked

    private void btnCal8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal8MouseClicked

        procNumericValue(btnCal8.getText());
    }//GEN-LAST:event_btnCal8MouseClicked

    private void btnCal9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal9MouseClicked

        procNumericValue(btnCal9.getText());
    }//GEN-LAST:event_btnCal9MouseClicked

    private void btnCalClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalClearMouseClicked
        // TODO add your handling code here:
        funClearBtnPressed();
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked

        procNumericValue(btnCal4.getText());
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked

        procNumericValue(btnCal5.getText());
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked

        procNumericValue(btnCal6.getText());
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked

        procNumericValue(btnCal0.getText());
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked

        procNumericValue(btnCal00.getText());
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked

        procNumericValue(btnCal3.getText());
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal3ActionPerformed

    private void btnCalBackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalBackSpaceMouseClicked
        // TODO add your handling code here:
        funBackspaceBtnPressed();
    }//GEN-LAST:event_btnCalBackSpaceMouseClicked

    private void btnCalBackSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalBackSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalBackSpaceActionPerformed

    private void btnCalDotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalDotMouseClicked

        procNumericValue(btnCalDot.getText());
    }//GEN-LAST:event_btnCalDotMouseClicked

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked

        procNumericValue(btnCal1.getText());
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked
        // TODO add your handling code here:
        procNumericValue(btnCal2.getText());
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCal2ActionPerformed

    private void btnCalEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalEnterMouseClicked
        // TODO add your handling code here:
        funEnterButtonPressed();
    }//GEN-LAST:event_btnCalEnterMouseClicked

    private void txtChequeNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtChequeNoMouseClicked
        if (txtChequeNo.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter Cheque No").setVisible(true);
            txtChequeNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtChequeNo.getText(), "Long", "Enter Cheque No").setVisible(true);
            txtChequeNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtChequeNoMouseClicked

    private void txtBankNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBankNameMouseClicked

        if (txtBankName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Bank Name").setVisible(true);
            txtBankName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtBankName.getText(), "1", "Enter Bank Name").setVisible(true);
            txtBankName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtBankNameMouseClicked

    private void txtAdvOrderNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAdvOrderNoMouseClicked

    }//GEN-LAST:event_txtAdvOrderNoMouseClicked

    private void txtRemarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRemarkMouseClicked
        if (txtRemark.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Remark").setVisible(true);
            txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtRemark.getText(), "1", "Enter Remark").setVisible(true);
            txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtRemarkMouseClicked

    private void btnSettle4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle4MouseClicked
        // TODO add your handling code here:
        procSettlementBtnClick(btnSettle4.getText());
    }//GEN-LAST:event_btnSettle4MouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        //funCheckAdvanceOrder();
        clsGlobalVarClass.hmActiveForms.remove("Advance Booking Receipt");
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnPrevSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevSettlementModeActionPerformed
        // TODO add your handling code here:
        funPrevSettlementMode();
    }//GEN-LAST:event_btnPrevSettlementModeActionPerformed

    private void btnNextSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextSettlementModeActionPerformed
        // TODO add your handling code here:
        funNextSettlementMode();
    }//GEN-LAST:event_btnNextSettlementModeActionPerformed

    private void txtAdvanceDepositKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvanceDepositKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funEnterButtonPressed();
        }
    }//GEN-LAST:event_txtAdvanceDepositKeyPressed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        funPrintButtonPressed();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Advance Booking Receipt");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Advance Booking Receipt");
    }//GEN-LAST:event_formWindowClosing

    private void btnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrintKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funPrintButtonPressed();
        }
    }//GEN-LAST:event_btnPrintKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCal0;
    private javax.swing.JButton btnCal00;
    private javax.swing.JButton btnCal1;
    private javax.swing.JButton btnCal2;
    private javax.swing.JButton btnCal3;
    private javax.swing.JButton btnCal4;
    private javax.swing.JButton btnCal5;
    private javax.swing.JButton btnCal6;
    private javax.swing.JButton btnCal7;
    private javax.swing.JButton btnCal8;
    private javax.swing.JButton btnCal9;
    private javax.swing.JButton btnCalBackSpace;
    private javax.swing.JButton btnCalClear;
    private javax.swing.JButton btnCalDot;
    private javax.swing.JButton btnCalEnter;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNextSettlementMode;
    private javax.swing.JButton btnPrevSettlementMode;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSettle1;
    private javax.swing.JButton btnSettle2;
    private javax.swing.JButton btnSettle3;
    private javax.swing.JButton btnSettle4;
    private com.toedter.calendar.JDateChooser dteCheque;
    private com.toedter.calendar.JDateChooser dteExpiry;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel lblAdvDeposite;
    private javax.swing.JLabel lblAdvOrderNo;
    private javax.swing.JLabel lblAmt;
    private javax.swing.JLabel lblBankName;
    private javax.swing.JLabel lblCardExpirtDate;
    private javax.swing.JLabel lblCardName;
    private javax.swing.JLabel lblChequeDate;
    private javax.swing.JLabel lblChequeNo;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFinalAmount;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOrderAmount;
    private javax.swing.JLabel lblOrderAmt;
    private javax.swing.JLabel lblOrderDate;
    private javax.swing.JLabel lblOrderDateDisplay;
    private javax.swing.JLabel lblPaidAmt;
    private javax.swing.JLabel lblPayMode;
    private javax.swing.JLabel lblPayModeHeader;
    private javax.swing.JLabel lblPaymentMode;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblPromoAmount;
    private javax.swing.JLabel lblPromoAmt;
    private javax.swing.JLabel lblReceiptNo;
    private javax.swing.JLabel lblRefund;
    private javax.swing.JLabel lblRefundAmount;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblTaxAmount;
    private javax.swing.JLabel lblTaxAmt;
    private javax.swing.JLabel lblTaxAmt1;
    private javax.swing.JLabel lblTotalAmount;
    private javax.swing.JLabel lblTotalAmount1;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel paneLChequeDetails;
    private javax.swing.JPanel panelAmountDetails;
    private javax.swing.JPanel panelCardDetails;
    private javax.swing.JPanel panelCoupen;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelFormBody1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelNumericKeyPad;
    private javax.swing.JPanel panelPayModeHeader;
    private javax.swing.JScrollPane scrTax1;
    private javax.swing.JTable tblSettlement;
    private javax.swing.JTextField txtAdvOrderNo;
    private javax.swing.JTextField txtAdvanceDeposit;
    private javax.swing.JLabel txtBalance;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtCardName;
    private javax.swing.JTextField txtChequeNo;
    private javax.swing.JTextField txtCoupenAmt;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtPaidAmount;
    private javax.swing.JTextField txtRecptNo;
    private javax.swing.JTextField txtRefund;
    private javax.swing.JTextField txtRemark;
    // End of variables declaration//GEN-END:variables

}
