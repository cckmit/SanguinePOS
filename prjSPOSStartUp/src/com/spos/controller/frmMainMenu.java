package com.spos.controller;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPLUItemDtl;
import com.POSGlobal.controller.clsSendMail;
import com.POSGlobal.controller.clsStructureUpdater;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmChangePassword;
import com.POSGlobal.view.frmExportTallyInterface;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmPropertySetup;
import com.POSGlobal.view.frmSalesReports;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSGlobal.view.frmShortCutKeys;
import com.POSGlobal.view.frmStockFlash;
import com.POSGlobal.view.frmTools;
import com.POSMaster.view.frmPrinterSetup;
import com.POSMaster.view.frmPromotionGroupMaster;
import com.POSMaster.view.frmSupplierMaster;
import com.POSReport.view.frmBillWiseSettlementWiseGroupWiseBreakup;
import com.POSReport.view.frmCustomerHistoryFlash;
import com.POSReport.view.frmJioMoneyTransactionFlash;
import com.POSReport.view.frmMailDayEndReports;
import com.POSReport.view.frmPurchaseOrderReport;
import com.POSReport.view.frmSettlementWiseGroupWiseBreakup;
import com.POSTransaction.view.frmBarcodeGeneration;
import com.POSTransaction.view.frmBillForItems;
import com.POSTransaction.view.frmCustomerDisplaySystem;
import com.POSTransaction.view.frmDebitCardBulkRecharge;
import com.POSTransaction.view.frmGenrateMallInterfaceText;
import com.POSTransaction.view.frmKDSForKOT1366x768Resolution;
import com.POSTransaction.view.frmMoveItemsToTable;
import com.POSTransaction.view.frmMultiBillSettle;
import com.POSTransaction.view.frmPostPOSSalesDataToExcise;
import com.POSTransaction.view.frmPostPOSSalesDataToMMS;
import com.POSTransaction.view.frmPurchaseOrder;
import com.POSTransaction.view.frmRegisterInOutPlayZone;
import com.POSTransaction.view.frmSendBulkSMS;
import com.POSTransaction.view.frmShiftEndProcessConsolidate;
import com.POSTransaction.view.frmShowCostCenters;
import com.POSTransaction.view.frmStatistics;
import com.POSTransaction.view.frmTableReservation;
import com.POSTransaction.view.frmUnlockTable;
import com.POSTransaction.view.frmVoidAdvanceOrder;
import com.POSTransaction.view.frmWeraFoodOrders;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class frmMainMenu extends javax.swing.JFrame
{

    private String dteCreated, time, date, sql;
    private String[] actionCom, labelNames;
    private int h, m, sec, d, min, y, lblCount, cntNavigate;
    private java.util.Vector vFormNames, vFormImages;
    private String searchFormText = "";
    private boolean flagSearch = false;
    private StringBuilder stringBuilder = new StringBuilder();
    private clsUtility objUtility;
    public Map<String, String> hmNotifications = new HashMap<String, String>();

    /**
     * This method is used to initialize labels
     *
     * @return array of labels
     */
    public JLabel[] initLabels()
    {

	JLabel[] menuLabels1 =
	{
	    lblMenu1, lblMenu2, lblMenu3, lblMenu4, lblMenu5, lblMenu6, lblMenu7, lblMenu8, lblMenu9, lblMenu10, lblMenu11, lblMenu12, lblMenu13, lblMenu14, lblMenu15, lblMenu16, lblMenu17, lblMenu18
	};
	return menuLabels1;
    }

    private void funSetHeaderImages()
    {
	if (clsGlobalVarClass.gTheme.equalsIgnoreCase("Default"))
	{
	    lblDesktop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgDesktop.png")));
	    lblChangeModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangeModule.png"))); // NOI18N
	    lblChangePOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangePOS.png"))); // NOI18N

	    btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgPreviousButton.png"))); // NOI18N
	    btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgNextButton.png"))); // NOI18N
	} // NOI18N
	else
	{
	    lblDesktop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgDesktop1.png")));
	    lblChangeModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangeModule1.png"))); // NOI18N
	    lblChangePOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangePOS1.png"))); // NOI18N

	    btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgPreviousButton1.png"))); // NOI18N
	    btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgNextButton1.png"))); // NOI18N
	}
    }

    /**
     * This class is used to handle keyboard
     */
    private class KeyBoardEvent implements KeyEventDispatcher
    {

	@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
	    if (e.getID() == KeyEvent.KEY_PRESSED)
	    {
		if (e.getKeyCode() == 112)
		{
		    txtFormSearch.requestFocus();
		}
		else if (e.getKeyCode() == 27)
		{

		    txtFormSearch.setText("");
		    flagSearch = false;
		}
	    }
	    return false;
	}
    }

    /**
     * This method is used to initialize frmMainMenu
     */
    public frmMainMenu()
    {

	initComponents();

	objUtility = new clsUtility();

	funSetHeaderImages();

	lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	stringBuilder.setLength(0);
	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	manager.addKeyEventDispatcher(new KeyBoardEvent());
	this.setState(Frame.ICONIFIED);
	this.setState(Frame.NORMAL);
	boolean flgHOConn = clsGlobalVarClass.funGetConnectionStatus();

	if (flgHOConn)
	{
	    lblHOSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgGreenSymbol.png")));
	}
	else if (clsGlobalVarClass.gHOPOSType.equals("Client POS"))
	{
	    lblHOSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgRedSymbol.png")));
	}
	try
	{

	    objUtility.funFillUserForms(clsGlobalVarClass.gUserCode);

	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    clsGlobalVarClass.tickTockPosDate(lblDate);
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();

	    funSetIndexLabel(0);

	    /**
	     * HO Master syncing..........
	     */
	    clsGlobalVarClass.funFetchMasterDataFromHO();

	    sql = "select count(*) from tblsettelmenthd";
	    ResultSet settRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    settRs.next();
	    if (settRs.getInt(1) > 0)
	    {
		settRs.close();
		clsGlobalVarClass.gShifts = false;
		sql = "select count(intShiftCode) from tblshiftmaster where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'";
		ResultSet rsShiftCodeCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsShiftCodeCount.next())
		{
		    if (rsShiftCodeCount.getInt(1) > 0)
		    {
			clsGlobalVarClass.gShifts = true;
		    }
		    else
		    {
			clsGlobalVarClass.gShifts = false;
		    }
		}

		sql = "select count(*) from tbldayendprocess "
			+ " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strDayEnd='N' "
			+ " and (strShiftEnd='' or strShiftEnd='N')";
		//System.out.println(sql);
		settRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		settRs.next();
		int countEnd = settRs.getInt(1);
		if (countEnd > 0)
		{
		    sql = "select date(max(dtePOSDate)),intShiftCode,strShiftEnd,strDayEnd"
			    + " from tbldayendprocess "
			    + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and strDayEnd='N' "
			    + " and (strShiftEnd='' or strShiftEnd='N')";
		    settRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    settRs.next();
		    clsGlobalVarClass.setStartDate(settRs.getString(1));
		    clsGlobalVarClass.gShiftNo = settRs.getInt(2);
		    clsGlobalVarClass.gShiftEnd = settRs.getString(3);
		    clsGlobalVarClass.gDayEnd = settRs.getString(4);
		    if (clsGlobalVarClass.gShiftNo == 0)
		    {
			//JOptionPane.showMessageDialog(this,"Please Start the shift");
		    }
		    else
		    {
			sql = "select intShiftCode,strBillDateTimeType from tblshiftmaster "
				+ "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and "
				+ "intShiftCode=" + clsGlobalVarClass.gShiftNo;
			ResultSet rsShiftInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsShiftInfo.next())
			{
			    clsGlobalVarClass.gBillDateTimeType = rsShiftInfo.getString(1);
			}
			rsShiftInfo.close();
		    }
		    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date bDate = dFormat.parse(settRs.getString(1));
		    long posTime = bDate.getTime();
		    Date currDate = new Date();
		    long currTime = currDate.getTime();
		    long diffTime = currTime - posTime;
		    long diffDays = diffTime / (24 * 60 * 60 * 1000);
		    if (clsGlobalVarClass.gChangeModule.equals("N"))
		    {
			if (diffDays != 0)
			{
			    new frmOkPopUp(null, "POS Date is not equal to System Date", "Warning", 1).setVisible(true);
			}
		    }
		}
		else
		{
		    Date endDt = new Date();
		    String todayDate = (endDt.getYear() + 1900) + "-" + (endDt.getMonth() + 1) + "-" + endDt.getDate();
		    java.util.Date curDt = new java.util.Date();
		    d = curDt.getDate();
		    m = curDt.getMonth() + 1;
		    y = curDt.getYear() + 1900;
		    h = curDt.getHours();
		    min = curDt.getMinutes();
		    sec = curDt.getSeconds();
		    time = h + ":" + min + ":" + sec;
		    date = y + "-" + m + "-" + d;
		    dteCreated = date + " " + time;
		    clsGlobalVarClass.setStartDate(todayDate);
		    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date bDate = dFormat.parse(todayDate);
		    long posTime = bDate.getTime();
		    Date currDate = new Date();
		    long currTime = currDate.getTime();
		    long diffTime = currTime - posTime;
		    long diffDays = diffTime / (24 * 60 * 60 * 1000);
		    sql = "insert into tbldayendprocess(strPOSCode,dtePOSDate,strDayEnd,strShiftEnd,strUserCreated,dteDateCreated)"
			    + " values('" + clsGlobalVarClass.gPOSCode + "','" + todayDate + "','N','','" + clsGlobalVarClass.gUserCode
			    + "','" + dteCreated + "')";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    clsGlobalVarClass.gShiftEnd = "";
		    clsGlobalVarClass.gDayEnd = "N";
		    if (clsGlobalVarClass.gChangeModule.equals("N"))
		    {
			if (diffDays != 0)
			{
			    new frmOkPopUp(null, "POS Date is not equal to System Date", "Warning", 1).setVisible(true);
			}
		    }
		}
		settRs.close();
	    }
	    else
	    {
		StringBuilder strBuilder = new StringBuilder("INSERT INTO `tblsettelmenthd` "
			+ "(`strSettelmentCode`, `strSettelmentDesc`, `strSettelmentType`, "
			+ "`strApplicable`, `strBilling`, `strAdvanceReceipt`, `dblConvertionRatio`, "
			+ "`strUserCreated`, `strUserEdited`, `dteDateCreated`, `dteDateEdited`, "
			+ "`strClientCode`, `strDataPostFlag`, `strAccountCode`, `strBillPrintOnSettlement`,"
			+ " `strCreditReceiptYN`, `dblThirdPartyComission`, `strComissionType`, "
			+ "`strComissionOn`,`strCustomerSelectionOnBillSettlement`) "
			+ " VALUES('S01', 'CASH', 'Cash', 'Yes', 'Yes', 'null', 1.00, 'SANGUINE', 'SANGUINE', '"+clsGlobalVarClass.getCurrentDateTime()+"', '"+clsGlobalVarClass.getCurrentDateTime()+"', '"+clsGlobalVarClass.gClientCode+"', 'N', '', 'N', 'N', 0.00, 'Per', 'Net Amount','N');");
		clsGlobalVarClass.dbMysql.execute(strBuilder.toString());
//		new frmOkPopUp(null, "Settlement Type is not Present", "Warning", 1).setVisible(true);
//                new com.POSMaster.view.frmSettlementMaster().setVisible(true);
//                clsGlobalVarClass.hmActiveForms.put("Settlement", "Settlement");

	    }

	    clsGlobalVarClass.funSetPOSDate();
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    lblDate.setText(DateFormat.getDateTimeInstance().format(new Date()));

	    vFormNames = new java.util.Vector();
	    vFormImages = new java.util.Vector();

	    /**
	     * load forms
	     */
	    funInitForms();
	    funAddPLUItemDTL();

	    File makeFolder = new File("UserImageIcon");
	    if (!makeFolder.exists())
	    {
		makeFolder.mkdir();
	    }
	    String sqlImgLogout = "select imgUserIcon from tbluserhd where strUserCode='" + clsGlobalVarClass.gUserCode + "' ";
	    ResultSet rsImgLogout = clsGlobalVarClass.dbMysql.executeResultSet(sqlImgLogout);

	    if (rsImgLogout.next())
	    {
		if (rsImgLogout.getString(1).equals(""))
		{
		    if (clsGlobalVarClass.gTheme.equalsIgnoreCase("Default"))
		    {
			lblLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgLogOut.png")));
		    }
		    else
		    {
			lblLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgLogOut1.png")));
		    }
		}
		else
		{
		    // String Path=System.getProperty("user.dir")+"\\UserImageIcon\\";
		    File fileUserImage = new File(System.getProperty("user.dir") + "\\UserImageIcon\\" + clsGlobalVarClass.gUserCode + ".jpg");
		    if (fileUserImage.exists())
		    {
			fileUserImage.delete();
		    }
		    InputStream inImg = rsImgLogout.getBinaryStream(1);
		    OutputStream outImg = new FileOutputStream(fileUserImage);

		    int c = 0;
		    while ((c = inImg.read()) > -1)
		    {
			outImg.write(c);
		    }
		    outImg.close();
		    inImg.close();
		    lblLogOut1.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/UserImageIcon/" + clsGlobalVarClass.gUserCode + ".jpg"));
		}
	    }
	    else
	    {

		if (clsGlobalVarClass.gTheme.equalsIgnoreCase("Default"))
		{
		    lblLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgLogOut.png")));
		}
		else
		{
		    lblLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgLogOut1.png")));
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    this.dispose();
	    new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
	    new frmTools("startup").setVisible(true);
	}
    }

    public static void funResetMainMenuFormLabels()
    {

    }

    /**
     * This method is used to add PLU item DTL
     */
    private void funAddPLUItemDTL()
    {
	clsPLUItemDtl objPLUItemDtl = new clsPLUItemDtl();
	objPLUItemDtl.funPLUHashMap();
    }

    /**
     * This method is used to funSetindexLabel
     *
     * @param startIndex
     */
    private void funSetIndexLabel(int startIndex)
    {
	lbl1.setEnabled(false);
	lbl3.setEnabled(false);
	lbl2.setEnabled(false);
	lbl4.setEnabled(false);
	lbl5.setEnabled(false);

	startIndex += 18;
	int index = startIndex / 18;
	switch (index)
	{
	    case 1:
		lbl1.setEnabled(true);
		break;

	    case 2:
		lbl2.setEnabled(true);
		break;

	    case 3:
		lbl3.setEnabled(true);
		break;

	    case 4:
		lbl4.setEnabled(true);
		break;

	    case 5:
		lbl5.setEnabled(true);
		break;

	    default:
		lbl5.setEnabled(true);
		break;
	}
    }

    /**
     * This method is used to check navigation page
     *
     * @param vForms
     */
    private void funCheckNavigationPage()
    {
	double size = vFormNames.size();
	double totsize = Math.ceil(size / 18);
	int finalPageSize = (int) Math.round(totsize);
	lbl1.setVisible(false);
	lbl2.setVisible(false);
	lbl3.setVisible(false);
	lbl4.setVisible(false);
	lbl5.setVisible(false);

	switch (finalPageSize)
	{
	    case 1:
		lbl1.setVisible(true);
		break;

	    case 2:
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		break;

	    case 3:
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		break;

	    case 4:
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl4.setVisible(true);
		break;

	    case 5:
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl4.setVisible(true);
		lbl5.setVisible(true);
		break;

	    default:
		lbl1.setVisible(true);
		lbl2.setVisible(true);
		lbl3.setVisible(true);
		lbl4.setVisible(true);
		lbl5.setVisible(true);
		break;
	}
    }

    /**
     * This method is used to draw page
     */
    public void funDrawPage(int startIndex, int endIndex)
    {
	try
	{
	    int cntLabel = 0;
	    JLabel[] menuLabels = initLabels();
	    for (int i = startIndex; i < endIndex; i++)
	    {
		if (i == vFormNames.size())
		{
		    break;
		}
		if (cntLabel < 18)
		{
		    //System.out.println(labelNames[i] + ".png");
		    if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
		    {
			menuLabels[cntLabel].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/" + labelNames[i] + ".png")));
		    }
		    else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
		    {
			menuLabels[cntLabel].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/" + labelNames[i] + ".png")));
		    }
		    else
		    {
			menuLabels[cntLabel].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSReport/images/" + labelNames[i] + ".png")));
		    }

		    menuLabels[cntLabel].setEnabled(true);
		    menuLabels[cntLabel].setVisible(true);
		    actionCom[cntLabel] = vFormNames.elementAt(i).toString();
		    cntLabel++;
		}
	    }
	    for (int i = cntLabel; i < 18; i++)
	    {
		menuLabels[i].setEnabled(false);
		menuLabels[i].setVisible(false);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to search form and display it
     *
     * @param formName
     */
    private void funFormLabelClicked(String formName)
    {

	if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
	{
	    funLoadMasterForms(formName);
	}
	else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
	{
	    funLoadTransactionForms(formName);
	}
	else
	{
	    funLoadReportForms(formName);
	}
    }

    private void funLoadMasterForms(String formName)
    {
	try
	{
	    if (clsGlobalVarClass.hmActiveForms.containsKey(formName))
	    {
		return;
	    }
	    switch (formName)
	    {
		case "Cost Center":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmCostCenterMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Group":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmGroupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "POS Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.spos.controller.frmPOSMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }

		    break;

		case "Advance Order Type Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmAdvanceOrderTypeMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Area Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmAreaMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Customer Area Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmCustAreaMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "DebitCardMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmDebitCardMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Bulk Menu Item Pricing":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmBulkMenuItemPricing().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "CounterMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmCounterMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "CustomerTypeMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmCustomerTypeMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Home Delivery Person":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmDeliveryPersonMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Item Modifier":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmItemModifierMast().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "GiftVoucherMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmGiftVoucherMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Menu Head":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmMenuHeadMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Promotion Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmPromotionMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Reason Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmReasonMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Settlement":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmSettlementMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Shift Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmShiftMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "SubGroup":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmSubGroupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Table Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmTableMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Tax Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmTaxMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "TDH":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmTDH().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "User Registration":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmUserMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Waiter Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmWaiterMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "LoyaltyPoints":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmLoyaltyPointMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Menu Item":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmMenuItemMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "ModifierGroupMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmModifierGroupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Price Menu":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmMenuItemPricing().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "RecipeMaster":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmRecipeMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "DebitCardRegister":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmRegisterDebitCard().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Delivery Boy Category Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmDeliveryBoyCategoryMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Tools":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {

			new frmTools("").setVisible(true);
			//  clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "AreaWiseDBoyCharges":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmAreaWiseDeliveryBoyCharges().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Zone Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmZoneMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Property Setup":
		    new frmPropertySetup().setVisible(true);
		    //clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Sales Report":
		    new frmSalesReports().setVisible(true);
		    // clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Stock Flash Report":
		    new frmStockFlash().setVisible(true);
		    // clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Shortcut Key Setup":
		    new frmShortCutKeys().setVisible(true);
		    // clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "UserCardSwipe":
		    new com.POSMaster.view.frmUserCardSwipe().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Arrange Transaction":
		    new com.POSMaster.view.frmArrangeTransaction().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Order Master":
		    new com.POSMaster.view.frmOrderMaster().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Characterstics Master":
		    new com.POSMaster.view.frmCharactersticsMaster().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Item Wise Incentives":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmPOSWiseItemIncentive().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Factory Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmFactoryMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Linkup Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmLinkupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Tally Linkup Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmTallyLinkupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;
		case "Export Tally Interface":

		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new frmExportTallyInterface().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Change Password":

		    new frmChangePassword().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Promotion Group Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new frmPromotionGroupMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Supplier Master":

		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new frmSupplierMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }
		case "User Group Rights":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmUserGroupRights().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "UOM Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmUomMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }
		case "Discount Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmDiscountMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Bill Series Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmBillSeriesMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }
		case "Payment Interface Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmPaymentInterfaceMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }
		case "PlayZone Pricing Master":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new com.POSMaster.view.frmPlayZonePricingMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Printer Setup":
		    if (!clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("Client POS"))
		    {
			new frmPrinterSetup().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }
		
		
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadTransactionForms(String formName)
    {
	try
	{
	    String formKey = formName;
	    if (formName.equals("SettleBill") || formName.equals("Modify Bill") || formName.equals("BillFromKOTs"))
	    {
		formKey = "SettleBill";
	    }
	    if (formName.equals("Day End") || formName.equals("DayEndWithoutDetails") || formName.equals("ShiftEndProcessConsolidate"))
	    {
		formKey = "Day End";
	    }

	    if (clsGlobalVarClass.hmActiveForms.containsKey(formKey))
	    {
		return;
	    }

	    switch (formName)
	    {
		case "Direct Biller":

		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
			    {
				if (funSelectCounterCode())
				{
				    clsGlobalVarClass.gTransactionType = "Direct Biller";
				    new com.POSTransaction.view.frmDirectBiller().setVisible(true);
				    clsGlobalVarClass.hmActiveForms.put(formName, formName);
				}
			    }
			    else
			    {
				clsGlobalVarClass.gTransactionType = "Direct Biller";
				new com.POSTransaction.view.frmDirectBiller().setVisible(true);
				clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    }
			}
		    }
		    break;

		case "Make KOT":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    if (clsGlobalVarClass.gCounterWise.equals("Yes"))
			    {
				if (funSelectCounterCode())
				{
				    clsGlobalVarClass.gTransactionType = "Make KOT";
				    new com.POSTransaction.view.frmMakeKOT().setVisible(true);
				    clsGlobalVarClass.hmActiveForms.put(formName, formName);
				}
			    }
			    else
			    {
				clsGlobalVarClass.gTransactionType = "Make KOT";
				new com.POSTransaction.view.frmMakeKOT().setVisible(true);
				clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    }
			}
		    }
		    break;

		case "Make Bill":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "Make Bill";
			    new com.POSTransaction.view.frmMakeBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "SettleBill":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "SettleBill";
			    new com.POSTransaction.view.frmBillSettlement().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("SettleBill", formName);
			}
		    }
		    break;

		case "Modify Bill":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "ModifyBill";
			    new com.POSTransaction.view.frmBillSettlement().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("SettleBill", formName);
			}
		    }
		    break;

		case "ChangeCustomerOnBill":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "ChangeCustomerOnBill";
			    new com.POSTransaction.view.frmChangeCustomerOnBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "BillFromKOTs":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "Bill From KOT";
			    new com.POSTransaction.view.frmBillSettlement(1).setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("SettleBill", formName);
			}
		    }
		    break;

		case "Add KOT To Bill":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    new com.POSTransaction.view.frmAddKOTToBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Advance Order":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    clsGlobalVarClass.gTransactionType = "Advance Order";
			    new com.POSTransaction.view.frmAdvanceOrder().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Advance Booking Receipt":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
			{
			    JOptionPane.showMessageDialog(this, "Please start Day");
			    return;
			}
			else
			{
			    new com.POSTransaction.view.frmAdvanceReceipt("StartMenu", false).setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Void Bill":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmVoidBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "VoidKot":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmVoidKot().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Unsettle Bill":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmUnsettleBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Stock In":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmStkIn("StkIn").setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Stock Out":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			new com.POSTransaction.view.frmStkIn("StkOut").setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    }
		    break;

		case "Physical Stock Posting":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmPhysicalStk().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Stock Adujstment":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {

			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmStkAdjustment().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Move Table":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmMoveTable().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Move KOT":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmMoveKOT().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "SplitBill":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmSplitBill().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Tax Regeneration":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmTaxRegeneration().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Close Producion Order":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmCloseProductionOrder().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Day End":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			clsGlobalVarClass.gTransactionType = "ShiftEnd";

			if (clsGlobalVarClass.gShifts == true && clsGlobalVarClass.gShiftNo != 0)
			{
			    new com.POSTransaction.view.frmShiftEndProcess().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
			else if (clsGlobalVarClass.gShiftNo >= 0 && clsGlobalVarClass.gShifts == false)
			{
			    new com.POSTransaction.view.frmShiftEndProcess().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
			else if (clsGlobalVarClass.gShiftNo == 0 && clsGlobalVarClass.gShifts == true)
			{
			    new com.POSTransaction.view.frmShiftEndProcess().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
			else
			{
			    JOptionPane.showMessageDialog(this, "Shift is not created");
			}
		    }
		    break;

		case "DayEndWithoutDetails":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			clsGlobalVarClass.gTransactionType = "ShiftEndWithoutDetails";

			if (clsGlobalVarClass.gShifts == true && clsGlobalVarClass.gShiftNo != 0)
			{
			    new com.POSTransaction.view.frmShiftEndWithoutDetails().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("Day End", formName);
			}
			else if (clsGlobalVarClass.gShiftNo >= 0 && clsGlobalVarClass.gShifts == false)
			{
			    new com.POSTransaction.view.frmShiftEndWithoutDetails().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("Day End", formName);
			}
			else if (clsGlobalVarClass.gShiftNo == 0 && clsGlobalVarClass.gShifts == true)
			{
			    new com.POSTransaction.view.frmShiftEndWithoutDetails().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put("Day End", formName);
			}
			else
			{
			    JOptionPane.showMessageDialog(this, "Shift is not created");
			}
		    }
		    break;

		case "Cash Management":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmCashManagement().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "VoidStock":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmVoidStock().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "MultiCostCenterKDS":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmMultiCostCenterKDS().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Post Sale Data":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmPostDataToHO().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Post POS Data To CMS":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmPostPOSDataToCMS().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Kitchen System":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmKitchenDisplaySystem().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Reprint":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmReprintDocs().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "GiftVoucherIssue":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmGiftVoucherIssue().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "ImportExcel":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmImportExcelFile().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "AssignHomeDelivery":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmAssignHomeDelivery().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "Property Setup":
		    new frmPropertySetup().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Tools":
		    new frmTools("").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Customer Master":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmCustomerMaster().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Sales Report":
		    new frmSalesReports().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Stock Flash Report":
		    new frmStockFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Table Reservation":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmTableReservation().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "Statistics":
		    new frmStatistics().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Shortcut Key Setup":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmShortCutKeys().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "VoidAdvanceOrder":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmVoidAdvanceOrder().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "PostPOSSalesDataToMMS":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmPostPOSSalesDataToMMS().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "ShiftEndProcessConsolidate":
		    new frmShiftEndProcessConsolidate().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put("Day End", formName);
		    break;

		case "CustomerDisplaySystem":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new frmCustomerDisplaySystem().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "GenrateMallInterfaceText":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new frmGenrateMallInterfaceText().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "SendBulkSMS":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new frmSendBulkSMS().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "TableStatusReport":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmTableStatusReport().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "RechargeDebitCard":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmRechargeDebitCard().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "ShowCard":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmShowCard().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "KDSBookAndProcess":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmKDSBookAndProcess().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Import Database":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmImportDatabase().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Place Order":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmPlaceOrder().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "Pull Order":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmPullOrder().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "CW Interface":

		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			new com.POSTransaction.view.frmCocktailWorldInterface().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Move KOT Items":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmMoveKOTItemToTable().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			}
		    }
		    break;

		case "KDSForKOTBookAndProcess":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			clsGlobalVarClass.hmActiveForms.put(formName, formName);

			if (clsGlobalVarClass.gClientCode.equals("252.001") || clsGlobalVarClass.gClientCode.equals("239.001") || clsGlobalVarClass.gClientCode.equals("118.001"))//URBO//Delhi Swad(252.001)
			{
			    frmKDSForKOT1366x768Resolution objKDSForKOT1366x768Resolution = new frmKDSForKOT1366x768Resolution();

			    frmShowCostCenters objShowCostCenters = new frmShowCostCenters(objKDSForKOT1366x768Resolution);
			    objShowCostCenters.setVisible(true);

			}
			else
			{
			    clsUtility obj = new clsUtility();
			    obj.funCallForSearchForm("CostCenter");
			    new frmSearchFormDialog(this, true).setVisible(true);
			    if (clsGlobalVarClass.gSearchItemClicked)
			    {
				Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();

				String costCenterCode = data[0].toString();
				String costCenterName = data[1].toString();

				new com.POSTransaction.view.frmKDSForKOTBookAndProcess(costCenterCode, costCenterName).setVisible(true);
			    }
			}
			break;
		    }
		case "Export Tally Interface":
		    new frmExportTallyInterface().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Barcode Generation":
		    new frmBarcodeGeneration().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "PostPOSSalesDataToExcise":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmPostPOSSalesDataToExcise().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "Multi Bill Settle":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmMultiBillSettle().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "JioMoney Refund":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmJioMoneyRefund().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "Credit Bill Receipt":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmCreditBillReceipt().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }
		case "Non Available Items":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmNonAvailableItems().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		case "Change Settlement":
		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new com.POSTransaction.view.frmChangeSettlement().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }
		case "Change Password":
		    new frmChangePassword().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Purchase Order":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			new frmPurchaseOrder().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Unlock Table":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			new frmUnlockTable().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Move Items To Table":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			new frmMoveItemsToTable().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "Bill For Items":
		    if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
		    {
			new frmBillForItems().setVisible(true);
			clsGlobalVarClass.hmActiveForms.put(formName, formName);
			break;
		    }

		case "RegisterInOutPlayZone":

		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmRegisterInOutPlayZone().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

		    
		case "Wera Food Online Orders":

		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmWeraFoodOrders().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }
		    
		    
		    case "Debit Card Bulk Recharge":

		    if (clsGlobalVarClass.gShiftEnd.equals("") && clsGlobalVarClass.gDayEnd.equals("N"))
		    {
			JOptionPane.showMessageDialog(this, "Please start Day");
			return;
		    }
		    else
		    {
			if (!clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
			{
			    new frmDebitCardBulkRecharge().setVisible(true);
			    clsGlobalVarClass.hmActiveForms.put(formName, formName);
			    break;
			}
		    }

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadReportForms(String formName)
    {
	try
	{
	    if (clsGlobalVarClass.hmActiveForms.containsKey(formName))
	    {
		return;
	    }
	    System.out.println(formName);
	    switch (formName)
	    {
		case "AvgItemPerBill":
		    new com.POSReport.view.frmAIPB().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "AvgPerCover":
		    new com.POSReport.view.frmAPC().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "AvgTicketValue":
		    new com.POSReport.view.frmATV().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Audit Flash":
		    new com.POSReport.view.frmAuditFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Cost Centre Report":
		    new com.POSReport.view.frmCostCenterReport().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Auditor Report":
		    new com.POSReport.view.frmSalesMenuReport("Auditor Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Bill Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("Bill Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Complimentary Settlement Report":
		    new com.POSReport.view.frmSalesMenuReport("Complimentary Settlement Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Counter Wise Sales Report":
		    new com.POSReport.view.frmSalesMenuReport("Counter Wise Sales Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Discount Report":
		    new com.POSReport.view.frmSalesMenuReport("Discount Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Group Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("Group Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "SubGroupWise Report":
		    new com.POSReport.view.frmSalesMenuReport("SubGroup Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Group-SubGroup Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("GroupSubGroup Wise Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Item Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("Item Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Non Chargable KOT Report":
		    new com.POSReport.view.frmSalesMenuReport("Non Chargable KOT Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "OperatorWise Report":
		    new com.POSReport.view.frmSalesMenuReport("Operator Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Order Analysis Report":
		    new com.POSReport.view.frmSalesMenuReport("Order Analysis Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "SettlementWise Report":
		    new com.POSReport.view.frmSalesMenuReport("Settlement Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Tax Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("Tax Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Void Bill Report":
		    new com.POSReport.view.frmSalesMenuReport("Void Bill Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Tax Breakup Summary Report":
		    new com.POSReport.view.frmSalesMenuReport("Tax Breakup Summary Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Menu Head Wise":
		    new com.POSReport.view.frmSalesMenuReport("Menu Head Wise").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "WaiterWiseItemReport":
		    new com.POSReport.view.frmSalesMenuReport("Waiter Wise Item Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "WaiterWiseIncentivesReport":
		    new com.POSReport.view.frmSalesMenuReport("Waiter Wise Incentives Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Day End Flash":
		    new com.POSReport.view.frmDayEndFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Advance Order Flash":
		    new com.POSReport.view.frmAdvanceOrderFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Promotion Flash":
		    new com.POSReport.view.frmPromotionFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Loyalty Point Report":
		    new com.POSReport.view.frmLoyaltyPointReport().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "DebitCardFlashReports":
		    new com.POSReport.view.frmDebitCardFlashReports().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Property Setup":
		    new frmPropertySetup().setVisible(true);
		    break;

		case "Tools":
		    new frmTools("").setVisible(true);
		    break;

		case "Sales Report":
		    new frmSalesReports().setVisible(true);
		    break;

		case "Stock In Out Flash":
		    new com.POSReport.view.frmStkInOutFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Stock Flash Report":
		    new frmStockFlash().setVisible(true);
		    break;

		case "Cash Mgmt Report":
		    new com.POSReport.view.frmCashMgmtFlash("true*true").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "DeliveryboyIncentive":
		    new com.POSReport.view.frmSalesMenuReport("Delivery boy Incentives Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Sales Summary Flash":
		    new com.POSReport.view.frmSalesSummaryFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "POS Wise Sales":
		    new com.POSReport.view.frmPOSWiseSalesComparison().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Daily Collection Report":
		    new com.POSReport.view.frmSalesMenuReport("Daily Collection Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Daily Sales Report":
		    new com.POSReport.view.frmSalesMenuReport("Daily Sales Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Void KOT Report":
		    new com.POSReport.view.frmSalesMenuReport("Void KOT Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Shortcut Key Setup":
		    new frmShortCutKeys().setVisible(true);
		    break;

		case "Guest Credit Report":
		    new com.POSReport.view.frmSalesMenuReport("Guest Credit Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "SubGroupWiseSummaryReport":
		    new com.POSReport.view.frmSalesMenuReport("SubGroupWiseSummaryReport").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "UnusedCardBalanceReport":
		    new com.POSReport.view.frmSalesMenuReport("UnusedCardBalanceReport").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "DayWiseSalesSummaryFlash":
		    new com.POSReport.view.frmDayWiseSalesSummaryFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "BillWiseSettlementSalesSummaryFlash":
		    new com.POSReport.view.frmBillWiseSettlementSalesSummaryFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Revenue Head Wise Item Sales":
		    new com.POSReport.view.frmSalesMenuReport("Revenue Head Wise Item Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Item Wise Consumption":
		    new com.POSReport.view.frmSalesMenuReport("Item Wise Consumption").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Managers Report":
		    new com.POSReport.view.frmManagersReport().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Manager Summary Flash":
		    new com.POSReport.view.frmManagerSummaryFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Table Wise Pax Report":
		    new com.POSReport.view.frmSalesMenuReport("Table Wise Pax Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Posting Report":
		    new com.POSReport.view.frmSalesMenuReport("Posting Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Placed Order Report":
		    new com.POSReport.view.frmSalesMenuReport("Placed Order Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Advance Order Report":
		    new com.POSReport.view.frmSalesMenuReport("Advance Order Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Void Advance Order Report":
		    new com.POSReport.view.frmSalesMenuReport("Void Advance Order Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "PhysicalStockFlash":
		    new com.POSReport.view.frmPhysicalStockFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Reprint Docs Report":
		    new com.POSReport.view.frmReprintDocsReport().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Waiter Wise Item Wise Incentives Report":
		    new com.POSReport.view.frmSalesMenuReport("Waiter Wise Item Wise Incentives Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Item Master Listing Report":
		    new com.POSReport.view.frmSalesMenuReport("Item Master Listing Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;
		case "Export Tally Interface":
		    new frmExportTallyInterface().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Customer History Flash Report":
		    new frmCustomerHistoryFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Delivery Boy Wise Cash Taken":
		    new com.POSReport.view.frmSalesMenuReport("Delivery Boy Wise Cash Taken").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "JioMoney Transacttion Flash":
		    new frmJioMoneyTransactionFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Mail Day End Reports":
		    new frmMailDayEndReports().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Credit Bill Outstanding Report":
		    new com.POSReport.view.frmSalesMenuReport("Credit Bill Outstanding Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;
		case "Change Password":
		    new frmChangePassword().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;
		case "Settlement Wise Group Wise Breakup":
		    new frmSettlementWiseGroupWiseBreakup().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Bill Wise Settlement Wise Group Wise Breakup":
		    new frmBillWiseSettlementWiseGroupWiseBreakup().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Purchase Order Report":
		    new frmPurchaseOrderReport().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "KDS Flash":
		    new com.POSReport.view.frmKDSFlash().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Food Costing":
		    new com.POSReport.view.frmFoodCosting().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Blind Settlement Wise Report":
		    new com.POSReport.view.frmSalesMenuReport("Blind Settlement Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;
		case "Gross Sales Summary":
		    new com.POSReport.view.frmGrossSalesSummary().setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Open Item Wise Audit Report":
		    new com.POSReport.view.frmSalesMenuReport("Open Item Wise Audit Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Non Selling Items":
		    new com.POSReport.view.frmSalesMenuReport("Non Selling Items").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Debtors As On":
		    new com.POSReport.view.frmSalesMenuReport("Debtors As On").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Payment Receipt Report":
		    new com.POSReport.view.frmSalesMenuReport("Payment Receipt Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Credit Report":
		    new com.POSReport.view.frmSalesMenuReport("Credit Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Consolidated Discount Report":
		    new com.POSReport.view.frmSalesMenuReport("Consolidated Discount Report").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Customer Ledger":
		    new com.POSReport.view.frmSalesMenuReport("Customer Ledger").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;

		case "Area Wise Group Wise Sales":
		    new com.POSReport.view.frmSalesMenuReport("Area Wise Group Wise Sales").setVisible(true);
		    clsGlobalVarClass.hmActiveForms.put(formName, formName);
		    break;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to select counter code
     *
     * @return
     */
    private boolean funSelectCounterCode() throws Exception
    {
	boolean flgCounter = false;

	sql = "select strCounterCode,strCounterName from tblcounterhd "
		+ " where strUserCode='" + clsGlobalVarClass.gUserCode + "' ";
	ResultSet rsCounter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsCounter.next())
	{
	    clsGlobalVarClass.gCounterCode = rsCounter.getString(1);
	    clsGlobalVarClass.gCounterName = rsCounter.getString(2);
	    clsPLUItemDtl objPLUItemDtl = new clsPLUItemDtl();
	    objPLUItemDtl.funPLUHashMap(clsGlobalVarClass.gCounterCode);
	    clsGlobalVarClass.gSearchItemClicked = false;
	    flgCounter = true;
	}
	else
	{
	    objUtility.funCallForSearchForm("CounterForOperation");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		//setDataForCounterCode(data);
		clsGlobalVarClass.gCounterCode = data[0].toString();
		clsGlobalVarClass.gCounterName = data[1].toString();
		clsPLUItemDtl objPLUItemDtl = new clsPLUItemDtl();
		objPLUItemDtl.funPLUHashMap(clsGlobalVarClass.gCounterCode);
		clsGlobalVarClass.gSearchItemClicked = false;
		flgCounter = true;
	    }
	}
	rsCounter.close();
	return flgCounter;
    }

    /**
     * get forms
     *
     * @param searchText
     */
    private void funGetForm(String searchText)
    {
	searchFormText = searchText;
	funInitForms();
    }

    /**
     * This method is used to initialize forms
     */
    private void funInitForms()
    {
	try
	{

	    clsStructureUpdater objStructureUpdater = new clsStructureUpdater();
	    stringBuilder.setLength(0);
	    stringBuilder.append("show tables  like 'tblforms' ");
	    ResultSet rsIsTableExists = clsGlobalVarClass.dbMysql.executeResultSet(stringBuilder.toString());
	    if (rsIsTableExists.next())
	    {
		stringBuilder.setLength(0);
		stringBuilder.append(" select * from tblforms limit 1 ");
		ResultSet rsIsTableBlank = clsGlobalVarClass.dbMysql.executeResultSet(stringBuilder.toString());
		if (!rsIsTableBlank.next())
		{
		    objStructureUpdater.funUpdateTblForms();
		}
		rsIsTableBlank.close();
	    }
	    else
	    {
		objStructureUpdater.funUpdateTblForms();
	    }
	    rsIsTableExists.close();

	    stringBuilder.setLength(0);
	    cntNavigate = 0;
	    int formCount = 0;
	    if (clsGlobalVarClass.gSuperUser == true)
	    {
		stringBuilder.append("select count(intSequence) from tblforms where strModuleName like '%").append(searchFormText).append("%'");
	    }
	    else
	    {
		stringBuilder.append("select count(intSequence) from tbluserdtl where strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and strFormName like '%").append(searchFormText).append("%'");
	    }
	    ResultSet rsFormCount = clsGlobalVarClass.dbMysql.executeResultSet(stringBuilder.toString());
	    if (rsFormCount.next())
	    {
		formCount = rsFormCount.getInt(1);
	    }
	    rsFormCount.close();
	    stringBuilder.setLength(0);
	    btnPrev.setEnabled(false);
	    btnNext.setEnabled(false);
	    JLabel[] menuLabels = initLabels();
	    actionCom = new String[formCount];
	    labelNames = new String[formCount];
	    vFormNames.clear();
	    vFormImages.clear();

	    for (int k = 0; k < menuLabels.length; k++)
	    {
		menuLabels[k].setEnabled(false);
		menuLabels[k].setVisible(false);
	    }
	    int nx = 0;
	    lblCount = 0;
	    String imageColumnName = "strImageName";
	    if (clsGlobalVarClass.gTheme.equalsIgnoreCase("Tiles"))
	    {
		imageColumnName = "strColorImageName";
	    }

	    //System.out.println(clsGlobalVarClass.gSelectedModule);
	    if (clsGlobalVarClass.gSanguneUser)
	    {
		stringBuilder.setLength(0);
		if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + " from tblforms a "
			    + " where (a.strModuleType='M' or a.strModuleType='U') "
			    + " and a.strModuleName<>'ReOrderTime' "
			    + " and a.strModuleName<>'Customer Master' "
			    + " and a.strModuleName like '%").append(searchFormText).append("%' ");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + " from tblforms a "
			    + " where (a.strModuleType='T' or a.strModuleType='U' or strModuleName='Customer Master') "
			    + " and a.strModuleName!='NCKOT' "
			    + " and a.strModuleName!='Complimentry Settlement' "
			    + " and a.strModuleName!='Complimentary Items' "
			    + " and a.strModuleName!='Discount On Bill' "
			    + " and a.strModuleName!='NCKOT' "
			    + " and a.strModuleName!='Take Away' "
			    + " and a.strModuleName!='Customer Order' "
			    + " and a.strModuleName!='Mini Make KOT' "
			    + " and a.strModuleName!='Kitchen Process System' "
			    + " and a.strModuleName!='Bill Settlement' "
			    + " and a.strModuleName!='Call Center' "
			    + " and a.strModuleName!='Call Center Order Flash' ");
		    if (!clsGlobalVarClass.gShowUnSettletmentForm)
		    {
			stringBuilder.append(" and a.strModuleName<>'Unsettle Bill' ");
		    }
		    stringBuilder.append(" and a.strModuleName like '%").append(searchFormText).append("%' ");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Reports"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + " from tblforms a "
			    + " where (a.strModuleType='R' or a.strModuleType='U') "
			    + " and a.strModuleName !='Dashboard' "
			    + " and a.strModuleName !='SaleVSPurchase' "
			    + " and a.strModuleName !='Comparisonwise Dashboard' "
			    + " and a.strModuleName !='SubGroupWise Report' "
			    + " and a.strModuleName like '%").append(searchFormText).append("%' ");
		}
	    }
	    else if (clsGlobalVarClass.gSuperUser == true)
	    {
		stringBuilder.setLength(0);
		if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName,a.").append(imageColumnName).append(" from tblforms a,tblsuperuserdtl b "
			    + " where b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and a.strModuleName like '%").append(searchFormText).append("%'  "
			    + " and a.strModuleName=b.strFormName "
			    + " and (a.strModuleType='M' or a.strModuleType='U' ) "
			    + " and a.strModuleName<>'ReOrderTime' "
			    + " and a.strModuleName<>'Customer Master' "
			    + " order by b.intSequence");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName,a.").append(imageColumnName).append(" from tblforms a,tblsuperuserdtl b "
			    + " where b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and a.strModuleName like '%").append(searchFormText).append("%'  "
			    + " and a.strModuleName=b.strFormName and "
			    + " (a.strModuleType='T' or a.strModuleType='U' or strModuleName='Customer Master') "
			    + " and a.strModuleName!='Complimentry Settlement' "
			    + " and a.strModuleName!='Complimentary Items' "
			    + " and a.strModuleName!='Discount On Bill' "
			    + " and a.strModuleName!='NCKOT' "
			    + " and a.strModuleName!='Take Away' "
			    + " and a.strModuleName!='Customer Order' "
			    + " and a.strModuleName!='Mini Make KOT' "
			    + " and a.strModuleName!='Kitchen Process System' "
			    + " and a.strModuleName!='Bill Settlement' "
			    + " and a.strModuleName!='Call Center' "
			    + " and a.strModuleName!='Call Center Order Flash' ");
		    if (!clsGlobalVarClass.gShowUnSettletmentForm)
		    {
			stringBuilder.append(" and a.strModuleName<>'Unsettle Bill' ");
		    }
		    stringBuilder.append(" order by b.intSequence");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Reports"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName,a.").append(imageColumnName).append(" from tblforms a,tblsuperuserdtl b "
			    + " where b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and a.strModuleName like '%").append(searchFormText).append("%'  "
			    + " and a.strModuleName=b.strFormName "
			    + " and (a.strModuleType='R' or a.strModuleType='U' ) "
			    + " and a.strModuleName !='Dashboard' "
			    + " and a.strModuleName !='SaleVSPurchase' "
			    + " and a.strModuleName !='Comparisonwise Dashboard' "
			    + " and a.strModuleName !='SubGroupWise Report' "
			    + " order by b.intSequence");
		}
	    }
	    else
	    {
		stringBuilder.setLength(0);
		if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + ",b.intSequence from tblforms a,tbluserdtl b "
			    + "where b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and a.strModuleName like '%").append(searchFormText).append("%' and a.strModuleName=b.strFormName "
			    + "and (a.strModuleType='M' or a.strModuleType='U' ) and a.strModuleName<>'ReOrderTime' and a.strModuleName<>'Customer Master' "
			    + "and (b.strAdd='true' or b.strEdit='true' or b.strDelete = 'true' or b.strView='true' "
			    + "or b.strPrint = 'true'or b.strSave = 'true' or b.strGrant = 'true') "
			    + "order by b.intSequence ");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + ",b.intSequence "
			    + " from tblforms a,tbluserdtl b "
			    + " where (a.strModuleType='T' or a.strModuleType='U' or a.strModuleName='Customer Master') "
			    + " and a.strModuleName!='Complimentry Settlement' "
			    + " and a.strModuleName!='Complimentary Items' "
			    + " and a.strModuleName!='Discount On Bill' "
			    + " and a.strModuleName!='Customer Order' "
			    + " and a.strModuleName!='Mini Make KOT' "
			    + " and a.strModuleName!='Kitchen Process System' "
			    + " and a.strModuleName!='Bill Settlement' "
			    + " and a.strModuleName!='Call Center' "
			    + " and a.strModuleName!='Call Center Order Flash' ");
		    if (!clsGlobalVarClass.gShowUnSettletmentForm)
		    {
			stringBuilder.append(" and a.strModuleName<>'Unsettle Bill' ");
		    }
		    stringBuilder.append(" and b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' and a.strModuleName like '%").append(searchFormText).append("%' and a.strModuleName=b.strFormName "
			    + " and (b.strAdd='true' or b.strEdit='true' or b.strDelete = 'true' or b.strView='true' "
			    + " or b.strPrint = 'true' or b.strSave = 'true' or b.strGrant = 'true' or b.strTLA='true' )"
			    + " order by  b.intSequence");
		}
		else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Reports"))
		{
		    stringBuilder.append("select DISTINCT a.strModuleName," + imageColumnName + ",b.intSequence "
			    + "from tblforms a,tbluserdtl b "
			    + "where b.strUserCode='").append(clsGlobalVarClass.gUserCode).append("' "
			    + " and ( a.strModuleType='R' or a.strModuleType='U' )"
			    + " and a.strModuleName=b.strFormName "
			    + " and (b.strAdd='true' or b.strEdit='true' or b.strDelete = 'true' or b.strView='true' or b.strPrint = 'true'or b.strSave = 'true' or b.strGrant = 'true') "
			    + " and a.strModuleName !='Dashboard' "
			    + " and a.strModuleName !='SaleVSPurchase' "
			    + " and a.strModuleName !='SubGroupWise Report' "
			    + " and a.strModuleName !='Comparisonwise Dashboard' ");
		    stringBuilder.append(" and a.strModuleName like '%").append(searchFormText).append("%' ");
		    stringBuilder.append(" order by b.intSequence ");
		}
	    }

	    //System.out.println(sb);
	    ResultSet dRs = clsGlobalVarClass.dbMysql.executeResultSet(stringBuilder.toString());
	    while (dRs.next())
	    {
		labelNames[nx] = dRs.getString(2);
		vFormNames.add(dRs.getString(1));
		vFormImages.add(dRs.getString(2));
		nx++;
		lblCount++;
	    }
	    funCheckNavigationPage();
	    for (int k = 0; k < lblCount; k++)
	    {
		actionCom[k] = vFormNames.elementAt(k).toString();
	    }
	    if (vFormNames.size() > 18)
	    {
		btnNext.setEnabled(true);
		funDrawPage(0, 18);
	    }
	    else
	    {
		funDrawPage(0, vFormNames.size());
	    }
	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("Super"))
	    {
		for (int t = 0; t < menuLabels.length; t++)
		{
		    menuLabels[t].setEnabled(true);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funNextMenuScreenButtonPressed()
    {
	if (btnNext.isEnabled())
	{
	    try
	    {
		cntNavigate++;
		int startIndex = cntNavigate * 18;
		int endIndex = startIndex + 18;
		int div = vFormNames.size() / 18;
		int mod = vFormNames.size() % 18;

		funDrawPage(startIndex, endIndex);
		btnPrev.setEnabled(true);
		if (div == cntNavigate || mod < 0)
		{
		    btnNext.setEnabled(false);
		    btnPrev.requestFocus();
		}
		if (endIndex >= vFormNames.size())
		{
		    btnNext.setEnabled(false);
		    btnPrev.requestFocus();
		}
		funSetIndexLabel(startIndex);
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
    }

    private void funPreviousMenuScreenButtonPressed()
    {
	cntNavigate--;
	int startIndex = 0;
	int endIndex = 18;
	if (cntNavigate == 0)
	{
	    btnPrev.setEnabled(false);
	    funDrawPage(startIndex, endIndex);
	}
	else
	{
	    startIndex = cntNavigate * 18;
	    endIndex = startIndex + 18;
	    funDrawPage(startIndex, endIndex);
	}
	btnNext.setEnabled(true);
	funSetIndexLabel(startIndex);
    }

    private void funChangeModule()
    {
	clsGlobalVarClass.gChangeModule = "Y";
	sql = "";
	ArrayList<String> modulType = new ArrayList<String>();
	int cnt = 0;
	try
	{
	    sql = "select DISTINCT(b.strModuleType) "
		    + " from tbluserdtl a,tblforms b "
		    + " where a.strFormName=b.strModuleName and a.strUserCode='" + clsGlobalVarClass.gUserCode + "'";
	    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rssql.next())
	    {
		modulType.add(rssql.getString(1));
		cnt++;
	    }
	    if (modulType.contains("U"))
	    {
		modulType.remove("U");
		cnt = cnt - 1;
	    }

	    if (cnt == 2)
	    {
		if (clsGlobalVarClass.gSelectedModule.equals("Masters"))
		{
		    if (modulType.contains("T"))
		    {
			clsGlobalVarClass.gSelectedModule = "Transactions";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		    if (modulType.contains("R"))
		    {
			clsGlobalVarClass.gSelectedModule = "Reports";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		}
		else if (clsGlobalVarClass.gSelectedModule.equals("Transactions"))
		{
		    if (modulType.contains("M"))
		    {
			clsGlobalVarClass.gSelectedModule = "Masters";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		    if (modulType.contains("R"))
		    {
			clsGlobalVarClass.gSelectedModule = "Reports";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		}
		else if (clsGlobalVarClass.gSelectedModule.equals("Reports"))
		{
		    if (modulType.contains("M"))
		    {
			clsGlobalVarClass.gSelectedModule = "Masters";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		    if (modulType.contains("T"))
		    {
			clsGlobalVarClass.gSelectedModule = "Transactions";
			new frmMainMenu().setVisible(true);
			dispose();
		    }
		}
	    }
	    if (cnt == 0 || cnt == 1 || cnt == 3 || cnt == 4)
	    {
		new frmModuleSelection().setVisible(true);
		dispose();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCheckModuleType(int keyCode)
    {
	if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Masters"))
	{
	    funInvokeMasterForm(keyCode);
	}
	else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Transactions"))
	{
	    funInvokeTransactionForm(keyCode);
	}
	else if (clsGlobalVarClass.gSelectedModule.equalsIgnoreCase("Reports"))
	{
	    funInvokeReportForm(keyCode);
	}
    }

    private void funInvokeTransactionForm(int keyCode)
    {
	String moduleName = funGetModuleName(keyCode, "T");
	funLoadTransactionForms(moduleName);
    }

    private void funInvokeMasterForm(int keyCode)
    {
	String moduleName = funGetModuleName(keyCode, "M");
	funLoadMasterForms(moduleName);
    }

    private void funInvokeReportForm(int keyCode)
    {
	String moduleName = funGetModuleName(keyCode, "R");
	funLoadReportForms(moduleName);
    }

    private String funGetModuleName(int keyCode, String moduleType)
    {
	String moduleName = "";
	try
	{
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet("select strModuleName from tblshortcutkeysetup where strShortcutKey='" + keyCode + "' and strModuleType='" + moduleType + "' ");
	    if (resultSet.next())
	    {
		moduleName = resultSet.getString("strModuleName");
	    }
	    resultSet.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return moduleName;
	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        panelInnerBody = new javax.swing.JPanel();
        lblDesktop = new javax.swing.JLabel();
        lblChangePOS = new javax.swing.JLabel();
        lblLogOut1 = new javax.swing.JLabel();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblMenu1 = new javax.swing.JLabel();
        lblMenu2 = new javax.swing.JLabel();
        lblMenu3 = new javax.swing.JLabel();
        lblMenu4 = new javax.swing.JLabel();
        lblMenu5 = new javax.swing.JLabel();
        lblMenu6 = new javax.swing.JLabel();
        lblMenu12 = new javax.swing.JLabel();
        lblMenu11 = new javax.swing.JLabel();
        lblMenu10 = new javax.swing.JLabel();
        lblMenu9 = new javax.swing.JLabel();
        lblMenu8 = new javax.swing.JLabel();
        lblMenu7 = new javax.swing.JLabel();
        lblMenu13 = new javax.swing.JLabel();
        lblMenu14 = new javax.swing.JLabel();
        lblMenu15 = new javax.swing.JLabel();
        lblMenu16 = new javax.swing.JLabel();
        lblMenu17 = new javax.swing.JLabel();
        lblMenu18 = new javax.swing.JLabel();
        txtFormSearch = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        lblChangeModule = new javax.swing.JLabel();
        lblNotifications = new javax.swing.JLabel();
        panelFooter = new javax.swing.JPanel();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
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
        lblProductName.setText(" SPOS - ");
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
        lblformName.setText("- Main Menu");
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
        panelHeader.add(filler7);

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
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setLayout(new java.awt.GridBagLayout());

        panelInnerBody.setBackground(new java.awt.Color(255, 255, 255));
        panelInnerBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDesktop.setBackground(new java.awt.Color(51, 204, 255));
        lblDesktop.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDesktop.setForeground(new java.awt.Color(255, 255, 255));
        lblDesktop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgDesktop.png"))); // NOI18N
        lblDesktop.setToolTipText("Desktop");
        lblDesktop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDesktop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDesktopMouseClicked(evt);
            }
        });
        panelInnerBody.add(lblDesktop, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, -1, -1));

        lblChangePOS.setBackground(new java.awt.Color(51, 204, 255));
        lblChangePOS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChangePOS.setForeground(new java.awt.Color(255, 255, 255));
        lblChangePOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangePOS.png"))); // NOI18N
        lblChangePOS.setToolTipText("Change POS");
        lblChangePOS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblChangePOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblChangePOSMouseClicked(evt);
            }
        });
        panelInnerBody.add(lblChangePOS, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, -1, -1));

        lblLogOut1.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblLogOut1.setForeground(new java.awt.Color(216, 9, 21));
        lblLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgLogOut.png"))); // NOI18N
        lblLogOut1.setToolTipText("Logout");
        lblLogOut1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogOut1MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblLogOut1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 0, -1, -1));

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgNextButton.png"))); // NOI18N
        btnNext.setToolTipText("");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        panelInnerBody.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 100, 40));

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgPreviousButton.png"))); // NOI18N
        btnPrev.setToolTipText("");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        panelInnerBody.add(btnPrev, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 40));

        lblMenu1.setBackground(new java.awt.Color(51, 153, 255));
        lblMenu1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblMenu1.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu1MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        lblMenu2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu2.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu2MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));

        lblMenu3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu3.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblMenu3MouseEntered(evt);
            }
        });
        panelInnerBody.add(lblMenu3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, -1, -1));

        lblMenu4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblMenu4.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblMenu4MouseEntered(evt);
            }
        });
        panelInnerBody.add(lblMenu4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, -1, -1));

        lblMenu5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu5.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu5MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, -1));

        lblMenu6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu6.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu6MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 170, -1, -1));

        lblMenu12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu12.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu12MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu12, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 290, -1, -1));

        lblMenu11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu11.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu11MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu11, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 290, -1, -1));

        lblMenu10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu10.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu10MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu10, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, -1, -1));

        lblMenu9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu9.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu9MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, -1, -1));

        lblMenu8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu8.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu8MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, -1, -1));

        lblMenu7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu7.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu7MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        lblMenu13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu13.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu13MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        lblMenu14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu14.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu14MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, -1, -1));

        lblMenu15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu15.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu15MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu15, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 410, -1, -1));

        lblMenu16.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu16.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu16.setToolTipText("");
        lblMenu16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu16MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu16, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, -1, -1));

        lblMenu17.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu17.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu17.setToolTipText("");
        lblMenu17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu17MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu17, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, -1, -1));

        lblMenu18.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMenu18.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgDirectBiller1.png"))); // NOI18N
        lblMenu18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMenu18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMenu18MouseClicked(evt);
            }
        });
        panelInnerBody.add(lblMenu18, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 410, -1, -1));

        txtFormSearch.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        txtFormSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFormSearch.setToolTipText("");
        txtFormSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFormSearchMouseClicked(evt);
            }
        });
        txtFormSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFormSearchActionPerformed(evt);
            }
        });
        txtFormSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFormSearchFocusLost(evt);
            }
        });
        txtFormSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFormSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFormSearchKeyTyped(evt);
            }
        });
        panelInnerBody.add(txtFormSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 162, 30));

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");
        panelInnerBody.add(lblSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 60, 30));

        lblChangeModule.setBackground(new java.awt.Color(51, 204, 255));
        lblChangeModule.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChangeModule.setForeground(new java.awt.Color(255, 255, 255));
        lblChangeModule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgChangeModule.png"))); // NOI18N
        lblChangeModule.setToolTipText("Change Module");
        lblChangeModule.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblChangeModule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblChangeModuleMouseClicked(evt);
            }
        });
        panelInnerBody.add(lblChangeModule, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, -1, -1));
        panelInnerBody.add(lblNotifications, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, 100, 30));

        panelBody.add(panelInnerBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        panelFooter.setBackground(new java.awt.Color(255, 255, 255));
        panelFooter.setPreferredSize(new java.awt.Dimension(0, 30));
        java.awt.GridBagLayout jPanel3Layout = new java.awt.GridBagLayout();
        jPanel3Layout.columnWidths = new int[] {0, 15, 0, 15, 0, 15, 0, 15, 0};
        jPanel3Layout.rowHeights = new int[] {0};
        panelFooter.setLayout(jPanel3Layout);

        lbl1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgOrange.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        panelFooter.add(lbl1, gridBagConstraints);

        lbl2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgOrange.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        panelFooter.add(lbl2, gridBagConstraints);

        lbl3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgOrange.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        panelFooter.add(lbl3, gridBagConstraints);

        lbl4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgOrange.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        panelFooter.add(lbl4, gridBagConstraints);

        lbl5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgOrange.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        panelFooter.add(lbl5, gridBagConstraints);

        getContentPane().add(panelFooter, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblDesktopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDesktopMouseClicked
	// TODO add your handling code here:
	new frmDesktop().setVisible(true);
    }//GEN-LAST:event_lblDesktopMouseClicked

    private void lblChangePOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangePOSMouseClicked

	try
	{
	    Window openedWindows[] = Window.getWindows();
	    for (int i = 0; i < openedWindows.length; i++)
	    {
		openedWindows[i].dispose();
	    }

	    new frmPOSSelection().setVisible(true);
	    this.dispose();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    this.dispose();
	    new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
	    new frmTools("startup").setVisible(true);
	}
    }//GEN-LAST:event_lblChangePOSMouseClicked

    private void lblLogOut1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOut1MouseClicked
	// TODO add your handling code here:
	frmOkCancelPopUp objOKCancel = new frmOkCancelPopUp(this, "Do you want to log out??");
	objOKCancel.setVisible(true);
	if (objOKCancel.getResult() == 1)
	{
	    dispose();
	    new frmLogin().setVisible(true);
	}
    }//GEN-LAST:event_lblLogOut1MouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
	// TODO add your handling code here:
	funNextMenuScreenButtonPressed();

    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
	// TODO add your handling code here:
	funPreviousMenuScreenButtonPressed();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void lblMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu1MouseClicked

	funFormLabelClicked(actionCom[0]);
    }//GEN-LAST:event_lblMenu1MouseClicked

    private void lblMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu2MouseClicked

	funFormLabelClicked(actionCom[1]);
    }//GEN-LAST:event_lblMenu2MouseClicked

    private void lblMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu3MouseClicked

	funFormLabelClicked(actionCom[2]);
    }//GEN-LAST:event_lblMenu3MouseClicked

    private void lblMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu4MouseClicked

	funFormLabelClicked(actionCom[3]);
    }//GEN-LAST:event_lblMenu4MouseClicked

    private void lblMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu5MouseClicked

	funFormLabelClicked(actionCom[4]);
    }//GEN-LAST:event_lblMenu5MouseClicked

    private void lblMenu6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu6MouseClicked

	funFormLabelClicked(actionCom[5]);
    }//GEN-LAST:event_lblMenu6MouseClicked

    private void lblMenu12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu12MouseClicked

	funFormLabelClicked(actionCom[11]);
    }//GEN-LAST:event_lblMenu12MouseClicked

    private void lblMenu11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu11MouseClicked

	funFormLabelClicked(actionCom[10]);
    }//GEN-LAST:event_lblMenu11MouseClicked

    private void lblMenu10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu10MouseClicked

	funFormLabelClicked(actionCom[9]);
    }//GEN-LAST:event_lblMenu10MouseClicked

    private void lblMenu9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu9MouseClicked

	funFormLabelClicked(actionCom[8]);
    }//GEN-LAST:event_lblMenu9MouseClicked

    private void lblMenu8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu8MouseClicked

	funFormLabelClicked(actionCom[7]);
    }//GEN-LAST:event_lblMenu8MouseClicked

    private void lblMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu7MouseClicked

	funFormLabelClicked(actionCom[6]);
    }//GEN-LAST:event_lblMenu7MouseClicked

    private void lblMenu13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu13MouseClicked

	funFormLabelClicked(actionCom[12]);
    }//GEN-LAST:event_lblMenu13MouseClicked

    private void lblMenu14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu14MouseClicked

	funFormLabelClicked(actionCom[13]);
    }//GEN-LAST:event_lblMenu14MouseClicked

    private void lblMenu15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu15MouseClicked

	funFormLabelClicked(actionCom[14]);
    }//GEN-LAST:event_lblMenu15MouseClicked

    private void lblMenu16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu16MouseClicked

	funFormLabelClicked(actionCom[15]);
    }//GEN-LAST:event_lblMenu16MouseClicked

    private void lblMenu17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu17MouseClicked

	funFormLabelClicked(actionCom[16]);
    }//GEN-LAST:event_lblMenu17MouseClicked

    private void lblMenu18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenu18MouseClicked

	funFormLabelClicked(actionCom[17]);
    }//GEN-LAST:event_lblMenu18MouseClicked

    private void txtFormSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFormSearchKeyTyped

	//funGetForm(txtFormSearch.getText().trim());
    }//GEN-LAST:event_txtFormSearchKeyTyped

    private void txtFormSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFormSearchMouseClicked
	if (clsGlobalVarClass.gTouchScreenMode)
	{
	    if (txtFormSearch.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Form  Name").setVisible(true);
		txtFormSearch.setText(clsGlobalVarClass.gKeyboardValue);
		btnPrev.requestFocus();
		flagSearch = true;
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtFormSearch.getText(), "1", "Enter Form Name").setVisible(true);
		txtFormSearch.setText(clsGlobalVarClass.gKeyboardValue);
		btnPrev.requestFocus();
		flagSearch = true;
	    }
	}
    }//GEN-LAST:event_txtFormSearchMouseClicked

    private void txtFormSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFormSearchFocusLost
	if (txtFormSearch.getText().trim().length() > 0 || flagSearch)
	{
	    flagSearch = false;
	    txtFormSearch.setText(txtFormSearch.getText().trim().replaceAll("[\\W_]", " "));
	    funGetForm(txtFormSearch.getText().trim().replaceAll("[\\W_]", " "));
	}
    }//GEN-LAST:event_txtFormSearchFocusLost

    private void txtFormSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFormSearchActionPerformed
	// TODO add your handling code here:

    }//GEN-LAST:event_txtFormSearchActionPerformed

    private void lblChangeModuleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblChangeModuleMouseClicked
	Window openedWindows[] = Window.getWindows();
	for (int i = 0; i < openedWindows.length; i++)
	{
	    openedWindows[i].dispose();
	}
	funChangeModule();
    }//GEN-LAST:event_lblChangeModuleMouseClicked

    private void txtFormSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFormSearchKeyPressed

	// TODO add your handling code here:
	//JOptionPane.showMessageDialog(null,"Id="+evt.getID()+"\tCode="+evt.getKeyCode()+"\tChar="+evt.getKeyChar());        
	if (evt.getKeyCode() >= 112 && evt.getKeyCode() <= 121)
	{
	    funCheckModuleType(evt.getKeyCode());
	}
	else
	{
	    txtFormSearch.setText(txtFormSearch.getText().trim().replaceAll("[\\W_]", ""));
	    funGetForm(txtFormSearch.getText().trim().replaceAll("[\\W_]", ""));
	}
    }//GEN-LAST:event_txtFormSearchKeyPressed

    private void lblMenu3MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblMenu3MouseEntered
    {//GEN-HEADEREND:event_lblMenu3MouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_lblMenu3MouseEntered

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDateMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPosNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblformNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHOSignMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void lblMenu4MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblMenu4MouseEntered
    {//GEN-HEADEREND:event_lblMenu4MouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_lblMenu4MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lblChangeModule;
    private javax.swing.JLabel lblChangePOS;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDesktop;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblLogOut1;
    private javax.swing.JLabel lblMenu1;
    private javax.swing.JLabel lblMenu10;
    private javax.swing.JLabel lblMenu11;
    private javax.swing.JLabel lblMenu12;
    private javax.swing.JLabel lblMenu13;
    private javax.swing.JLabel lblMenu14;
    private javax.swing.JLabel lblMenu15;
    private javax.swing.JLabel lblMenu16;
    private javax.swing.JLabel lblMenu17;
    private javax.swing.JLabel lblMenu18;
    private javax.swing.JLabel lblMenu2;
    private javax.swing.JLabel lblMenu3;
    private javax.swing.JLabel lblMenu4;
    private javax.swing.JLabel lblMenu5;
    private javax.swing.JLabel lblMenu6;
    private javax.swing.JLabel lblMenu7;
    private javax.swing.JLabel lblMenu8;
    private javax.swing.JLabel lblMenu9;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNotifications;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelInnerBody;
    private javax.swing.JTextField txtFormSearch;
    // End of variables declaration//GEN-END:variables

}
