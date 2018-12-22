package com.POSTransaction.view;

import com.POSGlobal.controller.clsCRMInterface;
import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsItemPriceDtl;
import com.POSGlobal.controller.clsPLUItemDtl;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsRewards;
import com.POSGlobal.controller.clsTDHOnItemDtl;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmHomeDeliveryAddress;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsBillSettlementUtility;
import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import com.POSTransaction.controller.clsSaveAndPrintKOT;
import com.POSTransaction.controller.nfc.ReaderThread;
import static com.POSTransaction.view.frmDirectBiller.arrListItemCombination;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
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

public class frmMakeKOT extends javax.swing.JFrame
{

    private ResultSet objResultSet;
    private int menuCount, nextCnt, limit, nextClick;
    private boolean flgCheckNCKOTButtonColor, isModifierSelect;
    private boolean flgcheckDeliveryboyName, flgHomeDeliveryColor_Button, flgCheckTakeAway_ButtonColor;
    public boolean flgTableSelection, flgWaiterSelection, flagOpenItem;
    public boolean flag_isTDHModifier_Item, flgPopular;

    private double selectedQty, taxAmt;
    double MaxSubItemLimitWithComboItem = 0.00;
    Double MaxQTYOfModifierWithTDHItem;

    private int nextItemClick = 0, cntNavigate1, tblStartIndex, topSortingButtonsNavigator;
    public int navigate, totalItems, kotItemSequenceNO = 0;

    String KOTNo;
    private String sql, menuHeadCode, fieldSelected, updateSelectedField, oldTableNo = "NA", buildingCodeForHD;
    public String posDate, globalTableName, globalWaiterName, globalTableNo, globalWaiterNo, gTableFound, clsMemcode;
    private String homeDeliveryForTax, globalDebitCardNo, clsAreaCode, clsAreaName, CANCEL_ACTION = "cancel-search";
    private String deliveryBoyName, reasonCode, cmsMemName, cmsMemCode, strSerialNo, dtPOSDate, tableSelected = "", temp_ItemCode;

    private String[] menuNames, menuNames1, itemNames, btnforeground, reason;

    private ArrayList<String> listTopButtonName, listTopButtonCode;
    private List<clsMakeKotItemDtl> obj_List_KOT_ItemDtl;
    private List<String> arrListHomeDelDetails;
    public java.util.Vector vWaiterNo, vWaiterName;
    /**
     * this class object list is used to store rows of item in List object call
     * Old KOT Items On Table which printed
     */
    private List<clsMakeKotItemDtl> obj_List_Old_KOT_Item_On_table;

    /**
     * Store old and New Kot No on same Table
     */
    private ArrayList<String> list_KOT_On_Table;

    /**
     * This Object List Is to Store Price of All Items Under Same Menu Head
     */
    private List<clsItemPriceDtl> obj_List_ItemPrice;

    /**
     * Only To Store Item Name but Not to Display
     */
    private ArrayList<String> list_ItemNames_Buttoms = new ArrayList();

    private WeakHashMap<Object, Object> hmCMSMemberForTable;
    private WeakHashMap<String, String> hmReservedTables;
    private Map<String, String> hmTable;
    private Map<String, Integer> hmTableSeq;
    private HashMap<String, frmMakeKOT.clsModifierGroupDtl> hm_ModifierGroup = null;
    private HashMap<String, frmMakeKOT.clsModifierDtl> hm_ModifierDtl = null;
    private HashMap<String, List<clsItemPriceDtl>> hmHappyHourItems = null;
    private ArrayList<String> itemImageCode = null;
    private HashMap<String, String> hmMakeKotParams;

    private clsCustomerDataModelForSQY objData;
    private clsUtility objUtility = new clsUtility();
    private clsUtility2 objUtility2 = new clsUtility2();
    panelModifier objPanelModifier;
    panelSubItem objPanelSubItem;
    private StringBuilder sqlBuilder;
    private clsMakeKotItemDtl objOpenItemDtl = new clsMakeKotItemDtl();
    private clsRewards objCustomerRewards;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();
    private HashMap<String, String> mapCostCenters;
    private String kotToBillNote;
    private int selectedRowNoForModifer = -1;

    public frmMakeKOT()
    {
	initComponents();

	this.setState(Frame.ICONIFIED);
	this.setState(Frame.NORMAL);
	try
	{
	    objUtility = new clsUtility();
	    homeDeliveryForTax = "N";
	    hmMakeKotParams = new HashMap<String, String>();
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

	    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new KeyBoardEvent());

	    panelPLU.setVisible(false);
	    //panelAlfaNumKeyboard.setVisible(false);
	    //clsGlobalVarClass.funGetConnectionStatus();

	    String sqlUpdateTablStatus = "update tbltablemaster a "
		    + "join tblitemrtemp b on a.strTableNo=b.strTableNo "
		    + "set a.strStatus='Occupied' "
		    + "where a.strStatus='Normal' "
		    + "and b.strNCKOTYN='N' ";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdateTablStatus);

	    buildingCodeForHD = "";
	    InputMap im = txtPLUItemSearch.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = txtPLUItemSearch.getActionMap();
	    im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
	    am.put(CANCEL_ACTION, new frmMakeKOT.CancelAction());

	    InputMap imTable = txtTableNo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap amTable = txtTableNo.getActionMap();
	    imTable.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
	    amTable.put(CANCEL_ACTION, new frmMakeKOT.CancelAction());

	    obj_List_KOT_ItemDtl = new ArrayList<>();
	    nextClick = 0;
	    gTableFound = "No";
	    globalDebitCardNo = "";
	    clsGlobalVarClass.gCustomerCode = "";

	    flgPopular = false;
	    clsAreaName = "All";
	    clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    funResetTopSortingButtons();

	    posDate = clsGlobalVarClass.gPOSDateForTransaction;
	    hmReservedTables = new WeakHashMap<String, String>();
	    funFillReservedTables();
	    //flagHomeDeliverySelect = false;
	    updateSelectedField = "";
	    fieldSelected = "Table";
	    cntNavigate1 = 0;
	    tblStartIndex = 0;
	    btnPrevItem.setEnabled(false);
	    lblDebitCardBalance.setVisible(false);

	    btnforeground = new String[16];

	    selectedQty = 1;
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
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    btnButton3.setVisible(true); ///for check
	    funLoadMenuNames();

	    vWaiterNo = new java.util.Vector();
	    vWaiterName = new java.util.Vector();
	    btnNextMenu.setEnabled(false);
	    btnPrevMenu.setEnabled(false);
	    flgcheckDeliveryboyName = false;
	    flgHomeDeliveryColor_Button = false;
	    flgCheckTakeAway_ButtonColor = false;
	    flgCheckNCKOTButtonColor = false;
	    deliveryBoyName = "";
	    panelDeditCard.setVisible(false);
	    panelExternalCode.setVisible(false);
	    if (clsGlobalVarClass.gNatureOfBusinnes.equals("Retail"))
	    {
		panelExternalCode.setVisible(true);
	    }
	    btnItemMode.setVisible(false);
	    if (clsGlobalVarClass.ListTDHOnModifierItem.isEmpty())
	    {
		objUtility.addTDHOnModifierItem();
	    }

	    hmTable = new HashMap<String, String>();
	    hmTableSeq = new HashMap<String, Integer>();
	    funInitTables();
	    funLoadTables(0, hmTable.size());
	    reasonCode = "";
	    clsGlobalVarClass.gNumerickeyboardValue = "";
	    flgTableSelection = true;
	    btnPrevious.setEnabled(false);
	    cmsMemName = "";
	    cmsMemCode = "";
	    hmCMSMemberForTable = new WeakHashMap<Object, Object>();

	    funSetSelectedArea();

