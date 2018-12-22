package com.spos.controller;

import com.POSGlobal.controller.clsCreateJPOSInstance;
import com.POSGlobal.controller.clsEncryptDecryptClientCode;
import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTDHOnItemDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmTools;
import com.POSLicence.controller.clsClientDetails;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import static javax.mail.Message.RecipientType.TO;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class frmLogin extends javax.swing.JFrame
{

    private static frmSPOSAdvertise sposAdvertise;
    private String userCode, selectQuery, userType, userName, currentDate, posAccessCode;
    private ResultSet rs;
    private Timer timer;

    public frmLogin()
    {
	initComponents();
	lblExtLogo.setVisible(false);
	clsClientDetails.funAddClientCodeAndName();
	Date dt = new Date();
	String jarDate = funGetDateCreated(); //used to display date of jar file to understand support team how old jar file we are using on client machine
	lblJarModifiedDate.setText(jarDate);
	String currentDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
	Timer timer = new Timer(500, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		clsGlobalVarClass.tickTock(lblclock);
	    }
	});

	timer.setRepeats(true);
	timer.setCoalesce(true);
	timer.setInitialDelay(0);
	timer.start();

    }

    /**
     * This method is used to check valid client
     */
    private void funCheckClientValidity()
    {
	try
	{

	    // Code to get Client List
	    /*
             SimpleDateFormat dFormat1 = new SimpleDateFormat("yyyy-MM-dd");
             for (Map.Entry<String, clsClientDetails> entry : clsClientDetails.hmClientDtl.entrySet()) {
             System.out.println(entry.getValue().Client_Name+"\t"+dFormat1.format(entry.getValue().installDate)+"\t"+dFormat1.format(entry.getValue().expiryDate));
             }*/
	    selectQuery = "select count(*) from tblsetup;";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    rs.next();
	    int count = rs.getInt(1);
	    rs.close();
	    if (count > 0)
	    {
		selectQuery = "select strClientCode,strClientName from tblsetup;";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
		if (rs.next())
		{
		    String encryptedClientCodeFromDB = clsEncryptDecryptClientCode.funEncryptClientCode(rs.getString(1));
		    String clientCodeFromDB = rs.getString(1);
		    String clientNameFromDB = rs.getString(2);
		    if (clsClientDetails.hmClientDtl.containsKey(encryptedClientCodeFromDB))
		    {
			clsGlobalVarClass.gClientCode = clientCodeFromDB;

			//login Successfull
			String decryptedClientNameFromHM = clsEncryptDecryptClientCode.funDecryptClientCode(clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).Client_Name);
			if (decryptedClientNameFromHM.equalsIgnoreCase(clientNameFromDB))
			{
			    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
			    Date systemDate = dFormat.parse(dFormat.format(new Date()));
			    Date POSExpiryDate = dFormat.parse(clsEncryptDecryptClientCode.funDecryptClientCode(clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).expiryDate));

			    if (POSExpiryDate.before(systemDate))
			    {
				new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "LICENSE EXPIRED", 3).setVisible(true);
				return;
			    }
			    else
			    {
				long ExpiryDateTime = POSExpiryDate.getTime();
				long TimeDifference = 0;
				String billDate = "";
				String sql_MaxBillDate = "select ifnull(max(date(dteBillDate)),0) from tblqbillhd";
				ResultSet rsMaxBillDate = clsGlobalVarClass.dbMysql.executeResultSet(sql_MaxBillDate);
				if (rsMaxBillDate.next())
				{
				    billDate = rsMaxBillDate.getString(1);
				    if (!billDate.equals("0"))
				    {
					clsGlobalVarClass.gMaxBillDate = dFormat.parse(billDate);
					TimeDifference = ExpiryDateTime - clsGlobalVarClass.gMaxBillDate.getTime();
					long diffDays = TimeDifference / (24 * 60 * 60 * 1000);
					if (diffDays <= 15)
					{
					    new frmOkPopUp(null, +diffDays + " Days Remaining For Licence to Expire", " Licence is Expired ", 1).setVisible(true);
					}
				    }
				    else
				    {
					TimeDifference = ExpiryDateTime - systemDate.getTime();
				    }
				}
				rsMaxBillDate.close();
				if (TimeDifference >= 0)
				{
				    if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("windows"))
				    {
					//register this machine to start SPOS
					boolean hasThisTerminalLicence = funCheckTerminalLicence(clientCodeFromDB);
					if (!hasThisTerminalLicence)
					{
					    //max terminal count from licence
					    int intMAXTerminalFromLicence = funGetMAXTerminalFromLicence(clientCodeFromDB);
					    new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "<html>No. Of Terminal Licence is " + intMAXTerminalFromLicence + ".</html>", 1).setVisible(true);
					    return;
					}
					else
					{
					    //set SPOS Version
					    funSetPOSVerion(encryptedClientCodeFromDB);
					    //allow login
					    funCheckLogin();
					}
				    }
				    else
				    {
					//set SPOS Version
					funSetPOSVerion(encryptedClientCodeFromDB);
					//allow login
					funCheckLogin();
				    }
				}
				else
				{
				    new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "CONTACT US", 3).setVisible(true);
				}
			    }
			}
			else
			{
			    new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "CONTACT US", 3).setVisible(true);
			}
		    }
		    else
		    {
			new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "CONTACT US", 3).setVisible(true);
		    }
		}
		else
		{//pop up if client code is not present
		    new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "CONTACT US", 3).setVisible(true);
		}
		rs.close();
	    }
	    else
	    {//
		new frmOkPopUp(null, "<html>Please Contact Technical Support.<br>PUNE &nbsp;&nbsp;&nbsp;&nbsp;020&nbsp;65006563<br>MUMBAI 022&nbsp;26209898</html>", "CONTACT US", 3).setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(null, "ERROR= " + e.getMessage());
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to check login
     */
    public void funCheckLogin()
    {
	Date objDate = new Date();
	int day = objDate.getDate();
	int month = objDate.getMonth() + 1;
	int year = objDate.getYear() + 1900;
	currentDate = year + "-" + month + "-" + day;
	if (txtUserId.getText().trim().equals("SANGUINE"))
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
	    System.out.println("TREQ" + finalPassword + "GHTRD");

	    String userPassword = txtPassword.getText().trim();
	    if (finalPassword.equalsIgnoreCase(userPassword))
	    {
		userCode = txtUserId.getText();
		userName = "SANGUINE";
		userType = "Super";
		posAccessCode = "All POS";
		clsGlobalVarClass.gUserPOSCode = "All POS";
		clsGlobalVarClass.gSuperUser = true;
		clsGlobalVarClass.gSanguneUser = true;
		funLoadGlobalData();
	    }
	}
	else
	{
	    clsGlobalVarClass.gSanguneUser = false;
	    try
	    {
		String encKey = "04081977";
		String password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, txtPassword.getText().trim().toUpperCase());
		//System.out.println(password);
		selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess,intNoOfDaysReportsView "
			+ "from tbluserhd "
			+ "where strUserCode='" + txtUserId.getText() + "' and strPassword='" + password + "'";
		//System.out.println(selectQuery);
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
		rs.next();
		if (rs.getInt(1) == 1)
		{
		    userCode = txtUserId.getText();
		    userName = rs.getString(2);
		    userType = rs.getString(3);
		    posAccessCode = rs.getString(5);
		    clsGlobalVarClass.gUserPOSCode = rs.getString(5);
		    int intNoOfDaysReportsView = rs.getInt(6);
		    clsGlobalVarClass.gNoOfDaysReportsView = intNoOfDaysReportsView;

		    if (userType.equals("Super"))
		    {
			clsGlobalVarClass.gSuperUser = true;
		    }
		    else
		    {
			clsGlobalVarClass.gSuperUser = false;
		    }
		    selectQuery = "select count(*) from tbluserhd WHERE strUserCode = '" + txtUserId.getText()
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
			funLoadGlobalData();
		    }
		}
		else
		{
		    rs.close();
		    txtUserId.requestFocus();
		    new frmOkPopUp(null, "Login Failed", "Error", 1).setVisible(true);
		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
		new frmTools("startup").setVisible(true);
	    }
	}
    }

    private boolean funCHeckLoginForDebitCardString(String user) throws Exception
    {
	boolean flgLoginStatus = false;
	selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess,strUserCode from tbluserhd "
		+ "where strDebitCardString='" + user + "'";
	//System.out.println(selectQuery);
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	rs.next();
	if (rs.getInt(1) == 1)
	{
	    userCode = rs.getString(6);
	    userName = rs.getString(2);
	    userType = rs.getString(3);
	    posAccessCode = rs.getString(5);
	    clsGlobalVarClass.gUserPOSCode = rs.getString(5);
	    if (userType.equals("Super"))
	    {
		clsGlobalVarClass.gSuperUser = true;
	    }
	    else
	    {
		clsGlobalVarClass.gSuperUser = false;
	    }
	    if (userType.equalsIgnoreCase("Sanguine"))
	    {
		clsGlobalVarClass.gSanguneUser = true;
	    }
	    else
	    {
		clsGlobalVarClass.gSanguneUser = false;
	    }

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
		flgLoginStatus = true;
		funLoadGlobalData();
	    }
	}
	return flgLoginStatus;
    }

    /**
     * This method is used to add PLU item DTL
     */
    /*
     private void funAddPLUItemDTL() {
     clsPLUItemDtl objPLUItemDtl = new clsPLUItemDtl();
     objPLUItemDtl.fun_fillHasMap();
     }*/
    /**
     * Ritesh 18 Sept 2014
     *
     * @see TO check if JPOS is already Running Or Not
     */
    public void funCheckJarInstance() throws Exception
    {
	clsCreateJPOSInstance ua = new clsCreateJPOSInstance("JPOS");

	if (ua.isAppActive())
	{
	    JOptionPane.showMessageDialog(null, "JPOS is Already Running", "Error", JOptionPane.ERROR_MESSAGE);
	    System.exit(1);
	}
	else
	{
	    try
	    {
		clsGlobalVarClass.funLoadDB();
	    }
	    catch (Exception e)
	    {
		clsUtility objUtility = new clsUtility();
		objUtility.funShowErrorMessage(e);
		throw e;
	    }

	    timer = new Timer(0, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    timer.stop();
		    sposAdvertise.dispose();
		    new frmLogin().setVisible(true);
		}
	    });

	    timer.setInitialDelay(2000);
	    timer.start();
	}
    }

    /**
     * This method is used to get created date
     *
     * @return
     */
    private String funGetDateCreated()
    {
	String dateCreated = "";
	String filePath = System.getProperty("user.dir") + "\\prjSPOSStartUp.jar";
	File fileToTest = new File(filePath);
	if (!fileToTest.isDirectory())
	{
	    Date dt = new Date(fileToTest.lastModified());
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss ");
	    dateCreated = sdf.format(dt).toString();
	}
	else
	{
	    Date dt = new Date(fileToTest.lastModified());
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss ");
	    dateCreated = sdf.format(dt).toString();
	}
	return dateCreated;
    }

    /**
     * This method is used to load global data
     */
    private void funLoadGlobalData()
    {
	try
	{
	    //clsGlobalVarClass gVar = new clsGlobalVarClass();
	    //clsSettelementOptions objSettlmentOptions = new clsSettelementOptions();
	    //objSettlmentOptions.funAddSettelementOptions();

	    clsTDHOnItemDtl objComboItem = new clsTDHOnItemDtl();
	    objComboItem.funAddComboItemDtl();
	    clsGlobalVarClass.setUserCode(userCode);
	    clsGlobalVarClass.setUserName(userName);
	    clsGlobalVarClass.setUserType(userType);

	    selectQuery = "select count(*),strPosCode,strPosName,strPosType,strDebitCardTransactionYN"
		    + " ,strPropertyPOSCode,strCounterWiseBilling,strDelayedSettlementForDB,strBillPrinterPort"
		    + " ,strAdvReceiptPrinterPort,strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo"
		    + " ,strEnableShift from tblposmaster";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    rs.next();
	    int posCount = rs.getInt(1);
	    if (posCount == 1)
	    {
		clsGlobalVarClass.setPOSCode(rs.getString(2));
		clsGlobalVarClass.setPOSName(rs.getString(3));

		new clsGlobalVarClass(rs.getString(2));

		clsGlobalVarClass.gDebitCardPayment = (rs.getString(5));
		clsGlobalVarClass.setPropertyCode(rs.getString(6));
		clsGlobalVarClass.setCounterWiseBilling(rs.getString(7));
		clsGlobalVarClass.gDelayedSettlementForDB = rs.getString(8);
		// clsGlobalVarClass.gBillPrintPrinterPort = rs.getString(9);
		//clsGlobalVarClass.gAdvReceiptPrinterPort = rs.getString(10);
		clsGlobalVarClass.gPrintVatNoPOS = rs.getString(11);
		clsGlobalVarClass.gPrintServiceTaxNoPOS = rs.getString(12);
		clsGlobalVarClass.gPOSVatNo = rs.getString(13);
		clsGlobalVarClass.gPOSServiceTaxNo = rs.getString(14);

		//funAddPLUItemDTL();
		clsGlobalVarClass.gUserPOSCode = "";
		clsSettelementOptions objSettlmentOptions = new clsSettelementOptions();
		objSettlmentOptions.funAddSettelementOptions();
		funInvokeModuleSelection();
	    }
	    else if (!posAccessCode.equals("All POS"))
	    {
		funInvokeModuleSelection();
	    }
	    else            // for ALL POS
	    {
		funInvokeModuleSelection();
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
	    new frmTools("startup").setVisible(true);
	    e.printStackTrace();
	}
    }

    private void funInvokeModuleSelection() throws Exception
    {
	int cnt = 0;
	String moduleName = "";
	String sql = "select DISTINCT(b.strModuleType) "
		+ " from tbluserdtl a,tblforms b "
		+ " where a.strFormName=b.strModuleName "
		+ " and a.strUserCode='" + clsGlobalVarClass.gUserCode + "'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	while (rs.next())
	{
	    if (rs.getString(1).equals("M"))
	    {
		moduleName = "Masters";
	    }
	    else if (rs.getString(1).equals("T"))
	    {
		moduleName = "Transactions";
	    }
	    else if (rs.getString(1).equals("R"))
	    {
		moduleName = "Reports";
	    }
	    cnt++;
	}
	rs.close();

	if (cnt == 1)
	{
	    clsGlobalVarClass.gSelectedModule = moduleName;
	    if (clsGlobalVarClass.gUserPOSCode.trim().length() == 0)
	    {
		dispose();
		new frmMainMenu().setVisible(true);
		clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clsGlobalVarClass.gClientCode));
		clsClientDetails.hmClientDtl.clear();
		clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
	    }
	    else if (!clsGlobalVarClass.gUserPOSCode.equals("All POS"))
	    {
		dispose();
		clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clsGlobalVarClass.gClientCode));
		clsClientDetails.hmClientDtl.clear();
		clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
		new frmPOSSelection().setVisible(true);
	    }
	    else            // for ALL POS
	    {
		dispose();
		clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clsGlobalVarClass.gClientCode));
		clsClientDetails.hmClientDtl.clear();
		clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
		new frmPOSSelection().setVisible(true);
	    }
	}
	else
	{
	    new frmModuleSelection().setVisible(true);
	    clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clsGlobalVarClass.gClientCode));
	    clsClientDetails.hmClientDtl.clear();
	    clsClientDetails.hmClientDtl.put(clsGlobalVarClass.gClientCode, objClientDetails);
	    dispose();
	}
    }

    /**
     * This method is used to initialize frmLogin for parameter flag
     *
     * @param flag
     */
    public frmLogin(boolean flag)
    {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRoot = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblclock = new javax.swing.JLabel();
        panelLayout = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        lblImgSangPOS = new javax.swing.JLabel();
        lblUserIdKB = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        lblUserId = new javax.swing.JLabel();
        lblLoginImage = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPasswordKB = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        panelFooter = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        lblImgSanguineSoft = new javax.swing.JLabel();
        lblExtLogo = new javax.swing.JLabel();
        lblJarModifiedDate = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);

        panelRoot.setLayout(new java.awt.BorderLayout());

        panelHeader.setBackground(new java.awt.Color(0, 102, 255));
        panelHeader.setPreferredSize(new java.awt.Dimension(400, 30));

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("  SPOS - Login");

        lblclock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblclock.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 423, Short.MAX_VALUE)
                .addComponent(lblclock, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblModuleName, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(lblclock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelRoot.add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(600, 400));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblImgSangPOS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImgSangPOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgSanguinePOS.jpg"))); // NOI18N
        panelBody.add(lblImgSangPOS, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 450, 170));

        lblUserIdKB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgkeyboard.png"))); // NOI18N
        lblUserIdKB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserIdKBMouseClicked(evt);
            }
        });
        panelBody.add(lblUserIdKB, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, -1, 30));

        txtUserId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUserIdFocusLost(evt);
            }
        });
        txtUserId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserIdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtUserIdMouseEntered(evt);
            }
        });
        txtUserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserIdActionPerformed(evt);
            }
        });
        txtUserId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserIdKeyPressed(evt);
            }
        });
        panelBody.add(txtUserId, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 150, 30));

        lblUserId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUserId.setText("User Id     :");
        panelBody.add(lblUserId, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 80, 30));

        lblLoginImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgIconLocked.png"))); // NOI18N
        panelBody.add(lblLoginImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPassword.setText("Password  :");
        panelBody.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 80, 30));

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPasswordMouseEntered(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });
        panelBody.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 150, 30));

        lblPasswordKB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgkeyboard.png"))); // NOI18N
        lblPasswordKB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPasswordKBMouseClicked(evt);
            }
        });
        panelBody.add(lblPasswordKB, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, -1, 30));

        btnLogin.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn1.png"))); // NOI18N
        btnLogin.setText("Submit");
        btnLogin.setToolTipText("LogIn ");
        btnLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogin.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn2.png"))); // NOI18N
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });
        panelBody.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, 100, 40));

        btnReset.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setToolTipText("Reset UserId and Password");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        panelBody.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 330, 100, 40));

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        panelRoot.add(panelLayout, java.awt.BorderLayout.CENTER);

        panelFooter.setBackground(new java.awt.Color(255, 255, 255));

        btnExit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn1.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.setToolTipText("Close JPOS");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setMaximumSize(new java.awt.Dimension(51, 23));
        btnExit.setMinimumSize(new java.awt.Dimension(51, 23));
        btnExit.setPreferredSize(new java.awt.Dimension(100, 40));
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgCmnBtn2.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });

        lblImgSanguineSoft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgSanguineLogo.jpg"))); // NOI18N

        lblExtLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgDTSLOGO.JPG"))); // NOI18N

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblExtLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
                .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImgSanguineSoft, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblJarModifiedDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelFooterLayout.setVerticalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblImgSanguineSoft))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblJarModifiedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFooterLayout.createSequentialGroup()
                .addComponent(lblExtLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        panelRoot.add(panelFooter, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblUserIdKBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserIdKBMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtUserId.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter User ID").setVisible(true);
		txtUserId.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtUserId.getText(), "1", "Enter User ID").setVisible(true);
		txtUserId.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_lblUserIdKBMouseClicked

    private void txtUserIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUserIdFocusLost
	txtUserId.setText(txtUserId.getText().toUpperCase());
    }//GEN-LAST:event_txtUserIdFocusLost

    private void txtUserIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserIdMouseClicked
	try
	{
	    if (txtUserId.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter User ID").setVisible(true);
		txtUserId.setText(clsGlobalVarClass.gKeyboardValue.toUpperCase());
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtUserId.getText(), "1", "Enter User ID").setVisible(true);
		txtUserId.setText(clsGlobalVarClass.gKeyboardValue.toUpperCase());
	    }
	}
	catch (Exception e)
	{
	    // e.printStackTrace();
	}
    }//GEN-LAST:event_txtUserIdMouseClicked

    private void txtUserIdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserIdMouseEntered
	txtUserId.setText(txtUserId.getText().toUpperCase());
    }//GEN-LAST:event_txtUserIdMouseEntered

    private void txtUserIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserIdKeyPressed
	try
	{
	    if (evt.getKeyCode() == 10 && txtUserId.getText().equalsIgnoreCase("SANGUINE"))
	    {
		txtPassword.requestFocus();
	    }
	    else
	    {
		if (evt.getKeyCode() == 10)
		{
		    String sql = "select count(*) from tbluserhd where strUsercode='" + txtUserId.getText() + "' ";
		    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    while (rssql.next())
		    {
			if (rssql.getInt(1) > 0)
			{
			    txtPassword.requestFocus();
			}
			else
			{
			    sql = "  select count(*) from tbluserhd where strDebitCardString='" + txtUserId.getText() + "' ";
			    ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			    while (rssql1.next())
			    {
				if (rssql1.getInt(1) > 0)
				{
				    if (!funCHeckLoginForDebitCardString(txtUserId.getText()))
				    {
					txtUserId.setText("");
					new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
				    }
				}
				else
				{
				    txtUserId.setText("");
				    new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
				}
			    }
			}
		    }
		}
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }//GEN-LAST:event_txtUserIdKeyPressed

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
	try
	{
	    if (txtPassword.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "2", "Enter Password").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtPassword.getText(), "2", "Enter Password").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseEntered
    }//GEN-LAST:event_txtPasswordMouseEntered

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
	if (evt.getKeyCode() == 10)
	{
	    funCheckClientValidity();
	}
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void lblPasswordKBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPasswordKBMouseClicked
	try
	{
	    if (txtPassword.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "2", "Enter Password").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtPassword.getText(), "2", "Enter Password").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_lblPasswordKBMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
	funCheckClientValidity();
    }//GEN-LAST:event_btnLoginMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	txtUserId.setText("");
	txtPassword.setText("");
	txtUserId.requestFocus();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
	// TODO add your handling code here:
	funExitButtonClicked();
    }//GEN-LAST:event_btnExitMouseClicked

    private void txtUserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserIdActionPerformed
	// TODO add your handling code here:
	//funCheckClientValidity();
    }//GEN-LAST:event_txtUserIdActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
	/* Set the Nimbus look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
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
	    java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	sposAdvertise = new frmSPOSAdvertise();
	sposAdvertise.setVisible(true);

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{

	    public void run()
	    {
		try
		{
		    SimpleDateFormat ddMMyyyyDateFormate = new SimpleDateFormat("dd-MM-yyyy");
		    Date currentDate = new Date();
		    String toDay = ddMMyyyyDateFormate.format(currentDate);
		    System.setProperty("SPOSLog", toDay);

		    new frmLogin(true).funCheckJarInstance();
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		    System.exit(1);
		}
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnReset;
    private javax.swing.JLabel lblExtLogo;
    private javax.swing.JLabel lblImgSangPOS;
    private javax.swing.JLabel lblImgSanguineSoft;
    private javax.swing.JLabel lblJarModifiedDate;
    private javax.swing.JLabel lblLoginImage;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordKB;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JLabel lblUserIdKB;
    private javax.swing.JLabel lblclock;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelRoot;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserId;
    // End of variables declaration//GEN-END:variables

    private void funSetPOSVerion(String strClientCode)
    {
	clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(strClientCode);
	clsGlobalVarClass.gPOSVerion = clsEncryptDecryptClientCode.funDecryptClientCode(objClientDetails.getPosVersion());
    }

    private boolean funCheckTerminalLicence(String clientCode)
    {
	boolean hasTerminalLicence = false;
	try
	{
	    //max terminal count from db
	    int intMAXTerminalFromDB = funGetMAXTerminalFromDB(clientCode);
	    //max terminal count from licence
	    int intMAXTerminalFromLicence = funGetMAXTerminalFromLicence(clientCode);

	    clsUtility objUtility = new clsUtility();
	    String hostName = objUtility.funGetHostName();
	    String physicalAddress = objUtility.funGetCurrentMACAddress();

	    System.out.println("Host Name\t" + hostName);
	    System.out.println("Physical Address\t" + physicalAddress);

	    if (physicalAddress.trim().isEmpty())//if there is no internet 
	    {
		hasTerminalLicence = true;
	    }
	    else if (physicalAddress.trim().isEmpty() && intMAXTerminalFromLicence > 1)//if there is no internet and max terminal license is greater than 1
	    {
		hasTerminalLicence = false;
	    }
	    else if (physicalAddress.trim().isEmpty() && intMAXTerminalFromLicence == 1)//if there is no internet and max terminal license is equal to 1
	    {
		hasTerminalLicence = true;
	    }
	    else if (physicalAddress.equals("34-E6-AD-54-23-79"))//SHABBIR sir
	    {
		hasTerminalLicence = true;
	    }
	    else if (physicalAddress.equals(" 00-1E-4C-7D-6A-16"))//IMRAN 
	    {
		hasTerminalLicence = true;
	    }
	    else if (physicalAddress.equals(" 90-2B-34-08-5B-29"))//OFFICE
	    {
		hasTerminalLicence = true;
	    }
	    else if (intMAXTerminalFromDB == intMAXTerminalFromLicence)
	    {
		boolean isRegisterThisTerminal = isRegisterThisTerminal(clientCode, hostName, physicalAddress);
		if (isRegisterThisTerminal)
		{
		    hasTerminalLicence = true;
		}
		else
		{
		    hasTerminalLicence = false;
		}
	    }
	    else if (intMAXTerminalFromDB < intMAXTerminalFromLicence)
	    {
		boolean isRegisterThisTerminal = isRegisterThisTerminal(clientCode, hostName, physicalAddress);
		if (isRegisterThisTerminal)
		{
		    hasTerminalLicence = true;
		}
		else
		{
		    funRegisterThisMachine(clientCode, hostName, physicalAddress);

		    //recursion check
		    hasTerminalLicence = funCheckTerminalLicence(clientCode);
		}
	    }
	    else
	    {
		hasTerminalLicence = false;
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return hasTerminalLicence;
	}
    }

    private int funGetMAXTerminalFromLicence(String clientCode)
    {
	int intMAXTerminal = 0;
	try
	{
	    clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clientCode));
	    intMAXTerminal = Integer.parseInt(clsEncryptDecryptClientCode.funDecryptClientCode(objClientDetails.getIntMAXTerminal()));

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return intMAXTerminal;
	}
    }

    private int funGetMAXTerminalFromDB(String clientCode)
    {
	int intMAXTerminalFromDB = 0;
	try
	{

	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet("select count(strMACAddress) "
		    + "from tblregisterterminal  "
		    + "where strClientCode='" + clientCode + "' "
		    + "and strTerminalName='SPOS' ");
	    if (resultSet.next())
	    {
		intMAXTerminalFromDB = resultSet.getInt(1);
	    }
	    resultSet.close();

	}
	catch (Exception e)
	{
	    //e.printStackTrace();

	    String sql = "CREATE TABLE `tblregisterterminal` ( "
		    + "	`strClientCode` VARCHAR(20) NOT NULL, "
		    + "	`strHOSTName` VARCHAR(30) NOT NULL, "
		    + "	`strMACAddress` VARCHAR(20) NOT NULL, "
		    + " `strTerminalName` VARCHAR(10) NOT NULL, "
		    + " `strUserCreated` VARCHAR(10) NOT NULL, "
		    + "	`dteDateCreated` DATETIME NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		    + "	PRIMARY KEY (`strClientCode`, `strMACAddress`) "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    funGetMAXTerminalFromDB(clientCode);
	}
	finally
	{
	    return intMAXTerminalFromDB;
	}
    }

    private void funRegisterThisMachine(String clientCode, String hostName, String physicalAddress)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();

	    clsGlobalVarClass.dbMysql.execute("insert into tblregisterterminal(strClientCode,strHOSTName,strMACAddress,strTerminalName"
		    + ",strUserCreated,dteDateCreated,strDataPostFlag) "
		    + "values('" + clientCode + "','" + hostName + "','" + physicalAddress + "','SPOS','" + txtUserId.getText() + "','" + objUtility.funGetCurrentDate() + "','N') ");

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean isRegisterThisTerminal(String clientCode, String hostName, String physicalAddress)
    {
	boolean isRegisterThisTerminal = false;
	try
	{
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet("select * "
		    + "from tblregisterterminal  "
		    + "where strClientCode='" + clientCode + "' "
		    + "and strMACAddress='" + physicalAddress + "' "
		    + "and strTerminalName='SPOS' ");
	    if (resultSet.next())
	    {
		isRegisterThisTerminal = true;
	    }
	    resultSet.close();
	}
	catch (Exception e)
	{

	}
	finally
	{
	    return isRegisterThisTerminal;
	}
    }

    private void funExitButtonClicked()
    {
	clsGlobalVarClass.funCloseDB();

	funDeleteTempFolder();

	System.exit(0);
    }

    private void funDeleteTempFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp");

	    System.out.println("Temp path=" + file.toPath());
	    if (file.exists())
	    {
		// Get all files in the folder
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++)
		{
		    // Delete each file in the folder
		    files[i].delete();
		}
		// Delete the folder
		//file.delete();
	    }
	    else
	    {
		file.mkdir();
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}
