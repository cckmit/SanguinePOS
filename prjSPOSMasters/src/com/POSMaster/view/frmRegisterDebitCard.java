/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;
import com.POSMaster.controller.nfc.ReaderThread;

public class frmRegisterDebitCard extends javax.swing.JFrame
{

    private String sql;
    double redeemAmt, cardValue, minCharges;
    private String custemerCode = "", extCode = "";
    private Map<String, String> hmCardType;
    clsUtility objUtility = new clsUtility();
    Thread objThread = null;

    /**
     * This method is used to initialize frmRegisterDebitCard
     */
    public frmRegisterDebitCard()
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

	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    java.util.Date dt = new java.util.Date();
	    int day = dt.getDate();
	    int month = dt.getMonth() + 1;
	    int year = dt.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    hmCardType = new HashMap<String, String>();
	    sql = "select strCardTypeCode,strCardName,strCustomerCompulsory "
		    + "from tbldebitcardtype where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	    ResultSet rsFillComboBox = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    cmbCardType.addItem(" ");
	    while (rsFillComboBox.next())
	    {
		hmCardType.put(rsFillComboBox.getString(2), rsFillComboBox.getString(1) + "," + rsFillComboBox.getString(3));
		cmbCardType.addItem(rsFillComboBox.getString(2));
	    }
	    rsFillComboBox.close();
	    txtCardString.requestFocus();
	    funSetShortCutKeys();

