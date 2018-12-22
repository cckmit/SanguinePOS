/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.gPOSStartDate;
import static com.POSGlobal.controller.clsGlobalVarClass.gQueryForSearch;
import static com.POSGlobal.controller.clsGlobalVarClass.vArrSearchColumnSize;
import com.POSGlobal.controller.clsOperatorDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class frmUserMaster extends javax.swing.JFrame
{

    private String sql;
    private String dteCreated, dteEdited, moduleName, validDate;
    private java.util.Vector masterButtons, transButtons, utilityButtons, reportButtons;
    private boolean updateFlag, insertFlag;
    java.util.Vector vFormName, vFormImage, vForm;
    String userImagefilePath;
    FileInputStream fileInImg;
    File imgFile;
    BufferedImage imgBf;
    Map<String, List<clsOperatorDtl>> hmUserDtl;
    String selectedLikeUserCode = "NA";
    private File destFile = null;
    String strPath, part1, part2;
    private Map<String, String> mapWaiterNameCode;
    private Map<String, String> mapWaiterCodeName;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmUserMaster
     */
    public frmUserMaster()
    {

	initComponents();
	if (!clsGlobalVarClass.gTouchScreenMode)
	{
	    txtUserCode.setEditable(true);
	}
	try
	{
	    javax.swing.Timer timer = new javax.swing.Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    tickTock();
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();

	    masterButtons = new Vector();
	    transButtons = new Vector();
	    utilityButtons = new Vector();
	    reportButtons = new Vector();
	    hmUserDtl = new HashMap<String, List<clsOperatorDtl>>();
	    java.util.Date dt = new java.util.Date();
	    String dte = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dteValid.setDate(date);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    mapWaiterNameCode = new HashMap<String, String>();
	    mapWaiterCodeName = new HashMap<String, String>();
	    funFillWaiterNameComboBox();

	    funLoadForms();
	    funFillPOSSelectionGrid();
	    funSetShortCutKeys();
	    funCustomiseTransactionModuleTableColumnHeader();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private int funLoadForms() throws Exception
    {
	vFormImage = new Vector();
	vFormName = new Vector();
	vForm = new Vector();

	DefaultTableModel dm = (DefaultTableModel) tblMasterModule.getModel();
	DefaultTableModel dm1 = (DefaultTableModel) tblTransactionModule.getModel();
	DefaultTableModel dm2 = (DefaultTableModel) tblReportsModule.getModel();
	DefaultTableModel dm3 = (DefaultTableModel) tblUtilityModule.getModel();
	dm.setRowCount(0);
	dm2.setRowCount(0);
	dm3.setRowCount(0);
	dm1.setRowCount(0);

	if (clsGlobalVarClass.gHOPOSType.equals("HOPOS"))
	{
	    //sql="select * from tblforms where strModuleType!='T' order by intSequence";
	    sql = "select * from tblforms order by strModuleName";
	}
	else if (clsGlobalVarClass.gHOPOSType.equals("Client POS"))
	{
	    sql = "select * from tblforms where strModuleType!='M' order by strModuleName";
	}
	else if (clsGlobalVarClass.gHOPOSType.equals("DebitCard POS"))
	{
	    sql = "select * from tblforms "
		    + "where strModuleName='POS Master' or strModuleName='User Registration' "
		    + "or strModuleName='Property Setup' or strModuleName='Day End' "
		    + "or strModuleName='Customer Master' or strModuleName='StructureUpdate' "
		    + "or strModuleName='DebitCardMaster' or strModuleName='DebitCardRegister' "
		    + "or strModuleName='RechargeDebitCard' or strModuleName='DebitCardFlashReports'"
		    + " or strModuleName='DelistDebitCard' or strModuleName='CustomerTypeMaster' "
		    + "order by strModuleName";
	}
	else
	{
	    sql = "select * from tblforms order by strModuleName";
	}
	System.out.println(sql);
	//sql = "select * from tblforms";
	ResultSet rsForms = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	while (rsForms.next())
	{
	    vForm.add(rsForms.getString(1));
	    vFormName.add(rsForms.getString(2));
	    vFormImage.add(rsForms.getString(4));

	    if (rsForms.getString(2).equals("Customer Master") && rsForms.getString(3).equals("M"))
	    {
		Object[] ob1 =
		{
		    rsForms.getString(2), false, false, true
		};
		dm1.addRow(ob1);
		transButtons.add(rsForms.getString(4));
	    }

	    if ((!rsForms.getString(2).equals("Customer Master")) && rsForms.getString(3).equals("M"))
	    {
		Object[] ob =
		{
		    rsForms.getString(2), false, false, false, false
		};
		dm.addRow(ob);
		masterButtons.add(rsForms.getString(4));
	    }
	    if (rsForms.getString(3).equals("T") || rsForms.getString(3).equals("AT"))
	    {
		Object[] ob1 =
		{
		    rsForms.getString(2), false, false, true
		};
		dm1.addRow(ob1);
		transButtons.add(rsForms.getString(4));
	    }
	    if (rsForms.getString(3).equals("U"))
	    {
		Object[] ob1 =
		{
		    rsForms.getString(2), false
		};
		dm3.addRow(ob1);
		utilityButtons.add(rsForms.getString(4));
	    }
	    if (rsForms.getString(3).equals("R"))
	    {
		Object[] ob1 =
		{
		    rsForms.getString(2), false, false, false
		};
		dm2.addRow(ob1);
		reportButtons.add(rsForms.getString(4));
	    }
	}
	rsForms.close();
	tblMasterModule.setModel(dm);
	tblTransactionModule.setModel(dm1);
	tblUtilityModule.setModel(dm3);
	tblReportsModule.setModel(dm2);

	return 1;
    }

    private void funSetShortCutKeys()
    {
	btnCancel.setMnemonic('c');
	btnSubmit.setMnemonic('s');
	btnReset.setMnemonic('r');
    }

    /**
     * This method is used to set ticktock
     */
    private void tickTock()
    {
	Date date1 = new Date();
	String newstr = String.format("%tr", date1);
	String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
	lblDate.setText(dateAndTime);
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
	try
	{
	    lblImgShow.setText("");
	    lblImgShow.setIcon(null);
	    btnSubmit.setText("SAVE");
	    txtUserCode.setText("");
	    txtPassword.setText("");
	    txtConfirmPassword.setText("");
	    txtUserName.setText("");
	    txtUserImage.setText("");
	    lblImgShow.setIcon(null);
	    if (!clsGlobalVarClass.gTouchScreenMode)
	    {
		txtUserCode.setEditable(true);
	    }
	    chkSuperUser.setSelected(false);
	    java.util.Date dt = new java.util.Date();
	    int day = dt.getDate();
	    int month = dt.getMonth() + 1;
	    int year = dt.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    dteValid.setDate(date);
	    insertFlag = false;
	    updateFlag = false;
	    cmbWaiterName.setSelectedItem(" ");
	    txtNoOfDaysReportsView.setText("0");

	    /*
             * if (clsGlobalVarClass.gHOPOSType.equals("HOPOS")) { sql = "select
             * * from tblforms where strModuleType!='T' order by intSequence"; }
             * else if (clsGlobalVarClass.gHOPOSType.equals("Client POS")) { sql
             * = "select * from tblforms " + "where strModuleType!='M' order by
             * intSequence"; } else if
             * (clsGlobalVarClass.gHOPOSType.equals("DebitCard POS")) { sql =
             * "select * from tblforms " + "where strModuleName='POS Master' or
             * strModuleName='User Registration' " + "or strModuleName='Property
             * Setup' or strModuleName='Day End' " + "or strModuleName='Customer
             * Master' or strModuleName='StructureUpdate' " + "or
             * strModuleName='DebitCardMaster' or
             * strModuleName='DebitCardRegister' " + "or
             * strModuleName='RechargeDebitCard' or
             * strModuleName='DebitCardFlashReports'" + " or
             * strModuleName='DelistDebitCard' or
             * strModuleName='CustomerTypeMaster' " + "order by intSequence"; }
             * else { sql = "select * from tblforms order by intSequence"; }
             * System.out.println(sql); //sql = "select * from tblforms";
             * ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
             * DefaultTableModel dm = (DefaultTableModel)
             * tblMasterModule.getModel(); DefaultTableModel dm1 =
             * (DefaultTableModel) tblTransactionModule.getModel();
             * DefaultTableModel dm2 = (DefaultTableModel)
             * tblReportsModule.getModel(); DefaultTableModel dm3 =
             * (DefaultTableModel) tblUtilityModule.getModel();
             * dm.setRowCount(0); dm2.setRowCount(0); dm3.setRowCount(0);
             * dm1.setRowCount(0); while (rs.next()) { if
             * (rs.getString(3).equals("M")) { //dm.removeRow(0); Object[] ob =
             * {rs.getString(2), false}; dm.addRow(ob);
             * masterButtons.add(rs.getString(4)); } if
             * (rs.getString(3).equals("T")) { //dm1.removeRow(0); Object[] ob1
             * = {rs.getString(2), false}; dm1.addRow(ob1);
             * transButtons.add(rs.getString(4)); } if
             * (rs.getString(3).equals("U")) { //dm3.removeRow(0); Object[] ob1
             * = {rs.getString(2), false}; dm3.addRow(ob1);
             * utilityButtons.add(rs.getString(4)); } if
             * (rs.getString(3).equals("R")) { //dm2.removeRow(0); Object[] ob1
             * = {rs.getString(2), false}; dm2.addRow(ob1);
             * reportButtons.add(rs.getString(4)); } }
	     */
	    funLoadForms();
	    funFillPOSSelectionGrid();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to set user id
     *
     * @param uId
     */
    private void funSetUserDetails(String uId)
    {
	try
	{
	    funResetField();
	    boolean superUser = false;
	    txtUserCode.setText(uId);
	    sql = "select * from tbluserhd where strUserCode='" + uId + "'";
	    ResultSet rsUserDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsUserDtl.next())
	    {
		btnSubmit.setText("UPDATE");
		txtUserName.setText(rsUserDtl.getString(2));
		txtPassword.setText(rsUserDtl.getString(3));
		txtConfirmPassword.setText(rsUserDtl.getString(3));
		if (rsUserDtl.getString(4).equals("Super"))
		{
		    chkSuperUser.setSelected(true);
		    superUser = true;
		}
		if (superUser)
		{
		    //System.out.println(tblMasterModule.getRowCount());
		    for (int k = 0; k < tblMasterModule.getRowCount(); k++)
		    {
			tblMasterModule.setValueAt(true, k, 1);
			tblMasterModule.setValueAt(true, k, 2);
			tblMasterModule.setValueAt(true, k, 3);
			tblMasterModule.setValueAt(true, k, 4);
		    }
		    for (int k = 0; k < tblTransactionModule.getRowCount(); k++)
		    {
			tblTransactionModule.setValueAt(true, k, 1);
			tblTransactionModule.setValueAt(true, k, 2);
			tblTransactionModule.setValueAt(true, k, 3);
			tblTransactionModule.setValueAt(true, k, 4);
			tblTransactionModule.setValueAt(true, k, 5);
		    }
		    for (int k = 0; k < tblUtilityModule.getRowCount(); k++)
		    {
			tblUtilityModule.setValueAt(true, k, 1);
		    }
		    for (int k = 0; k < tblReportsModule.getRowCount(); k++)
		    {
			tblReportsModule.setValueAt(true, k, 1);
			tblReportsModule.setValueAt(true, k, 2);
			tblReportsModule.setValueAt(true, k, 3);
		    }
		}
		else
		{
		    DefaultTableModel dm = (DefaultTableModel) tblMasterModule.getModel();
		    DefaultTableModel dm1 = (DefaultTableModel) tblTransactionModule.getModel();
		    DefaultTableModel dm3 = (DefaultTableModel) tblReportsModule.getModel();
		    DefaultTableModel dm2 = (DefaultTableModel) tblUtilityModule.getModel();
		    String sqlUserDtl = "SELECT * FROM  tbluserdtl WHERE strUserCode='" + uId + "'";
		    String moduleType = "";
		    ResultSet rsUserDtlInfo = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
		    while (rsUserDtlInfo.next())
		    {
			sqlUserDtl = "select strModuleType from tblforms where strModuleName='" + rsUserDtlInfo.getString(2) + "'";
			ResultSet rsModuleType = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
			rsModuleType.next();
			moduleType = rsModuleType.getString(1);
			rsModuleType.close();

			if (moduleType.equals("M"))
			{
			    dm.removeRow(0);
			    Object[] ob =
			    {
				rsUserDtlInfo.getString(2), Boolean.parseBoolean(rsUserDtlInfo.getString(4)), Boolean.parseBoolean(rsUserDtlInfo.getString(5)), Boolean.parseBoolean(rsUserDtlInfo.getString(7)), Boolean.parseBoolean(rsUserDtlInfo.getString(8))
			    };
			    dm.addRow(ob);
			}
			if (moduleType.equals("T") || moduleType.equals("AT"))
			{
			    dm1.removeRow(0);
			    Object[] ob =
			    {
				rsUserDtlInfo.getString(2), Boolean.parseBoolean(rsUserDtlInfo.getString(4)), Boolean.parseBoolean(rsUserDtlInfo.getString(5)), Boolean.parseBoolean(rsUserDtlInfo.getString(6)), Boolean.parseBoolean(rsUserDtlInfo.getString(7)), Boolean.parseBoolean(rsUserDtlInfo.getString(8))
			    };
			    dm1.addRow(ob);
			}
			if (moduleType.equals("U"))
			{
			    dm2.removeRow(0);
			    Object[] ob =
			    {
				rsUserDtlInfo.getString(2), Boolean.parseBoolean(rsUserDtlInfo.getString(10))
			    };
			    dm2.addRow(ob);
			}
			if (moduleType.equals("R"))
			{
			    dm3.removeRow(0);
			    Object[] ob =
			    {
				rsUserDtlInfo.getString(2), Boolean.parseBoolean(rsUserDtlInfo.getString(7)), Boolean.parseBoolean(rsUserDtlInfo.getString(8)), Boolean.parseBoolean(rsUserDtlInfo.getString(9))
			    };
			    dm3.addRow(ob);
			}
		    }
		    rsUserDtlInfo.close();
		}
		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(rsUserDtl.getString(5));
		dteValid.setDate(date);
	    }
	    rsUserDtl.close();

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
        panelBody = new javax.swing.JPanel();
        txtUserCode = new javax.swing.JTextField();
        lblUserName = new javax.swing.JLabel();
        btnHelp = new javax.swing.JButton();
        lbluserCode = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        lblFormName = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        lblValidTill = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        PanelMasters = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMasterModule = new javax.swing.JTable();
        PanelTransactions = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTransactionModule = new javax.swing.JTable();
        panelUtilities = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblUtilityModule = new javax.swing.JTable();
        panelReports = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblReportsModule = new javax.swing.JTable();
        txtConfirmPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        dteValid = new com.toedter.calendar.JDateChooser();
        btnCancel = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        chkSuperUser = new javax.swing.JCheckBox();
        scrollPane = new javax.swing.JScrollPane();
        tblPOS = new javax.swing.JTable();
        lblUserImage = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        txtUserImage = new javax.swing.JTextField();
        lblImgShow = new javax.swing.JLabel();
        btnLikeUser = new javax.swing.JButton();
        lblWaiterName = new javax.swing.JLabel();
        cmbWaiterName = new javax.swing.JComboBox();
        txtNoOfDaysReportsView = new javax.swing.JTextField();
        lblNoOfDaysReportsView = new javax.swing.JLabel();

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

        lblProductName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- User Registration");
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

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        txtUserCode.setEditable(false);
        txtUserCode.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtUserCodeMouseClicked(evt);
            }
        });
        txtUserCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUserCodeActionPerformed(evt);
            }
        });
        txtUserCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUserCodeKeyPressed(evt);
            }
        });

        lblUserName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUserName.setText("Name :");

        btnHelp.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        btnHelp.setText("...");
        btnHelp.setToolTipText("Select User");
        btnHelp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHelpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnHelpMouseEntered(evt);
            }
        });
        btnHelp.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHelpKeyPressed(evt);
            }
        });

        lbluserCode.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lbluserCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbluserCode.setText("User Code :");

        lblConfirmPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblConfirmPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblConfirmPassword.setText("Confirm Password :");

        lblFormName.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("User Registration");

        txtUserName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtUserName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtUserNameMouseClicked(evt);
            }
        });
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUserNameKeyPressed(evt);
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
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        lblValidTill.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblValidTill.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValidTill.setText("Valid Till :");

        btnSubmit.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSubmit.setText("SAVE");
        btnSubmit.setToolTipText("Save User");
        btnSubmit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubmit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSubmitMouseClicked(evt);
            }
        });
        btnSubmit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSubmitActionPerformed(evt);
            }
        });
        btnSubmit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSubmitKeyPressed(evt);
            }
        });

        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        PanelMasters.setLayout(null);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tblMasterModule.setAutoCreateRowSorter(true);
        tblMasterModule.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblMasterModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Module Name", "Grant"
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
        tblMasterModule.setRowHeight(35);
        tblMasterModule.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblMasterModule);
        if (tblMasterModule.getColumnModel().getColumnCount() > 0)
        {
            tblMasterModule.getColumnModel().getColumn(1).setMinWidth(50);
            tblMasterModule.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblMasterModule.getColumnModel().getColumn(1).setMaxWidth(50);
        }

        PanelMasters.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 400, 440);

        tabPane.addTab("Masters", PanelMasters);

        jScrollPane3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tblTransactionModule.setAutoCreateRowSorter(true);
        tblTransactionModule.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblTransactionModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Module Name", "Grant", "<html>Transaction Level Authentication</html>", "<html>Enable<br>Auditing</html>"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, true, true
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
        tblTransactionModule.setRowHeight(35);
        tblTransactionModule.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblTransactionModule);
        if (tblTransactionModule.getColumnModel().getColumnCount() > 0)
        {
            tblTransactionModule.getColumnModel().getColumn(1).setMinWidth(50);
            tblTransactionModule.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblTransactionModule.getColumnModel().getColumn(1).setMaxWidth(50);
            tblTransactionModule.getColumnModel().getColumn(2).setMinWidth(100);
            tblTransactionModule.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblTransactionModule.getColumnModel().getColumn(2).setMaxWidth(100);
            tblTransactionModule.getColumnModel().getColumn(3).setMinWidth(50);
            tblTransactionModule.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblTransactionModule.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        javax.swing.GroupLayout PanelTransactionsLayout = new javax.swing.GroupLayout(PanelTransactions);
        PanelTransactions.setLayout(PanelTransactionsLayout);
        PanelTransactionsLayout.setHorizontalGroup(
            PanelTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTransactionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelTransactionsLayout.setVerticalGroup(
            PanelTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );

        tabPane.addTab("Transactions", PanelTransactions);

        jScrollPane5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tblUtilityModule.setAutoCreateRowSorter(true);
        tblUtilityModule.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblUtilityModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Module Name", "Grant"
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
        tblUtilityModule.setRowHeight(35);
        tblUtilityModule.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblUtilityModule);
        if (tblUtilityModule.getColumnModel().getColumnCount() > 0)
        {
            tblUtilityModule.getColumnModel().getColumn(1).setMinWidth(50);
            tblUtilityModule.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblUtilityModule.getColumnModel().getColumn(1).setMaxWidth(50);
        }

        javax.swing.GroupLayout panelUtilitiesLayout = new javax.swing.GroupLayout(panelUtilities);
        panelUtilities.setLayout(panelUtilitiesLayout);
        panelUtilitiesLayout.setHorizontalGroup(
            panelUtilitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        panelUtilitiesLayout.setVerticalGroup(
            panelUtilitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );

        tabPane.addTab("Utilities", panelUtilities);

        jScrollPane4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tblReportsModule.setAutoCreateRowSorter(true);
        tblReportsModule.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        tblReportsModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Module Name", "Grant"
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
        tblReportsModule.setRowHeight(35);
        tblReportsModule.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblReportsModule);
        if (tblReportsModule.getColumnModel().getColumnCount() > 0)
        {
            tblReportsModule.getColumnModel().getColumn(1).setMinWidth(50);
            tblReportsModule.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblReportsModule.getColumnModel().getColumn(1).setMaxWidth(50);
        }

        javax.swing.GroupLayout panelReportsLayout = new javax.swing.GroupLayout(panelReports);
        panelReports.setLayout(panelReportsLayout);
        panelReportsLayout.setHorizontalGroup(
            panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        panelReportsLayout.setVerticalGroup(
            panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );

        tabPane.addTab("Reports", panelReports);

        txtConfirmPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtConfirmPassword.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtConfirmPasswordMouseClicked(evt);
            }
        });
        txtConfirmPassword.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtConfirmPasswordKeyPressed(evt);
            }
        });

        lblPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPassword.setText("Password :");

        dteValid.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        dteValid.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteValidKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close User Master");
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

        txtPassword.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtPassword.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPasswordKeyPressed(evt);
            }
        });

        chkSuperUser.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        chkSuperUser.setText("Super User");
        chkSuperUser.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkSuperUserActionPerformed(evt);
            }
        });

        tblPOS.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        tblPOS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS Code", "POS Name", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, true
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
        tblPOS.setRowHeight(25);
        tblPOS.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblPOS.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblPOS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPOSMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblPOS);
        if (tblPOS.getColumnModel().getColumnCount() > 0)
        {
            tblPOS.getColumnModel().getColumn(0).setResizable(false);
            tblPOS.getColumnModel().getColumn(0).setPreferredWidth(0);
        }

        lblUserImage.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblUserImage.setText("User Image :");

        btnBrowse.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnBrowse.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnBrowse.setText("BROWSE");
        btnBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowseMouseClicked(evt);
            }
        });
        btnBrowse.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnBrowseKeyPressed(evt);
            }
        });

        txtUserImage.setEditable(false);
        txtUserImage.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        txtUserImage.setEnabled(false);
        txtUserImage.setMaximumSize(new java.awt.Dimension(6, 20));
        txtUserImage.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUserImageKeyPressed(evt);
            }
        });

        btnLikeUser.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        btnLikeUser.setForeground(new java.awt.Color(255, 255, 255));
        btnLikeUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnLikeUser.setText("LIKE USER");
        btnLikeUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLikeUser.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnLikeUserMouseClicked(evt);
            }
        });
        btnLikeUser.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnLikeUserKeyPressed(evt);
            }
        });

        lblWaiterName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblWaiterName.setText("Waiter :");
        lblWaiterName.setMaximumSize(new java.awt.Dimension(70, 15));
        lblWaiterName.setMinimumSize(new java.awt.Dimension(70, 15));
        lblWaiterName.setPreferredSize(new java.awt.Dimension(70, 15));

        cmbWaiterName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N

        txtNoOfDaysReportsView.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtNoOfDaysReportsView.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNoOfDaysReportsView.setText("0");

        lblNoOfDaysReportsView.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblNoOfDaysReportsView.setText("No. Of Days Report View");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblImgShow, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(275, 275, 275))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbluserCode, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                    .addGap(4, 4, 4)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(chkSuperUser))
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(txtUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnLikeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(lblConfirmPassword)
                                    .addGap(3, 3, 3)
                                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                        .addComponent(lblValidTill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(4, 4, 4)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dteValid, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblWaiterName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbWaiterName, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(87, 87, 87))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblUserImage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUserImage, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblNoOfDaysReportsView, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoOfDaysReportsView, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(0, 133, Short.MAX_VALUE)
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(tabPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbluserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLikeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chkSuperUser))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(4, 4, 4)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblValidTill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteValid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblWaiterName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbWaiterName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUserImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(txtUserImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImgShow, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblNoOfDaysReportsView, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNoOfDaysReportsView, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
                        .addGap(27, 27, 27))))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserCodeMouseClicked
	// TODO add your handling code here:
	try
	{
	    if ("Save".equalsIgnoreCase(btnSubmit.getText()))
	    {//As user will not change its User Code on update button
		if (txtUserCode.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Code").setVisible(true);
		    txtUserCode.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(this, true, txtUserCode.getText(), "1", "Enter User Code").setVisible(true);
		    txtUserCode.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtUserCodeMouseClicked

    private void funUserDetails()
    {
	try
	{
	    //new frmSearchForm(this,"FrmUserReg").setVisible(true);
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("UserMaster");
	    new frmSearchFormDialog(this, true).setVisible(true);

	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		btnSubmit.setText("UPDATE");
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
		txtUserCode.setEditable(false);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funLikeUserDetails()
    {
	try
	{

	    //new frmSearchForm(this,"FrmUserReg").setVisible(true);
	    String userType = "op";
	    if (chkSuperUser.isSelected())
	    {
		userType = "Super";
	    }
	    funCallForLikeSearchForm("LikeUserMaster", txtUserName.getText().toString(), userType);
	    new frmSearchFormDialog(this, true).setVisible(true);
	    List<clsOperatorDtl> arrUserList = new ArrayList<clsOperatorDtl>();
	    clsOperatorDtl objUser = null;
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		clsGlobalVarClass.gSearchItemClicked = false;
		selectedLikeUserCode = data[0].toString();
		funSetDataForLikeUser(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private Vector funCallForLikeSearchForm(String searchFormName, String name, String type)
    {
	try
	{
	    clsGlobalVarClass.gSearchMasterFormName = "";
	    clsGlobalVarClass.gSearchFormName = searchFormName;
	    vArrSearchColumnSize = new Vector();

	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date temDate = dFormat.parse(gPOSStartDate);
	    String todate = (temDate.getYear() + 1900) + "-" + (temDate.getMonth() + 1) + "-" + temDate.getDate();

	    switch (searchFormName)
	    {
		case "LikeUserMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = "select strUserCode as User_Code,strUserName as User_Name,strSuperType as User_Type,dteValidDate as Valid_Date,strPOSAccess as POS"
			    + " from tbluserhd a where a.strUserName like '%" + name + "%' or a.strSuperType like '%" + type + "%'";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return vArrSearchColumnSize;
    }

    private void btnHelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelpMouseClicked
	// TODO add your handling code here:
	funUserDetails();
    }//GEN-LAST:event_btnHelpMouseClicked

    private void txtUserNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtUserName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name").setVisible(true);
		txtUserName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtUserName.getText(), "1", "Enter User Name").setVisible(true);
		txtUserName.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtUserNameMouseClicked

    private void txtUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    dteValid.requestFocus();
	}
    }//GEN-LAST:event_txtUserNameKeyPressed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void funUserOperations()
    {

	if (chkSuperUser.isSelected())
	{
	    if (!clsGlobalVarClass.gSanguneUser)
	    {
		new frmOkPopUp(null, "You don't have right to create Supre User.", "Warning", 0).setVisible(true);
		txtUserCode.requestFocus();
		return;
	    }
	}

	//funPOSSeclection();
	String selectedPOSCodes = "";
	for (int cnt = 0; cnt < tblPOS.getRowCount(); cnt++)
	{
	    boolean flgPOSCheck = (boolean) tblPOS.getValueAt(cnt, 2);
	    if (flgPOSCheck)
	    {
		selectedPOSCodes += tblPOS.getValueAt(cnt, 0).toString() + ",";
	    }
	}
	if (selectedPOSCodes.isEmpty())
	{
	    JOptionPane.showMessageDialog(null, "Select Atleast one POS!!!");
	    return;
	}
	selectedPOSCodes = selectedPOSCodes.substring(0, selectedPOSCodes.length() - 1);

	try
	{
	    int sequence = 0;
	    java.util.Date curDt = new java.util.Date();
	    dteCreated = ((curDt.getYear() + 1900) + "-" + (curDt.getMonth() + 1) + "-" + curDt.getDate())
		    + " " + (curDt.getHours() + ":" + curDt.getMinutes() + ":" + curDt.getSeconds());
	    dteEdited = ((curDt.getYear() + 1900) + "-" + (curDt.getMonth() + 1) + "-" + curDt.getDate())
		    + " " + (curDt.getHours() + ":" + curDt.getMinutes() + ":" + curDt.getSeconds());
	    Date currentDate = new Date();

	    int cd = currentDate.getDate();
	    int cm = currentDate.getMonth() + 1;
	    int cy = currentDate.getYear() + 1900;
	    int cDateSum = cd + cm + cy;
	    Date dteValiTillDate = dteValid.getDate();

	    int d = dteValiTillDate.getDate();
	    int m = dteValiTillDate.getMonth() + 1;
	    int y = dteValiTillDate.getYear() + 1900;
	    int validDateSum = d + m + y;
	    //System.out.println("current date="+cDateSum+"\tvalid date="+validDateSum);
	    validDate = y + "-" + m + "-" + d;
	    clsUtility obj = new clsUtility();
	    if (txtUserCode.getText().trim().length() == 0)
	    {
		new frmOkPopUp(null, "Please Enter User Code", "Error", 0).setVisible(true);
		txtUserCode.requestFocus();
		return;
	    }

	    if (txtUserCode.getText().trim().equalsIgnoreCase("sanguine"))
	    {
		new frmOkPopUp(null, "User Code Not Allowed", "Error", 0).setVisible(true);
		txtUserCode.setText("");
		txtUserCode.requestFocus();
		return;
	    }

	    if (txtUserCode.getText().length() > 10)
	    {
		new frmOkPopUp(null, "User Code length must be less than 10", "Error", 0).setVisible(true);
		return;
	    }

	    if (txtUserName.getText().length() == 1)
	    {
		new frmOkPopUp(null, "Full Name should not blank", "Error", 0).setVisible(true);
		return;
	    }

	    if (!obj.funCheckLength(txtUserName.getText(), 25))
	    {
		new frmOkPopUp(this, "User Name length must be less than 25", "Error", 0).setVisible(true);
		txtUserName.requestFocus();
		return;
	    }
	    if (dteValiTillDate.compareTo(currentDate) < 0)
	    {
		new frmOkPopUp(null, "Invalid valid till date.", "Error", 0).setVisible(true);
		return;
	    }

	    if (txtPassword.getText().length() == 0)
	    {
		new frmOkPopUp(null, "Password field is blank", "Error", 0).setVisible(true);
		return;
	    }
	    if (!obj.funCheckLength(txtPassword.getText(), 10))
	    {
		new frmOkPopUp(this, "Password length must be less than 10", "Error", 0).setVisible(true);
		txtPassword.requestFocus();
		return;
	    }
	    if (txtConfirmPassword.getText().length() == 0)
	    {
		new frmOkPopUp(null, "Confirm Password field is blank", "Error", 0).setVisible(true);
		return;
	    }
	    if (!obj.funCheckLength(txtConfirmPassword.getText(), 10))
	    {
		new frmOkPopUp(this, "Password length must be less than 10", "Error", 0).setVisible(true);
		txtPassword.requestFocus();
		return;
	    }
	    if (!txtConfirmPassword.getText().equals(txtPassword.getText()))
	    {
		new frmOkPopUp(null, "Password and Confirm Password must match", "Error", 0).setVisible(true);
		return;
	    }
	    if (dteValid.getDate() == null)
	    {
		new frmOkPopUp(null, "Date Valid Field is blank", "Error", 0).setVisible(true);
		return;
	    }

	    String encKey = "04081977";
	    String userCode = txtUserCode.getText().trim().toUpperCase();
	    String password = txtPassword.getText().trim().toUpperCase();
	    password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, password);

	    String noOfDaysReportsView = txtNoOfDaysReportsView.getText();
	    if (!noOfDaysReportsView.matches("^[0-9]{1,18}?$"))
	    {
		new frmOkPopUp(null, "Please Enter Valid No. Of Days For Report View.", "Error", 1).setVisible(true);
		return;
	    }

	    if (btnSubmit.getText().equalsIgnoreCase("SAVE"))
	    {
		//////////////// MY SAVE CODE IN PREPARED STATEMENT //////////////

		String userType = "op";
		if (chkSuperUser.isSelected())
		{
		    userType = "Super";
		}
		String query = "insert into tbluserhd values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
		PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
		pre.setString(1, userCode);
		pre.setString(2, txtUserName.getText().trim());
		pre.setString(3, password);
		pre.setString(4, userType);
		pre.setString(5, validDate);
		pre.setString(6, selectedPOSCodes);
		pre.setString(7, clsGlobalVarClass.gUserCode);
		pre.setString(8, clsGlobalVarClass.gUserCode);
		pre.setString(9, dteCreated);
		pre.setString(10, dteEdited);
		pre.setString(11, clsGlobalVarClass.gClientCode);
		pre.setString(12, "N");
		if (txtUserImage.getText().toString().trim().isEmpty())
		{
		    pre.setString(13, "");
		    pre.setString(14, "");
		}
		else
		{
		    pre.setBinaryStream(13, (InputStream) fileInImg, (int) imgFile.length());
		    pre.setString(14, userImagefilePath);
		}
		pre.setString(15, "");
		if (cmbWaiterName.getSelectedItem().toString().trim().length() > 0)
		{
		    pre.setString(16, mapWaiterNameCode.get(cmbWaiterName.getSelectedItem().toString()));
		}
		else
		{
		    pre.setString(16, " ");
		}
		pre.setString(17, "");
		pre.setString(18, noOfDaysReportsView);

		int cnt = pre.executeUpdate();
		pre.close();

		if (selectedLikeUserCode.equals("NA"))
		{
		    int rowCount = tblMasterModule.getRowCount();
		    if (selectedLikeUserCode.equals("NA"))
		    {
			for (int k = 0; k < rowCount; k++)
			{
			    sequence++;
			    if (userType.equals("Super"))
			    {
				moduleName = tblMasterModule.getValueAt(k, 0).toString();
				sql = "insert into tblsuperuserdtl values('" + userCode
					+ "','" + moduleName + "','" + masterButtons.elementAt(k)
					+ "'," + sequence + ",'true','true','true','true','true','true','true','true','true')";
				clsGlobalVarClass.dbMysql.execute(sql);
			    }
			    else
			    {
				moduleName = tblMasterModule.getValueAt(k, 0).toString();
				String grant = tblMasterModule.getValueAt(k, 1).toString();
				if (grant.equalsIgnoreCase("true"))
				{
				    sql = "insert into tbluserdtl values('" + userCode
					    + "','" + moduleName + "','" + masterButtons.elementAt(k)
					    + "'," + sequence + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "'"
					    + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "')";
				    clsGlobalVarClass.dbMysql.execute(sql);
				}
			    }
			}
		    }

		    rowCount = tblTransactionModule.getRowCount();
		    for (int k = 0; k < rowCount; k++)
		    {
			sequence++;
			if (userType.equals("Super"))
			{
			    moduleName = tblTransactionModule.getValueAt(k, 0).toString();
			    String isTLA = "false";
			    String isAudit = tblTransactionModule.getValueAt(k, 3).toString();
			    sql = "insert into tblsuperuserdtl values('" + userCode
				    + "','" + moduleName + "','" + transButtons.elementAt(k)
				    + "'," + sequence + ",'true','true','true','true','true','true','true','" + isTLA + "','" + isAudit + "')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
			else
			{
			    moduleName = tblTransactionModule.getValueAt(k, 0).toString();
			    String grant = tblTransactionModule.getValueAt(k, 1).toString();
			    String isTLA = tblTransactionModule.getValueAt(k, 2).toString();
			    String isAudit = tblTransactionModule.getValueAt(k, 3).toString();
			    if (grant.equalsIgnoreCase("true") || isTLA.equalsIgnoreCase("true") || isAudit.equalsIgnoreCase("true"))
			    {
				sql = "insert into tbluserdtl values('" + userCode + "','" + moduleName + "'"
					+ ",'" + transButtons.elementAt(k) + "'," + sequence + ",'" + grant + "','" + grant + "'"
					+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + isTLA + "','" + isAudit + "')";
				clsGlobalVarClass.dbMysql.execute(sql);
			    }
			}
		    }

		    rowCount = tblUtilityModule.getRowCount();
		    for (int k = 0; k < rowCount; k++)
		    {
			sequence++;
			if (userType.equals("Super"))
			{
			    moduleName = tblUtilityModule.getValueAt(k, 0).toString();
			    sql = "insert into tblsuperuserdtl values('" + userCode
				    + "','" + moduleName + "','" + utilityButtons.elementAt(k)
				    + "'," + sequence + ",'true','true','true','true','true','true','true','true','true')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
			else
			{
			    moduleName = tblUtilityModule.getValueAt(k, 0).toString();
			    String grant = tblUtilityModule.getValueAt(k, 1).toString();
			    if (grant.equalsIgnoreCase("true"))
			    {
				sql = "insert into tbluserdtl values('" + userCode
					+ "','" + moduleName + "','" + utilityButtons.elementAt(k)
					+ "'," + sequence + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "'"
					+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "')";
				clsGlobalVarClass.dbMysql.execute(sql);
			    }
			}
		    }
		    rowCount = tblReportsModule.getRowCount();

		    for (int k = 0; k < rowCount; k++)
		    {
			sequence++;
			if (userType.equals("Super"))
			{
			    moduleName = tblReportsModule.getValueAt(k, 0).toString();
			    sql = "insert into tblsuperuserdtl values('" + userCode
				    + "','" + moduleName + "','" + reportButtons.elementAt(k)
				    + "'," + sequence + ",'true','true','true','true','true','true','true','true','true')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
			else
			{
			    moduleName = tblReportsModule.getValueAt(k, 0).toString();
			    String grant = tblReportsModule.getValueAt(k, 1).toString();
			    if (grant.equalsIgnoreCase("true"))
			    {
				sql = "insert into tbluserdtl values('" + userCode
					+ "','" + moduleName + "','" + reportButtons.elementAt(k)
					+ "'," + sequence + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "'"
					+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "')";
				clsGlobalVarClass.dbMysql.execute(sql);
			    }
			}
		    }
		}
		else
		{
		    if (userType.equals("Super"))
		    {
			int cnt1 = 0;
			String insertQuery = "insert into tbluserdtl values";
			String sql = "select a.strUserCode,a.strFormName,a.strButtonName,a.intSequence,"
				+ "a.strAdd,a.strEdit,a.strDelete,a.strView,a.strPrint,a.strSave,a.strGrant,a.strTLA,a.strAuditing"
				+ "from tbluserdtl a where a.strUserCode='" + selectedLikeUserCode + "'";
			ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rs.next())
			{
			    if (cnt1 == 0)
			    {
				insertQuery += "('" + userCode
					+ "','" + rs.getString(2) + "','" + rs.getString(3)
					+ "'," + rs.getInt(4) + ",'" + rs.getString(5) + "','" + rs.getString(6)
					+ "','" + rs.getString(7) + "','" + rs.getString(8) + "','" + rs.getString(9)
					+ "','" + rs.getString(10) + "','" + rs.getString(11) + "','" + rs.getString(12) + "','" + rs.getString(13) + "') ";
			    }
			    else
			    {
				insertQuery += ",('" + userCode
					+ "','" + rs.getString(2) + "','" + rs.getString(3)
					+ "'," + rs.getInt(4) + ",'" + rs.getString(5) + "','" + rs.getString(6)
					+ "','" + rs.getString(7) + "','" + rs.getString(8) + "','" + rs.getString(9)
					+ "','" + rs.getString(10) + "','" + rs.getString(11) + "','" + rs.getString(12) + "','" + rs.getString(13) + "') ";
			    }
			    cnt1++;
			}
			rs.close();

			if (cnt1 > 0)
			{
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
		    }
		    else
		    {
			int cnt1 = 0;
			String insertQuery = "insert into tbluserdtl values";
			String sql = "select a.strUserCode,a.strFormName,a.strButtonName,a.intSequence,"
				+ " a.strAdd,a.strEdit,a.strDelete,a.strView,a.strPrint,a.strSave,a.strGrant,a.strTLA,a.strAuditing "
				+ " from tbluserdtl a where a.strUserCode='" + selectedLikeUserCode + "' ";
			ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rs.next())
			{
			    if (cnt1 == 0)
			    {
				insertQuery += "('" + userCode
					+ "','" + rs.getString(2) + "','" + rs.getString(3)
					+ "'," + rs.getInt(4) + ",'" + rs.getString(5) + "','" + rs.getString(6)
					+ "','" + rs.getString(7) + "','" + rs.getString(8) + "','" + rs.getString(9)
					+ "','" + rs.getString(10) + "','" + rs.getString(11) + "','" + rs.getString(12) + "','" + rs.getString(13) + "') ";
			    }
			    else
			    {
				insertQuery += ",('" + userCode
					+ "','" + rs.getString(2) + "','" + rs.getString(3)
					+ "'," + rs.getInt(4) + ",'" + rs.getString(5) + "','" + rs.getString(6)
					+ "','" + rs.getString(7) + "','" + rs.getString(8) + "','" + rs.getString(9)
					+ "','" + rs.getString(10) + "','" + rs.getString(11) + "','" + rs.getString(12) + "','" + rs.getString(13) + "') ";
			    }
			    cnt1++;
			}
			rs.close();

			if (cnt1 > 0)
			{
			    clsGlobalVarClass.dbMysql.execute(insertQuery);
			}
		    }
		}

		sql = "delete from tbluserdtl where strUserCode='" + userCode + "' and strAdd='false' "
			+ "and strEdit='false' and strDelete='false' and strView='false' and strPrint='false' "
			+ "and strSave='false' and strGrant='false' and strTLA='false' and strAuditing='false' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		sequence = 1;

		sql = "select * from tbluserdtl where strUserCode='" + userCode + "'";
		ResultSet rsFormCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsFormCount.next())
		{
		    if (userType.equals("Super"))
		    {
			sql = "update tblsuperuserdtl set intSequence=" + sequence + " "
				+ "where strUserCode='" + userCode + "' "
				+ "and strFormName='" + rsFormCount.getString(2) + "'";
		    }
		    else
		    {
			sql = "update tbluserdtl set intSequence=" + sequence + " "
				+ "where strUserCode='" + userCode + "' "
				+ "and strFormName='" + rsFormCount.getString(2) + "'";
		    }
		    clsGlobalVarClass.dbMysql.execute(sql);
		    sequence++;
		}
		rsFormCount.close();

		if (cnt > 0)
		{
		    insertFlag = true;
		}
	    }
	    else
	    {
		// Update User Forms
		int SequenceNoToInsert = 1;
		String userType = "";
		if (chkSuperUser.isSelected())
		{
		    userType = "Super";
		}
		else
		{
		    userType = "op";
		}

		String query = "Update tbluserhd "
			+ " SET strUserCode=? ,strUserName=? ,"
			+ " strPassword=? ,strSuperType=? ,dteValidDate=? ,strPOSAccess=? ,"
			+ " strUserEdited=? ,dteDateEdited=? ,imgUserIcon=? ,strImgUserIconPath=?"
			+ ",strWaiterNo=?,strClientCode=?"
			+ ",intNoOfDaysReportsView=? "
			+ " where strUserCode=? ";
		PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
		pre.setString(1, userCode);
		pre.setString(2, txtUserName.getText());
		pre.setString(3, password);
		pre.setString(4, userType);
		pre.setString(5, validDate);
		pre.setString(6, selectedPOSCodes);
		pre.setString(7, clsGlobalVarClass.gUserCode);
		pre.setString(8, dteEdited);

//                String imageFilePath = System.getProperty("user.dir") + "\\UserImageIcon\\" + txtUserCode.getText() + ".jpg";
//                File fileUserImage = new File(imageFilePath);
//
//                if (fileUserImage.exists())
//                {
//                    FileInputStream fileInputStream = new FileInputStream(fileUserImage);
//                    pre.setBinaryStream(9, (InputStream) fileInputStream, (int) fileUserImage.length());
//                    pre.setString(10, imageFilePath);
//                }
//                else
//                {
//                    pre.setString(9, "");
//                    pre.setString(10, "");
//
//                }
//                if (txtUserImage.getText().toString().trim().isEmpty())
//                {
//                    pre.setString(9, "");
//                    pre.setString(10, "");
//                }
//                else
//                {
//                    pre.setBinaryStream(9, (InputStream) fileInImg, (int) imgFile.length());
//                    pre.setString(10, userImagefilePath);
//                }
		FileInputStream fileInputStream = null;
		if (txtUserImage.getText().toString().trim().isEmpty())
		{
		    pre.setString(9, "");
		    pre.setString(10, "");
		}
		else
		{

//                 strPath = imgFile.getAbsolutePath();
		    String extension = "";
		    int i = strPath.lastIndexOf('.');
		    if (i > 0)
		    {
			extension = strPath.substring(i + 1);
		    }
		    File fileItemImage = new File(System.getProperty("user.dir") + "\\UserImageIcon\\" + txtUserCode.getText().trim() + "." + extension);

		    if (imgFile == null)
		    {
			String imagePath = fileItemImage.getAbsolutePath();
			fileInputStream = new FileInputStream(fileItemImage);
			pre.setBinaryStream(9, (InputStream) fileInputStream, (int) fileItemImage.length());
			pre.setString(10, imagePath);

		    }
		    else
		    {
			String imagePath = imgFile.getAbsolutePath();
			fileInImg = new FileInputStream(imgFile);
			pre.setBinaryStream(9, (InputStream) fileInImg, (int) imgFile.length());
			pre.setString(10, imagePath);

		    }

		}
		if (cmbWaiterName.getSelectedItem().toString().trim().length() > 0)
		{
		    pre.setString(11, mapWaiterNameCode.get(cmbWaiterName.getSelectedItem().toString()));
		}
		else
		{
		    pre.setString(11, " ");
		}
		pre.setString(12, clsGlobalVarClass.gClientCode);

		pre.setString(13, noOfDaysReportsView);
		pre.setString(14, userCode);
		int exc = pre.executeUpdate();

		pre.close();

		if (fileInputStream != null)
		{
		    fileInputStream.close();
		}
		if (chkSuperUser.isSelected())
		{
		    Map<String, Integer> hmOldSequence = new HashMap<>();
		    String sqlSequenceNo = "select strFormName,intSequence from tblsuperuserdtl where strUserCode='" + txtUserCode.getText() + "' ";
		    ResultSet rssequenceNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlSequenceNo);
		    while (rssequenceNo.next())
		    {
			hmOldSequence.put(rssequenceNo.getString(1), rssequenceNo.getInt(2));
		    }
		    rssequenceNo.close();
		    sql = "delete from tbluserdtl where strUserCode='" + userCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    sql = "delete from tblsuperuserdtl where strUserCode='" + userCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    String grant = "true";
		    for (int i = 0; i < tblMasterModule.getRowCount(); i++)
		    {
			sql = "insert into tblsuperuserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				+ "strAdd,strEdit,strDelete,strView,"
				+ "strPrint,strSave,strGrant,strTLA,strAuditing) values "
				+ "('" + userCode + "','" + tblMasterModule.getValueAt(i, 0).toString() + "' ,'" + masterButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "')";
			clsGlobalVarClass.dbMysql.execute(sql);
			SequenceNoToInsert++;
		    }
		    for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
		    {
			sql = "insert into  tblsuperuserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				+ "strAdd,strEdit,strDelete,strView,"
				+ "strPrint,strSave,strGrant,strTLA,strAuditing) values "
				+ "('" + userCode + "','" + tblTransactionModule.getValueAt(i, 0).toString() + "' ,'" + transButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "')";
			clsGlobalVarClass.dbMysql.execute(sql);
			SequenceNoToInsert++;

		    }
		    for (int i = 0; i < tblUtilityModule.getRowCount(); i++)
		    {
			sql = "insert into  tblsuperuserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				+ "strAdd,strEdit,strDelete,strView,"
				+ "strPrint,strSave,strGrant) values "
				+ "('" + userCode + "','" + tblUtilityModule.getValueAt(i, 0).toString() + "' ,'" + utilityButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				+ ",'" + grant + "','" + grant + "','" + grant + "')";
			clsGlobalVarClass.dbMysql.execute(sql);
			SequenceNoToInsert++;
		    }
		    for (int i = 0; i < tblReportsModule.getRowCount(); i++)
		    {
			sql = "insert into  tblsuperuserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				+ "strAdd,strEdit,strDelete,strView,"
				+ "strPrint,strSave,strGrant) values "
				+ "('" + userCode + "','" + tblReportsModule.getValueAt(i, 0).toString() + "' ,'" + reportButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				+ ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				+ ",'" + grant + "','" + grant + "','" + grant + "')";
			clsGlobalVarClass.dbMysql.execute(sql);
			SequenceNoToInsert++;
		    }

		    for (String formName : hmOldSequence.keySet())
		    {
			String sqlUpdate = "update tblsuperuserdtl set intSequence='" + hmOldSequence.get(formName) + "' where strUserCode='" + userCode + "' and strFormName='" + formName + "';";
			clsGlobalVarClass.dbMysql.execute(sqlUpdate);
		    }
		}
		else
		{
		    Map<String, Integer> hmOldSequence = new HashMap<>();
		    String sqlSequenceNo = "select strFormName,intSequence from tbluserdtl where strUserCode='" + txtUserCode.getText() + "' ";
		    ResultSet rssequenceNo = clsGlobalVarClass.dbMysql.executeResultSet(sqlSequenceNo);
		    while (rssequenceNo.next())
		    {
			hmOldSequence.put(rssequenceNo.getString(1), rssequenceNo.getInt(2));
		    }
		    rssequenceNo.close();

		    sql = "delete from tbluserdtl where strUserCode='" + userCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);

		    /*
                     * for(Map.Entry<String,List<clsOperatorDtl>>
                     * entry:hmUserDtl.entrySet()) { userCode=entry.getKey();
                     * sql = "delete from tbluserdtl where strUserCode='" +
                     * userCode + "'"; clsGlobalVarClass.dbMysql.execute(sql);
                     * sql = "delete from tblsuperuserdtl where strUserCode='" +
                     * userCode + "'"; clsGlobalVarClass.dbMysql.execute(sql);
                     }
		     */
		    for (int i = 0; i < tblMasterModule.getRowCount(); i++)
		    {
			String grant = tblMasterModule.getValueAt(i, 1).toString();

			if (grant.equals("true"))
			{
			    sql = "insert into  tbluserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				    + "strAdd,strEdit,strDelete,strView,"
				    + "strPrint,strSave,strGrant,strTLA,strAuditing) values "
				    + "('" + userCode + "','" + tblMasterModule.getValueAt(i, 0).toString() + "' ,'" + masterButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				    + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				    + ",'" + grant + "','" + grant + "','" + grant + "','false','true')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			}
		    }
		    for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
		    {
			String grant = tblTransactionModule.getValueAt(i, 1).toString();
			String isTLA = tblTransactionModule.getValueAt(i, 2).toString();
			String isAudit = tblTransactionModule.getValueAt(i, 3).toString();
			if ((grant.equals("true") || isTLA.equals("true")) || isAudit.equals("true"))
			{
			    sql = "insert into  tbluserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				    + "strAdd,strEdit,strDelete,strView,strPrint,strSave,strGrant,strTLA,strAuditing) values "
				    + "('" + userCode + "','" + tblTransactionModule.getValueAt(i, 0).toString() + "' ,'" + transButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				    + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' "
				    + ",'" + grant + "','" + grant + "','" + grant + "','" + isTLA + "','" + isAudit + "')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			    SequenceNoToInsert++;
			}
		    }
		    for (int i = 0; i < tblUtilityModule.getRowCount(); i++)
		    {
			String grant = tblUtilityModule.getValueAt(i, 1).toString();
			if (grant.equals("true"))
			{
			    sql = "insert into  tbluserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				    + "strAdd,strEdit,strDelete,strView,"
				    + "strPrint,strSave,strGrant,strTLA,strAuditing) values "
				    + "('" + userCode + "','" + tblUtilityModule.getValueAt(i, 0).toString() + "' ,'" + utilityButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				    + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "' ,'" + grant + "','" + grant + "','" + grant + "','false','true')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			    SequenceNoToInsert++;
			}
		    }
		    for (int i = 0; i < tblReportsModule.getRowCount(); i++)
		    {
			String grant = tblReportsModule.getValueAt(i, 1).toString();
			if (grant.equals("true"))
			{
			    sql = "insert into  tbluserdtl (strUserCode,strFormName,strButtonName,intSequence,"
				    + "strAdd,strEdit,strDelete,strView,"
				    + "strPrint,strSave,strGrant,strTLA,strAuditing) values "
				    + "('" + userCode + "','" + tblReportsModule.getValueAt(i, 0).toString() + "' ,'" + reportButtons.elementAt(i) + "'," + SequenceNoToInsert + ""
				    + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "'"
				    + ",'" + grant + "','false','true')";
			    clsGlobalVarClass.dbMysql.execute(sql);
			    SequenceNoToInsert++;
			}
		    }

		    for (String formName : hmOldSequence.keySet())
		    {
			String sqlUpdate = "update tbluserdtl set intSequence='" + hmOldSequence.get(formName) + "' where strUserCode='" + userCode + "' and strFormName='" + formName + "';";
			clsGlobalVarClass.dbMysql.execute(sqlUpdate);
		    }
		}
		updateFlag = true;
	    }

	    sql = "delete from tbluserforms where strUserCreated='" + userCode + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tblsuperuserdtl a  "
		    + "join tbluserhd b on a.strUserCode=b.strUserCode "
		    + "set a.strAuditing='false' "
		    + "where b.strUserType='OWNER' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    if (insertFlag)
	    {
		sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ " where strTableName='User' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		new frmOkPopUp(this, "User Registered Successfully", "Successfull", 3).setVisible(true);
	    }
	    if (updateFlag)
	    {
		funCopyImageIfPresent();
		sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ " where strTableName='User' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
	    }
	    funResetField();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to copy image if present
     *
     * @throws IOException
     */
    private void funCopyImageIfPresent() throws IOException
    {
	if (null != imgFile && null != lblImgShow.getIcon())
	{
	    String imagePath = imgFile.getAbsolutePath();

	    String extension = "";

	    int i = imagePath.lastIndexOf('.');
	    if (i > 0)
	    {
		extension = imagePath.substring(i + 1);
	    }

	    String filePath = System.getProperty("user.dir");
	    funCreateitemImagesFolder();
	    fileInImg.close();
//            if(fileInImg!=null)
//                {    

	    //}
	    destFile = new File(filePath + "/UserImageIcon/" + txtUserCode.getText().trim() + "." + extension);
	    if (destFile.exists())
	    {
		destFile.setExecutable(true);
		destFile.setWritable(true);
		destFile.delete();

	    }
	    copyImageFiles(imgFile, destFile);

	}
    }

    /**
     * Manisha 29 April 2017
     */
    private void funCreateitemImagesFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/UserImageIcon");
	    if (!file.exists())
	    {
		if (file.mkdir())
		{
		    //System.out.println("Directory is created!");
		}
		else
		{
		    //System.out.println("Failed to create directory!");
		}
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to copy image file
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private void copyImageFiles(File source, File dest) throws IOException, SecurityException
    {
	try
	{

	    Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);
	    //,StandardCopyOption.REPLACE_EXISTING
	    source.delete();
	}
	catch (FileAlreadyExistsException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
//       fileInImg.close();
//        boolean bool = false;
//        bool=dest.delete();
//        Files.copy(source.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
//         bool=dest.delete();
	imgFile = null;
	destFile = null;
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetData(Object[] data)
    {
	try
	{
//            imgFile = null;
	    sql = "select * from tbluserhd where strUserCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    ResultSet rsUserInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsUserInfo.next();
	    String encKey = "04081977";
	    String password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().decrypt(encKey, rsUserInfo.getString(3));
	    Object arrUserReg[] = data;
	    String moduleType = null;
	    boolean superFlag = false;
	    txtUserCode.setText(rsUserInfo.getString(1));
	    txtUserName.setText(rsUserInfo.getString(2));
	    txtPassword.setText(password);
	    txtConfirmPassword.setText(password);

	    txtUserImage.setText(rsUserInfo.getString(14));
	    strPath = rsUserInfo.getString(14);

	    if (rsUserInfo.getString(4).equalsIgnoreCase("Super"))
	    {
		chkSuperUser.setSelected(true);
		superFlag = true;
	    }
	    else
	    {
		chkSuperUser.setSelected(false);
		superFlag = false;
	    }
	    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(rsUserInfo.getString(5));
	    dteValid.setDate(date);

	    if (rsUserInfo.getString(16).trim().length() > 0)
	    {
		cmbWaiterName.setSelectedItem(mapWaiterCodeName.get(rsUserInfo.getString(16)));
	    }
	    else
	    {
		cmbWaiterName.setSelectedItem(" ");
	    }
	    
	    String noOfDaysReportsView=rsUserInfo.getString(18);
	    txtNoOfDaysReportsView.setText(noOfDaysReportsView);

	    String posCodeForSplit = rsUserInfo.getString(6);
	    String[] posCode = posCodeForSplit.split(",");

	    for (int i = 0; i < tblPOS.getRowCount(); i++)
	    {
		String temPosCode = tblPOS.getValueAt(i, 0).toString();
		for (int j = 0; j < posCode.length; j++)
		{
		    if (posCode[j].equals(temPosCode))
		    {
			tblPOS.setValueAt(true, i, 2);
			break;
		    }
		    else
		    {
			tblPOS.setValueAt(false, i, 2);
		    }
		}
	    }
	    ////////////////Image shown code///////////////////

	    String extension = "";

	    int j = strPath.lastIndexOf('.');
	    if (j > 0)
	    {
		extension = strPath.substring(j + 1);
	    }

	    String imageFilePath = System.getProperty("user.dir") + "\\UserImageIcon\\" + txtUserCode.getText().trim() + "." + extension;
	    File fileUserImage = new File(imageFilePath);
	    if (fileUserImage.exists())
	    {
//               // lblImgShow.setIcon(new javax.swing.ImageIcon(imageFilePath));
//
//                imgFile = fileUserImage;
//                String imagePath = imgFile.getAbsolutePath();
//                userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//                txtUserImage.setText(imgFile.getAbsolutePath());
//                fileInImg = new FileInputStream(imgFile);
//                lblImgShow.setText("");
//                imgBf = funScaleImage(100, 100, userImagefilePath);
//                ImageIO.write(imgBf, "png", imgFile);
//                lblImgShow.setIcon(new javax.swing.ImageIcon(imgBf));
		funSetImage();
	    }
	    else
	    {
		Blob blob = rsUserInfo.getBlob(13);
		//InputStream inImg = blob.getBinaryStream();

		if (blob.length() > 0)
		{
		    InputStream inImg = blob.getBinaryStream(1, blob.length());
		    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
		    //BufferedImage image = ImageIO.read(inImg);
		    OutputStream outImg = new FileOutputStream(fileUserImage);
		    int c = 0;
		    while ((c = inImg.read()) > -1)
		    {
			outImg.write(c);
		    }
		    outImg.close();
		    inImg.close();

		    if (fileUserImage.exists())
		    {
			lblImgShow.setIcon(new javax.swing.ImageIcon(imageFilePath));
			txtUserImage.setText(imageFilePath);
		    }
		    else
		    {
			txtUserImage.setText("");
			lblImgShow.setIcon(null);
		    }
		}
		else
		{
		    txtUserImage.setText("");
		    lblImgShow.setIcon(null);
		}
	    }

	    //////////////////////////////////////////////////
	    String[] datauserDtl = null;
	    if (superFlag)
	    {
		for (int k = 0; k < tblMasterModule.getRowCount(); k++)
		{
		    tblMasterModule.setValueAt(true, k, 1);
		}
		for (int k = 0; k < tblTransactionModule.getRowCount(); k++)
		{
		    tblTransactionModule.setValueAt(true, k, 1);
		    tblTransactionModule.setValueAt(true, k, 3);
		}
		for (int k = 0; k < tblUtilityModule.getRowCount(); k++)
		{
		    tblUtilityModule.setValueAt(true, k, 1);
		}
		for (int k = 0; k < tblReportsModule.getRowCount(); k++)
		{
		    tblReportsModule.setValueAt(true, k, 1);
		}
	    }
	    else
	    {
		funUncheckAll();
		String sqlUserDtl = "SELECT * FROM  tbluserdtl WHERE strUserCode='" + arrUserReg[0] + "'";
		System.out.println(sqlUserDtl);
		ResultSet rsUserDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
		while (rsUserDtl.next())
		{
		    sqlUserDtl = "select strModuleType from tblforms where strModuleName='" + rsUserDtl.getString(2) + "'";
		    ResultSet rsModuleType = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
		    rsModuleType.next();
		    moduleType = rsModuleType.getString(1);
		    rsModuleType.close();

		    if ((!rsUserDtl.getString(2).equals("Customer Master")) && moduleType.equals("M"))
		    {
			String moduleName = rsUserDtl.getString(2);
			for (int i = 0; i < tblMasterModule.getRowCount(); i++)
			{
			    if (tblMasterModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
			    {
				tblMasterModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
			    }
			}
		    }
		    else
		    {
			String moduleName = rsUserDtl.getString(2);
			for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
			{
			    if (tblTransactionModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
			    {
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(12)), i, 2);
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(13)), i, 3);
			    }
			}
		    }

		    if (moduleType.equals("T") || moduleType.equals("AT"))
		    {
			String moduleName = rsUserDtl.getString(2);
			for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
			{
			    if (tblTransactionModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
			    {
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(12)), i, 2);
				tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(13)), i, 3);
			    }
			}
		    }
		    if (moduleType.equals("U"))
		    {
			String moduleName = rsUserDtl.getString(2);
			for (int i = 0; i < tblUtilityModule.getRowCount(); i++)
			{
			    if (tblUtilityModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
			    {
				tblUtilityModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
			    }
			}
		    }
		    if (moduleType.equals("R"))
		    {
			//dm3.removeRow(0);
			String moduleName = rsUserDtl.getString(2);
			for (int i = 0; i < tblReportsModule.getRowCount(); i++)
			{
			    if (tblReportsModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
			    {
				tblReportsModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
			    }
			}
		    }
		}
		rsUserDtl.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSetDataForLikeUser(Object[] data)
    {
	try
	{
	    sql = "select * from tbluserhd where strUserCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    ResultSet rsUserInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsUserInfo.next())
	    {
		Object arrUserReg[] = data;
		String moduleType = "";
		boolean superFlag = false;

		txtUserImage.setText(rsUserInfo.getString(14));
		if (rsUserInfo.getString(4).equalsIgnoreCase("Super"))
		{
		    chkSuperUser.setSelected(true);
		    superFlag = true;
		}
		else
		{
		    chkSuperUser.setSelected(false);
		    superFlag = false;
		}
		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(rsUserInfo.getString(5));
		dteValid.setDate(date);

		String posCodeForSplit = rsUserInfo.getString(6);
		String[] posCode = posCodeForSplit.split(",");

		for (int i = 0; i < tblPOS.getRowCount(); i++)
		{
		    String temPosCode = tblPOS.getValueAt(i, 0).toString();
		    for (int j = 0; j < posCode.length; j++)
		    {
			if (posCode[j].equals(temPosCode))
			{
			    tblPOS.setValueAt(true, i, 2);
			    break;
			}
			else
			{
			    tblPOS.setValueAt(false, i, 2);
			}
		    }
		}

		String[] datauserDtl = null;
		if (superFlag)
		{
		    for (int k = 0; k < tblMasterModule.getRowCount(); k++)
		    {
			tblMasterModule.setValueAt(true, k, 1);
		    }
		    for (int k = 0; k < tblTransactionModule.getRowCount(); k++)
		    {
			tblTransactionModule.setValueAt(true, k, 1);
			tblTransactionModule.setValueAt(true, k, 3);
		    }
		    for (int k = 0; k < tblUtilityModule.getRowCount(); k++)
		    {
			tblUtilityModule.setValueAt(true, k, 1);
		    }
		    for (int k = 0; k < tblReportsModule.getRowCount(); k++)
		    {
			tblReportsModule.setValueAt(true, k, 1);
		    }
		}
		else
		{
		    funUncheckAll();
		    String sqlUserDtl = "SELECT * FROM  tbluserdtl WHERE strUserCode='" + arrUserReg[0] + "'";
		    System.out.println(sqlUserDtl);
		    ResultSet rsUserDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
		    while (rsUserDtl.next())
		    {
			sqlUserDtl = "select strModuleType from tblforms where strModuleName='" + rsUserDtl.getString(2) + "'";
			ResultSet rsModuleType = clsGlobalVarClass.dbMysql.executeResultSet(sqlUserDtl);
			if (rsModuleType.next())
			{
			    moduleType = rsModuleType.getString(1);
			}
			rsModuleType.close();

			if (moduleType.equals("M"))
			{
			    String moduleName = rsUserDtl.getString(2);
			    for (int i = 0; i < tblMasterModule.getRowCount(); i++)
			    {
				if (tblMasterModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
				{
				    tblMasterModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				}
			    }
			}

			if (moduleType.equals("T") || moduleType.equals("AT"))
			{
			    String moduleName = rsUserDtl.getString(2);
			    for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
			    {
				if (tblTransactionModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
				{
				    tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				    tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(12)), i, 2);
				    tblTransactionModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(13)), i, 3);
				}
			    }
			}
			if (moduleType.equals("U"))
			{
			    String moduleName = rsUserDtl.getString(2);
			    for (int i = 0; i < tblUtilityModule.getRowCount(); i++)
			    {
				if (tblUtilityModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
				{
				    tblUtilityModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				}
			    }
			}
			if (moduleType.equals("R"))
			{
			    String moduleName = rsUserDtl.getString(2);
			    for (int i = 0; i < tblReportsModule.getRowCount(); i++)
			    {
				if (tblReportsModule.getValueAt(i, 0).toString().equalsIgnoreCase(moduleName))
				{
				    tblReportsModule.setValueAt(Boolean.parseBoolean(rsUserDtl.getString(11)), i, 1);
				}
			    }
			}
		    }
		    rsUserDtl.close();
		}
	    }
	    rsUserInfo.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to uncheck all
     */
    private void funUncheckAll()
    {

	for (int i = 0; i < tblMasterModule.getRowCount(); i++)
	{
	    tblMasterModule.setValueAt(false, i, 1);
	}
	for (int i = 0; i < tblTransactionModule.getRowCount(); i++)
	{
	    tblTransactionModule.setValueAt(false, i, 1);
	    tblTransactionModule.setValueAt(false, i, 3);
	}
	for (int i = 0; i < tblUtilityModule.getRowCount(); i++)
	{
	    tblUtilityModule.setValueAt(false, i, 1);
	}
	for (int i = 0; i < tblReportsModule.getRowCount(); i++)
	{
	    tblReportsModule.setValueAt(false, i, 1);
	}
    }

    /**
     * This method is used to load pos table
     */
    private void funFillPOSSelectionGrid()
    {
	try
	{
	    DefaultTableModel dmTable = (DefaultTableModel) tblPOS.getModel();
	    dmTable.getDataVector().removeAllElements();
	    String selectQuery = "select strPosCode,strPosName from tblposmaster";
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    while (rsPOS.next())
	    {
		Object[] ob =
		{
		    rsPOS.getString(1), rsPOS.getString(2), true
		};
		dmTable.addRow(ob);
	    }
	    rsPOS.close();
	    tblPOS.setModel(dmTable);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private BufferedImage funScaleImage(int WIDTH, int HEIGHT, String filename)
    {
	BufferedImage bi = null;
	try
	{
	    ImageIcon ii = new ImageIcon(filename);//path to image
	    bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	    Graphics2D gra2d = (Graphics2D) bi.createGraphics();
	    gra2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    gra2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	    return null;
	}
	return bi;
    }

    private void funFillWaiterNameComboBox()
    {
	try
	{

	    String selectQuery = "select a.strWaiterNo,a.strWShortName,a.strWFullName from tblwaitermaster a";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    cmbWaiterName.addItem(" ");
	    mapWaiterNameCode.put(" ", " ");
	    mapWaiterCodeName.put(" ", " ");
	    while (rs.next())
	    {
		cmbWaiterName.addItem(rs.getString(2));//waiterName
		mapWaiterNameCode.put(rs.getString(2), rs.getString(1));//name->code
		mapWaiterCodeName.put(rs.getString(1), rs.getString(2));//code->name
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }


    private void btnSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseClicked
	// TODO add your handling code here:
	funUserOperations();
    }//GEN-LAST:event_btnSubmitMouseClicked

    private void txtConfirmPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtConfirmPasswordMouseClicked
	// TODO add your handling code here:
	if (txtConfirmPassword.getText().length() == 0)
	{
	    new frmAlfaNumericKeyBoard(this, true, "2", "Enter Password").setVisible(true);
	    txtConfirmPassword.setText(clsGlobalVarClass.gKeyboardValue);
	}
	else
	{
	    new frmAlfaNumericKeyBoard(this, true, txtConfirmPassword.getText(), "2", "Enter Password").setVisible(true);
	    txtConfirmPassword.setText(clsGlobalVarClass.gKeyboardValue);
	}
    }//GEN-LAST:event_txtConfirmPasswordMouseClicked

    private void txtConfirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConfirmPasswordKeyPressed
	// TODO add your handling code here:
	// cmbPOSSelection.requestFocus();
	if (evt.getKeyCode() == 10)
	{
	    btnSubmit.requestFocus();
	}

    }//GEN-LAST:event_txtConfirmPasswordKeyPressed

    private void dteValidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteValidKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPassword.requestFocus();
	}
    }//GEN-LAST:event_dteValidKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("User Registration");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked

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
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed

	if (evt.getKeyCode() == 10)
	{
	    txtConfirmPassword.requestFocus();
	}
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void chkSuperUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuperUserActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSuperUserActionPerformed

    private void tblPOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOSMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_tblPOSMouseClicked

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
	// TODO add your handling code here:
	try
	{
	    JFileChooser jfc = new JFileChooser();
	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	    {
		imgFile = jfc.getSelectedFile();
		String imagePath = imgFile.getAbsolutePath();
		userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
		txtUserImage.setText(imgFile.getAbsolutePath());
		fileInImg = new FileInputStream(imgFile);
		lblImgShow.setText("");
		imgBf = funScaleImage(100, 100, userImagefilePath);
		ImageIO.write(imgBf, "png", imgFile);
		lblImgShow.setIcon(new javax.swing.ImageIcon(imgBf));
		strPath = imgFile.getAbsolutePath();
//
//                String userDirectory = System.getProperty("user.dir");
//                File fileReportImage = new File(userDirectory + "\\UserImageIcon\\" + txtUserCode.getText() + ".jpg");
//                if (fileReportImage.exists())
//                {
//                    fileReportImage.setWritable(true);
//                    fileReportImage.delete();
//                    Files.deleteIfExists(fileReportImage.toPath());
//                }
//                Files.copy(imgFile.toPath(), fileReportImage.toPath());
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void txtUserCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtUserCodeActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtUserCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funUserDetails();
	}
	if (evt.getKeyCode() == 10)
	{
	    txtUserName.requestFocus();
	}
    }//GEN-LAST:event_txtUserCodeKeyPressed

    private void txtUserImageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserImageKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtUserImageKeyPressed

    private void btnSubmitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSubmitKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funUserOperations();
	}
    }//GEN-LAST:event_btnSubmitKeyPressed

    private void btnHelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnHelpKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtUserName.requestFocus();
	}
    }//GEN-LAST:event_btnHelpKeyPressed

    private void btnBrowseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBrowseKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnSubmit.requestFocus();
	}
    }//GEN-LAST:event_btnBrowseKeyPressed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
	// TODO add your handling code here:
	funUserOperations();
	fileInImg = null;
	imgBf = null;

	lblImgShow.setIcon(null);
	userImagefilePath = null;
	destFile = null;
	imgFile = null;
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("User Registration");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLikeUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLikeUserMouseClicked
	// TODO add your handling code here:
	funLikeUserDetails();
    }//GEN-LAST:event_btnLikeUserMouseClicked

    private void btnLikeUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLikeUserKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnLikeUserKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("User Registration");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("User Registration");
    }//GEN-LAST:event_formWindowClosing

    private void btnHelpMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnHelpMouseEntered
    {//GEN-HEADEREND:event_btnHelpMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnHelpMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelMasters;
    private javax.swing.JPanel PanelTransactions;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnLikeUser;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JCheckBox chkSuperUser;
    private javax.swing.JComboBox cmbWaiterName;
    private com.toedter.calendar.JDateChooser dteValid;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblConfirmPassword;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblImgShow;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNoOfDaysReportsView;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserImage;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblValidTill;
    private javax.swing.JLabel lblWaiterName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lbluserCode;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelReports;
    private javax.swing.JPanel panelUtilities;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblMasterModule;
    private javax.swing.JTable tblPOS;
    private javax.swing.JTable tblReportsModule;
    private javax.swing.JTable tblTransactionModule;
    private javax.swing.JTable tblUtilityModule;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtNoOfDaysReportsView;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserCode;
    private javax.swing.JTextField txtUserImage;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

    private void funCustomiseTransactionModuleTableColumnHeader()
    {
	JTableHeader header = tblTransactionModule.getTableHeader();
	header.setPreferredSize(new Dimension(header.getWidth(), 50));

	DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tblTransactionModule.getTableHeader().getDefaultRenderer();
	renderer.setHorizontalAlignment(JLabel.LEFT);
	renderer.setVerticalTextPosition(JLabel.TOP);
	renderer.setVerticalAlignment(JLabel.TOP);
    }

    /**
     * This method is used to set image
     */
    private void funSetImage()
    {
	try
	{
	    String imgCode = txtUserCode.getText().trim();

	    String extension = "";

	    int i = strPath.lastIndexOf('.');
	    if (i > 0)
	    {
		extension = strPath.substring(i + 1);
	    }
	    if (imgCode.length() > 0)
	    {
		String filePath = System.getProperty("user.dir");
		File f = new File(filePath + "/UserImageIcon/" + imgCode + "." + extension);
		ImageIcon icon1 = new ImageIcon(ImageIO.read(f));
		lblImgShow.setIcon(icon1);
		// txtItemImage.setText(userImagefilePath);
	    }
	}
	catch (Exception e)
	{
	    lblImgShow.setText("NO IMAGE");
	    lblImgShow.setIcon(null);
	}

    }
}
