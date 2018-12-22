/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmDebitCardMaster extends javax.swing.JFrame
{

    private String dbtOnCredit, roomCard, complementary, autoTopUp, redeemabelCard, cardInUse, entryCharge, coverCharge, diplomate, RedemptionLimitType;
    private String allowTop, extenValOnTopUp, setExpiryDate, forCurrentFinaceYr, toDate, authorizeCard;
    private String cash, party, creditCard, member, staff, cheque, sql;
    private java.util.Vector vSettlementCode, vSettlementType;
    private double cardValueFixed, minBalance, maxBalance, minCharges, maxRefunds, depositedAmt;
    private Map<String, String> hmSettlementModes = new HashMap<String, String>();
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmDebitCardMaster
     */
    public frmDebitCardMaster()
    {
	initComponents();
	try
	{
	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    Date date1 = new Date();
		    String newstr = String.format("%tr", date1);
		    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
		    lblDate.setText(dateAndTime);
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();
	    vSettlementCode = new java.util.Vector();
	    vSettlementType = new java.util.Vector();
	    lblRedumptionValue.setVisible(false);
	    txtRedeemptionLimit.setVisible(false);

	    DefaultTableModel dmSettlementTable = (DefaultTableModel) tblPaymentMode.getModel();
	    sql = "select strSettelmentCode,strSettelmentDesc from tblsettelmenthd";
	    ResultSet rsSettlementGrid = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSettlementGrid.next())
	    {
		hmSettlementModes.put(rsSettlementGrid.getString(2), rsSettlementGrid.getString(1));
		Object[] ob =
		{
		    rsSettlementGrid.getString(2), false
		};
		dmSettlementTable.addRow(ob);
	    }
	    rsSettlementGrid.close();
	    tblPaymentMode.setModel(dmSettlementTable);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dteToDate.setDate(date);
	    txtCardCode.requestFocus();
	    funSetShortCutKeys();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSetShortCutKeys()
    {
	btnClose.setMnemonic('c');
	btnNew1.setMnemonic('s');
	btnReset.setMnemonic('r');

    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetCardTypeData(Object[] data)
    {
	try
	{
	    sql = "select * from tbldebitcardtype where strCardTypeCode='" + data[0].toString() + "'";
	    ResultSet rsCardCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    funResetFields();
	    if (rsCardCode.next())
	    {
		txtCardCode.setText(rsCardCode.getString(1));
		txtCardName.setText(rsCardCode.getString(2));
		if (rsCardCode.getString(3).equals("Y"))
		{
		    chkDebitOnCredit.setSelected(true);
		}
		else
		{
		    chkDebitOnCredit.setSelected(false);
		}
		if (rsCardCode.getString(4).equals("Y"))
		{
		    chkRoomCard.setSelected(true);
		}
		else
		{
		    chkRoomCard.setSelected(false);
		}
		if (rsCardCode.getString(5).equals("Y"))
		{
		    chkComplementary.setSelected(true);
		}
		else
		{
		    chkComplementary.setSelected(false);
		}
		if (rsCardCode.getString(6).equals("Y"))
		{
		    chkAutoTopUp.setSelected(true);
		}
		else
		{
		    chkAutoTopUp.setSelected(false);
		}
		if (rsCardCode.getString(7).equals("Y"))
		{
		    chkRedeemableCard.setSelected(true);
		}
		else
		{
		    chkRedeemableCard.setSelected(false);
		}
		if (rsCardCode.getString(8).equals("Y"))
		{
		    chkCardInUse.setSelected(true);
		}
		else
		{
		    chkCardInUse.setSelected(false);
		}
		if (rsCardCode.getString(9).equals("Y"))
		{
		    chkEntryCharge.setSelected(true);
		}
		else
		{
		    chkEntryCharge.setSelected(false);
		}
		if (rsCardCode.getString(10).equals("Y"))
		{
		    chkCoverCahrge.setSelected(true);
		}
		else
		{
		    chkCoverCahrge.setSelected(false);
		}
		if (rsCardCode.getString(11).equals("Y"))
		{
		    chkDiplomate.setSelected(true);
		}
		else
		{
		    chkDiplomate.setSelected(false);
		}
		if (rsCardCode.getString(12).equals("Y"))
		{
		    chkAllowTopUp.setSelected(true);
		}
		else
		{
		    chkAllowTopUp.setSelected(false);
		}

		if (rsCardCode.getString(13).equals("Y"))
		{
		    chkExtndValOnTopUp.setSelected(true);

		}
		else
		{
		    chkExtndValOnTopUp.setSelected(false);
		}
		if (rsCardCode.getString(14).equals("Y"))
		{
		    chkSetExpiryDate.setSelected(true);
		}
		else
		{
		    chkSetExpiryDate.setSelected(false);
		}
		if (rsCardCode.getString(16).equals("Y"))
		{
		    chkForCurrentFinaceYr.setSelected(true);
		}
		else
		{
		    chkForCurrentFinaceYr.setSelected(false);
		}
		if (rsCardCode.getString(39).equals("Y"))
		{
		    chkCashCard.setSelected(true);
		}
		else
		{
		    chkCashCard.setSelected(false);
		}

		if (rsCardCode.getString(40).equals("Y"))
		{
		    chkAuthorizeMemberCard.setSelected(true);
		}
		else
		{
		    chkAuthorizeMemberCard.setSelected(false);
		}

		if (rsCardCode.getString(36).equals("Y"))
		{
		    chkCustCompulsory.setSelected(true);
		}
		else
		{
		    chkCustCompulsory.setSelected(false);
		}

		if (rsCardCode.getString(41).equalsIgnoreCase("Y"))
		{
		    chkExpiryTime.setSelected(true);
		}
		else
		{
		    chkExpiryTime.setSelected(false);
		}
		txtExpiryTimeMin.setText(rsCardCode.getString(42));

		txtValidityDays.setText(rsCardCode.getString(17));
		txtCardValueFixed.setText(rsCardCode.getString(18));
		txtMin.setText(rsCardCode.getString(19));
		txtMax.setText(rsCardCode.getString(20));
		txtDepositeAmount.setText(rsCardCode.getString(21));
		txtMinCharges.setText(rsCardCode.getString(22));
		cmbRedeemptionLimit.setSelectedItem(rsCardCode.getString(34));
		txtRedeemptionLimit.setText(rsCardCode.getString(35));
		StringBuilder sb = new StringBuilder(rsCardCode.getString(15));
		int ind = sb.indexOf(" ");
		String dt = sb.substring(0, ind);
		String[] dts = new String[3];
		StringTokenizer stk = new StringTokenizer(dt, "-");
		int j = 0;
		while (stk.hasMoreTokens())
		{
		    dts[j] = stk.nextToken();
		    j++;
		}
		String date1 = dts[2] + "-" + dts[1] + "-" + dts[0];
		java.util.Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
		dteToDate.setDate(date2);
		txtMaxRefundAmt.setText(rsCardCode.getString(29));
		funFillSettlementModeGrid();
		btnNew1.setText("UPDATE");
		btnNew1.setMnemonic('u');
	    }

	    rsCardCode.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to generate debit card code
     *
     * @return long
     * @throws Exception
     */
    private long funGenerateDebitCardCode() throws Exception
    {
	long lastNo = 0;
	String sqlInternal = "select dblLastNo from tblinternal "
		+ "where strTransactionType='DebitCard'";
	ResultSet rsInternal = clsGlobalVarClass.dbMysql.executeResultSet(sqlInternal);
	if (rsInternal.next())
	{
	    lastNo = rsInternal.getLong(1);
	    long updateCounter = lastNo + 1;
	    String updateSql = "update tblinternal set dblLastNo=" + updateCounter + " "
		    + "where strTransactionType='DebitCard'";
	    clsGlobalVarClass.dbMysql.execute(updateSql);

	}
	else
	{
	    String insertSql = "insert into tblinternal(strTransactionType,dblLastNo) values('DebitCard',1)";
	    lastNo = 1;
	    clsGlobalVarClass.dbMysql.execute(insertSql);
	}
	rsInternal.close();
	return lastNo;
    }

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
	try
	{
	    txtCardCode.setText("");
	    txtCardName.setText("");
	    txtCardCode.requestFocus();
	    btnNew1.setMnemonic('s');
	    chkDebitOnCredit.setSelected(false);
	    chkRoomCard.setSelected(false);
	    chkComplementary.setSelected(false);
	    chkEntryCharge.setSelected(false);
	    chkCoverCahrge.setSelected(false);
	    chkForCurrentFinaceYr.setSelected(false);
	    chkAutoTopUp.setSelected(false);
	    chkCardInUse.setSelected(false);
	    chkSetExpiryDate.setSelected(false);
	    chkDiplomate.setSelected(false);
	    chkRedeemableCard.setSelected(false);
	    chkExtndValOnTopUp.setSelected(false);
	    chkAllowTopUp.setSelected(false);
	    chkCashCard.setSelected(false);
	    chkAuthorizeMemberCard.setSelected(false);
	    txtCardValueFixed.setText("0.00");
	    txtMin.setText("0.00");
	    txtMax.setText("0.00");
	    txtValidityDays.setText("");
	    txtDepositeAmount.setText("0.00");
	    txtMinCharges.setText("0.00");
	    txtMaxRefundAmt.setText("0.00");
	    cmbRedeemptionLimit.setSelectedIndex(0);
	    txtRedeemptionLimit.setText("0.00");
	    cardValueFixed = 0.00;
	    minBalance = 0.00;
	    maxBalance = 0.00;
	    minCharges = 0.00;
	    maxRefunds = 0.00;
	    depositedAmt = 0.00;
	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dteToDate.setDate(date);
	    btnNew1.setText("SAVE");
	    for (int i = 0; i < tblPaymentMode.getRowCount(); i++)
	    {
		tblPaymentMode.setValueAt(false, i, 1);
	    }

	    chkExpiryTime.setSelected(false);
	    txtExpiryTimeMin.setText("0");

	}
	catch (Exception e)
	{
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
        panelLayout = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        tabPane = new javax.swing.JTabbedPane();
        panelTabCardDtl = new javax.swing.JPanel();
        chkDebitOnCredit = new javax.swing.JCheckBox();
        txtCardCode = new javax.swing.JTextField();
        lblCardName = new javax.swing.JLabel();
        txtCardName = new javax.swing.JTextField();
        lblReasonCOde = new javax.swing.JLabel();
        chkRoomCard = new javax.swing.JCheckBox();
        chkComplementary = new javax.swing.JCheckBox();
        chkAutoTopUp = new javax.swing.JCheckBox();
        chkCoverCahrge = new javax.swing.JCheckBox();
        chkEntryCharge = new javax.swing.JCheckBox();
        chkCardInUse = new javax.swing.JCheckBox();
        chkRedeemableCard = new javax.swing.JCheckBox();
        chkDiplomate = new javax.swing.JCheckBox();
        chkAllowTopUp = new javax.swing.JCheckBox();
        chkExtndValOnTopUp = new javax.swing.JCheckBox();
        chkForCurrentFinaceYr = new javax.swing.JCheckBox();
        dteToDate = new com.toedter.calendar.JDateChooser();
        chkSetExpiryDate = new javax.swing.JCheckBox();
        lblTransferType = new javax.swing.JLabel();
        txtValidityDays = new javax.swing.JTextField();
        chkCustCompulsory = new javax.swing.JCheckBox();
        chkCashCard = new javax.swing.JCheckBox();
        chkAuthorizeMemberCard = new javax.swing.JCheckBox();
        chkExpiryTime = new javax.swing.JCheckBox();
        txtExpiryTimeMin = new javax.swing.JTextField();
        panelTabCardValue = new javax.swing.JPanel();
        lblCardValue = new javax.swing.JLabel();
        lblCardValueFix = new javax.swing.JLabel();
        txtCardValueFixed = new javax.swing.JTextField();
        lblMinimum = new javax.swing.JLabel();
        txtMin = new javax.swing.JTextField();
        lblMaximum = new javax.swing.JLabel();
        txtMax = new javax.swing.JTextField();
        txtMinCharges = new javax.swing.JTextField();
        lblMinCharges = new javax.swing.JLabel();
        txtDepositeAmount = new javax.swing.JTextField();
        lblDepositeAmount = new javax.swing.JLabel();
        lblMaxRefAmount = new javax.swing.JLabel();
        lblChrgesMode = new javax.swing.JLabel();
        txtMaxRefundAmt = new javax.swing.JTextField();
        cmbRedeemptionLimit = new javax.swing.JComboBox();
        scrollPanePayMode = new javax.swing.JScrollPane();
        tblPaymentMode = new javax.swing.JTable();
        txtRedeemptionLimit = new javax.swing.JTextField();
        lblRedumptionValue = new javax.swing.JLabel();
        lblRedeemptionLimit = new javax.swing.JLabel();
        lblMandetoryForCardValue = new javax.swing.JLabel();
        lblMandetoryForLimit = new javax.swing.JLabel();
        btnNew1 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" Card type Master");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setText("Card Type Master");

        tabPane.setBackground(new java.awt.Color(153, 204, 255));

        panelTabCardDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelTabCardDtl.setOpaque(false);

        chkDebitOnCredit.setBackground(new java.awt.Color(255, 255, 255));
        chkDebitOnCredit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDebitOnCredit.setText("Debit On Credit");
        chkDebitOnCredit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkDebitOnCreditKeyPressed(evt);
            }
        });

        txtCardCode.setEditable(false);
        txtCardCode.setBackground(new java.awt.Color(204, 204, 204));
        txtCardCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardCodeMouseClicked(evt);
            }
        });
        txtCardCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCardCodeKeyPressed(evt);
            }
        });

        lblCardName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardName.setText("Card Name    :");

        txtCardName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCardName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardNameMouseClicked(evt);
            }
        });
        txtCardName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCardNameKeyPressed(evt);
            }
        });

        lblReasonCOde.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReasonCOde.setText("Card Code   :");

        chkRoomCard.setBackground(new java.awt.Color(255, 255, 255));
        chkRoomCard.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkRoomCard.setText("Room Card");
        chkRoomCard.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                chkRoomCardStateChanged(evt);
            }
        });
        chkRoomCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkRoomCardKeyPressed(evt);
            }
        });

        chkComplementary.setBackground(new java.awt.Color(255, 255, 255));
        chkComplementary.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkComplementary.setText("Complimentary");
        chkComplementary.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkComplementaryKeyPressed(evt);
            }
        });

        chkAutoTopUp.setBackground(new java.awt.Color(255, 255, 255));
        chkAutoTopUp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAutoTopUp.setText("Auto Top Up");
        chkAutoTopUp.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkAutoTopUpKeyPressed(evt);
            }
        });

        chkCoverCahrge.setBackground(new java.awt.Color(255, 255, 255));
        chkCoverCahrge.setText("Cover Charge");
        chkCoverCahrge.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkCoverCahrgeKeyPressed(evt);
            }
        });

        chkEntryCharge.setBackground(new java.awt.Color(255, 255, 255));
        chkEntryCharge.setText("Entry Charge");
        chkEntryCharge.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkEntryChargeKeyPressed(evt);
            }
        });

        chkCardInUse.setBackground(new java.awt.Color(255, 255, 255));
        chkCardInUse.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkCardInUse.setText("Card Type In Use");
        chkCardInUse.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkCardInUseKeyPressed(evt);
            }
        });

        chkRedeemableCard.setBackground(new java.awt.Color(255, 255, 255));
        chkRedeemableCard.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkRedeemableCard.setText("Redeemable Card");
        chkRedeemableCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkRedeemableCardKeyPressed(evt);
            }
        });

        chkDiplomate.setBackground(new java.awt.Color(255, 255, 255));
        chkDiplomate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDiplomate.setText("Diplomate");
        chkDiplomate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkDiplomateKeyPressed(evt);
            }
        });

        chkAllowTopUp.setBackground(new java.awt.Color(255, 255, 255));
        chkAllowTopUp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAllowTopUp.setText("Allow Top Up");
        chkAllowTopUp.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkAllowTopUpKeyPressed(evt);
            }
        });

        chkExtndValOnTopUp.setBackground(new java.awt.Color(255, 255, 255));
        chkExtndValOnTopUp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkExtndValOnTopUp.setText("Extended Validity On TopUp");
        chkExtndValOnTopUp.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkExtndValOnTopUpKeyPressed(evt);
            }
        });

        chkForCurrentFinaceYr.setBackground(new java.awt.Color(255, 255, 255));
        chkForCurrentFinaceYr.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkForCurrentFinaceYr.setText("For Current Financial Year");
        chkForCurrentFinaceYr.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkForCurrentFinaceYrKeyPressed(evt);
            }
        });

        dteToDate.setOpaque(false);
        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        chkSetExpiryDate.setBackground(new java.awt.Color(255, 255, 255));
        chkSetExpiryDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSetExpiryDate.setText("Set Expiry date");
        chkSetExpiryDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkSetExpiryDateKeyPressed(evt);
            }
        });

        lblTransferType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTransferType.setText("Validity Days :");

        txtValidityDays.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtValidityDays.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValidityDays.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtValidityDaysMouseClicked(evt);
            }
        });
        txtValidityDays.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtValidityDaysKeyPressed(evt);
            }
        });

        chkCustCompulsory.setBackground(new java.awt.Color(255, 255, 255));
        chkCustCompulsory.setText("Customer Compulsory");
        chkCustCompulsory.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkCustCompulsoryKeyPressed(evt);
            }
        });

        chkCashCard.setBackground(new java.awt.Color(255, 255, 255));
        chkCashCard.setText("Cash Card");
        chkCashCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkCashCardKeyPressed(evt);
            }
        });

        chkAuthorizeMemberCard.setBackground(new java.awt.Color(255, 255, 255));
        chkAuthorizeMemberCard.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAuthorizeMemberCard.setText("Authorize Member Card");
        chkAuthorizeMemberCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkAuthorizeMemberCardKeyPressed(evt);
            }
        });

        chkExpiryTime.setBackground(new java.awt.Color(255, 255, 255));
        chkExpiryTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkExpiryTime.setText("Set Expiry Time (minutes)");
        chkExpiryTime.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkExpiryTimeKeyPressed(evt);
            }
        });

        txtExpiryTimeMin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtExpiryTimeMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtExpiryTimeMin.setText("0");
        txtExpiryTimeMin.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExpiryTimeMinMouseClicked(evt);
            }
        });
        txtExpiryTimeMin.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExpiryTimeMinKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelTabCardDtlLayout = new javax.swing.GroupLayout(panelTabCardDtl);
        panelTabCardDtl.setLayout(panelTabCardDtlLayout);
        panelTabCardDtlLayout.setHorizontalGroup(
            panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkDebitOnCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkRedeemableCard, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69)
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkRoomCard, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkCardInUse, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardDtlLayout.createSequentialGroup()
                                .addComponent(chkDiplomate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(109, 109, 109)
                                .addComponent(chkAllowTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43))
                            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                        .addComponent(lblReasonCOde, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCardCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                            .addComponent(chkExpiryTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtExpiryTimeMin, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTabCardDtlLayout.createSequentialGroup()
                                            .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(chkSetExpiryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtValidityDays, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(71, 71, 71)))
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                .addComponent(lblCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkExtndValOnTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(chkEntryCharge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chkComplementary, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(chkForCurrentFinaceYr, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkCashCard, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkAutoTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkCoverCahrge)
                                    .addComponent(chkCustCompulsory, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(chkAuthorizeMemberCard))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        panelTabCardDtlLayout.setVerticalGroup(
            panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCardCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblReasonCOde, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addComponent(chkDebitOnCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(chkRedeemableCard))
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                .addComponent(chkComplementary, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(chkEntryCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardDtlLayout.createSequentialGroup()
                                .addComponent(chkAutoTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(chkCoverCahrge, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                                .addComponent(chkRoomCard, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(chkCardInUse, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(1, 1, 1)))
                .addGap(31, 31, 31)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkAllowTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkExtndValOnTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkCustCompulsory, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkDiplomate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkSetExpiryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkForCurrentFinaceYr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkCashCard, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addComponent(chkAuthorizeMemberCard, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelTabCardDtlLayout.createSequentialGroup()
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTransferType, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValidityDays, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTabCardDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkExpiryTime, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExpiryTimeMin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59))))
        );

        tabPane.addTab("Card Details", panelTabCardDtl);

        panelTabCardValue.setBackground(new java.awt.Color(255, 255, 255));
        panelTabCardValue.setOpaque(false);

        lblCardValue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblCardValue.setText("Card Values");

        lblCardValueFix.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardValueFix.setText("Card Value Fixed               :  ");

        txtCardValueFixed.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCardValueFixed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCardValueFixed.setText("0.00");
        txtCardValueFixed.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCardValueFixedMouseClicked(evt);
            }
        });
        txtCardValueFixed.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCardValueFixedKeyPressed(evt);
            }
        });

        lblMinimum.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinimum.setText("Minimum                  :");
        lblMinimum.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtMin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMin.setText("0.00");
        txtMin.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMinMouseClicked(evt);
            }
        });
        txtMin.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMinKeyPressed(evt);
            }
        });

        lblMaximum.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaximum.setText("Maximum                         :");

        txtMax.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMax.setText("0.00");
        txtMax.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMaxMouseClicked(evt);
            }
        });
        txtMax.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMaxKeyPressed(evt);
            }
        });

        txtMinCharges.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMinCharges.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMinCharges.setText("0.00");
        txtMinCharges.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMinChargesMouseClicked(evt);
            }
        });
        txtMinCharges.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMinChargesKeyPressed(evt);
            }
        });

        lblMinCharges.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinCharges.setText("Min Charges              : ");
        lblMinCharges.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        txtDepositeAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDepositeAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDepositeAmount.setText("0.00");
        txtDepositeAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDepositeAmountMouseClicked(evt);
            }
        });
        txtDepositeAmount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDepositeAmountKeyPressed(evt);
            }
        });

        lblDepositeAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDepositeAmount.setText("Deposit Amount       :");

        lblMaxRefAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaxRefAmount.setText("Max. Refundable Amount   :");

        lblChrgesMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblChrgesMode.setText("Recharge Modes                :");

        txtMaxRefundAmt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaxRefundAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxRefundAmt.setText("0.00");
        txtMaxRefundAmt.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMaxRefundAmtMouseClicked(evt);
            }
        });
        txtMaxRefundAmt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMaxRefundAmtKeyPressed(evt);
            }
        });

        cmbRedeemptionLimit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NA", "Daily", "Monthly" }));
        cmbRedeemptionLimit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbRedeemptionLimitActionPerformed(evt);
            }
        });
        cmbRedeemptionLimit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbRedeemptionLimitKeyPressed(evt);
            }
        });

        tblPaymentMode.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "PaymentMode", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPaymentMode.setRowHeight(25);
        tblPaymentMode.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblPaymentMode.setSelectionForeground(new java.awt.Color(254, 254, 254));
        scrollPanePayMode.setViewportView(tblPaymentMode);
        if (tblPaymentMode.getColumnModel().getColumnCount() > 0)
        {
            tblPaymentMode.getColumnModel().getColumn(0).setPreferredWidth(200);
        }

        txtRedeemptionLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRedeemptionLimit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRedeemptionLimit.setText("0.00");
        txtRedeemptionLimit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtRedeemptionLimitKeyPressed(evt);
            }
        });

        lblRedumptionValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRedumptionValue.setText("Value                       :");

        lblRedeemptionLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRedeemptionLimit.setText("Redeemption Limit            :");

        lblMandetoryForCardValue.setForeground(new java.awt.Color(255, 51, 51));
        lblMandetoryForCardValue.setText("*");

        lblMandetoryForLimit.setForeground(new java.awt.Color(255, 51, 51));
        lblMandetoryForLimit.setText("*");

        javax.swing.GroupLayout panelTabCardValueLayout = new javax.swing.GroupLayout(panelTabCardValue);
        panelTabCardValue.setLayout(panelTabCardValueLayout);
        panelTabCardValueLayout.setHorizontalGroup(
            panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabCardValueLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addComponent(lblCardValue, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelTabCardValueLayout.createSequentialGroup()
                                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMandetoryForCardValue, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMandetoryForLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardValueLayout.createSequentialGroup()
                                            .addComponent(lblMaxRefAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(30, 30, 30))
                                        .addGroup(panelTabCardValueLayout.createSequentialGroup()
                                            .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(lblChrgesMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblRedeemptionLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                            .addGap(20, 20, 20)))
                                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                                        .addComponent(lblCardValueFix, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCardValueFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cmbRedeemptionLimit, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtMaxRefundAmt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                            .addGroup(panelTabCardValueLayout.createSequentialGroup()
                                .addComponent(lblMaximum, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtMax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardValueLayout.createSequentialGroup()
                .addGap(423, 423, 423)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRedumptionValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMinCharges, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDepositeAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTabCardValueLayout.createSequentialGroup()
                        .addComponent(lblMinimum, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtMin)
                        .addComponent(txtDepositeAmount)
                        .addComponent(txtMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtRedeemptionLimit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(96, 96, 96))
            .addGroup(panelTabCardValueLayout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(scrollPanePayMode, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTabCardValueLayout.setVerticalGroup(
            panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardValueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCardValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addComponent(txtDepositeAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMinimum, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblCardValueFix, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDepositeAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMandetoryForCardValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCardValueFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaximum, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTabCardValueLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtMax, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMinCharges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMinCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMaxRefAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMaxRefundAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbRedeemptionLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtRedeemptionLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRedumptionValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRedeemptionLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMandetoryForLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelTabCardValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblChrgesMode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))
                    .addGroup(panelTabCardValueLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPanePayMode, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        tabPane.addTab("Card Values", panelTabCardValue);

        btnNew1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew1.setForeground(new java.awt.Color(255, 255, 255));
        btnNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew1.setText("SAVE");
        btnNew1.setToolTipText("Save Debit Card Master");
        btnNew1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNew1MouseClicked(evt);
            }
        });
        btnNew1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNew1ActionPerformed(evt);
            }
        });
        btnNew1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnNew1KeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setToolTipText("Close Debit Card Master");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void txtCardCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardCodeMouseClicked
	funCardCode();
    }//GEN-LAST:event_txtCardCodeMouseClicked

    private void txtCardNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardNameMouseClicked
	// TODO add your handling code here:
	funCardName();
    }//GEN-LAST:event_txtCardNameMouseClicked

    private void chkRoomCardStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkRoomCardStateChanged
	// TODO add your handling code here:
    }//GEN-LAST:event_chkRoomCardStateChanged

    private void txtValidityDaysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtValidityDaysMouseClicked
	// TODO add your handling code here:

	if (txtValidityDays.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Long", "Enter Validity Days").setVisible(true);
	    txtValidityDays.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtValidityDays.getText(), "Long", "Enter Validity Days").setVisible(true);
	    txtValidityDays.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtValidityDaysMouseClicked

    private void txtCardValueFixedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCardValueFixedMouseClicked
	// TODO add your handling code here:
	if (txtCardValueFixed.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Card Value Fixed").setVisible(true);
	    txtCardValueFixed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtCardValueFixed.getText(), "Double", "Enter Card Value Fixed").setVisible(true);
	    txtCardValueFixed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtCardValueFixedMouseClicked

    private void txtMinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMinMouseClicked
	// TODO add your handling code here:
	if (txtMin.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Minimum Value").setVisible(true);
	    txtMin.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtMin.getText(), "Double", "Enter Minimum Value").setVisible(true);
	    txtMin.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtMinMouseClicked

    private void txtMaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaxMouseClicked
	// TODO add your handling code here:
	if (txtMax.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Maximum Value").setVisible(true);
	    txtMax.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtMax.getText(), "Double", "Enter Maximum Value").setVisible(true);
	    txtMax.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtMaxMouseClicked

    private void txtMinChargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMinChargesMouseClicked
	// TODO add your handling code here:
	if (txtMinCharges.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Min. Charges").setVisible(true);
	    txtMinCharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtMinCharges.getText(), "Double", "Enter Min. Charges").setVisible(true);
	    txtMinCharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtMinChargesMouseClicked

    private void txtDepositeAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDepositeAmountMouseClicked
	// TODO add your handling code here:
	if (txtDepositeAmount.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Deposit Amount").setVisible(true);
	    txtDepositeAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtDepositeAmount.getText(), "Double", "Enter Deposit Amount").setVisible(true);
	    txtDepositeAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtDepositeAmountMouseClicked

    private void txtMaxRefundAmtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaxRefundAmtMouseClicked
	// TODO add your handling code here:
	if (txtMaxRefundAmt.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Max. Refundable Amount").setVisible(true);
	    txtMaxRefundAmt.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtMaxRefundAmt.getText(), "Double", "Enter Max. Refundable Amount").setVisible(true);
	    txtMaxRefundAmt.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtMaxRefundAmtMouseClicked

    private void cmbRedeemptionLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRedeemptionLimitActionPerformed
	// TODO add your handling code here:
	if (cmbRedeemptionLimit.getSelectedIndex() > 0)
	{
	    lblRedumptionValue.setVisible(true);
	    txtRedeemptionLimit.setVisible(true);

	}
	else
	{
	    lblRedumptionValue.setVisible(false);
	    txtRedeemptionLimit.setVisible(false);
	}
    }//GEN-LAST:event_cmbRedeemptionLimitActionPerformed
    public void funOperationsSaveandUpdate()
    {
	long diff = 0;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date d1 = new Date();
	Date d2 = null;

	try
	{
	    String fromdate = format.format(d1);
	    d1 = format.parse(fromdate);
	    d2 = format.parse(getToDate());
	    diff = d2.getTime() - d1.getTime();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
//	if (diff < 0)
//	{
//	    new frmOkPopUp(this, "Please Enter Valid Date", "Error", 1).setVisible(true);
//	    return;
//	}
	if (Double.parseDouble(txtMax.getText().toString()) < (Double.parseDouble(txtMin.getText().toString())))
	{
	    new frmOkPopUp(this, "Please Enter Valid Value", "Error", 2).setVisible(true);
	    return;
	}
	funbtnSaveUpdateButtonClick();
    }
    private void btnNew1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNew1MouseClicked
	// TODO add your handling code here:
	funOperationsSaveandUpdate();
    }//GEN-LAST:event_btnNew1MouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("DebitCardMaster");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void txtCardCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funCardCode();
	}
	if (evt.getKeyCode() == 10)
	{
	    txtCardName.requestFocus();
	}
    }//GEN-LAST:event_txtCardCodeKeyPressed

    private void txtCardNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funCardName();
	}
	if (evt.getKeyCode() == 10)
	{
	    chkDebitOnCredit.requestFocus();
	}
    }//GEN-LAST:event_txtCardNameKeyPressed

    private void chkDebitOnCreditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkDebitOnCreditKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkRoomCard.requestFocus();
	}
    }//GEN-LAST:event_chkDebitOnCreditKeyPressed

    private void chkRoomCardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkRoomCardKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkComplementary.requestFocus();
	}
    }//GEN-LAST:event_chkRoomCardKeyPressed

    private void chkComplementaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkComplementaryKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkAutoTopUp.requestFocus();
	}
    }//GEN-LAST:event_chkComplementaryKeyPressed

    private void chkAutoTopUpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkAutoTopUpKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkRedeemableCard.requestFocus();
	}
    }//GEN-LAST:event_chkAutoTopUpKeyPressed

    private void chkRedeemableCardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkRedeemableCardKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkCardInUse.requestFocus();
	}
    }//GEN-LAST:event_chkRedeemableCardKeyPressed

    private void chkCardInUseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkCardInUseKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkEntryCharge.requestFocus();
	}
    }//GEN-LAST:event_chkCardInUseKeyPressed

    private void chkEntryChargeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkEntryChargeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkCoverCahrge.requestFocus();
	}
    }//GEN-LAST:event_chkEntryChargeKeyPressed

    private void chkCoverCahrgeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkCoverCahrgeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkDiplomate.requestFocus();
	}
    }//GEN-LAST:event_chkCoverCahrgeKeyPressed

    private void chkDiplomateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkDiplomateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkAllowTopUp.requestFocus();
	}
    }//GEN-LAST:event_chkDiplomateKeyPressed

    private void chkExtndValOnTopUpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkExtndValOnTopUpKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkCustCompulsory.requestFocus();
	}
    }//GEN-LAST:event_chkExtndValOnTopUpKeyPressed

    private void chkCustCompulsoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkCustCompulsoryKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkSetExpiryDate.requestFocus();
	}
    }//GEN-LAST:event_chkCustCompulsoryKeyPressed

    private void chkAllowTopUpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkAllowTopUpKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkExtndValOnTopUp.requestFocus();
	}
    }//GEN-LAST:event_chkAllowTopUpKeyPressed

    private void chkSetExpiryDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkSetExpiryDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    dteToDate.requestFocus();
	}
    }//GEN-LAST:event_chkSetExpiryDateKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteToDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkForCurrentFinaceYr.requestFocus();
	}
    }//GEN-LAST:event_dteToDateKeyPressed

    private void chkForCurrentFinaceYrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkForCurrentFinaceYrKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtValidityDays.requestFocus();
	}
    }//GEN-LAST:event_chkForCurrentFinaceYrKeyPressed

    private void txtValidityDaysKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValidityDaysKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtValidityDays.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Long", "Enter Validity Days").setVisible(true);
		txtValidityDays.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtValidityDays.getText(), "Long", "Enter Validity Days").setVisible(true);
		txtValidityDays.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    btnNew1.requestFocus();
	}
    }//GEN-LAST:event_txtValidityDaysKeyPressed

    private void btnNew1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNew1KeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funOperationsSaveandUpdate();
	}
    }//GEN-LAST:event_btnNew1KeyPressed

    private void txtCardValueFixedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardValueFixedKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtCardValueFixed.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Card Value Fixed").setVisible(true);
		txtCardValueFixed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtCardValueFixed.getText(), "Double", "Enter Card Value Fixed").setVisible(true);
		txtCardValueFixed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    txtDepositeAmount.requestFocus();
	}
    }//GEN-LAST:event_txtCardValueFixedKeyPressed

    private void txtDepositeAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDepositeAmountKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtDepositeAmount.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Deposit Amount").setVisible(true);
		txtDepositeAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtDepositeAmount.getText(), "Double", "Enter Deposit Amount").setVisible(true);
		txtDepositeAmount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    txtMax.requestFocus();
	}
    }//GEN-LAST:event_txtDepositeAmountKeyPressed

    private void txtMaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaxKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtMax.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Maximum Value").setVisible(true);
		txtMax.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtMax.getText(), "Double", "Enter Maximum Value").setVisible(true);
		txtMax.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    txtMin.requestFocus();
	}
    }//GEN-LAST:event_txtMaxKeyPressed

    private void txtMinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtMin.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Minimum Value").setVisible(true);
		txtMin.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtMin.getText(), "Double", "Enter Minimum Value").setVisible(true);
		txtMin.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    txtMaxRefundAmt.requestFocus();
	}
    }//GEN-LAST:event_txtMinKeyPressed

    private void txtMaxRefundAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaxRefundAmtKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtMaxRefundAmt.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Max. Refundable Amount").setVisible(true);
		txtMaxRefundAmt.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtMaxRefundAmt.getText(), "Double", "Enter Max. Refundable Amount").setVisible(true);
		txtMaxRefundAmt.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    txtMinCharges.requestFocus();
	}
    }//GEN-LAST:event_txtMaxRefundAmtKeyPressed

    private void txtMinChargesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinChargesKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (txtMinCharges.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Min. Charges").setVisible(true);
		txtMinCharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtMinCharges.getText(), "Double", "Enter Min. Charges").setVisible(true);
		txtMinCharges.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    cmbRedeemptionLimit.requestFocus();
	}
    }//GEN-LAST:event_txtMinChargesKeyPressed

    private void cmbRedeemptionLimitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbRedeemptionLimitKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew1.requestFocus();
	}
    }//GEN-LAST:event_cmbRedeemptionLimitKeyPressed

    private void txtRedeemptionLimitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRedeemptionLimitKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew1.requestFocus();
	}
    }//GEN-LAST:event_txtRedeemptionLimitKeyPressed

    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
	// TODO add your handling code here:
	funOperationsSaveandUpdate();
    }//GEN-LAST:event_btnNew1ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("DebitCardMaster");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkCashCardKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_chkCashCardKeyPressed
    {//GEN-HEADEREND:event_chkCashCardKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkCashCardKeyPressed

    private void chkAuthorizeMemberCardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkAuthorizeMemberCardKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAuthorizeMemberCardKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("DebitCardMaster");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("DebitCardMaster");
    }//GEN-LAST:event_formWindowClosing

    private void chkExpiryTimeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_chkExpiryTimeKeyPressed
    {//GEN-HEADEREND:event_chkExpiryTimeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkExpiryTimeKeyPressed

    private void txtExpiryTimeMinMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtExpiryTimeMinMouseClicked
    {//GEN-HEADEREND:event_txtExpiryTimeMinMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtExpiryTimeMinMouseClicked

    private void txtExpiryTimeMinKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtExpiryTimeMinKeyPressed
    {//GEN-HEADEREND:event_txtExpiryTimeMinKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtExpiryTimeMinKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew1;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkAllowTopUp;
    private javax.swing.JCheckBox chkAuthorizeMemberCard;
    private javax.swing.JCheckBox chkAutoTopUp;
    private javax.swing.JCheckBox chkCardInUse;
    private javax.swing.JCheckBox chkCashCard;
    private javax.swing.JCheckBox chkComplementary;
    private javax.swing.JCheckBox chkCoverCahrge;
    private javax.swing.JCheckBox chkCustCompulsory;
    private javax.swing.JCheckBox chkDebitOnCredit;
    private javax.swing.JCheckBox chkDiplomate;
    private javax.swing.JCheckBox chkEntryCharge;
    private javax.swing.JCheckBox chkExpiryTime;
    private javax.swing.JCheckBox chkExtndValOnTopUp;
    private javax.swing.JCheckBox chkForCurrentFinaceYr;
    private javax.swing.JCheckBox chkRedeemableCard;
    private javax.swing.JCheckBox chkRoomCard;
    private javax.swing.JCheckBox chkSetExpiryDate;
    private javax.swing.JComboBox cmbRedeemptionLimit;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCardName;
    private javax.swing.JLabel lblCardValue;
    private javax.swing.JLabel lblCardValueFix;
    private javax.swing.JLabel lblChrgesMode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDepositeAmount;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMandetoryForCardValue;
    private javax.swing.JLabel lblMandetoryForLimit;
    private javax.swing.JLabel lblMaxRefAmount;
    private javax.swing.JLabel lblMaximum;
    private javax.swing.JLabel lblMinCharges;
    private javax.swing.JLabel lblMinimum;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReasonCOde;
    private javax.swing.JLabel lblRedeemptionLimit;
    private javax.swing.JLabel lblRedumptionValue;
    private javax.swing.JLabel lblTransferType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelTabCardDtl;
    private javax.swing.JPanel panelTabCardValue;
    private javax.swing.JScrollPane scrollPanePayMode;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblPaymentMode;
    private javax.swing.JTextField txtCardCode;
    private javax.swing.JTextField txtCardName;
    private javax.swing.JTextField txtCardValueFixed;
    private javax.swing.JTextField txtDepositeAmount;
    private javax.swing.JTextField txtExpiryTimeMin;
    private javax.swing.JTextField txtMax;
    private javax.swing.JTextField txtMaxRefundAmt;
    private javax.swing.JTextField txtMin;
    private javax.swing.JTextField txtMinCharges;
    private javax.swing.JTextField txtRedeemptionLimit;
    private javax.swing.JTextField txtValidityDays;
    // End of variables declaration//GEN-END:variables
/**
     * This method is used for card code
     */
    private void funCardCode()
    {
	try
	{
	    funResetFields();
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("CardType");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		btnNew1.setText("UPDATE");
		btnNew1.setMnemonic('u');

		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetCardTypeData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

    }

    /**
     * This method is used for card name
     */
    private void funCardName()
    {
	try
	{
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
	    if (clsGlobalVarClass.gTouchScreenMode)
	    {
		chkDebitOnCredit.requestFocus();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to get date
     *
     * @return string
     */
    private String getToDate()
    {
	Date dt = dteToDate.getDate();
	int d = dt.getDate();
	int m = dt.getMonth() + 1;
	int y = dt.getYear() + 1900;
	toDate = y + "-" + m + "-" + d;
	return toDate;
    }

    /**
     * This method is used to fill payment mode table
     */
    private void funFillSettlementModeGrid()
    {
	try
	{
	    sql = "select b.strSettelmentDesc,a.strApplicable "
		    + " from tbldebitcardsettlementdtl a,tblsettelmenthd b "
		    + " where a.strSettlementCode=b.strSettelmentCode and strCardTypeCode='" + txtCardCode.getText() + "'";
	    clsGlobalVarClass.dbMysql.open("mysql");
	    DefaultTableModel dm1 = (DefaultTableModel) tblPaymentMode.getModel();
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		for (int k = 0; k < tblPaymentMode.getRowCount(); k++)
		{
		    if (rs.getString(2).equals("true") && rs.getString(1).equals(tblPaymentMode.getValueAt(k, 0)))
		    {
			tblPaymentMode.setValueAt(true, k, 1);
		    }
		}
	    }
	    tblPaymentMode.setModel(dm1);
	    rs.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to save or update changes
     */
    private void funbtnSaveUpdateButtonClick()
    {
	try
	{
	    if (btnNew1.getText().equalsIgnoreCase("SAVE"))
	    {
		funSaveDebitCardMasterData();
	    }
	    else
	    {
		funUpdateDebitCardMasterData();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    if (e.getMessage().startsWith("Duplicate entry"))
	    {
		new frmOkPopUp(this, "Card Name is already present", "Error", 1).setVisible(true);
		return;
	    }
	}
    }

    /**
     * This method is used to save debit card master
     */
    private void funSaveDebitCardMasterData()
    {
	try
	{
	    //clsGlobalVarClass.dbMysql.funStartTransaction();
	    dbtOnCredit = "N";
	    roomCard = "N";
	    complementary = "N";
	    autoTopUp = "N";
	    redeemabelCard = "N";
	    cardInUse = "N";
	    entryCharge = "N";
	    coverCharge = "N";
	    diplomate = "N";
	    allowTop = "N";
	    extenValOnTopUp = "N";
	    setExpiryDate = "N";
	    forCurrentFinaceYr = "N";
	    cash = "N";
	    party = "N";
	    creditCard = "N";
	    member = "N";
	    staff = "N";
	    cheque = "N";
	    RedemptionLimitType = "NA";
	    String cashCard = "N";
	    authorizeCard = "N";

	    if (cmbRedeemptionLimit.getSelectedIndex() > 0)
	    {
		RedemptionLimitType = cmbRedeemptionLimit.getSelectedItem().toString();
	    }
	    if (chkDebitOnCredit.isSelected() == true)
	    {
		dbtOnCredit = "Y";
	    }
	    if (chkRoomCard.isSelected() == true)
	    {
		roomCard = "Y";
	    }
	    if (chkComplementary.isSelected() == true)
	    {
		complementary = "Y";
	    }
	    if (chkAutoTopUp.isSelected() == true)
	    {
		autoTopUp = "Y";
	    }
	    if (chkRedeemableCard.isSelected() == true)
	    {
		redeemabelCard = "Y";
	    }
	    if (chkCardInUse.isSelected() == true)
	    {
		cardInUse = "Y";
	    }
	    if (chkEntryCharge.isSelected() == true)
	    {
		entryCharge = "Y";
	    }
	    if (chkCoverCahrge.isSelected() == true)
	    {
		coverCharge = "Y";
	    }
	    if (chkDiplomate.isSelected() == true)
	    {
		diplomate = "Y";
	    }
	    if (chkSetExpiryDate.isSelected() == true)
	    {
		setExpiryDate = "Y";
	    }
	    if (chkExtndValOnTopUp.isSelected() == true)
	    {
		extenValOnTopUp = "Y";
	    }
	    if (chkAllowTopUp.isSelected() == true)
	    {
		allowTop = "Y";
	    }
	    if (chkForCurrentFinaceYr.isSelected() == true)
	    {
		forCurrentFinaceYr = "Y";
	    }
	    if (chkCashCard.isSelected())
	    {
		cashCard = "Y";
	    }
	    if (chkAuthorizeMemberCard.isSelected())
	    {
		authorizeCard = "Y";
	    }
	    if (!clsGlobalVarClass.validateEmpty(txtCardName.getText()))
	    {
		new frmOkPopUp(this, "Enter Card Name Please", "Error", 0).setVisible(true);
		txtCardName.requestFocus();
		return;
	    }
	    boolean flagPaymentMode = false;
	    for (int k = 0; k < tblPaymentMode.getRowCount(); k++)
	    {
		boolean select = Boolean.parseBoolean(tblPaymentMode.getValueAt(k, 1).toString());
		if (select == true)
		{
		    flagPaymentMode = true;
		    break;
		}
	    }

	    if (flagPaymentMode == false)
	    {
		new frmOkPopUp(this, "Please select atleast one Payment Mode", "Error", 1).setVisible(true);
		return;
	    }

	    boolean checkCardValFix, checkMin, checkMax, checkDepositAmt, checkMinCharge, checkMaxRefunadableAmt, checkValidityDays;
	    checkCardValFix = clsGlobalVarClass.validateEmpty(txtCardValueFixed.getText());
	    checkMin = clsGlobalVarClass.validateEmpty(txtMin.getText());
	    checkMax = clsGlobalVarClass.validateEmpty(txtMax.getText());
	    checkDepositAmt = clsGlobalVarClass.validateEmpty(txtDepositeAmount.getText());
	    checkMinCharge = clsGlobalVarClass.validateEmpty(txtMinCharges.getText());
	    checkValidityDays = clsGlobalVarClass.validateEmpty(txtValidityDays.getText());
	    checkMaxRefunadableAmt = clsGlobalVarClass.validateEmpty(txtMaxRefundAmt.getText());

	    if (!(checkCardValFix || checkMin || checkMax || checkDepositAmt || checkMinCharge))
	    {
		new frmOkPopUp(this, "Please Enter Card Value In Card Value Tab", "Error", 0).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;

	    }
	    if (!checkMaxRefunadableAmt)
	    {
		new frmOkPopUp(this, "Please Enter Max. Refundable Amount ", "Error", 0).setVisible(true);
		txtMaxRefundAmt.requestFocus();
		return;

	    }
	    if (!checkValidityDays)
	    {
		new frmOkPopUp(this, "Please Enter Validity Days ", "Error", 0).setVisible(true);
		txtValidityDays.requestFocus();
		return;

	    }
	    if (cmbRedeemptionLimit.getSelectedIndex() > 0 && !clsGlobalVarClass.validateEmpty(txtRedeemptionLimit.getText()))
	    {
		new frmOkPopUp(this, "Please Enter Redeemption Limit ", "Error", 0).setVisible(true);
		txtRedeemptionLimit.requestFocus();
		return;
	    }

	    if (!clsGlobalVarClass.validateNumbers(txtCardValueFixed.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in card value fixed field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtCardValueFixed.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in card value fixed field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtMin.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in Minimum Value field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtMax.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in Maximum Value field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtDepositeAmount.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in Deposited Amount field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtMinCharges.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in Minimum Charges field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }
	    if (!clsGlobalVarClass.validateNumbers(txtValidityDays.getText()))
	    {
		new frmOkPopUp(this, "Enter only numbers in Validity Days field", "Error", 1).setVisible(true);
		txtCardValueFixed.requestFocus();
		return;
	    }

	    double RedeemValue = 0.00;
	    if (!cmbRedeemptionLimit.getSelectedItem().toString().equals("NA"))
	    {
		RedeemValue = Double.parseDouble(txtRedeemptionLimit.getText());
		if (cmbRedeemptionLimit.getSelectedIndex() > 0 && RedeemValue <= 0.00)
		{
		    new frmOkPopUp(this, "Please Enter Redeemption Limit ", "Error", 0).setVisible(true);
		    txtRedeemptionLimit.requestFocus();
		    return;
		}
	    }
	    cardValueFixed = Double.parseDouble(txtCardValueFixed.getText());
	    minBalance = Double.parseDouble(txtMin.getText());
	    maxBalance = Double.parseDouble(txtMax.getText());
	    minCharges = Double.parseDouble(txtMinCharges.getText());
	    maxRefunds = Double.parseDouble(txtMaxRefundAmt.getText());
	    depositedAmt = Double.parseDouble(txtDepositeAmount.getText());
	    long lastNo = funGenerateDebitCardCode();
	    String debitCardCode = "D" + String.format("%07d", lastNo);
	    txtCardCode.setText(debitCardCode);
	    toDate = getToDate();

	    String customerCompusory = "N";
	    if (chkCustCompulsory.isSelected())
	    {
		customerCompusory = "Y";
	    }
	    // authorizeCard= "N";

	    String isExpiryTime = "N";
	    int intExpiryTime = 0;
	    if (chkExpiryTime.isSelected())
	    {
		isExpiryTime = "Y";

		if (!clsGlobalVarClass.validateNumbers(txtExpiryTimeMin.getText()))
		{
		    new frmOkPopUp(this, "Enter only numbers in expiry time field", "Error", 1).setVisible(true);
		    txtExpiryTimeMin.requestFocus();
		    return;
		}
		intExpiryTime = Integer.parseInt(txtExpiryTimeMin.getText());
	    }

	    sql = "insert into tbldebitcardtype (strCardTypeCode,strCardName,strDebitOnCredit,strRoomCard,"
		    + "strComplementary,strAutoTopUp,strRedeemableCard,strCardInUse,strEntryCharge,strCoverCharge,"
		    + "strDiplomate,strAllowTopUp,strExValOnTopUp,strSetExpiryDt,dteExpiryDt,strCurrentFinacialYr,"
		    + "intValidityDays,dblCardValueFixed,dblMinVal,dblMaxVal,dblDepositAmt,dblMinCharge,strPayModCash,"
		    + "strPayModParty,strPayModMember,strPayModCreditCard,strPayModStaff,strPayModCheque,dblMaxRefundAmt,"
		    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strRedemptionLimitType,dblRedemptionLimitValue"
		    + ",strCustomerCompulsory,strClientCode,strDataPostFlag"
		    + ",strCashCard,strAuthorizeMemberCard,strSetExpiryTime,intExpiryTime)"
		    + "values ('" + txtCardCode.getText() + "','" + txtCardName.getText() + "','" + dbtOnCredit + "','" + roomCard + "','"
		    + complementary + "','" + autoTopUp + "','" + redeemabelCard + "','" + cardInUse + "','" + entryCharge + "','" + coverCharge + "','"
		    + diplomate + "','" + allowTop + "','" + extenValOnTopUp + "','" + setExpiryDate + "','" + toDate + "','" + forCurrentFinaceYr + "',"
		    + txtValidityDays.getText() + "," + cardValueFixed + "," + minBalance + "," + maxBalance + ","
		    + depositedAmt + "," + minCharges + ",'" + cash + "','" + party + "','" + member + "','"
		    + creditCard + "','" + staff + "','" + cheque + "'," + txtMaxRefundAmt.getText() + ",'"
		    + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','"
		    + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"
		    + RedemptionLimitType + "'," + txtRedeemptionLimit.getText() + ",'" + customerCompusory + "'"
		    + ",'" + clsGlobalVarClass.gClientCode + "','N','" + cashCard + "','" + authorizeCard + "'"
		    + ",'" + isExpiryTime + "','" + intExpiryTime + "')";
	    //System.out.println(insertQuery);
	    int exc = clsGlobalVarClass.dbMysql.execute(sql);
	    if (exc > 0)
	    {
		for (int k = 0; k < tblPaymentMode.getRowCount(); k++)
		{
		    boolean select = Boolean.parseBoolean(tblPaymentMode.getValueAt(k, 1).toString());
		    String settlementCode = hmSettlementModes.get(tblPaymentMode.getValueAt(k, 0).toString());
		    if (select)
		    {
			sql = "insert into tbldebitcardsettlementdtl "
				+ "( strCardTypeCode,strSettlementCode,strApplicable,strClientCode,strDataPostFlag)"
				+ "values('" + txtCardCode.getText() + "','" + settlementCode + "','" + select + "'"
				+ ",'" + clsGlobalVarClass.gClientCode + "','N')";
			clsGlobalVarClass.dbMysql.execute(sql);
		    }
		}
		new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
		funResetFields();
		//clsGlobalVarClass.dbMysql.funCommitTransaction();
	    }
	}
	catch (Exception e)
	{
	    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to update debit card master
     */
    private void funUpdateDebitCardMasterData()
    {
	try
	{
	    if (txtCardName.getText().length() == 0)
	    {
		new frmOkPopUp(this, "Enter Card Name Please", "Error", 0).setVisible(true);
	    }
	    else
	    {
		//clsGlobalVarClass.dbMysql.funStartTransaction();
		dbtOnCredit = "N";
		roomCard = "N";
		complementary = "N";
		autoTopUp = "N";
		redeemabelCard = "N";
		cardInUse = "N";
		entryCharge = "N";
		coverCharge = "N";
		diplomate = "N";
		allowTop = "N";
		extenValOnTopUp = "N";
		setExpiryDate = "N";
		forCurrentFinaceYr = "N";
		cash = "N";
		party = "N";
		creditCard = "N";
		member = "N";
		staff = "N";
		cheque = "N";
		RedemptionLimitType = "NA";
		String cashCard = "N";
		authorizeCard = "N";

		if (cmbRedeemptionLimit.getSelectedIndex() > 0)
		{
		    RedemptionLimitType = cmbRedeemptionLimit.getSelectedItem().toString();
		}
		if (chkDebitOnCredit.isSelected() == true)
		{
		    dbtOnCredit = "Y";
		}
		if (chkRoomCard.isSelected() == true)
		{
		    roomCard = "Y";
		}
		if (chkComplementary.isSelected() == true)
		{
		    complementary = "Y";
		}
		if (chkAutoTopUp.isSelected() == true)
		{
		    autoTopUp = "Y";
		}
		if (chkRedeemableCard.isSelected() == true)
		{
		    redeemabelCard = "Y";
		}
		if (chkCardInUse.isSelected() == true)
		{
		    cardInUse = "Y";
		}
		if (chkEntryCharge.isSelected() == true)
		{
		    entryCharge = "Y";
		}
		if (chkCoverCahrge.isSelected() == true)
		{
		    coverCharge = "Y";
		}
		if (chkDiplomate.isSelected() == true)
		{
		    diplomate = "Y";
		}
		if (chkSetExpiryDate.isSelected() == true)
		{
		    setExpiryDate = "Y";
		}
		if (chkExtndValOnTopUp.isSelected() == true)
		{
		    extenValOnTopUp = "Y";
		}
		if (chkAllowTopUp.isSelected() == true)
		{
		    allowTop = "Y";
		}
		if (chkForCurrentFinaceYr.isSelected() == true)
		{
		    forCurrentFinaceYr = "Y";
		}
		String customerCompusory = "N";
		if (chkCustCompulsory.isSelected())
		{
		    customerCompusory = "Y";
		}
		if (chkCashCard.isSelected())
		{
		    cashCard = "Y";
		}
		// authorizeCard= "N";
		if (chkAuthorizeMemberCard.isSelected())
		{
		    authorizeCard = "Y";
		}

		String isExpiryTime = "N";
		int intExpiryTime = 0;
		if (chkExpiryTime.isSelected())
		{
		    isExpiryTime = "Y";

		    if (!clsGlobalVarClass.validateNumbers(txtExpiryTimeMin.getText()))
		    {
			new frmOkPopUp(this, "Enter only numbers in expiry time field", "Error", 1).setVisible(true);
			txtExpiryTimeMin.requestFocus();
			return;
		    }
		    intExpiryTime = Integer.parseInt(txtExpiryTimeMin.getText());
		}

		toDate = getToDate();
		sql = "UPDATE tbldebitcardtype SET strCardName = '" + txtCardName.getText()
			+ "',strDebitOnCredit='" + dbtOnCredit + "',strRoomCard='" + roomCard + "',strComplementary='" + complementary + "'"
			+ ",strAutoTopUp='" + autoTopUp + "',strRedeemableCard='" + redeemabelCard + "',strCardInUse='" + cardInUse + "'"
			+ ",strEntryCharge='" + entryCharge + "',strCoverCharge='" + coverCharge + "',strDiplomate='" + diplomate + "'"
			+ ",strAllowTopUp='" + allowTop + "',strExValOnTopUp='" + extenValOnTopUp + "',strSetExpiryDt='" + setExpiryDate + "'"
			+ ",dteExpiryDt='" + toDate + "',strCurrentFinacialYr='" + forCurrentFinaceYr + "',intValidityDays=" + txtValidityDays.getText()
			+ ",dblCardValueFixed=" + txtCardValueFixed.getText() + ",dblMinVal=" + txtMin.getText() + ",dblMaxVal=" + txtMax.getText()
			+ ",dblDepositAmt=" + txtDepositeAmount.getText() + ",dblMinCharge=" + txtMinCharges.getText() + ",dblMaxRefundAmt=" + txtMaxRefundAmt.getText()
			+ ",strPayModCash='" + cash + "',strPayModParty='" + party + "',strPayModMember='" + member + "',strPayModCreditCard='" + creditCard + "'"
			+ ",strPayModStaff='" + staff + "',strPayModCheque='" + cheque + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
			+ ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strRedemptionLimitType='" + RedemptionLimitType + "'"
			+ ",dblRedemptionLimitValue='" + txtRedeemptionLimit.getText() + "',strCustomerCompulsory='" + customerCompusory + "'"
			+ ",strDataPostFlag='N',strCashCard='" + cashCard + "',strAuthorizeMemberCard='" + authorizeCard + "' "
			+ ",strSetExpiryTime='" + isExpiryTime + "',intExpiryTime='" + intExpiryTime + "'"
			+ "where strCardTypeCode ='" + txtCardCode.getText() + "' "
			+ "and strClientCode='" + clsGlobalVarClass.gClientCode + "'";

		int exc = clsGlobalVarClass.dbMysql.execute(sql);
		if (exc > 0)
		{
		    sql = "delete from tbldebitcardsettlementdtl where strCardTypeCode='" + txtCardCode.getText() + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    int PaymentMode = tblPaymentMode.getRowCount();
		    for (int k = 0; k < PaymentMode; k++)
		    {
			boolean select = Boolean.parseBoolean(tblPaymentMode.getValueAt(k, 1).toString());
			String settlementCode = hmSettlementModes.get(tblPaymentMode.getValueAt(k, 0).toString());
			if (select == true)
			{
			    sql = "insert into tbldebitcardsettlementdtl "
				    + "( strCardTypeCode,strSettlementCode,strApplicable,strClientCode,strDataPostFlag)"
				    + "values('" + txtCardCode.getText() + "','" + settlementCode + "','" + select + "'"
				    + ",'" + clsGlobalVarClass.gClientCode + "','N')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
		    }
		    //clsGlobalVarClass.dbMysql.funCommitTransaction();
		}
		new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
		funResetFields();
	    }
	}
	catch (Exception e)
	{
	    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
}
