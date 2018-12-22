/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsFixedSizeText;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsLinkupDtl;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumericKeyboard;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmMenuItemMaster extends javax.swing.JFrame
{

    //private String subCode;
    private String[] menuNames, subGroupNames, menuCodes, subGroupCodes;
    BufferedImage imgBf;
    String strPath;
    FileInputStream fileInImg;
    private String itemImagefilePath;
    private String userImagefilePath;
    private File tempFile;
    private File destFile = null;
    private String taxIndicator, strCode, updateQuery, stkInFlag, sql, discountApply;
    private String[] indicator
	    =
	    {
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	    };
    private Map hmPOS, hmOrder;
    private Map<String, List<String>> hmCharValues;
    clsUtility objUtility;
    DefaultTableModel dmItemLinkup, dmOrderItemLinkup;
    private DefaultTableModel dmChildRows, dmEmptyModel;
    private String selectedItemCode;

    /**
     * This method is used to initialize frmMenuItemMaster
     */
    public frmMenuItemMaster()
    {
	initComponents();
	objUtility = new clsUtility();
	lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	clsGlobalVarClass.gFormNameOnKeyBoard = "Item";
	try
	{
	    selectedItemCode = "";
	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    java.util.Date date1 = new java.util.Date();
		    String newstr = String.format("%tr", date1);
		    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
		    lblDate.setText(dateAndTime);
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();
	    hmCharValues = new HashMap<String, List<String>>();
	    dmItemLinkup = (DefaultTableModel) tblItemCodeLinkup.getModel();
	    dmOrderItemLinkup = (DefaultTableModel) tblOrderDetails.getModel();
	    int i = 0;
	    funFillComboBox();
	    txtItemCode.requestFocus();
	    txtItemName.setDocument(new clsFixedSizeText(50));
	    cmbTaxIndicator.setSelectedItem(" ");
	    txtPurRate.setText("0");
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    java.util.Date dt = new java.util.Date();
	    int day = dt.getDate();
	    int month = dt.getMonth() + 1;
	    int year = dt.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    String selectQuery = "select count(strMenuName) from tblmenuhd";
	    ResultSet recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    recordSet.next();
	    int cnt = recordSet.getInt(1);
	    menuNames = new String[cnt];
	    menuCodes = new String[cnt];
	    subGroupCodes = new String[cnt];
	    subGroupNames = new String[cnt];
	    recordSet.close();
	    selectQuery = "select strMenuCode,strMenuName from tblmenuhd";
	    recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    i = 0;
	    while (recordSet.next())
	    {
		menuCodes[i] = recordSet.getString(1);
		menuNames[i] = recordSet.getString(2);
		i++;
	    }
	    recordSet.close();
	    selectQuery = "select count(strSubGroupCode) from tblsubgrouphd";
	    recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    recordSet.next();
	    cnt = recordSet.getInt(1);
	    subGroupCodes = new String[cnt];
	    subGroupNames = new String[cnt];
	    recordSet.close();
	    selectQuery = "select strSubGroupCode,strSubGroupName from tblsubgrouphd";
	    recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    i = 0;
	    while (recordSet.next())
	    {
		cmbSubGroupCode.addItem(recordSet.getString(2));
		subGroupCodes[i] = recordSet.getString(1);
		subGroupNames[i] = recordSet.getString(2);
		i++;
	    }
	    recordSet.close();
	    for (int min = 1; min < 31; min++)
	    {
		cmbProcessingTme.addItem(min);
	    }

	    for (int min = 1; min < 31; min++)
	    {
		cmbTargetMissedTime.addItem(min);
	    }

	    if (clsGlobalVarClass.gNatureOfBusinnes.equalsIgnoreCase("F&B"))
	    {
		cmbItemType.setSelectedIndex(1);
	    }
	    else
	    {
		cmbItemType.setSelectedIndex(3);
	    }
	    funSetShortCutKeys();
	    funFillCharactersticsMaster();

	    dmEmptyModel = (DefaultTableModel) tblChildItems.getModel();
	    dmChildRows = (DefaultTableModel) tblChildItems.getModel();
	    funSetFormToInDateChosser();
	    funSetShortCutKeys();
	    txtRecipeCode.requestFocus();

	    funFillUomCombo();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	cmbRecipeUOM.addItemListener(new ItemListener()
	{
	    @Override
	    public void itemStateChanged(ItemEvent e)
	    {
		lblUomName.setText(cmbRecipeUOM.getSelectedItem().toString());
	    }
	});
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
    private void funSetData(Object[] data)
    {
	try
	{
	    tempFile = null;
	    sql = "select * from tblitemmaster where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    ResultSet rsItemData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemData.next())
	    {
		txtItemCode.setText(rsItemData.getString(1));
		txtItemName.setText(rsItemData.getString(2));
		txtItemImage.setText(rsItemData.getString(4));
		txtShortName1.setText(rsItemData.getString(19));
		txtMinlevel.setText(rsItemData.getString(20));
		txtMaxlevel.setText(rsItemData.getString(21));
		txtMenuItemCode.setText(rsItemData.getString(1));
		lblMenuItemName.setText(rsItemData.getString(2));

		String disApply = rsItemData.getString(18).toString();
		if (disApply.equalsIgnoreCase("N"))
		{
		    chkDiscount.setSelected(true);
		}
		else
		{
		    chkDiscount.setSelected(false);
		}
		for (int cntSubGpCode = 0; cntSubGpCode < subGroupCodes.length; cntSubGpCode++)
		{
		    if (subGroupCodes[cntSubGpCode].equals(rsItemData.getString(3)))
		    {
			cmbSubGroupCode.setSelectedItem(subGroupNames[cntSubGpCode]);
			break;
		    }
		}
		cmbTaxIndicator.setSelectedItem(rsItemData.getString(5));
		if (rsItemData.getString(6).equalsIgnoreCase("Y"))
		{
		    chkStkInEnable.setSelected(true);
		}
		txtPurRate.setText(rsItemData.getString(7));

		txtExtCode.setText(rsItemData.getString(9));
		clsGlobalVarClass.gItemDetails = rsItemData.getString(10);
		cmbItemType.setSelectedItem(rsItemData.getString(17));

		if (rsItemData.getString(23).equals("Y"))
		{
		    chkRawMaterial.setSelected(true);
		}
		else
		{
		    chkRawMaterial.setSelected(false);
		}

		txtSalePrice.setText(rsItemData.getString(24));
		if (rsItemData.getString(25).equals("Y"))
		{
		    chkItemForSale.setSelected(true);
		}
		cmbRevenueHead.setSelectedItem(rsItemData.getString(26).toUpperCase());
		txtItemWeight.setText(rsItemData.getString(27));

		if (rsItemData.getString(28).equals("Y"))
		{
		    chkOpenItem.setSelected(true);
		}
		else
		{
		    chkOpenItem.setSelected(false);
		}

		if (rsItemData.getString(29).equalsIgnoreCase("Y"))
		{
		    chkItemWiseKOTYN.setSelected(true);
		}
		else
		{
		    chkItemWiseKOTYN.setSelected(false);
		}
		txtExciseBrandCode.setText(rsItemData.getString(31));

		String[] spDays = rsItemData.getString(32).split(",");
		for (int cnt = 0; cnt < spDays.length; cnt++)
		{
		    if (spDays[cnt].equals("Monday"))
		    {
			chkMonday.setSelected(true);
		    }

		    if (spDays[cnt].equals("Tuesday"))
		    {
			chkTuesday.setSelected(true);
		    }

		    if (spDays[cnt].equals("Wednesday"))
		    {
			chkWed.setSelected(true);
		    }

		    if (spDays[cnt].equals("Thursday"))
		    {
			chkThursday.setSelected(true);
		    }

		    if (spDays[cnt].equals("Friday"))
		    {
			chkFriday.setSelected(true);
		    }

		    if (spDays[cnt].equals("Saturday"))
		    {
			chkSaturday.setSelected(true);
		    }

		    if (spDays[cnt].equals("Sunday"))
		    {
			chkSunday.setSelected(true);
		    }
		}

		txtRequiredProductDeliveryDays.setText(rsItemData.getString(33));
		txtItemWeight.setText(rsItemData.getString(34));
		txtMinItemWeight.setText(rsItemData.getString(35));
		if (rsItemData.getString(36).equals("Y"))
		{
		    chkUrgentOrder.setSelected(true);
		}

		cmbReceivedUOM.setSelectedItem(rsItemData.getString(37));
		cmbProcessingTme.setSelectedItem(rsItemData.getInt(8));
		cmbTargetMissedTime.setSelectedItem(rsItemData.getInt(39));
		cmbRecipeUOM.setSelectedItem(rsItemData.getString(40));
		lblUomName.setText(rsItemData.getString(40));
		txtReceivedConversion.setText(rsItemData.getString(41));
		txtRecipeConversion.setText(rsItemData.getString(42));
		txtHSNNo.setText(rsItemData.getString(43));
		String operationalYN = rsItemData.getString(44);
		if (operationalYN.equalsIgnoreCase("Y"))
		{
		    chkOperationalYN.setSelected(true);
		}
		else
		{
		    chkOperationalYN.setSelected(false);
		}

		strPath = rsItemData.getString(4);
		String extension = "";

		int i = strPath.lastIndexOf('.');
		if (i > 0)
		{
		    extension = strPath.substring(i + 1);
		}

		File fileItemImage = new File(System.getProperty("user.dir") + "\\itemImages\\" + txtItemCode.getText().trim() + "." + extension);

		if (fileItemImage.exists())
		{

		    // fileInImg = new FileInputStream(fileItemImage);
		    String imagePath = fileItemImage.getAbsolutePath();
		    userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());

		    imgBf = funScaleImage(100, 100, userImagefilePath);
		    ImageIO.write(imgBf, "png", fileItemImage);

		    ImageIcon icon1 = new ImageIcon(ImageIO.read(fileItemImage));
		    bttImage.setIcon(icon1);
		    txtItemImage.setText(imagePath);
		}
		else
		{
		    Blob blob = rsItemData.getBlob(38);
		    //InputStream inImg = blob.getBinaryStream();

		    if (blob.length() > 0)
		    {
			InputStream inImg = blob.getBinaryStream(1, blob.length());
			byte[] imageBytes = blob.getBytes(1, (int) blob.length());
			//BufferedImage image = ImageIO.read(inImg);
			OutputStream outImg = new FileOutputStream(fileItemImage);
			int c = 0;
			while ((c = inImg.read()) > -1)
			{
			    outImg.write(c);
			}
			outImg.close();
			inImg.close();

			if (fileItemImage.exists())
			{
			    //fileInImg = new FileInputStream(fileItemImage);
			    ImageIcon icon1 = new ImageIcon(ImageIO.read(fileItemImage));
			    bttImage.setIcon(icon1);

			}
			else
			{
			    txtItemImage.setText("");
			}
		    }
		    else
		    {
			txtItemImage.setText("");
		    }
		}
	    }
	    rsItemData.close();

//            funSelectRecipeData();
	    String recipeCode = "";
	    String sql = "select strRecipeCode,date(dteFromDate),date(dteToDate)  from tblrecipehd where strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
	    ResultSet rsRecipeData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsRecipeData.next())
	    {
		recipeCode = rsRecipeData.getString(1);
		txtRecipeCode.setText(rsRecipeData.getString(1));
		dteFromDate.setDate(rsRecipeData.getDate(2));
		dteToDate.setDate(rsRecipeData.getDate(3));
	    }
	    funFillRecipeDTlTable(recipeCode);

	    bttImage.setText("");
	    bttImage.setIcon(null);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillRecipeDTlTable(String recipeCode)
    {
	try
	{
	    boolean select = false;
	    DefaultTableModel dm = (DefaultTableModel) tblChildItems.getModel();
	    sql = "select b.strChildItemCode,c.strItemName,b.dblQuantity from tblrecipehd a ,tblrecipedtl b\n"
		    + ",tblitemmaster c"
		    + " where a.strRecipeCode = b.strRecipeCode and \n"
		    + " b.strChildItemCode = c.strItemCode and "
		    + "b.strRecipeCode = '" + recipeCode + "'\n";
	    ResultSet rsRecipeChildData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsRecipeChildData.next())
	    {
		select = true;
		Object row[] =
		{
		    rsRecipeChildData.getString(1), rsRecipeChildData.getString(2), rsRecipeChildData.getString(3), select
		};
		dm.addRow(row);
	    }
	    rsRecipeChildData.close();
	    tblChildItems.setModel(dm);
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
	    tempFile = null;
	    destFile = null;
	    btnNew.setMnemonic('s');
	    txtItemImage.setText("");
	    bttImage.setText("");
	    bttImage.setIcon(null);
	    btnNew.setText("SAVE");
	    txtMaxlevel.setText("");
	    txtItemCode.setText("");
	    txtShortName1.setText("");
	    txtItemName.setText("");
	    txtItemImage.setText("");
	    txtSalePrice.setText("0.00");
	    txtPurRate.setText("0.00");
	    txtExtCode.setText("");
	    cmbProcessingDay.setSelectedIndex(0);
	    txtMinlevel.setText("0.00");
	    txtMaxlevel.setText("0.00");
	    chkDiscount.setSelected(false);
	    chkStkInEnable.setSelected(false);
	    clsGlobalVarClass.gItemDetails = " ";

	    cmbTaxIndicator.removeAllItems();
	    cmbTaxIndicator.addItem(" ");
	    for (int i = 0; i < 26; i++)
	    {
		cmbTaxIndicator.addItem(indicator[i]);
	    }
	    chkStkInEnable.setSelected(false);
	    cmbProcessingTme.setSelectedIndex(0);
	    chkRawMaterial.setSelected(false);
	    txtItemCode.requestFocus();
	    cmbRevenueHead.setSelectedIndex(0);
	    txtItemWeight.setText("0.00");
	    chkOpenItem.setSelected(false);
	    chkItemWiseKOTYN.setSelected(false);
	    txtWSProductCode.setText("");
	    txtExciseBrandCode.setText("");
	    funFillCharactersticsMaster();
	    hmCharValues.clear();
	    dmItemLinkup.setRowCount(0);
	    tblItemCodeLinkup.setModel(dmItemLinkup);
	    dmOrderItemLinkup.setRowCount(0);
	    tblOrderDetails.setModel(dmOrderItemLinkup);

	    chkSunday.setSelected(false);
	    chkMonday.setSelected(false);
	    chkTuesday.setSelected(false);
	    chkWed.setSelected(false);
	    chkThursday.setSelected(false);
	    chkFriday.setSelected(false);
	    chkSaturday.setSelected(false);

	    cmbReceivedUOM.setSelectedIndex(0);
	    cmbTargetMissedTime.setSelectedIndex(0);

	    lblMenuItemName.setText("");
	    txtRecipeCode.setText("");
	    txtMenuItemCode.setText("");
	    funResetChildItemFields();
	    funSetFormToInDateChosser();
	    dmChildRows.setRowCount(0);
	    btnNew.setMnemonic('s');
	    txtRecipeCode.requestFocus();
	    cmbReceivedUOM.setSelectedIndex(0);
	    cmbRecipeUOM.setSelectedIndex(0);
	    txtReceivedConversion.setText("1.00");
	    txtRecipeConversion.setText("1.00");
	    txtHSNNo.setText("");
	    chkOperationalYN.setSelected(true);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to reset product linkup fields
     */
    private void funResetProductLinkupField()
    {
	try
	{

	    txtWSProductCode.setText("");
	    txtWSProductName.setText("");
	    dmItemLinkup.setRowCount(0);
	    txtExciseBrandCode.setText("");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to reset product linkup fields
     */
    private void funResetOrderLinkupField()
    {
	try
	{

	    txtRequiredProductDeliveryDays.setText("0");
	    chkSunday.setSelected(false);
	    chkMonday.setSelected(false);
	    chkTuesday.setSelected(false);
	    chkWed.setSelected(false);
	    chkThursday.setSelected(false);
	    chkFriday.setSelected(false);
	    chkSaturday.setSelected(false);
	    dmOrderItemLinkup.setRowCount(0);
	    txtItemWeight.setText("0");
	    txtMinItemWeight.setText("0");
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillComboBox() throws Exception
    {
	hmOrder = new HashMap<String, String>();
	hmPOS = new HashMap<String, String>();
	String sql = " select strOrderCode,strOrderDesc from tblordermaster "
		+ " where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	ResultSet rsOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsOrder.next())
	{
	    hmOrder.put(rsOrder.getString(2), rsOrder.getString(1));
	}
	rsOrder.close();

	Set setOrder = hmOrder.keySet();
	Iterator itrOrder = setOrder.iterator();
	while (itrOrder.hasNext())
	{
	    cmbOrderType.addItem(itrOrder.next());

	}

	String sqlPOS = " select strPosCode,strPosName from tblposmaster ";
	ResultSet rspos = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
	while (rspos.next())
	{
	    hmPOS.put(rspos.getString(2), rspos.getString(1));
	}
	rspos.close();

	Set setPOS = hmPOS.keySet();
	Iterator itrpos = setPOS.iterator();
	while (itrpos.hasNext())
	{
	    cmbPosCode.addItem(itrpos.next());
	}

    }

    private void funFillUomCombo()
    {
	try
	{
	    cmbReceivedUOM.addItem("");
	    cmbRecipeUOM.addItem("");
	    String sqlPOS = " select strUomName from tbluommaster ";
	    ResultSet rspos = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
	    while (rspos.next())
	    {
		cmbReceivedUOM.addItem(rspos.getString(1));
		cmbRecipeUOM.addItem(rspos.getString(1));
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to save item master
     */
  

 private void funSaveItemMaster()
    {
	try
	{
	    String itemName = txtItemName.getText().trim();
	    String itemShortName = txtShortName1.getText().trim();

	    String code = "", selectQuery = "";
	    selectQuery = "select max(strItemCode) from tblitemmaster";
	    ResultSet recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    recordSet.next();
	    code = recordSet.getString(1);

	    if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strItemName", "strItemCode", itemName, code, "save", ""))
	    {
		JOptionPane.showMessageDialog(this, "Item Name is Already Exsist");
		txtItemName.requestFocus();
		return;
	    }
	    if (!txtShortName1.getText().isEmpty())
	    {
		if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strShortName", "strItemCode", itemShortName, code, "save", ""))
		{
		    JOptionPane.showMessageDialog(this, "Item Short Name is Already Exsist");
		    return;
		}
		if (!objUtility.funCheckLength(txtShortName1.getText(), 20))
		{
		    new frmOkPopUp(this, "Item Short Name length must be less than 20", "Error", 0).setVisible(true);
		    txtShortName1.requestFocus();
		}
	    }
	    /*
             * if(funCheckProductCodeForItem()) {
             * JOptionPane.showMessageDialog(this, "This product code already
             * used for item from selected pos"); return; }
	     */

	    if (txtItemCode.getText().isEmpty())
	    {
		String itemCode = funGenerateItemCode();
		txtItemCode.setText(itemCode);
	    }

	    if (!clsGlobalVarClass.validateEmpty(txtItemName.getText()))
	    {
		new frmOkPopUp(this, "Please Enter Item Name", "Error", 0).setVisible(true);
		return;
	    }
	    if (!objUtility.funCheckLength(txtItemName.getText(), 50))
	    {
		new frmOkPopUp(this, "Item Name length must be less than 22", "Error", 0).setVisible(true);
		txtItemName.requestFocus();
		return;
	    }
	    if (!objUtility.funCheckLength(txtExtCode.getText(), 15))
	    {
		new frmOkPopUp(this, "External Code length must be less than 15", "Error", 0).setVisible(true);
		txtExtCode.requestFocus();
		return;
	    }
	    if (!objUtility.funCheckLength(txtPurRate.getText(), 21))
	    {
		new frmOkPopUp(this, "Purchase Rate length must be less than 20", "Error", 0).setVisible(true);
		txtPurRate.requestFocus();
		return;
	    }
	    if (txtExtCode.getText().trim().length() > 0)
	    {
		sql = "select count(*) from tblitemmaster where strExternalCode='" + txtExtCode.getText().trim() + "'";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rs.next();
		int found = rs.getInt(1);
		rs.close();
		if (found > 0)
		{
		    new frmOkPopUp(this, "External Code Already Used", "Error", 0).setVisible(true);
		    txtExtCode.requestFocus();
		    return;
		}
	    }
	    if (txtMaxlevel.getText().trim().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Short Name");
		txtMaxlevel.setFocusable(true);
		return;
	    }
	    if (txtMaxlevel.getText().trim().length() > 8)
	    {
		JOptionPane.showMessageDialog(this, "Length of the Short Name Must Be 8 characters");
		txtMaxlevel.setFocusable(true);
		return;
	    }
	    if (cmbItemType.getSelectedIndex() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Please Select Item Type");
		cmbItemType.requestFocus();
		return;
	    }
	    else
	    {
		if (chkStkInEnable.isSelected())
		{
		    stkInFlag = "Y";
		}
		else
		{
		    stkInFlag = "N";
		}
		discountApply = "Y";
		if (chkDiscount.isSelected() == true)
		{
		    discountApply = "N";
		}
		String rawMaterial = "N";
		if (chkRawMaterial.isSelected())
		{
		    rawMaterial = "Y";
		}
		else
		{
		    rawMaterial = "N";
		}

		if (rawMaterial.equals("Y"))
		{
		    if (!txtChildItemName.getText().equalsIgnoreCase(""))
		    {
			JOptionPane.showMessageDialog(this, "Please select prent item ");
			txtChildItemName.setFocusable(true);
			dmChildRows.setRowCount(0);
			txtChildItemName.setText("");
			return;
		    }
		}

		String itemForSale = "N";
		if (chkItemForSale.isSelected())
		{
		    itemForSale = "Y";
		}

		String subGroupCode = "";
		for (int k = 0; k < subGroupNames.length; k++)
		{
		    if (subGroupNames[k].equals(cmbSubGroupCode.getSelectedItem().toString()))
		    {
			subGroupCode = subGroupCodes[k];
			break;
		    }
		}

		String itemType = cmbItemType.getSelectedItem().toString();
		int intProcday = Integer.parseInt(cmbProcessingDay.getSelectedItem().toString());
		double minLevel = Double.valueOf(txtMinlevel.getText());
		double maxLevel = Double.valueOf(txtMaxlevel.getText());
		int intProcTimeMin = Integer.parseInt(cmbProcessingTme.getSelectedItem().toString());
		int intTargetMissedTimeMin = Integer.parseInt(cmbTargetMissedTime.getSelectedItem().toString());
		taxIndicator = cmbTaxIndicator.getSelectedItem().toString();

		String openItem = "N";
		if (chkOpenItem.isSelected())
		{
		    openItem = "Y";
		}
		String itemWiseKOT = "N";
		if (chkItemWiseKOTYN.isSelected())
		{
		    itemWiseKOT = "Y";
		}

		String noDeliverydays = "";

		if (chkSunday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Sunday";
		}
		if (chkMonday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Monday";
		}
		if (chkTuesday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Tuesday";
		}
		if (chkWed.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Wednesday";
		}
		if (chkThursday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Thursday";
		}
		if (chkFriday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Friday";
		}
		if (chkSaturday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Saturday";
		}
		if (noDeliverydays.isEmpty())
		{
		    noDeliverydays = "NA";
		}
		String urgentOrder = "N";
		if (chkUrgentOrder.isSelected())
		{
		    urgentOrder = "Y";
		}
		String unitOfMeasurement = cmbReceivedUOM.getSelectedItem().toString();
		String recipeUOM = cmbRecipeUOM.getSelectedItem().toString();

		if (rawMaterial.equalsIgnoreCase("Y"))
		{
		    if (unitOfMeasurement.isEmpty() || recipeUOM.isEmpty())
		    {
			JOptionPane.showMessageDialog(this, "Please select received and recipe UOM");
			return;
		    }
		}

		String receivedConversion = txtReceivedConversion.getText().toString();
		String recipeConversion = txtRecipeConversion.getText().toString();

		String hsnNo = txtHSNNo.getText().trim();
		String operationalYN = "N";
		if (chkOperationalYN.isSelected())
		{
		    operationalYN = "Y";
		}

		StringBuilder sb = new StringBuilder(noDeliverydays);
		sb = sb.delete(0, 1);
		noDeliverydays = sb.toString();
		int intDeliveryDays = Integer.parseInt(txtRequiredProductDeliveryDays.getText().toString());

		String query = "insert into tblitemmaster values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
		pre.setString(1, txtItemCode.getText());
		pre.setString(2, txtItemName.getText());
		pre.setString(3, subGroupCode);
		if (txtItemImage.getText().toString().trim().isEmpty())
		{
		    pre.setString(38, "");
		    pre.setString(4, "");
		}
		else
		{
		    pre.setBinaryStream(38, (InputStream) fileInImg, (int) tempFile.length());
		    pre.setString(4, userImagefilePath);
		}
		pre.setString(5, taxIndicator);
		pre.setString(6, stkInFlag);
		pre.setString(7, txtPurRate.getText());
		pre.setInt(8, intProcTimeMin);
		pre.setString(9, txtExtCode.getText());
		pre.setString(10, clsGlobalVarClass.gItemDetails);
		pre.setString(11, clsGlobalVarClass.gUserCode);
		pre.setString(12, clsGlobalVarClass.gUserCode);
		pre.setString(13, clsGlobalVarClass.getCurrentDateTime());
		pre.setString(14, clsGlobalVarClass.getCurrentDateTime());
		pre.setString(15, clsGlobalVarClass.gClientCode);
		pre.setString(16, "N");
		pre.setString(17, itemType);
		pre.setString(18, discountApply);
		pre.setString(19, txtShortName1.getText().trim());
		pre.setDouble(20, minLevel);
		pre.setDouble(21, maxLevel);
		pre.setInt(22, intProcday);
		pre.setString(23, rawMaterial);
		pre.setString(24, txtSalePrice.getText());
		pre.setString(25, itemForSale);
		pre.setString(26, cmbRevenueHead.getSelectedItem().toString().trim());
		pre.setString(27, txtItemWeight.getText());
		pre.setString(28, openItem);
		pre.setString(29, itemWiseKOT);
		pre.setString(30, txtWSProductCode.getText());
		pre.setString(31, txtExciseBrandCode.getText());
		pre.setString(32, noDeliverydays);
		pre.setInt(33, intDeliveryDays);
		pre.setString(34, txtItemWeight.getText());
		pre.setString(35, txtMinItemWeight.getText());
		pre.setString(36, urgentOrder);
		pre.setString(37, unitOfMeasurement);
		pre.setInt(39, intTargetMissedTimeMin);
		pre.setString(40, recipeUOM);
		pre.setString(41, receivedConversion);
		pre.setString(42, recipeConversion);
		pre.setString(43, hsnNo);
		pre.setString(44, operationalYN);
		pre.setString(45, "");
		int exc = pre.executeUpdate();
		pre.close();

		funSaveDataToItemLinkupTable();
		funSaveDataToItemOrderTypeLinkupTable();
		funSaveDataToItemCharLinkupTable();

		clsInvokeDataFromSanguineERPModules obj = new clsInvokeDataFromSanguineERPModules();
		if (clsGlobalVarClass.gSanguineWebServiceURL.contains("prjSanguineWebService"))
		{
		    boolean flgMMMSCon = false;
		    try
		    {
			String flgCon = obj.funMMSConsectionEstablished();
			flgMMMSCon = Boolean.parseBoolean(flgCon);
		    }
		    catch (Exception ex)
		    {
			ex.printStackTrace();
		    }

		    if (flgMMMSCon)
		    {
			String wsSGCode="";
			String sqlSGMaster="select strWSSubGroupCode from tblsubgroupmasterlinkupdtl where strSubGrooupCode='" + subGroupCode + "'  ";
			ResultSet rsSGMaster = clsGlobalVarClass.dbMysql.executeResultSet(sqlSGMaster);
			while (rsSGMaster.next())
			{
			   wsSGCode=rsSGMaster.getString(1);
			}
			rsSGMaster.close();
			String sqlPOS = " select strPOSCode from tblposmaster  ";
			ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
			String mmsProdCode = obj.funCreateProductInMMS(txtItemCode.getText(), txtItemName.getText(), clsGlobalVarClass.gClientCode, clsGlobalVarClass.getCurrentDateTime(), clsGlobalVarClass.gUserCode,"",wsSGCode);
			while (rsPOS.next())
			{
			    String insertQuery = "insert into tblitemmasterlinkupdtl (strItemCode,strWSProductCode,strWSProductName,strPOSCode,strClientCode,strDataPostFlag) values "
				    + " ('" + txtItemCode.getText() + "','" + mmsProdCode + "','" + txtItemName.getText() + "','" + rsPOS.getString(1) + "','" + clsGlobalVarClass.gClientCode + "','N' )";
			    clsGlobalVarClass.dbMysql.execute(insertQuery);

			}
			rsPOS.close();
		    }

		}

		if (txtRecipeCode.getText().equalsIgnoreCase(""))
		{
		    if (!txtChildItemName.getText().equalsIgnoreCase(""))
		    {
			funSaveRecipeMaster();
		    }
		}
		else
		{
		    funUpdateRecipeMaster();
		}

		if (exc > 0)
		{
		    //new frmOkPopUp(this,"Entry added Successfully", "Successfull",3).setVisible(true);
		    //funCopyImageIfPresent();

		    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + " where strTableName='MenuItem'";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    JOptionPane.showMessageDialog(this, "Entry added Successfully");
		    if ((rawMaterial.equals("N") || (rawMaterial.equals("Y") && itemForSale.equals("Y"))) && clsGlobalVarClass.gPriceFrom.equals("Menu Pricing"))
		    {
			int res = JOptionPane.showConfirmDialog(this, "Do you want to price this item ?");
			if (res == 0)
			{
			    dispose();
			    clsGlobalVarClass.gItemCodeforPricing = txtItemCode.getText();
			    clsGlobalVarClass.gPriceItem = true;
			    new frmMenuItemPricing().setVisible(true);
			}
		    }
		    funResetField();
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }


    //Generate ItemCode for new entry 
    private String funGenerateItemCode() throws Exception
    {
	String itemCode = "", code = "";
	String selectQuery = "select count(*) from tblitemmaster";//updated
	ResultSet recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	recordSet.next();
	int cn = recordSet.getInt(1);
	recordSet.close();

	if (cn > 0)
	{
	    selectQuery = "select max(strItemCode) from tblitemmaster";
	    recordSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    recordSet.next();
	    code = recordSet.getString(1);
	    StringBuilder sb = new StringBuilder(code);
	    String ss = sb.delete(0, 1).toString();
	    for (int i = 0; i < ss.length(); i++)
	    {
		if (ss.charAt(i) != '0')
		{
		    strCode = ss.substring(i, ss.length());
		    break;
		}
	    }

	    int intCode = Integer.parseInt(strCode);
	    intCode++;
	    if (intCode < 10)
	    {
		itemCode = "I00000" + intCode;
	    }
	    else if (intCode < 100)
	    {
		itemCode = "I0000" + intCode;
	    }
	    else if (intCode < 1000)
	    {
		itemCode = "I000" + intCode;
	    }
	    else if (intCode < 10000)
	    {
		itemCode = "I00" + intCode;
	    }
	    else if (intCode < 100000)
	    {
		itemCode = "I0" + intCode;
	    }
	    else if (intCode < 1000000)
	    {
		itemCode = "I" + intCode;
	    }
	}
	else
	{
	    itemCode = "I000001";
	}

	return itemCode;
    }

    //Save linking WS product Code with itemmaster in tblitemorderingdtl table
    private void funSaveDataToItemLinkupTable() throws Exception
    {
	String deleteQuery = " delete from tblitemmasterlinkupdtl where strItemCode='" + txtItemCode.getText().trim() + "' ";
	clsGlobalVarClass.dbMysql.execute(deleteQuery);
	if (tblItemCodeLinkup.getRowCount() > 0)
	{
	    String insertQuery = "insert into tblitemmasterlinkupdtl (strItemCode,strWSProductCode,strWSProductName,strPOSCode,strClientCode,strDataPostFlag) values ";
	    for (int row = 0; row < tblItemCodeLinkup.getRowCount(); row++)
	    {
		if (row == 0)
		{
		    insertQuery += "('" + tblItemCodeLinkup.getValueAt(row, 3) + "','" + tblItemCodeLinkup.getValueAt(row, 4) + "','" + tblItemCodeLinkup.getValueAt(row, 1) + "','" + tblItemCodeLinkup.getValueAt(row, 5) + "', '" + clsGlobalVarClass.gClientCode + "', 'N') ";
		}
		else
		{
		    insertQuery += ",('" + tblItemCodeLinkup.getValueAt(row, 3) + "','" + tblItemCodeLinkup.getValueAt(row, 4) + "','" + tblItemCodeLinkup.getValueAt(row, 1) + "','" + tblItemCodeLinkup.getValueAt(row, 5) + "', '" + clsGlobalVarClass.gClientCode + "', 'N')";
		}
	    }
	    clsGlobalVarClass.dbMysql.execute(insertQuery);
	}
    }

//Save linking Order with itemmaster in tblitemorderingdtl table
    private void funSaveDataToItemOrderTypeLinkupTable() throws Exception
    {
	String deleteQuery = " delete from tblitemorderingdtl where strItemCode='" + txtItemCode.getText().trim() + "' ";
	clsGlobalVarClass.dbMysql.execute(deleteQuery);

	if (tblOrderDetails.getRowCount() > 0)
	{
	    String insertQuery = "insert into tblitemorderingdtl (strItemCode,strPOSCode,strOrderCode,strClientCode,strDataPostFlag) values ";
	    for (int row = 0; row < tblOrderDetails.getRowCount(); row++)
	    {
		if (row == 0)
		{
		    insertQuery += "('" + tblOrderDetails.getValueAt(row, 2) + "', 'All','" + tblOrderDetails.getValueAt(row, 3) + "','" + clsGlobalVarClass.gClientCode + "', 'N') ";
		}
		else
		{
		    insertQuery += ",('" + tblOrderDetails.getValueAt(row, 2) + "','All', '" + tblOrderDetails.getValueAt(row, 3) + "', '" + clsGlobalVarClass.gClientCode + "', 'N')";
		}
	    }
	    clsGlobalVarClass.dbMysql.execute(insertQuery);
	}
    }

    //Save linking Characters with itemmaster in tblitemcharactersticslinkupdtl table
    private void funSaveDataToItemCharLinkupTable() throws Exception
    {
	int cnt = 0;
	String deleteQuery = " delete from tblitemcharctersticslinkupdtl where strItemCode='" + txtItemCode.getText().trim() + "' ";
	clsGlobalVarClass.dbMysql.execute(deleteQuery);
	if (hmCharValues.size() > 0)
	{
	    //System.out.println("Save Item Count= "+hmCharValues.size());

	    String insertQuery = "insert into tblitemcharctersticslinkupdtl (strItemCode,strCharCode,strCharValue,strPOSCode,strClientCode,strDataPostFlag) values ";
	    for (Map.Entry<String, List<String>> entry : hmCharValues.entrySet())
	    {
		List<String> listOfChar = entry.getValue();
		for (int i = 0; i < listOfChar.size(); i++)
		{
		    if (cnt == 0)
		    {
			insertQuery += "('" + txtItemCode.getText().toString() + "', '" + entry.getKey() + "','" + listOfChar.get(i) + "','All','" + clsGlobalVarClass.gClientCode + "','N') ";
		    }
		    else
		    {
			insertQuery += ",('" + txtItemCode.getText().toString() + "','" + entry.getKey() + "', '" + listOfChar.get(i) + "', 'All','" + clsGlobalVarClass.gClientCode + "','N')";
		    }
		    cnt++;
		}
	    }

	    if (cnt != 0)
	    {
		//System.out.println("insertQuery="+insertQuery);
		clsGlobalVarClass.dbMysql.execute(insertQuery);
	    }
	}
    }

    /**
     * This method is used to update item master
     */
    private void funUpdateItemMaster()
    {
	try
	{
	    String itemName = txtItemName.getText().trim();
	    String itemShortName = txtShortName1.getText().trim();
	    String code = txtItemCode.getText().trim();

	    if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strItemName", "strItemCode", itemName, code, "update", ""))
	    {
		JOptionPane.showMessageDialog(this, "Item Name is Already Exsist");
		txtItemName.requestFocus();
		return;
	    }
	    if (!txtShortName1.getText().isEmpty())
	    {
		if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strShortName", "strItemCode", itemShortName, code, "update", ""))
		{
		    JOptionPane.showMessageDialog(this, "Item Short Name is Already Exsist");
		    return;
		}
		if (!objUtility.funCheckLength(txtShortName1.getText(), 20))
		{
		    new frmOkPopUp(this, "Item Short Name length must be less than 20", "Error", 0).setVisible(true);
		    txtShortName1.requestFocus();
		}
	    }
	    if (txtQty.getText().trim().length() == 0)
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Quantity in Quantity Field!");
		txtQty.requestFocus();
		return;
	    }

	    /*
             * if(funCheckProductCodeForItem()) {
             * JOptionPane.showMessageDialog(this, "This product code already
             * used for item from selected pos"); return; }
	     */
	    String subGroupCode = "";
	    for (int k = 0; k < subGroupNames.length; k++)
	    {
		if (subGroupNames[k].equals(cmbSubGroupCode.getSelectedItem().toString()))
		{
		    subGroupCode = subGroupCodes[k];
		    break;
		}
	    }
	    taxIndicator = cmbTaxIndicator.getSelectedItem().toString();

	    if (!clsGlobalVarClass.validateEmpty(txtItemName.getText()))
	    {
		new frmOkPopUp(this, "Please Enter Item Name", "Error", 0).setVisible(true);
		txtItemName.requestFocus();
		return;
	    }
	    if (!objUtility.funCheckLength(txtItemName.getText(), 50))
	    {
		new frmOkPopUp(this, "Item Name length must be less than 30", "Error", 0).setVisible(true);
		txtItemName.requestFocus();
		return;
	    }
	    if (cmbItemType.getSelectedIndex() == 0)
	    {

		JOptionPane.showMessageDialog(this, "Please Select Item Type");
		cmbItemType.requestFocus();
		return;
	    }
	    if (!objUtility.funCheckLength(txtExtCode.getText(), 15))
	    {
		new frmOkPopUp(this, "External Code length must be less than 15", "Error", 0).setVisible(true);
		txtExtCode.requestFocus();
		return;
	    }
	    if (!objUtility.funCheckLength(txtPurRate.getText(), 21))
	    {
		new frmOkPopUp(this, "Purchase Rate length must be less than 6", "Error", 0).setVisible(true);
		txtPurRate.requestFocus();
		return;
	    }
	    if (txtMaxlevel.getText().trim().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Short Name");
		txtMaxlevel.setFocusable(true);
		return;
	    }
	    if (txtMaxlevel.getText().trim().length() > 8)
	    {
		JOptionPane.showMessageDialog(this, "Length of the Short Name Must Be 8 characters");
		txtMaxlevel.setFocusable(true);
		return;
	    }
	    else
	    {
		if (txtExtCode.getText().trim().length() > 0)
		{
		    sql = "select count(*) from tblitemmaster where strExternalCode='" + txtExtCode.getText().trim() + "' "
			    + "and strItemCode!='" + txtItemCode.getText().trim() + "'";
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    rs.next();
		    int found = rs.getInt(1);
		    rs.close();
		    if (found > 0)
		    {
			new frmOkPopUp(this, "External Code Already Used", "Error", 0).setVisible(true);
			txtExtCode.requestFocus();
			return;
		    }
		}
		if (chkStkInEnable.isSelected())
		{
		    stkInFlag = "Y";
		}
		else
		{
		    stkInFlag = "N";
		}
		discountApply = "Y";
		if (chkDiscount.isSelected() == true)
		{
		    discountApply = "N";
		}

		String rawMaterial = "N";
		if (chkRawMaterial.isSelected())
		{
		    rawMaterial = "Y";
		}
		else
		{
		    rawMaterial = "N";
		}

		String itemForSale = "N";
		if (chkItemForSale.isSelected())
		{
		    itemForSale = "Y";
		}
		String openItem = "N";
		if (chkOpenItem.isSelected())
		{
		    openItem = "Y";
		}

		String itemWiseKOT = "N";
		if (chkItemWiseKOTYN.isSelected())
		{
		    itemWiseKOT = "Y";
		}

		String noDeliverydays = "";

		if (chkSunday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Sunday";
		}
		if (chkMonday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Monday";
		}
		if (chkTuesday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Tuesday";
		}
		if (chkWed.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Wednesday";
		}
		if (chkThursday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Thursday";
		}
		if (chkFriday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Friday";
		}
		if (chkSaturday.isSelected())
		{
		    noDeliverydays = noDeliverydays + ",Saturday";
		}
		if (noDeliverydays.isEmpty())
		{
		    noDeliverydays = "NA";
		}
		String urgentOrder = "N";
		if (chkUrgentOrder.isSelected())
		{
		    urgentOrder = "Y";
		}

		String receivedUOM = cmbReceivedUOM.getSelectedItem().toString();
		String recipeUOM = cmbRecipeUOM.getSelectedItem().toString();

		if (rawMaterial.equalsIgnoreCase("Y"))
		{
		    if (receivedUOM.isEmpty() || recipeUOM.isEmpty())
		    {
			JOptionPane.showMessageDialog(this, "Please select received and recipe UOM");
			return;
		    }
		}

		StringBuilder sb = new StringBuilder(noDeliverydays);
		sb = sb.delete(0, 1);
		noDeliverydays = sb.toString();

		String itemType = cmbItemType.getSelectedItem().toString();
		int intProcday = Integer.parseInt(cmbProcessingDay.getSelectedItem().toString());
		double minLevel = Double.valueOf(txtMinlevel.getText());
		double maxLevel = Double.valueOf(txtMaxlevel.getText());
		int intProcTimeMin = Integer.parseInt(cmbProcessingTme.getSelectedItem().toString());
		int intTargetMissedTimeMin = Integer.parseInt(cmbTargetMissedTime.getSelectedItem().toString());
		int intDeliveryDays = Integer.parseInt(txtRequiredProductDeliveryDays.getText().toString());

		String hsnNo = txtHSNNo.getText().trim();
		String operationalYN = "N";
		if (chkOperationalYN.isSelected())
		{
		    operationalYN = "Y";
		}

		updateQuery = "Update tblitemmaster SET strItemName=? ,strSubGroupCode=? ,"
			+ " strItemImage=?,strTaxIndicator=?,strStockInEnable=?,strUserEdited=?,dteDateEdited=?,dblPurchaseRate=?,intProcTimeMin=?,strExternalCode=?,strItemDetails=?,strDataPostFlag=? ,"
			+ " strItemType=?,strDiscountApply=?,strShortName=?,dblMinLevel=?,dblMaxLevel=?,intProcDay=?,strRawMaterial=?,dblSalePrice=?,strItemForSale=?,strRevenueHead=?,strItemWeight=?,strOpenItem=? , "
			+ " strItemWiseKOTYN=?,strWSProdCode=?,strExciseBrandCode=?,strNoDeliveryDays=?,intDeliveryDays=?,dblIncrementalWeight=?,dblMinWeight=?,strUrgentOrder=?,strUOM=?"
			+ ",imgImage=?,tmeTargetMiss=?,strRecipeUOM=?,dblReceivedConversion=?,dblRecipeConversion=?,strHSNNo=?,strOperationalYN=? "
			+ "WHERE strItemCode =? ";

		PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(updateQuery);
		pre.setString(1, txtItemName.getText());
		pre.setString(2, subGroupCode);

		pre.setString(4, taxIndicator);
		pre.setString(5, stkInFlag);
		pre.setString(6, clsGlobalVarClass.gUserCode);
		pre.setString(7, clsGlobalVarClass.getCurrentDateTime());
		pre.setString(8, txtPurRate.getText());
		pre.setInt(9, intProcTimeMin);
		pre.setString(10, txtExtCode.getText());
		pre.setString(11, clsGlobalVarClass.gItemDetails);
		pre.setString(12, "N");
		pre.setString(13, itemType);
		pre.setString(14, discountApply);
		pre.setString(15, txtShortName1.getText().trim());
		pre.setDouble(16, minLevel);
		pre.setDouble(17, maxLevel);
		pre.setInt(18, intProcday);
		pre.setString(19, rawMaterial);
		pre.setString(20, txtSalePrice.getText());
		pre.setString(21, itemForSale);
		pre.setString(22, cmbRevenueHead.getSelectedItem().toString().trim());
		pre.setString(23, txtItemWeight.getText());
		pre.setString(24, openItem);
		pre.setString(25, itemWiseKOT);
		pre.setString(26, txtWSProductCode.getText());
		pre.setString(27, txtExciseBrandCode.getText());
		pre.setString(28, noDeliverydays);
		pre.setInt(29, intDeliveryDays);
		pre.setString(30, txtItemWeight.getText());
		pre.setString(31, txtMinItemWeight.getText());
		pre.setString(32, urgentOrder);
		pre.setString(33, receivedUOM);
		pre.setInt(35, intTargetMissedTimeMin);

		pre.setString(36, recipeUOM);
		pre.setString(37, txtReceivedConversion.getText().toString());
		pre.setString(38, txtRecipeConversion.getText().toString());
		pre.setString(39, hsnNo);
		pre.setString(40, operationalYN);

		pre.setString(41, txtItemCode.getText());

		FileInputStream fileInputStream = null;
		if (txtItemImage.getText().toString().trim().isEmpty())
		{
		    pre.setString(34, "");
		    pre.setString(3, "");
		}
		else
		{
		    if (tempFile != null)
		    {
			strPath = tempFile.getAbsolutePath();
		    }
		    else
		    {
			strPath = txtItemImage.getText();

		    }

		    String extension = "";
		    int i = strPath.lastIndexOf('.');
		    if (i > 0)
		    {
			extension = strPath.substring(i + 1);
		    }

		    File fileItemImage = new File(System.getProperty("user.dir") + "\\itemImages\\" + txtItemCode.getText().trim() + "." + extension);

		    if (tempFile == null)
		    {
			String imagePath = fileItemImage.getAbsolutePath();
			fileInputStream = new FileInputStream(fileItemImage);
			pre.setBinaryStream(34, (InputStream) fileInputStream, (int) fileItemImage.length());
			pre.setString(3, imagePath);
		    }

		    else
		    {
			String imagePath = tempFile.getAbsolutePath();
			fileInImg = new FileInputStream(tempFile);
			pre.setBinaryStream(34, (InputStream) fileInImg, (int) tempFile.length());
			pre.setString(3, imagePath);
		    }

		}
		int exc = pre.executeUpdate();
		pre.close();
		if (fileInputStream != null)
		{
		    fileInputStream.close();
		}
		//System.out.println("updateQuery:"+updateQuery);
		//int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
		if (rawMaterial.equals("N"))
		{
		    updateQuery = "update tblmenuitempricingdtl set strItemName = '" + txtItemName.getText()
			    + "' WHERE strItemCode ='" + txtItemCode.getText() + "'";
		    int exc1 = clsGlobalVarClass.dbMysql.execute(updateQuery);
		    funSaveDataToItemLinkupTable();
		    funSaveDataToItemOrderTypeLinkupTable();
		    funSaveDataToItemCharLinkupTable();
		    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + " where strTableName='MenuItem'";
		    System.out.println(sql);
		    clsGlobalVarClass.dbMysql.execute(sql);

		}

		if (txtRecipeCode.getText().toString().equalsIgnoreCase(""))
		{
		    if (!txtChildItemName.getText().equalsIgnoreCase(""))
		    {
			funSaveRecipeMaster();
		    }
		}
		else
		{
		    funUpdateRecipeMaster();
		}

		clsInvokeDataFromSanguineERPModules obj = new clsInvokeDataFromSanguineERPModules();
		if (clsGlobalVarClass.gSanguineWebServiceURL.contains("prjSanguineWebService"))
		{
		    boolean flgMMMSCon = false;
		    try
		    {
			String flgCon = obj.funMMSConsectionEstablished();
			flgMMMSCon = Boolean.parseBoolean(flgCon);
		    }
		    catch (Exception ex)
		    {
			ex.printStackTrace();
		    }

		     if (flgMMMSCon)
		    {
			String wsSGCode="";
			String sqlSGMaster="select strWSSubGroupCode from tblsubgroupmasterlinkupdtl where strSubGrooupCode='" + subGroupCode + "'  ";
			ResultSet rsSGMaster = clsGlobalVarClass.dbMysql.executeResultSet(sqlSGMaster);
			while (rsSGMaster.next())
			{
			   wsSGCode=rsSGMaster.getString(1);
			}
			rsSGMaster.close();
			
			
			String wsProductCode="";
			String sqlItemMaster="select strWSProductCode from tblitemmasterlinkupdtl where strItemCode='" + txtItemCode.getText() + "'  ";
			ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemMaster);
			while (rsItemMaster.next())
			{
			   wsProductCode=rsItemMaster.getString(1);
			}
			rsItemMaster.close();
			
			String sqlPOS = " select strPOSCode from tblposmaster  ";
			ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
			
			
			String mmsProdCode = obj.funCreateProductInMMS(txtItemCode.getText(), txtItemName.getText(), clsGlobalVarClass.gClientCode, clsGlobalVarClass.getCurrentDateTime(), clsGlobalVarClass.gUserCode,wsProductCode,wsSGCode);
			while (rsPOS.next())
			{
			    clsGlobalVarClass.dbMysql.execute("delete from tblitemmasterlinkupdtl where strItemCode='" + txtItemCode.getText() + "' ");

			    String insertQuery = "insert into tblitemmasterlinkupdtl (strItemCode,strWSProductCode,strWSProductName,strPOSCode,strClientCode,strDataPostFlag) values "
				    + " ('" + txtItemCode.getText() + "','" + mmsProdCode + "','" + txtItemName.getText() + "','" + rsPOS.getString(1) + "','" + clsGlobalVarClass.gClientCode + "','N' )";
			    clsGlobalVarClass.dbMysql.execute(insertQuery);

			}
			rsPOS.close();
		    }

		}

		if (exc > 0)
		{
		    funCopyImageIfPresent();
		    JOptionPane.showMessageDialog(this, "Updated Successfully");
		    if ((rawMaterial.equals("N") || (rawMaterial.equals("Y") && itemForSale.equals("Y"))) && clsGlobalVarClass.gPriceFrom.equals("Menu Pricing"))
		    {
			String sqlPricedItem = "select count(*) from tblmenuitempricingdtl where strItemCode ='" + txtItemCode.getText() + "'";
			ResultSet rsPricedItem = clsGlobalVarClass.dbMysql.executeResultSet(sqlPricedItem);
			rsPricedItem.next();
			int pricedItemFound = rsPricedItem.getInt(1);
			if (pricedItemFound <= 0)
			{
			    int res = JOptionPane.showConfirmDialog(this, "Do you want to price this item ?");
			    if (res == 0)
			    {
				dispose();
				clsGlobalVarClass.gItemCodeforPricing = txtItemCode.getText();
				clsGlobalVarClass.gPriceItem = true;
				new frmMenuItemPricing().setVisible(true);
			    }
			}
		    }
		    funResetField();
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
     * This method is used to save or update master
     */
    private void funSaveAndUpdate()
    {
	try
	{
	   
	    if (btnNew.getText().equalsIgnoreCase("SAVE"))
	    {
		funSaveItemMaster();

	    }
	    else
	    {
		funUpdateItemMaster();

	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to check external code
     *
     * @param exCode
     * @return boolean
     */
    private boolean funCheckExternalCode(String exCode)
    {
	boolean flg = false;
	try
	{
	    String sql = "select count(*) from tblitemmaster where strExternalCode='" + exCode + "' "
		    + "and strItemCode!='" + txtItemCode.getText() + "'";
	    ResultSet rsDuplicateExCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsDuplicateExCode.next();
	    int cnt = rsDuplicateExCode.getInt(1);
	    if (cnt == 0)
	    {
		flg = true;
	    }
	    rsDuplicateExCode.close();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	return flg;
    }

    /**
     * Ritesh 02 oct 2014
     */
    private void funCreateitemImagesFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/itemImages");
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
	String src = source.toString();
	String destination = dest.toString();
//        RandomAccessFile randomAccessFile1 = new RandomAccessFile(tempFile, "rw");
//        FileChannel fileChannel1 = randomAccessFile1.getChannel();
//
//        RandomAccessFile randomAccessFile2 = new RandomAccessFile(destFile, "rw");
//        FileChannel fileChannel2 = randomAccessFile2.getChannel();
//        fileChannel2.tryLock();
//
//        FileLock lock1 = null;
//        FileLock lock2 = null;
//        try {
//            lock1 = fileChannel1.tryLock();
//
//            lock1 = fileChannel2.tryLock();
//            // Ok. You get the lock
//        } catch (OverlappingFileLockException e) {
//            // File is open by someone else
//        } finally {
//
//            if (lock1 != null) {
//                lock1.release();
//            }
//
//            if (lock2 != null) {
//                lock2.release();
//            }
//
//        }
//
//        fileChannel1.close();
//
//        fileChannel2.close();
	try
	{

	    boolean bool = false;
	    bool = dest.delete();
	    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            bool = dest.delete();
	    //,StandardCopyOption.REPLACE_EXISTING);
	} //       catch (FileSystemException  e1) 
	//       {
	//          e1.printStackTrace();
	//       }
	catch (FileAlreadyExistsException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	//,StandardCopyOption.REPLACE_EXISTING
	tempFile = null;
	destFile = null;

	System.gc();

    }

    /**
     * This method is used to copy image if present
     *
     * @throws IOException
     */
    private void funCopyImageIfPresent() throws IOException
    {
	if (null != tempFile && null != bttImage.getIcon())
	{
	    String imagePath = tempFile.getAbsolutePath();
	    String extension = "";

	    int i = imagePath.lastIndexOf('.');
	    if (i > 0)
	    {
		extension = imagePath.substring(i + 1);
	    }

	    String filePath = System.getProperty("user.dir");
	    funCreateitemImagesFolder();
	    fileInImg.close();
//            String string = txtItemCode.getText().trim();
//            String[] parts = string.split("-");
//            String part1 = parts[0]; // 004
//            String part2 = parts[1]; // 034556
	    destFile = new File(filePath + "/itemImages/" + txtItemCode.getText().trim() + "." + extension);
	    if (destFile.exists())
	    {
		destFile.setExecutable(true);
		destFile.setWritable(true);

		destFile.delete();
	    }

	    copyImageFiles(tempFile, destFile);

	    userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
	    // txtUserImage.setText(imgFile.getAbsolutePath());

//            try
//            {
//                String query = "update tblitemmaster set strItemImage =?,imgImage=? WHERE strItemCode =? ";
//                PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
//                pre.setString(3, txtItemCode.getText());
//
//                String imageFilePath = System.getProperty("user.dir") + "\\itemImages\\" + txtItemCode.getText().trim() + ".jpg";
//
////                File fileUserImage = new File(imageFilePath);
////
////                if (fileUserImage.exists())
////                {
////                    pre.setString(1, imageFilePath);
////                }
//                if (txtItemImage.getText().trim().isEmpty())
//                {
//                    pre.setString(2, "");
//                    pre.setString(1, "");
//                }
//                else
//                {
//                    pre.setBinaryStream(2, (InputStream) fileInImg, (int) tempFile.length());
//                    pre.setString(1, userImagefilePath);
//                }
//                pre.executeUpdate();
//                pre.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
	}
    }

    /**
     * This method is used to set image
     */
    private void funSetImage()
    {
	try
	{
	    String imgCode = txtItemCode.getText().trim();

	    String extension = "";

	    int i = strPath.lastIndexOf('.');
	    if (i > 0)
	    {
		extension = strPath.substring(i + 1);
	    }
	    if (imgCode.length() > 0)
	    {
		String filePath = System.getProperty("user.dir");
		File f = new File(filePath + "/itemImages/" + imgCode + "." + extension);
		ImageIcon icon1 = new ImageIcon(ImageIO.read(f));
		bttImage.setIcon(icon1);
		// txtItemImage.setText(userImagefilePath);
	    }
	}
	catch (Exception e)
	{
	    bttImage.setText("NO IMAGE");
	    bttImage.setIcon(null);
	}

    }

    /**
     * This method is used to get image icon
     *
     * @param itemCode
     * @return image icon
     */
    private ImageIcon getImageIcon(String itemCode)
    {
	ImageIcon icon = null;
	try
	{
	    if (itemCode.length() > 0)
	    {
		String filePath = System.getProperty("user.dir");
		File f = new File(filePath + "/itemImages/" + itemCode + ".jpg");
		icon = new ImageIcon(ImageIO.read(f));
	    }
	}
	catch (Exception e)
	{
	    icon = null;
	}
	return icon;
    }

    //help for fetching product code
    private void funProductCodeTextFieldClicked()
    {
	clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
	try
	{
	    List<clsLinkupDtl> listProducts = objLinkSangERP.funGetProductDtl(clsGlobalVarClass.gWSClientCode);

	    List<String> listColumns = new ArrayList<String>();
	    listColumns.add("Product Code");
	    listColumns.add("Product Name");
	    new frmSearchFormDialog(this, true, listProducts, "MMS Products", listColumns).setVisible(true);

	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		txtWSProductCode.setText(data[0].toString());
		txtWSProductName.setText(data[1].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    objLinkSangERP = null;
	}
    }

    private boolean funCheckProductCodeForItem() throws Exception
    {
	boolean flgResult = false;
	String query = " select strItemCode from tblitemmasterlinkupdtl where strWSProductCode='" + txtWSProductCode.getText() + "' and strPOSCode='" + hmPOS.get(cmbPosCode.getSelectedItem().toString()) + "'; ";
	ResultSet rsCheckProdCode = clsGlobalVarClass.dbMysql.executeResultSet(query);
	if (rsCheckProdCode.next())
	{
	    flgResult = true;
	}
	rsCheckProdCode.close();
	return flgResult;
    }

    private boolean funCheckDuplicateProductCodeandPOSForItem() throws Exception
    {
	boolean flgResult = false;

	if (tblItemCodeLinkup.getRowCount() > 0)
	{
	    for (int row = 0; row < tblItemCodeLinkup.getRowCount(); row++)
	    {
		if (tblItemCodeLinkup.getValueAt(row, 3).equals(txtItemCode.getText().toString()) && tblItemCodeLinkup.getValueAt(row, 4).equals(txtWSProductCode.getText().toString()) && tblItemCodeLinkup.getValueAt(row, 5).equals(hmPOS.get(cmbPosCode.getSelectedItem().toString())))
		{
		    flgResult = true;
		}
	    }
	}
	return flgResult;
    }

    //help for fetching brand code
    private void funBrandCodeTextFieldClicked()
    {
	clsInvokeDataFromSanguineERPModules objLinkSangERP = new clsInvokeDataFromSanguineERPModules();
	try
	{
	    List<clsLinkupDtl> listBrands = objLinkSangERP.funGetBrandDtls(clsGlobalVarClass.gClientCode);

	    List<String> listColumns = new ArrayList<String>();
	    listColumns.add("Brand Code");
	    listColumns.add("Brand Name");
	    new frmSearchFormDialog(this, true, listBrands, "Excise Brands", listColumns).setVisible(true);

	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		txtExciseBrandCode.setText(data[0].toString());
		clsGlobalVarClass.gSearchItemClicked = false;
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
	finally
	{
	    objLinkSangERP = null;
	}
    }

    //Fill Item Linkup with WS Product Code table   
    private void funLoadItemLinkupTable()
    {
	try
	{
	    /*
             * String sql = " select
             * b.strItemName,a.strWSProductName,c.strPosName,a.strItemCode,a.strWSProductCode,a.strPOSCode
             * " + " from tblitemmasterlinkupdtl a,tblitemmaster b,tblposmaster
             * c " + " where (a.strPOSCode=c.strPosCode or a.strPOSCode='All')
             * and a.strItemCode=b.strItemCode " + " and
             * a.strItemCode='"+txtItemCode.getText().toString()+"' ";
	     */
	    String sql = " select b.strItemName,a.strWSProductName,ifnull(c.strPosName,'All'),a.strItemCode"
		    + " ,a.strWSProductCode,a.strPOSCode "
		    + " from tblitemmasterlinkupdtl a inner join tblitemmaster b on a.strItemCode=b.strItemCode "
		    + " left outer join tblposmaster c on a.strPOSCode=c.strPosCode "
		    + " where a.strItemCode='" + txtItemCode.getText().toString() + "' ";
	    //System.out.println(sql);
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    dmItemLinkup.setRowCount(0);
	    while (rs.next())
	    {
		Object[] column = new Object[7];
		column[0] = rs.getString(1);
		column[1] = rs.getString(2);
		column[2] = rs.getString(3);
		column[3] = rs.getString(4);
		column[4] = rs.getString(5);
		column[5] = rs.getString(6);
		column[6] = false;
		dmItemLinkup.addRow(column);
	    }
	    rs.close();
	    tblItemCodeLinkup.setModel(dmItemLinkup);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    tblItemCodeLinkup.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	    tblItemCodeLinkup.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
	    tblItemCodeLinkup.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

	    tblItemCodeLinkup.getColumnModel().getColumn(0).setPreferredWidth(150);
	    tblItemCodeLinkup.getColumnModel().getColumn(1).setPreferredWidth(150);
	    tblItemCodeLinkup.getColumnModel().getColumn(2).setPreferredWidth(150);

	    tblItemCodeLinkup.setSize(700, 900);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funInsertItemLinkupRow()
    {
	try
	{

	    Object row[] = new Object[]
	    {
		txtItemName.getText(), txtWSProductName.getText(), cmbPosCode.getSelectedItem().toString(),
		txtItemCode.getText(), txtWSProductCode.getText(), hmPOS.get(cmbPosCode.getSelectedItem().toString())
	    };
	    dmItemLinkup.addRow(row);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    tblItemCodeLinkup.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	    tblItemCodeLinkup.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
	    tblItemCodeLinkup.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

	    tblItemCodeLinkup.getColumnModel().getColumn(0).setPreferredWidth(150);
	    tblItemCodeLinkup.getColumnModel().getColumn(1).setPreferredWidth(150);
	    tblItemCodeLinkup.getColumnModel().getColumn(2).setPreferredWidth(150);
	    tblItemCodeLinkup.setSize(700, 900);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funLinkupTableRowSelection()
    {
	int rowNo = tblItemCodeLinkup.getSelectedRow();
	String rowValue = tblItemCodeLinkup.getValueAt(rowNo, 6).toString();
	if (Boolean.parseBoolean(rowValue))
	{
	    btnRemove.setEnabled(true);
	}

	boolean flgSelect = false;
	int total = tblItemCodeLinkup.getRowCount();
	for (int i = 0; i < total; i++)
	{
	    String rowValue1 = tblItemCodeLinkup.getValueAt(i, 6).toString();
	    if (Boolean.parseBoolean(rowValue1))
	    {
		flgSelect = true;
		break;
	    }
	}
	if (!flgSelect)
	{
	    btnRemove.setEnabled(false);
	}

	rowNo = 0;
    }

    private void funRemoveRow()
    {
	/*
         * int rowNo = tblItemCodeLinkup.getRowCount(); java.util.Vector
         * vIndexToDelete = new java.util.Vector(); for (int i = 0; i < rowNo;
         * i++) { boolean select =
         * Boolean.parseBoolean(tblItemCodeLinkup.getValueAt(i, 6).toString());
         * if (select) { vIndexToDelete.add(i);
         *
         * }
         * }
         * int cnt = 0; while (cnt < tblItemCodeLinkup.getRowCount()) { boolean
         * select = Boolean.parseBoolean(tblItemCodeLinkup.getValueAt(cnt,
         * 6).toString()); if (select) { if (tblItemCodeLinkup.getRowCount() >
         * 0) { dmItemLinkup.removeRow(cnt); } } else { cnt++; } }
         * btnRemove.setEnabled(false);
	 */

	boolean isItemSelected = false;
	List<String> listDeleteRows = new ArrayList<>();
	for (int i = 0; i < tblItemCodeLinkup.getRowCount(); i++)
	{
	    boolean isSelected = Boolean.parseBoolean(tblItemCodeLinkup.getValueAt(i, 6).toString());
	    if (isSelected)
	    {
		isItemSelected = true;
		break;
	    }
	    else
	    {
		continue;
	    }
	}
	if (isItemSelected)
	{
	    funRemoveFromItemCodeLinkupTable();
	}
	else
	{
	    new frmOkPopUp(this, "Please Select The Item.", "Error", 0).setVisible(true);
	    return;
	}
    }

    //Insert Order Type with selected item name in table
    private void funInsertOrderTypeWithItemInRow()
    {
	try
	{

	    Object row[] = new Object[]
	    {
		txtItemName.getText(), cmbOrderType.getSelectedItem().toString(), txtItemCode.getText(), hmOrder.get(cmbOrderType.getSelectedItem().toString())
	    };
	    dmOrderItemLinkup.addRow(row);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    tblOrderDetails.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	    tblOrderDetails.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

	    tblOrderDetails.getColumnModel().getColumn(0).setPreferredWidth(200);
	    tblOrderDetails.getColumnModel().getColumn(1).setPreferredWidth(200);

	    tblOrderDetails.setSize(700, 900);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funItemWithOrderTypeTableRowSelection()
    {
	int rowNo = tblOrderDetails.getSelectedRow();
	String rowValue = tblOrderDetails.getValueAt(rowNo, 4).toString();
	if (Boolean.parseBoolean(rowValue))
	{
	    btnRemoveOrder.setEnabled(true);
	}

	boolean flgSelect = false;
	int total = tblOrderDetails.getRowCount();
	for (int i = 0; i < total; i++)
	{
	    String rowValue1 = tblOrderDetails.getValueAt(i, 4).toString();
	    if (Boolean.parseBoolean(rowValue1))
	    {
		flgSelect = true;
		break;
	    }
	}
	if (!flgSelect)
	{
	    btnRemoveOrder.setEnabled(false);
	}

	rowNo = 0;

    }

    private void funRemoveItemWithOrderTypeRow()
    {
	/*
         * int rowNo = tblOrderDetails.getRowCount(); java.util.Vector
         * vIndexToDelete = new java.util.Vector(); for (int i = 0; i < rowNo;
         * i++) { boolean select =
         * Boolean.parseBoolean(tblOrderDetails.getValueAt(i, 4).toString()); if
         * (select) { vIndexToDelete.add(i);
         *
         * }
         * }
         * int cnt = 0; while (cnt < tblOrderDetails.getRowCount()) { boolean
         * select = Boolean.parseBoolean(tblOrderDetails.getValueAt(cnt,
         * 4).toString()); if (select) { if (tblOrderDetails.getRowCount() > 0)
         * { dmOrderItemLinkup.removeRow(cnt); } } else { cnt++; } }
         * btnRemoveOrder.setEnabled(false);
	 */

	boolean isItemSelected = false;
	List<String> listDeleteRows = new ArrayList<>();
	for (int i = 0; i < tblOrderDetails.getRowCount(); i++)
	{
	    boolean isSelected = Boolean.parseBoolean(tblOrderDetails.getValueAt(i, 4).toString());
	    if (isSelected)
	    {
		isItemSelected = true;
		break;
	    }
	    else
	    {
		continue;
	    }
	}
	if (isItemSelected)
	{
	    funRemoveFromOrderDetailsTable();
	}
	else
	{
	    new frmOkPopUp(this, "Please Select The Item.", "Error", 0).setVisible(true);
	    return;
	}
    }

    //Load Item-Order data from database and set to the tblOrderDetails
    private void funLoadItemOrderTypeLinkupTable()
    {
	try
	{
	    String sql = " select  b.strItemName,c.strOrderDesc,a.strItemCode,a.strOrderCode from "
		    + " tblitemorderingdtl a,tblitemmaster b,tblordermaster c "
		    + " where a.strItemCode=b.strItemCode and a.strOrderCode=c.strOrderCode "
		    + " and a.strItemCode='" + txtItemCode.getText().toString() + "' ";
	    //System.out.println(sql);
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    dmOrderItemLinkup.setRowCount(0);
	    while (rs.next())
	    {
		Object[] column = new Object[5];
		column[0] = rs.getString(1);
		column[1] = rs.getString(2);
		column[2] = rs.getString(3);
		column[3] = rs.getString(4);
		column[4] = false;

		dmOrderItemLinkup.addRow(column);
	    }
	    rs.close();
	    tblOrderDetails.setModel(dmOrderItemLinkup);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    tblOrderDetails.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	    tblOrderDetails.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

	    tblOrderDetails.getColumnModel().getColumn(0).setPreferredWidth(200);
	    tblOrderDetails.getColumnModel().getColumn(1).setPreferredWidth(200);
	    tblOrderDetails.setSize(700, 900);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    // function to check duplicate item code with same order code
    private boolean funCheckOrderCodeForItem() throws Exception
    {
	boolean flgResult = false;
	if (tblOrderDetails.getRowCount() > 0)
	{
	    String query = " select strItemCode from tblitemorderingdtl where strOrderCode='" + hmOrder.get(cmbOrderType.getSelectedItem().toString()) + "' and strItemCode='" + txtItemCode.getText() + "' ; ";
	    ResultSet rsCheckOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    if (rsCheckOrderCode.next())
	    {
		flgResult = true;
	    }
	    rsCheckOrderCode.close();
	}
	return flgResult;
    }

    private boolean funCheckDuplicateOrderCodeandPOSForItem() throws Exception
    {
	boolean flgResult = false;
	if (tblOrderDetails.getRowCount() > 0)
	{
	    for (int row = 0; row < tblOrderDetails.getRowCount(); row++)
	    {
		if (tblOrderDetails.getValueAt(row, 2).equals(txtItemCode.getText().toString()) && tblOrderDetails.getValueAt(row, 3).equals(hmOrder.get(cmbOrderType.getSelectedItem().toString())))
		{
		    flgResult = true;
		}
	    }
	}
	return flgResult;
    }

//Function To Fill tblCharacterstics Master
    private void funFillCharactersticsMaster()
    {
	try
	{
	    DefaultTableModel dmCharactersticsMaster = (DefaultTableModel) tblCharactersticsMaster.getModel();
	    String sql = " select a.strCharCode,a.strCharName,a.strCharType "
		    + " from tblcharactersticsmaster a order by a.strCharType desc";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    dmCharactersticsMaster.setRowCount(0);
	    while (rs.next())
	    {
		Object[] column
			=
			{
			    rs.getString(2), false, rs.getString(1), rs.getString(3)
			};
		dmCharactersticsMaster.addRow(column);
	    }
	    rs.close();
	    tblCharactersticsMaster.setRowHeight(30);
	    tblCharactersticsMaster.setModel(dmCharactersticsMaster);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funSelectAllCheckBoxClicked() throws Exception
    {
	int row = tblCharactersticsMaster.getSelectedRow();
	String charCode = tblCharactersticsMaster.getValueAt(row, 2).toString();
	String charType = tblCharactersticsMaster.getValueAt(row, 3).toString();
	boolean flgSelect = Boolean.parseBoolean(tblCharactersticsMaster.getValueAt(row, 1).toString());

	if (flgSelect)
	{
	    if (charType.equalsIgnoreCase("Value"))
	    {
		if (chkSelectAll.isSelected())
		{
		    List<String> listCharValue = new ArrayList<String>();
		    for (int cnt = 0; cnt < tblCharValue.getRowCount(); cnt++)
		    {
			tblCharValue.setValueAt(true, cnt, 1);
			listCharValue.add(tblCharValue.getValueAt(cnt, 0).toString());
		    }
		    hmCharValues.put(charCode, listCharValue);
		}
		else
		{
		    for (int cnt = 0; cnt < tblCharValue.getRowCount(); cnt++)
		    {
			tblCharValue.setValueAt(false, cnt, 1);
		    }
		    hmCharValues.remove(charCode);
		}
	    }
	}
	//System.out.println("Selected Item count= "+hmCharValues.size());
    }

    private void funCharMasterTableClicked() throws Exception
    {
	int row = tblCharactersticsMaster.getSelectedRow();
	String charCode = tblCharactersticsMaster.getValueAt(row, 2).toString();
	String charName = tblCharactersticsMaster.getValueAt(row, 0).toString();
	String charType = tblCharactersticsMaster.getValueAt(row, 3).toString();
	boolean flgSelect = Boolean.parseBoolean(tblCharactersticsMaster.getValueAt(row, 1).toString());

	if (flgSelect)
	{
	    if (charType.equals("Text"))
	    {
		List<String> arrTextList = new ArrayList<String>();
		arrTextList.add(" ");
		hmCharValues.put(charCode, arrTextList);
	    }
	    else
	    {
		funFillCharactersticsValueMaster(charCode, charName);
		//  funFillUpdatedCharactersticsValueMaster(charCode,charName);
	    }
	}
	else
	{
	    if (charType.equals("Text"))
	    {
		List<String> arrTextList = new ArrayList<String>();
		arrTextList.add(" ");
		hmCharValues.remove(charCode);
	    }
	    else
	    {
		funRemoveFillCharactersticsValueMaster(charCode, charName);
	    }
	}
    }

    private void funFillCharactersticsValueMaster(String charCode, String charName) throws Exception
    {
	chkSelectAll.setSelected(false);
	List<String> listCharValue = new ArrayList<String>();
	DefaultTableModel dmCharValue = (DefaultTableModel) tblCharValue.getModel();
	dmCharValue.setRowCount(0);

	String sql = " select b.strCharValue from tblcharvalue a ,tblitemcharctersticslinkupdtl b  "
		+ " where a.strCharCode=b.strCharCode "
		+ " and b.strCharCode='" + charCode + "' and b.strItemCode='" + txtItemCode.getText().toString() + "' "
		+ " group by b.strCharValue order by b.strCharValue ";
	ResultSet rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsCharValue.next())
	{
	    if (hmCharValues.containsKey(charCode))
	    {
		listCharValue = hmCharValues.get(charCode);
	    }
	    listCharValue.add(rsCharValue.getString(1));
	    hmCharValues.put(charCode, listCharValue);
	    Object[] column
		    =
		    {
			rsCharValue.getString(1), true, charCode, charName
		    };
	    dmCharValue.addRow(column);
	}
	rsCharValue.close();

	sql = " select a.strCharValues from tblcharvalue a  where a.strCharCode='" + charCode + "' "
		+ " and a.strCharValues NOT IN (select b.strCharValue  FROM tblitemcharctersticslinkupdtl b "
		+ " where b.strCharCode='" + charCode + "' and b.strItemCode='" + txtItemCode.getText().toString() + "' "
		+ " group by b.strCharValue)  ";
	rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsCharValue.next())
	{
	    boolean flgSelect = false;
	    if (hmCharValues.containsKey(charCode))
	    {
		listCharValue = hmCharValues.get(charCode);
	    }
	    if (listCharValue.size() > 0)
	    {
		if (listCharValue.contains(rsCharValue.getString(1)))
		{
		    flgSelect = true;
		}
	    }
	    Object[] column
		    =
		    {
			rsCharValue.getString(1), flgSelect, charCode, charName
		    };
	    dmCharValue.addRow(column);
	}
	rsCharValue.close();

	tblCharValue.setRowHeight(30);
	tblCharValue.setModel(dmCharValue);
    }

    private void funRemoveFillCharactersticsValueMaster(String charCode, String charName) throws Exception
    {
	List<String> listCharValue = new ArrayList<String>();
	DefaultTableModel dmCharValue = (DefaultTableModel) tblCharValue.getModel();
	dmCharValue.setRowCount(0);
	String sql = " select a.strCharValues from tblcharvalue a"
		+ " where a.strCharCode='" + charCode + "'";
	ResultSet rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsCharValue.next())
	{
	    boolean flgSelect = false;
	    if (hmCharValues.containsKey(charCode))
	    {
		listCharValue = hmCharValues.get(charCode);
		hmCharValues.remove(charCode);
	    }
	    if (listCharValue.size() > 0)
	    {
		if (listCharValue.contains(rsCharValue.getString(1)))
		{
		    flgSelect = false;
		    hmCharValues.remove(charCode);
		}
	    }
	    Object[] column
		    =
		    {
			rsCharValue.getString(1), flgSelect, charCode, charName
		    };
	    dmCharValue.addRow(column);
	}
	rsCharValue.close();
	tblCharValue.setRowHeight(30);
	tblCharValue.setModel(dmCharValue);
    }

    private int funFillCharValueMap()
    {
	List<String> listCharValue = new ArrayList<String>();

	if (tblCharValue.getSelectedColumn() == 1)
	{
	    int row = tblCharValue.getSelectedRow();
	    String charCode = tblCharValue.getValueAt(row, 2).toString();
	    String charValue = tblCharValue.getValueAt(row, 0).toString();
	    boolean flgSelect = Boolean.parseBoolean(tblCharValue.getValueAt(row, 1).toString());

	    if (hmCharValues.containsKey(charCode))
	    {
		listCharValue = hmCharValues.get(charCode);
	    }
	    if (flgSelect)
	    {
		listCharValue.add(charValue);
	    }
	    else
	    {
		listCharValue.remove(charValue);
	    }
	    hmCharValues.put(charCode, listCharValue);
	}
	return 1;
    }

    //Load Item-Char Linkup data from database 
    private void funLoadAndFillCharactersticsValueMaster()
    {
	try
	{
	    Map<String, String> hmCharValueType = new HashMap<String, String>();
	    hmCharValues.clear();
	    DefaultTableModel dmCharValue = (DefaultTableModel) tblCharValue.getModel();
	    dmCharValue.setRowCount(0);

	    String charCode = "", charType = "", charName = "";
	    String sql = " select a.strCharCode,b.strCharType,b.strCharName "
		    + " from tblitemcharctersticslinkupdtl a,tblcharactersticsmaster b "
		    + " where a.strCharCode=b.strCharCode and a.strItemCode='" + txtItemCode.getText().toString() + "'"
		    + " group by a.strCharCode ";
	    ResultSet rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCharValue.next())
	    {
		charCode = rsCharValue.getString(1);
		charType = rsCharValue.getString(2);
		charName = rsCharValue.getString(3);

		if (charType.equals("Text"))
		{
		    List<String> arrTextList = new ArrayList<String>();
		    arrTextList.add(" ");
		    hmCharValues.put(charCode, arrTextList);
		}
		else
		{
		    hmCharValueType.put(charCode, charName);
		    List<String> arrTextList = new ArrayList<String>();
		    hmCharValues.put(charCode, arrTextList);
		}
	    }
	    rsCharValue.close();

	    for (int cnt = 0; cnt < tblCharactersticsMaster.getRowCount(); cnt++)
	    {
		String charCodeFromTable = tblCharactersticsMaster.getValueAt(cnt, 2).toString();
		if (hmCharValues.containsKey(charCodeFromTable))
		{
		    tblCharactersticsMaster.setValueAt(true, cnt, 1);
		}
	    }

	    /*
             * if(row>0) { tblCharactersticsMaster.setRowHeight(30);
             * tblCharactersticsMaster.setModel(dmCharactersticsMaster); } else
             * { funFillCharactersticsMaster(); }
	     */
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    private void funFillUpdatedCharactersticsValueMaster(String charCode, String charName) throws Exception
    {
	List<String> listCharValue = new ArrayList<String>();
	DefaultTableModel dmCharValue = (DefaultTableModel) tblCharValue.getModel();
	dmCharValue.setRowCount(0);
	String sql = " select b.strCharValue from tblcharvalue a ,tblitemcharctersticslinkupdtl b "
		+ " where a.strCharCode=b.strCharCode and b.strCharCode='" + charCode + "' and b.strItemCode='" + txtItemCode.getText().toString() + "' "
		+ " group by b.strCharValue order by b.strCharValue ";

	/*
         * sql=" select a.strCharValues from tblcharvalue a left outer join
         * tblitemcharctersticslinkupdtl b " + " on a.strCharCode=b.strCharCode
         * " + " where b.strCharCode='"+charCode+"' and
         * b.strItemCode='"+txtItemCode.getText().toString()+"' " + " group by
         * a.strCharValues order by b.strCharValue ";
	 */
	ResultSet rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsCharValue.next())
	{
	    boolean flgSelect = true;
	    if (hmCharValues.containsKey(charCode))
	    {
		listCharValue = hmCharValues.get(charCode);
	    }
	    if (flgSelect)
	    {
		listCharValue.add(rsCharValue.getString(1));
	    }
	    else
	    {
		listCharValue.remove(rsCharValue.getString(1));
	    }
	    hmCharValues.put(charCode, listCharValue);
	    Object[] column
		    =
		    {
			rsCharValue.getString(1), true, charCode, charName
		    };
	    dmCharValue.addRow(column);
	}
	rsCharValue.close();
	tblCharValue.setModel(dmCharValue);
    }

    private void funRemoveFromOrderDetailsTable()
    {
	List<String> rowsToBeDeleted = new ArrayList<>();
	for (int i = 0; i < tblOrderDetails.getRowCount(); i++)
	{
	    if (Boolean.parseBoolean(tblOrderDetails.getValueAt(i, 4).toString()))
	    {
		rowsToBeDeleted.add(tblOrderDetails.getValueAt(i, 0).toString());
	    }
	}

	for (int i = 0; i < rowsToBeDeleted.size(); i++)
	{
	    for (int j = 0; j < tblOrderDetails.getRowCount(); j++)
	    {
		if (rowsToBeDeleted.get(i).equals(tblOrderDetails.getValueAt(j, 0).toString()) && Boolean.parseBoolean(tblOrderDetails.getValueAt(j, 4).toString()))
		{
		    ((DefaultTableModel) tblOrderDetails.getModel()).removeRow(j);
		}
	    }
	}

    }

    private void funRemoveFromItemCodeLinkupTable()
    {
	List<String> rowsToBeDeleted = new ArrayList<>();
	for (int i = 0; i < tblItemCodeLinkup.getRowCount(); i++)
	{
	    if (Boolean.parseBoolean(tblItemCodeLinkup.getValueAt(i, 6).toString()))
	    {
		rowsToBeDeleted.add(tblItemCodeLinkup.getValueAt(i, 0).toString());
	    }
	}

	for (int i = 0; i < rowsToBeDeleted.size(); i++)
	{
	    for (int j = 0; j < tblItemCodeLinkup.getRowCount(); j++)
	    {
		if (rowsToBeDeleted.get(i).equals(tblItemCodeLinkup.getValueAt(j, 0).toString()) && Boolean.parseBoolean(tblItemCodeLinkup.getValueAt(j, 6).toString()))
		{
		    ((DefaultTableModel) tblItemCodeLinkup.getModel()).removeRow(j);
		}
	    }
	}

    }

    private void funResetReipeFields()
    {
	lblMenuItemName.setText("");
	txtRecipeCode.setText("");
	txtMenuItemCode.setText("");
	funResetChildItemFields();
	funSetFormToInDateChosser();
	dmChildRows.setRowCount(0);
	btnNew.setMnemonic('s');
	txtRecipeCode.requestFocus();
	cmbReceivedUOM.setSelectedIndex(0);
	cmbRecipeUOM.setSelectedIndex(0);
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
        tabMenuItemMaster = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblitemCode = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        lblExtCode = new javax.swing.JLabel();
        txtExtCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        lblItemName = new javax.swing.JLabel();
        lblShortName = new javax.swing.JLabel();
        txtShortName1 = new javax.swing.JTextField();
        chkRawMaterial = new javax.swing.JCheckBox();
        cmbSubGroupCode = new javax.swing.JComboBox();
        lblSubGroupCode = new javax.swing.JLabel();
        cmbItemType = new javax.swing.JComboBox();
        lblItemType = new javax.swing.JLabel();
        lblTaxIndicator = new javax.swing.JLabel();
        cmbTaxIndicator = new javax.swing.JComboBox();
        lblBarCode2 = new javax.swing.JLabel();
        txtPurRate = new javax.swing.JTextField();
        bttImage = new javax.swing.JButton();
        btnBrowse = new javax.swing.JButton();
        txtItemImage = new javax.swing.JTextField();
        lblItemImage = new javax.swing.JLabel();
        lblMinlevel1 = new javax.swing.JLabel();
        txtSalePrice = new javax.swing.JTextField();
        cmbProcessingDay = new javax.swing.JComboBox();
        lblProcessingDay = new javax.swing.JLabel();
        lblMinlevel = new javax.swing.JLabel();
        txtMinlevel = new javax.swing.JTextField();
        lblProcessingTime = new javax.swing.JLabel();
        lblMaxlevel = new javax.swing.JLabel();
        cmbProcessingTme = new javax.swing.JComboBox();
        lblBarCode1 = new javax.swing.JLabel();
        txtMaxlevel = new javax.swing.JTextField();
        btnItemDetails = new javax.swing.JButton();
        chkDiscount = new javax.swing.JCheckBox();
        chkStkInEnable = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        chkItemForSale = new javax.swing.JCheckBox();
        lblRevenueHead = new javax.swing.JLabel();
        cmbRevenueHead = new javax.swing.JComboBox();
        chkOpenItem = new javax.swing.JCheckBox();
        chkItemWiseKOTYN = new javax.swing.JCheckBox();
        lblReceivedUOM = new javax.swing.JLabel();
        cmbReceivedUOM = new javax.swing.JComboBox();
        lblTargetMissedTime = new javax.swing.JLabel();
        cmbTargetMissedTime = new javax.swing.JComboBox();
        lblTargetMissedTimeMinutes = new javax.swing.JLabel();
        cmbRecipeUOM = new javax.swing.JComboBox();
        lblRecipeUOM = new javax.swing.JLabel();
        lblReceivedConversion = new javax.swing.JLabel();
        txtReceivedConversion = new javax.swing.JTextField();
        txtRecipeConversion = new javax.swing.JTextField();
        lblRecipeConversion = new javax.swing.JLabel();
        lblHSNNo = new javax.swing.JLabel();
        txtHSNNo = new javax.swing.JTextField();
        chkOperationalYN = new javax.swing.JCheckBox();
        panelLinkup = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItemCodeLinkup = new javax.swing.JTable();
        lblWSProductCode = new javax.swing.JLabel();
        txtWSProductCode = new javax.swing.JTextField();
        lblposname = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblExciseBrandCode = new javax.swing.JLabel();
        txtExciseBrandCode = new javax.swing.JTextField();
        lblWSProductName = new javax.swing.JLabel();
        txtWSProductName = new javax.swing.JTextField();
        btnLinkupReset = new javax.swing.JButton();
        panelOrderDetails = new javax.swing.JPanel();
        lblOrderType = new javax.swing.JLabel();
        cmbOrderType = new javax.swing.JComboBox();
        lblRequiredProductDays = new javax.swing.JLabel();
        txtRequiredProductDeliveryDays = new javax.swing.JTextField();
        lblDeliveryDays = new javax.swing.JLabel();
        chkSunday = new javax.swing.JCheckBox();
        chkMonday = new javax.swing.JCheckBox();
        chkTuesday = new javax.swing.JCheckBox();
        chkWed = new javax.swing.JCheckBox();
        chkThursday = new javax.swing.JCheckBox();
        chkFriday = new javax.swing.JCheckBox();
        chkSaturday = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        btnAddOrder = new javax.swing.JButton();
        btnRemoveOrder = new javax.swing.JButton();
        lblItemWeight = new javax.swing.JLabel();
        txtItemWeight = new javax.swing.JTextField();
        btnOrderingReset = new javax.swing.JButton();
        lblMinItemWeight = new javax.swing.JLabel();
        txtMinItemWeight = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblOrderDetails = new javax.swing.JTable();
        chkUrgentOrder = new javax.swing.JCheckBox();
        panelCharDetails = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCharactersticsMaster = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCharValue = new javax.swing.JTable();
        chkSelectAll = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        txtRecipeCode = new javax.swing.JTextField();
        lblRecipeCode = new javax.swing.JLabel();
        lblMenuItemCode = new javax.swing.JLabel();
        txtMenuItemCode = new javax.swing.JTextField();
        lblFromDate = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        lblChildItemName = new javax.swing.JLabel();
        txtChildItemName = new javax.swing.JTextField();
        lblQty = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        btnAddRecipe = new javax.swing.JButton();
        btnRemoveRecipe = new javax.swing.JButton();
        btnResetChild = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tblChildItems = new javax.swing.JTable();
        lblMenuItemName = new javax.swing.JLabel();
        lblUomName = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();

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

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Menu Item Master");
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

        panelGeneral.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelGeneral.setMinimumSize(new java.awt.Dimension(800, 570));
        panelGeneral.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(28, 18, 18));
        lblFormName.setText("Menu Item Master");

        lblitemCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblitemCode.setText("Menu Item Code   :");

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

        lblExtCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExtCode.setText("External Code   :");

        txtExtCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExtCodeMouseClicked(evt);
            }
        });
        txtExtCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExtCodeKeyPressed(evt);
            }
        });

        txtItemName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemNameMouseClicked(evt);
            }
        });
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemNameKeyPressed(evt);
            }
        });

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemName.setText("Item Name           :");

        lblShortName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShortName.setText("Short Name          :");

        txtShortName1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtShortName1MouseClicked(evt);
            }
        });
        txtShortName1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtShortName1ActionPerformed(evt);
            }
        });
        txtShortName1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtShortName1KeyPressed(evt);
            }
        });

        chkRawMaterial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkRawMaterial.setText("Raw Material   ");
        chkRawMaterial.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkRawMaterial.setOpaque(false);

        cmbSubGroupCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubGroupCodeActionPerformed(evt);
            }
        });
        cmbSubGroupCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSubGroupCodeKeyPressed(evt);
            }
        });

        lblSubGroupCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSubGroupCode.setText("Sub Group Code    :");

        cmbItemType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--", "Food", "Liquor", "Retail", "Play Charges", "Extra Play Charges", "Guest Charges", "Extra Guest Charges" }));
        cmbItemType.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbItemTypeMouseClicked(evt);
            }
        });
        cmbItemType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbItemTypeActionPerformed(evt);
            }
        });
        cmbItemType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbItemTypeKeyPressed(evt);
            }
        });

        lblItemType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemType.setText("Item Type            :");

        lblTaxIndicator.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTaxIndicator.setText("Tax Indicator        :");

        cmbTaxIndicator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        cmbTaxIndicator.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbTaxIndicatorKeyPressed(evt);
            }
        });

        lblBarCode2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBarCode2.setText("Purchase Rate       :");

        txtPurRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPurRate.setText("0.00");
        txtPurRate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPurRateMouseClicked(evt);
            }
        });
        txtPurRate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPurRateActionPerformed(evt);
            }
        });
        txtPurRate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPurRateKeyPressed(evt);
            }
        });

        bttImage.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bttImage.setToolTipText("Item Image");
        bttImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnBrowse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowse.setForeground(new java.awt.Color(254, 254, 254));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setToolTipText("Search Item Image");
        btnBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowse.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowseMouseClicked(evt);
            }
        });
        btnBrowse.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBrowseActionPerformed(evt);
            }
        });
        btnBrowse.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnBrowseKeyPressed(evt);
            }
        });

        txtItemImage.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemImageKeyPressed(evt);
            }
        });

        lblItemImage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemImage.setText("Item Image          :");

        lblMinlevel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinlevel1.setText("Sale Price             :");

        txtSalePrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSalePrice.setText("0.00");
        txtSalePrice.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSalePriceMouseClicked(evt);
            }
        });
        txtSalePrice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtSalePriceActionPerformed(evt);
            }
        });
        txtSalePrice.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSalePriceKeyPressed(evt);
            }
        });

        cmbProcessingDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));
        cmbProcessingDay.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbProcessingDayKeyPressed(evt);
            }
        });

        lblProcessingDay.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProcessingDay.setText("Processing Day   :");

        lblMinlevel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinlevel.setText("Minimum level       :");

        txtMinlevel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMinlevel.setText("0.00");
        txtMinlevel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMinlevelMouseClicked(evt);
            }
        });
        txtMinlevel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMinlevelActionPerformed(evt);
            }
        });
        txtMinlevel.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMinlevelKeyPressed(evt);
            }
        });

        lblProcessingTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProcessingTime.setText("Processing Time  :");

        lblMaxlevel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaxlevel.setText("Maximum level       :");

        cmbProcessingTme.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbProcessingTmeKeyPressed(evt);
            }
        });

        lblBarCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBarCode1.setText("Minutes");

        txtMaxlevel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxlevel.setText("0.00");
        txtMaxlevel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMaxlevelMouseClicked(evt);
            }
        });
        txtMaxlevel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMaxlevelActionPerformed(evt);
            }
        });
        txtMaxlevel.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMaxlevelKeyPressed(evt);
            }
        });

        btnItemDetails.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnItemDetails.setForeground(new java.awt.Color(255, 255, 255));
        btnItemDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgModBtn.png"))); // NOI18N
        btnItemDetails.setText("Item Details");
        btnItemDetails.setToolTipText("Set Item Detail");
        btnItemDetails.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemDetails.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgModBtn1.png"))); // NOI18N
        btnItemDetails.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnItemDetailsActionPerformed(evt);
            }
        });

        chkDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDiscount.setText("No Discount");
        chkDiscount.setOpaque(false);
        chkDiscount.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkDiscountKeyPressed(evt);
            }
        });

        chkStkInEnable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkStkInEnable.setText("Stock In Enable");
        chkStkInEnable.setOpaque(false);
        chkStkInEnable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkStkInEnableActionPerformed(evt);
            }
        });
        chkStkInEnable.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkStkInEnableKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(254, 254, 254));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Menu Item Master");
        btnCancel.setBorder(null);
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setPreferredSize(new java.awt.Dimension(60, 29));
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

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(254, 254, 254));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setBorder(null);
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(254, 254, 254));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Menu Item");
        btnNew.setBorder(null);
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
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

        chkItemForSale.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkItemForSale.setText("Item For Sale   ");
        chkItemForSale.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkItemForSale.setOpaque(false);

        lblRevenueHead.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRevenueHead.setText("Revenue Head      :");

        cmbRevenueHead.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FOOD", "BEVERAGE", "LIQUOR", "TOBBACO", "CONFECTIONARY", "MILD LIQUOR", "FERMENTATED LIQUOR", "DMFL", "BREWS", "IMPORTED LIQUOR", "BITTER/LIQUOR", "OTHER/MISC." }));
        cmbRevenueHead.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbRevenueHeadActionPerformed(evt);
            }
        });
        cmbRevenueHead.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbRevenueHeadKeyPressed(evt);
            }
        });

        chkOpenItem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkOpenItem.setText("Open Item");
        chkOpenItem.setOpaque(false);
        chkOpenItem.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkOpenItemKeyPressed(evt);
            }
        });

        chkItemWiseKOTYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkItemWiseKOTYN.setText("Item Wise KOT");
        chkItemWiseKOTYN.setOpaque(false);
        chkItemWiseKOTYN.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkItemWiseKOTYNActionPerformed(evt);
            }
        });
        chkItemWiseKOTYN.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkItemWiseKOTYNKeyPressed(evt);
            }
        });

        lblReceivedUOM.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReceivedUOM.setText("Received UOM       :");

        cmbReceivedUOM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbReceivedUOMKeyPressed(evt);
            }
        });

        lblTargetMissedTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTargetMissedTime.setText("Target Missed Time  :");

        cmbTargetMissedTime.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbTargetMissedTimeKeyPressed(evt);
            }
        });

        lblTargetMissedTimeMinutes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTargetMissedTimeMinutes.setText("Minutes");

        cmbRecipeUOM.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbRecipeUOMKeyPressed(evt);
            }
        });

        lblRecipeUOM.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRecipeUOM.setText("Recipe UOM      :");

        lblReceivedConversion.setText("Received Conversion :");

        txtReceivedConversion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtReceivedConversion.setText("1.00");

        txtRecipeConversion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRecipeConversion.setText("1.00");

        lblRecipeConversion.setText("Recipe Conversion  :");

        lblHSNNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHSNNo.setText("HSN No.              :");

        txtHSNNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHSNNoMouseClicked(evt);
            }
        });
        txtHSNNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHSNNoKeyPressed(evt);
            }
        });

        chkOperationalYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkOperationalYN.setSelected(true);
        chkOperationalYN.setText("Operational");
        chkOperationalYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkOperationalYN.setOpaque(false);
        chkOperationalYN.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkOperationalYNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                        .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtItemName))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFormName)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(lblitemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(lblExtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addComponent(txtExtCode))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblReceivedUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblReceivedConversion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(txtReceivedConversion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(cmbReceivedUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelGeneralLayout.createSequentialGroup()
                                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(chkStkInEnable)
                                                .addGap(18, 18, 18)
                                                .addComponent(chkDiscount))
                                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lblRecipeUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lblRecipeConversion))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtRecipeConversion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cmbRecipeUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(18, 18, 18)
                                        .addComponent(chkOpenItem)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chkItemWiseKOTYN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGeneralLayout.createSequentialGroup()
                                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(chkRawMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(chkItemForSale))
                                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(lblProcessingTime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(cmbProcessingTme, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblBarCode1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblTargetMissedTime, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbTargetMissedTime, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTargetMissedTimeMinutes))))))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(lblHSNNo)
                                .addGap(26, 26, 26)
                                .addComponent(txtHSNNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(182, 182, 182))
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(btnItemDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelGeneralLayout.createSequentialGroup()
                                        .addComponent(lblTaxIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                                .addComponent(cmbTaxIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(lblBarCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtPurRate, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtItemImage, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelGeneralLayout.createSequentialGroup()
                                        .addComponent(lblMaxlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMaxlevel, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                            .addComponent(txtMinlevel)
                                            .addComponent(txtSalePrice))
                                        .addGap(18, 18, 18)
                                        .addComponent(lblProcessingDay, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbProcessingDay, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(3, 3, 3)
                                .addComponent(bttImage, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblItemType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralLayout.createSequentialGroup()
                                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMinlevel1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRevenueHead, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMinlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbRevenueHead, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(lblShortName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtShortName1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGeneralLayout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(cmbItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSubGroupCode)
                        .addGap(18, 18, 18)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chkOperationalYN, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(39, 39, 39))
        );

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblReceivedConversion, lblReceivedUOM});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbReceivedUOM, txtReceivedConversion});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblRecipeConversion, lblRecipeUOM});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbRecipeUOM, txtRecipeConversion});

        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblitemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblExtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtExtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblShortName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtShortName1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkRawMaterial)
                        .addComponent(chkItemForSale)
                        .addComponent(chkOperationalYN)))
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSubGroupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTaxIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTaxIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBarCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPurRate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblItemImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtItemImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRevenueHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbRevenueHead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMinlevel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSalePrice, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                .addComponent(lblProcessingDay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbProcessingDay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bttImage, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblProcessingTime, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbProcessingTme, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblBarCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMinlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMinlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTargetMissedTime, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTargetMissedTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTargetMissedTimeMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaxlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaxlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkStkInEnable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkDiscount)
                        .addComponent(chkOpenItem)
                        .addComponent(chkItemWiseKOTYN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblRecipeUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbRecipeUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReceivedUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReceivedUOM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReceivedConversion)
                    .addComponent(txtReceivedConversion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblRecipeConversion)
                        .addComponent(txtRecipeConversion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnItemDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHSNNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHSNNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(44, Short.MAX_VALUE))))
        );

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblReceivedConversion, lblReceivedUOM});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbReceivedUOM, txtReceivedConversion});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblRecipeConversion, lblRecipeUOM});

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbRecipeUOM, txtRecipeConversion});

        tabMenuItemMaster.addTab("General", panelGeneral);
        panelGeneral.getAccessibleContext().setAccessibleParent(tabMenuItemMaster);

        panelLinkup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLinkup.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLinkup.setOpaque(false);

        tblItemCodeLinkup.setRowHeight(30);
        tblItemCodeLinkup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name", "WS Product Name", "POSName", "Item Code", "WS Product Code", "POSCode", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, true
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
        tblItemCodeLinkup.setColumnSelectionAllowed(true);
        tblItemCodeLinkup.getTableHeader().setReorderingAllowed(false);
        tblItemCodeLinkup.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemCodeLinkupMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItemCodeLinkup);
        tblItemCodeLinkup.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblItemCodeLinkup.getColumnModel().getColumnCount() > 0)
        {
            tblItemCodeLinkup.getColumnModel().getColumn(3).setMinWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(3).setMaxWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(4).setMinWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(4).setPreferredWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(4).setMaxWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(5).setMinWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(5).setPreferredWidth(2);
            tblItemCodeLinkup.getColumnModel().getColumn(5).setMaxWidth(2);
        }

        lblWSProductCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWSProductCode.setText("WS Product Code  :");

        txtWSProductCode.setEditable(false);
        txtWSProductCode.setBackground(new java.awt.Color(204, 204, 204));
        txtWSProductCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtWSProductCodeMouseClicked(evt);
            }
        });
        txtWSProductCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtWSProductCodeKeyPressed(evt);
            }
        });

        lblposname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblposname.setText("POS Name :");

        cmbPosCode.setToolTipText("Select POS");

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Delivery Charges");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnAddMouseEntered(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAddKeyPressed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setToolTipText(" Remove Delivery Charges");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });

        lblExciseBrandCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExciseBrandCode.setText("Enter Excise Brand Code   :");

        txtExciseBrandCode.setEditable(false);
        txtExciseBrandCode.setBackground(new java.awt.Color(204, 204, 204));
        txtExciseBrandCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtExciseBrandCodeMouseClicked(evt);
            }
        });
        txtExciseBrandCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtExciseBrandCodeKeyPressed(evt);
            }
        });

        lblWSProductName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWSProductName.setText("WS Product Name  :");

        txtWSProductName.setEditable(false);
        txtWSProductName.setBackground(new java.awt.Color(255, 255, 255));
        txtWSProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtWSProductNameMouseClicked(evt);
            }
        });
        txtWSProductName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtWSProductNameKeyPressed(evt);
            }
        });

        btnLinkupReset.setBackground(new java.awt.Color(255, 255, 255));
        btnLinkupReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLinkupReset.setForeground(new java.awt.Color(255, 255, 255));
        btnLinkupReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnLinkupReset.setText("RESET");
        btnLinkupReset.setToolTipText("Add Delivery Charges");
        btnLinkupReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLinkupReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnLinkupReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnLinkupResetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnLinkupResetMouseEntered(evt);
            }
        });
        btnLinkupReset.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnLinkupResetKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelLinkupLayout = new javax.swing.GroupLayout(panelLinkup);
        panelLinkup.setLayout(panelLinkupLayout);
        panelLinkupLayout.setHorizontalGroup(
            panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLinkupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelLinkupLayout.createSequentialGroup()
                        .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLinkupLayout.createSequentialGroup()
                                .addComponent(lblExciseBrandCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtExciseBrandCode, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLinkupLayout.createSequentialGroup()
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLinkupReset, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(167, 167, 167)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelLinkupLayout.createSequentialGroup()
                        .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLinkupLayout.createSequentialGroup()
                                .addComponent(lblposname, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblWSProductCode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLinkupLayout.createSequentialGroup()
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)))
                        .addComponent(txtWSProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblWSProductName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWSProductName, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLinkupLayout.setVerticalGroup(
            panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLinkupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLinkupLayout.createSequentialGroup()
                        .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblposname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLinkupReset, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(panelLinkupLayout.createSequentialGroup()
                        .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblWSProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtWSProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblWSProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtWSProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(panelLinkupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExciseBrandCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtExciseBrandCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabMenuItemMaster.addTab("Linkup", panelLinkup);
        panelLinkup.getAccessibleContext().setAccessibleParent(tabMenuItemMaster);

        panelOrderDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelOrderDetails.setMinimumSize(new java.awt.Dimension(800, 570));
        panelOrderDetails.setOpaque(false);
        panelOrderDetails.setLayout(null);

        lblOrderType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrderType.setText("Order Type  :");
        panelOrderDetails.add(lblOrderType);
        lblOrderType.setBounds(10, 10, 85, 34);

        cmbOrderType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOrderTypeActionPerformed(evt);
            }
        });
        cmbOrderType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbOrderTypeKeyPressed(evt);
            }
        });
        panelOrderDetails.add(cmbOrderType);
        cmbOrderType.setBounds(100, 10, 139, 34);

        lblRequiredProductDays.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRequiredProductDays.setText("Required Product Delivery Days :");
        panelOrderDetails.add(lblRequiredProductDays);
        lblRequiredProductDays.setBounds(250, 10, 186, 30);

        txtRequiredProductDeliveryDays.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRequiredProductDeliveryDays.setText("0");
        txtRequiredProductDeliveryDays.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRequiredProductDeliveryDaysMouseClicked(evt);
            }
        });
        txtRequiredProductDeliveryDays.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRequiredProductDeliveryDaysActionPerformed(evt);
            }
        });
        txtRequiredProductDeliveryDays.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtRequiredProductDeliveryDaysKeyPressed(evt);
            }
        });
        panelOrderDetails.add(txtRequiredProductDeliveryDays);
        txtRequiredProductDeliveryDays.setBounds(440, 10, 50, 30);

        lblDeliveryDays.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryDays.setText("No Delivery Days   :");
        panelOrderDetails.add(lblDeliveryDays);
        lblDeliveryDays.setBounds(10, 50, 120, 33);

        chkSunday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSunday.setText("Sun");
        chkSunday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkSundayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkSunday);
        chkSunday.setBounds(130, 50, 71, 30);

        chkMonday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMonday.setText("Mon");
        chkMonday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkMondayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkMonday);
        chkMonday.setBounds(200, 50, 73, 30);

        chkTuesday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkTuesday.setText("Tue");
        chkTuesday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkTuesdayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkTuesday);
        chkTuesday.setBounds(280, 50, 75, 30);

        chkWed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkWed.setText("Wed");
        chkWed.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkWedKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkWed);
        chkWed.setBounds(350, 50, 71, 30);

        chkThursday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkThursday.setText("Thu");
        chkThursday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkThursdayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkThursday);
        chkThursday.setBounds(420, 50, 64, 30);

        chkFriday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkFriday.setText("Fri");
        chkFriday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkFridayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkFriday);
        chkFriday.setBounds(490, 50, 60, 30);

        chkSaturday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSaturday.setText("Sat");
        chkSaturday.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkSaturdayKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkSaturday);
        chkSaturday.setBounds(559, 50, 60, 30);

        jLabel1.setText("Gram");
        panelOrderDetails.add(jLabel1);
        jLabel1.setBounds(1768, 13, 25, 34);

        btnAddOrder.setBackground(new java.awt.Color(255, 255, 255));
        btnAddOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAddOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnAddOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAddOrder.setText("ADD");
        btnAddOrder.setToolTipText("Add Delivery Charges");
        btnAddOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAddOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddOrderMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnAddOrderMouseEntered(evt);
            }
        });
        btnAddOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAddOrderKeyPressed(evt);
            }
        });
        panelOrderDetails.add(btnAddOrder);
        btnAddOrder.setBounds(10, 90, 90, 29);

        btnRemoveOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemoveOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnRemoveOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemoveOrder.setText("Remove");
        btnRemoveOrder.setToolTipText(" Remove Delivery Charges");
        btnRemoveOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemoveOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveOrderMouseClicked(evt);
            }
        });
        btnRemoveOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveOrderActionPerformed(evt);
            }
        });
        panelOrderDetails.add(btnRemoveOrder);
        btnRemoveOrder.setBounds(230, 90, 100, 29);

        lblItemWeight.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemWeight.setText("Incr. Weight  :");
        panelOrderDetails.add(lblItemWeight);
        lblItemWeight.setBounds(500, 10, 90, 30);

        txtItemWeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtItemWeight.setText("0");
        txtItemWeight.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemWeightMouseClicked(evt);
            }
        });
        txtItemWeight.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtItemWeightActionPerformed(evt);
            }
        });
        txtItemWeight.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemWeightKeyPressed(evt);
            }
        });
        panelOrderDetails.add(txtItemWeight);
        txtItemWeight.setBounds(590, 10, 50, 30);

        btnOrderingReset.setBackground(new java.awt.Color(255, 255, 255));
        btnOrderingReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOrderingReset.setForeground(new java.awt.Color(255, 255, 255));
        btnOrderingReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnOrderingReset.setText("RESET");
        btnOrderingReset.setToolTipText("Add Delivery Charges");
        btnOrderingReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOrderingReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnOrderingReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOrderingResetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnOrderingResetMouseEntered(evt);
            }
        });
        btnOrderingReset.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnOrderingResetKeyPressed(evt);
            }
        });
        panelOrderDetails.add(btnOrderingReset);
        btnOrderingReset.setBounds(120, 90, 100, 30);

        lblMinItemWeight.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMinItemWeight.setText("Min Weight  :");
        panelOrderDetails.add(lblMinItemWeight);
        lblMinItemWeight.setBounds(650, 10, 80, 30);

        txtMinItemWeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMinItemWeight.setText("0");
        txtMinItemWeight.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMinItemWeightMouseClicked(evt);
            }
        });
        txtMinItemWeight.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMinItemWeightActionPerformed(evt);
            }
        });
        txtMinItemWeight.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMinItemWeightKeyPressed(evt);
            }
        });
        panelOrderDetails.add(txtMinItemWeight);
        txtMinItemWeight.setBounds(730, 10, 50, 30);

        tblItemCodeLinkup.setRowHeight(30);
        tblOrderDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name", "Order Name", "Item Code", "OrderCode", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, true
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
        tblOrderDetails.setColumnSelectionAllowed(true);
        tblOrderDetails.getTableHeader().setReorderingAllowed(false);
        tblOrderDetails.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblOrderDetailsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblOrderDetails);
        tblOrderDetails.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblOrderDetails.getColumnModel().getColumnCount() > 0)
        {
            tblOrderDetails.getColumnModel().getColumn(2).setMinWidth(2);
            tblOrderDetails.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblOrderDetails.getColumnModel().getColumn(2).setMaxWidth(2);
            tblOrderDetails.getColumnModel().getColumn(3).setMinWidth(2);
            tblOrderDetails.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblOrderDetails.getColumnModel().getColumn(3).setMaxWidth(2);
        }

        panelOrderDetails.add(jScrollPane3);
        jScrollPane3.setBounds(10, 130, 770, 210);

        chkUrgentOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkUrgentOrder.setText("Urgent Order");
        chkUrgentOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                chkUrgentOrderKeyPressed(evt);
            }
        });
        panelOrderDetails.add(chkUrgentOrder);
        chkUrgentOrder.setBounds(649, 50, 130, 30);

        tabMenuItemMaster.addTab("Ordering ", panelOrderDetails);
        panelOrderDetails.getAccessibleContext().setAccessibleParent(tabMenuItemMaster);

        panelCharDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelCharDetails.setOpaque(false);

        tblItemCodeLinkup.setRowHeight(30);
        tblCharactersticsMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Char Name", "Select", "Char Code", "Char Type"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, false, false
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
        tblCharactersticsMaster.setColumnSelectionAllowed(true);
        tblCharactersticsMaster.getTableHeader().setReorderingAllowed(false);
        tblCharactersticsMaster.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCharactersticsMasterMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblCharactersticsMaster);
        tblCharactersticsMaster.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblCharactersticsMaster.getColumnModel().getColumnCount() > 0)
        {
            tblCharactersticsMaster.getColumnModel().getColumn(2).setMinWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(2).setMaxWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setMinWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setMaxWidth(2);
        }

        tblItemCodeLinkup.setRowHeight(30);
        tblCharValue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Value", "Select", "Code", "Char Name"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, false, false
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
        tblCharValue.setColumnSelectionAllowed(true);
        tblCharValue.getTableHeader().setReorderingAllowed(false);
        tblCharValue.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCharValueMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCharValue);
        tblCharValue.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblCharValue.getColumnModel().getColumnCount() > 0)
        {
            tblCharValue.getColumnModel().getColumn(2).setMinWidth(2);
            tblCharValue.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblCharValue.getColumnModel().getColumn(2).setMaxWidth(2);
            tblCharValue.getColumnModel().getColumn(3).setMinWidth(2);
            tblCharValue.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblCharValue.getColumnModel().getColumn(3).setMaxWidth(2);
        }

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkSelectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCharDetailsLayout = new javax.swing.GroupLayout(panelCharDetails);
        panelCharDetails.setLayout(panelCharDetailsLayout);
        panelCharDetailsLayout.setHorizontalGroup(
            panelCharDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCharDetailsLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelCharDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCharDetailsLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkSelectAll))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCharDetailsLayout.setVerticalGroup(
            panelCharDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCharDetailsLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(chkSelectAll)
                .addGap(73, 73, 73)
                .addGroup(panelCharDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabMenuItemMaster.addTab("Characterstics", panelCharDetails);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        jPanel1.setOpaque(false);

        txtRecipeCode.setEditable(false);
        txtRecipeCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtRecipeCodeMouseClicked(evt);
            }
        });
        txtRecipeCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtRecipeCodeKeyPressed(evt);
            }
        });

        lblRecipeCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRecipeCode.setText("Recipe Code          :");

        lblMenuItemCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuItemCode.setText("Menu Item Name   :");

        txtMenuItemCode.setEditable(false);
        txtMenuItemCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMenuItemCodeMouseClicked(evt);
            }
        });
        txtMenuItemCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMenuItemCodeKeyPressed(evt);
            }
        });

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date             :");

        dteFromDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteFromDateKeyPressed(evt);
            }
        });

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date           :");

        dteToDate.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteToDateKeyPressed(evt);
            }
        });

        lblChildItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblChildItemName.setText("Child Item Name    :");

        txtChildItemName.setEditable(false);
        txtChildItemName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtChildItemNameMouseClicked(evt);
            }
        });
        txtChildItemName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtChildItemNameKeyPressed(evt);
            }
        });

        lblQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblQty.setText("Quantity           :");

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setText("0");
        txtQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtQtyMouseClicked(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtQtyKeyPressed(evt);
            }
        });

        btnAddRecipe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAddRecipe.setForeground(new java.awt.Color(255, 255, 255));
        btnAddRecipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAddRecipe.setText("ADD");
        btnAddRecipe.setToolTipText("Add Item");
        btnAddRecipe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddRecipe.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAddRecipe.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddRecipeMouseClicked(evt);
            }
        });

        btnRemoveRecipe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemoveRecipe.setForeground(new java.awt.Color(255, 255, 255));
        btnRemoveRecipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemoveRecipe.setText("REMOVE");
        btnRemoveRecipe.setToolTipText("Remove Item");
        btnRemoveRecipe.setEnabled(false);
        btnRemoveRecipe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveRecipe.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemoveRecipe.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveRecipeMouseClicked(evt);
            }
        });

        btnResetChild.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnResetChild.setForeground(new java.awt.Color(255, 255, 255));
        btnResetChild.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetChild.setText("RESET");
        btnResetChild.setToolTipText("Reset All Fields");
        btnResetChild.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetChild.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnResetChild.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetChildMouseClicked(evt);
            }
        });

        tblChildItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ItemCode", "Item Name", "Quantity", "Select"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true
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
        tblChildItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblChildItemsMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblChildItems);

        lblUomName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lbl.setText("UOM   :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAddRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnRemoveRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnResetChild, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblMenuItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtQty, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                            .addComponent(lblUomName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtRecipeCode))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblMenuItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnResetChild, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnAddRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRemoveRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbl))
                        .addGap(18, 18, 18)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblUomName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(125, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbl, lblQty, lblUomName});

        tabMenuItemMaster.addTab("Recipe", jPanel1);

        panelLayout.add(tabMenuItemMaster, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void funSelectItemCode()
    {
	try
	{
	    objUtility.funCallForSearchForm("MenuItem");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		btnNew.setText("UPDATE");//updated
		btnNew.setMnemonic('u');
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funResetReipeFields();
		funSetData(data);
		funLoadItemLinkupTable();
		funLoadItemOrderTypeLinkupTable();
		funLoadAndFillCharactersticsValueMaster();
		clsGlobalVarClass.gSearchItemClicked = false;
		funSetImage();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
    private void cmbRevenueHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbRevenueHeadKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtSalePrice.requestFocus();
	}
    }//GEN-LAST:event_cmbRevenueHeadKeyPressed

    private void cmbRevenueHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRevenueHeadActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbRevenueHeadActionPerformed

    private void txtMaxlevelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaxlevelKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkStkInEnable.requestFocus();
	}
    }//GEN-LAST:event_txtMaxlevelKeyPressed

    private void txtMaxlevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaxlevelActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMaxlevelActionPerformed

    private void txtMaxlevelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaxlevelMouseClicked
	try
	{
	    if (txtMaxlevel.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter MAx Level.").setVisible(true);
		txtMaxlevel.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtMaxlevel.getText(), "1", "Please Enter Max Level.").setVisible(true);
		txtMaxlevel.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtMaxlevelMouseClicked

    private void cmbProcessingTmeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProcessingTmeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtMinlevel.requestFocus();
	}

    }//GEN-LAST:event_cmbProcessingTmeKeyPressed

    private void txtMinlevelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinlevelKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtMaxlevel.requestFocus();
	}
    }//GEN-LAST:event_txtMinlevelKeyPressed

    private void txtMinlevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinlevelActionPerformed

    }//GEN-LAST:event_txtMinlevelActionPerformed

    private void txtMinlevelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMinlevelMouseClicked
	try
	{
	    if (txtMinlevel.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Min Level.").setVisible(true);
		txtMinlevel.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtMinlevel.getText(), "1", "Please Enter Min Level.").setVisible(true);
		txtMinlevel.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtMinlevelMouseClicked

    private void cmbProcessingDayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProcessingDayKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbProcessingTme.requestFocus();
	}
    }//GEN-LAST:event_cmbProcessingDayKeyPressed

    private void txtSalePriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalePriceKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbProcessingDay.requestFocus();
	}
    }//GEN-LAST:event_txtSalePriceKeyPressed

    private void txtSalePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalePriceActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtSalePriceActionPerformed

    private void txtSalePriceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalePriceMouseClicked
	try
	{
	    if (txtSalePrice.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Sales Rate.").setVisible(true);
		txtSalePrice.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtSalePrice.getText(), "1", "Please Enter Sales Rate.").setVisible(true);
		txtSalePrice.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtSalePriceMouseClicked

    private void txtItemImageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemImageKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnBrowse.requestFocus();
	}
    }//GEN-LAST:event_txtItemImageKeyPressed

    private void btnBrowseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBrowseKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbRevenueHead.requestFocus();
	}
    }//GEN-LAST:event_btnBrowseKeyPressed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
	// TODO add your handling code here:
	try
	{

	    //FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg");
	    JFileChooser jfc = new JFileChooser();
	    // jfc.setFileFilter(filter);
	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	    {
		tempFile = jfc.getSelectedFile();
		String imagePath = tempFile.getAbsolutePath();
		userImagefilePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//                bttImage.setText("");
//                bttImage.setIcon(new ImageIcon(imagePath));
		txtItemImage.setText(tempFile.getAbsolutePath());
		fileInImg = new FileInputStream(tempFile);
		bttImage.setText("");
		imgBf = funScaleImage(100, 100, userImagefilePath);
		ImageIO.write(imgBf, "png", tempFile);
		bttImage.setIcon(new javax.swing.ImageIcon(imgBf));

		//  fileInImg.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void txtPurRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPurRateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtItemImage.requestFocus();
	}
    }//GEN-LAST:event_txtPurRateKeyPressed

    private void txtPurRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurRateActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtPurRateActionPerformed

    private void txtPurRateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPurRateMouseClicked
	try
	{
	    if (txtPurRate.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Purchase Rate.").setVisible(true);
		txtPurRate.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtPurRate.getText(), "1", "Please Enter Purchase Rate.").setVisible(true);
		txtPurRate.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPurRateMouseClicked

    private void cmbTaxIndicatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTaxIndicatorKeyPressed
	// TODO add your handling code here:
	//  focus to another field on click of enter key
	if (evt.getKeyCode() == 10)
	{
	    if (cmbTaxIndicator.getSelectedItem().equals(" "))
	    {
		JOptionPane.showMessageDialog(this, "Please select tax indicator");
	    }
	    else
	    {
		txtPurRate.requestFocus();
	    }
	}
    }//GEN-LAST:event_cmbTaxIndicatorKeyPressed

    private void cmbItemTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemTypeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbSubGroupCode.requestFocus();
	}
    }//GEN-LAST:event_cmbItemTypeKeyPressed

    private void cmbItemTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemTypeActionPerformed

    private void cmbItemTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbItemTypeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbItemTypeMouseClicked

    private void cmbSubGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSubGroupCodeKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbTaxIndicator.requestFocus();
	}
    }//GEN-LAST:event_cmbSubGroupCodeKeyPressed

    private void cmbSubGroupCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubGroupCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbSubGroupCodeActionPerformed

    private void txtShortName1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShortName1KeyPressed

	String itemShortName = txtShortName1.getText().trim();
	String code = txtItemCode.getText().trim();
	//  focus to another field on click of enter key
	if (evt.getKeyCode() == 10)
	{
	    if (txtShortName1.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Item Short Name").setVisible(true);
		txtShortName1.setText(clsGlobalVarClass.gKeyboardValue);
		cmbItemType.requestFocus();
	    }
	    else if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strShortName", "strItemCode", itemShortName, code, btnNew.getText().trim(), ""))
	    {
		JOptionPane.showMessageDialog(this, "Short Name is Already Exsist");
		txtShortName1.requestFocus();
	    }
	    else
	    {
		cmbItemType.requestFocus();
	    }
	}
    }//GEN-LAST:event_txtShortName1KeyPressed

    private void txtShortName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShortName1ActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtShortName1ActionPerformed

    private void txtShortName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShortName1MouseClicked

	try
	{
	    if (txtShortName1.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Item Short Name").setVisible(true);
		txtShortName1.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtShortName1.getText(), "1", "Enter Item Short Name").setVisible(true);
		txtShortName1.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtShortName1MouseClicked

    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed
	// TODO add your handling code here:
	String itemName = txtItemName.getText().trim();
	String code = txtItemCode.getText().trim();
	//  focus to another field on click of enter key
	if (evt.getKeyCode() == 10)
	{
	    if (txtItemName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Item Name").setVisible(true);
		txtItemName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else if (clsGlobalVarClass.funCheckItemName("tblitemmaster", "strItemName", "strItemCode", itemName, code, btnNew.getText().trim(), ""))
	    {
		JOptionPane.showMessageDialog(this, "Item  Name is Already Exsist");
		txtItemName.requestFocus();
		return;
	    }
	    else
	    {
		txtShortName1.requestFocus();
	    }
	}
    }//GEN-LAST:event_txtItemNameKeyPressed

    private void txtItemNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtItemName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Item Name").setVisible(true);
		txtItemName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtItemName.getText(), "1", "Enter Item Name").setVisible(true);
		txtItemName.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtItemNameMouseClicked

    private void txtExtCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtCodeKeyPressed
	// TODO add your handling code here:
	//  focus to another field on click of enter key
	if (evt.getKeyCode() == 10)
	{
	    if (txtExtCode.getText().length() == 0)
	    {
		txtItemName.requestFocus();
	    }
	    else
	    {
		txtItemName.requestFocus();
	    }
	}
    }//GEN-LAST:event_txtExtCodeKeyPressed

    private void txtExtCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExtCodeMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtExtCode.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter External Code").setVisible(true);
		txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtExtCode.getText(), "1", "Enter External Code").setVisible(true);
		txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtExtCodeMouseClicked

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
	// TODO add your handling code here:
	//open item help on click of '?' or '/' key
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funSelectItemCode();
	}
	//  focus to another field on click of enter key
	if (evt.getKeyCode() == 10)
	{
	    txtExtCode.requestFocus();
	}
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
	// TODO add your handling code here:
	funSelectItemCode();
    }//GEN-LAST:event_txtItemCodeMouseClicked

    private void chkStkInEnableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkStkInEnableKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    chkDiscount.requestFocus();
	}
    }//GEN-LAST:event_chkStkInEnableKeyPressed

    private void chkStkInEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStkInEnableActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkStkInEnableActionPerformed

    private void chkDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkDiscountKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_chkDiscountKeyPressed

    private void chkOpenItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkOpenItemKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkOpenItemKeyPressed

    private void btnItemDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDetailsActionPerformed
	// TODO add your handling code here:
	frmItemDetails objItemDetails = new frmItemDetails(this, true);
	objItemDetails.setLocation(200, 100);
	objItemDetails.setSize(400, 300);
	objItemDetails.setVisible(true);
    }//GEN-LAST:event_btnItemDetailsActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	dispose();
	objUtility = null;
	clsGlobalVarClass.hmActiveForms.remove("Menu Item");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Menu Item");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	// TODO add your handling code here:
	funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (!objUtility.funCheckDouble(txtMinlevel.getText()))
	    {
		JOptionPane.showMessageDialog(this, "Invaild Input");
		return;
	    }
	    if (!objUtility.funCheckDouble(txtMaxlevel.getText()))
	    {
		JOptionPane.showMessageDialog(this, "Invaild Input");
		return;
	    }

	    if ("".equals(txtMinlevel.getText()))
	    {
		new frmOkPopUp(null, "Please Enter Number", "Error", 1).setVisible(true);
		clsGlobalVarClass.gRateEntered = false;
	    }
	    if ("".equals(txtMaxlevel.getText()))
	    {
		new frmOkPopUp(null, "Please Enter Number", "Error", 1).setVisible(true);
		clsGlobalVarClass.gRateEntered = false;
	    }
	    funSaveAndUpdate();
	}
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
	// TODO add your handling code here:

	if (!objUtility.funCheckDouble(txtMinlevel.getText()))
	{
	    JOptionPane.showMessageDialog(this, "Invaild Input");
	    return;
	}
	if (!objUtility.funCheckDouble(txtMaxlevel.getText()))
	{
	    JOptionPane.showMessageDialog(this, "Invaild Input");
	    return;
	}

	if ("".equals(txtMinlevel.getText()))
	{
	    new frmOkPopUp(null, "Please Enter Number", "Error", 1).setVisible(true);
	    clsGlobalVarClass.gRateEntered = false;
	}
	if ("".equals(txtMaxlevel.getText()))
	{
	    new frmOkPopUp(null, "Please Enter Number", "Error", 1).setVisible(true);
	    clsGlobalVarClass.gRateEntered = false;
	}
	funSaveAndUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void chkItemWiseKOTYNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkItemWiseKOTYNKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkItemWiseKOTYNKeyPressed

    private void chkItemWiseKOTYNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkItemWiseKOTYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkItemWiseKOTYNActionPerformed

    private void tblItemCodeLinkupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemCodeLinkupMouseClicked

	funLinkupTableRowSelection();
    }//GEN-LAST:event_tblItemCodeLinkupMouseClicked

    private void txtWSProductCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWSProductCodeMouseClicked
	// TODO add your handling code here:
	funProductCodeTextFieldClicked();
    }//GEN-LAST:event_txtWSProductCodeMouseClicked

    private void txtWSProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWSProductCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtWSProductCodeKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked

	try
	{
	    if (txtItemCode.getText().isEmpty())
	    {
		String itemCode = funGenerateItemCode();
		txtItemCode.setText(itemCode);
	    }
	    if (txtWSProductCode.getText().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Product Code");
		return;
	    }
	    if (txtWSProductName.getText().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please Enter Product Name");
		return;
	    }

	    if (funCheckDuplicateProductCodeandPOSForItem())
	    {
		JOptionPane.showMessageDialog(this, "This product code already used for item from selected pos");
		return;
	    }

	    if (funCheckProductCodeForItem())
	    {
		JOptionPane.showMessageDialog(this, "This product code already used for item from selected pos");
		return;
	    }
	    funInsertItemLinkupRow();
	}
	catch (Exception ex)
	{
	    Logger.getLogger(frmMenuItemMaster.class.getName()).log(Level.SEVERE, null, ex);
	}

    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
	funRemoveRow();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void txtExciseBrandCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtExciseBrandCodeMouseClicked
	// TODO add your handling code here:
	funBrandCodeTextFieldClicked();
    }//GEN-LAST:event_txtExciseBrandCodeMouseClicked

    private void txtExciseBrandCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExciseBrandCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtExciseBrandCodeKeyPressed

    private void cmbOrderTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOrderTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbOrderTypeActionPerformed

    private void cmbOrderTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOrderTypeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbOrderTypeKeyPressed

    private void txtRequiredProductDeliveryDaysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRequiredProductDeliveryDaysMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRequiredProductDeliveryDaysMouseClicked

    private void txtRequiredProductDeliveryDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRequiredProductDeliveryDaysActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRequiredProductDeliveryDaysActionPerformed

    private void txtRequiredProductDeliveryDaysKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRequiredProductDeliveryDaysKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtRequiredProductDeliveryDaysKeyPressed

    private void chkSundayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkSundayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSundayKeyPressed

    private void chkMondayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkMondayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkMondayKeyPressed

    private void chkTuesdayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkTuesdayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkTuesdayKeyPressed

    private void chkWedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkWedKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkWedKeyPressed

    private void chkThursdayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkThursdayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkThursdayKeyPressed

    private void chkFridayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkFridayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkFridayKeyPressed

    private void chkSaturdayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkSaturdayKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSaturdayKeyPressed

    private void btnAddOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddOrderMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtItemCode.getText().isEmpty())
	    {
		String itemCode = funGenerateItemCode();
		txtItemCode.setText(itemCode);
	    }

	    if (cmbOrderType.getSelectedItem().toString().isEmpty())
	    {
		JOptionPane.showMessageDialog(this, "Please fill orderType");
		return;
	    }

	    if (funCheckDuplicateOrderCodeandPOSForItem())
	    {
		JOptionPane.showMessageDialog(this, "This order type already used for selected item ");
		return;
	    }

	    if (funCheckOrderCodeForItem())
	    {
		JOptionPane.showMessageDialog(this, "This order type already used for selected item ");
		return;
	    }

	    funInsertOrderTypeWithItemInRow();

	}
	catch (Exception ex)
	{
	    Logger.getLogger(frmMenuItemMaster.class.getName()).log(Level.SEVERE, null, ex);
	}
    }//GEN-LAST:event_btnAddOrderMouseClicked

    private void btnAddOrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddOrderMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddOrderMouseEntered

    private void btnAddOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddOrderKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddOrderKeyPressed

    private void btnRemoveOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveOrderMouseClicked
	// TODO add your handling code here:
	funRemoveItemWithOrderTypeRow();
    }//GEN-LAST:event_btnRemoveOrderMouseClicked

    private void btnRemoveOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOrderActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveOrderActionPerformed

    private void txtWSProductNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWSProductNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtWSProductNameMouseClicked

    private void txtWSProductNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWSProductNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtWSProductNameKeyPressed

    private void txtItemWeightMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemWeightMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtItemWeightMouseClicked

    private void txtItemWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemWeightActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtItemWeightActionPerformed

    private void txtItemWeightKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemWeightKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtItemWeightKeyPressed

    private void btnLinkupResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinkupResetMouseClicked
	// TODO add your handling code here:
	funResetProductLinkupField();
    }//GEN-LAST:event_btnLinkupResetMouseClicked

    private void btnLinkupResetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinkupResetMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnLinkupResetMouseEntered

    private void btnLinkupResetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLinkupResetKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnLinkupResetKeyPressed

    private void btnOrderingResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrderingResetMouseClicked
	// TODO add your handling code here:
	funResetOrderLinkupField();
    }//GEN-LAST:event_btnOrderingResetMouseClicked

    private void btnOrderingResetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrderingResetMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnOrderingResetMouseEntered

    private void btnOrderingResetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOrderingResetKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnOrderingResetKeyPressed

    private void txtMinItemWeightMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMinItemWeightMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMinItemWeightMouseClicked

    private void txtMinItemWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinItemWeightActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMinItemWeightActionPerformed

    private void txtMinItemWeightKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinItemWeightKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMinItemWeightKeyPressed

    private void tblOrderDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrderDetailsMouseClicked
	// TODO add your handling code here:
	//      funItemWithOrderTypeTableRowSelection();
    }//GEN-LAST:event_tblOrderDetailsMouseClicked

    private void tblCharValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharValueMouseClicked
	// TODO add your handling code here:
	funFillCharValueMap();
    }//GEN-LAST:event_tblCharValueMouseClicked

    private void tblCharactersticsMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharactersticsMasterMouseClicked
	// TODO add your handling code here:
	try
	{
	    funCharMasterTableClicked();
	}
	catch (Exception ex)
	{
	    objUtility.funWriteErrorLog(ex);
	    ex.printStackTrace();
	}
    }//GEN-LAST:event_tblCharactersticsMasterMouseClicked

    private void chkUrgentOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkUrgentOrderKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkUrgentOrderKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Menu Item");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Menu Item");
    }//GEN-LAST:event_formWindowClosing

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
	// TODO add your handling code here:
	try
	{
	    funSelectAllCheckBoxClicked();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void cmbReceivedUOMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbReceivedUOMKeyPressed
    {//GEN-HEADEREND:event_cmbReceivedUOMKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbReceivedUOMKeyPressed

    private void cmbTargetMissedTimeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbTargetMissedTimeKeyPressed
    {//GEN-HEADEREND:event_cmbTargetMissedTimeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbTargetMissedTimeKeyPressed

    private void txtRecipeCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtRecipeCodeMouseClicked
    {//GEN-HEADEREND:event_txtRecipeCodeMouseClicked

    }//GEN-LAST:event_txtRecipeCodeMouseClicked

    private void txtRecipeCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtRecipeCodeKeyPressed
    {//GEN-HEADEREND:event_txtRecipeCodeKeyPressed

    }//GEN-LAST:event_txtRecipeCodeKeyPressed

    private void txtMenuItemCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtMenuItemCodeMouseClicked
    {//GEN-HEADEREND:event_txtMenuItemCodeMouseClicked

    }//GEN-LAST:event_txtMenuItemCodeMouseClicked

    private void txtMenuItemCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtMenuItemCodeKeyPressed
    {//GEN-HEADEREND:event_txtMenuItemCodeKeyPressed

    }//GEN-LAST:event_txtMenuItemCodeKeyPressed

    private void dteFromDateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteFromDateKeyPressed
    {//GEN-HEADEREND:event_dteFromDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    dteToDate.requestFocus();
	}
    }//GEN-LAST:event_dteFromDateKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteToDateKeyPressed
    {//GEN-HEADEREND:event_dteToDateKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtChildItemName.requestFocus();
	}
    }//GEN-LAST:event_dteToDateKeyPressed

    private void txtChildItemNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtChildItemNameMouseClicked
    {//GEN-HEADEREND:event_txtChildItemNameMouseClicked
	// TODO add your handling code here:
	funSelectMenuItem("Child");
    }//GEN-LAST:event_txtChildItemNameMouseClicked

    private void txtChildItemNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtChildItemNameKeyPressed
    {//GEN-HEADEREND:event_txtChildItemNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtQty.requestFocus();
	}
    }//GEN-LAST:event_txtChildItemNameKeyPressed

    private void txtQtyMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtQtyMouseClicked
    {//GEN-HEADEREND:event_txtQtyMouseClicked
	// TODO add your handling code here:
	if (txtQty.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Item Quantity").setVisible(true);
	    txtQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Item Quantity").setVisible(true);
	    txtQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtQtyMouseClicked

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtQtyKeyPressed
    {//GEN-HEADEREND:event_txtQtyKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnAdd.requestFocus();
	}
    }//GEN-LAST:event_txtQtyKeyPressed

    private void btnAddRecipeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAddRecipeMouseClicked
    {//GEN-HEADEREND:event_btnAddRecipeMouseClicked
	// TODO add your handling code here:
	if (txtChildItemName.getText().trim().length() == 0)
	{
	    JOptionPane.showMessageDialog(this, "Please Select Child Item!");
	    return;
	}
	if (!clsGlobalVarClass.validateNumbers(txtQty.getText().trim()))
	{
	    JOptionPane.showMessageDialog(this, "Please Enter Numbers Only in Quantity Field!");
	    txtQty.requestFocus();
	    return;
	}
	if (txtQty.getText().trim().length() == 0)
	{
	    JOptionPane.showMessageDialog(this, "Please Enter Quantity in Quantity Field!");
	    txtQty.requestFocus();
	    return;
	}
	funAddRow(selectedItemCode, txtChildItemName.getText().trim(), txtQty.getText().trim());
    }//GEN-LAST:event_btnAddRecipeMouseClicked

    private void btnRemoveRecipeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnRemoveRecipeMouseClicked
    {//GEN-HEADEREND:event_btnRemoveRecipeMouseClicked
	// TODO add your handling code here:
	funRemoveRecipeRow();
    }//GEN-LAST:event_btnRemoveRecipeMouseClicked

    private void btnResetChildMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnResetChildMouseClicked
    {//GEN-HEADEREND:event_btnResetChildMouseClicked
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetChildMouseClicked

    private void tblChildItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblChildItemsMouseClicked
    {//GEN-HEADEREND:event_tblChildItemsMouseClicked
	// TODO add your handling code here:
	funCheckSelection();
    }//GEN-LAST:event_tblChildItemsMouseClicked

    private void cmbRecipeUOMKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbRecipeUOMKeyPressed
    {//GEN-HEADEREND:event_cmbRecipeUOMKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbRecipeUOMKeyPressed

    private void txtHSNNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHSNNoMouseClicked
    {//GEN-HEADEREND:event_txtHSNNoMouseClicked
	try
	{
	    if (txtHSNNo.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter HSN No.").setVisible(true);
		txtHSNNo.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtHSNNo.getText(), "1", "Enter HSN No.").setVisible(true);
		txtHSNNo.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtHSNNoMouseClicked

    private void txtHSNNoKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHSNNoKeyPressed
    {//GEN-HEADEREND:event_txtHSNNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHSNNoKeyPressed

    private void chkOperationalYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkOperationalYNActionPerformed
    {//GEN-HEADEREND:event_chkOperationalYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkOperationalYNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddOrder;
    private javax.swing.JButton btnAddRecipe;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnItemDetails;
    private javax.swing.JButton btnLinkupReset;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOrderingReset;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnRemoveOrder;
    private javax.swing.JButton btnRemoveRecipe;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetChild;
    private javax.swing.JButton bttImage;
    private javax.swing.JCheckBox chkDiscount;
    private javax.swing.JCheckBox chkFriday;
    private javax.swing.JCheckBox chkItemForSale;
    private javax.swing.JCheckBox chkItemWiseKOTYN;
    private javax.swing.JCheckBox chkMonday;
    private javax.swing.JCheckBox chkOpenItem;
    private javax.swing.JCheckBox chkOperationalYN;
    private javax.swing.JCheckBox chkRawMaterial;
    private javax.swing.JCheckBox chkSaturday;
    private javax.swing.JCheckBox chkSelectAll;
    private javax.swing.JCheckBox chkStkInEnable;
    private javax.swing.JCheckBox chkSunday;
    private javax.swing.JCheckBox chkThursday;
    private javax.swing.JCheckBox chkTuesday;
    private javax.swing.JCheckBox chkUrgentOrder;
    private javax.swing.JCheckBox chkWed;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JComboBox cmbOrderType;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbProcessingDay;
    private javax.swing.JComboBox cmbProcessingTme;
    private javax.swing.JComboBox cmbReceivedUOM;
    private javax.swing.JComboBox cmbRecipeUOM;
    private javax.swing.JComboBox cmbRevenueHead;
    private javax.swing.JComboBox cmbSubGroupCode;
    private javax.swing.JComboBox cmbTargetMissedTime;
    private javax.swing.JComboBox cmbTaxIndicator;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblBarCode1;
    private javax.swing.JLabel lblBarCode2;
    private javax.swing.JLabel lblChildItemName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryDays;
    private javax.swing.JLabel lblExciseBrandCode;
    private javax.swing.JLabel lblExtCode;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHSNNo;
    private javax.swing.JLabel lblItemImage;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblItemType;
    private javax.swing.JLabel lblItemWeight;
    private javax.swing.JLabel lblMaxlevel;
    private javax.swing.JLabel lblMenuItemCode;
    private javax.swing.JLabel lblMenuItemName;
    private javax.swing.JLabel lblMinItemWeight;
    private javax.swing.JLabel lblMinlevel;
    private javax.swing.JLabel lblMinlevel1;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOrderType;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProcessingDay;
    private javax.swing.JLabel lblProcessingTime;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblReceivedConversion;
    private javax.swing.JLabel lblReceivedUOM;
    private javax.swing.JLabel lblRecipeCode;
    private javax.swing.JLabel lblRecipeConversion;
    private javax.swing.JLabel lblRecipeUOM;
    private javax.swing.JLabel lblRequiredProductDays;
    private javax.swing.JLabel lblRevenueHead;
    private javax.swing.JLabel lblShortName;
    private javax.swing.JLabel lblSubGroupCode;
    private javax.swing.JLabel lblTargetMissedTime;
    private javax.swing.JLabel lblTargetMissedTimeMinutes;
    private javax.swing.JLabel lblTaxIndicator;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUomName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWSProductCode;
    private javax.swing.JLabel lblWSProductName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lblitemCode;
    private javax.swing.JLabel lblposname;
    private javax.swing.JPanel panelCharDetails;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelLinkup;
    private javax.swing.JPanel panelOrderDetails;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabMenuItemMaster;
    private javax.swing.JTable tblCharValue;
    private javax.swing.JTable tblCharactersticsMaster;
    private javax.swing.JTable tblChildItems;
    private javax.swing.JTable tblItemCodeLinkup;
    private javax.swing.JTable tblOrderDetails;
    private javax.swing.JTextField txtChildItemName;
    private javax.swing.JTextField txtExciseBrandCode;
    private javax.swing.JTextField txtExtCode;
    private javax.swing.JTextField txtHSNNo;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemImage;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemWeight;
    private javax.swing.JTextField txtMaxlevel;
    private javax.swing.JTextField txtMenuItemCode;
    private javax.swing.JTextField txtMinItemWeight;
    private javax.swing.JTextField txtMinlevel;
    private javax.swing.JTextField txtPurRate;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtReceivedConversion;
    private javax.swing.JTextField txtRecipeCode;
    private javax.swing.JTextField txtRecipeConversion;
    private javax.swing.JTextField txtRequiredProductDeliveryDays;
    private javax.swing.JTextField txtSalePrice;
    private javax.swing.JTextField txtShortName1;
    private javax.swing.JTextField txtWSProductCode;
    private javax.swing.JTextField txtWSProductName;
    // End of variables declaration//GEN-END:variables

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

    /**
     * This method is used to save recipe
     */
    private void funSaveRecipeMaster()
    {
	try
	{
	    long lastNo = funGenerateRecipeCode();
	    String recipeCode = "R" + String.format("%07d", lastNo);
	    String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + (dteFromDate.getDate().getDate());
	    String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + (dteToDate.getDate().getDate());

	    String sqlSaveRecipe = "insert into tblrecipehd (strRecipeCode,strItemCode"
		    + ",dteFromDate,dteToDate,strPOSCode,strUserCreated,strUserEdited,dteDateCreated"
		    + ",dteDateEdited,strClientCode) "
		    + "values('" + recipeCode + "','" + txtMenuItemCode.getText().trim() + "'" + ",'" + fromDate + "','" + toDate + "'"
		    + ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
		    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + ",'" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlSaveRecipe);

	    if (funSaveRecipeDtl(recipeCode) == 1)
	    {
		String sqlInternalUpdate = "update tblinternal set dblLastNo=" + lastNo + " "
			+ "where strTransactionType='Recipe'";
		clsGlobalVarClass.dbMysql.execute(sqlInternalUpdate);
		//JOptionPane.showMessageDialog(this, "Record Saved Successfully.");
		funResetFields();
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
    private void funResetFields()
    {

	funResetChildItemFields();
	funSetFormToInDateChosser();
	dmChildRows.setRowCount(0);
	txtChildItemName.requestFocus();

    }

    /**
     * This method is used to save recipe dtl
     *
     * @param recipeCode
     * @return
     * @throws Exception
     */
    private int funSaveRecipeDtl(String recipeCode) throws Exception
    {
	String sqlDelete = "delete from tblrecipedtl where strRecipeCode='" + recipeCode + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);
	for (int cnt = 0; cnt < tblChildItems.getRowCount(); cnt++)
	{
	    String childItemCode = tblChildItems.getValueAt(cnt, 0).toString();
	    String childItemName = tblChildItems.getValueAt(cnt, 1).toString();
	    String childItemQty = tblChildItems.getValueAt(cnt, 2).toString();

	    String sqlSaveRecipe = "insert into tblrecipedtl (strRecipeCode,strChildItemCode"
		    + ",dblQuantity,strPOSCode,strClientCode,strDataPostFlag) "
		    + "values('" + recipeCode + "','" + childItemCode + "','" + childItemQty + "'"
		    + ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gClientCode + "','N')";
	    clsGlobalVarClass.dbMysql.execute(sqlSaveRecipe);
	}
	return 1;
    }

    /**
     * This method is used to generate recipe code
     *
     * @return
     * @throws Exception
     */
    private long funGenerateRecipeCode() throws Exception
    {
	long lastNo = 0;

	String sqlInternal = "select dblLastNo from tblinternal "
		+ "where strTransactionType='Recipe'";
	ResultSet rsInternal = clsGlobalVarClass.dbMysql.executeResultSet(sqlInternal);
	if (rsInternal.next())
	{
	    lastNo = rsInternal.getLong(1);
	    lastNo++;
	}

	return lastNo;
    }

    /**
     * This method is used to select recipe data
     */
    private void funSelectRecipeData()
    {
	try
	{
	    clsUtility obj = new clsUtility();
	    obj.funCallForSearchForm("Recipe");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		if (data.length == 0)
		{

		}
		else
		{
		    funSetRecipeData(data);
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

    /**
     * This method is used to set recipe data
     *
     * @param data
     * @throws Exception
     */
    private void funSetRecipeData(Object[] data) throws Exception
    {

	txtRecipeCode.setText(data[0].toString());
	String sql = "select a.strItemCode,b.strItemName,dteFromDate,dteToDate "
		+ "from tblrecipehd a,tblitemmaster b "
		+ "where a.strItemCode=b.strItemCode and strRecipeCode='" + data[0].toString() + "'";
	ResultSet rsRecipeHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsRecipeHd.next())
	{
	    txtMenuItemCode.setText(rsRecipeHd.getString(1));
	    lblMenuItemCode.setText(rsRecipeHd.getString(2));

	    String fromDate = rsRecipeHd.getString(3);
	    String[] spFrom = fromDate.split(" ");
	    String[] spFrom1 = spFrom[0].split("-");

	    fromDate = spFrom1[2] + "-" + spFrom1[1] + "-" + spFrom1[0];
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
	    dteFromDate.setDate(date);

	    String toDate = rsRecipeHd.getString(4);
	    String[] spTo = toDate.split(" ");
	    String[] spTo1 = spTo[0].split("-");

	    toDate = spTo1[2] + "-" + spTo1[1] + "-" + spTo1[0];
	    date = new SimpleDateFormat("dd-MM-yyyy").parse(toDate);
	    dteToDate.setDate(date);

	    funSetRecipeDtl(data[0].toString());
	}
    }

    /**
     * This method is used to set recipe dtl
     *
     * @param recipeCode
     * @throws Exception
     */
    private void funSetRecipeDtl(String recipeCode) throws Exception
    {
	dmChildRows.setRowCount(0);
	String sql = "select a.strChildItemCode,a.dblQuantity,b.strItemName "
		+ "from tblrecipedtl a,tblitemmaster b "
		+ "where a.strChildItemCode=b.strItemCode and strRecipeCode='" + recipeCode + "'";
	System.out.println(sql);
	ResultSet rsRecipeDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsRecipeDtl.next())
	{
	    funAddRow(rsRecipeDtl.getString(1), rsRecipeDtl.getString(3), rsRecipeDtl.getString(2));
	}
    }

    /**
     * This method is used to add row
     *
     * @param itemCode
     * @param itemName
     * @param qty
     */
    private void funAddRow(String itemCode, String itemName, String qty)
    {
	try
	{
	    Object[] column = new Object[4];
	    long result;

	    column[0] = itemCode;
	    column[1] = itemName;
	    column[2] = qty;
	    column[3] = false;

	    dmChildRows.addRow(column);
	    tblChildItems.setModel(dmChildRows);
	    tblChildItems.setRowHeight(30);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    tblChildItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

	    tblChildItems.getColumnModel().getColumn(0).setPreferredWidth(0);
	    tblChildItems.getColumnModel().getColumn(1).setPreferredWidth(200);
	    tblChildItems.getColumnModel().getColumn(2).setPreferredWidth(40);
	    tblChildItems.getColumnModel().getColumn(3).setPreferredWidth(20);
	    tblChildItems.setSize(260, 900);

	    funResetChildItemFields();

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to set data chooser
     */
    private void funSetFormToInDateChosser()
    {
	try
	{
	    java.util.Date posDate = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	    dteFromDate.setDate(posDate);
	    dteToDate.setDate(posDate);
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to reset child item fields
     */
    private void funResetChildItemFields()
    {

	txtQty.setText("0");

    }

    /**
     * This method is used to selected menu item
     *
     * @param itemType
     */
    private void funSelectMenuItem(String itemType)
    {
	try
	{
	    if (itemType.equals("Parent"))
	    {
		clsUtility obj = new clsUtility();
		obj.funCallForSearchForm("MenuItemForPrice");
	    }
	    else
	    {
		clsUtility obj = new clsUtility();
		obj.funCallForSearchForm("MenuItemForRecipeChild");
	    }
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetMenuItemData(data, itemType);
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
     * This method is used to set menu item data
     *
     * @param data
     * @param itemType
     */
    private void funSetMenuItemData(Object[] data, String itemType)
    {
	if (itemType.equalsIgnoreCase("Parent"))
	{
	    txtMenuItemCode.setText(data[0].toString());
	    lblMenuItemCode.setText(data[1].toString());
	}
	else
	{
	    txtChildItemName.setText(data[1].toString());
	    selectedItemCode = data[0].toString();
	    txtQty.requestFocus();
	}
    }

    /**
     * This method is used to check selection
     */
    private void funCheckSelection()
    {
	int row = 0;
	boolean flg = false;
	int rowNo = tblChildItems.getSelectedRow();
	String rowValue = tblChildItems.getValueAt(rowNo, 3).toString();
	if (Boolean.parseBoolean(rowValue))
	{
	    btnRemoveRecipe.setEnabled(true);
	}

	boolean flgSelect = false;
	for (int i = 0; i < tblChildItems.getRowCount(); i++)
	{
	    String rowValue1 = tblChildItems.getValueAt(i, 3).toString();
	    if (Boolean.parseBoolean(rowValue1))
	    {
		flgSelect = true;
		break;
	    }
	}
	if (!flgSelect)
	{
	    btnRemoveRecipe.setEnabled(false);
	}
    }

    /**
     * This method is used to update recipe
     */
    private void funUpdateRecipeMaster()
    {
	try
	{
	    String recipeCode = "";
	    String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + (dteFromDate.getDate().getDate());
	    String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + (dteToDate.getDate().getDate());

	    recipeCode = txtRecipeCode.getText().trim();

	    String sqlUpdateRecipe = "update tblrecipehd set "
		    + "strItemCode='" + txtMenuItemCode.getText().trim() + "'"
		    + ",dteFromDate='" + fromDate + "',dteToDate='" + toDate + "'"
		    + ",strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
		    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
		    + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + ",strClientCode='" + clsGlobalVarClass.gClientCode + "',strDataPostFlag='N' "
		    + "where strRecipeCode='" + recipeCode + "'";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdateRecipe);

	    if (funSaveRecipeDtl(txtRecipeCode.getText().trim()) == 1)
	    {
		//JOptionPane.showMessageDialog(this, "Records Updated Successfully.");
		funResetFields();
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used to remove row
     */
    private void funRemoveRecipeRow()
    {
	int rowNo = tblChildItems.getRowCount();
	java.util.Vector vIndexToDelete = new java.util.Vector();
	int rowCount = 0;
	for (int i = 0; i < rowNo; i++)
	{
	    boolean select = Boolean.parseBoolean(tblChildItems.getValueAt(i, 3).toString());
	    if (select)
	    {
		rowCount++;
		vIndexToDelete.add(i);
	    }
	}
	int cnt = 0;
	while (cnt < tblChildItems.getRowCount())
	{
	    boolean select = Boolean.parseBoolean(tblChildItems.getValueAt(cnt, 3).toString());
	    if (select)
	    {
		dmChildRows.removeRow(cnt);
	    }
	    else
	    {
		cnt++;
	    }
	}
	btnRemoveRecipe.setEnabled(false);
    }
}
