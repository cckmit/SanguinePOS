package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsBillHd;
import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsBillModifierDtl;
import com.POSGlobal.controller.clsBillSeriesBillDtl;
import com.POSGlobal.controller.clsBillSettlementDtl;
import com.POSGlobal.controller.clsBillTaxDtl;
import com.POSGlobal.controller.clsCRMInterface;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsGuestRoomDtl;
import com.POSGlobal.controller.clsBenowIntegration;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsItemDtlForTax;
import com.POSGlobal.controller.clsRewards;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsTextFieldOnlyNumber;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSGlobal.view.frmUserAuthenticationPopUp;
import com.POSPrinting.clsKOTGeneration;
import com.POSTransaction.controller.clsBillDiscountDtl;
import com.POSTransaction.controller.clsBillSettlementUtility;
import com.POSTransaction.controller.clsCalculateBillDiscount;
import com.POSTransaction.controller.clsBuyPromotionItemDtl;
import com.POSTransaction.controller.clsCalculateBillPromotions;
import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsGetPromotionItemDtl;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import com.POSTransaction.controller.clsPlayZoneItems;
import com.POSTransaction.controller.clsPromotionItems;
import static com.POSTransaction.view.frmRegisterInOutPlayZone.txtRemarks;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class frmBillSettlement extends javax.swing.JFrame
{

    static Logger log = Logger.getLogger(frmBillSettlement.class.getName());
    private String voucherNo, amountBox, discountRemarks;
    private String textValue1, textValue2;
    boolean printstatus, settleMode;
    private String discountType;
    private BigDecimal btnVal, tempVal, billAmount;
    private Point PointCash, PointCheque;
    boolean dyn1, flgEnterBtnPressed, flgUpdateBillTableForDiscount;
    private String settlementName;
    private int paxNo, intBillSeriesPaxNo;
    private String settleName, settleType, strButtonClicked = "Print", billPrintOnSettlement;
    private String tableNo, waiterNo;
    private boolean flgMakeKot, flgMakeBill, flgUnsettledBills, flagBillForItems;
    private double dblTotalTaxAmt, currencyRate;
    private JButton[] settlementArray = new JButton[4];
    private boolean flgGiftVoucherOK;
    private static String debitCardNo;
    private String selectedReasonCode;
    public static String customerCodeForCredit;
    private Vector vModifyReasonCode, vModifyReasonName, vComplReasonCode, vComplReasonName, vReprintReasonCode, vReprintReasonName, vReasonCodeForDiscount, vReasonNameForDiscount;
    private ArrayList<String> listItemCode;
    private ArrayList<String> listSubGroupCode;
    private ArrayList<String> listSubGroupName;
    private ArrayList<String> listGroupName;
    private ArrayList<String> listGroupCode;
    private HashMap<String, String> hmItemList;
    private ButtonGroup radioButtonGroup;
    private double cmsMemberBalance = 0;
    private String billType, billTypeForTax;
    private int disableNext;
    private String custCode, delPersonCode;
    private Map<String, clsBillItemDtl> hmBillItemDtl = new HashMap<String, clsBillItemDtl>();
    private List<clsDirectBillerItemDtl> objListDirectBillerItemDtl = null, objListItemDtlTemp = null;//Used for Direct Biller ONLY
    private int noOfSettlementMode = clsSettelementOptions.listSettelmentOptions.size();
    private HashMap<String, clsSettelementOptions> hmSettlemetnOptions = new HashMap<>();
    private double dblDiscountAmt = 0.00;
    private double dblDiscountPer = 0.00;
    private double dblSettlementAmount = 0.00;
    private double _paidAmount = 0.00, tipAmount = 0;
    private double _subTotal = 0.00;
    private double _netAmount = 0.00;
    private double _grandTotal = 0.00;
    private double _balanceAmount = 0.00;
    private double _refundAmount = 0.00;
    private String _giftVoucherCode = "", custMobileNoForCRM;
    private String _giftVoucherSeriesCode = "", advOrderBookingNo = "", couponCode = "";
    private int _settlementNavigate;
    private double _deliveryCharge = 0.00, _loyalityPoints = 0.00;
    private String dtPOSDate, homeDelivery, areaCode, operationTypeForTax, takeAway, callingFormName = "", cmsMemberName;
    //private ArrayList<ArrayList<Object>> arrListTaxCal;
    private List<clsTaxCalculationDtls> arrListTaxCal;
    private boolean flagAddKOTstoBill = false;
    private Map<String, clsPromotionItems> hmPromoItem;
    private Map<String, Double> hmAddKOTItems;
    private frmMakeKOT kotObj = null, objMakeKOT = null;
    private frmMakeBill makeBillObj = null;
    private frmDirectBiller objDirectBiller = null;
    private panelShowBills objPannelShowBills = null;
    private panelShowKOTs objPannelShowKOTs = null;
    private frmAddKOTToBill objAddKOTToBill;
    private clsCustomerDataModelForSQY obj;
    private List<String> listBillFromKOT = null;
    //private Map<String, clsPromotionDtl> hmBuyPromoItemDtl;
    private String takeAwayRemarks, custAddType;
    clsUtility objUtility = new clsUtility();
    private double cmsMemberCreditLimit;
    private String cmsStopCredit;
    Map<String, clsBillDiscountDtl> mapBillDiscDtl = new HashMap<String, clsBillDiscountDtl>();
    private HashMap<String, clsBillItemDtl> mapPromoItemDisc;
    private boolean isDirectSettleFromMakeBill = false;
    private Map<String, List<clsBillItemDtl>> hmBillSeriesItemList;
    private List<clsBillSeriesBillDtl> listBillSeriesBillDtl;
    private String settlementCode = "";//use while calculating tax for settlement
    private Map<String, clsSettelementOptions> hmJioMoneySettleDtl = new HashMap<>();
    private clsUtility2 objUtility2 = new clsUtility2();
    private double _grandTotalRoundOffBy = 0.00;
    private Map<String, clsBillDtl> hmComplimentaryBillItemDtl = new HashMap<String, clsBillDtl>();
    private Map<String, Double> mapBeforeComplimentory = new HashMap<>();
    private String rewardId = "";
    private String discountCode = "";
    private String QRStringForBenow = "";
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();
    private clsCalculateBillDiscount objCalculateBillDisc;
    private clsCalculateBillPromotions objCalculateBillPromotions;
    private List<clsMakeKotItemDtl> listOfKOTWiseItemDtl;
    private frmBillForItems objBillForItems;
    private clsBillSettlementUtility objBillSettlementUtility;
    private boolean isRemoveSCTax = false;
    private String checkOutPlayZoneRegisterCode = "", onlineOrderNo = "";
    private boolean isCheckOutPlayZone = false;

    public frmBillSettlement()
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	//funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);
	funShowDiscountPannel(false);
	lblManualBillNo.setVisible(false);;
	txtManualBillNo.setVisible(false);

	try
	{
	    tableNo = "";
	    callingFormName = "";
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(false);
	    flgUnsettledBills = true;
	    panelSettlement.setVisible(false);

	    OrderPanel.setVisible(false);

	    objPannelShowBills = new panelShowBills(this);
	    objPannelShowBills.setLocation(panelSettlement.getLocation());
	    objPannelShowBills.setPreferredSize(new Dimension(800, 600));
	    objPannelShowBills.setVisible(true);
	    panelLayout.add(objPannelShowBills);
	    objPannelShowBills.repaint();
	    panelLayout.show();
	    funSetProperty();
	    billTypeForTax = "Unsettled";
	    voucherNo = "";
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    funSetTableNameVisible(false);
	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();

	    if (clsGlobalVarClass.gRemoveSCTaxCode.trim().length() > 0 && clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill"))
	    {
		btnRemoveSCTax.setVisible(true);
		btnRemoveSCTax.setLocation(btnReprint.getLocation());
	    }
	    
	    if(clsGlobalVarClass.gReprintOnSettleBill)
	    {
		btnReprint.setVisible(true);
	    }	
	    else
	    {
		btnReprint.setVisible(false);
	    }	
	    
	    

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    /**
     * contructor for add KOT to Bill
     *
     * @param objAddKOTToBill1
     * @param billNo
     * @param area
     * @param kotNo
     * @param tableNo
     */
    public frmBillSettlement(frmAddKOTToBill objAddKOTToBill1, String billNo, String area, String kotNo, String tableNo)
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);

	flagAddKOTstoBill = true;
	lblVoucherNo.setText(billNo);
	voucherNo = billNo;

	this.tableNo = tableNo;

	try
	{
	    objAddKOTToBill = objAddKOTToBill1;
	    areaCode = area;
	    callingFormName = "";
	    billTypeForTax = "Make KOT";

	    operationTypeForTax = "DineIn";
	    String sql = "select strOperationType from tblbillhd where strBillNo='" + billNo + "';";
	    ResultSet rsBillInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillInfo.next())
	    {
		if (rsBillInfo.getString(1).equals("HomeDelivery"))
		{
		    operationTypeForTax = "HomeDelivery";
		}
		else if (rsBillInfo.getString(1).equals("TakeAway"))
		{
		    operationTypeForTax = "TakeAway";
		}
	    }

	    rsBillInfo.close();
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(false);
	    funSetProperty();
	    panelSettlement.setVisible(true);
	    funDisableSettelementButtons();
	    funFillGridForAddKOTToBill("Unsettled Bills", billNo);
	    lblTipAmount.setVisible(false);
	    txtTip.setVisible(false);
	    billType = "Make KOT";
	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	    setAlwaysOnTop(true);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    public frmBillSettlement(String callingFormName, frmMakeKOT obj)
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);

	try
	{
	    objMakeKOT = obj;
	    tableNo = "";
	    this.callingFormName = callingFormName;
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(false);
	    flgUnsettledBills = true;
	    OrderPanel.setVisible(false);
	    objPannelShowBills = new panelShowBills(this);
	    objPannelShowBills.setLocation(panelSettlement.getLocation());
	    objPannelShowBills.setPreferredSize(new Dimension(800, 600));
	    panelLayout.add(objPannelShowBills);
	    objPannelShowBills.repaint();
	    panelLayout.show();
	    funSetProperty();
	    billTypeForTax = "Unsettled";
	    panelSettlement.setVisible(false);
	    voucherNo = "";
	    custAddType = "Home";
	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    //constructor call from Direct Biller Option from
    public frmBillSettlement(frmDirectBiller ob, String billTransType)
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	funShowDiscountPannel(true);
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);

	try
	{
	    //setAlwaysOnTop(true);
	    takeAwayRemarks = "";
	    custAddType = "Home";
	    homeDelivery = "N";
	    debitCardNo = "";
	    tableNo = "";

	    HashMap hmDirectBillerParams = ob.getHmDirectBillerParams();
	    if (null != hmDirectBillerParams)
	    {
		if (null != hmDirectBillerParams.get("TakeAwayRemarks"))
		{
		    takeAwayRemarks = hmDirectBillerParams.get("TakeAwayRemarks").toString();
		}
	    }
	    if (billTransType.equalsIgnoreCase("Home Delivery"))
	    {
		homeDelivery = "Y";
		delPersonCode = clsGlobalVarClass.gDeliveryBoyCode;
		if (null != hmDirectBillerParams)
		{
		    if (null != hmDirectBillerParams.get("CustAddType"))
		    {
			custAddType = hmDirectBillerParams.get("CustAddType").toString();
		    }
		}
	    }

	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(true);
	    txtDiscountPer.setText("0.00");
	    txtDiscountAmt.setText("0.00");
	    this.objDirectBiller = ob;
	    objListDirectBillerItemDtl = new ArrayList<>(objDirectBiller.getObj_List_ItemDtl());
	    objListItemDtlTemp = new ArrayList<clsDirectBillerItemDtl>();

	    for (clsDirectBillerItemDtl objListDBItems : objListDirectBillerItemDtl)
	    {
		clsDirectBillerItemDtl objTempListDBItems = new clsDirectBillerItemDtl(objListDBItems.getItemName(), objListDBItems.getItemCode(), objListDBItems.getQty(), objListDBItems.getAmt(), objListDBItems.isIsModifier(), objListDBItems.getModifierCode(), objListDBItems.getTdhComboItemYN(), objListDBItems.getTdh_ComboItemCode(), objListDBItems.getRate(), objListDBItems.getPromoCode(), objListDBItems.getSeqNo(), 0.00);
		objListItemDtlTemp.add(objTempListDBItems);
	    }

	    areaCode = "";
	    areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    if (billTransType.equalsIgnoreCase("Home Delivery"))
	    {
		operationTypeForTax = "HomeDelivery";
		areaCode = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	    }
	    else if (clsGlobalVarClass.gTakeAway.equals("Yes"))
	    {
		operationTypeForTax = "TakeAway";
		areaCode = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	    }
	    else
	    {
		operationTypeForTax = "DineIn";
	    }

	    if (billTransType.startsWith("CheckOutPlayZone"))
	    {
		isCheckOutPlayZone = true;
		checkOutPlayZoneRegisterCode = billTransType.split("!")[1];
	    }
	    onlineOrderNo = "";
	    if (billTransType.startsWith("WERAOnlineFood"))
	    {
		onlineOrderNo = billTransType.split("!")[1];
	    }
	    if (null != hmDirectBillerParams)
	    {
		if (null != hmDirectBillerParams.get("BillNote"))
		{
		    onlineOrderNo = hmDirectBillerParams.get("BillNote").toString();
		}
	    }

	    funSetProperty();
	    funHomeDelivery();
	    funFillItemDtlGridForDirectBiller();
	    funFillGroupSubGroupList(listItemCode);
	    funSetDate();
	    lblTipAmount.setVisible(false);
	    txtTip.setVisible(false);
	    voucherNo = "";
	    billType = "Direct Biller";
	    billTypeForTax = "Direct Biller";
	    advOrderBookingNo = "";

	    if (clsGlobalVarClass.gSelectCustomerCodeFromCardSwipe)
	    {
		String sql = "select strCardNo from tbldebitcardmaster "
			+ " where strCardString='" + clsGlobalVarClass.gDebitCardNo + "' ";
		ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsDebitCardNo.next())
		{
		    debitCardNo = rsDebitCardNo.getString(1);
		}
		rsDebitCardNo.close();
	    }

	    clsSettelementOptions objListSettle = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
	    if (!objListSettle.getStrSettelmentType().equals("Debit Card"))
	    {
		procSettlementBtnClick(objListSettle);
	    }
	    funCalculator();

	    if (clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
	    {
		obj = ob.getObjData();
		if (obj.getRedeemed_amt() > 0)
		{
		    discountType = "Amount";
		    amountBox = "discount";
		    txtDiscountAmt.setText(String.valueOf(obj.getRedeemed_amt()));
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    if (null != clsGlobalVarClass.gCustomerCode)
	    {
		String sqlCustWiseDisc = "select a.strCustomerCode,a.strCustomerName,b.dblDiscPer "
			+ " from tblcustomermaster a,tblcustomertypemaster b "
			+ " where a.strCustomerType=b.strCustTypeCode "
			+ "and a.strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "'";
		ResultSet rsCustWiseDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustWiseDisc);
		if (rsCustWiseDiscount.next())
		{
		    txtDiscountPer.setText(rsCustWiseDiscount.getString(3));
		}
		rsCustWiseDiscount.close();
		if (Double.parseDouble(txtDiscountPer.getText().trim()) > 0)
		{
		    discountType = "Percent";
		    amountBox = "discount";
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		custCode = objDirectBiller.getCmsMemberCode();
	    }

	    if (!clsGlobalVarClass.gEnableSettleBtnForDirectBiller)
	    {
		btnSettle.setVisible(false);
		btnPrint.setVisible(true);
	    }
	    else
	    {
		btnSettle.setVisible(true);
		btnPrint.setVisible(false);
	    }
	    if (clsGlobalVarClass.gEnablePrintAndSettleBtnForDB)
	    {
		btnSettle.setVisible(true);
		btnPrint.setVisible(true);
	    }

	    funSetTableNameVisible(false);

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
	    {
		if (objDirectBiller.getCustomerRewards() != null)
		{
		    objCalculateBillDisc.funSetCustomerRewards(objDirectBiller.getCustomerRewards());
		}
	    }
	    if (billTransType.startsWith("WERAOnlineFood"))
	    {
		discountType = "Amount";
		txtDiscountAmt.setText(String.valueOf(objDirectBiller.getDblDiscountAmt()));
		txtDeliveryCharges.setText(String.valueOf(objDirectBiller.getHomeDelCharges()));
		_deliveryCharge = objDirectBiller.getHomeDelCharges();
	    }

	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-2", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    //Constructor call from Make Bill Option
    public frmBillSettlement(frmMakeBill ob, String tbNo)
    {
	//super(parent,true);
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);
	PanelBenowSettlement.setVisible(false);

	try
	{
	    custAddType = "Home";
	    billTypeForTax = "Make Bill";
	    billType = "Make Bill";
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(true);
	    txtDiscountPer.setText("0.00");
	    txtDiscountAmt.setText("0.00");
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    voucherNo = "";
	    flgMakeBill = true;
	    tableNo = tbNo;
	    lblTableNo.setText(tbNo);
	    areaCode = "";

	    String sqlAreaCode = "select strAreaCode from tbltablemaster where strTableNo='" + tableNo + "'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlAreaCode);
	    if (rsAreaCode.next())
	    {
		areaCode = rsAreaCode.getString(1);
	    }
	    rsAreaCode.close();
	    operationTypeForTax = "DineIn";

	    String customerCodeForBill = clsGlobalVarClass.gCustomerCode;
	    String sql = "select strHomeDelivery,strCustomerCode,strCustomerName,strDelBoyCode,strTakeAwayYesNo "
		    + " from tblitemrtemp where strTableNo='" + tableNo + "'";
	    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillType.next())
	    {
		customerCodeForBill = rsBillType.getString(2);
		if (rsBillType.getString(1).equals("Yes"))
		{
		    operationTypeForTax = "HomeDelivery";
		    homeDelivery = "Y";
		    delPersonCode = rsBillType.getString(4);
		}
		else if (rsBillType.getString(5).equals("Yes"))
		{
		    operationTypeForTax = "TakeAway";
		}
	    }
	    rsBillType.close();

	    if (!customerCodeForBill.trim().isEmpty())
	    {
		String sqlCustWiseDisc = "select a.strCustomerCode,a.strCustomerName,b.dblDiscPer "
			+ " from tblcustomermaster a,tblcustomertypemaster b "
			+ " where a.strCustomerType=b.strCustTypeCode and a.strCustomerCode='" + customerCodeForBill + "'";
		ResultSet rsCustWiseDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustWiseDisc);
		if (rsCustWiseDiscount.next())
		{
		    txtDiscountPer.setText(rsCustWiseDiscount.getString(3));
		}
		rsCustWiseDiscount.close();
		if (Double.parseDouble(txtDiscountPer.getText().trim()) > 0)
		{
		    discountType = "Percent";
		    amountBox = "discount";
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    makeBillObj = ob;
	    funSetProperty();
	    funDisableSettelementButtons();

	    funFillGridForMakeKOTTransaction(tbNo, false, "Make KOT", "");

	    funFillGroupSubGroupList(listItemCode);
	    funFillGroupSubGroupList(listItemCode);

	    funCalculator();
	    if (clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
	    {
		obj = ob.getObjData();
		if (obj.getRedeemed_amt() > 0)
		{
		    discountType = "Amount";
		    amountBox = "discount";
		    txtDiscountAmt.setText(String.valueOf(obj.getRedeemed_amt()));
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    txtTip.setVisible(false);
	    lblTipAmount.setVisible(false);
	    funHomeDelivery();
	    billType = "Make Bill";

	    funUpdateTableStatusInItemRTemp(tableNo, "BillingInProgress");

	    btnPrint.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-4", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    //Constructor from Make KOT form
    public frmBillSettlement(frmMakeKOT ob, String tbNo, String tableName, List<String> arrListCustomerDetails, String homeDelivery)
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);
	PanelBenowSettlement.setVisible(false);

	try
	{
	    tableNo = "";
	    billType = "Make KOT";
	    this.homeDelivery = homeDelivery;
	    custAddType = "Home";
	    operationTypeForTax = "DineIn";
	    HashMap hmMakeKotParams = ob.getHmMakeKotParams();
	    

	    if (homeDelivery.equals("Y"))
	    {
		operationTypeForTax = "HomeDelivery";
		homeDelivery = "Y";
		delPersonCode = arrListCustomerDetails.get(4);
	    }

	    funAddButtonToGroup();
	    billTypeForTax = "Make KOT";
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(true);
	    txtDiscountPer.setText("0.00");
	    txtDiscountAmt.setText("0.00");
	    flgMakeKot = true;
	    voucherNo = "";
	    tableNo = tbNo;
	    lblTableNo.setText(tableName);
	    areaCode = "";

	    String sql_AreaCode = "select strAreaCode from tbltablemaster where strTableNo='" + tableNo + "'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql_AreaCode);
	    if (rsAreaCode.next())
	    {
		areaCode = rsAreaCode.getString(1);
	    }
	    rsAreaCode.close();

	    String sqlTakeAway = "select strTakeAwayYesNo from tblitemrtemp where strTableNo='" + tableNo + "'";
	    ResultSet rsTakeAway = clsGlobalVarClass.dbMysql.executeResultSet(sqlTakeAway);
	    if (rsTakeAway.next())
	    {
		if (rsTakeAway.getString(1).equals("Yes"))
		{
		    operationTypeForTax = "TakeAway";
		}
	    }
	    rsTakeAway.close();
	    kotObj = ob;
	    funHomeDelivery();
	    funSetProperty();
	    funDisableSettelementButtons();

	    funFillGridForMakeKOTTransaction(tbNo, true, "Make KOT", "");

	    funFillGroupSubGroupList(listItemCode);
	    funCalculator();
	    if (clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
	    {
		obj = ob.getObjData();
		if (obj.getRedeemed_amt() > 0)
		{
		    discountType = "Amount";
		    amountBox = "discount";
		    txtDiscountAmt.setText(String.valueOf(obj.getRedeemed_amt()));
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }
	    // for Customer Master Discount
	    if (!clsGlobalVarClass.gCustomerCode.trim().isEmpty())
	    {
		String sqlCustWiseDisc = "select a.strCustomerCode,a.strCustomerName,b.dblDiscPer "
			+ "from tblcustomermaster a,tblcustomertypemaster b "
			+ "where a.strCustomerType=b.strCustTypeCode and a.strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "'";
		ResultSet rsCustWiseDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustWiseDisc);
		if (rsCustWiseDiscount.next())
		{
		    txtDiscountPer.setText(rsCustWiseDiscount.getString(3));
		    rdbAll.setSelected(true);
		}
		rsCustWiseDiscount.close();
		if (Double.parseDouble(txtDiscountPer.getText().trim()) > 0)
		{
		    discountType = "Percent";
		    amountBox = "discount";
		    funGetDiscountReasons();
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}

		if (null != hmMakeKotParams)
		{
		    if (null != hmMakeKotParams.get("CustAddType"))
		    {
			custAddType = hmMakeKotParams.get("CustAddType").toString();
		    }
		}
	    }
	    funFillGroupSubGroupList(listItemCode);
	    lblTipAmount.setVisible(false);
	    txtTip.setVisible(false);
	    billType = "Make KOT";
	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	    btnJioMoneyCheckStatus.setVisible(false);

	    funUpdateTableStatusInItemRTemp(tableNo, "BillingInProgress");

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface"))
	    {
		if (kotObj.getCustomerRewards() != null)
		{
		    objCalculateBillDisc.funSetCustomerRewards(kotObj.getCustomerRewards());
		}
	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-5", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

// For Bill From KOTs Option.     
    public frmBillSettlement(int no)
    {
	initComponents();
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);

	try
	{
	    if (clsGlobalVarClass.gClientCode == "024.001")
	    {
		tableNo = "TB0000011";
	    }
	    else
	    {
		tableNo = "TB0000001";
	    }
	    areaCode = "";
	    String sql_AreaCode = "select strAreaCode from tbltablemaster where strTableNo='" + tableNo + "'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql_AreaCode);
	    if (rsAreaCode.next())
	    {
		areaCode = rsAreaCode.getString(1);
	    }
	    rsAreaCode.close();

	    clsGlobalVarClass.gAdvOrderNoForBilling = null;
	    callingFormName = "";
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(false);
	    flgUnsettledBills = true;
	    objPannelShowKOTs = new panelShowKOTs(this);
	    objPannelShowKOTs.setLocation(panelSettlement.getLocation());
	    objPannelShowKOTs.setLocation(panelSettlement.getLocation());
	    objPannelShowKOTs.setSize(490, 600);
	    objPannelShowKOTs.setVisible(true);
	    panelLayout.add(objPannelShowKOTs);
	    objPannelShowKOTs.repaint();
	    panelLayout.show();
	    funSetProperty();
	    billType = "Bill From KOTs";
	    billTypeForTax = "Bill From KOTs";
	    panelSettlement.setVisible(false);
	    voucherNo = "";
	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    public frmBillSettlement(String formName)
    {
	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	try
	{
	    System.out.println(formName);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

    }

    //Constructor call from Bill For Items Option
    public frmBillSettlement(frmBillForItems objBillForItems, String forTable, List<clsMakeKotItemDtl> listOfKOTWiseItemDtl, List<clsMakeKotItemDtl> listOfItemsForToBeBilled)
    {
	//super(parent,true);
	initComponents();

	objCalculateBillDisc = new clsCalculateBillDiscount(this);
	objCalculateBillPromotions = new clsCalculateBillPromotions(this);
	objBillSettlementUtility = new clsBillSettlementUtility(this);

	this.setLocationRelativeTo(null);
	btnReprint.setVisible(false);
	objCalculateBillDisc.funShowComplimentaryItemsButtonYN();
	objCalculateBillDisc.funShowDiscountPanel(clsGlobalVarClass.gEnableMasterDiscount);
	PanelBenowSettlement.setVisible(false);
	btnBack.setText("CLOSE");

	try
	{
	    custAddType = "Home";
	    billTypeForTax = "Bill For Items";
	    billType = "Bill For Items";
	    clsGlobalVarClass.gTransactionType = "Bill For Items";

	    txtDiscountPer.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDiscountAmt.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    txtDeliveryCharges.setDocument(new clsTextFieldOnlyNumber(6, 3).new JNumberFieldFilter());
	    funVisibleDeliveryCharges(true);
	    txtDiscountPer.setText("0.00");
	    txtDiscountAmt.setText("0.00");
	    funAddButtonToGroup();
	    funSetDefaultRadioBtnForDiscount();
	    voucherNo = "";
	    flagBillForItems = true;
	    tableNo = forTable;
	    lblTableNo.setText(forTable);
	    areaCode = "";

	    String sqlAreaCode = "select strAreaCode from tbltablemaster where strTableNo='" + tableNo + "'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlAreaCode);
	    if (rsAreaCode.next())
	    {
		areaCode = rsAreaCode.getString(1);
	    }
	    rsAreaCode.close();
	    operationTypeForTax = "DineIn";

	    String customerCodeForBill = clsGlobalVarClass.gCustomerCode;
	    String sql = "select strHomeDelivery,strCustomerCode,strCustomerName,strDelBoyCode,strTakeAwayYesNo "
		    + " from tblitemrtemp where strTableNo='" + tableNo + "'";
	    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillType.next())
	    {
		customerCodeForBill = rsBillType.getString(2);
		if (rsBillType.getString(1).equals("Yes"))
		{
		    operationTypeForTax = "HomeDelivery";
		    homeDelivery = "Y";
		    delPersonCode = rsBillType.getString(4);
		}
		else if (rsBillType.getString(5).equals("Yes"))
		{
		    operationTypeForTax = "TakeAway";
		}
	    }
	    rsBillType.close();

	    if (!customerCodeForBill.trim().isEmpty())
	    {
		String sqlCustWiseDisc = "select a.strCustomerCode,a.strCustomerName,b.dblDiscPer "
			+ " from tblcustomermaster a,tblcustomertypemaster b "
			+ " where a.strCustomerType=b.strCustTypeCode and a.strCustomerCode='" + customerCodeForBill + "'";
		ResultSet rsCustWiseDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustWiseDisc);
		if (rsCustWiseDiscount.next())
		{
		    txtDiscountPer.setText(rsCustWiseDiscount.getString(3));
		}
		rsCustWiseDiscount.close();
		if (Double.parseDouble(txtDiscountPer.getText().trim()) > 0)
		{
		    discountType = "Percent";
		    amountBox = "discount";
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    this.objBillForItems = objBillForItems;
	    this.listOfKOTWiseItemDtl = listOfKOTWiseItemDtl;

	    funSetProperty();
	    billType = "Bill For Items";
	    funDisableSettelementButtons();

	    funFillGridForBillForItemsTransaction(forTable, listOfKOTWiseItemDtl, listOfItemsForToBeBilled);

	    funCalculator();
	    if (clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
	    {
		//obj = objBillForItems.getObjData();
		if (obj.getRedeemed_amt() > 0)
		{
		    discountType = "Amount";
		    amountBox = "discount";
		    txtDiscountAmt.setText(String.valueOf(obj.getRedeemed_amt()));
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}
	    }

	    txtTip.setVisible(false);
	    lblTipAmount.setVisible(false);
	    funHomeDelivery();

	    funUpdateTableStatusInItemRTemp(tableNo, "BillingInProgress");

	    btnPrint.requestFocus();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-4", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funUpdateTableStatusInItemRTemp(String tblNo, String tableStatus) throws Exception
    {
	String sql = "update tblitemrtemp set strTableStatus='" + tableStatus + "' where strTableNo='" + tblNo + "'";
	clsGlobalVarClass.dbMysql.execute(sql);
    }

    //Create Temp folder
    private void funCreateTempFolder()
    {
	String filePath = System.getProperty("user.dir");
	File textReport = new File(filePath + "/Temp");
	if (!textReport.exists())
	{
	    textReport.mkdirs();
	}
    }

    private void funShowPromoItems() throws Exception
    {
	funCreateTempFolder();
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + "/Temp/PromoItems.txt");
	PrintWriter pw = new PrintWriter(file);

	pw.println();
	pw.println();
	pw.println();
	pw.println();
	pw.print(objUtility.funPrintTextWithAlignment(" " + clsGlobalVarClass.gPOSName, 40, "Center"));
	pw.println();

	if (!voucherNo.isEmpty())
	{
	    pw.print(objUtility.funPrintTextWithAlignment(" Bill No   : ", 15, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(" " + voucherNo, 20, "Left"));
	    pw.println();
	    for (int cnt = 0; cnt < 40; cnt++)
	    {
		pw.print("-");
	    }
	    pw.println();
	}

	pw.print(objUtility.funPrintTextWithAlignment(" Item Name", 25, "Left"));
	pw.print(objUtility.funPrintTextWithAlignment(" Qty", 15, "Left"));
	pw.println();
	for (int cnt = 0; cnt < 40; cnt++)
	{
	    pw.print("-");
	}
	pw.println();

	for (Map.Entry<String, clsPromotionItems> entry : hmPromoItem.entrySet())
	{
	    String sql = "select strItemName from tblitemmaster where strItemCode='" + entry.getKey() + "' ";
	    ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsPromoItems.next())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(" " + rsPromoItems.getString(1), 25, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(" " + entry.getValue().getFreeItemQty(), 15, "Left"));
		pw.println();
	    }
	    rsPromoItems.close();
	}

	pw.println();
	pw.println();
	pw.println();
	pw.println();

	pw.flush();
	pw.close();

	String data = "";
	FileReader fread = new FileReader(file);
	FileInputStream fis = new FileInputStream(file);
	BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	String line = "";
	while ((line = KOTIn.readLine()) != null)
	{
	    data = data + line + "\n";
	}
	String name = "Promo Items";

	new com.POSGlobal.view.frmShowTextFile(data, name, file, "").setVisible(true);
	fread.close();
    }

    private int funHomeDelivery()
    {
	if (!clsGlobalVarClass.gEditHDCharges)
	{
	    txtDeliveryCharges.setEditable(false);
	}
	txtDeliveryCharges.setText(String.valueOf(clsGlobalVarClass.gDeliveryCharges));
	lblDelBoyName.setText(clsGlobalVarClass.gDeliveryBoyName);
	_deliveryCharge = Double.parseDouble(txtDeliveryCharges.getText().trim());
	return 0;
    }

    private void funDisableSettelementButtons()
    {
	btnPrevSettlementMode.setVisible(false);
	btnSettlement1.setVisible(false);
	btnSettlement2.setVisible(false);
	btnSettlement3.setVisible(false);
	btnSettlement4.setVisible(false);
	btnNextSettlementMode.setVisible(false);
	btnSettle.setVisible(false);
    }

    private void funResetItemGrid()
    {
	try
	{
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(87);
	    tblItemTable.setShowHorizontalLines(true);
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-6", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funSetProperty()
    {
	try
	{

	    //btnPrint.setEnabled(false);
	    cmsMemberCreditLimit = 0;
	    cmsStopCredit = "N";
	    btnBack.setMnemonic('b');
	    btnPrint.setMnemonic('p');
	    btnCalEnter.setMnemonic('e');
	    btnSettle.setMnemonic('s');
	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("PMAM"))
	    {
		btnGetOffer.setVisible(true);
	    }
	    else
	    {
		btnGetOffer.setVisible(false);
	    }
	    debitCardNo = "";
	    discountRemarks = "";
	    couponCode = "";
	    clsGlobalVarClass.gNumerickeyboardValue = "";
	    billType = "";
	    flgGiftVoucherOK = false;
	    customerCodeForCredit = "";
	    //flgComplementarySettle = false;
	    settleType = "Cash";
	    settlementCode = "S01";//
	    selectedReasonCode = "";
	    billPrintOnSettlement = "";
	    //complementaryRemarks = "";
	    settlementName = "others";
	    flgUpdateBillTableForDiscount = false;
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    panelCustomer.setVisible(false);
	    PanelRemaks.setVisible(false);
	    PanelCard.setVisible(false);
	    txtPaidAmt.setFocusable(true);
	    panelMode.setVisible(false);
	    scrTax.setVisible(false);
	    discountType = "Percent";
	    funResetItemGrid();
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);

	    dtPOSDate = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
	    txtAmount.setFocusable(false);
	    txtAmount.setEditable(false);
	    txtPaidAmt.setFocusable(true);
	    scrSettle.setVisible(false);
	    PanelCheque.setVisible(false);
	    panelAmt.setVisible(false);
	    PanelCard.setVisible(false);
	    PanelCoupen.setVisible(false);
	    PanelGiftVoucher.setVisible(false);
	    panelRoomSettlement.setVisible(false);
	    PanelJioMoneySettlement.setVisible(false);
	    PointCash = panelAmt.getLocation();
	    PointCheque = PanelCheque.getLocation();
	    amountBox = "PaidAmount";
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    textValue1 = "";
	    textValue2 = "";
	    objUtility = new clsUtility();

	    funGetComplementryReasons();
	    funGetReprintReasons();
	    funGetDiscountReasons();
	    funGetModifyBillReasons();

	    //create the button array of settel mode
	    settlementArray[0] = btnSettlement1;
	    settlementArray[1] = btnSettlement2;
	    settlementArray[2] = btnSettlement3;
	    settlementArray[3] = btnSettlement4;

	    //funSetTableNameVisible(false);
	    if (clsGlobalVarClass.gManualBillNo.equals("Y"))
	    {
		txtManualBillNo.setVisible(true);
		lblManualBillNo.setVisible(true);
	    }
	    else
	    {
		txtManualBillNo.setVisible(false);
		lblManualBillNo.setVisible(false);
	    }
	    fun_FillSettlementBtns();
	    _settlementNavigate = 0;

	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Discount On Bill"))
		{
		    panelDiscount.setVisible(false);
		}
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-8", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funGetDiscountReasons()
    {
	try
	{
	    vReasonCodeForDiscount = new Vector();
	    vReasonNameForDiscount = new Vector();
	    String sql_Reason = "select strReasonCode,strReasonName from tblreasonmaster where strDiscount='Y'";
	    ResultSet rsDiscReason = clsGlobalVarClass.dbMysql.executeResultSet(sql_Reason);
	    while (rsDiscReason.next())
	    {
		vReasonCodeForDiscount.add(rsDiscReason.getString(1));
		vReasonNameForDiscount.add(rsDiscReason.getString(2));
	    }
	    rsDiscReason.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-9", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funGetComplementryReasons()
    {
	try
	{
	    vComplReasonCode = new Vector();
	    vComplReasonName = new Vector();
	    String sqlCmplementReason = "select strReasonCode,strReasonName from tblreasonmaster "
		    + "where strComplementary='Y'";
	    ResultSet rsComplReason = clsGlobalVarClass.dbMysql.executeResultSet(sqlCmplementReason);
	    while (rsComplReason.next())
	    {
		vComplReasonCode.add(rsComplReason.getString(1));
		vComplReasonName.add(rsComplReason.getString(2));
	    }
	    rsComplReason.close();
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-10", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funGetReprintReasons()
    {
	try
	{
	    vReprintReasonCode = new Vector();
	    vReprintReasonName = new Vector();
	    String sqlCmplementReason = "select strReasonCode,strReasonName from tblreasonmaster "
		    + "where strReprint='Y'";
	    ResultSet rsReprintReason = clsGlobalVarClass.dbMysql.executeResultSet(sqlCmplementReason);
	    while (rsReprintReason.next())
	    {
		vReprintReasonCode.add(rsReprintReason.getString(1));
		vReprintReasonName.add(rsReprintReason.getString(2));
	    }
	    rsReprintReason.close();
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-10", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funGetModifyBillReasons()
    {
	try
	{
	    vModifyReasonCode = new Vector();
	    vModifyReasonName = new Vector();
	    String sqlCmplementReason = "select strReasonCode,strReasonName from tblreasonmaster "
		    + "where strModifyBill='Y'";
	    ResultSet rsComplReason = clsGlobalVarClass.dbMysql.executeResultSet(sqlCmplementReason);
	    while (rsComplReason.next())
	    {
		vModifyReasonCode.add(rsComplReason.getString(1));
		vModifyReasonName.add(rsComplReason.getString(2));
	    }
	    rsComplReason.close();
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-10", JOptionPane.ERROR_MESSAGE);
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
	    if (cntSettlement == noOfSettlementMode)
	    {
		break;
	    }
	    if (cntArrayIndex < 4)
	    {
		settlementArray[cntArrayIndex].setText(clsSettelementOptions.listSettelmentOptions.get(cntSettlement));
		settlementArray[cntArrayIndex].setVisible(true);
		cntArrayIndex++;
	    }
	}
    }

    public void funRefreshItemTable()
    {
	try
	{
	    _deliveryCharge = Double.parseDouble(txtDeliveryCharges.getText().trim());

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    dm.addColumn("DiscPer");
	    dm.addColumn("DiscAmt");

	    dblDiscountAmt = 0;
	    dblDiscountPer = 0;
	    double itemSubTotal = 0;
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	    for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
	    {
		//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		clsBillItemDtl objBillItemDtl = entry.getValue();
		/*
                 * if( mapBeforeComplimentory.size()>0) {
                 * if(mapBeforeComplimentory.containsKey(entry.getKey())) {
                 * objBillItemDtl.setAmount(mapBeforeComplimentory.get(entry.getKey()));
                 * } }
		 */

		Object row[] =
		{
		    objBillItemDtl.getItemName(), gDecimalFormat.format(objBillItemDtl.getQuantity()), gDecimalFormat.format(objBillItemDtl.getAmount()), gDecimalFormat.format(objBillItemDtl.getDiscountPercentage()), gDecimalFormat.format(objBillItemDtl.getDiscountAmount())
		};
		dblDiscountAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
		dblDiscountPer = objBillItemDtl.getDiscountPercentage();
		if (objBillItemDtl.getDiscountAmount() > 0)
		{
		    itemSubTotal += objBillItemDtl.getAmount();
		}
		dm.addRow(row);

		clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		objItemDtl.setItemCode(objBillItemDtl.getItemCode());
		objItemDtl.setItemName(objBillItemDtl.getItemName());
		objItemDtl.setAmount(objBillItemDtl.getAmount());
		objItemDtl.setDiscAmt(objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity());
		arrListItemDtls.add(objItemDtl);
	    }

	    if (clsGlobalVarClass.gActivePromotions)
	    {
		if (null != mapPromoItemDisc)
		{
		    if (mapPromoItemDisc.size() > 0)
		    {
			String discReasonCode = "", selectedReason = "";
			if (vReasonCodeForDiscount.size() == 0)
			{
			    JOptionPane.showMessageDialog(this, "No Discount reasons are created");
			    return;
			}
			else
			{
			    Object[] arrObjReasonName = vReasonNameForDiscount.toArray();
			    selectedReason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
			    if (null == selectedReason)
			    {
				JOptionPane.showMessageDialog(this, "Please Select Reason");
				return;
			    }
			    else
			    {
				for (int cntReason = 0; cntReason < vReasonCodeForDiscount.size(); cntReason++)
				{
				    if (vReasonNameForDiscount.elementAt(cntReason).toString().equals(selectedReason))
				    {
					discReasonCode = vReasonCodeForDiscount.elementAt(cntReason).toString();
					selectedReasonCode = discReasonCode;
					if (txtAreaRemark.getText().trim().isEmpty())
					{
					    txtAreaRemark.setText("Promotion discount");
					}
					discountRemarks = "Promotion discount";
					break;
				    }
				}
			    }
			}

			//2024
			Iterator<Map.Entry<String, clsBillItemDtl>> itPromoDisc = mapPromoItemDisc.entrySet().iterator();
			while (itPromoDisc.hasNext())
			{
			    clsBillItemDtl objItemDtl = itPromoDisc.next().getValue();
			    if (mapPromoItemDisc.containsKey(objItemDtl.getItemCode()))
			    {
				mapBillDiscDtl.put("ItemWise!" + objItemDtl.getItemName() + "!P", new clsBillDiscountDtl(selectedReason, discReasonCode, objItemDtl.getDiscountPercentage(), objItemDtl.getDiscountAmount(), objItemDtl.getAmount()));
			    }
			}
		    }
		}
	    }

	    if (itemSubTotal > 0)
	    {
		dblDiscountPer = (dblDiscountAmt * 100) / itemSubTotal;
	    }

	    //*****Start if the no. of taxes changes on settlement mode get the settlement mode which has max no. of taxes*******//
	    String settlementCodeForTax = settlementCode;
	    int intMaxTaxCountForSettlement = 0;
	    for (clsSettelementOptions objSettelementOptions : hmSettlemetnOptions.values())
	    {
		String settlementCode = objSettelementOptions.getStrSettelmentCode();
		String sql = "select count(a.strTaxCode) "
			+ "from tblsettlementtax a "
			+ "where a.strSettlementCode='" + settlementCode + "' "
			+ "and a.strApplicable='true' ";
		ResultSet rsMaxTaxCountForSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsMaxTaxCountForSettlement.next())
		{
		    if (rsMaxTaxCountForSettlement.getInt(1) > intMaxTaxCountForSettlement)
		    {
			intMaxTaxCountForSettlement = rsMaxTaxCountForSettlement.getInt(1);
			settlementCodeForTax = settlementCode;
		    }
		}
		rsMaxTaxCountForSettlement.close();
	    }

	    //*****End if the no. of taxes changes on settlement mode get the settlement mode which has max no. of taxes*******//
	    if (billTypeForTax.equals("Direct Biller"))
	    {
		//funCalculateTax("direct");
		arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCodeForTax, "Sales");
	    }
	    else if (billTypeForTax.equals("Make KOT") || billTypeForTax.equals("Make Bill"))
	    {
		//funCalculateTax("Make KOT");
		arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCodeForTax, "Sales");
	    }
	    else
	    {
		//funCalculateTax("Unsettled");
		arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCodeForTax, "Sales");
	    }

	    if (isRemoveSCTax && clsGlobalVarClass.gRemoveSCTaxCode.trim().length() > 0)
	    {
		objBillSettlementUtility.funRemoveServiceCharge();
	    }
	    _netAmount = _subTotal - dblDiscountAmt;

	    if (settleType.equals("Complementary"))
	    {
	    }

	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);

	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);

	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);

	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);

	    dblTotalTaxAmt = 0;
	    if (!settleType.equals("Complementary"))
	    {
		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}

	    }

	    double deliveryCharges = 0.00;
	    if (txtDeliveryCharges.getText().trim().length() > 0)
	    {
		deliveryCharges = Double.parseDouble(txtDeliveryCharges.getText());
		if (deliveryCharges > 0)
		{
		    Object[] objDelChargesRow =
		    {
			"Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		    };
		    dm.addRow(objDelChargesRow);
		}
	    }
	    double advanceAmount = 0;
	    if (clsGlobalVarClass.gAdvOrderNoForBilling == null || clsGlobalVarClass.gAdvOrderNoForBilling.trim().length() == 0)
	    {
		advanceAmount = 0;
	    }
	    else
	    {
		String sql = "select a.dblAdvDeposite,b.dblHomeDelCharges "
			+ "from tbladvancereceipthd a,tbladvbookbillhd b "
			+ "where a.strAdvBookingNo=b.strAdvBookingNo and a.strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'";
		ResultSet advanceorder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (advanceorder.next())
		{
		    Object[] totalAdvAmountRow =
		    {
			"Advance Amt", "", advanceorder.getDouble("dblAdvDeposite")
		    };
		    dm.addRow(totalAdvAmountRow);
		    advanceAmount = advanceorder.getDouble("dblAdvDeposite");

		    if (txtDeliveryCharges.getText().trim().length() == 0)
		    {
			Object[] objDelChargesRow =
			{
			    "Del Charges", "", advanceorder.getDouble(2)
			};
			dm.addRow(objDelChargesRow);
			_deliveryCharge = advanceorder.getDouble(2);
		    }
		}
		advanceorder.close();
	    }

	    dblTotalTaxAmt = Double.parseDouble(gDecimalFormat.format(dblTotalTaxAmt));
	    advanceAmount = Double.parseDouble(gDecimalFormat.format(advanceAmount));

	    _grandTotal = _netAmount + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal - advanceAmount;

	    //start code to calculate roundoff amount and round off by amt	    
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
		_grandTotal = mapRoundOff.get("roundOffAmt");
		_grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    }
	    else
	    {
		_grandTotal = Double.parseDouble(gDecimalFormat.format(_grandTotal));
	    }
	    dblSettlementAmount = _grandTotal;
	    //end code to calculate roundoff amount and round off by amt

	    Object[] grandTotalRow =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(grandTotalRow);
	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		String settlementDesc = ob.getStrSettelmentDesc();
		double settlementAmt = 0;
		if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
		{
		    settlementAmt = ob.getDblSettlementAmt();
		}
		else
		{
		    settlementAmt = ob.getDblPaidAmt();
		}

		Object[] row =
		{
		    settlementDesc, "", gDecimalFormat.format(settlementAmt)
		};
		dm.addRow(row);
	    }

	    double tempBalance = 0;
	    if (_paidAmount > _grandTotal)
	    {
		double tempRefundAmt = funGetTotalPaidAmount() - _grandTotal;
		if (settleType.equals("Complementary"))
		{
		    tempRefundAmt = 0;
		}

		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    tempRefundAmt = Math.rint(tempRefundAmt);
		}
		else
		{
		    tempRefundAmt = Double.parseDouble(gDecimalFormat.format(tempRefundAmt));
		}

		Object[] row =
		{
		    "Refund", "", tempRefundAmt
		};
		dm.addRow(row);
	    }
	    else
	    {
		tempBalance = _grandTotal - funGetTotalPaidAmount();
		if (tempBalance <= 0)
		{
		    tempBalance = 0.00;
		}

		if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		{
		    tempBalance = Math.rint(tempBalance);
		}
		else
		{
		    tempBalance = Double.parseDouble(gDecimalFormat.format(tempBalance));
		}

		Object[] row =
		{
		    "<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + tempBalance + "</html>"
		};
		dm.addRow(row);
	    }
	    tblItemTable.setModel(dm);

	    boolean flgComplimentaryBill = false;
	    if (hmSettlemetnOptions.size() == 1)
	    {
		for (clsSettelementOptions ob : hmSettlemetnOptions.values())
		{
		    if (ob.getStrSettelmentType().equals("Complementary"))
		    {
			flgComplimentaryBill = true;
			break;
		    }
		}
	    }
	    if (!flgComplimentaryBill)
	    {
		_balanceAmount = tempBalance;
		txtPaidAmt.setText(gDecimalFormat.format(_balanceAmount));///////////////////////
	    }

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(87);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(0);
	    tblItemTable.setShowHorizontalLines(true);

	    //System.out.println("SUB Total in Refresh= " + _subTotal);
//            JScrollBar vertical = jScrollPane3.getVerticalScrollBar();
//            vertical.setValue( vertical.getMaximum()+30 );
//            vertical.updateUI();
	    Rectangle goodRect = tblItemTable.getCellRect(dm.getRowCount() - 1, 0, true);
	    tblItemTable.scrollRectToVisible(goodRect);

	    lblBenowQRCode.setIcon(null);
	    PanelBenowSettlement.setVisible(false);
	    QRStringForBenow = "";

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-17", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funFillItemDtlGridForDirectBiller()
    {
	try
	{
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    mapPromoItemDisc = new HashMap<>();
	    double deliveryCharges = 0.00;
	    double temp_Total = 0.00;
	    hmBillItemDtl.clear();
	    listItemCode = new ArrayList<>();
	    hmItemList = new HashMap<String, String>();
	    String item = null, itemCode = null;
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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");

	    Map<String, clsPromotionItems> hmPromoItemDtl = null;
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

	    Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();

	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    //directbiller
	    for (clsDirectBillerItemDtl objDirectBillerItemList : objListDirectBillerItemDtl)
	    {
		double freeAmount = 0.00;
		item = objDirectBillerItemList.getItemName().trim();
		double quantity = objDirectBillerItemList.getQty();
		double amount = objDirectBillerItemList.getAmt();
		double rate = objDirectBillerItemList.getRate();
		itemCode = objDirectBillerItemList.getItemCode();

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(itemCode);
		objItemDtlForTax.setItemName(item);
		objItemDtlForTax.setAmount(0);
		objItemDtlForTax.setDiscAmt(0);

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
					discA = objPromoItemsDtl.getDiscAmt() * objPromoItemsDtl.getFreeItemQty();
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
				    }
				    else
				    {
					discP = objPromoItemsDtl.getDiscPer();
					discA = (discP / 100) * (objPromoItemsDtl.getFreeItemQty() * rate);
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				}
			    }
			}
		    }
		}

		temp_Total += amount;
		objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + amount);
		arrListItemDtls.add(objItemDtlForTax);

		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);

		if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
		{
		    double discAmt = 0;
		    double discPer = 0;
		    if (mapPromoItemDisc.containsKey(itemCode))
		    {
			clsBillItemDtl objItemPromoDiscount = mapPromoItemDisc.get(itemCode);
			discAmt = objItemPromoDiscount.getDiscountAmount();
			discPer = objItemPromoDiscount.getDiscountPercentage();

			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			dblDiscountAmt = dblDiscountAmt + discAmt;
			txtDiscountAmt.setText(String.valueOf(dblDiscountAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }
		    else
		    {
			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			dblDiscountAmt = dblDiscountAmt + discAmt;
			txtDiscountAmt.setText(String.valueOf(dblDiscountAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }

		    Iterator<Map.Entry<String, clsBillItemDtl>> itPromoDisc = mapPromoItemDisc.entrySet().iterator();
		    while (itPromoDisc.hasNext())
		    {
			clsBillItemDtl objItemDtl = itPromoDisc.next().getValue();
			if (mapPromoItemDisc.containsKey(objItemDtl.getItemCode()))
			{
			    mapBillDiscDtl.put("ItemWise!" + objItemDtl.getItemName() + "!P", new clsBillDiscountDtl("Promotion Discount", "R01", objItemDtl.getDiscountPercentage(), objItemDtl.getDiscountAmount(), objItemDtl.getAmount()));
			}
		    }
		}
		else
		{
		    funFillListForItemRow(item, quantity, amount, itemCode, 0, 0);
		}

		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount)
		};
		dm.addRow(rows);
	    }

	    if (hmPromoItem.size() > 0)
	    {
		btnGetOffer.setVisible(true);
	    }

	    _subTotal = temp_Total;
	    funFillGroupSubGroupList(listItemCode);
	    //funCalculateTax("direct");
	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");

	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    if (settleType.equals("Complementary"))
	    {
		_subTotal = 0.00;
		_netAmount = 0.00;
		_grandTotal = 0.00;
	    }

	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    dblTotalTaxAmt = 0;

	    if (!settleType.equals("Complementary"))
	    {
		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();
			if (clsGlobalVarClass.gRoundOffBillFinalAmount)
			{
			    taxAmt = Math.rint(objTaxDtl.getTaxAmount());
			}

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}

	    }
	    if (txtDeliveryCharges.getText().trim().length() > 0)
	    {
		deliveryCharges = Double.parseDouble(txtDeliveryCharges.getText());
		if (deliveryCharges > 0)
		{
		    Object[] objDelChargesRow =
		    {
			"Del Charges", "", gDecimalFormat.format(deliveryCharges)
		    };
		    dm.addRow(objDelChargesRow);
		}
	    }

	    double advanceAmount = 0;
	    if (clsGlobalVarClass.gAdvOrderNoForBilling == null || clsGlobalVarClass.gAdvOrderNoForBilling.trim().length() == 0)
	    {
		advanceAmount = 0;
	    }
	    else
	    {
		String sql = "select a.dblAdvDeposite,b.dblHomeDelCharges "
			+ "from tbladvancereceipthd a,tbladvbookbillhd b "
			+ "where a.strAdvBookingNo=b.strAdvBookingNo and a.strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'";
		ResultSet advanceorder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (advanceorder.next())
		{
		    Object[] totalAdvAmountRow =
		    {
			"Advance Amt", "", advanceorder.getDouble("dblAdvDeposite")
		    };
		    dm.addRow(totalAdvAmountRow);
		    advanceAmount = advanceorder.getDouble("dblAdvDeposite");

		    if (txtDeliveryCharges.getText().trim().length() == 0)
		    {
			Object[] objDelChargesRow =
			{
			    "Del Charges", "", advanceorder.getDouble(2)
			};
			dm.addRow(objDelChargesRow);
			deliveryCharges = advanceorder.getDouble(2);
		    }
		}
		advanceorder.close();
	    }

	    _grandTotal = _grandTotal + dblTotalTaxAmt + deliveryCharges;
	    _grandTotal = _grandTotal - advanceAmount;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    txtAmount.setText(gDecimalFormat.format(_grandTotal));
	    txtPaidAmt.setText(gDecimalFormat.format(_grandTotal));
	    Object[] r5 =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + _grandTotal + "</html>"
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);

	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-15", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    /**
     * 19 oct 2014 Description:- this method is call by settle bill, make bill,
     * make kot, modify bill
     *
     * @param tblNo
     * @param flgDiscount
     * @param formName
     * @param BillNo
     */
    private void funFillGridForMakeKOTTransaction(String tblNo, boolean flgDiscount, String formName, String billNo)
    {
	boolean flgReturn = false;
	String reason = "";

	try
	{
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    mapPromoItemDisc = new HashMap<>();
	    String operationType = "";

	    hmBillItemDtl.clear();
	    boolean flagUnsettledbills = false;
	    double tempTotal = 0.00;
	    listItemCode = new ArrayList<String>();
	    hmItemList = new HashMap<String, String>();

	    String sql = "select strTableName from tbltablemaster where strTableNo='" + tblNo + "' ";
	    ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTableName.next())
	    {
		lblTableNo.setText(rsTableName.getString(1));
	    }
	    rsTableName.close();

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    String sqlQuery = "";
	    switch (formName)
	    {
		case "Make KOT":
		    sqlQuery = "select strItemName,sum(dblItemQuantity),sum(dblAmount) "
			    + ",strItemCode,strWaiterNo,intPaxNo "
			    + "from tblitemrtemp "
			    + "where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and strTableNo='" + tblNo + "' "
			    + "and tdhComboItemYN='N' and strNCKotYN='N' "
			    + "group by strItemCode,strItemName";
		    operationType = "Make KOT";
		    break;

		case "Unsettled Bills":
		    sqlQuery = "select strItemName,sum(dblQuantity),sum(dblAmount)"
			    + ",strItemCode,sum(dblDiscountAmt),dblDiscountPer "
			    + "from tblbilldtl "
			    + "where strBillNo='" + billNo + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			    + "and tdhYN='N' "
			    + "group by strItemCode;";
		    flagUnsettledbills = true;
		    operationType = "Unsettled";
		    break;
	    }

	    Map<String, clsPromotionItems> hmPromoItemDtl = null;
	    boolean flgApplyPromoOnBill = false;

	    if (formName.equals("Make KOT"))
	    {
		if (clsGlobalVarClass.gActivePromotions)
		{
		    hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("MakeKOT", "", "", new ArrayList());
		    if (null != hmPromoItemDtl)
		    {
			if (hmPromoItemDtl.size() > 0)
			{
			    if (clsGlobalVarClass.gPopUpToApplyPromotionsOnBill)
			    {
				frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do want to Calculate Promotions for this Bill?");
				okOb.setVisible(true);
				int res = okOb.getResult();
				if (res == 1)
				{
				    flgApplyPromoOnBill = true;
				}

				if (kotObj != null)
				{
				    kotObj.setVisible(false);
				}
			    }
			    else
			    {
				flgApplyPromoOnBill = true;

				if (kotObj != null)
				{
				    kotObj.setVisible(false);
				}
			    }
			}
		    }
		}
	    }

	    ResultSet rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    Map<String, Double> mapItemCodeWithQty = new HashMap<String, Double>();
	    while (rsItemDtls.next())
	    {
		mapItemCodeWithQty.put(rsItemDtls.getString(4), rsItemDtls.getDouble(2));
	    }
	    rsItemDtls.close();
	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");
	    mapPromoItemDisc = new HashMap<>();
	    Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    List<clsPlayZoneItems> listPlayZoneItems = null;
	    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	    {
		listPlayZoneItems = objBillSettlementUtility.funApplyPlayZonePrice();
	    }

	    rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    while (rsItemDtls.next())
	    {
		String item = null, itemCode = null;
		double quantity = 0.00, amount = 0.00;
		double discAmt = 0, discPer = 0;

		double freeAmount = 0.00;
		item = rsItemDtls.getString(1);
		quantity = rsItemDtls.getDouble(2);
		amount = rsItemDtls.getDouble(3);
		double rate = amount / quantity;
		itemCode = rsItemDtls.getString(4);

		if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
		{
		    if (null != listPlayZoneItems)
		    {
			for (clsPlayZoneItems objPlayZoneItems : listPlayZoneItems)
			{
			    if (itemCode.equals(objPlayZoneItems.getStrItemCode()))
			    {
				rate = objPlayZoneItems.getDblRate();
				amount = rate * quantity;
			    }
			}
		    }
		}

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(itemCode);
		objItemDtlForTax.setItemName(item);
		objItemDtlForTax.setAmount(0);
		objItemDtlForTax.setDiscAmt(0);

		if (flagUnsettledbills)
		{
		    String sqlModifier = "select strModifierName,dblQuantity,dblAmount,strModifierCode,strItemCode,dblDiscAmt,dblDiscPer "
			    + " from tblbillmodifierdtl "
			    + " where strBillNo='" + voucherNo + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			    + " and left(strItemCode,7)='" + itemCode + "' ;";
		    ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
		    while (rsModifier.next())
		    {
			listItemCode.add(rsModifier.getString(5));
			hmItemList.put(rsModifier.getString(1).trim(), rsModifier.getString(5));
			funFillListForItemRow(rsModifier.getString(1), rsModifier.getDouble(2), rsModifier.getDouble(3), rsModifier.getString(5), rsModifier.getDouble(6), rsModifier.getDouble(7));
			Object[] modifier_row =
			{
			    rsModifier.getString(1), rsModifier.getString(2), rsModifier.getString(3)
			};
			dm.addRow(modifier_row);

			objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + rsModifier.getDouble(3));
			objItemDtlForTax.setDiscAmt(objItemDtlForTax.getDiscAmt() + rsModifier.getDouble(6));
			objItemDtlForTax.setModifierCode(rsModifier.getString(4));
			objItemDtlForTax.setModifierAmount(rsModifier.getDouble(3));

			tempTotal += rsModifier.getDouble(3);
		    }
		    rsModifier.close();
		}

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
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
				    }
				}
				else if (objPromoItemsDtl.getPromoType().equals("Discount"))
				{
				    double discA = 0;
				    double discP = 0;
				    if (objPromoItemsDtl.getDiscType().equals("Value"))
				    {
					//discA = objPromoItemsDtl.getDiscAmt();
					discA = objPromoItemsDtl.getDiscAmt() * objPromoItemsDtl.getFreeItemQty();  // Changes done temporary for promo discount
					discP = (discA / amount) * 100;
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);

					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				    else
				    {
					discP = objPromoItemsDtl.getDiscPer();
					discA = (discP / 100) * (objPromoItemsDtl.getFreeItemQty() * rate);
					String promoCode = objPromoItemsDtl.getPromoCode();
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
				    }

				    discAmt = discA;
				}
			    }
			}
		    }
		}

		if ("Make KOT".equalsIgnoreCase(formName))
		{
		    waiterNo = rsItemDtls.getString(5);
		    paxNo = rsItemDtls.getInt(6);
		}
		if (formName.equals("Unsettled Bills"))
		{
		    discAmt = rsItemDtls.getDouble(5);
		    discPer = rsItemDtls.getDouble(6);
		}
		if (formName.equals("Make KOT"))
		{
		    objItemDtlForTax.setDiscAmt(discAmt);
		}
		else
		{
		    objItemDtlForTax.setDiscAmt(rsItemDtls.getDouble(5));
		}

		tempTotal += amount;
		objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + amount);
		arrListItemDtls.add(objItemDtlForTax);

		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);

		if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
		{
		    discAmt = 0;
		    discPer = 0;
		    if (mapPromoItemDisc.containsKey(itemCode))
		    {
			clsBillItemDtl objItemPromoDiscount = mapPromoItemDisc.get(itemCode);
			discAmt = objItemPromoDiscount.getDiscountAmount();
			discPer = objItemPromoDiscount.getDiscountPercentage();
			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			txtDiscountAmt.setText(String.valueOf(discAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }
		    else
		    {
			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			txtDiscountAmt.setText(String.valueOf(discAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }
		}
		else
		{
		    funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
		}

		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount)
		};
		dm.addRow(rows);
	    }
	    rsItemDtls.close();

	    if (hmPromoItem.size() > 0)
	    {
		btnGetOffer.setVisible(true);
	    }

	    funFillGroupSubGroupList(listItemCode);
	    if (flagUnsettledbills)
	    {
		String sql_GetDiscount = "select dblDiscountAmt,dblDiscountPer"
			+ ",dblDeliveryCharges,strAdvBookingNo "
			+ "from tblbillhd where strBillNo='" + billNo + "' ";
		ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sql_GetDiscount);

		if (rsDiscount.next())
		{
		    txtDiscountAmt.setText(rsDiscount.getString(1));
		    txtDiscountPer.setText(rsDiscount.getString(2));
		    dblDiscountAmt = rsDiscount.getDouble(1);
		    dblDiscountPer = rsDiscount.getDouble(2);
		    _deliveryCharge = rsDiscount.getDouble(3);
		    txtDeliveryCharges.setText(String.valueOf(_deliveryCharge));
		}
		rsDiscount.close();
	    }

	    if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
	    {
		if (mapPromoItemDisc.size() > 0)
		{
		    String discReasonCode = "", selectedReason = "";
		    if (vReasonCodeForDiscount.size() == 0)
		    {
			JOptionPane.showMessageDialog(this, "No Discount reasons are created");
			return;
		    }
		    else
		    {
			Object[] arrObjReasonName = vReasonNameForDiscount.toArray();
			selectedReason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
			if (null == selectedReason)
			{
			    JOptionPane.showMessageDialog(this, "Please Select Reason");
			    flgReturn = false;
			    reason = "Disc Reason";
			    dispose();
			}
			else
			{
			    for (int cntReason = 0; cntReason < vReasonCodeForDiscount.size(); cntReason++)
			    {
				if (vReasonNameForDiscount.elementAt(cntReason).toString().equals(selectedReason))
				{
				    discReasonCode = vReasonCodeForDiscount.elementAt(cntReason).toString();
				    selectedReasonCode = discReasonCode;
				    if (txtAreaRemark.getText().trim().isEmpty())
				    {
					txtAreaRemark.setText("Promotion discount");
				    }
				    discountRemarks = "Promotion discount";
				    flgReturn = true;
				    break;
				}
			    }
			}
		    }
		}

		Iterator<Map.Entry<String, clsBillItemDtl>> itPromoDisc = mapPromoItemDisc.entrySet().iterator();
		while (itPromoDisc.hasNext())
		{
		    clsBillItemDtl objItemDtl = itPromoDisc.next().getValue();
		    if (mapPromoItemDisc.containsKey(objItemDtl.getItemCode()))
		    {
			dblDiscountAmt = dblDiscountAmt + objItemDtl.getDiscountAmount();
			mapBillDiscDtl.put("ItemWise!" + objItemDtl.getItemName() + "!P", new clsBillDiscountDtl("Promotion Discount", selectedReasonCode, objItemDtl.getDiscountPercentage(), objItemDtl.getDiscountAmount(), objItemDtl.getAmount()));
		    }
		}
	    }

	    _subTotal = tempTotal;
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    if (settleType.equals("Complementary"))
	    {
		_subTotal = 0.00;
		_netAmount = 0.00;
		_grandTotal = 0.00;
	    }
	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    if (_deliveryCharge != 0.00)
	    {
		Object[] objDelChargesRow =
		{
		    "Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		};
		dm.addRow(objDelChargesRow);
	    }

	    dblTotalTaxAmt = 0;
	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");
	    if (isRemoveSCTax && clsGlobalVarClass.gRemoveSCTaxCode.trim().length() > 0)
	    {
		objBillSettlementUtility.funRemoveServiceCharge();
	    }

	    if (!settleType.equals("Complementary"))
	    {
		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}
	    }

	    _grandTotal = _grandTotal + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal;

	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		//start code to calculate roundoff amount and round off by amt
		Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
		_grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    else
	    {
		_grandTotal = Double.parseDouble(gDecimalFormat.format(_grandTotal));
	    }

	    txtAmount.setText(String.valueOf(_grandTotal));
	    txtPaidAmt.setText(String.valueOf(_grandTotal));
	    Object[] r5 =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + _grandTotal + "</html>"
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);

	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-16", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funFillGridForBillForItemsTransaction(String forTable, List<clsMakeKotItemDtl> listOfKOTWiseItemDtl, List<clsMakeKotItemDtl> listOfItemsForToBeBilled)
    {
	boolean flgReturn = false;
	String reason = "";

	try
	{
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    mapPromoItemDisc = new HashMap<>();
	    String operationType = "";

	    hmBillItemDtl.clear();
	    boolean flagUnsettledbills = false;
	    double tempTotal = 0.00;
	    listItemCode = new ArrayList<String>();
	    hmItemList = new HashMap<String, String>();
	    String item = null, itemCode = null;
	    double quantity = 0.00, amount = 0.00;
	    double discAmt = 0, discPer = 0;

	    String sql = "select strTableName from tbltablemaster where strTableNo='" + forTable + "' ";
	    ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTableName.next())
	    {
		lblTableNo.setText(rsTableName.getString(1));
	    }
	    rsTableName.close();

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");

	    Map<String, clsPromotionItems> hmPromoItemDtl = null;
	    boolean flgApplyPromoOnBill = false;

	    Map<String, Double> mapItemCodeWithQty = new HashMap<String, Double>();
	    for (clsMakeKotItemDtl objItemDtl : listOfItemsForToBeBilled)
	    {
		mapItemCodeWithQty.put(objItemDtl.getItemCode(), objItemDtl.getQty());
	    }

	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");
	    mapPromoItemDisc = new HashMap<>();
	    Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    List<clsPlayZoneItems> listPlayZoneItems = null;
	    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
	    {
		listPlayZoneItems = objBillSettlementUtility.funApplyPlayZonePrice();
	    }

	    for (clsMakeKotItemDtl objItemDtl : listOfItemsForToBeBilled)
	    {
		double freeAmount = 0.00;
		item = objItemDtl.getItemName();
		quantity = objItemDtl.getQty();
		amount = objItemDtl.getAmt();
		double rate = amount / quantity;
		itemCode = objItemDtl.getItemCode();

		if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
		{
		    if (null != listPlayZoneItems)
		    {
			for (clsPlayZoneItems objPlayZoneItems : listPlayZoneItems)
			{
			    if (itemCode.equals(objPlayZoneItems.getStrItemCode()))
			    {
				rate = objPlayZoneItems.getDblRate();
				amount = rate * quantity;
			    }
			}
		    }
		}

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(itemCode);
		objItemDtlForTax.setItemName(item);
		objItemDtlForTax.setAmount(0);
		objItemDtlForTax.setDiscAmt(0);

		waiterNo = objItemDtl.getWaiterNo();
		paxNo = objItemDtl.getPaxNo();

		tempTotal += amount;
		objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + amount);
		arrListItemDtls.add(objItemDtlForTax);

		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);

		funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);

		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount)
		};
		dm.addRow(rows);
	    }

	    funFillGroupSubGroupList(listItemCode);

	    _subTotal = tempTotal;
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    if (settleType.equals("Complementary"))
	    {
		_subTotal = 0.00;
		_netAmount = 0.00;
		_grandTotal = 0.00;
	    }
	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    if (_deliveryCharge != 0.00)
	    {
		Object[] objDelChargesRow =
		{
		    "Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		};
		dm.addRow(objDelChargesRow);
	    }
	    dblTotalTaxAmt = 0;

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");
	    if (!settleType.equals("Complementary"))
	    {
		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();
			if (clsGlobalVarClass.gRoundOffBillFinalAmount)
			{
			    taxAmt = Math.rint(objTaxDtl.getTaxAmount());
			}

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}
	    }

	    _grandTotal = _grandTotal + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    txtAmount.setText(gDecimalFormat.format(_grandTotal));
	    txtPaidAmt.setText(gDecimalFormat.format(_grandTotal));
	    Object[] r5 =
	    {
		"Grand Total", "", gDecimalFormat.format(_grandTotal)
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"Balance", "", gDecimalFormat.format(_grandTotal)
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);

	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-16", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void fillMakeTableForBillFromKOTs(List listBillFromKOTs)
    {
	try
	{
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    mapPromoItemDisc = new HashMap<>();
	    mapBillDiscDtl.clear();
	    listItemCode = new ArrayList<String>();
	    hmItemList = new HashMap<String, String>();
	    hmBillItemDtl.clear();

	    dblDiscountAmt = 0;
	    dblDiscountPer = 0;
	    _paidAmount = 0.00;
	    String operationType = "";

	    double temp_Total = 0.00;
	    String item = null, itemCode = null;
	    double quantity = 0.00, amount = 0.00;

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    dm.addColumn("DiscPer");
	    dm.addColumn("DiscAmt");
	    String sql_Query = "";
	    //String sqlAppendForBillFromKOTS="";
	    StringBuilder sbBillFromKOTs = new StringBuilder();
	    List<String> list_KOTNos = new ArrayList<String>();

	    list_KOTNos = listBillFromKOTs;
	    if (!list_KOTNos.isEmpty())
	    {
		boolean first = true;
		for (String kot : list_KOTNos)
		{
		    if (first)
		    {
			//sqlAppendForBillFromKOTS+="( strKOTNo='"+kot+"'" ;
			sbBillFromKOTs.append("( a.strKOTNo='");
			sbBillFromKOTs.append(kot);
			sbBillFromKOTs.append("'");
			first = false;
		    }
		    else
		    {
			//sqlAppendForBillFromKOTS+=" or ".concat(" strKOTNo='"+kot+"' ");
			sbBillFromKOTs.append(" or ");
			sbBillFromKOTs.append(" a.strKOTNo='");
			sbBillFromKOTs.append(kot);
			sbBillFromKOTs.append("' ");
		    }
		}
	    }

	    Map<String, clsPromotionItems> hmPromoItemDtl = null;
	    boolean flgApplyPromoOnBill = false;

	    if (clsGlobalVarClass.gActivePromotions)
	    {
		hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("BillFromKOTs", sbBillFromKOTs.toString(), "", new ArrayList());
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

	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	    sql_Query = "select a.strItemName,sum(a.dblItemQuantity),sum(a.dblAmount),a.strItemCode,a.strWaiterNo,a.intPaxNo "
		    + " from tblitemrtemp a"
		    + " where a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' and " + sbBillFromKOTs + ") "
		    + " and a.tdhComboItemYN='N' and a.strNCKotYN='N' "
		    + " group by a.strItemCode,a.strItemName ";
	    operationType = "Make KOT";

	    ResultSet rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	    while (rsItemDtls.next())
	    {
		double freeAmount = 0.00;
		item = rsItemDtls.getString(1).trim();
		quantity = rsItemDtls.getDouble(2);
		amount = rsItemDtls.getDouble(3);
		itemCode = rsItemDtls.getString(4);
		double rate = amount / quantity;

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(itemCode);
		objItemDtlForTax.setItemName(item);
		objItemDtlForTax.setAmount(0);
		objItemDtlForTax.setDiscAmt(0);

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
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
				    }
				}
				else if (objPromoItemsDtl.getPromoType().equals("Discount"))
				{
				    double discA = 0;
				    double discP = 0;
				    if (objPromoItemsDtl.getDiscType().equals("Value"))
				    {
					discA = objPromoItemsDtl.getDiscAmt() * objPromoItemsDtl.getFreeItemQty();
					discP = (discA / amount) * 100;
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);

					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				    else
				    {
					discP = objPromoItemsDtl.getDiscPer();
					discA = (discP / 100) * (objPromoItemsDtl.getFreeItemQty() * rate);
					String promoCode = objPromoItemsDtl.getPromoCode();
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				}
			    }
			}
		    }
		}

		temp_Total += amount;
		objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + amount);
		arrListItemDtls.add(objItemDtlForTax);

		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);
		double discAmt = 0, discPer = 0;
		if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
		{
		    discAmt = 0;
		    discPer = 0;
		    if (mapPromoItemDisc.containsKey(itemCode))
		    {
			clsBillItemDtl objItemPromoDiscount = mapPromoItemDisc.get(itemCode);
			discAmt = objItemPromoDiscount.getDiscountAmount();
			discPer = objItemPromoDiscount.getDiscountPercentage();
			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			txtDiscountAmt.setText(String.valueOf(discAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }
		    else
		    {
			funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
			txtDiscountAmt.setText(String.valueOf(discAmt));
			txtDiscountPer.setText(String.valueOf(discPer));
		    }
		}
		else
		{
		    funFillListForItemRow(item, quantity, amount, itemCode, 0, 0);
		}

		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount), 0, 0
		};
		dm.addRow(rows);
	    }
	    rsItemDtls.close();
	    funFillGroupSubGroupList(listItemCode);

	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");

	    _subTotal = temp_Total;
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    if (_deliveryCharge != 0.00)
	    {
		Object[] objDelChargesRow =
		{
		    "Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		};
		dm.addRow(objDelChargesRow);
	    }
	    dblTotalTaxAmt = 0;
	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");

	    if (!settleType.equals("Complementary"))
	    {
		/*
                 * for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++) {
                 * ArrayList<Object> list = arrListTaxCal.get(cnt); if
                 * (list.get(4).toString().equals("Forward")) { String
                 * strTaxName = list.get(1).toString(); double dblTaxAmt =
                 * Double.parseDouble(list.get(3).toString()); dblTotalTaxAmt =
                 * dblTotalTaxAmt + dblTaxAmt; Object[] taxTotalRow = {
                 * strTaxName, "", Math.rint(dblTaxAmt) };
                 * dm.addRow(taxTotalRow); } }
		 */

		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();
			if (clsGlobalVarClass.gRoundOffBillFinalAmount)
			{
			    taxAmt = Math.rint(objTaxDtl.getTaxAmount());
			}

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}
	    }

	    _grandTotal = _grandTotal + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    txtAmount.setText(gDecimalFormat.format(_grandTotal));
	    txtPaidAmt.setText(gDecimalFormat.format(_grandTotal));
	    Object[] r5 =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(0);
	    tblItemTable.setShowHorizontalLines(true);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-16", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funFillGridForAdvOrderAndDirectBillerBills(String formName, String billNo)
    {
	try
	{

	    hmBillItemDtl.clear();
	    boolean flagUnsettledbills = false;
	    double tempTotal = 0.00;
	    listItemCode = new ArrayList<String>();
	    hmItemList = new HashMap<String, String>();
	    String item = null, itemCode = null;
	    double quantity = 0.00, amount = 0.00;
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    dm.addColumn("DiscPer");
	    dm.addColumn("DiscAmt");
	    String sqlQuery = "";

	    switch (formName)
	    {
		case "Unsettled Bills":
		    sqlQuery = "select strItemName,sum(dblQuantity),sum(dblAmount),strItemCode"
			    + ",dblDiscountAmt,dblDiscountPer "
			    + "from tblbilldtl "
			    + "where strBillNo='" + billNo + "' and tdhYN='N' group by strItemCode;";
		    flagUnsettledbills = true;
		    break;
	    }
	    ResultSet rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    while (rsItemDtls.next())
	    {
		item = rsItemDtls.getString(1).trim();
		quantity = rsItemDtls.getDouble(2);
		amount = rsItemDtls.getDouble(3);
		itemCode = rsItemDtls.getString(4);
		double discAmt = Double.parseDouble(rsItemDtls.getString(5));
		double discPer = Double.parseDouble(rsItemDtls.getString(6));

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(itemCode);
		objItemDtlForTax.setItemName(item);
		objItemDtlForTax.setAmount(amount);
		objItemDtlForTax.setDiscAmt(rsItemDtls.getDouble(5));

		tempTotal += amount;
		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);
		funFillListForItemRow(item, quantity, amount, itemCode, discAmt, discPer);
		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount), discPer, discAmt
		};
		dm.addRow(rows);
		if (flagUnsettledbills)
		{
		    String sqlModifier = "select strModifierName,dblQuantity,dblAmount,strModifierCode,dblDiscPer,dblDiscAmt "
			    + " from tblbillmodifierdtl "
			    + " where strBillNo='" + voucherNo + "' and left(strItemCode,7)='" + itemCode + "' ;";
		    ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
		    while (rsModifier.next())
		    {
			String modCode = itemCode + "" + rsModifier.getString(4);
			listItemCode.add(modCode);
			hmItemList.put(rsModifier.getString(1).trim(), modCode);
			funFillListForItemRow(rsModifier.getString(1), rsModifier.getDouble(2), rsModifier.getDouble(3), modCode, rsModifier.getDouble(6), rsModifier.getDouble(5));
			Object[] arrObjModifiers =
			{
			    rsModifier.getString(1), rsModifier.getString(2), rsModifier.getString(3)
			};
			dm.addRow(arrObjModifiers);

			objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + rsModifier.getDouble(3));
			objItemDtlForTax.setDiscAmt(objItemDtlForTax.getDiscAmt() + rsModifier.getDouble(6));

			tempTotal += rsModifier.getDouble(3);
		    }
		    rsModifier.close();
		}
		arrListItemDtls.add(objItemDtlForTax);
	    }
	    rsItemDtls.close();

	    funFillGroupSubGroupList(listItemCode);
	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");
	    if (flagUnsettledbills)
	    {
		String sqlGetDiscount = "select dblDiscountAmt,dblDiscountPer,dblDeliveryCharges,strAdvBookingNo "
			+ "from tblbillhd where strBillNo='" + billNo + "'";
		ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlGetDiscount);

		if (rsDiscount.next())
		{
		    txtDiscountAmt.setText(rsDiscount.getString(1));
		    txtDiscountPer.setText(rsDiscount.getString(2));
		    dblDiscountAmt = rsDiscount.getDouble(1);
		    dblDiscountPer = rsDiscount.getDouble(2);
		    _deliveryCharge = rsDiscount.getDouble(3);
		    txtDeliveryCharges.setText(String.valueOf(_deliveryCharge));
		    //clsGlobalVarClass.gAdvOrderNoForBilling=rs_Discount.getString(4);
		}
		rsDiscount.close();
	    }
	    _subTotal = tempTotal;
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    if (settleType.equals("Complementary"))
	    {
		_subTotal = 0.00;
		_netAmount = 0.00;
		_grandTotal = 0.00;
		//dblFinalSubTotal = 0.00;
	    }
	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    if (_deliveryCharge != 0.00)
	    {
		Object[] objDelChargesRow =
		{
		    "Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		};
		dm.addRow(objDelChargesRow);
	    }

	    dblTotalTaxAmt = 0;
	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");
	    if (isRemoveSCTax && clsGlobalVarClass.gRemoveSCTaxCode.trim().length() > 0)
	    {
		objBillSettlementUtility.funRemoveServiceCharge();
	    }

	    if (!settleType.equals("Complementary"))
	    {

		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			double taxAmt = objTaxDtl.getTaxAmount();
			if (clsGlobalVarClass.gRoundOffBillFinalAmount)
			{
			    taxAmt = Math.rint(objTaxDtl.getTaxAmount());
			}

			String strTaxAmt = gDecimalFormat.format(taxAmt);
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", strTaxAmt
			};
			dm.addRow(taxTotalRow);
		    }
		}
	    }

	    double advanceAmount = 0;
	    sqlQuery = "select a.dblAdvDeposite,b.strAdvBookingNo "
		    + "from tbladvancereceipthd a left outer join tbladvbookbillhd b on a.strAdvBookingNo=b.strAdvBookingNo "
		    + "left outer join tblbillhd c on b.strAdvBookingNo=c.strAdvBookingNo "
		    + "where c.strBillNo='" + billNo + "'";
	    ResultSet advanceorder = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
	    if (advanceorder.next())
	    {
		advanceAmount = Double.parseDouble(advanceorder.getString("dblAdvDeposite"));
		Object[] totalAdvAmountRow =
		{
		    "Advance Amt", "", Math.rint(advanceAmount)
		};
		dm.addRow(totalAdvAmountRow);
		clsGlobalVarClass.gAdvOrderNoForBilling = advanceorder.getString(2);
	    }
	    advanceorder.close();

	    _grandTotal = _grandTotal + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal - advanceAmount;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    txtAmount.setText(gDecimalFormat.format(_grandTotal));
	    txtPaidAmt.setText(gDecimalFormat.format(_grandTotal));
	    Object[] r5 =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(0);
	    tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(0);
	    tblItemTable.setShowHorizontalLines(true);

	    txtPaidAmt.requestFocus();
	    txtPaidAmt.selectAll();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-16", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funFillGridForAddKOTToBill(String formName, String billNo)
    {
	try
	{
	    hmPromoItem = new HashMap<String, clsPromotionItems>();
	    mapPromoItemDisc = new HashMap<>();
	    hmAddKOTItems = new HashMap<String, Double>();

	    hmBillItemDtl.clear();
	    boolean flagUnsettledbills = false;
	    double tempTotal = 0.00;
	    listItemCode = new ArrayList<String>();
	    hmItemList = new HashMap<String, String>();
	    String item = null, itemCode = null, orderProcessingTime = "";
	    double quantity = 0.00, amount = 0.00;

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
	    dm.addColumn("Description");
	    dm.addColumn("Qty");
	    dm.addColumn("Amount");
	    dm.addColumn("DiscPer");
	    dm.addColumn("DiscAmt");

	    String sqlAppendForBillFromKOTS = "";
	    List<String> listKOTNos = new ArrayList<String>();
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    sqlAppendForBillFromKOTS = "";
	    listKOTNos = objAddKOTToBill.getList_Selected_KOTs();
	    if (!listKOTNos.isEmpty())
	    {
		boolean first = true;
		for (String kot : listKOTNos)
		{
		    if (first)
		    {
			sqlAppendForBillFromKOTS += "( strKOTNo='" + kot + "'";
			first = false;
		    }
		    else
		    {
			sqlAppendForBillFromKOTS += " or ".concat(" strKOTNo='" + kot + "' ");
		    }
		}
	    }
	    String[] dtlBillNos = null;
	    List<String> listBillNos = new ArrayList<String>();
	    listBillNos.add(voucherNo);
	    String sql = "select a.strPOSCode,a.strHdBillNo,a.strDtlBillNos "
	    + "from tblbillseriesbilldtl a  "
	    + "where a.strHdBillNo='" + voucherNo + "' "
	    + "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
	    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillType.next())
	    {
		dtlBillNos = rsBillType.getString(3).split(",");
	    }
	    rsBillType.close();
		if(dtlBillNos!=null){
		    for(String bill:dtlBillNos){
			if(!bill.isEmpty()){
			    listBillNos.add(bill);
			}
		    }
		}
		String sqlAppendBillFromBillSeries="";
		if (!listBillNos.isEmpty())
		{
		    boolean first = true;
		    for (String bill : listBillNos)
		    {
			if (first)
			{
			    sqlAppendBillFromBillSeries += "( strBillNo='" + bill + "'";
			    first = false;
			}
			else
			{
			    sqlAppendBillFromBillSeries += " or ".concat(" strBillNo='" + bill + "' ");
			}
		    }
		}
	     sql = "select strItemName,sum(Qty),sum(Amt),strItemCode,dblRate,DiscAmt,DiscPer,tmeOrderProcessing  "
		    + "from (select strItemCode,strItemName,dblQuantity as Qty,dblAmount as Amt ,dblDiscountAmt as DiscAmt, "
		    + "dblDiscountPer as DiscPer,dblRate,tmeOrderProcessing  from tblbilldtl where " + sqlAppendBillFromBillSeries + ")  "
		    + "union all select strItemCode,strItemName,dblItemQuantity as Qty,dblAmount as Amt ,0 as DiscAmt,0 as DiscPer,dblRate,tmeOrderProcessing  "
		    + "from tblitemrtemp where " + sqlAppendForBillFromKOTS + ")) a  "
		    + "Group By tmeOrderProcessing,strItemCode,strItemName ";
	    flagUnsettledbills = true;
	    //System.out.println("Add KOT to Bill= "+sql);

	    Map<String, clsPromotionItems> hmPromoItemDtl = null;
	    boolean flgApplyPromoOnBill = false;

	    if (clsGlobalVarClass.gActivePromotions)
	    {
		hmPromoItemDtl = objCalculateBillPromotions.funCalculatePromotions("AddKOTToBill", "", "", new ArrayList());
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

	    ResultSet rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    Map<String, Double> mapItemCodeWithQty = new HashMap<String, Double>();
	    while (rsItemDtls.next())
	    {
		mapItemCodeWithQty.put(rsItemDtls.getString(4), rsItemDtls.getDouble(2));
	    }
	    rsItemDtls.close();
	    txtDiscountAmt.setText("0.00");
	    txtDiscountPer.setText("0.00");
	    mapPromoItemDisc = new HashMap<>();
	    Map<String, clsPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPromotionItems>();

	    rsItemDtls = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemDtls.next())
	    {
		double freeAmount = 0.00;
		item = rsItemDtls.getString(1).trim();
		quantity = rsItemDtls.getDouble(2);
		double rate = rsItemDtls.getDouble(5);//amount / quantity;
		amount = rate * quantity;
		itemCode = rsItemDtls.getString(4);
		hmAddKOTItems.put(itemCode, amount);

		clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		objItemDtl.setItemCode(itemCode);
		objItemDtl.setItemName(item);
		objItemDtl.setAmount(0);
		objItemDtl.setDiscAmt(rsItemDtls.getDouble(6));
		orderProcessingTime = rsItemDtls.getString(8);

		if ("Make KOT".equalsIgnoreCase(formName))
		{
		    waiterNo = rsItemDtls.getString(5);
		    paxNo = rsItemDtls.getInt(6);
		}

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
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
				    }
				}
				else if (objPromoItemsDtl.getPromoType().equals("Discount"))
				{
				    double discA = 0;
				    double discP = 0;
				    if (objPromoItemsDtl.getDiscType().equals("Value"))
				    {
					discA = objPromoItemsDtl.getDiscAmt() * objPromoItemsDtl.getFreeItemQty();
					discP = (discA / amount) * 100;
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemDtl.remove(itemCode);
					hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);

					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				    else
				    {
					discP = objPromoItemsDtl.getDiscPer();
					discA = (discP / 100) * (objPromoItemsDtl.getFreeItemQty() * rate);
					String promoCode = objPromoItemsDtl.getPromoCode();
					hmPromoItem.put(itemCode, objPromoItemsDtl);
					hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
					//hmPromoItem.put(itemCode, discP+"#"+promoCode);
					hmPromoItemDtl.remove(itemCode);
					clsBillItemDtl objItemPromoDiscount = new clsBillItemDtl();
					objItemPromoDiscount.setItemCode(itemCode);
					objItemPromoDiscount.setItemName(item);
					objItemPromoDiscount.setDiscountAmount(discA);
					objItemPromoDiscount.setDiscountPercentage(discP);
					objItemPromoDiscount.setAmount(amount);

					mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
				    }
				}
			    }
			}
		    }
		}

		tempTotal += amount;
		objItemDtl.setAmount(objItemDtl.getAmount() + amount);

		listItemCode.add(itemCode);
		hmItemList.put(item, itemCode);
		funFillListForItemRow(item, quantity, amount, itemCode, rsItemDtls.getDouble(6), rsItemDtls.getDouble(7));
		Object[] rows =
		{
		    item, gDecimalFormat.format(quantity), gDecimalFormat.format(amount), rsItemDtls.getDouble(6), rsItemDtls.getDouble(7)
		};
		dm.addRow(rows);
		if (flagUnsettledbills)
		{
		    String sqlModifier = "select strModifierName,dblQuantity,dblAmount,strModifierCode,strItemCode "
			    + "from tblbillmodifierdtl where strBillNo='" + billNo + "' "
			    + "and left(strItemCode,7)='" + itemCode + "' ;";
		    ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
		    while (rsModifier.next())
		    {
			funFillListForItemRow(rsModifier.getString(1), rsModifier.getDouble(2), rsModifier.getDouble(3), itemCode + "" + rsModifier.getString(4), 0, 0);
			Object[] arrModifiers =
			{
			    rsModifier.getString(1), rsModifier.getString(2), rsModifier.getString(3)
			};
			dm.addRow(arrModifiers);
			hmItemList.put(rsModifier.getString(1), rsModifier.getString(5));
			tempTotal += rsModifier.getDouble(3);
			objItemDtl.setAmount(objItemDtl.getAmount() + rsModifier.getDouble(3));
		    }
		    rsModifier.close();
		}
		arrListItemDtls.add(objItemDtl);
	    }
	    rsItemDtls.close();

	    if (hmPromoItem.size() > 0)
	    {
		btnGetOffer.setVisible(true);
	    }

	    funFillGroupSubGroupList(listItemCode);
	    if (flagUnsettledbills)
	    {
		String sqlGetDiscount = "select dblDiscountAmt,dblDiscountPer"
			+ ",dblDeliveryCharges,strAdvBookingNo "
			+ "from tblbillhd where strBillNo='" + billNo + "'";
		ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sqlGetDiscount);
		if (rsDiscount.next())
		{
		    txtDiscountAmt.setText(rsDiscount.getString(1));
		    txtDiscountPer.setText(rsDiscount.getString(2));
		    dblDiscountAmt = rsDiscount.getDouble(1);
		    dblDiscountPer = rsDiscount.getDouble(2);
		    _deliveryCharge = rsDiscount.getDouble(3);
		    txtDeliveryCharges.setText(String.valueOf(_deliveryCharge));
		    //clsGlobalVarClass.gAdvOrderNoForBilling=rs_Discount.getString(4);
		}
		rsDiscount.close();
	    }

	    if (clsGlobalVarClass.gActivePromotions && flgApplyPromoOnBill)
	    {
		Iterator<Map.Entry<String, clsBillItemDtl>> itPromoDisc = mapPromoItemDisc.entrySet().iterator();
		while (itPromoDisc.hasNext())
		{
		    clsBillItemDtl objItemDtl = itPromoDisc.next().getValue();
		    if (mapPromoItemDisc.containsKey(objItemDtl.getItemCode()))
		    {
			dblDiscountAmt = dblDiscountAmt + objItemDtl.getDiscountAmount();
			mapBillDiscDtl.put("ItemWise!" + objItemDtl.getItemName() + "!P", new clsBillDiscountDtl("Promotion Discount", "R01", objItemDtl.getDiscountPercentage(), objItemDtl.getDiscountAmount(), objItemDtl.getAmount()));
		    }
		}
	    }

	    _subTotal = tempTotal;
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount;

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, dblDiscountAmt, "", settlementCode, "Sales");

	    if (settleType.equals("Complementary"))
	    {
		_subTotal = 0.00;
		_netAmount = 0.00;
		_grandTotal = 0.00;
	    }

	    Object[] blankRow =
	    {
		"", "", ""
	    };
	    dm.addRow(blankRow);
	    Object[] subTotalRow =
	    {
		"SubTotal", "", gDecimalFormat.format(_subTotal)
	    };
	    dm.addRow(subTotalRow);
	    Object[] discountRow =
	    {
		"Discount", "", gDecimalFormat.format(dblDiscountAmt)
	    };
	    dm.addRow(discountRow);
	    Object[] netTotalRow =
	    {
		"NetTotal", "", gDecimalFormat.format(_netAmount)
	    };
	    dm.addRow(netTotalRow);
	    if (_deliveryCharge != 0.00)
	    {
		Object[] objDelChargesRow =
		{
		    "Del Charges", "", gDecimalFormat.format(_deliveryCharge)
		};
		dm.addRow(objDelChargesRow);
	    }
	    dblTotalTaxAmt = 0;

	    if (!settleType.equals("Complementary"))
	    {
		/*
                 * for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++) {
                 * ArrayList<Object> list = arrListTaxCal.get(cnt); if
                 * (list.get(4).toString().equals("Forward")) { dblTotalTaxAmt =
                 * dblTotalTaxAmt + Double.parseDouble(list.get(3).toString());
                 * Object[] taxTotalRow = { list.get(1).toString(), "",
                 * Double.parseDouble(list.get(3).toString()) };
                 * dm.addRow(taxTotalRow); } }
		 */

		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			dblTotalTaxAmt = dblTotalTaxAmt + objTaxDtl.getTaxAmount();
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", objTaxDtl.getTaxAmount()
			};
			dm.addRow(taxTotalRow);
		    }
		}
	    }

	    _grandTotal = _grandTotal + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    txtAmount.setText(gDecimalFormat.format(_grandTotal));
	    txtPaidAmt.setText(gDecimalFormat.format(_grandTotal));
	    Object[] r5 =
	    {
		"<html> <font color=blue size=4 ><b>Grand Total</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    dm.addRow(r5);

	    for (int balnk = 0; balnk < 1; balnk++)
	    {
		Object[] blankrow =
		{
		    "", "", ""
		};
		dm.addRow(blankrow);
	    }
	    Object[] paymentrow =
	    {
		"Payment Modes", "", ""
	    };
	    dm.addRow(paymentrow);

	    Object[] row =
	    {
		"<html> <font color=blue size=4 ><b>Balance</html>", "", "<html> <font color=blue size=4 ><b>" + gDecimalFormat.format(_grandTotal) + "</html>"
	    };
	    _balanceAmount = _grandTotal;
	    dm.addRow(row);
	    tblItemTable.setModel(dm);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
	    tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	    tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
	    tblItemTable.setShowHorizontalLines(true);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-16", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funSetDate()
    {
	try
	{
	    java.util.Date dtDate = new java.util.Date();
	    String dte = dtDate.getDate() + "-" + (dtDate.getMonth() + 1) + "-" + (dtDate.getYear() + 1900);
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dteCheque.setDate(date);
	    dteExpiry.setDate(date);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-18", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

//To set the amount related to Grand total Amount in calculator
    private void funCalculator()
    {
	billAmount = new BigDecimal(txtAmount.getText());
	String firstval = "";
	String secondval = "";
	btnDny1.setText(billAmount.toString());
	if ((billAmount.compareTo(new BigDecimal(10))) == 1)
	{
	    btnVal = billAmount.remainder(new BigDecimal(5));
	    tempVal = new BigDecimal(10).subtract(btnVal);
	    btnVal = billAmount.add(tempVal);

	    firstval = gDecimalFormat.format(new Double(billAmount.toString()));
	    secondval = gDecimalFormat.format(new Double(btnVal.toString()));
	    if (btnVal.compareTo(new BigDecimal(50)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("50.00");
		btnDny4.setText("100.00");
	    }
	    else if ((btnVal.compareTo(new BigDecimal(50)) == 1 || btnVal.compareTo(new BigDecimal(50)) == 0) || btnVal.compareTo(new BigDecimal(100)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("100.00");
		btnDny4.setText("200.00");
	    }
	    else if (btnVal.compareTo(new BigDecimal(100)) == 1 && btnVal.compareTo(new BigDecimal(100)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("200.00");
		btnDny4.setText("300.00");
	    }
	    else if (btnVal.compareTo(new BigDecimal(100)) == 1 && btnVal.compareTo(new BigDecimal(500)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("300.00");
		btnDny4.setText("500.00");
	    }
	    else if (btnVal.compareTo(new BigDecimal(500)) == 1 && btnVal.compareTo(new BigDecimal(1000)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("800.00");
		btnDny4.setText("1000.00");
	    }
	    else if (btnVal.compareTo(new BigDecimal(1000)) == 1 && btnVal.compareTo(new BigDecimal(2000)) == -1)
	    {
		btnDny1.setText(firstval.toString());
		btnDny2.setText(secondval.toString());
		btnDny3.setText("2000.00");
		btnDny4.setText("5000.00");
	    }
	}
	else
	{
	    btnDny1.setText(firstval.toString());
	    btnDny2.setText("10.00");
	    btnDny3.setText("50.00");
	    btnDny4.setText("100.00");
	}
    }

    private void funResetVariableValues()
    {
	hmSettlemetnOptions.clear();
	clsGlobalVarClass.gAdvOrderNoForBilling = null;
    }

    private int funUpdateBillTransTablesWithTaxValues(String billNo) throws Exception
    {
	List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();
	double taxTotal = 0;
	for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	{
	    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
	    taxTotal += dblTaxAmt;
	    clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
	    objBillTaxDtl.setStrBillNo(voucherNo);
	    objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
	    objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
	    objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
	    objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
	    objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

	    listObjBillTaxBillDtls.add(objBillTaxDtl);
	}

	double grandTotalAmt = (_subTotal - dblDiscountAmt) + dblTotalTaxAmt + _deliveryCharge;

	//start code to calculate roundoff amount and round off by amt
	Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotalAmt);
	_grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	{
	    grandTotalAmt = mapRoundOff.get("roundOffAmt");
	}
	//end code to calculate roundoff amount and round off by amt

	String sqlUpdateBillHd = "update tblbillhd "
		+ "set dblTaxAmt='" + taxTotal + "' "
		+ ",dblGrandTotal='" + grandTotalAmt + "' "
		+ ",dblRoundOff='" + _grandTotalRoundOffBy + "' "
		+ "where strBillNo='" + billNo + "' "
		+ "and strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlUpdateBillHd);

	funInsertBillTaxDtlTable(listObjBillTaxBillDtls);

	objUtility.funUpdateBillDtlWithTaxValues(billNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	return 1;
    }

    public int funOnlyBillSettle()
    {
	String responseCode = "";
	boolean checkStatus = false;
	clsUtility obj = new clsUtility();
	try
	{
	    String settleBillDate = objUtility.funGetPOSDateForTransaction();
	    int ex = 0, row = 0;
	    String sqlInsertBillSettlementDtl = "insert into tblbillsettlementdtl(strBillNo,strSettlementCode,dblSettlementAmt,dblPaidAmt,"
		    + "strExpiryDate,strCardName,strRemark,strClientCode,strCustomerCode,dblActualAmt,dblRefundAmt,strGiftVoucherCode,strFolioNo,strRoomNo"
		    + ",dteBillDate) "
		    + "values ";
	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		double settleAmt = 0;
		if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
		{
		    settleAmt = ob.getDblSettlementAmt();
		}
		else
		{
		    settleAmt = ob.getDblPaidAmt();
		}

		if (ob.getStrSettelmentType().equals("Debit Card"))
		{
		    objUtility.funDebitCardTransaction(voucherNo, debitCardNo, settleAmt, "Settle");
		    objUtility.funUpdateDebitCardBalance(debitCardNo, settleAmt, "Settle");
		    //funDebitCardTransaction(voucherNo, clsGlobalVarClass.gDebitCardNo, Amount);
		    //funUpdateDebitCardBalance(clsGlobalVarClass.gDebitCardNo, Amount);
		}
		if (ob.getStrSettelmentType().equals("Room"))
		{
		    customerCodeForCredit = ob.getStrGuestCode();
		}
		//Make payment using JioMoney 
		if (clsGlobalVarClass.gJioMoneyIntegrationYN)
		{
		    hmJioMoneySettleDtl = new HashMap<String, clsSettelementOptions>();
		    clsSettelementOptions objSettle = null;
		    if (ob.getStrSettelmentType().equals("JioMoney"))
		    {
			obj.funStartSocketBat();
			if (ob.getStrSettelmentDesc().equals("JM Code"))
			{
			    responseCode = funMakePaymentUsingJioMoneyCode(voucherNo, ob.getDblSettlementAmt());
			    objSettle = new clsSettelementOptions();
			    objSettle.setStrSettelmentCode(ob.getStrSettelmentCode());
			    objSettle.setStrSettelmentDesc(ob.getStrSettelmentDesc());
			    objSettle.setDblSettlementAmt(ob.getDblSettlementAmt());
			    objSettle.setStrRemark(responseCode); //For response code
			    hmJioMoneySettleDtl.put(ob.getStrSettelmentCode(), objSettle);

			}
			else //JioMoney Card
			{
			    if (txtJioMoneyCode.getText().isEmpty())
			    {
				JOptionPane.showMessageDialog(null, "Enter Mobile No!!!");
				return 0;
			    }
			    responseCode = funMakePaymentUsingJioMoneyCard(voucherNo, ob.getDblSettlementAmt());
			    objSettle = new clsSettelementOptions();
			    objSettle.setStrSettelmentCode(ob.getStrSettelmentCode());
			    objSettle.setStrSettelmentDesc(ob.getStrSettelmentDesc());
			    objSettle.setDblSettlementAmt(ob.getDblSettlementAmt());
			    objSettle.setStrRemark(responseCode); //For response code
			    hmJioMoneySettleDtl.put(ob.getStrSettelmentCode(), objSettle);
			}
		    }
		}

		sqlInsertBillSettlementDtl += "('" + voucherNo + "','" + ob.getStrSettelmentCode() + "','" + settleAmt + "','" + ob.getDblPaidAmt() + "',"
			+ "'','" + ob.getStrCardName() + "','" + objUtility.funCheckSpecialCharacters(ob.getStrRemark()) + "','" + clsGlobalVarClass.gClientCode + "','" + customerCodeForCredit + "',"
			+ "'" + ob.getDblActualAmt() + "','" + ob.getDblRefundAmt() + "','" + ob.getStrGiftVoucherCode() + "','" + ob.getStrFolioNo() + "','" + ob.getStrRoomNo() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
		row++;

		if (ob.getStrSettelmentType().equals("Benow"))
		{
		    if (clsGlobalVarClass.gBenowIntegrationYN)
		    {
			String sqlInsertBenowSettlementDtl = "insert into tblbenowsettlementdtl(strBillNo,strQRString,dblSettlementAmount,strTransID,strTransStatus,dteTransDate,strMerchantCode) "
				+ " values ('" + voucherNo + "','" + ob.getStrQRString() + "','" + settleAmt + "','" + ob.getStrTransId() + "' "
				+ " ,'" + ob.getStrTransStatus() + "','" + ob.getStrTransDate() + "','" + ob.getStrMerchantCode() + "')";
			clsGlobalVarClass.dbMysql.execute(sqlInsertBenowSettlementDtl);
		    }
		}

	    }
	    /**
	     * recalculate taxes for settlements
	     */
	    //objUtility.funReCalculateTaxForBill(voucherNo, clsGlobalVarClass.gPOSCode, hmSettlemetnOptions, clsGlobalVarClass.gPOSOnlyDateForTransaction);
	    /**
	     *
	     */

	    if (clsGlobalVarClass.gJioMoneyIntegrationYN)
	    {
		if (hmJioMoneySettleDtl.size() > 0)
		{
		    for (clsSettelementOptions obSettle : hmJioMoneySettleDtl.values())
		    {
			if (obSettle.getStrSettelmentDesc().equals("JM Code"))
			{
			    if (!obSettle.getStrRemark().equals("0000"))
			    {
				checkStatus = true;
			    }
			}
			else
			{
			    if (!obSettle.getStrRemark().equals("0000"))
			    {
				checkStatus = true;
			    }
			}
		    }
		}
	    }

	    if (!checkStatus)
	    {
		DefaultTableModel dmItemTable = new DefaultTableModel();
		dmItemTable.setRowCount(0);
		tblItemTable.setModel(dmItemTable);
		objPannelShowBills.setVisible(true);
		panelSettlement.setVisible(false);
		clsGlobalVarClass.gDebitCardNo = "";

		funTruncateDebitCardTempTable();
		StringBuilder sb1 = new StringBuilder(sqlInsertBillSettlementDtl);
		int index1 = sb1.lastIndexOf(",");
		sqlInsertBillSettlementDtl = sb1.delete(index1, sb1.length()).toString();
		ex = clsGlobalVarClass.dbMysql.execute(sqlInsertBillSettlementDtl);
		if (row > 1)
		{
		    settleName = "MultiSettle";
		}

		String sqlUpdateBillHd = "update tblbillhd set dblTipAmount='" + tipAmount + "' "
			+ "where strBillNo='" + voucherNo + "'";
		clsGlobalVarClass.dbMysql.execute(sqlUpdateBillHd);

		if (ex > 0)
		{
		    sqlUpdateBillHd = "update tblbillhd set strSettelmentMode='" + settleName + "'"
			    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteSettleDate='" + settleBillDate + "' "
			    + ",strRemarks='" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "',strCardNo='" + debitCardNo + "'"
			    + ",strReasonCode='" + selectedReasonCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + "where strBillNo='" + voucherNo + "'";

		    int exc = clsGlobalVarClass.dbMysql.execute(sqlUpdateBillHd);
		    if (exc > 0)
		    {
			printstatus = true;
		    }

		    if (settleType.equals("Credit"))
		    {
			String sql = "update tblbillhd set strCustomerCode='" + customerCodeForCredit + "' "
				+ "where strBillNo='" + voucherNo + "'";
			clsGlobalVarClass.dbMysql.execute(sql);
		    }

		    //*********If the billhd grandstotal and screen grand total amount changes*************//
		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions ob : hmSettlemetnOptions.values())
			{
			    if (!ob.getStrSettelmentType().equals("Complementary"))
			    {
				double billHdGT = funGetBillHdGrandTotal(clsGlobalVarClass.gPOSCode, voucherNo);
				if (_grandTotal != billHdGT)
				{
				    objUtility.funWriteErrorLog(new Exception("Screen Grand Total Amount(" + _grandTotal + ") and BillHd Grand Total Amount(" + billHdGT + ") Not Matching:-BillNo=" + voucherNo + ",Settlement Mode=" + settleName + " ")
				    {
				    });
				    funUpdateBillTransTablesWithTaxValues(voucherNo);
				}
			    }
			}
		    }
		    else
		    {
			double billHdGT = funGetBillHdGrandTotal(clsGlobalVarClass.gPOSCode, voucherNo);
			if (_grandTotal != billHdGT)
			{
			    objUtility.funWriteErrorLog(new Exception("Screen Grand Total Amount(" + _grandTotal + ") and BillHd Grand Total Amount(" + billHdGT + ") Not Matching:-BillNo=" + voucherNo + ",Settlement Mode=" + settleName + " ")
			    {

			    });
			    funUpdateBillTransTablesWithTaxValues(voucherNo);
			}
		    }
		    //****************************************************************************//
		    //update billseriesbilldtl grand total
		    if (clsGlobalVarClass.gEnableBillSeries)
		    {
			clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl set dblGrandTotal='" + _grandTotal + "' where strHdBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
		    }

		    // For Complimentary Bill
		    funClearComplimetaryBillAmt(voucherNo);

		    if (printstatus == true)
		    {
			lblVoucherNo.setText(voucherNo);
			clsGlobalVarClass.funCheckHomeDelivery(voucherNo);
			String tableStatus = funGetTableStatus(tableNo);
			if (clsGlobalVarClass.gHomeDeliveryForSelectedBillNo)
			{
			    objBillSettlementUtility.funUpdateTableStatus(tableStatus);
			    clsGlobalVarClass.gHomeDeliveryForSelectedBillNo = false;
			}
			else
			{
			    objBillSettlementUtility.funUpdateTableStatus(tableStatus);
			}
		    }
		}

		if (clsGlobalVarClass.gBillSettleSMSYN)
		{
		    objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gBillSettlementSMS, "");
		}

		clsGlobalVarClass.gCustomerCode = null;
		clsGlobalVarClass.gCustCodeForAdvOrder = null;

		if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("Sqy"))
		{
		    String sql_CustMb = "select longMobileNo from tblcustomermaster "
			    + "where strCustomerCode='" + custCode + "'";
		    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
		    if (rsCust.next())
		    {
			funSaveCRMPoints(voucherNo, _grandTotal, "Sqy", rsCust.getString(1), settleBillDate);
		    }
		    funCallPostWebService(_grandTotal, "Direct Biller", voucherNo);
		}
		else if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && custCode != null && !custCode.isEmpty())
		{

		    Thread crmThread = new Thread()
		    {
			@Override
			public void run()
			{
			    try
			    {
				String mobileNo = "";
				String sql_CustMb = "select longMobileNo from tblcustomermaster "
					+ "where strCustomerCode='" + custCode + "'";
				ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
				if (rsCust.next())
				{
				    mobileNo = rsCust.getString(1);
				}
				rsCust.close();

				if (mobileNo.trim().isEmpty())
				{
				    System.out.println("No Mobile no");
				    return;
				}
				clsCRMInterface objCRMInterface = new clsCRMInterface();

				objCRMInterface.funPostBillDataCRM(custCode, voucherNo, clsGlobalVarClass.gPOSOnlyDateForTransaction);

				if (!rewardId.isEmpty())
				{
				    objCRMInterface.funPostRewardRedeemCRM(mobileNo, rewardId);
				}//rewardId,2300,2057

				rewardId = "";
			    }
			    catch (Exception e)
			    {
				objUtility.funShowDBConnectionLostErrorMessage(e);	
				e.printStackTrace();
			    }
			}
		    };
		    crmThread.start();
		}
		else
		{
		    String sql_CustMb = "select b.longMobileNo from tblbillhd a,tblcustomermaster b "
			    + "where a.strCustomerCode=b.strCustomerCode and a.strBillNo='" + voucherNo + "'";
		    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
		    if (rsCust.next())
		    {
			funSaveCRMPoints(voucherNo, _grandTotal, "JPOS", rsCust.getString(1), settleBillDate);
		    }
		}

		if (clsGlobalVarClass.gEnablePMSIntegrationYN && settleType.equals("Room"))
		{
		    funInsertPMSPostingBillDtlTable(voucherNo, _grandTotal, hmSettlemetnOptions);
		    clsInvokeDataFromSanguineERPModules objSangERP = new clsInvokeDataFromSanguineERPModules();
		    objSangERP.funPOSTRoomSettlementDtlToPMS(voucherNo, _grandTotal, hmSettlemetnOptions);
		    objSangERP = null;
		}

		if (billPrintOnSettlement.equalsIgnoreCase("Y"))
		{
		    billPrintOnSettlement = "N";
		    objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());

		    /**
		     * save reprint audit
		     */
		    objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint after modification of pending bill.", "", voucherNo, "");
		}

		if (flgMakeKot)
		{
		    kotObj.funClearTable();
		}
		if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		{
		    if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
		    {
			clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
		    }
		}

		//Post Bill Data and Item Details to Inresto POS
		if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
		{
		    funPostBillDataToInrestoPOS();
		}

		if (flgUnsettledBills)
		{

		    funResetVariableValues();

		    //ask to settle next bill for bill series
		    if (clsGlobalVarClass.gEnableBillSeries)//&& !callingFormName.equalsIgnoreCase("Make KOT")
		    {
			clsUtility2 objUtility2 = new clsUtility2();
			String nextBillNo = objUtility2.funGetNextSettleBill(voucherNo);

			if (nextBillNo.trim().length() > 0)
			{
			    frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do You Want To Settle Bill " + nextBillNo + " ?");
			    okOb.setVisible(true);
			    int res = okOb.getResult();
			    if (res == 1)
			    {
				//send settleed bill MSG                   
				String sql1 = "select a.strSendSMSYN,a.longMobileNo "
					+ "from tblsmssetup a "
					+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
					+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
					+ "and a.strTransactionName='SettleBill' "
					+ "and a.strSendSMSYN='Y'; ";
				ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
				if (rsSendSMS.next())
				{
				    String mobileNo = rsSendSMS.getString(2);//mobileNo

				    funSendSettleBillSMS(nextBillNo, mobileNo);

				}
				rsSendSMS.close();
				objPannelShowBills.funFillTableCombo();
				objPannelShowBills.funFillUnsettledBills(nextBillNo);

				if (null != callingFormName)
				{
				    if (callingFormName.equals("Make KOT"))
				    {
					objMakeKOT.funClearTable();
					this.setVisible(true);
				    }
				}
			    }
			    else
			    {
				//send settleed bill MSG                   
				String sql1 = "select a.strSendSMSYN,a.longMobileNo "
					+ "from tblsmssetup a "
					+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
					+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
					+ "and a.strTransactionName='SettleBill' "
					+ "and a.strSendSMSYN='Y'; ";
				ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
				if (rsSendSMS.next())
				{
				    String mobileNo = rsSendSMS.getString(2);//mobileNo

				    funSendSettleBillSMS(voucherNo, mobileNo);

				}
				rsSendSMS.close();
				objPannelShowBills.funFillTableCombo();
				objPannelShowBills.funFillUnsettledBills();

				if (null != callingFormName)
				{
				    if (callingFormName.equals("Make KOT"))
				    {
					objMakeKOT.funClearTable();
				    }
				}
			    }
			}
			else
			{
			    //send settleed bill MSG                   
			    String sql1 = "select a.strSendSMSYN,a.longMobileNo "
				    + "from tblsmssetup a "
				    + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				    + "and a.strTransactionName='SettleBill' "
				    + "and a.strSendSMSYN='Y'; ";
			    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
			    if (rsSendSMS.next())
			    {
				String mobileNo = rsSendSMS.getString(2);//mobileNo

				funSendSettleBillSMS(voucherNo, mobileNo);

			    }
			    rsSendSMS.close();
			    objPannelShowBills.funFillTableCombo();
			    objPannelShowBills.funFillUnsettledBills();

			    if (null != callingFormName)
			    {
				if (callingFormName.equals("Make KOT"))
				{
				    objMakeKOT.funClearTable();
				}
			    }
			}
		    }
		    else
		    {
			//send settleed bill MSG                   
			String sql1 = "select a.strSendSMSYN,a.longMobileNo "
				+ "from tblsmssetup a "
				+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				+ "and a.strTransactionName='SettleBill' "
				+ "and a.strSendSMSYN='Y'; ";
			ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
			if (rsSendSMS.next())
			{
			    String mobileNo = rsSendSMS.getString(2);//mobileNo

			    funSendSettleBillSMS(voucherNo, mobileNo);

			}
			rsSendSMS.close();
			objPannelShowBills.funFillTableCombo();
			objPannelShowBills.funFillUnsettledBills();

			if (null != callingFormName)
			{
			    if (callingFormName.equals("Make KOT"))
			    {
				objMakeKOT.funClearTable();
			    }
			}
		    }
		}
	    }
	    else
	    {
		objPannelShowBills.setVisible(false);
		panelSettlement.setVisible(true);
		btnJioMoneyCheckStatus.setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    e.printStackTrace();

	    if (e.getMessage().startsWith("Duplicate entry"))
	    {
		JOptionPane.showMessageDialog(this, " Bill already settled " + e.getMessage(), "Error Code: BS-21", JOptionPane.ERROR_MESSAGE);
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-21", JOptionPane.ERROR_MESSAGE);
	    }
	}
	finally
	{
	    System.gc();
	    return 0;
	}
    }

    private int funClearComplimetaryBillAmt(String billNo) throws Exception
    {
	if (hmSettlemetnOptions.size() == 1)
	{
	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		if (ob.getStrSettelmentType().equals("Complementary"))
		{
		    funInsertBillComplementryDtlTable(voucherNo);

		    String sqlUpdate = "update tblbilltaxdtl set dblTaxableAmount=0.00,dblTaxAmount=0.00 "
			    + "where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    sqlUpdate = "update tblbillhd set dblTaxAmt=0.00,dblSubTotal=0.00"
			    + ",dblDiscountAmt=0.00,dblDiscountPer=0.00,strReasonCode='" + selectedReasonCode + "'"
			    + ",strRemarks='" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "',dblDeliveryCharges=0.00"
			    + ",strCouponCode='" + couponCode + "',dblGrandTotal=0.00,dblRoundOff=0.00 "
			    + "where strBillNo='" + billNo + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    sqlUpdate = "update tblbilldtl set dblAmount=0.00,dblDiscountAmt=0.00,dblDiscountPer=0.00,dblTaxAmount=0.00 "
			    + "where strBillNo='" + billNo + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    sqlUpdate = "update tblbillmodifierdtl set dblAmount=0.00,dblDiscPer=0.00,dblDiscAmt=0.00 where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    sqlUpdate = "update tblbillsettlementdtl set dblSettlementAmt=0.00,dblPaidAmt=0.00 where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    sqlUpdate = "update tblbillseriesbilldtl set dblGrandTotal=0.00 where strHdBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

		    clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");

		    //send modified bill MSG
		    String sql = "select a.strSendSMSYN,a.longMobileNo "
			    + "from tblsmssetup a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			    + "and a.strTransactionName='ComplementaryBill' "
			    + "and a.strSendSMSYN='Y'; ";
		    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsSendSMS.next())
		    {
			String mobileNo = rsSendSMS.getString(2);//mobileNo

			funSendComplementaryBillSMS(billNo, mobileNo);

		    }
		    rsSendSMS.close();
		}
	    }
	}
	return 1;
    }

    private void funSaveBillForTable()
    {
	try
	{
	    if (voucherNo.length() == 0)
	    {
		DefaultTableModel dmItemTable = new DefaultTableModel();
		dmItemTable.setRowCount(0);
		tblItemTable.setModel(dmItemTable);
		objPannelShowBills.setVisible(true);
		panelSettlement.setVisible(false);
		clsGlobalVarClass.gDebitCardNo = "";

		funSaveWithoutSettleBill();
	    }
	    else
	    {
		funOnlyBillSettle();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-22", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void funSaveBillForBillsFromKOTs()
    {
	StringBuilder sb1 = null;
	try
	{
	    String mergeKOT = "";
	    if (!listBillFromKOT.isEmpty())
	    {
		boolean first = true;
		for (String kot : listBillFromKOT)
		{
		    if (first)
		    {
			mergeKOT += "( strKOTNo='" + kot + "'";
			first = false;
		    }
		    else
		    {
			mergeKOT += " or ".concat(" strKOTNo='" + kot + "' ");
		    }
		}
	    }

	    String operationType = "DineIn";
	    String transactionType = "Dine In";//For saving different transaction on same Bill in tblBillHd table in database
	    voucherNo = lblVoucherNo.getText();
	    revalidate();
	    objBillSettlementUtility.funGenerateBillNo();
	    //last order no
	    int intLastOrderNo = objUtility2.funGetLastOrderNo();

	    funSaveBillDiscountDetail(voucherNo);

	    StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
	    String BillDateTime = sb.substring(0, sb.lastIndexOf(" "));
	    String billDate = sb.substring(0, sb.lastIndexOf(" "));
	    String counterCode = "NA";

	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		counterCode = clsGlobalVarClass.gCounterCode;
	    }

	    int row = 0;
	    String sql_tblbillsettlementdtl = "insert into tblbillsettlementdtl(strBillNo,strSettlementCode,dblSettlementAmt,dblPaidAmt,"
		    + "strExpiryDate,strCardName,strRemark,strClientCode,strCustomerCode,dblActualAmt,dblRefundAmt,strGiftVoucherCode,dteBillDate) "
		    + "values ";
	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		double Amount = 0;
		if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
		{
		    Amount = ob.getDblSettlementAmt();
		}
		else
		{
		    Amount = ob.getDblPaidAmt();
		}

		if (ob.getStrSettelmentType().equals("Debit Card"))
		{
		    objUtility.funDebitCardTransaction(voucherNo, debitCardNo, Amount, "Settle");
		    objUtility.funUpdateDebitCardBalance(debitCardNo, Amount, "Settle");
		}

		sql_tblbillsettlementdtl += "('" + voucherNo + "','" + ob.getStrSettelmentCode() + "','" + Amount + "','" + ob.getDblPaidAmt() + "',"
			+ "'','" + ob.getStrCardName() + "','" + objUtility.funCheckSpecialCharacters(ob.getStrRemark()) + "','" + clsGlobalVarClass.gClientCode + "','" + customerCodeForCredit + "',"
			+ "'" + ob.getDblActualAmt() + "','" + ob.getDblRefundAmt() + "','" + ob.getStrGiftVoucherCode() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
		row++;
	    }
	    sb1 = new StringBuilder(sql_tblbillsettlementdtl);
	    int index1 = sb1.lastIndexOf(",");
	    sql_tblbillsettlementdtl = sb1.delete(index1, sb1.length()).toString();

	    clsGlobalVarClass.dbMysql.execute(sql_tblbillsettlementdtl);
	    if (row > 1)
	    {
		settleName = "MultiSettle";
	    }

	    String sql_ItemDTL = "select strItemCode,upper(strItemName),dblItemQuantity,dblAmount,strKOTNo"
		    + ",strManualKOTNo,Time(dteDateEdited),strCounterCode,strWaiterNo,strCardNo,strPromoCode,tmeOrderProcessing "
		    + " from tblitemrtemp "
		    + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' and " + mergeKOT + ") "
		    + " order by strTableNo ASC";
	    ResultSet rsItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_ItemDTL);
	    String kot = "";
	    String cardNo = "";

	    while (rsItemDtl.next())
	    {
		String iCode = rsItemDtl.getString(1);
		String iName = rsItemDtl.getString(2);
		double iQty = rsItemDtl.getDouble(3);
		double iQty1 = rsItemDtl.getDouble(3);
		String iAmt = rsItemDtl.getString(4);
		kot = rsItemDtl.getString(5);
		String manualKOTNo = rsItemDtl.getString(6);
		cardNo = rsItemDtl.getString(10);
		BillDateTime = billDate + " " + rsItemDtl.getString(7); // Date + Time
		String sqlInsertBillDtl = "";
		String promoCode = rsItemDtl.getString(11);
		String orderProcessingTime = rsItemDtl.getString(12);

		if (!iCode.contains("M"))
		{
		    //double discAmt=hmBillItemDtl.get(iCode).getDiscountAmount();
		    double discAmt = hmBillItemDtl.get(iCode).getDiscountAmount() * iQty;
		    double discPer = hmBillItemDtl.get(iCode).getDiscountPercentage();

		    double rate = Double.parseDouble(iAmt) / iQty;
		    double amt = Double.parseDouble(iAmt);

		    if (hmPromoItem.size() > 0)
		    {
			if (null != hmPromoItem.get(iCode))
			{
			    clsPromotionItems objPromoItemDtl = hmPromoItem.get(iCode);
			    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
			    {
				double freeQty = objPromoItemDtl.getFreeItemQty();
				double freeAmt = freeQty * rate;
				amt = amt - freeAmt;
				if (iQty - freeQty > -1)
				{
				    iQty1 = iQty1 - freeQty;
				    iAmt = String.valueOf(iQty1 * rate);

				    promoCode = objPromoItemDtl.getPromoCode();
				    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount"
					    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
			    }
			    else if (objPromoItemDtl.getPromoType().equals("Discount"))
			    {
				if (objPromoItemDtl.getDiscType().equals("Value"))
				{
				    double amount = objPromoItemDtl.getFreeItemQty() * rate;
				    double promoDiscAmt = objPromoItemDtl.getDiscAmt();

				    promoCode = objPromoItemDtl.getPromoCode();
				    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount"
					    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "'"
					    + ",'" + objPromoItemDtl.getDiscPer() + "','" + promoDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
				else
				{
				    iAmt = String.valueOf(iQty * rate);
				    double amount = iQty * rate;
				    double promoDiscAmt = amount * (objPromoItemDtl.getDiscPer() / 100);

				    promoCode = objPromoItemDtl.getPromoCode();
				    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount "
					    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "'"
					    + ",'" + objPromoItemDtl.getDiscPer() + "','" + promoDiscAmt + "'"
					    + ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
			    }
			}
		    }

		    if (iName.startsWith("=>"))
		    {
			sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				+ ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo,strClientCode"
				+ ",strManualKOTNo,tdhYN,strCounterCode,strWaiterNo,dblDiscountAmt,dblDiscountPer"
				+ ",dtBillDate,tmeOrderProcessing) "
				+ "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ",'" + iQty
				+ "','" + amt + "','0.00','" + BillDateTime + "','" + kot + "','"
				+ clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','Y'"
				+ ",'" + rsItemDtl.getString(8) + "','" + rsItemDtl.getString(9) + "'"
				+ ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "')";
		    }
		    else
		    {
			sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				+ ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo,strClientCode"
				+ ",strManualKOTNo,strCounterCode,strWaiterNo,dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing) "
				+ "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ",'" + iQty
				+ "','" + amt + "','0.00','" + BillDateTime + "','" + kot + "','"
				+ clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "'"
				+ ",'" + rsItemDtl.getString(8) + "','" + rsItemDtl.getString(9) + "'"
				+ ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "')";
		    }
		    clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
		}

		int ex = 0;
		if (iCode.contains("M"))
		{
		    sb1 = new StringBuilder(iCode);
		    int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
		    String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code
		    double rate = Double.parseDouble(iAmt) / iQty;
		    double amt = Double.parseDouble(iAmt);
		    double discAmt = hmBillItemDtl.get(iCode + "!" + iName).getDiscountAmount() * iQty;
		    double discPer = hmBillItemDtl.get(iCode + "!" + iName).getDiscountPercentage();
		    String sql_Modifier = "insert into  tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
			    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
			    + "values('" + voucherNo + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
			    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "','" + discPer + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "' )";
		    ex = clsGlobalVarClass.dbMysql.execute(sql_Modifier);
		}
		if (ex > 0)
		{
		    printstatus = true;
		}
	    }
	    rsItemDtl.close();
	    transactionType = transactionType + "," + "Bill From KOT";
	    String customerCode = "";
	    intBillSeriesPaxNo = paxNo;
	    String sqlBillHd = "insert into tblbillhd(strBillNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
		    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType,"
		    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strTableNo"
		    + ",strWaiterNo,strCustomerCode,intShiftCode,intPaxNo,strReasonCode,strRemarks"
		    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strAreaCode"
		    + ",strDiscountRemark,strTakeAwayRemarks,strDiscountOn,strCardNo,strTransactionType,dblRoundOff,intBillSeriesPaxNo,dtBillDate"
		    + ",intOrderNo,strCRMRewardId,dblUSDConverionRate ) "
		    + "values('" + voucherNo + "','" + objUtility.funGetPOSDateForTransaction() + "','"
		    + clsGlobalVarClass.gPOSCode + "','" + settleName + "','" + dblDiscountAmt + "','"
		    + dblDiscountPer + "','" + dblTotalTaxAmt + "','" + _subTotal + "','"
		    + _grandTotal + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
		    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.gClientCode + "','" + tableNo + "','" + waiterNo + "'"
		    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
		    + "," + paxNo + ",'" + selectedReasonCode + "','" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "'"
		    + "," + tipAmount + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
		    + ",'" + counterCode + "'," + _deliveryCharge + ", '" + areaCode + "'"
		    + ",'" + discountRemarks + "','','','" + cardNo + "','" + transactionType + "'"
		    + ",'" + _grandTotalRoundOffBy + "','" + intBillSeriesPaxNo + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",'" + intLastOrderNo + "','" + rewardId + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlBillHd);

	    if (settleType.equals("Credit"))
	    {
		String sql = "update tblbillhd set strCustomerCode='" + customerCodeForCredit + "' "
			+ "where strBillNo='" + voucherNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    //update billseriesbilldtl grand total
	    if (clsGlobalVarClass.gEnableBillSeries)
	    {
		clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl set dblGrandTotal='" + _grandTotal + "' where strHdBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
	    }

	    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

	    // insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		//totalTaxAmt = totalTaxAmt + dblTaxAmt;
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(voucherNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    // Form Complimentary Bill    
	    funClearComplimetaryBillAmt(voucherNo);

	    objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());

	    String sqlDeleteKOT = "delete from tblitemrtemp where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and " + mergeKOT + ")";
	    clsGlobalVarClass.dbMysql.execute(sqlDeleteKOT);
	    //insert into itemrtempbck table
	    objUtility.funInsertIntoTblItemRTempBckForMergeKOTs(clsGlobalVarClass.gPOSCode, mergeKOT + ")");
	    funCheckTableStatus();

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && custCode != null && !custCode.isEmpty())
	    {
		Thread crmThread = new Thread()
		{
		    @Override
		    public void run()
		    {
			try
			{
			    String mobileNo = "";
			    String sql_CustMb = "select longMobileNo from tblcustomermaster "
				    + "where strCustomerCode='" + custCode + "'";
			    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
			    if (rsCust.next())
			    {
				mobileNo = rsCust.getString(1);
			    }
			    rsCust.close();

			    if (mobileNo.trim().isEmpty())
			    {
				System.out.println("No Mobile no");
				return;
			    }
			    clsCRMInterface objCRMInterface = new clsCRMInterface();

			    objCRMInterface.funPostBillDataCRM(custCode, voucherNo, clsGlobalVarClass.gPOSOnlyDateForTransaction);

			    if (!rewardId.isEmpty())
			    {
				objCRMInterface.funPostRewardRedeemCRM(mobileNo, rewardId);
			    }//rewardId,2300,2057

			    rewardId = "";
			}
			catch (Exception e)
			{
			    objUtility.funShowDBConnectionLostErrorMessage(e);	
			    e.printStackTrace();
			}
		    }
		};
		crmThread.start();

	    }

	    funResetVariableValues();
	    objPannelShowKOTs.funFillGridWithKOT();
	    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
	    {
		if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
		{
		    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-24", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    sb1 = null;
	    System.gc();
	}
    }

    private int funCheckTableStatus() throws Exception
    {
	int rows = 0;
	String sql = "update tbltablemaster set strStatus='Normal' "
		+ " where strTableNo not in(select strTableNo from tblitemrtemp where strPOSCode='" + clsGlobalVarClass.gPOSCode + "') "
		+ " and strStatus='Occupied'";
	rows = clsGlobalVarClass.dbMysql.execute(sql);
	return rows;
    }

    private double funGetDiscountOnAmt(String discountOnType, String discountOnValue, double oldDiscPer)
    {
	double discOnAmt = 0.00;
	try
	{
	    StringBuilder sqlItemDiscAmt = new StringBuilder();
	    StringBuilder sqlBillItemDiscAmt = new StringBuilder();
	    List<String> listKOTs = objAddKOTToBill.getList_Selected_KOTs();
	    String kotNos = "";
	    for (int i = 0; i < listKOTs.size(); i++)
	    {
		if (i == 0)
		{
		    kotNos += "'" + listKOTs.get(i) + "'";
		}
		else
		{
		    kotNos += ",'" + listKOTs.get(i) + "'";
		}
	    }
	    if (discountOnType.equalsIgnoreCase("Total"))
	    {
		sqlItemDiscAmt.append("select a.strItemCode,a.dblAmount "
			+ "from tblitemrtemp a "
			+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			+ "where  b.strDiscountApply='Y' "
			+ "and a.strKOTNo IN(" + kotNos + ") ");
		sqlBillItemDiscAmt.append("select a.strItemCode,a.strItemName,a.dblAmount "
			+ "from tblbilldtl a "
			+ "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ "where b.strDiscountApply='Y' "
			+ "and strBillNo='" + voucherNo + "' ");
	    }
	    else if (discountOnType.equalsIgnoreCase("GroupWise"))
	    {
		sqlItemDiscAmt.append("select a.strItemCode,a.dblAmount,d.strGroupName "
			+ "from tblitemrtemp a "
			+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			+ "where  b.strDiscountApply='Y' "
			+ "and a.strKOTNo IN(" + kotNos + ") "
			+ "and d.strGroupName='" + discountOnValue + "' ");
		sqlBillItemDiscAmt.append("select a.strItemCode,a.strItemName,a.dblAmount,c.strSubGroupCode,c.strSubGroupName,d.strGroupCode,d.strGroupName "
			+ "from tblbilldtl a "
			+ "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			+ "where b.strDiscountApply='Y' "
			+ "and d.strGroupName='" + discountOnValue + "' "
			+ "and strBillNo='" + voucherNo + "' ");
	    }
	    else if (discountOnType.equalsIgnoreCase("SubGroupWise"))
	    {
		sqlItemDiscAmt.append("select a.strItemCode,a.dblAmount,c.strSubGroupName "
			+ "from tblitemrtemp a "
			+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ "where  b.strDiscountApply='Y' "
			+ "and a.strKOTNo IN(" + kotNos + ") "
			+ "and c.strSubGroupName='" + discountOnValue + "' ");
		sqlBillItemDiscAmt.append("select a.strItemCode,a.strItemName,a.dblAmount,c.strSubGroupCode,c.strSubGroupName,d.strGroupCode,d.strGroupName "
			+ "from tblbilldtl a "
			+ "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			+ "where b.strDiscountApply='Y' "
			+ "and c.strSubGroupName='" + discountOnValue + "' "
			+ "and strBillNo='" + voucherNo + "' ");
	    }
	    else if (discountOnType.equalsIgnoreCase("ItemWise"))
	    {
		sqlItemDiscAmt.append("select a.strItemCode,a.dblAmount,a.strItemName "
			+ "from tblitemrtemp a "
			+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			+ "where  b.strDiscountApply='Y' "
			+ "and a.strKOTNo IN(" + kotNos + ") "
			+ "and a.strItemName='" + discountOnValue + "' ");
		sqlBillItemDiscAmt.append("select a.strItemCode,a.strItemName,a.dblAmount "
			+ "from tblbilldtl a "
			+ "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ "where b.strDiscountApply='Y' "
			+ "and a.strItemName='" + discountOnValue + "'  "
			+ "and strBillNo='" + voucherNo + "' ");
	    }
	    //calculate kot discount
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDiscAmt.toString());
	    while (resultSet.next())
	    {
		discOnAmt += Double.parseDouble(resultSet.getString("dblAmount"));
		clsBillItemDtl billItemDtl = hmBillItemDtl.get(resultSet.getString("strItemCode").toUpperCase());
		double itemAmount = billItemDtl.getAmount();
		double itemDiscount = (oldDiscPer / 100) * itemAmount;
		billItemDtl.setDiscountAmount(itemDiscount);
		billItemDtl.setDiscountPercentage(oldDiscPer);
	    }
	    //calculate bill discount
	    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillItemDiscAmt.toString());
	    while (resultSet.next())
	    {
		discOnAmt += Double.parseDouble(resultSet.getString("dblAmount"));
		clsBillItemDtl billItemDtl = hmBillItemDtl.get(resultSet.getString("strItemCode").toUpperCase());
		double itemAmount = billItemDtl.getAmount();
		double itemDiscount = (oldDiscPer / 100) * itemAmount;
		billItemDtl.setDiscountAmount(itemDiscount);
		billItemDtl.setDiscountPercentage(oldDiscPer);

		if (!discountOnType.equalsIgnoreCase("ItemWise"))
		{
		    //add modifier dtl
		    String sqlModi = "SELECT strItemCode,dblAmount "
			    + "FROM tblbillmodifierdtl "
			    + "WHERE strBillNo='" + voucherNo + "' "
			    + "AND LEFT(strItemCode,7)='" + resultSet.getString("strItemCode") + "' ;";
		    ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sqlModi);
		    while (rsModi.next())
		    {
			discOnAmt += Double.parseDouble(rsModi.getString("dblAmount"));
			billItemDtl = hmBillItemDtl.get(rsModi.getString("strItemCode").toUpperCase());
			itemAmount = billItemDtl.getAmount();
			itemDiscount = (oldDiscPer / 100) * itemAmount;
			billItemDtl.setDiscountAmount(itemDiscount);
			billItemDtl.setDiscountPercentage(oldDiscPer);
		    }
		}
	    }
	    if (discountOnType.equalsIgnoreCase("ItemWise"))
	    {
		//add modifier dtl
		String sqlModi = "SELECT a.strItemCode,a.strModifierName,a.dblAmount "
			+ "FROM tblbillmodifierdtl a "
			+ "LEFT OUTER "
			+ "JOIN tblitemmaster b ON left(a.strItemCode,7)=b.strItemCode "
			+ "WHERE b.strDiscountApply='Y' AND a.strModifierName='" + discountOnValue + "'   and strBillNo='" + voucherNo + "' ";
		ResultSet rsModi = clsGlobalVarClass.dbMysql.executeResultSet(sqlModi);
		while (rsModi.next())
		{
		    discOnAmt += Double.parseDouble(rsModi.getString("dblAmount"));
		    clsBillItemDtl billItemDtl = hmBillItemDtl.get(rsModi.getString("strItemCode").toUpperCase());
		    double itemAmount = billItemDtl.getAmount();
		    double itemDiscount = (oldDiscPer / 100) * itemAmount;
		    billItemDtl.setDiscountAmount(itemDiscount);
		    billItemDtl.setDiscountPercentage(oldDiscPer);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	return discOnAmt;
    }

    private void funReCalculateBillDiscount()
    {
	try
	{
	    String billNo = lblVoucherNo.getText();
	    clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + billNo + "' ");
	    if (mapBillDiscDtl.size() > 0)
	    {
		Iterator<String> it = mapBillDiscDtl.keySet().iterator();
		String key = it.next();
		String discountOnType = key.split("!")[0];
		String discountOnValue = key.split("!")[1];
		clsBillDiscountDtl billDiscountDtl = mapBillDiscDtl.get(key);
		double oldDiscPer = billDiscountDtl.getDiscPer();
		double newDiscOnAmt = 0.00;
		double newDiscAmt = 0.00;

		if (discountOnType.equalsIgnoreCase("Total"))
		{
		    rdbAll.setSelected(true);
		    newDiscOnAmt = funGetDiscountOnAmt(discountOnType, discountOnValue, oldDiscPer);
		}
		else if (discountOnType.equalsIgnoreCase("GroupWise"))
		{
		    rdbGroupWise.setSelected(true);
		    newDiscOnAmt = funGetDiscountOnAmt(discountOnType, discountOnValue, oldDiscPer);
		}
		else if (discountOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    rdbSubGroupWise.setSelected(true);
		    newDiscOnAmt = funGetDiscountOnAmt(discountOnType, discountOnValue, oldDiscPer);
		}
		else if (discountOnType.equalsIgnoreCase("ItemWise"))
		{
		    rdbItemWise.setSelected(true);
		    newDiscOnAmt = funGetDiscountOnAmt(discountOnType, discountOnValue, oldDiscPer);
		}
		//newDiscOnAmt = oldDiscOnAmt+newDiscOnAmt;
		newDiscAmt = (oldDiscPer / 100) * newDiscOnAmt;
		billDiscountDtl.setDiscOnAmt(newDiscOnAmt);
		billDiscountDtl.setDiscAmt(newDiscAmt);
	    }

	    //clear old data for this billDiscountDtl table
	    String sql = "delete from tblbilldiscdtl where strBillNo='" + voucherNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    funSaveBillDiscountDetail(voucherNo);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funAddKOTToBill()
    {
	try
	{
	    //Bill Series Code
	    Map<String, List<clsBillItemDtl>> mapBillSeries = null;
		listBillSeriesBillDtl = new ArrayList<clsBillSeriesBillDtl>();
		voucherNo = lblVoucherNo.getText();
		 String operationType = "";
		String transactionType = "";//For saving different transaction on same Bill in tblBillHd table in database
		Boolean HomeDevlPrint = false;
		voucherNo = lblVoucherNo.getText();
		//last order no
		int intLastOrderNo = 0,pax=0;
		StringBuilder sb = new StringBuilder(clsGlobalVarClass.gPOSDateForTransaction);
		String counterCode = "NA";
		if (clsGlobalVarClass.gCounterWise.equals("Yes"))
		{
		    counterCode = clsGlobalVarClass.gCounterCode;
		}

		if (clsGlobalVarClass.gEnableBillSeries && (mapBillSeries = objBillSettlementUtility.funGetBillSeriesList()).size() > 0)
		{
		    if (mapBillSeries.containsKey("NoBillSeries"))
		    {
			new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
			objBillSettlementUtility.funUpdateTableStatus("Occupied");
			return;
		    }
		    
		    List<String> listBillForTable= new ArrayList<>();
		    listBillForTable.add(voucherNo);
		    String[] dtlBillNos = null;
		    String sql = "select a.strPOSCode,a.strHdBillNo,a.strDtlBillNos "
		    + "from tblbillseriesbilldtl a  "
		    + "where a.strHdBillNo='" + voucherNo + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
		    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsBillType.next())
		    {
			dtlBillNos = rsBillType.getString(3).split(",");
		    }
		    rsBillType.close();
		    
		    if(dtlBillNos!=null){
			for(String billNo:dtlBillNos){
			    if(!billNo.isEmpty()){
				listBillForTable.add(billNo);
			    }
			}
		    }
		   // Iterator<Map.Entry<String, List<clsBillItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
		    Map<String,String> mapPrefixBillNo=new HashMap<>();
		  
		    
		    for(String billPrefix:mapBillSeries.keySet()){
			for (String newBillNo:listBillForTable)
			{
			    if(newBillNo.startsWith(billPrefix)){
				mapPrefixBillNo.put(billPrefix,newBillNo);
			    }
			}
		    }
		    
		    for(String billPrefix:mapBillSeries.keySet()){
			if(mapPrefixBillNo.containsKey(billPrefix)){
			    funGenerateBillNoForBillSeriesForAddKOTToBill(mapPrefixBillNo.get(billPrefix), mapBillSeries.get(billPrefix));
			}else{
			    funGenerateBillNoForBillSeriesForMakeKOT(billPrefix, mapBillSeries.get(billPrefix));
			}
		    }
		    			
//clear temp kot table
			if (flagBillForItems)
			{
			    objBillSettlementUtility.funUpdateKOTTempTable();
			}
			else
			{
			    objBillSettlementUtility.funTruncateKOTTempTable();
			}
			
			/**
			 * ***********************************************
			 * Update table status code
			 * ***********************************************
			 */
			
			    boolean flag = true;
			    sql = "select strTableNo from tblitemrtemp where strTableNo='" + tableNo + "';";
			    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rs.next())
			    {
				flag = false;
			    }
			    rs.close();
			    if (flag)
			    {
				String sqlTableStatus = "";
				sqlTableStatus = "update tbltablemaster set strStatus='Billed' where strTableNo='" + tableNo + "';";
				clsGlobalVarClass.dbMysql.execute(sqlTableStatus);
				
//				sqlTableStatus = "update tbltablemaster set strStatus='Normal' where strTableNo='" + table + "';";
//				clsGlobalVarClass.dbMysql.execute(sqlTableStatus);
				
			    }
			

			/**
			 * ******************************************
			 * End Of Update table status code
			 * *******************************************
			 */

			//save bill series bill detail
			for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
			{
			    clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			    String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			    double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();
			    clsGlobalVarClass.dbMysql.execute("delete from tblbillseriesbilldtl where strHdBillNo='" + hdBillNo + "' ");
			    String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl "
				    + "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag"
				    + ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) "
				    + "values ('" + clsGlobalVarClass.gPOSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'"
				    + ",'" + hdBillNo + "','" + objBillSettlementUtility.funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'"
				    + ",'" + grandTotal + "'" + ",'" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + objUtility.funGetPOSDateForTransaction() + "')";
			    clsGlobalVarClass.dbMysql.execute(sqlInsertBillSeriesDtl);

			     sql = "select * "
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
			    clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			    String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			    boolean flgHomeDelPrint = objBillSeriesBillDtl.isFlgHomeDelPrint();
			    if (strButtonClicked.equals("Settle"))
			    {
				if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && isDirectSettleFromMakeBill)
				{
				    funOnlyBillSettle();
				    if (clsGlobalVarClass.gHomeDelSMSYN)
				    {
					objBillSettlementUtility.funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
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
					objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
				    }
				}
				else
				{
				    funOnlyBillSettle();
				}
			    }
			    else if (strButtonClicked.equals("Print"))
			    {
				if (clsGlobalVarClass.gHomeDelSMSYN)
				{
				    objBillSettlementUtility.funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
				}
				if (flgHomeDelPrint == true)
				{
				    objUtility.funPrintBill(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				}
				else
				{
				    objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
				}
			    }
			    if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 8") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 9") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 21")|| clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 22")
				    || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 11"))//XO
			    {
				break;
			    }

			}
 
		}
		else{
		    String sql1 = "select strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
		    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		    + ",strTableNo,strWaiterNo,strCustomerCode,intPaxNo,strReasonCode,strRemarks"
		    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strTransactionType,intOrderNo,strCRMRewardId "
		    + "from tblbillhd where strBillNo='" + voucherNo + "';";

		    ResultSet rsPrevBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
		    String customerCode = "";
		    while (rsPrevBillDtl.next())
		    {
			customerCode = rsPrevBillDtl.getString(14);
			tableNo = rsPrevBillDtl.getString(12);
			waiterNo = rsPrevBillDtl.getString(13);
			counterCode = rsPrevBillDtl.getString(20);
			operationType = rsPrevBillDtl.getString(11);
			transactionType = rsPrevBillDtl.getString(22);
			transactionType = rsPrevBillDtl.getString(22) + "," + "Add KOT To Bill";
			intLastOrderNo = rsPrevBillDtl.getInt(23);
			rewardId = rsPrevBillDtl.getString(24);
		    }
		    rsPrevBillDtl.close();
		    String deletePrevBill = "delete from tblbillhd where strBillNo='" + voucherNo + "';";
		    clsGlobalVarClass.dbMysql.execute(deletePrevBill);

		    //funReCalculateBillDiscount();
		    //clear old data for this billDiscountDtl table             
		    clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + voucherNo + "' ");
		    funSaveBillDiscountDetail(voucherNo);

		    //Insert into tblbillhd table
		    clsBillHd objBillHd = new clsBillHd();
		    objBillHd.setStrBillNo(voucherNo);
		    objBillHd.setStrAdvBookingNo(advOrderBookingNo);
		    objBillHd.setDteBillDate(objUtility.funGetPOSDateForTransaction());
		    objBillHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
		    objBillHd.setStrSettelmentMode("");
		    objBillHd.setDblDiscountAmt(dblDiscountAmt);
		    objBillHd.setDblDiscountPer(dblDiscountPer);
		    objBillHd.setDblTaxAmt(dblTotalTaxAmt);
		    objBillHd.setDblSubTotal(_subTotal);
		    objBillHd.setDblGrandTotal(_grandTotal);
		    objBillHd.setDblGrandTotalRoundOffBy(_grandTotalRoundOffBy);
		    objBillHd.setStrTakeAway(takeAway);
		    objBillHd.setStrOperationType(operationType);
		    objBillHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
		    objBillHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
		    objBillHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
		    objBillHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
		    objBillHd.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objBillHd.setStrTableNo(tableNo);
		    objBillHd.setStrWaiterNo(waiterNo);
		    objBillHd.setStrCustomerCode(customerCode);
		    objBillHd.setStrManualBillNo(txtManualBillNo.getText());
		    objBillHd.setIntShiftCode(clsGlobalVarClass.gShiftNo);
		    objBillHd.setIntPaxNo(paxNo);
		    objBillHd.setStrDataPostFlag("N");
		    objBillHd.setStrReasonCode(selectedReasonCode);
		    objBillHd.setStrRemarks(objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()));
		    objBillHd.setDblTipAmount(Double.parseDouble(txtTip.getText().trim()));
		    objBillHd.setDteSettleDate(objUtility.funGetPOSDateForTransaction());
		    objBillHd.setStrCounterCode(counterCode);
		    objBillHd.setDblDeliveryCharges(_deliveryCharge);
		    objBillHd.setStrAreaCode(areaCode);
		    objBillHd.setStrDiscountRemark(objUtility.funCheckSpecialCharacters(discountRemarks));
		    objBillHd.setStrTakeAwayRemarks(objUtility.funCheckSpecialCharacters(takeAwayRemarks));
		    String discountOn = "All";
		    objBillHd.setStrDiscountOn(discountOn);
		    objBillHd.setStrCardNo("");
		    objBillHd.setStrTransactionType(transactionType);
		    objBillHd.setIntBillSeriesPaxNo(intBillSeriesPaxNo);
		    objBillHd.setIntLastOrderNo(intLastOrderNo);
		    objBillHd.setDblUSDConvertionRate(clsGlobalVarClass.gUSDConvertionRate);

		    funInsertBillHdTable(objBillHd);

		    funUpdateBillSeriesGrandTotal(clsGlobalVarClass.gPOSCode, voucherNo, _grandTotal);

		    String sqlAppendForBillFromKOTS = "";
		    List<String> listKOTNos = new ArrayList<String>();
		    if (flagAddKOTstoBill)
		    {
			sqlAppendForBillFromKOTS = "";
			listKOTNos = objAddKOTToBill.getList_Selected_KOTs();
			if (!listKOTNos.isEmpty())
			{
			    boolean first = true;
			    for (String kot : listKOTNos)
			    {
				if (first)
				{
				    sqlAppendForBillFromKOTS += "( strKOTNo='" + kot + "'";
				    first = false;
				}
				else
				{
				    sqlAppendForBillFromKOTS += " or ".concat(" strKOTNo='" + kot + "' ");
				}
			    }
			}

			Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
			if (hmComplimentaryBillItemDtl.size() > 0)
			{
			    hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
			    for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
			    {
				hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
			    }
			}

			List<String> listBillItemDtl = new ArrayList<String>();
			StringBuilder sqlInsertBillDtlValues = new StringBuilder();
			String sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				+ ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
				+ ",strClientCode,strManualKOTNo,tdhYN,strWaiterNo,dblDiscountAmt,dblDiscountPer,dtBillDate"
				+ ",tmeOrderProcessing) "
				+ "values ";

			StringBuilder sqInsertBillModifierDtlValues = new StringBuilder();
			String sqlInsertBillModifierDtl = "insert into  tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
				+ "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
				+ "values ";

			String sql = "select strItemCode,upper(strItemName),sum(Qty),sum(Amt),dblRate,DiscAmt,DiscPer,strKOTNo,strManualKOTNo,strKotDateTime,strWaiterNo,tmeOrderProcessing  "
				+ "from (select strItemCode,strItemName,dblQuantity as Qty,dblAmount as Amt ,dblDiscountAmt as DiscAmt "
				+ ",dblDiscountPer as DiscPer,dblRate,strKOTNo,strManualKOTNo,dteBillDate as strKotDateTime,strWaiterNo,tmeOrderProcessing "
				+ "from tblbilldtl where strBillNo='" + voucherNo + "'  "
				+ "union all  "
				+ "select strItemCode,strItemName,dblItemQuantity as Qty,dblAmount as Amt ,0 as DiscAmt,0 as DiscPer "
				+ ",dblRate,strKOTNo,strManualKOTNo,dteDateCreated as strKotDateTime,strWaiterNo,tmeOrderProcessing "
				+ "from tblitemrtemp where " + sqlAppendForBillFromKOTS + ")) a "
				+ "Group By tmeOrderProcessing,strItemCode,strItemName";

			ResultSet rsItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rsItemDtl.next())
			{
			    String itemCode = rsItemDtl.getString(1);
			    String itemName = rsItemDtl.getString(2);
			    double itemQty = rsItemDtl.getDouble(3);
			    double itemAmt = rsItemDtl.getDouble(4);
			    double itemRate = rsItemDtl.getDouble(5);
			    double itemDiscAmt = rsItemDtl.getDouble(6);
			    double itemDiscPer = rsItemDtl.getDouble(7);
			    String kotNO = rsItemDtl.getString(8);
			    String manualKOTNo = rsItemDtl.getString(9);
			    String kotDateTime = rsItemDtl.getString(10);
			    String waiterNo = rsItemDtl.getString(11);
			    String orderProcessingTime = rsItemDtl.getString(12);

			    if (null != hmComplimentaryBillItemDtlTemp && hmComplimentaryBillItemDtlTemp.containsKey(itemCode))
			    {
				double complQty = hmComplimentaryBillItemDtlTemp.get(itemCode).getDblComplQty();
				double complAmt = hmComplimentaryBillItemDtlTemp.get(itemCode).getDblComplQty() * hmComplimentaryBillItemDtlTemp.get(itemCode).getDblRate();
				if (itemAmt >= complAmt)
				{
				    if (complQty == itemQty || complQty < itemQty)
				    {
					itemAmt = itemAmt - (hmComplimentaryBillItemDtlTemp.get(itemCode).getDblComplQty() * hmComplimentaryBillItemDtlTemp.get(itemCode).getDblRate());
					hmComplimentaryBillItemDtlTemp.remove(itemCode);
				    }
				    else if (itemQty < complQty)
				    {
					itemAmt = itemAmt - (itemQty * hmComplimentaryBillItemDtlTemp.get(itemCode).getDblRate());
					double newComplQty = complQty - itemQty;
					hmComplimentaryBillItemDtlTemp.get(itemCode).setDblComplQty(newComplQty);
				    }
				}
			    }

			    if (hmComplimentaryBillItemDtl.containsKey(itemCode))
			    {
				clsBillDtl objComplBillDtl = hmComplimentaryBillItemDtl.get(itemCode);
				objComplBillDtl.setDblRate(itemRate);
				objComplBillDtl.setStrBillNo(voucherNo);
				objComplBillDtl.setDteBillDate(kotDateTime);
				objComplBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				objComplBillDtl.setStrManualKOTNo(manualKOTNo);
				objComplBillDtl.setStrPromoCode("");
				objComplBillDtl.setStrCounterCode(counterCode);
				objComplBillDtl.setStrWaiterNo(waiterNo);
				objComplBillDtl.setDblDiscountAmt(0);
				objComplBillDtl.setDblDiscountPer(0);
				objComplBillDtl.setDblTaxAmount(0.00);
				objComplBillDtl.setDteBillSettleDate(kotDateTime);
				hmComplimentaryBillItemDtl.put(itemCode, objComplBillDtl);

				listBillItemDtl.add(itemCode);
			    }

			    if (!itemCode.contains("M"))
			    {
				itemQty = hmBillItemDtl.get(itemCode).getQuantity();
				itemAmt = hmBillItemDtl.get(itemCode).getAmount();
				itemDiscPer = hmBillItemDtl.get(itemCode).getDiscountPercentage();
				itemDiscAmt = (itemDiscPer / 100) * itemAmt;

				sqlInsertBillDtlValues.append(",('" + itemCode + "','" + itemName + "','" + voucherNo + "'," + itemRate + ",'" + itemQty
					+ "','" + itemAmt + "','0.00','" + kotDateTime + "','" + kotNO + "','"
					+ clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','N'"
					+ ",'" + waiterNo + "','" + itemDiscAmt + "','" + itemDiscPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "')");
			    }
			    int ex = 0;
			    if (itemCode.contains("M"))
			    {
				StringBuilder sb1 = new StringBuilder(itemCode);
				int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
				String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code

				if (hmBillItemDtl.containsKey(itemCode + "!" + itemName))
				{
				    itemQty = hmBillItemDtl.get(itemCode + "!" + itemName).getQuantity();
				    itemAmt = hmBillItemDtl.get(itemCode + "!" + itemName).getAmount();
				    itemDiscPer = hmBillItemDtl.get(itemCode + "!" + itemName).getDiscountPercentage();
				    itemDiscAmt = (itemDiscPer / 100) * itemAmt;
				}

				StringBuilder sbTemp = new StringBuilder(itemCode);
				if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
				{
				    itemAmt = 0;
				}
				sqInsertBillModifierDtlValues.append(",('" + voucherNo + "','" + itemCode + "','" + modifierCode + "','" + itemName + "'"
					+ "," + itemRate + ",'" + itemQty + "','" + itemAmt + "','" + clsGlobalVarClass.gClientCode + "'"
					+ ",'" + itemDiscPer + "','" + itemDiscAmt + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "')");
			    }
			    if (ex > 0)
			    {
				printstatus = true;
			    }
			}
			rsItemDtl.close();

			String sqlBillModifiers = "select * from tblbillmodifierdtl where strBillNo='" + voucherNo + "' ";
			ResultSet rsBillModifiers = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillModifiers);
			while (rsBillModifiers.next())
			{
			    String itemCode = rsBillModifiers.getString(2);
			    String modifierCode = rsBillModifiers.getString(3);
			    String modifierName = rsBillModifiers.getString(4);
			    String itemRate = rsBillModifiers.getString(5);

			    if (hmBillItemDtl.containsKey(itemCode + "!" + modifierName))
			    {
				double itemQty = hmBillItemDtl.get(itemCode + "!" + modifierName).getQuantity();
				double itemAmt = hmBillItemDtl.get(itemCode + "!" + modifierName).getAmount();
				double itemDiscPer = hmBillItemDtl.get(itemCode + "!" + modifierName).getDiscountPercentage();
				double itemDiscAmt = (itemDiscPer / 100) * itemAmt;

				StringBuilder sbTemp = new StringBuilder(itemCode);
				if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
				{
				    itemAmt = 0;
				}

				sqInsertBillModifierDtlValues.append(",('" + voucherNo + "','" + itemCode + "','" + modifierCode + "','" + modifierName + "'"
					+ "," + itemRate + ",'" + itemQty + "','" + itemAmt + "','" + clsGlobalVarClass.gClientCode + "'"
					+ ",'" + itemDiscPer + "','" + itemDiscAmt + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "')");
			    }
			}

			if (sqlInsertBillDtlValues.length() > 0)
			{
			    sqlInsertBillDtlValues.deleteCharAt(0);
			    sqlInsertBillDtl += sqlInsertBillDtlValues;
			    //delete old data
			    clsGlobalVarClass.dbMysql.execute("delete from tblbilldtl where strBillNo='" + voucherNo + "' ");
			    //insert new data
			    clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);

			    if (hmComplimentaryBillItemDtl.size() > 0)
			    {
				funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
			    }
			}
			if (sqInsertBillModifierDtlValues.length() > 0)
			{
			    sqInsertBillModifierDtlValues.deleteCharAt(0);
			    sqlInsertBillModifierDtl += sqInsertBillModifierDtlValues;
			    //delete old data                   
			    clsGlobalVarClass.dbMysql.execute("delete from tblbillmodifierdtl where strBillNo='" + voucherNo + "' ");
			    //insert new data                    
			    clsGlobalVarClass.dbMysql.execute(sqlInsertBillModifierDtl);
			}

			clsGlobalVarClass.dbMysql.execute("delete from tblbillpromotiondtl where strBillNo='" + voucherNo + "'");
			for (clsBillItemDtl objBillItemDtl : hmBillItemDtl.values())
			{
			    if (hmPromoItem.size() > 0)
			    {
				String iCode = objBillItemDtl.getItemCode();
				double rate = objBillItemDtl.getRate();
				double iQty = objBillItemDtl.getQuantity();
				if (null != hmPromoItem.get(iCode))
				{
				    clsPromotionItems objPromoItemDtl = hmPromoItem.get(iCode);
				    String promoCode = objPromoItemDtl.getPromoCode();
				    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
				    {
					double freeQty = objPromoItemDtl.getFreeItemQty();
					double freeAmt = freeQty * rate;

					String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						+ ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						+ ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						+ "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
						+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					hmPromoItem.remove(iCode);
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
						    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "' "
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "' "
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "' "
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    hmPromoItem.remove(iCode);
					}
					else
					{
					    //iAmt = String.valueOf(iQty * rate);
					    double amount = iQty * rate;
					    double discAmt = amount * (objPromoItemDtl.getDiscPer() / 100);

					    promoCode = objPromoItemDtl.getPromoCode();
					    String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						    + ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						    + ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "'"
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    hmPromoItem.remove(iCode);
					}
				    }
				}
			    }
			}

			flagAddKOTstoBill = false;
			List<String> listTables = new ArrayList<String>();

			String tableNoOfSelectedKOTS = "select distinct (strTableNo) from tblitemrtemp "
				+ "where  " + sqlAppendForBillFromKOTS + ") ";
			ResultSet rsTableNos = clsGlobalVarClass.dbMysql.executeResultSet(tableNoOfSelectedKOTS);
			while (rsTableNos.next())
			{
			    listTables.add(rsTableNos.getString(1));
			}
			rsTableNos.close();

			String deleteSelectedKOTS = "delete from tblitemrtemp where " + sqlAppendForBillFromKOTS + ") ";
			clsGlobalVarClass.dbMysql.execute(deleteSelectedKOTS);

			//insert into itemrtempbck table
			objUtility.funInsertIntoTblItemRTempBckForMergeKOTs(clsGlobalVarClass.gPOSCode, sqlAppendForBillFromKOTS + ")");
			/**
			 * ***********************************************
			 * Update table status code
			 * ***********************************************
			 */
			for (String table : listTables)
			{
			    boolean flag = true;
			    sql = "select strTableNo from tblitemrtemp where strTableNo='" + table + "';";
			    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    if (rs.next())
			    {
				flag = false;
			    }
			    rs.close();
			    if (flag)
			    {
				String sqlTableStatus = "";
				if (tableNo.equalsIgnoreCase(table))
				{
				    sqlTableStatus = "update tbltablemaster set strStatus='Billed' where strTableNo='" + table + "';";
				    clsGlobalVarClass.dbMysql.execute(sqlTableStatus);
				}
				else
				{
				    sqlTableStatus = "update tbltablemaster set strStatus='Normal' where strTableNo='" + table + "';";
				    clsGlobalVarClass.dbMysql.execute(sqlTableStatus);
				}
			    }
			}

			/**
			 * ******************************************
			 * End Of Update table status code
			 * *******************************************
			 */
		    }

		    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
		    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

		    // insert into tblbilltaxdtl    
		    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

		    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
		    {
			double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			// totalTaxAmt = totalTaxAmt + dblTaxAmt;
			clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
			objBillTaxDtl.setStrBillNo(voucherNo);
			objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
			objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
			objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
			objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			listObjBillTaxBillDtls.add(objBillTaxDtl);
		    }

		    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
		    clsUtility obj = new clsUtility();
		    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		    if (strButtonClicked.equals("Settle"))
		    {
			funOnlyBillSettle();
		    }
		    else if (strButtonClicked.equals("Print"))
		    {
			if (HomeDevlPrint == true)
			{
			    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
			    {
				objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			}
			else
			{
			    objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());
			}
		    }
		    dispose();

		
		}
		
	  
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: ADD_KOT_TO_BILL", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    if (null != objAddKOTToBill)
	    {
		objAddKOTToBill.getList_Selected_KOTs().clear();

		frmMakeKOT objMakeKOT = objAddKOTToBill.getObjMakeKOT();
		if (objMakeKOT != null)
		{
		    objMakeKOT.funRefreshAndLoadTables();
		    objMakeKOT.funClearTable();
		    objMakeKOT.setVisible(true);
		}

		objAddKOTToBill = null;
	    }
	    //System.gc();
	}
    }

    private String funGetItemsFromBill()
    {
	StringBuilder itemCodeBuilder = new StringBuilder();
	try
	{
	    ResultSet rsBillItemCode = clsGlobalVarClass.dbMysql.executeResultSet("select strItemCode from tblbilldtl "
		    + "where strBillNo='" + voucherNo + "' "
		    + "group by strItemCode ");
	    while (rsBillItemCode.next())
	    {
		itemCodeBuilder.append(",'" + rsBillItemCode.getString(1) + "' ");
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
	    return itemCodeBuilder.substring(1);
	}
    }

    private void funSaveWithoutSettleBill()
    {
	try
	{
	    if (tblItemTable.getRowCount() == 0)
	    {
		new frmOkPopUp(null, "", "Please Select Item", 1).setVisible(true);
	    }
	    else
	    {
		/**
		 * Bill series code
		 */
		Map<String, List<clsBillItemDtl>> mapBillSeries = null;
		listBillSeriesBillDtl = new ArrayList<clsBillSeriesBillDtl>();

		if (clsGlobalVarClass.gEnableBillSeries && (mapBillSeries = objBillSettlementUtility.funGetBillSeriesList()).size() > 0)
		{
		    if (mapBillSeries.containsKey("NoBillSeries"))
		    {
			new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
			objBillSettlementUtility.funUpdateTableStatus("Occupied");
			return;
		    }
		    //to calculate PAX per bill if there is a bill series or bill splited
		    Map<Integer, Integer> mapPAXPerBill = objUtility2.funGetPAXPerBill(paxNo, mapBillSeries.size());

		    Iterator<Map.Entry<String, List<clsBillItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
		    int billCount = 0;
		    while (billSeriesIt.hasNext())
		    {
			Map.Entry<String, List<clsBillItemDtl>> billSeriesEntry = billSeriesIt.next();
			String key = billSeriesEntry.getKey();
			List<clsBillItemDtl> values = billSeriesEntry.getValue();

			intBillSeriesPaxNo = 0;
			if (mapPAXPerBill.containsKey(billCount))
			{
			    intBillSeriesPaxNo = mapPAXPerBill.get(billCount);
			}
			funGenerateBillNoForBillSeriesForMakeKOT(key, values);

			billCount++;
		    }
		    //clear temp kot table
		    if (flagBillForItems)
		    {
			objBillSettlementUtility.funUpdateKOTTempTable();
		    }
		    else
		    {
			objBillSettlementUtility.funTruncateKOTTempTable();
		    }

		    //save bill series bill detail
		    for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
		    {
			clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();

			String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl "
				+ "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag"
				+ ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) "
				+ "values ('" + clsGlobalVarClass.gPOSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'"
				+ ",'" + hdBillNo + "','" + objBillSettlementUtility.funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'"
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
			clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			boolean flgHomeDelPrint = objBillSeriesBillDtl.isFlgHomeDelPrint();
			if (strButtonClicked.equals("Settle"))
			{
			    if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && isDirectSettleFromMakeBill)
			    {
				funOnlyBillSettle();
				if (clsGlobalVarClass.gHomeDelSMSYN)
				{
				    objBillSettlementUtility.funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
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
				    objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
				}
			    }
			    else
			    {
				funOnlyBillSettle();
			    }
			}
			else if (strButtonClicked.equals("Print"))
			{
			    if (clsGlobalVarClass.gHomeDelSMSYN)
			    {
				objBillSettlementUtility.funSendSMS(hdBillNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			    }
			    if (flgHomeDelPrint == true)
			    {
				objUtility.funPrintBill(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			    else
			    {
				objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
			    }
			}
			if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 8") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 9") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 21")|| clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 22")
				|| clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 11"))//XO
			{
			    break;
			}

		    }
		}
		else//if no bill series
		{
		    intBillSeriesPaxNo = paxNo;

		    String operationType = "DineIn";
		    String transactionType = "Dine In";//For saving different transaction on same Bill in tblBillHd table in database
		    boolean flgHomeDelPrint = false;
		    voucherNo = lblVoucherNo.getText();
		    revalidate();

		    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
		    {
			operationType = "TakeAway";
			transactionType = transactionType + "," + operationType;
		    }
		    if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
		    {
			operationType = "TakeAway";
			clsGlobalVarClass.hmTakeAway.remove(tableNo);
		    }

		    objBillSettlementUtility.funGenerateBillNo();
		    //last order no
		    int intLastOrderNo = objUtility2.funGetLastOrderNo();

		    funSaveBillDiscountDetail(voucherNo);

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
			    + "from tblitemrtemp where strTableNo='" + tableNo + "' "
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
				    + " values('" + voucherNo + "','" + customerCode + "','" + delPersonCode + "','"
				    + objUtility.funGetPOSDateForTransaction() + "','" + currentTime + "','"
				    + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','','','','','" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + _deliveryCharge + "')";

			    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
			    clsGlobalVarClass.gCustomerCode = null;
			    clsGlobalVarClass.gDeliveryCharges = 0.00;
			    flgHomeDelPrint = true;
			}
		    }
		    if (null != custCode)
		    {
			customerCode = custCode;
		    }
		    dblTotalTaxAmt = 0;

		    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
		    {
			if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
			{
			    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
			}
		    }

		    Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
		    if (hmComplimentaryBillItemDtl.size() > 0)
		    {
			hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
			for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
			{
			    hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
			}
		    }

		    List<clsPlayZoneItems> listPlayZoneItems = null;
		    if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
		    {
			listPlayZoneItems = objBillSettlementUtility.funApplyPlayZonePrice();
		    }

		    List<String> listBillItemDtl = new ArrayList<String>();
		    String custName = "", cardNo = "", orderProcessTime, orderPickupTime;
		    String sqlItemDtl = "select strItemCode,upper(strItemName),dblItemQuantity "
			    + " ,dblAmount,strKOTNo,strManualKOTNo,Time(dteDateCreated),strCustomerCode "
			    + " ,strCustomerName,strCounterCode,strWaiterNo,strPromoCode,dblRate,strCardNo,tmeOrderProcessing,tmeOrderPickup "
			    + " from tblitemrtemp "
			    + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and strTableNo='" + tableNo + "' and strNCKotYN='N' "
			    + " order by strTableNo ASC";

		    ResultSet rsItemKOTDTL = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);
		    String kot = "";
		    while (rsItemKOTDTL.next())
		    {
			String iCode = rsItemKOTDTL.getString(1);
			String iName = rsItemKOTDTL.getString(2);
			double iQty = new Double(rsItemKOTDTL.getString(3));
			String iAmt = rsItemKOTDTL.getString(4);

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
			custCode = rsItemKOTDTL.getString(8);
			custName = rsItemKOTDTL.getString(9);
			String promoCode = rsItemKOTDTL.getString(12);
			cardNo = rsItemKOTDTL.getString(14);
			orderProcessTime = rsItemKOTDTL.getString(15);
			orderPickupTime = rsItemKOTDTL.getString(16);
			String sqlInsertBillDtl = "";

			if (!iCode.contains("M"))
			{
			    if (hmPromoItem.size() > 0)
			    {
				if (null != hmPromoItem.get(iCode))
				{
				    clsPromotionItems objPromoItemDtl = hmPromoItem.get(iCode);
				    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
				    {
					double freeQty = objPromoItemDtl.getFreeItemQty();
					double freeAmt = freeQty * rate;

					promoCode = objPromoItemDtl.getPromoCode();
					String insertBillPromoDtl = "insert into tblbillpromotiondtl "
						+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
						+ ",strClientCode,strDataPostFlag,strPromoType,dblAmount"
						+ ",dblDiscountPer,dblDiscountAmt,dteBillDate) values "
						+ "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
						+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					hmPromoItem.remove(iCode);
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
						    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "' "
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "' "
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "' "
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    hmPromoItem.remove(iCode);
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
						    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
						    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
						    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + amount + "'"
						    + ",'" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
					    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
					    hmPromoItem.remove(iCode);
					}
				    }
				}
			    }

			    String amt = "0.00";
			    boolean flgComplimentaryBill = false;
			    if (hmSettlemetnOptions.size() == 1)
			    {
				for (clsSettelementOptions obj : hmSettlemetnOptions.values())
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
				discAmt = hmBillItemDtl.get(iCode).getDiscountAmount() * iQty;
				discPer = hmBillItemDtl.get(iCode).getDiscountPercentage();
			    }

			    if (iQty > 0)
			    {
				if (iName.startsWith("=>"))
				{
				    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
					    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
					    + ",strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo"
					    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
					    + "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ",'" + iQty
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
					    + "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ""
					    + ",'" + iQty + "','" + amt + "','0.00','" + billDateTime + "','" + kot + "'"
					    + ",'" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','" + promoCode + "'"
					    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
					    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessTime + "','" + orderPickupTime + "')";
				}
				clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
				if (hmComplimentaryBillItemDtl.containsKey(iCode))
				{
				    clsBillDtl objBillDtl = hmComplimentaryBillItemDtl.get(iCode);
				    objBillDtl.setDblRate(rate);
				    objBillDtl.setStrBillNo(voucherNo);
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

				    hmComplimentaryBillItemDtl.put(iCode, objBillDtl);

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
			    if (hmBillItemDtl.containsKey(iCode + "!" + iName))
			    {
				modDiscAmt = hmBillItemDtl.get(iCode + "!" + iName).getDiscountAmount() * iQty;
				modDiscPer = hmBillItemDtl.get(iCode + "!" + iName).getDiscountPercentage();
			    }
			    StringBuilder sbTemp = new StringBuilder(iCode);
			    if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
			    {
				amt = 0;
			    }

			    String sqlBillModifierDtl = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
				    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
				    + "values('" + voucherNo + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
				    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + modDiscPer + "','" + modDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
			    //System.out.println(sqlBillModifierDtl);
			    clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
			}
		    }
		    rsItemKOTDTL.close();

		    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
			    + "from tblbillpromotiondtl "
			    + " where strBillNo='" + voucherNo + "' and strPromoType='ItemWise' ";
		    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
		    while (rsBillPromo.next())
		    {
			double freeQty = rsBillPromo.getDouble(1);
			String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
				+ " from tblbilldtl "
				+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
				+ " and strBillNo='" + voucherNo + "'";
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
		    String sqlBillDtl = "select sum(dblAmount) from tblbilldtl where strBillNo='" + voucherNo + "' ";
		    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    if (rsBillDtl.next())
		    {
			subTotalAmt = rsBillDtl.getDouble(1);
		    }
		    rsBillDtl.close();

		    sqlBillDtl = "select sum(dblAmount) from tblbillmodifierdtl where strBillNo='" + voucherNo + "' ";
		    rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    if (rsBillDtl.next())
		    {
			subTotalAmt += rsBillDtl.getDouble(1);
		    }
		    rsBillDtl.close();

		    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
		    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

		    // insert into tblbilltaxdtl    
		    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

		    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
		    {
			double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			//totalTaxAmt = totalTaxAmt + dblTaxAmt;
			clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
			objBillTaxDtl.setStrBillNo(voucherNo);
			objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
			objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
			objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
			objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			listObjBillTaxBillDtls.add(objBillTaxDtl);
		    }

		    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
		    clsUtility obj = new clsUtility();
		    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		    grandTotalAmt = (subTotalAmt - dblDiscountAmt) + dblTotalTaxAmt + _deliveryCharge;

		    //start code to calculate roundoff amount and round off by amt
		    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(grandTotalAmt);
		    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
		    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
		    {
			grandTotalAmt = mapRoundOff.get("roundOffAmt");
		    }
		    //end code to calculate roundoff amount and round off by amt

		    if (hmComplimentaryBillItemDtl.size() > 0)
		    {
			funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
		    }

		    String sqlInsertBillHd = "insert into tblbillhd"
			    + "(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
			    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType,"
			    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strTableNo"
			    + ",strWaiterNo,strCustomerCode,intShiftCode,intPaxNo,strReasonCode,strRemarks"
			    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strAreaCode"
			    + ",strDiscountRemark,strTakeAwayRemarks,strDiscountOn,strCardNo,strTransactionType,dblRoundOff"
			    + ",intBillSeriesPaxNo,dtBillDate,intOrderNo,strCRMRewardId,strManualBillNo,dblUSDConverionRate ) "
			    + "values('" + voucherNo + "','" + advOrderBookingNo + "','" + objUtility.funGetPOSDateForTransaction() + "','"
			    + clsGlobalVarClass.gPOSCode + "','','" + dblDiscountAmt + "','"
			    + dblDiscountPer + "','" + dblTotalTaxAmt + "','" + subTotalAmt + "','"
			    + grandTotalAmt + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
			    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"
			    + clsGlobalVarClass.getCurrentDateTime() + "','"
			    + clsGlobalVarClass.gClientCode + "','" + tableNo + "','" + waiterNo + "'"
			    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
			    + "," + paxNo + ",'" + selectedReasonCode + "','" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "'"
			    + "," + txtTip.getText() + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
			    + ",'" + counterCode + "'," + _deliveryCharge + ",'" + areaCode + "'"
			    + ",'" + objUtility.funCheckSpecialCharacters(discountRemarks) + "','','','" + cardNo + "','" + transactionType + "'"
			    + ",'" + _grandTotalRoundOffBy + "','" + intBillSeriesPaxNo + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
			    + ",'" + intLastOrderNo + "','" + rewardId + "','" + txtManualBillNo.getText().trim() + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertBillHd);

		    clsBillHd objBillHd = new clsBillHd();
		    objBillHd.setStrBillNo(voucherNo);
		    objBillHd.setDblGrandTotal(_grandTotal);
		    objBillHd.setStrCustomerCode(customerCode);
		    objBillHd.setStrOnlineOrderNo(onlineOrderNo);
		    objBillSettlementUtility.funCallIntegrationAPIsAfterBillPrint(objBillHd);

		    /**
		     * update KOT to bill note
		     */
		    objBillSettlementUtility.funUpdateKOTToBillNote(clsGlobalVarClass.gPOSCode, tableNo, voucherNo);

//		    if (clsGlobalVarClass.gCMSIntegrationYN)
//		    {
//			if (custCode.trim().length() > 0)
//			{
//			    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + custCode + "' "
//				    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//			    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);
//
//			    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
//				    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
//				    + "values('" + custCode + "','" + custName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
//				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
//				    + ",'" + clsGlobalVarClass.gClientCode + "')";
//			    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
//			}
//		    }

		    if (strButtonClicked.equals("Settle"))
		    {
			if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && isDirectSettleFromMakeBill)
			{
			    funOnlyBillSettle();

			    if (clsGlobalVarClass.gHomeDelSMSYN)
			    {
				objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			    }
			    if (flgHomeDelPrint == true)
			    {
				if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
				{
				    objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				}
				else
				{
				    objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
				}
			    }
			    else
			    {
				objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());
			    }
			}
			else
			{
			    funOnlyBillSettle();
			}
		    }
		    else if (strButtonClicked.equals("Print"))
		    {
			if (flgHomeDelPrint == true)
			{
			    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
			    {
				objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			    else
			    {
				objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			    }
			}
			else
			{
			    objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());
			}
			if (clsGlobalVarClass.gHomeDelSMSYN)
			{
			    objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
			}
		    }

		    if (flagBillForItems)
		    {
			objBillSettlementUtility.funUpdateKOTTempTable();
		    }
		    else
		    {
			objBillSettlementUtility.funTruncateKOTTempTable();
		    }
		    dispose();
		}
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    // clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-24", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    if (null != makeBillObj)
	    {
		makeBillObj = null;
	    }
	    if (null != kotObj)
	    {
		kotObj = null;
	    }
	    System.gc();
	}
    }

    private int funSaveCRMPoints(String billNo, double temGrandTotal, String crmType, String mobileNo, String billDate) throws Exception
    {
	if (crmType.equalsIgnoreCase("SQY"))
	{
	    String sql_CRMPoints = "insert into tblcrmpoints (strBillNo,dblPoints,strTransactionId,strOutletUID"
		    + ",dblRedeemedAmt,longCustMobileNo,strClientCode,dteBillDate) values('" + billNo + "',0.00,'" + obj.getTransactionId() + "'"
		    + ",'" + obj.getOutlet_uuid() + "'," + obj.getRedeemed_amt() + ",'" + obj.getCustMobileNo() + "'"
		    + ",'" + clsGlobalVarClass.gClientCode + "','" + billDate + "')";
	    clsGlobalVarClass.dbMysql.execute(sql_CRMPoints);
	}
	else if (crmType.equalsIgnoreCase("PMAM"))
	{
	    String userToken = clsGlobalVarClass.gOutletUID;
	    String accessToken = clsGlobalVarClass.gPOSID;
	    //com.pmam.crm.CRMLoyalityProgramSvc obj = new com.pmam.crm.CRMLoyalityProgramSvc();

	    String sql = "select strCRMId from tblcustomermaster "
		    + "where longMobileNo='" + mobileNo + "'";
	    ResultSet rsCustData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustData.next())
	    {
		String custId = rsCustData.getString(1);
		String sql_CRMPoints = "insert into tblcrmpoints (strBillNo,dblPoints,strTransactionId,strOutletUID"
			+ ",dblRedeemedAmt,longCustMobileNo,strClientCode,strCustomerId,dteBillDate) "
			+ "values('" + billNo + "'," + _loyalityPoints + ",'',''," + temGrandTotal + ",'" + mobileNo + "'"
			+ ",'" + clsGlobalVarClass.gClientCode + "','" + custId + "','" + billDate + "')";
		clsGlobalVarClass.dbMysql.execute(sql_CRMPoints);
		clsGlobalVarClass.gCustMobileNoForCRM = "";
	    }
	    rsCustData.close();
	}
	else
	{
	    boolean flgLoyality = false;
	    double initAmount = 0, loyaltyPoints = 0, loyaltyValue = 0;
	    String sql_Points = "select a.dblAmount,a.dblLoyaltyPoints,a.dblLoyaltyValue "
		    + "from tblloyaltypoints a, tblloyaltypointposdtl b "
		    + "where a.strLoyaltyCode=b.strLoyaltyCode and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	    ResultSet rsLoyalty = clsGlobalVarClass.dbMysql.executeResultSet(sql_Points);
	    while (rsLoyalty.next())
	    {
		flgLoyality = true;
		initAmount = Double.parseDouble(rsLoyalty.getString(1));
		loyaltyPoints = Double.parseDouble(rsLoyalty.getString(2));
		loyaltyValue = Double.parseDouble(rsLoyalty.getString(3));
	    }
	    rsLoyalty.close();
	    if (flgLoyality)
	    {
		double val1 = temGrandTotal / initAmount;
		loyaltyPoints = loyaltyPoints * val1;
		loyaltyValue = loyaltyValue * val1;
		String sql_Delete = "delete from tblcrmpoints where strBillNo='" + billNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql_Delete);
		String sql_CRMPoints = "insert into tblcrmpoints (strBillNo,dblPoints"
			+ ",strTransactionId,strOutletUID,dblRedeemedAmt,longCustMobileNo"
			+ ",strClientCode,dblValue,dteBillDate) "
			+ "values('" + billNo + "'," + loyaltyPoints + ",'',''," + _loyalityPoints + ""
			+ ",'" + mobileNo + "','" + clsGlobalVarClass.gClientCode + "'," + loyaltyValue + ",'" + billDate + "')";
		clsGlobalVarClass.dbMysql.execute(sql_CRMPoints);
		clsGlobalVarClass.gCustMobileNoForCRM = "";
	    }
	}
	return 1;
    }

    /*
     * private int funCallPMAMWebService(String billNo, double temGrandTotal,
     * double discAmount, String crmType) throws Exception { String userToken =
     * clsGlobalVarClass.gOutletUID; String accessToken =
     * clsGlobalVarClass.gPOSID; URL pmamURL = new
     * URL(clsGlobalVarClass.gGetWebserviceURL);
     * com.pmam.crm.CRMLoyalityProgramSvc obj = new
     * com.pmam.crm.CRMLoyalityProgramSvc(pmamURL);
     *
     * String sql = "select longCustMobileNo,strCustomerId from tblcrmpoints " +
     * "where strBillNo='" + billNo + "'"; ResultSet rsCustData =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); if (rsCustData.next()) {
     * String custMBNo = rsCustData.getString(1); String custId =
     * rsCustData.getString(2); String[] split =
     * lblBillDate.getText().split("-"); String billDate = split[1] + "/" +
     * split[2] + "/" + split[0]; double netAmount = temGrandTotal - discAmount;
     *
     * String data = "{\"BillInfo\":[{\"UserName\":" + "\"" + custMBNo + "\"" +
     * ",\"InvoiceNo\":\"" + billNo + "\",\"InvoiceDate\":\"" + billDate + "\""
     * + ",\"TotalAmount\":\"" + temGrandTotal + "\",\"DiscountAmount\":\"" +
     * discAmount + "\"" + ",\"NetAmount\":\"" + netAmount +
     * "\",\"CouponCode\":\"" + couponCode + "\"}]}"; String output =
     * obj.getCRMLoyalityProgramSvcSoap().synchronise(userToken, accessToken,
     * data, "4"); JSONParser jsonParser = new JSONParser(); JSONObject
     * jsonObject = (JSONObject) jsonParser.parse(output); JSONArray lang =
     * (JSONArray) jsonObject.get("InvoiceDetails"); Iterator i =
     * lang.iterator(); while (i.hasNext()) { JSONObject innerObj = (JSONObject)
     * i.next(); String status = innerObj.get("Status").toString(); String
     * pointsGained = innerObj.get("PointsGained").toString(); String up =
     * "update tblcrmpoints set dblPoints=" + Double.parseDouble(pointsGained) +
     * " " + "where strBillNo='" + billNo + "'";
     * clsGlobalVarClass.dbMysql.execute(up); } } rsCustData.close(); return 1;
     * }
     */
// Function to save discount details in bill. tblbilldiscountdtl    
    public int funSaveBillDiscountDetail(String voucherNo)
    {
	try
	{
	    StringBuilder sqlBillDiscDtl = new StringBuilder();
	    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
	    Iterator<Map.Entry<String, clsBillDiscountDtl>> itDiscEntry = mapBillDiscDtl.entrySet().iterator();
	    double totalDiscAmt = 0.00, finalDiscPer = 0.00;

	    for (int i = 0; itDiscEntry.hasNext(); i++)
	    {
		Map.Entry<String, clsBillDiscountDtl> discEntry = itDiscEntry.next();
		String key = discEntry.getKey();
		clsBillDiscountDtl value = discEntry.getValue();

		String discOnType = key.split("!")[0];
		String discOnValue = key.split("!")[1];
		String remark = value.getRemark();
		String reason = value.getReason();
		double discPer = value.getDiscPer();
		double discAmt = value.getDiscAmt();
		double discOnAmt = value.getDiscOnAmt();

		if (i == 0)
		{
		    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "' )");
		}
		else
		{
		    sqlBillDiscDtl.append(",('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "' )");
		}
		totalDiscAmt += discAmt;
	    }

	    if (mapBillDiscDtl.size() > 0)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
	    }
	    if (_subTotal == 0.00)
	    {
	    }
	    else
	    {
		finalDiscPer = (totalDiscAmt / _subTotal) * 100;
	    }
	    dblDiscountAmt = totalDiscAmt;
	    dblDiscountPer = finalDiscPer;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return 0;
	}
    }

    private void funModifyBill()
    {
	try
	{
	    String modifyBillReasonCode = "", modifyBillReasonName = "";

	    if (vModifyReasonName.size() > 0)
	    {
		Object[] arrObjReasonName = vModifyReasonName.toArray();
		modifyBillReasonName = (String) JOptionPane.showInputDialog(this, "Please Select Modify Bill Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
		if (null == modifyBillReasonName)
		{
		    JOptionPane.showMessageDialog(this, "Please Select Modify Bill Reason");
		    return;
		}
		else
		{
		    for (int cntReason = 0; cntReason < vModifyReasonCode.size(); cntReason++)
		    {
			if (vModifyReasonName.elementAt(cntReason).toString().equals(modifyBillReasonName))
			{
			    modifyBillReasonCode = vModifyReasonCode.elementAt(cntReason).toString();
			    break;
			}
		    }
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "No Modify reasons are created");
		return;
	    }

	    String sql = "delete from tblvoidbillhd where strBillNo='" + lblVoucherNo.getText() + "' and strTransType='MB'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "insert into tblvoidbillhd (strPosCode,strReasonCode,strReasonName,strBillNo,"
		    + "dblActualAmount,dblModifiedAmount,dteBillDate,strTransType,"
		    + "dteModifyVoidBill,strTableNo,strWaiterNo,intShiftCode,strUserCreated,"
		    + "strUserEdited,strClientCode)"
		    + "(select '" + clsGlobalVarClass.gPOSCode + "','" + modifyBillReasonCode + "' "
		    + ",'" + modifyBillReasonName + "','" + lblVoucherNo.getText() + "',dblGrandTotal "
		    + ",'" + _grandTotal + "',dteBillDate,'MB','" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
		    + ",strTableNo,strWaiterNo,intShiftCode,strUserCreated "
		    + ",'" + clsGlobalVarClass.gUserCode + "',strClientCode "
		    + " from tblbillhd where strBillNo='" + lblVoucherNo.getText() + "')";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    clsGlobalVarClass.gReasoncode = "";
	    clsGlobalVarClass.gFavoritereason = "";

	    sql = "delete from tblvoidbilldtl where strBillNo='" + lblVoucherNo.getText() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "insert into tblvoidbilldtl (strPosCode,strReasonCode,strReasonName,"
		    + "strItemCode,strItemName,strBillNo,intQuantity,dblAmount,dblTaxAmount,dteBillDate,"
		    + "strTransType,dteModifyVoidBill,intShiftCode,strUserCreated,strClientCode)"
		    + "(select '" + clsGlobalVarClass.gPOSCode + "','','',strItemCode,strItemName,"
		    + "strBillNo,dblQuantity,dblAmount,dblTaxAmount,dteBillDate,"
		    + "'MB','" + clsGlobalVarClass.getPOSDateForTransaction() + "',(select intShiftCode from tblbillhd where strBillNo='" + lblVoucherNo.getText() + "'),'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gClientCode + "' "
		    + "from tblbilldtl where strBillNo='" + lblVoucherNo.getText() + "')";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //clear old data for this billDiscountDtl table
	    sql = "delete from tblbilldiscdtl where strBillNo='" + voucherNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    funSaveBillDiscountDetail(voucherNo);

	    //For saving different transaction on same Bill in tblBillHd table in database
	    String transactionType = "";
	    sql = "select strTransactionType from tblbillhd where strBillNo='" + voucherNo + "'";
	    ResultSet rsTransactionType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTransactionType.next())
	    {
		transactionType = rsTransactionType.getString(1) + ",Modify Bill";
	    }
	    rsTransactionType.close();

	    String nscTaxCode = "";
	    if (isRemoveSCTax && clsGlobalVarClass.gRemoveSCTaxCode.trim().length() > 0)
	    {
		nscTaxCode = clsGlobalVarClass.gRemoveSCTaxCode;
	    }

	    sql = "update tblbillhd set "
		    + " dblDiscountAmt='" + dblDiscountAmt + "',dblDiscountPer='" + dblDiscountPer + "'"
		    + ",dblTaxAmt='" + dblTotalTaxAmt + "'"
		    + ",dblSubTotal='" + _subTotal + "'"
		    + ",dblGrandTotal='" + _grandTotal + "'"
		    + ",dblRoundOff='" + _grandTotalRoundOffBy + "'"
		    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
		    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + ",strDataPostFlag='N'"
		    + ",strReasonCode='" + selectedReasonCode + "'"
		    + ",strRemarks='" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "'"
		    + ",dblTipAmount='" + txtTip.getText() + "'"
		    + ",strDiscountRemark='" + objUtility.funCheckSpecialCharacters(discountRemarks) + "'"
		    + ",strTransactionType='" + transactionType + "' "
		    + ",strNSCTax='" + nscTaxCode + "' "
		    + ",dblUSDConverionRate='" + clsGlobalVarClass.gUSDConvertionRate + "' "
		    + " where strBillNo='" + lblVoucherNo.getText() + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //update billseriesbilldtl grand total
	    if (clsGlobalVarClass.gEnableBillSeries)
	    {
		clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl set dblGrandTotal='" + _grandTotal + "' where strHdBillNo='" + lblVoucherNo.getText() + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
	    }

	    printstatus = true;

	    if (hmComplimentaryBillItemDtl.size() > 0)
	    {
		List<clsBillDtl> listObjBillDtl = new ArrayList<clsBillDtl>();
		String sqlBillDtl = "select * from tblbilldtl "
			+ " where strBillNo='" + voucherNo + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		while (rsBillDtl.next())
		{
		    clsBillDtl objBillDtl = new clsBillDtl();
		    objBillDtl.setStrItemCode(rsBillDtl.getString(1));
		    objBillDtl.setStrItemName(rsBillDtl.getString(2));
		    objBillDtl.setStrAdvBookingNo("");
		    objBillDtl.setStrBillNo(voucherNo);
		    objBillDtl.setDblRate(rsBillDtl.getDouble(5));
		    objBillDtl.setDblQuantity(rsBillDtl.getDouble(6));
		    objBillDtl.setDblAmount(rsBillDtl.getDouble(7));
		    objBillDtl.setDblTaxAmount(0);
		    objBillDtl.setDteBillDate(objUtility.funGetPOSDateForTransaction());
		    objBillDtl.setStrKOTNo(rsBillDtl.getString(10));
		    objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objBillDtl.setTmeOrderProcessing("00:00:00");
		    objBillDtl.setStrDataPostFlag("N");
		    objBillDtl.setStrMMSDataPostFlag("N");
		    objBillDtl.setStrManualKOTNo("");
		    objBillDtl.setTdhYN(rsBillDtl.getString(17));
		    objBillDtl.setStrPromoCode(rsBillDtl.getString(18));
		    objBillDtl.setStrCounterCode(rsBillDtl.getString(19));
		    objBillDtl.setStrWaiterNo(rsBillDtl.getString(20));
		    objBillDtl.setSequenceNo(rsBillDtl.getString(23));
		    objBillDtl.setStrOrderPickupTime(rsBillDtl.getString(25));

		    listObjBillDtl.add(objBillDtl);
		}
		funInsertBillDtlTable(listObjBillDtl);
	    }

	    for (clsBillItemDtl objBillItemDtl : hmBillItemDtl.values())
	    {
		String key = (objBillItemDtl.getItemCode().contains("M") ? objBillItemDtl.getItemCode() + "!" + objBillItemDtl.getItemName() : objBillItemDtl.getItemCode());
		String iCode = objBillItemDtl.getItemCode();
		String iName = objBillItemDtl.getItemName();

		double discPer = hmBillItemDtl.get(key).getDiscountPercentage();
		double discAmt = hmBillItemDtl.get(key).getDiscountAmount();
		sql = "update tblbilldtl set dblDiscountAmt=" + discAmt + "*dblQuantity, dblDiscountPer=" + discPer
			+ " where strItemCode='" + iCode + "'  and strBillNo='" + lblVoucherNo.getText() + "' ";
		clsGlobalVarClass.dbMysql.execute(sql);

		sql = "update tblbillmodifierdtl set dblDiscAmt=" + discAmt + "*dblQuantity, dblDiscPer=" + discPer
			+ " where strItemCode='" + iCode + "' and strModifierName='" + iName + "' and strBillNo='" + lblVoucherNo.getText() + "' ";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	    sql = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		//totalTaxAmt = totalTaxAmt + dblTaxAmt;
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(voucherNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);

	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    //funFillTaxBillTable(voucherNo);
	    if (printstatus == true)
	    {
		frmOkPopUp okOb = new frmOkPopUp(null, "Bill Updated Successfully ", "Success", 1);
		okOb.setVisible(true);
		sql = "select count(strBillNo),strCustomerCode "
			+ " from tblhomedelivery "
			+ " where strBillNo='" + voucherNo + "'";
		ResultSet rsHomeDelivery = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsHomeDelivery.next();
		if (rsHomeDelivery.getInt(1) > 0)
		{
		    if (clsGlobalVarClass.gHomeDelSMSYN)
		    {
			objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
		    }
		    if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
		    {
			objUtility.funPrintBill(voucherNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");

			if (clsGlobalVarClass.gEnableBillSeries)
			{
			    String reprintBillNo = objUtility2.funGetBillNoOnModifyBill(voucherNo);
			    objUtility.funPrintBill(reprintBillNo, objUtility.funGetOnlyPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode, "print");
			}
		    }
		}
		else
		{
		    objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());
		}
		rsHomeDelivery.close();

		/**
		 * save reprint audit
		 */
		objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint after modification of pending bill.", "", voucherNo, "");
	    }

	    //send modified bill MSG            
	    sql = "select a.strSendSMSYN,a.longMobileNo "
		    + "from tblsmssetup a "
		    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
		    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and a.strTransactionName='ModifyBill' "
		    + "and a.strSendSMSYN='Y'; ";
	    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsSendSMS.next())
	    {
		funSendModifyBillSMS(voucherNo, rsSendSMS.getString(2));
	    }
	    rsSendSMS.close();

	    dispose();
	    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
	    {
		if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
		{
		    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-25", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    System.gc();
	}
    }

    private void funOnlySettleBillDB()
    {
	try
	{
	    String settleBillDate = objUtility.funGetPOSDateForTransaction();
	    String sql = "update tblbillhd set dblTipAmount='" + tipAmount + "' where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    int row = 0;
	    List<clsBillSettlementDtl> listObjBillSettlementDtl = new ArrayList<clsBillSettlementDtl>();
	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		double settleAmt = 0;
		if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
		{
		    settleAmt = ob.getDblSettlementAmt();
		}
		else
		{
		    settleAmt = ob.getDblPaidAmt();
		}

		if (ob.getStrSettelmentType().equals("Debit Card"))
		{
		    objUtility.funDebitCardTransaction(voucherNo, debitCardNo, settleAmt, "Settle");
		    objUtility.funUpdateDebitCardBalance(debitCardNo, settleAmt, "Settle");
		}
		row++;

		clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl();
		objBillSettlementDtl.setStrBillNo(voucherNo);
		objBillSettlementDtl.setStrSettlementCode(ob.getStrSettelmentCode());
		objBillSettlementDtl.setDblSettlementAmt(settleAmt);
		objBillSettlementDtl.setDblPaidAmt(ob.getDblPaidAmt());
		objBillSettlementDtl.setStrExpiryDate("");
		objBillSettlementDtl.setStrCardName(ob.getStrCardName());
		objBillSettlementDtl.setStrRemark(ob.getStrRemark());
		objBillSettlementDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillSettlementDtl.setStrCustomerCode(customerCodeForCredit);
		objBillSettlementDtl.setDblActualAmt(ob.getDblActualAmt());
		objBillSettlementDtl.setDblRefundAmt(ob.getDblRefundAmt());
		objBillSettlementDtl.setStrGiftVoucherCode(ob.getStrGiftVoucherCode());
		objBillSettlementDtl.setStrDataPostFlag("N");

		listObjBillSettlementDtl.add(objBillSettlementDtl);

		if (ob.getStrSettelmentType().equals("Benow"))
		{
		    if (clsGlobalVarClass.gBenowIntegrationYN)
		    {
			String sqlInsertBenowSettlementDtl = "insert into tblbenowsettlementdtl(strBillNo,strQRString,dblSettlementAmount,strTransID,strTransStatus,dteTransDate,strMerchantCode) "
				+ " values ('" + voucherNo + "','" + ob.getStrQRString() + "','" + settleAmt + "','" + ob.getStrTransId() + "' "
				+ " ,'" + ob.getStrTransStatus() + "','" + ob.getStrTransDate() + "','" + ob.getStrMerchantCode() + "')";
			clsGlobalVarClass.dbMysql.execute(sqlInsertBenowSettlementDtl);
		    }
		}
	    }
	    funInsertBillSettlementDtlTable(listObjBillSettlementDtl);
	    funTruncateDebitCardTempTable();

	    if (row > 1)
	    {
		settleName = "MultiSettle";
	    }
	    /**
	     * recalculate taxes for settlements
	     */
	    //objUtility.funReCalculateTaxForBill(voucherNo, clsGlobalVarClass.gPOSCode, hmSettlemetnOptions, clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    //*********If the billhd grandstotal and screen grand total amount changes*************//
	    if (hmSettlemetnOptions.size() == 1)
	    {
		for (clsSettelementOptions ob : hmSettlemetnOptions.values())
		{
		    if (!ob.getStrSettelmentType().equals("Complementary"))
		    {
			double billHdGT = funGetBillHdGrandTotal(clsGlobalVarClass.gPOSCode, voucherNo);
			if (_grandTotal != billHdGT)
			{
			    objUtility.funWriteErrorLog(new Exception("Screen Grand Total Amount(" + _grandTotal + ") and BillHd Grand Total Amount(" + billHdGT + ") Not Matching:-BillNo=" + voucherNo + ",Settlement Mode=" + settleName + " ")
			    {

			    });
			    funUpdateBillTransTablesWithTaxValues(voucherNo);
			    //objUtility.funReCalculateTaxForBill(voucherNo, clsGlobalVarClass.gPOSCode, hmSettlemetnOptions, clsGlobalVarClass.gPOSOnlyDateForTransaction);
			}
		    }
		}
	    }
	    else
	    {
		double billHdGT = funGetBillHdGrandTotal(clsGlobalVarClass.gPOSCode, voucherNo);
		if (_grandTotal != billHdGT)
		{
		    objUtility.funWriteErrorLog(new Exception("Screen Grand Total Amount(" + _grandTotal + ") and BillHd Grand Total Amount(" + billHdGT + ") Not Matching:-BillNo=" + voucherNo + ",Settlement Mode=" + settleName + " ")
		    {

		    });
		    funUpdateBillTransTablesWithTaxValues(voucherNo);
		    //objUtility.funReCalculateTaxForBill(voucherNo, clsGlobalVarClass.gPOSCode, hmSettlemetnOptions, clsGlobalVarClass.gPOSOnlyDateForTransaction);
		}
	    }
	    //****************************************************************************//

	    //update billseriesbilldtl grand total
	    if (clsGlobalVarClass.gEnableBillSeries)
	    {
		clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl set dblGrandTotal='" + _grandTotal + "' where strHdBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
	    }

	    // For Complimentary Bill
	    funClearComplimetaryBillAmt(voucherNo);

	    sql = "update tblbillhd set strSettelmentMode='" + settleName + "'"
		    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "', dteSettleDate='" + settleBillDate + "' "
		    + ",strRemarks='" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "',strCardNo='" + debitCardNo + "'"
		    + ",strReasonCode='" + selectedReasonCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + "where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    if (clsGlobalVarClass.gHomeDeliveryForSelectedBillNo)
	    {
		sql = "update tblhomedeldtl set strSettleYN='Y' where strBillNo='" + voucherNo + "' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		clsGlobalVarClass.gHomeDeliveryForSelectedBillNo = false;
	    }

	    if (settleType.equals("Credit"))
	    {
		sql = "update tblbillhd set strCustomerCode='" + customerCodeForCredit + "' "
			+ "where strBillNo='" + voucherNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    //funShiftAdvOrderDataToQFile(voucherNo);

	    lblVoucherNo.setText(voucherNo);
	    clsGlobalVarClass.funCheckHomeDelivery(voucherNo);
	    if (clsGlobalVarClass.gHomeDeliveryForSelectedBillNo)
	    {
		clsGlobalVarClass.gHomeDeliveryForSelectedBillNo = false;
	    }
	    if (clsGlobalVarClass.gBillSettleSMSYN)
	    {
		objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gBillSettlementSMS, "");
	    }

	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("Sqy"))
	    {
		String sql_CustMb = "select longMobileNo from tblcustomermaster "
			+ "where strCustomerCode='" + custCode + "'";
		ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
		if (rsCust.next())
		{
		    funSaveCRMPoints(voucherNo, _grandTotal, "Sqy", rsCust.getString(1), settleBillDate);
		}
		funCallPostWebService(_grandTotal, "Direct Biller", voucherNo);
	    }
	    else if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && custCode != null && !custCode.isEmpty())
	    {
		Thread crmThread = new Thread()
		{
		    @Override
		    public void run()
		    {
			try
			{
			    String mobileNo = "";
			    String sql_CustMb = "select longMobileNo from tblcustomermaster "
				    + "where strCustomerCode='" + custCode + "'";
			    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
			    if (rsCust.next())
			    {
				mobileNo = rsCust.getString(1);
			    }
			    rsCust.close();

			    if (mobileNo.trim().isEmpty())
			    {
				System.out.println("No Mobile no");
				return;
			    }
			    clsCRMInterface objCRMInterface = new clsCRMInterface();

			    objCRMInterface.funPostBillDataCRM(custCode, voucherNo, clsGlobalVarClass.gPOSOnlyDateForTransaction);

			    if (!rewardId.isEmpty())
			    {
				objCRMInterface.funPostRewardRedeemCRM(mobileNo, rewardId);
			    }//rewardId,2300,2057
			    rewardId = "";
			}
			catch (Exception e)
			{
			    objUtility.funShowDBConnectionLostErrorMessage(e);	
			    e.printStackTrace();
			}
		    }
		};
		crmThread.start();

	    }
	    else
	    {
		String sql_CustMb = "select b.longMobileNo from tblbillhd a,tblcustomermaster b "
			+ "where a.strCustomerCode=b.strCustomerCode and a.strBillNo='" + voucherNo + "'";
		ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
		if (rsCust.next())
		{
		    funSaveCRMPoints(voucherNo, _grandTotal, "JPOS", rsCust.getString(1), settleBillDate);
		}
		rsCust.close();
	    }

	    if (clsGlobalVarClass.gEnablePMSIntegrationYN && settleType.equals("Room"))
	    {
		funInsertPMSPostingBillDtlTable(voucherNo, _grandTotal, hmSettlemetnOptions);
		clsInvokeDataFromSanguineERPModules objSangERP = new clsInvokeDataFromSanguineERPModules();
		objSangERP.funPOSTRoomSettlementDtlToPMS(voucherNo, _grandTotal, hmSettlemetnOptions);
		objSangERP = null;
	    }

	    clsGlobalVarClass.gCustomerCode = null;
	    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
	    {
		if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
		{
		    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
		}
	    }

	    if (flgMakeKot)
	    {
		kotObj.funClearTable();
	    }
	    if (billPrintOnSettlement.equalsIgnoreCase("Y"))
	    {
		billPrintOnSettlement = "N";
		objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());

		/**
		 * save reprint audit
		 */
		objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint after modification of pending bill.", "", voucherNo, "");
	    }

	    funResetVariableValues();
	    //ask to settle next bill for bill series
	    if (clsGlobalVarClass.gEnableBillSeries)
	    {
		clsUtility2 objUtility2 = new clsUtility2();
		String nextBillNo = objUtility2.funGetNextSettleBill(voucherNo);

		if (nextBillNo.trim().length() > 0)
		{
		    frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do You Want To Settle Bill " + nextBillNo + " ?");
		    okOb.setVisible(true);
		    int res = okOb.getResult();
		    if (res == 1)
		    {
			//send settleed bill MSG                   
			String sql1 = "select a.strSendSMSYN,a.longMobileNo "
				+ "from tblsmssetup a "
				+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				+ "and a.strTransactionName='SettleBill' "
				+ "and a.strSendSMSYN='Y'; ";
			ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
			if (rsSendSMS.next())
			{
			    String mobileNo = rsSendSMS.getString(2);//mobileNo

			    funSendSettleBillSMS(nextBillNo, mobileNo);

			}
			rsSendSMS.close();


			objPannelShowBills.funFillTableCombo();
			objPannelShowBills.funFillUnsettledBills(nextBillNo);
		    }
		    else
		    {
			//send settleed bill MSG                   
			String sql1 = "select a.strSendSMSYN,a.longMobileNo "
				+ "from tblsmssetup a "
				+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				+ "and a.strTransactionName='SettleBill' "
				+ "and a.strSendSMSYN='Y'; ";
			ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
			if (rsSendSMS.next())
			{
			    String mobileNo = rsSendSMS.getString(2);//mobileNo

			    funSendSettleBillSMS(voucherNo, mobileNo);

			}
			rsSendSMS.close();
			objPannelShowBills.funFillTableCombo();
			objPannelShowBills.funFillUnsettledBills();
		    }
		}
		else
		{
		    //send settleed bill MSG                   
		    String sql1 = "select a.strSendSMSYN,a.longMobileNo "
			    + "from tblsmssetup a "
			    + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			    + "and a.strTransactionName='SettleBill' "
			    + "and a.strSendSMSYN='Y'; ";
		    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
		    if (rsSendSMS.next())
		    {
			String mobileNo = rsSendSMS.getString(2);//mobileNo

			funSendSettleBillSMS(voucherNo, mobileNo);

		    }
		    rsSendSMS.close();
		    objPannelShowBills.funFillTableCombo();
		    objPannelShowBills.funFillUnsettledBills();
		}
	    }
	    else
	    {
		//send settleed bill MSG                   
		String sql1 = "select a.strSendSMSYN,a.longMobileNo "
			+ "from tblsmssetup a "
			+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			+ "and a.strTransactionName='SettleBill' "
			+ "and a.strSendSMSYN='Y'; ";
		ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
		if (rsSendSMS.next())
		{
		    String mobileNo = rsSendSMS.getString(2);//mobileNo

		    funSendSettleBillSMS(voucherNo, mobileNo);

		}
		rsSendSMS.close();
		objPannelShowBills.funFillTableCombo();
		objPannelShowBills.funFillUnsettledBills();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-82", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    System.gc();
	}
    }

    private int funInsertPMSPostingBillDtlTable(String billNo, double settleAmt, HashMap<String, clsSettelementOptions> hmSettlemetnOptions) throws Exception
    {
	int retValue = 0;

	try
	{
	    String bSettleAmt = "dblSettleAmt";
	    if (clsGlobalVarClass.gPOSToWebBooksPostingCurrency.equalsIgnoreCase("USD"))
	    {
		bSettleAmt = "dblSettleAmt/" + clsGlobalVarClass.gUSDConvertionRate + "";
	    }

	    for (clsSettelementOptions obj : hmSettlemetnOptions.values())
	    {
		if (obj.getStrSettelmentType().equals("Room"))
		{
		    String billType = new com.POSGlobal.controller.clsUtility2().funGetBillType(billNo);
		    String sqlInsertPMSDtl = "insert into tblpmspostingbilldtl (strBillNo,strPOSCode,dteBillDate,"+bSettleAmt+""
			    + ",strFolioNo,strGuestCode,strRoomNo,strClientCode,strDataPostFlag,strPMSDataPostFlag,strBillType) "
			    + "values ('" + billNo + "','" + clsGlobalVarClass.gPOSCode + "','" + objUtility.funGetPOSDateForTransaction() + "','" + settleAmt + "'"
			    + ",'" + obj.getStrFolioNo() + "','" + obj.getStrGuestCode() + "','" + obj.getStrRoomNo() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "','N','N','" + billType + "')";
		    System.out.println(sqlInsertPMSDtl);
		    clsGlobalVarClass.dbMysql.execute(sqlInsertPMSDtl);
		}
	    }
	    retValue = 1;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    return retValue;
	}

    }

    private void funSaveBillForDB()
    {
	boolean flgSettle = false;
	try
	{
	    String strCustomerCode = "";
	    if (null != clsGlobalVarClass.gCustomerCode)
	    {
		strCustomerCode = clsGlobalVarClass.gCustomerCode;
	    }
	    String operationType = "DirectBiller";
	    String transactionType = "Direct Biller";  //For saving different transaction on same Bill in tblBillHd table in database
	    int ex = 0;

	    if (hmSettlemetnOptions.isEmpty())
	    {
		new frmOkPopUp(null, "Select Settlement mode", "Warning", 1).setVisible(true);
		return;
	    }
	    if (flgEnterBtnPressed == false)
	    {
		new frmOkPopUp(null, "Please Press Enter Key", "Warning", 1).setVisible(true);
		return;
	    }
	    else
	    {
		flgSettle = true;

		//Bill series code 
		Map<String, List<clsBillItemDtl>> mapBillSeries = null;
		listBillSeriesBillDtl = new ArrayList<>();
		if (clsGlobalVarClass.gEnableBillSeries && (mapBillSeries = objBillSettlementUtility.funGetBillSeriesList()).size() > 0)
		{
		    if (mapBillSeries.containsKey("NoBillSeries"))
		    {
			new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
			return;
		    }
		    Iterator<Map.Entry<String, List<clsBillItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
		    while (billSeriesIt.hasNext())
		    {
			Map.Entry<String, List<clsBillItemDtl>> billSeriesEntry = billSeriesIt.next();
			String key = billSeriesEntry.getKey();
			List<clsBillItemDtl> values = billSeriesEntry.getValue();

			funGenerateBillNoForBillSeriesForDirectBiller(key, values);
		    }

		    //save bill series bill detail
		    for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
		    {
			clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();

			String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl "
				+ "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag"
				+ ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) "
				+ "values ('" + clsGlobalVarClass.gPOSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'"
				+ ",'" + hdBillNo + "','" + objBillSettlementUtility.funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'"
				+ ",'" + grandTotal + "'" + ",'" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + objUtility.funGetPOSDateForTransaction() + "')";
			clsGlobalVarClass.dbMysql.execute(sqlInsertBillSeriesDtl);

			String sql = "select * "
				+ "from tblbillcomplementrydtl a "
				+ "where a.strBillNo='" + hdBillNo + "' "
				+ "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'; ";
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
			clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
			String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
			objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());
			if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 8") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 9") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 21") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 22") 
				|| clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 11"))//XO
			{
			    break;
			}
		    }

		    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		    {
			if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
			{
			    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
			}
		    }
		}
		else//if no bill series
		{

		    flgSettle = true;
		    objBillSettlementUtility.funGenerateBillNo();
		    //last order no
		    int intLastOrderNo = objUtility2.funGetLastOrderNo();

		    funSaveBillDiscountDetail(voucherNo);
		    int row = 0;
		    List<clsBillSettlementDtl> listObjBillSettlementDtl = new ArrayList<clsBillSettlementDtl>();
		    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
		    {
			double settleAmt = 0;
			if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
			{
			    settleAmt = ob.getDblSettlementAmt();
			}
			else
			{
			    settleAmt = ob.getDblPaidAmt();
			}

			if (ob.getStrSettelmentType().equals("Debit Card"))
			{
			    objUtility.funDebitCardTransaction(voucherNo, debitCardNo, settleAmt, "Settle");
			    objUtility.funUpdateDebitCardBalance(debitCardNo, settleAmt, "Settle");
			}
			row++;

			clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl();
			objBillSettlementDtl.setStrBillNo(voucherNo);
			objBillSettlementDtl.setStrSettlementCode(ob.getStrSettelmentCode());
			objBillSettlementDtl.setDblSettlementAmt(settleAmt);
			objBillSettlementDtl.setDblPaidAmt(ob.getDblPaidAmt());
			objBillSettlementDtl.setStrExpiryDate("");
			objBillSettlementDtl.setStrCardName(ob.getStrCardName());
			objBillSettlementDtl.setStrRemark(ob.getStrRemark());
			objBillSettlementDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillSettlementDtl.setStrCustomerCode(customerCodeForCredit);
			objBillSettlementDtl.setDblActualAmt(ob.getDblActualAmt());
			objBillSettlementDtl.setDblRefundAmt(ob.getDblRefundAmt());
			objBillSettlementDtl.setStrGiftVoucherCode(ob.getStrGiftVoucherCode());
			objBillSettlementDtl.setStrDataPostFlag("N");
			listObjBillSettlementDtl.add(objBillSettlementDtl);
		    }
		    ex = funInsertBillSettlementDtlTable(listObjBillSettlementDtl);
		    funTruncateDebitCardTempTable();

		    if (row > 1)
		    {
			settleName = "MultiSettle";
		    }

		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions obj : hmSettlemetnOptions.values())
			{
			    if (obj.getStrSettelmentType().equals("Complementary"))
			    {
				_deliveryCharge = 0.00;
				break;
			    }
			}
		    }

		    if (ex > 0)
		    {
			if (custCode != null)
			{
			    strCustomerCode = custCode;
			    if (homeDelivery.equals("Y"))
			    {
				operationType = "HomeDelivery";
				transactionType = "Direct Biller" + "," + operationType;

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

				if (delPersonCode != null)
				{
				    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode"
					    + ",strDPCode,dteDate,tmeTime,strPOSCode,strCustAddressLine1,strClientCode"
					    + ",dblHomeDeliCharge) "
					    + "values('" + voucherNo + "','" + custCode + "','" + delPersonCode + "'"
					    + ",'" + clsGlobalVarClass.gPOSDateForTransaction + "','" + currentTime + "'"
					    + ",'" + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','" + clsGlobalVarClass.gClientCode + "'"
					    + "," + _deliveryCharge + ")";
				    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
				}
				else
				{
				    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode,dteDate,tmeTime"
					    + ",strPOSCode,strCustAddressLine1,strCustAddressLine2,strCustAddressLine3,strCustAddressLine4"
					    + ",strCustCity,strClientCode,dblHomeDeliCharge)"
					    + " values('" + voucherNo + "','" + custCode + "','"
					    + clsGlobalVarClass.gPOSDateForTransaction + "','" + currentTime + "','"
					    + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','',''"
					    + ",'','','" + clsGlobalVarClass.gClientCode + "'," + _deliveryCharge + ")";
				    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
				}
			    }
			}

			if (clsGlobalVarClass.gTakeAway.equals("Yes"))
			{
			    operationType = "TakeAway";
			    transactionType = "Direct Biller" + "," + operationType;
			}
			String counterCode = "NA";
			if (clsGlobalVarClass.gCounterWise.equals("Yes"))
			{
			    counterCode = clsGlobalVarClass.gCounterCode;
			}

			if (advOrderBookingNo.trim().length() > 0)
			{
			    String sql_AdvOrderCustCode = "select strCustomerCode from tbladvbookbillhd "
				    + "where strAdvBookingNo='" + advOrderBookingNo + "'";
			    ResultSet rsAdvOrderCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql_AdvOrderCustCode);
			    if (rsAdvOrderCustomer.next())
			    {
				strCustomerCode = rsAdvOrderCustomer.getString(1);
			    }
			    rsAdvOrderCustomer.close();
			}
			if (takeAway.equals("Yes"))
			{
			    operationType = "TakeAway";
			    transactionType = "Direct Biller" + "," + operationType;
			}

			double tempDiscAmt = 0, tempDiscPer = 0;
			double itemSubTotal = 0;
			for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
			{
			    clsBillItemDtl objBillItemDtl = entry.getValue();
			    tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
			    tempDiscPer = objBillItemDtl.getDiscountPercentage();
			    if (objBillItemDtl.getDiscountAmount() > 0)
			    {
				itemSubTotal += objBillItemDtl.getAmount();
			    }
			}
			if (itemSubTotal > 0)
			{
			    tempDiscPer = (tempDiscAmt * 100) / itemSubTotal;
			}

			//Insert into tblbillhd table
			clsBillHd objBillHd = new clsBillHd();
			objBillHd.setStrBillNo(voucherNo);
			objBillHd.setStrAdvBookingNo(advOrderBookingNo);
			objBillHd.setDteBillDate(objUtility.funGetPOSDateForTransaction());
			objBillHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
			objBillHd.setStrSettelmentMode(settleName);
			objBillHd.setDblDiscountAmt(dblDiscountAmt);
			objBillHd.setDblDiscountPer(dblDiscountPer);
			objBillHd.setDblTaxAmt(dblTotalTaxAmt);
			objBillHd.setDblSubTotal(_subTotal);
			objBillHd.setDblGrandTotal(_grandTotal);
			objBillHd.setDblGrandTotalRoundOffBy(_grandTotalRoundOffBy);
			objBillHd.setStrTakeAway(takeAway);
			objBillHd.setStrOperationType(operationType);
			objBillHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
			objBillHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
			objBillHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
			objBillHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
			objBillHd.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillHd.setStrTableNo("");
			objBillHd.setStrWaiterNo("");
			objBillHd.setStrCustomerCode(strCustomerCode);
			objBillHd.setStrManualBillNo(txtManualBillNo.getText());
			objBillHd.setIntShiftCode(clsGlobalVarClass.gShiftNo);
			objBillHd.setIntPaxNo(0);
			objBillHd.setStrDataPostFlag("N");
			objBillHd.setStrReasonCode(selectedReasonCode);
			objBillHd.setStrRemarks(objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()));
			objBillHd.setDblTipAmount(tipAmount);
			objBillHd.setDteSettleDate(objUtility.funGetPOSDateForTransaction());
			objBillHd.setStrCounterCode(counterCode);
			objBillHd.setDblDeliveryCharges(_deliveryCharge);
			objBillHd.setStrAreaCode(areaCode);
			objBillHd.setStrDiscountRemark(objUtility.funCheckSpecialCharacters(discountRemarks));
			objBillHd.setStrTakeAwayRemarks(objUtility.funCheckSpecialCharacters(takeAwayRemarks));
			objBillHd.setStrTransactionType(transactionType);
			objBillHd.setIntLastOrderNo(intLastOrderNo);
			objBillHd.setStrOnlineOrderNo(onlineOrderNo);

			String discountOn = "All";
			if (rdbAll.isSelected())
			{
			    discountOn = "All";
			}
			if (rdbItemWise.isSelected())
			{
			    discountOn = "Item";
			}
			if (rdbGroupWise.isSelected())
			{
			    discountOn = "Group";
			}
			if (rdbSubGroupWise.isSelected())
			{
			    discountOn = "SubGroup";
			}
			objBillHd.setStrDiscountOn(discountOn);
			objBillHd.setStrCardNo(debitCardNo);
			objBillHd.setDblUSDConvertionRate(clsGlobalVarClass.gUSDConvertionRate);

			funInsertBillHdTable(objBillHd);

			if (isCheckOutPlayZone)
			{
			    funUpdateRegisterInOutPlayZone(voucherNo);
			}

			if (settleType.equals("Credit"))
			{
			    String sql = "update tblbillhd set strCustomerCode='" + customerCodeForCredit + "' "
				    + "where strBillNo='" + voucherNo + "'";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}

			if (clsGlobalVarClass.gCMSIntegrationYN)
			{
			    if (custCode.trim().length() > 0)
			    {
				String sqlDeleteCustomer = "delete from tblcustomermaster "
					+ "where strCustomerCode='" + custCode + "' "
					+ "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
				clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

				String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
					+ ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
					+ "values('" + custCode + "','" + cmsMemberName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
					+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
					+ ",'" + clsGlobalVarClass.gClientCode + "')";
				clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
			    }
			}

			// Insert into tblbilldtl table    
			List<clsBillDtl> listObjBillDtl = new ArrayList<clsBillDtl>();
			for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
			{
			    if (!listclsItemRow.isIsModifier())
			    {
				double rate = 0.00;
				rate = listclsItemRow.getAmt() / listclsItemRow.getQty();

				clsBillDtl objBillDtl = new clsBillDtl();
				//double discAmt=hmBillItemDtl.get(listclsItemRow.getItemCode()).getDiscountAmount();
				double discAmt = hmBillItemDtl.get(listclsItemRow.getItemCode()).getDiscountAmount() * listclsItemRow.getQty();
				double discPer = hmBillItemDtl.get(listclsItemRow.getItemCode()).getDiscountPercentage();

				objBillDtl.setStrItemCode(listclsItemRow.getItemCode());
				objBillDtl.setStrItemName(listclsItemRow.getItemName());
				objBillDtl.setStrAdvBookingNo("");
				objBillDtl.setStrBillNo(voucherNo);
				objBillDtl.setDblRate(rate);
				objBillDtl.setDblQuantity(listclsItemRow.getQty());
				objBillDtl.setDblAmount(listclsItemRow.getAmt());
				objBillDtl.setDblTaxAmount(0);
				objBillDtl.setDteBillDate(objUtility.funGetPOSDateForTransaction());
				objBillDtl.setStrKOTNo("");
				objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				objBillDtl.setStrCounterCode(strCustomerCode);
				objBillDtl.setTmeOrderProcessing("00:00:00");
				objBillDtl.setStrDataPostFlag("N");
				objBillDtl.setStrMMSDataPostFlag("N");
				objBillDtl.setStrManualKOTNo("");
				objBillDtl.setTdhYN(listclsItemRow.getTdhComboItemYN());
				objBillDtl.setStrPromoCode(listclsItemRow.getPromoCode());
				objBillDtl.setStrCounterCode(counterCode);
				objBillDtl.setStrWaiterNo("NA");
				objBillDtl.setDblDiscountAmt(discAmt);
				objBillDtl.setDblDiscountPer(discPer);
				objBillDtl.setSequenceNo(listclsItemRow.getSeqNo());
				objBillDtl.setStrOrderPickupTime("00:00:00");
				listObjBillDtl.add(objBillDtl);
			    }
			}
			funInsertBillDtlTable(listObjBillDtl);

			// Insert into tblbillmodifierdtl
			List<clsBillModifierDtl> listObjBillModBillDtls = new ArrayList<clsBillModifierDtl>();
			for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
			{
			    double rate = listclsItemRow.getAmt() / listclsItemRow.getQty();
			    double amt = 0.00;

			    boolean flgComplimentaryBill1 = false;
			    if (hmSettlemetnOptions.size() == 1)
			    {
				for (clsSettelementOptions obj : hmSettlemetnOptions.values())
				{
				    if (obj.getStrSettelmentType().equals("Complementary"))
				    {
					flgComplimentaryBill1 = true;
					break;
				    }
				}
			    }
			    if (!flgComplimentaryBill1)
			    {
				amt = listclsItemRow.getAmt();
			    }

			    if (listclsItemRow.isIsModifier())
			    {
				clsBillModifierDtl objBillModDtl = new clsBillModifierDtl();
				objBillModDtl.setStrBillNo(voucherNo);
				objBillModDtl.setStrItemCode(listclsItemRow.getItemCode());
				objBillModDtl.setStrModifierCode(listclsItemRow.getModifierCode());
				objBillModDtl.setStrModifierName(listclsItemRow.getItemName());
				objBillModDtl.setDblRate(rate);
				objBillModDtl.setDblQuantity(listclsItemRow.getQty());
				objBillModDtl.setDblAmount(amt);
				objBillModDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
				objBillModDtl.setStrCustomerCode(strCustomerCode);
				objBillModDtl.setStrDataPostFlag("N");
				objBillModDtl.setStrMMSDataPostFlag("N");
				objBillModDtl.setStrDefaultModifierDeselectedYN(listclsItemRow.getStrDefaultModifierDeselectedYN());
				objBillModDtl.setSequenceNo(listclsItemRow.getSeqNo());
				objBillModDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

				double modDiscAmt = 0;
				double modDiscPer = 0;
				if (hmBillItemDtl.containsKey(listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName()))
				{
				    modDiscAmt = hmBillItemDtl.get(listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName()).getDiscountAmount() * listclsItemRow.getQty();
				    modDiscPer = hmBillItemDtl.get(listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName()).getDiscountPercentage();
				}

				objBillModDtl.setDblDiscPer(modDiscPer);
				objBillModDtl.setDblDiscAmt(modDiscAmt);

				listObjBillModBillDtls.add(objBillModDtl);
			    }
			}
			funInsertBillModifierDtlTable(listObjBillModBillDtls);

			// insert into tblbilltaxdtl    
			List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

			for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
			{
			    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
			    clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
			    objBillTaxDtl.setStrBillNo(voucherNo);
			    objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
			    objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
			    objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
			    objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			    objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			    listObjBillTaxBillDtls.add(objBillTaxDtl);
			}

			funInsertBillTaxDtlTable(listObjBillTaxBillDtls);

			clsUtility obj = new clsUtility();
			obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);
			//funShiftAdvOrderDataToQFile(voucherNo);

			// For Complimentary Bill
			funClearComplimetaryBillAmt(voucherNo);
			if (clsGlobalVarClass.gBillSettleSMSYN)
			{
			    objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gBillSettlementSMS, "");
			}

			if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("Sqy") && clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
			{
			    if (null != custCode)
			    {
				funSaveCRMPoints(voucherNo, _grandTotal, "Sqy", custMobileNoForCRM, objUtility.funGetPOSDateForTransaction());
				funCallPostWebService(_grandTotal, "Direct Biller", voucherNo);
			    }
			}
			else if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("JPOS"))
			{
			    if (null != custCode)
			    {
				funSaveCRMPoints(voucherNo, _grandTotal, "JPOS", custMobileNoForCRM, objUtility.funGetPOSDateForTransaction());
			    }
			}
			else if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && custCode != null && !custCode.isEmpty())
			{
			    Thread crmThread = new Thread()
			    {
				@Override
				public void run()
				{
				    try
				    {
					String mobileNo = "";
					String sql_CustMb = "select longMobileNo from tblcustomermaster "
						+ "where strCustomerCode='" + custCode + "'";
					ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
					if (rsCust.next())
					{
					    mobileNo = rsCust.getString(1);
					}
					rsCust.close();

					if (mobileNo.trim().isEmpty())
					{
					    System.out.println("No Mobile no");
					    return;
					}
					clsCRMInterface objCRMInterface = new clsCRMInterface();

					objCRMInterface.funPostBillDataCRM(custCode, voucherNo, clsGlobalVarClass.gPOSOnlyDateForTransaction);

					if (!rewardId.isEmpty())
					{
					    objCRMInterface.funPostRewardRedeemCRM(mobileNo, rewardId);
					}//rewardId,2300,2057
					rewardId = "";
				    }
				    catch (Exception e)
				    {
					objUtility.funShowDBConnectionLostErrorMessage(e);	
					e.printStackTrace();
				    }
				}
			    };
			    crmThread.start();

			}

			lblVoucherNo.setText(voucherNo);

			if (clsGlobalVarClass.gKOTPrintingEnableForDirectBiller)
			{
			    if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
			    {
				funTextFilePrintingKOTForDirectBiller();
			    }
			    else
			    {
				funTextFilePrintingKOTForDirectBiller();
			    }
			}
			objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());
			
			//send settleed bill MSG                   
			String sql = "select a.strSendSMSYN,a.longMobileNo "
				+ "from tblsmssetup a "
				+ "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
				+ "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
				+ "and a.strTransactionName='SettleBill' "
				+ "and a.strSendSMSYN='Y'; ";
			ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsSendSMS.next())
			{
			    String mobileNo = rsSendSMS.getString(2);//mobileNo

			    funSendSettleBillSMS(voucherNo, mobileNo);

			}
			rsSendSMS.close();
		    }
		    dispose();

		    clsGlobalVarClass.gTakeAway = "No";
		    clsGlobalVarClass.gDeliveryCharges = 0.00;
		    funResetVariableValues();

		    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		    {
			if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
			{
			    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-26", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	finally
	{
	    if (flgSettle)
	    {
		if (null != objDirectBiller)
		{
		    objDirectBiller = null;
		}
		System.gc();
	    }
	}
    }

    private void funSendBillSettleSMS(String billNo) throws Exception
    {
	String sql_CustMb = "select a.longMobileNo from tblcustomermaster a,tblbillhd b "
		+ "where a.strCustomerCode=b.strCustomerCode";
	ResultSet rsCustMb = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
	if (rsCustMb.next())
	{
	    String sms = clsGlobalVarClass.gBillSettlementSMS;

	    ArrayList<String> mobileNoList = new ArrayList<>();
	    mobileNoList.add(rsCustMb.getString(1));
	    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, sms);
	    objSMSSender.start();
	}
    }

    private void funCallPostWebService(double pointsAmount, String transType, String billNo)
    {
	try
	{
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    HttpPost postRequest = new HttpPost(clsGlobalVarClass.gPostWebserviceURL);

	    double redeemAmt = Double.parseDouble(txtDiscountAmt.getText());
	    String posId = clsGlobalVarClass.gPOSID;
	    String custMobileNo = "", transactionId = "", outletUId = "";

	    String sql_CRMPoints = "select dblPoints,strTransactionId,strOutletUID,dblRedeemedAmt,longCustMobileNo "
		    + "from tblcrmpoints where strBillNo='" + billNo + "'";
	    ResultSet rsCRM = clsGlobalVarClass.dbMysql.executeResultSet(sql_CRMPoints);
	    if (rsCRM.next())
	    {
		custMobileNo = rsCRM.getString(5);
		transactionId = rsCRM.getString(2);
		outletUId = rsCRM.getString(3);
	    }
	    rsCRM.close();

	    String postString = "{\"redemption\":{\"action\":\"accept\",\"redeemed_amt\":\"" + redeemAmt
		    + "\",\"transaction_id\":\"" + transactionId + "\""
		    + ",\"pos_id\":" + posId + ",\"outlet_uuid\":\"" + outletUId + "\""
		    + "},\"addition\":{\"phone_number\":" + custMobileNo + ",\"bill_amt\":" + pointsAmount + ","
		    + "\"pos_id\":" + posId + ",\"outlet_uuid\":\"" + outletUId + "\"}}";

	    StringEntity input = new StringEntity(postString);
	    input.setContentType("application/json");
	    postRequest.setEntity(input);

	    HttpResponse response = httpClient.execute(postRequest);
	    if (response.getStatusLine().getStatusCode() == 0)
	    {
		/*
                 * throw new RuntimeException("Failed : HTTP error code : " +
                 * response.getStatusLine().getStatusCode());
		 */
	    }

	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		System.out.println(output);
	    }
	    httpClient.getConnectionManager().shutdown();
	    clsGlobalVarClass.gFlgPoints = "";
	    clsGlobalVarClass.gNumerickeyboardValue = "";
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funInsertPointsOnBill(String billNo, double billAmount, String customerCodeForBill) throws Exception
    {
	double points = 0, pointValue = 0;
	String sql_PointsOnBill = "select strLoyaltyCode,dblAmount,dblLoyaltyPoints,dblLoyaltyPoints1,dblLoyaltyValue "
		+ "from tblloyaltypoints";
	ResultSet rsLoyaltyPoint = clsGlobalVarClass.dbMysql.executeResultSet(sql_PointsOnBill);
	if (rsLoyaltyPoint.next())
	{
	    points = billAmount / Double.parseDouble(rsLoyaltyPoint.getString(2));
	    pointValue = points * (Double.parseDouble(rsLoyaltyPoint.getString(5)));
	    if (pointValue > 0)
	    {
		sql_PointsOnBill = "insert into tblpointsonbill (strBillNo,dteBillDate,dblBillAmount,dblPointsEarned"
			+ ",strCustomerCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
			+ "values('" + billNo + "','" + objUtility.funGetPOSDateForTransaction() + "'," + billAmount + ""
			+ "," + Math.rint(pointValue) + ",'" + customerCodeForBill + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
		clsGlobalVarClass.dbMysql.execute(sql_PointsOnBill);
	    }
	}
	rsLoyaltyPoint.close();
    }

    private int procSettlementBtnClick(clsSettelementOptions objSettelement)
    {
	try
	{

	    lblPaymentMode.setText(objSettelement.getStrSettelmentDesc());
	    panelMode.setVisible(true);
	    dblSettlementAmount = 0.00;
	    settleName = objSettelement.getStrSettelmentDesc();
	    settleType = objSettelement.getStrSettelmentType();
	    settlementCode = objSettelement.getStrSettelmentCode();//use while calculating tax for settlement
	    currencyRate = objSettelement.getDblConvertionRatio();
	    billPrintOnSettlement = objSettelement.getStrBillPrintOnSettlement();

	    if (hmSettlemetnOptions.isEmpty())
	    {
		funRefreshItemTable();
	    }//if the tax changes on settlement mode

	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		dblSettlementAmount = Math.rint(_balanceAmount);
	    }
	    else
	    {
		dblSettlementAmount = Double.parseDouble(gDecimalFormat.format(_balanceAmount));
	    }

	    dblSettlementAmount = dblSettlementAmount * currencyRate;

	    //System.out.println(settleType);
	    switch (settleType)
	    {
		case "Loyality Points":
		    settlementName = "others";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    txtPaidAmt.setText(gDecimalFormat.format(dblSettlementAmount));
		    txtAmount.setText(gDecimalFormat.format(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    chkJioNotification.setSelected(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Cash":
		    settlementName = "others";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    txtRemark.requestFocus();
//                    PanelRemaks.setLocation(PanelCheque.getLocation());
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Credit Card":
		    settlementName = "others";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    PanelCard.setVisible(true);
		    panelAmt.setVisible(true);
		    PanelCard.setLocation(PointCheque);
		    panelAmt.setVisible(true);
		    txtRemark.requestFocus();
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    PanelGiftVoucher.setVisible(false);
		    PanelCoupen.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(true);
		    txtTip.setVisible(true);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Coupon":
		    settlementName = "others";
		    amountBox = "CouponAmount";
		    settleMode = true;
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCoupen.setVisible(true);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    PanelCoupen.setLocation(PointCash);
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    txtCoupenAmt.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Cheque":
		    settlementName = "others";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCoupen.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    panelAmt.setVisible(true);
		    PanelCheque.setVisible(true);
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Gift Voucher":
		    settlementName = "Gift voucher";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    PanelCard.setVisible(false);
		    PanelCoupen.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    PanelGiftVoucher.setVisible(true);
		    PanelGiftVoucher.setLocation(PointCheque);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Complementary":
		    txtRemark.requestFocus();
		    panelCustomer.setVisible(false);
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(false);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "Complementry";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    //flgComplementarySettle = true;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Credit":

		    StringBuilder strBuilder = new StringBuilder("select a.strCustomerSelectionOnBillSettlement from tblsettelmenthd a" 
			    + " where a.strSettelmentType='"+settleType+"' and a.strSettelmentCode='"+settlementCode+"'");
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(strBuilder.toString());
		    if(rs.next())
		    {
			if(rs.getString(1).equalsIgnoreCase("Y"))
			{
			    objUtility.funCallForSearchForm("CustomerMaster");
			    new frmSearchFormDialog(this, true).setVisible(true);

			    if (clsGlobalVarClass.gSearchItemClicked)
			    {
				Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
				lblCreditCustCode.setText(data[0].toString());
				customerCodeForCredit = lblCreditCustCode.getText();
				txtCoupenAmt.setText(String.valueOf(dblSettlementAmount));
				txtCustomerName.setText(data[1].toString());
				clsGlobalVarClass.gSearchItemClicked = false;
			    }
			}   
			else
			{
			    if (custCode != null && custCode.trim().length() > 0)
			    {
				lblCreditCustCode.setText(custCode);
				customerCodeForCredit = lblCreditCustCode.getText();
				txtCoupenAmt.setText(String.valueOf(dblSettlementAmount));
				txtCustomerName.setText(clsGlobalVarClass.gCustomerName);
			    }
			    else
			    {
				objUtility.funCallForSearchForm("CustomerMaster");
				new frmSearchFormDialog(this, true).setVisible(true);

				if (clsGlobalVarClass.gSearchItemClicked)
				{
				    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
				    lblCreditCustCode.setText(data[0].toString());
				    customerCodeForCredit = lblCreditCustCode.getText();
				    txtCoupenAmt.setText(String.valueOf(dblSettlementAmount));
				    txtCustomerName.setText(data[1].toString());
				    clsGlobalVarClass.gSearchItemClicked = false;
				}
			    } 
			}    
		    }

		    lblCreditCustCode.setVisible(false);
		    //PanelCoupen.setLocation(panelAmt.getLocation());
		    PanelCoupen.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelCustomer.setVisible(true);
		    panelCustomer.setLocation(panelDiscount.getLocation());
		    txtAreaRemark.requestFocus();
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "Credit";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Debit Card":

		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelGiftVoucher.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblTipAmount.setVisible(false);
		    PanelCheque.setVisible(false);
		    PanelCard.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelBenowSettlement.setVisible(false);
		    if (!tableNo.isEmpty())  // For KOT
		    {
			debitCardNo = funCardNo();
			if (debitCardNo.isEmpty())
			{
			    new frmSwipCardPopUp(this).setVisible(true);
			    if (clsGlobalVarClass.gDebitCardNo != null)
			    {
				ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet("select strCardNo from tbldebitcardmaster where strCardString='" + clsGlobalVarClass.gDebitCardNo + "'");
				if (rsDebitCardNo.next())
				{
				    debitCardNo = rsDebitCardNo.getString(1);
				}
				rsDebitCardNo.close();
			    }
			}
			if (!debitCardNo.isEmpty())
			{
			    clsUtility objUtility = new clsUtility();
			    //double debitCardBalance = objUtility.funGetDebitCardBalance(debitCardNo);
			    double debitCardBalance = objUtility.funGetDebitCardBalanceWithoutLiveBills(clsGlobalVarClass.gDebitCardNo, tableNo);

			    if (objUtility.funGetDebitCardStatus(debitCardNo, "CardNo").equalsIgnoreCase("Card is Not Active"))
			    {
				new frmOkPopUp(null, "This Card is not Activated ", "Warning", 1).setVisible(true);
			    }
			    else if (debitCardBalance < 0)
			    {
				new frmOkPopUp(null, "Card Balance is Negative", "Warning", 1).setVisible(true);
			    }
			    else
			    {
				lblcard.setVisible(true);
				chkJioNotification.setSelected(false);
				lblCardBalance.setVisible(true);
				lblCardBalance.setText(String.valueOf(debitCardBalance));
				amountBox = "PaidAmount";
				settlementName = "others";
				settleMode = true;
				panelAmt.setVisible(true);
				PanelGiftVoucher.setVisible(false);
				PanelJioMoneySettlement.setVisible(false);
				txtAmount.setText(String.valueOf(dblSettlementAmount));
				if (dblSettlementAmount > debitCardBalance)
				{
				    txtPaidAmt.setText(String.valueOf(debitCardBalance));
				    lblCardBalance.setBackground(Color.red);
				}
				else
				{
				    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
				    lblCardBalance.setBackground(Color.yellow);
				}
			    }
			}
		    }
		    else // For Direct Biller
		    {
			if (null == clsGlobalVarClass.gDebitCardNo || clsGlobalVarClass.gDebitCardNo.trim().isEmpty())
			{
			    new frmSwipCardPopUp(this).setVisible(true);
			}
			if (clsGlobalVarClass.gDebitCardNo != null && !clsGlobalVarClass.gDebitCardNo.trim().isEmpty())
			{
			    clsUtility objUtility = new clsUtility();
			    ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet("select strCardNo from tbldebitcardmaster where strCardString='" + clsGlobalVarClass.gDebitCardNo + "'");
			    if (rsDebitCardNo.next())
			    {
				debitCardNo = rsDebitCardNo.getString(1);
				//double debitCardBalance = objUtility.funGetDebitCardBalance(debitCardNo);
				double debitCardBalance = objUtility.funGetDebitCardBalanceWithoutLiveBills(clsGlobalVarClass.gDebitCardNo, tableNo);

				if (objUtility.funGetDebitCardStatus(debitCardNo, "CardNo").equalsIgnoreCase("Card is Not Active"))
				{
				    new frmOkPopUp(null, "This Card is not Activated ", "Warning", 1).setVisible(true);
				}
				else if (debitCardBalance < 0)
				{
				    new frmOkPopUp(null, "Card Balance is Negative", "Warning", 1).setVisible(true);
				}
				else
				{
				    lblcard.setVisible(true);
				    lblCardBalance.setVisible(true);
				    chkJioNotification.setSelected(false);
				    lblCardBalance.setText(String.valueOf(debitCardBalance));
				    amountBox = "PaidAmount";
				    settlementName = "others";
				    settleMode = true;
				    panelAmt.setVisible(true);
				    PanelGiftVoucher.setVisible(false);
				    PanelJioMoneySettlement.setVisible(false);
				    txtAmount.setText(String.valueOf(dblSettlementAmount));
				    if (dblSettlementAmount > debitCardBalance)
				    {
					txtPaidAmt.setText(String.valueOf(debitCardBalance));
					lblCardBalance.setBackground(Color.red);
				    }
				    else
				    {
					txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
					lblCardBalance.setBackground(Color.yellow);
				    }
				}
			    }
			    rsDebitCardNo.close();
			}
			PanelCard.setLocation(PointCheque);
		    }

		    break;

		case "Member":

		    if (clsGlobalVarClass.gCMSIntegrationYN)
		    {
			cmsMemberCreditLimit = 0;
			if (custCode.trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Member is Not Selected for This Bill!!!");
			    return 0;
			}
			else
			{
			    if (clsGlobalVarClass.gCMSIntegrationYN)
			    {
				//String memberInfo=funCheckMemeberBalance(custCode);
				clsUtility objUtility = new clsUtility();
				String memberInfo = objUtility.funCheckMemeberBalance(custCode);
				if (memberInfo.equals("no data"))
				{
				    JOptionPane.showMessageDialog(null, "Member Not Found!!!");
				}
				else
				{
				    String[] spMemberInfo = memberInfo.split("#");
				    double balance = Double.parseDouble(spMemberInfo[2]);
				    cmsMemberBalance = Double.parseDouble(spMemberInfo[3]);
				    String info = spMemberInfo[0] + "#" + spMemberInfo[1] + "#" + balance;
				    txtAreaRemark.setText(info);
				    cmsMemberCreditLimit = Double.parseDouble(spMemberInfo[3]);
				    cmsStopCredit = spMemberInfo[6];
				}
			    }
			}
		    }
		    settlementName = "Member";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    panelCustomer.setVisible(false);
		    txtRemark.requestFocus();
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    chkJioNotification.setSelected(false);
		    PanelCheque.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Room":

		    clsInvokeDataFromSanguineERPModules objSangERP = new clsInvokeDataFromSanguineERPModules();
		    List<clsGuestRoomDtl> listOfGuestRoomDtl = objSangERP.funGetGuestRoomDtl();
		    new frmSearchFormDialog("Guest Room Detail", listOfGuestRoomDtl, this, true).setVisible(true);
		    objSangERP = null;
		    //funOpenCustomerMaster();

		    if (clsGlobalVarClass.gSearchItemClicked)
		    {
			Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			txtGuestName.setText(data[0].toString());//guestName
			txtRoomNo.setText(data[2].toString());//roomNo                        
			txtFolioNo.setText(data[3].toString());//folioNo                                                
			txtGuestCode.setText(data[4].toString());//guestCode                                                
			clsGlobalVarClass.gSearchItemClicked = false;
		    }

		    PanelCoupen.setVisible(false);
		    panelCustomer.setVisible(false);
		    PanelRemaks.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "Room";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    chkJioNotification.setSelected(false);
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    lblCreditCustCode.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    panelRoomSettlement.setLocation(panelDiscount.getLocation());
		    panelRoomSettlement.setVisible(true);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "JioMoney":

		    btnJioMoneyCheckStatus.setVisible(false);
		    if (settleName.equals("JM Code"))
		    {
			lblJioMoneyCode.setVisible(true);
			txtJioMoneyCode.setVisible(true);
			lblJioMoneyCode.setText("Scan/Enter Code");
			lblJioDestination.setText("");
			txtJioDestination.setText("");
			chkJioNotification.setSelected(false);
			lblJioDestination.setVisible(false);
			txtJioDestination.setVisible(false);
		    }
		    else
		    {
			lblJioMoneyCode.setText("SMS/Email:");
			lblJioMoneyCode.setVisible(true);
			txtJioMoneyCode.setText("");
			txtJioMoneyCode.setVisible(true);
			lblJioDestination.setText("");
			txtJioDestination.setText("");
			lblJioDestination.setVisible(false);
			txtJioDestination.setVisible(false);
			chkJioNotification.setSelected(true);
		    }
		    PanelCoupen.setVisible(false);
		    panelCustomer.setVisible(false);
		    PanelRemaks.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "JioMoney";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    lblCreditCustCode.setVisible(false);
		    PanelJioMoneySettlement.setLocation(panelDiscount.getLocation());
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(true);
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Online Payment":
		    settlementName = "Online Payment";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    txtRemark.requestFocus();
		    PanelRemaks.setLocation(PanelCoupen.getLocation());
		    PanelRemaks.setVisible(true);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");
		    PanelBenowSettlement.setVisible(false);

		    break;

		case "Benow":
		    // String QRString="upi://pay?pa=AF8Y1@yesbank&pn=INTEGRATIONTESTMERCHANT&mc=5499&tr=WQ02&tn=jhjkssa&am=1&cu=INR"; 
		    PanelCoupen.setVisible(false);
		    panelCustomer.setVisible(false);
		    PanelRemaks.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "JioMoney";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    lblCreditCustCode.setVisible(false);
		    chkJioNotification.setSelected(false);
		    panelRoomSettlement.setVisible(false);
		    PanelJioMoneySettlement.setVisible(false);
		    txtJioDestination.setVisible(false);
		    lblJioDestination.setVisible(false);
		    lblJioDestination.setText("");

		    if (clsGlobalVarClass.gBenowIntegrationYN)
		    {
			clsBenowIntegration objBenow = new clsBenowIntegration();
			QRStringForBenow = objBenow.funGetDynamicQRString(voucherNo, dblSettlementAmount); //add dblSettlementAmount
			if (!QRStringForBenow.equalsIgnoreCase("NotFound"))
			{
			    PanelBenowSettlement.setVisible(true);
			    objCalculateBillDisc.funGenerateQrCode(QRStringForBenow);

			    //send payment SMS link
			    //objBenow.funSendPaymenetLinkSMS(voucherNo, dblSettlementAmount,custCode);
			}
		    }
		    break;

	    }
	    if (noOfSettlementMode == 1)
	    {
		if (clsGlobalVarClass.gTransactionType.equals("Direct Biller"))
		{
		    funEnterButtonPressed();
		    funSettleButtonPressed();
		}
		else if (clsGlobalVarClass.gTransactionType.equals("Bill From KOT") || clsGlobalVarClass.gTransactionType.equals("SettleBill"))
		{
		    funEnterButtonPressed();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-27", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	return 1;
    }

    public void funCloseForm()
    {
	objPannelShowBills = null;
	dispose();
    }

    //Delete the all records from tblItemTemp
    private void truncateTable()
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute("delete from  tblitemtemp where strUserCreated='" + clsGlobalVarClass.gUserCode + "'");
	    clsGlobalVarClass.dbMysql.execute("truncate table tbltemphomedelv");
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-29", JOptionPane.ERROR_MESSAGE);
	}
    }

    //Delete the all records from debitcardtemptables
    private void funTruncateDebitCardTempTable()
    {
	try
	{
	    clsGlobalVarClass.gDebitCardNo = "";
	    clsGlobalVarClass.dbMysql.execute("delete from tbldebitcardtabletemp where strTableNo='" + tableNo + "'");
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-30", JOptionPane.ERROR_MESSAGE);
	}
    }

    //Calculator code
    private void procNumericValue(String strValue)
    {
	try
	{
	    if (amountBox.equals("PaidAmount") && settlementName.equalsIgnoreCase("others"))
	    {
		textValue2 = textValue2 + strValue;
		txtPaidAmt.setText(textValue2);
	    }
	    else if (amountBox.equals("PaidAmount") && settlementName.equalsIgnoreCase("Gift Voucher"))
	    {
		txtPaidAmt.setText("");
	    }
	    else if (amountBox.equals("txtAmount"))
	    {
		textValue1 = textValue1 + strValue;
		txtAmount.setText(textValue1);
	    }
	    else if (amountBox.equals("discount"))
	    {
		if (discountType.equals("Percent"))
		{
		    textValue1 = textValue1 + strValue;
		    txtDiscountPer.setText(textValue1);
		}
		else
		{
		    textValue1 = textValue1 + strValue;
		    txtDiscountAmt.setText(textValue1);
		}
	    }
	    else if (amountBox.equals("CouponAmount"))
	    {
		textValue1 = textValue1 + strValue;
		txtCoupenAmt.setText(textValue1);
	    }
	    else if (amountBox.equals("tip"))
	    {
		textValue1 = textValue1 + strValue;
		txtTip.setText(textValue1);
	    }
	    else if (amountBox.equals("delcharges"))
	    {
		textValue1 = textValue1 + strValue;
		txtDeliveryCharges.setText(textValue1);
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-31", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

//Calculator code
    private void procEnterValue(String strValue)
    {
	if (amountBox.equals("PaidAmount"))
	{
	    if (panelAmt.isVisible())
	    {
		txtPaidAmt.setText(strValue);
		dyn1 = true;
	    }
	    else if (PanelCoupen.isVisible())
	    {
		txtCoupenAmt.setText(strValue);
		dyn1 = true;
	    }
	}
	else if (amountBox.equals("txtAmount"))
	{
	    txtAmount.setText(strValue);
	}
	else if (amountBox.equals("CouponAmount"))
	{
	    txtCoupenAmt.setText(strValue);
	}
    }

    /**
     * :- Ritesh 18 oct 2014
     *
     * @param refund_Amount
     * @param settleName set text to refund amount if _paidAmount is greater
     * than balance amount
     */
    private void funRefundAmount(double refund_Amount, String settleName)
    {
	lblRefund.setText("Refund Amount      (" + settleName + ")" + refund_Amount);
    }

    private void procClear()
    {
	txtCardName.setText("");
	txtCoupenAmt.setText("");
	PanelCoupen.setVisible(false);
	panelAmt.setVisible(false);
	PanelCard.setVisible(false);
	panelMode.setVisible(false);
	PanelCheque.setVisible(false);
	PanelGiftVoucher.setVisible(false);
	PanelRemaks.setVisible(false);
	panelCustomer.setVisible(false);
	txtAmount.setEnabled(false);
	flgGiftVoucherOK = false;
	lblTipAmount.setVisible(false);
	txtTip.setVisible(false);
	lblCreditCustCode.setText("");

    }

    public void funSetDeliveryCharges(double delCharges)
    {
	double billAmt = (Double.parseDouble(txtAmount.getText()) - clsGlobalVarClass.gDeliveryCharges) + delCharges;
	txtAmount.setText(String.valueOf(billAmt));
	txtPaidAmt.setText(String.valueOf(billAmt));
	txtDeliveryCharges.setText(String.valueOf(delCharges));
	amountBox = "";
    }

    private void funPrevSettlementMode()
    {
	try
	{
	    _settlementNavigate--;
	    if (_settlementNavigate == 0)
	    {
		btnPrevSettlementMode.setEnabled(false);
		btnNextSettlementMode.setEnabled(true);
		funFillSettlementButtons(0, noOfSettlementMode);
	    }
	    else
	    {
		btnNextSettlementMode.setEnabled(true);
		int startIndex = (_settlementNavigate * 4);
		int endIndex = startIndex + 4;
		funFillSettlementButtons(startIndex, endIndex);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-63", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funNextSettlementMode()
    {
	try
	{
	    _settlementNavigate++;
	    int startIndex = (_settlementNavigate * 4);
	    int endIndex = startIndex + 4;
	    if (_settlementNavigate == 1)
	    {
		disableNext = noOfSettlementMode / startIndex;
	    }
	    funFillSettlementButtons(startIndex, endIndex);
	    btnPrevSettlementMode.setEnabled(true);
	    if (disableNext == _settlementNavigate)
	    {
		btnNextSettlementMode.setEnabled(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-64", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funOpenCustomerMaster()
    {
	new frmCustomerMaster().setVisible(true);
    }

    private void funPostBillDataToInrestoPOS() throws Exception
    {
	JSONObject objJson = new JSONObject();
	JSONArray arrBillItemDtl = new JSONArray();
	JSONArray arrBillItemTaxDtl = new JSONArray();
	double grandTotal = 0.00, subTotal = 0.00, taxAmt = 0.00, disAmt = 0.00;
	String tableName = "", mobileNo = "";

	if (!voucherNo.isEmpty())
	{
	    String sql = "select a.strItemCode,a.strItemName,a.dblRate,sum(a.dblQuantity),sum(a.dblAmount), "
		    + " b.dblSubTotal,b.dblTaxAmt,b.dblDiscountAmt,b.dblGrandTotal, "
		    + " ifnull(c.longMobileNo,''),ifnull(d.strTableName,'')  "
		    + " from tblbilldtl a left outer join tblbillhd b on a.strBillNo=b.strBillNo "
		    + " left outer join tblcustomermaster c on b.strCustomerCode=c.strCustomerCode "
		    + " left outer join tbltablemaster d on b.strTableNo=d.strTableNo "
		    + " where a.strBillNo='" + voucherNo + "' "
		    + " group by a.strItemCode ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		subTotal = rs.getDouble(6);
		taxAmt = rs.getDouble(7);
		disAmt = rs.getDouble(8);
		grandTotal = rs.getDouble(9);
		mobileNo = rs.getString(10);
		tableName = rs.getString(11);
		JSONObject jObjItemDtl = new JSONObject();
		jObjItemDtl.put("code", rs.getString(1));
		jObjItemDtl.put("name", rs.getString(2));
		jObjItemDtl.put("perUnitPrice", rs.getString(3));
		jObjItemDtl.put("quantity", rs.getString(4));
		jObjItemDtl.put("totalAmount", rs.getString(5));
		arrBillItemDtl.add(jObjItemDtl);
	    }
	    rs.close();

	    sql = "select b.strTaxDesc,b.strTaxCode,b.dblPercent,sum(a.dblTaxAmount) "
		    + " from tblbilltaxdtl a,tbltaxhd b "
		    + " where a.strBillNo='" + voucherNo + "' and a.strTaxCode=b.strTaxCode "
		    + " group by a.strTaxCode ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		JSONObject jObjItemTaxDtl = new JSONObject();
		jObjItemTaxDtl.put("name", rs.getString(1));
		jObjItemTaxDtl.put("percent", rs.getString(3));
		jObjItemTaxDtl.put("amount", rs.getString(4));
		arrBillItemTaxDtl.add(jObjItemTaxDtl);
	    }
	    rs.close();
	}

	if (arrBillItemDtl.size() > 0)
	{
	    objJson.put("merchantId", clsGlobalVarClass.gInrestoPOSId);
	    objJson.put("merchantKey", clsGlobalVarClass.gInrestoPOSKey);

	    if (!mobileNo.isEmpty())
	    {
		if (!mobileNo.startsWith("91"))
		{
		    mobileNo = "91" + mobileNo;
		}
	    }
	    objJson.put("customerPhone", mobileNo);
	    objJson.put("roundOff", "0.00");
	    objJson.put("totalAmount", String.valueOf(grandTotal));
	    objJson.put("tableNumber", tableName);
	    objJson.put("discountAmount", String.valueOf(disAmt));
	    objJson.put("preDiscountTotalAmount", String.valueOf(subTotal + taxAmt));
	    objJson.put("items", arrBillItemDtl);
	    objJson.put("taxes", arrBillItemTaxDtl);
	    //String hoURL =clsGlobalVarClass.gInrestoPOSWebServiceURL+"/InrestoPOSIntegration/seatcustomer";
	    String hoURL = clsGlobalVarClass.gInrestoPOSWebServiceURL + "/updatebillfrompos";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    conn.disconnect();

	    System.out.println("Result= " + op);
	}
    }

    private void funCalDotButtonPressed()
    {
	try
	{
	    if (amountBox.equals("PaidAmount"))
	    {
		if (textValue2.contains("."))
		{
		}
		else
		{
		    textValue2 = textValue2 + btnCalDot.getText();
		    txtPaidAmt.setText(textValue2);
		}
	    }
	    else if (amountBox.equals("txtAmount"))
	    {
		if (textValue1.contains("."))
		{
		}
		else
		{
		    textValue1 = textValue1 + btnCalDot.getText();
		    txtAmount.setText(textValue1);
		}
	    }
	    else if (amountBox.equals("discount"))
	    {
		if (discountType.equals("Percent"))
		{
		    if (textValue1.contains("."))
		    {
		    }
		    else
		    {
			textValue1 = textValue1 + btnCalDot.getText();
			txtDiscountPer.setText(textValue1);
		    }
		}
		else
		{
		    if (textValue1.contains("."))
		    {
		    }
		    else
		    {
			textValue1 = textValue1 + btnCalDot.getText();
			txtDiscountAmt.setText(textValue1);
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-46", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funCalBackSpaceButtonPressed()
    {
	try
	{
	    if (amountBox.equals("PaidAmount") && textValue2.length() > 0)
	    {
		StringBuilder sb = new StringBuilder(textValue2);
		sb.delete(textValue2.length() - 1, textValue2.length());
		textValue2 = sb.toString();
		txtPaidAmt.setText(textValue2);
	    }
	    else if (amountBox.equals("txtAmount") && textValue1.length() > 0)
	    {
		StringBuilder sb = new StringBuilder(textValue1);
		sb.delete(textValue1.length() - 1, textValue1.length());
		textValue1 = sb.toString();
		txtAmount.setText(textValue1);
	    }
	    else if (amountBox.equals("discount") && textValue1.length() > 0)
	    {
		if (discountType.equals("Percent"))
		{
		    StringBuilder sb = new StringBuilder(textValue1);
		    sb.delete(textValue1.length() - 1, textValue1.length());
		    textValue1 = sb.toString();
		    txtDiscountPer.setText(textValue1);
		}
		else
		{
		    StringBuilder sb = new StringBuilder(textValue1);
		    sb.delete(textValue1.length() - 1, textValue1.length());
		    textValue1 = sb.toString();
		    txtDiscountAmt.setText(textValue1);
		}
	    }
	    else if (amountBox.equals("CouponAmount") && textValue1.length() > 0)
	    {
		StringBuilder sb = new StringBuilder(textValue1);
		sb.delete(textValue1.length() - 1, textValue1.length());
		textValue1 = sb.toString();
		txtCoupenAmt.setText(textValue1);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    //  e.printStackTrace();
	}
    }

    private void funTextAreaClicked()
    {
	try
	{
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		if (txtAreaRemark.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Remark").setVisible(true);
		    txtAreaRemark.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(this, true, txtAreaRemark.getText(), "1", "Enter Remark").setVisible(true);
		    txtAreaRemark.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	    else
	    {
		String data = JOptionPane.showInputDialog(null, "Enter Remark");
		txtAreaRemark.setText(data);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-59", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funVoucherSeriesTextBoxClicked()
    {
	try
	{
	    objUtility.funCallForSearchForm("GiftVoucherName");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetGiftVoucherData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-58", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funSeriesNoTextBoxClicked()
    {
	try
	{
	    if (txtSeriesNo.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Long", "Enter GiftVoucher Number.").setVisible(true);
		txtSeriesNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
		clsGlobalVarClass.gNumerickeyboardValue = "";
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtSeriesNo.getText(), "Long", "Enter GiftVoucher Number.").setVisible(true);
		txtSeriesNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
		clsGlobalVarClass.gNumerickeyboardValue = "";
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-57", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funApplyDelChargeButtonClicked()
    {
	if (txtDeliveryCharges.getText().trim().length() > 0 && hmSettlemetnOptions.isEmpty())
	{
	    _deliveryCharge = Double.parseDouble(txtDeliveryCharges.getText().trim());
	    funRefreshItemTable();

	    if ("SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType) || "Direct Biller".equalsIgnoreCase(billType))
	    {
		clsSettelementOptions objSettlementList = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
		procSettlementBtnClick(objSettlementList);
	    }
	}
	else
	{
	    txtDeliveryCharges.setText("0.00");
	}
    }

    private void funBankNameTextBoxClicked()
    {
	try
	{
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		if (txtBankName.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(null, true, "1", "Enter Bank Name").setVisible(true);
		    txtBankName.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(null, true, txtBankName.getText(), "1", "Enter Bank Name").setVisible(true);
		    txtBankName.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	    else
	    {
		String data = JOptionPane.showInputDialog(null, "Enter Bank Name");
		txtBankName.setText(data);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-55", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    public String funMakePaymentUsingJioMoneyCode(String billNo, double amount)
    {
	String resultCode = "";
	clsUtility obj = new clsUtility();
	try
	{
	    String RequestType = "1004";
	    String requestData = "requestType=" + RequestType
		    + "&mid=" + clsGlobalVarClass.gJioMoneyMID
		    + "&tid=" + clsGlobalVarClass.gJioMoneyTID
		    + "&imei=111111111111111"
		    + "&scanData=" + txtJioMoneyCode.getText()
		    + "&longitude="
		    + "&latitude="
		    + "&transactionType=sale"
		    + "&amount=" + amount
		    + "&mobileId=1111111111111111111111"
		    + "&merchantName="
		    + "&merchantMobileNumber="
		    + "&tipPerrmission=N"
		    + "&merchantBusinessName="
		    + "&tipPercent=0"
		    + "&version=V2"
		    + "&merchantInstitutionId=BPOS000085"
		    + "&merchantAddress="
		    + "&merchantCity="
		    + "&merchantState="
		    + "&merchantPincode="
		    + "&merchantCategory="
		    + "&invoiceNumber=" + billNo
		    + // bill no
		    "&originatedTransactionId=" + billNo
		    + // bill no
		    "&dealerId="
		    + "&dealerSubId="
		    + "&dealerName=";

	    String Response = "";
	    System.out.println("RequestData : " + requestData);
	    Response = obj.funMakeTransaction(requestData, RequestType, clsGlobalVarClass.gJioMoneyMID, clsGlobalVarClass.gJioMoneyTID, String.valueOf(amount), "PRE_PROD", "localhost", "5150");
	    System.out.println("Server Response: " + Response);

	    String strRes = Response.trim();
	    JSONParser jsonParser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
	    // String responseCode = (String) jsonObject.get("responseCode");
	    JSONArray lang = (JSONArray) jsonObject.get("result");
	    JSONParser jsonParser1 = new JSONParser();
	    JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
	    // String responseCode = (String) jsonObject.get("responseCode");
	    String responseCode = (String) jsonObject1.get("responseCode");
	    resultCode = responseCode;

	    if (null != responseCode)
	    {
		//responseCode ="9999";
		//resultCode=responseCode;
		if (responseCode.equals("0000"))
		{
		    JOptionPane.showMessageDialog(this, "JioMoney Payment Successful.");
		    String BillNo = (String) jsonObject1.get("originatedTransactionId");
		    String refNo = (String) jsonObject1.get("rrefNo");
		    String AuthCode = (String) jsonObject1.get("AuthCode");
		    String txnId = (String) jsonObject1.get("txnId");
		    String transactionDateTime = (String) jsonObject1.get("transactionDateTime");
		    String cardNo = "";
		    String cardType = "";

		    String sql = "update tblbillhd set strJioMoneyRRefNo='" + refNo + "',strJioMoneyAuthCode='" + AuthCode + "',"
			    + " strJioMoneyTxnId='" + txnId + "',strJioMoneyTxnDateTime='" + transactionDateTime + "',"
			    + " strJioMoneyCardNo='" + cardNo + "',strJioMoneyCardType='" + cardType + "'  where strBillNo='" + billNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    if (chkJioNotification.isSelected())
		    {
			if (!(txtJioDestination.getText().toString().isEmpty()))
			{
			    funSendNotification(txtJioDestination.getText(), refNo);
			}
		    }
		}
		else
		{
		    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));

		}

	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    System.out.println("Exception:" + e);
	}
	return resultCode;
    }

    public String funMakePaymentUsingJioMoneyCard(String billNo, double amount)
    {
	String resultCode = "";
	clsUtility obj = new clsUtility();
	try
	{

	    // lblCardMsg.setText("Please Swipe or Insert the Card");
	    String mid = clsGlobalVarClass.gJioMoneyMID;
	    String tid = clsGlobalVarClass.gJioMoneyTID;
	    String RequestType = "1001";
	    String imei = "111111111111111";
	    String longitude = "";
	    String Amount = String.valueOf(amount);
	    String latitude = "";
	    String transactionType = "sale";
	    String mobileId = "1111111111111111111111";
	    String dibUsername = "MPOS/SDK";

	    String requestData = "requestType=" + RequestType
		    + "&imei=" + imei
		    + "&amount=" + Amount
		    + "&longitude=" + longitude
		    + "&latitude=" + latitude
		    + "&transactionType=" + transactionType
		    + "&totalAmount=" + Amount
		    + "&mid=" + mid
		    + "&tid=" + tid
		    + "&dibUsername=" + dibUsername
		    + "&mobileId=" + mobileId
		    + "&version=V2"
		    + "&merchantInstitutionId=BPOS000085"
		    + "&merchantAddress="
		    + "&merchantCity="
		    + "&merchantState="
		    + "&merchantPincode="
		    + "&merchantCategory="
		    + "&invoiceNumber=" + billNo
		    + "&originatedTransactionId=" + billNo + "_card"
		    + "&dealerId="
		    + "&dealerSubId="
		    + "&dealerName=";

	    System.out.println("RequestData : " + requestData);
	    //String Response =  obj.Transaction(requestData, RequestType, mid, tid, Amount,this.Environment,this.IP,this.PORT);
	    String Response = "";
	    Response = obj.funMakeTransaction(requestData, RequestType, mid, tid, Amount, "PRE_PROD", "localhost", "5150");
	    //System.out.println("Server Response: " + response);
	    System.out.println(Response);
	    System.out.println(Response);
	    String strRes = Response.trim();
	    JSONParser jsonParser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
	    // String responseCode = (String) jsonObject.get("responseCode");
	    JSONArray lang = (JSONArray) jsonObject.get("result");
	    JSONParser jsonParser1 = new JSONParser();
	    JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
	    // String responseCode = (String) jsonObject.get("responseCode");
	    String responseCode = (String) jsonObject1.get("responseCode");
	    resultCode = responseCode;

	    if (null != responseCode)
	    {
		if (responseCode.equals("0000"))
		{
		    JOptionPane.showMessageDialog(this, "JioMoney Payment Successful.");
		    String BillNo = (String) jsonObject1.get("originatedTransactionId");
		    String refNo = (String) jsonObject1.get("rrefNo");
		    String AuthCode = (String) jsonObject1.get("AuthCode");
		    String txnId = (String) jsonObject1.get("transid");
		    String transDate = (String) jsonObject1.get("Date");
		    String transTime = (String) jsonObject1.get("Time");
		    String transactionDateTime = transDate + " " + transTime;
		    String cardNo = (String) jsonObject1.get("Card #");
		    String cardType = (String) jsonObject1.get("Card Type");

		    String sql = "update tblbillhd set strJioMoneyRRefNo='" + refNo + "',strJioMoneyAuthCode='" + AuthCode + "',"
			    + " strJioMoneyTxnId='" + txnId + "',strJioMoneyTxnDateTime='" + transactionDateTime + "',"
			    + " strJioMoneyCardNo='" + cardNo + "',strJioMoneyCardType='" + cardType + "'  where strBillNo='" + billNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    funSendNotification(txtJioMoneyCode.getText(), refNo);

		}
		else
		{
		    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));

		}

	    }

	    //System.out.print(obj);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    System.out.println("Exception:" + e);
	}
	return resultCode;
    }

    public void funSendNotification(String destination, String rrno)
    {
	clsUtility obj = new clsUtility();
	try
	{
	    String mid = clsGlobalVarClass.gJioMoneyMID;
	    String tid = clsGlobalVarClass.gJioMoneyTID;
	    String RequestType = "1007";
	    String Amount = "0.00";
	    String imei = "111111111111111";
	    //String destination=txtSendDestination.getText();
	    //  String rrn = txtSendRrNo.getText();

	    // doTransaction obj = new doTransaction();
	    String requestData = "requestType=" + RequestType
		    + "&destination=" + destination
		    + "&imei=" + imei
		    + "&rrn=" + rrno;

	    System.out.println("RequestData : " + requestData);
	    // String Response =  obj.Transaction(requestData, RequestType, mid, tid, Amount,this.Environment,this.IP,this.PORT);
	    String Response = "";
	    Response = obj.funMakeTransaction(requestData, RequestType, mid, tid, Amount, "PRE_PROD", "localhost", "5150");
	    //System.out.println("Server Response: " + response);
	    System.out.println(Response);

	    String strRes = Response.trim();

	    JSONParser jsonParser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
	    // String responseCode = (String) jsonObject.get("responseCode");
	    JSONArray lang = (JSONArray) jsonObject.get("result");
	    JSONParser jsonParser1 = new JSONParser();
	    JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
	    // String responseCode = (String) jsonObject.get("responseCode");
	    String responseCode = (String) jsonObject1.get("responseCode");

	    if (null != responseCode)
	    {
		if (responseCode.equals("0000"))
		{
		    JOptionPane.showMessageDialog(this, "JioMoney Notification Sent Successfully.");
		}
		else
		{
		    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    System.out.println("Exception:" + e);
	}
    }

    public void funCheckTransactionStatus()
    {
	clsUtility obj = new clsUtility();
	obj.funStartSocketBat();
	boolean checkStatus = false;
	try
	{
	    double amount = 0;
	    for (clsSettelementOptions obSettle : hmJioMoneySettleDtl.values())
	    {
		if (obSettle.getStrSettelmentDesc().equals("JM Code"))
		{
		    if (!obSettle.getStrRemark().equals("0000"))
		    {
			checkStatus = true;
			amount = obSettle.getDblSettlementAmt();
		    }

		}
		else
		{
		    if (!obSettle.getStrRemark().equals("0000"))
		    {
			checkStatus = true;
			amount = obSettle.getDblSettlementAmt();
		    }
		}
	    }
	    if (checkStatus)
	    {
		String RequestType = "1003";
		String Amount = String.valueOf(amount);
		String requestData = "requestType=" + RequestType
			+ "&mid=" + clsGlobalVarClass.gJioMoneyMID
			+ "&tid=" + clsGlobalVarClass.gJioMoneyTID
			+ "&version=V2"
			+ "&originatedTransactionId=" + voucherNo;

		System.out.println("RequestData : " + requestData);
		String Response = "";
		Response = obj.funMakeTransaction(requestData, RequestType, clsGlobalVarClass.gJioMoneyMID, clsGlobalVarClass.gJioMoneyTID, Amount, "PRE_PROD", "localhost", "5150");
		System.out.println(Response);

		String strRes = Response.trim();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
		JSONArray lang = (JSONArray) jsonObject.get("result");
		JSONParser jsonParser1 = new JSONParser();
		JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
		String responseCode = (String) jsonObject1.get("messageCode");
		JSONObject jsonObject2 = (JSONObject) jsonParser1.parse(lang.get(2).toString());
		String refNo = "";

		if (null != responseCode)
		{
		    switch (responseCode)
		    {
			case "RPSL_NAVI_BUS_NOT_FOUND_001":
			    JOptionPane.showMessageDialog(this, "Records not found");
			    break;
			case "RPSL_NAVI_BUS_RETURNED_ALL_001":
			    refNo = (String) jsonObject2.get("rrefNo");
			    if (null != refNo)
			    {
				String sql = "update tblbillhd set strJioMoneyRRefNo='" + refNo + "' where strBillNo='" + voucherNo + "'";
				clsGlobalVarClass.dbMysql.execute(sql);
				JOptionPane.showMessageDialog(this, "Payment Successful");
				objPannelShowBills.setVisible(true);
				panelSettlement.setVisible(false);
			    }

			    break;
			case "RPSL_NAVI_BUS_SUCCESS_001":
			    refNo = (String) jsonObject1.get("rrefNo");
			    if (null != refNo)
			    {
				String sql = "update tblbillhd set strJioMoneyRRefNo='" + refNo + "' where strBillNo='" + voucherNo + "'";
				clsGlobalVarClass.dbMysql.execute(sql);
				JOptionPane.showMessageDialog(this, "Payment Successful");
				objPannelShowBills.setVisible(true);
				panelSettlement.setVisible(false);
			    }

			    break;
			case "RPSL_NAVI_TCH_APP_ERR_001":
			    JOptionPane.showMessageDialog(this, "Technical Error");
			    break;

			case "RPSL_NAVI_BUS_AUTH_ERR_001":
			    JOptionPane.showMessageDialog(this, "Authorisation error");
			    break;

			case "0200":
			    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
			    break;
			case "9999":
			    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
			    break;
			default:
			    JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
			    break;
		    }
		}

	    }

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    System.out.println("Exception:" + e);
	}
    }

    private void funComplimentaryItemsClicked()
    {
	try
	{

	    boolean isUserGranted = false;
	    if (clsGlobalVarClass.gSuperUser)
	    {
		isUserGranted = true;
	    }
	    else
	    {
		String formName = "Complimentary Items";
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
		funCallComplimentaryItemsForm();
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

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
        panelLayout = new javax.swing.JPanel();
        panelSettlement = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        btnSettlement1 = new javax.swing.JButton();
        btnSettle = new javax.swing.JButton();
        btnSettlement2 = new javax.swing.JButton();
        btnSettlement4 = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        panelNumericPad = new javax.swing.JPanel();
        btnCal7 = new javax.swing.JButton();
        btnCal8 = new javax.swing.JButton();
        btnCal9 = new javax.swing.JButton();
        btnCalClear = new javax.swing.JButton();
        btnDny1 = new javax.swing.JButton();
        btnCal4 = new javax.swing.JButton();
        btnCal5 = new javax.swing.JButton();
        btnCal6 = new javax.swing.JButton();
        btnCal0 = new javax.swing.JButton();
        btnDny2 = new javax.swing.JButton();
        btnDny3 = new javax.swing.JButton();
        btnDny4 = new javax.swing.JButton();
        btnCalEnter = new javax.swing.JButton();
        btnCal00 = new javax.swing.JButton();
        btnCal3 = new javax.swing.JButton();
        btnCalBackSpace = new javax.swing.JButton();
        btnCalDot = new javax.swing.JButton();
        btnCal1 = new javax.swing.JButton();
        btnCal2 = new javax.swing.JButton();
        panelMode = new javax.swing.JPanel();
        lblPaymentModeLabel = new javax.swing.JLabel();
        lblPaymentMode = new javax.swing.JLabel();
        panelAmt = new javax.swing.JPanel();
        lblAmount = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        lblTip = new javax.swing.JLabel();
        txtPaidAmt = new javax.swing.JTextField();
        lblBalance = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();
        lblcard = new javax.swing.JLabel();
        lblCardBalance = new javax.swing.JLabel();
        PanelCard = new javax.swing.JPanel();
        txtCardName = new javax.swing.JTextField();
        lblSlipNo = new javax.swing.JLabel();
        lblExpiryDate = new javax.swing.JLabel();
        dteExpiry = new com.toedter.calendar.JDateChooser();
        PanelCoupen = new javax.swing.JPanel();
        lblAmountLabel = new javax.swing.JLabel();
        txtCoupenAmt = new javax.swing.JTextField();
        lblRemarkLabel = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        PanelCheque = new javax.swing.JPanel();
        lblChequeNo = new javax.swing.JLabel();
        txtChequeNo = new javax.swing.JTextField();
        txtBankName = new javax.swing.JTextField();
        lblChqDate = new javax.swing.JLabel();
        dteCheque = new com.toedter.calendar.JDateChooser();
        lblBankName = new javax.swing.JLabel();
        lblManualBillNo = new javax.swing.JLabel();
        txtManualBillNo = new javax.swing.JTextField();
        lblRefund = new javax.swing.JLabel();
        PanelGiftVoucher = new javax.swing.JPanel();
        lblGVouchName = new javax.swing.JLabel();
        lblGVSeriesNo = new javax.swing.JLabel();
        txtSeriesNo = new javax.swing.JTextField();
        btnGiftVoucherOK = new javax.swing.JButton();
        txtVoucherSeries = new javax.swing.JTextField();
        PanelRemaks = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaRemark = new javax.swing.JTextArea();
        lblRemark = new javax.swing.JLabel();
        panelCustomer = new javax.swing.JPanel();
        lblCustName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblTipAmount = new javax.swing.JLabel();
        txtTip = new javax.swing.JTextField();
        btnGetOffer = new javax.swing.JButton();
        scrSettle = new javax.swing.JScrollPane();
        tblSettlement = new javax.swing.JTable();
        scrTax = new javax.swing.JScrollPane();
        tblTaxTable = new javax.swing.JTable();
        panelDiscount = new javax.swing.JPanel();
        cmbItemCategory = new javax.swing.JComboBox();
        btnDiscOk = new javax.swing.JButton();
        rdbSubGroupWise = new javax.swing.JRadioButton();
        rdbGroupWise = new javax.swing.JRadioButton();
        rdbAll = new javax.swing.JRadioButton();
        lblDiscAmt = new javax.swing.JLabel();
        txtDiscountAmt = new javax.swing.JTextField();
        lblDisc = new javax.swing.JLabel();
        txtDiscountPer = new javax.swing.JTextField();
        rdbItemWise = new javax.swing.JRadioButton();
        chkDiscFromMaster = new javax.swing.JCheckBox();
        lblDeliveryCharges = new javax.swing.JLabel();
        txtDeliveryCharges = new javax.swing.JTextField();
        lblDelBoyName = new javax.swing.JLabel();
        btnApplyDeliveryCharge = new javax.swing.JButton();
        btnNextSettlementMode = new javax.swing.JButton();
        btnPrevSettlementMode = new javax.swing.JButton();
        btnSettlement3 = new javax.swing.JButton();
        lblCreditCustCode = new javax.swing.JLabel();
        panelRoomSettlement = new javax.swing.JPanel();
        lblFolioNo = new javax.swing.JLabel();
        txtFolioNo = new javax.swing.JTextField();
        lblGuestName = new javax.swing.JLabel();
        txtGuestName = new javax.swing.JTextField();
        lblRoomNo = new javax.swing.JLabel();
        txtRoomNo = new javax.swing.JTextField();
        lblGuestCode = new javax.swing.JLabel();
        txtGuestCode = new javax.swing.JTextField();
        PanelJioMoneySettlement = new javax.swing.JPanel();
        lblJioMoneyCode = new javax.swing.JLabel();
        txtJioMoneyCode = new javax.swing.JTextField();
        chkJioNotification = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        lblJioDestination = new javax.swing.JLabel();
        txtJioDestination = new javax.swing.JTextField();
        btnJioMoneyCheckStatus = new javax.swing.JButton();
        btnShowCompliItems = new javax.swing.JButton();
        btnReprint = new javax.swing.JButton();
        PanelBenowSettlement = new javax.swing.JPanel();
        lblBenowQR = new javax.swing.JLabel();
        lblBenowQRCode = new javax.swing.JLabel();
        btnRemoveSCTax = new javax.swing.JButton();
        OrderPanel = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        lblVNoLabel = new javax.swing.JLabel();
        lblVoucherNo = new javax.swing.JLabel();
        labelTableNo = new javax.swing.JLabel();
        lblTableNo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();

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
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
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
        lblformName.setText("- Bill Settlement");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelSettlement.setBackground(new java.awt.Color(255, 255, 255));
        panelSettlement.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        panelSettlement.setForeground(new java.awt.Color(255, 235, 174));
        panelSettlement.setLayout(null);

        btnSettlement1.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSettlement1.setForeground(new java.awt.Color(255, 255, 255));
        btnSettlement1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettlement1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettlement1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettlement1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettlement1ActionPerformed(evt);
            }
        });
        panelSettlement.add(btnSettlement1);
        btnSettlement1.setBounds(70, 10, 80, 40);

        btnSettle.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSettle.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle.setText("SETTLE");
        btnSettle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettleActionPerformed(evt);
            }
        });
        btnSettle.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSettleKeyPressed(evt);
            }
        });
        panelSettlement.add(btnSettle);
        btnSettle.setBounds(90, 550, 80, 40);

        btnSettlement2.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSettlement2.setForeground(new java.awt.Color(255, 255, 255));
        btnSettlement2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettlement2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettlement2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettlement2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettlement2ActionPerformed(evt);
            }
        });
        panelSettlement.add(btnSettlement2);
        btnSettlement2.setBounds(160, 10, 80, 40);

        btnSettlement4.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSettlement4.setForeground(new java.awt.Color(255, 255, 255));
        btnSettlement4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettlement4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettlement4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettlement4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettlement4ActionPerformed(evt);
            }
        });
        panelSettlement.add(btnSettlement4);
        btnSettlement4.setBounds(340, 10, 80, 40);

        btnPrint.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
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
        panelSettlement.add(btnPrint);
        btnPrint.setBounds(90, 500, 80, 40);

        btnBack.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBack.setText("BACK");
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBackActionPerformed(evt);
            }
        });
        panelSettlement.add(btnBack);
        btnBack.setBounds(10, 500, 70, 40);

        panelNumericPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericPad.setMinimumSize(new java.awt.Dimension(340, 260));
        panelNumericPad.setOpaque(false);

        btnCal7.setBackground(new java.awt.Color(204, 204, 204));
        btnCal7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal7.setText("7");
        btnCal7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal7MouseClicked(evt);
            }
        });

        btnCal8.setBackground(new java.awt.Color(204, 204, 204));
        btnCal8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal8.setText("8");
        btnCal8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal8MouseClicked(evt);
            }
        });

        btnCal9.setBackground(new java.awt.Color(204, 204, 204));
        btnCal9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
            }
        });

        btnCalClear.setBackground(new java.awt.Color(204, 204, 204));
        btnCalClear.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCalClear.setText("C");
        btnCalClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalClearMouseClicked(evt);
            }
        });

        btnDny1.setBackground(new java.awt.Color(204, 204, 204));
        btnDny1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnDny1.setText("10");
        btnDny1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny1ActionPerformed(evt);
            }
        });

        btnCal4.setBackground(new java.awt.Color(204, 204, 204));
        btnCal4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal4.setText("4");
        btnCal4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal4MouseClicked(evt);
            }
        });

        btnCal5.setBackground(new java.awt.Color(204, 204, 204));
        btnCal5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal5.setText("5");
        btnCal5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal5MouseClicked(evt);
            }
        });

        btnCal6.setBackground(new java.awt.Color(204, 204, 204));
        btnCal6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal6.setText("6");
        btnCal6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal6MouseClicked(evt);
            }
        });

        btnCal0.setBackground(new java.awt.Color(204, 204, 204));
        btnCal0.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal0.setText("0");
        btnCal0.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal0MouseClicked(evt);
            }
        });

        btnDny2.setBackground(new java.awt.Color(204, 204, 204));
        btnDny2.setText("20");
        btnDny2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny2ActionPerformed(evt);
            }
        });

        btnDny3.setBackground(new java.awt.Color(204, 204, 204));
        btnDny3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnDny3.setText("100");
        btnDny3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny3ActionPerformed(evt);
            }
        });

        btnDny4.setBackground(new java.awt.Color(204, 204, 204));
        btnDny4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnDny4.setText("500");
        btnDny4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny4ActionPerformed(evt);
            }
        });

        btnCalEnter.setBackground(new java.awt.Color(204, 204, 204));
        btnCalEnter.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCalEnter.setText("Enter");
        btnCalEnter.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalEnterMouseClicked(evt);
            }
        });
        btnCalEnter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalEnterActionPerformed(evt);
            }
        });

        btnCal00.setBackground(new java.awt.Color(204, 204, 204));
        btnCal00.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
            }
        });

        btnCal3.setBackground(new java.awt.Color(204, 204, 204));
        btnCal3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
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

        btnCalBackSpace.setBackground(new java.awt.Color(204, 204, 204));
        btnCalBackSpace.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalBackSpaceActionPerformed(evt);
            }
        });

        btnCalDot.setBackground(new java.awt.Color(204, 204, 204));
        btnCalDot.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCalDot.setText(".");
        btnCalDot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalDotActionPerformed(evt);
            }
        });

        btnCal1.setBackground(new java.awt.Color(204, 204, 204));
        btnCal1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnCal1.setText("1");
        btnCal1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal1MouseClicked(evt);
            }
        });

        btnCal2.setBackground(new java.awt.Color(204, 204, 204));
        btnCal2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
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

        javax.swing.GroupLayout panelNumericPadLayout = new javax.swing.GroupLayout(panelNumericPad);
        panelNumericPad.setLayout(panelNumericPadLayout);
        panelNumericPadLayout.setHorizontalGroup(
            panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericPadLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNumericPadLayout.createSequentialGroup()
                        .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDny3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNumericPadLayout.createSequentialGroup()
                        .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelNumericPadLayout.createSequentialGroup()
                                .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelNumericPadLayout.createSequentialGroup()
                                .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDny1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDny2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelNumericPadLayout.createSequentialGroup()
                        .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDny4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        panelNumericPadLayout.setVerticalGroup(
            panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumericPadLayout.createSequentialGroup()
                .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCalClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDny1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCal0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDny2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCal1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCal00, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDny3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCalDot, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelNumericPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCalBackSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCalEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDny4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        panelSettlement.add(panelNumericPad);
        panelNumericPad.setBounds(200, 280, 290, 173);

        panelMode.setMinimumSize(new java.awt.Dimension(250, 150));

        lblPaymentModeLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblPaymentModeLabel.setText("Settlement");

        lblPaymentMode.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblPaymentMode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout panelModeLayout = new javax.swing.GroupLayout(panelMode);
        panelMode.setLayout(panelModeLayout);
        panelModeLayout.setHorizontalGroup(
            panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblPaymentModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        panelModeLayout.setVerticalGroup(
            panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPaymentModeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPaymentMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        panelSettlement.add(panelMode);
        panelMode.setBounds(0, 60, 190, 30);

        panelAmt.setPreferredSize(new java.awt.Dimension(200, 80));

        lblAmount.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblAmount.setText("Bill Amount");

        txtAmount.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAmount.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtAmountActionPerformed(evt);
            }
        });

        lblTip.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblTip.setText("Paid Amount");

        txtPaidAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtPaidAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaidAmt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPaidAmt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPaidAmtMouseClicked(evt);
            }
        });
        txtPaidAmt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPaidAmtKeyPressed(evt);
            }
        });

        lblBalance.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblBalance.setText("Balance");

        txtBalance.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtBalance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBalance.setFocusable(false);
        txtBalance.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBalanceMouseClicked(evt);
            }
        });

        lblcard.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblcard.setText("Card Balance");

        lblCardBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardBalance.setOpaque(true);

        javax.swing.GroupLayout panelAmtLayout = new javax.swing.GroupLayout(panelAmt);
        panelAmt.setLayout(panelAmtLayout);
        panelAmtLayout.setHorizontalGroup(
            panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAmtLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblTip, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBalance, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblcard, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCardBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(txtPaidAmt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBalance, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        panelAmtLayout.setVerticalGroup(
            panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAmtLayout.createSequentialGroup()
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAmtLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(6, 6, 6)
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPaidAmt, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(lblTip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAmtLayout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(lblcard, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCardBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelSettlement.add(panelAmt);
        panelAmt.setBounds(0, 90, 190, 120);

        PanelCard.setPreferredSize(new java.awt.Dimension(240, 60));

        txtCardName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtCardName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardNameMouseClicked(evt);
            }
        });

        lblSlipNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblSlipNo.setText("Slip No.");

        lblExpiryDate.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblExpiryDate.setText("Expiry Date");

        dteExpiry.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        dteExpiry.setPreferredSize(new java.awt.Dimension(100, 25));

        javax.swing.GroupLayout PanelCardLayout = new javax.swing.GroupLayout(PanelCard);
        PanelCard.setLayout(PanelCardLayout);
        PanelCardLayout.setHorizontalGroup(
            PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCardLayout.createSequentialGroup()
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSlipNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblExpiryDate, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dteExpiry, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(txtCardName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCardLayout.setVerticalGroup(
            PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCardLayout.createSequentialGroup()
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSlipNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCardLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(lblExpiryDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dteExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelSettlement.add(PanelCard);
        PanelCard.setBounds(0, 360, 190, 60);

        lblAmountLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblAmountLabel.setText("Amount");

        txtCoupenAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtCoupenAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblRemarkLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblRemarkLabel.setText("Remark");

        txtRemark.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtRemark.setPreferredSize(new java.awt.Dimension(129, 25));
        txtRemark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRemarkMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PanelCoupenLayout = new javax.swing.GroupLayout(PanelCoupen);
        PanelCoupen.setLayout(PanelCoupenLayout);
        PanelCoupenLayout.setHorizontalGroup(
            PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCoupenLayout.createSequentialGroup()
                .addGroup(PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCoupenLayout.createSequentialGroup()
                        .addComponent(lblRemarkLabel)
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addComponent(lblAmountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRemark, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(txtCoupenAmt)))
        );
        PanelCoupenLayout.setVerticalGroup(
            PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCoupenLayout.createSequentialGroup()
                .addGroup(PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCoupenAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(PanelCoupenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRemarkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelSettlement.add(PanelCoupen);
        PanelCoupen.setBounds(0, 300, 190, 60);

        lblChequeNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblChequeNo.setText("Cheque No.   ");

        txtChequeNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtChequeNo.setMinimumSize(new java.awt.Dimension(110, 25));
        txtChequeNo.setPreferredSize(new java.awt.Dimension(120, 25));
        txtChequeNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtChequeNoMouseClicked(evt);
            }
        });

        txtBankName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtBankName.setMinimumSize(new java.awt.Dimension(110, 25));
        txtBankName.setPreferredSize(new java.awt.Dimension(120, 25));
        txtBankName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBankNameMouseClicked(evt);
            }
        });
        txtBankName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtBankNameActionPerformed(evt);
            }
        });

        lblChqDate.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblChqDate.setText("Date");
        lblChqDate.setMaximumSize(new java.awt.Dimension(61, 25));
        lblChqDate.setMinimumSize(new java.awt.Dimension(61, 25));
        lblChqDate.setPreferredSize(new java.awt.Dimension(61, 25));

        dteCheque.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        dteCheque.setMinimumSize(new java.awt.Dimension(110, 25));
        dteCheque.setPreferredSize(new java.awt.Dimension(120, 25));

        lblBankName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblBankName.setText("Bank Name");

        javax.swing.GroupLayout PanelChequeLayout = new javax.swing.GroupLayout(PanelCheque);
        PanelCheque.setLayout(PanelChequeLayout);
        PanelChequeLayout.setHorizontalGroup(
            PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelChequeLayout.createSequentialGroup()
                .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelChequeLayout.createSequentialGroup()
                        .addComponent(lblChqDate, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dteCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PanelChequeLayout.createSequentialGroup()
                        .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChequeNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtChequeNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(32, 32, 32))
        );
        PanelChequeLayout.setVerticalGroup(
            PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelChequeLayout.createSequentialGroup()
                .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelChequeLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblChqDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        panelSettlement.add(PanelCheque);
        PanelCheque.setBounds(0, 210, 190, 90);

        lblManualBillNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblManualBillNo.setText("Manual Bill No.");
        panelSettlement.add(lblManualBillNo);
        lblManualBillNo.setBounds(190, 210, 90, 30);

        txtManualBillNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        panelSettlement.add(txtManualBillNo);
        txtManualBillNo.setBounds(280, 210, 90, 30);

        lblRefund.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        lblRefund.setForeground(new java.awt.Color(0, 153, 255));
        panelSettlement.add(lblRefund);
        lblRefund.setBounds(320, 450, 160, 20);

        lblGVouchName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblGVouchName.setText("Voucher Name ");

        lblGVSeriesNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblGVSeriesNo.setText("Series No.");

        txtSeriesNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtSeriesNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSeriesNoMouseClicked(evt);
            }
        });

        btnGiftVoucherOK.setText("OK");
        btnGiftVoucherOK.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnGiftVoucherOKMouseClicked(evt);
            }
        });

        txtVoucherSeries.setEditable(false);
        txtVoucherSeries.setBackground(new java.awt.Color(204, 204, 204));
        txtVoucherSeries.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtVoucherSeries.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtVoucherSeriesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PanelGiftVoucherLayout = new javax.swing.GroupLayout(PanelGiftVoucher);
        PanelGiftVoucher.setLayout(PanelGiftVoucherLayout);
        PanelGiftVoucherLayout.setHorizontalGroup(
            PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGiftVoucherLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(btnGiftVoucherOK, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(PanelGiftVoucherLayout.createSequentialGroup()
                .addGroup(PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGVouchName)
                    .addComponent(lblGVSeriesNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtVoucherSeries, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(txtSeriesNo)))
        );
        PanelGiftVoucherLayout.setVerticalGroup(
            PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGiftVoucherLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGVouchName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVoucherSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGiftVoucherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGVSeriesNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSeriesNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(btnGiftVoucherOK)
                .addContainerGap())
        );

        panelSettlement.add(PanelGiftVoucher);
        PanelGiftVoucher.setBounds(0, 420, 190, 100);

        txtAreaRemark.setColumns(20);
        txtAreaRemark.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtAreaRemark.setLineWrap(true);
        txtAreaRemark.setRows(5);
        txtAreaRemark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtAreaRemarkMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(txtAreaRemark);

        lblRemark.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblRemark.setText("Remark");

        javax.swing.GroupLayout PanelRemaksLayout = new javax.swing.GroupLayout(PanelRemaks);
        PanelRemaks.setLayout(PanelRemaksLayout);
        PanelRemaksLayout.setHorizontalGroup(
            PanelRemaksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRemaksLayout.createSequentialGroup()
                .addComponent(lblRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRemaksLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        PanelRemaksLayout.setVerticalGroup(
            PanelRemaksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRemaksLayout.createSequentialGroup()
                .addComponent(lblRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelSettlement.add(PanelRemaks);
        PanelRemaks.setBounds(300, 450, 190, 110);

        lblCustName.setFont(new java.awt.Font("Trebuchet MS", 0, 10)); // NOI18N
        lblCustName.setText("Cust Name:");

        txtCustomerName.setFont(new java.awt.Font("Trebuchet MS", 0, 10)); // NOI18N
        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelCustomerLayout = new javax.swing.GroupLayout(panelCustomer);
        panelCustomer.setLayout(panelCustomerLayout);
        panelCustomerLayout.setHorizontalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addComponent(lblCustName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCustomerLayout.setVerticalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCustName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelSettlement.add(panelCustomer);
        panelCustomer.setBounds(190, 50, 300, 20);

        lblTipAmount.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblTipAmount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTipAmount.setText("Tip :");
        lblTipAmount.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        panelSettlement.add(lblTipAmount);
        lblTipAmount.setBounds(370, 210, 30, 30);

        txtTip.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtTip.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTip.setText("0.00");
        txtTip.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtTipMouseDragged(evt);
            }
        });
        txtTip.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTipMouseClicked(evt);
            }
        });
        panelSettlement.add(txtTip);
        txtTip.setBounds(410, 210, 70, 30);

        btnGetOffer.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnGetOffer.setForeground(new java.awt.Color(255, 255, 255));
        btnGetOffer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGetOffer.setText("OFFER");
        btnGetOffer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGetOffer.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGetOffer.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnGetOfferMouseClicked(evt);
            }
        });
        btnGetOffer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGetOfferActionPerformed(evt);
            }
        });
        panelSettlement.add(btnGetOffer);
        btnGetOffer.setBounds(180, 550, 70, 40);

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

        panelSettlement.add(scrSettle);
        scrSettle.setBounds(210, 450, 290, 90);

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

        panelSettlement.add(scrTax);
        scrTax.setBounds(400, 530, 90, 30);

        panelDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Discount", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Adobe Devanagari", 1, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        panelDiscount.setToolTipText("");
        panelDiscount.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        panelDiscount.setOpaque(false);
        panelDiscount.setLayout(null);

        cmbItemCategory.setBackground(new java.awt.Color(51, 102, 255));
        cmbItemCategory.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        panelDiscount.add(cmbItemCategory);
        cmbItemCategory.setBounds(0, 100, 170, 30);

        btnDiscOk.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnDiscOk.setForeground(new java.awt.Color(255, 255, 255));
        btnDiscOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnDiscOk.setText("OK");
        btnDiscOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDiscOk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnDiscOk.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDiscOkMouseClicked(evt);
            }
        });
        panelDiscount.add(btnDiscOk);
        btnDiscOk.setBounds(180, 100, 70, 30);

        rdbSubGroupWise.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        rdbSubGroupWise.setText("SubGroup");
        rdbSubGroupWise.setOpaque(false);
        rdbSubGroupWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbSubGroupWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbSubGroupWise);
        rdbSubGroupWise.setBounds(120, 70, 80, 23);

        rdbGroupWise.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        rdbGroupWise.setText("Group");
        rdbGroupWise.setOpaque(false);
        rdbGroupWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbGroupWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbGroupWise);
        rdbGroupWise.setBounds(60, 70, 60, 23);

        rdbAll.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        rdbAll.setText("Total");
        rdbAll.setOpaque(false);
        rdbAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbAllActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbAll);
        rdbAll.setBounds(0, 70, 60, 23);

        lblDiscAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblDiscAmt.setText("Discount Amt.");
        panelDiscount.add(lblDiscAmt);
        lblDiscAmt.setBounds(130, 40, 70, 30);

        txtDiscountAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtDiscountAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountAmt.setText("0.00");
        txtDiscountAmt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtDiscountAmtMouseDragged(evt);
            }
        });
        txtDiscountAmt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountAmtMouseClicked(evt);
            }
        });
        txtDiscountAmt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountAmtKeyPressed(evt);
            }
        });
        panelDiscount.add(txtDiscountAmt);
        txtDiscountAmt.setBounds(200, 40, 50, 25);

        lblDisc.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblDisc.setText("Discount %");
        panelDiscount.add(lblDisc);
        lblDisc.setBounds(130, 10, 70, 30);

        txtDiscountPer.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtDiscountPer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountPer.setText("0");
        txtDiscountPer.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtDiscountPerMouseDragged(evt);
            }
        });
        txtDiscountPer.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDiscountPerMouseClicked(evt);
            }
        });
        txtDiscountPer.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDiscountPerKeyPressed(evt);
            }
        });
        panelDiscount.add(txtDiscountPer);
        txtDiscountPer.setBounds(200, 10, 50, 25);

        rdbItemWise.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        rdbItemWise.setText("Item");
        rdbItemWise.setOpaque(false);
        rdbItemWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbItemWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbItemWise);
        rdbItemWise.setBounds(200, 70, 50, 23);

        chkDiscFromMaster.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        chkDiscFromMaster.setText("<html>Discount<br>Master</html>");
        chkDiscFromMaster.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkDiscFromMaster.setOpaque(false);
        chkDiscFromMaster.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                chkDiscFromMasterMouseClicked(evt);
            }
        });
        panelDiscount.add(chkDiscFromMaster);
        chkDiscFromMaster.setBounds(10, 30, 90, 30);

        panelSettlement.add(panelDiscount);
        panelDiscount.setBounds(230, 70, 260, 140);

        lblDeliveryCharges.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblDeliveryCharges.setText("Del Charges:");
        panelSettlement.add(lblDeliveryCharges);
        lblDeliveryCharges.setBounds(200, 250, 80, 30);

        txtDeliveryCharges.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtDeliveryCharges.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDeliveryCharges.setText("0.00");
        txtDeliveryCharges.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtDeliveryChargesFocusLost(evt);
            }
        });
        txtDeliveryCharges.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDeliveryChargesMouseClicked(evt);
            }
        });
        panelSettlement.add(txtDeliveryCharges);
        txtDeliveryCharges.setBounds(280, 250, 60, 30);
        panelSettlement.add(lblDelBoyName);
        lblDelBoyName.setBounds(400, 250, 90, 30);

        btnApplyDeliveryCharge.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        btnApplyDeliveryCharge.setText("OK");
        btnApplyDeliveryCharge.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnApplyDeliveryChargeMouseClicked(evt);
            }
        });
        panelSettlement.add(btnApplyDeliveryCharge);
        btnApplyDeliveryCharge.setBounds(343, 250, 50, 30);

        btnNextSettlementMode.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
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
        panelSettlement.add(btnNextSettlementMode);
        btnNextSettlementMode.setBounds(430, 10, 60, 40);

        btnPrevSettlementMode.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
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
        panelSettlement.add(btnPrevSettlementMode);
        btnPrevSettlementMode.setBounds(0, 10, 60, 40);

        btnSettlement3.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSettlement3.setForeground(new java.awt.Color(255, 255, 255));
        btnSettlement3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettlement3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettlement3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettlement3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettlement3ActionPerformed(evt);
            }
        });
        panelSettlement.add(btnSettlement3);
        btnSettlement3.setBounds(250, 10, 80, 40);
        panelSettlement.add(lblCreditCustCode);
        lblCreditCustCode.setBounds(190, 90, 60, 20);

        panelRoomSettlement.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Room Settlement", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Adobe Devanagari", 1, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        panelRoomSettlement.setToolTipText("");
        panelRoomSettlement.setMaximumSize(new java.awt.Dimension(231, 145));
        panelRoomSettlement.setMinimumSize(new java.awt.Dimension(231, 145));
        panelRoomSettlement.setOpaque(false);
        panelRoomSettlement.setPreferredSize(new java.awt.Dimension(231, 145));
        panelRoomSettlement.setLayout(null);

        lblFolioNo.setText("Folio No.");
        panelRoomSettlement.add(lblFolioNo);
        lblFolioNo.setBounds(10, 50, 70, 25);

        txtFolioNo.setEditable(false);
        txtFolioNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFolioNo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtFolioNoMouseDragged(evt);
            }
        });
        txtFolioNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFolioNoMouseClicked(evt);
            }
        });
        txtFolioNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtFolioNoKeyPressed(evt);
            }
        });
        panelRoomSettlement.add(txtFolioNo);
        txtFolioNo.setBounds(80, 50, 140, 25);

        lblGuestName.setText("Guest Name");
        panelRoomSettlement.add(lblGuestName);
        lblGuestName.setBounds(10, 20, 70, 20);

        txtGuestName.setEditable(false);
        txtGuestName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGuestName.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtGuestNameMouseDragged(evt);
            }
        });
        txtGuestName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGuestNameMouseClicked(evt);
            }
        });
        txtGuestName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGuestNameKeyPressed(evt);
            }
        });
        panelRoomSettlement.add(txtGuestName);
        txtGuestName.setBounds(80, 20, 140, 25);

        lblRoomNo.setText("Room No.");
        panelRoomSettlement.add(lblRoomNo);
        lblRoomNo.setBounds(10, 80, 70, 25);

        txtRoomNo.setEditable(false);
        txtRoomNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRoomNo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtRoomNoMouseDragged(evt);
            }
        });
        txtRoomNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRoomNoMouseClicked(evt);
            }
        });
        txtRoomNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtRoomNoKeyPressed(evt);
            }
        });
        panelRoomSettlement.add(txtRoomNo);
        txtRoomNo.setBounds(80, 80, 140, 25);

        lblGuestCode.setText("Guest Code");
        panelRoomSettlement.add(lblGuestCode);
        lblGuestCode.setBounds(10, 110, 70, 25);

        txtGuestCode.setEditable(false);
        txtGuestCode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGuestCode.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                txtGuestCodeMouseDragged(evt);
            }
        });
        txtGuestCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGuestCodeMouseClicked(evt);
            }
        });
        txtGuestCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtGuestCodeKeyPressed(evt);
            }
        });
        panelRoomSettlement.add(txtGuestCode);
        txtGuestCode.setBounds(80, 110, 140, 25);

        panelSettlement.add(panelRoomSettlement);
        panelRoomSettlement.setBounds(0, 80, 231, 145);

        lblJioMoneyCode.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblJioMoneyCode.setText("Scan/Enter Code ");

        txtJioMoneyCode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtJioMoneyCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtJioMoneyCodeMouseClicked(evt);
            }
        });

        chkJioNotification.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkJioNotificationActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("Send Notification");

        lblJioDestination.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblJioDestination.setText("SMS/Email:-");

        txtJioDestination.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtJioDestination.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtJioDestinationActionPerformed(evt);
            }
        });

        btnJioMoneyCheckStatus.setText("Check Status");
        btnJioMoneyCheckStatus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnJioMoneyCheckStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelJioMoneySettlementLayout = new javax.swing.GroupLayout(PanelJioMoneySettlement);
        PanelJioMoneySettlement.setLayout(PanelJioMoneySettlementLayout);
        PanelJioMoneySettlementLayout.setHorizontalGroup(
            PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelJioMoneySettlementLayout.createSequentialGroup()
                .addGroup(PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(PanelJioMoneySettlementLayout.createSequentialGroup()
                            .addComponent(lblJioDestination)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtJioDestination))
                        .addGroup(PanelJioMoneySettlementLayout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(chkJioNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(PanelJioMoneySettlementLayout.createSequentialGroup()
                            .addComponent(lblJioMoneyCode)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtJioMoneyCode, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnJioMoneyCheckStatus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelJioMoneySettlementLayout.setVerticalGroup(
            PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelJioMoneySettlementLayout.createSequentialGroup()
                .addGroup(PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblJioMoneyCode, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(txtJioMoneyCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkJioNotification))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelJioMoneySettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtJioDestination)
                    .addComponent(lblJioDestination, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnJioMoneyCheckStatus))
        );

        panelSettlement.add(PanelJioMoneySettlement);
        PanelJioMoneySettlement.setBounds(0, 420, 190, 100);
        PanelJioMoneySettlement.getAccessibleContext().setAccessibleName("JioMoney Code Settlement");
        PanelJioMoneySettlement.getAccessibleContext().setAccessibleDescription("");

        btnShowCompliItems.setBackground(new java.awt.Color(255, 255, 255));
        btnShowCompliItems.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        btnShowCompliItems.setForeground(new java.awt.Color(255, 255, 255));
        btnShowCompliItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSpacebar1.png"))); // NOI18N
        btnShowCompliItems.setText("SHOW ITEMS");
        btnShowCompliItems.setActionCommand("<html>SHOW ITEMS</html>");
        btnShowCompliItems.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowCompliItems.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShowCompliItemsActionPerformed(evt);
            }
        });
        panelSettlement.add(btnShowCompliItems);
        btnShowCompliItems.setBounds(180, 500, 100, 40);

        btnReprint.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnReprint.setForeground(new java.awt.Color(255, 255, 255));
        btnReprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReprint.setText("<html>REPRINT</html>");
        btnReprint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReprint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReprint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReprintActionPerformed(evt);
            }
        });
        btnReprint.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnReprintKeyPressed(evt);
            }
        });
        panelSettlement.add(btnReprint);
        btnReprint.setBounds(10, 550, 70, 40);

        PanelBenowSettlement.setOpaque(false);

        lblBenowQR.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblBenowQR.setText("Scan QR Code");

        javax.swing.GroupLayout PanelBenowSettlementLayout = new javax.swing.GroupLayout(PanelBenowSettlement);
        PanelBenowSettlement.setLayout(PanelBenowSettlementLayout);
        PanelBenowSettlementLayout.setHorizontalGroup(
            PanelBenowSettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBenowSettlementLayout.createSequentialGroup()
                .addGroup(PanelBenowSettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBenowSettlementLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblBenowQRCode, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelBenowSettlementLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblBenowQR, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        PanelBenowSettlementLayout.setVerticalGroup(
            PanelBenowSettlementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBenowSettlementLayout.createSequentialGroup()
                .addComponent(lblBenowQR, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblBenowQRCode, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSettlement.add(PanelBenowSettlement);
        PanelBenowSettlement.setBounds(230, 60, 160, 150);

        btnRemoveSCTax.setVisible(false);
        btnRemoveSCTax.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnRemoveSCTax.setForeground(new java.awt.Color(255, 255, 255));
        btnRemoveSCTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnRemoveSCTax.setText("NSC");
        btnRemoveSCTax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveSCTax.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnRemoveSCTax.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveSCTaxActionPerformed(evt);
            }
        });
        panelSettlement.add(btnRemoveSCTax);
        btnRemoveSCTax.setBounds(260, 550, 70, 40);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 499;
        gridBagConstraints.ipady = 599;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 4, 1);
        panelLayout.add(panelSettlement, gridBagConstraints);

        OrderPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        OrderPanel.setPreferredSize(new java.awt.Dimension(260, 600));
        OrderPanel.setLayout(null);

        lblVNoLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblVNoLabel.setText("Bill No:");
        OrderPanel.add(lblVNoLabel);
        lblVNoLabel.setBounds(160, 0, 40, 30);

        lblVoucherNo.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        lblVoucherNo.setForeground(new java.awt.Color(51, 102, 255));
        OrderPanel.add(lblVoucherNo);
        lblVoucherNo.setBounds(210, 0, 90, 30);

        labelTableNo.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        labelTableNo.setText("Table No:");
        OrderPanel.add(labelTableNo);
        labelTableNo.setBounds(0, 0, 60, 30);

        lblTableNo.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        lblTableNo.setForeground(new java.awt.Color(51, 102, 255));
        OrderPanel.add(lblTableNo);
        lblTableNo.setBounds(60, 0, 100, 30);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        tblItemTable.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Qty", "Amount", "DiscPer", "DiscAmt"
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
        jScrollPane3.setViewportView(tblItemTable);

        OrderPanel.add(jScrollPane3);
        jScrollPane3.setBounds(0, 30, 310, 570);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 309;
        gridBagConstraints.ipady = 599;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 4, 0);
        panelLayout.add(OrderPanel, gridBagConstraints);

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSettlement3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettlement3ActionPerformed

	clsSettelementOptions objSettlementOptions = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement3.getText());
	if (objSettlementOptions.getStrSettelmentType().equals("Complementary"))
	{
	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Complimentry Settlement"))
		{
		    JOptionPane.showMessageDialog(null, "You dont have rights to settle bill in Complimentry Mode!!!");
		    return;
		}
	    }
	}
	procSettlementBtnClick(objSettlementOptions);
    }//GEN-LAST:event_btnSettlement3ActionPerformed

    private void btnPrevSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevSettlementModeActionPerformed

	funPrevSettlementMode();
    }//GEN-LAST:event_btnPrevSettlementModeActionPerformed

    private void btnNextSettlementModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextSettlementModeActionPerformed

	funNextSettlementMode();
    }//GEN-LAST:event_btnNextSettlementModeActionPerformed

    private void btnApplyDeliveryChargeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnApplyDeliveryChargeMouseClicked
	funApplyDelChargeButtonClicked();
    }//GEN-LAST:event_btnApplyDeliveryChargeMouseClicked

    private void txtDeliveryChargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDeliveryChargesMouseClicked

	amountBox = "delcharges";
    }//GEN-LAST:event_txtDeliveryChargesMouseClicked

    private void txtDeliveryChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeliveryChargesFocusLost

    }//GEN-LAST:event_txtDeliveryChargesFocusLost

    private void txtDiscountPerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountPerMouseClicked

	funDiscPercentageMouseClicked();
    }//GEN-LAST:event_txtDiscountPerMouseClicked

    private void txtDiscountAmtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountAmtMouseClicked

	funDiscAmountMouseClicked();

    }//GEN-LAST:event_txtDiscountAmtMouseClicked

    private void rdbAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbAllActionPerformed

	if (!chkDiscFromMaster.isSelected())
	{
	    try
	    {
		cmbItemCategory.removeAllItems();
		cmbItemCategory.setEnabled(false);
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-62", JOptionPane.ERROR_MESSAGE);
		//e.printStackTrace();
	    }
	}

    }//GEN-LAST:event_rdbAllActionPerformed

    private void rdbGroupWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbGroupWiseActionPerformed

	if (!chkDiscFromMaster.isSelected())
	{
	    try
	    {
		if (rdbGroupWise.isSelected())
		{
		    funEnableGroupSubGroupCombo();
		    funFillComboBoxForGroup();
		}
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-61", JOptionPane.ERROR_MESSAGE);
		//e.printStackTrace();
	    }
	}

    }//GEN-LAST:event_rdbGroupWiseActionPerformed

    private void rdbSubGroupWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbSubGroupWiseActionPerformed

	if (!chkDiscFromMaster.isSelected())
	{
	    if (rdbSubGroupWise.isSelected())
	    {
		funEnableGroupSubGroupCombo();
		funFillComboBoxForSubgroup();
	    }
	}

    }//GEN-LAST:event_rdbSubGroupWiseActionPerformed

    private void btnDiscOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiscOkMouseClicked

	if (hmSettlemetnOptions.isEmpty() && !chkDiscFromMaster.isSelected())
	{

	    try
	    {
		boolean isUserGranted = false;
		if (clsGlobalVarClass.gSuperUser)
		{
		    isUserGranted = true;
		}
		else
		{
		    String formName = "Discount On Bill";
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
		    _paidAmount = 0.00;
		    objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
		}

	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		e.printStackTrace();
	    }

	}
    }//GEN-LAST:event_btnDiscOkMouseClicked

    private void btnGetOfferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGetOfferMouseClicked
	// TODO add your handling code here:
	//funCheckOffers();
	try
	{
	    funShowPromoItems();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnGetOfferMouseClicked

    private void txtTipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTipMouseClicked

	amountBox = "tip";
    }//GEN-LAST:event_txtTipMouseClicked

    private void txtAreaRemarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaRemarkMouseClicked
	funTextAreaClicked();
    }//GEN-LAST:event_txtAreaRemarkMouseClicked

    private void txtVoucherSeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVoucherSeriesMouseClicked

	funVoucherSeriesTextBoxClicked();
    }//GEN-LAST:event_txtVoucherSeriesMouseClicked

    private void btnGiftVoucherOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGiftVoucherOKMouseClicked
	funGiftVoucher();
    }//GEN-LAST:event_btnGiftVoucherOKMouseClicked

    private void txtSeriesNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSeriesNoMouseClicked
	funSeriesNoTextBoxClicked();
    }//GEN-LAST:event_txtSeriesNoMouseClicked

    private void txtBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankNameActionPerformed

    }//GEN-LAST:event_txtBankNameActionPerformed

    private void txtBankNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBankNameMouseClicked
	funBankNameTextBoxClicked();
    }//GEN-LAST:event_txtBankNameMouseClicked

    private void txtChequeNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtChequeNoMouseClicked

	try
	{
	    if (txtChequeNo.getText().length() == 0)
	    {
		new frmNumericKeyboard(null, true, "", "Long", "Enter Cheque No").setVisible(true);
		txtChequeNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(null, true, txtChequeNo.getText(), "Long", "Enter Cheque No").setVisible(true);
		txtChequeNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-54", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_txtChequeNoMouseClicked

    private void txtRemarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRemarkMouseClicked

	try
	{
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		if (txtRemark.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(null, true, "1", "Enter Remark").setVisible(true);
		    txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(null, true, txtRemark.getText(), "1", "Enter Remark").setVisible(true);
		    txtRemark.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	    else
	    {
		String data = JOptionPane.showInputDialog(null, "Enter Remark");
		txtRemark.setText(data);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-56", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_txtRemarkMouseClicked

    private void txtCardNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardNameMouseClicked

	try
	{
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		if (txtCardName.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(null, true, "1", "Enter Card Name").setVisible(true);
		    txtCardName.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(null, true, txtCardName.getText(), "1", "Enter Card Name").setVisible(true);
		    txtCardName.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	    else
	    {
		String data = JOptionPane.showInputDialog(null, "Enter Card Name");
		txtCardName.setText(data);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-52", JOptionPane.ERROR_MESSAGE);
	    // e.printStackTrace();
	}
    }//GEN-LAST:event_txtCardNameMouseClicked

    private void txtBalanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBalanceMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceMouseClicked

    private void txtPaidAmtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPaidAmtMouseClicked

	txtPaidAmt.setText("");
	textValue2 = "";
	amountBox = "PaidAmount";
    }//GEN-LAST:event_txtPaidAmtMouseClicked

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void btnCal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal2ActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCal2ActionPerformed

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked

	procNumericValue(btnCal2.getText());
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked

	procNumericValue(btnCal1.getText());
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCalBackSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalBackSpaceActionPerformed
	funCalBackSpaceButtonPressed();
    }//GEN-LAST:event_btnCalBackSpaceActionPerformed

    private void btnCal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal3ActionPerformed

    }//GEN-LAST:event_btnCal3ActionPerformed

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked

	procNumericValue(btnCal3.getText());
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked

	procNumericValue(btnCal00.getText());
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCalEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalEnterMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCalEnterMouseClicked

    private void btnDny4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny4ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny4.getText());
    }//GEN-LAST:event_btnDny4ActionPerformed

    private void btnDny3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny3ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny3.getText());
    }//GEN-LAST:event_btnDny3ActionPerformed

    private void btnDny2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny2ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny2.getText());
    }//GEN-LAST:event_btnDny2ActionPerformed

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal0.getText());
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal6.getText());
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked

	procNumericValue(btnCal5.getText());
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal4.getText());
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnDny1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny1ActionPerformed

	procEnterValue(btnDny1.getText());
    }//GEN-LAST:event_btnDny1ActionPerformed

    private void btnCalClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalClearMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (amountBox.equals("PaidAmount"))
	    {
		textValue2 = "";
		txtPaidAmt.setText(textValue2);
	    }
	    else if (amountBox.equals("txtAmount"))
	    {
		textValue1 = "";
		txtAmount.setText(textValue1);
	    }
	    else if (amountBox.equals("discount"))
	    {
		if (discountType.equals("Percent"))
		{
		    textValue1 = "0";
		    txtDiscountPer.setText(textValue1);
		    txtDiscountAmt.setText(textValue1);
		}
		else
		{
		    textValue1 = "0";
		    txtDiscountAmt.setText(textValue1);
		    txtDiscountPer.setText(textValue1);
		}
	    }
	    else if (amountBox.equals("CouponAmount"))
	    {
		textValue1 = "";
		txtCoupenAmt.setText(textValue1);
	    }
	    else if (amountBox.equals("delcharges"))
	    {
		textValue1 = "";
		txtDeliveryCharges.setText("");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-34", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnCal9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal9MouseClicked

	procNumericValue(btnCal9.getText());
    }//GEN-LAST:event_btnCal9MouseClicked

    private void btnCal8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal8MouseClicked

	procNumericValue(btnCal8.getText());
    }//GEN-LAST:event_btnCal8MouseClicked

    private void btnCal7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal7MouseClicked

	procNumericValue(btnCal7.getText());
    }//GEN-LAST:event_btnCal7MouseClicked

    private void btnSettlement4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettlement4ActionPerformed

	clsSettelementOptions objSettlementOptions = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement4.getText());
	if (objSettlementOptions.getStrSettelmentType().equals("Complementary"))
	{
	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Complimentry Settlement"))
		{
		    JOptionPane.showMessageDialog(null, "You dont have rights to settle bill in Complimentry Mode!!!");
		    return;
		}
	    }
	}
	procSettlementBtnClick(objSettlementOptions);
    }//GEN-LAST:event_btnSettlement4ActionPerformed

    private void btnSettlement2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettlement2ActionPerformed

	clsSettelementOptions objSettlementOptions = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement2.getText());
	if (objSettlementOptions.getStrSettelmentType().equals("Complementary"))
	{
	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Complimentry Settlement"))
		{
		    JOptionPane.showMessageDialog(null, "You dont have rights to settle bill in Complimentry Mode!!!");
		    return;
		}
	    }
	}
	procSettlementBtnClick(objSettlementOptions);
    }//GEN-LAST:event_btnSettlement2ActionPerformed

    private void btnSettlement1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettlement1ActionPerformed
	clsSettelementOptions objSettlementOptions = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
	if (objSettlementOptions.getStrSettelmentType().equals("Complementary"))
	{
	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Complimentry Settlement"))
		{
		    JOptionPane.showMessageDialog(null, "You dont have rights to settle bill in Complimentry Mode!!!");
		    return;
		}
	    }
	}
	procSettlementBtnClick(objSettlementOptions);
    }//GEN-LAST:event_btnSettlement1ActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
	// TODO add your handling code here:
	funPrintButtonPresed();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
	// TODO add your handling code here:
	funBackButtonPressed();
    }//GEN-LAST:event_btnBackActionPerformed

    private void rdbItemWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbItemWiseActionPerformed
	// TODO add your handling code here:
	if (!chkDiscFromMaster.isSelected())
	{
	    objCalculateBillDisc.funFillItemList();
	}
    }//GEN-LAST:event_rdbItemWiseActionPerformed

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerNameMouseClicked
	// TODO add your handling code here:
	funOpenCustomerMaster();
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtDiscountPerKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtDiscountPerKeyPressed
    {//GEN-HEADEREND:event_txtDiscountPerKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (hmSettlemetnOptions.isEmpty())
	    {
		try
		{
		    boolean isUserGranted = false;
		    if (clsGlobalVarClass.gSuperUser)
		    {
			isUserGranted = true;
		    }
		    else
		    {
			String formName = "Discount On Bill";
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
			_paidAmount = 0.00;
			objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
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
    }//GEN-LAST:event_txtDiscountPerKeyPressed

    private void txtDiscountAmtKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtDiscountAmtKeyPressed
    {//GEN-HEADEREND:event_txtDiscountAmtKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (hmSettlemetnOptions.isEmpty())
	    {
		try
		{

		    boolean isUserGranted = false;
		    if (clsGlobalVarClass.gSuperUser)
		    {
			isUserGranted = true;
		    }
		    else
		    {
			String formName = "Discount On Bill";
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
			_paidAmount = 0.00;
			objCalculateBillDisc.funDiscountOKButtonPressed("Manual");
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
    }//GEN-LAST:event_txtDiscountAmtKeyPressed

    private void btnCalEnterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCalEnterActionPerformed
    {//GEN-HEADEREND:event_btnCalEnterActionPerformed
	// TODO add your handling code here:
	if (_balanceAmount != 0.00 || hmSettlemetnOptions.isEmpty())
	{
	    funEnterButtonPressed();
	}
    }//GEN-LAST:event_btnCalEnterActionPerformed

    private void btnSettleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSettleActionPerformed
    {//GEN-HEADEREND:event_btnSettleActionPerformed
	// TODO add your handling code here:
	funSettleButtonPressed();
    }//GEN-LAST:event_btnSettleActionPerformed

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

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void txtDiscountPerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountPerMouseDragged
	textValue1 = "";
	txtDiscountPer.setText("");
	txtDiscountAmt.setText("0.00");
	amountBox = "discount";
	discountType = "Percent";
    }//GEN-LAST:event_txtDiscountPerMouseDragged

    private void txtDiscountAmtMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountAmtMouseDragged
	textValue1 = "";
	txtDiscountAmt.setText("");
	txtDiscountPer.setText("0.00");
	discountType = "Amount";
	amountBox = "discount";
    }//GEN-LAST:event_txtDiscountAmtMouseDragged

    private void txtTipMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTipMouseDragged
    {//GEN-HEADEREND:event_txtTipMouseDragged
	amountBox = "tip";
    }//GEN-LAST:event_txtTipMouseDragged

    private void txtFolioNoMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFolioNoMouseDragged
    {//GEN-HEADEREND:event_txtFolioNoMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoMouseDragged

    private void txtFolioNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFolioNoMouseClicked
    {//GEN-HEADEREND:event_txtFolioNoMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoMouseClicked

    private void txtFolioNoKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtFolioNoKeyPressed
    {//GEN-HEADEREND:event_txtFolioNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoKeyPressed

    private void txtGuestNameMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGuestNameMouseDragged
    {//GEN-HEADEREND:event_txtGuestNameMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameMouseDragged

    private void txtGuestNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGuestNameMouseClicked
    {//GEN-HEADEREND:event_txtGuestNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameMouseClicked

    private void txtGuestNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtGuestNameKeyPressed
    {//GEN-HEADEREND:event_txtGuestNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameKeyPressed

    private void txtRoomNoMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtRoomNoMouseDragged
    {//GEN-HEADEREND:event_txtRoomNoMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoMouseDragged

    private void txtRoomNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtRoomNoMouseClicked
    {//GEN-HEADEREND:event_txtRoomNoMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoMouseClicked

    private void txtRoomNoKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtRoomNoKeyPressed
    {//GEN-HEADEREND:event_txtRoomNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoKeyPressed

    private void txtGuestCodeMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGuestCodeMouseDragged
    {//GEN-HEADEREND:event_txtGuestCodeMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeMouseDragged

    private void txtGuestCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGuestCodeMouseClicked
    {//GEN-HEADEREND:event_txtGuestCodeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeMouseClicked

    private void txtGuestCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtGuestCodeKeyPressed
    {//GEN-HEADEREND:event_txtGuestCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeKeyPressed

    private void txtPaidAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaidAmtKeyPressed
	// TODO add your handling code here:

	if (evt.getKeyCode() == 10)
	{
	    funEnterButtonPressed();
	}
    }//GEN-LAST:event_txtPaidAmtKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("SettleBill");
    }//GEN-LAST:event_formWindowClosed

    private void btnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrintKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funPrintButtonPresed();
	}
    }//GEN-LAST:event_btnPrintKeyPressed

    private void btnSettleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSettleKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funSettleButtonPressed();
	}
    }//GEN-LAST:event_btnSettleKeyPressed

    private void btnCalDotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalDotActionPerformed
	// TODO add your handling code here:
	funCalDotButtonPressed();
    }//GEN-LAST:event_btnCalDotActionPerformed

    private void txtJioMoneyCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtJioMoneyCodeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtJioMoneyCodeMouseClicked

    private void chkJioNotificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkJioNotificationActionPerformed
	// TODO add your handling code here:
	if (settleName.equals("JM Code"))
	{
	    if (chkJioNotification.isSelected())
	    {
		txtJioDestination.setVisible(true);
		lblJioDestination.setVisible(true);
		lblJioDestination.setText("SMS/Email:");
	    }
	    else
	    {
		txtJioDestination.setVisible(false);
		lblJioDestination.setVisible(false);
		lblJioDestination.setText("");
	    }
	}


    }//GEN-LAST:event_chkJioNotificationActionPerformed

    private void txtJioDestinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJioDestinationActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtJioDestinationActionPerformed

    private void btnJioMoneyCheckStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJioMoneyCheckStatusActionPerformed
	// TODO add your handling code here:

	funCheckTransactionStatus();
    }//GEN-LAST:event_btnJioMoneyCheckStatusActionPerformed

    private void btnShowCompliItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCompliItemsActionPerformed

	funComplimentaryItemsClicked();
    }//GEN-LAST:event_btnShowCompliItemsActionPerformed

    private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
	// TODO add your handling code here:
	funReprintButtonClick();
    }//GEN-LAST:event_btnReprintActionPerformed

    private void btnReprintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnReprintKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnReprintKeyPressed

    private void chkDiscFromMasterMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_chkDiscFromMasterMouseClicked
    {//GEN-HEADEREND:event_chkDiscFromMasterMouseClicked

	if (chkDiscFromMaster.isSelected())
	{
	    objCalculateBillDisc.funDiscountFromMasterCheckBoxClicked();
	}
	else
	{
	    chkDiscFromMaster.setSelected(false);
	    rdbAll.setSelected(true);
	}
    }//GEN-LAST:event_chkDiscFromMasterMouseClicked

    private void btnGetOfferActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGetOfferActionPerformed
    {//GEN-HEADEREND:event_btnGetOfferActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnGetOfferActionPerformed

    private void btnRemoveSCTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveSCTaxActionPerformed
	funNSCButttonClicked();
    }//GEN-LAST:event_btnRemoveSCTaxActionPerformed
    /**
     * This method is used to reset fields
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JPanel PanelBenowSettlement;
    private javax.swing.JPanel PanelCard;
    private javax.swing.JPanel PanelCheque;
    private javax.swing.JPanel PanelCoupen;
    private javax.swing.JPanel PanelGiftVoucher;
    private javax.swing.JPanel PanelJioMoneySettlement;
    private javax.swing.JPanel PanelRemaks;
    private javax.swing.JButton btnApplyDeliveryCharge;
    private javax.swing.JButton btnBack;
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
    private javax.swing.JButton btnDiscOk;
    private javax.swing.JButton btnDny1;
    private javax.swing.JButton btnDny2;
    private javax.swing.JButton btnDny3;
    private javax.swing.JButton btnDny4;
    private javax.swing.JButton btnGetOffer;
    private javax.swing.JButton btnGiftVoucherOK;
    private javax.swing.JButton btnJioMoneyCheckStatus;
    private javax.swing.JButton btnNextSettlementMode;
    private javax.swing.JButton btnPrevSettlementMode;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRemoveSCTax;
    private javax.swing.JButton btnReprint;
    private javax.swing.JButton btnSettle;
    private javax.swing.JButton btnSettlement1;
    private javax.swing.JButton btnSettlement2;
    private javax.swing.JButton btnSettlement3;
    private javax.swing.JButton btnSettlement4;
    private javax.swing.JButton btnShowCompliItems;
    private javax.swing.JCheckBox chkDiscFromMaster;
    private javax.swing.JCheckBox chkJioNotification;
    private javax.swing.JComboBox cmbItemCategory;
    private com.toedter.calendar.JDateChooser dteCheque;
    private com.toedter.calendar.JDateChooser dteExpiry;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelTableNo;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblAmountLabel;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblBankName;
    private javax.swing.JLabel lblBenowQR;
    private javax.swing.JLabel lblBenowQRCode;
    private javax.swing.JLabel lblCardBalance;
    private javax.swing.JLabel lblChequeNo;
    private javax.swing.JLabel lblChqDate;
    public static javax.swing.JLabel lblCreditCustCode;
    private javax.swing.JLabel lblCustName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelBoyName;
    private javax.swing.JLabel lblDeliveryCharges;
    private javax.swing.JLabel lblDisc;
    private javax.swing.JLabel lblDiscAmt;
    private javax.swing.JLabel lblExpiryDate;
    private javax.swing.JLabel lblFolioNo;
    private javax.swing.JLabel lblGVSeriesNo;
    private javax.swing.JLabel lblGVouchName;
    private javax.swing.JLabel lblGuestCode;
    private javax.swing.JLabel lblGuestName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblJioDestination;
    private javax.swing.JLabel lblJioMoneyCode;
    private javax.swing.JLabel lblManualBillNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaymentMode;
    private javax.swing.JLabel lblPaymentModeLabel;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRefund;
    private javax.swing.JLabel lblRemark;
    private javax.swing.JLabel lblRemarkLabel;
    private javax.swing.JLabel lblRoomNo;
    private javax.swing.JLabel lblSlipNo;
    private javax.swing.JLabel lblTableNo;
    private javax.swing.JLabel lblTip;
    private javax.swing.JLabel lblTipAmount;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblVNoLabel;
    private javax.swing.JLabel lblVoucherNo;
    private javax.swing.JLabel lblcard;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAmt;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JPanel panelDiscount;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelMode;
    private javax.swing.JPanel panelNumericPad;
    private javax.swing.JPanel panelRoomSettlement;
    private javax.swing.JPanel panelSettlement;
    private javax.swing.JRadioButton rdbAll;
    private javax.swing.JRadioButton rdbGroupWise;
    private javax.swing.JRadioButton rdbItemWise;
    private javax.swing.JRadioButton rdbSubGroupWise;
    private javax.swing.JScrollPane scrSettle;
    private javax.swing.JScrollPane scrTax;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblSettlement;
    private javax.swing.JTable tblTaxTable;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextArea txtAreaRemark;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtCardName;
    private javax.swing.JTextField txtChequeNo;
    private javax.swing.JTextField txtCoupenAmt;
    public static javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDeliveryCharges;
    private javax.swing.JTextField txtDiscountAmt;
    private javax.swing.JTextField txtDiscountPer;
    private javax.swing.JTextField txtFolioNo;
    private javax.swing.JTextField txtGuestCode;
    private javax.swing.JTextField txtGuestName;
    private javax.swing.JTextField txtJioDestination;
    private javax.swing.JTextField txtJioMoneyCode;
    private javax.swing.JTextField txtManualBillNo;
    private javax.swing.JTextField txtPaidAmt;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtRoomNo;
    private javax.swing.JTextField txtSeriesNo;
    private javax.swing.JTextField txtTip;
    private javax.swing.JTextField txtVoucherSeries;
    // End of variables declaration//GEN-END:variables

    void setCardName(String textValue)
    {
	txtCardName.setText(textValue);
    }

    void setBankName(String text)
    {
	txtBankName.setText(text);
    }

    void setRemark(String text)
    {
	txtRemark.setText(text);
    }

    void setChequeNo(String text)
    {
	txtChequeNo.setText(text);
    }

    public void setBillData(String BillNo, String billFrom, String areaCode)
    {
	try
	{
//            selectedBillNo = BillNo;
	    OrderPanel.setVisible(true);

	    settleMode = false;

	    //funResetSettlementButtons();
	    fun_FillSettlementBtns();
	    _settlementNavigate = 0;

	    btnSettle.setEnabled(true);
	    clsSettelementOptions objSettlementOptions = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
	    settleType = objSettlementOptions.getStrSettelmentType();
	    settlementCode = objSettlementOptions.getStrSettelmentCode();//use while calculating tax for settlement

	    _paidAmount = 0.00;

	    txtAreaRemark.setText("");
	    hmSettlemetnOptions.clear();
	    this.areaCode = areaCode;
	    operationTypeForTax = "DineIn";

	    this.billType = billType;
	    if (billFrom.equals("Direct Biller"))
	    {
		this.billType = "Direct Biller";
		this.areaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    }
	    else
	    {
		this.billType = "Make KOT";
	    }
	    voucherNo = BillNo;
	    lblVoucherNo.setText(BillNo);

	    String sql_CustCode = "select a.strCustomerCode,a.strRemarks,a.strCRMRewardId,a.strReasonCode,a.strNSCTax,ifnull(b.strCustomerName,'')strCustomerName "
		    + "from tblbillhd a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + "where strBillNo='" + BillNo + "' "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
	    ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustCode);
	    if (rsCustCode.next())
	    {
		custCode = rsCustCode.getString(1);
		clsGlobalVarClass.gCustomerName = rsCustCode.getString(6);

		txtAreaRemark.setText(rsCustCode.getString(2));
		rewardId = rsCustCode.getString(3);
		selectedReasonCode = rsCustCode.getString(4);
		if (rsCustCode.getString(5) != null && rsCustCode.getString(5).trim().length() > 0)
		{
		    isRemoveSCTax = true;
		    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
		    {
			btnRemoveSCTax.setForeground(Color.red);
		    }
		}
	    }
	    rsCustCode.close();

	    String sqlHomeDel = "select count(strBillNo) from tblhomedelivery "
		    + "where strBillNo='" + BillNo + "' ";
	    ResultSet rsHomeDel = clsGlobalVarClass.dbMysql.executeResultSet(sqlHomeDel);
	    if (rsHomeDel.next())
	    {
		if (rsHomeDel.getInt(1) > 0)
		{
		    operationTypeForTax = "HomeDelivery";
		}
	    }
	    rsHomeDel.close();

	    String sql_TakeAway = "select strOperationType from tblbillhd "
		    + "where strBillNo='" + BillNo + "' "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
	    ResultSet rsTakeAway = clsGlobalVarClass.dbMysql.executeResultSet(sql_TakeAway);
	    if (rsTakeAway.next())
	    {
		if (rsTakeAway.getString(1).equalsIgnoreCase("TakeAway"))
		{
		    operationTypeForTax = "TakeAway";
		}
	    }
	    rsTakeAway.close();

	    if ("Direct Biller".equalsIgnoreCase(billFrom))
	    {
		funFillGridForAdvOrderAndDirectBillerBills("Unsettled Bills", BillNo);
	    }
	    else
	    {
		funSetTableNameVisible(true);
		String sql1 = "select ifnull(a.strTableNo,''),ifnull(b.strTableName,'') "
			+ "from tblbillhd a,tbltablemaster b "
			+ "where a.strTableNo=b.strTableNo and a.strBillNo='" + BillNo + "' "
			+ "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "'  ";
		ResultSet rsBill = clsGlobalVarClass.dbMysql.executeResultSet(sql1);

		if (rsBill.next())
		{
		    String tbno = rsBill.getString(1);
		    tableNo = tbno;
		    lblTableNo.setText(rsBill.getString(2));
		}
		rsBill.close();
		funFillGridForMakeKOTTransaction("", false, "Unsettled Bills", BillNo);
	    }
	    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		btnPrint.setVisible(true);
		btnSettle.setVisible(false);
		funDisableSettelementButtons();
		funShowDiscountPannel(true);
	    }
	    else if ("SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType) || "AddKOTToBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		funShowDiscountPannel(false);
		clsSettelementOptions objSettlementList = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
		if (!objSettlementList.getStrSettelmentType().equals("Debit Card"))
		{
		    procSettlementBtnClick(objSettlementList);
		}
		btnPrint.setVisible(false);
		btnReprint.setVisible(true);
		btnSettle.setVisible(true);
		txtPaidAmt.requestFocus();
		txtPaidAmt.selectAll();
		btnShowCompliItems.setVisible(false);
	    }
	    lblTipAmount.setVisible(false);
	    txtTip.setVisible(false);

	    lblManualBillNo.setVisible(false);
	    txtManualBillNo.setVisible(false);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-66", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    public void showPanel()
    {
	panelSettlement.setVisible(true);
	if(clsGlobalVarClass.gReprintOnSettleBill)
	{
	    btnReprint.setVisible(true);
	}	
	else
	{
	    btnReprint.setVisible(false);
	}	
	txtPaidAmt.requestFocus();
	txtPaidAmt.selectAll();
    }

    // Bill From KOT
    public void funSetBillFromKOTS(List listKOTs)
    {
	try
	{
	    operationTypeForTax = "DineIn";
	    //objPannelShowBills = null;//showbillpannel object
	    btnPrint.setVisible(false);
	    btnSettle.setVisible(true);
	    btnSettle.setEnabled(true);
	    btnShowCompliItems.setVisible(false);
	    lblTipAmount.setVisible(false);
	    txtTip.setVisible(false);
	    billType = "Bill From KOTs";
	    billTypeForTax = "Bill From KOTs";
	    btnPrint.setVisible(false);
	    listBillFromKOT = listKOTs;
	    chkDiscFromMaster.setSelected(false);

	    fillMakeTableForBillFromKOTs(listKOTs);

	    hmSettlemetnOptions = new HashMap<>();
	    clsSettelementOptions obj_list_sett = clsSettelementOptions.hmSettelementOptionsDtl.get(btnSettlement1.getText());
	    procSettlementBtnClick(obj_list_sett);

	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-66", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private void funGiftVoucher()
    {
	_giftVoucherCode = txtSeriesNo.getText();
	String temp = txtSeriesNo.getText();
	double dblSettlementAmountTemp;
	String giftVoucherNum = "";
	if (txtVoucherSeries.getText().trim().length() == 0)
	{
	    new frmOkPopUp(null, "Please Select Voucher Name", "Warning", 1).setVisible(true);
	    txtVoucherSeries.requestFocus();
	}
	else if (temp.length() > 0)
	{
	    for (int i = 0; i < temp.length(); i++)
	    {
		if (temp.charAt(i) < 65)
		{
		    giftVoucherNum = temp.substring(i);
		    break;
		}
	    }

	    if (giftVoucherNum.trim().length() == 0)
	    {
		new frmOkPopUp(null, "Invalid Gift Voucher", "Warning", 1).setVisible(true);
		txtSeriesNo.requestFocus();
	    }
	    else if (!clsGlobalVarClass.validateIntegers(txtSeriesNo.getText()))
	    {
		new frmOkPopUp(null, "Enter numbers only", "Warning", 1).setVisible(true);
		txtSeriesNo.requestFocus();
	    }
	    else if (funCheckDuplicateGiftVoucher())
	    {
		try
		{
		    String sql_tblgiftvoucher = "select intGiftVoucherStartNo,intGiftVoucherEndNo,strGiftVoucherValueType"
			    + ",dblGiftVoucherValue,date(dteValidFrom),date(dteValidTo) "
			    + "from tblgiftvoucher where strGiftVoucherName='" + txtVoucherSeries.getText().trim() + "'";
		    ResultSet rsGiftVoucherdtl;
		    rsGiftVoucherdtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_tblgiftvoucher);
		    rsGiftVoucherdtl.next();
		    int giftVoucherSeriesStartNo = rsGiftVoucherdtl.getInt(1);
		    int giftVoucherSeriesEndNo = rsGiftVoucherdtl.getInt(2);
		    String giftVoucherValueType = rsGiftVoucherdtl.getString(3);
		    double giftVoucherValue = rsGiftVoucherdtl.getDouble(4);
		    String validFrom = rsGiftVoucherdtl.getString(5);
		    String validTo = rsGiftVoucherdtl.getString(6);
		    int giftVoucherNo = Integer.parseInt(giftVoucherNum);

		    if (giftVoucherNo >= giftVoucherSeriesStartNo && giftVoucherNo <= giftVoucherSeriesEndNo)
		    {
			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dtPOSDate = dFormat.parse(clsGlobalVarClass.gPOSStartDate);
			long posTime = dtPOSDate.getTime();

			dFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dtGiftVoucherValidTo = dFormat.parse(validTo);
			long gfValidToTime = dtGiftVoucherValidTo.getTime();

			dFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dtGiftVoucherValidFrom = dFormat.parse(validFrom);
			long gfValidFromTime = dtGiftVoucherValidFrom.getTime();

			if ((gfValidToTime - posTime) >= 0 && (posTime - gfValidFromTime) >= 0)
			{
			    if ("Discount %".trim().equalsIgnoreCase(giftVoucherValueType))
			    {
				double discount = (_subTotal * giftVoucherValue) / 100;
				if (giftVoucherValue == 100)
				{
				    discount = Double.parseDouble(txtAmount.getText());
				    dblSettlementAmountTemp = 0;
				}
				else
				{
				    dblSettlementAmountTemp = _subTotal - discount;
				}
				txtPaidAmt.setText(String.valueOf(Math.rint(discount)));
				txtBalance.setText(String.valueOf(Math.rint(dblSettlementAmountTemp)));
			    }
			    else
			    {
				if (giftVoucherValue >= dblSettlementAmount)
				{
				    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
				}
				else
				{
				    dblSettlementAmountTemp = dblSettlementAmount - giftVoucherValue;
				    txtPaidAmt.setText(String.valueOf(Math.rint(giftVoucherValue)));
				    txtBalance.setText(String.valueOf(Math.rint(dblSettlementAmountTemp)));
				}
			    }
			    flgGiftVoucherOK = true;
			}
			else
			{
			    new frmOkPopUp(null, "This Gift Voucher is Expired.", "Warning", 1).setVisible(true);
			}
		    }
		    else
		    {
			new frmOkPopUp(null, "Invalid Gift Voucher No.", "Warning", 1).setVisible(true);
		    }
		}
		catch (Exception e)
		{

		    objUtility.funShowDBConnectionLostErrorMessage(e);	
		    objUtility.funWriteErrorLog(e);
		}
	    }
	    else
	    {
		new frmOkPopUp(null, "Gift Voucher Already Used", "Warning", 1).setVisible(true);
		_giftVoucherCode = "";
	    }
	}
	else
	{
	    new frmOkPopUp(null, "Please Enter Voucher No.", "Warning", 1).setVisible(true);
	    txtSeriesNo.requestFocus();
	}
    }

    private boolean funCheckDuplicateGiftVoucher()
    {
	boolean flagDuplicate = false;
	try
	{
	    int gfvCode = Integer.parseInt(_giftVoucherCode);
	    String sql_count = "select count(*) from tblbillsettlementdtl where strGiftVoucherCode ='" + _giftVoucherSeriesCode + gfvCode + "'";
	    ResultSet rscount = clsGlobalVarClass.dbMysql.executeResultSet(sql_count);
	    rscount.next();
	    int count = rscount.getInt(1);
	    rscount.close();
	    if (count > 0)
	    {
		flagDuplicate = false;
	    }
	    else
	    {
		flagDuplicate = true;
	    }
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-67", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
	finally
	{
	    return flagDuplicate;
	}
    }

    public void funSetGiftVoucherData(Object[] data)
    {
	txtVoucherSeries.setText(data[0].toString());
	_giftVoucherSeriesCode = data[1].toString();
    }

    private String funCardNo()
    {
	String retDebitCardNo = "";
	try
	{
	    String sql = "";
	    if (clsGlobalVarClass.gTransactionType.equals("SettleBill"))
	    {
		sql = "select a.strCardNo,b.strCardString "
			+ " from tblbillhd a,tbldebitcardmaster b "
			+ " where a.strCardNo=b.strCardNo and a.strBillNo='" + lblVoucherNo.getText().trim() + "'";
	    }
	    else
	    {
		sql = "select a.strCardNo,b.strCardString "
			+ " from tblitemrtemp a,tbldebitcardmaster b "
			+ " where a.strCardNo=b.strCardNo and a.strTableNo='" + tableNo + "' "
			+ " group by a.strTableNo ";
	    }
	    ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsDebitCardNo.next())
	    {
		retDebitCardNo = rsDebitCardNo.getString(1);
		clsGlobalVarClass.gDebitCardNo = rsDebitCardNo.getString(2);
	    }
	    rsDebitCardNo.close();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-72", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
	finally
	{
	    return retDebitCardNo;
	}
    }

    private void funShowDiscountPannel(boolean flag)
    {
	panelDiscount.setVisible(flag);
    }

    private void funFillGroupSubGroupList(ArrayList<String> arrListItemCode)
    {
	StringBuilder sb = new StringBuilder();
	try
	{
	    listSubGroupName = new ArrayList<>();
	    listSubGroupCode = new ArrayList<>();
	    listGroupName = new ArrayList<>();
	    listGroupCode = new ArrayList<>();
	    listSubGroupName.add("--select--");
	    listSubGroupCode.add("--select--");
	    listGroupName.add("--select--");
	    listGroupCode.add("--select--");
	    boolean first = true;
	    for (String test : arrListItemCode)
	    {
		if (first)
		{
		    sb.append("'").append(test).append("");
		    first = false;
		}
		else
		{
		    sb.append("','").append(test).append("");
		}
	    }
	    //String t1 = sb.toString()+"'";
	    sb.append("'");
	    if (sb.toString().trim().length() > 1)
	    {
		String sql_List = "select a.strSubGroupCode,b.strSubGroupName,c.strGroupCode,c.strGroupName "
			+ " from tblitemmaster a , tblsubgrouphd b,tblgrouphd c"
			+ " where a.strItemCode IN (" + sb.toString() + ") and a.strDiscountApply='Y' "
			+ " and a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode;";

		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql_List);
		while (rs.next())
		{
		    if (!listSubGroupCode.contains(rs.getString(1)))
		    {
			listSubGroupCode.add(rs.getString(1));
			listSubGroupName.add(rs.getString(2));
		    }
		    if (!listGroupCode.contains(rs.getString(3)))
		    {
			listGroupCode.add(rs.getString(3));
			listGroupName.add(rs.getString(4));
		    }
		}
		rs.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-74", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	finally
	{
	    sb = null;
	}
    }

    private void funFillComboBoxForSubgroup()
    {
	try
	{
	    DefaultComboBoxModel dm = (DefaultComboBoxModel) cmbItemCategory.getModel();
	    dm.removeAllElements();
	    for (Object ob : listSubGroupName)
	    {
		dm.addElement(ob);
	    }
	    cmbItemCategory.setModel(dm);
	}
	catch (Exception e)
	{

	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-75", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funAddButtonToGroup()
    {
	radioButtonGroup = new ButtonGroup();
	radioButtonGroup.add(rdbSubGroupWise);
	radioButtonGroup.add(rdbGroupWise);
	radioButtonGroup.add(rdbItemWise);
	radioButtonGroup.add(rdbAll);
	radioButtonGroup.clearSelection();
    }

    private void funFillComboBoxForGroup()
    {
	try
	{
	    DefaultComboBoxModel dm = (DefaultComboBoxModel) cmbItemCategory.getModel();
	    dm.removeAllElements();
	    for (Object ob : listGroupName)
	    {
		dm.addElement(ob);
	    }
	    cmbItemCategory.setModel(dm);
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-76", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funEnableGroupSubGroupCombo()
    {
	if (!cmbItemCategory.isEnabled())
	{
	    cmbItemCategory.setEnabled(true);
	}
    }

    private void funSetDefaultRadioBtnForDiscount()
    {
	rdbAll.setSelected(true);
	cmbItemCategory.setEnabled(false);
    }

    /**
     * Ritesh
     *
     * @param tableno
     * @return
     * @throws Exception
     */
    private double funSumOfAllDiscountApplicableItem(String tableno)
    {
	double sumTotalAmt = 0.00;
	try
	{
	    Map mapItemCode = new HashMap<String, String>();
	    String sql = "";
	    boolean flag_UnsettledBill = false;
	    boolean flag_DirectBiller = false;
	    boolean flag_DinaStyle = false;

	    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		//bill no
		sql = "select a.strItemCode,a.strItemName,a.dblAmount "
			+ "from tblbilldtl a,tblitemmaster b "
			+ "where a.strBillNo='" + voucherNo + "'  and a.tdhYN='N' and a.strItemCode=b.strItemCode "
			+ "and a.strItemCode=b.strItemCode and b.strDiscountApply='Y'";
		flag_UnsettledBill = true;
	    }
	    else if ("Bill From KOT".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		StringBuilder sbBillFromKOTs = new StringBuilder();
		List<String> list_KOTNos = new ArrayList<String>();

		list_KOTNos = listBillFromKOT;
		if (!list_KOTNos.isEmpty())
		{
		    boolean first = true;
		    for (String kot : list_KOTNos)
		    {
			if (first)
			{
			    //sqlAppendForBillFromKOTS+="( strKOTNo='"+kot+"'" ;
			    sbBillFromKOTs.append("( strKOTNo='");
			    sbBillFromKOTs.append(kot);
			    sbBillFromKOTs.append("'");
			    first = false;
			}
			else
			{
			    //sqlAppendForBillFromKOTS+=" or ".concat(" strKOTNo='"+kot+"' ");
			    sbBillFromKOTs.append(" or ");
			    sbBillFromKOTs.append(" strKOTNo='");
			    sbBillFromKOTs.append(kot);
			    sbBillFromKOTs.append("' ");
			}
		    }
		}

		sql = "select a.strItemCode,a.strItemName,a.dblAmount "
			+ "from tblitemrtemp a,tblitemmaster b "
			+ "where " + sbBillFromKOTs + ") "
			+ "and left(a.strItemCode,7)=b.strItemCode and b.strDiscountApply='Y'";
	    }
	    else
	    {
		if ("Direct Biller".equalsIgnoreCase(billType))
		{
		    boolean first = true;
		    StringBuilder sb = new StringBuilder();
		    for (String test : listItemCode)
		    {
			if (first)
			{
			    sb.append("'").append(test).append("");
			    first = false;
			}
			else
			{
			    sb.append("','").append(test).append("");
			}
		    }
		    String t1 = sb.toString();
		    t1 = t1 + "'";
		    sql = "select b.strItemCode "
			    + "from tblitemmaster b "
			    + "where b.strItemCode IN (" + t1 + ") and b.strDiscountApply='Y'";
		    flag_DirectBiller = true;
		}
		else
		{
		    // for Dina Style take item from tblitemrtemp
		    if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("AddKOTToBill"))
		    {
			sql = "select a.strItemCode,a.strItemName,a.dblAmount,a.strSerialNo "
				+ "from tblitemrtemp a,tblitemmaster b "
				+ "where a.strTableNo IN " + tableno + " and a.strItemCode=b.strItemCode  and b.strDiscountApply='Y'";
		    }
		    else
		    {
			sql = "select a.strItemCode,a.strItemName,a.dblAmount,a.strSerialNo "
				+ "from tblitemrtemp a,tblitemmaster b "
				+ "where a.strTableNo='" + tableno + "' and a.strItemCode=b.strItemCode  and b.strDiscountApply='Y'";
		    }
		    flag_DinaStyle = true;
		}
	    }
	    if (!flag_DirectBiller)
	    {
		String sql_modifier = "";
		boolean flgModifier = false;
		ResultSet rsTotalDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsTotalDiscount.next())
		{
		    flgModifier = false;
		    sumTotalAmt = sumTotalAmt + rsTotalDiscount.getDouble(3);
		    if (mapItemCode.size() == 0)
		    {
			mapItemCode.put(rsTotalDiscount.getString(1), rsTotalDiscount.getString(2));
			flgModifier = true;
		    }
		    else
		    {
			if (!mapItemCode.containsKey(rsTotalDiscount.getString(1)))
			{
			    flgModifier = true;
			    mapItemCode.put(rsTotalDiscount.getString(1), rsTotalDiscount.getString(2));
			}
		    }
		    if (flgModifier)
		    {
			if (flag_UnsettledBill)
			{
			    sql_modifier = "select sum(dblAmount) from tblbillmodifierdtl "
				    + "where strBillNo='" + voucherNo + "' "
				    + "and left(strItemCode,7)='" + rsTotalDiscount.getString(1) + "' group by strItemCode;";
			}
			else if (flag_DinaStyle)
			{
			    if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("AddKOTToBill"))
			    {
				sql_modifier = "select sum(dblAmount) from tblitemrtemp "
					+ "where strTableNo IN " + tableno + " "
					+ "and strItemCode like '" + rsTotalDiscount.getString(1) + "M%' "
					+ "group by strItemCode ";
			    }
			    else
			    {
				sql_modifier = "select sum(dblAmount) from tblitemrtemp "
					+ "where strTableNo='" + tableno + "' "
					+ "and strItemCode like '" + rsTotalDiscount.getString(1) + "M%' "
					+ "group by strItemCode ";
			    }

			}
			if (!sql_modifier.isEmpty())
			{
			    ResultSet rs_Modifier = clsGlobalVarClass.dbMysql.executeResultSet(sql_modifier);
			    while (rs_Modifier.next())
			    {
				sumTotalAmt += rs_Modifier.getDouble(1);
			    }
			    rs_Modifier.close();
			}
		    }
		}
		rsTotalDiscount.close();
	    }
	    if (flag_DirectBiller)
	    {
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		List<clsDirectBillerItemDtl> objListDirectBillerItemDtl = objDirectBiller.getObj_List_ItemDtl();
		while (rs.next())
		{
		    for (clsDirectBillerItemDtl ob : objListDirectBillerItemDtl)
		    {
			if (rs.getString(1).equalsIgnoreCase(ob.getItemCode()))
			{
			    sumTotalAmt += ob.getAmt();
			}
		    }
		}
		rs.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-80", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	return sumTotalAmt;
    }

    private void funFillListForItemRow(String tempItemName, double tempQty, double tempAmt, String tempItemCode, double discAmt, double discPer)
    {
	clsBillItemDtl objBillItemDtl = new clsBillItemDtl();

	if (tempItemCode.contains("M"))
	{
	    tempItemName = tempItemName.toUpperCase();
	    if (hmBillItemDtl.containsKey(tempItemCode + "!" + tempItemName))
	    {
		objBillItemDtl = hmBillItemDtl.get(tempItemCode + "!" + tempItemName);
		objBillItemDtl.setQuantity(objBillItemDtl.getQuantity() + tempQty);
		objBillItemDtl.setAmount(objBillItemDtl.getAmount() + tempAmt);
		if (objBillItemDtl.getQuantity() == 0)
		{
		    objBillItemDtl.setDiscountAmount(0.00);
		}
		else
		{
		    objBillItemDtl.setDiscountAmount((objBillItemDtl.getAmount() * (objBillItemDtl.getDiscountPercentage() / 100)) / objBillItemDtl.getQuantity());
		}
	    }
	    else
	    {
		objBillItemDtl.setItemCode(tempItemCode);
		objBillItemDtl.setItemName(tempItemName);
		objBillItemDtl.setQuantity(tempQty);
		objBillItemDtl.setAmount(tempAmt);
		if (tempQty == 0)
		{
		    objBillItemDtl.setRate(0.00);
		}
		else
		{
		    objBillItemDtl.setRate(tempAmt / tempQty);
		}
		if (tempQty == 0)
		{
		    objBillItemDtl.setDiscountAmount(0.00);
		}
		else
		{
		    objBillItemDtl.setDiscountAmount(discAmt / tempQty);
		}
		objBillItemDtl.setDiscountPercentage(discPer);

		hmBillItemDtl.put(tempItemCode + "!" + tempItemName, objBillItemDtl);
	    }
	}
	else
	{
	    if (hmBillItemDtl.containsKey(tempItemCode))
	    {
		objBillItemDtl = hmBillItemDtl.get(tempItemCode);

		objBillItemDtl.setQuantity(objBillItemDtl.getQuantity() + tempQty);
		objBillItemDtl.setAmount(objBillItemDtl.getAmount() + tempAmt);
		if (objBillItemDtl.getQuantity() == 0)
		{
		    objBillItemDtl.setDiscountAmount(0.00);
		}
		else
		{
		    objBillItemDtl.setDiscountAmount((objBillItemDtl.getAmount() * (objBillItemDtl.getDiscountPercentage() / 100)) / objBillItemDtl.getQuantity());
		}
	    }
	    else
	    {
		objBillItemDtl.setItemCode(tempItemCode);
		objBillItemDtl.setItemName(tempItemName);
		objBillItemDtl.setQuantity(tempQty);
		objBillItemDtl.setAmount(tempAmt);
		if (tempQty == 0)
		{
		    objBillItemDtl.setRate(0.00);
		}
		else
		{
		    objBillItemDtl.setRate(tempAmt / tempQty);
		}
		if (tempQty == 0)
		{
		    objBillItemDtl.setDiscountAmount(0.00);
		}
		else
		{
		    objBillItemDtl.setDiscountAmount(discAmt / tempQty);
		}
		objBillItemDtl.setDiscountPercentage(discPer);

		hmBillItemDtl.put(tempItemCode, objBillItemDtl);
	    }
	}
	objBillItemDtl = null;
    }

    private void funSetTableNameVisible(boolean flag)
    {
	lblTableNo.setVisible(flag);
	labelTableNo.setVisible(flag);
    }

    private void funTextFilePrintingKOTForDirectBiller()
    {
//        clsTextFileGeneratorForPrinting ob = new clsTextFileGeneratorForPrinting();
//        ob.funRemotePrintUsingTextFile("", "", voucherNo, "", "DirectBiller", "Y");

	clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
	objKOTGeneration.funKOTGeneration("", "", voucherNo, "", "DirectBiller", "Y");

    }

    /**
     * :- Ritesh 07 Oct 2014 Purpose: Remove Button action code and make
     * separate Method To reuse code in future for Shortcut Key Implementation
     * In POS call When Print Button Pressed
     */
    private void funPrintButtonPresed()
    {
	btnPrint.setEnabled(false);

	if (clsGlobalVarClass.gBenowIntegrationYN)
	{
	    try
	    {
		String filePath = "QRCode.png";
		File file = new File(filePath);
		if (file.exists())
		{
		    file.delete();
		}
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		e.printStackTrace();
	    }
	}

	if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && isDirectSettleFromMakeBill)
	{
	    if (strButtonClicked.equals("Settle"))
	    {
		strButtonClicked = "Settle";
	    }
	    else
	    {
		strButtonClicked = "Print";
	    }
	}
	else
	{
	    strButtonClicked = "Print";
	}
	if ("Direct Biller".equalsIgnoreCase(billType))
	{
	    if (_grandTotal == 0 && hmComplimentaryBillItemDtl.size() > 0)
	    {
		btnSettle.setEnabled(false);
		JOptionPane.showMessageDialog(null, "Grand total is 0, Close the form and Use Complimetary Settlement type!!!");
		return;
	    }

	    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		dispose();
		new Thread()
		{
		    @Override
		    public void run()
		    {
			funModifyBill();
		    }
		}.start();
	    }
	    else
	    {
		if (null != clsGlobalVarClass.gCustomerCode)
		{
		    custCode = clsGlobalVarClass.gCustomerCode;
		}
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    custCode = objDirectBiller.getCmsMemberCode();
		    cmsMemberName = objDirectBiller.getCmsMemberName();
		}

		delPersonCode = clsGlobalVarClass.gDeliveryBoyCode;
		custMobileNoForCRM = "";
		custMobileNoForCRM = clsGlobalVarClass.gCustMobileNoForCRM;
		if (clsGlobalVarClass.gAdvOrderNoForBilling != null)
		{
		    advOrderBookingNo = clsGlobalVarClass.gAdvOrderNoForBilling;
		}
		takeAway = "No";
		if (clsGlobalVarClass.gTakeAway.equals("Yes"))
		{
		    takeAway = "Yes";
		}
		objDirectBiller.funResetFields();
		dispose();
		new Thread()
		{
		    @Override
		    public void run()
		    {
			funSaveBillForDBWithOutSettle();
		    }
		}.start();
	    }
	}
	else
	{
	    if (_grandTotal == 0 && hmComplimentaryBillItemDtl.size() > 0)
	    {
		btnSettle.setEnabled(false);
		JOptionPane.showMessageDialog(null, "Grand total is 0, Close the form and Use Complimetary Settlement type!!!");
		return;
	    }

	    if ("ModifyBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		dispose();
		new Thread()
		{
		    @Override
		    public void run()
		    {
			funModifyBill();
		    }
		}.start();
	    }
	    else if (flagAddKOTstoBill)
	    {
		/**
		 * Add KOT To Bill
		 */
		dispose();
		new Thread()
		{
		    @Override
		    public void run()
		    {
		    if (flgMakeBill)
		    {
			makeBillObj.funResetMakeBillTable();
		    }
		    else if (flgMakeKot)
		    {
			kotObj.funClearTable();
			kotObj.setVisible(true);
		    }
			funAddKOTToBill();
		    }
		}.start();
	    }
	    else if (flagBillForItems)
	    {
		/**
		 * Bill for items
		 */

		//code call for make bill and Make Kot form
		frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want Generate Bill");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{
		    dispose();
		    new Thread()
		    {
			@Override
			public void run()
			{
			    objBillSettlementUtility.funSaveBillForItemsWithoutSettleBill();
			}
		    }.start();
		}
		else
		{
		    btnPrint.setEnabled(true);
		}
	    }
	    else //code call for make bill and Make Kot form
	    {

		frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want Generate Bill");
		okOb.setVisible(true);
		int res = okOb.getResult();
		if (res == 1)
		{
		    objBillSettlementUtility.funUpdateTableStatus("Billed");
		    if (flgMakeBill)
		    {
			makeBillObj.funResetMakeBillTable();
		    }
		    else if (flgMakeKot)
		    {
			kotObj.funClearTable();
			kotObj.setVisible(true);
		    }

		    dispose();
		    new Thread()
		    {
			@Override
			public void run()
			{
			    funSaveWithoutSettleBill();
			}
		    }.start();
		}
		else
		{
		    btnPrint.setEnabled(true);
		}
	    }
	}
    }

    /**
     * :- Ritesh 07 Oct 2014 Purpose: Remove Button action code and make
     * separate Method To reuse code in future for Shortcut Key Implementation
     * In POS call When Settle Button Pressed
     */
    private void funSettleButtonPressed()
    {
	btnSettle.setEnabled(false);
	if (txtTip.getText().isEmpty())
	{
	    txtTip.setText("0.00");
	}
	tipAmount = Double.parseDouble(txtTip.getText());
	txtTip.setText("0.00");

	if (_balanceAmount != 0.00)
	{
	    btnSettle.setEnabled(true);
	    JOptionPane.showMessageDialog(this, "Balance is not zero");
	    return;
	}
	if (hmSettlemetnOptions.isEmpty())
	{
	    btnSettle.setEnabled(true);
	    new frmOkPopUp(null, "Select Settlement mode", "Warning", 1).setVisible(true);
	    return;
	}
	if (flgEnterBtnPressed == false)
	{
	    btnSettle.setEnabled(true);
	    new frmOkPopUp(null, "Please Press Enter Key", "Warning", 1).setVisible(true);
	    return;
	}

	strButtonClicked = "Settle";
	if ("Direct Biller".equalsIgnoreCase(billType))
	{
	    if ("SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		//dispose();
		DefaultTableModel dmItemTable = new DefaultTableModel();
		dmItemTable.setRowCount(0);
		tblItemTable.setModel(dmItemTable);
		objPannelShowBills.setVisible(true);
		panelSettlement.setVisible(false);
		OrderPanel.setVisible(false);
		clsGlobalVarClass.gDebitCardNo = "";

		new Thread()
		{
		    @Override
		    public void run()
		    {
			funOnlySettleBillDB();
		    }
		}.start();
	    }
	    else
	    {
		if (!clsGlobalVarClass.gSuperUser)
		{
		    if (clsGlobalVarClass.hmUserForms.containsKey("SettleBill"))
		    {
			if (null != clsGlobalVarClass.gCustomerCode)
			{
			    custCode = clsGlobalVarClass.gCustomerCode;
			}
			if (clsGlobalVarClass.gCMSIntegrationYN)
			{
			    custCode = objDirectBiller.getCmsMemberCode();
			    cmsMemberName = objDirectBiller.getCmsMemberName();
			}
			custMobileNoForCRM = "";
			custMobileNoForCRM = clsGlobalVarClass.gCustMobileNoForCRM;
			if (clsGlobalVarClass.gAdvOrderNoForBilling != null)
			{
			    advOrderBookingNo = clsGlobalVarClass.gAdvOrderNoForBilling;
			}
			takeAway = "No";
			if (clsGlobalVarClass.gTakeAway.equals("Yes"))
			{
			    takeAway = "Yes";
			}
			objDirectBiller.funResetFields();
			dispose();
			new Thread()
			{
			    @Override
			    public void run()
			    {
				funSaveBillForDB();
				objListDirectBillerItemDtl = null;
			    }
			}.start();
		    }
		    else
		    {
			new frmOkPopUp(null, "Access Denied.", "Settle Bill", 1).setVisible(true);
			btnSettle.setEnabled(true);
			return;
		    }
		}
		else
		{
		    if (null != clsGlobalVarClass.gCustomerCode)
		    {
			custCode = clsGlobalVarClass.gCustomerCode;
		    }
		    if (clsGlobalVarClass.gCMSIntegrationYN)
		    {
			custCode = objDirectBiller.getCmsMemberCode();
			cmsMemberName = objDirectBiller.getCmsMemberName();
		    }
		    custMobileNoForCRM = "";
		    custMobileNoForCRM = clsGlobalVarClass.gCustMobileNoForCRM;
		    if (clsGlobalVarClass.gAdvOrderNoForBilling != null)
		    {
			advOrderBookingNo = clsGlobalVarClass.gAdvOrderNoForBilling;
		    }
		    takeAway = "No";
		    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
		    {
			takeAway = "Yes";
		    }
		    objDirectBiller.funResetFields();
		    dispose();
		    new Thread()
		    {
			@Override
			public void run()
			{
			    funSaveBillForDB();
			    objListDirectBillerItemDtl = null;
			}
		    }.start();
		}
	    }
	}

	else if (billType.equals("Bill From KOTs")) // For Bill From KOTs Option
	{
	    //dispose();
	    DefaultTableModel dmItemTable = new DefaultTableModel();
	    dmItemTable.setRowCount(0);
	    tblItemTable.setModel(dmItemTable);
	    objPannelShowKOTs.setVisible(true);
	    panelSettlement.setVisible(false);
	    clsGlobalVarClass.gDebitCardNo = "";

	    new Thread()
	    {
		@Override
		public void run()
		{
		    funSaveBillForBillsFromKOTs();
		}
	    }.start();
	    //funSaveBillForBillsFromKOTs();
	}

	else
	{
	    //Dina Style Settle Bill option
	    //dispose();
	    if (callingFormName.equalsIgnoreCase("Make KOT"))
	    {
		dispose();
	    }
	    if (clsGlobalVarClass.gEnableSettleBtnForDirectBiller && isDirectSettleFromMakeBill)
	    {
		try
		{
		    funPrintButtonPresed();
		}
		catch (Exception e)
		{
		    objUtility.funShowDBConnectionLostErrorMessage(e);	
		    objUtility.funWriteErrorLog(e);
		    e.printStackTrace();
		}
	    }
	    else
	    {
		DefaultTableModel dmItemTable = new DefaultTableModel();
		dmItemTable.setRowCount(0);
		tblItemTable.setModel(dmItemTable);
		objPannelShowBills.setVisible(true);
		panelSettlement.setVisible(false);
		OrderPanel.setVisible(false);
		clsGlobalVarClass.gDebitCardNo = "";

		new Thread()
		{
		    @Override
		    public void run()
		    {
			funSaveBillForTable();
		    }
		}.start();
	    }
	}
    }

    /**
     * :- Ritesh 07 Oct 2014 Purpose: Remove Button action code and make
     * separate Method To reuse code in future for Shortcut Key Implementation
     * In POS call When Back Button Pressed
     */
    private void funBackButtonPressed()
    {
	clsGlobalVarClass.gAdvOrderNoForBilling = null;
	clsGlobalVarClass.gDebitCardNo = "";
	if (billType.equals("Bill From KOTs"))
	{
	    DefaultTableModel dmItemTable = new DefaultTableModel();
	    dmItemTable.setRowCount(0);
	    tblItemTable.setModel(dmItemTable);
	    panelSettlement.setVisible(false);
	    objPannelShowKOTs.setVisible(true);
	    objPannelShowKOTs.funFillGridWithKOT();
	}
	else if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill"))
	{
	    new com.POSTransaction.view.frmBillSettlement().setVisible(true);
	    dispose();
	}
	else if (billType.equals("Make KOT"))
	{
	    if (!"SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType) && !"AddKOTToBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		kotObj.funEnableControls();
		kotObj.setVisible(true);
	    }
	    if(objAddKOTToBill!=null)
	    {
		if(objAddKOTToBill.flgMergeKOTToBill){
		    if(objAddKOTToBill.objMakeKOT !=null){
			objAddKOTToBill.objMakeKOT.funEnableControls();
			objAddKOTToBill.objMakeKOT.setVisible(true);
		    }else if(objAddKOTToBill.objMakeBill !=null){
			objAddKOTToBill.objMakeBill.funEnableControls();
			try
			{
			    funUpdateTableStatusInItemRTemp(tableNo, "Normal");
			}
			catch (Exception e)
			{
			    objUtility.funShowDBConnectionLostErrorMessage(e);	
			    log.error("Problem while updating table status in tblitemrtemp", e);
			    log.trace("Problem while updating table status in tblitemrtemp");
			}

			objAddKOTToBill.objMakeBill = null;
			dispose();
			clsGlobalVarClass.hmActiveForms.remove("SettleBill");

		    }
		    
		}
	    }
	    makeBillObj = null;
	    kotObj = null;
	    String sql = "update tblitemrtemp set strPromoCode='' "
		    + " where strTableNo='" + tableNo + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
	    try
	    {
		funUpdateTableStatusInItemRTemp(tableNo, "Normal");
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		log.error("Problem while updating table status in tblitemrtemp", e);
		log.trace("Problem while updating table status in tblitemrtemp");
	    }

	    try
	    {
		clsGlobalVarClass.dbMysql.execute(sql);
		//insert into itemrtempbck table
		objUtility.funInsertIntoTblItemRTempBck(tableNo);
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		objUtility.funWriteErrorLog(e);
		e.printStackTrace();
	    }
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("SettleBill");
	}
	else if (billType.equals("Direct Biller"))
	{
	    if (!"SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		objDirectBiller.funEnableControls();
	    }
	    if (null != objListDirectBillerItemDtl)
	    {
		objListDirectBillerItemDtl.clear();
		objDirectBiller.setObj_List_ItemDtl(objListItemDtlTemp);
		objListItemDtlTemp = null;
		objDirectBiller = null;
	    }
	    //clsGlobalVarClass.gListWeightPerBox.clear();
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("SettleBill");
	}
	else if (billType.equals("Make Bill"))
	{
	    if (!"SettleBill".equalsIgnoreCase(clsGlobalVarClass.gTransactionType))
	    {
		makeBillObj.funEnableControls();
	    }

	    try
	    {
		funUpdateTableStatusInItemRTemp(tableNo, "Normal");
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		log.error("Problem while updating table status in tblitemrtemp", e);
		log.trace("Problem while updating table status in tblitemrtemp");
	    }

	    makeBillObj = null;
	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("SettleBill");
	}
	else if (billType.equalsIgnoreCase("Bill For Items"))
	{

	    try
	    {
		funUpdateTableStatusInItemRTemp(tableNo, "Normal");
	    }
	    catch (Exception e)
	    {
		objUtility.funShowDBConnectionLostErrorMessage(e);	
		log.error("Problem while updating table status in tblitemrtemp", e);
		log.trace("Problem while updating table status in tblitemrtemp");
	    }

	    dispose();
	    clsGlobalVarClass.hmActiveForms.remove("SettleBill");
	}
    }

    private void fun_FillSettlementBtns()
    {
	btnPrevSettlementMode.setEnabled(false);
	btnNextSettlementMode.setEnabled(false);
	JButton btnArray[] =
	{
	    btnSettlement1, btnSettlement2, btnSettlement3, btnSettlement4
	};
	for (int i = 0; i < 4; i++)
	{
	    btnArray[i].setText("");
	    btnArray[i].setVisible(false);
	}
	if (noOfSettlementMode == 4)
	{
	    for (int i = 0; i < 4; i++)
	    {
		btnArray[i].setText(clsSettelementOptions.listSettelmentOptions.get(i));
		btnArray[i].setVisible(true);
	    }
	}
	else if (noOfSettlementMode < 4)
	{
	    int i = 0;
	    for (String settelementName : clsSettelementOptions.listSettelmentOptions)
	    {
		btnArray[i].setText(settelementName);
		btnArray[i].setVisible(true);
		i++;
	    }
	}
	else if (noOfSettlementMode > 4)
	{
	    int i = 0;
	    btnNextSettlementMode.setEnabled(true);
	    for (String settelementName : clsSettelementOptions.listSettelmentOptions)
	    {
		btnArray[i].setText(settelementName);
		btnArray[i].setVisible(true);
		if (i == 3)
		{
		    break;
		}
		i++;
	    }
	}
    }

    public static void funSetCreditCustomerInfo(String creditCustCode, String creditCustName)
    {
	customerCodeForCredit = creditCustCode;
	txtCustomerName.setText(creditCustName);
    }

    private void funEnterButtonPressed()
    {
	if (txtPaidAmt.getText().trim().length() == 0)
	{
	    _paidAmount = 0.00;
	}
	else
	{
	    _paidAmount = Double.parseDouble(gDecimalFormat.format(Double.parseDouble(txtPaidAmt.getText())));
	}
	if (_paidAmount == 0.00 && _grandTotal != 0.00)
	{
	    new frmOkPopUp(null, "Please Enter Amount", "Warning", 1).setVisible(true);
	    return;
	}

	if (settleType.equals("Debit Card"))
	{
	    if (!lblCardBalance.getText().isEmpty())
	    {
		double cardBal = Double.parseDouble(lblCardBalance.getText());
		if (cardBal < _paidAmount)
		{
		    new frmOkPopUp(null, "Insufficient Amount in Card", "Warning", 1).setVisible(true);
		    return;
		}
	    }
	}

	if (settleMode == true && (_balanceAmount != 0.00 || hmSettlemetnOptions.isEmpty()))
	{
	    switch (settleType)
	    {
		case "Cash":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			//hmSettlemetnOptions.put(settleName, new clsSettelementOptions(_settlementCode, dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", settleType));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Credit Card":
		    if ("".equals(txtPaidAmt.getText()))
		    {
			new frmOkPopUp(null, "Please Enter Amount", "Warning", 1).setVisible(true);
			return;
		    }
		    if (Double.parseDouble(txtPaidAmt.getText()) < 0)
		    {
			new frmOkPopUp(null, "Invalid paid amount", "Warning", 1).setVisible(true);
			return;
		    }
		    if (clsGlobalVarClass.gCreditCardSlipNo)
		    {
			if (txtCardName.getText().trim().length() <= 0)
			{
			    new frmOkPopUp(null, "Please Enter Slip No.", "Warning", 1).setVisible(true);
			    txtCardName.requestFocus();
			    return;
			}
		    }
		    Date objCreditCardExpDate = dteExpiry.getDate();
		    String expiryDate = "";
		    if (clsGlobalVarClass.gCreditCardExpiryDate)
		    {
			if (objCreditCardExpDate == null)
			{
			    JOptionPane.showMessageDialog(this, "Please Select Expiry Date");
			    return;
			}
			else
			{
			    expiryDate = objCreditCardExpDate.toString();
			}
		    }
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			ob.setStrCardName(txtCardName.getText().toString());
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, expiryDate, settleName, txtCardName.getText().toString(),objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Coupon":

		    _paidAmount = Double.parseDouble(txtCoupenAmt.getText().trim());
		    if (txtCoupenAmt.getText().trim().length() <= 0)
		    {
			new frmOkPopUp(null, "Please Enter Amount", "Warning", 1).setVisible(true);
			return;
		    }
		    if (_paidAmount < 0)
		    {
			new frmOkPopUp(null, "Invalid paid amount", "Warning", 1).setVisible(true);
			return;
		    }
		    if (txtRemark.getText().trim().isEmpty())
		    {
			new frmOkPopUp(null, "Please Enter Remark", "Warning", 1).setVisible(true);
			return;
		    }
		    else
		    {
			if (hmSettlemetnOptions.containsKey(settleName))
			{
			    clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			    double tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    ob.setDblRefundAmt(_refundAmount);
			    hmSettlemetnOptions.put(settleName, ob);
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			}
			else
			{
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
			}
		    }
		    break;

		case "Cheque":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			//hmSettlemetnOptions.put(settleName, new clsSettelementOptions(_settlementCode, dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", settleType));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Gift Voucher":
		    if (!flgGiftVoucherOK)
		    {
			new frmOkPopUp(null, "Press OK button on Gift Voucher", "Warning", 1).setVisible(true);
			return;
		    }

		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
			//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, _giftVoucherSeriesCode.concat(_giftVoucherCode), ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
		    }
		    break;

		case "Complementary":

		    if (hmSettlemetnOptions.size() > 0)
		    {
			JOptionPane.showMessageDialog(this, "Coplimentary Settlement is Not Allowed In MultiSettlement!!!");
			return;
		    }
		    if (txtAreaRemark.getText().trim().length() == 0)
		    {
			JOptionPane.showMessageDialog(this, "Please Enter Remarks");
			return;
		    }
		    if (vComplReasonCode.size() == 0)
		    {
			JOptionPane.showMessageDialog(this, "No complementary reasons are created");
			return;
		    }
		    else
		    {
			Object[] arrObjReasonCode = vComplReasonCode.toArray();
			Object[] arrObjReasonName = vComplReasonName.toArray();
			String selectedReason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
			if (null == selectedReason)
			{
			    JOptionPane.showMessageDialog(this, "Please Select Reason");
			    return;
			}
			else
			{
			    for (int cntReason = 0; cntReason < vComplReasonCode.size(); cntReason++)
			    {
				if (vComplReasonName.elementAt(cntReason).toString().equals(selectedReason))
				{
				    selectedReasonCode = vComplReasonCode.elementAt(cntReason).toString();
				    break;
				}
			    }
			    //complementaryRemarks = txtAreaRemark.getText().trim();
			    _refundAmount = 0.00;
			    _balanceAmount = 0.00;
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			}
		    }
		    break;

		case "Credit":

		    if (customerCodeForCredit.isEmpty())
		    {
			JOptionPane.showMessageDialog(null, "Please Select Customer!!!");
			return;
		    }

		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double temp_paidAmount = ob.getDblPaidAmt();
			temp_paidAmount += _paidAmount;
			ob.setDblPaidAmt(temp_paidAmount);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Debit Card":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt = _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Loyality Points":
		    if (funCheckPointsAgainstCustomer())
		    {
			if (hmSettlemetnOptions.containsKey(settleName))
			{
			    clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			    double tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    ob.setDblRefundAmt(_refundAmount);
			    hmSettlemetnOptions.put(settleName, ob);
			}
			else
			{
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			}
		    }
		    else
		    {
			return;
		    }
		    break;

		case "Member":
		    if (cmsStopCredit.equals("Y"))
		    {
			JOptionPane.showMessageDialog(null, "Credit Facility Is Stopped For This Member!!!");
		    }
		    else if (cmsMemberCreditLimit > 0)
		    {
			if (cmsMemberBalance < dblSettlementAmount)
			{
			    JOptionPane.showMessageDialog(this, "Credit Limit Exceeds, Balance Credit: " + cmsMemberBalance);
			    return;
			}
			if (_paidAmount <= cmsMemberBalance)
			{
			    cmsMemberBalance = 0;
			    if (hmSettlemetnOptions.containsKey(settleName))
			    {
				clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
				double tempPaidAmt = ob.getDblPaidAmt();
				tempPaidAmt += _paidAmount;
				ob.setDblPaidAmt(tempPaidAmt);
				ob.setDblRefundAmt(_refundAmount);
				hmSettlemetnOptions.put(settleName, ob);
				//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			    }
			    else
			    {
				clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
				hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
				//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
			    }
			}
		    }
		    else
		    {
			cmsMemberBalance = 0;
			if (hmSettlemetnOptions.containsKey(settleName))
			{
			    clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			    double tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    ob.setDblRefundAmt(_refundAmount);
			    hmSettlemetnOptions.put(settleName, ob);
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			}
			else
			{
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
			}
		    }
		    break;

		case "Room":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			clsSettelementOptions objSettleOpt = new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", "");
			objSettleOpt.setStrFolioNo(txtFolioNo.getText());
			objSettleOpt.setStrRoomNo(txtRoomNo.getText());
			objSettleOpt.setStrGuestCode(txtGuestCode.getText());

			hmSettlemetnOptions.put(settleName, objSettleOpt);
		    }
		    break;

		case "JioMoney":

		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			clsSettelementOptions objSettleOpt = new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", "");
			hmSettlemetnOptions.put(settleName, objSettleOpt);
		    }
		    break;

		case "Online Payment":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			double tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			ob.setDblRefundAmt(_refundAmount);
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			//hmSettlemetnOptions.put(settleName, new clsSettelementOptions(_settlementCode, dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", settleType));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Benow":
		    if (clsGlobalVarClass.gBenowIntegrationYN)
		    {
			if (hmSettlemetnOptions.containsKey(settleName))
			{
			    clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			    double tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    ob.setDblRefundAmt(_refundAmount);
			    hmSettlemetnOptions.put(settleName, ob);
			}
			else
			{
			    clsBenowIntegration objBenowIntegration = new clsBenowIntegration();
			    JSONObject jsonTransStatus = objBenowIntegration.funCheckTransactionStatusForBenowPayment(voucherNo);
			    String transStatus = jsonTransStatus.get("paymentStatus").toString();
			    if (transStatus.equals("PAID"))
			    {
				clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
				clsSettelementOptions objSettleOpt = new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "",objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), clsGlobalVarClass.gBenowMerchantCode, QRStringForBenow, transStatus, voucherNo, "1", clsGlobalVarClass.getPOSDateForTransaction());//add transStatus,transId,and transDate from webservice response
				hmSettlemetnOptions.put(settleName, objSettleOpt);
			    }
			    else
			    {
				JOptionPane.showMessageDialog(null, "Transaction failed!!! Response is " + transStatus);
				return;
			    }
			}
		    }

		    break;
	    }
	    procClear();

	    btnSettle.requestFocus();
	    funRefreshItemTable();
	    flgEnterBtnPressed = true;
	}
	procClear();
    }

    private boolean funCheckPointsAgainstCustomer()
    {
	boolean flgResult = false;
	double totalLoyalityPoints = 0.00, totalReedemedPoints = 0.00;
	try
	{
	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("PMAM"))
	    {

	    }
	    else
	    {
		String mobileNo = "";
//		if (null != clsGlobalVarClass.gCustMobileNoForCRM)
//		{
//		    if (clsGlobalVarClass.gCustMobileNoForCRM.length() > 0)
//		    {
//			mobileNo = clsGlobalVarClass.gCustMobileNoForCRM;
//		    }
//		}
//		else
//		{
//		    String sql_Bill = "select b.longMobileNo from tblbillhd a,tblcustomermaster b "
//			    + "where a.strCustomerCode=b.strCustomerCode and a.strBillNo='" + voucherNo + "'";
//		    ResultSet rsBill = clsGlobalVarClass.dbMysql.executeResultSet(sql_Bill);
//		    if (rsBill.next())
//		    {
//			mobileNo = rsBill.getString(1);
//		    }
//		    rsBill.close();
//		}

		String sql_Bill = "select b.longMobileNo from tblbillhd a,tblcustomermaster b "
			+ "where a.strCustomerCode=b.strCustomerCode and a.strBillNo='" + voucherNo + "'";
		ResultSet rsBill = clsGlobalVarClass.dbMysql.executeResultSet(sql_Bill);
		if (rsBill.next())
		{
		    mobileNo = rsBill.getString(1);
		}
		rsBill.close();

		if (mobileNo.trim().length() > 0)
		{
		    String sql_Points = "select sum(dblPoints),sum(dblRedeemedAmt) "
			    + "from tblcrmpoints "
			    + "where longCustMobileNo='" + mobileNo + "' "
			    + "group by longCustMobileNo";
		    ResultSet rsPoints = clsGlobalVarClass.dbMysql.executeResultSet(sql_Points);
		    if (rsPoints.next())
		    {
			totalLoyalityPoints = Double.parseDouble(rsPoints.getString(1));
			totalReedemedPoints = Double.parseDouble(rsPoints.getString(2));
			totalLoyalityPoints = totalLoyalityPoints - totalReedemedPoints;
			if (Double.parseDouble(txtPaidAmt.getText()) <= totalLoyalityPoints)
			{
			    flgResult = true;
			    _loyalityPoints = Double.parseDouble(txtPaidAmt.getText());
			}
			else
			{
			    JOptionPane.showMessageDialog(this, "Your total Loyality points are " + totalLoyalityPoints);
			}
		    }
		    rsPoints.close();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return flgResult;
    }

    private double funGetTotalPaidAmount()
    {
	double totalPaidAmt = 0.00;
	for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	{
	    if ("Complementary".equalsIgnoreCase(ob.getStrSettelmentDesc()))
	    {
		totalPaidAmt = _grandTotal;
		break;
	    }
	    totalPaidAmt += ob.getDblPaidAmt();
	}
	return totalPaidAmt;
    }

    private void funVisibleDeliveryCharges(boolean flag)
    {
	lblDeliveryCharges.setVisible(flag);
	txtDeliveryCharges.setVisible(flag);
	btnApplyDeliveryCharge.setVisible(flag);
	lblDelBoyName.setVisible(flag);
	txtDeliveryCharges.setText("0.00");
    }

    private int funInsertBillHdTable(clsBillHd objBillHd) throws Exception
    {

	String sqlDelete = "delete from tblbillhd where strBillNo='" + voucherNo + "' "
		+ "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	String sqlInsert = "insert into tblbillhd(strBillNo,strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,"
		+ "dblDiscountAmt,dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		+ ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
		+ ",strTableNo,strWaiterNo,strCustomerCode,strManualBillNo,intShiftCode"
		+ ",intPaxNo,strDataPostFlag,strReasonCode,strRemarks,dblTipAmount,dteSettleDate"
		+ ",strCounterCode,dblDeliveryCharges,strAreaCode,strDiscountRemark,strTakeAwayRemarks"
		+ ",strDiscountOn,strCardNo,strTransactionType,dblRoundOff,dtBillDate,intOrderNo,strCRMRewardId"
		+ ",strKOTToBillNote,dblUSDConverionRate ) "
		+ "values('" + objBillHd.getStrBillNo() + "','" + objBillHd.getStrAdvBookingNo() + "'"
		+ ",'" + objBillHd.getDteBillDate() + "','" + objBillHd.getStrPOSCode() + "'"
		+ ",'" + objBillHd.getStrSettelmentMode() + "','" + gDecimalFormat.format(objBillHd.getDblDiscountAmt()) + "'"
		+ ",'" + gDecimalFormat.format(objBillHd.getDblDiscountPer()) + "','" + objBillHd.getDblTaxAmt() + "'"
		+ ",'" + objBillHd.getDblSubTotal() + "','" + objBillHd.getDblGrandTotal() + "'"
		+ ",'" + objBillHd.getStrTakeAway() + "','" + objBillHd.getStrOperationType() + "'"
		+ ",'" + objBillHd.getStrUserCreated() + "','" + objBillHd.getStrUserEdited() + "'"
		+ ",'" + objBillHd.getDteDateCreated() + "','" + objBillHd.getDteDateEdited() + "'"
		+ ",'" + objBillHd.getStrClientCode() + "','" + objBillHd.getStrTableNo() + "'"
		+ ",'" + objBillHd.getStrWaiterNo() + "','" + objBillHd.getStrCustomerCode() + "'"
		+ ",'" + objBillHd.getStrManualBillNo() + "'," + objBillHd.getIntShiftCode() + ""
		+ "," + objBillHd.getIntPaxNo() + ",'" + objBillHd.getStrDataPostFlag() + "','" + objBillHd.getStrReasonCode() + "'"
		+ ",'" + objUtility.funCheckSpecialCharacters(objBillHd.getStrRemarks()) + "'," + objBillHd.getDblTipAmount() + ",'" + objBillHd.getDteSettleDate() + "'"
		+ ",'" + objBillHd.getStrCounterCode() + "'," + objBillHd.getDblDeliveryCharges() + ""
		+ ", '" + objBillHd.getStrAreaCode() + "','" + objBillHd.getStrDiscountRemark() + "'"
		+ ",'" + objUtility.funCheckSpecialCharacters(objBillHd.getStrTakeAwayRemarks()) + "','" + objBillHd.getStrDiscountOn() + "'"
		+ ",'" + objBillHd.getStrCardNo() + "','" + objBillHd.getStrTransactionType() + "','" + objBillHd.getDblGrandTotalRoundOffBy() + "'"
		+ ",'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + objBillHd.getIntLastOrderNo() + "','" + objBillHd.getStrCRMRewardId() + "'"
		+ ",'" + objBillHd.getStrOnlineOrderNo() + "','" + objBillHd.getDblUSDConvertionRate() + "' )";

	int i = clsGlobalVarClass.dbMysql.execute(sqlInsert);

	objBillSettlementUtility.funCallIntegrationAPIsAfterBillPrint(objBillHd);

	return i;

    }

    private int funInsertBillDtlTable(List<clsBillDtl> listObjBillDtl) throws Exception
    {
	List<String> listBillItemDtl = new ArrayList<String>();

	String sqlDelete = "delete from tblbilldtl where strBillNo='" + voucherNo + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	String sqlInsertBillDtl = "insert into tblbilldtl "
		+ "(strItemCode,strItemName,strBillNo,strAdvBookingNo,dblRate"
		+ ",dblQuantity,dblAmount,dblTaxAmount,dteBillDate,strKOTNo"
		+ ",strClientCode,strCustomerCode,tmeOrderProcessing,strDataPostFlag"
		+ ",strMMSDataPostFlag,strManualKOTNo,tdhYN,strPromoCode,strCounterCode"
		+ ",strWaiterNo,dblDiscountAmt,dblDiscountPer,strSequenceNo,dtBillDate,tmeOrderPickup) "
		+ "values ";

	String insertBillPromoDtl = "insert into tblbillpromotiondtl "
		+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity,dblRate"
		+ ",strClientCode,strDataPostFlag,strPromoType,dblAmount,dblDiscountPer,dblDiscountAmt,dteBillDate) values ";
	boolean flgBillPromoDtl = false;

	Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
	if (hmComplimentaryBillItemDtl.size() > 0)
	{
	    hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
	    for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
	    {
		hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
	    }
	}

	for (clsBillDtl objBillDtl : listObjBillDtl)
	{
	    if (null != hmPromoItem)
	    {
		if (null != hmPromoItem.get(objBillDtl.getStrItemCode()))
		{
		    clsPromotionItems objPromoItemDtl = hmPromoItem.get(objBillDtl.getStrItemCode());
		    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
		    {
			double billItemQty = objBillDtl.getDblQuantity() - objPromoItemDtl.getFreeItemQty();
			double billItemAmt = objBillDtl.getDblRate() * billItemQty;
			objBillDtl.setDblAmount(billItemAmt);
		    }
		}
	    }

	    if (null != hmComplimentaryBillItemDtlTemp && hmComplimentaryBillItemDtlTemp.containsKey(objBillDtl.getStrItemCode()))
	    {
		double complQty = hmComplimentaryBillItemDtlTemp.get(objBillDtl.getStrItemCode()).getDblComplQty();
		if (complQty == objBillDtl.getDblQuantity() || complQty < objBillDtl.getDblQuantity())
		{
		    double amtToSave = objBillDtl.getDblAmount() - (hmComplimentaryBillItemDtlTemp.get(objBillDtl.getStrItemCode()).getDblComplQty() * hmComplimentaryBillItemDtlTemp.get(objBillDtl.getStrItemCode()).getDblRate());
		    objBillDtl.setDblAmount(amtToSave);
		    hmComplimentaryBillItemDtlTemp.remove(objBillDtl.getStrItemCode());
		}
		else if (objBillDtl.getDblQuantity() < complQty)
		{
		    double amtToSave = objBillDtl.getDblAmount() - (objBillDtl.getDblQuantity() * hmComplimentaryBillItemDtlTemp.get(objBillDtl.getStrItemCode()).getDblRate());
		    objBillDtl.setDblAmount(amtToSave);
		    double newComplQty = complQty - objBillDtl.getDblQuantity();
		    hmComplimentaryBillItemDtlTemp.get(objBillDtl.getStrItemCode()).setDblComplQty(newComplQty);
		}
	    }

	    if (hmComplimentaryBillItemDtl.containsKey(objBillDtl.getStrItemCode()))
	    {
		clsBillDtl objComplBillDtl = hmComplimentaryBillItemDtl.get(objBillDtl.getStrItemCode());
		objComplBillDtl.setDblRate(objBillDtl.getDblRate());
		objComplBillDtl.setStrBillNo(voucherNo);
		objComplBillDtl.setDteBillDate(objBillDtl.getDteBillDate());
		objComplBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objComplBillDtl.setStrManualKOTNo(objBillDtl.getStrManualKOTNo());
		objComplBillDtl.setStrPromoCode(objBillDtl.getStrPromoCode());
		objComplBillDtl.setStrCounterCode(objBillDtl.getStrCounterCode());
		objComplBillDtl.setStrWaiterNo(objBillDtl.getStrWaiterNo());
		objComplBillDtl.setDblDiscountAmt(objBillDtl.getDblDiscountAmt());
		objComplBillDtl.setDblDiscountPer(objBillDtl.getDblDiscountPer());
		objComplBillDtl.setDblTaxAmount(0.00);
		objComplBillDtl.setDteBillSettleDate(objBillDtl.getDteBillDate());
		hmComplimentaryBillItemDtl.put(objBillDtl.getStrItemCode(), objComplBillDtl);

		listBillItemDtl.add(objBillDtl.getStrItemCode());
	    }

	    sqlInsertBillDtl += "('" + objBillDtl.getStrItemCode() + "','" + objBillDtl.getStrItemName() + "'"
		    + ",'" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrAdvBookingNo() + "'," + objBillDtl.getDblRate() + ""
		    + ",'" + objBillDtl.getDblQuantity() + "','" + objBillDtl.getDblAmount() + "'"
		    + "," + objBillDtl.getDblTaxAmount() + ",'" + objBillDtl.getDteBillDate() + "'"
		    + ",'" + objBillDtl.getStrKOTNo() + "','" + objBillDtl.getStrClientCode() + "'"
		    + ",'" + objBillDtl.getStrCustomerCode() + "','" + objBillDtl.getTmeOrderProcessing() + "'"
		    + ",'" + objBillDtl.getStrDataPostFlag() + "','" + objBillDtl.getStrMMSDataPostFlag() + "'"
		    + ",'" + objBillDtl.getStrManualKOTNo() + "','" + objBillDtl.getTdhYN() + "'"
		    + ",'" + objBillDtl.getStrPromoCode() + "','" + objBillDtl.getStrCounterCode() + "'"
		    + ",'" + objBillDtl.getStrWaiterNo() + "','" + objBillDtl.getDblDiscountAmt() + "'"
		    + ",'" + objBillDtl.getDblDiscountPer() + "','" + objBillDtl.getSequenceNo() + "'"
		    + ",'" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + objBillDtl.getStrOrderPickupTime() + "'),";
	}

	for (clsBillDtl objBillDtl : listObjBillDtl)
	{
	    double freeQty = 0;
	    if (null != hmPromoItem)
	    {
		if (null != hmPromoItem.get(objBillDtl.getStrItemCode()))
		{
		    clsPromotionItems objPromoItemDtl = hmPromoItem.get(objBillDtl.getStrItemCode());
		    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
		    {
			freeQty = objPromoItemDtl.getFreeItemQty();
			double freeAmt = freeQty * objBillDtl.getDblRate();
			flgBillPromoDtl = true;
			insertBillPromoDtl += "('" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrItemCode() + "'"
				+ ",'" + objPromoItemDtl.getPromoCode() + "'," + freeQty + "," + objBillDtl.getDblRate() + ""
				+ ",'" + clsGlobalVarClass.gClientCode + "','N','" + objPromoItemDtl.getPromoType() + "'"
				+ ",'" + freeAmt + "',0,0,'" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
			hmPromoItem.remove(objBillDtl.getStrItemCode());
		    }
		    else if (objPromoItemDtl.getPromoType().equals("Discount"))
		    {
			if (objPromoItemDtl.getDiscType().equals("Value"))
			{
			    double amount = freeQty * objBillDtl.getDblRate();
			    double discAmt = objPromoItemDtl.getDiscAmt();
			    insertBillPromoDtl += "('" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrItemCode() + "'"
				    + ",'" + objPromoItemDtl.getPromoCode() + "',0," + objBillDtl.getDblRate() + ""
				    + ",'" + clsGlobalVarClass.gClientCode + "','N','" + objPromoItemDtl.getPromoType() + "'"
				    + ",'" + amount + "','" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
			    hmPromoItem.remove(objBillDtl.getStrItemCode());
			}
			else
			{
			    double totalAmt = objBillDtl.getDblQuantity() * objBillDtl.getDblRate();
			    double discAmt = totalAmt - (totalAmt * (objPromoItemDtl.getDiscPer() / 100));
			    insertBillPromoDtl += "('" + objBillDtl.getStrBillNo() + "','" + objBillDtl.getStrItemCode() + "'"
				    + ",'" + objPromoItemDtl.getPromoCode() + "',0," + objBillDtl.getDblRate() + ""
				    + ",'" + clsGlobalVarClass.gClientCode + "','N','" + objPromoItemDtl.getPromoType() + "'"
				    + ",'" + totalAmt + "','" + objPromoItemDtl.getDiscPer() + "','" + discAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
			    hmPromoItem.remove(objBillDtl.getStrItemCode());
			}
		    }
		}
	    }
	}

	StringBuilder sb = new StringBuilder(sqlInsertBillDtl);
	int index = sb.lastIndexOf(",");
	sqlInsertBillDtl = sb.delete(index, sb.length()).toString();
	//System.out.println(sqlInsertBillDtl);
	int ex = clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);

	if (flgBillPromoDtl)
	{
	    sb = new StringBuilder(insertBillPromoDtl);
	    int index1 = sb.lastIndexOf(",");
	    insertBillPromoDtl = sb.delete(index1, sb.length()).toString();
	    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
	}

	if (hmComplimentaryBillItemDtl.size() > 0)
	{
	    funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
	}

	return ex;
    }

    private int funInsertBillComplementryDtlTable(String billNo) throws Exception
    {
	String sqlDelete = "delete from tblbillcomplementrydtl where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	/*
         * String sqlInsertBillComDtl = "insert into tblbillcomplementrydtl " +
         * " select * from tblbilldtl where strBillNo='" + billNo + "' ";
	 */
	String sqlInsertBillComDtl = "insert into tblbillcomplementrydtl(strItemCode,strItemName,strBillNo"
		+ ",strAdvBookingNo,dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
		+ ",strClientCode,strCustomerCode,tmeOrderProcessing,strDataPostFlag,strMMSDataPostFlag"
		+ ",strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo,dblDiscountAmt,dblDiscountPer"
		+ ",strSequenceNo,dtBillDate,tmeOrderPickup)"
		+ " select * from tblbilldtl where strBillNo='" + billNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";

	int ex = clsGlobalVarClass.dbMysql.execute(sqlInsertBillComDtl);

	return ex;
    }

    private int funInsertBillModifierDtlTable(List<clsBillModifierDtl> listObjBillModDtl) throws Exception
    {
	String sqlDelete = "delete from tblbillmodifierdtl where strBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);
	String sqlInsertBillModDtl = "insert into  tblbillmodifierdtl "
		+ "(strBillNo,strItemCode,strModifierCode,strModifierName,dblRate"
		+ ",dblQuantity,dblAmount,strClientCode,strCustomerCode"
		+ ",strDataPostFlag,strMMSDataPostFlag,strDefaultModifierDeselectedYN,strSequenceNo"
		+ ",dblDiscPer,dblDiscAmt,dteBillDate )"
		+ " values ";
	for (clsBillModifierDtl objBillModDtl : listObjBillModDtl)
	{
	    sqlInsertBillModDtl += "('" + objBillModDtl.getStrBillNo() + "','" + objBillModDtl.getStrItemCode() + "'"
		    + ",'" + objBillModDtl.getStrModifierCode() + "','" + objBillModDtl.getStrModifierName() + "'"
		    + "," + objBillModDtl.getDblRate() + "," + objBillModDtl.getDblQuantity() + "," + objBillModDtl.getDblAmount() + ""
		    + ",'" + objBillModDtl.getStrClientCode() + "','" + objBillModDtl.getStrCustomerCode() + "'"
		    + ",'" + objBillModDtl.getStrDataPostFlag() + "','" + objBillModDtl.getStrMMSDataPostFlag() + "'"
		    + ",'" + objBillModDtl.getStrDefaultModifierDeselectedYN() + "','" + objBillModDtl.getSequenceNo() + "'"
		    + ",'" + objBillModDtl.getDblDiscPer() + "','" + objBillModDtl.getDblDiscAmt() + "','" + objBillModDtl.getDteBillDate() + "'),";
	}

	int ex = 0;
	if (listObjBillModDtl.size() > 0)
	{
	    StringBuilder sb = new StringBuilder(sqlInsertBillModDtl);
	    int index = sb.lastIndexOf(",");
	    sqlInsertBillModDtl = sb.delete(index, sb.length()).toString();
	    ex = clsGlobalVarClass.dbMysql.execute(sqlInsertBillModDtl);
	}
	return ex;
    }

    private int funInsertBillSettlementDtlTable(List<clsBillSettlementDtl> listObjBillSettlementDtl) throws Exception
    {
	String sqlDelete = "delete from tblbillsettlementdtl where strBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	String sqlInsertBillSettlementDtl = "insert into tblbillsettlementdtl"
		+ "(strBillNo,strSettlementCode,dblSettlementAmt,dblPaidAmt,strExpiryDate"
		+ ",strCardName,strRemark,strClientCode,strCustomerCode,dblActualAmt"
		+ ",dblRefundAmt,strGiftVoucherCode,strDataPostFlag,dteBillDate) "
		+ "values ";
	for (clsBillSettlementDtl objBillSettlementDtl : listObjBillSettlementDtl)
	{
	    sqlInsertBillSettlementDtl += "('" + objBillSettlementDtl.getStrBillNo() + "'"
		    + ",'" + objBillSettlementDtl.getStrSettlementCode() + "'," + objBillSettlementDtl.getDblSettlementAmt() + ""
		    + "," + objBillSettlementDtl.getDblPaidAmt() + ",'" + objBillSettlementDtl.getStrExpiryDate() + "'"
		    + ",'" + objBillSettlementDtl.getStrCardName() + "','" + objUtility.funCheckSpecialCharacters(objBillSettlementDtl.getStrRemark()) + "'"
		    + ",'" + objBillSettlementDtl.getStrClientCode() + "','" + objBillSettlementDtl.getStrCustomerCode() + "'"
		    + "," + objBillSettlementDtl.getDblActualAmt() + "," + objBillSettlementDtl.getDblRefundAmt() + ""
		    + ",'" + objBillSettlementDtl.getStrGiftVoucherCode() + "','" + objBillSettlementDtl.getStrDataPostFlag() + "'"
		    + ",'" + clsGlobalVarClass.getPOSDateForTransaction() + "'),";
	}
	StringBuilder sb1 = new StringBuilder(sqlInsertBillSettlementDtl);
	int index1 = sb1.lastIndexOf(",");
	sqlInsertBillSettlementDtl = sb1.delete(index1, sb1.length()).toString();
	return clsGlobalVarClass.dbMysql.execute(sqlInsertBillSettlementDtl);
    }

    public int funInsertBillTaxDtlTable(List<clsBillTaxDtl> listObjBillTaxDtl) throws Exception
    {
	int rows = 0;
	String sqlDelete = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "' and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	for (clsBillTaxDtl objBillTaxDtl : listObjBillTaxDtl)
	{
	    String sqlInsertTaxDtl = "insert into tblbilltaxdtl "
		    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate) "
		    + "values('" + objBillTaxDtl.getStrBillNo() + "','" + objBillTaxDtl.getStrTaxCode() + "'"
		    + "," + objBillTaxDtl.getDblTaxableAmount() + "," + objBillTaxDtl.getDblTaxAmount() + ""
		    + ",'" + clsGlobalVarClass.gClientCode + "','" + objBillTaxDtl.getDteBillDate() + "')";
	    rows += clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
	}
	return rows;
    }

    private void funSaveBillForDBWithOutSettle()
    {
	boolean flgSettle = false;
	try
	{
	    //Bill series code 
	    Map<String, List<clsBillItemDtl>> mapBillSeries = null;
	    listBillSeriesBillDtl = new ArrayList<>();
	    if (clsGlobalVarClass.gEnableBillSeries && (mapBillSeries = objBillSettlementUtility.funGetBillSeriesList()).size() > 0)
	    {
		if (mapBillSeries.containsKey("NoBillSeries"))
		{
		    new frmOkPopUp(null, "Please Create Bill Series", "Bill Series Error", 1).setVisible(true);
		    return;
		}
		Iterator<Map.Entry<String, List<clsBillItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
		while (billSeriesIt.hasNext())
		{
		    Map.Entry<String, List<clsBillItemDtl>> billSeriesEntry = billSeriesIt.next();
		    String key = billSeriesEntry.getKey();
		    List<clsBillItemDtl> values = billSeriesEntry.getValue();

		    funGenerateBillNoForBillSeriesForDirectBiller(key, values);
		}
		//save bill series bill detail
		for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
		{
		    clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
		    String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
		    double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();

		    String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl "
			    + "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag"
			    + ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) "
			    + "values ('" + clsGlobalVarClass.gPOSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'"
			    + ",'" + hdBillNo + "','" + objBillSettlementUtility.funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'"
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
		    clsBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
		    String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
		    objBillSettlementUtility.funSendBillToPrint(hdBillNo, objUtility.funGetOnlyPOSDateForTransaction());

		    if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 5") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 8") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 9") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 21")|| clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 22")
			    || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 11"))//XO
		    {
			break;
		    }
		}
	    }
	    else//if no bill series
	    {
		String strCustomerCode = "";

		if (null != clsGlobalVarClass.gCustomerCode)
		{
		    strCustomerCode = clsGlobalVarClass.gCustomerCode;
		}
		String operationType = "DirectBiller";
		String transactionType = "Direct Biller";//For saving different transaction on same Bill in tblBillHd table in database
		objBillSettlementUtility.funGenerateBillNo();

		//last order no
		int intLastOrderNo = objUtility2.funGetLastOrderNo();

		funSaveBillDiscountDetail(voucherNo);

		if (custCode != null)
		{
		    strCustomerCode = custCode;
		    if (homeDelivery.equals("Y"))
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

			if (delPersonCode != null)
			{
			    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode"
				    + ",strDPCode,dteDate,tmeTime,strPOSCode,strCustAddressLine1,strClientCode,dblHomeDeliCharge) "
				    + "values('" + voucherNo + "','" + custCode + "'"
				    + ",'" + delPersonCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
				    + ",'" + currentTime + "','" + clsGlobalVarClass.gPOSCode + "','" + custAddType + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "'," + _deliveryCharge + ")";
			    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);

			    String delBoys[] = delPersonCode.split(",");
			    if (delBoys.length > 0)
			    {
				String sqldeltemp = "Delete from tblhomedeldtl where strBillNo='" + voucherNo + "' ";
				clsGlobalVarClass.dbMysql.execute(sqldeltemp);
				String sqltblhomedeliveryDtl = "Insert into tblhomedeldtl(strBillNo,strDPCode,strClientCode,strDataPostFlag,dblDBIncentives,dteBillDate) "
					+ "  ";

				String sqlCustAreaCode = "select c.strBuildingCode "
					+ "from tblbillhd a "
					+ "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
					+ "left outer join tblbuildingmaster c on b.strBuldingCode=c.strBuildingCode "
					+ "where a.strBillNo='" + voucherNo + "'; ";
				ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustAreaCode);
				String buildingCode = "";
				if (rsCustCode.next())
				{
				    buildingCode = rsCustCode.getString(1);
				}

				for (int i = 0; i < delBoys.length; i++)
				{
				    String delBoyCode = delBoys[i];
				    String sqlDBIncenetives = "select d.strCustAreaCode,d.strDeliveryBoyCode,ifnull(d.dblValue,0.00) "
					    + "from tblareawisedelboywisecharges d "
					    + "where d.strCustAreaCode='" + buildingCode + "' "
					    + "and strDeliveryBoyCode='" + delBoyCode + "'; ";
				    ResultSet rsDBIncentives = clsGlobalVarClass.dbMysql.executeResultSet(sqlDBIncenetives);
				    String dbIncentives = "0.00";
				    if (rsDBIncentives.next())
				    {
					dbIncentives = rsDBIncentives.getString(3);
				    }

				    if (i == 0)
				    {
					sqltblhomedeliveryDtl += "values ('" + voucherNo + "','" + delBoyCode + "','" + clsGlobalVarClass.gClientCode + "','N','" + dbIncentives + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "') ";
				    }
				    else
				    {
					sqltblhomedeliveryDtl += ",('" + voucherNo + "','" + delBoyCode + "','" + clsGlobalVarClass.gClientCode + "','N','" + dbIncentives + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "') ";
				    }
				}
				System.out.println("homedeldtl->" + sqltblhomedeliveryDtl);
				if (sqltblhomedeliveryDtl.contains("values"))
				{
				    clsGlobalVarClass.dbMysql.execute(sqltblhomedeliveryDtl);
				}
			    }
			}
			else
			{
			    String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode,dteDate,tmeTime"
				    + ",strPOSCode,strCustAddressLine1,strCustAddressLine2,strCustAddressLine3,strCustAddressLine4"
				    + ",strCustCity,strClientCode,dblHomeDeliCharge)"
				    + " values('" + voucherNo + "','" + custCode + "','"
				    + clsGlobalVarClass.gPOSDateForTransaction + "','" + currentTime + "','"
				    + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','',''"
				    + ",'','','" + clsGlobalVarClass.gClientCode + "'," + _deliveryCharge + ")";
			    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
			}
		    }
		}

		if (clsGlobalVarClass.gTakeAway.equals("Yes"))
		{
		    operationType = "TakeAway";
		    transactionType = "Direct Biller" + "," + operationType;
		}
		if (takeAway.equals("Yes"))
		{
		    operationType = "TakeAway";
		    transactionType = "Direct Biller" + "," + operationType;
		}

		String counterCode = "NA";
		if (clsGlobalVarClass.gCounterWise.equals("Yes"))
		{
		    if (null != clsGlobalVarClass.gCounterCode)
		    {
			counterCode = clsGlobalVarClass.gCounterCode;
		    }
		}
		double homeDeliveryCharges = 0.00;
		if (txtDeliveryCharges.getText().trim().length() > 0)
		{
		    homeDeliveryCharges = Double.parseDouble(txtDeliveryCharges.getText().trim());
		}
		String waiterNo = "NA";
		if (advOrderBookingNo.trim().length() > 0)
		{
		    String sql_AdvOrderCustCode = "select strCustomerCode,ifnull(strWaiterNo,'NA') from tbladvbookbillhd "
			    + "where strAdvBookingNo='" + advOrderBookingNo + "'";
		    ResultSet rsAdvOrderCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql_AdvOrderCustCode);
		    if (rsAdvOrderCustomer.next())
		    {
			strCustomerCode = rsAdvOrderCustomer.getString(1);
			waiterNo = rsAdvOrderCustomer.getString(2);
		    }
		    rsAdvOrderCustomer.close();
		}

		//Insert into tblbillhd table
		clsBillHd objBillHd = new clsBillHd();
		objBillHd.setStrBillNo(voucherNo);
		objBillHd.setStrAdvBookingNo(advOrderBookingNo);
		objBillHd.setDteBillDate(objUtility.funGetPOSDateForTransaction());
		objBillHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
		objBillHd.setStrSettelmentMode("");
		objBillHd.setDblDiscountAmt(dblDiscountAmt);
		objBillHd.setDblDiscountPer(dblDiscountPer);
		objBillHd.setDblTaxAmt(dblTotalTaxAmt);
		objBillHd.setDblSubTotal(_subTotal);
		objBillHd.setDblGrandTotal(_grandTotal);
		objBillHd.setDblGrandTotalRoundOffBy(_grandTotalRoundOffBy);
		objBillHd.setStrTakeAway(takeAway);
		objBillHd.setStrOperationType(operationType);
		objBillHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
		objBillHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
		objBillHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
		objBillHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
		objBillHd.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillHd.setStrTableNo("");
		objBillHd.setStrWaiterNo(waiterNo);
		objBillHd.setStrCustomerCode(strCustomerCode);
		objBillHd.setStrManualBillNo(txtManualBillNo.getText());
		objBillHd.setIntShiftCode(clsGlobalVarClass.gShiftNo);
		objBillHd.setIntPaxNo(0);
		objBillHd.setStrDataPostFlag("N");
		objBillHd.setStrReasonCode(selectedReasonCode);
		objBillHd.setStrRemarks(objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()));
		objBillHd.setDblTipAmount(Double.parseDouble(txtTip.getText().trim()));
		objBillHd.setDteSettleDate(objUtility.funGetPOSDateForTransaction());
		objBillHd.setStrCounterCode(counterCode);
		objBillHd.setDblDeliveryCharges(_deliveryCharge);
		objBillHd.setStrAreaCode(areaCode);
		objBillHd.setStrDiscountRemark(discountRemarks);
		objBillHd.setStrTakeAwayRemarks(takeAwayRemarks);
		objBillHd.setStrTransactionType(transactionType);
		objBillHd.setIntLastOrderNo(intLastOrderNo);
		objBillHd.setStrCRMRewardId(rewardId);
		objBillHd.setStrOnlineOrderNo(onlineOrderNo);

		String discountOn = "All";
		if (rdbAll.isSelected())
		{
		    discountOn = "All";
		}
		if (rdbItemWise.isSelected())
		{
		    discountOn = "Item";
		}
		if (rdbGroupWise.isSelected())
		{
		    discountOn = "Group";
		}
		if (rdbSubGroupWise.isSelected())
		{
		    discountOn = "SubGroup";
		}
		objBillHd.setStrDiscountOn(discountOn);
		objBillHd.setStrCardNo(debitCardNo);
		objBillHd.setDblUSDConvertionRate(clsGlobalVarClass.gUSDConvertionRate);

		funInsertBillHdTable(objBillHd);

		if (isCheckOutPlayZone)
		{
		    funUpdateRegisterInOutPlayZone(voucherNo);
		}

		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    if (custCode.trim().length() > 0)
		    {
			String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + custCode + "' "
				+ "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
			clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

			String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
				+ ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
				+ "values('" + custCode + "','" + cmsMemberName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.gClientCode + "')";
			clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
		    }
		}

		// Insert into tblbilldtl table
		List<clsBillDtl> listObjBillDtl = new ArrayList<clsBillDtl>();

		for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
		{
		    if (!listclsItemRow.isIsModifier())
		    {
			double rate = 0.00;
			if (listclsItemRow.getQty() == 0)
			{
			    rate = listclsItemRow.getRate();
			}
			else
			{
			    rate = listclsItemRow.getAmt() / listclsItemRow.getQty();
			}

			clsBillDtl objBillDtl = new clsBillDtl();
			objBillDtl.setStrItemCode(listclsItemRow.getItemCode());
			objBillDtl.setStrItemName(listclsItemRow.getItemName());
			objBillDtl.setStrAdvBookingNo("");
			objBillDtl.setStrBillNo(voucherNo);
			objBillDtl.setDblRate(rate);
			objBillDtl.setDblQuantity(listclsItemRow.getQty());
			objBillDtl.setDblAmount(listclsItemRow.getAmt());
			objBillDtl.setDblTaxAmount(0);
			objBillDtl.setDteBillDate(objUtility.funGetPOSDateForTransaction());
			objBillDtl.setStrKOTNo("");
			objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillDtl.setStrCounterCode(strCustomerCode);
			objBillDtl.setTmeOrderProcessing("00:00:00");
			objBillDtl.setStrDataPostFlag("N");
			objBillDtl.setStrMMSDataPostFlag("N");
			objBillDtl.setStrManualKOTNo("");
			objBillDtl.setTdhYN(listclsItemRow.getTdhComboItemYN());
			objBillDtl.setStrPromoCode(listclsItemRow.getPromoCode());
			objBillDtl.setStrCounterCode(counterCode);
			objBillDtl.setStrWaiterNo(waiterNo);
			objBillDtl.setSequenceNo(listclsItemRow.getSeqNo());
			objBillDtl.setStrOrderPickupTime("00:00:00");

			clsBillItemDtl objBillItemDtl = hmBillItemDtl.get(listclsItemRow.getItemCode());
			objBillDtl.setDblDiscountAmt(objBillItemDtl.getDiscountAmount() * listclsItemRow.getQty());
			objBillDtl.setDblDiscountPer(objBillItemDtl.getDiscountPercentage());

			listObjBillDtl.add(objBillDtl);
		    }
		}
		funInsertBillDtlTable(listObjBillDtl);

		// Insert into tblbillmodifierdtl
		List<clsBillModifierDtl> listObjBillModBillDtls = new ArrayList<clsBillModifierDtl>();
		for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
		{
		    double rate = listclsItemRow.getAmt() / listclsItemRow.getQty();
		    double amt = 0.00;
		    boolean flgComplimentaryBill = false;
		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions obj : hmSettlemetnOptions.values())
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
			amt = listclsItemRow.getAmt();
		    }

		    if (listclsItemRow.isIsModifier())
		    {
			clsBillModifierDtl objBillModDtl = new clsBillModifierDtl();
			objBillModDtl.setStrBillNo(voucherNo);
			objBillModDtl.setStrItemCode(listclsItemRow.getItemCode());
			objBillModDtl.setStrModifierCode(listclsItemRow.getModifierCode());
			objBillModDtl.setStrModifierName(listclsItemRow.getItemName());
			objBillModDtl.setDblRate(rate);
			objBillModDtl.setDblQuantity(listclsItemRow.getQty());
			StringBuilder sbTemp = new StringBuilder(objBillModDtl.getStrItemCode());
			if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
			{
			    amt = 0;
			}
			objBillModDtl.setDblAmount(amt);
			objBillModDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillModDtl.setStrCustomerCode(strCustomerCode);
			objBillModDtl.setStrDataPostFlag("N");
			objBillModDtl.setStrMMSDataPostFlag("N");
			objBillModDtl.setStrDefaultModifierDeselectedYN(listclsItemRow.getStrDefaultModifierDeselectedYN());
			objBillModDtl.setSequenceNo(listclsItemRow.getSeqNo());
			objBillModDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			String key = listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName().toUpperCase();
			if (hmBillItemDtl.containsKey(key))
			{
			    clsBillItemDtl objBillItemDtl = hmBillItemDtl.get(key);
			    objBillModDtl.setDblDiscAmt(objBillItemDtl.getDiscountAmount() * listclsItemRow.getQty());
			    objBillModDtl.setDblDiscPer(objBillItemDtl.getDiscountPercentage());
			}
			else
			{
			    objBillModDtl.setDblDiscAmt(0);
			    objBillModDtl.setDblDiscPer(0);
			}
			listObjBillModBillDtls.add(objBillModDtl);
		    }
		}
		funInsertBillModifierDtlTable(listObjBillModBillDtls);

		// insert into tblbilltaxdtl    
		List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

		for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
		{
		    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		    clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		    objBillTaxDtl.setStrBillNo(voucherNo);
		    objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		    objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		    objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		    objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		    listObjBillTaxBillDtls.add(objBillTaxDtl);
		}

		funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
		clsUtility obj = new clsUtility();
		obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

		lblVoucherNo.setText(voucherNo);

		if (clsGlobalVarClass.gKOTPrintingEnableForDirectBiller)
		{
		    if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
		    {
			funTextFilePrintingKOTForDirectBiller();
		    }
		    else
		    {
			funTextFilePrintingKOTForDirectBiller();
		    }
		}
		objBillSettlementUtility.funSendBillToPrint(voucherNo, objUtility.funGetOnlyPOSDateForTransaction());

		if (clsGlobalVarClass.gHomeDelSMSYN)
		{
		    objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
		}

		clsGlobalVarClass.gDeliveryCharges = 0.00;
		if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		{
		    if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
		    {
			clsGlobalVarClass.funInvokeHOWebserviceForTrans("Sales", "Bill");
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-26", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	finally
	{
	    if (flgSettle)
	    {
		if (null != objDirectBiller)
		{
		    objDirectBiller = null;
		}
		System.gc();
	    }
	}
    }

    private double funGetFreeQuantity(String itemCode, double buyItemQty, double getItemQty, double totalItemQty)
    {
	double freeQty = 0;
	double totalSaleQty = buyItemQty + getItemQty;

	if (totalSaleQty > totalItemQty)
	{
	    if (getItemQty == totalItemQty)
	    {
		freeQty = getItemQty;
	    }
	}
	else
	{
	    int limit = (int) (totalItemQty / totalSaleQty);
	    for (int cnt = 0; cnt < limit; cnt++)
	    {
		freeQty++;
	    }
	}
	return freeQty;
    }

    private double funGetTaxPer(String taxOnTaxCode)
    {
	double serviceChargePer = 0.00;
	try
	{
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet("select a.dblPercent from tbltaxhd a where a.strTaxCode='" + taxOnTaxCode + "' ");
	    if (resultSet.next())
	    {
		serviceChargePer = resultSet.getDouble(1);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return serviceChargePer;
    }

    private void funGenerateBillNoForBillSeriesForMakeKOT(String billSeriesPrefix, List<clsBillItemDtl> listOfItemDtl)
    {
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
	    String billSeriesBillNo = billSeriesPrefix + "" + clsGlobalVarClass.gPOSCode + "" + String.format("%05d", (billSeriesLastNo + 1));

	    //update last bill series last no
	    int a = clsGlobalVarClass.dbMysql.execute("update tblbillseries "
		    + "set intLastNo='" + (billSeriesLastNo + 1) + "' "
		    + "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR strPOSCode='All') "
		    + "and strBillSeries='" + billSeriesPrefix + "' ");

	    //last order no
	    int intLastOrderNo = objUtility2.funGetLastOrderNo();

	    hmBillItemDtl.clear();
	    double subTotal = 0.00;
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    for (clsBillItemDtl obj : listOfItemDtl)
	    {
		if (obj.getItemCode().contains("M"))
		{
		    hmBillItemDtl.put(obj.getItemCode() + "!" + obj.getItemName(), obj);
		}
		else
		{
		    hmBillItemDtl.put(obj.getItemCode(), obj);
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
	    voucherNo = billSeriesBillNo;
	    revalidate();
	    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
	    {
		operationType = "TakeAway";
	    }
	    if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
	    {
		operationType = "TakeAway";
		clsGlobalVarClass.hmTakeAway.remove(tableNo);
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
		    + "from tblitemrtemp where strTableNo='" + tableNo + "' "
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
			    + " values('" + voucherNo + "','" + customerCode + "','" + delPersonCode + "','"
			    + objUtility.funGetPOSDateForTransaction() + "','" + currentTime + "','"
			    + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','','','','','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + _deliveryCharge + "')";
		    clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
		    clsGlobalVarClass.gCustomerCode = null;
		    clsGlobalVarClass.gDeliveryCharges = 0.00;
		    flgHomeDelPrint = true;
		}
	    }
	    if (null != custCode)
	    {
		customerCode = custCode;
	    }

	    double advanceAmount = 0.00;
	    double _deliveryCharge = 0.00;
	    dblTotalTaxAmt = 0;
	    _netAmount = 0.00;
	    _subTotal = 0.00;
	    dblDiscountAmt = 0.00;
	    dblDiscountPer = 0.00;
	    _grandTotal = 0.00;

	    double tempDiscAmt = 0, tempDiscPer = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
	    {
		clsBillItemDtl objBillItemDtl = entry.getValue();
		tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
		tempDiscPer = objBillItemDtl.getDiscountPercentage();
	    }
	    if (subTotal > 0)
	    {
		tempDiscPer = (tempDiscAmt * 100) / subTotal;
	    }
	    _subTotal = subTotal;

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationType, subTotal, tempDiscAmt, "", settlementCode, "Sales");
	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
		{
		    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
		}
	    }

	    //save bill disc dtl
	    funSaveBillDiscDtlForBillSeries(listOfItemDtl);

	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal - advanceAmount;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
	    if (hmComplimentaryBillItemDtl.size() > 0)
	    {
		hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
		for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
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
		    + " and strTableNo='" + tableNo + "' and strNCKotYN='N' "
		    + " and strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
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
		custCode = rsItemKOTDTL.getString(8);
		custName = rsItemKOTDTL.getString(9);
		String promoCode = rsItemKOTDTL.getString(12);
		cardNo = rsItemKOTDTL.getString(14);
		String sqlInsertBillDtl = "";

		if (!iCode.contains("M"))
		{
		    if (hmPromoItem.size() > 0)
		    {
			if (null != hmPromoItem.get(iCode))
			{
			    clsPromotionItems objPromoItemDtl = hmPromoItem.get(iCode);
			    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
			    {
				double freeQty = objPromoItemDtl.getFreeItemQty();
				double freeAmt = freeQty * rate;

				promoCode = objPromoItemDtl.getPromoCode();
				String insertBillPromoDtl = "insert into tblbillpromotiondtl "
					+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					+ ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					+ "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "','" + kotDateTime + "')";
				clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				hmPromoItem.remove(iCode);
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
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
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
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
			    }
			}
		    }

		    String amt = "0.00";
		    boolean flgComplimentaryBill = false;
		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions obj : hmSettlemetnOptions.values())
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
			discAmt = hmBillItemDtl.get(iCode).getDiscountAmount() * iQty;
			discPer = hmBillItemDtl.get(iCode).getDiscountPercentage();
		    }

		    if (iQty > 0)
		    {
			if (iName.startsWith("=>"))
			{
			    sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
				    + ",strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo"
				    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
				    + "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ",'" + iQty
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
				    + "values('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ""
				    + ",'" + iQty + "','" + amt + "','0.00','" + kotDateTime + "','" + kot + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','" + promoCode + "'"
				    + ",'" + rsItemKOTDTL.getString(10) + "','" + rsItemKOTDTL.getString(11) + "'"
				    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "','" + orderPickupTime + "')";
			}
			clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
			if (hmComplimentaryBillItemDtl.containsKey(iCode))
			{
			    clsBillDtl objBillDtl = hmComplimentaryBillItemDtl.get(iCode);
			    objBillDtl.setDblRate(rate);
			    objBillDtl.setStrBillNo(voucherNo);
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
			    hmComplimentaryBillItemDtl.put(iCode, objBillDtl);

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
		    if (hmBillItemDtl.containsKey(iCode + "!" + iName))
		    {
			modDiscAmt = hmBillItemDtl.get(iCode + "!" + iName).getDiscountAmount() * iQty;
			modDiscPer = hmBillItemDtl.get(iCode + "!" + iName).getDiscountPercentage();
		    }

		    StringBuilder sbTemp = new StringBuilder(iCode);
		    if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
		    {
			amt = 0;
		    }
		    String sqlBillModifierDtl = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
			    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
			    + "values('" + voucherNo + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
			    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + modDiscPer + "','" + modDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
		    //System.out.println(sqlBillModifierDtl);
		    clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
		}
	    }
	    rsItemKOTDTL.close();

	    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
		    + "from tblbillpromotiondtl "
		    + " where strBillNo='" + voucherNo + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + " and strPromoType='ItemWise' ";
	    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
	    while (rsBillPromo.next())
	    {
		double freeQty = rsBillPromo.getDouble(1);
		String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
			+ " from tblbilldtl "
			+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
			+ " and strBillNo='" + voucherNo + "'";
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

	    if (hmComplimentaryBillItemDtl.size() > 0)
	    {
		funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
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
		    + ",intBillSeriesPaxNo,dtBillDate,intOrderNo,strCRMRewardId,strManualBillNo,dblUSDConverionRate ) "
		    + "values('" + voucherNo + "','" + advOrderBookingNo + "','" + objUtility.funGetPOSDateForTransaction() + "','"
		    + clsGlobalVarClass.gPOSCode + "','','" + dblDiscountAmt + "','"
		    + dblDiscountPer + "','" + dblTotalTaxAmt + "','" + _subTotal + "','"
		    + _grandTotal + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
		    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.gClientCode + "','" + tableNo + "','" + waiterNo + "'"
		    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
		    + "," + paxNo + ",'" + selectedReasonCode + "','" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "'"
		    + "," + txtTip.getText() + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
		    + ",'" + counterCode + "'," + _deliveryCharge + ",'" + areaCode + "'"
		    + ",'" + discountRemarks + "','','','" + cardNo + "','" + clsGlobalVarClass.gTransactionType + "'"
		    + ",'" + _grandTotalRoundOffBy + "','" + intBillSeriesPaxNo + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",'" + intLastOrderNo + "','" + rewardId + "','" + txtManualBillNo.getText().trim() + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlInsertBillHd);

	    clsBillHd objBillHd = new clsBillHd();
	    objBillHd.setStrBillNo(voucherNo);
	    objBillHd.setDblGrandTotal(_grandTotal);
	    objBillHd.setStrCustomerCode(customerCode);
	    objBillHd.setStrOnlineOrderNo(onlineOrderNo);
	    objBillSettlementUtility.funCallIntegrationAPIsAfterBillPrint(objBillHd);

	    /**
	     * update KOT to bill note
	     */
	    objBillSettlementUtility.funUpdateKOTToBillNote(clsGlobalVarClass.gPOSCode, tableNo, voucherNo);

	    clsBillSeriesBillDtl objBillSeriesBillDtl = new clsBillSeriesBillDtl();
	    objBillSeriesBillDtl.setStrHdBillNo(voucherNo);
	    objBillSeriesBillDtl.setStrBillSeries(billSeriesPrefix);
	    objBillSeriesBillDtl.setDblGrandTotal(_grandTotal);
	    objBillSeriesBillDtl.setFlgHomeDelPrint(flgHomeDelPrint);
	    listBillSeriesBillDtl.add(objBillSeriesBillDtl);

	    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

	    // insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(voucherNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (custCode.trim().length() > 0)
		{
		    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + custCode + "' "
			    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

		    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
			    + "values('" + custCode + "','" + custName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "')";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
		}
	    }

	    dispose();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funInsertComplimentaryItemsInBillDtl(List<String> listBillItems) throws Exception
    {
	for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
	{
	    if (listBillItems.contains(entry.getValue().getStrItemCode()))
	    {
		String sqlInsertBillDtl = "insert into tblbillcomplementrydtl(strItemCode,strItemName,strBillNo"
			+ ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
			+ ",strClientCode,strManualKOTNo,strPromoCode,strCounterCode,strWaiterNo"
			+ ",dblDiscountAmt,dblDiscountPer,strType,dtBillDate) "
			+ "values('" + entry.getValue().getStrItemCode() + "','" + entry.getValue().getStrItemName() + "'"
			+ ",'" + voucherNo + "'," + entry.getValue().getDblRate() + ",'" + entry.getValue().getDblQuantity() + "'"
			+ ",'" + entry.getValue().getDblAmount() + "','0.00','" + entry.getValue().getDteBillDate() + "'"
			+ ",'" + entry.getValue().getStrKOTNo() + "','" + clsGlobalVarClass.gClientCode + "','" + entry.getValue().getStrManualKOTNo() + "'"
			+ ",'" + entry.getValue().getStrPromoCode() + "','" + entry.getValue().getStrCounterCode() + "'"
			+ ",'" + entry.getValue().getStrWaiterNo() + "','" + entry.getValue().getDblDiscountAmt() + "'"
			+ ",'" + entry.getValue().getDblDiscountPer() + "','ItemComplimentary','" + entry.getValue().getDteBillDate() + "')";
		clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
	    }
	}

	listBillItems = null;
    }

    public void funSaveBillDiscDtlForBillSeries(List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{
	    StringBuilder sqlBillDiscDtl = new StringBuilder();

	    Iterator<Map.Entry<String, clsBillDiscountDtl>> itDiscEntry = mapBillDiscDtl.entrySet().iterator();
	    if (itDiscEntry.hasNext())
	    {
		Map.Entry<String, clsBillDiscountDtl> discEntry = itDiscEntry.next();
		String key = discEntry.getKey();
		String discOnType = key.split("!")[0];
		String discOnValue = key.split("!")[1];

		if (discOnType.equalsIgnoreCase("Total"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemName,a.dblAmount "
			    + " from tblitemrtemp a "
			    + " left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			    + " where  b.strDiscountApply='Y' and a.strTableNo='" + tableNo + "' "
			    + " and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " ");
		    ResultSet rsDiscOnItemWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    if (rsDiscOnItemWise.next())
		    {
			clsBillDiscountDtl objBillDiscDtl = mapBillDiscDtl.get(key);
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();

			double tempDiscAmt = 0, tempDiscPer = 0;
			for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
			{
			    clsBillItemDtl objBillItemDtl = entry.getValue();
			    tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
			    tempDiscPer = objBillItemDtl.getDiscountPercentage();
			}
			if (_subTotal > 0)
			{
			    tempDiscPer = (tempDiscAmt * 100) / _subTotal;
			}
			dblDiscountAmt = tempDiscAmt;
			dblDiscountPer = tempDiscPer;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + tempDiscAmt + "','" + tempDiscPer + "','" + _subTotal + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			//save total disc for bill series
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		    rsDiscOnItemWise.close();
		}
		else if (discOnType.equalsIgnoreCase("ItemWise"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemName,a.dblAmount "
			    + " from tblitemrtemp a "
			    + " left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			    + " where  b.strDiscountApply='Y' and a.strTableNo='" + tableNo + "' "
			    + " and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
			    + " and a.strItemName = '" + discOnValue + "' ");
		    ResultSet rsDiscOnItemWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    if (rsDiscOnItemWise.next())
		    {
			itDiscEntry = mapBillDiscDtl.entrySet().iterator();
			double totalDiscAmt = 0.00, finalDiscPer = 0.00;
			for (int i = 0; itDiscEntry.hasNext(); i++)
			{
			    discEntry = itDiscEntry.next();
			    key = discEntry.getKey();
			    clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			    discOnType = key.split("!")[0];
			    discOnValue = key.split("!")[1];
			    String remark = objBillDiscDtl.getRemark();
			    String reason = objBillDiscDtl.getReason();

			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + objBillDiscDtl.getDiscAmt() + "','" + objBillDiscDtl.getDiscPer() + "','" + objBillDiscDtl.getDiscOnAmt() + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			    totalDiscAmt += objBillDiscDtl.getDiscAmt();
			}

			if (_subTotal == 0.00)
			{
			}
			else
			{
			    finalDiscPer = (totalDiscAmt / _subTotal) * 100;
			}
			dblDiscountAmt = totalDiscAmt;
			dblDiscountPer = finalDiscPer;
		    }
		    rsDiscOnItemWise.close();
		}
		else if (discOnType.equalsIgnoreCase("GroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("select a.strItemCode,a.dblAmount,d.strGroupName "
				+ "from tblitemrtemp a "
				+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
				+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
				+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
				+ "where  b.strDiscountApply='Y' "
				+ "and a.strTableNo='" + tableNo + "'"
				+ "and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
				+ "and d.strGroupName='" + discOnValue + "' ");
			ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			while (rsGroupWiseDisc.next())
			{
			    discOnAmt += rsGroupWiseDisc.getDouble("dblAmount");
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;
			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {
		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
		}
		else if (discOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("select a.strItemCode,a.dblAmount,c.strSubGroupName "
				+ "from tblitemrtemp a "
				+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
				+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
				+ "where  b.strDiscountApply='Y' "
				+ "and a.strTableNo='" + tableNo + "'"
				+ "and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
				+ "and c.strSubGroupName='" + discOnValue + "' ");

			ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			while (rsGroupWiseDisc.next())
			{
			    discOnAmt += rsGroupWiseDisc.getDouble("dblAmount");
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;

			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {
		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
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


    private void funGenerateBillNoForBillSeriesForDirectBiller(String billSeriesPrefix, List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

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
	    String billSeriesBillNo = billSeriesPrefix + "" + clsGlobalVarClass.gPOSCode + "" + String.format("%05d", (billSeriesLastNo + 1));

	    //update last bill series last no
	    int a = clsGlobalVarClass.dbMysql.execute("update tblbillseries "
		    + "set intLastNo='" + (billSeriesLastNo + 1) + "' "
		    + "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' OR strPOSCode='All') "
		    + "and strBillSeries='" + billSeriesPrefix + "' ");
	    //last order no
	    int intLastOrderNo = objUtility2.funGetLastOrderNo();

	    hmBillItemDtl.clear();
	    double subTotal = 0.00;
	    for (clsBillItemDtl obj : listOfItemDtl)
	    {
		if (obj.getItemCode().contains("M"))
		{
		    hmBillItemDtl.put(obj.getItemCode() + "!" + obj.getItemName(), obj);
		}
		else
		{
		    hmBillItemDtl.put(obj.getItemCode(), obj);
		}
		subTotal += obj.getAmount();

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();
		objItemDtlForTax.setItemCode(obj.getItemCode());
		objItemDtlForTax.setItemName(obj.getItemName());
		objItemDtlForTax.setAmount(obj.getAmount());
		objItemDtlForTax.setDiscAmt(obj.getDiscountAmount() * obj.getQuantity());
		arrListItemDtls.add(objItemDtlForTax);
	    }
	    String strCustomerCode = "";

	    if (null != clsGlobalVarClass.gCustomerCode)
	    {
		strCustomerCode = clsGlobalVarClass.gCustomerCode;
	    }
	    String operationType = "DirectBiller";

	    voucherNo = billSeriesBillNo;

	    if (custCode != null)
	    {
		strCustomerCode = custCode;
		if (homeDelivery.equals("Y"))
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

		    if (delPersonCode != null)
		    {
			String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode"
				+ ",strDPCode,dteDate,tmeTime,strPOSCode,strCustAddressLine1,strClientCode,dblHomeDeliCharge) "
				+ "values('" + voucherNo + "','" + custCode + "'"
				+ ",'" + delPersonCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "'"
				+ ",'" + currentTime + "','" + clsGlobalVarClass.gPOSCode + "','" + custAddType + "'"
				+ ",'" + clsGlobalVarClass.gClientCode + "'," + _deliveryCharge + ")";
			clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
		    }
		    else
		    {
			String sql_tblhomedelivery = "insert into tblhomedelivery(strBillNo,strCustomerCode,dteDate,tmeTime"
				+ ",strPOSCode,strCustAddressLine1,strCustAddressLine2,strCustAddressLine3,strCustAddressLine4"
				+ ",strCustCity,strClientCode,dblHomeDeliCharge)"
				+ " values('" + voucherNo + "','" + custCode + "','"
				+ clsGlobalVarClass.gPOSDateForTransaction + "','" + currentTime + "','"
				+ clsGlobalVarClass.gPOSCode + "','" + custAddType + "','',''"
				+ ",'','','" + clsGlobalVarClass.gClientCode + "'," + _deliveryCharge + ")";
			clsGlobalVarClass.dbMysql.execute(sql_tblhomedelivery);
		    }
		}
	    }

	    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
	    {
		operationType = "TakeAway";
	    }
	    if (takeAway.equals("Yes"))
	    {
		operationType = "TakeAway";
	    }

	    String counterCode = "NA";
	    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
	    {
		if (null != clsGlobalVarClass.gCounterCode)
		{
		    counterCode = clsGlobalVarClass.gCounterCode;
		}
	    }
	    double homeDeliveryCharges = 0.00;
	    if (txtDeliveryCharges.getText().trim().length() > 0)
	    {
		homeDeliveryCharges = Double.parseDouble(txtDeliveryCharges.getText().trim());
	    }
	    String waiterNo = "NA";
	    if (advOrderBookingNo.trim().length() > 0)
	    {
		String sql_AdvOrderCustCode = "select strCustomerCode,ifnull(strWaiterNo,'NA') from tbladvbookbillhd "
			+ "where strAdvBookingNo='" + advOrderBookingNo + "'";
		ResultSet rsAdvOrderCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql_AdvOrderCustCode);
		if (rsAdvOrderCustomer.next())
		{
		    strCustomerCode = rsAdvOrderCustomer.getString(1);
		    waiterNo = rsAdvOrderCustomer.getString(2);
		}
		rsAdvOrderCustomer.close();
	    }

	    //calculate Tax for bill series wise
	    //funCalculateTaxForBillSeriesWise("direct", listOfItemDtl);
	    double advanceAmount = 0.00;
	    double _deliveryCharge = homeDeliveryCharges;
	    dblTotalTaxAmt = 0;
	    _netAmount = 0.00;
	    _subTotal = 0.00;
	    dblDiscountAmt = 0.00;
	    dblDiscountPer = 0.00;
	    _grandTotal = 0.00;

	    double tempDiscAmt = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
	    {
		clsBillItemDtl objBillItemDtl = entry.getValue();
		tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
	    }
	    _subTotal = subTotal;

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationTypeForTax, _subTotal, tempDiscAmt, "", settlementCode, "Sales");

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
		{
		    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
		}
	    }

	    //save bill disc dtl            
	    funSaveBillDiscDtlForBillSeriesForDirectBiller(listOfItemDtl);

	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal - advanceAmount;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    settleName = "";
	    ////////
	    if (strButtonClicked.equalsIgnoreCase("Settle"))
	    {
		int row = 0;
		boolean isBillSettled = false;
		List<clsBillSettlementDtl> listObjBillSettlementDtl = new ArrayList<clsBillSettlementDtl>();
		double billGrandTotalAmt = _grandTotal;
		for (clsSettelementOptions ob : hmSettlemetnOptions.values())
		{
		    if (ob.getDblPaidAmt() < 1)
		    {
			continue;
		    }

		    settleName = ob.getStrSettelmentDesc();
		    double settleAmt = 0;
		    if (billGrandTotalAmt > ob.getDblPaidAmt())
		    {
			settleAmt = ob.getDblPaidAmt();
			ob.setDblPaidAmt(0.00);
		    }
		    else
		    {
			settleAmt = billGrandTotalAmt;
			ob.setDblPaidAmt(ob.getDblPaidAmt() - settleAmt);
			isBillSettled = true;
		    }
		    billGrandTotalAmt = billGrandTotalAmt - settleAmt;

		    if (ob.getStrSettelmentType().equals("Debit Card"))
		    {
			objUtility.funDebitCardTransaction(voucherNo, debitCardNo, settleAmt, "Settle");
			objUtility.funUpdateDebitCardBalance(debitCardNo, settleAmt, "Settle");
		    }

		    clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl();
		    objBillSettlementDtl.setStrBillNo(voucherNo);
		    objBillSettlementDtl.setStrSettlementCode(ob.getStrSettelmentCode());
		    objBillSettlementDtl.setDblSettlementAmt(settleAmt);
		    if (billGrandTotalAmt == 0)
		    {
			objBillSettlementDtl.setDblPaidAmt(settleAmt);
			objBillSettlementDtl.setDblActualAmt(settleAmt);
		    }
		    else
		    {
			objBillSettlementDtl.setDblPaidAmt(settleAmt);
			objBillSettlementDtl.setDblActualAmt(settleAmt);
		    }

		    objBillSettlementDtl.setStrExpiryDate("");
		    objBillSettlementDtl.setStrCardName(ob.getStrCardName());
		    objBillSettlementDtl.setStrRemark(ob.getStrRemark());
		    objBillSettlementDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objBillSettlementDtl.setStrCustomerCode(customerCodeForCredit);
		    objBillSettlementDtl.setDblRefundAmt(0);
		    objBillSettlementDtl.setStrGiftVoucherCode(ob.getStrGiftVoucherCode());
		    objBillSettlementDtl.setStrDataPostFlag("N");
		    listObjBillSettlementDtl.add(objBillSettlementDtl);

		    row++;

		    if (isBillSettled)
		    {
			break;
		    }
		}
		funInsertBillSettlementDtlTable(listObjBillSettlementDtl);
		funTruncateDebitCardTempTable();

		if (row > 1)
		{
		    settleName = "MultiSettle";
		}
	    }

	    if (clsGlobalVarClass.gClientCode.equals("190.001") && billSeriesPrefix.equalsIgnoreCase("L") && strCustomerCode.trim().isEmpty())
	    {
		strCustomerCode = objUtility2.funAutoCustomerSelectionForLiquorBill();
	    }

	    //Insert into tblbillhd table
	    clsBillHd objBillHd = new clsBillHd();
	    objBillHd.setStrBillNo(voucherNo);
	    objBillHd.setStrAdvBookingNo(advOrderBookingNo);
	    objBillHd.setDteBillDate(objUtility.funGetPOSDateForTransaction());
	    objBillHd.setStrPOSCode(clsGlobalVarClass.gPOSCode);
	    objBillHd.setStrSettelmentMode(settleName);
	    objBillHd.setDblDiscountAmt(dblDiscountAmt);
	    objBillHd.setDblDiscountPer(dblDiscountPer);
	    objBillHd.setDblTaxAmt(dblTotalTaxAmt);
	    objBillHd.setDblSubTotal(_subTotal);
	    objBillHd.setDblGrandTotal(_grandTotal);
	    objBillHd.setDblGrandTotalRoundOffBy(_grandTotalRoundOffBy);
	    objBillHd.setStrTakeAway(takeAway);
	    objBillHd.setStrOperationType(operationType);
	    objBillHd.setStrTransactionType(clsGlobalVarClass.gTransactionType);
	    objBillHd.setStrUserCreated(clsGlobalVarClass.gUserCode);
	    objBillHd.setStrUserEdited(clsGlobalVarClass.gUserCode);
	    objBillHd.setDteDateCreated(clsGlobalVarClass.getCurrentDateTime());
	    objBillHd.setDteDateEdited(clsGlobalVarClass.getCurrentDateTime());
	    objBillHd.setStrClientCode(clsGlobalVarClass.gClientCode);
	    objBillHd.setStrTableNo("");
	    objBillHd.setStrWaiterNo(waiterNo);
	    objBillHd.setStrCustomerCode(strCustomerCode);
	    objBillHd.setStrManualBillNo(txtManualBillNo.getText());
	    objBillHd.setIntShiftCode(clsGlobalVarClass.gShiftNo);
	    objBillHd.setIntPaxNo(0);
	    objBillHd.setStrDataPostFlag("N");
	    objBillHd.setStrReasonCode(selectedReasonCode);
	    objBillHd.setStrRemarks(txtAreaRemark.getText().trim());
	    objBillHd.setDblTipAmount(Double.parseDouble(txtTip.getText().trim()));
	    objBillHd.setDteSettleDate(objUtility.funGetPOSDateForTransaction());
	    objBillHd.setStrCounterCode(counterCode);
	    objBillHd.setDblDeliveryCharges(_deliveryCharge);
	    objBillHd.setStrAreaCode(areaCode);
	    objBillHd.setStrDiscountRemark(discountRemarks);
	    objBillHd.setStrTakeAwayRemarks(takeAwayRemarks);
	    objBillHd.setIntLastOrderNo(intLastOrderNo);
	    objBillHd.setStrOnlineOrderNo(onlineOrderNo);

	    String discountOn = "All";
	    if (rdbAll.isSelected())
	    {
		discountOn = "All";
	    }
	    if (rdbItemWise.isSelected())
	    {
		discountOn = "Item";
	    }
	    if (rdbGroupWise.isSelected())
	    {
		discountOn = "Group";
	    }
	    if (rdbSubGroupWise.isSelected())
	    {
		discountOn = "SubGroup";
	    }
	    objBillHd.setStrDiscountOn(discountOn);
	    objBillHd.setStrCardNo(debitCardNo);
	    objBillHd.setStrKOTToBillNote(onlineOrderNo);
	    objBillHd.setDblUSDConvertionRate(clsGlobalVarClass.gUSDConvertionRate);

	    funInsertBillHdTable(objBillHd);

	    if (isCheckOutPlayZone)
	    {
		funUpdateRegisterInOutPlayZone(voucherNo);
	    }

	    clsBillSeriesBillDtl objBillSeriesBillDtl = new clsBillSeriesBillDtl();
	    objBillSeriesBillDtl.setStrHdBillNo(voucherNo);
	    objBillSeriesBillDtl.setStrBillSeries(billSeriesPrefix);
	    objBillSeriesBillDtl.setDblGrandTotal(_grandTotal);
	    listBillSeriesBillDtl.add(objBillSeriesBillDtl);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (custCode.trim().length() > 0)
		{
		    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + custCode + "' "
			    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

		    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
			    + "values('" + custCode + "','" + cmsMemberName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "')";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
		}
	    }

	    // Insert into tblbilldtl table
	    List<clsBillDtl> listObjBillDtl = new ArrayList<clsBillDtl>();

	    for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
	    {
		String key = (listclsItemRow.isIsModifier() ? listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName() : listclsItemRow.getItemCode());

		if (hmBillItemDtl.containsKey(key))
		{
		    if (!listclsItemRow.isIsModifier())
		    {
			double rate = 0.00;
			if (listclsItemRow.getQty() == 0)
			{
			    rate = listclsItemRow.getRate();
			}
			else
			{
			    rate = listclsItemRow.getAmt() / listclsItemRow.getQty();
			}

			clsBillDtl objBillDtl = new clsBillDtl();
			objBillDtl.setStrItemCode(listclsItemRow.getItemCode());
			objBillDtl.setStrItemName(listclsItemRow.getItemName());
			objBillDtl.setStrAdvBookingNo("");
			objBillDtl.setStrBillNo(voucherNo);
			objBillDtl.setDblRate(rate);
			objBillDtl.setDblQuantity(listclsItemRow.getQty());
			objBillDtl.setDblAmount(listclsItemRow.getAmt());
			objBillDtl.setDblTaxAmount(0);
			objBillDtl.setDteBillDate(objUtility.funGetPOSDateForTransaction());
			objBillDtl.setStrKOTNo("");
			objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillDtl.setStrCounterCode(strCustomerCode);
			objBillDtl.setTmeOrderProcessing("00:00:00");
			objBillDtl.setStrDataPostFlag("N");
			objBillDtl.setStrMMSDataPostFlag("N");
			objBillDtl.setStrManualKOTNo("");
			objBillDtl.setTdhYN(listclsItemRow.getTdhComboItemYN());
			objBillDtl.setStrPromoCode(listclsItemRow.getPromoCode());
			objBillDtl.setStrCounterCode(counterCode);
			objBillDtl.setStrWaiterNo(waiterNo);
			objBillDtl.setSequenceNo(listclsItemRow.getSeqNo());
			objBillDtl.setStrOrderPickupTime("00:00:00");

			clsBillItemDtl objBillItemDtl = hmBillItemDtl.get(listclsItemRow.getItemCode());
			objBillDtl.setDblDiscountAmt(objBillItemDtl.getDiscountAmount() * listclsItemRow.getQty());
			objBillDtl.setDblDiscountPer(objBillItemDtl.getDiscountPercentage());

			listObjBillDtl.add(objBillDtl);
		    }
		}
	    }
	    funInsertBillDtlTable(listObjBillDtl);

	    // Insert into tblbillmodifierdtl
	    String itemModCode="",seqNo="0";
	    List<clsBillModifierDtl> listObjBillModBillDtls = new ArrayList<clsBillModifierDtl>();
	    for (clsDirectBillerItemDtl listclsItemRow : objListDirectBillerItemDtl)
	    {
		String key = (listclsItemRow.isIsModifier() ? listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName().toUpperCase() : listclsItemRow.getItemCode());

		if (hmBillItemDtl.containsKey(key))
		{
		    if(key.contains(itemModCode)&&itemModCode.length()>0&&key.contains("-->"))
		    {
			if(!seqNo.contains("."))
			seqNo=seqNo.concat(".01");
		    }else{
			seqNo=listclsItemRow.getSeqNo();
			itemModCode=key;
		    }
		    double rate = listclsItemRow.getAmt() / listclsItemRow.getQty();
		    double amt = 0.00;
		    boolean flgComplimentaryBill = false;
		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions obj : hmSettlemetnOptions.values())
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
			amt = listclsItemRow.getAmt();
		    }

		    if (listclsItemRow.isIsModifier())
		    {
			clsBillModifierDtl objBillModDtl = new clsBillModifierDtl();
			objBillModDtl.setStrBillNo(voucherNo);
			objBillModDtl.setStrItemCode(listclsItemRow.getItemCode());
			objBillModDtl.setStrModifierCode(listclsItemRow.getModifierCode());
			objBillModDtl.setStrModifierName(listclsItemRow.getItemName());
			objBillModDtl.setDblRate(rate);
			objBillModDtl.setDblQuantity(listclsItemRow.getQty());
			StringBuilder sbTemp = new StringBuilder(objBillModDtl.getStrItemCode());
			if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
			{
			    amt = 0;
			}
			objBillModDtl.setDblAmount(amt);
			objBillModDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			objBillModDtl.setStrCustomerCode(strCustomerCode);
			objBillModDtl.setStrDataPostFlag("N");
			objBillModDtl.setStrMMSDataPostFlag("N");
			objBillModDtl.setStrDefaultModifierDeselectedYN(listclsItemRow.getStrDefaultModifierDeselectedYN());
			objBillModDtl.setSequenceNo(seqNo);
			clsBillItemDtl objBillItemDtl = hmBillItemDtl.get(listclsItemRow.getItemCode() + "!" + listclsItemRow.getItemName().toUpperCase());
			objBillModDtl.setDblDiscAmt(objBillItemDtl.getDiscountAmount() * listclsItemRow.getQty());
			objBillModDtl.setDblDiscPer(objBillItemDtl.getDiscountPercentage());
			objBillModDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

			listObjBillModBillDtls.add(objBillModDtl);
		    }
		}
		
	    }
	    funInsertBillModifierDtlTable(listObjBillModBillDtls);

	    // insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();
	    //double totalTaxAmt = 0;

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		//totalTaxAmt = totalTaxAmt + dblTaxAmt;
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(voucherNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    // For Complimentary Bill
	    funClearComplimetaryBillAmt(voucherNo);

	    if (clsGlobalVarClass.gHomeDelSMSYN)
	    {
		objBillSettlementUtility.funSendSMS(voucherNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
	    }
	    lblVoucherNo.setText(voucherNo);

	    if (clsGlobalVarClass.gKOTPrintingEnableForDirectBiller)
	    {
		if ("Text File".equalsIgnoreCase(clsGlobalVarClass.gPrintType))
		{
		    funTextFilePrintingKOTForDirectBiller();
		}
		else
		{
		    funTextFilePrintingKOTForDirectBiller();
		}
	    }
	    dispose();
	    clsGlobalVarClass.gDeliveryCharges = 0.00;
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSaveBillDiscDtlForBillSeriesForDirectBiller(List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{
	    StringBuilder sqlBillDiscDtl = new StringBuilder();

	    Iterator<Map.Entry<String, clsBillDiscountDtl>> itDiscEntry = mapBillDiscDtl.entrySet().iterator();
	    if (itDiscEntry.hasNext())
	    {
		Map.Entry<String, clsBillDiscountDtl> discEntry = itDiscEntry.next();
		String key = discEntry.getKey();
		String discOnType = key.split("!")[0];
		String discOnValue = key.split("!")[1];

		if (discOnType.equalsIgnoreCase("Total"))
		{
		    clsBillDiscountDtl objBillDiscDtl = mapBillDiscDtl.get(key);
		    String remark = objBillDiscDtl.getRemark();
		    String reason = objBillDiscDtl.getReason();

		    double tempDiscAmt = 0, tempDiscPer = 0;
		    for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
		    {
			clsBillItemDtl objBillItemDtl = entry.getValue();
			tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
			tempDiscPer = objBillItemDtl.getDiscountPercentage();
		    }
		    if (_subTotal > 0)
		    {
			tempDiscPer = (tempDiscAmt * 100) / _subTotal;
		    }
		    dblDiscountAmt = tempDiscAmt;
		    dblDiscountPer = tempDiscPer;

		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
		    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + tempDiscAmt + "','" + tempDiscPer + "','" + _subTotal + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    //save total disc for bill series
		    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());

		}
		else if (discOnType.equalsIgnoreCase("ItemWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();

			for (clsBillItemDtl objItemDtl : listOfItemDtl)
			{
			    if (objItemDtl.getItemName().equalsIgnoreCase(discOnValue))
			    {
				sqlBillDiscDtl.setLength(0);
				sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
				sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + objBillDiscDtl.getDiscAmt() + "','" + objBillDiscDtl.getDiscPer() + "','" + objBillDiscDtl.getDiscOnAmt() + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
				//save item wise disc for bill series
				clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());

				totalDiscAmt += objBillDiscDtl.getDiscAmt();
			    }
			}
		    }

		    if (_subTotal == 0.00)
		    {

		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
		}
		else if (discOnType.equalsIgnoreCase("GroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			for (clsBillItemDtl objItemDtl : listOfItemDtl)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("select a.strItemCode  "
				    + "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
				    + "where a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode  "
				    + "and a.strItemCode='" + objItemDtl.getItemCode().substring(0, 7) + "' "
				    + "and c.strGroupName='" + discOnValue + "' ");

			    ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			    if (rsGroupWiseDisc.next())
			    {
				discOnAmt += objItemDtl.getAmount();
			    }
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;
			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {

		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
		}
		else if (discOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			for (clsBillItemDtl objItemDtl : listOfItemDtl)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("select a.strItemCode  "
				    + "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
				    + "where a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode  "
				    + "and a.strItemCode='" + objItemDtl.getItemCode().substring(0, 7) + "' "
				    + "and b.strSubGroupName='" + discOnValue + "' ");

			    ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			    if (rsGroupWiseDisc.next())
			    {
				discOnAmt += objItemDtl.getAmount();
			    }
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;

			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {

		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
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

    private void funResetSettlementButtons()
    {
	_settlementNavigate = 1;
	funPrevSettlementMode();
    }

    private String funGetTableStatus(String tableNo)
    {
	String tableStatus = "Normal";
	try
	{
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
	    
	     SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	    String posDate = clsGlobalVarClass.getPOSDateForTransaction().split(" ")[0];
	    String posTime = clsGlobalVarClass.getPOSDateForTransaction().split(" ")[1];

	    String sql = "select a.strCustomerCode,a.tmeResTime as reservationtime from tblreservation a "
		    + " where a.strTableNo='" + tableNo + "' "
		    + " and date(a.dteResDate)='" + posDate + "' "
		    + " order by a.strResCode desc "
		    + " limit 1 ";
	    ResultSet rsReserve = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsReserve.next())
	    {
		Date reservationDateTime = simpleDateFormat.parse(rsReserve.getString(2));
		
		String reservationTime=simpleDateFormat.format(reservationDateTime);
		
		Date dteResDateTime=simpleDateTimeFormat.parse(posDate+" "+reservationTime);	
							
		Date dtePOSDateTime = simpleDateTimeFormat.parse(clsGlobalVarClass.getPOSDateForTransaction());
		
		

		if (dtePOSDateTime.getTime() > dteResDateTime.getTime())
		{
		    tableStatus = "Normal";
		}
		else
		{
		    tableStatus = "Reserve";
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
	    return tableStatus;
	}
    }

    private double funGetBillHdGrandTotal(String gPOSCode, String billNo)
    {
	double billHdGrandTotal = 0.00;
	try
	{
	    ResultSet rsBillHdGTotal = clsGlobalVarClass.dbMysql.executeResultSet("select dblGrandTotal "
		    + "from tblbillhd "
		    + "where strPOSCode='" + gPOSCode + "' "
		    + "and strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
	    if (rsBillHdGTotal.next())
	    {
		billHdGrandTotal = rsBillHdGTotal.getDouble(1);
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
	    return billHdGrandTotal;
	}
    }

    private void funUpdateBillSeriesGrandTotal(String posCode, String billNo, double grandTotal)
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute("update tblbillseriesbilldtl set  dblGrandTotal='" + grandTotal + "' "
		    + " where strHdBillNo='" + billNo + "' "
		    + " and strPOSCode='" + posCode + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ");
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSendModifyBillSMS(String billNo, String mobileNo)
    {

	try
	{
	    clsUtility2 objUtility2 = new clsUtility2();
	    StringBuilder mainSMSBuilder = new StringBuilder();

	    mainSMSBuilder.append("ModifyBill");
	    mainSMSBuilder.append(" ,Bill_No:" + billNo);
	    mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
	    mainSMSBuilder.append(" ,User:" + clsGlobalVarClass.gUserCode);

	    String sql = "select a.strBillNo,a.strReasonName,a.dblActualAmount,a.dblModifiedAmount,a.strUserEdited,TIME_FORMAT(time(a.dteModifyVoidBill),'%h:%i')time "
		    + "from tblvoidbillhd a "
		    + "where a.strTransType='MB' "
		    + "and strBillNo='" + billNo + "' ";
	    ResultSet rsModBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsModBill.next())
	    {
		mainSMSBuilder.append(" ,Actual Amt:" + gDecimalFormat.format(Math.rint(rsModBill.getDouble(3))));
		mainSMSBuilder.append(" ,Modified Amt:" + gDecimalFormat.format(Math.rint(rsModBill.getDouble(4))));
		mainSMSBuilder.append(" ,Disc Amt:" + gDecimalFormat.format(Math.rint(dblDiscountAmt)));
		mainSMSBuilder.append(" ,Time:" + rsModBill.getString(6));
		mainSMSBuilder.append(" ,Reason:" + rsModBill.getString(2));
		mainSMSBuilder.append(" ,Remarks:" + discountRemarks);
	    }
	    rsModBill.close();

	    ArrayList<String> mobileNoList = new ArrayList<>();
	    String mobNos[] = mobileNo.split(",");
	    for (String mn : mobNos)
	    {
		mobileNoList.add(mn);
	    }
	    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
	    objSMSSender.start();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSendComplementaryBillSMS(String billNo, String mobileNo)
    {

	try
	{
	    clsUtility2 objUtility2 = new clsUtility2();
	    StringBuilder mainSMSBuilder = new StringBuilder();

	    mainSMSBuilder.append("ComplementaryBill");
	    mainSMSBuilder.append(" ,Bill_No:" + billNo);
	    mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
	    mainSMSBuilder.append(" ,User:" + clsGlobalVarClass.gUserCode);

	    String sql = "select a.strBillNo,TIME_FORMAT(time(a.dteBillDate),'%h:%i')time,c.strReasonName,a.strRemarks,sum(b.dblAmount) "
		    + "from tblbillhd a "
		    + "left outer join tblbillcomplementrydtl b on a.strBillNo=b.strBillNo "
		    + "left outer join tblreasonmaster c on a.strReasonCode=c.strReasonCode "
		    + "where a.strBillNo='" + billNo + "' "
		    + "group by a.strBillNo ";
	    ResultSet rsModBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsModBill.next())
	    {
		mainSMSBuilder.append(" ,Time:" + rsModBill.getString(2));
		mainSMSBuilder.append(" ,Amount:" + gDecimalFormat.format(Math.rint(rsModBill.getDouble(5))));
		mainSMSBuilder.append(" ,Reason:" + rsModBill.getString(3));
		mainSMSBuilder.append(" ,Remarks:" + rsModBill.getString(4));
	    }
	    rsModBill.close();

	    ArrayList<String> mobileNoList = new ArrayList<>();
	    String mobNos[] = mobileNo.split(",");
	    for (String mn : mobNos)
	    {
		mobileNoList.add(mn);
	    }
	    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, mainSMSBuilder.toString());
	    objSMSSender.start();
	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    String viewORprint = "print";

    private void funReprintButtonClick()
    {

	boolean isCanceled = false;

	String remark = "", reason = "";
	try
	{
	    if (clsGlobalVarClass.gPrintRemarkAndReasonForReprint)
	    {
		if (clsGlobalVarClass.gTouchScreenMode)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Discount Remark.").setVisible(true);
//                txtAreaRemark.setText(clsGlobalVarClass.gKeyboardValue);
		    remark = clsGlobalVarClass.gKeyboardValue;
		}
		else
		{
		    remark = JOptionPane.showInputDialog(null, "Enter Discount Remarks");
//                txtAreaRemark.setText(discountRemarks);
		}
		if (vReprintReasonCode.size() == 0)
		{
		    JOptionPane.showMessageDialog(this, "No Reprint reasons are created");
		    return;
		}
		else
		{
		    Object[] arrObjReasonCode = vReprintReasonCode.toArray();
		    Object[] arrObjReasonName = vReprintReasonName.toArray();
		    String selectedReason = (String) JOptionPane.showInputDialog(this, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
		    if (null == selectedReason)
		    {
			JOptionPane.showMessageDialog(this, "Please Select Reason");
			return;
		    }
		    else
		    {
			for (int cntReason = 0; cntReason < vReprintReasonCode.size(); cntReason++)
			{
			    if (vReprintReasonName.elementAt(cntReason).toString().equals(selectedReason))
			    {
				reason = vReprintReasonCode.elementAt(cntReason).toString();
				break;
			    }
			}
		    }
		}
	    }
	    objUtility.funPrintBill(voucherNo, clsGlobalVarClass.getOnlyPOSDateForTransaction(), true, clsGlobalVarClass.gPOSCode, "print");
	    /**
	     * save reprint audit
	     */
	    objUtility2.funSaveReprintAudit("Reprint", "Bill", reason, remark, "", voucherNo, "");
	}

	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
    }

    private void funCallComplimentaryItemsForm()
    {

	List<clsBillItemDtl> listBillItemDtl = new ArrayList<clsBillItemDtl>();
	for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
	{
	    if (entry.getKey().length() == 7)
	    {
		double rate = entry.getValue().getAmount() / entry.getValue().getQuantity();
		//entry.getValue().setRate(rate);
		listBillItemDtl.add(entry.getValue());
	    }
	}

	frmBillItems objBillItems = new frmBillItems(this, listBillItemDtl);
	objBillItems.setVisible(true);
	objBillItems.setLocationRelativeTo(this);

    }

    public void funDiscPercentageMouseClicked()
    {
	textValue1 = "";
	txtDiscountPer.setText("");
	txtDiscountAmt.setText("0.00");
	amountBox = "discount";
	discountType = "Percent";
    }

    public void funDiscAmountMouseClicked()
    {
	textValue1 = "";
	txtDiscountAmt.setText("");
	txtDiscountPer.setText("0.00");
	discountType = "Amount";
	amountBox = "discount";
    }

    public HashMap<String, String> getHmItemList()
    {
	return hmItemList;
    }

    public Map<String, clsBillItemDtl> getHmBillItemDtl()
    {
	return hmBillItemDtl;
    }

    public HashMap<String, clsSettelementOptions> getHmSettlemetnOptions()
    {
	return hmSettlemetnOptions;
    }

    public Map<String, clsPromotionItems> getHmPromoItem()
    {
	return hmPromoItem;
    }

    public Map<String, Double> getHmAddKOTItems()
    {
	return hmAddKOTItems;
    }

    public frmMakeBill getMakeBillObj()
    {
	return makeBillObj;
    }

    public Map<String, clsBillDiscountDtl> getMapBillDiscDtl()
    {
	return mapBillDiscDtl;
    }

    public HashMap<String, clsBillItemDtl> getMapPromoItemDisc()
    {
	return mapPromoItemDisc;
    }

    public Map<String, List<clsBillItemDtl>> getHmBillSeriesItemList()
    {
	return hmBillSeriesItemList;
    }

    public Map<String, clsSettelementOptions> getHmJioMoneySettleDtl()
    {
	return hmJioMoneySettleDtl;
    }

    public Map<String, clsBillDtl> getHmComplimentaryBillItemDtl()
    {
	return hmComplimentaryBillItemDtl;
    }

    public Map<String, Double> getMapBeforeComplimentory()
    {
	return mapBeforeComplimentory;
    }

    public String getAmountBox()
    {
	return amountBox;
    }

    public String getDiscountRemarks()
    {
	return discountRemarks;
    }

    public boolean isSettleMode()
    {
	return settleMode;
    }

    public String getDiscountType()
    {
	return discountType;
    }

    public BigDecimal getBtnVal()
    {
	return btnVal;
    }

    public BigDecimal getBillAmount()
    {
	return billAmount;
    }

    public boolean isFlgEnterBtnPressed()
    {
	return flgEnterBtnPressed;
    }

    public boolean isFlgUpdateBillTableForDiscount()
    {
	return flgUpdateBillTableForDiscount;
    }

    public int getPaxNo()
    {
	return paxNo;
    }

    public int getIntBillSeriesPaxNo()
    {
	return intBillSeriesPaxNo;
    }

    public String getSettleName()
    {
	return settleName;
    }

    public String getSettleType()
    {
	return settleType;
    }

    public String getBillPrintOnSettlement()
    {
	return billPrintOnSettlement;
    }

    public String getTableNo()
    {
	return tableNo;
    }

    public boolean isFlgMakeKot()
    {
	return flgMakeKot;
    }

    public boolean isFlgMakeBill()
    {
	return flgMakeBill;
    }

    public boolean isFlgUnsettledBills()
    {
	return flgUnsettledBills;
    }

    public double getDblTotalTaxAmt()
    {
	return dblTotalTaxAmt;
    }

    public double getCurrencyRate()
    {
	return currencyRate;
    }

    public boolean isFlgGiftVoucherOK()
    {
	return flgGiftVoucherOK;
    }

    public static String getDebitCardNo()
    {
	return debitCardNo;
    }

    public String getSelectedReasonCode()
    {
	return selectedReasonCode;
    }

    public static String getCustomerCodeForCredit()
    {
	return customerCodeForCredit;
    }

    public ArrayList<String> getListItemCode()
    {
	return listItemCode;
    }

    public ArrayList<String> getListSubGroupCode()
    {
	return listSubGroupCode;
    }

    public ArrayList<String> getListSubGroupName()
    {
	return listSubGroupName;
    }

    public ArrayList<String> getListGroupName()
    {
	return listGroupName;
    }

    public ArrayList<String> getListGroupCode()
    {
	return listGroupCode;
    }

    public ButtonGroup getRadioButtonGroup()
    {
	return radioButtonGroup;
    }

    public double getCmsMemberBalance()
    {
	return cmsMemberBalance;
    }

    public String getBillType()
    {
	return billType;
    }

    public String getBillTypeForTax()
    {
	return billTypeForTax;
    }

    public int getDisableNext()
    {
	return disableNext;
    }

    public String getCustCode()
    {
	return custCode;
    }

    public String getDelPersonCode()
    {
	return delPersonCode;
    }

    public List<clsDirectBillerItemDtl> getObjListDirectBillerItemDtl()
    {
	return objListDirectBillerItemDtl;
    }

    public List<clsDirectBillerItemDtl> getObjListItemDtlTemp()
    {
	return objListItemDtlTemp;
    }

    public double getDblDiscountAmt()
    {
	return dblDiscountAmt;
    }

    public double getDblDiscountPer()
    {
	return dblDiscountPer;
    }

    public double getDblSettlementAmount()
    {
	return dblSettlementAmount;
    }

    public double getPaidAmount()
    {
	return _paidAmount;
    }

    public double getSubTotal()
    {
	return _subTotal;
    }

    public double getNetAmount()
    {
	return _netAmount;
    }

    public double getGrandTotal()
    {
	return _grandTotal;
    }

    public double getBalanceAmount()
    {
	return _balanceAmount;
    }

    public double getRefundAmount()
    {
	return _refundAmount;
    }

    public String getGiftVoucherCode()
    {
	return _giftVoucherCode;
    }

    public String getCustMobileNoForCRM()
    {
	return custMobileNoForCRM;
    }

    public String getGiftVoucherSeriesCode()
    {
	return _giftVoucherSeriesCode;
    }

    public String getAdvOrderBookingNo()
    {
	return advOrderBookingNo;
    }

    public String getCouponCode()
    {
	return couponCode;
    }

    public int getSettlementNavigate()
    {
	return _settlementNavigate;
    }

    public double getDeliveryCharge()
    {
	return _deliveryCharge;
    }

    public double getLoyalityPoints()
    {
	return _loyalityPoints;
    }

    public String getDtPOSDate()
    {
	return dtPOSDate;
    }

    public String getHomeDelivery()
    {
	return homeDelivery;
    }

    public String getAreaCode()
    {
	return areaCode;
    }

    public String getOperationTypeForTax()
    {
	return operationTypeForTax;
    }

    public String getTakeAway()
    {
	return takeAway;
    }

    public String getCallingFormName()
    {
	return callingFormName;
    }

    public String getCmsMemberName()
    {
	return cmsMemberName;
    }

    public List<clsTaxCalculationDtls> getArrListTaxCal()
    {
	return arrListTaxCal;
    }

    public List<clsTaxCalculationDtls> setArrListTaxCal(List<clsTaxCalculationDtls> arrListTaxCal)
    {
	return this.arrListTaxCal = arrListTaxCal;
    }

    public boolean isFlagAddKOTstoBill()
    {
	return flagAddKOTstoBill;
    }

    public frmMakeKOT getKotObj()
    {
	return kotObj;
    }

    public frmMakeKOT getObjMakeKOT()
    {
	return objMakeKOT;
    }

    public frmDirectBiller getObjDirectBiller()
    {
	return objDirectBiller;
    }

    public frmAddKOTToBill getObjAddKOTToBill()
    {
	return objAddKOTToBill;
    }

    public List<String> getListBillFromKOT()
    {
	return listBillFromKOT;
    }

    public String getTakeAwayRemarks()
    {
	return takeAwayRemarks;
    }

    public String getCustAddType()
    {
	return custAddType;
    }

    public double getCmsMemberCreditLimit()
    {
	return cmsMemberCreditLimit;
    }

    public String getCmsStopCredit()
    {
	return cmsStopCredit;
    }

    public boolean isIsDirectSettleFromMakeBill()
    {
	return isDirectSettleFromMakeBill;
    }

    public List<clsBillSeriesBillDtl> getListBillSeriesBillDtl()
    {
	return listBillSeriesBillDtl;
    }

    public double getGrandTotalRoundOffBy()
    {
	return _grandTotalRoundOffBy;
    }

    public String getRewardId()
    {
	return rewardId;
    }

    public String getDiscountCode()
    {
	return discountCode;
    }

    public String getQRStringForBenow()
    {
	return QRStringForBenow;
    }

    public clsCalculateBillDiscount getObjBillSettlementUtility()
    {
	return objCalculateBillDisc;
    }

    public JButton getBtnGetOffer()
    {
	return btnGetOffer;
    }

    public JButton getBtnJioMoneyCheckStatus()
    {
	return btnJioMoneyCheckStatus;
    }

    public JButton getBtnPrint()
    {
	return btnPrint;
    }

    public JButton getBtnReprint()
    {
	return btnReprint;
    }

    public JButton getBtnSettle()
    {
	return btnSettle;
    }

    public JButton getBtnShowCompliItems()
    {
	return btnShowCompliItems;
    }

    public JCheckBox getChkDiscFromMaster()
    {
	return chkDiscFromMaster;
    }

    public JCheckBox getChkJioNotification()
    {
	return chkJioNotification;
    }

    public JComboBox getCmbItemCategory()
    {
	return cmbItemCategory;
    }

    public JLabel getLabelTableNo()
    {
	return labelTableNo;
    }

    public JLabel getLblBenowQRCode()
    {
	return lblBenowQRCode;
    }

    public JLabel getLblcard()
    {
	return lblcard;
    }

    public JRadioButton getRdbAll()
    {
	return rdbAll;
    }

    public JRadioButton getRdbGroupWise()
    {
	return rdbGroupWise;
    }

    public JRadioButton getRdbItemWise()
    {
	return rdbItemWise;
    }

    public JRadioButton getRdbSubGroupWise()
    {
	return rdbSubGroupWise;
    }

    public JTextField getTxtDiscountAmt()
    {
	return txtDiscountAmt;
    }

    public JTextField getTxtDiscountPer()
    {
	return txtDiscountPer;
    }

    public void setRewardId(String rewardId)
    {
	this.rewardId = rewardId;
    }

    public void setSubTotal(double _subTotal)
    {
	this._subTotal = _subTotal;
    }

    public void setNetAmount(double _netAmount)
    {
	this._netAmount = _netAmount;
    }

    public void setGrandTotal(double _grandTotal)
    {
	this._grandTotal = _grandTotal;
    }

    public void setBalanceAmount(double _balanceAmount)
    {
	this._balanceAmount = _balanceAmount;
    }

    public void setRefundAmount(double _refundAmount)
    {
	this._refundAmount = _refundAmount;
    }

    public void setGiftVoucherCode(String _giftVoucherCode)
    {
	this._giftVoucherCode = _giftVoucherCode;
    }

    public void setGiftVoucherSeriesCode(String _giftVoucherSeriesCode)
    {
	this._giftVoucherSeriesCode = _giftVoucherSeriesCode;
    }

    public void setSettlementNavigate(int _settlementNavigate)
    {
	this._settlementNavigate = _settlementNavigate;
    }

    public void setDeliveryCharge(double _deliveryCharge)
    {
	this._deliveryCharge = _deliveryCharge;
    }

    public void setLoyalityPoints(double _loyalityPoints)
    {
	this._loyalityPoints = _loyalityPoints;
    }

    public void setGrandTotalRoundOffBy(double _grandTotalRoundOffBy)
    {
	this._grandTotalRoundOffBy = _grandTotalRoundOffBy;
    }

    public JPanel getPanelRemaks()
    {
	return PanelRemaks;
    }

    public JPanel getPanelCheque()
    {
	return PanelCheque;
    }

    public JPanel getPanelAmt()
    {
	return panelAmt;
    }

    public JTextArea getTxtAreaRemark()
    {
	return txtAreaRemark;
    }

    public Vector getvModifyReasonCode()
    {
	return vModifyReasonCode;
    }

    public Vector getvModifyReasonName()
    {
	return vModifyReasonName;
    }

    public Vector getvComplReasonCode()
    {
	return vComplReasonCode;
    }

    public Vector getvComplReasonName()
    {
	return vComplReasonName;
    }

    public Vector getvReprintReasonCode()
    {
	return vReprintReasonCode;
    }

    public Vector getvReprintReasonName()
    {
	return vReprintReasonName;
    }

    public Vector getvReasonCodeForDiscount()
    {
	return vReasonCodeForDiscount;
    }

    public Vector getvReasonNameForDiscount()
    {
	return vReasonNameForDiscount;
    }

    public void setSelectedReasonCode(String selectedReasonCode)
    {
	this.selectedReasonCode = selectedReasonCode;
    }

    public JPanel getOrderPanel()
    {
	return OrderPanel;
    }

    public JPanel getPanelBenowSettlement()
    {
	return PanelBenowSettlement;
    }

    public JPanel getPanelCard()
    {
	return PanelCard;
    }

    public JPanel getPanelCoupen()
    {
	return PanelCoupen;
    }

    public JPanel getPanelGiftVoucher()
    {
	return PanelGiftVoucher;
    }

    public JPanel getPanelJioMoneySettlement()
    {
	return PanelJioMoneySettlement;
    }

    public void setDiscountRemarks(String discountRemarks)
    {
	this.discountRemarks = discountRemarks;
    }

    public void setDiscountCode(String discountCode)
    {
	this.discountCode = discountCode;
    }

    public void setDblTotalTaxAmt(double dblTotalTaxAmt)
    {
	this.dblTotalTaxAmt = dblTotalTaxAmt;
    }

    public void setDblDiscountAmt(double dblDiscountAmt)
    {
	this.dblDiscountAmt = dblDiscountAmt;
    }

    public void setDblDiscountPer(double dblDiscountPer)
    {
	this.dblDiscountPer = dblDiscountPer;
    }

    public void setDblSettlementAmount(double dblSettlementAmount)
    {
	this.dblSettlementAmount = dblSettlementAmount;
    }

    public JTextField getTxtAmount()
    {
	return txtAmount;
    }

    public JTextField getTxtBalance()
    {
	return txtBalance;
    }

    public JTextField getTxtBankName()
    {
	return txtBankName;
    }

    public JTextField getTxtCardName()
    {
	return txtCardName;
    }

    public JTextField getTxtChequeNo()
    {
	return txtChequeNo;
    }

    public JTextField getTxtCoupenAmt()
    {
	return txtCoupenAmt;
    }

    public static JTextField getTxtCustomerName()
    {
	return txtCustomerName;
    }

    public JTextField getTxtDeliveryCharges()
    {
	return txtDeliveryCharges;
    }

    public JTextField getTxtFolioNo()
    {
	return txtFolioNo;
    }

    public JTextField getTxtGuestCode()
    {
	return txtGuestCode;
    }

    public JTextField getTxtGuestName()
    {
	return txtGuestName;
    }

    public JTextField getTxtJioDestination()
    {
	return txtJioDestination;
    }

    public JTextField getTxtJioMoneyCode()
    {
	return txtJioMoneyCode;
    }

    public JTextField getTxtManualBillNo()
    {
	return txtManualBillNo;
    }

    public JTextField getTxtPaidAmt()
    {
	return txtPaidAmt;
    }

    public JTextField getTxtRemark()
    {
	return txtRemark;
    }

    public JTextField getTxtRoomNo()
    {
	return txtRoomNo;
    }

    public JTextField getTxtSeriesNo()
    {
	return txtSeriesNo;
    }

    public JTextField getTxtTip()
    {
	return txtTip;
    }

    public JTextField getTxtVoucherSeries()
    {
	return txtVoucherSeries;
    }

    public void setAmountBox(String amountBox)
    {
	this.amountBox = amountBox;
    }

    public void setTextValue1(String textValue1)
    {
	this.textValue1 = textValue1;
    }

    public void setTextValue2(String textValue2)
    {
	this.textValue2 = textValue2;
    }

    public JLabel getLblDisc()
    {
	return lblDisc;
    }

    public JLabel getLblDiscAmt()
    {
	return lblDiscAmt;
    }

    public JButton getBtnDiscOk()
    {
	return btnDiscOk;
    }

    public void setBillPrintOnSettlement(String billPrintOnSettlement)
    {
	this.billPrintOnSettlement = billPrintOnSettlement;
    }

    public String getVoucherNo()
    {
	return voucherNo;
    }

    public void setVoucherNo(String voucherNo)
    {
	this.voucherNo = voucherNo;
    }

    public void setDtPOSDate(String dtPOSDate)
    {
	this.dtPOSDate = dtPOSDate;
    }

    public void setObjDirectBiller(frmDirectBiller objDirectBiller)
    {
	this.objDirectBiller = objDirectBiller;
    }

    public String getWaiterNo()
    {
	return waiterNo;
    }

    public void setWaiterNo(String waiterNo)
    {
	this.waiterNo = waiterNo;
    }

    public List<clsMakeKotItemDtl> getListOfKOTWiseItemDtl()
    {
	return listOfKOTWiseItemDtl;
    }

    public JTable getTblItemTable()
    {
	return tblItemTable;
    }

    public void setListBillSeriesBillDtl(List<clsBillSeriesBillDtl> listBillSeriesBillDtl)
    {
	this.listBillSeriesBillDtl = listBillSeriesBillDtl;
    }

    public void setIntBillSeriesPaxNo(int intBillSeriesPaxNo)
    {
	this.intBillSeriesPaxNo = intBillSeriesPaxNo;
    }

    public boolean isFlagBillForItems()
    {
	return flagBillForItems;
    }

    public String getStrButtonClicked()
    {
	return strButtonClicked;
    }

    public JLabel getLblVoucherNo()
    {
	return lblVoucherNo;
    }

    public void setCustCode(String custCode)
    {
	this.custCode = custCode;
    }

    public void setHmBillSeriesItemList(Map<String, List<clsBillItemDtl>> hmBillSeriesItemList)
    {
	this.hmBillSeriesItemList = hmBillSeriesItemList;
    }

    public JLabel getLblTableNo()
    {
	return lblTableNo;
    }

    public String getSettlementName()
    {
	return settlementName;
    }

    public void setSettlementName(String settlementName)
    {
	this.settlementName = settlementName;
    }

    public String getSettlementCode()
    {
	return settlementCode;
    }

    public void setSettlementCode(String settlementCode)
    {
	this.settlementCode = settlementCode;
    }

    private void funUpdateRegisterInOutPlayZone(String billNo)
    {
	frmRegisterInOutPlayZone objRegisterInOutPlayZone = null;
	String outDateTime = clsGlobalVarClass.getPOSDateForTransaction();

	try
	{
	    String sqlUpdateCheckOutPlayZone = "update tblregisterinoutplayzone  "
		    + "set strOut='Y' "
		    + ",strBillNo='" + billNo + "' "
		    + "where strIn='Y' "
		    + "and strOut='N' "
		    + "and strRegisterCode='" + checkOutPlayZoneRegisterCode + "' "
		    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";

	    int affectedRows = clsGlobalVarClass.dbMysql.execute(sqlUpdateCheckOutPlayZone);

	    objRegisterInOutPlayZone = new frmRegisterInOutPlayZone();
	    objRegisterInOutPlayZone.funCheckOutMessage(billNo, checkOutPlayZoneRegisterCode);

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    e.printStackTrace();
	}
	finally
	{
	    objRegisterInOutPlayZone = null;
	}
    }

    public void funSetCallingForm(String callingForm)
    {
	this.callingFormName = callingForm;
    }

    public void funSetObjMakeKOT(frmMakeKOT objMakeKOT)
    {
	this.objMakeKOT = objMakeKOT;
    }

    public String funGetOnlineOrderNo()
    {
	return this.onlineOrderNo;
    }

    private void funNSCButttonClicked()
    {
	if (isRemoveSCTax)
	{
	    isRemoveSCTax = false;
	    btnRemoveSCTax.setForeground(Color.white);
	}
	else
	{
	    isRemoveSCTax = true;
	    btnRemoveSCTax.setForeground(Color.red);
	}
	funRefreshItemTable();
    }
    
     private void funSendSettleBillSMS(String billNo, String mobileNo)
    {

        try
        {
            clsUtility2 objUtility2 = new clsUtility2();
            StringBuilder mainSMSBuilder = new StringBuilder();

            mainSMSBuilder.append("SettleBill");
            mainSMSBuilder.append(",Bill No:" + billNo);
            mainSMSBuilder.append(",POS:" + clsGlobalVarClass.gPOSName);
            mainSMSBuilder.append(",User:" + clsGlobalVarClass.gUserCode);

            String sql = "select a.strBillNo,a.strUserEdited,sum(b.dblSettlementAmt),strSettelmentDesc  "
                    + "from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c  "
                    + "where a.strBillNo='" + billNo + "' and a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode ";
            ResultSet rsModBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsModBill.next())
            {
		mainSMSBuilder.append(",Settlement Amt:" + rsModBill.getString(3));
                mainSMSBuilder.append(",Settlement Type:" + rsModBill.getString(4));
            }
            rsModBill.close();

            ArrayList<String> mobileNoList = new ArrayList<>();
            mobileNoList.add(mobileNo);
	    String strSmsContent = mainSMSBuilder.toString();
	    if(strSmsContent.contains("&"))
	    {
		strSmsContent=strSmsContent.replaceAll("&", "AND");
	    }
            clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, strSmsContent);
            objSMSSender.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
     
     
    private void funGenerateBillNoForBillSeriesForAddKOTToBill(String billNo, List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{
	    int billSeriesLastNo = 0;

	    hmBillItemDtl.clear();
	    double subTotal = 0.00;
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();

	    for (clsBillItemDtl obj : listOfItemDtl)
	    {
		if (obj.getItemCode().contains("M"))
		{
		    hmBillItemDtl.put(obj.getItemCode() + "!" + obj.getItemName(), obj);
		}
		else
		{
		    hmBillItemDtl.put(obj.getItemCode(), obj);
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
	    voucherNo = billNo;
	    revalidate();
	    if (clsGlobalVarClass.gTakeAway.equals("Yes"))
	    {
		operationType = "TakeAway";
	    }
	    if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
	    {
		operationType = "TakeAway";
		clsGlobalVarClass.hmTakeAway.remove(tableNo);
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

	    double advanceAmount = 0.00;
	    double _deliveryCharge = 0.00;
	    dblTotalTaxAmt = 0;
	    _netAmount = 0.00;
	    _subTotal = 0.00;
	    dblDiscountAmt = 0.00;
	    dblDiscountPer = 0.00;
	    _grandTotal = 0.00;

	    double tempDiscAmt = 0, tempDiscPer = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
	    {
		clsBillItemDtl objBillItemDtl = entry.getValue();
		tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
		tempDiscPer = objBillItemDtl.getDiscountPercentage();
	    }
	    if (subTotal > 0)
	    {
		tempDiscPer = (tempDiscAmt * 100) / subTotal;
	    }
	    _subTotal = subTotal;

	    arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, dtPOSDate, areaCode, operationType, subTotal, tempDiscAmt, "", settlementCode, "Sales");
	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		if (objTaxCalculationDtls.getTaxCalculationType().equalsIgnoreCase("Forward"))
		{
		    double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		    dblTotalTaxAmt = dblTotalTaxAmt + dblTaxAmt;
		}
	    }
	    int intLastOrderNo=0;
	    String transactionType="";
	     String sql1 = "select strAdvBookingNo,dteBillDate,strPOSCode,strSettelmentMode,dblDiscountAmt,"
		    + "dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,strTakeAway,strOperationType"
		    + ",strTableNo,strWaiterNo,strCustomerCode,intPaxNo,strReasonCode,strRemarks"
		    + ",dblTipAmount,dteSettleDate,strCounterCode,dblDeliveryCharges,strTransactionType,intOrderNo,strCRMRewardId "
		    + "from tblbillhd where strBillNo='" + voucherNo + "';";

		    ResultSet rsPrevBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
		    String customerCode = "";
		    while (rsPrevBillDtl.next())
		    {
			customerCode = rsPrevBillDtl.getString(14);
			tableNo = rsPrevBillDtl.getString(12);
			waiterNo = rsPrevBillDtl.getString(13);
			counterCode = rsPrevBillDtl.getString(20);
			operationType = rsPrevBillDtl.getString(11);
			transactionType = rsPrevBillDtl.getString(22);
			transactionType = rsPrevBillDtl.getString(22) + "," + "Add KOT To Bill";
			intLastOrderNo = rsPrevBillDtl.getInt(23);
			rewardId = rsPrevBillDtl.getString(24);
		    }
		    rsPrevBillDtl.close();
		    String deletePrevBill = "delete from tblbillhd where strBillNo='" + voucherNo + "';";
		    clsGlobalVarClass.dbMysql.execute(deletePrevBill);

		    //funReCalculateBillDiscount();
		    //clear old data for this billDiscountDtl table             
		    clsGlobalVarClass.dbMysql.execute("delete from tblbilldiscdtl where strBillNo='" + voucherNo + "' ");
		  
	    //save bill disc dtl
	    //funSaveBillDiscDtlForBillSeries(listOfItemDtl);
	    funSaveBillDiscDtlForBillSeriesAddKOTSToBill(listOfItemDtl);
	    _netAmount = _subTotal - dblDiscountAmt;
	    _grandTotal = _netAmount + dblTotalTaxAmt + _deliveryCharge;
	    _grandTotal = _grandTotal - advanceAmount;
	    _grandTotal = _grandTotal;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = objUtility2.funCalculateRoundOffAmount(_grandTotal);
	    _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    if (clsGlobalVarClass.gRoundOffBillFinalAmount)
	    {
		_grandTotal = mapRoundOff.get("roundOffAmt");
	    }
	    //end code to calculate roundoff amount and round off by amt

	    Map<String, clsBillDtl> hmComplimentaryBillItemDtlTemp = null;
	    if (hmComplimentaryBillItemDtl.size() > 0)
	    {
		hmComplimentaryBillItemDtlTemp = new HashMap<String, clsBillDtl>();
		for (Map.Entry<String, clsBillDtl> entry : hmComplimentaryBillItemDtl.entrySet())
		{
		    hmComplimentaryBillItemDtlTemp.put(entry.getKey(), entry.getValue());
		}
	    }

	    List<String> listBillItemDtl = new ArrayList<String>();
	    String custName = "";
	    String cardNo = "";
 	   String sqlItemDtl="SELECT strItemCode,upper(strItemName), SUM(Qty), SUM(Amt),dblRate,DiscAmt,DiscPer,tmeOrderProcessing,strKOTNo,tmeOrderPickup,strManualKOTNo,"
	    + " kttime,strCustomerCode,strPromoCode,strCardNo " +
	    " FROM ( SELECT strItemCode,strItemName,dblQuantity AS Qty,dblAmount AS Amt,dblDiscountAmt AS DiscAmt, " +
	    " dblDiscountPer AS DiscPer,dblRate,tmeOrderProcessing ,strKOTNo,tmeOrderPickup,strManualKOTNo ,"
	    + " time(dteBillDate) as kttime,strCustomerCode,strPromoCode,'' as strCardNo" +
	    " FROM tblbilldtl  WHERE strBillNo='"+voucherNo+"' UNION ALL " +
	    " SELECT strItemCode,strItemName,dblItemQuantity AS Qty,dblAmount AS Amt,0 AS DiscAmt,0 AS DiscPer,dblRate,tmeOrderProcessing,strKOTNo,tmeOrderPickup,strManualKOTNo,"
	    + " Time(dteDateCreated) as kttime,strCustomerCode,strPromoCode,strCardNo " +
	    " FROM tblitemrtemp " +
	    " WHERE strPosCode='" + clsGlobalVarClass.gPOSCode + "' \n" +
	    "  and strTableNo='" + tableNo + "' and strNCKotYN='N' ) a\n" +
	    "  where strItemCode in "+ objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) +" " +
	    " GROUP BY tmeOrderProcessing,strItemCode,strItemName ;";
	    ResultSet rsItemKOTDTL = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);
	    String kot = "";
	  
	    String insertBillPromoDtl ="";
	    String sqlInsertBillDtl = "insert into tblbilldtl(strItemCode,strItemName,strBillNo"
				    + ",dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo"
				    + ",strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo"
				    + ",dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup) "
				    + "values ";
	     String sqlBillModifierDtl = "insert into tblbillmodifierdtl(strBillNo,strItemCode,strModifierCode,"
			    + "strModifierName,dblRate,dblQuantity,dblAmount,strClientCode,dblDiscPer,dblDiscAmt,dteBillDate) "
			    + "values ";
	    StringBuilder sbSqlBillDtlvalues=new StringBuilder();
	    StringBuilder sbSqlBillModDtlValues=new StringBuilder();
	    clsGlobalVarClass.dbMysql.execute("delete from tblbillpromotiondtl where strBillNo='" + voucherNo + "' ");
	    while (rsItemKOTDTL.next())
	    {
		String iCode = rsItemKOTDTL.getString(1);
		String iName = rsItemKOTDTL.getString(2);
		double iQty = rsItemKOTDTL.getDouble(3);
		String iAmt = rsItemKOTDTL.getString(4);
		String orderProcessingTime = rsItemKOTDTL.getString(8);
		String orderPickupTime = rsItemKOTDTL.getString(10);
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

		double rate = rsItemKOTDTL.getDouble(5);
		kot = rsItemKOTDTL.getString(9);
		String manualKOTNo = rsItemKOTDTL.getString(11);
		kotDateTime = split + " " + rsItemKOTDTL.getString(12);
		custCode =customerCode;// rsItemKOTDTL.getString(8);
		//custName = rsItemKOTDTL.getString(9);
		String promoCode = rsItemKOTDTL.getString(14);
		cardNo = rsItemKOTDTL.getString(15);
		

		if (!iCode.contains("M"))
		{
		    if (hmPromoItem.size() > 0)
		    {
			if (null != hmPromoItem.get(iCode))
			{
			    clsPromotionItems objPromoItemDtl = hmPromoItem.get(iCode);
			    if (objPromoItemDtl.getPromoType().equals("ItemWise"))
			    {
				double freeQty = objPromoItemDtl.getFreeItemQty();
				double freeAmt = freeQty * rate;

				promoCode = objPromoItemDtl.getPromoCode();
				insertBillPromoDtl = "insert into tblbillpromotiondtl "
					+ "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					+ ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					+ "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					+ ",'" + freeQty + "','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					+ ",'N','" + objPromoItemDtl.getPromoType() + "','" + freeAmt + "','" + kotDateTime + "')";
				clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				hmPromoItem.remove(iCode);
			    }
			    else if (objPromoItemDtl.getPromoType().equals("Discount"))
			    {
				if (objPromoItemDtl.getDiscType().equals("Value"))
				{
				    double discAmt = objPromoItemDtl.getDiscAmt();

				    promoCode = objPromoItemDtl.getPromoCode();
				    insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
				else
				{
				    iAmt = String.valueOf(iQty * rate);
				    double amount = iQty * rate;
				    double discAmt = amount - (amount * (objPromoItemDtl.getDiscPer() / 100));

				    promoCode = objPromoItemDtl.getPromoCode();
				    insertBillPromoDtl = "insert into tblbillpromotiondtl "
					    + "(strBillNo,strItemCode,strPromotionCode,dblQuantity"
					    + ",dblRate,strClientCode,strDataPostFlag,strPromoType,dblAmount,dteBillDate) values "
					    + "('" + voucherNo + "','" + iCode + "','" + promoCode + "'"
					    + ",'1','" + rate + "','" + clsGlobalVarClass.gClientCode + "'"
					    + ",'N','" + objPromoItemDtl.getPromoType() + "','" + discAmt + "','" + kotDateTime + "')";
				    clsGlobalVarClass.dbMysql.execute(insertBillPromoDtl);
				    hmPromoItem.remove(iCode);
				}
			    }
			}
		    }

		    String amt = "0.00";
		    boolean flgComplimentaryBill = false;
		    if (hmSettlemetnOptions.size() == 1)
		    {
			for (clsSettelementOptions obj : hmSettlemetnOptions.values())
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
			discAmt = hmBillItemDtl.get(iCode).getDiscountAmount() * iQty;
			discPer = hmBillItemDtl.get(iCode).getDiscountPercentage();
		    }

		    if (iQty > 0)
		    {
			if (iName.startsWith("=>"))
			{
			    sbSqlBillDtlvalues.append(",('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ",'" + iQty
				    + "','" + amt + "','0.00','" + kotDateTime + "','" + kot + "','"
				    + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','Y','" + promoCode + "'"
				    + ",'" + counterCode + "','" + waiterNo + "'"
				    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "','" + orderPickupTime + "')");
			}
			else
			{
			    //strItemCode,strItemName,strBillNo,dblRate,dblQuantity,dblAmount,dblTaxAmount,dteBilldate,strKOTNo,
			    //strClientCode,strManualKOTNo,tdhYN,strPromoCode,strCounterCode,strWaiterNo,dblDiscountAmt,dblDiscountPer,dtBillDate,tmeOrderProcessing,tmeOrderPickup
			    sbSqlBillDtlvalues.append(",('" + iCode + "','" + iName + "','" + voucherNo + "'," + rate + ""
				    + ",'" + iQty + "','" + amt + "','0.00','" + kotDateTime + "','" + kot + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "','" + manualKOTNo + "','N','" + promoCode + "'"
				    + ",'" + counterCode+ "','" + waiterNo + "'"
				    + ",'" + discAmt + "','" + discPer + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "','" + orderProcessingTime + "','" + orderPickupTime + "')");
			}
			
			if (hmComplimentaryBillItemDtl.containsKey(iCode))
			{
			    clsBillDtl objBillDtl = hmComplimentaryBillItemDtl.get(iCode);
			    objBillDtl.setDblRate(rate);
			    objBillDtl.setStrBillNo(voucherNo);
			    objBillDtl.setDteBillDate(kotDateTime);
			    objBillDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
			    objBillDtl.setStrKOTNo(kot);
			    objBillDtl.setStrManualKOTNo(manualKOTNo);
			    objBillDtl.setStrPromoCode(promoCode);
			    objBillDtl.setStrCounterCode(counterCode);
			    objBillDtl.setStrWaiterNo(waiterNo);
			    objBillDtl.setDblDiscountAmt(discAmt);
			    objBillDtl.setDblDiscountPer(discPer);
			    objBillDtl.setDblTaxAmount(0.00);
			    objBillDtl.setDteBillSettleDate(kotDateTime);
			    hmComplimentaryBillItemDtl.put(iCode, objBillDtl);

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
		    if (hmBillItemDtl.containsKey(iCode + "!" + iName))
		    {
			modDiscAmt = hmBillItemDtl.get(iCode + "!" + iName).getDiscountAmount() * iQty;
			modDiscPer = hmBillItemDtl.get(iCode + "!" + iName).getDiscountPercentage();
		    }

		    StringBuilder sbTemp = new StringBuilder(iCode);
		    if (hmComplimentaryBillItemDtl.containsKey(sbTemp.substring(0, 7).toString()))
		    {
			amt = 0;
		    }
		   
		    sbSqlBillModDtlValues.append(",('" + voucherNo + "','" + iCode + "','" + modifierCode + "','" + iName + "'"
			    + "," + rate + ",'" + iQty + "','" + amt + "','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + modDiscPer + "','" + modDiscAmt + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
		    //System.out.println(sqlBillModifierDtl);
		    //clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
		}
	    }
	    rsItemKOTDTL.close();

	    
	    
	  
	    
	    if(sbSqlBillDtlvalues.length()>0){
		sbSqlBillDtlvalues.deleteCharAt(0);
		sqlInsertBillDtl=sqlInsertBillDtl+sbSqlBillDtlvalues;
		  clsGlobalVarClass.dbMysql.execute("delete from tblbilldtl where strBillNo='" + voucherNo + "' ");
		 clsGlobalVarClass.dbMysql.execute(sqlInsertBillDtl);
	    }
	    if(sbSqlBillModDtlValues.length()>0){
		sbSqlBillModDtlValues.deleteCharAt(0);
		sqlBillModifierDtl=sqlBillModifierDtl+sbSqlBillModDtlValues;
		clsGlobalVarClass.dbMysql.execute("delete from tblbillmodifierdtl where strBillNo='" + voucherNo + "' ");
		 clsGlobalVarClass.dbMysql.execute(sqlBillModifierDtl);
	    }
	    
	    String sqlBillPromo = "select dblQuantity,dblRate,strItemCode "
		    + "from tblbillpromotiondtl "
		    + " where strBillNo='" + voucherNo + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + " and strPromoType='ItemWise' ";
	    ResultSet rsBillPromo = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromo);
	    while (rsBillPromo.next())
	    {
		double freeQty = rsBillPromo.getDouble(1);
		String sqlBillDtl = "select strItemCode,dblQuantity,strKOTNo,dblAmount "
			+ " from tblbilldtl "
			+ " where strItemCode='" + rsBillPromo.getString(3) + "'"
			+ " and strBillNo='" + voucherNo + "'";
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

	    if (hmComplimentaryBillItemDtl.size() > 0)
	    {
		funInsertComplimentaryItemsInBillDtl(listBillItemDtl);
	    }
	    String billSeriesPrefix=voucherNo.substring(0,1);
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
		    + ",intBillSeriesPaxNo,dtBillDate,intOrderNo,strCRMRewardId,strManualBillNo,dblUSDConverionRate ) "
		    + "values('" + voucherNo + "','" + advOrderBookingNo + "','" + objUtility.funGetPOSDateForTransaction() + "','"
		    + clsGlobalVarClass.gPOSCode + "','','" + dblDiscountAmt + "','"
		    + dblDiscountPer + "','" + dblTotalTaxAmt + "','" + _subTotal + "','"
		    + _grandTotal + "','" + clsGlobalVarClass.gTakeAway + "','" + operationType + "','"
		    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + clsGlobalVarClass.gClientCode + "','" + tableNo + "','" + waiterNo + "'"
		    + ",'" + customerCode + "','" + clsGlobalVarClass.gShiftNo + "'"
		    + "," + paxNo + ",'" + selectedReasonCode + "','" + objUtility.funCheckSpecialCharacters(txtAreaRemark.getText().trim()) + "'"
		    + "," + txtTip.getText() + ",'" + objUtility.funGetPOSDateForTransaction() + "'"
		    + ",'" + counterCode + "'," + _deliveryCharge + ",'" + areaCode + "'"
		    + ",'" + discountRemarks + "','','','" + cardNo + "','" + clsGlobalVarClass.gTransactionType + "'"
		    + ",'" + _grandTotalRoundOffBy + "','" + intBillSeriesPaxNo + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'"
		    + ",'" + intLastOrderNo + "','" + rewardId + "','" + txtManualBillNo.getText().trim() + "','" + clsGlobalVarClass.gUSDConvertionRate + "' )";
	    clsGlobalVarClass.dbMysql.execute(sqlInsertBillHd);

	    clsBillHd objBillHd = new clsBillHd();
	    objBillHd.setStrBillNo(voucherNo);
	    objBillHd.setDblGrandTotal(_grandTotal);
	    objBillHd.setStrCustomerCode(customerCode);
	    objBillHd.setStrOnlineOrderNo(onlineOrderNo);
	    objBillSettlementUtility.funCallIntegrationAPIsAfterBillPrint(objBillHd);

	    /**
	     * update KOT to bill note
	     */
	    objBillSettlementUtility.funUpdateKOTToBillNote(clsGlobalVarClass.gPOSCode, tableNo, voucherNo);

	    clsBillSeriesBillDtl objBillSeriesBillDtl = new clsBillSeriesBillDtl();
	    objBillSeriesBillDtl.setStrHdBillNo(voucherNo);
	    objBillSeriesBillDtl.setStrBillSeries(billSeriesPrefix);
	    objBillSeriesBillDtl.setDblGrandTotal(_grandTotal);
	    objBillSeriesBillDtl.setFlgHomeDelPrint(flgHomeDelPrint);
	    listBillSeriesBillDtl.add(objBillSeriesBillDtl);

	    String deleteBillTaxDTL = "delete from tblbilltaxdtl where strBillNo='" + voucherNo + "'";
	    clsGlobalVarClass.dbMysql.execute(deleteBillTaxDTL);

	    // insert into tblbilltaxdtl    
	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();

	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(voucherNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBillTaxDtl.setDteBillDate(clsGlobalVarClass.getPOSDateForTransaction());

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(listObjBillTaxBillDtls);
	    clsUtility obj = new clsUtility();
	    obj.funUpdateBillDtlWithTaxValues(voucherNo, "Live", clsGlobalVarClass.gPOSOnlyDateForTransaction);

	    if (clsGlobalVarClass.gCMSIntegrationYN)
	    {
		if (custCode.trim().length() > 0)
		{
		    String sqlDeleteCustomer = "delete from tblcustomermaster where strCustomerCode='" + custCode + "' "
			    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sqlDeleteCustomer);

		    String sqlInsertCustomer = "insert into tblcustomermaster (strCustomerCode,strCustomerName,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode) "
			    + "values('" + custCode + "','" + custName + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "')";
		    clsGlobalVarClass.dbMysql.execute(sqlInsertCustomer);
		}
	    }

	    dispose();

	}
	catch (Exception e)
	{
	    objUtility.funShowDBConnectionLostErrorMessage(e);	
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funSaveBillDiscDtlForBillSeriesAddKOTSToBill(List<clsBillItemDtl> listOfItemDtl)
    {
	try
	{
	    StringBuilder sqlBillDiscDtl = new StringBuilder();

	    Iterator<Map.Entry<String, clsBillDiscountDtl>> itDiscEntry = mapBillDiscDtl.entrySet().iterator();
	    if (itDiscEntry.hasNext())
	    {
		Map.Entry<String, clsBillDiscountDtl> discEntry = itDiscEntry.next();
		String key = discEntry.getKey();
		String discOnType = key.split("!")[0];
		String discOnValue = key.split("!")[1];

		if (discOnType.equalsIgnoreCase("Total"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemName,a.dblAmount "
			    + " from tblitemrtemp a "
			    + " left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			    + " where  b.strDiscountApply='Y' and a.strTableNo='" + tableNo + "' "
			    + " and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " ");
		    ResultSet rsDiscOnItemWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    if (rsDiscOnItemWise.next())
		    {
			clsBillDiscountDtl objBillDiscDtl = mapBillDiscDtl.get(key);
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();

			double tempDiscAmt = 0, tempDiscPer = 0;
			for (Map.Entry<String, clsBillItemDtl> entry : hmBillItemDtl.entrySet())
			{
			    clsBillItemDtl objBillItemDtl = entry.getValue();
			    tempDiscAmt += objBillItemDtl.getDiscountAmount() * objBillItemDtl.getQuantity();
			    tempDiscPer = objBillItemDtl.getDiscountPercentage();
			}
			if (_subTotal > 0)
			{
			    tempDiscPer = (tempDiscAmt * 100) / _subTotal;
			}
			dblDiscountAmt = tempDiscAmt;
			dblDiscountPer = tempDiscPer;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + tempDiscAmt + "','" + tempDiscPer + "','" + _subTotal + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			//save total disc for bill series
			clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
		    }
		    rsDiscOnItemWise.close();
		}
		else if (discOnType.equalsIgnoreCase("ItemWise"))
		{
		    sqlBillDiscDtl.setLength(0);
		    sqlBillDiscDtl.append("select a.strItemName,a.dblAmount "
			    + " from tblitemrtemp a "
			    + " left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
			    + " where  b.strDiscountApply='Y' and a.strTableNo='" + tableNo + "' "
			    + " and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
			    + " and a.strItemName = '" + discOnValue + "' ");
		    ResultSet rsDiscOnItemWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
		    if (rsDiscOnItemWise.next())
		    {
			itDiscEntry = mapBillDiscDtl.entrySet().iterator();
			double totalDiscAmt = 0.00, finalDiscPer = 0.00;
			for (int i = 0; itDiscEntry.hasNext(); i++)
			{
			    discEntry = itDiscEntry.next();
			    key = discEntry.getKey();
			    clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			    discOnType = key.split("!")[0];
			    discOnValue = key.split("!")[1];
			    String remark = objBillDiscDtl.getRemark();
			    String reason = objBillDiscDtl.getReason();

			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + objBillDiscDtl.getDiscAmt() + "','" + objBillDiscDtl.getDiscPer() + "','" + objBillDiscDtl.getDiscOnAmt() + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			    totalDiscAmt += objBillDiscDtl.getDiscAmt();
			}

			if (_subTotal == 0.00)
			{
			}
			else
			{
			    finalDiscPer = (totalDiscAmt / _subTotal) * 100;
			}
			dblDiscountAmt = totalDiscAmt;
			dblDiscountPer = finalDiscPer;
		    }
		    rsDiscOnItemWise.close();
		}
		else if (discOnType.equalsIgnoreCase("GroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("select a.strItemCode,a.dblAmount,d.strGroupName "
				+ "from tblitemrtemp a "
				+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
				+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
				+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
				+ "where  b.strDiscountApply='Y' "
				+ "and a.strTableNo='" + tableNo + "'"
				+ "and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
				+ "and d.strGroupName='" + discOnValue + "' ");
			ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			while (rsGroupWiseDisc.next())
			{
			    discOnAmt += rsGroupWiseDisc.getDouble("dblAmount");
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;
			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {
		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
		}
		else if (discOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    itDiscEntry = mapBillDiscDtl.entrySet().iterator();
		    double totalDiscAmt = 0.00, finalDiscPer = 0.00;
		    for (int i = 0; itDiscEntry.hasNext(); i++)
		    {
			discEntry = itDiscEntry.next();
			key = discEntry.getKey();
			clsBillDiscountDtl objBillDiscDtl = discEntry.getValue();

			discOnType = key.split("!")[0];
			discOnValue = key.split("!")[1];
			String remark = objBillDiscDtl.getRemark();
			String reason = objBillDiscDtl.getReason();
			double discPer = objBillDiscDtl.getDiscPer();

			double discAmt = 0.00;
			double discOnAmt = 0.00;

			sqlBillDiscDtl.setLength(0);
			sqlBillDiscDtl.append("select a.strItemCode,a.dblAmount,c.strSubGroupName "
				+ "from tblitemrtemp a "
				+ "left outer  join tblitemmaster b on (a.strItemCode=b.strItemCode or left(a.strItemCode,7)=b.strItemCode) "
				+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
				+ "where  b.strDiscountApply='Y' "
				+ "and a.strTableNo='" + tableNo + "'"
				+ "and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList(listOfItemDtl) + " "
				+ "and c.strSubGroupName='" + discOnValue + "' ");

			ResultSet rsGroupWiseDisc = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDiscDtl.toString());
			while (rsGroupWiseDisc.next())
			{
			    discOnAmt += rsGroupWiseDisc.getDouble("dblAmount");
			}
			discAmt = (discPer / 100) * discOnAmt;

			totalDiscAmt += discAmt;

			if (discAmt > 0)
			{
			    sqlBillDiscDtl.setLength(0);
			    sqlBillDiscDtl.append("insert into tblbilldiscdtl values ");
			    sqlBillDiscDtl.append("('" + voucherNo + "','" + clsGlobalVarClass.gPOSCode + "','" + discAmt + "','" + discPer + "','" + discOnAmt + "','" + discOnType + "','" + discOnValue + "','" + reason + "','" + remark + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getPOSDateForTransaction() + "')");
			    //save item wise disc for bill series
			    clsGlobalVarClass.dbMysql.execute(sqlBillDiscDtl.toString());
			}
		    }
		    if (_subTotal == 0.00)
		    {
		    }
		    else
		    {
			finalDiscPer = (totalDiscAmt / _subTotal) * 100;
		    }
		    dblDiscountAmt = totalDiscAmt;
		    dblDiscountPer = finalDiscPer;
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
