package com.POSTransaction.view;

import com.POSGlobal.controller.clsCRMInterface;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsItemPriceDtl;
import com.POSGlobal.controller.clsPLUItemDtl;
import com.POSGlobal.controller.clsRewards;
import com.POSGlobal.controller.clsTDHOnItemDtl;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmHomeDeliveryAddress;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmNumberKeyPadWithPurRate;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSGlobal.view.frmUserAuthenticationPopUp;

import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.POSTransaction.controller.nfc.ReaderThread;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class frmDirectBiller extends javax.swing.JFrame
{

    static List<String> arrListItemCombination = new ArrayList<String>();
    private int menuCount, nextCnt, nextClick, nextItemClick = 0;
    private String[] menuNames, menuNames1, itemNames;
    private String menuHeadCode, sql;
    private double totalAmt = 0.00, selectedQty, noOfBoxes, itemWeightPerBox = 0.00,dblDiscountAmt=0.00;
    boolean PervBil = false, flgChangeQty = false;
    private String btnforeground[];
    public static String Itemcode;
    private static String TDH_Combo_Itemcode;
    public static boolean flagTDHItem;
    double MaxSubItemLimitWithComboItem = 0.00;
    private ArrayList<String> listTopButtonName, listTopButtonCode;
    private int itemNumber, totalItems;
    private boolean flgPopular;
    private ArrayList<String> list_ItemNames_Buttoms = new ArrayList();
    private List<clsDirectBillerItemDtl> obj_List_ItemDtl;
    private List<clsItemPriceDtl> obj_List_ItemPrice;
    public boolean flag_isTDHModifier_Item = false, flgBillForAdvOrder;
    private String CANCEL_ACTION = "cancel-search", advOrderCustCode;
    private String cmsMemberCode, cmsMemberName;
    public int tblStartIndex, tblIndex;
    private String temp_ItemCode;
    private Double MaxQTYOfModifierWithTDHItem;
    private boolean isModifierSelect;
    private HashMap<String, frmDirectBiller.clsModifierGroupDtl> hm_ModifierGroup = null;
    private HashMap<String, frmDirectBiller.clsModifierDtl> hm_ModifierDtl = null;
    private boolean flagOpenItem = false;
    private clsCustomerDataModelForSQY objData;
    private panelModifier objPanelModifier;
    private panelSubItem objPanelSubItem;
    private frmAgainstAdvBookorder objAgainstAdvOrder;
    private HashMap<String, List<clsItemPriceDtl>> hmHappyHourItems = null;
    private ArrayList<String> itemImageCode = null;
    private String seqNo;
    private int serNo = 0;
    private HashMap<String, String> hmDirectBillerParams;
    private double itemWeight = 0;
    private double homeDelCharges = 0;
    private clsUtility objUtility;
    private double debitCardBalance;
    private ArrayList<ArrayList<Object>> arrListTaxCal;
    private String dineInForTax, takeAwayForTax, homeDeliveryForTax;
    private StringBuilder sqlBuilder = new StringBuilder();
    private String billTransType = "";
    private clsMakeKotItemDtl objOpenItemDtl = new clsMakeKotItemDtl();
    private clsItemPriceDtl objGlobalPriceObject;
    private clsUtility2 objUtility2 = new clsUtility2();
    private clsRewards objCustomerRewards;
    private String selectedMenuHeadName;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public String getSeqNo()
    {
	seqNo = String.valueOf(serNo);
	return seqNo;
    }

    public frmDirectBiller(String formName, List<clsDirectBillerItemDtl> listDirectBillerItemDtl)
    {
	this.obj_List_ItemDtl = listDirectBillerItemDtl;
    }

    public frmDirectBiller()
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
	    homeDelCharges = 0;
	    hmDirectBillerParams = new HashMap<String, String>();
	    hmHappyHourItems = new HashMap<String, List<clsItemPriceDtl>>();
	    cmsMemberCode = "";
	    cmsMemberName = "";
	    selectedQty = 1;
	    clsGlobalVarClass.gCustomerCode = "";
	    clsGlobalVarClass.gCustMobileNoForCRM = "";
	    clsGlobalVarClass.gFlgPoints = "";
	    panelPLU.setVisible(false);
	    obj_List_ItemDtl = new ArrayList<>();
	    flgPopular = false;
	    btnPLU.setMnemonic('p');
	    btnSettle.setMnemonic('d');
	    InputMap im = txtPLUItemSearch.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = txtPLUItemSearch.getActionMap();
	    im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
	    am.put(CANCEL_ACTION, new frmDirectBiller.CancelAction());
	    funResetSortingButtons();
	    txtExternalCode.requestFocus();
	    btnforeground = new String[16];
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    //  btnCustomerHistory.setVisible(false);
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    lblDateTime.setText(bdte);
	    btnChangeQty.setVisible(false);

	    //To add TDH item List Globally as static only once
	    if (clsGlobalVarClass.ListTDHOnModifierItem.isEmpty())
	    {
		objUtility.addTDHOnModifierItem();
	    }

	    dineInForTax = "Y";
	    takeAwayForTax = "N";
	    homeDeliveryForTax = "N";

	    funLoadMenuNames();
	    funPopularItem();

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
	    itemImageCode = new ArrayList<String>();
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    funSetShortCutKeys();
	    funFillMapWithHappyHourItems();
	    debitCardBalance = 0;

	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		lblformName.setText(lblformName.getText() + "  " + clsGlobalVarClass.gCounterName);
	    }

	    if (clsGlobalVarClass.gShowItemStkColumnInDB)
	    {
		Date dtToday = new Date();
		clsGlobalVarClass.funCalculateStock(clsGlobalVarClass.gStartDate, dtToday, clsGlobalVarClass.gPOSCode, "Both", "Stock");
	    }

	    if (clsGlobalVarClass.gClientCode.equals("213.001"))
	    {
		btnDelBoy.setText("<html><h5>BEAUTY<br>THEOROPIST</h5></html>");
		btnDelBoy.setToolTipText("SELECT STYLIST");
	    }
	    else
	    {
		btnDelBoy.setText("<html>Delivery<br> Boy</html>");
		btnDelBoy.setToolTipText("Select Delivery Boy");
	    }
	    
	    String areaName="<html>";
	    String strArea[]=funGetAreaName(clsGlobalVarClass.gDineInAreaForDirectBiller).split(" ");
	    for(String area:strArea){
		areaName+="<br>"+area;
	    }
	    areaName=areaName+"</html>";
	    btnArea.setText(areaName);
	    panelArea.setVisible(false);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public HashMap<String, String> getHmDirectBillerParams()
    {
	return hmDirectBillerParams;
    }

    public frmDirectBiller(String advOrderNo, String advOrderCustCode)
    {
	initComponents();
	try
	{
	    objUtility = new clsUtility();
	    hmDirectBillerParams = new HashMap<String, String>();
	    hmHappyHourItems = new HashMap<String, List<clsItemPriceDtl>>();
	    cmsMemberCode = "";
	    cmsMemberName = "";
	    clsGlobalVarClass.gBillingType = "Direct Biller";
	    selectedQty = 1;
	    this.advOrderCustCode = advOrderCustCode;
	    flgBillForAdvOrder = true;
	    panelPLU.setVisible(false);
	    obj_List_ItemDtl = new ArrayList<>();
	    flgPopular = false;
	    btnPLU.setMnemonic('p');
	    btnSettle.setMnemonic('d');
	    InputMap im = txtPLUItemSearch.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = txtPLUItemSearch.getActionMap();
	    im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
	    am.put(CANCEL_ACTION, new CancelAction());
	    funResetSortingButtons();
	    txtExternalCode.requestFocus();
	    btnforeground = new String[16];

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    //  btnCustomerHistory.setVisible(false);
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    lblDateTime.setText(bdte);

	    dineInForTax = "Y";
	    takeAwayForTax = "N";
	    homeDeliveryForTax = "N";

	    //To add TDH item List Globally as static only once
	    if (clsGlobalVarClass.ListTDHOnModifierItem.isEmpty())
	    {
		objUtility.addTDHOnModifierItem();
	    }
	    btnChangeQty.setVisible(false);
	    funLoadMenuNames();
	    funPopularItem();
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
	    itemImageCode = new ArrayList<String>();

	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		lblformName.setText(lblformName.getText() + "  " + clsGlobalVarClass.gCounterName);
	    }
	    clsGlobalVarClass.gCustomerCode = "";

	    funSetAdvOrderNo(advOrderNo);
	    funFillMapWithHappyHourItems();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funFillMapWithHappyHourItems() throws Exception
    {
	String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	{
	    sql = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
		    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
		    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
		    + ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " WHERE a.strItemCode=b.strItemCode "
		    + " and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and a.strHourlyPricing='Yes' "
		    + " and b.strOperationalYN='Y' ";
	}
	else
	{
	    String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    if (dineInForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    }

	    if (homeDeliveryForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	    }

	    if (takeAwayForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	    }

	    sql = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
		    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
		    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate "
		    + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " WHERE a.strAreaCode='" + areaWisePricingAreaCode + "' "
		    + " and a.strItemCode=b.strItemCode "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and a.strHourlyPricing='Yes' "
		    + " and b.strOperationalYN='Y' ";
	}

	hmHappyHourItems.clear();
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rs.next())
	{
	    clsItemPriceDtl ob = new clsItemPriceDtl(rs.getString(1), rs.getString(2), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(3), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getDouble(21), rs.getString(22));
	    if (hmHappyHourItems.containsKey(ob.getStrItemName()))
	    {
		List<clsItemPriceDtl> listOfItemPriceDtl = hmHappyHourItems.get(ob.getStrItemName());
		listOfItemPriceDtl.add(ob);

		hmHappyHourItems.put(ob.getStrItemName(), listOfItemPriceDtl);
	    }
	    else
	    {
		List<clsItemPriceDtl> listOfItemPriceDtl = new ArrayList<>();
		listOfItemPriceDtl.add(ob);

		hmHappyHourItems.put(ob.getStrItemName(), listOfItemPriceDtl);
	    }
	}
	rs.close();
    }

    private void funSetShortCutKeys()
    {
	btnChangeQty.setVisible(true);
	btnHome.setMnemonic('h');
	btnModifier.setMnemonic('f');
	btnHomeDelivery.setMnemonic('o');
	btnTakeAway.setMnemonic('t');
	btnAgainstAdvOrder.setMnemonic('a');
	btnDelBoy.setMnemonic('b');
	btnSettle.setMnemonic('d');
	btnUp.setMnemonic('u');
	btnDown.setMnemonic('w');
	btnDelItem.setMnemonic('l');
	btnChangeQty.setMnemonic('q');
	bttnPLUPanelClose.setMnemonic('s');

    }

    private void funLoadMenuNames()
    {
	try
	{
	    JButton[] btnMenuArray =
	    {
		btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
	    };
	    int i = 0;
	    btnPrevMenu.setEnabled(false);
	    btnNextMenu.setEnabled(false);
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		sql = "select count(distinct(a.strMenuCode)) "
			+ "from tblmenuitempricingdtl a left outer join tblmenuhd b on a.strMenuCode=b.strMenuCode "
			+ "left outer join tblcounterdtl c on b.strMenuCode=c.strMenuCode "
			+ "left outer join tblcounterhd d on c.strCounterCode=d.strCounterCode "
			+ "where d.strOperational='Yes' "
			+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
			+ "and c.strCounterCode='" + clsGlobalVarClass.gCounterCode + "'";
	    }
	    else
	    {
		sql = "select count(distinct(a.strMenuCode)) from tblmenuitempricingdtl a,tblmenuhd b "
			+ "where a.strMenuCode=b.strMenuCode and b.strOperational='Y'  "
			+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL')";
	    }
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsMenuHead.next())
	    {
		menuCount = rsMenuHead.getInt(1);
	    }
	    if (menuCount > 7)
	    {
		btnNextMenu.setEnabled(true);
	    }
	    menuNames1 = new String[menuCount];
	    menuNames = new String[menuCount];

	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		sql = "select distinct(a.strMenuCode),b.strMenuName "
			+ "from tblmenuitempricingdtl a left outer join tblmenuhd b on a.strMenuCode=b.strMenuCode "
			+ "left outer join tblcounterdtl c on b.strMenuCode=c.strMenuCode "
			+ "left outer join tblcounterhd d on c.strCounterCode=d.strCounterCode "
			+ "where d.strOperational='Yes' "
			+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
			+ "and c.strCounterCode='" + clsGlobalVarClass.gCounterCode + "' "
			//+ "ORDER by b.intSequence";
			+ "ORDER by b.strMenuName";
	    }
	    else
	    {
		sql = "select distinct(a.strMenuCode),b.strMenuName "
			+ "from tblmenuitempricingdtl a left outer join tblmenuhd b "
			+ "on a.strMenuCode=b.strMenuCode "
			+ "where  b.strOperational='Y' "
			+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
			+ "ORDER by b.intSequence";
		//+ "ORDER by b.strMenuName";
	    }
	    //System.out.println(sql);
	    rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    for (int m = 0; m < 7; m++)
	    {
		btnMenuArray[m].setText("");
		btnMenuArray[m].setEnabled(false);
	    }
	    while (rsMenuHead.next())
	    {
		if (i < 7)
		{
		    String strMenuName = rsMenuHead.getString(2);
		    btnMenuArray[i].setText(clsGlobalVarClass.convertString(strMenuName));
		    btnMenuArray[i].setEnabled(true);
		}
		menuNames[i] = rsMenuHead.getString(2);
		menuNames1[i] = rsMenuHead.getString(2);
		i++;
	    }
	    rsMenuHead.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public void tickTock()
    {
	lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }
    //show the panel in close button of panelModifier and PanelPlu

    public void funShowPanel()
    {
	panelMenuHead.setVisible(true);
	panelItemList.setVisible(true);
	panelNavigate.setVisible(true);
	panelSubGroup.setVisible(true);

	if (objPanelSubItem != null)
	{
	    objPanelSubItem.setVisible(false);
	    objPanelSubItem = null;
	}
    }

    //function for Modifier(call by panelModifier form) to getModifier rate
    public void funGetModifierRate(String ModifierName, String itemCodeForModifier)
    {
	try
	{
	    double modifierAmount = 0.00, modifierQty = 1.00;
	    String modifierCode = "";
	    sql = "select strModifierCode from tblmodifiermaster where strModifierName='" + ModifierName + "'";//to retrive the modifiercode
	    ResultSet rsModiCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsModiCode.next())
	    {
		modifierCode = rsModiCode.getString(1);
	    }
	    rsModiCode.close();

	    //retrive the rate of modifier
	    sql = "select dblRate from tblitemmodofier "
		    + " where strModifierCode='" + modifierCode + "' "
		    + " and strItemCode='" + itemCodeForModifier + "' "
		    + " and strChargable='y' and strApplicable='y'";
	    ResultSet rsModifierRate = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsModifierRate.next())
	    {
		modifierAmount = rsModifierRate.getDouble(1);
	    }
	    rsModifierRate.close();

	    boolean flag = false;
	    if (obj_List_ItemDtl.size() > 0)
	    {
		for (clsDirectBillerItemDtl listItemRow : obj_List_ItemDtl)
		{
		    String temp_itemCode = listItemRow.getItemCode();
		    if (temp_itemCode.equalsIgnoreCase(itemCodeForModifier) && listItemRow.isIsModifier() == true && modifierCode.equalsIgnoreCase(listItemRow.getModifierCode()))
		    {
			double temp_qty = listItemRow.getQty();
			double final_qty = temp_qty + modifierQty;
			listItemRow.setRate(modifierAmount);
			listItemRow.setAmt(modifierAmount * final_qty);
			listItemRow.setQty(temp_qty + modifierQty);
			flag = true;
		    }
		    if (flag)
		    {
			break;
		    }
		}
	    }
	    if (!flag)
	    {
		clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(ModifierName, itemCodeForModifier, modifierQty, modifierAmount * modifierQty, true, modifierCode, "N", "", modifierAmount, "", getSeqNo(), 0);
		obj_List_ItemDtl.add(funGetIndexOfItem(itemCodeForModifier), ob);
	    }
	    funRefreshItemTable();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public void funAddSubItems(String subItemCode, String subItemName, double thisItemLimeis, String tdhComboItemCode)
    {
	try
	{
	    if (null != obj_List_ItemDtl && obj_List_ItemDtl.size() > 0)
	    {
		boolean flg_ItemFoundUpdate = false;
		boolean testflg = false;
		for (clsDirectBillerItemDtl obj : obj_List_ItemDtl)
		{
		    if (obj.getTdhComboItemYN().equalsIgnoreCase("Y") && obj.getItemCode().equalsIgnoreCase(subItemCode))
		    {
			testflg = true;
			if (obj.getQty() < thisItemLimeis)
			{
			    flg_ItemFoundUpdate = true;
			    double temp_qty = obj.getQty();
			    obj.setQty(temp_qty + 1);
			}
			else
			{
			    funAddSubItemToPriceList(subItemCode, subItemName);
			    funGetPrice(subItemName);
			    break;
			}
		    }
		    if (flg_ItemFoundUpdate)
		    {
			funRefreshItemTable();
			break;
		    }
		}

		if (!testflg)
		{
		    clsDirectBillerItemDtl ob1 = new clsDirectBillerItemDtl("=>".concat(subItemName), subItemCode, 1.00, 0.00, false, "", "Y", tdhComboItemCode, 0.00, "", getSeqNo(), 0);
		    serNo++;
		    obj_List_ItemDtl.add(ob1);
		    funRefreshItemTable();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private double funGetMaxQty(String tempComboItemCode, String checkSQL, String SeriealNo)
    {
	double qty = 0.00;
	String temp = checkSQL;
	try
	{

	    if ("MaxSubItemLimitWithComboItem".equalsIgnoreCase(temp))
	    {
		sql = "select intMaxQuantity from tbltdhhd where strItemCode='" + tempComboItemCode + "' and strComboItemYN='Y';";
	    }
	    if ("CurrentSubItemQty".equalsIgnoreCase(temp))
	    {
		sql = "select sum(a.dblItemQuantity) from  tblitemtemp a,tbltdhcomboitemdtl b where a.strSerialno='" + SeriealNo + "' and a.strItemCode=b.strSubItemCode and b.strItemCode='" + tempComboItemCode + "' and a.tdhComboItemYN='Y';";
	    }
	    if ("CurrentComboItemQty".equalsIgnoreCase(temp))
	    {
		sql = "select dblItemQuantity from  tblitemtemp where strItemCode='" + tempComboItemCode + "' and tdhComboItemYN='N';";
	    }
	    if ("thisSubItemCurrentQty".equalsIgnoreCase(temp))
	    {
		sql = "select dblItemQuantity from  tblitemtemp where strItemCode='" + tempComboItemCode + "' and strSerialno='" + SeriealNo + "' and tdhComboItemYN='Y'; ";
	    }

	    ResultSet rsMaxItemQty = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsMaxItemQty.next())
	    {
		qty = rsMaxItemQty.getDouble(1);
	    }
	    rsMaxItemQty.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    return qty;
	}
    }

    public void funInsertFreeFlowModifier(String ModifierName, BigDecimal rate)
    {
	try
	{
	    int row = tblItemTable.getSelectedRow();
	    String tempitemcode = (String) tblItemTable.getValueAt(row, 3);
	    clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl("-->".concat(ModifierName), tempitemcode, 1.00, rate.doubleValue(), true, "M99", "N", "", rate.doubleValue(), "", getSeqNo().concat(".00"), 0);
	    obj_List_ItemDtl.add(row + 1, ob);
	    funRefreshItemTable();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

//function for item button blank
    public void funResetItemNames()
    {
	isModifierSelect = false;
	hm_ModifierDtl = null;
	hm_ModifierGroup = null;
	flgPopular = false;
	JButton[] btnItemArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};//create the JButton array group
	for (int i = 0; i < btnItemArray.length; i++)
	{
	    btnItemArray[i].setText("");//set the item button blank
	    btnItemArray[i].setIcon(null);
	}
    }

    private void funSelectDeliveryPersonCode()
    {
	objUtility.funCallForSearchForm("DeliveryBoyForHD");
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    clsGlobalVarClass.gSearchItemClicked = false;
	    if (clsGlobalVarClass.gDeliveryBoyCode != null && clsGlobalVarClass.gDeliveryBoyCode.trim().length() > 0)
	    {
		if (!clsGlobalVarClass.gDeliveryBoyCode.contains(data[0].toString()))
		{
		    clsGlobalVarClass.gDeliveryBoyCode = clsGlobalVarClass.gDeliveryBoyCode + "," + data[0].toString();
		    clsGlobalVarClass.gDeliveryBoyName = clsGlobalVarClass.gDeliveryBoyName + "," + data[1].toString();
		    lblDelBoyName.setText("Delivery Boy: " + clsGlobalVarClass.gDeliveryBoyName);
		}
	    }
	    else
	    {
		clsGlobalVarClass.gDeliveryBoyCode = data[0].toString();
		clsGlobalVarClass.gDeliveryBoyName = data[1].toString();
		lblDelBoyName.setText("Delivery Boy: " + data[1].toString());
	    }
	}
    }

    //function for popular item
    private void funPopularItem() throws Exception
    {
	funResetItemNames();
	int cou = 0;
	int i = 0;
	JButton[] btnPopularArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	btnNextItem.setEnabled(false);
	btnPrevItem.setEnabled(false);
	nextItemClick = 0;
	list_ItemNames_Buttoms.clear();
	obj_List_ItemPrice = new ArrayList<>();

	String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	String sqlPopItems = "";
	/**
	 * auto select popular items baed on backed dated sales
	 */
	String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	if (dineInForTax.equalsIgnoreCase("Y"))
	{
	    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	}

	if (homeDeliveryForTax.equalsIgnoreCase("Y"))
	{
	    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	}

	if (takeAwayForTax.equalsIgnoreCase("Y"))
	{
	    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	}

	if (clsGlobalVarClass.gAutoShowPopItems)
	{
	    int intShowPopItemsOfDays = 1;
	    String sqlPopItemsDays = "select  a.strAutoShowPopItems,a.intShowPopItemsOfDays "
		    + "from tblsetup a "
		    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All')";
	    ResultSet rsPosItemsDays = clsGlobalVarClass.dbMysql.executeResultSet(sqlPopItemsDays);
	    if (rsPosItemsDays.next())
	    {
		intShowPopItemsOfDays = rsPosItemsDays.getInt(2);
	    }
	    rsPosItemsDays.close();

	    sqlPopItems = "select count(*) from "
		    + "(sELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday, "
		    + "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,  "
		    + "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo, "
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,c.strStockInEnable "
		    + ",count(b.strItemCode),b.dteBillDate  "
		    + "from tblmenuitempricingdtl a,tblqbilldtl b,tblitemmaster c "
		    + "where a.strItemCode=b.strItemCode "
		    + "and a.strItemCode=c.strItemCode "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + "and (a.strAreaCode='" + areaWisePricingAreaCode + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
		    + "and date(b.dteBillDate) >=DATE_ADD('" + posDateForPrice + "',interval -" + intShowPopItemsOfDays + " day) "
		    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + "group by b.strItemCode "
		    + "order by count(b.strItemCode) desc "
		    + "limit 48 ) d ";
	    ResultSet rsPosItemsCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlPopItems);
	    if (rsPosItemsCount.next())
	    {
		cou = rsPosItemsCount.getInt(1);
	    }
	    rsPosItemsCount.close();

	    itemNames = new String[cou];

	    sqlPopItems = "sELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday, "
		    + "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,  "
		    + "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo, "
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,c.strStockInEnable "
		    + ",count(b.strItemCode),b.dteBillDate  "
		    + "from tblmenuitempricingdtl a,tblqbilldtl b,tblitemmaster c "
		    + "where a.strItemCode=b.strItemCode "
		    + "and a.strItemCode=c.strItemCode "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + "and (a.strAreaCode='" + areaWisePricingAreaCode + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
		    + "and date(b.dteBillDate) >=DATE_ADD('" + posDateForPrice + "',interval -" + intShowPopItemsOfDays + " day) "
		    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + "group by b.strItemCode "
		    + "order by count(b.strItemCode) desc "
		    + "limit 48 ";
	}
	else
	{
	    String popularItems = "select count(strItemName) from tblmenuitempricingdtl a "
		    + " where a.strPopular='Y' and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and (a.strAreaCode='" + areaWisePricingAreaCode + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') ";
	    ResultSet rsItemData = clsGlobalVarClass.dbMysql.executeResultSet(popularItems);
	    while (rsItemData.next())
	    {
		cou = rsItemData.getInt(1);
	    }
	    rsItemData.close();

	    itemNames = new String[cou];
	    sqlPopItems = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
		    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
		    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
		    + ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " where a.strPopular='Y' and  a.strItemCode= b.strItemCode "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and (a.strAreaCode='" + areaWisePricingAreaCode + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
		    + " and b.strOperationalYN='Y' ";

	}
	ResultSet rsItemPrice = clsGlobalVarClass.dbMysql.executeResultSet(sqlPopItems);
	while (rsItemPrice.next())
	{
	    list_ItemNames_Buttoms.add(rsItemPrice.getString(2));
	    clsItemPriceDtl ob = new clsItemPriceDtl(rsItemPrice.getString(1), rsItemPrice.getString(2), rsItemPrice.getDouble(4), rsItemPrice.getDouble(5), rsItemPrice.getDouble(6), rsItemPrice.getDouble(7), rsItemPrice.getDouble(8), rsItemPrice.getDouble(9), rsItemPrice.getDouble(10), rsItemPrice.getString(11), rsItemPrice.getString(12), rsItemPrice.getString(13), rsItemPrice.getString(14), rsItemPrice.getString(15), rsItemPrice.getString(3), rsItemPrice.getString(16), rsItemPrice.getString(17), rsItemPrice.getString(18), rsItemPrice.getString(19), rsItemPrice.getString(20), rsItemPrice.getDouble(21), rsItemPrice.getString(22));
	    obj_List_ItemPrice.add(ob);
	    if (rsItemPrice.getString(2).contains(" "))
	    {
		StringBuilder sb1 = new StringBuilder(rsItemPrice.getString(2));
		int len = sb1.length();
		int seq = sb1.lastIndexOf(" ");
		String split = sb1.substring(0, seq);
		String last = sb1.substring(seq + 1, len);
		itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
	    }
	    else
	    {
		itemNames[i] = rsItemPrice.getString(2);
	    }
	    i++;
	}
	rsItemPrice.close();
	for (int j = i; j < 16; j++)
	{
	    btnPopularArray[j].setEnabled(false);
	}
	funLoadPopularItems(btnPopularArray);

	if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    flgPopular = true;
	    funFillTopButtonList(menuHeadCode);
	}
	else
	{
	    flgPopular = false;
	}
    }

    private void funLoadPopularItems(JButton[] btnPopularArray)
    {
	int start = nextItemClick * 16;
	int end = start + 16;
	if (end > itemNames.length)
	{
	    end = itemNames.length;
	}

	for (int j = 0; j < 16; j++)
	{
	    btnPopularArray[j].setText("");
	    btnPopularArray[j].setEnabled(false);
	}
	for (int i = start, j = 0; i < end; i++)
	{
	    String name = fun_Get_FormattedName(itemNames[i]);
	    btnPopularArray[j].setText(name);
	    btnPopularArray[j].setEnabled(true);
	    j++;
	}

	if (nextItemClick == 0)
	{
	    btnPrevItem.setEnabled(false);
	}
	else
	{
	    btnPrevItem.setEnabled(true);
	}
	int rem = (itemNames.length) - (16 * (nextItemClick + 1));
	if (rem > 0)
	{
	    btnNextItem.setEnabled(true);
	}
	else
	{
	    btnNextItem.setEnabled(false);
	}
    }

    private void funFillTopButtonList(String tempMenuCode)
    {
	try
	{
	    int i = 0;
	    if (listTopButtonName == null)
	    {
		listTopButtonName = new ArrayList<>();
	    }
	    if (listTopButtonCode == null)
	    {
		listTopButtonCode = new ArrayList<>();
	    }
	    listTopButtonName.clear();
	    listTopButtonCode.clear();
	    funResetSortingButtons();
	    JButton[] btnSubGroupArray =
	    {
		btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
	    };
	    btnPrevItemSorting.setEnabled(false);
	    btnPrevItemSorting.setEnabled(false);
	    String sqlCountItem = "";
	    String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCountItem = "select count(distinct(a.strSubGroupCode)) "
			+ " from tblitemmaster a,tblmenuitempricingdtl b "
			+ " where a.strItemCode=b.strItemCode "
			+ " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			+ " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All') "
			+ " and a.strOperationalYN='Y' ";
		if (flgPopular)
		{
		    sqlCountItem += " and b.strPopular='Y'";
		}
		else
		{
		    sqlCountItem += " and b.strMenuCode='" + tempMenuCode + "'";
		}
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCountItem = "select count(distinct(a.strSubMenuHeadCode)) "
			+ "from tblsubmenuhead a,tblmenuitempricingdtl b ";
		if (flgPopular)
		{
		    sqlCountItem += " where a.strSubMenuHeadCode=b.strSubMenuHeadCode and b.strPopular='Y' "
			    + " and a.strSubMenuOperational='Y' "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All') ";
		}
		else
		{
		    sqlCountItem += " where a.strSubMenuHeadCode=b.strSubMenuHeadCode and b.strMenuCode='" + tempMenuCode + "' "
			    + " and a.strSubMenuOperational='Y' "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All') ";
		}
	    }

	    ResultSet rsItemCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCountItem);
	    rsItemCount.next();
	    int ItemCount = rsItemCount.getInt(1);
	    rsItemCount.close();
	    if (ItemCount > 4)
	    {
		btnNextItemSorting.setEnabled(true);
	    }
	    else
	    {
		btnNextItemSorting.setEnabled(false);
	    }
	    String sqlItems = "";
	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlItems = "select c.strSubGroupName,a.strSubGroupCode,a.strItemCode ,b.strMenuCode "
			+ " from tblitemmaster a,tblmenuitempricingdtl b,tblsubgrouphd c "
			+ " where a.strSubGroupCode !='null' "
			+ " and a.strItemCode=b.strItemCode and a.strSubGroupCode=c.strSubGroupCode "
			+ " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "'  "
			+ " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All') "
			+ " and a.strOperationalYN='Y' ";
		if (flgPopular)
		{
		    sqlItems += " and b.strPopular='Y' group by a.strSubGroupCode  ORDER by c.strSubGroupName";
		}
		else
		{
		    sqlItems += " and b.strMenuCode='" + tempMenuCode + "' group by a.strSubGroupCode "
			    + " ORDER by c.strSubGroupName";
		}
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlItems = "select a.strSubMenuHeadName,a.strSubMenuHeadCode,b.strItemCode "
			+ "from tblsubmenuhead a,tblitemmaster b,tblmenuitempricingdtl c where "
			+ "b.strItemCode=c.strItemCode and a.strSubMenuHeadCode !='null' \n "
			+ "and a.strSubMenuHeadCode=c.strSubMenuHeadCode and a.strSubMenuOperational='Y' "
			+ " and date(c.dteFromDate)<='" + posDateForPrice + "' and date(c.dteToDate)>='" + posDateForPrice + "' "
			+ " and (c.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or c.strPosCode='All') "
			+ " and b.strOperationalYN='Y' ";
		if (flgPopular)
		{
		    sqlItems += " and c.strPopular='Y' group by a.strSubMenuHeadCode";
		}
		else
		{
		    sqlItems += " and a.strMenuCode='" + tempMenuCode + "' group by a.strSubMenuHeadCode";
		}
	    }
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlItems);
	    while (rs.next())
	    {
		listTopButtonName.add(rs.getString(1));
		listTopButtonCode.add(rs.getString(2));
	    }
	    rs.close();
	    if (!listTopButtonName.isEmpty())
	    {
		funFillTopButtons();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funFillTopButtons()
    {
	try
	{
	    JButton btnArray[] =
	    {
		btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
	    };
	    itemNumber = 0;
	    totalItems = listTopButtonName.size();
	    if (totalItems > 4)
	    {
		btnNextItemSorting.setEnabled(true);
	    }
	    if (totalItems >= 4)
	    {
		for (int i = itemNumber; itemNumber < 4; itemNumber++)
		{
		    btnArray[itemNumber].setText(listTopButtonName.get(itemNumber));
		    btnArray[itemNumber].setEnabled(true);
		}
	    }
	    else
	    {
		for (int i = itemNumber; itemNumber < totalItems; itemNumber++)
		{
		    btnArray[itemNumber].setText(listTopButtonName.get(itemNumber));
		    btnArray[itemNumber].setEnabled(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funDisableSelectedQtyBtn(String strQty)
    {
	JButton arrBtnQty[] =
	{
	    btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnMultiQty
	};
	for (int cnt = 0; cnt < arrBtnQty.length; cnt++)
	{
	    if (strQty.equals(arrBtnQty[cnt].getText()))
	    {
		arrBtnQty[cnt].setEnabled(false);
		break;
	    }
	}
    }

    private double funGetFinalPrice(clsItemPriceDtl ob) throws Exception
    {
	double Price = 0.00;
	if (clsGlobalVarClass.gPriceFrom.equals("Item Master"))
	{
	    String sql_Price = "select dblSalePrice from tblitemmaster where strItemCode='" + ob.getStrItemCode() + "'";
	    ResultSet rsItemSalePrice = clsGlobalVarClass.dbMysql.executeResultSet(sql_Price);
	    if (rsItemSalePrice.next())
	    {
		Price = rsItemSalePrice.getDouble(1);
	    }
	    rsItemSalePrice.close();
	}
	else
	{
	    String fromTime = ob.getTmeTimeFrom();
	    String toTime = ob.getTmeTimeTo();
	    String fromAMPM = ob.getStrAMPMFrom();
	    String toAMPM = ob.getStrAMPMTo();

	    if (null != hmHappyHourItems.get(ob.getStrItemName()))
	    {
		boolean flgHappyHrs = false;
		List<clsItemPriceDtl> listHappyHourItem = hmHappyHourItems.get(ob.getStrItemName());

		for (clsItemPriceDtl obHappyHourItem : listHappyHourItem)
		{
		    if (flgHappyHrs)
		    {
			break;
		    }

		    fromTime = obHappyHourItem.getTmeTimeFrom();
		    toTime = obHappyHourItem.getTmeTimeTo();

		    String fromDateTime = objUtility.funGetOnlyPOSDateForTransaction() + " " + fromTime;
		    String toDateTime = objUtility.funGetOnlyPOSDateForTransaction() + " " + toTime;
		    String posDateTime = objUtility.funGetPOSDateForTransaction();

		    long diff1 = objUtility.funCompareTime(fromDateTime, posDateTime);
		    long diff2 = objUtility.funCompareTime(posDateTime, toDateTime);
		    if (diff1 > 0 && diff2 > 0)
		    {
			switch (objUtility.funGetDayForPricing())
			{
			    case "strPriceMonday":
				Price = obHappyHourItem.getStrPriceMonday();
				break;

			    case "strPriceTuesday":
				Price = obHappyHourItem.getStrPriceTuesday();
				break;

			    case "strPriceWednesday":
				Price = obHappyHourItem.getStrPriceWednesday();
				break;

			    case "strPriceThursday":
				Price = obHappyHourItem.getStrPriceThursday();
				break;

			    case "strPriceFriday":
				Price = obHappyHourItem.getStrPriceFriday();
				break;

			    case "strPriceSaturday":
				Price = obHappyHourItem.getStrPriceSaturday();
				break;

			    case "strPriceSunday":
				Price = obHappyHourItem.getStrPriceSunday();
				break;
			}
			flgHappyHrs = true;
		    }
		    else
		    {
			flgHappyHrs = false;
		    }
		}

		if (!flgHappyHrs)
		{
		    switch (objUtility.funGetDayForPricing())
		    {
			case "strPriceMonday":
			    Price = ob.getStrPriceMonday();
			    break;

			case "strPriceTuesday":
			    Price = ob.getStrPriceTuesday();
			    break;

			case "strPriceWednesday":
			    Price = ob.getStrPriceWednesday();
			    break;

			case "strPriceThursday":
			    Price = ob.getStrPriceThursday();
			    break;

			case "strPriceFriday":
			    Price = ob.getStrPriceFriday();
			    break;

			case "strPriceSaturday":
			    Price = ob.getStrPriceSaturday();
			    break;

			case "strPriceSunday":
			    Price = ob.getStrPriceSunday();
			    break;
		    }
		}
	    }
	    else
	    {
		switch (objUtility.funGetDayForPricing())
		{
		    case "strPriceMonday":
			Price = ob.getStrPriceMonday();
			break;

		    case "strPriceTuesday":
			Price = ob.getStrPriceTuesday();
			break;

		    case "strPriceWednesday":
			Price = ob.getStrPriceWednesday();
			break;

		    case "strPriceThursday":
			Price = ob.getStrPriceThursday();
			break;

		    case "strPriceFriday":
			Price = ob.getStrPriceFriday();
			break;

		    case "strPriceSaturday":
			Price = ob.getStrPriceSaturday();
			break;

		    case "strPriceSunday":
			Price = ob.getStrPriceSunday();
			break;
		}
	    }
	}
	return Price;
    }

    //To get the price of the selected item day wise
    private int funGetPrice(String itemName)
    {
	String itemCode = "";
	try
	{
	    boolean flag_isComboItem = false;
	    int index = list_ItemNames_Buttoms.indexOf(itemName);
	    clsItemPriceDtl priceObject = obj_List_ItemPrice.get(index);
	    objGlobalPriceObject = priceObject;
	    flag_isTDHModifier_Item = false;
	    itemCode = priceObject.getStrItemCode();
	    double purRate = priceObject.getDblPurchaseRate();
	    boolean isVailable = objUtility.isItemAvailableForTotay(itemCode, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode);

	    if (!isVailable)
	    {
		new frmOkPopUp(this, "<html>\"" + itemName.toUpperCase() + "\" Is Not Available For Today.</html>", "Warning", 1).setVisible(true);
		return 0;
	    }

	    if (clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y"))
	    {
		itemWeight = funGetItemWeight(itemCode);
		System.out.println("ItemWeight=" + itemWeight);
	    }
	    if (clsGlobalVarClass.gItemQtyNumpad && clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
	    {
		frmNumericKeyboard numItemWeightKeyPad = new frmNumericKeyboard(this, true, "", "Double", "No Of Boxes.");
		numItemWeightKeyPad.setVisible(true);
		noOfBoxes = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    if (!clsGlobalVarClass.gItemQtyNumpad && clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
	    {
		frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Qty");
		num.setVisible(true);
		//selectedQty = num.getResult();
		if (null != clsGlobalVarClass.gNumerickeyboardValue)
		{
		    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		    clsGlobalVarClass.gNumerickeyboardValue = null;
		}

		frmNumericKeyboard numItemWeightKeyPad = new frmNumericKeyboard(this, true, "", "Double", "No Of Boxes.");
		numItemWeightKeyPad.setVisible(true);
		noOfBoxes = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
	    }

	    double dblPrice = 0;
	    double qty = 1;
	    dblPrice = funGetFinalPrice(priceObject);
	    double amt = dblPrice * qty;
	    double itemPrice = dblPrice;
	    double weight = selectedQty;

	    if (clsGlobalVarClass.ListTDHOnModifierItem.contains(itemCode))
	    {
		flag_isTDHModifier_Item = true;
		MaxQTYOfModifierWithTDHItem = clsGlobalVarClass.ListTDHOnModifierItemMaxQTY.get((clsGlobalVarClass.ListTDHOnModifierItem.indexOf(itemCode)));
		clsDirectBillerItemDtl ob1 = new clsDirectBillerItemDtl(itemName, itemCode, qty, amt, false, "", "N", "", dblPrice, "", getSeqNo(), 0);
		serNo++;
		frmTDHDialog ob = new frmTDHDialog(this, true, itemCode, ob1);
		ob.setVisible(true);
	    }
	    else
	    {
		if (itemPrice == 0)
		{
		    String formName = "open Items";
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
				//QTY POP UP
				frmNumberKeyPad num;
				if (clsGlobalVarClass.gItemQtyNumpad)
				{
				    num = new frmNumberKeyPad(this, true, "qty");
				    num.setVisible(true);
				    //selectedQty = num.getResult();
				    if (null != clsGlobalVarClass.gNumerickeyboardValue)
				    {
					selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
					clsGlobalVarClass.gNumerickeyboardValue = null;
				    }
				}

				if (clsGlobalVarClass.gShowPurRateInDirectBiller)
				{
				    frmNumberKeyPadWithPurRate obj = new frmNumberKeyPadWithPurRate(this, true, "Rate" + itemPrice, priceObject.getDblPurchaseRate());
				    obj.setVisible(true);
				}
				else
				{
				    frmNumberKeyPad obj = new frmNumberKeyPad(this, true, "Rate" + itemPrice);
				    obj.setVisible(true);
				}
				if (clsGlobalVarClass.gRateEntered)
				{
				    //itemPrice = obj.getResult();
				    if (null != clsGlobalVarClass.gNumerickeyboardValue)
				    {
					itemPrice = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
					clsGlobalVarClass.gNumerickeyboardValue = null;
				    }
				    if (selectedQty > 0)
				    {
					int row = tblItemTable.getSelectedRow();
					flagOpenItem = true;
					if (priceObject.getStrStockInEnable().equals("Y") && clsGlobalVarClass.gShowItemStkColumnInDB)
					{
					    if (Double.parseDouble(tblItemTable.getValueAt(row, 3).toString()) < selectedQty)
					    {
						new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
						txtPLUItemSearch.requestFocus();
						return 0;
					    }
					    else
					    {
						funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
						funRefreshItemTable();
						selectedQty = 1;
					    }
					}
					else
					{
					    funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
					    funRefreshItemTable();
					    selectedQty = 1;
					}
					flagOpenItem = false;
				    }
				    else
				    {
					new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
				    }
				    clsGlobalVarClass.gRateEntered = false;
				}
			    }
			    else
			    {
				new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);

				return 0;
			    }
			}
			else
			{
			    new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
			    return 0;
			}
		    }
		    else
		    {

			//QTY POP UP
			frmNumberKeyPad num;
			if (clsGlobalVarClass.gItemQtyNumpad)
			{
			    num = new frmNumberKeyPad(this, true, "qty");
			    num.setVisible(true);
			    //selectedQty = num.getResult();
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			}

			if (clsGlobalVarClass.gShowPurRateInDirectBiller)
			{
			    frmNumberKeyPadWithPurRate obj = new frmNumberKeyPadWithPurRate(this, true, "Rate" + itemPrice, priceObject.getDblPurchaseRate());
			    obj.setVisible(true);
			}
			else
			{
			    frmNumberKeyPad obj = new frmNumberKeyPad(this, true, "Rate" + itemPrice);
			    obj.setVisible(true);
			}
			if (clsGlobalVarClass.gRateEntered)
			{
			    //itemPrice = obj.getResult();
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				itemPrice = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			    if (selectedQty > 0)
			    {
				int row = tblItemTable.getSelectedRow();
				flagOpenItem = true;
				if (priceObject.getStrStockInEnable().equals("Y") && clsGlobalVarClass.gShowItemStkColumnInDB)
				{
				    if (Double.parseDouble(tblItemTable.getValueAt(row, 3).toString()) < selectedQty)
				    {
					new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
					txtPLUItemSearch.requestFocus();
					return 0;
				    }
				    else
				    {
					funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
					funRefreshItemTable();
					selectedQty = 1;
				    }
				}
				else
				{
				    funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
				    funRefreshItemTable();
				    selectedQty = 1;
				}
				flagOpenItem = false;
			    }
			    else
			    {
				new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
			    }
			    clsGlobalVarClass.gRateEntered = false;
			}
		    }
		}
		else
		{

		    //QTY POP UP
		    frmNumberKeyPad num;
		    if (clsGlobalVarClass.gItemQtyNumpad)
		    {
			num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			//selectedQty = num.getResult();
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			    clsGlobalVarClass.gNumerickeyboardValue = null;
			}
		    }

		    if (selectedQty != 0)
		    {
			if (priceObject.getStrStockInEnable().equals("Y") && clsGlobalVarClass.gShowItemStkColumnInDB)
			{
			    double stkQty = clsGlobalVarClass.funGetStock(itemCode, "Item");
			    if (stkQty < selectedQty)
			    {
				new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
				return 0;
			    }
			    if (tblItemTable.getRowCount() > 0)
			    {
				int count = 0;
				boolean flgSameRowAvaiale = false;
				for (int r = 0; r < tblItemTable.getRowCount(); r++)
				{
				    if (tblItemTable.getValueAt(r, 4).toString().equals(itemCode))
				    {
					flgSameRowAvaiale = true;
					break;
				    }
				    count++;
				}
				if (flgSameRowAvaiale)
				{
				    if (Double.parseDouble(tblItemTable.getValueAt(count, 3).toString()) < (Double.parseDouble(tblItemTable.getValueAt(count, 1).toString()) + 1.00))
				    {
					new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
					return 0;
				    }
				}
				funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
				funRefreshItemTable();
				selectedQty = 1;
			    }
			    else
			    {
				funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
				funRefreshItemTable();
				selectedQty = 1;
			    }
			}
			else
			{
			    funInsertData(selectedQty, itemPrice, itemCode, itemName, purRate);
			    funRefreshItemTable();
			    selectedQty = 1;
			}
		    }
		    else
		    {
			new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
		    }
		}
		if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(itemCode))
		{
		    flag_isComboItem = true;
		    frmTDHDialog frmComboItemTDH = new frmTDHDialog(this, true, itemCode, itemName, clsTDHOnItemDtl.hm_ComboItemDtl);
		    frmComboItemTDH.setVisible(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return 1;
    }

    private void funChangeQtyOfItem(String qty)
    {
	try
	{
	    int index = 3;
	    if (clsGlobalVarClass.gShowItemStkColumnInDB)
	    {
		index = 4;
	    }
	    String tempItemCode = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), index).toString();
	    selectedQty = Double.parseDouble(qty);
	    if (!clsGlobalVarClass.gNegBilling)
	    {
		if (!clsGlobalVarClass.funCheckNegativeStock(tempItemCode, selectedQty))
		{
		    selectedQty = 1;
		}
	    }
	    /////////////////for stk shown///////////

	    if (clsGlobalVarClass.gShowItemStkColumnInDB)
	    {
		clsItemPriceDtl priceObject = null;

		for (int cnt = 0; cnt < obj_List_ItemPrice.size(); cnt++)
		{
		    priceObject = obj_List_ItemPrice.get(cnt);
		    if (tempItemCode.equals(priceObject.getStrItemCode()))
		    {
			break;
		    }
		}

		if (null != priceObject && priceObject.getStrStockInEnable().equals("Y"))
		{
		    double stkQty = clsGlobalVarClass.funGetStock(tempItemCode, "Item");
		    if (stkQty < selectedQty)
		    {
			txtPLUItemSearch.requestFocus();
			new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
			return;
		    }
		}
	    }
	    ////////////////////////////////////////////////

	    int selectrow = tblItemTable.getSelectedRow();
	    //double price = Double.parseDouble(tblItemTable.getModel().getValueAt(selectrow, 2).toString());
	    clsDirectBillerItemDtl ob = obj_List_ItemDtl.get(selectrow);
	    double price = ob.getRate();
	    ob.setAmt(selectedQty * price);
	    ob.setQty(selectedQty);
	    selectedQty = 1;
	    funRefreshItemTable();
	    flgChangeQty = false;
	    txtPLUItemSearch.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    clsGlobalVarClass.gNumerickeyboardValue = null;
	}
    }

    private void funItemTablePressed(String clickType)
    {
	try
	{
	    if (tblItemTable.getRowCount() > 0)
	    {
		clsGlobalVarClass.gNumerickeyboardValue = null;

		int columnNo = tblItemTable.getSelectedColumn();
		int rowNo = tblItemTable.getSelectedRow();

		if (clickType.equals("Mouse"))
		{
		    if (columnNo == 1)
		    {
			frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
			    {
				funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue);
			    }
			}
			/*
                         * if (num.getResult() > 0) {
                         * funChangeQtyOfItem(String.valueOf(num.getResult()));
                         * }
			 */
		    }
		}
		else
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
		    num.setVisible(true);
		    /*
                     * if (num.getResult() > 0) {
                     * funChangeQtyOfItem(String.valueOf(num.getResult())); }
		     */

		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
			{
			    funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue);
			}
		    }
		}
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(rowNo, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(rowNo, 3).toString().trim();
		}

		funModifierButtonPressed();
		if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(Itemcode))
		{
		    TDH_Combo_Itemcode = tblItemTable.getValueAt(rowNo, 3).toString().trim();
		    MaxSubItemLimitWithComboItem = funGetMaxQty(tblItemTable.getValueAt(rowNo, 3).toString().trim(), "MaxSubItemLimitWithComboItem", "");
		    funShowComboSubItemTable(TDH_Combo_Itemcode);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funItemBtnClick(String itemName)
    {
	try
	{
	    funGetPrice(itemName);
	    txtExternalCode.requestFocus();
	    panelSubGroup.setVisible(true);
	    panelNavigate.setVisible(true);
	    panelItemList.setVisible(true);
	    if (null != objPanelModifier)
	    {
		objPanelModifier.setVisible(false);
	    }
	}
	catch (Exception ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    ex.printStackTrace();
	}
    }

    private void funHideShowSubGroupPanel(boolean flg)
    {
	panelSubGroup.setVisible(flg);
    }

    /*
     * private void funShowModifierPannel() { try { if (!flagTDHItem) { int r =
     * tblItemTable.getSelectedRow(); Itemcode =
     * tblItemTable.getModel().getValueAt(r, 3).toString(); } sql = "select
     * strApplicable from tblitemmodofier where strItemCode='" + Itemcode + "'"
     * + " or strItemCode='All'"; ResultSet modifierapplicable =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); if
     * (modifierapplicable.next()) { if
     * ("y".equalsIgnoreCase(modifierapplicable.getString(1))) {
     * IItemPanel.setVisible(false); panelNavigate.setVisible(false);
     * panelSubGroup.setVisible(false); panel_PLU.setVisible(false); if
     * (objPanelModifier == null) { objPanelModifier = new panelModifier(this);
     * objPanelModifier.getItemCode(Itemcode); jPanel3.add(objPanelModifier);
     * objPanelModifier.setLocation(panelNavigate.getLocation());
     * objPanelModifier.funFillTable(Itemcode);
     * objPanelModifier.setVisible(true); funHideShowSubGroupPanel(false);//
     * objPanelModifier.setSize(380, 500); objPanelModifier.revalidate();
     *
     * } else { objPanelModifier.getItemCode(Itemcode);
     * funHideShowSubGroupPanel(false);// objPanelModifier = new
     * panelModifier(this); jPanel3.add(objPanelModifier);
     * objPanelModifier.setVisible(true);
     * objPanelModifier.funFillTable(Itemcode);
     * objPanelModifier.setLocation(panelNavigate.getLocation());
     * objPanelModifier.setSize(380, 500); objPanelModifier.revalidate(); } }
     * else { new frmOkPopUp(this, "No Modifier for this item", "Error",
     * 1).setVisible(true); } } else { IItemPanel.setVisible(false);
     * panelNavigate.setVisible(false); panelSubGroup.setVisible(false); if
     * (objPanelModifier == null) { objPanelModifier = new panelModifier(this);
     * jPanel3.add(objPanelModifier);
     * objPanelModifier.setLocation(panelNavigate.getLocation());
     * objPanelModifier.setVisible(true);
     * objPanelModifier.funFillTable(Itemcode); funHideShowSubGroupPanel(false);
     * objPanelModifier.setSize(380, 500); objPanelModifier.revalidate();
     *
     * } else { objPanelModifier.setVisible(true);
     * funHideShowSubGroupPanel(false); } } modifierapplicable.close(); } catch
     * (Exception e) { e.printStackTrace(); } }
     */
    public int funCallWebService()
    {
	try
	{
	    String custCode = "";
	    String custMobileNo = clsGlobalVarClass.gNumerickeyboardValue;
	    String sql_CustInfo = "select strCustomerCode from tblcustomermaster where longMobileNo=" + custMobileNo;
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
	    if (rsCust.next())
	    {
		custCode = rsCust.getString(1);
	    }
	    rsCust.close();

	    objData = new clsCustomerDataModelForSQY();
	    getObjData().setTransactionId("");
	    getObjData().setRedeemed_amt(0);
	    getObjData().setCustMobileNo(Long.parseLong(custMobileNo));
	    getObjData().setOutlet_uuid(clsGlobalVarClass.gOutletUID);
	    getObjData().setCustomerCode(custCode);
	    //getObjData().setOutlet_uuid("f3fb30af-2aad-4b76-bd66-31c67230a1aa");
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    String getWebServiceURL = clsGlobalVarClass.gGetWebserviceURL;

	    getWebServiceURL += "" + custMobileNo + "/outlet/" + clsGlobalVarClass.gOutletUID + "/";
	    HttpGet getRequest = new HttpGet(getWebServiceURL);
	    //HttpGet getRequest = new HttpGet("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/"+custMobileNo+"/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
	    System.out.println("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/" + custMobileNo + "/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
	    HttpResponse response = httpClient.execute(getRequest);
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

	    String output, op = "";
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println(op);

	    JSONParser p = new JSONParser();
	    Object o = p.parse(op);
	    JSONObject obj = (JSONObject) o;
	    clsGlobalVarClass.gFlgPoints = "DiscountPoints";

	    if (null != obj.get("code"))
	    {
		if (Integer.parseInt(obj.get("code").toString()) == 323)
		{
		    JOptionPane.showMessageDialog(this, "Discount Request Expired! Please ask the customer to regenerate discount request!");
		    return 0;
		}
	    }
	    getObjData().setTransactionId(obj.get("transaction_id").toString());
	    getObjData().setStatus(Integer.parseInt(obj.get("status").toString()));
	    getObjData().setConsumer(obj.get("consumer").toString());
	    getObjData().setRedeemed_amt(Double.parseDouble(obj.get("redeemed_amt").toString()));
	    httpClient.getConnectionManager().shutdown();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return 1;
    }

    public int funCallWebServiceForPMAM()
    {
	try
	{
	    String custCode = "";
	    String custMobileNo = clsGlobalVarClass.gNumerickeyboardValue;
	    String sql_CustInfo = "select strCustomerCode from tblcustomermaster where longMobileNo=" + custMobileNo;
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
	    if (rsCust.next())
	    {
		custCode = rsCust.getString(1);
	    }
	    rsCust.close();

	    objData = new clsCustomerDataModelForSQY();
	    getObjData().setTransactionId("");
	    getObjData().setRedeemed_amt(0);
	    getObjData().setCustMobileNo(Long.parseLong(custMobileNo));
	    getObjData().setOutlet_uuid(clsGlobalVarClass.gOutletUID);
	    getObjData().setCustomerCode(custCode);
	    //getObjData().setOutlet_uuid("f3fb30af-2aad-4b76-bd66-31c67230a1aa");
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    String getWebServiceURL = clsGlobalVarClass.gGetWebserviceURL;

	    getWebServiceURL += "" + custMobileNo + "/outlet/" + clsGlobalVarClass.gOutletUID + "/";
	    HttpGet getRequest = new HttpGet(getWebServiceURL);
	    //HttpGet getRequest = new HttpGet("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/"+custMobileNo+"/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
	    System.out.println("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/" + custMobileNo + "/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
	    HttpResponse response = httpClient.execute(getRequest);
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

	    String output, op = "";
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println(op);

	    JSONParser p = new JSONParser();
	    Object o = p.parse(op);
	    JSONObject obj = (JSONObject) o;
	    clsGlobalVarClass.gFlgPoints = "DiscountPoints";

	    if (null != obj.get("code"))
	    {
		if (Integer.parseInt(obj.get("code").toString()) == 323)
		{
		    JOptionPane.showMessageDialog(this, "Discount Request Expired! Please ask the customer to regenerate discount request!");
		    return 0;
		}
	    }
	    getObjData().setTransactionId(obj.get("transaction_id").toString());
	    getObjData().setStatus(Integer.parseInt(obj.get("status").toString()));
	    getObjData().setConsumer(obj.get("consumer").toString());
	    getObjData().setRedeemed_amt(Double.parseDouble(obj.get("redeemed_amt").toString()));
	    httpClient.getConnectionManager().shutdown();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return 1;
    }

    private void funGetItemFromExtCode(String type)
    {
	try
	{

	    if ("click".equalsIgnoreCase(type))
	    {
		if (txtExternalCode.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Item Code").setVisible(true);
		    txtExternalCode.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(this, true, txtExternalCode.getText(), "1", "Enter Item Code").setVisible(true);
		    txtExternalCode.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	    String externalCode = txtExternalCode.getText();

	    if (clsGlobalVarClass.gClientCode.equals("138.001"))//"138.001", "Arabian Heritage Pvt Ltd"
	    {
		String arrExternalCode[] = externalCode.split(",");
		for (int i = 0; i < arrExternalCode.length; i++)
		{
		    funProcessForExternalCode(arrExternalCode[i].trim());
		}
	    }
	    else
	    {
		funProcessForExternalCode(externalCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funSetCustomerForAdvOrderHD()
    {
	try
	{
	    sql = "select strCustomerCode,strCustomerName,longMobileNo,strBuldingCode "
		    + "from tblcustomermaster where strCustomerCode='" + advOrderCustCode + "'";

	    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustomer.next())
	    {
		clsGlobalVarClass.gCustomerCode = rsCustomer.getString(1);
		lblCustInfo.setText(rsCustomer.getString(2));
		clsGlobalVarClass.gCustMBNo = rsCustomer.getString(3);
		clsGlobalVarClass.gBuildingCodeForHD = rsCustomer.getString(4);
		lblCustInfo.setText("<html>" + rsCustomer.getString(2) + "</html>");
	    }
	    rsCustomer.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public void funEnableControls()
    {
	btnSettle.setEnabled(true);
    }

    //to reset the whole field
    public void funResetFields()
    {
	try
	{
	    btnSettle.setEnabled(true);
	    obj_List_ItemDtl.clear();
	    PervBil = false;

	    DefaultTableModel dm = new DefaultTableModel();
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    selectedQty = 1;
	    tblItemTable.setModel(dm);
	    txtTotal.setText("");
	    lblVoucherNo.setText("");
	    lblCustInfo.setText("Customer Info");
	    clsGlobalVarClass.gTakeAway = "No";
	    btnTakeAway.setForeground(Color.white);
	    if (objAgainstAdvOrder != null)
	    {
		objAgainstAdvOrder.setVisible(false);
	    }
	    JButton[] btnSubMenuArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    for (int i = 0; i < btnSubMenuArray.length; i++)
	    {
		btnSubMenuArray[i].setEnabled(false);
		btnSubMenuArray[i].setText("");
		btnSubMenuArray[i].setIcon(null);
	    }

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    //tblItemTable.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(),24));
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	    DefaultTableModel model = (DefaultTableModel) tblItemTable.getModel();
	    model.setRowCount(0);
	    lblDelBoyName.setText("");
	    clsGlobalVarClass.gCustMobileNoForCRM = "";
	    clsGlobalVarClass.gCustomerCode = "";
	    funHideShowSubGroupPanel(true);
	    funResetHomeDeliveryFileds();
	    txtExternalCode.requestFocus();
	    setCmsMemberCode("");
	    setCmsMemberName("");
	    hmDirectBillerParams.clear();
	    debitCardBalance = 0;
	    clsGlobalVarClass.gDebitCardNo = null;

	    if (clsGlobalVarClass.gPriceFrom.equals("Item Master"))
	    {
		funSetVisiblePanels(false);
		txtPLUItemSearch.requestFocus();
	    }
	    else
	    {
		funShowPanel();
		funPopularItem();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    /**
     * Modified :-Ritesh 30 Sept 2014
     *
     * @param MenuCode
     * @param selectedButtonCode
     */
    private void funResetItemButtonTextSelectionWise(String MenuCode, String selectedButtonCode)
    {
	int i = 0;
	//create the button array of Item
	JButton[] btnSubMenuArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	try
	{
	    btnNextItem.setEnabled(false);
	    btnPrevItem.setEnabled(false);
	    nextItemClick = 0;
	    String sqlCount = "";
	    String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];

	    String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    if (dineInForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    }

	    if (homeDeliveryForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	    }

	    if (takeAwayForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	    }

	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCount = "SELECT count(c.strItemName) "
			+ " FROM tblmenuhd a "
			+ " LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
			+ " RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
			+ " WHERE "
			+ "  ";

		if (flgPopular)
		{
		    sqlCount += " b.strPopular = 'Y' and  c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and (strAreaCode='" + areaWisePricingAreaCode + "' or strAreaCode='') "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' ";
		}
		else
		{
		    sqlCount += " a.strMenuCode = '" + MenuCode + "' and  c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and (strAreaCode='" + areaWisePricingAreaCode + "' or strAreaCode='')"
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' "
			    + " AND b.strHourlyPricing='No' ";
		}
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCount = "select count(*) from tblmenuitempricingdtl ";

		if (flgPopular)
		{
		    sqlCount += " where strPopular='Y' and strSubMenuHeadCode='" + selectedButtonCode + "' and (strAreaCode='" + areaWisePricingAreaCode + "' or strAreaCode='') and strHourlyPricing='No' ";
		}
		else
		{
		    sqlCount += " where strMenuCode='" + menuHeadCode + "' and strSubMenuHeadCode='" + selectedButtonCode + "' and (strAreaCode='" + areaWisePricingAreaCode + "' or strAreaCode='') and strHourlyPricing='No' ";
		}
	    }
	    ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCount);
	    rsCount.next();
	    int cn = rsCount.getInt(1);
	    if (cn > 8)
	    {
		btnNextItem.setEnabled(true);
	    }
	    rsCount.close();
	    itemNames = new String[cn];
	    btnforeground = new String[cn];
	    itemImageCode.clear();
	    list_ItemNames_Buttoms.clear();
	    obj_List_ItemPrice = new ArrayList<>();

	    String sql1 = "";
	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sql1 = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday,"
			+ "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  "
			+ "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo,"
			+ "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate"
			+ ",c.strStockInEnable,c.dblPurchaseRate,b.strMenuCode "
			+ "FROM tblmenuhd a "
			+ " LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
			+ "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
			+ "WHERE ";

		if (flgPopular)
		{
		    sql1 += " b.strPopular = 'Y' "
			    + " and c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  "
			    + " and (b.strAreaCode='" + areaWisePricingAreaCode + "' or b.strAreaCode='') ";
		}
		else
		{
		    sql1 += " a.strMenuCode = '" + MenuCode + "' "
			    + " and c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All') "
			    + " and (b.strAreaCode='" + areaWisePricingAreaCode + "' or b.strAreaCode='') ";
		}
		sql1 += " and date(b.dteFromDate)<='" + posDateForPrice + "' "
			+ " and date(b.dteToDate)>='" + posDateForPrice + "' "
			+ " and c.strOperationalYN='Y' "
			+ " AND b.strHourlyPricing='No' ";
		sql1 = sql1 + " ORDER BY c.strItemName ASC";
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sql1 = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday,"
			+ "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  "
			+ "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo,"
			+ "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate"
			+ ",c.strStockInEnable,c.dblPurchaseRate,b.strMenuCode "
			+ "FROM tblmenuitempricingdtl b,tblitemmaster c "
			+ "WHERE ";

		if (flgPopular)
		{
		    sql1 += " b.strMenuCode = '" + menuHeadCode + "' "
			    + " and b.strItemCode=c.strItemCode "
			    + " and b.strSubMenuHeadCode='" + selectedButtonCode + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  "
			    + " and (b.strAreaCode='" + areaWisePricingAreaCode + "' or b.strAreaCode='')";
		}
		else
		{
		    sql1 += " b.strMenuCode = '" + menuHeadCode + "' "
			    + " and b.strItemCode=c.strItemCode "
			    + " and b.strSubMenuHeadCode='" + selectedButtonCode + "' "
			    + " and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  "
			    + " and (b.strAreaCode='" + areaWisePricingAreaCode + "' or b.strAreaCode='') ";
		}
		sql1 += " and date(b.dteFromDate)<='" + posDateForPrice + "' "
			+ " and date(b.dteToDate)>='" + posDateForPrice + "' "
			+ " and c.strOperationalYN='Y' "
			+ " AND b.strHourlyPricing='No' ";
		sql1 = sql1 + " ORDER BY c.strItemName ASC";
	    }
	    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql1);

	    while (rsItemInfo.next())
	    {
		list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
		clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
			rsItemInfo.getDouble(4),
			rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7), rsItemInfo.getDouble(8),
			rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
			rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
			rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19),
			rsItemInfo.getString(20), rsItemInfo.getDouble(21), rsItemInfo.getString(22));
		obj_List_ItemPrice.add(ob);

		String temItemName = rsItemInfo.getString(2);
		String txtColor = rsItemInfo.getString(3);
		String itemCode = rsItemInfo.getString(1);
		if (i < 16)
		{
		    if (temItemName.contains(" "))
		    {
			StringBuilder sb1 = new StringBuilder(temItemName);
			int len = sb1.length();
			int seq = sb1.lastIndexOf(" ");
			String split = sb1.substring(0, seq);
			String last = sb1.substring(seq + 1, len);

			btnSubMenuArray[i].setText("<html>" + split + "<br>" + last + "</html>");
			btnSubMenuArray[i].setIcon(funGetImageIcon(itemCode));
			itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
			btnforeground[i] = txtColor;
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			btnSubMenuArray[i].setText(temItemName);
			itemNames[i] = temItemName;
			btnforeground[i] = txtColor;
			itemImageCode.add(itemCode);
		    }
		    btnSubMenuArray[i].setEnabled(true);

		    switch (txtColor)
		    {
			case "Red":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.red);
			    break;
			case "Black":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.LIGHT_GRAY);
			    break;
			case "Green":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.GREEN);
			    break;
			case "CYAN":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.CYAN);
			    break;
			case "MAGENTA":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.MAGENTA);
			    break;
			case "ORANGE":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.ORANGE);
			    break;
			case "PINK":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.PINK);
			    break;
			case "YELLOW":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.YELLOW);
			    break;
			case "WHITE":
			    btnSubMenuArray[i].setForeground(Color.WHITE);
			    btnSubMenuArray[i].setBackground(Color.BLUE);
			    break;
			default:
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    break;
		    }
		}
		else
		{
		    if (temItemName.contains(" "))
		    {
			StringBuilder sb1 = new StringBuilder(temItemName);
			int len = sb1.length();
			int seq = sb1.lastIndexOf(" ");
			String split = sb1.substring(0, seq);
			String last = sb1.substring(seq + 1, len);
			itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			itemNames[i] = temItemName;
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }
		}
		i++;
	    }
	    rsItemInfo.close();
	    for (int j = i; j < 16; j++)
	    {
		btnSubMenuArray[j].setEnabled(false);
		btnSubMenuArray[j].setText("");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}

	txtExternalCode.requestFocus();
    }

//Conver the Sring in Original form
    public String convertString(String ItemName)
    {
	String retItemName = ItemName;
	if (retItemName.contains("<html>"))
	{
	    StringBuilder sb1 = new StringBuilder(ItemName);
	    sb1 = sb1.delete(0, 6);
	    int seq = sb1.lastIndexOf("<br>");
	    String split = sb1.substring(0, seq);
	    int end = sb1.lastIndexOf("</html>");
	    String last = sb1.substring(seq + 4, end);
	    retItemName = split + " " + last;
	}
	return retItemName;
    }

    private void funResetItemButtonText(String MenuName)
    {

	selectedMenuHeadName = MenuName;
	int i = 0;
	//create the button array of Item
	isModifierSelect = false;
	JButton[] btnSubMenuArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	try
	{
	    btnNextItem.setEnabled(false);
	    btnPrevItem.setEnabled(false);
	    nextItemClick = 0;
	    list_ItemNames_Buttoms.clear();
	    obj_List_ItemPrice = new ArrayList<>();
	    String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];

	    sql = "select strMenuCode from tblmenuhd where strMenuName='" + MenuName + "'";
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsMenuHead.next())
	    {
		menuHeadCode = rsMenuHead.getString(1);
	    }
	    rsMenuHead.close();

	    String itemCount = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		itemCount = "SELECT count(*) "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE a.strMenuCode = '" + menuHeadCode + "' and a.strItemCode=b.strItemCode "
			+ " and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			+ " and b.strOperationalYN='Y' "
			+ " and a.strHourlyPricing='No' ";
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		itemCount = "SELECT count(*) "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE a.strAreaCode='" + areaWisePricingAreaCode + "' and a.strMenuCode = '" + menuHeadCode + "' "
			+ " and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "'  "
			+ " and b.strOperationalYN='Y' "
			+ " and a.strHourlyPricing='No' ";
	    }
	    rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(itemCount);
	    rsMenuHead.next();
	    int cn = rsMenuHead.getInt(1);
	    rsMenuHead.close();
	    if (cn > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    itemNames = new String[cn];
	    btnforeground = new String[cn];
	    //itemImageCode = new String[cn];
	    itemImageCode.clear();

	    String sql_ItemDtl = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			+ " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			+ " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			+ " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
			+ ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE a.strMenuCode = '" + menuHeadCode + "' and a.strItemCode=b.strItemCode "
			+ " and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			+ " and b.strOperationalYN='Y' "
			+ " and a.strHourlyPricing='No' ";
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			+ " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
			+ " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			+ " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
			+ ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE a.strAreaCode='" + areaWisePricingAreaCode + "' and a.strMenuCode = '" + menuHeadCode + "' "
			+ " and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "'  "
			+ " and b.strOperationalYN='Y' "
			+ " and a.strHourlyPricing='No' ";
	    }
	    if (clsGlobalVarClass.gMenuItemSequence.equals("Ascending"))
	    {
		sql_ItemDtl += "ORDER BY b.strItemName ASC;";
	    }
	    //System.out.println(sql_ItemDtl);
	    ResultSet rs_ItemPrice = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
	    while (rs_ItemPrice.next())
	    {
		list_ItemNames_Buttoms.add(rs_ItemPrice.getString(2));
		clsItemPriceDtl ob = new clsItemPriceDtl(rs_ItemPrice.getString(1), rs_ItemPrice.getString(2),
			rs_ItemPrice.getDouble(4),
			rs_ItemPrice.getDouble(5), rs_ItemPrice.getDouble(6), rs_ItemPrice.getDouble(7), rs_ItemPrice.getDouble(8),
			rs_ItemPrice.getDouble(9), rs_ItemPrice.getDouble(10), rs_ItemPrice.getString(11), rs_ItemPrice.getString(12),
			rs_ItemPrice.getString(13), rs_ItemPrice.getString(14), rs_ItemPrice.getString(15),
			rs_ItemPrice.getString(3), rs_ItemPrice.getString(16), rs_ItemPrice.getString(17), rs_ItemPrice.getString(18),
			rs_ItemPrice.getString(19), rs_ItemPrice.getString(20), rs_ItemPrice.getDouble(21), rs_ItemPrice.getString(22));
		obj_List_ItemPrice.add(ob);

		String s = rs_ItemPrice.getString(2);
		String txtColor = rs_ItemPrice.getString(3);
		String itemCode = rs_ItemPrice.getString(1);
		if (i < 16)
		{
		    if (s.contains(" "))
		    {
			StringBuilder sb1 = new StringBuilder(s);
			int len = sb1.length();
			int seq = sb1.lastIndexOf(" ");
			String split = sb1.substring(0, seq);
			String last = sb1.substring(seq + 1, len);

			btnSubMenuArray[i].setText("<html>" + split + "<br>" + last + "</html>");
			btnSubMenuArray[i].setIcon(funGetImageIcon(itemCode));

			itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			btnSubMenuArray[i].setText(s);
			itemNames[i] = s;
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }

		    switch (txtColor)
		    {
			case "Red":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.red);
			    break;
			case "Black":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.LIGHT_GRAY);
			    break;
			case "Green":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.GREEN);
			    break;
			case "CYAN":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.CYAN);
			    break;
			case "MAGENTA":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.MAGENTA);
			    break;
			case "ORANGE":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.ORANGE);
			    break;
			case "PINK":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.PINK);
			    break;
			case "YELLOW":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.YELLOW);
			    break;
			case "WHITE":
			    btnSubMenuArray[i].setForeground(Color.WHITE);
			    btnSubMenuArray[i].setBackground(Color.BLUE);
			    break;
			default:
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    break;
		    }
		    btnSubMenuArray[i].setEnabled(true);
		}
		else
		{
		    if (s.contains(" "))
		    {
			StringBuilder sb1 = new StringBuilder(s);
			int len = sb1.length();
			int seq = sb1.lastIndexOf(" ");
			String split = sb1.substring(0, seq);
			String last = sb1.substring(seq + 1, len);
			itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			itemNames[i] = s;
			btnforeground[i] = txtColor;
			//itemImageCode[i] = itemCode;
			itemImageCode.add(itemCode);
		    }
		}
		i++;
	    }
	    rs_ItemPrice.close();

	    for (int j = i; j < 16; j++)
	    {
		btnSubMenuArray[j].setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

// to inset the record into tblItemtemp
    private void funInsertData(double qty, double itemPrice, String itemCode, String itemName, double purchaseRate)
    {
	try
	{
	    double dblQty = new Double(String.valueOf(qty));
	    double dblPrice = itemPrice;
	    boolean flag = false;
	    if (obj_List_ItemDtl.size() > 0 && !flagOpenItem)
	    {
		for (clsDirectBillerItemDtl list_cls_ItemRow : obj_List_ItemDtl)
		{
		    String temp_itemCode = list_cls_ItemRow.getItemCode();
		    if (temp_itemCode.equalsIgnoreCase(itemCode) && list_cls_ItemRow.isIsModifier() == false && "N".equalsIgnoreCase(list_cls_ItemRow.getTdhComboItemYN()))
		    {
			if (hasModifierEntered(itemCode))
			{
			    flag = false;
			    break;
			}
			double temp_qty = list_cls_ItemRow.getQty();
			double final_qty = temp_qty + dblQty;
			if (!clsGlobalVarClass.gNegBilling)
			{
			    if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, final_qty))
			    {
				return;
			    }
			}
			double amt = Double.parseDouble(gDecimalFormat.format(dblPrice * final_qty));
			list_cls_ItemRow.setRate(dblPrice);
			list_cls_ItemRow.setAmt(amt);
			list_cls_ItemRow.setQty(temp_qty + dblQty);
			flag = true;
		    }
		    if (flag)
		    {
			break;
		    }
		}
	    }

	    if (!flag)
	    {
		if (!clsGlobalVarClass.gNegBilling)
		{
		    if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, selectedQty))
		    {
			return;
		    }
		}

		if (clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
		{
		    double qtyPerBox = dblQty;
		    dblQty = (dblQty * noOfBoxes) / itemWeight;
		    //mapWeightPerBox.clear();
		    clsGlobalVarClass.gMapWeightPerBox.put(itemCode, noOfBoxes + "!" + qtyPerBox + "!" + itemName + "!" + dblPrice + "!" + dblQty);

		}
		clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(itemName, itemCode, dblQty, dblPrice * dblQty, false, "", "N", "", dblPrice, "", getSeqNo(), purchaseRate);
		serNo++;
		obj_List_ItemDtl.add(ob);

	    }
	    selectedQty = 1;
	}
	catch (Exception ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    ex.printStackTrace();
	}
    }

    private boolean hasModifierEntered(String itemCode)
    {
	boolean hasModifier = false;
	try
	{
	    for (clsDirectBillerItemDtl list_cls_ItemRow : obj_List_ItemDtl)
	    {
		if (list_cls_ItemRow.getItemCode().substring(0, 7).equals(itemCode) && list_cls_ItemRow.getItemName().startsWith("-->"))
		{
		    hasModifier = true;
		    break;
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    return hasModifier;
	}
    }

    /**
     * Ritesh 220sept 2014
     */
    public void funRefreshItemTable()
    {

	try
	{
	    totalAmt = homeDelCharges;
	    //ArrayList arrListMenuItems = new ArrayList();
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	    double subTotalForTax = 0;

	    DefaultTableModel dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.setRowCount(0);

	    if (clsGlobalVarClass.gShowItemStkColumnInDB)
	    {
		dm.setRowCount(0);
		dm.addColumn("Description");
		dm.addColumn("Qty");
		dm.addColumn("Amt");
		dm.addColumn("StkIn");
		dm.addColumn("ItemCode");
		dm.addColumn("SeqNo");

		Collections.sort(obj_List_ItemDtl, clsDirectBillerItemDtl.comparatorDirectBillerItemDtl);
		for (clsDirectBillerItemDtl listItemRow : obj_List_ItemDtl)
		{
		    totalAmt = totalAmt + listItemRow.getAmt();
		    double avaiableStk = clsGlobalVarClass.funGetStock(listItemRow.getItemCode(), "Item");
		    Object[] rows =
		    {
			listItemRow.getItemName(), gDecimalFormat.format(listItemRow.getQty()), gDecimalFormat.format(listItemRow.getAmt()), avaiableStk, listItemRow.getItemCode(), listItemRow.getSeqNo()
		    };
		    subTotalForTax += listItemRow.getAmt();
		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(listItemRow.getItemCode());
		    objItemDtl.setItemName(listItemRow.getItemName());
		    objItemDtl.setAmount(listItemRow.getAmt());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);

		    dm.addRow(rows);
		}

		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    totalAmt = Math.rint(totalAmt);
		}
		else
		{
		    totalAmt = Double.parseDouble(gDecimalFormat.format(totalAmt));
		}
		txtTotal.setText(String.valueOf(totalAmt));

		tblItemTable.setModel(dm);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblItemTable.setShowHorizontalLines(true);
		tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
		tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(40);
	    }
	    else if (clsGlobalVarClass.gShowPurRateInDirectBiller)
	    {
		dm.setRowCount(0);
		dm.addColumn("Description");
		dm.addColumn("Qty");
		dm.addColumn("Amt");
		dm.addColumn("Pur Rate");
		dm.addColumn("ItemCode");
		dm.addColumn("SeqNo");

		Collections.sort(obj_List_ItemDtl, clsDirectBillerItemDtl.comparatorDirectBillerItemDtl);
		for (clsDirectBillerItemDtl listItemRow : obj_List_ItemDtl)
		{
		    totalAmt = totalAmt + listItemRow.getAmt();
		    Object[] rows =
		    {
			listItemRow.getItemName(), gDecimalFormat.format(listItemRow.getQty()), gDecimalFormat.format(listItemRow.getAmt()), listItemRow.getPurchaseRate(), listItemRow.getItemCode(), listItemRow.getSeqNo()
		    };
		    subTotalForTax += listItemRow.getAmt();
		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(listItemRow.getItemCode());
		    objItemDtl.setItemName(listItemRow.getItemName());
		    objItemDtl.setAmount(listItemRow.getAmt());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);

		    dm.addRow(rows);
		}

		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    totalAmt = Math.rint(totalAmt);
		}
		else
		{
		    totalAmt = Double.parseDouble(gDecimalFormat.format(totalAmt));
		}
		txtTotal.setText(String.valueOf(totalAmt));

		tblItemTable.setModel(dm);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblItemTable.setShowHorizontalLines(true);
		tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
		tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(40);
	    }
	    else
	    {
		dm.setRowCount(0);
		dm.addColumn("Description");
		dm.addColumn("Qty");
		dm.addColumn("Amt");
		dm.addColumn("ItemCode");
		dm.addColumn("SeqNo");

		Collections.sort(obj_List_ItemDtl, clsDirectBillerItemDtl.comparatorDirectBillerItemDtl);
		for (clsDirectBillerItemDtl listItemRow : obj_List_ItemDtl)
		{
		    totalAmt = totalAmt + listItemRow.getAmt();

		    Object[] rows =
		    {
			listItemRow.getItemName(), gDecimalFormat.format(listItemRow.getQty()), gDecimalFormat.format(listItemRow.getAmt()), listItemRow.getItemCode(), listItemRow.getSeqNo()
		    };

		    dm.addRow(rows);
		    String itemDtl = listItemRow.getItemCode() + "#" + listItemRow.getItemName() + "#" + listItemRow.getQty() + "#" + listItemRow.getAmt();
		    //arrListMenuItems.add(itemDtl);
		    subTotalForTax += listItemRow.getAmt();
		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(listItemRow.getItemCode());
		    objItemDtl.setItemName(listItemRow.getItemName());
		    objItemDtl.setAmount(listItemRow.getAmt());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);
		}

		if (clsGlobalVarClass.gCalculateTaxOnMakeKOT)
		{

		    String dtPOSDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
		    String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    if (dineInForTax.equalsIgnoreCase("Y"))
		    {
			areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		    }

		    if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		    {
			areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		    }

		    if (takeAwayForTax.equalsIgnoreCase("Y"))
		    {
			areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		    }
		    List<clsTaxCalculationDtls> listTax = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaWisePricingAreaCode, "DineIn", subTotalForTax, 0, "", "S01", "Sales");

		    double taxAmt = 0;
		    for (clsTaxCalculationDtls objTaxDtl : listTax)
		    {
			if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
			{
			    taxAmt = taxAmt + objTaxDtl.getTaxAmount();
			}
		    }

		    totalAmt += taxAmt;
		}

		//start code to calculate roundoff amount and round off by amt		
		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(totalAmt);
		    totalAmt = mapRoundOff.get("roundOffAmt");
		}
		else
		{
		    totalAmt = Double.parseDouble(gDecimalFormat.format(totalAmt));
		}
		txtTotal.setText(String.valueOf(totalAmt));

		tblItemTable.setModel(dm);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblItemTable.setShowHorizontalLines(true);
		tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(180);
		tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    }

	    JButton arrBtnQty[] =
	    {
		btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnMultiQty
	    };
	    for (int cnt = 0; cnt < arrBtnQty.length; cnt++)
	    {
		arrBtnQty[cnt].setEnabled(true);
	    }

	}
	catch (Exception ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    ex.printStackTrace();
	}
    }

    private List funCalculateTax(String operationType, ArrayList<Object> arrListItemDtl)
    {
	List listTax = new ArrayList<ArrayList<Object>>();
	try
	{
	    listTax = funCheckDateRangeForTax(operationType, arrListItemDtl);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return listTax;
    }

    private ArrayList funCheckDateRangeForTax(String operationType, ArrayList<Object> arrListItemDtl) throws Exception
    {
	String taxCode = "", taxName = "", taxOnGD = "", taxCal = "", taxIndicator = "";
	String opType = "", area = "", taxOnTax = "", taxOnTaxCode = "", itemType = "";
	double taxPercent = 0.00, taxableAmount = 0.00, taxCalAmt = 0.00;
	ArrayList<Object> listTax = new ArrayList<Object>();
	arrListTaxCal = new ArrayList<ArrayList<Object>>();
	clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp;");// Empty Tax Temp Table
	double subTotalForTax = 0;

	for (int cnt1 = 0; cnt1 < arrListItemDtl.size(); cnt1++)
	{
	    //System.out.println(arrListItemDtl.get(cnt1));
	    String[] spItemDtl = arrListItemDtl.get(cnt1).toString().split("#");
	    subTotalForTax += Double.parseDouble(spItemDtl[3]);
	}
	String dtPOSDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	String sql_ChkTaxDate = "select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent"
		+ ",a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType"
		+ ",a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
		+ "from tbltaxhd a,tbltaxposdtl b "
		+ "where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and date(a.dteValidFrom) <='" + dtPOSDate + "' "
		+ "and date(a.dteValidTo)>='" + dtPOSDate + "' and a.strTaxOnSP='Sales' "
		+ "order by a.strTaxOnTax,a.strTaxCode";

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
	    if (funCheckAreaCode(taxCode, area))
	    {
		if (funCheckOperationType(taxCode, opType))
		{
		    if (funFindSettlementForTax(taxCode, "S01"))
		    {
			listTax = new ArrayList<Object>();
			if (taxIndicator.trim().length() > 0) // For Indicator Based Tax
			{
			    double taxIndicatorTotal = funGetTaxIndicatorTotal(taxIndicator, arrListItemDtl);
			    if (taxIndicatorTotal > 0)
			    {
				if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
				{
				    taxIndicatorTotal += funGetTaxAmountForTaxOnTaxForIndicatorTax(taxOnTaxCode, taxIndicatorTotal);
				}
				taxableAmount = taxIndicatorTotal;

				if (taxCal.equals("Forward")) // Forward Tax Calculation
				{
				    taxCalAmt = taxableAmount * (taxPercent / 100);
				}
				else // Backward Tax Calculation
				{
				    taxCalAmt = taxableAmount * 100 / (100 + taxPercent);
				    taxCalAmt = taxableAmount - taxCalAmt;
				}
				listTax.add(taxCode);
				listTax.add(taxName);
				listTax.add(taxableAmount);
				listTax.add(taxCalAmt);
				listTax.add(taxCal);
				arrListTaxCal.add(listTax);
				//funInsertTaxTemp(taxCode,taxName,taxableAmount,taxCalAmt,taxCal);
			    }
			}
			else // For Blank Indicator
			{
			    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation
			    {
				taxableAmount = funGetTaxableAmountForTaxOnTax(taxOnTaxCode);
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
			    else
			    {
				taxableAmount = subTotalForTax;

				if (taxCal.equals("Forward")) // Forward Tax Calculation
				{
				    taxCalAmt = taxableAmount * (taxPercent / 100);
				}
				else // Backward Tax Calculation
				{
				    taxCalAmt = taxableAmount * 100 / (100 + taxPercent);
				    taxCalAmt = taxableAmount - taxCalAmt;
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
	}
	return arrListTaxCal;
    }

    private boolean funCheckAreaCode(String taxCode, String area)
    {
	boolean flgTaxOn = false;
	String[] spAreaCode = area.split(",");
	for (int cnt = 0; cnt < spAreaCode.length; cnt++)
	{

	    String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    if (dineInForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    }

	    if (homeDeliveryForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	    }

	    if (takeAwayForTax.equalsIgnoreCase("Y"))
	    {
		areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	    }

	    if (spAreaCode[cnt].equals(areaWisePricingAreaCode))
	    {
		flgTaxOn = true;
		break;
	    }
	}
	return flgTaxOn;
    }

    private boolean funCheckOperationType(String taxCode, String opType)
    {
	boolean flgTaxOn = false;
	String[] spOpType = opType.split(",");
	for (int cnt = 0; cnt < spOpType.length; cnt++)
	{
	    if (spOpType[cnt].equals("HomeDelivery") && homeDeliveryForTax.equalsIgnoreCase("Y"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("DineIn") && dineInForTax.equalsIgnoreCase("Y"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("TakeAway") && takeAwayForTax.equalsIgnoreCase("Yes"))
	    {
		flgTaxOn = true;
		break;
	    }
	}
	return flgTaxOn;
    }

    private double funGetTaxIndicatorTotal(String indicator, ArrayList arrListItemDtlTemp) throws Exception
    {
	String sql_Query = "";
	double indicatorAmount = 0.00;

	for (int cnt = 0; cnt < arrListItemDtlTemp.size(); cnt++)
	{
	    String[] spItemDtl = arrListItemDtlTemp.get(cnt).toString().split("#");
	    sql_Query = "select " + spItemDtl[3]
		    + " from tblitemmaster "
		    + " where strItemCode='" + spItemDtl[0] + "' "
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

    private boolean funFindSettlementForTax(String taxCode, String settlementMode) throws Exception
    {
	boolean flgTaxSettlement = false;
	String sql_SettlementTax = "select strSettlementCode,strSettlementName "
		+ "from tblsettlementtax where strTaxCode='" + taxCode + "' "
		+ "and strApplicable='true' and strSettlementName='" + settlementMode + "'";
	ResultSet rsTaxSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_SettlementTax);
	if (rsTaxSettlement.next())
	{
	    flgTaxSettlement = true;
	}
	rsTaxSettlement.close();
	return flgTaxSettlement;
    }

    private double funGetTaxableAmountForTaxOnTax(String taxOnTaxCode) throws Exception
    {
	double taxableAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
	{
	    for (int t = 0; t < spTaxOnTaxCode.length; t++)
	    {
		ArrayList arrListTax = arrListTaxCal.get(cnt);
		if (arrListTax.get(0).toString().equals(spTaxOnTaxCode[t]))
		{
		    taxableAmt += Double.parseDouble(arrListTax.get(2).toString()) + Double.parseDouble(arrListTax.get(3).toString());
		}
	    }
	}
	return taxableAmt;
    }

    private void funFillModifierTableForTDH(String selectedItemCode)
    {
	try
	{
	    sql = "select strItemCode from tbltdhhd where strItemCode='" + selectedItemCode + "' and  strComboItemYN='N' and strApplicable='Y';";
	    ResultSet rsTDHForModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTDHForModifier.next())
	    {
		String sql1 = "select strApplicable from tblitemmodofier where strItemCode='" + selectedItemCode + "'"
			+ " or strItemCode='All'";
		ResultSet modifierapplicable = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
		if (modifierapplicable.next())
		{
		    if ("y".equalsIgnoreCase(modifierapplicable.getString(1)))
		    {
			panelItemList.setVisible(false);
			panelNavigate.setVisible(false);
			panelSubGroup.setVisible(false);
			panelPLU.setVisible(false);
			if (objPanelModifier == null)
			{
			    objPanelModifier = new panelModifier(this);
			    objPanelModifier.getItemCode(selectedItemCode);
			    add(objPanelModifier);
			    objPanelModifier.setLocation(panelNavigate.getLocation());
			    objPanelModifier.setVisible(true);
			    objPanelModifier.setSize(380, 500);
			    objPanelModifier.revalidate();

			}
			else
			{
			    objPanelModifier.getItemCode(selectedItemCode);
			    objPanelModifier.setVisible(true);
			}
		    }
		}
		modifierapplicable.close();
	    }
	    rsTDHForModifier.close();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funShowComboSubItemTable(String itemCode)
    {
	try
	{
	    if (objPanelSubItem == null)
	    {
		panelItemList.setVisible(false);
		panelNavigate.setVisible(false);
		panelSubGroup.setVisible(false);
		panelPLU.setVisible(false);

		objPanelSubItem = new panelSubItem(this);
		objPanelSubItem.getItem(itemCode);

		panelFormBody.add(objPanelSubItem);
		objPanelSubItem.setLocation(panelNavigate.getLocation());

		objPanelSubItem.setVisible(true);
		objPanelSubItem.setSize(380, 500);

		objPanelSubItem.invalidate();
		objPanelSubItem.validate();
		objPanelSubItem.revalidate();
		objPanelSubItem.repaint();
	    }
	    else
	    {
		objPanelSubItem.getItem(itemCode);
		panelItemList.setVisible(false);
		panelNavigate.setVisible(false);

		panelFormBody.add(objPanelSubItem);
		objPanelSubItem.setLocation(panelNavigate.getLocation());

		objPanelSubItem.setVisible(true);
		objPanelSubItem.setSize(380, 500);

		objPanelSubItem.invalidate();
		objPanelSubItem.validate();
		objPanelSubItem.revalidate();
		objPanelSubItem.repaint();
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void insertDefaultSubItems(String icode)
    {
	try
	{
	    List<clsTDHOnItemDtl.subItemDtl> ItemList = clsTDHOnItemDtl.hm_ComboItemDtl.get(icode);
	    for (clsTDHOnItemDtl.subItemDtl ob : ItemList)
	    {
		String temp_SubItemName = ob.getstrSubItemName();
		String temp_SubItemCode = ob.getStrSubItemCode();
		String defaultYN = ob.getStrDefaultYN();
		/*
                 * sTART OF KOT object List
		 */
		if ("Y".equalsIgnoreCase(defaultYN))
		{
		    clsDirectBillerItemDtl ob1 = new clsDirectBillerItemDtl("=>".concat(temp_SubItemName), temp_SubItemCode, 1.00, 0.00, false, "", "Y", icode, 0.00, "", getSeqNo(), 0);
		    obj_List_ItemDtl.add(ob1);
		}
		/*
                 * End OF KOT object List
		 */
	    }
	    funRefreshItemTable();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funResetSortingButtons()
    {
	try
	{
	    btnItemSorting1.setEnabled(false);
	    btnItemSorting2.setEnabled(false);
	    btnItemSorting3.setEnabled(false);
	    btnItemSorting4.setEnabled(false);
	    btnPrevItemSorting.setEnabled(false);
	    btnNextItemSorting.setEnabled(false);
	    btnItemSorting1.setText("");
	    btnItemSorting2.setText("");
	    btnItemSorting3.setText("");
	    btnItemSorting4.setText("");
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funHomeButtonPressed()
    {
	try
	{
	    objUtility = new clsUtility();

	    clsGlobalVarClass.hmActiveForms.remove("Direct Biller");
	    int rowNo = tblItemTable.getRowCount();
	    if (rowNo > 0)
	    {
		frmOkCancelPopUp okOb = new frmOkCancelPopUp(this, "Do you want to end transaction");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{
		    int srno = 1;
		    if (null != obj_List_ItemDtl && obj_List_ItemDtl.size() > 0)
		    {
			String insertLineVoid = "insert into tbllinevoid(strSerialno,strPosCode,strItemCode,strItemName,"
				+ "dblItemQuantity,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited) "
				+ "values ";
			for (clsDirectBillerItemDtl list_cls_ItemRow : obj_List_ItemDtl)
			{
			    insertLineVoid += "('" + srno + "','" + clsGlobalVarClass.gPOSCode + "','" + list_cls_ItemRow.getItemCode() + "','" + list_cls_ItemRow.getItemName() + "',"
				    + "'" + list_cls_ItemRow.getQty() + "','" + list_cls_ItemRow.getAmt() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + objUtility.funGetPOSDateForTransaction() + "','" + objUtility.funGetPOSDateForTransaction() + "'),";
			    srno++;
			}
			StringBuilder sb = new StringBuilder(insertLineVoid);
			int index = sb.lastIndexOf(",");
			insertLineVoid = sb.delete(index, sb.length()).toString();
			clsGlobalVarClass.dbMysql.execute(insertLineVoid);
		    }
		    obj_List_ItemDtl.clear();

		    funFreeObjectsFromMemory();
		    dispose();
		}
		else
		{

		}
	    }
	    else
	    {
		funFreeObjectsFromMemory();
		dispose();
	    }
	    funResetHomeDeliveryFileds();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funFreeObjectsFromMemory()
    {
	objData = null;
	objPanelModifier = null;
	objPanelSubItem = null;
	objAgainstAdvOrder = null;
	hmHappyHourItems = null;
	hm_ModifierDtl = null;
	hm_ModifierGroup = null;
	hmDirectBillerParams = null;
	objUtility = null;
    }

    private void funHomeDeliveryButtonPressed()
    {
	funResetTakeAway();
	if (flgBillForAdvOrder)
	{
	    funSetCustomerForAdvOrderHD();
	    btnHomeDelivery.setForeground(Color.black);
	    dineInForTax = "N";
	    homeDeliveryForTax = "Y";
	    takeAwayForTax = "N";
	}
	else
	{

	    //System.out.println("Color= "+btnHomeDelivery.getForeground() );
	    if (btnHomeDelivery.getForeground() == Color.white)
	    {
		btnHomeDelivery.setForeground(Color.black);

		dineInForTax = "N";
		homeDeliveryForTax = "Y";
		takeAwayForTax = "N";

		if (clsGlobalVarClass.gCustAddressSelectionForBill)
		{
		    frmHomeDeliveryAddress objDeliveryAddress = new frmHomeDeliveryAddress(this, true, clsGlobalVarClass.gCustMBNo);
		    objDeliveryAddress.setVisible(true);

		    String[] data = objDeliveryAddress.funGetCustomerAddressDetail();
		    String billNote = objDeliveryAddress.funGetBillNote();

		    //clsGlobalVarClass.gCustomerCodeForHomeDelivery = data[0].toString();
		    clsGlobalVarClass.gCustomerCode = data[0].toString();
		    hmDirectBillerParams.put("CustAddType", data[2].toString());
		    hmDirectBillerParams.put("BillNote", billNote);
		    //clsGlobalVarClass.funGetDeliveryCharges(data[6].toString(),totalBillAmount);
		    clsGlobalVarClass.gSearchItemClicked = false;
		    lblCustInfo.setText("<html>" + data[1].toString() + "</html>");
		}
		else
		{

		}
	    }
	    else
	    {
		funResetHomeDeliveryButton();
	    }

	    if (clsGlobalVarClass.gAreaWisePricing.equalsIgnoreCase("Y"))
	    {
		funRefreshMenuForAreaWisePricingArea();
	    }

	}
    }

    private void funScanBarCodeButtonPressed()
    {
	if (btnCustomerHistory.isSelected())
	{
	    btnCustomerHistory.setForeground(Color.black);
	    txtExternalCode.requestFocus();
	}
	else
	{
	    btnCustomerHistory.setForeground(Color.WHITE);
	}
    }

    private void funAgainstAdvOrderButtonPressed()
    {
	panelItemList.setVisible(false);
	panelNavigate.setVisible(false);
	panelSubGroup.setVisible(false);
	panelPLU.setVisible(false);
	if (objAgainstAdvOrder == null)
	{
	    objAgainstAdvOrder = new frmAgainstAdvBookorder(this);
	    if (objPanelModifier == null)
	    {
	    }
	    else
	    {
		objPanelModifier.setVisible(false);
	    }

	    panelFormBody.add(objAgainstAdvOrder);
	    objAgainstAdvOrder.setLocation(panelPLU.getLocation());
	    objAgainstAdvOrder.setVisible(true);
	    objAgainstAdvOrder.setSize(350, 500);
	    //objAgainstAdvOrder.revalidate();
	    //pack();
	}
	else
	{
	    //objAgainstAdvOrder.revalidate();
	    objAgainstAdvOrder.updateUI();
	    //objAgainstAdvOrder.repaint();
	    objAgainstAdvOrder.filltable();
	    objAgainstAdvOrder.setVisible(true);
	    if (objPanelModifier != null)
	    {
		objPanelModifier.setVisible(false);
	    }
	}
    }

    private void funTakeAwayButtonPressed()
    {
	try
	{
	    funResetHomeDeliveryButton();
	    if ("No".equals(clsGlobalVarClass.gTakeAway))
	    {
		clsGlobalVarClass.gTakeAway = "Yes";

		dineInForTax = "N";
		homeDeliveryForTax = "N";
		takeAwayForTax = "Y";

		btnTakeAway.setForeground(Color.black);
		btnTakeAway.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
	    }
	    else
	    {
		funResetTakeAway();
	    }
	    if (clsGlobalVarClass.gAreaWisePricing.equalsIgnoreCase("Y"))
	    {
		funRefreshMenuForAreaWisePricingArea();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funModifierButtonPressed()
    {
	int rowno = tblItemTable.getSelectedRow();
	if (rowno > -1)
	{
	    if (tblItemTable.getRowCount() > 0)
	    {
		if (!(tblItemTable.getValueAt(rowno, 0).toString().startsWith("KT")) && !(tblItemTable.getValueAt(rowno, 0).toString().startsWith("-->")) && !(tblItemTable.getValueAt(rowno, 0).toString().startsWith("=>")))
		{
		    if (clsGlobalVarClass.gShowItemStkColumnInDB)
		    {
			if (!clsGlobalVarClass.ListTDHOnModifierItem.contains(tblItemTable.getValueAt(rowno, 4).toString()))
			{
			    if (funIsFreshItem(tblItemTable.getValueAt(rowno, 4).toString()))
			    {
				funShowModifier(tblItemTable.getValueAt(rowno, 4).toString());
			    }
			}
		    }
		    else
		    {
			if (!clsGlobalVarClass.ListTDHOnModifierItem.contains(tblItemTable.getValueAt(rowno, 3).toString()))
			{
			    if (funIsFreshItem(tblItemTable.getValueAt(rowno, 3).toString()))
			    {
				funShowModifier(tblItemTable.getValueAt(rowno, 3).toString());
			    }
			}
		    }
		}
	    }
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
        panelMainForm = new javax.swing.JPanel();
        panelFormBody = new javax.swing.JPanel();
        panelOperationButtons = new javax.swing.JPanel();
        btnSettle = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnPLU = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnAgainstAdvOrder = new javax.swing.JButton();
        btnTakeAway = new javax.swing.JButton();
        btnCustomerHistory = new javax.swing.JToggleButton();
        btnHomeDelivery = new javax.swing.JButton();
        btnDelBoy = new javax.swing.JButton();
        panelItemDtl = new javax.swing.JPanel();
        scrItemGrid = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        txtTotal = new javax.swing.JTextField();
        lblPaxNo = new javax.swing.JLabel();
        btnDelItem = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        lblDateTime = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblVoucherNo = new javax.swing.JLabel();
        btnChangeQty = new javax.swing.JButton();
        panelMenuHead = new javax.swing.JPanel();
        btnMenu3 = new javax.swing.JButton();
        btnMenu2 = new javax.swing.JButton();
        btnMenu4 = new javax.swing.JButton();
        btnMenu6 = new javax.swing.JButton();
        btnMenu8 = new javax.swing.JButton();
        btnMenu7 = new javax.swing.JButton();
        btnPopular = new javax.swing.JButton();
        btnMenu5 = new javax.swing.JButton();
        btnNextMenu = new javax.swing.JButton();
        panelItemList = new javax.swing.JPanel();
        btnIItem2 = new javax.swing.JButton();
        btnIItem1 = new javax.swing.JButton();
        btnIItem3 = new javax.swing.JButton();
        btnIItem4 = new javax.swing.JButton();
        btnIItem5 = new javax.swing.JButton();
        btnIItem6 = new javax.swing.JButton();
        btnIItem7 = new javax.swing.JButton();
        btnIItem8 = new javax.swing.JButton();
        btnIItem9 = new javax.swing.JButton();
        btnIItem10 = new javax.swing.JButton();
        btnIItem11 = new javax.swing.JButton();
        btnIItem12 = new javax.swing.JButton();
        btnIItem13 = new javax.swing.JButton();
        btnIItem14 = new javax.swing.JButton();
        btnIItem15 = new javax.swing.JButton();
        btnIItem16 = new javax.swing.JButton();
        panelNumeric = new javax.swing.JPanel();
        btnNumber2 = new javax.swing.JButton();
        btnNumber1 = new javax.swing.JButton();
        btnNumber4 = new javax.swing.JButton();
        btnNumber3 = new javax.swing.JButton();
        btnNumber5 = new javax.swing.JButton();
        btnNumber6 = new javax.swing.JButton();
        btnNumber7 = new javax.swing.JButton();
        btnNumber8 = new javax.swing.JButton();
        btnNumber9 = new javax.swing.JButton();
        btnMultiQty = new javax.swing.JButton();
        panelNavigate = new javax.swing.JPanel();
        panelCustomer = new javax.swing.JPanel();
        lblCustInfo = new javax.swing.JLabel();
        btnPrevItem = new javax.swing.JButton();
        btnNextItem = new javax.swing.JButton();
        panelExternalCode = new javax.swing.JPanel();
        txtExternalCode = new javax.swing.JTextField();
        lblExtCode = new javax.swing.JLabel();
        panelSubGroup = new javax.swing.JPanel();
        btnPrevItemSorting = new javax.swing.JButton();
        btnItemSorting1 = new javax.swing.JButton();
        btnItemSorting2 = new javax.swing.JButton();
        btnItemSorting3 = new javax.swing.JButton();
        btnNextItemSorting = new javax.swing.JButton();
        btnItemSorting4 = new javax.swing.JButton();
        lblDelBoyName = new javax.swing.JLabel();
        panelPLU = new javax.swing.JPanel();
        bttnPLUPanelClose = new javax.swing.JButton();
        txtPLUItemSearch = new javax.swing.JTextField();
        scrPLU = new javax.swing.JScrollPane();
        tblPLUItems = new javax.swing.JTable();
        btnPrevMenu = new javax.swing.JButton();
        panelArea = new javax.swing.JPanel();
        btnArea = new javax.swing.JButton();
        lblExtCode1 = new javax.swing.JLabel();

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
        lblformName.setText("- Direct Biller");
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
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setBackground(new java.awt.Color(255, 255, 255));
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelOperationButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelOperationButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSettle.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnSettle.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle.setMnemonic('D');
        btnSettle.setText("<html><u>D</u>ONE</html>");
        btnSettle.setToolTipText("Go To Settle Bill");
        btnSettle.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnSettle.setBorderPainted(false);
        btnSettle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettleActionPerformed(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setMnemonic('H');
        btnHome.setText("<html><u>H</u>OME</html>");
        btnHome.setToolTipText("Close Direct Biller Form");
        btnHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnHome.setBorderPainted(false);
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeActionPerformed(evt);
            }
        });

        btnPLU.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPLU.setForeground(new java.awt.Color(255, 255, 255));
        btnPLU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPLU.setText("<html><u>P</u>LU</html>");
        btnPLU.setToolTipText("Search Item From List");
        btnPLU.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnPLU.setBorderPainted(false);
        btnPLU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPLU.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPLU.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPLUActionPerformed(evt);
            }
        });
        btnPLU.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPLUKeyPressed(evt);
            }
        });

        btnModifier.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnModifier.setForeground(new java.awt.Color(255, 255, 255));
        btnModifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnModifier.setText("<html><u>M</u>ODIFIER</html>");
        btnModifier.setToolTipText("Apply Modifier to Selected Item");
        btnModifier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModifier.setPreferredSize(new java.awt.Dimension(102, 42));
        btnModifier.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnModifier.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModifierActionPerformed(evt);
            }
        });

        btnAgainstAdvOrder.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnAgainstAdvOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnAgainstAdvOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnAgainstAdvOrder.setText("<html>AGAINST<br><u>A</u>DV-ORDER</html>");
        btnAgainstAdvOrder.setToolTipText("Select Advance Order");
        btnAgainstAdvOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgainstAdvOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnAgainstAdvOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAgainstAdvOrderActionPerformed(evt);
            }
        });

        btnTakeAway.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnTakeAway.setForeground(new java.awt.Color(255, 255, 255));
        btnTakeAway.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnTakeAway.setText("<html><u>T</u>AKE<br> AWAY</html>");
        btnTakeAway.setToolTipText("Make this bill as Take Away");
        btnTakeAway.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTakeAway.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnTakeAway.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTakeAwayActionPerformed(evt);
            }
        });

        btnCustomerHistory.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnCustomerHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnCustomerHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCustomerHistory.setText("<html>CUSTOMER<br>HI<u>S</u>TORY<html>");
        btnCustomerHistory.setToolTipText("");
        btnCustomerHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustomerHistory.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnCustomerHistory.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCustomerHistoryActionPerformed(evt);
            }
        });

        btnHomeDelivery.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnHomeDelivery.setForeground(Color.white);
        btnHomeDelivery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHomeDelivery.setText("<html>    H<u>O</u>ME<br>DELIVERY</html>");
        btnHomeDelivery.setToolTipText("Make this bill as Home Delivery");
        btnHomeDelivery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHomeDelivery.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHomeDelivery.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeDeliveryActionPerformed(evt);
            }
        });

        btnDelBoy.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDelBoy.setForeground(new java.awt.Color(255, 255, 255));
        btnDelBoy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDelBoy.setMnemonic('B');
        btnDelBoy.setText("<html>DELIVERY<br><u>B</u>OY</html>");
        btnDelBoy.setToolTipText("Select Delivery Boy");
        btnDelBoy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelBoy.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDelBoy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelBoyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperationButtonsLayout = new javax.swing.GroupLayout(panelOperationButtons);
        panelOperationButtons.setLayout(panelOperationButtonsLayout);
        panelOperationButtonsLayout.setHorizontalGroup(
            panelOperationButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationButtonsLayout.createSequentialGroup()
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCustomerHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgainstAdvOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTakeAway, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelBoy, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSettle, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelOperationButtonsLayout.setVerticalGroup(
            panelOperationButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationButtonsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelOperationButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOperationButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnModifier, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHome, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelOperationButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgainstAdvOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustomerHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTakeAway, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelBoy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnSettle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(btnPLU, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        panelOperationButtonsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAgainstAdvOrder, btnCustomerHistory, btnDelBoy, btnHome, btnHomeDelivery, btnModifier, btnSettle, btnTakeAway});

        panelItemDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtl.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtl.setPreferredSize(new java.awt.Dimension(260, 600));
        panelItemDtl.setLayout(null);

        tblItemTable.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
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
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseEntered(evt);
            }
        });
        scrItemGrid.setViewportView(tblItemTable);

        panelItemDtl.add(scrItemGrid);
        scrItemGrid.setBounds(0, 0, 300, 460);

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        panelItemDtl.add(txtTotal);
        txtTotal.setBounds(210, 470, 80, 40);
        panelItemDtl.add(lblPaxNo);
        lblPaxNo.setBounds(290, 20, 0, 0);

        btnDelItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDelItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelItem.setToolTipText("Delete item from list");
        btnDelItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelItemActionPerformed(evt);
            }
        });
        panelItemDtl.add(btnDelItem);
        btnDelItem.setBounds(150, 470, 40, 48);

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUpArrow.png"))); // NOI18N
        btnUp.setText("UP");
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUpArrow.png"))); // NOI18N
        btnUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpActionPerformed(evt);
            }
        });
        panelItemDtl.add(btnUp);
        btnUp.setBounds(0, 470, 40, 48);

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownArrow.png"))); // NOI18N
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownArrow.png"))); // NOI18N
        btnDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDownActionPerformed(evt);
            }
        });
        panelItemDtl.add(btnDown);
        btnDown.setBounds(50, 470, 40, 48);
        panelItemDtl.add(lblDateTime);
        lblDateTime.setBounds(100, 30, 80, 30);

        jLabel4.setText("Date ");
        panelItemDtl.add(jLabel4);
        jLabel4.setBounds(10, 30, 60, 30);

        jLabel2.setText("Bill No.");
        panelItemDtl.add(jLabel2);
        jLabel2.setBounds(10, 0, 60, 30);
        panelItemDtl.add(lblVoucherNo);
        lblVoucherNo.setBounds(100, 0, 90, 30);

        btnChangeQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnChangeQty.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgChangeQty.png"))); // NOI18N
        btnChangeQty.setToolTipText("Change Quantity of an Item");
        btnChangeQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgChangeQty.png"))); // NOI18N
        btnChangeQty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnChangeQtyActionPerformed(evt);
            }
        });
        panelItemDtl.add(btnChangeQty);
        btnChangeQty.setBounds(100, 470, 40, 48);

        panelMenuHead.setBackground(new java.awt.Color(255, 255, 255));
        panelMenuHead.setForeground(new java.awt.Color(255, 236, 205));

        btnMenu3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu3.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu3.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMenu3MouseClicked(evt);
            }
        });
        btnMenu3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu3ActionPerformed(evt);
            }
        });

        btnMenu2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu2.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu2.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu2ActionPerformed(evt);
            }
        });

        btnMenu4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu4.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu4.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMenu4MouseClicked(evt);
            }
        });
        btnMenu4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu4ActionPerformed(evt);
            }
        });

        btnMenu6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu6.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu6.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu6ActionPerformed(evt);
            }
        });

        btnMenu8.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu8.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu8.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu8ActionPerformed(evt);
            }
        });

        btnMenu7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu7.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu7.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu7ActionPerformed(evt);
            }
        });

        btnPopular.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPopular.setForeground(new java.awt.Color(255, 255, 255));
        btnPopular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnPopular.setText("Popular");
        btnPopular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopular.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPopular.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnPopular.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopularActionPerformed(evt);
            }
        });

        btnMenu5.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu5.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnMenu5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu5.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnMenu5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu5ActionPerformed(evt);
            }
        });

        btnNextMenu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNextMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnNextMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownDark.png"))); // NOI18N
        btnNextMenu.setToolTipText("Next Menu Head");
        btnNextMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnNextMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownLite.png"))); // NOI18N
        btnNextMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMenuHeadLayout = new javax.swing.GroupLayout(panelMenuHead);
        panelMenuHead.setLayout(panelMenuHeadLayout);
        panelMenuHeadLayout.setHorizontalGroup(
            panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuHeadLayout.createSequentialGroup()
                .addGroup(panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMenuHeadLayout.setVerticalGroup(
            panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMenuHeadLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        panelItemList.setBackground(new java.awt.Color(255, 255, 255));
        panelItemList.setEnabled(false);

        btnIItem2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnIItem2MouseEntered(evt);
            }
        });

        btnIItem1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem1ActionPerformed(evt);
            }
        });

        btnIItem3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem3MouseClicked(evt);
            }
        });
        btnIItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem3ActionPerformed(evt);
            }
        });

        btnIItem4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem4MouseClicked(evt);
            }
        });
        btnIItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem4ActionPerformed(evt);
            }
        });

        btnIItem5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem5MouseClicked(evt);
            }
        });
        btnIItem5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem5ActionPerformed(evt);
            }
        });

        btnIItem6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem6MouseClicked(evt);
            }
        });
        btnIItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem6ActionPerformed(evt);
            }
        });

        btnIItem7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem7MouseClicked(evt);
            }
        });
        btnIItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem7ActionPerformed(evt);
            }
        });

        btnIItem8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem8MouseClicked(evt);
            }
        });
        btnIItem8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem8ActionPerformed(evt);
            }
        });

        btnIItem9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem9MouseClicked(evt);
            }
        });
        btnIItem9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem9ActionPerformed(evt);
            }
        });

        btnIItem10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem10MouseClicked(evt);
            }
        });
        btnIItem10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem10ActionPerformed(evt);
            }
        });

        btnIItem11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem11MouseClicked(evt);
            }
        });
        btnIItem11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem11ActionPerformed(evt);
            }
        });

        btnIItem12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem12MouseClicked(evt);
            }
        });
        btnIItem12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem12ActionPerformed(evt);
            }
        });

        btnIItem13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem13MouseClicked(evt);
            }
        });
        btnIItem13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem13ActionPerformed(evt);
            }
        });

        btnIItem14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem14MouseClicked(evt);
            }
        });
        btnIItem14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem14ActionPerformed(evt);
            }
        });

        btnIItem15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem15MouseClicked(evt);
            }
        });
        btnIItem15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem15ActionPerformed(evt);
            }
        });

        btnIItem16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem16MouseClicked(evt);
            }
        });
        btnIItem16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelItemListLayout = new javax.swing.GroupLayout(panelItemList);
        panelItemList.setLayout(panelItemListLayout);
        panelItemListLayout.setHorizontalGroup(
            panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemListLayout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemListLayout.createSequentialGroup()
                        .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemListLayout.createSequentialGroup()
                        .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemListLayout.createSequentialGroup()
                        .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemListLayout.createSequentialGroup()
                        .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );
        panelItemListLayout.setVerticalGroup(
            panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemListLayout.createSequentialGroup()
                .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelItemListLayout.createSequentialGroup()
                            .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNumeric.setBackground(new java.awt.Color(255, 255, 255));
        panelNumeric.setForeground(new java.awt.Color(255, 236, 205));

        btnNumber2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber2.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber2.setText("2");
        btnNumber2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber2MouseClicked(evt);
            }
        });

        btnNumber1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber1.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber1.setText("1");
        btnNumber1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber1MouseClicked(evt);
            }
        });

        btnNumber4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber4.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber4.setText("4");
        btnNumber4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber4MouseClicked(evt);
            }
        });

        btnNumber3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber3.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber3.setText("3");
        btnNumber3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber3MouseClicked(evt);
            }
        });

        btnNumber5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber5.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber5.setText("5");
        btnNumber5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber5MouseClicked(evt);
            }
        });

        btnNumber6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber6.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber6.setText("6");
        btnNumber6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber6MouseClicked(evt);
            }
        });

        btnNumber7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber7.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber7.setText("7");
        btnNumber7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber7MouseClicked(evt);
            }
        });

        btnNumber8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber8.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber8.setText("8");
        btnNumber8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber8MouseClicked(evt);
            }
        });

        btnNumber9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber9.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber9.setText("9");
        btnNumber9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber9MouseClicked(evt);
            }
        });

        btnMultiQty.setBackground(new java.awt.Color(102, 153, 255));
        btnMultiQty.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        btnMultiQty.setForeground(new java.awt.Color(255, 255, 255));
        btnMultiQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnMultiQty.setText(">>");
        btnMultiQty.setToolTipText("");
        btnMultiQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMultiQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnMultiQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMultiQtyMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelNumericLayout = new javax.swing.GroupLayout(panelNumeric);
        panelNumeric.setLayout(panelNumericLayout);
        panelNumericLayout.setHorizontalGroup(
            panelNumericLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericLayout.createSequentialGroup()
                .addGroup(panelNumericLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnMultiQty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        panelNumericLayout.setVerticalGroup(
            panelNumericLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNumericLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMultiQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        panelNavigate.setBackground(new java.awt.Color(255, 255, 255));
        panelNavigate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelCustomer.setBackground(new java.awt.Color(69, 164, 238));
        panelCustomer.setForeground(new java.awt.Color(240, 200, 80));

        lblCustInfo.setBackground(new java.awt.Color(255, 255, 255));
        lblCustInfo.setDisplayedMnemonic('C');
        lblCustInfo.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblCustInfo.setForeground(new java.awt.Color(255, 255, 255));
        lblCustInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustInfo.setText("<html><u>C</u>USTOMER</html>");
        lblCustInfo.setToolTipText("Enter Customer Mobile No");
        lblCustInfo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblCustInfoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                lblCustInfoMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout panelCustomerLayout = new javax.swing.GroupLayout(panelCustomer);
        panelCustomer.setLayout(panelCustomerLayout);
        panelCustomerLayout.setHorizontalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCustInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelCustomerLayout.setVerticalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addComponent(lblCustInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnPrevItem.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnPrevItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevItem.setText("<<<");
        btnPrevItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevItemActionPerformed(evt);
            }
        });

        btnNextItem.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnNextItem.setForeground(new java.awt.Color(255, 255, 255));
        btnNextItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextItem.setText(">>>");
        btnNextItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNavigateLayout = new javax.swing.GroupLayout(panelNavigate);
        panelNavigate.setLayout(panelNavigateLayout);
        panelNavigateLayout.setHorizontalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelNavigateLayout.setVerticalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCustomer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        txtExternalCode.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtExternalCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExternalCodeMouseClicked(evt);
            }
        });
        txtExternalCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtExternalCodeActionPerformed(evt);
            }
        });
        txtExternalCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExternalCodeKeyPressed(evt);
            }
        });

        lblExtCode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblExtCode.setText("ITEM CODE :");

        javax.swing.GroupLayout panelExternalCodeLayout = new javax.swing.GroupLayout(panelExternalCode);
        panelExternalCode.setLayout(panelExternalCodeLayout);
        panelExternalCodeLayout.setHorizontalGroup(
            panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExternalCodeLayout.createSequentialGroup()
                .addComponent(lblExtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        panelExternalCodeLayout.setVerticalGroup(
            panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblExtCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtExternalCode, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );

        panelSubGroup.setBackground(new java.awt.Color(255, 255, 255));
        panelSubGroup.setLayout(null);

        btnPrevItemSorting.setText("<<");
        btnPrevItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnPrevItemSorting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnPrevItemSorting.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrevItemSortingMouseClicked(evt);
            }
        });
        panelSubGroup.add(btnPrevItemSorting);
        btnPrevItemSorting.setBounds(0, 0, 30, 40);

        btnItemSorting1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting1MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting1);
        btnItemSorting1.setBounds(30, 0, 70, 40);

        btnItemSorting2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting2MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting2);
        btnItemSorting2.setBounds(100, 0, 70, 40);

        btnItemSorting3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting3MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting3);
        btnItemSorting3.setBounds(170, 0, 70, 40);

        btnNextItemSorting.setText(">>");
        btnNextItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnNextItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnNextItemSorting.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNextItemSortingMouseClicked(evt);
            }
        });
        panelSubGroup.add(btnNextItemSorting);
        btnNextItemSorting.setBounds(310, 0, 30, 40);

        btnItemSorting4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting4MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting4);
        btnItemSorting4.setBounds(240, 0, 70, 40);

        panelPLU.setBackground(new java.awt.Color(255, 255, 255));
        panelPLU.setLayout(null);

        bttnPLUPanelClose.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        bttnPLUPanelClose.setForeground(new java.awt.Color(255, 255, 255));
        bttnPLUPanelClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        bttnPLUPanelClose.setText("CLOSE");
        bttnPLUPanelClose.setToolTipText("Close PLU Panel");
        bttnPLUPanelClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bttnPLUPanelClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                bttnPLUPanelCloseMouseClicked(evt);
            }
        });
        bttnPLUPanelClose.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                bttnPLUPanelCloseKeyPressed(evt);
            }
        });
        panelPLU.add(bttnPLUPanelClose);
        bttnPLUPanelClose.setBounds(260, 0, 90, 40);

        txtPLUItemSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPLUItemSearch.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtPLUItemSearchFocusGained(evt);
            }
        });
        txtPLUItemSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPLUItemSearchMouseClicked(evt);
            }
        });
        txtPLUItemSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPLUItemSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtPLUItemSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                txtPLUItemSearchKeyTyped(evt);
            }
        });
        panelPLU.add(txtPLUItemSearch);
        txtPLUItemSearch.setBounds(10, 0, 250, 40);

        tblPLUItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPLUItems.setRowHeight(25);
        tblPLUItems.getTableHeader().setReorderingAllowed(false);
        tblPLUItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPLUItemsMouseClicked(evt);
            }
        });
        tblPLUItems.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblPLUItemsKeyPressed(evt);
            }
        });
        scrPLU.setViewportView(tblPLUItems);

        panelPLU.add(scrPLU);
        scrPLU.setBounds(10, 40, 340, 400);

        btnPrevMenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPDark.png"))); // NOI18N
        btnPrevMenu.setToolTipText("Previous Menu Head");
        btnPrevMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPrevMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPLite.png"))); // NOI18N
        btnPrevMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevMenuActionPerformed(evt);
            }
        });

        btnArea.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnArea.setForeground(new java.awt.Color(255, 255, 255));
        btnArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnArea.setToolTipText("Search Item From List");
        btnArea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnArea.setBorderPainted(false);
        btnArea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArea.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnArea.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAreaActionPerformed(evt);
            }
        });
        btnArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAreaKeyPressed(evt);
            }
        });

        lblExtCode1.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblExtCode1.setText("Area      :");

        javax.swing.GroupLayout panelAreaLayout = new javax.swing.GroupLayout(panelArea);
        panelArea.setLayout(panelAreaLayout);
        panelAreaLayout.setHorizontalGroup(
            panelAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExtCode1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAreaLayout.createSequentialGroup()
                    .addGap(0, 61, Short.MAX_VALUE)
                    .addComponent(btnArea, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelAreaLayout.setVerticalGroup(
            panelAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblExtCode1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
            .addGroup(panelAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnArea, javax.swing.GroupLayout.PREFERRED_SIZE, 36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelOperationButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(panelSubGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(panelItemList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(panelPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(panelNavigate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(panelArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panelExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelNumeric, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                        .addComponent(btnPrevMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDelBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnPrevMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelNumeric, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(panelFormBodyLayout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(panelPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormBodyLayout.createSequentialGroup()
                                                    .addGap(100, 100, 100)
                                                    .addComponent(panelItemList, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(panelSubGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(panelNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(panelExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panelArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(2, 2, 2)
                                .addComponent(lblDelBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panelItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(panelOperationButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPLUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPLUActionPerformed
	funPLUButtonPressed();
    }//GEN-LAST:event_btnPLUActionPerformed

    private void btnPLUKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPLUKeyPressed
	if (btnPLU.isFocusable() && evt.getKeyCode() == 10)
	{
	    funPLUButtonPressed();
	}
    }//GEN-LAST:event_btnPLUKeyPressed
    public boolean fun_Add_TDH_Modifier(List<clsDirectBillerItemDtl> obj_TDH_Modifier_ItemDtl)
    {
	for (clsDirectBillerItemDtl ob1 : obj_TDH_Modifier_ItemDtl)
	{
	    obj_List_ItemDtl.add(ob1);
	}
	funRefreshItemTable();
	return true;
    }

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
	// TODO add your handling code here:
	funModifierButtonPressed();
    }//GEN-LAST:event_btnModifierActionPerformed

    private boolean funIsFreshItem(String itemCode)
    {
	boolean flag_freshItem = false;
	try
	{
	    if (null != obj_List_ItemDtl)
	    {
		for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
		{
		    if (itemCode.equalsIgnoreCase(ob.getItemCode()) && ob.isIsModifier() == false)
		    {
			flag_freshItem = true;
		    }
		    if (flag_freshItem)
		    {
			break;
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return flag_freshItem;
    }

    private void funShowModifier(String itemCode)
    {
	fun_Reset_Item_Buttons();
	funResetSortingButtons();
	hm_ModifierGroup = fun_fill_Top_Sorting_Buttons_for_Modifier(itemCode);
	fun_Asign_ModifierGroup_TopSortingButtons(hm_ModifierGroup);
	isModifierSelect = true;
	if (hm_ModifierGroup.isEmpty())
	{
	    hm_ModifierDtl = fun_Get_Modifier_All(itemCode);
	    fun_setModifierName_to_Buttons(hm_ModifierDtl);
	}
	else
	{
	    fun_Fill_Modifier(btnItemSorting1.getText());
	}
    }

    private String fun_Get_FormattedName(String Name)
    {
	String name = Name;
	if (Name.contains(" ") && !Name.contains("<html>"))
	{
	    StringBuilder sb1 = new StringBuilder(Name);
	    int len = sb1.length();
	    int seq = sb1.lastIndexOf(" ");
	    String split = sb1.substring(0, seq);
	    String last = sb1.substring(seq + 1, len);
	    name = "<html>" + split + "<br>" + last + "</html>";
	}
	return name;
    }

    private void fun_setModifierName_to_Buttons(HashMap<String, frmDirectBiller.clsModifierDtl> hm_ModifierDtl)
    {
	JButton[] btnSubMenuArray =
	{
	    btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	int i = 0;
	fun_Reset_Item_Buttons();
	itemNames = new String[hm_ModifierDtl.size()];
	btnIItem1.setEnabled(true);
	btnIItem1.setText(fun_Get_FormattedName("Free Flow Modifier"));
	for (String modiName : hm_ModifierDtl.keySet())
	{
	    itemNames[i] = modiName;
	    if (i < 15)
	    {
		btnSubMenuArray[i].setEnabled(true);
		String tempName = fun_Get_FormattedName(modiName);
		btnSubMenuArray[i].setText(tempName);

		if (hm_ModifierDtl.get(modiName).isIsDefaultModifier())
		{
		    btnSubMenuArray[i].setBackground(new Color(255, 105, 180));//dark pink
		}
	    }
	    i++;
	}
	if (i > 15)
	{
	    btnNextItem.setEnabled(true);
	}
    }

    private void fun_Asign_ModifierGroup_TopSortingButtons(HashMap<String, frmDirectBiller.clsModifierGroupDtl> hm_ModifierGroup)
    {
	if (hm_ModifierGroup.size() > 0)
	{
	    listTopButtonName = new ArrayList<>();
	    for (String name : hm_ModifierGroup.keySet())
	    {
		listTopButtonName.add(name);
	    }
	}
	if (null != listTopButtonName && !listTopButtonName.isEmpty())
	{
	    funFillTopButtons();
	}
    }

    private void fun_Reset_Item_Buttons()
    {
	JButton[] btnWaiterArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	for (int k = 0; k < 16; k++)
	{
	    btnWaiterArray[k].setText("");
	    btnWaiterArray[k].setBackground(Color.LIGHT_GRAY);
	    btnWaiterArray[k].setIcon(null);
	    btnWaiterArray[k].setEnabled(false);
	}
	btnPrevItem.setEnabled(false);
	btnNextItem.setEnabled(false);
	if (isModifierSelect)
	{
	    obj_List_ItemPrice = null;
	}
    }

    private HashMap<String, frmDirectBiller.clsModifierGroupDtl> fun_fill_Top_Sorting_Buttons_for_Modifier(String itemCode)
    {
	HashMap<String, frmDirectBiller.clsModifierGroupDtl> hm_ModifierGroupDetail = null;
	try
	{
	    String modifierGroupName = null;
	    hm_ModifierGroupDetail = new HashMap<>();
	    String sql_select = "select a.strModifierGroupCode,a.strModifierGroupShortName,a.strApplyMaxItemLimit,"
		    + "a.intItemMaxLimit,a.strApplyMinItemLimit,a.intItemMinLimit  from tblmodifiergrouphd a,tblmodifiermaster b,tblitemmodofier c "
		    + "where a.strOperational='YES' and a.strModifierGroupCode=b.strModifierGroupCode and "
		    + "b.strModifierCode=c.strModifierCode and c.strItemCode='" + itemCode + "' group by a.strModifierGroupCode";
	    ResultSet rs_ModifierGroupDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_select);
	    while (rs_ModifierGroupDtl.next())
	    {
		modifierGroupName = rs_ModifierGroupDtl.getString(2);
		frmDirectBiller.clsModifierGroupDtl obj = new frmDirectBiller.clsModifierGroupDtl(rs_ModifierGroupDtl.getString(1), rs_ModifierGroupDtl.getString(2), rs_ModifierGroupDtl.getString(3), rs_ModifierGroupDtl.getInt(4), itemCode, rs_ModifierGroupDtl.getString(5), rs_ModifierGroupDtl.getInt(6));
		hm_ModifierGroupDetail.put(modifierGroupName, obj);
	    }
	    rs_ModifierGroupDtl.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return hm_ModifierGroupDetail;
    }

    private HashMap<String, frmDirectBiller.clsModifierDtl> fun_Get_Modifier_All(String itemCode)
    {
	HashMap<String, frmDirectBiller.clsModifierDtl> temp_hm_ModifierDtl = null;
	try
	{
	    temp_hm_ModifierDtl = new HashMap<>();
	    String sql_selectModifier = "select a.strModifierName,a.strModifierCode,b.dblRate,a.strModifierGroupCode,b.strDefaultModifier "
		    + " from tblmodifiermaster  a,"
		    + " tblitemmodofier b where a.strModifierCode=b.strModifierCode   "
		    + " and b.strItemCode='" + itemCode + "' group by a.strModifierCode;";
	    ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
	    while (rs_ModifierDtl.next())
	    {
		boolean isDefaultModifier = false;
		if (rs_ModifierDtl.getString(5).equalsIgnoreCase("Y"))
		{
		    isDefaultModifier = true;
		}
		frmDirectBiller.clsModifierDtl obj = new frmDirectBiller.clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), "NA", rs_ModifierDtl.getDouble(3), itemCode, "N", 0.00, "N", 0.00, isDefaultModifier);
		temp_hm_ModifierDtl.put(rs_ModifierDtl.getString(1), obj);
	    }
	    rs_ModifierDtl.close();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return temp_hm_ModifierDtl;
    }

    private void fun_Fill_Modifier(String modifierGroupName)
    {
	frmDirectBiller.clsModifierGroupDtl obj = hm_ModifierGroup.get(modifierGroupName);
	String itemCode = obj.getTemp_ItemCode();
	String groupCode = obj.getStrModifierGroupCode();
	hm_ModifierDtl = fun_Get_Modifier_GroupWise(itemCode, groupCode);
	fun_setModifierName_to_Buttons(hm_ModifierDtl);
    }

    private HashMap<String, frmDirectBiller.clsModifierDtl> fun_Get_Modifier_GroupWise(String itemCode, String groupCode)
    {
	HashMap<String, frmDirectBiller.clsModifierDtl> temp_hm_ModifierDtl = null;
	try
	{
	    temp_hm_ModifierDtl = new HashMap<>();
	    String sql_selectModifier = "select a.strModifierName,a.strModifierCode,b.dblRate,a.strModifierGroupCode,c.strApplyMaxItemLimit,c.intItemMaxLimit,c.strApplyMinItemLimit,c.intItemMinLimit,b.strDefaultModifier "
		    + " from tblmodifiermaster  a,"
		    + " tblitemmodofier b, tblmodifiergrouphd c where a.strModifierCode=b.strModifierCode and  "
		    + "a.strModifierGroupCode=c.strModifierGroupCode and a.strModifierGroupCode='" + groupCode + "' "
		    + "and b.strItemCode='" + itemCode + "' group by a.strModifierCode;";
	    ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
	    while (rs_ModifierDtl.next())
	    {
		boolean isDefaultModifier = false;
		if (rs_ModifierDtl.getString(9).equalsIgnoreCase("Y"))
		{
		    isDefaultModifier = true;
		}
		frmDirectBiller.clsModifierDtl obj = new frmDirectBiller.clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), rs_ModifierDtl.getString(4), rs_ModifierDtl.getDouble(3), itemCode, rs_ModifierDtl.getString(5), rs_ModifierDtl.getDouble(6), rs_ModifierDtl.getString(7), rs_ModifierDtl.getDouble(8), isDefaultModifier);
		temp_hm_ModifierDtl.put(rs_ModifierDtl.getString(1), obj);
	    }
	    rs_ModifierDtl.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	return temp_hm_ModifierDtl;
    }

    public String getCmsMemberCode()
    {
	return cmsMemberCode;
    }

    public void setCmsMemberCode(String cmsMemberCode)
    {
	this.cmsMemberCode = cmsMemberCode;
    }

    public String getCmsMemberName()
    {
	return cmsMemberName;
    }

    public void setCmsMemberName(String cmsMemberName)
    {
	this.cmsMemberName = cmsMemberName;
    }

    private double funGetItemWeight(String itemCode)
    {
	double itemWeight = 0.00;
	try
	{
	    String itemWeightSql = "select strItemWeight from tblitemmaster where strItemCode='" + itemCode + "' ";
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(itemWeightSql);
	    if (resultSet.next())
	    {
		itemWeight = Double.parseDouble(resultSet.getString("strItemWeight"));

		return itemWeight;
	    }
	}
	catch (SQLException e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}

	return itemWeight;
    }

    private void funResetTakeAway()
    {
	clsGlobalVarClass.gTakeAway = "No";

	dineInForTax = "Y";
	homeDeliveryForTax = "N";
	takeAwayForTax = "N";

	btnTakeAway.setForeground(Color.white);
	btnTakeAway.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
    }

    private void funResetHomeDeliveryButton()
    {
	btnHomeDelivery.setForeground(Color.white);
	dineInForTax = "Y";
	homeDeliveryForTax = "N";
	takeAwayForTax = "N";
    }

    private void funSetEnableDoneButton(boolean flag)
    {
	btnSettle.setEnabled(flag);
    }

    private void funExternalCodeFocusLost()
    {
	if (!txtExternalCode.getText().trim().isEmpty())
	{
	    funGetItemFromExtCode("");
	}
    }

    private void funEscKeyPressed(KeyEvent evt)
    {
	if (evt.getKeyCode() == 27)
	{
	    funHomeButtonPressed();
	}
	else if (evt.isAltDown() && evt.getKeyCode() == 67)//Alt+C
	{
	    try
	    {
		// TODO add your handling code here:
		funCustInfoBtnClicked();
	    }
	    catch (IOException ex)
	    {
		Logger.getLogger(frmDirectBiller.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    private class clsModifierGroupDtl
    {

	private final String strModifierGroupCode;
	private final String strModifierGroupShortName;
	private final String strApplyMaxItemLimit;
	private final int intItemMaxLimit;
	private final String temp_ItemCode;
	private final String strApplyMinItemLimit;
	private final int intItemMinLimit;

	clsModifierGroupDtl(String strModifierGroupCode, String strModifierGroupShortName, String strApplyMaxItemLimit, int intItemMaxLimit, String temp_ItemCode, String strApplyMinItemLimit, int intItemMinLimit)
	{
	    this.strModifierGroupCode = strModifierGroupCode;
	    this.strModifierGroupShortName = strModifierGroupShortName;
	    this.strApplyMaxItemLimit = strApplyMaxItemLimit;
	    this.intItemMaxLimit = intItemMaxLimit;
	    this.temp_ItemCode = temp_ItemCode;
	    this.strApplyMinItemLimit = strApplyMinItemLimit;
	    this.intItemMinLimit = intItemMinLimit;
	}

	/**
	 * @return the strModifierGroupCode
	 */
	public String getStrModifierGroupCode()
	{
	    return strModifierGroupCode;
	}

	/**
	 * @return the strModifierGroupShortName
	 */
	public String getStrModifierGroupShortName()
	{
	    return strModifierGroupShortName;
	}

	/**
	 * @return the strApplyMaxItemLimit
	 */
	public String getStrApplyItemLimit()
	{
	    return strApplyMaxItemLimit;
	}

	/**
	 * @return the intItemMaxLimit
	 */
	public int getIntItemMaxLimit()
	{
	    return intItemMaxLimit;
	}

	/**
	 * @return the temp_ItemCode
	 */
	public String getTemp_ItemCode()
	{
	    return temp_ItemCode;
	}

	/**
	 * @return the strApplyMinItemLimit
	 */
	public String getStrApplyMinItemLimit()
	{
	    return strApplyMinItemLimit;
	}

	/**
	 * @return the intItemMinLimit
	 */
	public int getIntItemMinLimit()
	{
	    return intItemMinLimit;
	}
    }

    private class clsModifierDtl
    {

	private final String modifierCode;
	private final String modifierName;
	private final String modifierGroupCode;
	private final double dblRate;
	private final String itemCode;
	private final String strApplyMaxItemLimit;
	private final double intItemMaxLimit;
	private final String strApplyMinItemLimit;
	private final double intItemMinLimit;
	private boolean isDefaultModifier;

	/**
	 * Ritesh 08 Nov 2014
	 *
	 * @param strModifierCode
	 * @param strModifierName
	 * @param strModifierGroupCode
	 * @param dblRate
	 * @param itemCode
	 * @param strApplyItemLimit
	 * @param intItemLimit
	 */
	clsModifierDtl(String strModifierCode, String strModifierName, String strModifierGroupCode, double dblRate, String itemCode,
		String strApplyMaxItemLimit, double intItemMaxLimit, String strApplyMinItemLimit, double intItemMinLimit, boolean flag)
	{
	    this.modifierCode = strModifierCode;
	    this.modifierName = strModifierName;
	    this.modifierGroupCode = strModifierGroupCode;
	    this.dblRate = dblRate;
	    this.itemCode = itemCode;
	    this.strApplyMaxItemLimit = strApplyMaxItemLimit;
	    this.intItemMaxLimit = intItemMaxLimit;
	    this.strApplyMinItemLimit = strApplyMinItemLimit;
	    this.intItemMinLimit = intItemMinLimit;
	    this.isDefaultModifier = flag;
	}

	/**
	 * @return the modifierCode
	 */
	public String getModifierCode()
	{
	    return modifierCode;
	}

	/**
	 * @return the modifierName
	 */
	public String getModifierName()
	{
	    return modifierName;
	}

	/**
	 * @return the modifierGroupCode
	 */
	public String getModifierGroupCode()
	{
	    return modifierGroupCode;
	}

	/**
	 * @return the dblRate
	 */
	public double getDblRate()
	{
	    return dblRate;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode()
	{
	    return itemCode;
	}

	/**
	 * @return the strApplyMaxItemLimit
	 */
	public String getStrApplyMaxItemLimit()
	{
	    return strApplyMaxItemLimit;
	}

	/**
	 * @return the intItemMaxLimit
	 */
	public double getIntItemMaxLimit()
	{
	    return intItemMaxLimit;
	}

	/**
	 * @return the strApplyMinItemLimit
	 */
	public String getStrApplyMinItemLimit()
	{
	    return strApplyMinItemLimit;
	}

	/**
	 * @return the intItemMinLimit
	 */
	public double getIntItemMinLimit()
	{
	    return intItemMinLimit;
	}

	public boolean isIsDefaultModifier()
	{
	    return isDefaultModifier;
	}
    }

    private void btnAgainstAdvOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgainstAdvOrderActionPerformed
	// TODO add your handling code here:
	funAgainstAdvOrderButtonPressed();
    }//GEN-LAST:event_btnAgainstAdvOrderActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
	funItemTablePressed("Mouse");
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void funDeleteButtonPressed()
    {
	try
	{
	    boolean flagFound = false;
	    int selectedRow = tblItemTable.getSelectedRow();
	    if (selectedRow == -1)
	    {
		new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
	    }
	    else
	    {
		int itemCodeIndex = 3;

		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    itemCodeIndex = 4;
		}
		else if (clsGlobalVarClass.gShowPurRateInDirectBiller)
		{
		    itemCodeIndex = 4;
		}
		String selectedItemCode = tblItemTable.getValueAt(selectedRow, itemCodeIndex).toString();
		boolean isSelectedModifier = false;
		if (selectedItemCode.contains("M"))
		{
		    isSelectedModifier = true;
		}

		int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to delete item?", "Item Delete", JOptionPane.YES_NO_OPTION);
		if (ch == JOptionPane.YES_OPTION)
		{

		    if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(selectedItemCode))
		    {
			List<clsDirectBillerItemDtl> subItemList = new ArrayList<>();
			for (clsDirectBillerItemDtl listItemDtl : obj_List_ItemDtl)
			{
			    if (listItemDtl.getTdh_ComboItemCode().trim().length() > 0 && selectedItemCode.equalsIgnoreCase(listItemDtl.getTdh_ComboItemCode()) && "Y".equalsIgnoreCase(listItemDtl.getTdhComboItemYN()))
			    {
				subItemList.add(listItemDtl);
			    }

			}

			if (subItemList.size() > 0)
			{
			    for (clsDirectBillerItemDtl listItemDtl : subItemList)
			    {
				obj_List_ItemDtl.remove(listItemDtl);
			    }
			}
			for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
			{

			    if (ob.getItemCode().equalsIgnoreCase(selectedItemCode))
			    {
				obj_List_ItemDtl.remove(ob);
				flagFound = true;
				break;
			    }

			}
		    }
		    else
		    {

			Iterator<clsDirectBillerItemDtl> it = obj_List_ItemDtl.iterator();
			while (it.hasNext())
			{
			    clsDirectBillerItemDtl deletObj = it.next();

			    if (isSelectedModifier)
			    {
				if (deletObj.getItemCode().equals(selectedItemCode) && deletObj.isIsModifier())
				{
				    it.remove();
				    flagFound = true;
				    break;
				}
			    }
			    else
			    {
				if (deletObj.getItemCode().substring(0, 7).equals(selectedItemCode))
				{
				    it.remove();
				    flagFound = true;
				}
			    }
			}

		    }
		    if (!flagFound)
		    {
			JOptionPane.showMessageDialog(this, "You can not delete item now");
		    }
		}
		if (flagFound)
		{
		    funRefreshItemTable();
		}

	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funUpButtonPressed()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int itemRow = selectedRow;
	    int rowcount = tblItemTable.getRowCount();
	    if (selectedRow == -1)
	    {
		itemRow = 0;
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 3).toString().trim();
		}
		selectedRow = 0;
		tblItemTable.changeSelection(selectedRow, 0, false, false);
	    }
	    else if (selectedRow == rowcount)
	    {
		itemRow = 0;
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 3).toString().trim();
		}
		selectedRow = 0;
		tblItemTable.changeSelection(selectedRow, 0, false, false);
	    }
	    else if (selectedRow < rowcount)
	    {
		itemRow = selectedRow - 1;
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 3).toString().trim();
		}
		tblItemTable.changeSelection(selectedRow - 1, 0, false, false);
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    flgChangeQty = false;
	}
    }

    private void funDownButtonPressed()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int itemRow = selectedRow;
	    int rowcount = tblItemTable.getRowCount();
	    if (selectedRow < rowcount)
	    {
		itemRow = selectedRow + 1;
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 3).toString().trim();
		}
		tblItemTable.changeSelection(selectedRow + 1, 0, false, false);
	    }
	    else if (selectedRow == rowcount)
	    {
		selectedRow = 0;
		itemRow = 0;
		if (clsGlobalVarClass.gShowItemStkColumnInDB)
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 4).toString().trim();
		}
		else
		{
		    temp_ItemCode = tblItemTable.getValueAt(itemRow, 3).toString().trim();
		}
		tblItemTable.changeSelection(selectedRow, 0, false, false);
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	    flgChangeQty = false;
	}
    }


    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
	// TODO add your handling code here:
	funUpButtonPressed();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnChangeQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeQtyActionPerformed
	// TODO add your handling code here:
	//flgChangeQty = true;
	//txtExternalCode.requestFocus();
	funItemTablePressed("KeyBoard");
    }//GEN-LAST:event_btnChangeQtyActionPerformed

    private void btnMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenu3MouseClicked
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[1]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu3MouseClicked

    private void btnMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu3ActionPerformed
	// TODO add your handling code here:
	funResetItemNames();
	funResetItemButtonText(menuNames1[1]);
	if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    funFillTopButtonList(menuHeadCode);
	}
    }//GEN-LAST:event_btnMenu3ActionPerformed

    private void btnMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu2ActionPerformed
	// TODO add your handling code here:
	funResetItemNames();
	funResetItemButtonText(menuNames1[0]);
	if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    funFillTopButtonList(menuHeadCode);
	}
	txtExternalCode.requestFocus();

    }//GEN-LAST:event_btnMenu2ActionPerformed

    private void btnMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenu4MouseClicked
	// TODO add your handling code here:

	funResetItemNames();
	funResetItemButtonText(menuNames1[2]);
	if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    funFillTopButtonList(menuHeadCode);
	}
    }//GEN-LAST:event_btnMenu4MouseClicked

    private void btnMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu4ActionPerformed
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[2]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu4ActionPerformed

    private void btnMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu6ActionPerformed
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[4]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu6ActionPerformed

    private void btnMenu8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu8ActionPerformed
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[6]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu8ActionPerformed

    private void btnMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu7ActionPerformed
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[5]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu7ActionPerformed

    private void btnPopularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopularActionPerformed
	// TODO add your handling code here:
	//call the popularItem function
	try
	{
	    funPopularItem();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnPopularActionPerformed

    private void btnMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu5ActionPerformed
	// TODO add your handling code here:
	try
	{
	    funResetItemNames();
	    funResetItemButtonText(menuNames1[3]);
	    if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu5ActionPerformed

    private void btnPrevMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevMenuActionPerformed
	// TODO add your handling code here:
	try
	{
	    int cntMenu = 0, cntMenu1 = 0;
	    btnNextMenu.setEnabled(true);
	    JButton[] btnMenuArray =
	    {
		btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
	    };
	    nextClick--;
	    if (nextClick == 0)
	    {
		btnPrevMenu.setEnabled(false);
	    }
	    nextCnt = nextClick * 7;
	    int limit = nextCnt + 7;
	    for (int cntMenu2 = 0; cntMenu2 < 7; cntMenu2++)
	    {
		btnMenuArray[cntMenu2].setText("");
		btnMenuArray[cntMenu2].setEnabled(true);
	    }
	    for (int cntMenu2 = nextCnt; cntMenu2 < limit; cntMenu2++)
	    {
		if (cntMenu2 == menuNames.length)
		{
		    break;
		}
		btnMenuArray[cntMenu1].setText(clsGlobalVarClass.convertString(menuNames[cntMenu2]));
		btnMenuArray[cntMenu1].setEnabled(true);
		menuNames1[cntMenu] = menuNames[cntMenu2];
		cntMenu1++;
		cntMenu++;
	    }
	    for (int cntMenu2 = cntMenu1; cntMenu1 < 7; cntMenu1++)
	    {
		btnMenuArray[cntMenu1].setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	txtExternalCode.requestFocus();
    }//GEN-LAST:event_btnPrevMenuActionPerformed

    private void funAddModifierToDB(String modiname)
    {
	if (null != obj_List_ItemDtl && obj_List_ItemDtl.size() > 0)
	{

	    int rowIndex = tblItemTable.getSelectedRow();
	    String strSeq = "";
	    if (rowIndex > -1)
	    {
		strSeq = tblItemTable.getValueAt(rowIndex, 4).toString();
	    }

	    if ("Free Flow Modifier".equalsIgnoreCase(modiname))
	    {
		new frmAlfaNumericKeyBoard(null, true, "1", "Enter Name ", true).setVisible(true);
		String name = clsGlobalVarClass.gKeyboardValue;
		if (name.trim().length() > 0)
		{
		    name = name.toUpperCase();
		    if (name.length() > 100)
		    {
			name = name.substring(0, 100);
		    }

		    new frmNumericKeyboard(this, true, "", "Double", "Enter Amount").setVisible(true);
		    String price = clsGlobalVarClass.gNumerickeyboardValue;
		    clsGlobalVarClass.gNumerickeyboardValue = "";
		    if (name.trim().length() > 0 && price.trim().length() > 0)
		    {
			for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
			{
			    if (ob.getItemCode().equalsIgnoreCase(temp_ItemCode) && ob.isIsModifier() == false)
			    {
				if (strSeq.isEmpty())
				{
				    strSeq = ob.getSeqNo();
				}
				clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl("-->" + name, temp_ItemCode.concat("M99"), 1.00, Double.parseDouble(price), true, "M99", "", "", Double.parseDouble(price), "N", (strSeq.concat(".01")), 0);
				obj_List_ItemDtl.add(obj_row);
				break;
			    }
			}
			funRefreshItemTable();
		    }
		}
	    }
	    else
	    {
		frmDirectBiller.clsModifierDtl objModiDtl = hm_ModifierDtl.get(modiname);
		String temp_itemCode = objModiDtl.getItemCode();

		double rate = objModiDtl.getDblRate();
		double qty = 1;
		double amt = rate * qty;
		String ModifierCode = objModiDtl.getModifierCode();
		String ModifierName = objModiDtl.getModifierName();
		String modifierGroup = objModiDtl.getModifierGroupCode();
		String applyMaxLimit = objModiDtl.getStrApplyMaxItemLimit();
		double maxLimit = objModiDtl.getIntItemMaxLimit();
		double currentModifierLimit = funGetTotalModifierWithSubGroup(temp_itemCode, modifierGroup);
		for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
		{
		    boolean found = false;
		    boolean flagMaxLimitexceed = false;
		    if (ob.getItemCode().equalsIgnoreCase(temp_itemCode) && ob.isIsModifier() == false)
		    {
			if (strSeq.isEmpty())
			{
			    strSeq = ob.getSeqNo();
			}
			if (currentModifierLimit == 0.00)
			{
			    clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(ModifierName, temp_itemCode.concat(ModifierCode),
				    qty, amt, true, ModifierCode, "", "", rate, "N", strSeq.concat(".01"), 0);
			    obj_List_ItemDtl.add(obj_row);
			    found = true;
			}
			else if ("Y".equalsIgnoreCase(applyMaxLimit))
			{
			    if (isModifierPresent(temp_itemCode, modifierGroup))
			    {
				if (currentModifierLimit < maxLimit)
				{
				    clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(ModifierName, temp_itemCode.concat(ModifierCode),
					    qty, amt, true, ModifierCode, "", "", rate, "N", strSeq.concat(".01"), 0);
				    obj_List_ItemDtl.add(obj_row);
				    found = true;
				}
				else
				{
				    flagMaxLimitexceed = true;
				}
			    }
			}
			else if (!found)
			{
			    clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(ModifierName, temp_itemCode.concat(ModifierCode),
				    qty, amt, true, ModifierCode, "", "", rate, "N", strSeq.concat(".01"), 0);
			    obj_List_ItemDtl.add(obj_row);
			    found = true;
			}
		    }
		    if (flagMaxLimitexceed)
		    {
			JOptionPane.showMessageDialog(this, "Max Limit of Modifier Exceed with this Group");
			break;
		    }
		    if (found)
		    {
			funRefreshItemTable();
			break;
		    }
		}
	    }
	}
	selectedQty = 1;
    }

    private boolean isModifierPresent(String temp_itemCode, String modifierGroup)
    {
	boolean flag = false;
	for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
	{
	    String temItemCode = ob.getItemCode();
	    temItemCode = temItemCode.substring(0, 7);
	    if (temp_itemCode.equalsIgnoreCase(temItemCode) && ob.isIsModifier() == true && ob.getModifierCode().equalsIgnoreCase(modifierGroup))
	    {
		flag = true;
	    }
	    if (flag)
	    {
		break;
	    }
	}
	return flag;
    }

    private double funGetTotalModifierWithSubGroup(String temp_itemCode, String modifierGroup)
    {
	double totalModifier = 0.00;
	for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
	{
	    String temItemCode = ob.getItemCode();
	    temItemCode = temItemCode.substring(0, 7);
	    if (temp_itemCode.equalsIgnoreCase(temItemCode) && ob.isIsModifier() == true
		    && ob.getModifierCode().equalsIgnoreCase(modifierGroup))
	    {
		totalModifier++;
	    }
	}
	return totalModifier;
    }

    public void funMenuItemSelection(String btnValue, int btnIndex)
    {
	if (isModifierSelect)
	{
	    funAddModifierToDB(convertString(btnValue));
	}
	else
	{
	    String itemName = "";
	    itemName = convertString(btnValue);
	    funGetPrice(itemName);
	}
    }

    private void funGetCMSMemberCode() throws Exception
    {
	new frmAlfaNumericKeyBoard(null, true, "1", "Enter Member Code").setVisible(true);
	String strCustomerCode = clsGlobalVarClass.gKeyboardValue;
	if (strCustomerCode.trim().length() > 0)
	{
	    String memberInfo = objUtility.funCheckMemeberBalance(strCustomerCode);
	    if (memberInfo.contains("#"))
	    {
		if (memberInfo.split("#")[4].trim().equals("Y"))
		{
		    JOptionPane.showMessageDialog(this, "Member is blocked");
		    return;
		}
		this.setCmsMemberCode(memberInfo.split("#")[0]);
		this.setCmsMemberName(memberInfo.split("#")[1]);
		double creditLimit = Double.parseDouble(memberInfo.split("#")[2]);
		lblCustInfo.setText("<html>" + memberInfo.split("#")[1] + "</html>");

		double totalAmt = 0;
		if (txtTotal.getText().trim().length() > 0)
		{
		    totalAmt = Double.parseDouble(txtTotal.getText());
		}
		if (creditLimit < totalAmt)
		{
		    JOptionPane.showMessageDialog(this, "Credit Limit is " + creditLimit);
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Member Not Found!!!");
	    }
	}
    }

    private void funCustInfoBtnClicked() throws IOException
    {
//	if (clsGlobalVarClass.gCMSIntegrationYN)
//	{
//	    try
//	    {
//		funGetCMSMemberCode();
//	    }
//	    catch (Exception ex)
//	    {
//		objUtility.funShowDBConnectionLostErrorMessage(ex);	
//		ex.printStackTrace();
//	    }
//	}
//	else
	    if (clsGlobalVarClass.gSelectCustomerCodeFromCardSwipe)
	{

	    if (clsGlobalVarClass.gEnableNFCInterface)
	    {
		Thread objThread = null;

		ReaderThread objReader = new ReaderThread();
		objThread = new Thread(objReader);
		objThread.start();

		funSwipeDebitCard();

		clsGlobalVarClass.gDebitCardNo = null;
		if (null != objThread)
		{
		    objThread.stop();
		    objThread = null;
		}
	    }
	    else
	    {
		funSwipeDebitCard();
	    }
	}
	else
	{
	    if (clsGlobalVarClass.gCRMInterface.equals("SQY"))
	    {
		new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
		if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
		{
		    funCallWebService();
		}
	    }
	    if (clsGlobalVarClass.gCRMInterface.equals("PMAM"))
	    {
		new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
		if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
		{
		    clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
		    funSetMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
		}
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
		if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
		{
		    if (clsGlobalVarClass.gNumerickeyboardValue.matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$") || clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{8}") || clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{9}") || clsGlobalVarClass.gNumerickeyboardValue.matches("\\d{10}"))//\\d{10}
		    {
			clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
			funSetMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
		    }
		    else
		    {
			JOptionPane.showMessageDialog(null, "Please Enter Valid Mobile No.");
			return;
		    }
		}
	    }
	}
    }

    private void funSettleButtonClicked()
    {
	try
	{

	    String billTransType = "";

	    funSetEnableDoneButton(false);
	    funPLUClosedButtonPressed();
	    if (clsGlobalVarClass.gDebitCardPayment.equals("Yes"))
	    {
		if (clsGlobalVarClass.gCheckDebitCardBalanceOnTrans)
		{
		    if (Double.parseDouble(txtTotal.getText()) > debitCardBalance)
		    {
			JOptionPane.showMessageDialog(null, "Insufficient Balance on Card!!!");
			funSetEnableDoneButton(true);
			return;
		    }
		}
	    }

	    if (homeDeliveryForTax.equalsIgnoreCase("Y"))
	    {
		if (clsGlobalVarClass.gCustomerCode.trim().isEmpty())
		{
		    JOptionPane.showMessageDialog(null, "Select Customer for Home Delivery!!!");
		    funSetEnableDoneButton(true);
		    return;
		}
	    }

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (clsGlobalVarClass.gCMSMemberCodeForKOTJPOS.equals("Y"))
		{
		if (!clsGlobalVarClass.gClientCode.equals("074.001"))
		{
		    if (cmsMemberCode.trim().length() == 0)
		    {
			JOptionPane.showMessageDialog(this, "Please Select Member!!!");
			funSetEnableDoneButton(true);
			return;
		    }
		}
		}
	    }
	    else if (!clsGlobalVarClass.gCustomerCode.trim().isEmpty() && homeDeliveryForTax.equals("Y"))
	    {
		billTransType = "Home Delivery";
		double totalBillAmount = 0.00;
		if (txtTotal.getText().trim().length() > 0)
		{
		    totalBillAmount = Double.parseDouble(txtTotal.getText());
		}
		if (!clsGlobalVarClass.gNewCustomerForHomeDel)
		{
		    objUtility.funGetDeliveryCharges(clsGlobalVarClass.gBuildingCodeForHD, totalBillAmount - homeDelCharges, clsGlobalVarClass.gCustomerCode);
		}
		if (clsGlobalVarClass.gDelBoyCompulsoryOnDirectBiller)
		{
		    if (clsGlobalVarClass.gDeliveryBoyCode == null)
		    {
			JOptionPane.showMessageDialog(this, "Please Assign Delivery Boy");
			funSetEnableDoneButton(true);
			return;
		    }
		}
	    }
	    else if (clsGlobalVarClass.gRemarksOnTakeAway)
	    {
		if (clsGlobalVarClass.gTakeAway.equals("Yes") && (clsGlobalVarClass.gCustomerCode.trim().isEmpty() || clsGlobalVarClass.gCustomerCode.trim().length() == 0))
		{
		    JOptionPane.showMessageDialog(null, "Select Customer For Take Away!!!");
		    funSetEnableDoneButton(true);
		    return;
		}
	    }

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && this.objCustomerRewards != null && this.objCustomerRewards.getStrRewardId().trim().length() > 0)
	    {
		String reasonCode = objUtility2.funGetDefaultReasonCode("strHashTagLoyalty");
		if (reasonCode.trim().length() <= 0)
		{
		    JOptionPane.showMessageDialog(this, "No Hash Tag reasons are created.");
		    funSetEnableDoneButton(true);
		    return;
		}
	    }

	    if (tblItemTable.getRowCount() == 0)
	    {
		new frmOkPopUp(null, "", "Please Select Item", 1).setVisible(true);
		funSetEnableDoneButton(true);
	    }
	    else if ("".equals(lblVoucherNo.getText()))
	    {
		if (objPanelModifier == null)
		{
		}
		else
		{
		    objPanelModifier.setVisible(false);
		    funShowPanel();
		}
		if (flgBillForAdvOrder)
		{
		    dispose();
		    flgBillForAdvOrder = false;
		    advOrderCustCode = "";
		}

		boolean isUserGranted = false;
		if (clsGlobalVarClass.gSuperUser)
		{
		    isUserGranted = true;
		}
		else
		{
		    String formName = "Direct Biller";
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
			else
			{
			    isUserGranted = false;
			}
			if (isValidUser.equalsIgnoreCase("Valid User"))
			{
			    isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
			    if (isUserGranted)
			    {
				isUserGranted = true;
			    }
			    else
			    {
				new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
				isUserGranted = false;
			    }
			}
			else
			{
			    new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
			    isUserGranted = false;
			}
		    }
		    else
		    {
			isUserGranted = true;
		    }
		}
		if (isUserGranted)
		{
		    new frmBillSettlement(this, billTransType).setVisible(true);
		}
		else
		{
		    funSetEnableDoneButton(true);
		}

		// new frmBillSettlement(this, billTransType).setVisible(true);
	    }
	    funResetSortingButtons();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funSetSelectedQty(String buttonName)
    {
	try
	{
	    funDisableSelectedQtyBtn(buttonName);
	    if ("".equals(lblVoucherNo.getText()))
	    {
		selectedQty = Double.parseDouble(buttonName);
	    }
	    if (flgChangeQty == true)
	    {
		funChangeQtyOfItem(buttonName);
		flgChangeQty = false;
	    }
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funPreviousItemButtonClicked()
    {
	try
	{
	    btnNextItem.setEnabled(true);
	    JButton[] btnItemArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    nextItemClick--;
	    if (nextItemClick == 0)
	    {
		btnPrevItem.setEnabled(false);
	    }
	    if (flgPopular)
	    {
		funLoadPopularItems(btnItemArray);
	    }
	    else
	    {
		int cntItem = 0;
		nextCnt = nextItemClick * 16;
		int limit = nextCnt + 16;
		for (int cntItem1 = 0; cntItem1 < 16; cntItem1++)
		{
		    btnItemArray[cntItem1].setText("");
		    btnItemArray[cntItem1].setForeground(Color.black);
		    btnItemArray[cntItem1].setBackground(Color.lightGray);
		    btnItemArray[cntItem1].setIcon(null);
		}

		for (int cntItem1 = nextCnt; cntItem1 < limit; cntItem1++)
		{
		    if (cntItem1 == itemNames.length)
		    {
			break;
		    }

		    btnItemArray[cntItem].setText(fun_Get_FormattedName(itemNames[cntItem1]));
		    if (null != hm_ModifierDtl)
		    {
			if (hm_ModifierDtl.get(itemNames[cntItem1]).isIsDefaultModifier())
			{
			    btnItemArray[cntItem].setBackground(new Color(255, 105, 180));//dark pink
			}
		    }
		    if (!isModifierSelect)
		    {
			btnItemArray[cntItem].setIcon(funGetImageIcon(itemImageCode.get(cntItem1)));
			if (btnforeground[cntItem1].equals("Red"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.red);
			}
			else if (btnforeground[cntItem1].equals("Green"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.GREEN);
			}
			else if (btnforeground[cntItem1].equals("Black"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.lightGray);
			}
			else if (btnforeground[cntItem1].equals("CYAN"))
			{//
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.CYAN);
			}
			else if (btnforeground[cntItem1].equals("MAGENTA"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.MAGENTA);
			}
			else if (btnforeground[cntItem1].equals("ORANGE"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.ORANGE);
			}
			else if (btnforeground[cntItem1].equals("PINK"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.PINK);
			}
			else if (btnforeground[cntItem1].equals("YELLOW"))
			{
			    btnItemArray[cntItem].setForeground(Color.BLACK);
			    btnItemArray[cntItem].setBackground(Color.YELLOW);
			}
			else if (btnforeground[cntItem1].equals("WHITE"))
			{
			    btnItemArray[cntItem].setForeground(Color.WHITE);
			    btnItemArray[cntItem].setBackground(Color.BLUE);
			}
		    }
		    cntItem++;
		}

		//int startLimit = itemNames.length - 16;
		int startLimit = nextCnt;
		for (int cntItem1 = startLimit; cntItem1 < limit; cntItem1++)
		{
		    btnItemArray[cntItem1].setEnabled(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funNextItemButtonClicked()
    {
	try
	{
	    btnPrevItem.setEnabled(true);
	    JButton[] btnItemArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    nextItemClick++;

	    if (flgPopular)
	    {
		funLoadPopularItems(btnItemArray);
	    }
	    else
	    {
		//int itemDiv = itemNames.length / 17;
		int itemDiv = itemImageCode.size() / 17;
		if (itemDiv == nextItemClick)
		{
		    btnNextItem.setEnabled(false);
		}
		int k = 0;
		nextCnt = nextItemClick * 16;
		int limit = nextCnt + 16;
		for (int m1 = 0; m1 < 16; m1++)
		{
		    btnItemArray[m1].setText("");
		    btnItemArray[m1].setForeground(Color.black);
		    btnItemArray[m1].setBackground(Color.lightGray);
		    btnItemArray[m1].setIcon(null);
		    btnItemArray[m1].setEnabled(false);
		}
		if (limit > itemNames.length)
		{
		    limit = itemNames.length;
		    btnNextItem.setEnabled(false);
		}
		for (int j = nextCnt; j < limit; j++)
		{
		    /*
                     * if (j == itemNames.length) { break; }
		     */
		    if (j == itemImageCode.size())
		    {
			break;
		    }

		    btnItemArray[k].setText(fun_Get_FormattedName(itemNames[j]));
		    btnItemArray[k].setEnabled(true);
		    if (null != hm_ModifierDtl)
		    {
			if (hm_ModifierDtl.get(itemNames[j]).isIsDefaultModifier())
			{
			    btnItemArray[k].setBackground(new Color(255, 105, 180));//dark pink
			}
		    }

		    if (!isModifierSelect)
		    {
			btnItemArray[k].setIcon(funGetImageIcon(itemImageCode.get(j)));
			if (btnforeground[j].equals("Red"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.red);
			}
			else if (btnforeground[j].equals("Green"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.GREEN);
			}
			else if (btnforeground[j].equals("Black"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.lightGray);
			}
			else if (btnforeground[j].equals("CYAN"))
			{//
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.CYAN);
			}
			else if (btnforeground[j].equals("MAGENTA"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.MAGENTA);
			}
			else if (btnforeground[j].equals("ORANGE"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.ORANGE);
			}
			else if (btnforeground[j].equals("PINK"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.PINK);
			}
			else if (btnforeground[j].equals("YELLOW"))
			{
			    btnItemArray[k].setForeground(Color.BLACK);
			    btnItemArray[k].setBackground(Color.YELLOW);
			}
			else if (btnforeground[j].equals("WHITE"))
			{
			    btnItemArray[k].setForeground(Color.WHITE);
			    btnItemArray[k].setBackground(Color.BLUE);
			}
		    }
		    k++;
		}
		//int startLimit = itemNames.length - 16;
		int startLimit = itemImageCode.size() - 16;
		for (int j = startLimit; j < 16; j++)
		{
		    btnItemArray[j].setEnabled(false);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    /*
     * private void funPreviousItemButtonClicked() { try {
     * btnNextItem.setEnabled(true); JButton[] btnItemArray = {btnIItem1,
     * btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7,
     * btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13,
     * btnIItem14, btnIItem15, btnIItem16}; nextItemClick--; if (nextItemClick
     * == 0) { btnPrevItem.setEnabled(false); } int k = 0; nextCnt =
     * nextItemClick * 16; int limit = nextCnt + 16; for (int m = 0; m < 16;
     * m++) { btnItemArray[m].setText("");
     * btnItemArray[m].setForeground(Color.black);
     * btnItemArray[m].setBackground(Color.lightGray);
     * btnItemArray[m].setIcon(null); }
     *
     * for (int j = nextCnt; j < limit; j++) { if (j == itemNames.length) {
     * break; }
     *
     * btnItemArray[k].setText(itemNames[j]);
     * btnItemArray[k].setIcon(funGetImageIcon(itemImageCode[j])); if
     * (btnforeground[j].equals("Red")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.red); } else if
     * (btnforeground[j].equals("Green")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.GREEN); } else if
     * (btnforeground[j].equals("Black")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.lightGray); } else if
     * (btnforeground[j].equals("CYAN")) {//
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.CYAN); } else if
     * (btnforeground[j].equals("MAGENTA")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.MAGENTA); } else if
     * (btnforeground[j].equals("ORANGE")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.ORANGE); } else if
     * (btnforeground[j].equals("PINK")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.PINK); } else if
     * (btnforeground[j].equals("YELLOW")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.YELLOW); } else if
     * (btnforeground[j].equals("WHITE")) {
     * btnItemArray[k].setForeground(Color.WHITE);
     * btnItemArray[k].setBackground(Color.BLUE); }
     *
     * k++; } int startLimit = itemNames.length - 16; for (int j = startLimit; j
     * < 16; j++) { btnItemArray[j].setEnabled(true); }
     *
     * } catch (Exception e) { e.printStackTrace(); } } * private void
     * funNextItemButtonClicked() { try { btnPrevItem.setEnabled(true);
     * JButton[] btnItemArray = {btnIItem1, btnIItem2, btnIItem3, btnIItem4,
     * btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10,
     * btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16};
     * nextItemClick++; int itemDiv = itemNames.length / 17; if (itemDiv ==
     * nextItemClick) { btnNextItem.setEnabled(false); } int k = 0; nextCnt =
     * nextItemClick * 16; int limit = nextCnt + 16; for (int m1 = 0; m1 < 16;
     * m1++) { btnItemArray[m1].setText("");
     * btnItemArray[m1].setForeground(Color.black);
     * btnItemArray[m1].setBackground(Color.lightGray);
     * btnItemArray[m1].setIcon(null); }
     *
     * for (int j = nextCnt; j < limit; j++) { if (j == itemNames.length) {
     * break; }
     *
     * btnItemArray[k].setText(itemNames[j]);
     * btnItemArray[k].setIcon(funGetImageIcon(itemImageCode.get[j])); if
     * (btnforeground[j].equals("Red")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.red); } else if
     * (btnforeground[j].equals("Green")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.GREEN); } else if
     * (btnforeground[j].equals("Black")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.lightGray); } else if
     * (btnforeground[j].equals("CYAN")) {//
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.CYAN); } else if
     * (btnforeground[j].equals("MAGENTA")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.MAGENTA); } else if
     * (btnforeground[j].equals("ORANGE")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.ORANGE); } else if
     * (btnforeground[j].equals("PINK")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.PINK); } else if
     * (btnforeground[j].equals("YELLOW")) {
     * btnItemArray[k].setForeground(Color.BLACK);
     * btnItemArray[k].setBackground(Color.YELLOW); } else if
     * (btnforeground[j].equals("WHITE")) {
     * btnItemArray[k].setForeground(Color.WHITE);
     * btnItemArray[k].setBackground(Color.BLUE); } k++; }
     *
     * int startLimit = itemNames.length - 16; for (int j = startLimit; j < 16;
     * j++) { btnItemArray[j].setEnabled(false); }
     *
     * } catch (Exception e) { e.printStackTrace(); } }
     */
    private void funNextMenuButtonClicked()
    {
	try
	{
	    int cntMenu = 0, cntMenu1 = 0;
	    btnPrevMenu.setEnabled(true);
	    JButton[] btnMenuArray =
	    {
		btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
	    };
	    nextClick++;
	    int div = menuNames.length / 7;
	    if (nextClick == div)
	    {
		btnNextMenu.setEnabled(false);
	    }

	    nextCnt = nextClick * 7;
	    int limit = nextCnt + 7;
	    for (int m = 0; m < 7; m++)
	    {
		btnMenuArray[m].setText("");
	    }
	    for (int cntMenu2 = nextCnt; cntMenu2 < limit; cntMenu2++)
	    {
		if (cntMenu2 == menuNames.length)
		{
		    break;
		}
		btnMenuArray[cntMenu1].setText(clsGlobalVarClass.convertString(menuNames[cntMenu2]));
		menuNames1[cntMenu] = menuNames[cntMenu2];
		cntMenu1++;
		cntMenu++;
	    }
	    for (int cntMenu2 = cntMenu1; cntMenu2 < 7; cntMenu2++)
	    {
		btnMenuArray[cntMenu2].setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	txtExternalCode.requestFocus();
    }

    private void funPreviousItemSortingButtonClicked()
    {
	try
	{
	    if (btnPrevItemSorting.isEnabled())
	    {
		JButton btnArray[] =
		{
		    btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
		};
		funResetSortingButtons();

		if (totalItems > 4)
		{
		    int x = totalItems - itemNumber;
		    itemNumber = x;
		    for (int i = 0; i < 4; i++, itemNumber++)
		    {
			btnArray[i].setText(listTopButtonName.get(itemNumber));
			btnArray[i].setEnabled(true);
		    }
		}

		btnNextItemSorting.setEnabled(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	txtExternalCode.requestFocus();
    }

    private void funNextItemSortingButtonClicked()
    {
	try
	{
	    if (btnNextItemSorting.isEnabled())
	    {
		JButton btnArray[] =
		{
		    btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
		};
		funResetSortingButtons();
		int x = totalItems - itemNumber;
		if (x > 4)
		{
		    for (int i = 0; i < 4; i++, itemNumber++)
		    {

			btnArray[i].setText(listTopButtonName.get(itemNumber));
			btnArray[i].setEnabled(true);
		    }
		}
		else
		{
		    for (int i = 0; i < x; i++, itemNumber++)
		    {
			btnArray[i].setText(listTopButtonName.get(itemNumber));
			btnArray[i].setEnabled(true);
		    }
		}
		if (x > 4)
		{
		    btnNextItemSorting.setEnabled(true);
		}
		btnPrevItemSorting.setEnabled(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	txtExternalCode.requestFocus();
    }

    public void funSetAdvOrderNo(String advOrderNo)
    {
	try
	{
	    clsGlobalVarClass.gAdvOrderNoForBilling = advOrderNo;

	    homeDelCharges = 0;
	    sql = "select a.strCustomerCode,a.strCustomerName,a.longMobileNo,b.strHomeDelivery,dblHomeDelCharges "
		    + "from tblcustomermaster a, tbladvbookbillhd b "
		    + "where a.strCustomerCode=b.strCustomerCode  and b.strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "' ";
	    ResultSet rsAdvOrderCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsAdvOrderCustomer.next())
	    {
		lblCustInfo.setText(rsAdvOrderCustomer.getString(2) + "," + rsAdvOrderCustomer.getString(3));
		homeDelCharges = rsAdvOrderCustomer.getDouble(5);
		homeDeliveryForTax = rsAdvOrderCustomer.getString(4);
//                if (rsAdvOrderCustomer.getString(4).equals("Y"))
//                {
		sql = "select longMobileNo,strBuldingCode from tblcustomermaster "
			+ "where strCustomerCode='" + rsAdvOrderCustomer.getString(1) + "'";
		ResultSet rsCustArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsCustArea.next())
		{
		    clsGlobalVarClass.gCustomerCode = rsAdvOrderCustomer.getString(1);
		    clsGlobalVarClass.gCustMBNo = rsCustArea.getString(1);
		    clsGlobalVarClass.gBuildingCodeForHD = rsCustArea.getString(2);
		}
		rsCustArea.close();
//                }
	    }
	    rsAdvOrderCustomer.close();
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
	    sql = "select strItemCode,strItemName,dblQuantity,dblAmount,dteAdvBookingDate "
		    + " from tbladvbookbilldtl "
		    + " where strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'";
	    ResultSet rsAdvOrderForItem = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    obj_List_ItemDtl.clear();
	    while (rsAdvOrderForItem.next())
	    {
		String ItemCode = rsAdvOrderForItem.getString(1);
		String itemname = rsAdvOrderForItem.getString(2);
		String Qty = rsAdvOrderForItem.getString(3);
		String amount = rsAdvOrderForItem.getString(4);
		double dblPrice = Double.parseDouble(amount);
		double dblQty = Double.parseDouble(Qty);
		double rate = dblPrice / dblQty;
		clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(itemname, ItemCode, dblQty, dblPrice, false, "", "N", "", rate, "", getSeqNo(), 0);
		serNo++;
		obj_List_ItemDtl.add(ob);
	    }
	    rsAdvOrderForItem.close();

	    sql = "select strItemCode,strModifierCode,strModifierName,dblQuantity"
		    + ",dblAmount from tbladvordermodifierdtl "
		    + "where strAdvOrderNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'";
	    ResultSet rsAdvOrderForModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsAdvOrderForModifier.next())
	    {
		String ItemCode = rsAdvOrderForModifier.getString(1);
		String modifierCode = rsAdvOrderForModifier.getString(2);
		String modifierName = rsAdvOrderForModifier.getString(3);
		double Qty = Double.parseDouble(rsAdvOrderForModifier.getString(4));
		double amount = Double.parseDouble(rsAdvOrderForModifier.getString(5));
		double rate = amount / Qty;
		clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(modifierName, ItemCode + "" + modifierCode, Qty, amount, true, modifierCode, "N", "", rate, "", getSeqNo(), 0);
		obj_List_ItemDtl.add(funGetIndexOfItem(ItemCode), ob);
	    }
	    rsAdvOrderForModifier.close();
	    funRefreshItemTable();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funSetMobileNo(String mbNo)
    {
	try
	{
	    double totalBillAmount = 0.00;
	    if (txtTotal.getText().trim().length() > 0)
	    {
		totalBillAmount = Double.parseDouble(txtTotal.getText());
	    }
	    if (clsGlobalVarClass.gCustAddressSelectionForBill)
	    {
		if (mbNo.trim().length() == 0) // Open Customer Master Search
		{
		    objUtility.funCallForSearchForm("CustomerMaster");
		    new frmSearchFormDialog(this, true).setVisible(true);
		    if (clsGlobalVarClass.gSearchItemClicked)
		    {
			Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			//clsGlobalVarClass.gCustomerCodeForHomeDelivery = data[0].toString();
			clsGlobalVarClass.gCustomerCode = data[0].toString();
			clsGlobalVarClass.gBuildingCodeForHD = data[4].toString();
			clsGlobalVarClass.gSearchItemClicked = false;
			lblCustInfo.setText("<html>" + data[1].toString() + "</html>");

			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}

		    }
		}
		else // Get Cust Info Based On Entered Mobile No
		{
		    sql = "select count(strCustomerCode),strCustomerCode,strCustomerName,strBuldingCode"
			    + " from tblcustomermaster where longMobileNo like '%" + mbNo + "%' ";
		    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    rsCustomer.next();
		    int found = rsCustomer.getInt(1);
		    if (found > 0)
		    {
			//clsGlobalVarClass.gCustomerCodeForHomeDelivery = rsCustomer.getString(1);
			clsGlobalVarClass.gCustomerCode = rsCustomer.getString(2);
			lblCustInfo.setText(rsCustomer.getString(3));
			clsGlobalVarClass.gCustMBNo = mbNo;

			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		    else
		    {
			clsGlobalVarClass.gNewCustomerForHomeDel = true;
			clsGlobalVarClass.gTotalBillAmount = totalBillAmount;
			clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(mbNo);
			//new frmCustMasterRMS1(this,true).setVisible(true);
			new frmCustomerMaster().setVisible(true);
			lblCustInfo.setText(clsGlobalVarClass.gCustomerName);

			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		    rsCustomer.close();
		}
	    }
	    else
	    {
		if (mbNo.trim().length() == 0) // Open Customer Master Search
		{
		    objUtility.funCallForSearchForm("CustomerMaster");
		    new frmSearchFormDialog(this, true).setVisible(true);
		    if (clsGlobalVarClass.gSearchItemClicked)
		    {
			Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			//clsGlobalVarClass.gCustomerCodeForHomeDelivery = data[0].toString();
			clsGlobalVarClass.gCustomerCode = data[0].toString();
			clsGlobalVarClass.gBuildingCodeForHD = data[4].toString();
			clsGlobalVarClass.gSearchItemClicked = false;
			lblCustInfo.setText("<html>" + data[1].toString() + "</html>");
			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		}
		else // Get Cust Info Based On Entered Mobile No
		{
		    sql = "select count(strCustomerCode),strCustomerCode,strCustomerName,strBuldingCode"
			    + " from tblcustomermaster where longMobileNo like '%" + mbNo + "%' ";
		    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    rsCustomer.next();
		    int found = rsCustomer.getInt(1);
		    if (found > 1)
		    {
			//clsGlobalVarClass.gCustomerCodeForHomeDelivery = rsCustomer.getString(1);
			clsGlobalVarClass.gCustomerCode = rsCustomer.getString(2);
			lblCustInfo.setText(rsCustomer.getString(3));
			clsGlobalVarClass.gCustMBNo = mbNo;
			clsGlobalVarClass.gSearchItem = mbNo;
			objUtility.funCallForSearchForm("CustomerAddress");
			new frmSearchFormDialog(this, true).setVisible(true);
			if (clsGlobalVarClass.gSearchItemClicked)
			{
			    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			    //clsGlobalVarClass.gCustomerCodeForHomeDelivery = data[0].toString();

			    clsGlobalVarClass.gCustomerCode = data[0].toString();
			    clsGlobalVarClass.gCustomerAddress1 = data[2].toString();
			    clsGlobalVarClass.gCustomerAddress2 = data[3].toString();
			    clsGlobalVarClass.gCustomerCity = data[4].toString();
			    clsGlobalVarClass.gBuildingCodeForHD = data[5].toString();

			    //clsGlobalVarClass.funGetDeliveryCharges(data[6].toString(),totalBillAmount);
			    clsGlobalVarClass.gSearchItemClicked = false;
			    lblCustInfo.setText("<html>" + data[1].toString() + "</html>");

			    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			    {
				funShowRewardsButtonClicked();
			    }
			}
		    }
		    else if (found == 1)
		    {
			clsGlobalVarClass.gCustomerCode = rsCustomer.getString(2);
			clsGlobalVarClass.gBuildingCodeForHD = rsCustomer.getString(4);
			lblCustInfo.setText("<html>" + rsCustomer.getString(3) + "</html>");
			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		    else
		    {
			clsGlobalVarClass.gNewCustomerForHomeDel = true;
			clsGlobalVarClass.gTotalBillAmount = totalBillAmount;
			clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(mbNo);
			//new frmCustMasterRMS1(this,true).setVisible(true);
			new frmCustomerMaster().setVisible(true);
			lblCustInfo.setText(clsGlobalVarClass.gCustomerName);
			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		    rsCustomer.close();
		}
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funResetHomeDeliveryFileds()
    {
	//clsGlobalVarClass.gCustomerCodeForHomeDelivery = null;
	clsGlobalVarClass.gCustomerCode = "";
	clsGlobalVarClass.gCustomerAddress1 = "";
	clsGlobalVarClass.gCustomerAddress2 = "";
	clsGlobalVarClass.gCustomerCity = "";
	clsGlobalVarClass.gNewCustomerForHomeDel = false;
	clsGlobalVarClass.gNewCustomerMobileNo = 0;
	clsGlobalVarClass.gCustMBNo = "";
	clsGlobalVarClass.gTotalBillAmount = 0;
	lblDelBoyName.setText("");
	clsGlobalVarClass.gDeliveryBoyCode = null;
	clsGlobalVarClass.gDeliveryTime = null;
	clsGlobalVarClass.gDeliveryBoyName = "";
	clsGlobalVarClass.gAdvOrderNoForBilling = null;
	homeDeliveryForTax = "N";
	btnHomeDelivery.setForeground(Color.white);
    }

    private int funGetIndexOfItem(String itemCode)
    {
	int rowNo = 0;
	try
	{
	    boolean flag = false;
	    for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
	    {
		rowNo++;
		if (ob.getItemCode().equalsIgnoreCase(itemCode) && ob.isIsModifier() == false)
		{
		    flag = true;
		}
		if (flag)
		{
		    break;
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    
	}
	finally
	{
	    return rowNo;
	}
    }

    private ImageIcon funGetImageIcon(String itemCode)
    {
	ImageIcon icon = null;
	try
	{
	    if (itemCode.length() > 0)
	    {
		if (itemCode.equals("I000306"))
		{
		    System.out.println(itemCode);
		}
		String filePath = System.getProperty("user.dir");
		File f = new File(filePath + "/itemImages/" + itemCode + ".jpg");
		if (f.exists())
		{
		    icon = new ImageIcon(ImageIO.read(f));
		}
		else
		{
		    f = new File(filePath + "/itemImages/" + itemCode + ".png");
		    if (f.exists())
		    {
			icon = new ImageIcon(ImageIO.read(f));
		    }
		    else
		    {
			icon = null;
		    }
		}
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    icon = null;

	}
	return icon;
    }

    private void funAddSubItemToPriceList(String itemCode, String itemName)
    {
	try
	{
	    String sql_ItemDtl = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			+ " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			+ " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			+ " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
			+ ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE b.strItemCode='" + itemCode + "' and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and b.strOperationalYN='Y' ";

	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			+ "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
			+ "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			+ "a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
			+ ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ "WHERE b.strItemCode='" + itemCode + "' and a.strAreaCode='" + areaWisePricingAreaCode + "'  "
			+ "and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and b.strOperationalYN='Y' ";
	    }
	    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
	    if (rsItemInfo.next())
	    {
		clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
			rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6),
			rsItemInfo.getDouble(7), rsItemInfo.getDouble(8),
			rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
			rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
			rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), rsItemInfo.getDouble(21), rsItemInfo.getString(22));
		if (null != list_ItemNames_Buttoms && list_ItemNames_Buttoms.size() > 0 && null != obj_List_ItemPrice)
		{
		    itemName = rsItemInfo.getString(2);
		    if (!list_ItemNames_Buttoms.contains(rsItemInfo.getString(2)))
		    {
			list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
			obj_List_ItemPrice.add(ob);
		    }
		}
		else
		{
		    list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
		    itemName = rsItemInfo.getString(2);
		    obj_List_ItemPrice = new ArrayList<>();
		    obj_List_ItemPrice.add(ob);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funPLUClosedButtonPressed()
    {
	funSetVisiblePanels(true);
	txtExternalCode.requestFocus();
    }

    private void funSetVisiblePanels(boolean isVisible)
    {
	panelItemList.setVisible(isVisible);
	panelNavigate.setVisible(isVisible);
	panelSubGroup.setVisible(isVisible);
	panelExternalCode.setVisible(isVisible);
	panelPLU.setVisible(!isVisible);
	txtPLUItemSearch.setText("");
	btnPLU.requestFocus();
    }

    private void funPLUButtonPressed()
    {
	funSetVisiblePanels(false);
	txtPLUItemSearch.requestFocus();
	funFillPLUTable();
	frmAlfaNumericKeyBoard1 obj = new frmAlfaNumericKeyBoard1(this, true, "Direct Biller");
    }

    public void funSetKeyBoardValueOnPLUTextBox(String text)
    {
	txtPLUItemSearch.setText(text);
	funPLUItemSearch();
    }

    private void funFillPLUTable()
    {
	try
	{

	    DefaultTableModel dmPLUItemTable = (DefaultTableModel) tblPLUItems.getModel();
	    dmPLUItemTable.setRowCount(0);
	    String clsAreaCode = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		clsAreaCode = areaWisePricingAreaCode;
	    }
	    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);

	    for (String iName : x.keySet())
	    {

		Object[] rows =
		{
		    iName
		};
		dmPLUItemTable.addRow(rows);
	    }
	    tblPLUItems.setModel(dmPLUItemTable);

	    final TableRowSorter<TableModel> sorter;
	    sorter = new TableRowSorter<TableModel>(dmPLUItemTable);
	    tblPLUItems.setRowSorter(sorter);
	    tblPLUItems.setDefaultRenderer(Object.class, new MyTableCellRender());
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    class MyTableCellRender extends DefaultTableCellRenderer
    {

	public MyTableCellRender()
	{
	    setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{

	    String itemName = (String) value;

	    String clsAreaCode = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		clsAreaCode = areaWisePricingAreaCode;
	    }
	    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);
	    clsItemPriceDtl objItemPriceDtl = x.get(itemName);
	    switch (objItemPriceDtl.getStrTextColor())
	    {
		case "Red":
		    setForeground(Color.BLACK);
		    setBackground(Color.red);
		    break;
		case "Black":
		    setForeground(Color.BLACK);
		    setBackground(Color.WHITE);
		    break;
		case "Green":
		    setForeground(Color.BLACK);
		    setBackground(Color.GREEN);
		    break;
		case "CYAN":
		    setForeground(Color.BLACK);
		    setBackground(Color.CYAN);
		    break;
		case "MAGENTA":
		    setForeground(Color.BLACK);
		    setBackground(Color.MAGENTA);
		    break;
		case "ORANGE":
		    setForeground(Color.BLACK);
		    setBackground(Color.ORANGE);
		    break;
		case "PINK":
		    setForeground(Color.BLACK);
		    setBackground(Color.PINK);
		    break;
		case "YELLOW":
		    setForeground(Color.BLACK);
		    setBackground(Color.YELLOW);
		    break;
		case "WHITE":
		    setForeground(Color.BLACK);
		    setBackground(Color.WHITE);
		    break;
		case "BLUE":
		    setForeground(Color.WHITE);
		    setBackground(Color.BLUE);
		    break;
		default:
		    setForeground(Color.BLACK);
		    setBackground(Color.WHITE);
		    break;
	    }
	    setText(value != null ? value.toString() : "");
	    if (hasFocus)
	    {
		if (getBackground() == Color.WHITE)
		{
		    setBackground(tblPLUItems.getSelectionBackground());
		}
		else
		{
		    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}
	    }
	    else
	    {
		setBorder(noFocusBorder);
	    }
	    return this;
	}
    }

    private void funPLUItemsMouseClicked(int selectedRow)
    {
	try
	{

	    String tempItemName = tblPLUItems.getValueAt(selectedRow, 0).toString();
	    txtPLUItemSearch.setText("");
	    String clsAreaCode = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		clsAreaCode = areaWisePricingAreaCode;
	    }
	    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);
	    clsItemPriceDtl priceObject = x.get(tempItemName);

	    menuHeadCode = priceObject.getStrMenuHeadCode();
	    String sql = "select strMenuCode,strMenuName from tblmenuhd where strMenuCode='" + menuHeadCode + "'";
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsMenuHead.next())
	    {
		selectedMenuHeadName = rsMenuHead.getString(2);
	    }
	    rsMenuHead.close();

	    funGetPLUItemPrice(priceObject);
	    txtPLUItemSearch.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    static void permute(String[] a, int k)
    {
	String data = "";
	if (k == a.length)
	{
	    for (int i = 0; i < a.length; i++)
	    {
		//System.out.print(" [" + a[i] + "] ");
		if (!data.isEmpty())
		{
		    data = data + " " + a[i];
		}
		else
		{
		    data = a[i];
		}
	    }
	    arrListItemCombination.add(data);

	    //System.out.println(data);
	}
	else
	{
	    for (int i = k; i < a.length; i++)
	    {
		String temp = a[k];
		a[k] = a[i];
		a[i] = temp;

		permute(a, k + 1);

		temp = a[k];
		a[k] = a[i];
		a[i] = temp;
	    }
	}
    }

    private void funPLUItemSearch()
    {
	try
	{
	    arrListItemCombination.clear();
	    String text = txtPLUItemSearch.getText().trim();
	    DefaultTableModel dm_PLU_Item_Table = (DefaultTableModel) tblPLUItems.getModel();
	    final TableRowSorter<TableModel> sorter;
	    sorter = new TableRowSorter<TableModel>(dm_PLU_Item_Table);
	    tblPLUItems.setRowSorter(sorter);

	    List<RowFilter<Object, Object>> filters = new ArrayList<>();
	    RowFilter<Object, Object> forwordTextRowFilter = RowFilter.regexFilter("(?i)" + text);

	    String reverseText = "";

	    String words[] = text.split(" ");
	    for (int i = words.length - 1; i >= 0; i--)
	    {
		reverseText += words[i];
		reverseText += " ";
	    }
	    System.out.println("Rev Text= " + reverseText);
	    RowFilter<Object, Object> backwordTextRowFilter = RowFilter.regexFilter("(?i)" + reverseText.trim());

	    /*
             * if(words.length > 1) { for(int i=0;i<words.length;i++) {
             * RowFilter<Object,Object> wordsRowFilter
             * =RowFilter.regexFilter("(?i)" + words[i].trim());
             * filters.add(wordsRowFilter); } }
	     */
	    filters.add(forwordTextRowFilter);
	    filters.add(backwordTextRowFilter);

	    permute(words, 0);
	    System.out.println(arrListItemCombination);
	    for (String items : arrListItemCombination)
	    {
		RowFilter<Object, Object> combiTextRowFilter = RowFilter.regexFilter("(?i)" + items.trim());
		filters.add(combiTextRowFilter);
	    }

	    RowFilter<Object, Object> combinedRowFilter = RowFilter.orFilter(filters);
	    sorter.setRowFilter(combinedRowFilter);
	    sorter.setSortKeys(null);

	    if (tblPLUItems.getModel().getRowCount() > 0)
	    {
		int row = tblPLUItems.getSelectedRow();
		int rowcount = tblPLUItems.getRowCount();
		if (row == -1)
		{
		    row = 0;
		    tblPLUItems.changeSelection(row, 0, false, false);
		}
		else if (row == rowcount)
		{
		    row = 0;
		    tblPLUItems.changeSelection(row, 0, false, false);
		}
		else if (row < rowcount)
		{
		    tblPLUItems.changeSelection(row - 1, 0, false, false);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    txtPLUItemSearch.setText("");
	}
    }

    public void funGetPLUItemPrice(clsItemPriceDtl priceObject)
    {
	String itemCode = "";
	String itemName;
	try
	{
	    flag_isTDHModifier_Item = false;
	    itemCode = priceObject.getStrItemCode();
	    itemName = priceObject.getStrItemName();
	    boolean flg_isComboItem = false;

	    double dblPrice = 0;
	    double qty = 1;
	    dblPrice = funGetFinalPrice(priceObject);
	    double amt = dblPrice * qty;
	    double itemPrice = dblPrice;
	    double weight = selectedQty;

	    boolean isVailable = objUtility.isItemAvailableForTotay(itemCode, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode);

	    if (!isVailable)
	    {
		new frmOkPopUp(this, "<html>\"" + itemName.toUpperCase() + "\" Is Not Available For Today.</html>", "Warning", 1).setVisible(true);
		return;
	    }

	    if (clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y"))
	    {
		itemWeight = funGetItemWeight(itemCode);
		System.out.println("ItemWeight=" + itemWeight);
	    }

	    if (clsGlobalVarClass.gItemQtyNumpad && clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
	    {
		frmNumericKeyboard numItemWeightKeyPad = new frmNumericKeyboard(this, true, "", "Double", "No Of Boxes.");
		numItemWeightKeyPad.setVisible(true);
		noOfBoxes = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    if (!clsGlobalVarClass.gItemQtyNumpad && clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
	    {
		frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Qty");
		num.setVisible(true);
		//selectedQty = num.getResult();
		if (null != clsGlobalVarClass.gNumerickeyboardValue)
		{
		    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		    clsGlobalVarClass.gNumerickeyboardValue = null;
		}
		frmNumericKeyboard numItemWeightKeyPad = new frmNumericKeyboard(this, true, "", "Double", "No Of Boxes.");
		numItemWeightKeyPad.setVisible(true);
		noOfBoxes = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
	    }

	    if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(itemCode))
	    {
		flg_isComboItem = true;
		MaxSubItemLimitWithComboItem = funGetMaxQty(itemCode, "MaxSubItemLimitWithComboItem", "");
		funShowComboSubItemTable(itemCode);
	    }
	    else if (clsGlobalVarClass.ListTDHOnModifierItem.contains(itemCode))
	    {
		flag_isTDHModifier_Item = true;
		MaxQTYOfModifierWithTDHItem = clsGlobalVarClass.ListTDHOnModifierItemMaxQTY.get((clsGlobalVarClass.ListTDHOnModifierItem.indexOf(itemCode)));
		clsDirectBillerItemDtl ob1 = new clsDirectBillerItemDtl(itemName, itemCode, qty, amt, false, "", "N", "", dblPrice, "", getSeqNo(), 0);
		serNo++;
		frmTDHDialog ob = new frmTDHDialog(this, true, itemCode, ob1);
		ob.setVisible(true);
	    }
	    else
	    {
		double Price = funGetFinalPrice(priceObject);
		if (Price == 0)
		{
		    boolean isUserGranted = false;
		    if (clsGlobalVarClass.gSuperUser)
		    {
			isUserGranted = true;
		    }
		    else
		    {
			String formName = "open Items";
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
			    else
			    {
				isUserGranted = false;
			    }
			    if (isValidUser.equalsIgnoreCase("Valid User"))
			    {
				isUserGranted = objUtility2.funHasGrant(formName, enterUserCode);
				if (isUserGranted)
				{
				    isUserGranted = true;
				}
				else
				{
				    new frmOkPopUp(null, "User \"" + enterUserCode + "\" Not Granted.", "Error", 1).setVisible(true);
				    isUserGranted = false;
				}
			    }
			    else
			    {
				new frmOkPopUp(null, isValidUser, "Error", 1).setVisible(true);
				isUserGranted = false;
			    }
			}
			else
			{
			    isUserGranted = true;
			}
		    }

		    if (isUserGranted)
		    {

			frmNumberKeyPad num;
			if (clsGlobalVarClass.gItemQtyNumpad)
			{
			    selectedQty = 0;
			    num = new frmNumberKeyPad(this, true, "qty");
			    num.setVisible(true);
			    //selectedQty = num.getResult();
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
				{
				    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				    clsGlobalVarClass.gNumerickeyboardValue = null;
				}
			    }
			}

			frmNumberKeyPad obj = new frmNumberKeyPad(this, true, "Rate" + Price);
			obj.setVisible(true);
			if (clsGlobalVarClass.gRateEntered)
			{
			    //double itemPrice = obj.getResult();
			    itemPrice = 0;
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				itemPrice = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			    if (selectedQty > 0)
			    {
				flagOpenItem = true;
				funInsertData(selectedQty, itemPrice, itemCode, itemName, priceObject.getDblPurchaseRate());
				funRefreshItemTable();
				selectedQty = 1;
				flagOpenItem = false;
			    }
			    else
			    {
				new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
			    }
			    clsGlobalVarClass.gRateEntered = false;
			}
		    }

		}
		else
		{

		    frmNumberKeyPad num;
		    if (clsGlobalVarClass.gItemQtyNumpad)
		    {
			selectedQty = 0;
			num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			//selectedQty = num.getResult();
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			    {
				selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			}
		    }
		    if (selectedQty > 0)
		    {
			//////////////////////// foe Stk validation///////////
			if (priceObject.getStrStockInEnable().equals("Y") && clsGlobalVarClass.gShowItemStkColumnInDB)
			{
			    double stkQty = clsGlobalVarClass.funGetStock(itemCode, "Item");
			    if (stkQty < selectedQty)
			    {
				new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
				return;
			    }

			    if (tblItemTable.getRowCount() > 0)
			    {
				int count = 0;
				boolean flgSameRowAvaiale = false;
				for (int r = 0; r < tblItemTable.getRowCount(); r++)
				{
				    if (tblItemTable.getValueAt(r, 4).toString().equals(itemCode))
				    {
					flgSameRowAvaiale = true;
					break;
				    }
				    count++;
				}
				if (flgSameRowAvaiale)
				{
				    if (Double.parseDouble(tblItemTable.getValueAt(count, 3).toString()) < (Double.parseDouble(tblItemTable.getValueAt(count, 1).toString()) + 1.00))
				    {
					new frmOkPopUp(null, "Stock In quantity not Available", "Error", 1).setVisible(true);
					return;
				    }
				}

				funInsertData(selectedQty, Price, itemCode, itemName, priceObject.getDblPurchaseRate());
				funRefreshItemTable();
				selectedQty = 1;
			    }
			    else
			    {
				funInsertData(selectedQty, Price, itemCode, itemName, priceObject.getDblPurchaseRate());
				funRefreshItemTable();
				selectedQty = 1;
			    }
			    /////////////////////////////////////////////////////////
			}
			else
			{
			    funInsertData(selectedQty, Price, itemCode, itemName, priceObject.getDblPurchaseRate());
			    funRefreshItemTable();
			    selectedQty = 1;
			}
		    }
		    else
		    {
			new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
		    }
		}
	    }

	    if (flg_isComboItem)
	    {
		insertDefaultSubItems(itemCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}

    }

    /**
     * @return the objData
     */
    public clsCustomerDataModelForSQY getObjData()
    {
	return objData;
    }

    private class CancelAction extends AbstractAction
    {

	@Override
	public void actionPerformed(ActionEvent ev)
	{
	    if (txtPLUItemSearch.isFocusable() && txtPLUItemSearch.getText().trim().length() == 0)
	    {
		btnPLU.requestFocus();
		funSetVisiblePanels(true);
		txtExternalCode.requestFocus();
	    }
	    else
	    {
		txtPLUItemSearch.setText("");
		txtPLUItemSearch.requestFocus();
	    }
	}
    }

    private void funDownArrowPressedForPLU()
    {
	if (tblPLUItems.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblPLUItems.getSelectedRow();
	    int rowcount = tblPLUItems.getRowCount();
	    if (selectedRow < rowcount)
	    {
		tblPLUItems.changeSelection(selectedRow + 1, 0, false, false);
	    }
	    else if (selectedRow == rowcount)
	    {
		selectedRow = 0;
		tblPLUItems.changeSelection(selectedRow, 0, false, false);
	    }
	}
    }

    public void funSendItemName(String itemName, clsItemPriceDtl priceObject)
    {
	if (null != list_ItemNames_Buttoms && list_ItemNames_Buttoms.size() > 0 && null != obj_List_ItemPrice)
	{
	    if (!list_ItemNames_Buttoms.contains(itemName))
	    {
		list_ItemNames_Buttoms.add(itemName);
		obj_List_ItemPrice.add(priceObject);
	    }
	}
	else
	{
	    list_ItemNames_Buttoms.add(itemName);
	    obj_List_ItemPrice = new ArrayList<>();
	    obj_List_ItemPrice.add(priceObject);
	}
	funGetPrice(itemName);
    }

    public void funShowItemPanel()
    {
	selectedQty = 1;
	panelItemList.setVisible(true);
	panelNavigate.setVisible(true);
	funHideShowSubGroupPanel(true);
    }

    public List<clsDirectBillerItemDtl> getObj_List_ItemDtl()
    {
	return obj_List_ItemDtl;
    }

    public void setObj_List_ItemDtl(List<clsDirectBillerItemDtl> obj_List_ItemDtl)
    {
	this.obj_List_ItemDtl = obj_List_ItemDtl;
    }

    private void funSwipeDebitCard()
    {
	try
	{
	    new frmSwipCardPopUp(this, "").setVisible(true);
	    if (clsGlobalVarClass.gDebitCardNo != null)
	    {
		String sql = "select a.strCardNo,a.dblRedeemAmt,a.strCustomerCode,ifnull(b.strCustomerName,'') "
			+ " from tbldebitcardmaster a "
			+ " left outer join tblcustomermaster b "
			+ " on a.strCustomerCode=b.strCustomerCode "
			+ " where a.strCardString='" + clsGlobalVarClass.gDebitCardNo + "' ";
		ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsDebitCardNo.next())
		{
		    clsGlobalVarClass.gCustomerCode = rsDebitCardNo.getString(3);
		    debitCardBalance = objUtility.funGetDebitCardBalance(clsGlobalVarClass.gDebitCardNo, "");
		    lblCustInfo.setText("<html>" + String.valueOf(Math.rint(debitCardBalance)) + "<br>" + rsDebitCardNo.getString(4) + "</html>");
		}
	    }

	}

	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }


    private void btnNextMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextMenuActionPerformed
	// TODO add your handling code here:
	funNextMenuButtonClicked();
    }//GEN-LAST:event_btnNextMenuActionPerformed

    private void btnIItem2ActionPerformed(java.awt.event.ActionEvent evt)
    {
	if (btnIItem2.isEnabled())
	{
	    funMenuItemSelection(btnIItem2.getText(), 1);
	}
    }
    private void btnIItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem2MouseClicked
	if (btnIItem2.isEnabled())
	{
	    funMenuItemSelection(btnIItem2.getText(), 1);
	}
    }//GEN-LAST:event_btnIItem2MouseClicked

    private void btnIItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem1ActionPerformed
	// TODO add your handling code here:
	if (btnIItem1.isEnabled())
	{
	    funMenuItemSelection(btnIItem1.getText(), 0);
	}
    }//GEN-LAST:event_btnIItem1ActionPerformed

    private void btnIItem3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem3MouseClicked

    }//GEN-LAST:event_btnIItem3MouseClicked

    private void btnIItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem3ActionPerformed
	// TODO add your handling code here:
	if (btnIItem3.isEnabled())
	{
	    funMenuItemSelection(btnIItem3.getText(), 2);
	}
    }//GEN-LAST:event_btnIItem3ActionPerformed

    private void btnIItem4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem4MouseClicked
	// TODO add your handling code here:itemName=btnIItem1.getText();
    }//GEN-LAST:event_btnIItem4MouseClicked

    private void btnIItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem4ActionPerformed
	// TODO add your handling code here:
	if (btnIItem4.isEnabled())
	{
	    funMenuItemSelection(btnIItem4.getText(), 3);
	}
    }//GEN-LAST:event_btnIItem4ActionPerformed

    private void btnIItem5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem5MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem5MouseClicked

    private void btnIItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem5ActionPerformed
	// TODO add your handling code here:
	if (btnIItem5.isEnabled())
	{
	    funMenuItemSelection(btnIItem5.getText(), 4);
	}
    }//GEN-LAST:event_btnIItem5ActionPerformed

    private void btnIItem6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem6MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem6MouseClicked

    private void btnIItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem6ActionPerformed
	// TODO add your handling code here:
	if (btnIItem6.isEnabled())
	{
	    funMenuItemSelection(btnIItem6.getText(), 5);
	}
    }//GEN-LAST:event_btnIItem6ActionPerformed

    private void btnIItem7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem7MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem7MouseClicked

    private void btnIItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem7ActionPerformed
	// TODO add your handling code here:
	if (btnIItem7.isEnabled())
	{
	    funMenuItemSelection(btnIItem7.getText(), 6);
	}
    }//GEN-LAST:event_btnIItem7ActionPerformed

    private void btnIItem8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem8MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem8MouseClicked

    private void btnIItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem8ActionPerformed
	// TODO add your handling code here:
	if (btnIItem8.isEnabled())
	{
	    funMenuItemSelection(btnIItem8.getText(), 7);
	}
    }//GEN-LAST:event_btnIItem8ActionPerformed

    private void btnIItem9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem9MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem9MouseClicked

    private void btnIItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem9ActionPerformed
	// TODO add your handling code here:
	if (btnIItem9.isEnabled())
	{
	    funMenuItemSelection(btnIItem9.getText(), 8);
	}
    }//GEN-LAST:event_btnIItem9ActionPerformed

    private void btnIItem10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem10MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem10MouseClicked

    private void btnIItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem10ActionPerformed
	// TODO add your handling code here:
	if (btnIItem10.isEnabled())
	{
	    funMenuItemSelection(btnIItem10.getText(), 9);
	}
    }//GEN-LAST:event_btnIItem10ActionPerformed

    private void btnIItem11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem11MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem11MouseClicked

    private void btnIItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem11ActionPerformed
	// TODO add your handling code here:
	if (btnIItem11.isEnabled())
	{
	    funMenuItemSelection(btnIItem11.getText(), 10);
	}
    }//GEN-LAST:event_btnIItem11ActionPerformed

    private void btnIItem12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem12MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem12MouseClicked

    private void btnIItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem12ActionPerformed
	// TODO add your handling code here:
	if (btnIItem12.isEnabled())
	{
	    funMenuItemSelection(btnIItem12.getText(), 11);
	}
    }//GEN-LAST:event_btnIItem12ActionPerformed

    private void btnIItem13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem13MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem13MouseClicked

    private void btnIItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem13ActionPerformed
	// TODO add your handling code here:
	if (btnIItem13.isEnabled())
	{
	    funMenuItemSelection(btnIItem13.getText(), 12);
	}
    }//GEN-LAST:event_btnIItem13ActionPerformed

    private void btnIItem14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem14MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem14MouseClicked

    private void btnIItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem14ActionPerformed
	// TODO add your handling code here:
	if (btnIItem14.isEnabled())
	{
	    funMenuItemSelection(btnIItem14.getText(), 13);
	}
    }//GEN-LAST:event_btnIItem14ActionPerformed

    private void btnIItem15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem15MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem15MouseClicked

    private void btnIItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem15ActionPerformed
	// TODO add your handling code here:
	if (btnIItem15.isEnabled())
	{
	    funMenuItemSelection(btnIItem15.getText(), 14);
	}
    }//GEN-LAST:event_btnIItem15ActionPerformed

    private void btnIItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem16MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem16MouseClicked

    private void btnIItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem16ActionPerformed
	// TODO add your handling code here:
	if (btnIItem16.isEnabled())
	{
	    funMenuItemSelection(btnIItem16.getText(), 15);
	}
    }//GEN-LAST:event_btnIItem16ActionPerformed

    private void btnNumber2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber2MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber2.getText().trim());
    }//GEN-LAST:event_btnNumber2MouseClicked

    private void btnNumber1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber1MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber1.getText().trim());
    }//GEN-LAST:event_btnNumber1MouseClicked

    private void btnNumber4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber4MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber4.getText().trim());
    }//GEN-LAST:event_btnNumber4MouseClicked

    private void btnNumber3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber3MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber3.getText().trim());
    }//GEN-LAST:event_btnNumber3MouseClicked

    private void btnNumber5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber5MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber5.getText().trim());
    }//GEN-LAST:event_btnNumber5MouseClicked

    private void btnNumber6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber6MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber6.getText().trim());
    }//GEN-LAST:event_btnNumber6MouseClicked

    private void btnNumber7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber7MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber7.getText().trim());
    }//GEN-LAST:event_btnNumber7MouseClicked

    private void btnNumber8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber8MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber8.getText().trim());
    }//GEN-LAST:event_btnNumber8MouseClicked

    private void btnNumber9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber9MouseClicked
	// TODO add your handling code here:
	funSetSelectedQty(btnNumber9.getText().trim());
    }//GEN-LAST:event_btnNumber9MouseClicked

    private void btnMultiQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMultiQtyMouseClicked
	// TODO add your handling code here:
	try
	{
	    funDisableSelectedQtyBtn(btnMultiQty.getText().trim());
	    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
	    num.setVisible(true);
	    //Double result = num.getResult();
	    double result = 0;
	    if (null != clsGlobalVarClass.gNumerickeyboardValue)
	    {
		result = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		clsGlobalVarClass.gNumerickeyboardValue = null;
	    }
	    if ("".equals(lblVoucherNo.getText()))
	    {
		selectedQty = result;
	    }
	    if (flgChangeQty == true)
	    {
		funChangeQtyOfItem(btnMultiQty.getText().trim());
		flgChangeQty = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMultiQtyMouseClicked

    private void lblCustInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustInfoMouseClicked
	try
	{
	    // TODO add your handling code here:
	    funCustInfoBtnClicked();
	}
	catch (IOException ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    Logger.getLogger(frmDirectBiller.class.getName()).log(Level.SEVERE, null, ex);
	}
    }//GEN-LAST:event_lblCustInfoMouseClicked

    private void btnPrevItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevItemActionPerformed
	// TODO add your handling code here:
	funPreviousItemButtonClicked();
    }//GEN-LAST:event_btnPrevItemActionPerformed

    private void btnNextItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextItemActionPerformed
	// TODO add your handling code here:
	funNextItemButtonClicked();
    }//GEN-LAST:event_btnNextItemActionPerformed

    private void txtExternalCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExternalCodeMouseClicked
	// TODO add your handling code here:
	funGetItemFromExtCode("click");
    }//GEN-LAST:event_txtExternalCodeMouseClicked

    private void txtExternalCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExternalCodeActionPerformed

	System.out.println("action perform");
	funGetItemFromExtCode("");
    }//GEN-LAST:event_txtExternalCodeActionPerformed

    private void btnPrevItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevItemSortingMouseClicked
	// TODO add your handling code here:
	funPreviousItemSortingButtonClicked();
    }//GEN-LAST:event_btnPrevItemSortingMouseClicked

    private void btnItemSorting1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting1MouseClicked
	if (btnItemSorting1.isEnabled())
	{
	    int index = listTopButtonName.indexOf(btnItemSorting1.getText());
	    String buttonCode = listTopButtonCode.get(index);
	    funResetItemButtonTextSelectionWise(menuHeadCode, buttonCode);
	}
    }//GEN-LAST:event_btnItemSorting1MouseClicked

    private void btnItemSorting2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting2MouseClicked
	// TODO add your handling code here:
	if (btnItemSorting2.isEnabled())
	{
	    int index = listTopButtonName.indexOf(btnItemSorting2.getText());
	    String buttonCode = listTopButtonCode.get(index);
	    funResetItemButtonTextSelectionWise(menuHeadCode, buttonCode);
	}
    }//GEN-LAST:event_btnItemSorting2MouseClicked

    private void btnItemSorting3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting3MouseClicked
	// TODO add your handling code here:

	if (btnItemSorting3.isEnabled())
	{
	    int index = listTopButtonName.indexOf(btnItemSorting3.getText());
	    String buttonCode = listTopButtonCode.get(index);
	    funResetItemButtonTextSelectionWise(menuHeadCode, buttonCode);
	}
    }//GEN-LAST:event_btnItemSorting3MouseClicked

    private void btnNextItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextItemSortingMouseClicked
	// TODO add your handling code here:
	funNextItemSortingButtonClicked();
    }//GEN-LAST:event_btnNextItemSortingMouseClicked

    private void btnItemSorting4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting4MouseClicked
	// TODO add your handling code here:
	if (btnItemSorting4.isEnabled())
	{
	    int index = listTopButtonName.indexOf(btnItemSorting4.getText());
	    String buttonCode = listTopButtonCode.get(index);
	    funResetItemButtonTextSelectionWise(menuHeadCode, buttonCode);
	}
    }//GEN-LAST:event_btnItemSorting4MouseClicked

    private void bttnPLUPanelCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bttnPLUPanelCloseMouseClicked
	funPLUClosedButtonPressed();
    }//GEN-LAST:event_bttnPLUPanelCloseMouseClicked

    private void txtPLUItemSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPLUItemSearchFocusGained
	funPLUItemSearch();
    }//GEN-LAST:event_txtPLUItemSearchFocusGained

    private void txtPLUItemSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPLUItemSearchMouseClicked
	//new frmAlfaNumericKeyBoard(null, true, "1", "Search").setVisible(true);
	//txtPLUItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
	frmAlfaNumericKeyBoard1 obj = new frmAlfaNumericKeyBoard1(this, true, "Direct Biller");
    }//GEN-LAST:event_txtPLUItemSearchMouseClicked

    private void txtPLUItemSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLUItemSearchKeyPressed
	if (txtPLUItemSearch.isFocusable() && evt.getKeyCode() == 40)
	{
	    tblPLUItems.requestFocus();
	    funDownArrowPressedForPLU();
	}
	if (txtPLUItemSearch.isFocusable() && evt.getKeyCode() == 10)
	{
	    tblPLUItems.requestFocus();
	    int selectedRow = tblPLUItems.getSelectedRow();
	    funPLUItemsMouseClicked(selectedRow);
	}
    }//GEN-LAST:event_txtPLUItemSearchKeyPressed

    private void txtPLUItemSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLUItemSearchKeyReleased
	funPLUItemSearch();
    }//GEN-LAST:event_txtPLUItemSearchKeyReleased

    private void txtPLUItemSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLUItemSearchKeyTyped

    }//GEN-LAST:event_txtPLUItemSearchKeyTyped

    private void tblPLUItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPLUItemsMouseClicked
	int selectedRow = tblPLUItems.getSelectedRow();
	funPLUItemsMouseClicked(selectedRow);
    }//GEN-LAST:event_tblPLUItemsMouseClicked

    private void tblPLUItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPLUItemsKeyPressed
	if (evt.getKeyCode() == 10)
	{
	    int selectedRow = tblPLUItems.getSelectedRow();
	    funPLUItemsMouseClicked(selectedRow);
	}
    }//GEN-LAST:event_tblPLUItemsKeyPressed

    private void btnSettleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettleActionPerformed

	if (clsGlobalVarClass.gEnableDineIn)
	{
	    funSettleButtonClicked();
	}
	else
	{
	    if (btnTakeAway.getForeground().equals(Color.white) && btnHomeDelivery.getForeground().equals(Color.white))
	    {
		new frmOkPopUp(this, "Please Select Take Away or Home Delivery", "Warning", 3).setVisible(true);
		return;
	    }
	    else
	    {
		funSettleButtonClicked();
	    }
	}
    }//GEN-LAST:event_btnSettleActionPerformed

    private void bttnPLUPanelCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bttnPLUPanelCloseKeyPressed

    }//GEN-LAST:event_bttnPLUPanelCloseKeyPressed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
	// TODO add your handling code here:
	funDownButtonPressed();
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnDelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelItemActionPerformed
	// TODO add your handling code here:
	funDeleteButtonPressed();
    }//GEN-LAST:event_btnDelItemActionPerformed

    private void btnHomeDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeDeliveryActionPerformed
	// TODO add your handling code here:
	if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.isEmpty())
	{
	    new frmOkPopUp(null, "Please Select Customer.", "Warning", 1).setVisible(true);
	    return;
	}
	else
	{
	    funHomeDeliveryButtonPressed();
	}
    }//GEN-LAST:event_btnHomeDeliveryActionPerformed

    private void btnCustomerHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerHistoryActionPerformed
	// TODO add your handling code here:

	if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.isEmpty())
	{
	    new frmOkPopUp(null, "Please Select Customer.", "Warning", 1).setVisible(true);
	    return;
	}
	else
	{
	    //frmCustomerHistory objCustomer=new frmCustomerHistory(this);
	    frmCustomerHistory objCustomer = new frmCustomerHistory(this);
	    objCustomer.setLocation(panelFormBody.getLocation());
	    this.disable();
	    objCustomer.setVisible(true);

//                    final JDialog frame = new JDialog(objCustomer, "", true);
//                    frame.getContentPane().add(this);
//                    frame.pack();
//                    frame.setVisible(true);
	    btnCustomerHistory.setSelected(false);
	    objUtility = new clsUtility();
	}
    }//GEN-LAST:event_btnCustomerHistoryActionPerformed

    private void btnTakeAwayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTakeAwayActionPerformed

	funTakeAwayButtonPressed();
    }//GEN-LAST:event_btnTakeAwayActionPerformed

    private void btnDelBoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelBoyActionPerformed
	// TODO add your handling code here:
	funSelectDeliveryPersonCode();
    }//GEN-LAST:event_btnDelBoyActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
	// TODO add your handling code here:
	funHomeButtonPressed();

	dispose();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void lblCustInfoMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblCustInfoMouseEntered
    {//GEN-HEADEREND:event_lblCustInfoMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_lblCustInfoMouseEntered

    private void tblItemTableMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblItemTableMouseEntered
    {//GEN-HEADEREND:event_tblItemTableMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_tblItemTableMouseEntered

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Direct Biller");
    }//GEN-LAST:event_formWindowClosed

    private void txtExternalCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtExternalCodeKeyPressed
    {//GEN-HEADEREND:event_txtExternalCodeKeyPressed
	funEscKeyPressed(evt);
    }//GEN-LAST:event_txtExternalCodeKeyPressed

    private void btnIItem2MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnIItem2MouseEntered
    {//GEN-HEADEREND:event_btnIItem2MouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnIItem2MouseEntered

    private void btnAreaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAreaActionPerformed
    {//GEN-HEADEREND:event_btnAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAreaActionPerformed

    private void btnAreaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAreaKeyPressed
    {//GEN-HEADEREND:event_btnAreaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAreaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgainstAdvOrder;
    private javax.swing.JButton btnArea;
    private javax.swing.JButton btnChangeQty;
    private javax.swing.JToggleButton btnCustomerHistory;
    private javax.swing.JButton btnDelBoy;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnHomeDelivery;
    private javax.swing.JButton btnIItem1;
    private javax.swing.JButton btnIItem10;
    private javax.swing.JButton btnIItem11;
    private javax.swing.JButton btnIItem12;
    private javax.swing.JButton btnIItem13;
    private javax.swing.JButton btnIItem14;
    private javax.swing.JButton btnIItem15;
    private javax.swing.JButton btnIItem16;
    private javax.swing.JButton btnIItem2;
    private javax.swing.JButton btnIItem3;
    private javax.swing.JButton btnIItem4;
    private javax.swing.JButton btnIItem5;
    private javax.swing.JButton btnIItem6;
    private javax.swing.JButton btnIItem7;
    private javax.swing.JButton btnIItem8;
    private javax.swing.JButton btnIItem9;
    private javax.swing.JButton btnItemSorting1;
    private javax.swing.JButton btnItemSorting2;
    private javax.swing.JButton btnItemSorting3;
    private javax.swing.JButton btnItemSorting4;
    private javax.swing.JButton btnMenu2;
    private javax.swing.JButton btnMenu3;
    private javax.swing.JButton btnMenu4;
    private javax.swing.JButton btnMenu5;
    private javax.swing.JButton btnMenu6;
    private javax.swing.JButton btnMenu7;
    private javax.swing.JButton btnMenu8;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnMultiQty;
    private javax.swing.JButton btnNextItem;
    private javax.swing.JButton btnNextItemSorting;
    private javax.swing.JButton btnNextMenu;
    private javax.swing.JButton btnNumber1;
    private javax.swing.JButton btnNumber2;
    private javax.swing.JButton btnNumber3;
    private javax.swing.JButton btnNumber4;
    private javax.swing.JButton btnNumber5;
    private javax.swing.JButton btnNumber6;
    private javax.swing.JButton btnNumber7;
    private javax.swing.JButton btnNumber8;
    private javax.swing.JButton btnNumber9;
    private javax.swing.JButton btnPLU;
    private javax.swing.JButton btnPopular;
    private javax.swing.JButton btnPrevItem;
    private javax.swing.JButton btnPrevItemSorting;
    private javax.swing.JButton btnPrevMenu;
    private javax.swing.JButton btnSettle;
    private javax.swing.JButton btnTakeAway;
    private javax.swing.JButton btnUp;
    private javax.swing.JButton bttnPLUPanelClose;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel lblCustInfo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblDelBoyName;
    private javax.swing.JLabel lblExtCode;
    private javax.swing.JLabel lblExtCode1;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblVoucherNo;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelArea;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JPanel panelExternalCode;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtl;
    private javax.swing.JPanel panelItemList;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelMenuHead;
    private javax.swing.JPanel panelNavigate;
    private javax.swing.JPanel panelNumeric;
    private javax.swing.JPanel panelOperationButtons;
    private javax.swing.JPanel panelPLU;
    private javax.swing.JPanel panelSubGroup;
    private javax.swing.JScrollPane scrItemGrid;
    private javax.swing.JScrollPane scrPLU;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblPLUItems;
    private javax.swing.JTextField txtExternalCode;
    private javax.swing.JTextField txtPLUItemSearch;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    private void funProcessForExternalCode(String externalCode)
    {
	try
	{
	    DecimalFormat decimalFormatFor3DecPoint = new DecimalFormat("0.000");
	    DecimalFormat decimalFormatFor2DecPoint = new DecimalFormat("0.00");
	    String itemName = "";
	    if (externalCode.trim().length() > 0)
	    {

		funPopularItem();
		if (clsGlobalVarClass.gPriceFrom.equals("Menu Pricing"))
		{
		    String extCode = "", weight = "";

		    String sqlPrincing = " a.strPriceMonday,a.strPriceTuesday,"
			    + "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			    + "a.strPriceSaturday,a.strPriceSunday ";

		    //#12345678
		    if (externalCode.startsWith("#"))
		    {
			extCode = externalCode.substring(1, 4).toString();
			weight = externalCode.substring(4, 8).toString();
			weight = weight.substring(0, 1).toString() + "." + weight.substring(1, weight.length());
		    }
		    else if (externalCode.startsWith("21") && externalCode.length() > 10)//weight
		    {
//                        Barcode: 211001100520
//                        Product Code: 100110
//                        Weight: 0520 grams

			extCode = externalCode.substring(2, 8);//productCode
			int intWeightOfProductInGrams = Integer.parseInt(externalCode.substring(8, externalCode.length()));
			String weightOfProductInKg = decimalFormatFor3DecPoint.format(intWeightOfProductInGrams / 1000.0);
			weight = weightOfProductInKg;

		    }
		    else if (externalCode.startsWith("23") && externalCode.length() > 10)//price
		    {
//                        Barcode: 2366600100750
//                        Product Code: 666001
//                        Price: 0750

			extCode = externalCode.substring(2, 8);//productCode
			int intPriceOfProductPerKg = Integer.parseInt(externalCode.substring(8, externalCode.length()));
			String dblPriceOfProductPerKg = decimalFormatFor2DecPoint.format(intPriceOfProductPerKg * 1.0);
			weight = "1";//default weight 1Kg

			sqlPrincing = " '" + dblPriceOfProductPerKg + "','" + dblPriceOfProductPerKg + "',"
				+ "'" + dblPriceOfProductPerKg + "','" + dblPriceOfProductPerKg + "','" + dblPriceOfProductPerKg + "', "
				+ "'" + dblPriceOfProductPerKg + "','" + dblPriceOfProductPerKg + "' ";
		    }
		    else
		    {
			extCode = externalCode;
			weight = "1";
		    }

		    externalCode = extCode;

		    sql = "select count(a.strItemCode) from tblitemmaster a,tblmenuitempricingdtl b "
			    + "where a.strItemCode=b.strItemCode and a.strExternalCode='" + externalCode + "' "
			    + "and a.strOperationalYN='Y' ";
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rs.next())
		    {
			int count = rs.getInt(1);
			rs.close();
			if (count > 0)
			{
			    String sql_ItemDtl = "";
			    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
			    {
				sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor," + sqlPrincing + ",a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
					+ "a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate"
					+ ",a.dteToDate,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
					+ "FROM tblmenuitempricingdtl a ,tblitemmaster b "
					+ "WHERE b.strExternalCode='" + externalCode + "' and a.strItemCode=b.strItemCode "
					+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
					+ "and b.strOperationalYN='Y' ";

			    }
			    else
			    {
				String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
				if (dineInForTax.equalsIgnoreCase("Y"))
				{
				    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
				}

				if (homeDeliveryForTax.equalsIgnoreCase("Y"))
				{
				    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
				}

				if (takeAwayForTax.equalsIgnoreCase("Y"))
				{
				    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
				}

				sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor," + sqlPrincing + ",a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
					+ "a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate"
					+ ",a.dteToDate,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
					+ "FROM tblmenuitempricingdtl a ,tblitemmaster b "
					+ "WHERE b.strExternalCode='" + externalCode + "' and a.strAreaCode='" + areaWisePricingAreaCode + "'  "
					+ "and a.strItemCode=b.strItemCode "
					+ "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
					+ "and b.strOperationalYN='Y' ";
			    }

			    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
			    if (rsItemInfo.next())
			    {
				clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
					rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7), rsItemInfo.getDouble(8),
					rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
					rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
					rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), rsItemInfo.getDouble(21), rsItemInfo.getString(22));
				if (null != list_ItemNames_Buttoms && list_ItemNames_Buttoms.size() > 0 && null != obj_List_ItemPrice)
				{
				    itemName = rsItemInfo.getString(2);
				    if (!list_ItemNames_Buttoms.contains(rsItemInfo.getString(2)))
				    {
					list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
					obj_List_ItemPrice.add(ob);
				    }
				}
				else
				{
				    list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
				    itemName = rsItemInfo.getString(2);
				    obj_List_ItemPrice = new ArrayList<>();
				    obj_List_ItemPrice.add(ob);
				}
				//selectedQty=1;
				selectedQty = Double.parseDouble(weight);
				if (clsGlobalVarClass.gChangeQtyForExternalCode)
				{
				    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
				    num.setVisible(true);
				    if (null != clsGlobalVarClass.gNumerickeyboardValue)
				    {
					selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
					clsGlobalVarClass.gNumerickeyboardValue = null;
				    }
				    //selectedQty = num.getResult();
				}
				funGetPrice(itemName);
				txtExternalCode.setText("");
			    }
			    rsItemInfo.close();

			}
			else
			{
			    txtExternalCode.setText("");
			}
		    }
		}
		else
		{
		    String extCode = "", weight = "";
		    if (externalCode.startsWith("#"))
		    {
			extCode = externalCode.substring(1, 4).toString();
			weight = externalCode.substring(4, 8).toString();
			weight = weight.substring(0, 1).toString() + "." + weight.substring(1, weight.length());
		    }
		    else
		    {
			extCode = externalCode;
			weight = "1";
		    }
		    if (extCode.startsWith("0"))
		    {
			extCode = extCode.substring(1, extCode.length());
			if (extCode.startsWith("0"))
			{
			    extCode = extCode.substring(1, extCode.length());
			}
		    }

		    sql = "select count(strItemCode) from tblitemmaster where strExternalCode = '" + extCode + "' and strOperationalYN='Y' ";
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rs.next())
		    {
			int count = rs.getInt(1);
			rs.close();
			if (count > 0)
			{
			    String sql_ItemDtl = "SELECT strItemCode,strItemName,strStockInEnable,dblPurchaseRate "
				    + "FROM tblitemmaster WHERE strExternalCode = '" + extCode + "' and strOperationalYN='Y' ";
			    //System.out.println(sql_ItemDtl);
			    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
			    if (rsItemInfo.next())
			    {
				clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
					0, 0, 0, 0, 0, 0, 0, "", "", "", "", "", "", "", "", "", "", rsItemInfo.getString(3), rsItemInfo.getDouble(4), "");
				if (null != list_ItemNames_Buttoms && list_ItemNames_Buttoms.size() > 0 && null != obj_List_ItemPrice)
				{
				    itemName = rsItemInfo.getString(2);
				    if (!list_ItemNames_Buttoms.contains(rsItemInfo.getString(2)))
				    {
					list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
					obj_List_ItemPrice.add(ob);
				    }
				}
				else
				{
				    list_ItemNames_Buttoms.add(rsItemInfo.getString(2));
				    itemName = rsItemInfo.getString(2);
				    obj_List_ItemPrice = new ArrayList<>();
				    obj_List_ItemPrice.add(ob);
				}
				selectedQty = Double.parseDouble(weight);
				if (clsGlobalVarClass.gChangeQtyForExternalCode)
				{
				    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
				    num.setVisible(true);
				    //selectedQty = num.getResult();
				    if (null != clsGlobalVarClass.gNumerickeyboardValue)
				    {
					selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
					clsGlobalVarClass.gNumerickeyboardValue = null;
				    }
				}
				funGetPrice(itemName);
				txtExternalCode.setText("");
			    }
			    rsItemInfo.close();
			}
			else
			{
			    txtExternalCode.setText("");
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public void funShowRewardsButtonClicked()
    {
	List<clsRewards> listOfRewards = new ArrayList<>();
	try
	{
	    if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.isEmpty())
	    {
		return;
	    }

	    clsCRMInterface objCRMInterface = new clsCRMInterface();

	    String sql_CustMb = "select longMobileNo,strCustomerCode,strCustomerName from tblcustomermaster "
		    + "where strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "'";
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
	    if (rsCust.next())
	    {
		listOfRewards = objCRMInterface.funGetCustomerRewards(rsCust.getString(1));
		if (listOfRewards.size() > 0)
		{
		    frmShowRewards objShowRewards = new frmShowRewards(this, rsCust.getString(2), rsCust.getString(3), rsCust.getString(1), listOfRewards);
		    objShowRewards.setVisible(true);
		    objShowRewards.setLocationRelativeTo(this);
		}
	    }
	    rsCust.close();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public clsRewards getCustomerRewards()
    {
	return objCustomerRewards;
    }

    public void setCustomerRewards(clsRewards objCustomerRewards)
    {
	try
	{
	    this.objCustomerRewards = objCustomerRewards;
	    if (objCustomerRewards.isItemOff())
	    {
		String rewardPOSItemCode = objCustomerRewards.getStrRewardPOSItemCode();
		boolean isItemSelected = false;
		for (int r = 0; r < tblItemTable.getRowCount(); r++)
		{
		    if (tblItemTable.getValueAt(r, 3).toString().equals(rewardPOSItemCode))
		    {
			isItemSelected = true;
		    }
		    else
		    {
			continue;
		    }
		}
		if (!isItemSelected)
		{
		    String sql = "select a.strItemCode,a.strItemName,a.strPriceMonday  "
			    + "from tblmenuitempricingdtl a "
			    + "where a.strItemCode='" + rewardPOSItemCode + "' "
			    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' ";
		    ResultSet rsItem = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsItem.next())
		    {
			String itemName = rsItem.getString(2);
			double rate = rsItem.getDouble(3);

			funInsertData(1.0, rate, rewardPOSItemCode, itemName, rate);
			funRefreshItemTable();
		    }
		    rsItem.close();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }

    private void funRefreshMenuForAreaWisePricingArea()
    {
	try
	{

	    String clsAreaCode = "";
	    if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
	    {
		clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		String areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		if (dineInForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
		}

		if (homeDeliveryForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
		}

		if (takeAwayForTax.equalsIgnoreCase("Y"))
		{
		    areaWisePricingAreaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
		}

		clsAreaCode = areaWisePricingAreaCode;
	    }

	    if (selectedMenuHeadName == null || selectedMenuHeadName.isEmpty())
	    {
		funResetItemNames();
		funResetItemButtonText(menuNames1[0]);
		if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
		{
		    funFillTopButtonList(menuHeadCode);
		}
	    }
	    else
	    {
		funResetItemNames();
		funResetItemButtonText(selectedMenuHeadName);
		if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
		{
		    funFillTopButtonList(menuHeadCode);
		}
	    }

	    if (clsGlobalVarClass.gAreaWisePricing.equalsIgnoreCase("Y"))
	    {
		List<clsDirectBillerItemDtl> tempItemDtlList = new ArrayList<>();
		tempItemDtlList.addAll(obj_List_ItemDtl);
		obj_List_ItemDtl.clear();
		serNo = 0;

		for (clsDirectBillerItemDtl listItemRow : tempItemDtlList)
		{

		    String itemCode = listItemRow.getItemCode();
		    String itemName = listItemRow.getItemName();
		    boolean isModifier = false;
		    if (itemName.startsWith("-->"))
		    {
			isModifier = true;
		    }
		    double oldQty = listItemRow.getQty();
		    double amt = listItemRow.getRate();
		    double purchaseRate = listItemRow.getPurchaseRate();

		    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);

		    int index = list_ItemNames_Buttoms.indexOf(itemName);
		    if (x.containsKey(itemName))
		    {
			clsItemPriceDtl priceObject = x.get(itemName);

			menuHeadCode = priceObject.getStrMenuHeadCode();
			String sql = "select strMenuCode,strMenuName from tblmenuhd where strMenuCode='" + menuHeadCode + "'";
			ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsMenuHead.next())
			{
			    selectedMenuHeadName = rsMenuHead.getString(2);
			}
			rsMenuHead.close();

			double itemPrice = funGetFinalPrice(priceObject);
			double purRate = priceObject.getDblPurchaseRate();

			funUpdateInsertedData(oldQty, itemPrice, itemCode, itemName, purRate);
		    }
		    else if (isModifier)
		    {
			Iterator<clsDirectBillerItemDtl> it = obj_List_ItemDtl.iterator();
			while (it.hasNext())
			{
			    clsDirectBillerItemDtl objItemDtlForThisModifier = it.next();
			    if (objItemDtlForThisModifier.getItemCode().equalsIgnoreCase(itemCode.substring(0, 7)))
			    {
				funUpdateInsertedData(oldQty, amt, itemCode, itemName, purchaseRate);
				break;
			    }
			}
		    }
		}
		funRefreshItemTable();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funUpdateInsertedData(double qty, double itemPrice, String itemCode, String itemName, double purchaseRate)
    {
	try
	{
	    double dblQty = new Double(String.valueOf(qty));
	    double dblPrice = itemPrice;
	    boolean flag = false;
	    if (obj_List_ItemDtl.size() > 0 && !flagOpenItem)
	    {
		for (clsDirectBillerItemDtl list_cls_ItemRow : obj_List_ItemDtl)
		{
		    String temp_itemCode = list_cls_ItemRow.getItemCode();
		    if (temp_itemCode.equalsIgnoreCase(itemCode) && list_cls_ItemRow.isIsModifier() == false && "N".equalsIgnoreCase(list_cls_ItemRow.getTdhComboItemYN()))
		    {
			double temp_qty = list_cls_ItemRow.getQty();
			double final_qty = temp_qty + dblQty;
			if (!clsGlobalVarClass.gNegBilling)
			{
			    if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, final_qty))
			    {
				return;
			    }
			}
			double amt = Double.parseDouble(gDecimalFormat.format(dblPrice * final_qty));
			list_cls_ItemRow.setRate(dblPrice);
			list_cls_ItemRow.setAmt(amt);
			list_cls_ItemRow.setQty(temp_qty + dblQty);
			flag = true;
		    }
		    if (flag)
		    {
			break;
		    }
		}
	    }

	    if (!flag)
	    {
		if (!clsGlobalVarClass.gNegBilling)
		{
		    if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, selectedQty))
		    {
			return;
		    }
		}

		if (clsGlobalVarClass.gAllowToCalculateItemWeight.equalsIgnoreCase("Y") && itemWeight > 0)
		{
		    double qtyPerBox = dblQty;
		    dblQty = (dblQty * noOfBoxes) / itemWeight;
		    //mapWeightPerBox.clear();
		    clsGlobalVarClass.gMapWeightPerBox.put(itemCode, noOfBoxes + "!" + qtyPerBox + "!" + itemName + "!" + dblPrice + "!" + dblQty);

		}

		if (itemName.startsWith("-->"))
		{
		    clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(itemName, itemCode, qty, dblPrice * dblQty, true, itemCode.split("M")[1], "", "", dblPrice, "N", getSeqNo().concat(".01"), purchaseRate);
		    obj_List_ItemDtl.add(obj_row);
		}
		else
		{
		    clsDirectBillerItemDtl ob = new clsDirectBillerItemDtl(itemName, itemCode, dblQty, dblPrice * dblQty, false, "", "N", "", dblPrice, "", getSeqNo(), purchaseRate);
		    if(obj_List_ItemDtl.size()>0){
			serNo++;
		    }
		    obj_List_ItemDtl.add(ob);
		}
	    }
	    selectedQty = 1;
	}
	catch (Exception ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    ex.printStackTrace();
	}
    }

    public double getDblDiscountAmt()
    {
	return dblDiscountAmt;
    }

    public void setDblDiscountAmt(double dblDiscountAmt)
    {
	this.dblDiscountAmt = dblDiscountAmt;
    }
    
    public double getHomeDelCharges()
    {
        return homeDelCharges;
    }

    public void setHomeDelCharges(double homeDelCharges)
    {
        this.homeDelCharges = homeDelCharges;
    }
    
    private String funGetAreaName(String areaCode){
	String areaName="";
	try{
	   
	    String sql = "select strAreaName from tblareamaster where strAreaCode='" + areaCode + "'";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		areaName = rsArea.getString(1);
		
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
	return areaName;
    }
}
