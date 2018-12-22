/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author sss11
 */
public class frmTableReservation extends javax.swing.JFrame
{

    private String fileName;
    private String buildingType;
    private StringBuilder sqlQuery;
    private final SimpleDateFormat yyyyMMddDateFormate;
    private final Date date;
    private String customerCode;
    private String buildingCode;
    private String buildingZoneCode = "";
    private String reservationDate;
    private String reservationTime;
    private String specialInformation;
    private String reservationCode;
    private String tableNo = "";
    private boolean isCustomerExists;
    private DefaultTableModel dtmTableReservation;
    private SimpleDateFormat yyyyMMddHHmmssTimeFormate;
    private final SimpleDateFormat hhmmssTimeFormate;
    private clsUtility objUtility;
    private String fromTableNo;
    private String exportFormName, ExportReportPath;
    private java.util.Vector vTableReservationExcelColLength;
    public frmTableReservation() throws ParseException
    {
	initComponents();
	objUtility = new clsUtility();
	lblUserCode.setText(clsGlobalVarClass.gUserCode);
	lblPosName.setText(clsGlobalVarClass.gPOSName);
	lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	lblModuleName1.setText(clsGlobalVarClass.gSelectedModule);
	sqlQuery = new StringBuilder();
	txtContactNo.requestFocus(true);
	yyyyMMddDateFormate = new SimpleDateFormat("yyyy-MM-dd");
	yyyyMMddHHmmssTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
	hhmmssTimeFormate = new SimpleDateFormat("HH:mm:ss");
	date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	dteReservationDate.setDate(date);
	dteFromDate.setDate(date);
	dteToDate.setDate(date);
	funGetPOSNames();
	cmbPOS.setSelectedItem(clsGlobalVarClass.gPOSName);
	funSetShortcutKeys();
	exportFormName = "TableReservationList";
	ExportReportPath = clsPosConfigFile.exportReportPath;
	if(txtContactNo.equals(""))
	{
	    if(tabPaneTableReservation.getSelectedIndex()==2)
	    {
		JOptionPane.showMessageDialog(null, "Please select Mobile Number");
		return;
	    }	
	} 
	
	vTableReservationExcelColLength = new java.util.Vector();
	vTableReservationExcelColLength.add("10#Left"); //
	vTableReservationExcelColLength.add("20#Left"); //
	vTableReservationExcelColLength.add("10#Right"); //
	vTableReservationExcelColLength.add("10#Right"); //
	vTableReservationExcelColLength.add("10#Right");//
	vTableReservationExcelColLength.add("10#Left"); //
	vTableReservationExcelColLength.add("10#Left"); //
	vTableReservationExcelColLength.add("20#Right"); //
	vTableReservationExcelColLength.add("10#Right"); //
	vTableReservationExcelColLength.add("10#Right");//
	//"Contact No", "Customer Name","Smoking","Table","PAX", "Date", "Time","SpecialInfo","TableNo","Reservation Code"
	
    }

    private void funGetPOSNames()
    {
	try
	{

	    sqlQuery.setLength(0);
	    sqlQuery.append("select strPosName from tblposmaster ");
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    while (resultSet.next())
	    {
		cmbPOS.addItem(resultSet.getString(1));
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funResetFields()
    {
	txtReservationCode.setText("");
	txtContactNo.setText("");
	txtCustomerName.setText("");
	txtBuildingCode.setText("");
	txtBuildingName.setText("");
	cmbCity1.setSelectedIndex(0);
	txtTableName.setText("");
	cmbHour.setSelectedIndex(0);
	cmbMinutes.setSelectedIndex(0);
	// cmbSeconds.setSelectedIndex(0);
	cmbAMPM.setSelectedIndex(0);
	cmbSmoking.setSelectedIndex(0);
	txtPAX.setText("1");
	txtSpecialInformation1.setText("");
	dteReservationDate.setDate(date);
	btnSave.setText("Save");

	chkCancelReservation.setEnabled(false);
	chkCancelReservation.setSelected(false);

	customerCode = "";
	buildingCode = "";
	buildingZoneCode = "";
	reservationDate = "";
	reservationTime = "";
	specialInformation = "";
	reservationCode = "";
	tableNo = "";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName1 = new javax.swing.JLabel();
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
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        tabPaneTableReservation = new javax.swing.JTabbedPane();
        panelTableReservation = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblReservationCode = new javax.swing.JLabel();
        txtReservationCode = new javax.swing.JTextField();
        lblContactNo = new javax.swing.JLabel();
        txtContactNo = new javax.swing.JTextField();
        btnContactNoHelp = new javax.swing.JButton();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblAddress = new javax.swing.JLabel();
        txtBuildingCode = new javax.swing.JTextField();
        txtBuildingName = new javax.swing.JTextField();
        cmbPOS = new javax.swing.JComboBox();
        lblTableName = new javax.swing.JLabel();
        txtTableName = new javax.swing.JTextField();
        btnTableHelp = new javax.swing.JButton();
        lblReservationDate = new javax.swing.JLabel();
        dteReservationDate = new com.toedter.calendar.JDateChooser();
        lblTime = new javax.swing.JLabel();
        cmbHour = new javax.swing.JComboBox();
        cmbMinutes = new javax.swing.JComboBox();
        cmbAMPM = new javax.swing.JComboBox();
        txtPAX = new javax.swing.JTextField();
        lblCustomerName2 = new javax.swing.JLabel();
        cmbSmoking = new javax.swing.JComboBox();
        lblSmoking = new javax.swing.JLabel();
        lblSpecialInformation = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose1 = new javax.swing.JButton();
        cmbCity1 = new javax.swing.JComboBox();
        lblPOSName = new javax.swing.JLabel();
        chkCancelReservation = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSpecialInformation1 = new javax.swing.JTextArea();
        lblPOSName1 = new javax.swing.JLabel();
        cmbReservationType = new javax.swing.JComboBox();
        panelReservation = new javax.swing.JPanel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblFromDate = new javax.swing.JLabel();
        lblToDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        btnExecute = new javax.swing.JButton();
        scrollPaneTableReservation = new javax.swing.JScrollPane();
        tblTableReservation = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnClose2 = new javax.swing.JButton();
        lblFromTime = new javax.swing.JLabel();
        cmbFromTimeHour = new javax.swing.JComboBox();
        cmbFromTimeMinutes = new javax.swing.JComboBox();
        cmbFromTimeAMPM = new javax.swing.JComboBox();
        lblToTime = new javax.swing.JLabel();
        cmbToTimeHour = new javax.swing.JComboBox();
        cmbToTimeMinutes = new javax.swing.JComboBox();
        cmbToTimeAMPM = new javax.swing.JComboBox();
        btnCancleReservation = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        lblTotNoShow = new javax.swing.JLabel();
        lblTotBooking = new javax.swing.JLabel();
        lblTotSeated = new javax.swing.JLabel();
        lblTotCancelled = new javax.swing.JLabel();
        txtTotalBooking = new javax.swing.JTextField();
        txtTotSeated = new javax.swing.JTextField();
        txtTotalNoShow = new javax.swing.JTextField();
        txtTotCancelled = new javax.swing.JTextField();
        btnReset1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblReservationHistory = new javax.swing.JTable();
        btnExportReservHistory = new javax.swing.JButton();
        btnClose3 = new javax.swing.JButton();

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
        panelHeader.add(lblProductName);

        lblModuleName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName1.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName1);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("Table Reservation");
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

        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 600));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(800, 600));

