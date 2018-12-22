/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.POSGlobal.controller.clsUtility;
import com.POSMaster.controller.nfc.ReaderThread;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import java.awt.Dimension;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class frmRegisterInOutPlayZone extends javax.swing.JFrame
{

    private clsUtility objUtility = new clsUtility();
    Thread objThread = null;
    private String debitCardNo;
    private Date currentDate;
    private String registerCode;
    private boolean isSetExpiryDate;

    public frmRegisterInOutPlayZone(String formName)
    {

    }

    /**
     * This method is used to initialize frmRegisterDebitCard
     */
    public frmRegisterInOutPlayZone()
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
	    currentDate = new SimpleDateFormat("dd-MM-yyyy").parse(dte);

	    txtMemberCode.requestFocus();
	    funSetShortCutKeys();

	    ReaderThread objReader = new ReaderThread();
	    objThread = new Thread(objReader);
	    objThread.start();

	    lblCardStatus.setVisible(false);
	    panelRegisterIn.setVisible(false);
	    panelRegisterOut.setVisible(false);
	    panelDuration.setVisible(false);

	    dteDOJ.setDate(currentDate);
	    dteDOE.setDate(currentDate);
	    
	    
	    

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
     * This method is used to save register debit card dtl
     */
    private void funSaveButtonPressed()
    {
	try
	{

	    String memberCode = txtMemberCode.getText().trim();
	    String cardTypeName = txtCardName.getText().trim();

	    if (memberCode.isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Swipe the Card");
		txtMemberCode.requestFocus();
		return;
	    }

	    if (btnNew.getText().equalsIgnoreCase("Check In"))
	    {
		funSaveCheckedIn();
	    }
	    else
	    {
		funSaveCheckedOut();
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
	    String sql = "select count(*) from tbldebitcardmaster "
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
			txtMemberCode.setText(rsCustomerData.getString(1));
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
	txtMemberCode.setText("");
	txtCardName.setText("");
	txtNoOfMembers.setText("1");
	txtNoOfExtraGuests.setText("0");
	txtMemberName.setText("");
	txtPhone.setText("");
	txtMobileNo.setText("");
	txtEmailId.setText("");
	txtParents.setText("");
	txtRemarks.setText("");

	lblCardStatus.setVisible(false);
	panelRegisterIn.setVisible(false);
	panelRegisterOut.setVisible(false);
	panelDuration.setVisible(false);

	dteDOJ.setDate(currentDate);
	dteDOE.setDate(currentDate);

	clsGlobalVarClass.gDebitCardNo = null;
	debitCardNo = "";

	btnNew.setText("<html>Check <br>In/Out</html>");

	lblformName.setText("Register In/Out");
	lblformName.setPreferredSize(new Dimension(170, 30));

	registerCode = "";
	isSetExpiryDate = false;

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
        lblMemberCode = new javax.swing.JLabel();
        btnSwipeCard = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtMemberCode = new javax.swing.JPasswordField();
        txtCardName = new javax.swing.JPasswordField();
        lblMemberCode1 = new javax.swing.JLabel();
        txtNoOfMembers = new javax.swing.JPasswordField();
        lblMemberCode2 = new javax.swing.JLabel();
        txtNoOfExtraGuests = new javax.swing.JPasswordField();
        lblMemberCode3 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JPasswordField();
        lblMemberCode4 = new javax.swing.JLabel();
        txtMemberName = new javax.swing.JPasswordField();
        lblMemberCode5 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JPasswordField();
        lblMemberCode6 = new javax.swing.JLabel();
        txtMobileNo = new javax.swing.JPasswordField();
        txtEmailId = new javax.swing.JPasswordField();
        lblMemberCode7 = new javax.swing.JLabel();
        lblMemberCode8 = new javax.swing.JLabel();
        lblMemberCode9 = new javax.swing.JLabel();
        lblMemberCode10 = new javax.swing.JLabel();
        lblMemberCode11 = new javax.swing.JLabel();
        lblMemberCode12 = new javax.swing.JLabel();
        txtParents = new javax.swing.JFormattedTextField();
        cmbGender = new javax.swing.JComboBox<>();
        dteDOB = new com.toedter.calendar.JDateChooser();
        dteDOJ = new com.toedter.calendar.JDateChooser();
        dteDOE = new com.toedter.calendar.JDateChooser();
        panelRegisterIn = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblInTimeValue = new javax.swing.JLabel();
        panelRegisterOut = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblOutTimeValue = new javax.swing.JLabel();
        panelDuration = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblDurationValue = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblCardStatus = new javax.swing.JLabel();
        lblFormName = new javax.swing.JLabel();

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
            public void windowOpened(java.awt.event.WindowEvent evt)
            {
                windowOpen(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Register In/Out Member");
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

        lblMemberCode.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode.setText("Member Code :");

        btnSwipeCard.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
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

        btnNew.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("<html>Check <br>In/Out</html>");
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
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnNewKeyPressed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
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

        btnCancel.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
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

        txtMemberCode.setEditable(false);
        txtMemberCode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtMemberCode.setEchoChar('\u0000');
        txtMemberCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMemberCodeActionPerformed(evt);
            }
        });

        txtCardName.setEditable(false);
        txtCardName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtCardName.setEchoChar('\u0000');
        txtCardName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCardNameActionPerformed(evt);
            }
        });

        lblMemberCode1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode1.setText("Member         :");

        txtNoOfMembers.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtNoOfMembers.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNoOfMembers.setText("1");
        txtNoOfMembers.setEchoChar('\u0000');
        txtNoOfMembers.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNoOfMembersActionPerformed(evt);
            }
        });

        lblMemberCode2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode2.setText("Guest  :");

        txtNoOfExtraGuests.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtNoOfExtraGuests.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNoOfExtraGuests.setText("0");
        txtNoOfExtraGuests.setEchoChar('\u0000');
        txtNoOfExtraGuests.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNoOfExtraGuestsActionPerformed(evt);
            }
        });

        lblMemberCode3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode3.setText("Remark  :");

        txtRemarks.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtRemarks.setEchoChar('\u0000');
        txtRemarks.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRemarksActionPerformed(evt);
            }
        });

        lblMemberCode4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode4.setText("Name              : ");

        txtMemberName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtMemberName.setEchoChar('\u0000');
        txtMemberName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMemberNameActionPerformed(evt);
            }
        });

        lblMemberCode5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode5.setText("Phone             : ");

        txtPhone.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtPhone.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPhone.setEchoChar('\u0000');
        txtPhone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPhoneActionPerformed(evt);
            }
        });

        lblMemberCode6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode6.setText("Mobile             : ");

        txtMobileNo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtMobileNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMobileNo.setEchoChar('\u0000');
        txtMobileNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMobileNoActionPerformed(evt);
            }
        });

        txtEmailId.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtEmailId.setEchoChar('\u0000');
        txtEmailId.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtEmailIdActionPerformed(evt);
            }
        });

        lblMemberCode7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode7.setText("Email ID            : ");

        lblMemberCode8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode8.setText("Date Of Birth        : ");

        lblMemberCode9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode9.setText("Date Of Joining     : ");

        lblMemberCode10.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode10.setText("Membership Expiry : ");

        lblMemberCode11.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode11.setText("Sex                      : ");

        lblMemberCode12.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMemberCode12.setText("Parent                  : ");

        txtParents.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N

        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));

        dteDOB.setDateFormatString("dd/MM/yyyy");

        dteDOJ.setDateFormatString("dd/MM/yyyy");

        dteDOE.setDateFormatString("dd/MM/yyyy");

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("In Time   :");

        lblInTimeValue.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblInTimeValue.setForeground(new java.awt.Color(255, 51, 51));
        lblInTimeValue.setText("12:26 PM");

        javax.swing.GroupLayout panelRegisterInLayout = new javax.swing.GroupLayout(panelRegisterIn);
        panelRegisterIn.setLayout(panelRegisterInLayout);
        panelRegisterInLayout.setHorizontalGroup(
            panelRegisterInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegisterInLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );
        panelRegisterInLayout.setVerticalGroup(
            panelRegisterInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblInTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Out Time :");

        lblOutTimeValue.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblOutTimeValue.setForeground(new java.awt.Color(255, 51, 51));
        lblOutTimeValue.setText("02:30 PM");

        javax.swing.GroupLayout panelRegisterOutLayout = new javax.swing.GroupLayout(panelRegisterOut);
        panelRegisterOut.setLayout(panelRegisterOutLayout);
        panelRegisterOutLayout.setHorizontalGroup(
            panelRegisterOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegisterOutLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOutTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );
        panelRegisterOutLayout.setVerticalGroup(
            panelRegisterOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblOutTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 51, 51));
        jLabel3.setText("Duration  :");

        lblDurationValue.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblDurationValue.setForeground(new java.awt.Color(255, 51, 51));
        lblDurationValue.setText("00:04");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 51));
        jLabel7.setText("HOURS");

        javax.swing.GroupLayout panelDurationLayout = new javax.swing.GroupLayout(panelDuration);
        panelDuration.setLayout(panelDurationLayout);
        panelDurationLayout.setHorizontalGroup(
            panelDurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDurationLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDurationValue, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelDurationLayout.setVerticalGroup(
            panelDurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblDurationValue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lblCardStatus.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblCardStatus.setForeground(new java.awt.Color(255, 51, 51));
        lblCardStatus.setText("ACTIVE");

        lblFormName.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(51, 102, 255));
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFormName.setText("Register In/Out");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMemberCode4)
                                    .addComponent(lblMemberCode7)
                                    .addComponent(lblMemberCode6)
                                    .addComponent(lblMemberCode5)
                                    .addComponent(lblMemberCode8)
                                    .addComponent(lblMemberCode9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtMobileNo, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPhone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                                    .addComponent(txtEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dteDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dteDOJ, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dteDOE, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblMemberCode10)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMemberCode11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblMemberCode12)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(txtParents, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblMemberCode)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblCardStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelBodyLayout.createSequentialGroup()
                                                .addComponent(txtMemberCode, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblFormName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblMemberCode1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNoOfMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblMemberCode2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNoOfExtraGuests, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(52, 52, 52)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelRegisterIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelRegisterOut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMemberCode3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(panelRegisterIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelRegisterOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCardStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMemberCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblMemberCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNoOfMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMemberCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNoOfExtraGuests, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMemberCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMemberCode4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMemberCode5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMemberCode6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMemberCode7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(txtMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dteDOB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMemberCode8, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dteDOJ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMemberCode9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMemberCode10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteDOE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblMemberCode11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmbGender))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMemberCode12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtParents, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMemberCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSwipeCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSwipeCardMouseClicked
	// TODO add your handling code here:		
	funSwipeCardButtonClicked();
    }//GEN-LAST:event_btnSwipeCardMouseClicked

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
	clsGlobalVarClass.hmActiveForms.remove("RegisterInOutPlayZone");
    }//GEN-LAST:event_btnCancelMouseClicked

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

    private void txtMemberCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMemberCodeActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("RegisterInOutPlayZone");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("RegisterInOutPlayZone");
    }//GEN-LAST:event_formWindowClosing

    private void txtCardNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCardNameActionPerformed
    {//GEN-HEADEREND:event_txtCardNameActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtCardNameActionPerformed

    private void txtNoOfMembersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNoOfMembersActionPerformed
    {//GEN-HEADEREND:event_txtNoOfMembersActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfMembersActionPerformed

    private void txtNoOfExtraGuestsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNoOfExtraGuestsActionPerformed
    {//GEN-HEADEREND:event_txtNoOfExtraGuestsActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfExtraGuestsActionPerformed

    private void txtRemarksActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRemarksActionPerformed
    {//GEN-HEADEREND:event_txtRemarksActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRemarksActionPerformed

    private void txtMemberNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMemberNameActionPerformed
    {//GEN-HEADEREND:event_txtMemberNameActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMemberNameActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPhoneActionPerformed
    {//GEN-HEADEREND:event_txtPhoneActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtMobileNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMobileNoActionPerformed
    {//GEN-HEADEREND:event_txtMobileNoActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMobileNoActionPerformed

    private void txtEmailIdActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtEmailIdActionPerformed
    {//GEN-HEADEREND:event_txtEmailIdActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdActionPerformed

    private void windowOpen(java.awt.event.WindowEvent evt)//GEN-FIRST:event_windowOpen
    {//GEN-HEADEREND:event_windowOpen
        funSwipeCardButtonClicked();
    }//GEN-LAST:event_windowOpen


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSwipeCard;
    private javax.swing.JComboBox<String> cmbGender;
    private com.toedter.calendar.JDateChooser dteDOB;
    private com.toedter.calendar.JDateChooser dteDOE;
    private com.toedter.calendar.JDateChooser dteDOJ;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblCardStatus;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDurationValue;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblInTimeValue;
    private javax.swing.JLabel lblMemberCode;
    private javax.swing.JLabel lblMemberCode1;
    private javax.swing.JLabel lblMemberCode10;
    private javax.swing.JLabel lblMemberCode11;
    private javax.swing.JLabel lblMemberCode12;
    private javax.swing.JLabel lblMemberCode2;
    private javax.swing.JLabel lblMemberCode3;
    private javax.swing.JLabel lblMemberCode4;
    private javax.swing.JLabel lblMemberCode5;
    private javax.swing.JLabel lblMemberCode6;
    private javax.swing.JLabel lblMemberCode7;
    private javax.swing.JLabel lblMemberCode8;
    private javax.swing.JLabel lblMemberCode9;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOutTimeValue;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelDuration;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelRegisterIn;
    private javax.swing.JPanel panelRegisterOut;
    public static javax.swing.JPasswordField txtCardName;
    public static javax.swing.JPasswordField txtEmailId;
    public static javax.swing.JPasswordField txtMemberCode;
    public static javax.swing.JPasswordField txtMemberName;
    public static javax.swing.JPasswordField txtMobileNo;
    public static javax.swing.JPasswordField txtNoOfExtraGuests;
    public static javax.swing.JPasswordField txtNoOfMembers;
    private javax.swing.JFormattedTextField txtParents;
    public static javax.swing.JPasswordField txtPhone;
    public static javax.swing.JPasswordField txtRemarks;
    // End of variables declaration//GEN-END:variables

    private void funSetCardData(String cardString)
    {
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

	try
	{
	    String sql = "SELECT a.strCardTypeCode,b.strCardName,a.strCardString,a.strCardNo,a.strStatus "
		    + ", IFNULL(c.strIn,'N')strIn, IFNULL(c.strOut,'N')strOut,ifnull(c.intMembers,1)intMembers,ifnull(c.intExtraGuests,0)intExtraGuests "
		    + ",ifnull(c.strMemberName,'')strMemberName,ifnull(c.strPhoneNo,'')strPhoneNo,ifnull(c.strMobileNo,'')strMobileNo,ifnull(c.strEmailId,'')strEmailId "
		    + ",ifnull(c.dteDOB,'1990-01-01')dteDOB,date(b.dteDateCreated)dteDOJ,DATE_ADD(date(b.dteDateCreated),interval b.intValidityDays day)dteDOE "
		    + ",ifnull(c.strGender,'Male')gender,ifnull(c.strParents,'Male')parents,ifnull(c.strRemarks,'')remarks "
		    + ",ifnull(time(c.dteInDateTime),'')dteInDateTime,TIME(CURRENT_TIME())currentTime"
		    + ",TIMEDIFF(CURRENT_TIME(),time(c.dteInDateTime))duration,ifnull(c.strRegisterCode,'NR')strRegisterCode "
		    + ",b.strSetExpiryDt,b.dteExpiryDt "
		    + "FROM tbldebitcardmaster a "
		    + "INNER JOIN tbldebitcardtype b ON a.strCardTypeCode=b.strCardTypeCode "
		    + "LEFT OUTER JOIN tblregisterinoutplayzone c ON a.strCardNo=c.strCardNo and (c.strIn!='Y' or c.strOut!='Y') "
		    + "WHERE a.strCardString='" + cardString + "' ";
	    ResultSet rsCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCardNo.next())
	    {
		String cardTypeCode = rsCardNo.getString(1);
		String cardTypeName = rsCardNo.getString(2);
		//String cardString=rsCardNo.getString(3);
		String cardNo = rsCardNo.getString(4);
		String cardStatus = rsCardNo.getString(5);

		String inYN = rsCardNo.getString(6);
		String outYN = rsCardNo.getString(7);
		registerCode = rsCardNo.getString(23);

		String setExpiryDate = rsCardNo.getString(24);
		if (setExpiryDate.trim().equalsIgnoreCase("Y"))
		{
		    isSetExpiryDate = true;
		}
		else
		{
		    isSetExpiryDate = false;
		}

		String cardActiveStatus = "InActive";
		if (inYN.equalsIgnoreCase("Y") && outYN.equalsIgnoreCase("N"))
		{
		    cardActiveStatus = "Active";

		    lblCardStatus.setText("Active");
		    lblCardStatus.setVisible(true);
		    lblFormName.setText("Register Out");
		    btnNew.setText("Check Out");
		    panelRegisterIn.setVisible(true);
		    panelRegisterOut.setVisible(true);
		    panelDuration.setVisible(true);

		    long longCardNo = Long.parseLong(cardNo);
		    debitCardNo = cardNo;

		    txtMemberCode.setText(String.valueOf(longCardNo));
		    txtCardName.setText(cardTypeName);

		    String members = rsCardNo.getString(8);
		    String guests = rsCardNo.getString(9);
		    String memberName = rsCardNo.getString(10);

		    txtNoOfMembers.setText(members);
		    txtNoOfExtraGuests.setText(guests);
		    txtMemberName.setText(memberName);

		    String phoneNo = rsCardNo.getString(11);
		    String mobileNo = rsCardNo.getString(12);
		    String emailId = rsCardNo.getString(13);

		    txtPhone.setText(phoneNo);
		    txtMobileNo.setText(mobileNo);
		    txtEmailId.setText(emailId);

		    Date dateDOB = rsCardNo.getDate(14);
		    Date dateDOJ = rsCardNo.getDate(15);
		    Date dateDOE = rsCardNo.getDate(16);

		    dteDOB.setDate(dateDOB);
		    dteDOJ.setDate(dateDOJ);
		    dteDOE.setDate(dateDOE);

		    String gender = rsCardNo.getString(17);
		    String parents = rsCardNo.getString(18);
		    String remarks = rsCardNo.getString(19);

		    cmbGender.setSelectedItem(gender);
		    txtParents.setText(parents);
		    txtRemarks.setText(remarks);

		    Time inDateTime = rsCardNo.getTime(20);

		    String inTimeFormat = timeFormat.format(inDateTime);
		    lblInTimeValue.setText(inTimeFormat);

		    Time currentTime = rsCardNo.getTime(21);
		    String outTimeFormat = timeFormat.format(currentTime);
		    lblOutTimeValue.setText(outTimeFormat);

		    Time duration = rsCardNo.getTime(22);
		    //String durationTimeFormat = timeFormat.format(duration);

		    String strDuration = String.valueOf(duration);

		    String durationHRS = strDuration.split(":")[0];
		    String durationMNTS = strDuration.split(":")[1];

		    lblDurationValue.setText(durationHRS + ":" + durationMNTS);

		}
		else if (inYN.equalsIgnoreCase("N") && outYN.equalsIgnoreCase("Y"))
		{
		    cardActiveStatus = "Invalid";

		    lblCardStatus.setText("Invalid");
		    lblCardStatus.setVisible(true);
		    lblFormName.setText("Register In/Out");

		    return;
		}
		else
		{
		    cardActiveStatus = "InActive";

		    long longCardNo = Long.parseLong(cardNo);
		    debitCardNo = cardNo;

		    txtMemberCode.setText(String.valueOf(longCardNo));
		    txtCardName.setText(cardTypeName);

		    lblFormName.setText("Register In");
		    btnNew.setText("Check In");
		    panelRegisterIn.setVisible(true);
		    lblCardStatus.setVisible(false);

		    Time currentTime = rsCardNo.getTime(21);
		    String strCurrentTimeFormat = timeFormat.format(currentTime);
		    lblInTimeValue.setText(strCurrentTimeFormat);

		    Date dateDOB = rsCardNo.getDate(14);
		    Date dateDOJ = rsCardNo.getDate(15);
		    Date dateDOE = rsCardNo.getDate(16);

		    dteDOB.setDate(dateDOB);
		    dteDOJ.setDate(dateDOJ);
		    dteDOE.setDate(dateDOE);
		}
	    }
	    rsCardNo.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{

	}
    }

    private void funSaveCheckedIn()
    {
	try
	{

	    String memberName = txtMemberName.getText().trim();
	    if (memberName.isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please enter the member name.");
		txtMemberCode.requestFocus();
		return;
	    }

	    String mobileNo = txtMobileNo.getText().trim();

	    if (mobileNo.isEmpty())
	    {
		new frmOkPopUp(this, "Please enter a mobile number.", "Error", 0).setVisible(true);
		txtMobileNo.requestFocus();
		return;
	    }

	    if (mobileNo.trim().length() > 0)
	    {
		if (!mobileNo.matches("\\d{10}"))
		{
		    new frmOkPopUp(this, "Please Enter Valid Mobile Number.", "Error", 0).setVisible(true);
		    txtMobileNo.requestFocus();
		    return;
		}
	    }

	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    String memberCode = txtMemberCode.getText();
	    String debitCardTypeName = txtCardName.getText();

	    if (txtNoOfMembers.getText().trim().isEmpty())
	    {
		txtNoOfMembers.setText("1");
	    }
	    int noOfMembers = Integer.parseInt(txtNoOfMembers.getText());

	    if (txtNoOfExtraGuests.getText().trim().isEmpty())
	    {
		txtNoOfExtraGuests.setText("0");
	    }
	    int noOfGuests = Integer.parseInt(txtNoOfExtraGuests.getText());

	    String phone = txtPhone.getText().trim();

	    String emailId = txtEmailId.getText().trim();

	    Date dateDOB = dteDOB.getDate();
	    String strDOB = "1990-01-01";
	    if (dateDOB != null)
	    {
		strDOB = yyyyMMddDateFormat.format(dateDOB);
	    }

	    Date dateDOJ = dteDOJ.getDate();
	    String strDOJ = clsGlobalVarClass.gPOSOnlyDateForTransaction;
	    if (dateDOJ != null)
	    {
		strDOJ = yyyyMMddDateFormat.format(dateDOJ);
	    }

	    Date dateDOE = dteDOE.getDate();
	    String strDOE = clsGlobalVarClass.gPOSOnlyDateForTransaction;
	    if (dateDOE != null)
	    {
		strDOE = yyyyMMddDateFormat.format(dateDOE);
	    }

	    String gender = cmbGender.getSelectedItem().toString();
	    String parents = txtParents.getText().trim();
	    String remarks = txtRemarks.getText().trim();

	    String inYN = "Y";
	    String outYN = "N";

	    String inDateTime = clsGlobalVarClass.getPOSDateForTransaction();
	    String outDateTime = clsGlobalVarClass.getPOSDateForTransaction();

	    String registerCode = funGetRegisterCode();
	    String posCode = clsGlobalVarClass.gPOSCode;

	    String sqlInsert = "insert into tblregisterinoutplayzone"
		    + "(strRegisterCode,strPOSCode,strMemberCode,strCardNo,strMemberName"
		    + ",intMembers,intExtraGuests,strPhoneNo,strMobileNo,strEmailId"
		    + ",dteDOB,dteDOJ,dteMembershipExpiry,strGender,strParents"
		    + ",strRemarks,strIn,dteInDateTime,strOut,dteOutDateTime"
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
		    + ",strDataPostFlag) "
		    + "values "
		    + "('" + registerCode + "','" + posCode + "','" + memberCode + "','" + debitCardNo + "','" + memberName + "'"
		    + ",'" + noOfMembers + "','" + noOfGuests + "','" + phone + "','" + mobileNo + "','" + emailId + "'"
		    + ",'" + strDOB + "','" + strDOJ + "','" + strDOE + "','" + gender + "','" + parents + "'"
		    + ",'" + remarks + "','" + inYN + "','" + inDateTime + "','" + outYN + "','" + outDateTime + "'"
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "'"
		    + ",'N')";

	    int affectedRows = clsGlobalVarClass.dbMysql.execute(sqlInsert);

	    if (affectedRows > 0)
	    {
		new frmOkPopUp(null, "Checked in Successfully", "Successfull", 3).setVisible(true);

		funResetFields();
	    }
	    else
	    {
		new frmOkPopUp(null, "Unable to register member", "Error", 3).setVisible(true);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSaveCheckedOut()
    {
	try
	{
	    String outDateTime = clsGlobalVarClass.getPOSDateForTransaction();

	    String sqlUpdateCheckOutPlayZone = "update tblregisterinoutplayzone  "
		    + "set dteOutDateTime='" + outDateTime + "' "
		    + "where strIn='Y' "
		    + "and strOut='N' "
		    + "and strRegisterCode='" + registerCode + "' "
		    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	    int affectedRows = clsGlobalVarClass.dbMysql.execute(sqlUpdateCheckOutPlayZone);

	    if (isSetExpiryDate)//don't generate a bill for a membership card
	    {
		sqlUpdateCheckOutPlayZone = "update tblregisterinoutplayzone  "
			+ "set strOut='Y' "
			+ ",strBillNo='' "
			+ "where strIn='Y' "
			+ "and strOut='N' "
			+ "and strRegisterCode='" + registerCode + "' "
			+ "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";

		affectedRows = clsGlobalVarClass.dbMysql.execute(sqlUpdateCheckOutPlayZone);

		funCheckOutMessage("", registerCode);
	    }
	    else
	    {
		funGenerateTheBill();
	    }

	    funResetFields();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGetRegisterCode()
    {
	String registerCode = "R000000001";
	try
	{
	    String sqlMaxCode = "select  ifnull(max(right(a.strRegisterCode,9)),0)maxRegisterNo "
		    + "from tblregisterinoutplayzone a "
		    + "where a.strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	    ResultSet rsMaxCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlMaxCode);
	    if (rsMaxCode.next())
	    {
		String maxCode = rsMaxCode.getString(1);

		long lastNo = Long.parseLong(maxCode);

		registerCode = "R" + String.format("%09d", (lastNo + 1));

	    }
	    rsMaxCode.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return registerCode;
	}
    }

    private void funGenerateTheBill()
    {
	try
	{
	    String sqlRegisterMember = "SELECT a.strRegisterCode,a.strPOSCode,b.strCardTypeCode,b.strCardString,a.strCardNo "
		    + ",a.strIn,a.strOut,a.intMembers,a.intExtraGuests,a.strMemberName "
		    + ",a.strPhoneNo,a.strMobileNo,a.strEmailId "
		    + ",TIME(a.dteInDateTime)dteInDateTime,TIME(a.dteOutDateTime)dteOutDateTime "
		    + ",TIMEDIFF(TIME(a.dteOutDateTime), TIME(a.dteInDateTime))duration "
		    + "FROM  tblregisterinoutplayzone a,tbldebitcardmaster b "
		    + "WHERE a.strCardNo=b.strCardNo "
		    + "and a.strRegisterCode='" + registerCode + "' ";
	    ResultSet rsRegisterMember = clsGlobalVarClass.dbMysql.executeResultSet(sqlRegisterMember);
	    if (rsRegisterMember.next())
	    {
		String debitCardString = rsRegisterMember.getString(4);

		String debitCardNo = rsRegisterMember.getString(5);
		int noOfMembers = rsRegisterMember.getInt(8);
		int noOfGuests = rsRegisterMember.getInt(9);
		String inTime = rsRegisterMember.getString(14);
		String outTime = rsRegisterMember.getString(15);
		String duration = rsRegisterMember.getString(16);

		Time tmeInTime = rsRegisterMember.getTime(14);
		Time tmeOutTime = rsRegisterMember.getTime(15);
		Time tmeDurationTime = rsRegisterMember.getTime(16);

		List<clsDirectBillerItemDtl> listDirectBillerItemDtl = new ArrayList<>();
		clsGlobalVarClass.gTransactionType = "Direct Biller";
		frmDirectBiller objDirectBiller = new frmDirectBiller();

		String itemTypeFilter = " (c.strItemType='Play Charges' or c.strItemType='Extra Guest Charges' ) ";

		String sqlPricing = "select a.strItemCode,c.strItemName,c.strItemType "
			+ ",a.intGracePeriod,a.intTimeStamp "
			+ ",b.* "
			+ "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c "
			+ "where a.strPlayZonePricingCode=b.strPlayZonePricingCode "
			+ "and a.strItemCode=c.strItemCode "
			+ "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "and " + itemTypeFilter + " "
			+ "and '" + inTime + "' between b.dteFromTime and b.dteToTime ";
		ResultSet rsChargesItemPrincing = clsGlobalVarClass.dbMysql.executeResultSet(sqlPricing);
		int seqNo = 1;
		while (rsChargesItemPrincing.next())
		{
		    String itemCode = rsChargesItemPrincing.getString(1);
		    String itemName = rsChargesItemPrincing.getString(2);
		    String itemType = rsChargesItemPrincing.getString(3);
		    int gracePeried = rsChargesItemPrincing.getInt(4);
		    int timeSlot = rsChargesItemPrincing.getInt(5);
		    if (itemType.equalsIgnoreCase("Play Charges"))
		    {
			int qty = 1;

			String dayOfWeek = funGetDayForPricing("Member");
			double itemRate = rsChargesItemPrincing.getDouble(dayOfWeek);
			double amount = itemRate;

			int hrs = tmeDurationTime.getHours();
			int mints = tmeDurationTime.getMinutes();
			int durationMinutes = (hrs * 60) + mints;

			int extraTime = 0;
			if (durationMinutes > timeSlot)
			{
			    extraTime = durationMinutes - timeSlot;
			}
			else
			{
			    extraTime = 0;

			    qty = 1;
			}

			if (extraTime > gracePeried)
			{

			    int extraTimeCount = extraTime;
			    int extraHours = 0;

			    while (extraTimeCount > 0)
			    {
				extraHours++;

				extraTimeCount = extraTimeCount - timeSlot;
			    }

			    durationMinutes = timeSlot + (timeSlot * extraHours);

			    double extraChargeAmount = 0;
			    sqlPricing = "select a.strItemCode,c.strItemName,c.strItemType "
				    + ",a.intGracePeriod,a.intTimeStamp "
				    + ",b.* "
				    + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c "
				    + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode "
				    + "and a.strItemCode=c.strItemCode "
				    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
				    + "and c.strItemType='Extra Play Charges' "
				    + "and '" + inTime + "' between b.dteFromTime and b.dteToTime ";
			    ResultSet rsExtraChargesItemPrincing = clsGlobalVarClass.dbMysql.executeResultSet(sqlPricing);
			    while (rsExtraChargesItemPrincing.next())
			    {
				String extraItemCode = rsExtraChargesItemPrincing.getString(1);
				String extraItemName = rsExtraChargesItemPrincing.getString(2);
				String extraItemType = rsExtraChargesItemPrincing.getString(3);
				int extraGracePeried = rsExtraChargesItemPrincing.getInt(4);
				int extraTimeSlot = rsExtraChargesItemPrincing.getInt(5);

				int extraQty = 1;

				String extraDayOfWeek = funGetDayForPricing("Member");
				double extraItemRate = rsExtraChargesItemPrincing.getDouble(dayOfWeek);

				extraChargeAmount = extraItemRate;

				seqNo++;
			    }

			    amount = itemRate + (extraChargeAmount * extraHours);
			}
			else
			{
			    durationMinutes = timeSlot;

			    amount = (itemRate * durationMinutes) / timeSlot;
			}

			String itemNameHrs = "Up to " + (durationMinutes / 60);
			String itemNameMinutes = "." + (durationMinutes % 60) + " hours";

			String itemNameForPrinting = itemNameHrs + itemNameMinutes;

			clsDirectBillerItemDtl objDirectBillerItemDtl = new clsDirectBillerItemDtl(itemNameForPrinting, itemCode, qty, amount, false, "", "N", "", itemRate, "", String.valueOf(seqNo), itemRate);
			listDirectBillerItemDtl.add(objDirectBillerItemDtl);
		    }
		    else//'Extra Guest Charges' if (noOfGuests > 0)
		    {

			int guestQty = noOfGuests;

			String guestDayOfWeek = funGetDayForPricing("Guest");
			double guestItemRate = rsChargesItemPrincing.getDouble(guestDayOfWeek);
			double guestAmount = guestItemRate;

			guestAmount = (guestItemRate * guestQty);

			String guestItemNameForPrinting = "Extra Guest";

			clsDirectBillerItemDtl objDirectBillerItemDtlForGuest = new clsDirectBillerItemDtl(guestItemNameForPrinting, itemCode, guestQty, guestAmount, false, "", "N", "", guestItemRate, "", String.valueOf(seqNo), guestItemRate);
			listDirectBillerItemDtl.add(objDirectBillerItemDtlForGuest);
		    }

		    seqNo++;
		}
		rsChargesItemPrincing.close();

		objDirectBiller.setObj_List_ItemDtl(listDirectBillerItemDtl);
		frmBillSettlement objBillSettlement = new frmBillSettlement(objDirectBiller, "CheckOutPlayZone" + "!" + registerCode);
		objBillSettlement.setVisible(true);

	    }
	    rsRegisterMember.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public String funGetDayForPricing(String memberOrGuest)
    {
	String day = "dblMemberPriceMonday";
	if (memberOrGuest.equalsIgnoreCase("Member"))
	{
	    day = "dblMemberPriceMonday";
	}
	else
	{
	    day = "dblGuestPriceMonday";
	}

	String dayNames[] = new DateFormatSymbols().getWeekdays();
	Calendar date2 = Calendar.getInstance();
	date2.setTime(new Date(clsGlobalVarClass.gPOSDate));
	String tempday = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
	switch (tempday)
	{
	    case "Sunday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceSunday";
		}
		else
		{
		    day = "dblGuestPriceSunday";
		}
		break;

	    case "Monday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceMonday";
		}
		else
		{
		    day = "dblGuestPriceMonday";
		}
		break;

	    case "Tuesday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceTuesday";
		}
		else
		{
		    day = "dblGuestPriceTuesday";
		}
		break;

	    case "Wednesday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceWednesday";
		}
		else
		{
		    day = "dblGuestPriceWednesday";
		}
		break;

	    case "Thursday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceThursday";
		}
		else
		{
		    day = "dblGuestPriceThursday";
		}
		break;

	    case "Friday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceFriday";
		}
		else
		{
		    day = "dblGuestPriceFriday";
		}
		break;

	    case "Saturday":
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceSaturday";
		}
		else
		{
		    day = "dblGuestPriceSaturday";
		}
		break;

	    default:
		if (memberOrGuest.equalsIgnoreCase("Member"))
		{
		    day = "dblMemberPriceMonday";
		}
		else
		{
		    day = "dblGuestPriceMonday";
		}
	}
	return day;
    }

    public void funCheckOutMessage(String billNo, String registerCode)
    {
	try
	{
	    new frmOkPopUp(null, "Checked Out Successfully", "Successfull", 3).setVisible(true);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSwipeCardButtonClicked()
    {
	clsUtility obj = new clsUtility();

	funResetFields();

	if (clsGlobalVarClass.gEnableNFCInterface)
	{
	    //new frmSwipCardPopUp(this, "frmRegisterDebitCard",objReaderer).setVisible(true);
	}
	else
	{
	    new frmSwipCardPopUp(this, "frmRegisterInOutPlayZone").setVisible(true);

	    if (null != clsGlobalVarClass.gDebitCardNo)
	    {

		if (obj.funValidateDebitCardString(clsGlobalVarClass.gDebitCardNo))
		{

		    String cardString = clsGlobalVarClass.gDebitCardNo;

		    funSetCardData(cardString);

		}
		else
		{
		    JOptionPane.showMessageDialog(this, "Invalid Card No");
		}
	    }
	}
    }
}
