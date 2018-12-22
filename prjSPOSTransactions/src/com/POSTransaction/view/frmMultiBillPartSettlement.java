package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsBillSeriesBillDtl;
import com.POSGlobal.controller.clsBillSettlementDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsGuestRoomDtl;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTaxCalculationDtls;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsBillDiscountDtl;
import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class frmMultiBillPartSettlement extends javax.swing.JFrame
{

    private StringBuilder sqlBuilder = null;
    private int noOfSettlementMode = clsSettelementOptions.listSettelmentOptions.size();
    DefaultTableModel dmBillDetail, dmPaymentAmtDtl;
    private String txtAmtVal, txtPaidAmtVal;
    private String amountBox, finalAmount, discountRemarks;
    private String textValue1, textValue2;
    boolean printstatus, settleMode;
    private String discountType;
    private BigDecimal btnVal, tempVal, billAmount;
    private Point PointCash, PointCheque;
    boolean dyn1, flgEnterBtnPressed, flgUpdateBillTableForDiscount;
    private String settlementName;
    private int paxNo;
    private String settleName, settleType, strButtonClicked = "Print", billPrintOnSettlement;
    private String tableNo, waiterNo;
    private boolean flgMakeKot, flgMakeBill, flgUnsettledBills;
    private double dblTotalTaxAmt, currencyRate;
    private JButton[] settlementArray = new JButton[4];
    private boolean flgGiftVoucherOK;
    private static String debitCardNo;
    private String selectedReasonCode;
    public static String customerCodeForCredit;
    private Vector vModifyReasonCode, vModifyReasonName, vComplReasonCode, vComplReasonName, vReasonCodeForDiscount, vReasonNameForDiscount;
    private ArrayList<String> listItemCode;
    private ArrayList<String> listSubGroupCode;
    private ArrayList<String> listSubGroupName;
    private ArrayList<String> listGroupName;
    private ArrayList<String> listGroupCode;

    private ButtonGroup radioButtonGroup;
    private double cmsMemberBalance = 0;
    private String billType, billTypeForTax;
    private int disableNext;
    private String custCode, delPersonCode;
    private Map<String, clsBillItemDtl> hmBillItemDtl = new HashMap<String, clsBillItemDtl>();
    private List<clsDirectBillerItemDtl> objListDirectBillerItemDtl = null, objListItemDtlTemp = null;//Used for Direct Biller ONLY

    private HashMap<String, clsSettelementOptions> hmSettlemetnOptions = new HashMap<>();
    private double billTotal = 0.00;
    private String strBillTot;
    private double dblDiscountAmt = 0.00;
    private double dblDiscountPer = 0.00;
    private double dblSettlementAmount = 0.00;
    private double _paidAmount = 0.00, tipAmount = 0;
    private double _subTotal = 0.00;
    private double _netAmount = 0.00;
    private double _grandTotal = 0.00;
    private double _balanceAmount = 0.00;
    private double refundAmt = 0.00, tempPaidAmt = 0.00;
    private double _refundAmount = 0.00;
    private String _giftVoucherCode = "", custMobileNoForCRM;
    private String _giftVoucherSeriesCode = "", advOrderBookingNo = "", couponCode = "";
    private int _settlementNavigate;
    private double _deliveryCharge = 0.00, _loyalityPoints = 0.00;
    private String dtPOSDate, homeDelivery, areaCode, operationTypeForTax, takeAway, callingFormName = "", cmsMemberName;
    //private ArrayList<ArrayList<Object>> arrListTaxCal;
    private List<clsTaxCalculationDtls> arrListTaxCal;
    private boolean flagAddKOTstoBill = false;
    //private Map<String, clsPromotionItems> hmPromoItem;
    //private Map<String, Double> hmAddKOTItems;
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
    //private Map<String, List<clsBillItemDtl>> hmBillSeriesItemList;
    private List<clsBillSeriesBillDtl> listBillSeriesBillDtl;
    private String settlementCode = "";//use while calculating tax for settlement
    private SimpleDateFormat ddMMyyyyDateFormat;
    private SimpleDateFormat yyyyMMddDateFormat;

    private ArrayList<String> listOfBills = null;
    private frmMultiBillSettle objMultiBillSettle = null;
    private HashMap<String, Double> mapDebitCardBalance;

    public frmMultiBillPartSettlement(frmMultiBillSettle objMultiBillSettle, ArrayList<String> listOfBills)
    {
	initComponents();
	try
	{

	    ddMMyyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    //dteBillDate.setDate(objUtility.funGetDateToSetCalenderDate());
	    this.objMultiBillSettle = objMultiBillSettle;
	    this.listOfBills = listOfBills;

	    tableNo = "";
	    callingFormName = "";
//           System.out.println(noOfSettlementMode);
	    sqlBuilder = new StringBuilder();
	    dmBillDetail = (DefaultTableModel) tblBillDetailsTable.getModel();
	    dmPaymentAmtDtl = (DefaultTableModel) tblPaymentDetails.getModel();
	    flgUnsettledBills = true;
	    panelSettlement.setVisible(true);
	    //create the button array of settel mode
	    settlementArray[0] = btnSettlement1;
	    settlementArray[1] = btnSettlement2;
	    settlementArray[2] = btnSettlement3;
	    settlementArray[3] = btnSettlement4;
	    fun_FillSettlementBtns();
	    // funFillSettlementButtons(0, noOfSettlementMode);
	    settleType = "Cash";
	    settlementCode = "S01";//
	    lblPaymentModeVal.setText(settleType);
	    PointCheque = PanelCheque.getLocation();

	    txtAmount.setFocusable(false);
	    txtAmount.setEditable(false);
	    txtPaidAmt.setFocusable(true);

	    PanelCheque.setVisible(false);
	    panelAmt.setVisible(true);
	    panelMode.setVisible(true);
	    PanelCard.setVisible(false);
	    PanelCoupen.setVisible(false);
	    PanelGiftVoucher.setVisible(false);
	    panelRoomSettlement.setVisible(false);
	    PointCash = panelAmt.getLocation();
	    PointCheque = PanelCheque.getLocation();
	    amountBox = "PaidAmount";
	    panelCustomer.setVisible(false);
	    PanelRemaks.setVisible(true);
	    PanelCard.setVisible(false);
	    txtPaidAmt.setFocusable(true);
	    textValue2 = "";
	    textValue1 = "";
	    jScrollPane3.setVisible(false);
	    scrItemDetials.setVisible(false);
	    panelDiscount.setVisible(false);
	    scrSettle.setVisible(false);
	    lblTotSettleAmt.setVisible(false);
	    lblBillAmount.setVisible(false);
	    lblTotal.setVisible(false);
	    lblTotalVal.setVisible(false);
	    PanelRemaks.setVisible(false);
	    panelMode.setVisible(false);
	    panelAmt.setVisible(false);
	    if (!clsGlobalVarClass.gSuperUser)
	    {
		if (!clsGlobalVarClass.hmUserForms.containsKey("Discount On Bill"))
		{
		    panelDiscount.setVisible(false);
		}
	    }
	    discountType = "Percent";

	    funResetPaymentDetailField();
	    funResetFieldVariables();

	    funSetBillData(listOfBills);

	    mapDebitCardBalance = new HashMap<String, Double>();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-1", JOptionPane.ERROR_MESSAGE);
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
        btnSettlement2 = new javax.swing.JButton();
        btnSettlement4 = new javax.swing.JButton();
        lblRefund = new javax.swing.JLabel();
        lblDelBoyName = new javax.swing.JLabel();
        btnNextSettlementMode = new javax.swing.JButton();
        btnPrevSettlementMode = new javax.swing.JButton();
        btnSettlement3 = new javax.swing.JButton();
        lblCreditCustCode = new javax.swing.JLabel();
        btnChangeSettlement = new javax.swing.JButton();
        panelAmt = new javax.swing.JPanel();
        txtAmount = new javax.swing.JTextField();
        lblTip = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblCardBalance = new javax.swing.JLabel();
        lblcard = new javax.swing.JLabel();
        txtPaidAmt = new javax.swing.JTextField();
        txtBalance = new javax.swing.JTextField();
        panelMode = new javax.swing.JPanel();
        lblPaymentMode = new javax.swing.JLabel();
        lblPaymentModeVal = new javax.swing.JLabel();
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
        PanelRemaks = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaRemark = new javax.swing.JTextArea();
        lblRemark = new javax.swing.JLabel();
        panelCustomer = new javax.swing.JPanel();
        lblCustName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblTipAmount = new javax.swing.JLabel();
        txtTip = new javax.swing.JTextField();
        scrSettle = new javax.swing.JScrollPane();
        tblSettlement = new javax.swing.JTable();
        PanelGiftVoucher = new javax.swing.JPanel();
        lblGVouchName = new javax.swing.JLabel();
        lblGVSeriesNo = new javax.swing.JLabel();
        txtSeriesNo = new javax.swing.JTextField();
        btnGiftVoucherOK = new javax.swing.JButton();
        txtVoucherSeries = new javax.swing.JTextField();
        panelRoomSettlement = new javax.swing.JPanel();
        lblFolioNo = new javax.swing.JLabel();
        txtFolioNo = new javax.swing.JTextField();
        lblGuestName = new javax.swing.JLabel();
        txtGuestName = new javax.swing.JTextField();
        lblRoomNo = new javax.swing.JLabel();
        txtRoomNo = new javax.swing.JTextField();
        lblGuestCode = new javax.swing.JLabel();
        txtGuestCode = new javax.swing.JTextField();
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
        btnClose = new javax.swing.JButton();
        OrderPanel = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBillDetailsTable = new javax.swing.JTable();
        lblBillAmount = new javax.swing.JLabel();
        lblTotSettleAmt = new javax.swing.JLabel();
        scrItemDetials = new javax.swing.JScrollPane();
        tblPaymentDetails = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();

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
        btnSettlement1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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

        btnSettlement2.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
        btnSettlement4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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

        lblRefund.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblRefund.setForeground(new java.awt.Color(0, 153, 255));
        panelSettlement.add(lblRefund);
        lblRefund.setBounds(320, 450, 160, 20);
        panelSettlement.add(lblDelBoyName);
        lblDelBoyName.setBounds(400, 250, 90, 30);

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
        panelSettlement.add(btnNextSettlementMode);
        btnNextSettlementMode.setBounds(430, 10, 60, 40);

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
        panelSettlement.add(btnPrevSettlementMode);
        btnPrevSettlementMode.setBounds(0, 10, 60, 40);

        btnSettlement3.setBackground(new java.awt.Color(102, 153, 255));
        btnSettlement3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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

        btnChangeSettlement.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnChangeSettlement.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeSettlement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnChangeSettlement.setText("<html>SETTLE</html>");
        btnChangeSettlement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeSettlement.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnChangeSettlement.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnChangeSettlementActionPerformed(evt);
            }
        });
        btnChangeSettlement.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnChangeSettlementKeyPressed(evt);
            }
        });
        panelSettlement.add(btnChangeSettlement);
        btnChangeSettlement.setBounds(300, 550, 110, 40);

        txtAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAmount.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtAmountActionPerformed(evt);
            }
        });

        lblTip.setText("Paid Amount");

        lblBalance.setText("Balance");

        jLabel2.setText("Bill Amount");

        lblcard.setText("Card Balance");

        txtPaidAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaidAmt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPaidAmtActionPerformed(evt);
            }
        });
        txtPaidAmt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPaidAmtKeyPressed(evt);
            }
        });

        txtBalance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBalance.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtBalanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAmtLayout = new javax.swing.GroupLayout(panelAmt);
        panelAmt.setLayout(panelAmtLayout);
        panelAmtLayout.setHorizontalGroup(
            panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAmtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAmtLayout.createSequentialGroup()
                        .addComponent(lblBalance)
                        .addGap(51, 51, 51)
                        .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelAmtLayout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAmtLayout.createSequentialGroup()
                            .addComponent(lblTip)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPaidAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAmtLayout.createSequentialGroup()
                            .addComponent(lblcard)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCardBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );
        panelAmtLayout.setVerticalGroup(
            panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAmtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAmtLayout.createSequentialGroup()
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPaidAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBalance)))
                    .addGroup(panelAmtLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTip)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAmtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblcard, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblCardBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        panelAmtLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, txtAmount});

        panelAmtLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblBalance, txtBalance});

        txtPaidAmt.getAccessibleContext().setAccessibleName("");

        panelSettlement.add(panelAmt);
        panelAmt.setBounds(10, 100, 200, 150);

        lblPaymentMode.setText("Payment Mode");

        javax.swing.GroupLayout panelModeLayout = new javax.swing.GroupLayout(panelMode);
        panelMode.setLayout(panelModeLayout);
        panelModeLayout.setHorizontalGroup(
            panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPaymentMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPaymentModeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelModeLayout.setVerticalGroup(
            panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPaymentModeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelModeLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblPaymentMode, lblPaymentModeVal});

        panelSettlement.add(panelMode);
        panelMode.setBounds(10, 60, 200, 37);

        panelNumericPad.setBackground(new java.awt.Color(255, 255, 255));
        panelNumericPad.setMinimumSize(new java.awt.Dimension(340, 260));
        panelNumericPad.setOpaque(false);

        btnCal7.setBackground(new java.awt.Color(204, 204, 204));
        btnCal7.setText("7");
        btnCal7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal7MouseClicked(evt);
            }
        });

        btnCal8.setBackground(new java.awt.Color(204, 204, 204));
        btnCal8.setText("8");
        btnCal8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal8MouseClicked(evt);
            }
        });

        btnCal9.setBackground(new java.awt.Color(204, 204, 204));
        btnCal9.setText("9");
        btnCal9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal9MouseClicked(evt);
            }
        });

        btnCalClear.setBackground(new java.awt.Color(204, 204, 204));
        btnCalClear.setText("C");
        btnCalClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCalClearMouseClicked(evt);
            }
        });

        btnDny1.setBackground(new java.awt.Color(204, 204, 204));
        btnDny1.setText("10");
        btnDny1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny1ActionPerformed(evt);
            }
        });

        btnCal4.setBackground(new java.awt.Color(204, 204, 204));
        btnCal4.setText("4");
        btnCal4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal4MouseClicked(evt);
            }
        });

        btnCal5.setBackground(new java.awt.Color(204, 204, 204));
        btnCal5.setText("5");
        btnCal5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal5MouseClicked(evt);
            }
        });

        btnCal6.setBackground(new java.awt.Color(204, 204, 204));
        btnCal6.setText("6");
        btnCal6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal6MouseClicked(evt);
            }
        });

        btnCal0.setBackground(new java.awt.Color(204, 204, 204));
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
        btnDny3.setText("100");
        btnDny3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny3ActionPerformed(evt);
            }
        });

        btnDny4.setBackground(new java.awt.Color(204, 204, 204));
        btnDny4.setText("500");
        btnDny4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDny4ActionPerformed(evt);
            }
        });

        btnCalEnter.setBackground(new java.awt.Color(204, 204, 204));
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
        btnCal00.setText("00");
        btnCal00.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal00MouseClicked(evt);
            }
        });

        btnCal3.setBackground(new java.awt.Color(204, 204, 204));
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
        btnCalBackSpace.setText("BackSpace");
        btnCalBackSpace.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalBackSpaceActionPerformed(evt);
            }
        });

        btnCalDot.setBackground(new java.awt.Color(204, 204, 204));
        btnCalDot.setText(".");
        btnCalDot.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCalDotActionPerformed(evt);
            }
        });

        btnCal1.setBackground(new java.awt.Color(204, 204, 204));
        btnCal1.setText("1");
        btnCal1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCal1MouseClicked(evt);
            }
        });

        btnCal2.setBackground(new java.awt.Color(204, 204, 204));
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
        panelNumericPad.setBounds(210, 370, 290, 170);

        PanelCard.setPreferredSize(new java.awt.Dimension(240, 60));

        txtCardName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardNameMouseClicked(evt);
            }
        });

        lblSlipNo.setText("Slip No.");

        lblExpiryDate.setText("Expiry Date");

        dteExpiry.setPreferredSize(new java.awt.Dimension(100, 25));

        javax.swing.GroupLayout PanelCardLayout = new javax.swing.GroupLayout(PanelCard);
        PanelCard.setLayout(PanelCardLayout);
        PanelCardLayout.setHorizontalGroup(
            PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCardLayout.createSequentialGroup()
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSlipNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblExpiryDate, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGroup(PanelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dteExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        PanelCard.setBounds(10, 420, 200, 60);

        lblAmountLabel.setText("Amount");

        txtCoupenAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblRemarkLabel.setText("Remark");

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
                        .addGap(0, 26, Short.MAX_VALUE))
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
        PanelCoupen.setBounds(10, 350, 200, 70);

        lblChequeNo.setText("Cheque No.   ");

        txtChequeNo.setMinimumSize(new java.awt.Dimension(110, 25));
        txtChequeNo.setPreferredSize(new java.awt.Dimension(120, 25));
        txtChequeNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtChequeNoMouseClicked(evt);
            }
        });

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

        lblChqDate.setText("Date");
        lblChqDate.setMaximumSize(new java.awt.Dimension(61, 25));
        lblChqDate.setMinimumSize(new java.awt.Dimension(61, 25));
        lblChqDate.setPreferredSize(new java.awt.Dimension(61, 25));

        dteCheque.setMinimumSize(new java.awt.Dimension(110, 25));
        dteCheque.setPreferredSize(new java.awt.Dimension(120, 25));

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
                        .addComponent(dteCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(PanelChequeLayout.createSequentialGroup()
                        .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChequeNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelChequeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtChequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(1, 1, 1))
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
        PanelCheque.setBounds(10, 250, 200, 100);

        txtAreaRemark.setColumns(20);
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        PanelRemaksLayout.setVerticalGroup(
            PanelRemaksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRemaksLayout.createSequentialGroup()
                .addComponent(lblRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        panelSettlement.add(PanelRemaks);
        PanelRemaks.setBounds(300, 130, 200, 100);

        lblCustName.setText("Cust Name:");

        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
            }
        });

        lblTipAmount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTipAmount.setText("Tip :");
        lblTipAmount.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

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

        javax.swing.GroupLayout panelCustomerLayout = new javax.swing.GroupLayout(panelCustomer);
        panelCustomer.setLayout(panelCustomerLayout);
        panelCustomerLayout.setHorizontalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addGroup(panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustomerLayout.createSequentialGroup()
                        .addComponent(lblCustName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustomerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTipAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtTip, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelCustomerLayout.setVerticalGroup(
            panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomerLayout.createSequentialGroup()
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(panelCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTipAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(lblCustName)
        );

        panelSettlement.add(panelCustomer);
        panelCustomer.setBounds(230, 60, 200, 70);

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
        scrSettle.setBounds(200, 130, 290, 100);

        lblGVouchName.setText("Voucher Name ");

        lblGVSeriesNo.setText("Series No.");

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
                .addContainerGap(125, Short.MAX_VALUE)
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
        PanelGiftVoucher.setBounds(10, 482, 200, 90);

        panelRoomSettlement.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Room Settlement", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Adobe Devanagari", 1, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        panelRoomSettlement.setToolTipText("");
        panelRoomSettlement.setMaximumSize(new java.awt.Dimension(231, 145));
        panelRoomSettlement.setMinimumSize(new java.awt.Dimension(231, 145));
        panelRoomSettlement.setOpaque(false);
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

        panelDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Discount", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Adobe Devanagari", 1, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        panelDiscount.setToolTipText("");
        panelDiscount.setOpaque(false);
        panelDiscount.setLayout(null);

        cmbItemCategory.setBackground(new java.awt.Color(51, 102, 255));
        panelDiscount.add(cmbItemCategory);
        cmbItemCategory.setBounds(0, 90, 140, 30);

        btnDiscOk.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        btnDiscOk.setBounds(160, 90, 70, 30);

        rdbSubGroupWise.setText("SubGroup");
        rdbSubGroupWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbSubGroupWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbSubGroupWise);
        rdbSubGroupWise.setBounds(0, 70, 73, 20);

        rdbGroupWise.setText("Group");
        rdbGroupWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbGroupWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbGroupWise);
        rdbGroupWise.setBounds(75, 70, 55, 20);

        rdbAll.setText("Total");
        rdbAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbAllActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbAll);
        rdbAll.setBounds(180, 70, 50, 20);

        lblDiscAmt.setText("Discount Amount");
        panelDiscount.add(lblDiscAmt);
        lblDiscAmt.setBounds(10, 40, 120, 25);

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
        txtDiscountAmt.setBounds(140, 40, 80, 25);

        lblDisc.setText("Discount %");
        panelDiscount.add(lblDisc);
        lblDisc.setBounds(10, 10, 110, 30);

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
        txtDiscountPer.setBounds(140, 10, 80, 25);

        rdbItemWise.setText("Item");
        rdbItemWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rdbItemWiseActionPerformed(evt);
            }
        });
        panelDiscount.add(rdbItemWise);
        rdbItemWise.setBounds(130, 70, 50, 20);

        panelSettlement.add(panelDiscount);
        panelDiscount.setBounds(250, 80, 230, 130);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("BACK");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });
        btnClose.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCloseKeyPressed(evt);
            }
        });
        panelSettlement.add(btnClose);
        btnClose.setBounds(420, 550, 80, 40);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 499;
        gridBagConstraints.ipady = 599;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 4, 1);
        panelLayout.add(panelSettlement, gridBagConstraints);

        OrderPanel.setBackground(new java.awt.Color(255, 255, 255));
        OrderPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));
        OrderPanel.setPreferredSize(new java.awt.Dimension(260, 600));
        OrderPanel.setLayout(null);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        tblBillDetailsTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblBillDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No", "Settlement Amt"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblBillDetailsTable.setMinimumSize(new java.awt.Dimension(45, 240));
        tblBillDetailsTable.setRowHeight(30);
        tblBillDetailsTable.setShowVerticalLines(false);
        jScrollPane3.setViewportView(tblBillDetailsTable);

        OrderPanel.add(jScrollPane3);
        jScrollPane3.setBounds(0, 0, 310, 300);

        lblBillAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBillAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillAmount.setText("Bill Amount");
        OrderPanel.add(lblBillAmount);
        lblBillAmount.setBounds(40, 300, 120, 30);

        lblTotSettleAmt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        OrderPanel.add(lblTotSettleAmt);
        lblTotSettleAmt.setBounds(190, 300, 120, 30);

        tblPaymentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Payment Modes", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPaymentDetails.setRowHeight(30);
        tblPaymentDetails.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPaymentDetailsMouseClicked(evt);
            }
        });
        scrItemDetials.setViewportView(tblPaymentDetails);

        OrderPanel.add(scrItemDetials);
        scrItemDetials.setBounds(0, 340, 310, 200);

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotal.setText("Total");
        OrderPanel.add(lblTotal);
        lblTotal.setBounds(90, 550, 80, 30);

        lblTotalVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        OrderPanel.add(lblTotalVal);
        lblTotalVal.setBounds(210, 550, 80, 30);

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

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Multi Bill Part Settle");
    }//GEN-LAST:event_formWindowClosed

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

    private void btnChangeSettlementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnChangeSettlementKeyPressed

    }//GEN-LAST:event_btnChangeSettlementKeyPressed

    private void btnChangeSettlementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeSettlementActionPerformed
	// TODO add your handling code here:
	funMultiBillPartSettleButtonPressed();
    }//GEN-LAST:event_btnChangeSettlementActionPerformed

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

    private void tblPaymentDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPaymentDetailsMouseClicked
	// TODO add your handling code here:

    }//GEN-LAST:event_tblPaymentDetailsMouseClicked

    private void txtPaidAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaidAmtActionPerformed


    }//GEN-LAST:event_txtPaidAmtActionPerformed

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

		}
		else
		{
		    textValue1 = "0";

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

	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-34", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_btnCalClearMouseClicked

    private void btnDny1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny1ActionPerformed

	procEnterValue(btnDny1.getText());
    }//GEN-LAST:event_btnDny1ActionPerformed

    private void btnCal4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal4MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal4.getText());
    }//GEN-LAST:event_btnCal4MouseClicked

    private void btnCal5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal5MouseClicked

	procNumericValue(btnCal5.getText());
    }//GEN-LAST:event_btnCal5MouseClicked

    private void btnCal6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal6MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal6.getText());
    }//GEN-LAST:event_btnCal6MouseClicked

    private void btnCal0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal0MouseClicked
	// TODO add your handling code here:
	procNumericValue(btnCal0.getText());
    }//GEN-LAST:event_btnCal0MouseClicked

    private void btnDny2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny2ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny2.getText());
    }//GEN-LAST:event_btnDny2ActionPerformed

    private void btnDny3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny3ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny3.getText());
    }//GEN-LAST:event_btnDny3ActionPerformed

    private void btnDny4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDny4ActionPerformed
	// TODO add your handling code here:
	procEnterValue(btnDny4.getText());
    }//GEN-LAST:event_btnDny4ActionPerformed

    private void btnCalEnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalEnterMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCalEnterMouseClicked

    private void btnCalEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalEnterActionPerformed
	// TODO add your handling code here:
	if (_balanceAmount != 0.00 || hmSettlemetnOptions.isEmpty())
	{
	    //funResetPaymentDetailField();
	    funEnterButtonPressed();

	}
    }//GEN-LAST:event_btnCalEnterActionPerformed

    private void btnCal00MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal00MouseClicked

	procNumericValue(btnCal00.getText());
    }//GEN-LAST:event_btnCal00MouseClicked

    private void btnCal3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal3MouseClicked

	procNumericValue(btnCal3.getText());
    }//GEN-LAST:event_btnCal3MouseClicked

    private void btnCal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal3ActionPerformed

    }//GEN-LAST:event_btnCal3ActionPerformed

    private void btnCalBackSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalBackSpaceActionPerformed
	funCalBackSpaceButtonPressed();
    }//GEN-LAST:event_btnCalBackSpaceActionPerformed

    private void btnCalDotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalDotActionPerformed
	// TODO add your handling code here:
	funCalDotButtonPressed();
    }//GEN-LAST:event_btnCalDotActionPerformed

    private void btnCal1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal1MouseClicked

	procNumericValue(btnCal1.getText());
    }//GEN-LAST:event_btnCal1MouseClicked

    private void btnCal2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCal2MouseClicked

	procNumericValue(btnCal2.getText());
    }//GEN-LAST:event_btnCal2MouseClicked

    private void btnCal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCal2ActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCal2ActionPerformed

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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-52", JOptionPane.ERROR_MESSAGE);
	    // e.printStackTrace();
	}
    }//GEN-LAST:event_txtCardNameMouseClicked

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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-56", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_txtRemarkMouseClicked

    private void txtBankNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBankNameMouseClicked
	funBankNameTextBoxClicked();
    }//GEN-LAST:event_txtBankNameMouseClicked

    private void txtBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankNameActionPerformed

    }//GEN-LAST:event_txtBankNameActionPerformed

    private void txtAreaRemarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaRemarkMouseClicked
	funTextAreaClicked();
    }//GEN-LAST:event_txtAreaRemarkMouseClicked

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerNameMouseClicked
	// TODO add your handling code here:
	funOpenCustomerMaster();
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtTipMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTipMouseDragged
	amountBox = "tip";
    }//GEN-LAST:event_txtTipMouseDragged

    private void txtTipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTipMouseClicked

	amountBox = "tip";
    }//GEN-LAST:event_txtTipMouseClicked

    private void txtSeriesNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSeriesNoMouseClicked
	funSeriesNoTextBoxClicked();
    }//GEN-LAST:event_txtSeriesNoMouseClicked

    private void btnGiftVoucherOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGiftVoucherOKMouseClicked
	funGiftVoucher();
    }//GEN-LAST:event_btnGiftVoucherOKMouseClicked

    private void txtVoucherSeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVoucherSeriesMouseClicked

	funVoucherSeriesTextBoxClicked();
    }//GEN-LAST:event_txtVoucherSeriesMouseClicked

    private void txtFolioNoMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFolioNoMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoMouseDragged

    private void txtFolioNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFolioNoMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoMouseClicked

    private void txtFolioNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFolioNoKeyPressed

    private void txtGuestNameMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGuestNameMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameMouseDragged

    private void txtGuestNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGuestNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameMouseClicked

    private void txtGuestNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGuestNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestNameKeyPressed

    private void txtRoomNoMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRoomNoMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoMouseDragged

    private void txtRoomNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRoomNoMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoMouseClicked

    private void txtRoomNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRoomNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNoKeyPressed

    private void txtGuestCodeMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGuestCodeMouseDragged
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeMouseDragged

    private void txtGuestCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtGuestCodeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeMouseClicked

    private void txtGuestCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGuestCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtGuestCodeKeyPressed

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

	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-54", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_txtChequeNoMouseClicked

    private void rdbItemWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbItemWiseActionPerformed
	// TODO add your handling code here:
	//        funFillItemList();
    }//GEN-LAST:event_rdbItemWiseActionPerformed

    private void txtDiscountPerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountPerKeyPressed

    }//GEN-LAST:event_txtDiscountPerKeyPressed

    private void txtDiscountPerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountPerMouseClicked

	textValue1 = "";
	txtDiscountPer.setText("");
	txtDiscountAmt.setText("0.00");
	amountBox = "discount";
	discountType = "Percent";
    }//GEN-LAST:event_txtDiscountPerMouseClicked

    private void txtDiscountPerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountPerMouseDragged
	textValue1 = "";
	txtDiscountPer.setText("");
	txtDiscountAmt.setText("0.00");
	amountBox = "discount";
	discountType = "Percent";
    }//GEN-LAST:event_txtDiscountPerMouseDragged

    private void txtDiscountAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountAmtKeyPressed
	// TODO add your handling code here:

    }//GEN-LAST:event_txtDiscountAmtKeyPressed

    private void txtDiscountAmtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountAmtMouseClicked

	textValue1 = "";
	txtDiscountAmt.setText("");
	txtDiscountPer.setText("0.00");
	discountType = "Amount";
	amountBox = "discount";
    }//GEN-LAST:event_txtDiscountAmtMouseClicked

    private void txtDiscountAmtMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiscountAmtMouseDragged
	textValue1 = "";
	txtDiscountAmt.setText("");
	txtDiscountPer.setText("0.00");
	discountType = "Amount";
	amountBox = "discount";
    }//GEN-LAST:event_txtDiscountAmtMouseDragged

    private void rdbAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbAllActionPerformed

	try
	{
	    cmbItemCategory.removeAllItems();
	    cmbItemCategory.setEnabled(false);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-62", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }//GEN-LAST:event_rdbAllActionPerformed

    private void rdbGroupWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbGroupWiseActionPerformed


    }//GEN-LAST:event_rdbGroupWiseActionPerformed

    private void rdbSubGroupWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbSubGroupWiseActionPerformed


    }//GEN-LAST:event_rdbSubGroupWiseActionPerformed

    private void btnDiscOkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiscOkMouseClicked


    }//GEN-LAST:event_btnDiscOkMouseClicked

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	funBackButtonPressed();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnCloseKeyPressed

    private void txtBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBalanceActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceActionPerformed

    private void txtPaidAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaidAmtKeyPressed
	if (evt.getKeyCode() == 10)
	{
	    funEnterButtonPressed();
	}
    }//GEN-LAST:event_txtPaidAmtKeyPressed
    /**
     * This method is used to reset fields
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel OrderPanel;
    private javax.swing.JPanel PanelCard;
    private javax.swing.JPanel PanelCheque;
    private javax.swing.JPanel PanelCoupen;
    private javax.swing.JPanel PanelGiftVoucher;
    private javax.swing.JPanel PanelRemaks;
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
    private javax.swing.JButton btnChangeSettlement;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDiscOk;
    private javax.swing.JButton btnDny1;
    private javax.swing.JButton btnDny2;
    private javax.swing.JButton btnDny3;
    private javax.swing.JButton btnDny4;
    private javax.swing.JButton btnGiftVoucherOK;
    private javax.swing.JButton btnNextSettlementMode;
    private javax.swing.JButton btnPrevSettlementMode;
    private javax.swing.JButton btnSettlement1;
    private javax.swing.JButton btnSettlement2;
    private javax.swing.JButton btnSettlement3;
    private javax.swing.JButton btnSettlement4;
    private javax.swing.JComboBox cmbItemCategory;
    private com.toedter.calendar.JDateChooser dteCheque;
    private com.toedter.calendar.JDateChooser dteExpiry;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAmountLabel;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblBankName;
    private javax.swing.JLabel lblBillAmount;
    private javax.swing.JLabel lblCardBalance;
    private javax.swing.JLabel lblChequeNo;
    private javax.swing.JLabel lblChqDate;
    public static javax.swing.JLabel lblCreditCustCode;
    private javax.swing.JLabel lblCustName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelBoyName;
    private javax.swing.JLabel lblDisc;
    private javax.swing.JLabel lblDiscAmt;
    private javax.swing.JLabel lblExpiryDate;
    private javax.swing.JLabel lblFolioNo;
    private javax.swing.JLabel lblGVSeriesNo;
    private javax.swing.JLabel lblGVouchName;
    private javax.swing.JLabel lblGuestCode;
    private javax.swing.JLabel lblGuestName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPaymentMode;
    private javax.swing.JLabel lblPaymentModeVal;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRefund;
    private javax.swing.JLabel lblRemark;
    private javax.swing.JLabel lblRemarkLabel;
    private javax.swing.JLabel lblRoomNo;
    private javax.swing.JLabel lblSlipNo;
    private javax.swing.JLabel lblTip;
    private javax.swing.JLabel lblTipAmount;
    private javax.swing.JLabel lblTotSettleAmt;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblUserCode;
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
    private javax.swing.JScrollPane scrItemDetials;
    private javax.swing.JScrollPane scrSettle;
    private javax.swing.JTable tblBillDetailsTable;
    private javax.swing.JTable tblPaymentDetails;
    private javax.swing.JTable tblSettlement;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextArea txtAreaRemark;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtCardName;
    private javax.swing.JTextField txtChequeNo;
    private javax.swing.JTextField txtCoupenAmt;
    public static javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDiscountAmt;
    private javax.swing.JTextField txtDiscountPer;
    private javax.swing.JTextField txtFolioNo;
    private javax.swing.JTextField txtGuestCode;
    private javax.swing.JTextField txtGuestName;
    private javax.swing.JTextField txtPaidAmt;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtRoomNo;
    private javax.swing.JTextField txtSeriesNo;
    private javax.swing.JTextField txtTip;
    private javax.swing.JTextField txtVoucherSeries;
    // End of variables declaration//GEN-END:variables

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

    public void funResetFields()
    {

	btnChangeSettlement.setEnabled(true);

	lblTotSettleAmt.setText("");
	lblTotalVal.setText("");
	dmPaymentAmtDtl.setRowCount(0);
	dmBillDetail.setRowCount(0);
	txtAmount.setText("");
	txtPaidAmt.setText("");
	txtBalance.setText("");
	lblCardBalance.setText("");
	txtBankName.setText("");
	txtChequeNo.setText("");

	txtCoupenAmt.setText("");
	txtRemark.setText("");
	txtCardName.setText("");

	txtVoucherSeries.setText("");
	txtSeriesNo.setText("");
	txtCustomerName.setText("");
	txtTip.setText("");

	txtAreaRemark.setText("");
	jScrollPane3.setVisible(false);
	scrItemDetials.setVisible(false);
	panelDiscount.setVisible(false);
	scrSettle.setVisible(false);
	lblTotSettleAmt.setVisible(false);
	lblBillAmount.setVisible(false);
	lblTotal.setVisible(false);
	lblTotalVal.setVisible(false);
	PanelRemaks.setVisible(false);
	panelMode.setVisible(false);
	panelAmt.setVisible(false);
	panelCustomer.setVisible(false);
    }

    /**
     * :- Ritesh 07 Oct 2014 Purpose: Remove Button action code and make
     * separate Method To reuse code in future for Shortcut Key Implementation
     * In POS call When Settle Button Pressed
     */
    private void funMultiBillPartSettleButtonPressed()
    {

	btnChangeSettlement.setEnabled(false);
	if (txtTip.getText().isEmpty())
	{
	    txtTip.setText("0.00");
	}
	tipAmount = Double.parseDouble(txtTip.getText());
	txtTip.setText("0.00");

	if (refundAmt != 0.00)
	{
	    btnChangeSettlement.setEnabled(true);
	    JOptionPane.showMessageDialog(this, "Balance is not zero");
	    return;
	}
	if (hmSettlemetnOptions.isEmpty())
	{
	    btnChangeSettlement.setEnabled(true);
	    new frmOkPopUp(null, "Select Settlement mode", "Warning", 1).setVisible(true);
	    return;
	}
	if (flgEnterBtnPressed == false)
	{
	    btnChangeSettlement.setEnabled(true);
	    new frmOkPopUp(null, "Please Press Enter Key", "Warning", 1).setVisible(true);
	    return;
	}

	funDoPartSettleBills();
    }

    private void funDoPartSettleBills()
    {

	try
	{

	    List<clsBillSettlementDtl> listObjBillSettlementDtl = new ArrayList<clsBillSettlementDtl>();

	    double totalSettlementAmt = Double.parseDouble(lblTotSettleAmt.getText());

	    for (int row = 0; row < tblBillDetailsTable.getRowCount(); row++)
	    {
		String billNo = tblBillDetailsTable.getValueAt(row, 0).toString();
		double billAmount = Double.parseDouble(tblBillDetailsTable.getValueAt(row, 1).toString());

		for (clsSettelementOptions ob : hmSettlemetnOptions.values())
		{
		    if (ob.getStrSettelmentType().equalsIgnoreCase("Complementary"))
		    {
			new frmOkPopUp(this, "Can Not Settle Complementary In Partial Settlement.", "Message", 2).setVisible(true);
			return;
		    }

		    double settleAmt = 0;
		    if (ob.getDblPaidAmt() > ob.getDblSettlementAmt())
		    {
			settleAmt = ob.getDblSettlementAmt();
		    }
		    else
		    {
			settleAmt = ob.getDblPaidAmt();
		    }

		    double newSettleAmt = Math.rint((settleAmt / totalSettlementAmt) * billAmount);

		    if (ob.getStrSettelmentType().equals("Debit Card"))
		    {
			objUtility.funDebitCardTransaction(billNo, debitCardNo, newSettleAmt, "Settle");
			objUtility.funUpdateDebitCardBalance(debitCardNo, newSettleAmt, "Settle");
			/*
                         * ResultSet
                         * rsDebitCardNo=clsGlobalVarClass.dbMysql.executeResultSet("select
                         * strCardNo from tbldebitcardmaster where
                         * strCardString='"+clsGlobalVarClass.gDebitCardNo+"'");
                         * if(rsDebitCardNo.next()) {
                         * funDebitCardTransaction(voucherNo,
                         * rsDebitCardNo.getString(1), settleAmt);
                         * funUpdateDebitCardBalance(rsDebitCardNo.getString(1),
                         * settleAmt); } rsDebitCardNo.close();
			 */
		    }

		    clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl();
		    objBillSettlementDtl.setStrBillNo(billNo);
		    objBillSettlementDtl.setStrSettlementCode(ob.getStrSettelmentCode());
		    objBillSettlementDtl.setDblSettlementAmt(newSettleAmt);
		    objBillSettlementDtl.setDblPaidAmt(newSettleAmt);
		    objBillSettlementDtl.setStrExpiryDate("");
		    objBillSettlementDtl.setStrCardName(ob.getStrCardName());
		    objBillSettlementDtl.setStrRemark(ob.getStrRemark());
		    objBillSettlementDtl.setStrClientCode(clsGlobalVarClass.gClientCode);
		    objBillSettlementDtl.setStrCustomerCode(customerCodeForCredit);
		    objBillSettlementDtl.setDblActualAmt(newSettleAmt);
		    objBillSettlementDtl.setDblRefundAmt(newSettleAmt);
		    objBillSettlementDtl.setStrGiftVoucherCode(ob.getStrGiftVoucherCode());
		    objBillSettlementDtl.setStrDataPostFlag("N");

		    listObjBillSettlementDtl.add(objBillSettlementDtl);
		}

		settleName = "MultiSettle";
		String sql = "update tblbillhd set strSettelmentMode='" + settleName + "'"
			+ ",strTransactionType=CONCAT(strTransactionType,',','MultiBillPartSettle') "
			+ "where strBillNo='" + billNo + "' "
			+ "and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		clsGlobalVarClass.dbMysql.execute(sql);

		String sqlDelete = "delete from tblbillsettlementdtl "
			+ " where strBillNo='" + billNo + "' "
			+ " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		clsGlobalVarClass.dbMysql.execute(sqlDelete);

	    }
	    funInsertBillSettlementDtlTable(listObjBillSettlementDtl);

	    new frmOkPopUp(this, "Multi Bill Partial Settlement Successfull", "Successfull", 3).setVisible(true);

	    //table status
	    for (int row = 0; row < tblBillDetailsTable.getRowCount(); row++)
	    {
		String billNo = tblBillDetailsTable.getValueAt(row, 0).toString();
		String sqlBillTable = "select a.strBillNo,a.dteBillDate,a.strPOSCode,a.strTableNo,b.strTableName "
			+ "from tblbillhd a,tbltablemaster b "
			+ "where a.strTableNo=b.strTableNo "
			+ "and a.strBillNo='" + billNo + "' ";
		ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillTable);
		if (rsBillTable.next())
		{
		    String tableNo = rsBillTable.getString(4);
		    String tableName = rsBillTable.getString(5);

		    if (!tableNo.isEmpty() && !tableName.isEmpty())
		    {
			String tableStatus = funGetTableStatus(tableNo);
			funUpdateTableStatus(tableNo, tableName, tableStatus);
		    }
		}
		rsBillTable.close();
	    }
	    funResetFields();
	    funBackButtonPressed();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-82", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
	    System.gc();
	}
    }

    private int funInsertBillSettlementDtlTable(List<clsBillSettlementDtl> listObjBillSettlementDtl) throws Exception
    {

	String sqlInsertBillSettlementDtl = "insert into tblbillsettlementdtl "
		+ "(strBillNo,strSettlementCode,dblSettlementAmt,dblPaidAmt,strExpiryDate"
		+ ",strCardName,strRemark,strClientCode,strCustomerCode,dblActualAmt"
		+ ",dblRefundAmt,strGiftVoucherCode,strDataPostFlag,dteBillDate) "
		+ "values ";
	for (clsBillSettlementDtl objBillSettlementDtl : listObjBillSettlementDtl)
	{
	    sqlInsertBillSettlementDtl += "('" + objBillSettlementDtl.getStrBillNo() + "'"
		    + ",'" + objBillSettlementDtl.getStrSettlementCode() + "'," + objBillSettlementDtl.getDblSettlementAmt() + ""
		    + "," + objBillSettlementDtl.getDblPaidAmt() + ",'" + objBillSettlementDtl.getStrExpiryDate() + "'"
		    + ",'" + objBillSettlementDtl.getStrCardName() + "','" + objBillSettlementDtl.getStrRemark() + "'"
		    + ",'" + objBillSettlementDtl.getStrClientCode() + "','" + objBillSettlementDtl.getStrCustomerCode() + "'"
		    + "," + objBillSettlementDtl.getDblActualAmt() + "," + objBillSettlementDtl.getDblRefundAmt() + ""
		    + ",'" + objBillSettlementDtl.getStrGiftVoucherCode() + "','" + objBillSettlementDtl.getStrDataPostFlag() + "','" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'),";
	}
	StringBuilder sb1 = new StringBuilder(sqlInsertBillSettlementDtl);
	int index1 = sb1.lastIndexOf(",");
	sqlInsertBillSettlementDtl = sb1.delete(index1, sb1.length()).toString();

	return clsGlobalVarClass.dbMysql.execute(sqlInsertBillSettlementDtl);
    }

    private void funResetLookAndFeel()
    {
	try
	{
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
	    {
		System.out.println("lookandfeel" + info.getName());
		if ("Nimbus".equals(info.getName()))
		{
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    SwingUtilities.updateComponentTreeUI(this);
		    break;
		}
	    }
	}
	catch (ClassNotFoundException ex)
	{
	}
	catch (InstantiationException ex)
	{
	}
	catch (IllegalAccessException ex)
	{
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
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

    /**
     * This method is used to reset settlement bills
     */
    private void funResetBillDetailField()
    {
	dmBillDetail.setRowCount(0);
    }

    /**
     * This method is used to reset amount details
     */
    private void funResetPaymentDetailField()
    {
	dmPaymentAmtDtl.setRowCount(0);
    }

    /**
     * This method is used to load bill details
     */
    private void funLoadBillDtl(String billno)
    {
	try
	{
	    double totSettleAmt = 0;
	    String sql = "select strBillNo,dblGrandTotal "
		    + "from tblbillhd "
		    + " where strBillNo='" + billno + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillDtl.next())
	    {
		totSettleAmt += Double.parseDouble(rsBillDtl.getString(2));
		Object row[] =
		{
		    rsBillDtl.getString(1), rsBillDtl.getString(2), false
		};
		dmBillDetail.addRow(row);
	    }
	    tblBillDetailsTable.setModel(dmBillDetail);
	    lblTotSettleAmt.setText(String.valueOf(totSettleAmt));
	    rsBillDtl.close();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
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

	    }
	}
	catch (Exception e)
	{

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
	txtAmount.setEnabled(false);
	flgGiftVoucherOK = false;
	txtPaidAmt.requestFocus();

    }

    private void funCalBackSpaceButtonPressed()
    {
	try
	{
	    if (amountBox.equals("txtAmount") && textValue1.length() > 0)
	    {
		StringBuilder sb = new StringBuilder(textValue1);
		sb.delete(textValue1.length() - 1, textValue1.length());
		textValue1 = sb.toString();
		txtAmount.setText(textValue1);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    //  e.printStackTrace();
	}
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-64", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    private int procSettlementBtnClick(clsSettelementOptions objSettelement)
    {
	try
	{

	    lblPaymentModeVal.setText(objSettelement.getStrSettelmentDesc());
	    panelMode.setVisible(true);
	    dblSettlementAmount = 0.00;
	    settleName = objSettelement.getStrSettelmentDesc();
	    settleType = objSettelement.getStrSettelmentType();
	    settlementCode = objSettelement.getStrSettelmentCode();//use while calculating tax for settlement
	    currencyRate = objSettelement.getDblConvertionRatio();
	    billPrintOnSettlement = objSettelement.getStrBillPrintOnSettlement();

//            if (hmSettlemetnOptions.isEmpty())
//            {
//                funRefreshItemTable();
//            }//if the tax changes on settlement mode
	    billTotal = Double.parseDouble(txtAmount.getText());
	    _balanceAmount = billTotal;
	    dblSettlementAmount = Math.rint(_balanceAmount);
	    dblSettlementAmount = dblSettlementAmount * currencyRate;
	    dblSettlementAmount = Math.rint(dblSettlementAmount);
	    //System.out.println(settleType);

//                txtAmount.setText(billTotal);
//                txtPaidAmt.setText(billTotal);
	    switch (settleType)
	    {

		case "Cash":
		    settlementName = "others";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    panelCustomer.setVisible(false);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    panelMode.setVisible(true);
		    txtRemark.requestFocus();
		    PanelRemaks.setLocation(PanelCheque.getLocation());
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
		    panelRoomSettlement.setVisible(false);

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

		    panelRoomSettlement.setVisible(false);
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
		    panelRoomSettlement.setVisible(false);
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
		    //panelAmt.setLocation(PointCard);
		    panelAmt.setVisible(true);
		    PanelCheque.setVisible(true);
		    //PanelCheque.setLocation(PointCash);
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
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
		    PanelGiftVoucher.setVisible(true);
		    PanelGiftVoucher.setLocation(PointCheque);
		    panelRoomSettlement.setVisible(false);
		    break;

		case "Complementary":
		    txtRemark.requestFocus();
		    panelCustomer.setVisible(false);
		    PanelRemaks.setLocation(panelAmt.getLocation());
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
		    panelRoomSettlement.setVisible(false);
		    break;

		case "Credit":

		    objUtility.funCallForSearchForm("CustomerMaster");
		    new frmSearchFormDialog(this, true).setVisible(true);
		    //funOpenCustomerMaster();

		    if (clsGlobalVarClass.gSearchItemClicked)
		    {
			Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
			lblCreditCustCode.setText(data[0].toString());
			customerCodeForCredit = lblCreditCustCode.getText();
			txtCoupenAmt.setText(String.valueOf(dblSettlementAmount));
			txtCustomerName.setText(data[1].toString());
			clsGlobalVarClass.gSearchItemClicked = false;
		    }
		    //PanelCoupen.setLocation(panelAmt.getLocation());
		    PanelCoupen.setVisible(false);
		    panelCustomer.setVisible(true);
		    panelCustomer.setLocation(PanelCheque.getLocation());
		    txtAreaRemark.requestFocus();
		    PanelRemaks.setLocation(panelAmt.getLocation());
		    PanelRemaks.setVisible(true);
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    settlementName = "Credit";
		    amountBox = "PaidAmount";
		    settleMode = true;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
		    break;

		case "Debit Card":
		    settleMode = false;
		    PanelGiftVoucher.setVisible(false);
		    PanelRemaks.setVisible(false);
		    panelCustomer.setVisible(false);
		    lblTipAmount.setVisible(false);
		    PanelCheque.setVisible(false);
		    PanelCard.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
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
				lblCardBalance.setVisible(true);
				lblCardBalance.setText(String.valueOf(debitCardBalance));
				amountBox = "PaidAmount";
				settlementName = "others";
				settleMode = true;
				panelAmt.setVisible(true);
				PanelGiftVoucher.setVisible(false);
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

			new frmSwipCardPopUp(this).setVisible(true);

			if (clsGlobalVarClass.gDebitCardNo != null && !clsGlobalVarClass.gDebitCardNo.trim().isEmpty())
			{
			    clsUtility objUtility = new clsUtility();
			    ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet("select strCardNo from tbldebitcardmaster where strCardString='" + clsGlobalVarClass.gDebitCardNo + "'");
			    if (rsDebitCardNo.next())
			    {
				debitCardNo = rsDebitCardNo.getString(1);
				//double debitCardBalance = objUtility.funGetDebitCardBalance(debitCardNo);
				double debitCardBalance = objUtility.funGetDebitCardBalanceWithoutLiveBills(clsGlobalVarClass.gDebitCardNo, tableNo);

				if (mapDebitCardBalance.containsKey(clsGlobalVarClass.gDebitCardNo))
				{
				    debitCardBalance = mapDebitCardBalance.get(clsGlobalVarClass.gDebitCardNo);
				}

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
				    lblCardBalance.setText(String.valueOf(debitCardBalance));
				    amountBox = "PaidAmount";
				    settlementName = "others";
				    settleMode = true;
				    panelAmt.setVisible(true);
				    PanelGiftVoucher.setVisible(false);
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
		    //flgComplementarySettle = false;

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
		    PanelRemaks.setLocation(PanelCheque.getLocation());
		    PanelRemaks.setVisible(true);
		    panelCustomer.setVisible(false);
		    txtRemark.requestFocus();
		    lblcard.setVisible(false);
		    lblCardBalance.setVisible(false);
		    panelAmt.setVisible(true);
		    PanelCoupen.setVisible(false);
		    PanelGiftVoucher.setVisible(false);
		    PanelCard.setVisible(false);
		    PanelCheque.setVisible(false);
		    txtPaidAmt.setText(String.valueOf(dblSettlementAmount));
		    txtAmount.setText(String.valueOf(dblSettlementAmount));
		    //flgComplementarySettle = false;
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    panelRoomSettlement.setVisible(false);
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
		    lblTipAmount.setVisible(false);
		    txtTip.setVisible(false);
		    lblCreditCustCode.setVisible(false);
		    panelRoomSettlement.setLocation(panelDiscount.getLocation());
		    panelRoomSettlement.setVisible(true);

		    break;
	    }
	    if (noOfSettlementMode == 1)
	    {
		if (clsGlobalVarClass.gTransactionType.equals("Direct Biller"))
		{
		    funEnterButtonPressed();
//                    funSettleButtonPressed();
		}
		else if (clsGlobalVarClass.gTransactionType.equals("Bill From KOT") || clsGlobalVarClass.gTransactionType.equals("SettleBill"))
		{
		    funEnterButtonPressed();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-27", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	return 1;
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-46", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funEnterButtonPressed()
    {
	double tempPaidAmount = 0.00;
	String payMode;
	String balance = "Balance";
	payMode = lblPaymentModeVal.getText();
	if (txtPaidAmt.getText().trim().length() == 0)
	{
	    _paidAmount = 0.00;
	}
	else
	{
	    _paidAmount = Double.parseDouble(txtPaidAmt.getText());
	}
	if (_paidAmount == 0.00 && dblSettlementAmount != 0.00)
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
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);

			refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(_paidAmount));
			//hmSettlemetnOptions.put(settleName, new clsSettelementOptions(_settlementCode, dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", settleType));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
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
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			//lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			//lblTotalVal.setText(String.valueOf(_paidAmount));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, expiryDate, settleName, txtCardName.getText().toString(), "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
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
			    tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    refundAmt = dblSettlementAmount - tempPaidAmt;
			    ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			    //lblTotalVal.setText(String.valueOf(tempPaidAmt));
			    hmSettlemetnOptions.put(settleName, ob);
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			}
			else
			{
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			    //lblTotalVal.setText(String.valueOf(_paidAmount));
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", txtRemark.getText().trim(), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
			}
		    }
		    break;

		case "Cheque":

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
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			//lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
			//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(_paidAmount));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", txtRemark.getText().trim(), _grandTotal, _refundAmount, _giftVoucherSeriesCode.concat(_giftVoucherCode), ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
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
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", txtAreaRemark.getText().trim(), _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
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
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(_paidAmount));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }
		    break;

		case "Debit Card":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(_paidAmount));
			hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
		    }

		    double cardBal = Double.parseDouble(lblCardBalance.getText());
		    cardBal = cardBal - _paidAmount;

		    lblCardBalance.setText(String.valueOf(cardBal));

		    mapDebitCardBalance.put(clsGlobalVarClass.gDebitCardNo, cardBal);
		    clsGlobalVarClass.gDebitCardNo = "";

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
				tempPaidAmt = ob.getDblPaidAmt();
				tempPaidAmt += _paidAmount;
				ob.setDblPaidAmt(tempPaidAmt);
				refundAmt = dblSettlementAmount - tempPaidAmt;
				ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
				//lblTotalVal.setText(String.valueOf(tempPaidAmt));
				hmSettlemetnOptions.put(settleName, ob);
				//_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			    }
			    else
			    {
				clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
				refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
				// lblTotalVal.setText(String.valueOf(_paidAmount));
				hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
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
			    tempPaidAmt = ob.getDblPaidAmt();
			    tempPaidAmt += _paidAmount;
			    ob.setDblPaidAmt(tempPaidAmt);
			    refundAmt = dblSettlementAmount - tempPaidAmt;
			    ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			    //lblTotalVal.setText(String.valueOf(tempPaidAmt));
			    hmSettlemetnOptions.put(settleName, ob);
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, temp_paidAmount, settleType);
			}
			else
			{
			    clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			    refundAmt = dblSettlementAmount - _paidAmount;

//                        Object row[] = {payMode, _paidAmount, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);
//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			    // lblTotalVal.setText(String.valueOf(_paidAmount));
			    hmSettlemetnOptions.put(settleName, new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", ""));
			    //_balanceAmount = fun_get_BalanceAmount(_balanceAmount, _paidAmount, settleType);
			}
		    }
		    break;

		case "Room":
		    if (hmSettlemetnOptions.containsKey(settleName))
		    {
			clsSettelementOptions ob = hmSettlemetnOptions.get(settleName);
			tempPaidAmt = ob.getDblPaidAmt();
			tempPaidAmt += _paidAmount;
			ob.setDblPaidAmt(tempPaidAmt);
			refundAmt = dblSettlementAmount - tempPaidAmt;
			ob.setDblRefundAmt(_refundAmount);
//                        Object row[] = {payMode, tempPaidAmt, false};
//                        Object row1[] = {balance, refundAmt, false};
//                        dmBillAmtDetail.addRow(row);
//                        dmBillAmtDetail.addRow(row1);

//                        txtAmount.setText(String.valueOf(refundAmt));
//                        txtPaidAmt.setText(String.valueOf(refundAmt));
			// lblTotalVal.setText(String.valueOf(tempPaidAmt));
			hmSettlemetnOptions.put(settleName, ob);
		    }
		    else
		    {
			clsSettelementOptions ob = clsSettelementOptions.hmSettelementOptionsDtl.get(settleName);
			clsSettelementOptions objSettleOpt = new clsSettelementOptions(ob.getStrSettelmentCode(), dblSettlementAmount, _paidAmount, "", settleName, "", "", _grandTotal, _refundAmount, "", ob.getStrSettelmentDesc(), ob.getStrSettelmentType(), "", "", "", "", "", "");
			objSettleOpt.setStrFolioNo(txtFolioNo.getText());
			objSettleOpt.setStrRoomNo(txtRoomNo.getText());
			objSettleOpt.setStrGuestCode(txtGuestCode.getText());

			hmSettlemetnOptions.put(settleName, objSettleOpt);
		    }
		    break;
	    }