        tabPaneTableReservation.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabPaneTableReservationStateChanged(evt);
            }
        });
        tabPaneTableReservation.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tabPaneTableReservationFocusGained(evt);
            }
        });

        panelTableReservation.setOpaque(false);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(14, 7, 7));
        lblModuleName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModuleName.setText("Table Reservation");

        lblReservationCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReservationCode.setText("Reservation Code     :");

        txtReservationCode.setEditable(false);
        txtReservationCode.setEnabled(false);
        txtReservationCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtReservationCodeMouseClicked(evt);
            }
        });

        lblContactNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblContactNo.setText("Contact No.  :");

        txtContactNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtContactNoFocusLost(evt);
            }
        });
        txtContactNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtContactNoMouseClicked(evt);
            }
        });
        txtContactNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtContactNoKeyPressed(evt);
            }
        });

        btnContactNoHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnContactNoHelp.setText("...");
        btnContactNoHelp.setToolTipText("Help");
        btnContactNoHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnContactNoHelp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnContactNoHelp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnContactNoHelpActionPerformed(evt);
            }
        });

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Customer Name       :");

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
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtCustomerNameKeyReleased(evt);
            }
        });

        lblAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAddress.setText("Area/City                :");

        txtBuildingCode.setEditable(false);
        txtBuildingCode.setEnabled(false);
        txtBuildingCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBuildingCodeMouseClicked(evt);
            }
        });

        txtBuildingName.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtBuildingNameFocusLost(evt);
            }
        });
        txtBuildingName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBuildingNameMouseClicked(evt);
            }
        });
        txtBuildingName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBuildingNameKeyPressed(evt);
            }
        });

        cmbPOS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbPOS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPOSActionPerformed(evt);
            }
        });
        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPOSKeyPressed(evt);
            }
        });

        lblTableName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTableName.setText("Table No                :");

        txtTableName.setEditable(false);
        txtTableName.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtTableNameFocusLost(evt);
            }
        });
        txtTableName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTableNameMouseClicked(evt);
            }
        });
        txtTableName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTableNameKeyPressed(evt);
            }
        });

        btnTableHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnTableHelp.setText("...");
        btnTableHelp.setToolTipText("Help");
        btnTableHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableHelp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnTableHelp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTableHelpActionPerformed(evt);
            }
        });

        lblReservationDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReservationDate.setText("Date                      :");

        dteReservationDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteReservationDateKeyPressed(evt);
            }
        });

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTime.setText("Time  :");

        cmbHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH:", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        cmbHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbHourKeyPressed(evt);
            }
        });

        cmbMinutes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM:", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        cmbMinutes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbMinutesKeyPressed(evt);
            }
        });

        cmbAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbAMPM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAMPMKeyPressed(evt);
            }
        });

        txtPAX.setText("0");
        txtPAX.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPAXMouseClicked(evt);
            }
        });
        txtPAX.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPAXActionPerformed(evt);
            }
        });
        txtPAX.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPAXKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtPAXKeyReleased(evt);
            }
        });

        lblCustomerName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName2.setText("PAX  :");

        cmbSmoking.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "YES", "NO" }));
        cmbSmoking.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSmokingKeyPressed(evt);
            }
        });

        lblSmoking.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSmoking.setText("Smoking :");

        lblSpecialInformation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSpecialInformation.setText("Special Information  :");

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnClose1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose1.setForeground(new java.awt.Color(255, 255, 255));
        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose1.setText("CLOSE");
        btnClose1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClose1ActionPerformed(evt);
            }
        });

        cmbCity1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbCity1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pune", "Agalgaon", "Agartala", "Agra", "Ahmedabad", "Ahmednagar", "Ajmer", "Akluj", "Akola", "Akot", "Allahabad", "Allepey", "Amalner", "Ambernath", "Amravati", "Amritsar", "Anand", "Arvi", "Asansol", "Ashta", "Aurangabad", "Aziwal", "Baddi", "Bangalore", "Bansarola", "Baramati", "Bareilly", "Baroda", "Barshi", "Beed", "Belgum", "Bellary", "Bhandara", "Bhilai", "Bhivandi", "Bhiwandi", "Bhopal", "Bhubaneshwar", "Bhusawal", "Bikaner", "Bokaro", "Bombay", "Buldhana", "Burhanpur", "Chandigarh", "Chandigarh", "Chattisgad", "Chennai(Madras)", "Cochin", "Coimbature", "Dehradun", "Delhi", "Dhanbad", "Dhule", "Faridabad", "Goa", "Gujrat", "Gurgaon", "Guwahati", "Gwalior", "Hyderabad", "Ichalkaranji", "Indapur", "Indore", "Jabalpur", "Jaipur", "Jalandhar", "Jalgaon", "Jalna", "Jamshedpur", "Kalamnuri", "Kanpur", "Karad", "Kochi(Cochin)", "Kolhapur", "Kolkata(Calcutta)", "Kozhikode(Calicut)", "Latur", "Lucknow", "Ludhiana", "Mumbai", "Madurai", "Mangalvedha", "Manmad", "Meerut", "Mumbai(Bombay)", "Mysore", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Orisa", "Osmanabad", "Pachora", "Pandharpur", "Parbhani", "Patna", "Pratapgad", "Raipur", "Rajasthan", "Rajkot", "Ranchi", "Ratnagiri", "Salem", "Sangamner", "Sangli", "Satara", "Sawantwadi", "Secunderabad", "Shirdi", "Sindhudurga", "Solapur", "Srinagar", "Surat", "Tiruchirapalli", "Vadodara(Baroda)", "Varanasi(Benares)", "Vijayawada", "Visakhapatnam", "Yawatmal", "Other" }));
        cmbCity1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCity1ActionPerformed(evt);
            }
        });
        cmbCity1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCity1KeyPressed(evt);
            }
        });

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS                       :");

        chkCancelReservation.setText("Cancel Reservation");
        chkCancelReservation.setEnabled(false);
        chkCancelReservation.setOpaque(false);

        txtSpecialInformation1.setColumns(20);
        txtSpecialInformation1.setRows(5);
        jScrollPane1.setViewportView(txtSpecialInformation1);

        lblPOSName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName1.setText("Reservation Type     :");

        cmbReservationType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbReservationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BOOKING", "WALK-IN" }));
        cmbReservationType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbReservationTypeActionPerformed(evt);
            }
        });
        cmbReservationType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbReservationTypeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelTableReservationLayout = new javax.swing.GroupLayout(panelTableReservation);
        panelTableReservation.setLayout(panelTableReservationLayout);
        panelTableReservationLayout.setHorizontalGroup(
            panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTableReservationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableReservationLayout.createSequentialGroup()
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTableReservationLayout.createSequentialGroup()
                                .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCustomerName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSmoking)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSmoking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableReservationLayout.createSequentialGroup()
                                .addComponent(lblTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTableHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCustomerName2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPAX))
                            .addGroup(panelTableReservationLayout.createSequentialGroup()
                                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                                        .addComponent(lblReservationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtReservationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblContactNo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnContactNoHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                                        .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtBuildingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtBuildingName, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbCity1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                                        .addComponent(lblSpecialInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panelTableReservationLayout.createSequentialGroup()
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(34, 34, 34)
                                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(btnClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblReservationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelTableReservationLayout.createSequentialGroup()
                                                .addComponent(dteReservationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblTime)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelTableReservationLayout.createSequentialGroup()
                                                .addComponent(cmbReservationType, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(chkCancelReservation)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(174, 174, 174))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableReservationLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblModuleName)
                        .addGap(293, 293, 293))
                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                        .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelTableReservationLayout.setVerticalGroup(
            panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTableReservationLayout.createSequentialGroup()
                .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTableReservationLayout.createSequentialGroup()
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblReservationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReservationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnContactNoHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSmoking, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSmoking, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuildingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuildingName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCity1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49))
                    .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPOSName1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReservationType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkCancelReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dteReservationDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReservationDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbHour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnTableHelp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCustomerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPAX, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSpecialInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(panelTableReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        tabPaneTableReservation.addTab("Table Reservation", panelTableReservation);

        panelReservation.setOpaque(false);

        lblFromDate.setText("From Date :");

        lblToDate.setText("To Date :");

        btnExecute.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExecute.setText("Execute");
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExecute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteActionPerformed(evt);
            }
        });

        scrollPaneTableReservation.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPaneTableReservationMouseClicked(evt);
            }
        });

        dtmTableReservation=new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Contact No", "Customer Name","Smoking","Table","PAX", "Date", "Time","SpecialInfo","Status","Select","TableNo","Reservation Code"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class,java.lang.Boolean.class,java.lang.String.class,java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false,false,false,true,false,false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        };
        tblTableReservation.setModel(dtmTableReservation);
        tblTableReservation.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTableReservationMouseClicked(evt);
            }
        });
        scrollPaneTableReservation.setViewportView(tblTableReservation);

        btnClear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearActionPerformed(evt);
            }
        });

        btnClose2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose2.setForeground(new java.awt.Color(255, 255, 255));
        btnClose2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose2.setText("Close");
        btnClose2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClose2ActionPerformed(evt);
            }
        });

        lblFromTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromTime.setText("From Time  :");

        cmbFromTimeHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH:", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        cmbFromTimeHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromTimeHourKeyPressed(evt);
            }
        });

        cmbFromTimeMinutes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM:", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        cmbFromTimeMinutes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromTimeMinutesKeyPressed(evt);
            }
        });

        cmbFromTimeAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbFromTimeAMPM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromTimeAMPMKeyPressed(evt);
            }
        });

        lblToTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToTime.setText("To Time  :");

        cmbToTimeHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH:", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        cmbToTimeHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToTimeHourKeyPressed(evt);
            }
        });

        cmbToTimeMinutes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM:", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        cmbToTimeMinutes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToTimeMinutesKeyPressed(evt);
            }
        });

        cmbToTimeAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbToTimeAMPM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToTimeAMPMKeyPressed(evt);
            }
        });

        btnCancleReservation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancleReservation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancleReservation.setText("Cancel ");
        btnCancleReservation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancleReservation.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancleReservation.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancleReservationActionPerformed(evt);
            }
        });

        btnExport.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });

        lblTotNoShow.setText("Total No Show Pax :");

        lblTotBooking.setText("Total Booking Pax :");

        lblTotSeated.setText("Total Seated Pax :");

        lblTotCancelled.setText("Total Cancelled Pax:");

        txtTotalBooking.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalBookingActionPerformed(evt);
            }
        });

        txtTotSeated.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotSeatedActionPerformed(evt);
            }
        });

        txtTotalNoShow.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalNoShowActionPerformed(evt);
            }
        });

        txtTotCancelled.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotCancelledActionPerformed(evt);
            }
        });

        btnReset1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset1.setText("RESET");
        btnReset1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReset1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReset1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelReservationLayout = new javax.swing.GroupLayout(panelReservation);
        panelReservation.setLayout(panelReservationLayout);
        panelReservationLayout.setHorizontalGroup(
            panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReservationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneTableReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelReservationLayout.createSequentialGroup()
                        .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelReservationLayout.createSequentialGroup()
                                .addComponent(lblFromTime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFromTimeHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFromTimeMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFromTimeAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblToTime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbToTimeHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbToTimeMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbToTimeAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelReservationLayout.createSequentialGroup()
                                .addComponent(lblFromDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(lblToDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(45, 45, 45)
                        .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExecute, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCancleReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelReservationLayout.createSequentialGroup()
                        .addComponent(lblTotBooking)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotSeated)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotSeated, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblTotNoShow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalNoShow, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotCancelled)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotCancelled, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        panelReservationLayout.setVerticalGroup(
            panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReservationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancleReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFromTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFromTimeHour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFromTimeMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFromTimeAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbToTimeHour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbToTimeMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbToTimeAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollPaneTableReservation, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReservationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotNoShow, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotCancelled, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotSeated, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotSeated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalNoShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotCancelled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelReservationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTotBooking, txtTotalBooking});

        panelReservationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTotSeated, txtTotSeated});

        panelReservationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTotNoShow, txtTotalNoShow});

        panelReservationLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTotCancelled, txtTotCancelled});

        tabPaneTableReservation.addTab("Reservations", panelReservation);

        tblReservationHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String []
            {
                "Customer Name", "Reservation Date", "Reservation Time", "Special Info", "Reservation Type"
            }
        ));
        jScrollPane2.setViewportView(tblReservationHistory);

        btnExportReservHistory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnExportReservHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExportReservHistory.setText("Export");
        btnExportReservHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExportReservHistory.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExportReservHistory.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportReservHistoryActionPerformed(evt);
            }
        });

        btnClose3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose3.setForeground(new java.awt.Color(255, 255, 255));
        btnClose3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose3.setText("Close");
        btnClose3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClose3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportReservHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportReservHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabPaneTableReservation.addTab("History", jPanel1);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(tabPaneTableReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPaneTableReservation)
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("Table Reservation");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Table Reservation");
    }//GEN-LAST:event_formWindowClosing

    private void tabPaneTableReservationStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tabPaneTableReservationStateChanged
    {//GEN-HEADEREND:event_tabPaneTableReservationStateChanged

        if (tabPaneTableReservation.getSelectedIndex() == 1)
        {
            cmbFromTimeHour.setSelectedIndex(12);
            cmbFromTimeMinutes.setSelectedIndex(1);
            cmbFromTimeAMPM.setSelectedIndex(0);

            cmbToTimeHour.setSelectedIndex(11);
            cmbToTimeMinutes.setSelectedIndex(1);
            cmbToTimeAMPM.setSelectedIndex(1);

            funExecuteDefault();
        }
	else if(tabPaneTableReservation.getSelectedIndex()==2 && txtContactNo.getText().equals(""))
	{   
	    JOptionPane.showMessageDialog(null, "Please select Mobile Number");
	    tabPaneTableReservation.setSelectedIndex(0);
	    txtContactNo.requestFocus();
	    return;
	} 
	else if(tabPaneTableReservation.getSelectedIndex()==2 && !txtContactNo.getText().equals(""))
	{
	    funReservationHistory(txtContactNo.getText());
	}    

    }//GEN-LAST:event_tabPaneTableReservationStateChanged

    private void btnCancleReservationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancleReservationActionPerformed
    {//GEN-HEADEREND:event_btnCancleReservationActionPerformed
        // TODO add your handling code here:
        if (tblTableReservation.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Please select reservation to cancle");
            return;
        }
        else
        {
            frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do You Want To Cancel Reservation???");
            okOb.setVisible(true);
            int res = okOb.getResult();
            if (res == 1)
            {
                funCancelReservation();
            }
        }
    }//GEN-LAST:event_btnCancleReservationActionPerformed

    private void cmbToTimeAMPMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToTimeAMPMKeyPressed
    {//GEN-HEADEREND:event_cmbToTimeAMPMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbToTimeAMPMKeyPressed

    private void cmbToTimeMinutesKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToTimeMinutesKeyPressed
    {//GEN-HEADEREND:event_cmbToTimeMinutesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbToTimeMinutesKeyPressed

    private void cmbToTimeHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToTimeHourKeyPressed
    {//GEN-HEADEREND:event_cmbToTimeHourKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbToTimeHourKeyPressed

    private void cmbFromTimeAMPMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromTimeAMPMKeyPressed
    {//GEN-HEADEREND:event_cmbFromTimeAMPMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFromTimeAMPMKeyPressed

    private void cmbFromTimeMinutesKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromTimeMinutesKeyPressed
    {//GEN-HEADEREND:event_cmbFromTimeMinutesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFromTimeMinutesKeyPressed

    private void cmbFromTimeHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromTimeHourKeyPressed
    {//GEN-HEADEREND:event_cmbFromTimeHourKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFromTimeHourKeyPressed

    private void btnClose2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClose2ActionPerformed
    {//GEN-HEADEREND:event_btnClose2ActionPerformed
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Table Reservation");
    }//GEN-LAST:event_btnClose2ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClearActionPerformed
    {//GEN-HEADEREND:event_btnClearActionPerformed
        funResetfields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExecuteActionPerformed
    {//GEN-HEADEREND:event_btnExecuteActionPerformed

        if (dteFromDate.getDate() == null || dteToDate.getDate() == null)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid Date.");
            return;
        }
        if (dteToDate.getDate().before(dteFromDate.getDate()))
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid Date.");
            return;
        }
        if (cmbFromTimeHour.getSelectedIndex() < 1 || cmbFromTimeMinutes.getSelectedIndex() < 1)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid From Time.");
            return;
        }
        if (cmbToTimeHour.getSelectedIndex() < 1 || cmbToTimeMinutes.getSelectedIndex() < 1)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid To Time.");
            return;
        }
        funExecuteNLoadTable();
    }//GEN-LAST:event_btnExecuteActionPerformed

    private void cmbReservationTypeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbReservationTypeKeyPressed
    {//GEN-HEADEREND:event_cmbReservationTypeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbReservationTypeKeyPressed

    private void cmbReservationTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbReservationTypeActionPerformed
    {//GEN-HEADEREND:event_cmbReservationTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbReservationTypeActionPerformed

    private void cmbCity1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbCity1KeyPressed
    {//GEN-HEADEREND:event_cmbCity1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtTableName.requestFocus();
        }
    }//GEN-LAST:event_cmbCity1KeyPressed

    private void cmbCity1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCity1ActionPerformed
    {//GEN-HEADEREND:event_cmbCity1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCity1ActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClose1ActionPerformed
    {//GEN-HEADEREND:event_btnClose1ActionPerformed

        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Table Reservation");
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed

        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        try
        {
            if (txtContactNo.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Please Enter Contact No.");
                return;
            }
            if (txtContactNo.getText().contains(","))
            {

            }
            else
            {
                if (txtContactNo.getText().matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$"))//\\d{10}
                {
                    //                System.out.println("Pattern Matches");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please Enter Valid Mobile No.");
                    return;
                }
            }

            if (txtCustomerName.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Please Enter Customer Name.");
                return;
            }
            if (cmbCity1.getSelectedIndex() < 0)
            {
                JOptionPane.showMessageDialog(null, "Please Select City.");
                return;
            }
            if (clsGlobalVarClass.gCustAreaCompulsory)
            {
                if (txtBuildingName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Please Select Area.");
                    return;
                }
            }
            //            if (txtTableName.getText().isEmpty())
            //
            {
                //                JOptionPane.showMessageDialog(null, "Please Select Table.");
                //                return;
                //            }
            if (cmbHour.getSelectedIndex() < 1 || cmbMinutes.getSelectedIndex() < 1)
            {
                JOptionPane.showMessageDialog(null, "Please Select Valid Time.");
                return;
            }
            try
            {
                Integer.parseInt(txtPAX.getText());
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, "Please Enter Valid PAX No.");
                return;
            }

            reservationDate = yyyyMMddDateFormate.format(dteReservationDate.getDate());
            Date selectedDate = dteReservationDate.getDate();

            reservationTime = cmbHour.getSelectedItem().toString() + ":" + cmbMinutes.getSelectedItem().toString() + ":00";
            specialInformation = txtSpecialInformation1.getText();

            SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa"); //yyyy/MM/dd HH:mm:ss"

            String tmp = reservationDate + " " + cmbHour.getSelectedItem().toString() + ":" + cmbMinutes.getSelectedItem().toString() + ":00 " + cmbAMPM.getSelectedItem().toString();
            Date tmpDate = f1.parse(tmp);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date posDate = simpleDateFormat.parse(clsGlobalVarClass.gPOSDateForTransaction);

            String strPOSDate = simpleDateFormat.format(posDate);
            posDate = simpleDateFormat.parse(strPOSDate);

            String tmp1 = simpleDateFormat.format(tmpDate);
            tmpDate = simpleDateFormat.parse(tmp1);

            if (tmpDate.before(posDate))
            {
                JOptionPane.showMessageDialog(null, "Please Select Valid Date And Time.");
                return;
            }

            String time = hhmmssTimeFormate.format(tmpDate);
            reservationTime = time;

            if (btnSave.getText().equalsIgnoreCase("Save"))
            {
                funSaveReservation();
            }
            else
            {
                funUpdateReservation();
            }
        }
	}
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSaveMouseClicked
    {//GEN-HEADEREND:event_btnSaveMouseClicked

    }//GEN-LAST:event_btnSaveMouseClicked

    private void cmbSmokingKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbSmokingKeyPressed
    {//GEN-HEADEREND:event_cmbSmokingKeyPressed
        if (evt.getKeyCode() == 10)
        {
            txtPAX.requestFocus();
        }
    }//GEN-LAST:event_cmbSmokingKeyPressed

    private void txtPAXKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPAXKeyReleased
    {//GEN-HEADEREND:event_txtPAXKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPAXKeyReleased

    private void txtPAXKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPAXKeyPressed
    {//GEN-HEADEREND:event_txtPAXKeyPressed
        if (evt.getKeyCode() == 10)
        {
            txtSpecialInformation1.requestFocus();
        }
    }//GEN-LAST:event_txtPAXKeyPressed

    private void txtPAXActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPAXActionPerformed
    {//GEN-HEADEREND:event_txtPAXActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPAXActionPerformed

    private void txtPAXMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPAXMouseClicked
    {//GEN-HEADEREND:event_txtPAXMouseClicked
        if (txtPAX.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter PAX NO. ").setVisible(true);
            txtPAX.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtPAX.getText(), "Long", "Enter PAX NO.").setVisible(true);
            txtPAX.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPAXMouseClicked

    private void cmbAMPMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbAMPMKeyPressed
    {//GEN-HEADEREND:event_cmbAMPMKeyPressed
        if (evt.getKeyCode() == 10)
        {
            cmbPOS.requestFocus();
        }
    }//GEN-LAST:event_cmbAMPMKeyPressed

    private void cmbMinutesKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbMinutesKeyPressed
    {//GEN-HEADEREND:event_cmbMinutesKeyPressed
        if (evt.getKeyCode() == 10)
        {

        }
    }//GEN-LAST:event_cmbMinutesKeyPressed

    private void cmbHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbHourKeyPressed
    {//GEN-HEADEREND:event_cmbHourKeyPressed
        if (evt.getKeyCode() == 10)
        {
            cmbMinutes.requestFocus();
        }
    }//GEN-LAST:event_cmbHourKeyPressed

    private void dteReservationDateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteReservationDateKeyPressed
    {//GEN-HEADEREND:event_dteReservationDateKeyPressed
        if (evt.getKeyCode() == 10)
        {
            cmbHour.requestFocus();
        }
    }//GEN-LAST:event_dteReservationDateKeyPressed

    private void btnTableHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTableHelpActionPerformed
    {//GEN-HEADEREND:event_btnTableHelpActionPerformed
        setAlwaysOnTop(false);
        funSelectTableNo();
    }//GEN-LAST:event_btnTableHelpActionPerformed

    private void txtTableNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTableNameKeyPressed
    {//GEN-HEADEREND:event_txtTableNameKeyPressed
        if (evt.getKeyCode() == 10)
        {
            if (txtTableName.getText().isEmpty())
            {
                setAlwaysOnTop(false);
                funSelectTableNo();
            }
            else
            {
                dteReservationDate.requestFocus();
            }
        }
    }//GEN-LAST:event_txtTableNameKeyPressed

    private void txtTableNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTableNameMouseClicked
    {//GEN-HEADEREND:event_txtTableNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTableNameMouseClicked

    private void txtTableNameFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtTableNameFocusLost
    {//GEN-HEADEREND:event_txtTableNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTableNameFocusLost

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbPOSKeyPressed
    {//GEN-HEADEREND:event_cmbPOSKeyPressed
        if (evt.getKeyCode() == 10)
        {

        }
        txtSpecialInformation1.requestFocus();
    }//GEN-LAST:event_cmbPOSKeyPressed

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPOSActionPerformed
    {//GEN-HEADEREND:event_cmbPOSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPOSActionPerformed

    private void txtBuildingNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtBuildingNameKeyPressed
    {//GEN-HEADEREND:event_txtBuildingNameKeyPressed
        if (evt.getKeyCode() == 10)
        {
            cmbPOS.requestFocus();
        }
    }//GEN-LAST:event_txtBuildingNameKeyPressed

    private void txtBuildingNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBuildingNameMouseClicked
    {//GEN-HEADEREND:event_txtBuildingNameMouseClicked

        if (txtBuildingName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Building Name").setVisible(true);
            txtBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtBuildingName.getText(), "1", "Enter Building Name").setVisible(true);
            txtBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtBuildingNameMouseClicked

    private void txtBuildingNameFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtBuildingNameFocusLost
    {//GEN-HEADEREND:event_txtBuildingNameFocusLost

    }//GEN-LAST:event_txtBuildingNameFocusLost

    private void txtBuildingCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBuildingCodeMouseClicked
    {//GEN-HEADEREND:event_txtBuildingCodeMouseClicked

        setAlwaysOnTop(false);
        buildingType = "residential";
        funSelectBuilding();
    }//GEN-LAST:event_txtBuildingCodeMouseClicked

    private void txtCustomerNameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCustomerNameKeyReleased
    {//GEN-HEADEREND:event_txtCustomerNameKeyReleased

    }//GEN-LAST:event_txtCustomerNameKeyReleased

    private void txtCustomerNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCustomerNameKeyPressed
    {//GEN-HEADEREND:event_txtCustomerNameKeyPressed

        if (evt.getKeyCode() == 10)
        {
            txtBuildingName.requestFocus();
        }
    }//GEN-LAST:event_txtCustomerNameKeyPressed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCustomerNameActionPerformed
    {//GEN-HEADEREND:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCustomerNameMouseClicked
    {//GEN-HEADEREND:event_txtCustomerNameMouseClicked

        try
        {
            if (txtCustomerName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Name.").setVisible(true);
                txtCustomerName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtCustomerName.getText(), "1", "Enter Customer Name.").setVisible(true);
                txtCustomerName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void btnContactNoHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnContactNoHelpActionPerformed
    {//GEN-HEADEREND:event_btnContactNoHelpActionPerformed
        setAlwaysOnTop(false);
        funSelectContactNo();
    }//GEN-LAST:event_btnContactNoHelpActionPerformed

    private void txtContactNoKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtContactNoKeyPressed
    {//GEN-HEADEREND:event_txtContactNoKeyPressed

        if (evt.getKeyCode() == 10)
        {
            if (txtContactNo.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Contact No.").setVisible(true);
                txtContactNo.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else if (txtContactNo.getText().length() == 10)
            {
                if (funCheckCustomerExist(txtContactNo.getText().trim()))
                {
                    cmbHour.requestFocus();
                }
                else
                {
                    txtCustomerName.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txtContactNoKeyPressed

    private void txtContactNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtContactNoMouseClicked
    {//GEN-HEADEREND:event_txtContactNoMouseClicked
        try
        {
            if (txtContactNo.getText().length() == 0)
            {
                new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile No ").setVisible(true);
                txtContactNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
                //new frmOkPopUp(this, "Please Enter Contact Nos Separated with Comma(,)", "Error", 0).setVisible(true);
                txtContactNo.requestFocus();
                return;
            }
            else
            {
                new frmNumericKeyboard(this, true, txtContactNo.getText(), "Long", "Enter Mobile No").setVisible(true);
                txtContactNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtContactNoMouseClicked

    private void txtContactNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtContactNoFocusLost
    {//GEN-HEADEREND:event_txtContactNoFocusLost

    }//GEN-LAST:event_txtContactNoFocusLost

    private void txtReservationCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtReservationCodeMouseClicked
    {//GEN-HEADEREND:event_txtReservationCodeMouseClicked
        setAlwaysOnTop(false);
        funSelectTableReservation();
    }//GEN-LAST:event_txtReservationCodeMouseClicked

    private void tabPaneTableReservationFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_tabPaneTableReservationFocusGained
    {//GEN-HEADEREND:event_tabPaneTableReservationFocusGained
       
    }//GEN-LAST:event_tabPaneTableReservationFocusGained

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportActionPerformed
    {//GEN-HEADEREND:event_btnExportActionPerformed
        // TODO add your handling code here:
	try
	{
	    if (dteFromDate.getDate() == null || dteToDate.getDate() == null)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid Date.");
            return;
        }
        if (dteToDate.getDate().before(dteFromDate.getDate()))
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid Date.");
            return;
        }
        if (cmbFromTimeHour.getSelectedIndex() < 1 || cmbFromTimeMinutes.getSelectedIndex() < 1)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid From Time.");
            return;
        }
        if (cmbToTimeHour.getSelectedIndex() < 1 || cmbToTimeMinutes.getSelectedIndex() < 1)
        {
            JOptionPane.showMessageDialog(null, "Please Select Valid To Time.");
            return;
        }
	
	File theDir = new File(ExportReportPath);
	File file = new File(ExportReportPath + File.separator + exportFormName + objUtility.funGetDateInString() + ".xls");
	if (!theDir.exists())
	{
	    theDir.mkdir();
	    //funExportFile(tblReservationHistory, file);
	    funExportClick();
	    //sendMail();
	}
	else
	{
	    //funExportFile(tblReservationHistory, file);
	    funExportClick();
	    //sendMail();
	}
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }//GEN-LAST:event_btnExportActionPerformed

    private void txtTotalBookingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotalBookingActionPerformed
    {//GEN-HEADEREND:event_txtTotalBookingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBookingActionPerformed

    private void txtTotSeatedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotSeatedActionPerformed
    {//GEN-HEADEREND:event_txtTotSeatedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotSeatedActionPerformed

    private void txtTotalNoShowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotalNoShowActionPerformed
    {//GEN-HEADEREND:event_txtTotalNoShowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalNoShowActionPerformed

    private void txtTotCancelledActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotCancelledActionPerformed
    {//GEN-HEADEREND:event_txtTotCancelledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotCancelledActionPerformed

    private void btnExportReservHistoryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportReservHistoryActionPerformed
    {//GEN-HEADEREND:event_btnExportReservHistoryActionPerformed
        // TODO add your handling code here:
	funExportHistoryClick();
    }//GEN-LAST:event_btnExportReservHistoryActionPerformed

    private void btnClose3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClose3ActionPerformed
    {//GEN-HEADEREND:event_btnClose3ActionPerformed
        // TODO add your handling code here:
	 dispose();
        clsGlobalVarClass.hmActiveForms.remove("Table Reservation");
    }//GEN-LAST:event_btnClose3ActionPerformed

    private void btnReset1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReset1ActionPerformed
    {//GEN-HEADEREND:event_btnReset1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReset1ActionPerformed

    private void scrollPaneTableReservationMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPaneTableReservationMouseClicked
    {//GEN-HEADEREND:event_scrollPaneTableReservationMouseClicked
        // TODO add your handling code here:
	
    }//GEN-LAST:event_scrollPaneTableReservationMouseClicked

    private void tblTableReservationMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTableReservationMouseClicked
    {//GEN-HEADEREND:event_tblTableReservationMouseClicked
        // TODO add your handling code here:
	try
	{
	DefaultTableModel dtm = (DefaultTableModel) tblTableReservation.getModel();
	
	    for(int i=0;i<dtm.getRowCount();i++)
	    {
		String reservationNo=dtm.getValueAt(i, 11).toString();
		boolean flgSelect=Boolean.parseBoolean(dtm.getValueAt(i, 9).toString());
		if(flgSelect)
		{
		    StringBuilder strBuilder = new StringBuilder("select a.strCancelReservation from tblreservation a\n" 
			    + "where a.strCancelReservation='Y' and a.strResCode='"+reservationNo+"'");
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(strBuilder.toString());
		    if(rs.next())
		    {
			JOptionPane.showMessageDialog(null, "This Reservation Already Cancelled");
			return;
		    }
		}
	    }
	   
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}    
    }//GEN-LAST:event_tblTableReservationMouseClicked

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
	    java.util.logging.Logger.getLogger(frmTableReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmTableReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmTableReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmTableReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    new frmTableReservation().setVisible(true);
		}
		catch (ParseException ex)
		{
		    clsGlobalVarClass.gLog.error(ex);
		}
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancleReservation;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose1;
    private javax.swing.JButton btnClose2;
    private javax.swing.JButton btnClose3;
    private javax.swing.JButton btnContactNoHelp;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnExportReservHistory;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTableHelp;
    private javax.swing.JCheckBox chkCancelReservation;
    private javax.swing.JComboBox cmbAMPM;
    private javax.swing.JComboBox cmbCity1;
    private javax.swing.JComboBox cmbFromTimeAMPM;
    private javax.swing.JComboBox cmbFromTimeHour;
    private javax.swing.JComboBox cmbFromTimeMinutes;
    private javax.swing.JComboBox cmbHour;
    private javax.swing.JComboBox cmbMinutes;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.JComboBox cmbReservationType;
    private javax.swing.JComboBox cmbSmoking;
    private javax.swing.JComboBox cmbToTimeAMPM;
    private javax.swing.JComboBox cmbToTimeHour;
    private javax.swing.JComboBox cmbToTimeMinutes;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteReservationDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblContactNo;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblCustomerName2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblFromTime;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPOSName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReservationCode;
    private javax.swing.JLabel lblReservationDate;
    private javax.swing.JLabel lblSmoking;
    private javax.swing.JLabel lblSpecialInformation;
    private javax.swing.JLabel lblTableName;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblToTime;
    private javax.swing.JLabel lblTotBooking;
    private javax.swing.JLabel lblTotCancelled;
    private javax.swing.JLabel lblTotNoShow;
    private javax.swing.JLabel lblTotSeated;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelReservation;
    private javax.swing.JPanel panelTableReservation;
    private javax.swing.JScrollPane scrollPaneTableReservation;
    private javax.swing.JTabbedPane tabPaneTableReservation;
    private javax.swing.JTable tblReservationHistory;
    private javax.swing.JTable tblTableReservation;
    public javax.swing.JTextField txtBuildingCode;
    private javax.swing.JTextField txtBuildingName;
    public javax.swing.JTextField txtContactNo;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtPAX;
    private javax.swing.JTextField txtReservationCode;
    private javax.swing.JTextArea txtSpecialInformation1;
    public javax.swing.JTextField txtTableName;
    private javax.swing.JTextField txtTotCancelled;
    private javax.swing.JTextField txtTotSeated;
    private javax.swing.JTextField txtTotalBooking;
    private javax.swing.JTextField txtTotalNoShow;
    // End of variables declaration//GEN-END:variables

    private void funSelectBuilding()
    {

	objUtility.funCallForSearchForm("BuildingMaster");
	new frmSearchFormDialog(null, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetBuildingCode(data);
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funSetBuildingCode(Object[] data)
    {

	if (buildingType.equals("residential"))
	{
	    txtBuildingCode.setText(data[0].toString());
	    buildingCode = data[0].toString();
	    txtBuildingName.setText(data[1].toString());
	}
	else
	{
//            txtOfficeBuildingCode.setText(data[0].toString());
//            txtOfficeBuildingName.setText(data[1].toString());
	}
    }

    private void funSaveReservation()
    {
	reservationCode = funGetReservationNo();
	txtReservationCode.setText(reservationCode);
	String ampm = cmbAMPM.getSelectedItem().toString();

	String strSmokingYN = "N";
	if (cmbSmoking.getSelectedItem().toString().equalsIgnoreCase("Yes"))
	{
	    strSmokingYN = "Y";
	}
	else
	{
	    strSmokingYN = "N";
	}
	if (isCustomerExists)
	{
	    try
	    {
		//update building data
		sqlQuery.setLength(0);
		sqlQuery.append("update  tblbuildingmaster set strBuildingName='" + txtBuildingName.getText() + "',strUserCreated='" + clsGlobalVarClass.gUserCode + "', strUserEdited='" + clsGlobalVarClass.gUserCode + "', dteDateCreated='" + clsGlobalVarClass.getCurrentDateTime() + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' where strBuildingCode='" + buildingCode + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		//update customer data
		sqlQuery.setLength(0);
		sqlQuery.append("update  tblcustomermaster set strCustomerName='" + txtCustomerName.getText() + "',strBuildingName='" + txtBuildingName.getText() + "',strCity='" + cmbCity1.getSelectedItem().toString() + "',longMobileNo='" + txtContactNo.getText() + "',strUserCreated='" + clsGlobalVarClass.gUserCode + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateCreated='" + clsGlobalVarClass.getCurrentDateTime() + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' where strCustomerCode='" + customerCode + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		sqlQuery.setLength(0);
		sqlQuery.append("select strPosCode from tblposmaster where strPosName='" + cmbPOS.getSelectedItem() + "'");
		ResultSet rsPOSCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		rsPOSCode.next();
		String strPOSCode = rsPOSCode.getString(1);

		sqlQuery.setLength(0);
		sqlQuery.append("INSERT INTO tblreservation "
			+ "(strResCode,strCustomerCode,intPax,strSmoking,dteResDate"
			+ ",tmeResTime,strAMPM,strSpecialInfo,strTableNo,strUserCreated"
			+ ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strPosCode,strReservationType) "
			+ "VALUES ");
		sqlQuery.append("('" + reservationCode + "', '" + customerCode + "','" + Integer.parseInt(txtPAX.getText()) + "'"
			+ ",'" + strSmokingYN + "','" + reservationDate + "', '" + reservationTime + "'"
			+ ",'" + ampm + "', '" + specialInformation + "','" + tableNo + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ", '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ", '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', '" + strPOSCode + "','"+cmbReservationType.getSelectedItem().toString()+"') ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		sqlQuery.setLength(0);
		sqlQuery.append("update tbltablemaster set strStatus='Reserve' where strTableNo='" + tableNo + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		JOptionPane.showMessageDialog(null, "Save Successfully.");
		
		if (clsGlobalVarClass.gTableReservationSMSYN)
		{
		    funSendTableReservationSMS(reservationCode, clsGlobalVarClass.gTableReservedSMS, "Table Reservation");
		}
		funResetFields();
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	else
	{
	    int affected = 0;
	    try
	    {
		customerCode = funGetCustomerCode();
		if (clsGlobalVarClass.gCustAreaCompulsory)
		{
		    if (txtBuildingCode.getText().isEmpty())
		    {
			buildingCode = funGetBuildingCode();
			txtBuildingCode.setText(buildingCode);
			//buildingZoneCode=funGetBuildingZoneCode();                      

			String buildingSql = "INSERT INTO tblbuildingmaster (strBuildingCode, strBuildingName,strUserCreated, strUserEdited, dteDateCreated,dteDateEdited,strClientCode, strZoneCode) "
				+ "VALUES ('" + buildingCode + "', '" + txtBuildingName.getText() + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', '" + buildingZoneCode + "') ";
			affected = clsGlobalVarClass.dbMysql.execute(buildingSql);
		    }
		}

		String customerSql = "INSERT INTO tblcustomermaster (strCustomerCode,strCustomerName,strBuldingCode,strBuildingName,strCity,longMobileNo,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
			+ "         VALUES ('" + customerCode + "', '" + txtCustomerName.getText() + "', '" + buildingCode + "', '" + txtBuildingName.getText() + "', '" + cmbCity1.getSelectedItem().toString() + "', '" + txtContactNo.getText().trim() + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "');";
		affected = clsGlobalVarClass.dbMysql.execute(customerSql);

		sqlQuery.setLength(0);
		sqlQuery.append("select strPosCode from tblposmaster where strPosName='" + cmbPOS.getSelectedItem() + "'");
		ResultSet rsPOSCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		rsPOSCode.next();
		String strPOSCode = rsPOSCode.getString(1);
		sqlQuery.setLength(0);
		sqlQuery.append("INSERT INTO tblreservation "
			+ "(strResCode,strCustomerCode,intPax,dteResDate,tmeResTime"
			+ ",strAMPM,strSpecialInfo,strTableNo,strUserCreated,strUserEdited"
			+ ",dteDateCreated,dteDateEdited,strClientCode,strPosCode) "
			+ "VALUES ");
		sqlQuery.append("('" + reservationCode + "', '" + customerCode + "'"
			+ ",'" + Integer.parseInt(txtPAX.getText()) + "', '" + reservationDate + "'"
			+ ", '" + reservationTime + "','" + ampm + "', '" + specialInformation + "','" + tableNo + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "'"
			+ ", '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ", '" + clsGlobalVarClass.gClientCode + "', '" + strPOSCode + "');");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		sqlQuery.setLength(0);
		sqlQuery.append("update tbltablemaster set strStatus='Reserve' where strTableNo='" + tableNo + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		JOptionPane.showMessageDialog(null, "Save Successfully.");
		if (clsGlobalVarClass.gTableReservationSMSYN)
		{
		    funSendTableReservationSMS(reservationCode, clsGlobalVarClass.gTableReservedSMS, "Table Reservation");
		}
		funResetFields();
	    }
	    catch (Exception ex)
	    {
		ex.printStackTrace();
	    }
	}
    }

    private String funGetReservationNo()
    {
	String reservationCode = "", strCode = "", code = "";
	long lastNo = 1;
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("select count(*) from tblreservation");
	    ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    rsCustCode.next();
	    int cntReserveCode = rsCustCode.getInt(1);
	    rsCustCode.close();

	    if (cntReserveCode > 0)
	    {
		sqlQuery.setLength(0);
		sqlQuery.append("select max(strResCode) from tblreservation");
		rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		rsCustCode.next();
		code = rsCustCode.getString(1);
		strCode = code.substring(2, code.length());
		lastNo = Long.parseLong(strCode);
		lastNo++;
		reservationCode = "RS" + String.format("%07d", lastNo);

		rsCustCode.close();
	    }
	    else
	    {
		reservationCode = "RS0000001";
		//reservCode = "RS000001";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return reservationCode;
    }

    private boolean funCheckCustomerExist(String contactNo)
    {
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("select strCustomerCode,strCustomerName,strBuldingCode,strBuildingName,strCity from tblcustomermaster  where longMobileNo='" + contactNo + "' ");
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    if (resultSet.next())
	    {
		customerCode = resultSet.getString("strCustomerCode");
		txtCustomerName.setText(resultSet.getString("strCustomerName"));
		buildingCode = resultSet.getString("strBuldingCode");
		txtBuildingCode.setText(buildingCode);
//                buildingZoneCode=resultSet.getString("strZoneCode");
		txtBuildingName.setText(resultSet.getString("strBuildingName"));
		cmbCity1.setSelectedItem(resultSet.getString("strCity"));
		isCustomerExists = true;
		return true;
	    }
	    else
	    {
		isCustomerExists = false;
		return false;
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return false;
    }

    private String funGetCustomerCode()
    {

	String customerCode = "", strCode = "", code = "";
	String propertCode = clsGlobalVarClass.gClientCode.substring(4);
	long lastNo = 1;
	try
	{
	    String sql = "select count(*) from tblcustomermaster";
	    ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCustCode.next();
	    int cntCustCode = rsCustCode.getInt(1);
	    rsCustCode.close();

	    if (cntCustCode > 0)
	    {
		sql = "select max(right(strCustomerCode,8)) from tblcustomermaster";
		rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsCustCode.next();
		code = rsCustCode.getString(1);
		StringBuilder sb = new StringBuilder(code);

		strCode = sb.substring(1, sb.length());

		lastNo = Long.parseLong(strCode);
		lastNo++;
		customerCode = propertCode + "C" + String.format("%07d", lastNo);

		rsCustCode.close();
	    }
	    else
	    {
		sql = "select longCustSeries from tblsetup";
		ResultSet rsCustSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsCustSeries.next())
		{
		    lastNo = Long.parseLong(rsCustSeries.getString(1));
		}
		rsCustSeries.close();
		customerCode = propertCode + "C" + String.format("%07d", lastNo);
		//CustCode = "C0000001";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return customerCode;

    }

    private String funGetBuildingCode()
    {
	String code = "", strCode = "";
	long lastNo = 1;
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("select count(*) from tblbuildingmaster");
	    ResultSet rsBuildCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    rsBuildCode.next();
	    int cntBuildCode = rsBuildCode.getInt(1);
	    rsBuildCode.close();

	    if (cntBuildCode > 0)
	    {
		sqlQuery.setLength(0);
		sqlQuery.append("select max(strBuildingCode) from tblbuildingmaster");
		rsBuildCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		rsBuildCode.next();
		code = rsBuildCode.getString(1);
		strCode = code.substring(1, code.length());
		lastNo = Long.parseLong(strCode);
		lastNo++;
		buildingCode = "B" + String.format("%07d", lastNo);

		rsBuildCode.close();
	    }
	    else
	    {
		buildingCode = "B0000001";
		//BuildingCode = "B0000001";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return buildingCode;
    }

    private String funGetBuildingZoneCode()
    {
	String code = "", strCode = "";
	long lastNo = 1;
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("select count(*) from tblbuildingmaster");
	    ResultSet rsBuildZoneCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    rsBuildZoneCode.next();
	    int cntBuildZoneCode = rsBuildZoneCode.getInt(1);
	    rsBuildZoneCode.close();

	    if (cntBuildZoneCode > 0)
	    {
		sqlQuery.setLength(0);
		sqlQuery.append("select max(strZoneCode) from tblbuildingmaster");
		rsBuildZoneCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		rsBuildZoneCode.next();
		code = rsBuildZoneCode.getString(1);
		strCode = code.substring(2, code.length());
		lastNo = Long.parseLong(strCode);
		lastNo++;
		buildingZoneCode = "BZ" + String.format("%07d", lastNo);

		rsBuildZoneCode.close();
	    }
	    else
	    {
		buildingZoneCode = "BZ00001";
		//BuildingZoneCode = "BZ000001";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return buildingZoneCode;
    }

    private void funUpdateReservation()
    {
	try
	{
	    String ampm = cmbAMPM.getSelectedItem().toString();
	    String strSmokingYN = "N";
	    if (cmbSmoking.getSelectedItem().toString().equalsIgnoreCase("Yes"))
	    {
		strSmokingYN = "Y";
	    }
	    else
	    {
		strSmokingYN = "N";
	    }

	    //update building data
	    sqlQuery.setLength(0);
	    sqlQuery.append("update  tblbuildingmaster "
		    + "set strBuildingName='" + txtBuildingName.getText() + "' "
		    + ",strUserCreated='" + clsGlobalVarClass.gUserCode + "' "
		    + ", strUserEdited='" + clsGlobalVarClass.gUserCode + "' "
		    + ", dteDateCreated='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + "where strBuildingCode='" + buildingCode + "' ");
	    clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

	    //update customer data
	    sqlQuery.setLength(0);
	    sqlQuery.append("update  tblcustomermaster "
		    + "set strBuldingCode='" + buildingCode + "' "
		    + ",strCustomerName='" + txtCustomerName.getText() + "' "
		    + ",strBuildingName='" + txtBuildingName.getText() + "' "
		    + ",strCity='" + cmbCity1.getSelectedItem().toString() + "' "
		    + ",longMobileNo='" + txtContactNo.getText() + "' "
		    + ",strUserCreated='" + clsGlobalVarClass.gUserCode + "' "
		    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "' "
		    + ",dteDateCreated='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + "where strCustomerCode='" + customerCode + "' ");
	    clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

	    //get POS code
	    sqlQuery.setLength(0);
	    sqlQuery.append("select strPosCode from tblposmaster where strPosName='" + cmbPOS.getSelectedItem() + "'");
	    ResultSet rsPOSCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    rsPOSCode.next();
	    String strPOSCode = rsPOSCode.getString(1);
	    //update table reservation data

	    String cancelReservation = "N";
	    if (chkCancelReservation.isSelected())
	    {
		cancelReservation = "Y";
	    }

	    sqlQuery.setLength(0);
	    sqlQuery.append("UPDATE tblreservation "
		    + "SET intPax='" + txtPAX.getText() + "',strSmoking='" + strSmokingYN + "'"
		    + ",dteResDate='" + reservationDate + "',tmeResTime='" + reservationTime + "'"
		    + ",strSpecialInfo='" + specialInformation + "',strTableNo='" + tableNo + "'"
		    + ",strAMPM='" + ampm + "' "
		    + ",strPosCode='" + strPOSCode + "' "
		    + ",strCancelReservation='" + cancelReservation + "',"
		    + " strReservationType = '"+cmbReservationType.getSelectedItem().toString()+"'"
		    + "WHERE strResCode='" + reservationCode + "' "
	    );
	    clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

	    String tableReserveStatus = "Reserve";
	    if (cancelReservation.equalsIgnoreCase("Y"))
	    {
		tableReserveStatus = "Normal";
	    }
	    if (fromTableNo.equals(tableNo))
	    {
		sqlQuery.setLength(0);
		sqlQuery.append("update tbltablemaster set strStatus='" + tableReserveStatus + "' where strTableNo='" + tableNo + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());
	    }
	    else
	    {
		sqlQuery.setLength(0);
		sqlQuery.append("update tbltablemaster set strStatus='Normal' where strTableNo='" + fromTableNo + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());

		sqlQuery.setLength(0);
		sqlQuery.append("update tbltablemaster set strStatus='" + tableReserveStatus + "' where strTableNo='" + tableNo + "' ");
		clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());
	    }

	    JOptionPane.showMessageDialog(null, "Updated Successfully");
	    funResetFields();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSelectTableReservation()
    {
	objUtility.funCallForSearchForm("TableReservation");
	new frmSearchFormDialog(null, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetTableReservationCode(data);
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funSetTableReservationCode(Object[] data)
    {

	reservationCode = data[0].toString();
	txtReservationCode.setText(reservationCode);
	btnSave.setText("Update");
	btnSave.setMnemonic('u');
	funSetTableReseveData(reservationCode);
    }

    private void funSetTableReseveData(String reservationCode)
    {
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("select a.strResCode,b.strCustomerCode,b.strCustomerName,ifnull(b.strBuldingCode,''),ifnull(b.strBuildingName,''), "
		    + "b.strCity,b.longMobileNo,ifnull(a.strTableNo,''),a.dteResDate,a.tmeResTime,a.intPax,a.strSmoking,a.strSpecialInfo , "
		    + "ifnull(d.strTableNo,''),ifnull(d.strTableName,''),a.strAMPM,a.strPosCode,a.strCancelReservation,a.strReservationType "
		    + "from tblreservation a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + "left outer join tblbuildingmaster c on b.strBuldingCode=c.strBuildingCode "
		    + "left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
		    + "where a.strResCode='" + reservationCode + "' ");
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    if (resultSet.next())
	    {
		customerCode = resultSet.getString(2);
		txtContactNo.setText(resultSet.getString(7));
		txtCustomerName.setText(resultSet.getString(3));
		buildingCode = resultSet.getString(4);
		txtBuildingCode.setText(buildingCode);
		txtBuildingName.setText(resultSet.getString(5));
		cmbCity1.setSelectedItem(resultSet.getString(6));
		tableNo = resultSet.getString(14);
		fromTableNo = tableNo;
		txtTableName.setText(resultSet.getString(15));

		String pcode = resultSet.getString(17);

		String canselReservation = resultSet.getString(18);
		chkCancelReservation.setEnabled(true);
		if (canselReservation.equalsIgnoreCase("Y"))
		{
		    chkCancelReservation.setSelected(true);
		}
		else
		{
		    chkCancelReservation.setSelected(false);
		}
		String reservationType = resultSet.getString(19);
		cmbReservationType.setSelectedItem(reservationType);
		
		String sql = ("select strPosName from tblposmaster where strPosCode='" + pcode + "'");
		ResultSet rsPOSCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsPOSCode.next();
		String posName = rsPOSCode.getString(1);
		cmbPOS.setSelectedItem(posName);

		//cmbSmoking.setSelectedItem(resultSet.getString(12));
		//txtSpecialInformation1.setText(resultSet.getString(13));
		dteReservationDate.setDate(resultSet.getDate(9));
		reservationTime = resultSet.getString(10);

		//hhmmssTimeFormate = new SimpleDateFormat("HH:mm:ss");
		Date d = hhmmssTimeFormate.parse(reservationTime);

		SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
		String tdate = sf.format(d);
		Date d2 = sf.parse(tdate);

		//String s=d.
		String[] time = tdate.split(":");
		cmbHour.setSelectedItem(time[0]);
		cmbMinutes.setSelectedItem(time[1]);//00 AM
		cmbAMPM.setSelectedItem(time[2].substring(3, 5));

		//cmbSeconds.setSelectedItem(time[2]);
//                String ampPm = resultSet.getString((16));
//                if (ampPm.equalsIgnoreCase("AM"))
//                {
//                    cmbAMPM.setSelectedIndex(0);
//                }
//                else
//                {
//                    cmbAMPM.setSelectedIndex(1);
//                }
		String smoking = resultSet.getString(12);
		if (smoking.equalsIgnoreCase("Y") || smoking.equalsIgnoreCase("YES"))
		{
		    cmbSmoking.setSelectedItem("YES");
		}
		else
		{
		    cmbSmoking.setSelectedItem("NO");
		}
		txtPAX.setText(resultSet.getString(11));
		specialInformation = resultSet.getString(13);
		String specialInfo[] = specialInformation.split("!");
		if (specialInfo.length >= 3)
		{
		    txtSpecialInformation1.setText(specialInfo[0]);
		    
		}
		else
		{
		    txtSpecialInformation1.setText(specialInformation);
		}        

	    }
	    resultSet.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSelectContactNo()
    {
	objUtility.funCallForSearchForm("ContactNo");
	new frmSearchFormDialog(null, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetContactNo(data);
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funSetContactNo(Object[] data)
    {
	customerCode = data[0].toString();
	txtCustomerName.setText(data[1].toString());
	txtContactNo.setText(data[2].toString());

	funCheckCustomerExist(data[2].toString());
    }

    private void funSelectTableNo()
    {
	objUtility.funCallForSearchForm("TableReserveMaster");
	new frmSearchFormDialog(null, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    funSetTableNo(data);
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }

    private void funSetTableNo(Object[] data)
    {
	tableNo = data[0].toString();
	txtTableName.setText(data[1].toString());

    }

    private void funExecuteNLoadTable()
    {
	String fromDate = yyyyMMddDateFormate.format(dteFromDate.getDate());
	String toDate = yyyyMMddDateFormate.format(dteToDate.getDate());
	String ftime = cmbFromTimeHour.getSelectedItem().toString() + ":" + cmbFromTimeMinutes.getSelectedItem().toString() + ":00 " + cmbFromTimeAMPM.getSelectedItem().toString();
	Date d = null;
	//hhmmssTimeFormate = new SimpleDateFormat("HH:mm:ss");
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(ftime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String fromTime = hhmmssTimeFormate.format(d);

	String tTime = cmbToTimeHour.getSelectedItem().toString() + ":" + cmbToTimeMinutes.getSelectedItem().toString() + ":00 " + cmbToTimeAMPM.getSelectedItem().toString();;
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(tTime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String toTime = hhmmssTimeFormate.format(d);

	//  String fromAmPm = cmbFromTimeAMPM.getSelectedItem().toString();
	// String toAmPm = cmbToTimeAMPM.getSelectedItem().toString();
	dtmTableReservation.setRowCount(0);
	tblTableReservation.setRowHeight(25);
	
	try
	{
	    sqlQuery.setLength(0);
	   sqlQuery.append("SELECT ifnull(a.totalBookingPax,0),ifnull(b.totalCancelledPax,0),ifnull(c.totalSeatedPax,0), ifnull(d.totalNoShowPax,0) \n" 
		    + " FROM (SELECT sum(a.intPax) as totalBookingPax" 
		    + " FROM tblreservation a" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"'  AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"'"
		    + " and TIME_FORMAT(a.tmeResTime,'%T') between '"+fromTime+"' and '"+toTime+"') a, (" 
		    + " SELECT sum(a.intPax) as totalCancelledPax" 
		    + " FROM tblreservation a" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND a.strCancelReservation='Y'  AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"'"
		    + " and TIME_FORMAT(a.tmeResTime,'%T') between '"+fromTime+"' and '"+toTime+"') b, " 
		    + " (SELECT sum(a.intPax) as totalSeatedPax" 
		    + " FROM tblreservation a, tbltablemaster b" 
		    + " WHERE a.strTableNo=b.strTableNo AND DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND b.strStatus='Occupied' AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"'"
		    + " and TIME_FORMAT(a.tmeResTime,'%T') between '"+fromTime+"' and '"+toTime+"') c," 
		    + " (SELECT ifnull(SUM(a.intPax),0) AS totalNoShowPax" 
		    + " FROM tblreservation a" 
		    + " LEFT OUTER JOIN tblcustomermaster b ON a.strCustomerCode=b.strCustomerCode" 
		    + " LEFT OUTER JOIN tbltablemaster c ON a.strTableNo=c.strTableNo" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"' AND TIME_FORMAT(a.tmeResTime,'%T') <= '"+java.time.LocalTime.now()+"' AND a.strCancelReservation!='Y'"
		    + " and TIME_FORMAT(a.tmeResTime,'%T') between '"+fromTime+"' and '"+toTime+"') d");
	    ResultSet resultSet1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    while(resultSet1.next())
	    {
		txtTotalBooking.setText(resultSet1.getString(1));
		txtTotCancelled.setText(resultSet1.getString(2));
		txtTotSeated.setText(resultSet1.getString(3));
		txtTotalNoShow.setText(resultSet1.getString(4));
	    }	
	    
	    sqlQuery.setLength(0);
	    sqlQuery.append("select b.longMobileNo,b.strCustomerName,a.strSmoking,c.strTableName,a.intPax ,a.dteResDate,TIME_FORMAT(a.tmeResTime, '%r'),a.strSpecialInfo,c.strTableNo,a.strResCode "
		    + "from tblreservation a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode  "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo  "
		    + "where date(a.dteResDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "'"
		    + "and  TIME_FORMAT(a.tmeResTime,'%T') >= '" + fromTime + "'and TIME_FORMAT(a.tmeResTime,'%T') <= '" + toTime + "' "
		    + "");
	    //TIME_FORMAT(a.tmeResTime, '%T') >= '17:00:00' and TIME_FORMAT(a.tmeResTime, '%T')<= '18:00:00';
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());

	    while (resultSet.next())
	    {
		String status="";
		sqlQuery.setLength(0);
		sqlQuery.append("select 'No Show' from tblreservation a LEFT OUTER JOIN tbltablemaster b ON a.strTableNo=b.strTableNo AND b.strStatus='Reserve'\n" 
			+ "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation!='Y' "
			+ " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
			+ " AND TIME_FORMAT(a.tmeResTime,'%T') <= '"+java.time.LocalTime.now()+"'; ");
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		   
		
		sqlQuery.setLength(0);
		sqlQuery.append("select 'Cancelled' from tblreservation a\n" 
			    + "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation='Y'"
			    + " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ;");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		  
		
		sqlQuery.setLength(0);
		sqlQuery.append("select 'Seated' from tblreservation a,tbltablemaster b\n" 
			    + "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation!='Y'\n" 
			    + "and a.strTableNo=b.strTableNo and b.strStatus='Occupied'"
			    + " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ;");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		
		sqlQuery.setLength(0);
		sqlQuery.append("SELECT 'Billed'\n" 
			+ "FROM tblreservation a, tbltablemaster b  \n" 
			+ "WHERE a.strResCode='"+resultSet.getString(10)+"' and a.strTableNo=b.strTableNo AND b.strStatus='Billed' \n" 
			+ "AND DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		
		Object[] row =
		{
		    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8),status,false,resultSet.getString(9), resultSet.getString(10)
		};

		dtmTableReservation.addRow(row);
	    }
	    tblTableReservation.setModel(dtmTableReservation);
	    tblTableReservation.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTableReservation.getColumnModel().getColumn(0).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(1).setPreferredWidth(150);
	    tblTableReservation.getColumnModel().getColumn(2).setPreferredWidth(55);
	    tblTableReservation.getColumnModel().getColumn(3).setPreferredWidth(50);
	    tblTableReservation.getColumnModel().getColumn(4).setPreferredWidth(40);
	    tblTableReservation.getColumnModel().getColumn(5).setPreferredWidth(80);
	    tblTableReservation.getColumnModel().getColumn(6).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(7).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(8).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(9).setPreferredWidth(50);
	    tblTableReservation.getColumnModel().getColumn(10).setPreferredWidth(0);
	    tblTableReservation.getColumnModel().getColumn(11).setPreferredWidth(0);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funResetfields()
    {
	dteFromDate.setDate(date);
	dteToDate.setDate(date);
	dtmTableReservation.setRowCount(0);
	cmbFromTimeHour.setSelectedIndex(0);
	cmbFromTimeMinutes.setSelectedIndex(0);
	//cmbFromTimeSeconds.setSelectedIndex(0);
	cmbFromTimeAMPM.setSelectedIndex(0);
	cmbToTimeHour.setSelectedIndex(0);
	cmbToTimeMinutes.setSelectedIndex(0);
	//cmbToTimeSeconds.setSelectedIndex(0);
	cmbToTimeAMPM.setSelectedIndex(0);
	cmbPOS.setSelectedIndex(0);
	
	chkCancelReservation.setEnabled(false);
	chkCancelReservation.setSelected(false);
    }

    private void funExecuteDefault()
    {
	String fromDate = yyyyMMddDateFormate.format(dteFromDate.getDate());
	String toDate = yyyyMMddDateFormate.format(dteToDate.getDate());
	dtmTableReservation.setRowCount(0);
	tblTableReservation.setRowHeight(25);
	try
	{
	    sqlQuery.setLength(0);
	    sqlQuery.append("SELECT ifnull(a.totalBookingPax,0),ifnull(b.totalCancelledPax,0),ifnull(c.totalSeatedPax,0), ifnull(d.totalNoShowPax,0) " 
		    + " FROM (SELECT sum(a.intPax) as totalBookingPax" 
		    + " FROM tblreservation a" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"'  AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"') a, (" 
		    + " SELECT sum(a.intPax) as totalCancelledPax" 
		    + " FROM tblreservation a" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND a.strCancelReservation='Y'  AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"') b, " 
		    + " (SELECT sum(a.intPax) as totalSeatedPax" 
		    + " FROM tblreservation a, tbltablemaster b" 
		    + " WHERE a.strTableNo=b.strTableNo AND DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND b.strStatus='Occupied' AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"') c," 
		    + " (SELECT ifnull(SUM(a.intPax),0) AS totalNoShowPax" 
		    + " FROM tblreservation a" 
		    + " LEFT OUTER JOIN tblcustomermaster b ON a.strCustomerCode=b.strCustomerCode" 
		    + " LEFT OUTER JOIN tbltablemaster c ON a.strTableNo=c.strTableNo" 
		    + " WHERE DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' AND a.strPosCode='"+clsGlobalVarClass.gPOSCode+"' AND TIME_FORMAT(a.tmeResTime,'%T') <= '"+java.time.LocalTime.now()+"' AND a.strCancelReservation!='Y') d");
	    ResultSet resultSet1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    while(resultSet1.next())
	    {
		txtTotalBooking.setText(resultSet1.getString(1));
		txtTotCancelled.setText(resultSet1.getString(2));
		txtTotSeated.setText(resultSet1.getString(3));
		txtTotalNoShow.setText(resultSet1.getString(4));
	    }	
	     
	    sqlQuery.setLength(0);
	    sqlQuery.append("select b.longMobileNo,b.strCustomerName,a.strSmoking,ifnull(c.strTableName,''),a.intPax "
		    + ",a.dteResDate,TIME_FORMAT(a.tmeResTime, '%r'),a.strSpecialInfo,ifnull(c.strTableNo,''),a.strResCode "
		    + "from tblreservation a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode  "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo  "
		    + "where date(a.dteResDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' ");
          
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());

	    while (resultSet.next())
	    {
		String status="";
		sqlQuery.setLength(0);
		sqlQuery.append("select 'No Show' from tblreservation a LEFT OUTER JOIN tbltablemaster b ON a.strTableNo=b.strTableNo AND b.strStatus='Reserve'\n" 
			+ "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation!='Y' "
			+ " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
			+ " AND TIME_FORMAT(a.tmeResTime,'%T') <= '"+java.time.LocalTime.now()+"'; ");
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		   
		
		sqlQuery.setLength(0);
		sqlQuery.append("select 'Cancelled' from tblreservation a\n" 
			    + "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation='Y'"
			    + " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ;");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		  
		
		sqlQuery.setLength(0);
		sqlQuery.append("select 'Seated' from tblreservation a,tbltablemaster b\n" 
			    + "where a.strResCode='"+resultSet.getString(10)+"' and a.strCancelReservation!='Y'\n" 
			    + "and a.strTableNo=b.strTableNo and b.strStatus='Occupied'"
			    + " and DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ;");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		  
		sqlQuery.setLength(0);
		sqlQuery.append("SELECT 'Billed'\n" 
			+ "FROM tblreservation a, tbltablemaster b  \n" 
			+ "WHERE a.strResCode='"+resultSet.getString(10)+"' and a.strTableNo=b.strTableNo AND b.strStatus='Billed' \n" 
			+ "AND DATE(a.dteResDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' ");
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
		if(rs.next())
		{
		    status = rs.getString(1);
		}
		
		Object[] row =
		{
		    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8),status,false, resultSet.getString(9), resultSet.getString(10)
		};

		dtmTableReservation.addRow(row);
	    }
	    tblTableReservation.setModel(dtmTableReservation);
	    tblTableReservation.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTableReservation.getColumnModel().getColumn(0).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(1).setPreferredWidth(150);
	    tblTableReservation.getColumnModel().getColumn(2).setPreferredWidth(55);
	    tblTableReservation.getColumnModel().getColumn(3).setPreferredWidth(50);
	    tblTableReservation.getColumnModel().getColumn(4).setPreferredWidth(40);
	    tblTableReservation.getColumnModel().getColumn(5).setPreferredWidth(80);
	    tblTableReservation.getColumnModel().getColumn(6).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(7).setPreferredWidth(200);
	    tblTableReservation.getColumnModel().getColumn(8).setPreferredWidth(100);
	    tblTableReservation.getColumnModel().getColumn(9).setPreferredWidth(50);
	    tblTableReservation.getColumnModel().getColumn(10).setPreferredWidth(0);
	    tblTableReservation.getColumnModel().getColumn(10).setPreferredWidth(0);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetShortcutKeys()
    {
	btnExecute.setMnemonic('e');
	btnClear.setMnemonic('l');
	btnClose1.setMnemonic('c');
	btnClose2.setMnemonic('x');
	btnSave.setMnemonic('s');
	btnReset.setMnemonic('r');
	//btnReset2.setMnemonic('');        
    }

    private void funCancelReservation()
    {
	try
	{
	    DefaultTableModel dtm = (DefaultTableModel) tblTableReservation.getModel();
	
	    for(int i=0;i<dtm.getRowCount();i++)
	    {
		String reservationNo=dtm.getValueAt(i, 11).toString();
		boolean flgSelect=Boolean.parseBoolean(dtm.getValueAt(i, 9).toString());
		if(flgSelect)
		{
		    clsGlobalVarClass.dbMysql.execute("update tblreservation  set strCancelReservation='Y' where strResCode='" + reservationNo + "' ");
		    sqlQuery.setLength(0);

		    if (dtm.getValueAt(i, 10) != null)
		    {
			String tableNo = dtm.getValueAt(i, 10).toString();
			    sqlQuery.append("update tbltablemaster set strStatus='Normal' "
				+ " where strTableNo='" + tableNo + "' "
				+ " and strStatus='Reserve' ");
			clsGlobalVarClass.dbMysql.execute(sqlQuery.toString());
		    }
		    
		}
	    }
	    funExecuteNLoadTable();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    public void funReservationHistory(String custMobileNo)
    {
	try
	{    
	DefaultTableModel dmTblReservationHistory = new DefaultTableModel();
	dmTblReservationHistory.setRowCount(0);
	dmTblReservationHistory.addColumn("Customer Name");
	dmTblReservationHistory.addColumn("Reserv Date");
	dmTblReservationHistory.addColumn("Reserv Time");
	dmTblReservationHistory.addColumn("Special Info");
	dmTblReservationHistory.addColumn("Reserv Type");
	dmTblReservationHistory.addColumn("Table No");
	dmTblReservationHistory.addColumn("Smoking");
	tblReservationHistory.setRowHeight(25);
	StringBuilder sb = new StringBuilder();
	sb.setLength(0);
	sb.append("select b.strCustomerName,DATE_FORMAT(a.dteResDate,'%d-%m-%Y'),concat(TIME_FORMAT(a.tmeResTime,'%h:%i:%s'),' ',a.strAMPM),a.strSpecialInfo,a.strReservationType,a.strTableNo,a.strSmoking "
		+ "from tblreservation a,tblcustomermaster b "
                + "where b.longMobileNo='"+custMobileNo+"' and a.strCustomerCode=b.strCustomerCode");
	ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
	while(resultSet.next())
	{
	   Object[] row =
		{
		    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)
		};

		dmTblReservationHistory.addRow(row); 
	} 
	tblReservationHistory.setModel(dmTblReservationHistory);
	tblReservationHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	tblReservationHistory.getColumnModel().getColumn(0).setPreferredWidth(200);
	tblReservationHistory.getColumnModel().getColumn(1).setPreferredWidth(80);
	tblReservationHistory.getColumnModel().getColumn(2).setPreferredWidth(100);
	tblReservationHistory.getColumnModel().getColumn(3).setPreferredWidth(200);
	tblReservationHistory.getColumnModel().getColumn(4).setPreferredWidth(100);
	tblReservationHistory.getColumnModel().getColumn(4).setPreferredWidth(60);
	tblReservationHistory.getColumnModel().getColumn(4).setPreferredWidth(80);
	
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}    
    }
    
    
    private void funExportFile(JTable tblReservationHistory, File file)
    {
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
	    TableModel model = tblReservationHistory.getModel();
	    sheet1.addCell(new Label(0, 0, exportFormName));

	    for (int i = 0; i < model.getColumnCount(); i++)
	    {
		Label column = new Label(i, 1, model.getColumnName(i));
		int colLen = Integer.parseInt(vTableReservationExcelColLength.elementAt(i).toString().split("#")[0]);
		sheet1.setColumnView(i, model.getColumnName(i).toString().length() + colLen);
		sheet1.addCell(column);
	    }
	    int i = 0, j = 0;
	    int k = 0;

	    for (i = 3; i < model.getRowCount() + 3; i++)
	    {
		for (j = 0; j < model.getColumnCount(); j++)
		{
		    //System.out.println(model.getValueAt(k, j).toString()+"\tcol="+j);
		    int colLen = Integer.parseInt(vTableReservationExcelColLength.elementAt(j).toString().split("#")[0]);
		    Label row = new Label(j, i + 1, model.getValueAt(k, j).toString());
		    sheet1.setColumnView(j, model.getValueAt(k, j).toString().length() + colLen);
		    sheet1.addCell(row);
		}
		k++;
	    }
	    funAddLastOfExportReport(workbook1);
	    workbook1.write();
	    workbook1.close();

	    String fileName = ExportReportPath + File.separator + exportFormName + objUtility.funGetDateInString() + ".xls";
	    //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + fileName);

	    //sendMail();
//            clsExportDocument exportDocument = new clsExportDocument();
//            exportDocument.funExportToPDF(tblDayWiseSalesSummary, tblTotal, "Day Wise Sales Summary");
	    /**
	     * to open file
	     */
	    Desktop desktop = Desktop.getDesktop();
	    desktop.open(file);

	}
	catch (FileNotFoundException ex)
	{
	    JOptionPane.showMessageDialog(this, "File Not Found Invalid File Path!!!");
	    ex.printStackTrace();

	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    
    private void funAddLastOfExportReport(WritableWorkbook workbook1)
    {
	try
	{
//	    int i = 0, j = 0, LastIndexReport = 0;
//	    if (exportFormName.equals("SalesSummary"))
//	    {
//		LastIndexReport = 5;
//	    }

	    WritableSheet sheet2 = workbook1.getSheet(0);
	    int r = sheet2.getRows();
	    System.out.println(r);
	    
	    WritableSheet sheet3 = workbook1.getSheet(0);
	    r = sheet3.getRows();
	    Formatter fmt = new Formatter();
	    Calendar cal = Calendar.getInstance();
	    fmt.format("%tr", cal);
	    Label row = new Label(1, r + 1, " Created On : " + objUtility.funGetDateInString() + " At : " + fmt + " By : " + clsGlobalVarClass.gUserCode + " ");
	    sheet2.addCell(row);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funExportClick()
    {

        try
        {
           
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            CellStyle style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
	    //"", "","","","", "", "","","","Reservation Code"
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Contact No");
            rowhead.getCell(0).setCellStyle(style);
            rowhead.createCell((short) 1).setCellValue("Customer Name");
            rowhead.getCell(1).setCellStyle(style);
            rowhead.createCell((short) 2).setCellValue("Smoking");
            rowhead.getCell(2).setCellStyle(style);
            rowhead.createCell((short) 3).setCellValue("PAX");
            rowhead.getCell(3).setCellStyle(style);
            rowhead.createCell((short) 4).setCellValue("Date");
            rowhead.getCell(4).setCellStyle(style);
            rowhead.createCell((short) 5).setCellValue("Time");
            rowhead.getCell(5).setCellStyle(style);
            rowhead.createCell((short) 6).setCellValue("SpecialInfo");
            rowhead.getCell(6).setCellStyle(style);
            rowhead.createCell((short) 7).setCellValue("TableNo");
            rowhead.getCell(7).setCellStyle(style);
            rowhead.createCell((short) 8).setCellValue("Reservation Code");
            rowhead.getCell(8).setCellStyle(style);
	    
	    String fromDate = yyyyMMddDateFormate.format(dteFromDate.getDate());
	    String toDate = yyyyMMddDateFormate.format(dteToDate.getDate());
	    String ftime = cmbFromTimeHour.getSelectedItem().toString() + ":" + cmbFromTimeMinutes.getSelectedItem().toString() + ":00 " + cmbFromTimeAMPM.getSelectedItem().toString();
	    Date d = null;
	//hhmmssTimeFormate = new SimpleDateFormat("HH:mm:ss");
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(ftime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String fromTime = hhmmssTimeFormate.format(d);

	String tTime = cmbToTimeHour.getSelectedItem().toString() + ":" + cmbToTimeMinutes.getSelectedItem().toString() + ":00 " + cmbToTimeAMPM.getSelectedItem().toString();;
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(tTime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String toTime = hhmmssTimeFormate.format(d);

	//  String fromAmPm = cmbFromTimeAMPM.getSelectedItem().toString();
	// String toAmPm = cmbToTimeAMPM.getSelectedItem().toString();
	dtmTableReservation.setRowCount(0);
	tblTableReservation.setRowHeight(25);

	
	    sqlQuery.setLength(0);
	    sqlQuery.append("select b.longMobileNo,b.strCustomerName,a.strSmoking,c.strTableName,a.intPax ,a.dteResDate,TIME_FORMAT(a.tmeResTime, '%r'),a.strSpecialInfo,c.strTableNo,a.strResCode "
		    + "from tblreservation a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode  "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo  "
		    + "where date(a.dteResDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "'"
		    + "and  TIME_FORMAT(a.tmeResTime,'%T') >= '" + fromTime + "'and TIME_FORMAT(a.tmeResTime,'%T') <= '" + toTime + "' ");
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    int i=1;
	    while (resultSet.next())
	    {
		    HSSFRow row = sheet.createRow(i);
		    row.createCell((short) 0).setCellValue( resultSet.getString(1));
		    row.createCell((short) 1).setCellValue(resultSet.getString(2));
		    row.createCell((short) 2).setCellValue(resultSet.getString(3));
		    row.createCell((short) 3).setCellValue(resultSet.getString(5));
		    row.createCell((short) 4).setCellValue(resultSet.getString(6));
		    row.createCell((short) 5).setCellValue(resultSet.getString(7));
		    row.createCell((short) 6).setCellValue(resultSet.getString(8));
		    row.createCell((short) 7).setCellValue(resultSet.getString(4));
		    row.createCell((short) 8).setCellValue(resultSet.getString(10));
		    
		
		i++;

	    }
	 	    
            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/TableReservation.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            hwb.write(fileOut);
            fileOut.close();
            JOptionPane.showMessageDialog(this, "File Created Successfully \n" + filePath + " : " + "TableReservation.xls");
            funResetFields();
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/MenuItemPricing.xls");

            Desktop dt = Desktop.getDesktop();
            dt.open(file);

        }
        catch (FileNotFoundException ex)
        {
            //JOptionPane.showMessageDialog(this, "File is already opened please close ");

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
	
	private void funExportHistoryClick()
	{

        try
        {
         
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            CellStyle style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
	    //"", "","","","", "", "","","","Reservation Code"
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Customer Name");
            rowhead.getCell(0).setCellStyle(style);
            rowhead.createCell((short) 1).setCellValue("Reserv Date");
            rowhead.getCell(1).setCellStyle(style);
            rowhead.createCell((short) 2).setCellValue("Reserv Time");
            rowhead.getCell(2).setCellStyle(style);
            rowhead.createCell((short) 3).setCellValue("Special Info");
            rowhead.getCell(3).setCellStyle(style);
            rowhead.createCell((short) 4).setCellValue("Reserv Type");
            rowhead.getCell(4).setCellStyle(style);
            rowhead.createCell((short) 5).setCellValue("Table No");
            rowhead.getCell(5).setCellStyle(style);
            rowhead.createCell((short) 6).setCellValue("Smoking");
            rowhead.getCell(6).setCellStyle(style);
            
	    String fromDate = yyyyMMddDateFormate.format(dteFromDate.getDate());
	    String toDate = yyyyMMddDateFormate.format(dteToDate.getDate());
	    String ftime = cmbFromTimeHour.getSelectedItem().toString() + ":" + cmbFromTimeMinutes.getSelectedItem().toString() + ":00 " + cmbFromTimeAMPM.getSelectedItem().toString();
	    Date d = null;
	//hhmmssTimeFormate = new SimpleDateFormat("HH:mm:ss");
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(ftime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String fromTime = hhmmssTimeFormate.format(d);

	String tTime = cmbToTimeHour.getSelectedItem().toString() + ":" + cmbToTimeMinutes.getSelectedItem().toString() + ":00 " + cmbToTimeAMPM.getSelectedItem().toString();;
	try
	{
	    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss aa");
	    d = sf.parse(tTime);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	String toTime = hhmmssTimeFormate.format(d);

	//  String fromAmPm = cmbFromTimeAMPM.getSelectedItem().toString();
	// String toAmPm = cmbToTimeAMPM.getSelectedItem().toString();
	dtmTableReservation.setRowCount(0);
	tblTableReservation.setRowHeight(25);

	String custMobileNo = txtContactNo.getText();
	    sqlQuery.setLength(0);
	    sqlQuery.append("select b.strCustomerName,DATE_FORMAT(a.dteResDate,'%d-%m-%Y'),concat(TIME_FORMAT(a.tmeResTime,'%h:%i:%s'),' ',a.strAMPM),a.strSpecialInfo,a.strReservationType,a.strTableNo,a.strSmoking "
		+ "from tblreservation a,tblcustomermaster b "
                + "where b.longMobileNo='"+custMobileNo+"' and a.strCustomerCode=b.strCustomerCode");
	    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery.toString());
	    int i=1;
	    while (resultSet.next())
	    {
		    HSSFRow row = sheet.createRow(i);
		    row.createCell((short) 0).setCellValue( resultSet.getString(1));
		    row.createCell((short) 1).setCellValue(resultSet.getString(2));
		    row.createCell((short) 2).setCellValue(resultSet.getString(3));
		    row.createCell((short) 3).setCellValue(resultSet.getString(4));
		    row.createCell((short) 4).setCellValue(resultSet.getString(5));
		    row.createCell((short) 5).setCellValue(resultSet.getString(6));
		    row.createCell((short) 6).setCellValue(resultSet.getString(7));
		    i++;

	    }
	 	    
            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/TableReservationHistory.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            hwb.write(fileOut);
            fileOut.close();
            JOptionPane.showMessageDialog(this, "File Created Successfully \n" + filePath + " : " + "TableReservationHistory.xls");
            funResetFields();
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/MenuItemPricing.xls");

            Desktop dt = Desktop.getDesktop();
            dt.open(file);

        }
        catch (FileNotFoundException ex)
        {
            //JOptionPane.showMessageDialog(this, "File is already opened please close ");

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    public void funSendTableReservationSMS(String reservationNo, String smsData, String transType)
    {
	try
	{
	    //String smsData=clsGlobalVarClass.gBillSettlementSMS;
	    String result = "", result1 = "", result2 = "", result3 = "", result4 = "", result5 = "", result6 = "", result7 = "";
	    String sql = "";

	    if (transType.equalsIgnoreCase("Table Reservation"))
	    {
		sql = "SELECT CONCAT(TIME_FORMAT(a.tmeResTime,'%h:%i:%s'),' ',a.strAMPM),a.intPax,DATE_FORMAT(a.dteResDate,'%d-%m-%Y'),ifnull(d.strAreaName,''),b.longMobileNo,ifnull(a.strTableNo,'')" 
		    + " FROM tblreservation a left outer join tbltablemaster c on a.strTableNo=c.strTableNo " 
		    + " left outer join tblareamaster d on c.strAreaCode=d.strAreaCode," 
		    + " tblcustomermaster b" 
		    + " WHERE a.strCustomerCode=b.strCustomerCode AND a.strResCode='"+reservationNo+"';";
	    }
	    
	    //System.out.println(sql);
	    ResultSet rs_SqlGetSMSData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs_SqlGetSMSData.next())
	    {
		int intIndex = smsData.indexOf("%%RESERVATION TIME");
		if (intIndex != - 1)
		{
		    result = smsData.replaceAll("%%RESERVATION TIME", rs_SqlGetSMSData.getString(1));
		    smsData = result;
		}
		int intIndex1 = smsData.indexOf("%%PAX NO");

		if (intIndex1 != - 1)
		{
		    result1 = smsData.replaceAll("%%PAX NO", rs_SqlGetSMSData.getString(2));
		    smsData = result1;
		}
		int intIndex2 = smsData.indexOf("%%RESERVATION DATE");

		if (intIndex2 != - 1)
		{
		    result2 = smsData.replaceAll("%%RESERVATION DATE", rs_SqlGetSMSData.getString(3));
		    smsData = result2;
		}
		int intIndex3 = smsData.indexOf("%%AREA NAME");

		if (intIndex3 != - 1)
		{
		    result3 = smsData.replaceAll("%%AREA NAME", rs_SqlGetSMSData.getString(4));
		    smsData = result3;
		}
		
		ArrayList<String> mobileNoList = new ArrayList<>();
		mobileNoList.add(rs_SqlGetSMSData.getString(5));
		clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsData);
		objSMSSender.start();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
}