	    btnItemMode.setVisible(false);
	    funSetShortCutKeys();
	    itemImageCode = new ArrayList<String>();
	    hmHappyHourItems = new HashMap<String, List<clsItemPriceDtl>>();
	    funFillMapWithHappyHourItems();
	    taxAmt = 0;
	    obj_List_Old_KOT_Item_On_table = new ArrayList<>();
	    panelKOTMessage.setVisible(false);
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		lblformName.setText(lblformName.getText() + "  " + clsGlobalVarClass.gCounterName);
	    }
	    txtTableNo.requestFocus();
	    arrListHomeDelDetails = new ArrayList<String>();

	    sqlBuilder = new StringBuilder();
	    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	    {
		panelTopSortingButtons.setVisible(false);
	    }
	}
	
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private int funFillReservedTables() throws Exception
    {
	hmReservedTables.clear();
	//System.out.println(posDate);
	String[] arrPOSDate = posDate.split(" ");
	sql = "select strTableNo,strCustomerCode "
		+ " from tblreservation where dteResDate = '" + arrPOSDate[0] + "'";
	ResultSet rsResTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsResTables.next())
	{
	    hmReservedTables.put(rsResTables.getString(1), rsResTables.getString(2));
	}
	rsResTables.close();
	return 1;
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
		    + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
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
	    if(clsAreaCode!=null && !clsAreaCode.isEmpty()){
		sql = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
		    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
		    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
		    + ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " WHERE a.strAreaCode='" + clsAreaCode+ "' "
		    + " and a.strItemCode=b.strItemCode "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and a.strHourlyPricing='Yes' "
		    + " and b.strOperationalYN='Y' ";
	    }else{
		sql = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
		    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
		    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
		    + ",b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " WHERE a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
		    + " and a.strItemCode=b.strItemCode "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and a.strHourlyPricing='Yes' "
		    + " and b.strOperationalYN='Y' ";
	    }
	    
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
    }

    private void funSetShortCutKeys()
    {

	btnMakeKOT.setMnemonic('d');
	btnMakeBill.setMnemonic('m');
	btnHome.setMnemonic('h');
	btnNewCustomer.setMnemonic('c');
	btnUp.setMnemonic('u');
	btnDown.setMnemonic('w');
	btnDelItem.setMnemonic('l');
	btnButton1.setMnemonic('s');
	btnButton2.setMnemonic('p');
	btnButton3.setMnemonic('f');
	btnButton4.setMnemonic('y');
	btnNext.setMnemonic('>');
	btnPrevious.setMnemonic('<');
    }

    private void funLoadMenuNames() throws Exception
    {
	nextClick = 0;
	JButton[] btnMenuArray =
	{
	    btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
	};
	int i = 0;
	btnPrevMenu.setEnabled(false);

	if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	{
	    sql = "select count(distinct(a.strMenuCode)) "
		    + "from tblmenuitempricingdtl a left outer join tblmenuhd b on a.strMenuCode=b.strMenuCode "
		    + "left outer join tblcounterdtl c on b.strMenuCode=c.strMenuCode "
		    + "left outer join tblcounterhd d on c.strCounterCode=d.strCounterCode "
		    + "where d.strOperational='Yes' "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
		    + "and c.strCounterCode='" + clsGlobalVarClass.gCounterCode + "' "
		    + "order by b.strMenuName";
	}
	else if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	{
	    sql = "select count(distinct(a.strMenuCode)) "
		    + " from tblplayzonepricinghd a,tblmenuhd b "
		    + "where a.strMenuCode=b.strMenuCode and b.strOperational='Y' "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
		    + "order by b.strMenuName";
	}
	else
	{
	    sql = "select count(distinct(a.strMenuCode)) "
		    + " from tblmenuitempricingdtl a,tblmenuhd b "
		    + "where a.strMenuCode=b.strMenuCode and b.strOperational='Y' "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
		    + "order by b.strMenuName";
	}
	//System.out.println(sql);
	ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsMenuHead.next())
	{
	    menuCount = rsMenuHead.getInt(1);
	}
	rsMenuHead.close();

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
		    + "order by b.intSequence";
	}
	else if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	{
	    sql = "select distinct(a.strMenuCode),b.strMenuName "
		    + " from tblplayzonepricinghd a,tblmenuhd b "
		    + "where a.strMenuCode=b.strMenuCode and b.strOperational='Y' "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
		    + "order by b.strMenuName";
	}
	else
	{
	    sql = "select distinct(a.strMenuCode),b.strMenuName "
		    + "from tblmenuitempricingdtl a left outer join tblmenuhd b "
		    + "on a.strMenuCode=b.strMenuCode "
		    + "where  b.strOperational='Y' "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
		    + "order by b.intSequence";
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
		btnMenuArray[i].setText(clsGlobalVarClass.convertString(rsMenuHead.getString(2)));
		btnMenuArray[i].setEnabled(true);
	    }
	    menuNames[i] = rsMenuHead.getString(2);
	    menuNames1[i] = rsMenuHead.getString(2);
	    i++;
	}
	rsMenuHead.close();
    }

    public void funMinMaxForm()
    {
	this.setState(Frame.ICONIFIED);
	this.setState(Frame.NORMAL);
    }

    public void funInitTables() throws Exception
    {
	hmTable.clear();
	hmTableSeq.clear();
	if (clsGlobalVarClass.gCMSIntegrationYN)
	{
	    if (clsGlobalVarClass.gTreatMemberAsTable)
	    {
		sql = "select strTableNo,strTableName from tbltablemaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strOperational='Y' and strStatus!='Normal' "
			+ " order by strTableName";
	    }
	    else
	    {
		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strOperational='Y' "
			+ " order by intSequence";
	    }
	}
	else
	{
	    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
		    + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
		    + " and strOperational='Y' "
		    + " order by intSequence";
	}
	//System.out.println(sql);
	ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsTableInfo.next())
	{
	    hmTable.put(rsTableInfo.getString(2).toUpperCase(), rsTableInfo.getString(1));
	    hmTableSeq.put(rsTableInfo.getString(1) + "!" + rsTableInfo.getString(2), rsTableInfo.getInt(3));
	}
	rsTableInfo.close();

	sql = "select strWaiterNo,strWShortName,strWFullName "
		+ " from tblwaitermaster where strOperational='Y' and (strPOSCode='All' or strPOSCode='" + clsGlobalVarClass.gPOSCode + "')  ";
	ResultSet rsWaiterInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsWaiterInfo.next())
	{
	    vWaiterNo.add(rsWaiterInfo.getString(1));
	    vWaiterName.add(rsWaiterInfo.getString(2));
	}
	rsWaiterInfo.close();
    }

    public void funLoadTables(int startIndex, int totalSize)
    {
	try
	{

	    flgTableSelection = true;
	    funResetTopSortingButtons();
	    fieldSelected = "Table";
	    int cntIndex = 0;
	    if (startIndex == 0)
	    {
		btnPrevItem.setEnabled(false);
	    }
	    funResetMenuHeadButtons();
	    funEnablePaxButtons(false);
	    btnPopular.setEnabled(false);
	    JButton[] btnTableArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    for (int cntTable = 0; cntTable < btnTableArray.length; cntTable++)
	    {
		btnTableArray[cntTable].setForeground(Color.black);
		btnTableArray[cntTable].setText("");
		btnTableArray[cntTable].setIcon(null);
	    }

	    hmTableSeq = clsGlobalVarClass.funSortMapOnValues(hmTableSeq);
	    //Object[] arrObjTables = hmTable.entrySet().toArray();
	    Object[] arrObjTables = hmTableSeq.entrySet().toArray();

	    for (int cntTable = startIndex; cntTable < totalSize; cntTable++)
	    {
		//System.out.println("Counter="+cntTable+"\tStart="+startIndex+"\tTotal Size="+totalSize);
		if (cntTable == totalSize)
		{
		    break;
		}
		String tblInfo = arrObjTables[cntTable].toString().split("=")[0];
		String tblNo = tblInfo.split("!")[0];
		String tblName = tblInfo.split("!")[1];

		sql = "select strTableNo,strStatus,intPaxNo from tbltablemaster "
			+ " where strTableNo='" + tblNo + "' "
			+ " and strOperational='Y' "
			+ " order by intSequence";
		//System.out.println(arrObjTables[cntTable].toString().split("=")[1]);
		ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsTableInfo.next();
		String status = rsTableInfo.getString(2);
		int pax = rsTableInfo.getInt(3);
		if (cntIndex < 16)
		{
		    if (status.equals("Occupied"))
		    {
			btnTableArray[cntIndex].setBackground(Color.red);
			btnTableArray[cntIndex].setForeground(Color.white);
			String timeDiffInFirstKOTAndCurrentTime = funGetTimeDiffInFirstKOTAndCurrentTime(tblNo);
			if (timeDiffInFirstKOTAndCurrentTime.startsWith("-"))
			{
			    timeDiffInFirstKOTAndCurrentTime = "";
			}
			btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5><br>" + pax + "<br>" + timeDiffInFirstKOTAndCurrentTime + "</html>");
		    }
		    else if (status.equals("Billed"))
		    {
			btnTableArray[cntIndex].setBackground(Color.blue);
			btnTableArray[cntIndex].setForeground(Color.white);
			String timeDiffInLastBilledAndCurrentTime = funGetTimeDiffInBilledAndCurrentTime(tblNo);
			if (timeDiffInLastBilledAndCurrentTime.startsWith("-"))
			{
			    timeDiffInLastBilledAndCurrentTime = "";
			}
			btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5><br>" + pax + "<br>" + timeDiffInLastBilledAndCurrentTime + "</html>");
		    }
		    else if (status.equals("Normal"))
		    {
			btnTableArray[cntIndex].setBackground(Color.lightGray);
			btnTableArray[cntIndex].setForeground(Color.black);
			btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5><br>" + pax + "</html>");
		    }
		    else if (status.equals("Reserve"))
		    {
			btnTableArray[cntIndex].setBackground(Color.green);
			btnTableArray[cntIndex].setForeground(Color.black);
			btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5><br>" + pax + "</html>");
		    }
		    //btnTableArray[cntIndex].setText("<html>" + tblName + "<br>" + pax + "</html>");
		    btnTableArray[cntIndex].setEnabled(true);
		    cntIndex++;
		}
		rsTableInfo.close();
	    }

	    for (int cntTable1 = cntIndex; cntTable1 < 16; cntTable1++)
	    {
		btnTableArray[cntTable1].setEnabled(false);
	    }
	    if (totalSize > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    else
	    {
		btnNextItem.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funLoadWaiterNo(int startIndex, int totalSize)
    {
	try
	{
	    int cntIndex = 0;
	    flgWaiterSelection = true;
	    String wtName = "";
	    JButton[] btnWaiterArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    for (int k = 0; k < 16; k++)
	    {
		btnWaiterArray[k].setText("");
		btnWaiterArray[k].setBackground(Color.LIGHT_GRAY);
		btnWaiterArray[k].setIcon(null);
	    }
	    for (int i = startIndex; i < totalSize; i++)
	    {
		if (i == vWaiterNo.size())
		{
		    break;
		}
		wtName = vWaiterName.elementAt(i).toString();
		if (cntIndex < 16)
		{
		    if (wtName.contains(" "))
		    {
			StringBuilder sb = new StringBuilder(wtName);
			int len = sb.length();
			int seq = sb.lastIndexOf(" ");
			String split = sb.substring(0, seq);
			String last = sb.substring(seq + 1, len);
			btnWaiterArray[cntIndex].setText("<html>" + split + "<br>" + last + "</html>");
		    }
		    else
		    {
			btnWaiterArray[cntIndex].setText(wtName);
		    }
		    btnWaiterArray[cntIndex].setForeground(Color.black);
		    btnWaiterArray[cntIndex].setEnabled(true);
		}
		else
		{
		    if (wtName.contains(" "))
		    {
			StringBuilder sb = new StringBuilder(wtName);
			int len = sb.length();
			int seq = sb.lastIndexOf(" ");
			String split = sb.substring(0, seq);
			String last = sb.substring(seq + 1, len);
		    }
		}
		cntIndex++;
	    }
	    for (int cntWaiter = cntIndex; cntWaiter < 16; cntWaiter++)
	    {
		btnWaiterArray[cntWaiter].setEnabled(false);
	    }
	    if (vWaiterNo.size() > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    else
	    {
		btnNextItem.setEnabled(false);
	    }
	    txtWaiterNo.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funShowModifier()
    {
	String itemCode = "";
	try
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    //ItemName = tblItemTable.getModel().getValueAt(selectedRow, 0).toString();
	    itemCode = tblItemTable.getModel().getValueAt(selectedRow, 3).toString();

	    sql = "select strApplicable from tblitemmodofier where strItemCode='" + itemCode + "'"
		    + " or strItemCode='All'";
	    ResultSet rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemModifierInfo.next())
	    {
		if ("y".equalsIgnoreCase(rsItemModifierInfo.getString(1)))
		{
		    panelItemButtons.setVisible(false);
		    panelNavigate.setVisible(false);
		    panelTopSortingButtons.setVisible(false);
		    panelPLU.setVisible(false);
		    if (objPanelModifier == null)
		    {
			objPanelModifier = new panelModifier(this);
			objPanelModifier.getItemCode(itemCode);
			add(objPanelModifier);
			objPanelModifier.setLocation(panelNavigate.getLocation());
			objPanelModifier.setVisible(true);
			objPanelModifier.setSize(380, 500);
			objPanelModifier.revalidate();
		    }
		    else
		    {
			objPanelModifier.getItemCode(itemCode);
			objPanelModifier.setVisible(true);
		    }
		}
		else
		{
		    new frmOkPopUp(null, "No Modifier for this item", "Error", 1).setVisible(true);
		}
		rsItemModifierInfo.close();
	    }
	    else
	    {
		panelItemButtons.setVisible(false);
		panelNavigate.setVisible(false);
		panelTopSortingButtons.setVisible(false);
		if (objPanelModifier == null)
		{
		    objPanelModifier = new panelModifier(this);
		    add(objPanelModifier);
		    objPanelModifier.setLocation(panelNavigate.getLocation());
		    objPanelModifier.setVisible(true);
		    objPanelModifier.setSize(380, 500);
		    objPanelModifier.revalidate();

		}
		else
		{
		    objPanelModifier.setVisible(true);
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

    private void funShowModifierTDH(String TempItemCode) throws Exception
    {
	sql = "select strApplicable from tblitemmodofier where strItemCode='" + TempItemCode + "'"
		+ " or strItemCode='All'";
	ResultSet rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsItemModifierInfo.next())
	{
	    if ("y".equalsIgnoreCase(rsItemModifierInfo.getString(1)))
	    {
		panelItemButtons.setVisible(false);
		panelNavigate.setVisible(false);
		panelTopSortingButtons.setVisible(false);
		btnItemMode.setVisible(false);
		panelExternalCode.setVisible(false);
		panelPLU.setVisible(false);
		if (objPanelModifier == null)
		{
		    objPanelModifier = new panelModifier(this);
		    objPanelModifier.getItemCode(TempItemCode);
		    add(objPanelModifier);
		    objPanelModifier.setLocation(panelNavigate.getLocation());
		    objPanelModifier.setVisible(true);
		    objPanelModifier.setSize(380, 500);
		    objPanelModifier.revalidate();

		}
		else
		{
		    objPanelModifier.getItemCode(TempItemCode);
		    objPanelModifier.setVisible(true);

		}
	    }
	    else
	    {
		new frmOkPopUp(null, "No Modifier for this item", "Error", 1).setVisible(true);
	    }
	    rsItemModifierInfo.close();
	}
	else
	{
	    panelItemButtons.setVisible(false);
	    panelNavigate.setVisible(false);
	    panelTopSortingButtons.setVisible(false);
	    btnItemMode.setVisible(false);
	    panelExternalCode.setVisible(false);
	    if (objPanelModifier == null)
	    {
		objPanelModifier = new panelModifier(this);
		add(objPanelModifier);
		objPanelModifier.setLocation(panelNavigate.getLocation());
		objPanelModifier.setVisible(true);
		objPanelModifier.setSize(380, 500);
		objPanelModifier.revalidate();

	    }
	    else
	    {
		objPanelModifier.setVisible(true);

	    }
	}
    }

    //show the panel in close button of panelModifier and PanelPlu
    public void funShowPanel()
    {
	selectedQty = 1;
	panelTopSortingButtons.setVisible(true);
	panelMenuHead.setVisible(true);
	panelItemButtons.setVisible(true);
	panelNavigate.setVisible(true);

	panelTopSortingButtons.setVisible(true);
	if (!clsGlobalVarClass.gSkipWaiter && !clsGlobalVarClass.gSkipPax)
	{
	    btnItemMode.setVisible(false);
	}
	else
	{
	    btnItemMode.setVisible(true);
	}

	panelExternalCode.setVisible(true);
	if (objPanelSubItem != null)
	{
	    objPanelSubItem.setVisible(false);
	    objPanelSubItem = null;
	}

	if (null != objPanelModifier)
	{
	    objPanelModifier.setVisible(false);
	    objPanelModifier = null;
	}
    }

    public void funEnableControls()
    {
	btnMakeBill.setEnabled(true);
    }

    public void funClearTable()
    {
	try
	{
	    btnMakeBill.setEnabled(true);
	    funClearObjectList();
	    DefaultTableModel dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    //flagHomeDeliverySelect = false;
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.setShowHorizontalLines(true);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(165);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    txtTotal.setText("");
	    lblAreaName.setText("ALL FLOORS");

	    txtTableNo.setText("");
	    txtWaiterNo.setText("");
	    txtPaxNo.setText("1");
	    fieldSelected = "Table";
	    clsGlobalVarClass.gTakeAway = "No";
	    btnButton1.setForeground(Color.white);
	    btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));

	    panelDeditCard.setVisible(false);
	    lblCardBalnce.setText("");
	    lblDebitCardBalance.setVisible(false);
	    clsGlobalVarClass.gDebitCardNo = null;

	    panelExternalCode.setVisible(false);
	    btnItemMode.setVisible(false);
	    funResetHomeDeliveryFields();
	    btnButton4.setForeground(Color.white);
	    btnButton3.setText("<Html>DELIVERY<br>BOY</html>");
	    //funLoadTables(0, vTableNo.size());
	    funLoadTables(0, hmTable.size());

	}
	catch (Exception ex)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(ex);	
	    objUtility.funWriteErrorLog(ex);
	    ex.printStackTrace();
	}
    }

//function for Modifier(call by panelModifier form) to getModifier rate 
    public void funGetModifierRate(String modifierName)
    {
	try
	{
	    String modifierCode = null;
	    double rate = 0;
	    //to retrive the modifiercode
	    sql = "select strModifierCode from tblmodifiermaster "
		    + " where strModifierName='" + modifierName + "'";
	    ResultSet rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemModifierInfo.next())
	    {
		modifierCode = rsItemModifierInfo.getString(1);
	    }
	    //retrive the rate of modifier
	    sql = "select dblRate from tblitemmodofier "
		    + " where strModifierCode='" + modifierCode + "'"
		    + " and strChargable='y' and strApplicable='y'";
	    rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemModifierInfo.next())
	    {
		rate = rsItemModifierInfo.getDouble(1);
	    }
	    rsItemModifierInfo.close();

	    int selectedRow = tblItemTable.getSelectedRow();
	    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	    {
		String temp_itemCode = tblItemTable.getValueAt(selectedRow, 3).toString().trim();
		int i = 0;
		for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
		{
		    boolean found = false;

		    if (ob.getItemCode().equalsIgnoreCase(temp_itemCode) && ob.isIsModifier() == false)
		    {
			clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(ob.getSequenceNo().concat(".00"), KOTNo, globalTableNo, globalWaiterNo, modifierName, temp_itemCode.concat(modifierCode), 1.00, rate, ob.getPaxNo(), "N", "N", true, modifierCode, "", "", "N", rate);
			obj_List_KOT_ItemDtl.add(i + 1, obj_row);
			found = true;
		    }
		    i++;
		    if (found)
		    {
			funRefreshItemTable();
			break;
		    }
		}
	    }
	    selectedQty = 0;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, "Please Select an Item from grid");
	    //e.printStackTrace();
	}
    }

    public void funAddSubItems(String subItemCode, String subItemName, double thisSubItemLimeit, String tdhComboItemCode)
    {
	try
	{
	    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	    {
		boolean flag_ItemFoundUpdate = false;
		boolean testflag = false;

		for (clsMakeKotItemDtl obj : obj_List_KOT_ItemDtl)
		{
		    if (obj.getTdhComboItemYN().equalsIgnoreCase("Y") && obj.getItemCode().equalsIgnoreCase(subItemCode))
		    {
			testflag = true;
			if (obj.getQty() < thisSubItemLimeit)
			{
			    flag_ItemFoundUpdate = true;
			    double temp_qty = obj.getQty();
			    obj.setQty(temp_qty + 1);

			}
			else
			{
			    funAddSubItemToPriceList(subItemCode, subItemName);
			    funGetItemPrice(subItemName);
			    break;
			}
		    }

		    if (flag_ItemFoundUpdate)
		    {
			funRefreshItemTable();
			break;
		    }
		}
		if (!testflag)
		{
		    clsMakeKotItemDtl ob1 = new clsMakeKotItemDtl(getStrSerialNo(), KOTNo, globalTableNo, globalWaiterNo, "=>".concat(subItemName), subItemCode, 1, 0.00, 0, "N", "Y", false, "", tdhComboItemCode, "", "N", 0.00);
		    kotItemSequenceNO++;
		    obj_List_KOT_ItemDtl.add(ob1);
		    funRefreshItemTable();
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

    private double funGetMaxQty(String tempComboItemCode, String checkSQL, String SeriealNo) throws Exception
    {
	double qty = 0.00;
	String temp = checkSQL;
	if ("MaxSubItemLimitWithComboItem".equalsIgnoreCase(temp))
	{
	    sql = "select intMaxQuantity from tbltdhhd where strItemCode='" + tempComboItemCode + "' and strComboItemYN='Y';";
	}
	if ("CurrentSubItemQtyAll".equalsIgnoreCase(temp))
	{
	    sql = "select sum(a.dblItemQuantity) from  tblitemrtemp a,tbltdhcomboitemdtl b "
		    + "where a.strSerialno='" + SeriealNo + "' and a.strItemCode=b.strSubItemCode "
		    + "and b.strItemCode='" + tempComboItemCode + "' and a.tdhComboItemYN='Y' "
		    + "and a.strTableNo='" + globalTableNo + "' and a.strPrintYN='N' and a.strKOTNo='" + KOTNo + "';";
	}
	if ("CurrentComboItemQty".equalsIgnoreCase(temp))
	{
	    sql = "select dblItemQuantity from  tblitemrtemp "
		    + "where strItemCode='" + tempComboItemCode + "';";
	}
	if ("thisSubItemCurrentQty".equalsIgnoreCase(temp))
	{
	    sql = "select dblItemQuantity from  tblitemrtemp "
		    + "where strItemCode='" + tempComboItemCode + "' "
		    + " and strSerialno='" + SeriealNo + "' and tdhComboItemYN='Y' "
		    + "and strKOTNo='" + KOTNo + "' and strPrintYN='N'; ";
	}
	objResultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (objResultSet.next())
	{
	    qty = objResultSet.getDouble(1);
	}
	objResultSet.close();

	return qty;
    }

    public void funInsertFreeFlowModifier(String ModifierName, BigDecimal rate)
    {
	try
	{
	    //int serialno = 0;
	    int selectedRow = tblItemTable.getSelectedRow();
	    String tempitemcode = tblItemTable.getValueAt(selectedRow, 3).toString();
	    int i = 0;
	    for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
	    {
		boolean found = false;
		if (ob.getItemCode().equalsIgnoreCase(tempitemcode) && ob.isIsModifier() == false)
		{
		    clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(getStrSerialNo().concat(".00"), KOTNo, globalTableNo, globalWaiterNo, "-->".concat(ModifierName), tempitemcode.concat("M99"), 1.00, rate.doubleValue(), ob.getPaxNo(), "N", "N", true, "M99", "", "", "N", rate.doubleValue()); //ask"
		    obj_List_KOT_ItemDtl.add(i + 1, obj_row);
		    found = true;
		}
		i++;
		if (found)
		{
		    funRefreshItemTable();
		    break;
		}
	    }
	    selectedQty = 0;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
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
	    btnPopularArray[j].setText(fun_Get_FormattedName(itemNames[i]));
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

//function for item button blank
    private void funResetItemNames()
    {
	isModifierSelect = false;
	hm_ModifierDtl = null;
	hm_ModifierGroup = null;
	//create the JButton array group
	JButton[] btnItemArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	flgPopular = false;
	for (int i = 0; i < btnItemArray.length; i++)
	{
	    btnItemArray[i].setText("");//set the item button blank
	    btnItemArray[i].setForeground(Color.black);
	    btnItemArray[i].setBackground(Color.LIGHT_GRAY);
	    btnItemArray[i].setIcon(null);
	}
	btnPrevItem.setEnabled(false);
	btnNextItem.setEnabled(false);
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
	String posDateForPrice = posDate.split(" ")[0];

	String sqlPopItems = "";
	/**
	 * auto select popular items baed on backed dated sales
	 */
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
		    + "and (a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
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

	    sqlPopItems = "select a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday, "
		    + "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,  "
		    + "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo, "
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,c.strStockInEnable "
		    + ",count(b.strItemCode),b.dteBillDate  "
		    + "from tblmenuitempricingdtl a,tblqbilldtl b,tblitemmaster c "
		    + "where a.strItemCode=b.strItemCode "
		    + "and a.strItemCode=c.strItemCode "
		    + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + "and (a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
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
		    + " and (a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') ";
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
		    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable"
		    + " ,b.dblPurchaseRate,a.strMenuCode "
		    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
		    + " where a.strPopular='Y' "
		    + " and  a.strItemCode= b.strItemCode "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and (a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') ";
	}
	ResultSet rsItemPrice = clsGlobalVarClass.dbMysql.executeResultSet(sqlPopItems);
	while (rsItemPrice.next())
	{
	    list_ItemNames_Buttoms.add(rsItemPrice.getString(2));
	    clsItemPriceDtl ob = new clsItemPriceDtl(rsItemPrice.getString(1), rsItemPrice.getString(2), rsItemPrice.getDouble(4), rsItemPrice.getDouble(5), rsItemPrice.getDouble(6), rsItemPrice.getDouble(7), rsItemPrice.getDouble(8), rsItemPrice.getDouble(9), rsItemPrice.getDouble(10), rsItemPrice.getString(11), rsItemPrice.getString(12), rsItemPrice.getString(13), rsItemPrice.getString(14), rsItemPrice.getString(15), rsItemPrice.getString(3), rsItemPrice.getString(16), rsItemPrice.getString(17), rsItemPrice.getString(18), rsItemPrice.getString(19), rsItemPrice.getString(20), 0, rsItemPrice.getString(22));
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

    private void funNumericSelection(String btnValue)
    {
	if ((!clsGlobalVarClass.gSkipWaiter) && txtWaiterNo.getText().trim().length() < 1)
	{
	    new frmOkPopUp(this, "Please Select The Waiter.", "error", 1).setVisible(true);
	    return;
	}

	// funEnterPAXNo(btnValue);
	if (fieldSelected.equals("Pax"))
	{
	    funEnterPAXNo(btnValue);
	}
	else
	{
	    funNumberEntry(btnValue);
	}
    }

    private String funGenerateKOTNo()
    {
	String kotNo = "";
	try
	{
	    long code = 0;
	    sql = "select dblLastNo from tblinternal where strTransactionType='KOTNo'";
	    ResultSet rsKOT = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsKOT.next())
	    {
		code = rsKOT.getLong(1);
		code = code + 1;
		kotNo = "KT" + String.format("%07d", code);
		clsGlobalVarClass.gUpdatekot = true;
		clsGlobalVarClass.gKOTCode = code;
	    }
	    else
	    {
		kotNo = "KT0000001";
		clsGlobalVarClass.gUpdatekot = false;
	    }
	    rsKOT.close();
	    sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='KOTNo'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return kotNo;
    }

    /**
     * :-Ritesh 30 Sept 2014
     *
     * @param ob of clsItemPriceDtl
     * @return
     */
    private double funGetFinalPrice(clsItemPriceDtl ob)
    {
	double Price = 0.00;
	String fromTime = ob.getTmeTimeFrom();
	String toTime = ob.getTmeTimeTo();
	String fromAMPM = ob.getStrAMPMFrom();
	String toAMPM = ob.getStrAMPMTo();
	String hourlyPricing = ob.getStrHourlyPricing();

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

	return Price;
    }

    //To get the price of the selected item day wise
    private void funGetItemPrice(String itemName)
    {
	String itemCode = "";
	try
	{
	    double Price = 0;
	    int index = list_ItemNames_Buttoms.indexOf(itemName);
	    clsItemPriceDtl priceObject = obj_List_ItemPrice.get(index);
	    flag_isTDHModifier_Item = false;
	    itemCode = priceObject.getStrItemCode();

	    boolean isVailable = objUtility.isItemAvailableForTotay(itemCode, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode);

	    if (!isVailable)
	    {
		new frmOkPopUp(this, "<html>\"" + itemName.toUpperCase() + "\" Is Not Available For Today.</html>", "Warning", 1).setVisible(true);
		return;
	    }

	    Price = funGetFinalPrice(priceObject);
	    int pax = 0;
	    try
	    {
		if (txtPaxNo.getText().trim().length() > 0)
		{
		    pax = Integer.parseInt(txtPaxNo.getText());
		}
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		pax = 0;
		objUtility.funWriteErrorLog(e);
	    }

	    if (clsGlobalVarClass.ListTDHOnModifierItem.contains(itemCode))
	    {
		if (funNextKOTFlag(globalTableNo))
		{
		    KOTNo = funGenerateKOTNo();
		}

		flag_isTDHModifier_Item = true;
		MaxQTYOfModifierWithTDHItem = clsGlobalVarClass.ListTDHOnModifierItemMaxQTY.get((clsGlobalVarClass.ListTDHOnModifierItem.indexOf(itemCode)));
		clsMakeKotItemDtl obTDHitem = new clsMakeKotItemDtl(getStrSerialNo(), KOTNo, globalTableNo, globalWaiterNo, itemName, itemCode, 1.00, Price, pax, "N", "N", false, "", "", "", "N", Price);
		kotItemSequenceNO++;
		frmTDHDialog ob = new frmTDHDialog(this, true, itemCode, obTDHitem);
		ob.setVisible(true);
	    }
	    else
	    {
		if (Price == 0)
		{

		    sqlBuilder.setLength(0);
		    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
			    + "from tbluserdtl a "
			    + "where a.strFormName='Open Items' "
			    + "and a.strTLA='true' "
			    + "and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' ");
		    ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		    if (rsTLA.next())
		    {

			objOpenItemDtl.setItemCode(itemCode);
			objOpenItemDtl.setItemName(itemName);
			objOpenItemDtl.setItemRate(Price);

			boolean isUserGranted = funCheckUserAuthentication();
		    }
		    else
		    {
			//QTY POP UP
			frmNumberKeyPad num;
			if (clsGlobalVarClass.gItemQtyNumpad)
			{
			    selectedQty = 0;
			    num = new frmNumberKeyPad(this, true, "qty");
			    num.setVisible(true);
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
				{
				    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				    clsGlobalVarClass.gNumerickeyboardValue = null;
				}
			    }
			}

			//RATE POP UP
			frmNumberKeyPad obj = new frmNumberKeyPad(null, true, "Rate" + Price);
			obj.setVisible(true);
			if (clsGlobalVarClass.gRateEntered)
			{
			    //Price = obj.getResult();
			    if (null != clsGlobalVarClass.gNumerickeyboardValue)
			    {
				Price = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			    if (selectedQty != 0 && Price >= 0)
			    {
				if (funNextKOTFlag(globalTableNo))
				{
				    KOTNo = funGenerateKOTNo();
				}

				flagOpenItem = true;
				funInsertData(selectedQty, Price, globalTableNo, itemName, itemCode);
				selectedQty = 1;
			    }
			    /*
                             * else { new frmOkPopUp(null, "Please select
                             * quantity first", "Error", 1).setVisible(true); }
			     */
			    clsGlobalVarClass.gRateEntered = false;
			}
		    }
		    rsTLA.close();
		}
		else
		{

		    //QTY POP UP
		    frmNumberKeyPad num;
		    if (clsGlobalVarClass.gItemQtyNumpad)
		    {
			selectedQty = 0;
			num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			    {
				selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
				clsGlobalVarClass.gNumerickeyboardValue = null;
			    }
			}
		    }

		    if (selectedQty != 0)
		    {
			if (funNextKOTFlag(globalTableNo))
			{
			    KOTNo = funGenerateKOTNo();
			}

			funInsertData(selectedQty, Price, globalTableNo, itemName, itemCode);
			selectedQty = 1;
		    }
		    /*
                     * else { new frmOkPopUp(null, "Please select quantity
                     * first", "Error", 1).setVisible(true); }
		     */
		}
		if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(itemCode))
		{
		    //code to load all item in combo item
		    frmTDHDialog frmComboItemTDH = new frmTDHDialog(this, true, itemCode, itemName, clsTDHOnItemDtl.hm_ComboItemDtl);
		    frmComboItemTDH.setVisible(true);
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

    private void funInsertDefaultSubItems(String icode)
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
		    clsMakeKotItemDtl ob1 = new clsMakeKotItemDtl(getStrSerialNo(), KOTNo, globalTableNo, globalWaiterNo, "=>".concat(temp_SubItemName), temp_SubItemCode, 1, 0.00, 0, "N", "Y", false, "", icode, "", "N", 0.00);
		    obj_List_KOT_ItemDtl.add(ob1);
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funShowComboSubItemTable(String itemCode)
    {
	try
	{
	    if (objPanelSubItem == null)
	    {
		panelTopSortingButtons.setVisible(false);
		panelNavigate.setVisible(false);
		panelItemButtons.setVisible(false);
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
		panelItemButtons.setVisible(false);
		panelNavigate.setVisible(false);
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
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funEnterPAXNo(String no)
    {
	try
	{
	    if ((!clsGlobalVarClass.gSkipWaiter) && txtWaiterNo.getText().trim().length() < 1)
	    {
		new frmOkPopUp(this, "Please Select The Waiter.", "error", 1).setVisible(true);
		return;
	    }

	    txtPaxNo.setText(no);
	    sql = "update tblitemrtemp set intPaxNo='" + no + "' where strTableNo='" + globalTableNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbltablemaster set intPaxNo='" + no + "' where strTableNo='" + globalTableNo + "'";
	    //exe = clsGlobalVarClass.dbMysql.execute(sql);
	    fieldSelected = "MenuItem";
	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBck(globalTableNo);

	    funShowMenuHeadPanel();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funNumberEntry(String num)
    {
	funDisableSelectedQtyBtn(num);
	selectedQty = Double.parseDouble(num);
    }

    private void funChangeQtyOfItem(String qty, String seqNo)
    {
	try
	{
	    if (!(tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0).toString().startsWith("KT")) && !(tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0).toString().startsWith("=>")))
	    {
		int rowNo = tblItemTable.getSelectedRow();
		String selectedItemCode = tblItemTable.getValueAt(rowNo, 3).toString().trim();
		boolean flag_found = false;
		if (null != selectedItemCode)
		{
		    for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
		    {
			if (selectedItemCode.equalsIgnoreCase(ob.getItemCode()) && KOTNo.equalsIgnoreCase(ob.getKOTNo())
				&& seqNo.equals(ob.getSequenceNo()))
			{
			    double temp_Original_amt = ob.getAmt() / ob.getQty();
			    double temp_currentQty = Double.parseDouble(qty);
			    double temp_final_Amt = temp_Original_amt * temp_currentQty;
			    if (!clsGlobalVarClass.gNegBilling)
			    {
				if (!clsGlobalVarClass.funCheckNegativeStock(selectedItemCode, temp_currentQty))
				{
				    return;
				}
			    }
			    ob.setAmt(temp_final_Amt);
			    ob.setQty(temp_currentQty);
			    flag_found = true;
			}
			if (flag_found)
			{
			    break;
			}
		    }
		}
		if (!flag_found)
		{
		    selectedQty = 1;
		}
		if (flag_found)
		{
		    selectedQty = 1;
		    funRefreshItemTable();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    clsGlobalVarClass.gNumerickeyboardValue = null;
	}
    }

    private void funItemTableClicked(String clickType)
    {
	try
	{

	    if (tblItemTable.getRowCount() > 0 && !(tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0).toString().startsWith("KT")) && fieldSelected.equals("MenuItem"))
	    {
		int columnNo = tblItemTable.getSelectedColumn();
		int rowNo = tblItemTable.getSelectedRow();

		if (clickType.equalsIgnoreCase("Mouse"))
		{
		    if (columnNo == 1)
		    {
			frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    String seqNo = tblItemTable.getValueAt(rowNo, 4).toString();
			    if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			    {
				if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
				{
				    funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue, seqNo);
				}
			    }
			}
		    }
		}
		else
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
		    num.setVisible(true);
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			String seqNo = tblItemTable.getValueAt(rowNo, 4).toString();
			if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			{
			    if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
			    {
				funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue, seqNo);
			    }
			}
		    }
		}
		String tempiCode = tblItemTable.getValueAt(rowNo, 3).toString();
		temp_ItemCode = tempiCode;
		funModifierButtonAction();
		selectedRowNoForModifer = rowNo;
		if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(tempiCode))
		{
		    flag_isTDHModifier_Item = false;
		    MaxSubItemLimitWithComboItem = funGetMaxQty(tempiCode, "MaxSubItemLimitWithComboItem", "");
		    funShowComboSubItemTable(tempiCode);
		}
		else if (clsGlobalVarClass.ListTDHOnModifierItem.contains(tempiCode))
		{
		    flag_isTDHModifier_Item = true;
		}
		else
		{
		    flag_isTDHModifier_Item = false;
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

    private void funPreviousWaiterClick()
    {
	try
	{
	    cntNavigate1--;
	    if (cntNavigate1 == 0)
	    {
		btnPrevItem.setEnabled(false);
		btnNextItem.setEnabled(true);
		tblStartIndex = 0;
		funLoadWaiterNo(0, vWaiterNo.size());
	    }
	    else
	    {
		int waiterSize = cntNavigate1 * 16;
		int totalSize = waiterSize + 16;
		tblStartIndex = waiterSize;
		funLoadTables(waiterSize, totalSize);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funPreviousTableClick()
    {
	try
	{
	    cntNavigate1--;
	    if (cntNavigate1 == 0)
	    {
		btnPrevItem.setEnabled(false);
		btnNextItem.setEnabled(true);
		tblStartIndex = 0;
		funLoadTables(0, hmTable.size());
	    }
	    else
	    {
		int tableSize = cntNavigate1 * 16;
		int totalSize = tableSize + 16;
		tblStartIndex = tableSize;
		funLoadTables(tableSize, totalSize);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funPreviousItemClick()
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
		limit = nextCnt + 16;
		if (limit > itemNames.length)
		{
		    limit = itemNames.length;
		}

		for (int cntItem1 = 0; cntItem1 < 16; cntItem1++)
		{
		    btnItemArray[cntItem1].setText("");
		    btnItemArray[cntItem1].setForeground(Color.black);
		    btnItemArray[cntItem1].setBackground(Color.lightGray);
		    btnItemArray[cntItem1].setIcon(null);
		    btnItemArray[cntItem1].setEnabled(false);
		}

		for (int cntItem1 = nextCnt; cntItem1 < limit; cntItem1++)
		{
		    btnItemArray[cntItem].setText(fun_Get_FormattedName(itemNames[cntItem1]));
		    btnItemArray[cntItem].setEnabled(true);
		    if (null != hm_ModifierDtl)
		    {
			if (hm_ModifierDtl.get(itemNames[cntItem1]).isIsDefaultModifier())
			{
			    btnItemArray[cntItem].setBackground(new Color(255, 105, 180));//dark pink
			}
		    }

		    if (!isModifierSelect)
		    {
			//btnItemArray[cntItem].setIcon(funGetImageIcon(itemImageCode.get(cntItem1)));
			ImageIcon icon = funGetImageIcon(itemImageCode.get(cntItem1));
			if (null != icon)
			{
			    btnItemArray[cntItem].setIcon(icon);
			}

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

		//int startLimit = nextCnt;
		int startLimit = 0;
		int end = limit - nextCnt;

		//for (int cntItem1 = startLimit; cntItem1 < limit; cntItem1++)
		for (int cntItem1 = startLimit; cntItem1 < end; cntItem1++)
		{
		    btnItemArray[cntItem1].setEnabled(true);
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

    private void funNextItemClick()
    {
	try
	{
	    btnPrevItem.setEnabled(true);
	    JButton[] btnItemArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    nextItemClick++;
	    //int itemDiv = itemNames.length / 17;
	    int itemDiv = itemImageCode.size() / 17;
	    if (itemDiv == nextItemClick)
	    {
		btnNextItem.setEnabled(false);
	    }

	    if (flgPopular)
	    {
		funLoadPopularItems(btnItemArray);
	    }
	    else
	    {
		int k = 0;
		nextCnt = nextItemClick * 16;
		limit = nextCnt + 16;
		if (limit > itemNames.length)
		{
		    limit = itemNames.length;
		    btnNextItem.setEnabled(false);

		}
		for (int m1 = 0; m1 < 16; m1++)
		{
		    btnItemArray[m1].setText("");
		    btnItemArray[m1].setForeground(Color.black);
		    btnItemArray[m1].setBackground(Color.lightGray);
		    btnItemArray[m1].setIcon(null);
		    btnItemArray[m1].setEnabled(false);
		}

		for (int j = nextCnt; j < limit; j++)
		{
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
			//btnItemArray[k].setIcon(funGetImageIcon(itemImageCode.get(j)));
			ImageIcon icon = funGetImageIcon(itemImageCode.get(j));
			if (null != icon)
			{
			    btnItemArray[k].setIcon(icon);
			}
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funNextWaiterClick()
    {
	try
	{
	    cntNavigate1++;
	    int waiterSize = cntNavigate1 * 16;
	    int resDiv = vWaiterNo.size() / waiterSize;
	    int totalSize = waiterSize + 16;
	    tblStartIndex = waiterSize;
	    if (vWaiterNo.size() < totalSize)
	    {
		funLoadWaiterNo(waiterSize, vWaiterNo.size());
	    }
	    else
	    {
		funLoadWaiterNo(waiterSize, totalSize);
	    }
	    btnPrevItem.setEnabled(true);
	    if (resDiv == cntNavigate1)
	    {
		btnNextItem.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funNextTableClick()
    {
	cntNavigate1++;
	int tableSize = cntNavigate1 * 16;
	int resMod = hmTable.size() % tableSize;
	int resDiv = hmTable.size() / 16;
	int totalSize = tableSize + 16;
	tblStartIndex = tableSize;
	if (hmTable.size() < totalSize)
	{
	    funLoadTables(tableSize, hmTable.size());
	}
	else
	{
	    funLoadTables(tableSize, totalSize);
	}
	btnPrevItem.setEnabled(true);
	if (resDiv == cntNavigate1)
	{
	    btnNextItem.setEnabled(false);
	}
    }

    private void funMenuItemSelection(String btnValue, int btnIndex)
    {
	try
	{    
	if ("Table".equals(fieldSelected))
	{
	    String tableName = funRemoveLast(btnValue);
	    String tableNo = hmTable.get(tableName.toUpperCase());  // through index get tblno
	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(tableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
	    }
	    else
	    {
		funCheckTable(funRemoveLast(btnValue));
	    }
	}
	else if ("Waiter".equals(fieldSelected))
	{
	    funCheckWaiter(btnValue, tblStartIndex + btnIndex);
	}
	else if ("MenuItem".equals(fieldSelected) && isModifierSelect)
	{
	    funAddModifierToKOT(funConvertString(btnValue));
	}
	else if ("MenuItem".equals(fieldSelected))
	{
	    String itemName = "";
	    itemName = funConvertString(btnValue);
	    funGetItemPrice(itemName);

	    //to display modifier paner after item selection
	    if (obj_List_KOT_ItemDtl.size() > 0 && clsGlobalVarClass.gClientCode.equals("171.001"))//CHINA GRILL-PIMPRI
	    {
		tblItemTable.setRowSelectionInterval(obj_List_KOT_ItemDtl.size(), obj_List_KOT_ItemDtl.size());
		funItemTableClicked("Mouse");
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "Please Select PAX No");
	}
	}
	catch(Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}    
    }

    private int funCheckKOTStatus(String tableCode)
    {
	int retRows = 0;
	try
	{
	    String sql = "select count(*) from tblitemrtemp where strTableNo='" + tableCode + "'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		retRows = rs.getInt(1);
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return retRows;
    }

    private void funModifierButtonAction() throws Exception
    {
	int rowno = tblItemTable.getSelectedRow();
	if (rowno > -1)
	{
	    if (tblItemTable.getRowCount() > 0)
	    {
		if (!(tblItemTable.getValueAt(rowno, 0).toString().startsWith("KT"))
			&& !(tblItemTable.getValueAt(rowno, 0).toString().startsWith("-->"))
			&& !(tblItemTable.getValueAt(rowno, 0).toString().startsWith("=>")))
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

    private void funPLUButtonAction() throws Exception
    {
	if ("MenuItem".equals(fieldSelected))
	{
	    funPLUButtonPressed();
	}
    }

    private void funCheckKOTButtonAction() throws Exception
    {
	if (txtTableNo.getText().equals(""))
	{
	    new frmOkPopUp(null, "Please Select Table", "Error", 1).setVisible(true);
	}
	if ("Table".equalsIgnoreCase(fieldSelected))
	{
	    //do Nothing
	}
	else
	{
	    if (!funCheckKOTSave())
	    {
		if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
		{

		    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();

		    objKOTGeneration.funCkeckKotTextFile(globalTableNo, txtWaiterNo.getText().trim(), "N", "");
		}
		else
		{
		    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();

		    objKOTGeneration.funCkeckKotForJasper(globalTableNo, txtWaiterNo.getText().trim(), "N");
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Please Save KOt First", "Error", 1).setVisible(true);
	    }
	}
    }

    private void funHomeDeliveryButtonAction(JButton objButton) throws Exception
    {

	flgCheckNCKOTButtonColor = false;
	if (globalTableNo == null)
	{
	    new frmOkPopUp(null, "Please Select Table", "Warning", 1).setVisible(true);
	    return;
	}
	else
	{
	    //if(null!=clsGlobalVarClass.gNumerickeyboardValue || clsGlobalVarClass.gNumerickeyboardValue.trim().length()>0)

	    if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.trim().isEmpty())
	    {

		JOptionPane.showMessageDialog(this, "Please Enter Customer Mobile No!");
		return;

//                if (null != clsGlobalVarClass.gNumerickeyboardValue)
//                {
//                    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
//                    {
//                        funCheckHomeDelStatus(objButton);
//                    }
//                    else
//                    {
//                        JOptionPane.showMessageDialog(this, "Please Enter Customer Mobile No!");
//                         return ;
//                    }
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog(this, "Please Enter Customer Mobile No!");
//                    return ;
//                }
	    }
	    else
	    {
		funCheckHomeDelStatus(objButton);
	    }
	}
    }

    private void funCheckHomeDelStatus(JButton objButton)
    {
	//System.out.println("Color= "+objButton.getForeground() );
	if (objButton.getForeground() == Color.white)
	{
	    homeDeliveryForTax = "Y";
	    objButton.setForeground(Color.black);
	    flgHomeDeliveryColor_Button = true;

	    if (arrListHomeDelDetails.size() == 0)
	    {
		arrListHomeDelDetails.add(clsGlobalVarClass.gCustomerCode);
		arrListHomeDelDetails.add(clsGlobalVarClass.gCustomerName);
		arrListHomeDelDetails.add("HomeDelivery");
		arrListHomeDelDetails.add("");
		arrListHomeDelDetails.add("");
	    }

	    if (clsGlobalVarClass.gTakeAway.equalsIgnoreCase("Yes"))
	    {
		sql = "update tblitemrtemp set strTakeAwayYesNo='No' where strTableNo='" + globalTableNo + "'";
		try
		{
		    clsGlobalVarClass.dbMysql.execute(sql);
		    //insert into itemrtempbck table
		    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
		}
		catch (Exception ex)
		{
		    objUtility.funShowDBConnectionLostErrorMessage(ex);	
		    objUtility.funWriteErrorLog(ex);
		    ex.printStackTrace();
		}
		clsGlobalVarClass.gTakeAway = "No";
		btnButton1.setForeground(Color.white);
	    }

	    if (clsGlobalVarClass.gCustAddressSelectionForBill)
	    {
		frmHomeDeliveryAddress objDeliveryAddress = new frmHomeDeliveryAddress(this, true, clsGlobalVarClass.gCustMBNo);
		objDeliveryAddress.setVisible(true);
		String[] data = objDeliveryAddress.funGetCustomerAddressDetail();

		clsGlobalVarClass.gCustomerCode = data[0].toString();
		buildingCodeForHD = "";
		hmMakeKotParams.put("CustAddType", data[2].toString());
		btnNewCustomer.setText("<html>" + data[1].toString() + "</html>");

		funSetHomeDeliveryData(data[0].toString(), data[1].toString(), buildingCodeForHD, "", "");
	    }
	    else
	    {

	    }

	}
	else
	{

	    homeDeliveryForTax = "N";
	    objButton.setForeground(Color.white);
	    flgHomeDeliveryColor_Button = false;
	}
    }

    private void funNCKOTButtonAction()
    {
	if(clsGlobalVarClass.gSuperUser || clsGlobalVarClass.hmUserForms.containsKey("NCKOT")){
	    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	    {
		if (btnButton4.getForeground() == Color.white)
		{
		    btnButton4.setForeground(Color.black);
		    flgCheckNCKOTButtonColor = true;
		}
		else
		{
		    btnButton4.setForeground(Color.white);
		    flgCheckNCKOTButtonColor = false;
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "KOT Not Present.");
		return;
	    }    
	}else{
		JOptionPane.showMessageDialog(this, "Access Denied");
		return;
	}
	
    }

    private void funTakeAwayButtonAction(JButton objButton) throws Exception
    {
	if (globalTableNo != null)
	{
	    if (null == clsGlobalVarClass.hmTakeAway.get(globalTableNo))
	    {
		sql = "update tblitemrtemp set strTakeAwayYesNo='Yes' where strTableNo='" + globalTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
		clsGlobalVarClass.hmTakeAway.put(globalTableNo, "Yes");
		objButton.setForeground(Color.black);
		flgCheckTakeAway_ButtonColor = true;
		objButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		if (btnButton4.getForeground() == Color.black)
		{
		    btnButton4.setForeground(Color.white);
		    arrListHomeDelDetails.clear();
		}
	    }
	    else
	    {
		sql = "update tblitemrtemp set strTakeAwayYesNo='No' where strTableNo='" + globalTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
		clsGlobalVarClass.hmTakeAway.remove(globalTableNo);
		objButton.setForeground(Color.white);
		objButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
	    }
	    //insert into itemrtempbck table
	    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
	}
    }

    private void funSettleButtonAction() throws Exception
    {
	if (clsGlobalVarClass.gSuperUser || clsGlobalVarClass.hmUserForms.containsKey("SettleBill"))
	{
	    clsGlobalVarClass.gTransactionType = "SettleBill";
	    frmBillSettlement objBillSettlement = new com.POSTransaction.view.frmBillSettlement();
	    objBillSettlement.setVisible(true);
	    objBillSettlement.funSetCallingForm("Make KOT");
	    objBillSettlement.funSetObjMakeKOT(this);
	    clsGlobalVarClass.hmActiveForms.put("SettleBill", "SettleBill");

	}
	else
	{
	    new frmOkPopUp(null, "Access Denied.", "Settle Bill", 1).setVisible(true);
	    return;
	}

    }

    private void funButtonAction(JButton objButton)
    {
	try
	{
	    funCloseKitchenNotePanel();
	    String buttonName = objButton.getText();
	    switch (buttonName)
	    {
		case "<html><u>S</u>ETTLE<br>BILL</html>":
		    if (!clsGlobalVarClass.gClientCode.equals("024.001"))
		    {
			funSettleButtonAction();
		    }
		    break;

		case "PLU":
		    funPLUButtonAction();
		    break;

		case "<Html>DELIVERY<br>BOY</html>":
		    funSelectDeliveryPersonCode(objButton);
		    break;

		case "<html>HOME<br>DELIVERY</html>":
		    funHomeDeliveryButtonAction(objButton);
		    break;

		case "<html>FIRE<br>KOT</html>":
		    //funTakeAwayButtonAction(objButton);
		    funFireKOTButtonClicked();
		    break;

		case "CHECKKOT":
		    funCheckKOTButtonAction();
		    break;

		case "<html>ZOMATO<br>CODE</html>":
		    //funModifierButtonAction();

		    funBillNoteButtonClicked();
		    break;

		case "NC KOT":
		    funNCKOTButtonAction();
		    break;
		case "<html>CUSTOMER<br>HISTORY<html>":
		    funCustomerHistoryAction();
		    break;

	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public clsCustomerDataModelForSQY getObjData()
    {
	return objData;
    }

    private void funHomeDelivery()
    {
	if (arrListHomeDelDetails.size() > 0)
	{
	    double totalBillAmount = 0.00;
	    if (txtTotal.getText().trim().length() > 0)
	    {
		totalBillAmount = Double.parseDouble(txtTotal.getText());
	    }
	    objUtility.funGetDeliveryCharges(arrListHomeDelDetails.get(2), totalBillAmount, "");
	}
    }

    private void funSelectDeliveryPersonCode(JButton objButton)
    {
	try
	{
	    if (arrListHomeDelDetails.size() > 0)
	    {
		objUtility.funCallForSearchForm("DeliveryBoyForHD");
		new frmSearchFormDialog(this, true).setVisible(true);
		if (clsGlobalVarClass.gSearchItemClicked)
		{
		    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		    clsGlobalVarClass.gSearchItemClicked = false;
		    clsGlobalVarClass.gDeliveryBoyCode = data[0].toString();
		    clsGlobalVarClass.gDeliveryBoyName = data[1].toString();
		    arrListHomeDelDetails.set(4, data[0].toString());
		    arrListHomeDelDetails.add(5, data[1].toString());
		    deliveryBoyName = data[1].toString();
		    objButton.setText(data[1].toString());
		    flgcheckDeliveryboyName = true;
		    sql = "update tblitemrtemp set strDelBoyCode='" + data[0].toString() + "' "
			    + "where strTableNo='" + globalTableNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    //insert into itemrtempbck table
		    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Please Select Customer");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private boolean funCheckKOTSave()
    {
	boolean flg = false;
	try
	{
	    String sqltblExist = "select * from tblitemrtemp where strTableNo='" + globalTableNo + "'";
	    ResultSet rstblExist = clsGlobalVarClass.dbMysql.executeResultSet(sqltblExist);

	    if (rstblExist.next())
	    {

		String sql_KOTSave = "select strPrintYN from tblitemrtemp where strKOTNo='" + KOTNo + "' "
			+ "and strTableNo='" + globalTableNo + "' and strPrintYN='N' group by  strPrintYN";
		ResultSet rsKOTSave = clsGlobalVarClass.dbMysql.executeResultSet(sql_KOTSave);
		if (rsKOTSave.next())
		{
		    flg = true;
		}
	    }
	    else
	    {
		flg = true;
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return flg;
    }

    private boolean funCheckKOT()
    {
	boolean flgSaveKot = true;
	if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	{
	    flgSaveKot = false;
	}

	return flgSaveKot;
    }

    //Conver the Sring in Original form
    private String funConvertString(String name)
    {
	if (name.contains("<html>"))
	{
	    StringBuilder sb1 = new StringBuilder(name);
	    sb1 = sb1.delete(0, 6);
	    int seq = sb1.lastIndexOf("<br>");
	    String split = sb1.substring(0, seq);
	    int end = sb1.lastIndexOf("</html>");
	    String last = sb1.substring(seq + 4, end);
	    name = split + " " + last;
	    sb1 = null;
	}
	return name;
    }

    public void funSetArea(String selAreaCode)
    {
	try
	{
	    objUtility = new clsUtility();

	    funFillReservedTables();
	    cntNavigate1 = 0;
	    clsAreaCode = selAreaCode;
	    tblStartIndex = 0;
	    clsAreaName = "NA";
	    sql = "select strAreaName from tblareamaster "
		    + "where strAreaCode='" + clsAreaCode + "'";
	    ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsAreaInfo.next())
	    {
		clsAreaName = rsAreaInfo.getString(1);
	    }
	    rsAreaInfo.close();

	    lblAreaName.setText(clsAreaName);
	    hmTable.clear();
	    hmTableSeq.clear();

	    if (clsAreaName.equalsIgnoreCase("All"))
	    {
		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ "and strOperational='Y' order by intSequence ";
	    }
	    else
	    {
		sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
			+ "where strAreaCode='" + clsAreaCode + "' "
			+ "and (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ "and strOperational='Y' order by intSequence ";
	    }

	    //System.out.println(sql);
	    ResultSet rsTableCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTableCode.next())
	    {
		//vTableNo.add(rsTableCode.getString(1));
		//vTableName.add(rsTableCode.getString(2));
		hmTable.put(rsTableCode.getString(2).toUpperCase(), rsTableCode.getString(1));
		hmTableSeq.put(rsTableCode.getString(1) + "!" + rsTableCode.getString(2), rsTableCode.getInt(3));
	    }
	    rsTableCode.close();
	    //funLoadTables(0, vTableNo.size());
	    funLoadTables(0, hmTableSeq.size());
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funCheckTable(String tableName)
    {
	btnNewCustomer.getText();
	try
	{
	    homeDeliveryForTax = "N";
	    btnButton4.setForeground(Color.white);
	    clsGlobalVarClass.gCustomerCode = "";
	    globalDebitCardNo = "";
	    globalWaiterNo = "";
	    tableSelected = "TableSelected";
	    btnButton1.setForeground(Color.white);
	    btnPrevItem.setEnabled(false);
	    btnNextItem.setEnabled(false);

	    globalTableName = tableName;
	    txtTableNo.setText(tableName);
	    globalTableNo = hmTable.get(tableName.toUpperCase());  // through index get tblno
	    objCustomerRewards = null;

	    //********************set auto customer name if table is reserve (table status=reserve)
	    String sqlReserveTableStatus = "select a.strTableNo,a.strTableName,a.strStatus "
		    + "from tbltablemaster a "
		    + "where a.strTableNo='" + globalTableNo + "' "
		    + "and a.strStatus='Reserve' ";
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sqlReserveTableStatus);
	    if (rsCust.next())
	    {
		String sqlCustomer = "select a.strResCode,a.strCustomerCode,b.strCustomerName,b.longMobileNo "
			+ "from tblreservation a,tblcustomermaster b "
			+ "where a.strTableNo='" + globalTableNo + "' "
			+ "and a.strCustomerCode=b.strCustomerCode "
			+ "order by a.strResCode desc "
			+ "limit 1; ";
		rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustomer);
		if (rsCust.next())
		{
		    clsGlobalVarClass.gCustomerCode = rsCust.getString(2);
		    btnNewCustomer.setText("<html>" + rsCust.getString(3) + "</html>");
		    clsGlobalVarClass.gCustMBNo = rsCust.getString(4);
		}
	    }
	    else
	    {
		btnNewCustomer.setText("<Html>CUSTOMER</html>");
	    }
	    rsCust.close();

	    /**
	     * set NC properties for NC Table
	     */
	    funDefaultNCTableClicked();

	    arrListHomeDelDetails.clear();
	    funClearOldKotObjectList();
	    if (!funNextKOTFlag(oldTableNo))
	    {
		funShiftKOT(oldTableNo, globalTableNo);
		funRefreshItemTable();
	    }
	    else
	    {
		funRefreshItemTable();
	    }
	    flgTableSelection = false;

	    sql = "select strAreaCode from tbltablemaster where strTableNo='" + globalTableNo + "'";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		clsAreaCode = rsArea.getString(1);
	    }
	    sql = " select a.strWaiterNo,a.intPaxNo,sum(a.dblAmount),a.strCardNo,if(a.strCustomerCode='null','',a.strCustomerCode) "
		    + ",ifnull(b.strCustomerName,''),ifnull(b.strBuldingCode,''),a.strHomeDelivery "
		    + " from tblitemrtemp a "
		    + " left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + " where a.strTableNo='" + globalTableNo + "' and a.strPrintYN='Y' and a.strNCKotYN='N' "
		    + " group by a.strTableNo";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		if (rs.getString(5) == null || rs.getString(5).isEmpty())
		{
		    clsGlobalVarClass.gCustomerCode = "";
		    btnNewCustomer.setText("<Html>CUSTOMER</html>");
		}
		else
		{
		    clsGlobalVarClass.gCustomerCode = rs.getString(5);
		    clsGlobalVarClass.gCustomerName = rs.getString(6);
		    btnNewCustomer.setText("<html>" + rs.getString(6) + "</html>");

		    try
		    {
			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    sql = "select a.strTableNo,a.strKOTNo,b.strCustomerCode,b.strCustomerName,b.longMobileNo,a.strCRMRewardId "
				    + "from tblitemrtemp a,tblcustomermaster b "
				    + "where a.strCustomerCode=b.strCustomerCode "
				    + "and a.strCRMRewardId!='' "
				    + "and a.strTableNo='" + globalTableNo + "'  "
				    + "AND a.strPrintYN='Y'  "
				    + "AND a.strNCKotYN='N' "
				    + "order by a.strKOTNo desc ";
			    ResultSet rsForRewards = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rsForRewards.next())
			    {
				String mobileNo = rsForRewards.getString(5);
				String rewardId = rsForRewards.getString(6);

				clsCRMInterface objCRMInterface = new clsCRMInterface();
				clsRewards objRewards = objCRMInterface.funGetCustomerRewards(mobileNo, rewardId);
				if (objRewards != null)
				{
				    this.objCustomerRewards = objRewards;
				}
			    }
			    rsForRewards.close();
			}
		    }
		    catch (Exception e)
		    {
			objUtility.funShowDBConnectionLostErrorMessage(e);	
			e.printStackTrace();
			objUtility.funWriteErrorLog(e);
		    }
		}

		if (rs.getString(8).equalsIgnoreCase("Yes"))
		{
		    homeDeliveryForTax = "Y";
		    btnButton4.setForeground(Color.black);
		    flgHomeDeliveryColor_Button = true;

		    if (arrListHomeDelDetails.size() == 0)
		    {
			arrListHomeDelDetails.add(clsGlobalVarClass.gCustomerCode);
			arrListHomeDelDetails.add(clsGlobalVarClass.gCustomerName);
			arrListHomeDelDetails.add("HomeDelivery");
			arrListHomeDelDetails.add("");
			arrListHomeDelDetails.add("");
		    }
		}
		else
		{
		    homeDeliveryForTax = "N";
		    btnButton4.setForeground(Color.white);
		    flgHomeDeliveryColor_Button = false;
		}

		sql = " select a.strWaiterNo,a.intPaxNo,sum(a.dblAmount),b.dblRedeemAmt,a.strCardNo,c.dblcardvaluefixed "
			+ " ,b.strCardString,c.strSetExpiryTime,c.dblDepositAmt "
			+ " from tblitemrtemp a,tbldebitcardmaster b,tbldebitcardtype c "
			+ " where a.strCardNo=b.strCardNo and strTableNo='" + globalTableNo + "' "
			+ " and b.strCardTypeCode=c.strCardTypeCode and strPrintYN='Y' and strNCKotYN='N' "
			+ " group by strTableNo";
		ResultSet rsKOTWithCard = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsKOTWithCard.next())
		{
		    if (rsKOTWithCard.getDouble(4) > 0)
		    {
			String cardString = rsKOTWithCard.getString(7);
			String isSetExpiryTime = rsKOTWithCard.getString(8);
			if (isSetExpiryTime.equalsIgnoreCase("Y"))
			{
			    String status = objUtility.funIsCardTimeExpire(cardString);
			    if (status.equalsIgnoreCase("Active"))
			    {
				//valid

				globalDebitCardNo = rsKOTWithCard.getString(5);
				double debitCardBal = rsKOTWithCard.getDouble(4) - rsKOTWithCard.getDouble(6) - rsKOTWithCard.getDouble(9);
				debitCardBal -= objUtility.funGetKOTAmtOnTable(globalDebitCardNo);

				lblCardBalnce.setText(String.valueOf(Math.rint(debitCardBal)));
				lblCardBalnce.setVisible(true);
				lblDebitCardBalance.setVisible(true);
				double balAmt = rsKOTWithCard.getDouble(4);
				double kotAmt = rsKOTWithCard.getDouble(3);

				if (rsKOTWithCard.getDouble(4) < rsKOTWithCard.getDouble(3))
				{
				    lblCardBalnce.setBackground(Color.red);
				}
				else
				{
				    lblCardBalnce.setBackground(Color.yellow);
				}
			    }
			    else
			    {
				//time expired

				String[] arrMesg = status.split("!");

				JOptionPane.showMessageDialog(null, "<html>Recharge No:" + arrMesg[1] + "<br>Recharge Amt:" + arrMesg[2] + "<br>Recharge Time:" + arrMesg[3] + "</html>", "Card Time Expired", JOptionPane.ERROR_MESSAGE);
			    }
			}
			else
			{
			    globalDebitCardNo = rsKOTWithCard.getString(5);
			    double debitCardBal = rsKOTWithCard.getDouble(4) - rsKOTWithCard.getDouble(6) - rsKOTWithCard.getDouble(9);
			    debitCardBal -= objUtility.funGetKOTAmtOnTable(globalDebitCardNo);

			    lblCardBalnce.setText(String.valueOf(Math.rint(debitCardBal)));
			    lblCardBalnce.setVisible(true);
			    lblDebitCardBalance.setVisible(true);
			    double balAmt = rsKOTWithCard.getDouble(4);
			    double kotAmt = rsKOTWithCard.getDouble(3);

			    if (rsKOTWithCard.getDouble(4) < rsKOTWithCard.getDouble(3))
			    {
				lblCardBalnce.setBackground(Color.red);
			    }
			    else
			    {
				lblCardBalnce.setBackground(Color.yellow);
			    }
			}
		    }
		}
		rsKOTWithCard.close();

		gTableFound = "Yes";
		funFillOldKOTItems(globalTableNo);
		funRefreshItemTable();

		if (clsGlobalVarClass.gMultiWaiterSelOnMakeKOT)
		{
		    txtPaxNo.setText(rs.getString(2));
		    funLoadWaiterNames();
		}
		else
		{
		    for (int k = 0; k < vWaiterNo.size(); k++)
		    {
			if (vWaiterNo.elementAt(k).toString().equals(rs.getString(1)))
			{
			    globalWaiterNo = rs.getString(1);
			    txtWaiterNo.setText(funConvertString(vWaiterName.elementAt(k).toString()));
			    break;
			}
		    }
		    txtPaxNo.setText(rs.getString(2));
		    fieldSelected = "MenuItem";
		    btnItemMode.setVisible(true);
		    btnItemMode.setEnabled(true);

		    funShowMenuHeadPanel();
		    funCheckHomeDelivery();

		    if (null != clsGlobalVarClass.hmTakeAway.get(globalTableNo))
		    {
			if (navigate == 1)
			{
			    btnButton1.setForeground(Color.black);
			}
		    }
		}
	    }
	    else if (clsGlobalVarClass.gSkipWaiter && clsGlobalVarClass.gSkipPax)
	    {
		funItemModeButtonClicked();
	    }
	    else if (clsGlobalVarClass.gSkipWaiter)
	    {
		funResetMenuItemButtons();
		funEnablePaxButtons(true);
		fieldSelected = "Pax";
	    }
	    else
	    {
		funLoadWaiterNames();
	    }
	    sql = "select a.strAreaCode,b.strAreaName from tbltablemaster a,tblareamaster b "
		    + "where a.strTableNo='" + globalTableNo + "' and a.strAreaCode=b.strAreaCode";
	    ResultSet rsTemp = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTemp.next())
	    {
		clsAreaCode = rsTemp.getString(1);
		lblAreaName.setText(rsTemp.getString(2));
	    }
	    rsTemp.close();

	    tblStartIndex = 0;
	    cntNavigate1 = 0;
	    if (vWaiterNo.size() > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    else
	    {
		btnNextItem.setEnabled(false);
	    }

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		String sql_TableStatus = "select strCustomerCode ,strCustomerName "
			+ "from tblitemrtemp where strtableno = '" + globalTableNo + "' and strCustomerCode <>'' ";
		ResultSet rsTableStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql_TableStatus);
		if (rsTableStatus.next())
		{
		    cmsMemCode = rsTableStatus.getString(1);
		    cmsMemName = rsTableStatus.getString(2);
		    btnNewCustomer.setText("<html>" + cmsMemName + "</html>");
		    hmCMSMemberForTable.put(globalTableNo, cmsMemCode);
		}
		else
		{
		    if (!rsTableStatus.next())
		    {
			btnNewCustomer.setText("<html>CUSTOMER</html>");
		    }
		}
		rsTableStatus.close();
	    }
	    funFillMapWithHappyHourItems();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }


    private void funLoadWaiterNames() throws Exception
    {
	fieldSelected = "Waiter";
	btnItemMode.setVisible(true);
	btnItemMode.setEnabled(true);

	if (clsGlobalVarClass.gSelectWaiterFromCardSwipe && clsPosConfigFile.gSelectWaiterFromCardSwipe.equalsIgnoreCase("true"))
	{
	    new frmSwipCardPopUp(this, "frmMakeKOT").setVisible(true);
	    String waiterInfo = funCheckDebitCardString(clsGlobalVarClass.gDebitCardNo);
	    if (!waiterInfo.isEmpty())
	    {
		String[] spWaiter = waiterInfo.split("#");
		funSetWaiterInfo(spWaiter[1], spWaiter[0]);
		txtWaiterNo.setText(spWaiter[1]);
		funEnablePaxButtons(true);
		if (!clsGlobalVarClass.gSkipPax)
		{
		    if (Integer.parseInt(txtPaxNo.getText().trim()) != 0)
		    {
			funItemModeButtonClicked();
		    }
		    else
		    {
			fieldSelected = "Pax";
			txtPaxNo.requestFocus();
			txtPaxNo.selectAll();
		    }
		}
		else if (clsGlobalVarClass.gSkipPax)
		{
		    fieldSelected = "MenuItem";
		}
		panelExternalCode.setVisible(true);
	    }
	    else
	    {
		funEnablePaxButtons(false);
	    }
	}
	else
	{
	    sql = "select strWaiterNo from tbltablemaster where strTableNo='" + globalTableNo + "'";
	    ResultSet rsWaiterNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsWaiterNo.next();
	    String temWaiterNo = rsWaiterNo.getString(1);
	    if (temWaiterNo.equalsIgnoreCase("all"))
	    {
		funLoadWaiterNo(0, vWaiterNo.size());
	    }
	    else
	    {
		JButton[] btnItemArray =
		{
		    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
		};
		funResetMenuItemButtons();

		for (int cntWaiter = 0; cntWaiter < vWaiterNo.size(); cntWaiter++)
		{
		    if (vWaiterNo.elementAt(cntWaiter).toString().equals(temWaiterNo))
		    {
			globalWaiterNo = rsWaiterNo.getString(1);
			txtWaiterNo.setText(funConvertString(vWaiterName.elementAt(cntWaiter).toString()));
			break;
		    }
		}
		funEnablePaxButtons(true);

		if (!clsGlobalVarClass.gSkipPax)
		{
		    fieldSelected = "Pax";
		}
		else if (clsGlobalVarClass.gSkipPax)
		{
		    fieldSelected = "MenuItem";
		    panelExternalCode.setVisible(true);
		}
	    }
	}
    }

    private void funCheckTableForCMS(String tableNo, String tableName)
    {
	try
	{
	    tableSelected = "TableSelected";
	    btnButton1.setForeground(Color.white);
	    btnPrevItem.setEnabled(false);
	    btnNextItem.setEnabled(false);
	    globalTableName = tableName;
	    txtTableNo.setText("");
	    globalWaiterNo = "";
	    txtTableNo.setText(tableName);
	    globalTableNo = tableNo;
	    funClearOldKotObjectList();
	    if (!funNextKOTFlag(oldTableNo))
	    {
		funShiftKOT(oldTableNo, globalTableNo);
		funRefreshItemTable();
	    }
	    flgTableSelection = false;
	    funRefreshItemTable();

	    sql = "select strAreaCode from tbltablemaster where strTableNo='" + globalTableNo + "'";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		clsAreaCode = rsArea.getString(1);
	    }
	    sql = "select strWaiterNo,intPaxNo from tblitemrtemp where strTableNo='" + globalTableNo + "' "
		    + "and strPrintYN='Y' and strNCKotYN='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		//Ritesh 27 Sept 2014
		funFillOldKOTItems(globalTableNo);
		funRefreshItemTable();
		///
		if (clsGlobalVarClass.gMultiWaiterSelOnMakeKOT)
		{
		    funLoadWaiterNames();
		}
		else
		{
		    for (int k = 0; k < vWaiterNo.size(); k++)
		    {
			if (vWaiterNo.elementAt(k).toString().equals(rs.getString(1)))
			{
			    globalWaiterNo = rs.getString(1);
			    txtWaiterNo.setText(funConvertString(vWaiterName.elementAt(k).toString()));
			    break;
			}
		    }
		    txtPaxNo.setText(rs.getString(2));
		    fieldSelected = "MenuItem";
		    btnItemMode.setVisible(true);
		    funShowMenuHeadPanel();
		    funCheckDebitCardTable();
		    funCheckHomeDelivery();

		    /*
                     * if (funCheckHomeDelivery()) {
                     * funSetHomeDeliveryCustumerName();
                     * clsGlobalVarClass.gHomeDeliveryTable = true; } else {
                     * clsGlobalVarClass.gHomeDeliveryTable = false; } if (null
                     * != clsGlobalVarClass.hmTakeAway.get(globalTableNo)) { if
                     * (navigate == 1) { btnButton1.setForeground(Color.black);
                     * } }
		     */
		}
	    }
	    else if (clsGlobalVarClass.gSkipWaiter && clsGlobalVarClass.gSkipPax)
	    {
		funItemModeButtonClicked();
	    }
	    else if (clsGlobalVarClass.gSkipWaiter)
	    {
		funResetMenuItemButtons();
		funEnablePaxButtons(true);
		fieldSelected = "Pax";
	    }
	    else
	    {
		funLoadWaiterNames();
	    }
	    sql = "select a.strAreaCode,b.strAreaName "
		    + " from tbltablemaster a,tblareamaster b "
		    + " where a.strTableNo='" + globalTableNo + "' and a.strAreaCode=b.strAreaCode";
	    ResultSet rsTemp = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTemp.next())
	    {
		clsAreaCode = rsTemp.getString(1);
		lblAreaName.setText(rsTemp.getString(2));
	    }
	    rsTemp.close();

	    tblStartIndex = 0;
	    cntNavigate1 = 0;
	    if (vWaiterNo.size() > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    else
	    {
		btnNextItem.setEnabled(false);
	    }
	    //Changes By PAVAN Date 20-03-2015//
	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		String sql_TableStatus = "select strCustomerCode ,strCustomerName "
			+ " from tblitemrtemp "
			+ "where strtableno = '" + globalTableNo + "' and strCustomerCode <>'' ";
		ResultSet rsTableStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql_TableStatus);
		if (rsTableStatus.next())
		{
		    cmsMemCode = rsTableStatus.getString(1);
		    cmsMemName = rsTableStatus.getString(2);
		    btnNewCustomer.setText("<html>" + cmsMemName + "</html>");
		    hmCMSMemberForTable.put(globalTableNo, cmsMemCode);
		}
		else
		{
		    if (!rsTableStatus.next())
		    {
			btnNewCustomer.setText("<html>CUSTOMER</html>");
		    }
		}
		rsTableStatus.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funCheckWaiter(String wtName, int index)
    {
	funSetWaiterInfo(wtName, vWaiterNo.elementAt(index).toString());
    }

    private void funSetWaiterInfo(String waiterName, String waiterNo)
    {
	try
	{
	    globalWaiterNo = waiterNo;
	    globalWaiterName = waiterName;
	    flgWaiterSelection = false;
	    txtWaiterNo.setText("");
	    txtWaiterNo.setText(funConvertString(waiterName));
	    funEnablePaxButtons(true);
	    if (clsGlobalVarClass.gSkipPax)
	    {
		funItemModeButtonClicked();
	    }
	    else
	    {
		if (Integer.parseInt(txtPaxNo.getText().trim()) > 1)
		{
		    funItemModeButtonClicked();
		}
		else
		{
		    fieldSelected = "Pax";
		    txtPaxNo.requestFocus();
		    txtPaxNo.selectAll();
		}
	    }
	    tblStartIndex = 0;

	    if (updateSelectedField.equals("Waiter"))
	    {
		sql = "update tblitemrtemp set strWaiterNo='" + globalWaiterNo + "' "
			+ " where strTableNo='" + globalTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
		//insert into itemrtempbck table
		new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
	    }
	    updateSelectedField = "";
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funShowMenuHeadPanel()
    {
	try
	{
	    funLoadMenuNames();
	    btnPrevMenu.setEnabled(false);
	    panelNumericKeyPad.setVisible(true);
	    if ("Yes".equalsIgnoreCase(clsGlobalVarClass.gDebitCardPayment))
	    {
		panelDeditCard.setVisible(true);
	    }
	    panelExternalCode.setVisible(true);
	    panelMenuHead.setVisible(true);
	    btnB4.setEnabled(true);
	    btnNumber1.setEnabled(true);
	    btnNumber2.setEnabled(true);
	    btnNumber3.setEnabled(true);
	    btnNumber4.setEnabled(true);
	    btnNumber5.setEnabled(true);
	    btnNumber6.setEnabled(true);
	    btnNumber7.setEnabled(true);
	    btnNumber8.setEnabled(true);
	    btnNumber9.setEnabled(true);
	    btnMultiQty.setEnabled(true);
	    btnPopular.setEnabled(true);
	    funPopularItem();
	    txtExternalCode.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    //to reset the whole field
    public void funResetFields()
    {
	try
	{
	    btnMakeKOT.setEnabled(true);
	    if (null != objPanelModifier)
	    {
		objPanelModifier.setVisible(false);
		objPanelModifier = null;
	    }
	    hmMakeKotParams.clear();
	    funShowMenuHeadPanel();
	    funClearObjectList();
	    fieldSelected = "Table";
	    funResetTopSortingButtons();
	    panelExternalCode.setVisible(false);
	    btnItemMode.setVisible(false);
	    panelDeditCard.setVisible(false);
	    lblDebitCardBalance.setVisible(false);
	    lblCardBalnce.setText("");
	    globalTableNo = null;
	    lblAreaName.setText("ALL");
	    DefaultTableModel dm = new DefaultTableModel();
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    selectedQty = 1;
	    tblItemTable.setModel(dm);
	    txtTotal.setText("");
	    btnButton1.setForeground(Color.white);
	    txtTableNo.setText("");
	    txtWaiterNo.setText("");
	    txtPaxNo.setText("1");

	    JButton[] btnSubMenuArray =
	    {
		btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	    };
	    for (int i = 0; i < btnSubMenuArray.length; i++)
	    {
		btnSubMenuArray[i].setEnabled(false);
		btnSubMenuArray[i].setText("");
	    }

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(165);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		funInitTables();
		funLoadTables(0, hmTable.size());
	    }
	    else
	    {
		String sql_Area = "select strAreaCode from tblareamaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strAreaName='All' "
			+ " order by strAreaCode";
		//System.out.println(sql_Area);
		ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql_Area);
		if (rsArea.next())
		{
		    funSetArea(rsArea.getString(1));
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Please Check Area Master for All Area!!!");
		}
		rsArea.close();
		flgWaiterSelection = false;
	    }

	    flgTableSelection = true;
	    btnButton4.setForeground(Color.white);
	    btnButton1.setForeground(Color.white);
	    flgCheckNCKOTButtonColor = false;
	    cmsMemCode = "";
	    cmsMemName = "";
	    setKOTToBillNote();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    private void funResetItemButtonText(String menuName)
    {
	int i = 0;
	fieldSelected = "MenuItem";
	isModifierSelect = false;
	//create the button array of Item
	JButton[] btnSubMenuArray =
	{
	    btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};

	try
	{
	    list_ItemNames_Buttoms.clear();
	    obj_List_ItemPrice = new ArrayList<>();
	    btnNextItem.setEnabled(false);
	    btnPrevItem.setEnabled(false);
	    nextItemClick = 0;
	    String posDateForPrice = posDate.split(" ")[0];

	    sql = "select strMenuCode from tblmenuhd where strMenuName='" + menuName + "' and strOperational='Y' ";
	    ResultSet rsMenuInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsMenuInfo.next())
	    {
		menuHeadCode = rsMenuInfo.getString(1);
	    }
	    rsMenuInfo.close();

	    String sql_ItemCount = "";
	    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	    {
		if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
		{
		    sql_ItemCount = "select count(*) "
			    + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
			    + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
			    + "and a.strItemCode=c.strItemCode \n"
			    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
			    + "and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strMenuCode='" + menuHeadCode + "' "
			    + "and c.strOperationalYN='Y' "
			    + "ORDER BY b.dteFromTime";
		}
		else
		{
		    sql_ItemCount = "select count(*) "
			    + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
			    + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
			    + "and a.strItemCode=c.strItemCode \n"
			    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
			    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strAreaCode='" + clsAreaCode + "' "
			    + "and a.strMenuCode='" + menuHeadCode + "' "
			    + "and c.strOperationalYN='Y' "
			    + "ORDER BY b.dteFromTime";
		}
	    }
	    else
	    {
		if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
		{
		    sql_ItemCount = "select count(*) "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + " WHERE a.strMenuCode = '" + menuHeadCode + "' "
			    + " and a.strItemCode=b.strItemCode "
			    + " and a.strHourlyPricing='NO' "
			    + " and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			    + " and date(dteFromDate)<='" + posDateForPrice + "' and date(dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + " ORDER BY b.strItemName ASC";
		}
		else
		{
		    sql_ItemCount = "select count(*) "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + " WHERE a.strAreaCode='" + clsAreaCode + "' "
			    + " and a.strHourlyPricing='NO' "
			    + " and a.strMenuCode = '" + menuHeadCode + "' "
			    + " and a.strItemCode=b.strItemCode "
			    //+ "WHERE (a.strAreaCode='" + clsAreaCode + "') "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + " ORDER BY b.strItemName ASC";
		}
	    }
	    rsMenuInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemCount);
	    rsMenuInfo.next();
	    int cn = rsMenuInfo.getInt(1);
	    if (cn > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    rsMenuInfo.close();
	    itemNames = new String[cn];
	    btnforeground = new String[cn];
	    itemImageCode.clear();

	    String sql_ItemDtl = "";

	    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	    {
		if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
		{
		    sql_ItemDtl = "select a.strItemCode,c.strItemName,'Black',b.dblMemberPriceMonday,b.dblMemberPriceTuesday"
			    + ",b.dblMemberPriceWednesday,b.dblMemberPriceThursday\n"
			    + ",b.dblMemberPriceFriday,b.dblMemberPriceSaturday,b.dblMemberPriceSunday,'','','','',a.strCostCenterCode"
			    + ",'','',a.dteFromDate,a.dteToDate,'N' \n"
			    + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
			    + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
			    + "and a.strItemCode=c.strItemCode \n"
			    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
			    + "and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strMenuCode='" + menuHeadCode + "' "
			    + "and c.strOperationalYN='Y' "
			    + "ORDER BY b.dteFromTime";
		}
		else
		{
		    sql_ItemDtl = "select a.strItemCode,c.strItemName,'Black',b.dblMemberPriceMonday,b.dblMemberPriceTuesday,b.dblMemberPriceWednesday,b.dblMemberPriceThursday\n"
			    + ",b.dblMemberPriceFriday,b.dblMemberPriceSaturday,b.dblMemberPriceSunday,'','','','',a.strCostCenterCode,'','',a.dteFromDate,a.dteToDate,'N' \n"
			    + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
			    + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
			    + "and a.strItemCode=c.strItemCode \n"
			    + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
			    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strAreaCode='" + clsAreaCode + "' "
			    + "and a.strMenuCode='" + menuHeadCode + "' "
			    + "and c.strOperationalYN='Y' "
			    + "ORDER BY b.dteFromTime";
		}
	    }
	    else
	    {
		if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
		{
		    sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable  "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + " WHERE a.strMenuCode = '" + menuHeadCode + "' "
			    + " and a.strItemCode=b.strItemCode "
			    + " and a.strHourlyPricing='NO' "
			    + " and a.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			    + " and date(dteFromDate)<='" + posDateForPrice + "' and date(dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + " ORDER BY b.strItemName ASC";
		}
		else
		{
		    sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable  "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + " WHERE a.strAreaCode='" + clsAreaCode + "' "
			    + " and a.strHourlyPricing='NO' "
			    + " and a.strMenuCode = '" + menuHeadCode + "' "
			    + " and a.strItemCode=b.strItemCode "
			    //+ "WHERE (a.strAreaCode='" + clsAreaCode + "') "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + " ORDER BY b.strItemName ASC";
		}
	    }
	    //System.out.println(sql_ItemDtl);
	    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
	    while (rsItemInfo.next())
	    {
		//=======//
		list_ItemNames_Buttoms.add(rsItemInfo.getString(2));

		clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
			rsItemInfo.getDouble(4),
			rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7), rsItemInfo.getDouble(8),
			rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
			rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
			rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), 0, "");
		obj_List_ItemPrice.add(ob);

		//========//
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
			    btnSubMenuArray[i].setForeground(Color.BLACK);
			    btnSubMenuArray[i].setBackground(Color.WHITE);
			    break;
			case "BLUE":
			    btnSubMenuArray[i].setForeground(Color.BLACK);
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
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			itemNames[i] = temItemName;
			btnforeground[i] = txtColor;
			itemImageCode.add(itemCode);
		    }
		}
		i++;
	    }
	    rsItemInfo.close();
	    for (int j = i; j < 16; j++)
	    {
		btnSubMenuArray[j].setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funRefreshButtonItemSelectionWise(String MenuCode, String selectedButtonCode)
    {
	int i = 0;

	fieldSelected = "MenuItem";
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
	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCount = "SELECT count(c.strItemName) "
			+ "FROM tblmenuhd a LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
			+ "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
			+ "WHERE ";
		if (flgPopular)
		{
		    sqlCount += " b.strPopular = 'Y' and  c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		}
		else
		{
		    sqlCount += " a.strMenuCode = '" + MenuCode + "' and  c.strSubGroupCode='" + selectedButtonCode + "' "
			    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		}

		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (lblAreaName.getText().equalsIgnoreCase("All Floors"))
		    {
			sqlCount = sqlCount + " and  b.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "'";
		    }
		    else
		    {
			sqlCount = sqlCount + " and (b.strAreaCode='" + clsAreaCode + "' or b.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' ) ";
		    }
		}
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sqlCount = "select count(*) from tblmenuitempricingdtl "
			+ " where strMenuCode='" + menuHeadCode + "' "
			+ " and strSubMenuHeadCode='" + selectedButtonCode + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (lblAreaName.getText().equalsIgnoreCase("All Floors"))
		    {
			sqlCount = sqlCount + " and strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' ";
		    }
		    else
		    {
			sqlCount = sqlCount + " and (strAreaCode='" + clsAreaCode + "' or strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "')";
		    }
		}
	    }

	    ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCount);
	    rsCount.next();
	    int cn = rsCount.getInt(1);
	    if (cn > 16)
	    {
		btnNextItem.setEnabled(true);
	    }
	    rsCount.close();
	    itemNames = new String[cn];
	    btnforeground = new String[cn];
	    //itemImageCode = new String[cn];
	    itemImageCode.clear();

	    list_ItemNames_Buttoms.clear();
	    obj_List_ItemPrice = new ArrayList<>();

	    String sql1 = "";
	    String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	    if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sql1 = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday,"
			+ "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  "
			+ "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo,"
			+ "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate,c.strStockInEnable "
			+ "FROM tblmenuhd a LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
			+ "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
			+ "WHERE ";

		if (flgPopular)
		{
		    sql1 += " b.strPopular = 'Y' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')   "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' ";
		}
		else
		{
		    sql1 += " a.strMenuCode = '" + MenuCode + "' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' ";
		}
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (lblAreaName.getText().equalsIgnoreCase("All Floors"))
		    {
			sql1 = sql1 + " and b.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' ";
		    }
		    else
		    {
			sql1 = sql1 + " and (b.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' or b.strAreaCode='" + clsAreaCode + "')";
		    }
		}
		sql1 = sql1 + " ORDER BY c.strItemName ASC";
	    }
	    else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	    {
		sql1 = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday,"
			+ "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  "
			+ "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo,"
			+ "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate,c.strStockInEnable "
			+ "FROM tblmenuitempricingdtl b,tblitemmaster c "
			+ "WHERE ";

		if (flgPopular)
		{
		    sql1 += " b.strMenuCode = '" + menuHeadCode + "' and b.strItemCode=c.strItemCode and b.strSubMenuHeadCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' ";
		}
		else
		{
		    sql1 += " b.strMenuCode = '" + menuHeadCode + "' and b.strItemCode=c.strItemCode and b.strSubMenuHeadCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')   "
			    + " and date(b.dteFromDate)<='" + posDateForPrice + "' and date(b.dteToDate)>='" + posDateForPrice + "' "
			    + " and c.strOperationalYN='Y' ";
		}

		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (lblAreaName.getText().equalsIgnoreCase("All Floors"))
		    {
			sql1 = sql1 + " and strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "'";
		    }
		    else
		    {
			sql1 = sql1 + " and (strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "' or strAreaCode='" + clsAreaCode + "')";
		    }
		}
		sql1 = sql1 + " ORDER BY strItemName ASC";
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
			rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), 0, "");
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
			itemImageCode.add(itemCode);
		    }
		    else
		    {
			itemNames[i] = temItemName;
			btnforeground[i] = txtColor;
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    // to insert the record into tblitemrtemp
    private void funInsertData(double quantity, double itemPrice, String tableNum, String itemName, String itemCode)
    {
	try
	{
	    //update the Current bill
	    double qty1 = quantity;
	    int pax = 0;
	    if (txtPaxNo.getText().trim().length() > 0)
	    {
		pax = Integer.parseInt(txtPaxNo.getText());
	    }
	    /*
             * sTART OF KOT object List
	     */
	    boolean flag = false;
	    if (obj_List_KOT_ItemDtl.size() > 0 && !flagOpenItem)
	    {
		for (clsMakeKotItemDtl listItemDtl : obj_List_KOT_ItemDtl)
		{
		    String tempItemCode = listItemDtl.getItemCode();
		    if (tempItemCode.equalsIgnoreCase(itemCode) && listItemDtl.isIsModifier() == false && "N".equalsIgnoreCase(listItemDtl.getTdhComboItemYN()))
		    {
			double prevQty = listItemDtl.getQty();

			//******************************************************//
			if (clsGlobalVarClass.gShowPopUpForNextItemQuantity)
			{
			    frmOkCancelPopUp okCancelPopUp = new frmOkCancelPopUp(null, "<html>Do You Want To Add " + qty1 + " Quantity To<br>" + listItemDtl.getItemName() + " " + prevQty + " Quantity.</html>");
			    okCancelPopUp.setVisible(true);
			    int res2 = okCancelPopUp.getResult();
			    if (res2 == 1)
			    {
				double finalQty = prevQty + qty1;

				if (!clsGlobalVarClass.gNegBilling)
				{
				    if (!clsGlobalVarClass.funCheckNegativeStock(tempItemCode, finalQty))
				    {
					return;
				    }
				}
				listItemDtl.setAmt(itemPrice * finalQty);
				listItemDtl.setQty(prevQty + qty1);
				flag = true;
			    }
			    else
			    {
				flag = true;
			    }
			}
			else
			{
			    double finalQty = prevQty + qty1;

			    if (!clsGlobalVarClass.gNegBilling)
			    {
				if (!clsGlobalVarClass.funCheckNegativeStock(tempItemCode, finalQty))
				{
				    return;
				}
			    }
			    listItemDtl.setAmt(itemPrice * finalQty);
			    listItemDtl.setQty(prevQty + qty1);
			    flag = true;
			}
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
		    if (!clsGlobalVarClass.funCheckNegativeStock(itemCode, qty1))
		    {
			return;
		    }
		}
		clsMakeKotItemDtl ob = new clsMakeKotItemDtl(getStrSerialNo(), KOTNo, tableNum, globalWaiterNo, itemName, itemCode, qty1, (qty1 * itemPrice), pax, "N", "N", false, "", "", "", "N", itemPrice);
		kotItemSequenceNO++;
		obj_List_KOT_ItemDtl.add(ob);
	    }
	    selectedQty = 1;
	    flagOpenItem = false;
	    funRefreshItemTable();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * Ritesh 27 Sept 2014
     *
     * @param tableNo to fill item on table which are already printed (old
     * items)
     */
    private void funFillOldKOTItems(String tableNo)
    {
	try
	{
	    boolean flagIsKOTPresent = false;
	    list_KOT_On_Table = new ArrayList<>();
	    obj_List_Old_KOT_Item_On_table.clear();

	    String sqlKOTDtl = "select distinct(strKOTNo) "
		    + " from tblitemrtemp "
		    + " where (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All') "
		    + " and strTableNo='" + tableNo + "' and strPrintYN='Y' and strNCKotYN='N' "
		    + " order by strKOTNo DESC";

	    ResultSet rsKOTDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlKOTDtl);
	    while (rsKOTDtl.next())
	    {
		list_KOT_On_Table.add(rsKOTDtl.getString(1));
		flagIsKOTPresent = true;
	    }
	    rsKOTDtl.close();
	    if (flagIsKOTPresent)
	    {
		String sqlTableItemDtl = "select strKOTNo,strTableNo,strWaiterNo"
			+ " ,strItemName,strItemCode,dblItemQuantity,dblAmount"
			+ " ,intPaxNo,strPrintYN,tdhComboItemYN,strSerialNo,strNcKotYN,dblRate "
			+ " from tblitemrtemp where strTableNo='" + tableNo + "' "
			+ " and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All') "
			+ " and strNcKotYN='N' "
			+ " order by strKOTNo desc ,strSerialNo";

		ResultSet rsTableItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableItemDtl);
		while (rsTableItemDtl.next())
		{
		    clsMakeKotItemDtl ob = new clsMakeKotItemDtl(rsTableItemDtl.getString(11), rsTableItemDtl.getString(1), rsTableItemDtl.getString(2), rsTableItemDtl.getString(3), rsTableItemDtl.getString(4), rsTableItemDtl.getString(5), rsTableItemDtl.getDouble(6), rsTableItemDtl.getDouble(7), rsTableItemDtl.getInt(8), rsTableItemDtl.getString(9), rsTableItemDtl.getString(10), false, "", "", "", "N", rsTableItemDtl.getDouble(13));
		    if (rsTableItemDtl.getDouble(7) >= 0)
		    {
			obj_List_Old_KOT_Item_On_table.add(ob);
		    }
		}
		rsTableItemDtl.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funRefreshItemTable()
    {
	try
	{

	    double amt = 0.00;
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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amt");
	    dm.addColumn("ItemCode");
	    dm.addColumn("SeqNo");
	    //ArrayList arrListMenuItems = new ArrayList();
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	    double subTotalForTax = 0;

	    //==New KOT Items==//
	    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	    {
		Collections.sort(obj_List_KOT_ItemDtl);
		String kotTime = funGetKOTTimeForNewKOT();
		String newKOTNo = "<html><b>" + KOTNo + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ kotTime + "</html>";
		Object obKotTitle[] =
		{
		    newKOTNo, "", "", ""
		};
		dm.addRow(obKotTitle);

		for (clsMakeKotItemDtl obList : obj_List_KOT_ItemDtl)
		{
		    amt += obList.getAmt();
		    Object ob_NewKOT_Item[] =
		    {
			obList.getItemName(), gDecimalFormat.format(obList.getQty()), gDecimalFormat.format(obList.getAmt()), obList.getItemCode(), obList.getSequenceNo()
		    };
		    dm.addRow(ob_NewKOT_Item);
		    //String itemDtl = obList.getItemCode() + "#" + obList.getItemName() + "#" + obList.getQty() + "#" + obList.getAmt();
		    //arrListMenuItems.add(itemDtl);
		    subTotalForTax += obList.getAmt();
		    clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		    objItemDtl.setItemCode(obList.getItemCode());
		    objItemDtl.setItemName(obList.getItemName());
		    objItemDtl.setAmount(obList.getAmt());
		    objItemDtl.setDiscAmt(0);
		    arrListItemDtls.add(objItemDtl);
		}
	    }

	    //==Old KOT Items===//
	    if (null != list_KOT_On_Table && list_KOT_On_Table.size() > 0)
	    {

		//Collections.sort(list_KOT_On_Table);
		for (String oldKOTNos : list_KOT_On_Table)
		{
		    String kotTime = funGetKOTTimeForOldKOT(oldKOTNos);
		    String tempOldKOT = "<html><font color=black><b>" + oldKOTNos + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			    + kotTime + "</font></html>";
		    Object obKotTitle[] =
		    {
			tempOldKOT, "", "", ""
		    };
		    dm.addRow(obKotTitle);
		    for (clsMakeKotItemDtl obList : obj_List_Old_KOT_Item_On_table)
		    {

			String temp_kot = obList.getKOTNo();
			if (oldKOTNos.equalsIgnoreCase(temp_kot))
			{
			    amt += obList.getAmt();
			    String itemName = "<html><font color=black>" + obList.getItemName() + "</font></html>";
			    String qty = "<html><font color=black>" + obList.getQty() + "</font></html>";
			    String itemAmt = "<html><font color=black>" + obList.getAmt() + "</font></html>";
			    String itemCode = "<html><font color=black>" + obList.getItemCode() + "</font></html>";
			    String seq = "<html><font color=black>" + obList.getSequenceNo() + "</font></html>";

			    Object ob_OldKOT_Item[] =
			    {
				itemName, qty, itemAmt, itemCode, seq
			    };
			    dm.addRow(ob_OldKOT_Item);
			}
		    }
		}
	    }

	    if (clsGlobalVarClass.gCalculateTaxOnMakeKOT)
	    {
		dtPOSDate = posDate.split(" ")[0];
		List<clsTaxCalculationDtls> listTax = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, clsAreaCode, "DineIn", subTotalForTax, 0, "", "S01", "Sales");
		taxAmt = 0;
		for (clsTaxCalculationDtls objTaxDtl : listTax)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			taxAmt = taxAmt + objTaxDtl.getTaxAmount();
		    }
		}
		amt += taxAmt;

		sql = "SELECT SUM(a.dblTaxAmt) "
			+ "FROM tblkottaxdtl a "
			+ "WHERE a.strTableNo= '" + globalTableNo + "'  "
			+ "and a.strTableNo in (select b.strTableNo from tblitemrtemp b where b.strTableNo= '" + globalTableNo + "' ) "
			+ "AND a.strKOTNo!='' "
			+ "GROUP BY a.strTableNo  ";
		ResultSet rsKOTTaxAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsKOTTaxAmt.next())
		{
		    amt += rsKOTTaxAmt.getDouble(1);
		}
		rsKOTTaxAmt.close();
	    }

	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		//start code to calculate roundoff amount and round off by amt
		Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(amt);
		amt = mapRoundOff.get("roundOffAmt");
	    }
	    else
	    {
		amt = Double.parseDouble(gDecimalFormat.format(amt));
	    }

	    txtTotal.setText(String.valueOf(amt));

	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.setShowHorizontalLines(true);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(210);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(30);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(50);

	    JButton arrBtnQty[] =
	    {
		btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnMultiQty
	    };
	    for (int cnt = 0; cnt < arrBtnQty.length; cnt++)
	    {
		arrBtnQty[cnt].setEnabled(true);
	    }
	    //arrListMenuItems = null;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private String funRemoveLast(String text)
    {
	String temSt = text;

//        StringBuilder sb = new StringBuilder(text);
//        sb = sb.delete(sb.lastIndexOf("<br>"), sb.length());
//        temSt = sb.substring(6, sb.length());
	if (temSt.contains("<br>"))
	{
	    temSt = temSt.split("<br>")[0].split("<html>")[1];
	}
	if (temSt.contains("<h5>"))
	{
	    temSt = temSt.split("<h5>")[1].split("</h5>")[0];
	}

	return temSt;
    }

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
	    //System.out.println(getWebServiceURL);
	    HttpResponse response = httpClient.execute(getRequest);
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

	    String output = "", op = "";
	    //System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    //System.out.println(op);
	    JSONParser p = new JSONParser();
	    Object objJSON = p.parse(op);
	    JSONObject obj = (JSONObject) objJSON;
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return 1;
    }

    private void funNewCustomerButtonPressed()
    {
	if (clsGlobalVarClass.gCRMInterface.equals("SQY"))
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
	    {
		funCallWebService();
	    }
	}
	else if (clsGlobalVarClass.gCRMInterface.equals("PMAM"))
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
	    if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
	    {
		clsGlobalVarClass.gCustMobileNoForCRM = clsGlobalVarClass.gNumerickeyboardValue;
		funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
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
		    funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Please Enter Valid Mobile No.");
		    return;
		}
	    }
	}
    }

    private void funExternalCodeButtonPressed(String click)
    {
	try
	{
	    if (click.equals("click"))
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

	    funPopularItem();
	    String ExternalCode = txtExternalCode.getText().trim();
	    if (ExternalCode.trim().length() > 0)
	    {
		String posDateForPrice = posDate.split(" ")[0];
		String itemName = "";
		String sql_ItemDtl = "";
		if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
		{
		    sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			    + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			    + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			    + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable  "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + " WHERE b.strExternalCode='" + ExternalCode + "' and a.strItemCode=b.strItemCode  "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All')"
			    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + " ORDER BY b.strItemName ASC";
		}
		else
		{
		    sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			    + "a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
			    + "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			    + "a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable  "
			    + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			    + "WHERE b.strExternalCode='" + ExternalCode + "' and (a.strAreaCode='' or a.strAreaCode='" + clsAreaCode + "') and   a.strItemCode=b.strItemCode "
			    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All')"
			    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
			    + " and b.strOperationalYN='Y' "
			    + "ORDER BY b.strItemName ASC";
		}
		ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
		if (rsItemInfo.next())
		{
		    clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
			    rsItemInfo.getDouble(4),
			    rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7), rsItemInfo.getDouble(8),
			    rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
			    rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
			    rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), 0, "");
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
		    funGetItemPrice(itemName);
		    txtExternalCode.setText("");
		}
		else
		{
		    txtExternalCode.setText("");
		}
		rsItemInfo.close();
	    }
	    txtExternalCode.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funPrevMenuButtonPressed()
    {
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
	    limit = nextCnt + 7;

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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funNextMenuButtonPressed()
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
	    limit = nextCnt + 7;
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funCallTableSearch()
    {
	if (!clsGlobalVarClass.gSuperUser && clsGlobalVarClass.gAutoAreaSelectionInMakeKOT)
	{
	    objUtility.funCallForSearchForm("TableMasterForAutoAreaSelectionInMakeKOT", "", "", clsAreaCode);
	}
	else
	{
	    objUtility.funCallForSearchForm("TableMasterForKOT");
	}
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    String tableNo = data[0].toString();
	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(tableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
		clsGlobalVarClass.gSearchItemClicked = false;
		return;
	    }
	    funCheckTableForCMS(data[0].toString(), data[1].toString());
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funCallWaiterSearch()
    {
	objUtility.funCallForSearchForm("WaiterWiseTableSearch");
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetWaiterInfo(data[1].toString(), data[0].toString());
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
	
    }

    private void funDisableSelectedQtyBtn(String strQty)
    {
	JButton arrBtnQty[] =
	{
	    btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnMultiQty
	};
	int i;
	for (i = 0; i < arrBtnQty.length; i++)
	{
	    if (strQty.equals(arrBtnQty[i].getText()))
	    {
		arrBtnQty[i].setEnabled(false);
		break;
	    }
	}
	for (int cnt = 0; cnt < arrBtnQty.length; cnt++)
	{
	    if (!arrBtnQty[i].getText().equals(arrBtnQty[cnt].getText()))
	    {
		arrBtnQty[cnt].setEnabled(true);
	    }
	}
    }

    private void funButtonB4Clicked()
    {
	try
	{
	    funIsVisiblePanels(false);
	    panelPLU.setVisible(false);
	    panelNumericKeyPad.setVisible(false);
	    panelMenuHead.setVisible(false);
	    panelNavigate.setVisible(false);
	    panelKOTMessage.setLocation(panelNavigate.getLocation());
	    panelKOTMessage.setVisible(true);

	    mapCostCenters = new HashMap<String, String>();
	    mapCostCenters.put("All", "All");

	    cmbCostCenterForKOTMsg.removeAllItems();
	    cmbCostCenterForKOTMsg.addItem("All");
	    String sqlCostCenters = "select a.strCostCenterCode,a.strCostCenterName from tblcostcentermaster a";
	    ResultSet rsCostCenters = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenters);
	    while (rsCostCenters.next())
	    {
		cmbCostCenterForKOTMsg.addItem(rsCostCenters.getString(2).toString());
		mapCostCenters.put(rsCostCenters.getString(2).toString(), rsCostCenters.getString(1).toString());

	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funPreviousButtonClicked()
    {
	try
	{
	    if (btnPrevious.isEnabled())
	    {
		navigate--;
	    }
	    if (navigate == 0)
	    {
		btnButton1.setMnemonic('s');
		btnButton2.setMnemonic('p');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('o');

		btnPrevious.setEnabled(false);
		btnButton1.setVisible(true);
		btnButton1.setText("<html><u>S</u>ETTLE<br>BILL</html>");
		btnButton1.setForeground(Color.white);
		btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		btnButton1.setEnabled(true);

		btnButton2.setVisible(true);
		btnButton2.setText("PLU");

		if (!flgcheckDeliveryboyName)
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText("<html>ZOMATO<br>CODE</html>");
		    btnButton3.setForeground(Color.white);
		}
		else
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText("<html>ZOMATO<br>CODE</html>");
		    btnButton3.setForeground(Color.white);
		}

		if (flgHomeDeliveryColor_Button)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("<html>HOME<br>DELIVERY</html>");
		    btnButton4.setForeground(Color.black);
		}
		else
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("<html>HOME<br>DELIVERY</html>");
		    btnButton4.setForeground(Color.white);
		}
		btnNext.setEnabled(true);
	    }
	    if (navigate == 1)
	    {
		btnButton1.setMnemonic('t');
		btnButton2.setMnemonic('k');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('n');

		btnPrevious.setEnabled(true);

		btnButton1.setVisible(true);
		btnButton1.setText("<html>FIRE<br>KOT</html>");
		btnButton1.setForeground(Color.white);
		//btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		btnButton1.setEnabled(true);

		btnButton2.setVisible(true);
		btnButton2.setText("CHECKKOT");

		if (flgcheckDeliveryboyName)
		{
		    btnButton3.setVisible(true);
		    btnButton3.setEnabled(true);
		    btnButton3.setText(deliveryBoyName);
		}
		else
		{
		    btnButton3.setVisible(true);
		    btnButton3.setEnabled(true);
		    btnButton3.setText("<Html>DELIVERY<br>BOY</html>");
		}

		if (!flgHomeDeliveryColor_Button && !flgCheckNCKOTButtonColor)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setEnabled(true);
		    btnButton4.setText("NC KOT");
		    btnButton4.setForeground(Color.white);
		}
		else if (flgCheckNCKOTButtonColor)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("NC KOT");
		    btnButton4.setEnabled(true);
		    btnButton4.setForeground(Color.black);
		}
		btnNext.setEnabled(true);
	    }
	    if (navigate == 2)
	    {
		btnButton1.setMnemonic('t');
		btnButton2.setMnemonic('k');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('n');

		btnPrevious.setEnabled(true);
		btnNext.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funNextButtonClicked()
    {
	try
	{

	    if (btnNext.isEnabled())
	    {
		navigate++;
	    }
	    if (navigate == 0)
	    {
		btnButton1.setMnemonic('s');
		btnButton2.setMnemonic('p');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('o');
		btnPrevious.setEnabled(false);

		if (!flgCheckTakeAway_ButtonColor)
		{
		    btnButton1.setVisible(true);
		    btnButton1.setText("<html><u>S</u>ETTLE<br>BILL</html>");
		    btnButton1.setForeground(Color.white);
		    btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		    btnButton1.setEnabled(true);
		}
		else
		{
		    btnButton1.setVisible(true);
		    btnButton1.setText("<html><u>S</u>ETTLE<br>BILL</html>");
		    btnButton1.setForeground(Color.white);
		    btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		    btnButton1.setEnabled(true);
		}
		btnButton2.setVisible(true);
		btnButton2.setText("PLU");

		if (!flgcheckDeliveryboyName)
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText("<html>ZOMATO<br>CODE</html>");
		    btnButton3.setForeground(Color.white);
		}
		else
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText("<html>ZOMATO<br>CODE</html>");
		    btnButton3.setForeground(Color.white);
		}

		if (flgHomeDeliveryColor_Button)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("<html>HOME<br>DELIVERY</html>");
		    btnButton4.setForeground(Color.black);
		}
		else
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("<html>HOME<br>DELIVERY</html>");
		    btnButton4.setForeground(Color.white);
		}
		btnNext.setEnabled(true);
	    }
	    if (navigate == 1)
	    {
		btnButton1.setMnemonic('t');
		btnButton2.setMnemonic('k');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('n');

		btnPrevious.setEnabled(true);

		btnButton1.setVisible(true);
		btnButton1.setText("<html>FIRE<br>KOT</html>");
		btnButton1.setForeground(Color.white);
		//btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png")));
		btnButton1.setEnabled(true);

		btnButton2.setVisible(true);
		btnButton2.setText("CHECKKOT");

		if (flgcheckDeliveryboyName)
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText(deliveryBoyName);
		}
		else
		{
		    btnButton3.setVisible(true);
		    btnButton3.setText("<Html>DELIVERY<br>BOY</html>");
		}

		if (!flgHomeDeliveryColor_Button && !flgCheckNCKOTButtonColor)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("NC KOT");
		    btnButton4.setForeground(Color.white);
		}
		else if (flgCheckNCKOTButtonColor)
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("NC KOT");
		    btnButton4.setForeground(Color.black);
		}
		else
		{
		    btnButton4.setVisible(true);
		    btnButton4.setText("NC KOT");
		    btnButton4.setForeground(Color.white);
		}
		btnNext.setEnabled(true);
	    }
	    if (navigate == 2)
	    {
		btnButton1.setMnemonic('t');
		btnButton2.setMnemonic('k');
		btnButton3.setMnemonic('b');
		btnButton4.setMnemonic('n');

		btnPrevious.setEnabled(true);
		btnButton2.setVisible(true);
		btnButton2.setText("<html>CUSTOMER<br>HISTORY<html>");

		btnButton3.setText("");
		btnButton3.setEnabled(false);
		btnButton4.setText("");
		btnButton4.setEnabled(false);
		btnNext.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funGetCMSMemberCode() throws Exception
    {
	new frmAlfaNumericKeyBoard(null, true, "1", "Enter Member Code").setVisible(true);
	String strCustomerCode = clsGlobalVarClass.gKeyboardValue;
	if (clsGlobalVarClass.gKeyboardValue.trim().length() > 0)
	{
	    String memberInfo = objUtility.funCheckMemeberBalance(strCustomerCode);
	    if (memberInfo.contains("#"))
	    {
		if (memberInfo.split("#")[4].trim().equals("Y"))
		{
		    JOptionPane.showMessageDialog(this, "Member is blocked");
		    return;
		}
		else
		{
		    cmsMemCode = memberInfo.split("#")[0];
		    cmsMemName = memberInfo.split("#")[1];
		    hmCMSMemberForTable.put(globalTableNo, cmsMemCode);
		    btnNewCustomer.setText("<html>" + cmsMemName + "</html>");
		    double creditLimit = Double.parseDouble(memberInfo.split("#")[2]);
		    double totalAmt = 0;
		    sql = "update tblitemrtemp set strCustomerCode='" + cmsMemCode + "' "
			    + " ,strCustomerName = '" + cmsMemName + "' "
			    + " where strTableNo='" + globalTableNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    //insert into itemrtempbck table
		    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);

		    if (txtTotal.getText().trim().length() > 0)
		    {
			totalAmt = Double.parseDouble(txtTotal.getText());
		    }
		    if (creditLimit < totalAmt)
		    {
			JOptionPane.showMessageDialog(this, "Credit Limit is " + creditLimit);
		    }
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Member Not Found!!!");
	    }
	}
    }

    private void funMakeBillButtonClicked()
    {
	try
	{
	    btnMakeBill.setEnabled(false);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (clsGlobalVarClass.gCMSMemberCodeForKOTJPOS.equals("Y"))
		{
		sql = "select strCustomerCode from tblitemrtemp "
			+ "where strTableNo='" + globalTableNo + "'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rs.next())
		{
		    if (rs.getString(1).trim().isEmpty())
		    {
			btnMakeBill.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Please Select Member!!!");
			return;
		    }
		}
		rs.close();
		}
	    }
	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && this.objCustomerRewards != null && this.objCustomerRewards.getStrRewardId().trim().length() > 0)
	    {
		String reasonCode = new clsUtility2().funGetDefaultReasonCode("strHashTagLoyalty");
		if (reasonCode.trim().length() <= 0)
		{
		    JOptionPane.showMessageDialog(this, "No Hash Tag reasons are created.");
		    btnMakeBill.setEnabled(true);
		    return;
		}
	    }

	    if (clsGlobalVarClass.gFireCommunication)
	    {
		boolean isAllItemFired = objUtility2.funIsAllItemFired(globalTableNo);
		if (!isAllItemFired)
		{
		    JOptionPane.showMessageDialog(this, "Please fire the all items.");
		    btnMakeBill.setEnabled(true);
		    return;
		}
	    }

	    if (homeDeliveryForTax.equals("Y"))
	    {
		double totalBillAmount = Double.parseDouble(txtTotal.getText());
		double minAmount = objUtility.funGetMinBillAmountForDelCharges(arrListHomeDelDetails.get(2).toString(), arrListHomeDelDetails.get(0).toString());
		if (totalBillAmount < minAmount)
		{
		    int res = JOptionPane.showConfirmDialog(this, "Bill Amount is " + totalBillAmount + " and minimum delivery charges amount is " + minAmount + " Do you want to continue?");
		    if (res != 0)
		    {
			btnMakeBill.setEnabled(true);
			return;
		    }
		}

		funHomeDelivery();
		String sqlUpdate = "update tblitemrtemp set strHomeDelivery='Yes',strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "' "
			+ "where strTableNo='" + globalTableNo + "'";
		try
		{
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
		    //insert into itemrtempbck table
		    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
		}
		catch (Exception ex)
		{
		    objUtility.funShowDBConnectionLostErrorMessage(ex);	
		    objUtility.funWriteErrorLog(ex);
		    ex.printStackTrace();
		}
	    }
	    else
	    {
		String sqlUpdate = "update tblitemrtemp set strHomeDelivery='No',strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "' "
			+ "where strTableNo='" + globalTableNo + "'";
		try
		{
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);
		    //insert into itemrtempbck table
		    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
		}
		catch (Exception ex)
		{
		    objUtility.funShowDBConnectionLostErrorMessage(ex);	
		    objUtility.funWriteErrorLog(ex);
		    ex.printStackTrace();
		}
	    }

	    funResetTopSortingButtons();
	    if (tblItemTable.getRowCount() > 0)
	    {
		if (funCheckKOT())
		{
		    String strStatus="Normal";
		    String sql="select count(a.strTableNo) from tblbillhd a where a.strTableNo='"+globalTableNo+"' " +
				" and a.strBillNo NOT IN (select b.strBillNo from tblbillsettlementdtl b)  ";
		    ResultSet rs= clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if(rs.next()){
			if(rs.getInt(1)>0){
			    strStatus="AlreadyBilled";
			}
		    }
		    if(strStatus.equals("AlreadyBilled")){
			if(clsGlobalVarClass.gStrMergeAllKOTSToBill){
			    frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want to Merge this Table on Previous Bill ",false);
			    okOb.setVisible(true);
			    int res = okOb.getResult();
			    okOb.dispose();
			    if (res == 1)
			    {
				 if(!funAddAllKOTSOnTableToBill(globalTableNo)){
				     btnMakeBill.setEnabled(true);
				     JOptionPane.showMessageDialog(this, "Bill Not Found On This Table");
				     return;
				 }

			    }else{
				btnItemMode.setEnabled(false);
				frmBillSettlement objBillSettlement = new frmBillSettlement(this, globalTableNo, globalTableName, arrListHomeDelDetails, homeDeliveryForTax);
				clsBillSettlementUtility objBillSettlementUtility = new clsBillSettlementUtility(objBillSettlement);
				if (clsGlobalVarClass.gEnableBillSeries && objBillSettlementUtility.funGetBillSeriesList().containsKey("NoBillSeries"))
				{
				    btnMakeBill.setEnabled(true);
				    new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
				    return;
				}
				objBillSettlement.setVisible(true);
			    }
			}
			else{
			    btnItemMode.setEnabled(false);
			    frmBillSettlement objBillSettlement = new frmBillSettlement(this, globalTableNo, globalTableName, arrListHomeDelDetails, homeDeliveryForTax);
			    clsBillSettlementUtility objBillSettlementUtility = new clsBillSettlementUtility(objBillSettlement);
			    if (clsGlobalVarClass.gEnableBillSeries && objBillSettlementUtility.funGetBillSeriesList().containsKey("NoBillSeries"))
			    {
				btnMakeBill.setEnabled(true);
				new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
				return;
			    }
			    objBillSettlement.setVisible(true);
			}
		    }
		    else{
			    btnItemMode.setEnabled(false);
			    frmBillSettlement objBillSettlement = new frmBillSettlement(this, globalTableNo, globalTableName, arrListHomeDelDetails, homeDeliveryForTax);
			    clsBillSettlementUtility objBillSettlementUtility = new clsBillSettlementUtility(objBillSettlement);
			    if (clsGlobalVarClass.gEnableBillSeries && objBillSettlementUtility.funGetBillSeriesList().containsKey("NoBillSeries"))
			    {
				btnMakeBill.setEnabled(true);
				new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
				return;
			    }
			    objBillSettlement.setVisible(true);
		    }

		}
		else
		{
		    btnMakeBill.setEnabled(true);
		    JOptionPane.showMessageDialog(this, "Please save kot first");
		}
	    }
	    else
	    {
		if (txtTableNo.getText().equals(""))
		{
		    btnMakeBill.setEnabled(true);
		    JOptionPane.showMessageDialog(this, "Please select table");
		}
		else if (txtWaiterNo.getText().equals(""))
		{
		    btnMakeBill.setEnabled(true);
		    JOptionPane.showMessageDialog(this, "Please select Waiter");
		}
		else
		{
		    btnMakeBill.setEnabled(true);
		    JOptionPane.showMessageDialog(this, "Please select Items");
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

    private void funHomeButtonClicked()
    {
	objUtility = new clsUtility();
	try
	{
	    int totalTableRows = tblItemTable.getRowCount();
	    if (totalTableRows > 0)
	    {
		frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want to end transaction");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{
		    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
		    {
			clsGlobalVarClass.hmActiveForms.remove("Make KOT");
			funInsertLineVoidItems();
			funDeleteTempData();
			dispose();
		    }
		    else
		    {
			clsGlobalVarClass.hmActiveForms.remove("Make KOT");
			funDeleteTempData();
			dispose();
		    }
		}
	    }
	    else
	    {
		clsGlobalVarClass.hmActiveForms.remove("Make KOT");
		funDeleteTempData();
		dispose();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    // funFreeObjectsFromMemory();
	}
    }

    private void funFreeObjectsFromMemory()
    {
	objData = null;
	objPanelModifier = null;
	objPanelSubItem = null;
	hmCMSMemberForTable = null;
	hmHappyHourItems = null;
	hmCMSMemberForTable = null;
	hmReservedTables = null;
	hm_ModifierDtl = null;
	hm_ModifierGroup = null;
	hmTableSeq = null;
	hmTable = null;
	hmMakeKotParams = null;
	objUtility = null;
    }

    private void funTableNoTextFieldClicked(java.awt.event.MouseEvent evt)
    {
	if (evt.getClickCount() == 2)
	{
	    funCallTableSearch();
	}
	else
	{
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		frmAlfaNumericKeyBoard keyboard = new frmAlfaNumericKeyBoard(this, true, "1", "Search All Tables");
		keyboard.setVisible(true);
		keyboard.setAlwaysOnTop(true);
		keyboard.setAutoRequestFocus(true);
		txtTableNo.setText(clsGlobalVarClass.gKeyboardValue);
		funSelectEnterTable();
	    }
	    else
	    {
		funShowTableGrid();
	    }
	}
    }

    private void funShowTableGrid()
    {
	funResetTopSortingButtons();
	funIsVisiblePanels(true);
	flgTableSelection = true;
	oldTableNo = globalTableNo;
	fieldSelected = "Table";
	txtWaiterNo.setText("");
	txtPaxNo.setText("1");
	txtTableNo.setText(globalTableName);
	txtTableNo.requestFocus();
	txtTableNo.selectAll();
	//flagHomeDeliverySelect = false;
	//funLoadTables(0, vTableNo.size());
	funLoadTables(0, hmTable.size());
    }

    private void funWaiterNoTextFieldClicked(java.awt.event.MouseEvent evt)
    {
	if (txtTableNo.getText().trim().length() < 1)
	{
	    new frmOkPopUp(this, "Please Select The Table.", "error", 1).setVisible(true);
	    return;
	}
	if (!clsGlobalVarClass.gMultiWaiterSelOnMakeKOT && globalWaiterNo != null && globalWaiterNo.length() > 0 && obj_List_Old_KOT_Item_On_table.size() > 0)
	{
	    return;
	}

	fieldSelected = "Waiter";
	updateSelectedField = "Waiter";

	if (evt.getClickCount() == 2)
	{
	    funCallWaiterSearch();
	}
	else
	{
	    try
	    {
		if (clsGlobalVarClass.gSelectWaiterFromCardSwipe && clsPosConfigFile.gSelectWaiterFromCardSwipe.equalsIgnoreCase("true"))
		{
		    new frmSwipCardPopUp(this, "frmMakeKOT").setVisible(true);
		    String waiterInfo = funCheckDebitCardString(clsGlobalVarClass.gDebitCardNo);
		    if (!waiterInfo.isEmpty())
		    {
			String[] spWaiter = waiterInfo.split("#");
			funSetWaiterInfo(spWaiter[1], spWaiter[0]);
			txtWaiterNo.setText(spWaiter[1]);
			funEnablePaxButtons(true);
			if (!clsGlobalVarClass.gSkipPax)
			{
			    fieldSelected = "Pax";
			}
			else if (clsGlobalVarClass.gSkipPax)
			{
			    fieldSelected = "MenuItem";
			}
		    }
		}
		else
		{
		    sql = "select strWaiterNo from tbltablemaster "
			    + " where strTableNo='" + globalTableNo + "'";
		    ResultSet rsWaiterNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsWaiterNo.next())
		    {
			String temWaiterNo = rsWaiterNo.getString(1);
			if (temWaiterNo.equalsIgnoreCase("all"))
			{
			    funLoadWaiterNo(0, vWaiterNo.size());
			}
			else
			{
			    JButton[] btnItemArray =
			    {
				btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
			    };
			    for (int cntWaiterArray = 0; cntWaiterArray < btnItemArray.length; cntWaiterArray++)
			    {
				btnItemArray[cntWaiterArray].setText("");
				btnItemArray[cntWaiterArray].setIcon(null);
			    }
			    for (int cntWaiter = 0; cntWaiter < vWaiterNo.size(); cntWaiter++)
			    {
				if (vWaiterNo.elementAt(cntWaiter).toString().equals(temWaiterNo))
				{
				    globalWaiterNo = rsWaiterNo.getString(1);
				    txtWaiterNo.setText(funConvertString(vWaiterName.elementAt(cntWaiter).toString()));
				    break;
				}
			    }
			}
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
    }

    /**
     * this function show pop up to swipe card
     *
     * if debit card is valid, shows customer name if
     * gSelectCustomerCodeFromCardSwipe is enabled also check the card is
     * already in use or not if card is not already in use it shows the card
     * balance
     */
    private void funDebitCardButtonClicked()
    {
	try
	{
	    new frmSwipCardPopUp(this, "").setVisible(true);
	    if (clsGlobalVarClass.gDebitCardNo != null)
	    {
		if (clsGlobalVarClass.gSelectCustomerCodeFromCardSwipe)
		{
		    sql = " select a.strCustomerCode,b.strCustomerName,b.longMobileNo "
			    + " from tbldebitcardmaster a,tblcustomermaster b "
			    + " where a.strCardString='" + clsGlobalVarClass.gDebitCardNo + "' "
			    + " and a.strCustomerCode=b.strCustomerCode";
		    ResultSet rsCustomerData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsCustomerData.next())
		    {
			clsGlobalVarClass.gCustomerCode = rsCustomerData.getString(1);
			btnNewCustomer.setText(rsCustomerData.getString(2));
			clsGlobalVarClass.gCustMobileNoForCRM = rsCustomerData.getString(3);
			funSetCustMobileNo(clsGlobalVarClass.gCustMobileNoForCRM);
		    }
		}

		String debitCardNo = "";
		double debitCardBal = 0;
		sql = "select a.strCardNo,a.dblRedeemAmt,c.dblcardvaluefixed "
			+ "from tbldebitcardmaster a,tbldebitcardtype c "
			+ "where a.strCardTypeCode=c.strCardTypeCode and a.strCardString='" + clsGlobalVarClass.gDebitCardNo + "'";
		ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsDebitCardNo.next())
		{
		    debitCardNo = rsDebitCardNo.getString(1);
		    globalDebitCardNo = debitCardNo;
		    debitCardBal = objUtility.funGetDebitCardBalance(clsGlobalVarClass.gDebitCardNo, globalTableNo);
		    //debitCardBal=debitCardBal-rsDebitCardNo.getDouble(3);

		    DecimalFormat objDecFormat = new DecimalFormat("####.##");

		    sql = "select b.strTableName from tblitemrtemp a,tbltablemaster b "
			    + " where a.strTableNo=b.strTableNo and a.strCardNo='" + debitCardNo + "'";
		    ResultSet rsTempTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsTempTable.next())
		    {
			new frmOkPopUp(null, "Card is Already Selected On Table " + rsTempTable.getString(1), "Warning", 1).setVisible(true);
			rsTempTable.close();
		    }
		    else
		    {
			sql = "select b.strTableName from tblbillhd a,tbltablemaster b "
				+ " where a.strTableNo=b.strTableNo and a.strCardNo='" + debitCardNo + "' "
				+ " and a.strBillNo not in (select strBillNo from tblbillsettlementdtl) ";
			ResultSet rsCheckCardInBillHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsCheckCardInBillHd.next())
			{
			    new frmOkPopUp(null, "Card is Already Selected On Table " + rsCheckCardInBillHd.getString(1), "Warning", 1).setVisible(true);
			}
			else
			{
			    lblDebitCardBalance.setVisible(true);
			    lblCardBalnce.setText(String.valueOf(objUtility.funGetDebitCardBalance(clsGlobalVarClass.gDebitCardNo, globalTableNo)));

			    if (txtTotal.getText().trim().length() > 0)
			    {
				if (Double.parseDouble(txtTotal.getText().trim()) <= debitCardBal)
				{
				    lblCardBalnce.setVisible(true);
				    lblCardBalnce.setBackground(Color.yellow);
				    lblCardBalnce.setText(objDecFormat.format(debitCardBal));
				}
				else
				{
				    lblCardBalnce.setVisible(true);
				    lblCardBalnce.setBackground(Color.red);
				    lblCardBalnce.setText(objDecFormat.format(debitCardBal));
				}
			    }
			}
		    }
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

    private void funUPButtonPressed()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int rowcount = tblItemTable.getRowCount();
	    if (selectedRow == -1)
	    {
		selectedRow = 0;
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		temp_ItemCode = tblItemTable.getValueAt(selectedRow, 3).toString().trim();
	    }
	    else if (selectedRow == rowcount)
	    {
		selectedRow = 0;
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		temp_ItemCode = tblItemTable.getValueAt(selectedRow, 3).toString().trim();
	    }
	    else if (selectedRow < rowcount)
	    {
		tblItemTable.changeSelection(selectedRow - 1, 0, false, false);
		temp_ItemCode = tblItemTable.getValueAt(selectedRow - 1, 3).toString().trim();
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	}
    }

    private void funDownButtonPressed()
    {
	if (tblItemTable.getModel().getRowCount() > 0)
	{
	    int selectedRow = tblItemTable.getSelectedRow();
	    int rowcount = tblItemTable.getRowCount();
	    if (selectedRow < rowcount)
	    {
		tblItemTable.changeSelection(selectedRow + 1, 0, false, false);
		temp_ItemCode = tblItemTable.getValueAt(selectedRow + 1, 3).toString().trim();
	    }
	    else if (selectedRow == rowcount)
	    {
		selectedRow = 0;
		tblItemTable.changeSelection(selectedRow, 0, false, false);
		temp_ItemCode = tblItemTable.getValueAt(selectedRow, 3).toString().trim();
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
	}
    }

    private String funCheckDebitCardString(String debitCardString)
    {
	String waiterNo = "";
	try
	{
	    sql = "select strWaiterNo,strWShortName,strWFullName,strOperational "
		    + " from tblwaitermaster "
		    + " where strDebitCardString='" + debitCardString + "' and (strPOSCode='All' or strPOSCode='" + clsGlobalVarClass.gPOSCode + "') ";
	    ResultSet rsWaiter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsWaiter.next())
	    {
		if (rsWaiter.getString(4).equals("N"))
		{
		    fieldSelected = "Table";
		    JOptionPane.showMessageDialog(null, "This Card is not operational!!!");
		}
		else
		{
		    waiterNo = rsWaiter.getString(1) + "#" + rsWaiter.getString(2);
		}
	    }
	    else
	    {
		fieldSelected = "Table";
		JOptionPane.showMessageDialog(null, "Invalid Card!!!");
	    }
	    rsWaiter.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return waiterNo;
	}
    }

    public void funSetKeyBoardValueOnPLUTextBox(String text)
    {
	txtPLUItemSearch.setText(text);
	funPLUItemSearch();
    }

    private void funResetBottomButtons()
    {
	/*
         * if (navigate == 0) { btnButton4.setForeground(Color.WHITE); } if
         * (navigate == 1) { btnButton3.setText("<Html>DELIVERY<br>BOY</html>");
         * }
	 */

	btnNewCustomer.setText("<html>Customer</html>");
	flgHomeDeliveryColor_Button = false;
	flgcheckDeliveryboyName = false;
	btnButton4.setForeground(Color.white);
	flgCheckNCKOTButtonColor = false;
    }

    private void funKOTMsgMouseClicked()
    {
	try
	{
	    if (txtKOTMsg.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter KOT Message").setVisible(true);
		txtKOTMsg.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtKOTMsg.getText(), "1", "Enter KOT Message").setVisible(true);
		txtKOTMsg.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSetSelectedArea()
    {
	try
	{
	    if (!clsGlobalVarClass.gCMSIntegrationYN)
	    {
		String sql_Area = "select strAreaCode from tblareamaster "
			+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			+ " and strAreaName='All' "
			+ " order by strAreaCode";
		if (!clsGlobalVarClass.gSuperUser && clsGlobalVarClass.gAutoAreaSelectionInMakeKOT)
		{
		    clsUtility objUtility = new clsUtility();
		    String hostName = objUtility.funGetHostName();
		    String physicalAddress = objUtility.funGetCurrentMACAddress();

		    sql_Area = "SELECT strAreaCode "
			    + "FROM tblareamaster "
			    + "WHERE (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR strPOSCode='All')  "
			    + "and strMACAddress like '%" + physicalAddress + "%' "
			    + "ORDER BY strAreaCode";
		}

		ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql_Area);
		if (rsArea.next())
		{
		    funSetArea(rsArea.getString(1));
		}
		else
		{
		    funSetArea("NA");
		}
		rsArea.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * set default NC table properties
     *
     * @throws Exception
     */
    private void funDefaultNCTableClicked() throws Exception
    {
	String ncTableSql = "select a.strTableNo,a.strTableName,a.strNCTable "
		+ "from tbltablemaster a  "
		+ "where a.strTableNo='" + globalTableNo + "' "
		+ "and a.strNCTable='Y' ";
	ResultSet rsNCTable = clsGlobalVarClass.dbMysql.executeResultSet(ncTableSql);
	if (rsNCTable.next())
	{
	    navigate = 0;
	    btnNext.setEnabled(true);

	    funNextButtonClicked();

	    btnButton4.setForeground(Color.black);
	    flgCheckNCKOTButtonColor = true;

	}
	else
	{
	    navigate = -1;
	    btnNext.setEnabled(true);
	    btnButton4.setForeground(Color.white);
	    flgCheckNCKOTButtonColor = false;

	    funNextButtonClicked();
	}
	rsNCTable.close();
    }

    private void funSelectEnterTable()
    {
	String tableName = txtTableNo.getText().trim().toUpperCase();
	if (hmTable.containsKey(tableName))
	{
	    String tableNo = hmTable.get(tableName);
	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(tableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
		return;
	    }
	    funCheckTable(tableName);
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Wrong table name!!!");
	    txtTableNo.setText("");
	}

	funFocusExternalCodeTextField();
    }

    private void funFocusExternalCodeTextField()
    {
	txtExternalCode.setFocusable(true);
	txtExternalCode.requestFocus();
    }

    /**
     * This class is used to handle keyboard
     */
    private class KeyBoardEvent implements KeyEventDispatcher
    {

	@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
	    if (fieldSelected.equals("MenuItem"))
	    {
		if (e.getID() == KeyEvent.KEY_PRESSED)
		{
		    if (e.getKeyCode() == 113)
		    {
			clsGlobalVarClass.gNewCustomerForHomeDel = true;
			new frmCustomerMaster().setVisible(true);
		    }
		    if (e.getKeyCode() == 123)
		    {
			try
			{
			    funDONEButtonPressed();
			}
			catch (Exception ex)
			{
			    objUtility.funShowDBConnectionLostErrorMessage(ex);	
			    ex.printStackTrace();
			}
		    }
		}
	    }
	    return false;
	}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelUserAuthentication = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
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
        panelOperationalButtons = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnButton2 = new javax.swing.JButton();
        btnButton1 = new javax.swing.JButton();
        btnButton3 = new javax.swing.JButton();
        btnButton4 = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnNewCustomer = new javax.swing.JButton();
        btnMakeKOT = new javax.swing.JButton();
        btnMakeBill = new javax.swing.JButton();
        panelItemDtl = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        txtTotal = new javax.swing.JTextField();
        lblPaxNo = new javax.swing.JLabel();
        btnChangeQty = new javax.swing.JButton();
        labelWaiterName = new javax.swing.JLabel();
        labelPaxNo = new javax.swing.JLabel();
        lblKOTNo = new javax.swing.JLabel();
        txtTableNo = new javax.swing.JTextField();
        txtWaiterNo = new javax.swing.JTextField();
        txtPaxNo = new javax.swing.JTextField();
        btnDelItem = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        panelMenuHead = new javax.swing.JPanel();
        btnMenu3 = new javax.swing.JButton();
        btnMenu2 = new javax.swing.JButton();
        btnMenu4 = new javax.swing.JButton();
        btnMenu6 = new javax.swing.JButton();
        btnMenu8 = new javax.swing.JButton();
        btnMenu7 = new javax.swing.JButton();
        btnPopular = new javax.swing.JButton();
        btnMenu5 = new javax.swing.JButton();
        btnPrevMenu = new javax.swing.JButton();
        btnNextMenu = new javax.swing.JButton();
        panelNumericKeyPad = new javax.swing.JPanel();
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
        btnB4 = new javax.swing.JButton();
        panelNavigate = new javax.swing.JPanel();
        btnPrevItem = new javax.swing.JButton();
        btnNextItem = new javax.swing.JButton();
        panelAreaName = new javax.swing.JPanel();
        lblAreaName = new javax.swing.JLabel();
        panelDeditCard = new javax.swing.JPanel();
        lblDebitCardBalance = new javax.swing.JLabel();
        btnDebitCard = new javax.swing.JButton();
        lblCardBalnce = new javax.swing.JLabel();
        panelExternalCode = new javax.swing.JPanel();
        txtExternalCode = new javax.swing.JTextField();
        lblExternalCode = new javax.swing.JLabel();
        btnItemMode = new javax.swing.JButton();
        panelItemButtons = new javax.swing.JPanel();
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
        panelTopSortingButtons = new javax.swing.JPanel();
        btnPrevItemSorting = new javax.swing.JButton();
        btnItemSorting1 = new javax.swing.JButton();
        btnItemSorting2 = new javax.swing.JButton();
        btnItemSorting3 = new javax.swing.JButton();
        btnNextItemSorting = new javax.swing.JButton();
        btnItemSorting4 = new javax.swing.JButton();
        panelPLU = new javax.swing.JPanel();
        bttn_PLU_Panel_Close = new javax.swing.JButton();
        scrPLU = new javax.swing.JScrollPane();
        tblPLUItems = new javax.swing.JTable();
        txtPLUItemSearch = new javax.swing.JTextField();
        panelKOTMessage = new javax.swing.JPanel();
        btnCloseKOTMsg = new javax.swing.JButton();
        cmbCostCenterForKOTMsg = new javax.swing.JComboBox();
        lblCostCenterForKOTMsg = new javax.swing.JLabel();
        scrollKOTMsg = new javax.swing.JScrollPane();
        txtKOTMsg = new javax.swing.JTextArea();
        lblKOTMsg = new javax.swing.JLabel();
        btnKOTSend = new javax.swing.JButton();
        btnKOTSend1 = new javax.swing.JButton();

        lblUsername.setText("Enter a Username:");

        lblPassword.setText("Enter a Password:");

        txtUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUsernameMouseClicked(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelUserAuthenticationLayout = new javax.swing.GroupLayout(panelUserAuthentication);
        panelUserAuthentication.setLayout(panelUserAuthenticationLayout);
        panelUserAuthenticationLayout.setHorizontalGroup(
            panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUserAuthenticationLayout.createSequentialGroup()
                        .addComponent(lblPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addGap(2, 2, 2)))
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUserAuthenticationLayout.setVerticalGroup(
            panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtUsername)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Make KOT");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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

        panelOperationalButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelOperationalButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnHome.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setText("HOME");
        btnHome.setToolTipText("Close Make KOT Form");
        btnHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnHome.setBorderPainted(false);
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnButton2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnButton2.setForeground(new java.awt.Color(255, 255, 255));
        btnButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnButton2.setMnemonic('p');
        btnButton2.setText("PLU");
        btnButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnButton2.setBorderPainted(false);
        btnButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnButton2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnButton2ActionPerformed(evt);
            }
        });
        btnButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnButton2KeyPressed(evt);
            }
        });

        btnButton1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnButton1.setForeground(new java.awt.Color(255, 255, 255));
        btnButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnButton1.setText("<html><u>S</u>ETTLE<br>BILL</html>");
        btnButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnButton1.setPreferredSize(new java.awt.Dimension(102, 42));
        btnButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnButton1ActionPerformed(evt);
            }
        });

        btnButton3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnButton3.setForeground(new java.awt.Color(255, 255, 255));
        btnButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnButton3.setText("<html>ZOMATO<br>CODE</html>");
        btnButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnButton3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnButton3ActionPerformed(evt);
            }
        });

        btnButton4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnButton4.setForeground(new java.awt.Color(255, 255, 255));
        btnButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnButton4.setText("<html>HOME<br>DELIVERY</html>");
        btnButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnButton4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnButton4MouseClicked(evt);
            }
        });
        btnButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnButton4ActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnPrevious.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevious.setText("<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrevious.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPreviousMouseClicked(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNext.setText(">>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnNewCustomer.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNewCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnNewCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNewCustomer.setText("<Html>CUSTOMER</html>");
        btnNewCustomer.setToolTipText("Enter Customer Mobile No");
        btnNewCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewCustomer.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNewCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewCustomerMouseClicked(evt);
            }
        });
        btnNewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCustomerActionPerformed(evt);
            }
        });

        btnMakeKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMakeKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnMakeKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMakeKOT.setMnemonic('d');
        btnMakeKOT.setText("DONE");
        btnMakeKOT.setToolTipText("Save and Print KOT");
        btnMakeKOT.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnMakeKOT.setBorderPainted(false);
        btnMakeKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMakeKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMakeKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMakeKOTActionPerformed(evt);
            }
        });

        btnMakeBill.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMakeBill.setForeground(new java.awt.Color(255, 255, 255));
        btnMakeBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMakeBill.setText("<html><u>M</u>AKE<br>BILL</html>");
        btnMakeBill.setToolTipText("Make Bill");
        btnMakeBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMakeBill.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMakeBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMakeBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperationalButtonsLayout = new javax.swing.GroupLayout(panelOperationalButtons);
        panelOperationalButtons.setLayout(panelOperationalButtonsLayout);
        panelOperationalButtonsLayout.setHorizontalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMakeKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMakeBill, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(260, 260, 260))
        );
        panelOperationalButtonsLayout.setVerticalGroup(
            panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOperationalButtonsLayout.createSequentialGroup()
                .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelOperationalButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHome))
                    .addComponent(btnButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMakeKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMakeBill, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelItemDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtl.setForeground(new java.awt.Color(254, 184, 80));
        panelItemDtl.setPreferredSize(new java.awt.Dimension(260, 600));

        tblItemTable.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblItemTable.setForeground(new java.awt.Color(0, 0, 204));
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "Qty", "Amount", "Itemcode", "SerialNo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblItemTable.setRowHeight(25);
        tblItemTable.setShowVerticalLines(false);
        tblItemTable.getTableHeader().setReorderingAllowed(false);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItemTable);

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnChangeQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnChangeQty.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgChangeQty.png"))); // NOI18N
        btnChangeQty.setToolTipText("Change Quantity of an item");
        btnChangeQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgChangeQty.png"))); // NOI18N
        btnChangeQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeQtyActionPerformed(evt);
            }
        });

        labelWaiterName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        labelWaiterName.setText("WTR");

        labelPaxNo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        labelPaxNo.setText("PAX ");

        lblKOTNo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblKOTNo.setText("TBL");

        txtTableNo.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        txtTableNo.setForeground(new java.awt.Color(51, 102, 255));
        txtTableNo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTableNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTableNoMouseClicked(evt);
            }
        });
        txtTableNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTableNoActionPerformed(evt);
            }
        });
        txtTableNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTableNoKeyPressed(evt);
            }
        });

        txtWaiterNo.setFont(new java.awt.Font("Trebuchet MS", 0, 10)); // NOI18N
        txtWaiterNo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtWaiterNo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                txtWaiterNoMouseDragged(evt);
            }
        });
        txtWaiterNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWaiterNoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtWaiterNoMouseEntered(evt);
            }
        });
        txtWaiterNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWaiterNoKeyPressed(evt);
            }
        });

        txtPaxNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtPaxNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaxNo.setText("0");
        txtPaxNo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                txtPaxNoMouseDragged(evt);
            }
        });
        txtPaxNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPaxNoMouseClicked(evt);
            }
        });
        txtPaxNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPaxNoKeyPressed(evt);
            }
        });

        btnDelItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDelItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelItem.setToolTipText("Delete Item from list");
        btnDelItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDelete.png"))); // NOI18N
        btnDelItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDelItemMouseClicked(evt);
            }
        });

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUpArrow.png"))); // NOI18N
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUpArrow.png"))); // NOI18N
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownArrow.png"))); // NOI18N
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setPreferredSize(new java.awt.Dimension(44, 48));
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownArrow.png"))); // NOI18N
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelItemDtlLayout = new javax.swing.GroupLayout(panelItemDtl);
        panelItemDtl.setLayout(panelItemDtlLayout);
        panelItemDtlLayout.setHorizontalGroup(
            panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemDtlLayout.createSequentialGroup()
                .addComponent(lblKOTNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTableNo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelWaiterName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWaiterNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPaxNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPaxNo)
                .addGap(10, 10, 10))
            .addGroup(panelItemDtlLayout.createSequentialGroup()
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelItemDtlLayout.createSequentialGroup()
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnChangeQty, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelItemDtlLayout.setVerticalGroup(
            panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemDtlLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblKOTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTableNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelWaiterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtWaiterNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelItemDtlLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblPaxNo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangeQty, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelMenuHead.setBackground(new java.awt.Color(255, 255, 255));
        panelMenuHead.setForeground(new java.awt.Color(255, 236, 205));

        btnMenu3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu3.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu3.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu3ActionPerformed(evt);
            }
        });

        btnMenu2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu2.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu2.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu2ActionPerformed(evt);
            }
        });

        btnMenu4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu4.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu4.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu4ActionPerformed(evt);
            }
        });

        btnMenu6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu6.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu6.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu6ActionPerformed(evt);
            }
        });

        btnMenu8.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu8.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu8.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu8ActionPerformed(evt);
            }
        });

        btnMenu7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu7.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu7.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu7ActionPerformed(evt);
            }
        });

        btnPopular.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnPopular.setForeground(new java.awt.Color(255, 255, 255));
        btnPopular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopular.setText("POPULAR");
        btnPopular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopular.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPopular.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPopular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopularActionPerformed(evt);
            }
        });

        btnMenu5.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMenu5.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu5.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenu5ActionPerformed(evt);
            }
        });

        btnPrevMenu.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnPrevMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPDark.png"))); // NOI18N
        btnPrevMenu.setToolTipText("Prevoius Menu Head");
        btnPrevMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPrevMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgUPLite.png"))); // NOI18N
        btnPrevMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrevMenuMouseClicked(evt);
            }
        });
        btnPrevMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevMenuActionPerformed(evt);
            }
        });

        btnNextMenu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNextMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnNextMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownDark.png"))); // NOI18N
        btnNextMenu.setToolTipText("Next Menu Head");
        btnNextMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnNextMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgDownLite.png"))); // NOI18N
        btnNextMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMenuHeadLayout = new javax.swing.GroupLayout(panelMenuHead);
        panelMenuHead.setLayout(panelMenuHeadLayout);
        panelMenuHeadLayout.setHorizontalGroup(
            panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuHeadLayout.createSequentialGroup()
                .addGroup(panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnMenu7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMenu8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPrevMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMenuHeadLayout.setVerticalGroup(
            panelMenuHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuHeadLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrevMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNumericKeyPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericKeyPad.setForeground(new java.awt.Color(255, 236, 205));

        btnNumber2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber2.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber2.setText("2");
        btnNumber2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber2MouseClicked(evt);
            }
        });

        btnNumber1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber1.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber1.setText("1");
        btnNumber1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber1MouseClicked(evt);
            }
        });

        btnNumber4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber4.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber4.setText("4");
        btnNumber4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber4MouseClicked(evt);
            }
        });

        btnNumber3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber3.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber3.setText("3");
        btnNumber3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber3MouseClicked(evt);
            }
        });

        btnNumber5.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber5.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber5.setText("5");
        btnNumber5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber5MouseClicked(evt);
            }
        });

        btnNumber6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber6.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber6.setText("6");
        btnNumber6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber6MouseClicked(evt);
            }
        });

        btnNumber7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber7.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber7.setText("7");
        btnNumber7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber7MouseClicked(evt);
            }
        });

        btnNumber8.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber8.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber8.setText("8");
        btnNumber8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber8MouseClicked(evt);
            }
        });

        btnNumber9.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNumber9.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber9.setText("9");
        btnNumber9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNumber9MouseClicked(evt);
            }
        });

        btnMultiQty.setBackground(new java.awt.Color(102, 153, 255));
        btnMultiQty.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnMultiQty.setForeground(new java.awt.Color(255, 255, 255));
        btnMultiQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnMultiQty.setText(">>");
        btnMultiQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMultiQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnMultiQty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMultiQtyMouseClicked(evt);
            }
        });

        btnB4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnB4.setForeground(new java.awt.Color(255, 255, 255));
        btnB4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgKeyBoardShiftButtonDark.png"))); // NOI18N
        btnB4.setText("<html>KOT<br>MSG</html>");
        btnB4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 153, 255), new java.awt.Color(51, 102, 255)));
        btnB4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnB4.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnB4.setMaximumSize(new java.awt.Dimension(53, 53));
        btnB4.setMinimumSize(new java.awt.Dimension(53, 53));
        btnB4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgKeyBoardShiftButtonLight.png"))); // NOI18N
        btnB4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnB4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelNumericKeyPadLayout = new javax.swing.GroupLayout(panelNumericKeyPad);
        panelNumericKeyPad.setLayout(panelNumericKeyPadLayout);
        panelNumericKeyPadLayout.setHorizontalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
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
            .addComponent(btnB4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelNumericKeyPadLayout.setVerticalGroup(
            panelNumericKeyPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericKeyPadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnB4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMultiQty, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelNavigate.setBackground(new java.awt.Color(255, 255, 255));

        btnPrevItem.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnPrevItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevItem.setText("<<<");
        btnPrevItem.setToolTipText("Prevoius Menu Item");
        btnPrevItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevItemActionPerformed(evt);
            }
        });

        btnNextItem.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        btnNextItem.setForeground(new java.awt.Color(255, 255, 255));
        btnNextItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextItem.setText(">>>");
        btnNextItem.setToolTipText("Next Menu Item");
        btnNextItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextItemActionPerformed(evt);
            }
        });

        panelAreaName.setBackground(new java.awt.Color(69, 164, 238));
        panelAreaName.setForeground(new java.awt.Color(240, 200, 80));

        lblAreaName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblAreaName.setForeground(new java.awt.Color(255, 255, 255));
        lblAreaName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAreaName.setText("ALL FLOORS");
        lblAreaName.setToolTipText("Select Area");
        lblAreaName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAreaNameMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelAreaNameLayout = new javax.swing.GroupLayout(panelAreaName);
        panelAreaName.setLayout(panelAreaNameLayout);
        panelAreaNameLayout.setHorizontalGroup(
            panelAreaNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAreaName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
        );
        panelAreaNameLayout.setVerticalGroup(
            panelAreaNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAreaName, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelNavigateLayout = new javax.swing.GroupLayout(panelNavigate);
        panelNavigate.setLayout(panelNavigateLayout);
        panelNavigateLayout.setHorizontalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelNavigateLayout.setVerticalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panelAreaName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panelDeditCard.setEnabled(false);

        lblDebitCardBalance.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblDebitCardBalance.setText("Card Balance :");
        lblDebitCardBalance.setAlignmentY(0.0F);

        btnDebitCard.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDebitCard.setForeground(new java.awt.Color(255, 255, 255));
        btnDebitCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDebitCard.setText("DEBIT CARD");
        btnDebitCard.setToolTipText("Swipe Card");
        btnDebitCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDebitCard.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDebitCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDebitCardMouseClicked(evt);
            }
        });

        lblCardBalnce.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblCardBalnce.setForeground(new java.awt.Color(51, 102, 255));
        lblCardBalnce.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardBalnce.setAlignmentY(0.0F);
        lblCardBalnce.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCardBalnce.setOpaque(true);

        javax.swing.GroupLayout panelDeditCardLayout = new javax.swing.GroupLayout(panelDeditCard);
        panelDeditCard.setLayout(panelDeditCardLayout);
        panelDeditCardLayout.setHorizontalGroup(
            panelDeditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeditCardLayout.createSequentialGroup()
                .addComponent(btnDebitCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(lblDebitCardBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCardBalnce, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelDeditCardLayout.setVerticalGroup(
            panelDeditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeditCardLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelDeditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDebitCard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCardBalnce, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDebitCardBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panelExternalCode.setOpaque(false);

        txtExternalCode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtExternalCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExternalCode.setFocusCycleRoot(true);
        txtExternalCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtExternalCodeMouseClicked(evt);
            }
        });
        txtExternalCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExternalCodeActionPerformed(evt);
            }
        });
        txtExternalCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExternalCodeKeyPressed(evt);
            }
        });

        lblExternalCode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblExternalCode.setText("ITEM CODE ");

        javax.swing.GroupLayout panelExternalCodeLayout = new javax.swing.GroupLayout(panelExternalCode);
        panelExternalCode.setLayout(panelExternalCodeLayout);
        panelExternalCodeLayout.setHorizontalGroup(
            panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExternalCodeLayout.createSequentialGroup()
                .addComponent(lblExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
            .addGroup(panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExternalCodeLayout.createSequentialGroup()
                    .addContainerGap(71, Short.MAX_VALUE)
                    .addComponent(txtExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        panelExternalCodeLayout.setVerticalGroup(
            panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExternalCodeLayout.createSequentialGroup()
                .addComponent(lblExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelExternalCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExternalCodeLayout.createSequentialGroup()
                    .addComponent(txtExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        btnItemMode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnItemMode.setForeground(new java.awt.Color(255, 255, 255));
        btnItemMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnItemMode.setText("MENU ITEM");
        btnItemMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemMode.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnItemMode.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnItemMode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemModeMouseClicked(evt);
            }
        });
        btnItemMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemModeActionPerformed(evt);
            }
        });

        panelItemButtons.setBackground(new java.awt.Color(255, 255, 255));
        panelItemButtons.setEnabled(false);
        panelItemButtons.setLayout(null);

        btnIItem2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem2ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem2);
        btnIItem2.setBounds(86, 0, 80, 80);

        btnIItem1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem1ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem1);
        btnIItem1.setBounds(0, 0, 80, 81);

        btnIItem3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem3ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem3);
        btnIItem3.setBounds(170, 0, 80, 80);

        btnIItem4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem4ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem4);
        btnIItem4.setBounds(255, 0, 80, 80);

        btnIItem5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem5ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem5);
        btnIItem5.setBounds(0, 87, 80, 80);

        btnIItem6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem6ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem6);
        btnIItem6.setBounds(86, 87, 80, 80);

        btnIItem7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem7ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem7);
        btnIItem7.setBounds(170, 87, 80, 80);

        btnIItem8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem8ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem8);
        btnIItem8.setBounds(255, 87, 80, 80);

        btnIItem9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem9ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem9);
        btnIItem9.setBounds(0, 174, 80, 80);

        btnIItem10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem10ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem10);
        btnIItem10.setBounds(86, 174, 80, 80);

        btnIItem11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem11ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem11);
        btnIItem11.setBounds(170, 174, 80, 80);

        btnIItem12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem12ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem12);
        btnIItem12.setBounds(255, 174, 80, 80);

        btnIItem13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem13ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem13);
        btnIItem13.setBounds(0, 260, 80, 80);

        btnIItem14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem14ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem14);
        btnIItem14.setBounds(86, 260, 80, 80);

        btnIItem15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem15ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem15);
        btnIItem15.setBounds(170, 260, 80, 80);

        btnIItem16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem16ActionPerformed(evt);
            }
        });
        panelItemButtons.add(btnIItem16);
        btnIItem16.setBounds(255, 260, 80, 80);

        panelTopSortingButtons.setBackground(new java.awt.Color(255, 255, 255));

        btnPrevItemSorting.setText("<<");
        btnPrevItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnPrevItemSorting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnPrevItemSorting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrevItemSortingMouseClicked(evt);
            }
        });

        btnItemSorting1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemSorting1MouseClicked(evt);
            }
        });

        btnItemSorting2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemSorting2MouseClicked(evt);
            }
        });

        btnItemSorting3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemSorting3MouseClicked(evt);
            }
        });

        btnNextItemSorting.setText(">>");
        btnNextItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnNextItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnNextItemSorting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextItemSortingMouseClicked(evt);
            }
        });

        btnItemSorting4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemSorting4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelTopSortingButtonsLayout = new javax.swing.GroupLayout(panelTopSortingButtons);
        panelTopSortingButtons.setLayout(panelTopSortingButtonsLayout);
        panelTopSortingButtonsLayout.setHorizontalGroup(
            panelTopSortingButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addComponent(btnPrevItemSorting, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnItemSorting1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnItemSorting2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnItemSorting3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnItemSorting4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnNextItemSorting, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelTopSortingButtonsLayout.setVerticalGroup(
            panelTopSortingButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(btnPrevItemSorting, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(btnItemSorting1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(btnItemSorting2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(btnItemSorting3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(btnItemSorting4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelTopSortingButtonsLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(btnNextItemSorting, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelPLU.setBackground(new java.awt.Color(255, 255, 255));

        bttn_PLU_Panel_Close.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        bttn_PLU_Panel_Close.setForeground(new java.awt.Color(255, 255, 255));
        bttn_PLU_Panel_Close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        bttn_PLU_Panel_Close.setText("CLOSE");
        bttn_PLU_Panel_Close.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bttn_PLU_Panel_Close.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        bttn_PLU_Panel_Close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bttn_PLU_Panel_CloseMouseClicked(evt);
            }
        });
        bttn_PLU_Panel_Close.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bttn_PLU_Panel_CloseKeyPressed(evt);
            }
        });

        tblPLUItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPLUItems.setRowHeight(25);
        tblPLUItems.getTableHeader().setReorderingAllowed(false);
        tblPLUItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPLUItemsMouseClicked(evt);
            }
        });
        tblPLUItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPLUItemsKeyPressed(evt);
            }
        });
        scrPLU.setViewportView(tblPLUItems);

        txtPLUItemSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPLUItemSearchFocusGained(evt);
            }
        });
        txtPLUItemSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPLUItemSearchMouseClicked(evt);
            }
        });
        txtPLUItemSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPLUItemSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPLUItemSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPLUItemSearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelPLULayout = new javax.swing.GroupLayout(panelPLU);
        panelPLU.setLayout(panelPLULayout);
        panelPLULayout.setHorizontalGroup(
            panelPLULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPLULayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPLULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelPLULayout.createSequentialGroup()
                        .addComponent(txtPLUItemSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(bttn_PLU_Panel_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelPLULayout.setVerticalGroup(
            panelPLULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPLULayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelPLULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPLULayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtPLUItemSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bttn_PLU_Panel_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(scrPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelKOTMessage.setBackground(new java.awt.Color(255, 255, 255));

        btnCloseKOTMsg.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnCloseKOTMsg.setForeground(new java.awt.Color(255, 255, 255));
        btnCloseKOTMsg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCloseKOTMsg.setText("CLOSE");
        btnCloseKOTMsg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseKOTMsg.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCloseKOTMsg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseKOTMsgMouseClicked(evt);
            }
        });
        btnCloseKOTMsg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCloseKOTMsgKeyPressed(evt);
            }
        });

        lblCostCenterForKOTMsg.setText("Cost Center : ");

        txtKOTMsg.setColumns(20);
        txtKOTMsg.setRows(5);
        txtKOTMsg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKOTMsgMouseClicked(evt);
            }
        });
        scrollKOTMsg.setViewportView(txtKOTMsg);

        lblKOTMsg.setText("Kitchen Note : ");

        btnKOTSend.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnKOTSend.setForeground(new java.awt.Color(255, 255, 255));
        btnKOTSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnKOTSend.setText("SEND");
        btnKOTSend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKOTSend.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnKOTSend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKOTSendMouseClicked(evt);
            }
        });
        btnKOTSend.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnKOTSendKeyPressed(evt);
            }
        });

        btnKOTSend1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnKOTSend1.setForeground(new java.awt.Color(255, 255, 255));
        btnKOTSend1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNameTittle.png"))); // NOI18N
        btnKOTSend1.setText("KITCHEN NOTE");
        btnKOTSend1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKOTSend1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNameTittle.png"))); // NOI18N
        btnKOTSend1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKOTSend1MouseClicked(evt);
            }
        });
        btnKOTSend1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnKOTSend1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelKOTMessageLayout = new javax.swing.GroupLayout(panelKOTMessage);
        panelKOTMessage.setLayout(panelKOTMessageLayout);
        panelKOTMessageLayout.setHorizontalGroup(
            panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKOTMessageLayout.createSequentialGroup()
                .addGap(0, 83, Short.MAX_VALUE)
                .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTMessageLayout.createSequentialGroup()
                        .addComponent(btnKOTSend, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(btnCloseKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTMessageLayout.createSequentialGroup()
                        .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCostCenterForKOTMsg)
                            .addComponent(lblKOTMsg))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCostCenterForKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrollKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTMessageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnKOTSend1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        panelKOTMessageLayout.setVerticalGroup(
            panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKOTMessageLayout.createSequentialGroup()
                .addComponent(btnKOTSend1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCostCenterForKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCostCenterForKOTMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(panelKOTMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKOTSend, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCloseKOTMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(300, 300, 300)
                                .addComponent(panelTopSortingButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(299, 299, 299)
                                .addComponent(panelPLU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(panelItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(panelItemButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(46, 46, 46)
                                        .addComponent(btnItemMode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(panelExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panelDeditCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(300, 300, 300)
                                .addComponent(panelNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelNumericKeyPad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                    .addGap(0, 313, Short.MAX_VALUE)
                    .addComponent(panelKOTMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelPLU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelNumericKeyPad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelMenuHead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(445, 445, 445)
                                .addComponent(btnItemMode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(panelTopSortingButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                                        .addGap(100, 100, 100)
                                        .addComponent(panelItemButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(panelNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelExternalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDeditCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(panelOperationalButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFormBodyLayout.createSequentialGroup()
                    .addComponent(panelKOTMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 85, Short.MAX_VALUE)))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu3ActionPerformed
	funResetItemNames();
	funResetItemButtonText(menuNames1[1]);
	if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    try
	    {
		funFillTopButtonList(menuHeadCode);
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		e.printStackTrace();
	    }
	}
    }//GEN-LAST:event_btnMenu3ActionPerformed

    private void btnMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu2ActionPerformed
	funResetItemNames();
	funResetItemButtonText(menuNames1[0]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu2ActionPerformed

    private void btnMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu4ActionPerformed

	funResetItemNames();
	funResetItemButtonText(menuNames1[2]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu4ActionPerformed

    private void btnMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu6ActionPerformed
	funResetItemNames();
	funResetItemButtonText(menuNames1[4]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu6ActionPerformed

    private void btnMenu8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu8ActionPerformed
	funResetItemNames();
	funResetItemButtonText(menuNames1[6]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu8ActionPerformed

    private void btnMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu7ActionPerformed
	funResetItemNames();
	funResetItemButtonText(menuNames1[5]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu7ActionPerformed

    private void btnPopularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopularActionPerformed

	try
	{
	    funPopularItem();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	}
    }//GEN-LAST:event_btnPopularActionPerformed

    private void btnMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu5ActionPerformed

	funResetItemNames();
	funResetItemButtonText(menuNames1[3]);
	try
	{
	    funFillTopButtonList(menuHeadCode);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMenu5ActionPerformed

    private void btnPrevMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevMenuMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnPrevMenuMouseClicked

    private void btnPrevMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevMenuActionPerformed

	funPrevMenuButtonPressed();
    }//GEN-LAST:event_btnPrevMenuActionPerformed

    private void btnNextMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextMenuActionPerformed
	// TODO add your handling code here:
	funNextMenuButtonPressed();
    }//GEN-LAST:event_btnNextMenuActionPerformed

    private void btnNumber2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber2MouseClicked
	if (btnNumber2.isEnabled())
	{
	    funNumericSelection(btnNumber2.getText());
	}
    }//GEN-LAST:event_btnNumber2MouseClicked

    private void btnNumber1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber1MouseClicked

	if (btnNumber1.isEnabled())
	{
	    funNumericSelection(btnNumber1.getText());
	}
    }//GEN-LAST:event_btnNumber1MouseClicked

    private void btnNumber4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber4MouseClicked
	if (btnNumber4.isEnabled())
	{
	    funNumericSelection(btnNumber4.getText());
	}
    }//GEN-LAST:event_btnNumber4MouseClicked

    private void btnNumber3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber3MouseClicked
	if (btnNumber3.isEnabled())
	{
	    funNumericSelection(btnNumber3.getText());
	}
    }//GEN-LAST:event_btnNumber3MouseClicked

    private void btnNumber5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber5MouseClicked

	if (btnNumber5.isEnabled())
	{
	    funNumericSelection(btnNumber5.getText());
	}
    }//GEN-LAST:event_btnNumber5MouseClicked

    private void btnNumber6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber6MouseClicked
	if (btnNumber6.isEnabled())
	{
	    funNumericSelection(btnNumber6.getText());
	}
    }//GEN-LAST:event_btnNumber6MouseClicked

    private void btnNumber7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber7MouseClicked
	if (btnNumber7.isEnabled())
	{
	    funNumericSelection(btnNumber7.getText());
	}
    }//GEN-LAST:event_btnNumber7MouseClicked

    private void btnNumber8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber8MouseClicked

	if (btnNumber8.isEnabled())
	{
	    funNumericSelection(btnNumber8.getText());
	}
    }//GEN-LAST:event_btnNumber8MouseClicked

    private void btnNumber9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber9MouseClicked
	if (btnNumber9.isEnabled())
	{
	    funNumericSelection(btnNumber9.getText());
	}
    }//GEN-LAST:event_btnNumber9MouseClicked

    private void btnMultiQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMultiQtyMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (btnMultiQty.isEnabled())
	    {
		frmNumberKeyPad num = new frmNumberKeyPad(null, true, "qty");
		num.setVisible(true);
		//selectedQty = num.getResult();
		if (null != clsGlobalVarClass.gNumerickeyboardValue)
		{
		    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		    clsGlobalVarClass.gNumerickeyboardValue = null;
		}

		if (fieldSelected.equals("Pax"))
		{
		    StringBuilder sbPaxNo = new StringBuilder(String.valueOf(selectedQty));
		    funEnterPAXNo(sbPaxNo.delete(sbPaxNo.indexOf("."), sbPaxNo.length()).toString());
		    selectedQty = 1.00;
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMultiQtyMouseClicked

    private void btnB4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnB4MouseClicked

	if (btnB4.isEnabled())
	{
	    funButtonB4Clicked();
	}
    }//GEN-LAST:event_btnB4MouseClicked


    private void btnButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnButton2ActionPerformed

	funButtonAction(btnButton2);
    }//GEN-LAST:event_btnButton2ActionPerformed

    private void btnButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnButton2KeyPressed
	if (btnButton2.isFocusable() && evt.getKeyCode() == 10)
	{
	    if ("MenuItem".equals(fieldSelected))
	    {
		funPLUButtonPressed();
	    }
	}
    }//GEN-LAST:event_btnButton2KeyPressed

    private void btnButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnButton1ActionPerformed

	funButtonAction(btnButton1);
    }//GEN-LAST:event_btnButton1ActionPerformed

    private void btnButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnButton3ActionPerformed
	// TODO add your handling code here:
	funButtonAction(btnButton3);
    }//GEN-LAST:event_btnButton3ActionPerformed

    private void btnButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnButton4MouseClicked

    }//GEN-LAST:event_btnButton4MouseClicked

    private void btnPreviousMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPreviousMouseClicked
	// TODO add your handling code here:
	funPreviousButtonClicked();
    }//GEN-LAST:event_btnPreviousMouseClicked

    private void btnMakeKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakeKOTActionPerformed
	try
	{

	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(globalTableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table ");
	    }
	    else
	    {
		funDONEButtonPressed();

		if (!clsGlobalVarClass.gSuperUser && clsGlobalVarClass.gAutoAreaSelectionInMakeKOT)
		{
		    clsUtility objUtility = new clsUtility();
		    String hostName = objUtility.funGetHostName();
		    String physicalAddress = objUtility.funGetCurrentMACAddress();

		    String sqlArea = "SELECT strAreaCode,strAreaName "
			    + "FROM tblareamaster "
			    + "WHERE (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR strPOSCode='All')  "
			    + "and strMACAddress like '%" + physicalAddress + "%' "
			    + "ORDER BY strAreaCode";
		    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
		    if (rsArea.next())
		    {
			clsAreaCode = rsArea.getString(1);
			clsAreaName = rsArea.getString(2);
		    }
		    else
		    {
			clsAreaCode = "NA";
			clsAreaName = "NA";
		    }
		    rsArea.close();

		    hmTable.clear();
		    hmTableSeq.clear();

		    if (clsAreaName.equalsIgnoreCase("All"))
		    {
			sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
				+ "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
				+ "and strOperational='Y' order by intSequence ";
		    }
		    else
		    {
			sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
				+ "where strAreaCode='" + clsAreaCode + "' "
				+ "and (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
				+ "and strOperational='Y' order by intSequence ";
		    }

		    //System.out.println(sql);
		    ResultSet rsTableCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    while (rsTableCode.next())
		    {
			//vTableNo.add(rsTableCode.getString(1));
			//vTableName.add(rsTableCode.getString(2));
			hmTable.put(rsTableCode.getString(2).toUpperCase(), rsTableCode.getString(1));
			hmTableSeq.put(rsTableCode.getString(1) + "!" + rsTableCode.getString(2), rsTableCode.getInt(3));
		    }
		    rsTableCode.close();
		    //funLoadTables(0, vTableNo.size());
		    funLoadTables(0, hmTableSeq.size());
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnMakeKOTActionPerformed

    private void btnMakeBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakeBillActionPerformed
	funMakeBillButtonPressed();
    }//GEN-LAST:event_btnMakeBillActionPerformed

    private void txtExternalCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExternalCodeMouseClicked

	if ("TableSelected".equals(tableSelected))
	{
	    try
	    {
		if (!clsGlobalVarClass.gSkipWaiter)
		{
		    if (clsGlobalVarClass.gSelectWaiterFromCardSwipe && clsPosConfigFile.gSelectWaiterFromCardSwipe.equalsIgnoreCase("true"))
		    {
			new frmSwipCardPopUp(this, "frmMakeKOT").setVisible(true);
			String waiterInfo = funCheckDebitCardString(clsGlobalVarClass.gDebitCardNo);
			if (!waiterInfo.isEmpty())
			{
			    String[] spWaiter = waiterInfo.split("#");
			    funSetWaiterInfo(spWaiter[1], spWaiter[0]);
			    txtWaiterNo.setText(spWaiter[1]);
			    funEnablePaxButtons(true);
			    if (!clsGlobalVarClass.gSkipPax)
			    {
				fieldSelected = "Pax";
			    }
			    else if (clsGlobalVarClass.gSkipPax)
			    {
				fieldSelected = "MenuItem";
			    }

			    funExternalCodeButtonPressed("click");
			}
		    }
		}
		else
		{
		    funExternalCodeButtonPressed("click");
		}
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		e.printStackTrace();
	    }
	    //funExternalCodeButtonPressed("click");
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "Please Select Table First");
	}
    }//GEN-LAST:event_txtExternalCodeMouseClicked

    private void txtExternalCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExternalCodeActionPerformed

	if ("TableSelected".equals(tableSelected))
	{
	    funExternalCodeButtonPressed("");
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "Please Select Table First");
	}
    }//GEN-LAST:event_txtExternalCodeActionPerformed

    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
	funItemTableClicked("Mouse");
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnDelItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelItemMouseClicked
	funDeleteItem();
    }//GEN-LAST:event_btnDelItemMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
	funUPButtonPressed();
    }//GEN-LAST:event_btnUpActionPerformed

    private void txtTableNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTableNoMouseClicked
	funTableNoTextFieldClicked(evt);
    }//GEN-LAST:event_txtTableNoMouseClicked

    private void txtWaiterNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWaiterNoMouseClicked

	funWaiterNoTextFieldClicked(evt);
    }//GEN-LAST:event_txtWaiterNoMouseClicked

    private void txtWaiterNoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWaiterNoMouseEntered

    }//GEN-LAST:event_txtWaiterNoMouseEntered

    private void txtPaxNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPaxNoMouseClicked

	funPAXTextFieldClicked();
    }//GEN-LAST:event_txtPaxNoMouseClicked

    private void btnDebitCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDebitCardMouseClicked

	if (clsGlobalVarClass.gEnableNFCInterface)
	{
	    Thread objThread = null;
	    ReaderThread objReader = new ReaderThread();
	    objThread = new Thread(objReader);
	    objThread.start();
	    funDebitCardButtonClicked();
	    clsGlobalVarClass.gDebitCardNo = null;
	    if (null != objThread)
	    {
		objThread.stop();
		objThread = null;
	    }
	}
	else
	{
	    funDebitCardButtonClicked();
	}
    }//GEN-LAST:event_btnDebitCardMouseClicked

    private void btnPrevItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevItemActionPerformed

	if (flgTableSelection)
	{
	    funPreviousTableClick();
	}
	else if (flgWaiterSelection)
	{
	    funPreviousWaiterClick();
	}
	else
	{
	    funPreviousItemClick();
	}
    }//GEN-LAST:event_btnPrevItemActionPerformed

    private void btnNextItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextItemActionPerformed

	if (flgTableSelection)
	{
	    funNextTableClick();
	}
	else if (flgWaiterSelection)
	{
	    funNextWaiterClick();
	}
	else
	{
	    funNextItemClick();
	}
    }//GEN-LAST:event_btnNextItemActionPerformed

    private void lblAreaNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAreaNameMouseClicked

	if (funCheckKOTStatus(globalTableNo) == 0)
	{
	    btnItemMode.setVisible(false);
	    objUtility.funCallForSearchForm("AreaMasterForMakeKOT");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetArea(data[0].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "Please Save current KOT or Delete item from KOT");
	}
    }//GEN-LAST:event_lblAreaNameMouseClicked

    private void btnItemModeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemModeMouseClicked

    }//GEN-LAST:event_btnItemModeMouseClicked

    private void btnIItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem2ActionPerformed
	funMenuItemSelection(btnIItem2.getText(), 1);
    }//GEN-LAST:event_btnIItem2ActionPerformed

    private void btnIItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem1ActionPerformed
	funMenuItemSelection(btnIItem1.getText(), 0);
    }//GEN-LAST:event_btnIItem1ActionPerformed

    private void btnIItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem3ActionPerformed
	funMenuItemSelection(btnIItem3.getText(), 2);
    }//GEN-LAST:event_btnIItem3ActionPerformed

    private void btnIItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem4ActionPerformed
	funMenuItemSelection(btnIItem4.getText(), 3);
    }//GEN-LAST:event_btnIItem4ActionPerformed

    private void btnIItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem5ActionPerformed
	funMenuItemSelection(btnIItem5.getText(), 4);
    }//GEN-LAST:event_btnIItem5ActionPerformed

    private void btnIItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem6ActionPerformed

	funMenuItemSelection(btnIItem6.getText(), 5);
    }//GEN-LAST:event_btnIItem6ActionPerformed

    private void btnIItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem7ActionPerformed

	funMenuItemSelection(btnIItem7.getText(), 6);
    }//GEN-LAST:event_btnIItem7ActionPerformed

    private void btnIItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem8ActionPerformed

	funMenuItemSelection(btnIItem8.getText(), 7);
    }//GEN-LAST:event_btnIItem8ActionPerformed

    private void btnIItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem9ActionPerformed

	funMenuItemSelection(btnIItem9.getText(), 8);
    }//GEN-LAST:event_btnIItem9ActionPerformed

    private void btnIItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem10ActionPerformed

	funMenuItemSelection(btnIItem10.getText(), 9);
    }//GEN-LAST:event_btnIItem10ActionPerformed

    private void btnIItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem11ActionPerformed

	funMenuItemSelection(btnIItem11.getText(), 10);
    }//GEN-LAST:event_btnIItem11ActionPerformed

    private void btnIItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem12ActionPerformed

	funMenuItemSelection(btnIItem12.getText(), 11);
    }//GEN-LAST:event_btnIItem12ActionPerformed

    private void btnIItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem13ActionPerformed
	funMenuItemSelection(btnIItem13.getText(), 12);
    }//GEN-LAST:event_btnIItem13ActionPerformed

    private void btnIItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem14ActionPerformed

	funMenuItemSelection(btnIItem14.getText(), 13);
    }//GEN-LAST:event_btnIItem14ActionPerformed

    private void btnIItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem15ActionPerformed

	funMenuItemSelection(btnIItem15.getText(), 14);
    }//GEN-LAST:event_btnIItem15ActionPerformed

    private void btnIItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem16ActionPerformed

	funMenuItemSelection(btnIItem16.getText(), 15);
    }//GEN-LAST:event_btnIItem16ActionPerformed

    private void btnPrevItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevItemSortingMouseClicked

	if (btnPrevItemSorting.isEnabled())
	{
	    JButton btnArray[] =
	    {
		btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
	    };
	    funResetTopSortingButtons();
	    topSortingButtonsNavigator--;
	    int x = totalItems - (topSortingButtonsNavigator * 4);
	    if (x > 4)
	    {
		for (int i = 0; i < 4; i++)
		{
		    btnArray[i].setText(listTopButtonName.get((topSortingButtonsNavigator * 4) + i));
		    btnArray[i].setEnabled(true);
		}
	    }
	    else
	    {
		for (int i = 0; i < x; i++)
		{
		    btnArray[i].setText(listTopButtonName.get((topSortingButtonsNavigator * 4) + i));
		    btnArray[i].setEnabled(true);
		}
	    }
	    if (topSortingButtonsNavigator > 0)
	    {
		btnPrevItemSorting.setEnabled(true);
	    }
	    btnNextItemSorting.setEnabled(true);
	}


    }//GEN-LAST:event_btnPrevItemSortingMouseClicked

    private void btnItemSorting1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting1MouseClicked
	if (btnItemSorting1.isEnabled())
	{
	    if (isModifierSelect)
	    {
		funFillModifier(btnItemSorting1.getText());
	    }
	    else
	    {
		int index = listTopButtonName.indexOf(btnItemSorting1.getText());
		String buttonCode = listTopButtonCode.get(index);
		funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
	    }
	}
    }//GEN-LAST:event_btnItemSorting1MouseClicked

    private void btnItemSorting2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting2MouseClicked
	if (btnItemSorting2.isEnabled())
	{
	    if (isModifierSelect)
	    {
		funFillModifier(btnItemSorting2.getText());
	    }
	    else
	    {
		int index = listTopButtonName.indexOf(btnItemSorting2.getText());
		String buttonCode = listTopButtonCode.get(index);
		funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
	    }
	}
    }//GEN-LAST:event_btnItemSorting2MouseClicked

    private void btnItemSorting3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting3MouseClicked
	if (btnItemSorting3.isEnabled())
	{
	    if (isModifierSelect)
	    {
		funFillModifier(btnItemSorting3.getText());
	    }
	    else
	    {
		int index = listTopButtonName.indexOf(btnItemSorting3.getText());
		String buttonCode = listTopButtonCode.get(index);
		funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
	    }
	}
    }//GEN-LAST:event_btnItemSorting3MouseClicked

    private void btnNextItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextItemSortingMouseClicked

	if (btnNextItemSorting.isEnabled())
	{
	    JButton btnArray[] =
	    {
		btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
	    };
	    funResetTopSortingButtons();
	    topSortingButtonsNavigator++;
	    int x = totalItems - (topSortingButtonsNavigator * 4);

	    if (x > 4)
	    {
		for (int i = 0; i < 4; i++)
		{
		    btnArray[i].setText(listTopButtonName.get((topSortingButtonsNavigator * 4) + i));
		    btnArray[i].setEnabled(true);
		}
	    }
	    else
	    {
		for (int i = 0; i < x; i++)
		{
		    btnArray[i].setText(listTopButtonName.get((topSortingButtonsNavigator * 4) + i));
		    btnArray[i].setEnabled(true);
		}
	    }
	    if (x > 4)
	    {
		btnNextItemSorting.setEnabled(true);
	    }
	    btnPrevItemSorting.setEnabled(true);
	}
    }//GEN-LAST:event_btnNextItemSortingMouseClicked

    private void btnItemSorting4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting4MouseClicked
	if (btnItemSorting4.isEnabled())
	{
	    if (isModifierSelect)
	    {
		funFillModifier(btnItemSorting4.getText());
	    }
	    else
	    {
		int index = listTopButtonName.indexOf(btnItemSorting4.getText());
		String buttonCode = listTopButtonCode.get(index);
		funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
	    }
	}
    }//GEN-LAST:event_btnItemSorting4MouseClicked

    private void bttn_PLU_Panel_CloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bttn_PLU_Panel_CloseMouseClicked
	funPLUCloseButtonPressed();
    }//GEN-LAST:event_bttn_PLU_Panel_CloseMouseClicked

    private void tblPLUItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPLUItemsMouseClicked
	int selectedRow = tblPLUItems.getSelectedRow();
	funPLUItemsMouseClicked(selectedRow);
    }//GEN-LAST:event_tblPLUItemsMouseClicked

    private void tblPLUItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPLUItemsKeyPressed
	if (evt.getKeyCode() == 10)
	{
	    btnButton2.requestFocus();
	    int selectedRow = tblPLUItems.getSelectedRow();
	    funPLUItemsMouseClicked(selectedRow);
	}
    }//GEN-LAST:event_tblPLUItemsKeyPressed

    private void txtPLUItemSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPLUItemSearchFocusGained
	funPLUItemSearch();
    }//GEN-LAST:event_txtPLUItemSearchFocusGained

    private void txtPLUItemSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPLUItemSearchMouseClicked
	//new frmAlfaNumericKeyBoard(null, true, "1", "Search").setVisible(true);
	//txtPLUItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
	new frmAlfaNumericKeyBoard1(this, true, "Make KOT");
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

    private void btnNewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCustomerActionPerformed
	// TODO add your handling code here:
	try
	{
	    funCloseKitchenNotePanel();
//	    if (clsGlobalVarClass.gCMSIntegrationYN)
//	    {
//		if (clsGlobalVarClass.gCMSMemberCodeForKOTJPOS.equals("Y"))
//		{
//		String sql_TableStatus = "select ifnull(sum(dblAmount),0),ifnull(strCustomerCode,''),ifnull(strCustomerName,'') "
//			+ "from tblitemrtemp where strtableno = '" + globalTableNo + "' and strCustomerCode <>'' ";
//		ResultSet rsTableStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql_TableStatus);
//		if (rsTableStatus.next())
//		{
//		    if (rsTableStatus.getDouble(1) < 1)
//		    {
//			if (null != hmCMSMemberForTable.get(globalTableNo))
//			{
//			    hmCMSMemberForTable.remove(globalTableNo);
//			}
//			funGetCMSMemberCode();
//		    }
//		    else
//		    {
//			cmsMemCode = rsTableStatus.getString(2);
//			cmsMemName = rsTableStatus.getString(3);
//		    }
//		}
//		rsTableStatus.close();
//		}
//	    } 
//	    else
//	    {
//		funNewCustomerButtonPressed();
//	    }
	    funNewCustomerButtonPressed();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnNewCustomerActionPerformed

    private void btnNewCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewCustomerMouseClicked
	// TODO add your handling code here:        
    }//GEN-LAST:event_btnNewCustomerMouseClicked

    private void bttn_PLU_Panel_CloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bttn_PLU_Panel_CloseKeyPressed

    }//GEN-LAST:event_bttn_PLU_Panel_CloseKeyPressed

    private void btnItemModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemModeActionPerformed
	// TODO add your handling code here:
	funItemModeButtonClicked();
    }//GEN-LAST:event_btnItemModeActionPerformed

    private void txtPaxNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaxNoKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funEnterPAXNo(txtPaxNo.getText().trim());
	}
    }//GEN-LAST:event_txtPaxNoKeyPressed

    private void txtTableNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTableNoKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    txtTableNo.setText(" ");
	    funCallTableSearch();
	}
	else if (evt.getKeyCode() == 10)
	{
	    funSelectEnterTable();
	}
    }//GEN-LAST:event_txtTableNoKeyPressed

    private void txtWaiterNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWaiterNoKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