//            Object row[] = {payMode, tempPaidAmt, false};
//            Object row1[] = {balance, refundAmt, false};
//            dmBillAmtDetail.addRow(row);
//            dmBillAmtDetail.addRow(row1);
	    procClear();

	    funRefreshItemTable();
	    flgEnterBtnPressed = true;
	}
	procClear();
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

    private void funRefreshItemTable()
    {
	try
	{
	    DecimalFormat formt = new DecimalFormat("####0.00");
//            DefaultTableModel dm = new DefaultTableModel()
//            {
//                @Override
//                public boolean isCellEditable(int row, int column)
//                {
//                    //all cells false
//                    return false;
//                }
//            };
//                        Object[] paymentrow =
//            {
//                "Payment Modes", ""
//            };
//            dmBillAmtDetail.addRow(paymentrow);
	    txtAmount.setText(String.valueOf(refundAmt));
	    txtPaidAmt.setText(String.valueOf(refundAmt));
	    dmPaymentAmtDtl.getDataVector().removeAllElements();
	    for (clsSettelementOptions ob : hmSettlemetnOptions.values())
	    {
		String settlementDesc = ob.getStrSettelmentDesc();
		tempPaidAmt = ob.getDblPaidAmt();
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
		    settlementDesc, formt.format(settlementAmt)
		};
		dmPaymentAmtDtl.addRow(row);
	    }
	    double tempBalance = 0;
	    String tempTot = lblTotSettleAmt.getText();
	    tempPaidAmt = Double.parseDouble(tempTot);
	    if (_paidAmount > tempPaidAmt)
	    {
		double tempRefundAmt = funGetTotalPaidAmount() - tempPaidAmt;
		if (settleType.equals("Complementary"))
		{
		    tempRefundAmt = 0;
		}
		Object[] row =
		{
		    "Refund", formt.format(tempRefundAmt)
		};
		dmPaymentAmtDtl.addRow(row);
	    }
	    else
	    {
		tempBalance = tempPaidAmt - funGetTotalPaidAmount();
		if (tempBalance <= 0)
		{
		    tempBalance = 0.00;
		}
		Object[] row =
		{
		    "Balance", formt.format(tempBalance)
		};
		dmPaymentAmtDtl.addRow(row);
	    }
	    tblPaymentDetails.setModel(dmPaymentAmtDtl);

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
		txtPaidAmt.setText(String.valueOf(_balanceAmount));///////////////////////
	    }
	    lblTotalVal.setText(String.valueOf(tempBalance));
