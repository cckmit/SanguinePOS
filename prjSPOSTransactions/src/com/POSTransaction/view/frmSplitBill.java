package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsCalculateBillPromotions;
import com.POSTransaction.controller.clsPromotionItems;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


/**
 * This class will split bill in one of the type from 'Equal Split,Group Wise,Item Type Wise,Item Wise'
 * @author Admin
 * 
 * Equal Split, will split a single bill into  either 2 bills,3 bills or 4 bills with equal quantity of all items. 
 * Group Wise, will split a bill Item Group wise,no matter how many items group are there in bill.eg. groups may be FOOD,LIQOUR,BEVERAGE,etc.
 * Item Type Wise, will split a bill Item type wise which is define in item master(Item Type).item type eg. FOOD,LIQUOR.
 * Item Wise, This split type will allow user to select items and add it to selected grid. Then this slit type will split bill according to grid items.
 */
public class frmSplitBill extends javax.swing.JFrame
{

    private String sql, dtBillDate, voucherNo, advanceBookNo;
    private ArrayList<String> listTaxCode;
    private String selectedTable = "NA";
    private int selecedRow;
    private ArrayList<Double> listActualAmountOfItems;
    private ArrayList<String> listItemCode;
    private ArrayList<String> listItemName;
    private ArrayList<Double> listItemQTY;
    private String billNo = "";
    private int no_Of_Items_withModifier = 0, no_Of_tax_withModifier = 0;
    private String cls_ItemCode, cls_ItemName, dtPOSDate, settlementCode, billedAreaCode, dineInForTax, takeAwayForTax, homeDeliveryForTax;
    private double cls_qty, discountAmt, cls_Amt, cls_Rate;
    private List<frmSplitBill> list_Grid1_SplitedItems = new ArrayList<>();
    private List<frmSplitBill> list_Grid2_SplitedItems = new ArrayList<>();
    private List<frmSplitBill> list_Grid3_SplitedItems = new ArrayList<>();
    private List<frmSplitBill> list_Grid4_SplitedItems = new ArrayList<>();
    private ArrayList<Double> listSplittedAmountOfItems;
    private ArrayList<ArrayList<Object>> arrListTaxCal;
    private ArrayList<String> listItemTypeForItemType;
    private Map hmItemTypeWiseBill;
    private Map<String, List<Object>> hmGroupWiseBill;
    private Map<String, List<Object>> hmSubGroupWiseBill;
    private Map<String, ArrayList<ArrayList>> hmTaxOnBill;
    private HashMap<String, String> mapMainGridItemDtls;
    private HashMap<String, String> billsToBePrinted = new HashMap<String, String>();
    private int[] itemWiseSplitQty =
    {
	0, 1, 2, 3, 4
    };
    private clsUtility objUtility;
    private double selectedQty = 1;
    private Map<String, frmSplitBill> mapItemCodeOriginalQty;
    private HashMap<String, List<clsBillDtl>> mapKOTWiseItemList;
    private Map<String, clsPromotionItems> hmPromoItem;
    private clsUtility2 objUtility2 = new clsUtility2();
    private String billedOperationType = "DineIn";
    private int intLastOrderNo = 0;

    public frmSplitBill()
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
	    dineInForTax = "Y";
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    scrSettle.setVisible(false);
	    scrTax.setVisible(false);
	    cmbSplitQty.setVisible(false);
	    lblPersons.setVisible(false);
	    btnMoveNext.setEnabled(false);
	    btnMovePrev.setEnabled(false);
	    scrBillSplitDtl1.setVisible(false);
	    scrBillSplitDtl2.setVisible(false);
	    scrBillSplitDtl3.setVisible(false);
	    scrBillSplitDtl4.setVisible(false);
	    btnMoveNext.setVisible(false);
	    btnMovePrev.setVisible(false);
	    scrBillSplitDtl1.setVisible(false);
	    scrBillSplitDtl2.setVisible(false);
	    scrBillSplitDtl3.setVisible(false);
	    scrBillSplitDtl4.setVisible(false);
	    lblSubTotal1.setVisible(false);
	    lblDiscount1.setVisible(false);
	    lblNetAmt1.setVisible(false);
	    btnMovePrev.setVisible(false);
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
	    dtPOSDate = date1;
	    settlementCode = "S01";
	    dineInForTax = "Y";
	    takeAwayForTax = "N";
	    hmItemTypeWiseBill = new HashMap<String, ArrayList<Object>>();
	    hmGroupWiseBill = new HashMap<String, List<Object>>();
	    hmSubGroupWiseBill = new HashMap<String, List<Object>>();
	    hmTaxOnBill = new HashMap<String, ArrayList<ArrayList>>();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private frmSplitBill(String itemName, double qty, double Amt, String itemcode, double discount, double rate)
    {
	this.cls_ItemName = itemName;
	this.cls_ItemCode = itemcode;
	this.cls_qty = qty;
	this.cls_Amt = Amt;
	this.discountAmt = discount;
	this.cls_Rate = rate;
    }