	    ReaderThread objReader = new ReaderThread();
	    objThread = new Thread(objReader);
	    objThread.start();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSetShortCutKeys()
    {
	btnCancel.setMnemonic('c');
	btnNew.setMnemonic('s');
	btnReset.setMnemonic('r');

    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    public void setData(Object[] data)
    {
	txtCustomerName.setText(data[1].toString());
	custemerCode = data[0].toString();
	extCode = data[0].toString();
	funCheckCustomerForCard(custemerCode);
    }

    /**
     * This method is used to save register debit card dtl
     */
    private void funSaveButtonPressed()
    {
	try
	{
	    if (!clsGlobalVarClass.validateEmpty(txtCardString.getText()))
	    {
		JOptionPane.showMessageDialog(this, "Swipe the Card");
		txtCardString.requestFocus();
		return;
	    }

	    if (cmbCardType.getSelectedItem() == null || cmbCardType.getSelectedItem().toString().trim().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please select the card type.");
		txtCardString.requestFocus();
		return;
	    }

	    String cardCode = hmCardType.get(cmbCardType.getSelectedItem().toString().trim());
	    String[] spCard = cardCode.split(",");
	    String custCompulsory = spCard[1];
	    String selectedCardCode = spCard[0];
	    if (cmbOperation.getSelectedItem().toString().equals("Register"))
	    {
		if (!clsGlobalVarClass.validateEmpty(txtCardString.getText()))
		{
		    JOptionPane.showMessageDialog(this, "Swipe the Card");
		    txtCardString.requestFocus();
		    return;
		}
		if (!clsGlobalVarClass.validateEmpty(selectedCardCode))
		{
		    JOptionPane.showMessageDialog(this, "Please Select Card Type");
		    return;
		}
		if (custCompulsory.equalsIgnoreCase("Y"))
		{
		    if (!clsGlobalVarClass.validateEmpty(txtCustomerName.getText()))
		    {
			JOptionPane.showMessageDialog(this, "Please Select Custemer");
			return;
		    }
		}
		funRegisterCard(selectedCardCode);
	    }
	    else
	    {
		if (!clsGlobalVarClass.validateEmpty(txtCardString.getText()))
		{
		    new frmOkPopUp(this, "Error", " Please Enter Card No.", 0).setVisible(true);
		    txtCardString.requestFocus();
		}
		else
		{
		    funDelistCard();
		}
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
     * This method is used to check debit card status
     *
     * @param cardNo
     */
    private void funCheckDebitCardStatus(String cardNo)
    {
	try
	{
	    sql = "select count(*) from tbldebitcardmaster where strCardString='" + cardNo + "'";
	    ResultSet rsCardStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCardStatus.next())
	    {
		int count = rsCardStatus.getInt(1);
		if (count > 0)
		{
		    rsCardStatus.close();
		    sql = "select a.strStatus,a.strCustomerCode,a.intPassword,a.strManualNo ,b.strCardName "
			    + "from tbldebitcardmaster a ,tbldebitcardtype b "
			    + "where a.strCardTypeCode=b.strCardTypeCode "
			    + "and a.strCardString='" + cardNo + "' ";
		    rsCardStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsCardStatus.next())
		    {
			if (rsCardStatus.getString(1).equalsIgnoreCase("Deactive"))
			{
			    lblCardStatus.setText("Expired");
			}
			else
			{
			    lblCardStatus.setText("Active");
			}
			txtCustomerName.setText(funGetCustomerName(rsCardStatus.getString(2)));
			txtCustomerName.setEnabled(false);
			txtManualCardNo.setText(rsCardStatus.getString(4));
			cmbCardType.setSelectedItem(rsCardStatus.getString(5));

		    }
		    rsCardStatus.close();
		}
	    }
	    rsCardStatus.close();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to get customer names
     *
     * @param customerCode
     * @return
     */
    private String funGetCustomerName(String customerCode)
    {
	String customerName = "";
	try
	{
	    sql = "select strCustomerName from tblcustomermaster where strCustomerCode='" + customerCode + "'";
	    ResultSet rsCustomerData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustomerData.next())
	    {
		customerName = rsCustomerData.getString(1);
	    }
	    rsCustomerData.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return customerName;
	}
    }

    /**
     * This method is used to check customer for card
     *
     * @param customerCode
     * @return
     */
    private boolean funCheckCustomerForCard(String customerCode)
    {
	boolean flgCustomerCount = false;
	try
	{
	    sql = "select count(*) from tbldebitcardmaster "
		    + "where strCustomerCode='" + customerCode + "' and strStatus='Active'";
	    ResultSet rsCustomerData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustomerData.next())
	    {
		int count = rsCustomerData.getInt(1);
		rsCustomerData.close();
		if (count > 0)
		{
		    flgCustomerCount = true;
		    sql = "select strCardNo from tbldebitcardmaster "
			    + "where strCustomerCode='" + customerCode + "' and strStatus='Active'";
		    rsCustomerData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsCustomerData.next())
		    {
			clsGlobalVarClass.gDebitCardNo = rsCustomerData.getString(1);
			txtCardString.setText(rsCustomerData.getString(1));
		    }
		    rsCustomerData.close();
		}
	    }
	}
	catch (Exception e)
	{
	    flgCustomerCount = false;
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flgCustomerCount;
	}
    }

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
	txtCardString.setText("");
	cmbOperation.setSelectedItem("Register");
	cmbCardType.setSelectedIndex(0);
	txtCustomerName.setText("");
	custemerCode = "";
	txtCustomerName.setEnabled(true);
	cmbCardType.requestFocus();
	clsGlobalVarClass.gDebitCardNo = null;
	txtManualCardNo.setText("");
	lblCardStatus.setText("");

    }

    private long funGetDebitCardNo() throws Exception
    {
	long lastNo = 0;
	sql = "select count(dblLastNo) from tblinternal where strTransactionType='CardNo'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rs.next();
	int cntDelBoyCategory = rs.getInt(1);
	rs.close();
	if (cntDelBoyCategory > 0)
	{
	    sql = "select dblLastNo from tblinternal where strTransactionType='CardNo'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rs.next();
	    long code = rs.getLong(1);
	    code = code + 1;
	    lastNo = code;
	    rs.close();
	}
	else
	{
	    lastNo = 1;
	}
	String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
		+ "where strTransactionType='CardNo'";
	clsGlobalVarClass.dbMysql.execute(updateSql);

	return lastNo;
    }

    /**
     * This method is used to register debit card
     *
     * @param cardCode
     * @throws Exception
     */
    private void funRegisterCard(String cardCode) throws Exception
    {
	//clsGlobalVarClass.dbMysql.funStartTransaction();
	String cardStatus = "";
	if (cmbOperation.getSelectedItem().toString().equalsIgnoreCase("Register"))
	{
	    cardStatus = "Active";
	}
	else
	{
	    cardStatus = "Deactive";
	}
	String sqlCheckDuplicate = "";
	sqlCheckDuplicate = "select strCardNo from tbldebitcardmaster "
		+ "where strCardString='" + txtCardString.getText().trim() + "'";
	ResultSet rsCheckDuplicate = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckDuplicate);
	if (rsCheckDuplicate.next())
	{
	    new frmOkPopUp(this, "This Card Is Already Register", "Error", 0).setVisible(true);
	    txtCardString.setText("");
	    txtCardString.requestFocus();
	    return;
	}
	rsCheckDuplicate.close();