//            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//            tblBillDetails.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
//           
//            tblBillDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            tblBillDetails.getColumnModel().getColumn(0).setPreferredWidth(90);
//            tblBillDetails.getColumnModel().getColumn(1).setPreferredWidth(40);
//           
//            tblBillDetails.setShowHorizontalLines(true);
//
//            //System.out.println("SUB Total in Refresh= " + _subTotal);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-17", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-59", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    private void funOpenCustomerMaster()
    {
	new frmCustomerMaster().setVisible(true);
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-55", JOptionPane.ERROR_MESSAGE);
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-57", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-58", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
    }

    public void funSetGiftVoucherData(Object[] data)
    {
	txtVoucherSeries.setText(data[0].toString());
	_giftVoucherSeriesCode = data[1].toString();
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

	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-67", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
	finally
	{
	    return flagDuplicate;
	}
    }

    private void funResetFieldVariables()
    {

	hmBillItemDtl.clear();
	hmSettlemetnOptions.clear();

	_balanceAmount = 0;
	_deliveryCharge = 0;
	_giftVoucherCode = "";
	_grandTotal = 0;
	_netAmount = 0;
	_paidAmount = 0;
	_refundAmount = 0;
	_settlementNavigate = 0;
	_subTotal = 0;
    }

    private void funSetBillData(ArrayList listOfBills) throws Exception
    {

	txtAmount.setText("");
	txtPaidAmt.setText("");
	double txtAmt = 0.0;
	String varBalance = "Balance";
	String paymentMode;

	funResetBillDetailField();

	double totSettleAmt = 0;

	for (int i = 0; i < listOfBills.size(); i++)
	{
	    String billNo = listOfBills.get(i).toString();

	    String sql = "select strBillNo,dblGrandTotal "
		    + "from tblbillhd "
		    + " where strBillNo='" + billNo + "' "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillDtl.next())
	    {
		totSettleAmt += Double.parseDouble(rsBillDtl.getString(2));
		Object row[] =
		{
		    rsBillDtl.getString(1), rsBillDtl.getString(2), false
		};
		dmBillDetail.addRow(row);
	    }
	    tblBillDetailsTable.setModel(dmBillDetail);
	    lblTotSettleAmt.setText(String.valueOf(totSettleAmt));
	    rsBillDtl.close();
	}

	strBillTot = lblTotSettleAmt.getText();
	txtAmount.setText(strBillTot);
	txtPaidAmt.setText(strBillTot);
	paymentMode = lblPaymentModeVal.getText();
	txtAmtVal = txtAmount.getText();
	txtPaidAmtVal = txtPaidAmt.getText();
	txtAmt = Double.parseDouble(txtAmtVal);

	//                txtPaidAmtValue=Double.parseDouble(txtPaidAmtVal);
	Object row[] =
	{
	    varBalance, txtAmt, false
	};

	dmPaymentAmtDtl.addRow(row);
	lblTotalVal.setText(String.valueOf(txtAmt));

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
	jScrollPane3.setVisible(true);
	scrItemDetials.setVisible(true);
	lblTotSettleAmt.setVisible(true);
	lblBillAmount.setVisible(true);
	lblTotal.setVisible(true);
	lblTotalVal.setVisible(true);
	txtPaidAmt.requestFocus();

    }

    private void funBackButtonPressed()
    {
	dispose();
	funResetLookAndFeel();
	clsGlobalVarClass.hmActiveForms.remove("Multi Bill Part Settle");

	objMultiBillSettle.funFillUnsettledBills();
    }

    private String funGetTableStatus(String tableNo)
    {
	String tableStatus = "Normal";
	try
	{
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

	    String posDate = clsGlobalVarClass.getPOSDateForTransaction().split(" ")[0];
	    String posTime = clsGlobalVarClass.getPOSDateForTransaction().split(" ")[1];

	    String sql = "select a.strCustomerCode,CONCAT(a.tmeResTime,' ',a.strAMPM) as reservationtime from tblreservation a "
		    + " where a.strTableNo='" + tableNo + "' "
		    + " and date(a.dteResDate)='" + posDate + "' "
		    + " order by a.strResCode desc "
		    + " limit 1 ";
	    ResultSet rsReserve = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsReserve.next())
	    {
		Date reservationDateTime = simpleDateFormat.parse(rsReserve.getString(2));
		Date posDateTime = new Date();
		String strPOSTime = String.format("%tr", posDateTime);
		posDateTime = simpleDateFormat.parse(strPOSTime);

		if (posDateTime.getTime() > reservationDateTime.getTime())
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
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return tableStatus;
	}
    }

    private int funUpdateTableStatus(String tableNo, String tableName, String status)
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
		sql_updateTableStatus = "select count(*) from tblitemrtemp where strTableNo='" + tableNo + "';";
		ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sql_updateTableStatus);
		rsCount.next();
		int count = rsCount.getInt(1);
		rsCount.close();
		if (count == 0)
		{
		    // no table present in tblitemrtemp so update it to normal
		    sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "',intPaxNo=0 where strTableNo='" + tableNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
		}
		else
		{
		    status = "Occupied";
		    sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "' where strTableNo='" + tableNo + "'";
		    clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
		}
	    }
	    else
	    {
		sql_updateTableStatus = "update tbltablemaster set strStatus='" + status + "',intPaxNo=0 where strTableNo='" + tableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql_updateTableStatus);
	    }

	    //Update Table Status to Inresto POS
	    if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
	    {
		objUtility.funUpdateTableStatusToInrestoApp(tableNo, tableName, status);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-23", JOptionPane.ERROR_MESSAGE);
	}
	return 0;
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
			+ " where a.strCardNo=b.strCardNo ";
		for (String billNo : listOfBills)
		{
		    sql = sql + " and a.strBillNo='" + billNo + "' ";
		}
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
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Code: BS-72", JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	}
	finally
	{
	    return retDebitCardNo;
	}
    }

}