    private List funCalculateTax(String operationType, List<String> arrListItemDtl)
    {
	List listTax = new ArrayList<ArrayList<Object>>();
	try
	{
	    listTax = funCheckDateRangeForTax(operationType, arrListItemDtl);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return listTax;
    }

    private ArrayList funCheckDateRangeForTax(String operationType, List<String> arrListItemDtl) throws Exception
    {
	String taxCode = "", taxName = "", taxOnGD = "", taxCal = "", taxIndicator = "";
	String opType = "", area = "", taxOnTax = "", taxOnTaxCode = "";
	double taxPercent = 0.00, taxableAmount = 0.00, taxCalAmt = 0.00;
	ArrayList<Object> listTax = new ArrayList<Object>();
	arrListTaxCal = new ArrayList<ArrayList<Object>>();
	clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp;");// Empty Tax Temp Table
	double subTotalForTax = 0;
	double discAmt = 0;
	double discPer = 0;

	String sql_ChkTaxDate = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent "
		+ " ,a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType "
		+ " ,a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
		+ " from tbltaxhd a,tbltaxposdtl b "
		+ " where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ " and date(a.dteValidFrom) <='" + dtPOSDate + "' "
		+ " and date(a.dteValidTo)>='" + dtPOSDate + "' and a.strTaxOnSP='Sales' "
		+ " order by a.strTaxOnTax,a.strTaxCode";
	ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_ChkTaxDate);
	while (rsTax.next())
	{
	    taxCode = rsTax.getString(1);
	    taxName = rsTax.getString(2);
	    taxPercent = Double.parseDouble(rsTax.getString(5));
	    taxOnGD = rsTax.getString(7);
	    taxCal = rsTax.getString(8);
	    taxIndicator = rsTax.getString(9);
	    taxOnTax = rsTax.getString(13);
	    taxOnTaxCode = rsTax.getString(14);
	    taxableAmount = 0.00;
	    taxCalAmt = 0.00;
	    String sql_TaxOn = "select strAreaCode,strOperationType,strItemType "
		    + "from tbltaxhd where strTaxCode='" + taxCode + "'";
	    ResultSet rsTaxOn = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxOn);
	    if (rsTaxOn.next())
	    {
		area = rsTaxOn.getString(1);
		opType = rsTaxOn.getString(2);
	    }
	    if (funCheckAreaCode(billedAreaCode, area))
	    {
		if (funCheckOperationType(opType, operationType))
		{
		    if (funFindSettlementForTax(taxCode, settlementCode))
		    {
			boolean flgTaxOnGrpApplicable = false;
			taxableAmount = 0;
			listTax = new ArrayList<Object>();

			if (taxOnGD.equals("Gross"))
			{
			    //to calculate tax on group of an item
			    for (int i = 0; i < arrListItemDtl.size(); i++)
			    {
				String[] spItemDtl = arrListItemDtl.get(i).toString().split("#");
				//0 code
				//1 name
				//2 qty
				//3 amt
				//4 discAmt                                

				boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, spItemDtl[0]);
				if (isApplicable)
				{
				    flgTaxOnGrpApplicable = true;
				    double itemAmt = Double.parseDouble(spItemDtl[3]);
				    double itemDisc = Double.parseDouble(spItemDtl[4]);
				    taxableAmount = taxableAmount + itemAmt;

				    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
				    {
					taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, itemAmt, itemDisc, billedAreaCode, operationType, settlementCode);
				    }
				}
			    }

//                            if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
//                            {
//                                taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, arrListTaxDtl);
//                            }
			}
			else
			{
			    subTotalForTax = 0;
			    discAmt = 0;
			    for (String objItemDtl : arrListItemDtl)
			    {
				String[] spItemDtl = objItemDtl.split("#");
				//0 code
				//1 name
				//2 qty
				//3 amt
				//4 discAmt     

				boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, spItemDtl[0]);
				if (isApplicable)
				{
				    flgTaxOnGrpApplicable = true;
				    double itemAmt = Double.parseDouble(spItemDtl[3]);
				    double itemDisc = Double.parseDouble(spItemDtl[4]);
				    if (itemDisc > 0)
				    {
					discAmt += itemDisc;
				    }
				    taxableAmount = taxableAmount + itemAmt;

				    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
				    {
					taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, itemAmt, itemDisc, billedAreaCode, operationType, settlementCode);
				    }
				}
			    }
			    if (taxableAmount > 0)
			    {
				taxableAmount = taxableAmount - discAmt;
			    }
//                            if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
//                            {
//                                taxableAmount += funGetTaxableAmountForTaxOnTax(taxOnTaxCode, arrListTaxDtl);
//                            }
			}
			if (flgTaxOnGrpApplicable)
			{
			    if (taxCal.equals("Forward")) // Forward Tax Calculation
			    {
				taxCalAmt = taxableAmount * (taxPercent / 100);
			    }
			    else // Backward Tax Calculation
			    {
				taxCalAmt = taxableAmount - (taxableAmount * 100 / (100 + taxPercent));
			    }

			    listTax.add(taxCode);
			    listTax.add(taxName);
			    listTax.add(taxableAmount);
			    listTax.add(taxCalAmt);
			    listTax.add(taxCal);
			    arrListTaxCal.add(listTax);
			}
		    }
		}
	    }
	}
	return arrListTaxCal;
    }

    private boolean funCheckAreaCode(String taxCode, String area)
    {
	boolean flgTaxOn = false;
	String[] spAreaCode = area.split(",");
	for (int cnt = 0; cnt < spAreaCode.length; cnt++)
	{
	    if (spAreaCode[cnt].equals(billedAreaCode))
	    {
		flgTaxOn = true;
		break;
	    }
	}
	return flgTaxOn;
    }

    private boolean funCheckOperationType(String taxOperationType, String operationTypeForTax)
    {
	boolean flgTaxOn = false;
	if (operationTypeForTax.equalsIgnoreCase("DirectBiller"))
	{
	    operationTypeForTax = "DineIn";
	}
	String[] spOpType = taxOperationType.split(",");
	for (int cnt = 0; cnt < spOpType.length; cnt++)
	{
	    if (spOpType[cnt].equals("HomeDelivery") && operationTypeForTax.equalsIgnoreCase("HomeDelivery"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("HomeDelivery") && operationTypeForTax.equalsIgnoreCase("Home Delivery"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("DineIn"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("Dine In"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("TakeAway") && operationTypeForTax.equalsIgnoreCase("TakeAway"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("TakeAway") && operationTypeForTax.equalsIgnoreCase("Take Away"))
	    {
		flgTaxOn = true;
		break;
	    }
	}
	return flgTaxOn;
    }

    private double funGetTaxIndicatorTotal(String indicator, List arrListItemDtlTemp) throws Exception
    {
	String sql_Query = "";
	double indicatorAmount = 0.00;
	for (int cnt = 0; cnt < arrListItemDtlTemp.size(); cnt++)
	{
	    String[] spItemDtl = arrListItemDtlTemp.get(cnt).toString().split("#");
	    sql_Query = "select " + spItemDtl[3]
		    + " from tblitemmaster "
		    + " where strItemCode='" + spItemDtl[0].substring(0, 7) + "' "
		    + " and strTaxIndicator='" + indicator + "' "
		    + " group by strTaxIndicator";
	    ResultSet rsTaxIndicator = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	    if (rsTaxIndicator.next())
	    {
		indicatorAmount += Double.parseDouble(rsTaxIndicator.getString(1));
	    }
	    rsTaxIndicator.close();
	}
	return indicatorAmount;
    }

    private double funGetTaxAmountForTaxOnTaxForIndicatorTax(String taxOnTaxCode, double indicatorTaxableAmt) throws Exception
    {
	double taxAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
	{
	    for (int t = 0; t < spTaxOnTaxCode.length; t++)
	    {
		ArrayList arrListTax = arrListTaxCal.get(cnt);
		if (arrListTax.get(0).toString().equals(spTaxOnTaxCode[t]))
		{
		    taxAmt += funGetTaxOnTaxAmtForIndicatorTax(spTaxOnTaxCode[t], indicatorTaxableAmt);
		}
	    }
	}
	return taxAmt;
    }

    private double funGetTaxOnTaxAmtForIndicatorTax(String taxCode, double taxableAmt) throws Exception
    {
	double taxAmt = 0;
	String sql = "select a.strTaxCode,a.strTaxType,a.dblPercent"
		+ " ,a.dblAmount,a.strTaxOnGD,a.strTaxCalculation "
		+ " from tbltaxhd a "
		+ " where a.strTaxOnSP='Sales' and a.strTaxCode='" + taxCode + "'";
	ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsTax.next())
	{
	    double taxPercent = rsTax.getDouble(3);
	    if (rsTax.getString(6).equals("Forward")) // Forward Tax Calculation
	    {
		taxAmt = taxableAmt * (taxPercent / 100);
	    }
	    else // Backward Tax Calculation
	    {
		taxAmt = taxableAmt * 100 / (100 + taxPercent);
		taxAmt = taxableAmt - taxAmt;
	    }
	}
	rsTax.close();
	return taxAmt;
    }

    private double funGetItemTypeTotal(String itemType, String formName) throws Exception
    {
	String sql_Query = "";
	double itemTypeAmount = 0.00;
	sql_Query = "select a.strItemCode,b.strTaxIndicator,sum(a.dblAmount) "
		+ "from tblbilldtl a,tblitemmaster b "
		+ "where a.strItemCode=b.strItemCode and b.strItemType='" + itemType + "' "
		+ "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strBillNo='" + voucherNo + "' and a.tdhComboItemYN='N' "
		+ " group by b.strItemType";
	//System.out.println(sql_Query);
	ResultSet raItemType = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	if (raItemType.next())
	{
	    itemTypeAmount = Double.parseDouble(raItemType.getString(3));
	}
	raItemType.close();
	return itemTypeAmount;
    }

    private boolean funFindSettlementForTax(String taxCode, String settlementMode) throws Exception
    {
	boolean flgTaxSettlement = false;
	String sql_SettlementTax = "select strSettlementCode,strSettlementName "
		+ "from tblsettlementtax where strTaxCode='" + taxCode + "' "
		+ "and strApplicable='true' and strSettlementCode='" + settlementMode + "'";
	ResultSet rsTaxSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_SettlementTax);
	if (rsTaxSettlement.next())
	{
	    flgTaxSettlement = true;
	}
	rsTaxSettlement.close();
	return flgTaxSettlement;
    }

    private double funGetTaxAmountForTaxOnTax(String taxOnTaxCode) throws Exception
    {
	double taxAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
	{
	    for (int t = 0; t < spTaxOnTaxCode.length; t++)
	    {
		ArrayList arrListTax = arrListTaxCal.get(cnt);
		if (arrListTax.get(0).toString().equals(spTaxOnTaxCode[t]))
		{
		    //taxableAmt += Double.parseDouble(arrListTax.get(2).toString()) + Double.parseDouble(arrListTax.get(3).toString());
		    taxAmt += Double.parseDouble(arrListTax.get(3).toString());
		}
	    }
	}
	return taxAmt;
    }

    private void funFillGridWithBillData()
    {
	try
	{
	    double billHdSubTotal = 0.00;
	    double billHdDiscAmt = 0.00;
	    double billHdNetTotal = 0.00;

	    double totalGrAmt = 0.00;
	    double advanceAmt = 0.00;
	    double dblTotalTaxAmt = 0.00;
	    String finalAmount = "";
	    listItemCode = new ArrayList<>();
	    //listKOTNO = new ArrayList<>();
	    listActualAmountOfItems = new ArrayList<>();
	    listItemName = new ArrayList<>();
	    listItemQTY = new ArrayList<>();
	    double dblDiscAmt = 0;
	    String modifierQuery = null;
	    double totalAmt = 0.00;
	    DefaultTableModel dmImemTable = (DefaultTableModel) tblItemTable.getModel();
	    dmImemTable.setRowCount(0);
	    sql = "select a.strTakeAway,a.strTableNo,ifnull(b.strAreaCode,''),a.dblSubTotal,a.dblDiscountAmt,a.strOperationType,a.intOrderNo "
		    + "from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + "where strBillNo='" + voucherNo + "' and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBill.next())
	    {
		takeAwayForTax = rsBill.getString(1);
		if (rsBill.getString(2).trim().length() > 0)
		{
		    billedAreaCode = rsBill.getString(3);
		}
		else
		{
		    billedAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}
		billHdSubTotal = rsBill.getDouble(4);//subTotal
		billHdDiscAmt = rsBill.getDouble(5);//discAmt
		billHdNetTotal = billHdSubTotal - billHdDiscAmt;//netTotal

		billedOperationType = rsBill.getString(6);//operation type
		intLastOrderNo = rsBill.getInt(7);
	    }
	    rsBill.close();
	    sql = "select strBillNo from tblhomedelivery where strBillNo='" + voucherNo + "' and date(dteDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    rsBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBill.next())
	    {
		homeDeliveryForTax = "Y";
	    }
	    else
	    {
		homeDeliveryForTax = "N";
	    }
	    rsBill.close();
	    sql = "select b.strItemName,sum(b.dblQuantity),sum(b.dblAmount),a.strWaiterNo,"
		    + " a.dblDiscountAmt,a.dblDiscountPer ,b.strItemCode ,b.strKOTNo,sum(b.dblDiscountAmt),b.dblRate "
		    + " from tblbillhd a,tblbilldtl b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and a.strBillNo='" + voucherNo + "' "
		    + " and b.tdhYN='N' "
		    + " group by b.strItemCode ";
//            sql = "select b.strItemName,b.dblQuantity,b.dblAmount,a.strWaiterNo,"
//                    + "a.dblDiscountAmt,a.dblDiscountPer ,b.strItemCode ,b.strKOTNo,b.dblDiscountAmt,b.dblRate "
//                    + "from tblbillhd a,tblbilldtl b where a.strBillNo=b.strBillNo "
//                    + "and a.strBillNo='" + voucherNo + "' and b.tdhYN='N' ";
	    ResultSet rsItemDetalis = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    int itemCount = 0;
	    mapItemCodeOriginalQty = new HashMap<String, frmSplitBill>();
	    while (rsItemDetalis.next())
	    {
		itemCount++;
		double tempAmt = Double.parseDouble(rsItemDetalis.getString(3));
		//double tempAmt = rsItemDetalis.getDouble(10) * rsItemDetalis.getDouble(2);//rate*qty becouse of promotion amt already redeemed

		totalAmt = totalAmt + tempAmt;
		dblDiscAmt = Double.parseDouble(rsItemDetalis.getString(5));
		listItemCode.add(rsItemDetalis.getString(7));
		//listKOTNO.add(rsItemDetalis.getString(8));
		listActualAmountOfItems.add(rsItemDetalis.getDouble(3));
		listItemName.add(rsItemDetalis.getString(1));
		listItemQTY.add(rsItemDetalis.getDouble(2));
		frmSplitBill objOriginalBillItemDtl = new frmSplitBill(rsItemDetalis.getString(1), rsItemDetalis.getDouble(2), tempAmt, rsItemDetalis.getString(7), rsItemDetalis.getDouble(9), rsItemDetalis.getDouble(10));
		mapItemCodeOriginalQty.put(rsItemDetalis.getString(7), objOriginalBillItemDtl);
		Object[] itemRows =
		{
		    rsItemDetalis.getString(1), rsItemDetalis.getString(2), tempAmt, rsItemDetalis.getString(7), rsItemDetalis.getDouble(9)
		};
		dmImemTable.addRow(itemRows);
		String itemCode = rsItemDetalis.getString(7);
		modifierQuery = "select Count(*) from tblbillmodifierdtl where strBillNo='" + voucherNo + "' and left(strItemCode,7)='" + itemCode + "'";
		ResultSet count = clsGlobalVarClass.dbMysql.executeResultSet(modifierQuery);
		count.next();
		int cntRecord = count.getInt(1);
		if (cntRecord > 0)
		{
		    modifierQuery = "select strModifierName,dblQuantity,dblAmount,strItemCode,dblDiscAmt,dblRate "
			    + "from tblbillmodifierdtl "
			    + "where strBillNo='" + voucherNo + "' and left(strItemCode,7)='" + itemCode + "'";
		    ResultSet modifierRecord = clsGlobalVarClass.dbMysql.executeResultSet(modifierQuery);
		    while (modifierRecord.next())
		    {
			itemCount++;
			Object[] ModifierRows =
			{
			    modifierRecord.getString(1), modifierRecord.getString(2), modifierRecord.getString(3), modifierRecord.getString(4), modifierRecord.getDouble(5)
			};
			dmImemTable.addRow(ModifierRows);
			totalAmt = totalAmt + modifierRecord.getDouble(3);
			frmSplitBill objOriginalBillModiDtl = new frmSplitBill(modifierRecord.getString(1), modifierRecord.getDouble(2), modifierRecord.getDouble(3), modifierRecord.getString(4), modifierRecord.getDouble(5), modifierRecord.getDouble(6));
			mapItemCodeOriginalQty.put(modifierRecord.getString(4) + "!" + modifierRecord.getString(1), objOriginalBillModiDtl);
			listItemCode.add(modifierRecord.getString(4));
			//listKOTNO.add(rsItemDetalis.getString(8));
			listActualAmountOfItems.add(modifierRecord.getDouble(3));
			listItemName.add(modifierRecord.getString(1));
			listItemQTY.add(modifierRecord.getDouble(2));
		    }
		}
	    }
	    rsItemDetalis.close();
	    clsGlobalVarClass.gItemCount = itemCount;
	    finalAmount = String.valueOf(totalAmt);
	    //funCalculateTax("direct");
	    sql = "select b.strTaxDesc,a.dblTaxAmount,b.strTaxCode  "
		    + " from tblbilltaxdtl a,tbltaxhd b "
		    + " where a.strTaxCode=b.strTaxCode "
		    + " and a.strBillNo='" + voucherNo + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsBillTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillTaxDtl.next())
	    {
		dblTotalTaxAmt = dblTotalTaxAmt + rsBillTaxDtl.getDouble(2);
		Object[] taxTotalRow =
		{
		    rsBillTaxDtl.getString(1), "", rsBillTaxDtl.getDouble(2), rsBillTaxDtl.getString(3)
		};
		dmImemTable.addRow(taxTotalRow);
	    }
	    rsBillTaxDtl.close();
	    tblItemTable.setModel(dmImemTable);
	    if (advanceBookNo == null)
	    {
		advanceAmt = 0.00;
	    }
	    else
	    {
		sql = "select dblAdvDeposite from tbladvancereceipthd "
			+ " where strAdvBookingNo='" + advanceBookNo + "'";
		ResultSet advanceorder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		advanceorder.next();
		Object[] r5 =
		{
		    "Advance Amt", "", advanceorder.getDouble("dblAdvDeposite")
		};
		advanceAmt = advanceorder.getDouble("dblAdvDeposite");
	    }
	    totalGrAmt = new Double(finalAmount.toString()) + dblTotalTaxAmt;
	    totalGrAmt = totalGrAmt - advanceAmt - dblDiscAmt;
	    totalGrAmt = Math.rint(totalGrAmt);
	    int rowCount = 0;
	    double balanceAmt = totalGrAmt;
	    for (rowCount = 0; rowCount < tblSettlement.getRowCount(); rowCount++)
	    {
		String Name = tblSettlement.getValueAt(rowCount, 1).toString();
		String Amount = tblSettlement.getValueAt(rowCount, 2).toString();
		Object[] row =
		{
		    Name, "", Amount
		};
		balanceAmt = balanceAmt - Double.parseDouble(Amount);
	    }
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	    billNo = txtBillSearch.getText();
	    funSetLabels(billHdSubTotal, billHdDiscAmt, billHdNetTotal);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funSetLabels(double subTotal, double totalDisc, double netAmount)
    {
	try
	{
	    lblSubTotal1.setVisible(true);
	    lblDiscount1.setVisible(true);
	    lblNetAmt1.setVisible(true);
	    lblSubTotal.setText(String.valueOf(subTotal));
	    lblDiscount.setText(String.valueOf(totalDisc));
	    lblNetAmount.setText(String.valueOf(netAmount));
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    void funSetBillData(Object[] data)
    {
	try
	{
	    funResetFields();
	    txtBillSearch.setText(data[0].toString());
	    voucherNo = data[0].toString();
	    dtBillDate = clsGlobalVarClass.getPOSDateForTransaction();
	    String billno = voucherNo + "-1";
	    String sql = "select strBillNo from tblbilldtl where strBillNo='" + billno + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsbill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsbill.next())
	    {
		new frmOkPopUp(null, "This Bill Already Splitted", "Error", 1).setVisible(true);
		funResetFields();
	    }
	    else
	    {
		funFillGridWithBillData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funEqualSplit(String billNo)
    {
	String bill = billNo;
	funInsertNewDataForEqualsSplit(bill);
    }

    
    /**
     * This method is responsible to split a bill into no of bills selected by number.
     * @param billNo 
     * 
     * This type simply divide the qty ,amount,tax by a number selected by a user.
     */
    private void funInsertNewDataForEqualsSplit(String billNo)
    {
	String bill = billNo;
	String sqlBillDtl = "", sqlBillHd = "", sqlModifier = "";
	try
	{
	    int splitQty = 0;
	    splitQty = Integer.valueOf(cmbSplitQty.getSelectedItem().toString());
	    funFillSplitItemAmountArrayList(splitQty);
	    listTaxCode = new ArrayList<>();
	    String sqlTaxList = "select strTaxCode from tblbilltaxdtl "
		    + "where strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rstaxList = clsGlobalVarClass.dbMysql.executeResultSet(sqlTaxList);
	    while (rstaxList.next())
	    {
		listTaxCode.add(rstaxList.getString(1));
	    }
	    rstaxList.close();
	    for (int i = 1; i <= splitQty; i++)
	    {
		String nwBillNo = bill.concat("-") + i;
		for (int j = 0; j < listItemCode.size(); j++)
		{
		    sqlBillDtl = "insert into tblbilldtl(strItemCode,strItemName"
			    + ",strBillNo,strAdvBookingNo,dblRate,dblQuantity,dblAmount"
			    + ",dblTaxAmount,dteBillDate,strKOTNo,strClientCode"
			    + ",strCustomerCode,tmeOrderProcessing,strDataPostFlag"
			    + ",strMMSDataPostFlag,strManualKOTNo,tdhYN,dblDiscountAmt,dblDiscountPer,dtBillDate)"
			    + "(select strItemCode,strItemName,'" + nwBillNo + "',strAdvBookingNo,dblRate,dblQuantity/('" + splitQty + "'),"
			    + "dblAmount/('" + splitQty + "'),if(dblAmount>0,dblTaxAmount/('" + splitQty + "'),0.00),dteBillDate,strKOTNo,"
			    + "strClientCode,strCustomerCode,tmeOrderProcessing,strDataPostFlag"
			    + ",strMMSDataPostFlag,strManualKOTNo,tdhYN,dblDiscountAmt/('" + splitQty + "'),dblDiscountPer,'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' from tblbilldtl "
			    + "where strBillNo='" + bill + "' "//and strKOTNo='" + listKOTNO.get(j) + "' "
			    + "and strItemCode='" + listItemCode.get(j) + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
		    clsGlobalVarClass.dbMysql.execute(sqlBillDtl);
		    if (funIsModifierPresent(bill, listItemCode.get(j)))
		    {
			sqlModifier = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,strModifierName"
				+ " ,dblRate,dblQuantity,dblAmount,strClientCode,strCustomerCode,strDataPostFlag,strMMSDataPostFlag"
				+ " ,strDefaultModifierDeselectedYN,strSequenceNo,dblDiscPer,dblDiscAmt,dteBillDate )"
				+ " (select '" + nwBillNo + "',strItemCode,strModifierCode,strModifierName,dblRate "
				+ " ,dblQuantity/('" + splitQty + "'),dblAmount/('" + splitQty + "'),strClientCode,strCustomerCode"
				+ " ,strDataPostFlag,strMMSDataPostFlag,strDefaultModifierDeselectedYN,strSequenceNo,dblDiscPer,dblDiscAmt/('" + splitQty + "'),'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
				+ " from tblbillmodifierdtl "
				+ " where strBillNo='" + bill + "' "
				+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
			clsGlobalVarClass.dbMysql.execute(sqlModifier);
		    }
//                    clsGlobalVarClass.dbMysql.execute("INSERT INTO tblbillpromotiondtl (strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount)"
//                            + "(select '" + nwBillNo + "',strItemCode,strPromotionCode,dblQuantity/('" + splitQty + "'),dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount/('" + splitQty + "') "
//                            + " from tblbillpromotiondtl where strBillNo='" + bill + "' and strItemCode='" + listItemCode.get(j) + "') ");
		}
		for (int t = 0; t < listTaxCode.size(); t++)
		{
		    String sql_tblbilltaxdtl = "insert into tblbilltaxdtl "
			    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,strDataPostFlag,dteBillDate) \n"
			    + "(select '" + nwBillNo + "', strTaxCode, dblTaxableAmount/('" + splitQty + "'),dblTaxAmount/('" + splitQty + "'),"
			    + "'" + clsGlobalVarClass.gClientCode + "',strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			    + "from tblbilltaxdtl "
			    + " where strBillNo='" + bill + "' "
			    + " and strTaxCode='" + listTaxCode.get(t) + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
		    clsGlobalVarClass.dbMysql.execute(sql_tblbilltaxdtl);
		    //System.out.println("sqlBilltaxdtl   =" + sql_tblbilltaxdtl);
		}
		sqlBillHd = "insert into tblbillhd(strBillNo,strAdvBookingNo,dteBillDate"
			+ ",strPOSCode,strSettelmentMode,dblDiscountAmt,dblDiscountPer"
			+ ",dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
			+ ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
			+ ",strClientCode,strTableNo,strWaiterNo,strCustomerCode,strManualBillNo"
			+ ",intShiftCode,intPaxNo,strDataPostFlag,strReasonCode,"
			+ "strRemarks,dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges"
			+ ",strCouponCode,strAreaCode,strDiscountOn,dblRoundOff,strTransactionType,dtBillDate,intOrderNo,dblUSDConverionRate)"
			+ "(select '" + nwBillNo + "',strAdvBookingNo,dteBillDate"
			+ ",strPOSCode,strSettelmentMode,dblDiscountAmt/('" + splitQty + "')"
			+ ",dblDiscountPer,ROUND(dblTaxAmt)/('" + splitQty + "'),dblSubTotal/('" + splitQty + "')"
			+ ",(dblSubTotal-dblDiscountAmt+ROUND(dblTaxAmt))/('" + splitQty + "')"
			+ ",strTakeAway,strOperationType,strUserCreated,strUserEdited,dteDateCreated"
			+ ",dteDateEdited,strClientCode,strTableNo,strWaiterNo,strCustomerCode"
			+ ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag,strReasonCode"
			+ ",strRemarks,dblTipAmount,'" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
			+ ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,dblRoundOff,concat(strTransactionType,',','SplitBill'),'" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
			+ ",intOrderNo,dblUSDConverionRate "
			+ "from tblbillhd where strBillNo='" + bill + "' "
			+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
		clsGlobalVarClass.dbMysql.execute(sqlBillHd);

		String sqlGT = "select dblGrandTotal from tblbillhd "
			+ " where strBillNo='" + nwBillNo + "' "
			+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		ResultSet rsGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlGT);
		if (rsGT.next())
		{
		    double grandTotal = rsGT.getDouble(1);

		    //start code to calculate roundoff amount and round off by amt
		    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
		    grandTotal = mapRoundOff.get("roundOffAmt");
		    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
		    //end code to calculate roundoff amount and round off by amt

		    clsGlobalVarClass.dbMysql.execute("update tblbillhd "
			    + "set dblGrandTotal='" + grandTotal + "'"
			    + ",dblRoundOff='" + grandTotalRoundOffBy + "'"
			    + "where strBillNo='" + nwBillNo + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");

		}
		rsGT.close();

		String sqlBillDiscDtl = "insert into tblbilldiscdtl "
			+ "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
			+ ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
			+ ",strClientCode,strDataPostFlag,dteBillDate) "
			+ "(select '" + nwBillNo + "',strPOSCode,dblDiscAmt/('" + splitQty + "'),dblDiscPer,dblDiscOnAmt/('" + splitQty + "'),strDiscOnType,strDiscOnValue "
			+ ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ ",strClientCode,strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			+ " from tblbilldiscdtl "
			+ " where strBillNo='" + bill + "' "
			+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ " )";
		clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
		billsToBePrinted.put(nwBillNo, clsGlobalVarClass.getPOSDateForTransaction());

	    }
	    String POSCode = "";
	    sql = "select strPOSCode from tblbillhd "
		    + " where strBillNo='" + bill + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsPOS.next())
	    {
		POSCode = rsPOS.getString(1);
	    }
	    rsPOS.close();
	    //System.out.println("sqlBillHd=" + sqlBillHd);
	    sql = "delete from tblbillhd where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "delete from tblbilldtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "delete from tblbilltaxdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "delete from tblbillmodifierdtl where strBillNo='" + bill + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "delete from tblbilldiscdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "delete from tblbillpromotiondtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    new frmOkPopUp(null, "Bill Split Successfully", "Successfull", 1).setVisible(true);
	    funResetFields();
	    frmOkCancelPopUp okCancelPopUp = new frmOkCancelPopUp(null, "Do You Want To Print All Bills?");
	    okCancelPopUp.setVisible(true);
	    int result = okCancelPopUp.getResult();
	    if (result == 1)
	    {
		clsUtility utility = new clsUtility();
		Iterator<Entry<String, String>> it = billsToBePrinted.entrySet().iterator();
		while (it.hasNext())
		{
		    Entry<String, String> entry = it.next();
		    utility.funPrintBill(entry.getKey(), entry.getValue(), false, POSCode, "print");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    
    /**
     * This function is responsible for to split a bill items group wise.
     * @param billNo 
     * 
     * This method simply get the distinct item group and depending on how many item groups are calculated,it will create a bill per item group.
     * 
     * ie. it will create a bill per item group
     */
    private void funGroupWiseSplit(String billNo)
    {
	hmGroupWiseBill.clear();
	hmSubGroupWiseBill.clear();
	try
	{
	    String sqlGroup = "select d.strGroupCode "
		    + " from tblbilldtl a "
		    + " left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
		    + " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
		    + " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
		    + " where a.strBillNo='" + billNo + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " group by d.strGroupCode";
	    ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sqlGroup);
	    while (rsGroup.next())
	    {
		List arrListGroupWiseDtl = new ArrayList<>();
		//items
		sql = "select b.strItemCode,a.dblAmount,b.strItemName"
			+ " ,a.dblQuantity,a.dblDiscountAmt,a.dblDiscountPer,c.strSubGroupName   "
			+ " from tblbilldtl a "
			+ " left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			+ " where a.strBillNo='" + billNo + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ " and d.strGroupCode='" + rsGroup.getString(1) + "'";
		//System.out.println(sql);
		ResultSet rsGroup1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsGroup1.next())
		{
		    String itemDtl = rsGroup1.getString(1) + "#" + rsGroup1.getString(3)
			    + "#" + rsGroup1.getString(4) + "#" + rsGroup1.getString(2) + "#" + rsGroup1.getString(5) + "#" + rsGroup1.getString(6) + "#" + rsGroup1.getString(7);
		    arrListGroupWiseDtl.add(itemDtl);
		}
//                //modifiers
		sql = "SELECT a.strItemCode,a.dblAmount,a.strModifierName,a.dblQuantity,a.dblDiscAmt,a.dblDiscPer,c.strSubGroupName  "
			+ " FROM tblbillmodifierdtl a "
			+ " LEFT OUTER "
			+ " JOIN tblitemmaster b ON left(a.strItemCode,7)=b.strItemCode "
			+ " LEFT OUTER "
			+ " JOIN tblsubgrouphd c ON b.strSubGroupCode=c.strSubGroupCode "
			+ " LEFT OUTER "
			+ " JOIN tblgrouphd d ON c.strGroupCode=d.strGroupCode "
			+ " WHERE a.strBillNo='" + billNo + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ " AND d.strGroupCode='" + rsGroup.getString(1) + "' ";
		//System.out.println(sql);
		rsGroup1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsGroup1.next())
		{
		    String itemDtl = rsGroup1.getString(1) + "#" + rsGroup1.getString(3)
			    + "#" + rsGroup1.getString(4) + "#" + rsGroup1.getString(2) + "#" + rsGroup1.getString(5) + "#" + rsGroup1.getString(6) + "#" + rsGroup1.getString(7);
		    arrListGroupWiseDtl.add(itemDtl);
		}
		hmGroupWiseBill.put(rsGroup.getString(1), arrListGroupWiseDtl);
	    }
	    if (hmGroupWiseBill.size() < 2)
	    {
		new frmOkPopUp(null, "Can Not Split Only One Group Is Available.", "Error", 1).setVisible(true);
		return;
	    }
	    else
	    {
		funInsertNewDataGroupWise(billNo);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertNewDataGroupWise(String billNo) throws Exception
    {
	String bill = billNo;
	String insertQuery = "";
	int billCnt = 1;
	double dblTotalTaxAmt = 0.00;
	for (Map.Entry<String, List<Object>> entry : hmGroupWiseBill.entrySet())
	{
	    String groupCode = entry.getKey();
	    ArrayList arrListGroupWiseItemDtl = (ArrayList) entry.getValue();
	    double subTotal = 0;
	    double totalDiscount = 0;
	    String newBillNo = bill.concat("-") + billCnt;
	    dblTotalTaxAmt = 0;
	    List listTax = funCalculateTax(billedOperationType, arrListGroupWiseItemDtl);
	    for (int cntTax = 0; cntTax < listTax.size(); cntTax++)
	    {
		//System.out.println(listTax.get(cntTax));
		ArrayList<Object> list = (ArrayList<Object>) listTax.get(cntTax);
		double dblTaxAmt = Double.parseDouble(list.get(3).toString());
		if (list.get(4).toString().equalsIgnoreCase("Forward"))
		{
		    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
		}
		String sqlBillTax = "insert into tblbilltaxdtl "
			+ "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate) "
			+ "values('" + newBillNo + "','" + list.get(0).toString() + "'," + list.get(2).toString() + ""
			+ "," + dblTaxAmt + ",'" + clsGlobalVarClass.gClientCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
		clsGlobalVarClass.dbMysql.execute(sqlBillTax);
	    }
	    for (int cnt1 = 0; cnt1 < arrListGroupWiseItemDtl.size(); cnt1++)
	    {
		String[] spItemDtl = arrListGroupWiseItemDtl.get(cnt1).toString().split("#");
		subTotal += Double.parseDouble(spItemDtl[3]);
		totalDiscount = totalDiscount + Double.parseDouble(spItemDtl[4]);
		double rate = Double.parseDouble(spItemDtl[3]) / Double.parseDouble(spItemDtl[2]);
		if (spItemDtl[0].contains("M"))
		{
		    sql = "insert into tblbillmodifierdtl"
			    + " (select '" + newBillNo + "',strItemCode,strModifierCode,strModifierName"
			    + " ,dblRate,dblQuantity,dblAmount,strClientCode,strCustomerCode"
			    + " ,strDataPostFlag,strMMSDataPostFlag,strDefaultModifierDeselectedYN,strSequenceNo,dblDiscPer,dblDiscAmt,dteBillDate "
			    + " from tblbillmodifierdtl "
			    + " where strItemCode='" + spItemDtl[0] + "' "
			    + " and strModifierName='" + spItemDtl[1] + "' "
			    + " and strBillNo='" + bill + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
		    clsGlobalVarClass.dbMysql.execute(sql);
		}
		else
		{
		    String sqlBill = "select strKOTNo,strCustomerCode,tmeOrderProcessing"
			    + " ,strManualKOTNo,tdhYN,strPromoCode "
			    + " from tblbilldtl "
			    + " where strBillNo='" + bill + "' "
			    + " and strItemCode='" + spItemDtl[0] + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    ResultSet rsItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBill);
		    if (rsItemDtl.next())
		    {
			insertQuery = "insert into tblbilldtl "
				+ "(strItemCode,strItemName,strBillNo,dblRate"
				+ ",dblQuantity,dblAmount,dblTaxAmount,dteBillDate"
				+ ",strKOTNo,strClientCode,strCustomerCode,tmeOrderProcessing"
				+ ",strManualKOTNo, tdhYN,strPromoCode,dblDiscountAmt,dblDiscountPer,dtBillDate) "
				+ "values ('" + spItemDtl[0] + "','" + spItemDtl[1] + "','" + newBillNo + "',"
				+ "'" + rate + "','" + spItemDtl[2] + "','" + spItemDtl[3] + "','0',"
				+ "'" + dtBillDate + "','" + rsItemDtl.getString(1) + "',"
				+ "'" + clsGlobalVarClass.gClientCode + "','" + rsItemDtl.getString(2) + "',"
				+ "'" + rsItemDtl.getTime(3) + "','" + rsItemDtl.getString(4) + "',"
				+ "'" + rsItemDtl.getString(5) + "','" + rsItemDtl.getString(6) + "','" + spItemDtl[4] + "',"
				+ "'" + spItemDtl[5] + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' );";
			clsGlobalVarClass.dbMysql.execute(insertQuery);
//                        clsGlobalVarClass.dbMysql.execute("update tblbillpromotiondtl set strBillNo='" + newBillNo + "' "
//                                + "where strBillNo='" + bill + "' and strItemCode='" + spItemDtl[0] + "' ");
		    }
		}
		ResultSet rsDiscType = clsGlobalVarClass.dbMysql.executeResultSet("select * "
			+ " from tblbilldiscdtl a  "
			+ " where a.strBillNo='" + bill + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
		while (rsDiscType.next())
		{
		    if (rsDiscType.getString("strDiscOnType").equals("ItemWise") && spItemDtl[1].equals(rsDiscType.getString("strDiscOnValue")))
		    {
			clsGlobalVarClass.dbMysql.execute("update tblbilldiscdtl  "
				+ " set strBillNo='" + newBillNo + "' "
				+ " where strBillNo='" + bill + "' "
				+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
				+ " and strDiscOnType='ItemWise' "
				+ " and strDiscOnValue='" + spItemDtl[1] + "' ");
		    }
		}
	    }
	    double grandTotal = subTotal + dblTotalTaxAmt - totalDiscount;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
	    grandTotal = mapRoundOff.get("roundOffAmt");
	    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt

	    String sqlItemTypeWiseHd = "insert into tblbillhd"
		    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode"
		    + ",strSettelmentMode,dblDiscountAmt,dblDiscountPer,dblTaxAmt"
		    + ",dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
		    + ",strClientCode,strTableNo,strWaiterNo,strCustomerCode"
		    + ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag"
		    + ",strReasonCode,strRemarks,dblTipAmount,dteSettleDate"
		    + ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,dblRoundOff,strTransactionType,dtBillDate"
		    + ",intOrderNo,dblUSDConverionRate ) "
		    + "(select '" + newBillNo + "',strAdvBookingNo,dteBillDate"
		    + ",strPOSCode,strSettelmentMode,'" + totalDiscount + "'"
		    + ",'" + (totalDiscount * 100) / subTotal + "','" + dblTotalTaxAmt + "','" + subTotal + "','" + grandTotal + "'"
		    + ",strTakeAway,strOperationType,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strClientCode,strTableNo,strWaiterNo,strCustomerCode"
		    + ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag"
		    + ",strReasonCode,strRemarks,dblTipAmount,'" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
		    + ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,'" + grandTotalRoundOffBy + "'"
		    + ",CONCAT(strTransactionType,',','SplitBill'),'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",intOrderNo,dblUSDConverionRate "
		    + " from tblbillhd "
		    + " where strBillNo='" + bill + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlItemTypeWiseHd);
	    ResultSet rsGroupName = clsGlobalVarClass.dbMysql.executeResultSet("select a.strGroupName from tblgrouphd a "
		    + "where a.strGroupCode='" + groupCode + "' ");
	    if (rsGroupName.next())
	    {
		ResultSet rsDiscType = clsGlobalVarClass.dbMysql.executeResultSet("select * "
			+ " from tblbilldiscdtl a  "
			+ " where a.strBillNo='" + bill + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
			+ " and strDiscOnValue='" + rsGroupName.getString(1) + "' ");
		while (rsDiscType.next())
		{
		    if (rsDiscType.getString("strDiscOnType").equals("GroupWise") && rsGroupName.getString(1).equals(rsDiscType.getString("strDiscOnValue")))
		    {
			if (totalDiscount > 0)
			{
			    double discOnAmount = subTotal;
			    double discPercent = (totalDiscount * 100) / subTotal;
			    String sqlBillDiscDtl = "insert into tblbilldiscdtl "
				    + "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
				    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
				    + ",strClientCode,strDataPostFlag,dteBillDate) "
				    + "(select '" + newBillNo + "',strPOSCode,'" + totalDiscount + "',dblDiscPer,'" + discOnAmount + "',strDiscOnType,strDiscOnValue "
				    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
				    + ",strClientCode,strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
				    + " from tblbilldiscdtl "
				    + " where strBillNo='" + bill + "' "
				    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
				    + " and strDiscOnType='GroupWise'"
				    + " and strDiscOnValue='" + rsGroupName.getString(1) + "' "
				    + ")";
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
			}
		    }
		}
	    }
	    //subGroup wise discount
	    ResultSet rsSubGroup = clsGlobalVarClass.dbMysql.executeResultSet("select * "
		    + " from tblbilldiscdtl a "
		    + " where a.strBillNo='" + bill + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and a.strDiscOnType='SubGroupWise'  ");
	    while (rsSubGroup.next())
	    {
		String discOnValue = rsSubGroup.getString("strDiscOnValue");
		subTotal = 0.00;
		totalDiscount = 0.00;
		for (int cnt1 = 0; cnt1 < arrListGroupWiseItemDtl.size(); cnt1++)
		{
		    String[] spItemDtl = arrListGroupWiseItemDtl.get(cnt1).toString().split("#");
		    if (spItemDtl[6].equals(discOnValue))
		    {
			subTotal += Double.parseDouble(spItemDtl[3]);
			totalDiscount = totalDiscount + Double.parseDouble(spItemDtl[4]);
		    }
		}
		if (totalDiscount > 0)
		{
		    double discOnAmount = subTotal;
		    double discPercent = (totalDiscount * 100) / subTotal;
		    String sqlBillDiscDtl = "insert into tblbilldiscdtl "
			    + "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
			    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
			    + ",strClientCode,strDataPostFlag,dteBillDate) "
			    + "(select '" + newBillNo + "',strPOSCode,'" + totalDiscount + "',dblDiscPer,'" + discOnAmount + "',strDiscOnType,strDiscOnValue "
			    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + ",strClientCode,strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			    + " from tblbilldiscdtl "
			    + " where strBillNo='" + bill + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and strDiscOnType='SubGroupWise'"
			    + " and strDiscOnValue='" + discOnValue + "' "
			    + ")";
		    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
		}
	    }
	    rsSubGroup.close();

	    //Total wise discount
	    ResultSet rsTotal = clsGlobalVarClass.dbMysql.executeResultSet("select * "
		    + " from tblbilldiscdtl a "
		    + " where a.strBillNo='" + bill + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and a.strDiscOnType='Total'  ");
	    while (rsTotal.next())
	    {
		String discOnValue = rsTotal.getString("strDiscOnValue");
		subTotal = 0.00;
		totalDiscount = 0.00;
		for (int cnt1 = 0; cnt1 < arrListGroupWiseItemDtl.size(); cnt1++)
		{
		    String[] spItemDtl = arrListGroupWiseItemDtl.get(cnt1).toString().split("#");

		    subTotal += Double.parseDouble(spItemDtl[3]);
		    totalDiscount = totalDiscount + Double.parseDouble(spItemDtl[4]);

		}
		if (totalDiscount > 0)
		{
		    double discOnAmount = subTotal;
		    double discPercent = (totalDiscount * 100) / subTotal;
		    String sqlBillDiscDtl = "insert into tblbilldiscdtl "
			    + "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
			    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
			    + ",strClientCode,strDataPostFlag,dteBillDate) "
			    + "(select '" + newBillNo + "',strPOSCode,'" + totalDiscount + "',dblDiscPer,'" + discOnAmount + "',strDiscOnType,strDiscOnValue "
			    + ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + ",strClientCode,strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			    + " from tblbilldiscdtl "
			    + " where strBillNo='" + bill + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and strDiscOnType='Total'"
			    + " and strDiscOnValue='" + discOnValue + "' "
			    + ")";
		    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
		}
	    }
	    rsTotal.close();

	    billsToBePrinted.put(newBillNo, clsGlobalVarClass.getPOSDateForTransaction());
	    billCnt++;
	}
	String POSCode = "";
	sql = "select strPOSCode from tblbillhd "
		+ " where strBillNo='" + bill + "' "
		+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsPOS.next())
	{
	    POSCode = rsPOS.getString(1);
	}
	rsPOS.close();
	sql = "delete from tblbilltaxdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillmodifierdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbilldtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillhd where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbilldiscdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillpromotiondtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	new frmOkPopUp(null, "Bill Split Successfully", "Successfull", 1).setVisible(true);
	funResetFields();
	frmOkCancelPopUp okCancelPopUp = new frmOkCancelPopUp(null, "Do You Want To Print All Bills?");
	okCancelPopUp.setVisible(true);
	int result = okCancelPopUp.getResult();
	if (result == 1)
	{
	    clsUtility utility = new clsUtility();
	    Iterator<Entry<String, String>> it = billsToBePrinted.entrySet().iterator();
	    while (it.hasNext())
	    {
		Entry<String, String> entry = it.next();
		utility.funPrintBill(entry.getKey(), entry.getValue(), false, POSCode, "print");
	    }
	}
    }

     /**
     * This function is responsible for to split a bill items type wise. Item type is get define from item master.
     * @param billNo 
     * 
     * This method simply get the distinct item type and depending on how many item type are calculated,it will create a bill per item type.
     * 
     * ie. it will create a bill per item type.
     */
    private void funItemTypeWiseSplit(String billNo)
    {
	String sqlItemType = "";
	//listSplittedAmountOfItemsType = new ArrayList<>();
	listItemTypeForItemType = new ArrayList<>();
	//List arrListItemTypeWiseDtl = new ArrayList<>();
	hmItemTypeWiseBill.clear();
	try
	{
	    String sqlItemTypeCount = "select a.strItemType "
		    + " from tblitemmaster a,tblbilldtl b "
		    + " where b.strBillNo='" + billNo + "' "
		    + " and date(b.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and a.strItemCode=b.strItemCode "
		    + " group by strItemType";
	    ResultSet rsItemType = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemTypeCount);
	    while (rsItemType.next())
	    {
		listItemTypeForItemType.add(rsItemType.getString(1));
	    }
	    if (listItemTypeForItemType.size() > 1)
	    {
		for (int cnt = 0; cnt < listItemTypeForItemType.size(); cnt++)
		{
		    List arrListItemTypeWiseDtl = new ArrayList<>();
		    sqlItemType = "select a.strItemCode,b.dblAmount,b.strItemName"
			    + " ,b.dblQuantity,b.dblDiscountAmt,b.dblDiscountPer "
			    + " from tblitemmaster a,tblbilldtl b "
			    + " where a.strItemCode=b.strItemCode "
			    + " and b.strBillNo='" + billNo + "' "
			    + " and date(b.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and a.strItemType='" + listItemTypeForItemType.get(cnt).toString() + "'";
		    //System.out.println(sqlItemType);
		    ResultSet rsItemType1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemType);
		    while (rsItemType1.next())
		    {
			//listItemCodeForItemType.add(rsItemType1.getString(1));
			//listSplittedAmountOfItemsType.add(rsItemType1.getDouble(2));
			String itemDtl = rsItemType1.getString(1) + "#" + rsItemType1.getString(3)
				+ "#" + rsItemType1.getString(4) + "#" + rsItemType1.getString(2) + "#" + rsItemType1.getString(5) + "#" + rsItemType1.getString(6);
			arrListItemTypeWiseDtl.add(itemDtl);
		    }
		    hmItemTypeWiseBill.put(listItemTypeForItemType.get(cnt).toString(), arrListItemTypeWiseDtl);
		}
		funInsertNewDataItemTypeWise(billNo);
	    }
	    else
	    {
		new frmOkPopUp(null, "Only One Item Type Available", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertNewDataItemTypeWise(String billNoItemType) throws Exception
    {
	String bill = billNoItemType;
	String insertQuery = "";
	int billCnt = 1;
	double dblTotalTaxAmt = 0.00;
	for (int cnt = 0; cnt < listItemTypeForItemType.size(); cnt++)
	{
	    ArrayList arrListItemType = (ArrayList) hmItemTypeWiseBill.get(listItemTypeForItemType.get(cnt));
	    double subTotal = 0;
	    double finalDiscAmt = 0.00;
	    double finalDiscPer = 0.00;
	    String newBillNo = bill.concat("-") + billCnt;
	    dblTotalTaxAmt = 0;
	    List listTax = funCalculateTax(billedOperationType, arrListItemType);
	    for (int cntTax = 0; cntTax < listTax.size(); cntTax++)
	    {
		//System.out.println(listTax.get(cntTax));
		ArrayList<Object> list = (ArrayList<Object>) listTax.get(cntTax);
		double dblTaxAmt = Double.parseDouble(list.get(3).toString());
		if (list.get(4).toString().equalsIgnoreCase("Forward"))
		{
		    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
		}
		String sqlBillTax = "insert into tblbilltaxdtl "
			+ "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate) "
			+ "values('" + newBillNo + "','" + list.get(0).toString() + "'," + list.get(2).toString() + ""
			+ "," + dblTaxAmt + ",'" + clsGlobalVarClass.gClientCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
		clsGlobalVarClass.dbMysql.execute(sqlBillTax);
	    }
	    double discOnAmt = 0.00;
	    for (int cnt1 = 0; cnt1 < arrListItemType.size(); cnt1++)
	    {
		String[] spItemDtl = arrListItemType.get(cnt1).toString().split("#");
		subTotal += Double.parseDouble(spItemDtl[3]);
		double rate = Double.parseDouble(spItemDtl[3]) / Double.parseDouble(spItemDtl[2]);
		double discounrAmt = Double.parseDouble(spItemDtl[4]);
		if (discounrAmt > 0)
		{
		    discOnAmt += Double.parseDouble(spItemDtl[3]);
		}
		finalDiscAmt += discounrAmt;
		double discounrPer = Double.parseDouble(spItemDtl[5]);
		String sqlBill = "select strKOTNo,strCustomerCode,tmeOrderProcessing"
			+ ",strManualKOTNo,tdhYN,strPromoCode from tblbilldtl "
			+ "where strBillNo='" + bill + "' and strItemCode='" + spItemDtl[0] + "' ";
		ResultSet rsItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBill);
		if (rsItemDtl.next())
		{
		    insertQuery = "insert into tblbilldtl "
			    + "(strItemCode,strItemName,strBillNo,dblRate"
			    + ",dblQuantity,dblAmount,dblTaxAmount,dteBillDate"
			    + ",strKOTNo,strClientCode,strCustomerCode,tmeOrderProcessing"
			    + ",strManualKOTNo, tdhYN,strPromoCode,dblDiscountAmt,dblDiscountPer,dtBillDate ) "
			    + "values ('" + spItemDtl[0] + "','" + spItemDtl[1] + "','" + newBillNo + "',"
			    + "'" + rate + "','" + spItemDtl[2] + "','" + spItemDtl[3] + "','0',"
			    + "'" + dtBillDate + "','" + rsItemDtl.getString(1) + "',"
			    + "'" + clsGlobalVarClass.gClientCode + "','" + rsItemDtl.getString(2) + "',"
			    + "'" + rsItemDtl.getTime(3) + "','" + rsItemDtl.getString(4) + "',"
			    + "'" + rsItemDtl.getString(5) + "','" + rsItemDtl.getString(6) + "','" + discounrAmt + "','" + discounrPer + "',"
			    + "'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' );";
		    clsGlobalVarClass.dbMysql.execute(insertQuery);
//                    clsGlobalVarClass.dbMysql.execute("update tblbillpromotiondtl set strBillNo='" + newBillNo + "' "
//                            + "where strBillNo='" + bill + "' and strItemCode='" + spItemDtl[0] + "' ");
		    sql = "select dblDiscAmt,dblAmount "
			    + " from tblbillmodifierdtl "
			    + " where left(strItemCode,7)='" + spItemDtl[0] + "' "
			    + " and strModifierName='" + spItemDtl[1] + "' "
			    + " and strBillNo='" + bill + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    while (rsModi.next())
		    {
			finalDiscAmt += rsModi.getDouble(1);//discAmt
			subTotal += rsModi.getDouble(2);//amount
			if (discounrAmt > 0)
			{
			    discOnAmt += rsModi.getDouble(2);//amount
			}
		    }
		    sql = "insert into tblbillmodifierdtl"
			    + " (select '" + newBillNo + "',strItemCode,strModifierCode,strModifierName"
			    + " ,dblRate,dblQuantity,dblAmount,strClientCode,strCustomerCode"
			    + " ,strDataPostFlag,strMMSDataPostFlag,strDefaultModifierDeselectedYN,strSequenceNo,dblDiscPer,dblDiscAmt,dteBillDate "
			    + " from tblbillmodifierdtl "
			    + " where left(strItemCode,7)='" + spItemDtl[0] + "' "
			    + " and strModifierName='" + spItemDtl[1] + "' "
			    + " and strBillNo='" + bill + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
		    clsGlobalVarClass.dbMysql.execute(sql);
		}
	    }
	    finalDiscPer = (finalDiscAmt / subTotal) * 100;
	    double grandTotal = subTotal + dblTotalTaxAmt - finalDiscAmt;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
	    grandTotal = mapRoundOff.get("roundOffAmt");
	    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt

	    String sqlItemTypeWiseHd = "insert into tblbillhd"
		    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode"
		    + ",strSettelmentMode,dblDiscountAmt,dblDiscountPer,dblTaxAmt"
		    + ",dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
		    + ",strClientCode,strTableNo,strWaiterNo,strCustomerCode"
		    + ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag"
		    + ",strReasonCode,strRemarks,dblTipAmount,dteSettleDate"
		    + ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,dblRoundOff,strTransactionType,dtBillDate"
		    + ",intOrderNo,dblUSDConverionRate ) "
		    + "(select '" + newBillNo + "',strAdvBookingNo,dteBillDate"
		    + ",strPOSCode,strSettelmentMode,'" + finalDiscAmt + "' "
		    + ",'" + finalDiscPer + "','" + dblTotalTaxAmt + "','" + subTotal + "','" + grandTotal + "'"
		    + ",strTakeAway,strOperationType,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strClientCode,strTableNo,strWaiterNo,strCustomerCode"
		    + ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag"
		    + ",strReasonCode,strRemarks,dblTipAmount,'" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
		    + ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,'" + grandTotalRoundOffBy + "'"
		    + ",CONCAT(strTransactionType,',','SplitBill'),'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",intOrderNo,dblUSDConverionRate "
		    + " from tblbillhd where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlItemTypeWiseHd);
	    if (finalDiscAmt > 0)
	    {
		double discOnAmount = discOnAmt;
		double discPercent = (finalDiscAmt / discOnAmt) * 100;
		String sqlBillDiscDtl = "insert into tblbilldiscdtl "
			+ "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
			+ ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
			+ ",strClientCode,strDataPostFlag,dteBillDate) "
			+ "(select '" + newBillNo + "',strPOSCode,'" + finalDiscAmt + "','" + discPercent + "','" + discOnAmount + "',strDiscOnType,strDiscOnValue "
			+ ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ ",strClientCode,strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			+ " from tblbilldiscdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ ")";
		clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
	    }
	    billsToBePrinted.put(newBillNo, clsGlobalVarClass.getPOSDateForTransaction());
	    billCnt++;
	}
	String POSCode = "";
	sql = "select strPOSCode from tblbillhd where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsPOS.next())
	{
	    POSCode = rsPOS.getString(1);
	}
	rsPOS.close();
	sql = "delete from tblbilltaxdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillmodifierdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbilldtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillhd where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbilldiscdtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "delete from tblbillpromotiondtl where strBillNo='" + bill + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
	new frmOkPopUp(null, "Bill Split Successfully", "Successfull", 1).setVisible(true);
	funResetFields();
	frmOkCancelPopUp okCancelPopUp = new frmOkCancelPopUp(null, "Do You Want To Print All Bills?");
	okCancelPopUp.setVisible(true);
	int result = okCancelPopUp.getResult();
	if (result == 1)
	{
	    clsUtility utility = new clsUtility();
	    Iterator<Entry<String, String>> it = billsToBePrinted.entrySet().iterator();
	    while (it.hasNext())
	    {
		Entry<String, String> entry = it.next();
		utility.funPrintBill(entry.getKey(), entry.getValue(), false, POSCode, "print");
	    }
	}
    }

    private void funFillSplitItemAmountArrayList(int qty)
    {
	listSplittedAmountOfItems = new ArrayList<>();
	for (double i : listActualAmountOfItems)
	{
	    double splitAmt = i;
	    splitAmt = splitAmt / qty;
	    listSplittedAmountOfItems.add(splitAmt);
	}
    }

    private void funSplitTypeComboClicked()
    {
	if (cmbSplitType.getSelectedIndex() > 0)
	{
	    if (txtBillSearch.getText().equals(""))
	    {
		new frmOkPopUp(null, "Please Select Bill First", "Error", 1).setVisible(true);
		cmbSplitType.setSelectedIndex(0);
	    }
	    else if (cmbSplitType.getSelectedIndex() == 0)
	    {
		funHideTables();
		lblPersons.setVisible(false);
		cmbSplitQty.setVisible(false);
		btnMoveNext.setVisible(false);
		btnMovePrev.setVisible(false);
	    }
	    else if (cmbSplitType.getSelectedIndex() > 0)
	    {
		String splitType = cmbSplitType.getSelectedItem().toString();
		switch (splitType)
		{
		    case "Equal Split":
			funHideTables();
			lblPersons.setVisible(true);
			cmbSplitQty.setVisible(true);
			btnMoveNext.setVisible(false);
			btnMovePrev.setVisible(false);
			funFillQtyCombo();
			break;
		    case "Group Wise":
			funHideTables();
			lblPersons.setVisible(false);
			cmbSplitQty.setVisible(false);
			btnMoveNext.setVisible(false);
			btnMovePrev.setVisible(false);
			break;
		    case "Item Type Wise":
			funHideTables();
			lblPersons.setVisible(false);
			cmbSplitQty.setVisible(false);
			btnMoveNext.setVisible(false);
			btnMovePrev.setVisible(false);
			funSortItemsItemTypeWise();
			break;
		    case "Item Wise":
			funHideTables();
			lblPersons.setVisible(true);
			cmbSplitQty.setVisible(true);
			btnMoveNext.setVisible(false);
			btnMovePrev.setVisible(false);
			funFillQtyCombo();
			break;
		}
	    }
	}
    }

    private void funSplitQtyComboClicked()
    {
	funClearObjectLists();
	if ((cmbSplitQty.getSelectedIndex() > 0) && (cmbSplitType.getSelectedItem().equals("Equal Split")))
	{
	    funFillGridForEqualSplit();
	}
	else if ((cmbSplitQty.getSelectedIndex() > 0) && (cmbSplitType.getSelectedItem().equals("Item Wise")))
	{
	    int qtyItemW = Integer.valueOf(cmbSplitQty.getSelectedItem().toString());
	    funReseteOnlyRightSideGrids();
	    funFillGridWithBillData();
	    funGenerateKOTWiseItemList();
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    switch (qtyItemW)
	    {
		case 1:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(false);
		    scrBillSplitDtl3.setVisible(false);
		    scrBillSplitDtl4.setVisible(false);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    btnMoveNext.setVisible(true);
		    break;
		case 2:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(false);
		    scrBillSplitDtl4.setVisible(false);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
		case 3:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(true);
		    scrBillSplitDtl4.setVisible(false);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
		case 4:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(true);
		    scrBillSplitDtl4.setVisible(true);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
	    }
	}
	else
	{
	    funHideTables();
	}
    }

    private void funResetFields()
    {
	try
	{
	    funClearObjectLists();
	    cmbSplitType.setSelectedIndex(0);
	    selectedTable = "NA";
	    funHideTables();
	    DefaultTableModel dm = (DefaultTableModel) tblItemTable.getModel();
	    dm.setRowCount(0);
	    txtBillSearch.setText("");
	    lblPersons.setVisible(false);
	    cmbSplitQty.setVisible(false);
	    lblSubTotal1.setVisible(false);
	    lblDiscount1.setVisible(false);
	    lblNetAmt1.setVisible(false);
	    lblSubTotal.setText("");
	    lblDiscount.setText("");
	    lblNetAmount.setText("");
	    btnMovePrev.setVisible(false);
	    btnMoveNext.setVisible(false);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillTable1ForManualSplit()
    {
	try
	{
	    int count = 0;
	    boolean flagModifier = false;
	    DefaultTableModel dm_tblTable1 = (DefaultTableModel) tblTable1.getModel();
	    String temp_ItemName = tblItemTable.getValueAt(selecedRow, 0).toString();
	    if (!temp_ItemName.startsWith("-->"))
	    {
		Double temp_Qty = Double.valueOf(tblItemTable.getValueAt(selecedRow, 1).toString());
		Double temp_Amt = Double.valueOf(tblItemTable.getValueAt(selecedRow, 2).toString());
		String tempCode = tblItemTable.getValueAt(selecedRow, 3).toString();
		if (temp_Qty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(null, true, "qty", temp_Qty);
		    num.setVisible(true);
		    //selectedQty = num.getResult();
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			clsGlobalVarClass.gNumerickeyboardValue = null;
		    }
		}
		else
		{
		    selectedQty = 1;
		}
		if (selectedQty <= 0 || selectedQty > temp_Qty)
		{
		    new frmOkPopUp(this, "Please Enter Valid Qty.", "error", 0).setVisible(true);
		    return;
		}
		double discount = funGetItemWiseDiscount(txtBillSearch.getText(), tempCode, temp_ItemName);
		frmSplitBill obj = mapItemCodeOriginalQty.get(tempCode);
		funFillListForManualSplit(list_Grid1_SplitedItems, temp_ItemName, selectedQty, ((obj.cls_Amt / obj.cls_qty) * selectedQty), tempCode, ((obj.discountAmt / obj.cls_qty) * selectedQty), obj.cls_Rate);
		if (funIsModifierPresent(billNo, tempCode))
		{
		    count = funAddModifier(list_Grid1_SplitedItems);
		    flagModifier = true;
		}
		tblTable1.setModel(dm_tblTable1);
		funRefreshGrids(dm_tblTable1);
		funRemoveItems(flagModifier, count);
	    }
	}
	catch (Exception e)
	{
	    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
	    e.printStackTrace();
	}
    }

    private void funFillTable2ForManualSplit()
    {
	try
	{
	    int count = 0;
	    boolean flagModifier = false;
	    DefaultTableModel dm_tblTable2 = (DefaultTableModel) tblTable2.getModel();
	    String temp_ItemName = tblItemTable.getValueAt(selecedRow, 0).toString();
	    if (!temp_ItemName.startsWith("-->"))
	    {
		Double temp_Qty = Double.valueOf(tblItemTable.getValueAt(selecedRow, 1).toString());
		Double temp_Amt = Double.valueOf(tblItemTable.getValueAt(selecedRow, 2).toString());
		String tempCode = tblItemTable.getValueAt(selecedRow, 3).toString();
		if (temp_Qty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(null, true, "qty", temp_Qty);
		    num.setVisible(true);
		    //selectedQty = num.getResult();
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			clsGlobalVarClass.gNumerickeyboardValue = null;
		    }
		}
		else
		{
		    selectedQty = 1;
		}
		if (selectedQty <= 0 || selectedQty > temp_Qty)
		{
		    new frmOkPopUp(this, "Please Enter Valid Qty.", "error", 0).setVisible(true);
		    return;
		}
		double discount = funGetItemWiseDiscount(txtBillSearch.getText(), tempCode, temp_ItemName);
		frmSplitBill obj = mapItemCodeOriginalQty.get(tempCode);
		funFillListForManualSplit(list_Grid2_SplitedItems, temp_ItemName, selectedQty, ((obj.cls_Amt / obj.cls_qty) * selectedQty), tempCode, ((obj.discountAmt / obj.cls_qty) * selectedQty), obj.cls_Rate);
		if (funIsModifierPresent(billNo, tempCode))
		{
		    count = funAddModifier(list_Grid2_SplitedItems);
		    flagModifier = true;
		}
		tblTable2.setModel(dm_tblTable2);
		funRefreshGrids(dm_tblTable2);
		funRemoveItems(flagModifier, count);
	    }
	}
	catch (Exception e)
	{
	    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
	    //e.printStackTrace();
	}
    }

    private void funFillTable3ForManualSplit()
    {
	try
	{
	    int count = 0;
	    boolean flagModifier = false;
	    DefaultTableModel dm_tblTable3 = (DefaultTableModel) tblTable3.getModel();
	    String temp_ItemName = tblItemTable.getValueAt(selecedRow, 0).toString();
	    if (!temp_ItemName.startsWith("-->"))
	    {
		Double temp_Qty = Double.valueOf(tblItemTable.getValueAt(selecedRow, 1).toString());
		Double temp_Amt = Double.valueOf(tblItemTable.getValueAt(selecedRow, 2).toString());
		String tempCode = tblItemTable.getValueAt(selecedRow, 3).toString();
		if (temp_Qty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(null, true, "qty", temp_Qty);
		    num.setVisible(true);
		    //selectedQty = num.getResult();
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			clsGlobalVarClass.gNumerickeyboardValue = null;
		    }
		}
		else
		{
		    selectedQty = 1;
		}
		if (selectedQty <= 0 || selectedQty > temp_Qty)
		{
		    new frmOkPopUp(this, "Please Enter Valid Qty.", "error", 0).setVisible(true);
		    return;
		}
		double discount = funGetItemWiseDiscount(txtBillSearch.getText(), tempCode, temp_ItemName);
		frmSplitBill obj = mapItemCodeOriginalQty.get(tempCode);
		funFillListForManualSplit(list_Grid3_SplitedItems, temp_ItemName, selectedQty, ((obj.cls_Amt / obj.cls_qty) * selectedQty), tempCode, ((obj.discountAmt / obj.cls_qty) * selectedQty), obj.cls_Rate);
		if (funIsModifierPresent(billNo, tempCode))
		{
		    count = funAddModifier(list_Grid3_SplitedItems);
		    flagModifier = true;
		}
		tblTable3.setModel(dm_tblTable3);
		funRefreshGrids(dm_tblTable3);
		funRemoveItems(flagModifier, count);
	    }
	}
	catch (Exception e)
	{
	    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
	    //e.printStackTrace();
	}
    }

    private void funFillTable4ForManualSplit()
    {
	try
	{
	    int count = 0;
	    boolean flagModifier = false;
	    DefaultTableModel dm_tblTable4 = (DefaultTableModel) tblTable4.getModel();
	    String temp_ItemName = tblItemTable.getValueAt(selecedRow, 0).toString();
	    if (!temp_ItemName.startsWith("-->"))
	    {
		Double temp_Qty = Double.valueOf(tblItemTable.getValueAt(selecedRow, 1).toString());
		Double temp_Amt = Double.valueOf(tblItemTable.getValueAt(selecedRow, 2).toString());
		String tempCode = tblItemTable.getValueAt(selecedRow, 3).toString();
		if (temp_Qty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(null, true, "qty", temp_Qty);
		    num.setVisible(true);
		    //selectedQty = num.getResult();
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			clsGlobalVarClass.gNumerickeyboardValue = null;
		    }
		}
		else
		{
		    selectedQty = 1;
		}
		if (selectedQty <= 0 || selectedQty > temp_Qty)
		{
		    new frmOkPopUp(this, "Please Enter Valid Qty.", "error", 0).setVisible(true);
		    return;
		}
		double discount = funGetItemWiseDiscount(txtBillSearch.getText(), tempCode, temp_ItemName);
		frmSplitBill obj = mapItemCodeOriginalQty.get(tempCode);
		funFillListForManualSplit(list_Grid4_SplitedItems, temp_ItemName, selectedQty, ((obj.cls_Amt / obj.cls_qty) * selectedQty), tempCode, ((obj.discountAmt / obj.cls_qty) * selectedQty), obj.cls_Rate);
		if (funIsModifierPresent(billNo, tempCode))
		{
		    count = funAddModifier(list_Grid4_SplitedItems);
		    flagModifier = true;
		}
		tblTable4.setModel(dm_tblTable4);
		funRefreshGrids(dm_tblTable4);
		funRemoveItems(flagModifier, count);
	    }
	}
	catch (Exception e)
	{
	    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
	    e.printStackTrace();
	}
    }

    private void funSetSelectedTable(JScrollPane jScrollPane)
    {
	try
	{
	    if (jScrollPane.isVisible())
	    {
		String ScrollPaneName = jScrollPane.getName();
		switch (ScrollPaneName)
		{
		    case "scrBillSplitDtl1":
			selectedTable = "Table1";
			jScrollPane.setBorder(BorderFactory.createBevelBorder(1, Color.RED, Color.RED));//SELECTED TABLE
			scrBillSplitDtl2.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl3.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl4.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			break;
		    case "scrBillSplitDtl2":
			selectedTable = "Table2";
			jScrollPane.setBorder(BorderFactory.createBevelBorder(1, Color.RED, Color.RED));//SELECTED TABLE
			scrBillSplitDtl1.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl3.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl4.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			break;
		    case "scrBillSplitDtl3":
			selectedTable = "Table3";
			jScrollPane.setBorder(BorderFactory.createBevelBorder(1, Color.RED, Color.RED));//SELECTED TABLE
			scrBillSplitDtl1.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl2.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl4.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			break;
		    case "scrBillSplitDtl4":
			selectedTable = "Table4";
			jScrollPane.setBorder(BorderFactory.createBevelBorder(1, Color.RED, Color.RED));//SELECTED TABLE
			scrBillSplitDtl1.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl2.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			scrBillSplitDtl3.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
			break;
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillQtyCombo()
    {
	if (cmbSplitType.getSelectedItem().equals("Equal Split"))
	{
	    cmbSplitQty.removeAllItems();
	    cmbSplitQty.addItem("---Select---");
	    for (int i = 2; i <= 100; i++)
	    {
		cmbSplitQty.addItem(i);
	    }
	}
	else if (cmbSplitType.getSelectedItem().equals("Item Wise"))
	{
	    cmbSplitQty.removeAllItems();
	    cmbSplitQty.addItem("---Select---");
	    for (int i = 2; i <= itemWiseSplitQty.length - 1; i++)
	    {
		cmbSplitQty.addItem(itemWiseSplitQty[i]);
	    }
	}
    }

    /**
     * Item Wise or manual split,
     * This split type will allow user to select items and add it to selected grid. 
     * Then this slit type will split bill according to grid items added.
     * 
     * This split type will split a bill into only either 2 bills,3 bills or 4 bills.
     */    
    private void funManualSplit()
    {
	try
	{
	    if (cmbSplitQty.getSelectedIndex() == 0)
	    {
		new frmOkPopUp(null, "Please select split quantity.", "Error", 1).setVisible(true);
	    }
	    else
	    {
		if (tblItemTable.getRowCount() == clsGlobalVarClass.gtblRowcount) //==0
		{
		    String noOfSplit = cmbSplitQty.getSelectedItem().toString();
		    switch (noOfSplit)
		    {
			case "1":
			    //fun_insertData(tblTable1);
			    break;
			case "2":
			    funInsertDataForManualSplit(list_Grid1_SplitedItems, list_Grid2_SplitedItems);
			    break;
			case "3":
			    funInsertDataForManualSplit(list_Grid1_SplitedItems, list_Grid2_SplitedItems, list_Grid3_SplitedItems);
			    break;
			case "4":
			    funInsertDataForManualSplit(list_Grid1_SplitedItems, list_Grid2_SplitedItems, list_Grid3_SplitedItems, list_Grid4_SplitedItems);
			    break;
		    }
		}
		else
		{
		    new frmOkPopUp(null, "Please Insert Remainnig Items in Table", "Error", 1).setVisible(true);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertDataForManualSplit(List<frmSplitBill> list_Grid1_SplitedItems, List<frmSplitBill> list_Grid2_SplitedItems)
    {
	try
	{
	    clsUtility obj = new clsUtility();

	    double temp_SubTotal = 0.00;
	    double totalDiscountAmt = 0.00;
	    if (tblTable1.getRowCount() > 0)
	    {
		if (tblTable2.getRowCount() > 0)
		{
		    Map hmBillTax = new HashMap<String, String>();
		    String bill = txtBillSearch.getText().trim();
		    String nwBillNo = bill.concat("-") + 1;
		    // Code to insert billtaxdtl table with new billno
		    for (Map.Entry<String, ArrayList<ArrayList>> entry : hmTaxOnBill.entrySet())
		    {
			double totalTempTaxableAmt = 0, totalTempTaxAmt1 = 0;
			String billIndex = entry.getKey().substring(5, entry.getKey().length());
			String newBillNo = bill.concat("-") + billIndex;
			ArrayList<ArrayList> arrList = (ArrayList) entry.getValue();
			for (int cnt = 0; cnt < arrList.size(); cnt++)
			{
			    ArrayList<String> arrListTemp = (ArrayList<String>) arrList.get(cnt);
			    String taxCode = arrListTemp.get(0).toString();
			    double taxableAmt = Double.parseDouble(arrListTemp.get(1).toString());
			    double taxAmt = Double.parseDouble(arrListTemp.get(2).toString());
			    totalTempTaxableAmt += taxableAmt;
			    if (arrListTemp.get(3).toString().equalsIgnoreCase("Forward"))
			    {
				totalTempTaxAmt1 += taxAmt;
			    }
			    funInsertBillTaxDtlForManualSplit(newBillNo, taxCode, taxableAmt, taxAmt);
			}
			hmBillTax.put(entry.getKey(), totalTempTaxableAmt + "#" + totalTempTaxAmt1);
		    }
		    // Code to delete billtaxdtl table for old billno
		    funDeleteBillTaxDtlForManualSplit(bill);
		    List<clsBillDtl> listOfBillItems = new LinkedList<>();
		    List<frmSplitBill> listOfBillModifiers = new LinkedList<>();
		    totalDiscountAmt = 0.00;
		    temp_SubTotal = 0.00;
		    for (frmSplitBill objSplitedItem : list_Grid1_SplitedItems)
		    {
			String splitedItemCode = objSplitedItem.cls_ItemCode;
			String splitedItemName = objSplitedItem.cls_ItemName;
			double splitedItemQty = objSplitedItem.cls_qty;
			double splitedItemAmt = objSplitedItem.cls_Amt;
			double splitedItemDiscAmt = objSplitedItem.discountAmt;
			double splitedItemRate = objSplitedItem.cls_Rate;
			temp_SubTotal += splitedItemAmt;
			totalDiscountAmt += splitedItemDiscAmt;
			if (splitedItemCode.contains("M"))
			{
			    listOfBillModifiers.add(objSplitedItem);
			}
			else
			{
			    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
			    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
			    while (kotItemIterator.hasNext())
			    {
				clsBillDtl objKOTItem = kotItemIterator.next();
				if (splitedItemQty == objKOTItem.getDblQuantity())
				{
				    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
				    objBillItem.setDblQuantity(splitedItemQty);
				    objBillItem.setDblAmount(splitedItemAmt);
				    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
				    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

				    listOfBillItems.add(objBillItem);

				    kotItemIterator.remove();

				    splitedItemQty = 0;
				}
				else if (splitedItemQty < objKOTItem.getDblQuantity())
				{
				    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
				    objBillItem.setDblQuantity(splitedItemQty);
				    objBillItem.setDblAmount(splitedItemAmt);
				    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
				    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
				    listOfBillItems.add(objBillItem);
				    //update kot item
				    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
				    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
				    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
				    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
				    objKOTItem.setDblQuantity(remainingQty);
				    splitedItemQty = 0;
				}
				else
				{
				    listOfBillItems.add(objKOTItem);
				    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
				    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

				    kotItemIterator.remove();
				}
				if (splitedItemQty <= 0)
				{
				    break;
				}
			    }
			}
		    }
		    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
		    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table1");
		    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid1_SplitedItems);

//                    List<clsBillDtl> listOfItems = new ArrayList<>();
//                    listOfItems.addAll(listOfBillItems);
//                    for (int i = 0; i < listOfBillModifiers.size(); i++)
//                    {
//                        clsBillDtl objItemDtl = new clsBillDtl();
//
//                        objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                        objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                        objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                        objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                        listOfItems.add(objItemDtl);
//                    }
//                    obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);
		    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		    // Code to insert billhd table
		    String billTaxDtl = hmBillTax.get("Table1").toString();
		    double totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
		    double totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
		    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
		    temp_SubTotal = 0.00;
		    //======================Ritesh 05 SEPT 2014=============================//
		    nwBillNo = bill.concat("-") + 2;
		    listOfBillItems.clear();
		    listOfBillModifiers.clear();
		    totalDiscountAmt = 0.00;
		    temp_SubTotal = 0.00;
		    for (frmSplitBill objSplitedItem : list_Grid2_SplitedItems)
		    {
			String splitedItemCode = objSplitedItem.cls_ItemCode;
			String splitedItemName = objSplitedItem.cls_ItemName;
			double splitedItemQty = objSplitedItem.cls_qty;
			double splitedItemAmt = objSplitedItem.cls_Amt;
			double splitedItemDiscAmt = objSplitedItem.discountAmt;
			temp_SubTotal += splitedItemAmt;
			totalDiscountAmt += splitedItemDiscAmt;
			if (splitedItemCode.contains("M"))
			{
			    listOfBillModifiers.add(objSplitedItem);
			}
			else
			{
			    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
			    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
			    while (kotItemIterator.hasNext())
			    {
				clsBillDtl objKOTItem = kotItemIterator.next();
				if (splitedItemQty == objKOTItem.getDblQuantity())
				{
				    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
				    objBillItem.setDblQuantity(splitedItemQty);
				    objBillItem.setDblAmount(splitedItemAmt);
				    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
				    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

				    listOfBillItems.add(objBillItem);

				    kotItemIterator.remove();

				    splitedItemQty = 0;
				}
				else if (splitedItemQty < objKOTItem.getDblQuantity())
				{
				    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
				    objBillItem.setDblQuantity(splitedItemQty);
				    objBillItem.setDblAmount(splitedItemAmt);
				    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
				    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
				    listOfBillItems.add(objBillItem);
				    //update kot item
				    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
				    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
				    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
				    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
				    objKOTItem.setDblQuantity(remainingQty);
				    splitedItemQty = 0;
				}
				else
				{
				    listOfBillItems.add(objKOTItem);
				    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
				    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

				    kotItemIterator.remove();
				}
				if (splitedItemQty <= 0)
				{
				    break;
				}
			    }
			}
		    }
		    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
		    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table2");
		    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid2_SplitedItems);
//                    listOfItems = new ArrayList<>();
//                    listOfItems.addAll(listOfBillItems);
//                    for (int i = 0; i < listOfBillModifiers.size(); i++)
//                    {
//                        clsBillDtl objItemDtl = new clsBillDtl();
//
//                        objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                        objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                        objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                        objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                        listOfItems.add(objItemDtl);
//                    }
//                    obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

		    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		    billTaxDtl = hmBillTax.get("Table2").toString();
		    totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
		    totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
		    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
		    temp_SubTotal = 0.00;
		    funDeleteBillHdForManualSplit(bill);//to delete data from billhd:Ritesh                    
		}
		else
		{
		    new frmOkPopUp(null, "Please Fill Items in Grid 2", "Error", 1).setVisible(true);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please Fill Items in Grid 1", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertDataForManualSplit(List<frmSplitBill> list_Grid1_SplitedItems, List<frmSplitBill> list_Grid2_SplitedItems, List<frmSplitBill> list_Grid3_SplitedItems)
    {
	try
	{
	    clsUtility obj = new clsUtility();

	    double temp_SubTotal = 0.00;
	    double totalDiscountAmt = 0.00;
	    if (tblTable1.getRowCount() > 0)
	    {
		if (tblTable2.getRowCount() > 0)
		{
		    if (tblTable3.getRowCount() > 0)
		    {
			String bill = txtBillSearch.getText().trim();
			// Code to insert billtaxdtl table with new billno
			Map hmBillTax = new HashMap<String, String>();
			for (Map.Entry<String, ArrayList<ArrayList>> entry : hmTaxOnBill.entrySet())
			{
			    double totalTempTaxableAmt = 0, totalTempTaxAmt1 = 0;
			    String billIndex = entry.getKey().substring(5, entry.getKey().length());
			    String newBillNo = bill.concat("-") + billIndex;
			    ArrayList<ArrayList> arrList = (ArrayList) entry.getValue();
			    for (int cnt = 0; cnt < arrList.size(); cnt++)
			    {
				ArrayList<String> arrListTemp = (ArrayList<String>) arrList.get(cnt);
				String taxCode = arrListTemp.get(0).toString();
				double taxableAmt = Double.parseDouble(arrListTemp.get(1).toString());
				double taxAmt = Double.parseDouble(arrListTemp.get(2).toString());
				totalTempTaxableAmt += taxableAmt;
				if (arrListTemp.get(3).toString().equalsIgnoreCase("Forward"))
				{
				    totalTempTaxAmt1 += taxAmt;
				}
				funInsertBillTaxDtlForManualSplit(newBillNo, taxCode, taxableAmt, taxAmt);
			    }
			    hmBillTax.put(entry.getKey(), totalTempTaxableAmt + "#" + totalTempTaxAmt1);
			}
			// Code to delete billtaxdtl table for old billno
			funDeleteBillTaxDtlForManualSplit(bill);
			//===Grid 1===================Ritesh 05 SEPT 2014=============================//
			String nwBillNo = bill.concat("-") + 1;
			List<clsBillDtl> listOfBillItems = new LinkedList<>();
			List<frmSplitBill> listOfBillModifiers = new LinkedList<>();
			totalDiscountAmt = 0.00;
			temp_SubTotal = 0.00;
			for (frmSplitBill objSplitedItem : list_Grid1_SplitedItems)
			{
			    String splitedItemCode = objSplitedItem.cls_ItemCode;
			    String splitedItemName = objSplitedItem.cls_ItemName;
			    double splitedItemQty = objSplitedItem.cls_qty;
			    double splitedItemAmt = objSplitedItem.cls_Amt;
			    double splitedItemDiscAmt = objSplitedItem.discountAmt;
			    temp_SubTotal += splitedItemAmt;
			    totalDiscountAmt += splitedItemDiscAmt;
			    if (splitedItemCode.contains("M"))
			    {
				listOfBillModifiers.add(objSplitedItem);
			    }
			    else
			    {
				List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				while (kotItemIterator.hasNext())
				{
				    clsBillDtl objKOTItem = kotItemIterator.next();
				    if (splitedItemQty == objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					listOfBillItems.add(objBillItem);

					kotItemIterator.remove();

					splitedItemQty = 0;
				    }
				    else if (splitedItemQty < objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					listOfBillItems.add(objBillItem);
					//update kot item
					double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					objKOTItem.setDblQuantity(remainingQty);
					splitedItemQty = 0;
				    }
				    else
				    {
					listOfBillItems.add(objKOTItem);
					splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					kotItemIterator.remove();
				    }
				    if (splitedItemQty <= 0)
				    {
					break;
				    }
				}
			    }
			}
			funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table1");
			funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid1_SplitedItems);
//                        List<clsBillDtl> listOfItems = new ArrayList<>();
//                        listOfItems.addAll(listOfBillItems);
//                        for (int i = 0; i < listOfBillModifiers.size(); i++)
//                        {
//                            clsBillDtl objItemDtl = new clsBillDtl();
//
//                            objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                            objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                            objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                            objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                            listOfItems.add(objItemDtl);
//                        }
//                        obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			// Insert into tblbillhd for grid no 1    
			String billTaxDtl = hmBillTax.get("Table1").toString();
			double totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			double totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			temp_SubTotal = 0.00;
			//===Grid 2===================Ritesh 05 SEPT 2014=============================//
			nwBillNo = bill.concat("-") + 2;
			listOfBillItems.clear();;
			listOfBillModifiers.clear();;
			totalDiscountAmt = 0.00;
			temp_SubTotal = 0.00;
			for (frmSplitBill objSplitedItem : list_Grid2_SplitedItems)
			{
			    String splitedItemCode = objSplitedItem.cls_ItemCode;
			    String splitedItemName = objSplitedItem.cls_ItemName;
			    double splitedItemQty = objSplitedItem.cls_qty;
			    double splitedItemAmt = objSplitedItem.cls_Amt;
			    double splitedItemDiscAmt = objSplitedItem.discountAmt;
			    temp_SubTotal += splitedItemAmt;
			    totalDiscountAmt += splitedItemDiscAmt;
			    if (splitedItemCode.contains("M"))
			    {
				listOfBillModifiers.add(objSplitedItem);
			    }
			    else
			    {
				List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				while (kotItemIterator.hasNext())
				{
				    clsBillDtl objKOTItem = kotItemIterator.next();
				    if (splitedItemQty == objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					listOfBillItems.add(objBillItem);

					kotItemIterator.remove();

					splitedItemQty = 0;
				    }
				    else if (splitedItemQty < objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					listOfBillItems.add(objBillItem);
					//update kot item
					double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					objKOTItem.setDblQuantity(remainingQty);
					splitedItemQty = 0;
				    }
				    else
				    {
					listOfBillItems.add(objKOTItem);
					splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					kotItemIterator.remove();
				    }
				    if (splitedItemQty <= 0)
				    {
					break;
				    }
				}
			    }
			}
			funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table2");
			funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid2_SplitedItems);
//                        listOfItems = new ArrayList<>();
//                        listOfItems.addAll(listOfBillItems);
//                        for (int i = 0; i < listOfBillModifiers.size(); i++)
//                        {
//                            clsBillDtl objItemDtl = new clsBillDtl();
//
//                            objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                            objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                            objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                            objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                            listOfItems.add(objItemDtl);
//                        }
//                        obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			// Insert into tblbillhd for grid no 2    
			billTaxDtl = hmBillTax.get("Table2").toString();
			totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			temp_SubTotal = 0.00;
			//======Grid 3=================Ritesh 05 SEPT 2014============================//
			nwBillNo = bill.concat("-") + 3;
			listOfBillItems.clear();;
			listOfBillModifiers.clear();;
			totalDiscountAmt = 0.00;
			temp_SubTotal = 0.00;
			for (frmSplitBill objSplitedItem : list_Grid3_SplitedItems)
			{
			    String splitedItemCode = objSplitedItem.cls_ItemCode;
			    String splitedItemName = objSplitedItem.cls_ItemName;
			    double splitedItemQty = objSplitedItem.cls_qty;
			    double splitedItemAmt = objSplitedItem.cls_Amt;
			    double splitedItemDiscAmt = objSplitedItem.discountAmt;
			    temp_SubTotal += splitedItemAmt;
			    totalDiscountAmt += splitedItemDiscAmt;
			    if (splitedItemCode.contains("M"))
			    {
				listOfBillModifiers.add(objSplitedItem);
			    }
			    else
			    {
				List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				while (kotItemIterator.hasNext())
				{
				    clsBillDtl objKOTItem = kotItemIterator.next();
				    if (splitedItemQty == objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					listOfBillItems.add(objBillItem);

					kotItemIterator.remove();

					splitedItemQty = 0;
				    }
				    else if (splitedItemQty < objKOTItem.getDblQuantity())
				    {
					clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					objBillItem.setDblQuantity(splitedItemQty);
					objBillItem.setDblAmount(splitedItemAmt);
					objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					listOfBillItems.add(objBillItem);
					//update kot item
					double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					objKOTItem.setDblQuantity(remainingQty);
					splitedItemQty = 0;
				    }
				    else
				    {
					listOfBillItems.add(objKOTItem);
					splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					kotItemIterator.remove();
				    }
				    if (splitedItemQty <= 0)
				    {
					break;
				    }
				}
			    }
			}
			funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table3");
			funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid3_SplitedItems);
//                        listOfItems = new ArrayList<>();
//                        listOfItems.addAll(listOfBillItems);
//                        for (int i = 0; i < listOfBillModifiers.size(); i++)
//                        {
//                            clsBillDtl objItemDtl = new clsBillDtl();
//
//                            objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                            objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                            objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                            objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                            listOfItems.add(objItemDtl);
//                        }
//                        obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			// Insert into tblbillhd for grid no 3    
			billTaxDtl = hmBillTax.get("Table3").toString();
			totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			temp_SubTotal = 0.00;
			//======================Ritesh 05 SEPT 2014============================//
			funDeleteBillHdForManualSplit(bill);//to delete data from billhd:Ritesh
		    }
		    else
		    {
			new frmOkPopUp(null, "Please Fill Items in Grid 3", "Error", 1).setVisible(true);
		    }
		}
		else
		{
		    new frmOkPopUp(null, "Please Fill Items in Grid 2", "Error", 1).setVisible(true);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please Fill Items in Grid 1", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertDataForManualSplit(List<frmSplitBill> list_Grid1_SplitedItems, List<frmSplitBill> list_Grid2_SplitedItems, List<frmSplitBill> list_Grid3_SplitedItems, List<frmSplitBill> list_Grid4_SplitedItems)
    {
	try
	{
	    clsUtility obj = new clsUtility();

	    double temp_SubTotal = 0.00;
	    double totalDiscountAmt = 0.00;
	    if (tblTable1.getRowCount() > 0)
	    {
		if (tblTable2.getRowCount() > 0)
		{
		    if (tblTable3.getRowCount() > 0)
		    {
			if (tblTable4.getRowCount() > 0)
			{
			    String bill = txtBillSearch.getText().trim();
			    // Code to insert billtaxdtl table with new billno
			    Map hmBillTax = new HashMap<String, String>();
			    for (Map.Entry<String, ArrayList<ArrayList>> entry : hmTaxOnBill.entrySet())
			    {
				double totalTempTaxableAmt = 0, totalTempTaxAmt1 = 0;
				String billIndex = entry.getKey().substring(5, entry.getKey().length());
				String newBillNo = bill.concat("-") + billIndex;
				ArrayList<ArrayList> arrList = (ArrayList) entry.getValue();
				for (int cnt = 0; cnt < arrList.size(); cnt++)
				{
				    ArrayList<String> arrListTemp = (ArrayList<String>) arrList.get(cnt);
				    String taxCode = arrListTemp.get(0).toString();
				    double taxableAmt = Double.parseDouble(arrListTemp.get(1).toString());
				    double taxAmt = Double.parseDouble(arrListTemp.get(2).toString());
				    totalTempTaxableAmt += taxableAmt;
				    if (arrListTemp.get(3).toString().equalsIgnoreCase("Forward"))
				    {
					totalTempTaxAmt1 += taxAmt;
				    }
				    funInsertBillTaxDtlForManualSplit(newBillNo, taxCode, taxableAmt, taxAmt);
				}
				hmBillTax.put(entry.getKey(), totalTempTaxableAmt + "#" + totalTempTaxAmt1);
			    }
			    // Code to delete billtaxdtl table for old billno
			    funDeleteBillTaxDtlForManualSplit(bill);
			    //===Grid 1===================Ritesh 05 SEPT 2014=============================//
			    String nwBillNo = bill.concat("-") + 1;
			    List<clsBillDtl> listOfBillItems = new LinkedList<>();
			    List<frmSplitBill> listOfBillModifiers = new LinkedList<>();
			    totalDiscountAmt = 0.00;
			    temp_SubTotal = 0.00;
			    for (frmSplitBill objSplitedItem : list_Grid1_SplitedItems)
			    {
				String splitedItemCode = objSplitedItem.cls_ItemCode;
				String splitedItemName = objSplitedItem.cls_ItemName;
				double splitedItemQty = objSplitedItem.cls_qty;
				double splitedItemAmt = objSplitedItem.cls_Amt;
				double splitedItemDiscAmt = objSplitedItem.discountAmt;
				temp_SubTotal += splitedItemAmt;
				totalDiscountAmt += splitedItemDiscAmt;
				if (splitedItemCode.contains("M"))
				{
				    listOfBillModifiers.add(objSplitedItem);
				}
				else
				{
				    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				    while (kotItemIterator.hasNext())
				    {
					clsBillDtl objKOTItem = kotItemIterator.next();
					if (splitedItemQty == objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					    listOfBillItems.add(objBillItem);

					    kotItemIterator.remove();

					    splitedItemQty = 0;
					}
					else if (splitedItemQty < objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					    listOfBillItems.add(objBillItem);
					    //update kot item
					    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					    objKOTItem.setDblQuantity(remainingQty);
					    splitedItemQty = 0;
					}
					else
					{
					    listOfBillItems.add(objKOTItem);
					    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					    kotItemIterator.remove();
					}
					if (splitedItemQty <= 0)
					{
					    break;
					}
				    }
				}
			    }
			    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table1");
			    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid1_SplitedItems);
//                            List<clsBillDtl> listOfItems = new ArrayList<>();
//                            listOfItems.addAll(listOfBillItems);
//                            for (int i = 0; i < listOfBillModifiers.size(); i++)
//                            {
//                                clsBillDtl objItemDtl = new clsBillDtl();
//
//                                objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                                objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                                objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                                objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                                listOfItems.add(objItemDtl);
//                            }
//                            obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			    // Insert into tblbillhd for grid no 1    
			    String billTaxDtl = hmBillTax.get("Table1").toString();
			    double totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			    double totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			    temp_SubTotal = 0.00;
			    //===Grid 2===================Ritesh 05 SEPT 2014=============================//
			    nwBillNo = bill.concat("-") + 2;
			    listOfBillItems.clear();
			    listOfBillModifiers.clear();
			    totalDiscountAmt = 0.00;
			    temp_SubTotal = 0.00;
			    for (frmSplitBill objSplitedItem : list_Grid2_SplitedItems)
			    {
				String splitedItemCode = objSplitedItem.cls_ItemCode;
				String splitedItemName = objSplitedItem.cls_ItemName;
				double splitedItemQty = objSplitedItem.cls_qty;
				double splitedItemAmt = objSplitedItem.cls_Amt;
				double splitedItemDiscAmt = objSplitedItem.discountAmt;
				temp_SubTotal += splitedItemAmt;
				totalDiscountAmt += splitedItemDiscAmt;
				if (splitedItemCode.contains("M"))
				{
				    listOfBillModifiers.add(objSplitedItem);
				}
				else
				{
				    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				    while (kotItemIterator.hasNext())
				    {
					clsBillDtl objKOTItem = kotItemIterator.next();

					if (splitedItemQty == objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					    listOfBillItems.add(objBillItem);

					    kotItemIterator.remove();

					    splitedItemQty = 0;
					}
					else if (splitedItemQty < objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					    listOfBillItems.add(objBillItem);
					    //update kot item
					    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;

					    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					    objKOTItem.setDblQuantity(remainingQty);

					    splitedItemQty = 0;
					}
					else
					{
					    listOfBillItems.add(objKOTItem);
					    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					    kotItemIterator.remove();
					}
					if (splitedItemQty <= 0)
					{
					    break;
					}
				    }
				}
			    }
			    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table2");
			    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid2_SplitedItems);
//                            listOfItems = new ArrayList<>();
//                            listOfItems.addAll(listOfBillItems);
//                            for (int i = 0; i < listOfBillModifiers.size(); i++)
//                            {
//                                clsBillDtl objItemDtl = new clsBillDtl();
//
//                                objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                                objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                                objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                                objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                                listOfItems.add(objItemDtl);
//                            }
//                            obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			    // Insert into tblbillhd for grid no 2
			    billTaxDtl = hmBillTax.get("Table2").toString();
			    totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			    totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			    temp_SubTotal = 0.00;
			    //======Grid 3=================Ritesh 05 SEPT 2014============================//
			    nwBillNo = bill.concat("-") + 3;
			    listOfBillItems.clear();
			    listOfBillModifiers.clear();
			    totalDiscountAmt = 0.00;
			    temp_SubTotal = 0.00;
			    for (frmSplitBill objSplitedItem : list_Grid3_SplitedItems)
			    {
				String splitedItemCode = objSplitedItem.cls_ItemCode;
				String splitedItemName = objSplitedItem.cls_ItemName;
				double splitedItemQty = objSplitedItem.cls_qty;
				double splitedItemAmt = objSplitedItem.cls_Amt;
				double splitedItemDiscAmt = objSplitedItem.discountAmt;
				temp_SubTotal += splitedItemAmt;
				totalDiscountAmt += splitedItemDiscAmt;
				if (splitedItemCode.contains("M"))
				{
				    listOfBillModifiers.add(objSplitedItem);
				}
				else
				{
				    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				    while (kotItemIterator.hasNext())
				    {
					clsBillDtl objKOTItem = kotItemIterator.next();
					if (splitedItemQty == objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					    listOfBillItems.add(objBillItem);

					    kotItemIterator.remove();

					    splitedItemQty = 0;
					}
					else if (splitedItemQty < objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					    listOfBillItems.add(objBillItem);
					    //update kot item
					    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					    objKOTItem.setDblQuantity(remainingQty);
					    splitedItemQty = 0;
					}
					else
					{
					    listOfBillItems.add(objKOTItem);
					    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					    kotItemIterator.remove();
					}
					if (splitedItemQty <= 0)
					{
					    break;
					}
				    }
				}
			    }
			    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table3");
			    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid3_SplitedItems);
//                            listOfItems = new ArrayList<>();
//                            listOfItems.addAll(listOfBillItems);
//                            for (int i = 0; i < listOfBillModifiers.size(); i++)
//                            {
//                                clsBillDtl objItemDtl = new clsBillDtl();
//
//                                objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                                objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                                objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                                objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                                listOfItems.add(objItemDtl);
//                            }
//                            obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			    // Insert into tblbillhd for grid no 2
			    billTaxDtl = hmBillTax.get("Table3").toString();
			    totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			    totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			    temp_SubTotal = 0.00;
			    //=======Grid 4===============Ritesh 05 SEPT 2014============================//
			    nwBillNo = bill.concat("-") + 4;
			    listOfBillItems.clear();
			    listOfBillModifiers.clear();
			    totalDiscountAmt = 0.00;
			    temp_SubTotal = 0.00;
			    for (frmSplitBill objSplitedItem : list_Grid4_SplitedItems)
			    {
				String splitedItemCode = objSplitedItem.cls_ItemCode;
				String splitedItemName = objSplitedItem.cls_ItemName;
				double splitedItemQty = objSplitedItem.cls_qty;
				double splitedItemAmt = objSplitedItem.cls_Amt;
				double splitedItemDiscAmt = objSplitedItem.discountAmt;
				temp_SubTotal += splitedItemAmt;
				totalDiscountAmt += splitedItemDiscAmt;
				if (splitedItemCode.contains("M"))
				{
				    listOfBillModifiers.add(objSplitedItem);
				}
				else
				{
				    List<clsBillDtl> listOfKOTWiseItems = mapKOTWiseItemList.get(splitedItemCode);
				    Iterator<clsBillDtl> kotItemIterator = listOfKOTWiseItems.iterator();
				    while (kotItemIterator.hasNext())
				    {
					clsBillDtl objKOTItem = kotItemIterator.next();
					if (splitedItemQty == objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());

					    listOfBillItems.add(objBillItem);

					    kotItemIterator.remove();

					    splitedItemQty = 0;
					}
					else if (splitedItemQty < objKOTItem.getDblQuantity())
					{
					    clsBillDtl objBillItem = (clsBillDtl) objKOTItem.clone();
					    objBillItem.setDblQuantity(splitedItemQty);
					    objBillItem.setDblAmount(splitedItemAmt);
					    objBillItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * splitedItemQty);
					    objBillItem.setDblDiscountAmt((objBillItem.getDblDiscountPer() / 100) * objBillItem.getDblAmount());
					    listOfBillItems.add(objBillItem);
					    //update kot item
					    double remainingQty = objKOTItem.getDblQuantity() - splitedItemQty;
					    objKOTItem.setDblAmount((objKOTItem.getDblAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblTaxAmount((objKOTItem.getDblTaxAmount() / objKOTItem.getDblQuantity()) * remainingQty);
					    objKOTItem.setDblDiscountAmt((objKOTItem.getDblDiscountPer() / 100) * objKOTItem.getDblAmount());
					    objKOTItem.setDblQuantity(remainingQty);
					    splitedItemQty = 0;
					}
					else
					{
					    listOfBillItems.add(objKOTItem);
					    splitedItemAmt = splitedItemAmt - objKOTItem.getDblAmount();
					    splitedItemQty = splitedItemQty - objKOTItem.getDblQuantity();

					    kotItemIterator.remove();
					}
					if (splitedItemQty <= 0)
					{
					    break;
					}
				    }
				}
			    }
			    funInsertBillDtlForManualSplit(bill, nwBillNo, listOfBillItems, listOfBillModifiers);
			    funInsertBillPromotionDtlForManualSplit(bill, nwBillNo, listOfBillItems, "Table4");
			    funInsertBillDiscDtlForManualBillSplit(nwBillNo, bill, list_Grid4_SplitedItems);
//                            listOfItems = new ArrayList<>();
//                            listOfItems.addAll(listOfBillItems);
//                            for (int i = 0; i < listOfBillModifiers.size(); i++)
//                            {
//                                clsBillDtl objItemDtl = new clsBillDtl();
//
//                                objItemDtl.setStrItemCode(listOfBillModifiers.get(i).getCls_ItemCode());
//                                objItemDtl.setStrItemName(listOfBillModifiers.get(i).getCls_ItemName());
//                                objItemDtl.setDblAmount(listOfBillModifiers.get(i).getCls_Amt());
//                                objItemDtl.setDblDiscountAmt(listOfBillModifiers.get(i).discountAmt);
//
//                                listOfItems.add(objItemDtl);
//                            }
//                            obj.funReCalculateDiscountForBill("SplitBill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, bill, nwBillNo, listOfItems);

			    obj.funUpdateBillDtlWithTaxValues(nwBillNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			    // Insert into tblbillhd for grid no 2
			    billTaxDtl = hmBillTax.get("Table4").toString();
			    totalTaxableAmt = Double.parseDouble(billTaxDtl.split("#")[0]);
			    totalTaxAmt1 = Double.parseDouble(billTaxDtl.split("#")[1]);
			    funInsertBillHdForManualSplit(bill, nwBillNo, temp_SubTotal, totalTaxableAmt, totalTaxAmt1, totalDiscountAmt);
			    funDeleteBillHdForManualSplit(bill);
			    temp_SubTotal = 0.00;
			}
			else
			{
			    new frmOkPopUp(null, "Please Fill Items in Grid 4", "Error", 1).setVisible(true);
			}
		    }
		    else
		    {
			new frmOkPopUp(null, "Please Fill Items in Grid 3", "Error", 1).setVisible(true);
		    }
		}
		else
		{
		    new frmOkPopUp(null, "Please Fill Items in Grid 2", "Error", 1).setVisible(true);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please Fill Items in Grid 1", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertBillDtlForManualSplit(String nwBillNo, String oldBillNo, String temp_itemCode, frmSplitBill objItemDtl)
    {
	try
	{
	    String sql_Insert = "insert into tblbilldtl(strItemCode,strItemName,strBillNo,strAdvBookingNo,dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBillDate,"
		    + " strKOTNo,strClientCode,strCustomerCode,tmeOrderProcessing,"
		    + " strDataPostFlag,strMMSDataPostFlag,strManualKOTNo,tdhYN,dblDiscountAmt,dblDiscountPer,dtBillDate)"
		    + " (select strItemCode,strItemName,'" + nwBillNo + "',strAdvBookingNo,dblRate,'" + objItemDtl.cls_qty + "',"
		    + " '" + objItemDtl.cls_Amt + "',dblTaxAmount,dteBillDate,strKOTNo,"
		    + " strClientCode,strCustomerCode,"
		    + " tmeOrderProcessing,strDataPostFlag,strMMSDataPostFlag,strManualKOTNo,tdhYN,"
		    + " '" + objItemDtl.discountAmt + "',dblDiscountPer,'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " from tblbilldtl "
		    + " where strBillNo='" + oldBillNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and  strItemCode='" + temp_itemCode + "' "
		    + " group by strItemCode ) ";
	    clsGlobalVarClass.dbMysql.execute(sql_Insert);
	    clsGlobalVarClass.dbMysql.execute("update tblbillpromotiondtl set strBillNo='" + nwBillNo + "' "
		    + " where strBillNo='" + oldBillNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and strItemCode='" + temp_itemCode + "' ");
//              clsGlobalVarClass.dbMysql.execute("INSERT INTO tblbillpromotiondtl (strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount)"
//                            +"(select '"+nwBillNo+"',strItemCode,strPromotionCode,dblQuantity,dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount "
//                            +" from tblbillpromotiondtl where strBillNo='"+oldBillNo+"' and strItemCode='"+temp_itemCode+"') ");
	    clsGlobalVarClass.dbMysql.execute("delete from  tblbillpromotiondtl "
		    + " where strBillNo='" + oldBillNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and strItemCode='" + temp_itemCode + "' ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertBillHdForManualSplit(String oldBill, String nwBillNo, double subTotal, double totalTaxableAmt, double totalTaxAmt, double totalDiscountAmt)
    {
	try
	{
	    double totalDiscountPer = (totalDiscountAmt / subTotal) * 100;
//            sql = "select dblDiscountPer from tblbillhd where strBillNo='" + oldBill + "'";
//            ResultSet rsBillDiscPer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            if (rsBillDiscPer.next())
//            {
//                discountPer = rsBillDiscPer.getDouble(1);
//            }
	    //double discountAmt = (discountPer / 100) * originalDiscAmt;
	    subTotal = Math.rint(subTotal);
	    double grandTotal = (subTotal - totalDiscountAmt) + totalTaxAmt;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
	    grandTotal = mapRoundOff.get("roundOffAmt");
	    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt

	    String sqlInsertDatabillhd = "insert into tblbillhd"
		    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode"
		    + ",dblDiscountAmt,dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal"
		    + ",strTakeAway,strOperationType,strUserCreated,strUserEdited"
		    + ",dteDateCreated,dteDateEdited,strClientCode,strTableNo,strWaiterNo"
		    + ",strCustomerCode,strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag"
		    + ",strReasonCode,strRemarks,dblTipAmount,dteSettleDate,strCounterCode"
		    + ",dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,dblRoundOff,strTransactionType,dtBillDate,intOrderNo,dblUSDConverionRate ) "
		    + " (select '" + nwBillNo + "',strAdvBookingNo,dteBillDate,strPOSCode"
		    + ",strSettelmentMode,'" + totalDiscountAmt + "','" + totalDiscountPer + "'"
		    + ",'" + totalTaxAmt + "','" + subTotal + "','" + grandTotal + "',strTakeAway"
		    + ",strOperationType,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strClientCode,strTableNo,strWaiterNo,strCustomerCode"
		    + ",strManualBillNo,intShiftCode,intPaxNo,strDataPostFlag,strReasonCode"
		    + ",strRemarks,dblTipAmount,'" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
		    + ",strCounterCode,dblDeliveryCharges,strCouponCode,strAreaCode,strDiscountOn,'" + grandTotalRoundOffBy + "' "
		    + ",CONCAT(strTransactionType,',','SplitBill'),'" + clsGlobalVarClass.getPOSDateForTransaction() + "',intOrderNo,dblUSDConverionRate "
		    + " from tblbillhd where strBillNo='" + oldBill + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlInsertDatabillhd);
//            if (totalDiscountAmt > 0)
//            {
//                double discOnAmount = subTotal;
//                double discPercent = (totalDiscountAmt * 100) / subTotal;
//
//                String sqlBillDiscDtl = "insert into tblbilldiscdtl "
//                        + "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue "
//                        + ",strDiscReasonCode,strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
//                        + ",strClientCode,strDataPostFlag) "
//                        + "(select '" + nwBillNo + "',strPOSCode,'" + totalDiscountAmt + "','" + discPercent + "','" + discOnAmount + "',strDiscOnType,strDiscOnValue "
//                        + ",strDiscReasonCode,strDiscRemarks,strUserCreated,'" + clsGlobalVarClass.gUserCode + "',dteDateCreated,'" + clsGlobalVarClass.getCurrentDateTime() + "' "
//                        + ",strClientCode,strDataPostFlag "
//                        + " from tblbilldiscdtl where strBillNo='" + oldBill + "' "
//                        + ")";
//                clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl);
//            }
	    billsToBePrinted.put(nwBillNo, clsGlobalVarClass.getPOSDateForTransaction());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funDeleteBillHdForManualSplit(String oldBillNo)
    {
	try
	{
	    String POSCode = "";
	    sql = "select strPOSCode from tblbillhd where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsPOS.next())
	    {
		POSCode = rsPOS.getString(1);
	    }
	    rsPOS.close();
	    String sqlDeleteBillHd = "delete from tblbillhd where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ;";
	    clsGlobalVarClass.dbMysql.execute(sqlDeleteBillHd);
	    String sqlDeleteBillDtl = "delete from tblbilldtl where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'  ;";
	    clsGlobalVarClass.dbMysql.execute(sqlDeleteBillDtl);
	    String sqlDeleteBillModiDtl = "delete from tblbillmodifierdtl where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ;";
	    clsGlobalVarClass.dbMysql.execute(sqlDeleteBillModiDtl);
	    String sqlDeleteBillPromoDtl = "delete from tblbillpromotiondtl where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ;";
	    clsGlobalVarClass.dbMysql.execute(sqlDeleteBillPromoDtl);
	    new frmOkPopUp(null, "Bill Split Successfully", "Successfull", 1).setVisible(true);
	    funResetFields();
	    frmOkCancelPopUp okCancelPopUp = new frmOkCancelPopUp(null, "Do You Want To Print All Bills?");
	    okCancelPopUp.setVisible(true);
	    int result = okCancelPopUp.getResult();
	    if (result == 1)
	    {
		clsUtility utility = new clsUtility();
		Iterator<Entry<String, String>> it = billsToBePrinted.entrySet().iterator();
		while (it.hasNext())
		{
		    Entry<String, String> entry = it.next();
		    utility.funPrintBill(entry.getKey(), entry.getValue(), false, POSCode, "print");
		}
	    }
	    sql = "delete from tblbilldiscdtl where strBillNo='" + oldBillNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertBillTaxDtlForManualSplit(String newBillNo, String taxCode, double taxableAmt, double taxAmt) throws Exception
    {
	sql = "insert into tblbilltaxdtl values "
		+ "('" + newBillNo + "','" + taxCode + "','" + taxableAmt + "','" + taxAmt + "'"
		+ " ,'" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
	clsGlobalVarClass.dbMysql.execute(sql);
    }

    private void funDeleteBillTaxDtlForManualSplit(String oldBillNo) throws Exception
    {
	sql = "delete from tblbilltaxdtl where strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);
    }

    private void funFillTable1ForEqualSplit(JTable tblName)
    {
	try
	{
	    double subTotal = 0.00;
	    String finalAmount = "";
	    int splitQty = Integer.valueOf(cmbSplitQty.getSelectedItem().toString());
	    //System.out.println("splitQty=" + splitQty);
	    DefaultTableModel dm_tblTable1 = (DefaultTableModel) tblName.getModel();
	    String billNo = txtBillSearch.getText();
	    for (int i = 0; i < listItemName.size(); i++)
	    {
		String temp_ItemName = listItemName.get(i);
		double temp_Qty = listItemQTY.get(i) / splitQty;
		double temp_Amt = listActualAmountOfItems.get(i) / splitQty;
		String tempCode = listItemCode.get(i);
		subTotal += temp_Amt;
		Object ob[] =
		{
		    temp_ItemName, temp_Qty, temp_Amt, tempCode
		};
		dm_tblTable1.addRow(ob);
	    }
	    finalAmount = String.valueOf(subTotal);
	    double tempTotalTaxAmt = 0;
	    sql = "select b.strTaxDesc,a.dblTaxAmount "
		    + " from tblbilltaxdtl a,tbltaxhd b "
		    + " where a.strTaxCode=b.strTaxCode "
		    + " and a.strBillNo='" + voucherNo + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsBillTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillTaxDtl.next())
	    {
		tempTotalTaxAmt = tempTotalTaxAmt + (rsBillTaxDtl.getDouble(2) / splitQty);
		Object[] taxTotalRow =
		{
		    rsBillTaxDtl.getString(1), "", (rsBillTaxDtl.getDouble(2) / splitQty)
		};
		dm_tblTable1.addRow(taxTotalRow);
	    }
	    rsBillTaxDtl.close();
	    Object ob_blankRow[] =
	    {
		" ", "", "", ""
	    };
	    dm_tblTable1.addRow(ob_blankRow);
	    Object ob[] =
	    {
		"Sub Total ", "", subTotal, ""
	    };
	    dm_tblTable1.addRow(ob);
	    Object ob1[] =
	    {
		"Discount ", "", Double.parseDouble(lblDiscount.getText()) / splitQty, ""
	    };
	    dm_tblTable1.addRow(ob1);
	    Object ob2[] =
	    {
		"Grand Total ", "", ((subTotal + tempTotalTaxAmt) - (Double.parseDouble(lblDiscount.getText()) / splitQty)), ""
	    };
	    dm_tblTable1.addRow(ob2);
	    tblName.setModel(dm_tblTable1);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillGridForEqualSplit()
    {
	try
	{
	    int qtyEqualSplit = Integer.valueOf(cmbSplitQty.getSelectedItem().toString());
	    if (qtyEqualSplit > 4)
	    {
		qtyEqualSplit = 4;
	    }
	    switch (qtyEqualSplit)
	    {
		case 1:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(false);
		    scrBillSplitDtl3.setVisible(false);
		    scrBillSplitDtl4.setVisible(false);
		    btnMoveNext.setVisible(true);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
		case 2:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(false);
		    scrBillSplitDtl4.setVisible(false);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
		case 3:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(true);
		    scrBillSplitDtl4.setVisible(false);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
		case 4:
		    scrBillSplitDtl1.setVisible(true);
		    scrBillSplitDtl2.setVisible(true);
		    scrBillSplitDtl3.setVisible(true);
		    scrBillSplitDtl4.setVisible(true);
		    btnMovePrev.setVisible(false);
		    btnMoveNext.setVisible(true);
		    break;
	    }
	    switch (qtyEqualSplit)
	    {
		case 1:
		    funReseteOnlyRightSideGrids();
		    funFillTable1ForEqualSplit(tblTable1);
		    break;
		case 2:
		    funReseteOnlyRightSideGrids();
		    funFillTable1ForEqualSplit(tblTable1);
		    funFillTable1ForEqualSplit(tblTable2);
		    break;
		case 3:
		    funReseteOnlyRightSideGrids();
		    funFillTable1ForEqualSplit(tblTable1);
		    funFillTable1ForEqualSplit(tblTable2);
		    funFillTable1ForEqualSplit(tblTable3);
		    break;
		case 4:
		    funReseteOnlyRightSideGrids();
		    funFillTable1ForEqualSplit(tblTable1);
		    funFillTable1ForEqualSplit(tblTable2);
		    funFillTable1ForEqualSplit(tblTable3);
		    funFillTable1ForEqualSplit(tblTable4);
		    break;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funReseteOnlyRightSideGrids()
    {
	try
	{
	    DefaultTableModel dm1 = (DefaultTableModel) tblTable1.getModel();
	    dm1.setRowCount(0);
	    DefaultTableModel dm2 = (DefaultTableModel) tblTable2.getModel();
	    dm2.setRowCount(0);
	    DefaultTableModel dm3 = (DefaultTableModel) tblTable3.getModel();
	    dm3.setRowCount(0);
	    DefaultTableModel dm4 = (DefaultTableModel) tblTable4.getModel();
	    dm4.setRowCount(0);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funHideTables()
    {
	try
	{
	    funReseteOnlyRightSideGrids();
	    scrBillSplitDtl1.setVisible(false);
	    scrBillSplitDtl2.setVisible(false);
	    scrBillSplitDtl3.setVisible(false);
	    scrBillSplitDtl4.setVisible(false);
	    scrBillSplitDtl1.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
	    scrBillSplitDtl2.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
	    scrBillSplitDtl3.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
	    scrBillSplitDtl4.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.lightGray));
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean funIsModifierPresent(String oldBillNo, String temp_itemCode)
    {
	boolean flag = false;
	try
	{
	    String sqlModifier = "select * "
		    + " from tblbillmodifierdtl "
		    + " where strBillNo='" + oldBillNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and left(strItemCode,7)='" + temp_itemCode + "' ";
	    ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
	    if (rsModifier.next())
	    {
		sqlModifier = "select count(strItemCode) "
			+ " from tblbillmodifierdtl "
			+ " where strBillNo='" + oldBillNo + "' "
			+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'  "
			+ " and left(strItemCode,7)='" + temp_itemCode + "' ";
		rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
		if (rsModifier.next())
		{
		    flag = true;
		    no_Of_Items_withModifier = rsModifier.getInt(1);
		}
		rsModifier.close();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return flag;
    }

    private void funInsertBillModifierDtlForManualSplit(String nwBillNo, String oldBillNo, String tempItemCode, frmSplitBill objModiDtl)
    {
	try
	{
	    String sql_Insert = "INSERT INTO tblbillmodifierdtl (strBillNo,strItemCode,strModifierCode,strModifierName,dblRate,dblQuantity,dblAmount"
		    + ",strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
		    + " (select '" + nwBillNo + "',strItemCode,strModifierCode,strModifierName,dblRate,'" + objModiDtl.cls_qty + "','" + objModiDtl.cls_Amt + "'"
		    + " ,strClientCode,dblDiscPer,'" + objModiDtl.discountAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
		    + " from tblbillmodifierdtl "
		    + " where strBillNo='" + oldBillNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and strItemCode='" + objModiDtl.cls_ItemCode + "' "
		    + " and strModifierName='" + objModiDtl.cls_ItemName + "'  ) ";
	    clsGlobalVarClass.dbMysql.execute(sql_Insert);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillPrevOriginalTable()
    {
	try
	{
	    if (tblTable1.getRowCount() > 0)
	    {
		btnMovePrev.setEnabled(true);
		DefaultTableModel dm_tblTable1 = (DefaultTableModel) tblItemTable.getModel();
		String temp_ItemName = tblTable1.getValueAt(selecedRow, 0).toString();
		Double temp_Qty = Double.valueOf(tblTable1.getValueAt(selecedRow, 1).toString());
		Double temp_Amt = Double.valueOf(tblTable1.getValueAt(selecedRow, 2).toString());
		String tempCode = tblTable1.getValueAt(selecedRow, 3).toString();
		Object ob[] =
		{
		    temp_ItemName, temp_Qty, temp_Amt, tempCode
		};
		dm_tblTable1.addRow(ob);
		tblItemTable.setModel(dm_tblTable1);
		DefaultTableModel dm = (DefaultTableModel) tblTable1.getModel();
		dm.removeRow(tblTable1.getSelectedRow());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

///ritesh Date 04 Sept 2014
    private void funRemoveItems(boolean flagModifier, int count)
    {
	DefaultTableModel dm = (DefaultTableModel) tblItemTable.getModel();
	double itemQty = Double.parseDouble(tblItemTable.getValueAt(selecedRow, 1).toString());
	double itemAmt = Double.parseDouble(tblItemTable.getValueAt(selecedRow, 2).toString());
	String itemCode = tblItemTable.getValueAt(selecedRow, 3).toString();
	if (itemQty > 1)
	{
	    if (itemQty > selectedQty)
	    {
		tblItemTable.setValueAt((itemQty - selectedQty), selecedRow, 1);
		tblItemTable.setValueAt(((itemAmt / itemQty) * (itemQty - selectedQty)), selecedRow, 2);
		if (flagModifier)
		{
		    int x = selecedRow + count;
		    for (int y = x; y > selecedRow; y--)
		    {
			double modiItemQty = Double.parseDouble(tblItemTable.getValueAt(y, 1).toString());
			double modiItemAmt = Double.parseDouble(tblItemTable.getValueAt(y, 2).toString());
			String modiName = tblItemTable.getValueAt(y, 0).toString();
			String modiCode = tblItemTable.getValueAt(y, 3).toString();
			frmSplitBill objOriginalItem = mapItemCodeOriginalQty.get(itemCode);
			frmSplitBill objOriginalModifier = mapItemCodeOriginalQty.get(modiCode + "!" + modiName);
			double modifierQtyPerOneItemQty = objOriginalModifier.cls_qty / objOriginalItem.cls_qty;
			double modifierAmtPerOneItemQty = objOriginalModifier.cls_Amt / objOriginalItem.cls_qty;
			tblItemTable.setValueAt(modiItemQty - (selectedQty * modifierQtyPerOneItemQty), y, 1);
			tblItemTable.setValueAt(modiItemAmt - (modifierAmtPerOneItemQty * selectedQty), y, 2);
		    }
		}
	    }
	    else
	    {
		if (flagModifier)
		{
		    int x = selecedRow + count;
		    for (int y = x; y >= selecedRow; y--)
		    {
			dm.removeRow(y);
		    }
		}
		else
		{
		    dm.removeRow(selecedRow);
		}
	    }
	}
	else
	{
	    if (flagModifier)
	    {
		int x = selecedRow + count;
		for (int y = x; y >= selecedRow; y--)
		{
		    dm.removeRow(y);
		}
	    }
	    else
	    {
		dm.removeRow(selecedRow);
	    }
	}
	tblItemTable.revalidate();
	no_Of_Items_withModifier = 0;
	selecedRow = -1;
    }

    private int funAddModifier(List<frmSplitBill> list_Grids)
    {
	int count = 0;
	String tempCode = tblItemTable.getValueAt(selecedRow, 3).toString();
	for (int i = 1; i <= no_Of_Items_withModifier; i++)
	{
	    String Mtemp_ItemName = tblItemTable.getValueAt(selecedRow + i, 0).toString();
	    Double Mtemp_Qty = Double.valueOf(tblItemTable.getValueAt(selecedRow + i, 1).toString());
	    Double Mtemp_Amt = Double.valueOf(tblItemTable.getValueAt(selecedRow + i, 2).toString());
	    String MtempCode = tblItemTable.getValueAt(selecedRow + i, 3).toString();

	    frmSplitBill objOriginalItem = mapItemCodeOriginalQty.get(tempCode);
	    frmSplitBill objOriginalModifier = mapItemCodeOriginalQty.get(MtempCode + "!" + Mtemp_ItemName);

	    double modifierQtyPerOneItemQty = objOriginalModifier.cls_qty / objOriginalItem.cls_qty;
	    double modifierAmtPerOneItemQty = objOriginalModifier.cls_Amt / objOriginalItem.cls_qty;
	    funFillListForManualSplit(list_Grids, Mtemp_ItemName, (selectedQty * modifierQtyPerOneItemQty), (modifierAmtPerOneItemQty * selectedQty), MtempCode, ((objOriginalModifier.discountAmt) / objOriginalModifier.cls_qty) * (selectedQty * modifierQtyPerOneItemQty), objOriginalModifier.cls_Rate);
	    count++;
	}
	return count;
    }

///ritesh Date 05 Sept 2014
    private void funRefreshGrids(DefaultTableModel tableModel)
    {
	try
	{
	    if (btnMoveNext.isEnabled())
	    {
		switch (selectedTable)
		{
		    case "Table1":
			funAddRowsForManualSplit(tableModel, list_Grid1_SplitedItems, selectedTable);
			break;
		    case "Table2":
			funAddRowsForManualSplit(tableModel, list_Grid2_SplitedItems, selectedTable);
			break;
		    case "Table3":
			funAddRowsForManualSplit(tableModel, list_Grid3_SplitedItems, selectedTable);
			break;
		    case "Table4":
			funAddRowsForManualSplit(tableModel, list_Grid4_SplitedItems, selectedTable);
			break;
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * @return the cls_ItemName
     */
    public String getCls_ItemName()
    {
	return cls_ItemName;
    }

    /**
     * @param cls_ItemName the cls_ItemName to set
     */
    public void setCls_ItemName(String cls_ItemName)
    {
	this.cls_ItemName = cls_ItemName;
    }

    /**
     * @return the cls_ItemCode
     */
    public String getCls_ItemCode()
    {
	return cls_ItemCode;
    }

    /**
     * @param cls_ItemCode the cls_ItemCode to set
     */
    public void setCls_ItemCode(String cls_ItemCode)
    {
	this.cls_ItemCode = cls_ItemCode;
    }

    /**
     * @return the cls_qty
     */
    public double getCls_qty()
    {
	return cls_qty;
    }

    /**
     * @param cls_qty the cls_qty to set
     */
    public void setCls_qty(double cls_qty)
    {
	this.cls_qty = cls_qty;
    }

    /**
     * @return the cls_Amt
     */
    public double getCls_Amt()
    {
	return cls_Amt;
    }

    /**
     * @param cls_Amt the cls_Amt to set
     */
    public void setCls_Amt(double cls_Amt)
    {
	this.cls_Amt = cls_Amt;
    }

///ritesh Date 05 Sept 2014
    private void funAddRowsForManualSplit(DefaultTableModel tableModel, List<frmSplitBill> list_Grids, String tableNo)
    {
	try
	{
	    double subTotal = 0.00;
	    double grandTotal = 0.00;
	    double discountAmt = 0.00;
	    double discount = 0.00;
	    DecimalFormat decimalFormat = new DecimalFormat("#.##");
	    String itemCode = "";
	    String finalAmount = "";
	    tableModel.setRowCount(0);

	    //calculate promotions at the time of item moving to grid
	    boolean flgApplyPromoOnBill = false;
	    HashMap<String, clsBillItemDtl> mapPromoItemDisc = new HashMap<>();
	    Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();
	    Map<String, clsPromotionItems> hmPromoItemDtl = null;// funCalculatePromotionForManualSplit(list_Grids);
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

	    ArrayList arrListManualSplitItemDtl = new ArrayList();
	    for (frmSplitBill list_cls_SplitedItem : list_Grids)
	    {
		frmSplitBill obj = (frmSplitBill) list_cls_SplitedItem;
		String itemName = obj.getCls_ItemName();
		double itemQty = obj.getCls_qty();
		double amount = obj.getCls_Amt();
		double rate = obj.cls_Rate;
		itemCode = obj.getCls_ItemCode();
		double freeAmount = 0.00;
		////////////////////////
		if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
		{
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
					hmPromoItem.put(tableNo + "!" + itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(itemCode + "!" + itemName, objPromoItemsDtl);
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
					hmPromoItem.put(tableNo + "!" + itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(tableNo + "!" + itemCode + "!" + itemName, objPromoItemsDtl);

					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(itemName);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(tableNo + "!" + itemCode, objItemPromoDiscount);
				    }
				    else
				    {
					discP = objPromoItemsDtl.getDiscPer();
					discA = (discP / 100) * amount;
					//amount=amount-(amount*(disc/100));
					String promoCode = objPromoItemsDtl.getPromoCode();
					hmPromoItem.put(tableNo + "!" + itemCode, objPromoItemsDtl);
					hmPromoItemsToDisplay.put(tableNo + "!" + itemCode + "!" + itemName, objPromoItemsDtl);
					//hmPromoItem.put(itemCode, discP+"#"+promoCode);
					hmPromoItemDtl.remove(itemCode);
					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(itemName);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(tableNo + "!" + itemCode, objItemPromoDiscount);
				    }
				}
			    }
			}
		    }
		}
		///////////////////////  
		obj.cls_Amt = amount;

		subTotal += amount;
		String itemDtl = itemCode + "#" + itemName + "#" + itemQty + "#" + amount;
		Object row[] =
		{
		    itemName, decimalFormat.format(itemQty), decimalFormat.format(amount), itemCode
		};
		tableModel.addRow(row);
		discount = obj.discountAmt;
		itemDtl = itemDtl + "#" + discount;
		arrListManualSplitItemDtl.add(itemDtl);

	    }
	    Object blankRow[] =
	    {
		"", "", "", ""
	    };
	    tableModel.addRow(blankRow);
	    Object row_subTotal[] =
	    {
		"Sub Total", "", decimalFormat.format(subTotal), ""
	    };
	    tableModel.addRow(row_subTotal);
	    if (clsGlobalVarClass.gItemWiseDiscount)
	    {
		for (int i = 0; i < arrListManualSplitItemDtl.size(); i++)
		{
		    discountAmt += Double.parseDouble(arrListManualSplitItemDtl.get(i).toString().split("#")[4]);
		}
	    }
	    Object row_discount[] =
	    {
		"Discount", "", decimalFormat.format(discountAmt), ""
	    };
	    tableModel.addRow(row_discount);
	    finalAmount = String.valueOf(subTotal);
	    List listTax = funCalculateTax(billedOperationType, arrListManualSplitItemDtl);
	    ArrayList<ArrayList> arrListTax = new ArrayList<ArrayList>();
	    double taxAmt = 0;
	    for (int cntTax = 0; cntTax < listTax.size(); cntTax++)
	    {
		ArrayList<String> arrListTax1 = new ArrayList<String>();
		//System.out.println(listTax.get(cntTax));
		ArrayList<Object> list = (ArrayList<Object>) listTax.get(cntTax);
		double dblTaxAmt = Double.parseDouble(list.get(3).toString());
		taxAmt = taxAmt + dblTaxAmt;
		Object[] taxTotalRow =
		{
		    list.get(1).toString(), "", decimalFormat.format(dblTaxAmt)
		};
		tableModel.addRow(taxTotalRow);
		arrListTax1.add(list.get(0).toString()); // Tax Code
		arrListTax1.add(list.get(2).toString()); // Taxable Amt
		arrListTax1.add(list.get(3).toString()); // Tax Amt
		arrListTax1.add(list.get(4).toString()); // Tax calculation

		arrListTax.add(arrListTax1);
	    }
	    // grandTotal=subTotal+taxAmt;
	    if (null != hmTaxOnBill.get(tableNo))
	    {
		hmTaxOnBill.remove(tableNo);
	    }
	    hmTaxOnBill.put(tableNo, arrListTax);

	    Object row_Grand[] =
	    {
		"Grand Total", "", decimalFormat.format(grandTotal), ""
	    };
	    tableModel.addRow(row_Grand);
	    funSetLabelsAmount(subTotal, discount);
	    //funSetLabelsAmount(subTotal,discountAmt,grandTotal);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

///ritesh Date 05 Sept 2014
    private void funFillListForManualSplit(List<frmSplitBill> list_SplitedItems, String temp_ItemName, double temp_Qty, double temp_Amt, String tempCode, double discAmt, double rate)
    {
	double discount = funGetItemWiseDiscount(txtBillSearch.getText(), tempCode, temp_ItemName);
	frmSplitBill obj = new frmSplitBill(temp_ItemName, temp_Qty, temp_Amt, tempCode, discAmt, rate);
	boolean isAdded = false;
	for (int i = 0; i < list_SplitedItems.size(); i++)
	{
	    frmSplitBill oldObj = list_SplitedItems.get(i);
	    if (oldObj.cls_ItemCode.equalsIgnoreCase(tempCode) && oldObj.cls_ItemName.equals(temp_ItemName))
	    {
		list_SplitedItems.remove(oldObj);
		oldObj = new frmSplitBill(temp_ItemName, oldObj.cls_qty + temp_Qty, oldObj.cls_Amt + temp_Amt, tempCode, oldObj.discountAmt + discAmt, rate);
		list_SplitedItems.add(oldObj);
		isAdded = true;
		break;
	    }
	    else
	    {
		continue;
	    }
	}
	if (!isAdded)
	{
	    list_SplitedItems.add(obj);
	}
	obj = null;
    }

///ritesh Date 05 Sept 2014
    private void funClearObjectLists()
    {
	list_Grid1_SplitedItems.clear();
	list_Grid2_SplitedItems.clear();
	list_Grid3_SplitedItems.clear();
	list_Grid4_SplitedItems.clear();
    }

    private void funSetLabelsAmount(double itemAmt, double discountAmt)
    {
	try
	{
	    lblSubTotal1.setVisible(true);
	    lblDiscount1.setVisible(true);
	    lblNetAmt1.setVisible(true);
	    double subfromLabel = Double.valueOf(lblSubTotal.getText());
	    double disFromLabel = Double.valueOf(lblDiscount.getText());
	    double subFinal = subfromLabel - itemAmt;
	    double disFinal = disFromLabel - discountAmt;
	    double netFinal = subFinal - disFinal;
	    lblSubTotal.setText(String.valueOf(Math.rint(subFinal)));
	    lblDiscount.setText(String.valueOf(Math.rint(disFinal)));
	    lblNetAmount.setText(String.valueOf(Math.rint(netFinal)));
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
        panelFormBody1 = new javax.swing.JPanel();
        lblBillSearch = new javax.swing.JLabel();
        txtBillSearch = new javax.swing.JTextField();
        scrBillDtl = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        lblPersons = new javax.swing.JLabel();
        btnSplit = new javax.swing.JButton();
        scrSettle = new javax.swing.JScrollPane();
        tblSettlement = new javax.swing.JTable();
        scrTax = new javax.swing.JScrollPane();
        tblTaxTable = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        lblSelectSplitTYpe = new javax.swing.JLabel();
        cmbSplitType = new javax.swing.JComboBox();
        scrBillSplitDtl1 = new javax.swing.JScrollPane();
        tblTable1 = new javax.swing.JTable();
        scrBillSplitDtl2 = new javax.swing.JScrollPane();
        tblTable2 = new javax.swing.JTable();
        scrBillSplitDtl3 = new javax.swing.JScrollPane();
        tblTable3 = new javax.swing.JTable();
        scrBillSplitDtl4 = new javax.swing.JScrollPane();
        tblTable4 = new javax.swing.JTable();
        btnMoveNext = new javax.swing.JButton();
        btnMovePrev = new javax.swing.JButton();
        lblSubTotal1 = new javax.swing.JLabel();
        lblDiscount1 = new javax.swing.JLabel();
        lblNetAmt1 = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        lblDiscount = new javax.swing.JLabel();
        lblNetAmount = new javax.swing.JLabel();
        cmbSplitQty = new javax.swing.JComboBox();

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
        lblformName.setText("- Split Bill");
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

        panelFormBody1.setBackground(new java.awt.Color(255, 255, 255));
        panelFormBody1.setOpaque(false);
        panelFormBody1.setPreferredSize(new java.awt.Dimension(610, 600));
        panelFormBody1.setLayout(null);

        lblBillSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillSearch.setText("Bill No.           :");
        panelFormBody1.add(lblBillSearch);
        lblBillSearch.setBounds(20, 10, 90, 30);

        txtBillSearch.setEditable(false);
        txtBillSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBillSearchMouseClicked(evt);
            }
        });
        panelFormBody1.add(txtBillSearch);
        txtBillSearch.setBounds(120, 10, 150, 30);

        scrBillDtl.setBackground(new java.awt.Color(255, 255, 255));

        tblItemTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount", "code", "Discount"
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
        tblItemTable.setMinimumSize(new java.awt.Dimension(45, 240));
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
        scrBillDtl.setViewportView(tblItemTable);

        panelFormBody1.add(scrBillDtl);
        scrBillDtl.setBounds(10, 50, 310, 410);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnClose);
        btnClose.setBounds(700, 520, 90, 40);

        lblPersons.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPersons.setText("Enter Split QTY     :");
        panelFormBody1.add(lblPersons);
        lblPersons.setBounds(580, 10, 120, 30);

        btnSplit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSplit.setForeground(new java.awt.Color(255, 255, 255));
        btnSplit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSplit.setText("SPLIT BILL");
        btnSplit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSplit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSplit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSplitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnSplitMouseEntered(evt);
            }
        });
        panelFormBody1.add(btnSplit);
        btnSplit.setBounds(500, 520, 100, 40);

        tblSettlement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "SettlementCode", "SettlementName", "Amount", "CardNumber", "ExpriyDate", "PaidAmount", "CouponRemark", "ActualAmount", "RefundAmount"
            }
        ));
        scrSettle.setViewportView(tblSettlement);

        panelFormBody1.add(scrSettle);
        scrSettle.setBounds(200, 540, 50, 20);

        tblTaxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Tax Code", "Tax Name", "Taxable Amt", "Tax Amt", "ItemCode", "TaxCalculation"
            }
        ));
        scrTax.setViewportView(tblTaxTable);

        panelFormBody1.add(scrTax);
        scrTax.setBounds(250, 540, 80, 20);

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnReset);
        btnReset.setBounds(610, 520, 80, 40);

        lblSelectSplitTYpe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSelectSplitTYpe.setText("Split Type  :");
        panelFormBody1.add(lblSelectSplitTYpe);
        lblSelectSplitTYpe.setBounds(290, 10, 120, 30);

        cmbSplitType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----- Select -----", "Equal Split", "Group Wise", "Item Type Wise", "Item Wise" }));
        cmbSplitType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSplitTypeActionPerformed(evt);
            }
        });
        panelFormBody1.add(cmbSplitType);
        cmbSplitType.setBounds(410, 10, 160, 30);

        scrBillSplitDtl1.setBackground(new java.awt.Color(255, 255, 255));
        scrBillSplitDtl1.setName("scrBillSplitDtl1"); // NOI18N
        scrBillSplitDtl1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrBillSplitDtl1MouseClicked(evt);
            }
        });

        tblTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amt", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTable1.getTableHeader().setReorderingAllowed(false);
        scrBillSplitDtl1.setViewportView(tblTable1);

        panelFormBody1.add(scrBillSplitDtl1);
        scrBillSplitDtl1.setBounds(330, 50, 230, 230);

        scrBillSplitDtl2.setName("scrBillSplitDtl2"); // NOI18N
        scrBillSplitDtl2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrBillSplitDtl2MouseClicked(evt);
            }
        });

        tblTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amt", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTable2.getTableHeader().setReorderingAllowed(false);
        scrBillSplitDtl2.setViewportView(tblTable2);

        panelFormBody1.add(scrBillSplitDtl2);
        scrBillSplitDtl2.setBounds(570, 50, 220, 230);

        scrBillSplitDtl3.setName("scrBillSplitDtl3"); // NOI18N
        scrBillSplitDtl3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrBillSplitDtl3MouseClicked(evt);
            }
        });

        tblTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amt", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTable3.getTableHeader().setReorderingAllowed(false);
        scrBillSplitDtl3.setViewportView(tblTable3);

        panelFormBody1.add(scrBillSplitDtl3);
        scrBillSplitDtl3.setBounds(330, 290, 230, 220);

        scrBillSplitDtl4.setName("scrBillSplitDtl4"); // NOI18N
        scrBillSplitDtl4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrBillSplitDtl4MouseClicked(evt);
            }
        });

        tblTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amt", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTable4.getTableHeader().setReorderingAllowed(false);
        scrBillSplitDtl4.setViewportView(tblTable4);

        panelFormBody1.add(scrBillSplitDtl4);
        scrBillSplitDtl4.setBounds(570, 290, 220, 220);

        btnMoveNext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMoveNext.setForeground(new java.awt.Color(255, 255, 255));
        btnMoveNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnMoveNext.setText(">>");
        btnMoveNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoveNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnMoveNext.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMoveNextMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnMoveNext);
        btnMoveNext.setBounds(430, 520, 60, 40);

        btnMovePrev.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMovePrev.setForeground(new java.awt.Color(255, 255, 255));
        btnMovePrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnMovePrev.setText("<<");
        btnMovePrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMovePrev.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnMovePrev.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMovePrevMouseClicked(evt);
            }
        });
        panelFormBody1.add(btnMovePrev);
        btnMovePrev.setBounds(330, 520, 60, 40);

        lblSubTotal1.setBackground(new java.awt.Color(255, 255, 255));
        lblSubTotal1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSubTotal1.setText("Sub Total        :");
        panelFormBody1.add(lblSubTotal1);
        lblSubTotal1.setBounds(20, 470, 110, 20);

        lblDiscount1.setBackground(new java.awt.Color(255, 255, 255));
        lblDiscount1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDiscount1.setText("Discount         :");
        panelFormBody1.add(lblDiscount1);
        lblDiscount1.setBounds(20, 500, 100, 20);

        lblNetAmt1.setBackground(new java.awt.Color(255, 255, 255));
        lblNetAmt1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNetAmt1.setText("Net Amount    :");
        panelFormBody1.add(lblNetAmt1);
        lblNetAmt1.setBounds(20, 530, 100, 20);

        lblSubTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubTotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelFormBody1.add(lblSubTotal);
        lblSubTotal.setBounds(180, 470, 120, 20);

        lblDiscount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelFormBody1.add(lblDiscount);
        lblDiscount.setBounds(180, 500, 120, 20);

        lblNetAmount.setBackground(new java.awt.Color(255, 255, 255));
        lblNetAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNetAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetAmount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelFormBody1.add(lblNetAmount);
        lblNetAmount.setBounds(180, 530, 120, 20);

        cmbSplitQty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSplitQtyActionPerformed(evt);
            }
        });
        panelFormBody1.add(cmbSplitQty);
        cmbSplitQty.setBounds(700, 10, 90, 30);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFormBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelFormBody1, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFormBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelFormBody1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBillSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillSearchMouseClicked
	// TODO add your handling code here:
	try
	{
	    clsGlobalVarClass.gFormNameOnKeyBoard = "Bill No";
	    objUtility.funCallForSearchForm("SplitBill");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetBillData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
		clsGlobalVarClass.gtblRowcount = tblItemTable.getRowCount() - clsGlobalVarClass.gItemCount;
		//System.out.println("search click tbl count=" + tblItemTable.getRowCount());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtBillSearchMouseClicked

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
	try
	{
	    if (tblItemTable.getRowCount() > 0)
	    {
		int temp_selectedRow = tblItemTable.getSelectedRow();
		if (temp_selectedRow > -1)
		{
		    btnMoveNext.setEnabled(true);
		    selecedRow = tblItemTable.getSelectedRow();
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("SplitBill");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnSplitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSplitMouseClicked
	try
	{
	    billsToBePrinted.clear();
	    if (cmbSplitType.getSelectedIndex() == 0)
	    {
		new frmOkPopUp(null, "Please Select Split Type", "Error", 1).setVisible(true);
	    }
	    else if (cmbSplitType.getSelectedItem().equals("Equal Split"))
	    {
		funEqualSplit(voucherNo);
	    }
	    else if (cmbSplitType.getSelectedItem().equals("Group Wise"))
	    {
		funGroupWiseSplit(voucherNo);
	    }
	    else if (cmbSplitType.getSelectedItem().equals("Item Type Wise"))
	    {
		funItemTypeWiseSplit(voucherNo);
	    }
	    else if (cmbSplitType.getSelectedItem().equals("Item Wise"))
	    {
		funManualSplit();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnSplitMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void cmbSplitTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSplitTypeActionPerformed
	funSplitTypeComboClicked();
    }//GEN-LAST:event_cmbSplitTypeActionPerformed

    private void scrBillSplitDtl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrBillSplitDtl1MouseClicked
	funSetSelectedTable(scrBillSplitDtl1);
    }//GEN-LAST:event_scrBillSplitDtl1MouseClicked

    private void scrBillSplitDtl2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrBillSplitDtl2MouseClicked
	funSetSelectedTable(scrBillSplitDtl2);
    }//GEN-LAST:event_scrBillSplitDtl2MouseClicked

    private void scrBillSplitDtl3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrBillSplitDtl3MouseClicked
	funSetSelectedTable(scrBillSplitDtl3);
    }//GEN-LAST:event_scrBillSplitDtl3MouseClicked

    private void scrBillSplitDtl4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrBillSplitDtl4MouseClicked
	funSetSelectedTable(scrBillSplitDtl4);
    }//GEN-LAST:event_scrBillSplitDtl4MouseClicked

    private void btnMoveNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMoveNextMouseClicked
	if (cmbSplitType.getSelectedItem().toString().equalsIgnoreCase("Item Wise"))
	{
	    if (btnMoveNext.isEnabled())
	    {
		switch (selectedTable)
		{
		    case "Table1":
			funFillTable1ForManualSplit();
			break;
		    case "Table2":
			funFillTable2ForManualSplit();
			break;
		    case "Table3":
			funFillTable3ForManualSplit();
			break;
		    case "Table4":
			funFillTable4ForManualSplit();
			break;
		}
	    }
	}
    }//GEN-LAST:event_btnMoveNextMouseClicked

    private void btnMovePrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovePrevMouseClicked
	if (btnMovePrev.isEnabled())
	{
	    funFillPrevOriginalTable();
	}
    }//GEN-LAST:event_btnMovePrevMouseClicked

    private void cmbSplitQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSplitQtyActionPerformed
	funSplitQtyComboClicked();
    }//GEN-LAST:event_cmbSplitQtyActionPerformed

    private void btnSplitMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSplitMouseEntered
    {//GEN-HEADEREND:event_btnSplitMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnSplitMouseEntered

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("SplitBill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("SplitBill");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
	/*
         * Set the Nimbus look and feel
	 */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
	    java.util.logging.Logger.getLogger(frmSplitBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmSplitBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmSplitBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmSplitBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/*
         * Create and display the form
	 */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmSplitBill().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrev;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSplit;
    private javax.swing.JComboBox cmbSplitQty;
    private javax.swing.JComboBox cmbSplitType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBillSearch;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblDiscount1;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNetAmount;
    private javax.swing.JLabel lblNetAmt1;
    private javax.swing.JLabel lblPersons;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSelectSplitTYpe;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblSubTotal1;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelFormBody1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrBillDtl;
    private javax.swing.JScrollPane scrBillSplitDtl1;
    private javax.swing.JScrollPane scrBillSplitDtl2;
    private javax.swing.JScrollPane scrBillSplitDtl3;
    private javax.swing.JScrollPane scrBillSplitDtl4;
    private javax.swing.JScrollPane scrSettle;
    private javax.swing.JScrollPane scrTax;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblSettlement;
    private javax.swing.JTable tblTable1;
    private javax.swing.JTable tblTable2;
    private javax.swing.JTable tblTable3;
    private javax.swing.JTable tblTable4;
    private javax.swing.JTable tblTaxTable;
    private javax.swing.JTextField txtBillSearch;
    // End of variables declaration//GEN-END:variables

    private void funSortItemsItemTypeWise()
    {
	try
	{
	    funReseteOnlyRightSideGrids();
	    DefaultTableModel dtmForSplitBillItemTypeWise = (DefaultTableModel) tblItemTable.getModel();
	    HashMap<String, String> mapMainGridDetail = new HashMap<String, String>();
	    int rowCount = dtmForSplitBillItemTypeWise.getRowCount();
	    ArrayList<String> itemCodeList = new ArrayList<String>();
	    for (int row = 0; row < rowCount; row++)
	    {
		Object objItemCode = dtmForSplitBillItemTypeWise.getValueAt(row, 3);
		if (objItemCode != null && objItemCode.toString().startsWith("I"))
		{
		    String itemCode = objItemCode.toString();
		    itemCodeList.add(itemCode);
		    String itemName = dtmForSplitBillItemTypeWise.getValueAt(row, 0).toString();
		    String itemQty = dtmForSplitBillItemTypeWise.getValueAt(row, 1).toString();
		    String itemAmount = dtmForSplitBillItemTypeWise.getValueAt(row, 2).toString();
		    String itemDisc = dtmForSplitBillItemTypeWise.getValueAt(row, 4).toString();
		    mapMainGridDetail.put(itemCode, itemName + "!" + itemQty + "!" + itemAmount + "!" + itemDisc);
		}
	    }
	    String sqlItem = "SELECT strItemCode,strItemType "
		    + "FROM tblitemmaster  "
		    + "WHERE strItemCode "
		    + "IN (";
	    for (int i = 0; i < itemCodeList.size(); i++)
	    {
		if (i == 0)
		{
		    sqlItem = sqlItem + "'" + itemCodeList.get(i) + "' ";
		}
		else
		{
		    sqlItem = sqlItem + ",'" + itemCodeList.get(i) + "' ";
		}
	    }
	    sqlItem = sqlItem + ")";
	    sql = sqlItem + " GROUP BY  strItemType ";
	    System.out.println("Item Types==" + sql);
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    ArrayList<String> itemTypeList = new ArrayList<String>();
	    int count = 0;
	    while (resultSet.next())
	    {
		count++;
		itemTypeList.add(resultSet.getString("strItemType"));
	    }
	    if (count > 4)
	    {
		new frmOkPopUp(null, "Item Type Is More Than Four.", "Error", 1).setVisible(true);
		return;
	    }
	    HashMap<String, List> mapItemTypeWithItems = new HashMap<String, List>();
	    for (int i = 0; i < itemTypeList.size(); i++)
	    {
		String itemType = itemTypeList.get(i);
		sql = sqlItem
			+ "AND strItemType='" + itemType + "' ";
		System.out.println("Items Per Type==" + sql);
		resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		List<String> items = new ArrayList<String>();
		while (resultSet.next())
		{
		    items.add(resultSet.getString("strItemCode"));
		}
		//modifiers
		String sqlModiItems = "select  a.strItemCode,b.strItemType "
			+ " from tblbillmodifierdtl a,tblitemmaster b "
			+ " where left(a.strItemCode,7)=b.strItemCode "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ " and  a.strItemCode IN ( ";
		for (int j = 0; j < itemCodeList.size(); j++)
		{
		    if (j == 0)
		    {
			sqlModiItems = sqlModiItems + "'" + itemCodeList.get(j) + "' ";
		    }
		    else
		    {
			sqlModiItems = sqlModiItems + ",'" + itemCodeList.get(j) + "' ";
		    }
		}
		sqlModiItems = sqlModiItems + ")";
		sqlModiItems = sqlModiItems + " AND b.strItemType='" + itemType + "' and a.strBillNo='" + billNo + "' ";
		ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sqlModiItems);
		while (rsModi.next())
		{
		    items.add(rsModi.getString(1));
		}
		//
		mapItemTypeWithItems.put(itemType, items);
	    }
	    funFillItemsItemTypeWise(mapItemTypeWithItems, mapMainGridDetail);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillItemsItemTypeWise(HashMap<String, List> mapItemTypeWithItems, HashMap<String, String> mapMainGridItemDetail)
    {
	Iterator<String> it = mapItemTypeWithItems.keySet().iterator();
	int itemTypeCount = 0;
	while (it.hasNext())
	{
	    try
	    {
		itemTypeCount++;
		String key = it.next();
		List<String> items = mapItemTypeWithItems.get(key);
		switch (itemTypeCount)
		{
		    case 1:
			funFillGrid1(key, items, mapMainGridItemDetail);
			break;
		    case 2:
			funFillGrid2(key, items, mapMainGridItemDetail);
			break;
		    case 3:
			funFillGrid3(key, items, mapMainGridItemDetail);
			break;
		    case 4:
			funFillGrid4(key, items, mapMainGridItemDetail);
			break;
		}
	    }
	    catch (Exception ex)
	    {
		ex.printStackTrace();
	    }
	}
    }

    private void funFillGrid1(String key, List<String> items, HashMap<String, String> mapMainGridItemDetail) throws Exception
    {
	try
	{
	    DefaultTableModel dtm1 = (DefaultTableModel) tblTable1.getModel();
	    dtm1.setRowCount(0);
	    System.out.println("");
	    System.out.println("Key------->" + key);
	    System.out.println("Item Details");
	    mapMainGridItemDtls = mapMainGridItemDetail;
	    double subTotal = 0.00;
	    double discount = 0.00;
	    for (int i = 0; i < items.size(); i++)
	    {
		String itemCode = items.get(i);
		String itemDetail = mapMainGridItemDetail.get(itemCode);
		String itemDetailArray[] = itemDetail.split("!");
		String itemName = itemDetailArray[0];
		String itemQty = itemDetailArray[1];
		String itemAmt = itemDetailArray[2];
		double itemDisc = Double.parseDouble(itemDetailArray[3]);
		subTotal = subTotal + Double.parseDouble(itemAmt);
		Object object[] =
		{
		    itemName, itemQty, itemAmt, itemCode
		};
		dtm1.addRow(object);
		discount += itemDisc;
	    }
	    //funAddTaxDetail(dtm1,subTotal,discount);
	    double discPercentage = (discount / subTotal) * 100;
	    List<ArrayList<Object>> listTax = funCheckDateRangeForTax2(billedOperationType, items, discPercentage, mapMainGridItemDetail);
	    funAddTaxDetail(dtm1, subTotal, listTax, discount);
	    tblTable1.setModel(dtm1);
	    scrBillSplitDtl1.setVisible(true);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillGrid2(String key, List<String> items, HashMap<String, String> mapMainGridItemDetail)
    {
	try
	{
	    DefaultTableModel dtm2 = (DefaultTableModel) tblTable2.getModel();
	    dtm2.setRowCount(0);
	    System.out.println("");
	    System.out.println("Key------->" + key);
	    System.out.println("Item Details");
	    double subTotal = 0.00;
	    double discount = 0.00;
	    double discPer = 0.00;
	    for (int i = 0; i < items.size(); i++)
	    {
		String itemCode = items.get(i);
		String itemDetail = mapMainGridItemDetail.get(itemCode);
		String itemDetailArray[] = itemDetail.split("!");
		String itemName = itemDetailArray[0];
		String itemQty = itemDetailArray[1];
		String itemAmt = itemDetailArray[2];
		double itemDisc = Double.parseDouble(itemDetailArray[3]);
		subTotal = subTotal + Double.parseDouble(itemAmt);
		Object object[] =
		{
		    itemName, itemQty, itemAmt, itemCode
		};
		dtm2.addRow(object);
		discount += itemDisc;
	    }
	    //discount=Double.parseDouble(lblDiscount.getText());
	    //funAddTaxDetail(dtm2, subTotal, discount);
	    double discPercentage = (discount / subTotal) * 100;
	    List<ArrayList<Object>> listTax = funCheckDateRangeForTax2(billedOperationType, items, discPercentage, mapMainGridItemDetail);
	    funAddTaxDetail(dtm2, subTotal, listTax, discount);
	    tblTable2.setModel(dtm2);
	    scrBillSplitDtl2.setVisible(true);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillGrid3(String key, List<String> items, HashMap<String, String> mapMainGridItemDetail)
    {
	try
	{
	    DefaultTableModel dtm3 = (DefaultTableModel) tblTable3.getModel();
	    dtm3.setRowCount(0);
	    System.out.println("");
	    System.out.println("Key------->" + key);
	    System.out.println("Item Details");
	    double subTotal = 0.00;
	    double discount = 0.00;
	    for (int i = 0; i < items.size(); i++)
	    {
		String itemCode = items.get(i);
		String itemDetail = mapMainGridItemDetail.get(itemCode);
		String itemDetailArray[] = itemDetail.split("!");
		String itemName = itemDetailArray[0];
		String itemQty = itemDetailArray[1];
		String itemAmt = itemDetailArray[2];
		double itemDisc = Double.parseDouble(itemDetailArray[3]);
		subTotal = subTotal + Double.parseDouble(itemAmt);
		Object object[] =
		{
		    itemName, itemQty, itemAmt, itemCode
		};
		dtm3.addRow(object);
		discount += itemDisc;
	    }
	    //double discount=Double.parseDouble(lblDiscount.getText());
	    //funAddTaxDetail(dtm3, subTotal, discount);
	    double discPercentage = (discount / subTotal) * 100;
	    List<ArrayList<Object>> listTax = funCheckDateRangeForTax2(billedOperationType, items, discPercentage, mapMainGridItemDetail);
	    funAddTaxDetail(dtm3, subTotal, listTax, discount);
	    tblTable3.setModel(dtm3);
	    scrBillSplitDtl3.setVisible(true);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillGrid4(String key, List<String> items, HashMap<String, String> mapMainGridItemDetail)
    {
	try
	{
	    DefaultTableModel dtm4 = (DefaultTableModel) tblTable4.getModel();
	    dtm4.setRowCount(0);
	    System.out.println("");
	    System.out.println("Key------->" + key);
	    System.out.println("Item Details");
	    double subTotal = 0.00;
	    double discount = 0.00;
	    for (int i = 0; i < items.size(); i++)
	    {
		String itemCode = items.get(i);
		String itemDetail = mapMainGridItemDetail.get(itemCode);
		String itemDetailArray[] = itemDetail.split("!");
		String itemName = itemDetailArray[0];
		String itemQty = itemDetailArray[1];
		String itemAmt = itemDetailArray[2];
		double itemDisc = Double.parseDouble(itemDetailArray[3]);
		subTotal = subTotal + Double.parseDouble(itemAmt);
		Object object[] =
		{
		    itemName, itemQty, itemAmt, itemCode
		};
		dtm4.addRow(object);
		discount += itemDisc;
	    }
	    //double discount=Double.parseDouble(lblDiscount.getText());
	    // funAddTaxDetail(dtm4, subTotal, discount);
	    double discPercentage = (discount / subTotal) * 100;
	    List<ArrayList<Object>> listTax = funCheckDateRangeForTax2("", items, discPercentage, mapMainGridItemDetail);
	    funAddTaxDetail(dtm4, subTotal, listTax, discount);
	    tblTable4.setModel(dtm4);
	    scrBillSplitDtl4.setVisible(true);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAddTaxDetail(DefaultTableModel dtm, double subTotal, double discount)
    {
	try
	{
	    Object object1[] =
	    {
		"", "", "", ""
	    };
	    dtm.addRow(object1);
	    Object object2[] =
	    {
		"SubTotal", "", subTotal, ""
	    };
	    dtm.addRow(object2);
	    // double contibuteDiscount=discount/itemTypeCount;
	    Object object3[] =
	    {
		"Discount", "", discount, ""
	    };
	    dtm.addRow(object3);
	    String sql = "select a.dblPercent,a.strTaxDesc from tbltaxhd a "
		    + " where strTaxCode In(select strTaxCode from tblbilltaxdtl where strBillNo='" + txtBillSearch.getText().trim() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' )";
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    double totalTaxAmount = 0.00;
	    while (resultSet.next())
	    {
		String taxDiscription = resultSet.getString("strTaxDesc");
		double taxPercent = resultSet.getDouble("dblPercent");
		double taxPerSubTotal = (taxPercent / 100) * subTotal;
		taxPerSubTotal = Math.round(taxPerSubTotal);
		totalTaxAmount = totalTaxAmount + taxPerSubTotal;
		Object tax[] =
		{
		    taxDiscription, "", taxPerSubTotal, ""
		};
		dtm.addRow(tax);
	    }
	    totalTaxAmount = Math.rint(totalTaxAmount);
	    double grandTotal = subTotal + totalTaxAmount - discount;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
	    grandTotal = mapRoundOff.get("roundOffAmt");
	    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt

	    Object grandTotalAmount[] =
	    {
		"GrandTotal", "", grandTotal, ""
	    };
	    dtm.addRow(grandTotalAmount);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetLabelsAmount(double subTotal, double discountAmt, double grandTotal)
    {
	double finalSubTotal = Double.parseDouble(lblSubTotal.getText()) - subTotal;
	double finalDiscount = Double.parseDouble(lblDiscount.getText()) - discountAmt;
	double finalNetAmount = Double.parseDouble(lblNetAmount.getText()) - grandTotal;
	lblSubTotal.setText(String.valueOf(Math.rint(finalSubTotal)));
	lblDiscount.setText(String.valueOf(Math.rint(finalDiscount)));
	lblNetAmount.setText(String.valueOf(Math.rint(finalNetAmount)));
    }

    private double funGetItemWiseDiscount(String billNo, String itemCode, String itemName)
    {
	double discount = 0.00;
	try
	{
	    String sql = "select a.dblDiscountAmt from tblbilldtl a "
		    + " where a.strBillNo='" + billNo + "' "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " and a.strItemCode='" + itemCode + "' ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		discount = rs.getDouble("dblDiscountAmt");
	    }
	    else
	    {
		sql = "select a.dblDiscAmt from tblbillmodifierdtl a "
			+ " where a.strBillNo='" + billNo + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			+ " and a.strItemCode='" + itemCode + "' "
			+ " and a.strModifierName='" + itemName + "' ";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rs.next())
		{
		    discount = rs.getDouble("dblDiscAmt");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return discount;
    }

    private ArrayList funCheckDateRangeForTax2(String operationType, List<String> arrListItemDtl, double discPercent, HashMap<String, String> mapMainGridItemDetail) throws Exception
    {
	String taxCode = "", taxName = "", taxOnGD = "", taxCal = "", taxIndicator = "";
	String opType = "", area = "", taxOnTax = "", taxOnTaxCode = "", itemType = "";
	double taxPercent = 0.00, taxableAmount = 0.00, taxCalAmt = 0.00;
	ArrayList<Object> listTax = new ArrayList<Object>();
	arrListTaxCal = new ArrayList<ArrayList<Object>>();
	clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp;");// Empty Tax Temp Table
	double subTotalForTax = 0, discAmt = 0;

//        String itemCode = items.get(i);
//        String itemDetail = mapMainGridItemDetail.get(itemCode);
//        String itemDetailArray[] = itemDetail.split("!");
//        String itemName = itemDetailArray[0];
//        String itemQty = itemDetailArray[1];
//        double itemAmt = Double.parseDouble(itemDetailArray[2]);
//        double itemDisc = Double.parseDouble(itemDetailArray[3]);
	String sql_ChkTaxDate = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent"
		+ ",a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType"
		+ ",a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
		+ "from tbltaxhd a,tbltaxposdtl b "
		+ "where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and date(a.dteValidFrom) <='" + dtPOSDate + "' "
		+ "and date(a.dteValidTo)>='" + dtPOSDate + "' and a.strTaxOnSP='Sales'";
	ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_ChkTaxDate);
	while (rsTax.next())
	{
	    taxCode = rsTax.getString(1);
	    taxName = rsTax.getString(2);
	    taxPercent = Double.parseDouble(rsTax.getString(5));
	    taxOnGD = rsTax.getString(7);
	    taxCal = rsTax.getString(8);
	    taxIndicator = rsTax.getString(9);
	    taxOnTax = rsTax.getString(13);
	    taxOnTaxCode = rsTax.getString(14);
	    taxableAmount = 0.00;
	    taxCalAmt = 0.00;
	    String sql_TaxOn = "select strAreaCode,strOperationType,strItemType "
		    + "from tbltaxhd where strTaxCode='" + taxCode + "'";
	    ResultSet rsTaxOn = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxOn);
	    if (rsTaxOn.next())
	    {
		area = rsTaxOn.getString(1);
		opType = rsTaxOn.getString(2);
		itemType = rsTaxOn.getString(3);
	    }
	    if (funCheckAreaCode(billedAreaCode, area))
	    {
		if (funCheckOperationType(opType, operationType))
		{
		    if (funFindSettlementForTax(taxCode, settlementCode))
		    {
			boolean flgTaxOnGrpApplicable = false;
			taxableAmount = 0;
			listTax = new ArrayList<Object>();

			if (taxOnGD.equals("Gross"))
			{
			    //to calculate tax on group of an item
			    for (int i = 0; i < arrListItemDtl.size(); i++)
			    {
				String itemCode = arrListItemDtl.get(i);
				String itemDetail = mapMainGridItemDetail.get(itemCode);
				String itemDetailArray[] = itemDetail.split("!");
				String itemName = itemDetailArray[0];
				String itemQty = itemDetailArray[1];
				double itemAmt = Double.parseDouble(itemDetailArray[2]);
				double itemDisc = Double.parseDouble(itemDetailArray[3]);

				boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, itemCode);
				if (isApplicable)
				{
				    flgTaxOnGrpApplicable = true;
				    taxableAmount = taxableAmount + itemAmt;

				    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
				    {
					taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, itemAmt, itemDisc, billedAreaCode, operationType, settlementCode);
				    }
				}
			    }

//                            if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
//                            {
//                                taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, arrListTaxDtl);
//                            }
			}
			else
			{
			    subTotalForTax = 0;
			    discAmt = 0;
			    for (String itemCode : arrListItemDtl)
			    {

				String itemDetail = mapMainGridItemDetail.get(itemCode);
				String itemDetailArray[] = itemDetail.split("!");
				String itemName = itemDetailArray[0];
				String itemQty = itemDetailArray[1];
				double itemAmt = Double.parseDouble(itemDetailArray[2]);
				double itemDisc = Double.parseDouble(itemDetailArray[3]);

				boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, itemCode);
				if (isApplicable)
				{
				    flgTaxOnGrpApplicable = true;
				    if (itemDisc > 0)
				    {
					discAmt += itemDisc;
				    }
				    taxableAmount = taxableAmount + itemAmt;

				    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
				    {
					taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, itemAmt, itemDisc, billedAreaCode, operationType, settlementCode);
				    }
				}
			    }
			    if (taxableAmount > 0)
			    {
				taxableAmount = taxableAmount - discAmt;
			    }
//                            if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
//                            {
//                                taxableAmount += funGetTaxableAmountForTaxOnTax(taxOnTaxCode, arrListTaxDtl);
//                            }
			}
			if (flgTaxOnGrpApplicable)
			{
			    if (taxCal.equals("Forward")) // Forward Tax Calculation
			    {
				taxCalAmt = taxableAmount * (taxPercent / 100);
			    }
			    else // Backward Tax Calculation
			    {
				taxCalAmt = taxableAmount - (taxableAmount * 100 / (100 + taxPercent));
			    }

			    listTax.add(taxCode);
			    listTax.add(taxName);
			    listTax.add(taxableAmount);
			    listTax.add(taxCalAmt);
			    listTax.add(taxCal);
			    arrListTaxCal.add(listTax);
			}
		    }
		}
	    }
	}
	return arrListTaxCal;
    }

    private void funAddTaxDetail(DefaultTableModel dtm, double subTotal, List<ArrayList<Object>> listTax, double discount)
    {
	try
	{
	    Object object1[] =
	    {
		"", "", "", ""
	    };
	    dtm.addRow(object1);
	    Object object2[] =
	    {
		"SubTotal", "", subTotal, ""
	    };
	    dtm.addRow(object2);
	    // double contibuteDiscount=discount/itemTypeCount;
	    Object object3[] =
	    {
		"Discount", "", discount, ""
	    };
	    dtm.addRow(object3);
	    double totalTaxAmount = 0.00;
	    for (int i = 0; i < listTax.size(); i++)
	    {
		ArrayList<Object> tax = listTax.get(i);
		String taxDescription = tax.get(1).toString();
		double taxAmt = (double) tax.get(3);
		totalTaxAmount = totalTaxAmount + taxAmt;
		Object taxRow[] =
		{
		    taxDescription, "", taxAmt, ""
		};
		dtm.addRow(taxRow);
	    }
	    totalTaxAmount = Math.rint(totalTaxAmount);
	    double grandTotal = subTotal + totalTaxAmount - discount;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
	    grandTotal = mapRoundOff.get("roundOffAmt");
	    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt

	    Object grandTotalAmount[] =
	    {
		"GrandTotal", "", grandTotal, ""
	    };
	    dtm.addRow(grandTotalAmount);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertBillDiscDtlForManualBillSplit(String newBillNo, String oldBillNo, List<frmSplitBill> listGridSplitedItems)
    {
	try
	{
	    String sqlOldDisc = "select * from tblbilldiscdtl a where a.strBillNo='" + oldBillNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsOldBillDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlOldDisc);
	    StringBuilder sqlBillDiscDtl = new StringBuilder();
	    while (rsOldBillDisc.next())
	    {
		String discOnType = rsOldBillDisc.getString(6);
		String discOnValue = rsOldBillDisc.getString(7);
		double discPer = rsOldBillDisc.getDouble(4);
		String reasonCode = rsOldBillDisc.getString(8);
		String remarks = rsOldBillDisc.getString(9);
		if (discOnType.equalsIgnoreCase("ItemWise"))
		{
		    frmSplitBill objHdItemDtl = null;
		    for (int i = 0; i < listGridSplitedItems.size(); i++)
		    {
			if (listGridSplitedItems.get(i).cls_ItemName.equalsIgnoreCase(discOnValue))
			{
			    objHdItemDtl = listGridSplitedItems.get(i);
			    break;
			}
		    }
		    double dblDiscOnAmt = 0.00, dblDiscAmt = 0.00;
		    for (int i = 0; i < listGridSplitedItems.size(); i++)
		    {
			frmSplitBill objDtlItemDtl = listGridSplitedItems.get(i);
			if (objHdItemDtl.cls_ItemCode.equals(objDtlItemDtl.cls_ItemCode.substring(0, 7)))
			{
			    dblDiscOnAmt += objDtlItemDtl.cls_Amt;
			}
		    }
		    dblDiscAmt = (discPer / 100) * dblDiscOnAmt;
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
		    sqlBillDiscDtl.append("('" + newBillNo + "','" + clsGlobalVarClass.gPOSCode + "','" + dblDiscAmt + "','" + discPer + "','" + dblDiscOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reasonCode + "','" + remarks + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    if (dblDiscAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		}
		else if (discOnType.equalsIgnoreCase("Total"))
		{
		    double dblDiscOnAmt = 0.00, dblDiscAmt = 0.00;
		    for (int i = 0; i < listGridSplitedItems.size(); i++)
		    {
			frmSplitBill objDtlItemDtl = listGridSplitedItems.get(i);
			dblDiscOnAmt += objDtlItemDtl.cls_Amt;
		    }
		    dblDiscAmt = (discPer / 100) * dblDiscOnAmt;
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
		    sqlBillDiscDtl.append("('" + newBillNo + "','" + clsGlobalVarClass.gPOSCode + "','" + dblDiscAmt + "','" + discPer + "','" + dblDiscOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reasonCode + "','" + remarks + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    if (dblDiscAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		}
		else if (discOnType.equalsIgnoreCase("GroupWise"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemCode,a.strItemName,a.dblAmount,c.strSubGroupCode,c.strSubGroupName,d.strGroupCode,d.strGroupName "
			    + " from tblbilldtl a "
			    + " left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			    + " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			    + " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			    + " where b.strDiscountApply='Y' "
			    + " and d.strGroupName='" + discOnValue + "' "
			    + " and a.dblDiscountAmt>0 "
			    + " and strBillNo='" + oldBillNo + "' "
			    + " and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
		    ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    double dblDiscOnAmt = 0.00, dblDiscAmt = 0.00;
		    while (rsGroupWiseDisc.next())
		    {
			for (int i = 0; i < listGridSplitedItems.size(); i++)
			{
			    frmSplitBill objDtlItemDtl = listGridSplitedItems.get(i);
			    if (objDtlItemDtl.cls_ItemCode.equals(rsGroupWiseDisc.getString(1)))
			    {
				dblDiscOnAmt += objDtlItemDtl.cls_Amt;
				//add modifier dtl
				String sqlModi = "SELECT strItemCode,dblAmount "
					+ " FROM tblbillmodifierdtl "
					+ " WHERE strBillNo='" + oldBillNo + "' "
					+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
					+ " AND LEFT(strItemCode,7)='" + objDtlItemDtl.cls_ItemCode + "' ;";
				ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sqlModi);
				while (rsModi.next())
				{
				    dblDiscOnAmt += rsModi.getDouble(2);
				}
			    }
			}
		    }
		    dblDiscAmt = (discPer / 100) * dblDiscOnAmt;
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
		    sqlBillDiscDtl.append("('" + newBillNo + "','" + clsGlobalVarClass.gPOSCode + "','" + dblDiscAmt + "','" + discPer + "','" + dblDiscOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reasonCode + "','" + remarks + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    if (dblDiscAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		}
		else if (discOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemCode,a.strItemName,a.dblAmount,c.strSubGroupCode,c.strSubGroupName,d.strGroupCode,d.strGroupName "
			    + " from tblbilldtl a "
			    + " left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			    + " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			    + " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			    + " where b.strDiscountApply='Y' "
			    + " and c.strSubGroupName='" + discOnValue + "' "
			    + " and a.dblDiscountAmt>0 "
			    + " and strBillNo='" + oldBillNo + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
		    ResultSet rsSubGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    double dblDiscOnAmt = 0.00, dblDiscAmt = 0.00;
		    while (rsSubGroupWiseDisc.next())
		    {
			for (int i = 0; i < listGridSplitedItems.size(); i++)
			{
			    frmSplitBill objDtlItemDtl = listGridSplitedItems.get(i);
			    if (objDtlItemDtl.cls_ItemCode.equals(rsSubGroupWiseDisc.getString(1)))
			    {
				dblDiscOnAmt += objDtlItemDtl.cls_Amt;
				//add modifier dtl
				String sqlModi = "SELECT strItemCode,dblAmount "
					+ " FROM tblbillmodifierdtl "
					+ " WHERE strBillNo='" + oldBillNo + "' "
					+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
					+ " AND LEFT(strItemCode,7)='" + objDtlItemDtl.cls_ItemCode + "' ;";
				ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sqlModi);
				while (rsModi.next())
				{
				    dblDiscOnAmt += rsModi.getDouble(2);
				}
			    }
			}
		    }
		    dblDiscAmt = (discPer / 100) * dblDiscOnAmt;
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
		    sqlBillDiscDtl.append("('" + newBillNo + "','" + clsGlobalVarClass.gPOSCode + "','" + dblDiscAmt + "','" + discPer + "','" + dblDiscOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reasonCode + "','" + remarks + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    if (dblDiscAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateKOTWiseItemList()
    {
	try
	{
	    mapKOTWiseItemList = new HashMap<String, List<clsBillDtl>>();
	    sql = "select * from tblbilldtl a "
		    + " where a.strBillNo='" + voucherNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + " order by a.strKOTNo desc ";
	    ResultSet rsItemDetalis = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemDetalis.next())
	    {
		if (mapKOTWiseItemList.containsKey(rsItemDetalis.getString(1)))
		{
		    List<clsBillDtl> kotWiseItemListForManualSplit = mapKOTWiseItemList.get(rsItemDetalis.getString(1));
		    clsBillDtl objBillDtl = new clsBillDtl();
		    objBillDtl.setStrItemCode(rsItemDetalis.getString(1));
		    objBillDtl.setStrItemName(rsItemDetalis.getString(2));
		    objBillDtl.setStrBillNo(rsItemDetalis.getString(3));
		    objBillDtl.setStrAdvBookingNo(rsItemDetalis.getString(4));
		    objBillDtl.setDblRate(rsItemDetalis.getDouble(5));
		    objBillDtl.setDblQuantity(rsItemDetalis.getDouble(6));
		    objBillDtl.setDblAmount(rsItemDetalis.getDouble(5) * rsItemDetalis.getDouble(6));//rate * qty becose if  promotion bill
		    objBillDtl.setDblTaxAmount(rsItemDetalis.getDouble(8));
		    objBillDtl.setDteBillDate(rsItemDetalis.getString(9));
		    objBillDtl.setStrKOTNo(rsItemDetalis.getString(10));
		    objBillDtl.setStrClientCode(rsItemDetalis.getString(11));
		    objBillDtl.setStrCustomerCode(rsItemDetalis.getString(12));
		    objBillDtl.setTmeOrderProcessing(rsItemDetalis.getString(13));
		    objBillDtl.setStrDataPostFlag(rsItemDetalis.getString(14));
		    objBillDtl.setStrMMSDataPostFlag(rsItemDetalis.getString(15));
		    objBillDtl.setStrManualKOTNo(rsItemDetalis.getString(16));
		    objBillDtl.setTdhYN(rsItemDetalis.getString(17));
		    objBillDtl.setStrPromoCode(rsItemDetalis.getString(18));
		    objBillDtl.setStrCounterCode(rsItemDetalis.getString(19));
		    objBillDtl.setStrWaiterNo(rsItemDetalis.getString(20));
		    objBillDtl.setDblDiscountAmt(rsItemDetalis.getDouble(21));
		    objBillDtl.setDblDiscountPer(rsItemDetalis.getDouble(22));
		    objBillDtl.setSequenceNo(rsItemDetalis.getString(23));
		    kotWiseItemListForManualSplit.add(objBillDtl);
		}
		else
		{
		    List<clsBillDtl> kotWiseItemListForManualSplit = new LinkedList<clsBillDtl>();
		    clsBillDtl objBillDtl = new clsBillDtl();
		    objBillDtl.setStrItemCode(rsItemDetalis.getString(1));
		    objBillDtl.setStrItemName(rsItemDetalis.getString(2));
		    objBillDtl.setStrBillNo(rsItemDetalis.getString(3));
		    objBillDtl.setStrAdvBookingNo(rsItemDetalis.getString(4));
		    objBillDtl.setDblRate(rsItemDetalis.getDouble(5));
		    objBillDtl.setDblQuantity(rsItemDetalis.getDouble(6));
		    objBillDtl.setDblAmount(rsItemDetalis.getDouble(5) * rsItemDetalis.getDouble(6));//rate * qty becose if  promotion bill
		    objBillDtl.setDblTaxAmount(rsItemDetalis.getDouble(8));
		    objBillDtl.setDteBillDate(rsItemDetalis.getString(9));
		    objBillDtl.setStrKOTNo(rsItemDetalis.getString(10));
		    objBillDtl.setStrClientCode(rsItemDetalis.getString(11));
		    objBillDtl.setStrCustomerCode(rsItemDetalis.getString(12));
		    objBillDtl.setTmeOrderProcessing(rsItemDetalis.getString(13));
		    objBillDtl.setStrDataPostFlag(rsItemDetalis.getString(14));
		    objBillDtl.setStrMMSDataPostFlag(rsItemDetalis.getString(15));
		    objBillDtl.setStrManualKOTNo(rsItemDetalis.getString(16));
		    objBillDtl.setTdhYN(rsItemDetalis.getString(17));
		    objBillDtl.setStrPromoCode(rsItemDetalis.getString(18));
		    objBillDtl.setStrCounterCode(rsItemDetalis.getString(19));
		    objBillDtl.setStrWaiterNo(rsItemDetalis.getString(20));
		    objBillDtl.setDblDiscountAmt(rsItemDetalis.getDouble(21));
		    objBillDtl.setDblDiscountPer(rsItemDetalis.getDouble(22));
		    objBillDtl.setSequenceNo(rsItemDetalis.getString(23));
		    kotWiseItemListForManualSplit.add(objBillDtl);
		    mapKOTWiseItemList.put(rsItemDetalis.getString(1), kotWiseItemListForManualSplit);
		}
	    }
	    rsItemDetalis.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funInsertBillDtlForManualSplit(String oldBillNo, String newBillNo, List<clsBillDtl> listOfBillItems, List<frmSplitBill> listOfBillModifiers)
    {
	try
	{
	    StringBuilder sqlBuilder = new StringBuilder();
	    //insert bill dtl
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("insert into tblbilldtl(strItemCode,strItemName,strBillNo,strAdvBookingNo,dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBillDate,"
		    + "strKOTNo,strClientCode,strCustomerCode,tmeOrderProcessing,"
		    + "strDataPostFlag,strMMSDataPostFlag,strManualKOTNo,tdhYN,dblDiscountAmt,dblDiscountPer,dtBillDate)values");
	    for (int i = 0; i < listOfBillItems.size(); i++)
	    {
		clsBillDtl objBillDtl = listOfBillItems.get(i);
		if (i == 0)
		{
		    sqlBuilder.append("('" + objBillDtl.getStrItemCode() + "','" + objBillDtl.getStrItemName() + "','" + newBillNo + "','" + objBillDtl.getStrAdvBookingNo() + "','" + objBillDtl.getDblRate() + "','" + objBillDtl.getDblQuantity() + "',"
			    + "'" + objBillDtl.getDblAmount() + "','" + objBillDtl.getDblTaxAmount() + "','" + objBillDtl.getDteBillDate() + "','" + objBillDtl.getStrKOTNo() + "',"
			    + "'" + objBillDtl.getStrClientCode() + "','" + objBillDtl.getStrCustomerName() + "',"
			    + "'" + objBillDtl.getTmeOrderProcessing() + "','N','N','" + objBillDtl.getStrManualKOTNo() + "','" + objBillDtl.getTdhYN() + "',"
			    + "'" + objBillDtl.getDblDiscountAmt() + "','" + objBillDtl.getDblDiscountPer() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "')");
		}
		else
		{
		    sqlBuilder.append(",('" + objBillDtl.getStrItemCode() + "','" + objBillDtl.getStrItemName() + "','" + newBillNo + "','" + objBillDtl.getStrAdvBookingNo() + "','" + objBillDtl.getDblRate() + "','" + objBillDtl.getDblQuantity() + "',"
			    + "'" + objBillDtl.getDblAmount() + "','" + objBillDtl.getDblTaxAmount() + "','" + objBillDtl.getDteBillDate() + "','" + objBillDtl.getStrKOTNo() + "',"
			    + "'" + objBillDtl.getStrClientCode() + "','" + objBillDtl.getStrCustomerName() + "',"
			    + "'" + objBillDtl.getTmeOrderProcessing() + "','N','N','" + objBillDtl.getStrManualKOTNo() + "','" + objBillDtl.getTdhYN() + "',"
			    + "'" + objBillDtl.getDblDiscountAmt() + "','" + objBillDtl.getDblDiscountPer() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "')");
		}
	    }
	    if (listOfBillItems.size() > 0)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
	    }
	    //insert bill modifier dtl     
	    for (int i = 0; i < listOfBillModifiers.size(); i++)
	    {
		frmSplitBill objModifierDtl = listOfBillModifiers.get(i);
		String sql_Insert = "INSERT INTO tblbillmodifierdtl (strBillNo,strItemCode,strModifierCode,strModifierName,dblRate,dblQuantity,dblAmount"
			+ " ,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
			+ " (select '" + newBillNo + "',strItemCode,strModifierCode,strModifierName,dblRate,'" + objModifierDtl.cls_qty + "','" + objModifierDtl.cls_Amt + "' "
			+ " ,strClientCode,dblDiscPer,'" + objModifierDtl.discountAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			+ " from tblbillmodifierdtl "
			+ " where strBillNo='" + oldBillNo + "' "
			+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'  "
			+ " and strItemCode='" + objModifierDtl.cls_ItemCode + "' "
			+ " and strModifierName='" + objModifierDtl.cls_ItemName + "'  ) ";
		clsGlobalVarClass.dbMysql.execute(sql_Insert);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private Map<String, clsPromotionItems> funCalculatePromotionForManualSplit(List<frmSplitBill> listOfSplitedGrid)
    {
	Map<String, clsPromotionItems> hmPromoItemDtl = null;
	try
	{
	    List<clsBillDtl> listOfSplitedGridItems = new LinkedList<>();
	    for (frmSplitBill objSplitedItem : listOfSplitedGrid)
	    {
		clsBillDtl objItemDtl = new clsBillDtl();

		objItemDtl.setStrItemCode(objSplitedItem.cls_ItemCode);
		objItemDtl.setStrItemName(objSplitedItem.cls_ItemName);
		objItemDtl.setDblQuantity(objSplitedItem.cls_qty);
		objItemDtl.setDblAmount(objSplitedItem.cls_Amt);
		objItemDtl.setDblDiscountAmt(objSplitedItem.discountAmt);

		listOfSplitedGridItems.add(objItemDtl);
	    }

	    if (clsGlobalVarClass.gActivePromotions)
	    {
		frmBillSettlement objBillSettlement = new frmBillSettlement("SplitBill");
		objBillSettlement.objUtility = new clsUtility();

		clsCalculateBillPromotions objCalculateBillPromotions = new clsCalculateBillPromotions(objBillSettlement);

		hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("SplitBill", "", "", listOfSplitedGridItems);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return hmPromoItemDtl;
	}
    }

    private void funInsertBillPromotionDtlForManualSplit(String oldBillNo, String newBillNo, List<clsBillDtl> listOfBillItems, String table)
    {
	try
	{
	    boolean isPromotionApply = false;
	    StringBuilder sqlBuilder = new StringBuilder();
	    //insert bill promotion dtl
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("insert into tblbillpromotiondtl(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate)values");
	    if (hmPromoItem.size() > 0)
	    {
		String values = "";
		for (int i = 0; i < listOfBillItems.size(); i++)
		{
		    clsBillDtl objBillDtl = listOfBillItems.get(i);
		    if (hmPromoItem.containsKey(table + "!" + objBillDtl.getStrItemCode()))
		    {
			clsPromotionItems objPromotionItem = hmPromoItem.get(table + "!" + objBillDtl.getStrItemCode());
			double freeQty = objPromotionItem.getFreeItemQty();
			double rate = objBillDtl.getDblRate();
			double freeAmt = freeQty * rate;

			values += ",('" + newBillNo + "','" + objPromotionItem.getItemCode() + "','" + objPromotionItem.getPromoCode() + "','" + freeQty + "'"
				+ ",'" + rate + "','" + clsGlobalVarClass.gClientCode + "','N','" + objPromotionItem.getPromoType() + "','" + freeAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";

			hmPromoItem.remove(table + "!" + objBillDtl.getStrItemCode());
		    }
		}
		if (values.length() > 0)
		{
		    isPromotionApply = true;
		    values = values.substring(1, values.length());
		    sqlBuilder.append(values);
		}
	    }
	    if (isPromotionApply)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean isTaxApplicableOnItemGroup(String taxCode, String itemCode)
    {
	boolean isApplicable = false;

	try
	{
	    String sql = "select a.strItemCode,a.strItemName,b.strSubGroupCode,b.strSubGroupName,c.strGroupCode,c.strGroupName,d.strTaxCode,d.strApplicable "
		    + "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c,tbltaxongroup d "
		    + "where a.strSubGroupCode=b.strSubGroupCode "
		    + "and b.strGroupCode=c.strGroupCode "
		    + "and c.strGroupCode=d.strGroupCode "
		    + "and a.strItemCode='" + itemCode.substring(0, 7) + "' "
		    + "and d.strTaxCode='" + taxCode + "' "
		    + "and d.strApplicable='true' ";
	    ResultSet rsTaxApplicable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTaxApplicable.next())
	    {
		isApplicable = true;
	    }
	    rsTaxApplicable.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return isApplicable;
	}
    }

    //new logic for tax on tax
    private double funGetTaxableAmountForTaxOnTax(String taxOnTaxCode, double itemAmt, double itemDisc, String billAreaCode, String operationTypeForTax, String settlementCode) throws Exception
    {
	//0 code
	//1 name
	//2 qty
	//3 amt
	//4 discAmt
	double taxAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	String opType = "", taxAreaCodes = "";

	for (int t = 0; t < spTaxOnTaxCode.length; t++)
	{

	    String sqlTaxOn = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent "
		    + ",a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType "
		    + ",a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
		    + "from tbltaxhd a,tbltaxposdtl b "
		    + "where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(a.dteValidFrom) <='" + dtPOSDate + "' "
		    + "and date(a.dteValidTo)>='" + dtPOSDate + "' "
		    + "and a.strTaxOnSP='Sales' "
		    + "and  a.strTaxCode='" + spTaxOnTaxCode[t] + "' ";
	    ResultSet rsTaxOn = clsGlobalVarClass.dbMysql.executeResultSet(sqlTaxOn);
	    if (rsTaxOn.next())
	    {
		taxAreaCodes = rsTaxOn.getString(10);
		opType = rsTaxOn.getString(11);

		if (funCheckAreaCode(taxAreaCodes, billAreaCode))
		{
		    if (funCheckOperationType(opType, operationTypeForTax))
		    {
			if (funFindSettlementForTax(spTaxOnTaxCode[t], settlementCode))
			{

			    String sqlTaxOnTax = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent,a.dblAmount,a.dteValidFrom,a.dteValidTo,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator "
				    + ",a.strTaxRounded,a.strTaxOnTax,a.strTaxOnTaxCode "
				    + "from tbltaxhd a "
				    + "where a.strTaxCode='" + spTaxOnTaxCode[t] + "' ";
			    ResultSet rsTaxOnTax = clsGlobalVarClass.dbMysql.executeResultSet(sqlTaxOnTax);
			    if (rsTaxOnTax.next())
			    {
				String taxCode = rsTaxOnTax.getString(1);
				String taxName = rsTaxOnTax.getString(2);
				String taxOnGD = rsTaxOnTax.getString(7);
				String taxCal = rsTaxOnTax.getString(8);
				String taxIndicator = rsTaxOnTax.getString(9);
				String taxOnTax = rsTaxOnTax.getString(13);
				//String taxOnTaxCode = rsTaxOnTax.getString(14);
				double taxPercent = Double.parseDouble(rsTaxOnTax.getString(5));

				if (taxOnGD.equals("Gross"))
				{
				    taxAmt += (taxPercent / 100) * itemAmt;
				}
				else//discount
				{
				    taxAmt += (taxPercent / 100) * (itemAmt - itemDisc);
				}
			    }
			    rsTaxOnTax.close();

			}
		    }
		}
	    }
	}
	return taxAmt;
    }
}