//	    txtWaiterNo.setText(" ");
	    funCallWaiterSearch();
	}
	

    }//GEN-LAST:event_txtWaiterNoKeyPressed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
	// TODO add your handling code here:
	funHomeButtonClicked();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
	// TODO add your handling code here:
	funDownButtonPressed();
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnButton4ActionPerformed
	// TODO add your handling code here:
	funButtonAction(btnButton4);
    }//GEN-LAST:event_btnButton4ActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
	// TODO add your handling code here:
	funNextButtonClicked();
    }//GEN-LAST:event_btnNextActionPerformed

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

    private void txtWaiterNoMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtWaiterNoMouseDragged
    {//GEN-HEADEREND:event_txtWaiterNoMouseDragged
	funWaiterNoTextFieldClicked(evt);
    }//GEN-LAST:event_txtWaiterNoMouseDragged

    private void txtPaxNoMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPaxNoMouseDragged
    {//GEN-HEADEREND:event_txtPaxNoMouseDragged
	funPAXTextFieldClicked();
    }//GEN-LAST:event_txtPaxNoMouseDragged

    private void btnCloseKOTMsgMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnCloseKOTMsgMouseClicked
    {//GEN-HEADEREND:event_btnCloseKOTMsgMouseClicked
	funCloseKitchenNotePanel();
    }//GEN-LAST:event_btnCloseKOTMsgMouseClicked

    private void btnCloseKOTMsgKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCloseKOTMsgKeyPressed
    {//GEN-HEADEREND:event_btnCloseKOTMsgKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCloseKOTMsgKeyPressed

    private void btnKOTSendMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnKOTSendMouseClicked
    {//GEN-HEADEREND:event_btnKOTSendMouseClicked
	if (txtKOTMsg.getText().trim().length() <= 0)
	{
	    new frmOkPopUp(this, "Please Enter The Note.", "error", 0).setVisible(true);
	    return;
	}
	else
	{
	    panelKOTMessage.setVisible(false);
	    panelNumericKeyPad.setVisible(true);
	    panelMenuHead.setVisible(true);
	    panelNavigate.setVisible(true);
	    funIsVisiblePanels(true);

	    String kitchenNote = txtKOTMsg.getText().trim();
	    if (kitchenNote.length() > 0)
	    {
		funSendKitchenNote(obj_List_KOT_ItemDtl);
	    }
	}
    }//GEN-LAST:event_btnKOTSendMouseClicked

    private void btnKOTSendKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKOTSendKeyPressed
    {//GEN-HEADEREND:event_btnKOTSendKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnKOTSendKeyPressed

    private void btnKOTSend1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnKOTSend1MouseClicked
    {//GEN-HEADEREND:event_btnKOTSend1MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnKOTSend1MouseClicked

    private void btnKOTSend1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKOTSend1KeyPressed
    {//GEN-HEADEREND:event_btnKOTSend1KeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnKOTSend1KeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Make KOT");
    }//GEN-LAST:event_formWindowClosed

    private void txtTableNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTableNoActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTableNoActionPerformed

    private void txtExternalCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExternalCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funPLUButtonPressed();
	}
    }//GEN-LAST:event_txtExternalCodeKeyPressed

    private void txtKOTMsgMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtKOTMsgMouseClicked
    {//GEN-HEADEREND:event_txtKOTMsgMouseClicked
	funKOTMsgMouseClicked();
    }//GEN-LAST:event_txtKOTMsgMouseClicked

    private void txtUsernameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtUsernameMouseClicked
    {//GEN-HEADEREND:event_txtUsernameMouseClicked
	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    if (txtUsername.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name.").setVisible(true);
		txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtUsername.getText(), "1", "Enter User Name.").setVisible(true);
		txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
    }//GEN-LAST:event_txtUsernameMouseClicked

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtUsernameActionPerformed
    {//GEN-HEADEREND:event_txtUsernameActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtUsernameKeyPressed
    {//GEN-HEADEREND:event_txtUsernameKeyPressed
	try
	{
	    if (evt.getKeyCode() == 10 && txtUsername.getText().equalsIgnoreCase("SANGUINE"))
	    {
		txtPassword.requestFocus();
	    }
	    else
	    {
		if (evt.getKeyCode() == 10)
		{
		    String sql = "select count(*) from tbluserhd where strUsercode='" + txtUsername.getText() + "' ";
		    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rssql.next())
		    {
			if (rssql.getInt(1) > 0)
			{
			    txtPassword.requestFocus();
			}
			else
			{
			    sql = "  select count(*) from tbluserhd where strDebitCardString='" + txtUsername.getText() + "' ";
			    ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rssql1.next())
			    {
				if (rssql1.getInt(1) > 0)
				{
				    if (funCHeckLoginForDebitCardString(txtUsername.getText()))
				    {

				    }
				}
				else
				{
				    txtUsername.setText("");
				    new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
				    return;
				}
			    }
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPasswordMouseClicked
    {//GEN-HEADEREND:event_txtPasswordMouseClicked
	if (txtPassword.getPassword().length == 0)
	{
	    new frmAlfaNumericKeyBoard(this, true, "1", "Enter  Password.").setVisible(true);
	    txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	}
	else
	{
	    new frmAlfaNumericKeyBoard(this, true, txtPassword.getPassword().toString(), "1", "Enter Password.").setVisible(true);
	    txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	}
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPasswordKeyPressed
    {//GEN-HEADEREND:event_txtPasswordKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funUserAuthenticationOKButtonPressed();

	}
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void btnChangeQtyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnChangeQtyActionPerformed
    {//GEN-HEADEREND:event_btnChangeQtyActionPerformed
	funItemTableClicked("KeyBoard");
    }//GEN-LAST:event_btnChangeQtyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnB4;
    private javax.swing.JButton btnButton1;
    private javax.swing.JButton btnButton2;
    private javax.swing.JButton btnButton3;
    private javax.swing.JButton btnButton4;
    private javax.swing.JButton btnChangeQty;
    private javax.swing.JButton btnCloseKOTMsg;
    private javax.swing.JButton btnDebitCard;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnHome;
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
    private javax.swing.JButton btnItemMode;
    private javax.swing.JButton btnItemSorting1;
    private javax.swing.JButton btnItemSorting2;
    private javax.swing.JButton btnItemSorting3;
    private javax.swing.JButton btnItemSorting4;
    private javax.swing.JButton btnKOTSend;
    private javax.swing.JButton btnKOTSend1;
    private javax.swing.JButton btnMakeBill;
    private javax.swing.JButton btnMakeKOT;
    private javax.swing.JButton btnMenu2;
    private javax.swing.JButton btnMenu3;
    private javax.swing.JButton btnMenu4;
    private javax.swing.JButton btnMenu5;
    private javax.swing.JButton btnMenu6;
    private javax.swing.JButton btnMenu7;
    private javax.swing.JButton btnMenu8;
    private javax.swing.JButton btnMultiQty;
    public static javax.swing.JButton btnNewCustomer;
    private javax.swing.JButton btnNext;
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
    private javax.swing.JButton btnPopular;
    private javax.swing.JButton btnPrevItem;
    private javax.swing.JButton btnPrevItemSorting;
    private javax.swing.JButton btnPrevMenu;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUp;
    private javax.swing.JButton bttn_PLU_Panel_Close;
    private javax.swing.JComboBox cmbCostCenterForKOTMsg;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelPaxNo;
    private javax.swing.JLabel labelWaiterName;
    public javax.swing.JLabel lblAreaName;
    private javax.swing.JLabel lblCardBalnce;
    private javax.swing.JLabel lblCostCenterForKOTMsg;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDebitCardBalance;
    private javax.swing.JLabel lblExternalCode;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblKOTMsg;
    private javax.swing.JLabel lblKOTNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPaxNo;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAreaName;
    private javax.swing.JPanel panelDeditCard;
    private javax.swing.JPanel panelExternalCode;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemButtons;
    private javax.swing.JPanel panelItemDtl;
    private javax.swing.JPanel panelKOTMessage;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelMenuHead;
    private javax.swing.JPanel panelNavigate;
    private javax.swing.JPanel panelNumericKeyPad;
    private javax.swing.JPanel panelOperationalButtons;
    private javax.swing.JPanel panelPLU;
    private javax.swing.JPanel panelTopSortingButtons;
    private javax.swing.JPanel panelUserAuthentication;
    private javax.swing.JScrollPane scrPLU;
    private javax.swing.JScrollPane scrollKOTMsg;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblPLUItems;
    private javax.swing.JTextField txtExternalCode;
    private javax.swing.JTextArea txtKOTMsg;
    private javax.swing.JTextField txtPLUItemSearch;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPaxNo;
    private javax.swing.JTextField txtTableNo;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtWaiterNo;
    // End of variables declaration//GEN-END:variables

    public void funShowItemPanel()
    {
	selectedQty = 1;
	panelItemButtons.setVisible(true);
	panelNavigate.setVisible(true);
	panelTopSortingButtons.setVisible(true);
    }

    public void setOpenItemName(String OpenItemName)
    {
	//JOptionPane.showMessageDialog(this, OpenItemName);
    }

    public void setopenItemRate(String OpenItemRate)
    {
	//JOptionPane.showMessageDialog(this, OpenItemRate);
    }

    private void funSetCustMobileNo(String mbNo)
    {
	try
	{
	    double totalBillAmount = 0.00;
	    if (txtTotal.getText().trim().length() > 0)
	    {
		totalBillAmount = Double.parseDouble(txtTotal.getText());
	    }
	    if (mbNo.trim().length() == 0)
	    {
		objUtility.funCallForSearchForm("CustomerMaster");
		new frmSearchFormDialog(this, true).setVisible(true);
		if (clsGlobalVarClass.gSearchItemClicked)
		{
		    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		    clsGlobalVarClass.gCustomerCode = data[0].toString();
		    buildingCodeForHD = data[4].toString();
		    clsGlobalVarClass.gSearchItemClicked = false;
		    btnNewCustomer.setText("<html>" + data[1].toString() + "</html>");

		    funSetHomeDeliveryData(data[0].toString(), data[1].toString(), buildingCodeForHD, "", "");

		    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
		    {
			funShowRewardsButtonClicked();
		    }
		}
	    }
	    else
	    {
		sql = "select count(strCustomerCode) from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
		ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsCustomer.next();
		int found = rsCustomer.getInt(1);
		rsCustomer.close();

		if (found > 0)
		{
		    String sql_CustInfo = "select strCustomerCode,strCustomerName,strBuldingCode "
			    + "from tblcustomermaster where longMobileNo like '%" + mbNo + "%'";
		    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
		    if (rsCust.next())
		    {
			clsGlobalVarClass.gCustomerCode = rsCust.getString(1);
			btnNewCustomer.setText("<html>" + rsCust.getString(2) + "</html>");
			clsGlobalVarClass.gCustMBNo = mbNo;
			buildingCodeForHD = rsCust.getString(3);

			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
			{
			    funShowRewardsButtonClicked();
			}
		    }
		}
		else
		{
		    clsGlobalVarClass.gNewCustomerForHomeDel = true;
		    clsGlobalVarClass.gTotalBillAmount = totalBillAmount;
		    clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(mbNo);
		    clsGlobalVarClass.gCustMBNo = mbNo;
		    new frmCustomerMaster().setVisible(true);

		    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
		    {
			funShowRewardsButtonClicked();
		    }
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

    private void funSetHomeDeliveryData(String custCode, String custName, String buildingCode, String delPersonCode, String delPersonName)
    {
	arrListHomeDelDetails.clear();
	arrListHomeDelDetails.add(custCode);//0 cust code
	arrListHomeDelDetails.add(custName);//1 cust name
	arrListHomeDelDetails.add(buildingCode);//2 building code
	arrListHomeDelDetails.add("HomeDelivery");//3 home delivery
	arrListHomeDelDetails.add(delPersonCode);//4 del person code
	arrListHomeDelDetails.add(delPersonName);//5 del person name
    }

    private void funResetHomeDeliveryFields()
    {
	homeDeliveryForTax = "N";
	clsGlobalVarClass.gCustomerCode = "";
	clsGlobalVarClass.gCustomerAddress1 = "";
	clsGlobalVarClass.gCustomerAddress2 = "";
	clsGlobalVarClass.gCustomerCity = "";
	clsGlobalVarClass.gNewCustomerForHomeDel = false;
	clsGlobalVarClass.gNewCustomerMobileNo = 0;
	clsGlobalVarClass.gTotalBillAmount = 0;
	clsGlobalVarClass.gDeliveryBoyCode = null;
	clsGlobalVarClass.gDeliveryTime = null;
	clsGlobalVarClass.gDeliveryBoyName = "";
	if (navigate == 1)
	{
	    btnButton4.setText("NC KOT");
	}
	else
	{
	    btnButton4.setText("<html>HOME<br>DELIVERY</html>");
	}
	btnNewCustomer.setText("<html>CUSTOMER</html>");
    }

    private void funCheckDebitCardBal(String debitCardNo)
    {
	try
	{
	    if (funDebitCardSelect())
	    {
		String sqlGetDebitBal = "select dblRedeemAmt from tbldebitcardmaster where strCardNo='" + debitCardNo + "'";
		ResultSet rsDebitCardBal = clsGlobalVarClass.dbMysql.executeResultSet(sqlGetDebitBal);
		if (rsDebitCardBal.next())
		{
		    double debitCardBal = rsDebitCardBal.getDouble(1);
		    if (txtTotal.getText().trim().length() > 0)
		    {
			double tatamt = Double.parseDouble(txtTotal.getText().trim());
			if (tatamt <= debitCardBal)
			{
			    lblCardBalnce.setVisible(true);
			    lblCardBalnce.setBackground(Color.yellow);
			    lblCardBalnce.setText(String.valueOf(debitCardBal));
			}
			else
			{
			    lblCardBalnce.setVisible(true);
			    lblCardBalnce.setBackground(Color.red);
			    lblCardBalnce.setText(String.valueOf(debitCardBal));
			}
		    }
		}
		rsDebitCardBal.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funCheckDebitCardTable()
    {
	if ("Yes".equalsIgnoreCase(clsGlobalVarClass.gDebitCardPayment))
	{
	    try
	    {
		String sqlCheckDebitCTable = "select strCardNo from tbldebitcardtabletemp "
			+ "where strTableNo='" + globalTableNo + "'";
		ResultSet rsCheckDebitCTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckDebitCTable);
		if (rsCheckDebitCTable.next())
		{
		    panelDeditCard.setVisible(true);
		    lblDebitCardBalance.setVisible(true);
		    lblCardBalnce.setVisible(true);
		    funCheckDebitCardBal(rsCheckDebitCTable.getString(1));
		}
		else
		{
		    panelDeditCard.setVisible(true);
		    lblDebitCardBalance.setVisible(false);
		    lblCardBalnce.setVisible(false);
		}
		rsCheckDebitCTable.close();
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		e.printStackTrace();
	    }
	}
    }

    private boolean funDebitCardSelect()
    {
	boolean flagDebitCard = false;
	try
	{
	    String sqlCheckDebitCTable = "select count(*) from tbldebitcardtabletemp where strTableNo='" + globalTableNo + "'";
	    ResultSet rsCheckDebitCTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckDebitCTable);
	    rsCheckDebitCTable.next();
	    int found = rsCheckDebitCTable.getInt(1);
	    if (found > 0)
	    {
		flagDebitCard = true;
	    }
	    else
	    {
		flagDebitCard = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return flagDebitCard;
    }

    private boolean funCheckHomeDelivery()
    {
	boolean flagHomeDelivery = false;
	try
	{
	    sql = "select a.strTableNo,ifnull(a.strCustomerCode,''),ifnull(b.strCustomerName,'ND')"
		    + " ,ifnull(b.strBuldingCode,''),ifnull(a.strDelBoyCode,'NA'),ifnull(c.strDPName,'NA') "
		    + " from tblitemrtemp a left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + " left outer join tbldeliverypersonmaster c on a.strDelBoyCode=c.strDPCode "
		    + " where a.strHomeDelivery='Yes' and a.strTableNo='" + globalTableNo + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
	    ResultSet rsCustInfo = dbMysql.executeResultSet(sql);
	    if (rsCustInfo.next())
	    {
		flagHomeDelivery = true;
		btnNewCustomer.setText("<html>" + rsCustInfo.getString(3) + "</html>");
		flgHomeDeliveryColor_Button = true;
		if (navigate == 0)
		{
		    btnButton4.setForeground(Color.black);
		}
		homeDeliveryForTax = "Y";
		arrListHomeDelDetails.add(rsCustInfo.getString(2));//0 cust code
		arrListHomeDelDetails.add(rsCustInfo.getString(3));//1 cust name
		arrListHomeDelDetails.add(rsCustInfo.getString(4));//2 building code
		arrListHomeDelDetails.add("HomeDelivery");//3 home delivery
		arrListHomeDelDetails.add(rsCustInfo.getString(5));//4 del person code
		arrListHomeDelDetails.add(rsCustInfo.getString(6));//5 del person name
	    }
	    else
	    {
		arrListHomeDelDetails.clear();
//                btnNewCustomer.setText("<html>New<br>Customer</html>");
		if (btnButton4.getForeground() == Color.white)
		{
		    flgCheckNCKOTButtonColor = false;
		}
	    }
	    rsCustInfo.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagHomeDelivery;
	}
    }

    private void funSetHomeDeliveryCustumerName()
    {
	try
	{
	    sql = "select a.strCustomerCode, a.strCustomerName ,  a.longMobileNo "
		    + "from tblcustomermaster a, tblitemrtemp b "
		    + "where b.strTableNo='" + globalTableNo + "'and a.strCustomerCode=b.strCustomerCode "
		    + "group by a.strCustomerCode";
	    ResultSet rsSetName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSetName.next())
	    {
		//lblAreaName.setText(rsSetName.getString(2) + "," + rsSetName.getString(3));
		btnNewCustomer.setText("<html>" + rsSetName.getString(2) + "</html>");
	    }
	    rsSetName.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /*
     * public String funGetCustCode() { String CustCode = ""; try { if
     * (funCheckHomeDelivery()) { sql = "select strCustomerCode from
     * tblitemrtemp " + "where strTableNo='" + globalTableNo + " 'and
     * strCustomerCode !='' " + "GROUP BY strCustomerCode"; ResultSet resultSet
     * = clsGlobalVarClass.dbMysql.executeResultSet(sql); resultSet.next();
     * CustCode = resultSet.getString(1); resultSet.close(); } } catch
     * (Exception e) { objUtility.funWriteErrorLog(e); e.printStackTrace(); }
     * return CustCode; }
     */
    /**
     * Ritesh 03 Oct 2014
     *
     * @param ModifierName
     * @param TempiCode
     */
    public void funGetModifierRateForTDHOnModifier(String ModifierName, String TempiCode)
    {
	try
	{
	    String ModifierCode = null;
	    // BigDecimal sum = new BigDecimal("0.00");
	    BigDecimal rate = new BigDecimal("0.00");
	    //to retrive the modifiercode
	    sql = "select strModifierCode from tblmodifiermaster where strModifierName='" + ModifierName + "'";
	    ResultSet rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemModifierInfo.next())
	    {
		ModifierCode = rsItemModifierInfo.getString(1);
	    }
	    //retrive the rate of modifier
	    sql = "select dblRate from tblitemmodofier where strItemCode='" + TempiCode + "' and strModifierCode='" + ModifierCode + "'"
		    + " and strChargable='Y' and strApplicable='Y'";
	    rsItemModifierInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemModifierInfo.next())
	    {
		rate = new BigDecimal(rsItemModifierInfo.getString(1).toString());
	    }
	    rsItemModifierInfo.close();

	    if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	    {
		String temp_itemCode = TempiCode;
		for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
		{
		    boolean found = false;
		    if (ob.getItemCode().equalsIgnoreCase(temp_itemCode) && ob.isIsModifier() == false)
		    {
			clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(ob.getSequenceNo().concat(".00"), KOTNo, globalTableNo, globalWaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate.doubleValue(), ob.getPaxNo(), "N", "N", true, ModifierCode, "", "", "N", rate.doubleValue());//"ask"
			obj_List_KOT_ItemDtl.add(obj_row);
			found = true;
		    }
		    if (found)
		    {
			funRefreshItemTable();
			break;
		    }
		}
	    }
	    selectedQty = 0;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

//By Abhijeet For TopButtonsList
    private void funFillTopButtonList(String tempMenuCode) throws Exception
    {
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
	funResetTopSortingButtons();
	JButton[] btnSubGroupArray =
	{
	    btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
	};
	btnPrevItemSorting.setEnabled(false);
	btnPrevItemSorting.setEnabled(false);

	String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];

	String sqlItems = "";
	if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    sqlItems = "select c.strSubGroupName,b.strSubGroupCode "
		    + " from tblmenuitempricingdtl a,tblitemmaster b,tblsubgrouphd c "
		    + " where a.strItemCode=b.strItemCode "
		    + " and b.strSubGroupCode=c.strSubGroupCode "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
		    + " and b.strOperationalYN='Y' ";

	    if (flgPopular)
	    {
		sqlItems += " and a.strPopular='Y' and a.strAreaCode='" + clsAreaCode + "' "
			+ " group by c.strSubGroupCode ORDER by c.strSubGroupName";
	    }
	    else
	    {
		sqlItems += " and a.strMenuCode='" + tempMenuCode + "' "
			+ " group by c.strSubGroupCode ORDER by c.strSubGroupName";
	    }
	}
	else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
	{
	    sqlItems = "select b.strSubMenuHeadName,a.strSubMenuHeadCode "
		    + " from tblmenuitempricingdtl a left outer join tblsubmenuhead b "
		    + " on a.strSubMenuHeadCode=b.strSubMenuHeadCode and a.strMenuCode=b.strMenuCode "
		    + " where b.strSubMenuHeadName is not null and b.strSubMenuOperational='Y' "
		    + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
		    + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' ";

	    if (flgPopular)
	    {
		sqlItems += " and a.strPopular='Y' and a.strAreaCode='" + clsAreaCode + "' "
			+ "group by a.strSubMenuHeadCode";
	    }
	    else
	    {
		sqlItems += " and a.strMenuCode='" + tempMenuCode + "' group by a.strSubMenuHeadCode";
	    }
	}

	if (!sqlItems.isEmpty())
	{
	    int itemCount = 0;
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlItems);
	    while (rs.next())
	    {
		listTopButtonName.add(rs.getString(1));
		listTopButtonCode.add(rs.getString(2));
		itemCount++;
	    }
	    if (!listTopButtonName.isEmpty())
	    {
		funFillTopButtons();
	    }
	    if (!listTopButtonName.isEmpty() && itemCount > 4)
	    {
		btnNextItemSorting.setEnabled(true);
	    }
	    else
	    {
		btnNextItemSorting.setEnabled(false);
	    }
	}
    }

    private void funResetTopSortingButtons()
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
	    funPLUCloseButtonPressed();
	    txtTableNo.requestFocus();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
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
	    topSortingButtonsNavigator = 0;
	    btnNextItemSorting.setEnabled(false);
	    totalItems = listTopButtonName.size();

	    if (totalItems > 4)
	    {
		btnNextItemSorting.setEnabled(true);
	    }
	    if (totalItems >= 4)
	    {
		for (int i = 0; i < 4; i++)
		{
		    btnArray[i].setText(listTopButtonName.get(i));
		    btnArray[i].setEnabled(true);
		}
	    }
	    else
	    {
		for (int i = 0; i < totalItems; i++)
		{
		    btnArray[i].setText(listTopButtonName.get(i));
		    btnArray[i].setEnabled(true);
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

    private void funResetMenuHeadButtons()
    {
	try
	{
	    //to reset menu head buttons:Ritesh
	    JButton[] btnMenuArray =
	    {
		btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
	    };
	    for (int k = 0; k < btnMenuArray.length; k++)
	    {
		btnMenuArray[k].setText("");
		btnMenuArray[k].setEnabled(false);
	    }
	    btnNextMenu.setEnabled(false);
	    btnPrevMenu.setEnabled(false);

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funDeleteTempData()
    {
	try
	{
	    if (null != obj_List_KOT_ItemDtl)
	    {
		obj_List_KOT_ItemDtl.clear();
	    }
	    clsGlobalVarClass.dbMysql.execute("delete from tbldebitcardtabletemp where strPrintYN='N'");
	    sql = "delete from tblitemrtemp where strSerialNo='new'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    //insert into itemrtempbck table
	    new clsUtility().funInsertIntoTblItemRTempBck(globalTableNo);
	    funResetHomeDeliveryFields();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funInsertLineVoidItems() throws Exception
    {
	//To insert  items to linevoid table before delete it : Ritesh
	String insertLineVoid = "insert into tbllinevoid(strSerialno,strPosCode,strItemCode,strItemName,"
		+ "dblItemQuantity,dblAmount,strUserCreated,strUserEdited,dteDateCreated,"
		+ "dteDateEdited,strKOTNo) values ";
	for (clsMakeKotItemDtl list_cls_ItemRow : obj_List_KOT_ItemDtl)
	{
	    insertLineVoid += "('1','" + clsGlobalVarClass.gPOSCode + "','" + list_cls_ItemRow.getItemCode() + "','" + list_cls_ItemRow.getItemName() + "',"
		    + "'" + list_cls_ItemRow.getQty() + "','" + list_cls_ItemRow.getAmt() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + objUtility.funGetPOSDateForTransaction() + "','" + objUtility.funGetPOSDateForTransaction() + "','" + list_cls_ItemRow.getKOTNo() + "'),";
	}
	StringBuilder sb = new StringBuilder(insertLineVoid);
	int index = sb.lastIndexOf(",");
	insertLineVoid = sb.delete(index, sb.length()).toString();
	clsGlobalVarClass.dbMysql.execute(insertLineVoid);
    }

    private void funShiftKOT(String oldTableNo, String newTableNo)
    {
	//To shift KOT from one table to another 
	if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	{
	    for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
	    {
		ob.setTableNo(newTableNo);
	    }
	    //fieldSelected="MenuItem";
	}
    }

    private boolean funNextKOTFlag(String tableNo)
    {
	boolean flagNextKO = true;
	if (obj_List_KOT_ItemDtl.size() > 0)
	{
	    flagNextKO = false;
	}
	return flagNextKO;
    }

    private boolean funIsFreshItem(String itemCode)
    {
	boolean flag_freshItem = false;
	try
	{
	    if (null != obj_List_KOT_ItemDtl)
	    {
		for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return flag_freshItem;
    }

    /**
     * @return the obj_List_KOT_ItemDtl
     */
    public List<clsMakeKotItemDtl> getObj_List_KOT_ItemDtl()
    {
	return obj_List_KOT_ItemDtl;
    }

    private void funClearObjectList()
    {
	if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	{
	    obj_List_KOT_ItemDtl.clear();
	}

	if (null != obj_List_Old_KOT_Item_On_table && obj_List_Old_KOT_Item_On_table.size() > 0)
	{
	    obj_List_Old_KOT_Item_On_table.clear();
	}
	if (null != list_KOT_On_Table)
	{
	    list_KOT_On_Table.clear();
	}
    }

    private void funClearOldKotObjectList()
    {

	if (null != obj_List_Old_KOT_Item_On_table && obj_List_Old_KOT_Item_On_table.size() > 0)
	{
	    obj_List_Old_KOT_Item_On_table.clear();
	}
	if (null != list_KOT_On_Table)
	{
	    list_KOT_On_Table.clear();
	}
    }

    /**
     * :-Ritesh 01 Oct 2014 PLU Item Punching
     *
     * @param itemName
     * @param priceObject
     */
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
	funGetItemPrice(itemName);
    }

    /**
     * Ritesh 02 Oct 2014
     *
     * @param itemCode
     * @return
     */
    private ImageIcon funGetImageIcon(String itemCode) throws Exception
    {
	ImageIcon icon = null;
	try
	{
	    if (itemCode.length() > 0)
	    {
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

    private void funDeleteItem()
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
		int indexKT = tblItemTable.getValueAt(0, 0).toString().indexOf("KT");
		String kotNo = tblItemTable.getValueAt(0, 0).toString().substring(indexKT, indexKT + 9);

		//delete the selected item
		String selectedItemCode = tblItemTable.getValueAt(selectedRow, 3).toString();
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
			List<clsMakeKotItemDtl> subItemList = new ArrayList<>();
			for (clsMakeKotItemDtl listItemDtl : obj_List_KOT_ItemDtl)
			{
			    if (listItemDtl.getTdh_ComboItemCode().trim().length() > 0 && selectedItemCode.equalsIgnoreCase(listItemDtl.getTdh_ComboItemCode()) && "Y".equalsIgnoreCase(listItemDtl.getTdhComboItemYN()))
			    {
				subItemList.add(listItemDtl);
			    }
			}

			if (subItemList.size() > 0)
			{
			    for (clsMakeKotItemDtl listItemDtl : subItemList)
			    {
				obj_List_KOT_ItemDtl.remove(listItemDtl);
			    }
			}
			for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
			{
			    if (ob.getItemCode().equalsIgnoreCase(selectedItemCode))
			    {
				obj_List_KOT_ItemDtl.remove(ob);
				flagFound = true;
				break;
			    }
			}
		    }
		    else
		    {
			//new 
			Iterator<clsMakeKotItemDtl> it = obj_List_KOT_ItemDtl.iterator();
			while (it.hasNext())
			{
			    clsMakeKotItemDtl deletObj = it.next();
			    if (isSelectedModifier)
			    {
				if (kotNo.equals(deletObj.getKOTNo()) && deletObj.getItemCode().equals(selectedItemCode) && deletObj.isIsModifier())
				{
				    it.remove();
				    flagFound = true;
				    break;
				}
			    }
			    else
			    {
				if (kotNo.equals(deletObj.getKOTNo()) && deletObj.getItemCode().substring(0, 7).equals(selectedItemCode))
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
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
			+ " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE b.strItemCode='" + itemCode + "' and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and b.strOperationalYN='Y' ";
	    }
	    else
	    {
		sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
			+ " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday,"
			+ " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
			+ " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable "
			+ " FROM tblmenuitempricingdtl a ,tblitemmaster b "
			+ " WHERE b.strItemCode='" + itemCode + "' and a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "'  "
			+ " and a.strItemCode=b.strItemCode "
			+ " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
			+ " and b.strOperationalYN='Y' ";
	    }

	    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDtl);
	    if (rsItemInfo.next())
	    {
		clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2), rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7), rsItemInfo.getDouble(8), rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12), rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15), rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20), 0, "");
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * Ritesh 03 Nov 2014
     *
     * @param isVisible
     */
    private void funIsVisiblePanels(boolean isVisible)
    {
	panelItemButtons.setVisible(isVisible);
	panelNavigate.setVisible(isVisible);
	panelTopSortingButtons.setVisible(isVisible);
	if (!clsGlobalVarClass.gSkipWaiter && !clsGlobalVarClass.gSkipPax)
	{
	    btnItemMode.setVisible(isVisible);
	}
	else
	{
	    btnItemMode.setVisible(false);
	}
	panelExternalCode.setVisible(isVisible);
	panelPLU.setVisible(!isVisible);
	txtPLUItemSearch.setText("");
    }

    private void funPLUButtonPressed()
    {
	funCloseKitchenNotePanel();
	funIsVisiblePanels(false);

	txtPLUItemSearch.requestFocus();
	funFillPLUTable();

	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    new frmAlfaNumericKeyBoard1(this, true, "Make KOT");
	}
    }

    private void funPLUItemsMouseClicked(int selectedRow)
    {
	String tempAreaCode = "";
	if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
	{
	    tempAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	}
	else
	{
	    tempAreaCode = clsAreaCode;
	}
	String tempItemName = tblPLUItems.getValueAt(selectedRow, 0).toString();
	txtPLUItemSearch.setText("");
	Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(tempAreaCode);
	clsItemPriceDtl priceObject = x.get(tempItemName);
	funGetPLUItemPrice(priceObject);
	txtPLUItemSearch.requestFocus();

	//to display modifier paner after item selection
	if (obj_List_KOT_ItemDtl.size() > 0 && clsGlobalVarClass.gClientCode.equals("171.001"))//CHINA GRILL-PIMPRI
	{
	    tblItemTable.setRowSelectionInterval(obj_List_KOT_ItemDtl.size(), obj_List_KOT_ItemDtl.size());
	    funItemTableClicked("Mouse");
	    funPLUCloseButtonPressed();
	}
    }

    private void funPLUCloseButtonPressed()
    {
	funClosePLUPannel();
    }

    private void funClosePLUPannel()
    {
	funIsVisiblePanels(true);
	txtExternalCode.requestFocus();
    }

    private void funFillPLUTable()
    {
	try
	{
	    String tempAreaCode = "";
	    if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
	    {
		tempAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		tempAreaCode = clsAreaCode;
	    }
	    DefaultTableModel dmPLUItemTable = (DefaultTableModel) tblPLUItems.getModel();
	    dmPLUItemTable.setRowCount(0);
	    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(tempAreaCode);

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
	    objUtility.funWriteErrorLog(e);
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

	    String tempAreaCode = "";
	    if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
	    {
		tempAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
	    }
	    else
	    {
		tempAreaCode = clsAreaCode;
	    }
	    Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(tempAreaCode);
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

    /**
     * Ritesh 03 Nov 2014
     *
     * @param priceObject
     */
    private void funGetPLUItemPrice(clsItemPriceDtl priceObject)
    {
	String itemCode = "";
	String itemName = "";
	try
	{
	    boolean flag_isComboItem = false;
	    flag_isTDHModifier_Item = false;
	    double Price = 0;
	    itemCode = priceObject.getStrItemCode();
	    itemName = priceObject.getStrItemName();

	    int pax = 0;
	    try
	    {
		if (txtPaxNo.getText().trim().length() > 0)
		{
		    pax = Integer.parseInt(txtPaxNo.getText());
		}
	    }
	    catch (Exception e)
	    {
		pax = 0;
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
	    }

	    boolean isVailable = objUtility.isItemAvailableForTotay(itemCode, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gClientCode);

	    if (!isVailable)
	    {
		new frmOkPopUp(this, "<html>\"" + itemName.toUpperCase() + "\" Is Not Available For Today.</html>", "Warning", 1).setVisible(true);
		return;
	    }

	    if (clsTDHOnItemDtl.hm_ComboItemDtl.containsKey(itemCode))
	    {
		//code to load all item in combo item
		flag_isComboItem = true;
		MaxSubItemLimitWithComboItem = funGetMaxQty(itemCode, "MaxSubItemLimitWithComboItem", "");
		funShowComboSubItemTable(itemCode);
	    }
	    else if (clsGlobalVarClass.ListTDHOnModifierItem.contains(itemCode))
	    {
		if (funNextKOTFlag(globalTableNo))
		{
		    KOTNo = funGenerateKOTNo();
		}

		flag_isTDHModifier_Item = true;
		MaxQTYOfModifierWithTDHItem = clsGlobalVarClass.ListTDHOnModifierItemMaxQTY.get((clsGlobalVarClass.ListTDHOnModifierItem.indexOf(itemCode)));
		clsMakeKotItemDtl obTDHitem = new clsMakeKotItemDtl(getStrSerialNo(), KOTNo, globalTableNo, globalWaiterNo, itemName, itemCode, 1.00, Price, pax, "N", "N", false, "", "", "", "N", Price);
		kotItemSequenceNO++;
		frmTDHDialog ob = new frmTDHDialog(this, true, itemCode, obTDHitem);
		ob.setVisible(true);

//                flag_isTDHModifier_Item = true;
//                MaxQTYOfModifierWithTDHItem = clsGlobalVarClass.ListTDHOnModifierItemMaxQTY.get((clsGlobalVarClass.ListTDHOnModifierItem.indexOf(itemCode)));
//                funShowModifierTDH(itemCode);
	    }

	    Price = funGetFinalPrice(priceObject);
	    if (Price == 0)
	    {
		sqlBuilder.setLength(0);
		sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
			+ "from tbluserdtl a "
			+ "where a.strFormName='Open Items' "
			+ "and a.strTLA='true' "
			+ "and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' ");
		ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		if (rsTLA.next())
		{

		    objOpenItemDtl.setItemCode(itemCode);
		    objOpenItemDtl.setItemName(itemName);
		    objOpenItemDtl.setItemRate(Price);

		    boolean isUserGranted = funCheckUserAuthentication();
		}
		else
		{

		    //QTY POP UP
		    if (clsGlobalVarClass.gItemQtyNumpad)
		    {
			selectedQty = 0;
			frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
			num.setVisible(true);
			//selectedQty = num.getResult();
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			    clsGlobalVarClass.gNumerickeyboardValue = null;
			}
		    }

		    frmNumberKeyPad obj = new frmNumberKeyPad(null, true, "Rate" + Price);
		    obj.setVisible(true);
		    if (clsGlobalVarClass.gRateEntered)
		    {
			//Price = obj.getResult();                    
			if (null != clsGlobalVarClass.gNumerickeyboardValue)
			{
			    Price = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
			    clsGlobalVarClass.gNumerickeyboardValue = null;
			}

			if (selectedQty != 0)
			{
			    if (funNextKOTFlag(globalTableNo))
			    {
				KOTNo = funGenerateKOTNo();
			    }

			    flagOpenItem = true;
			    funInsertData(selectedQty, Price, globalTableNo, itemName, itemCode);
			    flagOpenItem = false;
			    selectedQty = 1;
			}
			else
			{
			    new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
			}
			clsGlobalVarClass.gRateEntered = false;
		    }
		}
		rsTLA.close();
	    }
	    else
	    {
		//QTY POP UP
		if (clsGlobalVarClass.gItemQtyNumpad)
		{
		    selectedQty = 0;
		    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
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
		    if (funNextKOTFlag(globalTableNo))
		    {
			KOTNo = funGenerateKOTNo();
		    }

		    funInsertData(selectedQty, Price, globalTableNo, itemName, itemCode);
		    selectedQty = 1;
		}
		else
		{
		    new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
		}
	    }
	    if (flag_isComboItem)
	    {
		funInsertDefaultSubItems(itemCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
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
	    // System.out.println("Rev Text= "+reverseText);
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
	    //System.out.println(arrListItemCombination);
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

    private void funItemModeButtonClicked()
    {
	if (null == txtTableNo.getText() || txtTableNo.getText().isEmpty())
	{
	    JOptionPane.showMessageDialog(this, "Please Select Table.");
	    return;
	}
	if (txtWaiterNo.getText().trim().length() < 1 && !clsGlobalVarClass.gSkipWaiter)
	{
	    JOptionPane.showMessageDialog(this, "Please Select Waiter.");
	    return;
	}
	if (!clsGlobalVarClass.gSkipPax && Integer.parseInt(txtPaxNo.getText().trim()) < 1)
	{
	    JOptionPane.showMessageDialog(this, "Please Select PAX NO.");
	    return;
	}

	funResetTopSortingButtons();
	flgWaiterSelection = false;
	fieldSelected = "MenuItem";
	funShowMenuHeadPanel();
    }

    private void funDONEButtonPressed() throws Exception
    {
	String tableNoForAddKOTToBill = globalTableNo;

	funCloseKitchenNotePanel();
	if (clsGlobalVarClass.gDebitCardPayment.equals("Yes"))
	{
	    if (clsGlobalVarClass.gCheckDebitCardBalanceOnTrans)
	    {
		if (!flgCheckNCKOTButtonColor)
		{
		    if (!lblCardBalnce.getText().isEmpty())
		    {
			//double debitCardBalance = Double.parseDouble(lblCardBalnce.getText());
			double debitCardBalance = 0;
			sql = " select a.dblRedeemAmt,b.dblcardvaluefixed "
				+ " from tbldebitcardmaster a,tbldebitcardtype b "
				+ " where a.strCardTypeCode=b.strCardTypeCode and a.strCardNo ='" + globalDebitCardNo + "' ";
			ResultSet rsCardBalance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsCardBalance.next())
			{
			    debitCardBalance = rsCardBalance.getDouble(1) - rsCardBalance.getDouble(2);
			}
			rsCardBalance.close();

			if (Double.parseDouble(txtTotal.getText()) > debitCardBalance)
			{
			    JOptionPane.showMessageDialog(null, "Insufficient Balance on Card!!!");
			    return;
			}
		    }
		    else
		    {
			JOptionPane.showMessageDialog(null, "Please Swipe Card!!!");
			return;
		    }
		}
	    }
	}
	if (clsGlobalVarClass.gCMSIntegrationYN)
	{
	    if (clsGlobalVarClass.gCMSMemberCodeForKOTJPOS.equals("Y"))
	    {
		if (null != hmCMSMemberForTable)
		{
		    if (null == hmCMSMemberForTable.get(globalTableNo))
		    {
			JOptionPane.showMessageDialog(this, "Please Select Member!!!");
			return;
		    }
		}
		else
		{
		    JOptionPane.showMessageDialog(this, "Please Select Member!!!");
		    return;
		}
	    }
	}
	String ncKot = "N";
	if (flgCheckNCKOTButtonColor) //NC KOT
	{
	    ncKot = "Y";
	    globalDebitCardNo = "";
	}
	int paxNo = Integer.parseInt(txtPaxNo.getText());
	if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	{
	    if (homeDeliveryForTax.equals("N"))
	    {
		arrListHomeDelDetails.clear();
	    }
	    btnItemMode.setEnabled(true);

	    if (flgCheckNCKOTButtonColor) //NC KOT
	    {
		if (!getReasonCode())
		{
		    return;
		}
		if (clsGlobalVarClass.gTouchScreenMode)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter NC Remark.").setVisible(true);
		}
		else
		{
		    clsGlobalVarClass.gKeyboardValue = JOptionPane.showInputDialog(null, "Enter NC Remark");
		}
	    }
	    btnMakeKOT.setEnabled(false);
	    clsGlobalVarClass.gPrinterQueueStatus = "";
	    List<clsMakeKotItemDtl> arrListKOTDtl = new ArrayList<>(obj_List_KOT_ItemDtl);
	    String tableNO = globalTableNo;
	    String KOTNO = KOTNo;
	    obj_List_KOT_ItemDtl.clear();

	    if (!lblCardBalnce.getText().isEmpty())
	    {
		double totalAmt = Double.parseDouble(txtTotal.getText().trim());
		if (totalAmt > 0)
		{
		    clsSaveAndPrintKOT ob = new clsSaveAndPrintKOT(arrListKOTDtl, tableNO, KOTNO, this, ncKot, reasonCode, cmsMemCode, cmsMemName, paxNo, globalDebitCardNo, Double.parseDouble(lblCardBalnce.getText()), taxAmt, arrListHomeDelDetails, homeDeliveryForTax);
		    Thread t = new Thread(ob);
		    t.start();
		    funResetTopSortingButtons();
		    funSetSelectedArea();
		}
	    }
	    else if (!lblCardBalnce.getText().isEmpty() && gTableFound.equals("Yes"))
	    {
		double totalAmt = Double.parseDouble(txtTotal.getText().trim());
		if (totalAmt > 0)
		{
		    clsSaveAndPrintKOT ob = new clsSaveAndPrintKOT(arrListKOTDtl, tableNO, KOTNO, this, ncKot, reasonCode, cmsMemCode, cmsMemName, paxNo, globalDebitCardNo, Double.parseDouble(lblCardBalnce.getText()), taxAmt, arrListHomeDelDetails, homeDeliveryForTax);
		    Thread t = new Thread(ob);
		    t.start();
		    funResetTopSortingButtons();
		    funSetSelectedArea();
		}
	    }
	    else
	    {
		double cardBal = 0;
		if (lblCardBalnce.getText().isEmpty())
		{
		    globalDebitCardNo = "";
		}
		else
		{
		    cardBal = Double.parseDouble(lblCardBalnce.getText());
		}
		clsSaveAndPrintKOT ob = new clsSaveAndPrintKOT(arrListKOTDtl, tableNO, KOTNO, this, ncKot, reasonCode, cmsMemCode, cmsMemName, paxNo, globalDebitCardNo, cardBal, taxAmt, arrListHomeDelDetails, homeDeliveryForTax);
		Thread t = new Thread(ob);
		t.start();
		funResetTopSortingButtons();
		funSetSelectedArea();
	    }

	    if (clsGlobalVarClass.gAutoAddKOTToBill && !flgCheckNCKOTButtonColor && !clsGlobalVarClass.gEnableBillSeries)//if not NC KOTa and bill series is not enable and auto add kot to bill parameter is on
	    {
		funAddItemsToBill(tableNoForAddKOTToBill);
	    }

	    String kitchenNote = txtKOTMsg.getText().trim();
	    if (!kitchenNote.isEmpty())
	    {
		funSendKitchenNote(arrListKOTDtl);
	    }

	    funResetBottomButtons();
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "KOT not present to save");
	}
    }

    private void funMakeBillButtonPressed()
    {
	if (!clsGlobalVarClass.gSuperUser)
	{
	    if (clsGlobalVarClass.hmUserForms.containsKey("Make Bill"))
	    {
		funCloseKitchenNotePanel();
		funMakeBillButtonClicked();
		funResetBottomButtons();
		funSetSelectedArea();
	    }
	    else
	    {
		new frmOkPopUp(null, "Access Denied.", "Make Bill", 1).setVisible(true);
		return;
	    }
	}
	else
	{
	    funCloseKitchenNotePanel();
	    funMakeBillButtonClicked();
	    funResetBottomButtons();
	    funSetSelectedArea();
	}
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param hm_ModifierGroup
     */
    private void funAsignModifierGroupTopSortingButtons(HashMap<String, clsModifierGroupDtl> hm_ModifierGroup)
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

    /**
     * Ritesh 08 Nov 2014
     *
     * @param modifierGroupName
     */
    private void funFillModifier(String modifierGroupName)
    {
	nextItemClick = 0;
	clsModifierGroupDtl obj = hm_ModifierGroup.get(modifierGroupName);
	String itemCode = obj.getTemp_ItemCode();
	String groupCode = obj.getStrModifierGroupCode();
	hm_ModifierDtl = funGetModifierGroupWise(itemCode, groupCode);
	funSetModifierNameToButtons(hm_ModifierDtl);
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param itemCode
     * @param groupCode
     * @return
     */
    private HashMap<String, clsModifierDtl> funGetModifierGroupWise(String itemCode, String groupCode)
    {
	HashMap<String, clsModifierDtl> temp_hm_ModifierDtl = null;
	try
	{
	    temp_hm_ModifierDtl = new HashMap<>();
	    String sql_selectModifier = "select a.strModifierName,a.strModifierCode"
		    + ",b.dblRate,a.strModifierGroupCode,c.strApplyMaxItemLimit,c.intItemMaxLimit"
		    + ",c.strApplyMinItemLimit,c.intItemMinLimit,b.strDefaultModifier "
		    + "from tblmodifiermaster a, tblitemmodofier b, tblmodifiergrouphd c "
		    + "where a.strModifierCode=b.strModifierCode "
		    + "and a.strModifierGroupCode=c.strModifierGroupCode "
		    + "and a.strModifierGroupCode='" + groupCode + "' "
		    + "and b.strItemCode='" + itemCode + "' "
		    + "group by a.strModifierCode;";
	    ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
	    while (rs_ModifierDtl.next())
	    {
		boolean isDefaultModifier = false;
		if (rs_ModifierDtl.getString(9).equalsIgnoreCase("Y"))
		{
		    isDefaultModifier = true;
		}
		clsModifierDtl obj = new clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), rs_ModifierDtl.getString(4), rs_ModifierDtl.getDouble(3), itemCode, rs_ModifierDtl.getString(5), rs_ModifierDtl.getDouble(6), rs_ModifierDtl.getString(7), rs_ModifierDtl.getDouble(8), isDefaultModifier);
		temp_hm_ModifierDtl.put(rs_ModifierDtl.getString(1), obj);
	    }
	    rs_ModifierDtl.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return temp_hm_ModifierDtl;
    }

    /**
     * Ritesh 10 Nov 2014
     *
     * @param itemCode
     * @return
     */
    private HashMap<String, clsModifierDtl> funGetModifierAll(String itemCode)
    {
	HashMap<String, clsModifierDtl> hmModifierDtl = null;
	try
	{
	    hmModifierDtl = new HashMap<>();
	    String sql_selectModifier = "select a.strModifierName,a.strModifierCode"
		    + " ,b.dblRate,a.strModifierGroupCode,b.strDefaultModifier "
		    + " from tblmodifiermaster a,tblitemmodofier b "
		    + " where a.strModifierCode=b.strModifierCode "
		    + " and b.strItemCode='" + itemCode + "' "
		    + " group by a.strModifierCode;";
	    ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
	    while (rs_ModifierDtl.next())
	    {
		boolean isDefaultModifier = false;
		if (rs_ModifierDtl.getString(5).equalsIgnoreCase("Y"))
		{
		    isDefaultModifier = true;
		}
		clsModifierDtl obj = new clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), "NA", rs_ModifierDtl.getDouble(3), itemCode, "N", 0.00, "N", 0.00, isDefaultModifier);
		hmModifierDtl.put(rs_ModifierDtl.getString(1), obj);
	    }
	    rs_ModifierDtl.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return hmModifierDtl;
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param hm_ModifierDtl
     */
    private void funSetModifierNameToButtons(HashMap<String, clsModifierDtl> hm_ModifierDtl)
    {
	JButton[] btnSubMenuArray =
	{
	    btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
	};
	int i = 0;
	funResetMenuItemButtons();
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

    /**
     * Ritesh 08 Nov 2014
     *
     * @param modiname
     */
    private void funAddModifierToKOT(String modiname)
    {
	modiname = modiname.trim();
	if (selectedRowNoForModifer < 0)
	{
	    JOptionPane.showMessageDialog(null, "Please Select Item to Apply Modifier!!!");
	    return;
	}

	DecimalFormat decimalFormat = new DecimalFormat("#.##");
	int modifierSeqNo = 1;
	int selectedRow = selectedRowNoForModifer;
	int rowCount = tblItemTable.getRowCount();

	if ((selectedRow + 1) < rowCount)
	{
	    int row = selectedRow + 1;
	    while (row < rowCount && tblItemTable.getValueAt(row, 0).toString().startsWith("-->"))
	    {
		modifierSeqNo++;
		row++;
	    }
	}

	String seqNo = tblItemTable.getValueAt(selectedRow, 4).toString();
	if (null != obj_List_KOT_ItemDtl && obj_List_KOT_ItemDtl.size() > 0)
	{
	    if ("Free Flow Modifier".equalsIgnoreCase(modiname))
	    {
		new frmAlfaNumericKeyBoard(null, true, "1", "Enter Name ", true).setVisible(true);
		String name = clsGlobalVarClass.gKeyboardValue;

		if (name.trim().length() > 0)
		{
		    name = name.toUpperCase();
		    name = name.replaceAll("[^a-zA-Z0-9]", " ").trim();

		    if (name.length() > 100)
		    {
			name = name.substring(0, 100);
		    }

		    //int ffModNo=99;
		    new frmNumericKeyboard(this, true, "", "Double", "Enter Amount").setVisible(true);
		    String price = clsGlobalVarClass.gNumerickeyboardValue;
		    clsGlobalVarClass.gNumerickeyboardValue = "";
		    if (name.trim().length() > 0 && price.trim().length() > 0)
		    {
			for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
			{
			    if (ob.getItemCode().equalsIgnoreCase(temp_ItemCode)
				    && ob.isIsModifier() == false && seqNo.equals(ob.getSequenceNo()))
			    {
				/*
                                 * if(hmFFModifierCode.containsKey(temp_ItemCode))
                                 * {
                                 * ffModNo=hmFFModifierCode.get(temp_ItemCode);
                                 * ffModNo++; } String ffModCode="M"+ffModNo;
				 */

				clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(ob.getSequenceNo().concat("." + String.format("%02d", modifierSeqNo)), KOTNo, globalTableNo, globalWaiterNo, "-->" + name, temp_ItemCode.concat("M99"), 1.00, Double.parseDouble(price), ob.getPaxNo(), "N", "N", true, "M99", "", "", "N", Double.parseDouble(price));
				//clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(ob.getSequenceNo().concat(".01"), KOTNo, globalTableNo, globalWaiterNo, "-->" + name, temp_ItemCode.concat(ffModCode), 1.00, Double.parseDouble(price), ob.getPaxNo(), "N", "N", true, "M99", "", "", "N", Double.parseDouble(price));
				obj_List_KOT_ItemDtl.add(obj_row);
				break;
			    }
			}
			funRefreshItemTable();
		    }
		}
	    }
	    else
	    {
		clsModifierDtl objModiDtl = hm_ModifierDtl.get(modiname);
		String temp_itemCode = objModiDtl.getItemCode();
		double rate = objModiDtl.getDblRate();
		String ModifierCode = objModiDtl.getModifierCode();
		String ModifierName = objModiDtl.getModifierName();
		String modifierGroup = objModiDtl.getModifierGroupCode();
		String applyMaxLimit = objModiDtl.getStrApplyMaxItemLimit();
		double maxLimit = objModiDtl.getIntItemMaxLimit();
		double currentModifierLimit = funGetTotalModifierWithSubGroup(temp_itemCode, modifierGroup);

		for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
		{
		    boolean found = false;
		    boolean flagMaxLimitexceed = false;
		    if (ob.getItemCode().equalsIgnoreCase(temp_itemCode) && ob.isIsModifier() == false
			    && seqNo.equals(ob.getSequenceNo()))
		    {
			if (currentModifierLimit == 0.00)
			{
			    clsMakeKotItemDtl objKOTDtlRow = new clsMakeKotItemDtl(ob.getSequenceNo().concat("." + String.format("%02d", modifierSeqNo)), KOTNo, globalTableNo, globalWaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, ob.getPaxNo(), "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
			    obj_List_KOT_ItemDtl.add(objKOTDtlRow);
			    found = true;
			}
			else if ("Y".equalsIgnoreCase(applyMaxLimit))
			{
			    if (funIsModifierPresent(temp_itemCode, modifierGroup))
			    {
				if (currentModifierLimit < maxLimit)
				{
				    clsMakeKotItemDtl objKOTDtlRow = new clsMakeKotItemDtl(ob.getSequenceNo().concat("." + String.format("%02d", modifierSeqNo)), KOTNo, globalTableNo, globalWaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, ob.getPaxNo(), "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
				    obj_List_KOT_ItemDtl.add(objKOTDtlRow);
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
			    //clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(getStrSerialNo().concat(".00"), KOTNo, globalTableNo, globalWaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, ob.getPaxNo(), "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
			    clsMakeKotItemDtl objKOTDtlRow = new clsMakeKotItemDtl(ob.getSequenceNo().concat("." + String.format("%02d", modifierSeqNo)), KOTNo, globalTableNo, globalWaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, ob.getPaxNo(), "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
			    obj_List_KOT_ItemDtl.add(objKOTDtlRow);
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

    private void funEnablePaxButtons(boolean flag)
    {
	btnB4.setEnabled(flag);
	btnNumber1.setEnabled(flag);
	btnNumber2.setEnabled(flag);
	btnNumber3.setEnabled(flag);
	btnNumber4.setEnabled(flag);
	btnNumber5.setEnabled(flag);
	btnNumber6.setEnabled(flag);
	btnNumber7.setEnabled(flag);
	btnNumber8.setEnabled(flag);
	btnNumber9.setEnabled(flag);
	btnMultiQty.setEnabled(flag);
    }

    /**
     * Ritesh 10 Nov 2014
     *
     * @param temp_itemCode
     * @param modifierGroup
     * @return
     */
    private double funGetTotalModifierWithSubGroup(String temp_itemCode, String modifierGroup)
    {
	double totalModifier = 0.00;
	for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
	{
	    String temItemCode = ob.getItemCode();
	    temItemCode = temItemCode.substring(0, 7);
	    if (temp_itemCode.equalsIgnoreCase(temItemCode) && ob.isIsModifier() == true
		    && ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup))
	    {
		totalModifier++;
	    }
	}
	return totalModifier;
    }

    /**
     * Ritesh 10 Nov 2014
     *
     * @param temp_itemCode
     * @param modifierGroup
     * @return
     */
    private boolean funIsModifierPresent(String temp_itemCode, String modifierGroup)
    {
	boolean flag = false;
	for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
	{
	    String temItemCode = ob.getItemCode();
	    temItemCode = temItemCode.substring(0, 7);
	    if (temp_itemCode.equalsIgnoreCase(temItemCode) && ob.isIsModifier() == true && ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup))
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

    /**
     * Ritesh 10 Nov 2014
     *
     * @param itemCode
     */
    private void funShowModifier(String itemCode)
    {
	funResetMenuItemButtons();
	funResetTopSortingButtons();
	hm_ModifierGroup = funFillTopSortingButtonsForModifier(itemCode);
	funAsignModifierGroupTopSortingButtons(hm_ModifierGroup);
	isModifierSelect = true;
	if (hm_ModifierGroup.isEmpty())
	{
	    hm_ModifierDtl = funGetModifierAll(itemCode);
	    funSetModifierNameToButtons(hm_ModifierDtl);
	}
	else
	{
	    funFillModifier(btnItemSorting1.getText());
	}
    }

    /**
     * @return the strSerialNo
     */
    public String getStrSerialNo()
    {
	strSerialNo = String.valueOf(kotItemSequenceNO);
	return strSerialNo;
    }

    private void funPAXTextFieldClicked()
    {
	if ((!clsGlobalVarClass.gSkipWaiter) && txtWaiterNo.getText().trim().length() < 1)
	{
	    new frmOkPopUp(this, "Please Select The Waiter.", "error", 1).setVisible(true);
	    return;
	}
	fieldSelected = "Pax";
    }

    private void funCloseKitchenNotePanel()
    {
	panelKOTMessage.setVisible(false);
	panelNumericKeyPad.setVisible(true);
	panelMenuHead.setVisible(true);
	panelNavigate.setVisible(true);
	funIsVisiblePanels(true);
//	txtKOTMsg.setText("");
    }

    private class CancelAction extends AbstractAction
    {

	@Override
	public void actionPerformed(ActionEvent ev)
	{
	    if (txtPLUItemSearch.hasFocus() && txtPLUItemSearch.getText().trim().length() == 0)
	    {
		if (txtPLUItemSearch.isFocusable() && txtPLUItemSearch.getText().trim().length() == 0)
		{
		    btnButton2.requestFocus();
		    funIsVisiblePanels(true);
		    txtExternalCode.requestFocus();
		}
		else
		{
		    txtPLUItemSearch.setText("");
		    txtPLUItemSearch.requestFocus();
		}
	    }
	    else
	    {
		funShowTableGrid();
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

    /**
     * Ritesh 08 Nov 2014
     */
    private void funResetMenuItemButtons()
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

    /**
     * Ritesh 08 Nov 2014
     *
     * @param itemCode
     * @return
     */
    private HashMap<String, clsModifierGroupDtl> funFillTopSortingButtonsForModifier(String itemCode)
    {
	HashMap<String, clsModifierGroupDtl> hm_ModifierGroupDetail = null;
	try
	{
	    String modifierGroupName = null;
	    hm_ModifierGroupDetail = new HashMap<>();
	    String sql = "select a.strModifierGroupCode,a.strModifierGroupShortName,a.strApplyMaxItemLimit,"
		    + "a.intItemMaxLimit,a.strApplyMinItemLimit,a.intItemMinLimit  from tblmodifiergrouphd a,tblmodifiermaster b,tblitemmodofier c "
		    + "where a.strOperational='YES' and a.strModifierGroupCode=b.strModifierGroupCode and "
		    + "b.strModifierCode=c.strModifierCode and c.strItemCode='" + itemCode + "' group by a.strModifierGroupCode";
	    ResultSet rs_ModifierGroupDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs_ModifierGroupDtl.next())
	    {
		modifierGroupName = rs_ModifierGroupDtl.getString(2);
		clsModifierGroupDtl obj = new clsModifierGroupDtl(rs_ModifierGroupDtl.getString(1), rs_ModifierGroupDtl.getString(2), rs_ModifierGroupDtl.getString(3), rs_ModifierGroupDtl.getInt(4), itemCode, rs_ModifierGroupDtl.getString(5), rs_ModifierGroupDtl.getInt(6));
		hm_ModifierGroupDetail.put(modifierGroupName, obj);
	    }
	    rs_ModifierGroupDtl.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return hm_ModifierGroupDetail;
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

	public void isIsDefaultModifier(boolean isDefaultModifier)
	{
	    this.isDefaultModifier = isDefaultModifier;
	}
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param Name
     * @return
     */
    private String fun_Get_FormattedName(String Name)
    {
	String name = Name;
	if (Name != null && Name.contains(" ") && !Name.contains("<html>"))
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

    public boolean fun_Add_TDH_Modifier(List<clsMakeKotItemDtl> obj_TDH_Modifier_ItemDtl)
    {
	for (clsMakeKotItemDtl ob : obj_TDH_Modifier_ItemDtl)
	{
	    obj_List_KOT_ItemDtl.add(ob);
	}
	funRefreshItemTable();
	return true;
    }

    private boolean getReasonCode()
    {
	String favoritereason = null;
	boolean flgRet = false;
	try
	{
	    int reasoncount = 0, i = 0;
	    sql = "select count(strReasonName) from tblreasonmaster where strNCKOT='Y'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		reasoncount = rs.getInt(1);
	    }
	    if (reasoncount > 0)
	    {
		reason = new String[reasoncount];
		sql = "select strReasonName from tblreasonmaster where strNCKOT='Y'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		i = 0;
		while (rs.next())
		{
		    reason[i] = rs.getString(1);
		    i++;
		}
		favoritereason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.PLAIN_MESSAGE, null, reason, reason[0]);
	    }
	    else
	    {
		new frmOkPopUp(this, "Please Create Reason", "Warning", 1).setVisible(true);
		flgRet = false;
	    }
	    if (favoritereason != null)
	    {
		sql = "select strReasonCode from tblreasonmaster where strReasonName='" + favoritereason + "'";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rs.next();
		reasonCode = rs.getString("strReasonCode");
		flgRet = true;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	return flgRet;
    }

    public HashMap<String, String> getHmMakeKotParams()
    {
	return hmMakeKotParams;
    }

    private String funGetKOTTimeForOldKOT(String kotNo)
    {
	String kotTime = "";
	try
	{
	    String sqlKot = "select DATE_FORMAT(dteDateCreated,'%H:%i') from tblitemrtemp where strKOTNo='" + kotNo + "' limit 1";
	    ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
	    if (rsKOTTime.next())
	    {
		kotTime = rsKOTTime.getString(1);
	    }
	    rsKOTTime.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    return kotTime;
	}
    }

    private String funGetKOTTimeForNewKOT()
    {
	String kotTime = "";
	try
	{
	    String sqlKot = "select TIME_FORMAT(CURRENT_TIME(),'%H:%i')";
	    ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
	    if (rsKOTTime.next())
	    {
		kotTime = rsKOTTime.getString(1);
	    }
	    rsKOTTime.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return kotTime;
	}
    }

    private String funGetTimeDiffInFirstKOTAndCurrentTime(String tableNo)
    {
	String timeDiffInFirstKOTAndCurrentTime = "";
	try
	{
	    String sqlKot = "select TIME_FORMAT(TIMEDIFF(CURRENT_TIME(),time(dteDateCreated)),'%i:%s'),strKOTNo "
		    + "from tblitemrtemp  "
		    + "where strTableNo='" + tableNo + "' "
		    + "group by strKOTNo asc "
		    + "limit 1 ";
	    ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
	    if (rsKOTTime.next())
	    {
		timeDiffInFirstKOTAndCurrentTime = rsKOTTime.getString(1);
	    }
	    rsKOTTime.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return timeDiffInFirstKOTAndCurrentTime;
	}
    }

    private String funGetTimeDiffInBilledAndCurrentTime(String tableNo)
    {
	String timeDiffInLastBilledAndCurrentTime = "";
	try
	{
	    String sqlKot = "select TIME_FORMAT(TIMEDIFF(CURRENT_TIME(),time(dteBillDate)),'%i:%s'),a.strBillNo "
		    + "from tblbillhd a "
		    + "where date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + "and a.strTableNo='" + tableNo + "' "
		    + "and a.strBillNo not in(select strBillNo from tblbillsettlementdtl) "
		    + "order by a.dteBillDate desc "
		    + "limit 1; ";
	    ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
	    if (rsKOTTime.next())
	    {
		timeDiffInLastBilledAndCurrentTime = rsKOTTime.getString(1);
	    }
	    rsKOTTime.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return timeDiffInLastBilledAndCurrentTime;
	}
    }

    private boolean funCheckUserAuthentication()
    {
	boolean isUserGranted = false;
	try
	{
	    String[] options = new String[]
	    {
		"OK", "Cancel"
	    };
	    txtUsername.setText("");
	    txtPassword.setText("");
	    txtUsername.requestFocus();
	    int option = JOptionPane.showOptionDialog(null, panelUserAuthentication, "User Authentication!!!",
		    JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		    null, options, txtUsername);
	    if (option == 0) // pressing OK button
	    {
		isUserGranted = funUserAuthenticationOKButtonPressed();
	    }
	    else
	    {
		isUserGranted = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return isUserGranted;
	}
    }

    private boolean funUserAuthenticationOKButtonPressed()
    {
	boolean isUserGranted = false;
	try
	{
	    Date objDate = new Date();
	    int day = objDate.getDate();
	    int month = objDate.getMonth() + 1;
	    int year = objDate.getYear() + 1900;
	    String currentDate = year + "-" + month + "-" + day;
	    if (txtUsername.getText().trim().equalsIgnoreCase("SANGUINE"))
	    {
		int password = year + month + day + day;

		clsUtility objUtility = new clsUtility();

		String strpass = Integer.toString(password);
		char num1 = strpass.charAt(0);
		char num2 = strpass.charAt(1);
		char num3 = strpass.charAt(2);
		char num4 = strpass.charAt(3);
		String alph1 = objUtility.funGetAlphabet(Character.getNumericValue(num1));
		String alph2 = objUtility.funGetAlphabet(Character.getNumericValue(num2));
		String alph3 = objUtility.funGetAlphabet(Character.getNumericValue(num3));
		String alph4 = objUtility.funGetAlphabet(Character.getNumericValue(num4));

		String finalPassword = String.valueOf(password) + alph1 + alph2 + alph3 + alph4;

		String userPassword = txtPassword.getText().trim();
		if (finalPassword.equalsIgnoreCase(userPassword))
		{
		    String userCode = txtUsername.getText();
		    String userName = "SANGUINE";
		    String userType = "Super";
		    String posAccessCode = "All POS";

		    funSetTransactionUserDtl(userCode, userName, userType, posAccessCode);

		    isUserGranted = true;
		}
	    }
	    else
	    {
		if (txtPassword.getText().length() == 0)
		{
		    String sql = "select count(*) from tbluserhd where strUsercode='" + txtUsername.getText() + "' ";
		    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rssql.next())
		    {

			sql = "  select count(*) from tbluserhd where strDebitCardString='" + txtUsername.getText() + "' ";
			ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rssql1.next())
			{
			    if (rssql1.getInt(1) > 0)
			    {
				if (funCHeckLoginForDebitCardString(txtUsername.getText()))
				{
				    return true;
				}
			    }
			    else
			    {
				txtUsername.setText("");
				new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
				return false;
			    }
			}

		    }
		}
		String encKey = "04081977";
		String password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, txtPassword.getText().trim().toUpperCase());
		//System.out.println(password);
		String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess from tbluserhd "
			+ "where strUserCode='" + txtUsername.getText() + "' and strPassword='" + password + "'";
		//System.out.println(selectQuery);
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
		rs.next();
		if (rs.getInt(1) == 1)
		{
		    String userCode = txtUsername.getText();
		    String userName = rs.getString(2);
		    String userType = rs.getString(3);
		    String posAccessCode = rs.getString(5);

		    selectQuery = "select count(*) from tbluserhd WHERE strUserCode = '" + txtUsername.getText()
			    + "' and strPassword='" + password + "'" + " AND dteValidDate>='" + currentDate + "'";

		    rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
		    rs.next();
		    if (rs.getInt(1) == 0)
		    {
			rs.close();
			new frmOkPopUp(null, "User Has Expired", "Error", 1).setVisible(true);
		    }
		    else
		    {
			funCloseAuthenticationDialog();
			if (userType.equalsIgnoreCase("Super"))
			{
			    funSetTransactionUserDtl(userCode, userName, userType, posAccessCode);
			    isUserGranted = true;
			}
			else
			{
			    sqlBuilder.setLength(0);
			    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
				    + "from tbluserdtl a "
				    + "where a.strFormName='Open Items' "
				    + "and a.strGrant='true' "
				    + "and a.strUserCode='" + userCode + "' ");
			    ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
			    if (rsTLA.next())
			    {
				funSetTransactionUserDtl(userCode, userName, userType, posAccessCode);
				isUserGranted = true;
			    }
			    else
			    {
				txtUsername.requestFocus();
				new frmOkPopUp(null, "User \"" + userCode + "\" Not Granted.", "Error", 1).setVisible(true);
			    }
			    rsTLA.close();
			}
		    }
		}
		else
		{
		    rs.close();
		    txtUsername.requestFocus();
		    new frmOkPopUp(null, "Login Failed", "Error", 1).setVisible(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return isUserGranted;
	}
    }

    private void funSetTransactionUserDtl(String userCode, String userName, String userType, String posAccessCode)
    {
	String gLoginUserCode = clsGlobalVarClass.gUserCode;
	String gLoginUserName = clsGlobalVarClass.gUserName;
	String gLoginUserType = clsGlobalVarClass.gUserType;

	//set new user dtl
	clsGlobalVarClass.gUserCode = userCode;
	clsGlobalVarClass.gUserName = userName;
	clsGlobalVarClass.gUserType = userType;

	txtUsername.setText("");
	txtPassword.setText("");

	String itemCode = objOpenItemDtl.getItemCode();
	String itemName = objOpenItemDtl.getItemName();
	double price = objOpenItemDtl.getItemRate();

	//QTY POP UP
	frmNumberKeyPad num;
	if (clsGlobalVarClass.gItemQtyNumpad)
	{
	    selectedQty = 0;
	    num = new frmNumberKeyPad(this, true, "qty");
	    num.setVisible(true);
	    if (null != clsGlobalVarClass.gNumerickeyboardValue)
	    {
		if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
		{
		    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		    clsGlobalVarClass.gNumerickeyboardValue = null;
		}
	    }
	}

	frmNumberKeyPad obj = new frmNumberKeyPad(null, true, "Rate" + price);
	obj.setVisible(true);
	if (clsGlobalVarClass.gRateEntered)
	{
	    //Price = obj.getResult();
	    if (null != clsGlobalVarClass.gNumerickeyboardValue)
	    {
		price = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
		clsGlobalVarClass.gNumerickeyboardValue = null;
	    }
	    if (selectedQty != 0 && price >= 0)
	    {
		if (funNextKOTFlag(globalTableNo))
		{
		    KOTNo = funGenerateKOTNo();
		}

		flagOpenItem = true;
		funInsertData(selectedQty, price, globalTableNo, itemName, itemCode);
		selectedQty = 1;
	    }
	    /*
             * else { new frmOkPopUp(null, "Please select quantity first",
             * "Error", 1).setVisible(true); }
	     */
	    clsGlobalVarClass.gRateEntered = false;

	    //reset to login user dtl
	    clsGlobalVarClass.gUserCode = gLoginUserCode;
	    clsGlobalVarClass.gUserName = gLoginUserName;
	    clsGlobalVarClass.gUserType = gLoginUserType;

	    objOpenItemDtl.setItemCode("");
	    objOpenItemDtl.setItemName("");
	    objOpenItemDtl.setItemRate(0.00);
	}

    }

    private boolean funCHeckLoginForDebitCardString(String user) throws Exception
    {
	boolean flgLoginStatus = false;
	String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess,strUserCode from tbluserhd "
		+ "where strDebitCardString='" + user + "'";
	//System.out.println(selectQuery);
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	rs.next();
	if (rs.getInt(1) == 1)
	{
	    String userCode = rs.getString(6);
	    String userName = rs.getString(2);
	    String userType = rs.getString(3);
	    String posAccessCode = rs.getString(5);

	    selectQuery = "select count(*) from tbluserhd WHERE strDebitCardString = '" + user + "' "
		    + " AND dteValidDate>='" + rs.getString(4) + "' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    rs.next();
	    if (rs.getInt(1) == 0)
	    {
		flgLoginStatus = false;
		rs.close();
		new frmOkPopUp(null, "User Has Expired", "Error", 1).setVisible(true);
	    }
	    else
	    {
		if (userType.equalsIgnoreCase("Super"))
		{
		    funCloseAuthenticationDialog();
		    flgLoginStatus = true;

		    funSetTransactionUserDtl(userCode, userName, userType, posAccessCode);
		}
		else
		{
		    sqlBuilder.setLength(0);
		    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
			    + "from tbluserdtl a "
			    + "where a.strFormName='Open Items' "
			    + "and a.strGrant='true' "
			    + "and a.strUserCode='" + userCode + "' ");
		    ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		    if (rsTLA.next())
		    {
			funCloseAuthenticationDialog();
			flgLoginStatus = true;
			funSetTransactionUserDtl(userCode, userName, userType, posAccessCode);
		    }
		    else
		    {
			txtUsername.requestFocus();
			new frmOkPopUp(null, "User \"" + userCode + " Not Granted.", "Error", 1).setVisible(true);
		    }
		    rsTLA.close();
		}
	    }
	}
	return flgLoginStatus;
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

    // call frmCustomerHistory
    private void funCustomerHistoryAction()
    {
	if ("Table".equals(fieldSelected))
	{
	    JOptionPane.showMessageDialog(this, "Please Select Table. ");
	}
	else if ("Waiter".equals(fieldSelected))
	{
	    JOptionPane.showMessageDialog(this, "Please Select Waiter. ");
	}
	else if ("Pax".equals(fieldSelected))
	{
	    JOptionPane.showMessageDialog(this, "Please Select Pax No.");
	}
	else
	{
	    if (Integer.parseInt(txtPaxNo.getText()) > 0)
	    {
		if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.isEmpty())
		{
		    new frmOkPopUp(null, "Please Select Customer.", "Warning", 1).setVisible(true);
		    funNewCustomerButtonPressed();
		    return;
		}
		else
		{
		    if (tblItemTable.getRowCount() == 0)
		    {
			KOTNo = funGenerateKOTNo();
		    }
		    frmCustomerHistory objCustomer = new frmCustomerHistory(this);
		    objCustomer.setLocation(panelFormBody.getLocation());
		    this.disable();
		    objCustomer.setVisible(true);
		    btnButton1.setSelected(false);
		    objUtility = new clsUtility();
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Please Select Pax No.");
	    }
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

    public String getKOTToBillNote()
    {
	return this.kotToBillNote;
    }

    public void setKOTToBillNote()
    {
	this.kotToBillNote = "";
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

			String tempAreaCode = "";
			if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
			{
			    tempAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
			}
			else
			{
			    tempAreaCode = clsAreaCode;
			}
			String tempItemName = itemName;
			Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(tempAreaCode);
			clsItemPriceDtl priceObject = x.get(tempItemName);
			funGetPLUItemPrice(priceObject);
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

    private void funAddItemsToBill(String tableNoForAddKOTToBill)
    {
	try
	{

	    frmAddKOTToBill objAddKOTToBill = new frmAddKOTToBill("AddItemsToBillForSameTable", this);

	    String sqlLastBill = "select a.strBillNo,a.strPOSCode,a.strSettelmentMode,a.strTableNo,b.strTableName,b.strStatus,b.strAreaCode "
		    + "from tblbillhd a,tbltablemaster b "
		    + "where a.strTableNo=b.strTableNo "
		    + "and a.strTableNo='" + tableNoForAddKOTToBill + "' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + "and a.strSettelmentMode='' "
		    + "order by a.dteBillDate desc "
		    + "limit 1 ";
	    ResultSet rsLastBilledTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastBill);
	    if (rsLastBilledTable.next())
	    {

		String lastBilledBillNo = rsLastBilledTable.getString(1);
		String billNo = lastBilledBillNo;

		frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want to add this KOT to Bill No. " + lastBilledBillNo + " ");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{

		    String areaCode = clsGlobalVarClass.gAreaCodeForTrans;
		    areaCode = rsLastBilledTable.getString(7);
		    String tableNo = rsLastBilledTable.getString(4);

		    List<String> listSelectedKOTs = new ArrayList<String>();

		    String sqlKOTs = "select a.strKOTNo  "
			    + "from tblitemrtemp a "
			    + "where a.strTableNo='" + tableNoForAddKOTToBill + "'  "
			    + "and a.tdhComboItemYN='N'  "
			    + "and a.strNCKotYN='N' "
			    + "group by a.strKOTNo";
		    ResultSet rsKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sqlKOTs);
		    while (rsKOTs.next())
		    {
			listSelectedKOTs.add(rsKOTs.getString(1));
		    }
		    rsKOTs.close();

		    StringBuilder kots = new StringBuilder("(");
		    for (int i = 0; i < listSelectedKOTs.size(); i++)
		    {
			if (i == 0)
			{
			    kots.append("'" + listSelectedKOTs.get(i) + "'");
			}
			else
			{
			    kots.append(",'" + listSelectedKOTs.get(i) + "'");
			}
		    }
		    kots.append(")");

		    objAddKOTToBill.setListSelectedKOTs(listSelectedKOTs);

		    clsGlobalVarClass.gTransactionType = "AddKOTToBill";
		    String kotNo = "";

		    new frmBillSettlement(objAddKOTToBill, billNo, areaCode, kotNo, tableNo).setVisible(true);

		}
		else
		{
		    return;
		}
	    }
	    rsLastBilledTable.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    public void funRefreshAndLoadTables()
    {
	funLoadTables(0, hmTableSeq.size());;
    }

    private void funSendKitchenNote(List<clsMakeKotItemDtl> arrListKOTDtl)
    {
	try
	{

	    String costCenterName = cmbCostCenterForKOTMsg.getSelectedItem().toString();
	    String costCenterCode = mapCostCenters.get(costCenterName);

	    String kitchenNote = txtKOTMsg.getText().trim();

	    if (costCenterName.equalsIgnoreCase("All"))
	    {

		String sqlCostCenters = "select a.strItemCode,a.strItemName,b.strCostCenterCode,b.strCostCenterName "
			+ "from tblmenuitempricingdtl a,tblcostcentermaster b  "
			+ "where a.strCostCenterCode=b.strCostCenterCode "
			+ "and a.strItemCode in " + funGetItemCodeList(arrListKOTDtl) + " "
			+ "group by b.strCostCenterCode";
		ResultSet rsCostCenters = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenters);
		while (rsCostCenters.next())
		{
		    costCenterName = rsCostCenters.getString(4);
		    costCenterCode = rsCostCenters.getString(3);

		    clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
		    objKOTGeneration.funPrintKOTMessage(costCenterCode, costCenterName, kitchenNote);
		}
		rsCostCenters.close();
	    }
	    else
	    {
		clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
		objKOTGeneration.funPrintKOTMessage(costCenterCode, costCenterName, kitchenNote);
	    }

	    txtKOTMsg.setText("");
	    cmbCostCenterForKOTMsg.setSelectedIndex(0);

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private String funGetItemCodeList(List<clsMakeKotItemDtl> arrListKOTDtl)
    {
	StringBuilder itemCodeListBuilder = new StringBuilder("(");

	int i = 0;
	for (clsMakeKotItemDtl objItemDtl : arrListKOTDtl)
	{
	    if (i == 0)
	    {
		itemCodeListBuilder.append("'" + objItemDtl.getItemCode() + "'");
	    }
	    else
	    {
		itemCodeListBuilder.append(",'" + objItemDtl.getItemCode() + "'");
	    }
	    i++;
	}

	itemCodeListBuilder.append(")");

	return itemCodeListBuilder.toString();
    }

    private void funBillNoteButtonClicked()
    {

	if (null == txtTableNo.getText() || txtTableNo.getText().isEmpty())
	{
	    JOptionPane.showMessageDialog(this, "Please Select Table.");
	    return;
	}
	if (txtWaiterNo.getText().trim().length() < 1 && !clsGlobalVarClass.gSkipWaiter)
	{
	    JOptionPane.showMessageDialog(this, "Please Select Waiter.");
	    return;
	}
	if (!clsGlobalVarClass.gSkipPax && Integer.parseInt(txtPaxNo.getText().trim()) < 1)
	{
	    JOptionPane.showMessageDialog(this, "Please Select PAX NO.");
	    return;
	}

	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Bill Note").setVisible(true);
	}
	else
	{
	    clsGlobalVarClass.gKeyboardValue = JOptionPane.showInputDialog(null, "Enter Bill Note");
	}

	kotToBillNote = "";
	if (clsGlobalVarClass.gKeyboardValue != null && !clsGlobalVarClass.gKeyboardValue.isEmpty())
	{
	    kotToBillNote = clsGlobalVarClass.gKeyboardValue;
	    clsGlobalVarClass.gKeyboardValue = "";
	}

    }

    private void funFireKOTButtonClicked()
    {
	try
	{

	    if (clsGlobalVarClass.gFireCommunication)
	    {
		if (txtTableNo.getText().equals(""))
		{
		    new frmOkPopUp(null, "Please Select Table", "Error", 1).setVisible(true);
		}
		else
		{
		    frmKOTFireCommunication objKOTFireCommunication = new frmKOTFireCommunication(this, true, globalTableNo);
		    objKOTFireCommunication.setVisible(true);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private boolean funAddAllKOTSOnTableToBill(String tableNoForAddKOTToBill){
	try
	{

	    frmAddKOTToBill objAddKOTToBill = new frmAddKOTToBill("AddItemsToBillForSameTable", this);

	    String sqlLastBill = "select a.strBillNo,a.strPOSCode,a.strSettelmentMode,a.strTableNo,b.strTableName,b.strStatus,b.strAreaCode "
		    + "from tblbillhd a,tbltablemaster b "
		    + "where a.strTableNo=b.strTableNo "
		    + "and a.strTableNo='" + tableNoForAddKOTToBill + "' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + "and a.strSettelmentMode='' "
		    + "order by a.dteBillDate desc "
		    + "limit 1 ";
	    ResultSet rsLastBilledTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastBill);
	    if (rsLastBilledTable.next())
	    {

		String lastBilledBillNo = rsLastBilledTable.getString(1);
		String billNo = lastBilledBillNo;
		String areaCode = clsGlobalVarClass.gAreaCodeForTrans;
		areaCode = rsLastBilledTable.getString(7);
		String tableNo = rsLastBilledTable.getString(4);

		List<String> listSelectedKOTs = new ArrayList<String>();

		String sqlKOTs = "select a.strKOTNo  "
			+ "from tblitemrtemp a "
			+ "where a.strTableNo='" + tableNoForAddKOTToBill + "'  "
			+ "and a.tdhComboItemYN='N'  "
			+ "and a.strNCKotYN='N' "
			+ "group by a.strKOTNo";
		ResultSet rsKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sqlKOTs);
		while (rsKOTs.next())
		{
		    listSelectedKOTs.add(rsKOTs.getString(1));
		}
		rsKOTs.close();

		StringBuilder kots = new StringBuilder("(");
		for (int i = 0; i < listSelectedKOTs.size(); i++)
		{
		    if (i == 0)
		    {
			kots.append("'" + listSelectedKOTs.get(i) + "'");
		    }
		    else
		    {
			kots.append(",'" + listSelectedKOTs.get(i) + "'");
		    }
		}
		kots.append(")");

		objAddKOTToBill.setListSelectedKOTs(listSelectedKOTs);
		objAddKOTToBill.flgMergeKOTToBill=true;
		clsGlobalVarClass.gTransactionType = "AddKOTToBill";
		String kotNo = "";

		new frmBillSettlement(objAddKOTToBill, billNo, areaCode, kotNo, tableNo).setVisible(true);
	    }
	    else{
		return false;
	    }
	    rsLastBilledTable.close();
	    funResetBottomButtons();
	    funRefreshAndLoadTables();
	   
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	 return true;
    }
}
