/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmMenuItemPricing extends javax.swing.JFrame
{

    private String[] itemNames, itemCodes;
    private String insertQuery, updateQuery, sql, oldAreaCode;
    private boolean flag, multiSearch;
    private String time, date;

    private String popularItem;
    private boolean flagfrmItemMaster;
    private HashMap<String, String> mapArea, mapPOS, mapCostCenter, mapMenuHead;
    private ArrayList<String> listSubMenuCode;
    private ArrayList<String> listSubMenuName;
    private String oldPOSCode;
    clsUtility objUtility = new clsUtility();
    private String longPrincingId;
    private Map<String, String> mapPOSCodeWithName;
    private Map<String, String> mapAreaCodeWithName;
    private Map<String, String> mapCostCenterCodeWithName;
    private Map<String, String> mapMenuHeadCodeWithName;
    private Map<String, String> mapSubMenuHeadCodeWithName;
    private DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    /**
     * This method is used to initialize frmMenuItemPricing
     */
    public frmMenuItemPricing()
    {
	initComponents();
	try
	{
	    Timer timer = new Timer(500, new ActionListener()
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

	    flagfrmItemMaster = false;
	    multiSearch = false;
	    if (clsGlobalVarClass.gPriceItem)
	    {
		flag = true;
		txtItemCode.setText(clsGlobalVarClass.gItemCodeforPricing);
		String sql = "select strItemName from tblitemmaster where strItemCode='" + clsGlobalVarClass.gItemCodeforPricing + "'";
		ResultSet rsCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsCode.next();
		txtItemName.setText(rsCode.getString(1));
	    }
	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    int year1 = dt1.getYear() + 1901;
	    year1 += 99;
	    String dte = day + "-" + month + "-" + year;
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
	    String dte1 = day + "-" + month + "-" + year1;
	    java.util.Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dte1);
	    java.util.Date posDate = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	    dteFromDate.setDate(posDate);
	    dteToDate.setDate(date1);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    int itemIndex = 0;

	    funFillPOS();
	    funFillArea();
	    funFillCostCenter();
	    funFillMenuHead();
	    txtItemCode.requestFocus();

	    sql = "select count(strItemCode) from tblitemmaster";
	    ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsItemMaster.next();
	    int cnt = rsItemMaster.getInt(1);
	    itemCodes = new String[cnt];
	    itemNames = new String[cnt];
	    sql = "select strItemCode,strItemName from tblitemmaster";
	    rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemMaster.next())
	    {
		itemCodes[itemIndex] = rsItemMaster.getString(1);
		itemNames[itemIndex] = rsItemMaster.getString(2);
		itemIndex++;
	    }
	    rsItemMaster.close();

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
	btnCancel.setMnemonic('c');
	btnNew.setMnemonic('s');
	btnReset.setMnemonic('r');

    }

    /**
     * This method is used to tickTock data
     */
    private void tickTock()
    {
	Date date1 = new Date();
	String newstr = String.format("%tr", date1);
	String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
	lblDate.setText(dateAndTime);
    }

    /**
     * This method is used to fill pos
     */
    private void funFillPOS()
    {
	try
	{
	    cmbPOSNames.removeAllItems();
	    mapPOS = new HashMap<String, String>();
	    mapPOSCodeWithName = new HashMap<String, String>();

	    cmbPOSNames.addItem("All");
	    mapPOS.put("All", "All");
	    mapPOSCodeWithName.put("All", "All");

	    sql = "select strPosCode,strPosName from tblposmaster";
	    ResultSet rsPOSMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsPOSMaster.next())
	    {
		cmbPOSNames.addItem(rsPOSMaster.getString(2));
		mapPOS.put(rsPOSMaster.getString(2), rsPOSMaster.getString(1));
		mapPOSCodeWithName.put(rsPOSMaster.getString(1), rsPOSMaster.getString(2));
	    }
	    rsPOSMaster.close();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to fill menu head
     */
    private void funFillMenuHead()
    {
	try
	{
	    cmbMenuNames.removeAllItems();
	    mapMenuHead = new HashMap<String, String>();
	    mapMenuHeadCodeWithName = new HashMap<String, String>();

	    cmbMenuNames.addItem(" ");
	    mapMenuHead.put(" ", " ");
	    mapMenuHeadCodeWithName.put(" ", " ");

	    sql = "select strMenuCode,strMenuName from tblmenuhd  order by strMenuName asc";
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuHead.next())
	    {
		cmbMenuNames.addItem(rsMenuHead.getString(2));
		mapMenuHead.put(rsMenuHead.getString(2), rsMenuHead.getString(1));

		mapMenuHeadCodeWithName.put(rsMenuHead.getString(1), rsMenuHead.getString(2));
	    }
	    rsMenuHead.close();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to fill cost center
     */
    private void funFillCostCenter()
    {
	try
	{
	    cmbCostCenter.removeAllItems();
	    mapCostCenter = new HashMap<String, String>();
	    mapCostCenterCodeWithName = new HashMap<String, String>();

	    cmbCostCenter.addItem("All");
	    mapCostCenter.put("All", "All");
	    mapCostCenterCodeWithName.put("All", "All");

	    sql = "select strCostCenterCode,strCostCenterName from tblcostcentermaster";
	    ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCostCenter.next())
	    {
		cmbCostCenter.addItem(rsCostCenter.getString(2));
		mapCostCenter.put(rsCostCenter.getString(2), rsCostCenter.getString(1));
		mapCostCenterCodeWithName.put(rsCostCenter.getString(1), rsCostCenter.getString(2));
	    }
	    rsCostCenter.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to fill area
     */
    private void funFillArea()
    {
	try
	{
	    cmbArea.removeAllItems();
	    mapArea = new HashMap<String, String>();
	    mapAreaCodeWithName = new HashMap<String, String>();

	    sql = "select * from tblareamaster where strAreaName ='All' ";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		mapArea.put(rsArea.getString(2), rsArea.getString(1));
		cmbArea.addItem(rsArea.getString(2));
		mapAreaCodeWithName.put(rsArea.getString(1), rsArea.getString(2));
	    }
	    rsArea.close();

	    sql = "select * from tblareamaster where strAreaName !='All' ";
	    rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsArea.next())
	    {
		mapArea.put(rsArea.getString(2), rsArea.getString(1));
		cmbArea.addItem(rsArea.getString(2));
		mapAreaCodeWithName.put(rsArea.getString(1), rsArea.getString(2));
	    }
	    rsArea.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to set item for price
     *
     * @param data
     */
    private void funSetItemForPrice(Object[] data)
    {
	funResetField();
	flag = true;
	txtItemCode.setText(data[0].toString());
	txtItemName.setText(data[1].toString());
	cmbMenuNames.requestFocus();
    }

    /**
     * This method is used to set item price info
     *
     * @param data
     */
    private void funSetItemPriceInfo(Object[] data)
    {
	try
	{
	    String areaName = "", hourlyPrice = "", sql = "", areaCode = "", pricingId = "";
	    String posCode = mapPOS.get(data[2].toString());

	    oldPOSCode = posCode;

	    if (multiSearch)
	    {
		hourlyPrice = data[6].toString();
		pricingId = data[7].toString();
	    }
	    else
	    {
		hourlyPrice = data[7].toString();
		pricingId = data[8].toString();
	    }

	    if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
	    {
		areaName = data[6].toString();
		hourlyPrice = data[7].toString();
		pricingId = data[8].toString();

		areaCode = mapArea.get(areaName);
		oldAreaCode = areaCode;
		//System.out.println("Area Code="+oldAreaCode);
		sql = "select * from tblmenuitempricingdtl "
			+ " where strItemCode='" + data[0].toString() + "' "
			+ " and strAreaCode='" + oldAreaCode + "'  and ( strPOSCode='" + posCode + "' or strPOSCode='All') "
			+ " and strHourlyPricing='" + hourlyPrice + "' and longPricingId='" + pricingId + "' ";
	    }
	    else
	    {

		areaName = data[6].toString();
		areaCode = mapArea.get(areaName);
		oldAreaCode = areaCode;
		sql = "select * from tblmenuitempricingdtl "
			+ " where strItemCode='" + data[0].toString() + "' "
			+ " and strHourlyPricing='" + hourlyPrice + "' "
			+ " and (strPOSCode='" + posCode + "' or strPOSCode='All') "
			+ " and longPricingId='" + pricingId + "' ";
	    }
	    //System.out.println(sql);
	    ResultSet rsItemPrice = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemPrice.next())
	    {
		txtItemCode.setText(rsItemPrice.getString(1));
		txtItemName.setText(rsItemPrice.getString(2));
		
		txtPriceMon.setText(gDecimalFormat.format(rsItemPrice.getDouble(6)));
		txtPriceTue.setText(gDecimalFormat.format(rsItemPrice.getDouble(7)));
		txtPriceWed.setText(gDecimalFormat.format(rsItemPrice.getDouble(8)));
		txtPriceThu.setText(gDecimalFormat.format(rsItemPrice.getDouble(9)));
		txtPriceFri.setText(gDecimalFormat.format(rsItemPrice.getDouble(10)));
		txtPriceSat.setText(gDecimalFormat.format(rsItemPrice.getDouble(11)));
		txtPriceSun.setText(gDecimalFormat.format(rsItemPrice.getDouble(12)));

		cmbColor.setSelectedItem(rsItemPrice.getString(20));
		String costCenterCode = rsItemPrice.getString(19);
		String menuHeadCode = rsItemPrice.getString(4);
		oldAreaCode = rsItemPrice.getString(25);
		String subMenuCode = rsItemPrice.getString(26).trim();

		StringBuilder sb = new StringBuilder(rsItemPrice.getString(13));
		int ind = sb.indexOf(" ");
		String dt = sb.substring(0, ind);
		String[] dts = new String[3];
		StringTokenizer stk = new StringTokenizer(dt, "-");
		int k = 0;
		while (stk.hasMoreTokens())
		{
		    dts[k] = stk.nextToken();
		    k++;
		}
		String date1 = dts[2] + "-" + dts[1] + "-" + dts[0];
		java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
		dteFromDate.setDate(date);

		sb = new StringBuilder(rsItemPrice.getString(14));
		ind = sb.indexOf(" ");
		dt = sb.substring(0, ind);
		dts = new String[3];
		stk = new StringTokenizer(dt, "-");
		k = 0;
		while (stk.hasMoreTokens())
		{
		    dts[k] = stk.nextToken();
		    k++;
		}
		String date2 = dts[2] + "-" + dts[1] + "-" + dts[0];
		date = new SimpleDateFormat("dd-MM-yyyy").parse(date2);
		dteToDate.setDate(date);
		if (rsItemPrice.getString(5).equals("Y"))
		{
		    chkPopular.setSelected(true);
		}

		posCode = rsItemPrice.getString(3);

		String posName = mapPOSCodeWithName.get(posCode);
		String menuHeadName = mapMenuHeadCodeWithName.get(menuHeadCode);
		String areaName2 = mapAreaCodeWithName.get(areaCode);
		String costCenterName = mapCostCenterCodeWithName.get(costCenterCode);

		cmbPOSNames.setSelectedItem(posName);
		cmbMenuNames.setSelectedItem(menuHeadName);
		cmbCostCenter.setSelectedItem(costCenterName);
		cmbArea.setSelectedItem(areaName2);

		funFillSubMenuCombo(menuHeadCode);
		int index = listSubMenuCode.indexOf(subMenuCode);
		if (index > 0)
		{
		    cmbSubMenuHead.setSelectedIndex(index);
		}
		else
		{
		    cmbSubMenuHead.setSelectedIndex(0);
		}

		String[] spFromTime = rsItemPrice.getString(15).split(":");
		String[] spToTime = rsItemPrice.getString(17).split(":");
		cmbFromHour.setSelectedItem(spFromTime[0]);
		cmbFromMinute.setSelectedItem(spFromTime[1]);
		cmbFromTimeSeconds.setSelectedItem(spFromTime[2]);
		cmbToHour.setSelectedItem(spToTime[0]);
		cmbToMinute.setSelectedItem(spToTime[1]);
		cmbToTimeSeconds.setSelectedItem(spToTime[2]);

		chkHourlyPricing.setSelected(false);
		if (rsItemPrice.getString(27).equalsIgnoreCase("Yes"))
		{
		    chkHourlyPricing.setSelected(true);
		}

		longPrincingId = rsItemPrice.getString(28);

		rsItemPrice.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
	try
	{
	    multiSearch = false;
	    btnNew.setMnemonic('s');
	    btnNew.setText("SAVE");
	    flag = true;
	    chkPopular.setSelected(false);
	    txtItemCode.setText("");
	    txtItemName.setText("");
	    txtPriceMon.setText(" ");
	    txtPriceTue.setText(" ");
	    txtPriceWed.setText(" ");
	    txtPriceThu.setText(" ");
	    txtPriceFri.setText(" ");
	    txtPriceSat.setText(" ");
	    txtPriceSun.setText(" ");

	    funFillPOS();
	    funFillMenuHead();
	    funFillCostCenter();
	    funFillArea();
	    if (cmbSubMenuHead.getItemCount() > 0)
	    {
		cmbSubMenuHead.setSelectedIndex(0);
	    }
	    cmbColor.setSelectedIndex(0);
	    clsGlobalVarClass.gPriceItem = false;
	    chkHourlyPricing.setSelected(false);
	    cmbFromHour.setSelectedItem("HH");
	    cmbToHour.setSelectedItem("HH");
	    cmbFromMinute.setSelectedItem("MM");
	    cmbToMinute.setSelectedItem("MM");
	    cmbFromTimeSeconds.setSelectedItem("S");
	    cmbToTimeSeconds.setSelectedItem("S");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to check negative values
     *
     * @return boolean
     */
    private boolean funCheckNegativeValue()
    {
	boolean flag = false;
	try
	{
	    if (Double.parseDouble(txtPriceSun.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceMon.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceTue.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceWed.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceThu.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceFri.getText()) < 0)
	    {
		flag = true;
	    }
	    else if (Double.parseDouble(txtPriceSat.getText()) < 0)
	    {
		flag = true;
	    }
	    else
	    {
		flag = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flag;
	}
    }

    /**
     * This method is used to get area names
     *
     * @param areacode
     */
    private void funGetAreaName(String areacode)
    {
	String areaName = "";
	try
	{
	    String sql = "select strAreaName from tblareamaster where strAreaCode='" + areacode + "'";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		areaName = rsArea.getString(1);
		cmbArea.setSelectedItem(areaName);
	    }
	    else if (areaName == "")
	    {
		cmbArea.setSelectedItem("All");
	    }
	    else
	    {
		cmbArea.setSelectedItem("All");
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to fill sub menu como box
     *
     * @param MenuCode
     * @return
     */
    private int funFillSubMenuCombo(String MenuCode)
    {
	String menuHeadCode = MenuCode;
	try
	{
	    listSubMenuCode = new ArrayList<String>();
	    listSubMenuName = new ArrayList<String>();
	    mapSubMenuHeadCodeWithName = new HashMap<String, String>();

	    cmbSubMenuHead.removeAllItems();
	    listSubMenuName.add("--SELECT--");
	    listSubMenuCode.add("--SELECT--");

	    mapSubMenuHeadCodeWithName.put("--SELECT--", "--SELECT--");

	    sql = "select strSubMenuHeadCode,strSubMenuHeadName from tblsubmenuhead  where "
		    + "strMenuCode='" + menuHeadCode + "'";
	    //System.out.println(sql);
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuHead.next())
	    {
		String code = rsMenuHead.getString(1);
		String name = rsMenuHead.getString(2);
		listSubMenuCode.add(code);
		listSubMenuName.add(name);

		mapSubMenuHeadCodeWithName.put(code, name);
	    }
	    rsMenuHead.close();
	    for (String subMenuName : listSubMenuName)
	    {
		cmbSubMenuHead.addItem(subMenuName);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return 0;
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

        panelheader = new javax.swing.JPanel();
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
        };  ;
        panelbody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblItemCode = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        btnHelp = new javax.swing.JButton();
        lblItemName = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        lblColor = new javax.swing.JLabel();
        cmbColor = new javax.swing.JComboBox();
        cmbSubMenuHead = new javax.swing.JComboBox();
        lblSubMenuHead = new javax.swing.JLabel();
        cmbMenuNames = new javax.swing.JComboBox();
        lblMenuHead = new javax.swing.JLabel();
        cmbPOSNames = new javax.swing.JComboBox();
        lblPosNameL = new javax.swing.JLabel();
        lblArea = new javax.swing.JLabel();
        cmbArea = new javax.swing.JComboBox();
        chkHourlyPricing = new javax.swing.JCheckBox();
        dteToDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblFromDate = new javax.swing.JLabel();
        lblTimeFrom = new javax.swing.JLabel();
        lblToTime = new javax.swing.JLabel();
        chkPopular = new javax.swing.JCheckBox();
        cmbCostCenter = new javax.swing.JComboBox();
        lblCostCenter = new javax.swing.JLabel();
        lblPriceSun = new javax.swing.JLabel();
        txtPriceSun = new javax.swing.JTextField();
        lblPriceMon = new javax.swing.JLabel();
        txtPriceMon = new javax.swing.JTextField();
        lblPriceTue = new javax.swing.JLabel();
        txtPriceTue = new javax.swing.JTextField();
        lblPriceWed = new javax.swing.JLabel();
        txtPriceWed = new javax.swing.JTextField();
        txtPriceSat = new javax.swing.JTextField();
        lblPriceSat = new javax.swing.JLabel();
        txtPriceFri = new javax.swing.JTextField();
        lblPriceFri = new javax.swing.JLabel();
        txtPriceThu = new javax.swing.JTextField();
        lblPriceThu = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        cmbFromHour = new javax.swing.JComboBox();
        cmbFromMinute = new javax.swing.JComboBox();
        cmbFromTimeSeconds = new javax.swing.JComboBox();
        cmbToHour = new javax.swing.JComboBox();
        cmbToMinute = new javax.swing.JComboBox();
        cmbToTimeSeconds = new javax.swing.JComboBox();

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

        panelheader.setBackground(new java.awt.Color(69, 164, 238));
        panelheader.setLayout(new javax.swing.BoxLayout(panelheader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelheader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelheader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Pricing Master");
        panelheader.add(lblformName);
        panelheader.add(filler4);
        panelheader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelheader.add(lblPosName);
        panelheader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelheader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        panelheader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelheader.add(lblHOSign);

        getContentPane().add(panelheader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelbody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(23, 16, 16));
        lblFormName.setText("Pricing Master");
        lblFormName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblItemCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemCode.setText("Item Code    :");

        txtItemCode.setEditable(false);
        txtItemCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemCodeMouseClicked(evt);
            }
        });
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemCodeKeyPressed(evt);
            }
        });

        btnHelp.setBackground(new java.awt.Color(51, 102, 255));
        btnHelp.setForeground(new java.awt.Color(255, 255, 255));
        btnHelp.setText("...");
        btnHelp.setToolTipText("Open Items");
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

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemName.setText("Item Name    :");

        txtItemName.setEditable(false);
        txtItemName.setBackground(new java.awt.Color(204, 204, 204));

        lblColor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblColor.setText("Color :");
        lblColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        cmbColor.setBackground(new java.awt.Color(51, 102, 255));
        cmbColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Green", "Red", "BLUE", "CYAN", "ORANGE", "PINK", "YELLOW", "WHITE" }));

        cmbSubMenuHead.setBackground(new java.awt.Color(51, 102, 255));
        cmbSubMenuHead.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSubMenuHeadKeyPressed(evt);
            }
        });

        lblSubMenuHead.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubMenuHead.setText("Sub Menu Head  :");

        cmbMenuNames.setBackground(new java.awt.Color(51, 102, 255));
        cmbMenuNames.setForeground(new java.awt.Color(255, 255, 255));
        cmbMenuNames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cmbMenuNames.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbMenuNamesMouseClicked(evt);
            }
        });
        cmbMenuNames.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMenuNamesActionPerformed(evt);
            }
        });
        cmbMenuNames.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbMenuNamesKeyPressed(evt);
            }
        });

        lblMenuHead.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuHead.setText("Menu Head    :");

        cmbPOSNames.setBackground(new java.awt.Color(51, 102, 255));
        cmbPOSNames.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOSNames.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbPOSNamesMouseClicked(evt);
            }
        });
        cmbPOSNames.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPOSNamesKeyPressed(evt);
            }
        });

        lblPosNameL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPosNameL.setText("POS Name    :");

        lblArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblArea.setText("Area            :");

        cmbArea.setBackground(new java.awt.Color(51, 102, 255));
        cmbArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAreaKeyPressed(evt);
            }
        });

        chkHourlyPricing.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkHourlyPricing.setText("Hourly Pricing");

        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date        :");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date    :");

        lblTimeFrom.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTimeFrom.setText("Time From    :");

        lblToTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToTime.setText("To Time        :");

        chkPopular.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        chkPopular.setText("Popular");
        chkPopular.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkPopularKeyPressed(evt);
            }
        });

        cmbCostCenter.setBackground(new java.awt.Color(51, 102, 255));
        cmbCostCenter.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbCostCenterMouseClicked(evt);
            }
        });
        cmbCostCenter.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCostCenterKeyPressed(evt);
            }
        });

        lblCostCenter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCostCenter.setText("Cost Center  :");

        lblPriceSun.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceSun.setText("Sunday        :");

        txtPriceSun.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceSun.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtPriceSunFocusLost(evt);
            }
        });
        txtPriceSun.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceSunMouseClicked(evt);
            }
        });
        txtPriceSun.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceSunKeyPressed(evt);
            }
        });

        lblPriceMon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceMon.setText("Monday        :");

        txtPriceMon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceMon.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceMonMouseClicked(evt);
            }
        });
        txtPriceMon.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceMonKeyPressed(evt);
            }
        });

        lblPriceTue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceTue.setText("Tuesday          :");

        txtPriceTue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceTue.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceTueMouseClicked(evt);
            }
        });
        txtPriceTue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceTueKeyPressed(evt);
            }
        });

        lblPriceWed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceWed.setText("Wednesday          :");

        txtPriceWed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceWed.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceWedMouseClicked(evt);
            }
        });
        txtPriceWed.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceWedKeyPressed(evt);
            }
        });

        txtPriceSat.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceSat.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceSatMouseClicked(evt);
            }
        });
        txtPriceSat.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceSatKeyPressed(evt);
            }
        });

        lblPriceSat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceSat.setText("Saturday        :");

        txtPriceFri.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceFri.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceFriMouseClicked(evt);
            }
        });
        txtPriceFri.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceFriKeyPressed(evt);
            }
        });

        lblPriceFri.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceFri.setText("Friday           :");

        txtPriceThu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceThu.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPriceThuMouseClicked(evt);
            }
        });
        txtPriceThu.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPriceThuKeyPressed(evt);
            }
        });

        lblPriceThu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPriceThu.setText("Thrusday     :");

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setToolTipText("Close Item Pricing Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setLabel("CLOSE");
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

        btnReset.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
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

        btnNew.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Item Pricing");
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

        cmbFromHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbFromHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromHourKeyPressed(evt);
            }
        });

        cmbFromMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbFromMinute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromMinuteKeyPressed(evt);
            }
        });

        cmbFromTimeSeconds.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbFromTimeSeconds.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFromTimeSecondsActionPerformed(evt);
            }
        });
        cmbFromTimeSeconds.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbFromTimeSecondsKeyPressed(evt);
            }
        });

        cmbToHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbToHour.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToHourKeyPressed(evt);
            }
        });

        cmbToMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbToMinute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToMinuteKeyPressed(evt);
            }
        });

        cmbToTimeSeconds.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbToTimeSeconds.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbToTimeSecondsKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelbodyLayout.createSequentialGroup()
                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(lblArea, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(22, 22, 22)
                                    .addComponent(chkHourlyPricing, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(70, 70, 70)
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addGap(90, 90, 90)
                                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panelbodyLayout.createSequentialGroup()
                                                    .addComponent(cmbToHour, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(8, 8, 8)
                                                    .addComponent(cmbToMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(8, 8, 8)
                                                    .addComponent(cmbToTimeSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(lblPriceSun, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtPriceSun, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(lblPriceMon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceMon, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(lblPriceTue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceTue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(lblPriceWed, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceWed, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(lblPriceThu, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceThu, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(lblPriceFri, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceFri, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(lblPriceSat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtPriceSat, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addGap(100, 100, 100)
                                            .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(110, 110, 110)
                                    .addComponent(chkPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(btnHelp)
                                            .addGap(20, 20, 20))
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblTimeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(cmbFromHour, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(cmbFromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmbFromTimeSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(14, 14, 14)))
                                    .addComponent(lblToTime, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblPosNameL, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(cmbPOSNames, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(24, 24, 24)
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(cmbMenuNames, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(20, 20, 20)
                                            .addComponent(lblSubMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(cmbSubMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(10, 10, 10)
                                            .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(10, 10, 10)
                                            .addComponent(cmbColor, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(271, 271, 271))))
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbColor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPosNameL, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPOSNames, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMenuNames, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSubMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblArea, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkHourlyPricing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTimeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblToTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbFromHour, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbFromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbFromTimeSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbToMinute, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbToHour, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbToTimeSeconds))))
                .addGap(16, 16, 16)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPriceSun, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceSun, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceMon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceMon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceTue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceTue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceWed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceWed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPriceThu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceThu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceFri, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceFri, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceSat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceSat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void funMenuItemForPrice()
    {
	try
	{
	    flagfrmItemMaster = true;
	    multiSearch = true;
	    flag = true;
	    clsGlobalVarClass.gPOSCodeForPricing = mapPOS.get(cmbPOSNames.getSelectedItem().toString());
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("MenuItemForPrice");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetItemForPrice(data);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
	// TODO add your handling code here:
	funMenuItemForPrice();
    }//GEN-LAST:event_txtItemCodeMouseClicked

    public void funGetItemPriceHelp()
    {
	try
	{
	    if (!cmbPOSNames.getSelectedItem().toString().equals("All"))
	    {
		multiSearch = true;
	    }
	    if (!cmbMenuNames.getSelectedItem().toString().trim().isEmpty())
	    {
		multiSearch = true;
	    }
	    flag = false;
	    clsGlobalVarClass.gPOSCodeForPricing = mapPOS.get(cmbPOSNames.getSelectedItem().toString());
	    if (multiSearch)
	    {
		String posCode = mapPOS.get(cmbPOSNames.getSelectedItem().toString());
		String menuCode = funGetMenuCode(cmbMenuNames.getSelectedItem().toString());
		String costCenterCode = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());
		String selectedAreaCode = mapArea.get(cmbArea.getSelectedItem().toString());
		clsUtility obj = new clsUtility();
		obj.funCallForSearchForm("MultiPrice");

		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (cmbArea.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			clsGlobalVarClass.gQueryForSearch = "select a.strItemCode as ItemCode, a.strItemName as ItemName,b.strPosName as POSName,"
				+ "c.strMenuName as MenuName,a.strPopular as Popular,d.strCostCenterName as CostCenterName,"
				+ "IFNULL(e.strAreaName,'All') as Area,a.strHourlyPricing as HourlyPricing,longPricingId as ID "
				+ "from tblmenuitempricingdtl a left outer join tblareamaster e on a.strAreaCode=e.strAreaCode "
				+ "left outer join tblposmaster b on (a.strPOSCode=b.strPOSCode or a.strPosCode='All') "
				+ "left outer join tblmenuhd c on a.strMenuCode=c.strMenuCode "
				+ "left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode ";
		    }
		    else
		    {
			clsGlobalVarClass.gQueryForSearch = "select a.strItemCode as ItemCode, a.strItemName as ItemName,b.strPosName as POSName,"
				+ "c.strMenuName as MenuName,a.strPopular as Popular,d.strCostCenterName as CostCenterName,"
				+ "IFNULL(e.strAreaName,'All') as Area,a.strHourlyPricing as HourlyPricing,longPricingId as ID "
				+ "from tblmenuitempricingdtl a left outer join tblareamaster e on a.strAreaCode=e.strAreaCode "
				+ "left outer join tblposmaster b on (a.strPOSCode=b.strPOSCode or a.strPosCode='All') "
				+ "left outer join tblmenuhd c on a.strMenuCode=c.strMenuCode "
				+ "left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode ";
		    }
		}
		else
		{
		    clsGlobalVarClass.gQueryForSearch = "select a.strItemCode as ItemCode, a.strItemName as ItemName,"
			    + "b.strPosName as POSName,c.strMenuName as MenuName,a.strPopular as Popular,"
			    + "d.strCostCenterName as CostCenterName,a.strHourlyPricing as HourlyPricing,longPricingId as ID "
			    + "from tblmenuitempricingdtl a left outer join tblposmaster b on (a.strPOSCode=b.strPOSCode or a.strPosCode='All')"
			    + "left outer join tblmenuhd c on a.strMenuCode=c.strMenuCode "
			    + "left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode ";
		}
		if (!txtItemName.getText().trim().isEmpty())
		{
		    clsGlobalVarClass.gQueryForSearch += "and a.strItemName = '" + txtItemName.getText() + "' ";
		}
		if (!cmbPOSNames.getSelectedItem().toString().equals("All"))
		{
		    if (clsGlobalVarClass.gQueryForSearch.contains("where"))
		    {
			//clsGlobalVarClass.gQueryForSearch+="and b.strPosName = '"+cmbPOSNames.getSelectedItem().toString()+"' ";
			clsGlobalVarClass.gQueryForSearch += "and b.strPosCode = '" + posCode + "' ";
		    }
		    else
		    {
			//clsGlobalVarClass.gQueryForSearch+="where b.strPosName = '"+cmbPOSNames.getSelectedItem().toString()+"' ";
			clsGlobalVarClass.gQueryForSearch += "where b.strPosCode = '" + posCode + "' ";
		    }
		}
		if (!cmbMenuNames.getSelectedItem().toString().trim().isEmpty())
		{
		    if (clsGlobalVarClass.gQueryForSearch.contains("where"))
		    {
			//clsGlobalVarClass.gQueryForSearch+="and c.strMenuName = '"+cmbMenuNames.getSelectedItem().toString()+"' ";
			clsGlobalVarClass.gQueryForSearch += "and c.strMenuCode = '" + menuCode + "' ";
		    }
		    else
		    {
			//clsGlobalVarClass.gQueryForSearch+="where c.strMenuName = '"+cmbMenuNames.getSelectedItem().toString()+"' ";
			clsGlobalVarClass.gQueryForSearch += "where c.strMenuCode = '" + menuCode + "' ";
		    }
		}
		if (!cmbCostCenter.getSelectedItem().toString().trim().isEmpty())
		{
		    if (clsGlobalVarClass.gQueryForSearch.contains("where"))
		    {
			if (!cmbCostCenter.getSelectedItem().toString().trim().equals("All"))
			{
			    clsGlobalVarClass.gQueryForSearch += "and d.strCostCenterCode = '" + costCenterCode + "' ";
			}
		    }
		    else
		    {
			if (!cmbCostCenter.getSelectedItem().toString().trim().equals("All"))
			{
			    clsGlobalVarClass.gQueryForSearch += "where d.strCostCenterCode = '" + costCenterCode + "' ";
			}
		    }
		}

		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    if (!cmbArea.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			clsGlobalVarClass.gQueryForSearch += " and a.strAreaCode='" + selectedAreaCode + "' ";
		    }
		}

		clsGlobalVarClass.gQueryForSearch += "order by a.strItemName asc";
		//multiSearch=false;
	    }
	    else
	    {
		clsUtility obj = new clsUtility();
		obj.funCallForSearchForm("Price");
	    }
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		btnNew.setText("UPDATE");
		btnNew.setMnemonic('u');
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		if (data.length > 0)
		{
		    funSetItemPriceInfo(data);
		}
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
    private void btnHelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelpMouseClicked
	funGetItemPriceHelp();
    }//GEN-LAST:event_btnHelpMouseClicked

    private void cmbSubMenuHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubMenuHeadKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbArea.requestFocus();
	}
    }//GEN-LAST:event_cmbSubMenuHeadKeyPressed

    private void cmbMenuNamesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbMenuNamesMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbMenuNamesMouseClicked

    private void cmbMenuNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMenuNamesActionPerformed

	if (cmbMenuNames.getItemCount() > 0)
	{
	    if (cmbMenuNames.getSelectedItem().toString().trim().length() > 0)
	    {
		String menuHeadCode = funGetMenuCode(cmbMenuNames.getSelectedItem().toString());
		funFillSubMenuCombo(menuHeadCode);
	    }
	}
    }//GEN-LAST:event_cmbMenuNamesActionPerformed

    private void cmbMenuNamesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMenuNamesKeyPressed
	// TODO add your handling code here:
	multiSearch = true;
	if (evt.getKeyCode() == 10)
	{
	    cmbSubMenuHead.requestFocus();
	}
    }//GEN-LAST:event_cmbMenuNamesKeyPressed

    private void cmbPOSNamesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbPOSNamesMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbPOSNamesMouseClicked

    private void cmbPOSNamesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOSNamesKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbMenuNames.requestFocus();
	}
    }//GEN-LAST:event_cmbPOSNamesKeyPressed

    private void cmbAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAreaKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbCostCenter.requestFocus();
	}

    }//GEN-LAST:event_cmbAreaKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteToDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbFromHour.requestFocus();
	}
    }//GEN-LAST:event_dteToDateKeyPressed

    private void chkPopularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkPopularKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceSun.requestFocus();
	}
    }//GEN-LAST:event_chkPopularKeyPressed

    private void cmbCostCenterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCostCenterMouseClicked
	// TODO add your handling code here:
	multiSearch = true;
    }//GEN-LAST:event_cmbCostCenterMouseClicked

    private void cmbCostCenterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCostCenterKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkPopular.requestFocus();
	}
    }//GEN-LAST:event_cmbCostCenterKeyPressed

    private void txtPriceSunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPriceSunFocusLost
	// TODO add your handling code here:
	try
	{
	    txtPriceMon.setText(txtPriceSun.getText());
	    txtPriceTue.setText(txtPriceSun.getText());
	    txtPriceWed.setText(txtPriceSun.getText());
	    txtPriceThu.setText(txtPriceSun.getText());
	    txtPriceFri.setText(txtPriceSun.getText());
	    txtPriceSat.setText(txtPriceSun.getText());

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceSunFocusLost

    private void txtPriceSunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceSunMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceSun.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Sunday").setVisible(true);
		txtPriceSun.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceSun.getText(), "Double", "Enter Price For Sunday").setVisible(true);
		txtPriceSun.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceSunMouseClicked

    private void txtPriceSunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceSunKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceMon.requestFocus();
	}
    }//GEN-LAST:event_txtPriceSunKeyPressed

    private void txtPriceMonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceMonMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceMon.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Monday").setVisible(true);
		txtPriceMon.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceMon.getText(), "Double", "Enter Price For Monday").setVisible(true);
		txtPriceMon.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceMonMouseClicked

    private void txtPriceMonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceMonKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceTue.requestFocus();
	}
    }//GEN-LAST:event_txtPriceMonKeyPressed

    private void txtPriceTueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceTueMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceTue.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Tuesday").setVisible(true);
		txtPriceTue.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceTue.getText(), "Double", "Enter Price For Tuesday").setVisible(true);
		txtPriceTue.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceTueMouseClicked

    private void txtPriceTueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceTueKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceWed.requestFocus();
	}
    }//GEN-LAST:event_txtPriceTueKeyPressed

    private void txtPriceWedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceWedMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceWed.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Wednesday").setVisible(true);
		txtPriceWed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceWed.getText(), "Double", "Enter Price For Wednesday").setVisible(true);
		txtPriceWed.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceWedMouseClicked

    private void txtPriceWedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceWedKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceThu.requestFocus();
	}
    }//GEN-LAST:event_txtPriceWedKeyPressed

    private void txtPriceSatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceSatMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceSat.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Saturday").setVisible(true);
		txtPriceSat.setText(clsGlobalVarClass.gNumerickeyboardValue);

	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceSat.getText(), "Double", "Enter Price For Saturday").setVisible(true);
		txtPriceSat.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceSatMouseClicked

    private void txtPriceSatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceSatKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_txtPriceSatKeyPressed

    private void txtPriceFriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceFriMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceFri.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Friday").setVisible(true);
		txtPriceFri.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceFri.getText(), "Double", "Enter Price For Friday").setVisible(true);
		txtPriceFri.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceFriMouseClicked

    private void txtPriceFriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceFriKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceSat.requestFocus();
	}
    }//GEN-LAST:event_txtPriceFriKeyPressed

    private void txtPriceThuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPriceThuMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPriceThu.getText().length() == 0)
	    {
		new frmNumericKeyboard(this, true, "", "Double", "Enter Price For Thursday").setVisible(true);
		txtPriceThu.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPriceThu.getText(), "Double", "Enter Price For Thursday").setVisible(true);
		txtPriceThu.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPriceThuMouseClicked

    private void txtPriceThuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceThuKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPriceFri.requestFocus();
	}
    }//GEN-LAST:event_txtPriceThuKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Price Menu");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    public void funSaveAndUpdateOperations()
    {
	try
	{

	    String areaCode = "", hourlyPrice = "No";
	    if (mapArea.size() > 0)
	    {
		areaCode = mapArea.get(cmbArea.getSelectedItem().toString());
	    }
	    if (txtPriceSun.getText().trim().length() == 0 || txtPriceMon.getText().trim().length() == 0
		    || txtPriceTue.getText().trim().length() == 0 || txtPriceWed.getText().trim().length() == 0
		    || txtPriceThu.getText().trim().length() == 0 || txtPriceSat.getText().trim().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Please Enter All Day Price!");
		return;
	    }

	    String posCode = mapPOS.get(cmbPOSNames.getSelectedItem().toString());

	    if (flag)
	    {
		funInsertItemPrice(areaCode);
	    }
	    else
	    {
		funUpdateItemPrice(areaCode);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
	// TODO add your handling code here:
	funSaveAndUpdateOperations();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    funSaveAndUpdateOperations();
	}
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
	// TODO add your handling code here:
	funSaveAndUpdateOperations();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Price Menu");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnHelp.requestFocus();
	}
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funMenuItemForPrice();
	}

    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void btnHelpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelpMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnHelpMouseEntered

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Price Menu");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Price Menu");
    }//GEN-LAST:event_formWindowClosing

    private void cmbFromHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromHourKeyPressed
    {//GEN-HEADEREND:event_cmbFromHourKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbFromMinute.requestFocus();
	}
    }//GEN-LAST:event_cmbFromHourKeyPressed

    private void cmbFromMinuteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromMinuteKeyPressed
    {//GEN-HEADEREND:event_cmbFromMinuteKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbFromTimeSeconds.requestFocus();
	}
    }//GEN-LAST:event_cmbFromMinuteKeyPressed

    private void cmbFromTimeSecondsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFromTimeSecondsActionPerformed
    {//GEN-HEADEREND:event_cmbFromTimeSecondsActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbFromTimeSecondsActionPerformed

    private void cmbFromTimeSecondsKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbFromTimeSecondsKeyPressed
    {//GEN-HEADEREND:event_cmbFromTimeSecondsKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbToHour.requestFocus();
	}
    }//GEN-LAST:event_cmbFromTimeSecondsKeyPressed

    private void cmbToHourKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToHourKeyPressed
    {//GEN-HEADEREND:event_cmbToHourKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbToMinute.requestFocus();
	}
    }//GEN-LAST:event_cmbToHourKeyPressed

    private void cmbToMinuteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToMinuteKeyPressed
    {//GEN-HEADEREND:event_cmbToMinuteKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbToTimeSeconds.requestFocus();
	}
    }//GEN-LAST:event_cmbToMinuteKeyPressed

    private void cmbToTimeSecondsKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbToTimeSecondsKeyPressed
    {//GEN-HEADEREND:event_cmbToTimeSecondsKeyPressed

    }//GEN-LAST:event_cmbToTimeSecondsKeyPressed

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
	    java.util.logging.Logger.getLogger(frmMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmMenuItemPricing().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JCheckBox chkHourlyPricing;
    private javax.swing.JCheckBox chkPopular;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbColor;
    private javax.swing.JComboBox cmbCostCenter;
    private javax.swing.JComboBox cmbFromHour;
    private javax.swing.JComboBox cmbFromMinute;
    private javax.swing.JComboBox cmbFromTimeSeconds;
    private javax.swing.JComboBox cmbMenuNames;
    private javax.swing.JComboBox cmbPOSNames;
    private javax.swing.JComboBox cmbSubMenuHead;
    private javax.swing.JComboBox cmbToHour;
    private javax.swing.JComboBox cmbToMinute;
    private javax.swing.JComboBox cmbToTimeSeconds;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblCostCenter;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemCode;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblMenuHead;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPosNameL;
    private javax.swing.JLabel lblPriceFri;
    private javax.swing.JLabel lblPriceMon;
    private javax.swing.JLabel lblPriceSat;
    private javax.swing.JLabel lblPriceSun;
    private javax.swing.JLabel lblPriceThu;
    private javax.swing.JLabel lblPriceTue;
    private javax.swing.JLabel lblPriceWed;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSubMenuHead;
    private javax.swing.JLabel lblTimeFrom;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblToTime;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JPanel panelheader;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPriceFri;
    private javax.swing.JTextField txtPriceMon;
    private javax.swing.JTextField txtPriceSat;
    private javax.swing.JTextField txtPriceSun;
    private javax.swing.JTextField txtPriceThu;
    private javax.swing.JTextField txtPriceTue;
    private javax.swing.JTextField txtPriceWed;
    // End of variables declaration//GEN-END:variables

    /**
     * This method is used to get menu code
     *
     * @param menuHeadName
     * @return
     */
    private String funGetMenuCode(String menuHeadName)
    {
	String menuHeadCode = "";
	menuHeadCode = mapMenuHead.get(cmbMenuNames.getSelectedItem().toString());
	return menuHeadCode;
    }

    /**
     * This method is used to get sub menu codes
     *
     * @param subMenuName
     * @return
     */
    private String funGetSubMenuCode(String subMenuName)
    {
	String subCode = "NA";
	ResultSet rsMenuHeadName = null;
	String subMName = subMenuName;
	try
	{
	    if (subMenuName.equalsIgnoreCase("--SELECT--") || subMenuName.trim().length() == 0)
	    {
		subCode = "NA";
	    }
	    else
	    {
		String sql = "select strSubMenuHeadCode "
			+ "from tblsubmenuhead "
			+ "where strSubMenuHeadName='" + subMName + "' "
			+ "group by strSubMenuHeadCode";
		rsMenuHeadName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsMenuHeadName.next();
		subCode = rsMenuHeadName.getString(1);
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	return subCode;
    }

    /**
     * This method is used to insert item price
     *
     * @param areaCode
     */
    private void funInsertItemPrice(String areaCode)
    {
	try
	{
	    String hourlyPrice = "No";
	    btnNew.setText("SAVE");

	    java.util.Date dt = new java.util.Date();

	    Date fromDt = dteFromDate.getDate();
	    Date toDt = dteToDate.getDate();
	    if ((toDt.getTime() - fromDt.getTime()) < 0)
	    {
		new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
		return;
	    }
	    if (cmbMenuNames.getSelectedItem().equals(" "))
	    {
		cmbMenuNames.requestFocus();
		new frmOkPopUp(this, "Please select menu", "Error", 1).setVisible(true);
		return;
	    }
	    if (cmbCostCenter.getItemCount() == 0)
	    {
		cmbCostCenter.requestFocus();
		new frmOkPopUp(this, "Please select cost center", "Error", 1).setVisible(true);
		return;
	    }
	    if (funCheckNegativeValue())
	    {
		new frmOkPopUp(this, "Invalid Amount", "Error", 1).setVisible(true);
		return;
	    }
	    if ((!cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH")))
	    {
		new frmOkPopUp(this, "Please Select To Time", "Error", 1).setVisible(true);
		return;
	    }
	    if ((cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (!cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH")))
	    {
		new frmOkPopUp(this, "Please Select From Time", "Error", 1).setVisible(true);
		return;
	    }
	    if (cmbCostCenter.getSelectedItem().toString().equals("All"))
	    {
		new frmOkPopUp(this, "Please Select Cost Center", "Error", 1).setVisible(true);
		return;
	    }
	    if (chkHourlyPricing.isSelected())
	    {
		if (funCheckFromToTime())
		{

		    String currentDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
		    String strFromTime = cmbFromHour.getSelectedItem().toString() + ":" + cmbFromMinute.getSelectedItem().toString() + ":" + cmbFromTimeSeconds.getSelectedItem().toString();
		    String strToTime = cmbToHour.getSelectedItem().toString() + ":" + cmbToMinute.getSelectedItem().toString() + ":" + cmbToTimeSeconds.getSelectedItem().toString();

		    //String fromTime = currentDate + " " + funConvertTime(strFromTime);
		    //String toTime = currentDate + " " + funConvertTime(strToTime);
		    String fromTime = currentDate + " " + strFromTime;
		    String toTime = currentDate + " " + strToTime;
		    clsUtility objUtility = new clsUtility();
		    //long diff1 = objUtility.funCompareTime(fromTime, toTime);
//		    if (diff1 <= 0)
//		    {
//			JOptionPane.showMessageDialog(this, "Please Enter vaild TO Time");
//			return;
//		    }
		}
		else
		{
		    new frmOkPopUp(this, "Select Time for Hourly Pricing.", "Error", 1).setVisible(true);
		    return;
		}
	    }

	    if (chkHourlyPricing.isSelected())
	    {
		hourlyPrice = "Yes";
		String fromHour = cmbFromHour.getSelectedItem().toString();
		String fromMin = cmbFromMinute.getSelectedItem().toString();
		String toHour = cmbToHour.getSelectedItem().toString();
		String toMin = cmbToMinute.getSelectedItem().toString();

		if (fromHour.equalsIgnoreCase("HH") || fromMin.equalsIgnoreCase("HH") || toHour.equalsIgnoreCase("HH") || toMin.equalsIgnoreCase("MM"))
		{
		    new frmOkPopUp(this, "Invalid Time Entered.", "Error", 1).setVisible(true);
		    return;
		}
	    }
	    else if (funCheckFromToTime())
	    {
		new frmOkPopUp(this, "Please Tick Hourly Pricing.", "Error", 1).setVisible(true);
		return;
	    }
	    String menuName = cmbMenuNames.getSelectedItem().toString();
	    String posCode = mapPOS.get(cmbPOSNames.getSelectedItem().toString());
	    String posName = cmbPOSNames.getSelectedItem().toString();
	    String menuCode = funGetMenuCode(cmbMenuNames.getSelectedItem().toString());
	    String costCenter = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());

	    String subMenucode = "NA";
	    if (cmbSubMenuHead.getSelectedIndex() > 0)
	    {
		subMenucode = funGetSubMenuCode(cmbSubMenuHead.getSelectedItem().toString());
	    }
	    popularItem = "N";
	    if (chkPopular.isSelected() == true)
	    {
		popularItem = "Y";
	    }

	    int exc = 0;
	    String tmeFrom = cmbFromHour.getSelectedItem().toString() + ":" + cmbFromMinute.getSelectedItem().toString() + ":" + cmbFromTimeSeconds.getSelectedItem().toString();
	    String tmeTo = cmbToHour.getSelectedItem().toString() + ":" + cmbToMinute.getSelectedItem().toString() + ":" + cmbToTimeSeconds.getSelectedItem().toString();

	    Date fromDate = dteFromDate.getDate();
	    Date toDate = dteToDate.getDate();

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    String strFromDate = simpleDateFormat.format(fromDate);
	    String strToDate = simpleDateFormat.format(toDate);

	    sql = "select count(*) from tblmenuitempricingdtl "
		    + "where (strPosCode='" + posCode + "' or strPosCode='All') "
		    + "and strItemCode='" + txtItemCode.getText() + "' and strAreaCode='" + areaCode + "' "
		    + "and strHourlyPricing='" + hourlyPrice + "'";
	    ResultSet rsItemPriceCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsItemPriceCount.next();
	    if (rsItemPriceCount.getInt(1) == 0 || hourlyPrice.equals("Yes"))
	    {
		insertQuery = "insert into tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,strMenuCode"
			+ ",strPopular,strPriceMonday,strPriceTuesday,strPriceWednesday,strPriceThursday,strPriceFriday"
			+ ",strPriceSaturday,strPriceSunday,dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,tmeTimeTo"
			+ ",strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,strUserEdited,dteDateCreated"
			+ ",dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode) "
			+ "values('" + txtItemCode.getText() + "','" + txtItemName.getText() + "','" + posCode + "','" + menuCode + "'"
			+ ",'" + popularItem + "','" + txtPriceMon.getText() + "','" + txtPriceTue.getText() + "'"
			+ ",'" + txtPriceWed.getText() + "','" + txtPriceThu.getText() + "','" + txtPriceFri.getText() + "','" + txtPriceSat.getText() + "'"
			+ ",'" + txtPriceSun.getText() + "','" + strFromDate + "','" + strToDate + "','" + tmeFrom + "'"
			+ ",'AM','" + tmeTo + "','AM','" + costCenter + "','" + cmbColor.getSelectedItem() + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + areaCode + "','" + subMenucode + "'"
			+ ",'" + hourlyPrice + "','" + clsGlobalVarClass.gClientCode + "')";
		//System.out.println(insertQuery);
		exc = clsGlobalVarClass.dbMysql.execute(insertQuery);

		sql = "select count(*) from tblmenuitempricinghd where strPosCode='" + posCode + "' and strMenuCode='" + menuCode + "'";
		ResultSet menuRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		menuRs.next();
		if (menuRs.getInt(1) == 0)
		{
		    insertQuery = "insert into tblmenuitempricinghd(strPosCode,strMenuCode,strMenuName,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited) "
			    + "values('" + posCode + "','" + menuCode + "','" + menuName + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
		    exc = clsGlobalVarClass.dbMysql.execute(insertQuery);
		}
		if (exc > 0)
		{
		    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + " where strTableName='MenuItemPricing' ";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
		    funResetField();
		    if (!flagfrmItemMaster)
		    {
			dispose();
			new frmMenuItemMaster().setVisible(true);
		    }
		}
	    }
	    else
	    {
		String msg = "<html>Price for this item is already<br>set in " + posName + "</html>";
		new frmOkPopUp(this, msg, "Error", 1).setVisible(true);
	    }
	    rsItemPriceCount.close();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to update item price
     *
     * @param areaCode
     */
    private void funUpdateItemPrice(String areaCode)
    {
	try
	{
	    java.util.Date dt = new java.util.Date();

	    String posCode = mapPOS.get(cmbPOSNames.getSelectedItem().toString());
	    String menuCode = funGetMenuCode(cmbMenuNames.getSelectedItem().toString());
	    String costCenter = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());
	    String SubMenuHeadName = cmbSubMenuHead.getSelectedItem().toString();
	    String subMenucode = funGetSubMenuCode(SubMenuHeadName);

	    popularItem = "N";
	    if (chkPopular.isSelected() == true)
	    {
		popularItem = "Y";
	    }
	    Date fromDt = dteFromDate.getDate();
	    Date toDt = dteToDate.getDate();
	    if ((toDt.getTime() - fromDt.getTime()) < 0)
	    {
		new frmOkPopUp(this, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else if (cmbMenuNames.getSelectedItem().equals(" "))
	    {
		cmbMenuNames.requestFocus();
		new frmOkPopUp(this, "Please select menu", "Error", 1).setVisible(true);
	    }
	    else if (cmbCostCenter.getItemCount() == 0)
	    {
		cmbCostCenter.requestFocus();
		new frmOkPopUp(this, "Please select cost center", "Error", 1).setVisible(true);
	    }
	    else if (funCheckNegativeValue())
	    {
		new frmOkPopUp(this, "Invalid Amount", "Error", 1).setVisible(true);
	    }
	    else if ((!cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH")))
	    {
		new frmOkPopUp(this, "Please Select To Time", "Error", 1).setVisible(true);
	    }
	    else if ((cmbFromHour.getSelectedItem().toString().equalsIgnoreCase("HH")) && (!cmbToHour.getSelectedItem().toString().equalsIgnoreCase("HH")))
	    {
		new frmOkPopUp(this, "Please Select From Time", "Error", 1).setVisible(true);
	    }
	    else if (cmbCostCenter.getSelectedItem().toString().equals("All"))
	    {
		new frmOkPopUp(this, "Please Select Cost Center", "Error", 1).setVisible(true);
	    }
	    else
	    {
		String hourlyPrice = "No";
		if (chkHourlyPricing.isSelected())
		{
		    if (funCheckFromToTime())
		    {

			String currentDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
			String strFromTime = cmbFromHour.getSelectedItem().toString() + ":" + cmbFromMinute.getSelectedItem().toString() + ":" + cmbFromTimeSeconds.getSelectedItem().toString();
			String strToTime = cmbToHour.getSelectedItem().toString() + ":" + cmbToMinute.getSelectedItem().toString() + ":" + cmbToTimeSeconds.getSelectedItem().toString();

			//String fromTime = currentDate + " " + funConvertTime(strFromTime);
			//String toTime = currentDate + " " + funConvertTime(strToTime);
			String fromTime = currentDate + " " + strFromTime;
			String toTime = currentDate + " " + strToTime;
			clsUtility objUtility = new clsUtility();
			long diff1 = objUtility.funCompareTime(fromTime, toTime);
//			if (diff1 <= 0)
//			{
//			    JOptionPane.showMessageDialog(this, "Please Enter vaild TO Time");
//			    return;
//			}
		    }
		    else
		    {
			new frmOkPopUp(this, "Select Time for Hourly Pricing.", "Error", 1).setVisible(true);
			return;
		    }
		}

		if (chkHourlyPricing.isSelected())
		{
		    hourlyPrice = "Yes";
		    String fromHour = cmbFromHour.getSelectedItem().toString();
		    String fromMin = cmbFromMinute.getSelectedItem().toString();
		    String toHour = cmbToHour.getSelectedItem().toString();
		    String toMin = cmbToMinute.getSelectedItem().toString();

		    if (fromHour.equalsIgnoreCase("HH") || fromMin.equalsIgnoreCase("HH") || toHour.equalsIgnoreCase("HH") || toMin.equalsIgnoreCase("MM"))
		    {
			new frmOkPopUp(this, "Invalid Time Entered.", "Error", 1).setVisible(true);
			return;
		    }
		}
		else if (funCheckFromToTime())
		{
		    new frmOkPopUp(this, "Please Tick Hourly Pricing.", "Error", 1).setVisible(true);
		    return;
		}
		String tmeFrom = cmbFromHour.getSelectedItem().toString() + ":" + cmbFromMinute.getSelectedItem().toString() + ":" + cmbFromTimeSeconds.getSelectedItem().toString();
		String tmeTo = cmbToHour.getSelectedItem().toString() + ":" + cmbToMinute.getSelectedItem().toString() + ":" + cmbToTimeSeconds.getSelectedItem().toString();

		if (funCheckItemFromDatetoDate(posCode, areaCode))
		{
		    insertQuery = "insert into tblitempricingauditdtl "
			    + " select * from tblmenuitempricingdtl "
			    + " where (strPosCode='" + posCode + "' or strPosCode='All') "
			    + " and strItemCode='" + txtItemCode.getText() + "' and strAreaCode='" + oldAreaCode + "' "
			    + " and strHourlyPricing='" + hourlyPrice + "' ";
		    //System.out.println(insertQuery);
		    clsGlobalVarClass.dbMysql.execute(insertQuery);
		}

		Date fromDate = dteFromDate.getDate();
		Date toDate = dteToDate.getDate();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String strFromDate = simpleDateFormat.format(fromDate);
		String strToDate = simpleDateFormat.format(toDate);

		updateQuery = "UPDATE tblmenuitempricingdtl "
			+ " SET strItemName = '" + txtItemName.getText() + "',strPosCode='" + posCode + "',strMenuCode='" + menuCode + "'"
			+ ",strPopular='" + popularItem + "',strPriceMonday='" + txtPriceMon.getText() + "'"
			+ ",strPriceTuesday='" + txtPriceTue.getText() + "',strPriceWednesday='" + txtPriceWed.getText() + "'"
			+ ",strPriceThursday='" + txtPriceThu.getText() + "',strPriceFriday='" + txtPriceFri.getText() + "'"
			+ ",strPriceSaturday='" + txtPriceSat.getText() + "',strPriceSunday='" + txtPriceSun.getText() + "',dteFromDate='" + strFromDate + "'"
			+ ",dteToDate='" + strToDate + "',tmeTimeFrom='" + tmeFrom + "',strAMPMFrom='AM'"
			+ ",tmeTimeTo='" + tmeTo + "',strAMPMTo='AM'"
			+ ",strCostCenterCode='" + costCenter + "',strTextColor='" + cmbColor.getSelectedItem() + "'"
			+ ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strAreaCode='" + areaCode + "'"
			+ ",strSubMenuHeadCode='" + subMenucode + "',strHourlyPricing='" + hourlyPrice + "'"
			+ ",strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			+ "where longPricingId='" + longPrincingId + "' ";
//                        + " WHERE strAreaCode='" + oldAreaCode + "' and strItemCode ='" + txtItemCode.getText() + "' "
//                        + " and strHourlyPricing='" + hourlyPrice + "' and (strPosCode='" + oldPOSCode + "' or strPosCode='All') ";
		//System.out.println(updateQuery);
		clsGlobalVarClass.dbMysql.execute(updateQuery);

		if (posCode.equalsIgnoreCase("All"))
		{
		    String sqlDeleteDuplicates = "delete from tblmenuitempricingdtl "
			    + "where strItemCode='" + txtItemCode.getText() + "' "
			    + "and strPosCode!='All' ";
		    clsGlobalVarClass.dbMysql.execute(sqlDeleteDuplicates);
		}

		String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			+ " where strTableName='MenuItemPricing' ";
		clsGlobalVarClass.dbMysql.execute(sql);
		new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
		funResetField();
		/*
                 else
                 {
                 String msg="<html>Price for this item is not<br>set in "+posName+"</html>";
                 new frmOkPopUp(this,msg, "Error",1).setVisible(true);
                 }*/
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to check from to time
     *
     * @return
     */
    private boolean funCheckFromToTime()
    {
	boolean flg = true;
	String fromHour = cmbFromHour.getSelectedItem().toString();
	String fromMin = cmbFromMinute.getSelectedItem().toString();
	String toHour = cmbToHour.getSelectedItem().toString();
	String toMin = cmbToMinute.getSelectedItem().toString();

	if (fromHour.equalsIgnoreCase("HH") || toHour.equalsIgnoreCase("HH") || fromMin.equalsIgnoreCase("MM") || toMin.equalsIgnoreCase("MM"))
	{
	    flg = false;
	}

	return flg;
    }

    private boolean funCheckItemFromDatetoDate(String posCode, String areaCode)
    {
	boolean flgDate = false;
	try
	{

	    Date fromDate = dteFromDate.getDate();
	    Date toDate = dteToDate.getDate();

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    String strFromDate = simpleDateFormat.format(fromDate);
	    String strToDate = simpleDateFormat.format(toDate);

	    sql = "select count(*) from tblmenuitempricingdtl "
		    + "where (strPosCode='" + posCode + "' or strPosCode='All') "
		    + "and strItemCode='" + txtItemCode.getText() + "' "
		    + "and strAreaCode='" + areaCode + "' "
		    + "and date(dteToDate)!='" + strToDate + "' ";
	    //System.out.println(sql);
	    ResultSet cntRs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (cntRs.next())
	    {
		if (cntRs.getInt(1) > 0)
		{
		    flgDate = true;
		}
	    }
	    cntRs.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flgDate;
	}
    }
}
