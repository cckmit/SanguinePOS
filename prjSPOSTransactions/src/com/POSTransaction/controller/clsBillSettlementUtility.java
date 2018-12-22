/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.controller;

import com.POSGlobal.controller.clsBenowIntegration;
import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsBillHd;
import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsBillSeriesBillDtl;
import com.POSGlobal.controller.clsBillTaxDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSTransaction.view.frmBillSettlement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.rowset.CachedRowSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajim
 */
public class clsBillSettlementUtility
{

    private frmBillSettlement objFrmBillSettlement;
    private clsUtility objUtility;
    private clsUtility2 objUtility2;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public clsBillSettlementUtility()
    {
	objUtility = new clsUtility();
	objUtility2 = new clsUtility2();
    }

    public clsBillSettlementUtility(frmBillSettlement objFrmBillSettlement)
    {
	this.objFrmBillSettlement = objFrmBillSettlement;
	objUtility = new clsUtility();
	objUtility2 = new clsUtility2();
    }

    public void funSaveBillForItemsWithoutSettleBill()
    {
	try
	{
	    if (objFrmBillSettlement.getTblItemTable().getRowCount() == 0)
	    {
		new frmOkPopUp(null, "", "Please Select Item", 1).setVisible(true);
	    }
	    else
	    {
		//Bill series code 
		Map<String, List<clsBillItemDtl>> mapBillSeries = null;
		List listBillSeriesBillDtl = new ArrayList<clsBillSeriesBillDtl>();
		objFrmBillSettlement.setListBillSeriesBillDtl(listBillSeriesBillDtl);

		if (clsGlobalVarClass.gEnableBillSeries && (mapBillSeries = funGetBillSeriesList()).size() > 0)
		{
		    if (mapBillSeries.containsKey("NoBillSeries"))
		    {
			new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
			funUpdateTableStatus("Occupied");
			return;
		    }
		    //to calculate PAX per bill if there is a bill series or bill splited
		    Map<Integer, Integer> mapPAXPerBill = objUtility2.funGetPAXPerBill(objFrmBillSettlement.getPaxNo(), mapBillSeries.size());

		    Iterator<Map.Entry<String, List<clsBillItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
		    int billCount = 0;
		    while (billSeriesIt.hasNext())
		    {
			Map.Entry<String, List<clsBillItemDtl>> billSeriesEntry = billSeriesIt.next();
			String key = billSeriesEntry.getKey();
			List<clsBillItemDtl> values = billSeriesEntry.getValue();

			int intBillSeriesPaxNo = 0;
			if (mapPAXPerBill.containsKey(billCount))
			{
			    intBillSeriesPaxNo = mapPAXPerBill.get(billCount);
			}
			objFrmBillSettlement.setIntBillSeriesPaxNo(intBillSeriesPaxNo);

			funSaveBillForBillForItems(key, values);

			billCount++;
		    }
		    //clear temp kot table
		    if (objFrmBillSettlement.isFlagBillForItems())
		    {
			funUpdateKOTTempTable();
		    }
		    else
		    {
			funTruncateKOTTempTable();
		    }

		    //save bill series bill detail
		    for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
		    {
			clsBillSeriesBillDtl objBillSeriesBillDtl = objFrmBillSettlement.getListBillSeriesBillDtl().get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();

			String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl "
				+ "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag"
				+ ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) "
				+ "values ('" + clsGlobalVarClass.gPOSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'"
				+ ",'" + hdBillNo + "','" + funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'"
				+ ",'" + grandTotal + "'" + ",'" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + objUtility.funGetPOSDateForTransaction() + "')";
			clsGlobalVarClass.dbMysql.execute(sqlInsertBillSeriesDtl);

			String sql = "select * "
				+ "from tblbillcomplementrydtl a "
				+ "where a.strBillNo='" + hdBillNo + "' "
				+ "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
				+ "and a.strType='Complimentary'; ";
			ResultSet rsIsComplementary = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsIsComplementary.next())
			{
			    String sqlUpdate = "update tblbillseriesbilldtl set dblGrandTotal=0.00 where strHdBillNo='" + hdBillNo + "' "
				    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
			    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			}
			rsIsComplementary.close();
		    }

		    for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
		    {
			clsBillSeriesBillDtl objBillSeriesBillDtl = objFrmBillSettlement.getListBillSeriesBillDtl().get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			boolean flgHomeDelPrint = objBillSeriesBillDtl.isFlgHomeDelPrint();
			if (objFrmBillSettlement.getStrButtonClicked().equals("Settle"))
			{
			    if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && objFrmBillSettlement.isIsDirectSettleFromMakeBill())
			    {
				objFrmBillSettlement.funOnlyBillSettle();
				if (clsGlobalVarClass.gHomeDelSMSYN)
				{
				    funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
				}
				if (flgHomeDelPrint == true)
				{
				    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
				    {
					objUtility.funPrintBill(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				    }
				}
				else
				{
				    funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
				}
			    }
			    else
			    {
				objFrmBillSettlement.funOnlyBillSettle();
			    }
			}
			else if (objFrmBillSettlement.getStrButtonClicked().equals("Print"))
			{
			    if (clsGlobalVarClass.gHomeDelSMSYN)
			    {
				funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			    }
			    if (flgHomeDelPrint == true)
			    {
				objUtility.funPrintBill(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			    else
			    {
				funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
			    }
			}
			if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5"))//XO
			{
			    break;
			}
		    }
		}
		else//if no bill series
		{
		    objFrmBillSettlement.setIntBillSeriesPaxNo(objFrmBillSettlement.getPaxNo());

		    String operationType = "DineIn";
		    String transactionType = "Dine In";//For saving different transaction on same Bill in tblBillHd table in database
		    boolean flgHomeDelPrint = false;
		    objFrmBillSettlement.setVoucherNo(objFrmBillSettlement.getLblVoucherNo().getText());
		    objFrmBillSettlement.revalidate();

		    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
		    {
			operationType = "TakeAway";
			transactionType = transactionType + "," + operationType;
		    }
		    if (null != clsGlobalVarClass.hmTakeAway.get(objFrmBillSettlement.getTableNo()))
		    {
			operationType = "TakeAway";
			clsGlobalVarClass.hmTakeAway.remove(objFrmBillSettlement.getTableNo());
		    }

		    funGenerateBillNo();
		    //last order no
		    int intLastOrderNo = objUtility2.funGetLastOrderNo();

		    objFrmBillSettlement.funSaveBillDiscountDetail(objFrmBillSettlement.getVoucherNo());

		    StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
		    int seq1 = sb.lastIndexOf(" ");
		    String split = sb.substring(0, seq1);
		    String billDateTime = split;

		    String counterCode = "NA";
		    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
		    {
			counterCode = clsGlobalVarClass.gCounterCode;
		    }

		    String sqlCheckHomeDelivery = "select strHomeDelivery,strCustomerCode "
			    + "from tblitemrtemp where strTableNo='" + objFrmBillSettlement.getTableNo() + "' "
			    + "group by strTableNo ;";
		    ResultSet rsHomeDeleveryCheck = null;
		    String customerCode = "";
		    rsHomeDeleveryCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckHomeDelivery);
		    if (rsHomeDeleveryCheck.next())
		    {
			String homeDeliveryYesNo = rsHomeDeleveryCheck.getString(1);
			customerCode = rsHomeDeleveryCheck.getString(2);
			rsHomeDeleveryCheck.close();
			if ("Yes".equalsIgnoreCase(homeDeliveryYesNo))
			{
			    operationType = "HomeDelivery";
			    transactionType = transactionType + "," + operationType;
			    Calendar c = Calendar.getInstance();
			    int hh = c.get(Calendar.HOUR);
			    int mm = c.get(Calendar.MINUTE);
			    int ss = c.get(Calendar.SECOND);
			    int ap = c.get(Calendar.AM_PM);

			    String ampm = "AM";
			    if (ap == 1)
			    {
				ampm = "PM";
			    }
			    String currentTime = hh + ":" + mm + ":" + ss + ":" + ampm;
			    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode"
				    + ",strDPCode,dteDate,tmeTime,strPOSCode,strCustAddressLine1,strCustAddressLine2"
				    + ",strCustAddressLine3,strCustAddressLine4,strCustCity,strClientCode,dblHomeDeliCharge)"
				    + " values('" + objFrmBillSettlement.getVoucherNo() + "','" + customerCode + "','" + objFrmBillSettlement.getDelPersonCode() + "','"
				    + objUtility.funGetPOSDateForTransaction() + "','" + currentTime + "','"
				    + clsGlobalVarClass.gPOSCode + "','" + objFrmBillSettlement.getCustAddType() + "','','','','','" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + objFrmBillSettlement.getDeliveryCharge() + "')";

			    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
			    clsGlobalVarClass.gCustomerCode = null;
			    clsGlobalVarClass.gDeliveryCharges = 0.00;
			    flgHomeDelPrint = true;
			}
		    }
		    if (null != objFrmBillSettlement.getCustCode())
		    {
			customerCode = objFrmBillSettlement.getCustCode();
		    }
		    double dblTotalTaxAmt = 0;

		    for (clsTaxCalculationDtls objTaxCalculationDtls : objFrmBillSettlement.getArrListTaxCal())
		    {
			if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
			{
			    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
			}
		    }
		    objFrmBillSettlement.setDblTotalTaxAmt(dblTotalTaxAmt);

		    Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
		    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().size() > 0)
		    {
			hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
			for (Map.Entry<String, clsBillDtl> entry : objFrmBillSettlement.getHmComplimentaryBillItemDtl().entrySet())
			{
			    hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
			}
		    }

		    List<clsPlayZoneItems> listPlayZoneItems = null;
		    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
		    {
			listPlayZoneItems = funApplyPlayZonePrice();
		    }

		    List<String> listBillItemDtl = new ArrayList<String>();
		    String custName = "", cardNo = "", orderProcessTime, orderPickupTime;
		    String sqlItemDtl = "select strItemCode,upper(strItemName),dblItemQuantity "
			    + " ,dblAmount,strKOTNo,strManualKOTNo,Time(dteDateCreated),strCustomerCode "
			    + " ,strCustomerName,strCounterCode,strWaiterNo,strPromoCode,dblRate,strCardNo,tmeOrderProcessing,tmeOrderPickup "
			    + " from tblitemrtemp a"
			    + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and strTableNo='" + objFrmBillSettlement.getTableNo() + "' "
			    + " and strNCKotYN='N' "
			    + " and a.strItemCode in " + funGetItemCodeList() + "  "
			    + " order by strTableNo ASC";

		    ResultSet rsItemKOTDTL = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);
		    String kot = "";
		    while (rsItemKOTDTL.next())
		    {
			String iCode = rsItemKOTDTL.getString(1);
			String iName = rsItemKOTDTL.getString(2);
			double iQty = 1;
			String iAmt = "0.00";

			Map<String, clsBillItemDtl> hmBillItemDtl = objFrmBillSettlement.getHmBillItemDtl();
			if (iName.startsWith("-->"))
			{

			    if (hmBillItemDtl.containsKey(iCode + "!" + iName))
			    {
				iQty = hmBillItemDtl.get(iCode + "!" + iName).getQuantity();
				iAmt = String.valueOf(hmBillItemDtl.get(iCode + "!" + iName).getAmount());
			    }
			    else
			    {
				continue;
			    }

			}
			else
			{
			    if (hmBillItemDtl.containsKey(iCode))
			    {
				iQty = hmBillItemDtl.get(iCode).getQuantity();
				iAmt = String.valueOf(hmBillItemDtl.get(iCode).getAmount());
			    }
			    else
			    {
				continue;
			    }

			}

			if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
			{
			    if (null != listPlayZoneItems)
			    {
				for (clsPlayZoneItems objPlayZoneItems : listPlayZoneItems)
				{
				    if (iCode.equals(objPlayZoneItems.getStrItemCode()))
				    {
					double rate = objPlayZoneItems.getDblRate();
					iAmt = String.valueOf(rate * iQty);
				    }
				}
			    }
			}

			if (null != hmComplimentaryBillItemDtlTemp && hmComplimentaryBillItemDtlTemp.containsKey(iCode))
			{
			    double complQty = hmComplimentaryBillItemDtlTemp.get(iCode).getDblComplQty();
			    if (complQty == iQty || complQty < iQty)
			    {
				double amtToSave = rsItemKOTDTL.getDouble(4) - (hmComplimentaryBillItemDtlTemp.get(iCode).getDblComplQty() * hmComplimentaryBillItemDtlTemp.get(iCode).getDblRate());
				iAmt = String.valueOf(amtToSave);
				/*
                                 * double chargedQty = iQty -
                                 * hmComplimentaryBillItemDtlTemp.get(iCode).getDblComplQty();
                                 * if (chargedQty > 0) { iQty = chargedQty; }
				 */
				hmComplimentaryBillItemDtlTemp.remove(iCode);
			    }
			    else if (iQty < complQty)
			    {
				double amtToSave = rsItemKOTDTL.getDouble(4) - (iQty * hmComplimentaryBillItemDtlTemp.get(iCode).getDblRate());
				iAmt = String.valueOf(amtToSave);
				double newComplQty = complQty - iQty;
				hmComplimentaryBillItemDtlTemp.get(iCode).setDblComplQty(newComplQty);
			    }
			}
			double rate = rsItemKOTDTL.getDouble(13);
			kot = rsItemKOTDTL.getString(5);
			String manualKOTNo = rsItemKOTDTL.getString(6);
			billDateTime = split + " " + rsItemKOTDTL.getString(7);

			objFrmBillSettlement.setCustCode(rsItemKOTDTL.getString(8));
			custName = rsItemKOTDTL.getString(9);
			String promoCode = rsItemKOTDTL.getString(12);
			cardNo = rsItemKOTDTL.getString(14);
			orderProcessTime = rsItemKOTDTL.getString(15);
			orderPickupTime = rsItemKOTDTL.getString(16);
			String sqlInsertBillDtl = "";

			if (!iCode.contains("M"))
			{
			    if (objFrmBillSettlement.getHmPromoItem().size() > 0)
			    {
				if (null != objFrmBillSettlement.getHmPromoItem().get(iCode))
				{
				    clsPromotionItems objPromoItemDtl = objFrmBillSettlement.getHmPromoItem().get(iCode);
				    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
				    {
					double freeQty = objPromoItemDtl.getFreeItemQty();
					double freeAmt = freeQty * rate;

					promoCode = objPromoItemDtl.getPromoCode();
					String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						+ ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						+ ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						+ "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "'"
						+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					objFrmBillSettlement.getHmPromoItem().remove(iCode);
				    }
				    else if (objPromoItemDtl.getPromoType().equals("Discount"))
				    {
					if (objPromoItemDtl.getDiscType().equals("Value"))
					{
					    double freeQty = objPromoItemDtl.getFreeItemQty();
					    double amount = freeQty * rate;
					    double discAmt = objPromoItemDtl.getDiscAmt();

					    promoCode = objPromoItemDtl.getPromoCode();
					    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						    + ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						    + "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "' "
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "' "
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "' "
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    objFrmBillSettlement.getHmPromoItem().remove(iCode);
					}
					else
					{
					    iAmt = String.valueOf(iQty * rate);
					    double amount = iQty * rate;
					    double discAmt = amount * (objPromoItemDtl.getDiscPer() / 100);

					    promoCode = objPromoItemDtl.getPromoCode();
					    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						    + ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						    + "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "'"
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "'"
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    objFrmBillSettlement.getHmPromoItem().remove(iCode);
					}
				    }
				}
			    }

			    String amt = "0.00";
			    boolean flgComplimentaryBill = false;
			    if (objFrmBillSettlement.getHmSettlemetnOptions().size() == 1)
			    {
				for (clsSettelementOptions obj : objFrmBillSettlement.getHmSettlemetnOptions().values())
				{
				    if (obj.getStrSettelmentType().equals("Complementary"))
				    {
					flgComplimentaryBill = true;
					break;
				    }
				}
			    }
			    if (!flgComplimentaryBill)
			    {
				amt = iAmt;
			    }
			    double discAmt = 0.00;
			    double discPer = 0.00;
			    if (!iCode.contains("M"))
			    {
				discAmt = objFrmBillSettlement.getHmBillItemDtl().get(iCode).getDiscountAmount() * iQty;
				discPer = objFrmBillSettlement.getHmBillItemDtl().get(iCode).getDiscountPercentage();
			    }

			    if (iQty > 0)
			    {
				if (iName.startsWith("=>"))
				{
				    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
					    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
					    + ",strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo"
					    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
					    + "values('" + iCode + "','" + iName + "','" + objFrmBillSettlement.getVoucherNo() + "'," + rate + ",'" + iQty
					    + "','" + amt + "','0.00','" + billDateTime + "','" + kot + "','"
					    + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','Y','" + promoCode + "'"
					    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
					    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessTime + "','" + orderPickupTime + "')";
				}
				else
				{
				    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
					    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
					    + ",strClientCode,strManualKOTNo,strPromoCode,strCounterCode,strWaiterNo"
					    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
					    + "values('" + iCode + "','" + iName + "','" + objFrmBillSettlement.getVoucherNo() + "'," + rate + ""
					    + ",'" + iQty + "','" + amt + "','0.00','" + billDateTime + "','" + kot + "'"
					    + ",'" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','" + promoCode + "'"
					    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
					    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessTime + "','" + orderPickupTime + "')";
				}
				clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
				if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().containsKey(iCode))
				{
				    clsBillDtl objBillDtl = objFrmBillSettlement.getHmComplimentaryBillItemDtl().get(iCode);
				    objBillDtl.setDblRate(rate);
				    objBillDtl.setStrBillNo(objFrmBillSettlement.getVoucherNo());
				    objBillDtl.setDteBillDate(billDateTime);
				    objBillDtl.setStrKOTNo(kot);
				    objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				    objBillDtl.setStrManualKOTNo(manualKOTNo);
				    objBillDtl.setStrPromoCode(promoCode);
				    objBillDtl.setStrCounterCode(rsItemKOTDTL.getString(10));
				    objBillDtl.setStrWaiterNo(rsItemKOTDTL.getString(11));
				    objBillDtl.setDblDiscountAmt(discAmt);
				    objBillDtl.setDblDiscountPer(discPer);
				    objBillDtl.setDblTaxAmount(0.00);
				    objBillDtl.setDteBillSettleDate(billDateTime);
				    objBillDtl.setTmeOrderProcessing(orderProcessTime);

				    objFrmBillSettlement.getHmComplimentaryBillItemDtl().put(iCode, objBillDtl);

				    listBillItemDtl.add(iCode);
				}
			    }
			}
			if (iCode.contains("M"))
			{
			    StringBuilder sb1 = new StringBuilder(iCode);
			    int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
			    String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code
			    double amt = Double.parseDouble(iAmt);

			    double modDiscAmt = 0, modDiscPer = 0;
			    if (objFrmBillSettlement.getHmBillItemDtl().containsKey(iCode + "!" + iName))
			    {
				modDiscAmt = objFrmBillSettlement.getHmBillItemDtl().get(iCode + "!" + iName).getDiscountAmount() * iQty;
				modDiscPer = objFrmBillSettlement.getHmBillItemDtl().get(iCode + "!" + iName).getDiscountPercentage();
			    }
			    StringBuilder sbTemp = new StringBuilder(iCode);
			    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().containsKey(sbTemp.substring(0, 7).toString()))
			    {
				amt = 0;
			    }

			    String sqlBillModifierDtl = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
				    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
				    + "values('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
				    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + modDiscPer + "','" + modDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
			    //System.out.println(sqlBillModifierDtl);
			    clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
			}
		    }
		    rsItemKOTDTL.close();

		    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
			    + "from tblbillpromotiondtl "
			    + " where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' and strPromoType='ItemWise' ";
		    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
		    while (rsBillPromo.next())
		    {
			double freeQty = rsBillPromo.getDouble(1);
			String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
				+ " from tblbilldtl "
				+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
				+ " and strBillNo='" + objFrmBillSettlement.getVoucherNo() + "'";
			ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
			while (rsBillDtl.next())
			{
			    if (freeQty > 0)
			    {
				double saleQty = rsBillDtl.getDouble(2);
				double saleAmt = rsBillDtl.getDouble(4);
				if (saleQty <= freeQty)
				{
				    freeQty = freeQty - saleQty;
				    double amtToUpdate = saleAmt - (saleQty * rsBillPromo.getDouble(2));
				    String sqlUpdate = "update tblbilldtl set dblAmount= " + amtToUpdate + " "
					    + " where strItemCode='" + rsBillDtl.getString(1) + "' "
					    + "and strKOTNo='" + rsBillDtl.getString(3) + "'";
				    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
				}
				else
				{
				    double amtToUpdate = saleAmt - (freeQty * rsBillPromo.getDouble(2));
				    String sqlUpdate = "update tblbilldtl set dblAmount= " + amtToUpdate + " "
					    + " where strItemCode='" + rsBillDtl.getString(1) + "' "
					    + "and strKOTNo='" + rsBillDtl.getString(3) + "'";
				    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
				    freeQty = 0;
				}
			    }
			}
			rsBillDtl.close();
		    }
		    rsBillPromo.close();

		    double subTotalAmt = 0, grandTotalAmt = 0;
		    String sqlBillDtl = "select sum(dblAmount) from tblbilldtl where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' ";
		    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    if (rsBillDtl.next())
		    {
			subTotalAmt = rsBillDtl.getDouble(1);
		    }
		    rsBillDtl.close();

		    sqlBillDtl = "select sum(dblAmount) from tblbillmodifierdtl where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' ";
		    rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    if (rsBillDtl.next())
		    {
			subTotalAmt += rsBillDtl.getDouble(1);
		    }
		    rsBillDtl.close();

		    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "'";
		    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

		    // insert into tblbilltaxdtl    
		    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

		    for (clsTaxCalculationDtls objTaxCalculationDtls : objFrmBillSettlement.getArrListTaxCal())
		    {
			double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			//totalTaxAmt = totalTaxAmt + dblTaxAmt;
			clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
			objBillTaxDtl.setStrBillNo(objFrmBillSettlement.getVoucherNo());
			objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
			objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
			objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
			objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			listObjBillTaxBillDtls.add(objBillTaxDtl);
		    }

		    objFrmBillSettlement.funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
		    clsUtility obj = new clsUtility();
		    obj.funUpdateBillDtlWithTaxValues(objFrmBillSettlement.getVoucherNo(), "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		    grandTotalAmt = (subTotalAmt - objFrmBillSettlement.getDblDiscountAmt()) + objFrmBillSettlement.getDblTotalTaxAmt() + objFrmBillSettlement.getDeliveryCharge();

		    //start code to calculate roundoff amount and round off by amt
		    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotalAmt);
		    double _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
		    objFrmBillSettlement.setGrandTotalRoundOffBy(_grandTotalRoundOffBy);

		    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		    {
			grandTotalAmt = mapRoundOff.get("roundOffAmt");
		    }
		    //end code to calculate roundoff amount and round off by amt

		    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().size() > 0)
		    {
			objFrmBillSettlement.funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
		    }

		    String sqlInsertBillHd = "insert into tblbillhd"
			    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
			    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType,"
			    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strTableNo"
			    + ",strWaiterNo,strCustomerCode,intShiftCode,intPaxNo,strReasonCode,strRemarks"
			    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strAreaCode"
			    + ",strDiscountRemark,strTakeAwayRemarks,strDiscountOn,strCardNo,strTransactionType,dblRoundOff"
			    + ",intBillSeriesPaxNo,dtBillDate,intOrderNo,strCRMRewardId,dblUSDConverionRate ) "
			    + "values('" + objFrmBillSettlement.getVoucherNo() + "','" + objFrmBillSettlement.getAdvOrderBookingNo() + "','" + objUtility.funGetPOSDateForTransaction() + "','"
			    + clsGlobalVarClass.gPOSCode + "','','" + objFrmBillSettlement.getDblDiscountAmt() + "','"
			    + objFrmBillSettlement.getDblDiscountPer() + "','" + dblTotalTaxAmt + "','" + subTotalAmt + "','"
			    + grandTotalAmt + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
			    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"
			    + clsGlobalVarClass.getCurrentDateTime() + "','"
			    + clsGlobalVarClass.gClientCode + "','" + objFrmBillSettlement.getTableNo() + "','" + objFrmBillSettlement.getWaiterNo() + "'"
			    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
			    + "," + objFrmBillSettlement.getPaxNo() + ",'" + objFrmBillSettlement.getSelectedReasonCode() + "','" + objUtility.funCheckSpecialCharacters(objFrmBillSettlement.getTxtAreaRemark().getText().trim()) + "'"
			    + "," + objFrmBillSettlement.getTxtTip().getText() + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
			    + ",'" + counterCode + "'," + objFrmBillSettlement.getDeliveryCharge() + ",'" + objFrmBillSettlement.getAreaCode() + "'"
			    + ",'" + objUtility.funCheckSpecialCharacters(objFrmBillSettlement.getDiscountRemarks()) + "','','','" + cardNo + "','" + transactionType + "'"
			    + ",'" + _grandTotalRoundOffBy + "','" + objFrmBillSettlement.getIntBillSeriesPaxNo() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
			    + ",'" + intLastOrderNo + "','" + objFrmBillSettlement.getRewardId() + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertBillHd);

		    if (clsGlobalVarClass.gCMSIntegrationYN)
		    {
			if (objFrmBillSettlement.getCustCode().trim().length() > 0)
			{
			    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + objFrmBillSettlement.getCustCode() + "' "
				    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
			    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

			    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
				    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
				    + "values('" + objFrmBillSettlement.getCustCode() + "','" + custName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "')";
			    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
			}
		    }

		    if (objFrmBillSettlement.getStrButtonClicked().equals("Settle"))
		    {
			if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && objFrmBillSettlement.isIsDirectSettleFromMakeBill())
			{
			    objFrmBillSettlement.funOnlyBillSettle();

			    if (clsGlobalVarClass.gHomeDelSMSYN)
			    {
				funSendSMS(objFrmBillSettlement.getVoucherNo(), clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			    }
			    if (flgHomeDelPrint == true)
			    {
				if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
				{
				    objUtility.funPrintBill(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				}
				else
				{
				    objUtility.funPrintBill(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				}
			    }
			    else
			    {
				funSendBillToPrint(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction());
			    }
			}
			else
			{
			    objFrmBillSettlement.funOnlyBillSettle();
			}
		    }
		    else if (objFrmBillSettlement.getStrButtonClicked().equals("Print"))
		    {
			if (flgHomeDelPrint == true)
			{
			    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
			    {
				objUtility.funPrintBill(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			    else
			    {
				objUtility.funPrintBill(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			}
			else
			{
			    funSendBillToPrint(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction());
			}
			if (clsGlobalVarClass.gHomeDelSMSYN)
			{
			    funSendSMS(objFrmBillSettlement.getVoucherNo(), clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			}
		    }

		    if (objFrmBillSettlement.isFlagBillForItems())
		    {
			funUpdateKOTTempTable();
		    }
		    else
		    {
			funTruncateKOTTempTable();
		    }
		    objFrmBillSettlement.dispose();
		}
	    }
	}
	catch (Exception e)
	{

	    objUtility.funWriteErrorLog(e);
	    // clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-24", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
//	    if (null != objFrmBillSettlement.getMakeBillObj())
//	    {
//		makeBillObj = null;
//	    }
//	    if (null != kotObj)
//	    {
//		kotObj = null;
//	    }
	    System.gc();
	}
    }

    /**
     * This method is responsible for to split a item list bill series wise.
     * This method takes a list of item list which is going to be bill and
     * @return a map whose key is bill series code and value is a list of items for this bill series code.
     * 
     * if billseries not found for some items then it will put 'NoBillSeries' as key in map and bill will not generate until update bill series.
     */
    
    public Map<String, List<clsBillItemDtl>> funGetBillSeriesList()
    {
	Map<String, List<clsBillItemDtl>> hmBillSeriesItemList = new HashMap<String, List<clsBillItemDtl>>();
	try
	{

	    StringBuilder sqlBuilder = new StringBuilder();
	    for (Map.Entry<String, clsBillItemDtl> entry : objFrmBillSettlement.getHmBillItemDtl().entrySet())
	    {
		clsBillItemDtl objBillItemDtl = entry.getValue();
		ResultSet rsBillSeriesType = clsGlobalVarClass.dbMysql.executeResultSet(" select * from tblbillseries where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') ");
		boolean isExistsBillSeries = false;
		while (rsBillSeriesType.next())
		{
		    sqlBuilder.setLength(0);
		    sqlBuilder.append("select a.strItemCode,a.strItemName,a.strRevenueHead,b.strPosCode,c.strMenuCode,c.strMenuName "
			    + " ,d.strSubGroupCode,d.strSubGroupName,e.strGroupCode,e.strGroupName "
			    + " from tblitemmaster a,tblmenuitempricingdtl b,tblmenuhd c,tblsubgrouphd d,tblgrouphd e "
			    + " where a.strItemCode=b.strItemCode and b.strMenuCode=c.strMenuCode "
			    + " and a.strSubGroupCode=d.strSubGroupCode and d.strGroupCode=e.strGroupCode ");
		    sqlBuilder.append(" and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' Or b.strPosCode='All') ");
		    sqlBuilder.append(" and a.strItemCode='" + objBillItemDtl.getItemCode().substring(0, 7) + "' ");

		    String billSeriesType = rsBillSeriesType.getString("strType");
		    String filter = " e.strGroupCode ";
		    if (billSeriesType.equalsIgnoreCase("Group"))
		    {
			filter = " e.strGroupCode ";
		    }
		    else if (billSeriesType.equalsIgnoreCase("Sub Group"))
		    {
			filter = " d.strSubGroupCode ";
		    }
		    else if (billSeriesType.equalsIgnoreCase("Menu Head"))
		    {
			filter = " c.strMenuCode ";
		    }
		    else if (billSeriesType.equalsIgnoreCase("Revenue Head"))
		    {
			filter = " a.strRevenueHead ";
		    }
		    else
		    {
			filter = "  ";
		    }
		    sqlBuilder.append(" and " + filter + " IN " + funGetCodes(rsBillSeriesType.getString("strCodes")));
		    sqlBuilder.append(" GROUP BY a.strItemCode; ");

		    ResultSet rsIsExists = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		    if (rsIsExists.next())
		    {
			isExistsBillSeries = true;
			if (hmBillSeriesItemList.containsKey(rsBillSeriesType.getString("strBillSeries")))
			{
			    hmBillSeriesItemList.get(rsBillSeriesType.getString("strBillSeries")).add(objBillItemDtl);
			}
			else
			{
			    List<clsBillItemDtl> listBillSeriesDtl = new ArrayList<clsBillItemDtl>();
			    listBillSeriesDtl.add(objBillItemDtl);
			    hmBillSeriesItemList.put(rsBillSeriesType.getString("strBillSeries"), listBillSeriesDtl);
			}
			break;
		    }
		}
		if (!isExistsBillSeries)
		{
		    if (hmBillSeriesItemList.containsKey("NoBillSeries"))
		    {
			hmBillSeriesItemList.get("NoBillSeries").add(objBillItemDtl);
		    }
		    else
		    {
			List<clsBillItemDtl> listBillSeriesDtl = new ArrayList<clsBillItemDtl>();
			listBillSeriesDtl.add(objBillItemDtl);
			hmBillSeriesItemList.put("NoBillSeries", listBillSeriesDtl);
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{

	    objFrmBillSettlement.setHmBillSeriesItemList(hmBillSeriesItemList);
	    return hmBillSeriesItemList;
	}
    }

    private String funGetCodes(String codes)
    {
	StringBuilder codeBuilder = new StringBuilder("(");
	try
	{
	    String code[] = codes.split(",");
	    for (int i = 0; i < code.length; i++)
	    {
		if (i == 0)
		{
		    codeBuilder.append("'" + code[i] + "'");
		}
		else
		{
		    codeBuilder.append(",'" + code[i] + "'");
		}
	    }
	    codeBuilder.append(")");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return codeBuilder.toString();
	}
    }

    public int funUpdateTableStatus(String status)
    {
	try
	{
	    String sql_updateTableStatus = "";

	    if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
	    {
		if (status.equalsIgnoreCase("Reserve"))
		{
		    status = "Normal";
		}
	    }

	    if ("Normal".equalsIgnoreCase(status))
	    {
		sql_updateTableStatus = "select count(*) from tblitemrtemp where strTableNo='" + objFrmBillSettlement.getTableNo() + "';";
		ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sql_updateTableStatus);
		rsCount.next();
		int count = rsCount.getInt(1);
		rsCount.close();
		if (count == 0)
		{
		    // no table present in tblitemrtemp so update it to normal
		    sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "',intPaxNo=0 where strTableNo='" + objFrmBillSettlement.getTableNo() + "'";
		    clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
		}
		else
		{
		    status = "Occupied";
		    sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "' where strTableNo='" + objFrmBillSettlement.getTableNo() + "'";
		    clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
		}
	    }
	    else
	    {
		sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "',intPaxNo=0 where strTableNo='" + objFrmBillSettlement.getTableNo() + "'";
		clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
	    }

	    //Update Table Status to Inresto POS
	    if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
	    {
		objUtility.funUpdateTableStatusToInrestoApp(objFrmBillSettlement.getTableNo(), objFrmBillSettlement.getLblTableNo().getText().trim(), status);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-23", JOptionPane.ERROR_MESSAGE);
	}
	return 0;
    }

    public void funUpdateKOTTempTable()
    {
	try
	{
	    String tableNo = objFrmBillSettlement.getTableNo();

	    for (clsMakeKotItemDtl itemDtl : objFrmBillSettlement.getListOfKOTWiseItemDtl())
	    {

		String itemCode = itemDtl.getItemCode();
		double itemQty = itemDtl.getQty();

		if (itemQty <= 0)
		{
		    String deleteQuery = "delete from tblitemrtemp  "
			    + "where strItemCode='" + itemCode + "' "
			    + "and strTableNo='" + tableNo + "' "
			    + "and strNCKotYN='N' "
			    + "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		}
		else
		{
		    String deleteQuery = "delete from tblitemrtemp  "
			    + "where strItemCode='" + itemCode + "' "
			    + "and strTableNo='" + tableNo + "' "
			    + "and strNCKotYN='N' "
			    + "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		    String insertQuery = "insert into tblitemrtemp (strSerialNo,strTableNo,strCardNo,dblRedeemAmt,strPosCode,strItemCode"
			    + ",strHomeDelivery,strCustomerCode,strItemName,dblItemQuantity,dblAmount,strWaiterNo"
			    + ",strKOTNo,intPaxNo,strPrintYN,strUserCreated,strUserEdited,dteDateCreated"
			    + ",dteDateEdited,strTakeAwayYesNo,strNCKotYN,strCustomerName,strCounterCode"
			    + ",dblRate,dblTaxAmt,strDelBoyCode) values ";

		    insertQuery += "('" + itemDtl.getSequenceNo() + "','" + tableNo + "'"
			    + ",'','0.00','" + clsGlobalVarClass.gPOSCode + "'"
			    + ",'" + itemDtl.getItemCode() + "','NO','' "
			    + ",'" + itemDtl.getItemName() + "','" + itemQty + "','" + itemDtl.getAmt() + "'"
			    + ",'" + itemDtl.getWaiterNo() + "','" + itemDtl.getKOTNo() + "'"
			    + ",'" + itemDtl.getPaxNo() + "','" + itemDtl.getPrintYN() + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
			    + ",'','N','','' "
			    + ",'" + itemDtl.getItemRate() + "','0.00','')";

		    clsGlobalVarClass.dbMysql.execute(insertQuery);
		}
	    }

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(tableNo);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-30", JOptionPane.ERROR_MESSAGE);
	}
    }

    //Delete the all records from tblItemRTemp and debitcardtemptables
    public void funTruncateKOTTempTable()
    {
	try
	{
	    String sql1 = "delete from tblitemrtemp where strTableNo='" + objFrmBillSettlement.getTableNo() + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	    clsGlobalVarClass.dbMysql.execute(sql1);

	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(objFrmBillSettlement.getTableNo());

	    sql1 = "delete from tblkottaxdtl where strTableNo='" + objFrmBillSettlement.getTableNo() + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql1);

	    clsGlobalVarClass.dbMysql.execute("truncate table tbltemphomedelv");
	    clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-30", JOptionPane.ERROR_MESSAGE);
	}
    }

    public String funGetBillSeriesDtlBillNos(List<clsBillSeriesBillDtl> listBillSeriesBillDtl, String hdBillNo)
    {
	StringBuilder sbDtllBillNos = new StringBuilder("");
	try
	{
	    for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
	    {
		if (listBillSeriesBillDtl.get(i).getStrHdBillNo().equals(hdBillNo))
		{
		    continue;
		}
		else
		{
		    if (sbDtllBillNos.length() == 0)
		    {
			sbDtllBillNos.append(listBillSeriesBillDtl.get(i).getStrHdBillNo());
		    }
		    else
		    {
			sbDtllBillNos.append(",");
			sbDtllBillNos.append(listBillSeriesBillDtl.get(i).getStrHdBillNo());
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	return sbDtllBillNos.toString();
    }

    public void funSendSMS(String billno, String smsData, String transType)
    {
	try
	{
	    //String smsData=clsGlobalVarClass.gBillSettlementSMS;
	    String result = "", result1 = "", result2 = "", result3 = "", result4 = "", result5 = "", result6 = "", result7 = "";
	    String sql = "";

	    if (transType.equalsIgnoreCase("Home Delivery"))
	    {
		sql = "select c.strCustomerName,c.longMobileNo,a.dblGrandTotal "
			+ " ,DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),time(a.dteBillDate) "
			+ " ,a.strUserCreated,ifnull(d.strDPName,'') "
			+ " from tblbillhd a,tblcustomermaster c ,tblhomedelivery b "
			+ " left outer join tbldeliverypersonmaster d on b.strDPCode=d.strDPCode "
			+ " where a.strBillNo='" + billno + "' and a.strBillNo=b.strBillNo "
			+ " and a.strCustomerCode=c.strCustomerCode ";
	    }
	    else
	    {
		sql = "select ifnull(c.strCustomerName,''),ifnull(c.longMobileNo,'NA')"
			+ " ,a.dblGrandTotal ,DATE_FORMAT(a.dteBillDate,'%d-%m-%Y')"
			+ " ,time(a.dteBillDate),a.strUserCreated,ifnull(d.strDPName,'') "
			+ " from tblbillhd a left outer join tblhomedelivery b on a.strBillNo=b.strBillNo "
			+ " left outer join tbldeliverypersonmaster d on b.strDPCode=d.strDPCode "
			+ " left outer join tblcustomermaster c on a.strCustomerCode=c.strCustomerCode "
			+ " where a.strBillNo='" + billno + "'";
	    }
	    //System.out.println(sql);
	    ResultSet rs_SqlGetSMSData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs_SqlGetSMSData.next())
	    {
		int intIndex = smsData.indexOf("%%BILL NO");
		if (intIndex != - 1)
		{
		    result = smsData.replaceAll("%%BILL NO", billno);
		    smsData = result;
		}
		int intIndex1 = smsData.indexOf("%%CUSTOMER NAME");

		if (intIndex1 != - 1)
		{
		    result1 = smsData.replaceAll("%%CUSTOMER NAME", rs_SqlGetSMSData.getString(1));
		    smsData = result1;
		}
		int intIndex2 = smsData.indexOf("%%BILL AMT");

		if (intIndex2 != - 1)
		{
		    result2 = smsData.replaceAll("%%BILL AMT", rs_SqlGetSMSData.getString(3));
		    smsData = result2;
		}
		int intIndex3 = smsData.indexOf("%%DATE");

		if (intIndex3 != - 1)
		{
		    result3 = smsData.replaceAll("%%DATE", rs_SqlGetSMSData.getString(4));
		    smsData = result3;
		}
		int intIndex4 = smsData.indexOf("%%DELIVERY BOY");

		if (intIndex4 != - 1)
		{
		    result4 = smsData.replaceAll("%%DELIVERY BOY", rs_SqlGetSMSData.getString(7));
		    smsData = result4;
		}
		int intIndex5 = smsData.indexOf("%%ITEMS");

		if (intIndex5 != - 1)
		{
		    StringBuilder sbItems = new StringBuilder();
		    sbItems.append("");
		    if (clsGlobalVarClass.gClientCode.equals("117.001"))//prems
		    {
			sql = "select a.strItemName from tblbilldtl a where a.strBillNo='" + billno + "' ";
			ResultSet rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rsItems.next())
			{
			    sbItems.append(rsItems.getString(1));
			    sbItems.append(",");
			}
			rsItems.close();
			sbItems.deleteCharAt(sbItems.lastIndexOf(","));
		    }

		    result5 = smsData.replaceAll("%%ITEMS", sbItems.toString());
		    smsData = result5;
		}
		int intIndex6 = smsData.indexOf("%%USER");

		if (intIndex6 != - 1)
		{
		    result6 = smsData.replaceAll("%%USER", rs_SqlGetSMSData.getString(6));
		    smsData = result6;
		}
		int intIndex7 = smsData.indexOf("%%TIME");

		if (intIndex7 != - 1)
		{
		    result7 = smsData.replaceAll("%%TIME", rs_SqlGetSMSData.getString(5));
		    smsData = result7;
		}

		ArrayList<String> mobileNoList = new ArrayList<>();
		mobileNoList.add(rs_SqlGetSMSData.getString(2));
		clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsData);
		objSMSSender.start();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funSendBillToPrint(String billNo, String POSDate)
    {
	if (clsGlobalVarClass.gPrintBillYN)
	{
	    frmOkCancelPopUp okOb2 = new frmOkCancelPopUp(null, "Do you want to Print Bill");
	    okOb2.setVisible(true);
	    int res2 = okOb2.getResult();
	    if (res2 == 1)
	    {
		//funPrintBill(billNo, POSDate);
		objUtility.funPrintBill(billNo, POSDate, false, clsGlobalVarClass.gPOSCode, "print");
		if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))//XO		
		{
		    if (clsGlobalVarClass.gEnableBillSeries && !clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5"))
		    {
			String reprintBillNo = objUtility2.funGetBillNoOnModifyBill(objFrmBillSettlement.getVoucherNo());
			objUtility.funPrintBill(reprintBillNo, POSDate, false, clsGlobalVarClass.gPOSCode, "print");
		    }
		}
	    }
	}
	else
	{
	    //funPrintBill(billNo, POSDate);
	    objUtility.funPrintBill(billNo, POSDate, false, clsGlobalVarClass.gPOSCode, "print");
	    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		if (clsGlobalVarClass.gEnableBillSeries && !clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5"))
		{
		    String reprintBillNo = objUtility2.funGetBillNoOnModifyBill(objFrmBillSettlement.getVoucherNo());
		    objUtility.funPrintBill(reprintBillNo, POSDate, false, clsGlobalVarClass.gPOSCode, "print");
		}
	    }
	}

    }

    //generate bill no.
    public void funGenerateBillNo()
    {
	try
	{
	    long code = 0;
	    String sqlGetLastBillNo = "select strBillNo from tblstorelastbill where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";
	    ResultSet rsLastBillNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlGetLastBillNo);

	    if (rsLastBillNo.next())
	    {
		code = rsLastBillNo.getLong(1);
		code = code + 1;
		rsLastBillNo.close();

		String voucherNo = clsGlobalVarClass.gPOSCode + String.format("%05d", code);
		objFrmBillSettlement.setVoucherNo(voucherNo);

		String sqlUpdateBillNo = "update tblstorelastbill set strBillNo='" + code + "' where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";
		clsGlobalVarClass.dbMysql.execute(sqlUpdateBillNo);
	    }
	    else
	    {
		rsLastBillNo.close();
		String voucherNo = clsGlobalVarClass.gPOSCode + "00001";
		objFrmBillSettlement.setVoucherNo(voucherNo);

		String sql_insert = "insert into tblstorelastbill values('" + clsGlobalVarClass.gPOSCode + "','1')";
		clsGlobalVarClass.dbMysql.execute(sql_insert);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-19", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    public List<clsPlayZoneItems> funApplyPlayZonePrice() throws Exception
    {
	String pricingDay = funGetPricingDay("Member");
	List<clsPlayZoneItems> listPlayZoneItems = new ArrayList<clsPlayZoneItems>();
	String sql = "select a.strItemCode,a.dblRate,b.intTimeStamp,MINUTE(time(SYSDATE()-time(a.dteDateCreated))) "
		+ " ,ifnull(minute(timediff(SYSDATE(),a.dteDateCreated))/b.intTimeStamp,0) "
		+ " ,ifnull(minute(timediff(SYSDATE(),a.dteDateCreated))%b.intTimeStamp,0),b.intGracePeriod,d.strPlayZoneCustType "
		+ " from tblitemrtemp a,tblplayzonepricinghd b,tblcustomermaster c,tblcustomertypemaster d "
		+ " where a.strItemCode=b.strItemCode and a.strPOSCode=b.strPosCode "
		+ " and a.strCustomerCode=c.strCustomerCode and c.strCustomerType=d.strCustTypeCode "
		+ " and a.strTableNo='" + objFrmBillSettlement.getTableNo() + "' ";
	ResultSet rsPlayZone = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsPlayZone.next())
	{
	    int limit = 1;
	    if (rsPlayZone.getInt(5) > 0)
	    {
		limit = rsPlayZone.getInt(5);
	    }
	    int remenderTime = rsPlayZone.getInt(6);
	    int gracePeriod = rsPlayZone.getInt(7);
	    if (gracePeriod < remenderTime && remenderTime > 0)
	    {
		limit = limit + 1;
	    }
	    clsPlayZoneItems objPlayZoneItems = new clsPlayZoneItems();
	    objPlayZoneItems.setStrItemCode(rsPlayZone.getString(1));
	    objPlayZoneItems.setIntTimeSlice(rsPlayZone.getInt(5));
	    if (rsPlayZone.getInt(5) == 0)
	    {
		objPlayZoneItems.setIntTimeSlice(1);
	    }
	    objPlayZoneItems.setIntRemender(rsPlayZone.getInt(6));

	    double rate = 0;
	    if (rsPlayZone.getString(8).equals("Guest"))
	    {
		pricingDay = funGetPricingDay("Guest");
	    }

	    sql = "select b.strPlayZonePricingCode," + pricingDay + " "
		    + " from tblplayzonepricinghd a,tblplayzonepricingdtl b "
		    + " where a.strPlayZonePricingCode=b.strPlayZonePricingCode and a.strItemCode='" + rsPlayZone.getString(1) + "' "
		    + " and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
		    + " limit " + limit + "";
	    ResultSet rsPlayZonePrice = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsPlayZonePrice.next())
	    {
		rate = rate + rsPlayZonePrice.getDouble(2);
	    }
	    rsPlayZonePrice.close();
	    objPlayZoneItems.setDblRate(rate);

	    listPlayZoneItems.add(objPlayZoneItems);
	}
	rsPlayZone.close();

	return listPlayZoneItems;
    }

    private String funGetPricingDay(String playZoneCustType)
    {
	String pricingDay = "b.dblMemberPriceSunday";
	if (playZoneCustType.equals("Guest"))
	{
	    pricingDay = "b.dblGuestPriceSunday";
	}

	switch (objUtility.funGetDayForPricing())
	{
	    case "strPriceMonday":
		pricingDay = "b.dblMemberPriceMonday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceMonday";
		}
		break;

	    case "strPriceTuesday":
		pricingDay = "b.dblMemberPriceTuesday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceTuesday";
		}
		break;

	    case "strPriceWednesday":
		pricingDay = "b.dblMemberPriceWednesday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceWednesday";
		}
		break;

	    case "strPriceThursday":
		pricingDay = "b.dblMemberPriceThursday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceThursday";
		}
		break;

	    case "strPriceFriday":
		pricingDay = "b.dblMemberPriceFriday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceFriday";
		}
		break;

	    case "strPriceSaturday":
		pricingDay = "b.dblMemberPriceSaturday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceSaturday";
		}
		break;

	    case "strPriceSunday":
		pricingDay = "b.dblMemberPriceSunday";
		if (playZoneCustType.equals("Guest"))
		{
		    pricingDay = "b.dblGuestPriceSunday";
		}
		break;
	}

	return pricingDay;
    }

    public String funGenerateBillNoForBillSeries(String billSeriesPrefix)
    {
	String billSeriesBillNo = "";

	try
	{
	    int billSeriesLastNo = 0;
	    String sqlBillSeriesLastNo = "select a.intLastNo "
		    + "from tblbillseries a "
		    + "where a.strBillSeries='" + billSeriesPrefix + "' "
		    + "and (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR a.strPOSCode='All'); ";
	    ResultSet rsBillSeriesLastNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillSeriesLastNo);
	    if (rsBillSeriesLastNo.next())
	    {
		billSeriesLastNo = rsBillSeriesLastNo.getInt("intLastNo");
	    }

	    billSeriesBillNo = billSeriesPrefix + "" + clsGlobalVarClass.gPOSCode + "" + String.format("%05d", (billSeriesLastNo + 1));

	    //update last bill series last no
	    int a = clsGlobalVarClass.dbMysql.execute("update tblbillseries "
		    + "set intLastNo='" + (billSeriesLastNo + 1) + "' "
		    + "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR strPOSCode='All') "
		    + "and strBillSeries='" + billSeriesPrefix + "' ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return billSeriesBillNo;
    }

    private void funSaveBillForBillForItems(String billSeriesPrefix, List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{

	    String billSeriesBillNo = funGenerateBillNoForBillSeries(billSeriesPrefix);

	    //last order no
	    int intLastOrderNo = objUtility2.funGetLastOrderNo();

	    objFrmBillSettlement.getHmBillItemDtl().clear();
	    double subTotal = 0.00;
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    for (clsBillItemDtl obj : listOfItemDtl)
	    {
		if (obj.getItemCode().contains("M"))
		{
		    objFrmBillSettlement.getHmBillItemDtl().put(obj.getItemCode() + "!" + obj.getItemName(), obj);
		}
		else
		{
		    objFrmBillSettlement.getHmBillItemDtl().put(obj.getItemCode(), obj);
		}
		subTotal = subTotal + obj.getAmount();

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(obj.getItemCode());
		objItemDtlForTax.setItemName(obj.getItemName());
		objItemDtlForTax.setAmount(obj.getAmount());
		objItemDtlForTax.setDiscAmt(obj.getDiscountAmount() * obj.getQuantity());
		arrListItemDtls.add(objItemDtlForTax);
	    }

	    String operationType = "DineIn";
	    boolean flgHomeDelPrint = false;

	    objFrmBillSettlement.setVoucherNo(billSeriesBillNo);
	    objFrmBillSettlement.revalidate();
	    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
	    {
		operationType = "TakeAway";
	    }
	    if (null != clsGlobalVarClass.hmTakeAway.get(objFrmBillSettlement.getTableNo()))
	    {
		operationType = "TakeAway";
		clsGlobalVarClass.hmTakeAway.remove(objFrmBillSettlement.getTableNo());
	    }
	    //funSaveBillDiscountDetail(voucherNo);

	    StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
	    int seq1 = sb.lastIndexOf(" ");
	    String split = sb.substring(0, seq1);
	    String kotDateTime = split;

	    String counterCode = "NA";
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		counterCode = clsGlobalVarClass.gCounterCode;
	    }

	    String sqlCheckHomeDelivery = "select strHomeDelivery,strCustomerCode "
		    + "from tblitemrtemp where strTableNo='" + objFrmBillSettlement.getTableNo() + "' "
		    + "group by strTableNo ;";
	    ResultSet rsHomeDeleveryCheck = null;
	    String customerCode = "";
	    rsHomeDeleveryCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckHomeDelivery);
	    if (rsHomeDeleveryCheck.next())
	    {
		String homeDeliveryYesNo = rsHomeDeleveryCheck.getString(1);
		customerCode = rsHomeDeleveryCheck.getString(2);
		rsHomeDeleveryCheck.close();
		if ("Yes".equalsIgnoreCase(homeDeliveryYesNo))
		{
		    operationType = "HomeDelivery";
		    Calendar c = Calendar.getInstance();
		    int hh = c.get(Calendar.HOUR);
		    int mm = c.get(Calendar.MINUTE);
		    int ss = c.get(Calendar.SECOND);
		    int ap = c.get(Calendar.AM_PM);

		    String ampm = "AM";
		    if (ap == 1)
		    {
			ampm = "PM";
		    }
		    String currentTime = hh + ":" + mm + ":" + ss + ":" + ampm;
		    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode"
			    + ",strDPCode,dteDate,tmeTime,strPOSCode,strCustAddressLine1,strCustAddressLine2"
			    + ",strCustAddressLine3,strCustAddressLine4,strCustCity,strClientCode,dblHomeDeliCharge)"
			    + " values('" + objFrmBillSettlement.getVoucherNo() + "','" + customerCode + "','" + objFrmBillSettlement.getDelPersonCode() + "','"
			    + objUtility.funGetPOSDateForTransaction() + "','" + currentTime + "','"
			    + clsGlobalVarClass.gPOSCode + "','" + objFrmBillSettlement.getCustAddType() + "','','','','','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + objFrmBillSettlement.getDeliveryCharge() + "')";
		    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
		    clsGlobalVarClass.gCustomerCode = null;
		    clsGlobalVarClass.gDeliveryCharges = 0.00;
		    flgHomeDelPrint = true;
		}
	    }
	    if (null != objFrmBillSettlement.getCustCode())
	    {
		customerCode = objFrmBillSettlement.getCustCode();
	    }

	    double advanceAmount = 0.00;
	    double _deliveryCharge = 0.00;

	    objFrmBillSettlement.setDblTotalTaxAmt(0.00);
	    objFrmBillSettlement.setNetAmount(0.00);
	    objFrmBillSettlement.setSubTotal(0.00);
	    objFrmBillSettlement.setDblDiscountAmt(0.00);
	    objFrmBillSettlement.setDblDiscountPer(0.00);
	    objFrmBillSettlement.setGrandTotal(0.00);

	    double tempDiscAmt = 0, tempDiscPer = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : objFrmBillSettlement.getHmBillItemDtl().entrySet())
	    {
		clsBillItemDtl objBillItemDtl = entry.getValue();
		tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
		tempDiscPer = objBillItemDtl.getDiscountPercentage();
	    }
	    if (subTotal > 0)
	    {
		tempDiscPer = (tempDiscAmt * 100) / subTotal;
	    }

	    objFrmBillSettlement.setSubTotal(subTotal);

	    List<clsTaxCalculationDtls> arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, objFrmBillSettlement.getDtPOSDate(), objFrmBillSettlement.getAreaCode(), operationType, subTotal, tempDiscAmt, "", objFrmBillSettlement.getSettlementCode(), "Sales");
	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
		{
		    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();

		    objFrmBillSettlement.setDblTotalTaxAmt(objFrmBillSettlement.getDblTotalTaxAmt() + dblTaxAmt);
		}
	    }
	    objFrmBillSettlement.setArrListTaxCal(arrListTaxCal);

	    //save bill disc dtl
	    objFrmBillSettlement.funSaveBillDiscDtlForBillSeries(listOfItemDtl);

	    objFrmBillSettlement.setNetAmount(objFrmBillSettlement.getSubTotal() - objFrmBillSettlement.getDblDiscountAmt());
	    objFrmBillSettlement.setGrandTotal(objFrmBillSettlement.getNetAmount() + objFrmBillSettlement.getDblTotalTaxAmt() + objFrmBillSettlement.getDeliveryCharge());
	    objFrmBillSettlement.setGrandTotal(objFrmBillSettlement.getGrandTotal() - advanceAmount);

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(objFrmBillSettlement.getGrandTotal());

	    objFrmBillSettlement.setGrandTotalRoundOffBy(mapRoundOff.get("roundOffByAmt"));

	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		objFrmBillSettlement.setGrandTotal(mapRoundOff.get("roundOffAmt"));
	    }
	    //end code to calculate roundoff amount and round off by amt

	    Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
	    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().size() > 0)
	    {
		hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
		for (Map.Entry<String, clsBillDtl> entry : objFrmBillSettlement.getHmComplimentaryBillItemDtl().entrySet())
		{
		    hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
		}
	    }

	    List<String> listBillItemDtl = new ArrayList<String>();
	    String custName = "";
	    String cardNo = "";
	    String sqlItemDtl = "select strItemCode,upper(strItemName),dblItemQuantity "
		    + " ,dblAmount,strKOTNo,strManualKOTNo,Time(dteDateCreated),strCustomerCode "
		    + " ,strCustomerName,strCounterCode,strWaiterNo,strPromoCode"
		    + " ,dblRate,strCardNo,tmeOrderProcessing,tmeOrderPickup "
		    + " from tblitemrtemp "
		    + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and strTableNo='" + objFrmBillSettlement.getTableNo() + "' and strNCKotYN='N' "
		    + " and strItemCode in " + funGetItemCodeList(listOfItemDtl) + " "
		    + " order by strTableNo ASC";
	    ResultSet rsItemKOTDTL = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);
	    String kot = "";

	    while (rsItemKOTDTL.next())
	    {
		String iCode = rsItemKOTDTL.getString(1);
		String iName = rsItemKOTDTL.getString(2);
		double iQty = rsItemKOTDTL.getDouble(3);
		String iAmt = rsItemKOTDTL.getString(4);
		String orderProcessingTime = rsItemKOTDTL.getString(15);
		String orderPickupTime = rsItemKOTDTL.getString(16);
		if (null != hmComplimentaryBillItemDtlTemp && hmComplimentaryBillItemDtlTemp.containsKey(iCode))
		{
		    double complQty = hmComplimentaryBillItemDtlTemp.get(iCode).getDblComplQty();
		    if (complQty == iQty || complQty < iQty)
		    {
			double amtToSave = rsItemKOTDTL.getDouble(4) - (hmComplimentaryBillItemDtlTemp.get(iCode).getDblComplQty() * hmComplimentaryBillItemDtlTemp.get(iCode).getDblRate());
			iAmt = String.valueOf(amtToSave);
			hmComplimentaryBillItemDtlTemp.remove(iCode);
		    }
		    else if (iQty < complQty)
		    {
			double amtToSave = rsItemKOTDTL.getDouble(4) - (iQty * hmComplimentaryBillItemDtlTemp.get(iCode).getDblRate());
			iAmt = String.valueOf(amtToSave);
			double newComplQty = complQty - iQty;
			hmComplimentaryBillItemDtlTemp.get(iCode).setDblComplQty(newComplQty);
		    }
		}

		double rate = rsItemKOTDTL.getDouble(13);
		kot = rsItemKOTDTL.getString(5);
		String manualKOTNo = rsItemKOTDTL.getString(6);
		kotDateTime = split + " " + rsItemKOTDTL.getString(7);

		objFrmBillSettlement.setCustCode(rsItemKOTDTL.getString(8));

		custName = rsItemKOTDTL.getString(9);
		String promoCode = rsItemKOTDTL.getString(12);
		cardNo = rsItemKOTDTL.getString(14);
		String sqlInsertBillDtl = "";

		if (!iCode.contains("M"))
		{
		    if (objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			if (null != objFrmBillSettlement.getHmPromoItem().get(iCode))
			{
			    clsPromotionItems objPromoItemDtl = objFrmBillSettlement.getHmPromoItem().get(iCode);
			    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
			    {
				double freeQty = objPromoItemDtl.getFreeItemQty();
				double freeAmt = freeQty * rate;

				promoCode = objPromoItemDtl.getPromoCode();
				String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					+ ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					+ "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "'"
					+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "','" + kotDateTime + "')";
				clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				objFrmBillSettlement.getHmPromoItem().remove(iCode);
			    }
			    else if (objPromoItemDtl.getPromoType().equals("Discount"))
			    {
				if (objPromoItemDtl.getDiscType().equals("Value"))
				{
				    double discAmt = objPromoItemDtl.getDiscAmt();

				    promoCode = objPromoItemDtl.getPromoCode();
				    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					    + "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    objFrmBillSettlement.getHmPromoItem().remove(iCode);
				}
				else
				{
				    iAmt = String.valueOf(iQty * rate);
				    double amount = iQty * rate;
				    double discAmt = amount - (amount * (objPromoItemDtl.getDiscPer() / 100));

				    promoCode = objPromoItemDtl.getPromoCode();
				    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					    + "('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    objFrmBillSettlement.getHmPromoItem().remove(iCode);
				}
			    }
			}
		    }

		    String amt = "0.00";
		    boolean flgComplimentaryBill = false;
		    if (objFrmBillSettlement.getHmSettlemetnOptions().size() == 1)
		    {
			for (clsSettelementOptions obj : objFrmBillSettlement.getHmSettlemetnOptions().values())
			{
			    if (obj.getStrSettelmentType().equals("Complementary"))
			    {
				flgComplimentaryBill = true;
				break;
			    }
			}
		    }
		    if (!flgComplimentaryBill)
		    {
			amt = iAmt;
		    }

		    double discAmt = 0.00;
		    double discPer = 0.00;
		    if (!iCode.contains("M"))
		    {
			discAmt = objFrmBillSettlement.getHmBillItemDtl().get(iCode).getDiscountAmount() * iQty;
			discPer = objFrmBillSettlement.getHmBillItemDtl().get(iCode).getDiscountPercentage();
		    }

		    if (iQty > 0)
		    {
			if (iName.startsWith("=>"))
			{
			    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
				    + ",strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo"
				    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
				    + "values('" + iCode + "','" + iName + "','" + objFrmBillSettlement.getVoucherNo() + "'," + rate + ",'" + iQty
				    + "','" + amt + "','0.00','" + kotDateTime + "','" + kot + "','"
				    + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','Y','" + promoCode + "'"
				    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
				    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "','" + orderPickupTime + "')";
			}
			else
			{
			    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
				    + ",strClientCode,strManualKOTNo,strPromoCode,strCounterCode,strWaiterNo"
				    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
				    + "values('" + iCode + "','" + iName + "','" + objFrmBillSettlement.getVoucherNo() + "'," + rate + ""
				    + ",'" + iQty + "','" + amt + "','0.00','" + kotDateTime + "','" + kot + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','" + promoCode + "'"
				    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
				    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "','" + orderPickupTime + "')";
			}
			clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
			if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().containsKey(iCode))
			{
			    clsBillDtl objBillDtl = objFrmBillSettlement.getHmComplimentaryBillItemDtl().get(iCode);
			    objBillDtl.setDblRate(rate);
			    objBillDtl.setStrBillNo(objFrmBillSettlement.getVoucherNo());
			    objBillDtl.setDteBillDate(kotDateTime);
			    objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			    objBillDtl.setStrKOTNo(kot);
			    objBillDtl.setStrManualKOTNo(manualKOTNo);
			    objBillDtl.setStrPromoCode(promoCode);
			    objBillDtl.setStrCounterCode(rsItemKOTDTL.getString(10));
			    objBillDtl.setStrWaiterNo(rsItemKOTDTL.getString(11));
			    objBillDtl.setDblDiscountAmt(discAmt);
			    objBillDtl.setDblDiscountPer(discPer);
			    objBillDtl.setDblTaxAmount(0.00);
			    objBillDtl.setDteBillSettleDate(kotDateTime);
			    objFrmBillSettlement.getHmComplimentaryBillItemDtl().put(iCode, objBillDtl);

			    listBillItemDtl.add(iCode);
			}
		    }
		}
		if (iCode.contains("M"))
		{
		    StringBuilder sb1 = new StringBuilder(iCode);
		    int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
		    String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code
		    double amt = Double.parseDouble(iAmt);
		    double modDiscAmt = 0, modDiscPer = 0;
		    if (objFrmBillSettlement.getHmBillItemDtl().containsKey(iCode + "!" + iName))
		    {
			modDiscAmt = objFrmBillSettlement.getHmBillItemDtl().get(iCode + "!" + iName).getDiscountAmount() * iQty;
			modDiscPer = objFrmBillSettlement.getHmBillItemDtl().get(iCode + "!" + iName).getDiscountPercentage();
		    }

		    StringBuilder sbTemp = new StringBuilder(iCode);
		    if (hmComplimentaryBillItemDtlTemp != null && hmComplimentaryBillItemDtlTemp.containsKey(sbTemp.substring(0, 7).toString()))
		    {
			amt = 0;
		    }
		    String sqlBillModifierDtl = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
			    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
			    + "values('" + objFrmBillSettlement.getVoucherNo() + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
			    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + modDiscPer + "','" + modDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
		    //System.out.println(sqlBillModifierDtl);
		    clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
		}
	    }
	    rsItemKOTDTL.close();

	    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
		    + "from tblbillpromotiondtl "
		    + " where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + " and strPromoType='ItemWise' ";
	    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
	    while (rsBillPromo.next())
	    {
		double freeQty = rsBillPromo.getDouble(1);
		String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
			+ " from tblbilldtl "
			+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
			+ " and strBillNo='" + objFrmBillSettlement.getVoucherNo() + "'";
		ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		while (rsBillDtl.next())
		{
		    if (freeQty > 0)
		    {
			double saleQty = rsBillDtl.getDouble(2);
			double saleAmt = rsBillDtl.getDouble(4);
			if (saleQty <= freeQty)
			{
			    freeQty = freeQty - saleQty;
			    double amtToUpdate = saleAmt - (saleQty * rsBillPromo.getDouble(2));
			    String sqlUpdate = "update tblbilldtl set dblAmount= " + amtToUpdate + " "
				    + " where strItemCode='" + rsBillDtl.getString(1) + "' "
				    + "and strKOTNo='" + rsBillDtl.getString(3) + "'";
			    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			}
			else
			{
			    double amtToUpdate = saleAmt - (freeQty * rsBillPromo.getDouble(2));
			    String sqlUpdate = "update tblbilldtl set dblAmount= " + amtToUpdate + " "
				    + " where strItemCode='" + rsBillDtl.getString(1) + "' "
				    + "and strKOTNo='" + rsBillDtl.getString(3) + "'";
			    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
			    freeQty = 0;
			}
		    }
		}
		rsBillDtl.close();
	    }
	    rsBillPromo.close();

	    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().size() > 0)
	    {
		objFrmBillSettlement.funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
	    }

	    if (clsGlobalVarClass.gClientCode.equals("190.001") && billSeriesPrefix.equalsIgnoreCase("L") && customerCode.trim().isEmpty())
	    {
		customerCode = objUtility2.funAutoCustomerSelectionForLiquorBill();
	    }

	    String sqlInsertBillHd = "insert into tblbillhd"
		    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
		    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType,"
		    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strTableNo"
		    + ",strWaiterNo,strCustomerCode,intShiftCode,intPaxNo,strReasonCode,strRemarks"
		    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strAreaCode"
		    + ",strDiscountRemark,strTakeAwayRemarks,strDiscountOn,strCardNo,strTransactionType,dblRoundOff"
		    + ",intBillSeriesPaxNo,dtBillDate,intOrderNo,strCRMRewardId,dblUSDConverionRate ) "
		    + "values('" + objFrmBillSettlement.getVoucherNo() + "','" + objFrmBillSettlement.getAdvOrderBookingNo() + "','" + objUtility.funGetPOSDateForTransaction() + "','"
		    + clsGlobalVarClass.gPOSCode + "','','" + objFrmBillSettlement.getDblDiscountAmt() + "','"
		    + objFrmBillSettlement.getDblDiscountPer() + "','" + objFrmBillSettlement.getDblTotalTaxAmt() + "','" + objFrmBillSettlement.getSubTotal() + "','"
		    + objFrmBillSettlement.getGrandTotal() + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
		    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.gClientCode + "','" + objFrmBillSettlement.getTableNo() + "','" + objFrmBillSettlement.getWaiterNo() + "'"
		    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
		    + "," + objFrmBillSettlement.getPaxNo() + ",'" + objFrmBillSettlement.getSelectedReasonCode() + "','" + objUtility.funCheckSpecialCharacters(objFrmBillSettlement.getTxtAreaRemark().getText().trim()) + "'"
		    + "," + objFrmBillSettlement.getTxtTip().getText() + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
		    + ",'" + counterCode + "'," + objFrmBillSettlement.getDeliveryCharge() + ",'" + objFrmBillSettlement.getAreaCode() + "'"
		    + ",'" + objFrmBillSettlement.getDiscountRemarks() + "','','','" + cardNo + "','" + clsGlobalVarClass.gTransactionType + "'"
		    + ",'" + objFrmBillSettlement.getGrandTotalRoundOffBy() + "','" + objFrmBillSettlement.getIntBillSeriesPaxNo() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",'" + intLastOrderNo + "','" + objFrmBillSettlement.getRewardId() + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlInsertBillHd);

	    clsBillSeriesBillDtl objBillSeriesBillDtl = new clsBillSeriesBillDtl();
	    objBillSeriesBillDtl.setStrHdBillNo(objFrmBillSettlement.getVoucherNo());
	    objBillSeriesBillDtl.setStrBillSeries(billSeriesPrefix);
	    objBillSeriesBillDtl.setDblGrandTotal(objFrmBillSettlement.getGrandTotal());
	    objBillSeriesBillDtl.setFlgHomeDelPrint(flgHomeDelPrint);
	    objFrmBillSettlement.getListBillSeriesBillDtl().add(objBillSeriesBillDtl);

	    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + objFrmBillSettlement.getVoucherNo() + "'";
	    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

	    // insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(objFrmBillSettlement.getVoucherNo());
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    objFrmBillSettlement.funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(objFrmBillSettlement.getVoucherNo(), "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (objFrmBillSettlement.getCustCode().trim().length() > 0)
		{
		    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + objFrmBillSettlement.getCustCode() + "' "
			    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

		    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
			    + "values('" + objFrmBillSettlement.getCustCode() + "','" + custName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "')";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
		}
	    }

	    objFrmBillSettlement.dispose();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public String funGetItemCodeList(List<clsBillItemDtl> listOfItemDtl)
    {
	StringBuilder codeBuilder = new StringBuilder();
	try
	{
	    codeBuilder.append("(");
	    for (int i = 0; i < listOfItemDtl.size(); i++)
	    {
		if (i == 0)
		{
		    codeBuilder.append("'" + listOfItemDtl.get(i).getItemCode() + "' ");
		}
		else
		{
		    codeBuilder.append(",'" + listOfItemDtl.get(i).getItemCode() + "' ");
		}
	    }
	    codeBuilder.append(")");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return codeBuilder.toString();
	}
    }

    public String funGetItemCodeList()
    {
	StringBuilder codeBuilder = new StringBuilder();
	try
	{
	    Object[] arrItemCode = objFrmBillSettlement.getHmBillItemDtl().keySet().toArray();

	    codeBuilder.append("(");
	    for (int i = 0; i < arrItemCode.length; i++)
	    {
		if (i == 0)
		{
		    codeBuilder.append("'" + arrItemCode[i].toString().split("!")[0] + "' ");
		}
		else
		{
		    codeBuilder.append(",'" + arrItemCode[i].toString().split("!")[0] + "' ");
		}
	    }
	    codeBuilder.append(")");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return codeBuilder.toString();
	}
    }

    public void funRemoveServiceCharge()
    {
	List<clsTaxCalculationDtls> arrListTaxCal = objFrmBillSettlement.getArrListTaxCal();
	Iterator<clsTaxCalculationDtls> taxIterator = arrListTaxCal.iterator();
	while (taxIterator.hasNext())
	{
	    clsTaxCalculationDtls objTaxDtls = taxIterator.next();

	    if (clsGlobalVarClass.gRemoveSCTaxCode.equalsIgnoreCase(objTaxDtls.getTaxCode()))
	    {
		taxIterator.remove();
	    }
	    //need to discuss with sir remove NSC and tax on NSC
//	    else if (objTaxDtls.isIsTaxOnTax().equalsIgnoreCase("Yes") && clsGlobalVarClass.gRemoveSCTaxCode.equalsIgnoreCase(objTaxDtls.getStrTaxOnTaxCode()))
//	    {
//		taxIterator.remove();
//	    }
	}

	objFrmBillSettlement.setArrListTaxCal(arrListTaxCal);
    }

    public void funUpdateKOTToBillNote(String gPOSCode, String tableNo, String billNo)
    {
	try
	{
	    StringBuilder sqlBuilder = new StringBuilder();
	    StringBuilder billNoteBuilder = new StringBuilder();

	    sqlBuilder.setLength(0);
	    sqlBuilder.append("select a.strBillNote "
		    + "from tblitemrtemp a "
		    + "where a.strTableNo='" + tableNo + "' "
		    + "and a.strPOSCode='" + gPOSCode + "' "
		    + "and length(a.strBillNote)>0 "
		    + "group by a.strBillNote ");
	    billNoteBuilder.setLength(0);
	    ResultSet rsBillNoteBuilder = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    for (int i = 0; rsBillNoteBuilder.next(); i++)
	    {
		if (i == 0)
		{
		    billNoteBuilder.append(rsBillNoteBuilder.getString(1));
		}
		else
		{
		    billNoteBuilder.append("," + rsBillNoteBuilder.getString(1));
		}
	    }
	    rsBillNoteBuilder.close();

	    int a = clsGlobalVarClass.dbMysql.execute("update tblbillhd "
		    + "set strKOTToBillNote='" + billNoteBuilder.toString() + "' "
		    + "where strPOSCode='" + gPOSCode + "' "
		    + "and strBillNo='" + billNo + "' ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funCallIntegrationAPIsAfterBillPrint(clsBillHd objBillHd)
    {
	try
	{
	    if (clsGlobalVarClass.gBenowIntegrationYN && objBillHd.getStrCustomerCode().length() > 0)
	    {
		clsBenowIntegration objBenowIntegration = new clsBenowIntegration();
		//send payment SMS link
		objBenowIntegration.funSendPaymenetLinkSMS(objBillHd.getStrBillNo(), objBillHd.getDblGrandTotal(), objBillHd.getStrCustomerCode());
	    }

	    if (clsGlobalVarClass.gWERAOnlineOrderIntegration)
	    {
		clsWERAOnlineOrderIntegration objOnlineOrderIntegration = new clsWERAOnlineOrderIntegration();
		objOnlineOrderIntegration.funCallAcceptTheOrder(objBillHd.getStrOnlineOrderNo(), 20);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}
