package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsBillHd;
import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsBillModifierDtl;
import com.POSGlobal.controller.clsBillTaxDtl;
import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.controller.clsVoidBillDtl;
import com.POSGlobal.controller.clsVoidBillHd;
import com.POSGlobal.controller.clsVoidBillModifierDtl;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmUserAuthenticationPopUp;
import com.POSTransaction.controller.clsBillDiscountDtl;
import com.POSTransaction.controller.clsCalculateBillPromotions;
import com.POSTransaction.controller.clsPromotionItems;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmVoidBill extends javax.swing.JFrame
{

    ResultSet rs, rs1;
    private String sql, voidBillDate;
    private String[] reason;
    private String reasoncode, dtPOSDate;
    private String areaCode, operationTypeForTax;
    private int selectedBillNoRow;
    private int reasoncount;
    //private double selectedVoidQty, voidedItemQty;
    private List<clsTaxCalculationDtls> arrListTaxCal;
    private List<clsBillDtl> arrListBillDtl;
    private List<clsBillDtl> arrListKOTWiseBillDtl;
    private List<clsBillHd> arrListBillHd;
    private List<clsBillModifierDtl> arrListBillModifierDtl;
    private List<clsBillTaxDtl> arrListBillTaxDtl;
    private List<clsBillDiscountDtl> arrListBillDiscDtl;
    private List<clsVoidBillDtl> arrListVoidBillDtl;
    private List<clsVoidBillHd> arrListVoidBillHd;
    private List<clsVoidBillModifierDtl> arrListVoidBillModifierDtl;
    private clsUtility objUtility = new clsUtility();
    private clsUtility2 objUtility2 = new clsUtility2();
    private int intShiftNo;
    private String transactionUserCode = null;
    private String buttonClicked;
    private StringBuilder sqlBuilder;
    private Map<String, clsBillItemDtl> hmBillItemDtl = new HashMap<String, clsBillItemDtl>();
    private HashMap<String, clsSettelementOptions> hmSettlemetnOptions = new HashMap<>();
    String userCode = clsGlobalVarClass.gUserCode;
    String sqlQuery = null;
    String billno = "";
    private Map<String, clsVoidBillDtl> mapItemToBeDeleted;
    private Map<String, clsVoidBillModifierDtl> mapItemModifierToBeDeleted;
    private boolean isAuditing = false;

    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public frmVoidBill()
    {

	try
	{
	    initComponents();
	    txtSearch.requestFocus(true);
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
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();

	    sqlBuilder = new StringBuilder();

	    String tbluserdtl = "tbluserdtl";
	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
	    {
		tbluserdtl = "tblsuperuserdtl";
	    }
	    else
	    {
		tbluserdtl = "tbluserdtl";
	    }
	    sqlQuery = "select strAuditing from " + tbluserdtl + " where strUserCode='" + userCode + "' and strFormName='Void Bill'";

	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
	    dtPOSDate = date1;
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    arrListTaxCal = new ArrayList<clsTaxCalculationDtls>();
	    arrListBillDtl = new ArrayList<clsBillDtl>();
	    arrListKOTWiseBillDtl = new ArrayList<clsBillDtl>();
	    arrListBillHd = new ArrayList<clsBillHd>();
	    arrListBillModifierDtl = new ArrayList<clsBillModifierDtl>();
	    arrListBillTaxDtl = new ArrayList<clsBillTaxDtl>();
	    arrListBillDiscDtl = new ArrayList<clsBillDiscountDtl>();

	    arrListVoidBillHd = new ArrayList<clsVoidBillHd>();
	    arrListVoidBillDtl = new ArrayList<clsVoidBillDtl>();
	    arrListVoidBillModifierDtl = new ArrayList<clsVoidBillModifierDtl>();

	    mapItemToBeDeleted = new HashMap<String, clsVoidBillDtl>();
	    mapItemModifierToBeDeleted = new HashMap<String, clsVoidBillModifierDtl>();

	    List<clsTaxCalculationDtls> arrListTaxCal = new ArrayList<clsTaxCalculationDtls>();
	    funFillBillNoGrid("");

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillBillNoGrid(String searchBillNo)
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
	    dm.addColumn("Time");
	    //dm.addColumn("Settle Mode");
	    dm.addColumn("Amount");
	    dm.addColumn("Table");

	    sql = "select a.strBillNo,TIME_FORMAT(time(a.dteBillDate),'%h:%i') as dteBillDate,a.strSettelmentMode,a.dblTaxAmt,a.dblSubTotal,a.dblGrandTotal"
		    + ",a.strUserCreated,b.strTableName "
		    + " from tblbillhd a left outer join tbltablemaster b "
		    + " on a.strTableNo=b.strTableNo "
		    + " where date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and a.strBillNo NOT IN(select b.strBillNo from tblbillsettlementdtl b) ";
	    if (searchBillNo.length() > 0)
	    {
		sql += " and a.strBillNo LIKE '" + searchBillNo + "%'  or a.strBillNo LIKE '%" + searchBillNo + "' ";
	    }

	    sql += "  and a.strSettelmentMode='' ";

	    sql += " order by TIME_FORMAT(time(a.dteBillDate),'%h:%i') desc ";

	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		Object[] rows
			=
			{
			    rs.getString(1), rs.getString(2), gDecimalFormat.format(rs.getDouble(6)), rs.getString(8)
			};
		dm.addRow(rows);
	    }
	    tblBillDetails.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblBillDetails.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

	    tblBillDetails.getColumnModel().getColumn(0).setPreferredWidth(70);
	    tblBillDetails.getColumnModel().getColumn(1).setPreferredWidth(90);
	    tblBillDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblBillDetails.getColumnModel().getColumn(3).setPreferredWidth(50);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funSelectBill()
    {
	try
	{
	    mapItemToBeDeleted.clear();
	    mapItemModifierToBeDeleted.clear();
	    double totalQtyCount = 0;

	    selectedBillNoRow = tblBillDetails.getSelectedRow();
	    String billNo = tblBillDetails.getModel().getValueAt(selectedBillNoRow, 0).toString();
	    lblVoucherNo.setText(billNo);
	    sql = "select a.strOperationType,a.strTableNo,ifnull(b.strAreaCode,'') "
		    + "from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + "where strBillNo='" + billNo + "' ";
	    ResultSet rsBillInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillInfo.next())
	    {
		operationTypeForTax = "DineIn";
		if (rsBillInfo.getString(1).equals("HomeDelivery"))
		{
		    operationTypeForTax = "HomeDelivery";
		}
		if (rsBillInfo.getString(1).equals("TakeAway"))
		{
		    operationTypeForTax = "TakeAway";
		}
		if (rsBillInfo.getString(2).trim().length() > 0)
		{
		    areaCode = rsBillInfo.getString(3);
		}
		else
		{
		    areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}
	    }
	    rsBillInfo.close();

	    arrListBillDtl.clear();
	    arrListBillHd.clear();
	    arrListBillModifierDtl.clear();
	    arrListVoidBillHd.clear();
	    arrListVoidBillDtl.clear();
	    arrListVoidBillModifierDtl.clear();
	    arrListBillDiscDtl.clear();
	    arrListKOTWiseBillDtl.clear();

	    String sql = "select * from tblbillhd "
		    + "where strBillNo='" + lblVoucherNo.getText() + "'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsBillHd objBillHd = new clsBillHd();
		objBillHd.setStrBillNo(rs.getString(1));
		objBillHd.setStrAdvBookingNo(rs.getString(2));
		objBillHd.setDteBillDate(rs.getString(3));
		objBillHd.setStrPOSCode(rs.getString(4));
		objBillHd.setStrSettelmentMode(rs.getString(5));
		objBillHd.setDblDiscountAmt(rs.getDouble(6));
		objBillHd.setDblDiscountPer(rs.getDouble(7));
		objBillHd.setDblTaxAmt(rs.getDouble(8));
		objBillHd.setDblSubTotal(rs.getDouble(9));
		objBillHd.setDblGrandTotal(rs.getDouble(10));
		objBillHd.setStrTakeAway(rs.getString(11));
		objBillHd.setStrOperationType(rs.getString(12));
		objBillHd.setStrUserCreated(rs.getString(13));
		objBillHd.setStrUserEdited(rs.getString(14));
		objBillHd.setDteDateCreated(rs.getString(15));
		objBillHd.setDteDateEdited(rs.getString(16));
		objBillHd.setStrClientCode(rs.getString(17));
		objBillHd.setStrTableNo(rs.getString(18));
		objBillHd.setStrWaiterNo(rs.getString(19));
		objBillHd.setStrCustomerCode(rs.getString(20));
		objBillHd.setStrManualBillNo(rs.getString(21));
		objBillHd.setIntShiftCode(rs.getInt(22));
		intShiftNo = rs.getInt(22);
		objBillHd.setIntPaxNo(rs.getInt(23));
		objBillHd.setStrDataPostFlag(rs.getString(24));
		objBillHd.setStrReasonCode(rs.getString(25));
		objBillHd.setStrRemarks(rs.getString(26));
		objBillHd.setDblTipAmount(rs.getDouble(27));
		objBillHd.setDteSettleDate(rs.getString(28));
		objBillHd.setStrCounterCode(rs.getString(29));
		objBillHd.setDblDeliveryCharges(rs.getDouble(30));
		objBillHd.setStrCouponCode(rs.getString(31));
		objBillHd.setStrAreaCode(rs.getString(32));
		objBillHd.setStrDiscountRemark(rs.getString(33));
		objBillHd.setStrTakeAwayRemarks(rs.getString(34));
		objBillHd.setStrDiscountOn(rs.getString(35));
		objBillHd.setDblGrandTotalRoundOffBy(rs.getDouble(44));
		objBillHd.setIntLastOrderNo(rs.getInt(47));
		arrListBillHd.add(objBillHd);
	    }
	    rs.close();

	    sql = "select a.strItemCode,a.strItemName,a.strBillNo,a.strAdvBookingNo,a.dblRate,sum(a.dblQuantity) "
		    + ",sum(a.dblAmount),sum(a.dblTaxAmount),a.dteBillDate,a.strKOTNo,a.strClientCode,a.strCustomerCode "
		    + ",a.tmeOrderProcessing,a.strDataPostFlag,a.strMMSDataPostFlag,a.strManualKOTNo,a.tdhYN "
		    + ",a.strPromoCode,a.strCounterCode,a.strWaiterNo,a.dblDiscountAmt,a.dblDiscountPer,b.strSubGroupCode "
		    + ",c.strSubGroupName,c.strGroupCode,d.strGroupName "
		    + "from tblbilldtl a,tblitemmaster b ,tblsubgrouphd c,tblgrouphd d "
		    + "where a.strBillNo='" + billNo + "'  "
		    + "and a.strItemCode=b.strItemCode "
		    + "and b.strSubGroupCode=c.strSubGroupCode "
		    + "and c.strGroupCode=d.strGroupCode "
		    + "group by a.strItemCode,a.strItemName,a.strBillNo;";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsBillDtl objBillDtl = new clsBillDtl();
		String itemCode = rs.getString(1);
		objBillDtl.setStrItemCode(rs.getString(1));
		objBillDtl.setStrItemName(rs.getString(2));
		objBillDtl.setStrBillNo(rs.getString(3));
		objBillDtl.setStrAdvBookingNo(rs.getString(4));
		objBillDtl.setDblRate(rs.getDouble(5));
		objBillDtl.setDblQuantity(rs.getDouble(6));
		objBillDtl.setDblAmount(rs.getDouble(7));
		objBillDtl.setDblTaxAmount(rs.getDouble(8));
		objBillDtl.setDteBillDate(rs.getString(9));
		objBillDtl.setStrKOTNo(rs.getString(10));
		objBillDtl.setStrClientCode(rs.getString(11));
		objBillDtl.setStrCustomerCode(rs.getString(12));
		objBillDtl.setTmeOrderProcessing(rs.getString(13));
		objBillDtl.setStrDataPostFlag(rs.getString(14));
		objBillDtl.setStrMMSDataPostFlag(rs.getString(15));
		objBillDtl.setStrManualKOTNo(rs.getString(16));
		objBillDtl.setTdhYN(rs.getString(17));
		objBillDtl.setStrPromoCode(rs.getString(18));
		objBillDtl.setStrCounterCode(rs.getString(19));
		objBillDtl.setStrWaiterNo(rs.getString(20));
		objBillDtl.setDblDiscountAmt(rs.getDouble(21));
		objBillDtl.setDblDiscountPer(rs.getDouble(22));
		objBillDtl.setSubGrouName(rs.getString(24));
		objBillDtl.setGroupName(rs.getString(26));
		arrListBillDtl.add(objBillDtl);

		double itemQty = objBillDtl.getDblQuantity();
		if (itemQty > 1.0)
		{
		    totalQtyCount = totalQtyCount + itemQty;
		}
		else
		{
		    totalQtyCount = totalQtyCount + 1.0;
		}

		sql = "select a.strBillNo,a.strItemCode,a.strModifierCode,a.strModifierName "
			+ ",a.dblRate,sum(a.dblQuantity),sum(a.dblAmount),a.strClientCode,a.strCustomerCode "
			+ ",a.strDataPostFlag,a.strMMSDataPostFlag,sum(a.dblDiscPer),sum(a.dblDiscAmt) "
			+ ",b.strSubGroupCode ,c.strSubGroupName,c.strGroupCode,d.strGroupName "
			+ "from tblbillmodifierdtl a,tblitemmaster b ,tblsubgrouphd c,tblgrouphd d "
			+ "where left(a.strItemCode,7)='" + itemCode + "'  "
			+ "and a.strBillNo='" + billNo + "' "
			+ "and left(a.strItemCode,7)=b.strItemCode "
			+ "and b.strSubGroupCode=c.strSubGroupCode "
			+ "and c.strGroupCode=d.strGroupCode  "
			+ "group by a.strItemCode,a.strModifierName ";
		ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsModifier.next())
		{
		    clsBillModifierDtl objBillModDtl = new clsBillModifierDtl();
		    objBillModDtl.setStrBillNo(rsModifier.getString(1));
		    objBillModDtl.setStrItemCode(rsModifier.getString(2));
		    objBillModDtl.setStrModifierCode(rsModifier.getString(3));
		    objBillModDtl.setStrModifierName(rsModifier.getString(4));
		    objBillModDtl.setDblRate(rsModifier.getDouble(5));
		    objBillModDtl.setDblQuantity(rsModifier.getDouble(6));
		    objBillModDtl.setDblAmount(rsModifier.getDouble(7));
		    objBillModDtl.setStrClientCode(rsModifier.getString(8));
		    objBillModDtl.setStrCustomerCode(rsModifier.getString(9));
		    objBillModDtl.setStrDataPostFlag(rsModifier.getString(10));
		    objBillModDtl.setStrMMSDataPostFlag(rsModifier.getString(11));
		    objBillModDtl.setDblDiscPer(rsModifier.getDouble(12));
		    objBillModDtl.setDblDiscAmt(rsModifier.getDouble(13));
		    objBillModDtl.setSubGrouName(rsModifier.getString(15));
		    objBillModDtl.setGroupName(rsModifier.getString(17));
		    arrListBillModifierDtl.add(objBillModDtl);
		}
		rsModifier.close();
	    }
	    rs.close();

	    sql = "select a.strItemCode,a.strItemName,a.strBillNo,a.strAdvBookingNo,a.dblRate,sum(a.dblQuantity) "
		    + " ,sum(a.dblAmount),sum(a.dblTaxAmount),a.dteBillDate,a.strKOTNo,a.strClientCode,a.strCustomerCode "
		    + " ,a.tmeOrderProcessing,a.strDataPostFlag,a.strMMSDataPostFlag,a.strManualKOTNo,a.tdhYN "
		    + " ,a.strPromoCode,a.strCounterCode,a.strWaiterNo,a.dblDiscountAmt,a.dblDiscountPer,b.strSubGroupCode "
		    + " ,c.strSubGroupName,c.strGroupCode,d.strGroupName,a.tmeOrderPickup "
		    + " from tblbilldtl a,tblitemmaster b ,tblsubgrouphd c,tblgrouphd d "
		    + " where a.strBillNo='" + billNo + "'  "
		    + " and a.strItemCode=b.strItemCode "
		    + " and b.strSubGroupCode=c.strSubGroupCode "
		    + " and c.strGroupCode=d.strGroupCode "
		    + " group by a.strBillNo,a.strKOTNo,a.strItemCode,a.strItemName "
		    + " order by a.strKOTNo desc,a.strItemCode; ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsBillDtl objBillDtl = new clsBillDtl();
		objBillDtl.setStrItemCode(rs.getString(1));
		objBillDtl.setStrItemName(rs.getString(2));
		objBillDtl.setStrBillNo(rs.getString(3));
		objBillDtl.setStrAdvBookingNo(rs.getString(4));
		objBillDtl.setDblRate(rs.getDouble(5));
		objBillDtl.setDblQuantity(rs.getDouble(6));
		objBillDtl.setDblAmount(rs.getDouble(7));
		objBillDtl.setDblTaxAmount(rs.getDouble(8));
		objBillDtl.setDteBillDate(rs.getString(9));
		objBillDtl.setStrKOTNo(rs.getString(10));
		objBillDtl.setStrClientCode(rs.getString(11));
		objBillDtl.setStrCustomerCode(rs.getString(12));
		objBillDtl.setTmeOrderProcessing(rs.getString(13));
		objBillDtl.setStrDataPostFlag(rs.getString(14));
		objBillDtl.setStrMMSDataPostFlag(rs.getString(15));
		objBillDtl.setStrManualKOTNo(rs.getString(16));
		objBillDtl.setTdhYN(rs.getString(17));
		objBillDtl.setStrPromoCode(rs.getString(18));
		objBillDtl.setStrCounterCode(rs.getString(19));
		objBillDtl.setStrWaiterNo(rs.getString(20));
		objBillDtl.setDblDiscountAmt(rs.getDouble(21));
		objBillDtl.setDblDiscountPer(rs.getDouble(22));
		objBillDtl.setSubGrouName(rs.getString(24));
		objBillDtl.setGroupName(rs.getString(26));
		objBillDtl.setStrOrderPickupTime(rs.getString(27));
		arrListKOTWiseBillDtl.add(objBillDtl);
	    }
	    rs.close();

	    sql = "select * from tblbilldiscdtl where strBillNo='" + lblVoucherNo.getText() + "'";
	    ResultSet rsBillDiscDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillDiscDtl.next())
	    {
		clsBillDiscountDtl objBillDiscDtl = new clsBillDiscountDtl();
		objBillDiscDtl.setBillNo(rsBillDiscDtl.getString(1));
		objBillDiscDtl.setPOSCode(rsBillDiscDtl.getString(2));
		objBillDiscDtl.setDiscAmt(rsBillDiscDtl.getDouble(3));
		objBillDiscDtl.setDiscPer(rsBillDiscDtl.getDouble(4));
		objBillDiscDtl.setDiscOnAmt(rsBillDiscDtl.getDouble(5));
		objBillDiscDtl.setDiscOnType(rsBillDiscDtl.getString(6));
		objBillDiscDtl.setDiscOnValue(rsBillDiscDtl.getString(7));
		objBillDiscDtl.setReason(rsBillDiscDtl.getString(8));
		objBillDiscDtl.setRemark(rsBillDiscDtl.getString(9));
		objBillDiscDtl.setUserCreated(rsBillDiscDtl.getString(10));
		objBillDiscDtl.setUserEdited(rsBillDiscDtl.getString(11));
		objBillDiscDtl.setDateCreated(rsBillDiscDtl.getString(12));
		objBillDiscDtl.setDateEdited(rsBillDiscDtl.getString(13));
		arrListBillDiscDtl.add(objBillDiscDtl);
	    }
	    rsBillDiscDtl.close();

	    sql = "select * from tblvoidbillhd where strBillNo='" + lblVoucherNo.getText() + "' and strTransType='VB' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsVoidBillHd objVoidBillHd = new clsVoidBillHd();
		objVoidBillHd.setStrPosCode(rs.getString(1));
		objVoidBillHd.setStrReasonCode(rs.getString(2));
		objVoidBillHd.setStrReasonName(rs.getString(3));
		objVoidBillHd.setStrBillNo(rs.getString(4));
		objVoidBillHd.setDblActualAmount(rs.getDouble(5));
		objVoidBillHd.setDblModifiedAmount(rs.getDouble(6));
		objVoidBillHd.setDteBillDate(rs.getString(7));
		objVoidBillHd.setStrTransType(rs.getString(8));
		objVoidBillHd.setDteModifyVoidBill(rs.getString(9));
		objVoidBillHd.setStrTableNo(rs.getString(10));
		objVoidBillHd.setStrWaiterNo(rs.getString(11));
		objVoidBillHd.setIntShiftCode(rs.getInt(12));
		objVoidBillHd.setStrUserCreated(rs.getString(13));
		objVoidBillHd.setStrUserEdited(rs.getString(14));
		objVoidBillHd.setStrClientCode(rs.getString(15));
		objVoidBillHd.setStrDataPostFlag(rs.getString(16));
		objVoidBillHd.setStrRemark(rs.getString(17));
		arrListVoidBillHd.add(objVoidBillHd);
	    }
	    rs.close();

	    sql = "select * from tblvoidbilldtl where strBillNo='" + lblVoucherNo.getText() + "' and strTransType='VB' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsVoidBillDtl objVoidBillDtl = new clsVoidBillDtl();
		objVoidBillDtl.setStrPosCode(rs.getString(1));
		objVoidBillDtl.setStrReasonCode(rs.getString(2));
		objVoidBillDtl.setStrReasonName(rs.getString(3));
		objVoidBillDtl.setStrItemCode(rs.getString(4));
		objVoidBillDtl.setStrItemName(rs.getString(5));
		objVoidBillDtl.setStrBillNo(rs.getString(6));
		objVoidBillDtl.setIntQuantity(rs.getDouble(7));
		objVoidBillDtl.setDblAmount(rs.getDouble(8));
		objVoidBillDtl.setDblTaxAmount(rs.getDouble(9));
		objVoidBillDtl.setDteBillDate(rs.getString(10));
		objVoidBillDtl.setStrTransType(rs.getString(11));
		objVoidBillDtl.setDteModifyVoidBill(rs.getString(12));
		objVoidBillDtl.setStrSettlementCode(rs.getString(13));
		objVoidBillDtl.setDblSettlementAmt(rs.getDouble(14));
		objVoidBillDtl.setDblPaidAmt(rs.getDouble(15));
		objVoidBillDtl.setStrTableNo(rs.getString(16));
		objVoidBillDtl.setStrWaiterNo(rs.getString(17));
		objVoidBillDtl.setIntShiftCode(intShiftNo);
		objVoidBillDtl.setStrUserCreated(rs.getString(19));
		objVoidBillDtl.setStrClientCode(rs.getString(20));
		objVoidBillDtl.setStrDataPostFlag(rs.getString(21));
		objVoidBillDtl.setStrKOTNo(rs.getString(22));
		objVoidBillDtl.setStrRemarks(rs.getString(23));
		arrListVoidBillDtl.add(objVoidBillDtl);
	    }
	    rs.close();

	    sql = "select * from tblvoidmodifierdtl where strBillNo='" + lblVoucherNo.getText() + "'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		clsVoidBillModifierDtl objVoidBillModDtl = new clsVoidBillModifierDtl();
		objVoidBillModDtl.setStrBillNo(rs.getString(1));
		objVoidBillModDtl.setStrItemCode(rs.getString(2));
		objVoidBillModDtl.setStrModifierCode(rs.getString(3));
		objVoidBillModDtl.setStrModifierName(rs.getString(4));
		objVoidBillModDtl.setDblQuantity(rs.getDouble(5));
		objVoidBillModDtl.setDblAmount(rs.getDouble(6));
		objVoidBillModDtl.setStrClientCode(rs.getString(7));
		objVoidBillModDtl.setStrCustomerCode(rs.getString(8));
		objVoidBillModDtl.setStrDataPostFlag(rs.getString(9));
		objVoidBillModDtl.setStrRemarks(rs.getString(10));
		objVoidBillModDtl.setStrReasonCode(rs.getString(11));
		arrListVoidBillModifierDtl.add(objVoidBillModDtl);
	    }
	    rs.close();
	    funFillItemGrid(billNo);

	    if (totalQtyCount <= 1)
	    {
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
		btnDelete.setEnabled(false);
		btnSave.setEnabled(false);
	    }
	    else
	    {
		btnUp.setEnabled(true);
		btnDown.setEnabled(true);
		btnDelete.setEnabled(true);
		btnSave.setEnabled(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private int funFillItemGrid(String billNo) throws Exception
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
	dm.addColumn("Description");
	dm.addColumn("Qty");
	dm.addColumn("Amount");
	dm.addColumn("ModCode");
	dm.addColumn("KOT No");
	lblVoucherNo.setText(billNo);

	double subTotalForTax = 0;
	double totalDiscAmt = 0.00;
	List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	for (clsBillDtl objBillItemDtl : arrListBillDtl)
	{
	    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
	    Object[] rows
		    =
		    {
			objBillItemDtl.getStrItemName(), objBillItemDtl.getDblQuantity(), objBillItemDtl.getDblAmount(), objBillItemDtl.getStrItemCode(), objBillItemDtl.getStrKOTNo()
		    };
	    dm.addRow(rows);
	    objItemDtl.setItemCode(objBillItemDtl.getStrItemCode());
	    objItemDtl.setItemName(objBillItemDtl.getStrItemName());
	    objItemDtl.setAmount(objBillItemDtl.getDblAmount());
	    objItemDtl.setDiscAmt(objBillItemDtl.getDblDiscountAmt());
	    arrListItemDtls.add(objItemDtl);
	    subTotalForTax += objBillItemDtl.getDblAmount();
	    totalDiscAmt += objBillItemDtl.getDblDiscountAmt();

	    for (clsBillModifierDtl objBillModDtl : arrListBillModifierDtl)
	    {
		if ((objBillItemDtl.getStrItemCode() + "" + objBillModDtl.getStrModifierCode()).equals(objBillModDtl.getStrItemCode()))
		{
		    subTotalForTax += objBillModDtl.getDblAmount();
		    totalDiscAmt += objBillModDtl.getDblDiscAmt();

		    Object[] modRows
			    =
			    {
				objBillModDtl.getStrModifierName(), objBillModDtl.getDblQuantity(), objBillModDtl.getDblAmount(), objBillModDtl.getStrModifierCode(), objBillModDtl.getStrItemCode(), objBillItemDtl.getStrKOTNo()
			    };
		    dm.addRow(modRows);

		    //add modifier items
		    clsItemDtlForTax objModiItemDtl = new clsItemDtlForTax();
		    objModiItemDtl.setItemCode(objBillModDtl.getStrItemCode());
		    objModiItemDtl.setItemName(objBillModDtl.getStrModifierName());
		    objModiItemDtl.setAmount(objBillModDtl.getDblAmount());
		    objModiItemDtl.setDiscAmt(objBillModDtl.getDblDiscAmt());
		    arrListItemDtls.add(objModiItemDtl);
		}
	    }
	}
	rs.close();

	tblItemTable.setModel(dm);
	double subTotal = 0;
	double grandTotal = 0;
	double discountPer = 0;
	String userCreated = "";

	arrListTaxCal.clear();
	clsUtility obj = new clsUtility();
	arrListTaxCal = obj.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, subTotalForTax, totalDiscAmt, "", "S01", "Sales");
	arrListBillTaxDtl.clear();
	double totalTaxAmount = 0;
	for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
	{
	    clsTaxCalculationDtls objTaxDtl = arrListTaxCal.get(cnt);
	    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
	    {
		totalTaxAmount += objTaxDtl.getTaxAmount();
	    }
	    clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
	    objBillTaxDtl.setStrBillNo(billNo);
	    objBillTaxDtl.setStrTaxCode(objTaxDtl.getTaxCode());
	    objBillTaxDtl.setDblTaxableAmount(objTaxDtl.getTaxableAmount());
	    objBillTaxDtl.setDblTaxAmount(objTaxDtl.getTaxAmount());
	    objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
	    objBillTaxDtl.setStrDataPostFlag("N");
	    arrListBillTaxDtl.add(objBillTaxDtl);
	}

	clsBillHd objBillHd = arrListBillHd.get(0);
	subTotal = objBillHd.getDblSubTotal();
	grandTotal = objBillHd.getDblGrandTotal();
	userCreated = objBillHd.getStrUserCreated();
	discountPer = objBillHd.getDblDiscountPer();

	grandTotal = (subTotalForTax - objBillHd.getDblDiscountAmt()) + totalTaxAmount;
	objBillHd.setDblSubTotal(subTotalForTax);
	objBillHd.setDblTaxAmt(totalTaxAmount);
	objBillHd.setDblGrandTotal(grandTotal);
	arrListBillHd.set(0, objBillHd);

	lblUserCreated.setText(userCreated);
	lblSubTotalValue.setText(String.valueOf(Math.rint(subTotal)));
	lblTaxValue.setText(String.valueOf(Math.rint(totalTaxAmount)));
	lblTotalAmt.setText(String.valueOf(Math.rint(grandTotal)));
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

	//tblBillDetails.setValueAt(String.valueOf(Math.rint(grandTotal)), selectedBillNoRow, 3);
	return 1;
    }

    private void funVoidBill()
    {
	String voidBillType = "Bill Void";
	List<clsBillDtl> arrListVoidItemBillDtls = new ArrayList<clsBillDtl>();
	try
	{

	    if (this.transactionUserCode != null && !this.transactionUserCode.isEmpty())
	    {
		userCode = this.transactionUserCode;
	    }
	    isAuditing = false;
	    rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    if (rs1.next())
	    {
		if (Boolean.parseBoolean(rs1.getString(1)))
		{
		    isAuditing = true;
		}
	    }
	    else
	    {
		isAuditing = true;
	    }

	    java.util.Date dt = new java.util.Date();
	    String time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	    StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
	    int seq1 = sb.lastIndexOf(" ");
	    String split = sb.substring(0, seq1);
	    voidBillDate = split + " " + time;

	    if (tblItemTable.getModel().getRowCount() > 0)
	    {
		int reasoncount = 0;
		sql = "select count(strReasonName) from tblreasonmaster where strVoidBill='Y'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		int i = 0;
		while (rs.next())
		{
		    reasoncount = rs.getInt(1);
		}
		if (reasoncount > 0)
		{
		    reason = new String[reasoncount];
		    sql = "select strReasonName from tblreasonmaster where strVoidBill='Y'";
		    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    i = 0;
		    while (rs.next())
		    {
			reason[i] = rs.getString(1);
			i++;
		    }
		    String favoritereason = "NoAuditing";
		    if (isAuditing)
		    {
			favoritereason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, reason, reason[0]);
		    }
		    if (favoritereason != null)
		    {
			sql = "select strReasonCode from tblreasonmaster where strReasonName='" + favoritereason + "' "
				+ "and strVoidBill='Y'";
			rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rs.next())
			{
			    reasoncode = rs.getString(1);
			}
			int choice = JOptionPane.showConfirmDialog(this, "Do you want to Void Bill ?", "Void Bill", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION)
			{
			    String remark = "";
			    if (isAuditing)
			    {
				if (!clsGlobalVarClass.gTouchScreenMode)
				{
				    remark = JOptionPane.showInputDialog(null, "Enter Remarks");
				}
				else
				{
				    new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Remark.").setVisible(true);
				    remark = clsGlobalVarClass.gKeyboardValue;
				}
			    }

			    String billDate = "";
			    String billNo = lblVoucherNo.getText();
			    String shiftNo = "1";
			    sql = "select left(dteBillDate,10) ,right(dteDateCreated,8),intShiftCode from tblbillhd"
				    + " where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
			    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rs.next())
			    {
				billDate = rs.getString(1) + " " + rs.getString(2);
				shiftNo = rs.getString(3);
			    }
			    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

			    double subTotal = 0.00, discAmt = 0.0;
			    String itemCode = "", itemname, amount = "", tableNo = "";
			    String discAmount = "", billno = "";
			    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

			    sql = "delete from tblvoidbilldtl where strBillNo='" + billno + "' and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
			    clsGlobalVarClass.dbMysql.execute(sql);
			    
			    sql = "select a.strItemCode,a.strItemName,a.strBillNo,a.dblQuantity,a.dblAmount,"
				    + "a.dblTaxAmount,a.dteBillDate,b.strTableNo,a.strKOTNo,a.dblDiscountAmt"
				    + " from tblbilldtl a,tblbillhd b "
				    + "where a.strBillNo=b.strBillNo and a.strBillNo='" + billNo + "'";
			    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

			    while (rs.next())
			    {
				String taxAmount = rs.getString(6);
				billDate = rs.getString(7);
				tableNo = rs.getString(8);
				String KOTNo = rs.getString(9);
				itemCode = rs.getString(1);
				discAmt = Double.parseDouble(rs.getString(10));
				subTotal = subTotal + (Double.parseDouble(rs.getString(5)));
				clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
				objItemDtlForTax.setItemCode(rs.getString(1));
				objItemDtlForTax.setItemName(rs.getString(2));
				objItemDtlForTax.setAmount(Double.parseDouble(rs.getString(5)));
				objItemDtlForTax.setDiscAmt(Double.parseDouble(rs.getString(10)));
				objItemDtlForTax.setIntQuantity(Double.parseDouble(rs.getString(4)));
				billno = rs.getString(3);
				arrListItemDtls.add(objItemDtlForTax);

				clsBillDtl objBean = new clsBillDtl();
				objBean.setStrItemCode(rs.getString(1));
				objBean.setDblAmount(Double.parseDouble(rs.getString(5)));
				objBean.setDblDiscountAmt(Double.parseDouble(rs.getString(10)));
				arrListVoidItemBillDtls.add(objBean);

				sql = "delete from tblvoidbilldtl where strBillNo='" + billno + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
				    + " and strClientCode='"+clsGlobalVarClass.gClientCode+"' and strItemCode='"+rs.getString(1)+"' ";
				clsGlobalVarClass.dbMysql.execute(sql);
				
				sql = "insert into tblvoidbilldtl(strPosCode,strReasonCode,strReasonName,strItemCode"
					+ ",strItemName,strBillNo,intQuantity,dblAmount,dblTaxAmount,dteBillDate,"
					+ "strTransType,dteModifyVoidBill,intShiftCode,strUserCreated,strClientCode"
					+ ",strKOTNo,strRemarks,strSettlementCode) "
					+ "values('" + clsGlobalVarClass.gPOSCode + "','" + reasoncode + "'"
					+ ",'" + favoritereason + "','" + rs.getString(1) + "'"
					+ ",'" + rs.getString(2) + "','" + billno + "'"
					+ ",'" + rs.getString(4) + "','" + rs.getString(5) + "'"
					+ ",'" + taxAmount + "','" + billDate + "'"
					+ ",'VB'" + ",'" + voidBillDate + "'"
					+ "," + shiftNo + ",'" + userCode + "'"
					+ ",'" + clsGlobalVarClass.gClientCode + "','" + KOTNo + "'"
					+ ",'" + objUtility.funCheckSpecialCharacters(remark) + "','')";
				if (isAuditing)
				{
				    clsGlobalVarClass.dbMysql.execute(sql);
				}

				for (clsVoidBillDtl objVoidBillDtl : arrListVoidBillDtl)
				{
				    if (itemCode.equalsIgnoreCase(objVoidBillDtl.getStrItemCode()))
				    {
					discAmt = Double.parseDouble(rs.getString(10));
				    }
				    else
				    {
					discAmt = objVoidBillDtl.getDblDiscAmt();
				    }
				    subTotal = subTotal + (objVoidBillDtl.getDblAmount());
				    //discAmt = objVoidBillDtl.getDblAmount()*(objVoidBillDtl.getDblDiscPer()/100);
				    objItemDtlForTax = new clsItemDtlForTax();
				    objItemDtlForTax.setItemCode(objVoidBillDtl.getStrItemCode());
				    objItemDtlForTax.setItemName(objVoidBillDtl.getStrItemName());
				    objItemDtlForTax.setAmount(objVoidBillDtl.getDblAmount());
				    objItemDtlForTax.setDiscAmt(discAmt);
				    objItemDtlForTax.setIntQuantity(objVoidBillDtl.getIntQuantity());
				    billno = objVoidBillDtl.getStrBillNo();
				    arrListItemDtls.add(objItemDtlForTax);

				    objBean = new clsBillDtl();
				    objBean.setStrItemCode(objVoidBillDtl.getStrItemCode());
				    objBean.setDblAmount(objVoidBillDtl.getDblAmount());
				    objBean.setDblDiscountAmt(discAmt);
				    arrListVoidItemBillDtls.add(objBean);
				}

				String tableStatus = funGetTableStatus(tableNo);
				if (tableStatus.equalsIgnoreCase("Normal"))
				{
				    String updateQuery = "update tbltablemaster set strStatus='Normal',intPaxNo=0 "
					    + "where strTableNo='" + tableNo + "'";
				    clsGlobalVarClass.dbMysql.execute(updateQuery);
				}
				else
				{
				    String updateQuery = "update tbltablemaster set strStatus='" + tableStatus + "' "
					    + "where strTableNo='" + tableNo + "'";
				    clsGlobalVarClass.dbMysql.execute(updateQuery);
				}
			    }

			    sql = "select strBillNo,strItemCode,strModifierCode,strModifierName,dblQuantity,dblAmount,strClientCode,strCustomerCode,dblDiscAmt"
				    + " from tblbillmodifierdtl"
				    + " where strBillNo='" + billNo + "'";
			    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    while (rs.next())
			    {

				subTotal = subTotal + (Double.parseDouble(rs.getString(6)));

				clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
				objItemDtlForTax.setItemCode(rs.getString(2));
				objItemDtlForTax.setItemName(rs.getString(4));
				objItemDtlForTax.setAmount(Double.parseDouble(rs.getString(6)));
				objItemDtlForTax.setDiscAmt(Double.parseDouble(rs.getString(9)));
				objItemDtlForTax.setIntQuantity(Double.parseDouble(rs.getString(5)));
				billno = rs.getString(1);
				arrListItemDtls.add(objItemDtlForTax);

				clsBillDtl objBean = new clsBillDtl();
				objBean.setStrItemCode(rs.getString(2));
				objBean.setDblAmount(Double.parseDouble(rs.getString(6)));
				objBean.setDblDiscountAmt(Double.parseDouble(rs.getString(9)));
				arrListVoidItemBillDtls.add(objBean);

				sql = "insert into tblvoidmodifierdtl(strBillNo,strItemCode,strModifierCode,"
					+ "strModifierName,dblQuantity,dblAmount,strClientCode,strCustomerCode"
					+ ",strRemarks,strReasonCode,dteBillDate) values "
					+ "('" + billno + "','" + rs.getString(2) + "'"
					+ ",'" + rs.getString(3) + "','" + rs.getString(4) + "'"
					+ ",'" + rs.getString(5) + "','" + rs.getString(6) + "'"
					+ ",'" + clsGlobalVarClass.gClientCode + "','" + rs.getString(8) + "'"
					+ ",'" + objUtility.funCheckSpecialCharacters(remark) + "'"
					+ ",'" + reasoncode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";

				if (isAuditing)
				{
				    clsGlobalVarClass.dbMysql.execute(sql);

				}

				for (clsVoidBillModifierDtl objVoidBillModDtl : arrListVoidBillModifierDtl)
				{
				    subTotal = subTotal + (objVoidBillModDtl.getDblAmount());
				    discAmt = objVoidBillModDtl.getDblDiscAmt();
				    objItemDtlForTax = new clsItemDtlForTax();
				    objItemDtlForTax.setItemCode(objVoidBillModDtl.getStrItemCode());
				    objItemDtlForTax.setItemName(objVoidBillModDtl.getStrModifierName());
				    objItemDtlForTax.setAmount(objVoidBillModDtl.getDblAmount());
				    objItemDtlForTax.setDiscAmt(objVoidBillModDtl.getDblDiscAmt());
				    billno = objVoidBillModDtl.getStrBillNo();
				    arrListItemDtls.add(objItemDtlForTax);

				    objBean = new clsBillDtl();
				    objBean.setStrItemCode(objVoidBillModDtl.getStrItemCode());
				    objBean.setDblAmount(objVoidBillModDtl.getDblAmount());
				    objBean.setDblDiscountAmt(objVoidBillModDtl.getDblDiscAmt());
				    arrListVoidItemBillDtls.add(objBean);
				}
			    }

			    double dblTotalTaxAmt = 0;
			    String settlementCode = "";
			    for (clsSettelementOptions objSettelementOptions : hmSettlemetnOptions.values())
			    {
				settlementCode = objSettelementOptions.getStrSettelmentCode();

			    }

			    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, subTotal, discAmt, "", "S01", "Sales");

			    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();
			    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
			    {
				double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
				clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
				objBillTaxDtl.setStrBillNo(billno);
				objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
				objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
				objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
				objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

				listObjBillTaxBillDtls.add(objBillTaxDtl);
			    }
			    funInsertBillTaxDtlTable(listObjBillTaxBillDtls, billno);

			    objUtility.funReCalculateDiscountForBill("Void Bill", "Live", clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode, billno, "", arrListVoidItemBillDtls);

			    String billCreatedUser = arrListBillHd.get(0).getStrUserCreated();

			    sql = "select * from tblvoidbillhd where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
			    ResultSet rsExistingRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rsExistingRecord.next())
			    {
				sql = "delete from tblvoidbillhd where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
				clsGlobalVarClass.dbMysql.execute(sql);

				sql = "insert into tblvoidbillhd (strPosCode,strReasonCode,strReasonName,strBillNo,"
					+ "dblActualAmount,dblModifiedAmount,dteBillDate,strTransType,dteModifyVoidBill,strTableNo,strWaiterNo"
					+ ",intShiftCode,strUserCreated,strUserEdited,strClientCode,strRemark,strVoidBillType) "
					+ "(select '" + clsGlobalVarClass.gPOSCode + "','" + reasoncode + "','" + favoritereason + "',"
					+ " '" + lblVoucherNo.getText() + "',dblGrandTotal,dblGrandTotal,'" + billDate + "','VB',"
					+ " '" + voidBillDate + "',strTableNo,strWaiterNo,'" + shiftNo + "'"
					+ ",'" + billCreatedUser + "','" + userCode + "',strClientCode,'" + objUtility.funCheckSpecialCharacters(remark) + "','" + voidBillType + "' "
					+ "from tblbillhd where strBillNo='" + lblVoucherNo.getText() + "')";

				if (isAuditing)
				{
				    clsGlobalVarClass.dbMysql.execute(sql);
				}

				sql = "update tblvoidbillhd set dblActualAmount=dblActualAmount+" + rsExistingRecord.getDouble(5) + ""
					+ ",dblModifiedAmount=dblModifiedAmount+" + rsExistingRecord.getDouble(6) + " "
					+ " where strBillNo='" + billNo + "' ";

				if (isAuditing)
				{
				    clsGlobalVarClass.dbMysql.execute(sql);
				}

			    }
			    else
			    {
				sql = "insert into tblvoidbillhd (strPosCode,strReasonCode,strReasonName,strBillNo,"
					+ "dblActualAmount,dblModifiedAmount,dteBillDate,strTransType,dteModifyVoidBill,strTableNo,strWaiterNo"
					+ ",intShiftCode,strUserCreated,strUserEdited,strClientCode,strRemark,strVoidBillType) "
					+ "(select '" + clsGlobalVarClass.gPOSCode + "','" + reasoncode + "','" + favoritereason + "',"
					+ " '" + lblVoucherNo.getText() + "',dblGrandTotal,dblGrandTotal,'" + billDate + "','VB',"
					+ " '" + voidBillDate + "',strTableNo,strWaiterNo,'" + shiftNo + "'"
					+ ",'" + billCreatedUser + "','" + userCode + "',strClientCode,'" + objUtility.funCheckSpecialCharacters(remark) + "','" + voidBillType + "' "
					+ "from tblbillhd where strBillNo='" + lblVoucherNo.getText() + "')";

				if (isAuditing)
				{
				    clsGlobalVarClass.dbMysql.execute(sql);
				}

			    }
			    //System.out.println(sql);

			    sql = "insert into tblvoidbillsettlementdtl"
				    + "(strBillNo,strSettlementCode,dblSettlementAmt,"
				    + "dblPaidAmt,strExpiryDate,strCardName,"
				    + "strRemark,strClientCode,strCustomerCode,"
				    + "dblActualAmt,dblRefundAmt,strGiftVoucherCode,dteBillDate)"
				    + "(select strBillNo,strSettlementCode,dblSettlementAmt,"
				    + "dblPaidAmt,strExpiryDate,strCardName,"
				    + "strRemark,strClientCode,strCustomerCode,"
				    + "dblActualAmt,dblRefundAmt,strGiftVoucherCode,dteBillDate "
				    + " from tblbillsettlementdtl where strBillNo='" + lblVoucherNo.getText() + "')";

			    if (isAuditing)
			    {
				clsGlobalVarClass.dbMysql.execute(sql);
			    }

			    ResultSet rsDate = clsGlobalVarClass.dbMysql.executeResultSet("select date(dteBillDate),strPOSCode from tblbillhd where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
			    rsDate.next();
			    String date = rsDate.getString(1);
			    String POSCode = rsDate.getString(2);

			    clsGlobalVarClass.dbMysql.execute("Delete from tblbilldtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbillhd where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbillmodifierdtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbillsettlementdtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblhomedelivery where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbilltaxdtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbilldiscdtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbillpromotiondtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblbillcomplementrydtl where strBillNo='" + billNo + "'");
			    clsGlobalVarClass.dbMysql.execute("Delete from tblhomedeldtl where strBillNo='" + billNo + "'");

			    if (clsGlobalVarClass.gEnableBillSeries)
			    {
				//update billseriesbilldtl grand total
				clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl "
					+ "set dblGrandTotal='0.00' "
					+ "where strHdBillNo='" + billNo + "' "
					+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");

				if (clsGlobalVarClass.gEnableBillSeries)
				{
				    String reprintBillNo = objUtility2.funGetBillNoOnModifyBill(billNo);
				    if (!reprintBillNo.isEmpty())
				    {

					objUtility.funPrintBill(reprintBillNo, "Void", objUtility.funGetOnlyPOSDateForTransaction(), clsGlobalVarClass.gPOSCode, "print");
					/**
					 * save reprint audit
					 */
					objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint after modification of pending bill.", "", reprintBillNo, "");					
				    }
				}
			    }
			    else
			    {
				//objUtility.funPrintBill(billNo, "Void", objUtility.funGetOnlyPOSDateForTransaction(), clsGlobalVarClass.gPOSCode, "print");
			    }
			    //send void bill sms
			    sql = "select a.strSendSMSYN,a.longMobileNo "
				    + "from tblsmssetup a "
				    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All' ) "
				    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				    + "and a.strTransactionName='VoidBill' "
				    + "and a.strSendSMSYN='Y'; ";
			    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rsSendSMS.next())
			    {
				String mobileNo = rsSendSMS.getString(2);//mobileNo

				funSendVoidBillSMS(lblVoucherNo.getText(), mobileNo);

			    }
			    rsSendSMS.close();

			    funResetField();
			    funFillBillNoGrid("");
			}
			if (clsGlobalVarClass.gConnectionActive.equals("Y"))
			{
			    if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
			    {
				clsGlobalVarClass.funInvokeHOWebserviceForTrans("Audit", "Void");
			    }
			}
		    }
		}
		else
		{
		    new frmOkPopUp(null, "Please Create Reason First", "Error", 1).setVisible(true);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

// Function To Void Single Item    
    private void funVoidItem()
    {
	String voidBillType = "Item Void";
	try
	{
	    String userCode = clsGlobalVarClass.gUserCode;
	    if (this.transactionUserCode != null && !this.transactionUserCode.isEmpty())
	    {
		userCode = this.transactionUserCode;
	    }
	    isAuditing = false;
	    rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    if (rs1.next())
	    {
		if (Boolean.parseBoolean(rs1.getString(1)))
		{
		    isAuditing = true;
		}
	    }
	    else
	    {
		isAuditing = true;
	    }

	    java.util.Date objDate = new java.util.Date();
	    String time = (objDate.getHours()) + ":" + (objDate.getMinutes()) + ":" + (objDate.getSeconds());
	    StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
	    int seq1 = sb.lastIndexOf(" ");
	    String split = sb.substring(0, seq1);
	    voidBillDate = split + " " + time;

	    if (tblItemTable.getModel().getRowCount() > 0)
	    {
		reasoncount = 0;
		String tableNo = "";

		sql = "select count(strReasonName) from tblreasonmaster where strVoidBill='Y'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		int i = 0;
		while (rs.next())
		{
		    reasoncount = rs.getInt(1);
		}
		if (reasoncount > 0)
		{
		    String selectedReasonCode = "";
		    reason = new String[reasoncount];
		    sql = "select strReasonName from tblreasonmaster where strVoidBill='Y'";
		    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    i = 0;
		    while (rs.next())
		    {
			reason[i] = rs.getString(1);
			i++;
		    }
		    String selectedReasonDesc = "NoAuditing";
		    if (isAuditing)
		    {
			selectedReasonDesc = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, reason, reason[0]);
		    }
		    if (selectedReasonDesc != null)
		    {
			sql = "select strReasonCode from tblreasonmaster where strReasonName='" + selectedReasonDesc + "' "
				+ "and strVoidBill='Y'";
			rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rs.next())
			{
			    selectedReasonCode = rs.getString(1);
			}
			int choice = JOptionPane.showConfirmDialog(this, "Do you want to Void Item ?", "Void Bill", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION)
			{
			    String remark = "";

			    if (isAuditing)
			    {
				if (!clsGlobalVarClass.gTouchScreenMode)
				{
				    remark = JOptionPane.showInputDialog(null, "Enter Remarks");
				}
				else
				{
				    new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Remark.").setVisible(true);
				    remark = clsGlobalVarClass.gKeyboardValue;
				}
			    }

			    String billNo = lblVoucherNo.getText();

			    for (clsVoidBillModifierDtl objModi : mapItemModifierToBeDeleted.values())
			    {
				String itemName = objModi.getStrModifierName();
				//String modItemCode = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 3).toString();//M99
				String itemCode = objModi.getStrItemCode();//I000025M99
				Iterator<clsBillModifierDtl> billModiIt = arrListBillModifierDtl.iterator();
				while (billModiIt.hasNext())
				{
				    clsBillModifierDtl objBillModDtl = billModiIt.next();
				    if (objBillModDtl.getStrItemCode().equals(itemCode) && objBillModDtl.getStrModifierName().equals(itemName))
				    {
					clsVoidBillModifierDtl objVoidBillModDtl = new clsVoidBillModifierDtl();
					objVoidBillModDtl.setStrBillNo(billNo);
					objVoidBillModDtl.setStrItemCode(objBillModDtl.getStrItemCode());
					objVoidBillModDtl.setStrModifierCode(objBillModDtl.getStrModifierCode());
					objVoidBillModDtl.setStrModifierName(objBillModDtl.getStrModifierName());
					objVoidBillModDtl.setDblQuantity(objBillModDtl.getDblQuantity());
					objVoidBillModDtl.setDblAmount(objBillModDtl.getDblAmount());
					objVoidBillModDtl.setStrClientCode(objBillModDtl.getStrClientCode());
					objVoidBillModDtl.setStrCustomerCode(objBillModDtl.getStrCustomerCode());
					objVoidBillModDtl.setStrDataPostFlag(objBillModDtl.getStrDataPostFlag());
					objVoidBillModDtl.setStrRemarks(remark);
					objVoidBillModDtl.setStrReasonCode(selectedReasonCode);
					objVoidBillModDtl.setDblDiscAmt(objBillModDtl.getDblDiscAmt());
					billModiIt.remove();
					arrListVoidBillModifierDtl.add(objVoidBillModDtl);
					arrListBillModifierDtl.remove(objBillModDtl);
				    }
				}
			    }

			    for (clsVoidBillDtl obj : mapItemToBeDeleted.values())
			    {
				String tempItemCode = obj.getStrItemCode();
				String itemName = obj.getStrItemName();
				double selectedVoidQty = obj.getIntQuantity();

				String sqlMaxKOTForItem = "select a.strKOTNo "
					+ "from tblbilldtl a "
					+ "where a.strBillNo='" + billNo + "' and a.strItemCode='" + tempItemCode + "' "
					+ "order by a.strKOTNo desc ";
				ResultSet rsMaxKOT = clsGlobalVarClass.dbMysql.executeResultSet(sqlMaxKOTForItem);
				String maxKOT = "";
				while (rsMaxKOT.next())
				{
				    if (selectedVoidQty <= 0)
				    {
					break;
				    }
				    maxKOT = rsMaxKOT.getString(1);
				    int cnt = 0;
				    Iterator<clsBillDtl> billDtlIt = arrListKOTWiseBillDtl.iterator();
				    while (billDtlIt.hasNext())
				    {
					if (selectedVoidQty <= 0)
					{
					    break;
					}
					clsBillDtl objBillDtl = billDtlIt.next();
					if (objBillDtl.getStrItemCode().equals(tempItemCode) && objBillDtl.getStrKOTNo().equals(maxKOT))
					{
					    boolean flgRecordPresent = false;
					    int cntVoid = 0;
					    for (clsVoidBillDtl objVoidBillDtl : arrListVoidBillDtl)
					    {
						if (objVoidBillDtl.getStrItemCode().equals(objBillDtl.getStrItemCode()) && objVoidBillDtl.getStrKOTNo().equals(maxKOT))
						{
						    double voidedQty = 1;
						    if (objBillDtl.getDblQuantity() < selectedVoidQty)
						    {
							voidedQty = objVoidBillDtl.getIntQuantity() + objBillDtl.getDblQuantity();
						    }
						    else
						    {
							voidedQty = objVoidBillDtl.getIntQuantity() + selectedVoidQty;
						    }

						    objVoidBillDtl.setIntQuantity(voidedQty);
						    double voidedAmt = objBillDtl.getDblRate() * voidedQty;
						    objVoidBillDtl.setDblAmount(voidedAmt);
						    arrListVoidBillDtl.set(cntVoid, objVoidBillDtl);
						    flgRecordPresent = true;
						    break;
						}
						cntVoid++;
					    }

					    if (!flgRecordPresent)
					    {
						clsVoidBillDtl objVoidBillDtl = new clsVoidBillDtl();
						double voidedQty = 1;
						if (objBillDtl.getDblQuantity() < selectedVoidQty)
						{
						    voidedQty = objVoidBillDtl.getIntQuantity() + objBillDtl.getDblQuantity();
						}
						else
						{
						    voidedQty = objVoidBillDtl.getIntQuantity() + selectedVoidQty;
						}
						double voidedAmt = objBillDtl.getDblRate() * voidedQty;
						double totalBillQty = funGetOriginalItemQty(tempItemCode);//Double.parseDouble(tblItemTable.getValueAt(r, 1).toString());
						double taxAmt = objBillDtl.getDblTaxAmount() / totalBillQty;
						objVoidBillDtl.setStrBillNo(billNo);
						objVoidBillDtl.setStrPosCode(clsGlobalVarClass.gPOSCode);
						objVoidBillDtl.setStrItemCode(tempItemCode);
						objVoidBillDtl.setStrItemName(itemName);
						objVoidBillDtl.setIntQuantity(voidedQty);
						objVoidBillDtl.setDblAmount(voidedAmt);
						objVoidBillDtl.setDblPaidAmt(0);
						objVoidBillDtl.setDblSettlementAmt(0);
						objVoidBillDtl.setDblTaxAmount(taxAmt * voidedQty);
						objVoidBillDtl.setDteBillDate(objBillDtl.getDteBillDate());
						objVoidBillDtl.setDteModifyVoidBill(voidBillDate);
						objVoidBillDtl.setStrTransType("VB");
						objVoidBillDtl.setIntShiftCode(intShiftNo);
						objVoidBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
						objVoidBillDtl.setStrDataPostFlag("N");
						objVoidBillDtl.setStrKOTNo(maxKOT);
						objVoidBillDtl.setStrUserCreated(userCode);
						objVoidBillDtl.setStrReasonCode(selectedReasonCode);
						objVoidBillDtl.setStrReasonName(selectedReasonDesc);
						objVoidBillDtl.setStrRemarks(remark);
						objVoidBillDtl.setStrSettlementCode("");
						objVoidBillDtl.setStrWaiterNo("NA");
						objVoidBillDtl.setStrTableNo("NA");
						objVoidBillDtl.setDblDiscPer(objBillDtl.getDblDiscountPer());
						objVoidBillDtl.setDblDiscAmt(voidedAmt * (objBillDtl.getDblDiscountPer() / 100));
						arrListVoidBillDtl.add(objVoidBillDtl);
					    }

					    double qty = objBillDtl.getDblQuantity() - selectedVoidQty;
					    if (qty < 1)
					    {
						selectedVoidQty = selectedVoidQty - objBillDtl.getDblQuantity();
						billDtlIt.remove();
					    }
					    else
					    {
						double amt = objBillDtl.getDblRate() * qty;
						double discAmtForSingleQty = objBillDtl.getDblDiscountAmt() / objBillDtl.getDblQuantity();
						double discAmt = discAmtForSingleQty * qty;
						double totalBillQty = funGetOriginalItemQty(tempItemCode);//Double.parseDouble(tblItemTable.getValueAt(r, 1).toString());
						double taxAmt = objBillDtl.getDblTaxAmount() / totalBillQty;
						objBillDtl.setDblDiscountAmt(discAmt);
						objBillDtl.setDblQuantity(qty);
						objBillDtl.setDblAmount(amt);
						objBillDtl.setDblTaxAmount(taxAmt * qty);
						arrListKOTWiseBillDtl.set(cnt, objBillDtl);
						selectedVoidQty = 0;
					    }

					    Iterator<clsBillModifierDtl> billModiIt = arrListBillModifierDtl.iterator();
					    while (billModiIt.hasNext())
					    {
						clsBillModifierDtl objBillModDtl = billModiIt.next();
						boolean isItemExistsInBillItemDtl = false;
						for (clsBillDtl billDtl : arrListKOTWiseBillDtl)
						{
						    if (billDtl.getStrItemCode().equalsIgnoreCase(tempItemCode))
						    {
							isItemExistsInBillItemDtl = true;
							break;
						    }
						}
						if (!isItemExistsInBillItemDtl)
						{
						    if (objBillModDtl.getStrItemCode().equals(tempItemCode + "" + objBillModDtl.getStrModifierCode()))
						    {
							clsVoidBillModifierDtl objVoidBillModDtl = new clsVoidBillModifierDtl();
							objVoidBillModDtl.setStrBillNo(billNo);
							objVoidBillModDtl.setStrItemCode(objBillModDtl.getStrItemCode());
							objVoidBillModDtl.setStrModifierCode(objBillModDtl.getStrModifierCode());
							objVoidBillModDtl.setStrModifierName(objBillModDtl.getStrModifierName());
							objVoidBillModDtl.setDblQuantity(objBillModDtl.getDblQuantity());
							objVoidBillModDtl.setDblAmount(objBillModDtl.getDblAmount());
							objVoidBillModDtl.setStrClientCode(objBillModDtl.getStrClientCode());
							objVoidBillModDtl.setStrCustomerCode(objBillModDtl.getStrCustomerCode());
							objVoidBillModDtl.setStrDataPostFlag(objBillModDtl.getStrDataPostFlag());
							objVoidBillModDtl.setStrRemarks(remark);
							objVoidBillModDtl.setStrReasonCode(selectedReasonCode);
							billModiIt.remove();
							arrListVoidBillModifierDtl.add(objVoidBillModDtl);
							arrListBillModifierDtl.remove(objBillModDtl);
						    }
						}
					    }
					    //1 remove item from billmodifer dtl
					    //2 add item to voidmodifirt dtl
					    break;
					}
					cnt++;
				    }
				}
				rsMaxKOT.close();
			    }

			    funFillItemGrid(billNo);
			    double totalDiscAmt = 0;
			    double discPer = 0.00;
			    double itemSubTotal = 0;
			    String discountOnType = "", discountOnValue = "", reasoncode = "";
			    //re-calculate discount
			    if (arrListBillDiscDtl.size() > 0)
			    {
				Iterator<clsBillDiscountDtl> billDiscIt = arrListBillDiscDtl.iterator();

				while (billDiscIt.hasNext())
				{
				    clsBillDiscountDtl objBillDiscDtl = billDiscIt.next();
				    discountOnType = objBillDiscDtl.getDiscOnType();
				    discountOnValue = objBillDiscDtl.getDiscOnValue();
				    double discPerce = objBillDiscDtl.getDiscPer();

				    double newDiscAmt = 0;
				    double newDiscOnAmt = 0;

				    if (discountOnType.equalsIgnoreCase("Total"))
				    {
					//bill dtl
					for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
					{
					    newDiscAmt += objBillDtl.getDblDiscountAmt();
					    newDiscOnAmt += objBillDtl.getDblAmount();
					    itemSubTotal += objBillDtl.getDblAmount();
					}
					//modifier  dtl
					for (clsBillModifierDtl objBillModifierDtl : arrListBillModifierDtl)
					{
					    newDiscAmt += objBillModifierDtl.getDblDiscAmt();
					    newDiscOnAmt += objBillModifierDtl.getDblAmount();
					    itemSubTotal += objBillModifierDtl.getDblAmount();
					}
				    }
				    else if (discountOnType.equalsIgnoreCase("ItemWise"))
				    {
					//bill dtl
					for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
					{
					    if (objBillDtl.getStrItemName().equalsIgnoreCase(discountOnValue))
					    {
						newDiscOnAmt += objBillDtl.getDblAmount();
						itemSubTotal += objBillDtl.getDblAmount();

						//modifier  dtl
						for (clsBillModifierDtl objBillModifierDtl : arrListBillModifierDtl)
						{
						    if (objBillDtl.getStrItemCode().equals(objBillModifierDtl.getStrItemCode().substring(0, 7)))
						    {
							newDiscOnAmt += objBillModifierDtl.getDblAmount();
							itemSubTotal += objBillModifierDtl.getDblAmount();
						    }
						}
					    }
					}
				    }
				    else if (discountOnType.equalsIgnoreCase("GroupWise"))
				    {
					//bill dtl
					for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
					{
					    if (objBillDtl.getGroupName().equalsIgnoreCase(discountOnValue))
					    {
						newDiscAmt += objBillDtl.getDblDiscountAmt();
						newDiscOnAmt += objBillDtl.getDblAmount();
						itemSubTotal += objBillDtl.getDblAmount();
					    }
					    else
					    {
						itemSubTotal += objBillDtl.getDblAmount();
					    }
					}
					//modifier  dtl
					for (clsBillModifierDtl objBillModifierDtl : arrListBillModifierDtl)
					{
					    if (objBillModifierDtl.getGroupName().equalsIgnoreCase(discountOnValue))
					    {
						newDiscAmt += objBillModifierDtl.getDblDiscAmt();
						newDiscOnAmt += objBillModifierDtl.getDblAmount();
						itemSubTotal += objBillModifierDtl.getDblAmount();
					    }
					    else
					    {
						itemSubTotal += objBillModifierDtl.getDblAmount();
					    }
					}
				    }
				    else if (discountOnType.equalsIgnoreCase("SubGroupWise"))
				    {
					//bill dtl
					for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
					{
					    if (objBillDtl.getGroupName().equalsIgnoreCase(discountOnValue))
					    {
						newDiscAmt += objBillDtl.getDblDiscountAmt();
						newDiscOnAmt += objBillDtl.getDblAmount();
						itemSubTotal += objBillDtl.getDblAmount();
					    }
					    else
					    {
						itemSubTotal += objBillDtl.getDblAmount();
					    }
					}
					//modifier  dtl
					for (clsBillModifierDtl objBillModifierDtl : arrListBillModifierDtl)
					{
					    if (objBillModifierDtl.getSubGrouName().equalsIgnoreCase(discountOnValue))
					    {
						newDiscAmt += objBillModifierDtl.getDblDiscAmt();
						newDiscOnAmt += objBillModifierDtl.getDblAmount();
						itemSubTotal += objBillModifierDtl.getDblAmount();
					    }
					    else
					    {
						itemSubTotal += objBillModifierDtl.getDblAmount();
					    }
					}
				    }

				    //update bill discounr
				    if (newDiscOnAmt > 0)
				    {
					newDiscAmt = (discPerce / 100) * newDiscOnAmt;
					objBillDiscDtl.setDiscAmt(newDiscAmt);
					objBillDiscDtl.setDiscOnAmt(newDiscOnAmt);
					totalDiscAmt += newDiscAmt;
				    }
				    else
				    {
					billDiscIt.remove();
				    }
				}
			    }
			    itemSubTotal = 0.00;

			    //bill dtl
			    for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
			    {
				itemSubTotal += objBillDtl.getDblAmount();
			    }

			    //modifier  dtl
			    for (clsBillModifierDtl objBillModifierDtl : arrListBillModifierDtl)
			    {
				itemSubTotal += objBillModifierDtl.getDblAmount();
			    }

			    if (itemSubTotal == 0.00)
			    {
				discPer = 0.00;
			    }
			    else
			    {
				discPer = (totalDiscAmt / itemSubTotal) * 100;
			    }
			    clsBillHd objBillHd = arrListBillHd.get(0);
			    double totalVoidedAmt = objBillHd.getDblGrandTotal() - itemSubTotal;

			    clsVoidBillHd objVoidBillHd = new clsVoidBillHd();
			    if (arrListVoidBillHd.size() > 0)
			    {
				objVoidBillHd = arrListVoidBillHd.get(0);
				objVoidBillHd.setDblModifiedAmount(objVoidBillHd.getDblModifiedAmount() + totalVoidedAmt);
				arrListVoidBillHd.set(0, objVoidBillHd);
			    }
			    else
			    {
				objVoidBillHd.setStrBillNo(billNo);
				objVoidBillHd.setStrPosCode(objBillHd.getStrPOSCode());
				objVoidBillHd.setStrReasonCode(selectedReasonCode);
				objVoidBillHd.setStrReasonName(selectedReasonDesc);
				objVoidBillHd.setDblActualAmount(objBillHd.getDblGrandTotal());
				objVoidBillHd.setDblModifiedAmount(totalVoidedAmt);
				objVoidBillHd.setDteBillDate(objBillHd.getDteBillDate());
				objVoidBillHd.setStrTransType("VB");
				objVoidBillHd.setDteModifyVoidBill(voidBillDate);
				objVoidBillHd.setStrTableNo(objBillHd.getStrTableNo());
				objVoidBillHd.setStrWaiterNo(objBillHd.getStrWaiterNo());
				objVoidBillHd.setIntShiftCode(objBillHd.getIntShiftCode());
				objVoidBillHd.setStrUserCreated(objBillHd.getStrUserCreated());//billcreated
				objVoidBillHd.setStrUserEdited(userCode);//voided user
				objVoidBillHd.setStrClientCode(objBillHd.getStrClientCode());
				objVoidBillHd.setStrDataPostFlag(objBillHd.getStrDataPostFlag());
				objVoidBillHd.setStrRemark(remark);
				arrListVoidBillHd.add(objVoidBillHd);
			    }

			    objBillHd.setDblSubTotal(itemSubTotal);
			    objBillHd.setDblDiscountAmt(totalDiscAmt);
			    objBillHd.setDblDiscountPer(discPer);
			    objBillHd.setStrDiscOnType(discountOnType);
			    objBillHd.setStrDiscOnValue(discountOnValue);
			    objBillHd.setStrReasonCode(reasoncode);
			    objBillHd.setStrRemarks(remark);
			    double grandTotal = (itemSubTotal + objBillHd.getDblTaxAmt()) - totalDiscAmt;

			    //start code to calculate roundoff amount and round off by amt
			    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotal);
			    grandTotal = mapRoundOff.get("roundOffAmt");
			    double grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
			    //end code to calculate roundoff amount and round off by amt
			    objBillHd.setDblGrandTotal(grandTotal);
			    objBillHd.setDblGrandTotalRoundOffBy(grandTotalRoundOffBy);

			    arrListBillHd.set(0, objBillHd);
			    lblTotalAmt.setText(String.valueOf(Math.rint(objBillHd.getDblGrandTotal())));
			    lblTaxValue.setText(String.valueOf(Math.rint(objBillHd.getDblTaxAmt())));
			    lblSubTotalValue.setText(String.valueOf(Math.rint(objBillHd.getDblSubTotal())));
			    //tblBillDetails.setValueAt(Math.rint(objBillHd.getDblGrandTotal()), selectedBillNoRow, 3);

			    funInsertVoidData(voidBillType);
			    funUpdateBillData();

			    objUtility.funPrintBill(billNo, "Void", clsGlobalVarClass.getPOSDateForTransaction(), objBillHd.getStrPOSCode(), "print");
			    /**
			     * save reprint audit
			     */
			    objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint the bill after partial bill void.", "", billNo, "");

			    if (clsGlobalVarClass.gEnableBillSeries)
			    {
				String reprintBillNo = objUtility2.funGetBillNoOnModifyBill(billNo);
				if (reprintBillNo.trim().length() > 0)
				{
				    objUtility.funPrintBill(reprintBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				    /**
				     * save reprint audit
				     */
				    objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint the bill after partial voided bill", "", billNo, "");
				}
			    }

			    mapItemToBeDeleted.clear();
			}
			funFillBillNoGrid("");
		    }
		}
		else
		{
		    new frmOkPopUp(null, "Please Create Reason First", "Error", 1).setVisible(true);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funInsertVoidData(String voidBillType) throws Exception
    {
	String billNo = lblVoucherNo.getText();

	clsVoidBillHd objVoidBillHd = arrListVoidBillHd.get(0);
	sql = "delete from tblvoidbillhd where strBillNo='" + objVoidBillHd.getStrBillNo() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	clsGlobalVarClass.dbMysql.execute(sql);
	sql = "insert into tblvoidbillhd (strPosCode,strReasonCode,strReasonName,strBillNo,"
		+ "dblActualAmount,dblModifiedAmount,dteBillDate,"
		+ "strTransType,dteModifyVoidBill,strTableNo,strWaiterNo,intShiftCode,"
		+ "strUserCreated,strUserEdited,strClientCode,strRemark,strVoidBillType) values "
		+ "('" + objVoidBillHd.getStrPosCode() + "','" + objVoidBillHd.getStrReasonCode() + "'"
		+ ",'" + objVoidBillHd.getStrReasonName() + "','" + objVoidBillHd.getStrBillNo() + "'"
		+ ",'" + objVoidBillHd.getDblActualAmount() + "'," + objVoidBillHd.getDblModifiedAmount() + ""
		+ ",'" + objVoidBillHd.getDteBillDate() + "','" + objVoidBillHd.getStrTransType() + "'"
		+ ",'" + objVoidBillHd.getDteModifyVoidBill() + "','" + objVoidBillHd.getStrTableNo() + "'"
		+ ",'" + objVoidBillHd.getStrWaiterNo() + "','" + objVoidBillHd.getIntShiftCode() + "'"
		+ ",'" + objVoidBillHd.getStrUserCreated() + "','" + objVoidBillHd.getStrUserEdited() + "'"
		+ ",'" + objVoidBillHd.getStrClientCode() + "','" + objUtility.funCheckSpecialCharacters(objVoidBillHd.getStrRemark()) + "','" + voidBillType + "')";
	//System.out.println(sql);
	if (isAuditing)
	{
	    clsGlobalVarClass.dbMysql.execute(sql);
	}

	String billno = "";
	double discAmt = 0.0;
	List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	List<clsBillDtl> arrListVoidItemBillDtls = new ArrayList<clsBillDtl>();
	double subTotal = 0.00;

	for (clsVoidBillDtl objVoidBillDtl : arrListVoidBillDtl)
	{
	    objVoidBillDtl.getStrItemCode();
	    subTotal = subTotal + (objVoidBillDtl.getDblAmount());
	    discAmt = objVoidBillDtl.getDblAmount() * (objVoidBillDtl.getDblDiscPer() / 100);
	    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
	    objItemDtlForTax.setItemCode(objVoidBillDtl.getStrItemCode());
	    objItemDtlForTax.setItemName(objVoidBillDtl.getStrItemName());
	    objItemDtlForTax.setAmount(objVoidBillDtl.getDblAmount());
	    objItemDtlForTax.setDiscAmt(objVoidBillDtl.getDblAmount() * (objVoidBillDtl.getDblDiscPer() / 100));
	    objItemDtlForTax.setIntQuantity(objVoidBillDtl.getIntQuantity());
	    billno = objVoidBillDtl.getStrBillNo();
	    arrListItemDtls.add(objItemDtlForTax);

	    clsBillDtl objBean = new clsBillDtl();
	    objBean.setStrItemCode(objVoidBillDtl.getStrItemCode());
	    objBean.setDblAmount(objVoidBillDtl.getDblAmount());
	    objBean.setDblDiscountAmt(objVoidBillDtl.getDblDiscAmt());
	    arrListVoidItemBillDtls.add(objBean);

	    sql = "delete from tblvoidbilldtl where strBillNo='" + objVoidBillHd.getStrBillNo() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		+ " and strClientCode='"+clsGlobalVarClass.gClientCode+"' and strItemCode='"+objVoidBillDtl.getStrItemCode()+"' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    
	    sql = "insert into tblvoidbilldtl(strPosCode,strReasonCode,strReasonName,strItemCode"
		    + ",strItemName,strBillNo,intQuantity,dblAmount,dblTaxAmount,dteBillDate,"
		    + "strTransType,dteModifyVoidBill,intShiftCode,strUserCreated,strClientCode"
		    + ",strKOTNo,strRemarks,strSettlementCode) "
		    + "values('" + objVoidBillDtl.getStrPosCode() + "','" + objVoidBillDtl.getStrReasonCode() + "'"
		    + ",'" + objVoidBillDtl.getStrReasonName() + "','" + objVoidBillDtl.getStrItemCode() + "'"
		    + ",'" + objVoidBillDtl.getStrItemName() + "','" + objVoidBillDtl.getStrBillNo() + "'"
		    + ",'" + objVoidBillDtl.getIntQuantity() + "','" + objVoidBillDtl.getDblAmount() + "'"
		    + ",'" + objVoidBillDtl.getDblTaxAmount() + "','" + objVoidBillDtl.getDteBillDate() + "'"
		    + ",'" + objVoidBillDtl.getStrTransType() + "'" + ",'" + objVoidBillDtl.getDteModifyVoidBill() + "'"
		    + "," + objVoidBillDtl.getIntShiftCode() + ",'" + objVoidBillDtl.getStrUserCreated() + "'"
		    + ",'" + objVoidBillDtl.getStrClientCode() + "','" + objVoidBillDtl.getStrKOTNo() + "'"
		    + ",'" + objUtility.funCheckSpecialCharacters(objVoidBillDtl.getStrRemarks()) + "','')";
	    if (isAuditing)
	    {
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	}

	sql = "delete from tblvoidmodifierdtl where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	clsGlobalVarClass.dbMysql.execute(sql);
	for (clsVoidBillModifierDtl objVoidBillModDtl : arrListVoidBillModifierDtl)
	{
	    subTotal = subTotal + (objVoidBillModDtl.getDblAmount());
	    discAmt = objVoidBillModDtl.getDblDiscAmt();
	    clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
	    objItemDtlForTax.setItemCode(objVoidBillModDtl.getStrItemCode());
	    objItemDtlForTax.setItemName(objVoidBillModDtl.getStrModifierName());
	    objItemDtlForTax.setAmount(objVoidBillModDtl.getDblAmount());
	    objItemDtlForTax.setDiscAmt(objVoidBillModDtl.getDblDiscAmt());
	    billno = objVoidBillModDtl.getStrBillNo();
	    arrListItemDtls.add(objItemDtlForTax);

	    clsBillDtl objBean = new clsBillDtl();
	    objBean.setStrItemCode(objVoidBillModDtl.getStrItemCode());
	    objBean.setDblAmount(objVoidBillModDtl.getDblAmount());
	    objBean.setDblDiscountAmt(objVoidBillModDtl.getDblDiscAmt());
	    arrListVoidItemBillDtls.add(objBean);

	    sql = "insert into tblvoidmodifierdtl(strBillNo,strItemCode,strModifierCode,"
		    + "strModifierName,dblQuantity,dblAmount,strClientCode,strCustomerCode"
		    + ",strDataPostFlag,strRemarks,strReasonCode,dteBillDate) values "
		    + "('" + objVoidBillModDtl.getStrBillNo() + "','" + objVoidBillModDtl.getStrItemCode() + "'"
		    + ",'" + objVoidBillModDtl.getStrModifierCode() + "','" + objVoidBillModDtl.getStrModifierName() + "'"
		    + ",'" + objVoidBillModDtl.getDblQuantity() + "','" + objVoidBillModDtl.getDblAmount() + "'"
		    + ",'" + objVoidBillModDtl.getStrClientCode() + "','" + objVoidBillModDtl.getStrCustomerCode() + "'"
		    + ",'" + objVoidBillModDtl.getStrDataPostFlag() + "','" + objUtility.funCheckSpecialCharacters(objVoidBillModDtl.getStrRemarks()) + "'"
		    + ",'" + objVoidBillModDtl.getStrReasonCode() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
	    //System.out.println("recordset:"+sql);
	    if (isAuditing)
	    {
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	}

	arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, subTotal, discAmt, "", "S01", "Sales");
	List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();
	for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	{
	    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
	    clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
	    objBillTaxDtl.setStrBillNo(billno);
	    objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
	    objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
	    objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
	    objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
	    objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

	    listObjBillTaxBillDtls.add(objBillTaxDtl);
	}
	funInsertBillTaxDtlTable(listObjBillTaxBillDtls, billNo);

	objUtility.funReCalculateDiscountForBill("Void Bill", "Live", objVoidBillHd.getStrPosCode(), clsGlobalVarClass.gClientCode, billno, "", arrListVoidItemBillDtls);
	//send void bill sms
	sql = "select a.strSendSMSYN,a.longMobileNo "
		+ "from tblsmssetup a "
		+ "where  (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All' )  "
		+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		+ "and a.strTransactionName='VoidBill' "
		+ "and a.strSendSMSYN='Y'; ";
	ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsSendSMS.next())
	{
	    String mobileNo = rsSendSMS.getString(2);//mobileNo

	    funSendVoidBillSMS(lblVoucherNo.getText(), mobileNo);

	}
	rsSendSMS.close();
    }

    private void funUpdateBillData() throws Exception
    {
	DecimalFormat objDecFormat = new DecimalFormat("####0.00");

	String sqlDelete = "delete from tblbilldtl  "
		+ " where strBillNo='" + lblVoucherNo.getText() + "' "
		+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	String sqlInsertBillDtl = "insert into tblbilldtl "
		+ "(strItemCode,strItemName,strBillNo,strAdvBookingNo,dblRate"
		+ ",dblQuantity,dblAmount,dblTaxAmount,dteBillDate,strKOTNo"
		+ ",strClientCode,strCustomerCode,tmeOrderProcessing,strDataPostFlag"
		+ ",strMMSDataPostFlag,strManualKOTNo,tdhYN,strPromoCode,strCounterCode"
		+ ",strWaiterNo,dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderPickup) "
		+ "values ";
	for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
	{
	    //double amount = objBillDtl.getDblAmount();
	    //double amount = objBillDtl.getDblQuantity() * objBillDtl.getDblRate();
	    //objBillDtl.setDblAmount(amount);
	    double amt=0.0;
	    StringBuilder strBuilder = new StringBuilder("select a.dblQuantity,a.dblRate from tblbillcomplementrydtl a" 
		    + " where a.strItemCode='" + objBillDtl.getStrItemCode() + "' " 
		    + " and a.strBillNo='"+objBillDtl.getStrBillNo()+"'");
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(strBuilder.toString());
	    if(rs.next())
	    {
		double qty = objBillDtl.getDblQuantity()-rs.getDouble(1);
		amt = objBillDtl.getDblRate() * qty;
	    }
	    else
	    {	
	    amt = objBillDtl.getDblRate() * objBillDtl.getDblQuantity();
	    }
	    objBillDtl.setDblAmount(amt);
	    String sql = "('" + objBillDtl.getStrItemCode() + "','" + objBillDtl.getStrItemName() + "'"
		    + ",'" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrAdvBookingNo() + "'," + objBillDtl.getDblRate() + ""
		    + ",'" + objBillDtl.getDblQuantity() + "','" + objBillDtl.getDblAmount() + "'"
		    + "," + objBillDtl.getDblTaxAmount() + ",'" + objBillDtl.getDteBillDate() + "'"
		    + ",'" + objBillDtl.getStrKOTNo() + "','" + objBillDtl.getStrClientCode() + "'"
		    + ",'" + objBillDtl.getStrCustomerCode() + "','" + objBillDtl.getTmeOrderProcessing() + "'"
		    + ",'" + objBillDtl.getStrDataPostFlag() + "','" + objBillDtl.getStrMMSDataPostFlag() + "'"
		    + ",'" + objBillDtl.getStrManualKOTNo() + "','" + objBillDtl.getTdhYN() + "'"
		    + ",'" + objBillDtl.getStrPromoCode() + "','" + objBillDtl.getStrCounterCode() + "'"
		    + ",'" + objBillDtl.getStrWaiterNo() + "','" + objBillDtl.getDblDiscountAmt() + "'"
		    + ",'" + objBillDtl.getDblDiscountPer() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + objBillDtl.getStrOrderPickupTime() + "')";

	    clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl + "" + sql);
	}

	Map<String, clsPromotionItems> hmPromoItem = new HashMap<String, clsPromotionItems>();
	double freeAmount = 0;
	frmBillSettlement objBillSettlement = new frmBillSettlement("Void Bill");
	clsCalculateBillPromotions objCalculateBillPromotions = new clsCalculateBillPromotions(objBillSettlement);

	Map<String, clsPromotionItems> hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("VoidBill", null, lblVoucherNo.getText(), null);
	if (null != hmPromoItemDtl && hmPromoItemDtl.size() > 0)
	{
	    sqlQuery = "select strItemName,sum(dblQuantity),sum(dblAmount)"
		    + ",strItemCode,sum(dblDiscountAmt),dblDiscountPer "
		    + "from tblbilldtl "
		    + "where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' and tdhYN='N' "
		    + "group by strItemCode;";
	    ResultSet rsPromoBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);

	    while (rsPromoBill.next())
	    {
		String itemCode = rsPromoBill.getString(4);
		double rate = rsPromoBill.getDouble(3) / rsPromoBill.getDouble(2);
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
				hmPromoItem.put(itemCode, objPromoItemsDtl);
				hmPromoItemDtl.remove(itemCode);
			    }
			}
		    }
		}
	    }
	    rsPromoBill.close();
	}

	String sqlDelBillPromo = "delete from tblbillpromotiondtl where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelBillPromo);

	if (hmPromoItem.size() > 0)
	{
	    sqlDelete = "delete from tblbilldtl  "
		    + " where strBillNo='" + lblVoucherNo.getText() + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(sqlDelete);

	    for (clsBillDtl objBillDtl : arrListKOTWiseBillDtl)
	    {
		if (hmPromoItem.containsKey(objBillDtl.getStrItemCode()))
		{
		    clsPromotionItems objPromoItemDtl = hmPromoItem.get(objBillDtl.getStrItemCode());
		    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
		    {
			double freeQty = objPromoItemDtl.getFreeItemQty();
			double freeAmt = freeQty * objBillDtl.getDblRate();

			String insertBillPromoDtl = "insert into tblbillpromotiondtl "
				+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
				+ ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
				+ ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
				+ "('" + lblVoucherNo.getText() + "','" + objBillDtl.getStrItemCode() + "','" + objPromoItemDtl.getPromoCode() + "'"
				+ ",'" + freeQty + "','" + objBillDtl.getDblRate() + "','" + clsGlobalVarClass.gClientCode + "'"
				+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
			clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
			hmPromoItem.remove(objBillDtl.getStrItemCode());
		    }
		}

		String sql = "('" + objBillDtl.getStrItemCode() + "','" + objBillDtl.getStrItemName() + "'"
			+ ",'" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrAdvBookingNo() + "'," + objBillDtl.getDblRate() + ""
			+ ",'" + objBillDtl.getDblQuantity() + "','" + objBillDtl.getDblAmount() + "'"
			+ "," + objBillDtl.getDblTaxAmount() + ",'" + objBillDtl.getDteBillDate() + "'"
			+ ",'" + objBillDtl.getStrKOTNo() + "','" + objBillDtl.getStrClientCode() + "'"
			+ ",'" + objBillDtl.getStrCustomerCode() + "','" + objBillDtl.getTmeOrderProcessing() + "'"
			+ ",'" + objBillDtl.getStrDataPostFlag() + "','" + objBillDtl.getStrMMSDataPostFlag() + "'"
			+ ",'" + objBillDtl.getStrManualKOTNo() + "','" + objBillDtl.getTdhYN() + "'"
			+ ",'" + objBillDtl.getStrPromoCode() + "','" + objBillDtl.getStrCounterCode() + "'"
			+ ",'" + objBillDtl.getStrWaiterNo() + "','" + objBillDtl.getDblDiscountAmt() + "'"
			+ ",'" + objBillDtl.getDblDiscountPer() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + objBillDtl.getStrOrderPickupTime() + "')";

		clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl + "" + sql);
	    }

	    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
		    + "from tblbillpromotiondtl "
		    + " where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' and strPromoType='ItemWise' ";
	    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
	    while (rsBillPromo.next())
	    {
		double freeQty = rsBillPromo.getDouble(1);
		String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
			+ " from tblbilldtl "
			+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
			+ " and strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
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
	}

	// Promotion Code End Here  
	arrListBillDtl.clear();
	sql = "select a.strItemCode,a.strItemName,a.strBillNo,a.strAdvBookingNo,a.dblRate,sum(a.dblQuantity) "
		+ ",sum(a.dblAmount),sum(a.dblTaxAmount),a.dteBillDate,a.strKOTNo,a.strClientCode,a.strCustomerCode "
		+ ",a.tmeOrderProcessing,a.strDataPostFlag,a.strMMSDataPostFlag,a.strManualKOTNo,a.tdhYN "
		+ ",a.strPromoCode,a.strCounterCode,a.strWaiterNo,a.dblDiscountAmt,a.dblDiscountPer,b.strSubGroupCode "
		+ ",c.strSubGroupName,c.strGroupCode,d.strGroupName "
		+ "from tblbilldtl a,tblitemmaster b ,tblsubgrouphd c,tblgrouphd d "
		+ "where a.strBillNo='" + lblVoucherNo.getText().trim() + "' and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' and a.strItemCode=b.strItemCode "
		+ "and b.strSubGroupCode=c.strSubGroupCode and c.strGroupCode=d.strGroupCode "
		+ "group by a.strItemCode,a.strItemName,a.strBillNo;";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rs.next())
	{
	    clsBillDtl objBillDtl = new clsBillDtl();
	    objBillDtl.setStrItemCode(rs.getString(1));
	    objBillDtl.setStrItemName(rs.getString(2));
	    objBillDtl.setStrBillNo(rs.getString(3));
	    objBillDtl.setStrAdvBookingNo(rs.getString(4));
	    objBillDtl.setDblRate(rs.getDouble(5));
	    objBillDtl.setDblQuantity(rs.getDouble(6));
	    objBillDtl.setDblAmount(rs.getDouble(7));
	    objBillDtl.setDblTaxAmount(rs.getDouble(8));
	    objBillDtl.setDteBillDate(rs.getString(9));
	    objBillDtl.setStrKOTNo(rs.getString(10));
	    objBillDtl.setStrClientCode(rs.getString(11));
	    objBillDtl.setStrCustomerCode(rs.getString(12));
	    objBillDtl.setTmeOrderProcessing(rs.getString(13));
	    objBillDtl.setStrDataPostFlag(rs.getString(14));
	    objBillDtl.setStrMMSDataPostFlag(rs.getString(15));
	    objBillDtl.setStrManualKOTNo(rs.getString(16));
	    objBillDtl.setTdhYN(rs.getString(17));
	    objBillDtl.setStrPromoCode(rs.getString(18));
	    objBillDtl.setStrCounterCode(rs.getString(19));
	    objBillDtl.setStrWaiterNo(rs.getString(20));
	    objBillDtl.setDblDiscountAmt(rs.getDouble(21));
	    objBillDtl.setDblDiscountPer(rs.getDouble(22));
	    objBillDtl.setSubGrouName(rs.getString(24));
	    objBillDtl.setGroupName(rs.getString(26));
	    arrListBillDtl.add(objBillDtl);
	}
	rs.close();

	sqlDelete = "delete from tblbillhd "
		+ "where strBillNo='" + lblVoucherNo.getText().trim() + "' "
		+ "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	clsBillHd objBillHd = arrListBillHd.get(0);
	objBillHd.setDblSubTotal(objBillHd.getDblSubTotal());
	objBillHd.setDblGrandTotal(objBillHd.getDblGrandTotal());
	//objBillHd.setDblSubTotal(objBillHd.getDblSubTotal()-promoItemAmt);
	//objBillHd.setDblGrandTotal(objBillHd.getDblGrandTotal()-promoItemAmt);
	arrListBillHd.set(0, objBillHd);
	funFillItemGrid(objBillHd.getStrBillNo());

	String sqlInsert = "insert into tblbillhd(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,"
		+ "dblDiscountAmt,dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		+ ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
		+ ",strTableNo,strWaiterNo,strCustomerCode,strManualBillNo,intShiftCode"
		+ ",intPaxNo,strDataPostFlag,strReasonCode,strRemarks,dblTipAmount,dteSettleDate"
		+ ",strCounterCode,dblDeliveryCharges,strAreaCode,strDiscountRemark,strTakeAwayRemarks,strDiscountOn,dblRoundOff,dtBillDate"
		+ ",intOrderNo ) "
		+ "values('" + objBillHd.getStrBillNo() + "','" + objBillHd.getStrAdvBookingNo() + "'"
		+ ",'" + objBillHd.getDteBillDate() + "','" + objBillHd.getStrPOSCode() + "'"
		+ ",'" + objBillHd.getStrSettelmentMode() + "','" + objDecFormat.format(objBillHd.getDblDiscountAmt()) + "'"
		+ ",'" + objDecFormat.format(objBillHd.getDblDiscountPer()) + "','" + objBillHd.getDblTaxAmt() + "'"
		+ ",'" + objBillHd.getDblSubTotal() + "','" + Math.rint(objBillHd.getDblGrandTotal()) + "'"
		+ ",'" + objBillHd.getStrTakeAway() + "','" + objBillHd.getStrOperationType() + "'"
		+ ",'" + objBillHd.getStrUserCreated() + "','" + objBillHd.getStrUserEdited() + "'"
		+ ",'" + objBillHd.getDteDateCreated() + "','" + objBillHd.getDteDateEdited() + "'"
		+ ",'" + objBillHd.getStrClientCode() + "','" + objBillHd.getStrTableNo() + "'"
		+ ",'" + objBillHd.getStrWaiterNo() + "','" + objBillHd.getStrCustomerCode() + "'"
		+ ",'" + objBillHd.getStrManualBillNo() + "'," + objBillHd.getIntShiftCode() + ""
		+ "," + objBillHd.getIntPaxNo() + ",'" + objBillHd.getStrDataPostFlag() + "','" + objBillHd.getStrReasonCode() + "'"
		+ ",'" + objUtility.funCheckSpecialCharacters(objBillHd.getStrRemarks()) + "'," + objBillHd.getDblTipAmount() + ",'" + objBillHd.getDteSettleDate() + "'"
		+ ",'" + objBillHd.getStrCounterCode() + "'," + objBillHd.getDblDeliveryCharges() + ""
		+ ", '" + objBillHd.getStrAreaCode() + "','" + objUtility.funCheckSpecialCharacters(objBillHd.getStrDiscountRemark()) + "'"
		+ ",'" + objUtility.funCheckSpecialCharacters(objBillHd.getStrTakeAwayRemarks()) + "','" + objBillHd.getStrDiscountOn() + "','" + objBillHd.getDblGrandTotalRoundOffBy() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		+ ",'" + objBillHd.getIntLastOrderNo() + "')";
	clsGlobalVarClass.dbMysql.execute(sqlInsert);

	sql = "update tblvoidbillhd "
		+ " set dblModifiedAmount='" + objBillHd.getDblGrandTotal() + "' "
		+ " where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' and strTransType='VB' ";
	clsGlobalVarClass.dbMysql.execute(sql);

	//update billseriesbilldtl grand total
	clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl "
		+ "set dblGrandTotal='" + objBillHd.getDblGrandTotal() + "' "
		+ "where strHdBillNo='" + lblVoucherNo.getText() + "' "
		+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");

	sqlDelete = "delete from tblbillmodifierdtl "
		+ " where strBillNo='" + lblVoucherNo.getText() + "' "
		+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	String sqlInsertBillModDtl = "insert into  tblbillmodifierdtl "
		+ "(strBillNo,strItemCode,strModifierCode,strModifierName,dblRate"
		+ ",dblQuantity,dblAmount,strClientCode,strCustomerCode"
		+ ",strDataPostFlag,strMMSDataPostFlag,dblDiscPer,dblDiscAmt,dteBillDate )"
		+ " values ";
	for (clsBillModifierDtl objBillModDtl : arrListBillModifierDtl)
	{
	    String sql = sqlInsertBillModDtl + "('" + objBillModDtl.getStrBillNo() + "','" + objBillModDtl.getStrItemCode() + "'"
		    + ",'" + objBillModDtl.getStrModifierCode() + "','" + objBillModDtl.getStrModifierName() + "'"
		    + "," + objBillModDtl.getDblRate() + "," + objBillModDtl.getDblQuantity() + "," + objBillModDtl.getDblAmount() + ""
		    + ",'" + objBillModDtl.getStrClientCode() + "','" + objBillModDtl.getStrCustomerCode() + "'"
		    + ",'" + objBillModDtl.getStrDataPostFlag() + "','" + objBillModDtl.getStrMMSDataPostFlag() + "','" + objBillModDtl.getDblDiscPer() + "','" + objBillModDtl.getDblDiscAmt() + "'"
		    + ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "') ";

	    clsGlobalVarClass.dbMysql.execute(sql);
	}

	sqlDelete = "delete from tblbilltaxdtl "
		+ " where strBillNo='" + lblVoucherNo.getText() + "' "
		+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);
	for (clsBillTaxDtl objBillTaxDtl : arrListBillTaxDtl)
	{
	    String sqlInsertTaxDtl = "insert into tblbilltaxdtl "
		    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate) "
		    + "values('" + objBillTaxDtl.getStrBillNo() + "','" + objBillTaxDtl.getStrTaxCode() + "'"
		    + "," + objBillTaxDtl.getDblTaxableAmount() + "," + objBillTaxDtl.getDblTaxAmount() + ""
		    + ",'" + clsGlobalVarClass.gClientCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";

	    clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
	}

	//delete all discount
	clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'");

	//update bill discount
	StringBuilder insertDisc = new StringBuilder("insert into tblbilldiscdtl values ");
	for (int i = 0; i < arrListBillDiscDtl.size(); i++)
	{
	    clsBillDiscountDtl objBillDiscountDtl = arrListBillDiscDtl.get(i);

	    if (i == 0)
	    {
		insertDisc.append("('" + lblVoucherNo.getText() + "','" + objBillDiscountDtl.getPOSCode() + "','" + objBillDiscountDtl.getDiscAmt() + "','" + objBillDiscountDtl.getDiscPer() + "','" + objBillDiscountDtl.getDiscOnAmt() + "',"
			+ "'" + objBillDiscountDtl.getDiscOnType() + "','" + objBillDiscountDtl.getDiscOnValue() + "','" + objBillDiscountDtl.getReason() + "','" + objUtility.funCheckSpecialCharacters(objBillDiscountDtl.getRemark()) + "','" + objBillDiscountDtl.getUserCreated() + "',"
			+ "'" + clsGlobalVarClass.gUserCode + "','" + objBillDiscountDtl.getDateCreated() + "','" + voidBillDate + "','" + clsGlobalVarClass.gClientCode + "','N'"
			+ ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
	    }
	    else
	    {
		insertDisc.append(",('" + lblVoucherNo.getText() + "','" + objBillDiscountDtl.getPOSCode() + "','" + objBillDiscountDtl.getDiscAmt() + "','" + objBillDiscountDtl.getDiscPer() + "','" + objBillDiscountDtl.getDiscOnAmt() + "',"
			+ "'" + objBillDiscountDtl.getDiscOnType() + "','" + objBillDiscountDtl.getDiscOnValue() + "','" + objBillDiscountDtl.getReason() + "','" + objUtility.funCheckSpecialCharacters(objBillDiscountDtl.getRemark()) + "','" + objBillDiscountDtl.getUserCreated() + "',"
			+ "'" + clsGlobalVarClass.gUserCode + "','" + objBillDiscountDtl.getDateCreated() + "','" + voidBillDate + "','" + clsGlobalVarClass.gClientCode + "','N'"
			+ ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
	    }
	}
	//insert new entries
	if (insertDisc.length() > 35)
	{
	    clsGlobalVarClass.dbMysql.execute(insertDisc.toString());
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
	    dm.addColumn("Bill Date");
	    dm.addColumn("Table Name");
	    String sql = "select a.strBillNo,a.dteBillDate,ifnull(b.strTableName,'') "
		    + " from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + " where a.strBillNo not in (select strBillNo from tblbillsettlementdtl) "
		    + " and (a.strBillNo Like '" + searchText + "%' or b.strTableName like '" + searchText + "%') "
		    + " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";

	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		Object[] rows
			=
			{
			    rs.getString(1), rs.getString(2), rs.getString(3)
			};
		dm.addRow(rows);
		tblBillDetails.setModel(dm);
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funResetField()
    {
	tblItemTable.setModel(new DefaultTableModel());
	tblBillDetails.setModel(new DefaultTableModel());
	lblVoucherNo.setText("");
	lblUserCreated.setText("");
	lblSubTotalValue.setText("");
	lblTaxValue.setText("");
	lblTotalAmt.setText("");
	panelHeader.setVisible(true);
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
        panelShowBills = new javax.swing.JPanel();
        scrBills = new javax.swing.JScrollPane();
        tblBillDetails = new javax.swing.JTable();
        lblBillNo = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        OrderPanel = new javax.swing.JPanel();
        scrItemDtlGrid = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        lblPaxNo = new javax.swing.JLabel();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
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
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

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
        lblformName.setText("- Void Bill");
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

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelShowBills.setBackground(new java.awt.Color(255, 255, 255));
        panelShowBills.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelShowBills.setOpaque(false);
        panelShowBills.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblBillDetails.setBackground(new java.awt.Color(254, 254, 254));
        tblBillDetails.setForeground(new java.awt.Color(1, 1, 1));
        tblBillDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No", "Bill Date"
            }
        ));
        tblBillDetails.setRowHeight(25);
        tblBillDetails.setSelectionBackground(new java.awt.Color(0, 120, 255));
        tblBillDetails.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblBillDetails.getTableHeader().setReorderingAllowed(false);
        tblBillDetails.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblBillDetailsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                tblBillDetailsMouseEntered(evt);
            }
        });
        scrBills.setViewportView(tblBillDetails);

        panelShowBills.add(scrBills, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 51, 446, 513));

        lblBillNo.setText("Bill No.");
        panelShowBills.add(lblBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 11, 50, 30));

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
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchKeyReleased(evt);
            }
        });
        panelShowBills.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 11, 123, 30));

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
        panelShowBills.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 11, 80, 34));

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");
        panelShowBills.add(lblSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 50, 30));

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
        scrItemDtlGrid.setBounds(0, 50, 340, 360);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");
        OrderPanel.add(lblTotal);
        lblTotal.setBounds(150, 470, 70, 40);
        OrderPanel.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPDark.png"))); // NOI18N
        btnUp.setToolTipText("Up");
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
        OrderPanel.add(btnUp);
        btnUp.setBounds(10, 520, 60, 40);

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownDark.png"))); // NOI18N
        btnDown.setToolTipText("Down");
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownLite.png"))); // NOI18N
        btnDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownMouseClicked(evt);
            }
        });
        OrderPanel.add(btnDown);
        btnDown.setBounds(80, 520, 60, 40);

        lblBillNo1.setText("Bill No. :");
        OrderPanel.add(lblBillNo1);
        lblBillNo1.setBounds(10, 10, 50, 30);
        OrderPanel.add(lblVoucherNo);
        lblVoucherNo.setBounds(80, 10, 80, 30);

        lblSubTotalTitle.setText("Sub Total ");
        OrderPanel.add(lblSubTotalTitle);
        lblSubTotalTitle.setBounds(150, 410, 70, 30);

        lblSubTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblSubTotalValue);
        lblSubTotalValue.setBounds(240, 410, 100, 30);

        lblTaxTitle.setText("Tax");
        OrderPanel.add(lblTaxTitle);
        lblTaxTitle.setBounds(150, 440, 70, 30);

        lblTaxValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblTaxValue);
        lblTaxValue.setBounds(240, 440, 100, 30);
        OrderPanel.add(lblUserCreated);
        lblUserCreated.setBounds(240, 10, 100, 30);

        lblTotalAmt.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalAmt.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderPanel.add(lblTotalAmt);
        lblTotalAmt.setBounds(240, 470, 100, 40);

        lblUserName.setText("User :");
        OrderPanel.add(lblUserName);
        lblUserName.setBounds(170, 10, 50, 30);

        btnVoidBill.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVoidBill.setForeground(new java.awt.Color(255, 255, 255));
        btnVoidBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnVoidBill.setText("FULL VOID");
        btnVoidBill.setToolTipText("Full Void");
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
        btnVoidBill.setBounds(240, 520, 100, 40);

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setToolTipText("Save");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        OrderPanel.add(btnSave);
        btnSave.setBounds(60, 420, 80, 40);

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });
        OrderPanel.add(btnDelete);
        btnDelete.setBounds(10, 420, 40, 40);

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(OrderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelShowBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(OrderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panelShowBills, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblBillDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailsMouseClicked
	// TODO add your handling code here:
	funSelectBill();
    }//GEN-LAST:event_tblBillDetailsMouseClicked

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
	// TODO add your handling code here:
	frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Enter Bill No.");
	keyboard.setVisible(true);
	keyboard.setAlwaysOnTop(true);
	keyboard.setAutoRequestFocus(true);
	txtSearch.setText(clsGlobalVarClass.gKeyboardValue);

	//funSearchBillNo(txtSearch.getText().trim());
	funFillBillNoGrid(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Void Bill");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
	//selectedVoidQty = Double.parseDouble(tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 1).toString());
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
	// TODO add your handling code here:
	if (btnUp.isEnabled())
	{
	    if (tblItemTable.getModel().getRowCount() > 0)
	    {
		int r = tblItemTable.getSelectedRow();
		tblItemTable.changeSelection(r - 1, 0, false, false);
	    }
	    else
	    {
		new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    }
	}
    }//GEN-LAST:event_btnUpMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
	if (btnUp.isEnabled())
	{        // TODO add your handling code here:

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
	}
    }//GEN-LAST:event_btnDownMouseClicked

    private void btnVoidBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoidBillActionPerformed

	try
	{
	    String formName = "Void Bill";
	    buttonClicked = "FullVoid";
	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
	    {
		funVoidBill();
	    }
	    else
	    {

		boolean isTrue = objUtility2.funHasTLA(formName, clsGlobalVarClass.gUserCode);
		if (isTrue)
		{
		    String isValidUser = "Invalid User";
		    frmUserAuthenticationPopUp okCancelPopUp = new frmUserAuthenticationPopUp(null, "User Authentication!!!", formName);
		    okCancelPopUp.setVisible(true);
		    String enterUserCode = okCancelPopUp.getUserName();
		    String enterPassword = okCancelPopUp.getPassword();
		    int res2 = okCancelPopUp.getResult();

		    if (res2 == 1) // pressing OK button
		    {
			isValidUser = objUtility2.funIsValidUser(enterUserCode, enterPassword);
		    }

		    if (isValidUser.equalsIgnoreCase("Valid User"))
		    {
			boolean isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
			if (isUserGranted)
			{
			    funSetTransactionUserDtl(enterUserCode);

			    funVoidBill();

			    funResetTransactionUserDtl();
			}
			else
			{
			    new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
			}
		    }
		    else
		    {
			new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
		    }
		}
		else
		{
		    funVoidBill();
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnVoidBillActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyPressed
    {//GEN-HEADEREND:event_txtSearchKeyPressed

    }//GEN-LAST:event_txtSearchKeyPressed

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
	clsGlobalVarClass.hmActiveForms.remove("Void Bill");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Void Bill");
    }//GEN-LAST:event_formWindowClosing

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyReleased
    {//GEN-HEADEREND:event_txtSearchKeyReleased
	funFillBillNoGrid(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed

	if (btnSave.isEnabled())
	{

	    try
	    {
		if (mapItemToBeDeleted.size() < 1)
		{
		    JOptionPane.showMessageDialog(this, "Please select Item");
		    return;
		}

		buttonClicked = "ItemVoid";
		String formName = "Void Bill";

		if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
		{
		    funVoidItem();
		}
		else
		{

		    boolean isTrue = objUtility2.funHasTLA(formName, clsGlobalVarClass.gUserCode);
		    if (isTrue)
		    {
			String isValidUser = "Invalid User";
			frmUserAuthenticationPopUp okCancelPopUp = new frmUserAuthenticationPopUp(null, "User Authentication!!!", formName);
			okCancelPopUp.setVisible(true);
			String enterUserCode = okCancelPopUp.getUserName();
			String enterPassword = okCancelPopUp.getPassword();
			int res2 = okCancelPopUp.getResult();

			if (res2 == 1) // pressing OK button
			{
			    isValidUser = objUtility2.funIsValidUser(enterUserCode, enterPassword);
			}

			if (isValidUser.equalsIgnoreCase("Valid User"))
			{
			    boolean isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
			    if (isUserGranted)
			    {
				funSetTransactionUserDtl(enterUserCode);

				funVoidItem();

				funResetTransactionUserDtl();
			    }
			    else
			    {
				new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
			    }
			}
			else
			{
			    new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
			}
		    }
		    else
		    {
			funVoidItem();
		    }
		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
	if (btnDelete.isEnabled())
	{
	    funDeleteButtonClicked();
	}
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblBillDetailsMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblBillDetailsMouseEntered
    {//GEN-HEADEREND:event_tblBillDetailsMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_tblBillDetailsMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUp;
    private javax.swing.JButton btnVoidBill;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblBillNo1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
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
    private javax.swing.JTable tblBillDetails;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private void funUpdateDiscountForModifier(String itemCode, String itemName, String modifierCode, String billNo, String qty, String amount, double toBeVoidQty)
    {
	try
	{
	    String discountOnType = null;
	    if (arrListBillDiscDtl.size() > 0)
	    {
		discountOnType = arrListBillDiscDtl.get(0).getDiscOnType();
	    }
	    double itemQty = Double.parseDouble(qty);

	    if (discountOnType != null && discountOnType.equalsIgnoreCase("ItemWise"))
	    {
		for (clsBillDiscountDtl objBillDiscountDtl : arrListBillDiscDtl)
		{
		    if (itemName.equalsIgnoreCase(objBillDiscountDtl.getDiscOnValue()))
		    {

			if (itemQty - toBeVoidQty == 0)
			{
			    arrListBillDiscDtl.remove(objBillDiscountDtl);
			}
			else
			{
			    double discPerItemQty = objBillDiscountDtl.getDiscAmt() / itemQty;
			    double discOnAmtPerItemQty = objBillDiscountDtl.getDiscOnAmt() / itemQty;

			    itemQty = itemQty - toBeVoidQty;
			    objBillDiscountDtl.setDiscAmt(discPerItemQty * itemQty);
			    objBillDiscountDtl.setDiscOnAmt(discOnAmtPerItemQty * itemQty);
			}
		    }
		    else
		    {
			continue;
		    }
		}
	    }
	    else if (discountOnType != null && discountOnType.equalsIgnoreCase("Total"))
	    {
		if (arrListBillDiscDtl.size() > 0)
		{
		    clsBillDiscountDtl objBillDiscountDtl = arrListBillDiscDtl.get(0);

		    double discPerItemQty = objBillDiscountDtl.getDiscAmt() / itemQty;
		    double discOnAmtPerItemQty = objBillDiscountDtl.getDiscOnAmt() / itemQty;

		    itemQty = itemQty - toBeVoidQty;
		    objBillDiscountDtl.setDiscAmt(discPerItemQty * itemQty);
		    objBillDiscountDtl.setDiscOnAmt(discOnAmtPerItemQty * itemQty);
		}
	    }
	    else if (discountOnType != null && discountOnType.equalsIgnoreCase("GroupWise"))
	    {

	    }
	    else if (discountOnType != null && discountOnType.equalsIgnoreCase("SubGroupWise"))
	    {

	    }

	    //delete all discount
	    clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'");

	    //update bill discount
	    StringBuilder insertDisc = new StringBuilder("insert into tblbilldiscdtl values ");
	    for (int i = 0; i < arrListBillDiscDtl.size(); i++)
	    {
		clsBillDiscountDtl objBillDiscountDtl = arrListBillDiscDtl.get(i);

		if (i == 0)
		{
		    insertDisc.append("('" + billNo + "','" + objBillDiscountDtl.getPOSCode() + "','" + objBillDiscountDtl.getDiscAmt() + "','" + objBillDiscountDtl.getDiscPer() + "','" + objBillDiscountDtl.getDiscOnAmt() + "',"
			    + "'" + objBillDiscountDtl.getDiscOnType() + "','" + objBillDiscountDtl.getDiscOnValue() + "','" + objBillDiscountDtl.getReason() + "','" + objBillDiscountDtl.getRemark() + "','" + objBillDiscountDtl.getUserCreated() + "',"
			    + "'" + clsGlobalVarClass.gUserCode + "','" + objBillDiscountDtl.getDateCreated() + "','" + voidBillDate + "','" + clsGlobalVarClass.gClientCode + "','N'"
			    + ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		}
		else
		{
		    insertDisc.append(",('" + billNo + "','" + objBillDiscountDtl.getPOSCode() + "','" + objBillDiscountDtl.getDiscAmt() + "','" + objBillDiscountDtl.getDiscPer() + "','" + objBillDiscountDtl.getDiscOnAmt() + "',"
			    + "'" + objBillDiscountDtl.getDiscOnType() + "','" + objBillDiscountDtl.getDiscOnValue() + "','" + objBillDiscountDtl.getReason() + "','" + objBillDiscountDtl.getRemark() + "','" + objBillDiscountDtl.getUserCreated() + "',"
			    + "'" + clsGlobalVarClass.gUserCode + "','" + objBillDiscountDtl.getDateCreated() + "','" + voidBillDate + "','" + clsGlobalVarClass.gClientCode + "','N'"
			    + ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		}
	    }
	    //insert new entries
	    if (insertDisc.length() > 35)
	    {
		clsGlobalVarClass.dbMysql.execute(insertDisc.toString());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGetTableStatus(String tableNo)
    {
	String tableStatus = "Normal";
	try
	{
	    String sql = "select strTableNO from tblitemrtemp where strTableNO='" + tableNo + "' ";
	    ResultSet rsNext = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsNext.next())
	    {
		tableStatus = "Occupied";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return tableStatus;
	}
    }

    private void funSetTransactionUserDtl(String userCode)
    {
	this.transactionUserCode = userCode;

    }

    private void funResetTransactionUserDtl()
    {
	this.transactionUserCode = null;
    }

    private void funCloseAuthenticationDialog()
    {
	Window[] windows = Window.getWindows();
	for (Window window : windows)
	{
	    if (window instanceof JDialog)
	    {
		JDialog dialog = (JDialog) window;
		if (dialog.getContentPane().getComponentCount() == 1 && dialog.getContentPane().getComponent(0) instanceof JOptionPane)
		{
		    dialog.dispose();
		}
	    }
	}
    }

    private void funDeleteButtonClicked()
    {

	double selectedVoidQty = 0.00;

	int selectedRow = tblItemTable.getSelectedRow();
	if (selectedRow < 0)
	{
	    JOptionPane.showMessageDialog(this, "Please select item");
	    return;
	}
	if (Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString()) > 1)
	{
	    new frmNumericKeyboard(this, true, "", "VoidBill", "Enter quantity to void").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.length() == 0)
	    {
		return;
	    }
	    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString()))
	    {
		JOptionPane.showMessageDialog(this, "Please select valid quantity");
		return;
	    }

	    if (arrListBillDtl.size() == 1 && Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) == Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString()))
	    {
		JOptionPane.showMessageDialog(this, "You Can Do Full Bill Void.");
		return;
	    }

	    double quantity = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
	    if (quantity == 0)
	    {

	    }
	    else
	    {
		selectedVoidQty = Integer.parseInt(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	else
	{
	    selectedVoidQty = 1;
	}

	DefaultTableModel dtm = (DefaultTableModel) tblItemTable.getModel();

	String itemName = tblItemTable.getValueAt(selectedRow, 0).toString();
	double itemQty = Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString());
	String itemCode = tblItemTable.getValueAt(selectedRow, 3).toString();
	String modifierCode = tblItemTable.getValueAt(selectedRow, 4).toString();

	if (itemName.startsWith("-->"))
	{
	    JOptionPane.showMessageDialog(this, "Please select Item.");
	    return;
	}

	if (itemQty == selectedVoidQty)
	{
	    clsVoidBillDtl obj = null;
	    if (mapItemToBeDeleted.containsKey(itemCode))
	    {
		obj = mapItemToBeDeleted.get(itemCode);
		obj.setIntQuantity(obj.getIntQuantity() + selectedVoidQty);
	    }
	    else
	    {
		obj = new clsVoidBillDtl();
		obj.setStrItemCode(itemCode);
		obj.setStrItemName(itemName);
		obj.setIntQuantity(selectedVoidQty);
	    }
	    mapItemToBeDeleted.put(itemCode, obj);
	    dtm.removeRow(selectedRow);

	    for (int r = selectedRow; r < tblItemTable.getRowCount();)
	    {
		String nextItemName = tblItemTable.getValueAt(r, 0).toString();
		if (nextItemName.startsWith("-->"))
		{
		    double nextItemQty = Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString());
		    String nextModifierCode = tblItemTable.getValueAt(selectedRow, 3).toString();//M99
		    String nextItemCode = tblItemTable.getValueAt(selectedRow, 4).toString();//I000025M99

		    clsVoidBillModifierDtl objModi = null;
		    if (mapItemModifierToBeDeleted.containsKey(nextItemCode))
		    {
			objModi = mapItemModifierToBeDeleted.get(nextItemCode);
			objModi.setDblQuantity(objModi.getDblQuantity() + nextItemQty);
		    }
		    else
		    {
			objModi = new clsVoidBillModifierDtl();
			objModi.setStrItemCode(nextItemCode);
			objModi.setStrModifierCode(nextModifierCode);
			objModi.setStrModifierName(nextItemName);
			objModi.setDblQuantity(nextItemQty);
		    }
		    mapItemModifierToBeDeleted.put(nextItemCode, objModi);

		    dtm.removeRow(r);

		}
		else
		{
		    break;
		}
	    }
	}
	else
	{
	    clsVoidBillDtl obj = null;
	    if (mapItemToBeDeleted.containsKey(itemCode))
	    {
		obj = mapItemToBeDeleted.get(itemCode);
		obj.setIntQuantity(obj.getIntQuantity() + selectedVoidQty);
	    }
	    else
	    {
		obj = new clsVoidBillDtl();
		obj.setStrItemCode(itemCode);
		obj.setStrItemName(itemName);
		obj.setIntQuantity(selectedVoidQty);
	    }
	    mapItemToBeDeleted.put(itemCode, obj);

	    double qty = Double.parseDouble(tblItemTable.getValueAt(selectedRow, 1).toString());
	    double amt = Double.parseDouble(tblItemTable.getValueAt(selectedRow, 2).toString());
	    double rate = amt / qty;

	    double newQty = qty - selectedVoidQty;
	    double newAmt = newQty * rate;

	    tblItemTable.setValueAt(newQty, selectedRow, 1);
	    tblItemTable.setValueAt(newAmt, selectedRow, 2);
	}

    }

    private double funGetOriginalItemQty(String tempItemCode)
    {
	double originalQty = 0.00;

	for (clsBillDtl objBillDtl : arrListBillDtl)
	{
	    if (tempItemCode.equals(objBillDtl.getStrItemCode()))
	    {
		originalQty = objBillDtl.getDblQuantity();
		break;
	    }
	}

	return originalQty;
    }

    private void funSendVoidBillSMS(String billNo, String mobileNo) throws Exception
    {
	clsUtility2 objUtility2 = new clsUtility2();
	StringBuilder mainSMSBuilder = new StringBuilder();

	String sql = "select a.strBillNo,d.strPosName,ifnull(b.strTableName,''),ifnull(c.strWFullName,'')  "
		+ ",a.strReasonName,a.strRemark,a.dblActualAmount,a.dblModifiedAmount "
		+ ",TIME_FORMAT(time(a.dteModifyVoidBill),'%h:%i')voidTime "
		+ "from tblvoidbillhd  a "
		+ "left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		+ "left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo "
		+ "left outer join tblposmaster d on a.strPosCode=d.strPosCode "
		+ "where a.strBillNo='" + billNo + "' and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		+ "and a.strTransType='VB' ";
	ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	mainSMSBuilder.append("VoidBill");
	mainSMSBuilder.append(" ,Bill No:" + billNo);
	mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
	mainSMSBuilder.append(" ,User:" + userCode);
	if (rsVoidBill.next())
	{
	    mainSMSBuilder.append(" ,Table:" + rsVoidBill.getString(3));
	    mainSMSBuilder.append(" ,Waiter:" + rsVoidBill.getString(4));
	    mainSMSBuilder.append(" ,Time:" + rsVoidBill.getString(9));
	    mainSMSBuilder.append(" ,Actual Amount:" + gDecimalFormat.format(rsVoidBill.getDouble(7)));
	    mainSMSBuilder.append(" ,Modified Amount:" + gDecimalFormat.format(rsVoidBill.getDouble(8)));
	    mainSMSBuilder.append(" ,Reason:" + rsVoidBill.getString(5));
	    mainSMSBuilder.append(" ,Remarks:" + rsVoidBill.getString(6));
	}

	ArrayList<String> mobileNoList = new ArrayList<>();
	String mobNos[] = mobileNo.split(",");
	for (String mn : mobNos)
	{
	    mobileNoList.add(mn);
	}
	clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
	objSMSSender.start();
    }

    private int funInsertBillTaxDtlTable(List<clsBillTaxDtl> listObjBillTaxDtl, String billno) throws Exception
    {
	int rows = 0;
	String sqlDelete = "delete from tblvoidbilltaxdtl where strBillNo='" + billno + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	for (clsBillTaxDtl objBillTaxDtl : listObjBillTaxDtl)
	{
	    String sqlInsertTaxDtl = "insert into tblvoidbilltaxdtl "
		    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate,strTransType) "
		    + "values('" + objBillTaxDtl.getStrBillNo() + "','" + objBillTaxDtl.getStrTaxCode() + "'"
		    + "," + objBillTaxDtl.getDblTaxableAmount() + "," + objBillTaxDtl.getDblTaxAmount() + ""
		    + ",'" + clsGlobalVarClass.gClientCode + "','" + objBillTaxDtl.getDteBillDate() + "','VB')";
	    rows += clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
	}
	return rows;
    }

    private int funInsertBillDiscDtlTable(List<clsBillDiscountDtl> listObjBillDiscDtl, String billno) throws Exception
    {
	int rows = 0;
	String sqlDelete = "delete from tblvoidbilldiscdtl where strBillNo='" + billno + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	for (clsBillDiscountDtl objBillDiscDtl : listObjBillDiscDtl)
	{
	    String billDate = "";
	    String sql = "select dteBillDate from tblbilldiscdtl where strBillNo='" + billno + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	    ResultSet rsBillDate = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillDate.next())
	    {
		billDate = rsBillDate.getString(1);
	    }
	    rsBillDate.close();

	    String sqlInsertTaxDtl = "insert into tblvoidbilldiscdtl "
		    + "(strBillNo,strPOSCode,dblDiscAmt,dblDiscPer,dblDiscOnAmt,strDiscOnType,strDiscOnValue,strDiscReasonCode,"
		    + "strDiscRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode "
		    + ",strDataPostFlag,dteBillDate,strTransType)"
		    + "values('" + objBillDiscDtl.getBillNo() + "','" + objBillDiscDtl.getPOSCode() + "'"
		    + "," + objBillDiscDtl.getDiscAmt() + ",'" + objBillDiscDtl.getDiscPer() + "'"
		    + ",'" + objBillDiscDtl.getDiscOnAmt() + "','" + objBillDiscDtl.getDiscOnType() + "'"
		    + ",'" + objBillDiscDtl.getDiscOnValue() + "','" + objBillDiscDtl.getReason() + "'"
		    + ",'" + objBillDiscDtl.getRemark() + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + billDate + "'"
		    + ",'VB')";
	    rows += clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
	}
	return rows;
    }

}