	String manualCardNo = txtManualCardNo.getText().trim();

	if (!manualCardNo.isEmpty())
	{
	    sqlCheckDuplicate = "select strCardNo from tbldebitcardmaster "
		    + "where strManualNo='" + txtManualCardNo.getText().trim() + "'";
	    rsCheckDuplicate = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckDuplicate);
	    if (rsCheckDuplicate.next())
	    {
		new frmOkPopUp(this, "Duplicate manual card no.", "Error", 0).setVisible(true);
		txtManualCardNo.setText("");
		txtManualCardNo.requestFocus();
		return;
	    }
	    rsCheckDuplicate.close();
	}

	if (manualCardNo.isEmpty())
	{
	    manualCardNo = "NA";
	}

	if (!custemerCode.isEmpty())
	{
	    if (funCheckCustomerForCard(custemerCode))
	    {
		JOptionPane.showMessageDialog(this, "This Customer Is Already Registered with another Card");
		txtCustomerName.requestFocus();
		return;
	    }
	}
	if (cardStatus.equals("Active")) //for register card
	{
	    sql = "select dblCardValueFixed,dblMinCharge,right(strCardTypeCode,3) "
		    + "from tbldebitcardtype "
		    + "where strCardTypeCode='" + cardCode + "'";
	    redeemAmt = 0.0;

	    String cardTypeCode = "";
	    ResultSet rsCardValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCardValue.next())
	    {
		cardValue = Double.parseDouble(rsCardValue.getString(1));
		minCharges = Double.parseDouble(rsCardValue.getString(2));
		redeemAmt = redeemAmt - (cardValue + minCharges);
		cardTypeCode = rsCardValue.getString(3);
	    }
	    rsCardValue.close();

	    long lastNo = funGetDebitCardNo();
	    String cardNo = cardTypeCode + String.format("%06d", lastNo);

	    sql = "insert into tbldebitcardmaster (strCardTypeCode,strCardNo,dblRedeemAmt,strStatus,"
		    + "strUserCreated,dteDateCreated,strCustomerCode,strDataPostFlag,strClientCode,strCardString,strManualNo) "
		    + "values('" + cardCode + "','" + cardNo + "','" + redeemAmt + "','" + cardStatus + "'"
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + ",'" + custemerCode + "','N','" + clsGlobalVarClass.gClientCode + "','" + txtCardString.getText() + "','" + manualCardNo + "')";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    if (clsGlobalVarClass.gRFIDInterface.equalsIgnoreCase("Y"))
	    {
		// Post Registered Debit Card from JPOS to RMS
		int rows = funPostDebitCardInfoToRMS();
		if (rows > 0)
		{
		    //clsGlobalVarClass.dbMysql.funCommitTransaction();
		    JOptionPane.showMessageDialog(this, "Entry Added Successfully");
		    funResetFields();
		}
		else
		{
		    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
		}
	    }
	    else
	    {
		//clsGlobalVarClass.dbMysql.funCommitTransaction();
		JOptionPane.showMessageDialog(this, "Entry Added Successfully");
		funResetFields();
	    }
	}

    }

    /**
     * This method is used to post debit card info to RMS
     *
     * @return
     */
    private int funPostDebitCardInfoToRMS()
    {
	Connection conRMS = null;
	String status = "E";
	int insertedRows = 0;
	try
	{
	    String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    conRMS = DriverManager.getConnection(rmsConURL);
	    conRMS.setAutoCommit(false);
	    Statement st = conRMS.createStatement();

	    sql = "select strCustomerCode,strStatus from tbldebitcardmaster "
		    + "where strCardString='" + txtCardString.getText().trim() + "'";
	    ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustCode.next())
	    {
		if (rsCustCode.getString(2).equals("Active"))
		{
		    status = "A";
		}
		else
		{
		    status = "E";
		}
		sql = "insert into tblCustomerDebitCard(strCustomerCode,strDebitCardString,strStatus) "
			+ "values('" + extCode + "','" + txtCardString.getText().trim() + "','" + status + "')";
		System.out.println(sql);
		insertedRows = st.executeUpdate(sql);
	    }
	    rsCustCode.close();
	    conRMS.commit();

	}
	catch (Exception e)
	{
	    try
	    {
		conRMS.rollback();
	    }
	    catch (Exception ex)
	    {
	    }
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		conRMS.close();
	    }
	    catch (Exception ex)
	    {
	    }
	    return insertedRows;
	}
    }

    /**
     * This method is used to update debit card from RMS
     *
     * @return
     */
    private int funDelistDebitCardFromRMS()
    {
	Connection conRMS = null;
	int updatedRows = 0;
	try
	{
	    String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    conRMS = DriverManager.getConnection(rmsConURL);
	    conRMS.setAutoCommit(false);
	    Statement st = conRMS.createStatement();
	    sql = "update tblcustomerdebitcard set strStatus='E' where strDebitCardString='" + txtCardString.getText().trim() + "'";
	    updatedRows = st.executeUpdate(sql);
	    conRMS.commit();

	}
	catch (Exception e)
	{
	    try
	    {
		conRMS.rollback();
	    }
	    catch (Exception ex)
	    {
	    }
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		conRMS.close();
	    }
	    catch (Exception ex)
	    {
	    }
	    return updatedRows;
	}
    }

    /**
     * This method is used to delist card
     *
     * @throws Exception
     */
    private void funDelistCard() throws Exception
    {
	//clsGlobalVarClass.dbMysql.funStartTransaction();
	String cardStatus = "";
	if (cmbOperation.getSelectedItem().toString().equalsIgnoreCase("Register"))
	{
	    cardStatus = "Active";
	}
	else
	{
	    cardStatus = "Deactive";
	}

	String cardNoToDelist = "";
	cardNoToDelist = "select count(*) from tbldebitcardmaster "
		+ "where strCardString='" + txtCardString.getText().trim() + "'and strStatus='Active'";
	ResultSet rsCheckCard = null;
	rsCheckCard = clsGlobalVarClass.dbMysql.executeResultSet(cardNoToDelist);
	if (rsCheckCard.next())
	{
	    int cn1 = rsCheckCard.getInt(1);
	    if (cn1 > 0)
	    {
		//code set stastus of card deactive
		sql = "update tbldebitcardmaster set strStatus='" + cardStatus + "' "
			+ "where strCardString='" + txtCardString.getText().trim() + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
		if (clsGlobalVarClass.gRFIDInterface.equalsIgnoreCase("Y"))
		{
		    if (funDelistDebitCardFromRMS() > 0)
		    {
			//clsGlobalVarClass.dbMysql.funCommitTransaction();
			JOptionPane.showMessageDialog(this, "Card Delist Successfully");
		    }
		    else
		    {
			//clsGlobalVarClass.dbMysql.funRollbackTransaction();
		    }
		    funResetFields();
		}
	    }
	    else
	    {
		//clsGlobalVarClass.dbMysql.funCommitTransaction();
		JOptionPane.showMessageDialog(this, "Card Delist Successfully");
		funResetFields();
	    }
	}
    }

    /**
     * This method is used to select customer
     */
    private void funSelectCustomer()
    {
	try
	{
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("CustomerMaster");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {

		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		setData(data);
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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  
        ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblCardType = new javax.swing.JLabel();
        cmbCardType = new javax.swing.JComboBox();
        lblCardNum = new javax.swing.JLabel();
        btnSwipeCard = new javax.swing.JButton();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblOperation = new javax.swing.JLabel();
        cmbOperation = new javax.swing.JComboBox();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblCardStatus = new javax.swing.JLabel();
        txtCardString = new javax.swing.JPasswordField();
        lblManualNo = new javax.swing.JLabel();
        txtManualCardNo = new javax.swing.JTextField();

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
        lblformName.setText("- Register Debit Card");
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
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Register Debit Card");

        lblCardType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardType.setText("Debit Card Type         : ");

        cmbCardType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCardTypeActionPerformed(evt);
            }
        });
        cmbCardType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCardTypeKeyPressed(evt);
            }
        });

        lblCardNum.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardNum.setText("Card Number             :");

        btnSwipeCard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSwipeCard.setForeground(new java.awt.Color(255, 255, 255));
        btnSwipeCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSwipeCard.setText("Swipe..");
        btnSwipeCard.setToolTipText("Swipe The Card");
        btnSwipeCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSwipeCard.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSwipeCard.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSwipeCardMouseClicked(evt);
            }
        });
        btnSwipeCard.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSwipeCardKeyPressed(evt);
            }
        });

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Customer Name         :");

        txtCustomerName.setEditable(false);
        txtCustomerName.setBackground(new java.awt.Color(204, 204, 204));
        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
            }
        });
        txtCustomerName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCustomerNameActionPerformed(evt);
            }
        });
        txtCustomerName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCustomerNameKeyPressed(evt);
            }
        });

        lblOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperation.setText("Operation                 :");

        cmbOperation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Register", "Delist" }));
        cmbOperation.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperationActionPerformed(evt);
            }
        });
        cmbOperation.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbOperationKeyPressed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Debit Crd Detail");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnNewKeyPressed(evt);
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Debit Card Registration");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        txtCardString.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCardStringActionPerformed(evt);
            }
        });

        lblManualNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblManualNo.setText(" Manual Number        :");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                                .addComponent(lblManualNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(txtManualCardNo))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                                .addComponent(lblCardNum, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtCardString, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCardStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCustomerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCardStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCardNum, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardString, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblManualNo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtManualCardNo))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbCardTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCardTypeActionPerformed

    }//GEN-LAST:event_cmbCardTypeActionPerformed

    private void btnSwipeCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSwipeCardMouseClicked
	// TODO add your handling code here:
	clsUtility obj = new clsUtility();

	if (clsGlobalVarClass.gEnableNFCInterface)
	{
	    //new frmSwipCardPopUp(this, "frmRegisterDebitCard",objReaderer).setVisible(true);
	}
	else
	{
	    new frmSwipCardPopUp(this, "frmRegisterDebitCard").setVisible(true);

	    if (null != clsGlobalVarClass.gDebitCardNo)
	    {
		txtCardString.setText(clsGlobalVarClass.gDebitCardNo);
		if (obj.funValidateDebitCardString(clsGlobalVarClass.gDebitCardNo))
		{
		    funCheckDebitCardStatus(clsGlobalVarClass.gDebitCardNo);
		}
		else
		{
		    JOptionPane.showMessageDialog(this, "Invalid Card No");
		}
	    }
	}
    }//GEN-LAST:event_btnSwipeCardMouseClicked

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerNameMouseClicked
	// TODO add your handling code here:
	if (txtCustomerName.isEnabled())
	{
	    funSelectCustomer();
	}
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed

    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void cmbOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbOperationActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
	// TODO add your handling code here:
	funSaveButtonPressed();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("DebitCardRegister");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	clsGlobalVarClass.gDebitCardNo = null;
	if (null != objThread)
	{
	    objThread.stop();
	}
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("DebitCardRegister");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbCardTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCardTypeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtCardString.requestFocus();
	}

    }//GEN-LAST:event_cmbCardTypeKeyPressed

    private void txtCustomerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyPressed
	// TODO add your handling code here:

    }//GEN-LAST:event_txtCustomerNameKeyPressed

    private void cmbOperationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationKeyPressed
	// TODO add your handling code here:

	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_cmbOperationKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funSaveButtonPressed();
	}
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnSwipeCardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSwipeCardKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{

	}
    }//GEN-LAST:event_btnSwipeCardKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
	// TODO add your handling code here:
	funSaveButtonPressed();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtCardStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCardStringActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtCardStringActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("DebitCardRegister");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("DebitCardRegister");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSwipeCard;
    private javax.swing.JComboBox cmbCardType;
    private javax.swing.JComboBox cmbOperation;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCardNum;
    private javax.swing.JLabel lblCardStatus;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblManualNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOperation;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    public static javax.swing.JPasswordField txtCardString;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtManualCardNo;
    // End of variables declaration//GEN-END:variables

}
